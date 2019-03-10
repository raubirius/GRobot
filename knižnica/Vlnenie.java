
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2018 by Roman Horváth
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import knižnica.GRobot;
import knižnica.Poloha;

/**
 * <p>Táto trieda slúži na oživenie vlnenia obrázkov, plátien i sveta.</p>
 * 
 * <p>Animácia vlnenia použitého v rámci programovacieho rámca je vykonávaná
 * automaticky počas činnosti časovača a zvlnený raster objektu (napríklad
 * {@linkplain Plátno plátna} alebo {@linkplain Obrázok obrázka})
 * je v rámci programovacieho rámca tiež použitý automaticky a to v čase
 * kreslenia vlniaceho sa objektu.</p>
 * 
 * <p>Vlnenie pracuje s pomocou dvoch výškových máp (obrazne nazývaných
 * v tejto dokumentácii aj hladinou), ktoré sú striedavo prepočítavané tak,
 * ako keby tvorili striedajúce sa snímky postupujúcej animácie. Na základe
 * hodnoty aktuálnej snímky mapy je výsledný obraz v každom okamihu animácie
 * zdeformovaný tak, ako keby slnečné lúče prechádzali „krivou šošovkou“
 * (čiže takou šošovkou, ktorej povrch obsahuje rôzne deformity) umiestnenou
 * nad stred vlniacej sa plochy (s tým, že tvar tejto „krivej šošovky“
 * sa dynamicky mení). Časti hladiny môžu byť ľubovoľne aktivované alebo
 * deaktivované, takže je možné presne určiť, ktorá časť zobrazovenej plochy
 * bude ovplyvnená vlnením, a ktorá nie. Možnosti vlnenia najlepšie ukazuje
 * nasledujúci príklad.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento vyčerpávajúci príklad ukazuje mnoho spôsobov použitia tejto triedy.
 * Na svoje fungovanie vyžaduje nasledujúci obrázok slnečnice (fotografiu
 * vyhotovil a mierne upravil autor programovacieho rámca): <a target="_blank"
 * href="resources/slnecnica.png">slnecnica.png</a>.</p>
 * 
 * <p>Po spustení sú použiteľné nasledujúce klávesy na ovládanie aplikácie
 * (robota a/alebo plochy vlnenia – podrobnosti sú v komentároch príkladu):</p>
 * 
 * <table>
 * <tr><td><code>MEDZERA</code></td><td>–</td><td>pozastavenie a obnovenie
 * animácie vlnenia</td></tr>
 * <tr><td><code>ESCAPE</code></td><td>–</td><td>okamžité upokojenie
 * hladiny</td></tr> <tr><td><code>DELETE</code></td><td>–</td><td>zrušenie
 * všetkých zmien – vymazanie zmrazených oblastí vlnenia, vymazanie prípadnej
 * kresby na podlahe a obnovenie pôvodného obrázka</td></tr>
 * <tr><td colspan="3"><hr/></td></tr>
 * </table>
 * 
 * <table>
 * <tr><td><code>A</code></td><td>–</td><td>kruhové šplechnutie na pozícii
 * robota</td></tr>
 * <tr><td><code>S</code></td><td>–</td><td>kružnicové šplechnutie na pozícii
 * robota</td></tr>
 * <tr><td><code>D</code></td><td>–</td><td>štvorcový vzruch na pozícii
 * robota</td></tr>
 * <tr><td><code>F</code></td><td>–</td><td>vlnka na pozícii robota v tvare
 * pyramídy na nedokonalej kruhovej podstave; v kombinácii s klávesom
 * <code>SHIFT</code> znamená zmrazenie vlnky</td></tr>
 * 
 * <tr><td colspan="3"><hr/></td></tr>
 * 
 * <tr><td><code>X</code></td><td>–</td><td>„krídlové šplechnutie“ s použitím
 * tvaru kruhu</td></tr>
 * <tr><td><code>C</code></td><td>–</td><td>„krídlové šplechnutie“ s použitím
 * tvaru štvorca</td></tr>
 * <tr><td><code>V</code></td><td>–</td><td>„krídlové šplechnutie“ s použitím
 * tvaru hviezdy</td></tr>
 * 
 * <tr><td colspan="3"><hr/></td></tr>
 * 
 * <tr><td><code>E</code></td><td>–</td><td>búrlivé rozvlnenie
 * obrazu</td></tr>
 * <tr><td><code>R</code></td><td>–</td><td>náhodné rozčerenie
 * hladiny</td></tr>
 * <tr><td><code>T</code></td><td>–</td><td>jemné pravidelné rozvlnenie
 * hladiny</td></tr>
 * 
 * <tr><td colspan="3"><hr/></td></tr>
 * 
 * <tr><td><code>G</code></td><td>–</td><td>zmrazenie oblasti hviezdy na
 * pozícii robota</td></tr>
 * <tr><td><code>H</code></td><td>–</td><td>izolácia deaktivovanej oblasti
 * vlnenia</td></tr>
 * 
 * <tr><td colspan="3"><hr/></td></tr>
 * 
 * <tr><td><code>U</code></td><td>–</td><td>zapnutie alebo vypnutie pomocného
 * robota tvoriaceho efekt vlajky</td></tr>
 * <tr><td><code>P</code></td><td>–</td><td>zapnutie alebo vypnutie pomocného
 * robota tvoriaceho efekt dažďa</td></tr>
 * 
 * <tr><td colspan="3"><hr/></td></tr>
 * 
 * <tr><td><code>I</code></td><td>–</td><td>zväčšenie robota</td></tr>
 * <tr><td><code>O</code></td><td>–</td><td>zmenšenie robota</td></tr>
 * </table>
 * 
 * <table>
 * <tr><td colspan="3"><hr/></td></tr>
 * <tr><td><code>HORE</code></td><td>–</td><td>posun robota dopredu; alebo
 * v kombinácii s klávesom <code>CTRL</code> rolovanie mapy vlnenia smerom
 * hore; alebo v kombinácii s klávesom <code>SHIFT</code> pretáčanie mapy
 * vlnenia smerom hore</td></tr>
 * <tr><td><code>DOLE</code></td><td>–</td><td>posun robota dozadu; alebo
 * v kombinácii s klávesom <code>CTRL</code> rolovanie mapy vlnenia smerom
 * dole; alebo v kombinácii s klávesom <code>SHIFT</code> pretáčanie mapy
 * vlnenia smerom dole</td></tr>
 * <tr><td><code>VĽAVO</code></td><td>–</td><td>pootočenie robota vľavo; alebo
 * v kombinácii s klávesom <code>CTRL</code> rolovanie mapy vlnenia smerom
 * vľavo; alebo v kombinácii s klávesom <code>SHIFT</code> pretáčanie mapy
 * vlnenia smerom
 * vľavo</td></tr>
 * <tr><td><code>VPRAVO</code></td><td>–</td><td>pootočenie robota vpravo;
 * alebo v kombinácii s klávesom <code>CTRL</code> rolovanie mapy vlnenia
 * smerom vpravo; alebo v kombinácii s klávesom <code>SHIFT</code> pretáčanie
 * mapy vlnenia smerom vpravo</td></tr>
 * <tr><td><code>J</code></td><td>–</td><td>posunutie robota dopredu
 * a vytvorenie bariéry (zmrazenie čiary) na dráhe robota</td></tr>
 * <tr><td><code>N</code></td><td>–</td><td>posunutie robota dozadu
 * a vytvorenie bariéry (zmrazenie čiary) na dráhe robota</td></tr>
 * </table>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.{@link GRobot GRobot};
	{@code kwdimport} knižnica.{@code currVlnenie};

	{@code kwdpublic} {@code typeclass} TestovanieVlnenia {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Atribút na uloženie predlohy vlnenia (obrázka slnečnice):}
		{@code kwdprivate} {@link Obrázok Obrázok} predloha;

		{@code comm// Atribút na uloženie vlnenia, s ktorým budeme v tomto príklade}
		{@code comm// manipulovať}
		{@code kwdprivate} {@code currVlnenie} vlnenie;

		{@code comm// Atribúty na uloženie inštancií dvoch pomocných robotov:}
		{@code kwdprivate} {@link GRobot GRobot} vlajka, dážď;

		{@code comm// Konštruktor.}
		{@code kwdprivate} TestovanieVlnenia()
		{
			{@code comm// Volanie nadradeného konštruktora – úprava rozmerov plátien.}
			{@code valsuper}({@code num850}, {@code num650});

			{@code comm// Vypnutie automatického prekresľovania:}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Nastavenie parametrov robota:}
			{@link GRobot#farba(Color) farba}({@link červená červená});
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num3});
			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#nekresliTvary() nekresliTvary}();
			{@link GRobot#veľkosť(double) veľkosť}({@code num50});
			{@link GRobot#rýchlosť(double) rýchlosť}({@link GRobot#veľkosť() veľkosť}() / {@code num10}, {@code valfalse});
			{@link GRobot#vypĺňajTvary(boolean) vypĺňajTvary}({@code valtrue});

			{@code comm// Prečítanie a nakreslenie predlohy vlnenia:}
			predloha = {@link Obrázok Obrázok}.{@link Obrázok#čítaj(String) čítaj}({@code srg"slnecnica.png"});
			{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(predloha);

			{@code comm// Aktivovanie vlnenia pre celý svet (ak by sme identifikátor „Svet“}
			{@code comm// nahradili identifikátorom „podlaha“, vlnila by sa len podlaha,}
			{@code comm// robot by nebol ovlpyvnený; ak by sme ho nahradili identifikátorom}
			{@code comm// „predloha“, vlnil by sa len obrázok predlohy, ktorý by sme museli}
			{@code comm// neustále kresliť na podlahu alebo strop, aby bolo jeho vlnenie}
			{@code comm// viditeľné):}
			{@link Svet Svet}.{@link Svet#pridajVlnenie() pridajVlnenie}();

			{@code comm// Uloženie aktívnej inštancie vlnenia (ak by sme vlnili iný objekt}
			{@code comm// než Svet, tak by sme identifikátor „Svet“ museli nahradiť správnym}
			{@code comm// identifikátorom – pozri komentár príkazu vyššie):}
			vlnenie = {@link Svet Svet}.{@link Svet#vlnenie() vlnenie}();

			{@code comm// Vytvorenie inštancie pomocného robota na vlajku:}
			vlajka = {@code kwdnew} {@link GRobot#GRobot() GRobot}()
			{
				{
					{@code comm// Nastavenie parametrov robota:}
					{@link GRobot#vpravo(double) vpravo}({@code num90});
					{@link GRobot#rýchlosť(double) rýchlosť}({@code num20}, {@code valfalse});
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#skry() skry}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktivita() aktivita}()
				{
					{@code comm// Definícia správania počas jeho aktivity…}

					{@code comm// Kontrola opustenia hraníc obrazovky a presun na}
					{@code comm// protiľahnú stranu (pri neustálom pohybe zľava doprava by}
					{@code comm// stačili len prvé dva riadky nasledujúcich dvoch}
					{@code comm// konštrukcií, ale úplná kontrola zabezpečuje, že pomocník}
					{@code comm// bude fungovať aj keď zmeníme smer jeho pohybu):}

					{@code kwdif} ({@link GRobot#polohaX() polohaX}() &gt; {@link Svet Svet}.{@link Svet#najväčšieX() najväčšieX}() + {@code num600})
						{@link GRobot#polohaX(double) polohaX}({@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}() &#45; {@code num600});
					{@code kwdelse} {@code kwdif} ({@link GRobot#polohaX() polohaX}() &lt; {@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}() &#45; {@code num600})
						{@link GRobot#polohaX(double) polohaX}({@link Svet Svet}.{@link Svet#najväčšieX() najväčšieX}() + {@code num600});

					{@code kwdif} ({@link GRobot#polohaY() polohaY}() &gt; {@link Svet Svet}.{@link Svet#najväčšieY() najväčšieY}() + {@code num400})
						{@link GRobot#polohaY(double) polohaY}({@link Svet Svet}.{@link Svet#najmenšieY() najmenšieY}() &#45; {@code num400});
					{@code kwdelse} {@code kwdif} ({@link GRobot#polohaY() polohaY}() &lt; {@link Svet Svet}.{@link Svet#najmenšieY() najmenšieY}() &#45; {@code num400})
						{@link GRobot#polohaY(double) polohaY}({@link Svet Svet}.{@link Svet#najväčšieY() najväčšieY}() + {@code num400});

					{@code comm// Pridanie vlnky s veľkým rozmerom a malou intenzitou na}
					{@code comm// aktuálnej pozícii pomocníka:}
					vlnenie.{@link #pridajVlnku(Poloha, double, double) pridajVlnku}({@code valthis}, {@code num800}, -{@code num0.05});
				}
			};

			{@code comm// Vytvorenie inštancie pomocného robota na dážď:}
			dážď = {@code kwdnew} {@link GRobot#GRobot() GRobot}()
			{
				{
					{@code comm// Nastavenie parametrov robota (vlastne ho stačí len skryť,}
					{@code comm// aby neprekážal vo výhľade):}
					{@link GRobot#skry() skry}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktivita() aktivita}()
				{
					{@code comm// Definícia správania počas jeho aktivity – presun na}
					{@code comm// náhodnú pozíciu a pridanie vzruchu na hladinu:}
					{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
					vlnenie.{@link #pridajVzruch(Poloha) pridajVzruch}({@code valthis});
				}
			};

			{@code comm// Spustenie časovača…}
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}


		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktivita() aktivita}()
		{
			{@code comm// Robot bude počas svojho pohybu vlniť prostredie s polomerom podľa}
			{@code comm// svojej veľkosti (intenzita má hodnotu 5):}
			vlnenie.{@link #pridajVlnku(Poloha, double, double) pridajVlnku}({@code valthis}, {@link GRobot#veľkosť() veľkosť}(), {@code num5});
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@code comm// Ak by sme aktivovali vlnenie len pre obrázok, tu by sme ho mohli}
			{@code comm// nakresliť:}
			{@code comm//	podlaha.obrázok(predloha);}

			{@code comm// Automatické prekresľovanie v prípade potreby:}
			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieKlávesu() stlačenieKlávesu}()
		{
			{@code comm// Všeobecné funkcie na ovládanie vlnenia.}

			{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#MEDZERA MEDZERA}))
			{
				{@code comm// Pozastavenie a opätovné spustenie vlnenia:}
				{@code kwdif} (vlnenie.{@link #aktívne() aktívne}())
					vlnenie.{@link #deaktivuj() deaktivuj}();
				{@code kwdelse}
					vlnenie.{@link #aktivuj() aktivuj}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_ESCAPE VK_ESCAPE}))
			{
				{@code comm// Okamžité zrušenie vlnenia hladiny:}
				vlnenie.{@link #upokojHladinu() upokojHladinu}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_DELETE VK_DELETE}))
			{
				{@code comm// Zrušenie zamrazených oblastí vlnenia, upokojenie hladiny,}
				{@code comm// vymazanie prípadnej kresby na podlahe s obnovením pôvodného}
				{@code comm// obrázka:}
				vlnenie.{@link #aktivujHladinu() aktivujHladinu}();
				vlnenie.{@link #upokojHladinu() upokojHladinu}();
				{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
				{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(predloha);
			}

			{@code comm// Séria A – F: Rôzne spôsoby rozvlnenia na pozícii robota.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_A VK_A}))
			{
				{@code comm// Intenzívne kruhové šplechnutie (pozor, metóda „kruh“ definuje}
				{@code comm// len hranice tvaru, ktoré môžu byť nakreslené ako čiara alebo}
				{@code comm// vyplnené ako plocha; kruhový tvar šplechu v skutočnosti určuje}
				{@code comm// hodnota true parametra „vyplnený“):}
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kruh() kruh}(), {@code valtrue});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_S VK_S}))
			{
				{@code comm// Intenzívne kružnicové šplechutie (pozor, metóda „kružnica“}
				{@code comm// definuje len hranice tvaru, ktoré môžu byť nakreslené ako}
				{@code comm// čiara alebo vyplnené ako plocha; kružnicový tvar šplechu}
				{@code comm// v skutočnosti určuje hodnota false parametra „vyplnený“):}
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kružnica() kružnica}(), {@code valfalse});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_D VK_D}))
			{
				{@code comm// Intenzívne štvorcové šplechnutie na pozícii robota}
				{@code comm// a s veľkosťou robota:}
				vlnenie.{@link #pridajVzruch(Poloha, double) pridajVzruch}({@code valthis}, {@link GRobot#veľkosť() veľkosť}());
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_F VK_F}))
			{
				{@code comm// Intenzívne rozvlnenie na pozícii robota, s veľkosťou robota}
				{@code comm// a s tvarom pyramídy na nedokonalej kruhovej podstave (kláves SHIFT}
				{@code comm// spôsobí, že vlnka zostane zmrazená):}
				vlnenie.{@link #pridajVlnku(Poloha, double, double) pridajVlnku}({@code valthis}, {@link GRobot#veľkosť() veľkosť}(), {@code num765},
					{@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isShiftDown() isShiftDown}());
			}

			{@code comm// Séria X – V: „Krídlové“ šplechnutia – po stranách robota.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_X VK_X}))
			{
				{@code comm// Krídlové šplechnutie v tvare kružnice a kruhu – platí to isté}
				{@code comm// ako pri skratke VK_A – vyššie:}
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kružnica() kružnica}(), {@code valfalse});
				{@link GRobot#posuňVpravo(double) posuňVpravo}({@link GRobot#veľkosť() veľkosť}() * {@code num4});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kruh() kruh}(), {@code valtrue});
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_C VK_C}))
			{
				{@code comm// Krídlové šplechnutie v tvare štvorca ako obrysu aj ako}
				{@code comm// vyplnenej plochy – platí to isté ako pri kreslení kružnice}
				{@code comm// a kruhu – metódy robota určujú len obrys, skutočný spôsob}
				{@code comm// (ne)vyplnenia určuje druhý parameter metódy pridajVzruch:}
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kresliŠtvorec() kresliŠtvorec}(), {@code valfalse});
				{@link GRobot#posuňVpravo(double) posuňVpravo}({@link GRobot#veľkosť() veľkosť}() * {@code num4});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#vyplňŠtvorec() vyplňŠtvorec}(), {@code valtrue});
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_V VK_V}))
			{
				{@code comm// Krídlové šplechnutie v tvare hviezdy ako obrysu aj ako}
				{@code comm// vyplnenej plochy – platí to isté ako pri kreslení kružnice}
				{@code comm// a kruhu – metódy robota určujú len obrys, skutočný spôsob}
				{@code comm// (ne)vyplnenia určuje druhý parameter metódy pridajVzruch:}
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#kresliHviezdu() kresliHviezdu}(), {@code valfalse});
				{@link GRobot#posuňVpravo(double) posuňVpravo}({@link GRobot#veľkosť() veľkosť}() * {@code num4});
				vlnenie.{@link #pridajVzruch(Shape, boolean) pridajVzruch}({@link GRobot#vyplňHviezdu() vyplňHviezdu}(), {@code valtrue});
				{@link GRobot#posuňVľavo(double) posuňVľavo}({@link GRobot#veľkosť() veľkosť}() * {@code num2});
			}

			{@code comm// Séria E – T: Rôzne spôsoby celoplošného rozvlnenia.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_E VK_E}))
			{
				{@code comm// Intenzívne, búrlivé, rozvlnenie obrazu pomocou samého seba}
				{@code comm// zadaného ako mapy vlnenia:}
				vlnenie.{@link #pridajVzruch(Image) pridajVzruch}(predloha);
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_R VK_R}))
			{
				{@code comm// Rozčerenie hladiny náhodným šumom:}
				vlnenie.{@link #rozčerHladinu() rozčerHladinu}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_T VK_T}))
			{
				{@code comm// Pridanie gobálnych pravidelných vlniek hladiny:}
				vlnenie.{@link #rozvlňHladinu() rozvlňHladinu}();
			}

			{@code comm// Dvojica G, H: Zmrazovanie hladiny.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_G VK_G}))
			{
				{@code comm// Zmrazí hviezdu na pozícii robota (ak zmrazenie nastalo}
				{@code comm// v čase vlnenia, okraje zmrazenej oblasti môžu generovať}
				{@code comm// vysokofrekvenčný šum – tento efekt je možné eliminovať}
				{@code comm// funkciou pod skratkou VK_H):}
				vlnenie.{@link #deaktivujHladinu(Shape) deaktivujHladinu}({@link GRobot#hviezda() hviezda}());
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_H VK_H}))
			{
				{@code comm// Izoluje deaktivovanú hladinu, aby nenastávalo vysokofrekvenčné}
				{@code comm// rušenie pri jej okrajoch:}
				vlnenie.{@link #izolujDeaktivovanúHladinu() izolujDeaktivovanúHladinu}();
			}

			{@code comm// Dvojica U, P: Aktivovanie alebo deaktivovanie pomocníkov na}
			{@code comm// vlnenie hladiny.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_U VK_U}))
			{
				{@code comm// Aktivuje robota prechádzajúceho zľava doprava simulujúceho}
				{@code comm// vlnenie vlajky:}
				{@code kwdif} (vlajka.{@link GRobot#aktívny() aktívny}()) vlajka.{@link GRobot#deaktivuj() deaktivuj}(); {@code kwdelse} vlajka.{@link GRobot#aktivuj() aktivuj}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_P VK_P}))
			{
				{@code comm// Aktivuje robota náhodne pridávajúceho vzruchy na hladinu, čím}
				{@code comm// simuluje kvapky dažďa (dopadajúce na hladinu z pohľadu}
				{@code comm// pozorovateľa):}
				{@code kwdif} (dážď.{@link GRobot#aktívny() aktívny}()) dážď.{@link GRobot#deaktivuj() deaktivuj}(); {@code kwdelse} dážď.{@link GRobot#aktivuj() aktivuj}();
			}

			{@code comm// Dvojica I, O: Zmena rozmeru robota.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_I VK_I}))
			{
				{@code comm// Zväčšenie robota.}
				{@link GRobot#veľkosť(double) veľkosť}({@link GRobot#veľkosť() veľkosť}() + {@code num10});
				{@link GRobot#rýchlosť(double) rýchlosť}({@link GRobot#veľkosť() veľkosť}() / {@code num10}, {@code valfalse});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_O VK_O}))
			{
				{@code comm// Zmenšenie robota.}
				{@link GRobot#veľkosť(double) veľkosť}({@link GRobot#veľkosť() veľkosť}() &#45; {@code num10});
				{@link GRobot#rýchlosť(double) rýchlosť}({@link GRobot#veľkosť() veľkosť}() / {@code num10}, {@code valfalse});
			}

			{@code comm// Skupina ovládania robota a celej plochy vlnenia.}

			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#HORE HORE}))
			{
				{@code comm// (Kláves berie do úvahy riadiace klávesy CTRL alebo SHIFT.)}

				{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isControlDown() isControlDown}())
					{@code comm// CTRL – roluje rozvlnenú hladinu hore:}
					vlnenie.{@link #roluj(double, double) roluj}({@code num0}, {@code num10});
				{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isShiftDown() isShiftDown}())
					{@code comm// SHIFT – pretáča rozvlnenú hladinu hore:}
					vlnenie.{@link #pretoč(double, double) pretoč}({@code num0}, {@code num10});
				{@code kwdelse}
					{@code comm// Inak pohne robotom dopredu:}
					{@link GRobot#dopredu() dopredu}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#DOLE DOLE}))
			{
				{@code comm// (Kláves berie do úvahy riadiace klávesy CTRL alebo SHIFT.)}

				{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isControlDown() isControlDown}())
					{@code comm// CTRL – roluje rozvlnenú hladinu dole:}
					vlnenie.{@link #roluj(double, double) roluj}({@code num0}, -{@code num10});
				{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isShiftDown() isShiftDown}())
					{@code comm// SHIFT – pretáča rozvlnenú hladinu dole:}
					vlnenie.{@link #pretoč(double, double) pretoč}({@code num0}, -{@code num10});
				{@code kwdelse}
					{@code comm// Inak pohne robotom vzad:}
					{@link GRobot#vzad() vzad}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VPRAVO VPRAVO}))
			{
				{@code comm// (Kláves berie do úvahy riadiace klávesy CTRL alebo SHIFT.)}

				{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isControlDown() isControlDown}())
					{@code comm// CTRL – roluje rozvlnenú hladinu vpravo:}
					vlnenie.{@link #roluj(double, double) roluj}({@code num10}, {@code num0});
				{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isShiftDown() isShiftDown}())
					{@code comm// SHIFT – pretáča rozvlnenú hladinu vpravo:}
					vlnenie.{@link #pretoč(double, double) pretoč}({@code num10}, {@code num0});
				{@code kwdelse}
					{@code comm// Inak pootočí robota vpravo:}
					{@link GRobot#vpravo() vpravo}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VĽAVO VĽAVO}))
			{
				{@code comm// (Kláves berie do úvahy riadiace klávesy CTRL alebo SHIFT.)}

				{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isControlDown() isControlDown}())
					{@code comm// CTRL – roluje rozvlnenú hladinu vľavo:}
					vlnenie.{@link #roluj(double, double) roluj}(-{@code num10}, {@code num0});
				{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.KeyEvent#isShiftDown() isShiftDown}())
					{@code comm// SHIFT – pretáča rozvlnenú hladinu vľavo:}
					vlnenie.{@link #pretoč(double, double) pretoč}(-{@code num10}, {@code num0});
				{@code kwdelse}
					{@code comm// Inak pootočí robota vľavo:}
					{@link GRobot#vľavo() vľavo}();
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_J VK_J}))
			{
				{@code comm// Vytvorí prekážku vlnenia pri pohybe robot dopredu –}
				{@code comm// „zmrazí čiaru na dráhe pohybu robota“:}
				{@link GRobot#začniCestu() začniCestu}();
				{@link GRobot#dopredu() dopredu}();
				vlnenie.{@link #deaktivujHladinu(Shape) deaktivujHladinu}({@link GRobot#cesta() cesta}(), {@code valfalse});
			}
			{@code kwdelse} {@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#kláves(int) kláves}({@link GRobot.Kláves Kláves}.{@link GRobot.Kláves#VK_N VK_N}))
			{
				{@code comm// Vytvorí prekážku vlnenia pri pohybe robota vzad –}
				{@code comm// „zmrazí čiaru na dráhe pohybu robota“:}
				{@link GRobot#začniCestu() začniCestu}();
				{@link GRobot#vzad() vzad}();
				vlnenie.{@link #deaktivujHladinu(Shape) deaktivujHladinu}({@link GRobot#cesta() cesta}(), {@code valfalse});
			}
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
		{
			{@code comm// Stlačenie tlačidla myši má rovnaký efekt ako ťahanie myšou:}
			{@link GRobot#ťahanieMyšou() ťahanieMyšou}();
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
		{
			{@code kwdif} ({@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link ÚdajeUdalostí#ĽAVÉ ĽAVÉ}))
			{
				{@code comm// Ľavé tlačidlo určí robotovi nový cieľ:}
				{@link GRobot#cieľNaMyš() cieľNaMyš}();
			}
			{@code kwdelse}
			{
				{@code comm// Ostatné tlačidlá pridávajú vzruchy na hladinu:}
				vlnenie.{@link #pridajVzruch(Poloha) pridajVzruch}(
					{@link GRobot.ÚdajeUdalostí ÚdajeUdalostí}.{@link GRobot.ÚdajeUdalostí#polohaMyši() polohaMyši}());
			}
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestovanieVlnenia.cfg"});
			{@code kwdnew} TestovanieVlnenia();
		}
	}
	</pre>
 * 
 * <p><a target="_blank" href="resources/slnecnica.png">slnecnica.png</a> –
 * obrázok slnečnice na prevzatie.</p>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p>Vo výslednom animovanom obrázku sú zostrihané dve akcie. Najprv
 * vytvorenie vlnky na pozícii robota (klávesová skratka <code>F</code>
 * v rámci tohto príkladu) a vyčkanie na upokojenie hladiny. Potom nasleduje
 * prestrih na ukážku, ktorej začiatočný stav bol pripravený zmrazením hviezdy
 * v úplne začiatočnej fáze búrlivého rozvlnenia (skratky <code>E</code>
 * a <code>G</code> v rámci tohto príkladu), presunom na pozíciu mierne vľavo
 * dole pod hviezdou (klikaním myšou) a vyčkaním na úplné upokojenie hladiny
 * (táto prípravná fáza je z animácie vystrihnutá). Z tohto stavu prejde robot
 * krížom popod zmrazenú hviezdu (kliknutím myšou) a vyčká na úplné upokojenie
 * hladiny. Potom sa celý proces zopakuje.</p>
 * 
 * <p><image>vlnenie-ukazka.gif<alt/></image>Ukážka animácií vlnenia
 * <small>(ukážka je zmenšená a mierne zrýchlená)</small>.</p>
 * 
 * <p><b>Poďakovanie a poznámky k implementácii</b></p>
 * 
 * <p>Za pomoc pri implementácii algoritmu v tejto triede vďačím viacerým
 * informačným zdrojom na internete, ktoré mi pomohli získať prehľad
 * o problematike, najviac však <a
 * href="http://www.neilwallis.com/projects/java/water/index.php"
 * target="_blank"><small>Neilovi Wallisovi</small> a jeho článku
 * <em>„Simulate ripples on water“</em> z decembra 2004</a>.</p>
 * 
 * <p>Autor predstavuje algoritmus založený na dvojitom zásobníku pixelov,
 * používanom na vykonávanie výpočtov jednotlivých snímok simulácie vlnenia.
 * Pôvodný algoritmus bol v prvej fáze upravený a prenesený z prostredia
 * Java Appletov do prostredia programovacieho rámca GRobot a bol vylepšený
 * tak, aby vlnenie neprechádzalo vzájomne cez ľavý a pravý okraj rastra.
 * Autor sám tento nedostatok na svojej stránke opisuje, no úmyselne necháva
 * jeho vyriešenie na tých, ktorí sa jeho riešením chcú inšpirovať.</p>
 * 
 * <p>Potom pribudli ďalšie možnosti ako úprava parametrov algoritmu,
 * definovanie rôznych druhov vzruchov, možnosť indikovať pozastavenie
 * činnosti, samostatná simulácia série krokov vlnenia bez viazanosti na
 * časovač a možnosť definovania masky tekutiny, ktorá dovoľuje definovať
 * aktívne a neaktívne oblasti vlnenia. Výsledný produkt vo forme tejto
 * triedy vo všeobecnosti značne rozširuje možnosti použitia pôvodného
 * (jednoduchšieho) algoritmu (v rámci programovacieho rámca GRobot i mimo
 * nej, pretože trieda je navrhnutá a napísaná tak, aby bola použiteľná
 * aj samostatne).</p>
 * 
 * <p><b>Použitý zdroj:</b></p>
 * 
 * <ul><li><a href="http://www.neilwallis.com/projects/java/water/index.php"
 * target="_blank"><small>Wallis, Neil</small>: <em>Simulate ripples on
 * water.</em> December 2004.</a></li></ul>
 */
public class Vlnenie
{
	// Zoznam vlnení – na účely udržania ich aktivity
	/*packagePrivate*/ final static Vector<Vlnenie> vlnenia = new Vector<Vlnenie>();


	// Inštancia náhodného generátora
	private final static Random generátor = new Random();

	// Predvolená farba čiary alebo výplne tvarov kreslených do masky
	private final static Color predvolenáFarba = new Color(0, 0, 0);

	// Predvolená čiara tvarov kreslených do masky
	private final static BasicStroke predvolenáČiara = new BasicStroke(
		6.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	// Inštancia obsahujúca pravidelne aktualizovaný raster zvlnenej
	// verzie zdroja
	private final BufferedImage zvlnenýRaster;

	// Toto je pomocný rastrový obrázok masky slúžiacej pri implementácii
	// viacerých funkcií triedy: pridávanie rastrových a vektorových vzruchov
	// a úprava mapy tekutiny
	private final BufferedImage rasterMasky;

	// Grafika masky na kreslenie do masky
	private final Graphics2D grafikaMasky;

	// Polia rastrových údajov zdroja, cieľa, masky efektov a pomocného
	// zásobníka na pretáčanie údajov masky
	private final int[] údajeZdroja, zvlnenéÚdaje, údajeMasky, pomocnéÚdaje;

	// Celé a polovičné rozmery vlniaceho sa rastra
	private final int šírka, výška, polŠírky, polVýšky;

	// Dvojitý zásobník mapy vĺn (s rezervným rámom veľkým jeden pixel)
	private final short[] mapaVĺn;

	// Mapa pixelov, ktoré majú patriť do vlniacej sa oblasti
	private final byte[] mapaTekutiny;

	// Indexy ukazujúce na začiatočné pozície prvého a druhého zásobníka
	// v mape vĺn
	private int indexA, indexB;

	// Parameter mohutnosti vlnenia (ide o protiklad útlmu, ktorý je
	// zadávaný pri konštrukcii, dá sa upravovať a z ktorého sa zároveň
	// hodnota možnosti vypočíta.
	private int mohutnosť;

	// Príznak aktivity kontrolovaný nadradenou metódou (vypnuté vlnenie by
	// sa nemalo animovať – svet grafického robota to rešpektuje, ale pri
	// použití mimo neho to musí zabepečiť programátor).
	private boolean aktívne;

	// Tieto atribúty majú rovnaký význam ako v inštanciách triedy Obrázok.
	// Posúvajú súradnicový priestor pri kreslení tvarov, čo má význam
	// pri použití tvarov generovaných robotom na vlnenie obrázka s rozmermi
	// rôznymi od rozmerov plátien sveta.
	private double posunX, posunY;

	// Úroveň prahu pri aktivácii a deaktivácii hladiny z masky
	private int úroveňPrahu;

	// Atribúty rolovania masky
	private int Δx, Δy;

	// Hodnota, ktorou má byť vyplnená prázdna časť masky pri rolovaní
	private int prázdnaMaska;

	/**
	 * <p>Toto je konštruktor novej inštancie vlnenia. V rámci programovacieho
	 * rámca je používaný automaticky pri vzniku požiadavky na pridanie efektu
	 * vlnenia k určitej súčasti programovacieho rámca.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V podstate ide o plátna {@linkplain 
	 * GRobot#strop stropu} a {@linkplain GRobot#podlaha podlahy} (pozri {@link 
	 * Plátno#pridajVlnenie() Plátno.pridajVlnenie()}),
	 * o {@linkplain grafickú plochu sveta} ako takého (pozri
	 * {@link Svet#pridajVlnenie() Svet.pridajVlnenie()})
	 * a o {@linkplain Obrázok obrázky programovacieho rámca} (pozri
	 * {@link Obrázok#pridajVlnenie()
	 * Obrázok.pridajVlnenie()}).</p>
	 * 
	 * <p>Trieda {@code currVlnenie} je naprogramovaná tak, aby bola použiteľná
	 * aj mimo programovacieho rámca GRobot. V takom prípade je potrebné vytvoriť
	 * jej inštanciu s pomocou tohto konštruktora. Ten prijíma inštanciu obrázka
	 * {@link BufferedImage BufferedImage}, ktorý musí byť typu
	 * {@link BufferedImage#TYPE_INT_ARGB BufferedImage.TYPE_INT_ARGB}, inak
	 * konštrukcia vlnenia zlyhá. Útlm určuje rýchlosť utlmenia vzruchov na
	 * virtuálnej hladine vlnenia (pozri aj metódu {@link #útlm(int) útlm}).
	 * Ak sú všetky podmienky splnené a vytvorenie vlnenia bolo úspešné,
	 * programátor musí ešte zariadiť pravidelné spúšťanie metódy
	 * {@link #vykonaj() vykonaj} (najlepšie v rekacii časovača), ktorá
	 * zabezpečuje simuláciu vlnenia a tiež prekresľovanie zvlneného rastra,
	 * ktorý získa metódou {@link #zvlnenýRaster() zvlnenýRaster}.</p>
	 * 
	 * <p><small>(<b>Pripomenutie faktov:</b> Animácia vlnenia použitého
	 * v rámci programovacieho rámca je vykonávaná automaticky počas činnosti
	 * časovača a zvlnený raster je v rámci programovacieho rámca tiež použitý
	 * automaticky v čase kreslenia objektu.)</small></p>
	 * 
	 * @param zdroj obraz predlohy, ktorá má byť vlnená, napríklad
	 *     {@linkplain Obrázok obrázok programovacieho rámca GRobot},
	 *     {@linkplain Plátno plátno} a podobne
	 * @param útlm predvolená hodnota útlmu vlnenia (hodnoty v rozsahu 0 – 31;
	 *     pozri aj metódu {@link #útlm(int) útlm})
	 */
	public Vlnenie(BufferedImage zdroj, int útlm)
	{
		šírka = zdroj.getWidth();
		výška = zdroj.getHeight();

		zvlnenýRaster = new BufferedImage(šírka, výška,
			BufferedImage.TYPE_INT_ARGB);
		rasterMasky = new BufferedImage(šírka, výška,
			BufferedImage.TYPE_INT_ARGB);

		grafikaMasky = rasterMasky.createGraphics();
		grafikaMasky.addRenderingHints(Obrázok.hints);

		útlm(útlm);

		polŠírky = šírka >> 1;
		polVýšky = výška >> 1;

		int veľkosť = (šírka + 2) * (výška + 2) * 2;
		mapaVĺn = new short[veľkosť];
		// Upokoj celú hladinu; porovnaj s upokojHladinu()
		Arrays.fill(mapaVĺn, (short)0);

		mapaTekutiny = new byte[šírka * výška];
		// Aktivuj celú hladinu; pozri aj aktivujHladinu()
		Arrays.fill(mapaTekutiny, (byte)255);

		indexA = šírka + 4;
		indexB = (šírka + 2) * (výška + 3) + 1;

		údajeZdroja = ((DataBufferInt)zdroj.
			getRaster().getDataBuffer()).getData();
		zvlnenéÚdaje = ((DataBufferInt)zvlnenýRaster.
			getRaster().getDataBuffer()).getData();
		údajeMasky = ((DataBufferInt)rasterMasky.
			getRaster().getDataBuffer()).getData();
		pomocnéÚdaje = new int[údajeMasky.length];

		System.arraycopy(údajeZdroja, 0,
			zvlnenéÚdaje, 0, zvlnenéÚdaje.length);

		aktívne = false;
		posunX = posunY = 0;
		úroveňPrahu = 24;
	}


	// Prepočíta zadanú horizontálnu súradnicu (x) z kartézskeho do
	// Java 2D grafického priestoru podľa veľkosti vlniaceho sa priestoru
	private int prepočítajX(double x)
	{
		return (int)((šírka / 2.0) + x);
	}

	// Prepočíta zadanú vertikálnu súradnicu (y) z kartézskeho do
	// Java 2D grafického priestoru podľa veľkosti vlniaceho sa priestoru
	private int prepočítajY(double y)
	{
		return (int)((výška / 2.0) - y);
	}


	/**
	 * <p>Overí, či je táto inštancia aktívna, to jest, či má byť vlnenie rastra
	 * automaticky prepočítavané počas udalostí časovača.</p>
	 * 
	 * @return {@code valtrue} – inštancia je aktívna; {@code valfalse} –
	 *     inštancia je neaktívna;
	 * 
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public boolean aktívne() { return aktívne; }

	/** <p><a class="alias"></a> Alias pre {@link #aktívne() aktívne}.</p> */
	public boolean aktivne() { return aktívne; }

	/**
	 * <p>Nastaví príznak aktivity na {@code valtrue}. Tento príznak je
	 * kontrolovaný svetom počas udalostí časovača. V aktivovanej inštancii
	 * je vlnenie automaticky prepočítavané svetom grafického robota.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Aktivácia alebo deaktivácia
	 * inštancie vlnenia nemá vplyv na aktivované a deaktivované časti
	 * hladiny – pozri napríklad metódy {@link #aktivujHladinu(double, double,
	 * int) aktivujHladinu(x, y, rozsah)}, {@link #aktivujHladinu(Shape,
	 * boolean) aktivujHladinu(tvar, vyplnený)}, {@link 
	 * #deaktivujHladinu(double, double, int) deaktivujHladinu(x, y, rozsah)}
	 * alebo {@link #deaktivujHladinu(Shape, boolean) deaktivujHladinu(tvar,
	 * vyplnený)}.</p>
	 * 
	 * @see #aktívne()
	 * @see #deaktivuj()
	 */
	public void aktivuj() { aktívne = true; }

	/**
	 * <p>Nastaví príznak aktivity na {@code valfalse}. Tento príznak je
	 * kontrolovaný svetom počas udalostí časovača. Vlnenie deaktivovanej
	 * inštancie nie je automaticky prepočítavané svetom grafického robota.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto informácia vyžaduje spresnenie.
	 * Deaktivácia neznamená úplné zastavenie prepočtov súvisiacich s vlnením,
	 * len zastavenie prepočtov súvisiacich so zmenami vo vnútorných výškových
	 * mapách vlnenia, čiže s animáciou vlnenia. Ak by boli zastavené všetky
	 * prepočty, znamenalo by to, že keby sa obsah predlohy vlnenia (čiže
	 * pôvodného nezvlneného obrázka) zmenil, tak by sa to vo výslednom
	 * (zvlnenom) rastri nikdy neprejavilo. To nie je cieľom deaktivácie.
	 * Cieľom deaktivácie je len „zamraziť“ vlnenie. V skutočnosti sa táto
	 * trieda správa tak, že vždy pri poskytovaní {@linkplain #zvlnenýRaster()
	 * zvlneného rastra} overí, či je inštancia aktívna a ak nie je, tak
	 * raster aktualizuje bez ohľadu na to, či obsah predlohy bol alebo nebol
	 * zmenený (pretože pri aktívnom vlnení sa to deje automaticky počas
	 * animácie vlnenia).
	 * <br /> <br />
	 * (Preto je v prípade potreby vykonávania viacerých akcií s rastrom
	 * v rámci jedného tiku časovača výhodnejšie poskytnutý raster dočasne
	 * uložiť do lokálnej premennej a pracovať s uloženou verziou. Aby
	 * zbytočne nenastávalo mnohonásobné prepočítavanie zvlneného rastra.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Aktivácia alebo deaktivácia
	 * inštancie vlnenia nemá vplyv na aktivované a deaktivované časti
	 * hladiny – pozri napríklad metódy {@link #aktivujHladinu(double, double,
	 * int) aktivujHladinu(x, y, rozsah)}, {@link #aktivujHladinu(Shape,
	 * boolean) aktivujHladinu(tvar, vyplnený)}, {@link 
	 * #deaktivujHladinu(double, double, int) deaktivujHladinu(x, y, rozsah)}
	 * alebo {@link #deaktivujHladinu(Shape, boolean) deaktivujHladinu(tvar,
	 * vyplnený)}.</p>
	 * 
	 * @see #aktívne()
	 * @see #aktivuj()
	 */
	public void deaktivuj() { aktívne = false; }


	/**
	 * <p>Zistí aktuálnu úroveň prahu používaného pri aktivácii a deaktivácii
	 * hladiny s pomocou masky, čo zahŕňa aj aktiváciu alebo deaktiváciu
	 * s použitím tvarov. Prah stanovuje hodnotu, pod ktorou už body masky
	 * nie sú brané do úvahy. Predvolená hodnota je {@code num24}.</p>
	 * 
	 * @return aktuálna úroveň prahu používaného pri aktivácii a deaktivácii
	 *     hladiny s pomocou masky
	 * 
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public int úroveňPrahu()
	{
		return úroveňPrahu;
	}

	/** <p><a class="alias"></a> Alias pre {@link #úroveňPrahu() úroveňPrahu}.</p> */
	public int urovenPrahu()
	{
		return úroveňPrahu;
	}

	/**
	 * <p>Určí novú úroveň prahu používaného pri aktivácii a deaktivácii hladiny
	 * s pomocou masky, čo zahŕňa aj aktiváciu alebo deaktiváciu s použitím
	 * tvarov. Prah stanoví hodnotu, pod ktorou už body masky nebudú brané do
	 * úvahy. <small>(Predvolená hodnota je {@code num24}.)</small>
	 * 
	 * @param úroveň nová úroveň prahu používaného pri aktivácii a deaktivácii
	 *     hladiny s pomocou masky
	 * 
	 * @see #úroveňPrahu()
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void úroveňPrahu(int úroveň)
	{
		úroveňPrahu = úroveň;
	}

	/** <p><a class="alias"></a> Alias pre {@link #úroveňPrahu(int) úroveňPrahu}.</p> */
	public void urovenPrahu(int úroveň)
	{
		úroveňPrahu = úroveň;
	}


	/**
	 * <p>Aktivuje hladinu na celej ploche vlniaceho sa obrázka. (Zruší všetky
	 * zmeny v súvislosti s deaktivovaním častí vlniacej sa plochy.)</p>
	 * 
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 * @see #aktivujHladinu(Image)
	 */
	public void aktivujHladinu()
	{
		Arrays.fill(mapaTekutiny, (byte)255);
	}

	/**
	 * <p>Aktivuje bod, resp. malú štvorcovú oblasť s veľkosťou 6 × 6 bodov,
	 * na hladine so stredom na určenej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void aktivujHladinu(double x, double y)
	{
		aktivujHladinu(x, y, 3);
	}

	/**
	 * <p>Aktivuje štvorcovú oblasť na hladine so zadaným rozsahom (rozsah určuje
	 * polovicu strany štvorca) so stredom na určenej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsah rozsah štvorcovej oblasti (polomer vpísanej kružnice)
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void aktivujHladinu(double x, double y, int rozsah)
	{
		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + rozsah - 1, uy = ly + rozsah - 1;
		lx -= rozsah; ly -= rozsah;

		for (int j = ly; j <= uy; ++j)
			for (int k = lx; k <= ux; ++k)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					mapaTekutiny[(j * šírka) + k] = (byte)255;
	}

	/**
	 * <p>Aktivuje bod, resp. malú štvorcovú oblasť s veľkosťou 6 × 6 bodov,
	 * na hladine so stredom na určenej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void aktivujHladinu(Poloha poloha)
	{
		aktivujHladinu(poloha.polohaX(), poloha.polohaY(), 3);
	}

	/**
	 * <p>Aktivuje štvorcovú oblasť na hladine so zadaným rozsahom (rozsah
	 * určuje polovicu strany štvorca) so stredom na určenej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsah rozsah štvorcovej oblasti (polomer vpísanej kružnice)
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void aktivujHladinu(Poloha poloha, int rozsah)
	{
		aktivujHladinu(poloha.polohaX(), poloha.polohaY(), rozsah);
	}


	// Aktivuje hladinu podľa údajov z masky, do ktorej môžu byť vložené
	// z rôznych zdrojov.
	private void aktivujHladinuZMasky()
	{
		for (int y = 0, i = 0; y < výška; ++y)
			for (int x = 0; x < šírka; ++x, ++i)
			{
				int maska = údajeMasky[i];

				// Úplne biela farba s ľubovoľnou úrovňou priehľadnosti
				// a všetky úplne priehľadné farby a môžu byť bez rizika
				// ignorované.
				if (0 != (maska & 0xff000000) &&
					0xffffff != (maska & 0xffffff))
				{
					// Výpočet prahu má overiť, či je úroveň signálu na
					// určitom mieste v obrázku dostatočná.
					int prah = 765;

					// — červená zložka (Red):
					prah -= (maska >> 16) & 0xff;
					// — zelená zložka (Green):
					prah -= (maska >>  8) & 0xff;
					// — modrá zložka (Blue):
					prah -=  maska        & 0xff;

					// Vypočítaná úroveň prahu je dodatočne upravená
					// úrovňou zložky priehľadnosti (Alpha) v prípade,
					// že táto zložka nie je na maxime:
					if (0xff000000 != (maska & 0xff000000))
					{
						// — krát maximum priehľadnosti (255):
						prah *= 0xff;
						// — deleno skutočná úroveň priehľadnosti:
						prah /= (maska >> 24) & 0xff;
					}

					// Ak je úroveň prahu dostatočná, tak je hladina
					// v tomto bode aktivovaná:
					if (prah > úroveňPrahu) mapaTekutiny[i] = (byte)255;
				}
			}
	}


	/**
	 * <p>Aktivuje hladinu podľa zadaného obrázka. Ak obrázok nemá zhodné rozmery
	 * s rozmermi máp vlnenia, bude použitý tak, ako keby bol nakreslený
	 * v strede mapy vlnenia (ak je väčší, nadbytočné časti budú ignorované,
	 * ak je menší, chýbajúce časti budú považované za prázdne). Jednotlivé
	 * body máp vlnenia budú aktivované podľa toho, či hodnota vypočítaná
	 * z jasu a priehľadnosti bodu na zadanom obrázku prekročí aktuálnu
	 * {@linkplain #úroveňPrahu(int) úroveň prahu} inštancie vlnenia.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na aktiváciu hladiny podľa aktuálnej
	 *     {@linkplain #úroveňPrahu(int) úrovne prahu}
	 * 
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 * @see #aktivujHladinu()
	 * @see #deaktivujHladinu()
	 */
	public void aktivujHladinu(Image obrázok)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		aktivujHladinuZMasky();
	}

	/**
	 * <p>Aktivuje body máp vlnenia podľa zadaného tvaru. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #aktivujHladinu(Image) aktivujHladinu(obrázok)}. Preto je
	 * aj pre túto metódu relevantný {@linkplain #úroveňPrahu(int) prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude aktivovaná prislúchajúca časť
	 *     hladiny
	 * 
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void aktivujHladinu(Shape tvar)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		aktivujHladinuZMasky();
	}

	/**
	 * <p>Aktivuje body máp vlnenia podľa zadaného tvaru. Tvar bude použitý ako
	 * vyplnený alebo obkreslený čiarou podľa hodnoty druhého parametra. Pri
	 * nevyplnených tvaroch je použitá predvolená hrúbka čiary {@code num6.0}
	 * bodov. Táto metóda funguje tak, že zadaný tvar nakreslí (vyplní alebo
	 * obkreslí) do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #aktivujHladinu(Image)
	 * aktivujHladinu(obrázok)}. Preto je aj pre túto metódu relevantný
	 * {@linkplain #úroveňPrahu(int) prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude aktivovaná prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako obreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * 
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void aktivujHladinu(Shape tvar, boolean vyplnený)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		aktivujHladinuZMasky();
	}

	/**
	 * <p>Aktivuje body máp vlnenia podľa zadaného tvaru. Tvar bude použitý ako
	 * obkreslená čiara s hrúbkou podľa hodnoty druhého parametra. Táto
	 * metóda funguje tak, že zadaný tvar nakreslí (obkreslí) so zadanou
	 * hrúbkou čiary do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #aktivujHladinu(Image)
	 * aktivujHladinu(obrázok)}. Preto je aj pre túto metódu relevantný
	 * {@linkplain #úroveňPrahu(int) prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude aktivovaná prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * 
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void aktivujHladinu(Shape tvar, double hrúbkaČiary)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		aktivujHladinuZMasky();
	}


	/**
	 * <p>Deaktivuje celú plochu hladiny vlniaceho sa obrázka. Je to použiteľné
	 * na inverzné povoľovanie vlniacich sa oblastí – najprv deaktivujeme
	 * celú hladinu a následne aktivujeme len tie časti, ktoré chceme, aby
	 * sa vlnili.</p>
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 * @see #aktivujHladinu(Image)
	 */
	public void deaktivujHladinu()
	{
		Arrays.fill(mapaTekutiny, (byte)0);
	}

	/**
	 * <p>Deaktivuje bod, resp. malú štvorcovú oblasť s veľkosťou 6 × 6 bodov,
	 * na hladine so stredom na určenej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void deaktivujHladinu(double x, double y)
	{
		deaktivujHladinu(x, y, 3);
	}

	/**
	 * <p>Deaktivuje štvorcovú oblasť na hladine so zadaným rozsahom (rozsah
	 * určuje polovicu strany štvorca) so stredom na určenej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsah rozsah štvorcovej oblasti (polomer vpísanej kružnice)
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void deaktivujHladinu(double x, double y, int rozsah)
	{
		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + rozsah - 1, uy = ly + rozsah - 1;
		lx -= rozsah; ly -= rozsah;

		for (int j = ly; j <= uy; ++j)
			for (int k = lx; k <= ux; ++k)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
				{
					mapaTekutiny[(j * šírka) + k] = 0;
					mapaVĺn[indexA + (j * (šírka + 2)) + k] =
						mapaVĺn[indexB + (j * (šírka + 2)) + k];
				}
	}

	/**
	 * <p>Deaktivuje bod, resp. malú štvorcovú oblasť s veľkosťou 6 × 6 bodov,
	 * na hladine so stredom na určenej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha, int)
	 */
	public void deaktivujHladinu(Poloha poloha)
	{
		deaktivujHladinu(poloha.polohaX(), poloha.polohaY(), 3);
	}

	/**
	 * <p>Deaktivuje štvorcovú oblasť na hladine so zadaným rozsahom (rozsah
	 * určuje polovicu strany štvorca) so stredom na určenej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsah rozsah štvorcovej oblasti (polomer vpísanej kružnice)
	 * 
	 * @see #aktivujHladinu()
	 * @see #aktivujHladinu(double, double)
	 * @see #aktivujHladinu(double, double, int)
	 * @see #aktivujHladinu(Poloha)
	 * @see #aktivujHladinu(Poloha, int)
	 * @see #deaktivujHladinu()
	 * @see #deaktivujHladinu(double, double)
	 * @see #deaktivujHladinu(double, double, int)
	 * @see #deaktivujHladinu(Poloha)
	 */
	public void deaktivujHladinu(Poloha poloha, int rozsah)
	{
		deaktivujHladinu(poloha.polohaX(), poloha.polohaY(), rozsah);
	}


	// Deaktivuje hladinu podľa údajov z masky, do ktorej môžu byť vložené
	// z rôznych zdrojov.
	private void deaktivujHladinuZMasky()
	{
		for (int y = 0, i = 0, j = indexA, k = indexB;
			y < výška; ++y, j += 2, k += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j, ++k)
			{
				int maska = údajeMasky[i];

				// Úplne biela farba s ľubovoľnou úrovňou priehľadnosti
				// a všetky úplne priehľadné farby a môžu byť bez rizika
				// ignorované.
				if (0 != (maska & 0xff000000) &&
					0xffffff != (maska & 0xffffff))
				{
					// Výpočet prahu má overiť, či je úroveň signálu na
					// určitom mieste v obrázku dostatočná.
					int prah = 765;

					// — červená zložka (Red):
					prah -= (maska >> 16) & 0xff;
					// — zelená zložka (Green):
					prah -= (maska >>  8) & 0xff;
					// — modrá zložka (Blue):
					prah -=  maska        & 0xff;

					// Vypočítaná úroveň prahu je dodatočne upravená
					// úrovňou zložky priehľadnosti (Alpha) v prípade,
					// že táto zložka nie je na maxime:
					if (0xff000000 != (maska & 0xff000000))
					{
						// — krát maximum priehľadnosti (255):
						prah *= 0xff;
						// — deleno skutočná úroveň priehľadnosti:
						prah /= (maska >> 24) & 0xff;
					}

					// Ak je úroveň prahu dostatočná, tak je hladina
					// v tomto bode deaktivovaná:
					if (prah > úroveňPrahu)
					{
						mapaTekutiny[i] = 0;
						mapaVĺn[j] = mapaVĺn[k];
					}
				}
			}
	}


	/**
	 * <p>Deaktivuje hladinu podľa zadaného obrázka. Ak obrázok nemá zhodné
	 * rozmery s rozmermi máp vlnenia, bude použitý tak, ako keby bol
	 * nakreslený v strede mapy vlnenia (ak je väčší, nadbytočné časti budú
	 * ignorované, ak je menší, chýbajúce časti budú považované za prázdne).
	 * Jednotlivé body máp vlnenia budú deaktivované podľa toho, či hodnota
	 * vypočítaná z jasu a priehľadnosti bodu na zadanom obrázku prekročí
	 * aktuálnu {@linkplain #úroveňPrahu(int) úroveň prahu} inštancie vlnenia.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na deaktiváciu hladiny podľa aktuálnej
	 *     {@linkplain #úroveňPrahu(int) úrovne prahu}
	 * 
	 * @see #izolujDeaktivovanúHladinu()
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void deaktivujHladinu(Image obrázok)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		deaktivujHladinuZMasky();
	}

	/**
	 * <p>Deaktivuje body máp vlnenia podľa zadaného tvaru. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #deaktivujHladinu(Image) deaktivujHladinu(obrázok)}.
	 * Preto je aj pre túto metódu relevantný {@linkplain #úroveňPrahu(int)
	 * prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude deaktivovaná prislúchajúca časť
	 *     hladiny
	 * 
	 * @see #izolujDeaktivovanúHladinu()
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void deaktivujHladinu(Shape tvar)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		deaktivujHladinuZMasky();
	}

	/**
	 * <p>Deaktivuje body máp vlnenia podľa zadaného tvaru. Tvar bude použitý ako
	 * vyplnený alebo obkreslený čiarou podľa hodnoty druhého parametra. Pri
	 * nevyplnených tvaroch je použitá predvolená hrúbka čiary {@code num6.0}
	 * bodov. Táto metóda funguje tak, že zadaný tvar nakreslí (vyplní alebo
	 * obkreslí) do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #deaktivujHladinu(Image)
	 * deaktivujHladinu(obrázok)}. Preto je aj pre túto metódu relevantný
	 * {@linkplain #úroveňPrahu(int) prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude deaktivovaná prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako obreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * 
	 * @see #izolujDeaktivovanúHladinu()
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void deaktivujHladinu(Shape tvar, boolean vyplnený)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		deaktivujHladinuZMasky();
	}

	/**
	 * <p>Deaktivuje body máp vlnenia podľa zadaného tvaru. Tvar bude použitý
	 * ako obkreslená čiara s hrúbkou podľa hodnoty druhého parametra. Táto
	 * metóda funguje tak, že zadaný tvar nakreslí (obkreslí) so zadanou
	 * hrúbkou čiary do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #deaktivujHladinu(Image)
	 * deaktivujHladinu(obrázok)}. Preto je aj pre túto metódu relevantný
	 * {@linkplain #úroveňPrahu(int) prah}.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude deaktivovaná prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * 
	 * @see #izolujDeaktivovanúHladinu()
	 * @see #úroveňPrahu()
	 * @see #úroveňPrahu(int)
	 * @see #aktivujHladinu(Image)
	 * @see #aktivujHladinu(Shape)
	 * @see #aktivujHladinu(Shape, boolean)
	 * @see #aktivujHladinu(Shape, double)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 */
	public void deaktivujHladinu(Shape tvar, double hrúbkaČiary)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		deaktivujHladinuZMasky();
	}


	/**
	 * <p>Izoluje oblasti deaktivovanej hladiny. Ak bola časť hladiny
	 * deaktivovaná počas „živej“ animácie vlnenia, to jest v stave, keď
	 * výškové mapy vlnenia obsahujú rozdielne údaje, bez ohľadu na to, či je
	 * inštancia vlnenia aktívna alebo nie, tak takmer určite vznikne na
	 * hraniciach zmrazenej oblasti stav, ktorý bude počas aktívnej animácie
	 * spôsobovať vznik vysokofrekvenčného vlnenia (hoci s malým dosahom).
	 * Tento jav nemusí byť vždy vnímaný ako negatívny, preto nie je
	 * potláčaný automaticky. Táto metóda ho však dokáže eliminovať, no dokáže
	 * tak urobiť len celoplošne. (To znamená, že ak by pre niektoré časti
	 * hladiny mal byť tento jav žiaduci, tak tie musia byť zmrazené
	 * dodatočne, po spustení tejto metódy.)</p>
	 * 
	 * @see #úroveňPrahu(int)
	 * @see #deaktivujHladinu(Image)
	 * @see #deaktivujHladinu(Shape)
	 * @see #deaktivujHladinu(Shape, boolean)
	 * @see #deaktivujHladinu(Shape, double)
	 */
	public void izolujDeaktivovanúHladinu()
	{
		// Potrebujeme ďalší zásobník, do ktorého by sa dala preniesť mapa
		// izolovanej hladiny – využijeme masku:

		for (int y = 0, i = 0; y < výška; ++y)
			for (int x = 0; x < šírka; ++x, ++i)
				údajeMasky[i] = mapaTekutiny[i];

		for (int y = 0, i = 0, j = indexA, k = indexB;
			y < výška; ++y, j += 2, k += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j, ++k)
			{
				// Nezmrazené okolia tých bodov, ktoré sú zmrazené
				// zmrazíme a vynulujeme im mapu vĺn:
				if (0 == údajeMasky[i])
				{
					// Vľavo:
					if (x - 1 >= 0 && 0 != mapaTekutiny[i - 1])
					{
						mapaTekutiny[i - 1] = 0;
						mapaVĺn[j - 1] = mapaVĺn[k - 1] = 0;
					}

					// Vpravo:
					if (x + 1 < šírka && 0 != mapaTekutiny[i + 1])
					{
						mapaTekutiny[i + 1] = 0;
						mapaVĺn[j + 1] = mapaVĺn[k + 1] = 0;
					}

					// Hore:
					if (y - 1 >= 0 && 0 != mapaTekutiny[i - šírka])
					{
						mapaTekutiny[i - šírka] = 0;
						mapaVĺn[j - šírka - 2] = mapaVĺn[k - šírka - 2] = 0;
					}

					// Dole:
					if (y + 1 < výška && 0 != mapaTekutiny[i + šírka])
					{
						mapaTekutiny[i + šírka] = 0;
						mapaVĺn[j + šírka + 2] = mapaVĺn[k + šírka + 2] = 0;
					}
				}
			}
	}

	/** <p><a class="alias"></a> Alias pre {@link #izolujDeaktivovanúHladinu() izolujDeaktivovanúHladinu}.</p> */
	public void izolujDeaktivovanuHladinu()
	{
		izolujDeaktivovanúHladinu();
	}


	/**
	 * <p>Vráti posun v smere horizontálnej osi (osi x). Pre podrobnosti pozri
	 * opis metódy {@link #posun(double, double) posun(x, y)}.</p>
	 * 
	 * @return posun v smere horizontálnej osi – osi x
	 * 
	 * @see #posun(double, double)
	 * @see #posunY()
	 */
	public double posunX() { return posunX; }

	/**
	 * <p>Vráti posun v smere vertikálnej osi (osi y). Pre podrobnosti pozri
	 * opis metódy {@link #posun(double, double) posun(x, y)}.</p>
	 * 
	 * @return posun v smere vertikálnej osi – osi y
	 * 
	 * @see #posun(double, double)
	 * @see #posunX()
	 */
	public double posunY() { return posunY; }

	/**
	 * <p>Nastaví úroveň posunu v osiach x a y, o ktorú budú posunuté tvary
	 * Javy pri ich použití na definovanie tekutiny alebo vzruchov. Tento
	 * posun má význam pri použití tvarov generovaných robotom na vlniace
	 * sa plochy, ktoré majú rôzne rozmery než sú rozmery plátien sveta.
	 * (Ide najmä o inštancie vlnenia obrázkov.) Je to preto, lebo tvary
	 * generované robotom sú síce vygenerované v súradnicovom priestore
	 * Javy, ale vzhľadom na rozmery plátien sveta a pri použití v obrázkoch
	 * s inými rozmermi, by neboli správne umiestnené.</p>
	 * 
	 * <p><b>V rámci programovacieho rámca GRobot</b> je posun inštancií
	 * vlnenia <b>obrázkov</b> upravený <b>automaticky</b>. <small>(Iné
	 * inštancie vlnenia používané v rámci programovacieho rámca nemusia mať
	 * posun upravovaný. V podstate ide už len o plátna a samotnú grafickú
	 * plochu sveta.)</small></p>
	 * 
	 * <p>Posun má význam pri používaní vlnenia siahajúc za hranice
	 * programovacieho rámca.</p>
	 * 
	 * @param x posun v smere horizontálnej osi – osi x
	 * @param y posun v smere vertikálnej osi – osi y
	 * 
	 * @see #posunX()
	 * @see #posunY()
	 */
	public void posun(double x, double y)
	{
		posunX = x;
		posunY = y;
	}


	/**
	 * <p>Vráti aktuálnu hodnotu útlmu vĺn tejto inštancie vlnenia.</p>
	 * 
	 * @return aktuálna hodnota útlmu vĺn (hodnoty v rozsahu 0 – 31)
	 */
	public int útlm() { return 31 - mohutnosť; }

	/** <p><a class="alias"></a> Alias pre {@link #útlm() útlm}.</p> */
	public int utlm() { return útlm(); }

	/**
	 * <p>Nastaví novú hodnotu útlmu vĺn tejto inštancie vlnenia. Povolené sú
	 * hodnoty v rozsahu 0 – 31 a všetky hodnoty mimo tohto rozsahu budú
	 * posunuté na najbližšiu platnú hranicu. Nulový útlm neznamená
	 * nekonečné vlnenie, ale to, že vlny odznejú tak, ako je to prirodzené
	 * pre implementovaný algoritmus vlnenia. Horná hranica určuje opačný
	 * extrém. V podstate majú v skutočnosti zmysel len hodnoty v rozsahu
	 * 0 – 30, pretože útlm s hodnotou 31 znamená, že vlny budú okamžite
	 * eliminované, takže vo výsledku nebude viditeľné žiadne vlnenie obrazu.</p>
	 * 
	 * @param útlm nová hodnota útlmu vĺn (odporúčané hodnoty: 0 – 30)
	 */
	public void útlm(int útlm)
	{
		if (útlm > 31) útlm = 31;
		if (útlm < 0) útlm = 0;
		mohutnosť = 31 - útlm;
	}

	/** <p><a class="alias"></a> Alias pre {@link #útlm(int) útlm}.</p> */
	public void utlm(int útlm) { útlm(útlm); }


	/**
	 * <p>Upokojí hladinu vlnenia – odoberie všetky vzruchy.</p>
	 */
	public void upokojHladinu()
	{
		for (int y = 0, i = 0, j = indexA, k = indexB;
			y < výška; ++y, j += 2, k += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j, ++k)
				if (0 != mapaTekutiny[i])
				{
					mapaVĺn[j] = (short)0;
					mapaVĺn[k] = (short)0;
				}
	}

	/**
	 * <p>Pridá náhodný šum k úrovniam hladiny vlnenia – pridá náhodné vzruchy.</p>
	 */
	public void rozčerHladinu()
	{
		rozčerHladinu((short)-32, (short)32);
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozčerHladinu() rozčerHladinu}.</p> */
	public void rozcerHladinu()
	{
		rozčerHladinu((short)-32, (short)32);
	}

	/**
	 * <p>Pridá náhodný šum k úrovniam hladiny vlnenia – pridá náhodné vzruchy.</p>
	 * 
	 * @param spodnáHranicaŠumu najnižšia možná vygenerovaná náhodná hodnota,
	 *     ktorá bude pridaná k hladine vlnenia (odporúčaná je záporná hodnota
	 *     hornej hranice šumu)
	 * @param hornáHranicaŠumu najvyššia možná vygenerovaná náhodná hodnota,
	 *     ktorá bude pridaná k hladine vlnenia (odporúčané sú hodnoty
	 *     v rozmedzí {@code num30} – {@code num70})
	 */
	public void rozčerHladinu(short spodnáHranicaŠumu,
		short hornáHranicaŠumu)
	{
		if (spodnáHranicaŠumu > hornáHranicaŠumu)
		{
			short vymeň = spodnáHranicaŠumu;
			spodnáHranicaŠumu = hornáHranicaŠumu;
			hornáHranicaŠumu = vymeň;
		}

		for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
				if (0 != mapaTekutiny[i])
					mapaVĺn[j] += spodnáHranicaŠumu +
						(short)(Math.abs(generátor.nextInt()) % (1 +
							hornáHranicaŠumu - spodnáHranicaŠumu));
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozčerHladinu(short, short) rozčerHladinu}.</p> */
	public void rozcerHladinu(short spodnáHranicaŠumu,
		short hornáHranicaŠumu)
	{
		rozčerHladinu(spodnáHranicaŠumu, hornáHranicaŠumu);
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozčerHladinu(short, short) rozčerHladinu}.</p> */
	public void rozčerHladinu(int spodnáHranicaŠumu,
		int hornáHranicaŠumu)
	{
		rozčerHladinu((short)spodnáHranicaŠumu, (short)hornáHranicaŠumu);
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozčerHladinu(short, short) rozčerHladinu}.</p> */
	public void rozcerHladinu(int spodnáHranicaŠumu,
		int hornáHranicaŠumu)
	{
		rozčerHladinu((short)spodnáHranicaŠumu, (short)hornáHranicaŠumu);
	}


	/**
	 * <p>Pridá pravidelné jemné vlnenie po celej ploche hladiny s predvolenou
	 * dĺžkou vlny {@code num25.0} a predvolenou amplitúdou {@code num1.0}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Vlnenie bude utlmené relatívne rýchlo,
	 * preto musí byť v prípade požadovania jeho stálej prítomnosti
	 * pravidelne obnovované.</p>
	 * 
	 * @see #rozvlňHladinu(double)
	 * @see #rozvlňHladinu(double, double)
	 */
	public void rozvlňHladinu()
	{
		rozvlňHladinu(25.0, 1.0);
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozvlňHladinu() rozvlňHladinu}.</p> */
	public void rozvlnHladinu()
	{
		rozvlňHladinu(25.0, 1.0);
	}

	/**
	 * <p>Pridá pravidelné jemné vlnenie po celej ploche hladiny so zadanou
	 * dĺžkou vlny a predvolenou amplitúdou {@code num1.0}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Vlnenie bude utlmené relatívne rýchlo,
	 * preto musí byť v prípade požadovania jeho stálej prítomnosti
	 * pravidelne obnovované.</p>
	 * 
	 * @param dĺžkaVlny požadovaná dĺžka vlny pridávaného plošného vlnenia
	 * 
	 * @see #rozvlňHladinu()
	 * @see #rozvlňHladinu(double, double)
	 */
	public void rozvlňHladinu(double dĺžkaVlny)
	{
		rozvlňHladinu(dĺžkaVlny, 1.0);
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozvlňHladinu(double) rozvlňHladinu}.</p> */
	public void rozvlnHladinu(double dĺžkaVlny)
	{
		rozvlňHladinu(dĺžkaVlny, 1.0);
	}

	/**
	 * <p>Pridá pravidelné vlnenie po celej ploche hladiny so zadanou dĺžkou
	 * vlny a amplitúdou.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Vlnenie bude časom utlmené,
	 * preto musí byť v prípade požadovania jeho stálej prítomnosti
	 * pravidelne obnovované.</p>
	 * 
	 * @param dĺžkaVlny požadovaná dĺžka vlny pridávaného plošného vlnenia
	 * @param amplitúda požadovaná amplitúda pridávaného plošného vlnenia
	 * 
	 * @see #rozvlňHladinu()
	 * @see #rozvlňHladinu(double)
	 */
	public void rozvlňHladinu(double dĺžkaVlny, double amplitúda)
	{
		int polvlna = (int)(dĺžkaVlny / 2.0);
		if (0 == polvlna) polvlna = 1;
		int vlna = polvlna * 2;

		for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
		{
			for (int x = 0; x < šírka; ++x, ++i, ++j)
				if (0 != mapaTekutiny[i])
				{
					double úroveň;

					if (0 == ((x / vlna) + (y / vlna)) % 2)
						úroveň = (vlna - (x % vlna) - polvlna) *
							((y % vlna) - polvlna);
					else
						úroveň = ((x % vlna) - polvlna) *
							((y % vlna) - polvlna);

					úroveň /= (double)polvlna;
					úroveň *= amplitúda;

					/* Neúspech:
					int úroveň = x * y;
					int úroveň1 = (amplitúda * ((úroveň % vlna) >= polvlna ?
						polvlna - úroveň % polvlna : úroveň % polvlna)) -
						polamplitúda;
					úroveň = (polvlna + x) * (polvlna + y);
					int úroveň2 = (amplitúda * ((úroveň % vlna) >= polvlna ?
						polvlna - úroveň % polvlna : úroveň % polvlna)) -
						polamplitúda;
					úroveň = úroveň1 + úroveň2;
					*/

					mapaVĺn[j] += (int)úroveň;
				}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozvlňHladinu(double, double) rozvlňHladinu}.</p> */
	public void rozvlnHladinu(double dĺžkaVlny, double amplitúda)
	{
		rozvlňHladinu(dĺžkaVlny, amplitúda);
	}


	/**
	 * <p>Táto metóda pridá oblasť vlnky so zadaným polomerom a intenzitou na
	 * zadanej pozícii, pričom ju zároveň umožní nechať zmrazenú. Oblasť
	 * „vlnky“ by v priestore vyzerala ako pyramída, ktorej podstava je
	 * zabrúsená do tvaru nedokonalého kruhu. Tento tvar je výsledkom úsilia
	 * o zachovanie čo najmenšej výpočtovej náročnosti – v algoritme sú
	 * v dvojnásobnom cykle použité len základné aritmetické operácie
	 * a jednoduché vetvenie. Tvar podstavca s pyramídou pri pohľade zvrchu
	 * názornejšie ukazujú nasledujúce obrázky (všetky tri zobrazujú tú istú
	 * výškovú mapu vlnky, len sú vyhotovené v troch farebných odtieňoch):</p>
	 * 
	 * <table class="centered">
	 * <tr>
	 * <td><image>vlnenie-vlnka-zvrchu-00.png<alt/></image>Čiernobiele
	 * zobrazenie<br />výškovej mapy vlnky.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zvrchu-01.png<alt/></image>Žltočervené
	 * zobrazenie<br />výškovej mapy vlnky.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zvrchu-02.png<alt/></image>Modrozelené
	 * zobrazenie<br />výškovej mapy vlnky.</td>
	 * </tr>
	 * <tr><td colspan="5"><p class="image">(Polomer vlnky bol 100 bodov;
	 * najvyššia intenzita je v strede.)</p></td></tr>
	 * </table>
	 * 
	 * <p>Výsledná maximálna intenzita vlnky je závislá ako od parametra
	 * {@code násobokIntenzity}, tak od polomeru, pretože čím väčší je zadaný
	 * polomer, tým vyššia bude pyramída. Nasledujúca séria obrázkov ukazuje
	 * nárast intenzity v závislosti od zväčšovania polomeru pri zachovaní
	 * rovnakej hodnoty parametra {@code násobokIntenzity}:</p>
	 * 
	 * <table class="centered">
	 * <tr>
	 * <td><image>vlnenie-vlnka-zboku-00.png<alt/></image>Polomer: 20 bodov.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zboku-01.png<alt/></image>Polomer: 40 bodov.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zboku-02.png<alt/></image>Polomer: 60 bodov.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zboku-03.png<alt/></image>Polomer: 80 bodov.</td>
	 * <td> </td>
	 * <td><image>vlnenie-vlnka-zboku-04.png<alt/></image>Polomer: 100 bodov.</td>
	 * </tr>
	 * </table>
	 * 
	 * <p>Ak má parameter {@code zmraz} hodnotu {@code valtrue}, tak budú
	 * všetky tie časti hladiny, ktoré boli touto metódou ovplyvnené, zároveň
	 * zmrazené. Pozor, metóda nemôže ovplyvniť alebo zmraziť už zmrazené
	 * časti hladiny!</p>
	 * 
	 * <p>Metóda má viacero klonov, ktoré umožňujú jej jednoduchšie použitie…</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(double, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, int, double, boolean)
	 */
	public void pridajVlnku(double x, double y,
		int polomer, double násobokIntenzity, boolean zmraz)
	{
		// Bolo to komplikované a chcel som vyrobiť niečo iné, ale keď už sa
		// podarilo toto, tak som to tu nechal…

		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + polomer - 1, uy = ly + polomer - 1;
		lx -= polomer; ly -= polomer;

		int polvlna = polomer / 2;
		if (0 == polvlna) polvlna = 1;
		int vlna = polvlna * 2;
		// int minimálnaÚroveň = polvlna; //
		int minimálnaÚroveň = (polomer / 10) * polvlna;


		// Zvyšky rôznych pokusov:
		// for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			// for (int x = 0; x < šírka; ++x, ++i, ++j)
				// if (0 != mapaTekutiny[i]) {}
					// && (j == ly || j == uy || k == lx || k == ux)
					// && (0 == (j + k) % 2)


		for (int j = ly, m = 0; j <= uy; ++j, ++m)
			for (int k = lx, n = 0; k <= ux; ++k, ++n)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
					{
						double úroveň;

						int o = (m / 2) + polvlna;
						int p = (n / 2) + polvlna;

						if (0 == ((p / vlna) + (o / vlna)) % 2)
							úroveň = ((p % vlna) - polvlna) *
								((o % vlna) - polvlna);
						else
							úroveň = (vlna - (p % vlna) - polvlna) *
								((o % vlna) - polvlna);

						if (úroveň > minimálnaÚroveň)
						{
							úroveň /= (double)polvlna;
							úroveň *= násobokIntenzity;

							mapaVĺn[indexA + (j *
								(šírka + 2)) + k] += (int)úroveň;
							mapaVĺn[indexB + (j *
								(šírka + 2)) + k] += (int)úroveň;

							if (zmraz) mapaTekutiny[(j * šírka) + k] = 0;
						}
					}
	}


	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(double, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 */
	public void pridajVlnku(double x, double y,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň umožňuje zadať polohu stredu oblasti ako jeden parameter
	 * {@code poloha}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(double, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 */
	public void pridajVlnku(Poloha poloha,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		pridajVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(double, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 */
	public void pridajVlnku(double x, double y,
		double polomer, double násobokIntenzity)
	{
		pridajVlnku(x, y, (int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(double, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 */
	public void pridajVlnku(Poloha poloha, double polomer,
		double násobokIntenzity)
	{
		pridajVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(Poloha, double)
	 */
	public void pridajVlnku(double x, double y, double polomer)
	{
		pridajVlnku(x, y, (int)polomer, 765, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #pridajVlnku(double, double, double, double, boolean)
	 * @see #pridajVlnku(Poloha, double, double, boolean)
	 * @see #pridajVlnku(double, double, double, double)
	 * @see #pridajVlnku(Poloha, double, double)
	 * @see #pridajVlnku(double, double, double)
	 */
	public void pridajVlnku(Poloha poloha, double polomer)
	{
		pridajVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, 765, false);
	}


	/**
	 * <p>Táto metóda odoberie oblasť vlnky so zadaným polomerom a intenzitou na
	 * zadanej pozícii, pričom ju zároveň umožní nechať zmrazenú. Metóda funguje
	 * úplne rovnako ako metóda {@link #pridajVlnku(double, double, int, double,
	 * boolean) pridajVlnku(x, y, polomer, násobokIntenzity, zmraz)} s tým
	 * rozdielom, že pyramída je od mapy vlnenia v aktívnej sníme odčítaná.
	 * To znamená, že pri zastavenej simulácii by sa po sebe nasledujúce volania
	 * metódy {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku} a {@code currodoberVlnku} s rovnakými parametrami vzájomne
	 * zrušili. Na pokojnej hladine vyvolá volanie tejto metódy vlnenie
	 * s negatívnou začiatočnou fázou vlnky. Rozdielny efekt je najlepšie
	 * vidieť na zmrazených vlnkách (pozri obrázok nižšie).</p>
	 * 
	 * <table class="centered">
	 * <tr>
	 * <td><image>porovnanie-kladna-vlnka.png<alt/></image>Začiatočná fáza
	 * vlnenia po volaní metódy {@link #pridajVlnku(double, double, int, double,
	 * boolean) pridajVlnku}.</td>
	 * <td> </td>
	 * <td><image>porovnanie-zaporna-vlnka.png<alt/></image>Začiatočná fáza
	 * vlnenia po volaní metódy {@code currodoberVlnku}.</td>
	 * </tr>
	 * <tr><td colspan="3"><p class="image">(Obrázky sú orezané
	 * a zväčšené.)</p></td></tr>
	 * </table>
	 * 
	 * <p>(Aj táto metóda, podobne ako {@link #pridajVlnku(double, double, int,
	 * double, boolean) pridajVlnku}, má viacero klonov, ktoré umožňujú jej
	 * jednoduchšie použitie…)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Všetky metódy tejto triedy, ktorých
	 * názov sa začína slovom „odober“ majú definovanú plnohodnotnú alternatívnu
	 * metódu, ktorej názov sa začína slovom „uber“ a naopak. Obidve metódy
	 * sú vzájomnými úplnými kópiami.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(double, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, int, double, boolean)
	 */
	public void odoberVlnku(double x, double y,
		int polomer, double násobokIntenzity, boolean zmraz)
	{
		// Bolo to komplikované a chcel som vyrobiť niečo iné, ale keď už sa
		// podarilo toto, tak som to tu nechal…

		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + polomer - 1, uy = ly + polomer - 1;
		lx -= polomer; ly -= polomer;

		int polvlna = polomer / 2;
		if (0 == polvlna) polvlna = 1;
		int vlna = polvlna * 2;
		// int minimálnaÚroveň = polvlna; //
		int minimálnaÚroveň = (polomer / 10) * polvlna;


		// Zvyšky rôznych pokusov:
		// for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			// for (int x = 0; x < šírka; ++x, ++i, ++j)
				// if (0 != mapaTekutiny[i]) {}
					// && (j == ly || j == uy || k == lx || k == ux)
					// && (0 == (j + k) % 2)


		for (int j = ly, m = 0; j <= uy; ++j, ++m)
			for (int k = lx, n = 0; k <= ux; ++k, ++n)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
					{
						double úroveň;

						int o = (m / 2) + polvlna;
						int p = (n / 2) + polvlna;

						if (0 == ((p / vlna) + (o / vlna)) % 2)
							úroveň = ((p % vlna) - polvlna) *
								((o % vlna) - polvlna);
						else
							úroveň = (vlna - (p % vlna) - polvlna) *
								((o % vlna) - polvlna);

						if (úroveň > minimálnaÚroveň)
						{
							úroveň /= (double)polvlna;
							úroveň *= násobokIntenzity;

							mapaVĺn[indexA + (j *
								(šírka + 2)) + k] -= (int)úroveň;
							mapaVĺn[indexB + (j *
								(šírka + 2)) + k] -= (int)úroveň;

							if (zmraz) mapaTekutiny[(j * šírka) + k] = 0;
						}
					}
	}


	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(double, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 */
	public void odoberVlnku(double x, double y,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň umožňuje zadať polohu stredu oblasti ako jeden parameter
	 * {@code poloha}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(double, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 */
	public void odoberVlnku(Poloha poloha,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		odoberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(double, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 */
	public void odoberVlnku(double x, double y,
		double polomer, double násobokIntenzity)
	{
		odoberVlnku(x, y, (int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(double, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 */
	public void odoberVlnku(Poloha poloha, double polomer,
		double násobokIntenzity)
	{
		odoberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(Poloha, double)
	 */
	public void odoberVlnku(double x, double y, double polomer)
	{
		odoberVlnku(x, y, (int)polomer, 765, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #odoberVlnku(double, double, int, double, boolean)
	 * odoberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, double, double, boolean)
	 * @see #odoberVlnku(Poloha, double, double, boolean)
	 * @see #odoberVlnku(double, double, double, double)
	 * @see #odoberVlnku(Poloha, double, double)
	 * @see #odoberVlnku(double, double, double)
	 */
	public void odoberVlnku(Poloha poloha, double polomer)
	{
		odoberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, 765, false);
	}


	/**
	 * <p>Táto metóda odoberie oblasť vlnky so zadaným polomerom a intenzitou na
	 * zadanej pozícii, pričom ju zároveň umožní nechať zmrazenú. Metóda funguje
	 * úplne rovnako ako metóda {@link #pridajVlnku(double, double, int, double,
	 * boolean) pridajVlnku(x, y, polomer, násobokIntenzity, zmraz)} s tým
	 * rozdielom, že pyramída je od mapy vlnenia v aktívnej sníme odčítaná.
	 * To znamená, že pri zastavenej simulácii by sa po sebe nasledujúce volania
	 * metódy {@link #pridajVlnku(double, double, int, double, boolean)
	 * pridajVlnku} a {@code curruberVlnku} s rovnakými parametrami vzájomne
	 * zrušili. Na pokojnej hladine vyvolá volanie tejto metódy vlnenie
	 * s negatívnou začiatočnou fázou vlnky. Rozdielny efekt je najlepšie
	 * vidieť na zmrazených vlnkách (pozri obrázok nižšie).</p>
	 * 
	 * <table class="centered">
	 * <tr>
	 * <td><image>porovnanie-kladna-vlnka.png<alt/></image>Začiatočná fáza
	 * vlnenia po volaní metódy {@link #pridajVlnku(double, double, int, double,
	 * boolean) pridajVlnku}.</td>
	 * <td> </td>
	 * <td><image>porovnanie-zaporna-vlnka.png<alt/></image>Začiatočná fáza
	 * vlnenia po volaní metódy {@code curruberVlnku}.</td>
	 * </tr>
	 * <tr><td colspan="3"><p class="image">(Obrázky sú orezané
	 * a zväčšené.)</p></td></tr>
	 * </table>
	 * 
	 * <p>(Aj táto metóda, podobne ako {@link #pridajVlnku(double, double, int,
	 * double, boolean) pridajVlnku}, má viacero klonov, ktoré umožňujú jej
	 * jednoduchšie použitie…)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Všetky metódy tejto triedy, ktorých
	 * názov sa začína slovom „uber“ majú definovanú plnohodnotnú alternatívnu
	 * metódu, ktorej názov sa začína slovom „odober“ a naopak. Obidve metódy
	 * sú vzájomnými úplnými kópiami.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(double, double, double)
	 * @see #uberVlnku(Poloha, double)
	 * @see #pridajVlnku(double, double, int, double, boolean)
	 * @see #odoberVlnku(double, double, int, double, boolean)
	 */
	public void uberVlnku(double x, double y,
		int polomer, double násobokIntenzity, boolean zmraz)
	{
		// Bolo to komplikované a chcel som vyrobiť niečo iné, ale keď už sa
		// podarilo toto, tak som to tu nechal…

		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + polomer - 1, uy = ly + polomer - 1;
		lx -= polomer; ly -= polomer;

		int polvlna = polomer / 2;
		if (0 == polvlna) polvlna = 1;
		int vlna = polvlna * 2;
		// int minimálnaÚroveň = polvlna; //
		int minimálnaÚroveň = (polomer / 10) * polvlna;


		// Zvyšky rôznych pokusov:
		// for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			// for (int x = 0; x < šírka; ++x, ++i, ++j)
				// if (0 != mapaTekutiny[i]) {}
					// && (j == ly || j == uy || k == lx || k == ux)
					// && (0 == (j + k) % 2)


		for (int j = ly, m = 0; j <= uy; ++j, ++m)
			for (int k = lx, n = 0; k <= ux; ++k, ++n)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
					{
						double úroveň;

						int o = (m / 2) + polvlna;
						int p = (n / 2) + polvlna;

						if (0 == ((p / vlna) + (o / vlna)) % 2)
							úroveň = ((p % vlna) - polvlna) *
								((o % vlna) - polvlna);
						else
							úroveň = (vlna - (p % vlna) - polvlna) *
								((o % vlna) - polvlna);

						if (úroveň > minimálnaÚroveň)
						{
							úroveň /= (double)polvlna;
							úroveň *= násobokIntenzity;

							mapaVĺn[indexA + (j *
								(šírka + 2)) + k] -= (int)úroveň;
							mapaVĺn[indexB + (j *
								(šírka + 2)) + k] -= (int)úroveň;

							if (zmraz) mapaTekutiny[(j * šírka) + k] = 0;
						}
					}
	}


	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(double, double, double)
	 * @see #uberVlnku(Poloha, double)
	 */
	public void uberVlnku(double x, double y,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň umožňuje zadať polohu stredu oblasti ako jeden parameter
	 * {@code poloha}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * @param zmraz ak je hodnota tohto parametra rovná {@code valtrue}, tak
	 *     budú všetky ovplyvnené časti hladiny zároveň zmrazené
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(double, double, double)
	 * @see #uberVlnku(Poloha, double)
	 */
	public void uberVlnku(Poloha poloha,
		double polomer, double násobokIntenzity, boolean zmraz)
	{
		uberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, zmraz);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(double, double, double)
	 * @see #uberVlnku(Poloha, double)
	 */
	public void uberVlnku(double x, double y,
		double polomer, double násobokIntenzity)
	{
		uberVlnku(x, y, (int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametru {@code zmraz} predvolenú hodnotu
	 * {@code valfalse}. Ďalšie podrobnosti sa dočítate v opise uvedenej
	 * metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * @param násobokIntenzity násobok intenzity vlnky – úroveň intenzity
	 *     v každom bode bude násobená touto hodnotou
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(double, double, double)
	 * @see #uberVlnku(Poloha, double)
	 */
	public void uberVlnku(Poloha poloha, double polomer,
		double násobokIntenzity)
	{
		uberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, násobokIntenzity, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(Poloha, double)
	 */
	public void uberVlnku(double x, double y, double polomer)
	{
		uberVlnku(x, y, (int)polomer, 765, false);
	}

	/**
	 * <p>Táto metóda funguje úplne rovnako ako metóda
	 * {@link #uberVlnku(double, double, int, double, boolean)
	 * uberVlnku(x, y, (int)polomer, násobokIntenzity, zmraz)}, ibaže
	 * umožňuje zadať parameter {@code polomer} ako reálne číslo, umožňuje
	 * zadať polohu stredu oblasti ako jeden parameter {@code poloha}
	 * a zároveň určuje parametrom {@code násobokIntenzity} a {@code zmraz}
	 * predvolené hodnoty {@code num765} a {@code valfalse}. Ďalšie
	 * podrobnosti sa dočítate v opise uvedenej metódy.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param polomer polomer vlnky
	 * 
	 * @see #uberVlnku(double, double, int, double, boolean)
	 * @see #uberVlnku(double, double, double, double, boolean)
	 * @see #uberVlnku(Poloha, double, double, boolean)
	 * @see #uberVlnku(double, double, double, double)
	 * @see #uberVlnku(Poloha, double, double)
	 * @see #uberVlnku(double, double, double)
	 */
	public void uberVlnku(Poloha poloha, double polomer)
	{
		uberVlnku(poloha.polohaX(), poloha.polohaY(),
			(int)polomer, 765, false);
	}


	/**
	 * <p>Pridanie základného vzruchu na zadanej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * 
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double)
	 * @see #pridajVzruch(Poloha, double, double)
	 * @see #uberVzruch(double, double)
	 * @see #odoberVzruch(double, double)
	 * @see #pridajVzruch(Image)
	 */
	public void pridajVzruch(double x, double y)
	{
		pridajVzruch(x, y, 3, 765);
	}

	/**
	 * <p>Pridanie základného vzruchu so zadaným rozsahom na zadanej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double)
	 * @see #pridajVzruch(Poloha, double, double)
	 */
	public void pridajVzruch(double x, double y, double rozsahVzruchu)
	{
		pridajVzruch(x, y, (int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Pridanie základného vzruchu so zadaným rozsahom a intenzitou
	 * na zadanej pozícii. (Toto je pôvodná metóda, ktore prekrytie
	 * ovplyvní všetky metódy, ktoré sú jej klonmi.)</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude pripočítaná ku každému
	 *     bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double)
	 * @see #pridajVzruch(Poloha, double, double)
	 */
	public void pridajVzruch(double x, double y, int rozsahVzruchu,
		int intenzitaVzruchu)
	{
		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + rozsahVzruchu - 1, uy = ly + rozsahVzruchu - 1;
		lx -= rozsahVzruchu; ly -= rozsahVzruchu;

		for (int j = ly; j <= uy; ++j)
			for (int k = lx; k <= ux; ++k)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
						mapaVĺn[indexA + (j * (šírka + 2)) + k] +=
							intenzitaVzruchu;
	}

	/**
	 * <p>Pridanie základného vzruchu so zadaným rozsahom a intenzitou
	 * na zadanej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude pripočítaná ku každému
	 *     bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double)
	 * @see #pridajVzruch(Poloha, double, double)
	 * @see #pridajVzruch(double, double, int, int)
	 */
	public void pridajVzruch(double x, double y,
		double rozsahVzruchu, double intenzitaVzruchu)
	{
		pridajVzruch(x, y, (int)rozsahVzruchu, (int)intenzitaVzruchu);
	}

	/**
	 * <p>Pridanie základného vzruchu na zadanej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha, double)
	 * @see #pridajVzruch(Poloha, double, double)
	 */
	public void pridajVzruch(Poloha poloha)
	{
		pridajVzruch(poloha.polohaX(), poloha.polohaY(), 3, 765);
	}

	/**
	 * <p>Pridanie základného vzruchu so zadaným rozsahom na zadanej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double, double)
	 */
	public void pridajVzruch(Poloha poloha, double rozsahVzruchu)
	{
		pridajVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Pridanie základného vzruchu so zadaným rozsahom a intenzitou
	 * na zadanej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude pripočítaná ku každému
	 *     bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #pridajVzruch(double, double)
	 * @see #pridajVzruch(double, double, double)
	 * @see #pridajVzruch(double, double, double, double)
	 * @see #pridajVzruch(Poloha)
	 * @see #pridajVzruch(Poloha, double)
	 */
	public void pridajVzruch(Poloha poloha, double rozsahVzruchu,
		double intenzitaVzruchu)
	{
		pridajVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, (int)intenzitaVzruchu);
	}


	// Pridá vzruchy do mapy vzruchov z údajov masky, do ktorej môžu byť
	// vložené z rôznych zdrojov.
	private void pridajVzruchy(int posunIntenzity)
	{
		for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
			{
				// Miesta, kde nie je definovaná tekutina sú ignorované.
				if (0 != mapaTekutiny[i])
				{
					int maska = údajeMasky[i];

					// Všetky úplne priehľadné farby sú ignorované.
					if (0 != (maska & 0xff000000))
					{
						// Úplne biela farba s ľubovoľnou úrovňou
						// priehľadnosti by mala výslednú úroveň vzruchu
						// rovnú nule, preto môžeme priamo pripočítať iba
						// posun intenzity:
						if (0xffffff == (maska & 0xffffff))
							mapaVĺn[j] += posunIntenzity;
						else
						{
							// Nepriehľadná čierna farba znamená najvyššiu
							// úroveň vzruchu: 765. Od tejto počiatočnej
							// úrovne vzruchu sú odčítané všetky farebné
							// zložky. (Čierna farba má všetky zložky rovné
							// nule, preto ponechá úroveň na maxime.)
							int vzruch = 765;

							// — červená zložka (Red):
							vzruch -= (maska >> 16) & 0xff;
							// — zelená zložka (Green):
							vzruch -= (maska >>  8) & 0xff;
							// — modrá zložka (Blue):
							vzruch -=  maska        & 0xff;

							// Vypočítaná úroveň vzruchu je dodatočne upravená
							// úrovňou zložky priehľadnosti (Alpha) v prípade,
							// že táto zložka nie je na maxime:
							if (0xff000000 != (maska & 0xff000000))
							{
								// — krát maximum priehľadnosti (255):
								vzruch *= 0xff;
								// — deleno skutočná úroveň priehľadnosti:
								vzruch /= (maska >> 24) & 0xff;
							}

							// Pripočítame vypočítanú úroveň vzruchu spolu
							// s posunom intenzity:
							mapaVĺn[j] += vzruch + posunIntenzity;
						}
					}
				}
			}
	}


	/**
	 * <p>Pridá k hladine vzruchy podľa masky reprezentovanej vo forme zadaného
	 * obrázka. Ak obrázok (maska) nemá zhodné rozmery s rozmermi výškových
	 * máp vlnenia, bude použitý tak, ako keby bol nakreslený v strede mapy
	 * vlnenia (ak je väčší, nadbytočné časti budú ignorované, ak je menší,
	 * chýbajúce časti budú považované za prázdne). Hodnoty bodov výškovej
	 * mapy vlnenia budú potom ovplyvnené súčtom negatívnych hodnôt farebných
	 * zložiek jednotlivých bodov obrázka. Čierne body znamenajú maximálny
	 * nárast intenzity, farby s najvyššími hodnotami jedinej farebnej zložky
	 * (červenej, zelenej alebo modrej) znamenajú dvojtretinový nárast
	 * intenzity a biele body znamenajú nulový nárast intenzity. Popri tom
	 * je zároveň braná do úvahy aj úroveň priehľadnosti jednotlivých bodov
	 * masky. Neviditeľné body sú paušálne ignorované a intenzita
	 * polopriehľadných bodov je adekvátne znížená.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na pridanie vzruchov k aktuálnej výškovej mape vlnenia
	 * 
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 * @see #uberVzruch(Image)
	 * @see #odoberVzruch(Image)
	 * @see #pridajVzruch(double, double)
	 */
	public void pridajVzruch(Image obrázok)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		pridajVzruchy(0);
	}

	/**
	 * <p>Pridá k hladine vzruchy podľa masky reprezentovanej vo forme zadaného
	 * obrázka s možnosťou posunutia výslednej intenzity hladiny pre všetky
	 * ovplyvnené body. Ak je posun intenzity nulový, tak je výsledok
	 * vykonania tejto metódy rovnaký ako pri metóde
	 * {@link #pridajVzruch(Image) pridajVzruch(obrázok)} (pozrite si aj jej
	 * opis). Ak je posun intenzity rovný napríklad presne hodnote
	 * {@code num765}, tak všetky čisto čierne nepriehľadné body masky budú
	 * mať na stav hladiny nulový vplyv. Jednoducho posun intenzity ovplyvní
	 * výsledok v kladnom alebo zápornom smere. Aj pri tejto metóde platí,
	 * že neviditeľné body sú paušálne ignorované.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na pridanie vzruchov k aktuálnej výškovej mape vlnenia
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Image obrázok, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		pridajVzruchy(posunIntenzity);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #pridajVzruch(Image) pridajVzruch(obrázok)}. (Pozrite si
	 * aj opis uvedenej metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Shape tvar)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(0);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare s možnosťou posunutia výslednej
	 * intenzity hladiny pre všetky ovplyvnené body. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #pridajVzruch(Image, int) pridajVzruch(obrázok,
	 * posunIntenzity)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Shape tvar, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(posunIntenzity);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnenú plochu alebo kreslenú čiaru podľa hodnoty parametra
	 * {@code vyplnený}. Metóda funguje tak, že zadaný tvar nakreslí alebo
	 * vyplní do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #pridajVzruch(Image)
	 * pridajVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Shape tvar, boolean vyplnený)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(0);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare. Tvar je považovaný za
	 * čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak, že zadaný
	 * tvar nakreslí do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #pridajVzruch(Image)
	 * pridajVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Shape tvar, double hrúbkaČiary)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(0);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare s možnosťou posunutia výslednej
	 * intenzity hladiny pre všetky ovplyvnené body. Tvar je považovaný za
	 * vyplnenú plochu alebo kreslenú čiaru podľa hodnoty parametra
	 * {@code vyplnený}. Metóda funguje tak, že zadaný tvar nakreslí alebo
	 * vyplní do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #pridajVzruch(Image, int)
	 * pridajVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis uvedenej
	 * metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, double)
	 * @see #pridajVzruch(Shape, double, int)
	 */
	public void pridajVzruch(Shape tvar, boolean vyplnený,
		int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(posunIntenzity);
	}

	/**
	 * <p>Pridá na hladine vzruch v zadanom tvare s možnosťou posunutia výslednej
	 * intenzity hladiny pre všetky ovplyvnené body. Tvar je považovaný za
	 * čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak, že zadaný
	 * tvar nakreslí do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #pridajVzruch(Image, int)
	 * pridajVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis uvedenej
	 * metódy.)</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky
	 * 
	 * @see #pridajVzruch(Image)
	 * @see #pridajVzruch(Image, int)
	 * @see #pridajVzruch(Shape)
	 * @see #pridajVzruch(Shape, int)
	 * @see #pridajVzruch(Shape, boolean)
	 * @see #pridajVzruch(Shape, boolean, int)
	 * @see #pridajVzruch(Shape, double)
	 */
	public void pridajVzruch(Shape tvar, double hrúbkaČiary,
		int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		pridajVzruchy(posunIntenzity);
	}


	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * 
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double)
	 * @see #uberVzruch(Poloha, double, double)
	 * @see #pridajVzruch(double, double)
	 * @see #odoberVzruch(double, double)
	 * @see #uberVzruch(Image)
	 */
	public void uberVzruch(double x, double y)
	{
		uberVzruch(x, y, 3, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double)
	 * @see #uberVzruch(Poloha, double, double)
	 */
	public void uberVzruch(double x, double y, double rozsahVzruchu)
	{
		uberVzruch(x, y, (int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou. (Toto je pôvodná metóda, ktore prekrytie
	 * ovplyvní všetky metódy, ktoré sú jej klonmi.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Všetky metódy tejto triedy, ktorých
	 * názov sa začína slovom „uber“ majú definovanú plnohodnotnú alternatívnu
	 * metódu, ktorej názov sa začína slovom „odober“ a naopak. Obidve metódy
	 * sú vzájomnými úplnými kópiami.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double)
	 * @see #uberVzruch(Poloha, double, double)
	 */
	public void uberVzruch(double x, double y, int rozsahVzruchu,
		int intenzitaVzruchu)
	{
		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + rozsahVzruchu - 1, uy = ly + rozsahVzruchu - 1;
		lx -= rozsahVzruchu; ly -= rozsahVzruchu;

		for (int j = ly; j <= uy; ++j)
			for (int k = lx; k <= ux; ++k)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
						mapaVĺn[indexA + (j * (šírka + 2)) + k] -=
							intenzitaVzruchu;
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double)
	 * @see #uberVzruch(Poloha, double, double)
	 * @see #uberVzruch(double, double, int, int)
	 */
	public void uberVzruch(double x, double y, double rozsahVzruchu,
		double intenzitaVzruchu)
	{
		uberVzruch(x, y, (int)rozsahVzruchu, (int)intenzitaVzruchu);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha, double)
	 * @see #uberVzruch(Poloha, double, double)
	 */
	public void uberVzruch(Poloha poloha)
	{
		uberVzruch(poloha.polohaX(), poloha.polohaY(), 3, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double, double)
	 */
	public void uberVzruch(Poloha poloha, double rozsahVzruchu)
	{
		uberVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(double, double, double)
	 * @see #uberVzruch(double, double, double, double)
	 * @see #uberVzruch(Poloha)
	 * @see #uberVzruch(Poloha, double)
	 */
	public void uberVzruch(Poloha poloha, double rozsahVzruchu,
		double intenzitaVzruchu)
	{
		uberVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, (int)intenzitaVzruchu);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * 
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double)
	 * @see #odoberVzruch(Poloha, double, double)
	 * @see #pridajVzruch(double, double)
	 * @see #uberVzruch(double, double)
	 * @see #uberVzruch(Image)
	 */
	public void odoberVzruch(double x, double y)
	{
		odoberVzruch(x, y, 3, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double)
	 * @see #odoberVzruch(Poloha, double, double)
	 */
	public void odoberVzruch(double x, double y, double rozsahVzruchu)
	{
		odoberVzruch(x, y, (int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou. (Toto je pôvodná metóda, ktore prekrytie
	 * ovplyvní všetky metódy, ktoré sú jej klonmi.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Všetky metódy tejto triedy, ktorých
	 * názov sa začína slovom „odober“ majú definovanú plnohodnotnú alternatívnu
	 * metódu, ktorej názov sa začína slovom „uber“ a naopak. Obidve metódy
	 * sú vzájomnými úplnými kópiami.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double)
	 * @see #odoberVzruch(Poloha, double, double)
	 */
	public void odoberVzruch(double x, double y, int rozsahVzruchu,
		int intenzitaVzruchu)
	{
		int lx = prepočítajX(x), ly = prepočítajY(y);
		int ux = lx + rozsahVzruchu - 1, uy = ly + rozsahVzruchu - 1;
		lx -= rozsahVzruchu; ly -= rozsahVzruchu;

		for (int j = ly; j <= uy; ++j)
			for (int k = lx; k <= ux; ++k)
				if (j >= 0 && j < výška && k >= 0 && k < šírka)
					if (0 != mapaTekutiny[(j * šírka) + k])
						mapaVĺn[indexA + (j * (šírka + 2)) + k] -=
							intenzitaVzruchu;
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou.</p>
	 * 
	 * @param x horizontálna súradnica stredu oblasti
	 * @param y vertikálna súradnica stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double)
	 * @see #odoberVzruch(Poloha, double, double)
	 * @see #odoberVzruch(double, double, int, int)
	 */
	public void odoberVzruch(double x, double y, double rozsahVzruchu,
		double intenzitaVzruchu)
	{
		odoberVzruch(x, y, (int)rozsahVzruchu, (int)intenzitaVzruchu);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha, double)
	 * @see #odoberVzruch(Poloha, double, double)
	 */
	public void odoberVzruch(Poloha poloha)
	{
		odoberVzruch(poloha.polohaX(), poloha.polohaY(), 3, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double, double)
	 */
	public void odoberVzruch(Poloha poloha, double rozsahVzruchu)
	{
		odoberVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, 765);
	}

	/**
	 * <p>Odobratie štvorcovej oblasti vzruchu na zadanej pozícii so zadaným
	 * rozsahom a intenzitou.</p>
	 * 
	 * @param poloha poloha stredu oblasti
	 * @param rozsahVzruchu hodnota rozsahu určená polovicou strany štvorca
	 *     ovplyvnenej oblasti (tiež v tvare štvorca)
	 * @param intenzitaVzruchu hodnota, ktorá bude odpočítaná od hodnoty
	 *     každého bodu výškovej mapy v rámci ovplyvnenej oblasti
	 * 
	 * @see #odoberVzruch(double, double)
	 * @see #odoberVzruch(double, double, double)
	 * @see #odoberVzruch(double, double, double, double)
	 * @see #odoberVzruch(Poloha)
	 * @see #odoberVzruch(Poloha, double)
	 */
	public void odoberVzruch(Poloha poloha, double rozsahVzruchu,
		double intenzitaVzruchu)
	{
		odoberVzruch(poloha.polohaX(), poloha.polohaY(),
			(int)rozsahVzruchu, (int)intenzitaVzruchu);
	}


	// Uberie vzruchy z mapy vzruchov podľa údajov masky.
	private void uberVzruchy(int posunIntenzity)
	{
		for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
			{
				// Miesta, kde nie je definovaná tekutina sú ignorované.
				if (0 != mapaTekutiny[i])
				{
					int maska = údajeMasky[i];

					// Všetky úplne priehľadné farby sú ignorované.
					if (0 != (maska & 0xff000000))
					{
						// Úplne biela farba s ľubovoľnou úrovňou
						// priehľadnosti by mala výslednú úroveň vzruchu
						// na odobratie rovnú nule, preto môžeme priamo
						// odrátať iba posun intenzity:
						if (0xffffff == (maska & 0xffffff))
							mapaVĺn[j] -= posunIntenzity;
						else
						{
							// Nepriehľadná čierna farba znamená najvyššiu
							// úroveň vzruchu na odobratie: 765. Od tejto
							// počiatočnej úrovne vzruchu sú odčítané všetky
							// farebné zložky. (Čierna farba má všetky
							// zložky rovné nule, preto ponechá úroveň na
							// maxime.)
							int vzruch = 765;

							// — červená zložka (Red):
							vzruch -= (maska >> 16) & 0xff;
							// — zelená zložka (Green):
							vzruch -= (maska >>  8) & 0xff;
							// — modrá zložka (Blue):
							vzruch -=  maska        & 0xff;

							// Vypočítaná úroveň vzruchu je dodatočne upravená
							// úrovňou zložky priehľadnosti (Alpha) v prípade,
							// že táto zložka nie je na maxime:
							if (0xff000000 != (maska & 0xff000000))
							{
								// — krát maximum priehľadnosti (255):
								vzruch *= 0xff;
								// — deleno skutočná úroveň priehľadnosti:
								vzruch /= (maska >> 24) & 0xff;
							}

							// Odrátame vypočítanú úroveň vzruchu spolu
							// s posunom intenzity:
							mapaVĺn[j] -= vzruch + posunIntenzity;
						}
					}
				}
			}
	}


	/**
	 * <p>Odoberie z hladiny vzruchy podľa masky reprezentovanej vo forme zadaného
	 * obrázka. Ak obrázok (maska) nemá zhodné rozmery s rozmermi výškových
	 * máp vlnenia, bude použitý tak, ako keby bol nakreslený v strede mapy
	 * vlnenia (ak je väčší, nadbytočné časti budú ignorované, ak je menší,
	 * chýbajúce časti budú považované za prázdne). Hodnoty bodov výškovej
	 * mapy vlnenia budú potom ovplyvnené súčtom negatívnych hodnôt farebných
	 * zložiek jednotlivých bodov obrázka. Čierne body znamenajú maximálny
	 * úbytok intenzity, farby s najvyššími hodnotami jedinej farebnej zložky
	 * (červenej, zelenej alebo modrej) znamenajú dvojtretinový úbytok
	 * intenzity a biele body znamenajú nulový úbytok intenzity. Popri tom
	 * je zároveň braná do úvahy aj úroveň priehľadnosti jednotlivých bodov
	 * masky. Neviditeľné body sú paušálne ignorované a intenzita
	 * polopriehľadných bodov je adekvátne znížená.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na pridanie vzruchov k aktuálnej výškovej mape vlnenia
	 * 
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 * @see #pridajVzruch(Image)
	 * @see #odoberVzruch(Image)
	 */
	public void uberVzruch(Image obrázok)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		uberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruchy podľa masky reprezentovanej vo forme
	 * zadaného obrázka s možnosťou posunutia výslednej intenzity hladiny
	 * pre všetky ovplyvnené body (v zápornom smere). Ak je posun intenzity
	 * nulový, tak je výsledok vykonania tejto metódy rovnaký ako pri metóde
	 * {@link #uberVzruch(Image) uberVzruch(obrázok)} (pozrite si aj jej
	 * opis). Táto metóda pôsobí ako negatívna verzia metódy
	 * {@link #pridajVzruch(Image, int) pridajVzruch(obrázok, posunIntenzity)}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na odobratie vzruchov k aktuálnej výškovej mape vlnenia
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Image obrázok, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		uberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #uberVzruch(Image) uberVzruch(obrázok)}. (Pozrite si
	 * aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Shape tvar)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do
	 * vnútornej masky (čo je v podstate obrázok), ktorú použije rovnakým
	 * spôsobom ako metóda {@link #uberVzruch(Image, int) uberVzruch(obrázok,
	 * posunIntenzity)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Shape tvar, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnenú plochu alebo kreslenú čiaru podľa hodnoty parametra
	 * {@code vyplnený}. Metóda funguje tak, že zadaný tvar nakreslí alebo
	 * vyplní do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #uberVzruch(Image)
	 * uberVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Shape tvar, boolean vyplnený)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak, že zadaný
	 * tvar nakreslí do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #uberVzruch(Image)
	 * uberVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Shape tvar, double hrúbkaČiary)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za vyplnenú plochu alebo kreslenú čiaru podľa hodnoty
	 * parametra {@code vyplnený}. Metóda funguje tak, že zadaný tvar
	 * nakreslí alebo vyplní do vnútornej masky (čo je v podstate obrázok),
	 * ktorú použije rovnakým spôsobom ako metóda {@link #uberVzruch(Image,
	 * int) uberVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis
	 * uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, double)
	 * @see #uberVzruch(Shape, double, int)
	 */
	public void uberVzruch(Shape tvar, boolean vyplnený, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak,
	 * že zadaný tvar nakreslí do vnútornej masky (čo je v podstate obrázok),
	 * ktorú použije rovnakým spôsobom ako metóda {@link #uberVzruch(Image,
	 * int) uberVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis
	 * uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #uberVzruch(Image)
	 * @see #uberVzruch(Image, int)
	 * @see #uberVzruch(Shape)
	 * @see #uberVzruch(Shape, int)
	 * @see #uberVzruch(Shape, boolean)
	 * @see #uberVzruch(Shape, boolean, int)
	 * @see #uberVzruch(Shape, double)
	 */
	public void uberVzruch(Shape tvar, double hrúbkaČiary, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		uberVzruchy(posunIntenzity);
	}


	// Uberie vzruchy z mapy vzruchov podľa údajov masky.
	private void odoberVzruchy(int posunIntenzity)
	{
		for (int y = 0, i = 0, j = indexA; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
			{
				// Miesta, kde nie je definovaná tekutina sú ignorované.
				if (0 != mapaTekutiny[i])
				{
					int maska = údajeMasky[i];

					// Všetky úplne priehľadné farby sú ignorované.
					if (0 != (maska & 0xff000000))
					{
						// Úplne biela farba s ľubovoľnou úrovňou
						// priehľadnosti by mala výslednú úroveň vzruchu
						// na odobratie rovnú nule, preto môžeme priamo
						// odrátať iba posun intenzity:
						if (0xffffff == (maska & 0xffffff))
							mapaVĺn[j] -= posunIntenzity;
						else
						{
							// Nepriehľadná čierna farba znamená najvyššiu
							// úroveň vzruchu na odobratie: 765. Od tejto
							// počiatočnej úrovne vzruchu sú odčítané všetky
							// farebné zložky. (Čierna farba má všetky
							// zložky rovné nule, preto ponechá úroveň na
							// maxime.)
							int vzruch = 765;

							// — červená zložka (Red):
							vzruch -= (maska >> 16) & 0xff;
							// — zelená zložka (Green):
							vzruch -= (maska >>  8) & 0xff;
							// — modrá zložka (Blue):
							vzruch -=  maska        & 0xff;

							// Vypočítaná úroveň vzruchu je dodatočne upravená
							// úrovňou zložky priehľadnosti (Alpha) v prípade,
							// že táto zložka nie je na maxime:
							if (0xff000000 != (maska & 0xff000000))
							{
								// — krát maximum priehľadnosti (255):
								vzruch *= 0xff;
								// — deleno skutočná úroveň priehľadnosti:
								vzruch /= (maska >> 24) & 0xff;
							}

							// Odrátame vypočítanú úroveň vzruchu spolu
							// s posunom intenzity:
							mapaVĺn[j] -= vzruch + posunIntenzity;
						}
					}
				}
			}
	}


	/**
	 * <p>Odoberie z hladiny vzruchy podľa masky reprezentovanej vo forme zadaného
	 * obrázka. Ak obrázok (maska) nemá zhodné rozmery s rozmermi výškových
	 * máp vlnenia, bude použitý tak, ako keby bol nakreslený v strede mapy
	 * vlnenia (ak je väčší, nadbytočné časti budú ignorované, ak je menší,
	 * chýbajúce časti budú považované za prázdne). Hodnoty bodov výškovej
	 * mapy vlnenia budú potom ovplyvnené súčtom negatívnych hodnôt farebných
	 * zložiek jednotlivých bodov obrázka. Čierne body znamenajú maximálny
	 * úbytok intenzity, farby s najvyššími hodnotami jedinej farebnej zložky
	 * (červenej, zelenej alebo modrej) znamenajú dvojtretinový úbytok
	 * intenzity a biele body znamenajú nulový úbytok intenzity. Popri tom
	 * je zároveň braná do úvahy aj úroveň priehľadnosti jednotlivých bodov
	 * masky. Neviditeľné body sú paušálne ignorované a intenzita
	 * polopriehľadných bodov je adekvátne znížená.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na pridanie vzruchov k aktuálnej výškovej mape vlnenia
	 * 
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 * @see #pridajVzruch(Image)
	 * @see #uberVzruch(Image)
	 */
	public void odoberVzruch(Image obrázok)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		odoberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruchy podľa masky reprezentovanej vo forme
	 * zadaného obrázka s možnosťou posunutia výslednej intenzity hladiny
	 * pre všetky ovplyvnené body (v zápornom smere). Ak je posun intenzity
	 * nulový, tak je výsledok vykonania tejto metódy rovnaký ako pri metóde
	 * {@link #odoberVzruch(Image) odoberVzruch(obrázok)} (pozrite si aj jej
	 * opis). Táto metóda pôsobí ako negatívna verzia metódy
	 * {@link #pridajVzruch(Image, int) pridajVzruch(obrázok, posunIntenzity)}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param obrázok obrázok, ktorého intenzita a priehľadnosť bodov budú
	 *     použité na odobratie vzruchov k aktuálnej výškovej mape vlnenia
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Image obrázok, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		int x = (šírka - obrázok.getWidth(null)) / 2;
		int y = (výška - obrázok.getHeight(null)) / 2;

		grafikaMasky.drawImage(obrázok, x, y, null);

		odoberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do vnútornej
	 * masky (čo je v podstate obrázok), ktorú použije rovnakým spôsobom ako
	 * metóda {@link #odoberVzruch(Image) odoberVzruch(obrázok)}. (Pozrite si
	 * aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Shape tvar)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za vyplnený. Metóda funguje tak, že zadaný tvar nakreslí do
	 * vnútornej masky (čo je v podstate obrázok), ktorú použije rovnakým
	 * spôsobom ako metóda {@link #odoberVzruch(Image, int)
	 * odoberVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis uvedenej
	 * metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Shape tvar, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.fill(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * vyplnenú plochu alebo kreslenú čiaru podľa hodnoty parametra
	 * {@code vyplnený}. Metóda funguje tak, že zadaný tvar nakreslí alebo
	 * vyplní do vnútornej masky (čo je v podstate obrázok), ktorú použije
	 * rovnakým spôsobom ako metóda {@link #odoberVzruch(Image)
	 * odoberVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Shape tvar, boolean vyplnený)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare. Tvar je považovaný za
	 * čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak, že zadaný
	 * tvar nakreslí do vnútornej masky (čo je v podstate obrázok), ktorú
	 * použije rovnakým spôsobom ako metóda {@link #odoberVzruch(Image)
	 * odoberVzruch(obrázok)}. (Pozrite si aj opis uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Shape tvar, double hrúbkaČiary)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(0);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za vyplnenú plochu alebo kreslenú čiaru podľa hodnoty
	 * parametra {@code vyplnený}. Metóda funguje tak, že zadaný tvar
	 * nakreslí alebo vyplní do vnútornej masky (čo je v podstate obrázok),
	 * ktorú použije rovnakým spôsobom ako metóda {@link #odoberVzruch(Image,
	 * int) odoberVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis
	 * uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param vyplnený ak je {@code valtrue}, tak tvar bude použitý ako
	 *     vyplnený, v opačnom prípade ako nakreslená čiara (s predvolenou
	 *     hrúbkou {@code num6.0} bodov)
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, double)
	 * @see #odoberVzruch(Shape, double, int)
	 */
	public void odoberVzruch(Shape tvar, boolean vyplnený, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		if (vyplnený)
			grafikaMasky.fill(tvar);
		else
		{
			grafikaMasky.setStroke(predvolenáČiara);
			grafikaMasky.draw(tvar);
		}
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(posunIntenzity);
	}

	/**
	 * <p>Odoberie z hladiny vzruch v zadanom tvare s možnosťou posunutia
	 * výslednej intenzity hladiny pre všetky ovplyvnené body. Tvar je
	 * považovaný za čiaru nakreslenú so zadanou hrúbkou. Metóda funguje tak,
	 * že zadaný tvar nakreslí do vnútornej masky (čo je v podstate obrázok),
	 * ktorú použije rovnakým spôsobom ako metóda {@link #odoberVzruch(Image,
	 * int) odoberVzruch(obrázok, posunIntenzity)}. (Pozrite si aj opis
	 * uvedenej metódy.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Úbytok intenzity neznamená jej útlm,
	 * ale odčítanie. To znamená, že efekt odčítania nejakej hodnoty od hladiny
	 * s nulovou intenzitou má za následok vyvolanie vlnenia so zápornou
	 * začiatočnou hodnotou „výšky“, resp. hĺbky vlny.</p>
	 * 
	 * @param tvar tvar, podľa ktorého bude ovplyvnená prislúchajúca časť
	 *     hladiny
	 * @param hrúbkaČiary hrúbka čiary použitého tvaru
	 * @param posunIntenzity posun výslednej intenzity jednotlivých bodov
	 *     masky (v zápornom smere)
	 * 
	 * @see #odoberVzruch(Image)
	 * @see #odoberVzruch(Image, int)
	 * @see #odoberVzruch(Shape)
	 * @see #odoberVzruch(Shape, int)
	 * @see #odoberVzruch(Shape, boolean)
	 * @see #odoberVzruch(Shape, boolean, int)
	 * @see #odoberVzruch(Shape, double)
	 */
	public void odoberVzruch(Shape tvar, double hrúbkaČiary, int posunIntenzity)
	{
		Arrays.fill(údajeMasky, 0);

		grafikaMasky.translate(posunX, posunY);
		grafikaMasky.setColor(predvolenáFarba);
		grafikaMasky.setStroke(new BasicStroke((float)hrúbkaČiary,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		grafikaMasky.draw(tvar);
		grafikaMasky.translate(-posunX, -posunY);

		odoberVzruchy(posunIntenzity);
	}


	// Poznámka: Nasledujúce dve metódy sú upravenými prispôsobenými verziami
		//       rovnakých metód z vnorenej súkromnej triedy VykonajVObrázku
		//       v ústrednej triede GRobot. Komentár nižšie vychádza
		//       z komentára z uvedenej triedy.
		// 
		// Metódy „rolujMasku“ a „pretočMasku“ sa usilujú o podobnú vec, ibaže
		// s jedným zásadným rozdielom. Metóda „rolujMasku“ za sebou „maže
		// stopy“ (napríklad ak rolujeme doprava, tak vľavo vzniká prázdny
		// priestor) a metóda „pretočMasku“ zachováva všetky grafické
		// informácie pôvodného obrázka (napríklad ak rolujeme doľava, tak
		// grafické informácie opúšťajúce obrázok vľavo do neho opätovne
		// prichádzajú sprava a naopak). Z toho plynie prvý zásadný rozdiel:
		// Metóde „rolujMasku“ postačujú pôvodné údaje obrázka, ale metóda
		// „pretočMasku“ potrebuje dodatočný pomocný zásobník.
		// 
		// Ďalšie rozdiely plynú z toho, ako metódy spracúvajú údaje. Metóda
		// „rolujMasku“ najprv musí overiť, ktorým smerom sú údaje posúvané
		// a podľa toho zvoliť protiidúci smer presúvania sa údajov (aby
		// aktualizácia neprepísala ešte nepresunuté údaje). Preto je metóda
		// „rolujMasku“ o poznanie dlhšia. Metóda „pretočMasku“ iba zálohuje
		// pôvodný obsah obrázka, rozdelí obraz na štyri nerovnomerné
		// „kvadranty“ a postupne skopíruje obsahy jednotlivých „kvadrantov“
		// zo zálohy do originálu podľa požadovanej miery posunutia.
	// 

	private void rolujMasku()
	{
		if (Δx >= šírka || Δx <= -šírka ||
			Δy >= výška || Δy <= -výška)
		{
			Arrays.fill(údajeMasky, prázdnaMaska);
			return;
		}

		int spodnáZarážka, vrchnáZarážka,
			zarážkaRiadkov, zarážkaStĺpcov;

		if (Δy <= 0)
		{
			spodnáZarážka = 0;
			vrchnáZarážka = šírka * -Δy - Δx;
			zarážkaRiadkov = výška + Δy;

			if (Δx <= 0)
			{
				zarážkaStĺpcov = šírka + Δx;

				for (int i = 0; i < zarážkaRiadkov; ++i)
				{
					int j = 0;

					for (; j < zarážkaStĺpcov; ++j)
						údajeMasky[spodnáZarážka + j] =
							údajeMasky[vrchnáZarážka + j];

					for (; j < šírka; ++j)
						údajeMasky[spodnáZarážka + j] = prázdnaMaska;

					spodnáZarážka += šírka;
					vrchnáZarážka += šírka;
				}
			}
			else
			{
				zarážkaStĺpcov = Δx;

				for (int i = 0; i < zarážkaRiadkov; ++i)
				{
					int j = šírka - 1;

					for (; j >= zarážkaStĺpcov; --j)
						údajeMasky[spodnáZarážka + j] =
							údajeMasky[vrchnáZarážka + j];

					for (; j >= 0; --j)
						údajeMasky[spodnáZarážka + j] = prázdnaMaska;

					spodnáZarážka += šírka;
					vrchnáZarážka += šírka;
				}
			}

			vrchnáZarážka = šírka * výška;

			while (spodnáZarážka < vrchnáZarážka)
			{
				údajeMasky[spodnáZarážka] = prázdnaMaska;
				++spodnáZarážka;
			}
		}
		else
		{
			spodnáZarážka = -Δx + šírka * (výška - 1 - Δy);
			vrchnáZarážka = šírka * (výška - 1);
			zarážkaRiadkov = Δy;

			if (Δx <= 0)
			{
				zarážkaStĺpcov = šírka + Δx;

				for (int i = výška - 1; i >= zarážkaRiadkov; --i)
				{
					int j = 0;

					for (; j < zarážkaStĺpcov; ++j)
						údajeMasky[vrchnáZarážka + j] =
							údajeMasky[spodnáZarážka + j];

					for (; j < šírka; ++j)
						údajeMasky[vrchnáZarážka + j] = prázdnaMaska;

					spodnáZarážka -= šírka;
					vrchnáZarážka -= šírka;
				}
			}
			else
			{
				zarážkaStĺpcov = Δx;

				for (int i = výška - 1; i >= zarážkaRiadkov; --i)
				{
					int j = šírka - 1;

					for (; j >= zarážkaStĺpcov; --j)
						údajeMasky[vrchnáZarážka + j] =
							údajeMasky[spodnáZarážka + j];

					for (; j >= 0; --j)
						údajeMasky[vrchnáZarážka + j] = prázdnaMaska;

					spodnáZarážka -= šírka;
					vrchnáZarážka -= šírka;
				}
			}

			vrchnáZarážka = (šírka * Δy) - 1;

			while (vrchnáZarážka >= 0)
			{
				údajeMasky[vrchnáZarážka] = prázdnaMaska;
				--vrchnáZarážka;
			}
		}
	}

	private void pretočMasku()
	{
		System.arraycopy(údajeMasky, 0, pomocnéÚdaje, 0, údajeMasky.length);

		int index1 = 0, index2 = šírka * (výška + 1 - Δy) - Δx;

		for (int i = Δy; i > 0; --i)
		{
			for (int j = Δx; j > 0; --j)
			{
				údajeMasky[index1++] =
					pomocnéÚdaje[index2++];
			}

			index2 -= šírka;

			for (int j = šírka - Δx; j > 0; --j)
			{
				údajeMasky[index1++] =
					pomocnéÚdaje[index2++];
			}

			index2 += šírka;
		}

		index2 = šírka - Δx;

		for (int i = výška - Δy; i > 0; --i)
		{
			for (int j = Δx; j > 0; --j)
			{
				údajeMasky[index1++] =
					pomocnéÚdaje[index2++];
			}

			index2 -= šírka;

			for (int j = šírka - Δx; j > 0; --j)
			{
				údajeMasky[index1++] =
					pomocnéÚdaje[index2++];
			}

			index2 += šírka;
		}
	}

	// Skopíruje jeden zo zásobníkov (podľa zadaného indexu) do údajov masky
	private void zásobníkDoMasky(int indexZásobníka)
	{
		for (int y = 0, i = 0, j = indexZásobníka; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
				údajeMasky[i] = mapaVĺn[j];
	}

	// Skopíruje údaje masky do jedného zo zásobníkov (podľa zadaného indexu)
	private void maskaDoZásobníka(int indexZásobníka)
	{
		for (int y = 0, i = 0, j = indexZásobníka; y < výška; ++y, j += 2)
			for (int x = 0; x < šírka; ++x, ++i, ++j)
				mapaVĺn[j] = (short)údajeMasky[i];
	}

	// Skopíruje údaje z mapy tekutiny do masky
	private void tekutinaDoMasky()
	{
		for (int y = 0, i = 0; y < výška; ++y)
			for (int x = 0; x < šírka; ++x, ++i)
				údajeMasky[i] = mapaTekutiny[i];
	}

	// Skopíruje údaje z masky do mapy tekutiny
	private void maskaDoTekutiny()
	{
		for (int y = 0, i = 0; y < výška; ++y)
			for (int x = 0; x < šírka; ++x, ++i)
				mapaTekutiny[i] = (byte)údajeMasky[i];
	}


	/**
	 * <p>Roluje mapu vlnenia o želaný počet bodov v horizontálnom
	 * a vertikálnom smere. Rolovanie znamená, že tá časť mapy vlnenia,
	 * ktorá opustí plochu mapy v ľubovoľnom smere bude stratená.</p>
	 * 
	 * <p class="warning"><b>Varovanie!</b> Rolovanie a pretáčanie údajov
	 * inštancií vlnenia je takmer 10-násobne výpočtovo náročnejšie
	 * v porovnaní s rolovaním obrázkov a plátien sveta. Navyše, výsledok
	 * rolovania nemusí spĺňať všetky očakávania, pretože simulácia vlnenia
	 * je ovplyvňovaná rozmermi vlnenej plochy – prirodzeným dôsledkom
	 * algoritmu totiž je aj jeho správanie, ktoré ústi do toho, že sa vlny
	 * odrážajú od hraníc celej vlnenej plochy rovnako ako od hraníc
	 * deaktivovaných oblastí.</p>
	 * 
	 * @param Δx posun v horizontálnom (vodorovnom) smere
	 * @param Δy posun vo vertikálnom (zvislom) smere
	 * 
	 * @see #pretoč(double, double)
	 */
	public void roluj(double Δx, double Δy)
	{
		this.Δx = (int)Δx;
		this.Δy = (int)-Δy;

		if (0 == this.Δx && 0 == this.Δy) return;

		prázdnaMaska = 0;

		zásobníkDoMasky(indexA);
		rolujMasku();
		maskaDoZásobníka(indexA);

		zásobníkDoMasky(indexB);
		rolujMasku();
		maskaDoZásobníka(indexB);

		prázdnaMaska = 255;

		tekutinaDoMasky();
		rolujMasku();
		maskaDoTekutiny();
	}

	/**
	 * <p>Pretočí mapu vlnenia o želaný počet bodov v horizontálnom
	 * a vertikálnom smere. Pretočenie znamená, že tá časť mapy vlnenia,
	 * ktorá opustí plochu mapy v jenom smere (napr. hore) bude doplnená
	 * v protiľahlom smere (napr. dole) a naopak.</p>
	 * 
	 * <p class="warning"><b>Varovanie!</b> Rolovanie a pretáčanie údajov
	 * inštancií vlnenia je takmer 10-násobne výpočtovo náročnejšie
	 * v porovnaní s rolovaním obrázkov a plátien sveta. Navyše, výsledok
	 * rolovania nemusí spĺňať všetky očakávania, pretože simulácia vlnenia
	 * je ovplyvňovaná rozmermi vlnenej plochy – prirodzeným dôsledkom
	 * algoritmu totiž je aj jeho správanie, ktoré ústi do toho, že sa vlny
	 * odrážajú od hraníc celej vlnenej plochy rovnako ako od hraníc
	 * deaktivovaných oblastí.</p>
	 * 
	 * @param Δx posun v horizontálnom (vodorovnom) smere
	 * @param Δy posun vo vertikálnom (zvislom) smere
	 * 
	 * @see #roluj(double, double)
	 */
	public void pretoč(double Δx, double Δy)
	{
		this.Δx = (int)Δx;
		this.Δy = (int)-Δy;

		while (this.Δx < 0) this.Δx += šírka;
		while (this.Δx >= šírka) this.Δx -= šírka;

		while (this.Δy < 0) this.Δy += výška;
		while (this.Δy >= výška) this.Δy -= výška;

		if (0 == this.Δx && 0 == this.Δy) return;

		zásobníkDoMasky(indexA);
		pretočMasku();
		maskaDoZásobníka(indexA);

		zásobníkDoMasky(indexB);
		pretočMasku();
		maskaDoZásobníka(indexB);

		tekutinaDoMasky();
		pretočMasku();
		maskaDoTekutiny();
	}

	/** <p><a class="alias"></a> Alias pre {@link #pretoč(double, double) pretoč}.</p> */
	public void pretoc(double Δx, double Δy)
	{
		pretoč(Δx, Δy);
	}


	/**
	 * <p>Vykoná proces vlnenia – prepočíta ďalšiu snímku. Táto metóda je
	 * programovacím rámcom používaná automaticky počas činnosti časovača.
	 * Vyžitie však môže nájsť v prípade {@linkplain #deaktivuj()
	 * deaktivovaných} inštancií vlnenia na krokové posúvanie simulácie
	 * vlnenia. Podobný účel plní metóda {@link #simuluj(int) simuluj}, ktorá
	 * je schopná posunúť simuláciu vlnenia o niekoľko snímok dopredu. (Tá
	 * nie je programovacím rámcom používaná automaticky.)</p>
	 * 
	 * @see #simuluj(int)
	 */
	public void vykonaj()
	{
		// Vymení mapu pred každým prepočtom:
		int i = indexA; indexA = indexB; indexB = i;

		// Inicializuje tri indexy používané počas prepočtov:
		//  i – pozícia v rámci zvlnených (cieľových) údajov
		//  j – pozícia v rámci časti A zásobníka s mapou vĺn
		//  k – pozícia v rámci časti B zásobníka s mapou vĺn
		i = 0; int j = indexA, k = indexB;

		try
		{
			for (int y = 0; y < výška; ++y, j += 2, k += 2)
				for (int x = 0; x < šírka; ++x, ++i, ++j, ++k)
				{
					short údaj;

					if (0 == mapaTekutiny[i])
						údaj = (short)(1530 - mapaVĺn[k]);
					else
					{
						// Vypočíta súčet hodnôt susedných pixelov a vydelí ho
						// dvomi (z dôvodu optimalizácie použije bitový posun):
						údaj = (short)((
							mapaVĺn[j - šírka - 2] +
							mapaVĺn[j + šírka + 2] +
							mapaVĺn[j - 1] +
							mapaVĺn[j + 1]) >> 1);

						// Odčíta hodnotu od mapy aktuálneho stavu:
						údaj -= mapaVĺn[k];

						// Ak by údaj bol ponechaný v tomto stave (výpočty
						// vyššie), tak by vlnenie nikdy neprestalo, preto
						// ho treba nejakým spôsobom tlmiť. Najrealistickejší
						// spôsob je znížíť výslednú výšku o zlomok vlastnej
						// hodnoty. (Ide vlastne o zníženie v percentách.) Aj
						// na toto je použitý bitový posun. Táto hodnota je
						// parametrizovateľná, pričom pôvodne zadávaný „útlm“
						// musel byť prepočítaný na „mohutnosť“, pretože čím
						// je hodnota/úroveň bitového posunu vyššia, tým je
						// úbytok amplitúdy vĺn pomalší.
						údaj -= údaj >> mohutnosť;

						// Prepočítaný údaj sa zapíše späť do aktuálneho
						// stavu:
						mapaVĺn[k] = údaj;


						// Teraz je potrebné zdeformovať pôvodný obrázok na
						// základe výšky/amplitúdy vĺn. To sa dosahuje pomocou
						// výpočtu vysunutia bodu zobrazovaného z pôvodného
						// obrázka na aktuálnej pozícii. Presne ako pri
						// reálnej vodnej hladine – lúče prenikajúce cez
						// vodnú hladinu sú lámané. Výpočet vysunutia je
						// založený na aktuálnej vzdialenosti od stredu mapy
						// vlnenia a mohutnosti vlny na aktuálnej pozícii.

						// Nulové hodnoty údajov znamenajú pokoj, kladné sú
						// vlny:
						údaj = (short)(1530 - údaj);
					}

					// Výpočet vysunutia:
					int dx = ((x - polŠírky) * údaj / 1530) + polŠírky;
					int dy = ((y - polVýšky) * údaj / 1530) + polVýšky;

					// Overenie hraníc – aby vysunutie nezasahovalo mimo
					// hraníc obrázka:
					if (dx >= šírka) dx = šírka - 1;
					if (dx < 0) dx = 0;
					if (dy >= výška) dy = výška - 1;
					if (dy < 0) dy = 0;

					// Premieta body z podkladu na základe vypočítaného
					// vysunutia:
					zvlnenéÚdaje[i] = údajeZdroja[dx + (dy * šírka)];
				}
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}
	}

	/**
	 * <p>Vykoná výpočet niekoľkých snímok procesu vlnenia naraz – posunie
	 * simuláciu vlnenia o niekoľko snímok dopredu. Táto metóda nie je
	 * používaná programovacím rámcom automaticky. Slúži na získanie rastra,
	 * ktorý je deformovaný v určitej pokročilej fáze vlnenia. V súčinnosti
	 * s {@linkplain #deaktivuj() deaktivovaním} automatického prepočítavania
	 * počas časovača môže byť táto metóda využitá na získanie statických
	 * snímok (rastrov) deformovaných algoritmom vlnenia, ktorý bol vopred
	 * posunutý do určitej fázy, v ktorej ho ponecháme zamrazený a tým
	 * využiť tento algoritmus napríklad na simulovanie zvlneného skla.</p>
	 * 
	 * @param počet počet snímok, ktoré majú byť prepočítané
	 * 
	 * @see #vykonaj()
	 */
	public void simuluj(int počet)
	{
		// Komentáre algoritmu boli v tele tejto metódy redukované.
		// Podrobnejšie komentáre sú v tele metódy vykonaj().

		// Táto metóda je efektívnejšia len pre vyšší počet snímok:
		if (počet > 1) try
		{
			int i, k;

			for (int n = 0; n < počet; ++n)
			{
				// Vymení mapu pred každým prepočtom:
				i = indexA; indexA = indexB; indexB = i;

				// Inicializuje indexy používané počas prepočtov:
				i = 0; int j = indexA; k = indexB;

				for (int y = 0; y < výška; ++y, j += 2, k += 2)
					for (int x = 0; x < šírka; ++x, ++i, ++j, ++k)
					{
						short údaj;

						if (0 == mapaTekutiny[i])
							údaj = (short)(1530 - mapaVĺn[k]);
						else
						{
							// Vypočíta zlomok súčtu susedných hodnôt:
							údaj = (short)((
								mapaVĺn[j - šírka - 2] +
								mapaVĺn[j + šírka + 2] +
								mapaVĺn[j - 1] +
								mapaVĺn[j + 1]) >> 1);

							// Prepočíta hodnotu aktuálneho pixela:
							údaj -= mapaVĺn[k];
							údaj -= údaj >> mohutnosť;
							mapaVĺn[k] = údaj;
						}
					}
			}

			// Inicializuje indexy:
			i = 0; k = indexB;

			// Prepočet hodnôt aktuálneho rastra, ktorý bol posunutý
			// o niekoľko snímok dopredu:
			for (int y = 0; y < výška; ++y, k += 2)
				for (int x = 0; x < šírka; ++x, ++i, ++k)
			{
				// Nulové hodnoty údajov znamenajú pokoj, kladné sú vlny:
				short údaj = (short)(1530 - mapaVĺn[k]);

				// Výpočet vysunutia:
				int dx = ((x - polŠírky) * údaj / 1530) + polŠírky;
				int dy = ((y - polVýšky) * údaj / 1530) + polVýšky;

				// Overenie hraníc:
				if (dx >= šírka) dx = šírka - 1;
				if (dx < 0) dx = 0;
				if (dy >= výška) dy = výška - 1;
				if (dy < 0) dy = 0;

				// Priemet bodov z podkladu na základe vypočítaného
				// vysunutia:
				zvlnenéÚdaje[i] = údajeZdroja[dx + (dy * šírka)];
			}
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}
		// Posunutie o jedinú snímku vykoná efektívnejšie metóda vykonaj():
		else if (1 == počet) vykonaj();
	}

	private void aktualizuj()
	{
		// Komentáre v tele tejto metódy sú redukované.
		// Podrobnejšie informácie sú v tele metódy vykonaj().

		try
		{
			// Inicializuje indexy:
			int i = 0, k = indexB;

			// Prepočet hodnôt aktuálneho rastra:
			for (int y = 0; y < výška; ++y, k += 2)
				for (int x = 0; x < šírka; ++x, ++i, ++k)
			{
				// Prevzatie hodnoty z mapy vĺn. Nulové hodnoty znamenajú
				// pokoj, kladné sú vlny:
				short údaj = (short)(1530 - mapaVĺn[k]);

				// Výpočet vysunutia:
				int dx = ((x - polŠírky) * údaj / 1530) + polŠírky;
				int dy = ((y - polVýšky) * údaj / 1530) + polVýšky;

				// Overenie hraníc:
				if (dx >= šírka) dx = šírka - 1;
				if (dx < 0) dx = 0;
				if (dy >= výška) dy = výška - 1;
				if (dy < 0) dy = 0;

				// Aktualizácia zvlneného rastra:
				zvlnenéÚdaje[i] = údajeZdroja[dx + (dy * šírka)];
			}
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}
	}


	/**
	 * <p>Táto metóda vráti rastrový obrázok inštancie typu
	 * {@link BufferedImage BufferedImage} obsahujúci aktuálny zvlnený
	 * obraz svojej predlohy. To znamená, že obraz v tejto inštancii je
	 * pravidelne aktualizovaný a obsahuje algoritmicky prepočítaný zvlnený
	 * obraz zdroja – predlohy, ktorá bola zadaná pri
	 * {@linkplain Vlnenie#Vlnenie(BufferedImage, int) konštrukcii tejto
	 * inštancie vlnenia}.</p>
	 * 
	 * <p>Rámec je vnútorne schopná rozpoznať, či má jeho konkrétna
	 * súčasť (napríklad {@linkplain Plátno plátno} alebo
	 * {@linkplain Obrázok obrázok}) zapnutý mechanizmus vlnenia
	 * a ak je mechanizmus zapnutý, tak je počas kreslenia vykonaného
	 * v rámci programovacieho rámca vždy nakreslený zvlnený raster. Ak by
	 * ste však potrebovali zvlnený raster objektu použiť mimo
	 * programovacieho rámca, tak bez tejto metódy by to nebolo možné,
	 * pretože samotná inštancia objektu (to jest napríklad {@linkplain 
	 * Obrázok obrázka}) navonok vždy reprezentuje len svoju nezvlnenú
	 * verziu.</p>
	 * 
	 * <p><small>(<b>Pripomenutie faktov:</b> Animácia vlnenia použitého
	 * v rámci programovacieho rámca je vykonávaná automaticky počas činnosti
	 * časovača a zvlnený raster je v rámci programovacieho rámca tiež použitý
	 * automaticky v čase kreslenia objektu.)</small></p>
	 * 
	 * @return inštancia obsahujúca raster zvlnenej verzie tohto
	 *     obrázka alebo {@code valnull}, ak vlnenie pre tento obrázok
	 *     nie je aktívne
	 */
	public BufferedImage zvlnenýRaster()
	{
		if (!aktívne && null != zvlnenéÚdaje && null != údajeZdroja)
			aktualizuj();
		return zvlnenýRaster;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zvlnenýRaster() zvlnenýRaster}.</p> */
	public BufferedImage zvlnenyRaster() { return zvlnenýRaster(); }
}
