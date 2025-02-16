
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2025 by Roman Horváth
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

import java.awt.Image;
import java.awt.Shape;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

// ----------------------- //
//  *** Trieda Oblasť ***  //
// ----------------------- //

/**
 * <p>Trieda je rozšírením triedy Javy {@link Area Area}. Oblasť je
 * ľubovoľná plocha, ktorá môže byť napríklad obkreslená čiarou,
 * vyplnená farbou, použitá na {@linkplain GRobot#nekresliDo(Shape)
 * obmedzenie kreslenia}, prípadne na ďalšie účely. Na tvorbe oblasti sa
 * môže podieľať zvolený robot alebo ju môžeme vytvoriť pomocou útvarov
 * ({@link Shape Shape}) Javy. Každý robot je schopný produkovať útvary,
 * ktoré môžu byť využité na vytvorenie oblasti. Filozofia je taká, že
 * zavoláme metódu robota na nakreslenie útvaru alebo výpis textu (ktoré
 * zároveň produkujú útvary) a vyprodukovaný útvar (s jeho veľkosťou
 * a orientáciou) {@linkplain #pridaj(Shape) pridáme} či {@linkplain 
 * #odober(Shape) odoberieme} od tvorenej oblasti, vytvoríme
 * {@linkplain #prienik(Shape) prienik} alebo {@linkplain 
 * #alternatíva(Shape) alternatívu} s vytváranou oblasťou. Počas
 * procesu tvorby oblasti by však bolo nežiaduce (prípadne
 * nemožné<sup>[1]</sup>), aby robot súčasne produkované útvary
 * kreslil<sup>[2]</sup>. Preto je dobré ho {@linkplain 
 * #zamestnaj(GRobot) zamestnať} na účely tvorby oblasti. Zamestnaný
 * robot nekreslí útvary ani nepíše texty dovtedy, kým nie je zo služby
 * {@linkplain #uvoľni(GRobot) uvoľnený} ({@linkplain #prepusti(GRobot)
 * prepustený}).</p>
 * 
 * <p><small>[1] – to sa dotýka útvarov vytvorených
 * z textu.</small><br />
 * <small>[2] – nemali by sme možnosť zabrániť
 * nakresleniu produkovaného útvaru – zdvihnutie pera by nepomohlo,
 * pretože jeho poloha nemá na kreslenie útvarov vplyv.</small></p>
 * 
 * <p>Na tvorbu oblasti je možné použiť tieto metódy robota:
 * {@link GRobot#krúžok(double polomer) krúžok(polomer)},
 * {@link GRobot#kružnica(double polomer) kružnica(polomer)},
 * {@link GRobot#kruh(double polomer) kruh(polomer)},
 * {@link GRobot#elipsa(double a, double b) elipsa(a, b)},
 * {@link GRobot#kresliElipsu(double a, double b) kresliElipsu(a, b)},
 * {@link GRobot#vyplňElipsu(double a, double b) vyplňElipsu(a, b)},
 * {@link GRobot#štvorec(double polomer) štvorec(polomer)},
 * {@link GRobot#kresliŠtvorec(double polomer) kresliŠtvorec(polomer)},
 * {@link GRobot#vyplňŠtvorec(double polomer) vyplňŠtvorec(polomer)},
 * {@link GRobot#obdĺžnik(double a, double b) obdĺžnik(a, b)},
 * {@link GRobot#kresliObdĺžnik(double a, double b) kresliObdĺžnik(a, b)},
 * {@link GRobot#vyplňObdĺžnik(double a, double b) vyplňObdĺžnik(a, b)},
 * {@link GRobot#hviezda(double polomer) hviezda(polomer)},
 * {@link GRobot#kresliHviezdu(double polomer) kresliHviezdu(polomer)},
 * {@link GRobot#vyplňHviezdu(double polomer) vyplňHviezdu(polomer)},
 * {@link GRobot#cesta() cesta()},
 * {@link GRobot#text(String) text(text)}
 * a {@link GRobot#text(String, int) text(text, spôsobKreslenia)}.</p>
 * 
 * <p>Každá z nich môže byť argumentom metód {@link #pridaj(Shape) pridaj},
 * {@link #odober(Shape) odober}, {@link #prienik(Shape) prienik}
 * a {@link #alternatíva(Shape) alternatíva}.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>V tomto príklade sú oblasti použité na nakreslenie šrafovaného
 * obrázka. Oblasti tu slúžia na vymedzenie plochy, do ktorej môže
 * robot kresliť, vďaka čomu bude výsledkom kreslenia šrafov cez celú
 * plochu plátna vyšrafovanie iba vymedzenej plochy.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} ŠrafovanýObrázokZOblastí {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Konštruktor hlavného robota.}
		{@code kwdprivate} ŠrafovanýObrázokZOblastí()
		{
			{@code comm// Volaním nadradeného konštruktora zmeníme romery plátna}
			{@code comm// (volanie nadradeného konštruktora musí byť prvým príkazom}
			{@code comm// každého nového konštruktora):}
			{@code valsuper}({@code num600}, {@code num400});

			{@code comm// Pred začatím kreslenia vypneme automatické prekresľovanie,}
			{@code comm// aby bol celý proces rýchlejší:}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Najskôr zdvihneme pero a vypneme kreslenie tvarov robota,…}
			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#nekresliTvary() nekresliTvary}();

			{@code comm// …aby sme mohli nerušene vygenerovať predlohy všetkých oblastí,}
			{@code comm// ktoré budeme používať pri kreslení šrafovaného obrázka:}
			{@link Oblasť Oblasť} koruna1 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(pootočenáElipsa(-{@code num100}, {@code num80}, {@code num0}, {@code num50}, {@code num100}));
			koruna1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num70}, {@code num70}, {@code num0}, {@code num30}, {@code num80}));
			koruna1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num140}, {@code num60}, {@code num0}, {@code num30}, {@code num60}));
			{@link Oblasť Oblasť} kmeň1 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(kmeň(-{@code num100}, -{@code num70}, {@code num0}, {@code num40}, {@code num140}));
			kmeň1.{@link #odober(Shape) odober}(koruna1);

			{@link Oblasť Oblasť} koruna2 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(pootočenáElipsa({@code num140}, {@code num70}, {@code num0}, {@code num40}, {@code num80}));
			koruna2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num110}, {@code num80}, {@code num0}, {@code num30}, {@code num60}));
			koruna2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num180}, {@code num60}, {@code num0}, {@code num30}, {@code num50}));
			{@link Oblasť Oblasť} kmeň2 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(kmeň({@code num140}, -{@code num60}, {@code num0}, {@code num30}, {@code num120}));
			kmeň2.{@link #odober(Shape) odober}(koruna2);

			{@link Oblasť Oblasť} krík1 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(pootočenáElipsa({@code num25}, -{@code num50}, {@code num0}, {@code num80}, {@code num50}));
			krík1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num5}, -{@code num20}, {@code num10}, {@code num35}, {@code num30}));
			krík1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num55}, -{@code num20}, -{@code num10}, {@code num35}, {@code num30}));
			krík1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num35}, -{@code num70} , {@code num10}, {@code num35}, {@code num30}));
			krík1.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num75}, -{@code num70} , -{@code num10}, {@code num35}, {@code num30}));

			{@link Oblasť Oblasť} krík2 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(pootočenáElipsa({@code num275}, -{@code num60} , {@code num0}, {@code num75}, {@code num45}));
			krík2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num245}, -{@code num30} , {@code num10}, {@code num30}, {@code num25}));
			krík2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num305}, -{@code num30} , -{@code num10}, {@code num30}, {@code num25}));
			krík2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num215}, -{@code num80} , {@code num10}, {@code num30}, {@code num25}));
			krík2.{@link #pridaj(Shape) pridaj}(pootočenáElipsa({@code num325}, -{@code num80} , -{@code num10}, {@code num30}, {@code num25}));

			{@link Oblasť Oblasť} krík3 = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(pootočenáElipsa(-{@code num265}, -{@code num80} , {@code num0}, {@code num75}, {@code num45}));
			krík3.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num235}, -{@code num50} , {@code num10}, {@code num30}, {@code num25}));
			krík3.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num295}, -{@code num50} , -{@code num10}, {@code num30}, {@code num25}));
			krík3.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num205}, -{@code num100} , {@code num10}, {@code num30}, {@code num25}));
			krík3.{@link #pridaj(Shape) pridaj}(pootočenáElipsa(-{@code num315}, -{@code num100} , -{@code num10}, {@code num30}, {@code num25}));

			{@link Oblasť Oblasť} horizont = horizont();
			{@link Oblasť Oblasť} slnko = kružnicaNa({@code num280}, {@code num200}, {@code num80});
			{@link Oblasť Oblasť} oblak = oblak();
			<hr/>
			{@code comm// (Všimnite si, že od oblasti zeme a oblohy musíme odčítať všetky}
			{@code comm// také oblasti, ktoré ich prekrývajú, inak by šrafovanie zasiahlo}
			{@code comm// aj do nich.)}
			{@link Oblasť Oblasť} zem = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(horizont);
			zem.{@link #odober(Shape) odober}(koruna1);
			zem.{@link #odober(Shape) odober}(kmeň1);
			zem.{@link #odober(Shape) odober}(koruna2);
			zem.{@link #odober(Shape) odober}(kmeň2);
			zem.{@link #odober(Shape) odober}(krík1);
			zem.{@link #odober(Shape) odober}(krík2);
			zem.{@link #odober(Shape) odober}(krík3);

			{@code comm// Počiatočný tvar oblasti oblohy získame odčítaním oblasti}
			{@code comm// horizontu od obdĺžnika prekrývajúceho celé plátno (zasahujúc}
			{@code comm// mierne za jeho hranice):}
			{@link GRobot#domov() domov}();
			{@link Oblasť Oblasť} obloha = {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}(obdĺžnik({@code num310}, {@code num210}));
			obloha.{@link #odober(Shape) odober}(horizont);

			{@code comm// (Potom pokračujeme odčítavaním oblastí tvarov, ktoré oblohu}
			{@code comm// prekrývajú – ako je napísané vyššie.)}
			obloha.{@link #odober(Shape) odober}(koruna1);
			obloha.{@link #odober(Shape) odober}(kmeň1);
			obloha.{@link #odober(Shape) odober}(koruna2);
			obloha.{@link #odober(Shape) odober}(kmeň2);
			obloha.{@link #odober(Shape) odober}(krík1);
			obloha.{@link #odober(Shape) odober}(krík2);
			obloha.{@link #odober(Shape) odober}(krík3);
			obloha.{@link #odober(Shape) odober}(slnko);
			obloha.{@link #odober(Shape) odober}(oblak);
			<hr/>
			{@code comm// Pred šrafovaním položíme pero:}
			{@link GRobot#položPero() položPero}();
			<hr/>
			{@code comm// A vyšrafujeme a obkreslíme jednotlivé oblasti:}
			{@link GRobot#kresliDo(Shape) kresliDo}(koruna1);
			šrafovanie3();
			{@link GRobot#kresliVšade() kresliVšade}();
			koruna1.{@link #kresli(GRobot) kresli}({@code valthis}); {@code comm// <-- toto znamená, že koruna1 bude}
			                      {@code comm// nakreslená „týmto“ (this), to jest}
			                      {@code comm// aktuálnym robotom}

			{@link GRobot#kresliDo(Shape) kresliDo}(kmeň1);
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			kmeň1.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(koruna2);
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			koruna2.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(kmeň2);
			šrafovanie3();
			{@link GRobot#kresliVšade() kresliVšade}();
			kmeň2.{@link #kresli(GRobot) kresli}({@code valthis});
			<hr/>
			{@link GRobot#kresliDo(Shape) kresliDo}(krík1);
			šrafovanie3();
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			krík1.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(krík2);
			šrafovanie3();
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			krík2.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(krík3);
			šrafovanie3();
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			krík3.{@link #kresli(GRobot) kresli}({@code valthis});
			<hr/>
			{@link GRobot#kresliDo(Shape) kresliDo}(zem);
			šrafovanie1();
			{@link GRobot#kresliVšade() kresliVšade}();
			zem.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(obloha);
			šrafovanie2();
			{@link GRobot#kresliVšade() kresliVšade}();
			obloha.{@link #kresli(GRobot) kresli}({@code valthis});
			<hr/>
			{@link GRobot#kresliDo(Shape) kresliDo}(slnko);
			šrafovanie3();
			{@link GRobot#kresliVšade() kresliVšade}();
			slnko.{@link #kresli(GRobot) kresli}({@code valthis});

			{@link GRobot#kresliDo(Shape) kresliDo}(oblak);
			šrafovanie4();
			{@link GRobot#kresliVšade() kresliVšade}();
			oblak.{@link #kresli(GRobot) kresli}({@code valthis});
			<hr/>
			{@code comm// Obrázok je hotový, spätne zapneme prekresľovanie:}
			{@link Svet Svet}.{@link Svet#kresli() kresli}();
		}
		<hr/>
		{@code comm// Šrafovanie vo vertikálnom (zvislom) smere.}
		{@code comm// (Implementácia sa spolieha na presné rozmery plátna: 600 × 400 bodov.)}
		{@code kwdprivate} {@code typevoid} šrafovanie1()
		{
			{@link GRobot#smer(double) smer}({@code num90});
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num41}; ++i)
			{
				{@link GRobot#skočNa(double, double) skočNa}(-{@code num300} + i * {@code num15}, -{@code num200});
				{@link GRobot#dopredu(double) dopredu}({@code num400});
			}
		}

		{@code comm// Šrafovanie v horizontálnom (vodorovnom) smere.}
		{@code comm// (Implementácia sa spolieha na presné rozmery plátna: 600 × 400 bodov.)}
		{@code kwdprivate} {@code typevoid} šrafovanie2()
		{
			{@link GRobot#smer(double) smer}({@code num0});
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num28}; ++i)
			{
				{@link GRobot#skočNa(double, double) skočNa}(-{@code num300}, -{@code num200} + i * {@code num15});
				{@link GRobot#dopredu(double) dopredu}({@code num600});
			}
		}

		{@code comm// Šrafovanie v diagonálnom smere 45° (zospodu zľava hore doprava).}
		{@code comm// (Implementácia sa spolieha na presné rozmery plátna: 600 × 400 bodov.)}
		{@code kwdprivate} {@code typevoid} šrafovanie3()
		{
			{@link GRobot#smer(double) smer}({@code num45});
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num68}; ++i)
			{
				{@link GRobot#skočNa(double, double) skočNa}({@code num300} &#45; i * {@code num15}, -{@code num200});
				{@link GRobot#dopredu(double) dopredu}({@code num580});
			}
		}

		{@code comm// Šrafovanie v diagonálnom smere 135° (zospodu sprava hore doľava).}
		{@code comm// (Implementácia sa spolieha na presné rozmery plátna: 600 × 400 bodov.)}
		{@code kwdprivate} {@code typevoid} šrafovanie4()
		{
			{@link GRobot#smer(double) smer}({@code num135});
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num68}; ++i)
			{
				{@link GRobot#skočNa(double, double) skočNa}(-{@code num300} + i * {@code num15}, -{@code num200});
				{@link GRobot#dopredu(double) dopredu}({@code num580});
			}
		}
		<hr/>
		{@code comm// Nasledujúca metóda slúži na vygenerovanie oblasti v tvare pootočenej}
		{@code comm// elipsy (so stanovením jej rozmerov).}
		{@code comm//   sx – x-ová súradnica stredu elipsy}
		{@code comm//   sy – y-ová súradnica stredu elipsy}
		{@code comm//   α  – uhol pootočenia elipsy}
		{@code comm//   a  – veľkosť hlavnej poloosi elipsy}
		{@code comm//   b  – veľkosť vedľajšej poloosi elipsy}
		{@code kwdprivate} Oblasť pootočenáElipsa(
			{@code typedouble} sx, {@code typedouble} sy, {@code typedouble} α, {@code typedouble} a, {@code typedouble} b)
		{
			{@link GRobot#skočNa(double, double) skočNa}(sx, sy);
			{@link GRobot#smer(double) smer}({@code num90} + α);
			{@code kwdreturn} {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}({@link GRobot#elipsa(double, double) elipsa}(a, b));
		}

		{@code comm// Nasledujúca metóda slúži na vygenerovanie oblasti, ktorú ohraničuje}
		{@code comm// kružnica s určením jej stredu (a samozrejme i polomeru).}
		{@code comm//   sx – x-ová súradnica stredu kružnice}
		{@code comm//   sy – y-ová súradnica stredu kružnice}
		{@code comm//   r  – polomer kružnice}
		{@code kwdprivate} {@link Oblasť Oblasť} kružnicaNa({@code typedouble} sx, {@code typedouble} sy, {@code typedouble} r)
		{
			{@link GRobot#skočNa(double, double) skočNa}(sx, sy);
			{@code kwdreturn} {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}({@link GRobot#kružnica(double) kružnica}(r));
		}
		<hr/>
		{@code comm// Nasledujúca metóda slúži na vygenerovanie tvaru nazvaného kmeň. Ide}
		{@code comm// o tvar získaný odrezaním plôšok z dvoch protiľahlých strán obdĺžnika,}
		{@code comm// pričom plôšky sú určené oblúčikmi tak, že v podstate vymedzujú}
		{@code comm// kruhové odseky. Tento tvar dokážeme získať rôznymi spôsobmi, ale}
		{@code comm// v tomto príklade ho vytvárame z cesty (čiže akoby zo záznamu dráhy,}
		{@code comm// ktorú robot prechádza), pričom okrem priamočiareho kráčania}
		{@code comm// použijeme aj príkazy robota na kráčanie po oblúku.}
		{@code comm//   sx – x-ová súradnica stredu kmeňa}
		{@code comm//   sy – y-ová súradnica stredu kmeňa}
		{@code comm//   α  – pootočenie tvaru kmeňa}
		{@code comm//   š  – šírka kmeňa}
		{@code comm//   v  – výška kmeňa}
		{@code kwdprivate} {@link Oblasť Oblasť} kmeň({@code typedouble} sx, {@code typedouble} sy, {@code typedouble} α, {@code typedouble} š, {@code typedouble} v)
		{
			{@code comm// Najprv treba vypočítať/získať súradnice rohov obdĺžnika}
			{@code comm// ohraničujúceho tvar kmeňa.}

			{@code comm// Prvým krokom je presun na súradnice stredu, pootočenie}
			{@code comm// a potom presun na súradnice ľavého dolného rohu kmeňa:}
			{@link GRobot#skočNa(double, double) skočNa}(sx, sy);
			{@link GRobot#smer(double) smer}({@code num90} + α);
			{@link GRobot#dozadu(double) dozadu}(v / {@code num2.0});
			{@link GRobot#posuňDoľava(double) posuňDoľava}(š / {@code num2.0});

			{@code comm// Potom pokračujeme postupným zapamätávaním aktuálnych súradníc}
			{@code comm// a pochodovanie po ohraničujúcom obdĺžniku, čím získavame súradnice}
			{@code comm// rohov obdĺžnika:}
			{@link Bod Bod} vľavoDole = {@link GRobot#poloha() poloha}();
			{@link GRobot#dopredu(double) dopredu}(v);
			{@link Bod Bod} vľavoHore = {@link GRobot#poloha() poloha}();
			{@link GRobot#vpravo(double) vpravo}({@code num90});
			{@link GRobot#dopredu(double) dopredu}(š);
			{@link Bod Bod} vpravoHore = {@link GRobot#poloha() poloha}();
			{@link GRobot#vpravo(double) vpravo}({@code num90});
			{@link GRobot#dopredu(double) dopredu}(v);
			{@link Bod Bod} vpravoDole = {@link GRobot#poloha() poloha}();

			{@code comm// Teraz môžeme začať kresliť tvar kmeňa. Najprv sa presunieme}
			{@code comm// do ľavého spodného rohu a pootočíme robot o 10° doprava vzhľadom}
			{@code comm// na počiatočné pootočenie, čiže presne na uhol 80° + α:}
			{@link GRobot#skočNa(double, double) skočNa}(vľavoDole);
			{@link GRobot#smer(double) smer}({@code num80} + α);

			{@code comm// Zahájime záznam cesty a postupne sa rôznymi spôsobmi posúvame do}
			{@code comm// ďalších rohov ohraničujúceho obdĺžnika tak, aby sme získali}
			{@code comm// požadovaný tvar:}
			{@link GRobot#začniCestu() začniCestu}();
			{@link GRobot#choďNaPoOblúku(Poloha) choďNaPoOblúku}(vľavoHore);
			{@link GRobot#choďNa(double, double) choďNa}(vpravoHore);
			{@link GRobot#smer(double) smer}({@code num260} + α);
			{@link GRobot#choďNaPoOblúku(Poloha) choďNaPoOblúku}(vpravoDole);

			{@code comm// Nakoniec cestu zavrieme a výsledný tvar vrátime:}
			{@link GRobot#uzavriCestu() uzavriCestu}();
			{@code kwdreturn} {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}({@link GRobot#cesta() cesta}());
		}
		<hr/>
		{@code comm// Tvar horizontu je vytvorený napevno ako obdĺžnik, ktorý je o niečo}
		{@code comm// širší ako šírka plátna, je tiež posunutý mierne pod úroveň rozmerov}
		{@code comm// plátna a namiesto hornej strany má čiaru vytvorenú z niekoľkých}
		{@code comm// oblúkov.}
		{@code comm// (Implementácia sa spolieha na presné rozmery plátna: 600 × 400 bodov.)}
		{@code kwdprivate} {@link Oblasť Oblasť} horizont()
		{
			{@link GRobot#skočNa(double, double) skočNa}(-{@code num310}, -{@code num210});
			{@link GRobot#začniCestu() začniCestu}();
			{@link GRobot#choďNa(double, double) choďNa}(-{@code num310}, -{@code num80});
			{@link GRobot#smer(double) smer}({@code num30});
			{@link GRobot#choďNaPoOblúku(double, double) choďNaPoOblúku}({@code num50}, {@code num0});
			{@link GRobot#choďNaPoOblúku(double, double) choďNaPoOblúku}({@code num230}, -{@code num50});
			{@link GRobot#choďNaPoOblúku(double, double) choďNaPoOblúku}({@code num310}, -{@code num50});
			{@link GRobot#choďNa(double, double) choďNa}({@code num310}, -{@code num210});
			{@link GRobot#uzavriCestu() uzavriCestu}();
			{@code kwdreturn} {@code kwdnew} {@link Oblasť#Oblasť(Shape) Oblasť}({@link GRobot#cesta() cesta}());
		}

		{@code comm// Tvar oblaku je najkomplikovanejší z generovaných tvarov. Je}
		{@code comm// vytvorený zlúčením trojice „chumáčov“ rotovaných elíps. Tvar je}
		{@code comm// v tomto príklade vytvorený napevno. Keby sme ho chceli}
		{@code comm// parametrizovať (získať možnosť úpravy jeho polohy, prípadne}
		{@code comm// orientáciu a podobne), museli by sme sa zamerať na požadované}
		{@code comm// časti metódy – poloha chumáčov je nastavená pomocou trojice:}
		{@code comm// riadiaca štruktúra switch a volanie metód smer a dopredu;}
		{@code comm// s úpravou orientácie oblaku by to bolo komplikovanejšie, na to}
		{@code comm// by bolo potrebné metódu mierne prepracovať.}
		{@code kwdprivate} {@link Oblasť Oblasť} oblak()
		{
			{@code comm// Vytvorenie oblasti, do ktorej budú postupne zlučované všetky}
			{@code comm// vygenerované elipsy:}
			{@link Oblasť Oblasť} oblak = {@code kwdnew} {@link Oblasť#Oblasť() Oblasť}();

			{@code comm// Vonkajší cyklus určuje, že budú vygenerované tri „chumáče“ elíps:}
			{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num3}; ++j)
			{
				{@code comm// Vnútorný cyklus určuje, že každý „chumáč“ bude pozostávať}
				{@code comm// z piatich elíps:}
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num5}; ++i)
				{
					{@code comm// Toto vetvenie určuje stredy jednotlivých „chumáčov“:}
					{@code kwdswitch} (j)
					{
						{@code kwdcase} {@code num0}: {@link GRobot#skočNa(double, double) skočNa}(-{@code num280}, {@code num200}); {@code kwdbreak};
						{@code kwdcase} {@code num1}: {@link GRobot#skočNa(double, double) skočNa}(-{@code num250}, {@code num220}); {@code kwdbreak};
						{@code kwdcase} {@code num2}: {@link GRobot#skočNa(double, double) skočNa}(-{@code num220}, {@code num200}); {@code kwdbreak};
					}
					{@code comm// Tieto dva príkazy rozmiestňujú elipsy v rámci jedného}
					{@code comm// „chumáča“:}
					{@link GRobot#smer(double) smer}({@code num75} * i);
					{@link GRobot#dopredu(double) dopredu}({@code num20});

					{@code comm// Vygenerovanie elipsy a jej pridanie do oblasti:}
					oblak.{@link #pridaj(Shape) pridaj}({@link GRobot#elipsa(double, double) elipsa}({@code num50}, {@code num30}));
				}
			}

			{@code comm// Vrátenie výsledného tvaru oblaku:}
			{@code kwdreturn} oblak;
		}
		<hr/>
		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"srafovany-obrazok-z-oblasti.cfg"});
			{@code kwdnew} ŠrafovanýObrázokZOblastí();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <table class="centered"><tr>
 * <td><image>srafovanyObrazokSkica.png<alt/></image></td>
 * <td rowspan="2"> </td>
 * <td><image>srafovanyObrazok.png<alt/></image></td></tr><tr>
 * <td><p class="image">Skica obrázka – všetky použité tvary (len
 * predlohy) s vyznačením plochy plátna (tyrkysovou farbou).</p></td>
 * <td><p class="image">Výsledný vyšrafovaný obrázok.</p></td></tr></table>
 * 
 * <p>Výsledok je vcelku uspokojivý. No čo ak by sme chceli štyri metódy
 * na šrafovanie nahradiť jedinou univerzálnou? Metódou, ktorá by
 * dovoľovala určiť šírku šrafov a ich smer?</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Zachováme spôsob prešrafovania
 * celej plochy plátna, pretože tento spôsob dokáže bez dodatočných úprav
 * v algoritme zabezpečiť univerzálne polohovanie šrafov v rôznych
 * tvaroch pri zadaní rovnakých parametrov rozostupu a smeru šrafovania.
 * Ak by nám na univerálnom polohovaní nezáležalo (alebo by sme si ho
 * boli ochotní algoritmicky doriešiť) a chceli by sme byť úsporní
 * a prešrafovať len plochu šrafovanej oblasti, museli by sme zadať
 * šrafovanú oblasť ako parameter metódy a potom by bolo treba: nahradiť
 * zisťovanie {@linkplain Svet#šírka() šírky} a {@linkplain Svet#výška()
 * výšky} plátien {@linkplain #šírka() šírkou} a {@linkplain #výška()
 * výškou} oblasti, použiť príkaz na hľadanie {@linkplain 
 * GRobot#vzdialenosťOd(Shape) vzdialenosti k stredu oblasti (resp.
 * tvaru)} a nahradiť alebo eliminovať príkaz {@linkplain GRobot#domov()
 * prechodu na domovskú pozíciu robota}, napríklad nahradiť ho príkazom
 * {@linkplain GRobot#skočNa(Shape) prechodu (preskoku) do stredu oblasti}
 * alebo eliminovať ho zabezpečením tohto prechodu (do stredu oblasti)
 * už na začiatku algoritmu a zisťovaním vzdialenosti k opačnému bodu
 * vyšetrovanej pomyselnej úsečky – robot by sa potom už nemusel dodatočne
 * nikam presúvať.</p>
 * 
 * <p>Takáto metóda je uvedená nižšie a funguje tak, že najskôr zistí
 * rozsah šrafovania, presunie sa mimo šrafovanú plochu v určenom smere
 * šrafovania a prešrafuje ju s použitím určeného rozostupu šrafov.</p>
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code typevoid} šrafovanie({@code typedouble} smer, {@code typedouble} rozostup)
	{
		{@code comm// Presun za hranice plôch plátien sveta (o vzdialenosť šírky}
		{@code comm// šrafovania v oboch smeroch osí súradnicovej sústavy do pravého}
		{@code comm// horného rohu plôch plátien) a zistenie vzdialenosti od stredu}
		{@code comm// súradnicovej sústavy. Táto vzdialenosť bude určovať rozsah}
		{@code comm// šrafovania od stredu šrafovanej plochy (t. j. celého plátna):}
		{@link GRobot#skočNa(double, double) skočNa}({@link Svet Svet}.{@link Svet#šírka() šírka}() + rozostup, {@link Svet Svet}.{@link Svet#výška() výška}() + rozostup);
		{@code typedouble} rozsah = {@link GRobot#vzdialenosť() vzdialenosť}();

		{@code comm// Presun na začiatok (ak predpokladáme, že domovskú pozíciu}
		{@code comm// nebudeme meniť) a nasmerovanie sa do smeru šrafovania:}
		{@link GRobot#domov() domov}();
		{@link GRobot#smer(double) smer}(smer);

		{@code comm// Presun za hranice šrafovanej plochy o zistený rozsah:}
		{@link GRobot#zdvihniPero() zdvihniPero}();
		{@link GRobot#dozadu(double) dozadu}(rozsah);
		{@link GRobot#posuňVpravo(double) posuňVpravo}(rozsah);

		{@code comm// Postupné prešrafovanie celej plochy:}
		{@code kwdfor} ({@code typedouble} l = -rozsah; l &lt; rozsah; l += rozostup)
		{
			{@link GRobot#položPero() položPero}();
			{@link GRobot#dopredu(double) dopredu}({@code num2} * rozsah);

			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#vzad(double) vzad}({@code num2} * rozsah);
			{@link GRobot#posuňVľavo(double) posuňVľavo}(rozostup);
		}
	}
	</pre>
 * 
 * <p>Skutočný priebeh šrafovania obdĺžnikového tvaru (napríklad celého
 * plátna) v smere 72° a s rozostupom 4 body:</p>
 * 
 * <p><image>univerzalneSrafovanieCelaObrazovka.png<alt/>Vysvetlenie
 * princípu šrafovania.</image>Skutočný priebeh šrafovania príkazom
 * <code>šrafovanie(72, 4)</code> z nadhľadu – tyrkysový obdĺžnik
 * vymedzuje plochu šrafovanej oblasti.</p>
 * 
 * <p>Nižšie je obrázok šrafovaný metódami zhrnutými v nasledujúcej
 * tabuľke:</p>
 * 
 * <style><!--
 * table.srafovanie
 * {
 *	border: 1px solid black;
 * }
 * table.srafovanie tr th,
 * table.srafovanie tr td
 * {
 *	padding: 4px 8px;
 *	border: 1px solid black;
 * }
 * --></style>
 * 
 * <table class="centered">
 * <tr><td>
 * <table class="srafovanie">
 * <tr><th>Oblasť</th><th>Príkazy</th></tr>
 * <tr><td><code>koruna1</code></td>
 * <td><code>šrafovanie(18, 12);<br />šrafovanie(-18, 12);</code></td></tr>
 * <tr><td><code>kmeň1</code></td>
 * <td><code>šrafovanie(72, 8);<br />šrafovanie(108, 8);</code></td></tr>
 * <tr><td><code>koruna2</code></td>
 * <td><code>šrafovanie(18, 12);<br />šrafovanie(-18, 12);</code></td></tr>
 * <tr><td><code>kmeň2</code></td>
 * <td><code>šrafovanie(72, 8);<br />šrafovanie(108, 8);</code></td></tr>
 * </table>
 * </td><td> </td><td>
 * <table class="srafovanie">
 * <tr><th>Oblasť</th><th>Príkaz</th></tr>
 * <tr><td><code>krík1</code></td>
 * <td><code>šrafovanie(90, 9);</code></td></tr>
 * <tr><td><code>krík2</code></td>
 * <td><code>šrafovanie(72, 9);</code></td></tr>
 * <tr><td><code>krík3</code></td>
 * <td><code>šrafovanie(108, 9);</code></td></tr>
 * <tr><td><code>zem</code></td>
 * <td><code>šrafovanie(30, 16);</code></td></tr>
 * <tr><td><code>obloha</code></td>
 * <td><code>šrafovanie(0, 18);</code></td></tr>
 * <tr><td><code>slnko</code></td>
 * <td><code>šrafovanie(45, 20);</code></td></tr>
 * <tr><td><code>oblak</code></td>
 * <td><code>šrafovanie(-45, 20);</code></td></tr>
 * </table>
 * </td></tr>
 * </table>
 * 
 * <p><image>univerzalneSrafovanyObrazok.png<alt/>Výsledok
 * šrafovania.</image>Výsledok šrafovania obrázka príkazmi
 * uvedenými v tabuľke vyššie.</p>
 * 
 * <p> </p>
 * 
 * <p>Na záver niekoľko faktov o oblastiach a o ich vnútornej stavbe.
 * Informácie vyplývajú aj z originálnej dokumentácie triedy {@link Area
 * Area} (pozri odkaz nižšie). Oblasť tvorí vždy uzavretý útvar (obopína
 * vždy nejakú plochu po celom jej obvode). Aj keď bola vytvorená
 * z tvaru, ktorý pôvodne nebol je uzavretý (napríklad neuzavretá {@link 
 * GRobot#cesta() cesta}). Ak sa pokúsime vytvoriť oblasť z útvaru, ktorý
 * „nemá“ (neobopína) žiadnu plochu (napríklad úsečka), získame
 * {@linkplain #prázdna() prázdnu} oblasť. Aj keď útvary, z ktorých
 * oblasť tvoríme, sú jednoduché, výsledná oblasť nikdy nie je
 * v skutočnosti taká jednoduchá. Napríklad {@linkplain 
 * GRobot#kružnica(double) kružnica} alebo {@linkplain 
 * GRobot#elipsa(double, double) elipsa} sú na pohľad jednoduché, ale
 * z nich vytvorená oblasť bude vždy tvorená viacerými segmentami
 * (v tomto prípade krivkami). Zložitosť oblasti má vplyv na rýchlosť
 * spracovania pri jej použití v programe, najmä na detekciu
 * {@linkplain GRobot#koliduje(GRobot) kolízií}.</p>
 * 
 * @see java.awt.geom.Area
 */
public class Oblasť extends Area implements Poloha
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Area.html

	// Obojstranná previazanosť – robot si zase pamätá „zamestnanýPre“
	/*packagePrivate*/ GRobot zamestnanec;

	/**
	 * <p>Predvolený konštruktor. Dovoľuje vytvoriť oblasť bez spresňujúcich
	 * parametrov.</p>
	 */
	public Oblasť() {}

	/**
	 * <p>Vytvorí oblasť z určeného tvaru.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 */
	public Oblasť(Shape tvar) { super(tvar); }

	/**
	 * <p>Vytvorí prázdnu oblasť so zadaným zamestnancom. Pre podrobnosti
	 * o zamestnancovi pozri metódu {@link #zamestnaj(GRobot) zamestnaj}.</p>
	 * 
	 * @param zamestnanec robot, ktorý má byť použitý na účely tvorby
	 *     tejto oblasti (prípadne na jej nakreslenie)
	 * 
	 * @throws GRobotException ak je stanovený robot zamestnaný pre inú
	 *     oblasť
	 */
	public Oblasť(GRobot zamestnanec) { zamestnaj(zamestnanec); }

	/**
	 * <p>Vytvorí oblasť z určeného tvaru a so zadaným zamestnancom. Pre
	 * podrobnosti o zamestnancovi pozri metódu {@link #zamestnaj(GRobot)
	 * zamestnaj}.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 * @param zamestnanec robot, ktorý má byť použitý na účely tvorby
	 *     tejto oblasti (prípadne na jej nakreslenie)
	 * 
	 * @throws GRobotException ak je stanovený robot zamestnaný pre inú
	 *     oblasť
	 */
	public Oblasť(Shape tvar, GRobot zamestnanec)
	{ super(tvar); zamestnaj(zamestnanec); }


	/**
	 * <p>Zistí, či je táto oblasť prázdna – t. j. či obopína nejakú plochu.</p>
	 * 
	 * @return {@code valtrue} ak je táto oblasť prázdna
	 *     (netvorí/neobsahuje žiadny útvar); inak {@code valfalse}
	 */
	public boolean prázdna() { return isEmpty(); }

	/** <p><a class="alias"></a> Alias pre {@link #prázdna() prázdna}.</p> */
	public boolean prazdna() { return isEmpty(); }


	/**
	 * <p>Zamestná zadaného robota na účely tvorby tejto oblasti. Oblasť
	 * môže byť vytvorená aj bez pomoci robota (prostriedkami Javy),
	 * avšak na využitie možností robota, je jednoduchšie zamestnať
	 * robot, ktorý pomôže vytvoriť oblasť pomocou svojich prostriedkov
	 * na kreslenie útvarov a písanie textov (resp. „kreslenie“ textov).
	 * Každý robot stráca súčasne so zamestnaním schopnosť kreslenia
	 * a vypĺňania útvarov (okrem kreslenia a vypĺňania cesty) a písania
	 * textov. Je to z dôvodu presunutia týchto schopností do tvorby
	 * oblasti… Po {@linkplain #uvoľni(GRobot) prepustení} robota zo
	 * služby sa jeho schopnosti automaticky obnovia.</p>
	 * 
	 * <p>Robot nesmie byť zamestnaný pre inú oblasť, lebo vznikne
	 * výnimka.</p>
	 * 
	 * <p>Na tvorbu oblasti je možné použiť tieto metódy robota:
	 * {@link GRobot#krúžok(double polomer) krúžok(polomer)},
	 * {@link GRobot#kružnica(double polomer) kružnica(polomer)},
	 * {@link GRobot#kruh(double polomer) kruh(polomer)},
	 * {@link GRobot#elipsa(double a, double b) elipsa(a, b)},
	 * {@link GRobot#kresliElipsu(double a, double b) kresliElipsu(a, b)},
	 * {@link GRobot#vyplňElipsu(double a, double b) vyplňElipsu(a, b)},
	 * {@link GRobot#štvorec(double polomer) štvorec(polomer)},
	 * {@link GRobot#kresliŠtvorec(double polomer) kresliŠtvorec(polomer)},
	 * {@link GRobot#vyplňŠtvorec(double polomer) vyplňŠtvorec(polomer)},
	 * {@link GRobot#obdĺžnik(double a, double b) obdĺžnik(a, b)},
	 * {@link GRobot#kresliObdĺžnik(double a, double b) kresliObdĺžnik(a, b)},
	 * {@link GRobot#vyplňObdĺžnik(double a, double b) vyplňObdĺžnik(a, b)},
	 * {@link GRobot#hviezda(double polomer) hviezda(polomer)},
	 * {@link GRobot#kresliHviezdu(double polomer) kresliHviezdu(polomer)},
	 * {@link GRobot#vyplňHviezdu(double polomer) vyplňHviezdu(polomer)},
	 * {@link GRobot#cesta() cesta()},
	 * {@link GRobot#text(String) text(text)}
	 * a {@link GRobot#text(String, int) text(text, spôsobKreslenia)}.</p>
	 * 
	 * <p>Každá z nich môže byť argumentom metód {@link #pridaj(Shape)
	 * pridaj}, {@link #odober(Shape) odober}, {@link #prienik(Shape)
	 * prienik} a {@link #alternatíva(Shape) alternatíva}.</p>
	 * 
	 * @param zamestnanec robot, ktorý má byť použitý na účely tvorby
	 *     tejto oblasti (prípadne na jej nakreslenie)
	 * 
	 * @throws GRobotException ak je robot zamestnaný pre inú oblasť
	 */
	public void zamestnaj(GRobot zamestnanec)
	{
		if (this.zamestnanec == zamestnanec) return;
		if (null != zamestnanec.zamestnanýPre) throw new GRobotException(
			"Tento robot už je zamestnaný na účely tvorby inej " +
			"oblasti.", "robotAlreadyEmployed");
		uvoľni();
		zamestnanec.kresliTvary = false;
		zamestnanec.zamestnanýPre = this;
		this.zamestnanec = zamestnanec;
	}

	/**
	 * <p>Automaticky uvoľní robot {@linkplain #zamestnaj(GRobot)
	 * zamestnaný} na účely tvorby (prípadne kreslenia) tejto oblasti.
	 * Ak taký robot nejestvuje, volanie metódy nemá žiadny efekt.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
	 * v programovacom rámci GRobot deväť rôznych metód:
	 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
	 * uvoľni}{@code ()},
	 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
	 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
	 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
	 * uvoľni}{@code ()},
	 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
	 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
	 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
	 * oblasť (čo je geometrická trieda),
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
	 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
	 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
	 * Svet#upevni() Svet.upevni}) a nakoniec
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
	 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
	 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Plazma)
	 * uvoľni}{@code (}{@link Plazma Plazma}{@code )}
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
	 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
	 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
	 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
	 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
	 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
	 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
	 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
	 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
	 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
	 * disku zmenil).</p>
	 * 
	 * @see #uvoľni(GRobot)
	 * @see GRobot#uvoľni()
	 */
	public void uvoľni()
	{
		if (null != zamestnanec)
		{
			zamestnanec.kresliTvary = true;
			zamestnanec.zamestnanýPre = null;
			zamestnanec = null;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľni() uvoľni}.</p> */
	public void uvolni() { uvoľni(); }

	/** <p><a class="alias"></a> Alias pre {@link #uvoľni() uvoľni}.</p> */
	public void prepusti() { uvoľni(); }

	/**
	 * <p>Uvoľní robot {@linkplain #zamestnaj(GRobot) zamestnaný} na
	 * účely tvorenia tejto oblasti. Ak zadaný robot nebol zamestnaný
	 * pre túto oblasť, volanie tejto metódy nemá žiadny efekt.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
	 * v programovacom rámci GRobot deväť rôznych metód:
	 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
	 * uvoľni}{@code ()},
	 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
	 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
	 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
	 * uvoľni}{@code ()},
	 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
	 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
	 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
	 * oblasť (čo je geometrická trieda),
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
	 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
	 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
	 * Svet#upevni() Svet.upevni}) a nakoniec
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
	 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
	 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Plazma)
	 * uvoľni}{@code (}{@link Plazma Plazma}{@code )}
	 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
	 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
	 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
	 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
	 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
	 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
	 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
	 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
	 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
	 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
	 * disku zmenil).</p>
	 * 
	 * @param zamestnanec robot, ktorý bol {@linkplain 
	 *     #zamestnaj(GRobot) zamestnaný} pre túto oblasť
	 * 
	 * @see #uvoľni()
	 * @see GRobot#uvoľni(Oblasť)
	 */
	public void uvoľni(GRobot zamestnanec)
	{
		if (this.zamestnanec == zamestnanec)
		{
			zamestnanec.kresliTvary = true;
			zamestnanec.zamestnanýPre = null;
			this.zamestnanec = null;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľni(GRobot) uvoľni}.</p> */
	public void uvolni(GRobot zamestnanec) { uvoľni(zamestnanec); }

	/** <p><a class="alias"></a> Alias pre {@link #uvoľni(GRobot) uvoľni}.</p> */
	public void prepusti(GRobot zamestnanec) { uvoľni(zamestnanec); }

	/**
	 * <p>Overí, či je pre túto oblasť zamestnaný ľubovoľný robot.</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse}
	 */
	public boolean zamestnaný() { return zamestnanec != null; }

	/** <p><a class="alias"></a> Alias pre {@link #zamestnaný() zamestnaný}.</p> */
	public boolean zamestnany() { return zamestnanec != null; }

	/**
	 * <p>Overí, či je pre túto oblasť zamestnaný určený robot.</p>
	 * 
	 * @param zamestnanec vyšetrovaný robot
	 * @return {@code valtrue}/&#8203;{@code valfalse}
	 */
	public boolean zamestnaný(GRobot zamestnanec)
	{ return this.zamestnanec == zamestnanec; }

	/** <p><a class="alias"></a> Alias pre {@link #zamestnaný(GRobot) zamestnaný}.</p> */
	public boolean zamestnany(GRobot zamestnanec)
	{ return this.zamestnanec == zamestnanec; }


	/**
	 * <p>Pridá k oblasti zadaný tvar.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 * 
	 * @see #zamestnaj(GRobot)
	 */
	public void pridaj(Shape tvar)
	{
		Area oblasť = new Area(tvar);
		add(oblasť);
	}

	/**
	 * <p>Odoberie z oblasti zadaný tvar.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 * 
	 * @see #zamestnaj(GRobot)
	 */
	public void odober(Shape tvar)
	{
		Area oblasť = new Area(tvar);
		subtract(oblasť);
	}

	/**
	 * <p>Vytvorí prienik oblasti a zadaného tvaru.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 * 
	 * @see #zamestnaj(GRobot)
	 */
	public void prienik(Shape tvar)
	{
		Area oblasť = new Area(tvar);
		intersect(oblasť);
	}

	/**
	 * <p>Vytvorí z oblasti a zadaného tvaru alternatívu – XOR (vylučujúce
	 * alebo, buď/alebo). Nová oblasť bude obsahovať také plochy, ktoré
	 * patrili buď výlučne do pôvodnej oblasti, alebo výlučne do zadaného
	 * tvaru.</p>
	 * 
	 * @param tvar ľubovoľný tvar Javy; ak sa chcete vyhnúť ťažkostiam
	 *     s úpravou súradnicového priestoru, odporúčame na generovanie
	 *     tvarov používať metódy robota (pozri {@linkplain Oblasť
	 *     úvod}), najlepšie toho, ktorý bol na účely tvorby tejto
	 *     oblasti {@linkplain #zamestnaj(GRobot) zamestnaný}
	 * 
	 * @see #zamestnaj(GRobot)
	 */
	public void alternatíva(Shape tvar)
	{
		Area oblasť = new Area(tvar);
		exclusiveOr(oblasť);
	}

	/** <p><a class="alias"></a> Alias pre {@link #alternatíva(Shape) alternatíva}.</p> */
	public void alternativa(Shape tvar)
	{
		Area oblasť = new Area(tvar);
		exclusiveOr(oblasť);
	}


	/**
	 * <p>Kompletne odstráni geometriu oblasti – vyprázdni oblasť. Použiteľné
	 * pri recyklovaní objektu, keď vykonal prácu na ktorú bol určený…</p>
	 */
	public void vymaž() { reset(); }

	/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
	public void vymaz() { reset(); }


	/**
	 * <p>Nakreslí túto oblasť s využitím {@linkplain #zamestnaj(GRobot)
	 * zamestnaného} robota (použije jeho farbu alebo náter, hrúbku pera
	 * a aktívne plátno). Keď bol robot medzičasom {@linkplain 
	 * #uvoľni(GRobot) uvoľnený} zo služby, nebude mať volanie tejto
	 * metódy žiadny efekt. Volanie tejto metódy nakreslí oblasť tak, ako
	 * bola vytvorená. Keď chcete využiť aj aktuálnu polohu a rotáciu
	 * robota, volajte metódu {@link GRobot#obkresliOblasť(Area)
	 * obkresliOblasť}.</p>
	 */
	public void kresli()
	{
		if (null != zamestnanec)
			zamestnanec.kresli(this);
	}

	/**
	 * <p>Vyplní plochu tejto oblasti s využitím {@linkplain 
	 * #zamestnaj(GRobot) zamestnaného} robota (použije jeho farbu alebo
	 * náter a aktívne plátno). Keď bol robot medzičasom {@linkplain 
	 * #uvoľni(GRobot) uvoľnený} zo služby, tak nemá volanie tejto metódy
	 * žiadny efekt. Volanie tejto metódy vyplní oblasť tak, ako bola
	 * vytvorená. Keď chcete využiť aj aktuálnu polohu a rotáciu robota,
	 * volajte metódu {@link GRobot#vyplňOblasť(Area) vyplňOblasť}.</p>
	 */
	public void vyplň()
	{
		if (null != zamestnanec)
			zamestnanec.vyplň(this);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň() vyplň}.</p> */
	public void vypln() { vyplň(); }

	/**
	 * <p>Vyplní plochu tejto oblasti textúrou s využitím {@linkplain 
	 * #zamestnaj(GRobot) zamestnaného} robota (použije jeho aktívne
	 * plátno). Keď bol robot medzičasom {@linkplain 
	 * #uvoľni(GRobot) uvoľnený} zo služby, tak nemá volanie tejto metódy
	 * žiadny efekt. Volanie tejto metódy vyplní oblasť tak, ako bola
	 * vytvorená. Keď chcete využiť aj aktuálnu polohu a rotáciu robota,
	 * volajte metódu {@link GRobot#vyplňOblasť(Area, String)
	 * vyplňOblasť}. Textúra je súbor s obrázkom, ktorý bude použitý na
	 * dlaždicové vyplnenie oblasti.</p>
	 * 
	 * <p>Obrázok (výplne) prečítaný zo súboru je chápaný ako zdroj a po
	 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
	 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
	 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
	 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
	 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
	 * prijímajú názov súboru ako parameter.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Parametre textúry sa dajú
	 * ovplyvňovať špeciálnymi príkazmi. Súvisí s nimi i predvolené
	 * správanie príkazov vypĺňania. Predvolený bod začiatku vypĺňania
	 * dlaždicami sa nachádza v strede plátna alebo vypĺňaného
	 * obrázka. Pozrite si aj opis metódy
	 * {@link Svet#posunutieVýplne(double, double) posunutieVýplne},
	 * kde nájdete príklad použitia a odkazy na metódy
	 * upravujúce ďalšie parametre obrázkových výplní.</p>
	 * 
	 * @param súbor názov súboru s obrázkom textúry
	 * 
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void vyplň(String súbor)
	{
		if (null != zamestnanec)
			zamestnanec.vyplň(this, súbor);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň(String) vyplň}.</p> */
	public void vypln(String súbor) { vyplň(súbor); }

	/**
	 * <p>Vyplní plochu tejto oblasti textúrou s využitím {@linkplain 
	 * #zamestnaj(GRobot) zamestnaného} robota (použije jeho aktívne
	 * plátno). Keď bol robot medzičasom {@linkplain 
	 * #uvoľni(GRobot) uvoľnený} zo služby, tak nemá volanie tejto metódy
	 * žiadny efekt. Volanie tejto metódy vyplní oblasť tak, ako bola
	 * vytvorená. Keď chcete využiť aj aktuálnu polohu a rotáciu robota,
	 * volajte metódu {@link GRobot#vyplňOblasť(Area, Image)
	 * vyplňOblasť}. Textúra je {@linkplain Obrázok obrázok} (objekt typu
	 * {@link Image Image} alebo odvodený), ktorý bude
	 * použitý na dlaždicové vyplnenie oblasti.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Parametre textúry sa dajú
	 * ovplyvňovať špeciálnymi príkazmi. Súvisí s nimi i predvolené
	 * správanie príkazov vypĺňania. Predvolený bod začiatku vypĺňania
	 * dlaždicami sa nachádza v strede plátna alebo vypĺňaného
	 * obrázka. Pozrite si aj opis metódy
	 * {@link Svet#posunutieVýplne(double, double) posunutieVýplne},
	 * kde nájdete príklad použitia a odkazy na metódy
	 * upravujúce ďalšie parametre obrázkových výplní.</p>
	 * 
	 * @param výplň obrázok s textúrou
	 */
	public void vyplň(Image výplň)
	{
		if (null != zamestnanec)
			zamestnanec.vyplň(this, výplň);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň(Image) vyplň}.</p> */
	public void vypln(Image výplň) { vyplň(výplň); }

	/**
	 * <p>Nakreslí túto oblasť na aktívne plátno aktuálnou farbou (alebo
	 * náterom) a hrúbkou pera zadaného robota. Táto metóda nakreslí oblasť
	 * tak, ako bola vytvorená. Keď ju chcete rotovať a/alebo posúvať,
	 * použite metódu {@link GRobot#obkresliOblasť(Area)
	 * obkresliOblasť}.</p>
	 * 
	 * @param r robot, ktorý bude použitý na nakreslenie tejto oblasti
	 *     (iba farba, hrúbka pera a aktívne plánto)
	 */
	public void kresli(GRobot r)
	{
		r.kresli(this);
	}

	/**
	 * <p>Vyplní plochu tejto oblasti na aktívne plátno aktuálnou farbou
	 * (alebo náterom) zadaného robota. Táto metóda vyplní oblasť tak, ako
	 * bola vytvorená. Keď ju chcete rotovať a/alebo posúvať, použite
	 * metódu {@link GRobot#vyplňOblasť(Area) vyplňOblasť}.</p>
	 * 
	 * @param r robot, ktorý bude použitý na vyplnenie tejto oblasti (iba
	 *     farba a aktívne plánto)
	 */
	public void vyplň(GRobot r)
	{
		r.vyplň(this);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň(GRobot) vyplň}.</p> */
	public void vypln(GRobot r) { vyplň(r); }

	/**
	 * <p>Vyplní plochu tejto oblasti na aktívne plátno zadaného robota
	 * zadanou textúrou. Táto metóda vyplní oblasť tak, ako bola
	 * vytvorená.
	 * Keď ju chcete rotovať a/alebo posúvať, použite metódu {@link 
	 * GRobot#vyplňOblasť(Area, String) vyplňOblasť}. Textúra je súbor
	 * s obrázkom, ktorý bude použitý na dlaždicové vyplnenie oblasti.</p>
	 * 
	 * <p>Obrázok (výplne) prečítaný zo súboru je chápaný ako zdroj a po
	 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
	 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
	 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
	 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
	 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
	 * prijímajú názov súboru ako parameter.)</p>
	 * 
	 * @param r robot, ktorý bude použitý na vyplnenie tejto oblasti
	 *     (iba aktívne plánto)
	 * @param súbor názov súboru s obrázkom textúry
	 * 
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void vyplň(GRobot r, String súbor)
	{
		r.vyplň(this, súbor);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň(GRobot, String) vyplň}.</p> */
	public void vypln(GRobot r, String súbor) { vyplň(r, súbor); }

	/**
	 * <p>Vyplní plochu tejto oblasti na aktívne plátno zadaného robota
	 * zadanou textúrou. Táto metóda vyplní oblasť tak, ako bola
	 * vytvorená.
	 * Keď ju chcete rotovať a/alebo posúvať, použite metódu {@link 
	 * GRobot#vyplňOblasť(Area, Image) vyplňOblasť}. Textúra je
	 * {@linkplain Obrázok obrázok} (objekt typu {@link Image
	 * Image} alebo odvodený), ktorý bude použitý na dlaždicové
	 * vyplnenie oblasti.</p>
	 * 
	 * @param r robot, ktorý bude použitý na vyplnenie tejto oblasti
	 *     (iba aktívne plánto)
	 * @param výplň obrázok s textúrou
	 */
	public void vyplň(GRobot r, Image výplň)
	{
		r.vyplň(this, výplň);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyplň(GRobot, Image) vyplň}.</p> */
	public void vypln(GRobot r, Image výplň) { vyplň(r, výplň); }


	/**
	 * <p>Zistí, či sa súradnice zadaného bodu nachádzajú v tejto oblasti.
	 * Metóda posudzuje oblasť tak, ako bola vytvorená. Keď chcete
	 * využiť rotáciu alebo posun niektorého robota, použite metódu
	 * {@link GRobot#bodVOblasti(double, double, Area) bodVOblasti}.</p>
	 * 
	 * @param súradnicaBoduX x-ová súradnica bodu
	 * @param súradnicaBoduY y-ová súradnica bodu
	 * @return {@code valtrue} – áno; {@code valfalse} – nie
	 */
	public boolean bodV(double súradnicaBoduX, double súradnicaBoduY)
	{
		return contains(Svet.prepočítajX(súradnicaBoduX),
			Svet.prepočítajY(súradnicaBoduY));
	}

	// /**
	//  * <p>Zistí, či sa súradnice zadaného bodu nachádzajú v tejto oblasti.
	//  * Metóda posudzuje oblasť tak, ako bola vytvorená. Keď chcete
	//  * využiť rotáciu alebo posun niektorého robota, použite metódu
	//  * {@link GRobot#bodVOblasti(Point2D, Area) bodVOblasti}.</p>
	//  * 
	//  * @param bod súradnice vyšetrovaného bodu
	//  * @return {@code valtrue} – áno; {@code valfalse} – nie
	//  */
	// public boolean bodV(Point2D bod)
	// { return contains(Svet.prepočítajX(bod.getX()), Svet.prepočítajY(bod.getY())); }

	/**
	 * <p>Zistí, či sa bod polohy robota nachádza v tejto oblasti. Metóda
	 * posudzuje polohu bodu v oblasti v takom stave, ako bola
	 * vytvorená. Keď chcete využiť rotáciu alebo posun niektorého
	 * robota, použite metódu {@link GRobot#bodVOblasti(Poloha, Area)
	 * bodVOblasti}.</p>
	 * 
	 * @param r robot, ktorého poloha je vyšetrovaná
	 * @return {@code valtrue} – áno; {@code valfalse} – nie
	 */
	public boolean bodV(Poloha r)
	{
		return contains(Svet.prepočítajX(r.polohaX()),
			Svet.prepočítajY(r.polohaY()));
	}

	/**
	 * <p>Zistí, či sa aktuálne súradnice myši nachádzajú v tejto oblasti.
	 * Metóda posudzuje oblasť tak, ako bola vytvorená. Keď chcete
	 * využiť rotáciu alebo posun niektorého robota, použite metódu
	 * {@link GRobot#myšVOblasti(Area) myšVOblasti}.</p>
	 * 
	 * @return {@code valtrue} – áno; {@code valfalse} – nie
	 */
	public boolean myšV()
	{
		return contains(Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX),
			Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY));
	}

	/** <p><a class="alias"></a> Alias pre {@link #myšV() myšV}.</p> */
	public boolean mysV() { return myšV(); }


	/**
	 * <p>Vypočíta a vráti x-ovú súradnicu polohy stredu oblasti.</p>
	 * 
	 * @return x-ová súradnica polohy stredu oblasti
	 */
	public double polohaX()
	{
		Rectangle2D hranice = getBounds2D();
		double ox = Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2;
		return ox;
	}

	/**
	 * <p>Vypočíta a vráti y-ovú súradnicu polohy stredu oblasti.</p>
	 * 
	 * @return y-ová súradnica polohy stredu oblasti
	 */
	public double polohaY()
	{
		Rectangle2D hranice = getBounds2D();
		double oy = Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2;
		return oy;
	}

	/**
	 * <p>Vypočíta a vráti x-ovú súradnicu polohy stredu oblasti.</p>
	 * 
	 * @return x-ová súradnica polohy stredu oblasti
	 */
	public double súradnicaX()
	{
		return polohaX();
	}

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaX() súradnicaX}.</p> */
	public double suradnicaX()
	{
		return polohaX();
	}

	/**
	 * <p>Vypočíta a vráti y-ovú súradnicu polohy stredu oblasti.</p>
	 * 
	 * @return y-ová súradnica polohy stredu oblasti
	 */
	public double súradnicaY()
	{
		return polohaY();
	}

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaY() súradnicaY}.</p> */
	public double suradnicaY()
	{
		return polohaY();
	}

	/**
	 * <p>Vypočíta a vráti polohu stredu oblasti.</p>
	 * 
	 * @return poloha stredu oblasti
	 */
	public Bod poloha()
	{
		Rectangle2D hranice = getBounds2D();
		double ox = Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2;
		double oy = Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2;
		return new Bod(ox, oy);
	}


	/**
	 * <p>Overí, či sa poloha tejto oblasti (t. j. súradníc jej stredu)
	 * dokonale zhoduje so zadanými súradnicami. Ak je zistená zhoda, tak
	 * metóda vráti hodnotu {@code valtrue}, v opačnom prípade hodnotu
	 * {@code valfalse}.</p>
	 * 
	 * @param x x-ová súradnica, s ktorou má byť porovnaná poloha tejto oblasti
	 * @param y y-ová súradnica, s ktorou má byť porovnaná poloha tejto oblasti
	 * @return {@code valtrue} ak sa poloha tejto oblasti zhoduje so zadanými
	 *     súradnicami, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(double x, double y)
	{
		Rectangle2D hranice = getBounds2D();
		double ox = Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2;
		double oy = Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2;
		return ox == x && oy == y;
	}

	/**
	 * <p>Overí, či sa poloha tejto oblasti (t. j. súradníc jej stredu)
	 * a poloha zadaného objektu dokonale zhodujú. Ak je zistená zhoda,
	 * tak metóda vráti hodnotu {@code valtrue}, v opačnom prípade hodnotu
	 * {@code valfalse}.</p>
	 * 
	 * @param poloha objekt, ktorého poloha má byť porovnaná s polohou tejto
	 *     oblasti
	 * @return {@code valtrue} ak sa poloha tejto oblasti zhoduje s polohou
	 *     zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(Poloha poloha)
	{
		Rectangle2D hranice = getBounds2D();
		double ox = Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2;
		double oy = Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2;
		return poloha.polohaX() == ox && poloha.polohaY() == oy;
	}


	/**
	 * <p>Vráti šírku oblasti.</p>
	 * 
	 * @return šírka oblasti
	 */
	public double šírka() { return getBounds2D().getWidth(); }

	/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
	public double sirka() { return getBounds2D().getWidth(); }

	/**
	 * <p>Vráti výšku oblasti.</p>
	 * 
	 * @return výška oblasti
	 */
	public double výška()
	{ return getBounds2D().getHeight(); }

	/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
	public double vyska() { return getBounds2D().getHeight(); }
}
