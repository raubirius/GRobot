
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

 // Táto trieda bola do verzie 1.85 vnorenou triedou ústrednej triedy GRobot.
 // Po tejto verzii sa osamostatnila a teraz tvorí samostatnú triedu balíčka
 // programového rámca skupiny tried grafického robota.

package knižnica;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap; // (Na väčšinu účelov je výhodnejšia trieda TreeMap.)
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;


import knižnica.podpora.AnimatedGifEncoder;
import knižnica.podpora.GifDecoder;
import static knižnica.Farebnosť.*;
import static knižnica.Konštanty.ČÍTANIE_PNG_SEKVENCIE;
import static knižnica.Konštanty.CHYBA_ČÍTANIA_PNG_SEKVENCIE;
import static knižnica.Konštanty.ČÍTANIE_GIF_ANIMÁCIE;
import static knižnica.Konštanty.ZÁPIS_PNG_SEKVENCIE;
import static knižnica.Konštanty.ZÁPIS_GIF_ANIMÁCIE;


// ------------------------ //
//  *** Trieda Obrázok ***  //
// ------------------------ //

/**
 * <p>Obrázok je rozšírením triedy Javy {@link BufferedImage BufferedImage}
 * nielen o funkcionalitu bežne používanú vo svete grafického robota.
 * <!--   -->
 * Obrázok obsahuje komponent {@link #grafika grafika}, vďaka ktorému
 * môžeme do obrázkov {@linkplain GRobot#kresliDoObrázka(Obrázok) priamo
 * kresliť}.
 * <!--   -->
 * Trieda je využiteľná v rôznych situáciách, napríklad pri {@linkplain 
 * Plátno#vyplň(Image) vypĺňaní plátna}, definícii {@linkplain 
 * GRobot#vlastnýTvar(Image) vlastného tvaru} robota a podobne. Ponúka
 * nástroje na {@linkplain #prevráťVodorovne() zrkadlenie}, {@linkplain 
 * #bledší() zmenu svetlosti}, úpravu na {@linkplain #negatív() farebný
 * negatív}, {@linkplain #rozmaž() rozmazanie} a tak ďalej.</p>
 * 
 * <p class="attention"><b>Upozornenie:</b> Pri úpravách obrázkov (hoci
 * ich vlastnými metódami, napríklad {@link #prevráťVodorovne()
 * prevráťVodorovne}, {@link #bledší() bledší}, {@link #negatív()
 * negatív}, {@link #rozmaž() rozmaž} a podobne) nikdy nie je spustené
 * automatické prekreslenie! Všetky zmeny vykonané v obrázu sa prejavia
 * až pri jeho najbližšom nakreslení! Následkom je napríklad i to, že
 * ak je obrázok použitý ako vlastný tvar niektorého robota, treba po
 * každej zmene ({@linkplain #priehľadnosť(double) úprave priehľadnosti},
 * {@linkplain #vylejFarbu(Poloha, Color) vypĺňaní},
 * {@linkplain #kresli(Shape) kreslení}…) zabezpečiť {@linkplain 
 * Svet#prekresli() prekreslenie sveta}.</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Automatické prekresľovanie sveta
 * sa pri úpravách obsahu obrázkov alebo ich vlastností ich vlastnými
 * metódami neaktivuje napríklad i preto, že svet nemá nikdy garantované,
 * či je stanovený obrázok niekde použitý alebo iba pasívne uložený
 * v pamäti…)</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Nasledujúci príklad ukazuje možné použitie triedy
 * {@code currObrázok} ako vlastného tvaru viacerých robotov
 * znázorňujúcich hodiny (hodinových robotov). Obrázok je prekresľovaný
 * podľa potreby. Takéto použitie je v tomto prípade efektívnejšie,
 * pretože prekresľovanie tvaru je vykonávané len v prípade potreby
 * (v tomto prípade každú minútu) a prekreslený tvar dokáže využiť
 * množstvo robotov naraz.</p>
 * 
 * <p>Príklad obsahuje aj možnosť zobrazenia na celej obrazovke. Tá je
 * pred zrakom Javy zakrytá komentármi označenými dvojitou mriežkou:
 * {@code comm// ##}. Po odstránení všetkých takto označených znakov
 * komentárov (vrátane znakov dvojitej mriežky) bude príklad fungovať
 * v režime celej obrazovky.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdimport} java.util.{@link Calendar Calendar};

	{@code kwdpublic} {@code typeclass} ObrázokAkoTvar {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Spoločná inštancia obrázka, ktorá bude tvoriť vlastný tvar}
		{@code comm// hodinových robotov.}
		{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code currObrázok} tvarHodín = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num50}, {@code num50});

		{@code comm// Atribúty uchovávajúce starý a nový čas. Ak bude zistená zmena,}
		{@code comm// čo bude každú minútu, tak budú hodiny prekreslené.}
		{@code kwdprivate} {@code typeint} hodina0 = {@code num0}, minúta0 = {@code num0}, hodina1 = -{@code num1}, minúta1 = -{@code num1};

		{@code comm// Nebolo by efektívne overovať hodnoty času niekoľko ráz za sekundu,}
		{@code comm// preto je tu toto počítadlo, ktoré spôsobí, že overenie hodnôt času}
		{@code comm// bude vykonané raz za minútu.}
		{@code kwdprivate} {@code typeint} overČasPo = -{@code num1};

		{@code comm// ## private static int zariadenie = 0;}


		{@code comm// Súkromná trieda hodín.}
		{@code kwdprivate} {@code kwdstatic} {@code typeclass} Hodiny {@code kwdextends} {@link GRobot GRobot}
		{
			{@code comm// Konštruktor, ktorý nastaví všetkým hodinám rovnaké parametre}
			{@code comm// a náhodnú individuálnu rýchlosť.}
			{@code kwdprivate} Hodiny()
			{
				{@link GRobot#zdvihniPero() zdvihniPero}();
				{@link GRobot#vlastnýTvar(Image) vlastnýTvar}(tvarHodín);
				{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
				novýCieľ();
				{@link GRobot#gyroskop(Double) gyroskop}({@code num90.0});
				{@link GRobot#rýchlosť(double, boolean) rýchlosť}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num1.5}, {@code num3.5}), {@code valfalse});
			}

			{@code comm// Nový cieľ je nastavený na začiatku a potom vždy po dosiahnutí}
			{@code comm// starého cieľa.}
			{@code kwdprivate} {@code typevoid} novýCieľ()
			{
				{@link Bod Bod} štart = {@link GRobot#poloha() poloha}();
				{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
				{@link Bod Bod} cieľ = {@link GRobot#poloha() poloha}();
				{@link GRobot#skočNa(Poloha) skočNa}(štart);
				{@link GRobot#cieľ(Poloha) cieľ}(cieľ);
				{@link GRobot#kružnica() kružnica}();
			}

			{@code comm// Reakcia na dosiahnutie cieľa hodinami.}
			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#dosiahnutieCieľa() dosiahnutieCieľa}()
			{
				novýCieľ();
			}
		}


		{@code comm// Konštruktor hlavného robota. Sú v ňom vykonané potrebné nastavenia}
		{@code comm// a vytvorené hodinové roboty.}
		{@code kwdprivate} ObrázokAkoTvar()
		{
			{@code comm// ## super(Svet.šírkaZariadenia(zariadenie),}
			{@code comm// ## 	Svet.výškaZariadenia(zariadenie));}

			{@code comm// ## Svet.celáObrazovka(zariadenie);}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
			{@link Svet Svet}.{@link Svet#farbaPozadia(Color) farbaPozadia}({@link Farebnosť#čierna čierna});

			{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(tvarHodín);
			{@link GRobot#skry() skry}();
			{@link GRobot#veľkosť(double) veľkosť}({@code num22.0});
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num2.2});
			{@link GRobot#farba(Color) farba}({@link Farebnosť#svetlotyrkysová svetlotyrkysová});

			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num50}; ++i)
				{@code kwdnew} Hodiny();

			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}

		{@code comm// Zistenie nových hodnôt času. Metóda používa triedu Javy Calendar.}
		{@code kwdprivate} {@code typevoid} dajČas()
		{
			{@link Calendar Calendar} teraz = {@link Calendar Calendar}.{@link Calendar#getInstance() getInstance}();
			hodina0 = teraz.{@link Calendar#get(int) get}({@link Calendar Calendar}.{@link Calendar#HOUR HOUR});
			minúta0 = teraz.{@link Calendar#get(int) get}({@link Calendar Calendar}.{@link Calendar#MINUTE MINUTE});
		}

		{@code comm// Metóda slúžiaca na nakreslenie tvaru hodín. Kreslenie je vykonané len}
		{@code comm// v prípade, že sa hodnoty času zmenili, pričom hodnoty časových údajov}
		{@code comm// sú overované každú minútu.}
		{@code kwdprivate} {@code typevoid} nakresliHodiny()
		{
			{@code comm// Hodnota času je overovaná každú minútu. Predvolená hodnota}
			{@code comm// časovača je 40 ms, to je 25 ráz za sekundu, preto je čas overený}
			{@code comm// každých 1 500 tikov.}
			{@code kwdif} ({@code num0} == ++overČasPo % {@code num1500}) dajČas();

			{@code comm// Kreslenie je vykonané len v prípade, že sa hodnoty času zmenili.}
			{@code kwdif} (hodina1 != hodina0 || minúta1 != minúta0)
			{
				{@code comm// Vymazanie starého tvaru hodín.}
				tvarHodín.{@link Obrázok#vymaž() vymaž}();

				{@code comm// Uloženie aktuálnej farby hlavného robota – tá bude použitá}
				{@code comm// na nakreslenie hodín.}
				{@link Farba Farba} farba = {@link GRobot#farba() farba}();
				{@link GRobot#domov() domov}();

				{@code comm// Farba pozadia sveta je použitá na vyplnenie tvaru hodín.}
				{@link GRobot#farba(Color) farba}({@link Svet Svet}.{@link Svet#farbaPozadia() farbaPozadia}());
				{@link GRobot#kruh() kruh}();

				{@code comm// Nakreslenie kružnice uloženou farbou.}
				{@link GRobot#farba(Color) farba}(farba);
				{@link GRobot#kružnica() kružnica}();

				{@code comm// Nakreslenie štyroch značiek hodín – na 12., 3., 6. a 9. hodine.}
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num4}; ++i)
				{
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#dopredu(double) dopredu}({@link GRobot#veľkosť() veľkosť}() * {@code num0.8});
					{@link GRobot#položPero() položPero}();
					{@link GRobot#dopredu(double) dopredu}({@link GRobot#veľkosť() veľkosť}() * {@code num0.2});
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#dozadu(double) dozadu}({@link GRobot#veľkosť() veľkosť}());
					{@link GRobot#doprava(double) doprava}({@code num90});
				}
				{@link GRobot#položPero() položPero}();

				{@code comm// Nakreslenie hodinovej ručičky.}
				{@link GRobot#domov() domov}();
				{@link GRobot#smer(double) smer}({@code num90} &#45; {@code num30} * hodina0 &#45; {@code num0.5} * minúta0);
				{@link GRobot#dopredu(double) dopredu}({@link GRobot#veľkosť() veľkosť}() * {@code num0.5});

				{@code comm// Nakreslenie minútovej ručičky.}
				{@link GRobot#domov() domov}();
				{@link GRobot#smer(double) smer}({@code num90} &#45; {@code num6} * minúta0);
				{@link GRobot#dopredu(double) dopredu}({@link GRobot#veľkosť() veľkosť}() * {@code num0.9});

				{@code comm// Synchronizácia hodnôt času – ďalšia zmena nastane o minútu.}
				hodina1 = hodina0;
				minúta1 = minúta0;
			}
		}

		{@code comm// Reakcia na tik časovača. Prekresľuje tvar hodín a svet (obidvoje len}
		{@code comm// ak je to potrebné).}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			nakresliHodiny();
			{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"obrazok-ako-vlastny-tvar.cfg"});
			{@code comm// ## if (Svet.početZariadení() > 1) ++zariadenie;}
			{@code kwdnew} ObrázokAkoTvar();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>hodinoveRoboty.png<alt/>Hodinové roboty.</image>Na ploche
 * plátna sa bude pohybovať niekoľko „hodinových robotov.“</p>
 * 
 * @see Svet#priečinokObrázkov(String)
 */
public class Obrázok extends BufferedImage implements Priehľadnosť
{
	// Zoznam obrázkov prečítaných zo súboru

		/*packagePrivate*/ static String priečinokObrázkov = "";

		/*packagePrivate*/ final static Vector<String> zoznamSúborovObrázkov =
			new Vector<>();
		/*packagePrivate*/ final static Vector<Image> zoznamObrázkov =
			new Vector<>();
		/*packagePrivate*/ final static Vector<Icon> zoznamIkon =
			new Vector<>();

	// Zoznam všetkých vytvorených obrázkov programovacieho rámca

		/*packagePrivate*/ final static Vector<Obrázok>
			zoznamObrázkovKnižnice = new Vector<>();

	// Zoznam všetkých práve animovaných obrázkov…
	/*packagePrivate*/ final static Vector<Obrázok> animácie = new Vector<>();


	// Vnútorná trieda na rôzne operácie s objektami typu BufferedImage
	/*packagePrivate*/ static class VykonajVObrázku
	{
		// Matice pre pole „rozmazanie“
		private final static float[][] maticeRozmazania = {
			{0.070f, 0.120f, 0.070f,
			0.120f, 0.240f, 0.120f,
			0.070f, 0.120f, 0.070f},

			{0.006f, 0.027f, 0.036f, 0.027f, 0.006f,
			0.027f, 0.056f, 0.071f, 0.056f, 0.027f,
			0.036f, 0.071f, 0.107f, 0.071f, 0.036f,
			0.027f, 0.056f, 0.071f, 0.056f, 0.027f,
			0.006f, 0.027f, 0.036f, 0.027f, 0.006f},

			{0.000f, 0.006f, 0.013f, 0.015f, 0.013f, 0.006f, 0.000f,
			0.006f, 0.017f, 0.026f, 0.030f, 0.026f, 0.017f, 0.006f,
			0.013f, 0.026f, 0.039f, 0.045f, 0.039f, 0.026f, 0.013f,
			0.015f, 0.030f, 0.045f, 0.060f, 0.045f, 0.030f, 0.015f,
			0.013f, 0.026f, 0.039f, 0.045f, 0.039f, 0.026f, 0.013f,
			0.006f, 0.017f, 0.026f, 0.030f, 0.026f, 0.017f, 0.006f,
			0.000f, 0.006f, 0.013f, 0.015f, 0.013f, 0.006f, 0.000f},

			{0.000f, 0.000f, 0.004f, 0.007f, 0.008f, 0.007f, 0.004f, 0.000f, 0.000f,
			0.000f, 0.006f, 0.011f, 0.014f, 0.015f, 0.014f, 0.011f, 0.006f, 0.000f,
			0.004f, 0.011f, 0.017f, 0.021f, 0.023f, 0.021f, 0.017f, 0.011f, 0.004f,
			0.007f, 0.014f, 0.021f, 0.028f, 0.031f, 0.028f, 0.021f, 0.014f, 0.007f,
			0.008f, 0.015f, 0.023f, 0.031f, 0.038f, 0.031f, 0.023f, 0.015f, 0.008f,
			0.007f, 0.014f, 0.021f, 0.028f, 0.031f, 0.028f, 0.021f, 0.014f, 0.007f,
			0.004f, 0.011f, 0.017f, 0.021f, 0.023f, 0.021f, 0.017f, 0.011f, 0.004f,
			0.000f, 0.006f, 0.011f, 0.014f, 0.015f, 0.014f, 0.011f, 0.006f, 0.000f,
			0.000f, 0.000f, 0.004f, 0.007f, 0.008f, 0.007f, 0.004f, 0.000f, 0.000f},

			{0.000f, 0.000f, 0.001f, 0.003f, 0.004f, 0.005f, 0.004f, 0.003f, 0.001f, 0.000f, 0.000f,
			0.000f, 0.002f, 0.005f, 0.007f, 0.009f, 0.010f, 0.009f, 0.007f, 0.005f, 0.002f, 0.000f,
			0.001f, 0.005f, 0.008f, 0.011f, 0.014f, 0.014f, 0.014f, 0.011f, 0.008f, 0.005f, 0.001f,
			0.003f, 0.007f, 0.011f, 0.015f, 0.018f, 0.019f, 0.018f, 0.015f, 0.011f, 0.007f, 0.003f,
			0.004f, 0.009f, 0.014f, 0.018f, 0.022f, 0.024f, 0.022f, 0.018f, 0.014f, 0.009f, 0.004f,
			0.005f, 0.010f, 0.014f, 0.019f, 0.024f, 0.029f, 0.024f, 0.019f, 0.014f, 0.010f, 0.005f,
			0.004f, 0.009f, 0.014f, 0.018f, 0.022f, 0.024f, 0.022f, 0.018f, 0.014f, 0.009f, 0.004f,
			0.003f, 0.007f, 0.011f, 0.015f, 0.018f, 0.019f, 0.018f, 0.015f, 0.011f, 0.007f, 0.003f,
			0.001f, 0.005f, 0.008f, 0.011f, 0.014f, 0.014f, 0.014f, 0.011f, 0.008f, 0.005f, 0.001f,
			0.000f, 0.002f, 0.005f, 0.007f, 0.009f, 0.010f, 0.009f, 0.007f, 0.005f, 0.002f, 0.000f,
			0.000f, 0.000f, 0.001f, 0.003f, 0.004f, 0.005f, 0.004f, 0.003f, 0.001f, 0.000f, 0.000f},
		};

		// Objekty metódy „rozmaž“
		private final static ConvolveOp rozmazanie[] = {
			new ConvolveOp(new Kernel(3, 3, maticeRozmazania[0]),
				ConvolveOp.EDGE_NO_OP, null),
			new ConvolveOp(new Kernel(5, 5, maticeRozmazania[1]),
				ConvolveOp.EDGE_NO_OP, null),
			new ConvolveOp(new Kernel(7, 7, maticeRozmazania[2]),
				ConvolveOp.EDGE_NO_OP, null),
			new ConvolveOp(new Kernel(9, 9, maticeRozmazania[3]),
				ConvolveOp.EDGE_NO_OP, null),
			new ConvolveOp(new Kernel(11, 11, maticeRozmazania[4]),
				ConvolveOp.EDGE_NO_OP, null),
		};


		// Zdroj pre „vylejFarbu“: http://lodev.org/cgtutor/floodfill.html

		private static int x = 0;
		private static int y = 0;

		private static int index = 0;

		// Zásobník pre „vylejFarbu“
		private final static int počiatočnáVeľkosťZásobníka = 300000;
		private static int[] zásobníkX = new int[počiatočnáVeľkosťZásobníka];
		private static int[] zásobníkY = new int[počiatočnáVeľkosťZásobníka];
		private static int[] zásobníkIndexov = new int[počiatočnáVeľkosťZásobníka];
		private static int ukazovateľZásobníka = -1;

		private static int[] údajeObrázka = null;
		private static int[] údajeMasky = null;
		private static int šírka = 0;
		private static int výška = 0;

		private static int aktuálnaFarba = 0;
		private static int podkladováFarba = 0;

		private static int A = 0, R = 0, G = 0, B = 0;


		// Táto a ďalšie štyri metódy patria k metóde „vylejFarbu“
		private static void zväčšiZásobník()
		{
			zásobníkX = Arrays.copyOf(zásobníkX, zásobníkX.length * 2);
			zásobníkY = Arrays.copyOf(zásobníkY, zásobníkY.length * 2);
			zásobníkIndexov = Arrays.copyOf(zásobníkIndexov, zásobníkIndexov.length * 2);
		}

		private static boolean vyberZoZásobníka()
		{
			if (ukazovateľZásobníka >= 0)
			{
				x = zásobníkX[ukazovateľZásobníka];
				y = zásobníkY[ukazovateľZásobníka];
				index = zásobníkIndexov[ukazovateľZásobníka];
				// index = x + y * šírka;
				--ukazovateľZásobníka;
				return true;
			}

			return false;
		}

		private static void vložDoZásobníka(int x, /* int y, */int index)
		{
			while (ukazovateľZásobníka >= zásobníkX.length)
				zväčšiZásobník();

			++ukazovateľZásobníka;
			zásobníkX[ukazovateľZásobníka] = x;
			zásobníkY[ukazovateľZásobníka] = y;
			zásobníkIndexov[ukazovateľZásobníka] = index;
		}

		private static void vyprázdniZásobník()
		{
			ukazovateľZásobníka = -1;
		}

		// Pri vypĺňaní farbou „z plechovky“ (semienkové vypĺňanie)
		// vznikali na okrajoch vypĺňanej oblasti aliasy, ktoré rieši
		// táto metóda s pomocou miešania farieb
		private static void riešAlias()
		{
			///////////////////////////////////////////////////////////////
			// Zdroj:
			//    http://billyjin.kodingen.com/punbb-1.3.4/viewtopic.php?id=372
			// 
			// Na miešanie farieb s použitím alfa kanálov, môžete použiť
			// nasledujúce vzorce:
			// 
			//    ax = 1 - (1 - a) * (1 - A)
			//    rx = r * a / ax + R * A * (1 - a) / ax
			//    gx = g * a / ax + G * A * (1 - a) / ax
			//    bx = b * a / ax + B * A * (1 - a) / ax
			//
			// (r, g, b, a) je farba kresby. (R, G, B, A) je farba pozadia.
			// (rx, gx, bx, ax) je výsledná farba.
			// 
			// Poznámka: Všetky použité premenné sú reálne čísla v rozsahu
			//    od 0.0 do 1.0. Ak chcete používať celočíselný rozsah od
			//    0 do 255, musíte všetko náležite násobiť alebo deliť
			//    hodnotou 255.
			//

			int xs = (x > 0) ? (x - 1) : 0;
			int xh = (x + 1 < šírka) ? (x + 1) : x;
			int ys = (y > 0) ? (y - 1) : 0;
			int yh = (y + 1 < výška) ? (y + 1) : y;

			// Osembodové vyhladzovanie:
			// 
			// for (int xx = xs; xx <= xh; ++xx)
			// {
			//	for (int yy = ys; yy <= yh; ++yy)
			//	{

			// Štvorbodové vyhladzovanie:
			// 
			for (int i = 0; i < 4; ++i)
			{
				int xx = (i % 2 == 0) ? xs : xh;
				int yy = (i / 2 == 0) ? ys : yh;
				{
					int farba = údajeObrázka[xx + yy * šírka];
					if (farba != podkladováFarba && farba != aktuálnaFarba)
					{
						int a = (farba >> 24) & 0xff;
						// if (a == 255 && A == 255) nemožné, komplikované
						if (a < 255)
						{
							if (a == 0)
							{
								údajeObrázka[xx + yy * šírka] =
									aktuálnaFarba;
							}
							else
							{
								int r = (farba >> 16) & 0xff;
								int g = (farba >>  8) & 0xff;
								int b  = farba        & 0xff;

								int ax = 65535 - ((255 - a) * (255 - A));
								int rx = (r * a * 255) +
									(R * A * (255 - a));
								int gx = (g * a * 255) +
									(G * A * (255 - a));
								int bx = (b * a * 255) +
									(B * A * (255 - a));
								rx /= ax; gx /= ax; bx /= ax;

								údajeObrázka[xx + yy * šírka] =
									(((((ax << 8) | rx) << 8) | gx)
										<< 8) | bx;
							}
						}
					}
				}
			}
		}


		public static void negatív(BufferedImage obrázok)
		{
			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			for (int i = 0; i < údajeObrázka.length; ++i)
				údajeObrázka[i] ^= 0x00ffffff;
		}


		public static void zrušPriehľadnosť(BufferedImage obrázok)
		{
			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			for (int i = 0; i < údajeObrázka.length; ++i)
				údajeObrázka[i] |= 0xff000000;
		}


		public static boolean použiMasku(BufferedImage obrázok,
			BufferedImage maska)
		{
			if (obrázok.getWidth() != maska.getWidth() ||
				obrázok.getHeight() != maska.getHeight()) return false;

			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			údajeMasky = ((DataBufferInt)maska.getRaster().
				getDataBuffer()).getData();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				aktuálnaFarba = údajeMasky[i];
				A = (aktuálnaFarba >> 24) & 0xff;
				int a = (údajeObrázka[i] >> 24) & 0xff;

				if (A == 0 || a == 0) aktuálnaFarba = 0; else
				{
					R = (aktuálnaFarba >> 16) & 0xff;
					G = (aktuálnaFarba >>  8) & 0xff;
					B =  aktuálnaFarba        & 0xff;
					aktuálnaFarba = ((765 - R - G - B) * A * a) / 0x2fa03;
					//	0x2fa03 = 195075 = 765 * 255	// 765
				}

				údajeObrázka[i] = (údajeObrázka[i] & 0x00ffffff) |
					(aktuálnaFarba << 24);
			}

			return true;
		}

		public static boolean vymažKresbu(BufferedImage obrázok,
			BufferedImage kresba)
		{
			if (obrázok.getWidth() != kresba.getWidth() ||
				obrázok.getHeight() != kresba.getHeight()) return false;

			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			údajeMasky = ((DataBufferInt)kresba.getRaster().
				getDataBuffer()).getData();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				aktuálnaFarba = údajeMasky[i];
				R = (aktuálnaFarba >> 16) & 0xff;
				G = (aktuálnaFarba >>  8) & 0xff;
				B =  aktuálnaFarba        & 0xff;
				// A = (aktuálnaFarba >> 24) & 0xff;
				A = R + G + B; A /= 3; A = 255 - A;
				int a = (údajeObrázka[i] >> 24) & 0xff;

				if (A == 0 || a == 0) aktuálnaFarba = 0;
				else if (A == 255 && a == 255) aktuálnaFarba = 255;
				else
				{
					// Toto vzniklo asi dôsledkom kopírovania z inej metódy,
					// pričom dôležitosť tohto fragmentu v čase kopírovania
					// kódu bola iba zdanlivá; zbytočne znižovala efektívnosť
					// algoritmu:
					// R = (aktuálnaFarba >> 16) & 0xff;
					// G = (aktuálnaFarba >>  8) & 0xff;
					// B =  aktuálnaFarba        & 0xff;
					// aktuálnaFarba = ((765 - R - G - B) * A * a) / 0x2fa03;
					// //	0x2fa03 = 195075 = 765 * 255	// 765
					aktuálnaFarba = (A * a) / 255;
				}

				údajeObrázka[i] = (údajeObrázka[i] & 0x00ffffff) |
					(aktuálnaFarba << 24);
			}

			return true;
		}

		public static BufferedImage vyrobMasku(BufferedImage obrázok,
			BufferedImage nováMaska)
		{
			if (null == nováMaska) nováMaska = new BufferedImage(
				obrázok.getWidth(), obrázok.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
			else if (obrázok.getWidth() != nováMaska.getWidth() ||
				obrázok.getHeight() != nováMaska.getHeight()) return null;

			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			údajeMasky = ((DataBufferInt)nováMaska.getRaster().
				getDataBuffer()).getData();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				/*
				aktuálnaFarba = údajeObrázka[i];
				A = (aktuálnaFarba >> 24) & 0xff;

				if (A == 0) aktuálnaFarba = 0; else
				{
					R = (aktuálnaFarba >> 16) & 0xff;
					G = (aktuálnaFarba >>  8) & 0xff;
					B =  aktuálnaFarba        & 0xff;
					aktuálnaFarba = ((765 - R - G - B) * A) / 765;
				}

				údajeMasky[i] = aktuálnaFarba << 24;
				*/

				údajeMasky[i] = údajeObrázka[i] & 0xff000000;
			}

			return nováMaska;
		}

		public static void rozmaž(BufferedImage obrázok, Graphics2D
			grafika, int opakovanie, int rozsah, Color farbaPozadia)
		{
			if (rozsah < 1 || opakovanie < 1) return;
			if (--rozsah >= rozmazanie.length)
				rozsah = rozmazanie.length - 1;

			// šírka = obrázok.getWidth();
			// výška = obrázok.getHeight();

			// BufferedImage naRozmazanie = new BufferedImage(šírka + 2,
			//	výška + 2, BufferedImage.TYPE_INT_ARGB);

			BufferedImage naRozmazanie =
				new BufferedImage(obrázok.getWidth() + (rozsah + 1) * 2,
					obrázok.getHeight() + (rozsah + 1) * 2,
					BufferedImage.TYPE_INT_ARGB);

			naRozmazanie.createGraphics().drawImage(obrázok,
				rozsah + 1, rozsah + 1, null);

			údajeObrázka = ((DataBufferInt)naRozmazanie.getRaster().
				getDataBuffer()).getData();
			aktuálnaFarba = farbaPozadia.getRGB() & 0x00ffffff;

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				if (0 == ((údajeObrázka[i] >> 24) & 255))
					údajeObrázka[i] = aktuálnaFarba;
			}

			for (int i = 0; i < opakovanie; ++i)
				naRozmazanie = rozmazanie[rozsah].
					filter(naRozmazanie, null);

			Shape clipZáloha = grafika.getClip();
			if (null == clipZáloha)
			{
				Arrays.fill(((DataBufferInt)obrázok.getRaster().
					getDataBuffer()).getData(), 0);

				grafika.drawImage(naRozmazanie,
					-rozsah - 1, -rozsah - 1, null);
			}
			else
			{
				BufferedImage zálohaObrázka = new BufferedImage(
					obrázok.getWidth(), obrázok.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
				Arrays.fill(((DataBufferInt)zálohaObrázka.getRaster().
					getDataBuffer()).getData(), 0);
				zálohaObrázka.createGraphics().drawImage(
					obrázok, 0, 0, null);

				Shape clip = new Rectangle.Double(0, 0,
					obrázok.getWidth(), obrázok.getHeight());
				Area oblasťA = new Area(clip);
				Area oblasťB = new Area(clipZáloha);
				oblasťA.exclusiveOr(oblasťB);

				Arrays.fill(((DataBufferInt)obrázok.getRaster().
					getDataBuffer()).getData(), 0);
				grafika.setClip(oblasťA);
				grafika.drawImage(zálohaObrázka, 0, 0, null);
				grafika.setClip(clipZáloha);
				grafika.drawImage(naRozmazanie,
					-rozsah - 1, -rozsah - 1, null);
			}

			// Svet.prekresli();
		}


		// Prepíše farbu bodu na obrázku podobne
		// ako metóda BufferedImage.setRGB(x, y, rgb)
		/*
			public static void prepíšBod(BufferedImage obrázok,
				double x0, double y0, Color farba)
			{
				šírka = obrázok.getWidth();
				výška = obrázok.getHeight();

				int x = (int)((šírka / 2.0) + x0);
				int y = (int)((výška / 2.0) - y0);

				if (x < 0 || x >= šírka || y < 0 || y >= výška) return;

				údajeObrázka = ((DataBufferInt)obrázok.getRaster().
					getDataBuffer()).getData();

				údajeObrázka[x + y * šírka] = farba.getRGB();
			}
		*/


		public static void vylejFarbu(BufferedImage obrázok, double x0,
			double y0, Color farba)
		{
			šírka = obrázok.getWidth();
			výška = obrázok.getHeight();

			x = (int)((šírka / 2.0) + x0);
			y = (int)((výška / 2.0) - y0);

			if (x < 0 || x >= šírka || y < 0 || y >= výška) return;

			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			aktuálnaFarba = farba.getRGB();

			index = x + y * šírka;
			podkladováFarba = údajeObrázka[index];

			if (podkladováFarba == aktuálnaFarba) return;

			A = (aktuálnaFarba >> 24) & 0xff;
			R = (aktuálnaFarba >> 16) & 0xff;
			G = (aktuálnaFarba >>  8) & 0xff;
			B =  aktuálnaFarba        & 0xff;

			boolean ľaváZarážka, praváZarážka;
			vyprázdniZásobník();
			vložDoZásobníka(x, /*y, */index);

			while (vyberZoZásobníka())
			{
				while (y >= 0 &&
				//	((údajeObrázka[index] == podkladováFarba) ||
				//	(((údajeObrázka[index] >> 24) & 0xff) < 8)))
					údajeObrázka[index] == podkladováFarba)
					{ --y; index -= šírka; }

				++y; index += šírka;
				ľaváZarážka = praváZarážka = false;

				while (y < výška &&
				//	((údajeObrázka[index] == podkladováFarba) ||
				//	(((údajeObrázka[index] >> 24) & 0xff) < 8)))
					údajeObrázka[index] == podkladováFarba)
				{
					údajeObrázka[index] = aktuálnaFarba;
					riešAlias();

					if (!ľaváZarážka && x > 0 &&
					//	((údajeObrázka[(x - 1) +
					//		y * šírka] == podkladováFarba) ||
					//	(((údajeObrázka[(x - 1) +
					//		y * šírka] >> 24) & 0xff) < 8)))
						údajeObrázka[index - 1] == podkladováFarba)
					{
						vložDoZásobníka(x - 1, /*y, */index - 1);
						ľaváZarážka = true;
					}
					else if (ľaváZarážka && x > 0 &&
					//	((údajeObrázka[(x - 1) +
					//		y * šírka] != podkladováFarba) &&
					//	(((údajeObrázka[(x - 1) +
					//		y * šírka] >> 24) & 0xff) >= 8)))
						údajeObrázka[index - 1] != podkladováFarba)
					{
						ľaváZarážka = false;
					}

					if (!praváZarážka && x < šírka - 1 &&
					//	((údajeObrázka[(x + 1) +
					//		y * šírka] == podkladováFarba) ||
					//	(((údajeObrázka[(x + 1) +
					//		y * šírka] >> 24) & 0xff) < 8)))
						údajeObrázka[index + 1] == podkladováFarba)
					{
						vložDoZásobníka(x + 1, /*y, */index + 1);
						praváZarážka = true;
					}
					else if (praváZarážka && x < šírka - 1 &&
					//	((údajeObrázka[(x + 1) +
					//		y * šírka] != podkladováFarba) &&
					//	(((údajeObrázka[(x + 1) +
					//		y * šírka] >> 24) & 0xff) >= 8)))
						údajeObrázka[index + 1] != podkladováFarba)
					{
						praváZarážka = false;
					}

					++y; index += šírka;
				}
			}
		}

		// Poznámka: Osamostatnené verzie nasledujúcich dvoch metód sú
		//           v komentároch na konci súboru TestovanieVlnenia.java.
			// 
			// Metódy roluj a pretoč sa usilujú o podobnú vec, ibaže
			// s jedným zásadným rozdielom. Roluj za sebou „maže stopy“
			// a pretoč zachováva všetky grafické informácie pôvodného
			// obrázka. Z toho plynie prvý zásadný rozdiel: roluj pracuje
			// iba s údajmi obrázka; pretoč potrebuje pomocný zásobník.
			// 
			// Ďalšie rozdiely plynú z toho ako metóda spracúva údaje.
			// Roluj najprv musí overiť, ktorým smerom sú údaje posúvané
			// a podľa toho zvoliť protiidúci smer presúvania sa údajov
			// (aby aktualizácia neprepísala ešte nepresunuté údaje).
			// Preto je metóda roluj o poznanie dlhšia. Metóda pretoč
			// iba zálohuje pôvodný obsah obrázka, rozdelí obraz na
			// štyri nerovnomerné „kvadranty“ a postupne skopíruje
			// obsahy jednotlivých „kvadrantov“ zo zálohy do originálu
			// podľa požadovanej miery posunutia.
		public static void roluj(BufferedImage obrázok, int Δx, int Δy)
		{
			if (Δx == 0 && Δy == 0) return;

			Δy = -Δy;
			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();
			šírka = obrázok.getWidth();
			výška = obrázok.getHeight();

			if (Δx >= šírka || Δx <= -šírka ||
				Δy >= výška || Δy <= -výška)
			{
				Arrays.fill(údajeObrázka, 0);
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
							údajeObrázka[spodnáZarážka + j] =
								údajeObrázka[vrchnáZarážka + j];

						for (; j < šírka; ++j)
							údajeObrázka[spodnáZarážka + j] = 0;

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
							údajeObrázka[spodnáZarážka + j] =
								údajeObrázka[vrchnáZarážka + j];

						for (; j >= 0; --j)
							údajeObrázka[spodnáZarážka + j] = 0;

						spodnáZarážka += šírka;
						vrchnáZarážka += šírka;
					}
				}

				vrchnáZarážka = šírka * výška;

				while (spodnáZarážka < vrchnáZarážka)
				{
					údajeObrázka[spodnáZarážka] = 0;
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
							údajeObrázka[vrchnáZarážka + j] =
								údajeObrázka[spodnáZarážka + j];

						for (; j < šírka; ++j)
							údajeObrázka[vrchnáZarážka + j] = 0;

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
							údajeObrázka[vrchnáZarážka + j] =
								údajeObrázka[spodnáZarážka + j];

						for (; j >= 0; --j)
							údajeObrázka[vrchnáZarážka + j] = 0;

						spodnáZarážka -= šírka;
						vrchnáZarážka -= šírka;
					}
				}

				vrchnáZarážka = (šírka * Δy) - 1;

				while (vrchnáZarážka >= 0)
				{
					údajeObrázka[vrchnáZarážka] = 0;
					--vrchnáZarážka;
				}
			}
		}

		public static void pretoč(BufferedImage obrázok, int Δx, int Δy)
		{
			if (Δx == 0 && Δy == 0) return;

			Δy = -Δy;
			údajeObrázka = ((DataBufferInt)obrázok.getRaster().
				getDataBuffer()).getData();

			údajeMasky = new int[údajeObrázka.length];

			System.arraycopy(údajeObrázka, 0,
				údajeMasky, 0, údajeObrázka.length);

			šírka = obrázok.getWidth();
			výška = obrázok.getHeight();

			while (Δx < 0) Δx += šírka;
			while (Δx >= šírka) Δx -= šírka;

			while (Δy < 0) Δy += výška;
			while (Δy >= výška) Δy -= výška;

			int index1 = 0, index2 = šírka * (výška + 1 - Δy) - Δx;

			for (int i = Δy; i > 0; --i)
			{
				for (int j = Δx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeMasky[index2++];
				}

				index2 -= šírka;

				for (int j = šírka - Δx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeMasky[index2++];
				}

				index2 += šírka;
			}

			index2 = šírka - Δx;

			for (int i = výška - Δy; i > 0; --i)
			{
				for (int j = Δx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeMasky[index2++];
				}

				index2 -= šírka;

				for (int j = šírka - Δx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeMasky[index2++];
				}

				index2 += šírka;
			}
		}
	}


	// Prevod obrázkov typu Image na BufferedImage nie je úplne
		// priamočiary, preto sme definovali túto pomocnú triedu:

		private static class PrevedenýObrázok
		{
			public final BufferedImage prevedený;
			public final Graphics2D grafika;
			public final int[] údaje;

			public PrevedenýObrázok(Image obrázok)
			{
				prevedený = new BufferedImage(obrázok.getWidth(null),
					obrázok.getHeight(null), BufferedImage.TYPE_INT_ARGB);

				grafika = prevedený.createGraphics();
				grafika.addRenderingHints(hints);

				údaje = ((DataBufferInt)prevedený.getRaster().
					getDataBuffer()).getData();
			}
		}

	// Zoznam obrázkov typu Image prevedených na BufferedImage
		// (v ideálnom prípade zostane zoznam prázdny, lebo sa bude
		// pracovať len s obrázkami typu BufferedImage, ale zaručené
		// to byť nemôže)
		private final static HashMap<Image, PrevedenýObrázok>
			prevedenéObrázky = new HashMap<>();

	// Táto metóda v prvom rade zistí, či zadaný objekt nie je
		// typu BufferedImage (alebo odvodeného), ak nie je overí, či
		// už nebol vykonávaný prevod – čiže či už sa obrázok nenachádza
		// v zozname prevedených obrázkov a až potom vyrobí novú
		// prevedenú inštanciu, ktorú zároveň uloží do spomenutého zoznamu
		/*packagePrivate*/ static BufferedImage preveďNaBufferedImage(Image obrázok)
		{
			// Ak sem prišla inštancia BufferedImage, tak ju priamo
			// posunieme ďalej:
			if (obrázok instanceof BufferedImage)
				return (BufferedImage)obrázok;

			// Ak nie, tak skúsime nájsť prevedený obrázok v zozname:
			PrevedenýObrázok prevedený = prevedenéObrázky.get(obrázok);

			// Ak tam nebol, tak ho vytvorí a uloží ho do zoznamu:
			if (null == prevedený)
			{
				prevedený = new PrevedenýObrázok(obrázok);
				prevedenéObrázky.put(obrázok, prevedený);
			}

			// Obsah obrázka môže byť medzičasom aktualizovaný,
			// preto ho treba zakaždým prekresliť… (Definovať na to
			// metódu som považoval za zbytočné a menej optimálne.)
			Arrays.fill(prevedený.údaje, 0);
			prevedený.grafika.drawImage(obrázok, 0, 0, null);
			return prevedený.prevedený;
		}


	// Transformuje názov súboru na obrázok

		/*packagePrivate*/ static BufferedImage súborNaObrázok(String súbor)
		{
			// Najskôr vyhľadáme obrázok vo vnútornom zozname
			// prečítaných obrázkov
			int indexOf;
			if (-1 != (indexOf = zoznamSúborovObrázkov.indexOf(súbor)))
				return preveďNaBufferedImage(
					zoznamObrázkov.elementAt(indexOf));

			// Ak nie je v zozname, prečítame ho zo súboru
			URL url = null;

			try
			{
				File súborSObrázkom = Súbor.nájdiSúbor(
					priečinokObrázkov + súbor);
				if (súborSObrázkom.canRead())
					url = súborSObrázkom.toURI().toURL();
			}
			// catch (MalformedURLException e)
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}

			if (null == url)
			{
				// Prečítaj z lokality URL (mimo pevného disku)
				try
				{
					url = new URL(súbor);
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}

			// V prípade, že by bol v .jar súbore
			if (null == url) url = Súbor.nájdiZdroj(
				priečinokObrázkov + súbor);
			if (null == url) throw new GRobotException("Obrázok „" +
				súbor + "“ nebol nájdený.", "imageNotFound", súbor);

			BufferedImage obrázok = null;

			try { obrázok = ImageIO.read(url); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e, true);
			return null; }

			zoznamSúborovObrázkov.add(súbor);
			zoznamObrázkov.add(obrázok);
			zoznamIkon.add(null);

			// bufferedImage.getGraphics().drawImage(image, 0, 0, null);

			return obrázok;
		}

	// Transformuje názov súboru na ikonu

		/*packagePrivate*/ static Icon súborNaIkonu(String súbor)
		{
			for (int i = 0; i < 2; ++i)
			{
				int indexOf;
				if (-1 != (indexOf =
					zoznamSúborovObrázkov.indexOf(súbor)))
				{
					if (null == zoznamIkon.elementAt(indexOf))
					{
						zoznamIkon.setElementAt(
							new ImageIcon(zoznamObrázkov.
								elementAt(indexOf)), indexOf);
					}
					return zoznamIkon.elementAt(indexOf);
				}

				súborNaObrázok(súbor);
			}
			return null;
		}

		/*packagePrivate*/ static Icon obrázokNaIkonu(Image obrázok)
		{
			int indexOf;

			if (-1 != (indexOf = zoznamObrázkov.indexOf(obrázok)))
			{
				if (null == zoznamIkon.elementAt(indexOf))
				{
					zoznamIkon.setElementAt(
						new ImageIcon(obrázok), indexOf);
				}
				return zoznamIkon.elementAt(indexOf);
			}

			Icon ikona = new ImageIcon(obrázok);
			zoznamSúborovObrázkov.add(null);
			zoznamObrázkov.add(obrázok);
			zoznamIkon.add(ikona);
			return ikona;
		}


	// Univerzálny objekt obrázka

		// Nasledujúce dve metódy overujú, či zadaný objekt nižšieho typu
		// (Image alebo BufferedImage) nie je vyššieho typu (Obrázok)
		// a či nemá byť vykonané kreslenie iného rastra než predloženého
		// (zatiaľ iba zvlnenej verzie rastra v prípade aktíneho vlnenia):

		/*packagePrivate*/ static Image dajRelevantnýRaster(Image obrázok)
		{
			if (obrázok instanceof Obrázok)
			{
				Obrázok overenie = (Obrázok)obrázok;
				if (null != overenie.vlnenie)
					return overenie.vlnenie.zvlnenýRaster();
			}

			return obrázok;
		}

		/*packagePrivate*/ static BufferedImage dajRelevantnýRaster(
			BufferedImage obrázok)
		{
			if (obrázok instanceof Obrázok)
			{
				Obrázok overenie = (Obrázok)obrázok;
				if (null != overenie.vlnenie)
					return overenie.vlnenie.zvlnenýRaster();
			}

			return obrázok;
		}


	// Inštancia „hints“ na pridávanie antialiasingu do všetkých
	// obrázkov sveta
	/*packagePrivate*/ final static RenderingHints hints = new RenderingHints(
		RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	static
	{
		hints.put(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
			RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	// Priehľadnosť obrázka
	/*packagePrivate*/ float priehľadnosť = 1.0f;

	// Posun grafiky obrázka (na úpravu súradnicového systému)
	/*packagePrivate*/ double posunX, posunY;

	// Nevyhnutné na vymazanie a používané pri niektorých operáciách
	/*packagePrivate*/ int[] údajeObrázka;
	private int[] údajeOperácie;

	// Faktor pre potreby metód bledší(), tmavší()…
	private final static double faktor = 0.7;

	// Používané pre viaceré aktívne filtre
	// private float[][] filterObrázka;
	// private double[][] filterObrázka;

	// Kreslič obrázka – robot, ktorý bude určený na kreslenie do obrázka
	private GRobot kreslič = null;

	// Kreslenie obrázka na zadané súradnice do zadaného grafického
	// objektu v súradnicovom priestore Javy(!)
	/*packagePrivate*/ void kresliNa(int x, int y, Graphics2D grafika)
	{
		if (priehľadnosť > 0)
		{
			if (priehľadnosť < 1)
			{
				Composite záloha = grafika.getComposite();
				grafika.setComposite(
					AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, priehľadnosť));
				if (null == vlnenie)
					grafika.drawImage(this, x, y, null);
				else
					grafika.drawImage(vlnenie.zvlnenýRaster(), x, y, null);
				grafika.setComposite(záloha);
			}
			else if (null == vlnenie)
				grafika.drawImage(this, x, y, null);
			else
				grafika.drawImage(vlnenie.zvlnenýRaster(), x, y, null);
		}
	}

	/*packagePrivate*/ void kresliNaStred(int x, int y, Graphics2D grafika)
	{
		if (priehľadnosť > 0)
		{
			if (priehľadnosť < 1)
			{
				Composite záloha = grafika.getComposite();
				grafika.setComposite(
					AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, priehľadnosť));
				if (null == vlnenie)
					grafika.drawImage(this,
						x - (šírka / 2), y - (výška / 2), null);
				else
					grafika.drawImage(vlnenie.zvlnenýRaster(),
						x - (šírka / 2), y - (výška / 2), null);
				grafika.setComposite(záloha);
			}
			else if (null == vlnenie)
				grafika.drawImage(this,
					x - (šírka / 2), y - (výška / 2), null);
			else
				grafika.drawImage(vlnenie.zvlnenýRaster(),
					x - (šírka / 2), y - (výška / 2), null);
		}
	}

	// Keď sa zmenia rozmery plátna, musí sa upraviť aj posun obrázkov,
	// inak by zostali kvázi nepoužiteľné (v súvislosti s kreslením
	// na nich)
	/*packagePrivate*/ void upravPosun()
	{
		grafika.translate(-posunX, -posunY);
		posunX = -(Plátno.šírkaPlátna / 2) + (šírka / 2);
		posunY = -(Plátno.výškaPlátna / 2) + (výška / 2);
		if (null != vlnenie) vlnenie.posun(posunX, posunY);
		grafika.translate(posunX, posunY);
	}

	/**
	 * <p>Šírka obrázka. Konštanta má rovnakú hodnotu, akú vracia metóda
	 * {@link #šírka() šírka}.</p>
	 */
	public final int šírka, sirka;

	/**
	 * <p>Výška obrázka. Konštanta má rovnakú hodnotu, akú vracia metóda
	 * {@link #výška() výška}.</p>
	 */
	public final int výška, vyska;

	/**
	 * <p>Grafika obrázka (pre potreby kreslenia do obrázka). Robot má
	 * dostatok nástrojov (metód) na kreslenie. Ak potrebujete priamy
	 * prístup ku {@linkplain Graphics2D grafickému objektu obrázka}
	 * (a využívať jeho metódy – ide o triedu {@link Graphics2D
	 * Graphics2D}), použite na prístup k nemu túto konštantu alebo
	 * metódu {@link #grafika() grafika}, ktorá vracia rovnaký objekt.
	 * Používajte tento objekt ojedinele, pretože inštancie triedy
	 * obrázok mierne prispôsobujú svoj súradnicový priestor, aby boli
	 * lepšie použiteľné v prostredí programovacieho rámca grafického
	 * robota.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Súradnicový priestor obrázka
	 * je posunutý tak, aby počiatok súradnicovej sústavy grafického
	 * robota ležal v strede obrázka. Predpokladá sa, že všetky
	 * objekty smerujúce do obrázka budú generované robotom a ten
	 * generuje tvary tak, aby boli priamo použiteľné v súradnicovom
	 * priestore Javy, ibaže v prepočte vzhľadom k rozmerom plátien,
	 * preto má obrázok svoj priestor posunutý.
	 * O súradnicových priestoroch sa podrobnejšie píše napríklad
	 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
	 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
	 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
	 * v celej dokumentácii.</p>
	 */
	public final Graphics2D grafika = this.createGraphics();
	{ grafika.addRenderingHints(hints); }

	/**
	 * <p>Predvolený konštruktor. Vytvorí nový prázdny obrázok s rozmermi
	 * plátna.</p>
	 */
	public Obrázok()
	{
		super(Plátno.šírkaPlátna, Plátno.výškaPlátna,
			BufferedImage.TYPE_INT_ARGB);
		sirka = this.šírka = Plátno.šírkaPlátna;
		vyska = this.výška = Plátno.výškaPlátna;
		this.posunX = this.posunY = 0;
		zoznamObrázkovKnižnice.add(this);
	}

	/**
	 * <p>Konštruktor, ktorý vytvorí nový prázdny obrázok so zadanými
	 * rozmermi (v bodoch).</p>
	 * 
	 * @param šírka šírka nového obrázka
	 * @param výška výška nového obrázka
	 */
	public Obrázok(int šírka, int výška)
	{
		super(šírka, výška, BufferedImage.TYPE_INT_ARGB);
		sirka = this.šírka = šírka; vyska = this.výška = výška;
		this.posunX = -(Plátno.šírkaPlátna / 2) + (šírka / 2);
		this.posunY = -(Plátno.výškaPlátna / 2) + (výška / 2);
		zoznamObrázkovKnižnice.add(this);
		grafika.translate(posunX, posunY);
	}

	/**
	 * <p>Vytvorí nový obrázok podľa zadaného obrázka ako predlohy. Nový
	 * obrázok bude mať rozmery aj obsah predlohy. Konštruktor najskôr
	 * vytvorí prázdny obrázok s rozmermi predlohy a potom do neho
	 * prekreslí obsah predlohy.
	 * <!--   -->
	 * Ak potrebujete vytvoriť nový obrázok prečítaný zo súboru, použite
	 * metódu {@link Obrázok Obrázok}{@code .}{@link 
	 * Obrázok#čítaj(String) čítaj}{@code (názovSúboru)}.</p>
	 * 
	 * @param obrázok obrázoky predlohy
	 */
	public Obrázok(Image obrázok)
	{
		super(obrázok.getWidth(null), obrázok.getHeight(null),
			BufferedImage.TYPE_INT_ARGB);
		sirka = this.šírka = getWidth(); vyska = this.výška = getHeight();
		this.posunX = -(Plátno.šírkaPlátna / 2) + (šírka / 2);
		this.posunY = -(Plátno.výškaPlátna / 2) + (výška / 2);
		zoznamObrázkovKnižnice.add(this);
		grafika.drawImage(obrázok, 0, 0, null);
		grafika.translate(posunX, posunY);
	}


	// Statické metódy

		/**
		 * <p>Ak sú všetky obrázky uložené v spoločnom priečinku, môžeme pre
		 * nich touto metódou nastaviť zdrojový priečinok čítania.
		 * Priečinok by sa mal nachádzať v hlavnom priečinku projektu alebo by
		 * k nemu mala viesť systémovo nezávislá relatívna cesta. Zadaním
		 * prázdneho reťazca alebo hodnoty {@code valnull} používanie
		 * priečinka zrušíme.</p>
		 * 
		 * @param priečinok názov priečinka, relatívna cesta, prípadne
		 *     prázdny reťazec alebo {@code valnull}
		 * 
		 * @see Svet#priečinokObrázkov()
		 */
		public static void priečinokObrázkov(String priečinok)
		{ priečinokObrázkov = Súbor.upravLomky(priečinok, true); }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokObrázkov(String) priečinokObrázkov}.</p> */
		public static void priecinokObrazkov(String priečinok)
		{ priečinokObrázkov = Súbor.upravLomky(priečinok, true); }

		/**
		 * <p>Vráti reťazec s aktuálnym priečinkom, z ktorého sú obrázky
		 * prečítané. Reťazec je obohatený o oddeľovací znak priečinkov {@link 
		 * File#separatorChar java.io.File.separatorChar} ({@code /} alebo
		 * {@code \} – záleží na type operačného systému), ktorý automaticky
		 * pridáva metóda {@link #priečinokObrázkov(String)
		 * priečinokObrázkov(priečinok)}. Rovnako všetky oddeľovacie znaky
		 * priečinkov v relatívnej ceste sú nahradené podľa typu operačného
		 * systému.</p>
		 * 
		 * @return aktuálny priečinok, z ktorého sú obrázky prečítané
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static String priečinokObrázkov()
		{ return priečinokObrázkov; }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokObrázkov() priečinokObrázkov}.</p> */
		public static String priecinokObrazkov()
		{ return priečinokObrázkov; }


		/**
		 * <p>Prečíta do vnútornej pamäte sveta zadaný obrázok zo súboru
		 * a vytvorí z neho nový objekt typu {@link Obrázok Obrázok}.</p>
		 * 
		 * <p>Táto metóda uzatvára funkcionalitu metódy {@link 
		 * Svet#čítajObrázok(String) Svet.čítajObrázok(súbor)}, ibaže
		 * naviac z objektu typu {@link Image Image} (ktorý zostáva uložený
		 * vo vnútornej pamäti sveta) automaticky vytvára nový objekt {@link 
		 * Obrázok Obrázok}.</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p>Táto metóda je schopná čítať aj animácie
		 * uložené vo formáte GIF (z dôvodu obmedzení tohto formátu ho
		 * neodporúčame používať na ukladanie nových animácií) a tiež sekvencie
		 * uložené v číslovaných obrázkových súboroch vo formáte PNG (čo je
		 * lepšia alternatíva na ukladanie plnofarebných animácií, na druhej
		 * strane má tú nevýhodu, že nevie automaticky uložiť informácie
		 * o trvaní zobrazenia jednotlivých snímok a predvolene pracuje
		 * s hodnotou 40 ms nastavenou pre každú snímku). Podrobnejšie
		 * informácie o PNG sekvenciách sú v opise metódy {@link #ulož(String,
		 * boolean) ulož(súbor, prepísať)}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Pri čítaní animácie z PNG
		 * sekvencie sú do vnútorného zoznamu zdrojov sveta uložené všetky
		 * obrázky sekvencie. (Príklad s PNG sekvenciou je uvedený nižšie,
		 * pod príkladom s animovaným GIFom.)</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Ako bolo povedané, programovací rámec dokáže čítať a pracovať
		 * s animovanými obrázkami vo formáte GIF. Je to historický formát
		 * s určitými (najmä farebnými) obmedzeniami, ale stále nachádza svoje
		 * použitie. Nasledujúci príklad ukazuje, ako takýto súbor prečítať
		 * a animovať s použitím programovacieho rámca GRobot.</p>
		 * 
		 * <p> <br /><a target="_blank" href="resources/srdce.gif">srdce.gif</a> –
		 * animovaný obrázok na prevzatie, ktorý potrebuje tento príklad na to,
		 * aby fungoval<br /> </p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} AnimujGIF {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Súkromná statická inštancia obrázka, ktorý budeme animovať. Statickosť}
				{@code comm// umožňuje jej použitie v hlavnej metóde, ktorá musí byť statická.}
				{@code kwdprivate} {@code kwdstatic} {@link Obrázok Obrázok} animovanýObrázok;

				{@code comm// Hlavná metóda. (Obvykle ju umiestňujeme na koniec súboru. Je to taká}
				{@code comm// dohoda, zvyk medzi programátormi v Jave, ale z pohľadu samotného jazyka}
				{@code comm// je jedno, kde je jej definícia umiestnená. Teraz sme ju presunuli sem,}
				{@code comm// lebo na komentáre v nej logicky nadväzujú komentáre v konštruktore}
				{@code comm// tejto triedy.)}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@code comm// Skryjeme svet (čiže okno aplikácie).}
					{@link Svet Svet}.{@link Svet#skry() skry}();

					{@code comm// Vopred prečítame animovaný obrázok zo súboru, aby sme podľa jeho}
					{@code comm// rozmerov mohli upraviť rozmery sveta v konštruktore:}
					animovanýObrázok = {@link Obrázok Obrázok}.{@code currčítaj}({@code srg"srdce.gif"});

					{@code comm// Vytvorenie anonymnej inštancie tejto triedy (konštruktor triedy}
					{@code comm// odvodenej od robota vždy zabezpečí vytvorenie aplikácie):}
					{@code kwdnew} AnimujGIF();

					{@code comm// Spustenie animácie:}
					animovanýObrázok.{@link Obrázok#spusti() spusti}();

					{@code comm// Spustenie časovača:}
					{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.020});

					{@code comm// Automatické nastavenie veľkosti a polohy okna sveta (t. j.}
					{@code comm// aplikácie) a jeho zobrazenie:}
					{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();
					{@link Svet Svet}.{@link Svet#zobraz() zobraz}();
				}

				{@code comm// Súkromný konštruktor.}
				{@code kwdprivate} AnimujGIF()
				{
					{@code comm// Spustenie tej verzie nadradeného konštruktora, ktorá umožňuje}
					{@code comm// zmeniť rozmery plátien sveta:}
					{@code valsuper}(animovanýObrázok.{@link Obrázok#šírka šírka}, animovanýObrázok.{@link Obrázok#výška výška});

					{@code comm// Skrytie robota:}
					{@link GRobot#skry() skry}();
				}

				{@code comm// Reakcia na časovač – pravidelne prekresľuje animovaný obrázok, ktorého}
				{@code comm// animácia je vykonávaná automaticky (vnútornými mechanizmami sveta).}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
					{
						{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
						{@link GRobot#obrázok(Image) obrázok}(animovanýObrázok);
					}
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>srdce.gif<alt/>Animované srdce.</image>Táto animácia
		 * bude zobrazená v okne aplikácie.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad ukazuje ako prečítať PNG sekvenciu s použitím
		 * rovnakej šablóny názvu súborov, akú používa druhý príklad uvedený
		 * v opise metódy {@link #ulož(String, boolean) ulož(súbor,
		 * prepísať)}. Dotknutý príklad generuje súbory použiteľné s týmto
		 * príkladom, preto je vhodné sa zaoberať najskôr ním. (Generovanie
		 * môže trvať aj sedem minút, podľa výkonnosti hadvéru,
		 * a vygenerované súbory budú dohromady zaberať okolo 120 MB.)</p>
		 * 
		 * <p>V súlade s informáciami uvedenými v opise metódy
		 * {@link #ulož(String, boolean) ulož(súbor, prepísať)} platí, že ak
		 * táto metóda pri čítaní sekvencie nájde súbor s poradovým číslom
		 * nula, tak ho prečíta ako neanimovaný obrázok a už sa nepokúša
		 * hľadať ďalšie súbory vyhovujúce kritériám šablóny názvov súborov
		 * animovanej sekvencie. Inak postupuje pri čítaní systematicky
		 * a pridáva snímky do prečítanej animácie dovtedy, kým jestvujú
		 * súbory v neprerušenej sekvencii podľa určenej šablóny.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda {@link #ulož(String, boolean)
		 * ulož(súbor, prepísať)} dokáže vygenerovať len PNG sekvenciu, ale
		 * táto metóda ({@code currčítaj(súbor)}) je schopná prečítať aj
		 * sekvenciu vo formáte JPEG. JPEG je stratový formát a neumožňuje
		 * ukladanie (polo)priehľadných bodov. Je však vhodný na ukladanie
		 * fotografií, kde sa jeho kompresné artefakty vizuálne strácajú
		 * (opticky zanikajú v grafickej komplikovanosti obrazu), čím sa lepšie
		 * uplatňuje jeho kompresný benefit. JPEG sekvencie môžete použiť
		 * na simulovanie prehrávania videa, ak takú sekvenciu vyrobíte
		 * s použitím externého softvéru a zachováte pravidlá pomenovania
		 * šablón s rozdielom prípony – namiesto {@code .png} musí byť uvedená
		 * {@code .jpg} alebo {@code .jpeg}</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} AnimujSekvenciuPNG {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Inštancia obrázka, ktorý bude obsahovať a animovať prečítanú sekvenciu.}
				{@code kwdprivate} {@link Obrázok Obrázok} animovanýObrázok;

				{@code comm// Aktuálne „percento“ prečítaných snímok vypočítané v reakcii „sekvencia“}
				{@code comm// (v skutočnosti hodnota v rozsahu od 0.0 do 1.0, ktorá je použitá na}
				{@code comm// zobrazenie percent):}
				{@code kwdprivate} {@code typedouble} percento = {@code num0.0};

				{@code comm// Nastavenie začiatočného stavu aplikácie (po prečítaní sekvencie sa}
				{@code comm// zmení na false):}
				{@code kwdprivate} {@code typeboolean} čítam = {@code valtrue};

				{@code comm// Názov naposledy prečítaného súboru sekvencie:}
				{@code kwdprivate} {@link String String} naposledyPrečítaný = {@code srg""};

				{@code comm// Súkromný konštruktor.}
				{@code kwdprivate} AnimujSekvenciuPNG()
				{
					{@code comm// Spustenie tej verzie nadradeného konštruktora, ktorá umožňuje}
					{@code comm// zmeniť rozmery plátien sveta:}
					{@code valsuper}({@link Svet Svet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}({@code srg"sekvencia/nahodne-ciary-001.png"}),
						{@link Svet Svet}.{@link Svet#výškaObrázka(String) výškaObrázka}({@code srg"sekvencia/nahodne-ciary-001.png"}));

					{@code comm// Počas čítania pozastavíme automatické prekresľovanie (aby}
					{@code comm// kreslenie v reakcii sekvencia „neblikalo“):}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

					{@code comm// Automatické nastavenie veľkosti a polohy okna sveta}
					{@code comm// (t. j. aplikácie):}
					{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();

					{@code comm// Skrytie robota:}
					{@link GRobot#skry() skry}();

					{@code comm// Spustíme časomieru, pretože chceme vedieť ako dlho potrvá čítanie}
					{@code comm// PNG sekvencie a časovač, aby mohlo byť aktualizované zobrazovanie}
					{@code comm// priebehu čítania:}
					{@link Svet Svet}.{@link Svet#spustiČasomieru() spustiČasomieru}();
					{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.250});

					{@code comm// Prečítanie PNG sekvencie:}
					animovanýObrázok = {@link Obrázok Obrázok}.{@link Obrázok#čítaj(String) čítaj}({@code srg"sekvencia/nahodne-ciary-***.png"});

					{@code comm// Zastavíme časomieru aj časovač a zobrazíme výsledok (na konzole):}
					{@code typedouble} čas = {@link Svet Svet}.{@link Svet#zastavČasomieru() zastavČasomieru}();
					{@link Svet Svet}.{@link Svet#zastavČasovač() zastavČasovač}();
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Čítanie animácie trvalo: "} +
						{@link Math Math}.{@link Math#round(double) round}(čas / {@code num60}) + {@code srg" min "} + {@link GRobot#F(double, int) F}(čas % {@code num60}, {@code num2}) + {@code srg" s"});

					{@code comm// Prečítané – odteraz treba zobrazovať animáciu:}
					čítam = {@code valfalse};

					{@code comm// Nastavíme čiernu farbu pozadia:}
					{@link Svet Svet}.{@link Svet#farbaPozadia(Color) farbaPozadia}(čierna);

					{@code comm// A spustíme animáciu a časovač:}
					animovanýObrázok.{@link Obrázok#spusti() spusti}();
					{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.040});

					{@code comm// Obnovíme automatické prekresľovanie, aby sme videli priebeh}
					{@code comm// animácie:}
					{@link Svet Svet}.{@link Svet#kresli() kresli}();
				}

				{@code comm// Táto reakcia je automaticky spúšťaná počas čítania (alebo zápisu)}
				{@code comm// animovanej PNG sekvencie. Používame ju na aktualizovanie informácie}
				{@code comm// o stave čítania zobrazenej na obrazovke pre používateľa cez reakciu tik.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#sekvencia(int, Object, Object, long, long) sekvencia}({@code typeint} kódSpracovania, {@link String String} zdroj,
					{@link String String} cieľ, {@code typelong} stav, {@code typelong} celkovo)
				{
					{@code comm// Vypočítame percento prečítanej animácie a uložíme názov súboru:}
					percento = ({@code typedouble})stav / ({@code typedouble})celkovo;
					naposledyPrečítaný = zdroj;

					{@code comm// Žiadame prekreslenie, čo bude spracované v reakcii tik:}
					{@link Svet Svet}.{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
				}

				{@code comm// Reakcia na časovač – pravidelne prekresľuje animovaný obrázok, ktorého}
				{@code comm// animácia je vykonávaná automaticky (vnútornými mechanizmami sveta).}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
					{
						{@code comm// Reakcia slúži na zobrazovanie priebehu čítania aj na}
						{@code comm// zobrazovanie prečítanej animácie. V obidvoch prípadoch najprv}
						{@code comm// vymažeme grafiku podlahy:}
						{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();

						{@code comm// Potom podľa stavu aplikácie zobrazíme priebeh alebo animovaný}
						{@code comm// obrázok:}
						{@code kwdif} (čítam)
						{
							{@code comm// Nasledujúce príkazy kreslia jednoduchý ukazovateľ priebehu}
							{@code comm// obsahujúci informačný text o aktuálnom percente a naposledy}
							{@code comm// prečítanom súbore:}

							{@link GRobot#skoč(double, double) skoč}(percento * {@code num200} &#45; {@code num200}, {@code num0});
							{@link GRobot#farba(Color) farba}({@link Farebnosť#svetlošedá svetlošedá});
							{@link GRobot#vyplňObdĺžnik(double, double) vyplňObdĺžnik}(percento * {@code num200}, {@code num10});

							{@link GRobot#farba(Color) farba}(čierna);
							{@link GRobot#skočNa(double, double) skočNa}({@code num0}, {@code num0});
							{@link GRobot#kresliObdĺžnik(double, double) kresliObdĺžnik}({@code num200}, {@code num10});
							{@link GRobot#text(String) text}({@link GRobot#F(double, int) F}(percento * {@code num100.0}, {@code num0}) + {@code srg" % ("} +
								naposledyPrečítaný + {@code srg")"});

							{@code comm// Prekreslíme obrazovku, aby bolo vidno aktuálny stav}
							{@code comm// priebehu:}
							{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
						}
						{@code kwdelse}
							{@link GRobot#obrázok(Image) obrázok}(animovanýObrázok);
					}
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@code comm// Vytvorenie anonymnej inštancie tejto triedy (konštruktor triedy}
					{@code comm// odvodenej od robota vždy zabezpečí vytvorenie aplikácie):}
					{@code kwdnew} AnimujSekvenciuPNG();
				}
			}
			</pre>
		 * 
		 * <p><b>Opis priebehu a výsledku:</b></p>
		 * 
		 * <p>Počas čítania bude na obrazovke premietaný stav priebehu čítania
		 * s názvom naposledy prečítaného súboru. Kód spracovania reakcie
		 * {@link GRobot#sekvencia(int, Object, Object, long, long) sekvencia}
		 * sme v príklade nepoužili. V tomto prípade mal hodnotu konštanty
		 * {@link GRobot#ČÍTANIE_PNG_SEKVENCIE ČÍTANIE_PNG_SEKVENCIE}.
		 * (V prípade čítania animáce z formátu GIF je hodnota rovná
		 * hodnote konštanty {@link GRobot#ČÍTANIE_GIF_ANIMÁCIE
		 * ČÍTANIE_GIF_ANIMÁCIE}.) Podľa nami nameraných hodnôt trvalo čítanie
		 * sekvencie od osem do dvanásť sekúnd.</p>
		 * 
		 * <p>Výsledkom vykonania tohto príkladu bude spustenie animácie
		 * prečítanej zo sekvencie súborov vo formáte PNG (uloženej na
		 * disku). Grafické súbory potrebné na fungovanie tohto príkladu je
		 * schopný vytvoriť v poradí druhý príklad uvedený v opise metódy
		 * {@link #ulož(String, boolean) ulož(súbor, prepísať)} (preto je
		 * vhodné sa zaoberať najskôr ním).</p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>sekvencia-png-1.gif<alt/>Sekvencia 1.</image></td>
		 * <td><image>sekvencia-png-2.gif<alt/>Sekvencia 2.</image></td>
		 * </tr><tr><td colspan="2"><p class="image">Ukážky možného vzhľadu
		 * prečítanej animáce, ak bola sekvencia vygenerovaná príkladom
		 * z opisu metódy {@link #ulož(String, boolean) ulož(súbor, prepísať)}
		 * <small>(plátno ukážok je úmyselne
		 * zmenšené)</small>.</p></td></tr></table>
		 * 
		 * <p>Na 32-bitových operačných systémoch sa môže stať, že aplikácia
		 * nebude mať dostatok pamäte na vykonanie. V takom prípade skúste
		 * cez príkazový riadok operačného systému spustiť aplikáciu
		 * nasledujúcim príkazom potvrdeným v priečinku s preloženou
		 * aplikáciou:</p>
		 * 
		 * <pre CLASS="example">
			java -Xmx1g AnimujSekvenciuPNG
			</pre>
		 * 
		 * <p>V prípade, že táto metóda nenájde dostatok miesta v pamäti na
		 * prečítanie celej sekvencie, tak uvoľní obrázok z pamäti a vráti
		 * hodnotu {@code valnull}. Tiež upozorní všetky inštancie sledujúce
		 * čítanie sekvencie reakciou {@link GRobot#sekvencia(int, Object,
		 * Object, long, long) sekvencia} a/alebo aktívnu obsluhu udalostí
		 * s definovanou reakciou {@link ObsluhaUdalostí#sekvencia(int,
		 * Object, Object, long, long) sekvencia} poslaním informácií
		 * platných v čase zlyhania s kódom spracovania {@link 
		 * GRobot#CHYBA_ČÍTANIA_PNG_SEKVENCIE CHYBA_ČÍTANIA_PNG_SEKVENCIE}.</p>
		 * 
		 * <p> </p>
		 * 
		 * @param súbor názov súboru s obrázkom
		 * @return obrázok v novom objekte typu {@link Obrázok Obrázok}
		 *     (prípadne {@code valnull} ak pokus o prečítanie sekvencie
		 *     zlyhal z pamäťových dôvodov)
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 *     (identifikátor {@code imageNotFound})
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static Obrázok čítaj(String súbor)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			Obrázok novýObrázok = null;

			int prvýIndex = súbor.indexOf('*');

			if (-1 != prvýIndex)
			{
				String prípona = súbor.substring(súbor.lastIndexOf('.') + 1);

				if (!prípona.toLowerCase().equals("png") &&
					!prípona.toLowerCase().equals("jpg") &&
					!prípona.toLowerCase().equals("jpeg"))
					throw new GRobotException("Šablóna „" + súbor +
						"“ nie je použiteľná.", "invalidImageTemplate", súbor);

				int poslednýIndex = 1 + súbor.lastIndexOf('*');
				String prefix = súbor.substring(0, prvýIndex);
				String postfix = súbor.substring(poslednýIndex);
				String formát = "%0" + (poslednýIndex - prvýIndex) + "d";

				String menoSúboru = null;
				Image obrázokZoSúboru = null;
				int i = 0, n = 0;

				for (; i <= 1; ++i)
				{
					if (1 == i)
					{
						boolean našiel;

						do
						{
							našiel = false;
							menoSúboru = prefix + String.format(
								Locale.ENGLISH, formát, ++n) + postfix;

							try
							{
								if (null != Súbor.nájdiSúbor(
									priečinokObrázkov + menoSúboru))
								{
									našiel = true;
									continue;
								}
							}
							catch (Exception e)
							{
								// Ignorované…
							}

							try
							{
								if (null != new URL(menoSúboru))
								{
									našiel = true;
									continue;
								}
							}
							catch (Exception e)
							{
								// Ignorované…
							}

							if (null != Súbor.nájdiZdroj(priečinokObrázkov +
								menoSúboru)) našiel = true;
						}
						while (našiel);
						--n;
					}

					menoSúboru = prefix + String.format(
						Locale.ENGLISH, formát, i) + postfix;

					try
					{
						obrázokZoSúboru = súborNaObrázok(menoSúboru);

						if (null != ObsluhaUdalostí.počúvadlo)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.sekvencia(
									ČÍTANIE_PNG_SEKVENCIE,
									menoSúboru, obrázokZoSúboru, i, n);
							}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci :
								GRobot.počúvajúciSúbory)
							{
								počúvajúci.sekvencia(
									ČÍTANIE_PNG_SEKVENCIE,
									menoSúboru, obrázokZoSúboru, i, n);
							}
						}

						break;
					}
					catch (Exception e)
					{
						// Ignorované…
					}
				}

				if (null == obrázokZoSúboru)
					throw new GRobotException("Obrázok „" + menoSúboru +
						"“ nebol nájdený.", "imageNotFound", menoSúboru);

				// Obrázok je vytvorený podľa prvého nájdeného súboru:
				novýObrázok = new Obrázok(
					obrázokZoSúboru.getWidth(null),
					obrázokZoSúboru.getHeight(null));

				novýObrázok.grafika.translate(
					-novýObrázok.posunX, -novýObrázok.posunY);
				novýObrázok.grafika.drawImage(obrázokZoSúboru, 0, 0, null);
				novýObrázok.grafika.translate(
					novýObrázok.posunX, novýObrázok.posunY);

				if (i > 0)
				{
					// Ak mal prvý súbor poradové číslo 1, tak je to zároveň
					// prvá snímka animácie…
					novýObrázok.trvanie = 40;
					novýObrázok.pridajĎalšiu(obrázokZoSúboru,
						novýObrázok.trvanie);

					for (;;)
					{
						menoSúboru = prefix + String.format(
							Locale.ENGLISH, formát, ++i) + postfix;

						// Potom sa číta sekvencia súborov, dokedy sú
						// nachádzané ďalšie súbory:
						try
						{
							obrázokZoSúboru = súborNaObrázok(menoSúboru);
							novýObrázok.pridajĎalšiu(obrázokZoSúboru,
								novýObrázok.trvanie);

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.sekvencia(
										ČÍTANIE_PNG_SEKVENCIE,
										menoSúboru, obrázokZoSúboru, i, n);
								}

							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								for (GRobot počúvajúci :
									GRobot.počúvajúciSúbory)
								{
									počúvajúci.sekvencia(
										ČÍTANIE_PNG_SEKVENCIE,
										menoSúboru, obrázokZoSúboru, i, n);
								}
							}
						}
						catch (OutOfMemoryError e)
						{
							Svet.uvoľni(novýObrázok);
							novýObrázok = null;
							for (int j = 0; j <= i; ++j)
								Svet.uvoľni(prefix + String.format(
									Locale.ENGLISH, formát, j) + postfix);

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.sekvencia(
										CHYBA_ČÍTANIA_PNG_SEKVENCIE,
										menoSúboru, null, i, n);
								}

								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									for (GRobot počúvajúci :
										GRobot.počúvajúciSúbory)
									{
										počúvajúci.sekvencia(
											CHYBA_ČÍTANIA_PNG_SEKVENCIE,
											menoSúboru, null, i, n);
									}
								}

							return null;
						}
						catch (Exception e)
						{
							break;
						}
					}

					novýObrázok.snímka = 0;
					novýObrázok.dajGrafikuSnímky(0);
				}
			}
			else
			{
				Image obrázokZoSúboru = súborNaObrázok(súbor);

				novýObrázok = new Obrázok(
					obrázokZoSúboru.getWidth(null),
					obrázokZoSúboru.getHeight(null));

				novýObrázok.grafika.translate(
					-novýObrázok.posunX, -novýObrázok.posunY);
				novýObrázok.grafika.drawImage(obrázokZoSúboru, 0, 0, null);
				novýObrázok.grafika.translate(
					novýObrázok.posunX, novýObrázok.posunY);

				// Prečítaj prípadnú animáciu gifu
				if (súbor.toLowerCase().endsWith(".gif"))
				{
					FileNotFoundException notFound = null;
					InputStream čítanie = null;

					// Skúsi otvoriť prúd z aktuálnej lokality
					// alebo z aktuálneho zoznamu ciest classpath:
					try
					{
						čítanie = Súbor.dajVstupnýPrúdSúboru(súbor);
					}
					catch (FileNotFoundException e)
					{
						notFound = e;
					}

					// Posledný pokus – otvoriť súbor ako zdroj:
					if (null == čítanie)
					{
						try
						{
							čítanie = Súbor.dajVstupnýPrúdZdroja(súbor);
						}
						catch (NullPointerException isNull)
						{
							if (null != notFound) throw new GRobotException(
								"Obrázok „" + súbor + "“ nebol nájdený.",
									"imageNotFound", súbor);
							throw isNull;
						}
					}

					// Použije triedu GifDecoder na prečítanie všetkých snímok
					// (ak je ich viac než jedna)
					GifDecoder gifDecoder = new GifDecoder();
					gifDecoder.read(čítanie);

					int n = gifDecoder.getFrameCount();

					if (n > 1)
					{
						for (int i = 0; i < n; ++i)
						{
							BufferedImage snímka = gifDecoder.getFrame(i);
							int trvanie = gifDecoder.getDelay(i);
							novýObrázok.pridajĎalšiu(snímka, trvanie);

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.sekvencia(
										ČÍTANIE_GIF_ANIMÁCIE,
										súbor, snímka, i - 1, n);
								}

								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									for (GRobot počúvajúci :
										GRobot.počúvajúciSúbory)
									{
										počúvajúci.sekvencia(
											ČÍTANIE_GIF_ANIMÁCIE,
											súbor, snímka, i - 1, n);
									}
								}
						}

						novýObrázok.snímka = 0;
						novýObrázok.dajGrafikuSnímky(0);
						// ‼ novýObrázok.spusti(); — Nie! Zamietnuté‼
						// Nič by nemalo byť vykonávané bez vedomia
						// programátora resp. používateľa frameworku‼
						// (Svojvoľné spustenie animácie určite nie.)
					}
				}
			}

			return novýObrázok;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
		public static Obrazok citaj(String súbor)
		{ return new Obrazok(čítaj(súbor)); }

		/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
		public static Obrázok prečítaj(String súbor)
		{ return čítaj(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
		public static Obrazok precitaj(String súbor)
		{ return new Obrazok(čítaj(súbor)); }


		// Jedna nestatická metóda:

		// Pridá ďalšiu snímku animácie počas čítania animovanej sekvencie
		// PNG obrázkov alebo počas čítania obrázka vo formáte GIF:
		private void pridajĎalšiu(Image obrázok, int trvanie)
		{
			snímky.add(new Snímka(obrázok, trvanie));
		}


	// Rozmery a grafika

		/**
		 * <p>Vráti šírku obrázka. Ide o rovnaký údaj, aký je uložený v konštante
		 * {@link #šírka šírka}.</p>
		 * 
		 * @return šírka obrázka
		 */
		public int šírka() { return getWidth(); }

		/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
		public int sirka() { return getWidth(); }

		/**
		 * <p>Vráti šírku obrázka. Ide o rovnaký údaj, aký je uložený v konštante
		 * {@link #výška výška}.</p>
		 * 
		 * @return výška obrázka
		 */
		public int výška() { return getHeight(); }

		/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
		public int vyska() { return getHeight(); }

		/**
		 * <p>Vráti objekt grafiky obrázka (pre potreby kreslenia do obrázka).
		 * Robot má dostatok nástrojov (metód) na kreslenie. Ak potrebujete
		 * priamy prístup ku {@linkplain Graphics2D grafickému objektu
		 * obrázka} (a využívať jeho metódy – ide o triedu {@link Graphics2D
		 * Graphics2D}), použite na prístup k nemu túto metódu alebo
		 * konštantu {@link #grafika grafika}, ktorá obsahuje rovnaký
		 * objekt, aký vracia táto metóda. Používajte tento objekt
		 * ojedinele, pretože inštancie triedy obrázok mierne prispôsobujú
		 * svoj súradnicový priestor, aby boli lepšie použiteľné v prostredí
		 * programovacieho rámca grafického robota.</p>
		 * 
		 * <p>(Na spresnenie: Súradnicový priestor obrázka je posunutý tak,
		 * aby počiatok súradnicovej sústavy programovacieho rámca GRobot
		 * ležal v strede obrázka. Predpokladá sa totiž, že všetky objekty
		 * tvarov určené na nakreslenie do obrázka budú generované robotom
		 * a ten generuje tvary tak, aby boli priamo použiteľné
		 * v súradnicovom priestore Javy, ibaže v prepočte vzhľadom
		 * k rozmerom plátien, preto má obrázok svoj priestor posunutý.
		 * Viac sa o súradnicových priestoroch píše napríklad v opisoch
		 * metód {@link GRobot#cesta() GRobot.cesta()},
		 * {@link SVGPodpora#zapíš(String, String, boolean)
		 * SVGpodpora.zapíš(…)}, {@link SVGPodpora#čítaj(String)
		 * SVGpodpora.čítaj(meno)} a tiež priebežne v celej dokumentácii.)</p>
		 * 
		 * @return objekt typu {@link Graphics2D Graphics2D} – grafika
		 *     obrázka
		 */
		public Graphics2D grafika() { return grafika; }

		/**
		 * <p>Vyrobí nový novú verziu tohto obrázka, ktorej zmení veľkosť podľa
		 * zadanej mierky.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestZmenyVeľkosti {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} {@code kwdfinal} {@link Zoznam Zoznam}&lt;{@link Obrázok Obrázok}&gt; obrázky = {@code kwdnew} {@link Zoznam#Zoznam() Zoznam}&lt;{@link Obrázok Obrázok}&gt;();

				{@code kwdprivate} TestZmenyVeľkosti()
				{
					{@code comm// Najprv vyrobíme a nakreslíme obrázok:}
					{@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num100}, {@code num100});

					{@link GRobot#kresliNaObrázok(Obrázok) kresliNaObrázok}(obrázok);
					{@link GRobot#farba(Farebnosť) farba}({@link Farebnosť#žltá žltá});
					{@link GRobot#vyplň() vyplň}();
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num1});
					{@link GRobot#farba(Farebnosť) farba}({@link Farebnosť#červená červená});
					{@link GRobot#kružnica(double) kružnica}({@code num45});
					{@link GRobot#farba(Farebnosť) farba}({@link Farebnosť#zelená zelená});
					{@link GRobot#štvorec(double) štvorec}({@code num25});

					{@link GRobot#farba(Farebnosť) farba}({@link Farebnosť#tyrkysová tyrkysová});
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num2});
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num18}; ++i)
					{
						{@link GRobot#skočNa(double, double) skočNa}({@code num0}, {@code num0});
						{@link GRobot#vpravo(double) vpravo}({@code num20});
						{@link GRobot#dopredu(double) dopredu}({@code num47});
					}

					{@link GRobot#domov() domov}();
					{@link GRobot#farba(Farebnosť) farba}({@link Farebnosť#modrá modrá});
					{@link GRobot#kruh(double) kruh}({@code num5});

					{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();

					{@code comm// Potom postupne zmeníme jeho rozmer v rozmedzí mierok 0.33 – 3.0…}
					{@code kwdfor} ({@code typedouble} mierka = {@code num0.33}; mierka &lt;= {@code num3.0}; mierka += {@code num0.025})
					{
						{@link GRobot#veľkosť(double) veľkosť}({@code num10} * mierka);   {@code comm// nepovinné – veľkosť robota bude}
												{@code comm// indikovať fázu procesu}
						obrázky.{@link Zoznam#pridaj(Object) pridaj}(obrázok.{@code currzmeňVeľkosť}(mierka));
					}

					{@code comm// …a späť. (Obrázky so zmenenou veľkosťou ukladáme do zoznamu.)}
					{@code kwdfor} ({@code typedouble} mierka = {@code num3.0}; mierka &gt;= {@code num0.33}; mierka -= {@code num0.025})
					{
						{@link GRobot#veľkosť(double) veľkosť}({@code num10} * mierka);   {@code comm// nepovinné – veľkosť robota bude}
												{@code comm// indikovať fázu procesu}
						obrázky.{@link Zoznam#pridaj(Object) pridaj}(obrázok.{@code currzmeňVeľkosť}(mierka));
					}

					{@code comm// Konštantná uhlová rýchlosť zabezpečí rotáciu obrázka}
					{@link GRobot#uhlováRýchlosť(double) uhlováRýchlosť}({@code num0.5});
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktivita() aktivita}()
				{
					{@code comm// Zmena vlastného tvaru robota postupným výberom prvkov zoznamu}
					{@code comm// zabezpečí ukážku kontinuálnej zmeny obrázka v rozmedzí mierok,}
					{@code comm// ktoré sme použili v konštruktore.}
					{@link GRobot#vlastnýTvar(java.awt.Image) vlastnýTvar}(obrázky.{@link Zoznam#ďalší() ďalší}());
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code kwdnew} TestZmenyVeľkosti();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>zmenaVelkostiObrazka.png<alt/>Ukážka činnosti príkladu
		 * transformujúceho obrázok.</image>Ukážka výsledku – jedna z verzií
		 * zväčšeného obrázka vo fáze otáčania sa okolo svojej osi.</p>
		 * 
		 * @param mierka mierka {@code num0.5} znamená, že výsledný obrázok
		 *     bude polovičný; mierka {@code num2.0} znamená, že výsledný
		 *     obrázok bude dvojnásobný
		 * @return nová verzia obrázka (v novom objekte typu
		 *     {@link Obrázok Obrázok})
		 */
		public Obrázok zmeňVeľkosť(double mierka)
		{
			return new Obrázok(getScaledInstance((int)(šírka * mierka),
				(int)(výška * mierka), Image.SCALE_SMOOTH));
		}

		/**
		 * <p>Zruší oblasť na obmedzenie kreslenia na tento obrázok.</p>
		 * 
		 * @see GRobot#kresliVšade()
		 * @see #kresliDo(Shape)
		 * @see #nekresliDo(Shape)
		 */
		public void kresliVšade()
		{
			grafika.setClip(null);
		}

		/** <p><a class="alias"></a> Alias pre {@link #kresliVšade() kresliVšade}.</p> */
		public void kresliVsade() { kresliVšade(); }

		/**
		 * <p>Obmedzí kreslenie na tento obrázok na plochu zadaného útvaru
		 * ({@link Shape Shape}).</p>
		 * 
		 * <p>Robot disponuje množinou metód na kreslenie tvarov ({@link 
		 * GRobot#kružnica(double) kružnica}, {@link GRobot#elipsa(double,
		 * double) elipsa}, {@link GRobot#štvorec(double) štvorec}…), ktoré
		 * zároveň generujú tvary. Na ich použitie s touto metódou je dobré
		 * predtým kreslenie tvarov {@linkplain GRobot#nekresliTvary()
		 * zakázať} a neskôr opäť {@linkplain GRobot#kresliTvary() povoliť}.
		 * Metóda {@link GRobot#text(String) text} dokonca zákaz kreslenia
		 * tvarov požaduje, aby mohla vygenerovať tvar (bez zákazu má
		 * návratovú hodnotu {@code valnull}). Tvar je možné vytvoriť aj
		 * z {@linkplain GRobot#cesta() cesty}…</p>
		 * 
		 * <p>Obmedzenie zužuje aktuálny priestor kreslenia, to znamená, že
		 * sa priebežne vytvára oblasť, ktorá je prienikom všetkých
		 * obmedzení. Ak chceme vytvoriť obmedzenie tvaru, ktorý je
		 * možné vytvoriť inou množinovou operáciou, môžeme na obmedzenie
		 * kreslenia použiť {@link Oblasť Oblasť} (zadanú namiesto
		 * parametra {@code tvar}). Obmedzenia sú platné pre všetky
		 * roboty a zrušíme ich volaním metódy {@link #kresliVšade()
		 * kresliVšade}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri takomto orezávaní nie
		 * je na všetkých platformách a/alebo implementáciách virtuálneho
		 * stroja Javy dostupná funkcia anti-aliasingu, čo zjednodušene
		 * povedané znamená, že okraje orezanej kresby budú „zúbkaté.“
		 * Ak sa chcete tejto nedokonalosti vyhnúť, použite radšej funkciu
		 * {@linkplain Plátno#použiMasku masky}. Tá dovoľuje ovplyvňovať
		 * úroveň priehľadnosti s jemnosťou na jednotlivé body rastra.</p>
		 * 
		 * @param tvar tvar ({@link Shape Shape}) alebo {@link Oblasť
		 *     Oblasť}
		 * 
		 * @see GRobot#kresliDo(Shape)
		 * @see #kresliVšade()
		 * @see #nekresliDo(Shape)
		 */
		public void kresliDo(Shape tvar)
		{
			if (null != tvar) grafika.clip(tvar);
		}

		/**
		 * <p>Vytvára obmedzenie kreslenia na tento obrázok.
		 * Funguje rovnako ako metóda {@link #kresliDo(Shape)
		 * kresliDo}, ibaže obrátene – kreslenie je možné všade, okrem
		 * zadaného tvaru alebo {@linkplain Oblasť oblasti}.</p>
		 * 
		 * <p>Rovnako ako pri metóde {@link #kresliDo(Shape) kresliDo}, sa
		 * aj toto obmedzenie kombinuje s aktuálnymi obmedzeniami kreslenia
		 * a je platné pre všetky roboty. Všetky ombedzenia zrušíme
		 * volaním metódy {@link #kresliVšade() kresliVšade}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri takomto orezávaní nie
		 * je na všetkých platformách a/alebo implementáciách virtuálneho
		 * stroja Javy dostupná funkcia anti-aliasingu, čo zjednodušene
		 * povedané znamená, že okraje orezanej kresby budú „zúbkaté.“
		 * Ak sa chcete tejto nedokonalosti vyhnúť, použite radšej funkciu
		 * {@linkplain Plátno#použiMasku masky}. Tá dovoľuje ovplyvňovať
		 * úroveň priehľadnosti s jemnosťou na jednotlivé body rastra.</p>
		 * 
		 * @param tvar tvar ({@link Shape Shape}) alebo {@link Oblasť
		 *     Oblasť}
		 * 
		 * @see GRobot#nekresliDo(Shape)
		 * @see #kresliDo(Shape)
		 * @see #kresliVšade()
		 */
		public void nekresliDo(Shape tvar)
		{
			if (null != tvar)
			{
				Shape clip = grafika.getClip();
				if (null == clip) clip = new Rectangle.
					Double(-posunX, -posunY, šírka, výška);
				Area oblasťA = new Area(clip);
				Area oblasťB = new Area(tvar);
				oblasťA.subtract(oblasťB);
				grafika.setClip(oblasťA);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zmeňVeľkosť(double) zmeňVeľkosť}.</p> */
		public Obrazok zmenVelkost(double mierka)
		{
			return new Obrazok(getScaledInstance((int)(šírka * mierka),
				(int)(výška * mierka), Image.SCALE_SMOOTH));
		}

		/**
		 * <p>Vyrobí nový novú verziu tohto obrázka, ktorej zmení veľkosť podľa
		 * zadaných rozmerov.</p>
		 * 
		 * @param nováŠírka šírka novej verzie obrázka
		 * @param nováVýška šírka novej verzie obrázka
		 * @return nová verzia obrázka (v novom objekte typu
		 *     {@link Obrázok Obrázok})
		 */
		public Obrázok zmeňVeľkosť(int nováŠírka, int nováVýška)
		{
			return new Obrázok(getScaledInstance(nováŠírka, nováVýška,
				Image.SCALE_SMOOTH));
		}

		/** <p><a class="alias"></a> Alias pre {@link #zmeňVeľkosť(int, int) zmeňVeľkosť}.</p> */
		public Obrazok zmenVelkost(int nováŠírka, int nováVýška)
		{
			return new Obrazok(getScaledInstance(nováŠírka, nováVýška,
				Image.SCALE_SMOOTH));
		}


	// Okraje

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu x-ovú súradnicu obrázka.</p>
		 * 
		 * @return najmenšia x-ová súradnica obrázka
		 * 
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public double najmenšieX() { return -šírka / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public double najmensieX() { return -šírka / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public double minimálneX() { return -šírka / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public double minimalneX() { return -šírka / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu y-ovú súradnicu obrázka.</p>
		 * 
		 * @return najmenšia y-ová súradnica obrázka
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najväčšieY()
		 */
		public double najmenšieY() { return -(výška - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public double najmensieY() { return -(výška - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public double minimálneY() { return -(výška - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public double minimalneY() { return -(výška - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu x-ovú súradnicu obrázka.</p>
		 * 
		 * @return najväčšia x-ová súradnica obrázka
		 * 
		 * @see #najmenšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public double najväčšieX() { return (šírka - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public double najvacsieX() { return (šírka - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public double maximálneX() { return (šírka - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public double maximalneX() { return (šírka - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu y-ovú súradnicu obrázka.</p>
		 * 
		 * @return najväčšia y-ová súradnica obrázka
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 */
		public double najväčšieY() { return výška / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public double najvacsieY() { return výška / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public double maximálneY() { return výška / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public double maximalneY() { return výška / 2; }


	// Vymazanie

		/**
		 * <p>Vymaže obsah obrázka. V podstate vyplní obrázok priehľadnou
		 * farbou.</p>
		 */
		public void vymaž()
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();
			Arrays.fill(údajeObrázka, 0);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public void vymaz() { vymaž(); }


	// Kreslenie a vypĺňanie

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu na zadanej
		 * pozícii zadanou farbou.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param farba objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(double x, double y, Color farba)
		{
			// int xx = (int)((šírka / 2.0) + x);
			// int yy = (int)((výška / 2.0) - y);
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, farba.getRGB());
			try
			{
				setRGB((int)((šírka / 2.0) + x),
					(int)((výška / 2.0) - y), farba.getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu na zadanej
		 * pozícii farbou zadaného objektu.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param objekt objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(double x, double y, Farebnosť objekt)
		{
			// int xx = (int)((šírka / 2.0) + x);
			// int yy = (int)((výška / 2.0) - y);
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, objekt.farba().getRGB());
			try
			{
				setRGB((int)((šírka / 2.0) + x),
					(int)((výška / 2.0) - y), objekt.farba().getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu na zadanej
		 * pozícii zadanou kombináciou zložiek ARGB zakódovaných
		 * v celočíselnej hodnote.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param farba celé číslo obsahujúce kombináciu farebných zložiek
		 *     RGB a priehľadnosti
		 */
		public void prepíšBod(double x, double y, int farba)
		{
			// int xx = (int)((šírka / 2.0) + x);
			// int yy = (int)((výška / 2.0) - y);
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, farba);
			try
			{
				setRGB((int)((šírka / 2.0) + x),
					(int)((výška / 2.0) - y), farba);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, Color) prepíšBod}.</p> */
		public void prepisBod(double x, double y, Color farba)
		{ prepíšBod(x, y, farba); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, Farebnosť) prepíšBod}.</p> */
		public void prepisBod(double x, double y, Farebnosť objekt)
		{ prepíšBod(x, y, objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, int) prepíšBod}.</p> */
		public void prepisBod(double x, double y, int farba)
		{ prepíšBod(x, y, farba); }


		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii zadaného
		 * bodu zadanou farbou.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(Poloha bod, Color farba)
		{
			// int xx = (int)((šírka / 2.0) + bod.polohaX());
			// int yy = (int)((výška / 2.0) - bod.polohaY());
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, farba.getRGB());
			try
			{
				setRGB((int)((šírka / 2.0) + bod.polohaX()),
					(int)((výška / 2.0) - bod.polohaY()), farba.getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii zadaného
		 * bodu farbou zadaného objektu.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param objekt objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(Poloha bod, Farebnosť objekt)
		{
			// int xx = (int)((šírka / 2.0) + bod.polohaX());
			// int yy = (int)((výška / 2.0) - bod.polohaY());
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, objekt.farba().getRGB());
			try
			{
				setRGB((int)((šírka / 2.0) + bod.polohaX()),
					(int)((výška / 2.0) - bod.polohaY()),
					objekt.farba().getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii zadaného
		 * bodu zadanou kombináciou zložiek ARGB zakódovaných v celočíselnej
		 * hodnote.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba celé číslo obsahujúce kombináciu farebných zložiek
		 *     RGB a priehľadnosti
		 */
		public void prepíšBod(Poloha bod, int farba)
		{
			// int xx = (int)((šírka / 2.0) + bod.polohaX());
			// int yy = (int)((výška / 2.0) - bod.polohaY());
			// if (xx >= 0 && xx < šírka && yy >= 0 && yy < výška)
			// 	setRGB(xx, yy, farba);
			try
			{
				setRGB((int)((šírka / 2.0) + bod.polohaX()),
					(int)((výška / 2.0) - bod.polohaY()), farba);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, Color) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, Color farba)
		{ prepíšBod(bod, farba); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, Farebnosť) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, Farebnosť objekt)
		{ prepíšBod(bod, objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, int) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, int farba)
		{ prepíšBod(bod, farba); }

		/**
		 * <p><a class="getter"></a> Vráti aktuálneho kresliča obrázka alebo
		 * {@code valnull}, ak nie je kreslič nastavený. Pre podrobnosti
		 * pozri informácie pri setteri vlastnosti
		 * {@link #kreslič(GRobot) kreslič}.</p>
		 * 
		 * @return grafický robot zvolený za kresliča alebo {@code valnull},
		 *     ak obrázok nemá nastaveného žiadneho konkrétneho kresliča
		 */
		public GRobot kreslič() { return kreslič; }

		/** <p><a class="alias"></a> Alias pre {@link #kreslič() kreslič}.</p> */
		public GRobot kreslic() { return kreslič; }

		/**
		 * <p><a class="setter"></a> Nastaví „kresliča“ obrázka – grafického
		 * robota, ktorý bude slúžiť na alternatívne kreslenie a vypĺňanie
		 * tvarov v rámci obrázka. Vlastnosť je určená na kombinované
		 * fungovanie s metódami {@link #kresli(Shape) kresli}
		 * a {@link #vyplň(Shape) vyplň}. (Tie sú v súčasnosti považované
		 * za metódy, ktoré poskytujú alternatívny spôsob kreslenia
		 * a vypĺňania tvarov do obrázka. Podrobnosti nájdete v ich opisoch.)
		 * Novému zvolenému kresličovi bude automaticky {@linkplain 
		 * GRobot#nekresliTvary() vypnuté kreslenie tvarov}, pričom jeho
		 * štandardné kresliace schopnosti (kreslenie pri pohybe {@linkplain 
		 * GRobot#dopredu(double) dopredu}, {@linkplain GRobot#dozadu(double)
		 * dozadu} a podobne, pri {@linkplain GRobot#položPero() položenom
		 * pere}) zostávajú zachované. Prípadnému starému kresličovi bude
		 * {@linkplain GRobot#kresliTvary() kreslenie tvarov automaticky
		 * zapnuté}.</p>
		 * 
		 * <p>Táto vlastnosť je určená <b>výhradne</b> na použitie s metódami
		 * {@link #kresli(Shape) kresli} a {@link #vyplň(Shape) vyplň}, ktoré
		 * očakávajú výstupy z metód robota na kreslenie tvarov ({@link 
		 * GRobot#kruh(double) kruh}, {@link GRobot#elipsa(double, double)
		 * elipsa}, {@link GRobot#štvorec(double) štvorec} a podobne). Nie je
		 * dobré túto vlastnosť kombinovať s úplným {@linkplain 
		 * GRobot#kresliNaObrázok(Obrázok) presmerovaním kreslenia robota do
		 * obrázka}. Mohlo by dôjsť k neočakávaným výsledkom.</p>
		 * 
		 * <p>Keď chceme kresliča zrušiť, pošleme do argumentu tejto metódy
		 * hodnotu {@code valnull} alebo použijeme metódu {@link 
		 * #zrušKresliča() zrušKresliča}.</p>
		 * 
		 * @param kreslič nový kreslič (grafický robot) alebo {@code valnull}
		 */
		public void kreslič(GRobot kreslič)
		{
			if (this.kreslič == kreslič) return;
			if (null != this.kreslič) this.kreslič.kresliTvary();
			this.kreslič = kreslič;
			if (null != kreslič) kreslič.nekresliTvary();
			// Neopoužiť „kresliNaObrázok“
		}

		/** <p><a class="alias"></a> Alias pre {@link #kreslič(GRobot) kreslič}.</p> */
		public void kreslic(GRobot kreslič) { kreslič(kreslič); }

		/**
		 * <p>Zruší kresliča obrázka. Pre podrobnosti pozri informácie pri
		 * setteri vlastnosti {@link #kreslič(GRobot) kreslič} – volanie
		 * tejto metódy má rovnaký efekt ako spustenie metódy
		 * {@link #kreslič(GRobot) kreslič} s hodnotou argumentu
		 * {@code valnull}.</p>
		 */
		public void zrušKresliča() { kreslič(null); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušKresliča() zrušKresliča}.</p> */
		public void zrusKreslica() { kreslič(null); }

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru do obrázka.
		 * Ak má tento objekt obrázka nastaveného takzvaného
		 * {@linkplain #kreslič(GRobot) kresliča}, tak táto metóda použije na
		 * nakreslenie jeho farbu alebo náter a štýl čiary.
		 * V opačnom prípade použije {@linkplain Svet#hlavnýRobot() hlavný
		 * robot}.</p>
		 * 
		 * <p>Zadaný tvar by mal byť vygenerovaný niektorým robotom (metódami
		 * na kreslenie tvarov), pretože obrázky majú súradnicový priestor
		 * prispôsobený prostrediu rámca – osi sú posunuté tak, aby robot
		 * v pozícii [0, 0] kreslil do stredu obrázka (toto posunutie závisí
		 * nielen od rozmerov obrázka, ale aj od aktuálnych rozmerov plátien)
		 * a y-ová súradnica je orientovaná podľa súradnicového systému Javy,
		 * to znamená, že je voči svetu robota zrkadlovo prevrátená.
		 * (O súradnicových priestoroch sa podrobnejšie píše napríklad
		 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
		 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
		 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
		 * v celej dokumentácii.)</p>
		 * 
		 * <p class="tip"><b>Tip:</b> V súvislosti s generovaním tvarov a ich
		 * použitím by robot, ktorého chceme použiť na generovanie tvarov,
		 * mal mať {@linkplain GRobot#nekresliTvary() vypnuté kreslenie
		 * tvarov}, aby vygenerovaný tvar nenakreslil do aktívneho plátna,
		 * prípadne do iného obrázka, do ktorého má {@linkplain 
		 * GRobot#kresliNaObrázok(Obrázok) presmerované kreslenie}.
		 * Automatické vypnutie alebo zapnutie kreslenia tvarov zvoleného
		 * grafického robota vykoná metóda {@link #kreslič(GRobot) kreslič}
		 * a to podľa toho, či je jej argumentom konkrétny robot – vtedy
		 * mu kreslenie tvarov vypne, alebo hodnota {@code valnull} – vtedy
		 * zapne kreslenie tvarov tomu robotu, ktorý bol aktuálnym
		 * kresličom konkrétneho obrázka. Metóda však nezaznamenáva počet
		 * spustení pre roboty, takže v prípade jej náhodného spúšťania
		 * s rôznymi hodnotami argumentov – robotov alebo hodnôt
		 * {@code valnull}, je výsledný stav kreslenia alebo nekreslenia
		 * tvarov pre konkrétny robot rovný tomu stavu, do ktorého ho
		 * preplo posledné spustenie dotknutej metódy. Treba si na to dať
		 * pozor v prípade, že chceme ten istý robot použiť ako kresliča
		 * viacerých obrázkov.</p>
		 * 
		 * <p>Na kreslenie robotom do obrázka je jednoduchšie je použiť
		 * metódu {@link GRobot#kresliNaObrázok(Obrázok)} na presmerovanie
		 * kreslenia robota do obrázka a kresliť robotom rovnakým spôsobom
		 * ako pri klasickom kreslení na {@linkplain Plátno plátno}
		 * (podlahu alebo strop).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude
		 *     nakreslený aktuálnym štýlom čiary a farbou/náterom {@linkplain 
		 *     #kreslič(GRobot) kresliča} alebo {@linkplain 
		 *     Svet#hlavnýRobot() hlavného robota}
		 * 
		 * @see #vyplň(Shape)
		 * @see #kresli(Shape, GRobot)
		 * @see #vyplň(Shape, GRobot)
		 */
		public void kresli(Shape tvar)
		{
			if (null != kreslič)
			{
				// grafika.setColor(kreslič.farbaRobota);
				kreslič.nastavVlastnostiGrafiky(grafika);
				kreslič.nastavFarbuAleboVýplňPodľaRobota(grafika);
				grafika.setStroke(kreslič.čiara);
				grafika.draw(tvar);
				kreslič.obnovVlastnostiGrafiky(grafika);
			}
			else if (null != Svet.hlavnýRobot)
			{
				// grafika.setColor(Svet.hlavnýRobot.farbaRobota);
				Svet.hlavnýRobot.nastavVlastnostiGrafiky(grafika);
				Svet.hlavnýRobot.nastavFarbuAleboVýplňPodľaRobota(grafika);
				grafika.setStroke(Svet.hlavnýRobot.čiara);
				grafika.draw(tvar);
				Svet.hlavnýRobot.obnovVlastnostiGrafiky(grafika);
			}
		}

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru do
		 * obrázka. Metóda potrebuje na svoje správne fungovanie zadanie
		 * robota „kresliča,“ ktorého farbu alebo náter a štýl čiary použije
		 * na kreslenie. Ak je do metódy namiesto konkrétneho kresliča zadaná
		 * hodnota {@code valnull}, tak je na získanie parametrov kreslenia
		 * použitý {@linkplain Svet#hlavnýRobot() hlavný robot} (ak ten
		 * nejestvuje, kreslenie nebude vykonané).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude
		 *     nakreslený aktuálnym štýlom čiary a farbou/náterom zadaného
		 *     robota („kresliča“)
		 * @param kreslič grafický robot, ktorého parametre budú použité
		 *     na kreslenie alebo {@code valnull}
		 * 
		 * @see #vyplň(Shape, GRobot)
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape)
		 */
		public void kresli(Shape tvar, GRobot kreslič)
		{
			if (null == kreslič) kreslič = Svet.hlavnýRobot;
			if (null == kreslič) return;

			// grafika.setColor(kreslič.farbaRobota);
			kreslič.nastavVlastnostiGrafiky(grafika);
			kreslič.nastavFarbuAleboVýplňPodľaRobota(grafika);
			grafika.setStroke(kreslič.čiara);
			grafika.draw(tvar);
			kreslič.obnovVlastnostiGrafiky(grafika);
		}

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov do obrázka.
		 * Ak má tento objekt obrázka nastaveného takzvaného
		 * {@linkplain #kreslič(GRobot) kresliča}, tak táto metóda použije na
		 * vyplnenie jeho farbu alebo náter. V opačnom
		 * prípade použije {@linkplain Svet#hlavnýRobot() hlavný robot}
		 * (ak ten nejestvuje, kreslenie nebude vykonané).</p>
		 * 
		 * <p>Zadaný tvar by mal byť vygenerovaný niektorým robotom (metódami
		 * na kreslenie tvarov), pretože obrázky majú súradnicový priestor
		 * prispôsobený prostrediu rámca – osi sú posunuté tak, aby robot
		 * v pozícii [0, 0] kreslil do stredu obrázka (toto posunutie závisí
		 * nielen od rozmerov obrázka, ale aj od aktuálnych rozmerov plátien)
		 * a y-ová súradnica je orientovaná podľa súradnicového systému Javy,
		 * to znamená, že je voči svetu robota zrkadlovo prevrátená.
		 * (O súradnicových priestoroch sa podrobnejšie píše napríklad
		 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
		 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
		 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
		 * v celej dokumentácii.)</p>
		 * 
		 * <p class="tip"><b>Tip:</b> V súvislosti s generovaním tvarov a ich
		 * použitím by robot, ktorého chceme použiť na generovanie tvarov,
		 * mal mať {@linkplain GRobot#nekresliTvary() vypnuté kreslenie
		 * tvarov}, aby vygenerovaný tvar nenakreslil do aktívneho plátna,
		 * prípadne do iného obrázka, do ktorého má {@linkplain 
		 * GRobot#kresliNaObrázok(Obrázok) presmerované kreslenie}.
		 * Automatické vypnutie alebo zapnutie kreslenia tvarov zvoleného
		 * grafického robota vykoná metóda {@link #kreslič(GRobot) kreslič}
		 * a to podľa toho, či je jej argumentom konkrétny robot – vtedy
		 * mu kreslenie tvarov vypne, alebo hodnota {@code valnull} – vtedy
		 * zapne kreslenie tvarov tomu robotu, ktorý bol aktuálnym
		 * kresličom konkrétneho obrázka. Metóda však nezaznamenáva počet
		 * spustení pre roboty, takže v prípade jej náhodného spúšťania
		 * s rôznymi hodnotami argumentov – robotov alebo hodnôt
		 * {@code valnull}, je výsledný stav kreslenia alebo nekreslenia
		 * tvarov pre konkrétny robot rovný tomu stavu, do ktorého ho
		 * preplo posledné spustenie dotknutej metódy. Treba si na to dať
		 * pozor v prípade, že chceme ten istý robot použiť ako kresliča
		 * viacerých obrázkov.</p>
		 * 
		 * <p>Na kreslenie robotom do obrázka je jednoduchšie je použiť
		 * metódu {@link GRobot#kresliNaObrázok(Obrázok)} na presmerovanie
		 * kreslenia robota do obrázka a kresliť robotom rovnakým spôsobom
		 * ako pri klasickom kreslení na {@linkplain Plátno plátno}
		 * (podlahu alebo strop).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     aktuálnou farbou/náterom {@linkplain #kreslič(GRobot) kresliča}
		 *     alebo {@linkplain Svet#hlavnýRobot() hlavného robota}
		 * 
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape, GRobot)
		 * @see #kresli(Shape, GRobot)
		 */
		public void vyplň(Shape tvar)
		{
			if (null != kreslič)
			{
				// grafika.setColor(kreslič.farbaRobota);
				kreslič.nastavVlastnostiGrafiky(grafika);
				kreslič.nastavFarbuAleboVýplňPodľaRobota(grafika);
				grafika.fill(tvar);
				kreslič.obnovVlastnostiGrafiky(grafika);
			}
			else if (null != Svet.hlavnýRobot)
			{
				// grafika.setColor(Svet.hlavnýRobot.farbaRobota);
				Svet.hlavnýRobot.nastavVlastnostiGrafiky(grafika);
				Svet.hlavnýRobot.nastavFarbuAleboVýplňPodľaRobota(grafika);
				grafika.fill(tvar);
				Svet.hlavnýRobot.obnovVlastnostiGrafiky(grafika);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape) vyplň}.</p> */
		public void vypln(Shape tvar) { vyplň(tvar); }

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov do obrázka.
		 * Metóda potrebuje na svoje správne fungovanie zadanie robota
		 * „kresliča,“ ktorého farbu alebo náter použije na vyplnenie
		 * zadaného tvaru. Ak je do metódy namiesto konkrétneho kresliča
		 * zadaná hodnota {@code valnull}, tak je na získanie parametrov
		 * kreslenia použitý {@linkplain Svet#hlavnýRobot() hlavný robot}
		 * (ak ten nejestvuje, kreslenie nebude vykonané).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     aktuálnou farbou/náterom zadaného robota („kresliča“)
		 * @param kreslič grafický robot, ktorého parametre budú použité
		 *     na kreslenie alebo {@code valnull}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #vyplň(Shape)
		 * @see #kresli(Shape)
		 */
		public void vyplň(Shape tvar, GRobot kreslič)
		{
			if (null == kreslič) kreslič = Svet.hlavnýRobot;
			if (null == kreslič) return;

			// grafika.setColor(kreslič.farbaRobota);
			kreslič.nastavVlastnostiGrafiky(grafika);
			kreslič.nastavFarbuAleboVýplňPodľaRobota(grafika);
			grafika.fill(tvar);
			kreslič.obnovVlastnostiGrafiky(grafika);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape, GRobot) vyplň}.</p> */
		public void vypln(Shape tvar, GRobot kreslič) { vyplň(tvar, kreslič); }


		// Vypĺňanie s použitím všetkých vlastností robota (náter,
		// cieľová farba a pod.).
		private void vyplň(GRobot robot)
		{
			robot.nastavVlastnostiGrafiky(grafika);
			robot.nastavFarbuAleboVýplňPodľaRobota(grafika);
			grafika.translate(-posunX, -posunY);
			grafika.fillRect(0, 0, šírka, výška);
			grafika.translate(posunX, posunY);
			robot.obnovVlastnostiGrafiky(grafika);
		}

		/**
		 * <p>Vyplní celú plochu obrázka zadanou farbou.</p>
		 * 
		 * @param farba objekt určujúci farbu na výplň plátna
		 * 
		 * @see #vymaž()
		 */
		public void vyplň(Color farba)
		{
			grafika.setColor(farba);
			grafika.translate(-posunX, -posunY);
			grafika.fillRect(0, 0, šírka, výška);
			grafika.translate(posunX, posunY);

			// Nemôžem takto, prišiel by som o transparenciu:
			// if (null == údajeObrázka) údajeObrázka =
			//	((DataBufferInt)getRaster().getDataBuffer()).getData();
			// Arrays.fill(údajeObrázka, farba.getRGB());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Color) vyplň}.</p> */
		public void vypln(Color farba) { vyplň(farba); }

		/**
		 * <p>Vyplní celú plochu obrázka farbou zadaného objektu.</p>
		 * 
		 * @param objekt objekt určujúci farbu na výplň plátna
		 * 
		 * @see #vymaž()
		 */
		public void vyplň(Farebnosť objekt)
		{
			grafika.setColor(objekt.farba());
			grafika.translate(-posunX, -posunY);
			grafika.fillRect(0, 0, šírka, výška);
			grafika.translate(posunX, posunY);

			// Nemôžem takto, prišiel by som o transparenciu:
			// if (null == údajeObrázka) údajeObrázka =
			//	((DataBufferInt)getRaster().getDataBuffer()).getData();
			// Arrays.fill(údajeObrázka, farba.getRGB());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Farebnosť) vyplň}.</p> */
		public void vypln(Farebnosť objekt) { vyplň(objekt); }

		/* *
		 * <p>Toto je „klon“ metódy {@link #vyplň(Color)}. Obrázok je
		 * vyplnený len v prípade, že je v premennej typu {@link Object}
		 * (zadanej ako parameter) uložená inštancia triedy {@link Farba
		 * Farba} alebo {@link Color Color}.</p>
		 * /
		public void vyplň(Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vyplň((Farba)farba);
		}

		/* * <p><a class="alias"></a> Alias pre {@link #vyplň(Object) vyplň}.</p> * /
		public void vypln(Object farba) { vyplň(farba); }
		*/

		/**
		 * <p>Vyplní obrázok farbou zadanou prostredníctvom farebných zložiek.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vyplň(Color)
		 */
		public Farba vyplň(int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vyplň(nováFarba);
			return nováFarba;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int) vyplň}.</p> */
		public Farba vypln(int r, int g, int b) { return vyplň(r, g, b); }

		/**
		 * <p>Vyplní obrázok farbou zadanou prostredníctvom farebných zložiek
		 * a úrovne (ne)priehľadnosti.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vyplň(Color)
		 */
		public Farba vyplň(int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vyplň(nováFarba);
			return nováFarba;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int, int) vyplň}.</p> */
		public Farba vypln(int r, int g, int b, int a) { return vyplň(r, g, b, a); }


		/**
		 * <p>Vyplní zadaný tvar textúrou prečítanou zo súboru s obrázkom
		 * a výsledok nakreslí do obrázka.</p>
		 * 
		 * <p>Zadaný tvar by mal byť generovaný niektorým robotom (metódami
		 * na kreslenie tvarov), pretože obrázky majú súradnicový priestor
		 * prispôsobený prostrediu rámca. Zvolený grafický robot by mal mať
		 * {@linkplain GRobot#nekresliTvary() vypnuté kreslenie tvarov}, aby
		 * vygenerovaný tvar nenakreslil do aktívneho plátna (prípadne do
		 * obrázka, do ktorého bolo jeho kreslenie presmerované).</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
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
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     textúrou
		 * @param súbor názov súboru s obrázkom textúry
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 *     (identifikátor {@code imageNotFound})
		 */
		public void vyplň(Shape tvar, String súbor)
		{
			BufferedImage obrázok = súborNaObrázok(súbor);

			grafika.setPaint(new TexturePaint(obrázok,
				new Rectangle2D.Double(Svet.posuňVýplňX, Svet.posuňVýplňY,
					obrázok.getWidth()  * Svet.mierkaVýplneX,
					obrázok.getHeight() * Svet.mierkaVýplneY)));

			if (0 == Svet.otočVýplňΑ)
				grafika.fill(tvar);
			else
			{
				double β = Math.toRadians(Svet.otočVýplňΑ);
				grafika.rotate(-β, Svet.otočVýplňX, Svet.otočVýplňY);
				Shape s = AffineTransform.getRotateInstance(β,
					Svet.otočVýplňX, Svet.otočVýplňY).
					createTransformedShape(tvar);

				grafika.fill(s);

				grafika.rotate(β, Svet.otočVýplňX, Svet.otočVýplňY);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypln(Shape, String) vypln}.</p> */
		public void vypln(Shape tvar, String súbor) { vyplň(tvar, súbor); }

		/**
		 * <p>Vyplní zadaný tvar textúrou určenou zadaným obrázkom a výsledok
		 * nakreslí do tohto obrázka. Ako textúra by nemal byť použitý ten
		 * obrázok, do ktorého je vypĺňaný tvar kreslený.</p>
		 * 
		 * <p>Zadaný tvar by mal byť generovaný niektorým robotom (metódami
		 * na kreslenie tvarov), pretože obrázky majú súradnicový priestor
		 * prispôsobený prostrediu rámca. Zvolený grafický robot by mal mať
		 * {@linkplain GRobot#nekresliTvary() vypnuté kreslenie tvarov}, aby
		 * vygenerovaný tvar nenakreslil do aktívneho plátna (prípadne do
		 * obrázka, do ktorého bolo jeho kreslenie presmerované).</p>
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
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     textúrou
		 * @param výplň obrázok s textúrou
		 */
		public void vyplň(Shape tvar, Image výplň)
		{
			BufferedImage obrázok = preveďNaBufferedImage(výplň);
			BufferedImage relevantný = dajRelevantnýRaster(obrázok);

			float priehľadnosť = (obrázok instanceof Obrázok) ?
				((Obrázok)obrázok).priehľadnosť : 1.0f;

			if (priehľadnosť > 0)
			{
				grafika.setPaint(new TexturePaint(relevantný,
					new Rectangle2D.Double(Svet.posuňVýplňX, Svet.posuňVýplňY,
						relevantný.getWidth(null)  * Svet.mierkaVýplneX,
						relevantný.getHeight(null) * Svet.mierkaVýplneY)));

				Shape s = tvar; double β = 0.0;

				if (0 != Svet.otočVýplňΑ)
				{
					β = Math.toRadians(Svet.otočVýplňΑ);
					grafika.rotate(-β, Svet.otočVýplňX, Svet.otočVýplňY);
					s = AffineTransform.getRotateInstance(β,
						Svet.otočVýplňX, Svet.otočVýplňY).
						createTransformedShape(tvar);
				}

				if (priehľadnosť < 1)
				{
					Composite záloha = grafika.getComposite();
					grafika.setComposite(
						AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, priehľadnosť));
					grafika.fill(s);
					grafika.setComposite(záloha);
				}
				else
					grafika.fill(s);

				if (0 != Svet.otočVýplňΑ)
					grafika.rotate(β, Svet.otočVýplňX, Svet.otočVýplňY);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypln(Shape, Image) vypln}.</p> */
		public void vypln(Shape tvar, Image výplň) { vyplň(tvar, výplň); }

		/**
		 * <p>Nakreslí do stredu tohto obrázka obrázok zo zadaného súboru.</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param súbor názov súboru s obrázkom
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void kresli(String súbor)
		{
			BufferedImage obrázok = súborNaObrázok(súbor);
			int x = (šírka - obrázok.getWidth()) / 2;
			int y = (výška - obrázok.getHeight()) / 2;

			// System.out.println(súbor + " x: " + x + " y: " + y);

			grafika.translate(-posunX, -posunY);
			grafika.drawImage(obrázok, x, y, null);
			grafika.translate(posunX, posunY);
		}

		/**
		 * <p>Nakreslí do tohto obrázka obrázok zo zadaného súboru, ktorý bude
		 * posunutý od stredu o zadané súradnice v horizontálnom
		 * a vertikálnom smere.</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param x posun od stredu v horizontálnom smere
		 * @param y posun od stredu vo vertikálnom smere
		 * @param súbor názov súboru s obrázkom
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void kresli(double x, double y, String súbor)
		{
			BufferedImage obrázok = súborNaObrázok(súbor);
			x = (šírka - obrázok.getWidth()) / 2.0 + x;
			y = (výška - obrázok.getHeight()) / 2.0 - y;

			// System.out.println(súbor + " x: " + x + " y: " + y);

			grafika.translate(-posunX, -posunY);
			grafika.drawImage(obrázok, (int)x, (int)y, null);
			grafika.translate(posunX, posunY);
		}

		// /**
		//  * <p>Nakreslí do tohto obrázka obrázok zo zadaného súboru, ktorý
		//  * bude posunutý o zadané súradnice v horizontálnom a vertikálnom
		//  * smere.</p>
		//  * 
		//  * @param bod súradnice, o ktoré má byť obrázok posunutý
		//  * @param súbor názov súboru s obrázkom
		//  */
		// public void kresli(Point2D bod, String súbor)
		// { kresli(bod.getX(), bod.getY(), súbor); }

		/**
		 * <p>Nakreslí do tohto obrázka obrázok zo zadaného súboru, obrázok bude
		 * posunutý o súradnice určené polohou zadaného objektu.</p>
		 * 
		 * @param objekt objekt, ktorého súradnice určia posunutie obrázka
		 * @param súbor názov súboru s obrázkom
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void kresli(Poloha objekt, String súbor)
		{ kresli(objekt.polohaX(), objekt.polohaY(), súbor); }

		/**
		 * <p>Nakreslí do stredu tohto obrázka zadaný obrázok.</p>
		 * 
		 * @param obrázok obrázok, ktorý má byť nakreslený
		 */
		public void kresli(Image obrázok)
		{
			Image relevantný = dajRelevantnýRaster(obrázok);

			int x = (šírka - relevantný.getWidth(null)) / 2;
			int y = (výška - relevantný.getHeight(null)) / 2;

			grafika.translate(-posunX, -posunY);
			grafika.drawImage(relevantný, x, y, null);
			grafika.translate(posunX, posunY);
		}

		/**
		 * <p>Nakreslí do tohto obrázka zadaný obrázok, pričom ho posunie
		 * od stredu o zadané súradnice v horizontálnom a vertikálnom smere.</p>
		 * 
		 * @param x posun od stredu v horizontálnom smere
		 * @param y posun od stredu vo vertikálnom smere
		 * @param obrázok obrázok, ktorý má byť nakreslený
		 */
		public void kresli(double x, double y, Image obrázok)
		{
			Image relevantný = dajRelevantnýRaster(obrázok);

			x = (šírka - relevantný.getWidth(null)) / 2.0 + x;
			y = (výška - relevantný.getHeight(null)) / 2.0 - y;

			grafika.translate(-posunX, -posunY);
			grafika.drawImage(relevantný, (int)x, (int)y, null);
			grafika.translate(posunX, posunY);
		}

		// /**
		//  * <p>Nakreslí do tohto obrázka zadaný obrázok, pričom ho posunie
		//  * o zadané súradnice v horizontálnom a vertikálnom smere.</p>
		//  * 
		//  * @param bod súradnice, o ktoré má byť obrázok posunutý
		//  * @param obrázok obrázok, ktorý má byť nakreslený
		//  */
		// public void kresli(Poloha bod, BufferedImage obrázok)
		// { kresli(bod.polohaX(), bod.polohaY(), obrázok); }

		/**
		 * <p>Nakreslí do tohto obrázka zadaný obrázok, obrázok bude
		 * posunutý o súradnice určené polohou zadaného objektu.</p>
		 * 
		 * @param objekt objekt, ktorého súradnice určia posunutie obrázka
		 * @param obrázok obrázok, ktorý má byť nakreslený
		 */
		public void kresli(Poloha objekt, Image obrázok)
		{ kresli(objekt.polohaX(), objekt.polohaY(), obrázok); }


		/**
		 * <p>Vyplní obrázok zadanou textúrou. Textúra je súbor s obrázkom, ktorý
		 * bude použitý na dlaždicové vyplnenie celej plochy tohto obrázka.</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
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
		 * @see #vyplň(Color)
		 * @see Svet#priečinokObrázkov(String)
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 *     (identifikátor {@code imageNotFound})
		 */
		public void vyplň(String súbor)
		{
			BufferedImage obrázok = súborNaObrázok(súbor);

			grafika.setPaint(new TexturePaint(obrázok,
				new Rectangle2D.Double(
					Svet.posuňVýplňX + posunX, Svet.posuňVýplňY + posunY,
					obrázok.getWidth()  * Svet.mierkaVýplneX,
					obrázok.getHeight() * Svet.mierkaVýplneY)));

			grafika.translate(-posunX, -posunY);

			if (0 == Svet.otočVýplňΑ)
				grafika.fillRect(0, 0, šírka, výška);
			else
			{
				double β = Math.toRadians(Svet.otočVýplňΑ);
				grafika.rotate(-β, Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY);

				Shape s = AffineTransform.getRotateInstance(β,
					Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY).
					createTransformedShape(new Rectangle(0, 0, šírka, výška));

				// grafika.fillRect(0, 0, šírka, výška);
				grafika.fill(s);

				grafika.rotate(β, Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY);
			}

			grafika.translate(posunX, posunY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(String) vyplň}.</p> */
		public void vypln(String súbor) { vyplň(súbor); }

		/**
		 * <p>Vyplní obrázok zadanou textúrou. Textúra je iný {@linkplain 
		 * Obrázok obrázok} (objekt typu {@link Image Image}
		 * alebo odvodený), ktorý bude použitý na dlaždicové vyplnenie celej
		 * plochy tohto obrázka.</p>
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
		 * 
		 * @see #vyplň(Color)
		 */
		public void vyplň(Image výplň)
		{
			BufferedImage obrázok = preveďNaBufferedImage(výplň);
			BufferedImage relevantný = dajRelevantnýRaster(obrázok);

			float priehľadnosť = (obrázok instanceof Obrázok) ?
				((Obrázok)obrázok).priehľadnosť : 1.0f;

			if (priehľadnosť > 0)
			{
				grafika.setPaint(new TexturePaint(relevantný,
					new Rectangle2D.Double(
						Svet.posuňVýplňX + posunX, Svet.posuňVýplňY + posunY,
						relevantný.getWidth(null)  * Svet.mierkaVýplneX,
						relevantný.getHeight(null) * Svet.mierkaVýplneY)));

				grafika.translate(-posunX, -posunY);

				Shape s = new Rectangle(0, 0, šírka, výška); double β = 0.0;

				if (0 != Svet.otočVýplňΑ)
				{
					β = Math.toRadians(Svet.otočVýplňΑ);
					grafika.rotate(-β,
						Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY);

					s = AffineTransform.getRotateInstance(β,
						Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY).
						createTransformedShape(s);
				}

				if (priehľadnosť < 1)
				{
					Composite záloha = grafika.getComposite();
					grafika.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, priehľadnosť));
					// grafika.fillRect(0, 0, šírka, výška);
					grafika.fill(s);
					grafika.setComposite(záloha);
				}
				else
					// grafika.fillRect(0, 0, šírka, výška);
					grafika.fill(s);

				if (0 != Svet.otočVýplňΑ) grafika.rotate(β,
					Svet.otočVýplňX + posunX, Svet.otočVýplňY + posunY);

				grafika.translate(posunX, posunY);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Image) vyplň}.</p> */
		public void vypln(Image výplň) { vyplň(výplň); }


		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu, ktorá sa odtiaľ
		 * rozšíri po okraje tej časti kresby v obrázku, v ktorej sa zadaný
		 * bod nachádza. Na ďalšie informácie pozri metódu {@link 
		 * Plátno#vylejFarbu(double, double, Color)}. Správanie
		 * tejto metódy je jej veľmi podobné.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param farba objekt určujúci farbu výplne
		 */
		public void vylejFarbu(double x, double y, Color farba)
		{
			VykonajVObrázku.vylejFarbu(this, x, y, farba);

			// Prečo tu bolo toto(?):
			// 
			// 	if (Svet.právePrekresľujem) return;
			// 	Svet.automatickéPrekreslenie();
			// 
			// Buď omyl, alebo na testovanie. Nemá predsa zmysel,
			// aby inštancia triedy Obrázok automaticky prekresľovala
			// svet. (Vysvetlené to je inde.)
		}

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu (určenú objektom),
		 * ktorá sa odtiaľ rozšíri po okraje tej časti kresby v obrázku,
		 * v ktorej sa zadaný bod nachádza. Na ďalšie informácie pozri
		 * metódu {@link Plátno#vylejFarbu(double, double, Color)}.
		 * Správanie tejto metódy je jej veľmi podobné.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param objekt objekt určujúci farbu výplne
		 */
		public void vylejFarbu(double x, double y, Farebnosť objekt)
		{
			VykonajVObrázku.vylejFarbu(this, x, y, objekt.farba());
			// Vymazané to isté, čo bolo v metóde (pozri komentáre):
			// public void vylejFarbu(double x, double y, Color farba)
		}

		/* *
		 * <p>Toto je „klon“ metódy {@link #vylejFarbu(double, double,
		 * Color)}. Proces vyplnenia sa uskutoční len v prípade, že je
		 * v premennej typu {@link Object} (zadanej ako parameter) uložená
		 * inštancia triedy {@link Farba Farba} alebo {@link Color Color}.</p>
		 * /
		public void vylejFarbu(double x, double y, Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vylejFarbu(x, y, (Farba)farba);
		}
		*/

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu zadanú prostredníctvom
		 * farebných zložiek, ktorá sa určeného bodu rozšíri po okraje
		 * okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double, double, Color)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vylejFarbu(double, double, Color)
		 */
		public Farba vylejFarbu(double x, double y, int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vylejFarbu(x, y, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu zadanú prostredníctvom
		 * farebných zložiek a úrovne (ne)priehľadnosti, pričom farba sa
		 * z určeného bodu rozšíri k okrajom okolitej kresby. Na ďalšie
		 * informácie pozri metódu {@link #vylejFarbu(double, double,
		 * Color)}. Správanie tejto metódy je odvodené od nej.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vylejFarbu(double, double, Color)
		 */
		public Farba vylejFarbu(double x, double y, int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vylejFarbu(x, y, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu, ktorá sa odtiaľ rozšíri
		 * po okraje tej časti kresby v obrázku, v ktorej sa zadaný bod
		 * nachádza. Na ďalšie informácie pozri metódu {@link 
		 * Plátno#vylejFarbu(double, double, Color)}. Správanie tejto
		 * metódy je jej veľmi podobné.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param farba objekt určujúci farbu výplne
		 */
		public void vylejFarbu(Poloha bod, Color farba)
		{
			VykonajVObrázku.vylejFarbu(this,
				bod.polohaX(), bod.polohaY(), farba);
			// Vymazané to isté, čo bolo v metóde (pozri komentáre):
			// public void vylejFarbu(double x, double y, Color farba)
		}

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu (určenú objektom), ktorá
		 * sa odtiaľ rozšíri po okraje tej časti kresby v obrázku, v ktorej
		 * sa zadaný bod nachádza. Na ďalšie informácie pozri metódu {@link 
		 * Plátno#vylejFarbu(double, double, Color)}. Správanie tejto
		 * metódy je jej veľmi podobné.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param objekt objekt určujúci farbu výplne
		 */
		public void vylejFarbu(Poloha bod, Farebnosť objekt)
		{
			VykonajVObrázku.vylejFarbu(this,
				bod.polohaX(), bod.polohaY(), objekt.farba());
			// Vymazané to isté, čo bolo v metóde (pozri komentáre):
			// public void vylejFarbu(double x, double y, Color farba)
		}

		/* *
		 * <p>Toto je „klon“ metódy {@link #vylejFarbu(double, double,
		 * Color)}. Proces vyplnenia sa uskutoční len v prípade, že je
		 * v premennej typu {@link Object} (zadanej ako parameter) uložená
		 * inštancia triedy {@link Farba Farba} alebo {@link Color Color}.</p>
		 * /
		public void vylejFarbu(Poloha bod, Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vylejFarbu(bod, (Farba)farba);
		}
		*/

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu zadanú prostredníctvom
		 * farebných zložiek, ktorá sa určeného bodu rozšíri po okraje
		 * okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double, double, Color)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vylejFarbu(Poloha, Color)
		 */
		public Farba vylejFarbu(Poloha bod, int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vylejFarbu(bod, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu do obrázka farbu zadanú prostredníctvom
		 * farebných zložiek a úrovne (ne)priehľadnosti, pričom farba sa
		 * z určeného bodu rozšíri k okrajom okolitej kresby. Na ďalšie
		 * informácie pozri metódu {@link #vylejFarbu(double, double,
		 * Color)}. Správanie tejto metódy je odvodené od nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vylejFarbu(Poloha, Color)
		 */
		public Farba vylejFarbu(Poloha bod, int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vylejFarbu(bod, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Zadaný robot vyleje na svojej pozícii do obrázka svoju aktuálnu
		 * {@linkplain GRobot#farba() farbu}, ktorá sa odtiaľ rozšíri po
		 * okraje okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double, double, Color)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param ktorý robot, ktorého poloha a farba sú použité na výplň
		 */
		public void vylejFarbu(GRobot ktorý)
		{ vylejFarbu(ktorý.aktuálneX, ktorý.aktuálneY, ktorý.farbaRobota); }


	// Aktívne filtre

		/**
		 * <p>Pretvorí tento obrázok na svoj farebný negatív.</p>
		 */
		public void negatív() { VykonajVObrázku.negatív(this); }

		/** <p><a class="alias"></a> Alias pre {@link #negatív() negatív}.</p> */
		public void negativ() { VykonajVObrázku.negatív(this); }


		/**
		 * <p>Zvýši úroveň svetlosti tohto obrázka. Metóda používa rovnaký
		 * spôsob zosvetlenia ako metóda {@link Farba#svetlejšia(double)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @param faktor faktor zosvetlenia – hodnota v rozsahu 0.0 – 1.0,
		 *     pričom krajné hodnoty (0.0 a 1.0) nie sú povolené; čím je
		 *     hodnota faktora nižšia, tým je zmena svetlosti výraznejšia
		 * 
		 * @see #tmavší(double)
		 */
		public void bledší(double faktor)
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				// if (0 != (údajeObrázka[j] & 0xff000000))
				// {
					int r = (údajeObrázka[j] >> 16) & 0xff;
					int g = (údajeObrázka[j] >>  8) & 0xff;
					int b =  údajeObrázka[j]        & 0xff;

					int i = (int)(1.0 / (1.0 - faktor));

					if (r == 0 && g == 0 && b == 0)
					{
						r = g = b = i;
					}
					else
					{

						if (r > 0 && r < i) r = i;
						if (g > 0 && g < i) g = i;
						if (b > 0 && b < i) b = i;

						r /= faktor; if (r > 255) r = 255;
						g /= faktor; if (g > 255) g = 255;
						b /= faktor; if (b > 255) b = 255;
					}

					// a je už „prepočítané“/má správnu hodnotu (a << 24):
					údajeObrázka[j] = a | (r << 16) | (g << 8) | b;
				// }
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #bledší(double) bledší}.</p> */
		public void bledsi(double faktor) { bledší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #bledší(double) bledší}.</p> */
		public void svetlejší(double faktor) { bledší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #bledší(double) bledší}.</p> */
		public void svetlejsi(double faktor) { bledší(faktor); }

		/**
		 * <p>Zníži úroveň svetlosti tohto obrázka. Metóda používa rovnaký
		 * spôsob stmavenia ako metóda {@link Farba#tmavšia(double)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @param faktor faktor stmavenia – hodnota v rozsahu 0.0 – 1.0,
		 *     pričom krajné hodnoty (0.0 a 1.0) nie sú na použitie vhodné
		 *     (0.0 by stmavila farbu do čiernej a 1.0 by nevykonala žiadnu
		 *     zmenu svetlosti); čím je hodnota faktora nižšia, tým je
		 *     úroveň stmavenia výraznejšia
		 * 
		 * @see #bledší(double)
		 */
		public void tmavší(double faktor)
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				// if (0 != (údajeObrázka[j] & 0xff000000))
				// {
					int r = (údajeObrázka[j] >> 16) & 0xff;
					int g = (údajeObrázka[j] >>  8) & 0xff;
					int b =  údajeObrázka[j]        & 0xff;

					r *= faktor; if (r < 0) r = 0;
					g *= faktor; if (g < 0) g = 0;
					b *= faktor; if (b < 0) b = 0;

					// a je už „prepočítané“/má správnu hodnotu (a << 24):
					údajeObrázka[j] = a | (r << 16) | (g << 8) | b;
				// }
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #tmavší(double) tmavší}.</p> */
		public void tmavsi(double faktor) { tmavší(faktor); }

		/**
		 * <p>Zvýši úroveň svetlosti tohto obrázka. Metóda používa rovnaký
		 * spôsob zosvetlenia ako metóda {@link Farba#svetlejšia()}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda používa predvolenú
		 * hodnotu faktoru zosvetlenia {@code num0.7}. Pozri aj {@link 
		 * #bledší(double) bledší(faktor)}.</p>
		 * 
		 * @see #tmavší()
		 */
		public void bledší() { bledší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #bledší() bledší}.</p> */
		public void bledsi() { bledší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #bledší() bledší}.</p> */
		public void svetlejší() { bledší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #bledší() bledší}.</p> */
		public void svetlejsi() { bledší(faktor); }

		/**
		 * <p>Zníži úroveň svetlosti tohto obrázka. Metóda používa rovnaký
		 * spôsob stmavenia ako metóda {@link Farba#tmavšia()}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda používa predvolenú
		 * hodnotu faktoru stmavenia {@code num0.7}. Pozri aj {@link 
		 * #tmavší(double) tmavší(faktor)}.</p>
		 * 
		 * @see #bledší()
		 */
		public void tmavší() { tmavší(faktor); }

		/** <p><a class="alias"></a> Alias pre {@link #tmavší(double) tmavší}.</p> */
		public void tmavsi() { tmavší(faktor); }


		/**
		 * <p>Upraví tento obrázok do odtieňov šedej s použitím priemerovania
		 * farebných zložiek. Je to rýchla metóda, ale výsledné odtiene nie sú
		 * v súlade s modelom ľudského vnímania farieb. Pozri aj {@linkplain 
		 * #čiernobiely(boolean) čiernobiely(vyvážiťZložky)}.</p>
		 * 
		 * <p>Porovnanie rôznych metód prevodu do odtieňov šedej je v opise
		 * metódy {@link #čiernobiely(boolean) čiernobiely(vyvážiťZložky)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @see #čiernobiely(boolean)
		 * @see #čiernobiely(double, double, double)
		 * @see #monochromatický(Color)
		 * @see #farebnýFilter(Color)
		 * @see #farebnéTienidlo(Color)
		 */
		public void čiernobiely()
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				// if (0 != (údajeObrázka[j] & 0xff000000))
				// {
					int r = (údajeObrázka[j] >> 16) & 0xff;
					int g = (údajeObrázka[j] >>  8) & 0xff;
					int b =  údajeObrázka[j]        & 0xff;

					r = (r + g + b) / 3;

					// a je už „prepočítané“/má správnu hodnotu (a << 24):
					údajeObrázka[j] = a | (r << 16) | (r << 8) | r;
				// }
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely() čiernobiely}.</p> */
		public void ciernobiely() { čiernobiely(); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely() čiernobiely}.</p> */
		public void odtieneŠedej() { čiernobiely(); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely() čiernobiely}.</p> */
		public void odtieneSedej() { čiernobiely(); }


		/**
		 * <p>Upraví tento obrázok do odtieňov šedej s možnosťou určenia
		 * predvoleného vyváženia farebných zložiek – podľa modelu ľudského
		 * vnímania farieb.</p>
		 * 
		 * <p>Ak je parameter {@code vyvážiťZložky} rovný {@code valtrue},
		 * tak bude použité predvolené vyváženie zložiek, inak bude použité
		 * priemerovanie, rovnako ako pri metóde {@link #čiernobiely()
		 * čiernobiely()}.</p>
		 * 
		 * <p>Predvolené hodnoty vyváženia sú nasledujúce: 29,9 % červenej,
		 * 58,7 % zelenej a 11,4 % modrej. Ak chcete použiť vlastné váhy,
		 * tak použite metódu {@link #čiernobiely(double, double, double)
		 * čiernobiely(váhaČervenej, váhaZelenej, váhaModrej)}.</p>
		 * 
		 * <p><image>slnecnica-do-odtienov-sedej.jpeg<alt/>Porovnanie rôznych
		 * metód prevodu do odtieňov šedej.</image>Porovnanie rôznych metód
		 * prevodu do odtieňov šedej.</p>
		 * 
		 * <p>Obrázok vyššie porovnáva rôzne metódy prevodu obrázka do
		 * odtieňov šedej. Vľavo hore je pôvodný farebný obrázok, vedľa neho
		 * je obrázok prevedený metódou {@link #čiernobiely() čiernobiely()}
		 * (priemerovanie zložiek), vľavo dole je verzia prevedená s pomocou
		 * tejto metódy s parametrom {@code vyvážiťZložky} rovným
		 * {@code valtrue} a vpravo dole je použitá metóda {@link 
		 * #čiernobiely(double, double, double) čiernobiely(váhaČervenej,
		 * váhaZelenej, váhaModrej)} s váhami nastavenými na hodnoty:
		 * {@code num0.0} (červená), {@code num0.3} (zelená) a {@code num0.5}
		 * (modrá). To znamená, že pôvodná červená zložka je z odtieňov
		 * elimiovaná a súčet zostávajúcich dvoch nie
		 * je rovný 100 % (0,3 = 30 %; 0,5 = 50 %; čo je dohromady len 80 %).
		 * Z toho je jasné, že výsledok bude tmavší. Obrázok vpravo dole
		 * skutočne pôsobí akoby bol snímaný v odlišnom spektre
		 * elektromagnetického vlnenia (čo je, samozrejme, falošný dojem,
		 * pretože chýbajúcu informáciu nemôžeme od obrazu doplniť žiadnym
		 * prevodom, ale môžeme aspoň nasimulovať taký efekt).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * <p>Obrázok nižšie ukazuje porovnanie metódy priemerovania
		 * a automatického vyváženia zložiek na staršej verzii predvolenej
		 * palety {@linkplain Farebnosť#preddefinovanéFarby preddefinovaných
		 * farieb}.</p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>paleta-predvolena.png<alt/>predvolená
		 * paleta</image></td>
		 * <td><image>paleta-ciernobiela1.png<alt/>priemerovaná
		 * paleta</image></td>
		 * <td><image>paleta-ciernobiela2.png<alt/>vyvážená
		 * paleta</image></td>
		 * </tr><tr>
		 * <td><p class="image"><small>a) predvolená paleta</small></p></td>
		 * <td><p class="image"><small>b) použitie metódy
		 * priemerovania</small></p></td>
		 * <td><p class="image"><small>c) použitie metódy vyváženia
		 * zložiek</small></p></td>
		 * </tr><tr><td colspan="3"><p class="image">Porovnanie rôznych metód
		 * prevodu do odtieňov šedej.</p></td></tr></table>
		 * 
		 * <p><b>Použitý zdroj:</b></p>
		 * 
		 * <ul><li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-3-greyscale-conversion/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 3: Greyscale Conversion.</em> Dreamland
		 * Fantasy Studios, 2008, 2010. Citované: 2018.</a></li></ul>
		 * 
		 * @param vyvážiťZložky pravdivostná hodnota určujúca, či majú byť
		 *     odtiene vypočítané algoritmom vyvažovania farebných zložiek
		 * 
		 * @see #čiernobiely()
		 * @see #čiernobiely(double, double, double)
		 * @see #monochromatický(Color)
		 * @see #farebnýFilter(Color)
		 * @see #farebnéTienidlo(Color)
		 */
		public void čiernobiely(boolean vyvážiťZložky)
		{
			// Príkazy na vyrobenie prvého obrázka (vyššie):
			// Obrázok predloha = čítaj("slnecnica.png").
			// 	zmeňVeľkosť(0.5);
			// 
			// podlaha.obrázok(-400, 300, predloha);
			// 
			// Obrázok čiernobiely1 = new Obrázok(predloha);
			// čiernobiely1.čiernobiely();
			// podlaha.obrázok(0, 300, čiernobiely1);
			// 
			// Obrázok čiernobiely2 = new Obrázok(predloha);
			// čiernobiely2.čiernobiely(true);
			// podlaha.obrázok(-400, 0, čiernobiely2);
			// 
			// Obrázok čiernobiely3 = new Obrázok(predloha);
			// čiernobiely3.čiernobiely(0.0, 0.3, 0.5);
			// podlaha.obrázok(0, 0, čiernobiely3);
			// 
			// zbaľ();
			// vystreď();
			// 
			// podlaha.uložObrázok("slnecnica-do-odtienov-sedej.jpeg", false);


			if (!vyvážiťZložky)
			{
				čiernobiely();
				return;
			}

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				int r = (údajeObrázka[j] >> 16) & 0xff;
				int g = (údajeObrázka[j] >>  8) & 0xff;
				int b =  údajeObrázka[j]        & 0xff;

				r = (int)(0.299 * (double)r + 0.587 * (double)g +
					0.114 * (double)b);

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeObrázka[j] = a | (r << 16) | (r << 8) | r;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void ciernobiely(boolean vyvážiťZložky)
		{ čiernobiely(vyvážiťZložky); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void odtieneŠedej(boolean vyvážiťZložky)
		{ čiernobiely(vyvážiťZložky); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void odtieneSedej(boolean vyvážiťZložky)
		{ čiernobiely(vyvážiťZložky); }


		/**
		 * <p>Upraví tento obrázok do odtieňov šedej s možnosťou určenia
		 * vlastného vyváženia jednotlivých farebných zložiek. Táto metóda
		 * umožňuje úplne odfiltrovať alebo preexponovať niektorú farebnú
		 * zložku.</p>
		 * 
		 * <p>Porovnanie rôznych metód prevodu do odtieňov šedej je v opise
		 * metódy {@link #čiernobiely(boolean) čiernobiely(vyvážiťZložky)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @param váhaČervenej hodnota určujúca mieru vyváženia červenej
		 *     farebnej zložky
		 * @param váhaZelenej hodnota určujúca mieru vyváženia zelenej
		 *     farebnej zložky
		 * @param váhaModrej hodnota určujúca mieru vyváženia modrej
		 *     farebnej zložky
		 * 
		 * @see #čiernobiely()
		 * @see #čiernobiely(boolean)
		 * @see #monochromatický(Color)
		 * @see #farebnýFilter(Color)
		 * @see #farebnéTienidlo(Color)
		 */
		public void čiernobiely(double váhaČervenej, double váhaZelenej,
			double váhaModrej)
		{

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				int r = (údajeObrázka[j] >> 16) & 0xff;
				int g = (údajeObrázka[j] >>  8) & 0xff;
				int b =  údajeObrázka[j]        & 0xff;

				r = (int)(váhaČervenej * (double)r +
					váhaZelenej * (double)g + váhaModrej * (double)b);

				if (r < 0) r = 0; else if (r > 255) r = 255;

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeObrázka[j] = a | (r << 16) | (r << 8) | r;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void ciernobiely(double váhaČervenej, double váhaZelenej,
			double váhaModrej)
		{ čiernobiely(váhaČervenej, váhaZelenej, váhaModrej); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void odtieneŠedej(double váhaČervenej, double váhaZelenej,
			double váhaModrej)
		{ čiernobiely(váhaČervenej, váhaZelenej, váhaModrej); }

		/** <p><a class="alias"></a> Alias pre {@link #čiernobiely(boolean) čiernobiely}.</p> */
		public void odtieneSedej(double váhaČervenej, double váhaZelenej,
			double váhaModrej)
		{ čiernobiely(váhaČervenej, váhaZelenej, váhaModrej); }


		/**
		 * <p>Upraví tento obrázok do odtieňov zadanej farby. Monochromatická
		 * zložka, čiže zložka jasu (odtiene šedej), bude násobená farebnými
		 * zložkami zadanej farby. Výsledkom bude obrázok v odtieňoch zadanej
		 * farby. Ak zadáme čiernu farbu, získame čierny obrázok. Ak zadáme
		 * bielu, dostaneme čiernobiely obrázok. Ľubovoľná iná farba zafarbí
		 * obrázok do jej odtieňov.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @param farba farba, do odtieňov ktorej bude obrázok prevedený
		 * 
		 * @see #čiernobiely()
		 * @see #čiernobiely(boolean)
		 * @see #čiernobiely(double, double, double)
		 * @see #farebnýFilter(Color)
		 * @see #farebnéTienidlo(Color)
		 */
		public void monochromatický(Color farba)
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			int i = farba.getRGB();
			int R = (i >> 16) & 0xff;
			int G = (i >>  8) & 0xff;
			int B =  i        & 0xff;

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				// if (0 != (údajeObrázka[j] & 0xff000000))
				// {
					int r = (údajeObrázka[j] >> 16) & 0xff;
					int g = (údajeObrázka[j] >>  8) & 0xff;
					int b =  údajeObrázka[j]        & 0xff;

					r = (r + g + b) / 3;
					g = (r * G) / 0xff;
					b = (r * B) / 0xff;
					r = (r * R) / 0xff;

					// a je už „prepočítané“/má správnu hodnotu (a << 24):
					údajeObrázka[j] = a | (r << 16) | (g << 8) | b;
				// }
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Color) monochromatický}.</p> */
		public void monochromaticky(Color farba) { monochromatický(farba); }

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Color) monochromatický}.</p> */
		public void jednofarebný(Color farba) { monochromatický(farba); }

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Color) monochromatický}.</p> */
		public void jednofarebny(Color farba) { monochromatický(farba); }


		/**
		 * <p>Použije na tento obrázok zadaný farebný filter. Obrázok
		 * nadobudne vzhľad, ako keby sme sa na neho pozerali cez filter
		 * prepúšťajúci len zadanú farbu.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * @param farba farba filtra
		 * 
		 * @see #čiernobiely()
		 * @see #čiernobiely(boolean)
		 * @see #čiernobiely(double, double, double)
		 * @see #monochromatický(Color)
		 * @see #farebnéTienidlo(Color)
		 */
		public void farebnýFilter(Color farba)
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			int i = farba.getRGB();
			int R = (i >> 16) & 0xff;
			int G = (i >>  8) & 0xff;
			int B =  i        & 0xff;

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				// if (0 != (údajeObrázka[j] & 0xff000000))
				// {
					int r = (údajeObrázka[j] >> 16) & 0xff;
					int g = (údajeObrázka[j] >>  8) & 0xff;
					int b =  údajeObrázka[j]        & 0xff;

					r = (r + g + b) / 3;
					g = (r + G) / 2;
					b = (r + B) / 2;
					r = (r + R) / 2;

					// a je už „prepočítané“/má správnu hodnotu (a << 24):
					údajeObrázka[j] = a | (r << 16) | (g << 8) | b;
				// }
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #farebnýFilter(Color) farebnýFilter}.</p> */
		public void farebnyFilter(Color farba) { farebnýFilter(farba); }


		/**
		 * <p>Použije na tento obrázok zadané farebné tienidlo. Obrázok
		 * nadobudne vzhľad, ako keby sme sa na neho pozerali cez tienidlo,
		 * ktoré má zadanú farbu. Všetky farby v obrázku (ich zložky) budú
		 * posunuté smerom k zadanej farbe.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda
		 * zachováva aj kanál priehľadnosti obrázka.</p>
		 * 
		 * <p>Nasledujúci obrázok zhromažďuje a porovnáva rôzne efekty
		 * použité na tú istú predlohu:</p>
		 * 
		 * <p><image>slnecnica-rozne-efekty.png<alt/></image>Porovnanie
		 * rôznych efekty aplikovaných na ten istý obrázok.</p>
		 * 
		 * <p>V bloku A je pôvodný obrázok. Blok B obsahuje použitie efektu
		 * {@linkplain #posterizuj(Color[]) posterizácie} s a bez algoritmu
		 * {@linkplain #posterizuj(boolean, Color[]) difúzie chyby} (prvý
		 * riadok bloku je bez difúzie chyby, druhý s ňou). Stĺpce bloku sú
		 * posterizované postupne s použitím troch farieb (červenej, zelenej
		 * a modrej), šiestich farieb (pribudla tyrkysová, purpurová a žltá)
		 * a staršej verzie predvolenej palety {@linkplain 
		 * Farebnosť#preddefinovanéFarby preddefinovaných farieb}.</p>
		 * 
		 * <p>V bloku C sú pod sebou použité efekty {@linkplain 
		 * #monochromatický(Color) monochromatickosti}, {@linkplain 
		 * #farebnýFilter(Color) farebného filtra} a {@linkplain 
		 * #farebnéTienidlo(Color) farebného tienidla} s červenou farbou.
		 * Blok D obsahuje vedľa seba použitie tých istých efektov so zelenou
		 * farbou a blok E s modrou farbou.</p>
		 * 
		 * @param farba farba tienidla
		 * 
		 * @see #čiernobiely()
		 * @see #čiernobiely(boolean)
		 * @see #čiernobiely(double, double, double)
		 * @see #monochromatický(Color)
		 * @see #farebnýFilter(Color)
		 */
		public void farebnéTienidlo(Color farba)
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			int i = farba.getRGB();
			int R = (i >> 16) & 0xff;
			int G = (i >>  8) & 0xff;
			int B =  i        & 0xff;

			for (int j = 0; j < údajeObrázka.length; ++j)
			{
				int a = údajeObrázka[j] & 0xff000000;
				int r = (údajeObrázka[j] >> 16) & 0xff;
				int g = (údajeObrázka[j] >>  8) & 0xff;
				int b =  údajeObrázka[j]        & 0xff;

				g = (r + G) / 2;
				b = (g + B) / 2;
				r = (b + R) / 2;

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeObrázka[j] = a | (r << 16) | (g << 8) | b;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #farebnéTienidlo(Color) farebnéTienidlo}.</p> */
		public void farebneTienidlo(Color farba) { farebnéTienidlo(farba); }


		/**
		 * <p>Upraví tento obrázok do odtieňov farby zadaného objektu.
		 * Monochromatická zložka, čiže zložka jasu (odtiene šedej), bude
		 * násobená farebnými zložkami farby zadaného objektu. Výsledkom bude
		 * obrázok v odtieňoch jeho farby. Ak má objekt čiernu farbu, získame
		 * čierny obrázok. Ak má objekt bielu farbu, dostaneme čiernobiely
		 * obrázok. Ľubovoľná iná farba objektu zafarbí obrázok do jej
		 * odtieňov.</p>
		 * 
		 * @param objekt objekt, do odtieňov farby ktorého bude obrázok
		 *     prevedený
		 */
		public void monochromatický(Farebnosť objekt)
		{ monochromatický(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Farebnosť) monochromatický}.</p> */
		public void monochromaticky(Farebnosť objekt)
		{ monochromatický(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Farebnosť) monochromatický}.</p> */
		public void jednofarebný(Farebnosť objekt)
		{ monochromatický(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #monochromatický(Farebnosť) monochromatický}.</p> */
		public void jednofarebny(Farebnosť objekt)
		{ monochromatický(objekt.farba()); }


		/**
		 * <p>Použije na tento obrázok farebný filter podľa farby zadaného
		 * objektu. Obrázok nadobudne vzhľad, ako keby sme sa na neho
		 * pozerali cez sklo farby zadaného objektu.</p>
		 * 
		 * @param objekt objekt určujúci farbu filtra
		 */
		public void farebnýFilter(Farebnosť objekt)
		{ farebnýFilter(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #farebnýFilter(Farebnosť) farebnýFilter}.</p> */
		public void farebnyFilter(Farebnosť objekt)
		{ farebnýFilter(objekt.farba()); }


		/**
		 * <p>Zruší priehľadnosť všetkých bodov v obrázku.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Volanie tejto metódy neovplyvní
		 * celkovú priehľadnosť obrázka ovplyvňovanú metódami
		 * {@link #priehľadnosť(double) priehľadnosť(priehľadnosť)},
		 * {@link #priehľadnosť(Priehľadnosť) priehľadnosť(objekt)}
		 * a {@link #upravPriehľadnosť(double) upravPriehľadnosť(zmena)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Princíp toho, ako funguje
		 * zrušenie priehľadnosti, je ukázaný v opise rovnomennej metódy
		 * triedy {@link Plátno Plátno} – {@link Plátno#zrušPriehľadnosť()
		 * zrušPriehľadnosť}.</p>
		 */
		public void zrušPriehľadnosť() { VykonajVObrázku.zrušPriehľadnosť(this); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušPriehľadnosť() zrušPriehľadnosť}.</p> */
		public void zrusPriehladnost() { VykonajVObrázku.zrušPriehľadnosť(this); }

		/**
		 * <p>Použije na tento obrázok masku vyrobenú zo zadaného obrázka.
		 * Maska musí mať rovnaký rozmer ako obrázok, inak operácia zlyhá.
		 * Maska je vyrobená z kombinácie intenzity farieb a priehľadnosti
		 * jednotlivých bodov zadaného obrázka. Čím je bod masky tmavší, tým
		 * bude viditeľnejší, čím svetlejší, tým menej viditeľný. Jas je
		 * korigovaný aj zložkami priehľadnosti obidvoch bodov, teda ako
		 * pôvodného bodu na obrázku, tak aj bodu prevzatého z masky.
		 * Ak je súčin ich zložiek priehľadnosti rovný nule, tak bude
		 * výsledný bod neviditeľný. Viditeľnosť bodov v ostatných
		 * prípadoch záleží na výsledku výpočtov algoritmu. Napríklad
		 * úplne čierne úplne nepriehľadné body masky neovplyvnia
		 * priehľadnosť bodov na obrázku.</p>
		 * 
		 * @param maska obrázok, ktorý bude použitý ako maska
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean použiMasku(BufferedImage maska)
		{ return VykonajVObrázku.použiMasku(this, maska); }

		/** <p><a class="alias"></a> Alias pre {@link #použiMasku(BufferedImage) použiMasku}.</p> */
		public boolean pouziMasku(BufferedImage maska)
		{ return VykonajVObrázku.použiMasku(this, maska); }

		/**
		 * <p>Použije na tento obrázok filter vyrobený zo zložky jasu kresby
		 * zadaného/predloženého obrázka. Zložka priehľadnosti bodov na
		 * obrázku, ktorý poslúži ako predloha pre filter nie je braná do
		 * úvahy. Hodnoty farebných zložiek úplne priehľadných (neviditeľných)
		 * bodov sú nepredvídateľné, preto by mal byť obrázok predlohy filtra
		 * úplne pokrytý nepriehľadnou kresbou. Svetlé/biele body na obrázku
		 * predlohy spôsobia vymazanie bodov na tomto obrázku (nastavenie
		 * hodnôt všetkých ich farebných zložiek a priehľadnosti na nulu).
		 * Tmavé/čierne body na predloženom
		 * obrázku nespôsobia na tomto obrázku žiadnu zmenu priehľadnosti.
		 * (Ostatné odtiene šedej a farebné body vo filtri spôsobia nastavenie
		 * úrovne priehľadnosti bodov obrázka na hodnotu jasu vypočítanú
		 * z priemeru farebných zložiek bodov filtra.)
		 * Obrázok s predlohou musí mať rovnaký rozmer ako tento obrázok, inak
		 * operácia zlyhá.</p>
		 * 
		 * @param kresba obrázok, ktorý bude použitý ako predloha pre filter
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean vymažKresbu(BufferedImage kresba)
		{ return VykonajVObrázku.vymažKresbu(this, kresba); }

		/** <p><a class="alias"></a> Alias pre {@link #vymažKresbu(BufferedImage) vymažKresbu}.</p> */
		public boolean vymazKresbu(BufferedImage kresba)
		{ return VykonajVObrázku.vymažKresbu(this, kresba); }


		/**
		 * <p>Vyrobí z tohto obrázka do zadaného obrázka masku priehľadnosti.
		 * Zadaný obrázok musí mať rovnakú veľkosť ako tento obrázok, inak
		 * operácia zlyhá. Vyrobená maska bude obsahovať čierne body rôznej
		 * priehľadnosti podľa priehľadnosti bodov pôvodného obrázka. Pôvodný
		 * obsah zadaného obrázka (argumentu {@code nováMaska}) bude
		 * nahradený.</p>
		 * 
		 * @param nováMaska obrázok, do ktorého bude nová maska vyrobená
		 *     (pôvodný obsah obrázka bude nahradený maskou)
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean vyrobMasku(BufferedImage nováMaska)
		{ return null != VykonajVObrázku.vyrobMasku(this, nováMaska); }

		/**
		 * <p>Vyrobí z tohto obrázka masku priehľadnosti. Metóda vytvorí masku
		 * do nového obrázka (typu {@link BufferedImage BufferedImage}),
		 * ktorý sama automaticky vytvorí a vráti ako svoj výsledok
		 * (návratovú hodnotu). Nová maska bude obsahovať čierne body rôznej
		 * priehľadnosti vychádzajúc z priehľadnosti bodov tohto obrázka.</p>
		 * 
		 * @return nový obrázok obsahujúci vyrobenú masku
		 */
		public BufferedImage vyrobMasku()
		{ return VykonajVObrázku.vyrobMasku(this, null); }


		/**
		 * <p>Rozmaže grafiku obrázka. Pre priehľadné (neviditeľné) body je
		 * pri procese rozmazania použitá zadaná farba pozadia. Opakovanie
		 * a rozsah majú z vizuálneho hľadiska podobný dopad na výsledný
		 * efekt rozmazania, ale matice lineárneho kruhového rozmazania sú
		 * vygenerované len do úrovne rozsahu 5 (vrátane). Pri zadaní vyššej
		 * hodnoty rozsahu získame rovnaký efekt ako keby sme zadali hodnotu
		 * 5. Ak chceme dosiahnuť vyššiu mieru rozmazania, musíme zvýšiť
		 * počet opakovaní procesu rozmazania (argument opakovanie). Čím
		 * vyššie sú hodnoty opakovania a rozsahu, tým vyššie sú nároky
		 * metódy na výpočtový výkon.</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania; reálny efekt dosiahneme len
		 *     v rozsahu hodnôt 1 – 5, vyššie čísla sú zaokrúhlené na 5,
		 *     nižšie nespôsobia žiadne rozmazanie
		 * @param pozadie farba použitá pri procese rozmazania pre
		 *     priehľadné body
		 */
		public void rozmaž(int opakovanie, int rozsah, Color pozadie)
		{
			grafika.translate(-posunX, -posunY);
			VykonajVObrázku.rozmaž(this, grafika, opakovanie,
				rozsah, pozadie);
			grafika.translate(posunX, posunY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, Color) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, Color pozadie)
		{ rozmaž(opakovanie, rozsah, pozadie); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , pozadie);}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(int opakovanie, Color pozadie)
		{ rozmaž(opakovanie, 1, pozadie); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, Color) rozmaž}.</p> */
		public void rozmaz(int opakovanie, Color pozadie)
		{ rozmaž(opakovanie, 1, pozadie); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code 
		 * , pozadie);}</p>
		 * 
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(Color pozadie) { rozmaž(1, 1, pozadie); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(Color) rozmaž}.</p> */
		public void rozmaz(Color pozadie) { rozmaž(1, 1, pozadie); }


		/**
		 * <p>Rozmaže grafiku obrázka. Pre priehľadné (neviditeľné) body je
		 * pri procese rozmazania použitá zadaná farba pozadia. Opakovanie
		 * a rozsah majú z vizuálneho hľadiska podobný dopad na výsledný
		 * efekt rozmazania, ale matice lineárneho kruhového rozmazania sú
		 * vygenerované len do úrovne rozsahu 5 (vrátane). Pri zadaní vyššej
		 * hodnoty rozsahu získame rovnaký efekt ako keby sme zadali hodnotu
		 * 5. Ak chceme dosiahnuť vyššiu mieru rozmazania, musíme zvýšiť
		 * počet opakovaní procesu rozmazania (argument opakovanie). Čím
		 * vyššie sú hodnoty opakovania a rozsahu, tým vyššie sú nároky
		 * metódy na výpočtový výkon.</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania; reálny efekt dosiahneme len
		 *     v rozsahu hodnôt 1 – 5, vyššie čísla sú zaokrúhlené na 5,
		 *     nižšie nespôsobia žiadne rozmazanie
		 * @param pozadie farba použitá pri procese rozmazania pre
		 *     priehľadné body
		 */
		public void rozmaž(int opakovanie, int rozsah, Farebnosť pozadie)
		{
			grafika.translate(-posunX, -posunY);
			VykonajVObrázku.rozmaž(this, grafika, opakovanie,
				rozsah, pozadie.farba());
			grafika.translate(posunX, posunY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, Farebnosť) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, Farebnosť pozadie)
		{ rozmaž(opakovanie, rozsah, pozadie.farba()); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Farebnosť)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , pozadie);}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(int opakovanie, Farebnosť pozadie)
		{ rozmaž(opakovanie, 1, pozadie.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, Farebnosť) rozmaž}.</p> */
		public void rozmaz(int opakovanie, Farebnosť pozadie)
		{ rozmaž(opakovanie, 1, pozadie.farba()); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Farebnosť)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code 
		 * , pozadie);}</p>
		 * 
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(Farebnosť pozadie) { rozmaž(1, 1, pozadie.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(Farebnosť) rozmaž}.</p> */
		public void rozmaz(Farebnosť pozadie) { rozmaž(1, 1, pozadie.farba()); }


		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color) rozmaž}{@code 
		 * (opakovanie, rozsah, }{@link Svet Svet}<code>.</code>{@link 
		 * Svet#farbaPozadia() farbaPozadia}{@code ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania
		 */
		public void rozmaž(int opakovanie, int rozsah)
		{ rozmaž(opakovanie, rozsah, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah)
		{ rozmaž(opakovanie, rozsah, Svet.farbaPozadia); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color) rozmaž}{@code 
		 * (opakovanie, }{@code num1}{@code , }{@link Svet
		 * Svet}<code>.</code>{@link Svet#farbaPozadia()
		 * farbaPozadia}{@code ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 */
		public void rozmaž(int opakovanie)
		{ rozmaž(opakovanie, 1, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int) rozmaž}.</p> */
		public void rozmaz(int opakovanie)
		{ rozmaž(opakovanie, 1, Svet.farbaPozadia); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color) rozmaž}{@code 
		 * (}{@code num1}{@code , }{@code num1}{@code , }{@link Svet 
		 * Svet}<code>.</code>{@link Svet#farbaPozadia()
		 * farbaPozadia}{@code ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 */
		public void rozmaž() { rozmaž(1, 1, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž() rozmaž}.</p> */
		public void rozmaz() { rozmaž(1, 1, Svet.farbaPozadia); }


		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, rozsah, }{@code kwdnew}{@code  }{@link 
		 * Farba Farba}{@code (bgr, bgg, bgb));}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int opakovanie, int rozsah,
			int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, rozsah, new Farba(bgr, bgg, bgb)); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int, int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, rozsah, new Farba(bgr, bgg, bgb)); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , }{@code 
		 * kwdnew}{@code  }{@link Farba Farba}{@code (bgr, bgg, bgb));}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int opakovanie, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, 1, new Farba(bgr, bgg, bgb)); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, 1, new Farba(bgr, bgg, bgb)); }

		/**
		 * <p>Rozmaže grafiku obrázka. Dosiahneme rovnaký efekt, ako keby sme
		 * volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code , }{@code 
		 * kwdnew}{@code  }{@link Farba Farba}{@code (bgr, bgg, bgb));}</p>
		 * 
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int bgr, int bgg, int bgb)
		{ rozmaž(1, 1, new Farba(bgr, bgg, bgb)); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int) rozmaž}.</p> */
		public void rozmaz(int bgr, int bgg, int bgb)
		{ rozmaž(1, 1, new Farba(bgr, bgg, bgb)); }


		/**
		 * <p>Upraví parametre jasu a kontrastu celého obrázka. Oba parametre
		 * môžu nadobúdať kladné aj záporné hodnoty. Na obrázkoch nižšie je
		 * vidno, ako sa zmení obrázok pri rôznych hodnotách týchto
		 * parametrov.</p>
		 * 
		 * <p><image>trees+sky-200.jpeg<alt/>Zdrojový obrázok na ukážku
		 * úpravy jasu a kontrastu.</image>Zdrojový obrázok na ukážku úpravy
		 * jasu a kontrastu<br /><small>(verzia v mierne vyššom rozlíšení
		 * je dostupná na prevzatie <a href="resources/trees+sky-800.jpeg"
		 * target="_blank">tu</a>)</small>.</p>
		 * 
		 * <p> </p>
		 * 
		 * <table class="tightTable centered">
		 * <tr><td> </td><td style="width: 80px; text-align: center"
		 * >-240,0</td><td style="width: 80px; text-align: center"
		 * >-160,0</td><td style="width: 80px; text-align: center"
		 * >-80,0</td><td style="width: 80px; text-align: center"
		 * >0,0</td><td style="width: 80px; text-align: center"
		 * >80,0</td><td style="width: 80px; text-align: center"
		 * >160,0</td><td style="width: 80px; text-align: center"
		 * >240,0</td><td> </td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-240,0</td><td colspan="7" rowspan="7" style="width:
		 * 560px; height: 560px"><image>bctable-trees+sky.jpeg<alt/>Ukážka
		 * rôznych úrovní jasu a kontrastu.</image></td><td rowspan="7"
		 * style="text-align: left"><svg width="25px" height="200px"
		 * style="background: none"><g><text transform="rotate(-90)
		 * translate(-180, 15)">rôzne úrovne kontrastu</text></g></svg></td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-160,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-80,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">0,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">80,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">160,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">240,0</td></tr>
		 * <tr><td> </td><td colspan="7" style="text-align: center; width:
		 * 540px">rôzne úrovne jasu</td><td> </td></tr>
		 * </table>
		 * 
		 * <p class="image">Ukážka rôznych úrovní jasu a kontrastu.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p><image>trunk-200.jpeg<alt/>Zdrojový obrázok na ukážku úpravy
		 * jasu a kontrastu.</image>Zdrojový obrázok na ukážku úpravy jasu
		 * a kontrastu<br /><small>(verzia v mierne vyššom rozlíšení je
		 * dostupná na prevzatie <a href="resources/trunk-800.jpeg"
		 * target="_blank">tu</a>)</small>.</p>
		 * 
		 * <p> </p>
		 * 
		 * <table class="tightTable centered">
		 * <tr><td> </td><td style="width: 80px; text-align: center"
		 * >-240,0</td><td style="width: 80px; text-align: center"
		 * >-160,0</td><td style="width: 80px; text-align: center"
		 * >-80,0</td><td style="width: 80px; text-align: center"
		 * >0,0</td><td style="width: 80px; text-align: center"
		 * >80,0</td><td style="width: 80px; text-align: center"
		 * >160,0</td><td style="width: 80px; text-align: center"
		 * >240,0</td><td> </td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-240,0</td><td colspan="7" rowspan="7" style="width: 560px;
		 * height: 560px"><image>bctable-trunk.jpeg<alt/>Ukážka rôznych
		 * úrovní jasu a kontrastu.</image></td><td rowspan="7"
		 * style="text-align: left"><svg width="25px" height="200px"
		 * style="background: none"><g><text transform="rotate(-90)
		 * translate(-180, 15)">rôzne úrovne kontrastu</text></g></svg></td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-160,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">-80,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">0,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">80,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">160,0</td></tr>
		 * <tr><td style="height: 80px; text-align: right; padding-right:
		 * 5px">240,0</td></tr>
		 * <tr><td> </td><td colspan="7" style="text-align: center">rôzne
		 * úrovne jasu</td><td> </td></tr>
		 * </table>
		 * 
		 * <p class="image">Ukážka rôznych úrovní jasu a kontrastu.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Úplne priehľadné (neviditeľné)
		 * body nie sú spracované. Tento efekt ukazuje nasledujúci obrázok,
		 * na ktorého vytvorenie bola použitá aj metóda {@link 
		 * #zrušPriehľadnosť() zrušPriehľadnosť}.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>ciarky-s-dierami.png<alt/></image></td>
		 * <td><image>ciarky-bez-dier.png<alt/></image></td>
		 * <td><image>ciarky-bez-dier-zvyraznene.png<alt/></image></td></tr>
		 * <tr><td style="text-align: center">a)</td><td style="text-align: center">b)</td><td style="text-align: center">c)</td></tr></table>
		 * 
		 * <p class="image">Ukážka vplyvu metódy {@link 
		 * #upravJasKontrast(double, double) upravJasKontrast} na priehľadné
		 * časti obrázka:<br />a) vygenerovaný obrázok s priehľadnými
		 * časťami,<br />b) obrázok po volaní metód {@link 
		 * #upravJasKontrast(double, double) upravJasKontrast(-240, 160)}
		 * a {@link #zrušPriehľadnosť() zrušPriehľadnosť()},<br />c)
		 * zvýraznenie pôvodne priehľadných častí, na ktoré nemalo volanie
		 * metódy {@link #upravJasKontrast(double, double) upravJasKontrast}
		 * žiadny vplyv.</p>
		 * 
		 * <p><b>Použité zdroje:</b></p>
		 * 
		 * <ul>
		 * <li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-4-brightness-adjustment/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 4: Brightness Adjustment.</em>
		 * Dreamland Fantasy Studios, 2008, 2010. Citované: 2018.</a></li>
		 * <li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-4-brightness-adjustment/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 4: Brightness Adjustment.</em>
		 * Dreamland Fantasy Studios, 2008, 2010. Citované: 2018.</a></li>
		 * </ul>
		 * 
		 * @param jas reálnočíselná hodnota určujúca mieru zmeny jasu obrázka;
		 *     relevantné sú hodnoty v rozmedzí od −255.0 do 255.0
		 * @param kontrast reálnočíselná hodnota určujúca mieru zmeny kontrastu
		 *     obrázka; relevantné sú hodnoty v rozmedzí od −255.0 do 255.0
		 */
		public void upravJasKontrast(double jas, double kontrast)
		{
			if (0.0 == jas && 0.0 == kontrast) return;

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			if (0.0 != jas && 0.0 != kontrast)
			{
				double faktorKontrastu = (259.0 * (kontrast + 255.0)) /
					(255.0 * (259.0 - kontrast));

				for (int i = 0; i < údajeObrázka.length; ++i)
				{
					// Mení jas a kontrast len tých bodov, ktoré nemajú
					// nulový alfa kanál (sú aspoň trochu viditeľné)
					if (0 != (údajeObrázka[i] & 0xff000000))
					{
						double r = faktorKontrastu * (((údajeObrázka[i] >>
							16) & 0xff) - 128.0) + 128.0 + jas;
						double g = faktorKontrastu * (((údajeObrázka[i] >>
							8) & 0xff) - 128.0) + 128.0 + jas;
						double b = faktorKontrastu * ((údajeObrázka[i]
							& 0xff) - 128.0) + 128.0 + jas;

						if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
						if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
						if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

						údajeObrázka[i] = (údajeObrázka[i] & 0xff000000) |
							(((int)r) << 16) | (((int)g) << 8) | ((int)b);
					}
				}
			}
			else if (0.0 != kontrast)
			{
				double faktorKontrastu = (259.0 * (kontrast + 255.0)) /
					(255.0 * (259.0 - kontrast));

				for (int i = 0; i < údajeObrázka.length; ++i)
				{
					if (0 != (údajeObrázka[i] & 0xff000000))
					{
						double r = faktorKontrastu * (((údajeObrázka[i] >>
							16) & 0xff) - 128.0) + 128.0;
						double g = faktorKontrastu * (((údajeObrázka[i] >>
							8) & 0xff) - 128.0) + 128.0;
						double b = faktorKontrastu * (( údajeObrázka[i]
							& 0xff) - 128.0) + 128.0;

						if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
						if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
						if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

						údajeObrázka[i] = (údajeObrázka[i] & 0xff000000) |
							(((int)r) << 16) | (((int)g) << 8) | ((int)b);
					}
				}
			}
			else
			{
				for (int i = 0; i < údajeObrázka.length; ++i)
				{
					if (0 != (údajeObrázka[i] & 0xff000000))
					{
						double r = ((údajeObrázka[i] >> 16) & 0xff) + jas;
						double g = ((údajeObrázka[i] >>  8) & 0xff) + jas;
						double b = ( údajeObrázka[i]        & 0xff) + jas;

						if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
						if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
						if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

						údajeObrázka[i] = (údajeObrázka[i] & 0xff000000) |
							(((int)r) << 16) | (((int)g) << 8) | ((int)b);
					}
				}
			}
		}

		/**
		 * <p>Upraví intenzitu farieb obrázka na základe hodnoty parametra
		 * gama (γ). Gama vyjadruje vzťah medzi vstupnou a výstupnou hodnotou
		 * intenzity farby, ktorý je najlepšie viditeľný na nasledujúcom
		 * grafe:</p>
		 * 
		 * <p><image>gama-korekcie.svg<alt/>Graf zmeny vstupnej veličiny na
		 * výstupnú pri rôznych hodnotách
		 * γ.<onerror>gama-korekcie.png</onerror></image>Graf vyjadrujúci
		 * zmenu vstupnej veličiny na výstupnú pri rôznych hodnotách
		 * faktora gama (γ).</p>
		 * 
		 * <p> </p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Algoritmus môže principiálne
		 * prijímať akúkoľvek hodnotu γ, okrem nuly, ale tento prípad je
		 * v metóde ošetrený zadaním paušálnej hodnoty korekcie 65 025,
		 * takže aj v tom prípade produkuje metóda relatívne korektný
		 * výsledok. Záporné hodnoty sú tiež mimo rozsahu korektných hodnôt,
		 * ale metóda ich nefiltruje, pretože zadaním zápornej hodnoty sa
		 * dá získať síce nesprávny, ale zaujímavý výsledok. Odporúčaný rozsah
		 * hodnôt sa nachádza v rozmedzí 0,01 – 7,99.</p>
		 * 
		 * <p>Nasledujúci obrázok ukazuje vplyv rôznych úrovní gama korekcie
		 * na dvoch obrázkoch, ktoré sú k dispozícii na prevzatie v opise metódy
		 * {@link #upravJasKontrast(double, double) upravJasKontrast}.</p>
		 * 
		 * <p><image>glines-trunk-trees+sky.jpeg<alt/>Ukážka škály úpravy
		 * dvoch obrázkov gama korekciou.</image>Úprava dvoch obrázkov gama
		 * korekciou s hodnotami (zľava): {@code num0.01}, {@code num0.25},
		 * {@code num0.5}, {@code num1.0}, {@code num2.0}, {@code num4.0}
		 * a {@code num7.99}.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Úplne priehľadné (neviditeľné)
		 * body nie sú spracované. Nasledujúci príklad vygeneruje kresbu
		 * s bublinkami, na ktorej je táto vlastnosť ukázaná.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad vygeneruje obrázok s bublinkami, ktoré sa
		 * vzájomne prekrývajú. Prekrývanie je riešené vymazaním
		 * (spriehľadnením) tej časti kresby, ktorú bude tvoriť nasledujúca
		 * nakreslená bublinka. Následne je na kresbu použitá gama korekcia
		 * a metóda {@link #zrušPriehľadnosť() zrušPriehľadnosť()}, čím sa
		 * ukážu aj pôvodne spriehľadnené časti kresby, na ktoré nemala
		 * gama korekcia vplyv.</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Generovanie náhodných bubliniek.}
			{@link Obrázok Obrázok} bublinky = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num400}, {@code num400});
			{@link Obrázok Obrázok} guma = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num400}, {@code num400});
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num5.5});
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num100}; ++i)
			{
				{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
				{@link GRobot#smer(double) smer}({@code num90});
				{@code typelong} polomer = {@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num30}, {@code num50});

				guma.{@link Obrázok#vyplň(Color) vyplň}({@link Farebnosť#čierna čierna});
				{@link GRobot#farba(Color) farba}({@link Farebnosť#biela biela});
				{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(guma);
				{@link GRobot#kruh(double) kruh}(polomer);
				bublinky.{@link Obrázok#vymažKresbu(BufferedImage) vymažKresbu}(guma);

				{@code kwdswitch} (i % {@code num3})
				{
				{@code kwdcase} {@code num0}: {@link GRobot#farba(Color) farba}({@link Farebnosť#svetlotyrkysová svetlotyrkysová}); {@code kwdbreak};
				{@code kwdcase} {@code num1}: {@link GRobot#farba(Color) farba}({@link Farebnosť#tyrkysová tyrkysová}); {@code kwdbreak};
				{@code kwdcase} {@code num2}: {@link GRobot#farba(Color) farba}({@link Farebnosť#tmavotyrkysová tmavotyrkysová});
				}

				{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(bublinky);
				{@link GRobot#kružnica(double) kružnica}(polomer);
				{@link GRobot#preskočVľavo(double) preskočVľavo}(polomer * {@code num0.7});
				{@link GRobot#choďPoOblúku(double, double) choďPoOblúku}({@code num90}, polomer * {@code num0.7});
			}

			{@code comm// Úprava obrázka s použitím gama korekcie a zrušenie priehľadnosti}
			{@code comm// bodov v obrázku.}
			bublinky.{@code currgamaKorekcia}({@code num0.1});
			bublinky.{@link Obrázok#zrušPriehľadnosť() zrušPriehľadnosť}();
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>bublinky-povodne.png<alt/></image></td>
		 * <td><image>bublinky-po-upravach.png<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * gama korekciou (γ = 0,1) s odhalením priehľadných častí kresby
		 * metódou {@link #zrušPriehľadnosť() zrušPriehľadnosť()}.</p>
		 * 
		 * <p><b>Použitý zdroj:</b></p>
		 * 
		 * <ul><li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-6-gamma-correction/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 6: Gamma Correction.</em> Dreamland
		 * Fantasy Studios, 2008, 2010. Citované: 2018.</a></li></ul>
		 * 
		 * @param γ hodnota, ktorá určí mieru korekcie intenzity farieb obrázka;
		 *     vhodný rozsah hodnôt je zhruba v rozmedzí 0,01 – 7,99
		 */
		public void gamaKorekcia(double γ)
		{
			if (1.0 == γ) return;

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			double γC = (0.0 == γ) ? 65025.0 : (1.0 / γ);

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				if (0 != (údajeObrázka[i] & 0xff000000))
				{
					double r = 255.0 * Math.pow(((údajeObrázka[i]
						>> 16) & 0xff) / 255.0, γC);
					double g = 255.0 * Math.pow(((údajeObrázka[i]
						>>  8) & 0xff) / 255.0, γC);
					double b = 255.0 * Math.pow((údajeObrázka[i]
						& 0xff) / 255.0, γC);

					if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
					if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
					if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

					údajeObrázka[i] = (údajeObrázka[i] & 0xff000000) |
						(((int)r) << 16) | (((int)g) << 8) | ((int)b);
				}
			}
		}

		/**
		 * <p>Posterizuje obrázok podľa predvolenej palety {@linkplain 
		 * Farebnosť#preddefinovanéFarby preddefinovaných farieb}. Pod
		 * posterizáciou sa rozumie nastavenie všetkých farieb na najbližšiu
		 * farbu v palete.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj--.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * @see #posterizuj(Color, Color...)
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean)
		 * @see #posterizuj(boolean, Color, Color...)
		 * @see #posterizuj(boolean, Color[])
		 */
		public void posterizuj()
		{ posterizuj(preddefinovanéFarby); }

		/**
		 * <p>Posterizuje obrázok podľa palety zadanej vo forme zoznamu
		 * farieb, pričom povinné je zadanie aspoň jednej farby. Pod
		 * posterizáciou sa rozumie nastavenie všetkých farieb na najbližšiu
		 * farbu v palete. Ak je zadaná len jedna farba palety, tak ňou bude
		 * obrázok jednoducho vyplnený.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj-RGB-etc-.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou s parametrami: {@link Farebnosť#červená červená},
		 * {@link Farebnosť#zelená zelená}, {@link Farebnosť#modrá modrá},
		 * {@link Farebnosť#čierna čierna}, {@link Farebnosť#biela biela},
		 * {@link Farebnosť#šedá šedá}, {@link Farebnosť#žltá žltá}, {@link 
		 * Farebnosť#tyrkysová tyrkysová}, {@link Farebnosť#purpurová
		 * purpurová}.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * @param prváFarba prvá farba palety na posterizáciu
		 * @param ostatnéFarby zoznam ostatných farieb palety na posterizáciu
		 * 
		 * @see #posterizuj()
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean)
		 * @see #posterizuj(boolean, Color, Color...)
		 * @see #posterizuj(boolean, Color[])
		 */
		public void posterizuj(Color prváFarba, Color... ostatnéFarby)
		{
			if (null == prváFarba && null == ostatnéFarby) return;
			if (null == prváFarba) posterizuj(ostatnéFarby);
			else if (null == ostatnéFarby || 0 == ostatnéFarby.length)
				posterizuj(new Color[] {prváFarba});
			else
			{
				Color[] paleta = new Color[1 + ostatnéFarby.length];
				paleta[0] = prváFarba;
				System.arraycopy(ostatnéFarby, 0,
					paleta, 1, ostatnéFarby.length);
				posterizuj(paleta);
			}
		}

		/**
		 * <p>Posterizuje obrázok podľa palety zadanej vo forme poľa farieb.
		 * Pod posterizáciou sa rozumie nastavenie všetkých farieb na
		 * najbližšiu farbu v palete. Ak paleta obsahuje len jednu farbu,
		 * tak ňou bude obrázok jednoducho vyplnený.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj-pal32-.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou s parametrom:
		 * <em>«inštancia»</em><code>.</code>{@link Obrázok#paleta(int)
		 * paleta}({@code num32}).</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * <p><b>Použitý zdroj:</b></p>
		 * 
		 * <ul><li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-1-finding-nearest-colour/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 1: Finding The Nearest Colour.</em>
		 * Dreamland Fantasy Studios, 2008, 2010. Citované: 2018.</a></li></ul>
		 * 
		 * @param paleta paleta na posterizáciu
		 * 
		 * @see #posterizuj()
		 * @see #posterizuj(Color, Color...)
		 * @see #posterizuj(boolean)
		 * @see #posterizuj(boolean, Color, Color...)
		 * @see #posterizuj(boolean, Color[])
		 * @see #početFarieb()
		 * @see #paleta(int)
		 */
		public void posterizuj(Color[] paleta)
		{
			if (null == paleta || 0 == paleta.length) return;

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			if (1 == paleta.length)
			{
				if (255 != paleta[0].getAlpha())
				{
					vyplň(paleta[0].getRed(),
						paleta[0].getGreen(),
						paleta[0].getBlue());
				}
				else vyplň(paleta[0]);
			}
			else for (int i = 0; i < údajeObrázka.length; ++i)
			{
				// if (0 != (údajeObrázka[i] & 0xff000000))
				{
					Color výslednáFarba = paleta[0];

					int r = ((údajeObrázka[i] >> 16) & 0xff);
					int g = ((údajeObrázka[i] >>  8) & 0xff);
					int b = ( údajeObrázka[i]        & 0xff);

					int Δr = r - paleta[0].getRed();
					int Δg = g - paleta[0].getGreen();
					int Δb = b - paleta[0].getBlue();

					int minimum = Δr * Δr + Δg * Δg + Δb * Δb;

					for (int j = 1; j < paleta.length; ++j)
					{
						Color farba = paleta[j];

						Δr = r - farba.getRed();
						Δg = g - farba.getGreen();
						Δb = b - farba.getBlue();

						int vzdialenosť = Δr * Δr + Δg * Δg + Δb * Δb;

						if (vzdialenosť < minimum)
						{
							minimum = vzdialenosť;
							výslednáFarba = farba;
						}
					}

					údajeObrázka[i] = výslednáFarba.getRGB() | 0xff000000;
				}
			}
		}

		/**
		 * <p>Posterizuje obrázok podľa predvolenej palety {@linkplain 
		 * Farebnosť#preddefinovanéFarby preddefinovaných farieb} a umožňuje
		 * zvoliť, či má byť pri tomto procese použitý algoritmus difúzie
		 * chyby. Pre posterizáciu platí to isté, čo je uvedené v opise
		 * metódy {@link #posterizuj() posterizuj()}. Algoritmus difúzie chyby
		 * prenáša časť chyby zaokrúhlenia farieb na susedné pixely, čo vo
		 * výsledku vyvolá optický klam – dojem, že posterizovaný obrázok má
		 * v skutočnosti viac farieb, než obsahovala paleta použitá pri
		 * posterizácii (čo nie je pravda).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj-T-.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou s parametrom: {@code valtrue}.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * @param difúziaChyby pravdivostná hodnota určujúca, či má byť pri
		 *     posterizácii použitý algoritmus difúzie chyby
		 * 
		 * @see #posterizuj()
		 * @see #posterizuj(Color, Color...)
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean, Color, Color...)
		 * @see #posterizuj(boolean, Color[])
		 */
		public void posterizuj(boolean difúziaChyby)
		{ posterizuj(difúziaChyby, preddefinovanéFarby); }

		/**
		 * <p>Posterizuje obrázok podľa palety zadanej vo forme zoznamu
		 * farieb, pričom povinné je zadanie aspoň jednej farby a umožňuje
		 * zvoliť, či má byť pri tomto procese použitý algoritmus difúzie
		 * chyby. Pre posterizáciu platí to isté, čo je uvedené v opise
		 * metódy {@link #posterizuj(Color, Color...) posterizuj(prváFarba,
		 * ostatnéFarby)}. Algoritmus difúzie chyby prenáša časť chyby
		 * zaokrúhlenia farieb na susedné pixely, čo vo výsledku vyvolá
		 * optický klam – dojem, že posterizovaný obrázok má v skutočnosti
		 * viac farieb, než obsahovala zadaná paleta (čo nie je pravda).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj-T-RGB-etc-.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou s parametrami: {@code valtrue}, {@link 
		 * Farebnosť#červená červená}, {@link Farebnosť#zelená zelená},
		 * {@link Farebnosť#modrá modrá}, {@link Farebnosť#čierna čierna},
		 * {@link Farebnosť#biela biela}, {@link Farebnosť#šedá šedá},
		 * {@link Farebnosť#žltá žltá}, {@link Farebnosť#tyrkysová
		 * tyrkysová}, {@link Farebnosť#purpurová purpurová}.</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * @param difúziaChyby pravdivostná hodnota určujúca, či má byť pri
		 *     posterizácii použitý algoritmus difúzie chyby
		 * @param prváFarba prvá farba palety na posterizáciu
		 * @param ostatnéFarby zoznam ostatných farieb palety na posterizáciu
		 * 
		 * @see #posterizuj()
		 * @see #posterizuj(Color, Color...)
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean)
		 * @see #posterizuj(boolean, Color[])
		 */
		public void posterizuj(boolean difúziaChyby,
			Color prváFarba, Color... ostatnéFarby)
		{
			if (null == prváFarba && null == ostatnéFarby) return;
			if (null == prváFarba) posterizuj(difúziaChyby, ostatnéFarby);
			else if (null == ostatnéFarby || 0 == ostatnéFarby.length)
				posterizuj(difúziaChyby, new Color[] {prváFarba});
			else
			{
				Color[] paleta = new Color[1 + ostatnéFarby.length];
				paleta[0] = prváFarba;
				System.arraycopy(ostatnéFarby, 0,
					paleta, 1, ostatnéFarby.length);
				posterizuj(difúziaChyby, paleta);
			}
		}

		// Súkromná metóda implementujúca časť algoritmu difúzie chyby
		private static void difúziaChyby(int[] údajeObrázka,
			int poloha, int chybaR, int chybaG, int chybaB)
		{
			if (poloha >= 0 && poloha < údajeObrázka.length)
			{
				int r = ((údajeObrázka[poloha] >> 16) & 0xff) + chybaR;
				int g = ((údajeObrázka[poloha] >>  8) & 0xff) + chybaG;
				int b = ( údajeObrázka[poloha]        & 0xff) + chybaB;

				if (r > 255) r = 255; else if (r < 0) r = 0;
				if (g > 255) g = 255; else if (g < 0) g = 0;
				if (b > 255) b = 255; else if (b < 0) b = 0;

				údajeObrázka[poloha] = (údajeObrázka[poloha] &
					0xff000000) | (r << 16) | (g << 8) | b;
			}
		}

		/**
		 * <p>Posterizuje obrázok podľa palety zadanej vo forme poľa farieb
		 * a umožňuje zvoliť, či má byť pri tomto procese použitý algoritmus
		 * difúzie chyby. Pre posterizáciu platí to isté, čo je uvedené v opise
		 * metódy {@link #posterizuj(Color[]) posterizuj(paleta)}. Algoritmus
		 * difúzie chyby prenáša časť chyby zaokrúhlenia farieb na susedné
		 * pixely, čo vo výsledku vyvolá optický klam – dojem, že posterizovaný
		 * obrázok má v skutočnosti viac farieb, než obsahovala zadaná paleta
		 * (čo nie je pravda).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda zruší priehľadnosť
		 * bodov pôvodného obrázka. Ak ju chcete zachovať, musíte
		 * {@linkplain #vyrobMasku() vytvoriť} a {@linkplain 
		 * #použiMasku(BufferedImage) použiť} masku.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>posterizuj.jpeg<alt/></image></td>
		 * <td><image>posterizuj-T-pal32-.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Vľavo je verzia obrázka pred a vpravo po úprave
		 * touto metódou s parametrami: {@code valtrue},
		 * <em>«inštancia»</em><code>.</code>{@link 
		 * Obrázok#paleta(int) paleta}({@code num32}).</p>
		 * 
		 * <p>Porovnanie rôznych efektov použitých na rovnaký obrázok je
		 * aj v opise metódy {@link #farebnéTienidlo(Color)
		 * farebnéTienidlo(farba)}.</p>
		 * 
		 * <p><b>Použité zdroje:</b></p>
		 * 
		 * <ul>
		 * <li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-1-finding-nearest-colour/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 1: Finding The Nearest Colour.</em>
		 * Dreamland Fantasy Studios, 2008, 2010. Citované: 2018.</a></li>
		 * <li><a
		 * href="https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-2-error-diffusion/"
		 * target="_blank"><small>Francis G. Loch</small>: <em>Image
		 * Processing Algorithms Part 2: Error Diffusion.</em> Dreamland
		 * Fantasy Studios, 2008, 2010. Citované: 2018.</a></li>
		 * </ul>
		 * 
		 * @param difúziaChyby pravdivostná hodnota určujúca, či má byť pri
		 *     posterizácii použitý algoritmus difúzie chyby
		 * @param paleta paleta na posterizáciu
		 * 
		 * @see #posterizuj()
		 * @see #posterizuj(Color, Color...)
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean)
		 * @see #posterizuj(boolean, Color, Color...)
		 * @see #početFarieb()
		 * @see #paleta(int)
		 */
		public void posterizuj(boolean difúziaChyby, Color[] paleta)
		{
			if (null == paleta || 0 == paleta.length) return;

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			if (!difúziaChyby)
			{
				posterizuj(paleta);
				return;
			}

			if (1 == paleta.length)
			{
				if (255 != paleta[0].getAlpha())
				{
					vyplň(paleta[0].getRed(),
						paleta[0].getGreen(),
						paleta[0].getBlue());
				}
				else vyplň(paleta[0]);
			}
			else for (int i = 0; i < údajeObrázka.length; ++i)
			{
				// if (0 != (údajeObrázka[i] & 0xff000000))
				{
					Color výslednáFarba = paleta[0];

					int r = ((údajeObrázka[i] >> 16) & 0xff);
					int g = ((údajeObrázka[i] >>  8) & 0xff);
					int b = ( údajeObrázka[i]        & 0xff);

					int Δr = r - paleta[0].getRed();
					int Δg = g - paleta[0].getGreen();
					int Δb = b - paleta[0].getBlue();

					int minimum = Δr * Δr + Δg * Δg + Δb * Δb;

					for (int j = 1; j < paleta.length; ++j)
					{
						Color farba = paleta[j];

						Δr = r - farba.getRed();
						Δg = g - farba.getGreen();
						Δb = b - farba.getBlue();

						int vzdialenosť = Δr * Δr + Δg * Δg + Δb * Δb;

						if (vzdialenosť < minimum)
						{
							minimum = vzdialenosť;
							výslednáFarba = farba;
						}
					}

					int chybaR = r - výslednáFarba.getRed();
					int chybaG = g - výslednáFarba.getGreen();
					int chybaB = b - výslednáFarba.getBlue();

					difúziaChyby(údajeObrázka, i + 1,
						chybaR * 7 / 16, chybaG * 7 / 16, chybaB * 7 / 16);
					difúziaChyby(údajeObrázka, i + šírka - 1,
						chybaR * 3 / 16, chybaG * 3 / 16, chybaB * 3 / 16);
					difúziaChyby(údajeObrázka, i + šírka,
						chybaR * 5 / 16, chybaG * 5 / 16, chybaB * 5 / 16);
					difúziaChyby(údajeObrázka, i + šírka + 1,
						chybaR / 16, chybaG / 16, chybaB / 16);

					údajeObrázka[i] = výslednáFarba.getRGB() | 0xff000000;
				}
			}
		}

		/**
		 * <p>Zistí počet farieb použitých v aktuálnej snímke obrázka.
		 * Metóda neberie do úvahy priehľadnosť farebných bodov, počíta
		 * farby len na základe ich farebných zložiek.</p>
		 * 
		 * @return počet farieb v obrázku (bez ohľadu na ich priehľadnosť)
		 * 
		 * @see #paleta(int)
		 * @see #paleta(int, int)
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean, Color[])
		 */
		public int početFarieb()
		{
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			TreeSet<Integer> počet = new TreeSet<>();

			for (int i = 0; i < údajeObrázka.length; ++i)
				počet.add(údajeObrázka[i] | 0xff000000);

			return počet.size();
		}

		/** <p><a class="alias"></a> Alias pre {@link #početFarieb() početFarieb}.</p> */
		public int pocetFarieb() { return početFarieb(); }

		/**
		 * <p>Vyrobí paletu najpoužívanejších farieb vyskytujúcich sa
		 * v aktuálnej snímke obrázka so zadaným maximálnym počtom farieb.
		 * Metóda funguje rovnako ako metóda {@link #paleta(int, int)
		 * paleta(početFarieb, hranicaZdravia)} s predvolenou hranicou
		 * zdravia 256. (Ďalšie podrobnosti pozri v opise uvedenej
		 * metódy.)</p>
		 * 
		 * @param početFarieb maximálny počet farieb, ktoré smie paleta
		 *     obsahovať
		 * @return paleta farieb obsahujúca zadaný počet farieb
		 * 
		 * @see #paleta(int, int)
		 * @see #početFarieb()
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean, Color[])
		 */
		public Farba[] paleta(int početFarieb)
		{ return paleta(početFarieb, 256); }

		// Trieda nevyhnutná na fungovanie metódy paleta(početFarieb,
		// hranicaZdravia)
		private class PoložkaPalety implements Comparable<PoložkaPalety>
		{
			public final int farba;
			public final int počet;

			public PoložkaPalety(int farba, int počet)
			{
				this.farba = farba;
				this.počet = počet;
			}

			public int compareTo(PoložkaPalety položka)
			{
				// if (položka.počet == počet) return 1;
				return počet - položka.počet;
			}
		}

		/**
		 * <p>Vyrobí paletu najpoužívanejších farieb vyskytujúcich sa
		 * v aktuálnej snímke obrázka so zadaným maximálnym počtom farieb
		 * a hranicou zdravia 256. Metóda zaraďuje farby do palety podľa
		 * frekvencie ich výskytu (od najpoužívanejších po najmenej
		 * používané). Keď počet farieb, ktoré boli zaradené do palety
		 * presiahne zadaný maximálny počet farieb, tak sa metóda pokúsi
		 * vypočítať novú farbu ako priemer najbližšej farby a nasledujúcej
		 * zadanej farby. Hranica zdravia určuje povolenú vzdialenosť medzi
		 * ďalšou pridávanou položkou a jestvujúcou položkou palety (0
		 * znamená, že do palety budú zaradené len prvé najpoužívanejšie
		 * položky). Metóda neberie do úvahy priehľadnosť farebných bodov,
		 * spracuje všetky body obrázka len na základe ich farebných zložiek
		 * a rovnako výsledná paleta bude zložená len z nepriehľadných
		 * farieb.</p>
		 * 
		 * @param početFarieb maximálny počet farieb, ktoré smie paleta
		 *     obsahovať
		 * @param hranicaZdravia maximálny vzdialenosť dvoch farieb
		 *     zaraďovaných do palety ako ich priemer
		 * @return paleta farieb obsahujúca zadaný počet farieb
		 * 
		 * @see #paleta(int)
		 * @see #početFarieb()
		 * @see #posterizuj(Color[])
		 * @see #posterizuj(boolean, Color[])
		 */
		public Farba[] paleta(int početFarieb, int hranicaZdravia)
		{
			// Poznámka: Táto verzia algoritmu je lepšia a rýchlejšia ako
			// predchádzajúca – v komentároch nižšie, ale dala by sa zlepšiť.
			// Na to, aby bola paleta optimalizovaná, by bolo treba, aby
			// vyberala farby z určitých „pásiem.“ (Našťastie sa ani
			// nepokúšame tvrdiť, že paleta je optimalizovaná.)

			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			TreeMap<Integer, Integer> všetkyFarby = new TreeMap<>();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				Integer farba = údajeObrázka[i] | 0xff000000;
				Integer počet = všetkyFarby.get(farba);
				if (null == počet) počet = 1; else ++počet;
				všetkyFarby.put(farba, počet);
			}

			ArrayList<PoložkaPalety> zoradenéFarby =
				new ArrayList<>(všetkyFarby.size());

			for (Map.Entry<Integer, Integer> položka :
				všetkyFarby.entrySet()) zoradenéFarby.add(
					new PoložkaPalety(položka.getKey(),
						položka.getValue()));
			Collections.sort(zoradenéFarby);

			TreeSet<Integer> paleta = new TreeSet<>();

			for (PoložkaPalety ďalšiaFarba : zoradenéFarby)
			{
				if (paleta.size() < početFarieb)
					paleta.add(ďalšiaFarba.farba);
				else
				{
					int farba = ďalšiaFarba.farba;
					int najbližšiaFarba = paleta.first();
					int r1, g1, b1;

					int r2 = r1 = ((farba >> 16) & 0xff);
					int g2 = g1 = ((farba >>  8) & 0xff);
					int b2 = b1 = ( farba        & 0xff);

					int Δr = r1 - ((najbližšiaFarba >> 16) & 0xff);
					int Δg = g1 - ((najbližšiaFarba >>  8) & 0xff);
					int Δb = b1 - ( najbližšiaFarba        & 0xff);

					int minimum = Δr * Δr + Δg * Δg + Δb * Δb;

					for (int položka : paleta)
					{
						Δr = r1 - ((položka >> 16) & 0xff);
						Δg = g1 - ((položka >>  8) & 0xff);
						Δb = b1 - ( položka        & 0xff);

						int vzdialenosť = Δr * Δr + Δg * Δg + Δb * Δb;

						if (vzdialenosť < minimum)
						{
							minimum = vzdialenosť;
							najbližšiaFarba = položka;
							r2 = r1; g2 = g1; b2 = b1;
						}
					}

					// Ak minimum neklesne pod určitú hodnotu, tak novú
					// farbu zahodím…
					if (minimum <= hranicaZdravia)
					{
						r2 += r1; r2 >>= 1;
						g2 += g1; g2 >>= 1;
						b2 += b1; b2 >>= 1;

						farba = (0xff000000 | (r2 << 16) | (g2 << 8) | b2);
						paleta.remove(najbližšiaFarba);
						paleta.add(farba);
					}
				}
			}

			Farba[] farby = new Farba[paleta.size()];
			int i = 0; for (int položka : paleta)
				farby[i++] = new Farba(položka);
			return farby;

			/*
			// STARÁ VERZIA (nefungovala celkom správne):
			if (null == údajeObrázka) údajeObrázka =
				((DataBufferInt)getRaster().getDataBuffer()).getData();

			TreeSet<Integer> všetkyFarby = new TreeSet<>();

			for (int i = 0; i < údajeObrázka.length; ++i)
				všetkyFarby.add(údajeObrázka[i] | 0xff000000);

			TreeSet<Integer> paleta = new TreeSet<>();

			// ✓ Určite zlepšiť algoritmus, keď bude na to najbližšie čas.
			// (Možno implementovať počítadlo výskytov farieb, zoradiť ich
			// a potom pokračovať ako doteraz…)
			// Pozri nájdené zdroje:
			// https://stackoverflow.com/questions/3849115/image-palette-reduction
			// https://www.google.sk/search?q=algorithm+to+reduce+image+colors+(Java)&oq=algorithm+to+reduce+image+colors+(Java)&aqs=chrome..69i57.10612j0j1&sourceid=chrome&ie=UTF-8
			// https://www.olympus-lifescience.com/en/microscope-resource/primer/java/digitalimaging/processing/colorreduction/
			// https://stackoverflow.com/questions/29244307/effective-gif-image-color-quantization
			// https://stackoverflow.com/questions/5906693/how-to-reduce-the-number-of-colors-in-an-image-with-opencv
			// https://stackoverflow.com/questions/87062/color-reduction-in-java

			for (int farba : všetkyFarby)
			{
				if (!paleta.contains(farba))
				{
					// System.out.println("Nová farba: " + farba);

					if (paleta.size() >= početFarieb)
					{
						// System.out.println("Musím nájsť najbližšiu.");
						int najbližšiaFarba = paleta.first();
						int r1, g1, b1;

						int r2 = r1 = ((farba >> 16) & 0xff);
						int g2 = g1 = ((farba >>  8) & 0xff);
						int b2 = b1 = ( farba        & 0xff);

						int Δr = r1 - ((najbližšiaFarba >> 16) & 0xff);
						int Δg = g1 - ((najbližšiaFarba >>  8) & 0xff);
						int Δb = b1 - ( najbližšiaFarba        & 0xff);

						int minimum = Δr * Δr + Δg * Δg + Δb * Δb;

						for (int položka : paleta)
						{
							Δr = r1 - ((položka >> 16) & 0xff);
							Δg = g1 - ((položka >>  8) & 0xff);
							Δb = b1 - ( položka        & 0xff);

							int vzdialenosť = Δr * Δr + Δg * Δg + Δb * Δb;

							if (vzdialenosť < minimum)
							{
								minimum = vzdialenosť;
								najbližšiaFarba = položka;
								r2 = r1; g2 = g1; b2 = b1;
							}
						}

						r2 += r1; r2 >>= 1;
						g2 += g1; g2 >>= 1;
						b2 += b1; b2 >>= 1;

						farba = (0xff000000 | (r2 << 16) | (g2 << 8) | b2);
						paleta.remove(najbližšiaFarba);

						// System.out.println("Najbližšia farba: " +
						// 	najbližšiaFarba);
						// System.out.println("Prepočítaná farba: " + farba);
					}

					paleta.add(farba);
				}
			}

			Farba[] farby = new Farba[paleta.size()];

			int i = 0;
			for (int položka : paleta)
				farby[i++] = new Farba(položka);

			return farby;*/
		}

		/**
		 * <p>Táto metóda použije na tento obrázok zadanú svetelnú masku.
		 * Táto operácia nahradí pôvodný obsah obrázka. Správanie tejto
		 * metódy je podobné ako správanie metódy {@link #svetlo(Obrázok,
		 * Obrázok)} (pozrite si aj jej opis).</p>
		 * 
		 * @param osvetlenie iný obrázok, ktorého obsah bude použitý
		 *     na svetelnú úpravu bodov tohto obrázka
		 * 
		 * @see #svetlo(Obrázok, Obrázok)
		 */
		public void svetlo(Obrázok osvetlenie)
		{
			if (šírka != osvetlenie.šírka || výška != osvetlenie.výška)
				throw new GRobotException(
					"Rozmery obrázokov svetelnej operácie sa nezhodujú.",
						"imageSizeMismatch");

			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (null == osvetlenie.údajeObrázka)
				osvetlenie.údajeObrázka = ((DataBufferInt)osvetlenie.getRaster().
					getDataBuffer()).getData();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				int a = údajeObrázka[i] & 0xff000000;
				int r = ((údajeObrázka[i] >> 16) & 0xff) *
					((osvetlenie.údajeObrázka[i] >> 16) & 0xff) / 0x80;
				int g = ((údajeObrázka[i] >>  8) & 0xff) *
					((osvetlenie.údajeObrázka[i] >>  8) & 0xff) / 0x80;
				int b = ( údajeObrázka[i]        & 0xff) *
					( osvetlenie.údajeObrázka[i]        & 0xff) / 0x80;

				if (r > 0xff) r = 0xff;
				if (g > 0xff) g = 0xff;
				if (b > 0xff) b = 0xff;

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeObrázka[i] = a | (r << 16) | (g << 8) | b;
			}
		}

		/**
		 * <p>Táto metóda použije na obrázok zadaný v prvom parametri
		 * ({@code grafika}) svetelnú masku zadanú v druhom parametri
		 * ({@code osvetlenie}) a výsledok zlúči do tenjo ({@code valthis})
		 * inštancie obrázka. Svetelný filter môže obsahovať ľubovoľné
		 * farby. Pravidlom je, že zložka s hodnotou {@code num0x80} je
		 * neutrálna, hodnoty zložiek pod touto hranicou výsledný obrázok
		 * stmavujú a nad touto hranicou zase zosvetľujú.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad prečíta obrázok fotografie, upraví parametre
		 * robota tak, aby mohol rýchlo a ľahko vytvárať svetelnú masku so
		 * zeleným kruhovým osvetlením (ktoré je premiestňované podľa polohy
		 * myši) a masku použije na fotografiu.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} SvetloObrázka {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Čítanie obrázka a vytvorenie jeho kópií}
				{@code comm// (len z dôvodu rýchleho získania obrázkov rovnakých rozmerov)…}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@link Obrázok Obrázok} zdroj = {@link Obrázok Obrázok}.{@link Obrázok#čítaj(String) čítaj}({@code srg"vuje-01.jpeg"});
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@link Obrázok Obrázok} svetlo = {@code kwdnew} {@link Obrázok#Obrázok(java.awt.Image) Obrázok}(zdroj);
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@link Obrázok Obrázok} výsledok = {@code kwdnew} {@link Obrázok#Obrázok(java.awt.Image) Obrázok}(zdroj);

				{@code kwdprivate} SvetloObrázka()
				{
					{@code comm// Úprava rozmerov a polohy okna (aj plátna):}
					{@code valsuper}(zdroj.{@link Obrázok#šírka šírka}, zdroj.{@link Obrázok#výška výška});
					{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();

					{@code comm// Prispôsobenie vlastností robota:}
					{@link GRobot#veľkosť(double) veľkosť}({@code num150});
					{@link GRobot#farba(Color) farba}({@code kwdnew} {@link Farba#Farba(int) Farba}({@code num0x80FF80}));
					{@link GRobot#cieľováFarba(Color) cieľováFarba}({@code kwdnew} {@link Farba#Farba(int) Farba}({@code num0x0080FF80}, {@code valtrue}));
					{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(svetlo);
					{@link GRobot#použiKruhovýNáter() použiKruhovýNáter}();
					{@link GRobot#skry() skry}();

					{@code comm// Prvé prekreslenie:}
					{@link GRobot#pohybMyši() pohybMyši}();
				}

				{@code comm// Keby sme chceli zachovať svetlosť zvyšku obrázka, museli by sme}
				{@code comm// použiť na výplň svetelnej masky túto farbu:}
				{@code comm// ## private final static Farba neutrálna = new Farba(0x808080);}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#pohybMyši() pohybMyši}()
				{
					{@code comm// Vytvorenie svetelnej masky:}
					svetlo.{@link Obrázok#vyplň(Color) vyplň}({@link Farebnosť#tmavošedá tmavošedá});
					{@code comm// ## svetlo.vyplň(neutrálna);}
					{@link GRobot#skočNaMyš() skočNaMyš}();
					{@link GRobot#kruh() kruh}();

					{@code comm// Použitie masky:}
					výsledok.{@link Obrázok#svetlo(Obrázok, Obrázok) svetlo}(zdroj, svetlo);

					{@code comm// Nakreslenie výsledku:}
					{@link Plátno podlaha}.{@link Plátno#obrázok(Image) obrázok}(výsledok);

					{@code comm// Keby sme chceli zobraziť len svetelnú masku:}
					{@code comm// ## podlaha.obrázok(svetlo);}
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code kwdnew} SvetloObrázka();
				}
			}
			</pre>
		 * 
		 * <p>Pôvodné obrázky na prevzatie: <a href="resources/vuje-01.jpeg"
		 * target="_blank">vuje-01.jpeg</a>, <a href="resources/vuje-02.jpeg"
		 * target="_blank">vuje-02.jpeg</a></p>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>vuje-01-small.jpeg<alt/></image></td>
		 * <td><image>vuje-01-svetlo.jpeg<alt/></image></td>
		 * <td><image>vuje-01-vysledok.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Úprava obrázka <a href="resources/vuje-01.jpeg"
		 * target="_blank">vuje-01.jpeg</a> – zľava doprava: pôvodný obrázok,
		 * svetelná maska, výsledok.</p>
		 * 
		 * <table class="centered">
		 * <tr><td><image>vuje-02-small.jpeg<alt/></image></td>
		 * <td><image>vuje-02-svetlo.jpeg<alt/></image></td>
		 * <td><image>vuje-02-vysledok.jpeg<alt/></image></td></tr></table>
		 * 
		 * <p class="image">Úprava obrázka <a href="resources/vuje-02.jpeg"
		 * target="_blank">vuje-02.jpeg</a> – zľava doprava: pôvodný obrázok,
		 * svetelná maska, výsledok.</p>
		 * 
		 * @param grafika iný obrázok, ktorého obsah bude slúžiť ako predloha
		 *     osvetleného výsledku uloženého do tohto ({@code valthis})
		 *     obrázka
		 * @param osvetlenie iný obrázok, ktorého obsah bude použitý
		 *     na svetelnú úpravu bodov výsledku
		 * 
		 * @see #svetlo(Obrázok)
		 */
		public void svetlo(Obrázok grafika, Obrázok osvetlenie)
		{
			if (šírka != grafika.šírka || šírka != osvetlenie.šírka ||
				výška != grafika.výška || výška != osvetlenie.výška)
				throw new GRobotException(
					"Rozmery obrázokov svetelnej operácie sa nezhodujú.",
						"imageSizeMismatch");

			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (null == grafika.údajeObrázka)
				grafika.údajeObrázka = ((DataBufferInt)grafika.getRaster().
					getDataBuffer()).getData();

			if (null == osvetlenie.údajeObrázka)
				osvetlenie.údajeObrázka = ((DataBufferInt)osvetlenie.getRaster().
					getDataBuffer()).getData();

			for (int i = 0; i < údajeObrázka.length; ++i)
			{
				int a = grafika.údajeObrázka[i] & 0xff000000;
				int r = ((grafika.údajeObrázka[i] >> 16) & 0xff) *
					((osvetlenie.údajeObrázka[i] >> 16) & 0xff) / 0x80;
				int g = ((grafika.údajeObrázka[i] >>  8) & 0xff) *
					((osvetlenie.údajeObrázka[i] >>  8) & 0xff) / 0x80;
				int b = ( grafika.údajeObrázka[i]        & 0xff) *
					( osvetlenie.údajeObrázka[i]        & 0xff) / 0x80;

				if (r > 0xff) r = 0xff;
				if (g > 0xff) g = 0xff;
				if (b > 0xff) b = 0xff;

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeObrázka[i] = a | (r << 16) | (g << 8) | b;
			}
		}


	// Zrkadlenie, rolovanie a pretáčanie

		/**
		 * <p>Prevráti obrázok podľa vodorovnej osi – horná časť obrázka sa
		 * ocitne dole a naopak.</p>
		 * 
		 * @see #prevráťVodorovne()
		 */
		public void prevráťZvislo()
		{
			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (null == údajeOperácie)
				údajeOperácie = new int[údajeObrázka.length];

			System.arraycopy(údajeObrázka, 0,
				údajeOperácie, 0, údajeObrázka.length);

			int index1 = 0, index2 = šírka * (výška - 1);

			for (int i = 0; i < výška; ++i)
			{
				for (int j = 0; j < šírka; ++j)
				{
					údajeObrázka[index1 + j] =
						údajeOperácie[index2 + j];
				}

				index1 += šírka;
				index2 -= šírka;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prevráťZvislo() prevráťZvislo}.</p> */
		public void prevratZvislo() { prevráťZvislo(); }

		/**
		 * <p><a class="alias"></a> Prevráti obrázok podľa vodorovnej osi –
		 * horná časť obrázka sa ocitne dole a naopak. Metóda je zároveň
		 * aliasom pre {@link #prevráťZvislo() prevráťZvislo}.</p>
		 * 
		 * @see #prevráťHorizontálne()
		 */
		public void prevráťVertikálne() { prevráťZvislo(); }

		/** <p><a class="alias"></a> Alias pre {@link #prevráťZvislo() prevráťZvislo}.</p> */
		public void prevratVertikalne() { prevráťZvislo(); }

		/**
		 * <p>Prevráti obrázok podľa zvislej osi – pravá časť obrázka
		 * sa ocitne vľavo a naopak.</p>
		 * 
		 * @see #prevráťZvislo()
		 */
		public void prevráťVodorovne()
		{
			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (null == údajeOperácie)
				údajeOperácie = new int[údajeObrázka.length];

			System.arraycopy(údajeObrázka, 0,
				údajeOperácie, 0, údajeObrázka.length);

			int index1 = 0, index2 = šírka - 1;

			for (int i = 0; i < výška; ++i)
			{
				for (int j = 0; j < šírka; ++j)
				{
					údajeObrázka[index1 + j] =
						údajeOperácie[index2 - j];
				}

				index1 += šírka;
				index2 += šírka;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prevráťVodorovne() prevráťVodorovne}.</p> */
		public void prevratVodorovne() { prevráťVodorovne(); }

		/**
		 * <p><a class="alias"></a> Prevráti obrázok podľa zvislej osi –
		 * pravá časť obrázka sa ocitne vľavo a naopak. Metóda je zároveň
		 * aliasom pre {@link #prevráťVodorovne() prevráťVodorovne}.</p>
		 * 
		 * @see #prevráťVertikálne()
		 */
		public void prevráťHorizontálne() { prevráťVodorovne(); }

		/** <p><a class="alias"></a> Alias pre {@link #prevráťVodorovne() prevráťVodorovne}.</p> */
		public void prevratHorizontalne() { prevráťVodorovne(); }


		/**
		 * <p>Posunie obsah obrázka o zadaný počet bodov v horizontálnom
		 * a/alebo vertikálnom smere. Tá časť obrázka, ktorá opustí jeho
		 *  rozmery, bude stratená, pričom na protiľahlej strane vznikne
		 * prázdna oblasť. Metóda má využitie napríklad pri posune hracej
		 * plochy uloženej v obrázku, pričom musíme zabezpečiť, aby boli
		 * vzniknuté prázdne časti plochy dokreslené. Ak chceme pretočiť
		 * obrázok dookola (t. j. bez straty obsahu), musíme použiť metódu
		 * {@link #pretoč(double, double) pretoč}.</p>
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

			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (dx >= šírka || dx <= -šírka ||
				dy >= výška || dy <= -výška)
			{
				Arrays.fill(údajeObrázka, 0);
				return;
			}

			int spodnáZarážka, vrchnáZarážka,
				zarážkaRiadkov, zarážkaStĺpcov;

			if (dy <= 0)
			{
				spodnáZarážka = 0;
				vrchnáZarážka = šírka * -dy - dx;
				zarážkaRiadkov = výška + dy;

				if (dx <= 0)
				{
					zarážkaStĺpcov = šírka + dx;

					for (int i = 0; i < zarážkaRiadkov; ++i)
					{
						int j = 0;

						for (; j < zarážkaStĺpcov; ++j)
							údajeObrázka[spodnáZarážka + j] =
								údajeObrázka[vrchnáZarážka + j];

						for (; j < šírka; ++j)
							údajeObrázka[spodnáZarážka + j] = 0;

						spodnáZarážka += šírka;
						vrchnáZarážka += šírka;
					}
				}
				else
				{
					zarážkaStĺpcov = dx;

					for (int i = 0; i < zarážkaRiadkov; ++i)
					{
						int j = šírka - 1;

						for (; j >= zarážkaStĺpcov; --j)
							údajeObrázka[spodnáZarážka + j] =
								údajeObrázka[vrchnáZarážka + j];

						for (; j >= 0; --j)
							údajeObrázka[spodnáZarážka + j] = 0;

						spodnáZarážka += šírka;
						vrchnáZarážka += šírka;
					}
				}

				vrchnáZarážka = šírka * výška;

				while (spodnáZarážka < vrchnáZarážka)
				{
					údajeObrázka[spodnáZarážka] = 0;
					++spodnáZarážka;
				}
			}
			else
			{
				spodnáZarážka = -dx + šírka * (výška - 1 - dy);
				vrchnáZarážka = šírka * (výška - 1);
				zarážkaRiadkov = dy;

				if (dx <= 0)
				{
					zarážkaStĺpcov = šírka + dx;

					for (int i = výška - 1; i >= zarážkaRiadkov; --i)
					{
						int j = 0;

						for (; j < zarážkaStĺpcov; ++j)
							údajeObrázka[vrchnáZarážka + j] =
								údajeObrázka[spodnáZarážka + j];

						for (; j < šírka; ++j)
							údajeObrázka[vrchnáZarážka + j] = 0;

						spodnáZarážka -= šírka;
						vrchnáZarážka -= šírka;
					}
				}
				else
				{
					zarážkaStĺpcov = dx;

					for (int i = výška - 1; i >= zarážkaRiadkov; --i)
					{
						int j = šírka - 1;

						for (; j >= zarážkaStĺpcov; --j)
							údajeObrázka[vrchnáZarážka + j] =
								údajeObrázka[spodnáZarážka + j];

						for (; j >= 0; --j)
							údajeObrázka[vrchnáZarážka + j] = 0;

						spodnáZarážka -= šírka;
						vrchnáZarážka -= šírka;
					}
				}

				vrchnáZarážka = (šírka * dy) - 1;

				while (vrchnáZarážka >= 0)
				{
					údajeObrázka[vrchnáZarážka] = 0;
					--vrchnáZarážka;
				}
			}
		}

		/**
		 * <p>Pretočí obsah obrázka o zadaný počet bodov v horizontálnom
		 * a/alebo vertikálnom smere. Tá časť obrázka, ktorá by mala pri
		 * pretočení opustiť jeho rozmery, sa objaví na protiľahlej strane.
		 * Pretáčaním obrázka v ľubovoľnom smere nikdy nestratíme obrazovú
		 * informáciu a spätným posunom dostaneme pôvodný obrázok. Ak
		 * z rôznych dôvodov potrebujeme, aby sa pri pretáčaní obrázka tie
		 * časti, ktoré opustia rozmery obrázka stratili a aby vzniknuté
		 * prázdne časti zostali skutočne prázdne (pripravené na ďalšie
		 * kreslenie), musíme použiť metódu {@link #roluj(double, double)
		 * roluj}.</p>
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

			if (null == údajeObrázka)
				údajeObrázka = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

			if (null == údajeOperácie)
				údajeOperácie = new int[údajeObrázka.length];

			System.arraycopy(údajeObrázka, 0,
				údajeOperácie, 0, údajeObrázka.length);

			while (dx < 0) dx += šírka;
			while (dx >= šírka) dx -= šírka;

			while (dy < 0) dy += výška;
			while (dy >= výška) dy -= výška;

			int index1 = 0, index2 = šírka * (výška + 1 - dy) - dx;

			for (int i = dy; i > 0; --i)
			{
				for (int j = dx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeOperácie[index2++];
				}

				index2 -= šírka;

				for (int j = šírka - dx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeOperácie[index2++];
				}

				index2 += šírka;
			}

			index2 = šírka - dx;

			for (int i = výška - dy; i > 0; --i)
			{
				for (int j = dx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeOperácie[index2++];
				}

				index2 -= šírka;

				for (int j = šírka - dx; j > 0; --j)
				{
					údajeObrázka[index1++] =
						údajeOperácie[index2++];
				}

				index2 += šírka;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pretoč(double, double) pretoč}.</p> */
		public void pretoc(double Δx, double Δy) { pretoč(Δx, Δy); }


	// Farba bodu

		/**
		 * <p>Zistí farbu bodu (jedného pixela) obrázka na zadaných
		 * súradniciach v súradnicovom priestore rámca. Funguje podobne
		 * ako {@link Plátno#farbaBodu(double, double) Plátno.farbaBodu(x,
		 * y)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na zadanej
		 *     pozícii alebo inštancia {@link Farebnosť#žiadna žiadna}, ak sú
		 *     zadané súradnice mimo rozmerov obrázka
		 */
		public Farba farbaBodu(double x, double y)
		{
			int x0 = (int)((šírka / 2.0) + x);
			int y0 = (int)((výška / 2.0) - y);
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return žiadna;

			/*
			int farba = getRGB(x0, y0);

			int alpha = (farba >> 24) & 0xff;
			int red   = (farba >> 16) & 0xff;
			int green = (farba >>  8) & 0xff;
			int blue  =  farba        & 0xff;

			return new Farba(red, green, blue, alpha);
			*/
			return new Farba(getRGB(x0, y0), true);
		}

		/**
		 * <p>Zistí farbu bodu (jedného pixela) obrázka na súradniciach
		 * určených polohou objektu. Funguje podobne ako {@link 
		 * #farbaBodu(double, double) farbaBodu(x, y)}, ale namiesto
		 * jednotlivých súradníc sa spracuje poloha zadaného objekt.</p>
		 * 
		 * @param objekt objekt, ktorého poloha určuje súradnice
		 *     vyšetrovaného bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na pozícii
		 *     objektu alebo inštancia {@link Farebnosť#žiadna žiadna}, ak je
		 *     objekt mimo rozmerov obrázka
		 */
		public Farba farbaBodu(Poloha objekt)
		{
			// return farbaBodu(objekt.polohaX(), objekt.polohaY());
			int x0 = (int)((šírka / 2.0) + objekt.polohaX());
			int y0 = (int)((výška / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return žiadna;

			return new Farba(getRGB(x0, y0), true);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na zadaných
		 * súradniciach v súradnicovom priestore rámca zhoduje so zadanou
		 * farbou. Funguje podobne ako {@link Plátno#farbaBodu(double, double,
		 * Color) Plátno.farbaBodu(x, y, farba)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na zadanej
		 *     pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     obrázka a farba bodu na zadaných súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, Color farba)
		{
			int x0 = (int)((šírka / 2.0) + x);
			int y0 = (int)((výška / 2.0) - y);
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			return (farba.getRGB() & 0xffffffff) ==
				(getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na súradniciach
		 * zadaného objektu v súradnicovom priestore rámca zhoduje so zadanou
		 * farbou. Funguje podobne ako {@link #farbaBodu(double, double,
		 * Color) farbaBodu(x, y, farba)}, ale namiesto jednotlivých
		 * súradníc je zadaný objekt, ktorého poloha určuje vyšetrované
		 * súradnice.</p>
		 * 
		 * @param objekt objekt, ktorého poloha určuje súradnice vyšetrovaného
		 *     bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na pozícii
		 *     objektu
		 * @return {@code valtrue} ak je poloha objektu v rámci rozmerov
		 *     obrázka a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, Color farba)
		{
			// return farbaBodu(objekt.polohaX(), objekt.polohaY(), farba);
			int x0 = (int)((šírka / 2.0) + objekt.polohaX());
			int y0 = (int)((výška / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			return (farba.getRGB() & 0xffffffff) ==
				(getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na zadaných
		 * súradniciach v súradnicovom priestore rámca zhoduje s farbou
		 * zadaného objektu. Funguje podobne ako {@link 
		 * Plátno#farbaBodu(double, double, Color)
		 * Plátno.farbaBodu(x, y, farba)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param objekt objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     obrázka a farba bodu na zadaných súradniciach sa zhoduje
		 *     s farbou zadaného objektu (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, Farebnosť objekt)
		{
			int x0 = (int)((šírka / 2.0) + x);
			int y0 = (int)((výška / 2.0) - y);
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			return (objekt.farba().getRGB() & 0xffffffff) ==
				(getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na súradniciach
		 * prvého zadaného objektu (v súradnicovom priestore rámca) zhoduje
		 * s farbou druhého zadaného objektu. Funguje podobne ako
		 * {@link #farbaBodu(double, double, Color)
		 * farbaBodu(x, y, farba)}, ale namiesto jednotlivých súradníc je
		 * spracovaná poloha objektu.</p>
		 * 
		 * @param objekt objekt, ktorého poloha určuje súradnice vyšetrovaného
		 *     bodu
		 * @param farebnosť objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na pozícii objektu
		 * @return {@code valtrue} ak je poloha objektu v rámci rozmerov
		 *     obrázka a farba bodu na jeho súradniciach sa zhoduje s farbou
		 *     zadaného objektu (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, Farebnosť farebnosť)
		{
			// return farbaBodu(objekt.polohaX(), objekt.polohaY(),
			// 	farebnosť.farba());
			int x0 = (int)((šírka / 2.0) + objekt.polohaX());
			int y0 = (int)((výška / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			return (farebnosť.farba().getRGB() & 0xffffffff) ==
				(getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na zadaných
		 * súradniciach v súradnicovom priestore rámca zhoduje s farbou
		 * zadanou prostredníctvom farebných zložiek.
		 * (Úroveň priehľadnosti je nastavená na hodnotu {@code num255},
		 * čiže na úplne nepriehľadnú farbu.)
		 * Funguje podobne ako {@link Plátno#farbaBodu(double, double,
		 * Color) Plátno.farbaBodu(x, y, farba)}, ale farba je
		 * určená prostredníctvom farebných zložiek.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     obrázka a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, int r, int g, int b)
		{
			int x0 = (int)((šírka / 2.0) + x);
			int y0 = (int)((výška / 2.0) - y);
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na zadaných
		 * súradniciach v súradnicovom priestore rámca zhoduje s farbou
		 * zadanou prostredníctvom farebných zložiek a úrovne priehľadnosti.
		 * Funguje podobne ako {@link Plátno#farbaBodu(double, double,
		 * Color) Plátno.farbaBodu(x, y, farba)}, ale farba je
		 * určená prostredníctvom farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na zadanej pozícii
		 * @param a úroveň priehľadnosti farby, ktorú chceme porovnať
		 *     s farbou bodu na zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     obrázka a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, int r, int g, int b, int a)
		{
			int x0 = (int)((šírka / 2.0) + x);
			int y0 = (int)((výška / 2.0) - y);
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na súradniciach
		 * zadaného objektu (v súradnicovom priestore rámca) zhoduje
		 * s farbou zadanou prostredníctvom farebných zložiek a úrovne.
		 * (Úroveň priehľadnosti je nastavená na hodnotu {@code num255},
		 * čiže na úplne nepriehľadnú farbu.)
		 * Funguje podobne ako {@link #farbaBodu(double,
		 * double, Color) farbaBodu(x, y, farba)}, ale namiesto
		 * jednotlivých súradníc je spracovaná poloha objektu a farba je
		 * určená prostredníctvom farebných zložiek.</p>
		 * 
		 * @param objekt objekt, ktorého poloha určuje súradnice vyšetrovaného
		 *     bodu
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @return {@code valtrue} ak je poloha objektu v rámci rozmerov
		 *     obrázka a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, int r, int g, int b)
		{
			int x0 = (int)((šírka / 2.0) + objekt.polohaX());
			int y0 = (int)((výška / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) obrázka na súradniciach
		 * zadaného objektu (v súradnicovom priestore rámca) zhoduje
		 * s farbou zadanou prostredníctvom farebných zložiek a úrovne
		 * priehľadnosti. Funguje podobne ako {@link #farbaBodu(double,
		 * double, Color) farbaBodu(x, y, farba)}, ale namiesto
		 * jednotlivých súradníc je spracovaná poloha objektu a farba je
		 * určená prostredníctvom farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * @param objekt objekt, ktorého poloha určuje súradnice vyšetrovaného
		 *     bodu
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na pozícii objektu
		 * @param a úroveň priehľadnosti farby, ktorú chceme porovnať
		 *     s farbou bodu na pozícii objektu
		 * @return {@code valtrue} ak je poloha objektu v rámci rozmerov
		 *     obrázka a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, int r, int g, int b, int a)
		{
			int x0 = (int)((šírka / 2.0) + objekt.polohaX());
			int y0 = (int)((výška / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírka ||
				y0 < 0 || y0 >= výška) return false;

			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(getRGB(x0, y0) & 0xffffffff);
		}


	// Priehľadnosť

		/**
		 * <p><a class="getter"></a> Zistí aktuálnu úroveň priehľadnosti tohto obrázka.</p>
		 * 
		 * @return aktuálna úroveň priehľadnosti tohto obrázka
		 * 
		 * @see #priehľadnosť(double)
		 * @see #upravPriehľadnosť(double)
		 */
		public double priehľadnosť() { return priehľadnosť; }

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť() priehľadnosť}.</p> */
		public double priehladnost() { return priehľadnosť; }

		/**
		 * <p><a class="setter"></a> Nastaví novú úroveň (ne)priehľadnosti
		 * tohto obrázka, pričom jednotlivé body na obrázku môžu mať svoju
		 * vlastnú úroveň priehľadnosti. Úroveň 0.0 znamená, že obrázok
		 * nebude pri kreslení zobrazený. Úroveň 1.0 znamená, že jednotlivé
		 * body obrázka budú zobrazené s ich vlastnou úrovňou priehľadnosti –
		 * nepriehľadné body budú plne viditeľné. Úroveň priehľadnosti
		 * obrázka nemá vplyv na jeho {@linkplain #ulož(String) uloženie
		 * do súboru}, iba na jeho zobrazenie.</p>
		 * 
		 * @param priehľadnosť nová úroveň priehľadnosti (0.0 – 1.0)
		 * 
		 * @see #priehľadnosť()
		 * @see #upravPriehľadnosť(double)
		 */
		public void priehľadnosť(double priehľadnosť)
		{
			this.priehľadnosť = (float)priehľadnosť;
		}

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť(double) priehľadnosť}.</p> */
		public void priehladnost(double priehľadnosť)
		{ priehľadnosť(priehľadnosť); }

		/**
		 * <p>Skopíruje úroveň (ne)priehľadnosti zo zadaného objektu.</p>
		 * 
		 * @param objekt objekt určujúci novú úroveň priehľadnosti
		 * 
		 * @see #priehľadnosť()
		 * @see #upravPriehľadnosť(double)
		 */
		public void priehľadnosť(Priehľadnosť objekt)
		{ this.priehľadnosť = (float)objekt.priehľadnosť(); }

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť(Priehľadnosť) priehľadnosť}.</p> */
		public void priehladnost(Priehľadnosť objekt)
		{ priehľadnosť(objekt); }

		/**
		 * <p>Upraví úroveň (ne)priehľadnosti obráza. Pre viac informácií
		 * o priehľadnosti pozri {@link #priehľadnosť(double)
		 * priehľadnosť}.</p>
		 * 
		 * @param zmena hodnota, ktorou bude násobená aktuálna hodnota
		 *     priehľadnosti; príklady: 0.5 – priehľadnosť bude znížená
		 *     o polovicu, 2.0 – úroveň priehľadnosti bude zdvojnásobená
		 * 
		 * @see #priehľadnosť(double)
		 * @see #priehľadnosť()
		 */
		public void upravPriehľadnosť(double zmena)
		{
			if (0 == priehľadnosť) priehľadnosť = 0.1f;
			else priehľadnosť *= (float)zmena;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravPriehľadnosť(double) upravPriehľadnosť}.</p> */
		public void upravPriehladnost(double zmena)
		{ upravPriehľadnosť(zmena); }


	// Uloženie do súboru

		/**
		 * <p>Uloží obrázok do súboru. Prípona súboru musí byť {@code .gif},
		 * {@code .png} alebo {@code .jpg} (resp. {@code .jpeg}). Ak súbor
		 * jestvuje, tak vznikne výnimka oznamujúca, že súbor so zadaným
		 * menom už jestvuje. Ak chcete súbor prepísať, použite metódu
		 * {@link #ulož(String, boolean)} s druhým parametrom rovným
		 * {@code valtrue}. Odporúčame pozrieť si opis uvedenej metódy,
		 * pretože obsahuje užitočné príklady použitia a informácie v jej
		 * opise sú platné aj pre túto metódu.</p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * 
		 * @throws GRobotException ak súbor jestvuje alebo bol zadaný názov
		 *     súboru s neplatnou príponou
		 */
		public void ulož(String súbor) { ulož(súbor, false); }

		/** <p><a class="alias"></a> Alias pre {@link #ulož(String) ulož}.</p> */
		public void uloz(String súbor) { ulož(súbor); }

		/**
		 * <p>Uloží obsah obrázka do súboru. Prípona súboru musí byť
		 * {@code .gif}, {@code .png} alebo {@code .jpg}
		 * (resp. {@code .jpeg}).</p>
		 * 
		 * <p>Formát GIF je jedným z najstarších z pohľadu histórie
		 * grafických formátov, ale stal sa obľúbeným, pretože umožňuje
		 * tvoriť animované verzie obrázkov. Ak je inštancia obrázka
		 * animovaná, tak táto metóda automaticky vytvorí a uloží animovanú
		 * verziu GIFu. Implicitne pri tom použije niektoré jestvujúce
		 * vlastnosti/nastavenia/stavy programovacieho rámca, napríklad
		 * {@linkplain Svet#farbaPozadia(Color) farba pozadia} je zmiešaná
		 * so všetkými polopriehľadnými bodmi, pretože formát GIF
		 * nepodporuje čiastočnú priehľadnosť bodov a aktuálna hodnota
		 * {@linkplain  #opakovaniaAnimácie(int) počtu opakovaní animácie}
		 * zároveň určí počet opakovaní uložený a používaný v tomto
		 * súborovom formáte, pričom pri ukladaní má hodnota {@code num0}
		 * rovnaký význam ako akákoľvek záporná hodnota, čiže nekonečný
		 * počet opakovaní (na rozdiel od významu použitého v programovacom
		 * rámci, kedy hodnota {@code num0} znamená koniec animácie
		 * a záporné hodnoty znamenajú nekonečný počet opakovaní).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Formát GIF je historickým formátom s určitými (najmä farebnými)
		 * obmedzeniami, ale stále nachádza svoje použitie. Nasledujúci
		 * príklad ukazuje ako takýto súbor vytvoriť s použitím programovacieho
		 * rámca GRobot, ale na plnohodnotné (plnofarebné) uloženie
		 * snímok animovaného obrázka odporúčame použiť sekvenciu súborov vo
		 * formáte PNG, ktorú táto metóda vytvorí automaticky, ak nájde
		 * v názve súboru zástupný znak <code>*</code> – pozri ďalší
		 * príklad. (Znak <code>*</code> bol zvolený úmyselne, pretože na
		 * nami používaných operačných systémoch ide o neplatný znak v rámci
		 * názvov súborov.)</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} VytvorAnimovanýGIF {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Inštancia obrázka, do ktorej budú ukladané snímky animácie lopty}
				{@code comm// a ktorá výslednú animáciu uloží do formátu GIF.}
				{@code kwdprivate} {@link Obrázok Obrázok} animovanýObrázok;

				{@code comm// Zoznam modrých farieb použitých na tieňovanie lopty.}
				{@code kwdprivate} {@code kwdfinal} {@link Zoznam Zoznam}&lt;{@link Farba Farba}&gt; farby = {@code kwdnew} {@link Zoznam#Zoznam(Object[]) Zoznam}&lt;{@link Farba Farba}&gt;(
					{@code kwdnew} {@link Farba#Farba(int, int, int) Farba}( {@code num0},  {@code num0}, {@code num224}), {@code kwdnew} {@link Farba#Farba(int, int, int) Farba}({@code num16}, {@code num16}, {@code num232}),
					{@code kwdnew} {@link Farba#Farba(int, int, int) Farba}({@code num32}, {@code num32}, {@code num240}), {@code kwdnew} {@link Farba#Farba(int, int, int) Farba}({@code num48}, {@code num48}, {@code num248}),
					{@code kwdnew} {@link Farba#Farba(int, int, int) Farba}({@code num64}, {@code num64}, {@code num255}));

				{@code comm// Táto metóda je schopná nakresliť loptu vrátane jej prípadnej}
				{@code comm// deformácie.}
				{@code kwdprivate} {@code typevoid} kresliLoptu({@code typedouble} sploštenieX, {@code typedouble} sploštenieY)
				{
					{@code comm// Vždy pred kreslením lopty sa aktuálny obsah animovaného obrázka}
					{@code comm// vymaže:}
					animovanýObrázok.{@link Obrázok#vymaž() vymaž}();

					{@code comm// Najprv spracujeme tieňovanie lopty. Na to treba obmedziť oblasť}
					{@code comm// kreslenia do obrázka na požadovaný tvar, čo zariadia nasledujúce}
					{@code comm// tri príkazy:}
					{@link GRobot#nekresliTvary() nekresliTvary}();
					{@link GRobot#kresliDo(Shape) kresliDo}({@link GRobot#elipsa(double, double) elipsa}({@code num40} + sploštenieX, {@code num40} + sploštenieY));
					{@link GRobot#kresliTvary() kresliTvary}();

					{@code comm// Nasledujúci blok kreslí tieňovanie lopty:}
					{@code comm// (Poznámka: Toto by sa dalo riešiť aj }{@link GRobot#náter(Paint) náterom}{@code comm.)}
					{@code typedouble} menej = {@code num0};
					{@link GRobot#skoč(double, double) skoč}(-{@code num12} + sploštenieX, {@code num12} + sploštenieY);
					{@code kwdfor} ({@link Farba Farba} farba : farby)
					{
						{@link GRobot#farba(Color) farba}(farba);
						{@link GRobot#kruh(double) kruh}({@code num60} + menej);
						menej -= {@code num12};
					}
					{@link GRobot#skoč(double, double) skoč}({@code num12} &#45; sploštenieX, -{@code num12} &#45; sploštenieY);

					{@code comm// Rozmažeme kresbu a zrušíme obmedzenie kreslenia do oblasti:}
					animovanýObrázok.{@link Obrázok#rozmaž(int) rozmaž}({@code num12});
					{@link GRobot#kresliVšade() kresliVšade}();

					{@code comm// Na záver prekreslíme obrysy lopty, aby sme trochu zamaskovali}
					{@code comm// nie celkom esteticky rozmazané okraje:}
					{@link GRobot#farba(Color) farba}(farby.{@link Zoznam#prvý() prvý}());
					{@link GRobot#elipsa(double, double) elipsa}({@code num40} + sploštenieX, {@code num40} + sploštenieY);

					{@link GRobot#farba(Color) farba}(farby.{@link Zoznam#daj(int) daj}({@code num1}));
					{@link GRobot#elipsa(double, double) elipsa}({@code num40} + sploštenieX &#45; {@code num1.5}, {@code num40} + sploštenieY &#45; {@code num1.5});
					{@link GRobot#elipsa(double, double) elipsa}({@code num40} + sploštenieX &#45; {@code num3.0}, {@code num40} + sploštenieY &#45; {@code num3.0});
				}

				{@code comm// Odľahčená verzia metódy kresliaca nedeformovanú loptu.}
				{@code kwdprivate} {@code typevoid} kresliLoptu()
				{
					kresliLoptu({@code num0}, {@code num0});
				}

				{@code comm// Súkromný konštruktor.}
				{@code kwdprivate} VytvorAnimovanýGIF()
				{
					{@code comm// Rozmer plátna bude o málo väčší oproti rozmeru obrázka:}
					{@code valsuper}({@code num140}, {@code num240});

					{@code comm// Vypneme automatické prekresľovanie}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

					{@code comm// Vytvoríme nový obrázok, do ktorého bude ukladaná animácia;}
					{@code comm// určíme, že trvanie zobrazenia každej snímky bude 40 ms}
					{@code comm// a vykonáme nevyhnutné nastavenia robota, čo zahŕňa aj}
					{@code comm// presmerovanie jeho kreslenia do animovaného obrázka:}
					animovanýObrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num120}, {@code num220});
					animovanýObrázok.{@link Obrázok#trvanie(double) trvanie}({@code num0.040});
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num3});
					{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(animovanýObrázok);
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#skry() skry}();

					{@code comm// Posunieme robot dozadu (nižšie na obrazovke) a začneme animáciu:}
					{@link GRobot#dozadu(double) dozadu}({@code num52});

					{@code comm// Prvá (päťsnímková) fáza animácie deformuje loptu pri dopade:}
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt;= {@code num4}; ++i)
					{
						kresliLoptu({@code num2.0} * i, -{@code num2.0} * i);
						{@link GRobot#dozadu(double) dozadu}({@code num2.0});
						animovanýObrázok.{@link Obrázok#pridajSnímku() pridajSnímku}();
					}

					{@code comm// Pri návrate deformovanej lopty do pôvodného tvaru jednu snímku}
					{@code comm// preskočíme…}
					{@link GRobot#dopredu(double) dopredu}({@code num2.0});

					{@code comm// …a animujeme už len štyri fázy:}
					{@code kwdfor} ({@code typeint} i = {@code num3}; i &gt;= {@code num0}; --i)
					{
						kresliLoptu({@code num2.0} * i, -{@code num2.0} * i);
						{@link GRobot#dopredu(double) dopredu}({@code num2.0});
						animovanýObrázok.{@link Obrázok#pridajSnímku() pridajSnímku}();
					}

					{@code comm// Ďalšia fáza je výskok lopty do výšky:}
					{@code kwdfor} ({@code typeint} i = {@code num14}; i &gt;= {@code num0}; --i)
					{
						kresliLoptu();
						{@link GRobot#dopredu(double) dopredu}({@code num1.0} * i);
						animovanýObrázok.{@link Obrázok#pridajSnímku() pridajSnímku}();
					}

					{@code comm// A posledná fáza je jej návrat na zem:}
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt;= {@code num14}; ++i)
					{
						kresliLoptu();
						{@link GRobot#dozadu(double) dozadu}({@code num1.0} * i);
						animovanýObrázok.{@link Obrázok#pridajSnímku() pridajSnímku}();
					}

					{@code comm// Vrátime robot domov, obnovíme mu kreslenie na plátno}
					{@code comm// a uložíme a spustíme animáciu:}
					{@link GRobot#domov() domov}();
					{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();
					animovanýObrázok.{@link Obrázok#ulož(String) ulož}({@code srg"lopta.gif"}, {@code valtrue});
					animovanýObrázok.{@link Obrázok#spusti() spusti}();

					{@code comm// Spustíme časovač:}
					{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.020});
				}

				{@code comm// Reakcia na časovač – pravidelne prekresľuje animovaný obrázok, ktorého}
				{@code comm// animácia je vykonávaná automaticky (vnútornými mechanizmami sveta).}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
					{
						{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
						{@link GRobot#obrázok(Image) obrázok}(animovanýObrázok);
						{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
					}
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@code comm// Skrytie sveta (okna aplikácie):}
					{@link Svet Svet}.{@link Svet#skry() skry}();

					{@code comm// Vytvorenie anonymnej inštancie tejto triedy (konštruktor triedy}
					{@code comm// odvodenej od robota vždy zabezpečí vytvorenie aplikácie):}
					{@code kwdnew} VytvorAnimovanýGIF();

					{@code comm// Automatické nastavenie veľkosti a polohy okna sveta}
					{@code comm// (t. j. aplikácie) a jeho zobrazenie:}
					{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();
					{@link Svet Svet}.{@link Svet#zobraz() zobraz}();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>lopta.gif<alt/>Animovaná lopta.</image>Výsledný
		 * obrázok – <a target="_blank"
		 * href="resources/lopta.gif">lopta.gif</a>.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Sekvenciu súborov vo formáte PNG vytvoríte, ak namiesto názvu
		 * súboru zadáte šablónu v nasledujúcom tvare:</p>
		 * 
		 * <p>    <em>«prefix»</em><code>*</code>[<em>«ľubovoľné znaky»</em><code>*</code>]<em>«postfix»</em><code>.png</code></p>
		 * 
		 * <p>Príklady platných šablón a ukážky názvov prvých dvoch
		 * a niekoľkých ďalších náhodne zvolených súborov snímok v animovanej
		 * sekvencii (ak by animácia mala potrebný rozsah, čo najmä
		 * v poslednom prípade hraničí s reálnosťou):</p>
		 * 
		 * <p>    <code>srdce*.png</code> – <code>srdce1.png</code>,
		 * <code>srdce2.png</code>… <code>srdce10.png</code>…</p>
		 * 
		 * <p>    <code>lopta-**.png</code> – <code>lopta-01.png</code>,
		 * <code>lopta-02.png</code>… <code>lopta-10.png</code>…
		 * <code>lopta-102.png</code>…</p>
		 * 
		 * <p>    <code>_algoritmus-****_.png</code> –
		 * <code>_algoritmus-0001_.png</code>,
		 * <code>_algoritmus-0002_.png</code>…
		 * <code>_algoritmus-0010_.png</code>…
		 * <code>_algoritmus-0102_.png</code>…
		 * <code>_algoritmus-1024_.png</code>…
		 * <code>_algoritmus-22800_.png</code>…</p>
		 * 
		 * <p>    <code>Sekvencia_*2345*.PNG</code> –
		 * <code>Sekvencia_000001.PNG</code>,
		 * <code>Sekvencia_000002.PNG</code>…
		 * <code>Sekvencia_000010.PNG</code>…
		 * <code>Sekvencia_000102.PNG</code>…
		 * <code>Sekvencia_001024.PNG</code>…
		 * <code>Sekvencia_022800.PNG</code>…
		 * <code>Sekvencia_10000000.PNG</code>…</p>
		 * 
		 * <p>Na príkladoch je vidieť, že v šablóne je podstatný výskyt
		 * prvého a posledného znaku <code>*</code>, pričom môže ísť aj
		 * o ten istý znak (ak je jediný). Prípadné znaky medzi nimi sú
		 * z pohľadu obsahu ignorované a do úvahy je vzatý len ich celkový
		 * počet, ktorý prispeje k určeniu počtu cifier číslovania v názvoch
		 * súborov snímok animácie.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak sa týmto spôsobom pokúsite
		 * uložiť obrázok, ktorý nemá definovanú ani jednu snímku animácie,
		 * tak táto metóda uloží jediný súbor s poradovým číslom nula a ak
		 * je tento súbor nájdený metódou {@link #čítaj(String) čítaj(súbor)}
		 * pri pokuse o čítanie sekvencie súborov so zhodnou šablónou, tak ho
		 * prečíta ako neanimovaný obrázok a už sa nepokúša hľadať ďalšie
		 * súbory vyhovujúce kritériám šablóny názvov súborov animovanej
		 * sekvencie.</p>
		 * 
		 * <p>Podobne funguje komunikácia pri určovaní výsledného počtu snímok
		 * ak je druhý parameter tejto metódy ({@code prepísať}) rovný
		 * {@code valtrue} a na pevnom disku jestvuje sekvencia s vyšším
		 * alebo nulovým počtom snímok, tak pri zápise táto metóda vymaže
		 * súbor s poradovým číslom nula a/alebo súbor s najbližším vyšším
		 * poradovým číslom po skončení aktuálnej sekvencie. Tým je zaručené,
		 * že metóda {@link #čítaj(String) čítaj(súbor)} prečíta rovnaký počet
		 * snímok aký má aktuálna sekvencia.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} VytvorSekvenciuPNG {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Tieto konštanty určia rozmery indikátora stavu zobrazeného}
				{@code comm// a aktualizovaného na obrazovke počas tvorby a zápisu animácie do}
				{@code comm// sekvencie PNG súborov (v skutočnosti ide o polovičné hodnoty):}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typedouble} výškaIndikátora = {@code num20.0};
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typedouble} šírkaIndikátora = {@code num160.0};

				{@code comm// Táto konštanta určuje požadovaný počet snímok animácie (ak nie je podiel}
				{@code comm// 360.0 / početSnímok celé číslo, tak sa môže skutočný počet zvýšiť}
				{@code comm// o jednu snímku):}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} početSnímok = {@code num250};

				{@code comm// Inštancia obrázka, do ktorej budú ukladané snímky vytváranej animácie}
				{@code comm// a ktorá ju uloží do seknvencie PNG súborov:}
				{@code kwdprivate} {@link Obrázok Obrázok} animovanýObrázok;


				{@code comm// Kreslenie indikátora priebehu.}
				{@code kwdprivate} {@code typevoid} kresliIndikátor({@code typedouble} hotovo)
				{
					{@code comm// Prepočítame percentuálnu hodnotu na počet pixelov:}
					hotovo *= šírkaIndikátora;

					{@code comm// Nastavíme hrúbku čiary tvarov indikátora:}
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num3.0});

					{@code comm// Nakreslenie šedej výplne tej časti indikátora, ktorá reprezentuje}
					{@code comm// nedokončenú časť úlohy:}
					{@link GRobot#farba(Color) farba}({@link Farebnosť#šedá šedá});
					{@link GRobot#vyplňObdĺžnik(double, double) vyplňObdĺžnik}(šírkaIndikátora, výškaIndikátora);

					{@code comm// Nakreslenie čierneho obrysu rovnakej časti indikátora:}
					{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
					{@link GRobot#kresliObdĺžnik(double, double) kresliObdĺžnik}(šírkaIndikátora, výškaIndikátora);

					{@code comm// Presunutie robota na správne miesto tak, aby bola „dokončená časť“}
					{@code comm// indikátora kreslená presne od ľavého okraja celého indikátora:}
					{@link GRobot#skoč(double, double) skoč}(hotovo &#45; šírkaIndikátora, {@code num0});

					{@code comm// Nakreslenie zelenej výplne tej časti indikátora, ktorá reprezentuje}
					{@code comm// dokončenú časť úlohy:}
					{@link GRobot#farba(Color) farba}({@link Farebnosť#zelená zelená});
					{@link GRobot#vyplňObdĺžnik(double, double) vyplňObdĺžnik}(hotovo, výškaIndikátora);

					{@code comm// Nakreslenie čierneho obrysu rovnakej časti indikátora:}
					{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
					kresliObdĺžnik(hotovo, výškaIndikátora);
				}

				{@code comm// Metóda slúžiaca na vytvorenie animácie. Prijíma počet bodov, cez ktoré}
				{@code comm// má prechádzať animovaná krivka a požadovaný počet snímok (na túto}
				{@code comm// hodnotu máme zároveň definovanú konštantu; do tejto metódy ju posielame,}
				{@code comm// aby bola univerzálnejšia – vystrihnuteľná a použiteľná v inej}
				{@code comm// aplikácii).}
				{@code kwdprivate} {@code typevoid} vytvorAnimáciu({@code typeint} početBodov, {@code typeint} početSnímok)
				{
					{@code comm// Pole bodov, cez ktoré prechádza animovaná krivka:}
					{@link Bod Bod}[] čiara = {@code kwdnew} {@link Bod Bod}[početBodov];

					{@code comm// Uloženie prvého bodu do poľa (zámerne je umiestnený mimo plátna,}
					{@code comm// aby nebolo vidno začiatok krivky – pôsobilo by to rušivo):}
					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, -{@link Svet Svet}.{@link Svet#výška() výška}() &#45; {@code num10});
					čiara[{@code num0}] = {@link GRobot#poloha() poloha}();

					{@code comm// Pridanie požadovaného počtu náhodných bodov}
					{@code comm// (okrem prvého a posledného):}
					{@code kwdfor} ({@code typeint} i = {@code num1}; i &lt; početBodov &#45; {@code num1}; ++i)
					{
						{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
						čiara[i] = {@link GRobot#poloha() poloha}();
					}

					{@code comm// Uloženie posledného bodu do poľa (tiež je umiestnený mimo plátna,}
					{@code comm// aby nebolo vidno koniec krivky):}
					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, {@link Svet Svet}.{@link Svet#výška() výška}() + {@code num10});
					čiara[početBodov &#45; {@code num1}] = {@link GRobot#poloha() poloha}();

					{@code comm// Pole farieb použitých na nakreslenie krivky:}
					{@link Farba Farba}[] farby = {@code kwdnew} {@link Farba Farba}[početBodov];

					{@code comm// Stanovenie začiatočnej a koncovej farby krivky:}
					{@link Farba Farba} prvá = červená;
					{@link Farba Farba} posledná = žltá;

					{@code comm// Vypočítanie farebného prechodu medzi začiatočnou a koncovou farbou}
					{@code comm// s použitím lineárnej interpolácie na jednotlivé farebné zložky}
					{@code comm// (vznikne tým lineárny farebný gradient):}
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početBodov; ++i)
						farby[i] = {@code kwdnew} {@link Farba#Farba(int, int, int) Farba}(
							({@code typeint}){@link Svet Svet}.{@link Svet#lineárnaInterpolácia(double, double, double) lineárnaInterpolácia}(
								prvá.{@link Farba#červená() červená}(), posledná.{@link Farba#červená() červená}(),
								({@code typedouble})i / ({@code typedouble})početBodov),
							({@code typeint}){@link Svet Svet}.{@link Svet#lineárnaInterpolácia(double, double, double) lineárnaInterpolácia}(
								prvá.{@link Farba#zelená() zelená}(), posledná.{@link Farba#zelená() zelená}(),
								({@code typedouble})i / ({@code typedouble})početBodov),
							({@code typeint}){@link Svet Svet}.{@link Svet#lineárnaInterpolácia(double, double, double) lineárnaInterpolácia}(
								prvá.{@link Farba#modrá() modrá}(), posledná.{@link Farba#modrá() modrá}(),
								({@code typedouble})i / ({@code typedouble})početBodov));

					{@code comm// Vypočítanie uhla, ktorý bude slúžiť ako prírastok riadiaceho uhla}
					{@code comm// animácie. Riadiaci uhol bude nadobúdať hodnoty medzi nulou}
					{@code comm// a tristošesťdesiat stupňami a tento meniaci sa uhol bude postupne}
					{@code comm// určovať počiatočné pootočenie robota kresliaceho krivku v rámci}
					{@code comm// jednotlivých snímok. Okrem tohto parametra sa na animovaných}
					{@code comm// snímkach nemení nič. Zvyšok algoritmu kreslenia krivky je úplne}
					{@code comm// rovnaký – prejsť všetkými bodmi, ktoré boli náhodne zvolené vyššie.}
					{@code typedouble} prírastok = {@code num360.0} / početSnímok;

					{@code comm// Výroba jednotlivých snímok animácie:}
					{@code kwdfor} ({@code typedouble} uhol = {@code num0.0}; uhol &lt; {@code num360.0}; uhol += prírastok)
					{
						{@code comm// Vymazanie aktuálnej snímky:}
						animovanýObrázok.{@link Obrázok#vymaž() vymaž}();

						{@code comm// Zahájenie niekoľkých cyklov kreslenia krivky, počas ktorých}
						{@code comm// bude rozmazávaná a opätovne prekresľovaná:}
						{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num10}; ++j)
						{
							{@code comm// Presmerujeme kreslenie robota do animovaného obrázka:}
							{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(animovanýObrázok);

							{@code comm// Nastavíme hrúbku kreslenej čiary:}
							{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num5.0} / ({@code typedouble})(j + {@code num1}));

							{@code comm// Skočíme na začiatok krivky:}
							{@link GRobot#skočNa(double, double) skočNa}(čiara[{@code num0}]);
							{@link GRobot#farba(Color) farba}(farby[{@code num0}]);
							{@link GRobot#smer(double) smer}(uhol);

							{@code comm// Ak na snímke jestvuje kresba krivky (ak toto nie je prvá}
							{@code comm// snímka), tak ju rozmažeme:}
							{@code kwdif} ({@code num0} != j) animovanýObrázok.{@link Obrázok#rozmaž(int, Color) rozmaž}({@code num10}, {@link Farebnosť#čierna čierna});

							{@code comm// Nakreslíme farebnú krivku (štruktúra try-catch je}
							{@code comm// potrebná, lebo metóda choďNaPoOblúku môže zlyhať; my však}
							{@code comm// jej prípadné zlyhanie ignorujeme):}
							{@code kwdfor} ({@code typeint} i = {@code num1}; i &lt; početBodov; ++i)
								{@code kwdtry}
								{
									{@link GRobot#farba(Color) farba}(farby[i]);
									{@link GRobot#choďNaPoOblúku(double, double) choďNaPoOblúku}(čiara[i]);
								}
								{@code kwdcatch} ({@link Exception Exception} e)
								{
									{@code comm// (Prípadné chyby ignorujeme…)}
								}

							{@code comm// Vrátime robot domov, obnovíme mu kreslenie na plátno}
							{@code comm// podlahy:}
							{@link GRobot#domov() domov}();
							{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();

							{@code comm// Prekreslíme indikátor:}
							kresliIndikátor(({@code num0.8} * (uhol +
								((prírastok * ({@code typedouble})(j + {@code num1})) / {@code num10.0}))) / {@code num360.0});

							{@code comm// A tiež svet (aby sa zmena indikátora prejavila):}
							{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
						}

						{@code comm// Tu prekreslíme snímku viac ráz cez seba. Keďže obsahuje mnoho}
						{@code comm// polopriehľadných bodov, má to význam. Výsledkom bude}
						{@code comm// zvýraznenie poloriehľadných bodov, ktoré vznikli pri rozmazaní.}
						{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num2}; ++i)
							animovanýObrázok.{@link Obrázok#kresli(Image) kresli}(animovanýObrázok);

						{@code comm// Pridáme snímku do animovaného obrázka:}
						animovanýObrázok.{@link Obrázok#pridajSnímku() pridajSnímku}();
					}
				}

				{@code comm// Súkromný konštruktor.}
				{@code kwdprivate} VytvorSekvenciuPNG()
				{
					{@code comm// Upravíme rozmer plátna (ktorý zároveň určí rozmer obrázka):}
					{@code valsuper}({@code num640}, {@code num480});

					{@code comm// Skryjeme robot:}
					{@link GRobot#skry() skry}();

					{@code comm// Vypneme automatické prekresľovanie:}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

					{@code comm// Automatické nastavenie veľkosti a polohy okna sveta}
					{@code comm// (t. j. aplikácie) a jeho zobrazenie:}
					{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();

					{@code comm// Vytvoríme nový obrázok, do ktorého bude ukladaná animácia:}
					animovanýObrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@link Svet Svet}.{@link Svet#šírka() šírka}(), {@link Svet Svet}.{@link Svet#výška() výška}());

					{@code comm// Určíme, že trvanie zobrazenia každej snímky bude 40 ms:}
					animovanýObrázok.{@link Obrázok#trvanie(double) trvanie}({@code num0.040});

					{@code comm// Nakreslíme počiatočný stav indikátora (prázdny):}
					kresliIndikátor({@code num0.0});

					{@code comm// Prekreslíme svet, aby sa indikátor zobrazil používateľovi:}
					{@link Svet Svet}.{@link Svet#prekresli() prekresli}();

					{@code comm// Spustíme časomieru, pretože sme zvedaví ako dlho bude trvať}
					{@code comm// generovanie animácie:}
					{@link Svet Svet}.{@link Svet#spustiČasomieru() spustiČasomieru}();

					{@code comm// Spustíme výrobu animácie:}
					vytvorAnimáciu({@code num50}, početSnímok);

					{@code comm// Zastavíme časomieru a necháme výsledok vypísať na konzolu}
					{@code comm// (výpis sa zobrazí v okne terminálu BlueJa):}
					{@code typedouble} čas = {@link Svet Svet}.{@link Svet#zastavČasomieru() zastavČasomieru}();
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Tvorba animácie trvala: "} +
						{@link Math Math}.{@link Math#round(double) round}(čas / {@code num60}) + {@code srg" min "} + {@link GRobot#F(double, int) F}(čas % {@code num60}, {@code num2}) + {@code srg" s"});

					{@code comm// Opäť spustíme časomieru, lebo sme zvedaví ako dlho bude trvať}
					{@code comm// ukladanie PNG sekvencie:}
					{@link Svet Svet}.{@link Svet#spustiČasomieru() spustiČasomieru}();

					{@code comm// Vytvoríme priečinok, do ktorého bude sekvencia ukladaná:}
					{@link Súbor Súbor}.{@link Súbor#novýPriečinok(String) novýPriečinok}({@code srg"sekvencia"});

					{@code comm// Uložíme sekvenciu obrázkov podľa šablóny (šablóna obsahuje názov}
					{@code comm// podpriečinka „sekvencia“):}
					animovanýObrázok.{@link Obrázok#ulož(String) ulož}({@code srg"sekvencia/nahodne-ciary-***.png"}, {@code valtrue});

					{@code comm// Opäť zastavíme časomieru a vypíšeme výsledok (tiež na konzolu):}
					čas = {@link Svet Svet}.{@link Svet#zastavČasomieru() zastavČasomieru}();
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Vytváranie sekvencie súborov trvalo: "} +
						{@link Math Math}.{@link Math#round(double) round}(čas / {@code num60}) + {@code srg" min "} + {@link GRobot#F(double, int) F}(čas % {@code num60}, {@code num2}) + {@code srg" s"});

					{@code comm// Možno je to pre niekoho zbytočné, ale nakoniec nakreslíme}
					{@code comm// indikátor v úplne dokončenom stave (ak by náhodou zopár}
					{@code comm// nasledujúcich príkazov trvalo dlhšie):}
					{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
					kresliIndikátor({@code num1.0});
					{@link Svet Svet}.{@link Svet#prekresli() prekresli}();

					{@code comm// Vrátime robot domov, aby bola animácia kreslená korektne:}
					{@link GRobot#domov() domov}();

					{@code comm// A spustíme animáciu aj časovač:}
					animovanýObrázok.{@link Obrázok#spusti() spusti}();
					{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.040});

					{@code comm// Na záver nastavíme čiernu farbu pozadia:}
					{@link Svet Svet}.{@link Svet#farbaPozadia(Color) farbaPozadia}({@link Farebnosť#čierna čierna});
				}

				{@code comm// Táto reakcia je automaticky spúšťaná počas zápisu (alebo čítania)}
				{@code comm// animovanej PNG sekvencie. Používame ju na aktualizovanie informácie}
				{@code comm// o stave zápisu zobrazenej na obrazovke pre používateľa.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#sekvencia(int, Object, Object, long, long) sekvencia}({@code typeint} kódSpracovania, {@link String String} zdroj,
					{@link String String} cieľ, {@code typelong} stav, {@code typelong} celkovo)
				{
					{@code comm// Pred každým kreslení indikátora vymažeme grafiku podlahy}
					{@code comm// a vrátime robot domov:}
					{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
					{@link GRobot#domov() domov}();

					{@code comm// Vypočítame percento dokončenia s tým, že 80 % bolo vykonaných}
					{@code comm// pri tvorbe animácie, takže teraz zobrazujeme už len priebeh zvyšných}
					{@code comm// 20 %:}
					kresliIndikátor({@code num0.8} + (({@code num0.2} * ({@code typedouble})stav) / ({@code typedouble})celkovo));

					{@code comm// Prekreslíme obrazovku:}
					{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}

				{@code comm// Reakcia na časovač – pravidelne prekresľuje animovaný obrázok, ktorého}
				{@code comm// animácia je vykonávaná automaticky (vnútornými mechanizmami sveta).}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}())
					{
						{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();
						{@link GRobot#obrázok(Image) obrázok}(animovanýObrázok);
						{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
					}
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@code comm// Vytvorenie anonymnej inštancie tejto triedy (konštruktor triedy}
					{@code comm// odvodenej od robota vždy zabezpečí vytvorenie aplikácie):}
					{@code kwdnew} VytvorSekvenciuPNG();
				}
			}
			</pre>
		 * 
		 * <p><b>Opis priebehu a výsledku:</b></p>
		 * 
		 * <p>Počas tvorby animácie bude v okne sveta aktualizovaný grafický
		 * ukazovateľ stavu činnosti:</p>
		 * 
		 * <p><image>priebeh-vytvarania-sekvencie.png<alt/>Ukážka grafického
		 * ukazovateľa priebehu.</image>Grafický ukazovateľ stavu
		 * činnosti.</p>
		 * 
		 * <p>Kód spracovania reakcie {@link GRobot#sekvencia(int, Object,
		 * Object, long, long) sekvencia} sme v príklade nepoužili. V tomto
		 * prípade mal hodnotu konštanty {@link GRobot#ZÁPIS_PNG_SEKVENCIE
		 * ZÁPIS_PNG_SEKVENCIE}. (V prípade ukladania animáce do formátu GIF
		 * je hodnota rovná hodnote konštanty
		 * {@link GRobot#ZÁPIS_GIF_ANIMÁCIE ZÁPIS_GIF_ANIMÁCIE}.)</p>
		 * 
		 * <p>Podľa nami nameraných hodnôt trvalo vytváranie sekvencie od
		 * štyroch do šesť a pol minúty. Po skončení fázy generovania (80 %
		 * ukazovateľa) pokračuje aplikácia fázou ukladania sekvencie
		 * do súboru (zvyšných 20 % ukazovateľa). Podľa nami nameraných hodnôt
		 * trvalo ukladanie sekvencie asi minútu a štyridsať sekúnd
		 * (s rozptylom desať sekúnd).</p>
		 * 
		 * <p>Výsledkom vykonania tohto príkladu bude sekvencia súborov vo
		 * formáte PNG vytvorená na disku v priečinku <code>sekvencie</code>.
		 * Prvý súbor bude mať názov <code>nahodne-ciary-001.png</code>
		 * a posledný <code>nahodne-ciary-251.png</code>.</p>
		 * 
		 * <p>Po dokončení bude v okne aplikácie spustená vytvorená animácia.</p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>sekvencia-png-1.gif<alt/>Sekvencia 1.</image></td>
		 * <td><image>sekvencia-png-2.gif<alt/>Sekvencia 2.</image></td>
		 * </tr><tr><td colspan="2"><p class="image">Ukážky možného vzhľadu
		 * vytvorenej animáce – výsledok každého generovania je unikátny
		 * <small>(plátno ukážok je úmyselne
		 * zmenšené)</small>.</p></td></tr></table>
		 * 
		 * <p>Na 32-bitových operačných systémoch sa môže stať, že aplikácia
		 * nebude mať dostatok pamäte na vykonanie. V takom prípade skúste
		 * cez príkazový riadok operačného systému spustiť aplikáciu
		 * nasledujúcim príkazom potvrdeným v priečinku s preloženou
		 * aplikáciou:</p>
		 * 
		 * <pre CLASS="example">
			java -Xmx1g VytvorSekvenciuPNG
			</pre>
		 * 
		 * <p> </p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * @param prepísať ak je {@code valtrue}, prípadný jestvujúci
		 *     súbor bude prepísaný, inak sa správa rovnako ako metóda
		 *     {@link #ulož(String)}
		 * 
		 * @throws GRobotException ak súbor jestvuje a parameter prepísať
		 *     je {@code valfalse} alebo ak bol zadaný názov súboru
		 *     s neplatnou príponou
		 */
		public void ulož(String súbor, boolean prepísať)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			if (!snímky.isEmpty())
			{
				snímka = overIndexSnímky(snímka);
				uložGrafikuSnímky(snímka);
			}

			int prvýIndex = súbor.indexOf('*');

			if (-1 != prvýIndex)
			{
				String prípona = súbor.substring(súbor.lastIndexOf('.') + 1);

				if (!prípona.toLowerCase().equals("png")/* &&
					!prípona.toLowerCase().equals("jpg") &&
					!prípona.toLowerCase().equals("jpeg")*/)
					throw new GRobotException("Šablóna „" + súbor +
						"“ nie je použiteľná.", "invalidImageTemplate", súbor);

				int poslednýIndex = 1 + súbor.lastIndexOf('*');
				String prefix = súbor.substring(0, prvýIndex);
				String postfix = súbor.substring(poslednýIndex);
				String formát = "%0" + (poslednýIndex - prvýIndex) + "d";

				int n = snímky.size();
				File uložiťDo = null;
				String menoSúboru = null;

				if (!prepísať)
				{
					if (0 == n)
					{
						menoSúboru = prefix + String.format(
							Locale.ENGLISH, formát, 0) + postfix;
						uložiťDo = new File(menoSúboru);

						if (uložiťDo.exists())
							throw new GRobotException("Obrázok „" +
								menoSúboru + "“ už jestvuje.",
								"imageAlreadyExists", menoSúboru);
					}
					else
					{
						for (int i = 0; i <= n + 1; ++i)
						{
							menoSúboru = prefix + String.format(
								Locale.ENGLISH, formát, i) + postfix;
							uložiťDo = new File(menoSúboru);

							if (uložiťDo.exists())
								throw new GRobotException("Obrázok „" +
									menoSúboru + "“ už jestvuje.",
									"imageAlreadyExists", menoSúboru);
						}
					}
				}

				if (0 == n)
				{
					// Jediný súbor PNG:
					menoSúboru = prefix + String.format(
						Locale.ENGLISH, formát, 0) + postfix;
					uložiťDo = new File(menoSúboru);

					try
					{
						ImageIO.write(this, prípona, uložiťDo);

						if (null != ObsluhaUdalostí.počúvadlo)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.sekvencia(
									ZÁPIS_PNG_SEKVENCIE,
									this, menoSúboru, 0, 0);
							}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci :
								GRobot.počúvajúciSúbory)
							{
								počúvajúci.sekvencia(
									ZÁPIS_PNG_SEKVENCIE,
									this, menoSúboru, 0, 0);
							}
						}
					}
					catch (IOException e)
					{ GRobotException.vypíšChybovéHlásenia(e, true); }
				}
				else
				{
					menoSúboru = prefix + String.format(
						Locale.ENGLISH, formát, 0) + postfix;
					uložiťDo = new File(menoSúboru);
					if (uložiťDo.exists()) uložiťDo.delete();

					menoSúboru = prefix + String.format(
						Locale.ENGLISH, formát, n + 1) + postfix;
					uložiťDo = new File(menoSúboru);
					if (uložiťDo.exists()) uložiťDo.delete();

					// Skutočná sekvencia súborov PNG:
					for (int i = 0; i < n; ++i)
					{
						Snímka snímka = snímky.get(i);

						menoSúboru = prefix + String.format(
							Locale.ENGLISH, formát, i + 1) + postfix;
						uložiťDo = new File(menoSúboru);

						try
						{
							ImageIO.write(snímka, prípona, uložiťDo);

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.sekvencia(
										ZÁPIS_PNG_SEKVENCIE,
										snímka, menoSúboru, i + 1, n);
								}

							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								for (GRobot počúvajúci :
									GRobot.počúvajúciSúbory)
								{
									počúvajúci.sekvencia(
										ZÁPIS_PNG_SEKVENCIE,
										snímka, menoSúboru, i + 1, n);
								}
							}
						}
						catch (IOException e)
						{ GRobotException.vypíšChybovéHlásenia(e, true); }
					}
				}
			}
			else
			{
				File uložiťDo = new File(súbor);

				if (!prepísať && uložiťDo.exists())
					throw new GRobotException("Obrázok „" + súbor +
						"“ už jestvuje.", "imageAlreadyExists", súbor);

				String prípona = súbor.substring(súbor.lastIndexOf('.') + 1);

				if (prípona.toLowerCase().equals("png"))
				{
					// Súbory png:
					try { ImageIO.write(this, prípona, uložiťDo); }
					catch (IOException e)
					{ GRobotException.vypíšChybovéHlásenia(e, true); }
				}
				else if (prípona.toLowerCase().equals("jpg") ||
					prípona.toLowerCase().equals("jpeg"))
				{
					// Pre jpeg musíme zmeniť farebný model z ARGB na RGB
					// Pozri: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727

					WritableRaster raster = getRaster();
					WritableRaster novýRaster = raster.createWritableChild(
						0, 0, šírka, výška, 0, 0, new int[] {0, 1, 2});

					DirectColorModel farebnýModel =
						(DirectColorModel)getColorModel();

					DirectColorModel novýFarebnýModel =
						new DirectColorModel(farebnýModel.getPixelSize(),
						farebnýModel.getRedMask(),
						farebnýModel.getGreenMask(),
						farebnýModel.getBlueMask());

					BufferedImage rgbBuffer = new BufferedImage(
						novýFarebnýModel, novýRaster, false, null);

					try { ImageIO.write(rgbBuffer, prípona, uložiťDo); }
					catch (IOException e)
					{ GRobotException.vypíšChybovéHlásenia(e, true); }
				}
				else if (prípona.toLowerCase().equals("gif"))
				{
					AnimatedGifEncoder age = new AnimatedGifEncoder();
					age.start(súbor);

					if (0 >= opakuj)
						age.setRepeat(0);
					else if (1 != opakuj)
						age.setRepeat(opakuj);

					int n = snímky.size();

					if (0 == n)
					{
						age.setDelay(0);
						SnímkaGIFu snímkaGIFu = new SnímkaGIFu(this);

						if (null != snímkaGIFu.transparentná)
							age.setTransparent(snímkaGIFu.transparentná);

						age.addFrame(snímkaGIFu);
					}
					else
					{
						for (int i = 0; i < n; ++i)
						{
							Snímka snímka = snímky.get(i);
							age.setDelay(snímka.trvanie);
							SnímkaGIFu snímkaGIFu = new SnímkaGIFu(snímka);

							if (null != snímkaGIFu.transparentná)
								age.setTransparent(snímkaGIFu.transparentná);

							age.addFrame(snímkaGIFu);

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.sekvencia(
										ZÁPIS_GIF_ANIMÁCIE,
										snímka, súbor, i + 1, n);
								}

								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									for (GRobot počúvajúci :
										GRobot.počúvajúciSúbory)
									{
										počúvajúci.sekvencia(
											ZÁPIS_GIF_ANIMÁCIE,
											snímka, súbor, i + 1, n);
									}
								}
						}
					}

					age.finish();
				}
				else
				{
					throw new GRobotException("Neplatný formát obrázka: " +
						prípona, "invalidImageFormat", prípona);
				}
			}
		}


		// --- Pomocná trieda použitá pri ukladaní vo formáte GIF --- //

		private class SnímkaGIFu extends BufferedImage
		{
			public Color transparentná;
			private int[] raster;

			public SnímkaGIFu(Obrázok obrázok)
			{
				super(obrázok.šírka, obrázok.výška,
					BufferedImage.TYPE_INT_ARGB);

				raster = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();

				if (null == obrázok.údajeObrázka)
					obrázok.údajeObrázka = ((DataBufferInt)obrázok.
						getRaster().getDataBuffer()).getData();

				System.arraycopy(obrázok.údajeObrázka, 0,
					raster, 0, obrázok.údajeObrázka.length);

				nájdiTransparentnú();
			}

			public SnímkaGIFu(Snímka snímka)
			{
				super(snímka.getWidth(), snímka.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

				raster = ((DataBufferInt)getRaster().
					getDataBuffer()).getData();
				int[] kopíruj = ((DataBufferInt)snímka.getRaster().
					getDataBuffer()).getData();
				System.arraycopy(kopíruj, 0, raster, 0, kopíruj.length);

				nájdiTransparentnú();
			}

			// Nájde transparentnú farbu a zbaví sa všetkých
			// (polo)priehľadných bodov v rastri
			private void nájdiTransparentnú()
			{
				boolean nehľadajTransparentnú = true;

				int ra = 0, ga = 0, ba = 0, aa = 0;
				int počet = 0;

				for (int i = 0; i < raster.length; ++i)
				{
					if (((raster[i] >> 24) & 0xff) > 0x0f)
					{
						++počet;
						ra += (raster[i] >> 16) & 0xff;
						ga += (raster[i] >>  8) & 0xff;
						ba +=  raster[i]        & 0xff;
					}
					else
						nehľadajTransparentnú = false;
				}

				int farba = Svet.farbaPozadia.getRGB();

				int rb = (farba >> 16) & 0xff;
				int gb = (farba >>  8) & 0xff;
				int bb =  farba        & 0xff;

				int rc = 0, gc = 0, bc = 0;

				if (nehľadajTransparentnú)
				{
					transparentná = null;

					for (int i = 0; i < raster.length; ++i)
					{
						aa = (raster[i] >> 24) & 0xff;
						if (aa != 0xff)
						{
							ra = (raster[i] >> 16) & 0xff;
							ga = (raster[i] >>  8) & 0xff;
							ba =  raster[i]        & 0xff;

							rc = ((ra * aa) + (rb * (0xff - aa))) / 0xff;
							gc = ((ga * aa) + (gb * (0xff - aa))) / 0xff;
							bc = ((ba * aa) + (bb * (0xff - aa))) / 0xff;

							raster[i] = new Color(rc, gc, bc).getRGB();
						}
					}
				}
				else
				{
					if (0 == počet)
					{
					}
					else
					{
						ra /= počet;
						ga /= počet;
						ba /= počet;
					}

					ra = 0xff - ra;
					ga = 0xff - ga;
					ba = 0xff - ba;

					transparentná = new Color(ra, ga, ba);

					farba = transparentná.getRGB();

					// System.out.println("  Farba: " + farba);

					for (int i = 0; i < raster.length; ++i)
					{
						aa = (raster[i] >> 24) & 0xff;
						if (aa <= 0x0f)
						{
							raster[i] = farba;
						}
						else if (aa != 0xff)
						{
							ra = (raster[i] >> 16) & 0xff;
							ga = (raster[i] >>  8) & 0xff;
							ba =  raster[i]        & 0xff;

							rc = ((ra * aa) + (rb * (0xff - aa))) / 0xff;
							gc = ((ga * aa) + (gb * (0xff - aa))) / 0xff;
							bc = ((ba * aa) + (bb * (0xff - aa))) / 0xff;

							raster[i] = new Color(rc, gc, bc).getRGB();
						}
					}
				}
			}
		}

		// --- Pomocná trieda použitá pri ukladaní vo formáte GIF --- //


		/** <p><a class="alias"></a> Alias pre {@link #ulož(String, boolean) ulož}.</p> */
		public void uloz(String súbor, boolean prepísať)
		{ ulož(súbor, prepísať); }


	// Vloženie do schránky

		/**
		 * <p>Vloží obsah tohto obrázka do schránky.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Obrázok je uchovaný
		 * v schránke len počas činnosti aplikácie robota. Po zatvorení
		 * okna sveta, je obrázok zo schránky odstránený.</p>
		 * 
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#obrázok(Image)
		 */
		public boolean doSchránky() { return Schránka.obrázok(this); }

		/** <p><a class="alias"></a> Alias pre {@link #doSchránky() doSchránky}.</p> */
		public boolean doSchranky() { return Schránka.obrázok(this); }


	// Porovnanie

		/**
		 * <p>Porovná obsahy dvoch zadaných inštancií obrázkov. V prípade,
		 * že sú oba argumenty tejto metódy tým istým obrázkom, tak je bez
		 * ďalšej kontroly vrátená hodnota {@code valtrue}. Ak ide o rôzne
		 * objekty obrázkov, tak bude overená zhoda ich obsahu. Najskôr sú
		 * porovnané rozmery obrázkov a ak sú zhodné, tak sú porovnané ich
		 * rastre. Potom je podľa toho, či sú obsahovo zhodné alebo nie,
		 * vrátená hodnota {@code valtrue} alebo {@code valfalse}.</p>
		 * 
		 * @param obrázok1 prvý porovnávaný obrázok
		 * @param obrázok2 druhý porovnávaný obrázok
		 * @return ak sú obrázky zhodné, tak {@code valtrue}, inak
		 *     {@code valfalse}
		 */
		public static boolean porovnaj(Image obrázok1, Image obrázok2)
		{
			if (obrázok1 == obrázok2) return true;

			BufferedImage porovnaj1 = preveďNaBufferedImage(obrázok1);
			BufferedImage porovnaj2 = preveďNaBufferedImage(obrázok2);

			if (porovnaj1.getWidth() != porovnaj2.getWidth() ||
				porovnaj1.getHeight() != porovnaj2.getHeight()) return false;

			if (TYPE_INT_ARGB != porovnaj1.getType())
			{
				BufferedImage preveď = new BufferedImage(
					porovnaj1.getWidth(), porovnaj1.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
				preveď.getGraphics().drawImage(porovnaj1, 0, 0, null);
				porovnaj1 = preveď;
			}

			if (TYPE_INT_ARGB != porovnaj2.getType())
			{
				BufferedImage preveď = new BufferedImage(
					porovnaj2.getWidth(), porovnaj2.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
				preveď.getGraphics().drawImage(porovnaj2, 0, 0, null);
				porovnaj2 = preveď;
			}

			int[] údaje1 = ((DataBufferInt)porovnaj1.getRaster().
				getDataBuffer()).getData();
			int[] údaje2 = ((DataBufferInt)porovnaj2.getRaster().
				getDataBuffer()).getData();

			return Arrays.equals(údaje1, údaje2);
		}

		/**
		 * <p>Porovná obsah tejto a zadanej inštancie obrázka. Najskôr sú
		 * overené rozmery obrázkov a ak sú zhodné, tak sú porovnané ich
		 * rastre a podľa toho, či sú obsahovo zhodné alebo nie, je vrátená
		 * hodnota {@code valtrue} alebo {@code valfalse}.</p>
		 * 
		 * @param inýObrázok obrázok, s ktorým je porovnávaný obsah tejto
		 *     inštancie
		 * @return ak je obsah tohto a iného obrázka zhodný, tak
		 *     {@code valtrue}, inak {@code valfalse}
		 */
		public boolean porovnaj(Image inýObrázok)
		{ return porovnaj(this, inýObrázok); }


	// Vlnenie

		// Inštancia vlnenia tohto obrázka
		private Vlnenie vlnenie = null;

		/**
		 * <p>Overí, či je definovaná inštancia vlnenia pre tento obrázok.</p>
		 * 
		 * @return {@code valtrue} ak je inštancia vlnenia definovaná;
		 *     {@code valfalse} v opačnom prípade
		 * 
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 */
		public boolean máVlnenie() { return null != vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #máVlnenie() máVlnenie}.</p> */
		public boolean maVlnenie() { return null != vlnenie; }

		/**
		 * <p>Vráti {@linkplain Vlnenie inštanciu vlnenia} pre tento obrázok,
		 * aby bolo možné s vlnením ďalej pracovať. Ak vlnenie nie je pre
		 * tento obrázok definované, tak metóda definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. <small>(Overiť
		 * to, či je definovaná inštancia vlnenia, je možné s pomocou metódy
		 * {@link #máVlnenie() máVlnenie}.)</small> Naopak, metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v takom prípade, že jestvuje. <small>(V opačnom
		 * prípade vráti metóda {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie} hodnotu {@code valnull}.)</small></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Ak svet grafického robota
		 * nemá aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať ani po jeho aktivácii. Táto metóda <b>nespúšťa</b>
		 * časovač (ani vlnenie) automaticky! Účelom automatického
		 * vytvorenia inštancie vlnenia touto metódou v prípade jej
		 * neprítomnosti je len zabránenie vzniku chýb. Táto metóda nemá
		 * nahradiť metódu {@link #pridajVlnenie() pridajVlnenie}.</p>
		 * 
		 * @return metóda zaručuje vrátenie inštancie {@link Vlnenie Vlnenie}
		 *     definovanej pre tento obrázok aj v takom prípade, že pred jej
		 *     volaním nebola inštancia definovaná
		 * 
		 * @see #máVlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 */
		public Vlnenie vlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(this, 26);
				vlnenie.posun(posunX, posunY);
				Vlnenie.vlnenia.add(vlnenie);
			}
			return vlnenie;
		}

		/**
		 * <p>Táto metóda vráti inštanciu vlnenia len v prípade, že jestvuje.
		 * V opačnom prípade vráti hodnotu {@code valnull}, čo môže viesť
		 * ku vzniku výnimky, ak sa programátor pokúsi použiť vrátenú
		 * hodnotu bez overenia. Naopak, vrátenie inštancie
		 * {@linkplain Vlnenie vlnenia} aj v prípade, že ešte nebolo
		 * definované zaručuje metóda {@link #vlnenie() vlnenie}.</p>
		 * 
		 * @return ak je definovaná inštancia {@linkplain Vlnenie vlnenia},
		 *     tak ju metóda vráti; v opačnom prípade vráti hodnotu
		 *     {@code valnull}
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 */
		public Vlnenie jestvujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie jestvujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie existujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie existujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie definovanéVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie definovaneVlnenie() { return vlnenie; }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto obrázka. Ak nie je definované
		 * alebo aktívne vlnenie pre tento obrázok, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia s predvolenou
		 * úrovňou útlmu {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p>Ak by svet grafického robota nemal aktívny
		 * {@linkplain Svet#spustiČasovač() časovač}, tak by vlnenie nemohlo
		 * fungovať, preto je časovač touto metódou spúšťaný automaticky.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou s pomocou
		 * metódy {@link #vlnenie() vlnenie} alebo {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie}.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak vlnenie nie je definované,
		 * tak metóda {@link #vlnenie() vlnenie} definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. Overiť to, či
		 * je definovaná inštancia vlnenia, je možné s pomocou metódy
		 * {@link #máVlnenie() máVlnenie}. Metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v prípade, že jestvuje. V opačnom prípade vráti hodnotu
		 * {@code valnull}, čo môže viesť ku vzniku výnimky, ak sa
		 * programátor pokúsi použiť vrátenú hodnotu bez overenia.</p>
		 * 
		 * <p>Ak už je definovaná inštancia vlnenia, tak ju volanie tejto
		 * metódy zresetuje upokojením hladiny a nastavením predvolenej
		 * úrovne útlmu {@code num26}.
		 * (Aktivácia je vykonaná v každom prípade.)</p>
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 */
		public void pridajVlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(this, 26);
				vlnenie.posun(posunX, posunY);
				Vlnenie.vlnenia.add(vlnenie);
				Svet.spustiČasovač();
			}
			else
			{
				vlnenie.upokojHladinu();
				vlnenie.útlm(26);
			}
			vlnenie.aktivuj();
		}

		// /** <p><a class="alias"></a> Alias pre {@link #pridajVlnenie() pridajVlnenie}.</p> */
		// public void zacniVlnenie() { pridajVlnenie(); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto obrázka. Ak nie je definované
		 * alebo aktívne vlnenie pre tento obrázok, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia s predvolenou
		 * úrovňou útlmu {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak svet grafického robota nemá
		 * aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať. Táto metóda dovoľuje určiť, či má alebo nemá
		 * byť časovač spustený automaticky. Umožňuje to parameter
		 * {@code ajČasovač}.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou s pomocou
		 * metódy {@link #vlnenie() vlnenie} alebo {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie}.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak vlnenie nie je definované,
		 * tak metóda {@link #vlnenie() vlnenie} definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. Overiť to, či
		 * je definovaná inštancia vlnenia, je možné s pomocou metódy
		 * {@link #máVlnenie() máVlnenie}. Metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v prípade, že jestvuje. V opačnom prípade vráti hodnotu
		 * {@code valnull}, čo môže viesť ku vzniku výnimky, ak sa
		 * programátor pokúsi použiť vrátenú hodnotu bez overenia.</p>
		 * 
		 * <p>Ak už je definovaná inštancia vlnenia, tak ju volanie tejto
		 * metódy zresetuje upokojením hladiny a nastavením predvolenej
		 * úrovne útlmu {@code num26}.
		 * (Aktivácia je vykonaná v každom prípade.)</p>
		 * 
		 * @param ajČasovač ak je hodnota tohto parametra rovná
		 *     {@code valtrue}, tak je v prípade jeho nečinnosti
		 *     automaticky {@linkplain Svet#spustiČasovač()
		 *     spustený časovač}
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 */
		public void pridajVlnenie(boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(this, 26);
				vlnenie.posun(posunX, posunY);
				Vlnenie.vlnenia.add(vlnenie);
				if (ajČasovač) Svet.spustiČasovač();
			}
			else
			{
				vlnenie.upokojHladinu();
				vlnenie.útlm(26);
			}
			vlnenie.aktivuj();
		}

		// /** <p><a class="alias"></a> Alias pre {@link #pridajVlnenie(boolean) pridajVlnenie}.</p> */
		// public void zacniVlnenie(boolean ajČasovač) { pridajVlnenie(ajČasovač); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto obrázka. Ak nie je definované
		 * alebo aktívne vlnenie pre tento obrázok, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia so zadanou úrovňou
		 * útlmu (pozri aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p>Ak by svet grafického robota nemal aktívny
		 * {@linkplain Svet#spustiČasovač() časovač}, tak by vlnenie nemohlo
		 * fungovať, preto je časovač touto metódou spúšťaný automaticky.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou s pomocou
		 * metódy {@link #vlnenie() vlnenie} alebo {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie}.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak vlnenie nie je definované,
		 * tak metóda {@link #vlnenie() vlnenie} definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. Overiť to, či
		 * je definovaná inštancia vlnenia, je možné s pomocou metódy
		 * {@link #máVlnenie() máVlnenie}. Metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v prípade, že jestvuje. V opačnom prípade vráti hodnotu
		 * {@code valnull}, čo môže viesť ku vzniku výnimky, ak sa
		 * programátor pokúsi použiť vrátenú hodnotu bez overenia.</p>
		 * 
		 * <p>Ak už je definovaná inštancia vlnenia, tak ju volanie tejto
		 * metódy zresetuje upokojením hladiny a nastavením zadanej úrovne
		 * útlmu.
		 * (Aktivácia je vykonaná v každom prípade.)</p>
		 * 
		 * @param útlm požadovaná úroveň útlmu vlnenia; odporúčané sú
		 *     hodnoty v rozmedzí 0 – 30; pozri aj {@link Vlnenie#útlm(int)
		 *     Vlnenie.útlm(útlm)}
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int, boolean)
		 * @see #odstráňVlnenie()
		 * @see Vlnenie#útlm(int)
		 */
		public void pridajVlnenie(int útlm)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(this, útlm);
				vlnenie.posun(posunX, posunY);
				Vlnenie.vlnenia.add(vlnenie);
				Svet.spustiČasovač();
			}
			else
			{
				vlnenie.upokojHladinu();
				vlnenie.útlm(útlm);
			}
			vlnenie.aktivuj();
		}

		// /** <p><a class="alias"></a> Alias pre {@link #pridajVlnenie(int) pridajVlnenie}.</p> */
		// public void zacniVlnenie(int útlm) { pridajVlnenie(útlm); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto obrázka. Ak nie je definované
		 * alebo aktívne vlnenie pre tento obrázok, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia so zadanou úrovňou
		 * útlmu (pozri aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak svet grafického robota nemá
		 * aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať. Táto metóda dovoľuje určiť, či má alebo nemá
		 * byť časovač spustený automaticky. Umožňuje to parameter
		 * {@code ajČasovač}.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou s pomocou
		 * metódy {@link #vlnenie() vlnenie} alebo {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie}.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak vlnenie nie je definované,
		 * tak metóda {@link #vlnenie() vlnenie} definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. Overiť to, či
		 * je definovaná inštancia vlnenia, je možné s pomocou metódy
		 * {@link #máVlnenie() máVlnenie}. Metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v prípade, že jestvuje. V opačnom prípade vráti hodnotu
		 * {@code valnull}, čo môže viesť ku vzniku výnimky, ak sa
		 * programátor pokúsi použiť vrátenú hodnotu bez overenia.</p>
		 * 
		 * <p>Ak už je definovaná inštancia vlnenia, tak ju volanie tejto
		 * metódy zresetuje upokojením hladiny a nastavením zadanej úrovne
		 * útlmu.
		 * (Aktivácia je vykonaná v každom prípade.)</p>
		 * 
		 * @param útlm požadovaná úroveň útlmu vlnenia; odporúčané sú
		 *     hodnoty v rozmedzí 0 – 30; pozri aj {@link Vlnenie#útlm(int)
		 *     Vlnenie.útlm(útlm)}
		 * @param ajČasovač ak je hodnota tohto parametra rovná
		 *     {@code valtrue}, tak je v prípade jeho nečinnosti
		 *     automaticky {@linkplain Svet#spustiČasovač()
		 *     spustený časovač}
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #odstráňVlnenie()
		 * @see Vlnenie#útlm(int)
		 */
		public void pridajVlnenie(int útlm, boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(this, útlm);
				vlnenie.posun(posunX, posunY);
				Vlnenie.vlnenia.add(vlnenie);
				if (ajČasovač) Svet.spustiČasovač();
			}
			else
			{
				vlnenie.upokojHladinu();
				vlnenie.útlm(útlm);
			}
			vlnenie.aktivuj();
		}

		/**
		 * <p>Ukončí vlnenie a úplne odstráni inštanciu vlnenia tohto obrázka
		 * z prostredia programovacieho rámca GRobot.</p>
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 */
		public void odstráňVlnenie()
		{
			if (null != vlnenie)
			{
				Vlnenie.vlnenia.remove(vlnenie);
				vlnenie = null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #odstráňVlnenie() odstráňVlnenie}.</p> */
		public void odstranVlnenie() { odstráňVlnenie(); }


	// Animácia

		// Táto vnútorná súkromná trieda uchováva informácie o jedninej snímke
		// animácie tohto obrázka
		private class Snímka extends BufferedImage
		{
			/**
			 * <p>Tento atribút reprezentuje individuálnu dĺžku zobrazenia tejto
			 * snímky. (Je verejne upravovateľný.)</p>
			 */
			public int trvanie;

			/** <p>Konštruktor s predvolenou grafikou a trvaním.</p> */
			public Snímka(Image obrázok, int trvanie)
			{
				super(šírka, výška, BufferedImage.TYPE_INT_ARGB);

				int x = (šírka - obrázok.getWidth(null)) / 2;
				int y = (výška - obrázok.getHeight(null)) / 2;

				Graphics2D grafika = this.createGraphics();
				grafika.addRenderingHints(hints);
				grafika.drawImage(obrázok, x, y, null);

				this.trvanie = trvanie;
			}

			/** <p>Konštruktor s predvolenou grafikou a nulovým trvaním.</p> */
			public Snímka(Image obrázok)
			{
				super(šírka, výška, BufferedImage.TYPE_INT_ARGB);

				int x = (šírka - obrázok.getWidth(null)) / 2;
				int y = (výška - obrázok.getHeight(null)) / 2;

				Graphics2D grafika = this.createGraphics();
				grafika.addRenderingHints(hints);
				grafika.drawImage(obrázok, x, y, null);

				trvanie = 0;
			}

			/** <p>Konštruktor s prázdnou grafikou a trvaním.</p> */
			public Snímka(int trvanie)
			{
				super(šírka, výška, BufferedImage.TYPE_INT_ARGB);
				this.trvanie = trvanie;
			}

			/** <p>Konštruktor s prázdnou grafikou a nulovým trvaním.</p> */
			public Snímka()
			{
				super(šírka, výška, BufferedImage.TYPE_INT_ARGB);
				trvanie = 0;
			}
		}


		// Zoznam snímok tohto obrázka:
		private final Vector<Snímka> snímky = new Vector<>();

		// Index aktívnej snímky:
		private int snímka = -1;

		// Aktuálna hodnota na nastavenie dĺžky zobrazenia snímky:
		private int trvanie = 0;

		// Počet opakovaní animácie do ukončenia animácie:
		private int opakuj = -1;

		// Príznak spätného prehrávania animácie
		private boolean pospiatku = false;

		// Zrýchlenie alebo spomalenie animácie:
		private double rýchlosť = 1.0;

		// Časová pečiatka, po dosiahnutí ktorej má animácia prejsť na ďalšiu
		// snímku:
		private long čas = 0;

		/**
		 * <p>Vráti aktuálnu rýchlosť prehrávania animácie (bez ohľadu na to,
		 * či je práve animácia spustená alebo nie). Rýchlosť je vyjadrená
		 * pomerne. Hodnota {@code num1.0} znamená skutočnú rýchlosť určenú
		 * hodnotami {@linkplain #trvanie() trvania zobrazenia} snímok
		 * animácie, {@code num2.0} znamená dvojnásobnú rýchlosť a tak
		 * podobne. Záporná hodnota znamená, že animácia je prehrávaná
		 * pospiatku.</p>
		 * 
		 * @return aktuálna rýchlosť prehrávania animácie
		 */
		public double rýchlosť()
		{
			return pospiatku ? -rýchlosť : rýchlosť;
		}

		/** <p><a class="alias"></a> Alias pre {@link #rýchlosť() rýchlosť}.</p> */
		public double rychlost() { return rýchlosť(); }

		/**
		 * <p>Vráti aktuálnu rýchlosť prehrávania animácie (bez ohľadu na to,
		 * či je práve animácia spustená alebo nie). Rýchlosť je vyjadrená
		 * pomerne. Hodnota {@code num1.0} znamená skutočnú rýchlosť určenú
		 * hodnotami {@linkplain #trvanie() trvania zobrazenia} snímok
		 * animácie, {@code num2.0} znamená dvojnásobnú rýchlosť a tak
		 * podobne. Záporná hodnota znamená, že animácia je prehrávaná
		 * pospiatku.</p>
		 * 
		 * @return aktuálna rýchlosť prehrávania animácie
		 */
		public double rýchlosťAnimácie()
		{
			return pospiatku ? -rýchlosť : rýchlosť;
		}

		/** <p><a class="alias"></a> Alias pre {@link #rýchlosťAnimácie() rýchlosťAnimácie}.</p> */
		public double rychlostAnimacie() { return rýchlosťAnimácie(); }

		/**
		 * <p>Upraví rýchlosť prehrávania animácie (bez ohľadu na to, či je
		 * práve animácia spustená alebo nie). Rýchlosť je vyjadrená pomerne.
		 * Hodnota {@code num1.0} znamená skutočnú rýchlosť určenú hodnotami
		 * {@linkplain #trvanie() trvania zobrazenia} snímok animácie,
		 * {@code num2.0} znamená dvojnásobnú rýchlosť a tak podobne. Záporná
		 * hodnota znamená, že animácia bude prehrávaná pospiatku a nula
		 * znamená, že animácia má byť zastavená (pričom pôvodná rýchlosť
		 * prehrávania <b>nebude zmenená</b>).</p>
		 * 
		 * @param nováRýchlosť nová rýchlosť animácie; záporná hodnota
		 *     znamená smer prehrávania animácie pospiatku; nulová hodnota
		 *     nezmení skutočnú rýchlosť prehrávania, ale len zastaví
		 *     vykonávanie animácie (ak prebiehalo)
		 */
		public void rýchlosť(double nováRýchlosť)
		{
			if (nováRýchlosť > 0)
			{
				pospiatku = false;
				rýchlosť = nováRýchlosť;
				nastavČas();
			}
			else if (0 == nováRýchlosť)
				zastav();
			else
			{
				pospiatku = true;
				rýchlosť = -nováRýchlosť;
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #rýchlosť(double) rýchlosť}.</p> */
		public void rychlost(double nováRýchlosť) { rýchlosť(nováRýchlosť); }

		/**
		 * <p>Upraví rýchlosť prehrávania animácie (bez ohľadu na to, či je
		 * práve animácia spustená alebo nie). Rýchlosť je vyjadrená pomerne.
		 * Hodnota {@code num1.0} znamená skutočnú rýchlosť určenú hodnotami
		 * {@linkplain #trvanie() trvania zobrazenia} snímok animácie,
		 * {@code num2.0} znamená dvojnásobnú rýchlosť a tak podobne. Záporná
		 * hodnota znamená, že animácia bude prehrávaná pospiatku a nula
		 * znamená, že animácia má byť zastavená (pričom pôvodná rýchlosť
		 * prehrávania <b>nebude zmenená</b>).</p>
		 * 
		 * @param nováRýchlosť nová rýchlosť animácie; záporná hodnota
		 *     znamená smer prehrávania animácie pospiatku; nulová hodnota
		 *     nezmení skutočnú rýchlosť prehrávania, ale len zastaví
		 *     vykonávanie animácie (ak prebiehalo)
		 */
		public void rýchlosťAnimácie(double nováRýchlosť)
		{
			if (nováRýchlosť > 0)
			{
				pospiatku = false;
				rýchlosť = nováRýchlosť;
				nastavČas();
			}
			else if (0 == nováRýchlosť)
				zastav();
			else
			{
				pospiatku = true;
				rýchlosť = -nováRýchlosť;
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #rýchlosťAnimácie(double) rýchlosťAnimácie}.</p> */
		public void rychlostAnimacie(double nováRýchlosť)
		{ rýchlosťAnimácie(nováRýchlosť); }


		// Overí správnosť hodnoty indexu snímky. Pozor! Vektor „snímky“
		// nesmie byť prázdny, inak vznikne delenie nulou…
		private int overIndexSnímky(int index)
		{
			if (index >= snímky.size())
				index = index % snímky.size();
			else if (index < 0)
			{
				index = (snímky.size() +
					((index + 1) % snímky.size()) - 1);
				if (index < 0) index = 0;
			}
			return index;
		}


		/**
		 * <p>Vráti aktuálny počet snímok animácie uloženej v tomto obrázku.
		 * Nula znamená, že tento obrázok nie je animovaný.</p>
		 * 
		 * <p>Každá nová snímka je vnútorne reprezentovaná ako samostatná
		 * kópia grafickej informácie obrázka, ktorá môže byť prenesená
		 * z alebo do hlavnej grafiky obrázka. Tento proces prenosu môže byť
		 * jednostranný (počas animácie) alebo obojstranný (pri zmene snímky
		 * alebo pri ukladaní alebo čítaní obrázka zo súboru). To znamená, že
		 * ak nie je spustená animácia, tak sa medzi snímkami môžeme posúvať
		 * metódami {@link #prváSnímka prváSnímka}, {@link #ďalšiaSnímka
		 * ďalšiaSnímka}, {@link #predchádzajúcaSnímka predchádzajúcaSnímka},
		 * {@link #poslednáSnímka predchádzajúcaSnímka} a {@link #snímka(int)
		 * snímka(ktorá)} a vtedy je zaručené, že sa pri posune medzi snímkami
		 * zároveň stihnú aktualizovať grafické informácie, ktoré boli
		 * medzičasom vykonané v hlavnej grafike obrázka. Naopak, ak je
		 * animácia spustená, môže kedykoľvek nastať prepísanie hlavnej
		 * grafiky grafikou niektorej snímky a prípadné zmeny sa nemusia
		 * stihnúť uložiť. Počas procesu animácie (ktorý je automaticky
		 * vykonávaný na pozadí) sa totiž údaje z hlavnej grafiky
		 * z optimalizačných dôvodov neuchovávajú späť do snímok.</p>
		 * 
		 * @return aktuálny počet snímok animácie uložených v tomto obrázku
		 */
		public int početSnímok()
		{
			return snímky.size();
		}

		/** <p><a class="alias"></a> Alias pre {@link #početSnímok() početSnímok}.</p> */
		public int pocetSnimok() { return početSnímok(); }


		/**
		 * <p>Pridá novú snímku na koniec animácie s aktuálnou (naposledy
		 * použitou) hodnotou {@linkplain #trvanie() trvania} zobrazenia
		 * a s aktuálnou grafikou obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 */
		public void pridajSnímku()
		{
			snímky.add(new Snímka(this, trvanie));
			snímka = snímky.size() - 1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajSnímku() pridajSnímku}.</p> */
		public void pridajSnimku() { pridajSnímku(); }

		/**
		 * <p>Pridá novú snímku na koniec animácie s aktuálnou (naposledy
		 * použitou) hodnotou {@linkplain #trvanie() trvania} zobrazenia
		 * a so zadanou grafikou.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param obrázok grafika novej snímky
		 */
		public void pridajSnímku(Image obrázok)
		{
			snímky.add(new Snímka(obrázok, trvanie));
			dajGrafikuSnímky(snímka = snímky.size() - 1);
			nastavČas();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajSnímku(Image) pridajSnímku}.</p> */
		public void pridajSnimku(Image obrázok) { pridajSnímku(obrázok); }

		/**
		 * <p>Pridá novú snímku na koniec animácie so zadanou grafikou a so
		 * zadanou hodnotou trvania jej zobrazenia.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param obrázok grafika novej snímky
		 * @param trvanie čas zobrazenia snímky v sekundách s presnosťou
		 *     na tisíciny
		 */
		public void pridajSnímku(Image obrázok, double trvanie)
		{
			if (trvanie < 0.0) trvanie = 0.0;
			this.trvanie = (int)(trvanie * 1000.0);
			snímky.add(new Snímka(obrázok, this.trvanie));
			dajGrafikuSnímky(snímka = snímky.size() - 1);
			nastavČas();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajSnímku(Image, double) pridajSnímku}.</p> */
		public void pridajSnimku(Image obrázok, double trvanie)
		{ pridajSnímku(obrázok, trvanie); }


		/**
		 * <p>Vloží novú snímku animácie na zadanú pozíciu s aktuálnou
		 * (naposledy použitou) hodnotou {@linkplain #trvanie() trvania}
		 * zobrazenia a s aktuálnou grafikou obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 */
		public void vložSnímku(int indexSnímky)
		{
			if (snímky.isEmpty()) pridajSnímku(); else
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				snímky.add(indexSnímky, new Snímka(this, trvanie));
				snímka = indexSnímky;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vložSnímku(int) vložSnímku}.</p> */
		public void vlozSnimku(int indexSnímky) { vložSnímku(indexSnímky); }

		/**
		 * <p>Vloží novú snímku animácie na zadanú pozíciu s aktuálnou
		 * (naposledy použitou) hodnotou {@linkplain #trvanie() trvania}
		 * zobrazenia a so zadanou grafikou.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @param obrázok grafika novej snímky
		 */
		public void vložSnímku(int indexSnímky, Image obrázok)
		{
			if (snímky.isEmpty()) pridajSnímku(obrázok); else
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				snímky.add(indexSnímky, new Snímka(obrázok, trvanie));
				dajGrafikuSnímky(snímka = indexSnímky);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vložSnímku(int, Image) vložSnímku}.</p> */
		public void vlozSnimku(int indexSnímky, Image obrázok)
		{ vložSnímku(indexSnímky, obrázok); }

		/**
		 * <p>Vloží novú snímku animácie na zadanú pozíciu so zadanou grafikou
		 * a so zadanou hodnotou trvania jej zobrazenia.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @param obrázok grafika novej snímky
		 * @param trvanie čas zobrazenia snímky v sekundách s presnosťou
		 *     na tisíciny
		 */
		public void vložSnímku(int indexSnímky, Image obrázok, double trvanie)
		{
			if (snímky.isEmpty()) pridajSnímku(obrázok, trvanie); else
			{
				if (trvanie < 0.0) trvanie = 0.0;
				this.trvanie = (int)(trvanie * 1000.0);
				indexSnímky = overIndexSnímky(indexSnímky);
				snímky.add(indexSnímky, new Snímka(obrázok, this.trvanie));
				dajGrafikuSnímky(snímka = indexSnímky);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vložSnímku(int, Image, double) vložSnímku}.</p> */
		public void vlozSnimku(int indexSnímky, Image obrázok, double trvanie)
		{ vložSnímku(indexSnímky, obrázok, trvanie); }

		/**
		 * <p>Vymaže aktuálnu snímku animácie a aktivuje najbližšiu dostupnú
		 * snímku. Ak to bola posledná snímka v obrázku, tak zostane nastavená
		 * jej grafika a prípadná prebiehajúca animácia sa zastaví.</p>
		 */
		public void vymažSnímku()
		{
			if (!snímky.isEmpty())
			{
				// uložGrafikuSnímky(snímka);
				snímky.remove(snímka);
				if (snímky.isEmpty())
				{
					zastav();
					snímka = -1;
				}
				else
				{
					snímka = overIndexSnímky(snímka);
					dajGrafikuSnímky(snímka);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSnímku() vymažSnímku}.</p> */
		public void vymazSnimku() { vymažSnímku(); }

		/**
		 * <p>Vymaže zadanú snímku animácie a ak to bola aktívna snímka,
		 * tak aktivuje najbližšiu dostupnú snímku. Ak to bola posledná
		 * zostávajúca snímka v tomto obrázku, tak zostane nastavená jej
		 * grafika a prípadná prebiehajúca animácia sa zastaví.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 */
		public void vymažSnímku(int indexSnímky)
		{
			if (!snímky.isEmpty())
			{
				// uložGrafikuSnímky(snímka);
				indexSnímky = overIndexSnímky(indexSnímky);
				snímky.remove(indexSnímky);

				if (snímky.isEmpty())
				{
					zastav();
					snímka = -1;
				}
				else
				{
					if (snímka == indexSnímky)
					{
						snímka = overIndexSnímky(snímka);
						dajGrafikuSnímky(snímka);
						nastavČas();
					}
					else if (snímka > indexSnímky) --snímka;
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSnímku(int) vymažSnímku}.</p> */
		public void vymazSnimku(int indexSnímky) { vymažSnímku(indexSnímky); }


		/**
		 * <p>Aktivuje nasledujúcu snímku animácie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 */
		public void ďalšiaSnímka()
		{
			if (!snímky.isEmpty())
			{
				uložGrafikuSnímky(snímka);
				if (++snímka >= snímky.size()) snímka = 0;
				dajGrafikuSnímky(snímka);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #ďalšiaSnímka() ďalšiaSnímka}.</p> */
		public void dalsiaSnimka() { ďalšiaSnímka(); }

		/**
		 * <p>Aktivuje predchádzajúcu snímku animácie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 */
		public void predchádzajúcaSnímka()
		{
			if (!snímky.isEmpty())
			{
				uložGrafikuSnímky(snímka);
				if (--snímka < 0) snímka = snímky.size() - 1;
				dajGrafikuSnímky(snímka);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #predchádzajúcaSnímka() predchádzajúcaSnímka}.</p> */
		public void predchadzajucaSnimka() { predchádzajúcaSnímka(); }

		/**
		 * <p>Aktivuje prvú snímku animácie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 */
		public void prváSnímka()
		{
			if (!snímky.isEmpty() && 0 != snímka)
			{
				uložGrafikuSnímky(snímka);
				snímka = 0;
				dajGrafikuSnímky(snímka);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prváSnímka() prváSnímka}.</p> */
		public void prvaSnimka() { prváSnímka(); }

		/**
		 * <p>Aktivuje poslednú snímku animácie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 */
		public void poslednáSnímka()
		{
			if (!snímky.isEmpty() && snímky.size() - 1 != snímka)
			{
				uložGrafikuSnímky(snímka);
				snímka = snímky.size() - 1;
				dajGrafikuSnímky(snímka);
				nastavČas();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #poslednáSnímka() poslednáSnímka}.</p> */
		public void poslednaSnimka() { poslednáSnímka(); }

		/**
		 * <p>Aktivuje zadanú snímku animácie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok}.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 */
		public void snímka(int indexSnímky)
		{
			if (!snímky.isEmpty())
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				if (indexSnímky != snímka)
				{
					uložGrafikuSnímky(snímka);
					snímka = indexSnímky;
					dajGrafikuSnímky(snímka);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #snímka(int) snímka}.</p> */
		public void snimka(int indexSnímky) { snímka(indexSnímky); }

		/**
		 * <p>Vráti index aktívnej snímky animácie.</p>
		 * 
		 * @return index aktívnej (aktuálne zvolenej) snímky animácie
		 */
		public int snímka() { return snímka; }

		/** <p><a class="alias"></a> Alias pre {@link #snímka() snímka}.</p> */
		public int snimka() { return snímka(); }


		/**
		 * <p>Zistí hodnotu trvania zobrazenia aktívnej snímky. (Prípadne
		 * aktuálnu naposledy použitú hodnotu trvania, ak animácia nemá
		 * žiadne snímky.)</p>
		 * 
		 * @return trvanie zobrazenia aktívnej snímky počas animácie
		 *     v sekundách s presnosťou na tisíciny (pri jednotkovej
		 *     rýchlosti prehrávania)
		 */
		public double trvanie()
		{
			if (!snímky.isEmpty())
			{
				Snímka snímka = snímky.get(this.snímka);
				return snímka.trvanie * 1000.0;
			}

			return trvanie * 1000.0;
		}

		/**
		 * <p>Zistí hodnotu trvania zobrazenia zadanej snímky. (Prípadne
		 * aktuálnu naposledy použitú hodnotu trvania, ak animácia nemá
		 * žiadne snímky.)</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @return trvanie zobrazenia cieľovej snímky počas animácie
		 *     v sekundách s presnosťou na tisíciny (pri jednotkovej
		 *     rýchlosti prehrávania)
		 */
		public double trvanie(int indexSnímky)
		{
			if (!snímky.isEmpty())
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				Snímka snímka = snímky.get(indexSnímky);
				return snímka.trvanie * 1000.0;
			}

			return trvanie * 1000.0;
		}

		/**
		 * <p>Zistí hodnotu trvania zobrazenia aktívnej snímky. (Prípadne
		 * aktuálnu naposledy použitú hodnotu trvania, ak animácia nemá
		 * žiadne snímky.)</p>
		 * 
		 * @return trvanie zobrazenia aktívnej snímky počas animácie
		 *     v sekundách s presnosťou na tisíciny (pri jednotkovej
		 *     rýchlosti prehrávania)
		 */
		public double trvanieZobrazeniaSnímky()
		{
			if (!snímky.isEmpty())
			{
				Snímka snímka = snímky.get(this.snímka);
				return snímka.trvanie * 1000.0;
			}

			return trvanie * 1000.0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #trvanieZobrazeniaSnímky() trvanieZobrazeniaSnímky}.</p> */
		public double trvanieZobrazeniaSnimky()
		{ return trvanieZobrazeniaSnímky(); }

		/**
		 * <p>Zistí hodnotu trvania zobrazenia zadanej snímky. (Prípadne
		 * aktuálnu naposledy použitú hodnotu trvania, ak animácia nemá
		 * žiadne snímky.)</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @return trvanie zobrazenia cieľovej snímky počas animácie
		 *     v sekundách s presnosťou na tisíciny (pri jednotkovej
		 *     rýchlosti prehrávania)
		 */
		public double trvanieZobrazeniaSnímky(int indexSnímky)
		{
			if (!snímky.isEmpty())
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				Snímka snímka = snímky.get(indexSnímky);
				return snímka.trvanie * 1000.0;
			}

			return trvanie * 1000.0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #trvanieZobrazeniaSnímky(int) trvanieZobrazeniaSnímky}.</p> */
		public double trvanieZobrazeniaSnimky(int indexSnímky)
		{ return trvanieZobrazeniaSnímky(indexSnímky); }


		/**
		 * <p>Nastaví novú hodnotu trvania aktívnej snímky.</p>
		 * 
		 * @param trvanie nový čas zobrazenia aktívnej snímky v sekundách
		 *     s presnosťou na tisíciny
		 */
		public void trvanie(double trvanie)
		{
			if (trvanie < 0.0) trvanie = 0.0;
			this.trvanie = (int)(trvanie * 1000.0);

			if (!snímky.isEmpty())
			{
				Snímka snímka = snímky.get(this.snímka);
				snímka.trvanie = this.trvanie;
			}
		}

		/**
		 * <p>Nastaví novú hodnotu trvania zadanej snímky.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @param trvanie nový čas zobrazenia cieľovej snímky v sekundách
		 *     s presnosťou na tisíciny
		 */
		public void trvanie(int indexSnímky, double trvanie)
		{
			if (trvanie < 0.0) trvanie = 0.0;
			this.trvanie = (int)(trvanie * 1000.0);

			if (!snímky.isEmpty())
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				Snímka snímka = snímky.get(indexSnímky);
				snímka.trvanie = this.trvanie;
			}
		}

		/**
		 * <p>Nastaví novú hodnotu trvania aktívnej snímky.</p>
		 * 
		 * @param trvanie nový čas zobrazenia aktívnej snímky v sekundách
		 *     s presnosťou na tisíciny
		 */
		public void trvanieZobrazeniaSnímky(double trvanie)
		{
			if (trvanie < 0.0) trvanie = 0.0;
			this.trvanie = (int)(trvanie * 1000.0);

			if (!snímky.isEmpty())
			{
				Snímka snímka = snímky.get(this.snímka);
				snímka.trvanie = this.trvanie;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #trvanieZobrazeniaSnímky(double) trvanieZobrazeniaSnímky}.</p> */
		public void trvanieZobrazeniaSnimky(double trvanie)
		{ trvanieZobrazeniaSnímky(trvanie); }

		/**
		 * <p>Nastaví novú hodnotu trvania zadanej snímky.</p>
		 * 
		 * @param indexSnímky index cieľovej snímky; ak je táto hodnota
		 *     väčšia než je aktuálny počet snímok, tak bude prepočítaná
		 *     tak, aby ukazovala na adekvátne vzdialenú snímku v rámci
		 *     cyklickej animácie (čiže hodnota rovná počtu snímok bude
		 *     presmerovaná na prvú snímku, hodnota o jedno vyššia na druhú
		 *     a tak ďalej); záporné hodnoty budú prepočítané v opačnom
		 *     smere (od konca animácie)
		 * @param trvanie nový čas zobrazenia cieľovej snímky v sekundách
		 *     s presnosťou na tisíciny
		 */
		public void trvanieZobrazeniaSnímky(int indexSnímky, double trvanie)
		{
			if (trvanie < 0.0) trvanie = 0.0;
			this.trvanie = (int)(trvanie * 1000.0);

			if (!snímky.isEmpty())
			{
				indexSnímky = overIndexSnímky(indexSnímky);
				Snímka snímka = snímky.get(indexSnímky);
				snímka.trvanie = this.trvanie;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #trvanieZobrazeniaSnímky(int, double) trvanieZobrazeniaSnímky}.</p> */
		public void trvanieZobrazeniaSnimky(int indexSnímky, double trvanie)
		{ trvanieZobrazeniaSnímky(indexSnímky, trvanie); }


		// Nastaví čas zobrazenia aktívnej snímky podľa trvania a rýchlosti
		private long nastavČas()
		{
			Snímka snímka = snímky.get(this.snímka);
			long trvanie;

			if (1.0 == rýchlosť)
				trvanie = snímka.trvanie;
			else
				trvanie = (long)((double)snímka.trvanie / rýchlosť);

			čas = System.currentTimeMillis() + trvanie;
			return trvanie;
		}


		/**
		 * <p>Zistí, či je práve spustená animácia obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @return {@code valtrue} ak je práve spustená animácia obrázka,
		 *     {@code valfalse} v opačnom prípade
		 */
		public boolean animujeSa() { return animácie.contains(this); }


		/**
		 * <p>Zistí, koľko opakovaní zostáva do ukončenia animácie. Hodnota
		 * {@code num-1} znamená, že animácia má byť prehrávaná
		 * donekonečna.</p>
		 * 
		 * @return počet opakovaní zostávajúci do ukončenia animácie alebo
		 *     {@code num-1} v prípade nekonečne sa opakujúcej animácie
		 */
		public int opakovaniaAnimácie() { return opakuj; }

		/** <p><a class="alias"></a> Alias pre {@link #opakovaniaAnimácie() opakovaniaAnimácie}.</p> */
		public int opakovaniaAnimacie() { return opakovaniaAnimácie(); }

		/**
		 * <p>Zistí, koľko opakovaní zostáva do ukončenia animácie. Hodnota
		 * {@code num-1} znamená, že animácia má byť prehrávaná
		 * donekonečna.</p>
		 * 
		 * @return počet opakovaní zostávajúci do ukončenia animácie alebo
		 *     {@code num-1} v prípade nekonečne sa opakujúcej animácie
		 */
		public int početOpakovaní() { return opakuj; }

		/** <p><a class="alias"></a> Alias pre {@link #početOpakovaní() početOpakovaní}.</p> */
		public int pocetOpakovani() { return početOpakovaní(); }


		/**
		 * <p>Ak má tento obrázok nenulový počet snímok animácie, tak táto
		 * metóda nastaví nový počet opakovaní animácie zostávajúci do
		 * ukončenia animácie a to bez ohľadu na to, či je animácia spustená
		 * alebo nie. Hodnota {@code num-1} znamená, že animácia má byť
		 * prehrávaná donekonečna. Ak je zadaná nula, tak prípadná spustená
		 * animácia bude zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void opakovaniaAnimácie(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #opakovaniaAnimácie(int) opakovaniaAnimácie}.</p> */
		public void opakovaniaAnimacie(int početOpakovaní) { opakovaniaAnimácie(početOpakovaní); }

		/**
		 * <p>Ak má tento obrázok nenulový počet snímok animácie, tak táto
		 * metóda nastaví nový počet opakovaní animácie zostávajúci do
		 * ukončenia animácie a to bez ohľadu na to, či je animácia spustená
		 * alebo nie. Hodnota {@code num-1} znamená, že animácia má byť
		 * prehrávaná donekonečna. Ak je zadaná nula, tak prípadná spustená
		 * animácia bude zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void početOpakovaní(int početOpakovaní)
		{
			opakuj = početOpakovaní;
		}

		/** <p><a class="alias"></a> Alias pre {@link #početOpakovaní(int) početOpakovaní}.</p> */
		public void pocetOpakovani(int početOpakovaní) { početOpakovaní(početOpakovaní); }


		/**
		 * <p>Spustí animáciu s nekonečným počtom opakovaní v naposledy
		 * použitom smere prehrávania. Ak bola animácia už spustená, tak iba
		 * nastaví nekonečný počet opakovaní.</p>
		 */
		public void spusti()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu s nekonečným počtom opakovaní v naposledy
		 * použitom smere prehrávania. Ak bola animácia už spustená, tak iba
		 * nastaví nekonečný počet opakovaní.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 */
		public void spustiAnimáciu()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciu() spustiAnimáciu}.</p> */
		public void spustiAnimaciu() { spustiAnimáciu(); }

		/**
		 * <p>Spustí animáciu so zadaným počtom opakovaní v naposledy použitom
		 * smere prehrávania. Ak bola animácia už spustená, tak iba nastaví
		 * zadaný počet opakovaní. Ak je zadaný počet opakovaní rovný nule,
		 * tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spusti(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu so zadaným počtom opakovaní v naposledy použitom
		 * smere prehrávania. Ak bola animácia už spustená, tak iba nastaví
		 * zadaný počet opakovaní. Ak je zadaný počet opakovaní rovný nule,
		 * tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spustiAnimáciu(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciu(int) spustiAnimáciu}.</p> */
		public void spustiAnimaciu(int početOpakovaní)
		{ spustiAnimáciu(početOpakovaní); }


		/**
		 * <p>Spustí animáciu dopredu s nekonečným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania a nastaví
		 * nekonečný počet opakovaní.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 */
		public void spustiDopredu()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;
				pospiatku = false;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu dopredu s nekonečným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania a nastaví
		 * nekonečný počet opakovaní.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 */
		public void spustiAnimáciuDopredu()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;
				pospiatku = false;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciuDopredu() spustiAnimáciuDopredu}.</p> */
		public void spustiAnimaciuDopredu() { spustiAnimáciuDopredu(); }

		/**
		 * <p>Spustí animáciu dopredu so zadaným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania a nastaví
		 * zadaný počet opakovaní. Ak je zadaný počet opakovaní rovný nule,
		 * tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spustiDopredu(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				pospiatku = false;
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu dopredu so zadaným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania a nastaví
		 * zadaný počet opakovaní. Ak je zadaný počet opakovaní rovný nule,
		 * tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spustiAnimáciuDopredu(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				pospiatku = false;
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciuDopredu(int) spustiAnimáciuDopredu}.</p> */
		public void spustiAnimaciuDopredu(int početOpakovaní) { spustiAnimáciuDopredu(početOpakovaní); }


		/**
		 * <p>Spustí animáciu pospiatku s nekonečným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania (pospiatku)
		 * a nastaví nekonečný počet opakovaní.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 */
		public void spustiPospiatku()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;
				pospiatku = true;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu pospiatku s nekonečným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania (pospiatku)
		 * a nastaví nekonečný počet opakovaní.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 */
		public void spustiAnimáciuPospiatku()
		{
			if (!snímky.isEmpty())
			{
				opakuj = -1;
				pospiatku = true;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciuPospiatku() spustiAnimáciuPospiatku}.</p> */
		public void spustiAnimaciuPospiatku() { spustiAnimáciuPospiatku(); }

		/**
		 * <p>Spustí animáciu pospiatku so zadaným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania (pospiatku)
		 * a nastaví zadaný počet opakovaní. Ak je zadaný počet opakovaní
		 * rovný nule, tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spustiPospiatku(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				pospiatku = true;
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/**
		 * <p>Spustí animáciu pospiatku so zadaným počtom opakovaní. Ak bola
		 * animácia už spustená, tak iba upraví smer prehrávania (pospiatku)
		 * a nastaví zadaný počet opakovaní. Ak je zadaný počet opakovaní
		 * rovný nule, tak je animácia zastavená.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Počítadlo opakovaní sa
		 * vzťahuje na momenty reštartovania animácie, to znamená, že hodnota
		 * počítadla je znížená po prejdení animácie z konca na začiatok alebo
		 * naopak, podľa smeru prehrávania animácie. To znamená, že ak nie je
		 * nastavená správna snímka (začiatočná alebo koncová podľa smeru
		 * prehrávania), tak sa môže animácia skončiť hneď po jej spustení
		 * (ak malo byť vykonané len jedno opakovanie) alebo sa nevykoná celá,
		 * podľa toho, ktorá snímka bola práve aktívna.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * @param početOpakovaní počet opakovaní animácie, po ktorom sa
		 *     animácia ukončí; hodnota {@code num-1} je rezervovaná pre
		 *     nekonečný počet opakovaní (v skutočnosti akákoľvek zadaná
		 *     záporná hodnota nastaví počet opakovaní na hodnotu
		 *     {@code num-1}); hodnota {@code num0} spôsobí zastavenie
		 *     animácie (ak náhodou prebieha)
		 */
		public void spustiAnimáciuPospiatku(int početOpakovaní)
		{
			if (!snímky.isEmpty())
			{
				pospiatku = true;
				if (početOpakovaní <= -1) opakuj = -1;
				else if (0 == početOpakovaní)
				{
					opakuj = 0;
					zastav();
					return;
				}
				else opakuj = početOpakovaní;

				if (!animácie.contains(this))
				{
					snímka = overIndexSnímky(snímka);
					uložGrafikuSnímky(snímka);
					animácie.add(this);
					nastavČas();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiAnimáciuPospiatku(int) spustiAnimáciuPospiatku}.</p> */
		public void spustiAnimaciuPospiatku(int početOpakovaní) { spustiAnimáciuPospiatku(početOpakovaní); }


		/**
		 * <p>Zastaví prehrávanie animácie tohto obrázka.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozri aj informácie o snímkach
		 * uvedené v opise metódy {@link #početSnímok() početSnímok} (tieto
		 * informácie súvisia aj s vykonávaním animácie).</p>
		 * 
		 * <p>Táto metóda je vo viacerých prípadoch spúšťaná automaticky.
		 * Napríklad po dokončení všetkých {@linkplain #početOpakovaní()
		 * opakovaní animácie}, nastavení {@linkplain #opakovaniaAnimácie(int)
		 * počtu opakovaní} alebo {@linkplain #rýchlosť(double) rýchlosti
		 * prehrávania} animácie na nulu, {@linkplain #vymažSnímku(int)
		 * vymazaní} všetkých snímok animácie a podobne. Ak animácia
		 * prebiehala, tak vykonanie tejto metódy spustí prekryté reakcie
		 * robota {@link GRobot#zastavenieAnimácie(Obrázok)
		 * zastavenieAnimácie} a/alebo rovnomennú reakciu aktívnej obsluhy
		 * udalostí – {@link ObsluhaUdalostí#zastavenieAnimácie(Obrázok)
		 * zastavenieAnimácie}.</p>
		 */
		public void zastav()
		{
			if (animácie.contains(this))
			{
				animácie.remove(this);

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.zastavenieAnimácie(this);
						ObsluhaUdalostí.počúvadlo.zastavenieAnimacie(this);
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciSystém)
					{
						počúvajúci.zastavenieAnimácie(this);
						počúvajúci.zastavenieAnimacie(this);
					}
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zastav() zastav}.</p> */
		public void zastavAnimáciu() { zastav(); }

		/** <p><a class="alias"></a> Alias pre {@link #zastav() zastav}.</p> */
		public void zastavAnimaciu() { zastav(); }


		// Skopíruje grafické údaje snímky s určeným indexom do grafiky tohto
		// obrázka.
		private void dajGrafikuSnímky(int index)
		{
			if (!snímky.isEmpty() && index >= 0 && index < snímky.size())
			{
				if (null == údajeObrázka) údajeObrázka =
					((DataBufferInt)getRaster().getDataBuffer()).getData();

				Snímka snímka = snímky.get(index);

				int[] údajeSnímky = ((DataBufferInt)snímka.
					getRaster().getDataBuffer()).getData();

				System.arraycopy(údajeSnímky, 0,
					údajeObrázka, 0, údajeSnímky.length);
			}
		}

		// Skopíruje grafiku tohto obrázka do grafických údajov snímky
		// s určeným indexom.
		private void uložGrafikuSnímky(int index)
		{
			if (!snímky.isEmpty() && index >= 0 && index < snímky.size())
			{
				if (null == údajeObrázka) údajeObrázka =
					((DataBufferInt)getRaster().getDataBuffer()).getData();

				Snímka snímka = snímky.get(index);

				int[] údajeSnímky = ((DataBufferInt)snímka.
					getRaster().getDataBuffer()).getData();

				System.arraycopy(údajeObrázka, 0,
					údajeSnímky, 0, údajeObrázka.length);
			}
		}

		// Overí hodnotu časovača a ak je to relevantné, tak posunie animáciu
		// na ďalšiu snímku… Pozor! Táto metóda neuloží zmeny, ktoré mohli
		// byť medzičasom vykonané v aktívnej snímke!
		/*packagePrivate*/ void animuj()
		{
			if (!snímky.isEmpty())
			{
				if (System.currentTimeMillis() >= čas)
				{
					long rozdiel = System.currentTimeMillis() - čas;
					try
					{
						if (pospiatku)
						{
							--this.snímka;
							if (this.snímka < 0 ||
								this.snímka >= snímky.size())
							{
								this.snímka = snímky.size() - 1;
								if (opakuj > 0)
								{
									--opakuj;
									if (opakuj <= 0)
									{
										zastav();
										return;
									}
								}
							}
						}
						else
						{
							++this.snímka;
							if (this.snímka < 0 ||
								this.snímka >= snímky.size())
							{
								this.snímka = 0;
								if (opakuj > 0)
								{
									--opakuj;
									if (opakuj <= 0)
									{
										zastav();
										return;
									}
								}
							}
						}
					}
					finally
					{
						dajGrafikuSnímky(this.snímka);
						long trvanie = nastavČas();
						if (trvanie > rozdiel) čas -= rozdiel;
						else čas -= trvanie;
						Svet.neboloPrekreslené = true;
					}
				}
			}
		}
}
