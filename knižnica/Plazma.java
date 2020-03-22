
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2020 by Roman Horváth
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

import java.awt.Color;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * <p>Táto trieda generuje pixelovú simuláciu horiacej plazmy v zadanom
 * obrázku. Obrázok, ktorý je povinným parametrom konštruktora bude
 * aktivitou tejto triedy celoplošne prepisovaný, preto sa v podstate stáva
 * nepoužiteľným na iné účely. (Teoreticky je možné upraviť obsah obrázka
 * po každej aktivite generátora plazmy, prakticky však býva zväčša efektívne
 * iné riešenie.)</p>
 * 
 * <p>Táto trieda v skutočnosti implementuje dej. Mala by sa teda skôr volať
 * „Plazmovanie“ (podobne ako trieda {@link Vlnenie Vlnenie}), avšak také
 * sloveso slovenčina nerozoznáva, tak je pomenovaná podľa skupenstva, ktoré
 * je dynamické a neustále sa meniace. Primárnym účelom bolo simulovať oheň,
 * ale rôznymi úpravami parametrov simulácie sa dajú docieliť aj také efekty,
 * ktoré oheň nepripomínajú, preto trieda nesie ten názov, ktorý má.</p>
 * 
 * <p>Táto <b>pixelová simulácia</b> neustále prepočítava všetky pixely
 * podľa hodnôt svojich susedov. Cieľom je napodobniť spotrebúvanie
 * určitej „energie,“ ktoré je vizualizované prostredníctvom {@linkplain 
 * #nastavPaletu(Color[], int[]) palety farieb} v pridruženom {@linkplain 
 * Obrázok obrázku}. Dej sa dá ovplyvniť nasledujúcimi parametrami:</p>
 * 
 * <ul>
 * <li>{@linkplain #váhyŤahu(int, int, int, int) ťah} – čo je štvorica
 * koeficientov určujúca, ktorým smerom sa má mať plazma tendencia pohybovať
 * viac (simulácia je pixelová, takže vždy ide o jeden pixel),</li>
 * <li>{@linkplain #dohorenie(int) dohorenie} – čo je parameter, ktorým sa
 * dá spomaliť proces horenia</li>
 * <li>a {@linkplain #útlm(int) útlm} – čím sa rozumie celoplošné tlmenie
 * plameňov, čiže vyššie hodnoty simuláciu urýchlia.</li>
 * </ul>
 * 
 * <p>Okrem toho výsledok značne ovplyvňuje {@linkplain 
 * #nastavPaletu(Color[], int[]) paleta}, ktorej počet farieb je určený
 * rozsahom hodnôt simulácie, ktorý sa dá sa určiť jednorazovo a to výhradne
 * jedným z {@linkplain #Plazma(Obrázok, int) konštruktorov} plazmového
 * generátora.</p>
 * 
 * <p>Generátor je tiež programovateľný asynchrónne a to s pomocou takzvaných
 * {@linkplain #pridajZdroj(double, double, double, int, int, int, int)
 * zdrojov} a {@linkplain #dej(Runnable) deja}, ktorými sa dajú docieliť rôzne
 * dynamické efekty. Každý {@linkplain #pridajZdroj(double, double, double,
 * int, int, int, int) zdroj} je časovo obmedzený a statický, ale dá sa buď
 * naraz definovať séria vzájomne časovo posunutých zdrojov, alebo umiestniť
 * pravidelné pridávanie nových zdrojov do {@linkplain #dej(Runnable) deja}
 * (čo je v podstate „spätne volaná funkcia/metóda“ – angl. callback). Tým sa
 * dá simulovať plazma s pohyblivým „zápalným {@linkplain #pridajZdroj(double,
 * double, double, int, int, int, int) zdrojom}.“</p>
 * 
 * <p class="attention"><b>Upozornenie:</b>
 * Je dôležité si uvedomiť, že po pripojení plazmy k obrázku nemá
 * rolovanie alebo pretáčanie samotného obrázka zmysel, pretože jeho
 * obsah je vždy úplne nahradený generátorom plazmy. Na pretáčanie
 * obsahu takého obrázka má zmysel použiť iba metódy plazmy.</p>
 * 
 * <p class="attention"><b>Upozornenie:</b>
 * Každá inštancia pixelového generátora plazmy je predvolene aktívna,
 * ale žiadna z nich nikdy automaticky nespúšťa {@linkplain 
 * Svet#spustiČasovač() časovač} sveta, od ktorého je automatizovaná
 * činnosť generátorov plazmy závislá.
 * 
 * (Keď hovoríme o automatizovanej činnosti, tak jednotlivé generátory môžu
 * fungovať aj nezávisle od {@linkplain Svet#spustiČasovač() časovača sveta},
 * ak sa o ich činnosť postará programátor – spúšťaním metódy
 * {@link #pracuj() pracuj}.)</p>
 * 
 * <p class="attention"><b>Upozornenie:</b>
 * Trieda {@link Plazma Plazma} vykonáva <b>pixelovú</b> simuláciu horenia
 * plazmy. Na to treba brať zreteľ najmä pri posudzovaní vplyvu jednotlivých
 * parametrov horenia na výsledok simulácie. Najmä v súvislosti s veličinou
 * {@linkplain #váhyŤahu(int, int, int, int) ťahu}. Akokoľvek veľká hodnota
 * ťahu nedokáže vynútiť zmenu pohybu plazmy špecifickým smerom viac než
 * o jeden pixel. Ďalšie podrobnosti nájdete v opise metódy {@link 
 * #váhyŤahu(int, int, int, int) váhyŤahu(ťahDoľava, ťahHore, ťahDoprava,
 * ťahDole)}.</p>
 * 
 * <p> </p>
 * 
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje generovanie plazmy s predvolenými parametrami.
 * Každé kliknutie striedavo pridá alebo odoberie {@linkplain #dej(Runnable)
 * dej} simulátora (ktorý je principiálne tzv. „callback“ funkciou/metódou).
 * Dej je definovaný veľmi jednoducho – generuje zdroj s polomerom 100 bodov
 * v strede plátna.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} ZákladnáPlazma {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Obrázok rozmermi totožný s veľkosťou plátna.}
		{@code kwdprivate} {@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num250}, {@code num250});

		{@code comm// Inštancia generátora plazmy prepojená na obrázok.}
		{@code kwdprivate} {@code currPlazma}  plazma  = {@code kwdnew} {@link Plazma#Plazma(Obrázok) Plazma}(obrázok);

		{@code comm// Inštancia deja generujúca v strede obrázka plazmovú guľu}
		{@code comm// s polomerom 100 bodov.}
		{@code kwdprivate} {@link Runnable Runnable} dej = () -&gt; plazma.{@link Plazma#pridajZdroj(Poloha, double) pridajZdroj}({@link Poloha#stred stred}, {@code num100});


		{@code comm// Konštruktor.}
		{@code kwdprivate} ZákladnáPlazma()
		{
			{@code valsuper}({@code num250}, {@code num250});
			{@link GRobot#skry() skry}();
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}


		{@code comm// Reakcia na kliknutie myšou.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			{@code comm// Prepínač: Ak nie je definovaný dej plazmy, použije}
			{@code comm// sa naša inštancia deja, inak sa zruší…}
			{@code kwdif} ({@code valnull} == plazma.{@link Plazma#dej() dej}())
				plazma.{@link Plazma#dej(Runnable) dej}(dej);
			{@code kwdelse}
				plazma.{@link Plazma#dej(Runnable) dej}({@code valnull});
		}

		{@code comm// Reakcia na časovač.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@code comm// Ak nie je svet prekreslený, tak sa prekreslí – v tomto}
			{@code comm// príklade pozostáva prekreslenie len z vymazania grafiky}
			{@code comm// podlahy a nakreslenia nášho obrázka spárovaného}
			{@code comm// s generátorom plazmy na podlahu.}
			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
			{
				{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
				{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(obrázok);
				{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			}
		}


		{@code comm// Hlavná metóda (vstupný bod programu).}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"ZákladnáPlazma.cfg"});
			{@code kwdnew} ZákladnáPlazma();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>zakladna-plazma.gif<alt/>Základná plazma.</image>Ukážka
 * zapnutia a vypnutia deja generujúceho základnú plazmu.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad upravuje viacero parametrov simulácie. Dej je tiež
 * definovaný komplikovanejšie – generuje zdroje sledujúce polohu kurzora
 * myši.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} Plamienok {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Obrázok rozmermi totožný s veľkosťou plátna.}
		{@code kwdprivate} {@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num250}, {@code num250});

		{@code comm// Inštancia generátora plazmy prepojená na obrázok.}
		{@code kwdprivate} {@code currPlazma}  plazma  = {@code kwdnew} {@link Plazma#Plazma(Obrázok) Plazma}(obrázok);

		{@code comm// Generátory pílových číselných radov na jemnú úpravu polohy plamienka.}
		{@code kwdprivate} {@link Rad Rad} radX = {@code kwdnew} {@link Rad#Rad(int, int, int) Rad}(-{@code num5}, {@code num5}, {@code num2});
		{@code kwdprivate} {@link Rad Rad} radY = {@code kwdnew} {@link Rad#Rad(int, int) Rad}(-{@code num1}, {@code num1});

		{@code comm// Inštancia deja generujúca plamienok podľa aktuálnej polohy myši.}
		{@code kwdprivate} {@link Runnable Runnable} dej = () -&gt;
		{
			{@link Bod Bod} myš = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();

			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num1}; ++i)
			{
				{@code typeint} y = radY.{@link Rad#daj() daj}();

				{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num16}; ++j)
				{
					{@code typeint} x = radX.{@link Rad#daj() daj}();

					{@code comm// Pridanie ďalšieho zdroja:}
					plazma.{@link Plazma#pridajZdroj(double, double, double, int, int, int, int) pridajZdroj}(
						{@code comm// Poloha:}
						myš.{@link Poloha#polohaX() polohaX}() + x,
						myš.{@link Poloha#polohaY() polohaY}() + y + j * {@code num3},

						({@code num10} - (j / {@code num2})) * {@code num2}, {@code comm// rozsah}
						{@code num8}, {@code num24},              {@code comm// hranice}
						{@code num4}, i * {@code num8} + j);      {@code comm// trvanie a oneskorenie}
				}
			}
		};


		{@code comm// Konštruktor.}
		{@code kwdprivate} Plamienok()
		{
			{@code valsuper}({@code num250}, {@code num250});

			{@code comm// Nastavenie vlastnej palety generátora plazmy:}
			plazma.{@link Plazma#nastavPaletu(Color[], int[]) nastavPaletu}({@code kwdnew} {@link Farba Farba}[] {
				{@code comm// Svetložltá, oranžová, červená a čierna}
				{@code comm// (s postupne klesajúcou priehľadnosťou):}
				{@code kwdnew} {@link Farba#Farba(int, int, int) Farba}({@code num255}, {@code num255}, {@code num128}),
				{@code kwdnew} {@link Farba#Farba(int, int, int, int) Farba}({@code num255}, {@code num128}, {@code num0}, {@code num192}),
				{@code kwdnew} {@link Farba#Farba(int, int, int, int) Farba}({@code num255}, {@code num0}, {@code num0}, {@code num32}),
				{@code kwdnew} {@link Farba#Farba(int, int, int, int) Farba}({@code num0}, {@code num0}, {@code num0}, {@code num0})});

			{@code comm// Úprava predvoleného kroku deja:}
			plazma.{@link Plazma#krok(int) krok}({@code num8});

			{@link GRobot#skry() skry}();
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}


		{@code comm// Reakcia na kliknutie myšou.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			{@code comm// Prepínač: Ak nie je definovaný dej plazmy, použije}
			{@code comm// sa naša inštancia deja, inak sa zruší…}
			{@code kwdif} ({@code valnull} == plazma.{@link Plazma#dej() dej}())
				plazma.{@link Plazma#dej(Runnable) dej}(dej);
			{@code kwdelse}
				plazma.{@link Plazma#dej(Runnable) dej}({@code valnull});
		}

		{@code comm// Reakcia na ťahanie myšou.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
		{
			{@code comm// Ak je dej aktívny, tak sa vykoná pri každej zmene polohy kurzora}
			{@code comm// myši (počas držania stlačeného tlačidla myši, t. j. počas ťahania}
			{@code comm// myšou). Spôsobí to rozohnenie mohutnejších plameňov.}
			{@code kwdif} ({@code valnull} != plazma.{@link Plazma#dej() dej}()) dej.{@link Runnable#run() run}();
		}

		{@code comm// Reakcia na časovač.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@code comm// Ak nie je svet prekreslený, tak sa prekreslí – v tomto}
			{@code comm// príklade pozostáva prekreslenie len z vymazania grafiky}
			{@code comm// podlahy a nakreslenia nášho obrázka spárovaného}
			{@code comm// s generátorom plazmy na podlahu.}
			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
			{
				{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
				{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(obrázok);
				{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			}
		}


		{@code comm// Hlavná metóda (vstupný bod programu).}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"Plamienok.cfg"});
			{@code kwdnew} Plamienok();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>plamienok.gif<alt/>Plamienok.</image>Ukážka horiaceho plamienka
 * pri nemeniacej sa polohe kurzora myši.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Ďalšie zdroje:</b></p>
 * 
 * <p>Môžeme povedať, že algoritmus použitý v triede {@code currPlazma} je
 * zovšeobecnením optimalizovanej verzie generátora ohňa dostupného tu:</p>
 * 
 * <ul>
 * <li><small>smartyP</small>: <em>Old School Fire Algorithm in Modern Day
 * WPF – SmartyPantsCoding.com.</em> 2008. Dostupné na:
 * <a
 * href="http://www.smartypantscoding.com/content/old-school-fire-algorithm-modern-day-wpf"
 * target="_blank">http://www.smartypantscoding.com/content/old-school-fire-algorithm-modern-day-wpf</a>
 * Citované: 27. 11. 2019.</li>
 * </ul>
 * 
 * <p>To znamená, že ide o základnú neoptimalizovanú verziu vhodnú na
 * samoštúdium (pretože veci príliš nekomplikuje). Verzia algoritmu použitá
 * v tejto triede ňou bola inšpirovaná, ale výsledná implementácia je
 * odlišná.</p>
 */
public class Plazma
{
	// http://www.smartypantscoding.com/sites/default/files/WPFFireApp.zip

	// Vnútorná inštancia generátora náhodných čísiel – je použitý na
	// generovanie intenzít zdrojov plazmy:
	private final static Random generátor = new Random();

	// Štvorica váh upravujúcich silu ťahu horenia plazmy v štyroch smeroch
	// (prepočet plazmy je vždy pixelový, tieto hodnoty určujú len váhu ťahu
	// v určenom smere):
	private int ťahDoľava = 50;
	private int ťahHore = 1000;
	private int ťahDoprava = 50;
	private int ťahDole = 500;

	// Hodnota dohorenia určuje zmiernenie horenia plazmy (v každom bode)
	// v pomere k váham (čím sú hodnoty váh vyššie, tým menší vplyv má
	// jednotkové dohorenie):
	private int dohorenie = 10;

	// Hodnota útlmu paušálne urýchľuje horenie plazmy o zadanú hodnotu:
	private int útlm = 30;


	// Rozsah hodnôt plazmového stroja zároveň určuje počet farieb palety:
	private int rozsah;

	// Nasledujúce dve polia obsahujú aktuálne hodnoty bodov plazmy
	// a striedajú sa počas prepočtu horenia plazmy:
	private int[] údajePlazmy;
	private int[] údajeZálohy;

	// Paleta určuje mapovanie všetkých hodnôt plazmy (v rozsahu) na farby:
	private int[] paleta;

	// Vnútorná inštancia obrázka, ku ktorému je plazma priradená:
	/*packagePrivate*/ final Obrázok obrázok;

	// Stav počítadla deja (krokov).
	private int dej = 0;

	// Aktuálny interval kroku deja.
	private int krok = 1;

	// Príkazy, ktoré sa majú vykonať vždy po vynulovaní počítadla deja.
	private Runnable vykonať = null;

	// Príznak aktívnosti generátora.
	private boolean aktívny = true;

	// Zdroj je generátor horiacej plazmy s určitou dĺžkou trvania a rozsahom.
	// (Môže mať aj určité oneskorenie a dá sa mu definovať rozsah
	// generovania hodnôt plazmy.)
	private class Zdroj
	{
		private int trvanie, oneskorenie;
		private int dolnáHranica, hornáHranica;
		private int rozsah, mocnina, dvojnásobok, oRiadok;
		private int x, y;

		private Zdroj nastav(double x, double y, double polomer,
			int dolnáHranica, int hornáHranica, int trvanie, int oneskorenie)
		{
			rozsah = (int)polomer;
			if (rozsah < 1)
				trvanie = oneskorenie = 0;
			else
			{
				this.trvanie = trvanie;
				this.oneskorenie = oneskorenie;

				this.dolnáHranica = dolnáHranica;
				this.hornáHranica = hornáHranica;

				mocnina = rozsah * rozsah;
				dvojnásobok = 2 * rozsah;
				oRiadok = obrázok.šírka - dvojnásobok;

				this.x = (int)((obrázok.šírka / 2.0) + x);
				this.y = (int)((obrázok.výška / 2.0) - y);
			}
			return this;
		}

		private void pracuj()
		{
			if (oneskorenie > 0) --oneskorenie;
			else if (trvanie > 0)
			{
				int x0 = x - rozsah;
				int y0 = y - rozsah;
				int ii = (obrázok.šírka * y0) + x0;

				for (int Δy = -rozsah; Δy < rozsah; ++Δy, ++y0, ii += oRiadok)
				{
					if (y0 >= 0 && y0 < obrázok.výška)
					{
						for (int Δx = -rozsah; Δx < rozsah; ++Δx, ++x0, ++ii)
							if (x0 >= 0 && x0 < obrázok.šírka)
							{
								int vzdialenosť = Δx * Δx + Δy * Δy;
								if (vzdialenosť >= mocnina) continue;
								vzdialenosť = mocnina - vzdialenosť;

								údajeZálohy[ii] += (int)(
									((dolnáHranica > hornáHranica) ?
										(hornáHranica + (Math.abs(generátor.
											nextLong()) % (1 + dolnáHranica -
												hornáHranica))) :
										(dolnáHranica + (Math.abs(generátor.
											nextLong()) % (1 + hornáHranica -
												dolnáHranica)))) *
									vzdialenosť / mocnina);

								if (údajeZálohy[ii] >= Plazma.this.rozsah)
									údajeZálohy[ii] = Plazma.this.rozsah - 1;
								else if (údajeZálohy[ii] < 0)
									údajeZálohy[ii] = 0;
							}
						x0 -= dvojnásobok;
					}
					else ii += dvojnásobok;
				}

				--trvanie;
			}
		}
	}

	// Zoznam zdrojov (generátorov horiacej plazmy).
	private final Vector<Zdroj> zdroje = new Vector<>();

	// Zoznam plaziem sveta.
	/*packagePrivate*/ final static Vector<Plazma> plazmy = new Vector<>();


	/**
	 * <p>Konštruktor plazmy prijímajúci obrázok, do ktorého bude
	 * prekresľovaný výsledok tohto generátora plazmy. Obrázok je
	 * povinným parametrom konštruktora. Po priradení k tomuto generátoru
	 * bude obsah obrázka aktivitou tejto triedy celoplošne prepisovaný,
	 * preto sa jeho inštancia stáva v podstate nepoužiteľnou na iné
	 * účely.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b>
	 * Je dôležité si uvedomiť, že pri pripojení plazmy k obrázku nemá
	 * rolovanie alebo pretáčanie samotného obrázka zmysel, pretože jeho
	 * obsah je vždy úplne nahradený generátorom plazmy. Na pretáčanie
	 * obsahu takého obrázka má zmysel použiť iba metódy plazmy.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Generátor je predvolene aktívny, ale žiadna inštancia plazmy nikdy
	 * automaticky nespúšťa {@linkplain Svet#spustiČasovač() časovač} sveta,
	 * od ktorého je automatizovaná činnosť generátora plazmy závislá.
	 * (Hovoríme o automatizovanej činnosti. Generátor môže fungovať aj
	 * nezávisle od toho, ak sa o jeho činnosť postará programátor.)</p>
	 * 
	 * @param obrázok inštancia obrázka, ktorá bude spárovaná s týmto
	 *     pixelovým generátorom plazmy
	 * 
	 * @see #Plazma(Obrázok, int)
	 */
	public Plazma(Obrázok obrázok)
	{ this(obrázok, 256); }

	/**
	 * <p>Alternatívny konštruktor umožňujúci nastavenie rozsahu hodnôt
	 * tejto inštancie simulátora. Parameter {@code rozsah} umožňuje určiť
	 * počet dovolených hodnôt používaných v simulátore na vyjadrenie
	 * intenzity plazmy. Predvolená hodnota je 256 hodnôt, čo zodpovedá
	 * číselnému rozsahu ⟨0; 255⟩. Počet hodnôt zároveň určuje počet farieb
	 * palety (pozri metódu {@link #nastavPaletu(Color[], int[])
	 * nastavPaletu}), pričom pri veľkých rozsahoch môžu byť viaceré položky
	 * palety (farby) idúce v rade za sebou identické. (Čiže rozsah neslúži
	 * na určenie výsledného počtu farieb, ale na ovplyvnenie výpočtu/činnosti
	 * simulátora.)</p>
	 * 
	 * <p>Ďalšie informácie o konštrukcii simulátora nájdete v opise
	 * konštruktora {@link #Plazma(Obrázok) Plazma(obrázok)}.</p>
	 * 
	 * @param obrázok inštancia obrázka, ktorá bude spárovaná s týmto
	 *     pixelovým generátorom plazmy
	 * @param rozsah rozsah hodnôt intenzity plazmy v pixelovej simulácii
	 * 
	 * @see #Plazma(Obrázok)
	 */
	public Plazma(Obrázok obrázok, int rozsah)
	{
		this.obrázok = obrázok;
		if (null == obrázok.údajeObrázka) obrázok.údajeObrázka =
			((DataBufferInt)obrázok.getRaster().getDataBuffer()).getData();

		údajePlazmy = new int[obrázok.šírka * obrázok.výška];
		údajeZálohy = new int[obrázok.šírka * obrázok.výška];

		this.rozsah = rozsah;
		paleta = new int[rozsah];

		nastavPaletu(null

			// new Color[] {

			/* Biela, žltá, červená, čierna (s klesajúcou priehľadnosťou). * /
			new Color(0xff_ffffff, true),
			new Color(0x80_ffff00, true),
			new Color(0x80_ff0000, true),
			new Color(0x00_000000, true),
			/**/

			/* Žltá do červenej. * /
			new Color(0xff_ffff00, true),
			new Color(0x00_ff0000, true),
			/**/

			/* Červená, modrá, zelená, žltá, biela. * /
			new Color(0x20_ff0000, true),
			new Color(0x40_0000ff, true),
			new Color(0x80_00ff00, true),
			new Color(0xA0_ffff00, true),
			new Color(0x00_ffffff, true)
			/**/

			/* Červená, modrá, zelená, žltá, čierna. * /
			new Color(0x20_ff0000, true),
			new Color(0x40_0000ff, true),
			new Color(0x80_00ff00, true),
			new Color(0xA0_ffff00, true),
			new Color(0x00_000000, true)
			/**/

			/* Dúha * /
			new Color(0xC0_f60000, true),
			new Color(0xA0_ffbc00, true),
			new Color(0x80_ffee00, true),
			new Color(0x60_4de94c, true),
			new Color(0x40_3783ff, true),
			new Color(0x20_4815aa, true),
			new Color(0x00_ffffff, true)
			/**/

			// }

			, null);

		plazmy.add(this);
	}


	// Prepočíta údaje plazmy – posunie simuláciu o jeden krok ďalej.
	private void prepočítaj()
	{
		int maxX = obrázok.šírka - 1;
		int maxY = obrázok.výška - 1;

		// int ľaváHodnota, praváHodnota, hornáHodnota, dolnáHodnota;
		int aktuálnaPoloha = 0, ľaváPoloha = 1, praváPoloha = -1,
			hornáPoloha = +obrázok.šírka, dolnáPoloha = -obrázok.šírka;

		int deliteľ = ťahDoľava + ťahHore + ťahDoprava + ťahDole - dohorenie;
		if (0 == deliteľ) deliteľ = 1;

		for (int y = 0; y < obrázok.výška; ++y)
		{
			for (int x = 0; x < obrázok.šírka; ++x)
			{
				/*
				// Zápis pred optimalizáciou (pozor, medzitým boli
				// prehodené znamienka polôh; dôsledok zrejme pôvodne
				// nesprávneho zrkadlenia poľa plazmy oproti bitovej mape
				// obrázka):
				if (x <= 0) ľaváHodnota = 0; else
					ľaváHodnota = údajeZálohy[ľaváPoloha];
				if (x >= maxX) praváHodnota = 0; else
					praváHodnota = údajeZálohy[praváPoloha];
				if (y <= 0) hornáHodnota = 0; else
					hornáHodnota = údajeZálohy[hornáPoloha];
				if (y >= maxY) dolnáHodnota = 0; else
					dolnáHodnota = údajeZálohy[dolnáPoloha];

				int priemer =
					((ťahDoľava * praváHodnota + ťahHore * dolnáHodnota +
						ťahDoprava * ľaváHodnota + ťahDole * hornáHodnota) /
						deliteľ) - útlm;
				*/

				int priemer = ((
					ťahDoľava  * (x >= maxX ? 0 : údajeZálohy[ľaváPoloha]) +
					ťahHore    * (y >= maxY ? 0 : údajeZálohy[hornáPoloha]) +
					ťahDoprava * (x <= 0    ? 0 : údajeZálohy[praváPoloha]) +
					ťahDole    * (y <= 0    ? 0 : údajeZálohy[dolnáPoloha])
						) / deliteľ) - útlm;

				if (priemer < 0) priemer = 0; else
				if (priemer >= rozsah) priemer = rozsah - 1;

				údajePlazmy[aktuálnaPoloha] = priemer;

				++aktuálnaPoloha; ++ľaváPoloha; ++praváPoloha;
				++hornáPoloha; ++dolnáPoloha;
			}
		}

		int[] vymeň = údajePlazmy;
		údajePlazmy = údajeZálohy;
		údajeZálohy = vymeň;
	}


	/**
	 * <p>Vygeneruje novú paletu farieb pre jednotlivé úrovne intenzít
	 * plazmy podľa skupiny zadaných (kľúčových) farieb s možnosťou
	 * ovplyvnenia ich umiestnenia vo výslednej palete zarážkami. Prvý prvok
	 * poľa určuje farbu zodpovedajúcu najvyššej intenzite plazmy.</p>
	 * 
	 * <p>Ak je pole farieb prázdne, tak metóda použije predvolené kľúčové
	 * farby (a vypočíta lineárny farebný prechod medzi nimi): sýtočervenú
	 * úplne nepriehľadnú a úplne priehľadnú čiernu. Ak pole obsahuje iba
	 * jeden prvok, tak ten sa stane prvou kľúčovou farbou (zodpovedajúcou
	 * najvyššej intenzite plazmy) a druhá kľúčová farba bude (podobne ako
	 * v predchádzajúcom prípade) úplne čierna priehľadná farba. Pri inom
	 * počte farieb bude paleta vygenerovaná podľa nich. Je vysoko
	 * odporúčané, aby posledná farba bola úplne transparentná
	 * (priehľadná).</p>
	 * 
	 * <p>Ak nie sú zadané žiadne zarážky, farby budú vo výslednej palete
	 * rozmiestnené rovnomerne (a priestor medzi nimi bude vyplnený
	 * lineárne interpolovanými farbami). Zadanie zarážok ovplyvní polohu
	 * kľúčových farieb ležiacich medzi prvou a poslednou farbou. Z toho
	 * vyplýva, že počet využiteľných zarážok je o dve menší, než počet
	 * zadaných farieb. Poloha chýbajúcich zarážok je určená (vypočítaná)
	 * automaticky. Nadbytočné zarážky sú ignorované.</p>
	 * 
	 * <p>Nasledujúci obrázok ukazuje predvolenú paletu farieb nakreslenú na
	 * bielom pozadí. Predvolená paleta prechádza z úplne nepriehľadnej
	 * červenej farby do úplne priehľadnej čiernej farby.</p>
	 * 
	 * <p><image>paleta-plazmy-predvolena.png<alt/>Predvolená paleta
	 * plazmy</image>Predvolená paleta plazmy (pričom biela farba vľavo
	 * vznikla nakreslením úplne priehľadnej čiernej farby na biele
	 * pozadie).</p>
	 * 
	 * <p>Na ďalšom obrázku je zobrazená paleta vygenerovaná z týchto
	 * kľúčových farieb: <span style="background-color: #f60000; border:
	 * 1px solid black;">    </span> červená, <span style="background-color:
	 * #ffbc00; border: 1px solid black;">    </span> oranžová, <span
	 * style="background-color: #ffee00; border: 1px solid
	 * black;">    </span> žltá, <span style="background-color: #4de94c;
	 * border: 1px solid black;">    </span> zelená, <span
	 * style="background-color: #3783ff; border: 1px solid
	 * black;">    </span> bledomodrá, <span style="background-color:
	 * #4815aa; border: 1px solid black;">    </span> tmavomodrá a <span
	 * style="background-color: #ffffff; border: 1px solid
	 * black;">    </span> biela, pričom ich priehľadnosť postupne klesá.
	 * Toto je úplný príkaz:</p>
	 * 
	 * <pre CLASS="example">
		{@code comm// (Predpokladáme, že inštancia plazmy bola vytvorená skôr.)}
		plazma.{@link #nastavPaletu(Color[]) nastavPaletu}({@code kwdnew} {@link Farba#Farba(int, boolean) Farba}[] {
			{@code comm// Dúha.}
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0xC0_f60000}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0xA0_ffbc00}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0x80_ffee00}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0x60_4de94c}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0x40_3783ff}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0x20_4815aa}, {@code valtrue}),
			{@code kwdnew} {@link Farba#Farba(int, boolean) Farba}({@code num0x00_ffffff}, {@code valtrue})
		});
		</pre>
	 * 
	 * <p><image>paleta-plazmy-duha.png<alt/>Dúhová paleta
	 * plazmy</image>Dúhová paleta plazmy (pričom priehľadnosť farieb
	 * klesá smerom sprava doľava).</p>
	 * 
	 * @param farby pole farieb, ktoré majú byť použité vo výslednej palete
	 * @param zarážky nepovinné pole zarážok, ktorými sa dá ovplyvniť poloha
	 *     zadaných (kľúčových) farieb vo výslednej palete
	 * 
	 * @see #nastavPaletu(Color[])
	 * @see #dajPaletu()
	 */
	public void nastavPaletu(Color[] farby, int[] zarážky)
	{
		if (null == farby || 0 == farby.length)
			farby = new Color[]{new Color(255, 0, 0), new Color(0, true)};
		else if (1 == farby.length)
			farby = new Color[]{farby[0], new Color(0, true)};

		if (null == zarážky)
		{
			int prírastok = rozsah / (farby.length - 1);
			zarážky = new int[farby.length - 2];

			if (zarážky.length > 0)
			{
				zarážky[0] = prírastok;
				for (int i = 1; i < zarážky.length; ++i)
					zarážky[i] = zarážky[i - 1] + prírastok;
			}
		}

		if (zarážky.length < farby.length - 2)
		{
			int nové[] = new int[farby.length - 2];
			for (int i = 0; i < zarážky.length; ++i)
				nové[i] = zarážky[i];
			int prírastok = (rozsah - zarážky[zarážky.length - 1]) /
				(farby.length - zarážky.length - 1);
			for (int i = zarážky.length; i < nové.length; ++i)
				nové[i] = nové[i - 1] + prírastok;
			zarážky = nové;
		}

		for (int i = 0; i < zarážky.length; ++i)
		{
			if (zarážky[i] < 0) zarážky[i] = 0;
			else if (zarážky[i] >= rozsah) zarážky[i] = rozsah - 1;
		}

		int začiatok = 0, koniec;
		int f1 = farby[0].getRGB(), f2;

		System.out.println("Počet farieb: " + farby.length);
		System.out.println("Počet zarážok: " + zarážky.length +
			" (max. využiteľných: " + ((farby.length - 2) >= 0 ?
			(farby.length - 2) : 0) + ")");

		for (int j = 0; j < farby.length - 1; ++j)
		{
			if (j >= zarážky.length || j >= farby.length - 2)
				koniec = rozsah - 1;
			else
				koniec = zarážky[j];

			int počet = 1 + koniec - začiatok;
			f2 = farby[j + 1].getRGB();

			System.out.println(j + ": Rozsah: " + začiatok +
				" – " + koniec + "; počet: " + počet);

			int r1 = ((f1 >> 16) & 0xff);
			int g1 = ((f1 >>  8) & 0xff);
			int b1 = ( f1        & 0xff);
			int a1 = ((f1 >> 24) & 0xff);

			int r2 = ((f2 >> 16) & 0xff);
			int g2 = ((f2 >>  8) & 0xff);
			int b2 = ( f2        & 0xff);
			int a2 = ((f2 >> 24) & 0xff);

			for (int i = 0; i < počet; ++i)
			{
				double t = (double)i / (double)(počet - 1);
				int r = (int)Svet.lineárnaInterpolácia(r1, r2, t);
				int g = (int)Svet.lineárnaInterpolácia(g1, g2, t);
				int b = (int)Svet.lineárnaInterpolácia(b1, b2, t);
				int a = (int)Svet.lineárnaInterpolácia(a1, a2, t);

				paleta[rozsah - začiatok - i - 1] =
					(a << 24) | (r << 16) | (g << 8) | b;
			}

			začiatok = koniec + 1;
			f1 = f2;
		}
	}

	/**
	 * <p>Vygeneruje novú paletu farieb pre jednotlivé úrovne intenzít
	 * plazmy podľa skupiny zadaných (kľúčových) farieb s rovnomerným
	 * rozmiestnením. (Prvý prvok poľa určuje farbu zodpovedajúcu najvyššej
	 * intenzite plazmy. Pozri aj metódu {@link Plazma#nastavPaletu(Color[],
	 * int[]) nastavPaletu(farby, zarážky)}.)</p>
	 * 
	 * @param farby pole farieb, ktoré majú byť použité vo výslednej palete
	 * 
	 * @see #nastavPaletu(Color[], int[])
	 * @see #dajPaletu()
	 */
	public void nastavPaletu(Color[] farby)
	{ nastavPaletu(farby, null); }


	/**
	 * <p>Vráti aktuálny zoznam farieb palety používanej na kreslenie plazmy
	 * tejto inštancie simulátora. Akoukoľvek úpravou vráteného zoznamu nie
	 * je možné ovplyvniť skutočnú (vnútornú) paletu tohto simulátora.</p>
	 * 
	 * @return zoznam farieb aktuálnej palety
	 * 
	 * @see #nastavPaletu(Color[], int[])
	 * @see #nastavPaletu(Color[])
	 */
	public Zoznam<Farba> dajPaletu()
	{
		Zoznam<Farba> dajPaletu = new Zoznam<>();
		for (int i : paleta) dajPaletu.pridaj(new Farba(i, true));
		return dajPaletu;
	}


	/**
	 * <p>Vráti štvoricu koeficientov váh ťahu, ktoré sú ústrednou súčasťou
	 * vzorca na výpočet plazmy. Vrátené pole obsahuje štvoricu koeficientov
	 * smerových ťahov v tomto poradí: doľava, hore, doprava a dole. Viac
	 * podrobností o váhach ťahu je uvedených v opise metódy {@link 
	 * #váhyŤahu(int, int, int, int) váhyŤahu(ťahDoľava, ťahHore,
	 * ťahDoprava, ťahDole)} (dôležité je najmä upozornenie, ktoré sa
	 * nachádza v opise uvedenej metódy).</p>
	 * 
	 * @return pole so štvoricou koeficientov smerových ťahov v tomto
	 *     poradí: doľava, hore, doprava, dole
	 * 
	 * @see #váhyŤahu(int, int, int, int)
	 * @see #váhyŤahu(double, double)
	 * @see #váhyŤahu(Poloha)
	 * @see #váhyŤahu(int[])
	 */
	public int[] váhyŤahu()
	{ return new int[]{ťahDoľava, ťahHore, ťahDoprava, ťahDole}; }

	/** <p><a class="alias"></a> Alias pre {@link #váhyŤahu() váhyŤahu}.</p> */
	public int[] vahyTahu()
	{ return váhyŤahu(); }

	/**
	 * <p>Nastaví nové váhy smerových koeficientov ťahu v pixelovej simulácii
	 * plazmy.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b>
	 * Trieda {@link Plazma Plazma} vykonáva <b>pixelovú</b> simuláciu
	 * a ľubovoľne veľká hodnota ťahu nedokáže vynútiť zmenu pohybu plazmy
	 * viac než o jeden pixel. Koeficienty ťahu sú len váhy, ktoré ovplvňujú
	 * to, ktorým smerom počas simulovania sa bude mať plazma tendenciu viac
	 * pohybovať. Rýchlejší pohyb plazmy sa dá docieliť len pohyblivým
	 * zdrojom plazmy. (Pozri napríklad opis metódy {@link 
	 * #pridajZdroj(double, double, double, int, int, int, int) pridajZdroj}
	 * alebo príklady v {@linkplain Plazma hlavnom opise} tejto triedy.)</p>
	 * 
	 * <p>Tieto váhové koeficienty sú vo vzorci simulácie použité na
	 * ovplyvnenie intenzít plazmy v jednotlivých bodoch simulácie podľa
	 * hodnôt ich susediacich bodov. Podľa toho, ktorá váha má vyššiu
	 * hodnotu sa plazma má tendenciu pohybovať jej smerom.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Štvorica koeficientov ťahu je len jednou veličinou ovplyvňujúcou
	 * výsledok simulácie. Ďalšími dvomi sú {@linkplain #dohorenie(int)
	 * dohorenie} a {@linkplain #útlm(int) útlm}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} SmerováPlazma {@code kwdextends} {@link GRobot GRobot}
		{
			{@code comm// Obrázok rozmermi totožný s veľkosťou plátna.}
			{@code kwdprivate} {@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok Obrázok}({@code num250}, {@code num250});

			{@code comm// Inštancia generátora plazmy prepojená na obrázok.}
			{@code kwdprivate} {@link Plazma Plazma}  plazma  = {@code kwdnew} {@link Plazma#Plazma(Obrázok) Plazma}(obrázok);

			{@code comm// Inštancia deja generujúca v strede obrázka plazmovú guľôčku}
			{@code comm// s polomerom 10 bodov.}
			{@code kwdprivate} {@link Runnable Runnable} dej = () -&gt; plazma.{@link #pridajZdroj(Poloha, double) pridajZdroj}({@link Poloha#stred stred}, {@code num10});


			{@code comm// Konštruktor.}
			{@code kwdprivate} SmerováPlazma()
			{
				{@code valsuper}({@code num250}, {@code num250});
				{@link GRobot#skry() skry}();
				{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
			}


			{@code comm// Reakcia na kliknutie myšou.}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
			{
				{@code comm// Prepínač: Ak nie je definovaný dej plazmy, použije}
				{@code comm// sa naša inštancia deja, inak sa zruší…}
				{@code kwdif} ({@code valnull} == plazma.{@link #dej() dej}())
					plazma.{@link #dej(Runnable) dej}(dej);
				{@code kwdelse}
					plazma.{@link #dej(Runnable) dej}({@code valnull});
			}

			{@code comm// Reakcia na ťahanie myšou.}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
			{
				{@code comm// Ťah sa nastaví podľa polohy myši (ktorá je teraz zároveň}
				{@code comm// smerovým vektorom, keďže zdroj plazmy je umiestnený}
				{@code comm// v strede plátna).}
				{@code kwdif} ({@code valnull} != plazma.{@link #dej() dej}())
					plazma.{@link #váhyŤahu(Poloha) váhyŤahu}({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}());
			}

			{@code comm// Reakcia na časovač.}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
			{
				{@code comm// Ak nie je svet prekreslený, tak sa prekreslí – v tomto}
				{@code comm// príklade pozostáva prekreslenie len z vymazania grafiky}
				{@code comm// podlahy a nakreslenia nášho obrázka spárovaného}
				{@code comm// s generátorom plazmy na podlahu.}
				{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
				{
					{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
					{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(obrázok);

					{@code comm// Výpis aktuálneho nastavenia váh vo vrchnej časti plátna:}
					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, {@code num100});
					{@code typeint} ťah[] = plazma.{@link #váhyŤahu() váhyŤahu}();
					{@link GRobot#text(String) text}(ťah[{@code num0}] + {@code srg", "} + ťah[{@code num1}] + {@code srg", "} + ťah[{@code num2}] + {@code srg", "} + ťah[{@code num3}]);

					{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}
			}


			{@code comm// Hlavná metóda (vstupný bod programu).}
			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"SmerováPlazma.cfg"});
				{@code kwdnew} SmerováPlazma();
			}
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <p><image>smerova-plazma.gif<alt/>Smerová plazma.</image>Ukážka
	 * zmeny smerovania horenia plazmy pri pohybe kurzora myši približne po
	 * kruhovej dráhe okolo stredu.<br /><small>(Krížik znázorňujúci kurzor
	 * myši je dokreslený.)</small></p>
	 * 
	 * @param ťahDoľava  hodnota koeficientu váhy ťahu smerom doľava
	 * @param ťahHore    hodnota koeficientu váhy ťahu smerom hore
	 * @param ťahDoprava hodnota koeficientu váhy ťahu smerom doprava
	 * @param ťahDole    hodnota koeficientu váhy ťahu smerom dole
	 * 
	 * @see #váhyŤahu()
	 * @see #váhyŤahu(double, double)
	 * @see #váhyŤahu(Poloha)
	 * @see #váhyŤahu(int[])
	 */
	public void váhyŤahu(int ťahDoľava, int ťahHore,
		int ťahDoprava, int ťahDole)
	{
		this.ťahDoľava = ťahDoľava;
		this.ťahHore = ťahHore;
		this.ťahDoprava = ťahDoprava;
		this.ťahDole = ťahDole;
	}

	/** <p><a class="alias"></a> Alias pre {@link #váhyŤahu(int, int, int, int) váhyŤahu}.</p> */
	public void vahyTahu(int ťahDoľava, int ťahHore,
		int ťahDoprava, int ťahDole)
	{ váhyŤahu(ťahDoľava, ťahHore, ťahDoprava, ťahDole); }

	/**
	 * <p>Alternatívny spôsob nastavenia smerových váh ťahu plazmy. Zadané
	 * súradnice sú považované za súradnice kvázi smerového vektora, ktorý
	 * bude použitý na nastavenie váh takto:</p>
	 * 
	 * <ul>
	 * <li>Ak je súradnica (x alebo y) kladná, tak jej stonásobok
	 * zaokrúhlený na celé číslo bude vložený do kladného smeru (doprava
	 * alebo hore) a jej pôvodná hodnota zaokrúhlená na celé číslo do
	 * záporného smeru (doľava alebo dole).</li>
	 * <li>Ak je súradnica (x alebo y) záporná, tak stonásobok jej
	 * absolútnej hodnoty zaokrúhlený na celé číslo bude vložený do
	 * záporného smeru (doľava alebo dole) a jej absolútna hodnota
	 * zaokrúhlená na celé číslo do kladného smeru (doprava alebo hore).</li>
	 * <li>Nulová hodnota súradnice spôsobí nastavenie dvojice k nej
	 * prislúchajúcich váh na hodnoty 10.</li>
	 * </ul>
	 * 
	 * <p> Viac podrobností o váhach ťahu je uvedených v opise metódy
	 * {@link #váhyŤahu(int, int, int, int) váhyŤahu(ťahDoľava, ťahHore,
	 * ťahDoprava, ťahDole)} (dôležité je najmä upozornenie, ktoré sa
	 * nachádza v opise uvedenej metódy).</p>
	 * 
	 * @param x x-ová súradnica kvázi smerového vektora určujúceho nové
	 *     nastavenie váh ťahu (kvázi kosínus smeru)
	 * @param y y-ová súradnica kvázi smerového vektora určujúceho nové
	 *     nastavenie váh ťahu (kvázi sínus smeru)
	 * 
	 * @see #váhyŤahu()
	 * @see #váhyŤahu(int, int, int, int)
	 * @see #váhyŤahu(Poloha)
	 * @see #váhyŤahu(int[])
	 */
	public void váhyŤahu(double x, double y)
	{
		if (x > 0)
		{
			ťahDoprava = (int)(100 * x);
			ťahDoľava = (int)x;
		}
		else if (x < 0)
		{
			ťahDoprava = -(int)x;
			ťahDoľava = -(int)(100 * x);
		}
		else ťahDoprava = ťahDoľava = 10;

		if (y > 0)
		{
			ťahHore = (int)(100 * y);
			ťahDole = (int)y;
		}
		else if (y < 0)
		{
			ťahHore = -(int)y;
			ťahDole = -(int)(100 * y);
		}
		else ťahHore = ťahDole = 10;
	}

	/** <p><a class="alias"></a> Alias pre {@link #váhyŤahu(double, double) váhyŤahu}.</p> */
	public void vahyTahu(double x, double y) { váhyŤahu(x, y); }

	/**
	 * <p>Alternatívny spôsob nastavenia smerových váh ťahu plazmy. Zadaný
	 * bod je spracovaný rovnako ako súradnice kvázi smerového vektora
	 * zadávané v parametroch metódy {@link #váhyŤahu(double, double)
	 * váhyŤahu(x, y)} (podrobnosti pozri v jej opise).</p>
	 * 
	 * @param bod kvázi smerový vektor určujúci nové nastavenie váh ťahu
	 *     (pozri aj opis metódy{@link #váhyŤahu(double, double)
	 *     váhyŤahu(x, y)})
	 * 
	 * @see #váhyŤahu()
	 * @see #váhyŤahu(int, int, int, int)
	 * @see #váhyŤahu(double, double)
	 * @see #váhyŤahu(int[])
	 */
	public void váhyŤahu(Poloha bod)
	{ váhyŤahu(bod.polohaX(), bod.polohaY()); }

	/** <p><a class="alias"></a> Alias pre {@link #váhyŤahu(Poloha) váhyŤahu}.</p> */
	public void vahyTahu(Poloha bod)
	{ váhyŤahu(bod.polohaX(), bod.polohaY()); }

	/**
	 * <p>Nastaví štvoricu koeficientov váh ťahu. Metóda očakáva v zadanom
	 * poli štvoricu smerových koeficientov ťahov v nasledujúcom poradí:
	 * doľava, hore, doprava a dole. Prípadné chýbajúce hodnoty (napr. pri
	 * nedostatočnej dĺžke poľa) nie sú nastavené. Viac podrobností o váhach
	 * ťahu je uvedených v opise metódy {@link #váhyŤahu(int, int, int, int)
	 * váhyŤahu(ťahDoľava, ťahHore, ťahDoprava, ťahDole)} (dôležité je najmä
	 * upozornenie, ktoré sa nachádza v opise uvedenej metódy).</p>
	 * 
	 * @param váhy pole so štvoricou smerových koeficientov ťahov v poradí:
	 *     doľava, hore, doprava, dole
	 * 
	 * @see #váhyŤahu()
	 * @see #váhyŤahu(int, int, int, int)
	 * @see #váhyŤahu(double, double)
	 * @see #váhyŤahu(Poloha)
	 */
	public void váhyŤahu(int[] váhy)
	{
		if (null == váhy) return;
		if (váhy.length > 0) ťahDoľava = váhy[0];
		if (váhy.length > 1) ťahHore = váhy[1];
		if (váhy.length > 2) ťahDoprava = váhy[2];
		if (váhy.length > 3) ťahDole = váhy[3];
	}

	/** <p><a class="alias"></a> Alias pre {@link #váhyŤahu(int[]) váhyŤahu}.</p> */
	public void vahyTahu(int[] váhy) { váhyŤahu(váhy); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu hodnotu dohorenia plazmy.
	 * Podrobnosti o tejto vlastnosti sú uvedené v opise metódy {@link 
	 * #dohorenie(int) dohorenie}.</p>
	 * 
	 * @return aktuálna hodnota dohorenia
	 * 
	 * @see #dohorenie(int)
	 * @see #útlm()
	 */
	public int dohorenie()
	{
		return dohorenie;
	}

	/**
	 * <p><a class="setter"></a> Nastaví novú hodnotu dohorenia plazmy.
	 * Táto vlastnosť určuje zmiernenie (spomalenie) horenia plazmy
	 * v pomere k váham. (Pozri {@link #váhyŤahu(int, int, int, int)
	 * váhyŤahu(ťahDoľava, ťahHore, ťahDoprava, ťahDole)}.) Čím sú hodnoty
	 * váh vyššie, tým menší vplyv má jednotkové dohorenie na výslednú
	 * rýchlosť horenia.
	 * 
	 * (Algoritmicky je táto vlastnosť umiestnená do deliteľa/menovateľa
	 * vzorca používaného na výpočet simulácie horenia plazmy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Dohorenie je len jednou z hlavných veličín ovplyvňujúcich výsledok
	 * simulácie. Ďalšími dvomi sú {@linkplain #váhyŤahu(int, int, int, int)
	 * ťah} a {@linkplain #útlm(int) útlm}</p>
	 * 
	 * @param dohorenie nová hodnota dohorenia
	 * 
	 * @see #dohorenie()
	 * @see #útlm(int)
	 */
	public void dohorenie(int dohorenie)
	{
		this.dohorenie = dohorenie;
	}


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu hodnotu útlmu plazmy.
	 * Podrobnosti o tejto vlastnosti sú uvedené v opise metódy {@link 
	 * #útlm(int) útlm}.</p>
	 * 
	 * @return aktuálna hodnota útlmu
	 * 
	 * @see #útlm(int)
	 * @see #dohorenie()
	 */
	public int útlm()
	{
		return útlm;
	}

	/** <p><a class="alias"></a> Alias pre {@link #útlm() útlm}.</p> */
	public int utlm()
	{
		return útlm;
	}

	/**
	 * <p><a class="setter"></a> Nastaví novú hodnotu útlmu plazmy.
	 * Táto vlastnosť paušálne (celoplošne) tlmí intenzitu plameňov plazmy
	 * počas simulácie horenia, čím proces horenia urýchľuje.
	 * 
	 * (Algoritmicky je táto vlastnosť realizovaná ako odčítanec intenzity
	 * vo vzorci používanom pri výpočte simulácie horenia plazmy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Útlm je len jednou z hlavných veličín ovplyvňujúcich výsledok
	 * simulácie. Ďalšími dvomi sú {@linkplain #váhyŤahu(int, int, int, int)
	 * ťah} a {@linkplain #dohorenie(int) dohorenie}</p>
	 * 
	 * @param útlm nová hodnota útlmu
	 * 
	 * @see #útlm()
	 * @see #dohorenie(int)
	 */
	public void útlm(int útlm)
	{
		this.útlm = útlm;
	}

	/** <p><a class="alias"></a> Alias pre {@link #útlm(int) útlm}.</p> */
	public void utlm(int útlm)
	{
		this.útlm = útlm;
	}


	/**
	 * <p>Overí, či je tento generátor aktívny. Vracia presne opačnú
	 * informáciu ako metóda {@link #neaktívny() neaktívny} (resp. metóda
	 * {@link #pasívny() pasívny}). Generátor je predvolene aktívny, ale
	 * žiadna inštancia plazmy nikdy automaticky nespúšťa {@linkplain 
	 * Svet#spustiČasovač() časovač} sveta, od ktorého je automatizovaná
	 * činnosť generátora plazmy závislá.
	 * 
	 * Ak je generátor aktívny, tak metóda {@link #pracuj() pracuj}
	 * automaticky spúšťa reakciu {@link #aktivita() aktivita}.
	 * V opačnom prípade spúšťa reakciu {@link #pasivita() pasivita}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Podrobnosti o aktivite sú v opisoch metód: {@link #aktivita()
	 * aktivita()}/{@link #pasivita() pasivita()} a {@link #pracuj()
	 * pracuj()}.</p>
	 * 
	 * @return {@code valtrue} ak je generátor aktívny, inak {@code 
	 *     valfalse}
	 * 
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 * @see #pracuj()
	 * @see #krok()
	 * @see #dej()
	 */
	public boolean aktívny() { return aktívny; }

	/** <p><a class="alias"></a> Alias pre {@link #aktívny() aktívny}.</p> */
	public boolean aktivny() { return aktívny; }

	/**
	 * <p>Overí, či je tento generátor neaktívny. Vracia presne opačnú
	 * informáciu ako metóda {@link #aktívny() aktívny}. Generátor je
	 * predvolene aktívny, ale žiadna inštancia plazmy nikdy automaticky
	 * nespúšťa {@linkplain Svet#spustiČasovač() časovač} sveta, od ktorého
	 * je automatizovaná činnosť generátora plazmy závislá.
	 * 
	 * Ak je generátor aktívny, tak metóda {@link #pracuj() pracuj}
	 * automaticky spúšťa reakciu {@link #aktivita() aktivita}.
	 * V opačnom prípade spúšťa reakciu {@link #pasivita() pasivita}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Podrobnosti o aktivite sú v opisoch metód: {@link #aktivita()
	 * aktivita()}/{@link #pasivita() pasivita()} a {@link #pracuj()
	 * pracuj()}.</p>
	 * 
	 * @return {@code valtrue} ak <b>nie je</b> generátor aktívny, inak
	 *     {@code valfalse}
	 * 
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 * @see #pracuj()
	 * @see #krok()
	 * @see #dej()
	 */
	public boolean neaktívny() { return !aktívny; }

	/** <p><a class="alias"></a> Alias pre {@link #neaktívny() neaktívny}.</p> */
	public boolean neaktivny() { return !aktívny; }

	/** <p><a class="alias"></a> Alias pre {@link #neaktívny() neaktívny}.</p> */
	public boolean pasívny() { return !aktívny; }

	/** <p><a class="alias"></a> Alias pre {@link #neaktívny() neaktívny}.</p> */
	public boolean pasivny() { return !aktívny; }

	/**
	 * <p>Aktivuje tento pixelový generátor plazmy.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Podrobnosti o aktivite sú v opisoch metód: {@link #aktivita()
	 * aktivita()}/{@link #pasivita() pasivita()} a {@link #pracuj()
	 * pracuj()}.</p>
	 * 
	 * @see #deaktivuj()
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivita()
	 * @see #pasivita()
	 * @see #pracuj()
	 * @see #krok()
	 * @see #dej()
	 */
	public void aktivuj() { aktívny = true; }

	/**
	 * <p>Deaktivuje tento pixelový generátor plazmy.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Podrobnosti o aktivite sú v opisoch metód: {@link #aktivita()
	 * aktivita()}/{@link #pasivita() pasivita()} a {@link #pracuj()
	 * pracuj()}.</p>
	 * 
	 * @see #aktivuj()
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivita()
	 * @see #pasivita()
	 * @see #pracuj()
	 * @see #krok()
	 * @see #dej()
	 */
	public void deaktivuj() { aktívny = false; }


	/**
	 * <p><a class="getter"></a> Vráti aktuálna inštanciu príkazov deja
	 * ({@link Runnable Runnable}). Ďalšie podrobnosti o deji sú v opise
	 * metódy {@link #dej(Runnable) dej(vykonať)}.</p>
	 * 
	 * @return vráti aktuálna inštanciu príkazov {@link Runnable Runnable}
	 * 
	 * @see #dej(Runnable)
	 * @see #dej(Runnable, int)
	 * @see #krok()
	 * @see #pracuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 */
	public Runnable dej() { return vykonať; }

	/**
	 * <p><a class="setter"></a> Nastaví novú inštanciu príkazov deja
	 * ({@link Runnable Runnable}) so zachovaním aktuálneho kroku deja.
	 * Príkazy sú spúšťané len v stave {@linkplain #aktívny() aktívneho
	 * generátora}. Ak je krok deja menší, než {@code num1}, príkazy deja
	 * nebudú vykonávané. Rovnako hodnota {@code valnull} inštancie
	 * príkazov v parametri {@code vykonať} spôsobí zastavenie
	 * vykonávania príkazov deja.</p>
	 * 
	 * <p>Inou možnosťou ovplyvňovania správania sa generátora plazmy je
	 * odvodenie novej triedy od tejto triedy a prekrytie metódy {@link 
	 * #aktivita() aktivita}, prípadne aj {@link #pasivita() pasivita}.
	 * Tie sú potom automaticky spúšťané podľa toho, či je generátor
	 * {@linkplain #aktívny() aktívny} alebo {@linkplain #pasívny() pasívny}.
	 * Je to podobný princíp ako je implementovaný v triede {@link GRobot
	 * GRobot} – {@link GRobot#aktivita() GRobot.aktivita()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Príkazy {@linkplain #dej(Runnable) deja} sú vykonávané tesne pred
	 * prepočtom údajov generátora plazmy a príkazy {@linkplain #aktivita()
	 * aktivity} sú vykonané tesne po prepočte a prekreslení obsahu obrázka
	 * spárovaného s plazmou).</p>
	 * 
	 * @param vykonať inštancia {@link Runnable Runnable} s príkazmi deja
	 * 
	 * @see #dej()
	 * @see #dej(Runnable, int)
	 * @see #krok()
	 * @see #pracuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 */
	public void dej(Runnable vykonať)
	{
		this.vykonať = vykonať;
		dej = null == vykonať ? 0 : krok;
	}

	/**
	 * <p><a class="setter"></a> Nastaví novú inštanciu príkazov deja
	 * ({@link Runnable Runnable}) a nový krok deja. Podrobnosti o deji
	 * sú v opise metódy {@link #dej(Runnable) dej(vykonať)}.</p>
	 * 
	 * @param vykonať nová inštancia príkazov deja (pozri {@link 
	 *     #dej(Runnable) dej})
	 * @param krok nová hodnota kroku deja (pozri {@link #krok(int) krok})
	 * 
	 * @see #dej()
	 * @see #dej(Runnable)
	 * @see #krok()
	 * @see #pracuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 */
	public void dej(Runnable vykonať, int krok)
	{
		this.vykonať = vykonať;
		this.krok = krok;
		dej = null == vykonať ? 0 : krok;
	}


	/**
	 * <p><a class="getter"></a> Vráti aktuálnu hodnotu kroku deja. Ide
	 * o počet prepočtov simulátora plazmy, po ktorom sa majú vykonať
	 * príkazy deja (ak sú definované).</p>
	 * 
	 * @return aktuálna hodnota kroku deja
	 * 
	 * @see #krok(int)
	 * @see #dej()
	 * @see #pracuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 */
	public int krok()
	{
		return krok;
	}

	/**
	 * <p><a class="setter"></a> Nastaví novú hodnotu kroku deja. Krok je
	 * počet prepočtov (pozri aj metódu {@link #pracuj() pracuj}) simulátora
	 * plazmy, po ktorom sa majú vykonať príkazy deja (ak sú definované a ak
	 * je generátor plazmy v {@linkplain #aktívny() aktívnom stave}). Ak je
	 * krok deja menší, než {@code num1}, príkazy deja nebudú vykonávané.
	 * 
	 * (Rovnako hodnota {@code valnull} inštancie príkazov v parametri
	 * {@code vykonať} niektorej z metód na nastavenie deja (napr.
	 * {@link #dej(Runnable) dej(vykonať)}) spôsobí zastavenie vykonávania
	 * príkazov deja.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Príkazy {@linkplain #dej(Runnable) deja} sú vykonávané tesne pred
	 * prepočtom údajov generátora plazmy a príkazy {@linkplain #aktivita()
	 * aktivity} sú vykonané tesne po prepočte a prekreslení obsahu obrázka
	 * spárovaného s plazmou).</p>
	 * 
	 * @param krok nová hodnota kroku deja
	 * 
	 * @see #krok()
	 * @see #dej()
	 * @see #pracuj()
	 * @see #aktivita()
	 * @see #pasivita()
	 */
	public void krok(int krok)
	{
		this.krok = krok;
		dej = null == vykonať ? 0 : krok;
	}


	/**
	 * <p>Pridá nový zdroj plazmy. Zdroj plazmy zvyšuje intenzitu plazmy
	 * v kruhovej oblasti so stredom na zadaných súradniciach a so zadaným
	 * polomerom. Výsledný nárast intenzity v jednotlivých bodoch zdroja
	 * závisí od dvoch veličín: vzdialenosť bodu od stredu zdroja a rozsah
	 * hraníc (parametre {@code dolnáHranica} a {@code hornáHranica}), ktorý
	 * je vstupom generátora pseudonáhodných čísiel. Trvanie generovania je
	 * určené v počte tikov, rovnako ako prípadné oneskorenie začatia
	 * generovania.</p>
	 * 
	 * <p>Ďalšie verzie tejto metódy majú vynechaný jeden alebo viacero
	 * z nasledujúcich parametrov a na ich miesto sú dosadené nasledujúce
	 * hodnoty (uvedené v tom poradí, ktoré platí pre túto verziu metódy):</p>
	 * 
	 * <ul>
	 * <li>{@code dolnáHranica}: {@code num0}, pričom parameter
	 * {@code hornáHranica} môže byť stále prítomný, ale v tom prípade je
	 * premenovaný na {@code rozsah}.</li>
	 * <li>{@code hornáHranica} (musí absentovať aj parameter {@code 
	 * dolnáHranica}): maximálna dovolená hodnota intenzity plazmy určená
	 * rozsahom pri konštrukcii (predvolene je maximum rovné hodnote 255) –
	 * pozri konštruktor {@link #Plazma(Obrázok, int) Plazma(obrázok,
	 * rozsah)}.</li>
	 * <li>{@code trvanie}: {@code num1}.</li>
	 * <li>{@code oneskorenie}: {@code num0}.</li>
	 * </ul>
	 * 
	 * <!-- TODO po vyrobení svetelného filtra doplniť príklad použitia
	 * kooperujúci s plazmou -->
	 * 
	 * <p>Užitočné príklady použitia sú napríklad v {@linkplain Plazma
	 * hlavnom opise} tejto triedy.</p>
	 * 
	 * @param x x-ová súradnica stredu oblasti zdroja
	 * @param y y-ová súradnica stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param trvanie dĺžka aktivity zdroja v tikoch
	 * @param oneskorenie odloženie začiatku aktivity zdroja v tikoch
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(double x, double y, double polomer,
		int dolnáHranica, int hornáHranica, int trvanie, int oneskorenie)
	{
		for (Zdroj zdroj : zdroje)
		{
			if (zdroj.trvanie <= 0)
			{
				zdroj.nastav(x, y, polomer,
					dolnáHranica, hornáHranica,
					trvanie, oneskorenie);
				return;
			}
		}

		zdroje.add(new Zdroj().nastav(x, y, polomer,
			dolnáHranica, hornáHranica, trvanie, oneskorenie));
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}.</p>
	 * 
	 * @param x x-ová súradnica stredu oblasti zdroja
	 * @param y y-ová súradnica stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param trvanie dĺžka aktivity zdroja v tikoch
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(double x, double y, double polomer,
		int dolnáHranica, int hornáHranica, int trvanie)
	{
		pridajZdroj(x, y, polomer,
			dolnáHranica, hornáHranica,
			trvanie, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}.</p>
	 * 
	 * @param x x-ová súradnica stredu oblasti zdroja
	 * @param y y-ová súradnica stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(double x, double y, double polomer,
		int dolnáHranica, int hornáHranica)
	{
		pridajZdroj(x, y, polomer,
			dolnáHranica, hornáHranica,
			1, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}.</p>
	 * 
	 * @param x x-ová súradnica stredu oblasti zdroja
	 * @param y y-ová súradnica stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param rozsah horná hranica generovania náhodných hodnôt intenzity
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(double x, double y, double polomer, int rozsah)
	{
		pridajZdroj(x, y, polomer, 0, rozsah, 1, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}.</p>
	 * 
	 * @param x x-ová súradnica stredu oblasti zdroja
	 * @param y y-ová súradnica stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(double x, double y, double polomer)
	{
		pridajZdroj(x, y, polomer, 0, rozsah - 1, 1, 0);
	}


	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}, pričom pri tejto verzii metódy sú súradnice
	 * polohy {@code x} a {@code y} nahradené inštanciou polohy {@code 
	 * poloha}.</p>
	 * 
	 * @param poloha poloha stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param trvanie dĺžka aktivity zdroja v tikoch
	 * @param oneskorenie odloženie začiatku aktivity zdroja v tikoch
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(Poloha poloha, double polomer,
		int dolnáHranica, int hornáHranica, int trvanie, int oneskorenie)
	{
		for (Zdroj zdroj : zdroje)
		{
			if (zdroj.trvanie <= 0)
			{
				zdroj.nastav(poloha.polohaX(), poloha.polohaY(), polomer,
					dolnáHranica, hornáHranica, trvanie, oneskorenie);
				return;
			}
		}

		zdroje.add(new Zdroj().nastav(poloha.polohaX(), poloha.polohaY(),
			polomer, dolnáHranica, hornáHranica, trvanie, oneskorenie));
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}, pričom pri tejto verzii metódy sú súradnice
	 * polohy {@code x} a {@code y} nahradené inštanciou polohy {@code 
	 * poloha}.</p>
	 * 
	 * @param poloha poloha stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param trvanie dĺžka aktivity zdroja v tikoch
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(Poloha poloha, double polomer,
		int dolnáHranica, int hornáHranica, int trvanie)
	{
		pridajZdroj(poloha, polomer,
			dolnáHranica, hornáHranica,
			trvanie, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}, pričom pri tejto verzii metódy sú súradnice
	 * polohy {@code x} a {@code y} nahradené inštanciou polohy {@code 
	 * poloha}.</p>
	 * 
	 * @param poloha poloha stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param dolnáHranica dolná hranica generovania náhodných hodnôt
	 *     intenzity
	 * @param hornáHranica horná hranica generovania náhodných hodnôt
	 *     intenzity
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(Poloha poloha, double polomer,
		int dolnáHranica, int hornáHranica)
	{
		pridajZdroj(poloha, polomer,
			dolnáHranica, hornáHranica,
			1, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}, pričom pri tejto verzii metódy sú súradnice
	 * polohy {@code x} a {@code y} nahradené inštanciou polohy {@code 
	 * poloha}.</p>
	 * 
	 * @param poloha poloha stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * @param rozsah horná hranica generovania náhodných hodnôt intenzity
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double)
	 */
	public void pridajZdroj(Poloha poloha, double polomer, int rozsah)
	{
		pridajZdroj(poloha, polomer, 0, rozsah, 1, 0);
	}

	/**
	 * <p>Pridá nový zdroj plazmy. Informácie o zdrojoch sú centralizované
	 * v opise metódy {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj(x, y, polomer, dolnáHranica, hornáHranica,
	 * trvanie, oneskorenie)}, pričom pri tejto verzii metódy sú súradnice
	 * polohy {@code x} a {@code y} nahradené inštanciou polohy {@code 
	 * poloha}.</p>
	 * 
	 * @param poloha poloha stredu oblasti zdroja
	 * @param polomer polomer oblasti zdroja
	 * 
	 * @see #pridajZdroj(double, double, double, int, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int, int)
	 * @see #pridajZdroj(double, double, double, int, int)
	 * @see #pridajZdroj(double, double, double, int)
	 * @see #pridajZdroj(double, double, double)
	 * 
	 * @see #pridajZdroj(Poloha, double, int, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int, int)
	 * @see #pridajZdroj(Poloha, double, int, int)
	 * @see #pridajZdroj(Poloha, double, int)
	 */
	public void pridajZdroj(Poloha poloha, double polomer)
	{
		pridajZdroj(poloha, polomer, 0, rozsah - 1, 1, 0);
	}


	/**
	 * <p>Táto metóda je predvolene prázdna. Je automaticky spúšťaná
	 * metódou {@link #pracuj() pracuj} ak je tento generátor plazmy
	 * {@linkplain #aktívny() aktívny}. Jej prekrytím sa dá upraviť
	 * správanie generátora v aktívnom stave. Inou možnosťou na
	 * ovplyvňovanie správania sa generátora plazmy je definícia
	 * {@linkplain #dej(Runnable) deja}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Príkazy {@linkplain #dej(Runnable) deja} sú vykonávané tesne pred
	 * prepočtom údajov generátora plazmy a príkazy {@linkplain #aktivita()
	 * aktivity} sú vykonané tesne po prepočte a prekreslení obsahu obrázka
	 * spárovaného s plazmou).</p>
	 * 
	 * @see #pasivita()
	 * @see #dej(Runnable)
	 * @see #pracuj()
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void aktivita() {}

	/**
	 * <p>Táto metóda je predvolene prázdna. Je automaticky spúšťaná
	 * metódou {@link #pracuj() pracuj} ak je generátor plazmy
	 * {@linkplain #pasívny() pasívny}. Jej prekrytím sa dá upraviť
	 * správanie generátora v pasívnom stave.</p>
	 * 
	 * @see #aktivita()
	 * @see #pracuj()
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void pasivita() {}


	/**
	 * <p>Táto metóda je vykonávaná automaticky pri každom tiku
	 * {@linkplain Svet#spustiČasovač() časovača} sveta. Ak je inštancia
	 * generátora plazmy {@linkplain #aktívny() aktívna}, tak sú vykonané
	 * príkazy {@linkplain #dej(Runnable) deja} (ak sú definované a ak je
	 * krok kladný), hneď potom sú prepočítané údaje plazmy a prekreslený
	 * obsah obrázka, s ktorým je inštancia tohto generátora plazmy
	 * prepojená a nakoniec je vykonaná metóda {@link #aktivita() aktivita}
	 * (čo má výzman pri inštanciách prekrytých tried). Ak je generátor
	 * {@linkplain #neaktívny() neaktívny}, tak je len vykonaná metóda
	 * {@link #pasivita() pasivita} (pričom opäť – má to výzman len pri
	 * inštanciách prekrytých tried).</p>
	 * 
	 * <p>Rôzne ďalšie informácie môžete nájsť v opisoch súvisiacich
	 * metód (v zozname nižšie).</p>
	 * 
	 * @see #aktivita()
	 * @see #pasivita()
	 * @see #krok()
	 * @see #dej()
	 * @see #aktívny()
	 * @see #neaktívny()
	 * @see #pasívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void pracuj()
	{
		if (aktívny)
		{
			if (dej > 0)
			{
				if (--dej <= 0)
				{
					dej = krok;
					if (null != vykonať)
						vykonať.run();
				}
			}

			for (Zdroj zdroj : zdroje) zdroj.pracuj();
			prepočítaj();

			int dĺžka = obrázok.údajeObrázka.length;
			for (int i = 0; i < dĺžka; ++i)
				obrázok.údajeObrázka[i] = paleta[údajePlazmy[i]];

			aktivita();
			Svet.neboloPrekreslené = true;
		}
		else pasivita();
	}


	/**
	 * <p>Posunie mapu plazmy o zadaný počet bodov v horizontálnom
	 * a/alebo vertikálnom smere. Tá časť údajov plazmy, ktorá opustí
	 * rozmery mapy, bude navždy stratená, pričom na protiľahlej strane
	 * vznikne prázdna oblasť (oblasť s nulovým horením). Metóda má
	 * využitie napríklad pri posune hracej plochy s horením uloženým
	 * v obrázku, pričom musíme zabezpečiť, aby boli vzniknuté prázdne
	 * časti mapy horenia korektne dogenerované. Ak je potrebné pretočiť
	 * údaje plazmy dookola (t. j. bez straty obsahu), tak treba použiť
	 * metódu {@link #pretoč(double, double) pretoč}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Pri použití tejto metódy sa zároveň posunú všetky zdroje plazmy
	 * (pozri metódu {@link #pridajZdroj(double, double, double, int, int,
	 * int, int) pridajZdroj}). Aj zdroje plazmy, ktoré sa nachádzajú za
	 * hranicami obrázka ešte môžu mať vplyv na obsah generátora, ak majú
	 * dostatočný rozsah.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b>
	 * Je dôležité si uvedomiť, že pri pripojení plazmy k obrázku nemá
	 * rolovanie alebo pretáčanie samotného obrázka zmysel, pretože jeho
	 * obsah je vždy úplne nahradený generátorom plazmy. Na pretáčanie
	 * obsahu takého obrázka má zmysel použiť iba metódy plazmy.</p>
	 * 
	 * @param Δx posun v horizontálnom (vodorovnom) smere
	 * @param Δy posun vo vertikálnom (zvislom) smere
	 */
	public void roluj(double Δx, double Δy)
	{
		int dx = (int)Δx; int dy = -(int)Δy;
		if (dx == 0 && dy == 0) return;

		for (Zdroj zdroj : zdroje)
		{
			if (zdroj.trvanie > 0)
			{
				zdroj.x += dx;
				zdroj.y += dy;
			}
		}

		if (dx >= obrázok.šírka || dx <= -obrázok.šírka ||
			dy >= obrázok.výška || dy <= -obrázok.výška)
		{
			Arrays.fill(údajeZálohy, 0);
			return;
		}

		int spodnáZarážka, vrchnáZarážka,
			zarážkaRiadkov, zarážkaStĺpcov;

		if (dy <= 0)
		{
			spodnáZarážka = 0;
			vrchnáZarážka = obrázok.šírka * -dy - dx;
			zarážkaRiadkov = obrázok.výška + dy;

			if (dx <= 0)
			{
				zarážkaStĺpcov = obrázok.šírka + dx;

				for (int i = 0; i < zarážkaRiadkov; ++i)
				{
					int j = 0;

					for (; j < zarážkaStĺpcov; ++j)
						údajeZálohy[spodnáZarážka + j] =
							údajeZálohy[vrchnáZarážka + j];

					for (; j < obrázok.šírka; ++j)
						údajeZálohy[spodnáZarážka + j] = 0;

					spodnáZarážka += obrázok.šírka;
					vrchnáZarážka += obrázok.šírka;
				}
			}
			else
			{
				zarážkaStĺpcov = dx;

				for (int i = 0; i < zarážkaRiadkov; ++i)
				{
					int j = obrázok.šírka - 1;

					for (; j >= zarážkaStĺpcov; --j)
						údajeZálohy[spodnáZarážka + j] =
							údajeZálohy[vrchnáZarážka + j];

					for (; j >= 0; --j)
						údajeZálohy[spodnáZarážka + j] = 0;

					spodnáZarážka += obrázok.šírka;
					vrchnáZarážka += obrázok.šírka;
				}
			}

			vrchnáZarážka = obrázok.šírka * obrázok.výška;

			while (spodnáZarážka < vrchnáZarážka)
			{
				údajeZálohy[spodnáZarážka] = 0;
				++spodnáZarážka;
			}
		}
		else
		{
			spodnáZarážka = -dx + obrázok.šírka * (obrázok.výška - 1 - dy);
			vrchnáZarážka = obrázok.šírka * (obrázok.výška - 1);
			zarážkaRiadkov = dy;

			if (dx <= 0)
			{
				zarážkaStĺpcov = obrázok.šírka + dx;

				for (int i = obrázok.výška - 1; i >= zarážkaRiadkov; --i)
				{
					int j = 0;

					for (; j < zarážkaStĺpcov; ++j)
						údajeZálohy[vrchnáZarážka + j] =
							údajeZálohy[spodnáZarážka + j];

					for (; j < obrázok.šírka; ++j)
						údajeZálohy[vrchnáZarážka + j] = 0;

					spodnáZarážka -= obrázok.šírka;
					vrchnáZarážka -= obrázok.šírka;
				}
			}
			else
			{
				zarážkaStĺpcov = dx;

				for (int i = obrázok.výška - 1; i >= zarážkaRiadkov; --i)
				{
					int j = obrázok.šírka - 1;

					for (; j >= zarážkaStĺpcov; --j)
						údajeZálohy[vrchnáZarážka + j] =
							údajeZálohy[spodnáZarážka + j];

					for (; j >= 0; --j)
						údajeZálohy[vrchnáZarážka + j] = 0;

					spodnáZarážka -= obrázok.šírka;
					vrchnáZarážka -= obrázok.šírka;
				}
			}

			vrchnáZarážka = (obrázok.šírka * dy) - 1;

			while (vrchnáZarážka >= 0)
			{
				údajeZálohy[vrchnáZarážka] = 0;
				--vrchnáZarážka;
			}
		}
	}

	/**
	 * <p>Pretočí mapu plazmy o zadaný počet bodov v horizontálnom
	 * a/alebo vertikálnom smere. Tá časť údajov plazmy, ktorá by mala
	 * pri pretočení opustiť rozmery mapy, sa premietne do protiľahlej
	 * strany mapy. Pretáčaním údajov plazmy v ľubovoľnom smere sa nikdy
	 * nestratia jej údaje a spätným posunom sa dá získať pôvodný stav
	 * (ak medzitým nebol vykonaný ďalší prepočet). Ak je z rôznych
	 * dôvodov potrebné, aby sa pri pretáčaní údajov plazmy tie časti,
	 * ktoré opúšťajú rozmery mapy stratili a aby vzniknuté prázdne časti
	 * zostali skutočne prázdne (pripravené na nové generovanie horenia),
	 * treba použiť metódu {@link #roluj(double, double) roluj}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b>
	 * Pri použití tejto metódy sa zároveň posunú (s pretočením) všetky
	 * zdroje plazmy (pozri metódu {@link #pridajZdroj(double, double,
	 * double, int, int, int, int) pridajZdroj}). Pretočenie zdroja nastane
	 * ak jeho súradnice opustia hranice obrázka priradeného k tomuto
	 * generátoru plazmy.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b>
	 * Je dôležité si uvedomiť, že pri pripojení plazmy k obrázku nemá
	 * rolovanie alebo pretáčanie samotného obrázka zmysel, pretože jeho
	 * obsah je vždy úplne nahradený generátorom plazmy. Na pretáčanie
	 * obsahu takého obrázka má zmysel použiť iba metódy plazmy.</p>
	 * 
	 * @param Δx posun v horizontálnom (vodorovnom) smere
	 * @param Δy posun vo vertikálnom (zvislom) smere
	 */
	public void pretoč(double Δx, double Δy)
	{
		int dx = (int)Δx; int dy = -(int)Δy;
		if (dx == 0 && dy == 0) return;

		System.arraycopy(údajeZálohy, 0,
			údajePlazmy, 0, údajeZálohy.length);

		while (dx < 0) dx += obrázok.šírka;
		while (dx >= obrázok.šírka) dx -= obrázok.šírka;

		while (dy < 0) dy += obrázok.výška;
		while (dy >= obrázok.výška) dy -= obrázok.výška;

		for (Zdroj zdroj : zdroje)
		{
			if (zdroj.trvanie > 0)
			{
				zdroj.x += dx;
				zdroj.y += dy;

				if (zdroj.x < obrázok.najmenšieX()) zdroj.x += obrázok.šírka;
				else if (zdroj.x > obrázok.najväčšieX())
					zdroj.x -= obrázok.šírka;

				if (zdroj.y < obrázok.najmenšieY()) zdroj.y += obrázok.výška;
				else if (zdroj.y > obrázok.najväčšieY())
					zdroj.y -= obrázok.výška;
			}
		}

		int index1 = 0, index2 = obrázok.šírka * (obrázok.výška + 1 - dy) - dx;

		for (int i = dy; i > 0; --i)
		{
			for (int j = dx; j > 0; --j)
			{
				údajeZálohy[index1++] =
					údajePlazmy[index2++];
			}

			index2 -= obrázok.šírka;

			for (int j = obrázok.šírka - dx; j > 0; --j)
			{
				údajeZálohy[index1++] =
					údajePlazmy[index2++];
			}

			index2 += obrázok.šírka;
		}

		index2 = obrázok.šírka - dx;

		for (int i = obrázok.výška - dy; i > 0; --i)
		{
			for (int j = dx; j > 0; --j)
			{
				údajeZálohy[index1++] =
					údajePlazmy[index2++];
			}

			index2 -= obrázok.šírka;

			for (int j = obrázok.šírka - dx; j > 0; --j)
			{
				údajeZálohy[index1++] =
					údajePlazmy[index2++];
			}

			index2 += obrázok.šírka;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pretoč(double, double) pretoč}.</p> */
	public void pretoc(double Δx, double Δy) { pretoč(Δx, Δy); }
}
