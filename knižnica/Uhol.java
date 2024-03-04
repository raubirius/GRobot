
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2024 by Roman Horváth
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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
// import java.awt.Shape;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.DecimalFormat;

import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import javax.swing.event.ChangeEvent;
// import javax.swing.event.ChangeListener;

/**
 * <p>Táto trieda uchováva hodnotu uhla použiteľnú na rôznych miestach
 * programovacieho rámca. Trieda je odvoedená od triedy javy {@link Number
 * Number}, implementuje rozhrania {@link Comparable Comparable} a {@link 
 * Smer Smer}, čo všetko zvyšuje jej univerzálnosť a použiteľnosť. Statická
 * metóda {@link #vyberSmer() vyberSmer} (a jej klony) otvorí dialóg na
 * grafický výber uhla. Rovnaký grafický komponent je použitý
 * v konfigurovateľnom {@linkplain Svet#dialóg(String[], Object[], String)
 * dialógu} na vstup (a úpravu) údajov dostupnom cez triedu {@link Svet
 * Svet}.</p>
 * 
 * <!--
 * Poznámka: Autoboxing a unboxing funguje len s primitívnymi údajovými typmi.
 * (https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)
 * -->
 */
@SuppressWarnings("serial")
public final class Uhol extends Number implements Comparable<Uhol>, Smer
{
	// private static final long serialVersionUID = -1L;
	// public static final double POSITIVE_INFINITY = 1.0 / 0.0;
	// public static final double NEGATIVE_INFINITY = -1.0 / 0.0;
	// public static final double NaN = 0.0d / 0.0;

	/**
	 * <p>Konštanta vyjadrujúca teoretickú maximálnu hodnotu uhla:
	 * (2 − 2<sup>−52</sup>) · 2<sup>1023</sup>. Táto hodnota je ekvivalentná
	 * nasledujúcemu literálu zapísaného v šestnástkovej sústave:
	 * <code>0x1.fffffffffffffP+1023</code>.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#MAX_VALUE"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>MAX_VALUE</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final double MAX_VALUE = 0x1.fffffffffffffP+1023; // 1.7976931348623157e+308

	/**
	 * <p>Konštanta vyjadrujúca najmenšiu pozitívnu normálnu hodnotu tohto
	 * údajového typu: 2<sup>−1022</sup>. Táto hodnota je ekvivalentná
	 * nasledujúcemu literálu zapísaného v šestnástkovej sústave:
	 * <code>0x1.0p-1022</code>.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#MIN_NORMAL"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>MIN_NORMAL</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final double MIN_NORMAL = 0x1.0p-1022; // 2.2250738585072014E-308

	/**
	 * <p>Konštanta vyjadrujúca najmenšiu pozitívnu nenulovú hodnotu tohto
	 * údajového typu: 2<sup>−1074</sup>. Táto hodnota je ekvivalentná
	 * nasledujúcemu literálu zapísaného v šestnástkovej sústave:
	 * <code>0x0.0000000000001P-1022</code>.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#MIN_VALUE"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>MIN_VALUE</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final double MIN_VALUE = 0x0.0000000000001P-1022; // 4.9e-324

	/**
	 * <p>Najväčší technicky dovolený exponent konečného čísla premenných
	 * tohto údajového typu: 1023.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#MAX_EXPONENT"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>MAX_EXPONENT</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final int MAX_EXPONENT = 1023;

	/**
	 * <p>Najmenší technicky dovolený exponent normalizovaného čísla
	 * premenných tohto údajového typu: -1022.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#MIN_EXPONENT"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>MIN_EXPONENT</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final int MIN_EXPONENT = -1022;

	/**
	 * <p>Počet bitov použitých na reprezentáciu hodnoty uloženej
	 * v inštanciách tohto údajového typu (tejto triedy): 64.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#SIZE"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>SIZE</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final int SIZE = 64;

	/**
	 * <p>Počet bajtov použitých na reprezentáciu hodnoty uloženej
	 * v inštanciách tohto údajového typu (tejto triedy). Hodnota je rovná
	 * výsledku výpočtu: <code>SIZE / Byte.SIZE</code>.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#BYTES"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>BYTES</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final int BYTES = SIZE / Byte.SIZE;

	/**
	 * <p>Inštancia triedy {@link java.lang.Class Class} reprezentujúca
	 * primitívny údajový typ {@code typedouble}, ktorý je použitý na
	 * uchovanie hodnoty uhlov (inštancií tejto triedy).</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#TYPE"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>TYPE</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 */
	public static final Class<Double> TYPE = Double.TYPE;


	// Hodnota tejto inštancie uhla.
	private final double hodnota;

	/**
	 * <p>Konštruktor uhla prijímajúci reálnočíselnú hodnotu typu
	 * {@code typedouble} (alebo kompatibilného).</p>
	 * 
	 * @param hodnota hodnota tohto uhla
	 */
	public Uhol(double hodnota)
	{
		this.hodnota = hodnota;
	}

	/**
	 * <p>Konštruktor uhla prijímajúci hodnotu uhla v reťazcovej podobe.
	 * Reťazec je prevedený na reálne číslo typu {@code typedouble}
	 * s použitím metódy rámca {@link Svet#reťazecNaReálneČíslo(String)
	 * reťazecNaReálneČíslo}, pričom je navyše povolené, aby bol reťazec
	 * ukončený znakom stupňa (°), ktorý je pred prevodom vymazaný. Iné
	 * znaky používané pri zápisoch uhlov (minúty ′, sekundy ″, prípadne
	 * iné) nie sú v súčasnosti povolené.</p>
	 * 
	 * @param s reťazec, ktorý bude prevedený na reálne číslo s nepovinným
	 *     uvedením znaku stupňa (°) na konci
	 * 
	 * @throws NumberFormatException
	 */
	public Uhol(String s) throws NumberFormatException
	{
		if (s.endsWith("°")) s = s.substring(0, s.length() - 1);
		Double prevod = Svet.reťazecNaReálneČíslo(s);
		if (null == prevod) throw new NumberFormatException(s);
		hodnota = prevod;
	}

	/**
	 * <p>Táto metóda vráti {@code valtrue} v prípade, že vnútorná hodnota
	 * uhla v tejto inštancii má hodnotu <a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#NaN"
	 * target="_blank"><code>NaN</code></a> (Not-a-Number – „nie je číslo“)
	 * a {@code valfalse} v opačnom prípade.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#isNaN--"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>isNaN</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 * 
	 * @return {@code valtrue} ak je hodnota reprezentovaná touto inštanciou <a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#NaN"
	 * target="_blank"><code>NaN</code></a>, {@code valfalse} v opačnom prípade
	 */
	public boolean isNaN()
	{
		return Double.isNaN(hodnota);
	}

	/**
	 * <p>Vráti {@code valtrue} v prípade, že hodnota uhla uložená v tejto
	 * inštancii má nekonečnú veľkosť a {@code valfalse} v opačnom
	 * prípade.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#isInfinite--"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>isInfinite</code>.</em> Oracle. Citované: 2018.</a></li></ul>
	 * 
	 * @return {@code valtrue}, ak je hodnota uhla tejto inštancie kladné
	 *     alebo záporné nekonečno; {@code valfalse} v opačnom prípade
	 */
	public boolean isInfinite()
	{
		return Double.isInfinite(hodnota);
	}

	/**
	 * <p>Vráti {@code valtrue}, ak je hodnota uhla uložená v tejto inštancii
	 * konečnou reálnočíselnou hodnotou (typu {@code typedouble}). To znamená,
	 * že ak je hodnota v tejto inštancii rovná napríklad <a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#NaN"
	 * target="_blank"><code>NaN</code></a> alebo niektoré nekonečno (kladné,
	 * záporné), tak metóda vráti {@code valfalse}.</p>
	 * 
	 * <p><b>Zdroj:</b></p>
	 * 
	 * <ul><li><a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#isFinite--"
	 * target="_blank"><em>Double (Java Platform SE 8) –
	 * <code>isFinite(d)</code> (static version).</em> Oracle. Citované:
	 * 2018.</a></li></ul>
	 * 
	 * @return {@code valtrue}, ak je hodnota uhla tejto inštancie konečná;
	 *     {@code valfalse} v opačnom prípade
	 */
	public boolean isFinite()
	{
		return Double.isFinite(hodnota);
	}

	/**
	 * <p>Prevedie hodnotu tejto inštancie na reťazec s použitím metódy
	 * inštancie {@link Svet#formát Svet.formát}{@code .}{@link 
	 * java.text.DecimalFormat#format(double) format} a pripojí na koniec
	 * výsledného reťazca znak stupňa (°).</p>
	 * 
	 * @return hodnota uložená v tejto inštancii uhla prevedená na reťazec
	 */
	@Override public String toString()
	{
		return Svet.formát.format(hodnota) + "°";
	}


	/**
	 * <p>Vykoná primitívnu typovú konverziu údajového typu {@code typedouble},
	 * ktorý vnútorne uchováva hodnotu uhla tejto inštancie, na primitívny typ
	 * {@code typebyte} a vráti výsledok. (Táto metóda prekrýva metódu
	 * rodičovskej triedy {@link Number Number}.)</p>
	 * 
	 * @return vráti uhol konvertovaný na typ {@code typebyte} (pôvodná
	 *     hodnota uhla nie je nijako prispôsobená; riziko straty údajov
	 *     je vysoké)
	 */
	public byte byteValue()
	{
		return (byte)hodnota;
	}

	/**
	 * <p>Vykoná primitívnu typovú konverziu údajového typu {@code typedouble},
	 * ktorý vnútorne uchováva hodnotu uhla tejto inštancie, na primitívny typ
	 * {@code typeshort} a vráti výsledok. (Táto metóda prekrýva metódu
	 * rodičovskej triedy {@link Number Number}.)</p>
	 * 
	 * @return vráti uhol konvertovaný na typ {@code typeshort}
	 */
	public short shortValue()
	{
		return (short)hodnota;
	}

	/**
	 * <p>Vykoná primitívnu typovú konverziu údajového typu {@code typedouble},
	 * ktorý vnútorne uchováva hodnotu uhla tejto inštancie, na primitívny typ
	 * {@code typeint} a vráti výsledok. (Táto metóda je súčasťou
	 * implementácie abstraktnej triedy {@link Number Number}.)</p>
	 * 
	 * @return vráti uhol konvertovaný na typ {@code typeint}
	 */
	public int intValue()
	{
		return (int)hodnota;
	}

	/**
	 * <p>Vykoná primitívnu typovú konverziu údajového typu {@code typedouble},
	 * ktorý vnútorne uchováva hodnotu uhla tejto inštancie, na primitívny typ
	 * {@code typelong} a vráti výsledok. (Táto metóda je súčasťou
	 * implementácie abstraktnej triedy {@link Number Number}.)</p>
	 * 
	 * @return vráti uhol konvertovaný na typ {@code typelong}
	 */
	public long longValue()
	{
		return (long)hodnota;
	}

	/**
	 * <p>Vykoná primitívnu typovú konverziu údajového typu {@code typedouble},
	 * ktorý vnútorne uchováva hodnotu uhla tejto inštancie, na primitívny typ
	 * {@code typefloat} a vráti výsledok. (Táto metóda je súčasťou
	 * implementácie abstraktnej triedy {@link Number Number}.)</p>
	 * 
	 * @return vráti uhol konvertovaný na typ {@code typefloat}
	 */
	public float floatValue()
	{
		return (float)hodnota;
	}

	/**
	 * <p>Vráti hodnotu uhla tejto inštancie, ktorá má primitívny údajový typ
	 * {@code typedouble}. (Táto metóda je zároveň súčasťou implementácie
	 * abstraktnej triedy {@link Number Number}.)</p>
	 * 
	 * @return hodnota uhla tejto inštancie
	 * 
	 * @see #uhol()
	 * @see #smer()
	 */
	public double doubleValue()
	{
		return hodnota;
	}

	/**
	 * <p>Vytvorí hešovací kód pre túto inštanciu. Metóda využíva svoju
	 * statickú verziu {@link #hashCode(double) hashCode}.</p>
	 * 
	 * @return heš tejto inštancie uhla
	 */
	@Override public int hashCode()
	{
		return Uhol.hashCode(hodnota);
	}

	/**
	 * <p>Prijme ľubovoľnú inštanciu, overí, či ide o inštanciu triedy
	 * {@link Uhol Uhol} a ak áno, tak vyhodnotí jej zhodu s hodnotou tejto
	 * inštancie uhla. Ak sú hodnoty uhlov tejto a zadanej inštancie zhodné,
	 * tak metóda vráti hodnotu {@code valtrue}, inak vráti hodnotu
	 * {@code valfalse}</p>
	 * 
	 * @param objekt objekt ľubovoľnej inštancie, s ktorým má byť
	 *     vyhodnotená zhoda
	 * @return ak je zadaná inštancia typu {@link Uhol Uhol} a hodnoty uhlov
	 *     tejto aj zadanej inštancie sú zhodné, tak je vrátená hodnota
	 *     {@code valtrue}, inak {@code valfalse}
	 */
	@Override public boolean equals(Object objekt)
	{
		return (objekt instanceof Uhol) &&
			(Double.doubleToLongBits(((Uhol)objekt).hodnota) ==
				Double.doubleToLongBits(hodnota));
	}

	/**
	 * <p>Porovná hodnotu tejto inštancie s hodnotou zadanej inštancie.
	 * Vrátený výsledok bude v súlade s algoritmom metódy {@link Double
	 * Double}{@code .}{@link Double#compare(double, double) compare}.</p>
	 * 
	 * @param inýUhol inštancia iného uhla na porovnanie
	 * @return výsledok v súlade s algoritmom metódy {@link Double
	 *     Double}{@code .}{@link Double#compare(double, double) compare}
	 */
	public int compareTo(Uhol inýUhol)
	{
		return Double.compare(hodnota, inýUhol.hodnota);
	}

	/**
	 * <p>Porovná dve zadané reálnočíselné hodnoty (uhlov) v súlade
	 * s algoritmom metódy {@link Double Double}{@code .}{@link 
	 * Double#compare(double, double) compare}.</p>
	 * 
	 * @param u1 hodnota prvého uhla na porovnanie
	 * @param u1 hodnota druhého uhla na porovnanie
	 * @return výsledok v súlade s algoritmom metódy {@link Double
	 *     Double}{@code .}{@link Double#compare(double, double) compare}
	 */
	public static int compare(double u1, double u2)
	{
		return Double.compare(u1, u2);
	}

	/**
	 * <p>Implementácia prototypu metódy na zistenie uhla/smeru objektu.</p>
	 * 
	 * @return hodnota uhla (smeru) uložená v tejto inštancii
	 * 
	 * @see #doubleValue()
	 * @see #smer()
	 */
	public double uhol() { return hodnota; }

	/**
	 * <p>Implementácia prototypu metódy na zistenie smeru/uhla objektu.</p>
	 * 
	 * @return hodnota smeru (uhla) uložená v tejto inštancii
	 * 
	 * @see #doubleValue()
	 * @see #uhol()
	 */
	public double smer() { return hodnota; }


	/**
	 * <p>Prevedie zadanú reálnočíselnú hodnotu uhla na reťazec s použitím
	 * metódy inštancie {@link Svet#formát Svet.formát}{@code .}{@link 
	 * java.text.DecimalFormat#format(double) format}, pričom pripojí na
	 * koniec výsledného reťazca znak stupňa (°).</p>
	 * 
	 * @param d reálnočíselná hodnota uhla, ktorá má byť prevedená na reťazec
	 * @return zadaná hodnota uhla prevedená na reťazec
	 * 
	 * @throws NumberFormatException
	 */
	public static String toString(double d)
	{
		return Svet.formát.format(d) + "°";
	}

	// public static String toHexString(double d)
	// { return Double.toHexString(d); }

	/**
	 * <p>Vytvorí novú inštanciu uhla s použitím konštruktora
	 * {@link #Uhol(String) Uhol(s)}.</p>
	 * 
	 * @param s reťazec, z ktorého bude vytvorená nová inštancia uhla
	 * @return nová inštancia uhla
	 * 
	 * @throws NumberFormatException
	 */
	public static Uhol valueOf(String s) throws NumberFormatException
	{
		return new Uhol(s);
	}

	/**
	 * <p>Vytvorí novú inštanciu uhla s použitím konštruktora
	 * {@link #Uhol(double) Uhol(d)}.</p>
	 * 
	 * @param d hodnota novej inštancie uhla
	 * @return nová inštancia uhla
	 */
	public static Uhol valueOf(double d)
	{
		return new Uhol(d);
	}

	/**
	 * <p>Prevedie zadaný reťazec na číselnú hodnotu uhla s použitím rovnakých
	 * pravidiel ako konštruktor {@link #Uhol(String) Uhol(s)}. Túto metódu
	 * vnútorne používa aj panel smeru slúžiaci na grafickú voľbu uhla –
	 * pozri napríklad metódu {@link #vyberSmer() vyberSmer}.</p>
	 * 
	 * @param s reťazec reprezentujúci uhol, ktorý bude prevedený na
	 *     reálnočíselnú hodnotu
	 * @return prevedená číselná hodnota uhla
	 * 
	 * @throws NumberFormatException
	 */
	public static double parseUhol(String s) throws NumberFormatException
	{
		if (s.endsWith("°")) s = s.substring(0, s.length() - 1);
		Double prevod = Svet.reťazecNaReálneČíslo(s);
		if (null == prevod) throw new NumberFormatException(s);
		return prevod;
	}

	// public static boolean isNaN(double v) { return v != v; }

	// public static boolean isInfinite(double v)
	// { return v == POSITIVE_INFINITY || v == NEGATIVE_INFINITY; }

	// public static boolean isFinite(double d)
	// { return Math.abs(d) <= Double.MAX_VALUE; }

	/**
	 * <p>Vytvorí hešovací kód pre zadanú reálnočíselnú hodnotu
	 * (reprezentujúcu uhol).</p>
	 * 
	 * @param hodnota hodnota (ktorá by mala reprezentovať uhol)
	 * @return heš vytvorený zo zadanej hodnoty
	 */
	public static int hashCode(double hodnota)
	{
		long bity = Double.doubleToLongBits(hodnota);
		return (int)(bity ^ (bity >>> 32));
	}

	// public static long doubleToLongBits(double hodnota)
	// { return Double.doubleToLongBits(hodnota); }

	// public static long doubleToRawLongBits(double hodnota)
	// { return Double.doubleToRawLongBits(hodnota); }

	// public static double longBitsToDouble(long bity)
	// { return Double.longBitsToDouble(bity); }

	// public static double sum(double a, double b) { return a + b; }
	// public static double max(double a, double b) { return Math.max(a, b); }
	// public static double min(double a, double b) { return Math.min(a, b); }


		// Panel smerov používaný v dialógoch výberu smeru a voľby
		// rôznych parametrov (pozri: Uhol.vyberSmer a Svet.dialóg).
		@SuppressWarnings("serial")
		/*packagePrivate*/ static class PanelSmeru extends JPanel
		{
			// Formátovač uhlov pre textové pole spinera.
			private final static JFormattedTextField.AbstractFormatter
				formátovač = new JFormattedTextField.AbstractFormatter()
				{
					public Object stringToValue(String text)
					{
						try
						{
							return Uhol.parseUhol(text);
						}
						catch (NumberFormatException e)
						{
							// swallow, just go through and beep
						}
						Svet.pípni();
						return null;
					}

					public String valueToString(Object value)
					{
						if (value instanceof Number)
							return Uhol.toString(
								((Number)value).doubleValue());
						Svet.pípni();
						return "0°";
					}
				};

			// Továreň formátovača uhlov textového poľa spinera.
			private final static JFormattedTextField.AbstractFormatterFactory
				továreň = new JFormattedTextField.AbstractFormatterFactory()
				{
					public JFormattedTextField.AbstractFormatter
						getFormatter(JFormattedTextField tf)
					{
						return formátovač;
					}
				};

			// Štýl čiary kreslenia medzikružia:
			private BasicStroke čiaraMedzikružia = new BasicStroke(
				(float)1.5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

			// Oblasť medzikružia odporúčaného na voľbu uhla:
			private Area medzikružie = null;

			// Objekty komponentu ukážky zvoleného smeru na paneli:
			private BufferedImage obrázokUkážky;
			private Graphics2D grafikaUkážky;
			private ImageIcon ikonaUkážky;
			private JLabel komponentUkážky;
			private int[] údajeUkážky;

			// Ovládacie prvky (vstupné polia, tlačidlo resetu)
			// s ich panelom:
			private JSpinner uprav;
			private JButton tlačidloReset;
			private JPanel panelPrvkov;

			// Atribúty tohto panela smerov:
			private double predvolenýSmer = 0;
			private double zvolenýSmer = 0;
			private double faktorMierky = 1.00;
			private int veľkosťUkážky = 150;


			/**
			 * <p>Konštruktor.</p>
			 * 
			 * @param textReset text tlačidla resetu smeru
			 * @param smer predvolený smer na paneli smerov
			 * @param mierka mierka plochy na výber smeru, ktorá je súčasne
			 *     ukážkou zvoleného smeru
			 */
			public PanelSmeru(String textReset, Smer smer, double mierka)
			{
				if (null != smer)
				{
					predvolenýSmer = smer.smer();
					zvolenýSmer = predvolenýSmer;
				}

				if (mierka > 0.0) faktorMierky = mierka;
				// System.out.println("faktorMierky 1: " + faktorMierky);

				if (1.00 != faktorMierky)
				{
					double veľkosť = 150 * faktorMierky;
					// System.out.println("veľkosť 1: " + veľkosť);
					if (75.0 >= veľkosť)
					{
						faktorMierky = 0.5;
						veľkosť = 75.0;
						// System.out.println("veľkosť 2: " + veľkosť);
						// System.out.println("faktorMierky 2: " +
						// 	faktorMierky);
					}
					veľkosťUkážky = (int)veľkosť;
				}

				obrázokUkážky = new BufferedImage(
					veľkosťUkážky, veľkosťUkážky, BufferedImage.TYPE_INT_ARGB);
				údajeUkážky = null;
				grafikaUkážky = obrázokUkážky.createGraphics();
				grafikaUkážky.addRenderingHints(Obrázok.hints);
				ikonaUkážky = new ImageIcon(obrázokUkážky);
				komponentUkážky = new JLabel(ikonaUkážky);
				komponentUkážky.setBorder(BorderFactory.
					createEmptyBorder(10, 10, 10, 10));

				tlačidloReset = new JButton(
					null == textReset ? "Reset" : textReset);
				tlačidloReset.setAlignmentX(Component.CENTER_ALIGNMENT);

				setLayout(new BorderLayout());

				panelPrvkov = new JPanel();
				panelPrvkov.setLayout(new BoxLayout(
					panelPrvkov, BoxLayout.Y_AXIS));

				add(komponentUkážky, BorderLayout.WEST);
				panelPrvkov.add(Box.createVerticalStrut(10));

				{
					SpinnerNumberModel modelSpinera = new SpinnerNumberModel(
						(java.lang.Double)0.0, null, null,
						(java.lang.Double)1.0);
					uprav = new JSpinner(modelSpinera);

					JSpinner.NumberEditor editor = new JSpinner.
						NumberEditor(uprav);/*, Svet.formát.toPattern());
					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);*/

					uprav.setEditor(editor);

					JFormattedTextField textField = editor.getTextField();
					textField.setFormatterFactory(továreň);
					textField.setHorizontalAlignment(JTextField.CENTER);
					textField.setPreferredSize(new Dimension(50, 20));

					uprav.addChangeListener(e -> aktualizujPodľaSpinera(e));

					JPanel panel = new JPanel();
					panel.add(new JLabel("∠:"));
					panel.add(uprav);
					panel.setBorder(BorderFactory.
						createEmptyBorder(0, 10, 0, 10));

					panelPrvkov.add(panel);
					// panelPrvkov.add(uprav);

					// JFormattedTextField textField = null;
					// JComponent editor = uprav.getEditor();
					// if (editor instanceof JSpinner.DefaultEditor)
					// 	textField = ((JSpinner.DefaultEditor)editor).
					// 	getTextField();
				}

				// panelPrvkov.add(Box.createVerticalStrut(10));
				// panelPrvkov.add(Box.createRigidArea(new Dimension(100, 10)));


				panelPrvkov.add(Box.createVerticalGlue());
				panelPrvkov.add(tlačidloReset);
				panelPrvkov.add(Box.createVerticalGlue());

				add(panelPrvkov, BorderLayout.EAST);

				komponentUkážky.addMouseListener(new MouseListener()
					{
						public void mouseClicked(MouseEvent e) {}
						public void mouseEntered(MouseEvent e) {}
						public void mouseExited(MouseEvent e) {}
						public void mousePressed(MouseEvent e)
						{ aktualizujPodľaMyši(e); }
						public void mouseReleased(MouseEvent e) {}
					});

				komponentUkážky.addMouseMotionListener(
					new MouseMotionListener()
					{
						public void mouseMoved(MouseEvent e) {}
						public void mouseDragged(MouseEvent e)
						{ aktualizujPodľaMyši(e); }
					});

				tlačidloReset.addActionListener(e ->
					{
						zvolenýSmer = predvolenýSmer;
						aktualizujUkážku();
						aktualizujEditor();
					});

				aktualizujUkážku();
				aktualizujEditor();
			}

			// Aktualizácia ukážky zvoleného smeru.
			private void aktualizujUkážku()
			{
				if (null == údajeUkážky)
					údajeUkážky = ((DataBufferInt)obrázokUkážky.
						getRaster().getDataBuffer()).getData();
				Arrays.fill(údajeUkážky, 0);

				double polovica = veľkosťUkážky / 2.0;
				double sinα = Math.sin(Math.toRadians(zvolenýSmer));
				double cosα = Math.cos(Math.toRadians(zvolenýSmer));

				if (null == medzikružie)
				{
					medzikružie = new Area(new Ellipse2D.Double(2, 2,
						veľkosťUkážky - 4, veľkosťUkážky - 4));
					medzikružie.subtract(new Area(new Ellipse2D.Double(
						polovica - 5, polovica - 5, 10, 10)));
				}

				grafikaUkážky.setColor(Farebnosť.biela);
				grafikaUkážky.fill(medzikružie);

				grafikaUkážky.setColor(Farebnosť.čierna);
				grafikaUkážky.draw(new Line2D.Double(
					polovica + cosα * 6.0,
					polovica - sinα * 6.0,
					polovica + cosα * (polovica - 8.0),
					polovica - sinα * (polovica - 8.0)));

				grafikaUkážky.setColor(Farebnosť.svetlošedá);
				grafikaUkážky.setStroke(čiaraMedzikružia);
				grafikaUkážky.draw(medzikružie);

				komponentUkážky.repaint();
			}

			// Zaokrúhli zadané číslo na počet desatinných miest. (Záporný
			// počet miest zaokrúhľuje na miesta pred desatinnou čiarkou.)
			private static double round(double value, int places)
			{
				// if (places < 0) throw new IllegalArgumentException();
				BigDecimal bd = new BigDecimal(value);
				bd = bd.setScale(places, RoundingMode.HALF_UP);
				return bd.doubleValue();
			}

			// Aktualizuje súradnice zvoleného smeru podľa udalosti myši.
			private void aktualizujPodľaMyši(MouseEvent e)
			{
				// System.out.println("  Súradnice myši: " +
				// 	e.getX() + ", " + e.getY());

				zvolenýSmer = Math.toDegrees(Math.atan2(
					-e.getY() + 10 + (veľkosťUkážky / 2),
					 e.getX() - 10 - (veľkosťUkážky / 2)));
				if (zvolenýSmer < 0) zvolenýSmer += 360;
				zvolenýSmer = round(zvolenýSmer, 3);
				// System.out.println("  Zvolený smer: " + zvolenýSmer);
				aktualizujUkážku();
				aktualizujEditor();
			}

			// Aktualizuje súradnice zvoleného smeru podľa zmeny v spineroch.
			private void aktualizujPodľaSpinera(ChangeEvent e)
			{
				if (e.getSource() instanceof JSpinner)
				{
					JSpinner spiner = (JSpinner)e.getSource();
					Object v = spiner.getValue();
					if (v instanceof Number)
					{
						if (spiner == uprav)
						{
							zvolenýSmer = ((Number)v).doubleValue();
							if (Double.isFinite(zvolenýSmer))
							{
								boolean upravené = false;
								if (zvolenýSmer >= 360)
								{
									do {
										zvolenýSmer -= 360;
									} while (zvolenýSmer >= 360);
									upravené = true;
								}
								if (zvolenýSmer < 0)
								{
									do {
										zvolenýSmer += 360;
									} while (zvolenýSmer < 0);
									upravené = true;
								}
								if (upravené) spiner.setValue(zvolenýSmer);
							}
							aktualizujUkážku();
						}
					}
				}
			}

			// Aktualizuje hodnoty zvoleného smeru v editoroch.
			private void aktualizujEditor()
			{
				uprav.setValue(zvolenýSmer);
				// {
				// 	JSpinner.NumberEditor editor =
				// 		(JSpinner.NumberEditor)uprav.getEditor();
				// 	editor.commitEdit();
				// }
			}

			// Aktualizuje tento panel do takého stavu, v akom by sa
			// nachádzal po konštrukcii so zadanými hodnotami parametrov.
			// (Môžu nastať drobné odchýlky, ktoré sú neodsledovateľné, ale
			// zhruba by sa panel mal vizuálne aj vnútorne nachádzať
			// v požadovanom stave.)
			private void aktualizujPanel(String textReset,
				Smer smer, double mierka)
			{
				// aktualizujFormát();
				if (mierka <= 0.0 && faktorMierky != 1.0) mierka = 1.0;
				aktualizujVeľkosťUkážky(mierka);

				upravTextTlačidla(textReset);
				nastavSmer(smer);
			}

			/* *
			 * <p>Aktualizuje formáty čísiel podľa formátu sveta.</p>
			 * /
			public void aktualizujFormát()
			{
				{
					JSpinner.NumberEditor editor =
						(JSpinner.NumberEditor)uprav.getEditor();

					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);
				}

				aktualizujEditor();
			}*/

			/**
			 * <p>Aktualizuje veľkosť ukážky zvoleného smeru.</p>
			 * 
			 * @param mierka mierka plochy na výber smeru, ktorá je súčasne
			 *     ukážkou zvoleného smeru
			 */
			public void aktualizujVeľkosťUkážky(double mierka)
			{
				// System.out.println("faktorMierky 1: " + faktorMierky +
				// 	" (" + mierka + ")");
				if (mierka > 0.0 && faktorMierky != mierka)
				{
					faktorMierky = mierka;
					double veľkosť = 150.0 * faktorMierky;
					// System.out.println("veľkosť 1: " + veľkosť);
					if (75.0 >= veľkosť)
					{
						faktorMierky = 0.5;
						veľkosť = 75.0;
						// System.out.println("veľkosť 2: " + veľkosť);
						// System.out.println("faktorMierky 2: " +
						// 	faktorMierky);
					}
					veľkosťUkážky = (int)veľkosť;
					medzikružie = null;

					obrázokUkážky = new BufferedImage(
						veľkosťUkážky, veľkosťUkážky,
						BufferedImage.TYPE_INT_ARGB);
					údajeUkážky = null;
					grafikaUkážky = obrázokUkážky.createGraphics();
					grafikaUkážky.addRenderingHints(Obrázok.hints);
					ikonaUkážky = new ImageIcon(obrázokUkážky);
					komponentUkážky.setIcon(ikonaUkážky);
				}
			}

			/**
			 * <p>Nastavenie novej predvolenej smeru na paneli.</p>
			 * 
			 * @param novýSmer nová predvolený smer na paneli
			 */
			public void nastavSmer(Smer novýSmer)
			{
				if (null == novýSmer)
				{
					zvolenýSmer = predvolenýSmer = 0;
				}
				else
				{
					predvolenýSmer = novýSmer.smer();
					zvolenýSmer = predvolenýSmer;
				}

				aktualizujUkážku();
				aktualizujEditor();
			}

			/**
			 * <p>Získanie zvoleného smeru na paneli.</p>
			 * 
			 * @param novýSmer nová predvolený smer na paneli
			 */
			public Uhol dajSmer()
			{
				return new Uhol(zvolenýSmer);
			}

			/**
			 * <p>Upraví predvolený text tlačidla resetu smeru na paneli.</p>
			 * 
			 * @param textReset text tlačidla resetu smeru na paneli
			 */
			public void upravTextTlačidla(String textReset)
			{
				if (null != textReset)
					tlačidloReset.setText(textReset);
			}


			// Statický panel dialógu voľby smeru.
			private static PanelSmeru panelSmeru = null;

			/**
			 * <p>Otvorí dialóg s panelom na výber smeru. Metóda prijíma
			 * parameter určujúci titulok dialógového okna a predvolený smer
			 * na paneli smerov. Ak je niektorá z hodnôt rovná {@code valnull},
			 * tak bude zvolená vhodná hodnota.</p>
			 * 
			 * @param titulok titulok okna dialógu
			 * @param predvolenýSmer predvolený smer na paneli
			 * @return zvolený smer alebo {@code valnull} (ak používateľ
			 *     dialóg zavrel)
			 */
			public static Uhol dialóg(String titulok, Smer predvolenýSmer)
			{
				if (null == panelSmeru)
					panelSmeru = new PanelSmeru(
						Svet.tlačidláDialógu[3], predvolenýSmer, 0);
				else
					panelSmeru.aktualizujPanel(
						Svet.tlačidláDialógu[3], predvolenýSmer, 0);

				Object[] komponenty = new Object[] {panelSmeru};

				if ((Svet.odpoveďDialógu = JOptionPane.showOptionDialog(null ==
					Svet.oknoCelejObrazovky ? GRobot.svet : Svet.
					oknoCelejObrazovky, komponenty, null == titulok ?
					"Voľba smeru" : titulok, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, null != Svet.mojeOdpovede ?
					Svet.mojeOdpovede : Svet.odpovedeZadania, null)) ==
					JOptionPane.YES_OPTION)
					return panelSmeru.dajSmer();

				return null;
			}
		}

	// Predvolený smer dialógov.
	// private final static Uhol sever = new Uhol(90);


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #zvoľSmer() zvoľSmer}. Predvoleným smerom v otvorenom
	 * dialógu bude hodnota 90°. Po zvolení želaného smeru používateľom,
	 * vráti metóda zvolený smer v novom objekte typu {@link Uhol Uhol}.
	 * Ak používateľ dialóg zruší, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol vyberSmer()
	{ return PanelSmeru.dialóg(null, sever); }


	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer() vyberSmer}.</p> */
	public static Uhol dialógVýberSmeru() { return vyberSmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer() vyberSmer}.</p> */
	public static Uhol dialogVyberSmeru() { return vyberSmer(); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #zvoľSmer(Smer) zvoľSmer}. Otvorený dialóg bude mať predvolený
	 * zadaný smer (argument {@code počiatočnýSmer}). Po zvolení želaného
	 * smeru používateľom, vráti metóda zvolený smer v novom objekte typu
	 * {@link Uhol Uhol}. Ak používateľ dialóg zruší, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param počiatočnýSmer predvolený smer v novo otvorenom dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol vyberSmer(Smer počiatočnýSmer)
	{ return PanelSmeru.dialóg(null, počiatočnýSmer); }


	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(Smer) vyberSmer}.</p> */
	public static Uhol dialógVýberSmeru(Smer počiatočnýSmer)
	{ return vyberSmer(počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(Smer) vyberSmer}.</p> */
	public static Uhol dialogVyberSmeru(Smer počiatočnýSmer)
	{ return vyberSmer(počiatočnýSmer); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #zvoľSmer(String) zvoľSmer}. Predvoleným smerom v otvorenom
	 * dialógu bude hodnota 90°. Po zvolení želaného smeru používateľom,
	 * vráti metóda zvolený smer v novom objekte typu {@link Uhol Uhol}.
	 * Ak používateľ dialóg zruší, tak metóda vráti hodnotu {@code valnull}.
	 * Programátor má možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol vyberSmer(String titulok)
	{ return PanelSmeru.dialóg(titulok, sever); }


	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(String) vyberSmer}.</p> */
	public static Uhol dialógVýberSmeru(String titulok)
	{ return vyberSmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(String) vyberSmer}.</p> */
	public static Uhol dialogVyberSmeru(String titulok)
	{ return vyberSmer(titulok); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #zvoľSmer(String, Smer) zvoľSmer}. Otvorený dialóg bude mať predvolený
	 * zadaný smer (argument {@code počiatočnýSmer}). Po zvolení želaného
	 * smeru používateľom, vráti metóda zvolený smer v novom objekte typu
	 * {@link Uhol Uhol}. Ak používateľ dialóg zruší, tak metóda vráti
	 * hodnotu {@code valnull}. Programátor má možnosť zvoliť vlastný titulok
	 * dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnýSmer predvolený smer v novo otvorenom dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol vyberSmer(String titulok, Smer počiatočnýSmer)
	{ return PanelSmeru.dialóg(titulok, počiatočnýSmer); }


	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(String, Smer) vyberSmer}.</p> */
	public static Uhol dialógVýberSmeru(String titulok, Smer počiatočnýSmer) { return PanelSmeru.dialóg(titulok, počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberSmer(String, Smer) vyberSmer}.</p> */
	public static Uhol dialogVyberSmeru(String titulok, Smer počiatočnýSmer) { return PanelSmeru.dialóg(titulok, počiatočnýSmer); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #vyberSmer() vyberSmer}. Predvoleným smerom v otvorenom
	 * dialógu bude hodnota 90°. Po zvolení želaného smeru používateľom,
	 * vráti metóda zvolený smer v novom objekte typu {@link Uhol Uhol}.
	 * Ak používateľ dialóg zruší, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol zvoľSmer()
	{ return PanelSmeru.dialóg(null, sever); }


	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer() zvoľSmer}.</p> */
	public static Uhol zvolSmer() { return zvoľSmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer() zvoľSmer}.</p> */
	public static Uhol dialógVoľbaSmeru() { return zvoľSmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer() zvoľSmer}.</p> */
	public static Uhol dialogVolbaSmeru() { return zvoľSmer(); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #vyberSmer(Smer) vyberSmer}. Otvorený dialóg bude mať predvolený
	 * zadaný smer (argument {@code počiatočnýSmer}). Po zvolení želaného
	 * smeru používateľom, vráti metóda zvolený smer v novom objekte typu
	 * {@link Uhol Uhol}. Ak používateľ dialóg zruší, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @param počiatočnýSmer predvolený smer v novo otvorenom dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol zvoľSmer(Smer počiatočnýSmer)
	{ return PanelSmeru.dialóg(null, počiatočnýSmer); }


	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(Smer) zvoľSmer}.</p> */
	public static Uhol zvolSmer(Smer počiatočnýSmer)
	{ return zvoľSmer(počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(Smer) zvoľSmer}.</p> */
	public static Uhol dialógVoľbaSmeru(Smer počiatočnýSmer)
	{ return zvoľSmer(počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(Smer) zvoľSmer}.</p> */
	public static Uhol dialogVolbaSmeru(Smer počiatočnýSmer)
	{ return zvoľSmer(počiatočnýSmer); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #vyberSmer(String) vyberSmer}. Predvoleným smerom v otvorenom
	 * dialógu bude hodnota 90°. Po zvolení želaného smeru používateľom,
	 * vráti metóda zvolený smer v novom objekte typu {@link Uhol Uhol}.
	 * Ak používateľ dialóg zruší, tak metóda vráti hodnotu {@code valnull}.
	 * Programátor má možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol zvoľSmer(String titulok)
	{ return PanelSmeru.dialóg(titulok, sever); }


	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String) zvoľSmer}.</p> */
	public static Uhol zvolSmer(String titulok)
	{ return zvoľSmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String) zvoľSmer}.</p> */
	public static Uhol dialógVoľbaSmeru(String titulok)
	{ return zvoľSmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String) zvoľSmer}.</p> */
	public static Uhol dialogVolbaSmeru(String titulok)
	{ return zvoľSmer(titulok); }


	/**
	 * <p>Otvorí dialóg na výber smeru. Funguje rovnako ako metóda {@link 
	 * #vyberSmer(String, Smer) vyberSmer}. Otvorený dialóg bude mať predvolený
	 * zadaný smer (argument {@code počiatočnýSmer}). Po zvolení želaného
	 * smeru používateľom, vráti metóda zvolený smer v novom objekte typu
	 * {@link Uhol Uhol}. Ak používateľ dialóg zruší, tak metóda vráti
	 * hodnotu {@code valnull}. Programátor má možnosť zvoliť vlastný titulok
	 * dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnýSmer predvolený smer v novo otvorenom dialógu
	 * @return zvolený smer alebo {@code valnull}
	 */
	public static Uhol zvoľSmer(String titulok, Smer počiatočnýSmer)
	{ return PanelSmeru.dialóg(titulok, počiatočnýSmer); }


	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String, Smer) zvoľSmer}.</p> */
	public static Uhol zvolSmer(String titulok, Smer počiatočnýSmer) { return PanelSmeru.dialóg(titulok, počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String, Smer) zvoľSmer}.</p> */
	public static Uhol dialógVoľbaSmeru(String titulok, Smer počiatočnýSmer) { return PanelSmeru.dialóg(titulok, počiatočnýSmer); }

	/** <p><a class="alias"></a> Alias pre {@link #zvoľSmer(String, Smer) zvoľSmer}.</p> */
	public static Uhol dialogVolbaSmeru(String titulok, Smer počiatočnýSmer) { return PanelSmeru.dialóg(titulok, počiatočnýSmer); }
}
