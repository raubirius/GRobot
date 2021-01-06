
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2021 by Roman Horváth
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

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static java.lang.Math.*;

/**
 * <p>Táto trieda umožňuje definovať a pracovať so sériou bodov (zvaných roj)
 * umiestnených v trojrozmernom priestore. Na zobrazenie scény roja je
 * použitá bodová projekcia. Body môžu byť prepojené {@linkplain Bod#spoj
 * spojmi}, čím môžu byť vytvárané drôtené modely a v každom bode môže byť
 * definovaný objekt na {@linkplain Bod#kreslenie nakreslenie,} čím sa dajú
 * vytvárať jednoduché 3D scény zložené z 2D kulís (pozri príklad kolotoča
 * nižšie).</p>
 * 
 * <p>Súradnicový systém roja je orientovaný ľavotočivo, čiže keď sa pozrieme
 * na plochu xy zvrchu tak, že y-ová os smeruje doprava (rastom kladných
 * hodnôt), tak x-ová os smeruje hore (pozri obrázok nižšie). Alebo keď sa
 * pozeráme na počiatok súradnicovej sústavy z oktetu všetkých troch kladných
 * súradníc tak, že z-ová súradnica smeruje hore, tak x-ovú os máme po pravej
 * ruke a y-ovú po ľavej (pozri obrázok nižšie).</p>
 * 
 * <!--
 * Poznámka pre programátora: Pozri metódu roja transformuj(Bod bod) a v nej
 * komentár s „pravorukým“ (right-handed) t. j. pravotočivým systémom.
 * -->
 * 
 * <p>Každý {@linkplain Bod bod roja} má definované množstvo atribútov, ktoré
 * majú poskytovať čo najväčšiu flexibilitu. Väčšina z nich slúži na
 * umiestnenie bodu do priestoru. Základná trojica atribútov sú pôvodné
 * súradnice bodu v priestore [{@linkplain Bod#x0 x0}, {@linkplain Bod#y0
 * y0}, {@linkplain Bod#z0 z0}]. Z nich sú s pomocou atribútov posunutia
 * [{@linkplain Bod#dx dx}, {@linkplain Bod#dy dy}, {@linkplain Bod#dz dz}],
 * stredu rotácie [{@linkplain Bod#xs xs}, {@linkplain Bod#ys ys},
 * {@linkplain Bod#zs zs}] a uhlov rotácie ({@linkplain Bod#alfa alfa},
 * {@linkplain Bod#beta beta}, {@linkplain Bod#gama gama}) vypočítané
 * takzvané lokálne transformované súradnice v priestore [{@linkplain Bod#x1
 * x1}, {@linkplain Bod#y1 y1}, {@linkplain Bod#z1 z1}]. Ďalším krokom je
 * výpočet takzvaných konečných súradníc [{@linkplain Bod#x2 x2},
 * {@linkplain Bod#y2 y2}, {@linkplain Bod#z2 z2}], čo sú globálne
 * transformované súradnice, to jest riadia sa hodnotou aktuálnej
 * transformačnej matice roja, ktorá je prepočítavaná z polohy a orientácie
 * kamery (pozri napríklad: {@link #nastavUhly(double, double, double)
 * nastavUhly}, {@link #nastavStredOtáčania(double, double, double)
 * nastavStredOtáčania}, {@link #nastavKameru(double, double, double)
 * nastavKameru}), {@linkplain #mierka() mierky}, prípadne ďalších
 * (automaticky zisťovaných) hodnôt. Posledným krokom je výpočet polohy bodu
 * premietnutej na plátno [{@linkplain Bod#x3 x3}, {@linkplain Bod#y3 y3}]
 * a korešpondujúceho faktora rozmeru objektu kresleného v konkrétnom bode
 * {@linkplain Bod#z3 z3}.</p>
 * 
 * <table class="centered">
 * <tr><td
 * style="vertical-align:middle"><p><image>suradnicovy-system-roja-01.png<alt/>Súradnicový systém roja „zvrchu.“</image>Ľavotočivý súradnicový systém pri pohľade
 * „zvrchu.“</p></td><td> </td>
 * <td
 * style="vertical-align:middle"><p><image>suradnicovy-system-roja-02.png<alt/>Súradnicový systém roja „spredu.“" height="75%</image>Ľavotočivý súradnicový systém pri
 * pohľade „spredu.“</p></td></tr>
 * </table>
 * 
 * <p>V <a href="resources/test-roja.7z" target="_blank">tomto balíčku
 * (7z)</a> je dostupný na prevzatie miniprojekt obsahujúci ovládač roja
 * a od neho odvodenú jednoduchú triedu testu roja s niekoľkými príkladmi
 * použitia roja.</p>
 * 
 * <p><image>test-roja.png<alt/>Ukážka výstupu testu roja.</image>Ukážka
 * možného výstupu príkladu testu roja s implementáciou ovládača roja.</p>
 * 
 * <p>Na jeho základe je postavený nasledujúci príklad s kolotočom.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad implementuje trojrozmernú kolotočovú ponuku zloženú
 * z čiarových ikon vo formáte SVG.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;
	{@code kwdimport} {@link java.awt.Shape java.awt.Shape};

	{@code comm// SVG ikonky boli vyrobené (s pomocou vektorového grafického editora}
	{@code comm// Inkscape) z bitmapovej verzie ikon voľne dostupných na webovej stránke:}
	{@code comm// https://icon-library.net/icon/menu-icon-png-3-lines-20.html.}
	{@code comm// }
	{@code comm// Ďalšie odporúčané zdroje:}
	{@code comm// }
	{@code comm// • Výborný SVG tester: https://codepen.io/AmeliaBR/pen/JoYNEZ?editors=1000}
	{@code comm// • Coyier, Chris. 2018. The SVG ‘path’ Syntax : An Illustrated Guide.}
	{@code comm//   ⟨https://css-tricks.com/svg-path-syntax-illustrated-guide/⟩.}
	{@code comm// • https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths}
	{@code comm// • https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/d}

	{@code kwdpublic} {@code typeclass} Kolotoč {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Roj a príznak potreby jeho prekreslenia (po zmene parametrov):}
		{@code kwdprivate} {@link Roj Roj} roj = {@code kwdnew} {@link Roj Roj}({@code valthis});
		{@code kwdprivate} {@code typeboolean} prekresliRoj = {@code valtrue};

		{@code comm// Uhol jednotkového pootočenia kolotoča a aktuálne želaný uhol:}
		{@code kwdprivate} {@code typedouble} Δu, želanýUhol = {@code num90.0};

		{@code comm// Zoznam SVG súborov obsahujúcich SVG tvary ikoniek:}
		{@code kwdprivate} {@link String String}[] mená = {@code kwdnew} {@link String String}[]
		{
			{@code srg"ramcek.svg"}, {@code srg"spendlik.svg"}, {@code srg"hudba.svg"}, {@code srg"kompas.svg"}, {@code srg"film.svg"},
			{@code srg"obrazok.svg"}, {@code srg"kalkulacka.svg"}, {@code srg"kava.svg"}, {@code srg"papiere.svg"},
			{@code srg"zakladac.svg"}, {@code srg"mobilna-siet.svg"}, {@code srg"smernik.svg"}, {@code srg"kvapka.svg"},
			{@code srg"sluchadlo.svg"}, {@code comm// …}
		};

		{@code comm// Statická trieda zoskupujúca funkcionalitu prevodu čiarovej ikonky}
		{@code comm// vo formáte SVG do tvarov Javy.}
		{@code kwdpublic} {@code kwdstatic} {@code typeclass} Ikonka implements {@link KreslenieTvaru KreslenieTvaru}
		{
			{@code comm// Atribút na prepínanie zobrazenia ladiacich informácií:}
			{@code kwdprivate} {@code kwdstatic} {@code typeboolean} info = {@code valfalse};

			{@code comm// Vnútorné atribúty kreslenia ikonky:}
			{@code kwdprivate} {@link java.lang.String String} meno;
			{@code kwdprivate} {@link java.awt.Shape Shape}[] tvary;
			{@code kwdprivate} {@link Farba Farba}[] výplne;
			{@code kwdprivate} {@link Farba Farba}[] čiary;

			{@code comm// Konštruktor ikonky.}
			{@code kwdpublic} Ikonka({@link String String} meno)
			{
				{@code valthis}.meno = meno;
				SVGnaTvary(meno);
			}


			{@code comm// Metóda prevádzajúca tvary zo zadaného SVG súboru na tvary Javy.}
			{@code kwdprivate} {@code typevoid} SVGnaTvary({@link String String} meno)
			{
				{@code kwdtry}
				{
					{@code comm// Vyčistenie inštancie svgPodpora od predchádzajúceho}
					{@code comm// čítania:}
					{@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#vymaž() vymaž}();

					{@code comm// Overenie, či bol súbor korektne prečítaný:}
					{@code kwdif} (-{@code num1} == {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#čítaj(String) čítaj}(meno))
					{
						{@code comm// Predvolený tvar kružnice v prípade zlyhania čítania}
						{@code comm// súboru:}
						tvary = {@code kwdnew} {@link Shape Shape}[] {{@code kwdnew} {@link java.awt.geom.Ellipse2D.Double#Ellipse2D.Double(double, double, double, double) java.awt.geom.Ellipse2D.Double}(
							{@link Svet Svet}.{@link Svet#prepočítajX(double) prepočítajX}(-{@code num10}), {@link Svet Svet}.{@link Svet#prepočítajY(double) prepočítajY}({@code num10}), {@code num20}, {@code num20})};
						výplne = {@code kwdnew} {@link Farba Farba}[] {{@link Farebnosť#biela biela}.{@link Farba#priehľadnejšia(double) priehľadnejšia}({@code num0.6})};
						čiary = {@code kwdnew} {@link Farba Farba}[] {{@link Farebnosť#ružová ružová}};
					}
					{@code kwdelse}
					{
						{@code comm// Získanie súradnice stredu:}
						{@link knižnica.Bod Bod} stred = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#stredKresby() stredKresby}();

						{@code comm// Výroba transformácie posunutia v súradnicovom}
						{@code comm// priestore Javy (tá sa nižšie použije na posunutie}
						{@code comm// všetkých tvarov):}
						{@link SVGPodpora SVGPodpora}.{@link SVGPodpora.Transformácia Transformácia} posunTam =
							{@code kwdnew} {@link SVGPodpora SVGPodpora}.{@link SVGPodpora.Transformácia#SVGPodpora.Transformácia(int, Double[]) Transformácia}(
								{@link SVGPodpora SVGPodpora}.{@link SVGPodpora.Transformácia Transformácia}.{@link SVGPodpora.Transformácia#POSUN POSUN},
								-{@link Poloha#stred stred}.{@link knižnica.Bod#polohaX() polohaX}(), {@link Poloha#stred stred}.{@link knižnica.Bod#polohaY() polohaY}());

						{@code comm// Uloženie počtu tvarov kresby do pomocnej premennej:}
						{@code typeint} počet = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#počet() počet}();

						{@code comm// Vytvorenie polí s prislúchajúcimi počtami prvkov:}
						tvary = {@code kwdnew} {@link Shape Shape}[počet];
						výplne = {@code kwdnew} {@link Farba Farba}[počet];
						čiary = {@code kwdnew} {@link Farba Farba}[počet];

						{@code comm// Pridanie transformácie ku každému tvaru a zároveň}
						{@code comm// overenie toho, či má byť tento tvar vypĺňaný, kreslený}
						{@code comm// (alebo oboje):}
						{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; počet; ++i)
						{
							{@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#pridajTransformácie(int, SVGPodpora.Transformácia[]) pridajTransformácie}(i, posunTam);
							tvary[i] = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#dajVýsledný(int) dajVýsledný}(i);
							výplne[i] = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#farbaVýplne(int) farbaVýplne}(i);
							čiary[i] = {@link GRobot#svgPodpora svgPodpora}.{@link SVGPodpora#farbaČiary(int) farbaČiary}(i);
						}
					}
				}
				{@code kwdcatch} (Exception e)
				{
					{@code comm// (Toto nastane len výnimočne. Aj neprítomný alebo chybný}
					{@code comm// SVG súbor je signalizovaný inak: návratovou hodnotou}
					{@code comm// metódy čítaj.)}
					e.{@link Exception#printStackTrace() printStackTrace}();
				}
			}

			{@code comm// Kreslenie ikonky.}
			{@code kwdpublic} {@code typevoid} kresli({@link GRobot GRobot} r)
			{
				{@code comm// Všetky ikonky budú kreslené zvislo s hrúbkou čiary určenou}
				{@code comm// aktuálnou mierkou robota:}
				r.{@link GRobot#uhol(double) uhol}({@code num90});
				r.{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}(r.{@link GRobot#mierka() mierka}());


				{@code comm// Poznámka: Prispôsobenie (zmena) niektorých programátorom}
				{@code comm//  vybraných farieb počas kreslenia ikoniek kolotoča je vhodné}
				{@code comm//  potvrdiť práve na tomto mieste tak, že si farbu (alebo farby)}
				{@code comm//  vopred zapamätáme do premennej a potom ňou (nimi) nahradíme}
				{@code comm//  zvolenú kľúčovú farba (resp. farby) v kresbe. V tejto ukážke}
				{@code comm//  (iba na ukážku) nahrádzame ružovú farbu robotom vopred}
				{@code comm//  vygenerovanou náhodnou farbou. V dodaných SVG súboroch je}
				{@code comm//  táto farba (opäť len na ukážku) použitá iba raz – v „bodke“}
				{@code comm//  na obrázku mobilna-siet.svg. Môže však ísť o nahradenie}
				{@code comm//  ľubovoľnej farby ľubovoľnou farbou. Môžu byť tiež využité}
				{@code comm//  verejné atribúty farba a farbaSpoja aktuálne kresleného bodu}
				{@code comm//  roja (pozri atribút Roj.bod). (Spoje i tak v tomto príklade}
				{@code comm//  nevyužívame, tak môžeme využiť atribút rezervovaný na ich}
				{@code comm//  zafarbovanie na vlastné účely.)}

				r.{@link GRobot#náhodnáFarba() náhodnáFarba}();
				{@link Farba Farba} f = r.{@link GRobot#farba() farba}();


				{@code typeint} i = {@code num0};
				{@code kwdfor} ({@link Shape Shape} tvar : tvary)
				{
					{@code kwdif} ({@code valnull} != výplne[i])
					{
						{@code kwdif} ({@link Farebnosť#ružová ružová}.{@link Farba#equals(Object) equals}(výplne[i]))
							r.{@link GRobot#farba(Color) farba}(f);
						{@code kwdelse}
							r.{@link GRobot#farba(Color) farba}(výplne[i]);
						r.{@link GRobot#vyplňTvar(Shape, boolean) vyplňTvar}(tvar, {@code valtrue});
					}

					{@code kwdif} ({@code valnull} != čiary[i])
					{
						{@code kwdif} ({@link Farebnosť#ružová ružová}.{@link Farba#equals(Object) equals}(čiary[i]))
							r.{@link GRobot#farba(Color) farba}(f);
						{@code kwdelse}
							r.{@link GRobot#farba(Color) farba}(čiary[i]);
						r.{@link GRobot#kresliTvar(Shape, boolean) kresliTvar}(tvar, {@code valtrue});
					}
					++i;
				}

				{@code comm// Ladiace informácie ikoniek (meno súboru nad ňou a mierka}
				{@code comm// pod ňou):}
				{@code kwdif} (info)
				{
					r.{@link GRobot#skoč(double) skoč}(r.{@link GRobot#veľkosť() veľkosť}() * {@code num1.5});
					r.{@link GRobot#text(String) text}(meno);
					r.{@link GRobot#odskoč(double) odskoč}(r.{@link GRobot#veľkosť() veľkosť}() * {@code num2.75});
					r.{@link GRobot#text(String) text}(r.{@link GRobot#F(double, int) F}(r.{@link GRobot#mierka() mierka}(), {@code num2}));
				}
			}
		}


		{@code comm// Konštruktor celého kolotoča.}
		{@code kwdprivate} Kolotoč()
		{
			{@code comm// Keby sme chceli mať predvolene zobrazené osi súradnicovej}
			{@code comm// sústavy (ktoré sú dobrou pomôckou pri ladení), volali by sme}
			{@code comm// na tomto mieste tento príkaz: osi();}

			{@code comm// Nastavenie predvolených vlastností roja (pri každom počte}
			{@code comm// bodov je potrebné tieto parametre „doladiť“ – najmä mierku):}
			resetujRoj();
				{@code comm// Poznámka: Rôzne hodnoty od predvolených majú len:}
				{@code comm//  roj.mierka(500);}
				{@code comm//  roj.nastavKameru(0, 50, 30);}

			{@code comm// Výpočet pomocných parametrov, s pomocou ktorých vložíme}
			{@code comm// niekoľko bodov do roja:}
			{@code typeint} n = mená.length; {@code comm// počet bodov}
			{@code typedouble} r = {@code num15};       {@code comm// polomer kružnice, na ktorej budú umestnené}
			Δu = {@code num360.0} / n;      {@code comm// uhol otáčania smerníka (*)}
			{@code typedouble} d = ({@code num2} * {@link Math Math}.{@link Math#PI PI} * r) / n; {@code comm// dĺžka posunu smerníka (*)}

			{@code comm// (*) Smerník slúži na zjednodušenie vkladania bodov do roja.}

			{@code comm// Vypneme predvolené vkladanie spojov smerníkom:}
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#vkladajSpoje vkladajSpoje} = {@code valfalse};

			{@code comm// Uloženie jedného smeru (vektora osi otáčania) do zásobníka, aby}
			{@code comm// sa dal použiť jednoduchší tvar volania metódy „otoč“ smerníka}
			{@code comm// (je to jednotkový vektor v smere osi z):}
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#smerNa(double, double, double) smerNa}({@code num0}, {@code num0}, {@code num1});
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#zálohujSmer() zálohujSmer}();

			{@code comm// Počiatočné nastavenie smerníka (smer je súhlasný so smerom}
			{@code comm// osi y a smerník je vysunutý o polomer otáčania v smere osi x):}
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#smerNa(double, double, double) smerNa}({@code num0}, {@code num1}, {@code num0});
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#posuň(double, double, double) posuň}(-r, {@code num0}, {@code num0});

			{@code comm// Prvé pootočenie smerníka o polovicu uhla (voláme zjednodušený tvar}
			{@code comm// metódy „otoč,“ pretože smer osi otáčania určuje vektor uložený}
			{@code comm// v zásobníku; vyššie):}
			roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#otoč(double) otoč}(Δu / {@code num2.0});
				{@code comm// Inak by bolo treba používať tento tvar volania metódy:}
				{@code comm//  roj.smerník.otoč(0, 0, 1, -Δu / 2.0);}

			{@code comm// Cyklus vkladania bodov:}
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; n; ++i)
			{
				{@code comm// Pred každým vložením bodu potočíme smerník:}
				roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#otoč(double) otoč}(-Δu);
					{@code comm// Alternatívne (bez vektora v zásobníku spomínaného vyššie)}
					{@code comm// by bolo treba použiť tento tvar príkazu:}
					{@code comm//  roj.smerník.otoč(0, 0, 1, Δu);}

				{@code comm// Vložíme bod do roja a posunieme smerník:}
				{@link Roj.Bod Roj.Bod} bod = roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#pridajBod() pridajBod}();
				bod.{@link Roj.Bod#kreslenie kreslenie} = {@code kwdnew} Ikonka(mená[i]);
				roj.{@link Roj#smerník smerník}.{@link Roj.Smerník#posuň(double) posuň}(d);
			}

			{@link GRobot#skry() skry}(); {@code comm// (Skrytie hlavného robota.)}

			{@code comm// Keby bolo predvolene zapnuté zobrazenie ladiacich informácií}
			{@code comm// o roji (čo v tomto príklade nie je), tak by sme tu museli vykonať}
			{@code comm// prvé volanie (inak by boli po štarte skryté a zobrazili by sa až}
			{@code comm// po ľubovoľnej zmene zobrazenia roja): infoORoji();}

			{@code comm// Vypnutie automatického prekresľovania a zapnutie časovača:}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}


		{@code comm// Pomocné nástroje na definíciu a kreslenie (resp. skrývanie}
		{@code comm// a zobrazovanie) osí.}
		{@code comm// &#123;&#123;&#123;}
			{@code comm// Koncové body osí:}
			{@code kwdprivate} {@link Roj.Bod Roj.Bod} osX = {@code valnull};
			{@code kwdprivate} {@link Roj.Bod Roj.Bod} osY = {@code valnull};
			{@code kwdprivate} {@link Roj.Bod Roj.Bod} osZ = {@code valnull};

			{@code comm// Vlastný tvar na kreslenie šípky na konci osi.}
			{@code kwdpublic} {@link KreslenieTvaru KreslenieTvaru} šípka = r -&gt;
			{
				r.{@link GRobot#vpravo(double) vpravo}({@code num18});
				r.{@link GRobot#vzad(double) vzad}({@code num14});
				r.{@link GRobot#zdvihniPero() zdvihniPero}();
				r.{@link GRobot#vpred(double) vpred}({@code num14});
				r.{@link GRobot#vľavo(double) vľavo}({@code num36});
				r.{@link GRobot#položPero() položPero}();
				r.{@link GRobot#vzad(double) vzad}({@code num14});
			};

			{@code comm// Definovanie alebo úprava dĺžky osi x.}
			{@code kwdpublic} {@code typevoid} osX({@code typedouble} dĺžka)
			{
				{@code kwdif} ({@code valnull} == osX)
				{
					{@link Roj.Bod Roj.Bod} bod = roj.{@link Roj#pridajBod() pridajBod}();
					bod.{@link Roj.Bod#zobraz zobraz} = bod.{@link Roj.Bod#spoj spoj} = {@code valfalse};
					bod.{@link Roj.Bod#x0 x0} = -{@code num2};
					bod.{@link Roj.Bod#skupina skupina} = {@code num1};

					osX = roj.{@link Roj#pridajBod() pridajBod}();
					osX.{@link Roj.Bod#farba farba} = osX.{@link Roj.Bod#farbaSpoja farbaSpoja} = {@link Farebnosť#červená červená};
					osX.{@link Roj.Bod#x0 x0} = dĺžka;
					osX.{@link Roj.Bod#skupina skupina} = {@code num1};
					osX.{@link Roj.Bod#kreslenie kreslenie} = šípka;
				}
				{@code kwdelse} osX.{@link Roj.Bod#x0 x0} = dĺžka;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Zobrazenie/skrytie osi x.}
			{@code kwdpublic} {@code typevoid} osX({@code typeboolean} zobraz)
			{
				{@code kwdif} ({@code valnull} == osX)
				{
					{@code kwdif} (zobraz) osX({@code num30});
					{@code kwdelse} {@code kwdreturn};
				}
				osX.{@link Roj.Bod#zobraz zobraz} = osX.{@link Roj.Bod#spoj spoj} = zobraz;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Overenie, či je os x zobrazená.}
			{@code kwdpublic} {@code typeboolean} osX()
			{
				{@code kwdif} ({@code valnull} == osX) {@code kwdreturn} {@code valfalse};
				{@code kwdreturn} osX.{@link Roj.Bod#spoj spoj};
			}

			{@code comm// Definovanie alebo úprava dĺžky osi y.}
			{@code kwdpublic} {@code typevoid} osY({@code typedouble} dĺžka)
			{
				{@code kwdif} ({@code valnull} == osZ)
				{
					{@link Roj.Bod Roj.Bod} bod = roj.{@link Roj#pridajBod() pridajBod}();
					bod.{@link Roj.Bod#zobraz zobraz} = bod.{@link Roj.Bod#spoj spoj} = {@code valfalse};
					bod.{@link Roj.Bod#y0 y0} = -{@code num2};
					bod.{@link Roj.Bod#skupina skupina} = {@code num2};

					osY = roj.{@link Roj#pridajBod() pridajBod}();
					osY.{@link Roj.Bod#farba farba} = osY.{@link Roj.Bod#farbaSpoja farbaSpoja} = {@link Farebnosť#zelená zelená};
					osY.{@link Roj.Bod#y0 y0} = dĺžka;
					osY.{@link Roj.Bod#skupina skupina} = {@code num2};
					osY.{@link Roj.Bod#kreslenie kreslenie} = šípka;
				}
				{@code kwdelse} osY.{@link Roj.Bod#y0 y0} = dĺžka;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Zobrazenie/skrytie osi y.}
			{@code kwdpublic} {@code typevoid} osY({@code typeboolean} zobraz)
			{
				{@code kwdif} ({@code valnull} == osY)
				{
					{@code kwdif} (zobraz) osY({@code num30});
					{@code kwdelse} {@code kwdreturn};
				}
				osY.{@link Roj.Bod#zobraz zobraz} = osY.{@link Roj.Bod#spoj spoj} = zobraz;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Overenie, či je os y zobrazená.}
			{@code kwdpublic} {@code typeboolean} osY()
			{
				{@code kwdif} ({@code valnull} == osY) {@code kwdreturn} {@code valfalse};
				{@code kwdreturn} osY.{@link Roj.Bod#spoj spoj};
			}

			{@code comm// Definovanie alebo úprava dĺžky osi z.}
			{@code kwdpublic} {@code typevoid} osZ({@code typedouble} dĺžka)
			{
				{@code kwdif} ({@code valnull} == osZ)
				{
					{@link Roj.Bod Roj.Bod} bod = roj.{@link Roj#pridajBod() pridajBod}();
					bod.{@link Roj.Bod#zobraz zobraz} = bod.{@link Roj.Bod#spoj spoj} = {@code valfalse};
					bod.{@link Roj.Bod#z0 z0} = -{@code num2};
					bod.{@link Roj.Bod#skupina skupina} = {@code num3};

					osZ = roj.{@link Roj#pridajBod() pridajBod}();
					osZ.{@link Roj.Bod#farba farba} = osZ.{@link Roj.Bod#farbaSpoja farbaSpoja} = {@link Farebnosť#modrá modrá};
					osZ.{@link Roj.Bod#z0 z0} = dĺžka;
					osZ.{@link Roj.Bod#skupina skupina} = {@code num3};
					osZ.{@link Roj.Bod#kreslenie kreslenie} = šípka;
				}
				{@code kwdelse} osZ.{@link Roj.Bod#z0 z0} = dĺžka;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Zobrazenie/skrytie osi z.}
			{@code kwdpublic} {@code typevoid} osZ({@code typeboolean} zobraz)
			{
				{@code kwdif} ({@code valnull} == osZ)
				{
					{@code kwdif} (zobraz) osZ({@code num10});
					{@code kwdelse} {@code kwdreturn};
				}
				osZ.{@link Roj.Bod#zobraz zobraz} = osZ.{@link Roj.Bod#spoj spoj} = zobraz;
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Overenie, či je os z zobrazená.}
			{@code kwdpublic} {@code typeboolean} osZ()
			{
				{@code kwdif} ({@code valnull} == osZ) {@code kwdreturn} {@code valfalse};
				{@code kwdreturn} osZ.{@link Roj.Bod#spoj spoj};
			}

			{@code comm// Definovanie alebo úprava dĺžky všetkých troch osí naraz.}
			{@code kwdpublic} {@code typevoid} osi({@code typedouble} dĺžkaX, {@code typedouble} dĺžkaY, {@code typedouble} dĺžkaZ)
			{
				osX(dĺžkaX);
				osY(dĺžkaY);
				osZ(dĺžkaZ);
			}

			{@code comm// Zobrazenie/skrytie troch osí naraz.}
			{@code kwdpublic} {@code typevoid} osi({@code typeboolean} zobraz)
			{
				osX(zobraz);
				osY(zobraz);
				osZ(zobraz);
			}

			{@code comm// Zobrazenie (definovanie) všetkých troch osí s predvolenými}
			{@code comm// hodnotami dĺžok.}
			{@code kwdpublic} {@code typevoid} osi() { osi({@code num30}, {@code num30}, {@code num10}); }
		{@code comm// &#125;&#125;&#125;}


		{@code comm// Hromadné úpravy roja a jeho bodov. (Reset a hromadné transformácie}
		{@code comm// používajúce vnútorné atribúty bodov, ktoré sú rezervované na tieto}
		{@code comm// účely. Niektoré z týchto metód sú určené len pre pomocné režimy}
		{@code comm// ladenia 5 a 6 tejto ukážky.)}
		{@code comm// &#123;&#123;&#123;}
			{@code comm// Táto metóda slúži na rýchle nastavenie predvolených vlastností roja.}
			{@code comm// (Táto metóda je pravdepodobne jediná, ktorej obsahom sa treba}
			{@code comm// zaoberať pri redukcii príkladu – t. j. pri odstraňovaní kreslenia}
			{@code comm// osí a iných ladiacich informácií.)}
			{@code kwdpublic} {@code typevoid} resetujRoj()
			{
				{@code comm// Toto sú síce predvolené vlastnosti roja, ale pri každom resete}
				{@code comm// ich potrebujeme vrátiť späť:}
				roj.{@link Roj#nastavUhly(double, double, double) nastavUhly}(-{@code num110}, -{@code num360}, {@code num45});
				roj.{@link Roj#nastavStredOtáčania(double, double, double) nastavStredOtáčania}({@code num0}, {@code num0}, {@code num0});

				{@code comm// Nasledujúce dve vlastnosti upravujeme z predvolených hodnôt}
				{@code comm// (ktoré by boli: mierka = 1000; kamera = [0, -125, 200]) na také,}
				{@code comm// aké potrebujeme v našej ukážke:}
				roj.{@link Roj#mierka(double) mierka}({@code num500});
				roj.{@link Roj#nastavKameru(double, double, double) nastavKameru}({@code num0}, {@code num50}, {@code num30});

				{@code comm// Nastavenie príznaku potrebnosti prekreslenia roja:}
				prekresliRoj = {@code valtrue};

				{@code comm// Predvolená hodnota želaného uhla:}
				želanýUhol = {@code num270.0};
			}

			{@code comm// Reset vnútorných individuálnych vlastností posunutia (dx až dz)}
			{@code comm// a pootočenia (alfa až gama) jednotlivých bodov roja.}
			{@code kwdpublic} {@code typevoid} resetujBody()
			{
				{@code kwdfor} ({@link Roj.Bod Roj.Bod} bod : roj.{@link Roj#body() body}())
					{@code kwdif} ({@code num1} &gt; bod.{@link Roj.Bod#skupina skupina} || {@code num3} &lt; bod.{@link Roj.Bod#skupina skupina})
					{
						bod.{@link Roj.Bod#dx dx} = bod.{@link Roj.Bod#dy dy} = bod.{@link Roj.Bod#dz dz} =
							{@code comm// bod.xs = bod.ys = bod.zs =}
							bod.{@link Roj.Bod#alfa alfa} = bod.{@link Roj.Bod#beta beta} = bod.{@link Roj.Bod#gama gama} = {@code num0.0};
						bod.{@link Roj.Bod#transformuj transformuj} = {@code valtrue};
					}

				roj.{@link Roj#transformovať() transformovať}();
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Posunutie s pomocou vnútorných vlastností dx až dz bodov roja.}
			{@code kwdpublic} {@code typevoid} posuňBody({@code typedouble} Δx, {@code typedouble} Δy, {@code typedouble} Δz)
			{
				{@code kwdfor} ({@link Roj.Bod Roj.Bod} bod : roj.{@link Roj#body() body}())
					{@code kwdif} ({@code num1} &gt; bod.{@link Roj.Bod#skupina skupina} || {@code num3} &lt; bod.{@link Roj.Bod#skupina skupina})
					{
						bod.{@link Roj.Bod#dx dx} += Δx;
						bod.{@link Roj.Bod#dy dy} += Δy;
						bod.{@link Roj.Bod#dz dz} += Δz;
						bod.{@link Roj.Bod#transformuj transformuj} = {@code valtrue};
					}

				roj.{@link Roj#transformovať() transformovať}();
				prekresliRoj = {@code valtrue};
			}

			{@code comm// Pootočenie s pomocou vnútorných vlastností alfa až gama bodov roja.}
			{@code kwdpublic} {@code typevoid} pootočBody({@code typedouble} Δα, {@code typedouble} Δβ, {@code typedouble} Δγ)
			{
				{@code kwdfor} ({@link Roj.Bod Roj.Bod} bod : roj.{@link Roj#body() body}())
					{@code kwdif} ({@code num1} &gt; bod.{@link Roj.Bod#skupina skupina} || {@code num3} &lt; bod.{@link Roj.Bod#skupina skupina})
					{
						bod.{@link Roj.Bod#alfa alfa} += Δα;
						bod.{@link Roj.Bod#beta beta} += Δβ;
						bod.{@link Roj.Bod#gama gama} += Δγ;
						bod.{@link Roj.Bod#transformuj transformuj} = {@code valtrue};
					}

				roj.{@link Roj#transformovať() transformovať}();
				prekresliRoj = {@code valtrue};
			}
		{@code comm// &#125;&#125;&#125;}


		{@code comm// Ovládanie – režim ladenia.}
		{@code comm// &#123;&#123;&#123;}
			{@code comm// Atribúty súvisiace s režimom ladenia.}
			{@code kwdprivate} {@code typeint} režim = {@code num3};
			{@code kwdprivate} {@code kwdstatic} {@code typeboolean} infoORoji = {@code valfalse};
			{@code kwdprivate} {@code typedouble} myšX = {@code num0};
			{@code kwdprivate} {@code typedouble} myšY = {@code num0};

			{@code comm// Výpis ladiacich informácií.}
			{@code kwdpublic} {@code typevoid} infoORoji()
			{
				{@link Svet Svet}.{@link Svet#vymažTexty() vymažTexty}();

				{@code kwdif} ({@code num0} != režim && infoORoji)
				{
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"K[x, y, z]: "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#kameraX() kameraX}(), {@code num2}), {@code srg"; "},
						{@link GRobot#F(double, int) F}(roj.{@link Roj#kameraY() kameraY}(), {@code num2}), {@code srg"; "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#kameraZ() kameraZ}(), {@code num2}));
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"SO[x, y, z]: "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#stredOtáčaniaX() stredOtáčaniaX}(), {@code num2}),
						{@code srg"; "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#stredOtáčaniaY() stredOtáčaniaY}(), {@code num2}), {@code srg"; "},
						{@link GRobot#F(double, int) F}(roj.{@link Roj#stredOtáčaniaZ() stredOtáčaniaZ}(), {@code num2}));
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"α, β, γ: "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#uholAlfa() uholAlfa}(), {@code num2}) + {@code srg"°; "},
						{@link GRobot#F(double, int) F}(roj.{@link Roj#uholBeta() uholBeta}(), {@code num2}) + {@code srg"°; "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#uholGama() uholGama}(), {@code num2}) + {@code srg"°"});
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"M: "}, {@link GRobot#F(double, int) F}(roj.{@link Roj#mierka() mierka}(), {@code num2}));

					{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Režim myši: "});
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} {@code num1}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"kamera"}); {@code kwdbreak};
					{@code kwdcase} {@code num2}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"stred otáčania"}); {@code kwdbreak};
					{@code kwdcase} {@code num3}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"uhly rotácie"}); {@code kwdbreak};
					{@code kwdcase} {@code num4}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"mierka"}); {@code kwdbreak};
					{@code kwdcase} {@code num5}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"posunutie bodov"}); {@code kwdbreak};
					{@code kwdcase} {@code num6}: {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"pootočenie bodov"}); {@code kwdbreak};
					}

					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@link Konštanty#riadok riadok}, {@code srg"Želaný uhol: "}, želanýUhol);
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Uhol pootočenia: "}, Δu);
					{@code typeint} položka = čísloPoložky();
					{@code kwdif} (položka &lt; {@code num0} || položka &gt;= mená.length)
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Neznáme číslo položky: "}, položka);
					{@code kwdelse}
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Zvolená položka "}, položka,
							{@code srg": "}, mená[položka]);
				}
			}

			{@code comm// Rozšírenie ovládania klávesnicou pre režim ladenia.}
			{@code kwdpublic} {@code typevoid} priUvoľneníKlávesu()
			{
				{@code kwdswitch} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves() kláves}())
				{
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_0 VK_0}: režim = {@code num0}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_1 VK_1}: režim = {@code num1}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_2 VK_2}: režim = {@code num2}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_3 VK_3}: režim = {@code num3}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_4 VK_4}: režim = {@code num4}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_5 VK_5}: režim = {@code num5}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_6 VK_6}: režim = {@code num6}; {@code kwdbreak};

				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_I VK_I}: infoORoji = !infoORoji; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_O VK_O}: Ikonka.info = !Ikonka.info;
					prekresliRoj = {@code valtrue}; {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_S VK_S}: osi(!osX()); {@code kwdbreak};

				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#MEDZERA MEDZERA}: resetujRoj(); resetujBody(); {@code kwdbreak};
				}

				infoORoji();
			}

			{@code comm// Ovládanie myšou v režime ladenia – akcia vykonaná pri stlačení}
			{@code comm// ľubovoľného tlačidla myši.}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
			{
				{@code comm// if (tlačidloMyši(ĽAVÉ)) {} else {}}
				myšX = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}();
				myšY = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}();
			}

			{@code comm// Ovládanie myšou v režime ladenia – akcia vykonaná pri ťahaní}
			{@code comm// myšou (t. j. pohybe myšou počas držania ľubovoľného tlačidla).}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
			{
				{@code comm// (Rozlišuje sa akcia stlačenia ľavého a „iného“ tlačidla…)}
				{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ}))
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} {@code num1}:
						roj.{@link Roj#posuňKameru(double, double, double) posuňKameru}(
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX,
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY, {@code num0.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num2}:
						roj.{@link Roj#posuňStredOtáčania(double, double, double) posuňStredOtáčania}(
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX,
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY, {@code num0.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num3}:
						{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.MouseEvent#isControlDown() isControlDown}())
						{
							{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.MouseEvent#isShiftDown() isShiftDown}())
								roj.{@link Roj#pootoč(double, double, double) pootoč}({@code num0.0}, (
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX +
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0},
									{@code num0.0});
							{@code kwdelse}
								roj.{@link Roj#pootoč(double, double, double) pootoč}((
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX +
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0},
									{@code num0.0}, {@code num0.0});
						}
						{@code kwdelse}
							roj.{@link Roj#pootoč(double, double, double) pootoč}(
								({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0},
								({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX) / {@code num10.0}, {@code num0.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num4}:
						roj.{@link Roj#zmeňMierku(double) zmeňMierku}(
							(myšX &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}()) / {@code num10.0} +
							(myšY &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}()) / {@code num1.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num5}:
						posuňBody(
							({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX) / {@code num10.0},
							(myšY &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}()) / {@code num10.0}, {@code num0});
						{@code kwdbreak};

					{@code kwdcase} {@code num6}:
						pootočBody(
							({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0},
							({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX) / {@code num10.0}, {@code num0});
						{@code kwdbreak};
					}
				}
				{@code kwdelse}
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} {@code num1}:
						roj.{@link Roj#posuňKameru(double, double, double) posuňKameru}({@code num0.0}, {@code num0.0},
							((myšX &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}()) / {@code num100.0}) +
							((myšY &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}()) / {@code num10.0}));
						{@code kwdbreak};

					{@code kwdcase} {@code num2}:
						roj.{@link Roj#posuňStredOtáčania(double, double, double) posuňStredOtáčania}({@code num0.0}, {@code num0.0},
							myšY &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}());
						{@code kwdbreak};

					{@code kwdcase} {@code num3}:
						roj.pootoč({@code num0.0}, {@code num0.0}, (
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX +
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num4}:
						roj.zmeňMierku(
							(myšX &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}()) / {@code num1000.0} +
							(myšY &#45; {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}()) / {@code num100.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num5}:
						posuňBody({@code num0}, {@code num0},
							({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX) / {@code num100.0} +
							({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num6}:
						pootočBody({@code num0}, {@code num0},(
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &#45; myšX +
							{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}() &#45; myšY) / {@code num10.0});
						{@code kwdbreak};
					}
				}

				myšX = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}();
				myšY = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}();

				prekresliRoj = {@code valtrue};
				infoORoji();
				{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			}

			{@code comm// Kreslenie vlastného tvaru robota (ktoré by bolo predvoleným tvarom}
			{@code comm// tých bodov roja, ktoré by nemali definovaný žiadny vlastný tvar).}
			{@code comm// V tomto príklade je to zariadené tak, že volanie tejto metódy by}
			{@code comm// nemalo nikdy nastať. Je definovaná v podstate len „pre istotu.“}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#kresliTvar() kresliTvar}()
			{
				{@link GRobot#krúžok() krúžok}();
			}


			{@code comm// V tomto komentári sa nachádza krátka sekcia vytvorená na ladiace}
			{@code comm// účely, ktorá pri pohybe kurzorom myši (výhradne pri pohybe kurzorom}
			{@code comm// myši) overí prítomnosť kurzora nad niektorým bodom roja a v prípade}
			{@code comm// nájdenia takéhoto bodu nad ním nachvíľu zobrazí červenú kružnicu}
			{@code comm// (realizovanú prostredníctvom jednoúčelového robota uloženého}
			{@code comm// v inštancii zvýrazniť).}
			{@code comm// }
			{@code comm// private GRobot zvýrazniť = null;}
			{@code comm// @Override public void pohybMyši()}
			{@code comm// &#123;}
			{@code comm//     Roj.Bod bod = null == roj ? null : roj.dajBodNaMyši();}
			{@code comm//     if (null == bod)}
			{@code comm//     &#123;}
			{@code comm//         if (null != zvýrazniť) zvýrazniť.skry();}
			{@code comm//     &#125;}
			{@code comm//     else}
			{@code comm//     &#123;}
			{@code comm//         if (null == zvýrazniť)}
			{@code comm//         &#123;}
			{@code comm//             zvýrazniť = new GRobot()}
			{@code comm//             &#123;}
			{@code comm//                 @Override public void deaktivácia() &#123; skry(); &#125;}
			{@code comm//                 @Override public void aktivácia() &#123; zobraz(); &#125;}
			{@code comm//             &#125;;}
			{@code comm// }
			{@code comm//             zvýrazniť.vlastnýTvar(r -> r.krúžok());}
			{@code comm//             zvýrazniť.farba(červená);}
			{@code comm//             zvýrazniť.hrúbkaČiary(3);}
			{@code comm//             zvýrazniť.vrstva(1);}
			{@code comm//         &#125;}
			{@code comm// }
			{@code comm//         zvýrazniť.skočNa(bod.x3, bod.y3);}
			{@code comm//         zvýrazniť.veľkosť(bod.z3);}
			{@code comm//         zvýrazniť.aktivuj(10);}
			{@code comm//     &#125;}
			{@code comm// &#125;}

		{@code comm// &#125;&#125;&#125;}


		{@code comm// Pomocná metóda zisťujúca, ktorá ikonka je v popredí (podľa želaného}
		{@code comm// uhla pootočenia roja podľa osi z, ktorý je uložený v prislúchajúcom}
		{@code comm// atribúte tejto triedy).}
		{@code kwdprivate} {@code typeint} čísloPoložky()
		{
			{@code typedouble} uhol = (želanýUhol + {@code num90} + Δu / {@code num2}) % {@code num360.0};
			{@code kwdif} (uhol &lt; {@code num0}) uhol += {@code num360.0};
			{@code kwdreturn} ({@code typeint})(uhol / Δu);
		}

		{@code comm// Pomocná metóda určená na rozšírenie a na vykonanie prislúchajúcej}
		{@code comm// akcie podľa „aktuálneho“ čísla položky (zisteného metódou vyššie).}
		{@code kwdprivate} {@code typevoid} potvrdeniePoložky()
		{
			{@code typeint} položka = čísloPoložky();
			{@code kwdif} (položka &lt; {@code num0} || položka &gt;= mená.length)
				{@link Svet Svet}.{@link Svet#chyba(String) chyba}({@code srg"Neznáme číslo položky: "} + položka);
			{@code kwdelse}
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka "} + položka + {@code srg": "} + mená[položka]);
		}

		{@code comm// Ovládanie kolotoča klávesnicou.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieKlávesu() uvoľnenieKlávesu}()
		{
			{@code kwdswitch} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves() kláves}())
			{
			{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}: želanýUhol += Δu; {@code kwdbreak};
			{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:  želanýUhol -= Δu; {@code kwdbreak};
			{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#ENTER ENTER}:  potvrdeniePoložky(); {@code kwdbreak};
			{@code kwddefault}: priUvoľneníKlávesu();
			}
		}

		{@code comm// Alternatívny spôsob aktivovania položky – klikom myši.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			{@code comm// Prevzatie bodu na myši.}
			{@link Roj.Bod Roj.Bod} bod = {@code valnull} == roj ? {@code valnull} : roj.{@link Roj#dajBodNaMyši() dajBodNaMyši}();

			{@code kwdif} ({@code valnull} != bod && {@code valnull} != bod.{@link Roj.Bod#kreslenie kreslenie} &&
				bod.{@link Roj.Bod#kreslenie kreslenie} {@code kwdinstanceof} Ikonka)
			{
				{@code comm// Získanie „mena“ bodu (resp. názvu SVG súboru).}
				{@link String String} meno = ((Ikonka)bod.{@link Roj.Bod#kreslenie kreslenie}).meno;

				{@code comm// Zistenie indexu položky kolotoča podľa jej „mena.“}
				{@code typeint} indexOf = -{@code num1};
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; mená.length; ++i)
					{@code kwdif} (mená[i].{@link String#equals(Object) equals}(meno))
					{
						indexOf = i;
						{@code kwdbreak};
					}

				{@code kwdif} (-{@code num1} != indexOf)
				{
					{@code comm// „Aktivácia“ položky rolovaním na ňu.}
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}() &gt;= {@code num0})
						{@code kwdfor} ({@code typeint} i = {@code num0}; čísloPoložky() != indexOf &&
							i &lt; mená.length; ++i) želanýUhol += Δu;
					{@code kwdelse}
						{@code kwdfor} ({@code typeint} i = {@code num0}; čísloPoložky() != indexOf &&
							i &lt; mená.length; ++i) želanýUhol -= Δu;

					{@code comm// Potvrdenie položky (rovnakým spôsobom ako pri klávesnici).}
					{@code kwdif} (čísloPoložky() == indexOf) potvrdeniePoložky();
				}
			}
		}

		{@code comm// Animácia kolotoča v časovači.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@code typedouble} Δγ = želanýUhol &#45; roj.{@link Roj#uholGama() uholGama}();
			{@code kwdif} ({@link Math Math}.{@link Math#abs(double) abs}(Δγ) &gt;= {@code num1.0})
			{
				roj.{@link Roj#pootoč(double, double, double) pootoč}({@code num0.0}, {@code num0.0}, Δγ / {@code num10.0});
				prekresliRoj = {@code valtrue};
				infoORoji();
			}

			{@code kwdif} (prekresliRoj)
			{
				prekresliRoj = {@code valfalse};
				{@link Svet Svet}.{@link Svet#vymažGrafiku() vymažGrafiku}();
				roj.{@link Roj#kresli() kresli}();
			}

			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
		}


		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"Kolotoč.cfg"});
			{@code kwdnew} Kolotoč();
		}
	}
	</pre>
 * 
 * <p style="text-align: center;">Balíček SVG ikoniek na prevzatie: <a
 * href="resources/kolotoc-ikonky.7z" target="_blank">kolotoc-ikonky.7z</a></p>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>kolotoc.gif<alt/>Ukážka fungovania kolotoča.</image>Ukážka
 * fungovania kolotoča so zapnutými ladiacimi informáciami a pootočením
 * roviny otáčania.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Informačné zdroje, ktoré môžu pomôcť pri riešení matematických
 * problémov súvisiacich s touto kapitolou (triedou):</b></p>
 * 
 * <p class="remark"><b>Poznámka:</b> Zdroje môžu obsahovať chyby (v čase
 * ich citovania ich obsahovali), preto je vhodné informácie z nich
 * konfrontovať s inou (napríklad tlačenou) literatúrou. Autor pri tvorbe
 * tejto triedy použil uvedené zdroje najmä na pripomenutie si informácií
 * o 3D grafike a súvisiacich operáciách s maticami (ktoré naposledy
 * používal asi 12 až 15 rokov pred začatím písania tejto triedy).</p>
 * 
 * <ul>
 * <li><small>Liekens, Anthony</small>: <em>Computers » Rendering
 * Tutorial.</em> anthony.liekens.net, 2000–2013. Dostupné na:
 * <a href="http://anthony.liekens.net/index.php/Computers/RenderingTutorial"
 * target="_blank"
 * >http://anthony.liekens.net/index.php/Computers/RenderingTutorial</a>.
 * Citované: 15. 10. 2017.</li>
 * <li><em>Matrix multiplication – Wikipedia.</em> Dostupné na:
 * <a href="https://en.wikipedia.org/wiki/Matrix_multiplication"
 * target="_blank">https://en.wikipedia.org/wiki/Matrix_multiplication</a>.
 * Citované: 15. 10. 2017.</li>
 * <li><em>Rotation matrix – Wikipedia.</em> Dostupné na:
 * <a href="https://en.wikipedia.org/wiki/Rotation_matrix"
 * target="_blank">https://en.wikipedia.org/wiki/Rotation_matrix</a>.
 * Citované: 15. 10. 2017.</li>
 * </ul>
 */
public class Roj
{
	// <!-- Varovanie‼ JavaDoc nechce vygenerovať záznamy o Bode a Smerníku,
	// Preto treba všetky zmeny v prvej vete preniesť aj do RoboDoc.java. -->

	/**
	 * <p>Trieda uchovávajúca údaje o jednom bode roja. Kreslenie, aktivitu
	 * a potrebné prepočty zabezpečuje programátor s pomocou vstavaných metód
	 * alebo na základe údajov o bode.</p>
	 */
	public class Bod implements Comparable<Bod>
	{
		/**
		 * <p>Porovná polohu tohto bodu s polohou zadaného bodu a vráti
		 * hodnotu {@code num0.0}, ak sú vzdialenosti oboch bodov od kamery
		 * zhodné, kladnú hodnotu, ak je vzdialenosť druhého bodu od kamery
		 * väčšia a zápornú hodnotu v opačnom prípade.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Rozhranie {@link Comparable}
		 * bolo implementované z dôvodu zaručenia správneho poradia kreslenia
		 * bodov roja.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia, ktorý demonštruje vyššie
		 * uvedené informácie. -->
		 * 
		 * @param iný bod, s ktorým má byť tento bod porovnaný
		 * @return celé číslo vyjadrujúce rozdiel vzdialeností porovnávaných
		 *     bodov od kamery
		 */
		public int compareTo(Bod iný)
		{
			//// Upravovať súradnice x a y bola chyba…
			// double Δx = kx + this.x2;
			// double Δy = ky + this.y2;
			double Δz = kz + this.z2;
			// double d1 = (Δx * Δx + Δy * Δy + Δz * Δz);
			double d1 = (this.x2 * this.x2 + this.y2 * this.y2 + Δz * Δz);

			// Δx = kx + iný.x2;
			// Δy = ky + iný.y2;
			Δz = kz + iný.z2;
			// double d2 = (Δx * Δx + Δy * Δy + Δz * Δz);
			double d2 = (iný.x2 * iný.x2 + iný.y2 * iný.y2 + Δz * Δz);

			return (int)(d2 - d1);

			//// Iná chyba bola skúsiť to riešiť takto:
			// if (this.z2 <= 0 && iný.z2 <= 0) return 0;
			// if (this.z2 <= 0) return 1;
			// if (iný.z2 <= 0) return -1;
			// return (int)(1000 / this.z2) - (int)(1000 / iný.z2);
		}

		/**
		 * <p>Originálna súradnica polohy bodu. Atribúty {@code x0},
		 * {@code y0} a {@code z0} určujú pôvodnú netransformovanú polohu
		 * bodu v priestore.</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Po zmene hodnoty tohto
		 * atribútu je nevyhnutné nastaviť príznak {@link #transformuj
		 * transformuj} na {@code valtrue}.</p>
		 */
		public double x0 = 0.0, y0 = 0.0, z0 = 0.0;

		/**
		 * <p>Posunutie originálnej súradnice polohy bodu. Atribúty
		 * {@code dx}, {@code dy} a {@code dz} určujú voliteľné posunutie
		 * (transfromovanie posunutím) originálnej polohy bodu (pozri
		 * {@link #x0 x0}, {@link #y0 y0} alebo {@link #z0 z0}) v priestore.
		 * Takto sa dá bod ľubovoľne posúvať v priestore bez toho, aby sme
		 * stratili originálne hodnoty súradníc jeho polohy.</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Po zmene hodnoty tohto
		 * atribútu je nevyhnutné nastaviť príznak {@link #transformuj
		 * transformuj} na {@code valtrue}.</p>
		 */
		public double dx = 0.0, dy = 0.0, dz = 0.0;

		/**
		 * <p>Súradnica stredu otáčania bodu. Atribúty {@code xs}, {@code ys},
		 * {@code zs} určujú polohu stredu otáčania bodu v priestore.
		 * (Pozri aj {@link #alfa alfa}, {@link #beta beta} alebo
		 * {@link #gama gama}.)</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Po zmene hodnoty tohto
		 * atribútu je nevyhnutné nastaviť príznak {@link #transformuj
		 * transformuj} na {@code valtrue}.</p>
		 */
		public double xs = 0.0, ys = 0.0, zs = 0.0;

		/**
		 * <p>Uhly pootočenia bodu okolo stredu otáčania. Atribúty
		 * {@code alfa}, {@code beta} a {@code gama} určujú uhly pootočenia
		 * (transformovania otáčaním) bodu v priestore okolo stredu otáčania
		 * [{@link #xs xs}, {@link #ys ys}, {@link #zs zs}].</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Po zmene hodnoty tohto
		 * atribútu je nevyhnutné nastaviť príznak {@link #transformuj
		 * transformuj} na {@code valtrue}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public double alfa = 0.0, beta = 0.0, gama = 0.0;

		/**
		 * <p>Parameter rozmeru objektu kresleného na polohe bodu. (Pozri aj
		 * {@link #faktor faktor}.)</p>
		 */
		public double rozmer = 1.0;

		/**
		 * <p>Tento príznak určuje, že súradnice {@link #x1 x1},
		 * {@link #y1 y1} a {@link #z1 z1} musia byť prepočítané.</p>
		 */
		public boolean transformuj = true;

		/**
		 * <p>Prepočítaná (lokálne transformovaná) súradnica polohy bodu.
		 * Atribúty {@code x1}, {@code y1} a {@code z1} určujú lokálne
		 * transformovanú polohu bodu v priestore. Sú vypočítané z atribútov
		 * originálnych súradníc bodu [{@link #x0 x0}, {@link #y0 y0},
		 * {@link #z0 z0}], hodnôt posunutia bodu v priestore
		 * [{@link #dx dx}, {@link #dy dy}, {@link #dz dz}] (transformácia
		 * posunutím) a hodnôt pootočenia bodu v priestore (uhly {@link #alfa
		 * alfa}, {@link #beta beta} a {@link #gama gama}) okolo stredu
		 * otáčania [{@link #xs xs}, {@link #ys ys}, {@link #zs zs}]
		 * (transformovania otáčaním).</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Z uvedeného vyplýva, že ak
		 * sa hodnota ktoréhokoľvek z vyššie spomenutých atribútov zmení, tak
		 * súradnice {@code x1}, {@code y1} a {@code z1} musia byť opätovne
		 * prepočítané. To znamená, že súčasne so zmenou hociktorého
		 * z atribútov spomenutých v opise vyššie je nevyhnutné nastaviť
		 * príznak {@link #transformuj transformuj} na {@code valtrue}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public double x1 = 0.0, y1 = 0.0, z1 = 0.0;

		/**
		 * <p>Tento atribút určuje, či bude objekt na polohe tohto bodu
		 * zobrazený. V každom bode roja môže alebo nemusí byť kreslený
		 * nejaký objekt – 2D objekt (prípadne určitým vlastným spôsobom
		 * generovaný 3D objekt – autor takéhoto kódu by musel doriešiť
		 * zistenie orientácie objektu). Tento atribút určuje, či má byť
		 * spustené jeho kreslenie.</p>
		 */
		public boolean zobraz = true;

		/**
		 * <p>Tento atribút určuje zmenu farby robota vykonanú pred kreslením
		 * objektu na polohe tohto bodu.</p>
		 */
		public Farba farba = null;

		/**
		 * <p>Tento atribút určuje zmenu hrúbky čiary robota vykonanú pred
		 * kreslením objektu na polohe tohto bodu.</p>
		 */
		public double dho = 0.0;

		/**
		 * <p>Konečná (globálne transformovaná) súradnica polohy bodu.
		 * Atribúty {@code x2}, {@code y2} a {@code z2} určujú konečnú
		 * polohu bodu v priestore, ktorá je vypočítaná z hodnôt
		 * {@link #x1 x1}, {@link #y1 y1} a {@link #z1 z1} a z atribútov
		 * transformácie roja (pootočenia a polohy kamery, mierky…; preto
		 * ich môžeme označiť aj ako globálne transformované).</p>
		 */
		public double x2 = 0.0, y2 = 0.0, z2 = 0.0;

		/**
		 * <p>Tento atribút určuje, či bude medzi polohou predchádzajúceho
		 * a tohto bodu roja kreslená spojovacia čiara. (Pri kreslení roja
		 * ako celku je táto hodnota pre prvý bod roja irelevantná.)</p>
		 * 
		 * @see #farbaSpoja
		 * @see #dh
		 */
		public boolean spoj = true;

		/**
		 * <p>Tento atribút určuje zmenu farby robota vykonanú pred
		 * kreslením spoja smerujúceho do tohto bodu. (Pri kreslení roja
		 * ako celku je táto hodnota pre prvý bod roja irelevantná.)</p>
		 * 
		 * @see #spoj
		 * @see #dh
		 */
		public Farba farbaSpoja = null;

		/**
		 * <p>Tento atribút určuje zmenu hrúbky čiary robota vykonanú pred
		 * kreslením spoja smerujúceho do tohto bodu. (Pri kreslení roja
		 * ako celku je táto hodnota pre prvý bod roja irelevantná.)</p>
		 * 
		 * @see #spoj
		 * @see #farbaSpoja
		 */
		public double dh = 0.0;

		/**
		 * <p>Atribút cieľovej (zobrazovanej) polohy a veľkosti objektu na
		 * bode. Atribúty {@code x3} a {@code y3} určujú zobrazovanú
		 * (premietanú) polohu objektu (kresleného na polohe tohto bodu) na
		 * plátno sveta. Atribút {@code z3} určuje prepočítaný rozmer objektu.
		 * Hodnoty týchto atribútov sú vypočítané len v prípade, že je faktor
		 * rozmeru a zobrazovanej polohy (atribút {@link #faktor faktor})
		 * kladný. Počítajú sa z hodnôt súradníc konečnej (globálne
		 * transformovanej) polohy bodu [{@link #x2 x2}, {@link #y2 y2},
		 * {@link #z2 z2}], rozmeru objektu kresleného na bode –
		 * {@link #rozmer rozmer}, faktora (deliteľa) rozmeru a zobrazovanej
		 * polohy objektu na bode – {@link #faktor faktor} a z atribútov
		 * pohľadu na roj.</p>
		 */
		public double x3 = 0.0, y3 = 0.0, z3 = 0.0;

		/**
		 * <p>Faktor (deliteľ) rozmeru a zobrazovanej polohy objektu na bode,
		 * ktorý je (automaticky) prepočítavaný podľa konečných (globálne
		 * transformovaných) súradníc bodu ([{@link #x2 x2}, {@link #y2 y2},
		 * {@link #z2 z2}]) a aktuálnej vzdialenosti kamery od roja.</p>
		 * 
		 * <p>Ak je faktor menší alebo rovný nule, tak je objekt
		 * nepozorovateľný – nachádza sa za pozorovateľom alebo na jeho
		 * úrovni. Ak je faktor kladný, stáva sa deliteľom zobrazovanej
		 * polohy a rozmeru objektu, ktoré sú uložené v atribútoch {@link #x3
		 * x3}, {@link #y3 y3} a {@link #z3 z3}.</p>
		 */
		public double faktor = 0.0;

		/**
		 * <p>Tento atribút umožňuje rozlíšiť body patriace do zákaznícky
		 * definovateľných skupín. Atribút je dostupný na voľné použitie.
		 * Nemá žiadne vnútorné využitie v rámci tried roja.</p>
		 */
		public int skupina = 0;

		/**
		 * <p>Ľubovoľná inštancia s dodatočnými informáciami súvisiacimi
		 * s týmto bodom. Do tohto atribútu sa dá uložiť ľubovoľný
		 * zákaznícky objekt, ktorý môže niesť ďalšie informácie súvisiace
		 * s týmto bodom.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia. -->
		 */
		public Object objekt = null;

		/**
		 * <p>Mapa umožňujúca spravovanie zákazníckeho zoznamu celočíselných
		 * parametrov bodu. Význam parametrov určuje programátor podľa
		 * svojich potrieb.</p>
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #reálneParametre
		 */
		public final TreeMap<Integer, Integer> celočíselnéParametre =
			new TreeMap<>();

		/** <p><a class="alias"></a> Alias pre {@link #celočíselnéParametre celočíselnéParametre}.</p> */
		public final TreeMap<Integer, Integer> celociselneParametre =
			celočíselnéParametre;

		/**
		 * <p>Mapa umožňujúca spravovanie zákazníckeho zoznamu reálnych
		 * parametrov bodu. Význam parametrov určuje programátor podľa
		 * svojich potrieb.</p>
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 */
		public final TreeMap<Integer, Double> reálneParametre =
			new TreeMap<>();

		/** <p><a class="alias"></a> Alias pre {@link #reálneParametre reálneParametre}.</p> */
		public final TreeMap<Integer, Double> realneParametre =
			reálneParametre;

		/**
		 * <p>Toto je atribút, do ktorého sa priebežne (automaticky) ukladá
		 * posledná orientácia grafického robota počas kreslenia tohto bodu.
		 * Dá sa to využiť na rôzne účely, napríklad nakreslenie šípky ako
		 * objektu v tomto bode podľa poslednej orientácie v čase kreslenia
		 * spoja, pričom samotný spoj môže byť skrytý.</p>
		 */
		public double smer = 90.0;

		/**
		 * <p>Toto kreslenie umožňuje upraviť tvar objektu nakresleného na
		 * súradniciach tohto bodu roja.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Vo vlastnom kreslení sa na
		 * prístup k aktuálne kreslenému bodu dá s výhodou využiť atribút
		 * {@link Roj#bod bod} roja.</p>
		 * 
		 * @see #kresliTeleso()
		 * @see Roj#kresli()
		 */
		public KreslenieTvaru kreslenie = null;

		/**
		 * <p>Toto kreslenie umožňuje definovať komplexnejší spôsob exportu
		 * objektu na súradiciach tohto bodu roja do formátu SVG. Toto
		 * kreslenie má prioritu pred atribútom {@link #svgTvar svgTvar},
		 * ktorý poskytuje veľmi primitívnu možnosť exportu tvarov roja
		 * (resp. priemetov jeho bodov a objektov na nich) do formátu SVG.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Aj keď je tu použité rozhranie
		 * {@link KreslenieTvaru KreslenieTvaru}, náplňou jeho činnosti nemá
		 * byť v tomto prípade kreslenie zadaným robotom, ale export tvarov
		 * do inštancie triedy {@link SVGPodpora SVGPodpora} (buď do
		 * {@linkplain GRobot#svgPodpora predvolenej,} alebo do inak určenej)
		 * s pomocou zadaného robota a s prípadným využitím inštancie
		 * {@link Roj#bod bod} roja.</p>
		 * 
		 * <!-- TODO príklad použitia, inak bude nepochopiteľné, ako to bolo
		 * vôbec myslené… -->
		 * 
		 * @see #telesoDoSVG(SVGPodpora)
		 * @see Roj#pridajDoSVG(SVGPodpora)
		 */
		public KreslenieTvaru svgKreslenie = null;

		/**
		 * <p>Tento atribút umožňuje definovať jednoduchý tvar, ktorý
		 * bude exportovaný ako objekt umiestnený na súradniciach tohto
		 * bodu roja do formátu SVG. Prioritu pred týmto atribútom má
		 * kreslenie, ktoré môže byť uložené v atribúte {@link 
		 * #svgKreslenie svgKreslenie}.</p>
		 */
		public Shape svgTvar = null;
		// ✓ Treba domyslieť to, čo presne sa bude ukladať do tejto inštancie
			// a ako sa s tým bude manipulovať. To jest, či to má byť tvar vo
			// finálnej podobe (transformovaný), alebo má byť ovplyvňovaný
			// mierkou robota. Ak bude treba, tak treba implementovať
			// prislúchajúcu podporu k tomuto mechanizmu.
			// <!-- TODO ešte treba otestovať -->

		/**
		 * <p>Ak je hodnota atribútu {@link #transformuj transformuj} rovná
		 * {@code valtrue}, tak táto metóda prepočíta (vypočíta nové) hodnoty
		 * transformovaných súradníc {@link #x1 x1}, {@link #y1 y1}
		 * a {@link #z1 z1}. Potom automaticky nastaví atribútu {@link 
		 * #transformuj transformuj} hodnotu {@code valfalse}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public void transformuj()
		{
			if (transformuj)
			{
				double αr = toRadians(alfa);
				double βr = toRadians(beta);
				double γr = toRadians(gama);

				double sinα = sin(αr);
				double sinβ = sin(βr);
				double sinγ = sin(γr);

				double cosα = cos(αr);
				double cosβ = cos(βr);
				double cosγ = cos(γr);

				double T00 = cosβ * cosγ;
				double T01 = -cosβ * sinγ;
				double T02 = sinβ;

				double T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
				double T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
				double T12 = -sinα * cosβ;

				double T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
				double T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
				double T22 = cosα * cosβ;

				double xx = x0 + dx - xs;
				double yy = y0 + dy - ys;
				double zz = z0 + dz - zs;

				x1 = xs + (xx * T00) + (yy * T01) + (zz * T02);
				y1 = ys + (xx * T10) + (yy * T11) + (zz * T12);
				z1 = zs + (xx * T20) + (yy * T21) + (zz * T22);

				transformuj = false;
			}
		}

		/**
		 * <p>Táto metóda fyzicky presunie bod na jeho transformovanú polohu
		 * a zruší transformácie (inak dočasného) posunutia a pootočenia
		 * bodu. Inak povedané – lokálne transformované súradnice sa stanú
		 * originálnymi súradnicami a všetky relevantné atribúty lokálnych
		 * transformácií budú zrušené:</p>
		 * 
		 * <p>To znamená, že transformované súradnice [{@link #x1 x1},
		 * {@link #y1 y1}, {@link  #z1 z1}] budú skopírované do originálnych
		 * súradníc [{@link #x0 x0}, {@link #y0 y0}, {@link #z0 z0}]
		 * a hodnoty transformácií posunutia ({@link #dx dx}, {@link #dy dy},
		 * {@link #dz dz}) a pootočenia (uhly {@link #alfa alfa}, {@link #beta
		 * beta} a {@link #gama gama}) bodu v priestore budú vynulované.</p>
		 * 
		 * <p>Hodnoty súradníc stredu otáčania [{@link #xs xs}, {@link #ys ys},
		 * {@link #zs zs}] ponecháva táto metóda v pôvodnom stave. Ak je
		 * príznak {@link #transformuj transformuj} rovný {@code valtrue},
		 * tak je pred upevnením automaticky spustená metóda {@link 
		 * #transformuj() transformuj()}.</p>
		 */
		public void upevni()
		{
			if (transformuj) transformuj();
			x0 = x1; y0 = y1; z0 = z1;
			dx = dy = dz = 0.0;
			alfa = beta = gama = 0.0;
		}

		/**
		 * <p>Táto metóda využije kresliaci robot roja na nakreslenie spoja
		 * (čiary) smerujúceho z aktuálnej polohy robota do tohto bodu.
		 * (<b style="color: red">Ak má robot položené pero!</b>) Predpokladá
		 * sa, že aktuálna poloha robota je polohou predchádzajúceho bodu
		 * roja.</p>
		 * 
		 * <p>Táto metóda využije atribút {@linkplain #farbaSpoja farby
		 * spoja}, ak je nastavený (to jest, ak nemá hodnotu {@code valnull})
		 * a atribút {@linkplain #dh zmeny hrúbky čiary spoja} (tiež, ak je
		 * nenulový).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pred kreslením celého roja sú
		 * zálohované dve vlastnosti kresliaceho robota – {@linkplain 
		 * GRobot#farba(Color) farba} a {@linkplain GRobot#hrúbkaČiary(double)
		 * hrúbka čiary}. Ich hodnoty sú však obnovené až po nakreslení
		 * všetkých prvkov roja (spojov, telies/objektov) – pozri aj
		 * {@linkplain #kresliTeleso() kreslenie telesa.}</p>
		 */
		public void kresliSpoj()
		{
			if (faktor > 0.0)
			{
				if (neplatnáSúradnica)
				{
					neplatnáSúradnica = false;
					smer = kresli.smerNa(x3, y3);
					kresli.skočNa(x3, y3);
				}
				else if (spoj)
				{
					if (null == farbaSpoja)
					{
						if (0.0 == dh)
						{
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dh);
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dh);
						}
					}
					else
					{
						Farba f = kresli.farba();
						kresli.farba(farbaSpoja);

						if (0.0 == dh)
						{
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dh);
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dh);
						}

						kresli.farba(f);
					}
				}
				else
				{
					smer = kresli.smerNa(x3, y3);
					kresli.skočNa(x3, y3);
				}
			}
			else neplatnáSúradnica = true;
		}

		/**
		 * <p>Táto metóda využije kresliaci robot roja na nakreslenie
		 * objektu umiestneného na polohe tohto bodu (ak je viditeľný – pozri
		 * atribút {@link #zobraz zobraz}). Ak je definované vlastné
		 * {@link #kreslenie kreslenie}, tak je touto metódou využité, inak
		 * je tvarom objektu pečiatka robota (čo môže byť ľubovoľný tvar,
		 * pretože robot môže mať definované vlastné kreslenie). Kreslenie
		 * využije atribút {@linkplain #farba farby bodu} (resp. objektu
		 * kresleného na pozícii bodu), ak je nastavený (to jest, ak nemá
		 * hodnotu {@code valnull}) a atribút {@linkplain #dho zmeny hrúbky
		 * čiary objektu} (tiež, ak je nenulový).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pred kreslením celého roja sú
		 * zálohované dve vlastnosti kresliaceho robota – {@linkplain 
		 * GRobot#farba(Color) farba} a {@linkplain GRobot#hrúbkaČiary(double)
		 * hrúbka čiary}. Ich hodnoty sú však obnovené až po nakreslení
		 * všetkých prvkov roja (spojov a objektov), čiže ak toto kreslenie
		 * objektu/telesa tieto vlastnosti počas kreslenia zmení a neobnoví
		 * (čo sa nevzťahuje na vlastnosti {@linkplain #farba vlastnej farby
		 * bodu} a {@linkplain #dho zmeny hrúbky čiary} (kreslenia objektu),
		 * ktorých použitie je manažované automaticky – pred a po kreslení
		 * objektu bodu), tak sa zmeny prenesú do kreslenia ďalších prvkov
		 * roja, čo môže byť nežiaduce, preto odporúčame po skončení
		 * kreslenia telea <i>„všetko</i> (to jest nielen hrúbku čiary
		 * a farbu) <i>vrátiť do pôvodného stavu.“</i></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Vo vlastnom kreslení sa na
		 * prístup k aktuálne kreslenému bodu dá s výhodou využiť atribút
		 * {@link Roj#bod bod} roja.</p>
		 */
		public void kresliTeleso()
		{
			if (faktor > 0.0)
			{
				if (zobraz)
				{
					if (null == farba)
					{
						kresli.smer(smer);
						kresli.skočNa(x3, y3);
						kresli.veľkosť(z3);

						if (0.0 == dho)
						{
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						}
					}
					else
					{
						Farba f = kresli.farba();
						kresli.farba(farba);

						kresli.smer(smer);
						kresli.skočNa(x3, y3);
						kresli.veľkosť(z3);

						if (0.0 == dho)
						{
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						}

						kresli.farba(f);
					}
				}
			}
		}


		/**
		 * <p>Overí, či sa zadané súradnice nachádzajú v kruhu so zadaným
		 * polomerom a stredom na aktuálnej pozícii projekcie bodu.</p>
		 * 
		 * @param súradnicaX x-ová súradnica bodu
		 * @param súradnicaY y-ová súradnica bodu
		 * @param polomer polomer vyšetrovaného kruhu
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public boolean bodV(double súradnicaX, double súradnicaY,
			double polomer)
		{
			double Δx = súradnicaX - x3;
			double Δy = súradnicaY - y3;
			return (Δx * Δx + Δy * Δy) <= polomer * polomer;
		}

		/**
		 * <p>Funguje rovnako ako metóda {@link #bodV(double, double,
		 * double) bodV(x, y, polomer)}, len namiesto súradníc bodu
		 * je použitá poloha zadaného objektu…</p>
		 * 
		 * @param objekt objekt, ktorého poloha je použitá namiesto
		 *     súradníc bodu
		 * @param polomer polomer vyšetrovaného kruhu
		 * @return {@code valtrue}/&#8203;{@code valfalse}
		 */
		public boolean bodV(Poloha objekt, double polomer)
		{
			double Δx = objekt.polohaX() - x3;
			double Δy = objekt.polohaY() - y3;
			return (Δx * Δx + Δy * Δy) <= polomer * polomer;
		}

		/**
		 * <p>Zistí, či sa súradnice zadaného bodu nachádzajú v kruhu
		 * s veľkosťou polomeru úmernému vzdialenosti inštancie tohto bodu
		 * od kamery (aktuálnej projekcie) a stredom na aktuálnej pozícii
		 * jeho projekcie.</p>
		 * 
		 * @param súradnicaX x-ová súradnica bodu
		 * @param súradnicaY y-ová súradnica bodu
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public boolean bodV(double súradnicaX, double súradnicaY)
		{
			double Δx = súradnicaX - x3;
			double Δy = súradnicaY - y3;
			return (Δx * Δx + Δy * Δy) <= z3 * z3;
		}

		/**
		 * <p>Funguje rovnako ako metóda {@link #bodV(double, double)
		 * bodV(x, y)}, len namiesto súradníc bodu je použitá poloha
		 * zadaného objektu…</p>
		 * 
		 * @param objekt objekt, ktorého poloha je použitá namiesto
		 *     súradníc bodu
		 * @return {@code valtrue}/&#8203;{@code valfalse}
		 */
		public boolean bodV(Poloha objekt)
		{
			double Δx = objekt.polohaX() - x3;
			double Δy = objekt.polohaY() - y3;
			return (Δx * Δx + Δy * Δy) <= z3 * z3;
		}


		/**
		 * <p>Overí, či sa súradnice myši nachádzajú v kruhu so zadaným
		 * polomerom a stredom na aktuálnej pozícii projekcie bodu.</p>
		 * 
		 * @param polomer polomer vyšetrovaného kruhu
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public boolean myšV(double polomer)
		{
			double Δx = ÚdajeUdalostí.súradnicaMyšiX - x3;
			double Δy = ÚdajeUdalostí.súradnicaMyšiY - y3;
			return (Δx * Δx + Δy * Δy) <= polomer * polomer;
		}

		/** <p><a class="alias"></a> Alias pre {@link #myšV(double) myšV}.</p> */
		public boolean mysV(double polomer) { return myšV(polomer); }

		/**
		 * <p>Zistí, či sa súradnice myši nachádzajú v kruhu s veľkosťou
		 * polomeru úmernému vzdialenosti inštancie tohto bodu od kamery
		 * (aktuálnej projekcie) a stredom na aktuálnej pozícii jeho
		 * projekcie.</p>
		 * 
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public boolean myšV()
		{
			double Δx = ÚdajeUdalostí.súradnicaMyšiX - x3;
			double Δy = ÚdajeUdalostí.súradnicaMyšiY - y3;
			return (Δx * Δx + Δy * Δy) <= z3 * z3;
		}

		/** <p><a class="alias"></a> Alias pre {@link #myšV() myšV}.</p> */
		public boolean mysV() { return myšV(); }


		/**
		 * <p>Umožňuje prevziať hodnotu zákaznícky definovaného celočíselného
		 * parametra bodu. Metóda používa zoznam {@link #celočíselnéParametre
		 * celočíselnéParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param predvolenáHodnota hodnota, ktorá bude vrátená v prípade,
		 *     že parameter s určeným indexom nie je definovaný
		 * @return hodnota parametra alebo predvolená hodnota
		 * 
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public int dajParameter(int index, int predvolenáHodnota)
		{
			Integer parameter = celočíselnéParametre.get(index);
			if (null == parameter) return predvolenáHodnota;
			return parameter;
		}

		/**
		 * <p>Umožňuje nastaviť hodnotu zákaznícky definovaného celočíselného
		 * parametra bodu. Metóda používa zoznam {@link #celočíselnéParametre
		 * celočíselnéParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param hodnota nová hodnota parametra
		 * 
		 * @see #dajParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public void nastavParameter(int index, int hodnota)
		{ celočíselnéParametre.put(index, hodnota); }

		/**
		 * <p>Umožňuje prevziať hodnotu zákaznícky definovaného reálneho
		 * parametra bodu. Metóda používa zoznam {@link #reálneParametre
		 * reálneParametre}.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia. -->
		 * 
		 * @param index index parametra
		 * @param predvolenáHodnota hodnota, ktorá bude vrátená v prípade,
		 *     že parameter s určeným indexom nie je definovaný
		 * @return hodnota parametra alebo predvolená hodnota
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public double dajParameter(int index, double predvolenáHodnota)
		{
			Double parameter = reálneParametre.get(index);
			if (null == parameter) return predvolenáHodnota;
			return parameter;
		}

		/**
		 * <p>Umožňuje nastaviť hodnotu zákaznícky definovaného reálneho
		 * parametra bodu. Metóda používa zoznam {@link #reálneParametre
		 * reálneParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param hodnota nová hodnota parametra
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public void nastavParameter(int index, double hodnota)
		{ reálneParametre.put(index, hodnota); }


		/**
		 * <p>Táto metóda umožňuje exportovať spoj definovaný k tomuto bodu
		 * do zadanej inštancie {@linkplain SVGPodpora SVG podpory}. Ide
		 * o metódu, ktorá je v prípade exportu do formátu SVG pomyselným
		 * obrazom metódy {@link #kresliSpoj() kresliSpoj}. Táto metóda je
		 * automaticky volaná metódou roja {@link 
		 * Roj#pridajDoSVG(SVGPodpora) pridajDoSVG}.</p>
		 * 
		 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
		 *     do ktorej bude exportovaný {@linkplain Shape tvar Javy
		 *     (čiary)} reprezentujúci viditeľný spoj medzi prechádzajúcim
		 *     a týmto bodom roja
		 * 
		 * @see Roj#pridajDoSVG(SVGPodpora)
		 */
		public void spojDoSVG(SVGPodpora svgPodpora)
		{
			if (faktor > 0.0)
			{
				double x0 = Svet.prepočítajX(kresli.polohaX());
				double y0 = Svet.prepočítajY(kresli.polohaY());
				kresli.skočNa(x3, y3);
				double x1 = Svet.prepočítajX(kresli.polohaX());
				double y1 = Svet.prepočítajY(kresli.polohaY());

				if (neplatnáSúradnica)
					neplatnáSúradnica = false;
				else if (spoj)
				{
					svgPodpora.pridaj(new Line2D.Double(x0, y0, x1, y1));
					svgPodpora.hrúbkaČiary(-1, kresli.hrúbkaČiary() + dh);
					if (null == farbaSpoja)
						svgPodpora.farbaČiary(-1, kresli.farba());
					else
						svgPodpora.farbaČiary(-1, farbaSpoja);
				}
			}
			else neplatnáSúradnica = true;
		}

		/**
		 * <p>Táto metóda umožňuje exportovať objekt definovaný pre tento
		 * bod do zadanej inštancie {@linkplain SVGPodpora SVG podpory}.
		 * Ide o metódu, ktorá je v prípade exportu do formátu SVG
		 * pomyselným obrazom metódy {@link #kresliTeleso() kresliTeleso}.
		 * Metóda využíva hodnoty atribútov {@link #svgKreslenie
		 * svgKreslenie} a {@link #svgTvar svgTvar}, ak sú neprázdne.
		 * (Pozri aj ich opisy.) Táto metóda je automaticky volaná metódou
		 * roja {@link Roj#pridajDoSVG(SVGPodpora) pridajDoSVG}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri exporte sa na prístup
		 * k aktuálne exportovanému bodu dá s výhodou využiť atribút
		 * {@link Roj#bod bod} roja.</p>
		 * 
		 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
		 *     do ktorej budú exportované {@linkplain Shape tvary Javy}
		 *     reprezentujúce teleso umiestnené v tomto bode roja
		 */
		public void telesoDoSVG(SVGPodpora svgPodpora)
		{
			if (faktor > 0.0)
			{
				if (zobraz)
				{
					kresli.smer(smer);
					kresli.skočNa(x3, y3);
					kresli.veľkosť(z3);

					if (null != svgKreslenie)
					{
						// TODO – otestovať
						Farba f = kresli.farba();
						if (null != farba)
							kresli.farba(farba);
						kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);

						if (kresli.kreslenieTvarovPovolené())
						{
							kresli.nekresliTvary();
							svgKreslenie.kresli(kresli);
							kresli.kresliTvary();
						}
						else svgKreslenie.kresli(kresli);

						kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						kresli.farba(f);
					}
					else
					{
						if (null == svgTvar)
						{
							if (kresli.kreslenieTvarovPovolené())
							{
								kresli.nekresliTvary();
								svgPodpora.pridaj(kresli.kruh());
								kresli.kresliTvary();
							}
							else svgPodpora.pridaj(kresli.kruh());
						}
						else
						{
							// svgPodpora.pridaj(svgTvar); – „Zlepšené“ (resp.
							// opravené) nasledujúcim kódom, pričom treba
							// povedať, že tento pôvodný riadok (aj keď nebol
							// nikdy otestovaný) by isto aj tak dobre
							// nefungoval. Pôvodne to bolo myslené tak, že:
							// „Však ak bude tento tvar definovaný, vloží sa
							// do SVG ten. Však ho netreba nejako extra
							// transformovať… Rotáciu a mierku obetujeme…“
							// Lenže sám od seba by sa nedostal ani na
							// správnu pozíciu, heh…

							// TODO – overiť, či toto riešenie dostatočne
							// dobre funguje:
							if (kresli.kreslenieTvarovPovolené())
							{
								kresli.nekresliTvary();
								svgPodpora.pridaj(kresli.
									kresliTvar(svgTvar, true));
								kresli.kresliTvary();
							}
							else svgPodpora.pridaj(kresli.
								kresliTvar(svgTvar, true));
						}

						svgPodpora.hrúbkaČiary(-1, kresli.hrúbkaČiary() + dho);
						if (null == farba)
							svgPodpora.farbaČiary(-1, kresli.farba());
						else
							svgPodpora.farbaČiary(-1, farba);
					}
				}
			}
		}


		/**
		 * <p>Prečíta údaje o inštancii tohto bodu z konfiguračného súboru
		 * otvoreného na čítanie. Metóda vyžaduje identifikátor menného
		 * priestoru, ktorý bol vyhradený pre tento bod. <small>(Roj priraďuje
		 * automaticky ku každému bodu identifikátor v tvare {@code 
		 * bod[}<em>«index»</em>{@code ]}.)</small></p>
		 * 
		 * (Pozri aj informácie uvedené v opise metódy {@link 
		 * #uložDoSúboru(Súbor, String) uložDoSúboru}.)
		 * 
		 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
		 *     na čítanie
		 * @param identifikátor vnorený menný priestor, z ktorého budú
		 *     prevzaté údaje o bode
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public void čítajZoSúboru(Súbor súbor, String identifikátor)
			throws IOException
		{
			súbor.vnorMennýPriestorVlastností(identifikátor);

			try
			{
				{
					double[] predvolené = null; // new double[] {0.0, 0.0, 0.0};
					double[] prečítané;

					prečítané = súbor.čítajVlastnosť("poloha", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						x0 = prečítané[0];
						y0 = prečítané[1];
						z0 = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("posun", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						dx = prečítané[0];
						dy = prečítané[1];
						dz = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("stred", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						xs = prečítané[0];
						ys = prečítané[1];
						zs = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("uhly", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						alfa = prečítané[0];
						beta = prečítané[1];
						gama = prečítané[2];
					}
				}

				rozmer = súbor.čítajVlastnosť("rozmer", rozmer);
				zobraz = súbor.čítajVlastnosť("zobraz", zobraz);
				spoj = súbor.čítajVlastnosť("spoj", spoj);
				skupina = súbor.čítajVlastnosť("skupina", skupina);

				dho = súbor.čítajVlastnosť("dho", dho);
				dh = súbor.čítajVlastnosť("dh", dh);

				farba = súbor.čítajVlastnosť("farba", farba);
				farbaSpoja = súbor.čítajVlastnosť("farbaSpoja", farbaSpoja);

				{
					int[] predvolené = null, prečítané;

					celočíselnéParametre.clear();
					prečítané = súbor.čítajVlastnosť("parametre", predvolené);
					if (null != prečítané)
					{
						int[] hodnoty = súbor.čítajVlastnosť(
							"hodnoty", predvolené);

						if (null == hodnoty)
							for (int j : prečítané)
								celočíselnéParametre.put(j, 0);
						else
						{
							int i = 0;
							for (int j : prečítané)
							{
								if (i < hodnoty.length)
									celočíselnéParametre.put(j, hodnoty[i]);
								else
									celočíselnéParametre.put(j, 0);
								++i;
							}
						}
					}

					prečítané = súbor.čítajVlastnosť("prázdne", predvolené);
					if (null != prečítané)
						for (int j : prečítané)
							celočíselnéParametre.put(j, null);

					reálneParametre.clear();
					prečítané = súbor.čítajVlastnosť(
						"reálneParametre", predvolené);
					if (null != prečítané)
					{
						double[] hodnoty = súbor.čítajVlastnosť(
							"reálneHodnoty", (double[])null);

						if (null == hodnoty)
							for (int j : prečítané)
								reálneParametre.put(j, 0.0);
						else
						{
							int i = 0;
							for (int j : prečítané)
							{
								if (i < hodnoty.length)
									reálneParametre.put(j, hodnoty[i]);
								else
									reálneParametre.put(j, 0.0);
								++i;
							}
						}
					}

					prečítané = súbor.čítajVlastnosť(
						"reálnePrázdne", predvolené);
					if (null != prečítané)
						for (int j : prečítané)
							reálneParametre.put(j, null);
				}

				transformuj = true;
			}
			finally
			{
				súbor.vynorMennýPriestorVlastností();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void citajZoSuboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void prečítajZoSúboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void precitajZoSuboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/**
		 * <p>Zapíše údaje o inštancii tohto bodu do konfiguračného súboru
		 * otvoreného na zápis. Metóda vyžaduje identifikátor menného
		 * priestoru, ktorý bude použitý na bezpečné uloženie množiny
		 * údajov o bode. Roj priraďuje automaticky ku každému bodu
		 * identifikátor v tvare {@code bod[}<em>«index»</em>{@code ]}. Ak
		 * je namiesto identifikátora zadaná hodnota {@code valnull}, tak
		 * bude použitý aktuálny menný priestor a ak by to bol hlavný menný
		 * priestor, tak jestvuje vysoké riziko toho, že údaje budú
		 * kolidovať s inými vlastnosťami zapísanými v súbore.
		 * <small>(V skutočnosti by zápis a následné čítanie bez
		 * identifikátora menného podpriestoru fungoval len pre jediný bod
		 * roja, čo nie je príliš užitočné.)</small></p>
		 * 
		 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
		 *     na zápis
		 * @param identifikátor vnorený menný priestor, do ktorého budú
		 *     vložené údaje o bode
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public void uložDoSúboru(Súbor súbor, String identifikátor)
			throws IOException
		{
			súbor.vnorMennýPriestorVlastností(identifikátor);

			try
			{
				súbor.zapíšVlastnosť("poloha", new double[] {x0, y0, z0});
				súbor.zapíšVlastnosť("posun", new double[] {dx, dy, dz});
				súbor.zapíšVlastnosť("stred", new double[] {xs, ys, zs});
				súbor.zapíšVlastnosť("uhly", new double[] {alfa, beta, gama});

				súbor.zapíšVlastnosť("rozmer", rozmer);
				súbor.zapíšVlastnosť("zobraz", zobraz);
				súbor.zapíšVlastnosť("spoj", spoj);
				súbor.zapíšVlastnosť("skupina", skupina);

				súbor.zapíšVlastnosť("dho", dho);
				súbor.zapíšVlastnosť("dh", dh);

				súbor.zapíšVlastnosť("farba", farba);
				súbor.zapíšVlastnosť("farbaSpoja", farbaSpoja);

				StringBuilder kľúče = new StringBuilder();
				StringBuilder hodnoty = new StringBuilder();
				StringBuilder prázdne = new StringBuilder();

				for (Map.Entry<Integer, Integer> záznam :
					celočíselnéParametre.entrySet())
				{
					Integer kľúč = záznam.getKey();
					Integer hodnota = záznam.getValue();
					if (null == hodnota)
					{
						prázdne.append(kľúč);
						prázdne.append(" ");
					}
					else
					{
						kľúče.append(kľúč);
						kľúče.append(" ");
						hodnoty.append(hodnota);
						hodnoty.append(" ");
					}
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("parametre");
				else
				{
					súbor.zapíšVlastnosť("parametre", kľúče);
					súbor.zapíšVlastnosť("hodnoty", hodnoty);
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("prázdne");
				else
					súbor.zapíšVlastnosť("prázdne", prázdne);

				kľúče.setLength(0);
				hodnoty.setLength(0);
				prázdne.setLength(0);

				for (Map.Entry<Integer, Integer> záznam :
					celočíselnéParametre.entrySet())
				{
					Integer kľúč = záznam.getKey();
					Integer hodnota = záznam.getValue();
					if (null == hodnota)
					{
						prázdne.append(kľúč);
						prázdne.append(" ");
					}
					else
					{
						kľúče.append(kľúč);
						kľúče.append(" ");
						hodnoty.append(hodnota);
						hodnoty.append(" ");
					}
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("reálneParametre");
				else
				{
					súbor.zapíšVlastnosť("reálneParametre", kľúče);
					súbor.zapíšVlastnosť("reálneHodnoty", hodnoty);
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("reálnePrázdne");
				else
					súbor.zapíšVlastnosť("reálnePrázdne", prázdne);
			}
			finally
			{
				súbor.vynorMennýPriestorVlastností();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor, String) uložDoSúboru}.</p> */
		public void ulozDoSuboru(Súbor súbor, String identifikátor)
		throws IOException { uložDoSúboru(súbor, identifikátor); }
	}


	// <!-- Varovanie‼ JavaDoc nechce vygenerovať záznamy o Bode a Smerníku,
	// Preto treba všetky zmeny v prvej vete preniesť aj do RoboDoc.java. -->

	/**
	 * <p>Pomocná trieda na riadené vkladanie bodov do roja. Smerník je
	 * jednoduchá neviditeľná pomôcka na generovanie (pridávanie) bodov
	 * roja. Smerník je orientovaný kurzor umožňujúci pridávanie bodov
	 * na svojej pozícii. Dá sa presúvať, otáčať, orientovať smerom na
	 * zadaný bod, uchovávať (zálohovať) a obnovovať svoju polohu
	 * a orientáciu s použitím vnútorných zásobníkov a podobne.</p>
	 * 
	 * <!-- TODO – vyrobiť príklad použitia. -->
	 */
	public class Smerník
	{
		// Aktuálna poloha smerníka.
		private double x = 0.0;
		private double y = 0.0;
		private double z = 0.0;

		// Aktuálny smer (orientácia; zavrhnuté – miskoncepcia).
		// private double α = 0.0;
		// private double β = 0.0;
		// private double γ = 0.0;

		// Jednotkový vektor posunu – smer smerníka.
		private double Δx = 0.0;
		private double Δy = 0.0;
		private double Δz = 1.0;

		// Stred otáčania (nepoužité; zavrhnuté – miskoncepcia).
		// private double sx = 0.0;
		// private double sy = 0.0;
		// private double sz = 0.0;

		// Zásobníky na zálohovanie polohy a smeru.
		private final Stack<double[]> zásobník = new Stack<>();
		private final Stack<double[]> zásobníkPolôh = new Stack<>();
		private final Stack<double[]> zásobníkSmerov = new Stack<>();

		/**
		 * <p>Príznak toho, či nové vkladané body majú mať viditeľné
		 * (definované) spoje.</p>
		 */
		public boolean vkladajSpoje = true;

		/**
		 * <p>Príznak toho, či nové vkladané body majú mať viditeľné
		 * objekty – či majú byť objekty definované na ich polohe kreslené.</p>
		 */
		public boolean vkladajObjekty = true;


		// // Na testovanie:
		// public void dumpPos()
		// { System.out.println("Pos[" + x + ", " + y + ", " + z + "]"); }
		// // public void dumpDir()
		// // { System.out.println("Dir[" + α + ", " + β + ", " + γ + "]"); }
		// public void dumpUni()
		// { System.out.println("Uni[" + Δx + ", " + Δy + ", " + Δz + "]"); }
		// public void dump() { dumpPos(); // dumpDir();
		// dumpUni(); }


		/**
		 * <p>Vráti aktuálnu polohu smerníka vo forme trojprvkového
		 * poľa [x, y, z].</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] poloha() { return new double[] {x, y, z}; }

		/**
		 * <p>Presunie smerník na zadané súradnice.</p>
		 * 
		 * @param x x-ová súradnica novej polohy
		 * @param y y-ová súradnica novej polohy
		 * @param z z-ová súradnica novej polohy
		 */
		public void poloha(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/**
		 * <p>Presunie smerník na súradnice zadané vo forme trojprvkového
		 * poľa. (Ak má pole menší počet prvkov, poloha nebude nastavená.
		 * Pri väčšom počte prvkov sú brané do úvahy len prvé tri.)</p>
		 * 
		 * @param bod najmenej trojprvkové pole súradníc novej polohy
		 */
		public void poloha(double[] bod)
		{
			if (null != bod && bod.length >= 3)
			{
				x = bod[0];
				y = bod[1];
				z = bod[2];
			}
		}

		/**
		 * <p>Posunie smerník o zadaný rozdiel súradníc.</p>
		 * 
		 * @param Δx prírastok x-ovej súradnice
		 * @param Δy prírastok y-ovej súradnice
		 * @param Δz prírastok z-ovej súradnice
		 */
		public void posuň(double Δx, double Δy, double Δz)
		{
			x += Δx;
			y += Δy;
			z += Δz;
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double, double, double) posuň}.</p> */
		public void posun(double Δx, double Δy, double Δz)
		{ posuň(Δx, Δy, Δz); }

		/**
		 * <p>Posunie smerník o zadaný rozdiel súradníc zadaný vo forme
		 * trojprvkového poľa. (Ak má pole menší počet prvkov, poloha nebude
		 * upravená. Pri väčšom počte prvkov sú brané do úvahy len prvé
		 * tri.)</p>
		 * 
		 * @param bod najmenej trojprvkové pole prírastkov súradníc polohy
		 */
		public void posuň(double[] bod)
		{
			if (null != bod && bod.length >= 3)
			{
				x += bod[0];
				y += bod[1];
				z += bod[2];
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double[]) posuň}.</p> */
		public void posun(double[] bod) { posuň(bod); }

		/**
		 * <p>Posunie smerník o zadanú dĺžku v aktuálnom smere (jeho
		 * orientácie).</p>
		 * 
		 * @param dĺžka dĺžka, o ktorú sa má smerník posunúť v aktuálnom
		 *     smere jednotkového vektora
		 */
		public void posuň(double dĺžka)
		{
			x += Δx * dĺžka;
			y += Δy * dĺžka;
			z += Δz * dĺžka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double) posuň}.</p> */
		public void posun(double dĺžka) { posuň(dĺžka); }


		/**
		 * <p>Vráti aktuálny „smer“ (jednotkový vektor pohybu) smerníka vo
		 * forme trojprvkového poľa [Δx, Δy, Δz].</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] smer() { return new double[] {Δx, Δy, Δz}; }

		/**
		 * <p>Nastaví „smer“ (jednotkový vektor pohybu) smerníka podľa zadaných
		 * súradníc smerového vektora. Súradnice nemusia byť normalizované,
		 * metóda ich normalizuje.</p>
		 * 
		 * @param x x-ová súradnica smerového vektora
		 * @param y y-ová súradnica smerového vektora
		 * @param z z-ová súradnica smerového vektora
		 */
		public void smer(double x, double y, double z)
		{
			double Î = sqrt(x * x + y * y + z * z);
			Δx = x / Î; Δy = y / Î; Δz = z / Î;
		}

		/**
		 * <p>Nastaví „smer“ (jednotkový vektor pohybu) smerníka podľa
		 * zadaných súradníc smerového vektora. Súradnice nemusia byť
		 * normalizované, metóda ich normalizuje.</p>
		 * 
		 * @param vektor najmenej trojprvkové pole súradníc smerového vektora
		 */
		public void smer(double[] vektor)
		{
			if (null != vektor && vektor.length >= 3)
			{
				double Î = sqrt(vektor[0] * vektor[0] +
					vektor[1] * vektor[1] + vektor[2] * vektor[2]);
				Δx = vektor[0] / Î; Δy = vektor[1] / Î; Δz = vektor[2] / Î;
			}
		}

		/**
		 * <p>Pootočí smerník (zmení orientáciu) o zadané uhly v smere osí x,
		 * y a z. Ide o pootočenie jednotkového vektora pohybu, takže aj keď
		 * je výpočtovo stred otáčania v strede súradnicovej sústavy, výsledný
		 * praktický efekt pootočenia vektora je zmena orientácie smerníka,
		 * ktorý sa od tohto momentu bude pohybovať zmeneným smerom.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 * 
		 * @param Δα hodnota zmeny uhla v smere osi x
		 * @param Δβ hodnota zmeny uhla v smere osi y
		 * @param Δγ hodnota zmeny uhla v smere osi z
		 */
		public void otoč(double Δα, double Δβ, double Δγ)
		{
			double αr = toRadians(Δα);
			double βr = toRadians(Δβ);
			double γr = toRadians(Δγ);

			double sinα = sin(αr);
			double sinβ = sin(βr);
			double sinγ = sin(γr);

			double cosα = cos(αr);
			double cosβ = cos(βr);
			double cosγ = cos(γr);

			double T00 = cosβ * cosγ;
			double T01 = -cosβ * sinγ;
			double T02 = sinβ;

			double T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
			double T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
			double T12 = -sinα * cosβ;

			double T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
			double T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
			double T22 = cosα * cosβ;

			double x1 = (Δx * T00) + (Δy * T01) + (Δz * T02);
			double y1 = (Δx * T10) + (Δy * T11) + (Δz * T12);
			double z1 = (Δx * T20) + (Δy * T21) + (Δz * T22);

			Δx = x1; Δy = y1; Δz = z1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double, double, double) otoč}.</p> */
		public void otoc(double Δα, double Δβ, double Δγ)
		{ otoč(Δα, Δβ, Δγ); }

		/**
		 * <p>Pootočí smerník o uhly v smere osí x, y a z zadané vo forme
		 * poľa. Ak pole obsahuje aspoň tri prvky, tak sa metóda správa
		 * rovnako ako metóda {@link #otoč(double, double, double) otoč(Δα, Δβ,
		 * Δγ)}, ktorej boli poslané tri uhly z prvých troch prvkov poľa. Ak
		 * pole obsahuje len dva prvky, tak je to ekvivalentné volaniu metódy
		 * {@link #otoč(double, double, double) otoč(Δα, Δβ, 0.0)}, kde sú do
		 * prvých dvoch parametrov vložené dva prvky poľa a podobne pri
		 * jednoprvkovom poli ide o volanie ekvivalentné vykonaniu {@link 
		 * #otoč(double, double, double) otoč(Δα, 0.0, 0.0)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 * 
		 * @param uhly najmenej trojprvkové pole uhlov rotácie okolo osí
		 *     x, y a z
		 */
		public void otoč(double[] uhly)
		{
			if (null != uhly)
			{
				if (uhly.length >= 3)
					otoč(uhly[0], uhly[1], uhly[2]);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double[]) otoč}.</p> */
		public void otoc(double[] uhly) { otoč(uhly); }

		/**
		 * <p>Pootočí smerník (zmení jeho orientáciu) o uhol Θ okolo osi
		 * prechádzajúcej stredom súradnicovej sústavy (<smer>pretože ide
		 * o zmenu smerového vektora a zjednodušene môžeme povedať, že
		 * tento je lokalizovaný v strede súradnicovej sústavy</smer>)
		 * a orientovanej podľa smerového vektora ⟨u, v, w⟩.</p>
		 * 
		 * @param u x-ová súradnica smerového vektora určujúceho os rotácie
		 * @param v y-ová súradnica smerového vektora určujúceho os rotácie
		 * @param w z-ová súradnica smerového vektora určujúceho os rotácie
		 * @param Θ uhol rotácie smerníka
		 * 
		 * @see #otoč(double)
		 */
		public void otoč(double u, double v, double w, double Θ)
		{
			// TODO testuj…

			// Zdroje:
			// — Rovnaké ako v otočOkolo(a, b, c, u, v, w, Θ, î) – metóda je
			//   jej zjednodušením.

			// Normalizuj vektor (meníme smer, musíme mať istotu):
			double Î = sqrt(u * u + v * v + w * w);
			u /= Î; v /= Î; w /= Î;

			double Θr = toRadians(Θ);
			double sinΘ = sin(Θr), cosΘ = cos(Θr);
			double cosΘ_1 = 1 - cosΘ;
			double uΔx_vΔy_wΔz = u * Δx + v * Δy + w * Δz;

			double Δx1 = -u *  uΔx_vΔy_wΔz * cosΘ_1 +
				Δx * cosΘ + (-w * Δy + v * Δz) * sinΘ;
			double Δy1 = -v *  uΔx_vΔy_wΔz * cosΘ_1 +
				Δy * cosΘ + ( w * Δx - u * Δz) * sinΘ;
			double Δz1 = -w * -uΔx_vΔy_wΔz * cosΘ_1 +
				Δz * cosΘ + (-v * Δx + u * Δy) * sinΘ;

			Δx = Δx1; Δy = Δy1; Δz = Δz1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double, double, double, double) otoč}.</p> */
		public void otoc(double u, double v, double w, double Θ)
		{ otoč(u, v, w, Θ); }

		/**
		 * <p>Pootočí smerník (zmení jeho orientáciu) o zadaný uhol okolo
		 * osi prechádzajúcej stredom súradnicovej sústavy a smerujúcej
		 * k poslednému {@linkplain #zálohujSmer() uloženému smeru} smerníka.
		 * Ak je zásobník smerov prázdny, tak metóda nevykoná nič.</p>
		 * 
		 * <p>(Pozri aj informácie v metóde {@link #otoč(double, double,
		 * double, double) otoč(u, v, w, Θ)}.)</p>
		 * 
		 * @param uhol uhol pootočenia smerníka okolo osi prechádzajúcej
		 *     stredom súradnicovej sústavy a smerujúcej podľa orientácie
		 *     prevzatej z vrchola zásobníka smerov
		 * 
		 * @see #otoč(double, double, double, double)
		 */
		public void otoč(double uhol)
		{
			double[] a = dajSmer();
			if (null != a) otoč(a[0], a[1], a[2], uhol);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double) otoč}.</p> */
		public void otoc(double uhol) { otoč(uhol); }


		/**
		 * <p>Zmení polohu smerníka (orientácia zostane zachovaná) jeho
		 * pootočením o uhol Θ okolo osi prechádzajúcej bodom [a, b, c]
		 * a smerujúcej buď k bodu [u, v, w] (ak je parameter {@code î}
		 * rovný {@code valfalse}), alebo orientovanej v smere jednotkového
		 * vektora ⟨u, v, w⟩ (ak je parameter {@code î} rovný {@code 
		 * valtrue}).</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak je parameter {@code î}
		 * rovný {@code valtrue}, tak hodnoty parametrov u, v a w
		 * musia<small>(!)</small> obsahovať <em>normalizované</em>
		 * súradnice (súradnice jednotkového vektora), inak bude výsledok
		 * nesprávny!</p>
		 * 
		 * @param a x-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param b y-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param c z-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param u x-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param v y-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param w z-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param Θ uhol rotácie smerníka
		 * @param î rozlišovací parameter toho, či parametre u, v a w určuje
		 *     bod, do ktorého má os rotácie smerovať (hodnota {@code 
		 *     valfalse}), alebo jednotkový vektor (hodnota {@code valtrue})
		 * 
		 * @see #otočOkolo(double)
		 */
		public void otočOkolo(double a, double b, double c, double u, double v,
			double w, double Θ, boolean î)
		{
			// Zdroje:
			// — Weisstein, Eric W. “Unit Vector.” From MathWorld – A Wolfram
			//   Web Resource. [cit. 2018-07-14] ⟨http://mathworld.wolfram.
			//   com/UnitVector.html⟩
			// — Weisstein, Eric W. “Norm.” From MathWorld – A Wolfram Web
			//   Resource. [cit. 2018-07-14] ⟨http://mathworld.wolfram.com/
			//   Norm.html⟩
			// — Murray, Glenn. May 6, 2013. Rotation About an Arbitrary Axis
			//   in 3 Dimensions. [cit. 2018-07-14] ⟨https://sites.google.
			//   com/site/glennmurray/Home/rotation-matrices-and-formulas/
			//   rotation-about-an-arbitrary-axis-in-3-dimensions⟩

			if (!î)
			{
				// Normalizuj vektor:
				u -= a; v -= b; w -= c;
				double Î = sqrt(u * u + v * v + w * w);
				u /= Î; v /= Î; w /= Î;
			}

			double u2 = u * u, v2 = v * v, w2 = w * w;
			double Θr = toRadians(Θ);
			double sinΘ = sin(Θr), cosΘ = cos(Θr);
			double cosΘ_1 = 1 - cosΘ;

			double au = a * u, bv = b * v;
			double ux_vy_wz = u * x + v * y + w * z;
			double cw_ux_vy_wz = c * w - ux_vy_wz;

			double x1 = a * (v2 + w2) - u * (bv + cw_ux_vy_wz) * cosΘ_1 +
				x * cosΘ + (-c * v + b * w - w * y + v * z) * sinΘ;
			double y1 = b * (u2 + w2) - v * (au + cw_ux_vy_wz) * cosΘ_1 +
				y * cosΘ + ( c * u - a * w + w * x - u * z) * sinΘ;
			double z1 = c * (u2 + v2) - w * (au + bv - ux_vy_wz) * cosΘ_1 +
				z * cosΘ + (-b * u + a * w - v * x + u * y) * sinΘ;

			x = x1; y = y1; z = z1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočOkolo(double, double, double, double, double, double, double, boolean) otoč}.</p> */
		public void otocOkolo(double a, double b, double c, double u, double v,
			double w, double Θ, boolean î) { otočOkolo(a, b, c, u, v, w, Θ, î); }

		/**
		 * <p>Zmení polohu smerníka (orientácia zostane zachovaná) jeho
		 * pootočením o zadaný uhol okolo osi určenej posledným {@linkplain 
		 * #zálohuj() uloženým stavom} smerníka (<small>pri ukladaní stavu
		 * je uchovaná poloha aj smer smerníka, takže na jednoznačné určenie
		 * osi rotácie je k dispozícii dostatočné množstvo údajov</small>).
		 * Ak je zásobník stavov prázdny, tak metóda nevykoná nič.</p>
		 * 
		 * <p>(Pozri aj informácie v metóde {@link #otočOkolo(double,
		 * double, double, double, double, double, double, boolean)
		 * otočOkolo(a, b, c, u, v, w, Θ, î)}.)</p>
		 * 
		 * @param uhol uhol pootočenia smerníka okolo osi prevzatej
		 *     z vrchola zásobníka stavov
		 * 
		 * @see #otočOkolo(double, double, double, double, double,
		 *     double, double, boolean)
		 */
		public void otočOkolo(double uhol)
		{
			double[] a = dajStav();
			if (null != a)
				otočOkolo(a[0], a[1], a[2], a[3], a[4], a[5], uhol, true);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočOkolo(double) otoč}.</p> */
		public void otocOkolo(double uhol) { otočOkolo(uhol); }


		// „Malý“ omyl v rámci zlého počiatočného stanovenia konceptu smerníka…
			// (Ale využije sa to aspoň pri zistení uhlov (orientácie)
			// všeobecného vektora. Pozri: uhlyK(…).)
			// 
			// Nastaví orientáciu smerníka tak, aby smeroval k zadanému bodu.
			// public void smerNa(double x, double y, double z)
			// {
			// 	// Fungovalo podľa User:Beta v:
			// 	// ⟨https://stackoverflow.com/questions/1251828/calculate-
			// 	// rotations-to-look-at-a-3d-point⟩
			// 	// (Asked asked Aug 9 ’09 at 17:41 by User:Robinicks, Calculate
			// 	// rotations to look at a 3D point? Answered Oct 27 ’10 at
			// 	// 17:54.)
			// 
			// 	x -= this.x;
			// 	y -= this.y;
			// 	z -= this.z;
			// 	α = -toDegrees(atan2(y, z));
			// 	β = toDegrees(atan2(x, hypot(y, z)));
			// 	γ = 0.0;
			// 
			// 	— prepočítajPosun(); —
			// 
			// 	// Prepočíta jednotkový vektor posunu podľa aktuálneho smeru.
			// 	// (Prepočet je zjednodušením všeobecných transformácií
			// 	// použitých aj v metódach Bod.transformuj() a prepočítajT() –
			// 	// ide v podstate o rotáciu jednotkového vektora paralelného
			// 	// s osou z.)
			// 	private void — prepočítajPosun() —
			// 	{
			// 		double αr = toRadians(α), βr = toRadians(β), cosβ = cos(βr);
			// 		Δx = sin(βr); Δy = -sin(αr) * cosβ; Δz = cos(αr) * cosβ;
			// 	}
			// }


		/**
		 * <p>Nastaví orientáciu smerníka tak, aby smeroval k zadanému
		 * bodu.</p>
		 * 
		 * @param x x-ová súradnica cieľového bodu
		 * @param y y-ová súradnica cieľového bodu
		 * @param z z-ová súradnica cieľového bodu
		 */
		public void smerNa(double x, double y, double z)
		{
			x -= this.x; y -= this.y; z -= this.z;
			double Î = sqrt(x * x + y * y + z * z);
			Δx = x / Î; Δy = y / Î; Δz = z / Î;
		}


		/**
		 * <p>Vloží do vnútorného zásobníka stavu smerníka jeho aktuálnu
		 * polohu aj smer. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohuj()
		{
			try
			{
				return zásobník.add(new double[]
					{x, y, z, Δx, Δy, Δz});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohuj() zálohuj}.</p> */
		public boolean zalohuj() { return zálohuj(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka stavu smerníka
		 * poslednú uloženú polohu aj smer. (Ak metóda uspeje, tak vráti
		 * {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnov()
		{
			try
			{
				double[] a = zásobník.pop();
				x = a[0]; y = a[1]; z = a[2];
				Δx = a[3]; Δy = a[4]; Δz = a[5];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka stavov smerníka vo forme šesťprvkového
		 * poľa [x, y, z, Δx, Δy, Δz]. Ak je zásobník prázdny alebo nastane
		 * iné zlyhanie, tak metóda vráti hodnotu {@code valnull}. Zásobník
		 * zostane nezmenený.</p>
		 * 
		 * @return šesťprvkové pole stavu (x, y, z, Δx, Δy, Δz) z vrchola
		 *     zásobníka stavov alebo {@code valnull}, ak je zásobník prázdny
		 */
		public double[] dajStav()
		{
			try
			{
				double[] a = zásobník.peek();
				return new double[] {a[0], a[1], a[2], a[3], a[4], a[5]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník stavov smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdni()
		{
			try
			{
				int počet = zásobník.size();
				zásobník.clear();
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdni() vyprázdni}.</p> */
		public int vyprazdni() { return vyprázdni(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka stavov smerníka.
		 * (Porovnaj s metódou {@link #vyprázdni() vyprázdni}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymaž()
		{
			try
			{
				int počet = zásobník.size();
				zásobník.pop();
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public int vymaz() { return vymaž(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka stavov smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymaž(int koľko)
		{
			try
			{
				int počet = zásobník.size();
				if (koľko > počet) koľko = počet;
				zásobník.setSize(počet - koľko);
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž(int) vymaž}.</p> */
		public int vymaz(int koľko) { return vymaž(koľko); }


		/**
		 * <p>Vloží do vnútorného zásobníka polôh smerníka jeho aktuálnu
		 * polohu. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohujPolohu()
		{
			try
			{
				return zásobníkPolôh.add(new double[] {x, y, z});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujPolohu() zálohujPolohu}.</p> */
		public boolean zalohujPolohu() { return zálohujPolohu(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka polôh smerníka poslednú
		 * uloženú polohu. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnovPolohu()
		{
			try
			{
				double[] a = zásobníkPolôh.pop();
				x = a[0]; y = a[1]; z = a[2];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka polôh smerníka vo forme trojprvkového
		 * poľa [x, y, z]. Ak je zásobník prázdny alebo nastane iné zlyhanie,
		 * tak metóda vráti hodnotu {@code valnull}. Zásobník zostane
		 * nezmenený.</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] dajPolohu()
		{
			try
			{
				double[] a = zásobníkPolôh.peek();
				return new double[] {a[0], a[1], a[2]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník polôh smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdniPolohy()
		{
			try
			{
				int počet = zásobníkPolôh.size();
				zásobníkPolôh.clear();
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniPolohy() vyprázdniPolohy}.</p> */
		public int vyprazdniPolohy() { return vyprázdniPolohy(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka polôh smerníka. (Porovnaj
		 * s metódou {@link #vyprázdniPolohy() vyprázdniPolohy}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažPolohu()
		{
			try
			{
				int počet = zásobníkPolôh.size();
				zásobníkPolôh.pop();
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPolohu() vymažPolohu}.</p> */
		public int vymazPolohu() { return vymažPolohu(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka polôh smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažPolohy(int koľko)
		{
			try
			{
				int počet = zásobníkPolôh.size();
				if (koľko > počet) koľko = počet;
				zásobníkPolôh.setSize(počet - koľko);
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPolohy(int) vymažPolohy}.</p> */
		public int vymazPolohy(int koľko) { return vymažPolohy(koľko); }


		/**
		 * <p>Vloží do vnútorného zásobníka smerov smerníka jeho aktuálny smer.
		 * (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohujSmer()
		{
			try
			{
				return zásobníkSmerov.add(new double[] {Δx, Δy, Δz});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujSmer() zálohujSmer}.</p> */
		public boolean zalohujSmer() { return zálohujSmer(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka smerov smerníka posledný
		 * uložený smer. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnovSmer()
		{
			try
			{
				double[] a = zásobníkSmerov.pop();
				Δx = a[0]; Δy = a[1]; Δz = a[2];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka smerov smerníka vo forme trojprvkového
		 * poľa [Δx, Δy, Δz]. Ak je zásobník prázdny alebo nastane iné
		 * zlyhanie, tak metóda vráti hodnotu {@code valnull}. Zásobník
		 * zostane nezmenený.</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] dajSmer()
		{
			try
			{
				double[] a = zásobníkSmerov.peek();
				return new double[] {a[0], a[1], a[2]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník smerov smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdniSmery()
		{
			try
			{
				int počet = zásobníkSmerov.size();
				zásobníkSmerov.clear();
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniSmery() vyprázdniSmery}.</p> */
		public int vyprazdniSmery() { return vyprázdniSmery(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka smerov smerníka. (Porovnaj
		 * s metódou {@link #vyprázdniSmery() vyprázdniSmery}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažSmer()
		{
			try
			{
				int počet = zásobníkSmerov.size();
				zásobníkSmerov.pop();
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSmer() vymažSmer}.</p> */
		public int vymazSmer() { return vymažSmer(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka smerov smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažSmery(int koľko)
		{
			try
			{
				int počet = zásobníkSmerov.size();
				if (koľko > počet) koľko = počet;
				zásobníkSmerov.setSize(počet - koľko);
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSmery(int) vymažSmery}.</p> */
		public int vymazSmery(int koľko) { return vymažSmery(koľko); }


		/**
		 * <p>Vyprázdni všetky vnútorné zásobníky smerníka. Metóda vráti
		 * trojprvkové pole, ktoré bude obsahovať, koľko záznamov bolo
		 * odstránených zo zásobníka stavov, polôh a smerov. Hodnota
		 * −1 niektorého z prvkov znamená, že vyprázdňovanie konkrétneho
		 * zásobníka zlyhalo.</p>
		 * 
		 * @return trojprvkové pole počtov skutočne odstránených záznamov
		 *     v poradí: stavy, polohy a smery; hodnota −1 prvka značí chybu
		 */
		public int[] vyprázdniVšetko()
		{
			int stavy = vyprázdni();
			int polohy = vyprázdniPolohy();
			int smery = vyprázdniSmery();
			return new int[] {stavy, polohy, smery};
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniVšetko() vyprázdniVšetko}.</p> */
		public int[] vyprazdniVsetko() { return vyprázdniVšetko(); }

		/**
		 * <p>Vloží do roja bod na aktuálnej polohe smerníka s nastavenými
		 * príznakmi kreslenia spoja a objektu na základe hodnôt príznakov
		 * smerníka vkladajSpoje a vkladajObjekty. Nový bod je návratovou
		 * hodnotou tejto metódy.</p>
		 * 
		 * @return inštancia bodu, ktorý bol práve pridaný do roja
		 */
		public Bod pridajBod()
		{
			Bod bod = new Bod();
			bod.x0 = x;
			bod.y0 = y;
			bod.z0 = z;
			bod.spoj = vkladajSpoje;
			bod.zobraz = vkladajObjekty;
			body.pridaj(bod);
			poradieKreslenia.pridaj(bod);
			transformuj = true;
			prepočítaj = true;
			return bod;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link Smerník Smerník}.</p> */
	public class Smernik extends Smerník {}

	/**
	 * <p>Inštancia smerníka. Podrobnosti v dokumentácii triedy
	 * {@link Smerník Smerník}.</p>
	 */
	public final Smernik smerník = new Smernik();

	/** <p><a class="alias"></a> Alias pre {@link #smerník smerník}.</p> */
	public final Smernik smernik = smerník;


	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla alfa metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla alfa metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int ALFA = 1;

	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla beta metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla beta metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int BETA = 2;

	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla gama metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla gama metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int GAMA = 4;


	/**
	 * <p>Toto je kresliaci robot roja. Ak nie je nastavený, tak roj nemôže
	 * byť kreslený a pri individuálnom pokuse o nakreslenie bodu roja nastane
	 * chyba. Tvar tohto robota určuje predvolený tvar objektov na polohách
	 * bodov roja. Vlastnosti pera ovplyvňujú kreslenie spojov medzi bodmi
	 * roja – hrúbka určuje predvolenú hrúbku a poloha (poloha v zmysle stavu
	 * zdvihnutia/ploženia pera) to, či budú všetky spoje paušálne nakreslené
	 * alebo nie.</p>
	 */
	public GRobot kresli = null;


	/**
	 * <p>Inštancia aktuálne spracúvaného bodu. Počas kreslenia spojníc
	 * a objektov alebo počas exportu do SVG formátu je do tohto atribútu
	 * ukladaná inštancia aktuálne spracúvaného bodu.</p>
	 */
	public Bod bod = null;


	// Vnútorný príznak toho, že naposledy bol kreslený bod s neplatnou
	// súradnicou (alebo ide o prvý kreslený bod).
	private boolean neplatnáSúradnica = false;

	// Vnútorné zoznamy bodov. Prvý zoznam slúži na prechádzanie bodov
	// v poradí ich pridania, čo je dôležité pri kreslení spojov a druhý
	// zoznam je pravidelne zoraďovaný podľa vzdialenosti bodov od kamery,
	// čo je dôležité pri kreslení telies umiestnených na súradniciach
	// bodov roja.
	private final Zoznam<Bod> body = new Zoznam<>();
	private final Zoznam<Bod> poradieKreslenia = new Zoznam<>(); // „Z-bufer“
		// (Poznámka: Pomenovanie z-bufer (angl. z-buffer) je prevzaté
		//     („požičané“) z názvu zásobníka bodov (pixelov) v 3D grafike
		//     slúžiaceho na rýchle rozpoznanie toho, ktorý z kreslených
		//     bodov obrazu je viac v popredí. Tento z-bufer má v podstate
		//     rovnaký účel, ale pricíp je odlišný (ako je spomenuté vyššie).
		//     Tiež body roja nie sú pixely, ale 3D lokality s množstvom
		//     rôznych parametrov.)


	// Atribúty súvisiace s orientáciou celého roja (ktorá sa dá vnímať aj
	// ako orientácia kamery) a príznak nevyhnutnosti prepočítania hodnôt
	// konečných súradníc bodov roja (x2, y2, z2). Konkrétne: xs, ys a zs
	// určujú stred otáčania roja (kamery) a α, β a γ sú uhly pootočenia
	// roja (kamery).
	private double xs = 0.0, ys = 0.0, zs = 0.0;
	private double α = -110.0, β = -360.0, γ = 45.0;
	private boolean transformuj = false;


	// Atribúty súvisiace s kamerou a príznak nevyhnutnosti prepočítania
	// cieľových (v súvislosti s kreslením) súradníc bodov roja (x3, y3, z3).
	private double mierka = 1000.0;
	private double kx = 0.0, ky = -125.0, kz = 200.0;
	private boolean prepočítaj = false;


	// Koeficienty transformácií a príznak nevyhnutnosti ich prepočítania.
	private double T00 =  0.707106781186548;
	private double T01 = -0.707106781186548;
	private double T02 =  0.0;

	private double T10 = -0.241844762647975;
	private double T11 = -0.241844762647975;
	private double T12 =  0.939692620785908;

	private double T20 = -0.664463024388675;
	private double T21 = -0.664463024388675;
	private double T22 = -0.342020143325669;

	private boolean prepočítajT = false;


	// Prepočíta koeficienty transformácií.
	private void prepočítajT()
	{
		/* Transformačná matica:
		(cosβ.cosγ                  | −cosβ.sinγ                 | sinβ      )
		(sinα.sinβ.cosγ + cosα.sinγ | cosα.cosγ − sinα.sinβ.sinγ | −sinα.cosβ)
		(sinα.sinγ − cosα.sinβ.cosγ | sinα.cosγ + cosα.sinβ.sinγ | cosα.cosβ )
		*/
		if (prepočítajT)
		{
			// „Ľavoruký“ (left-handed) systém:
			double αr = toRadians(α);
			double βr = toRadians(β);
			double γr = toRadians(γ);

			// „Pravoruký“ (right-handed) systém:
			// double αr = toRadians(-β);
			// double βr = toRadians(-α);
			// double γr = toRadians(-γ);

			double sinα = sin(αr);
			double sinβ = sin(βr);
			double sinγ = sin(γr);

			double cosα = cos(αr);
			double cosβ = cos(βr);
			double cosγ = cos(γr);

			T00 = cosβ * cosγ;
			T01 = -cosβ * sinγ;
			T02 = sinβ;

			T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
			T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
			T12 = -sinα * cosβ;

			T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
			T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
			T22 = cosα * cosβ;

			prepočítajT = false;
		}
	}


	/**
	 * <p>Konštruktor roja. Prijíma inštanciu grafického robota, ktorý bude
	 * kresličom roja. Bez kresliča roj nemôže byť nakreslený.</p>
	 * 
	 * @param kreslič kreslič roja
	 */
	public Roj(GRobot kreslič)
	{
		this.kresli = kreslič;
	}


	/**
	 * <p>Pridá nový bod do roja. Bod bude mať hodnoty všetkých atribútov
	 * nastavené na predvolené. Návratová hodnota tejto metódy je inštancia
	 * nového bodu, ktorá môže byť použitá na nastavenie všetkých vlastností
	 * bodu.</p>
	 */
	public Bod pridajBod()
	{
		Bod bod = new Bod();
		body.pridaj(bod);
		poradieKreslenia.pridaj(bod);
		transformuj = true;
		prepočítaj = true;
		return bod;
	}

	/**
	 * <p>Odstráni bod z roja.</p>
	 * 
	 * @param bod inštancia bodu, ktorý má byť z roja odstránený
	 */
	public void vymažBod(Bod bod)
	{
		body.odober(bod);
		poradieKreslenia.odober(bod);
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymažBod(Bod) vymažBod}.</p> */
	public void vymazBod(Bod bod) { vymažBod(bod); }

	/**
	 * <p>Odstráni všetky body z roja.</p>
	 */
	public void vymaž()
	{
		body.vymaž();
		poradieKreslenia.vymaž();
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
	public void vymaz() { vymaž(); }

	/**
	 * <p>Vráti kópiu aktuálneho zoznamu bodov roja. Zoznam je určený
	 * (predovšetkým) na prechádzanie a úpravu vlastností jednotlivých
	 * bodov. Vymazaním bodu z tohto zoznamu sa bod z roja nevymaže. Na
	 * vymazanie bodu slúži metóda roja {@link #vymažBod(Roj.Bod)
	 * vymažBod}.</p>
	 */
	public Zoznam<Bod> body() { return new Zoznam<Bod>(body); }

	/**
	 * <p>Vráti kópiu aktuálneho zoznamu bodov roja zoradeného podľa
	 * poradia kreslenia. Zoznam je určený (predovšetkým) na prechádzanie
	 * v súvislosti s prekresľovaním objektov. (Napríklad vymazaním bodu
	 * z tohto zoznamu sa bod nevymaže. Na vymazanie bodu z roja slúži
	 * metóda {@link #vymažBod(Roj.Bod) vymažBod}.)</p>
	 */
	public Zoznam<Bod> poradieKreslenia()
	{ return new Zoznam<Bod>(poradieKreslenia); }


	/**
	 * <p>Vráti aktuálne hodnoty uhlov alfa (α), beta (β) a gama (γ) roja
	 * vo forme trojprvkového poľa [α, β, γ].</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálne hodnoty uhlov roja v trojprvkovom poli
	 * 
	 * @see #nastavUhly(double[])
	 */
	public double[] uhly() { return new double[] {α, β, γ}; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla alfa (vnútorne značeného
	 * {@code α}). Tento uhol je spätý s rotáciou okolo osi x, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla alfa
	 * 
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholAlfa() { return α; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla beta (vnútorne značeného
	 * {@code β}). Tento uhol je spätý s rotáciou okolo osi y, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla beta
	 * 
	 * @see #uholAlfa()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholBeta() { return β; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla gama (vnútorne značeného
	 * {@code γ}). Tento uhol je spätý s rotáciou okolo osi z, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholGama() { return γ; }

	/**
	 * <p>Nastaví novú hodnotu uhlu alfa (vnútorne značeného {@code α}).
	 * Pozri aj opis metódy {@link #uholAlfa() uholAlfa()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla alfa
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholAlfa(double hodnota)
	{
		α = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú hodnotu uhlu beta (vnútorne značeného {@code β}).
	 * Pozri aj opis metódy {@link #uholBeta() uholBeta()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla beta
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholBeta(double hodnota)
	{
		β = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú hodnotu uhlu gama (vnútorne značeného {@code γ}).
	 * Pozri aj opis metódy {@link #uholGama() uholGama()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholGama(double hodnota)
	{
		γ = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Vráti hodnotu zadaného uhla, pričom ak zadaná konštanta obsahuje
	 * kombináciu príznakov viacerých uhlov, tak prioritne vracia uhol
	 * {@link #ALFA ALFA}, potom {@link #BETA BETA} a nakoniec
	 * {@link #GAMA GAMA}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param ktorý konštanta určujúca, ktorý uhol má byť vrátený
	 *     ({@link #ALFA ALFA}, {@link #BETA BETA}, {@link #GAMA GAMA})
	 * @return hodnota uhla určeného parametrom {@code ktorý}
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uhol(int ktorý)
	{
		if (0 != (ktorý & ALFA)) return α;
		if (0 != (ktorý & BETA)) return β;
		if (0 != (ktorý & GAMA)) return γ;
		return 0.0;
	}

	/**
	 * <p>Nastaví jeden alebo viacero uhlov na stanovenú hodnotu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param ktorý konštanta určujúca, ktorý uhol alebo uhly majú byť
	 *     nastavené; parameter môže byť kombináciou konštánt {@link #ALFA
	 *     ALFA}, {@link #BETA BETA} a {@link #GAMA GAMA})
	 * @param hodnota nová hodnota uhla alebo uhlov určených parametrom
	 *     {@code ktorý}
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uhol(int ktorý, double hodnota)
	{
		if (0 != (ktorý & ALFA)) α = hodnota;
		if (0 != (ktorý & BETA)) β = hodnota;
		if (0 != (ktorý & GAMA)) γ = hodnota;

		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví hodnoty všetkých troch uhlov {@linkplain #uholAlfa() alfa},
	 * {@linkplain #uholBeta() beta} a {@linkplain #uholGama() gama}. (Pozri
	 * aj {@linkplain #nastavStredOtáčania(double, double, double) stred
	 * otáčania}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param nα nová hodnota uhla alfa
	 * @param nβ nová hodnota uhla beta
	 * @param nγ nová hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #pootoč(double, double, double)
	 */
	public void nastavUhly(double nα, double nβ, double nγ)
	{
		α = nα; β = nβ; γ = nγ;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví hodnoty uhlov roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie orientácie roja (napríklad
	 * v súvislosti zo zálohou a obnovou stavu roja; pozri aj {@link 
	 * #uhly() uhly}). Ak je zadané pole aspoň šesťprvkové,
	 * tak metóda pracuje s druhou trojicou prvkov poľa. Ak je pole
	 * aspoň trojprvkové, tak metóda berie do úvahy jeho prvé tri
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná
	 * nič.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param pole pole na základe ktorého budú nastavené uhly roja
	 * 
	 * @see #uhly()
	 * @see #nastavUhly(double, double, double)
	 */
	public void nastavUhly(double[] pole)
	{
		if (null != pole && pole.length >= 3)
		{
			if (pole.length >= 6)
				{ α = pole[3]; β = pole[4]; γ = pole[5]; }
			else
				{ α = pole[0]; β = pole[1]; γ = pole[2]; }
			transformuj = true;
			prepočítajT = true;
			prepočítaj = true;
		}
	}

	/**
	 * <p>Pozmení hodnoty všetkých troch uhlov {@linkplain #uholAlfa() alfa},
	 * {@linkplain #uholBeta() beta} a {@linkplain #uholGama() gama}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param Δα miera zmeny hodnoty uhla alfa
	 * @param Δβ miera zmeny hodnoty uhla beta
	 * @param Δγ miera zmeny hodnoty uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 */
	public void pootoč(double Δα, double Δβ, double Δγ)
	{
		α += Δα; β += Δβ; γ += Δγ;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pootoč(double, double, double) pootoč}.</p> */
	public void pootoc(double Δα, double Δβ, double Δγ)
	{ pootoč(Δα, Δβ, Δγ); }

	/**
	 * <p>Pozmení hodnoty uhlov roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu orientácie roja. Ak je zadané pole
	 * aspoň šesťprvkové, tak metóda pracuje s druhou trojicou prvkov
	 * poľa. Ak je pole aspoň trojprvkové, tak metóda berie do úvahy
	 * jeho prvé tri prvky. (Ak má pole menší počet prvkov, tak metóda
	 * nevykoná nič.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param pole pole na základe ktorého budú pozmenené uhly roja
	 * 
	 * @see #nastavUhly(double[])
	 * @see #pootoč(double, double, double)
	 */
	public void pootoč(double[] pole)
	{
		if (null != pole && pole.length >= 3)
		{
			if (pole.length >= 6)
				{ α += pole[3]; β += pole[4]; γ += pole[5]; }
			else
				{ α += pole[0]; β += pole[1]; γ += pole[2]; }
			transformuj = true;
			prepočítajT = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pootoč(double[]) pootoč}.</p> */
	public void pootoc(double[] pole) { pootoč(pole); }


	/**
	 * <p>Vráti aktuálny {@linkplain #nastavStredOtáčania(double, double,
	 * double) stred otáčania} vo forme trojprvkového poľa [xs, ys, zs].</p>
	 * 
	 * @return aktuálny {@linkplain #nastavStredOtáčania(double, double,
	 *     double) stred otáčania}
	 * 
	 * @see #nastavStredOtáčania(double[])
	 */
	public double[] stredOtáčania() { return new double[] {xs, ys, zs}; }

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčania() stredOtáčania}.</p> */
	public double[] stredOtacania() { return stredOtáčania(); }

	/**
	 * <p>Vráti x-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota x-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaX()
	{
		return xs;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaX() stredOtáčaniaX}.</p> */
	public double stredOtacaniaX()
	{ return stredOtáčaniaX(); }

	/**
	 * <p>Vráti y-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota y-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaY()
	{
		return ys;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaY() stredOtáčaniaY}.</p> */
	public double stredOtacaniaY()
	{ return stredOtáčaniaY(); }

	/**
	 * <p>Vráti z-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota z-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaZ()
	{
		return zs;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaZ() stredOtáčaniaZ}.</p> */
	public double stredOtacaniaZ()
	{ return stredOtáčaniaZ(); }

	/**
	 * <p>Nastaví novú hodnotu x-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota x-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaX(double hodnota)
	{
		xs = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaX(double) stredOtáčaniaX}.</p> */
	public void stredOtacaniaX(double hodnota)
	{ stredOtáčaniaX(hodnota); }

	/**
	 * <p>Nastaví novú hodnotu y-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota y-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaY(double hodnota)
	{
		ys = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaY(double) stredOtáčaniaY}.</p> */
	public void stredOtacaniaY(double hodnota)
	{ stredOtáčaniaY(hodnota); }

	/**
	 * <p>Nastaví novú hodnotu z-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota z-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaZ(double hodnota)
	{
		zs = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaZ(double) stredOtáčaniaZ}.</p> */
	public void stredOtacaniaZ(double hodnota)
	{ stredOtáčaniaZ(hodnota); }

	/**
	 * <p>Nastaví nové súradnice stredu otáčania. (Pozri aj uhly {@linkplain 
	 * #uholAlfa() alfa}, {@linkplain #uholBeta() beta} a {@linkplain 
	 * #uholGama() gama}.)</p>
	 * 
	 * @param nxs nová hodnota x-ovej súradnice stredu otáčania
	 * @param nys nová hodnota y-ovej súradnice stredu otáčania
	 * @param nzs nová hodnota z-ovej súradnice stredu otáčania
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void nastavStredOtáčania(double nxs, double nys, double nzs)
	{
		xs = nxs; ys = nys; zs = nzs;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #nastavStredOtáčania(double, double, double) nastavStredOtáčania}.</p> */
	public void nastavStredOtacania(double nxs, double nys, double nzs)
	{ nastavStredOtáčania(nxs, nys, nzs); }

	/**
	 * <p>Nastaví stred otáčania roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie polohy stredu otáčania roja berúc
	 * do úvahy spôsob práce {@linkplain #smerník smerníka}. Ak je zadané
	 * pole aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu určujúce novú polohu stredu otáčania roja
	 * 
	 * @see #stredOtáčania()
	 * @see #nastavStredOtáčania(double, double, double)
	 */
	public void nastavStredOtáčania(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			xs = bod[0]; ys = bod[1]; zs = bod[2];
			transformuj = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #nastavStredOtáčania(double[]) nastavStredOtáčania}.</p> */
	public void nastavStredOtacania(double[] bod) { nastavStredOtáčania(bod); }

	/**
	 * <p>Posunie súradnice {@linkplain #nastavStredOtáčania(double, double,
	 * double) stredu otáčania}. (Pozri aj uhly {@linkplain #uholAlfa()
	 * alfa}, {@linkplain #uholBeta() beta} a {@linkplain #uholGama()
	 * gama}.)</p>
	 * 
	 * @param Δxs miera zmeny x-ovej súradnice stredu otáčania
	 * @param Δys miera zmeny y-ovej súradnice stredu otáčania
	 * @param Δzs miera zmeny z-ovej súradnice stredu otáčania
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 */
	public void posuňStredOtáčania(double Δxs, double Δys, double Δzs)
	{
		xs += Δxs; ys += Δys; zs += Δzs;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňStredOtáčania(double, double, double) posuňStredOtáčania}.</p> */
	public void posunStredOtacania(double Δxs, double Δys, double Δzs)
	{ posuňStredOtáčania(Δxs, Δys, Δzs); }

	/**
	 * <p>Posunie stred otáčania roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu polohy stredu otáčania roja berúc
	 * do úvahy spôsob práce {@linkplain #smerník smerníka}. Ak je zadané
	 * pole aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod trojica polohových súradníc, o ktoré bude posunutá
	 *     poloha stredu otáčania roja
	 * 
	 * @see #nastavStredOtáčania(double[])
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void posuňStredOtáčania(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			xs += bod[0]; ys += bod[1]; zs += bod[2];
			transformuj = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňStredOtáčania(double[]) posuňStredOtáčania}.</p> */
	public void posunStredOtacania(double[] bod) { posuňStredOtáčania(bod); }


	/**
	 * <p>Vráti aktuálnu polohu {@linkplain #nastavKameru(double, double,
	 * double) kamery} vo forme trojprvkového poľa [kx, ky, kz].</p>
	 * 
	 * @return aktuálna poloha {@linkplain #nastavKameru(double, double,
	 *     double) kamery}
	 * 
	 * @see #nastavKameru(double[])
	 */
	public double[] kamera() { return new double[] {kx, ky, kz}; }

	/**
	 * <p>Vráti aktuálnu x-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota x-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraX()
	{
		return kx;
	}

	/**
	 * <p>Vráti aktuálnu y-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota y-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraY()
	{
		return ky;
	}

	/**
	 * <p>Vráti aktuálnu z-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota z-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraZ()
	{
		return kz;
	}

	/**
	 * <p>Nastaví novú x-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota x-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraX(double hodnota)
	{
		kx = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú y-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota y-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraY(double hodnota)
	{
		ky = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú z-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota z-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraZ(double hodnota)
	{
		kz = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú polohu kamery.</p>
	 * 
	 * @param nkx nová hodnota x-ovej súradnice polohy kamery
	 * @param nky nová hodnota y-ovej súradnice polohy kamery
	 * @param nkz nová hodnota z-ovej súradnice polohy kamery
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void nastavKameru(double nkx, double nky, double nkz)
	{
		kx = nkx; ky = nky; kz = nkz;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví polohu kamery roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie polohy kamery roja berúc do úvahy
	 * spôsob práce {@linkplain #smerník smerníka}. Ak je zadané pole
	 * aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho prvky.
	 * (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu, na ktoré bude nastavená poloha kamery
	 * 
	 * @see #kamera()
	 * @see #nastavKameru(double, double, double)
	 */
	public void nastavKameru(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			kx = bod[0]; ky = bod[1]; kz = bod[2];
			prepočítaj = true;
		}
	}

	/**
	 * <p>Posunie polohu kamery.</p>
	 * 
	 * @param Δkx miera zmeny hodnoty x-ovej súradnice polohy kamery
	 * @param Δky miera zmeny hodnoty y-ovej súradnice polohy kamery
	 * @param Δkz miera zmeny hodnoty z-ovej súradnice polohy kamery
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 */
	public void posuňKameru(double Δkx, double Δky, double Δkz)
	{
		kx += Δkx; ky += Δky; kz += Δkz;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňKameru(double, double, double) posuňKameru}.</p> */
	public void posunKameru(double Δkx, double Δky, double Δkz)
	{ posuňKameru(Δkx, Δky, Δkz); }

	/**
	 * <p>Posunie polohu kamery roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu polohy kamery roja berúc do úvahy
	 * spôsob práce {@linkplain #smerník smerníka}. Ak je zadané pole
	 * aspoň trojprvkové, metóda berie do úvahy prvé tri jeho prvky.
	 * (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu, o ktoré bude posunutá poloha kamery
	 * 
	 * @see #nastavKameru(double[])
	 * @see #posuňKameru(double, double, double)
	 */
	public void posuňKameru(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			kx += bod[0]; ky += bod[1]; kz += bod[2];
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňKameru(double[]) posuňKameru}.</p> */
	public void posunKameru(double[] bod) { posuňKameru(bod); }


	/**
	 * <p>Vráti aktuálnu hodnotu mierky roja. Mierka vo veľkej miere
	 * ovplyvňuje zobrazenie roja. Ak je nesprávne nastavená, výsledok
	 * je skreslený. Dá sa prirovnať k ohniskovej vzdialenosti objektívu.</p>
	 * 
	 * @return aktuálna hodnota mierky zobrazenia roja
	 * 
	 * @see #mierka(double)
	 * @see #zmeňMierku(double)
	 */
	public double mierka()
	{
		return mierka;
	}

	/**
	 * <p>Nastaví novú hodnotu mierky roja. Pozri aj opis metódy {@link 
	 * #mierka() mierka()}.</p>
	 * 
	 * @param hodnota nová hodnota mierky zobrazenia roja
	 * 
	 * @see #mierka()
	 * @see #zmeňMierku(double)
	 */
	public void mierka(double hodnota)
	{
		mierka = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Upraví hodnotu mierky roja. Pozri aj opis metódy {@link 
	 * #mierka() mierka()}.</p>
	 * 
	 * @param Δmierka miera zmeny hodnoty mierky zobrazenia roja
	 * 
	 * @see #mierka()
	 * @see #mierka(double)
	 */
	public void zmeňMierku(double Δmierka)
	{
		mierka += Δmierka;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zmeňMierku(double) zmeňMierku}.</p> */
	public void zmenMierku(double Δmierka)
	{ zmeňMierku(Δmierka); }


	/**
	 * <p>Táto metóda vynúti prepočet konečných (globálne transformovaných)
	 * súradníc {@code x2}, {@code y2}, {@code z2} všetkých bodov roja pri
	 * najbližšom kreslení alebo pri volaní metódy {@link #transformuj()
	 * transformuj}. Volanie tejto metódy zároveň nastaví príznak
	 * prepočítania atribútov použitých pri kreslení (premietaní) bodov
	 * roja. (Pozri metódu: {@link #prepočítať() prepočítať}.)</p>
	 * 
	 * @see #prepočítať()
	 * @see #transformuj()
	 */
	public void transformovať()
	{
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformovať() transformovať}.</p> */
	public void transformovat() { transformovať(); }

	/**
	 * <p>Táto metóda prepočíta hodnoty konečných (globálne transformovaných)
	 * súradníc {@link Roj.Bod#x2 x2}, {@link Roj.Bod#y2 y2}, {@link 
	 * Roj.Bod#z2 z2} všetkých bodov roja. Metóda používa vnútorný príznak
	 * na overenie toho, či je prepočítanie potrebné. Ak chcete prepočítanie
	 * vynútiť, musíte pred volaním tejto metódy zavolať metódu {@link 
	 * #transformovať() transformovať}.</p>
	 * 
	 * @see #transformovať()
	 * @see #prepočítaj()
	 * @see #transformuj(Roj.Bod)
	 */
	public void transformuj()
	{
		if (transformuj)
		{
			prepočítajT();

			for (Bod bod : body)
			{
				bod.transformuj();

				double xx = bod.x1 - xs;
				double yy = bod.y1 - ys;
				double zz = bod.z1 - zs;

				// „Ľavoruký“ (left-handed) systém:
				bod.x2 = xs + (xx * T00) + (yy * T01) + (zz * T02);
				bod.y2 = ys + (xx * T10) + (yy * T11) + (zz * T12);
				bod.z2 = zs + (xx * T20) + (yy * T21) + (zz * T22);

				// „Pravoruký“ (right-handed) systém:
				// bod.x2 = xs + (yy * T00) + (xx * T01) + (zz * T02);
				// bod.y2 = ys + (yy * T10) + (xx * T11) + (zz * T12);
				// bod.z2 = zs + (yy * T20) + (xx * T21) + (zz * T22);
			}

			transformuj = false;
		}
	}

	/**
	 * <p>Toto je metóda, ktorá prepočíta hodnoty konečných (globálne
	 * transformovaných) súradníc {@link Roj.Bod#x2 x2}, {@link 
	 * Roj.Bod#y2 y2}, {@link Roj.Bod#z2 z2} zadaného bodu roja.</p>
	 * 
	 * @param bod inštancia bodu roja, ktorého atribúty majú byť prepočítané
	 * 
	 * @see #transformuj()
	 */
	public void transformuj(Bod bod)
	{
		prepočítajT();
		bod.transformuj();

		double xx = bod.x1 - xs;
		double yy = bod.y1 - ys;
		double zz = bod.z1 - zs;

		// „Ľavoruký“ (left-handed) t. j. ľavotočivý systém:
		bod.x2 = xs + (xx * T00) + (yy * T01) + (zz * T02);
		bod.y2 = ys + (xx * T10) + (yy * T11) + (zz * T12);
		bod.z2 = zs + (xx * T20) + (yy * T21) + (zz * T22);

		// „Pravoruký“ (right-handed) t. j. pravotočivý systém:
		// bod.x2 = xs + (yy * T00) + (xx * T01) + (zz * T02);
		// bod.y2 = ys + (yy * T10) + (xx * T11) + (zz * T12);
		// bod.z2 = zs + (yy * T20) + (xx * T21) + (zz * T22);

		bod.faktor = bod.z2 + kz;
		if (bod.faktor > 0)
		{
			bod.x3 = kx + mierka * bod.x2 / bod.faktor;
			bod.y3 = ky + mierka * bod.y2 / bod.faktor;
			bod.z3 = mierka * bod.rozmer / bod.faktor;
		}

		Collections.sort(poradieKreslenia);
	}

	/**
	 * <p>Táto metóda vynúti prepočet premietaných súradníc ({@link 
	 * Roj.Bod#x3 x3}, {@link Roj.Bod#y3 y3}) a veľkostí kreslených objektov
	 * ({@link Roj.Bod#z3 z3}) všetkých bodov roja pri najbližšom kreslení
	 * alebo pri volaní metódy {@link #prepočítaj() prepočítaj}.</p>
	 * 
	 * @see #transformovať()
	 * @see #prepočítaj()
	 */
	public void prepočítať()
	{
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepočítať() prepočítať}.</p> */
	public void prepocitat() { prepočítať(); }

	/**
	 * <p>Táto metóda prepočíta pre všetky body roja atribúty cieľovej
	 * (premietanej) polohy na plátne a veľkosti objektu kresleného na
	 * polohe bodu – hodnoty atribútov {@link Roj.Bod#x3 x3},
	 * {@link Roj.Bod#y3 y3} a {@link Roj.Bod#z3 z3}. Metóda používa
	 * vnútorný príznak na overenie toho, či je prepočítanie potrebné.
	 * Ak chcete prepočítanie vynútiť, musíte pred volaním tejto metódy
	 * zavolať metódu {@link #prepočítať() prepočítať}.</p>
	 * 
	 * @see #prepočítať()
	 * @see #transformuj()
	 */
	public void prepočítaj()
	{
		if (prepočítaj)
		{
			for (Bod bod : body)
			{
				bod.faktor = bod.z2 + kz;
				if (bod.faktor > 0)
				{
					bod.x3 = kx + mierka * bod.x2 / bod.faktor;
					bod.y3 = ky + mierka * bod.y2 / bod.faktor;
					bod.z3 = mierka * bod.rozmer / bod.faktor;
				}
			}

			Collections.sort(poradieKreslenia);
			prepočítaj = false;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepočítaj() prepočítaj}.</p> */
	public void prepocitaj() { prepočítaj(); }


	/**
	 * <p>Prekreslí roj s použitím {@linkplain #kresli kresliaceho robota
	 * roja}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Pred kreslením roja sú zálohované
	 * iba dve vlastnosti: {@linkplain GRobot#farba(Color) farba}
	 * a {@linkplain GRobot#hrúbkaČiary(double) hrúbka čiary} robota. Ich
	 * hodnoty sú obnovené až po nakreslení všetkých prvkov roja, čiže ak
	 * zákaznícke kreslenie objektu (telesa) v niektorom z bodov roja tieto
	 * vlastnosti zmenilo (a neobnovilo), tak sa zmeny prenesú do kreslenia
	 * ďalších prvkov roja…</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Počas kreslenia je aktualizovaná
	 * inštancia roja {@link Roj#bod bod}, ktorá sa dá využiť pri zákaznícky
	 * definovanom kreslení bodov roja (pozri {@link Roj.Bod#kreslenie
	 * kreslenie}).</p>
	 */
	public void kresli()
	{
		transformuj();
		prepočítaj();
		neplatnáSúradnica = true;

		Farba f = kresli.farba();
		double hč = kresli.hrúbkaČiary();

		if (!body.isEmpty())
		{
			Bod bod = body.get(0);
			kresli.smer(90);
			kresli.skočNa(bod.x3, bod.y3);
		}

		for (Bod bod : body)
		{
			this.bod = bod;
			bod.kresliSpoj();
		}

		for (Bod bod : poradieKreslenia)
		{
			this.bod = bod;
			bod.kresliTeleso();
		}

		kresli.hrúbkaČiary(hč);
		kresli.farba(f);
	}


	/**
	 * <p>Táto metóda slúži na export grafiky roja do zadanej inštancie
	 * {@linkplain SVGPodpora SVG podpory}. Táto metóda automaticky spúšťa
	 * metódy {@link Roj.Bod#spojDoSVG(SVGPodpora) spojDoSVG}
	 * a {@link Roj.Bod#telesoDoSVG(SVGPodpora) telesoDoSVG} pre
	 * jednotlivé body roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Počas exportu je aktualizovaná
	 * inštancia roja {@link Roj#bod bod}, ktorá sa dá využiť pri zákaznícky
	 * definovanom exporte bodov roja (pozri {@link Roj.Bod#svgKreslenie
	 * svgKreslenie}).</p>
	 * 
	 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
	 *     do ktorej budú exportované {@linkplain Shape tvary Javy}
	 *     reprezentujúce všetky viditeľné spoje medzi bodmi roja a telesá
	 *     umiestnené v bodoch roja
	 * 
	 * @see Roj.Bod#spojDoSVG(SVGPodpora)
	 * @see Roj.Bod#telesoDoSVG(SVGPodpora)
	 */
	public void pridajDoSVG(SVGPodpora svgPodpora)
	{
		transformuj();
		prepočítaj();
		neplatnáSúradnica = true;

		if (!body.isEmpty())
		{
			Bod bod = body.get(0);
			kresli.smer(90);
			kresli.skočNa(bod.x3, bod.y3);
		}

		for (Bod bod : body)
		{
			this.bod = bod;
			bod.spojDoSVG(svgPodpora);
		}

		for (Bod bod : poradieKreslenia)
		{
			this.bod = bod;
			bod.telesoDoSVG(svgPodpora);
		}
	}


	/**
	 * <p>Vypočíta zo zadaných súradníc troch bodov určujúcich plochu
	 * v priestore súradnice normálového vektora k tejto ploche a vráti
	 * jeho <i><b>ne</b></i>normalizované súradnice.</p>
	 * 
	 * <p>Inak povedané, metóda vypočíta súradnice smerového vektora kolmice
	 * k ploche, tento vektor však <i><b>ne</b></i>prepočíta na jednotkový,
	 * ale ho vráti v (kvázi) „surovom“ stave. (Na väčšinu účelov by mal
	 * postačovať aj nenormalizovaný vektor. V prípade potreby použite metódu
	 * {@link #normalizuj(double[]) normalizuj}.)</p>
	 * 
	 * @param x1 x-ová súradnica prvého bodu
	 * @param y1 y-ová súradnica prvého bodu
	 * @param z1 z-ová súradnica prvého bodu
	 * @param x2 x-ová súradnica druhého bodu
	 * @param y2 y-ová súradnica druhého bodu
	 * @param z2 z-ová súradnica druhého bodu
	 * @param x3 x-ová súradnica tretieho bodu
	 * @param y3 y-ová súradnica tretieho bodu
	 * @param z3 z-ová súradnica tretieho bodu
	 * @return trojprvkové pole so súradnicami normálového vektora plochy
	 *     (určenej tromi bodmi v parametroch)
	 */
	public static double[] normála(double x1, double y1, double z1,
		double x2, double y2, double z2, double x3, double y3, double z3)
	{
		// Zdroje:
		// 
		// https://www.ma.utexas.edu/users/m408m/Display12-5-4.shtml
		// https://en.wikipedia.org/wiki/Cross_product
		// (https://math.stackexchange.com/questions/137538/calculate-the-vector-normal-to-the-plane-by-given-points)
		// 
		double Δx1 = x2 - x1;
		double Δy1 = y2 - y1;
		double Δz1 = z2 - z1;
		double Δx2 = x3 - x2;
		double Δy2 = y3 - y2;
		double Δz2 = z3 - z2;

		double nx = Δy1 * Δz2 - Δz1 * Δy2;
		double ny = Δz1 * Δx2 - Δx1 * Δz2;
		double nz = Δx1 * Δy2 - Δy1 * Δx2;

		return new double[] {nx, ny, nz};
	}

	/** <p><a class="alias"></a> Alias pre {@link #normála(double, double, double, double, double, double, double, double, double) normála}.</p> */
	public static double[] normala(double x1, double y1, double z1,
		double x2, double y2, double z2, double x3, double y3, double z3)
	{ return normála(x1, y1, z1, x2, y2, z2, x3, y3, z3); }


	/**
	 * <p>Normalizuje vektor zadaných súradníc. Táto metóda pracuje
	 * s ľubovoľne veľkým vektorom a normalizuje všetky súradnice podľa
	 * dĺžky vektora. To znamená, že ak má vektor šesť súradníc
	 * (pozri {@link Smerník#dajStav() smerník.dajStav()}), tak metóda
	 * vzájomne normalizuje všetkých šesť súradníc dohromady (akoby išlo
	 * napríklad o šesťrozmerný bod)! Návratovou hodnotou je rovnaké
	 * pole, ktoré bolo zadané vo vstupnom parametri {@code vektor}, len
	 * s prepočítanými súradnicami. (To znamená, že všetky zmeny sa
	 * rovnako prejavia aj vo vstupnom poli – ide o rovnakú inštanciu
	 * poľa.)</p>
	 * 
	 * @param vektor vektor súradníc určených na normalizáciu
	 * @return vektor s normalizovanými súradnicami
	 */
	public static double[] normalizuj(double[] vektor)
	{
		if (null != vektor && 1 < vektor.length)
		{
			double Î = 0.0;
			for (int i = 0; i < vektor.length; ++i)
				Î += vektor[i] * vektor[i];
			Î = sqrt(Î);
			for (int i = 0; i < vektor.length; ++i)
				vektor[i] /= Î;
		}
		return vektor;
	}


	/**
	 * <p>Vráti uhly potrebné na pootočenie jednotkového vektora paralelného
	 * s osou z okolo osí x (prvý prvok poľa v návratovej hodnote označovaný
	 * v tejto dokomentácii aj ako uhol α) a y (druhý prvok poľa v návratovej
	 * hodnote označovaný v tejto dokomentácii aj ako uhol β) tak, aby
	 * výsledný vektor smeroval k zadanému polohovému vektoru.</p>
	 * 
	 * @param x x-ová súradnica polohového vektora
	 * @param y y-ová súradnica polohového vektora
	 * @param z z-ová súradnica polohového vektora
	 * @return dvojprvkové pole s požadovanými uhlami
	 */
	public static double[] uhlyK(double x, double y, double z)
	{
		// Zdroje: Pozri ‚„Malý“ omyl…‘ v komentároch niekde vyššie.
		return new double[] { -toDegrees(atan2(y, z)),
			toDegrees(atan2(x, hypot(y, z))) };
	}

	/**
	 * <p>Vráti uhly potrebné na pootočenie jednotkového vektora paralelného
	 * s osou z okolo osí x (prvý prvok poľa v návratovej hodnote označovaný
	 * v tejto dokomentácii aj ako uhol α) a y (druhý prvok poľa v návratovej
	 * hodnote označovaný v tejto dokomentácii aj ako uhol β) tak, aby
	 * výsledný vektor smeroval k zadanému polohovému vektoru. Ak zadaný
	 * vektor neobsahuje aspoň tri prvky, tak metóda vráti hodnotu {@code 
	 * valnull}.</p>
	 * 
	 * @param vektor polohový vektor súradníc
	 * @return dvojprvkové pole s požadovanými uhlami alebo {@code valnull}
	 */
	public static double[] uhlyK(double[] vektor)
	{
		// Zdroje: Pozri ‚„Malý“ omyl…‘ v komentároch niekde vyššie.
		if (null != vektor && vektor.length >= 3)
			return new double[]
				{
					-toDegrees(atan2(vektor[1], vektor[2])),
					toDegrees(atan2(vektor[0], hypot(vektor[1], vektor[2])))
				};
		return null;
	}


	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na zadaných
	 * súradniciach. Metóda využíva metódu bodu roja:
	 * {@link Bod#bodV(double, double, double) bodV}. Bod, ktorý vyhovie
	 * podmienke je vrátený. Ak nie je nájdený žiadny bod, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param súradnicaX x-ová súradnica bodu
	 * @param súradnicaY y-ová súradnica bodu
	 * @param polomer polomer vyšetrovaného kruhu (pozri {@link 
	 *     Bod#bodV(double, double, double) bodV})
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNa(double súradnicaX, double súradnicaY,
		double polomer)
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.bodV(súradnicaX, súradnicaY, polomer)) return bod;
		return null;
	}

	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na
	 * súradniciach zadaného objektu. Metóda využíva metódu bodu roja:
	 * {@link Bod#bodV(Poloha, double) bodV}. Bod, ktorý vyhovie
	 * podmienke je vrátený. Ak nie je nájdený žiadny bod, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param objekt objekt, ktorého poloha je použitá na overenie bodov
	 * @param polomer polomer vyšetrovaného kruhu (pozri {@link 
	 *     Bod#bodV(Poloha, double) bodV})
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNa(Poloha objekt, double polomer)
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.bodV(objekt, polomer)) return bod;
		return null;
	}

	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na zadaných
	 * súradniciach. Metóda využíva metódu bodu roja: {@link Bod#bodV(double,
	 * double) bodV}. Bod, ktorý vyhovie podmienke je vrátený. Ak nie je
	 * nájdený žiadny bod, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @param súradnicaX x-ová súradnica bodu
	 * @param súradnicaY y-ová súradnica bodu
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNa(double súradnicaX, double súradnicaY)
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.bodV(súradnicaX, súradnicaY)) return bod;
		return null;
	}

	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na
	 * súradniciach zadaného objektu. Metóda využíva metódu bodu roja:
	 * {@link Bod#bodV(Poloha) bodV}. Bod, ktorý vyhovie
	 * podmienke je vrátený. Ak nie je nájdený žiadny bod, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param objekt objekt, ktorého poloha je použitá na overenie bodov
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNa(Poloha objekt)
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.bodV(objekt)) return bod;
		return null;
	}


	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na
	 * súradniciach myši. Metóda využíva metódu bodu roja: {@link 
	 * Bod#myšV(double) myšV}. Bod, ktorý vyhovie podmienke je vrátený.
	 * Ak nie je nájdený žiadny bod, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @param polomer polomer vyšetrovaného kruhu (pozri {@link 
	 *     Bod#myšV(double) myšV})
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNaMyši(double polomer)
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.myšV(polomer)) return bod;
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajBodNaMyši(double) dajBodNaMyši}.</p> */
	public Bod dajBodNaMysi(double polomer) { return dajBodNaMyši(polomer); }

	/**
	 * <p>Zistí, či sa projekcia niektorého bodu roja nachádza na
	 * súradniciach myši. Metóda využíva metódu bodu roja: {@link Bod#myšV()
	 * myšV}. Bod, ktorý vyhovie podmienke je vrátený. Ak nie je nájdený
	 * žiadny bod, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return nájdený bod alebo {@code valnull}
	 */
	public Bod dajBodNaMyši()
	{
		for (Bod bod : poradieKreslenia.odzadu())
			if (bod.myšV()) return bod;
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajBodNaMyši() dajBodNaMyši}.</p> */
	public Bod dajBodNaMysi() { return dajBodNaMyši(); }


	/**
	* <p>Prečíta údaje o roji z konfiguračného súboru otvoreného na čítanie.
	 * Metóda prijíma identifikátor menného priestoru. Identifikátor smie mať
	 * hodnotu {@code valnull}. V takom prípade sú údaje čítané z aktuálneho
	 * menného priestoru zadaného konfiguračného súboru.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
	 *     na čítanie
	 * @param identifikátor vnorený menný priestor, z ktorého budú
	 *     prevzaté údaje o roji
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void čítajZoSúboru(Súbor súbor, String identifikátor)
		throws IOException
	{
		súbor.vnorMennýPriestorVlastností(identifikátor);

		try
		{
			vymaž();

			double[] predvolené = null;
			double[] prečítané;

			prečítané = súbor.čítajVlastnosť("uhly", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				α = prečítané[0];
				β = prečítané[1];
				γ = prečítané[2];
			}

			prečítané = súbor.čítajVlastnosť("stred", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				xs = prečítané[0];
				ys = prečítané[1];
				zs = prečítané[2];
			}

			prečítané = súbor.čítajVlastnosť("kamera", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				kx = prečítané[0];
				ky = prečítané[1];
				kz = prečítané[2];
			}

			súbor.čítajVlastnosť("mierka", mierka);

			int početBodov = súbor.čítajVlastnosť("početBodov", 0);
			for (int i = 0; i < početBodov; ++i)
			{
				Bod bod = new Bod();
				body.pridaj(bod);
				poradieKreslenia.pridaj(bod);
				bod.čítajZoSúboru(súbor, "bod[" + i + "]");
			}

			transformuj = true;
			prepočítaj  = true;
			prepočítajT = true;
		}
		finally
		{
			súbor.vynorMennýPriestorVlastností();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void citajZoSuboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void prečítajZoSúboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void precitajZoSuboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/**
	 * <p>Zapíše údaje o roji do konfiguračného súboru otvoreného na zápis.
	 * Metóda prijíma identifikátor menného priestoru. Identifikátor smie mať
	 * hodnotu {@code valnull}. V takom prípade sú údaje ukladané do
	 * aktuálneho menného priestoru zadaného konfiguračného súboru.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
	 *     na zápis
	 * @param identifikátor vnorený menný priestor, do ktorého budú
	 *     vložené údaje o roji
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void uložDoSúboru(Súbor súbor, String identifikátor)
		throws IOException
	{
		súbor.vnorMennýPriestorVlastností(identifikátor);

		try
		{
			súbor.zapíšPrázdnyRiadokVlastností();
			súbor.zapíšVlastnosť("uhly", new double[] {α, β, γ});
			súbor.zapíšVlastnosť("stred", new double[] {xs, ys, zs});
			súbor.zapíšVlastnosť("kamera", new double[] {kx, ky, kz});
			súbor.zapíšVlastnosť("mierka", mierka);

			int početBodov = body.veľkosť();
			súbor.zapíšPrázdnyRiadokVlastností();
			súbor.zapíšVlastnosť("početBodov", početBodov);

			for (int i = 0; i < početBodov; ++i)
			{
				súbor.zapíšPrázdnyRiadokVlastností();
				body.daj(i).uložDoSúboru(súbor, "bod[" + i + "]");
			}
		}
		finally
		{
			súbor.vynorMennýPriestorVlastností();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor, String) uložDoSúboru}.</p> */
	public void ulozDoSuboru(Súbor súbor, String identifikátor)
	throws IOException { uložDoSúboru(súbor, identifikátor); }
}
