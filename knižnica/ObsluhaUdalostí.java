
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2023 by Roman Horváth
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

import java.awt.Color;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import java.io.IOException;

import static knižnica.Konštanty.VYKONAŤ_PRÍKAZ;

// -------------------------------- //
//  *** Trieda ObsluhaUdalostí ***  //
// -------------------------------- //

/**
 * <p>Trieda obsluhy udalostí slúži na obsluhu rozmanitých udalostí. Napríklad:
 * akcie myši, klávesnice, voľby položky ponuky, vymazania alebo
 * prekreslenia sveta… Keď chcete, aby aplikácia vytvorená s podporou
 * programovacieho rámca GRobot poskytovala funkcionalitu spojenú
 * s klávesnicou, myšou a podobne, je potrebné nasledujúcim spôsobom
 * vytvoriť inštanciu tejto triedy slúžiacej na obsluhu udalostí, pričom
 * stačí uviesť tie metódy (reakcie), ktoré plánujete využiť:</p>
 * 
 * <pre CLASS="example">
	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code comm// Časovač}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}() {}

		{@code comm// Klávesnica}
		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#zmenaFokusu(boolean) zmenaFokusu}({@code typeboolean} vpred) { {@code kwdreturn} {@code valtrue}; }
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#uvoľnenieKlávesu() uvoľnenieKlávesu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zadanieZnaku() zadanieZnaku}() {}

		{@code comm// Myš}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#pohybMyši() pohybMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ťahanieMyšou() ťahanieMyšou}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#rolovanieKolieskomMyši() rolovanieKolieskomMyši}() {}

		{@code comm// Rozhranie (položky, tlačidlá, skratky…)}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaPoložkyPonuky() voľbaPoložkyPonuky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaSystémovejIkony() voľbaSystémovejIkony}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaSystémovejPoložky() voľbaSystémovejPoložky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#aktiváciaOdkazu() aktiváciaOdkazu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klávesováSkratka() klávesováSkratka}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zmenaPosunuLišty() zmenaPosunuLišty}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ťahanieSúborov() ťahanieSúborov}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#pustenieSúboru(String) pustenieSúboru}({@link String String} súbor) {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ťahanieUkončené(JComponent, Transferable, int) ťahanieUkončené}({@link JComponent javax.swing.JComponent} zdroj, {@link Transferable java.awt.datatransfer.Transferable} údaje, {@code typeint} akcia) {}

		{@code comm// Prekresľovanie}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#prekreslenie() prekreslenie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#vymazanie() vymazanie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#dokreslenie() dokreslenie}() {}

		{@code comm// Sekvencia}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long, long) sekvencia}({@code typeint} kódSpracovania,
			{@link Object Object} zdroj, {@link Object Object} cieľ, {@code typelong} stav, {@code typelong} celkovo) {}

		{@code comm// Animácia}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zastavenieAnimácie(Obrázok) zastavenieAnimácie}({@link Obrázok Obrázok} obrázok) {}

		{@code comm// Okno}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#aktiváciaOkna() aktiváciaOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#deaktiváciaOkna() deaktiváciaOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#maximalizovanie() maximalizovanie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#minimalizovanie() minimalizovanie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#obnovenie() obnovenie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zobrazenieOkna() zobrazenieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#skrytieOkna() skrytieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#presunutieOkna() presunutieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zmenaVeľkostiOkna() zmenaVeľkostiOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#otvorenie() otvorenie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#zavretie() zavretie}() { {@code kwdreturn} {@code valtrue}; }
			{@code comm// Alternatíva: zatvorenie()}

		{@code comm// Ukončenie aplikácie}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ukončenie() ukončenie}() {}

		{@code comm// Konfigurácia}
		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}() { {@code kwdreturn} {@code valfalse}; }
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor) {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor) {}

		{@code comm// Konzola (vizuálna vlastnosť textu konzoly)}
		{@code comm// vyžaduje import java.awt.Color;}
		{@code kwd@}Override {@code kwdpublic} {@link Color Color} {@link ObsluhaUdalostí#farbaAktívnehoSlova(String) farbaAktívnehoSlova}({@link String String} slovo) { {@code kwdreturn} {@code valnull}; }

		{@code comm// Vstupný riadok}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov}() {}
			{@code comm// Alternatíva: potvrdenieVstupu()}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zrušenieÚdajov() zrušenieÚdajov}() {}
			{@code comm// Alternatíva: zrušenieVstupu()}

		{@code comm// Štandardný vstup}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#spracujRiadokVstupu(String) spracujRiadokVstupu}({@link String String} riadokVstupu) {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#koniecVstupu() koniecVstupu}() {}

		{@code comm// Príkazový riadok}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ukončenieProcesu(Svet.PríkazovýRiadok, int) ukončenieProcesu}(
			Svet.PríkazovýRiadok príkazovýRiadok, {@code typeint} návratovýKód) {}

		{@code comm// Ladenie a interaktívny režim}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#spracovaniePríkazu() spracovaniePríkazu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}({@code typeint} riadok, {@link String String} príkaz, {@code typeint} správa)
		{ {@code kwdreturn} {@code valsuper}.ladenie(riadok, príkaz, správa); }

		{@code comm// (Prípadne iné.)}
	};
	</pre>
 * 
 * <p>Musí byť presne dodržaná syntax metód, to jest vo väčšine prípadov:
 * {@code kwd@}{@code Override }{@code kwdpublic }{@code 
 * typevoid }<em>«názov reakcie»</em>{@code () }<code>{ … }</code>, kde
 * <em>«názov reakcie»</em> je jeden z názvov vyššie uvedených reakcií.
 * <small>(Klauzula {@code Override} slúži na kontrolu. Keby sme ju
 * neuviedli a omylom urobili v názve metódy (reakcie) hoci nepatrný
 * preklep, obsluha určenej udalosti by nefungovala (aplikácia by
 * nereagovala).)</small> Medzi zložené zátvorky sa <code>{}</code>,
 * samozrejme, vpisuje kód, ktorý bude spustený v prípade, že nastane
 * (vznikne) prislúchajúca udalosť (napríklad používateľ stlačí kláves,
 * tlačidlo myši a podobne…).</p>
 * 
 * <p>Vytváranie obsluhy udalostí sa dá obísť tak, že prekryjete
 * prislúchajúce metódy ľubovoľného robota, ktoré sú na tento účel pripravené:
 * {@link GRobot GRobot}{@code .}{@link GRobot#tik() tik}{@code ()},
 * {@link GRobot GRobot}{@code .}{@link GRobot#klik() klik}{@code ()}…
 * Rozhranie robotov však nemusí poskytnúť úplný zoznam metód obsluhy
 * udalostí. Napríklad neposkytuje metódu {@link #ladenie(int, String, int)
 * ladenie}. Pri tých metódach, ktoré má k dispozícii platí, že metódy
 * obsluhy udalostí majú prednosť. Z toho dôvodu je možné, že ak nastane
 * počas spracovania metódy obsluhy udalostí výnimka, ktorá nebude v jej
 * tele zachytená, tak spracovanie udalosti robotmi nemusí nastať.</p>
 * 
 * <p>Pre jeden svet je možné <b>spravidla</b> definovať len
 * <b>jednu</b> inštanciu obsluhy udalostí. <small>(Výnimku z tohto
 * pravidla spomenieme neskôr.)</small> Pri pokuse o vytvorenie
 * viacerých inštancií vznikne výnimka oznamujúca, že obsluha udalostí
 * už bola definovaná.</p>
 * 
 * <p>Nasledujúci príklad demonštruje vytvorenie obsluhy udalostí na
 * {@linkplain #klik() klik myšou}. Robotu najskôr nastavuje
 * {@linkplain GRobot#rýchlosť(double, boolean) rýchlosť}, inak by sa
 * nehýbal, a vzápätí je v obsluhe udalostí definované, aby sa robot po
 * {@linkplain #klik() kliknutí myšou} na plátno rozbehol smerom
 * {@linkplain GRobot#cieľNaMyš() na pozíciu, kam bolo kliknuté}.</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#rýchlosť(double, boolean) rýchlosť}({@code num10}, {@code valfalse});

	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link #klik() klik}()
		{
			{@link GRobot#cieľNaMyš() cieľNaMyš}();
		}
	};
	</pre>
 * 
 * <p>&nbsp;</p>
 * 
 * <!-- p><b>Ďalšie udalosti</b></p -->
 * <p><b>Rozšírená konfigurácia</b></p>
 * 
 * <!-- Udalosti zverejnené vo vyššie uvedenom zozname môžeme považovať za
 * „štandardné.“ (Z pohľadu programovacieho rámca GRobot.) Obsluha
 * udalostí podporuje ešte tri ďalšie udalosti, pomocou ktorých je možné
 * využiť automaticky vytváraný konfiguračný súbor programovacieho rámca
 * GRobot.</p -->
 * 
 * <p>Automatická konfigurácia sa spúšťa príkazom
 * {@link Svet Svet}<code>.</code>{@link Svet#použiKonfiguráciu()
 * použiKonfiguráciu} pred vytvorením sveta. Predvolene je v konfigurácii
 * uložená len informácia o veľkosti a polohe hlavného okna aplikácie.
 * K týmto údajom je možné pridať skupinu vlastných konfiguračných údajov
 * a to s využitím troch na to rezervovaných reakcií
 * ({@link ObsluhaUdalostí#konfiguráciaZmenená()
 * konfiguráciaZmenená()},
 * {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor)
 * zapíšKonfiguráciu(súbor)}
 * a {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor)
 * čítajKonfiguráciu(súbor)} – ich význam je podrobnejšie opísaný
 * v komentároch v príklade nižšie) nasledujúcim spôsobom:</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} TestKonfigurácie {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Stručný princíp:}
		{@code comm// }
		{@code comm//  1. Pre každú vlastnosť definujeme dvojicu atribútov.}
		{@code comm//     A) Jeden atribút bude slúžiť na uchovanie skutočnej hodnoty}
		{@code comm//        vlastnosti. S ním budeme pracovať v rámci celej aplikácie.}
		{@code comm//     B) Druhý atribút bude slúžiť na uchovanie počiatočnej (prečítanej)}
		{@code comm//        hodnoty vlastnosti. S ním budeme pracovať len pri čítaní}
		{@code comm//        počiatočnej hodnoty a overení toho, či nastala zmena hodnoty}
		{@code comm//        sledovanej vlastnosti.}
		{@code comm// }
		{@code comm//  2. Prekryjeme trojicu rekacií: konfiguráciaZmenená, zapíšKonfiguráciu}
		{@code comm//     a čítajKonfiguráciu.}
		{@code comm//     A) konfiguráciaZmenená – poskytuje od nás programovaciemu rámcu GRobot}
		{@code comm//        informáciu o tom, či je potrebné zapísať konfiguračný}
		{@code comm//        súbor, to jest, či bola konfigurácia zmenená…}
		{@code comm//     B) zapíšKonfiguráciu – slúži na zapísanie našich vlastných}
		{@code comm//        konfiguračných atribútov}
		{@code comm//     C) čítajKonfiguráciu – slúži na čítanie našich vlastných}
		{@code comm//        konfiguračných atribútov}
		{@code comm//}
		{@code comm//  3. Nesmieme zabudnúť spustiť príkaz Svet.použiKonfiguráciu… Tento}
		{@code comm//     príkaz musí byť spustený ešte pred inicializáciou (naštartovaním)}
		{@code comm//     sveta, to znamená – pred spustením konštruktora (pozri metódu main}
		{@code comm//     nižšie).}
		{@code comm//}
		<hr/>
		{@code comm// Definícia vlastných konfiguračných vlastností:}
		{@code kwdprivate} {@code typeboolean} vlastnosť = {@code valfalse};         {@code comm// „skutočná“ vlastnosť}
		{@code kwdprivate} {@code typeboolean} prečítanáVlastnosť = {@code valfalse}; {@code comm// „tieň“ – detektor zmien}

		{@code comm// Prípadne ďalšie dvojice…}
		{@code comm// ...}

		{@code kwdprivate} TestKonfigurácie()
		{
			{@code comm// Toto tlačidlo bude meniť stav vlastnosti:}
			{@code kwdfinal} {@link Tlačidlo Tlačidlo} tlačidlo = {@code kwdnew} {@link Tlačidlo#Tlačidlo(java.lang.String) Tlačidlo}({@code srg"Zapnúť"});

			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}()
				{
					{@code kwdif} (tlačidlo.{@link Tlačidlo#aktivované() aktivované}())
					{
						{@code comm// Ak bolo zvolené, zmení sa hodnota vlastnosti aj text}
						{@code comm// tlačidla}
						vlastnosť = !vlastnosť;
						tlačidlo.{@link Tlačidlo#text(java.lang.String) text}(vlastnosť ? {@code srg"Vypnúť"} : {@code srg"Zapnúť"});
					}
				}

				{@code comm// Na to, aby vlastná konfigurácia fungovala je nevyhnutné}
				{@code comm// naprogramovanie nasledujúcich troch reakcií:}

				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}()
				{
					{@code kwdif} (vlastnosť != prečítanáVlastnosť) {@code kwdreturn} {@code valtrue};
					{@code comm// ...}

					{@code kwdreturn} {@code valfalse};
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} {@code java.io.IOException}
				{
					{@code comm// Zapisujeme hodnoty skutočných vlastností:}
					{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(java.lang.String, java.lang.Object) zapíšVlastnosť}({@code srg"vlastnosť"}, vlastnosť);
					{@code comm// ...}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} {@code java.io.IOException}
				{
					{@code comm// Pri čítaní zmeníme naraz hodnotu skutočnej vlastnosti}
					{@code comm// aj hodnotu premennej prečítanáVlastnosť, ktorá slúži ako}
					{@code comm// detektor zmien a ktorá je vyhodnocovaná v reakcii}
					{@code comm// konfiguráciaZmenená.}
					vlastnosť = prečítanáVlastnosť = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(java.lang.String, java.lang.Boolean) čítajVlastnosť}(
						{@code srg"vlastnosť"}, vlastnosť).{@link Boolean#booleanValue() booleanValue}();
					{@code comm// ...}
				}
			};

			{@code comm// Pre korektnosť je po prečítaní konfigurácie vhodné upraviť}
			{@code comm// ovládacie prvky tak, aby ich vzhľad zodpovedal aktuálnej hodnote}
			{@code comm// vlastnosti s ktorou súvisia:}

			tlačidlo.{@link Tlačidlo#text(java.lang.String) text}(vlastnosť ? {@code srg"Vypnúť"} : {@code srg"Zapnúť"});
			{@code comm// ...}
		}

		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code comm// Dôležité je uviesť príkaz použiKonfiguráciu pred inicializáciou}
			{@code comm// aplikácie (pred spustením konštruktora). Inak by bol pokus}
			{@code comm// o vykonanie tohto príkazu zamietnutý}
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}({@code srg"test.cfg"});
			{@code kwdnew} TestKonfigurácie();
		}
	}
	</pre>
 * 
 * Na to, aby mala vlastná konfigurácia zmysel (aby sa aktuálne hodnoty
 * konfiguračných premenných vždy korektne uložili pri ukončení aplikácie
 * a prečítali pri jej opätovnom štarte), je potrebné prekryť
 * (naprogramovať obsah) všetkých troch reakcií: {@link 
 * #konfiguráciaZmenená() konfiguráciaZmenená}, {@link 
 * #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu} a {@link 
 * #čítajKonfiguráciu(Súbor) čítajKonfiguráciu}. Syntax pri
 * prekrývaní je potrebné detailne dodržať, to znamená, že pri tých
 * reakciách, pri ktorých je to vyžadované, je nevyhnutné uviesť parameter
 * {@code Súbor} aj klauzulu {@code kwdthrows}.
 * 
 * <p><b>Výnimka z pravidla jedinej obsluhy udalostí</b></p>
 * 
 * <p>Pôvodne nebolo možné v tomto smere v programovacom rámci GRobot robiť
 * žiadne výnimky. Z bezpečnostných dôvodov bolo v rámci jednej aplikácie
 * možné vytvoriť len jednu obsluhu udalostí a tá bola pre stanovenú aplikáciu
 * konečná. Možnosť {@linkplain Svet#povoľViacnásobnúObsluhuUdalostí()
 * povolenia vytvárania viacerých inštancií obsluhy udalostí} pribudla
 * v priebehu vývoja, no odporúčame túto možnosť využiť len v nevyhnutnom
 * prípade. Príklad, ktorý používa zapnutie viacnásobného spracovania udalostí
 * nájdete pri opise metódy {@link Svet#presmerujObsluhuUdalostí(ObsluhaUdalostí)
 * presmerujObsluhuUdalostí}.</p>
 * 
 * @see Svet#povoľViacnásobnúObsluhuUdalostí()
 * @see Svet#presmerujObsluhuUdalostí(ObsluhaUdalostí)
 */
public class ObsluhaUdalostí
{
	// TODO? (predbežne zavrhnuté)
	/*
		Definovať alternatívne obsluhy udalostí ku všetkým jestvujúcim
		reakciám:

			klik(Poloha poloha, int tlačidlo)
			…

			uvoľnenieKlávesu(int kláves)
			zadanieZnaku(char znak)
			…

			voľbaPoložkyPonuky(PoložkaPonuky položka)
			voľbaKontextovejPoložky(KontextováPoložka položka)
			…

			vymazanie(Plátno vymazané)
			…

			presunutieOkna(ComponentEvent udalosť)
			zmenaVeľkostiOkna(ComponentEvent udalosť)
			aktiváciaOkna(WindowEvent udalosť)
			otvorenie(WindowEvent udalosť)
			zavretie(WindowEvent udalosť)
			…

			potvrdenieÚdajov(String údaje)

	*/

	// Počúvadlo udalostí
	/*packagePrivate*/ static ObsluhaUdalostí počúvadlo;

	// Príznak umožnenia vytvorenia viacerých inštancií obsluhy udalostí
	/*packagePrivate*/ static boolean povoľViacnásobnúObsluhuUdalostí = false;



	/**
	 * <p>Vráti aktuálny objekt obsluhy udalostí (aktívnu obsluhu udalostí)
	 * alebo {@code valnull}, ak nie je aktívna žiadna obsluha udalostí.
	 * (Túto metódu nie je možné prekryť – neslúži na obsluhu žiadnej
	 * udalosti.)</p>
	 * 
	 * @return aktívna obsluha udalostí
	 * 
	 * @see Svet#aktívnaObsluhaUdalostí()
	 */
	public final static ObsluhaUdalostí aktívna() { return počúvadlo; }

	/** <p><a class="alias"></a> Alias pre {@link #aktívna() aktívna}.</p> */
	public final static ObsluhaUdalosti aktivna()
	{ return (ObsluhaUdalosti)počúvadlo; }


	/**
	 * <p>Predvolený konštruktor obsluhy udalostí.</p>
	 * 
	 * @throws GRobotException ak nie je povolená viacnásobná obsluha
	 *     udalostí a už bola vytvorená inštancia obsluhy udalostí
	 *     (identifikátor {@code eventFactoryAlreadyExists})
	 */
	public ObsluhaUdalostí()
	{
		if (povoľViacnásobnúObsluhuUdalostí)
		{
			if (null == počúvadlo)
			{
				počúvadlo = this;
				Svet.obnovKonfiguráciu();
			}
		}
		else
		{
			if (null != počúvadlo)
				throw new GRobotException(
					"Obsluha udalostí už bola definovaná!",
					"eventFactoryAlreadyExists");
			počúvadlo = this;
			Svet.obnovKonfiguráciu();
		}
	}

	/**
	 * <p>Spustená v pravidelnom časovom intervale časovača, ktorý sa dá
	 * odštartovať metódou {@link Svet#spustiČasovač(double)
	 * spustiČasovač} a zastaviť metódou {@link Svet#zastavČasovač()
	 * zastavČasovač}. Informácie o poslednej udalosti časovača sa dajú
	 * získať pomocou metódy {@link ÚdajeUdalostí#tik()
	 * ÚdajeUdalostí.tik()}.</p>
	 * 
	 * <p>Časovač pre všetky roboty automaticky spúšta metódu {@link 
	 * GRobot#pracuj() pracuj} a časovač môže byť niektorými metódami
	 * spustený automaticky. Pozri napríklad: {@link GRobot#rýchlosť(double)
	 * rýchlosť}, {@link GRobot#uhlováRýchlosť(double) uhlováRýchlosť}…</p>
	 * 
	 * @see ÚdajeUdalostí#tik()
	 * @see GRobot#tik()
	 * @see Svet#tik()
	 */
	public void tik() {}

	/**
	 * <p>Spustená pri kliknutí tlačidlom myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#klik()
	 */
	public void klik() {}

	/**
	 * <p>Spustená pri zvolení položky ponuky. Na získanie naposledy zvolenej
	 * položky ponuky použite metódu {@link ÚdajeUdalostí#položkaPonuky()
	 * ÚdajeUdalostí.položkaPonuky()} alebo využite metódu {@link 
	 * PoložkaPonuky#aktivovaná() aktivovaná} triedy {@link PoložkaPonuky
	 * PoložkaPonuky} na overenie aktivácie konkrétnej položky ponuky.</p>
	 * 
	 * @see GRobot#voľbaPoložkyPonuky()
	 */
	public void voľbaPoložkyPonuky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaPoložkyPonuky() voľbaPoložkyPonuky}.</p> */
	public void volbaPolozkyPonuky() {}

	/**
	 * <p>Spustená pri zvolení kontextovej položky. Na získanie naposledy
	 * zvolenej kontextovej položky použite metódu {@link 
	 * ÚdajeUdalostí#kontextováPoložka() ÚdajeUdalostí.kontextováPoložka()}
	 * alebo využite metódu {@link KontextováPoložka#aktivovaná()
	 * aktivovaná} triedy {@link KontextováPoložka KontextováPoložka} na
	 * overenie aktivácie konkrétnej kontextovej položky.</p>
	 * 
	 * @see GRobot#voľbaKontextovejPoložky()
	 */
	public void voľbaKontextovejPoložky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaKontextovejPoložky() voľbaKontextovejPoložky}.</p> */
	public void volbaKontextovejPolozky() {}


	/**
	 * <p>Spustená pri zvolení tlačidla. Použite metódu {@link 
	 * ÚdajeUdalostí#tlačidlo() ÚdajeUdalostí.tlačidlo()}
	 * na získanie inštancie naposledy zvoleného tlačidla, prípadne
	 * metódu {@link Tlačidlo Tlačidlo}{@code .}{@link 
	 * Tlačidlo#aktivované() aktivované}{@code ()} na overenie toho, či
	 * bolo toto tlačidlo naposledy aktivované. Praktický príklad
	 * nájdete v opise triedy {@link Tlačidlo Tlačidlo}.</p>
	 * 
	 * @see Tlačidlo
	 * @see GRobot#voľbaTlačidla()
	 */
	public void voľbaTlačidla() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaTlačidla() voľbaTlačidla}.</p> */
	public void volbaTlacidla() {}


	/**
	 * <p>Spustená pri zmene posunu rolovacej lišty. Použite metódu
	 * {@link ÚdajeUdalostí#rolovaciaLišta() ÚdajeUdalostí.rolovaciaLišta()}
	 * na získanie inštancie posunutej lišty, metódu
	 * {@link ÚdajeUdalostí#posunRolovacejLišty()
	 * ÚdajeUdalostí.posunRolovacejLišty()} na získanie konkrétnej
	 * hodnoty posunu lišty alebo metódu
	 * {@link ÚdajeUdalostí#udalosťPosunu() ÚdajeUdalostí.udalosťPosunu()}
	 * na získanie objektu udalosti rolovania
	 * {@link AdjustmentEvent AdjustmentEvent}.
	 * Praktický príklad nájdete v opise triedy {@link RolovaciaLišta
	 * RolovaciaLišta}.</p>
	 * 
	 * @see RolovaciaLišta
	 * @see GRobot#zmenaPosunuLišty()
	 */
	public void zmenaPosunuLišty() {}

	/** <p><a class="alias"></a> Alias pre {@link #zmenaPosunuLišty() zmenaPosunuLišty}.</p> */
	public void zmenaPosunuListy() {}


	/**
	 * <p>Spustená pri zvolení {@linkplain Svet#systémováIkona(String,
	 * Image, String...) systémovej ikony}. Použite metódu {@link 
	 * ÚdajeUdalostí#udalosťSystémovejIkony()
	 * ÚdajeUdalostí.udalosťSystémovejIkony()} na získanie všetkých
	 * údajov o tejto udalosti.</p>
	 * 
	 * @see ÚdajeUdalostí#udalosťSystémovejIkony()
	 * @see Svet#systémováIkona(String, Image, String...)
	 * @see #voľbaSystémovejPoložky()
	 */
	public void voľbaSystémovejIkony() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaSystémovejIkony() voľbaSystémovejIkony}.</p> */
	public void volbaSystemovejIkony() {}

	/**
	 * <p>Spustená pri zvolení položky kontextovej ponuky {@linkplain 
	 * Svet#systémováIkona(String, Image, String...) systémovej ikony}.
	 * Použite metódu {@link ÚdajeUdalostí#udalosťSystémovejIkony()
	 * ÚdajeUdalostí.udalosťSystémovejIkony()} na získanie všetkých
	 * údajov o tejto udalosti.</p>
	 * 
	 * @see ÚdajeUdalostí#udalosťSystémovejIkony()
	 * @see Svet#systémováIkona(String, Image, String...)
	 * @see #voľbaSystémovejIkony()
	 */
	public void voľbaSystémovejPoložky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaSystémovejPoložky() voľbaSystémovejPoložky}.</p> */
	public void volbaSystemovejPolozky() {}


	/**
	 * <p>Spustená po vymazaní grafiky sveta. Táto reakcia môže
	 * byť použitá na obnovenie (znovu nakreslenie) grafického obsahu
	 * {@linkplain Plátno plátien} po úplnom vymazaní. Je to
	 * alternatívny spôsob. Robot má dostatok nástrojov (metód) na
	 * kreslenie (a kreslenie každého robota môže byť nasmerované ako na
	 * {@linkplain GRobot#kresliNaPodlahu() podlahu}, tak na {@linkplain 
	 * GRobot#kresliNaStrop() strop}). Ak potrebujete využívať metódy
	 * triedy {@link Graphics2D Graphics2D}, použite metódu {@link 
	 * Plátno#grafika() podlaha.grafika()} (resp. {@link 
	 * Plátno#grafika() strop.grafika()}) na prístup k nej. Majte
	 * na pamäti, že grafika plátna pracuje s odlišným súradnicovým
	 * priestorom, než grafický programovací rámec.</p>
	 * 
	 * <p>V súčasnej verzii rámca je možné rozlišovať, ktoré plátno bolo
	 * práve vymazané. Slúžia na to metódy {@linkplain ÚdajeUdalostí údajov
	 * udalostí}: {@link ÚdajeUdalostí#vymazanéPlátno() vymazanéPlátno()}
	 * a {@link ÚdajeUdalostí#vymazanéPlátno(Plátno)
	 * vymazanéPlátno(plátno)}</p>
	 * 
	 * @see GRobot#vymazanie()
	 */
	public void vymazanie() {}

	/**
	 * <p>Spustená pri (pred) prekreslení sveta. Táto reakcia môže byť
	 * využitá na dokreslenie obsahu {@linkplain Plátno plátien}
	 * (ich „finálne úpravy“) pred prekreslením sveta. Robot má dostatok
	 * nástrojov (metód) na kreslenie (a kreslenie každého robota môže
	 * byť nasmerované ako na {@linkplain GRobot#kresliNaPodlahu()
	 * podlahu}, tak na {@linkplain GRobot#kresliNaStrop() strop}). Ak
	 * potrebujete využívať metódy (nástroje) triedy {@link Graphics2D
	 * Graphics2D}, použite metódu {@link Plátno#grafika()
	 * podlaha.grafika()} (resp. {@link Plátno#grafika()
	 * strop.grafika()}) na prevzatie grafického objektu, pomocou
	 * ktorého môžete tiež dokresľovať obsah plátien (v podstate
	 * rovnocenne ako pri kreslení pomocou robota, avšak treba mať na
	 * pamäti, že grafika plátna pracuje s odlišným súradnicovým
	 * priestorom, než grafický programovací rámec).</p>
	 * 
	 * @see GRobot#prekreslenie()
	 */
	public void prekreslenie() {}

	/**
	 * <p>Spustená po prekreslení sveta. Funguje podobne ako reakcia {@link 
	 * #prekreslenie() prekreslenie}, ibaže je spustená po prekreslení
	 * sveta, tesne pred finálnym zobrazením grafiky v okne (na
	 * obrazovke). Takže v tomto momente je čas na posledné finálne
	 * úpravy obsahu, ktorý bude reálne zobrazený na obrazovke. Čokoľvek
	 * sa v tomto momente pokúsite nakresliť na {@linkplain 
	 * Plátno plátno} podlahy alebo stropu, už nestihne byť
	 * zobrazené v tomto pracovnom cykle, avšak môžete použiť grafický
	 * objekt sveta (získaný metódou {@link Svet#grafika()
	 * Svet.grafika()}) na dokreslenie (krátkodobého) grafického obsahu.
	 * Treba podotknúť že spomenutý objekt {@code grafika} pracuje
	 * v súradnicovom priestore Javy a všetko, čo pomocou neho
	 * nakreslíte, bude na obrazovke zobrazené len do najbližšieho
	 * prekreslenia.
	 * (O súradnicových priestoroch sa podrobnejšie píše napríklad
	 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
	 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
	 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
	 * v celej dokumentácii.)</p>
	 * 
	 * @see GRobot#dokreslenie()
	 */
	public void dokreslenie() {}

	/**
	 * <p>Spustená pri zobrazení okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#zobrazenieOkna()
	 */
	public void zobrazenieOkna() {}

	/**
	 * <p>Spustená pri skrytí okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak {@linkplain Svet#skry()
	 * skryjete} okno sveta a neponecháte systému žiadnu možnosť
	 * interakcie s aplikáciou (napríklad {@linkplain 
	 * Svet#systémováIkona(String, Image, String...) systémovú ikonu}),
	 * tak virtuálny stroj Javy po chvíli Vašu aplikáciu (pri nečinnosti)
	 * automaticky ukončí.</p>
	 * 
	 * @see GRobot#skrytieOkna()
	 */
	public void skrytieOkna() {}

	/**
	 * <p>Spustená pri presunutí okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#presunutieOkna()
	 */
	public void presunutieOkna() {}

	/**
	 * <p>Spustená pri zmene veľkosti okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * <p>Po vykonaní tejto metódy pre svet je v prípade, že sa pracovalo
	 * s {@linkplain Plátno#vypíš(Object[]) výpismi textov} na podlahu
	 * alebo strop, spustené automatické prekreslenie. Ak je automatické
	 * prekreslenie {@linkplain Svet#nekresli() vypnuté}, musí sa
	 * o prekreslenie sveta pri zmene veľkosti okna {@linkplain 
	 * Svet#prekresli() postarať programátor}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#zmenaVeľkostiOkna()
	 */
	public void zmenaVeľkostiOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #zmenaVeľkostiOkna() zmenaVeľkostiOkna}.</p> */
	public void zmenaVelkostiOkna() {}

	/**
	 * <p>Spustená pri aktivácii okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#aktiváciaOkna()
	 */
	public void aktiváciaOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #aktiváciaOkna() aktiváciaOkna}.</p> */
	public void aktivaciaOkna() {}

	/**
	 * <p>Spustená pri deaktivácii okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#deaktiváciaOkna()
	 */
	public void deaktiváciaOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #deaktiváciaOkna() deaktiváciaOkna}.</p> */
	public void deaktivaciaOkna() {}

	/**
	 * <p>Spustená pri maximalizovaní okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#maximalizovanie()
	 */
	public void maximalizovanie() {}

	/**
	 * <p>Spustená pri minimalizovaní okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#minimalizovanie()
	 */
	public void minimalizovanie() {}

	/**
	 * <p>Spustená pri obnovení okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#obnovenie()
	 */
	public void obnovenie() {}

	/**
	 * <p>Spustená pri prvom zobrazení okna. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#otvorenie()
	 */
	public void otvorenie() {}

	/**
	 * <p>Spustená pri zatváraní okna, pričom vrátením návratovej
	 * hodnoty {@code valfalse} sa dá tejto aktivite zabrániť. Na získanie
	 * objektu s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see GRobot#zavretie()
	 * @see Svet#zavrieť()
	 * @see Svet#koniec()
	 */
	public boolean zavretie() { return true; }

	/** <p><a class="alias"></a> Alias pre {@link #zavretie() zavretie}.</p> */
	public boolean zatvorenie() { return true; }


	/**
	 * <p>Spustená pri ukončení aplikácie. V tejto reakcii je možné vykonať
	 * niektoré záverečné upratovacie akcie, napríklad uloženie stavu
	 * aplikácie a podobne.</p>
	 * 
	 * <p>Priorita spúšťania tejto obsluhy udalosti prekrytej v niektorom
	 * robote a v obsluhe udalostí je upravená tak, že udalosť v obsluhe
	 * udalostí je spustená pred automatickým uložením konfigurácie a udalosť
	 * v robote (prípadne vo viacerých robotoch) po ňom.</p>
	 * 
	 * @see GRobot#ukončenie()
	 */
	public void ukončenie() {}

	/** <p><a class="alias"></a> Alias pre {@link #ukončenie() ukončenie}.</p> */
	public void ukoncenie() {}

	/**
	 * <p>Spustená po stlačení klávesu {@code Enter} vo vstupnom riadku
	 * zobrazenom po zavolaní metódy {@link Svet#začniVstup()
	 * začniVstup}. Na získanie potvrdených údajov použite metódy
	 * {@link Svet#prevezmiReťazec() prevezmiReťazec},
	 * {@link Svet#prevezmiCeléČíslo() prevezmiCeléČíslo} alebo
	 * {@link Svet#prevezmiReálneČíslo() prevezmiReálneČíslo}.
	 * V prípade, že potvrdené údaje nie sú v správnom tvare (platí pre
	 * celé alebo reálne číslo), je prislúchajúcou metódou „prevezmi“
	 * vrátené {@code valnull}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nepracuje
	 * so štandardným vstupom údajov, ktorý je schopný prijímať údaje
	 * z iného procesu. Pozri reakciu {@link #spracujRiadokVstupu(String)
	 * spracujRiadokVstupu}.</p>
	 * 
	 * @see GRobot#potvrdenieÚdajov()
	 */
	public void potvrdenieÚdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #potvrdenieÚdajov() potvrdenieÚdajov}.</p> */
	public void potvrdenieUdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #potvrdenieÚdajov() potvrdenieÚdajov}.</p> */
	public void potvrdenieVstupu() {}

	/**
	 * <p>Spustená po pozitívnom spracovaní príkazu v {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívnom režime}.</p>
	 * 
	 * @see GRobot#spracovaniePríkazu()
	 */
	public void spracovaniePríkazu() {}

	/** <p><a class="alias"></a> Alias pre {@link #spracovaniePríkazu() spracovaniePríkazu}.</p> */
	public void spracovaniePrikazu() {}

	/**
	 * <p>Spustená po stlačení klávesu {@code ESC} vo vstupnom riadku
	 * zobrazenom po zavolaní metódy {@link Svet#začniVstup()
	 * začniVstup}.
	 * Reakciou na zrušenie vstupu môže byť napríklad {@linkplain 
	 * Svet#koniec() ukončenie} primitívnej priamočiarej aplikácie
	 * vyžadujúcej na svouju činnosť potvrdenie vstupu.</p>
	 * 
	 * @see GRobot#zrušenieÚdajov()
	 */
	public void zrušenieÚdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrusenieUdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrušenieVstupu() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrusenieVstupu() {}

	/**
	 * <p>Asynchrónne prijíma údaje (ak sú k dispozícii) zo štandardného
	 * vstupu (externého zdroja), ktorý bol aktivovaný metódou {@link 
	 * Svet#aktivujŠtandardnýVstup() aktivujŠtandardnýVstup} (alebo
	 * niektorou jej verziou).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nepracuje
	 * so zabudovaným vstupným riadkom programovacieho rámca. Pozri reakciu
	 * {@link #potvrdenieÚdajov() potvrdenieÚdajov}.</p>
	 * 
	 * @param riadokVstupu riadok údajov prijatý zo štandardného vstupu
	 * 
	 * @see GRobot#spracujRiadokVstupu(String)
	 * @see #koniecVstupu()
	 * @see Svet#aktivujŠtandardnýVstup()
	 * @see Svet#aktivujŠtandardnýVstup(String)
	 * @see Svet#štandardnýVstupAktívny()
	 * @see Svet#čakajNaVstup()
	 */
	public void spracujRiadokVstupu(String riadokVstupu) {}

	/**
	 * <p>Táto reakcia je spustená len v prípade, že štandardný vstup
	 * (aktivovaný metódou {@link Svet#aktivujŠtandardnýVstup()
	 * aktivujŠtandardnýVstup} alebo niektorou jej verziou) je konečný.
	 * Jej spustenie znamená, že prúd údajov prijímaný zo štandardného
	 * vstupu dosiahol svoj koniec.</p>
	 * 
	 * @see GRobot#koniecVstupu()
	 * @see #spracujRiadokVstupu(String)
	 * @see Svet#aktivujŠtandardnýVstup()
	 * @see Svet#aktivujŠtandardnýVstup(String)
	 * @see Svet#štandardnýVstupAktívny()
	 * @see Svet#čakajNaVstup()
	 */
	public void koniecVstupu() {}


	/**
	 * <p>Táto reakcia je spustená v prípade, že bola nahlásená chyba
	 * prostriedkami programovacieho rámca. Medzi nich patrí
	 * generovanie výnimiek programovacieho rámca ({@link GRobotException
	 * GRobotException}) a vnútorný mechanizmus hlásenia rôznych (chybových)
	 * stavov rámca.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Všetky chyby sú zároveň
	 * zaznamenávané do {@linkplain GRobotException#denník denníka chýb}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Výpisy chýb môžu byť rôznych úrovní. Niektoré spôsoby ukazuje
	 * tento príklad.</p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} VzniklaChyba {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} VzniklaChyba()
			{
				{@link GRobot#obrázok(String) obrázok}({@code srg"obrazok-nejestvuje"});
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#vzniklaChyba(GRobotException.Chyba) vzniklaChyba}({@link GRobotException GRobotException}.{@link GRobotException.Chyba#GRobotException.Chyba Chyba} chyba)
			{
				{@code kwdif} ({@code valnull} != chyba.{@link GRobotException.Chyba#správa správa})
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(chyba.{@link GRobotException.Chyba#správa správa});

				{@code kwdif} ({@code valnull} != chyba.{@link GRobotException.Chyba#výnimka výnimka})
				{
					{@code kwdif} ({@code valnull} == chyba.{@link GRobotException.Chyba#výnimka výnimka}.{@link Throwable#getMessage() getMessage}())
						Svet.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@link GRobotException GRobotException}.
							{@link GRobotException#stackTraceToString(Throwable) stackTraceToString}(chyba.{@link GRobotException.Chyba#výnimka výnimka}));
					{@code kwdelse}
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(chyba.{@link GRobotException.Chyba#výnimka výnimka}.{@link Throwable#getMessage() getMessage}());
				}

				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"———"});
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
			{
				{@code kwdnew} {@link GRobotException#GRobotException(String, String) GRobotException}({@code srg"Test výnimky"}, {@code valnull});
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@code kwdnew} VzniklaChyba();
			}
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <p><image>vznikla-vynimka.png<alt/>Ukážka výpisov chýb.</image>Ukážka
	 * výsledku výpisu chýb.</p>
	 * 
	 * @param chyba inštancia obsahujúca podrobnosti o chybe; pozri {@link 
	 *     GRobotException.Chyba GRobotException.Chyba}
	 * 
	 * @see GRobot#vzniklaChyba(GRobotException.Chyba)
	 * @see GRobotException
	 * @see GRobotException.Chyba
	 */
	public void vzniklaChyba(GRobotException.Chyba chyba) {}


	/**
	 * <p>Spustená pri stlačení tlačidla myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#stlačenieTlačidlaMyši()
	 */
	public void stlačenieTlačidlaMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}.</p> */
	public void stlacenieTlacidlaMysi() {}

	/**
	 * <p>Spustená pri uvoľnení tlačidla myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#uvoľnenieTlačidlaMyši()
	 */
	public void uvoľnenieTlačidlaMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}.</p> */
	public void uvolnenieTlacidlaMysi() {}

	/**
	 * <p>Spustená pri pohybe myši nad plátnom podlahy. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#pohybMyši()
	 */
	public void pohybMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #pohybMyši() pohybMyši}.</p> */
	public void pohybMysi() {}

	/**
	 * <p>Spustená pri pohybe myši nad hlavným komponentom okna (pri svete
	 * hovoríme často o plátnach) počas držania tlačidla myši.
	 * Použite metódu {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši je
	 * aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#ťahanieMyšou()
	 */
	public void ťahanieMyšou() {}

	/** <p><a class="alias"></a> Alias pre {@link #ťahanieMyšou() ťahanieMyšou}.</p> */
	public void tahanieMysou() {}

	/**
	 * <p>Spustená pri rolovaní kolieskom myši. Použite metódu {@link 
	 * ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} alebo {@link 
	 * ÚdajeUdalostí#kolieskoMyši() ÚdajeUdalostí.kolieskoMyši()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši je
	 * aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see ÚdajeUdalostí#rolovanieKolieskomMyšiX()
	 * @see ÚdajeUdalostí#rolovanieKolieskomMyšiY()
	 * @see GRobot#rolovanieKolieskomMyši()
	 */
	public void rolovanieKolieskomMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #rolovanieKolieskomMyši() rolovanieKolieskomMyši}.</p> */
	public void rolovanieKolieskomMysi() {}

	/**
	 * <p>Spustená pri stlačení klávesovej kombinácie zmeny fokusu
	 * niektorého z komponentov. Použite metódu {@link 
	 * ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()} na získanie
	 * podrobnejších údajov o tejto udalosti alebo metódu {@link 
	 * ÚdajeUdalostí#komponentFokusu() ÚdajeUdalostí.komponentFokusu()} na
	 * získanie komponentu, ktorý je aktuálnym vlastníkom fokusu v kontexte
	 * pokusu o jeho zmenu touto udalosťou. Užitočné sú aj ďalšie metódy
	 * triedy {@link ÚdajeUdalostí ÚdajeUdalostí} uvedené v zozname
	 * nižšie.</p>
	 * 
	 * <p>Táto reakcia je volaná vždy pred reakciou {@link #stlačenieKlávesu()
	 * stlačenieKlávesu}. Programátor v nej môže overiť, kde sa práve nachádza
	 * reťaz komponentov fokusu a či ju chce dočasne prerušiť, alebo nie.
	 * Podľa toho je povinný určiť návratovú hodnotu tejto reakcie:</p>
	 * 
	 * <ul>
	 * 	<li>{@code valtrue} – reťaz fokusu má pokračovať bez prerušenia
	 * 	(programátor nepotreboval vykonať žiadnu akciu a aplikácia nemá v tejto
	 * 	situácii nijako upravovať svoje predvolené správanie);</li>
	 * 	<li>{@code valfalse} – programátor prerušil reťaz fokusu – programátor
	 * 	určil, či a kam sa má fokus presunúť, predvolený mechanizmus zmeny
	 * 	fokusu bude potlačený.</li>
	 * </ul>
	 * 
	 * @param vpred ak je hodnotota tohto parametra rovná {@code valtrue}, tak
	 *     ide o udalosť zmeny fokusu vpred, v opačnom prípade je hodnota
	 *     parametra rovná {@code valfalse}
	 * @return návratová hodnota určuje, či má aplikácia ďalej spracovať túto
	 *     udalosť predvoleným mechanizmom zmeny fokusu, alebo nie
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#zmenaFokusu(boolean)
	 */
	public boolean zmenaFokusu(boolean vpred) { return true; }

	/**
	 * <p>Spustená pri stlačení klávesu. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#kláves() kláves()}
	 * a {@link ÚdajeUdalostí#kláves(int) kláves(int)}</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#stlačenieKlávesu()
	 */
	public void stlačenieKlávesu() {}

	/** <p><a class="alias"></a> Alias pre {@link #stlačenieKlávesu() stlačenieKlávesu}.</p> */
	public void stlacenieKlavesu() {}

	/**
	 * <p>Spustená pri uvoľnení klávesu. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#kláves() kláves()}
	 * a {@link ÚdajeUdalostí#kláves(int) kláves(int)}</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#uvoľnenieKlávesu()
	 */
	public void uvoľnenieKlávesu() {}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľnenieKlávesu() uvoľnenieKlávesu}.</p> */
	public void uvolnenieKlavesu() {}

	/**
	 * <p>Spustená pri zadaní znaku z klávesnice. Nie každý kláves generuje
	 * túto udalosť. Táto udalosť vzniká len pri stlačení a uvoľnení
	 * klávesu, ktorý produkuje znak, to znamená, že pri klávesoch ako
	 * kurzorové šípky, {@code Shift}, {@code Ctrl}, {@code Cmd} a podobne
	 * táto udalosť nevzniká. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#znak() znak()}
	 * a {@link ÚdajeUdalostí#znak(char) znak(char)}</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#zadanieZnaku()
	 */
	public void zadanieZnaku() {}


	/**
	 * <p>Spustená pri zvolení {@linkplain Svet#pridajKlávesovúSkratku(String,
	 * int, int) klávesovej skratky}. Použite metódu {@link 
	 * ÚdajeUdalostí#príkazSkratky() ÚdajeUdalostí.príkazSkratky()} na
	 * získanie textu príkazu súvisiaceho s touto klávesovou skratkou,
	 * prípadne metódu {@link ÚdajeUdalostí#udalosťSkratky()
	 * ÚdajeUdalostí.udalosťSkratky()} na získanie doplňujúcich údajov
	 * o tejto udalosti.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Príklad použitia nájdete
	 * napríklad v opise vnorenej triedy {@link Svet.PríkazovýRiadok
	 * Svet.PríkazovýRiadok}.</p>
	 * 
	 * @see ÚdajeUdalostí#príkazSkratky()
	 * @see ÚdajeUdalostí#udalosťSkratky()
	 * @see GRobot#klávesováSkratka()
	 */
	public void klávesováSkratka() {}

	/** <p><a class="alias"></a> Alias pre {@link #klávesováSkratka() klávesováSkratka}.</p> */
	public void klavesovaSkratka() {}


	/* *
	 * <p>Táto reakcia je spustená vždy, keď sa má aplikácia vyjadriť, či je
	 * ochotná akceptovať pustenie súboru alebo nie. Aplikácia to môže
	 * zvážiť napríklad podľa polohy kurzora myši. Predvolenie je pustenie
	 * súboru zakázané.</p>
	 * 
	 * <p>Príklad najjednoduchšieho spracovania pustených súborov je
	 * v opise metódy {@link #pustenieSúboru(String súbor)}.</p>
	 * 
	 * @return {@code valtrue} ak má byť pustenie súboru povolené,
	 *     inak {@code valfalse}
	 * /
	public boolean povoliťPustenieSúboru() { return false; }

	/** <p><a class="alias"></a> Alias pre {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}.</p> * /
	public boolean povolitPustenieSuboru() { return false; }

	Treba povoliť pustenie súborov prekrytím
	 * metódy {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}
	 * a spracovať súbory v tejto metóde (t. j. v metóde {@code 
	 * currpustenieSúboru} – tu ich len vypisujeme):

		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}()
		{
			{@code kwdreturn} {@code valtrue};
		}*/

	/**
	 * <p>Táto reakcia je spúšťaná počas ťahania súboru a lebo súborov nad
	 * hlavným komponentom okna. To umožňuje aplikácii v prípade potreby
	 * graficky zareagovať.</p>
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 */
	public void ťahanieSúborov() {}

	/** <p><a class="alias"></a> Alias pre {@link #ťahanieSúborov() ťahanieSúborov}.</p> */
	public void tahanieSuborov() {}

	/**
	 * <p>Spustená po dokončení ťahania súboru z externej aplikácie. Ak bolo
	 * z externej aplikácie potiahnutých viac súborov, tak bude táto
	 * reakcia spustená pre každý z nich osobitne.</p>
	 * 
	 * <!-- p>Ak je {@linkplain ÚdajeUdalostí#oknoUdalosti() okno udalosti} rovné
	 * {@code valnull}, tak udalosť vznikla v okne sveta (inak ide o inštanciu
	 * okna, ktoré udalosť vyvolalo).</p -->
	 * 
	 * <p>Na zistenie toho, v ktorom okne sa táto udalosť vyskytla
	 * použite metódu {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()}. (Pozor, nezamieňať s metódou
	 * {@link ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()},
	 * ktorá nesie informáciu o komponente okna a je relevantná len
	 * pri niektorých udalostiach.)</p>
	 * 
	 * <p>Ak sa udalosť vyskytla v hlavnom okne – vo svete, tak
	 * metóda {@link ÚdajeUdalostí#oknoUdalosti()
	 * ÚdajeUdalostí.oknoUdalosti()} vráti {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Od verzie 2.19 je možné zisťovať
	 * akciu pustenia súboru metódou {@link ÚdajeUdalostí#akciaPustenia()
	 * akciaPustenia}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Toto je najjednoduchší príklad spracovania súborov potiahnutých
	 * a pustených nad plátnom.</p>
	 * 
	 * <pre CLASS="example">
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@code currpustenieSúboru}({@link String String} súbor)
		{
			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(súbor);
		}
		</pre>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Tento príklad je o poznanie zložitejší. Umožňuje vykonávať
	 * základné transformácie obrázka (posunutie, otočenie, mierka)
	 * prostredníctvom aktuálneho robota. Tiež spravuje vnútornú pamäť
	 * rámca, čo umožňuje príkladu fungovať kontinuálne/dlhodobo (bez
	 * nevyhnutnosti reštartovania aplikácie) aj po vystriedaní väčšieho
	 * množstva obrázkov.</p>
	 * 
	 * <p>Tento príklad sa spomína aj v opisoch metód {@link 
	 * ÚdajeUdalostí#klávesnica() klávesnica}, {@link ÚdajeUdalostí#myš()
	 * myš} a {@link ÚdajeUdalostí#kolieskoMyši() kolieskoMyši}.
	 * V súvislosti s tým upriamujeme pozornosť na metódu {@code 
	 * príznakyMyši} (v tomto príklade), ktorá ukazuje možný spôsob
	 * spracovania príznakov udalostí myši. Zaujímavá je implementácia
	 * príkazu {@code skratkaPonuky} (v tomto príklade „vypnutá“ –
	 * umiestnená v komentároch), ktorá je spracovaná platformovo závisle
	 * (z dôvodu zvýšenia komfortu používateľov v súvislosti s obvyklými
	 * postupmi na rôznych platformách).</p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} TransformácieObrázka {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} TransformácieObrázka()
			{
				{@code valsuper}({@code srg"Transformácie obrázka"});
				{@link GRobot#skry() skry}();
				{@code kwdif} ({@link Svet Svet}.{@link Svet#prvéSpustenie() prvéSpustenie}()) {@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
				{@link Svet Svet}.{@link Svet#vystreď() vystreď}();
				{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
				{@link GRobot#veľkosťDoma(Double) veľkosťDoma}({@link GRobot#veľkosť() veľkosť}());
			}
			<hr/>
			{@code kwdprivate} {@link String String} názov = {@code valnull};
			{@code kwdprivate} {@link Obrázok Obrázok} obrázok = {@code valnull};

			{@code kwdprivate} {@code typevoid} obnov()
			{
				{@link Svet Svet}.{@link Svet#vymaž() vymaž}();
				obrázok(obrázok);
				{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			}

			{@code kwdprivate} {@code typevoid} otvor({@link String String} súbor)
			{
				{@code kwdif} ({@link Súbor Súbor}.{@link Súbor#jestvuje(String) jestvuje}(súbor))
				{
					{@code kwdif} ({@code valnull} != názov)
					{
						{@link Svet Svet}.{@link Svet#uvoľni(String) uvoľni}(názov);
						názov = {@code valnull};
					}

					{@code kwdif} ({@code valnull} != obrázok)
					{
						{@link Svet Svet}.{@link Svet#uvoľni(Obrázok) uvoľni}(obrázok);
						obrázok = {@code valnull};
					}

					obrázok = {@link Obrázok Obrázok}.{@link Obrázok#čítaj(String) čítaj}(súbor);
					názov = súbor;
					{@link GRobot#domov() domov}();
					obnov();
				}
				{@code kwdelse}
					{@link Svet Svet}.{@link Svet#chyba(String) chyba}({@code srg"Súbor nejestvuje."});
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@code currpustenieSúboru}({@link String String} súbor)
			{
				otvor(súbor);
			}
			<hr/>
			{@code kwdprivate} {@link Bod Bod} myš1 = {@code valnull};
			{@code comm// private boolean skratkaPonuky = false;}
			{@code comm// private boolean alt = false;}
			{@code comm// private boolean shift = false;}
			{@code kwdprivate} {@code typeboolean} ľavé = {@code valfalse};
			{@code comm// private boolean pravé = false;}
			{@code comm// private boolean stredné = false;}

			{@code kwdprivate} {@code typevoid} príznakyMyši()
			{
				{@code comm// skratkaPonuky =}
				{@code comm// 	Kláves.SKRATKA_PONUKY == Kláves.CTRL_MASK ?}
				{@code comm// 	ÚdajeUdalostí.myš().isControlDown() :}
				{@code comm// 	ÚdajeUdalostí.myš().isMetaDown();}
				{@code comm// alt = ÚdajeUdalostí.myš().isAltDown();}
				{@code comm// shift = ÚdajeUdalostí.myš().isShiftDown();}
				ľavé = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ});
				{@code comm// pravé = ÚdajeUdalostí.tlačidloMyši(PRAVÉ);}
				{@code comm// stredné = ÚdajeUdalostí.tlačidloMyši(STREDNÉ);}
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
			{
				myš1 = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
			{
				príznakyMyši();
				{@link Bod Bod} myš2 = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();

				{@code kwdif} ({@code valnull} != myš1)
				{
					{@code kwdif} (ľavé)
					{
						{@link GRobot#skoč(double, double) skoč}(myš2.{@link Poloha#polohaX() polohaX}() &#45; myš1.{@link Poloha#polohaX() polohaX}(),
							myš2.{@link Poloha#polohaY() polohaY}() &#45; myš1.{@link Poloha#polohaY() polohaY}());
					}
					{@code kwdelse} {@code comm// if (pravé)}
					{
						{@code typedouble} uhol1 = {@link GRobot#smerNa(Poloha) smerNa}(myš1);
						{@code typedouble} uhol2 = {@link GRobot#smerNa(Poloha) smerNa}(myš2);
						{@link GRobot#vľavo(double) vľavo}(uhol2 &#45; uhol1);
					}

					obnov();
				}

				myš1 = myš2;
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#rolovanieKolieskomMyši() rolovanieKolieskomMyši}()
			{
				{@code comm// int Δx = ÚdajeUdalostí.rolovanieKolieskomMyšiX();}
				{@code typeint} Δy = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#rolovanieKolieskomMyšiY() rolovanieKolieskomMyšiY}();

				{@code typedouble} zmena = {@link GRobot#veľkosť() veľkosť}() + Δy;
				{@code kwdif} (zmena &gt; {@code num100}) zmena = {@code num100};
				{@code kwdelse} {@code kwdif} (zmena &lt; {@code num1}) zmena = {@code num1};
				{@link GRobot#veľkosť(double) veľkosť}(zmena);
				obnov();
			}
			<hr/>
			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@link Svet#skry() skry}();
				{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TransformácieObrázka.cfg"});
				{@code kwdnew} TransformácieObrázka();
				{@link Svet Svet}.{@link Svet#zobraz() zobraz}();
			}
		}
		</pre>
	 * 
	 * @param súbor úplná cesta a meno súboru
	 * 
	 * @see GRobot#pustenieSúboru(String)
	 */
	public void pustenieSúboru(String súbor) {}

	/** <p><a class="alias"></a> Alias pre {@link #pustenieSúboru(String) pustenieSúboru}.</p> */
	public void pustenieSuboru(String súbor) {}


	/**
	 * <p>Spustená po dokončení ťahania súboru alebo súborov do externej
	 * aplikácie. Táto udalosť by mala vzniknúť po pustení ťahaných súborov
	 * z tejto aplikácie (aplikácie vytvorenej s týmto programovacím rámcom)
	 * do inej/externej aplikácie. Ťahanie by malo byť spustené metódou sveta:
	 * {@link Svet#ťahajSúbory(int, String...) ťahajSúbory}. Do tejto reakcie
	 * prídu údaje súvisiace s týmto procesom. Zdroj bude pravdepodobne
	 * {@code valnull}, pretože metóda sveta ho neposiela. Je možné, že aj
	 * údaje budú {@code valnull}, preto je vhodné túto udalosť spárovať
	 * s akciou inak.</p>
	 * 
	 * @param zdroj komponent, ktorý bol zdrojom údajov
	 * @param údaje údaje, ktoré boli prenesené, prípadne {@code valnull}, ak
	 *     je akcia {@link javax.swing.TransferHandler#NONE NONE}.
	 * @param akcia akcia, ktorá bola (s údajmi) vykonaná; pozri aj opis metódy
	 *     {@link Svet#ťahajSúbory(int, String...) ťahajSúbory}
	 * 
	 * @see GRobot#ťahanieUkončené(JComponent, Transferable, int)
	 */
	public void ťahanieUkončené(JComponent zdroj, Transferable údaje, int akcia) {}

	/** <p><a class="alias"></a> Alias pre {@link #ťahanieUkončené(JComponent, Transferable, int) ťahanieUkončené}.</p> */
	public void tahanieUkoncene(JComponent zdroj, Transferable údaje, int akcia) {}


	/**
	 * <p>Spustená pri overovaní zmeny farby, ktorou má byť vypísané určité
	 * (zadané) aktívne slovo vnútornej konzoly. S pomocou tejto reakcie je
	 * možné paušálne meniť farbu všetkých aktívnych slov, odlišovať farby
	 * takých slov, ktoré už boli používateľom zvolené od takých, ktoré
	 * doteraz používateľ ani raz nezvolil a podobne. Ak si farbu aktívneho
	 * slova neželáme zmeniť, treba vrátiť hodnotu {@code valnull}.</p>
	 * 
	 * @param slovo identifikátor toho aktívneho slova vnútornej konzoly,
	 *     ktorého farba má byť ovplyvnená
	 * @return nová farba aktívneho slova alebo {@code valnull}
	 * 
	 * @see GRobot#farbaAktívnehoSlova(String)
	 * @see Plátno#vypíšAktívneSlovo(String, Object[])
	 */
	public Color farbaAktívnehoSlova(String slovo) { return null; }

	/** <p><a class="alias"></a> Alias pre {@link #farbaAktívnehoSlova(String) farbaAktívnehoSlova}.</p> */
	public Color farbaAktivnehoSlova(String slovo) { return null; }


	/**
	 * <p>Spustená po aktivácii hypertextového odkazu umiestneného v rámci
	 * niektorej z inštancií triedy {@link PoznámkovýBlok PoznámkovýBlok}.
	 * Užitočné doplňujúce údaje súvisiace s touto udalosťou poskytujú metódy
	 * {@link ÚdajeUdalostí#poslednýOdkaz() poslednýOdkaz} a {@link 
	 * ÚdajeUdalostí#poslednýPoznámkovýBlok() poslednýPoznámkovýBlok}.
	 * Metóda {@link Svet#otvorWebovýOdkaz(String) otvorWebovýOdkaz}
	 * umožňuje otvorenie poskytnutého odkazu v predvolenom webovom
	 * prehliadači.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Na to, aby klikanie na webové
	 * odkazy v poznámkovom bloku fungovalo, nesmie byť poznámkový blok
	 * v režime úprav – pozri metódy {@link PoznámkovýBlok#upraviteľný()
	 * upraviteľný}, {@link PoznámkovýBlok#upravuj() upravuj} a {@link 
	 * PoznámkovýBlok#neupravuj() neupravuj}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Tento príklad ukazuje minimálne požiadavky, ktoré musia byť
	 * splnené na to, aby fungovalo klikanie na webové odkazy v poznámkovom
	 * bloku. Nepotrebnými bonusmi v tomto príklade sú len zmena veľkosti
	 * plátna (volaním nadradeného konštruktora robota – {@code valsuper}
	 * v prvom riadku konštruktora odvodenej triedy), nastavenie
	 * automatického rozťahovania (šírky a výšky) poznámkového bloku,
	 * prípadne aj skrytie hlavného robota (podľa toho, za akú veľkú
	 * prekážku považujeme jeho vizuálna prítomnosť na plátne).
	 * Ostatné časti príkladu sú nevyhnutné.</p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} OtvoriťWebovýOdkaz {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} OtvoriťWebovýOdkaz()
			{
				{@code valsuper}({@link Svet Svet}.{@link Svet#šírkaZariadenia() šírkaZariadenia}(), {@link Svet Svet}.{@link Svet#výškaZariadenia() výškaZariadenia}()); {@code comm// Nie je nevyhnutné.}
				{@link PoznámkovýBlok PoznámkovýBlok} blok = {@code kwdnew} {@link PoznámkovýBlok#PoznámkovýBlok() PoznámkovýBlok}();
				blok.{@link PoznámkovýBlok#roztiahniNaŠírku() roztiahniNaŠírku}(); {@code comm// Nie je nevyhnutné.}
				blok.{@link PoznámkovýBlok#roztiahniNaVýšku() roztiahniNaVýšku}(); {@code comm// Nie je nevyhnutné.}
				blok.{@link PoznámkovýBlok#html(String) html}({@code srg"<a href=\"https://pdf.truni.sk/\">Klikni na odkaz.</a>"});
				blok.{@link PoznámkovýBlok#neupravuj() neupravuj}();
				{@link GRobot#skry() skry}(); {@code comm// Tiež je možné vynechať.}
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktiváciaOdkazu() aktiváciaOdkazu}()
			{
				{@link Svet Svet}.{@link Svet#otvorWebovýOdkaz(String) otvorWebovýOdkaz}(
					{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#poslednýOdkaz() poslednýOdkaz}());
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@code kwdnew} OtvoriťWebovýOdkaz();
			}
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <p><image>vysledok-aktivacia-odkazu.png<alt/>Výsledok príkladu
	 * použitia aktivácie odkazu.</image>Výsledok príkladu použitia
	 * aktivácie odkazu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V príklade je použité prekrytie
	 * reakcie {@link GRobot#aktiváciaOdkazu() aktiváciaOdkazu} v triede
	 * odvodenej od triedy {@link GRobot GRobot}. Princíp je však rovnaký
	 * aj pri použití reakcie z triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @see GRobot#aktiváciaOdkazu()
	 */
	public void aktiváciaOdkazu() {}

	/** <p><a class="alias"></a> Alias pre {@link #aktiváciaOdkazu() aktiváciaOdkazu}.</p> */
	public void aktivaciaOdkazu() {}


	/**
	 * <p>Výsledok (návratová hodnota) tejto reakcie môže byť overená pred
	 * zápisom {@linkplain Svet#použiKonfiguráciu() automatickej
	 * konfigurácie}. Ak iné súčasti programovacieho rámca oznámia, že
	 * konfiguráciu je potrebné uložiť, tak táto reakcia vôbec nemusí byť
	 * spustená. V prípade, že je spustená, tak ak je jej návratová hodnota
	 * rovná {@code valtrue}, tak bude spustená reakcia
	 * {@link #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}.
	 * (V opačnom prípade by bola uvedená reakcia spustená len
	 * vtedy, ak by bola zmenená niektorá z predvolených položiek
	 * {@linkplain Svet#použiKonfiguráciu() automatickej
	 * konfigurácie} – t. j. napríklad používateľ by presunul hlavné okno
	 * aplikácie, zmenil jeho veľkosť alebo by bol registrovaný niektorý
	 * robot na uloženie konfigurácie.)
	 * Príklad použitia je uvedený v hlavnej sekcii opisu triedy {@link 
	 * ObsluhaUdalostí ObsluhaUdalostí}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Pri prekrývaní tejto reakcie
	 * majte na pamäti, že nie je garantované jej automatické vykonanie pri
	 * ukončovaní aplikácie (ak vyššia vrstva zistí, že konfigurácia bola
	 * zmenená, tak ďalej nevyšetruje, či ju treba, alebo netreba ukladať
	 * a automaticky pristúpi k procesu uloženia konfigurácie). Vložte do
	 * tela tejto reakcie len taký kód, ktorý rýchlo overí, či nastali nejaké
	 * zmeny v konfigurácii, ktoré treba uložiť a vráti túto informáciu
	 * vyššej vrstve.</p>
	 * 
	 * @return signál dovoľujúci programovaciemu rámcu overiť, či bola zmenená
	 *     aspoň jedna položka voliteľnej konfigurácie a či je potrebné
	 *     zapísať konfiguráciu na disk; ak majú byť zmenené prvky
	 *     konfigurácie korektne zapísané na disk, tak je nevyhnutné zariadiť,
	 *     aby návratová hodnota prekrývajúcej verzie tejto metódy bola
	 *     rovná {@code valtrue}
	 * 
	 * @see #čítajKonfiguráciu(Súbor)
	 * @see #zapíšKonfiguráciu(Súbor)
	 * @see GRobot#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 */
	public boolean konfiguráciaZmenená() { return false; }

	/** <p><a class="alias"></a> Alias pre {@link #konfiguráciaZmenená() konfiguráciaZmenená}.</p> */
	public boolean konfiguraciaZmenena() { return false; }

	/**
	 * <p>Reakcia je vykonaná počas čítania {@linkplain 
	 * Svet#použiKonfiguráciu() automatickej konfigurácie}. Je určená na
	 * prečítanie hodnôt vlastnej konfigurácie. Príklad použitia je
	 * uvedený v hlavnej sekcii opisu triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} reprezentujúca
	 *     otvorený konfiguračný súbor pripravený na čítanie údajov
	 * 
	 * @see #konfiguráciaZmenená()
	 * @see #zapíšKonfiguráciu(Súbor)
	 * @see GRobot#čítajKonfiguráciu(Súbor)
	 * @see Svet#použiKonfiguráciu()
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void čítajKonfiguráciu(Súbor súbor) throws IOException {}

	/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciu(Súbor) čítajKonfiguráciu}.</p> */
	public void citajKonfiguraciu(Súbor subor) throws IOException {}

	/**
	 * <p>Reakcia je vykonaná počas zápisu {@linkplain 
	 * Svet#použiKonfiguráciu() automatickej konfigurácie}. Je určená na
	 * zápis hodnôt vlastnej konfigurácie. Príklad použitia je uvedený
	 * v hlavnej sekcii opisu triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} reprezentujúca
	 *     otvorený konfiguračný súbor pripravený na zápis údajov
	 * 
	 * @see #konfiguráciaZmenená()
	 * @see #čítajKonfiguráciu(Súbor)
	 * @see GRobot#zapíšKonfiguráciu(Súbor)
	 * @see Svet#použiKonfiguráciu()
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void zapíšKonfiguráciu(Súbor súbor) throws IOException {}

	/** <p><a class="alias"></a> Alias pre {@link #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}.</p> */
	public void zapisKonfiguraciu(Súbor subor) throws IOException {}

	/**
	 * <p>Táto reakcia súvisí s časovo náročným spracovaním údajov počas
	 * práce s pevným diskom. Napríklad s {@link Obrázok#čítaj(String)
	 * čítaním} a {@link Obrázok#ulož(String, boolean) zápisom} sekvencie
	 * PNG súborov (pozri triedu {@link Obrázok Obrázok} a jej metódy
	 * {@link Obrázok#čítaj(String) čítaj(súbor)}
	 * a {@link Obrázok#ulož(String, boolean) ulož(súbor, prepísať)})
	 * alebo {@linkplain Súbor#kopíruj(String, String) kopírovaním
	 * súborov} a podobne. Typ udalosti, ktorá spôsobila spustenie tejto
	 * reakcie je určený prvým parametrom – {@code kódSpracovania}.
	 * Ostatné parametre sú naplnené podľa ich relevantnosti.</p>
	 * 
	 * @param kódSpracovania obsahuje kód činnosti, ktorá spôsobila
	 *     vznik tejto udalosti; aktuálne platné kódy sú:
	 *     {@link Konštanty#ČÍTANIE_PNG_SEKVENCIE ČÍTANIE_PNG_SEKVENCIE},
	 *     {@link Konštanty#CHYBA_ČÍTANIA_PNG_SEKVENCIE
	 *     CHYBA_ČÍTANIA_PNG_SEKVENCIE}, {@link Konštanty#ZÁPIS_PNG_SEKVENCIE
	 *     ZÁPIS_PNG_SEKVENCIE}, {@link Konštanty#ČÍTANIE_GIF_ANIMÁCIE
	 *     ČÍTANIE_GIF_ANIMÁCIE}, {@link Konštanty#ZÁPIS_GIF_ANIMÁCIE
	 *     ZÁPIS_GIF_ANIMÁCIE}, {@link Konštanty#KOPÍROVANIE_SÚBOROV
	 *     KOPÍROVANIE_SÚBOROV}, {@link Konštanty#PRIPÁJANIE_SÚBOROV
	 *     PRIPÁJANIE_SÚBOROV}, {@link Konštanty#POROVNANIE_SÚBOROV
	 *     POROVNANIE_SÚBOROV}, {@link Konštanty#ODOVZDANIE_ÚDAJOV
	 *     ODOVZDANIE_ÚDAJOV} a {@link Konštanty#PREVZATIE_ÚDAJOV
	 *     PREVZATIE_ÚDAJOV}
	 * @param zdroj objekt súvisiaci so zdrojom alebo {@code valnull};
	 *     napríklad v prípade kopírovania súborov je to {@linkplain 
	 *     String reťazec} s názvom zdrojového súboru, v prípade zápisu
	 *     PNG sekvencie je to {@linkplain BufferedImage obrázkový
	 *     objekt} so zdrojovou snímkou animácie a podobne
	 * @param cieľ objekt súvisiaci s cieľom alebo {@code valnull};
	 *     napríklad pri kopírovaní súborov je to {@linkplain String
	 *     reťazec} s názvom cieľového súboru; v prípade čítania PNG
	 *     sekvencie je to {@linkplain BufferedImage obrázkový objekt}
	 *     s cieľovou snímkou animácie (čiže tou, do ktorej sú ukladané
	 *     prečítané údaje) a podobne
	 * @param stav hodnota vyjadrujúca aktuálne spracovaný objem údajov
	 *     alebo {@code num-1}, ak táto hodnota nie je známa alebo je
	 *     irelevantná; môže to byť počet spracovaných bajtov, poradové
	 *     číslo snímky a podobne
	 * @param celkovo hodnota vyjadrujúca celkový objem údajov, ktorý má byť
	 *     spracovaný alebo {@code num-1}, ak táto hodnota nie je známa
	 *     alebo je irelevantná; môže to byť veľkosť súboru, celkový počet
	 *     snímok a podobne
	 */
	public void sekvencia(int kódSpracovania, Object zdroj,
		Object cieľ, long stav, long celkovo) {}

	/**
	 * <p>Táto reakcia je spustená pri zastavení prehrávania animácie
	 * obrázka. Reakcia prijíma inštanciu obrázka, ktorého prehrávanie bolo
	 * zastavené, pričom zastavenie prehrávania môže byť iniciované
	 * automaticky (pozri aj metódu obrázka {@link Obrázok#zastav() zastav})
	 * alebo na podnet používateľa (programátora).</p>
	 * 
	 * @param obrázok inštancia obrázka, ktorého prehrávanie bolo zastavené
	 */
	public void zastavenieAnimácie(Obrázok obrázok) {}

	/** <p><a class="alias"></a> Alias pre {@link #zastavenieAnimácie(Obrázok) zastavenieAnimácie}.</p> */
	public void zastavenieAnimacie(Obrázok obrázok) {}

	/**
	 * <p>Táto reakcia je spustená po ukončení externého procesu, ktorý
	 * spustila zadaná inštancia triedy {@link Svet.PríkazovýRiadok
	 * PríkazovýRiadok}. Číselný kód, ktorý prijíma táto reakcia v druhom
	 * parametri je návratový kód, ktorý vrátil proces pri svojom
	 * ukončení.</p>
	 * 
	 * @param príkazovýRiadok inštancia príkazového riadka, ktorá proces
	 *     spustila (jej zdedenými metódami
	 *     <code>ExecuteShellCommand.getRunCommand</code>,
	 *     <code>ExecuteShellCommand.getRunArguments</code>,
	 *     <code>ExecuteShellCommand.getCommandArray</code>
	 *     a <code>ExecuteShellCommand.getRunEnvironment</code>sa dajú
	 *     získať doplnkové informácie o procese v čase jeho spúšťania)
	 * @param návratovýKód kód, ktorý vrátil externý proces pri ukončení
	 */
	public void ukončenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok,
		int návratovýKód) {}

	/** <p><a class="alias"></a> Alias pre {@link #ukončenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok, int) ukončenieProcesu}.</p> */
	public void ukoncenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok, int návratovýKód) {}

	/**
	 * <p>Táto reakcia je vykonávaná v {@linkplain 
	 * Svet#režimLadenia(boolean) režime ladenia}. Návratová hodnota
	 * reakcie je odpoveďou na otázku položenú {@linkplain 
	 * Svet#režimLadenia(boolean) režimom ladenia} nadradenému procesu
	 * prostredníctvom parametra {@code správa}. Hodnota parametra
	 * {@code správa} môže byť jedna z nasledujúcich:</p>
	 * 
	 * <p> </p>
	 * 
	 * <table class="commands">
	 * <tr><td>{@link Konštanty#VYPÍSAŤ_PREMENNÉ VYPÍSAŤ_PREMENNÉ}</td><td
	 * >–</td><td>Má režim ladenia vypísať obsah všetkých premenných
	 * (pred začatím vykonávania skriptu)?</td></tr>
	 * <tr><td>{@link Konštanty#VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}</td><td
	 * >–</td><td>Má režim ladenia vypísať (pred začatím vykonávania
	 * skriptu) definície všetkých menoviek, ktoré našiel počas analýzy
	 * skriptu?</td></tr>
	 * <tr><td>{@link Konštanty#VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK}</td><td>–</td><td>Má
	 * režim ladenia vypísať aktuálny riadok skriptu?</td></tr>
	 * <tr><td>{@link Konštanty#ČAKAŤ ČAKAŤ}</td><td>–</td><td>Má režim ladenia
	 * čakať pred vykonaním riadka skriptu? Ak je odpoveď „áno,“
	 * tak je táto správa posielaná opakovane (každých 350 ms).</td></tr>
	 * <tr><td>{@link Konštanty#PRERUŠIŤ PRERUŠIŤ}</td><td>–</td><td>Má režim
	 * prerušiť vykonávanie skriptu?</td></tr>
	 * <tr><td>{@link Konštanty#ZABRÁNIŤ_VYKONANIU ZABRÁNIŤ_VYKONANIU}</td><td
	 * >–</td><td>Má režim v poslednej chvíli zabrániť vykonaniu
	 * príkazu s konkrétnymi hodnotami argumentov?</td></tr>
	 * <tr><td>{@link Konštanty#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}</td><td>–</td><td>Má
	 * režim vypísať ozvenu potvrdeného príkazu {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívneho režimu}?</td></tr>
	 * <tr><td>{@link Konštanty#VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}</td><td>–</td><td>Má
	 * režim vykonať potvrdený príkaz {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívneho režimu}?</td></tr>
	 * <tr><td>{@link Konštanty#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}</td><td
	 * >–</td><td>{@linkplain Svet#spustiSkript(String[]) Vykonávanie
	 * skriptu v samostatnom vlákne} oznamuje, že jeho činnosť bola
	 * ukončená (bez chyby).</td></tr>
	 * <tr><td>{@link Konštanty#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}</td><td
	 * >–</td><td>{@linkplain Svet#spustiSkript(String[]) Vykonávanie
	 * skriptu v samostatnom vlákne} oznamuje, že jeho činnosť bola
	 * ukončená chybou. V parametri {@code riadok} je číslo riadka, na
	 * ktorom vznikla chyba a reťazec parametra {@code príkaz} obsahuje
	 * v tomto prípade jednoduchý text chybového hlásenia.</td></tr>
	 * <tr><td>{@link Konštanty#VYPÍSAŤ_SKRIPT VYPÍSAŤ_SKRIPT}</td><td>–</td><td>Má
	 * režim ladenia vypísať úplné znenie aktuálne vykonávaného
	 * skriptu?</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#ČÍSELNÁ_PREMENNÁ ČÍSELNÁ_PREMENNÁ}<br />
	 * {@link Konštanty#FAREBNÁ_PREMENNÁ FAREBNÁ_PREMENNÁ}<br />
	 * {@link Konštanty#POLOHOVÁ_PREMENNÁ POLOHOVÁ_PREMENNÁ}<br />
	 * {@link Konštanty#REŤAZCOVÁ_PREMENNÁ REŤAZCOVÁ_PREMENNÁ}</td><td
	 * >–</td><td>Má režim vypísať obsah premennej prislúchajúceho
	 * údajového typu? Táto otázka vznikne vždy, keď sa zmení hodnota
	 * premennej.</td></tr>
	 * </table>
	 * 
	 * <p> </p>
	 * 
	 * <p>Ak je {@linkplain Svet#režimLadenia(boolean) režim ladenia}
	 * zapnutý počas {@linkplain Svet#interaktívnyRežim(boolean)
	 * interaktívneho režimu}, tak pred vykonaním každého potvrdeného
	 * riadka vznikne otázka typu {@link Konštanty#VYKONAŤ_PRÍKAZ
	 * VYKONAŤ_PRÍKAZ}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} RežimLadeniaInteraktívne {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} RežimLadeniaInteraktívne()
			{
				{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
				{@link Svet Svet}.{@link Svet#režimLadenia(boolean) režimLadenia}({@code valtrue});
				{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code valthis});

				{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
				{
					{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov}()
					{
						{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(červená);
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Chyba"});
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
					}

					{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@code currladenie}(
						{@code typeint} riadok, {@link String String} príkaz, {@code typeint} správa)
					{
						{@code kwdswitch} (správa)
						{
						{@code kwdcase} {@link Konštanty#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}:
						{@code kwdcase} {@link Konštanty#VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}:
							{@code kwdreturn} {@code valtrue};
						}

						{@code kwdif} (správa &lt; {@code num0}) {@code kwdreturn} {@code valtrue};

						{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(purpurová);
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" × "}, príkaz);
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
						{@code kwdreturn} {@code valfalse};
					}
				};

				{@link Svet Svet}.{@link Svet#registrujRobot() registrujRobot}();
				{@link Svet Svet}.{@link Svet#čítajKonfiguráciuSveta() čítajKonfiguráciuSveta}();
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}();
				{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
				{@code kwdnew} RežimLadeniaInteraktívne();
				{@link Svet Svet}.{@link Svet#kresli() kresli}();
				{@link Svet Svet}.{@link Svet#aktivujVstupnýRiadok() aktivujVstupnýRiadok}();
			}
		}
		</pre>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Komplexnejší príklad ladenia je
	 * v opise triedy {@link Skript Skript} v sekcii Príklad ladenia
	 * skriptov.</p>
	 * 
	 * @param riadok poradové číslo riadka skriptu alebo hodnota −1
	 *     (ktorá signalizuje, že číslo riadka nie je známe)
	 * @param príkaz príkaz skriptu, prípadne názov premennej
	 * @param správa typ správy, na ktorú sa režim pokúša získať odpoveď
	 *     od nadradeného procesu (pozri vyššie)
	 * @return návratová hodnota je odpoveďou na správu poslanú parametrom
	 *     <code>správa</code>; {@code valtrue} – kladná odpoveď na
	 *     niektorú z vyššie položených otázok; {@code valfalse} – záporná
	 *     odpoveď na niektorú z vyššie položených otázok
	 * 
	 * @see Svet#interaktívnyRežim(boolean)
	 * @see Svet#interaktívnaInštancia(String)
	 * @see Svet#režimLadenia(boolean)
	 * @see Svet#vykonajPríkaz(String)
	 * @see Svet#vykonajSkript(String[])
	 */
	public boolean ladenie(int riadok, String príkaz, int správa)
	{
		if (VYKONAŤ_PRÍKAZ == správa) return true;
		return false;
	}
}
