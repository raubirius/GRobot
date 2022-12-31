
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2022 by Roman Horváth
 // 
 // This program is free software: you can redistribute it and/or modify
 // it under the terms of the GNU General Public License as published by
 // the Free Software Foundation, either version 3 of the License, or
 // (at your option) any later version.
 // 
 // This program is distributed in the hope that it will be useful,
 // but WITHOUT ANY WARRANTY; without even the implied warranty of
 // MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 // GNU General Public License for more details.
 // 
 // You should have received a copy of the GNU General Public License
 // along with this program (look for the file named license.txt). If not,
 // see ⟨http://www.gnu.org/licenses/⟩ or
 // ⟨https://www.gnu.org/licenses/gpl-3.0.txt⟩.
 // 
 // In case of any questions or requests, please, contact the author
 // Roman Horváth by e-mail: roman.horvath@truni.sk
 // or horvath.roman.sk@gmail.com.
 // 

package knižnica;

import knižnica.Zoznam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Táto trieda zoskupuje a zjednodušuje prácu s regulárnymi výrazmi.
 * V podstate ide o zaobalenie práce triedami {@link Pattern Pattern}
 * a {@link Matcher Matcher}. Okrem toho implementuje jedno z častých
 * použití regulárnych výrazov – aplikovanie zoznamu filtrov na zadaný
 * reťazec.</p>
 * 
 * <p>Táto trieda reprezentuje {@linkplain Zoznam zoznam} filtrov, ktoré
 * budú postupne aplikované na reťazec zadaný ako parameter metódy {@link 
 * #použi(String) použi}. Každý z filtrov môže mať nastavený príznak {@link 
 * Filter#zastav zastav}, ktorého hodnota {@code valtrue} indikuje, že sa
 * má spracovanie po úspešnej aplikácii tohto filtra (ako prvku v zozname
 * filtrov) zastaviť (a nemá sa pokračovať ďalším filtrom v rámci zoznamu).</p>
 * 
 * <!-- TODO: Vhodný príklad použitia -->
 * 
 * @see Zoznam
 * @see Filtre.Filter
 */
@SuppressWarnings("serial")
public class Filtre extends Zoznam<Filtre.Filter>
{
	/**
	 * <p>Táto vnorená trieda je trieda prvkov zoznamu triedy {@link Filtre
	 * Filtre} a zároveň môže slúžiť na sprostredkovanie jednoduchších úkonov
	 * s regulárnymi výrazmi.</p>
	 * 
	 * @see Filtre
	 */
	public static class Filter
	{
		/**
		 * <p>Do tohto atribútu sa ukladá inštancia vzoru vytvoreného podľa
		 * parametra {@code vzor} vloženého do {@linkplain 
		 * Filtre.Filter#Filtre.Filter(String, String) konštruktora}. (Atribút
		 * je dostupný na čítanie aj zápis. Ak sa jeho obsah zmení, filter
		 * bude pracovať so zmeneným obsahom.)</p>
		 */
		public Pattern vzor;

		/**
		 * <p>Tento atribút obsahuje šablónu nahrádzania vloženú do parametra
		 * {@linkplain Filtre.Filter#Filtre.Filter(String, String)
		 * konštruktora}{@code nahradenie}.</p>
		 */
		public String nahradenie;

		/**
		 * <p>Tento atribút je používaný v rámci nadradenej triedy {@link 
		 * Filtre Filtre}. Metóda {@link Filtre#použi(String)
		 * Filtre.použi(reťazec)} ho používa na overenie toho, či sa má
		 * proces fitrovania zastaviť po úspešnej aplikácii tohto filtra.
		 * (Pozri aj opis metódy {@link Filtre#čítaj(String)
		 * Filtre.čítaj(reťazec)}.)</p>
		 */
		public boolean zastav = false;

		/**
		 * <p>Tento atribút je naplnený po volaní metód {@link #zhoda(String)
		 * zhoda}, {@link #nahradenie(String) nahradenie} a {@link 
		 * Filtre#použi(String) Filtre.použi(reťazec)}. Ak ho nenaplníme sami
		 * iným spôsobom, tak bude jeho hodnota rovná {@code valnull}.</p>
		 */
		public Matcher zhoda = null;

		/**
		 * <p>Konštruktor filtra. Parameter {@code vzor} musí obsahovať
		 * platný regulárny výraz, inak vznikne výnimka. Pozri aj metódu:
		 * {@link Pattern Pattern}{@code .}{Pattern#compile(String)
		 * compile}{@code (regex)}.</p>
		 * 
		 * @param vzor regulárny výraz na porovnávanie reťazcov (na ktoré bude
		 *     aplikovaný tento filter)
		 * @param nahradenie šablóna reťazca na nahrádzanie zhôd podľa
		 *     regulárneho výrazu v parametri {@code vzor}
		 */
		public Filter(String vzor, String nahradenie)
		{
			this.vzor = Pattern.compile(vzor);
			this.nahradenie = nahradenie;
		}

		/**
		 * <p>Porovná zadaný reťazec so vzorom zadávaným do {@linkplain 
		 * Filtre.Filter#Filtre.Filter(String, String) konštruktora} a vráti
		 * zodpovedajúci objekt {@link Matcher Matcher}.</p>
		 * 
		 * @param reťazec reťazec na porovnanie podľa vzoru (regulárneho
		 *     výrazu) tohto filtra
		 * @return objekt {@link Matcher Matcher}, ktorý dovoľuje overiť, či
		 *     zadaný reťazec alebo jeho časť vyhovuje vzoru tohto filtra
		 */
		public Matcher zhoda(String reťazec)
		{
			return zhoda = vzor.matcher(reťazec);
		}

		/**
		 * <p>Nahradí všetky časti zadaného reťazca, ktoré vyhovujú vzoru
		 * (regulárnemu výrazu) tohto filtra šablónou zadanou do konštruktora.
		 * Pozri {@link Filtre.Filter#Filtre.Filter(String, String)
		 * Filter}{@code (vzor, nahradenie)}. Výsledok nahrádzania vráti
		 * v návratovej hodnote.</p>
		 * 
		 * @param reťazec reťazec, v ktorom sa budú hľadať zhody podľa vzoru
		 *     (regulárneho výrazu), ktoré budú nahradené podľa šablóny
		 *     nahrádzania zadanej do konštruktora tohto filtra
		 * @return výsledok nahrádzania (ak bolo nejaké vykonané; ak nebolo
		 *     vykonané žiadne nahradenie, tak metóda vráti pôvodný tvar
		 *     reťazca)
		 */
		public String nahradenie(String reťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.find()) return zhoda.replaceAll(nahradenie);
			return reťazec;
		}

		/**
		 * <p>Nahradí celý zadaný reťazec šablónou toho filtra (zadanou do
		 * konštruktora) ak ako celok vyhovuje vzoru (regulárnemu výrazu)
		 * tohto filtra.
		 * (Pozri aj vzor a nahradenie v konštruktore: {@link 
		 * Filtre.Filter#Filtre.Filter(String, String) Filter}{@code (vzor,
		 * nahradenie)}.)
		 * Ak reťazec vyhovie filtru, tak metóda vráti v návratovej hodnote
		 * výsledok nahrádzania. V opačnom prídade vráti zadaný náhradný
		 * reťazec.</p>
		 * 
		 * @param reťazec reťazec, ktorý bude porovnaný s regulárnym výrazom
		 *     vzoru filtra ako celok a v prípade zhody nahradený podľa šablóny
		 *     nahrádzania (zadanej do konštruktora tohto filtra)
		 * @param náhradnýReťazec náhradný reťazec, ktorý bude vrátený
		 *     v prípade, že tento reťazec ako celok nevyhovie vzoru filtra
		 * @return výsledok spracovania (podľa pravidiel opísaných vyššie)
		 */
		public String nahradenieCelku(String reťazec, String náhradnýReťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.matches()) return zhoda.replaceFirst(nahradenie);
			return náhradnýReťazec;
		}

		/**
		 * <p>Funguje rovnako ako metóda {@link #nahradenieCelku(String,
		 * String) nahradenieCelku}{@code (reťazec, náhradnýReťazec)}, ale
		 * namiesto náhradného reťazca dosadí pôvodný reťazec.</p>
		 * 
		 * @param reťazec reťazec, ktorý bude porovnaný s regulárnym výrazom
		 *     vzoru filtra ako celok a v prípade zhody nahradený podľa šablóny
		 *     nahrádzania (zadanej do konštruktora tohto filtra)
		 * @return výsledok spracovania
		 */
		public String nahradenieCelku(String reťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.matches()) return zhoda.replaceFirst(nahradenie);
			return reťazec;
		}
	}


	// Atribúty pomáhajúce pri parsovaní riadkov:
	private String riadokParsovania = null, vzorParsovania = null;

	// Súkromná metóda slúžiaca na parsovanie riadkov čítaných zo súboru
	// alebo extrahovaných zo zadaného bloku (konfiguračného) textu.
	private int pridajRiadokParsovania()
	{
		int početChýb = 0;
		if (null == vzorParsovania)
		{
			if (riadokParsovania.equals("*"))
			{
				if (0 != size())
				{
					Filter filter = posledný();
					filter.zastav = true;
				}
				else ++početChýb;
			}
			else if (!riadokParsovania.isEmpty())
				vzorParsovania = riadokParsovania;
		}
		else
		{
			try
			{
				pridaj(new Filter(vzorParsovania, riadokParsovania));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				// GRobotException.vypíšChybovéHlásenia(t);
				++početChýb;
			}
			vzorParsovania = null;
		}
		return početChýb;
	}


	/**
	 * <p>Predvolený konštruktor – vytvorí prázdny zoznam filtrov.</p>
	 */
	public Filtre() { super(); }

	/**
	 * <p>Vytvorí zoznam filtrov obsahujúci zadané filtre.</p>
	 * 
	 * @param filtre zoznam filtrov oddelený čiarkami
	 */
	public Filtre(Filter... filtre) { super(filtre); }


	/**
	 * <p>Prečíta zoznam filtrov zo zadaného konfiguračného súboru. Metóda
	 * očakáva v súbore zoradené dvojice alebo trojice riadkov s významom
	 * „regulárny výraz“ (vzor), „nahradenie“ (šablóna) a nepovinný riadok
	 * príznaku zastavenia spracovania. Dvojice/trojice riadkov môžu byť
	 * oddelené ľubovoľným množstvom prázdnych riadkov.</p>
	 * 
	 * <p>Fungovanie metódy je zariadené tak, aby prvý neprázdny riadok bol
	 * považovaný za regulárny výraz, ktorý označuje filter a ďalší tesne za
	 * ním nasledujúci riadok za nahradenie. Nahradenie môže byť aj prázdne
	 * (keď chceme, aby filter obsah zadaného reťazca úplne vymazal). Ak je
	 * na samostatnom riadku nájdená hviezdička, tak tá nesie význam príznaku
	 * zastavenia a použije sa na posledný nad ňou definovaný filter.</p>
	 * 
	 * <p>Príznak zastavenia indikuje, že proces filtrovania sa má po úspešnom
	 * spracovaní filtra, ktorý má tento príznak nastavený zastaviť (t. j., že
	 * sa nemá pokračovať ďalšími filtrami v zozname). Keby mali tento príznak
	 * nastavený všetky filtre v zozname, tak sa spracovanie zastaví po prvom
	 * úspešnom spracovaní reťazca niektorým z filtrov.</p>
	 * 
	 * <!-- p><b>Príklady zoznamov filtrov:</b></p -->
	 * 
	 * <!-- pre CLASS="example" -->
		<!-- TODO -->
		<!-- /pre -->
	 * 
	 * <!-- hr / -->
	 * 
	 * <!-- pre CLASS="example" -->
		<!-- TODO -->
		<!-- /pre -->
	 * 
	 * <!-- hr / -->
	 * 
	 * <!-- pre CLASS="example" -->
		<!-- TODO -->
		<!-- /pre -->
	 * 
	 * <!-- hr / -->
	 * 
	 * <p class="remark"><b>Poznámka:</b> Na indikáciu toho, že aktuálny
	 * riadok neoznačuje začiatok nového filtra, ale vzťahuje sa k poslednému
	 * filtru a mení jeho nastavenie bol úmyselne vybraný taký reťazec, aby
	 * regulárny výraz, ktorý by mal oznaćovať nový filtre bol nezmyselný –
	 * samostatná {@code *} by znamenala „ľubovoľné opakovanie ničoho.“</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Počet úspešne pridaných filtrov
	 * zodpovedá zmene veľkosti zoznamu filtrov pred a po volaní tejto
	 * metódy. (Použite napríklad odvodenú metódu {@link Zoznam#veľkosť()
	 * veľkosť})</p>
	 * 
	 * @param názovSúboru názov súboru, z ktorého majú byť prečítané definície
	 *     filtrov
	 * @return počet chýb zaznamenaných pri čítaní filtrov alebo −1, ak súbor
	 *     nejestvuje
	 */
	public int čítaj(String názovSúboru)
	{
		if (!Súbor.jestvuje(názovSúboru)) return -1;

		Súbor súbor = new Súbor();
		int početChýb = 0;

		try
		{
			súbor.otvorNaČítanie(názovSúboru);
			vzorParsovania = null;

			while (null != (riadokParsovania = súbor.čítajRiadok()))
				početChýb += pridajRiadokParsovania();

			súbor.zavri();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			// GRobotException.vypíšChybovéHlásenia(t);
			++početChýb;
		}

		return početChýb;
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public int citaj(String názovSúboru) { return čítaj(názovSúboru); }


	/**
	 * <p>Pridá filtre podľa zadaného bloku konfiguračného textu. Blok textu
	 * je rozdelený na riadky, ktoré sú spracúvané rovnako ako keby boli
	 * prečítané zo súboru: pozri opis metódy {@link #čítaj(String) čítaj}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Počet úspešne pridaných filtrov
	 * zodpovedá zmene veľkosti zoznamu filtrov pred a po volaní tejto
	 * metódy. (Použite napríklad odvodenú metódu {@link Zoznam#veľkosť()
	 * veľkosť})</p>
	 * 
	 * @param blokTextu blok konfiguračného textu obsahujúci definície filtrov
	 * @return počet chýb zaznamenaných pri parsovaní zadaného reťazca;
	 *     −1 ak bol zadaný nulový parameter ({@code valnull}),
	 *     −2 ak bol zadaný prázdny reťazec ({@code srg""} alebo
	 *     {@code srg"\u005CuFEFF"} – BOM)
	 */
	public int parsuj(String blokTextu)
	{
		if (null == blokTextu) return -1;
		if (blokTextu.isEmpty()) return -2;

		int začiatok = 0;
		int koniec = blokTextu.length();

		if ('\uFEFF' == blokTextu.charAt(0))
		{
			if (1 == koniec) return -2;
			++začiatok;
		}

		int početChýb = 0;
		vzorParsovania = null;

		for (int i = začiatok; i < koniec; ++i)
		{
			char znak = blokTextu.charAt(i);
			if ('\r' == znak)
			{
				riadokParsovania = blokTextu.substring(začiatok, i);
				početChýb += pridajRiadokParsovania();
				if (i + 1 < koniec && '\n' == blokTextu.charAt(i + 1)) ++i;
				začiatok = i + 1;
			}
			else if ('\n' == znak)
			{
				riadokParsovania = blokTextu.substring(začiatok, i);
				početChýb += pridajRiadokParsovania();
				if (i + 1 < koniec && '\r' == blokTextu.charAt(i + 1)) ++i;
				začiatok = i + 1;
			}
		}

		if (začiatok < koniec)
		{
			riadokParsovania = blokTextu.substring(začiatok, koniec);
			početChýb += pridajRiadokParsovania();
		}

		return početChýb;
	}


	/**
	 * <p>Táto metóda slúži na pridanie nového filtra do tohto zoznamu
	 * filtrov, ktorý bude vytvorený podľa zadaných parametrov (vzoru
	 * a zhody). V rámci tohto procesu je volaný konštruktor filtra, takže
	 * platia rovnaké informácie ako v jeho opise: {@code Filter.}{@link 
	 * Filtre.Filter#Filtre.Filter(String, String) Filter}{@code (vzor,
	 * nahradenie)}.</p>
	 * 
	 * @param vzor regulárny výraz na porovnávanie reťazcov (ktoré budú
	 *     odovzdávané tomuto zoznamu filtrov prostredníctvom metódy {@link 
	 *     #použi(String) použi}
	 * @param nahradenie šablóna reťazca na nahrádzanie zhôd podľa regulárneho
	 *     výrazu v parametri {@code vzor}
	 */
	public void pridaj(String vzor, String nahradenie)
	{
		pridaj(new Filter(vzor, nahradenie));
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu ku vzoru filtra.
	 * Rovnaký efekt sa dá dosiahnuť volaním:
	 * {@link #daj(int) daj}{@code (i).}{@link Filter#vzor vzor}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia vzoru i-teho filtra ({@link Pattern Pattern})
	 */
	public Pattern dajVzor(int i)
	{
		return daj(i).vzor;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k šablóne nahradenia
	 * filtra. Rovnaký efekt sa dá dosiahnuť volaním:
	 * {@link #daj(int) daj}{@code (i).}{@link Filter#nahradenie
	 * nahradenie}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia nahradenia i-teho filtra ({@link Pattern Pattern})
	 */
	public String dajNahradenie(int i)
	{
		return daj(i).nahradenie;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k inštancii zhody
	 * filtra ({@link Matcher Matcher}). Rovnaký efekt sa dá dosiahnuť
	 * volaním: {@link #daj(int) daj}{@code (i).}{@link Filter#zhoda zhoda}.
	 * Z toho vyplýva, že platia rovnaké pravidlá, aké sú uvedené v opise
	 * atribútu filtra {@link Filter#zhoda zhoda}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia zhody i-teho filtra ({@link Matcher Matcher})
	 */
	public Matcher dajZhodu(int i)
	{
		return daj(i).zhoda;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k metóde
	 * porovnávania zhody i-teho filtra so zadaným reťazcom. Rovnaký efekt sa
	 * dá dosiahnuť volaním metódy: {@link #daj(int) daj}{@code (i).}{@link 
	 * Filter#zhoda(String) zhoda}{@code (reťazec)}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return nová inštancia zhody i-teho filtra ({@link Matcher Matcher})
	 *     vytvorená podľa pravidiel opísaných v metóde filtra {@link 
	 *     Filter#zhoda(String) zhoda}
	 */
	public Matcher dajZhodu(int i, String reťazec)
	{
		return daj(i).zhoda(reťazec);
	}


	/**
	 * <p>Použije na zadaný reťazec všetky filtre tejto inštancie. Ak má
	 * niektorý z filtrov v zozname nastavený príznak {@link Filter#zastav
	 * zastav}, tak sa spracovanie po úspešnom použití tohto filtra
	 * automaticky zastaví.</p>
	 * 
	 * @param reťazec reťazec na spracovanie
	 * @return spracovaný reťazec, ak bola nájdená aspoň jedna zhoda v tomto
	 *     zozname filtrov alebo pôvodný reťazec, ak jeho obsah nevyhovel ani
	 *     jednej definícii tohto zoznamu
	 */
	public String použi(String reťazec)
	{
		for (Filter filter : this)
		{
			filter.zhoda = filter.vzor.matcher(reťazec);
			if (filter.zhoda.find())
			{
				reťazec = filter.zhoda.replaceAll(filter.nahradenie);
				if (filter.zastav) break;
			}
		}
		return reťazec;
	}

	/** <p><a class="alias"></a> Alias pre {@link #použi(String) použi}.</p> */
	public String pouzi(String reťazec) { return použi(reťazec); }
}
