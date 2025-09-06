
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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import java.util.regex.Pattern;

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


// import knižnica.podpora.Alias; //


// ------------------------ //
//  *** Trieda Rozmery ***  //
// ------------------------ //

/**
 * <p>Táto trieda uchováva rozmery (šírku a výšku) objektu, pričom súčasne
 * reprezentuje inštanciu rozmeru Javy {@link Dimension2D Dimension2D}
 * a implementuje rozhranie {@link Rozmer Rozmer} programovacieho rámca
 * GRobot. Účelom tejto triedy je zlepšenie vnútornej kompatibility
 * v programovacom rámci GRobot. Niektoré metódy s návratovou hodnotou
 * {@link Rozmer Rozmer} v skutočnosti vracajú objekt typu
 * {@code currRozmery}.</p>
 */
public class Rozmery extends Dimension2D implements Rozmer
{
	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }


	/*packagePrivate*/ double šírka, výška;

	private final static Pattern dimMatch = Pattern.compile(
		"[Dd][Ii][Mm] *\\( *[-+.0-9A-Ea-eNPpXx]+ *, " +
		"*[-+.0-9A-Ea-eNPpXx]+ *\\)");

	private final static Pattern dimSplit = Pattern.compile(
		"[Dd][Ii][Mm] *\\( *| *, *| *\\)");

	private final static Pattern javaDimMatch = Pattern.compile(
		"\\[ *[-+.0-9A-Ea-eNPpXx]+ *, *[-+.0-9A-Ea-eNPpXx]+ *\\]");

	private final static Pattern javaDimSplit = Pattern.compile(
		"\\[ *| *, *| *\\]");


	/**
	 * <p>Predvolený konštruktor – nastaví rozmery tejto inštancie na hodnotu
	 * [0, 0].</p>
	 */
	public Rozmery() { šírka = výška = 0; }

	/**
	 * <p>Konštruktor – prijíma rozmery šírky a výšky jednotlivo.</p>
	 * 
	 * @param šírka šírka tejto inštancie
	 * @param výška výška tejto inštancie
	 */
	public Rozmery(double šírka, double výška) { setSize(šírka, výška); }

	/**
	 * <p>Konštruktor – prijíma objekt určujúci rozmery.</p>
	 * 
	 * @param rozmery objekt, z ktorého sa prevezmú rozmery do tejto inštancie
	 */
	public Rozmery(Dimension2D rozmery)
	{ setSize(rozmery.getWidth(), rozmery.getHeight()); }

	/**
	 * <p>Konštruktor – prijíma implementáciu rozmeru iného objektu, ktorá
	 * určí rozmery uložené v tejto inštancii.</p>
	 * 
	 * @param rozmery implementácia rozmeru iného objektu určujúca
	 *     hodnoty (šírku a výšku) tejto inštancie rozmeru
	 */
	public Rozmery(Rozmer rozmer)
	{ setSize(rozmer.šírka(), rozmer.výška()); }

	/**
	 * <p>Kopírovací konštruktor.</p>
	 * 
	 * @param rozmery iná inštancia rozmeru, ktorá bude skopírovaná
	 */
	public Rozmery(Rozmery rozmery) { setSize(rozmery.šírka, rozmery.výška); }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu šírku tejto inštancie.</p>
	 * 
	 * @return aktuálna šírka tejto inštancie
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public double getWidth() { return šírka; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu výšku tejto inštancie.</p>
	 * 
	 * @return aktuálna výška tejto inštancie
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public double getHeight() { return výška; }


	/**
	 * <p><a class="setter"></a> Nastaví nové rozmery tejto inštancie.</p>
	 * 
	 * @param šírka nová šírka tejto inštancie
	 * @param výška nová výška tejto inštancie
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public void setSize(double šírka, double výška)
	{ this.šírka = šírka; this.výška = výška; }



	/**
	 * <p><a class="getter"></a> Vráti aktuálnu šírku tejto inštancie.</p>
	 * 
	 * @return aktuálna šírka tejto inštancie
	 */
	public double šírka() { return šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
	public double sirka() { return šírka(); }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu výšku tejto inštancie.</p>
	 * 
	 * @return aktuálna výška tejto inštancie
	 */
	public double výška() { return výška; }

	/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
	public double vyska() { return výška(); }


	/**
	 * <p><a class="getter"></a> V tomto prípade vráti samého seba. Táto
	 * metóda je súčasťou úplnej implementácie rozhrania {@link Rozmer
	 * Rozmer}. Metóda je používaná inštanciami ostatných tried
	 * programovacieho rámca GRobot.</p>
	 */
	public Rozmery rozmery() { return this; }


	/**
	 * <p><a class="setter"></a> Nastaví novú šírku tejto inštancie.</p>
	 * 
	 * @param šírka nová šírka tejto inštancie
	 */
	public void šírka(double šírka) { this.šírka = šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #šírka(double) šírka}.</p> */
	public void sirka(double šírka) { šírka(šírka); }

	/**
	 * <p><a class="setter"></a> Nastaví novú výšku tejto inštancie.</p>
	 * 
	 * @param výška nová výška tejto inštancie
	 */
	public void výška(double výška) { this.výška = výška; }

	/** <p><a class="alias"></a> Alias pre {@link #výška(double) výška}.</p> */
	public void vyska(double výška) { výška(výška); }


	/**
	 * <p><a class="setter"></a> Nastaví nové rozmery tejto inštancie.</p>
	 * 
	 * @param šírka nová šírka tejto inštancie
	 * @param výška nová výška tejto inštancie
	 */
	public void rozmery(double šírka, double výška)
	{ this.šírka = šírka; this.výška = výška; }

	/**
	 * <p><a class="setter"></a> Nastaví nové rozmery tejto inštancie podľa
	 * zadanej implementácie rozmeru.</p>
	 * 
	 * @param rozmer iná inštancia, podľa ktorej sú nastavené nové rozmery
	 *     tejto inštancie
	 */
	public void rozmery(Rozmer rozmer)
	{ this.šírka = rozmer.šírka(); this.výška = rozmer.výška(); }


	/**
	 * <p>Porovná, či sa zadaná šírka zhoduje s hodnotou šírky uloženou
	 * v tejto inštancii.</p>
	 * 
	 * @param šírka šírka, ktorá má byť porovnaná so šírkou uloženou
	 *     v tejto inštancii
	 * @return {@code valtrue} ak sa šírka tejto inštancie zhoduje so
	 *     zadanou šírkou, {@code valfalse} v opačnom prípade
	 */
	public boolean máŠírku(double šírka) { return this.šírka == šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #máŠírku(double) máŠírku}.</p> */
	public boolean maSirku(double šírka) { return máŠírku(šírka); }

	/**
	 * <p>Porovná, či sa zadaná výška zhoduje s hodnotou výšky uloženou
	 * v tejto inštancii.</p>
	 * 
	 * @param výška výška, ktorá má byť porovnaná s výškou uloženou
	 *     v tejto inštancii
	 * @return {@code valtrue} ak sa výška tejto inštancie zhoduje so
	 *     zadanou výškou, {@code valfalse} v opačnom prípade
	 */
	public boolean máVýšku(double výška) { return this.výška == výška; }

	/** <p><a class="alias"></a> Alias pre {@link #máVýšku(double) máVýšku}.</p> */
	public boolean maVysku(double výška) { return máVýšku(výška); }


	/**
	 * <p>Overí, či sa rozmery tejto inštancie a rozmery zadaného objektu
	 * dokonale zhodujú. Ak je zistená zhoda, tak metóda vráti hodnotu
	 * {@code valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param rozmer iný objekt, ktorého rozmery majú byť porovnané
	 *     s rozmermi tejto inštancie
	 * @return {@code valtrue} ak sa rozmery tejto inštancie zhodujú s rozmermi
	 *     zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean máRozmer(Rozmer rozmer)
	{
		if (rozmer instanceof Rozmery)
			return ((Rozmery)rozmer).šírka == šírka &&
				((Rozmery)rozmer).výška == výška;
		return rozmer.šírka() == šírka && rozmer.výška() == výška;
	}

	/** <p><a class="alias"></a> Alias pre {@link #máRozmer(Rozmer) máRozmer}.</p> */
	public boolean maRozmer(Rozmer rozmer) { return máRozmer(rozmer); }


	/**
	 * <p>Overí, či sa rozmery tejto inštancie dokonale zhodujú so zadanými
	 * rozmermi. Ak je zistená zhoda, tak metóda vráti hodnotu
	 * {@code valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param šírka šírka porovnávaná s korešpondujúcou hodnotou tejto
	 *     inštancie
	 * @param výška výška porovnávaná s korešpondujúcou hodnotou tejto
	 *     inštancie
	 * @return {@code valtrue} ak sa rozmery tejto inštancie zhodujú so
	 *     zadanými rozmermi, {@code valfalse} v opačnom prípade
	 */
	public boolean máRozmer(double šírka, double výška)
	{ return this.šírka == šírka && this.výška == výška; }

	/** <p><a class="alias"></a> Alias pre {@link #máRozmer(double, double) máRozmer}.</p> */
	public boolean maRozmer(double šírka, double výška)
	{ return máRozmer(šírka, výška); }


	/**
	 * <p>Táto statická metóda prevedie rozmery zadaného tvaru
	 * z implementácie rozmerov Javy do implementácie rozmerov
	 * programovacieho rámca GRobot. (Vykoná to pomocou tzv. ohraničujúceho
	 * obdĺžnika – bounding box – metódou {@link Shape#getBounds2D()
	 * getBounds2D()} tvarov Javy. Takže technicky ide o prevod rozmerov
	 * získaných z inštancie {@link Rectangle2D Rectangle2D} na objekt
	 * typu {@link Rozmery Rozmery}.)</p>
	 * 
	 * @param tvar tvar, ktorého rozmery chceme zistiť
	 * @return objekt typu {@link Rozmery Rozmery}
	 */
	public static Rozmery rozmeryTvaru(Shape tvar)
	{
		Rectangle2D hranice = tvar.getBounds2D();
		return new Rozmery(hranice.getWidth(), hranice.getHeight());
	}


	/**
	 * <p>Reťazcová reprezentácia tejto inštancie vhodná na účely ladenia
	 * a obsahujúca informáciu o jednotlivých rozmeroch tejto inštancie.</p>
	 * 
	 * <p>Pozri aj metódy: {@link #rozmeryNaReťazec(Rozmer) rozmeryNaReťazec},
	 * {@link #reťazecNaRozmery(String) reťazecNaRozmery},
	 * {@link #dimension2DNaReťazec(Dimension2D) dimension2DNaReťazec}
	 * a {@link #reťazecNaDimension2D(String) reťazecNaDimension2D}.</p>
	 * 
	 * @return reťazcová reprezentácia tejto inštancie vhodná na účely ladenia
	 *     a obsahujúca informácie o rozmeroch
	 */
	@Override public String toString()
	{
		return this.getClass().getName() + "[šírka=" + šírka +
			",výška=" + výška + "]";
	}


	/**
	 * <p>Prevedie zadanú implementáciu rozhrania {@link Rozmer Rozmer}
	 * (čiže nielen inštancií typu {@link Rozmery Rozmery} – vstupom
	 * môže byť napríklad {@link GRobot robot}, {@link Oblasť oblasť},
	 * {@link Tlačidlo tlačidlo}, {@link Častica častica}…) do textovej
	 * podoby, ktorá bude rozmery (šírku a výšku) reprezentovať.
	 * (Vhodné napríklad pri ukladaní údajov do konfiguračného súboru.)</p>
	 * 
	 * @param rozmery implementácia rozmerov, ktoré majú byť prevedené do
	 *     reťazcovej podoby
	 * @return rozmery prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaRozmery(String)
	 * @see #správnyFormát(String)
	 */
	public static String rozmeryNaReťazec(Rozmer rozmer)
	{ return "dim(" + rozmer.šírka() + ", " + rozmer.výška() + ")"; }

	/** <p><a class="alias"></a> Alias pre {@link #rozmeryNaReťazec(Rozmer) rozmeryNaReťazec}.</p> */
	public static String rozmeryNaRetazec(Rozmer rozmer)
	{ return rozmeryNaReťazec(rozmer); }

	/**
	 * <p>Prevedie hodnoty rozmerov inštancie triedy {@link Dimension2D
	 * Dimension2D} („rozmery Javy“) do textovej podoby.</p>
	 * 
	 * @param rozmery inštancia {@link Dimension2D Dimension2D}, ktorej má
	 *     údaje majú byť prevedené do reťazcovej podoby
	 * @return údaje o šírke a výške prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaDimension2D(String)
	 * @see #správnyFormát(String)
	 */
	public static String dimension2DNaReťazec(Dimension2D rozmery)
	{ return "[" + rozmery.getWidth() + ", " + rozmery.getHeight() + "]"; }

	/** <p><a class="alias"></a> Alias pre {@link #dimension2DNaReťazec(Dimension2D) dimension2DNaReťazec}.</p> */
	public static String dimension2DNaRetazec(Dimension2D rozmery)
	{ return dimension2DNaReťazec(rozmery); }

	/**
	 * <p>Prevedie zadané rozmery (šírku a výšku) do textovej podoby.
	 * Funguje rovnako ako metóda {@link #dimension2DNaReťazec(double,
	 * double) dimension2DNaReťazec(šírka, výška)}, rozdiel je len vo
	 * výslednej textovej reprezentácii:</p>
	 * 
	 * <ul>
	 * <li>{@link #rozmeryNaReťazec(double, double) rozmeryNaReťazec(šírka,
	 * výška)}<code>dim(</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>)</code></li>
	 * <li>{@link #dimension2DNaReťazec(double, double)
	 * dimension2DNaReťazec(šírka, výška)}: <code>[</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>Výsledný text v každom prípade reprezentuje rozmery (šírku a výšku)
	 * nejakého objektu a v obidvoch prípadoch je rozpoznateľný metódami
	 * {@link #reťazecNaRozmery(String) reťazecNaRozmery}
	 * a {@link #reťazecNaDimension2D(String) reťazecNaDimension2D}.
	 * Využitie týchto metód je vhodné napríklad pri ukladaní údajov do
	 * textového konfiguračného súboru.</p>
	 * 
	 * @param šírka šírka určitého objektu, ktorá má byť prevedená do
	 *     reťazcovej podoby spolu s párujúcou výškou
	 * @param výška výška určitého objektu, ktorá má byť prevedená do
	 *     reťazcovej podoby spolu s párujúcou šírkou
	 * @return rozmery prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaRozmery(String)
	 * @see #správnyFormát(String)
	 */
	public static String rozmeryNaReťazec(double šírka, double výška)
	{ return "dim(" + šírka + ", " + výška + ")"; }

	/** <p><a class="alias"></a> Alias pre {@link #rozmeryNaReťazec(double, double) rozmeryNaReťazec}.</p> */
	public static String rozmeryNaRetazec(double šírka, double výška)
	{ return rozmeryNaReťazec(šírka, výška); }

	/**
	 * <p>Prevedie zadané rozmery (šírku a výšku) do textovej podoby.
	 * Funguje rovnako ako metóda {@link #rozmeryNaReťazec(double,
	 * double) rozmeryNaReťazec(šírka, výška)}, rozdiel je len vo
	 * výslednej textovej reprezentácii:</p>
	 * 
	 * <ul>
	 * <li>{@link #rozmeryNaReťazec(double, double) rozmeryNaReťazec(šírka,
	 * výška)}<code>dim(</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>)</code></li>
	 * <li>{@link #dimension2DNaReťazec(double, double)
	 * dimension2DNaReťazec(šírka, výška)}: <code>[</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>Výsledný text v každom prípade reprezentuje rozmery (šírku a výšku)
	 * nejakého objektu a v obidvoch prípadoch je rozpoznateľný metódami
	 * {@link #reťazecNaRozmery(String) reťazecNaRozmery}
	 * a {@link #reťazecNaDimension2D(String) reťazecNaDimension2D}.
	 * Využitie týchto metód je vhodné napríklad pri ukladaní údajov do
	 * textového konfiguračného súboru.</p>
	 * 
	 * @param šírka šírka určitého objektu, ktorý má byť prevedený do
	 *     reťazcovej podoby
	 * @param výška výška určitého objektu, ktorý má byť prevedený do
	 *     reťazcovej podoby
	 * @return rozmery objektu prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaDimension2D(String)
	 * @see #správnyFormát(String)
	 */
	public static String dimension2DNaReťazec(double šírka, double výška)
	{ return "[" + šírka + ", " + výška + "]"; }

	/** <p><a class="alias"></a> Alias pre {@link #dimension2DNaReťazec(double, double) dimension2DNaReťazec}.</p> */
	public static String dimension2DNaRetazec(double šírka, double výška)
	{ return dimension2DNaReťazec(šírka, výška); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci rozmery nejakého objektu
	 * na implementáciu rozhrania Rozmer (triedu {@link Rozmery
	 * Rozmery}). Metóda rozpoznáva reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>dim(</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>šírka</em> a <em>výška</em> sú číselne vyjadrené
	 * rozmery určitého objektu.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metóde {@link 
	 * #rozmeryNaReťazec(Rozmer) rozmeryNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený
	 * @return implementácia rozhrania Rozmer (konkrétne trieda
	 *     {@link Rozmery Rozmery}) obsahujúca rozmery získané
	 *     z reťazcovej podoby
	 * 
	 * @see #rozmeryNaReťazec(Rozmer)
	 * @see #správnyFormát(String)
	 */
	public static Rozmery reťazecNaRozmery(String text)
	{
		text = text.trim();
		double šírka = 0.0, výška = 0.0;

		if (dimMatch.matcher(text).matches())
		{
			String rozmery[] = dimSplit.split(text);

			try
			{
				šírka = java.lang.Double.parseDouble(rozmery[1]);
				výška = java.lang.Double.parseDouble(rozmery[2]);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}
		else if (javaDimMatch.matcher(text).matches())
		{
			String rozmery[] = javaDimSplit.split(text);

			try
			{
				šírka = java.lang.Double.parseDouble(rozmery[1]);
				výška = java.lang.Double.parseDouble(rozmery[2]);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		return new Rozmery(šírka, výška);
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaRozmery(String) reťazecNaRozmery}.</p> */
	public static Rozmer retazecNaRozmer(String text)
	{ return reťazecNaRozmery(text); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci rozmery určitého objektu
	 * na inštanciu triedy {@link Dimension Dimension}.
	 * Metóda rozpoznáva reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>dim(</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>šírka</em> a <em>výška</em> sú číselne vyjadrené
	 * rozmery určitého objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda vnútorne používa
	 * metódu {@link #reťazecNaRozmery(String) reťazecNaRozmery}, ktorej
	 * výstup prevedie na „rozmery Javy“ – inštanciu triedy {@link 
	 * Dimension Dimension}, ktorá ukladá celočíselné hodnoty, takže
	 * z hľadiska presnosti aj výkonu je výhodnejšie použitie metódy
	 * {@link #reťazecNaRozmery(String) reťazecNaRozmery}.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metóde {@link 
	 * #dimension2DNaReťazec(Dimension2D) dimension2DNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený
	 * @return inštancia triedy {@link Dimension Dimension}
	 *     s celočíselnými rozmermi získanými z reťazcovej podoby
	 * 
	 * @see #dimension2DNaReťazec(Dimension2D)
	 * @see #správnyFormát(String)
	 */
	public static Dimension reťazecNaDimension2D(String text)
	{
		Rozmer rozmer = reťazecNaRozmery(text);
		return new Dimension((int)rozmer.šírka(), (int)rozmer.výška());
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaDimension2D(String) reťazecNaDimension2D}.</p> */
	public static Dimension retazecNaDimension2D(String text)
	{ return reťazecNaDimension2D(text); }


	/**
	 * <p>Zistí, či je zadaný reťazec v rozpoznateľnom formáte
	 * reprezentujúcom rozmery určitého objektu.
	 * Metóda uzná za správne reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>dim(</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>šírka</em><code>,
	 * </code><em>výška</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>šírka</em> a <em>výška</em> sú číselne vyjadrené
	 * rozmery určitého objektu.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metódam {@link 
	 * #rozmeryNaReťazec(Rozmer) rozmeryNaReťazec} a {@link 
	 * #reťazecNaRozmery(String) reťazecNaRozmery}.</p>
	 * 
	 * @param text reťazec, ktorý má byť posúdený
	 * @return {@code valtrue} ak je zadaný teťazec v rozpoznateľnom
	 *     formáte
	 * 
	 * @see #rozmeryNaReťazec(double, double)
	 * @see #rozmeryNaReťazec(Rozmer)
	 * @see #rozmeryNaReťazec(double, double)
	 * @see #dimension2DNaReťazec(Dimension2D)
	 * @see #reťazecNaRozmery(String)
	 * @see #reťazecNaDimension2D(String)
	 * @see #správnyFormát(String)
	 */
	public static boolean správnyFormát(String text)
	{
		text = text.trim();
		double číslo = 0.0;

		if (dimMatch.matcher(text).matches())
		{
			String rozmery[] = dimSplit.split(text);

			try
			{
				číslo = java.lang.Double.parseDouble(rozmery[1]);
				číslo = java.lang.Double.parseDouble(rozmery[2]);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		else if (javaDimMatch.matcher(text).matches())
		{
			String rozmery[] = javaDimSplit.split(text);

			try
			{
				číslo = java.lang.Double.parseDouble(rozmery[1]);
				číslo = java.lang.Double.parseDouble(rozmery[2]);
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
	 * <p>Inštancia, ktorej atribúty budú použité v {@linkplain #vyberRozmer()
	 * dialógu výberu rozmerov,} ak nie sú zadané žiadne počiatočné
	 * rozmery.</p>
	 */
	public final static Rozmer predvolený = new Rozmery(10, 10);

	/** <p><a class="alias"></a> Alias pre {@link #predvolený predvolený}.</p> */
	public final static Rozmer predvoleny = predvolený;


		// Panel polôh používaný v dialógoch výberu rozmeru a voľby
		// rôznych parametrov (pozri: Rozmery.vyberRozmer a Svet.dialóg).
		@SuppressWarnings("serial")
		/*packagePrivate*/ static class PanelRozmeru extends JPanel
		{
			// Formátovač súradníc pre textové pole spinera.
			private final static JFormattedTextField.AbstractFormatter
				formátovač = new JFormattedTextField.AbstractFormatter()
				{
					public Object stringToValue(String text)
					{
						java.lang.Double prevod =
							Svet.reťazecNaReálneČíslo(text);
						if (null == prevod) Svet.pípni();
						return prevod;
					}

					public String valueToString(Object value)
					{
						if (value instanceof Number) return Svet.formát.
							format(((Number)value).doubleValue());
						Svet.pípni(); return "0";
					}
				};

			// Továreň formátovača súradníc textového poľa spinera.
			private final static JFormattedTextField.AbstractFormatterFactory
				továreň = new JFormattedTextField.AbstractFormatterFactory()
				{
					public JFormattedTextField.AbstractFormatter
						getFormatter(JFormattedTextField tf)
					{
						return formátovač;
					}
				};

			// Štýl čiary kreslenia osí:
			private BasicStroke čiaraObrysov = new BasicStroke(1.5f,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f,
				new float[]{3, 2}, 0f);

			// Objekty komponentu ukážky zvoleného rozmeru na paneli:
			private BufferedImage obrázokUkážky;
			private Graphics2D grafikaUkážky;
			private ImageIcon ikonaUkážky;
			private JLabel komponentUkážky;

			// Ovládacie prvky (vstupné polia, tlačidlo resetu)
			// s ich panelom:
			private JSpinner upravX;
			private JSpinner upravY;
			private JButton tlačidloReset;
			private JLabel menovkaŠírky;
			private JLabel menovkaVýšky;
			private JPanel panelPrvkov;

			// Atribúty tohto panela polôh:
			private double predvolenáŠírka = 0;
			private double predvolenáVýška = 0;
			private double zvolenáŠírka = 10;
			private double zvolenáVýška = 10;
			private double faktorMierky = 0.25;
			private int šírkaUkážky = 200;
			private int výškaUkážky = 150;


			/**
			 * <p>Konštruktor.</p>
			 * 
			 * @param textReset text tlačidla resetu rozmeru
			 * @param textŠírky text menovky šírky
			 * @param textVýšky text menovky výšky
			 * @param rozmery predvolené rozmery na paneli polôh
			 * @param mierka mierka plochy na výber rozmeru, ktorá je súčasne
			 *     ukážkou zvoleného rozmeru
			 */
			public PanelRozmeru(String textReset, String textŠírky,
				String textVýšky, Rozmer rozmer, double mierka)
			{
				if (null != rozmer)
				{
					predvolenáŠírka = rozmer.šírka();
					predvolenáVýška = rozmer.výška();
					zvolenáŠírka = predvolenáŠírka;
					zvolenáVýška = predvolenáVýška;
				}

				if (mierka > 0.0) faktorMierky = mierka;

				if (0.25 != faktorMierky ||
					800 != Plátno.šírkaPlátna ||
					600 != Plátno.výškaPlátna)
				{
					double šírka = Plátno.šírkaPlátna * faktorMierky;
					double výška = Plátno.výškaPlátna * faktorMierky;

					if (100.0 >= šírka || 75.0 >= výška)
					{
						faktorMierky = Math.max(
							100.0 / (double)Plátno.šírkaPlátna,
							 75.0 / (double)Plátno.výškaPlátna);
						šírka = Plátno.šírkaPlátna * faktorMierky;
						výška = Plátno.výškaPlátna * faktorMierky;
					}
					šírkaUkážky = (int)šírka;
					výškaUkážky = (int)výška;
				}

				obrázokUkážky = new BufferedImage(
					šírkaUkážky, výškaUkážky, BufferedImage.TYPE_INT_ARGB);
				grafikaUkážky = obrázokUkážky.createGraphics();
				grafikaUkážky.addRenderingHints(Obrázok.hints);
				ikonaUkážky = new ImageIcon(obrázokUkážky);
				komponentUkážky = new JLabel(ikonaUkážky);
				komponentUkážky.setBorder(BorderFactory.
					createEmptyBorder(10, 10, 10, 10));

				tlačidloReset = new JButton(
					null == textReset ? "Reset" : textReset);
				tlačidloReset.setAlignmentX(Component.CENTER_ALIGNMENT);

				menovkaŠírky = new JLabel(
					null == textŠírky ? "↔:" : textŠírky);
				menovkaVýšky = new JLabel(
					null == textVýšky ? "↕:" : textVýšky);

				setLayout(new BorderLayout());

				panelPrvkov = new JPanel();
				panelPrvkov.setLayout(new BoxLayout(
					panelPrvkov, BoxLayout.Y_AXIS));

				add(komponentUkážky, BorderLayout.WEST);
				panelPrvkov.add(Box.createVerticalStrut(10));

				{
					SpinnerNumberModel modelSpinera = new SpinnerNumberModel(
						(java.lang.Double)0.0, (java.lang.Double)0.0, null,
						(java.lang.Double)1.0);
					upravX = new JSpinner(modelSpinera);

					JSpinner.NumberEditor editor =
						new JSpinner.NumberEditor(upravX);
					editor.setPreferredSize(new Dimension(50, 20));

					upravX.setEditor(editor);

					JFormattedTextField textField = editor.getTextField();
					textField.setFormatterFactory(továreň);
					textField.setHorizontalAlignment(JTextField.CENTER);

					upravX.addChangeListener(e -> aktualizujPodľaSpinerov(e));

					JPanel panel = new JPanel();
					panel.add(menovkaŠírky);
					panel.add(upravX);
					panel.setBorder(BorderFactory.
						createEmptyBorder(0, 10, 0, 10));

					panelPrvkov.add(panel);
				}

				panelPrvkov.add(Box.createVerticalStrut(10));

				{
					SpinnerNumberModel modelSpinera = new SpinnerNumberModel(
						(java.lang.Double)0.0, (java.lang.Double)0.0, null,
						(java.lang.Double)1.0);
					upravY = new JSpinner(modelSpinera);

					JSpinner.NumberEditor editor =
						new JSpinner. NumberEditor(upravY);
					editor.setPreferredSize(new Dimension(50, 20));

					upravY.setEditor(editor);

					JFormattedTextField textField = editor.getTextField();
					textField.setFormatterFactory(továreň);
					textField.setHorizontalAlignment(JTextField.CENTER);

					upravY.addChangeListener(e -> aktualizujPodľaSpinerov(e));

					JPanel panel = new JPanel();
					panel.add(menovkaVýšky);
					panel.add(upravY);
					panel.setBorder(BorderFactory.
						createEmptyBorder(0, 10, 0, 10));

					panelPrvkov.add(panel);
				}

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
						zvolenáŠírka = predvolenáŠírka;
						zvolenáVýška = predvolenáVýška;
						aktualizujUkážku();
						aktualizujEditory();
					});

				aktualizujUkážku();
				aktualizujEditory();
			}

			// Aktualizácia ukážky zvoleného rozmeru.
			private void aktualizujUkážku()
			{
				grafikaUkážky.setColor(Farebnosť.biela);
				grafikaUkážky.fillRect(0, 0, šírkaUkážky, výškaUkážky);

				double šírka = Math.abs(zvolenáŠírka / 2);
				double výška = Math.abs(zvolenáVýška / 2);

				Shape obdĺžnik = new Rectangle2D.Double(
					-šírka * faktorMierky + (šírkaUkážky / 2),
					-výška * faktorMierky + (výškaUkážky / 2),
					šírka / 2, výška / 2);

				grafikaUkážky.setColor(Farebnosť.svetlošedá);
				grafikaUkážky.setStroke(čiaraObrysov);
				grafikaUkážky.draw(obdĺžnik);

				komponentUkážky.repaint();
			}

			// Aktualizuje rozmery zvoleného objektu podľa udalosti myši.
			private void aktualizujPodľaMyši(MouseEvent e)
			{
				zvolenáŠírka = 2 * Math.abs( (e.getX() - 10) -
					(šírkaUkážky / 2)) / faktorMierky;
				zvolenáVýška = 2 * Math.abs(-(e.getY() - 10) +
					(výškaUkážky / 2)) / faktorMierky;

				aktualizujUkážku();
				aktualizujEditory();
			}

			// Aktualizuje rozmery zvoleného objektu podľa zmeny
			// v spineroch.
			private void aktualizujPodľaSpinerov(ChangeEvent e)
			{
				if (e.getSource() instanceof JSpinner)
				{
					JSpinner spiner = (JSpinner)e.getSource();
					Object v = spiner.getValue();
					if (v instanceof Number)
					{
						if (spiner == upravX)
						{
							zvolenáŠírka = ((Number)v).doubleValue();
							aktualizujUkážku();
						}
						else if (spiner == upravY)
						{
							zvolenáVýška = ((Number)v).doubleValue();
							aktualizujUkážku();
						}
					}
				}
			}

			// Aktualizuje hodnoty zvoleného rozmeru v editoroch.
			private void aktualizujEditory()
			{
				upravX.setValue(zvolenáŠírka);
				upravY.setValue(zvolenáVýška);
			}

			// Aktualizuje tento panel do takého stavu, v akom by sa
			// nachádzal po konštrukcii so zadanými hodnotami parametrov.
			// (Môžu nastať drobné odchýlky, ktoré sú neodsledovateľné, ale
			// zhruba by sa panel mal vizuálne aj vnútorne nachádzať
			// v požadovanom stave.)
			private void aktualizujPanel(String textReset, String textŠírky,
				String textVýšky, Rozmer rozmer, double mierka)
			{
				if (mierka <= 0.0 && faktorMierky != 0.25) mierka = 0.25;
				aktualizujVeľkosťUkážky(mierka);

				upravTextTlačidla(textReset);
				upravTextyMenoviek(textŠírky, textVýšky);
				nastavRozmer(rozmer);
			}

			/**
			 * <p>Aktualizuje veľkosť ukážky zvoleného rozmeru.</p>
			 * 
			 * @param mierka mierka plochy na výber rozmeru, ktorá je súčasne
			 *     ukážkou zvoleného rozmeru
			 */
			public void aktualizujVeľkosťUkážky(double mierka)
			{
				if (mierka > 0.0) faktorMierky = mierka;

				if ((int)(Plátno.šírkaPlátna * faktorMierky) != šírkaUkážky ||
					(int)(Plátno.výškaPlátna * faktorMierky) != výškaUkážky)
				{
					double šírka = Plátno.šírkaPlátna * faktorMierky;
					double výška = Plátno.výškaPlátna * faktorMierky;

					if (100.0 >= šírka || 75.0 >= výška)
					{
						faktorMierky = Math.max(
							100.0 / (double)Plátno.šírkaPlátna,
							 75.0 / (double)Plátno.výškaPlátna);
						šírka = Plátno.šírkaPlátna * faktorMierky;
						výška = Plátno.výškaPlátna * faktorMierky;
					}
					šírkaUkážky = (int)šírka;
					výškaUkážky = (int)výška;

					obrázokUkážky = new BufferedImage(
						šírkaUkážky, výškaUkážky, BufferedImage.TYPE_INT_ARGB);
					grafikaUkážky = obrázokUkážky.createGraphics();
					grafikaUkážky.addRenderingHints(Obrázok.hints);
					ikonaUkážky = new ImageIcon(obrázokUkážky);
					komponentUkážky.setIcon(ikonaUkážky);
				}
			}

			/**
			 * <p>Nastavenie nového predvoleného rozmeru na paneli.</p>
			 * 
			 * @param novýRozmer nové predvolené rozmery na paneli
			 */
			public void nastavRozmer(Rozmer novýRozmer)
			{
				if (null == novýRozmer)
				{
					zvolenáŠírka = predvolenáŠírka = 10;
					zvolenáVýška = predvolenáVýška = 10;
				}
				else
				{
					predvolenáŠírka = novýRozmer.šírka();
					predvolenáVýška = novýRozmer.výška();
					zvolenáŠírka = predvolenáŠírka;
					zvolenáVýška = predvolenáVýška;
				}

				aktualizujUkážku();
				aktualizujEditory();
			}

			/**
			 * <p>Získanie zvoleného rozmeru na paneli.</p>
			 * 
			 * @return aktuálne zvolené rozmery na paneli
			 */
			public Rozmery dajRozmer()
			{
				return new Rozmery(zvolenáŠírka, zvolenáVýška);
			}

			/**
			 * <p>Upraví predvolený text tlačidla resetu rozmeru na paneli.</p>
			 * 
			 * @param textReset text tlačidla resetu rozmeru na paneli
			 */
			public void upravTextTlačidla(String textReset)
			{
				if (null != textReset)
					tlačidloReset.setText(textReset);
			}

			/**
			 * <p>Upraví predvolené texty menoviek rozmerov (šírky a výšky) na
			 * paneli.</p>
			 * 
			 * @param textŠírky text menovky šírky na paneli rozmeru
			 * @param textVýšky text menovky výšky na paneli rozmeru
			 */
			public void upravTextyMenoviek(String textŠírky, String textVýšky)
			{
				if (null != textŠírky)
					menovkaŠírky.setText(textŠírky);
				if (null != textVýšky)
					menovkaVýšky.setText(textVýšky);
			}


			// Statický panel dialógu voľby rozmeru.
			private static PanelRozmeru panelRozmeru = null;

			/**
			 * <p>Otvorí dialóg s panelom na výber rozmeru. Metóda prijíma
			 * parameter určujúci titulok dialógového okna a predvolený rozmer
			 * na paneli polôh. Ak je niektorá z hodnôt rovná {@code valnull},
			 * tak bude zvolená vhodná hodnota.</p>
			 * 
			 * @param titulok titulok okna dialógu
			 * @param predvolenýRozmer predvolené rozmery na paneli
			 * @return zvolené rozmery alebo {@code valnull} (ak používateľ
			 *     dialóg zavrel)
			 */
			public static Rozmery dialóg(String titulok,
				Rozmer predvolenýRozmer)
			{
				if (null == panelRozmeru)
					panelRozmeru = new PanelRozmeru(Svet.tlačidláDialógu[4],
						Svet.menovkyDialógu[2], Svet.menovkyDialógu[3],
						predvolenýRozmer, 0);
				else
					panelRozmeru.aktualizujPanel(Svet.tlačidláDialógu[4],
						Svet.menovkyDialógu[2], Svet.menovkyDialógu[3],
						predvolenýRozmer, 0);

				Object[] komponenty = new Object[] {panelRozmeru};

				if ((Svet.odpoveďDialógu = JOptionPane.showOptionDialog(null ==
					Svet.oknoCelejObrazovky ? GRobot.svet : Svet.
					oknoCelejObrazovky, komponenty, null == titulok ?
					"Voľba rozmeru" : titulok, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, null != Svet.mojeOdpovede ?
					Svet.mojeOdpovede : Svet.odpovedeZadania, null)) ==
					JOptionPane.YES_OPTION)
					return panelRozmeru.dajRozmer();

				return null;
			}
		}


	/**
	 * <p>Otvorí dialóg na výber rozmeru. Predvolenými rozmermi v otvorenom
	 * dialógu budú rozmery prevzaté z inštancie {@link #predvolený predvolený}.
	 * Po zvolení želaného rozmeru používateľom, vráti metóda zvolený rozmer
	 * v novom objekte typu {@link Rozmery Rozmery}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return zvolené rozmery alebo {@code valnull}
	 */
	public static Rozmery vyberRozmer()
	{ return PanelRozmeru.dialóg(null, predvolený); }

	/**
	 * <p>Otvorí dialóg na výber rozmeru. Otvorený dialóg bude mať
	 * predvolený zadaný rozmer (argument {@code počiatočnýRozmer}). Po
	 * zvolení želaného rozmeru používateľom, vráti metóda zvolený rozmer
	 * v novom objekte typu {@link Rozmery Rozmery}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @param počiatočnýRozmer predvolené rozmery v novo otvorenom dialógu
	 * @return zvolené rozmery alebo {@code valnull}
	 */
	public static Rozmery vyberRozmer(Rozmer počiatočnýRozmer)
	{ return PanelRozmeru.dialóg(null, počiatočnýRozmer); }

	/**
	 * <p>Otvorí dialóg na výber rozmeru. Predvolenými rozmermi v otvorenom
	 * dialógu budú rozmery prevzaté z inštancie {@link #predvolený predvolený}.
	 * Po zvolení želaného rozmeru používateľom, vráti metóda zvolený rozmer
	 * v novom objekte typu {@link Rozmery Rozmery}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolené rozmery alebo {@code valnull}
	 */
	public static Rozmery vyberRozmer(String titulok)
	{ return PanelRozmeru.dialóg(titulok, predvolený); }

	/**
	 * <p>Otvorí dialóg na výber rozmeru. Otvorený dialóg bude
	 * mať predvolený zadaný rozmer (argument {@code počiatočnýRozmer}). Po
	 * zvolení želaného rozmeru používateľom, vráti metóda zvolený rozmer
	 * v novom objekte typu {@link Rozmery Rozmery}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Texty tlačidiel dialógu
	 * (vrátane tlačidla reset) sú upraviteľné volaním metódy {@link 
	 * Svet#textTlačidla(String, String) textTlačidla} triedy {@link 
	 * Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnýRozmer predvolené rozmery v novo otvorenom dialógu
	 * @return zvolené rozmery alebo {@code valnull}
	 */
	public static Rozmery vyberRozmer(String titulok, Rozmer počiatočnýRozmer)
	{ return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery dialógVýberRozmeru() { return vyberRozmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery dialogVyberRozmeru() { return vyberRozmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialógVýberRozmeru(Rozmer počiatočnýRozmer)
	{ return vyberRozmer(počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialogVyberRozmeru(Rozmer počiatočnýRozmer)
	{ return vyberRozmer(počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery dialógVýberRozmeru(String titulok)
	{ return vyberRozmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery dialogVyberRozmeru(String titulok)
	{ return vyberRozmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialógVýberRozmeru(String titulok, Rozmer počiatočnýRozmer) { return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialogVyberRozmeru(String titulok, Rozmer počiatočnýRozmer) { return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery zvoľRozmer()
	{ return PanelRozmeru.dialóg(null, predvolený); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery zvolRozmer() { return zvoľRozmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery dialógVoľbaRozmeru() { return zvoľRozmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer() vyberRozmer}.</p> */
	public static Rozmery dialogVolbaRozmeru() { return zvoľRozmer(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery zvoľRozmer(Rozmer počiatočnýRozmer)
	{ return PanelRozmeru.dialóg(null, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery zvolRozmer(Rozmer počiatočnýRozmer)
	{ return zvoľRozmer(počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialógVoľbaRozmeru(Rozmer počiatočnýRozmer)
	{ return zvoľRozmer(počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialogVolbaRozmeru(Rozmer počiatočnýRozmer)
	{ return zvoľRozmer(počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery zvoľRozmer(String titulok)
	{ return PanelRozmeru.dialóg(titulok, predvolený); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery zvolRozmer(String titulok)
	{ return zvoľRozmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery dialógVoľbaRozmeru(String titulok)
	{ return zvoľRozmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String) vyberRozmer}.</p> */
	public static Rozmery dialogVolbaRozmeru(String titulok)
	{ return zvoľRozmer(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery zvoľRozmer(String titulok, Rozmer počiatočnýRozmer)
	{ return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery zvolRozmer(String titulok, Rozmer počiatočnýRozmer) { return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialógVoľbaRozmeru(String titulok, Rozmer počiatočnýRozmer) { return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberRozmer(String, Rozmer) vyberRozmer}.</p> */
	public static Rozmery dialogVolbaRozmeru(String titulok, Rozmer počiatočnýRozmer) { return PanelRozmeru.dialóg(titulok, počiatočnýRozmer); }


	/**
	 * <p>Vytvorí nový rozmery vytvorený zo súčtu rozmerov zadaných
	 * inštancií.</p>
	 * 
	 * @param rozmery1 prvý rozmery súčtu
	 * @param rozmery2 druhý rozmery súčtu
	 * @return výsledný rozmery súčtu
	 */
	public static Rozmery súčet(Rozmery rozmery1, Rozmery rozmery2)
	{ return new Rozmery(rozmery1.šírka + rozmery2.šírka, rozmery1.výška + rozmery2.výška); }

	/** <p><a class="alias"></a> Alias pre {@link #súčet(Rozmery, Rozmery) súčet}.</p> */
	public static Rozmery sucet(Rozmery rozmery1, Rozmery rozmery2) { return súčet(rozmery1, rozmery2); }

	/**
	 * <p>Vytvorí nový rozmery s rozmermi tvorenými rozdielom súradníc
	 * zadaných rozmeryov.</p>
	 * 
	 * @param rozmery1 prvý rozmery rozdielu
	 * @param rozmery2 druhý rozmery rozdielu
	 * @return výsledný rozmery rozdielu
	 */
	public static Rozmery rozdiel(Rozmery rozmery1, Rozmery rozmery2)
	{ return new Rozmery(rozmery1.šírka - rozmery2.šírka, rozmery1.výška - rozmery2.výška); }


	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }
}
