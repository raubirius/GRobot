
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
 // along with this program (look for the file named license.txShebat). If not,
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


import knižnica.podpora.SimpleTextShape;
import static knižnica.Konštanty.KRESLI_ROTOVANÉ;
import static knižnica.Konštanty.KRESLI_NA_STRED;


// --------------------------- //
//  *** Trieda SVGPodpora ***  //
// --------------------------- //

/**
 * <p>Táto trieda slúži na komunikáciu programovacieho rámca s okolím
 * prostredníctvom základov štandardu SVG. Cieľom pri implementácii bolo
 * najmä poskytnutie možnosti importu a exportu tvarov programovacieho rámca
 * GRobot vo formáte SVG 1.1, resp. SVG 2.0. Aj keď v čase implementácie
 * prvej verzie tejto triedy nebol návrh SVG 2.0 úplný, programovací rámec
 * s jeho použitím od začiatku počíta a návrh SVG 2.0 bol implementovaný do
 * procesu importu tvarov.</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Pri exporte nie sú využité
 * vlastnosti SVG 2.0 (najmä takzvaný „bearing“ – aktuálne pootočenie,
 * ktorý má v budúcnosti slúžiť na implementáciu korytnačej grafiky
 * v rámci štandardu SVG), pretože súčasný softvér tento štandard nie
 * je schopný spracovať, takže takto exportované údaje by neboli
 * použiteľné.</p>
 * 
 * <p>Spracovanie úplnej implementácie štandardu SVG by bolo príliš
 * náročné. Rámec sa zameriava na úplné základy – import a export
 * reprezentácie tvarov (úsečka, obdĺžnik, kružnica, elipsa, lomená
 * čiara, polygón a cesta), import a export základnej reprezentácie
 * farieb výplní a čiar a tiež hrúbok čiar a použitie geometrických
 * transformácií tvarov. Rámec nepodporuje žiadne zložitejšie výplne,
 * štýly/druhy čiar a nadpojenia čiar, posunutie počiatku transformácií,
 * ani štýlovanie prostredníctvom kaskádových štýlov (CSS) a podobne.
 * Trieda tieto vlastnosti ani nezachováva.
 * <small>(Jedinou drobnou výnimkou je automatický prevod vybraných CSS
 * definícií uvedených v <em>atribúte</em> {@code style="…"} na hodnoty
 * konkrétnych XML atribútov. Príklad: {@code <rect …
 * style="fill:blue" />} bude spracované tak, ako keby bolo uvedené
 * v tvare {@code <rect … fill="blue" />}. Avšak okrem vybraných CSS
 * definícií ako {@code fill}, {@code stroke}, {@code fill-opacity},
 * {@code stroke-opacity}, {@code stroke-width} a {@code transform} sú
 * všetky ostatné CSS definície ignorované. Tiež nie je analyzovaný
 * obsah <em>značky</em> {@code <style> … </style>}, z čoho vyplýva, že
 * aj obsah atribútu {@code class} je ignorovaný.)</small>
 * Z toho dôvodu <b>nie je</b> vhodné použiť prorgamovací rámec na úpravu
 * SVG súborov, ktoré boli vytvorené inými softvérmi (napríklad <a
 * href="https://inkscape.org/" target="_blank">Inkscape</a>). Stratilo
 * by sa príliš veľa informácií. Ukážky vhodného použitia tejto triedy
 * ukazujú nasledujúce príklady…</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje spôsob vykreslenia importovaných SVG údajov
 * v rámci podporovaných možností programovacieho rámca. Príklad potrebuje
 * na svoje fungovanie ukážkový SVG súbor {@code srg"Panak.svg"}, ktorý je
 * k dispozícii na prevzatie pod zdrojovým kódom príkladu. Nakreslený
 * obrázok je možné presúvať klikaním a ťahaním myšou.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.{@link Farba Farba};
	{@code kwdimport} knižnica.{@link GRobot GRobot};
	{@code kwdimport} knižnica.{@link Svet Svet};
	{@code kwdimport} knižnica.{@link ÚdajeUdalostí ÚdajeUdalostí};

	{@code kwdpublic} {@code typeclass} SVGZobraz {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Konštruktor.}
		{@code kwdprivate} SVGZobraz()
		{
			{@code comm// Nastavenie rozmerov plátien volaním nadradeného konštruktora:}
			{@code valsuper}({@code num1200}, {@code num650});

			{@code comm// Vypnutie predvoleného spôsobu automatického prekresľovania:}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Zabezpečenie toho, aby robot nebol skrytý po prvom konzolovom výpise:}
			{@link Svet Svet}.{@link Svet#vypíš(Object...) vypíš}({@code valthis});

			{@code comm// Prečítanie tvarov z SVG súboru:}
			{@code typeint} prečítané = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#čítaj(String) čítaj}({@code srg"Panak.svg"});

			{@code comm// Overenie toho, či sa čítanie podarilo:}
			{@code kwdif} ({@code num-1} == prečítané)
			{
				{@code comm// V prípade neúspechu je informácia vypísaná červenou farbou:}
				{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Čítanie súboru sa nepodarilo."});
			}
			{@code kwdelse}
			{
				{@code comm// V prípade úspechu je vypísaný počet prečítaných tvarov:}
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Počet prečítaných tvarov: "}, prečítané);
				vykresli();
			}

			{@code comm// Spustenie časovača zapne kontrolu potreby prekreslenia sveta}
			{@code comm// v pravidelných časových inervaloch (pozri reakciu tik nižšie;}
			{@code comm// samozrejme, že ak sa zistí, že je prekreslenie sveta potrebné,}
			{@code comm// tak bude zároveň vykonané):}
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}

		{@code comm// V obsluhe udalosti časovača (tik) je zabezpečované prekresľovanie}
		{@code comm// sveta v pravidelných časových intervaloch (v prípade potreby).}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
		}

		{@code comm// Po kliknutí (alebo ťahaní myšou – pozri nižšie) bude SVG obrázok}
		{@code comm// prekreslený na novej pozícii.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
			{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ}))
				{@link GRobot#skočNaMyš() skočNaMyš}(); {@code kwdelse} {@link GRobot#otočNaMyš() otočNaMyš}();
			vykresli();
		}

		{@code comm// Reakcia na ťahanie myšou je rovnaká ako na klik.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
		{
			{@link GRobot#klik() klik}();
		}

		{@code comm// Vlastný tvar robota bude kružnica so „zárezom.“}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#kresliTvar() kresliTvar}()
		{
			{@link GRobot#kružnica() kružnica}();
			{@link GRobot#dopredu() dopredu}();
		}

		{@code comm// Táto metóda slúži na nakreslenie prečítaného jednoduchého SVG obrázka.}
		{@code comm// (Jednoduchého v zmysle využitia základných možností formátu SVG,}
		{@code comm// s ktorými dovoľuje pracovať trieda SVGPodpora.)}
		{@code kwdpublic} {@code typevoid} vykresli()
		{
			{@code comm// Deklarácia a definícia premenných. Farbu bude treba na overenie}
			{@code comm// toho, či má kreslený tvar definovanú farbu výplne alebo čiary.}
			{@code comm// Počet uložený v premennej by mal mierne zvýšiť efektívnosť}
			{@code comm// vykonávania cyklu.}
			{@link Farba Farba} farba;
			{@code typeint} počet = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#počet() počet}();

			{@code comm// Cyklus – nakreslenie čiar a/alebo výplní všetkých tvarov.}
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; počet; ++i)
			{
				{@code comm// Kreslenie výplne:}
				farba = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#farbaVýplne(int) farbaVýplne}(i);
				{@code kwdif} ({@code valnull} != farba)
				{
					{@link GRobot#farba(Color) farba}(farba);
					{@link GRobot#vyplňTvar(Shape, boolean) vyplňTvar}({@link SVGPodpora svgPodpora}.{@link SVGPodpora#dajVýsledný(int) dajVýsledný}(i), {@code valtrue});
				}

				{@code comm// Kreslenie čiary:}
				farba = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#farbaČiary(int) farbaČiary}(i);
				{@code kwdif} ({@code valnull} != farba)
				{
					{@code typedouble} hrúbkaČiary = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#hrúbkaČiary(int) hrúbkaČiary}(i);
					{@code kwdif} ({@link Double Double}.{@link Double#isFinite(double) isFinite}(hrúbkaČiary))
						{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}(hrúbkaČiary);
					{@code kwdelse}
						{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num1});
					{@link GRobot#farba(Color) farba}(farba);
					{@link GRobot#kresliTvar(Shape, boolean) kresliTvar}({@link SVGPodpora svgPodpora}.{@link SVGPodpora#dajVýsledný(int) dajVýsledný}(i), {@code valtrue});
				}
			}

			{@code comm// Po dokončení každého kreslenia (prvé je vykonané pri spustení}
			{@code comm// aplikácie a ďalšie pri kliknutí alebo ťahaní myšou) je opätovne}
			{@code comm// nastavená hrúbka a farba čiary robota na 0.5 boda a červenú. To}
			{@code comm// ovplyvní nakreslenie vlastného tvaru robota – kružnice so „zárezom.“}
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num.5});
			{@link GRobot#farba(Color) farba}({@link Farebnosť#červená červená});
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"SVGZobraz.cfg"});
			{@code kwdnew} SVGZobraz();
		}
	}
	</pre>
 * 
 * <p> <br /><a target="_blank" href="resources/Panak.svg">Panak.svg</a> –
 * SVG obrázok na prevzatie.<br /> </p>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>svg-panak.png<alt/>Nakreslený panák prečítaný z SVG
 * súboru.</image>Vzhľad časti plochy sveta po spustení príkladu a miernom
 * posunutí panáka myšou smerom nadol<br /><small>(plátno ukážky je orezané
 * a zmenšené; veľkosť okna je po prvom spustení menšia, než veľkosť
 * plochy)</small>.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Nasledujúca séria príkazov ukazuje import tvaru z SVG súboru a jeho
 * použitie na kreslenie vlastného vyplneného tvaru robota. Príkazy
 * vyžadujú na svoje fungovanie SVG súbor {@code srg"SVGTvaryRobota.svg"},
 * ktorý je k dispozícii na prevzatie nižšie. (Pod ním nájdete rozšírenú
 * verziu tohto príkladu.)</p>
 * 
 * <pre CLASS="example">
	{@code kwdtry}
	{
		{@link SVGPodpora svgPodpora}.{@link SVGPodpora#čítaj(String) čítaj}({@code srg"SVGTvaryRobota.svg"});

		{@link Shape Shape} tvar = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#dajVýsledný(int) dajVýsledný}({@code num0});

		{@code kwdfinal} {@link Oblasť Oblasť} tvarVOblasti = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(
			{@link SVGPodpora SVGPodpora}.{@link SVGPodpora#presuňDoStredu(Shape) presuňDoStredu}(tvar));

		{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(({@link GRobot GRobot} r) -> r.{@link GRobot#vyplňOblasť(Area) vyplňOblasť}(tvarVOblasti));
	}
	{@code kwdcatch} ({@link Exception Exception} e)
	{
		e.{@link Exception#printStackTrace() printStackTrace}();
	}
	</pre>
 * 
 * <p> <br /><a target="_blank" href="resources/SVGTvaryRobota.svg"
 * >SVGTvaryRobota.svg</a> – SVG súbor (s tvarmi robota) na
 * prevzatie<br /> </p>
 * 
 * <p>Nasledujúce rozšírenie príkladu pracuje so zapnutým {@linkplain 
 * GRobot#interaktívnyRežim(boolean) interaktívnym režimom robota}
 * a {@linkplain Svet#interaktívnyRežim(boolean) sveta} a používa tiež
 * štandardú konfiguráciu {@linkplain Svet#použiKonfiguráciu sveta} aj
 * {@linkplain Svet#registrujRobot(GRobot) robota}. Definuje aj dve tzv.
 * zákaznícke vlastnosti. To sú také vlastnosti, ktoré sú definované nad
 * rámec štandardnej konfigurácie (podrobnosti sú v komentároch). Zmenu
 * tvaru docieli používateľ po spustení aplikácie zadaním a potvrdením
 * príkazu „nastav tvar <em>«číslo»</em>“ vo vstupnom riadku, pričom pri
 * použití súboru <a target="_blank" href="resources/SVGTvaryRobota.svg"
 * >SVGTvaryRobota.svg</a> sú použiteľné číselné hodnoty 1 – 3 na zmenu
 * tvarov prečítaných zo súboru a nula, ktorá znamená predvolený tvar.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;
	{@code kwdimport} knižnica.{@link SVGPodpora SVGPodpora}.{@link Transformácia Transformácia};

	{@code kwdimport} java.awt.{@link Shape Shape};

	{@code kwdpublic} {@code typeclass} SVGTvarRobota {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Do tohto atribútu bude ukladaný tvar prevedený z SVG údajov na}
		{@code comm// oblasť (tento proces nazývame zjednodušene v komentároch nižšie aj}
		{@code comm// generovaním oblasti):}
		{@code kwdprivate} {@link Oblasť Oblasť} tvarVOblasti = {@code valnull};

		{@code comm// Tento atribút slúži na detekciu zmeny veľkosti robota, aby mohol byť}
		{@code comm// tvar uložený v oblasti (definovanej vyššie) pregenerovaný (to znamená,}
		{@code comm// že pri každej zmene veľkosti robota sa oblasť znovu vygeneruje – tento}
		{@code comm// prístup nepočíta s príliš častými zmenami veľkosti robota):}
		{@code kwdprivate} {@code typedouble} poslednáVeľkosť = {@code num10.0};

		{@code comm// V tomto atribúte je uložené poradové číslo naposledy prevedeného tvaru}
		{@code comm// z SVG údajov (ide o jednu z dvoch tzv. zákazníckych vlastností v tomto}
		{@code comm// príklade):}
		{@code kwdprivate} {@code typeint} poslednýNastavenýTvar = {@code num1};

		{@code comm// Toto je záložná hodnota predchádzajúceho atribútu – je to hodnota,}
		{@code comm// ktorá bola prečítaná z konfigurácie (ak jestvovala) a slúži na detekciu}
		{@code comm// zmien (aby bola naplnená litera zapísaná v príklade TestKonfigurácie}
		{@code comm// uvedenom v opise triedy ObsluhaUdalostí):}
		{@code kwdprivate} {@code typeint} prečítanýNastavenýTvar = {@code num1};

		{@code comm// V tomto atribúte je názov naposledy čítaného SVG súboru, ktorého}
		{@code comm// údaje majú slúžiť ako zdroj tvarov (ide o jednu z dvoch tzv.}
		{@code comm// zákazníckych vlastností v tomto príklade):}
		{@code kwdprivate} {@link String String} poslednýČítanýSúbor = {@code valnull};

		{@code comm// Toto je záložná hodnota predchádzajúceho atribútu a je medzi nimi}
		{@code comm// rovnaký vzťah ako medzi dvomi celočíselnými atribútmi vyššie:}
		{@code kwdprivate} {@link String String} menoSúboruZKonfigurácie = {@code valnull};


		{@code comm// Konštruktor.}
		{@code kwdprivate} SVGTvarRobota()
		{
			{@code comm// Úprava rozmerov plátien volaním nadradeného konštruktora.}
			{@code valsuper}({@code num800}, {@code num600});

			{@code comm// Vypnutie automatického prekresľovania, pretože tento príklad}
			{@code comm// používa prekresľovanie v pravidelných časových intevaloch na}
			{@code comm// požiadanie (rovnako ako príklady uvedené v opise metódy}
			{@code comm// Svet.nebolPrekreslený() a triedy Obrázok).}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Tento príkaz zabezpečí, aby sa pri ďalších výpisoch metódami}
			{@code comm// sveta hlavný robot automaticky neskryl (vysvetlenie je uvedené}
			{@code comm// v poznámke v opise metódy Plátno.vypíš(…)):}
			{@link Svet Svet}.{@link Svet#vypíš(Object...) vypíš}({@code valthis});

			{@code comm// Definovanie obsluhy udalostí – obsluhujeme časovač, neúspešné}
			{@code comm// zadanie príkazu a konfiguráciu.}
			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code comm// Toto je reakcia na udalosti časovača.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
				{
					{@code comm// Vždy, keď má vnútorný indikátor potreby prekreslenia}
					{@code comm// pravdivú logickú hodnotu (true), zariadime spustenie}
					{@code comm// prekreslenia sveta:}
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}

				{@code comm// Reakcia na potvrdenie vstupu je v interaktívnom režime}
				{@code comm// spustená vtedy, keď potvrdený príkaz nebol rozpoznaný,}
				{@code comm// čiže v prípade chybného príkazu.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieVstupu() potvrdenieVstupu}()
				{
					{@code comm// Vyššie opísanú situáciu vyriešime tak, že potvrdený}
					{@code comm// reťazec vrátime späť do príkazového riadka, aby ho}
					{@code comm// mohol používateľ opraviť a znova potvrdiť.}
					{@link Svet Svet}.{@link Svet#textVstupnéhoRiadka(String) textVstupnéhoRiadka}({@link Svet Svet}.{@link Svet#prevezmiReťazec() prevezmiReťazec}());
				}


				{@code comm// Táto reakcia je spustená, keď si svet potrebuje overiť,}
				{@code comm// či má zmysel zapisovať konfiguráciu, či sa zmenila hodnota}
				{@code comm// niektorej zákazníckej vlastnosti. (Svet túto reakciu vôbec}
				{@code comm// nemusí spustiť.)}
				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}()
				{
					{@code comm// Nasledujúce riadky obsahujú kontroly zmien hodnôt}
					{@code comm// zákazníckych vlastností:}
					{@code kwdif} (poslednýNastavenýTvar != prečítanýNastavenýTvar)
						{@code kwdreturn} {@code valtrue};
					{@code kwdif} (poslednýČítanýSúbor != menoSúboruZKonfigurácie)
						{@code kwdreturn} {@code valtrue};

					{@code comm// (Tu by nasledovali ďalšie kontroly zmien hodnôt.)}

					{@code kwdreturn} {@code valfalse};
				}

				{@code comm// Táto reakcia dovoľuje zapísať zákaznícke vlastnosti do}
				{@code comm// konfiguračného súboru.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} java.io.IOException
				{
					{@code comm// Zapisujeme hodnoty vlastností:}
					súbor.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"nastavenýTvar"}, poslednýNastavenýTvar);
					súbor.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"čítanýSúbor"}, poslednýČítanýSúbor);

					{@code comm// (Tu by nasledovali ďalšie zápisy hodnôt.)}
				}

				{@code comm// Táto reakcia umožňuje prečítanie hodnôt zákazníckych}
				{@code comm// vlastností z konfiguračného súboru.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} java.io.{@link java.io.IOException IOException}
				{
					{@code comm// Čítanie hodnôt vlastností.}
					{@code comm// }
					{@code comm// Pri čítaní zmeníme pri každej čítanej vlastnosti naraz}
					{@code comm// hodnotu skutočnej vlastnosti aj hodnotu premennej}
					{@code comm// slúžiacej na uchovanie prečítanej hodnoty (tá slúži ako}
					{@code comm// detektor zmien a je vyhodnocovaná v reakcii}
					{@code comm// konfiguráciaZmenená.}

					poslednýNastavenýTvar = prečítanýNastavenýTvar =
						súbor.{@link Súbor#čítajVlastnosť(String, Long) čítajVlastnosť}({@code srg"nastavenýTvar"},
							({@code typelong})poslednýNastavenýTvar).{@link Long#intValue() intValue}();
					poslednýČítanýSúbor = menoSúboruZKonfigurácie =
						súbor.{@link Súbor#čítajVlastnosť(String, String) čítajVlastnosť}({@code srg"čítanýSúbor"}, poslednýČítanýSúbor);

					{@code comm// (Tu by nasledovali ďalšie čítania hodnôt.)}
				}
			};

			{@code comm// Zapnutie interaktívneho (príkazového) režimu a registrácia robota}
			{@code comm// v rámci konfigurácie:}
			{@link Svet Svet}.{@link Svet#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
			{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
			{@link Svet Svet}.{@link Svet#registrujRobot(GRobot) registrujRobot}({@code valthis});

			{@code comm// Čítanie naposledy čítaného súboru (počiatočná hodnota null má}
			{@code comm// špeciálny význam – vtedy sa metóda pokúsi čítať súbor}
			{@code comm// s predvoleným názvom – pozri definíciu metódy čítaj nižšie).}
			{@code comm// Keďže úspešné vykonanie tejto metódy mení (resetuje) hodnotu}
			{@code comm// poradového čísla naposledy nastavovaného tvaru (čo je žiaduce}
			{@code comm// pri vykonaní z príkazového riadka), je pred týmto volaním hodnota}
			{@code comm// tohto číselného atribútu zálohovaná a po ňom spätne obnovená:}
			{@code typeint} zálohaHodnotyTvaru = poslednýNastavenýTvar;
			čítaj(poslednýČítanýSúbor);
			poslednýNastavenýTvar = zálohaHodnotyTvaru;

			{@code comm// Nastavenie tvaru podľa atribútu, ktorého hodnotu sme práve}
			{@code comm// zálohovali a obnovili (pozri komentár a kód vyššie):}
			nastavTvar(poslednýNastavenýTvar);

			{@code comm// Spustenie časovača (inak by nefungovalo prekresľovanie spomínané}
			{@code comm// pri príkaze Svet.nekresli(); v úvode tohto konštruktora):}
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}


		{@code comm/////////////////////////////////////////////////////////////////}
		{@code comm// ##   Výhodou interaktívneho režimu je, že všetky nové    ## //}
		{@code comm// ##   definované verejné metódy (spĺňajúce kritériá       ## //}
		{@code comm// ##   opísané pri metóde Svet.interaktívnyRežim(zapni))   ## //}
		{@code comm// ##   sa automaticky stávajú príkazmi použiteľnými        ## //}
		{@code comm// ##   v tomto režime…                                     ## //}
		{@code comm/////////////////////////////////////////////////////////////////}


		{@code comm// Vypíše aktuálny počet útvarov uložených vo vnútornom zásobníku}
		{@code comm// inštancie svgPodpora. To je zároveň horná hranica číselného rozsahu}
		{@code comm// použiteľného s metódou nastavTvar (nižšie).}
		{@code kwdpublic} {@code typevoid} vypíšPočet()
		{
			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Počet tvarov v zásobníku: "}, {@link SVGPodpora svgPodpora}.{@link SVGPodpora#počet() počet}());
		}

		{@code comm// | Tento alias je definovaný len preto, aby bola v príkazovom riadku}
		{@code comm// | interaktívneho režimu použiteľná aj verzia tohto príkazu bez}
		{@code comm// | diakritiky.}
		{@code kwdpublic} {@code typevoid} vypisPocet() { vypíšPočet(); }


		{@code comm// Táto metóda slúži na prečítanie SVG súboru so zadaným menom. Tvary,}
		{@code comm// ktoré sa podaria nájsť inštancii svgPodpora budú použiteľné na}
		{@code comm// nastavenie vlastného tvaru robota.}
		{@code kwdpublic} {@code typevoid} čítaj({@link String String} meno)
		{
			{@code comm// Ak je namiesto názvu súboru zadaná hodnota null, tak je použitý}
			{@code comm// preddefinovaný názov súboru „SVGTvaryRobota.svg“:}
			{@code kwdif} ({@code valnull} == meno) meno = {@code srg"SVGTvaryRobota.svg"};

			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Čítam súbor: "}, meno);

			{@code comm// Pri každom novom čítaní sú z vnútorného zásobníka tvarov}
			{@code comm// inštancie svgPodpora vymazané všetky tvary:}
			{@link SVGPodpora svgPodpora}.{@link SVGPodpora#vymaž() vymaž}();

			{@code comm// Do premennej výsledok najprv vložíme hodnotu -1, ktorá signalizuje}
			{@code comm// chybu:}
			{@code typeint} výsledok = {@code num-1};

			{@code comm// V nasledujúcom bloku na zachytávanie výnimiek sa pokúsime}
			{@code comm// prečítať súbor so zadaným menom:}
			{@code kwdtry}
			{
				{@code comm// Ak sa čítanie podarí, tak v premennej výsledok bude počet}
				{@code comm// prečítaných tvarov (aj táto metóda môže vrátiť hodnotu -1}
				{@code comm// bez vzniku výnimky, takže pôvodná mínus jednotka môže byť}
				{@code comm// bez nášho vedomia prepísaná novou, ale to vôbec neprekáža,}
				{@code comm// lebo výsledná informácia je totožná):}
				výsledok = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#čítaj(String) čítaj}(meno);
			}
			{@code kwdcatch} ({@link Exception Exception} e)
			{
				{@code comm// V prípade vzniknutia výnimky je na štandardnom chybovo výstupe}
				{@code comm// vypísaná krátka správa o pôvode („stope v zásobníku“) chyby:}
				e.{@link Throwable#printStackTrace() printStackTrace}();
			}

			{@code comm// Podľa hodnoty premennej výsledok je vypísané hlásenie a prípadne}
			{@code comm// vykonané ďalšie kroky…}
			{@code kwdif} ({@code num-1} == výsledok)
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Čítanie súboru zlyhalo!"});
			{@code kwdelse}
			{
				{@code comm// V prípade úspechu je okrem vypísania správy nastavený}
				{@code comm// predvolený tvar robota a vymazaná oblasť obsahujúca}
				{@code comm// vlastný tvar:}
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Počet prečítaných tvarov: "}, výsledok);
				poslednýČítanýSúbor = meno;
				poslednýNastavenýTvar = {@code num0};
				tvarVOblasti = {@code valnull};
			}
			vypíšPočet();
		}

		{@code comm// | Tento alias je definovaný len preto, aby bola v príkazovom riadku}
		{@code comm// | interaktívneho režimu použiteľná aj verzia tohto príkazu bez}
		{@code comm// | diakritiky.}
		{@code kwdpublic} {@code typevoid} citaj({@link String String} meno) { čítaj(meno); }

		{@code comm// Táto verzia metódy dovoľuje použitie tohto príkazu bez zadania}
		{@code comm// parametra (namiesto ktorého je dosadená predvolená hodnota null).}
		{@code kwdpublic} {@code typevoid} čítaj() { čítaj({@code valnull}); }

		{@code comm// | Tento alias je definovaný len preto, aby bola v príkazovom riadku}
		{@code comm// | interaktívneho režimu použiteľná aj verzia tohto príkazu bez}
		{@code comm// | diakritiky.}
		{@code kwdpublic} {@code typevoid} citaj() { čítaj(); }


		{@code comm// Táto metóda vygeneruje taký tvar oblasti, ktorý bude použiteľný ako}
		{@code comm// vlastný tvar robota. Oblasť musí byť umiestnená v strede plátna, inak}
		{@code comm// by bol vlastný tvar posunutý oproti skutočnej polohe robota. Tvar}
		{@code comm// oblasti je vygenerovaný z SVG tvaru, ktorý je uložený vo vnútornom}
		{@code comm// zásobníku inštancie svgPodpora pod poradovým číslom určeným parametrom}
		{@code comm// „ktorý.“ Metóda tiež zariadi, aby bola veľkosť tvaru oblasi prispôsobená}
		{@code comm// podľa aktuálnej veľkosti a mierky robota.}
		{@code kwdpublic} {@code typevoid} nastavTvar({@code typeint} ktorý)
		{
			{@code comm// Hodnota nula znamená predvolený tvar robota.}
			{@code kwdif} ({@code num0} == ktorý)
			{
				poslednýNastavenýTvar = {@code num0};
				tvarVOblasti = {@code valnull};

				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Nastavený predvolený tvar."});
			}
			{@code kwdelse}
			{
				{@code comm// Všetky ostatné hodnoty znamenajú poradové číslo tvaru vo}
				{@code comm// vnútornom zásobníku inštancie svgPodpora, pričom metódy}
				{@code comm// dávajúce tvary smú prijímať aj záporné čísla – v tom prípade}
				{@code comm// to znamená n-tý tvar od konca zásobníka. Kladné hodnoty}
				{@code comm// musia byť odlíšené, pretože metóda funguje rovnako ako}
				{@code comm// všetko v jazyku Java – nula je index prvého tvaru. Index}
				{@code comm// vypočítame zo zadaného poradového čísla jednoducho, je to}
				{@code comm// hodnota: ktorý − 1.}
				{@link Shape Shape} tvar = {@link SVGPodpora svgPodpora}.{@link SVGPodpora#dajVýsledný(int) dajVýsledný}(ktorý &lt; {@code num0} ? ktorý : ktorý &#45; {@code num1});

				{@code kwdif} ({@code valnull} == tvar)
				{
					{@code comm// Ak metóda dajVýsledný vráti null, znamená to, že}
					{@code comm// tvar nebol nájdený (lebo požadovaný index je mimo}
					{@code comm// rozsahu).}
					{@link Svet Svet}.{@link Svet#pípni() pípni}();
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Tvar s poradovým číslom "},
						ktorý, {@code srg" nebol nájdený."});
				}
				{@code kwdelse}
				{
					{@code comm// V prípade úspešne nájdeného tvaru najprv upravíme}
					{@code comm// jeho veľkosť podľa aktuálnej veľkosti a mierky robota,}
					{@code comm// pričom ak sú veľkosť robota v súčine s mierkou rovné}
					{@code comm// hodnote desať, tak to značí, že veľkosť tvaru nemá}
					{@code comm// byť zmenená.}

					{@code comm// Atribút poslednáVeľkosť bude slúžiť na overenie zmeny}
					{@code comm// veľkosti vlastného tvaru a zároveň ho použijeme}
					{@code comm// v nasledujúcom kroku (na overenie potreby úpravy veľkosti}
					{@code comm// tvaru a na jej prípadné vykonanie).}
					poslednáVeľkosť = {@link GRobot#veľkosť() veľkosť}() * {@link GRobot#mierka() mierka}();

					{@code kwdif} ({@code num10.0} != poslednáVeľkosť)
						{@code comm// Na zmenu veľkosti tvaru (v prípade, že je potrebné ju}
						{@code comm// upraviť) je použitá transformácia MIERKA:}
						tvar = {@link SVGPodpora SVGPodpora}.{@link SVGPodpora#dajVýsledný(Shape, Transformácia, Transformácia...) dajVýsledný}(
							tvar, {@code kwdnew} {@link Transformácia#SVGPodpora.Transformácia(int, Double...) Transformácia}(
								{@link Transformácia Transformácia}.{@link Transformácia#MIERKA MIERKA},
								poslednáVeľkosť / {@code num10.0}));

					{@code comm// Potom presunieme vrátený tvar do stredu súradnicovej}
					{@code comm// sústavy grafického robota. Môžeme použiť metódu}
					{@code comm// SVGPodpora.presuňDoStredu(tvar):}
					tvarVOblasti = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}({@link SVGPodpora SVGPodpora}.{@link SVGPodpora#presuňDoStredu(Shape) presuňDoStredu}(tvar));

					{@code comm// Nakoniec zaznamenáme poradové číslo použitého tvaru:}
					poslednýNastavenýTvar = ktorý;

					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Nastavený tvar: "}, ktorý);
				}
			}
		}


		{@code comm// Táto verzia metódy dovoľuje použitie príkazu tohto bez zadania}
		{@code comm// parametra (namiesto ktorého je dosadená predvolená hodnota 1).}
		{@code kwdpublic} {@code typevoid} nastavTvar() { nastavTvar({@code num1}); }


		{@code comm// Táto reakcia zabezpečuje kreslenie vlastného (a v tomto prípade aj}
		{@code comm// predvoleného) tvaru robota.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#kresliTvar() kresliTvar}()
		{
			{@code comm// Ak je definovaná oblasť s vlastným tvarom, tak sa vykoná vetva}
			{@code comm// zabezpečujúca nakreslenie vlastného tvaru (s nejakými kontrolami).}
			{@code kwdif} ({@code valnull} != tvarVOblasti && {@code num0} != poslednýNastavenýTvar)
			{
				{@code comm// Ak sa zmenila veľkosť robota (alebo mierka), tak nastane}
				{@code comm// pregenerovanie tvaru:}
				{@code kwdif} (poslednáVeľkosť != {@link GRobot#veľkosť() veľkosť}() * {@link GRobot#mierka() mierka}())
					nastavTvar(poslednýNastavenýTvar);

				{@code comm// Podľa toho, či je aktivované vypĺňanie tvarov robotom}
				{@code comm// bude oblasť vyplnená alebo obkreslená:}
				{@code kwdif} ({@link GRobot#vypĺňaTvary() vypĺňaTvary}())
					{@link GRobot#vyplňOblasť(Area) vyplňOblasť}(tvarVOblasti);
				{@code kwdelse}
					{@link GRobot#obkresliOblasť(Area) obkresliOblasť}(tvarVOblasti);
			}
			{@code kwdelse}
			{
				{@code comm// Alternatívna vetva obsahuje len príkaz kreslenia predvoleného}
				{@code comm// tvaru robota:}
				{@link GRobot#trojzubec() trojzubec}();
			}
		}


		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"SVGTvarRobota.cfg"});
			{@code kwdnew} SVGTvarRobota();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>SVGTvarRobota.png<alt/>Ukážka zmeny tvaru robota s pomocou
 * SVG tvarov tesne po prvom spustení.</image>Ukážka vzhľadu okna po
 * prvom spustení aplikácie<br /><small>(ukážka je zmenšená)</small>.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje ako previesť (konvertovať) rastrový
 * obrázok prečítaný z PNG súboru na množinu kruhov s veľkosťami
 * podľa lokálneho jasu konkrétnych častí obrázka. Výsledok konverzie
 * je uložený do vektorového SVG súboru.</p>
 * 
 * <p>Budeme postupovať tak, že najskôr prečítame bitmapový obrázok
 * zo súboru s názvom „mola.jpeg,“ obrázok virtuálne rozdelíme na bloky
 * veľké 10 × 10 bodov (pixelov), vypočítame priemerný farebný jas
 * bodov v každom bloku a podľa týchto hodnôt vyrobíme kruhy tvoriace
 * mozaiku nového obrázka vo formáte SVG. (Viaceré hodnoty v príklade
 * sú zadané „napevno,“ nie univerzálne. Dôvodom je úsilie
 * o nezvyšovanie zložitosti príkladu.)</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} SVGKruhy {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Konštruktor.}
		{@code kwdprivate} SVGKruhy()
		{
			{@code comm// Úprava rozmerov plátna volaním nadradeného konštruktora.}
			{@code valsuper}({@code num800}, {@code num600});

			{@code comm// Skryje robot:}
			{@link GRobot#skry() skry}();

			{@code comm// Vypnutie automatického prekresľovania.}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Vytvorí obrázok zo súboru uloženého na pevnom disku}
			{@code comm// (prípadne inej jednotke, ktorá je aktuálna):}
			{@link Obrázok Obrázok} obrázok = {@link Obrázok Obrázok}.{@link Obrázok#prečítaj(String) prečítaj}({@code srg"mola.jpeg"});

			{@code comm// Zobrazí prečítaný obrázok v mierke 1 : 2, keďže obrázok má}
			{@code comm// v porovnaní s plátnami dvojnásobné rozmery. (Samozrejme, že}
			{@code comm// toto nie je univerzálne riešenie. Univerzálne riešenie by muselo}
			{@code comm// porovnať rozmery obrázka s rozmermi plátien a na základe toho}
			{@code comm// vypočítať mierku.)}
			{@link GRobot#obrázok(Image, double) obrázok}(obrázok, {@code num0.5});

			{@code comm// Ak teraz presmerujeme kreslenie na strop, kresba obrázka na}
			{@code comm// podlahe zostane neporušená a môžeme s ňou manipulovať samostatne}
			{@code comm// (to nesúvisí s grafikou samotného obrázka, tá zostane neporušená}
			{@code comm// i tak):}
			{@link GRobot#kresliNaStrop() kresliNaStrop}();

			{@code comm// Umiestnime robot na počiatočnú pozíciu – do ľavého dolného rohu}
			{@code comm// plátna. Poloha robota je dôležitá len pre výstupné SVG údaje.}
			{@link GRobot#skočNa(double, double) skočNa}({@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}() + {@code num2.5}, {@link Svet Svet}.{@link Svet#najmenšieY() najmenšieY}() + {@code num2.5});

			{@code comm// Nastavenie (konštantnej) veľkosti robota má zmysel najmä pri}
			{@code comm// inej verzii konverzie obrázka, ale výkonu aplikácie to neublíži:}
			{@link GRobot#veľkosť(double) veľkosť}({@code num2.5});


			{@code comm// Úlohou nasledujúcich štyroch (postupne sa vnárajúcich) cyklov}
			{@code comm// „for“ je prechádzať po blokoch 10 × 10 pixelov po obrázku,}
			{@code comm// vypočítať z každého bloku priemer jasu (alebo farebnosti, prípadne}
			{@code comm// iného parametra) a z vypočítaného údaja vyrobiť tvar, ktorý bude}
			{@code comm// uložený do SVG údajov.}
			//
			{@code comm// Poznámky:}
			{@code comm// ---------}
			//
			{@code comm// Využívané sú len metódy/príkazy programovacieho rámca GRobot.}
			{@code comm// Toto riešenie síce nie je optimálne, ale je odolné voči chybám,}
			{@code comm// pretože spracúvané hodnoty podstupujú viacnásobnú kontrolu.}
			{@code comm// Keby sme však potrebovali dávkovo spracovať veľké množstvo údajov}
			{@code comm// (napríklad mnoho obrázkov), bolo by nevyhnutné riešenie}
			{@code comm// optimalizovať. Odporúčame priamu prácu s rastrom:}
			//
			{@code comm//    int[] údaje = ((java.awt.image.DataBufferInt)obrázok.}
			{@code comm//        getRaster().getDataBuffer()).getData();}
			{@code comm//    …}
			//
			{@code comm// Takto získané celočíselné pole obsahuje farebné (ARGB) údaje}
			{@code comm// o jednotlivých bodoch obrázka počnúc ľavým horným rohom,}
			{@code comm// pokračujúc doprava a po jednotlivých riadkoch až na koniec.}
			//
			{@code kwdfor} ({@code typedouble} y = obrázok.{@link Obrázok#najmenšieY() najmenšieY}();
				y &lt;= obrázok.{@link Obrázok#najväčšieY() najväčšieY}(); y += {@code num10.0})
			{
				{@code kwdfor} ({@code typedouble} x = obrázok.{@link Obrázok#najmenšieX() najmenšieX}();
					x &lt;= obrázok.{@link Obrázok#najväčšieX() najväčšieX}(); x += {@code num10.0})
				{
					{@code comm// Do nasledujúcich premenných budú ukladané údaje určené}
					{@code comm// na výsledné spracovanie. V tomto prípade je to celková}
					{@code comm// suma hodnôt farebných zložiek všetkých bodov a počet}
					{@code comm// bodov, ktoré boli skutočne vyhodnotené.}
					{@code typeint} suma = {@code num0};
					{@code typeint} počet = {@code num0};

					{@code comm// Spracovanie bloku 10 × 10 pixelov:}
					{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num10}; ++j)
					{
						{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num10}; ++i)
						{
							{@code comm// Najprv je prečítaná farba každého bodu. Bod je}
							{@code comm// čítaný bez ohľadu na polohu robota. Súradnice}
							{@code comm// bodu sú určené výhradne riadiacimi premennými}
							{@code comm// štyroch cyklov „for“:}
							{@link Farba Farba} farba = obrázok.{@link Obrázok#farbaBodu(double, double) farbaBodu}(x + i, y + j);

							{@code comm// Ak neboli požadované súradnice mimo hraníc}
							{@code comm// obrázka, tak je získaná farba spracovaná:}
							{@code kwdif} ({@link Farebnosť#žiadna žiadna} != farba)
							{
								{@code comm// Zvýši sa počítadlo spracovaných farieb:}
								++počet;

								{@code comm// A súčet všetkých farebných zložiek je pridaný}
								{@code comm// do celkovej sumy:}
								suma += farba.{@link Farba#červená() červená}() +
									farba.{@link Farba#zelená() zelená}() + farba.{@link Farba#modrá() modrá}();
							}
						}
					}

					{@code comm// Vyrobiť tvar má zmysel len ak sú k dispozícii údaje}
					{@code comm// na spracovanie.}
					{@code kwdif} ({@code num0} != počet)
					{
						{@code comm// Upravíme veľkosť robota podľa priemeru vypočítaného}
						{@code comm// z farebných hodnôt všetkých spracovaných bodov}
						{@code comm// (čísla vo vzorci sme získali empiricky – to jest}
						{@code comm// čiastočne odhadom na základe predchádzajúcich}
						{@code comm// vedomostí a skúseností a čiastočne „doladením“ podľa}
						{@code comm// estetiky výsledku):}
						{@link GRobot#veľkosť(double) veľkosť}({@code num3.25} &#45; ((suma / ({@code num3} * počet)) / {@code num80.0}));

						{@code comm// Do inštancie svgPodpora pridáme čierny krúžok na}
						{@code comm// pozícii robota s veľkosťou robota:}
						{@link SVGPodpora svgPodpora}.{@link SVGPodpora#pridaj(Shape, String...) pridaj}({@link GRobot#kruh() kruh}(), {@code srg"fill"}, {@code srg"black"});
					}

					{@code comm// Posunutie robota o kúsok doprava – synchrónne s prechodom}
					{@code comm// na ďalší spracúvaný blok pixelov:}
					{@link GRobot#skoč(double, double) skoč}({@code num5.0}, {@code num0.0});
				}

				{@code comm// Posunutie robota na začiatok vyššieho riadka:}
				{@link GRobot#skočNa(double, double) skočNa}({@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}() + {@code num2.5}, {@link GRobot#polohaY() polohaY}() + {@code num5.0});

				{@code comm// Prekreslenie sveta (skrytím tohto príkazu do komentára sa}
				{@code comm// proces konverzie značne urýchli):}
				{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			}


			{@code comm// Skrytie obrázka na podlahe:}
			{@link Plátno podlaha}.{@link Plátno#priehľadnosť(double) priehľadnosť}({@code num0});

			{@code comm// Uloženie SVG súboru (s predvoleným titulkom a prepisujúc}
			{@code comm// prípadný pôvodný súbor):}
			{@link SVGPodpora svgPodpora}.{@link SVGPodpora#zapíš(String) zapíš}({@code srg"mola-bodkova.svg"}, {@code valnull}, {@code valtrue});

			{@code comm// Opätovné zapnutie automatického prekresľovania.}
			{@link Svet Svet}.{@link Svet#kresli() kresli}();
		}


		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"SVGKruhy.cfg"});
			{@code kwdnew} SVGKruhy();
		}
	}
	</pre>
 * 
 * <p> <br /><a target="_blank" href="resources/mola.jpeg">mola.jpeg</a> –
 * pôvodný rastrový obrázok na prevzatie.<br /> </p>
 * 
 * <p><image>mola-mensia.jpeg<alt/>Ukážka predlohy fotografie
 * s moľou.</image>Pôvodný rastrový obrázok<br /><small>(zobrazovaný
 * obrázok je oproti pôvodnému obrázku <a target="_blank"
 * href="resources/mola.jpeg">na prevzatie</a> zmenšený
 * o 50 %)</small>.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>mola-bodkova.svg<alt/>Mola prevedená na bodkovaný obraz
 * prevedený do SVG formátu.</image>Výsledný <a target="_blank"
 * href="resources/mola-bodkova.svg">SVG obraz</a>.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje vytvorenie fraktálneho útvaru rastlinky
 * s pomocou korytnačej grafiky grafického robota a jej uloženie do HTML
 * súboru obsahujúceho SVG definíciu. Generovanie rastlinky používa
 * náhodné čísla, preto každé spustenie vygeneruje a uloží unikátny
 * obrázok. V skutočnosti sú ukladané dva súbory – rastrový obrázok vo
 * formáte PNG a HTML obsahujúce SVG. Ďalšie podrobnosti o fungovaní
 * príkladu sú v jeho komentároch.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code comm// Túto triedu Javy reprezentujúcu 2D úsečku potrebujeme na pridávanie}
	{@code comm// jednotlivých úsečiek do inštancie svgPodpora.}
	{@code kwdimport} java.awt.geom.{@link Line2D Line2D};

	{@code comm// V tomto príklade je implementovaných viacero mechanizmov, ktoré priamo}
	{@code comm// nesúvisia s formátom SVG. Všetko je podrobne vysvetlené v komentároch.}
	{@code comm// SVG formátu sa dotýkajú všetky pasáže používajúce triedu SVGPodpora}
	{@code comm// a inštanciu svgPodpora.}
	{@code kwdpublic} {@code typeclass} SVGRastlinka {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Súkromný konštruktor.}
		{@code kwdprivate} SVGRastlinka()
		{
			{@code comm// Úprava rozmerov plátna volaním nadradeného konštruktora.}
			{@code valsuper}({@code num800}, {@code num640});

			{@code comm// Vypnutie predvoleného automatického prekresľovania.}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Obsluha udalostí zabezpečujúca prekresľovanie sveta}
			{@code comm// v pravidelných časových intervaloch.}
			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}
			};

			{@code comm// Spustenie časovača (na pravidelné prekresľovanie).}
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();

			{@code comm// Úvodné nastavenie robota a spustenie kreslenia rastlinky.}
			{@link GRobot#skoč(double, double) skoč}({@code num0}, {@code num-300});
			{@link GRobot#farba(int, int, int, int) farba}({@code num0}, {@code num120}, {@code num0}, {@code num120});
			rastlinka({@code num96});

			{@code comm// Skrytie robota.}
			{@link GRobot#skry() skry}();
		}

		{@code comm// Atribúty slúžiace na uchovanie súradníc úsečky.}
		{@code kwdprivate} {@code typedouble} x0, y0, x1, y1;

		{@code comm// Pomocná metóda na zaznamenanie aktuálnej polohy robota.}
		{@code kwdprivate} {@code typevoid} uložPolohu()
		{
			{@code comm// Je dôležité prepočítať súradnice zo súradnicového priestoru}
			{@code comm// rámca do súradnicového priestoru, ktorý je používaný formátom}
			{@code comm// SVG. Vo viacerých prípadoch je konverzia vykonaná automaticky,}
			{@code comm// ale tu to nie je možné, pretože čítame priamo polohu robota,}
			{@code comm// ktorú ukladáme ako bezrozmerné číslo, ktorého pôvod nie je možné}
			{@code comm// detegovať žiadnou súčasťou programovacieho rámca alebo Javy. Rovnaká situácia}
			{@code comm// nastáva aj v metóde pridajÚsečku (nižšie).}
			{@code comm// }
			{@code comm// O súradnicových priestoroch je viac napísané v príklade v opise}
			{@code comm// metódy cesta() triedy GRobot a v opisoch metód zapíš a čítaj}
			{@code comm// v tejto triede (SVGPodpora)…}
			x0 = x1 = {@link Svet Svet}.{@link Svet#prepočítajX(double) prepočítajX}({@link GRobot#polohaX() polohaX}());
			y0 = y1 = {@link Svet Svet}.{@link Svet#prepočítajY(double) prepočítajY}({@link GRobot#polohaY() polohaY}());
		}

		{@code comm// Pomocná metóda na pridanie úsečky do SVG údajov.}
		{@code kwdprivate} {@code typevoid} pridajÚsečku()
		{
			{@code comm// Je dôležité prepočítať súradnice zo súradnicového priestoru}
			{@code comm// rámca do súradnicového priestoru, ktorý je používaný formátom}
			{@code comm// SVG. Podrobnejšie je to vysvetlené v komentároch v metóde}
			{@code comm// uložPolohu (vyššie).}
			x0 = x1; x1 = {@link Svet Svet}.{@link Svet#prepočítajX(double) prepočítajX}({@link GRobot#polohaX() polohaX}());
			y0 = y1; y1 = {@link Svet Svet}.{@link Svet#prepočítajY(double) prepočítajY}({@link GRobot#polohaY() polohaY}());

			{@code comm// Z prevedených súradníc vytvoríme úsečku, ktorú vložíme do}
			{@code comm// vnútorného zásobníka tvarov inštancie svgPodpora a vzápätí}
			{@code comm// upravíme jej vizuálne vlastnosti.}
			{@link SVGPodpora svgPodpora}.{@link SVGPodpora#pridaj(Shape, String...) pridaj}({@code kwdnew} {@link Line2D Line2D}.{@link Line2D.Double#Line2D.Double(double, double, double, double) Double}(x0, y0, x1, y1));
			{@link SVGPodpora svgPodpora}.{@link SVGPodpora#hrúbkaČiary(int, double) hrúbkaČiary}({@code num-1}, {@link GRobot#hrúbkaČiary() hrúbkaČiary}());
			{@link SVGPodpora svgPodpora}.{@link SVGPodpora#farbaČiary(int, Color) farbaČiary}({@code num-1}, {@link GRobot#farba() farba}());
		}

		{@code comm// Rekurzívna metóda na kreslenie rastlinky}
		{@code kwdprivate} {@code typevoid} rastlinka({@code typedouble} dĺžka)
		{
			{@code kwdif} (dĺžka &lt; {@code num2}) {@code kwdreturn};

			{@code comm// V komentároch v tejto metóde vysvetlíme princípy, ktorými sa}
			{@code comm// riadi jej vykonávanie. Na zjednodušenie ignorujme v najbližších}
			{@code comm// úvahách rekurzívne volanie. (Aj tak sa nakoniec ukáže, že ak}
			{@code comm// správne uvažujeme, tak sa ním nemusíme zaoberať.)}

			{@code comm// Hlavná zásada znie:}
			{@code comm// -------------------}
			{@code comm// }
			{@code comm//   #######################################################}
			{@code comm//   ##  Po vykonaní tejto metódy musí mať robot rovnakú  ##}
			{@code comm//   ##  orientáciu a polohu ako mal pred jej vykonaním.  ##}
			{@code comm//   #######################################################}

			{@code comm// „Vypočítame,“ respektíve náhodne vygenerujeme, dva uhly, o ktoré}
			{@code comm// sa bude robot otáčať pred prechodom do rekurzívnej vetvy. Uhly}
			{@code comm// si musíme zapamätať v lokálnych premenných, aby sme boli schopní}
			{@code comm// zabezpečiť dodržanie hlavnej zásady (vyššie).}
			{@code typedouble} uhol1 = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num0}, {@code num30});
			{@code typedouble} uhol2 = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num0}, {@code num30});

			{@code comm// Nastavíme hrúbku čiary a položíme pero:}
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}(dĺžka / {@code num12});
			{@link GRobot#položPero() položPero}();

			{@code comm// Prostredný z nasledujúcej trojice príkazov mení polohu robota.}
			{@code comm// Túto zmenu treba na konci metódy vrátiť naspäť (v súlade}
			{@code comm// s dodržaním hlavnej zásady uvedenej vyššie). Keďže príkazom}
			{@code comm// vyššie sme položili pero, robot kreslí čiaru. Nakreslenú úsečku}
			{@code comm// zároveň zaznamenáme do SVG údajov metódami uložPolohu}
			{@code comm// a pridajÚsečku.}
			uložPolohu();
			{@link GRobot#dopredu(double) dopredu}(dĺžka);
			pridajÚsečku();

			{@code comm// Pootočenie doprava vlastne zjednodušene znamená odčítanie}
			{@code comm// zadaného uhla od aktuálneho uhla. Súčet všetkých pootočení}
			{@code comm// musí byť rovný nule. Toto je prvé pootočenie v zápornom smere:}
			{@link GRobot#vpravo(double) vpravo}(uhol1);

			{@code comm// Prvé rekurzívne volanie – tu nastáva vetvenie rastlinky vpravo:}
			rastlinka(dĺžka &#45; {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num5}, {@code num16}));

			{@code comm// Toto pootočenie v kladnom smere vykompenzuje predchádzajúce}
			{@code comm// prvé záporné pootočenie, no zároveň robot pootočí o ďalší úsek}
			{@code comm// v kladnom smere (doľava):}
			{@link GRobot#vľavo(double) vľavo}(uhol1 + uhol2);

			{@code comm// Prvé rekurzívne volanie – tu nastáva vetvenie rastlinky vľavo:}
			rastlinka(dĺžka &#45; {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num5}, {@code num16}));

			{@code comm// Posledné pootočenie vykompenzuje poslednú zmenu orientácie na nulu:}
			{@link GRobot#vpravo(double) vpravo}(uhol2);

			{@code comm// Nakoniec vrátime zmenu polohy tak, aby sa robot vrátil do svojej}
			{@code comm// východiskovej polohy. Musíme to urobiť až po vykompenzovaní}
			{@code comm// všetkých zmien orientácie, inak by sa robot vrátil na iné miesto.}
			{@code comm// Vďaka dodržaniu hlavnej zásady si nemusíme robiť starosti s tým,}
			{@code comm// čo sa udeje s polohou a orientáciou robota počas rekurzívneho}
			{@code comm// volania pretože práve dodržanie tejto zásady zabezpečí, že po}
			{@code comm// návrate z rekurzívneho volania bude poloha a orientácia robota}
			{@code comm// zachovaná. Z pohľadu orientácie a polohy sa rekurzívnym volaním}
			{@code comm// nič nezmenilo. Bez tohto faktu by sa s týmto typom fraktálneho}
			{@code comm// kreslenia vôbec nedalo pracovať. Práve preto je zásada uvedená}
			{@code comm// na začiatku klasifikovaná ako hlavná. }
			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#dozadu(double) dozadu}(dĺžka);
		}

		{@code comm// Tento atribút slúži na číslovanie verzií uložených grafických súborov.}
		{@code kwdprivate} {@code kwdstatic} {@code typeint} n = {@code num0};

		{@code comm// Táto metóda slúži na uloženie ďalšej verzie vygenerovaného obrázka}
		{@code comm// v dvoch formátoch – PNG a HTML obsahujúce SVG definície.}
		{@code kwdprivate} {@code kwdstatic} {@code typevoid} uložĎalšíObrázok({@link String String} názov)
		{
			{@code comm// Keďže v tomto príklade je implementované automatické}
			{@code comm// prekresľovanie časovačom, je potrebné pred uložením}
			{@code comm// prekresliť svet. Mohlo by sa totiž stať, že fáza ukladania}
			{@code comm// by predbehla fázu prekreslenia, takže by sa do súboru vo formáte}
			{@code comm// PNG uložil neúplný obrázok (SVG údaje sú vkladané do objektu}
			{@code comm// svgPodpora priebežne a nesúvisia s prekresľovaním).}
			{@link Svet Svet}.{@link Svet#prekresli() prekresli}();

			{@code comm// Zápis cyklu „for“ s chýbajúcim podmieneným výrazom (nižšie) znamená}
			{@code comm// „nekonečný cyklus.“ Žiadny cyklus nesmie byť pri programovaní}
			{@code comm// nekonečný! Spôsobovalo by to uviaznutie. Aj v tomto prípade nejde}
			{@code comm// o skutočný nekonečný cyklus, ale o cyklus, na ktorom sa usilujeme}
			{@code comm// ukázať také opakovanie, ktorého podmienky ukončenia nie je možné}
			{@code comm// zapísať v tvare logických (booleovských) výrazov.}
			{@code comm// }
			{@code comm// V tomto prípade ide o to, aby sa cyklus ukončil až po úspešnom}
			{@code comm// zápise obidvoch grafických súborov – PNG a HTML obsahujúce SVG}
			{@code comm// definície (ďalej HTML/SVG). To znamená, že ďalšia iterácia}
			{@code comm// nastáva iba v prípade neúspešného zápisu jedného zo súborov.}
			{@code comm// Ak zlyhá zápis PNG súboru, tak vznikne výnimka, o ktorej je}
			{@code comm// poslaná krátka správa na chybový konzolový výstup (err)}
			{@code comm// a pokračuje sa ďalšou iteráciou. Ak zlyhá zápis HTML/SVG}
			{@code comm// súboru, tak sa preruší vykonávanie aktuálnej iterácie}
			{@code comm// (a akoby mimochodom sa vymaže práve zapísaný PNG súbor, aby}
			{@code comm// vo výsledku obsahy oboch typov súborov korešpondovali – je to}
			{@code comm// obeť princípu – takáto situácia môže nastať v takom prípade,}
			{@code comm// ak používateľ vymaže niektorý PNG súbor, ale ponechá HTML/SVG}
			{@code comm// súbor, inak by nastať nemala) a pokračuje sa nasledujúcou}
			{@code comm// iteráciou. Ak nezlyhá zápis ani jedného súboru, tak je činnosť}
			{@code comm// metódy ukončená. V každom z uvedených prípadov je vykonaný blok}
			{@code comm// finally, v ktorom nastáva zvyšovanie hodnoty počítadla n (ktoré}
			{@code comm// slúži na číslovanie verzií ukladaných súborov). Celý tento}
			{@code comm// relatívne komplikovaný (prinajmenšom komplikovane znejúci)}
			{@code comm// mechanizmus vedie k nasledujúcemu výslednému efektu:}
			{@code comm// }
			{@code comm// Predpokladajme, že program je spustený prvý raz a nemá šancu}
			{@code comm// jestvovať ani jeden súbor s obrázkom (PNG alebo HTML/SVG). Ak}
			{@code comm// sa nevyskytne žiadny nepredvídateľný problém (napr. hardvérové}
			{@code comm// zlyhanie), tak budú obidva súbory (.png a .html) korektne}
			{@code comm// zapísané, metóda bude hneď po prvom pokuse (to jest prvej}
			{@code comm// iterácii „nekonečného cyklu“) ukončená, no ešte pred skončením}
			{@code comm// stihne aktualizovať hodnotu počítadla n (to jest zvýšiť ju}
			{@code comm// o jeden v bloku finally, pretože blok finally má to výsostné}
			{@code comm// postavenie, že je vykonaný vždy).}
			{@code comm// }
			{@code comm// Teraz predpokladajme, že program je spustený n-tý raz a jestvuje}
			{@code comm// bližšie neurčený počet uložených PNG a HTML/SVG súborov. Zápis}
			{@code comm// PNG súboru bude zlyhávať dokedy bude jestvovať súbor s aktuálnym}
			{@code comm// poradovým číslom, pričom po každom ďalšom pokuse sa poradové}
			{@code comm// číslo zvýši o jeden. Pri prvom poradovom čísle, pre ktoré nebude}
			{@code comm// jestvovať prislúchajúci súbor zápis uspeje a ďalej sa bude}
			{@code comm// pokračovať tak ako v predchádzajúcom prípade. Algoritmus bude}
			{@code comm// úspešný aj v prípade, že by príčina zlyhania bola iná. Ukončí}
			{@code comm// sa jednoducho pri prvom úspechu.}
			{@code comm// }
			{@code comm// Aby sme zamedzili uviaznutiu v nekonečnom cykle v takom prípade,}
			{@code comm// keď je cieľové umiestnenie súborov na zápis chránené proti zápisu,}
			{@code comm// je vo vetve catch umiestnená kontrola, ktorá overí, či súbor}
			{@code comm// s aktuálnym poradovým číslom jestvuje. Ak nie, znamená to, že}
			{@code comm// príčina zlyhania bola zrejme iná a vykonávanie metódy bude}
			{@code comm// predčasne ukončené. Algoritmus by vďaka tejto kontrole nemal}
			{@code comm// uviaznuť.}
			{@code comm// }
			{@code comm// Implementácia tohto algoritmu by samozrejme mohla byť aj iná, no}
			{@code comm// cieľom bolo ukázať možnosť vytvorenia takého cyklu, ktorý nie je}
			{@code comm// riadený výhradne booleovskými podmienkami.}
			{@code kwdfor} (;;)
			{
				{@code kwdtry}
				{
					{@code comm// Pokus o uloženie PNG súboru.}
					{@link Svet Svet}.{@link Svet#uložObrázok(String) uložObrázok}(názov + {@code srg"-"} + n + {@code srg".png"});

					{@code comm// Pokus o uloženie HTML/SVG súboru.}
					{@code kwdif} ({@code num-1} == {@link SVGPodpora svgPodpora}.{@link SVGPodpora#zapíš(String) zapíš}(názov + {@code srg"-"} + n + {@code srg".html"}))
					{
						{@link Súbor Súbor}.{@link Súbor#vymaž(String) vymaž}(názov + {@code srg"-"} + n + {@code srg".png"});
						{@code kwdcontinue};
					}

					{@code comm// Ukončenie – sem sa vykonávanie algoritmu dostane len}
					{@code comm// ak boli obidva zápisy úspešné.}
					{@code kwdreturn};
				}
				{@code kwdcatch} ({@link Throwable Throwable} t)
				{
					{@code comm// Oznámenie o chybe na chybovú konzolu.}
					{@link System System}.{@link System#err err}.{@link java.io.PrintStream#println(String) println}(t.{@link Throwable#getMessage() getMessage}());

					{@code comm// Test uviaznutia pri jednotke chránenej na zápis.}
					{@code kwdif} (!{@link Súbor Súbor}.{@link Súbor#jestvuje(String) jestvuje}(názov + {@code srg"-"} + n + {@code srg".png"}))
					{
						{@code comm// Oznámenie možnej príčiny a ukončenie.}
						{@link System System}.{@link System#err err}.{@link java.io.PrintStream#println(String) println}({@code srg"Disk je pravdepodobne "} +
							{@code srg"chránený proti zápisu!"});
						{@code kwdreturn};
					}
				}
				{@code kwdfinally}
				{
					{@code comm// Tento príkaz sa vykoná na konci každej iterácie, ale aj}
					{@code comm// tesne pred skončením metódy (popri vykonaní príkazu}
					{@code comm// return). Zabezpečuje postupné zvyšovanie hodnoty}
					{@code comm// atribútu n, ktorý slúži na číslovanie zapisovaných súborov.}
					++n;
				}
			}
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"svg-rastlinka.cfg"});

			{@code kwdtry}
			{
				{@code comm// Spustenie časomiery, vygenerovanie rastlinky}
				{@code comm// a zastavenie časomiery:}
				{@link Svet Svet}.{@link Svet#spustiČasomieru() spustiČasomieru}();
				{@code kwdnew} SVGRastlinka();
				{@code typedouble} čas = {@link Svet Svet}.{@link Svet#zastavČasomieru() zastavČasomieru}();

				{@code comm// Uloženie vygenerovanej rastlinky do súborov vo formátoch}
				{@code comm// PNG a HTML/SVG:}
				uložĎalšíObrázok({@code srg"rastlinka"});

				{@code comm// Vypísanie výsledku merania (času) na konzolu sveta}
				{@code comm// a systémovú konzolu:}
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Kreslenie rastlinky trvalo: "}, čas, {@code srg"s"});
				{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Kreslenie rastlinky trvalo: "} + čas + {@code srg" s"});

				{@code comm// Uloženie textov (momentálne jediného riadka) konzoly sveta}
				{@code comm// do schránky operačného systému:}
				{@link Svet Svet}.{@link Svet#textyDoSchránky() textyDoSchránky}();
			}
			{@code kwdcatch} ({@link Throwable Throwable} t)
			{
				{@code comm// Oznámenie o prípadnej chybe:}
				t.{@link Throwable#printStackTrace() printStackTrace}();
			}
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>rastlinka-svg.png<alt/>Generovaná rastlinka uložená do HTML
 * súboru v SVG formáte.</image>Ukážka možného výstupu<br />(výsledný
 * HTML súbor si môžete pozrieť <a target="_blank"
 * href="resources/rastlinka-svg.html">tu</a>).</p>
 * 
 * <p>Táto trieda umožňuje aj veľmi primitívne spracovanie textových SVG
 * definícií. So zreteľom na široké možnosti štandardu SVG na prezentáciu
 * textových (znakových) informácií sú možnosti tejto triedy skutočne
 * primitívne. Ide v podstate len o prečítanie a zápis obsahu značky
 * {@code &lt;text&gt;} a o poskytnutie základných geometrických
 * transformácií.</p>
 * 
 * <p><b>Použité zdroje:</b></p>
 * 
 * <ul>
 * <li><small><a href="mailto:nikos.andronikos@cisra.canon.com.au"
 * target="_blank">Andronikos, Nikos (Canon, Inc.)</a> –
 * <a href="mailto:ratan@microsoft.com" target="_blank">Atanassov,
 * Rossen (Microsoft Co.)</a> – <a href="mailto:tavmjong@free.fr"
 * target="_blank">Bah, Tavmjong (Invited Expert)</a> –
 * <a href="mailto:amelia.bellamy.royds@gmail.com"
 * target="_blank">Bellamy-Royds, Amelia (Invited Expert)</a> –
 * <a href="mailto:bbirtles@mozilla.com" target="_blank">Birtles,
 * Brian (Mozilla Japan)</a> – <a href="mailto:bbrinza@microsoft.com"
 * target="_blank">Brinza, Bogdan (Microsoft Co.)</a> –
 * <a href="mailto:cyril.concolato@telecom-paristech.fr"
 * target="_blank">Concolato, Cyril (Telecom ParisTech)</a> –
 * <a href="mailto:erik@dahlström.net" target="_blank">Dahlström,
 * Erik (Invited Expert)</a> – <a href="mailto:chris@w3.org"
 * target="_blank">Lilley, Chris (W3C)</a> – <a
 * href="mailto:cam@mcc.id.au" target="_blank">McCormack, Cameron
 * (Mozilla Corporation)</a> – <a href="mailto:schepers@w3.org"
 * target="_blank">Schepers, Doug (W3C)</a> –
 * <a href="mailto:dschulze@adobe.com" target="_blank">Schulze, Dirk
 * (Adobe Systems)</a> – <a href="mailto:schwer@us.ibm.com"
 * target="_blank">Schwerdtfeger, Richard (IBM)</a> –
 * <a href="mailto:sa-takagi@kddi.com" target="_blank">Takagi, Satoru
 * (KDDI Corporation)</a> – <a href="mailto:jwatt@jwatt.org"
 * target="_blank">Watt, Jonathan (Mozilla Corporation)</a></small>:
 * <a href="https://svgwg.org/svg2-draft/toc.html"
 * target="_blank"><em>Scalable Vector Graphics (SVG) 2.</em> W3C
 * Editor’s Draft 10 July 2016. Copyright © 2016 W3C<sup>®</sup> (MIT,
 * ERCIM, Keio, Beihang). W3C liability, trademark and document use rules
 * apply. Citované: 2016 – 2018.</a></li>
 * 
 * <li><small><a href="mailto:nikos.andronikos@cisra.canon.com.au"
 * target="_blank">Andronikos, Nikos (Canon, Inc.)</a> –
 * <a href="mailto:ratan@microsoft.com" target="_blank">Atanassov,
 * Rossen (Microsoft Co.)</a> – <a href="mailto:tavmjong@free.fr"
 * target="_blank">Bah, Tavmjong (Invited Expert)</a> –
 * <a href="mailto:amelia.bellamy.royds@gmail.com"
 * target="_blank">Bellamy-Royds, Amelia (Invited Expert)</a> –
 * <a href="mailto:bbirtles@mozilla.com" target="_blank">Birtles,
 * Brian (Mozilla Japan)</a> – <a href="mailto:bbrinza@microsoft.com"
 * target="_blank">Brinza, Bogdan (Microsoft Co.)</a> –
 * <a href="mailto:cyril.concolato@telecom-paristech.fr"
 * target="_blank">Concolato, Cyril (Telecom ParisTech)</a> –
 * <a href="mailto:erik@dahlström.net" target="_blank">Dahlström,
 * Erik (Invited Expert)</a> – <a href="mailto:chris@w3.org"
 * target="_blank">Lilley, Chris (W3C)</a> –
 * <a href="mailto:cam@mcc.id.au" target="_blank">McCormack, Cameron
 * (Mozilla Corporation)</a> – <a href="mailto:schepers@w3.org"
 * target="_blank">Schepers, Doug (W3C)</a> –
 * <a href="mailto:dschulze@adobe.com" target="_blank">Schulze, Dirk
 * (Adobe Systems)</a> – <a href="mailto:schwer@us.ibm.com"
 * target="_blank">Schwerdtfeger, Richard (IBM)</a> –
 * <a href="mailto:sa-takagi@kddi.com" target="_blank">Takagi, Satoru
 * (KDDI Corporation)</a> – <a href="mailto:jwatt@jwatt.org"
 * target="_blank">Watt, Jonathan (Mozilla Corporation)</a></small>:
 * <a href="https://www.w3.org/TR/SVG2/" target="_blank"><em>Scalable
 * Vector Graphics (SVG) 2. W3C Working Draft 15 September 2015.</em>
 * Copyright © 2015 W3C<sup>®</sup> (MIT, ERCIM, Keio, Beihang). W3C
 * liability, trademark and document use rules apply. Citované: 2016 –
 * 2018.</a></li>
 * 
 * <li><a href="https://www.w3schools.com/graphics/svg_intro.asp"
 * target="_blank"><em>SVG Tutorial.</em></a> <a
 * href="https://www.w3schools.com/">W3Schools.com.</a></li>
 * <!-- Starý odkaz: http://www.w3schools.com/svg/ -->
 * 
 * <li><a href="http://planetmath.org/goniometricformulas"
 * target="_blank"><small>Buck, Warren</small>: <em>Goniometric
 * formulas.</em> Planetmath.org, 2007.</a></li>
 * 
 * <li><a href="http://mathworld.wolfram.com/Ellipse.html"
 * target="_blank"><small>Weisstein, Eric W.</small>: <em>Ellipse.</em>
 * Wolfram MathWorld.</a></li>
 * 
 * <li><small><a href="https://math.stackexchange.com/users/112135/dilma"
 * target="_blank">User:Dilma</a> –
 * <a href="https://math.stackexchange.com/users/111703/kevin"
 * target="_blank">User:Kevin</a></small>:
 * <a
 * href="https://math.stackexchange.com/questions/584911/how-to-calculate-the-tangent-angle-with-the-axis-of-an-ellipse"
 * target="_blank"><em>How to calculate the tangent angle with the axis of
 * an ellipse?</em> Mathematics Stack Exchange (calculus), 2013.</a></li>
 * 
 * <li><small>(Zdroje citované: 2016 – 2018.)</small></li>
 * </ul>
 */
public class SVGPodpora
{
	/**
	 * <p>Táto trieda slúži na uchovanie pôvodných informácií
	 * o transformáciách rozpoznávaných z textovej podoby podľa
	 * špecifikácie SVG. Je to v podstate medzistupeň prevodu medzi
	 * textovou reprezentáciou (v súlade so špecifikáciou SVG)
	 * a výslednou afinnou transformáciou (prevedenou do prislúchajúcej
	 * inštancie triedy {@link AffineTransform AffineTransform} jazyka
	 * Java).</p>
	 * 
	 * <p>Po prevedení do tvaru afinnej transformácie sa totiž môžu
	 * niektoré informácie stratiť (napríklad údaje o pôvodnom uhle
	 * rotácie v kombinácii so súradnicami stredu rotácie, ale hlavne
	 * údaje o jednotlivých zložkách konečnej zloženej transformácie
	 * tvaru) a je pritom vhodné, aby programátor mal k rozpoznaným
	 * informáciám prístup pred ich záverečným prevedením do tvaru
	 * výslednej zloženej transformácie.</p>
	 * 
	 * <p>Táto trieda slúži len na uchovanie 2D transformácií
	 * špecifikácie SVG, pretože triedy {@link SVGPodpora SVGPodpora}
	 * programovacieho rámca a {@link AffineTransform AffineTransform}
	 * Javy pracujú len s týmto typom transformácií.</p>
	 * 
	 * <p>Táto trieda rozpoznáva aj transformácie, ktoré nie sú
	 * podporované štandardom SVG 1.1, ale odvoláva sa na nich (cez
	 * <a href="https://www.w3.org/TR/css-transforms-1/"
	 * target="_blank">definíciu CSS</a>) <a
	 * href="https://svgwg.org/svg2-draft/coords.html#TransformProperty"
	 * target="_blank">draft štandardu SVG 2</a>. (Pri každom takomto
	 * type transformácie sa nachádza upozornenie.)
	 * Tento typ transformácie je pri prevádzaní transformácií na
	 * reťazec predvolene prevedený na iný (kompatibilný a štandardom 1.1
	 * podporovaný) typ transformácie. Na zmenu tohto správania je
	 * potrebné zmeniť predvolenú hodnotu {@code valfalse} statického
	 * príznaku {@link #SVG2 SVG2} na hodnotu {@code valtrue}.</p>
	 */
	public static class Transformácia
	{
		/**
		 * <p>Niektoré transformácie nie sú podporované štandardom SVG 1.1,
		 * preto je ich prevádzanie na reťazce predvolene nastavené tak,
		 * aby boli prevedené na iný kompatibilný typ transformácie. (Aby
		 * neboli prenášané do výstupného súboru.) Tento príznak slúži
		 * na zapnutie doslovného prevádzania týchto typov transformácií –
		 * zmenou predvolenej hodnoty {@code valfalse} na {@code valtrue}.</p>
		 */
		public static boolean SVG2 = false;

		/**
		 * <p>Určuje všeobecnú 2D transformáciu v tvare matice so šiestimi
		 * hodnotami (označme ich <em>a</em> – <em>f</em> ), ktoré sú počas
		 * výpočtov dosadené do matice afinnej 2D transformácie takto:</p>
		 * 
		 * <!-- table>
		 * <tr><td>⎡</td><td>a</td><td> </td><td>c</td>
		 * <td> </td><td>e</td><td>⎤</td></tr>
		 * <tr><td>⎢</td><td>b</td><td> </td><td>d</td>
		 * <td> </td><td>f</td><td>⎥</td></tr>
		 * <tr><td>⎣</td><td>0</td><td> </td><td>0</td>
		 * <td> </td><td>1</td><td>⎦</td></tr>
		 * </table -->
		 * 
		 * <p><img src="resources/matica-vseobecnej-transformacie.svg"
		 * alt="Matica všeobecnej transformácie." onerror="this.onerror=null;
		 * this.src='resources/matica-vseobecnej-transformacie.png';" /></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #MATICA MATICA}.</p>
		 */
		public final static int MATRIX = 0;

		/**
		 * <p>Preklad anglického identifikátora {@link #MATRIX MATRIX}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int MATICA = 0;

		/**
		 * <p>Určuje 2D posunutie v smere osí x a y.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #POSUN POSUN}.</p>
		 */
		public final static int TRANSLATE = 1;

		/**
		 * <p>Preklad anglického identifikátora {@link #TRANSLATE TRANSLATE}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int POSUN = 1;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN POSUN}.</p> */
		public final static int POSUŇ = 1;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN POSUN}.</p> */
		public final static int POSUNUTIE = 1;

		/**
		 * <p>Určuje 2D posunutie v smere osi x.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Táto transformácia
		 * nie je podporovaná štandardom SVG 1.1. Predvolená hodnota
		 * príznaku {@link #SVG2 SVG2} {@code valfalse} zabezpečuje,
		 * aby bola táto transformácia pri prevode na reťazec prevedená
		 * na kompatibilnú transformáciu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #POSUN_X POSUN_X}.</p>
		 */
		public final static int TRANSLATE_X = 2;

		/**
		 * <p>Preklad anglického identifikátora {@link #TRANSLATE_X
		 * TRANSLATE_X} do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int POSUN_X = 2;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN_X POSUN_X}.</p> */
		public final static int POSUŇ_X = 2;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN_X POSUN_X}.</p> */
		public final static int POSUNUTIE_X = 2;

		/**
		 * <p>Určuje 2D posunutie v smere osi y.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Táto transformácia
		 * nie je podporovaná štandardom SVG 1.1. Predvolená hodnota
		 * príznaku {@link #SVG2 SVG2} {@code valfalse} zabezpečuje,
		 * aby bola táto transformácia pri prevode na reťazec prevedená
		 * na kompatibilnú transformáciu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #POSUN_Y POSUN_Y}.</p>
		 */
		public final static int TRANSLATE_Y = 3;

		/**
		 * <p>Preklad anglického identifikátora {@link #TRANSLATE_Y
		 * TRANSLATE_Y} do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int POSUN_Y = 3;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN_Y POSUN_Y}.</p> */
		public final static int POSUŇ_Y = 3;

		/** <p><a class="alias"></a> Alias pre {@link #POSUN_Y POSUN_Y}.</p> */
		public final static int POSUNUTIE_Y = 3;

		/**
		 * <p>Určuje 2D zmenu mierky v smere osí x a y.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #MIERKA MIERKA}.</p>
		 */
		public final static int SCALE = 4;

		/**
		 * <p>Preklad anglického identifikátora {@link #SCALE SCALE}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int MIERKA = 4;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA MIERKA}.</p> */
		public final static int ZMENA_MIERKY = 4;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA MIERKA}.</p> */
		public final static int ZMEŇ_MIERKU = 4;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA MIERKA}.</p> */
		public final static int ZMEN_MIERKU = 4;

		/**
		 * <p>Určuje 2D zmenu mierky v smere osi x.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Táto transformácia
		 * nie je podporovaná štandardom SVG 1.1. Predvolená hodnota
		 * príznaku {@link #SVG2 SVG2} {@code valfalse} zabezpečuje,
		 * aby bola táto transformácia pri prevode na reťazec prevedená
		 * na kompatibilnú transformáciu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #MIERKA_X MIERKA_X}.</p>
		 */
		public final static int SCALE_X = 5;

		/**
		 * <p>Preklad anglického identifikátora {@link #SCALE_X SCALE_X}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int MIERKA_X = 5;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_X MIERKA_X}.</p> */
		public final static int ZMENA_MIERKY_X = 5;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_X MIERKA_X}.</p> */
		public final static int ZMEŇ_MIERKU_X = 5;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_X MIERKA_X}.</p> */
		public final static int ZMEN_MIERKU_X = 5;

		/**
		 * <p>Určuje 2D zmenu mierky v smere osi y.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Táto transformácia
		 * nie je podporovaná štandardom SVG 1.1. Predvolená hodnota
		 * príznaku {@link #SVG2 SVG2} {@code valfalse} zabezpečuje,
		 * aby bola táto transformácia pri prevode na reťazec prevedená
		 * na kompatibilnú transformáciu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #MIERKA_Y MIERKA_Y}.</p>
		 */
		public final static int SCALE_Y = 6;

		/**
		 * <p>Preklad anglického identifikátora {@link #SCALE_Y SCALE_Y}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int MIERKA_Y = 6;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_Y MIERKA_Y}.</p> */
		public final static int ZMENA_MIERKY_Y = 6;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_Y MIERKA_Y}.</p> */
		public final static int ZMEŇ_MIERKU_Y = 6;

		/** <p><a class="alias"></a> Alias pre {@link #MIERKA_Y MIERKA_Y}.</p> */
		public final static int ZMEN_MIERKU_Y = 6;

		/**
		 * <p>Určuje 2D rotáciu (pootočenie) o stanovený uhol okolo určeného
		 * bodu ako stredu otáčania.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #OTOČ OTOČ}.</p>
		 */
		public final static int ROTATE = 7;

		/**
		 * <p>Preklad anglického identifikátora {@link #ROTATE ROTATE}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int OTOČ = 7;

		/** <p><a class="alias"></a> Alias pre {@link #OTOČ OTOČ}.</p> */
		public final static int OTOC = 7;

		/** <p><a class="alias"></a> Alias pre {@link #OTOČ OTOČ}.</p> */
		public final static int OTOČENIE = 7;

		/** <p><a class="alias"></a> Alias pre {@link #OTOČ OTOČ}.</p> */
		public final static int OTOCENIE = 7;

		/**
		 * <p>Určuje zošikmenie/sklonenie (čo nie je to isté ako skosenie –
		 * angl. shear) o zadaný uhol v smere osí x a y.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Táto transformácia
		 * nie je podporovaná štandardom SVG 1.1. Predvolená hodnota
		 * príznaku {@link #SVG2 SVG2} {@code valfalse} zabezpečuje,
		 * aby bola táto transformácia pri prevode na reťazec prevedená
		 * na kompatibilnú transformáciu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #SKLON SKLON}.</p>
		 */
		public final static int SKEW = 8;

		/**
		 * <p>Preklad anglického identifikátora {@link #SKEW SKEW}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int SKLON = 8;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON SKLON}.</p> */
		public final static int SKLOŇ = 8;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON SKLON}.</p> */
		public final static int SKOLONENIE = 8;

		/**
		 * <p>Určuje sklonenie o zadaný uhol v smere osi x.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #SKLON_X SKLON_X}.</p>
		 */
		public final static int SKEW_X = 9;

		/**
		 * <p>Preklad anglického identifikátora {@link #SKEW_X SKEW_X}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int SKLON_X = 9;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON_X SKLON_X}.</p> */
		public final static int SKLOŇ_X = 9;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON_X SKLON_X}.</p> */
		public final static int SKOLONENIE_X = 9;

		/**
		 * <p>Určuje sklonenie o zadaný uhol v smere osi y.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Slovenským prekladom tohto identifikátora je
		 * {@link #SKLON_Y SKLON_Y}.</p>
		 */
		public final static int SKEW_Y = 10;

		/**
		 * <p>Preklad anglického identifikátora {@link #SKEW_Y SKEW_Y}
		 * do slovenského jazyka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Všetky identifikátory
		 * transformácií boli preložené do slovenského jazyka a zároveň
		 * ponechané v anglickom jazyku, aby vzniklo terminologické
		 * prepojenie s definíciami štandardu SVG, ktorý je prirodzene
		 * anglický. Vysvetlenie významu jednotlivých transformácií je vždy
		 * uvedené pri anglickej verzii jej identifikátora.</p>
		 */
		public final static int SKLON_Y = 10;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON_Y SKLON_Y}.</p> */
		public final static int SKLOŇ_Y = 10;

		/** <p><a class="alias"></a> Alias pre {@link #SKLON_Y SKLON_Y}.</p> */
		public final static int SKOLONENIE_Y = 10;


		/**
		 * <p>Uchováva informáciu o type trasformácie uloženej v tejto
		 * inštancii. Pozri: {@link #MATRIX MATRIX}, {@link #TRANSLATE
		 * TRANSLATE}, {@link #TRANSLATE_X TRANSLATE_X}, {@link 
		 * #TRANSLATE_Y TRANSLATE_Y}, {@link #SCALE SCALE}, {@link 
		 * #SCALE_X SCALE_X}, {@link #SCALE_Y SCALE_Y}, {@link 
		 * #ROTATE ROTATE}, {@link #SKEW SKEW}, {@link #SKEW_X SKEW_X},
		 * {@link #SKEW_Y SKEW_Y}.</p>
		 */
		public final int typ;

		/**
		 * <p>Uchováva informácie o hodnotách konkrétnych parametrov
		 * podľa typu transformácie. Konkrétne transformácie majú
		 * stanovený určitý počet parametrov. Pozri: {@link #MATRIX
		 * MATRIX}, {@link #TRANSLATE TRANSLATE}, {@link #TRANSLATE_X
		 * TRANSLATE_X}, {@link #TRANSLATE_Y TRANSLATE_Y}, {@link #SCALE
		 * SCALE}, {@link #SCALE_X SCALE_X}, {@link #SCALE_Y SCALE_Y},
		 * {@link #ROTATE ROTATE}, {@link #SKEW SKEW}, {@link 
		 * #SKEW_X SKEW_X}, {@link #SKEW_Y SKEW_Y}.</p>
		 */
		public final double[] hodnota;

		/**
		 * <p>Konštruktor zabezpečujúci bezchybnú inicializáciu inštancie.
		 * Ak je typ mimo rozsahu povolených hodnôt, tak je automaticky
		 * nastavený na hodnotu {@link #MATRIX MATRIX}. Ak vo vstupných
		 * údajoch (v sérii parametrov {@code hodnoty}) niektorý
		 * parameter trasformácie chýba, je jeho hodnota automaticky
		 * nastavená podľa typu transformácie na predvolenú hodnotu
		 * podľa nasledujúcich pravidiel:</p>
		 * 
		 * <ol>
		 * <li>ak je transformácia typu {@link #SCALE SCALE} a chýba
		 * druhý parameter, tak je jeho hodnota nastavená podľa prvého
		 * parametra,</li>
		 * <li>ak je transformácia typu {@link #SCALE SCALE} a chýba aj
		 * prvý parameter, tak je jeho hodnota nastavená na {@code num1.0}
		 * (a tá je následne skopírovaná do druhého parametra),</li>
		 * <li>ak je transformácia typu {@link #SCALE_X SCALE_X} alebo
		 * {@link #SCALE_Y SCALE_Y}, tak je chýbajúcemu parametru
		 * (transformácia vyžaduje len jeden) nastavená hodnota
		 * {@code num1.0},</li>
		 * <li>vo všetkých ostatných prípadoch je namiesto každého
		 * chýbajúceho parametra doplnená hodnota {@code num0.0}.</li>
		 * </ol>
		 * 
		 * @param typ určuje typ rovinnej transformácie; ak zadaná hodnota
		 *     nie je v množine povolených hodnôt: {@link #MATRIX MATRIX},
		 *     {@link #TRANSLATE TRANSLATE}, {@link #TRANSLATE_X
		 *     TRANSLATE_X}, {@link #TRANSLATE_Y TRANSLATE_Y}, {@link 
		 *     #SCALE SCALE}, {@link #SCALE_X SCALE_X}, {@link #SCALE_Y
		 *     SCALE_Y}, {@link #ROTATE ROTATE}, {@link #SKEW SKEW},
		 *     {@link #SKEW_X SKEW_X}, {@link #SKEW_Y SKEW_Y}, tak je
		 *     automaticky doplnená hodnota {@link #MATRIX MATRIX}
		 * @param hodnoty séria argumentov určujúcich hodnoty parametrov
		 *     konkrétnej transformácie
		 */
		public Transformácia(int typ, Double... hodnoty)
		{
			this.typ = (typ >= MATRIX && typ <= SKEW_Y) ? typ : MATRIX;

			switch (typ)
			{
			default:
				this.hodnota = new double[6];

				if (hodnoty.length >= 6 && null != hodnoty[5])
					this.hodnota[5] = hodnoty[5];
				else
					this.hodnota[5] = 0.0;

				if (hodnoty.length >= 5 && null != hodnoty[4])
					this.hodnota[4] = hodnoty[4];
				else
					this.hodnota[4] = 0.0;

				if (hodnoty.length >= 4 && null != hodnoty[3])
					this.hodnota[3] = hodnoty[3];
				else
					this.hodnota[3] = 0.0;

				if (hodnoty.length >= 3 && null != hodnoty[2])
					this.hodnota[2] = hodnoty[2];
				else
					this.hodnota[2] = 0.0;

				if (hodnoty.length >= 2 && null != hodnoty[1])
					this.hodnota[1] = hodnoty[1];
				else
					this.hodnota[1] = 0.0;

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case TRANSLATE:
				this.hodnota = new double[2];

				if (hodnoty.length >= 2 && null != hodnoty[1])
					this.hodnota[1] = hodnoty[1];
				else
					this.hodnota[1] = 0.0;

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case TRANSLATE_X:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case TRANSLATE_Y:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case SCALE:
				this.hodnota = new double[2];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 1.0;

				if (hodnoty.length >= 2 && null != hodnoty[1])
					this.hodnota[1] = hodnoty[1];
				else
					this.hodnota[1] = this.hodnota[0];
				break;

			case SCALE_X:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 1.0;
				break;

			case SCALE_Y:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 1.0;
				break;

			case ROTATE:
				this.hodnota = new double[3];

				if (hodnoty.length >= 3 && null != hodnoty[2])
					this.hodnota[2] = hodnoty[2];
				else
					this.hodnota[2] = 0.0;

				if (hodnoty.length >= 2 && null != hodnoty[1])
					this.hodnota[1] = hodnoty[1];
				else
					this.hodnota[1] = 0.0;

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case SKEW:
				this.hodnota = new double[2];

				if (hodnoty.length >= 2 && null != hodnoty[1])
					this.hodnota[1] = hodnoty[1];
				else
					this.hodnota[1] = 0.0;

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case SKEW_X:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;

			case SKEW_Y:
				this.hodnota = new double[1];

				if (hodnoty.length >= 1 && null != hodnoty[0])
					this.hodnota[0] = hodnoty[0];
				else
					this.hodnota[0] = 0.0;
				break;
			}
		}

		/**
		 * <p>Vráti objekt typu {@link AffineTransform AffineTransform}
		 * vyrobený podľa údajov o 2D SVG transformácii uložených v tejto
		 * inštancii.</p>
		 * 
		 * @return nová inštancia triedy {@link AffineTransform
		 *     AffineTransform}
		 */
		public AffineTransform daj()
		{
			switch (typ)
			{
			default: return new AffineTransform(hodnota);

			case TRANSLATE: return AffineTransform.
				getTranslateInstance(hodnota[0], hodnota[1]);

			case TRANSLATE_X: return AffineTransform.
				getTranslateInstance(hodnota[0], 0.0);

			case TRANSLATE_Y: return AffineTransform.
				getTranslateInstance(0.0, hodnota[0]);

			case SCALE: return AffineTransform.
				getScaleInstance(hodnota[0], hodnota[1]);

			case SCALE_X: return AffineTransform.
				getScaleInstance(hodnota[0], 1.0);

			case SCALE_Y: return AffineTransform.
				getScaleInstance(1.0, hodnota[0]);

			case ROTATE: return AffineTransform.getRotateInstance(
				Math.toRadians(hodnota[0]), hodnota[1], hodnota[2]);

			case SKEW: return AffineTransform.getShearInstance(
				Math.tan(Math.toRadians(hodnota[0])),
				Math.tan(Math.toRadians(hodnota[1])));

			case SKEW_X: return AffineTransform.getShearInstance(
				Math.tan(Math.toRadians(hodnota[0])), 0.0);

			case SKEW_Y: return AffineTransform.getShearInstance(
				0.0, Math.tan(Math.toRadians(hodnota[0])));
			}
		}

		/**
		 * <p>Prevedie túto inštanciu na reťazec – do textovej podoby
		 * v súlade so špecifikáciou SVG. Metóda sa pokúša nájsť
		 * najkratšiu reťazcovú reprezentáciu podľa údajov uložených
		 * v tejto inštancii.</p>
		 * 
		 * @return prevedený reťazec obsahujúci text definície 2D SVG
		 *     transformácie
		 */
		@Override public String toString()
		{
			// (Pozor‼) Ani prehliadač, ani Inkscape v transformáciách
			// vôbec neakceptujú jednotky (uhlové „deg,“ ani rozmerové
			// „px“)…

			// Na to som prišiel pri testovaní novej metódy:
			// #pridajText(String, GRobot, String[])

			switch (typ)
			{
			default: return "matrix(" +
				hodnota[0] + ", " + hodnota[1] + ", " +
				hodnota[2] + ", " + hodnota[3] + ", " +
				hodnota[4] + ", " + hodnota[5] + ")";

			case TRANSLATE: return "translate(" +
				hodnota[0] + ", " + hodnota[1] + ")"; // px

			case TRANSLATE_X:
				if (SVG2) return "translateX(" + hodnota[0] + ")"; // px
				return "translate(" + hodnota[0] + ", " + // px
					"0)"; // px

			case TRANSLATE_Y:
				if (SVG2) return "translateY(" + hodnota[0] + ")"; // px
				return "translate(0, " + // px
					hodnota[0] + ")"; // px

			case SCALE:
				if (hodnota[0] == hodnota[1])
					return "scale(" + hodnota[0] + ")";
				return "scale(" + hodnota[0] +
					", " + hodnota[1] + ")";

			case SCALE_X:
				if (SVG2) return "scaleX(" + hodnota[0] + ")";
				return "scale(" + hodnota[0] + ", 1)";

			case SCALE_Y:
				if (SVG2) return "scaleY(" + hodnota[0] + ")";
				return "scale(1, " + hodnota[0] + ")";

			case ROTATE:
				if (0 == hodnota[1] && 0 == hodnota[2])
					return "rotate(" + hodnota[0] + ")"; // deg
				return "rotate(" + hodnota[0] + ", " + // deg
					hodnota[1] + ", " + // px
					hodnota[2] + ")"; // px

			case SKEW:
				if (SVG2) return "skew(" + hodnota[0] + ", " + // deg
					hodnota[1] + ")"; // deg
				return "matrix(1, " + Math.tan(Math.toRadians(
					hodnota[1])) + ", " + Math.tan(Math.toRadians(
					hodnota[0])) + ", 1, 0, 0)";

			case SKEW_X: return "skewX(" + hodnota[0] + ")"; // deg

			case SKEW_Y: return "skewY(" + hodnota[0] + ")"; // deg
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link Transformácia Transformácia}.</p> */
	public static class Transformacia extends Transformácia
	{
		private static Double[] preveďArgumenty(double[] hodnoty)
		{
			int dĺžka = hodnoty.length;
			Double[] hodnoty2 = new Double[dĺžka];
			for (int i = 0; i < dĺžka; ++i) hodnoty2[i] = hodnoty[i];
			return hodnoty2;
		}

		public Transformacia(Transformácia transformácia)
		{ super(transformácia.typ, preveďArgumenty(
			transformácia.hodnota)); }

		public Transformacia(int typ, Double... hodnoty)
		{ super(typ, hodnoty); }
	}


	// Toto je pomocná metóda, ktorá pridáva rozpoznané transformácie
	// zo zdrojového reťazca do cieľového vektora.
	private void pridajTransformácie(
		Vector<Transformácia> vektor, String zdroj)
	{
		int index1 = 0, index2, length = zdroj.length();
		while (-1 != (index2 = zdroj.indexOf(")", index1)))
		{
			Transformácia transformácia = reťazecNaTransformáciu(
				zdroj.substring(index1, 1 + index2));
			if (null != transformácia) vektor.add(transformácia);
			index1 = 1 + index2;
		}
	}

	// Toto pole je použité vždy, keď sa počas vykonávania nasledujúcej
	// metódy nepodarí rozpoznať žiadnu transformáciu
	private final static Transformácia[] žiadneTransformácie =
		new Transformácia[0];

	// Toto pole je použité vždy, keď sa počas vykonávania metódy
	// reťazecNaTransformáciu nepodarí rozpoznať žiadnu číselnú hodnotu
	private final static Double[] žiadneHodnoty = new Double[0];


	// Táto metóda slúži na centralizáciu prevodov podľa zásady:
	//   „Naprogramuj raz, používaj viac ráz.“
	// 
	// Používajú ju napríklad metódy:
	//   — reťazceNaTransformácie(String...)
	//   — a dajVýsledný(int, String, String...)
	// definované nižšie.
	// 
	private Transformácia[] reťazceNaTransformácie(
		String text, String[] texty)
	{
		if (null == text && null == texty) return null;

		Vector<Transformácia> vektor = new Vector<>();
		if (null != text) pridajTransformácie(vektor, text);

		if (null != texty) for (String t : texty)
			if (null != t) pridajTransformácie(vektor, t);

		return vektor.toArray(žiadneTransformácie);
	}

	/**
	 * <p>Táto metóda očakáva reťazec s jedinou 2D transformáciou, ktorý
	 * prevedie na objekt typu {@link Transformácia Transformácia}.
	 * Volajúca metóda musí zabezpečiť, aby bola táto podmienka
	 * dodržaná – je na jej zodpovednosti, aby napríklad rozdelila
	 * prípadnú sériu 2D transformácií, z ktorých sa môže skladať atribút
	 * ‚transform‘ špecifikácie SVG na jednotlivé celky. (Toto obmedzenie
	 * je dôležité v súvislosti s typom návratovej hodnoty – metóda
	 * dokáže poskytnúť informácie o jedinej rozpoznanej transformácii –
	 * vracia jediný objekt typu {@link Transformácia Transformácia}
	 * alebo {@code valnull} v prípade neúspechu.)</p>
	 * 
	 * @param text reťazec obsahujúci text definície jedinej
	 *     2D transformácie, ktorý má byť prevedený na objekt typu
	 *     {@link Transformácia Transformácia}
	 * @return prevedený objekt typu {@link Transformácia Transformácia}
	 *     alebo {@code valnull}, ak sa prevod nepodarí
	 */
	public Transformácia reťazecNaTransformáciu(String text)
	{
		if (null == text) return null;
		text = text.trim();

		int indexOf = text.length() - 1;
		if (indexOf <= 1) return null;

		if (')' != text.charAt(indexOf)) return null;
		text = text.substring(0, indexOf);

		if (-1 == (indexOf = text.indexOf("("))) return null;

		String meno = text.substring(0, indexOf).trim(); int typ;
		if (meno.equalsIgnoreCase("translate"))
			typ = Transformácia.TRANSLATE;
		else if (meno.equalsIgnoreCase("translateX"))
			typ = Transformácia.TRANSLATE_X;
		else if (meno.equalsIgnoreCase("translateY"))
			typ = Transformácia.TRANSLATE_Y;
		else if (meno.equalsIgnoreCase("scale"))
			typ = Transformácia.SCALE;
		else if (meno.equalsIgnoreCase("scaleX"))
			typ = Transformácia.SCALE_X;
		else if (meno.equalsIgnoreCase("scaleY"))
			typ = Transformácia.SCALE_Y;
		else if (meno.equalsIgnoreCase("rotate"))
			typ = Transformácia.ROTATE;
		else if (meno.equalsIgnoreCase("skew"))
			typ = Transformácia.SKEW;
		else if (meno.equalsIgnoreCase("skewX"))
			typ = Transformácia.SKEW_X;
		else if (meno.equalsIgnoreCase("skewY"))
			typ = Transformácia.SKEW_Y;
		else typ = Transformácia.MATRIX;

		ÚdajeTvaru čítaj = new ÚdajeTvaru(
			text.substring(indexOf + 1).trim());

		Vector<Double> hodnoty = new Vector<>();

		while (čítaj.nieJeKoniec())
		{
			double hodnota = čítaj.ďalšiíÚdaj();
			if (!Double.isNaN(hodnota)) hodnoty.add(hodnota);
		}

		return new Transformácia(typ, hodnoty.toArray(žiadneHodnoty));
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaTransformáciu(String) reťazecNaTransformáciu}.</p> */
	public Transformacia retazecNaTransformaciu(String text)
	{ return new Transformacia(reťazecNaTransformáciu(text)); }

	/**
	 * <p>Táto metóda slúži na prevod 2D transformácií zadaných
	 * v textovej podobe v tvare, ktorý je v súlade so špecifikáciou
	 * SVG. Metóda dokáže spracovať ľubovoľný počet reťazcov
	 * obsahujúcich ľubovoľný počet SVG definícií 2D transformácií.
	 * Všetky rozpoznané definície prevedie na objekty typu {@link 
	 * Transformácia Transformácia}, uloží ich do poľa, ktoré vráti
	 * v návratovej hodnote.</p>
	 * 
	 * @param texty reťazce obsahujúce text definícií 2D SVG
	 *     transformácií, ktorý majú byť prevedené na objekty typu
	 *     {@link Transformácia Transformácia}
	 * @return pole s dĺžkou rovnou počtu transformácií, ktoré sa
	 *     podarilo rozpoznať; každý prvok poľa obsahuje údaje o jednej
	 *     rozpoznanej transformácii uložené v objekte typu {@link 
	 *     Transformácia Transformácia}; pole môže mať aj nulovú dĺžku
	 *     (ak by sa nepodarilo rozpoznať ani jednu transformáciu);
	 *     v prípade kritického zlyhania môže metóda vrátiť hodnotu
	 *     {@code valnull}
	 */
	public Transformácia[] reťazceNaTransformácie(String... texty)
	{ return reťazceNaTransformácie(null, texty); }

	/** <p><a class="alias"></a> Alias pre {@link #reťazceNaTransformácie(String[]) reťazceNaTransformácie}.</p> */
	public Transformacia[] retazceNaTransformacie(String... texty)
	{
		Transformácia[] transformácie = reťazceNaTransformácie(texty);
		if (null == transformácie) return null;
		int dĺžka = transformácie.length;
		Transformacia[] transformacie = new Transformacia[dĺžka];
		for (int i = 0; i < dĺžka; ++i)
			transformacie[i] = new Transformacia(transformácie[i]);
		return transformacie;
	}

	/**
	 * <p>Táto metóda prevedie objekte typu {@link AffineTransform
	 * AffineTransform} do textovej podoby v súlade so špecifikáciou SVG.
	 * Vo viacerých prípadoch je tento prevod jednoducho prepísaním
	 * údajov o afinnej transformácii na maticu {@code matrix(…)}.
	 * Presnejší prevod je možný z údajov uložených v objektoch typu
	 * {@link Transformácia Transformácia}. (Na spôsob výstupu tejto
	 * metódy má vplyv aj hodnota príznaku {@link Transformácia#SVG2
	 * Transformácia.SVG2}.)</p>
	 * 
	 * @param transformácia objekt typu {@link AffineTransform
	 *     AffineTransform}, ktorý má byť prevedený na reťazec
	 * @return prevedený reťazec obsahujúci text definície 2D SVG
	 *     transformácie
	 */
	public String transformáciuNaReťazec(AffineTransform transformácia)
	{
		double[] hodnota = new double[6];
		transformácia.getMatrix(hodnota);

		if (0 == hodnota[1] && 0 == hodnota[2])
		{
			if (1 == hodnota[0] && 1 == hodnota[3])
			{
				if (Transformácia.SVG2)
				{
					if (0 == hodnota[5])
						return "translateX(" + hodnota[4] + ")"; // px
					if (0 == hodnota[4])
						return "translateY(" + hodnota[5] + ")"; // px
				}
				return "translate(" + hodnota[4] + ", " + // px
					hodnota[5] + ")"; // px
			}

			if (0 == hodnota[4] && 0 == hodnota[5])
			{
				if (hodnota[0] == hodnota[3])
					return "scale(" + hodnota[0] + ")";
				if (Transformácia.SVG2)
				{
					if (1 == hodnota[3])
						return "scaleX(" + hodnota[0] + ")";
					if (1 == hodnota[0])
						return "scaleY(" + hodnota[3] + ")";
				}
				return "scale(" + hodnota[0] + ", " + hodnota[3] +")";
			}
		}
		else if (1 == hodnota[1] && 1 == hodnota[2] &&
			0 == hodnota[4] && 0 == hodnota[5])
		{
			// (Pozor‼) Ani prehliadač, ani Inkscape v transformáciách
			// vôbec neakceptujú jednotky (uhlové „deg,“ ani rozmerové
			// „px“)…
			if (0 == hodnota[3])
				return "skewX(" + Math.toDegrees(
					Math.atan(hodnota[0])) + ")"; // deg
			if (0 == hodnota[0])
				return "skewY(" + Math.toDegrees(
					Math.atan(hodnota[3])) + ")"; // deg
			if (Transformácia.SVG2)
				return "skew(" + Math.toDegrees(Math.atan(hodnota[0])) + // deg
					", " + Math.toDegrees(Math.atan(hodnota[3])) + ")"; // deg
		}

		return "matrix(" +
			hodnota[0] + ", " + hodnota[1] + ", " +
			hodnota[2] + ", " + hodnota[3] + ", " +
			hodnota[4] + ", " + hodnota[5] + ")";
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformáciuNaReťazec(AffineTransform) transformáciuNaReťazec}.</p> */
	public String transformaciuNaRetazec(AffineTransform transformácia)
	{ return transformáciuNaReťazec(transformácia); }

	/**
	 * <p>Táto metóda je doplnkom k metóde {@link 
	 * #reťazecNaTransformáciu(String) reťazecNaTransformáciu},
	 * prevádza informácie uložené v objekte typu {@link Transformácia
	 * Transformácia} do textovej podoby v súlade so špecifikáciou SVG.
	 * (Na spôsob výstupu tejto metódy má vplyv aj hodnota príznaku
	 * {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param transformácia objekt typu {@link Transformácia
	 *     Transformácia}, ktorý má byť prevedený na reťazec
	 * @return prevedený reťazec obsahujúci text definície 2D SVG
	 *     transformácie
	 */
	public String transformáciuNaReťazec(Transformácia transformácia)
	{
		if (null == transformácia) return null;
		return transformácia.toString();
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformáciuNaReťazec(Transformácia) transformáciuNaReťazec}.</p> */
	public String transformaciuNaRetazec(Transformácia transformácia)
	{ return transformáciuNaReťazec(transformácia); }

	/**
	 * <p>Táto metóda slúži na prevod objektov typu {@link AffineTransform
	 * AffineTransform} do textovej podoby (tak, aby bola v súlade so
	 * špecifikáciou SVG). Vo viacerých prípadoch je tento prevod
	 * jednoducho prepísaním údajov o afinnej transformácii na maticu
	 * {@code matrix(…)}. Presnejší prevod je možný z údajov uložených
	 * v objektoch typu {@link Transformácia Transformácia}. (Na spôsob
	 * výstupu tejto metódy má vplyv aj hodnota príznaku {@link 
	 * Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param transformácie objekty typu {@link AffineTransform
	 *     AffineTransform}, ktoré majú byť prevedené do podoby série
	 *     textových definícií uložených (podobne ako pri metóde {@link 
	 *     #transformácieNaReťazec(AffineTransform[])
	 *     transformácieNaReťazec} prijímajúcej objekty typu {@link 
	 *     Transformácia Transformácia})
	 * @return prevedený reťazec obsahujúci textové definície všetkých
	 *     zadaných afinných transformácií
	 */
	public String transformácieNaReťazec(AffineTransform... transformácie)
	{
		if (null == transformácie) return null;

		// Reťazcový zásobník na spájanie prevedených argumentov
		StringBuffer spojené = new StringBuffer();

		for (AffineTransform jedna : transformácie)
			if (null != jedna)
			{
				spojené.append(' ');
				spojené.append(transformáciuNaReťazec(jedna));
			}

		if (0 == spojené.length()) return "";
		return spojené.substring(1);
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácieNaReťazec(AffineTransform[]) transformácieNaReťazec}.</p> */
	public String transformacieNaRetazec(AffineTransform... transformácie)
	{ return transformácieNaReťazec(transformácie); }

	/**
	 * <p>Táto metóda slúži na prevod informácií uložených v ľubovoľnom
	 * počte objektov typu {@link Transformácia Transformácia} do
	 * textovej podoby, ktorá je v súlade so špecifikáciou SVG. (Na
	 * spôsob výstupu tejto metódy má vplyv aj hodnota príznaku {@link 
	 * Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param transformácie objekty typu {@link Transformácia
	 *     Transformácia}, ktoré majú byť prevedené do celistvej
	 *     podoby série textových definícií uložených v rámci jediného
	 *     reťazca
	 * @return prevedený reťazec obsahujúci textové definície všetkých
	 *     zadaných transformácií
	 */
	public String transformácieNaReťazec(Transformácia... transformácie)
	{
		if (null == transformácie) return null;

		// Reťazcový zásobník na spájanie prevedených argumentov
		StringBuffer spojené = new StringBuffer();

		for (Transformácia jedna : transformácie)
			if (null != jedna)
			{
				spojené.append(' ');
				spojené.append(jedna.toString());
			}

		if (0 == spojené.length()) return "";
		return spojené.substring(1);
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácieNaReťazec(Transformácia[]) transformácieNaReťazec}.</p> */
	public String transformacieNaRetazec(Transformácia... transformácie)
	{ return transformácieNaReťazec(transformácie); }


	// Mapa definícií HTML farieb
	private final static TreeMap<String, Farba> mapaFarieb = new TreeMap<>();

	// Metóda slúžiaca na naplnenie mapy definícií HTML farieb
	private static void naplňMapuFarieb()
	{
		mapaFarieb.put("aliceblue", new Farba(240, 248, 255));
		mapaFarieb.put("amethyst", new Farba(153, 102, 204));
		mapaFarieb.put("antiquewhite", new Farba(250, 235, 215));
		mapaFarieb.put("aqua", new Farba(0, 255, 255));
		mapaFarieb.put("aquamarine", new Farba(127, 255, 212));
		mapaFarieb.put("azure", new Farba(240, 255, 255));
		mapaFarieb.put("beige", new Farba(245, 245, 220));
		mapaFarieb.put("bisque", new Farba(255, 228, 196));
		mapaFarieb.put("black", new Farba(0, 0, 0));
		mapaFarieb.put("blanchedalmond", new Farba(255, 235, 205));
		mapaFarieb.put("blue", new Farba(0, 0, 255));
		mapaFarieb.put("blueviolet", new Farba(138, 43, 226));
		mapaFarieb.put("brown", new Farba(165, 42, 42));
		mapaFarieb.put("burlywood", new Farba(222, 184, 135));
		mapaFarieb.put("cadetblue", new Farba(95, 158, 160));
		mapaFarieb.put("coral", new Farba(255, 127, 80));
		mapaFarieb.put("cornflowerblue", new Farba(100, 149, 237));
		mapaFarieb.put("cornsilk", new Farba(255, 248, 220));
		mapaFarieb.put("crimson", new Farba(220, 20, 60));
		mapaFarieb.put("cyan", new Farba(0, 255, 255));
		mapaFarieb.put("darkblue", new Farba(0, 0, 139));
		mapaFarieb.put("darkcyan", new Farba(0, 139, 139));
		mapaFarieb.put("darkgoldenrod", new Farba(184, 134, 11));
		mapaFarieb.put("darkgray", new Farba(169, 169, 169));
		mapaFarieb.put("darkgreen", new Farba(0, 100, 0));
		mapaFarieb.put("darkgrey", new Farba(169, 169, 169));
		mapaFarieb.put("darkkhaki", new Farba(189, 183, 107));
		mapaFarieb.put("darkmagenta", new Farba(139, 0, 139));
		mapaFarieb.put("darkolivegreen", new Farba(85, 107, 47));
		mapaFarieb.put("darkorange", new Farba(255, 140, 0));
		mapaFarieb.put("darkorchid", new Farba(153, 50, 204));
		mapaFarieb.put("darkred", new Farba(139, 0, 0));
		mapaFarieb.put("darksalmon", new Farba(233, 150, 122));
		mapaFarieb.put("darkseagreen", new Farba(143, 188, 143));
		mapaFarieb.put("darkslateblue", new Farba(72, 61, 139));
		mapaFarieb.put("darkslategray", new Farba(47, 79, 79));
		mapaFarieb.put("darkslategrey", new Farba(47, 79, 79));
		mapaFarieb.put("darkturquoise", new Farba(0, 206, 209));
		mapaFarieb.put("darkviolet", new Farba(148, 0, 211));
		mapaFarieb.put("deeppink", new Farba(255, 20, 147));
		mapaFarieb.put("deepskyblue", new Farba(0, 191, 255));
		mapaFarieb.put("dimgrey", new Farba(105, 105, 105));
		mapaFarieb.put("dodgerblue", new Farba(30, 144, 255));
		mapaFarieb.put("firebrick", new Farba(178, 34, 34));
		mapaFarieb.put("floralwhite", new Farba(255, 250, 240));
		mapaFarieb.put("forestgreen", new Farba(34, 139, 34));
		mapaFarieb.put("fuchsia", new Farba(255, 0, 255));
		mapaFarieb.put("gainsboro", new Farba(220, 220, 220));
		mapaFarieb.put("ghostwhite", new Farba(248, 248, 255));
		mapaFarieb.put("gold", new Farba(255, 215, 0));
		mapaFarieb.put("goldenrod", new Farba(218, 165, 32));
		mapaFarieb.put("gray", new Farba(128, 128, 128));
		mapaFarieb.put("green", new Farba(0, 128, 0));
		mapaFarieb.put("greenyellow", new Farba(173, 255, 47));
		mapaFarieb.put("grey", new Farba(128, 128, 128));
		mapaFarieb.put("honeydew", new Farba(240, 255, 240));
		mapaFarieb.put("hotpink", new Farba(255, 105, 180));
		mapaFarieb.put("chartreuse", new Farba(127, 255, 0));
		mapaFarieb.put("chocolate", new Farba(210, 105, 30));
		mapaFarieb.put("indianred ", new Farba(205, 92, 92));
		mapaFarieb.put("indigo", new Farba(75, 0, 130));
		mapaFarieb.put("ivory", new Farba(255, 255, 240));
		mapaFarieb.put("khaki", new Farba(240, 230, 140));
		mapaFarieb.put("lavender", new Farba(230, 230, 250));
		mapaFarieb.put("lavenderblush", new Farba(255, 240, 245));
		mapaFarieb.put("lawngreen", new Farba(124, 252, 0));
		mapaFarieb.put("lemonchiffon", new Farba(255, 250, 205));
		mapaFarieb.put("lightblue", new Farba(173, 216, 230));
		mapaFarieb.put("lightcoral", new Farba(240, 128, 128));
		mapaFarieb.put("lightcyan", new Farba(224, 255, 255));
		mapaFarieb.put("lightgoldenrodyellow", new Farba(250, 250, 210));
		mapaFarieb.put("lightgray", new Farba(211, 211, 211));
		mapaFarieb.put("lightgreen", new Farba(144, 238, 144));
		mapaFarieb.put("lightgrey", new Farba(211, 211, 211));
		mapaFarieb.put("lightpink", new Farba(255, 182, 193));
		mapaFarieb.put("lightsalmon", new Farba(255, 160, 122));
		mapaFarieb.put("lightseagreen", new Farba(32, 178, 170));
		mapaFarieb.put("lightskyblue", new Farba(135, 206, 250));
		mapaFarieb.put("lightslategray", new Farba(119, 136, 153));
		mapaFarieb.put("lightslategrey", new Farba(119, 136, 153));
		mapaFarieb.put("lightsteelblue", new Farba(176, 196, 222));
		mapaFarieb.put("lightyellow", new Farba(255, 255, 224));
		mapaFarieb.put("lime", new Farba(0, 255, 0));
		mapaFarieb.put("limegreen", new Farba(50, 205, 50));
		mapaFarieb.put("linen", new Farba(250, 240, 230));
		mapaFarieb.put("magenta", new Farba(255, 0, 255));
		mapaFarieb.put("maroon", new Farba(128, 0, 0));
		mapaFarieb.put("mediumaquamarine", new Farba(102, 205, 170));
		mapaFarieb.put("mediumblue", new Farba(0, 0, 205));
		mapaFarieb.put("mediumorchid", new Farba(186, 85, 211));
		mapaFarieb.put("mediumpurple", new Farba(147, 112, 219));
		mapaFarieb.put("mediumseagreen", new Farba(60, 179, 113));
		mapaFarieb.put("mediumslateblue", new Farba(123, 104, 238));
		mapaFarieb.put("mediumspringgreen", new Farba(0, 250, 154));
		mapaFarieb.put("mediumturquoise", new Farba(72, 209, 204));
		mapaFarieb.put("mediumvioletred", new Farba(199, 21, 133));
		mapaFarieb.put("midnightblue", new Farba(25, 25, 112));
		mapaFarieb.put("mintcream", new Farba(245, 255, 250));
		mapaFarieb.put("mistyrose", new Farba(255, 228, 225));
		mapaFarieb.put("moccasin", new Farba(255, 228, 181));
		mapaFarieb.put("navajowhite", new Farba(255, 222, 173));
		mapaFarieb.put("navy", new Farba(0, 0, 128));
		mapaFarieb.put("oldlace", new Farba(253, 245, 230));
		mapaFarieb.put("olive", new Farba(128, 128, 0));
		mapaFarieb.put("olivedrab", new Farba(107, 142, 35));
		mapaFarieb.put("orange", new Farba(255, 165, 0));
		mapaFarieb.put("orangered", new Farba(255, 69, 0));
		mapaFarieb.put("orchid", new Farba(218, 112, 214));
		mapaFarieb.put("palegoldenrod", new Farba(238, 232, 170));
		mapaFarieb.put("palegreen", new Farba(152, 251, 152));
		mapaFarieb.put("paleturquoise", new Farba(175, 238, 238));
		mapaFarieb.put("palevioletred", new Farba(219, 112, 147));
		mapaFarieb.put("papayawhip", new Farba(255, 239, 213));
		mapaFarieb.put("peachpuff", new Farba(255, 218, 185));
		mapaFarieb.put("peru", new Farba(205, 133, 63));
		mapaFarieb.put("pink", new Farba(255, 192, 203));
		mapaFarieb.put("plum", new Farba(221, 160, 221));
		mapaFarieb.put("powderblue", new Farba(176, 224, 230));
		mapaFarieb.put("purple", new Farba(128, 0, 128));
		mapaFarieb.put("rebeccapurple", new Farba(102, 51, 153));
		mapaFarieb.put("red", new Farba(255, 0, 0));
		mapaFarieb.put("rosybrown", new Farba(188, 143, 143));
		mapaFarieb.put("royalblue", new Farba(65, 105, 225));
		mapaFarieb.put("saddlebrown", new Farba(139, 69, 19));
		mapaFarieb.put("salmon", new Farba(250, 128, 114));
		mapaFarieb.put("sandybrown", new Farba(244, 164, 96));
		mapaFarieb.put("seagreen", new Farba(46, 139, 87));
		mapaFarieb.put("seashell", new Farba(255, 245, 238));
		mapaFarieb.put("sienna", new Farba(160, 82, 45));
		mapaFarieb.put("silver", new Farba(192, 192, 192));
		mapaFarieb.put("skyblue", new Farba(135, 206, 235));
		mapaFarieb.put("slateblue", new Farba(106, 90, 205));
		mapaFarieb.put("slategray", new Farba(112, 128, 144));
		mapaFarieb.put("slategrey", new Farba(112, 128, 144));
		mapaFarieb.put("snow", new Farba(255, 250, 250));
		mapaFarieb.put("springgreen", new Farba(0, 255, 127));
		mapaFarieb.put("steelblue", new Farba(70, 130, 180));
		mapaFarieb.put("tan", new Farba(210, 180, 140));
		mapaFarieb.put("teal", new Farba(0, 128, 128));
		mapaFarieb.put("thistle", new Farba(216, 191, 216));
		mapaFarieb.put("tomato", new Farba(255, 99, 71));
		mapaFarieb.put("turquoise", new Farba(64, 224, 208));
		mapaFarieb.put("violet", new Farba(238, 130, 238));
		mapaFarieb.put("wheat", new Farba(245, 222, 179));
		mapaFarieb.put("white", new Farba(255, 255, 255));
		mapaFarieb.put("whitesmoke", new Farba(245, 245, 245));
		mapaFarieb.put("yellow", new Farba(255, 255, 0));
		mapaFarieb.put("yellowgreen", new Farba(154, 205, 50));
	}

	private final TreeMap<String, Double> mapaJednotiek = new TreeMap<>();
	private final TreeMap<String, Double> mapaPosunov = new TreeMap<>();

	private void naplňMapuJednotiek()
	{
		mapaJednotiek.put("%", 0.01);
		mapaJednotiek.put("em", 1.0);
		mapaJednotiek.put("ex", 1.0);
		mapaJednotiek.put("ch", 1.0);
		mapaJednotiek.put("rem", 1.0);
		mapaJednotiek.put("vw", 1.0);
		mapaJednotiek.put("vh", 1.0);
		mapaJednotiek.put("vmin", 1.0);
		mapaJednotiek.put("vmax", 1.0);
		mapaJednotiek.put("cm", 96.0 / 2.54);
		mapaJednotiek.put("mm", 960.0 / 2.54);
		mapaJednotiek.put("q", 101.6);
		mapaJednotiek.put("in", 96.0);
		mapaJednotiek.put("pc", 96.0 / 6.0);
		mapaJednotiek.put("pt", 96.0 / 72.0);
		mapaJednotiek.put("px", 1.0);
		mapaJednotiek.put("deg", 1.0);
		mapaJednotiek.put("rad", 180.0 / Math.PI);
		mapaJednotiek.put("grad", 0.9); // 180.0 / 200.0 == 360.0 / 400.0
		mapaJednotiek.put("turn", 360.0);
	}

	// Rovnaké šablóny (názvami) ako sú tu, sú definované aj v triede
	// Farba, ale minimálne šablóna rgbaMatch je odlišná. Na zachovanie
	// flexibility do budúcna tu boli ponechané všetky.

	private final static Pattern hex6Match = Pattern.compile(
		"#[0-9A-Fa-f]{6}");

	private final static Pattern hex3Match = Pattern.compile(
		"#[0-9A-Fa-f]{3}");

	private final static Pattern rgbaMatch = Pattern.compile(
		"[Rr][Gg][Bb][Aa]? *\\(( *[0-9]{0,3} *,){2,3} *[-+0-9.eE]+ *\\)");

	private final static Pattern rgbaSplit = Pattern.compile(
		"[Rr][Gg][Bb][Aa]? *\\( *| *, *| *\\)");

	/**
	 * <p>Prevedie zadanú inštanciu farby do textovej podoby používanej
	 * v rámci jazykov HTML, CSS a teda aj SVG. Použitie tejto metódy je
	 * vhodné pri ukladaní údajov o farbe (napríklad výplne alebo čiary
	 * obrysu tvaru) do súboru v niektorom z uvedených súborových
	 * formátov (pri CSS by išlo o použitie nad rámec predvolených
	 * možností tejto triedy).</p>
	 * 
	 * <p>Podľa okolností je výsledkom tejto metódy reťazec v jednom
	 * z nasledujúcich tvarov: {@code none} (ak má zadaný objekt hodnotu
	 * {@code valnull}), <em>«HTML meno farby»</em>, {@code #rgb},
	 * {@code #rrggbb} alebo {@code rgba(red, green, blue, alpha)}.
	 * Posledná reprezentácia nie je univerzálne použiteľná v SVG
	 * atribútoch {@code fill} a {@code stroke}. Štandard SVG definuje
	 * doplňujúce atribúty {@code fill-opacity} a {@code stroke-opacity}.
	 * Na ich naplnenie je výhodné použiť metódu
	 * {@link #alfaNaReťazec(Color) alfaNaReťazec(farba)}.</p>
	 * 
	 * @param farba farba, ktorá má byť prevedená do reťazcovej podoby
	 * @param ignorujAlfu ak je hodnota tohto atribútu rovná
	 *     {@code valtrue}, tak vo výslednej reprezentácii bude ignorovaný
	 *     kanál priehľadnosti farby (alfa)
	 * @return farba prevedená do reťazcovej podoby
	 * 
	 * @see #alfaNaReťazec(Color)
	 * @see #reťazecNaFarbu(String, String)
	 */
	public static String farbaNaReťazec(Color farba, boolean ignorujAlfu)
	{
		if (null == farba) return "none";

		if (mapaFarieb.isEmpty()) naplňMapuFarieb();

		if (ignorujAlfu && 255 != farba.getAlpha())
			farba = new Color(farba.getRed(),
				farba.getGreen(), farba.getBlue());

		for (Map.Entry<String, Farba> položka : mapaFarieb.entrySet())
		{
			if (null == položka.getKey()) continue;
			if (položka.getValue().equals(farba)) return položka.getKey();
		}

		int r = farba.getRed();
		int g = farba.getGreen();
		int b = farba.getBlue();
		int a = farba.getAlpha();

		if (ignorujAlfu || 255 == a)
		{
			if (r / 16 == r % 16 && g / 16 == g % 16 && b / 16 == b % 16)
				return String.format(Locale.ENGLISH,
					"#%x%x%x", r % 16, g % 16, b % 16);
			return String.format(Locale.ENGLISH,
				"#%02x%02x%02x", r, g, b);
		}

		return String.format(Locale.ENGLISH,
			"rgba(%d, %d, %d, %f)", r, g, b,
			((float)a) / 255.0f);
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaNaReťazec(Color, boolean) farbaNaReťazec}.</p> */
	public static String farbaNaRetazec(Color farba, boolean ignorujAlfu)
	{ return farbaNaReťazec(farba, ignorujAlfu); }

	/**
	 * <p>Prevedie zadanú implementáciu farebnosti do textovej podoby
	 * používanej v rámci jazykov HTML, CSS a teda aj SVG. Použitie
	 * tejto metódy je vhodné pri ukladaní údajov o farbe (napríklad
	 * výplne alebo čiary obrysu tvaru) do súboru v niektorom z uvedených
	 * súborových formátov (pri CSS by išlo o použitie nad rámec
	 * predvolených možností tejto triedy).</p>
	 * 
	 * <p>Podľa okolností je výsledkom tejto metódy reťazec v jednom
	 * z nasledujúcich tvarov: {@code none} (ak má zadaný objekt hodnotu
	 * {@code valnull}), <em>«HTML meno farby»</em>, {@code #rgb},
	 * {@code #rrggbb} alebo {@code rgba(red, green, blue, alpha)}.
	 * Posledná reprezentácia nie je univerzálne použiteľná v SVG
	 * atribútoch {@code fill} a {@code stroke}. Štandard SVG definuje
	 * doplňujúce atribúty {@code fill-opacity} a {@code stroke-opacity}.
	 * Na ich naplnenie je výhodné použiť metódu
	 * {@link #alfaNaReťazec(Farebnosť) alfaNaReťazec(farba)}.</p>
	 * 
	 * @param farba inštancia farebnosti, ktorá má byť prevedená do
	 *     reťazcovej podoby
	 * @param ignorujAlfu ak je hodnota tohto atribútu rovná
	 *     {@code valtrue}, tak vo výslednej reprezentácii bude ignorovaný
	 *     kanál priehľadnosti farby (alfa)
	 * @return implementácia farebnosti prevedená do reťazcovej podoby
	 * 
	 * @see #alfaNaReťazec(Farebnosť)
	 * @see #reťazecNaFarbu(String, String)
	 */
	public static String farbaNaReťazec(Farebnosť farba,
		boolean ignorujAlfu)
	{
		if (null == farba) return "none";
		return farbaNaReťazec(farba.farba(), ignorujAlfu);
	}

	/**
	 * <p>Vyjme zo zadanej inštancie farby hodnotu úrovne priehľadnosti
	 * a prevedie ju na reťazec v rozsahu {@code num0.0} až
	 * {@code num1.0}. Táto metóda je dôležitým doplnkom metódy {@link 
	 * #farbaNaReťazec(Color, boolean) farbaNaReťazec(farba,
	 * ignorujAlfu)}, pretože nie každý softvér pracujúci s formátom SVG
	 * akceptuje v atribútoch {@code fill} a {@code stroke} farby v tvare
	 * {@code rgba(red, green, blue, alpha)}. Naproti tomu sme si overili,
	 * že všetok nami používaný softvér rešpektuje doplňujúce atribúty
	 * {@code fill-opacity} a {@code stroke-opacity}. Práve na určenie
	 * ich hodnôt je vhodná táto metóda.</p>
	 * 
	 * @param farba farba, ktorej zložka priehľadnosti má byť prevedená
	 *     do reťazcovej podoby
	 * @return zložka priehľadnosti zadanej farby prevedená do reťazcovej
	 *     podoby (číslo v rozsahu {@code num0.0} až {@code num1.0}
	 *     vyjadrené ako text)
	 * 
	 * @see #farbaNaReťazec(Color, boolean)
	 * @see #reťazecNaFarbu(String, String)
	 */
	public static String alfaNaReťazec(Color farba)
	{
		if (null == farba) return null;
		return String.format(Locale.ENGLISH,
			"%f", ((float)farba.getAlpha()) / 255.0f);
	}

	/** <p><a class="alias"></a> Alias pre {@link #alfaNaReťazec(Color) alfaNaReťazec}.</p> */
	public static String alfaNaRetazec(Color farba)
	{ return alfaNaReťazec(farba); }

	/**
	 * <p>Vyjme zo zadanej implementácie farebnosti hodnotu úrovne
	 * priehľadnosti a prevedie ju na reťazec v rozsahu {@code num0.0}
	 * až {@code num1.0}. Táto metóda je dôležitým doplnkom metódy
	 * {@link #farbaNaReťazec(Color, boolean) farbaNaReťazec(farba,
	 * ignorujAlfu)}, pretože nie každý softvér pracujúci s formátom SVG
	 * akceptuje v atribútoch {@code fill} a {@code stroke} farby v tvare
	 * {@code rgba(red, green, blue, alpha)}. Naproti tomu sme si overili,
	 * že všetok nami používaný softvér rešpektuje doplňujúce atribúty
	 * {@code fill-opacity} a {@code stroke-opacity}. Práve na určenie
	 * ich hodnôt je vhodná táto metóda.</p>
	 * 
	 * @param farba inštancia farebnosti, ktorej zložka priehľadnosti má
	 *     byť prevedená do reťazcovej podoby
	 * @return zložka priehľadnosti zadanej inštancie prevedená do
	 *     reťazcovej podoby (číslo v rozsahu {@code num0.0} až
	 *     {@code num1.0} vyjadrené ako text)
	 * 
	 * @see #farbaNaReťazec(Farebnosť, boolean)
	 * @see #reťazecNaFarbu(String, String)
	 */
	public static String alfaNaReťazec(Farebnosť farba)
	{
		if (null == farba) return null;
		return alfaNaReťazec(farba.farba());
	}

	/** <p><a class="alias"></a> Alias pre {@link #alfaNaReťazec(Farebnosť) alfaNaReťazec}.</p> */
	public static String alfaNaRetazec(Farebnosť farba)
	{ return alfaNaReťazec(farba); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNaReťazec(Farebnosť, boolean) farbaNaReťazec}.</p> */
	public static String farbaNaRetazec(Farebnosť farba,
		boolean ignorujAlfu)
	{ return farbaNaReťazec(farba, ignorujAlfu); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci farbu vo formátoch
	 * používaných jazykmi HTML, CSS a teda aj SVG na inštanciu farby
	 * programovacieho rámca. Metóda rozpoznáva buď názvy farebných entít
	 * uvedených jazykov (148 názvov typu {@code black}, {@code yellow},
	 * {@code white}…), alebo reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>#</code><em>rrggbb</em>, kde <em>rr</em> je červená zložka
	 * v šestnástkovom tvare, <em>gg</em> zelená zložka v šestnástkovom
	 * tvare a <em>bb</em> modrá zložka v šestnástkovom tvare,</li>
	 * <li><code>#</code><em>rgb</em>, kde <em>r</em> je červená zložka
	 * v šestnástkovom tvare a <em>g</em> zelená zložka v šestnástkovom
	 * tvare, <em>b</em> modrá zložka v šestnástkovom tvare, pričom všetky
	 * zložky v tomto budú rozšírené na: <em>rr</em>, <em>gg</em>, <em>bb</em>,
	 * napríklad: <code>#8af</code> = <code>#88aaff</code></li>
	 * <li><code>rgb(</code><em>red</em><code>, </code><em>green</em><code>,
	 * </code><em>blue</em><code>)</code>, kde <em>red</em> je červená zložka
	 * (v desiatkovom tvare), <em>green</em> zelená zložka, <em>blue</em>
	 * modrá zložka (všetko v desiatkovom tvare),</li>
	 * <li><code>rgba(</code><em>red</em><code>, </code><em>green</em><code>,
	 * </code><em>blue</em><code>, </code><em>alpha</em><code>)</code>, kde
	 * <em>red</em> je červená zložka (v desiatkovom tvare), <em>green</em>
	 * zelená zložka, <em>blue</em> modrá zložka (v desiatkových tvaroch),
	 * <em>alpha</em> priehľadnosť vyjadrená ako reálne číslo v rozsahu od
	 * {@code num0.0} do {@code num1.0}.</li>
	 * </ul>
	 * 
	 * <p>Druhý argument ({@code alfa}) slúži na doplnenie hodnoty
	 * priehľadnosti k jednej z vyššie uvedených reprezentácií (okrem
	 * poslednej). Je to implementácia alternatívneho spôsobu určovania
	 * priehľadnosti výplne a čiary ({@code fill} a {@code stroke})
	 * prostredníctvom XML atribútov {@code fill-opacity}
	 * a {@code stroke-opacity} štandardu SVG (niektoré softvéry
	 * rozpoznávajú len tento spôsob určovania priehľadnosti).</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metódam {@link 
	 * #farbaNaReťazec(Color, boolean) farbaNaReťazec} a {@link 
	 * #alfaNaReťazec(Color) alfaNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený na farbu
	 * @param alfa dodatočný reťazec obsahujúci úroveň priehľadnosti;
	 *     môže byť {@code valnull}
	 * @return inštancia farby získaná z reťazcovej podoby alebo
	 *     {@code valnull}, ak sa formát nepodarilo rozpoznať
	 * 
	 * @see #farbaNaReťazec(Color, boolean)
	 * @see #alfaNaReťazec(Color)
	 */
	public static Farba reťazecNaFarbu(String text, String alfa)
	{
		if (mapaFarieb.isEmpty()) naplňMapuFarieb();
		text = text.trim();

		int alfaHodnota = 255;
		try
		{
			if (null != alfa)
				alfaHodnota = (int)(Float.parseFloat(alfa) * 255.0f);
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e, true);
		}

		Farba farba = mapaFarieb.get(text.toLowerCase());
		if (null != farba)
		{
			if (255 == alfaHodnota) return farba;
			return new Farba(farba.getRed(), farba.getGreen(),
				farba.getBlue(), alfaHodnota);
		}

		if (hex6Match.matcher(text).matches())
		{
			try
			{
				if (255 == alfaHodnota)
					return new Farba(
						Integer.parseInt(text.substring(1, 3), 16),
						Integer.parseInt(text.substring(3, 5), 16),
						Integer.parseInt(text.substring(5, 7), 16));
				return new Farba(
					Integer.parseInt(text.substring(1, 3), 16),
					Integer.parseInt(text.substring(3, 5), 16),
					Integer.parseInt(text.substring(5, 7), 16),
					alfaHodnota);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		}
		else if (hex3Match.matcher(text).matches())
		{
			try
			{
				int r = Integer.parseInt(text.substring(1, 2), 16);
				int g = Integer.parseInt(text.substring(2, 3), 16);
				int b = Integer.parseInt(text.substring(3, 4), 16);
				if (255 == alfaHodnota)
					return new Farba(r << 4 | r, g << 4 | g, b << 4 | b);
				return new Farba(r << 4 | r, g << 4 | g, b << 4 | b,
					alfaHodnota);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		}
		else if (rgbaMatch.matcher(text).matches())
		{
			String zložky[] = rgbaSplit.split(text);

			try
			{
				if (5 == zložky.length)
					return new Farba(
						Integer.parseInt(zložky[1]),
						Integer.parseInt(zložky[2]),
						Integer.parseInt(zložky[3]),
						(int)(Float.parseFloat(zložky[4]) * 255.0f));
				if (255 == alfaHodnota)
					return new Farba(
						Integer.parseInt(zložky[1]),
						Integer.parseInt(zložky[2]),
						Integer.parseInt(zložky[3]));
				return new Farba(
					Integer.parseInt(zložky[1]),
					Integer.parseInt(zložky[2]),
					Integer.parseInt(zložky[3]),
					alfaHodnota);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaFarbu(String, String) reťazecNaFarbu}.</p> */
	public static Farba retazecNaFarbu(String text, String alfa)
	{ return reťazecNaFarbu(text, alfa); }


	/**
	 * <p>Zabezpečí prevod reťazca na číslo (bez vzniku chyby) s vyhľadaním
	 * a prepočítaním prípadných jednotiek používaných v jazykoch HTML,
	 * CSS a teda aj SVG (uvedených za číselnou hodnotou v reťazcovom
	 * tvare). Všetky relatívne jednotky, okrem percenta, (to jest:
	 * {@code em}, {@code ex}, {@code ch}, {@code rem}, {@code vw},
	 * {@code vh}, {@code vmin} a {@code vmax}) majú nastavený predvolený
	 * pomer prevodu 1 : 1, to znamená, že výsledkom je rovnaká hodnota,
	 * aká bola zapísaná v textovom tvare. Percentuálna jednotka má
	 * nastavený predvolený pomer 1 : 100. Absolútne jednotky (to jest:
	 * {@code cm}, {@code mm}, {@code q}, {@code in}, {@code pc},
	 * {@code pt} a {@code px}) sú prepočítané podľa štandardu. Jednotky
	 * uhlov ({@code deg}, {@code rad}, {@code grad} a {@code turn}) sú
	 * prepočítané na „klasické“ uhlové stupne ({@code deg}), kedy jedna
	 * otáčka znamená 360°.</p>
	 * 
	 * @param hodnota číselná hodnota s prípadným uvedením jednotiek
	 * @return výsledná prevedená a prepočítaná číselná hodnota
	 */
	public double reťazecNaČíslo(String hodnota)
	{
		if (mapaJednotiek.isEmpty()) naplňMapuJednotiek();
		hodnota = hodnota.trim().toLowerCase();
		double prevod = 1.0, posun = 0.0;

		for (Map.Entry<String, Double> položka : mapaJednotiek.entrySet())
		{
			if (null == položka.getKey()) continue;

			String jednotka = položka.getKey();

			if (hodnota.endsWith(jednotka))
			{
				hodnota = hodnota.substring(0, hodnota.length() -
					jednotka.length());
				prevod = položka.getValue();

				try { posun = mapaPosunov.get(jednotka); }
				catch (Exception e) {} // Týmto to ešte nekončí…
			}
		}

		try { return posun + (Double.parseDouble(hodnota) * prevod); }
		catch (Exception e) {} // A toto už nevadí – výsledok bude nula…

		return 0.0;
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaČíslo(String) reťazecNaČíslo}.</p> */
	public double retazecNaCislo(String hodnota)
	{ return reťazecNaČíslo(hodnota); }


	/**
	 * <p>Táto metóda umožňuje upraviť koeficient prevodu jednotky číselnej
	 * hodnoty (pozri opis metódy {@link #reťazecNaČíslo(String)
	 * reťazecNaČíslo}).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Aktuálnu hodnotu koeficientu je
	 * možné overiť inou verziou tejto metódy. Touto metódou je tiež možné
	 * nastaviť koeficient prevodu úplne nových jednotiek.</p>
	 * 
	 * <p>Ak je namiesto jednotky zadaný prázdny reťazec, tak je hodnota
	 * ignorovaná. Podobne, ak je zadaná nová hodnota koeficientu
	 * nekonečná (alebo neplatná, napríklad rovná nule), tak je
	 * ignorovaná.</p>
	 * 
	 * @param jednotka jednotka, ktorej koeficient prevodu chceme zmeniť
	 *     alebo nastaviť
	 * @param hodnota nová číselná hodnota koeficientu prevodu
	 */
	public void koeficient(String jednotka, double hodnota)
	{
		if (null == jednotka) return;
		jednotka = jednotka.trim().toLowerCase();
		if (jednotka.isEmpty()) return;
		if (mapaJednotiek.isEmpty()) naplňMapuJednotiek();
		if (Double.isFinite(hodnota) && 0.0 != hodnota)
			mapaJednotiek.put(jednotka, hodnota);
	}

	/**
	 * <p>Táto metóda umožňuje overiť hodnotu koeficientu prevodu jednotky
	 * číselnej hodnoty (pozri opis metódy {@link #reťazecNaČíslo(String)
	 * reťazecNaČíslo}). Ak zadaná jednotka nejestvuje (nie je definovaná),
	 * tak táto metóda vráti hodnotu {@link Double#NaN}.</p>
	 * 
	 * @param jednotka jednotka, ktorej koeficient prevodu chceme zistiť
	 * @return koeficient pre určenú jednotku alebo {@link Double#NaN}
	 */
	public double koeficient(String jednotka)
	{
		if (null == jednotka) return Double.NaN;
		jednotka = jednotka.trim().toLowerCase();
		if (jednotka.isEmpty()) return Double.NaN;
		if (mapaJednotiek.isEmpty()) naplňMapuJednotiek();
		Double hodnota = mapaJednotiek.get(jednotka);
		if (null != hodnota) return hodnota;
		return Double.NaN;
	}


	// Toto pole obsahuje šablónu HTML súboru na zápis SVG tvarov.
	// Hodnotu poľa je možné zistiť alebo nastaviť rovnomennými metódami
	// (a ich aliasmi).
	private String[] htmlŠablóna =
	{
		"<!DOCTYPE html>", "<html>", "<head>",
		"<meta charset=\"$KÓDOVANIE\" />", "<title>$TITULOK</title>",
		"</head>", "<body>", "<svg width=\"$ŠÍRKA\" height=\"$VÝŠKA\">",
		"<g stroke-linecap=\"round\" stroke-linejoin=\"round\">",
		"$TVARY", "</g>", "</svg>", "</body>", "</html>"
	};


	// Toto pole obsahuje šablónu SVG súboru na zápis tvarov. Hodnotu
	// poľa je možné zistiť alebo nastaviť rovnomennými metódami (a ich
	// aliasmi).
	private String[] svgŠablóna =
	{
		"<?xml version=\"1.0\" encoding=\"$KÓDOVANIE\"" +
		" standalone=\"no\"?>", "<svg xmlns=\"http://www.w3.org/2000/svg" +
		"\" width=\"$ŠÍRKA\" height=\"$VÝŠKA\">", "<title>$TITULOK</title>",
		"<g stroke-linecap=\"round\" stroke-linejoin=\"round\">", "$TVARY",
		"</g>", "</svg>"
	};


	/**
	 * <p>Táto metóda umožňuje zistiť aktuálny tvar šablóny HTML súboru.
	 * Šablóna je použitá metódami {@link #zapíš(String) zapíš} slúžiacimi
	 * na zápis tvarov (vo formáte SVG).</p>
	 * 
	 * <p>Jeden prvok poľa je ekvivalentný jednému riadku HTML súboru.
	 * Výnimku tvorí riadok obsahujúci rezervovaný reťazec {@code $TVARY}
	 * (resp. {@code $SHAPES}), pretože tento reťazec bude nahradený SVG
	 * definíciami tvarov.</p>
	 * 
	 * <p>V prípade potreby môžete obsah tejto šablóny nahradiť iným
	 * požadovaným tvarom metódou {@link #htmlŠablóna(String[])
	 * htmlŠablóna(šablóna)}.</p>
	 * 
	 * @return aktuálny tvar šablóny HTML súboru
	 */
	public String[] htmlŠablóna() { return htmlŠablóna; }

	/** <p><a class="alias"></a> Alias pre {@link #htmlŠablóna() htmlŠablóna}.</p> */
	public String[] htmlSablona() { return htmlŠablóna; }

	/**
	 * <p>Táto metóda umožňuje zistiť aktuálny tvar šablóny SVG súboru.
	 * Šablóna je použitá metódami {@link #zapíš(String) zapíš} slúžiacimi
	 * na zápis tvarov (vo formáte SVG).</p>
	 * 
	 * <p>Jeden prvok poľa je ekvivalentný jednému riadku SVG súboru.
	 * Výnimku tvorí riadok obsahujúci rezervovaný reťazec {@code $TVARY}
	 * (resp. {@code $SHAPES}), pretože tento reťazec bude nahradený SVG
	 * definíciami tvarov.</p>
	 * 
	 * <p>V prípade potreby môžete obsah tejto šablóny nahradiť iným
	 * požadovaným tvarom metódou {@link #svgŠablóna(String[])
	 * svgŠablóna(šablóna)}.</p>
	 * 
	 * @return aktuálny tvar šablóny SVG súboru
	 */
	public String[] svgŠablóna() { return svgŠablóna; }

	/** <p><a class="alias"></a> Alias pre {@link #svgŠablóna() svgŠablóna}.</p> */
	public String[] svgSablona() { return svgŠablóna; }


	/**
	 * <p>Táto metóda slúži na nastavenie nového tvaru šablóny HTML súboru.
	 * Šablóna je použitá metódami {@link #zapíš(String) zapíš} slúžiacimi
	 * na zápis tvarov (vo formáte SVG).</p>
	 * 
	 * <p>Jeden prvok poľa je ekvivalentný jednému riadku HTML súboru.
	 * Výnimku tvorí riadok obsahujúci rezervovaný reťazec {@code $TVARY}
	 * (resp. {@code $SHAPES}), pretože tento reťazec bude nahradený SVG
	 * definíciami tvarov.</p>
	 * 
	 * <p>Šablóna musí byť v korektnom tvare (dotýka sa to syntaxe
	 * a štruktúry) HTML súboru s vloženým {@code svg} elementom, inak
	 * bude výsledný súbor v nekorektnom tvare, čo môže viesť k jeho
	 * nefunkčnosti! (Za korektnú štruktúru šablóny je zodpovedný autor.)
	 * Šablóna musí obsahovať rezervovaný reťazec: {@code $TVARY} (resp.
	 * {@code $SHAPES}). Mala by tiež obsahovať rezervované reťazce:
	 * {@code $KÓDOVANIE}, {@code $ŠÍRKA}, {@code $VÝŠKA}
	 * a {@code $TITULOK}, ktoré budú nahradené korektným kódovaním,
	 * rozmermi plátien a zadaným alebo predvoleným titulkom.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Rezervované reťazce
	 * {@code $KÓDOVANIE}, {@code $ŠÍRKA} a {@code $VÝŠKA} môžu byť
	 * uvedené aj bez diakritiky a všetky rezervované reťazce majú
	 * definované aliasy v anglickom jazyku: {@code $KÓDOVANIE} –
	 * {@code $ENCODING}, {@code $ŠÍRKA} – {@code $WIDTH},
	 * {@code $VÝŠKA} – {@code $HEIGHT}, {@code $TITULOK} –
	 * {@code $TITLE} a {@code $TVARY} – {@code $SHAPES}.</p>
	 * 
	 * @param šablóna nový požadovaný tvar HTML šablóny
	 * @return {@code valtrue} v prípade úspechu (šablónu sa podarilo
	 *     zmeniť), {@code valfalse} v prípade neúspechu (šablóna
	 *     neobsahuje požadovaný reťazec {@code $TVARY} (resp.
	 *     {@code $SHAPES}) alebo sa v zadaných vstupných údajoch vyskytla
	 *     hodnota {@code valnull} – to znamená, že korektnosť štruktúry
	 *     šablóny nie je overovaná)
	 */
	public boolean htmlŠablóna(String[] šablóna)
	{
		if (null == šablóna) return false;

		boolean korektné = false;

		for (String riadok : šablóna)
		{
			if (null == riadok) return false;

			if (-1 != riadok.indexOf("$TVARY") ||
				-1 != riadok.indexOf("$SHAPES"))
			{
				korektné = true;
				break;
			}
		}

		if (korektné)
		{
			htmlŠablóna = šablóna;
			return true;
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #htmlŠablóna(String[]) htmlŠablóna}.</p> */
	public boolean htmlSablona(String[] šablóna)
	{ return htmlŠablóna(šablóna); }

	/**
	 * <p>Táto metóda slúži na nastavenie nového tvaru šablóny SVG súboru.
	 * Šablóna je použitá metódami {@link #zapíš(String) zapíš} slúžiacimi
	 * na zápis tvarov (vo formáte SVG).</p>
	 * 
	 * <p>Jeden prvok poľa je ekvivalentný jednému riadku SVG súboru.
	 * Výnimku tvorí riadok obsahujúci rezervovaný reťazec {@code $TVARY}
	 * (resp. {@code $SHAPES}), pretože tento reťazec bude nahradený SVG
	 * definíciami tvarov.</p>
	 * 
	 * <p>Šablóna musí byť v korektnom tvare (dotýka sa to syntaxe
	 * a štruktúry) SVG súboru, inak bude výsledný súbor v nekorektnom
	 * tvare, čo môže viesť k jeho nefunkčnosti! (Za korektnú štruktúru
	 * šablóny je zodpovedný autor.) Šablóna musí obsahovať rezervovaný
	 * reťazec: {@code $TVARY} (resp. {@code $SHAPES}). Mala by tiež
	 * obsahovať rezervované reťazce: {@code $KÓDOVANIE}, {@code $ŠÍRKA},
	 * {@code $VÝŠKA} a {@code $TITULOK}, ktoré budú nahradené korektným
	 * kódovaním, rozmermi plátien a zadaným alebo predvoleným
	 * titulkom.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Rezervované reťazce
	 * {@code $KÓDOVANIE}, {@code $ŠÍRKA} a {@code $VÝŠKA} môžu byť
	 * uvedené aj bez diakritiky a všetky rezervované reťazce majú
	 * definované aliasy v anglickom jazyku: {@code $KÓDOVANIE} –
	 * {@code $ENCODING}, {@code $ŠÍRKA} – {@code $WIDTH},
	 * {@code $VÝŠKA} – {@code $HEIGHT}, {@code $TITULOK} –
	 * {@code $TITLE} a {@code $TVARY} – {@code $SHAPES}.</p>
	 * 
	 * @param šablóna nový požadovaný tvar SVG šablóny
	 * @return {@code valtrue} v prípade úspechu (šablónu sa podarilo
	 *     zmeniť), {@code valfalse} v prípade neúspechu (šablóna
	 *     neobsahuje požadovaný reťazec {@code $TVARY} (resp.
	 *     {@code $SHAPES}) alebo sa v zadaných vstupných údajoch vyskytla
	 *     hodnota {@code valnull} – to znamená, že korektnosť štruktúry
	 *     šablóny nie je overovaná)
	 */
	public boolean svgŠablóna(String[] šablóna)
	{
		if (null == šablóna) return false;

		boolean korektné = false;

		for (String riadok : šablóna)
		{
			if (null == riadok) return false;

			if (-1 != riadok.indexOf("$TVARY") ||
				-1 != riadok.indexOf("$SHAPES"))
			{
				korektné = true;
				break;
			}
		}

		if (korektné)
		{
			svgŠablóna = šablóna;
			return true;
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #svgŠablóna(String[]) svgŠablóna}.</p> */
	public boolean svgSablona(String[] šablóna)
	{ return svgŠablóna(šablóna); }


	// „Továreň“ XML vstupu
	private static XMLInputFactory xmlVstup = null;


	/* *
	 * Prevedie celočíselnú hodnotu typu udalosti v rámci analýzy údajov
	 * z XML prúdu na čitateľný tvar reťazca v slovenskom jazyku.
	 * /
	private static String XMLTypUdalostiNaReťazec(int typUdalosti)
	{
		switch (typUdalosti)
		{
			case XMLStreamConstants.START_ELEMENT:          // 1
				return "začiatočný element";
			case XMLStreamConstants.END_ELEMENT:            // 2
				return "koncový element";
			case XMLStreamConstants.PROCESSING_INSTRUCTION: // 3
				return "inštrukcia na spracovanie";
			case XMLStreamConstants.CHARACTERS:             // 4
				return "znaky";
			case XMLStreamConstants.COMMENT:                // 5
				return "komentár";
			case XMLStreamConstants.SPACE:                  // 6
				return "medzery";
			case XMLStreamConstants.START_DOCUMENT:         // 7
				return "začiatok dokumentu";
			case XMLStreamConstants.END_DOCUMENT:           // 8
				return "koniec dokumentu";
			case XMLStreamConstants.ENTITY_REFERENCE:       // 9
				return "odkaz na entitu";
			case XMLStreamConstants.ATTRIBUTE:              // 10
				return "atribút";
			case XMLStreamConstants.DTD:                    // 11
				return "definícia typu dokumentu";
			case XMLStreamConstants.CDATA:                  // 12
				return "znakové údaje";
			case XMLStreamConstants.NAMESPACE:              // 13
				return "menný priestor";
			case XMLStreamConstants.NOTATION_DECLARATION:   // 14
				return "deklarácia spôsobu zápisu";
			case XMLStreamConstants.ENTITY_DECLARATION:     // 15
				return "deklarácia entity";
		}
		return "neznámy";
	}
	/* */

	// Trieda umožňujúca vytvoriť zásobník atribútov
	@SuppressWarnings("serial")
	private class Atribúty extends TreeMap<String, String>
	{
		public Atribúty() { super(); }
		public Atribúty(Comparator<String> comp) { super(comp); }
		public Atribúty(Map<String, String> m) { super(m); }
		public Atribúty(SortedMap<String, String> m) { super(m); }
	}

	// Zásobník série atribútov
	private final Stack<Atribúty> zásobníkAtribútov = new Stack<>();

	// Trieda reprezentujúca údajovú štruktúru tvaru s asociovanými
	// atribútmi
	private class Tvar
	{
		public Shape tvar;
		public final Atribúty atribúty;

		// Zoznam rozpoznaných transformácií
		private Transformácia[] transformácie = null;

		public Tvar(Shape tvar, Atribúty atribúty)
		{
			this.tvar = tvar;
			this.atribúty = new Atribúty(atribúty);
		}
	}

	// Vnútorný zoznam tvarov tejto inštancie
	private final Vector<Tvar> tvary = new Vector<>();

	/**
	 * <p>Vráti počet tvarov, ktoré sú momentálne uskladnené v tejto
	 * inštancii (v jej vnútornom zásobníku) podpory SVG formátu.
	 * Tvary mohli byť do zásobníka vložené (importované)
	 * {@linkplain #čítaj(String) čítaním zo súboru} alebo metódami
	 * {@link #pridaj(Shape, String[]) pridaj} a {@link #pridajText(String,
	 * GRobot, String[]) pridajText(tvar, tvorca, atribúty)}. Uskladnené
	 * tvary môžu byť {@linkplain #dajSVG(int) vyjadrené v XML/SVG formáte}
	 * a {@linkplain #zapíš(String) uložené} (exportované) do súboru
	 * (HTML alebo SVG).</p>
	 * 
	 * @return počet tvarov, ktoré sú momentálne uskladnené v tejto
	 *     inštancii (v jej vnútornom zásobníku)
	 */
	public int počet() { return tvary.size(); }

	/** <p><a class="alias"></a> Alias pre {@link #počet() počet}.</p> */
	public int pocet() { return počet(); }

	/**
	 * <p>Vyčistí vnútorný zásobník tvarov tejto inštancie. To znamená,
	 * že všetky vnútorne uskladnené tvary budú z tejto inštancie
	 * odstránené.</p>
	 */
	public void vymaž() { tvary.clear(); }

	/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
	public void vymaz() { vymaž(); }


	/**
	 * <p>Táto statická metóda vyrobí transformovanú verziu zadaného tvaru
	 * tak, aby nové súradnice jeho „stredu“ ležali v strede súradnicovej
	 * sústavy programovacieho rámca GRobot. „Stredom“ tvaru je myslený
	 * stred ohraničujúceho obdĺžnika tvaru. (O súradnicových priestoroch
	 * sa podrobnejšie píše napríklad v opisoch metód {@link 
	 * GRobot#cesta() GRobot.cesta()}, {@link SVGPodpora#zapíš(String,
	 * String, boolean) SVGpodpora.zapíš(…)}, {@link 
	 * SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
	 * v celej dokumentácii.)</p>
	 * 
	 * <p> </p>
	 * 
	 * <p><b>Malá algoritmická analýza</b></p>
	 * 
	 * <p>Keby nejestvovala táto metóda, bolo by potrebné zakaždým
	 * vykonať nasledujúce príkazy:</p>
	 * 
	 * <pre CLASS="example">
		{@link Bod Bod} bod = {@link Bod Bod}.{@link Bod#polohaTvaru(Shape) polohaTvaru}(tvar);

		tvar = {@link SVGPodpora SVGPodpora}.{@link SVGPodpora#dajVýsledný(Shape, Transformácia, Transformácia[]) dajVýsledný}(tvar,
			{@code kwdnew} {@link SVGPodpora.Transformácia#SVGPodpora.Transformácia(int, Double[]) Transformácia}({@link SVGPodpora.Transformácia Transformácia}.{@link SVGPodpora.Transformácia#POSUN POSUN},
				-bod.{@link Bod#polohaX() polohaX}(), bod.{@link Bod#polohaY() polohaY}()));
		</pre>
	 * 
	 * <p>Navyše, ak by nejestvovala metóda {@link Bod#polohaTvaru(Shape)
	 * polohaTvaru}, tak by bolo treba postupovať mierne odlišným spôsobom,
	 * napríklad využiť niektorý robot ({@code r}):</p>
	 * 
	 * <pre CLASS="example">
		{@code comm//  1. Najprv zálohovať jeho aktuálnu polohu (ak na nej záleží):}
		{@link Bod Bod} záloha = r.{@link GRobot#poloha() poloha}();

		{@code comm//  2. Potom sa robotom presunúť do stredu tvaru:}
		r.{@link GRobot#skočNa(Shape) skočNa}(tvar);

		{@code comm//  3. Potom využiť aktuálnu polohu robota na výrobu}
		{@code comm//     presunutého tvaru (s pomocou transformácie):}
		tvar = {@link SVGPodpora SVGPodpora}.{@link SVGPodpora#dajVýsledný(Shape, Transformácia, Transformácia[]) dajVýsledný}(tvar,
			{@code kwdnew} {@link SVGPodpora.Transformácia#SVGPodpora.Transformácia(int, Double[]) Transformácia}({@link SVGPodpora.Transformácia Transformácia}.{@link SVGPodpora.Transformácia#POSUN POSUN},
				-r.{@link GRobot#polohaX() polohaX}(), r.{@link GRobot#polohaY() polohaY}()));

		{@code comm//  4. Nakoniec vrátiť robot do pôvodnej polohy (zo zálohy):}
		r.{@link GRobot#skočNa(Poloha) skočNa}(záloha);
		</pre>
	 * 
	 * <p>Tieto okolnosti viedli k vzniku tejto metódy, ktorá pracuje
	 * priamočiarejšie.</p>
	 * 
	 * <p> </p>
	 * 
	 * @param tvar tvar, ktorého presunutá verzia má byť vyprodukovaná
	 * @return transformovaný tvar – presunutý do stredu súradnicovej
	 *     sústavy programovacieho rámca
	 */
	public static Shape presuňDoStredu(Shape tvar)
	{
		Rectangle2D hranice = tvar.getBounds2D();
		double novéX = Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2;
		double novéY = Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2;
		return AffineTransform.getTranslateInstance(-novéX, novéY).
			createTransformedShape(tvar);
	}

	/** <p><a class="alias"></a> Alias pre {@link #presuňDoStredu(Shape) presuňDoStredu}.</p> */
	public static Shape presunDoStredu(Shape tvar)
	{ return presuňDoStredu(tvar); }


	/**
	 * <p>Vypočíta súradnice stredu kresby a vráti ich v inštancii triedy
	 * {@link Bod Bod}. Vrátené súradnice sú prepočítané do súradnicového
	 * priestoru rámca. (O súradnicových priestoroch sa podrobnejšie píše
	 * napríklad v opisoch metód {@link GRobot#cesta() GRobot.cesta()},
	 * {@link SVGPodpora#zapíš(String, String, boolean)
	 * SVGpodpora.zapíš(…)}, {@link SVGPodpora#čítaj(String)
	 * SVGpodpora.čítaj(meno)} a priebežne v celej dokumentácii.)</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Nasledujúci príklad ukazuje spôsob použitia tejto metódy na
	 * pridanie takých transformácií (vo forme objektov triedy
	 * {@link AffineTransform AffineTransform}) všetkým tvarom kresby,
	 * ktoré ju posunú do stredu súradnicovej sústavy robota:</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Tento príklad vyžaduje
	 * importovanie triedy Javy {@link java.awt.geom.AffineTransform}.</p>
	 * 
	 * <pre CLASS="example">
		{@code comm// Získame súradnice stredu:}
		{@link Bod Bod} stred = {@link GRobot#svgPodpora svgPodpora}.{@code currstredKresby}();

		{@code comm// Vyrobíme transformáciu posunutia, ktorá však očakáva súradnice}
		{@code comm// v rámci súradnicového priestoru Javy (preto neobrátime y-ovú}
		{@code comm// súradnicu):}
		{@link AffineTransform AffineTransform} posun = {@link AffineTransform AffineTransform}.
			{@link AffineTransform#getTranslateInstance(double, double) getTranslateInstance}(&#45;stred.{@link Bod#polohaX() polohaX}(), stred.{@link Bod#polohaY() polohaY}());

		{@code comm// Uložíme počet tvarov kresby do pomocnej premennej:}
		{@code typeint} počet = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#počet() počet}();

		{@code comm// Pridáme transformáciu ku každému tvaru:}
		{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; počet; ++i)
			{@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#pridajTransformácie(int, AffineTransform, AffineTransform[]) pridajTransformácie}(i, posun);
		</pre>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nesúvisí s metódou
	 * {@link #presuňDoStredu(Shape) presuňDoStredu}, ktorá pracuje
	 * s jednotlivými inštanciami triedy {@link Shape Shape}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V triede nie je úmyselne
	 * definovaná metóda <em>„presuňKresbuDoStredu“</em>, ktorá by
	 * uzavrela (skryla) algoritmus vyššie uvedeného príkladu. Algoritmus
	 * používa na presun jednotlivých tvarov transformácie, ktoré pridáva
	 * k sérii prípadných jestvujúcich transformácií. Nepresúva tvary
	 * kresby priamo (fyzicky). Metódy {@link #daj(int) daj}
	 * a {@link #dajPôvodný(int) dajPôvodný} vrátia neposunuté tvary.
	 * Posun sa prejaví až na tvaroch vrátených metódou {@link 
	 * #dajVýsledný(int) dajVýsledný}.<br
	 * /> <br
	 * />V niektorých prípadoch nemusí byť výsledok celej transformácie
	 * v súlade s očakávaniami, pretože môžu nastať nežiaduce interakcie
	 * s jestvujúcimi transformáciami niektorých tvarov. V takom prípade
	 * odporúčame vybrané (prípadne všetky) tvary {@linkplain 
	 * #prepíšTvar(int, Shape) prepísať} ich {@linkplain #dajVýsledný(int)
	 * transformovanými} verziami (spolu s tým {@linkplain 
	 * #prepíšAtribút(int, String, Object) vymazať atribút} ‚transform‘ –
	 * zadaním hodnoty {@code valnull}) a až potom ich posunúť.
	 * <b>Upozornenie,</b> po tejto transformácii nemusí (a pravdepodobne
	 * ani nebude) metóda {@link #rozmerKresby() rozmerKresby} vracať
	 * korektnú hodnotu. Ak potrebujete poznať veľkosť kresby, musíte ju
	 * zistiť ihneď po prečítaní SVG súboru, skôr než vykonáte zásahy do
	 * vnútorných stavov inštancie triedy {@code currSVGPodpora}.</p>
	 * 
	 * @return {@link Bod bod} so súradnicami stredu kresby
	 */
	public Bod stredKresby()
	{
		int počet = počet();
		Rectangle2D hranice = new Rectangle2D.Double();

		for (int i = 0; i < počet; ++i)
		{
			Shape tvar = dajVýsledný(i);
			hranice.add(tvar.getBounds2D());
		}

		return new Bod(
			Svet.prepočítajSpäťX((hranice.getWidth() / 2) - hranice.getX()),
			Svet.prepočítajSpäťY((hranice.getHeight() / 2) - hranice.getY()));
	}


	/**
	 * <p>Vypočíta aktuálny rozmer kresby (šírku a výšku) a vráti ho
	 * v prvých dvoch prvkoch poľa typu {@code typedouble}.</p>
	 * 
	 * <!--p>Doplnkou informáciou v ďalších dvoch prvkoch poľa je miera
	 * vysunutia ľavého spodného rohu kresby od stredu súradnicovej sústavy.
	 * (Vrátené súradnice sú prepočítané do súradnicového priestoru rámca.
	 * O súradnicových priestoroch sa podrobnejšie píše napríklad v opisoch
	 * metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
	 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
	 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
	 * v celej dokumentácii.)</p-->
	 * 
	 * <!--p><b>Príklad:</b></p>
	 * 
	 * <p>TODO?</p>
	 * 
	 * <pre CLASS="example">
		</pre-->
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nemusí fungovať
	 * spoľahlivo, keď je vnútorný stav kresby upravený niektorými druhmi
	 * transformácií. Ak potrebujete napríklad zistiť veľkosť kresby
	 * prečítanej z SVG súboru, tak je najlepšie ho zisťovať ihneď po
	 * prečítaní súboru (skôr než budú vykonané určité zásahy do vnútorných
	 * stavov inštancie triedy {@code currSVGPodpora}).</p>
	 * 
	 * @return dvojica hodnôt (uložených v prvých dvoch prvkoch poľa)
	 *     udávajúcich rozmer kresby<!-- s doplnkovou informáciou o vysunutí
	 *     ľavého spodného rohu kresby od stredu súradnicovej sústavy-->
	 */
	public double[] rozmerKresby()
	{
		int počet = počet();
		Rectangle2D hranice = new Rectangle2D.Double();

		for (int i = 0; i < počet; ++i)
		{
			Shape tvar = dajVýsledný(i);
			hranice.add(tvar.getBounds2D());
		}

		return new double[] {hranice.getWidth(), hranice.getHeight()
			// , hranice.getX(), hranice.getY()
		};
	}


	/**
	 * <p>Poskytne netransformovaný tvar uložený vo vnútornom zásobníku
	 * tejto inštancie so zadaným „poradovým číslom,“ respektíve indexom,
	 * to znamená, že nula označuje prvý tvar v zásobníku. Ak je zadaný
	 * index záporný, metóda bude hľadať tvar od konca zásobníka, to
	 * znamená, že index {@code num-1} označuje posledný tvar vložený do
	 * vnútorného zásobníka. Ak index ani po úprave zo zápornej hodnoty
	 * na kladnú neukazuje na jestvujúci tvar, to znamená, že jeho
	 * hodnota je mimo rozsahu {@code num0} až {@link #počet()
	 * počet()}{@code  - }{@code num1}, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @return netransformovaný tvar uložený vo vnútornom zásobníku pod
	 *     zadaným indexom alebo {@code valnull}
	 */
	public Shape daj(int index)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			return tvary.get(index).tvar;
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Shape dajPôvodný(int index) { return daj(index); }

	/** <p><a class="alias"></a> Alias pre {@link #daj(int) daj}.</p> */
	public Shape dajPovodny(int index) { return daj(index); }

	/**
	 * <p>Poskytne transformovaný tvar, ktorý je uložený vo vnútornom
	 * zásobníku tejto inštancie pod zadaným „poradovým číslom,“
	 * respektíve indexom (čiže nula označuje prvý tvar v zásobníku).
	 * Táto metóda pracuje s vnútorne definovaným atribútom ‚transform‘,
	 * avšak spracúva iba 2D transformácie (ako všetky súčasti tejto
	 * triedy).
	 * Ak uvedený atribút nie je definovaný, tak je výstup tejto metódy
	 * zhodný s výstupom metódy {@link #daj(int) daj}
	 * (a teda aj {@link #dajPôvodný(int) dajPôvodný}).</p>
	 * 
	 * <p>Tiež platia rovnaké informácie, aké sú uvedené v opise metódy
	 * {@link #daj(int) daj}, čiže: Ak je zadaný index záporný, metóda
	 * hľadá tvar od konca zásobníka, to znamená, že hodnota indexu
	 * {@code num-1} označuje posledný tvar vložený do vnútorného
	 * zásobníka. Ak index ani po úprave z prípadnej zápornej hodnoty
	 * na kladnú neukazuje na jestvujúci tvar, to znamená, že jeho
	 * hodnota je mimo rozsahu {@code num0} až {@link #počet()
	 * počet()}{@code  - }{@code num1}, tak táto metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @return tvar uložený vo vnútornom zásobníku pod zadaným indexom
	 *     transformovaný podľa hodnoty vnútorného atribútu ‚transform‘
	 *     (ak má atribút definovanú hodnotu, inak je vrátený
	 *     netransforomvaný tvar), alebo {@code valnull} ak tvar so zadaným
	 *     indexom nejestvuje
	 */
	public Shape dajVýsledný(int index)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
		{
			Tvar tvar = tvary.get(index);
			if (null == tvar.transformácie)
			{
				String hodnota = tvar.atribúty.get("transform");
				if (null != hodnota)
					tvar.transformácie = reťazceNaTransformácie(hodnota);
			}
			return dajVýsledný(tvar.tvar, null, tvar.transformácie);
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, String, String[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index) { return dajVýsledný(index); }


	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int)
	 * dajVýsledný}, ale namiesto prípadných vnútorne definovaných
	 * transformácií použije 2D transformácie zadané v argumentoch
	 * v tvare reťazcov v súlade so špecifikáciou SVG. Výsledný tvar
	 * bude transformovaný zloženou 2D transformáciou zostavenej
	 * zo všetkých rozpoznaných 2D transformácií obsiahnutých v zadaných
	 * argumentoch {@code transformácia} a {@code transformácie}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácia povinná transformácia (2D) v textovom tvare;
	 *     podľa špecifikácie SVG môže obsahovať i niekoľko transformácií
	 *     uvedených za sebou
	 * @param transformácie ďalšie (nepovinné) transformácie (2D),
	 *     z ktorých každá môže obsahovať jednu alebo viac transformácií
	 *     (2D – podľa špecifikácie SVG)
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadanej transformácie alebo
	 *     transformácií alebo {@code valnull} ak tvar so zadaným indexom
	 *     nejestvuje
	 */
	public Shape dajVýsledný(int index, String transformácia,
		String... transformácie)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
		{
			Transformácia[] transformácie2 = reťazceNaTransformácie(
				transformácia, transformácie);
			return dajVýsledný(tvary.get(index).tvar,
				null, transformácie2);
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, String, String[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, String transformácia,
		String... transformácie)
	{ return dajVýsledný(index, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int,
	 * String, String[]) dajVýsledný(index, transformácia[,
	 * transformácie])}, ale transformácie sú zadané v tvare poľa
	 * reťazcov.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácie pole reťazcových prvkov, z ktorých každý
	 *     môže obsahovať jednu alebo viac transformácií (2D – podľa
	 *     špecifikácie SVG)
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadaných transformácií alebo
	 *     {@code valnull} ak tvar so zadaným indexom nejestvuje
	 */
	public Shape dajVýsledný(int index, String[] transformácie)
	{ return dajVýsledný(index, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, String[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, String[] transformácie)
	{ return dajVýsledný(index, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int)
	 * dajVýsledný}, ale namiesto prípadných vnútorne definovaných
	 * transformácií použije transformácie zadané vo forme objektov
	 * triedy {@link AffineTransform AffineTransform}, čiže v tomto
	 * prípade je podľa typu objektu zrejmé, že ide o 2D transformácie.
	 * Výsledný tvar bude transformovaný transformáciou zloženou
	 * zo všetkých zadaných transformácií.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácia povinný objekt transformácia typu {@link 
	 *     AffineTransform AffineTransform}
	 * @param transformácie ďalšie (nepovinné) objekty transformácií typu
	 *     {@link AffineTransform AffineTransform}
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadanej transformácie alebo
	 *     transformácií alebo {@code valnull} ak tvar so zadaným indexom
	 *     nejestvuje
	 */
	public Shape dajVýsledný(int index, AffineTransform transformácia,
		AffineTransform... transformácie)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			return dajVýsledný(tvary.get(index).tvar,
				transformácia, transformácie);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, AffineTransform, AffineTransform[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, AffineTransform transformácia,
		AffineTransform... transformácie)
	{ return dajVýsledný(index, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int,
	 * AffineTransform, AffineTransform[]) dajVýsledný(index,
	 * transformácia[, transformácie])}, ale transformácie sú zadané
	 * v tvare poľa objektov triedy {@link AffineTransform
	 * AffineTransform}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácie pole objektov typu {@link AffineTransform
	 *     AffineTransform}
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadaných transformácií alebo
	 *     {@code valnull} ak tvar so zadaným indexom nejestvuje
	 */
	public Shape dajVýsledný(int index, AffineTransform[] transformácie)
	{ return dajVýsledný(index, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, AffineTransform[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, AffineTransform[] transformácie)
	{ return dajVýsledný(index, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int)
	 * dajVýsledný}, ale namiesto prípadných vnútorne definovaných
	 * transformácií použije transformácie zadané vo forme objektov
	 * triedy {@link Transformácia Transformácia} (ktorá slúži na
	 * uchovanie informácií o rozpoznaných 2D SVG transformáciách).
	 * Výsledný tvar bude transformovaný transformáciou zloženou zo
	 * všetkých zadaných transformácií.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácia povinný objekt transformácia typu {@link 
	 *     Transformácia Transformácia}
	 * @param transformácie ďalšie (nepovinné) objekty transformácií typu
	 *     {@link Transformácia Transformácia}
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadanej transformácie alebo
	 *     transformácií alebo {@code valnull} ak tvar so zadaným indexom
	 *     nejestvuje
	 */
	public Shape dajVýsledný(int index, Transformácia transformácia,
		Transformácia... transformácie)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			return dajVýsledný(tvary.get(index).tvar,
				transformácia, transformácie);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, Transformácia, Transformácia[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, Transformácia transformácia,
		Transformácia... transformácie)
	{ return dajVýsledný(index, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(int,
	 * Transformácia, Transformácia[]) dajVýsledný(index,
	 * transformácia[, transformácie])}, ale transformácie sú zadané
	 * v tvare poľa objektov triedy {@link Transformácia
	 * Transformácia}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param transformácie pole objektov typu {@link Transformácia
	 *     Transformácia}
	 * @return tvar, ktorý je uložený vo vnútornom zásobníku pod zadaným
	 *     indexom a transformovaný podľa zadaných transformácií alebo
	 *     {@code valnull} ak tvar so zadaným indexom nejestvuje
	 */
	public Shape dajVýsledný(int index, Transformácia[] transformácie)
	{ return dajVýsledný(index, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, Transformácia[]) dajVýsledný}.</p> */
	public Shape dajVysledny(int index, Transformácia[] transformácie)
	{ return dajVýsledný(index, transformácie); }


	/**
	 * <p>Táto metóda umožňuje transformovať ľubovoľný tvar ({@link Shape
	 * Shape}) Javy podľa 2D transformácií zadaných v tvare reťazcov
	 * v súlade so špecifikáciou SVG.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácia povinná 2D transformácia v textovom tvare;
	 *     podľa špecifikácie SVG môže obsahovať i niekoľko transformácií
	 *     uvedených za sebou
	 * @param transformácie ďalšie (nepovinné) transformácie (2D),
	 *     z ktorých každá môže obsahovať jednu alebo viac transformácií
	 *     (podľa špecifikácie SVG)
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public Shape dajVýsledný(Shape tvar, String transformácia,
		String... transformácie)
	{
		// Poznámka: Metóda nemôže byť statická, lebo je závislá
		//     od tabuľky jednotiek.
		if (null != tvar)
		{
			Transformácia[] transformácie2 = reťazceNaTransformácie(
				transformácia, transformácie);
			return dajVýsledný(tvar, null, transformácie2);
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(int, String, String[]) dajVýsledný}.</p> */
	public Shape dajVysledny(Shape tvar, String transformácia,
		String... transformácie)
	{ return dajVýsledný(tvar, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(Shape,
	 * String, String[]) dajVýsledný(index, transformácia[,
	 * transformácie])}, ale transformácie sú zadané v tvare poľa
	 * reťazcov.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácie pole reťazcových prvkov, z ktorých každý
	 *     môže obsahovať jednu alebo viac transformácií (2D – podľa
	 *     špecifikácie SVG)
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public Shape dajVýsledný(Shape tvar, String[] transformácie)
	{ return dajVýsledný(tvar, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(Shape, String[]) dajVýsledný}.</p> */
	public Shape dajVysledny(Shape tvar, String[] transformácie)
	{ return dajVýsledný(tvar, transformácie); }

	/**
	 * <p>Táto metóda umožňuje transformovať ľubovoľný tvar ({@link Shape
	 * Shape}) Javy podľa 2D transformácií zadaných vo forme objektov
	 * typu {@link Transformácia Transformácia}.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácia povinný objekt transformácia typu {@link 
	 *     Transformácia Transformácia}
	 * @param transformácie ďalšie (nepovinné) objekty transformácií typu
	 *     {@link Transformácia Transformácia}
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public static Shape dajVýsledný(Shape tvar, Transformácia
		transformácia, Transformácia... transformácie)
	{
		if (null != tvar)
		{
			AffineTransform zložená = new AffineTransform();
			if (null != transformácia)
			{
				// zložená.preConcatenate(transformácia.daj());
				zložená.concatenate(transformácia.daj());
			}
			if (null != transformácie)
				for (Transformácia t : transformácie)
					if (null != t)
					{
						// zložená.preConcatenate(t.daj());
						zložená.concatenate(t.daj());
					}
			return zložená.createTransformedShape(tvar);
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(Shape, Transformácia, Transformácia[]) dajVýsledný}.</p> */
	public static Shape dajVysledny(Shape tvar, Transformácia
		transformácia, Transformácia... transformácie)
	{ return dajVýsledný(tvar, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(Shape,
	 * Transformácia, Transformácia[]) dajVýsledný(index, transformácia[,
	 * transformácie])}, ale transformácie sú zadané v tvare poľa objektov
	 * typu {@link Transformácia Transformácia}.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácie pole objektov typu {@link Transformácia
	 *     Transformácia}
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public static Shape dajVýsledný(Shape tvar,
		Transformácia[] transformácie)
	{ return dajVýsledný(tvar, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(Shape, Transformácia[]) dajVýsledný}.</p> */
	public static Shape dajVysledny(Shape tvar,
		Transformácia[] transformácie)
	{ return dajVýsledný(tvar, transformácie); }

	/**
	 * <p>Táto metóda umožňuje transformovať ľubovoľný tvar ({@link Shape
	 * Shape}) Javy podľa 2D transformácií zadaných vo forme objektov
	 * typu {@link AffineTransform AffineTransform}.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácia povinný objekt transformácia typu {@link 
	 *     AffineTransform AffineTransform}
	 * @param transformácie ďalšie (nepovinné) objekty transformácií typu
	 *     {@link AffineTransform AffineTransform}
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public static Shape dajVýsledný(Shape tvar, AffineTransform
		transformácia, AffineTransform... transformácie)
	{
		if (null != tvar)
		{
			AffineTransform zložená = new AffineTransform();
			if (null != transformácia)
			{
				// zložená.preConcatenate(transformácia);
				zložená.concatenate(transformácia);
			}
			if (null != transformácie)
				for (AffineTransform t : transformácie)
					if (null != t)
					{
						// zložená.preConcatenate(t);
						zložená.concatenate(t);
					}
			return zložená.createTransformedShape(tvar);
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(Shape, AffineTransform, AffineTransform[]) dajVýsledný}.</p> */
	public static Shape dajVysledny(Shape tvar, AffineTransform
		transformácia, AffineTransform... transformácie)
	{ return dajVýsledný(tvar, transformácia, transformácie); }

	/**
	 * <p>Táto metóda pracuje rovnako ako metóda {@link #dajVýsledný(Shape,
	 * AffineTransform, AffineTransform[]) dajVýsledný(index,
	 * transformácia[, transformácie])}, ale transformácie sú zadané
	 * v tvare poľa objektov typu {@link AffineTransform
	 * AffineTransform}.</p>
	 * 
	 * @param tvar tvar javy – objekt typu {@link Shape Shape} (môže ísť
	 *     aj o niektorý tvar generovaný robotom)
	 * @param transformácie pole objektov typu {@link AffineTransform
	 *     AffineTransform}
	 * @return vstupný tvar (z parametra {@code tvar}) transformovaný
	 *     podľa zadaných transformácií (ak nastane chyba, tak je
	 *     návratovou hodnotou {@code valnull})
	 */
	public static Shape dajVýsledný(Shape tvar,
		AffineTransform[] transformácie)
	{ return dajVýsledný(tvar, null, transformácie); }

	/** <p><a class="alias"></a> Alias pre {@link #dajVýsledný(Shape, AffineTransform[]) dajVýsledný}.</p> */
	public static Shape dajVysledny(Shape tvar,
		AffineTransform[] transformácie)
	{ return dajVýsledný(tvar, transformácie); }


	/**
	 * <p>Prepíše tvar určený indexom v rámci vnútorného zásobníka tvarov.
	 * Všetky vlastnosti (atribúty) súvisiace s tvarom zostanú zachované.
	 * Ak je zadaný index záporný, tak metóda prepíše tvar počítaný
	 * od konca zásobníka, to znamená, že index {@code num-1}
	 * označuje posledný tvar v zásobníku. Ak kladný alebo záporný index
	 * ukáže na tvar mimo zásobníka, tak metóda nevykoná žiadnu akciu.
	 * Ak je hodnota argumentu {@code tvar} rovná {@code valnull}, tak
	 * metóda tiež nevykoná žiadnu akciu.</p>
	 * 
	 * @param index index tvaru vo vnútornom zásobníku (hodnoty mimo
	 *     platného rozsahu sú ignorované)
	 * @param tvar nový tvar, ktorým má byť nahradený jestvujúci tvar
	 *     vo vnútornom zásobníku tvarov (hodnota {@code valnull} je
	 *     ignorovaná)
	 */
	public void prepíšTvar(int index, Shape tvar)
	{
		if (null == tvar) return;
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			tvary.get(index).tvar = tvar;
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepíšTvar(int, Shape) prepíšTvar}.</p> */
	public void prepisTvar(int index, Shape tvar) { prepíšTvar(index, tvar); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšTvar(int, Shape) prepíšTvar}.</p> */
	public void nahraďTvar(int index, Shape tvar) { prepíšTvar(index, tvar); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšTvar(int, Shape) prepíšTvar}.</p> */
	public void nahradTvar(int index, Shape tvar) { prepíšTvar(index, tvar); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšTvar(int, Shape) prepíšTvar}.</p> */
	public void nastavTvar(int index, Shape tvar) { prepíšTvar(index, tvar); }


	/**
	 * <p>Poskytne hodnotu atribútu asociovaného s tvarom so zadaným
	 * indexom. Ak je zadaný index záporný, metóda bude hľadať atribút
	 * pre tvar od konca zásobníka, to znamená, že index {@code num-1}
	 * označuje posledný tvar vložený do vnútorného zásobníka. Ak
	 * požadovaný atribút (alebo tvar) nejestvuje, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @param meno názov požadovaného atribútu asociovaného s tvarom
	 * @return reťazec s hodnotou požadovaného atribútu alebo
	 *     {@code valnull}
	 */
	public String dajAtribút(int index, String meno)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			return tvary.get(index).atribúty.get(meno);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajAtribút(int, String) dajAtribút}.</p> */
	public String dajAtribut(int index, String meno)
	{ return dajAtribút(index, meno); }

	/**
	 * <p>Vráti XML/SVG reprezentáciu tvaru uloženého vo vnútornom
	 * zásobníku tejto inštancie so zadaným poradovým číslom, resp.
	 * indexom – nula označuje prvý tvar. (Ide vlastne o export tvaru
	 * do reťazca.)
	 * <!--   -->
	 * Ak je zadaný index záporný, metóda bude hľadať tvar od konca
	 * zásobníka, to znamená, že index {@code num-1} označuje posledný
	 * tvar vložený do vnútorného zásobníka. Index musí po tejto úprave
	 * ukazovať na jestvujúci tvar, to znamená, že jeho hodnota musí byť
	 * v rozsahu {@code num0} až {@link #počet() počet()}{@code 
	 *  - }{@code num1}, inak metóda vráti hodnotu {@code valnull}.
	 * <!--   -->
	 * (Vkladanie/vytváranie nových tvarov do vnútorného zásobníka
	 * tejto inštancie s použitím XML/SVG reprezentácie je možné, okrem
	 * {@linkplain #čítaj(String) prečítania priamo z SVG alebo HTML
	 * súboru}, aj metódou {@link #pridajSVG(String)
	 * pridajSVG(xmlSVG)}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Pri exporte nie sú využité
	 * vlastnosti SVG 2.0, najmä takzvaný „bearing“ – aktuálne pootočenie,
	 * ktorý má v budúcnosti slúžiť na implementáciu korytnačej grafiky
	 * v rámci štandardu SVG. Dôvodom nepoužitia novšej verzie štandardu
	 * je to, že súčasný softvér tento štandard nie je schopný spracovať.
	 * To znamená, že takto exportované údaje by neboli v praxi
	 * použiteľné.</p>
	 * 
	 * @param index index požadovaného tvaru z vnútorného zásobníka
	 * @return textová XML/SVG reprezentácia tvaru uloženého vo vnútornom
	 *     zásobníku pod zadaným indexom alebo {@code valnull}
	 */
	public String dajSVG(int index)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
			return dajSVG(tvary.get(index));
		return null;
	}

	/**
	 * <p>Pokúsi sa zistiť farbu výplne tvaru asociovaného so zadaným
	 * indexom. Možnosti metódy sú obmedzené (pozri informácie v opise
	 * triedy). Ak sa farbu nepodarí zistiť, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * <p>Táto metóda vracia ako predvolenú farbu (to jest, ak nie je
	 * definovaný atribút {@code fill}) čiernu.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na zistenie hodnoty atribútu metódu {@link 
	 *     #dajAtribút(int, String) dajAtribút}, takže pre hodnotu indexu
	 *     pri tejto metóde platí to isté ako pre hodnotu indexu pri
	 *     metóde {@link #dajAtribút(int, String) dajAtribút})
	 * @return objekt typu {@link Farba Farba} alebo {@code valnull}
	 */
	public Farba farbaVýplne(int index)
	{
		String farba = dajAtribút(index, "fill");
		if (null != farba)
		{
			if ("none".equalsIgnoreCase(farba)) return null;
			return reťazecNaFarbu(farba, dajAtribút(index,
				"fill-opacity"));
		}
		// return null;
		return reťazecNaFarbu("black", dajAtribút(index, "fill-opacity"));
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaVýplne(int) farbaVýplne}.</p> */
	public Farba farbaVyplne(int index) { return farbaVýplne(index); }

	/**
	 * <p>Pokúsi sa zistiť farbu čiary tvaru asociovaného so zadaným
	 * indexom. Možnosti metódy sú obmedzené (pozri informácie v opise
	 * triedy). Ak sa farbu nepodarí zistiť, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na zistenie hodnoty atribútu metódu {@link 
	 *     #dajAtribút(int, String) dajAtribút}, takže pre hodnotu indexu
	 *     pri tejto metóde platí to isté ako pre hodnotu indexu pri metóde
	 *     {@link #dajAtribút(int, String) dajAtribút})
	 * @return objekt typu {@link Farba Farba} alebo {@code valnull}
	 */
	public Farba farbaČiary(int index)
	{
		String farba = dajAtribút(index, "stroke");
		if (null != farba)
		{
			if ("none".equalsIgnoreCase(farba)) return null;
			return reťazecNaFarbu(farba, dajAtribút(index,
				"stroke-opacity"));
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaČiary(int) farbaČiary}.</p> */
	public Farba farbaCiary(int index) { return farbaČiary(index); }

	/**
	 * <p>Pokúsi sa zistiť hrúbku čiary tvaru asociovaného so zadaným
	 * indexom. Možnosti metódy sú obmedzené (pozri informácie v opise
	 * triedy). Ak sa hrúbku nepodarí zistiť, tak metóda vráti hodnotu
	 * {@link Double#NaN}.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na zistenie hodnoty atribútu metódu {@link 
	 *     #dajAtribút(int, String) dajAtribút}, takže pre hodnotu indexu
	 *     pri tejto metóde platí to isté ako pre hodnotu indexu pri metóde
	 *     {@link #dajAtribút(int, String) dajAtribút})
	 * @return objekt typu {@link Farba Farba} alebo {@code valnull}
	 */
	public double hrúbkaČiary(int index)
	{
		String hodnota = dajAtribút(index, "stroke-width");
		if (null != hodnota)
		{
			if ("none".equalsIgnoreCase(hodnota)) return Double.NaN;
			return reťazecNaČíslo(hodnota);
		}
		return Double.NaN;
	}

	/** <p><a class="alias"></a> Alias pre {@link #hrúbkaČiary(int) hrúbkaČiary}.</p> */
	public double hrubkaCiary(int index) { return hrúbkaČiary(index); }


	/**
	 * <p>Prepíše, vloží novú alebo odstráni hodnotu atribútu asociovaného
	 * s tvarom so zadaným indexom. Ak je index záporný, metóda bude
	 * hľadať tvar od konca zásobníka, to znamená, že index {@code num-1}
	 * označuje posledný tvar vložený do vnútorného zásobníka. Okrem toho
	 * musia byť splnené nasledujúce podmienky: {@code index} musí
	 * ukazovať na jestvujúci tvar (po úprave prípadnej zápornej hodnoty)
	 * a {@code meno} nesmie byť {@code valnull} ani prázdny reťazec,
	 * inak nebude mať volanie tejto metódy žiadny efekt. Ak je
	 * {@code hodnota} rovná {@code valnull}, tak je atribút odstránený,
	 * v opačnom prípade je za novú hodnotu považovaný reťazec vytvorený
	 * z objektu volaním metódy {@code hodnota.}{@link Object#toString()
	 * toString()}.</p>
	 * 
	 * <p class="caution"><b>Pozor!</b> Atribúty, ktoré sú kľúčové pri
	 * vyjadrení konkrétneho tvaru (napr. {@code cx}, {@code cy}, {@code r}
	 * pri kružnici, {@code points} pri polygóne, {@code d} pri ceste
	 * a podobne) sú počas prevodu do XML/SVG tvaru nahradené konkrétnymi
	 * hodnotami. Preto ich nastavenie/prepísanie/vymazanie touto metódou
	 * nemá zmysel.</p>
	 * 
	 * @param index index tvaru vo vnútornom zásobníku
	 * @param meno názov atribútu asociovaného s tvarom
	 * @param hodnota nová hodnota atribútu alebo {@code valnull}
	 */
	public void prepíšAtribút(int index, String meno, Object hodnota)
	{
		if (index < 0) index = tvary.size() + index;
		if (null != meno && !meno.isEmpty() &&
			index >= 0 && index < tvary.size())
		{
			Tvar tvar = tvary.get(index);
			if (null == hodnota)
				tvar.atribúty.remove(meno);
			else
				tvar.atribúty.put(meno, hodnota.toString());
			if (meno.equals("transform")) tvar.transformácie = null;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepíšAtribút(int, String, Object) prepíšAtribút}.</p> */
	public void prepisAtribut(int index, String meno, Object hodnota)
	{ prepíšAtribút(index, meno, hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšAtribút(int, String, Object) prepíšAtribút}.</p> */
	public void nahraďAtribút(int index, String meno, Object hodnota)
	{ prepíšAtribút(index, meno, hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšAtribút(int, String, Object) prepíšAtribút}.</p> */
	public void nahradAtribut(int index, String meno, Object hodnota)
	{ prepíšAtribút(index, meno, hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšAtribút(int, String, Object) prepíšAtribút}.</p> */
	public void nastavAtribút(int index, String meno, Object hodnota)
	{ prepíšAtribút(index, meno, hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #prepíšAtribút(int, String, Object) prepíšAtribút}.</p> */
	public void nastavAtribut(int index, String meno, Object hodnota)
	{ prepíšAtribút(index, meno, hodnota); }


	/**
	 * <p>Nastaví novú farbu výplne tvaru asociovanému so zadaným indexom.
	 * (Metóda nastavuje asociovanému tvaru atribúty {@code fill}
	 * a {@code fill-opacity}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param farba nová farba výplne; ak je zadaná hodnota
	 *     {@code valnull} tak sú atribúty {@code fill}
	 *     a {@code fill-opacity} tvaru odstránené, inak sú na prevod farby
	 *     do reťazcovej podoby použité metódy {@link #farbaNaReťazec(Color,
	 *     boolean) farbaNaReťazec(farba, ignorujAlfu)} (pre atribút
	 *     {@code fill} a {@link #alfaNaReťazec(Color) alfaNaReťazec(farba)}
	 *     (pre atribút {@code fill-opacity})
	 */
	public void farbaVýplne(int index, Color farba)
	{
		if (null == farba)
		{
			prepíšAtribút(index, "fill", null);
			prepíšAtribút(index, "fill-opacity", null);
		}
		else if (Farebnosť.žiadna == farba)
		{
			prepíšAtribút(index, "fill", "none");
			prepíšAtribút(index, "fill-opacity", null);
		}
		else
		{
			prepíšAtribút(index, "fill", farbaNaReťazec(farba, true));
			if (255 == farba.getAlpha())
				prepíšAtribút(index, "fill-opacity", null);
			else
				prepíšAtribút(index, "fill-opacity",
					alfaNaReťazec(farba));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaVýplne(int, Color) farbaVýplne}.</p> */
	public void farbaVyplne(int index, Color farba) { farbaVýplne(index, farba); }

	/**
	 * <p>Nastaví novú farbu čiary tvaru asociovanému so zadaným indexom.
	 * (Metóda nastavuje asociovanému tvaru atribúty {@code stroke}
	 * a {@code stroke-opacity}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param farba nová farba čiary; ak je zadaná hodnota
	 *     {@code valnull} tak sú atribúty {@code stroke}
	 *     a {@code stroke-opacity} tvaru odstránené, inak sú na prevod
	 *     farby do reťazcovej podoby použité metódy {@link 
	 *     #farbaNaReťazec(Color, boolean) farbaNaReťazec(farba,
	 *     ignorujAlfu)} (pre atribút {@code stroke} a {@link 
	 *     #alfaNaReťazec(Color) alfaNaReťazec(farba)}
	 *     (pre atribút {@code stroke-opacity})
	 */
	public void farbaČiary(int index, Color farba)
	{
		if (null == farba)
		{
			prepíšAtribút(index, "stroke", null);
			prepíšAtribút(index, "stroke-opacity", null);
		}
		else
		{
			prepíšAtribút(index, "stroke", farbaNaReťazec(farba, true));
			if (255 == farba.getAlpha())
				prepíšAtribút(index, "stroke-opacity", null);
			else
				prepíšAtribút(index, "stroke-opacity",
					alfaNaReťazec(farba));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaČiary(int, Color) farbaČiary}.</p> */
	public void farbaCiary(int index, Color farba) { farbaČiary(index, farba); }

	/**
	 * <p>Nastaví novú hrúbku čiary tvaru asociovanému so zadaným indexom.
	 * (Metóda nastavuje asociovanému tvaru atribút {@code stroke-width}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param hrúbka nová hrúbka čiary
	 */
	public void hrúbkaČiary(int index, double hrúbka)
	{
		if (!mapaJednotiek.isEmpty())
		{
			Double prevod = mapaJednotiek.get("px");
			Double posun = mapaPosunov.get("px");
			if (null == posun) posun = 0.0;

			if (null != prevod && 1.0 != prevod &&
				0.0 != prevod && Double.isFinite(prevod))
			{
				prepíšAtribút(index, "stroke-width",
					((hrúbka - posun) / prevod) + "px");
				return;
			}
			else if (0.0 != posun)
			{
				prepíšAtribút(index, "stroke-width",
					(hrúbka - posun) + "px");
				return;
			}
		}

		prepíšAtribút(index, "stroke-width", hrúbka + "px");
	}

	/** <p><a class="alias"></a> Alias pre {@link #hrúbkaČiary(int, double) hrúbkaČiary}.</p> */
	public void hrubkaCiary(int index, double hrúbka) { hrúbkaČiary(index, hrúbka); }


	/**
	 * <p>Vráti zoznam transformácií v poli objektov typu
	 * {@link Transformácia Transformácia}, ktoré sú priradené k indexom
	 * určenému tvaru prostredníctvom vnútorného atribútu ‚transform‘.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre hodnotu
	 *     indexu platí to isté ako pre hodnotu indexu pri metóde {@link 
	 *     #dajAtribút(int, String) dajAtribút}; aj keď táto metóda
	 *     uvedenú metódu vnútorne nepoužíva)
	 * @return pole údajov o transformáciách reprezentovaných objektmi
	 *     {@link Transformácia Transformácia} s dĺžkou rovnou počtu
	 *     transformácií, ktoré sa podarilo rozpoznať vo vnútornom
	 *     atribúte ‚transform‘ (pole môže mať aj nulovú dĺžku, ak by sa
	 *     nepodarilo rozpoznať ani jednu transformáciu) alebo {@code 
	 *     valnull}, ak požadovaný tvar nejestvuje alebo ak tvar nemá
	 *     definovaný atribút ‚transform‘
	 */
	public Transformácia[] transformácie(int index)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
		{
			Tvar tvar = tvary.get(index);
			if (null == tvar.transformácie)
			{
				String hodnota = tvar.atribúty.get("transform");
				if (null != hodnota)
					tvar.transformácie = reťazceNaTransformácie(hodnota);
			}
			return tvar.transformácie;
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int) transformácie}.</p> */
	public Transformacia[] transformacie(int index)
	{
		Transformácia[] transformácie = transformácie(index);
		if (null == transformácie) return null;
		int dĺžka = transformácie.length;
		Transformacia[] transformacie = new Transformacia[dĺžka];
		for (int i = 0; i < dĺžka; ++i)
			transformacie[i] = new Transformacia(transformácie[i]);
		return transformacie;
	}

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa hodnoty zadaného
	 * reťazca alebo série reťazcov.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný reťazec určujúci novú transformáciu
	 *     alebo transformácie vkladané do atribútu ‚transform‘; ak je
	 *     zadaná pretypovaná hodnota {@code (}{@link String String}{@code 
	 *     )}{@code valnull} a nie sú zadané nepovinné parametre {@code 
	 *     transformácie}, tak sú transformácie tvaru odstránené
	 * @param transformácie nepovinné transformácie (do tohto parametra
	 *     môže byť zadaná aj pretypovaná hodnota {@code (}{@link String
	 *     String}{@code [])}{@code valnull} – výsledok potom závisí od
	 *     hodnoty povinného parametra {@code transformácia})
	 */
	public void transformácie(int index, String transformácia,
		String... transformácie)
	{
		if (null == transformácia && (null == transformácie ||
			0 == transformácie.length))
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformácia.trim());
			}

			if (null != transformácie)
				for (String jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(jedna.trim());
					}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, String, String[]) transformácie}.</p> */
	public void transformacie(int index, String transformácia,
		String... transformácie)
	{ transformácie(index, transformácia, transformácie); }

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa hodnôt prvkov
	 * zadaného poľa reťazcov.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole reťazcov určujúcich novú hodnotu
	 *     atribútu transformácií; ak je zadaná pretypovaná hodnota {@code 
	 *     (}{@link String String}{@code [])}{@code valnull}, tak sú
	 *     transformácie tvaru odstránené
	 */
	public void transformácie(int index, String[] transformácie)
	{
		if (null == transformácie)
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie argumentov
			StringBuffer spojené = new StringBuffer();

			for (String jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(jedna.trim());
				}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, String[]) transformácie}.</p> */
	public void transformacie(int index, String[] transformácie)
	{ transformácie(index, transformácie); }

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa zadaného objektu alebo
	 * série objektov afinných transformácií. Táto metóda používa vnútorne
	 * na prevod metódu {@link #transformáciuNaReťazec(AffineTransform)
	 * transformáciuNaReťazec}, takže platia aj informácie z opisu
	 * uvedenej metódy (najmä to o prevode do maticového tvaru –
	 * {@code matrix(…)}). (Na spôsob výstupu tejto metódy má vplyv aj
	 * hodnota príznaku {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný objekt určujúci novú transformáciu
	 *     vkladanú do atribútu ‚transform‘; ak je zadaná pretypovaná hodnota
	 *     {@code (}{@link AffineTransform AffineTransform}{@code )}{@code 
	 *     valnull} a nie sú zadané nepovinné parametre {@code 
	 *     transformácie}, tak je atribút tvaru ‚transform‘ odstránený
	 * @param transformácie nepovinné transformácie (do tohto parametra
	 *     môže byť zadaná aj pretypovaná hodnota {@code (}{@link 
	 *     AffineTransform AffineTransform}{@code [])}{@code valnull} –
	 *     výsledok potom závisí od hodnoty povinného parametra
	 *     {@code transformácia})
	 */
	public void transformácie(int index, AffineTransform transformácia,
		AffineTransform... transformácie)
	{
		if (null == transformácia && (null == transformácie ||
			0 == transformácie.length))
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformáciuNaReťazec(transformácia));
			}

			if (null != transformácie)
				for (AffineTransform jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(transformáciuNaReťazec(jedna));
					}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, AffineTransform, AffineTransform[]) transformácie}.</p> */
	public void transformacie(int index, AffineTransform transformácia,
		AffineTransform... transformácie)
	{ transformácie(index, transformácia, transformácie); }

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa hodnôt prvkov zadaného
	 * poľa objektov afinných transformácií. Táto metóda používa vnútorne
	 * na prevod metódu {@link #transformáciuNaReťazec(AffineTransform)
	 * transformáciuNaReťazec}, takže platia aj informácie z opisu
	 * uvedenej metódy (najmä to o prevode do maticového tvaru –
	 * {@code matrix(…)}). (Na spôsob výstupu tejto metódy má vplyv aj
	 * hodnota príznaku {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole objektov transformácií určujúcich
	 *     novú hodnotu atribútu transformácií; ak je zadaná pretypovaná
	 *     hodnota {@code (}{@link AffineTransform
	 *     AffineTransform}{@code [])}{@code valnull}, tak je atribút
	 *     tvaru ‚transform‘ odstránený
	 */
	public void transformácie(int index, AffineTransform[] transformácie)
	{
		if (null == transformácie)
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			for (AffineTransform jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(transformáciuNaReťazec(jedna));
				}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, AffineTransform[]) transformácie}.</p> */
	public void transformacie(int index, AffineTransform[] transformácie)
	{ transformácie(index, transformácie); }

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa zadaného objektu alebo
	 * série objektov afinných transformácií. Táto metóda používa vnútorne
	 * na prevod metódu {@link #transformáciuNaReťazec(Transformácia)
	 * transformáciuNaReťazec}, takže platia aj prípadné relevantné
	 * informácie z opisu uvedenej metódy. (Na spôsob výstupu tejto
	 * metódy má vplyv aj hodnota príznaku {@link Transformácia#SVG2
	 * Transformácia.SVG2}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný objekt určujúci novú transformáciu
	 *     vkladanú do atribútu ‚transform‘; ak je zadaná pretypovaná hodnota
	 *     {@code (}{@link Transformácia Transformácia}{@code )}{@code 
	 *     valnull} a nie sú zadané nepovinné parametre {@code 
	 *     transformácie}, tak je atribút tvaru ‚transform‘ odstránený
	 * @param transformácie nepovinné transformácie (do tohto parametra
	 *     môže byť zadaná aj pretypovaná hodnota {@code (}{@link Transformácia
	 *     Transformácia}{@code [])}{@code valnull} – výsledok potom
	 *     závisí od hodnoty povinného parametra {@code transformácia})
	 */
	public void transformácie(int index, Transformácia transformácia,
		Transformácia... transformácie)
	{
		if (null == transformácia && (null == transformácie ||
			0 == transformácie.length))
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformácia.toString());
			}

			if (null != transformácie)
				for (Transformácia jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(jedna.toString());
					}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, Transformácia, Transformácia[]) transformácie}.</p> */
	public void transformacie(int index, Transformácia transformácia,
		Transformácia... transformácie)
	{ transformácie(index, transformácia, transformácie); }

	/**
	 * <p>Nastaví alebo odstráni vnútorný atribút transformácií
	 * (‚transform‘) tvaru určeného indexom podľa hodnôt prvkov zadaného
	 * poľa objektov afinných transformácií. Táto metóda používa vnútorne
	 * na prevod metódu {@link #transformáciuNaReťazec(Transformácia)
	 * transformáciuNaReťazec}, takže platia aj prípadné relevantné
	 * informácie z opisu uvedenej metódy. (Na spôsob výstupu tejto
	 * metódy má vplyv aj hodnota príznaku {@link Transformácia#SVG2
	 * Transformácia.SVG2}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
	 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
	 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
	 * žiadneho objektu, neprítomnosť žiadnej inštancie.
	 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
	 * Pri volaní metódy musí byť pretypovanie prítomné preto,
	 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
	 * metód má volať.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (táto
	 *     metóda používa na vloženie hodnoty atribútu metódu {@link 
	 *     #prepíšAtribút(int, String, Object) prepíšAtribút}, takže pre
	 *     hodnotu indexu pri tejto metóde platí to isté ako pre hodnotu
	 *     indexu pri metóde {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole objektov transformácií určujúcich novú
	 *     hodnotu atribútu transformácií; ak je zadaná pretypovaná hodnota
	 *     {@code (}{@link Transformácia Transformácia}{@code [])}{@code 
	 *     valnull}, tak je atribút tvaru ‚transform‘ odstránený
	 */
	public void transformácie(int index, Transformácia[] transformácie)
	{
		if (null == transformácie)
			prepíšAtribút(index, "transform", null);
		else
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			for (Transformácia jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(jedna.toString());
				}

			if (0 == spojené.length())
				prepíšAtribút(index, "transform", "");
			else
				prepíšAtribút(index, "transform",
					spojené.substring(1));
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformácie(int, Transformácia[]) transformácie}.</p> */
	public void transformacie(int index, Transformácia[] transformácie)
	{ transformácie(index, transformácie); }


	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa zadaného reťazca
	 * alebo série reťazcov.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto triedy,
	 *     napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný reťazec určujúci transformáciu
	 *     alebo transformácie pridávané do atribútu ‚transform‘
	 * @param transformácie ďalšie (nepovinné) transformácie na pridanie
	 */
	public void pridajTransformácie(int index, String transformácia,
		String... transformácie)
	{
		if (null != transformácia || (null != transformácie &&
			0 != transformácie.length))
		{
			// Reťazcový zásobník na spájanie argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformácia.trim());
			}

			if (null != transformácie)
				for (String jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(jedna.trim());
					}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, String, String[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index, String transformácia,
		String... transformácie)
	{ pridajTransformácie(index, transformácia, transformácie); }

	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa hodnôt prvkov
	 * zadaného poľa reťazcov.</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto triedy,
	 *     napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole reťazcov s transformáciami na pridanie
	 */
	public void pridajTransformácie(int index, String[] transformácie)
	{
		if (null != transformácie)
		{
			// Reťazcový zásobník na spájanie argumentov
			StringBuffer spojené = new StringBuffer();

			for (String jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(jedna.trim());
				}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, String[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index, String[] transformácie)
	{ pridajTransformácie(index, transformácie); }

	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa zadaného objektu
	 * alebo série objektov afinných transformácií. Táto metóda používa
	 * vnútorne na prevod metódu
	 * {@link #transformáciuNaReťazec(AffineTransform)
	 * transformáciuNaReťazec}, takže platia aj informácie z opisu
	 * uvedenej metódy (najmä to o prevode do maticového tvaru –
	 * {@code matrix(…)}). (Na spôsob výstupu tejto metódy má vplyv aj
	 * hodnota príznaku {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto
	 *     triedy, napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný objekt určujúci transformáciu
	 *     pridávanú do atribútu ‚transform‘
	 * @param transformácie ďalšie (nepovinné) transformácie na pridanie
	 */
	public void pridajTransformácie(int index,
		AffineTransform transformácia, AffineTransform... transformácie)
	{
		if (null != transformácia || (null != transformácie &&
			0 != transformácie.length))
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformáciuNaReťazec(transformácia));
			}

			if (null != transformácie)
				for (AffineTransform jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(transformáciuNaReťazec(jedna));
					}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, AffineTransform, AffineTransform[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index,
		AffineTransform transformácia, AffineTransform... transformácie)
	{ pridajTransformácie(index, transformácia, transformácie); }

	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa hodnôt prvkov
	 * zadaného poľa objektov afinných transformácií. Táto metóda
	 * používa vnútorne na prevod metódu
	 * {@link #transformáciuNaReťazec(AffineTransform)
	 * transformáciuNaReťazec}, takže platia aj informácie z opisu
	 * uvedenej metódy (najmä to o prevode do maticového tvaru –
	 * {@code matrix(…)}). (Na spôsob výstupu tejto metódy má vplyv aj
	 * hodnota príznaku {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto
	 *     triedy, napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole s afinnými transformáciami na pridanie
	 */
	public void pridajTransformácie(int index,
		AffineTransform[] transformácie)
	{
		if (null != transformácie)
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			for (AffineTransform jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(transformáciuNaReťazec(jedna));
				}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, AffineTransform[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index,
		AffineTransform[] transformácie)
	{ pridajTransformácie(index, transformácie); }

	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa zadaného objektu
	 * alebo série objektov afinných transformácií. Táto metóda
	 * používa vnútorne na prevod metódu
	 * {@link #transformáciuNaReťazec(Transformácia)
	 * transformáciuNaReťazec}, takže platia aj prípadné relevantné
	 * informácie z opisu uvedenej metódy. (Na spôsob výstupu tejto
	 * metódy má vplyv aj hodnota príznaku {@link Transformácia#SVG2
	 * Transformácia.SVG2}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto
	 *     triedy, napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácia povinný objekt určujúci transformáciu
	 *     pridávanú do atribútu ‚transform‘
	 * @param transformácie ďalšie (nepovinné) transformácie na pridanie
	 */
	public void pridajTransformácie(int index,
		Transformácia transformácia, Transformácia... transformácie)
	{
		if (null != transformácia || (null != transformácie &&
			0 != transformácie.length))
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			if (null != transformácia)
			{
				spojené.append(' ');
				spojené.append(transformácia.toString());
			}

			if (null != transformácie)
				for (Transformácia jedna : transformácie)
					if (null != jedna)
					{
						spojené.append(' ');
						spojené.append(jedna.toString());
					}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, Transformácia, Transformácia[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index, Transformácia transformácia,
		Transformácia... transformácie)
	{ pridajTransformácie(index, transformácia, transformácie); }

	/**
	 * <p>Pridá k vnútornému atribútu transformácií (‚transform‘)
	 * tvaru určeného indexom transformácie podľa hodnôt prvkov
	 * zadaného poľa objektov afinných transformácií. Táto metóda
	 * používa vnútorne na prevod metódu {@link 
	 * #transformáciuNaReťazec(Transformácia) transformáciuNaReťazec},
	 * takže platia aj prípadné relevantné informácie z opisu uvedenej
	 * metódy. (Na spôsob výstupu tejto metódy má vplyv aj hodnota
	 * príznaku {@link Transformácia#SVG2 Transformácia.SVG2}.)</p>
	 * 
	 * @param index index tvaru v rámci vnútorného zásobníka (pre
	 *     hodnotu indexu platí to isté, čo pri iných metódach tejto
	 *     triedy, napríklad {@link #prepíšAtribút(int, String, Object)
	 *     prepíšAtribút})
	 * @param transformácie pole objektov transformácií na pridanie
	 */
	public void pridajTransformácie(int index,
		Transformácia[] transformácie)
	{
		if (null != transformácie)
		{
			// Reťazcový zásobník na spájanie prevedených argumentov
			StringBuffer spojené = new StringBuffer();

			for (Transformácia jedna : transformácie)
				if (null != jedna)
				{
					spojené.append(' ');
					spojené.append(jedna.toString());
				}

			if (0 != spojené.length())
			{
				String jestvujúce = dajAtribút(index, "transform");
				if (null == jestvujúce || jestvujúce.isEmpty())
					prepíšAtribút(index, "transform",
						spojené.substring(1));
				else
					prepíšAtribút(index, "transform",
						jestvujúce + spojené);
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajTransformácie(int, Transformácia[]) pridajTransformácie}.</p> */
	public void pridajTransformacie(int index,
		Transformácia[] transformácie)
	{ pridajTransformácie(index, transformácie); }


	/**
	 * <p>Poskytne zoznam atribútov, ktoré sú asociované s tvarom so
	 * zadaným indexom. Ak je zadaný index záporný, tak metóda vytvorí
	 * zoznam atribútov pre tvar od konca vnútorného zásobníka tvarov,
	 * to znamená, že index {@code num-1} označuje posledný tvar vložený
	 * do zásobníka. Ak požadovaný tvar nejestvuje, tak metóda namiesto
	 * zoznamu vráti hodnotu {@code valnull}.</p>
	 * 
	 * @param index index vyšetrovaného tvaru z vnútorného zásobníka
	 * @return pole reťazcov s názvami atribútov asociovaných
	 *     s vyšetrovaným tvarom alebo {@code valnull}
	 */
	public String[] zoznamAtribútov(int index)
	{
		if (index < 0) index = tvary.size() + index;
		if (index >= 0 && index < tvary.size())
		{
			Tvar tvar = tvary.get(index);
			Set<String> kľúče = tvar.atribúty.keySet();
			String zoznam[] = new String[kľúče.size()];
			int i = 0; for (String atribút : kľúče) zoznam[i++] = atribút;
			return zoznam;
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zoznamAtribútov(int) zoznamAtribútov}.</p> */
	public String[] zoznamAtributov(int index) { return zoznamAtribútov(index); }

	/**
	 * <p>Vloží do vnútorného zásobníka tejto inštancie ďalší tvar
	 * s prípadnou sériou atribútov. Trieda pri {@linkplain 
	 * #zapíš(String, String, boolean) ukladaní tvarov do súboru}
	 * nájde vhodnú reprezentáciu zadaného tvaru vo forme XML/SVG značky
	 * (pozri aj metódu {@link #dajSVG(int) dajSVG(index)}), ku ktorej
	 * priradí sériu zadaných atribútov. (Atribúty bez mena alebo
	 * s hodnotou {@code valnull} sú ignorované.)</p>
	 * 
	 * <p class="caution"><b>Pozor!</b> Atribúty, ktoré sú kľúčové pri
	 * vyjadrení konkrétneho tvaru (napr. {@code cx}, {@code cy}, {@code r}
	 * pri kružnici, {@code points} pri polygóne, {@code d} pri ceste
	 * a podobne) sú počas prevodu do XML/SVG tvaru nahradené konkrétnymi
	 * hodnotami. Preto ich nastavenie touto metódou nemá zmysel.</p>
	 * 
	 * @param tvar inštancia tvaru na uloženie
	 * @param atribúty séria dvojíc reťazcov určujúca doplňujúce atribúty
	 *     tvaru
	 */
	public void pridaj(Shape tvar, String... atribúty)
	{
		int dĺžka = atribúty.length - 1;
		Atribúty zoznam = new Atribúty();
		for (int i = 0; i < dĺžka; i += 2)
			if (null != atribúty[i] && !atribúty[i].isEmpty() &&
				null != atribúty[i + 1])
				zoznam.put(atribúty[i], atribúty[i + 1]);

		Tvar záznam = new Tvar(tvar, zoznam);
		tvary.add(záznam);
	}


	/**
	 * <p>Vloží do vnútorného zásobníka tejto inštancie nový tvar
	 * reprezentujúci text (znaky, textovú informáciu) s prípadnou
	 * sériou atribútov. Pri kreslení je zo zadaného textu vytvorený
	 * obrys podľa niektorých parametrov, ktoré sú pri procese
	 * pridávania textu prevzaté zo zadaného robota (konkrétne písmo,
	 * poloha, spôsob kreslenia textov a pootočenie robota, ktoré určia
	 * vlastnosti písma, polohu kreslenia a pootočenie kresleného textu;
	 * farba robota, ktorá je použitá na určenie predvolenej farby
	 * výplne obrysu textu – čiže predvolene nie je nakreslený
	 * vygenerovaný obrys textu, ale jeho výplň).</p>
	 * 
	 * <p>Trieda pri {@linkplain #zapíš(String, String, boolean) ukladaní
	 * tvarov do súboru} použije XML/SVG značku {@code &lt;text&gt;}, ku
	 * ktorej priradí sériu základných a zadaných atribútov. Obsahom značky
	 * {@code &lt;text&gt;} bude zadaný text.</p>
	 * 
	 * <p class="remark"><b>Poznámky:</b> Atribúty bez mena sú ignorované
	 * a na rozdiel od metódy {@link #pridaj(Shape, String[]) pridaj(tvar,
	 * atribúty)} sú v tejto metóde atribúty s hodnotou {@code valnull}
	 * pri spracovaní odstránené, čo sa dá využiť na úpravu niektorých
	 * predvolene vytváraných atribútov – napríklad na odstránenie
	 * predvolenej výplne – {@code fill}, {@code fill-opacity} alebo
	 * reset predvoleného pootočenia, ktoré sa ukladá vo forme
	 * transformácie otočenia do atribútu {@code transform}. Atribút
	 * {@code transform} má pritom špeciálny spôsob spracovania.
	 * Ak jeho hodnota nie je rovná {@code valnull}, tak je pripojená
	 * k pôvodnej (vygenerovanej) hodnote. To znamená, že v prípade
	 * tohto atribútu má zmysel i jeho opakovaný výskyt v zozname
	 * atribútov.</p>
	 * 
	 * <p class="caution"><b>Pozor!</b> Základné atribúty textu (to jest
	 * poloha: {@code x}, {@code y} a vlastnosti písma: {@code font-family},
	 * {@code font-size}, {@code font-weight} a {@code font-style}) sú
	 * v prípade ich platnosti počas prevodu do XML/SVG tvaru prepísané
	 * konkrétnymi hodnotami, preto ich nastavenie touto metódou buď nemá
	 * zmysel (v prípade polohy), alebo sa ich efekt nemusí prejaviť
	 * (v prípade vlastností písma).</p>
	 * 
	 * <!-- TODO príklady použitia vrátane príkladu ukazujúceho
	 * prepísanie atribútu transform a pod. -->
	 * 
	 * @param text text, z ktorého bude pri kreslení vytvorený obrys
	 *     (predvolene je obrys vyplnený, takže vizuálne vyzerá ako
	 *     klasický text)
	 * @param tvorca robot, z ktorého budú prevzaté niektoré parametre
	 *     užitočné počas vytvárania obrysu
	 * @param atribúty séria dvojíc reťazcov určujúca doplňujúce atribúty
	 *     výsledného (vygenerovaného) tvaru (čiže obrysu textu)
	 */
	public void pridajText(String text, GRobot tvorca, String... atribúty)
	{
		// Zoznam slúžiaci na zlúčenie niektorých predvolených atribútov
		// so zadanými atribútmi:
		Atribúty zoznam = new Atribúty();

		// Zistenie polohy a pootočenia robota, pričom pootočenie je
		// hneď využité na vytvorenie transformácie:
		double prepočítanéX = Svet.prepočítajX(tvorca.aktuálneX);
		double prepočítanéY = Svet.prepočítajY(tvorca.aktuálneY);

		tvorca.grafikaAktívnehoPlátna.setFont(tvorca.aktuálnePísmo);

		FontMetrics rozmery = tvorca.grafikaAktívnehoPlátna.getFontMetrics();
		int šírkaTextu = rozmery.stringWidth(text);
		int poklesTextu = (rozmery.getDescent() * 3) / 2;
			// System.out.println("Pokles " + text + ": " + poklesTextu);
			// ‼TODO – OTESTUJ NA WINDOWS‼

		if ((90.0 != tvorca.aktuálnyUhol) &&
			(0 != (tvorca.spôsobKreslenia & KRESLI_ROTOVANÉ)))
			zoznam.put("transform",
				// (Pozor‼) Ani prehliadač, ani Inkscape v transformáciách
				// vôbec neakceptujú jednotky (uhlové „deg,“ ani rozmerové
				// „px“)…

				"rotate(" + (90 - tvorca.aktuálnyUhol) + ", " + // deg
					prepočítanéX + ", " + // px
					prepočítanéY + ")" // px

				// Ak by ju bolo treba rozbiť na tri samostatné trasformácie:
				// "translate(" + prepočítanéX + ", " + // px
				// 	prepočítanéY + ") " + // px
				// "rotate(" + (90 - tvorca.aktuálnyUhol) + ") " + // deg
				// "translate(" + -prepočítanéX + ", " + // px
				// 	-prepočítanéY + ")" // px
				);

		if (0 != (tvorca.spôsobKreslenia & KRESLI_NA_STRED))
		{
			prepočítanéX -= šírkaTextu / 2.0;
			prepočítanéY += poklesTextu;
		}

		// Použitie farby robota na definíciu predvolenej výplne
		// textového tvaru:
		Color farba = tvorca.farbaRobota;
		if (null != farba)
		{
			zoznam.put("fill", farbaNaReťazec(farba, true));
			if (255 != farba.getAlpha())
				zoznam.put("fill-opacity", alfaNaReťazec(farba));
		}

		// Spracovanie ostatných atribútov:
		int dĺžka = atribúty.length - 1;
		for (int i = 0; i < dĺžka; i += 2)
			if (null != atribúty[i] && !atribúty[i].isEmpty())
			{
				if (null == atribúty[i + 1])
					zoznam.remove(atribúty[i]);
				else
				{
					if (atribúty[i].equals("transform"))
					{
						String pôvodnáHodnota = zoznam.get(atribúty[i]);
						if (null == pôvodnáHodnota)
							zoznam.put(atribúty[i], atribúty[i + 1]);
						else
							zoznam.put(atribúty[i],
								pôvodnáHodnota.trim() + " " + atribúty[i + 1]);
					}
					else zoznam.put(atribúty[i], atribúty[i + 1]);
				}
			}

		// Vytvorenie tvaru textu a jeho pridanie medzi SVG definície:
		Tvar záznam = new Tvar(new SimpleTextShape(prepočítanéX,
			prepočítanéY, tvorca.aktuálnePísmo, text, Svet.grafikaSveta1),
			zoznam);
		tvary.add(záznam);
	}

	/**
	 * <p>Vloží do vnútorného zásobníka tejto inštancie nový tvar
	 * reprezentujúci text (znaky, textovú informáciu) s prípadnou
	 * sériou atribútov.</p>
	 * 
	 * <p>Metóda funguje rovnako ako metóda {@link #pridajText(String,
	 * GRobot, String[]) pridajText(text, tvorca, atribúty)} s tým, že
	 * na doplnenie chýbajúcich atribútov (poloha, písmo…) je použitý
	 * {@linkplain Svet#hlavnýRobot() hlavný robot}.</p>
	 * 
	 * @param text text, ktorý má byť pridaný k ostatným SVG tvarom
	 * @param atribúty séria dvojíc reťazcov určujúca doplňujúce
	 *     atribúty textu
	 * 
	 * @see #pridajText(String, GRobot, String[])
	 */
	public void pridajText(String text, String... atribúty)
	{ pridajText(text, Svet.hlavnýRobot, atribúty); }


	// Hodnoty atribútov nesmú obsahovať rezervované znaky
	private static String upravHodnotuAtribútu(String hodnota)
	{
		hodnota = hodnota.replaceAll("&", "&amp;");
		hodnota = hodnota.replaceAll("<", "&lt;");
		hodnota = hodnota.replaceAll(">", "&gt;");
		hodnota = hodnota.replaceAll("\"", "&quot;");
		return hodnota;
	}

	// Prevedie tvar (inštanciu vnútornej triedy Tvar, ktorá obsahuje
		// inštanciu Javy Shape a množinu doplnkových XML atribútov) na
		// reťazec vo formáte XML/SVG:
		//   RoundRectangle2D.
		//   Rectangle2D:
		//    – treba zabezpečiť vymazanie rx, ry.
		//   Ellipse2D:
		//    – ak je rx a ry rovnaké, tak je to kružnica
		//      (tiež treba zabezpečiť vymazanie rx, ry),
		//    – inak je to elipsa.
		//   Line2D.
		//   Path2D:
		//    – ak pozostáva z počiatočného príkazu moveTo a ďalej len
		//      z príkazov lineTo, tak ide o lomenú čiaru (polyline);
		//    – ak «to isté» a je uzavretá, tak ide o polygón (polygon);
		//    – inak je to klasická cesta.
	private static String dajSVG(Tvar tvar)
	{
		// Pomocné pole na čítanie iterátora
		double[] body = new double[6];

		// Pomocný reťazcový zásobník
		StringBuffer xmlSVG = new StringBuffer();

		// Pomocný tvar
		Shape tvar2 = tvar.tvar;

		// Určuje ukončenie XML značky tvaru
		String ukončenie = "/>";

		if (tvar2 instanceof RoundRectangle2D)
		{
			RoundRectangle2D tvar3 = (RoundRectangle2D)tvar2;

			tvar.atribúty.put("x", "" + tvar3.getX());
			tvar.atribúty.put("y", "" + tvar3.getY());
			tvar.atribúty.put("width", "" + tvar3.getWidth());
			tvar.atribúty.put("height", "" + tvar3.getHeight());
			tvar.atribúty.put("rx", "" + (tvar3.getArcWidth() / 2.0));
			tvar.atribúty.put("ry", "" + (tvar3.getArcHeight() / 2.0));

			xmlSVG.append("<rect ");
		}
		else if (tvar2 instanceof Rectangle2D)
		{
			Rectangle2D tvar3 = (Rectangle2D)tvar2;

			tvar.atribúty.put("x", "" + tvar3.getX());
			tvar.atribúty.put("y", "" + tvar3.getY());
			tvar.atribúty.put("width", "" + tvar3.getWidth());
			tvar.atribúty.put("height", "" + tvar3.getHeight());
			tvar.atribúty.remove("rx");
			tvar.atribúty.remove("ry");

			xmlSVG.append("<rect ");
		}
		else if (tvar2 instanceof Ellipse2D)
		{
			Ellipse2D tvar3 = (Ellipse2D)tvar2;

			if (tvar3.getWidth() == tvar3.getHeight())
			{
				tvar.atribúty.put("cx", "" + tvar3.getCenterX());
				tvar.atribúty.put("cy", "" + tvar3.getCenterY());
				tvar.atribúty.put("r", "" + ((tvar3.getWidth() +
					tvar3.getHeight()) / 4));
				tvar.atribúty.remove("rx");
				tvar.atribúty.remove("ry");

				xmlSVG.append("<circle ");
			}
			else
			{
				tvar.atribúty.put("cx", "" + tvar3.getCenterX());
				tvar.atribúty.put("cy", "" + tvar3.getCenterY());
				tvar.atribúty.put("rx", "" + tvar3.getWidth());
				tvar.atribúty.put("ry", "" + tvar3.getHeight());

				xmlSVG.append("<ellipse ");
			}
		}
		else if (tvar2 instanceof Line2D)
		{
			Line2D tvar3 = (Line2D)tvar2;

			tvar.atribúty.put("x1", "" + tvar3.getX1());
			tvar.atribúty.put("y1", "" + tvar3.getY1());
			tvar.atribúty.put("x2", "" + tvar3.getX2());
			tvar.atribúty.put("y2", "" + tvar3.getY2());

			xmlSVG.append("<line ");
		}
		else if (tvar2 instanceof SimpleTextShape)
		{
			SimpleTextShape tvar3 = (SimpleTextShape)tvar2;

			String text = tvar3.getText();
			if (null != text && !text.isEmpty())
			{
				tvar.atribúty.put("x", "" + tvar3.getX());
				tvar.atribúty.put("y", "" + tvar3.getY());

				Font font = tvar3.getFont();
				if (null != font)
				{
					tvar.atribúty.put("font-family", "" + font.getFamily());
					tvar.atribúty.put("font-size", font.getSize2D() + "pt");
					if (font.isBold())
						tvar.atribúty.put("font-weight", "bold");
					if (font.isItalic())
						tvar.atribúty.put("font-style", "italic");
				}

				xmlSVG.append("<text ");
				ukončenie = ">" + text + "</text>";
			}
		}
		else
		{
			boolean jePolygón = true, zavretý = false; int režim = 0;
			PathIterator iterátor = tvar2.getPathIterator(null);
			while (!iterátor.isDone() && jePolygón)
			{
				int typ = iterátor.currentSegment(body);

				switch (typ)
				{
					case PathIterator.SEG_MOVETO: // 1 bod
						if (0 == režim) režim = 1;
						else jePolygón = false;
						break;

					case PathIterator.SEG_LINETO: // 1 bod
						if (1 != režim) jePolygón = false;
						break;

					case PathIterator.SEG_QUADTO: // 2 body
						jePolygón = false;
						break;

					case PathIterator.SEG_CUBICTO: // 3 body
						jePolygón = false;
						break;

					case PathIterator.SEG_CLOSE: // —
						if (1 == režim) režim = 2;
						else jePolygón = false;
						zavretý = true;
						break;
				}

				iterátor.next();
			}

			iterátor = tvar2.getPathIterator(null);

			// Pomocný reťazcový zásobník
			StringBuffer atribút = new StringBuffer();

			if (jePolygón)
			{
				boolean prvý = true;
				// atribút.setLength(0); // netreba

				while (!iterátor.isDone())
				{
					int typ = iterátor.currentSegment(body);

					switch (typ)
					{
						case PathIterator.SEG_MOVETO:
						case PathIterator.SEG_LINETO:
							if (prvý) prvý = false;
							else atribút.append("  ");
							atribút.append(body[0]);
							atribút.append(", ");
							atribút.append(body[1]);
					}

					iterátor.next();
				}

				tvar.atribúty.put("points", atribút.toString());
				xmlSVG.append("<");
				xmlSVG.append(zavretý ? "polygon" : "polyline");
				xmlSVG.append(" ");
			}
			else
			{
				boolean prvý = true;
				// atribút.setLength(0); // netreba

				while (!iterátor.isDone())
				{
					if (prvý) prvý = false;
					else atribút.append("\r\n    ");

					int typ = iterátor.currentSegment(body);

					switch (typ)
					{
						case PathIterator.SEG_MOVETO: // 1 bod
							atribút.append("M ");
							atribút.append(body[0]);
							atribút.append(", ");
							atribút.append(body[1]);
							break;

						case PathIterator.SEG_LINETO: // 1 bod
							atribút.append("L ");
							atribút.append(body[0]);
							atribút.append(", ");
							atribút.append(body[1]);
							break;

						case PathIterator.SEG_QUADTO: // 2 body
							atribút.append("Q ");
							atribút.append(body[0]);
							atribút.append(", ");
							atribút.append(body[1]);
							atribút.append("  ");
							atribút.append(body[2]);
							atribút.append(", ");
							atribút.append(body[3]);
							break;

						case PathIterator.SEG_CUBICTO: // 3 body
							atribút.append("C ");
							atribút.append(body[0]);
							atribút.append(", ");
							atribút.append(body[1]);
							atribút.append("  ");
							atribút.append(body[2]);
							atribút.append(", ");
							atribút.append(body[3]);
							atribút.append("  ");
							atribút.append(body[4]);
							atribút.append(", ");
							atribút.append(body[5]);
							break;

						case PathIterator.SEG_CLOSE: // —
							atribút.append("Z");
							break;
					}

					iterátor.next();
				}

				tvar.atribúty.put("d", atribút.toString());
				xmlSVG.append("<path ");
			}
		}

		for (Map.Entry<String, String> záznam :
			tvar.atribúty.entrySet())
		{
			xmlSVG.append(záznam.getKey());
			xmlSVG.append("=\"");
			xmlSVG.append(upravHodnotuAtribútu(záznam.getValue()));
			xmlSVG.append("\" ");
		}

		xmlSVG.append(ukončenie);
		return xmlSVG.toString();
	}

	/**
	 * <p>Táto metóda slúži na export tvarov uložených vo vnútornom
	 * zásobníku tejto inštancie do formátu SVG. Podľa zadanej prípony
	 * súboru (povolené sú len tieto: {@code .htm}, {@code .html}
	 * a {@code .svg}) bude na export použitá šablóna HTML (pozri aj
	 * {@link #htmlŠablóna() htmlŠablóna}) alebo SVG (pozri aj {@link 
	 * #svgŠablóna() svgŠablóna}). Pre každý tvar, ktorý bol {@linkplain 
	 * #pridaj(Shape, String[]) vložený do vnútorného zásobníka tejto
	 * inštancie} bude nájdená vhodná reprezentácia v rámci štandardu SVG
	 * (pozri aj metódy {@linkplain #dajSVG(int) dajSVG(index)}
	 * a {@link #pridajText(String, GRobot, String[]) pridajText(tvar,
	 * tvorca, atribúty)}).</p>
	 * 
	 * <p>Exportované tvary sú prepočítané tak, aby mohli byť spracúvané
	 * v predvolenom súradnicovom priestore Javy, v ktorom sa „nula“ (to
	 * jest počiatok súradnicovej sústavy) nachádza v ľavom hornom rohu
	 * plátna a y-ová súradnica stúpa smerom nadol. Tento súradnicový
	 * priestor je bežne používaný v oblasti 2D počítačovej grafiky
	 * a používa ho aj formát SVG. (V skutočnosti táto konverzia prebieha
	 * už počas vytvárania {@linkplain Shape tvarov Javy}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Pri exporte nie sú využité
	 * vlastnosti SVG 2.0, najmä takzvaný „bearing“ – aktuálne pootočenie,
	 * ktorý má v budúcnosti slúžiť na implementáciu korytnačej grafiky
	 * v rámci štandardu SVG. Dôvodom nepoužitia novšej verzie štandardu
	 * je to, že súčasný softvér tento štandard nie je schopný spracovať.
	 * To znamená, že takto exportované údaje by neboli v praxi
	 * použiteľné.</p>
	 * 
	 * @param meno názov súboru (s príponou {@code .htm}, {@code .html}
	 *     alebo {@code .svg}), do ktorého majú byť zapísané tvary vo
	 *     formáte SVG
	 * @param titulok titulok súboru; ak je zadaná hodnota {@code valnull},
	 *     tak sa použije predvolený tvar titulku: {@code SVG tvary
	 *     generované programovacím rámcom GRobot }<em>«verzia»</em>{@code ,
	 *     © }<em>«roky vývoja»</em>{@code , }<em>«hlavný vývojár»</em>
	 * @param prepísať príznak prepísania jestvujúceho súboru; ak je
	 *     zadaná hodnota {@code valtrue}, tak v prípade, že cieľový súbor
	 *     jestvuje, je prepísaný; ak je zadaná hodnota {@code valfalse},
	 *     tak v prípade, že cieľový súbor jestvuje, vráti metóda hodnotu
	 *     {@code num-1}
	 * @return počet zapísaných (exportovaných) tvarov alebo {@code num-1}
	 *     ak bol pokus o zápis neúspešný
	 * 
	 * @throws GRobotException ak nastane chyba počas zápisu súboru
	 */
	public int zapíš(String meno, String titulok, boolean prepísať)
	{
		if (null == meno)
			throw new GRobotException(
				"Názov súboru nesmie byť zamlčaný.",
				"fileNameOmitted", null, new NullPointerException());

		String malé = meno.toLowerCase();
		String[] šablóna;

		if (malé.endsWith(".svg")) šablóna = svgŠablóna;
		else if (malé.endsWith(".htm") || malé.endsWith(".html"))
			šablóna = htmlŠablóna; else return -1;

		File súborCieľa = new File(meno);

		if (!prepísať && súborCieľa.exists()) return -1;

		if (súborCieľa.exists() && !súborCieľa.isFile())
			throw new GRobotException(
				"Cieľový súbor „" + meno + "“ nie je súbor.",
				"targetObjectNotFile", meno);

		súborCieľa = null;

		if (null == titulok) titulok =
			"SVG tvary generované programovacím rámcom " +
			GRobot.versionString;

		BufferedWriter zápis = null;

		// Otvorenie súboru na zápis (prepísanie prípadného jestvujúceho)
		try
		{
			zápis = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(meno), "UTF-8"));
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri zápise SVG súboru „" +
				meno + ".“", "svgWriteError", e);
		}


		int početZapísaných = 0;

		// Pomocný reťazcový zásobník
		StringBuffer $TVARY = new StringBuffer();

		// Vyjadrenie inštancií triedy Tvar vo formáte SVG…
		for (Tvar tvar : tvary)
		{
			if (0 != početZapísaných) $TVARY.append("\r\n");
			$TVARY.append("  ");
			$TVARY.append(dajSVG(tvar));
			++početZapísaných;
		}

		// Zápis šablóny do súboru
		try
		{
			for (String riadok : šablóna)
			{
				if (-1 != riadok.indexOf("$TITULOK"))
					riadok = riadok.replace("$TITULOK", titulok);

				if (-1 != riadok.indexOf("$TITLE"))
					riadok = riadok.replace("$TITLE", titulok);

				if (-1 != riadok.indexOf("$KÓDOVANIE"))
					riadok = riadok.replace("$KÓDOVANIE", "UTF-8");

				if (-1 != riadok.indexOf("$KODOVANIE"))
					riadok = riadok.replace("$KODOVANIE", "UTF-8");

				if (-1 != riadok.indexOf("$ENCODING"))
					riadok = riadok.replace("$ENCODING", "UTF-8");

				if (-1 != riadok.indexOf("$ŠÍRKA"))
					riadok = riadok.replace("$ŠÍRKA", "" + Svet.šírka());

				if (-1 != riadok.indexOf("$WIDTH"))
					riadok = riadok.replace("$WIDTH", "" + Svet.šírka());

				if (-1 != riadok.indexOf("$VÝŠKA"))
					riadok = riadok.replace("$VÝŠKA", "" + Svet.výška());

				if (-1 != riadok.indexOf("$HEIGHT"))
					riadok = riadok.replace("$HEIGHT", "" + Svet.výška());

				if (-1 != riadok.indexOf("$TVARY"))
					riadok = riadok.replace("$TVARY", $TVARY);

				if (-1 != riadok.indexOf("$SHAPES"))
					riadok = riadok.replace("$SHAPES", $TVARY);

				zápis.write(riadok + "\r\n");
			}
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri zápise SVG súboru „" +
				meno + ".“", "svgWriteError", e);
		}

		// Bezpečné zavretie súboru
		try
		{
			zápis.close();
			zápis = null;
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri zápise SVG súboru „" +
				meno + ".“", "svgWriteError", e);
		}

		return početZapísaných;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zapíš(String, String, boolean) zapíš}.</p> */
	public int zapis(String meno, String titulok, boolean prepísať)
	{ return zapíš(meno, titulok, prepísať); }

	/**
	 * <p>Metóda exportuje tvary uložené vo vnútornom zásobníku tejto
	 * inštancie do formátu SVG.</p>
	 * 
	 * <p>Ďalšie podrobnosti si prečítajte v opise metódy
	 * {@link #zapíš(String, String, boolean)
	 * zapíš(meno, titulok, prepísať)}.</p>
	 * 
	 * <p>Táto metóda sa správa tak, ako keby bola uvedená metóda volaná
	 * s nasledujúcimi hodnotami argumentov: {@link #zapíš(String,
	 * String, boolean) zapíš}{@code (meno, titulok, }{@code 
	 * valfalse}{@code )}.</p>
	 * 
	 * @param meno názov súboru, do ktorého majú byť zapísané tvary vo
	 *     formáte SVG
	 * @param titulok titulok súboru (alebo {@code valnull})
	 * @return počet zapísaných (exportovaných) tvarov alebo {@code num-1}
	 *     ak bol pokus o zápis neúspešný
	 */
	public int zapíš(String meno, String titulok)
	{ return zapíš(meno, titulok, false); }

	/** <p><a class="alias"></a> Alias pre {@link #zapíš(String, String) zapíš}.</p> */
	public int zapis(String meno, String titulok)
	{ return zapíš(meno, titulok, false); }

	/**
	 * <p>Metóda exportuje tvary uložené vo vnútornom zásobníku tejto
	 * inštancie do formátu SVG.</p>
	 * 
	 * <p>Ďalšie podrobnosti si prečítajte v opise metódy
	 * {@link #zapíš(String, String, boolean)
	 * zapíš(meno, titulok, prepísať)}.</p>
	 * 
	 * <p>Táto metóda sa správa tak, ako keby bola uvedená metóda volaná
	 * s nasledujúcimi hodnotami argumentov: {@link #zapíš(String,
	 * String, boolean) zapíš}{@code (meno, }{@code valnull}{@code 
	 * , }{@code valfalse}{@code )}.</p>
	 * 
	 * @param meno názov súboru, do ktorého majú byť zapísané tvary vo
	 *     formáte SVG
	 * @return počet zapísaných (exportovaných) tvarov alebo {@code num-1}
	 *     ak bol pokus o zápis neúspešný
	 */
	public int zapíš(String meno)
	{ return zapíš(meno, null, false); }

	/** <p><a class="alias"></a> Alias pre {@link #zapíš(String) zapíš}.</p> */
	public int zapis(String meno)
	{ return zapíš(meno, null, false); }


	/**
	 * <p>Táto metóda slúži na export všetkých tvarov uložených vo vnútornom
	 * zásobníku tejto inštancie do formátu SVG. Podľa zadaného formátu
	 * (povolené sú: {@code HTML} a {@code SVG}) bude na export použitá
	 * šablóna HTML (pozri aj {@link #htmlŠablóna() htmlŠablóna}) alebo
	 * SVG (pozri aj {@link #svgŠablóna() svgŠablóna}) a výsledok je
	 * vrátený vo forme reťazca v návratovej hodnote tejto metódy.
	 * Inak táto metóda funguje rovnako ako metóda {@link #zapíš(String,
	 * String, boolean) zapíš}.</p>
	 * 
	 * <!-- TODO – vyrob príklad, ktorý to skombinuje s podporou
	 * ZIP a Base64. -->
	 * 
	 * @param formát formát exportovaného SVG reťazca; povolené sú dva
	 *     formáty: {@code HTML} a {@code SVG})
	 * @param titulok obsah značky {@code &lt;title&gt;}; ak je zadaná
	 *     hodnota {@code valnull}, tak sa použije predvolený tvar:
	 *     {@code SVG tvary generované programovacím rámcom
	 *     GRobot }<em>«verzia»</em>{@code , © }<em>«roky
	 *     vývoja»</em>{@code , }<em>«hlavný vývojár»</em>
	 * @return buď reťazec s exportovaným SVG v požadovanom formáte, alebo
	 *     prázdny reťazec ({@code srg""}), ak export zlyhal
	 * 
	 * @throws GRobotException ak nastane chyba počas zápisu súboru
	 */
	public String dajSVG(String formát, String titulok)
	{
		if (null == formát) return "";

		formát = formát.toLowerCase();
		String[] šablóna;

		if (formát.endsWith("svg")) šablóna = svgŠablóna;
		else if (formát.endsWith("htm") || formát.endsWith("html"))
			šablóna = htmlŠablóna; else return "";

		if (null == titulok) titulok =
			"SVG tvary generované programovacím rámcom " +
			GRobot.versionString;

		// Zásobník výsledného SVG reťazca
		StringBuffer svgFormát = new StringBuffer();

		// Pomocný reťazcový zásobník na uloženie exportovaných tvarov
		StringBuffer $TVARY = new StringBuffer();

		// Vyjadrenie inštancií triedy Tvar vo formáte SVG…
		{ boolean prvý = true;
		for (Tvar tvar : tvary)
		{
			if (prvý) prvý = false; else $TVARY.append("\r\n");
			$TVARY.append("  ");
			$TVARY.append(dajSVG(tvar));
		}}

		// Tvorba výsledného SVG reťazca podľa šablóny
		for (String riadok : šablóna)
		{
			if (-1 != riadok.indexOf("$TITULOK"))
				riadok = riadok.replace("$TITULOK", titulok);

			if (-1 != riadok.indexOf("$TITLE"))
				riadok = riadok.replace("$TITLE", titulok);

			if (-1 != riadok.indexOf("$KÓDOVANIE"))
				riadok = riadok.replace("$KÓDOVANIE", "UTF-8");

			if (-1 != riadok.indexOf("$KODOVANIE"))
				riadok = riadok.replace("$KODOVANIE", "UTF-8");

			if (-1 != riadok.indexOf("$ENCODING"))
				riadok = riadok.replace("$ENCODING", "UTF-8");

			if (-1 != riadok.indexOf("$ŠÍRKA"))
				riadok = riadok.replace("$ŠÍRKA", "" + Svet.šírka());

			if (-1 != riadok.indexOf("$WIDTH"))
				riadok = riadok.replace("$WIDTH", "" + Svet.šírka());

			if (-1 != riadok.indexOf("$VÝŠKA"))
				riadok = riadok.replace("$VÝŠKA", "" + Svet.výška());

			if (-1 != riadok.indexOf("$HEIGHT"))
				riadok = riadok.replace("$HEIGHT", "" + Svet.výška());

			if (-1 != riadok.indexOf("$TVARY"))
				riadok = riadok.replace("$TVARY", $TVARY);

			if (-1 != riadok.indexOf("$SHAPES"))
				riadok = riadok.replace("$SHAPES", $TVARY);

			svgFormát.append(riadok);
			svgFormát.append("\r\n");
		}

		return svgFormát.toString();
	}

	/**
	 * <p>Metóda exportuje tvary uložené vo vnútornom zásobníku tejto
	 * inštancie do formátu SVG.</p>
	 * 
	 * <p>Ďalšie podrobnosti si prečítajte v opise metódy
	 * {@link #dajSVG(String, String) dajSVG(formát, titulok)}.</p>
	 * 
	 * <p>Táto metóda sa správa tak, ako keby bola uvedená metóda
	 * volaná s nasledujúcimi hodnotami argumentov: {@link 
	 * #dajSVG(String, String) dajSVG}{@code (formát, }{@code 
	 * valnull}{@code )}.</p>
	 * 
	 * @param formát formát exportovaného SVG reťazca; povolené sú dva
	 *     formáty: {@code HTML} a {@code SVG})
	 * @return buď reťazec s exportovaným SVG v požadovanom formáte, alebo
	 *     prázdny reťazec ({@code srg""}), ak export zlyhal
	 */
	public String dajSVG(String formát) { return dajSVG(formát, null); }

	/**
	 * <p>Metóda exportuje tvary uložené vo vnútornom zásobníku tejto
	 * inštancie do formátu SVG.</p>
	 * 
	 * <p>Ďalšie podrobnosti si prečítajte v opise metódy
	 * {@link #dajSVG(String, String) dajSVG(formát, titulok)}.</p>
	 * 
	 * <p>Táto metóda sa správa tak, ako keby bola uvedená metóda
	 * volaná s nasledujúcimi hodnotami argumentov: {@link 
	 * #dajSVG(String, String) dajSVG}{@code (srg"SVG", }{@code 
	 * valnull}{@code )}.</p>
	 * 
	 * @return buď reťazec s exportovaným SVG v požadovanom formáte, alebo
	 *     prázdny reťazec ({@code srg""}), ak export zlyhal
	 */
	public String dajSVG() { return dajSVG("svg", null); }


	// Vnútorný prúd na čítanie XML údajov
	private XMLStreamReader čítanie = null;

	// Vnútorné počítadlo nájdených a pridaných tvarov (počas
	// spracovania SVG údajov; napríklad zo vstupného reťazca
	// alebo zo súboru)
	private int početPridanýchTvarov = 0;

	// Zoznam atribútov aktuálneho elementu
	private final Atribúty atribúty = new Atribúty();

	// Spracuje aktuálny element ako obdĺžnik a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujObdĺžnik()
	{
		double x, y, rx, ry, width, height;
		++početPridanýchTvarov;

		if (atribúty.containsKey("x"))
			x = reťazecNaČíslo(atribúty.get("x"));
		else
			x = 0.0;

		if (atribúty.containsKey("y"))
			y = reťazecNaČíslo(atribúty.get("y"));
		else
			y = 0.0;

		if (atribúty.containsKey("width"))
			width = reťazecNaČíslo(atribúty.get("width"));
		else
			width = 0.0;

		if (atribúty.containsKey("height"))
			height = reťazecNaČíslo(atribúty.get("height"));
		else
			height = 0.0;

		if (atribúty.containsKey("rx") && atribúty.containsKey("ry"))
		{
			rx = reťazecNaČíslo(atribúty.get("rx"));
			ry = reťazecNaČíslo(atribúty.get("ry"));
			tvary.add(new Tvar(new RoundRectangle2D.Double(
				x, y, width, height, 2.0 * rx, 2.0 * ry), atribúty));
		}
		else if (atribúty.containsKey("rx"))
		{
			rx = ry = reťazecNaČíslo(atribúty.get("rx"));
			tvary.add(new Tvar(new RoundRectangle2D.Double(
				x, y, width, height, 2.0 * rx, 2.0 * ry), atribúty));
		}
		else if (atribúty.containsKey("ry"))
		{
			rx = ry = reťazecNaČíslo(atribúty.get("ry"));
			tvary.add(new Tvar(new RoundRectangle2D.Double(
				x, y, width, height, 2.0 * rx, 2.0 * ry), atribúty));
		}
		else
		{
			tvary.add(new Tvar(new Rectangle2D.
				Double(x, y, width, height), atribúty));
		}
	}

	// Spracuje aktuálny element ako kružnicu a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujKružnicu()
	{
		double cx, cy, r;
		++početPridanýchTvarov;

		if (atribúty.containsKey("cx"))
			cx = reťazecNaČíslo(atribúty.get("cx"));
		else
			cx = 0.0;

		if (atribúty.containsKey("cy"))
			cy = reťazecNaČíslo(atribúty.get("cy"));
		else
			cy = 0.0;

		if (atribúty.containsKey("r"))
			r = reťazecNaČíslo(atribúty.get("r"));
		else
			r = 0.0;

		tvary.add(new Tvar(new Ellipse2D.Double(
			cx - r, cy - r, r * 2, r * 2), atribúty));
	}

	// Spracuje aktuálny element ako elipsu a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujElipsu()
	{
		double cx, cy, rx, ry;
		++početPridanýchTvarov;

		if (atribúty.containsKey("cx"))
			cx = reťazecNaČíslo(atribúty.get("cx"));
		else
			cx = 0.0;

		if (atribúty.containsKey("cy"))
			cy = reťazecNaČíslo(atribúty.get("cy"));
		else
			cy = 0.0;

		if (atribúty.containsKey("rx"))
			rx = reťazecNaČíslo(atribúty.get("rx"));
		else
			rx = 0.0;

		if (atribúty.containsKey("ry"))
			ry = reťazecNaČíslo(atribúty.get("ry"));
		else
			ry = 0.0;

		tvary.add(new Tvar(new Ellipse2D.Double(
			cx - rx, cy - ry, rx * 2, ry * 2), atribúty));
	}

	// Spracuje aktuálny element ako úsečku a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujÚsečku()
	{
		double x1, y1, x2, y2;
		++početPridanýchTvarov;

		if (atribúty.containsKey("x1"))
			x1 = reťazecNaČíslo(atribúty.get("x1"));
		else
			x1 = 0.0;

		if (atribúty.containsKey("y1"))
			y1 = reťazecNaČíslo(atribúty.get("y1"));
		else
			y1 = 0.0;

		if (atribúty.containsKey("x2"))
			x2 = reťazecNaČíslo(atribúty.get("x2"));
		else
			x2 = 0.0;

		if (atribúty.containsKey("y2"))
			y2 = reťazecNaČíslo(atribúty.get("y2"));
		else
			y2 = 0.0;

		tvary.add(new Tvar(new Line2D.Double(x1, y1, x2, y2), atribúty));
	}

	// Atribúty pomáhajúce pri spracovaní „zložených“ parametrov tvarov –
	// „points“ pri polygónoch a „d“ pri ceste.

	// Trieda na spracovanie hodnoty zvoleného atribútu – v podstate
	// funguje podobne ako prúd znakov
	private class ÚdajeTvaru
	{
		private char údaje[];
		private int poloha;

		// Predvolený konštruktor
		public ÚdajeTvaru()
		{
			údaje = null;
			poloha = -1;
		}

		// Konštruktor, ktorý vykoná rovnakú akciu ako metóda reset
		public ÚdajeTvaru(String novéÚdaje)
		{
			údaje = novéÚdaje.toCharArray();
			poloha = 0;
		}

		// Inicializuje inštanciu novou sériou znakov
		public void reset(String novéÚdaje)
		{
			údaje = novéÚdaje.toCharArray();
			poloha = 0;
		}

		// Vráti aktuálny znak (nekontroluje, či už bola vykonaná
		// inicializácia – spolieha sa na to, že áno)
		public char znak()
		{
			return údaje[poloha];
		}

		// Testuje, či je znak číslo alebo časť číselného literálu
		public boolean jeČíslo()
		{
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return '-' == znak || '+' == znak || '.' == znak ||
				'e' == znak || 'E' == znak || Character.isDigit(znak);
		}

		// Testuje, či je znak písmeno
		public boolean jePísmeno()
		{
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return Character.isLetter(znak);
		}

		// Testuje, či je znak riadiace písmeno
		public boolean jeRiadiacePísmeno()
		{
			preskočOddeľovače();
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return 'e' != znak && 'E' != znak && Character.isLetter(znak);
		}

		// Testuje, či je znak časť číselného iterálu alebo písmeno (jednotka)
		public boolean jeZnakÚdajov()
		{
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return '-' == znak || '+' == znak || '.' == znak ||
				Character.isLetterOrDigit(znak);
		}

		// Testuje, či je znak časť číselného iterálu
		public boolean jeZnakČísla()
		{
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return '-' == znak || '+' == znak || '.' == znak ||
				'e' == znak || 'E' == znak || Character.isDigit(znak);
		}

		// Testuje, či znak nie je časť číselného iterálu ani písmeno
		public boolean jeOddeľovač()
		{
			if (jeKoniec()) return false;
			char znak = údaje[poloha];
			return '-' != znak && '+' != znak && '.' != znak &&
				!Character.isLetterOrDigit(znak);
		}

		// Prejde na nasledujúci znak, ak jestvuje
		public void ďalej()
		{
			if (poloha < údaje.length) ++poloha;
		}

		// Overí, či bol dosiahnutý koniec série znakov
		public boolean jeKoniec()
		{
			if (null == údaje) return true;

			// Musel som tu nasilu predtestovať, či sa ku koncu reťazca dá
			// nájsť zmysluplný znak (napr. či už do konca nie sú samé
			// oddeľovače), lebo to zlyhávalo (v iných častiach mechanizmu)…
			for (int polohaNaKonci = poloha, údajeLength = údaje.length;
				polohaNaKonci < údajeLength; ++polohaNaKonci)
			{
				char znak = údaje[polohaNaKonci];
				if ('-' == znak || '+' == znak || '.' == znak ||
					Character.isLetterOrDigit(znak)) return false;
			}

			// ‼‼‼ Ak predchádzajúci for cestou nenarazil na „zmysluplný“
			// znak, tak je koniec…
			return true;

			// Pôvodný test bol jednoduchý, ale mal riziko zlyhania
			// vo zvyšku rozpoznávacieho mechanizmu:
			// 	return null == údaje || poloha >= údaje.length;
		}

		// Overí, či ešte nebol dosiahnutý koniec série znakov (to jest,
		// či sú ešte nejaké znaky v zásobníku)
		public boolean nieJeKoniec()
		{
			// Postup je inverzný k metóde jeKoniec… Deje sa presne to isté,
			// len namiesto true sa vracia false a naopak:

			if (null == údaje) return false;
			for (int polohaNaKonci = poloha, údajeLength = údaje.length;
				polohaNaKonci < údajeLength; ++polohaNaKonci)
			{
				char znak = údaje[polohaNaKonci];
				if ('-' == znak || '+' == znak || '.' == znak ||
					Character.isLetterOrDigit(znak)) return true;
			}
			return false;

			// Pôvodný test bol jednoduchý, ale mal riziko zlyhania
			// vo zvyšku rozpoznávacieho mechanizmu:
			// 	return null != údaje && poloha < údaje.length;
		}

		// Preskočí všetky znaky, ktoré považuje za oddeľovače číselných
		// hodnôt
		public void preskočOddeľovače()
		{
			while (jeOddeľovač()) ďalej();
		}

		// Rozpozná a vráti ďalší číselný údaj s jednotkou v prúde znakov
		// (ak jestvuje) alebo vráti NaN
		public double ďalšiíÚdaj()
		{
			while (!jeČíslo()) ďalej();

			if (nieJeKoniec())
			{
				int začiatok = poloha;
				while (jeZnakÚdajov()) ďalej();
				int dĺžka = poloha - začiatok;

				if (0 != dĺžka) try
				{
					// System.out.println("Parsujem údaj: " +
					// 	new String(údaje, začiatok, dĺžka));

					return reťazecNaČíslo(new String(
						údaje, začiatok, dĺžka));
				}
				catch (Exception e)
				{} // Po vyčerpaní zásobníka bude výsledok NaN…
			}

			return Double.NaN;
		}

		// Rozpozná a vráti ďalšie číslo v prúde znakov (ak jestvuje)
		// alebo vráti NaN
		public double ďalšieČíslo()
		{
			while (!jeČíslo()) ďalej();

			if (nieJeKoniec())
			{
				int začiatok = poloha;
				while (jeZnakČísla()) ďalej();
				int dĺžka = poloha - začiatok;

				if (0 != dĺžka) try
				{
					// System.out.println("Parsujem číslo: " +
					// 	new String(údaje, začiatok, dĺžka));

					return reťazecNaČíslo(new String(
						údaje, začiatok, dĺžka));
				}
				catch (Exception e)
				{} // Po vyčerpaní zásobníka bude výsledok NaN…
			}

			return Double.NaN;
		}
	}

	// Inštancia triedy simulujúcej prúd znakov vytvorený z hodnoty
	// atribútu s pridanou hodnotou rozpoznávania oddeľovačov, riadiacich
	// znakov a číselných hodnôt
	private ÚdajeTvaru údajeTvaru = new ÚdajeTvaru();

	// Spracuje aktuálny element ako lomenú čiaru alebo polygón (čo je
	// vo vnímaní SVG uzavretá lomená čiara) a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujLomenúCestu(boolean polygón)
	{
		if (atribúty.containsKey("points"))
		{
			++početPridanýchTvarov;
			údajeTvaru.reset(atribúty.get("points"));

			Path2D.Double cesta = new Path2D.Double();
			boolean prvý = true;

			while (údajeTvaru.nieJeKoniec())
			{
				double
					xx = údajeTvaru.ďalšieČíslo(),
					yy = údajeTvaru.ďalšieČíslo();

				if (Double.isNaN(xx) || Double.isNaN(yy)) break;

				if (prvý)
				{
					cesta.moveTo(xx, yy);
					prvý = false;
				}
				else cesta.lineTo(xx, yy);
			}

			if (polygón) cesta.closePath();
			tvary.add(new Tvar(cesta, atribúty));
		}
	}

	// Trieda slúžiaca na spracovanie údajov cesty SVG – rozpoznáva
	// a pracuje s posunom, úsečkami (priamymi čiarami), krivkami
	// a pootočením podľa špecifikácie SVG 2.0; používa inštanciu
	// údajeTvaru definovanú vyššie
	private class SpracovanieCesty
	{
		public char režim = 'm';
		public char znak = 0;
		public double odchýlka = 0.0;
		public double lOdchýlka = 0.0;
		public boolean relatívne = false;
		public boolean skrátene = false;
		public boolean otáčaj = false;

		public final double p[] = new double[8];  // point
		public final double lp[] = new double[6]; // last point
		public int n = 0, ln = 0;

		public Path2D.Double cesta;
		private double xx, yy;

		// Inicializuje inštanciu novou hodnotou atribútu „d“
		public void reset(String novéÚdaje)
		{
			režim = 'm'; znak = 0;
			odchýlka = lOdchýlka = 0.0;
			relatívne = skrátene = false;
			otáčaj = false;

			for (int i = p.length - 1; i >= 0; --i) p[i] = 0.0;
			for (int i = lp.length - 1; i >= 0; --i) lp[i] = 0.0;
			n = ln = 0;

			cesta = new Path2D.Double();
			údajeTvaru.reset(novéÚdaje);
		}

		// Zistí, či ďalším znakom na spracovanie je písmeno
		public boolean ďalšieJeRiadiacePísmeno()
		{
			if (údajeTvaru.jeRiadiacePísmeno())
			{
				znak = údajeTvaru.znak();
				údajeTvaru.ďalej();
				return true;
			}

			return false;
		}

		// Zistí, či aktuálnym znakom je platný riadiaci znak
		public boolean jeRiadiaciZnak()
		{
			return
				'A' == znak || 'a' == znak || 'B' == znak || 'b' == znak ||
				'C' == znak || 'c' == znak || 'H' == znak || 'h' == znak ||
				'L' == znak || 'l' == znak || 'M' == znak || 'm' == znak ||
				'Q' == znak || 'q' == znak || 'S' == znak || 's' == znak ||
				'T' == znak || 't' == znak || 'V' == znak || 'v' == znak ||
				'Z' == znak || 'z' == znak;
		}

		// Pokúsi sa pridať nový segment cesty – ak sa podarilo nazbierať
		// dostatok údajov
		public void pridajSegment()
		{
			switch (režim)
			{
			case 'm': pridajPresunutie(); break;
			case 'l': pridajČiaru(); break;
			case 'h': pridajHorizontálnuČiaru(); break;
			case 'v': pridajVertikálnuČiaru(); break;
			case 'c': pridajBézierovuKrivku(); break;
			case 's': pridajBézierovuKrivku(); break;
			case 'q': pridajKvadratickúKrivku(); break;
			case 't': pridajKvadratickúKrivku(); break;
			case 'a': pridajOblúk(); break;
			case 'b': spracujOdchýlku(); break;
			}
		}

		// Ak chýba, tak pridá posledný bod krivky totožný so začiatočným
		// bodom aktuálneho segmentu cesty – používa sa pri uzavretí
		// segmentu
		public void pridajPoslednýBodKrivky()
		{

			/*
				https://www.w3.org/TR/SVG/paths.html#PathDataClosePathCommand

				9.3.4. The “closepath” command
				==============================

				The “closepath” (Z or z) ends the current subpath by
				connecting it back to its initial point. An automatic
				straight line is drawn from the current point to the
				initial point of the current subpath. This path segment
				may be of zero length.

				If a “closepath” is followed immediately by a “moveto,”
				then the “moveto” identifies the start point of the next
				subpath. If a “closepath” is followed immediately by any
				other command, then the next subpath starts at the same
				initial point as the current subpath.

				When a subpath ends in a “closepath,” it differs in
				behavior from what happens when “manually” closing
				a subpath via a “lineto” command in how ‘stroke-linejoin’
				and ‘stroke-linecap’ are implemented. With “closepath,”
				the end of the final segment of the subpath is “joined”
				with the start of the initial segment of the subpath
				using the current value of ‘stroke-linejoin.’ If you
				instead “manually” close the subpath via a “lineto”
				command, the start of the first segment and the end of
				the last segment are not joined but instead are each
				capped using the current value of ‘stroke-linecap.’ At
				the end of the command, the new current point is set to
				the initial point of the current subpath.

				Command:    Z or z
				Name:       closepath
				Parameters: (none)
				Description:
					Close the current subpath by connecting it back to
					the current subpath's initial point (see prose above).
					Since the Z and z commands take no parameters, they
					have an identical effect.

				A closed subpath must be closed with a “closepath”
				command, this “joins” the first and last path segments.
				Any other path is an open subpath.

				A closed subpath differs in behavior from an open subpath
				whose final coordinate is the initial point of the subpath.
				The first and last path segments of an open subpath will
				not be joined, even when the final coordinate of the last
				path segment is the initial point of the subpath. This
				will result in the first and last path segments being
				capped using the current value of stroke-linecap rather
				than joined using the current value of stroke-linejoin.

				If a “closepath” is followed immediately by a “moveto,”
				then the “moveto” identifies the start point of the next
				subpath. If a “closepath” is followed immediately by any
				other command, then the next subpath must start at the
				same initial point as the current subpath.
			*/

			/*if (('c' == režim && 4 == n) ||
				('s' == režim && 2 == n) ||
				('q' == režim && 2 == n) ||
				('t' == režim && 0 == n) ||
				('a' == režim && 5 == n))
			{
				double xx, yy, x0, y0;
				x0 = lp[4];
				y0 = lp[5];

				if (relatívne)
				{
					if (ln >= 1)
					{
						x0 -= lp[0];
						y0 -= lp[1];
					}

					if (otáčaj)
					{
						double uhol = Math.toRadians(-odchýlka);
						double sinUhol = Math.sin(uhol);
						double cosUhol = Math.cos(uhol);
						xx = x0 * cosUhol - y0 * sinUhol;
						yy = x0 * sinUhol + y0 * cosUhol;
					}
					else
					{
						xx = x0;
						yy = y0;
					}
				}
				else
				{
					xx = x0;
					yy = y0;
				}*/

			// Oprava:

				lp[0] = xx = lp[4];
				lp[1] = yy = lp[5];

			// Podmienka bola zhora „presunutá“ sem:
			if (('c' == režim && 4 == n) ||
				('s' == režim && 2 == n) ||
				('q' == režim && 2 == n) ||
				('t' == režim && 0 == n) ||
				('a' == režim && 5 == n))
			{
				p[n++] = xx;
				p[n++] = yy;
			}
		}

		// Zmení režim vytvárania cesty podľa aktuálneho riadiaceho znaku
		public void zmeňRežim()
		{
			if (jeRiadiaciZnak())
			{
				// System.out.println("Mením režim: " + znak);

				if ('z' == znak || 'Z' == znak) pridajPoslednýBodKrivky();
				pridajSegment();

				relatívne = Character.isLowerCase(znak);
				režim = Character.toLowerCase(znak);
				skrátene = 's' == režim || 't' == režim;
				n = 0;

				if ('z' == režim)
				{
					režim = 'm';
					cesta.closePath();
				}
				else if ('b' == režim)
				{
					odchýlka = lOdchýlka;
					otáčaj = true;
				}
			}
		}

		// Spracuje zadané súradnice podľa aktuálneho režimu, uloží ich
		// do požadovaného registra bodu (platné sú hodnoty 1 alebo 2)
		// a výsledok uchová vo vnútorných premenných xx, yy.
		private void dajSúradnice(double x0, double y0, int uložBod)
		{
			// System.out.print((relatívne ? 'R' : 'A') + ":    " +
			// 	Svet.F(x0, 10, 5) + "; " + Svet.F(y0, 10, 5) +
			// 	" (" + uložBod + "): "); double lp0 = lp[0], lp1 = lp[1];

			/*
				Problém, ktorý som si poznačil na tomto mieste (nižšie)
				nakoniec vôbec nesúvisel s tým, čo som tam nižšie rozpísal.
				Problém bol v tom, že pri nájdení „príkazu“ 'z'/'Z' (close
				path) som neuložil nové súradnice ako aktuálne súradnice
				a tiež v tom, že som rozlišoval medzi príkazom 'z' a 'Z'
				(prepočítavajúc relatívne súradnice pri verzii 'z'), čo bolo
				oboje nesprávne, takže pri výskyte tohto príkazu, po ktorom
				nasledovali relatívne súradnice, nastal problém. Oprava bola
				vykonaná v metóde pridajPoslednýBodKrivky, kde je zároveň
				skopírovaný fragment SVG dokumentácie o príkaze close path
				('z'/'Z'; v angličtine).

				Prepis pôvodnej poznámky (upozorňujúcej na chybu):
				--------------------------------------------------

				Tu je (pozn.: čo nakoniec nebola pravda) nejaká „brutálna“
				chyba v relatívnych súradniciach (pozn.: niekedy som váhal,
				či nie práve naopak pri absolútnych, lebo chybu sa nedarilo
				nájsť – hľadal som nesprávne, lebo príznaky som skrz sťaženú
				identifiáciu neboli jednoznačné – to preto, lebo nemám Java
				editor s režimom ladenia a navyše v grafickom editore
				Inkscape sú zobrazované úplne iné súradnice bodov cesty,
				než v zdrojovom kóde SVG a nedá sa to prepnúť – nakoniec
				som však našiel spôsob ako to obísť). Treba to otestovať,
				len neviem ako to opraviť (pozn.: po sérii neúspešných
				pokusov). Niekedy to vyzerá, ako keby bola x-ová súradnicu
				vypočítaná správne a y-ovú nie, ale to je len jeden
				z príznakov. (Neprídem na to asi tak ľahko.)

				S odstupom času (pozn.: ale ešte ďaleko od termínu
				vyriešenia) som to skúšal opäť riešiť, ale už som ani
				nedokázal nájsť na disku SVG súbory, ktoré by vykresľovalo
				chybne. Viem, že sa také dávnejšie náhodne vyskytli pri hre
				Asteroidy, ale to bola iná chyba a tú som opravil… Potom sa
				zrazu vyskytlo niečo úplne iné, ale neuložil som si to
				a terazto neviem nájsť. Asi zostáva len čakať, kedy mi tento
				skrytý problém opäť „vybuchne do tváre.“

				————————————————————————
				To sa nakoniec stalo 28. 4. 2020. Problém s „príkazom“
				'z'/'Z' však nevyplával na povrch okamžite. Ladenie bolo
				rozkúskované v troch dňoch – do 30. 4. 2020. Mal som
				šťastie, že sa mi podarilo vypracovať veľa vhodného
				testovacieho materiálu k príkladu Kolotoč a tiež že som
				našiel spôsob ako si zobraziť korešpondujúce súradnice
				v Inkscape.
			*/

			if (relatívne)
			{
				if (otáčaj)
				{
					double uhol = Math.toRadians(odchýlka);
					double sinUhol = Math.sin(uhol);
					double cosUhol = Math.cos(uhol);
					xx = x0 * cosUhol - y0 * sinUhol;
					// xx = x0 * cosUhol + y0 * sinUhol;
					// ^^ Takto to odporúčali v draft dokumentácii k SVG 2.
					//    Stratil som deň hľadaním chyby, ale moja vina,
					//    lebo rovnice rotácie poznám, ale nechal som sa
					//    nimi pomýliť – myslel som si, že to nemôžu mať
					//    zle a že to v ich prípade tak má byť alebo čo…
					yy = x0 * sinUhol + y0 * cosUhol;
				}
				else
				{
					xx = x0;
					yy = y0;
				}

				if (ln >= 1)
				{
					xx += lp[0];
					yy += lp[1];
				}
			}
			else
			{
				xx = x0;
				yy = y0;
			}

			if (1 == uložBod || -1 == uložBod)
			{
				if (ln >= 1)
				{
					double Δx = xx - lp[0];
					double Δy = yy - lp[1];

					if (Δx != 0 || Δy != 0)
					{
						lOdchýlka = dajUhol(Δx, Δy);
						if (otáčaj) odchýlka = lOdchýlka;
					}
				}

				if (1 == uložBod)
				{
					lp[0] = xx;
					lp[1] = yy;
				}
			}
			else if (2 == uložBod)
			{
				lp[2] = xx;
				lp[3] = yy;
			}

			if (ln < uložBod) ln = uložBod;

			// System.out.println(Svet.F(xx, 10, 5) + "; " + Svet.F(yy, 10, 5) +
			// 	"\n  lp: " + Svet.F(lp0, 10, 5) + "; " + Svet.F(lp1, 10, 5));
		}

		// Vypočíta smer z rozdielu súradníc – používa sa na nastavenie
		// aktuálneho pootočenia (tzv. „bearingu“)
		private double dajUhol(double Δx, double Δy)
		{
			if (Δx == 0 && Δy == 0) return 360;
			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		// Pridá do cesty príkaz posunutia (moveTo)
		public void pridajPresunutie()
		{
			if (n >= 2)
			{
				dajSúradnice(p[0], p[1], 1);
				lp[4] = xx; lp[5] = yy;
				odchýlka = lOdchýlka = 0.0;

				cesta.moveTo(xx, yy);
				režim = 'l';
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia rovnej
		// čiary/úsečky (lineTo) – berie do úvahy rôzne aktuálne
		// nastavenia (relatívne súradnice, pootočenie)
		public void pridajČiaru()
		{
			if (n >= 2)
			{
				dajSúradnice(p[0], p[1], 1);
				cesta.lineTo(xx, yy);
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia horizontálnej
		// úsečky (pozri aj pridajČiaru)
		public void pridajHorizontálnuČiaru()
		{
			if (n >= 1)
			{
				dajSúradnice(p[0], relatívne ? 0 : yy, 1);
				cesta.lineTo(xx, yy);
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia vertikálnej
		// úsečky (pozri aj pridajČiaru)
		public void pridajVertikálnuČiaru()
		{
			if (n >= 1)
			{
				dajSúradnice(relatívne ? 0 : xx, p[0], 1);
				cesta.lineTo(xx, yy);
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia bézierovej
		// krivky
		public void pridajBézierovuKrivku() // c a s
		{
			if (skrátene && n >= 4)
			{
				double x1, y1;

				if (ln >= 2)
				{
					x1 = 2 * lp[0] - lp[2];
					y1 = 2 * lp[1] - lp[3];
				}
				else if (ln >= 1)
				{
					x1 = 2 * lp[0];
					y1 = 2 * lp[1];
				}
				else x1 = y1 = 0.0;

				dajSúradnice(p[0], p[1], 2);
				double x2 = xx, y2 = yy;

				dajSúradnice(p[2], p[3], 1);
				double x3 = xx, y3 = yy;

				cesta.curveTo(x1, y1, x2, y2, x3, y3);
				n = 0;
			}
			else if (n >= 6)
			{
				dajSúradnice(p[0], p[1], 0);
				double x1 = xx, y1 = yy;

				dajSúradnice(p[2], p[3], 2);
				double x2 = xx, y2 = yy;

				dajSúradnice(p[4], p[5], 1);
				double x3 = xx, y3 = yy;

				cesta.curveTo(x1, y1, x2, y2, x3, y3);
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia kvadratickej
		// krivky
		public void pridajKvadratickúKrivku() // q a t
		{
			if (skrátene && n >= 2)
			{
				double x1, y1;

				if (ln >= 2)
				{
					x1 = 2 * lp[0] - lp[2];
					y1 = 2 * lp[1] - lp[3];
				}
				else if (ln >= 1)
				{
					x1 = 2 * lp[0];
					y1 = 2 * lp[1];
				}
				else x1 = y1 = 0.0;

				dajSúradnice(p[0], p[1], 2);
				double x2 = xx, y2 = yy;

				cesta.quadTo(x1, y1, x2, y2);
				n = 0;
			}
			else if (n >= 4)
			{
				dajSúradnice(p[0], p[1], 2);
				double x1 = xx, y1 = yy;

				dajSúradnice(p[2], p[3], 1);
				double x2 = xx, y2 = yy;

				cesta.quadTo(x1, y1, x2, y2);
				n = 0;
			}
		}

		// Pridá do cesty segmet podľa príkazu kreslenia elipsového oblúka
		public void pridajOblúk()
		{
			if (n >= 7)
			{
				n = 0;

				if (0 == p[0] || 0 == p[1])
				{
					// Pôvodne mal byť tento stav považovaný za chybu,
					// ale neskôr som sa v štandarde dočítal, že tento
					// prípad má byť riešený nakreslením úsečky do
					// cieľového bodu, takže nasledujúca výnimka bola aj
					// s unikátnym jazykovým identifikátorom zrušená…
					//
					// throw new GRobotException(
					// 	"Oblúk SVG cesty nemá riešenie! (" +
					// 	"Jeden z rozmerov elipsy je nulový." +
					// 	")", "svgPathArcUnsolvable",
					// 	"Jeden z rozmerov elipsy je nulový.");

					dajSúradnice(p[5], p[6], 1);
					cesta.lineTo(xx, yy);
				}
				else
				{
					double x1, y1;

					if (ln >= 1)
					{
						x1 = lp[0];
						y1 = lp[1];
					}
					else x1 = y1 = 0.0;

					dajSúradnice(p[5], p[6], 1);
					double x2 = xx, y2 = yy;

					double Δx = x2 - x1;
					double Δy = y2 - y1;

					if (0 != Δx || 0 != Δy)
					{
						double a = Math.abs(p[0]);
						double b = Math.abs(p[1]);
						double ϕ = p[2];

						boolean veľký = p[3] != 0.0;
						boolean klenutý = p[4] != 0.0;

						double radϕ = Math.toRadians(ϕ);
						double cosϕ = Math.cos(radϕ);
						double sinϕ = Math.sin(radϕ);

						double Δx_ = -Δx / 2;
						double Δy_ = -Δy / 2;

						double Σx_ = (x1 + x2) / 2;
						double Σy_ = (y1 + y2) / 2;

						double x1_ = Δx_ * cosϕ + Δy_ * sinϕ;
						double y1_ = Δy_ * cosϕ - Δx_ * sinϕ;

						double Λ = ((x1_ * x1_) / (a * a)) +
							((y1_ * y1_) / (b * b));

						if (Λ > 1.0)
						{
							Λ = Math.sqrt(Λ);
							a = a * Λ;
							b = b * Λ;

							Λ = ((x1_ * x1_) / (a * a)) +
								((y1_ * y1_) / (b * b));
						}

						double ay = a * y1_;
						double bx = b * x1_;

						double ay2 = ay * ay;
						double bx2 = bx * bx;

						double SQ = Math.sqrt(
							(a * a * b * b - ay2 - bx2) / (ay2 + bx2));
						if (!Double.isFinite(SQ)) SQ = 0.0;
						if (veľký == klenutý) SQ = -SQ;

						double x0_ = SQ * ay / b;
						double y0_ = -SQ * bx / a;

						double x0 = x0_ * cosϕ - y0_ * sinϕ + Σx_;
						double y0 = x0_ * sinϕ + y0_ * cosϕ + Σy_;

						double Φx1 = (x1_ - x0_) / a;
						double Φy1 = (y1_ - y0_) / b;
						double Φx2 = (-x1_ - x0_) / a;
						double Φy2 = (-y1_ - y0_) / b;

						double D = Φx1 * Φx1 + Φy1 * Φy1;

						double t1 = Math.toDegrees(
							Math.acos(Φx1 / Math.sqrt(D)));
						if (Φy1 < 0) t1 = -t1;

						double Δt = Math.toDegrees(Math.acos(
							(Φx1 * Φx2 + Φy1 * Φy2) /
							Math.sqrt(D * (Φx2 * Φx2 + Φy2 * Φy2))));
						if (Φx1 * Φy2 - Φy1 * Φx2 < 0) Δt = -Δt;

						if (!klenutý && Δt > 0) Δt -= 360;
						else if (klenutý && Δt < 0) Δt += 360;

						t1 = -t1;
						Δt = -Δt;

						Shape oblúk = AffineTransform.getRotateInstance(
							Math.toRadians(ϕ), x0, y0).
							createTransformedShape(new Arc2D.Double(
								x0 - a, y0 - b, 2 * a, 2 * b, t1, Δt,
								Arc2D.OPEN));

						cesta.append(oblúk, true);

						double opt = (t1 + Δt) % 360;
						if (opt < 0) opt += 360;

						lOdchýlka = Math.toDegrees(Math.atan(b /
							(a * (Math.tan(Math.toRadians(t1 + Δt)))))) + ϕ;

						if (opt > 180) lOdchýlka += 180;
						if (Δt >= 0) lOdchýlka += 180;

						if (otáčaj) odchýlka = lOdchýlka;
					}
				}
			}
		}

		// Spracuje príkaz zmeny aktuálneho pootočenia (tzv. „bearingu“)
		public void spracujOdchýlku()
		{
			if (n >= 1)
			{
				if (relatívne)
					odchýlka += p[0];
				else
					odchýlka = p[0];

				n = 0;
			}
		}

		// Spracuje ďalšie číslo v zásobníku – pridá ho do iného zásobníka
		// a po nazbieraní dostatočného objemu údajov pre aktuálny príkaz
		// pridá do aktuálnej cesty ďalší segment…
		public void spracujČíslo()
		{
			double číslo = údajeTvaru.ďalšieČíslo();

			if (n < p.length)
			{
				if (Double.isFinite(číslo)) p[n++] = číslo;
				pridajSegment();
			}
			else
			{
				GRobotException.vypíšChybovéHlásenie("Import SVG " +
					"cesty pravdepodobne prešiel (chybne) do neznámeho " +
					"režimu. Jeden alebo viacero prvkov definície " +
					"nebolo možné korektne spracovať…");
			}
		}

		// Spracuje (všetky) údaje cesty…
		public void spracuj()
		{
			while (údajeTvaru.nieJeKoniec())
			{
				if (ďalšieJeRiadiacePísmeno())
					zmeňRežim(); else spracujČíslo();
			}
		}
	}

	// Inštancia triedy slúžiacej na spracovanie cesty (definovanej vyššie)
	private SpracovanieCesty spracovanieCesty = new SpracovanieCesty();

	// Spracuje aktuálny element ako cestu a vytvorí a pridá
	// prislúchajúci tvar do vnútorného zoznamu tvarov
	private void spracujCestu()
	{
		if (atribúty.containsKey("d"))
		{
			++početPridanýchTvarov;
			spracovanieCesty.reset(atribúty.get("d"));
			spracovanieCesty.spracuj();
			tvary.add(new Tvar(spracovanieCesty.cesta, atribúty));
		}
	}

	// Spracuje aktuálny element ako text; vytvorí a pridá obrys textu
	// do vnútorného zoznamu tvarov
	private void spracujText() throws XMLStreamException
	{
		StringBuffer text = new StringBuffer();

		// System.out.println("Atribúty:");
		// for (Map.Entry<String, String> atribút : atribúty.entrySet())
		// {
		// 	String kľúč = atribút.getKey();
		// 	String hodnota = atribút.getValue();
		// 	System.out.println("  [" + kľúč + "]: “" + hodnota + "”");
		// }


		// Čítam všetko doradu bez ohľadu na prípadné ďalšie atribúty…
		while (čítanie.hasNext())
		{
			if (čítanie.isEndElement())
			{
				String prefix = čítanie.getPrefix();

				if (null == prefix || prefix.isEmpty())
				{
					String meno = čítanie.getLocalName();
					if ("text".equals(meno)) break;
				}
			}
			else if (čítanie.isCharacters())
			{
				text.append(čítanie.getText());
			}

			čítanie.next();
		}


		// Prečítam vlastnosti textu (font, polohu)
		double x, y, fontSize;
		String fontFamily;
		int fontStyle;

		++početPridanýchTvarov;

		if (atribúty.containsKey("x"))
			x = reťazecNaČíslo(atribúty.get("x"));
		else
			x = 0.0;

		if (atribúty.containsKey("y"))
			y = reťazecNaČíslo(atribúty.get("y"));
		else
			y = 0.0;


		if (atribúty.containsKey("font-family"))
			fontFamily = atribúty.get("font-family");
		else
			fontFamily = "Tahoma";

		if (atribúty.containsKey("font-size"))
			fontSize = reťazecNaČíslo(atribúty.get("font-size"));
		else
			fontSize = 15.0;

		fontStyle = Font.PLAIN;

		if (atribúty.containsKey("font-style") &&
			atribúty.get("font-style").equalsIgnoreCase("italic"))
			fontStyle |= Font.ITALIC;

		if (atribúty.containsKey("font-weight") &&
			atribúty.get("font-weight").equalsIgnoreCase("bold"))
			fontStyle |= Font.BOLD;


		// Vytvorím písmo a textový tvar…
		Písmo písmo = new Písmo(fontFamily, fontStyle, fontSize);

		tvary.add(new Tvar(new SimpleTextShape(
			x, y, písmo, text.toString(), Svet.grafikaSveta1), atribúty));
	}

	// Do tohto zoznamu si metóda spracujLokálnyŠtýl uloží zoznam
	// CSS definícií, ktoré premení na XML/SVG atribúty.
	private static final TreeSet<String> prepíšZoŠtýlu = new TreeSet<>();

	// Do tohto zoznamu si metóda spracujLokálnyŠtýl uloží zoznam
	// CSS definícií, ktoré pripojí k jestvujúcim XML/SVG atribútom.
	private static final TreeSet<String> pripojZoŠtýlu = new TreeSet<>();

	// Spracuje atribút ‚style‘ tak, že vybranými CSS definíciami prepíše
	// jestvujúce XML atribúty a vybrané CSS definície pripojí
	// k jestvujúcim XML atribútom aktuálneho SVG elementu, pričom
	// v obidvoch prípadoch ak takéto atribúty nejestvujú, tak ich nanovo
	// definuje/vytvorí (a zvyšok atribútu ‚style‘ ignoruje).
	private void spracujLokálnyŠtýl()
	{
		String style = atribúty.get("style");

		if (prepíšZoŠtýlu.isEmpty())
		{
			prepíšZoŠtýlu.add("d");
			prepíšZoŠtýlu.add("x");
			prepíšZoŠtýlu.add("y");
			prepíšZoŠtýlu.add("r");
			prepíšZoŠtýlu.add("cx");
			prepíšZoŠtýlu.add("cy");
			prepíšZoŠtýlu.add("rx");
			prepíšZoŠtýlu.add("ry");
			prepíšZoŠtýlu.add("x1");
			prepíšZoŠtýlu.add("y1");
			prepíšZoŠtýlu.add("x2");
			prepíšZoŠtýlu.add("y2");
			prepíšZoŠtýlu.add("width");
			prepíšZoŠtýlu.add("height");
			prepíšZoŠtýlu.add("points");
			prepíšZoŠtýlu.add("fill");
			prepíšZoŠtýlu.add("stroke");
			prepíšZoŠtýlu.add("fill-opacity");
			prepíšZoŠtýlu.add("stroke-opacity");
			prepíšZoŠtýlu.add("stroke-width");
			prepíšZoŠtýlu.add("font-family");
			prepíšZoŠtýlu.add("font-size");
			prepíšZoŠtýlu.add("font-style");
			prepíšZoŠtýlu.add("font-weight");
		}

		if (pripojZoŠtýlu.isEmpty())
		{
			pripojZoŠtýlu.add("transform");
			// pripojZoŠtýlu.add("???");
		}

		if (null != style)
		{
			int index1 = 0, index2, index3, length = style.length();
			do
			{
				index2 = style.indexOf(";", index1);
				if (-1 == index2) index2 = length;
				String definícia = style.substring(index1, index2);

				index3 = definícia.indexOf(":");
				if (-1 != index3)
				{
					String atribút = definícia.substring(0, index3).trim();
					String hodnota = definícia.substring(index3 + 1).trim();

					if (prepíšZoŠtýlu.contains(atribút))
						atribúty.put(atribút, hodnota);

					if (pripojZoŠtýlu.contains(atribút))
					{
						String pôvodnáHodnota = atribúty.get(atribút);
						if (null == pôvodnáHodnota)
							atribúty.put(atribút, hodnota);
						else
							atribúty.put(atribút,
								pôvodnáHodnota.trim() + " " + hodnota);
					}
				}

				index1 = 1 + index2;
			}
			while (index2 < length);
			// atribúty.remove("style");
		}
	}

	// Súkromná metóda slúžiaca na rekurzívnu analýzu SVG údajov
	private void čítajSVG() throws XMLStreamException
	{
		zásobníkAtribútov.clear();

		while (čítanie.hasNext())
		{
			if (čítanie.isEndElement())
			{
				String prefix = čítanie.getPrefix();

				if (null == prefix || prefix.isEmpty())
				{
					String meno = čítanie.getLocalName();

					if ("svg".equals(meno)) return;
					if ("g".equals(meno)) zásobníkAtribútov.pop();
				}
			}
			else if (čítanie.isStartElement())
			{
				String prefix = čítanie.getPrefix();

				if (null == prefix || prefix.isEmpty())
				{
					String meno = čítanie.getLocalName();

					// Rekurzívne čítanie elementu svg
					if ("svg".equals(meno))
					{
						čítanie.next();
						čítajSVG();
					}
					else
					{
						// Čítanie ostatných elementov
						// (v rámci svg elementu)
						int počet = čítanie.getAttributeCount();

						if (0 != počet)
						{
							atribúty.clear();

							{
								Atribúty nadradenéAtribúty = null;
								if (!zásobníkAtribútov.isEmpty())
								{
									nadradenéAtribúty =
										zásobníkAtribútov.peek();
									atribúty.putAll(nadradenéAtribúty);
								}

								for (int i = 0; i < počet; ++i)
								{
									if (čítanie.isAttributeSpecified(i))
									{
										String atribút = čítanie.
											getAttributeLocalName(i);
										String hodnota = čítanie.
											getAttributeValue(i);

										if (null != nadradenéAtribúty &&
											atribút.equals("transform") &&
											nadradenéAtribúty.containsKey(
												atribút))
										{
											hodnota = nadradenéAtribúty.
												get(atribút) + " " + hodnota;
										}

										atribúty.put(atribút, hodnota);
									}
								}
							}

							spracujLokálnyŠtýl();

							if ("g".equals(meno))
							{
								Atribúty atr = new Atribúty();

								if (!zásobníkAtribútov.isEmpty())
								{
									Atribúty nadradenéAtribúty =
										zásobníkAtribútov.peek();

									if (nadradenéAtribúty.containsKey(
										"transform") &&
										atribúty.containsKey(
										"transform"))
									{
										String hodnota =
											nadradenéAtribúty.get(
												"transform") + " " +
											atribúty.get("transform");
										atribúty.put("transform", hodnota);
									}
									atr.putAll(nadradenéAtribúty);
									atr.putAll(atribúty);
								}
								else atr.putAll(atribúty);

								zásobníkAtribútov.push(atr);
							}
							else if ("rect".equals(meno))
								spracujObdĺžnik();
							else if ("circle".equals(meno))
								spracujKružnicu();
							else if ("ellipse".equals(meno))
								spracujElipsu();
							else if ("line".equals(meno))
								spracujÚsečku();
							else if ("polyline".equals(meno))
								spracujLomenúCestu(false);
							else if ("polygon".equals(meno))
								spracujLomenúCestu(true);
							else if ("path".equals(meno))
								spracujCestu();
							else if ("text".equals(meno))
								spracujText();
						}
					}
				}
			}

			čítanie.next();
		}
	}


	/**
	 * <p>Spracuje zadaný reťazec ako súčasť SVG definície a v prípade, že
	 * sú nájdené korektné XML/SVG údaje tvarov, pribudnú podľa nich do
	 * vnútorného zásobníka tejto inštancie ďalšie tvary. (Pozri aj
	 * metódy {@link #čítaj(String) čítaj(meno)}, {@link #pridaj(Shape,
	 * String[]) pridaj(tvar, atribúty)}, {@link #pridajText(String,
	 * GRobot, String[]) pridajText(tvar, tvorca, atribúty)}
	 * a {@link #dajSVG(int) dajSVG(index)}.)</p>
	 * 
	 * @param xmlSVG vstupné XML/SVG údaje
	 * @return počet tvarov, ktoré sa podarilo vo vstupných údajoch
	 *     rozpoznať alebo {@code num-1} ak analýza údajov zlyhá
	 * 
	 * @throws GRobotException ak nastane chyba počas spracovania
	 *     vstupných údajov
	 */
	public int pridajSVG(String xmlSVG)
	{
		početPridanýchTvarov = -1;

		// „Otvorí“ zadaný reťazec ako prúd…
		try
		{
			if (null == xmlVstup) xmlVstup = XMLInputFactory.newFactory();
			čítanie = xmlVstup.createXMLStreamReader(
				new ByteArrayInputStream(("<svg>" + xmlSVG +
					"</svg>").getBytes("UTF-8")), "UTF-8");
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG údajov.",
				"svgImportError", e);
		}


		try
		{
			početPridanýchTvarov = 0;

			while (čítanie.hasNext())
			{
				if (čítanie.isStartElement())
				{
					String prefix = čítanie.getPrefix();

					if (null == prefix || prefix.isEmpty())
					{
						// Začne čítanie elementu svg
						if ("svg".equals(čítanie.getLocalName()))
						{
							čítanie.next();
							čítajSVG();
						}
					}
				}

				čítanie.next();
			}
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG údajov.",
				"svgImportError", e);
		}


		// Bezpečné zavretie prúdu
		try
		{
			if (null != čítanie) čítanie.close();
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG údajov.",
				"svgImportError", e);
		}

		// Likvidácia dočasnej inštancie čítania prúdu
		čítanie = null;

		// Metóda má vrátiť počet rozpoznaných (a pridaných) tvarov:
		return početPridanýchTvarov;
	}


	/**
	 * <p>Metóda postupne analyzuje XML údaje v zadanom SVG súbore ({@code 
	 * meno}) a do vnútorného zásobníka uloží všetky tvary, ktoré v rámci
	 * analyzovaných údajov nájde a to bez ohľadu na ich umiestnenie vo
	 * vrstvách, skupinách alebo v rámci definícií takzvaných značiek
	 * (angl. marker) a podobne. Ak boli čítanie súboru a jeho analýza
	 * úspešné, tak metóda vráti počet spracovaných tvarov (ktoré pribudli
	 * do vnútorného zásobníka inštancie), inak vráti hodnotu {@code 
	 * num-1}.</p>
	 * 
	 * <p>Importované tvary sú spracúvané v predvolenom súradnicovom
	 * priestore Javy, v ktorom „nula“ (to jest počiatok súradnicovej
	 * sústavy) sa nachádza v ľavom hornom rohu plátna a y-ová súradnica
	 * stúpa smerom nadol. Tento súradnicový priestor je bežne používaný
	 * v oblasti 2D počítačovej grafiky a používa ho aj formát SVG.</p>
	 * 
	 * <p>Všetky tvary uložené vo vnútornom zásobníku a/alebo niektoré
	 * ich atribúty (súvisiace s ich vizuálnymi vlastnosťami) je možné
	 * získať metódami: {@link #daj(int) daj(index)},
	 * {@link #dajAtribút(int, String) dajAtribút(index, meno)},
	 * {@link #farbaVýplne(int) farbaVýplne(index)},
	 * {@link #farbaČiary(int) farbaČiary(index)},
	 * {@link #hrúbkaČiary(int) hrúbkaČiary(index)} atď.</p>
	 * 
	 * @param meno názov súboru, z ktorého majú byť prečítané
	 *     (importované) SVG tvary
	 * @return počet prečítaných tvarov (z množiny podporovaných tvarov)
	 *     alebo {@code num-1} ak sa čítanie tvarov ani nemohlo začať
	 *     (napríklad preto, že súbor nejestvuje)
	 * 
	 * @throws GRobotException ak nastane chyba počas spracovania súboru
	 */
	public int čítaj(String meno)
	{
		if (null == meno)
			throw new GRobotException(
				"Názov súboru nesmie byť zamlčaný.",
				"fileNameOmitted", null, new NullPointerException());

		početPridanýchTvarov = -1;

		FileNotFoundException notFound = null;
		boolean ešteRaz = true;

		// Skúsi otvoriť prúd z aktuálnej lokality
		// alebo z aktuálneho zoznamu ciest classpath:
		try
		{
			if (null == xmlVstup) xmlVstup = XMLInputFactory.newFactory();
			čítanie = xmlVstup.createXMLStreamReader(
				Súbor.dajVstupnýPrúdSúboru(meno), "UTF-8");
			ešteRaz = false;
		}
		catch (FileNotFoundException e)
		{
			notFound = e;
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG súboru „" +
				meno + ".“", "svgReadError", e);
		}


		if (ešteRaz)
		{
			// Posledný pokus – otvoriť súbor ako zdroj:
			try
			{
				čítanie = xmlVstup.createXMLStreamReader(
					Súbor.dajVstupnýPrúdZdroja(meno), "UTF-8");
			}
			catch (Exception e)
			{
				// ‼ // Tento prístup bol vhodný v triede Súbor,
				// ‼ // dokumentácia tejto metódy však oznamuje
				// ‼ // iný prístup…
				// ‼ throw new GRobotException(
				// ‼ 	"Chyba pri spracovaní SVG súboru „" +
				// ‼ 	meno + ".“", "svgReadError", e);
				return početPridanýchTvarov;
			}
		}


		try
		{
			početPridanýchTvarov = 0;

			while (čítanie.hasNext())
			{
				if (čítanie.isStartElement())
				{
					String prefix = čítanie.getPrefix();

					if (null == prefix || prefix.isEmpty())
					{
						// Začne čítanie elementu svg
						if ("svg".equals(čítanie.getLocalName()))
						{
							čítanie.next();
							čítajSVG();
						}
					}
				}

				čítanie.next();
			}
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG súboru „" +
				meno + ".“", "svgReadError", e);
		}


		// Bezpečné zavretie prúdu
		try
		{
			if (null != čítanie) čítanie.close();
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Chyba pri spracovaní SVG súboru „" +
				meno + ".“", "svgReadError", e);
		}

		// Likvidácia dočasnej inštancie čítania prúdu
		čítanie = null;

		// Metóda má vrátiť počet „prečítaných“ (pridaných) tvarov:
		return početPridanýchTvarov;
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public int citaj(String meno) { return čítaj(meno); }
}
