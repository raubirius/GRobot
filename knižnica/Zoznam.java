
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

 // Táto trieda bola do verzie 1.85 vnorenou triedou ústrednej triedy GRobot.
 // Po tejto verzii sa osamostatnila a teraz tvorí samostatnú triedu balíčka
 // programového rámca skupiny tried grafického robota.

package knižnica;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

// ---------------------------- //
//  *** Trieda Zoznam<Typ> ***  //
// ---------------------------- //

/**
 * <p>Zoznam je použiteľný na vytvorenie zoznamu ľubovoľných objektov.
 * Trieda obaľuje a rozširuje triedu Javy {@link Vector Vector}{@code 
 * <Typ>}. To znamená, že podobne ako pri pôvodnom vektore, je možné
 * vytvorený zoznam prechádzať konštrukciou:</p>
 * 
 * <pre CLASS="example">
	{@code kwdfor} (Typ prvok : zoznam)
	{
		prvok.metóda(…
		…
	}
	</pre>
 * 
 * <p>Napríklad zoznam reťazcov vytvoríme a prejdeme takto:</p>
 * 
 * <pre CLASS="example">
	{@code kwdfinal} {@code currZoznam}&lt;{@link String String}&gt; zoznamMien = {@code kwdnew} {@link Zoznam#Zoznam() Zoznam}&lt;{@link String String}&gt;();
	zoznamMien.{@link #pridaj(Object) pridaj}({@code srg"Adam"});
	zoznamMien.{@link #pridaj(Object) pridaj}({@code srg"Braňo"});
	zoznamMien.{@link #pridaj(Object) pridaj}({@code srg"Cyril"});
	zoznamMien.{@link #pridaj(Object) pridaj}({@code srg"Daniel"});

	{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Zoznam mien:"});
	{@code kwdfor} ({@link String String} meno : zoznamMien)
	{
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" >"}, meno);
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <pre CLASS="example">
	{@code Zoznam mien:}
	{@code  > Adam}
	{@code  > Braňo}
	{@code  > Cyril}
	{@code  > Daniel}
	</pre>
 * 
 * <p>Zoznamy sú dynamické – prvky je možné ľubovoľne pridávať a odoberať –
 * táto trieda obsahuje širokú skupinu metód pokrývajúcu najdôležitejšie
 * funkcie práce so zoznamom ({@linkplain #pridaj(Object)
 * pridávanie}/&#8203;{@linkplain #vlož(int, Object) vkladanie}, {@linkplain 
 * #odober(Object) odoberanie}, {@linkplain #nahraď(int, Object)
 * nahrádzanie} a {@linkplain #hľadaj(Object) vyhľadávanie}…).
 * Vnútorná správa zoznamu je v réžii Javy. Je dôležité upozorniť na to,
 * že nie je správne meniť zoznam (pridávať alebo odoberať prvky) počas
 * jeho prechádzania. Mohlo by dôjsť k nepredvídateľným vedľajším efektom
 * alebo by to mohlo viesť ku vzniku výnimky.</p>
 * 
 * @see java.util.Vector Vector&lt;E&gt;
 */
@SuppressWarnings("serial")
public class Zoznam<Typ> extends Vector<Typ>
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html

	// Inštancia náhodného generátora
	private final static Random generátor = new Random();

	/**
	 * <p>Táto trieda slúži na vytvorenie obráteného iterátora zoznamu.
	 * Jej použitie je implicitné. Je uvedené napríklad v opise metód
	 * {@link Zoznam#odzadu() Zoznam.odzadu()} a {@link Zoznam#naopak()
	 * Zoznam.naopak()}.</p>
	 * 
	 * <p><b>Použitý zdroj:</b></p>
	 * 
	 * <ul><li><small><a
	 * href="https://stackoverflow.com/users/939/ron-tuffin"
	 * target="_blank">User:Ron Tuffin</a> – <a
	 * href="https://stackoverflow.com/users/99389/nat"
	 * target="_blank">User:Nat</a> – <a
	 * href="https://stackoverflow.com/users/32453/rogerdpack"
	 * target="_blank">User:rogerdpack</a></small>: <a
	 * href="https://stackoverflow.com/questions/1098117/can-one-do-a-for-each-loop-in-java-in-reverse-order"
	 * target="_blank"><em>Can one do a for-each loop in Java in reverse
	 * order?</em> Stack Overflow, 2009, 2016. Citované: 2016.</a></li></ul>
	 * 
	 * @see Zoznam#odzadu()
	 * @see Zoznam#odzadu(List)
	 * @see Zoznam#naopak()
	 * @see Zoznam#naopak(List)
	 */
	public static class ObrátenýIterátor<Typ> implements Iterable<Typ>
	{
		// Ďalší zdroj: https://docs.oracle.com/javase/tutorial/extra/generics/methods.html
		private final List<Typ> pôvodný;

		public ObrátenýIterátor(List<Typ> pôvodný)
		{ this.pôvodný = pôvodný; }

		public Iterator<Typ> iterator()
		{
			final ListIterator<Typ> i =
				pôvodný.listIterator(pôvodný.size());

			return new Iterator<Typ>()
			{
				public boolean hasNext() { return i.hasPrevious(); }
				public Typ next() { return i.previous(); }
				public void remove() { i.remove(); }
			};
		}

		/*public static <Typ> ObrátenýIterátor<Typ> odzadu(List<Typ>
		pôvodný) { return new ObrátenýIterátor<Typ>(pôvodný); }
		public static <Typ> ObrátenýIterátor<Typ> naopak(List<Typ>
		pôvodný) { return new ObrátenýIterátor<Typ>(pôvodný); }*/
	}

	/** <p><a class="alias"></a> Alias pre {@link ObrátenýIterátor ObrátenýIterátor}.</p> */
	public static class ObratenyIterator<Typ> extends
	ObrátenýIterátor<Typ> { public ObratenyIterator(List<Typ> pôvodný)
	{ super(pôvodný); } }

	private int index = 0;
	private boolean prejdenýDokola = false;

	/**
	 * <p>Vytvorí prázdny zoznam.</p>
	 */
	public Zoznam() { super(); }

	/**
	 * <p>Vytvorí zoznam obsahujúci zadané prvky.</p>
	 * 
	 * @param prvky zoznam prvkov oddelený čiarkami
	 */
	// (Preklad nasledujúceho textu do slovenčiny nájdete nižšie.)
	// -------------------------------------------------------------------
	// To suppress: “warning: [unchecked] Possible heap pollution from
	// parameterized vararg type” there are three possibilities:
	//   ∙ @SafeVarargs
	//   ∙ @SuppressWarnings({"unchecked", "varargs"})
	//   ∙ @SuppressWarnings("unchecked")
	// We are using the first one.
	// Source: https://books.google.sk/books?id=TsHVAbd-kpUC&pg=PT114&lpg=
	//   PT114&dq=%22warning:+%5Bunchecked%5D+Possible+heap+pollution+from
	//   +parameterized+vararg+type%22&source=bl&ots=zfwcqYDTYe&sig=rjY7sI
	//   8r9VYHTgKEdsIXmgbqOkA&hl=sk&sa=X&ved=0CEMQ6AEwBWoVChMIq5nygPWOyAI
	//   VRpIsCh3p9QeR#v=onepage&q=%22warning%3A%20%5Bunchecked%5D%20Possi
	//   ble%20heap%20pollution%20from%20parameterized%20vararg%20type%22&
	//   f=false
	// Cite: “We could use the @SuppressWarnings("unchecked") annotation
	//   instead to suppress the warning at the declaration of the method,
	//   but warnings are still generated with their usage. Using
	//   @SafeVarargs suppresses warnings at both places.”
	// -------------------------------------------------------------------
	// Na potlačenie varovania: „[nekontrolované] Možné znečistenie haldy
	// parametrizovaným typom vararg“ sú tri možnosti:
	//   ∙ @SafeVarargs
	//   ∙ @SuppressWarnings({"unchecked", "varargs"})
	//   ∙ @SuppressWarnings("unchecked")
	// My používame prvý z nich.
	// Zdroj: «pozri vyššie»
	// Citácia: „Na potlačenie varovania by sme namiesto toho mohli použiť
	//   anotáciu @SuppressWarnings("unchecked") pri deklarácii metódy,
	//   ale varovania by boli stále generované pri jej použití. Použitím
	//   @SafeVarargs potlačíme varovania na obidvoch miestach.“
	// -------------------------------------------------------------------
	@SafeVarargs
	public Zoznam(Typ... prvky)
	{
		super();
		if (prvky.length > 10) ensureCapacity(prvky.length);
		for (Typ prvok : prvky) super.add(prvok);
	}

	/**
	 * <p>Vytvorí zoznam obsaujúci prvky zadanej kolekcie.</p>
	 * 
	 * @param c kolekcia, ktorej prvky sú použité na naplnenie zoznamu
	 * 
	 * @see java.util.Collection Collection&lt;E&gt;
	 */
	public Zoznam(Collection<? extends Typ> c)
	{
		// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html
		super(c);
	}

	/**
	 * <p>Vytvorí prázdny zoznam s určenou počiatočnou kapacitou. Tento
	 * konštruktor je vhodné použiť ak vieme, že do zoznamu pridáme naraz
	 * veľké množstvo prvkov. Odhadované množstvo prvkov zadáme ako
	 * počiatočnú kapacitu zoznamu.</p>
	 * 
	 * @param počiatočnáKapacita počiatočná {@linkplain #kapacita()
	 *     kapacita} zoznamu
	 */
	public Zoznam(int počiatočnáKapacita) { super(počiatočnáKapacita); }

	/**
	 * <p>Vytvorí prázdny zoznam s určenou počiatočnou kapacitou a zadaným
	 * prírastkom kapacity použitým na zväčšenie kapacity po dosiahnutí
	 * aktuálneho limitu. Tento konštruktor je vhodné použiť ak vieme,
	 * že do zoznamu pridáme naraz veľké množstvo prvkov a objem
	 * pridávaných prvkov sa bude nárazovo meniť. Odhadované množstvo
	 * prvkov zadáme ako počiatočnú kapacitu zoznamu. Prírastok kapacity
	 * je odhad naraz pridávaného objemu prvkov v priebehu bytia
	 * zoznamu.</p>
	 * 
	 * @param počiatočnáKapacita počiatočná {@linkplain #kapacita()
	 *     kapacita} zoznamu
	 * @param prírastokKapacity objem o ktorý sa zväčší kapacita
	 *     zoznamu po prekročení aktuálneho limitu
	 */
	public Zoznam(int počiatočnáKapacita, int prírastokKapacity)
	{ super(počiatočnáKapacita, prírastokKapacity); }


	/**
	 * <p>Vráti aktuálnu kapacitu zoznamu.</p>
	 * 
	 * @return aktuálna kapacita zoznamu (veľkosť vnútorného poľa
	 *     slúžiaceho na uchovávanie prvkov zoznamu – nie je totožná so
	 *     skutočnou {@linkplain #veľkosť() veľkosťou} zoznamu –
	 *     kapacita udáva množstvo prvkov, ktoré môžeme do zoznamu
	 *     pridať bez potreby zväčšenia jeho vnútornej veľkosti
	 *     (pamäťových nárokov); zväčšenie vnútornej veľkosti zoznamu
	 *     vždy zaberá určitý čas…)
	 */
	public int kapacita() { return capacity(); }

	/**
	 * <p>Vráti počet prvkov zoznamu.</p>
	 * 
	 * @return počet prvkov zoznamu
	 */
	public int veľkosť() { return size(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public int počet() { return size(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public int dĺžka() { return size(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public int velkost() { return size(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public int pocet() { return size(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public int dlzka() { return size(); }

	/**
	 * <p>Vráti {@code valtrue} vtedy a len vtedy, keď je zoznam prázdny,
	 * inak vráti {@code valfalse}.</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse}
	 */
	public boolean prázdny() { return isEmpty(); }

	/** <p><a class="alias"></a> Alias pre {@link #prázdny() prázdny}.</p> */
	public boolean prazdny() { return isEmpty(); }

	/** <p><a class="alias"></a> Alias pre {@link #prázdny() prázdny}.</p> */
	public boolean jePrázdny() { return isEmpty(); }

	/** <p><a class="alias"></a> Alias pre {@link #prázdny() prázdny}.</p> */
	public boolean jePrazdny() { return isEmpty(); }


	/**
	 * <p>Pridá zadaný prvok na koniec zoznamu. Dĺžka zoznamu narastie
	 * o jedna a {@linkplain #kapacita() kapacita} sa zvýši ak je
	 * prekročený aktuálny limit.</p>
	 * 
	 * @param prvok prvok, ktorý má byť pridaný na koniec zoznamu
	 */
	public void pridaj(Typ prvok) { addElement(prvok); }

	/**
	 * <p>Pridá prvky zadaného poľa na koniec tohto zoznamu. Metóda sa počas
	 * pridávania jednotlivých prvkov poľa správa rovnako ako metóda {@link 
	 * #pridaj(Object) pridaj}{@code (prvok)}.</p>
	 * 
	 * @param pole pole prvkov, ktoré majú byť pridané na koniec zoznamu
	 */
	public void pridaj(Typ[] pole)
	{
		for (Typ prvok : pole) addElement(prvok);
	}

	/**
	 * <p>Pridá zadaný zoznam na koniec tohto zoznamu podľa poradia určeného
	 * zadaným zoznamom (jeho iterátorom). Výsledok akcie je
	 * nepredvídateľný ak počas operácie dôjde ku zmene zadaného zoznamu.</p>
	 * 
	 * @param inýZoznam zoznam, ktorého prvky majú byť pridané na koniec
	 *     tohto zoznamu
	 */
	public void pridaj(Vector<Typ> inýZoznam)
	{
		if (this == inýZoznam)
		{
			Vector<Typ> kópia = new Vector<>(inýZoznam);
			addAll(kópia);
		}
		else addAll(inýZoznam);
	}


	/**
	 * <p>Vloží prvok na zadanú pozíciu v zozname. Všetky prvky, počnúc
	 * zadanou polohou, sú posunuté o jednu pozíciu vyššie. Veľkosť
	 * zoznamu sa zvýši o jedna. Zadaná poloha musí byť číslo väčšie alebo
	 * rovné nule a menšie alebo rovné veľkosti zoznamu. (Ak je poloha
	 * rovná dĺžke zoznamu, prvok je pridaný na jeho koniec.)</p>
	 * 
	 * @param kde poloha prvku v zozname – poradové číslo väčšie alebo
	 *     rovné nule a menšie alebo rovné veľkosti zoznamu
	 * @param prvok prvok, ktorý má byť vložený na zadanú pozíciu
	 */
	public void vlož(int kde, Typ prvok) { insertElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #vlož(int, java.lang.Object) vlož}.</p> */
	public void vloz(int kde, Typ prvok) { insertElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #vlož(int, java.lang.Object) vlož}.</p> */
	public void pridaj(int kde, Typ prvok) { insertElementAt(prvok, kde); }


	/**
	 * <p>Nahradí prvok na zadanej pozícii zadaným prvkom. Poloha musí byť
	 * číslo väčšie alebo rovné nule a menšie než veľkosť zoznamu. Dĺžka
	 * zoznamu sa nemení, pôvodný prvok je nahradený.</p>
	 * 
	 * @param kde poloha prvku v zozname – poradové číslo väčšie alebo
	 *     rovné nule a menšie než veľkosť zoznamu
	 * @param prvok prvok, ktorým má byť určený prvok nahradený
	 */
	public void prepíš(int kde, Typ prvok) { setElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíš(int, java.lang.Object) prepíš}.</p> */
	public void prepis(int kde, Typ prvok) { setElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíš(int, java.lang.Object) prepíš}.</p> */
	public void nastav(int kde, Typ prvok) { setElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíš(int, java.lang.Object) prepíš}.</p> */
	public void nahraď(int kde, Typ prvok) { setElementAt(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíš(int, java.lang.Object) prepíš}.</p> */
	public void nahrad(int kde, Typ prvok) { setElementAt(prvok, kde); }


	/**
	 * <p>Odstráni všetky prvky zoznamu. Po úspešnom vykonaní tejto metódy
	 * bude zoznam prázdny.</p>
	 */
	public void vymaž() { removeAllElements(); }

	/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
	public void vymaz() { removeAllElements(); }


	/**
	 * <p>Odstráni prvok na zadanej pozícii. Pozícia musí byť číslo väčšie
	 * alebo rovné nule a menšie než veľkosť zoznamu. Každý prvok
	 * nachádzajúci sa nad určeným prvkom je posunutý o jednu pozíciu
	 * nižšie. Veľkosť zoznamu je znížená o jedna.</p>
	 * 
	 * @param kde poloha prvku odstraňovaného zo zoznamu – poradové
	 *     číslo väčšie alebo rovné nule a menšie než veľkosť zoznamu
	 */
	public void odober(int kde) { removeElementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #odober(int) odober}.</p> */
	public void vymaž(int kde) { removeElementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #odober(int) odober}.</p> */
	public void vymaz(int kde) { removeElementAt(kde); }



	/**
	 * <p>Odstráni všetky prvky na pozíciách zadaných rozsahom parametrov
	 * {@code začiatok} (vrátane) a {@code koniec} (okrem neho). Ak je
	 * začiatočná hodnota rovná koncovej, tak nie je vymazaný žiadny prvok.
	 * Veľkosť zoznamu je znížená o počet prvkov nachádzajúcich sa medzi
	 * polohami určenými parametrami {@code začiatok} (vrátane)
	 * a {@code koniec} (okrem nej).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Prirodzeným dôsledkom vymazania je
	 * presunutie prípadných prvkov nachádzajúcich sa na a nad prvkom určeným
	 * parametrom {@code koniec} do radu pri zachovaní ich poradia na pozície
	 * začínajúce sa na polohe určenej parametrom {@code začiatok}.</p>
	 * 
	 * @param začiatok poloha určujúca prvý prvok odstraňovaný zo zoznamu
	 * @param koniec poloha určujúca koniec odstraňovnia prvkov zo zoznamu
	 */
	public void odober(int začiatok, int koniec)
	{ removeRange(začiatok, koniec); }

	/** <p><a class="alias"></a> Alias pre {@link #odober(int) odober}.</p> */
	public void vymaž(int začiatok, int koniec)
	{ odober(začiatok, koniec); }

	/** <p><a class="alias"></a> Alias pre {@link #odober(int) odober}.</p> */
	public void vymaz(int začiatok, int koniec)
	{ odober(začiatok, koniec); }


	/**
	 * <p>Ak sú parametre kladné, tak táto metóda funguje rovnako ako metóda
	 * {@link #odober(int, int) odober(začiatok, koniec)} – odstráni všetky
	 * prvky na pozíciách zadaných rozsahom parametrov {@code začiatok}
	 * (vrátane) a {@code koniec} (okrem neho). V prípade záporných
	 * parametrov sú hodnoty touto metódou upravené tak, aby boli väčšie
	 * alebo rovné nule a to tak, aby výsledný rozsah ukazoval na poradové
	 * čísla od konca zoznamu – pre parameter {@code začiatok} znamená mínus
	 * jednotka posledný prvok zoznamu a pre parameter {@code koniec} znamená
	 * mínus jednotka „koniec zoznamu“ (index rovný dĺžke zoznamu). Ak je
	 * začiatočná hodnota rovná koncovej (pozor na prepočet, vtedy sa hodnoty
	 * rozchádzajú o jedna), tak nie je vymazaný žiadny prvok. Veľkosť zoznamu
	 * je znížená o počet prvkov nachádzajúcich sa medzi polohami určenými
	 * parametrami {@code začiatok} a {@code koniec} (po ich prípadnom
	 * prepočítaní a vrátane prvku na začiatočnej pozícii).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Prirodzeným dôsledkom vymazania je
	 * presunutie prípadných prvkov nachádzajúcich sa na a nad prvkom určeným
	 * parametrom {@code koniec} do radu pri zachovaní ich poradia na pozície
	 * začínajúce sa na polohe určenej parametrom {@code začiatok}.</p>
	 * 
	 * @param začiatok poloha určujúca prvý prvok odstraňovaný zo zoznamu
	 *     (môže byť prepočítaná – pozri opis)
	 * @param koniec poloha určujúca koniec odstraňovnia prvkov zo zoznamu
	 *     (môže byť prepočítaná – pozri opis)
	 */
	public void odoberOdKonca(int začiatok, int koniec)
	{
		if (začiatok < 0) začiatok += size();
		if (koniec < 0) koniec += 1 + size();
		removeRange(začiatok, koniec);
	}

	/** <p><a class="alias"></a> Alias pre {@link #odoberOdKonca(int, int) odoberOdKonca}.</p> */
	public void vymažOdKonca(int začiatok, int koniec)
	{ odoberOdKonca(začiatok, koniec); }

	/** <p><a class="alias"></a> Alias pre {@link #odoberOdKonca(int, int) odoberOdKonca}.</p> */
	public void vymazOdKonca(int začiatok, int koniec)
	{ odoberOdKonca(začiatok, koniec); }


	/**
	 * <p>Odstráni zo zoznamu prvý výskyt prvku zhodného so zadaným
	 * prvkom<sup>[1]</sup>. Ak zoznam taký prvok neobsahuje, zostáva
	 * nezmenený.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * odstráni prvý výskyt elementu v zozname, ktorý pri porovnaní so
	 * zadaným prvkom spĺňa nasledujúcu podmienku: {@code (prvok ==
	 * }{@code valnull }{@code ? element == }{@code valnull }{@code :
	 * prvok.equals(element))} (ak taký element jestvuje);
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), tak sa
	 * metóda pokúsi nájsť a odstrániť prvý prázdny element v zozname,
	 * inak porovnáva obsahy jednotlivých elementov zoznamu a odstráni
	 * ten, ktorého obsah (hodnota) je ekvivalentný s obsahom nami
	 * zadaného prvku.</small></p>
	 * 
	 * @param prvok prvok, ktorý má byť zo zoznamu odstránený (ak sa
	 *     v ňom nachádza)
	 * @return {@code valtrue} ak zoznam zadaný prvok obsahoval
	 */
	public boolean odober(Typ prvok) { return remove(prvok); }


	/**
	 * <p>Zistí, či sa zadaný objekt nachádza v zozname<sup>[1]</sup>.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * vráti logickú hodnotu {@code valtrue} iba v prípade, že aspoň
	 * jeden element zoznamu vyhovuje podmienke
	 * {@code (prvok == }{@code valnull }{@code ? element == }{@code 
	 * valnull }{@code : prvok.equals(element))}, kde {@code prvok} je
	 * nami zadaný prvok a {@code element} je porovnávaný prvok zoznamu
	 * (metóda postupne prechádza jednotlivé elementy zoznamu
	 * a porovnáva ich s nami zadaným prvkom);
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), metóda hľadá
	 * výskyt prázdneho prvku v zozname, inak hľadá taký prvok, ktorého
	 * obsah sa zhoduje s obsahom nami zadaného (neprázdneho)
	 * prvku.</small></p>
	 * 
	 * @param prvok prvok, ktorého výskyt v zozname má byť overený
	 * @return ak bol zadaný prvok v zozname nájdený, vráti sa
	 *     {@code valtrue}
	 */
	public boolean obsahuje(Typ prvok) { return contains(prvok); }


	/**
	 * <p>Vráti hodnotu vnútorného počítadla používaného najmä metódami
	 * {@link #ďalší() ďalší} a {@link #predchádzajúci() predchádzajúci}.
	 * Vnútorné počítadlo používa a nastavuje aj mnoho iných metód triedy
	 * {@link Zoznam Zoznam}.</p>
	 * 
	 * @return hodnota vnútorného počítadla
	 */
	public int počítadlo() { return index; }

	/** <p><a class="alias"></a> Alias pre {@link #počítadlo() počítadlo}.</p> */
	public int pocitadlo() { return index; }

	/**
	 * <p>Nastaví novú hodnotu vnútorného počítadla používaného metódami
	 * {@link #ďalší() ďalší} a {@link #predchádzajúci() predchádzajúci}.
	 * Táto metóda kontroluje hodnotu počítadla tak, aby bola v rozsahu
	 * {@code -}{@code num1} až {@link #veľkosť() veľkosť}{@code ()},
	 * čo sú hraničné hodnoty vhodné na použitie pri cyklickom
	 * prechádzaní zoznamu. Ak chceme prechádzať zoznam od začiatku
	 * metódou {@link #ďalší() ďalší}, je šikovnejšie použiť metódu
	 * {@link #počítadloNaZačiatok() počítadloNaZačiatok} a v prípade
	 * prechádzania zoznamu od konca (metódou {@link #predchádzajúci()
	 * predchádzajúci}) zase metódu {@link #počítadloNaKoniec()
	 * počítadloNaKoniec}.</p>
	 * 
	 * <p>Nasledujúci príklad ukazuje, ako jednorazovo prejsť číselný zoznam
	 * od konca, aj keď na takéto jednorazové prejdenie zoznamu je vhodnejší
	 * iný spôsob – pozri poznámku pod výsledkom príkladu.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@code kwdfinal} {@link Zoznam Zoznam}&lt;{@link Integer Integer}&gt; čísla = {@code kwdnew} {@link Zoznam#Zoznam(Object[]) Zoznam}&lt;{@link Integer Integer}&gt;({@code num0}, {@code num8}, {@code num5}, {@code num4});

		čísla.{@link #počítadloNaKoniec() počítadloNaKoniec}();
		{@link Integer Integer} i = čísla.{@link #predchádzajúciPrvok() predchádzajúciPrvok}();
		{@code kwdwhile} (!čísla.{@link #prejdenýDokola() prejdenýDokola}())
		{
			{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}(i, {@code srg" "});
			i = čísla.{@link #predchádzajúciPrvok() predchádzajúciPrvok}();
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <pre CLASS="example">
		{@code 4 5 8 0 }
		</pre>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Tento spôsob prechádzania zoznamu
	 * je vhodný aj na asynchrónne prechádzanie zoznamu (pohyb po ňom).
	 * Jestvuje ešte jeden spôsob prechádzania zoznamu odzadu a síce
	 * s použitím prevráteného iterátora – pozri metódu {@link #odzadu()
	 * odzadu}.</p>
	 * 
	 * @param nováHodnota nová hodnota vnútorného počítadla
	 */
	public void počítadlo(int nováHodnota)
	{
		if (nováHodnota > size()) nováHodnota = size();
		if (nováHodnota < -1) nováHodnota = -1;
		index = nováHodnota;
	}

	/** <p><a class="alias"></a> Alias pre {@link #počítadlo() počítadlo}.</p> */
	public void pocitadlo(int nováHodnota) { počítadlo(index); }


	/**
	 * <p>Nastaví vnútorné počítadlo zoznamu na polohu pred prvým prvkom
	 * ({@code -}{@code num1}), aby najbližšie volanie metódy
	 * {@link #ďalší() ďalší} vrátilo prvý prvok zoznamu.</p>
	 */
	public void počítadloNaZačiatok() { index = -1; }

	/** <p><a class="alias"></a> Alias pre {@link #počítadloNaZačiatok() počítadloNaZačiatok}.</p> */
	public void pocitadloNaZaciatok() { index = -1; }

	/**
	 * <p>Nastaví vnútorné počítadlo zoznamu na polohu za posledným prvkom
	 * ({@link #veľkosť() veľkosť}{@code ()}), aby najbližšie volanie
	 * metódy {@link #predchádzajúci() predchádzajúci} vrátilo posledný
	 * prvok zoznamu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V opise metódy {@link 
	 * #počítadlo(int) počítadlo} je príklad ukazujúci, ako prejsť
	 * zoznam čísel od konca. Príklad využíva aj túto metódu.</p>
	 */
	public void počítadloNaKoniec() { index = size(); }

	/** <p><a class="alias"></a> Alias pre {@link #počítadloNaKoniec() počítadloNaKoniec}.</p> */
	public void pocitadloNaKoniec() { index = size(); }


	/**
	 * <p>Vráti prvok na zadanej pozícii v zozname. Poloha musí byť číslo
	 * väčšie alebo rovné nule a menšie než veľkosť zoznamu.</p>
	 * 
	 * @param kde poloha prvku v zozname – poradové číslo väčšie alebo
	 *     rovné nule a menšie než veľkosť zoznamu
	 * @return prvok zoznamu
	 */
	public Typ daj(int kde) { return elementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Typ vráť(int kde) { return elementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Typ vrat(int kde) { return elementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Typ čítaj(int kde) { return elementAt(kde); }

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Typ citaj(int kde) { return elementAt(kde); }

	/**
	 * <p>Vráti prvok na pozícii vnútorného {@linkplain #počítadlo()
	 * počítadla} zoznamu. Ak je z neakého dôvodu hodnota vnútorného
	 * počítadla menšia od nuly (a zoznam nie je prázdny), metóda vráti
	 * posledný prvok zoznamu. Ak je naopak hodnota počítadla väčšia
	 * alebo rovná dĺžke zoznamu, vráti prvý prvok zoznamu (opäť
	 * v prípade, že zoznam nie je prázdny, pretože v takom prípade
	 * metóda vrhá výnimku).</p>
	 * 
	 * @return prvok zoznamu
	 * @throws NoSuchElementException
	 */
	public Typ daj()
	{
		if (0 == size()) throw new NoSuchElementException();
		if (index >= size()) return firstElement();
		if (index < 0) return lastElement();
		return elementAt(index);
	}

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ vráť() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ vrat() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ čítaj() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ citaj() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ aktuálny() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ aktualny() { return daj(); }

	/** <p><a class="alias"></a> Alias pre {@link #daj() daj}.</p> */
	public Typ tento() { return daj(); }


	/**
	 * <p>Vráti prvý prvok zoznamu. (Prvok s indexom 0.) Zároveň nastaví
	 * vnútorné {@linkplain #počítadlo() počítadlo} prvkov na nulu.</p>
	 * 
	 * @return prvý prvok zoznamu
	 */
	public Typ prvý() { index = 0; return firstElement(); }

	/** <p><a class="alias"></a> Alias pre {@link #prvý() prvý}.</p> */
	public Typ prvy() { return prvý(); }

	/** <p><a class="alias"></a> Alias pre {@link #prvý() prvý}.</p> */
	public Typ prvýPrvok() { return prvý(); }

	/** <p><a class="alias"></a> Alias pre {@link #prvý() prvý}.</p> */
	public Typ prvyPrvok() { return prvý(); }

	/**
	 * <p>Vráti posledný prvok zoznamu. (Prvok s indexom
	 * <em>«zoznam»</em>{@code .}{@link #veľkosť()
	 * veľkosť}{@code  - }{@code num1}.) Zároveň nastaví vnútorné
	 * {@linkplain #počítadlo() počítadlo} prvkov na index posledného
	 * prvku.</p>
	 * 
	 * @return posledný prvok zoznamu
	 */
	public Typ posledný() { index = size() - 1; return lastElement(); }

	/** <p><a class="alias"></a> Alias pre {@link #posledný() posledný}.</p> */
	public Typ posledny() { return posledný(); }

	/** <p><a class="alias"></a> Alias pre {@link #posledný() posledný}.</p> */
	public Typ poslednýPrvok() { return posledný(); }

	/** <p><a class="alias"></a> Alias pre {@link #posledný() posledný}.</p> */
	public Typ poslednyPrvok() { return posledný(); }


	/**
	 * <p>Vráti ďalší prvok zoznamu. Ak vnútorné {@linkplain #počítadlo()
	 * počítadlo} prvkov prekročilo najvyššiu hodnotu, vráti prvý prvok.
	 * V takej situácii je zoznam považovaný za prejdený dokola a metóda
	 * metóda {@link #prejdenýDokola() prejdenýDokola} vráti {@code 
	 * valtrue}.</p>
	 * 
	 * @return ďalší prvok zoznamu; prvok je určený podľa hodnoty
	 *     vnútorného {@linkplain #počítadlo() počítadla}
	 */
	public Typ ďalší()
	{
		if (0 == size()) throw new NoSuchElementException();
		if (++index >= size() || index < 0)
		{
			prejdenýDokola = true;
			return prvý();
		}
		prejdenýDokola = false;
		return elementAt(index);
	}

	/** <p><a class="alias"></a> Alias pre {@link #ďalší() ďalší}.</p> */
	public Typ dalsi() { return ďalší(); }

	/** <p><a class="alias"></a> Alias pre {@link #ďalší() ďalší}.</p> */
	public Typ ďalšíPrvok() { return ďalší(); }

	/** <p><a class="alias"></a> Alias pre {@link #ďalší() ďalší}.</p> */
	public Typ dalsiPrvok() { return ďalší(); }

	/**
	 * <p>Vráti predchádzajúci prvok zoznamu. Ak vnútorné {@linkplain 
	 * #počítadlo() počítadlo} prvkov kleslo pod najnižšiu hodnotu,
	 * vráti posledný prvok. V takej situácii je zoznam považovaný za
	 * prejdený dokola a metóda metóda {@link #prejdenýDokola()
	 * prejdenýDokola} vráti {@code valtrue}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V opise metódy {@link 
	 * #počítadlo(int) počítadlo} je príklad ukazujúci, ako prejsť
	 * zoznam čísel od konca. Príklad využíva aj túto metódu.</p>
	 * 
	 * @return predchádzajúci prvok zoznamu; prvok je určený podľa hodnoty
	 *     vnútorného {@linkplain #počítadlo() počítadla}
	 */
	public Typ predchádzajúci()
	{
		if (0 == size()) throw new NoSuchElementException();
		if (--index < 0 || index >= size())
		{
			prejdenýDokola = true;
			return posledný();
		}
		prejdenýDokola = false;
		return elementAt(index);
	}

	/** <p><a class="alias"></a> Alias pre {@link #predchádzajúci() predchádzajúci}.</p> */
	public Typ predchadzajuci() { return predchádzajúci(); }

	/** <p><a class="alias"></a> Alias pre {@link #predchádzajúci() predchádzajúci}.</p> */
	public Typ predchádzajúciPrvok() { return predchádzajúci(); }

	/** <p><a class="alias"></a> Alias pre {@link #predchádzajúci() predchádzajúci}.</p> */
	public Typ predchadzajuciPrvok() { return predchádzajúci(); }


	/**
	 * <p>Vráti {@code valtrue}, keď bol zoznam pri poslednom volaní metódy
	 * {@link #ďalší() ďalší} alebo {@link #predchádzajúci() predchádzajúci}
	 * prejdený dokola. Na výslednú hodnotu tejto metódy majú vplyv len
	 * spomenuté dve metódy.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V opise metódy {@link 
	 * #počítadlo(int) počítadlo} je príklad ukazujúci, ako prejsť
	 * zoznam čísel od konca. Príklad využíva aj túto metódu.</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse}
	 */
	public boolean prejdenýDokola() { return prejdenýDokola; }

	/** <p><a class="alias"></a> Alias pre {@link #prejdenýDokola() prejdenýDokola}.</p> */
	public boolean prejdenyDokola() { return prejdenýDokola; }

	/** <p><a class="alias"></a> Alias pre {@link #prejdenýDokola() prejdenýDokola}.</p> */
	public boolean bolPrejdenýDokola() { return prejdenýDokola; }

	/** <p><a class="alias"></a> Alias pre {@link #prejdenýDokola() prejdenýDokola}.</p> */
	public boolean bolPrejdenyDokola() { return prejdenýDokola; }


	/**
	 * <p>Vráti náhodný prvok zoznamu. Zároveň nastaví vnútorné {@linkplain 
	 * #počítadlo() počítadlo} na hodnotu prvku, ktorý bol vrátený.</p>
	 * 
	 * @return náhodný prvok zoznamu
	 */
	public Typ náhodný()
	{
		if (0 == size()) throw new NoSuchElementException();
		return elementAt(index = Math.abs(generátor.nextInt()) % size());
	}

	/** <p><a class="alias"></a> Alias pre {@link #náhodný() náhodný}.</p> */
	public Typ nahodny() { return náhodný(); }

	/** <p><a class="alias"></a> Alias pre {@link #náhodný() náhodný}.</p> */
	public Typ náhodnýPrvok() { return náhodný(); }

	/** <p><a class="alias"></a> Alias pre {@link #náhodný() náhodný}.</p> */
	public Typ nahodnyPrvok() { return náhodný(); }


	/**
	 * <p>Vráti polohu prvého výskytu zadaného prvku v zozname alebo
	 * {@code -}{@code num1} ak prvok nebol v zozname
	 * nájdený<sup>[1]</sup>.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * vráti najnižší index {@code i} pre ktorý platí podmienka
	 * {@code (prvok == }{@code valnull }{@code ? element ==
	 * }{@code valnull }{@code : prvok.equals(element))} (kde
	 * {@code element} je prvok zoznamu na pozícii {@code i}) alebo
	 * {@code -}{@code num1} ak taký index nejestvuje;
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), tak sa
	 * metóda pokúsi nájsť prvý prázdny element v zozname, inak porovnáva
	 * obsahy jednotlivých elementov zoznamu s obsahom nami zadaného
	 * (neprázdneho) prvku.</small></p>
	 * 
	 * @param prvok hľadaný prvok
	 * @return poloha prvku v zozname (hľadá sa prvý výskyt) alebo
	 *     {@code -}{@code num1} ak prvok nebol nájdený
	 */
	public int nájdi(Typ prvok) { return indexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object) nájdi}.</p> */
	public int najdi(Typ prvok) { return indexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object) nájdi}.</p> */
	public int hľadaj(Typ prvok) { return indexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object) nájdi}.</p> */
	public int hladaj(Typ prvok) { return indexOf(prvok); }

	/**
	 * <p>Vráti polohu prvého výskytu zadaného prvku v zozname počnúc
	 * štartovacou pozíciou zadanou v parametri {@code začniOd} alebo
	 * {@code -}{@code num1} ak prvok nebol v zozname
	 * nájdený<sup>[1]</sup>.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * vráti najnižší index {@code i} pre ktorý platí podmienka
	 * {@code (i >= začniOd && prvok == }{@code valnull
	 * }{@code ? element == }{@code valnull }{@code :
	 * prvok.equals(element))} (kde {@code element} je prvok zoznamu na
	 * pozícii {@code i}) alebo {@code -}{@code num1} ak taký index
	 * nejestvuje;
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), tak sa
	 * metóda pokúsi nájsť prvý prázdny element v zozname (počnúc
	 * hľadanie od pozície {@code začniOd}), inak porovnáva obsahy
	 * jednotlivých elementov zoznamu s obsahom nami zadaného
	 * (neprázdneho) prvku.</small></p>
	 * 
	 * @param prvok hľadaný prvok
	 * @param začniOd pozícia v zozname od ktorej sa má začať hľadať
	 * @return poloha prvého prvku v zozname nájdeného na pozícii
	 *     väčšej alebo rovnej hodnote parametra {@code začniOd} alebo
	 *     {@code -}{@code num1} ak prvok nebol nájdený
	 */
	public int nájdi(Typ prvok, int začniOd) { return indexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object, int) nájdi}.</p> */
	public int najdi(Typ prvok, int začniOd) { return indexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object, int) nájdi}.</p> */
	public int hľadaj(Typ prvok, int začniOd)
	{ return indexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdi(java.lang.Object, int) nájdi}.</p> */
	public int hladaj(Typ prvok, int začniOd)
	{ return indexOf(prvok, začniOd); }

	/**
	 * <p>Vráti polohu posledného výskytu zadaného prvku v zozname alebo
	 * {@code -}{@code num1} ak prvok nebol v zozname
	 * nájdený<sup>[1]</sup>.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * vráti najvyšší index {@code i} pre ktorý platí podmienka
	 * {@code (prvok == }{@code valnull }{@code ? element ==
	 * }{@code valnull }{@code : prvok.equals(element))} (kde {@code 
	 * element} je prvok zoznamu na pozícii {@code i}) alebo
	 * {@code -}{@code num1} ak taký index nejestvuje;
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), tak sa
	 * metóda pokúsi nájsť posledný prázdny element v zozname, inak
	 * porovnáva obsahy jednotlivých elementov zoznamu s obsahom nami
	 * zadaného (neprázdneho) prvku.</small></p>
	 * 
	 * @param prvok hľadaný prvok
	 * @return poloha posledného prvku v zozname alebo
	 *     {@code -}{@code num1} ak prvok nebol nájdený
	 */
	public int nájdiPosledný(Typ prvok) { return lastIndexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object) nájdiPosledný}.</p> */
	public int najdiPosledny(Typ prvok) { return lastIndexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object) nájdiPosledný}.</p> */
	public int hľadajOdzadu(Typ prvok) { return lastIndexOf(prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object) nájdiPosledný}.</p> */
	public int hladajOdzadu(Typ prvok) { return lastIndexOf(prvok); }

	/**
	 * <p>Vráti polohu posledného výskytu zadaného prvku v zozname počnúc
	 * štartovacou pozíciou zadanou v parametri {@code začniOd} alebo
	 * {@code -}{@code num1} ak prvok nebol v zozname nájdený.</p>
	 * 
	 * <p><small>[1] – ak chceme byť úplne presní, tak povieme, že metóda
	 * vráti najvyšší index {@code i} pre ktorý platí podmienka
	 * {@code (i <= začniOd && prvok == }{@code valnull
	 * }{@code ? element == }{@code valnull }{@code :
	 * prvok.equals(element))} (kde {@code element} je prvok zoznamu na
	 * pozícii {@code i}) alebo {@code -}{@code num1} ak taký index
	 * nejestvuje;
	 * toto vyjadrenie obsahuje podmienku, ktorá exaktne vyjadruje to,
	 * že ak je nami zadaný prvok prázdny ({@code valnull}), tak sa
	 * metóda pokúsi nájsť posledný prázdny element v zozname (počnúc
	 * hľadanie od pozície {@code začniOd}), inak porovnáva obsahy
	 * jednotlivých elementov zoznamu s obsahom nami zadaného
	 * (neprázdneho) prvku</small></p>
	 * 
	 * @param prvok hľadaný prvok
	 * @param začniOd pozícia v zozname od ktorej sa má začať hľadať
	 * @return poloha posledného prvku v zozname nájdeného na pozícii
	 *     menšej alebo rovnej hodnote parametra {@code začniOd} alebo
	 *     {@code -}{@code num1} ak prvok nebol nájdený (vráti
	 *     {@code -}{@code num1}), ak je parameter {@code začniOd}
	 *     záporný)
	 */
	public int nájdiPosledný(Typ prvok, int začniOd) { return lastIndexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object, int) nájdiPosledný}.</p> */
	public int najdiPosledny(Typ prvok, int začniOd) { return lastIndexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object, int) nájdiPosledný}.</p> */
	public int hľadajOdzadu(Typ prvok, int začniOd)
	{ return lastIndexOf(prvok, začniOd); }

	/** <p><a class="alias"></a> Alias pre {@link #nájdiPosledný(java.lang.Object, int) nájdiPosledný}.</p> */
	public int hladajOdzadu(Typ prvok, int začniOd)
	{ return lastIndexOf(prvok, začniOd); }

	/**
	 * <p>Vymení prvky v zozname nachádzajúce sa na zadaných pozíciách.</p>
	 * 
	 * @param kde1 pozícia (index) prvku
	 * @param kde2 pozícia (index) prvku
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je niektorá zo zadaných
	 *     pozící mimo rozsahu zoznamu
	 * 
	 * @see #vymeň(int, int)
	 * @see #vymeň(java.lang.Object, int)
	 * @see #vymeň(int, java.lang.Object)
	 * @see #vymeň(java.lang.Object, java.lang.Object)
	 */
	public void vymeň(int kde1, int kde2)
	{
		if (kde1 == kde2) return;
		Typ prvok1 = elementAt(kde1);
		Typ prvok2 = elementAt(kde2);
		setElementAt(prvok2, kde1);
		setElementAt(prvok1, kde2);
	}

	/**
	 * <p>Vymení prvky v zozname, z ktorých jeden je určený pozíciou
	 * (indexom) a druhý objektom nachádzajúcim sa v zozname.</p>
	 * 
	 * @param kde pozícia (index) prvku
	 * @param prvok objekt nachádzajúci sa v zozname
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je zadaná pozícia mimo
	 *     rozsahu zoznamu alebo ak sa zadaný prvok v zozname nenachádza
	 * 
	 * @see #vymeň(int, int)
	 * @see #vymeň(java.lang.Object, int)
	 * @see #vymeň(int, java.lang.Object)
	 * @see #vymeň(java.lang.Object, java.lang.Object)
	 */
	public void vymeň(int kde, Typ prvok)
	{
		int i = indexOf(prvok);
		vymeň(kde, i);
	}

	/**
	 * <p>Vymení prvky v zozname, z ktorých jeden je určený pozíciou
	 * (indexom) a druhý objektom nachádzajúcim sa v zozname.</p>
	 * 
	 * @param prvok objekt nachádzajúci sa v zozname
	 * @param kde pozícia (index) prvku
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je zadaná pozícia mimo
	 *     rozsahu zoznamu alebo ak sa zadaný prvok v zozname nenachádza
	 * 
	 * @see #vymeň(int, int)
	 * @see #vymeň(java.lang.Object, int)
	 * @see #vymeň(int, java.lang.Object)
	 * @see #vymeň(java.lang.Object, java.lang.Object)
	 */
	public void vymeň(Typ prvok, int kde)
	{
		int i = indexOf(prvok);
		vymeň(i, kde);
	}

	/**
	 * <p>Vymení prvky zoznamu určené objektami nachádzajúcimi sa
	 * v zozname.</p>
	 * 
	 * @param prvok1 objekt nachádzajúci sa v zozname
	 * @param prvok2 objekt nachádzajúci sa v zozname
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak sa niektorý zo zadaných
	 *     prvkov v zozname nenachádza
	 * 
	 * @see #vymeň(int, int)
	 * @see #vymeň(java.lang.Object, int)
	 * @see #vymeň(int, java.lang.Object)
	 * @see #vymeň(java.lang.Object, java.lang.Object)
	 */
	public void vymeň(Typ prvok1, Typ prvok2)
	{
		int i = indexOf(prvok1);
		int j = indexOf(prvok2);
		vymeň(i, j);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymeň(int, int) vymeň}.</p> */
	public void vymen(int kde1, int kde2) { vymeň(kde1, kde2); }

	/** <p><a class="alias"></a> Alias pre {@link #vymeň(int, java.lang.Object) vymeň}.</p> */
	public void vymen(int kde, Typ prvok) { vymeň(kde, prvok); }

	/** <p><a class="alias"></a> Alias pre {@link #vymeň(java.lang.Object, int) vymeň}.</p> */
	public void vymen(Typ prvok, int kde) { vymeň(prvok, kde); }

	/** <p><a class="alias"></a> Alias pre {@link #vymeň(java.lang.Object, java.lang.Object) vymeň}.</p> */
	public void vymen(Typ prvok1, Typ prvok2) { vymeň(prvok1, prvok2); }


	/**
	 * <p>Presunie prvok zoznamu nachádzajúci sa na zadanej zdrojovej
	 * pozícii ({@code ktorý}) na zadanú cieľovú pozíciu ({@code kam}).</p>
	 * 
	 * @param ktorý zdrojová pozícia (index) prvku
	 * @param kam cieľová pozícia (index) prvku
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je niektorá zo zadaných
	 *     pozící mimo rozsahu zoznamu
	 * 
	 * @see #presuň(int, int)
	 * @see #presuň(java.lang.Object, int)
	 * @see #presuň(int, java.lang.Object)
	 * @see #presuň(java.lang.Object, java.lang.Object)
	 */
	public void presuň(int ktorý, int kam)
	{
		if (ktorý == kam) return;
		Typ element = elementAt(ktorý);
		removeElementAt(ktorý);
		if (kam > ktorý) --kam;
		insertElementAt(element, kam);
	}

	/**
	 * <p>Presunie prvok v zozname z určenej pozície (indexu) pred prvok
	 * učený inštanciou, ktorá sa musí nachádzať v zozname.</p>
	 * 
	 * @param ktorý pozícia (index) prvku
	 * @param pred inštancia nachádzajúca sa v zozname
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je zadaná pozícia mimo
	 *     rozsahu zoznamu alebo ak sa zadaný prvok v zozname nenachádza
	 * 
	 * @see #presuň(int, int)
	 * @see #presuň(java.lang.Object, int)
	 * @see #presuň(int, java.lang.Object)
	 * @see #presuň(java.lang.Object, java.lang.Object)
	 */
	public void presuň(int ktorý, Typ pred)
	{
		int i = indexOf(pred);
		presuň(ktorý, i);
	}

	/**
	 * <p>Presunie prvok zoznamu určený jeho inštanciou na zadanú
	 * pozíciu (index). Zadaný (presúvaný) prvok sa musí nachádzať
	 * v zozname, inak vznikne výnimka.</p>
	 * 
	 * @param ktorý inštancia nachádzajúca sa v zozname
	 * @param kam pozícia (index) prvku
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak je zadaná pozícia mimo
	 *     rozsahu zoznamu alebo ak sa zadaný prvok v zozname nenachádza
	 * 
	 * @see #presuň(int, int)
	 * @see #presuň(java.lang.Object, int)
	 * @see #presuň(int, java.lang.Object)
	 * @see #presuň(java.lang.Object, java.lang.Object)
	 */
	public void presuň(Typ ktorý, int kam)
	{
		int i = indexOf(ktorý);
		presuň(i, kam);
	}

	/**
	 * <p>Presunie prvok zoznamu určený jeho inštanciou pred iný prvok,
	 * tiež určený jeho inštanciou. Obe inštancie sa musia nachádzať
	 * v zozname, inak vznikne výnimka.</p>
	 * 
	 * @param ktorý inštancia nachádzajúca sa v zozname
	 * @param pred inštancia nachádzajúca sa v zozname
	 * 
	 * @throws ArrayIndexOutOfBoundsException ak sa niektorý zo zadaných
	 *     prvkov v zozname nenachádza
	 * 
	 * @see #presuň(int, int)
	 * @see #presuň(java.lang.Object, int)
	 * @see #presuň(int, java.lang.Object)
	 * @see #presuň(java.lang.Object, java.lang.Object)
	 */
	public void presuň(Typ ktorý, Typ pred)
	{
		int i = indexOf(ktorý);
		int j = indexOf(pred);
		presuň(i, j);
	}

	/** <p><a class="alias"></a> Alias pre {@link #presuň(int, int) presuň}.</p> */
	public void presun(int ktorý, int kam) { presuň(ktorý, kam); }

	/** <p><a class="alias"></a> Alias pre {@link #presuň(int, java.lang.Object) presuň}.</p> */
	public void presun(int ktorý, Typ pred) { presuň(ktorý, pred); }

	/** <p><a class="alias"></a> Alias pre {@link #presuň(java.lang.Object, int) presuň}.</p> */
	public void presun(Typ ktorý, int kam) { presuň(ktorý, kam); }

	/** <p><a class="alias"></a> Alias pre {@link #presuň(java.lang.Object, java.lang.Object) presuň}.</p> */
	public void presun(Typ ktorý, Typ pred) { presuň(ktorý, pred); }


	/* *
	 * <p>NEDÁ SA – skúšal som cez nebo aj cez peklo… Toto je v Jave
	 * nemožné/nepovolené – vytvoriť pole parametrického typu je (podľa
	 * všetkých dostupných zdrojov) principiálne nemožné… (Ak by polia mali
	 * niečo ako predvolený povinný konštruktor, ktorý musí byť definovaný
	 * pre každý objekt, tak by sa to asi dalo, ale podľa toho, čo som sa
	 * dočítal, by pri konštrukcii poľa parametrického typu bola určitá
	 * informačná neistota, ktorá sa nedá nijako prekonať. Presne z toho
	 * dôvodu nemá ani origiálna trieda tento druh metódy, ale má metódu
	 * prijímajúcu vytvorené pole zadaného údajového typu. – Zadaného,
	 * v zmysle konkrétneho. Deklarovaná je síce s parametrickým poľom,
	 * ale to je len formálna deklarácia, v realite to už musí byť
	 * konkrétny typ zhodný s typom parametra zoznamu, vektora…)</p>
	 * 
	 * @return —
	 * /
	public Typ[] naPole()
	{
		/*Class<Typ> typ = null;
		Typ[] pole = (Typ[])java.lang.reflect.
			Array.newInstance(typ, size());
		return toArray(pole);*/
		/*@SuppressWarnings("unchecked")
		Typ[] pole = toArray();
		return pole;*/
		// Typ[] pole = new Typ[0];

		/*Object[] objekty = new Object[size()];
			int i = 0;
			for (Typ prvok : this)
			{
				objekty[i] = prvok;
				++i;
			}

		@SuppressWarnings("unchecked")
		Typ[] pole = (Typ[])objekty;
		return pole;* /

		return (Typ[])toArray();
	}*/


	/**
	 * <p>Vytvorí pre tento zoznam prevrátený iterátor. To znamená, že
	 * nasledujúci cyklus:</p>
	 * 
	 * <pre CLASS="example">
		{@link Zoznam Zoznam}&lt;{@link String String}&gt; zoznam = …
		{@code kwdfor} ({@link String String} prvok : zoznam.{@code currodzadu}()) …
		</pre>
	 * 
	 * <p>bude vykonaný pre každý prvok zoznamu začnúc od posledného
	 * a končiac prvým.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Tento spôsob prechádzania zoznamu
	 * je vhodný na jednorazové (synchrónne) prejdenie zoznamu. Na
	 * asynchrónne prechádzanie zoznamu (pohyb po ňom) je vhodnejšie použiť
	 * vnútorné počítadlo zoznamu – pozri metódu {@link #počítadlo(int)
	 * počítadlo}.</p>
	 * 
	 * @param pôvodný pôvodný zoznam
	 * @return iterátor umožňujúci prejsť zoznam v obrátenom poradí
	 */
	public ObrátenýIterátor<Typ> odzadu()
	{ return new ObrátenýIterátor<Typ>(this); }

	/**
	 * <p>Vytvorí pre zadaný zoznam prevrátený iterátor. To znamená, že
	 * nasledujúci cyklus:</p>
	 * 
	 * <pre CLASS="example">
		{@link Zoznam Zoznam}&lt;{@link String String}&gt; zoznam = …
		{@code kwdfor} ({@link String String} prvok : Zoznam.&lt;{@link String String}&gt;{@code currodzadu}(zoznam)) …
		</pre>
	 * 
	 * <p>bude vykonaný pre každý prvok zoznamu začnúc od posledného
	 * a končiac prvým. Jednoduchšie je použitie metódy
	 * {@link #odzadu() odzadu}{@code ()}, ale tá je použiteľná len
	 * pre inštancie triedy {@link Zoznam Zoznam}. Táto metóda je
	 * použiteľná pre ľubovoľný zoznam, ktorý je implementáciou
	 * rozhrania {@link java.util.List List}.</p>
	 * 
	 * @param pôvodný pôvodný zoznam
	 * @return iterátor umožňujúci prejsť zoznam v obrátenom poradí
	 * 
	 * @see #odzadu()
	 */
	public static <Typ> ObrátenýIterátor<Typ> odzadu(List<Typ> pôvodný)
	{ return new ObrátenýIterátor<Typ>(pôvodný); }

	/**
	 * <p>Vytvorí pre tento zoznam prevrátený iterátor. To znamená, že
	 * nasledujúci cyklus:</p>
	 * 
	 * <pre CLASS="example">
		{@link Zoznam Zoznam}&lt;{@link String String}&gt; zoznam = …
		{@code kwdfor} ({@link String String} prvok : zoznam.{@code currnaopak}()) …
		</pre>
	 * 
	 * <p>bude vykonaný pre každý prvok zoznamu začnúc od posledného
	 * a končiac prvým.</p>
	 * 
	 * @param pôvodný pôvodný zoznam
	 * @return iterátor umožňujúci prejsť zoznam v obrátenom poradí
	 */
	public ObrátenýIterátor<Typ> naopak()
	{ return new ObrátenýIterátor<Typ>(this); }

	/**
	 * <p>Vytvorí pre zadaný zoznam prevrátený iterátor. To znamená, že
	 * nasledujúci cyklus:</p>
	 * 
	 * <pre CLASS="example">
		{@link Zoznam Zoznam}&lt;{@link String String}&gt; zoznam = …
		{@code kwdfor} ({@link String String} prvok : Zoznam.&lt;{@link String String}&gt;{@code currnaopak}(zoznam)) …
		</pre>
	 * 
	 * <p>bude vykonaný pre každý prvok zoznamu začnúc od posledného
	 * a končiac prvým. Jednoduchšie je použitie metódy
	 * {@link #naopak() naopak}{@code ()}, ale tá je použiteľná len
	 * pre inštancie triedy {@link Zoznam Zoznam}. Táto metóda je
	 * použiteľná pre ľubovoľný zoznam, ktorý je implementáciou
	 * rozhrania {@link java.util.List List}.</p>
	 * 
	 * @param pôvodný pôvodný zoznam
	 * @return iterátor umožňujúci prejsť zoznam v obrátenom poradí
	 * 
	 * @see #naopak()
	 */
	public static <Typ> ObrátenýIterátor<Typ> naopak(List<Typ> pôvodný)
	{ return new ObrátenýIterátor<Typ>(pôvodný); }
}
