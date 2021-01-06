
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;

import java.awt.color.ColorSpace;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;

import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static knižnica.Farebnosť.*;

// ---------------------- //
//  *** Trieda Farba ***  //
// ---------------------- //

/**
 * <p>Trieda je určená na prácu s farbami v rámci programovacieho rámca
 * GRobot. Všetky farby, s ktorými robot pracuje sú zväčša určované
 * tromi alebo štyrmi celočíselnými údajmi v rozsahu od 0 do 255 – ide
 * o farebné zložky (červenej, zelenej a modrej) na namiešanie
 * výslednej farby a zložku (ne)priehľadnosti (0 znamená neviditeľná
 * farba, 255 znamená úplne nepriehľadná farba). Táto trieda umožňuje
 * definovať farby aj s pomocou neceločíselných rozsahov – od 0.0 po 1.0,
 * ktoré sú zase používané napríklad v metódach pracujúcich
 * s {@linkplain Plátno#priehľadnosť(double) (ne)priehľadnosťou plátna}.</p>
 * 
 * <p>Táto trieda obaľuje triedu Javy {@link Color Color}. Mierne
 * rozširuje a upravuje jej správanie, najmä v súvislosti so získavaním
 * farieb so {@linkplain #svetlejšia() svetlejším} alebo {@linkplain 
 * #tmavšia() tmavším} odtieňom, kde sa berie do úvahy aj úroveň
 * priehľadnosti, ktorú zachováva (na rozdiel od originálnej triedy).
 * Ďalšie vybrané metódy sú k dispozícii s názvami preloženými do
 * slovenského jazyka. Neceločíselné rozsahy ({@code num0.0} –
 * {@code num1.0}) typu {@code typefloat} umožňuje zadávať ako údajový
 * typ {@code typedouble}, pretože tento údajový typ je široko
 * používaný grafickým robotom a nevyžaduje špeciálnu syntax pri
 * vkladaní hodnôt. (Konkrétne tým trieda programátora oslobodzuje od
 * nevyhnutnosti používania operátora pretypovania: {@code (}{@code 
 * typefloat}{@code )}.)</p>
 * 
 * <p>Rovnako ako originálna trieda {@link Color Color} i trieda
 * {@code currFarba} pracuje buď v predvolenom sRGB farebnom priestore,
 * alebo vo farebnom priestore definovanom s pomocou triedy {@link 
 * ColorSpace ColorSpace}. Každá farba má vlastnú úroveň
 * (ne)priehľadnosti, ktorá je predvolene nastavená na «nepriehľadnú»
 * alebo môže byť určená v rámci povoleného rozsahu (pozri konštruktory
 * nižšie). V súlade s konvenciou triedy {@link Color Color} znamená
 * najvyššia hodnota atribútu „priehľadnosti“ (alebo skôr
 * nepriehľadnosti – {@link Color#getAlpha()}) úplne nepriehľadný
 * bod a naopak.</p>
 * 
 * <p>Množstvo predvolených farebných inštancií je definovaných v rozhraní
 * {@link Farebnosť Farebnosť}. Nasledujúci príklad ukazuje ich využitie
 * pri kreslení kruhov vyplnených náhodnou farbou:</p>
 * 
 * <pre CLASS="example">
	{@code comm// Zoznam farieb}
	{@code kwdfinal} {@code currFarba}[] farby = {
			{@link Farebnosť#svetločervená svetločervená}, {@link Farebnosť#červená červená}, {@link Farebnosť#tmavočervená tmavočervená}, {@link Farebnosť#svetlozelená svetlozelená}, {@link Farebnosť#zelená zelená},
			{@link Farebnosť#tmavozelená tmavozelená}, {@link Farebnosť#svetlomodrá svetlomodrá}, {@link Farebnosť#modrá modrá}, {@link Farebnosť#tmavomodrá tmavomodrá}, {@link Farebnosť#svetlotyrkysová svetlotyrkysová},
			{@link Farebnosť#tyrkysová tyrkysová}, {@link Farebnosť#tmavotyrkysová tmavotyrkysová}, {@link Farebnosť#svetlopurpurová svetlopurpurová}, {@link Farebnosť#purpurová purpurová},
			{@link Farebnosť#tmavopurpurová tmavopurpurová}, {@link Farebnosť#svetložltá svetložltá}, {@link Farebnosť#žltá žltá}, {@link Farebnosť#tmavožltá tmavožltá}, {@link Farebnosť#čierna čierna}, {@link Farebnosť#tmavošedá tmavošedá},
			{@link Farebnosť#šedá šedá}, {@link Farebnosť#svetlošedá svetlošedá}, {@link Farebnosť#biela biela}, {@link Farebnosť#svetlohnedá svetlohnedá}, {@link Farebnosť#hnedá hnedá}, {@link Farebnosť#tmavohnedá tmavohnedá},
			{@link Farebnosť#svetlooranžová svetlooranžová}, {@link Farebnosť#oranžová oranžová}, {@link Farebnosť#tmavooranžová tmavooranžová}, {@link Farebnosť#svetloružová svetloružová}, {@link Farebnosť#ružová ružová},
			{@link Farebnosť#tmavoružová tmavoružová},
		};

	{@link GRobot#skry() skry}();
	{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

	{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num1000}; ++i)
	{
		{@code comm// Náhodná poloha kruhu}
		{@code typedouble} skočNaX = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}(
			{@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}(), {@link Svet Svet}.{@link Svet#najväčšieX() najväčšieX}());
		{@code typedouble} skočNaY = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}(
			{@link Svet Svet}.{@link Svet#najmenšieY() najmenšieY}(), {@link Svet Svet}.{@link Svet#najväčšieY() najväčšieY}());

		{@code comm// Náhodná farba a veľkosť kruhu}
		{@code typeint} ktoráFarba = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long) náhodnéCeléČíslo}
			(farby.length &#45; {@code num1});
		{@code typedouble} akáVeľkosť = {@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num25}, {@code num50});

		{@code comm// Nakreslenie farebného kruhu}
		{@link GRobot#skočNa(double, double) skočNa}(skočNaX, skočNaY);
		{@link GRobot#farba(Color) farba}(farby[ktoráFarba]);
		{@link GRobot#kruh(double) kruh}(akáVeľkosť);

		{@code comm// Prekreslenie každú 10-tu iteráciu}
		{@code kwdif} (i % {@code num10} == {@code num0})
		{
			{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.050});
		}
	}

	{@link Svet Svet}.{@link Svet#kresli() kresli}();
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>nahodneKruhy.png<alt/>Náhodné farebné
 * kruhy.</image>Výsledok kreslenia príkladu.</p>
 * 
 * <p>V príklade je najprv definovaný zoznam farieb v poli, z ktorého sa
 * neskôr vyberá náhodná farba. {@linkplain GRobot#skry() Skryje} sa
 * aktuálny grafický robot a {@linkplain Svet#nekresli() vypne sa
 * automatické prekresľovanie} (aby bolo vykonanie príkladu rýchlejšie).
 * Potom sa tisíc ráz opakuje sekvencia príkazov na kreslenie kruhu
 * ({@code kwdfor}{@code  }{@code (i = }… {@code num1000}…). {@link 
 * GRobot#kruh(double) Kruh} sa má nakresliť na náhodnej pozícii, má byť
 * vyplnený náhodnou farbou (v rámci spomenutého zoznamu farieb) a má
 * mať náhodnú veľkosť (s polomerom medzi 25 až 50 bodov) – generovanie
 * náhodných súradníc, veľkosti a výber náhodnej farby sú z dôvodu
 * prehľadnosti oddelené od časti samotného kreslenia kruhu. V cykle sa
 * nachádza blok príkazov na {@linkplain Svet#prekresli() prekreslenie}
 * s podmienkou určujúcou, že (exaktne povedané) prekreslenie sa má
 * vykonať vždy, keď je riadiaca premenná cyklu {@code i} deliteľná
 * desiatimi bezo zvyšku ({@code i % }{@code num10}) – to znamená, že sa
 * vykoná každý desiaty raz. Pri prekreslení je program zároveň
 * {@linkplain Svet#čakaj(double) pozdržaný} o 50 milisekúnd – to by
 * znamenalo, že ak by prekreslenie nezaberalo určitý čas, tak by sa celý
 * program stihol vykonať za asi 5 sekúnd; v skutočnosti je to viac,
 * pretože prekresľovanie trvá postrehnuteľne dlhší čas. Nakoniec je
 * opätovne zapnuté {@linkplain Svet#kresli() automatické prekresľovanie}.</p>
 * 
 * <p>S novšími príkazmi triedy {@link GRobot GRobot} sa dá predchádzajúci
 * príklad skrátiť takto:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#skry() skry}();
	{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

	{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num1000}; ++i)
	{
		{@code comm// Voľba náhodnej veľkosti kruhu}
		{@code typedouble} polomer = {@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num25}, {@code num50});

		{@code comm// Nakreslenie farebného kruhu}
		{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
		{@link GRobot#náhodnáFarba() náhodnáFarba}();
		{@link GRobot#kruh(double) kruh}(polomer);

		{@code comm// Prekreslenie každú 10-tu iteráciu}
		{@code kwdif} (i % {@code num10} == {@code num0})
		{
			{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.050});
		}
	}

	{@link Svet Svet}.{@link Svet#kresli() kresli}();
	</pre>
 * 
 * @see Farebnosť
 * @see Color
 * @see java.awt.color.ColorSpace
 */
@SuppressWarnings("serial")
public class Farba extends Color implements Comparable<Color>
// -!- implements Farebnosť -!-
// Farba nesmie byt implementáciou farebnosti, inak by vznikali konflikty
// medzi metódami xyz(Color) a xyz(Farebnosť)!
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/color/ColorSpace.html


	// Interactive Color Wheel: http://r0k.us/graphics/SIHwheel.html

	/**
	 * <p>Vytvorí inštanciu farby z jestvujúcej inštancie triedy {@link 
	 * Color Color}.</p>
	 * 
	 * @param c objekt typu {@link Color Color} alebo odvodenej triedy
	 *     (napríklad {@link Farba Farba})
	 */
	public Farba(Color c)
	{
		super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * <p>Vytvorí novú inštanciu farby podľa farby zadaného objektu.</p>
	 * 
	 * @param o objekt, ktorého farba určí novú farbu
	 */
	public Farba(Farebnosť o)
	{
		super(o.farba().getRed(), o.farba().getGreen(),
			o.farba().getBlue(), o.farba().getAlpha());
	}

	/**
	 * <p>Vytvorí inštanciu farby v určenom farebnom priestore ({@link 
	 * ColorSpace ColorSpace}). Pre rôzne farebné priestory
	 * platia rôzne podmienky. Napríklad: RGB vyžaduje tri komponenty, ale
	 * CMYK štyri.</p>
	 * 
	 * <p><b><small>Tento konštruktor bol vytvorený na zabezpečenie úplnej
	 * kompatibility s triedou {@link Color}.</small></b></p>
	 * 
	 * @param cspace farebný priestor použitý na interpretáciu farebných
	 *     zložiek
	 * @param components potrebný počet farebných zložiek požadovaný
	 *     zadaným farebným priestorom
	 * @param alpha hodnota priehľadnosti (0.0 – neviditeľná farba; 1.0 –
	 *     nepriehľadná farba)
	 * 
	 * @exception IllegalArgumentException ak je hociktorá hodnota
	 *     farebnej zložky alebo priehľadnosti mimo povoleného rozsahu
	 *     0.0 – 1.0
	 * 
	 * @see java.awt.color.ColorSpace
	 */
	public Farba(ColorSpace cspace, float[] components, float alpha)
	{
		// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/color/ColorSpace.html
		super(cspace, components, alpha);
	}

	/**
	 * <p>Vytvorí nepriehľadnú farbu zo zložiek červená (r), zelená (g)
	 * a modrá (b) v rozsahu (0.0 – 1.0). Priehľadnosť je predvolene
	 * nastavená na hodnotu 1.0. Skutočná farba použitá pri kreslení
	 * závisí od výstupného zariadenia, pričom je zvolená taká farba, aby
	 * bol dosiahnutý čo najlepší výsledok.</p>
	 * 
	 * @param r červená zložka
	 * @param g zelená zložka
	 * @param b modrá zložka
	 * 
	 * @exception IllegalArgumentException ak je hodnota {@code r},
	 *     {@code g} alebo {@code b} mimo povoleného rozsahu 0.0 – 1.0
	 */
	public Farba(double r, double g, double b)
	{ super((float)r, (float)g, (float)b); }

	/**
	 * <p>Vytvorí farbu zo zložiek červená (r), zelená (g), modrá (b)
	 * a priehľadnosť (a) v rozsahu (0.0 – 1.0). Skutočná farba použitá
	 * pri kreslení závisí od výstupného zariadenia, pričom je zvolená
	 * taká farba, aby bol dosiahnutý čo najlepší výsledok.</p>
	 * 
	 * @param r červená zložka
	 * @param g zelená zložka
	 * @param b modrá zložka
	 * @param a priehľadnosť (0.0 – neviditeľná farba;
	 *     1.0 – nepriehľadná farba)
	 * 
	 * @exception IllegalArgumentException ak je hodnota {@code r},
	 *     {@code g}, {@code b} alebo {@code a} mimo povoleného rozsahu
	 *     0.0 – 1.0
	 */
	public Farba(double r, double g, double b, double a)
	{ super((float)r, (float)g, (float)b, (float)a); }

	/**
	 * <p>Vytvorí nepriehľadnú farbu vo farebnom priestore sRGB, pričom
	 * jednotlivé farebné zložky tvoria skupiny bitov: červená bity 16 –
	 * 23, zelená 8 – 15 a modrá 0 – 7. Priehľadnosť je predvolene
	 * nastavená na hodnotu 255. Skutočná farba použitá pri kreslení
	 * závisí od výstupného zariadenia, pričom je zvolená taká farba, aby
	 * bol dosiahnutý čo najlepší výsledok.</p>
	 * 
	 * @param rgb kombinácia zložiek RGB
	 */
	public Farba(int rgb) { super(rgb); }

	/**
	 * <p>Vytvorí farbu vo farebnom priestore sRGB, pričom jednotlivé
	 * farebné zložky a priehľadnosť tvoria skupiny bitov: červená bity
	 * 16 – 23, zelená 8 – 15, modrá 0 – 7 a priehľadnosť 24 – 31.
	 * Skutočná farba použitá pri kreslení závisí od výstupného
	 * zariadenia, pričom je zvolená taká farba, aby bol dosiahnutý čo
	 * najlepší výsledok. Ak je argument „hasalpha“ (voľný preklad – „má
	 * priehľadnosť“) rovný {@code valfalse}, tak je priehľadnosť
	 * nastavená na hodnotu 255 (nepriehľadná farba).</p>
	 * 
	 * @param rgba kombinácia zložiek ARGB
	 * @param hasalpha ak má byť množina bitov priehľadnosti (bity
	 *     číslo 24 až 31) vzatá do úvahy, tak musí byť hodnota
	 *     tohto argumentu rovná {@code valtrue}, inak bude použitá
	 *     hodnota priehľadnosti 255 bez ohľadu na hodnoty uvedených
	 *     bitov
	 */
	public Farba(int rgba, boolean hasalpha) { super(rgba, hasalpha); }

	/**
	 * <p>Vytvorí nepriehľadnú farbu vo farebnom priestore sRGB zo zložiek
	 * červená (r), zelená (g) a modrá (b) v rozsahu (0 – 255).
	 * Priehľadnosť je predvolene nastavená na hodnotu 255 (nepriehľadná
	 * farba). Skutočná farba použitá pri kreslení závisí od výstupného
	 * zariadenia, pričom je zvolená taká farba, aby bol dosiahnutý čo
	 * najlepší výsledok.</p>
	 * 
	 * @param r červená zložka
	 * @param g zelená zložka
	 * @param b modrá zložka
	 * 
	 * @exception IllegalArgumentException ak je hodnota {@code r},
	 *     {@code g} alebo {@code b} mimo povoleného rozsahu 0 – 255
	 */
	public Farba(int r, int g, int b) { super(r, g, b); }

	/**
	 * <p>Vytvorí farbu vo farebnom priestore sRGB zo zložiek červená (r),
	 * zelená (g), modrá (b) a priehľadnosť (a) v rozsahu (0 – 255).</p>
	 * 
	 * @param r červená zložka
	 * @param g zelená zložka
	 * @param b modrá zložka
	 * @param a priehľadnosť (0 – neviditeľná farba;
	 *     255 – nepriehľadná farba)
	 * 
	 * @exception IllegalArgumentException ak je hodnota {@code r},
	 *     {@code g}, {@code b} alebo {@code a} mimo povoleného rozsahu
	 *     0 – 255
	 */
	public Farba(int r, int g, int b, int a) { super(r, g, b, a); }


	/**
	 * <p><a class="getter"></a> Vráti červenú zložku v rozsahu 0 – 255.</p>
	 * 
	 * @return červená farebná zložka
	 */
	public int červená() { return getRed(); }

	/** <p><a class="alias"></a> Alias pre {@link #červená() červená}.</p> */
	public int cervena() { return getRed(); }

	/**
	 * <p><a class="getter"></a> Vráti zelenú zložku v rozsahu 0 – 255.</p>
	 * 
	 * @return zelená farebná zložka
	 */
	public int zelená() { return getGreen(); }

	/** <p><a class="alias"></a> Alias pre {@link #zelená() zelená}.</p> */
	public int zelena() { return getGreen(); }

	/**
	 * <p><a class="getter"></a> Vráti modrú zložku v rozsahu 0 – 255.</p>
	 * 
	 * @return modrá farebná zložka
	 */
	public int modrá() { return getBlue(); }

	/** <p><a class="alias"></a> Alias pre {@link #modrá() modrá}.</p> */
	public int modra() { return getBlue(); }

	/**
	 * <p><a class="getter"></a> Vráti úroveň priehľadnosti v rozsahu 0 – 255.
	 * (0 – neviditeľná farba; 255 – nepriehľadná farba)</p>
	 * 
	 * @return úroveň priehľadnosti
	 */
	public int priehľadnosť() { return getAlpha(); }

	/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť() priehľadnosť}.</p> */
	public int priehladnost() { return getAlpha(); }


	/* *
	 * <p>Táto metóda predstavuje dokončenie implementácie rozhrania
	 * {@link Farebnosť Farebnosť}. Návratová hodnota je inštancia tejto
	 * farby.</p>
	 * 
	 * @return aktuálna inštancia farby
	 * /
	public Farba farba() { return this; }
	*/


	// Faktor pre potreby metód svetlejšia(), tmavšia(), priehľadnejšia
	// a nepriehľadnejšia()
	private final static double faktor = 0.7;

	/**
	 * <p>Vytvorí novú bledšiu verziu tejto farby. Metóda použije na každú
	 * farebnú zložku (RGB) vlastnú mierku (faktor) na zosvetlenie tejto
	 * farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda funguje podobne ako
	 * originálna metóda triedy {@link Color Color}{@code .}{@link 
	 * Color#brighter() brighter}{@code ()}, len berie do úvahy aj
	 * priehľadnosť pôvodnej farby a zachováva ju.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> I keď sú operácie
	 * {@code currsvetlejšia} a {@link #tmavšia() tmavšia} opačné (myslené
	 * logicky), ich niekoľkonásobné striedavé použitie bude v dôsledku
	 * zaokrúhlení viesť k získaniu úplne inej farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda používa predvolenú
	 * hodnotu faktoru zosvetlenia {@code num0.7}. Pozri aj {@link 
	 * #svetlejšia(double) svetlejšia(faktor)}.</p>
	 * 
	 * @return nová inštancia triedy {@link Farba Farba} s bledšou
	 *     verziou tejto farby
	 * 
	 * @see #svetlejšia()
	 * @see #tmavšia()
	 */
	public Farba svetlejšia()
	{
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		/*-
		 * Poznámky:
		 *  1. čierna.svetlejšia() má vytvoriť šedú,
		 *  2. použitie svetlejšia() na modrú, musí vrátiť modrú,
		 *  3. mnohonásobné použitie svetlejšia() nakoniec vyústi do
		 *     bielej.
		 */
		int i = (int)(1.0 / (1.0 - faktor));

		if (r == 0 && g == 0 && b == 0)
			return new Farba(i, i, i, getAlpha());

		if (r > 0 && r < i) r = i;
		if (g > 0 && g < i) g = i;
		if (b > 0 && b < i) b = i;

		return new Farba(Math.min((int)(r / faktor), 255),
			Math.min((int)(g / faktor), 255),
			Math.min((int)(b / faktor), 255), getAlpha());
	}

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia() svetlejšia}.</p> */
	public Farba svetlejsia() { return svetlejšia(); }

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia() svetlejšia}.</p> */
	public Farba bledšia() { return svetlejšia(); }

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia() svetlejšia}.</p> */
	public Farba bledsia() { return svetlejšia(); }

	/**
	 * <p>Vytvorí novú tmavšiu verziu tejto farby. Metóda použije na každú
	 * farebnú zložku (RGB) vlastnú mierku (faktor) na stmavenie tejto
	 * farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda funguje podobne ako
	 * originálna metóda triedy {@link Color Color}{@code .}{@link 
	 * Color#darker() darker}{@code ()}, len berie do úvahy aj
	 * priehľadnosť pôvodnej farby a zachováva ju.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> I keď sú operácie
	 * {@link #svetlejšia() svetlejšia} a tmavšia opačné (myslené logicky),
	 * ich niekoľkonásobné striedavé použitie bude v dôsledku zaokrúhlení
	 * viesť k získaniu úplne inej farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda používa predvolenú
	 * hodnotu faktoru stmavenia {@code num0.7}. Pozri aj {@link 
	 * #tmavšia(double) tmavšia(faktor)}.</p>
	 * 
	 * @return nová inštancia triedy {@link Farba Farba} s tmavšou
	 *     verziou tejto farby
	 */
	public Farba tmavšia()
	{
		return new Farba(Math.max((int)(getRed() * faktor), 0),
			Math.max((int)(getGreen() * faktor), 0),
			Math.max((int)(getBlue() * faktor), 0), getAlpha());
	}

	/** <p><a class="alias"></a> Alias pre {@link #tmavšia() tmavšia}.</p> */
	public Farba tmavsia() { return tmavšia(); }

	/**
	 * <p>Vytvorí priehľadnejšiu verziu tejto farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda používa predvolenú
	 * hodnotu faktoru spriehľadnenia {@code num0.7}. Pozri aj {@link 
	 * #priehľadnejšia(double) priehľadnejšia(faktor)}.</p>
	 * 
	 * @return nová inštancia triedy {@link Farba Farba} s priehľadnejšou
	 *     verziou tejto farby
	 */
	public Farba priehľadnejšia()
	{
		return new Farba(getRed(), getGreen(), getBlue(),
			Math.max((int)(getAlpha() * faktor), 0));
	}

	/** <p><a class="alias"></a> Alias pre {@link #priehľadnejšia() priehľadnejšia}.</p> */
	public Farba priehladnejsia() { return priehľadnejšia(); }

	/**
	 * <p>Vytvorí menej priehľadnú verziu tejto farby.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda používa predvolenú
	 * hodnotu faktoru znepriehľadnenia {@code num0.7}. Pozri aj {@link 
	 * #nepriehľadnejšia(double) nepriehľadnejšia(faktor)}.</p>
	 * 
	 * @return nová inštancia triedy {@link Farba Farba} s menej
	 *     priehľadnou verziou tejto farby
	 */
	public Farba nepriehľadnejšia()
	{
		int i = (int)(1.0 / (1.0 - faktor));
		int a = getAlpha();

		if (a > 0 && a < i)
			return new Farba(getRed(), getGreen(), getBlue(), i);

		return new Farba(getRed(), getGreen(), getBlue(),
			Math.min((int)(a / faktor), 255));
	}

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia() nepriehľadnejšia}.</p> */
	public Farba nepriehladnejsia() { return nepriehľadnejšia(); }

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia() nepriehľadnejšia}.</p> */
	public Farba menejPriehľadná() { return nepriehľadnejšia(); }

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia() nepriehľadnejšia}.</p> */
	public Farba menejPriehladna() { return nepriehľadnejšia(); }

	/**
	 * <p>Vytvorí novú bledšiu verziu tejto farby. Metóda použije na každú
	 * farebnú zložku (RGB) zadanú mierku (faktor) zosvetlenia.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> I keď sú operácie
	 * {@code currsvetlejšia} a {@link #tmavšia(double) tmavšia} opačné
	 * (myslené logicky), ich niekoľkonásobné striedavé použitie bude
	 * v dôsledku zaokrúhlení viesť k získaniu úplne inej farby.</p>
	 * 
	 * @param faktor faktor zosvetlenia – hodnota v rozsahu 0.0 – 1.0,
	 *     pričom krajné hodnoty (0.0 a 1.0) nie sú povolené; čím je
	 *     hodnota faktora nižšia, tým je zmena svetlosti výraznejšia
	 * @return nová inštancia triedy {@link Farba Farba} s bledšou
	 *     verziou tejto farby
	 * 
	 * @see #tmavšia(double)
	 */
	public Farba svetlejšia(double faktor)
	{
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		/*-
		 * Poznámky:
		 *  1. čierna.svetlejšia() má vytvoriť šedú,
		 *  2. použitie svetlejšia() na modrú, musí vrátiť modrú,
		 *  3. mnohonásobné použitie svetlejšia() nakoniec vyústi do
		 *     bielej.
		 */
		int i = (int)(1.0 / (1.0 - faktor));

		if (r == 0 && g == 0 && b == 0)
			return new Farba(i, i, i, getAlpha());

		if (r > 0 && r < i) r = i;
		if (g > 0 && g < i) g = i;
		if (b > 0 && b < i) b = i;

		return new Farba(Math.min((int)(r / faktor), 255),
			Math.min((int)(g / faktor), 255),
			Math.min((int)(b / faktor), 255), getAlpha());
	}

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia(double) svetlejšia}.</p> */
	public Farba bledšia(double faktor) { return svetlejšia(faktor); }

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia(double) svetlejšia}.</p> */
	public Farba bledsia(double faktor) { return svetlejšia(faktor); }

	/** <p><a class="alias"></a> Alias pre {@link #svetlejšia(double) svetlejšia}.</p> */
	public Farba svetlejsia(double faktor) { return svetlejšia(faktor); }

	/**
	 * <p>Vytvorí novú tmavšiu verziu tejto farby. Metóda použije na každú
	 * farebnú zložku (RGB) zadanú mierku (faktor) stmavenia.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> I keď sú operácie
	 * {@link #svetlejšia(double) svetlejšia} a {@code currtmavšia} opačné
	 * (myslené logicky), ich niekoľkonásobné striedavé použitie bude
	 * v dôsledku zaokrúhlení viesť k získaniu úplne inej farby.</p>
	 * 
	 * @param faktor faktor stmavenia – hodnota v rozsahu 0.0 – 1.0,
	 *     pričom krajné hodnoty (0.0 a 1.0) nie sú na použitie vhodné
	 *     (0.0 by stmavila farbu do čiernej a 1.0 by nevykonala žiadnu
	 *     zmenu svetlosti); čím je hodnota faktora nižšia, tým je
	 *     úroveň stmavenia výraznejšia
	 * @return nová inštancia triedy {@link Farba Farba} s tmavšou
	 *     verziou tejto farby
	 */
	public Farba tmavšia(double faktor)
	{
		return new Farba(Math.max((int)(getRed() * faktor), 0),
			Math.max((int)(getGreen() * faktor), 0),
			Math.max((int)(getBlue() * faktor), 0), getAlpha());
	}

	/** <p><a class="alias"></a> Alias pre {@link #tmavšia(double) tmavšia}.</p> */
	public Farba tmavsia(double faktor) { return tmavšia(faktor); }

	/**
	 * <p>Vytvorí priehľadnejšiu verziu tejto farby na základe zadaného
	 * faktora.</p>
	 * 
	 * @param faktor faktor spriehľadnenia – hodnota v rozsahu 0.0 – 1.0,
	 *     pričom krajné hodnoty (0.0 a 1.0) nie sú na použitie vhodné
	 *     (0.0 by nastavila farbu na úplne priehľadnú a 1.0 by
	 *     nevykonala žiadnu zmenu priehľadnosti); čím je hodnota
	 *     faktora nižšia, tým je úroveň zmeny priehľadnosti výraznejšia
	 * @return nová inštancia triedy {@link Farba Farba} s priehľadnejšou
	 *     verziou tejto farby
	 */
	public Farba priehľadnejšia(double faktor)
	{
		return new Farba(getRed(), getGreen(), getBlue(),
			Math.max((int)(getAlpha() * faktor), 0));
	}

	/** <p><a class="alias"></a> Alias pre {@link #priehľadnejšia() priehľadnejšia}.</p> */
	public Farba priehladnejsia(double faktor) { return priehľadnejšia(faktor); }

	/**
	 * <p>Vytvorí menej priehľadnú verziu tejto farby na základe zadaného
	 * faktora.</p>
	 * 
	 * @param faktor faktor znepriehľadnenia – hodnota v rozsahu 0.0 –
	 *     1.0, pričom krajné hodnoty (0.0 a 1.0) nie sú povolené; čím
	 *     je hodnota faktora nižšia, tým je zmena nepriehľadnosti
	 *     výraznejšia
	 * @return nová inštancia triedy {@link Farba Farba} s menej
	 *     priehľadnou verziou tejto farby
	 */
	public Farba nepriehľadnejšia(double faktor)
	{
		int i = (int)(1.0 / (1.0 - faktor));
		int a = getAlpha();

		if (a > 0 && a < i)
			return new Farba(getRed(), getGreen(), getBlue(), i);

		return new Farba(getRed(), getGreen(), getBlue(),
			Math.min((int)(a / faktor), 255));
	}

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia(double) nepriehľadnejšia}.</p> */
	public Farba nepriehladnejsia(double faktor) { return nepriehľadnejšia(faktor); }

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia(double) nepriehľadnejšia}.</p> */
	public Farba menejPriehľadná(double faktor) { return nepriehľadnejšia(faktor); }

	/** <p><a class="alias"></a> Alias pre {@link #nepriehľadnejšia(double) nepriehľadnejšia}.</p> */
	public Farba menejPriehladna(double faktor) { return nepriehľadnejšia(faktor); }


	/**
	 * <p>Vyrobí novú farbu s upravenými parametrami jasu a kontrastu. Oba
	 * parametre môžu nadobúdať kladné aj záporné hodnoty. Na príklade nižšie
	 * vidno vygenerované farby pri rôznych hodnotách týchto parametrov.</p>
	 * 
	 * <table class="tightTable centered"><tr>
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #32322c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #82827c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #d2d2cc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #464612"></td>
	 * <td style="width: 10px; height: 10px; background-color: #969662"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e6e6b2"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #121200"></td>
	 * <td style="width: 10px; height: 10px; background-color: #626200"></td>
	 * <td style="width: 10px; height: 10px; background-color: #b2b23c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffff8c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffffdc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #404000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #909000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e0e000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffff50"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffffa0"></td>
	 * <td style="width: 10px; height: 10px; background-color: ivory"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: #464600"></td>
	 * <td style="width: 10px; height: 10px; background-color: #969600"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e6e600"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffff2c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffff7c"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * <td style="width: 10px; height: 10px; background-color: yellow"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2c3030"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7c8080"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ccd0d0"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #123333"></td>
	 * <td style="width: 10px; height: 10px; background-color: #628383"></td>
	 * <td style="width: 10px; height: 10px; background-color: #b2d3d3"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #003838"></td>
	 * <td style="width: 10px; height: 10px; background-color: #3c8888"></td>
	 * <td style="width: 10px; height: 10px; background-color: #8cd8d8"></td>
	 * <td style="width: 10px; height: 10px; background-color: #dcffff"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #004040"></td>
	 * <td style="width: 10px; height: 10px; background-color: #009090"></td>
	 * <td style="width: 10px; height: 10px; background-color: #50e0e0"></td>
	 * <td style="width: 10px; height: 10px; background-color: #a0ffff"></td>
	 * <td style="width: 10px; height: 10px; background-color: azure"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #004e4e"></td>
	 * <td style="width: 10px; height: 10px; background-color: #009e9e"></td>
	 * <td style="width: 10px; height: 10px; background-color: #0ee"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2cffff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7cffff"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #002424"></td>
	 * <td style="width: 10px; height: 10px; background-color: #007474"></td>
	 * <td style="width: 10px; height: 10px; background-color: #00c4c4"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * <td style="width: 10px; height: 10px; background-color: aqua"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #322c32"></td>
	 * <td style="width: 10px; height: 10px; background-color: #827c82"></td>
	 * <td style="width: 10px; height: 10px; background-color: #d2ccd2"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #461246"></td>
	 * <td style="width: 10px; height: 10px; background-color: #966296"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e6b2e6"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #120012"></td>
	 * <td style="width: 10px; height: 10px; background-color: #620062"></td>
	 * <td style="width: 10px; height: 10px; background-color: #b23cb2"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff8cff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffdcff"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #400040"></td>
	 * <td style="width: 10px; height: 10px; background-color: #900090"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e000e0"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff50ff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffa0ff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #fff0ff"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color:
	 * #460046"></td>
	 * <td style="width: 10px; height: 10px; background-color: #960096"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e600e6"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff2cff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff7cff"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color:
	 * fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color:
	 * fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * <td style="width: 10px; height: 10px; background-color: fuchsia"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #302c2c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #807c7c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #d0cccc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #331212"></td>
	 * <td style="width: 10px; height: 10px; background-color: #836262"></td>
	 * <td style="width: 10px; height: 10px; background-color: #d3b2b2"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #380000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #883c3c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #d88c8c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffdcdc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #400000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #900000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e05050"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ffa0a0"></td>
	 * <td style="width: 10px; height: 10px; background-color: #fff0f0"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #4e0000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #9e0000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #e00"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff2c2c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ff7c7c"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #240000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #740000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #c40000"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * <td style="width: 10px; height: 10px; background-color: red"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2c2c31"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7c7c81"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ccccd1"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #12123b"></td>
	 * <td style="width: 10px; height: 10px; background-color: #62628b"></td>
	 * <td style="width: 10px; height: 10px; background-color: #b2b2db"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #000049"></td>
	 * <td style="width: 10px; height: 10px; background-color: #3c3c99"></td>
	 * <td style="width: 10px; height: 10px; background-color: #8c8ce9"></td>
	 * <td style="width: 10px; height: 10px; background-color: #dcdcff"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #000010"></td>
	 * <td style="width: 10px; height: 10px; background-color: #000060"></td>
	 * <td style="width: 10px; height: 10px; background-color: #0000b0"></td>
	 * <td style="width: 10px; height: 10px; background-color: #5050ff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #a0a0ff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #f0f0ff"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #00003b"></td>
	 * <td style="width: 10px; height: 10px; background-color: darkblue"></td>
	 * <td style="width: 10px; height: 10px; background-color: #0000db"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2c2cff"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7c7cff"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color:
	 * #00005c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #0000ac"></td>
	 * <td style="width: 10px; height: 10px; background-color: #0000fc"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * <td style="width: 10px; height: 10px; background-color: blue"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 10px;
	 * height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2c322c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7c827c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #ccd2cc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #124612"></td>
	 * <td style="width: 10px; height: 10px; background-color: #629662"></td>
	 * <td style="width: 10px; height: 10px; background-color: #b2e6b2"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #001200"></td>
	 * <td style="width: 10px; height: 10px; background-color: #006200"></td>
	 * <td style="width: 10px; height: 10px; background-color: #3cb23c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #8cff8c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #dcffdc"></td>
	 * <td style="width: 10px; height: 10px; background-color: white"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: black"></td>
	 * <td style="width: 10px; height: 10px; background-color: #004000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #009000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #00e000"></td>
	 * <td style="width: 10px; height: 10px; background-color: #50ff50"></td>
	 * <td style="width: 10px; height: 10px; background-color: #a0ffa0"></td>
	 * <td style="width: 10px; height: 10px; background-color: honeydew"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color:
	 * #004600"></td>
	 * <td style="width: 10px; height: 10px; background-color: #009600"></td>
	 * <td style="width: 10px; height: 10px; background-color: #00e600"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: #2cff2c"></td>
	 * <td style="width: 10px; height: 10px; background-color: #7cff7c"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * </tr>
	 * <tr><td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * <td style="width: 10px; height: 10px; background-color: lime"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * </tr></table>
	 * 
	 * <p class="image">Ukážka rôznych úrovní úpravy jasu a kontrastu
	 * (oboje v rozsahu −240 až 240 s krokom 80) zvolených farieb<br />(zľava
	 * doprava: {@link Farebnosť#žltá žltá}, {@link Farebnosť#tmavotyrkysová
	 * tmavotyrkysová}, {@link Farebnosť#svetlopurpurová svetlopurpurová},
	 * {@link Farebnosť#tmavočervená tmavočervená}, {@link Farebnosť#modrá
	 * modrá} a {@link Farebnosť#svetlozelená svetlozelená}).</p>
	 * 
	 * <p> </p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda používa rovnaký
	 * algoritmus ako metóda {@link Obrázok#upravJasKontrast(double, double)
	 * Obrázok.upravJasKontrast}, ale na rozdiel od uvedenej metódy
	 * spracúva aj farby, ktoré sú úplne priehľadné.</p>
	 * 
	 * @param jas reálnočíselná hodnota určujúca mieru zmeny jasu;
	 *     relevantné sú hodnoty v rozmedzí od −255.0 do 255.0
	 * @param kontrast reálnočíselná hodnota určujúca mieru zmeny kontrastu;
	 *     relevantné sú hodnoty v rozmedzí od −255.0 do 255.0
	 */
	public Farba jasKontrast(double jas, double kontrast)
	{
		if (0.0 == jas && 0.0 == kontrast) return new Farba(this);

		int rgb = getRGB();
		int a = getAlpha();

		if (0.0 != jas && 0.0 != kontrast)
		{
			double faktorKontrastu = (259.0 * (kontrast + 255.0)) /
				(255.0 * (259.0 - kontrast));

			double r = faktorKontrastu * (((rgb >>
				16) & 0xff) - 128.0) + 128.0 + jas;
			double g = faktorKontrastu * (((rgb >>
				8) & 0xff) - 128.0) + 128.0 + jas;
			double b = faktorKontrastu * ((rgb
				& 0xff) - 128.0) + 128.0 + jas;

			if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
			if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
			if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

			return new Farba((int)r, (int)g, (int)b, a);
		}
		else if (0.0 != kontrast)
		{
			double faktorKontrastu = (259.0 * (kontrast + 255.0)) /
				(255.0 * (259.0 - kontrast));

			double r = faktorKontrastu * (((rgb >>
				16) & 0xff) - 128.0) + 128.0;
			double g = faktorKontrastu * (((rgb >>
				8) & 0xff) - 128.0) + 128.0;
			double b = faktorKontrastu * (( rgb
				& 0xff) - 128.0) + 128.0;

			if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
			if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
			if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

			return new Farba((int)r, (int)g, (int)b, a);
		}

		double r = ((rgb >> 16) & 0xff) + jas;
		double g = ((rgb >>  8) & 0xff) + jas;
		double b = ( rgb        & 0xff) + jas;

		if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
		if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
		if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

		return new Farba((int)r, (int)g, (int)b, a);
	}

	/**
	 * <p>Vyrobí novú farbu upravenú s použitím algoritmu gama (γ) korekcie.
	 * Metóda používa rovnaký algoritmus ako {@link 
	 * Obrázok#gamaKorekcia(double) Obrázok.gamaKorekcia}. Ďalšie informácie
	 * o gama korekcii nájdete v opise uvedenej metódy. Parameter {@code γ}
	 * môže nadobúdať prakticky ľubovoľné hodnoty, ale odporúčaný rozsah je
	 * v rozmedzí od {@code num0.01} do {@code num7.99}. Príklad nižšie
	 * ukazuje úpravu niekoľkých predvolených farieb s rovnakými hodnotami
	 * parametra {@code γ}.</p>
	 * 
	 * <table class="tightTable centered"><tr>
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #979700"></td>
	 * <td style="width: 20px; height: 20px; background-color: #c4c400"></td>
	 * <td style="width: 20px; height: 20px; background-color: #e0e000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #ee0"></td>
	 * <td style="width: 20px; height: 20px; background-color: #f6f600"></td>
	 * <td style="width: 20px; height: 20px; background-color: #fafa00"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #001919"></td>
	 * <td style="width: 20px; height: 20px; background-color: #005151"></td>
	 * <td style="width: 20px; height: 20px; background-color: #009090"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00bfbf"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0dd"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00eded"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #970097"></td>
	 * <td style="width: 20px; height: 20px; background-color: #c400c4"></td>
	 * <td style="width: 20px; height: 20px; background-color: #e000e0"></td>
	 * <td style="width: 20px; height: 20px; background-color: #e0e"></td>
	 * <td style="width: 20px; height: 20px; background-color: #f600f6"></td>
	 * <td style="width: 20px; height: 20px; background-color: #fa00fa"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #190000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #510000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #900000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #bf0000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #d00"></td>
	 * <td style="width: 20px; height: 20px; background-color: #ed0000"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #000039"></td>
	 * <td style="width: 20px; height: 20px; background-color: #000079"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0000b0"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0000d3"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0000e8"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0000f3"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * <td><table class="tightTable"><tr><td style="width: 20px; height:
	 * 20px; background-color: black"></td>
	 * <td style="width: 20px; height: 20px; background-color: #009700"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00c400"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00e000"></td>
	 * <td style="width: 20px; height: 20px; background-color: #0e0"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00f600"></td>
	 * <td style="width: 20px; height: 20px; background-color: #00fa00"></td>
	 * </tr>
	 * </table></td>
	 * 
	 * </tr></table>
	 * 
	 * <p class="image">Ukážka rôznych mier gama korekcie niekoľkých
	 * predvolených farieb<br />(zľava doprava: {@link Farebnosť#žltá žltá},
	 * {@link Farebnosť#tmavotyrkysová tmavotyrkysová},
	 * {@link Farebnosť#svetlopurpurová svetlopurpurová},
	 * {@link Farebnosť#tmavočervená tmavočervená}, {@link Farebnosť#modrá
	 * modrá}<br />a {@link Farebnosť#svetlozelená svetlozelená} postupne
	 * upravené hodnotami γ: 0,01; 0,25; 0,5; 1,0; 2,0; 4,0 a 7.99).</p>
	 * 
	 * @param γ miera gama korekcie (gama korekcia súvisí s intenzitou
	 *     farby; vhodný rozsah hodnôt je zhruba v rozmedzí 0,01 – 7,99)
	 * 
	 * @see Obrázok#gamaKorekcia(double)
	 */
	public Farba gamaKorekcia(double γ)
	{
		if (1.0 == γ) return new Farba(this);

		double γC = (0.0 == γ) ? 65025.0 : (1.0 / γ);
		int rgb = getRGB();
		int a = getAlpha();

		double r = 255.0 * Math.pow(((rgb >> 16) & 0xff) / 255.0, γC);
		double g = 255.0 * Math.pow(((rgb >>  8) & 0xff) / 255.0, γC);
		double b = 255.0 * Math.pow( (rgb        & 0xff) / 255.0, γC);

		if (r > 255.0) r = 255.0; else if (r < 0.0) r = 0.0;
		if (g > 255.0) g = 255.0; else if (g < 0.0) g = 0.0;
		if (b > 255.0) b = 255.0; else if (b < 0.0) b = 0.0;

		return new Farba((int)r, (int)g, (int)b, a);
	}


	// Na základe tohto príkladu:
		// https://stackoverflow.com/questions/12026767/java-7-jcolorchooser-disable-transparency-slider
		// sa dá ľubovoľne upravovať vzhľad dialógu…
		//
		// private static void removeTransparencySlider(
		// 	JColorChooser jc) throws Exception
		// {
		// 	AbstractColorChooserPanel[] colorPanels = jc.getChooserPanels();
		// 
		// 	for (int i = 1; i < colorPanels.length; ++i)
		// 	{
		// 		AbstractColorChooserPanel cp = colorPanels[i];
		// 
		// 		Field f = cp.getClass().getDeclaredField("panel");
		// 		f.setAccessible(true);
		// 
		// 		Object colorPanel = f.get(cp);
		// 		Field f2 = colorPanel.getClass().
		// 			getDeclaredField("spinners");
		// 		f2.setAccessible(true);
		// 		Object spinners = f2.get(colorPanel);
		// 
		// 		Object transpSlispinner = Array.get(spinners, 3);
		// 		if (i == colorPanels.length - 1)
		// 		{
		// 			transpSlispinner = Array.get(spinners, 4);
		// 		}
		// 
		// 		Field f3 = transpSlispinner.getClass().
		// 			getDeclaredField("slider");
		// 		f3.setAccessible(true);
		// 		JSlider slider = (JSlider)f3.get(transpSlispinner);
		// 		slider.setEnabled(false);
		// 
		// 		Field f4 = transpSlispinner.getClass().
		// 			getDeclaredField("spinner");
		// 		f4.setAccessible(true);
		// 		JSpinner spinner = (JSpinner)f4.get(transpSlispinner);
		// 		spinner.setEnabled(false);
		// 	}
		// }


		// Panel farieb používaný v dialógoch výberu farby a voľby rôznych
		// parametrov (pozri: Farba.zvoľFarbu a Svet.dialóg).
		@SuppressWarnings("serial")
		/*packagePrivate*/ static class PanelFarieb extends JPanel
		{
			// Objekty komponentu palety panela:
			private BufferedImage obrázokPalety;
			private Graphics2D grafikaPalety;
			private ImageIcon ikonaPalety;
			private JLabel komponentPalety;

			// Objekty komponentu ukážky zvolenej farby na paneli:
			private BufferedImage obrázokUkážky;
			private Graphics2D grafikaUkážky;
			private ImageIcon ikonaUkážky;
			private JLabel komponentUkážky;

			// Ovládacie prvky (tlačidlá) s ich panelom:
			private JButton tlačidloReset;
			private JButton tlačidloMiešať;
			private JPanel panelTlačidielAUkážky;

			// Atribúty tohto panela farieb:
			private String textTitulku = "Voľba farby";
			private Farba predvolenáFarba = biela;
			private Farba zvolenáFarba = biela;
			private int veľkosťPolí = 25;


			/**
			 * <p>Konštruktor.</p>
			 * 
			 * @param titulok titulok okna miešania farieb
			 * @param textReset text tlačidla resetu farby
			 * @param textMiešať text tlačidla miešania farieb
			 * @param farba predvolená farba panela farieb
			 * @param rozmerPolí rozmer plôšok na výber farieb
			 */
			public PanelFarieb(String titulok,
				String textReset, String textMiešať,
				Color farba, int rozmerPolí)
			{
				if (null != titulok)
					textTitulku = titulok;

				if (null != farba)
				{
					if (farba instanceof Farba)
						predvolenáFarba = (Farba)farba;
					else
						predvolenáFarba = new Farba(farba);
					zvolenáFarba = predvolenáFarba;
				}

				if (4 < rozmerPolí)
					veľkosťPolí = rozmerPolí;

				int výška = 6 * veľkosťPolí;
				int šírka = 8 * veľkosťPolí;

				obrázokPalety = new BufferedImage(
					šírka, výška, BufferedImage.TYPE_INT_ARGB);
				grafikaPalety = obrázokPalety.createGraphics();
				// grafikaPalety.addRenderingHints(Obrázok.hints);
				ikonaPalety = new ImageIcon(obrázokPalety);
				komponentPalety = new JLabel(ikonaPalety);
				komponentPalety.setBorder(BorderFactory.
					createEmptyBorder(10, 10, 10, 10));

				obrázokUkážky = new BufferedImage(
					2 * veľkosťPolí, 2 * veľkosťPolí,
					BufferedImage.TYPE_INT_ARGB);
				grafikaUkážky = obrázokUkážky.createGraphics();
				// grafikaUkážky.addRenderingHints(Obrázok.hints);
				ikonaUkážky = new ImageIcon(obrázokUkážky);
				komponentUkážky = new JLabel(ikonaUkážky);
				komponentUkážky.setAlignmentX(Component.CENTER_ALIGNMENT);
				komponentUkážky.setBorder(BorderFactory.
					createEmptyBorder(20, 10, 10, 10));

				tlačidloReset = new JButton(
					null == textReset ? "Reset" : textReset);
				tlačidloReset.setAlignmentX(Component.CENTER_ALIGNMENT);

				tlačidloMiešať = new JButton(
					null == textMiešať ? "Miešať…" : textMiešať);
				tlačidloMiešať.setAlignmentX(Component.CENTER_ALIGNMENT);

				setLayout(new BorderLayout());

				panelTlačidielAUkážky = new JPanel();
				panelTlačidielAUkážky.setLayout(new BoxLayout(
					panelTlačidielAUkážky, BoxLayout.Y_AXIS));

				add(komponentPalety, BorderLayout.WEST);
				panelTlačidielAUkážky.add(komponentUkážky);
				panelTlačidielAUkážky.add(Box.createVerticalStrut(10));
				panelTlačidielAUkážky.add(tlačidloReset);
				panelTlačidielAUkážky.add(Box.createVerticalStrut(10));
				panelTlačidielAUkážky.add(tlačidloMiešať);
				add(panelTlačidielAUkážky, BorderLayout.EAST);

				komponentPalety.addMouseListener(new MouseListener()
					{
						public void mouseClicked(MouseEvent e)
						{
							// System.out.println("  Súradnice: " +
							// 	e.getX() + ", " + e.getY());

							int index = (((e.getX() - 10) / veľkosťPolí) * 6) +
								((e.getY() - 10) / veľkosťPolí);
							if (index >= 0 && index <
								preddefinovanéFarby.length)
							{
								zvolenáFarba = preddefinovanéFarby[index];
								aktualizujUkážku();

								// System.out.println("  Zvolená farba: " +
								// 	index + " " + Farba.farbaNaReťazec(
								// 	zvolenáFarba));
							}
							// else System.out.println(
							// 	"  Index mimo rozsahu: " + index);
						}

						public void mouseEntered(MouseEvent e) {}
						public void mouseExited(MouseEvent e) {}
						public void mousePressed(MouseEvent e) {}
						public void mouseReleased(MouseEvent e) {}
					});

				tlačidloReset.addActionListener(e ->
					{
						zvolenáFarba = predvolenáFarba;
						aktualizujUkážku();
					});

				tlačidloMiešať.addActionListener(e ->
					{
						Color voľba = JColorChooser.showDialog(
							null == Svet.oknoCelejObrazovky ?
							GRobot.svet : Svet.oknoCelejObrazovky,
							textTitulku, zvolenáFarba);

						if (null != voľba)
						{
							zvolenáFarba = new Farba(voľba);
							aktualizujUkážku();
						}
					});

				aktualizujPaletu();
				aktualizujUkážku();
			}

			// Aktualizuje paletu farieb podľa aktuálnych hodnôt poľa
			// preddefinovaných farieb.
			private void aktualizujPaletu()
			{
				int početRiadkov = veľkosťPolí * 6 / 10;
				int početStĺpcov = veľkosťPolí * 8 / 10;
				for (int i = 0; i < početRiadkov; ++i)
				{
					for (int j = 0; j < početStĺpcov; ++j)
					{
						if (0 == (i + j) % 2)
							grafikaPalety.setColor(šedá);
						else
							grafikaPalety.setColor(biela);
						grafikaPalety.fillRect(10 * j, 10 * i, 10, 10);
					}
				}

				for (int i = 0; i < 6; ++i)
				{
					for (int j = 0; j < 8; ++j)
					{
						grafikaPalety.setColor(preddefinovanéFarby[j * 6 + i]);
						grafikaPalety.fillRect(veľkosťPolí * j,
							veľkosťPolí * i, veľkosťPolí, veľkosťPolí);
					}
				}
			}

			// Aktualizácia ukážky zvolenej farby.
			private void aktualizujUkážku()
			{
				int počet = veľkosťPolí * 2 / 10;
				for (int i = 0; i < počet; ++i)
				{
					for (int j = 0; j < počet; ++j)
					{
						if (0 == (i + j) % 2)
							grafikaUkážky.setColor(šedá);
						else
							grafikaUkážky.setColor(biela);
						grafikaUkážky.fillRect(10 * j, 10 * i, 10, 10);
					}
				}

				if (null != zvolenáFarba)
				{
					grafikaUkážky.setColor(predvolenáFarba);
					grafikaUkážky.fillRect(0, 0,
						2 * veľkosťPolí, veľkosťPolí);
					grafikaUkážky.setColor(zvolenáFarba);
					grafikaUkážky.fillRect(0, veľkosťPolí,
						2 * veľkosťPolí, veľkosťPolí);
				}

				komponentUkážky.repaint();
			}

			// Aktualizuje tento panel do takého stavu, v akom by sa
			// nachádzal po konštrukcii so zadanými hodnotami parametrov.
			// (Môžu nastať drobné odchýlky, ktoré sú neodsledovateľné, ale
			// zhruba by sa panel mal vizuálne aj vnútorne nachádzať
			// v požadovanom stave.)
			private void aktualizujPanel(String titulok,
				String textReset, String textMiešať,
				Color farba, int rozmerPolí)
			{
				upravTitulok(titulok);
				upravTextyTlačidiel(textReset, textMiešať);

				if (rozmerPolí <= 4 && veľkosťPolí != 25) rozmerPolí = 25;
				aktualizujVeľkosťPaletyAUkážky(rozmerPolí);

				aktualizujPaletu();
				nastavFarbu(farba);
			}

			/**
			 * <p>Aktualizuje veľkosť palety a ukážky zvolenej farby.</p>
			 * 
			 * @param rozmerPolí rozmer plôšok (položiek) palety, ktorá je
			 *     zároveň polovicou veľkosti plochy s ukážkou zvolenej farby
			 */
			public void aktualizujVeľkosťPaletyAUkážky(int rozmerPolí)
			{
				// System.out.println("veľkosťPolí: " + veľkosťPolí +
				// 	" (" + rozmerPolí + ")");
				if (rozmerPolí > 4 && veľkosťPolí != rozmerPolí)
				{
					veľkosťPolí = rozmerPolí;

					int výška = 6 * veľkosťPolí;
					int šírka = 8 * veľkosťPolí;

					obrázokPalety = new BufferedImage(
						šírka, výška, BufferedImage.TYPE_INT_ARGB);
					grafikaPalety = obrázokPalety.createGraphics();
					// grafikaPalety.addRenderingHints(Obrázok.hints);
					ikonaPalety = new ImageIcon(obrázokPalety);
					komponentPalety.setIcon(ikonaPalety);

					obrázokUkážky = new BufferedImage(
						2 * veľkosťPolí, 2 * veľkosťPolí,
						BufferedImage.TYPE_INT_ARGB);
					grafikaUkážky = obrázokUkážky.createGraphics();
					// grafikaUkážky.addRenderingHints(Obrázok.hints);
					ikonaUkážky = new ImageIcon(obrázokUkážky);
					komponentUkážky.setIcon(ikonaUkážky);
				}
			}

			/**
			 * <p>Nastavenie novej predvolenej farby panela.</p>
			 * 
			 * @param nováFarba nová predvolená farba panela
			 */
			public void nastavFarbu(Color nováFarba)
			{
				if (null == nováFarba)
					zvolenáFarba = predvolenáFarba = biela;
				{
					if (nováFarba instanceof Farba)
						predvolenáFarba = (Farba)nováFarba;
					else
						predvolenáFarba = new Farba(nováFarba);
					zvolenáFarba = predvolenáFarba;
				}
				aktualizujUkážku();
			}

			/**
			 * <p>Vráti aktuálne zvolenú farbu v paneli.</p>
			 * 
			 * @return aktuálne zvolená farba panela
			 */
			public Farba dajFarbu() { return zvolenáFarba; }

			/**
			 * <p>Upraví titulok okna miešania farieb.</p>
			 * 
			 * @param novýTitulok nový titulok okna miešania farieb
			 */
			public void upravTitulok(String novýTitulok)
			{
				if (null == novýTitulok)
					textTitulku = "Voľba farby";
				else
					textTitulku = novýTitulok;
			}

			/**
			 * <p>Upraví predvolené texty tlačidiel resetu farby panela
			 * a otvorenia dialógu miešania farieb.</p>
			 * 
			 * @param textReset text tlačidla resetu farby panela
			 * @param textMiešať text tlačidla na otvorenie dialógu
			 *     miešania farieb
			 */
			public void upravTextyTlačidiel(
				String textReset, String textMiešať)
			{
				if (null == textReset)
					tlačidloReset.setText("Reset");
				else
					tlačidloReset.setText(textReset);

				if (null == textMiešať)
					tlačidloMiešať.setText("Miešať…");
				else
					tlačidloMiešať.setText(textMiešať);
			}


			// Statický panel dialógu voľby farby.
			private static PanelFarieb panelFarieb = null;

			/**
			 * <p>Otvorí dialóg s panelom na výber farby. Metóda prijíma
			 * parameter určujúci titulok dialógového okna a predvolenú farbu
			 * na paneli farieb. Ak je niektorá z hodnôt rovná {@code valnull},
			 * tak bude zvolená vhodná hodnota.</p>
			 * 
			 * @param titulok titulok okna dialógu
			 * @param predvolenáFarba predvolená farba panela
			 * @return zvolená farba alebo {@code valnull} (ak používateľ
			 *     dialóg zavrel)
			 */
			public static Farba dialóg(String titulok, Color predvolenáFarba)
			{
				if (null == panelFarieb)
					panelFarieb = new PanelFarieb(titulok,
						Svet.tlačidláDialógu[0], Svet.tlačidláDialógu[1],
						predvolenáFarba, 0);
				else
					panelFarieb.aktualizujPanel(titulok,
						Svet.tlačidláDialógu[0], Svet.tlačidláDialógu[1],
						predvolenáFarba, 0);

				Object[] komponenty = new Object[] {panelFarieb};

				if (JOptionPane.showOptionDialog(
					null == Svet.oknoCelejObrazovky ? GRobot.svet :
					Svet.oknoCelejObrazovky, komponenty, null == titulok ?
					"Voľba farby" : titulok, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, Svet.odpovedeZadania,
					null) == JOptionPane.YES_OPTION)
					return panelFarieb.zvolenáFarba;

				return null;
			}
		}

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #zvoľFarbu() zvoľFarbu}. Predvolená farba v otvorenom dialógu
	 * bude {@linkplain Svet#farbaPozadia() farba pozadia sveta}. Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba vyberFarbu()
	{
		return PanelFarieb.dialóg(null, Svet.farbaPozadia());

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, "Voľba farby", Svet.farbaPozadia());

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu() vyberFarbu}.</p> */
	public static Farba dialógVýberFarby() { return vyberFarbu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu() vyberFarbu}.</p> */
	public static Farba dialogVyberFarby() { return vyberFarbu(); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #vyberFarbu() vyberFarbu}. Predvolená farba v otvorenom dialógu
	 * bude {@linkplain Svet#farbaPozadia() farba pozadia sveta}. Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba zvoľFarbu()
	{
		return PanelFarieb.dialóg(null, Svet.farbaPozadia());

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, "Voľba farby", Svet.farbaPozadia());

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu() zvoľFarbu}.</p> */
	public static Farba zvolFarbu() { return zvoľFarbu(); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu() zvoľFarbu}.</p> */
	public static Farba dialógVoľbaFarby() { return zvoľFarbu(); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu() zvoľFarbu}.</p> */
	public static Farba dialogVolbaFarby() { return zvoľFarbu(); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #zvoľFarbu(Color) zvoľFarbu}. Otvorený dialóg bude mať
	 * predvolenú zadanú farbu (argument {@code počiatočnáFarba}). Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param počiatočnáFarba predvolená farba v novo otvorenom dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba vyberFarbu(Color počiatočnáFarba)
	{
		return PanelFarieb.dialóg(null, počiatočnáFarba);

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, "Voľba farby", počiatočnáFarba);

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(Color) vyberFarbu}.</p> */
	public static Farba dialógVýberFarby(Color počiatočnáFarba) { return vyberFarbu(počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(Color) vyberFarbu}.</p> */
	public static Farba dialogVyberFarby(Color počiatočnáFarba) { return vyberFarbu(počiatočnáFarba); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #vyberFarbu(Color) vyberFarbu}. Otvorený dialóg bude mať
	 * predvolenú zadanú farbu (argument {@code počiatočnáFarba}). Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param počiatočnáFarba predvolená farba v novo otvorenom dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba zvoľFarbu(Color počiatočnáFarba)
	{
		return PanelFarieb.dialóg(null, počiatočnáFarba);

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, "Voľba farby", počiatočnáFarba);

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(Color) zvoľFarbu}.</p> */
	public static Farba zvolFarbu(Color počiatočnáFarba) { return zvoľFarbu(počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(Color) zvoľFarbu}.</p> */
	public static Farba dialógVoľbaFarby(Color počiatočnáFarba) { return zvoľFarbu(počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(Color) zvoľFarbu}.</p> */
	public static Farba dialogVolbaFarby(Color počiatočnáFarba) { return zvoľFarbu(počiatočnáFarba); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #zvoľFarbu(String) zvoľFarbu}. Predvolená farba v otvorenom
	 * dialógu bude {@linkplain Svet#farbaPozadia() farba pozadia sveta}.
	 * Po zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba vyberFarbu(String titulok)
	{
		return PanelFarieb.dialóg(titulok, Svet.farbaPozadia());

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, Svet.farbaPozadia());

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(String) vyberFarbu}.</p> */
	public static Farba dialógVýberFarby(String titulok) { return vyberFarbu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(String) vyberFarbu}.</p> */
	public static Farba dialogVyberFarby(String titulok) { return vyberFarbu(titulok); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #vyberFarbu(String) vyberFarbu}. Predvolená farba v otvorenom
	 * dialógu bude {@linkplain Svet#farbaPozadia() farba pozadia sveta}.
	 * Po zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba zvoľFarbu(String titulok)
	{
		return PanelFarieb.dialóg(titulok, Svet.farbaPozadia());

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, Svet.farbaPozadia());

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String) zvoľFarbu}.</p> */
	public static Farba zvolFarbu(String titulok) { return zvoľFarbu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String) zvoľFarbu}.</p> */
	public static Farba dialógVoľbaFarby(String titulok) { return zvoľFarbu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String) zvoľFarbu}.</p> */
	public static Farba dialogVolbaFarby(String titulok) { return zvoľFarbu(titulok); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #zvoľFarbu(String, Color) zvoľFarbu}. Otvorený dialóg bude
	 * mať predvolenú zadanú farbu (argument {@code počiatočnáFarba}). Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnáFarba predvolená farba v novo otvorenom dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba vyberFarbu(String titulok, Color počiatočnáFarba)
	{
		return PanelFarieb.dialóg(titulok, počiatočnáFarba);

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, počiatočnáFarba);

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(String, Color) vyberFarbu}.</p> */
	public static Farba dialógVýberFarby(String titulok, Color počiatočnáFarba) { return vyberFarbu(titulok, počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberFarbu(String, Color) vyberFarbu}.</p> */
	public static Farba dialogVyberFarby(String titulok, Color počiatočnáFarba) { return vyberFarbu(titulok, počiatočnáFarba); }

	/**
	 * <p>Otvorí dialóg na výber farby. Funguje rovnako ako metóda {@link 
	 * #vyberFarbu(String, Color) vyberFarbu}. Otvorený dialóg bude
	 * mať predvolenú zadanú farbu (argument {@code počiatočnáFarba}). Po
	 * zvolení želanej farby používateľom, vráti metóda zvolenú farbu
	 * v novom objekte typu {@link Farba Farba}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel na potvrdenie
	 * a zrušenie dialógu a na reset a miešanie farieb sú upraviteľné
	 * volaním metódy {@link Svet#textTlačidla(String, String) textTlačidla}
	 * triedy {@link Svet Svet}.</p>
	 * 
	 * <p><image>dialog-volby-farby.png<alt/>Dialóg na výber
	 * farby.</image>Dialóg na výber farby.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnáFarba predvolená farba v novo otvorenom dialógu
	 * @return zvolená farba alebo {@code valnull}
	 */
	public static Farba zvoľFarbu(String titulok, Color počiatočnáFarba)
	{
		return PanelFarieb.dialóg(titulok, počiatočnáFarba);

			/* Starý spôsob:
			Color farba = JColorChooser.showDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, počiatočnáFarba);

			if (null == farba) return null;
			return new Farba(farba); */
	}

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String, Color) zvoľFarbu}.</p> */
	public static Farba zvolFarbu(String titulok, Color počiatočnáFarba) { return zvoľFarbu(titulok, počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String, Color) zvoľFarbu}.</p> */
	public static Farba dialógVoľbaFarby(String titulok, Color počiatočnáFarba) { return zvoľFarbu(titulok, počiatočnáFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľFarbu(String, Color) zvoľFarbu}.</p> */
	public static Farba dialogVolbaFarby(String titulok, Color počiatočnáFarba) { return zvoľFarbu(titulok, počiatočnáFarba); }


	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie RGB do HSB.
	 * Argument tejto metódy očakáva zložky farebného priestoru RGB
	 * skombinované v jednej celočíselnej premennej (prípadná zložka
	 * priehľadnosti je ignorovaná).
	 * Hodnoty definujúce farbu farebného priestoru HSB sú vrátené ako
	 * prvky poľa reálnych čísel v rozsahu od {@code num0.0} do
	 * {@code num1.0}. Ide o:<br />
	 *  H – odtieň (hue) farby – vyjadruje farebný odtieň farby v rámci
	 *  viditeľného farebného spektra),<br />
	 *  S – sýtosť (saturation) farby – hodnota {@code num0.0} znamená,
	 *  že farba je odtieňom šedej, hodnota {@code num1.0} vyjadruje
	 *  plne sýte farby,<br />
	 *  B – jas (brightnes) farby.</p>
	 * 
	 * @param rgb farba vyjadrená ako kombinácia zložiek farebného
	 *     priestoru RGB
	 * @return hodnoty definujúce farbu vo farebnom priestore HSB
	 * 
	 * @see #RGBtoHSV(int)
	 */
	public static double[] RGBtoHSB(int rgb)
	{
		float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xff,
			(rgb >> 8) & 0xff, rgb & 0xff, null);
		return new double[]{hsb[0], hsb[1], hsb[2]};
	}

	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie RGB do HSB.
	 * Funguje rovnako ako metóda {@link #RGBtoHSB(int) RGBtoHSB(rgb)},
	 * ibaže očakáva vopred vytvorené trojprvkové pole typu
	 * {@code typedouble}, do ktorého prevedené zložky vloží. Ak je
	 * namiesto poľa zadaná hodnota {@code valnull}, tak metóda pole
	 * vytvorí. Výsledné pole (vytvorené metódou alebo prijaté v druhom
	 * argumente) metóda zároveň vráti vo svojej návratovej hodnote.</p>
	 * 
	 * @param rgb farba vyjadrená ako kombinácia zložiek farebného
	 *     priestoru RGB
	 * @param hsb pole, do ktorého metóda vloží hodnoty definujúce farbu
	 *     vo farebnom priestore HSB alebo hodnota {@code valnull}
	 * @return pole, ktoré bolo metóde odovzdané cez argument {@code hsb},
	 *     alebo nové pole, ktoré metóda vytvorila v prípade prijatia hodnoty
	 *     {@code valnull} v parametri {@code hsb}
	 * 
	 * @see #RGBtoHSV(int, double[])
	 */
	public static double[] RGBtoHSB(int rgb, double[] hsb)
	{
		float[] f_hsb = new float[3];
		Color.RGBtoHSB((rgb >> 16) & 0xff,
			(rgb >> 8) & 0xff, rgb & 0xff, f_hsb);
		if (null == hsb) hsb = new double[3];
		hsb[0] = f_hsb[0]; hsb[1] = f_hsb[1]; hsb[2] = f_hsb[2];
		return hsb;
	}

	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie HSB do RGB. Zložky
	 * farebného priestoru HSB sú očakávané v rozsahu od {@code num0.0}
	 * do {@code num1.0}.
	 * Argumenty sú prepočítané na zložky farebného priestoru RGB
	 * a skombinované do celočíselnej hodnoty vrátenej ako výsledok.</p>
	 * 
	 * @param h odtieň (hue) farby (vyjadruje farbu v rámci viditeľného
	 *     farebného spektra)
	 * @param s sýtosť (saturation) farby ({@code num0.0} – odtiene
	 *     šedej, {@code num1.0} plne sýte farby)
	 * @param b jas (brightnes) farby
	 * @return výsledná kombinácia RGB komponentov
	 * 
	 * @see #HSVtoRGB(double, double, double)
	 */
	public static int HSBtoRGB(double h, double s, double b)
	{
		return Color.HSBtoRGB((float)h, (float)s, (float)b);
	}

	// Konverziu medzi HSV a RGB sme vykonali podľa nasledujúcich zdrojov:

		// https://en.wikipedia.org/wiki/HSL_and_HSV
		// http://www.cs.rit.edu/~ncs/color/t_convert.html
		//   (http://snipplr.com/view/14590)
		// http://stackoverflow.com/questions/2399150/convert-rgb-value-to-hsv
		// avšak v súlade s rozsahmi v javadoc-7.0/api/java/awt/Color.html#
		// HSBtoRGB(float, float, float): h, s, v ‹0, 1›

		// h * 60 = 360
		// h /= 6;

		/*
		int polomer = 200;
		Obrázok paleta = new Obrázok((polomer * 2) + 4, (polomer * 2) + 4);

		for (int y = -polomer; y <= polomer; ++y)
		{
			for (int x = -polomer; x <= polomer; ++x)
			{
				double vzdialenosť = vzdialenosťK(x, y);
				if (vzdialenosť <= polomer)
				{
					int priehľadnosť = (int)(vzdialenosť < (polomer - 1) ? 255 :
						(polomer - vzdialenosť) * 255);

					int farba = priehľadnosť << 24;
					double h = smerNa(x, y) / 360;
					double s = 1.0;
					double v = vzdialenosť / polomer;

					farba |= (Farba.HSVtoRGB(h, s, v) & 0x00ffffff);
					paleta.prepíšBod(x, y, farba);
				}
			}
		}

		obrázok(paleta);
		*/

	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie RGB do HSV.
	 * Argument tejto metódy očakáva zložky farebného priestoru RGB
	 * skombinované v jednej celočíselnej premennej (prípadná zložka
	 * priehľadnosti je ignorovaná).
	 * Hodnoty definujúce farbu farebného priestoru HSV sú vrátené ako
	 * prvky poľa reálnych čísel v rozsahu od {@code num0.0} do
	 * {@code num1.0}. Ide o:<br />
	 *  H – odtieň (hue) farby – vyjadruje farebný odtieň farby v rámci
	 *  viditeľného farebného spektra),<br />
	 *  S – sýtosť (saturation) farby – hodnota {@code num0.0} znamená,
	 *  že farba je odtieňom šedej, hodnota {@code num1.0} vyjadruje
	 *  plne sýte farby,<br />
	 *  V – valér (value) farby – vyjadruje svetlosť farby.</p>
	 * 
	 * @param rgb farba vyjadrená ako kombinácia zložiek farebného
	 *     priestoru RGB
	 * @return hodnoty definujúce farbu vo farebnom priestore HSV
	 * 
	 * @see #RGBtoHSB(int)
	 */
	public static double[] RGBtoHSV(int rgb)
	{
		double r = (double)((rgb >> 16) & 0xff) / 255.0;
		double g = (double)((rgb >>  8) & 0xff) / 255.0;
		double b = (double)(rgb         & 0xff) / 255.0;

		// Valér
		double min = r;
		double max = r;

		if (g < min) min = g;
		if (b < min) min = b;

		if (g > max) max = g;
		if (b > max) max = b;

		double h, s, v = max;
		double chroma = max - min;

		// Sýtosť
		if (max != 0)
		{
			s = chroma / max;
		}
		else
		{
			s = 0;
			h = -1;
			return new double[]{h, s, v};
		}

		// Odtieň
		if (r == max)
			h = ((g - b) / chroma) % 6;	// medzi žltou a purpurovou
		else if (g == max)
			h = 2 + (b - r) / chroma;		// medzi tyrkysovou a žltou
		else
			h = 4 + (r - g) / chroma;		// medzi purpurovou a tyrkysovou

		return new double[]{h, s, v};
	}

	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie RGB do HSV.
	 * Funguje rovnako ako metóda {@link #RGBtoHSV(int) RGBtoHSV(rgb)},
	 * ibaže očakáva trojprvkové pole typu {@code typedouble}, do ktorého
	 * prevedené zložky vloží. Ak je namiesto poľa zadaná hodnota
	 * {@code valnull}, tak metóda pole vytvorí. Výsledné pole (vytvorené
	 * metódou alebo prijaté v druhom argumente) metóda zároveň vráti vo
	 * svojej návratovej hodnote.</p>
	 * 
	 * @param rgb farba vyjadrená ako kombinácia zložiek farebného
	 *     priestoru RGB
	 * @param hsv pole, do ktorého metóda vloží hodnoty definujúce farbu
	 *     vo farebnom priestore HSV alebo hodnota {@code valnull}
	 * @return pole, ktoré bolo metóde odovzdané cez argument {@code hsb},
	 *     alebo nové pole, ktoré metóda vytvorila v prípade prijatia hodnoty
	 *     {@code valnull} v parametri {@code hsb}
	 * 
	 * @see #RGBtoHSB(int, double[])
	 */
	public static double[] RGBtoHSV(int rgb, double[] hsv)
	{
		double r = (double)((rgb >> 16) & 0xff) / 255.0;
		double g = (double)((rgb >>  8) & 0xff) / 255.0;
		double b = (double)(rgb         & 0xff) / 255.0;

		// Valér
		double min = r;
		double max = r;

		if (g < min) min = g;
		if (b < min) min = b;

		if (g > max) max = g;
		if (b > max) max = b;

		// double h, s, v = max;
		if (null == hsv) hsv = new double[3];
		hsv[2] = max;
		double chroma = max - min;

		// Sýtosť
		if (max != 0)
		{
			// s = chroma / max;
			hsv[1] = chroma / max;

			// Odtieň
			if (r == max)
				// h = ((g - b) / chroma) % 6;	// medzi žltou a purpurovou
				hsv[0] = ((g - b) / chroma) % 6;
			else if (g == max)
				// h = 2 + (b - r) / chroma;	// medzi tyrkysovou a žltou
				hsv[0] = 2 + (b - r) / chroma;
			else
				// h = 4 + (r - g) / chroma;	// medzi purpurovou a tyrkysovou
				hsv[0] = 4 + (r - g) / chroma;

			// return new double[]{h, s, v};
		}
		else
		{
			// s = 0;
			// h = -1;
			hsv[1] = 0;
			hsv[0] = -1;
			// return new double[]{h, s, v};
		}

		return hsv;
	}

	/**
	 * <p>Vykoná konverziu z farebnej reprezentácie HSV do RGB. Zložky
	 * farebného priestoru HSV sú očakávané v rozsahu od {@code num0.0}
	 * do {@code num1.0}.
	 * Argumenty sú prepočítané na zložky farebného priestoru RGB
	 * a skombinované do celočíselnej hodnoty vrátenej ako výsledok.</p>
	 * 
	 * @param h odtieň (hue) farby (vyjadruje farbu v rámci viditeľného
	 *     farebného spektra)
	 * @param s sýtosť (saturation) farby ({@code num0.0} – odtiene
	 *     šedej, {@code num1.0} plne sýte farby)
	 * @param v valér (value) farby (vyjadruje svetlosť farby)
	 * @return výsledná kombinácia RGB komponentov
	 * 
	 * @see #HSBtoRGB(double, double, double)
	 */
	public static int HSVtoRGB(double h, double s, double v)
	{
		double r, g, b; int i;

		// Úprava rozsahov argumentov
		if (h > 1.0 || h < 0.0) h -= Math.floor(h);
		if (s > 1.0 || s < 0.0) s -= Math.floor(s);
		if (v > 1.0 || v < 0.0) v -= Math.floor(v);

		if (0 == s)
		{
			// Achromatická „farba“ (šedá)
			r = g = b = v;
		}
		else
		{
			h *= 6;  i = (int)h;		// Nájdi farebný sektor (0 až 5)
			double fact = h - i;		// Rozložiteľná časť odtieňa

			double p = v * (1 - s);
			double q = v * (1 - s * fact);
			double t = v * (1 - s * (1 - fact));

			switch (i)
			{
				case  0: r = v; g = t; b = p; break;
				case  1: r = q; g = v; b = p; break;
				case  2: r = p; g = v; b = t; break;
				case  3: r = p; g = q; b = v; break;
				case  4: r = t; g = p; b = v; break;
				default: r = v; g = p; b = q; // case 5:
			}

			/*
			double c = v * s;
			double x = c * (1 - Math.abs((h % 2) - 1));

			switch (i)
			{
				case  0: r = c; g = x; b = 0; break;
				case  1: r = x; g = c; b = 0; break;
				case  2: r = 0; g = c; b = x; break;
				case  3: r = 0; g = x; b = c; break;
				case  4: r = x; g = 0; b = c; break;
				default: r = c; g = 0; b = x; // case 5:
			}

			double m = v - c;
			r += m; g += m; b += m;
			*/
		}

		i = 0xff00 |  (int)(r * 255);
		i <<= 8; i |= (int)(g * 255);
		i <<= 8; i |= (int)(b * 255);

		return i;
	}

	/**
	 * <p>Reťazcová reprezentácia tejto farby na účely ladenia obsahujúca
	 * informáciu o farebných zložkách a alfe. Pozri aj metódy
	 * {@link #farbaNaReťazec(Color) farbaNaReťazec}
	 * a {@link #reťazecNaFarbu(String) reťazecNaFarbu}.</p>
	 * 
	 * @return reťazcová reprezentácia tejto farby na účely ladenia
	 *     obsahujúca informáciu o farebných zložkách a alfe
	 */
	@Override public String toString()
	{
		return this.getClass().getName() + "[r=" + getRed() + ",g=" +
			getGreen() + ",b=" + getBlue() + ",a=" + getAlpha() + "]";
	}


	// @SuppressWarnings("serial")
	private static TreeMap<Farba, String> mapaFarieb = new TreeMap<>();
	private static void naplňMapu()
	// Inštancie farieb v čase definície ešte nie sú inicializované…
	{
		mapaFarieb.put(žiadna, "žiadna");
		mapaFarieb.put(biela, "biela");
		mapaFarieb.put(svetlošedá, "svetlošedá");
		mapaFarieb.put(šedá, "šedá");
		mapaFarieb.put(tmavošedá, "tmavošedá");
		mapaFarieb.put(čierna, "čierna");
		mapaFarieb.put(svetločervená, "svetločervená");
		mapaFarieb.put(červená, "červená");
		mapaFarieb.put(tmavočervená, "tmavočervená");
		mapaFarieb.put(svetlozelená, "svetlozelená");
		mapaFarieb.put(zelená, "zelená");
		mapaFarieb.put(tmavozelená, "tmavozelená");
		mapaFarieb.put(svetlomodrá, "svetlomodrá");
		mapaFarieb.put(modrá, "modrá");
		mapaFarieb.put(tmavomodrá, "tmavomodrá");
		mapaFarieb.put(svetlotyrkysová, "svetlotyrkysová");
		mapaFarieb.put(tyrkysová, "tyrkysová");
		mapaFarieb.put(tmavotyrkysová, "tmavotyrkysová");
		mapaFarieb.put(svetlopurpurová, "svetlopurpurová");
		mapaFarieb.put(purpurová, "purpurová");
		mapaFarieb.put(tmavopurpurová, "tmavopurpurová");
		mapaFarieb.put(svetložltá, "svetložltá");
		mapaFarieb.put(žltá, "žltá");
		mapaFarieb.put(tmavožltá, "tmavožltá");
		mapaFarieb.put(svetlohnedá, "svetlohnedá");
		mapaFarieb.put(hnedá, "hnedá");
		mapaFarieb.put(tmavohnedá, "tmavohnedá");
		mapaFarieb.put(svetlooranžová, "svetlooranžová");
		mapaFarieb.put(oranžová, "oranžová");
		mapaFarieb.put(tmavooranžová, "tmavooranžová");
		mapaFarieb.put(svetloružová, "svetloružová");
		mapaFarieb.put(ružová, "ružová");
		mapaFarieb.put(tmavoružová, "tmavoružová");
		mapaFarieb.put(uhlíková, "uhlíková");
		mapaFarieb.put(antracitová, "antracitová");
		mapaFarieb.put(papierová, "papierová");
		mapaFarieb.put(snehová, "snehová");
		mapaFarieb.put(tmavofialová, "tmavofialová");
		mapaFarieb.put(fialová, "fialová");
		mapaFarieb.put(svetlofialová, "svetlofialová");
		mapaFarieb.put(tmavoatramentová, "tmavoatramentová");
		mapaFarieb.put(atramentová, "atramentová");
		mapaFarieb.put(svetloatramentová, "svetloatramentová");
		mapaFarieb.put(tmavoakvamarínová, "tmavoakvamarínová");
		mapaFarieb.put(akvamarínová, "akvamarínová");
		mapaFarieb.put(svetloakvamarínová, "svetloakvamarínová");
		mapaFarieb.put(tmaváNebeská, "tmaváNebeská");
		mapaFarieb.put(nebeská, "nebeská");
		mapaFarieb.put(svetláNebeská, "svetláNebeská");
	}

	private final static Pattern hex6Match = Pattern.compile(
		"#[0-9A-Fa-f]{6}");

	private final static Pattern hex3Match = Pattern.compile(
		"#[0-9A-Fa-f]{3}");

	private final static Pattern rgbaMatch = Pattern.compile(
		"[Rr][Gg][Bb][Aa]? *\\(( *[0-9]{0,3} *,){2,3} *[0-9]{0,3} *\\)");

	private final static Pattern rgbaSplit = Pattern.compile(
		"[Rr][Gg][Bb][Aa]? *\\( *| *, *| *\\)");


	/**
	 * <p>Prevedie zadanú inštanciu farby do textovej podoby. (Vhodné
	 * napríklad pri ukladaní údajov do konfiguračného súboru.)</p>
	 * 
	 * @param farba farba, ktorá má byť prevedená do reťazcovej podoby
	 * @return farba prevedená do reťazcovej podoby
	 * 
	 * @see #reťazecNaFarbu(String)
	 */
	public static String farbaNaReťazec(Color farba)
	{
		if (mapaFarieb.isEmpty()) naplňMapu();

		// Pozor, java.awt.Color nie je implementáciou Comparable!
		if (!(farba instanceof Farba)) farba = new Farba(farba);

		// Mapa farieb bola spočiatku naopak, ale
		// z pohľadu efektivity to bolo… zbytočné:
		// for (Map.Entry<Farba, String> položka : mapaFarieb.entrySet())
		// 	if (položka.getValue().equals(farba)) return položka.getKey();

		// Testovanie, ktoré preukázalo, že mapa obsahuje jedinú položku
		// s kľúčom null (inštancie farieb v čase vytvorenia mapy
		// nejestvovali, takže inicializácia v {{…}} bola neúčinná:
		//
		// for (Map.Entry<Farba, String> položka : mapaFarieb.entrySet())
		// {
		// 	System.out.println(položka.getKey() + " " + položka.getValue());
		// 	if (null == položka.getKey()) continue;
		// 	if (položka.getKey().equals(farba)) return položka.getValue();
		// }

		String text = mapaFarieb.get(farba);
		if (null != text) return text;

		int r = farba.getRed();
		int g = farba.getGreen();
		int b = farba.getBlue();
		int a = farba.getAlpha();

		if (255 == a)
		{
			if (r / 16 == r % 16 && g / 16 == g % 16 && b / 16 == b % 16)
				return String.format(Locale.ENGLISH,
					"#%x%x%x", r % 16, g % 16, b % 16);
			return String.format(Locale.ENGLISH,
				"#%02x%02x%02x", r, g, b);
		}

		return String.format(Locale.ENGLISH,
			"rgba(%d, %d, %d, %d)", r, g, b, a);
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaNaReťazec(Color) farbaNaReťazec}.</p> */
	public static String farbaNaRetazec(Color farba)
	{ return farbaNaReťazec(farba); }

	/**
	 * <p>Prevedie zadanú implementáciu farebnosti do textovej podoby.
	 * (Vhodné napríklad pri ukladaní údajov do konfiguračného súboru.)</p>
	 * 
	 * @param farba inštancia farebnosti, ktorá má byť prevedená do
	 *     reťazcovej podoby
	 * @return implementácia farebnosti prevedená do reťazcovej podoby
	 * 
	 * @see #reťazecNaFarbu(String)
	 */
	public static String farbaNaReťazec(Farebnosť farba)
	{ return farbaNaReťazec(farba.farba()); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNaReťazec(Farebnosť) farbaNaReťazec}.</p> */
	public static String farbaNaRetazec(Farebnosť farba)
	{ return farbaNaReťazec(farba); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci farbu na inštanciu farby.
	 * Metóda rozpoznáva buď názvy inštancií rozhrania {@link Farebnosť
	 * Farebnosť} (modrá, červená…), alebo reťazce v tvare:</p>
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
	 * zelená zložka, <em>blue</em> modrá zložka, <em>alpha</em> priehľadnosť
	 * (všetko v desiatkovom tvare).</li>
	 * </ul>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Posledný z uvedených
	 * tvarov nekorešponduje so zápisom používaným v jazykoch slúžiacich
	 * na tvorbu webu (HTML, CSS…), pretože na rozdiel od nich uvádza
	 * z dôvodu zachovania presnej hodnoty zložku priehľadnosti ako celé
	 * číslo v rozsahu od {@code num0} do {@code num255}. Na prevod do
	 * skutočného webového tvaru môžete použiť metódy definované v triede
	 * {@link SVGPodpora SVGPodpora}.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metóde {@link 
	 * #farbaNaReťazec(Color) farbaNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený na farbu
	 * @return inštancia farby získaná z reťazcovej podoby
	 * 
	 * @see #farbaNaReťazec(Color)
	 */
	public static Farba reťazecNaFarbu(String text)
	{
		text = text.trim();
		// Mapa farieb bola spočiatku naopak, ale
		// z pohľadu efektivity to bolo… zbytočné:
		// Farba farba = mapaFarieb.get(text);
		// if (null != farba) return farba;
		try
		{
			Object o = Farebnosť.class.getField(text).get(null);
			if (o instanceof Farba) return (Farba)o;
		}
		catch (Exception e) { /* Nevadí, to je len prvý pokus. */ }

		if (hex6Match.matcher(text).matches())
		{
			try
			{
				return new Farba(
					Integer.parseInt(text.substring(1, 3), 16),
					Integer.parseInt(text.substring(3, 5), 16),
					Integer.parseInt(text.substring(5, 7), 16));
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
				return new Farba(r << 4 | r, g << 4 | g, b << 4 | b);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		}
		else if (rgbaMatch.matcher(text).matches())
		{
			String zložky[] = rgbaSplit.split(text);

			// System.out.println("zložky: " + zložky.length);
			// for (String s : zložky) System.out.println(s);

			try
			{
				if (5 == zložky.length)
					return new Farba(
						Integer.parseInt(zložky[1]),
						Integer.parseInt(zložky[2]),
						Integer.parseInt(zložky[3]),
						Integer.parseInt(zložky[4]));
				return new Farba(
					Integer.parseInt(zložky[1]),
					Integer.parseInt(zložky[2]),
					Integer.parseInt(zložky[3]));
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		}

		return žiadna;
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaFarbu(String) reťazecNaFarbu}.</p> */
	public static Farba retazecNaFarbu(String text)
	{ return reťazecNaFarbu(text); }


	/**
	 * <p>Posúdi, či je zadaný reťazec v rozpoznateľnom formáte
	 * reprezentujúcom farbu.
	 * Metóda rozpoznáva aj názvy inštancií rozhrania {@link Farebnosť
	 * Farebnosť} (modrá, červená…) a uzná za správe reťazce v jednom
	 * z nasledujúcich tvarov:</p>
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
	 * zelená zložka, <em>blue</em> modrá zložka, <em>alpha</em> priehľadnosť
	 * (všetko v desiatkovom tvare)</li>
	 * </ul>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metódam {@link 
	 * #farbaNaReťazec(Color) farbaNaReťazec} a {@link 
	 * #reťazecNaFarbu(String) reťazecNaFarbu}.</p>
	 * 
	 * @param text reťazec, ktorý má byť posúdený
	 * @return {@code val true} ak je zadaný teťazec v rozpoznateľnom
	 *     formáte
	 * 
	 * @see #farbaNaReťazec(Color)
	 * @see #farbaNaReťazec(Farebnosť)
	 * @see #reťazecNaFarbu(String)
	 */
	public static boolean správnyFormát(String text)
	{
		text = text.trim();
		// Mapa farieb bola spočiatku naopak, ale
		// z pohľadu efektivity to bolo… zbytočné:
		// Farba farba = mapaFarieb.get(text);
		// if (null != farba) return farba;
		try
		{
			Object o = Farebnosť.class.getField(text).get(null);
			if (o instanceof Farba) return true;
		}
		catch (Exception e) { /* Nevadí, to je len prvý pokus. */ }

		int číslo = 0;

		if (hex6Match.matcher(text).matches())
		{
			try
			{
				číslo = Integer.parseInt(text.substring(1, 3), 16);
				číslo = Integer.parseInt(text.substring(3, 5), 16);
				číslo = Integer.parseInt(text.substring(5, 7), 16);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		else if (hex3Match.matcher(text).matches())
		{
			try
			{
				číslo = Integer.parseInt(text.substring(1, 2), 16);
				číslo = Integer.parseInt(text.substring(2, 3), 16);
				číslo = Integer.parseInt(text.substring(3, 4), 16);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		else if (rgbaMatch.matcher(text).matches())
		{
			String zložky[] = rgbaSplit.split(text);

			// System.out.println("zložky: " + zložky.length);
			// for (String s : zložky) System.out.println(s);

			try
			{
				if (5 == zložky.length)
				{
					číslo = Integer.parseInt(zložky[1]);
					číslo = Integer.parseInt(zložky[2]);
					číslo = Integer.parseInt(zložky[3]);
					číslo = Integer.parseInt(zložky[4]);
					return true;
				}

				číslo = Integer.parseInt(zložky[1]);
				číslo = Integer.parseInt(zložky[2]);
				číslo = Integer.parseInt(zložky[3]);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #správnyFormát(String) správnyFormát}.</p> */
	public static boolean spravnyFormat(String text)
	{ return správnyFormát(text); }


	/**
	 * <p>Táto metóda je súčasťou implementácie rozhrania {@link Comparable
	 * Comparable}. Porovnanie farieb bolo implementované preto, aby mohli
	 * byť inštancie farieb zaradené do triedeného zoznamu. Nie je možné
	 * objektívne určiť ktorá farba je „väčšia“ alebo „menšia,“ ale farby
	 * sú v počítači reprezentované číslicovo. Táto metóda jednoducho
	 * vzájomne odčíta číslicové reprezentácie farieb a vráti výsledok.
	 * Vďaka tomu je možné použiť inštanciu triedy {@code Farba} ako kľúč
	 * triedeného zoznamu, ktorého použitie je v rámci jazyka Java
	 * efektívnejšie. Tak sa dá vytvoriť triedený zoznam Javy, v ktorom
	 * sa dajú rýchlo vyhľadávať informácie podľa farebného kľúča.</p>
	 * 
	 * @param ináFarba inštancia inej farby, s ktorou má byť táto farba
	 *     porovnaná
	 * @return rozdiel farebných zložiek inej a tejto farby
	 */
	public int compareTo(Color ináFarba)
	{ return getRGB() - ináFarba.getRGB(); }
}
