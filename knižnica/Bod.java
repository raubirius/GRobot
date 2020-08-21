
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

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import java.text.DecimalFormat;

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
// import javax.swing.event.ChangeListener;


// import knižnica.podpora.Alias; //


// -------------------- //
//  *** Trieda Bod ***  //
// -------------------- //

/**
 * <p>Táto trieda uchováva súradnice x, y, pričom súčasne reprezentuje
 * inštanciu bodu Javy {@link Point2D Point2D} a implementuje rozhranie
 * {@link Poloha Poloha} programovacieho rámca GRobot. Účelom tejto
 * triedy je zlepšenie vnútornej kompatibility v programovacom rámci GRobot.
 * Niektoré metódy s návratovou hodnotou {@link Poloha Poloha}
 * v skutočnosti vracajú objekt typu {@code currBod}.</p>
 */
public class Bod extends Point2D implements Poloha
{
	/*packagePrivate*/ double x, y;

	private final static Pattern xyMatch = Pattern.compile(
		"[Xx][Yy] *\\( *[-+.0-9A-Ea-eNPpXx]+ *, " +
		"*[-+.0-9A-Ea-eNPpXx]+ *\\)");

	private final static Pattern xySplit = Pattern.compile(
		"[Xx][Yy] *\\( *| *, *| *\\)");

	private final static Pattern coordMatch = Pattern.compile(
		"\\[ *[-+.0-9A-Ea-eNPpXx]+ *, *[-+.0-9A-Ea-eNPpXx]+ *\\]");

	private final static Pattern coordSplit = Pattern.compile(
		"\\[ *| *, *| *\\]");


	/**
	 * <p>Predvolený konštruktor – nastaví súradnice bodu na hodnotu
	 * [0, 0].</p>
	 */
	public Bod() { x = y = 0; }

	/**
	 * <p>Konštruktor – prijíma súradnice bodu.</p>
	 * 
	 * @param x počiatočná x-ová súradnica tohto objektu
	 * @param y počiatočná y-ová súradnica tohto objektu
	 */
	public Bod(double x, double y) { setLocation(x, y); }

	/**
	 * <p>Konštruktor – prijíma objekt určujúci bod.</p>
	 * 
	 * @param bod objekt určujúci počiatočné súradnice tohto objektu
	 */
	public Bod(Point2D bod) { setLocation(bod.getX(), bod.getY()); }

	/**
	 * <p>Konštruktor – prijíma implementáciu polohy určujúcu bod.</p>
	 * 
	 * @param poloha implementácia polohy určujúca počiatočné súradnice
	 *     tohto objektu
	 */
	public Bod(Poloha poloha) { setLocation(poloha.polohaX(),
		poloha.polohaY()); }

	/**
	 * <p>Kopírovací konštruktor.</p>
	 * 
	 * @param bod iná inštancia bodu, ktorá bude skopírovaná
	 */
	public Bod(Bod bod) { setLocation(bod.x, bod.y); }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu x-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna x-ová súradnica bodu
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public double getX() { return x; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu y-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna y-ová súradnica bodu
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public double getY() { return y; }


	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred

	/**
	 * <p><a class="setter"></a> Nastaví nové súradnice tohto bodu.</p>
	 * 
	 * @param x nová x-ová súradnica bodu
	 * @param y nová y-ová súradnica bodu
	 */
	// @Override // netreba, lebo ide o abstraktnú metódu
	public void setLocation(double x, double y)
	{ this.x = x; this.y = y; }

	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred


	/**
	 * <p><a class="getter"></a> Vráti aktuálnu x-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna x-ová súradnica bodu
	 */
	public double polohaX() { return x; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu y-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna y-ová súradnica bodu
	 */
	public double polohaY() { return y; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu x-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna x-ová súradnica bodu
	 */
	public double súradnicaX() { return x; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu x-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna x-ová súradnica bodu
	 */
	public double suradnicaX() { return x; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu y-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna y-ová súradnica bodu
	 */
	public double súradnicaY() { return y; }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu y-ovú súradnicu bodu.</p>
	 * 
	 * @return aktuálna y-ová súradnica bodu
	 */
	public double suradnicaY() { return y; }

	/**
	 * <p><a class="getter"></a> V tomto prípade vráti samého seba. Táto
	 * metóda je súčasťou úplnej implementácie rozhrania {@link Poloha
	 * Poloha}. Metóda je používaná inštanciami ostatných tried
	 * programovacieho rámca GRobot.</p>
	 */
	public Bod poloha() { return this; }


	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred

	/**
	 * <p><a class="setter"></a> Nastaví novú x-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param x nová x-ová súradnica bodu
	 */
	public void polohaX(double x) { this.x = x; }

	/**
	 * <p><a class="setter"></a> Nastaví novú y-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param y nová y-ová súradnica bodu
	 */
	public void polohaY(double y) { this.y = y; }

	/**
	 * <p><a class="setter"></a> Nastaví novú x-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param x nová x-ová súradnica bodu
	 */
	public void súradnicaX(double x) { this.x = x; }

	/**
	 * <p><a class="setter"></a> Nastaví novú x-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param x nová x-ová súradnica bodu
	 */
	public void suradnicaX(double x) { this.x = x; }

	/**
	 * <p><a class="setter"></a> Nastaví novú y-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param y nová y-ová súradnica bodu
	 */
	public void súradnicaY(double y) { this.y = y; }

	/**
	 * <p><a class="setter"></a> Nastaví novú y-ovú súradnicu tohto bodu.</p>
	 * 
	 * @param y nová y-ová súradnica bodu
	 */
	public void suradnicaY(double y) { this.y = y; }

	/**
	 * <p><a class="setter"></a> Nastaví nové súradnice tohto bodu.</p>
	 * 
	 * @param x nová x-ová súradnica bodu
	 * @param y nová y-ová súradnica bodu
	 */
	public void poloha(double x, double y)
	{ this.x = x; this.y = y; }

	/**
	 * <p><a class="setter"></a> Nastaví nové súradnice tohto bodu podľa
	 * zadanej implementácie polohy.</p>
	 * 
	 * @param poloha poloha obsahujúca nové súradnice bodu
	 */
	public void poloha(Poloha poloha)
	{ this.x = poloha.polohaX(); this.y = poloha.polohaY(); }

	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred


	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred

	/**
	 * <p>Upraví súradnice tohto bodu podľa zadaných hodnôt zmeny
	 * v horizontálnom (Δx) a vertikálnom (Δy) smere.</p>
	 * 
	 * @param Δx miera posunutia v smere osi x
	 * @param Δy miera posunutia v smere osi y
	 */
	public void posuň(double Δx, double Δy)
	{
		this.x += Δx;
		this.y += Δy;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuň(double, double) posuň}.</p> */
	public void posun(double Δx, double Δy) { posuň(Δx, Δy); }

	/**
	 * <p>Upraví (posunie) súradnice tohto bodu podľa súradníc zadanej
	 * inštancie polohového vektora. Súradnica polohy x zadanej inštancie
	 * určí mieru posunutia v horizontálnom smere a súradnica polohy
	 * y vo vertikálnom smere.</p>
	 * 
	 * @param poloha inštancia určujúca mieru posunutia tohto bodu
	 */
	public void posuň(Poloha poloha)
	{
		this.x += poloha.polohaX();
		this.y += poloha.polohaY();
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuň(Poloha) posuň}.</p> */
	public void posun(Poloha poloha) { posuň(poloha); }

	/**
	 * <p>Posunie súradnice tohto bodu určeným smerom o zadanú vzdialenosť.</p>
	 * 
	 * @param smer smer, v ktorom sa má bod posunúť
	 * @param dĺžka vzdialenosť, o ktorú sa má bod posunúť
	 */
	public void posuňVSmere(double smer, double dĺžka)
	{
		this.x += Math.cos(Math.toRadians(smer)) * dĺžka;
		this.y += Math.sin(Math.toRadians(smer)) * dĺžka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňVSmere(double, double) posuňVSmere}.</p> */
	public void posunVSmere(double smer, double dĺžka)
	{ posuňVSmere(smer, dĺžka); }

	/**
	 * <p>Posunie súradnice tohto bodu určeným smerom o zadanú vzdialenosť.</p>
	 * 
	 * @param smer inštancia určujúca smer, v ktorom sa má bod posunúť
	 * @param dĺžka vzdialenosť, o ktorú sa má bod posunúť
	 */
	public void posuňVSmere(Smer smer, double dĺžka)
	{
		this.x += Math.cos(Math.toRadians(smer.smer())) * dĺžka;
		this.y += Math.sin(Math.toRadians(smer.smer())) * dĺžka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňVSmere(Smer, double) posuňVSmere}.</p> */
	public void posunVSmere(Smer smer, double dĺžka)
	{ posuňVSmere(smer, dĺžka); }

	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred


	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred

	/**
	 * <p>Upraví mierku súradníc tohto bodu podľa zadanej hodnoty.</p>
	 * 
	 * <p class="tip"><b>Tip:</b> Pozrite si aj informácie v opise metódy
	 * {@link #mierka(double, double) mierka(mx, my).}</p>
	 * 
	 * @param mierka miera zmeny mierky oboch súradníc tohto bodu
	 * 
	 * @see #mierka(double, double)
	 * @see #mierka(Poloha)
	 */
	public void mierka(double mierka)
	{
		this.x *= mierka;
		this.y *= mierka;
	}

	/**
	 * <p>Upraví súradnice tohto bodu podľa zadaných hodnôt mierky
	 * v horizontálnom (mx) a vertikálnom (my) smere. Metóda jednoducho
	 * „prenásobí“ každú súradnicu zadanou súradnicou, avšak aj touto
	 * jednoduchou transformáciou sa dajú dosiahnuť zaujímavé výsledky.
	 * Napríklad vypočítať stred úsečky – nastavením súradníc tejto
	 * inštancie na hodnotu prvého bodu úsečky (napr. metódou {@link 
	 * #poloha(Poloha) poloha}), posunutím súradníc tejto inštancie
	 * o hodnotu druhého bodu úsečky (napr. metódou {@link  #posuň(Poloha)
	 * posuň}) a zmenou mierky tejto inštancie o hodnotu {@code num0.5}
	 * (napr. metódou {@link  #mierka(double) mierka}). Tiež môžeme
	 * zrkladliť body okolo osí (zápornými hodnotami mierok) a podobne.</p>
	 * 
	 * @param mx miera zmeny mierky v smere osi x
	 * @param my miera zmeny mierky v smere osi y
	 * 
	 * @see #mierka(double)
	 * @see #mierka(Poloha)
	 */
	public void mierka(double mx, double my)
	{
		this.x *= mx;
		this.y *= my;
	}

	/**
	 * <p>Upraví mierku súradníc tohto bodu podľa hodnôt súradníc zadanej
	 * inštancie. Súradnica polohy x zadanej inštancie určí zmenu mierky
	 * v horizontálnom smere a súradnica polohy y vo vertikálnom smere.</p>
	 * 
	 * <p class="tip"><b>Tip:</b> Pozrite si aj informácie v opise metódy
	 * {@link #mierka(double, double) mierka(mx, my).}</p>
	 * 
	 * @param poloha inštancia určujúca mieru zmeny mierky tohto bodu
	 *     podľa hodnôt súradníc zadaného bodu
	 * 
	 * @see #mierka(double)
	 * @see #mierka(double, double)
	 */
	public void mierka(Poloha poloha)
	{
		this.x *= poloha.polohaX();
		this.y *= poloha.polohaY();
	}

	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred


	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred

	/**
	 * <p>Pootočí súradnice tohto bodu okolo stredu súradnicovej sústavy
	 * s zadaný uhol.</p>
	 * 
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void otoč(double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x, y0 = this.y;
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α));
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α));
	}

	/** <p><a class="alias"></a> Alias pre {@link #otoč(double) otoč}.</p> */
	public void otoc(double uhol) { otoč(uhol); }

	/**
	 * <p>Pootočí súradnice tohto bodu okolo zadaného bodu o zadaný
	 * uhol.</p>
	 * 
	 * @param xs x-ová súradnica stredu rotácie
	 * @param ys y-ová súradnica stredu rotácie
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void otoč(double xs, double ys, double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x - xs, y0 = this.y - ys;
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α)) + xs;
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α)) + ys;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otoč(double, double, double) otoč}.</p> */
	public void otoc(double xs, double ys, double uhol) { otoč(xs, ys, uhol); }

	/**
	 * <p>Pootočí súradnice tohto bodu okolo zadaného bodu o zadaný
	 * uhol.</p>
	 * 
	 * @param stred stred rotácie
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void otoč(Poloha stred, double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x - stred.polohaX(), y0 = this.y - stred.polohaY();
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α)) + stred.polohaX();
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α)) + stred.polohaY();
	}

	/** <p><a class="alias"></a> Alias pre {@link #otoč(Poloha, double) otoč}.</p> */
	public void otoc(Poloha stred, double uhol) { otoč(stred, uhol); }


	/**
	 * <p>Pootočí súradnice tohto bodu okolo stredu súradnicovej sústavy
	 * s zadaný uhol.</p>
	 * 
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void rotuj(double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x, y0 = this.y;
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α));
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α));
	}

	/**
	 * <p>Pootočí súradnice tohto bodu okolo zadaného bodu o zadaný
	 * uhol.</p>
	 * 
	 * @param xs x-ová súradnica stredu rotácie
	 * @param ys y-ová súradnica stredu rotácie
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void rotuj(double xs, double ys, double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x - xs, y0 = this.y - ys;
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α)) + xs;
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α)) + ys;
	}

	/**
	 * <p>Pootočí súradnice tohto bodu okolo zadaného bodu o zadaný
	 * uhol.</p>
	 * 
	 * @param stred stred rotácie
	 * @param uhol uhol pootočenia
	 * 
	 * @see #otoč(double)
	 * @see #otoč(double, double, double)
	 * @see #otoč(Poloha, double)
	 * @see #rotuj(double)
	 * @see #rotuj(double, double, double)
	 * @see #rotuj(Poloha, double)
	 */
	public void rotuj(Poloha stred, double uhol)
	{
		if (0 == uhol) return;
		double α = Math.toRadians(uhol);
		double x0 = this.x - stred.polohaX(), y0 = this.y - stred.polohaY();
		this.x = (x0 * Math.cos(α)) - (y0 * Math.sin(α)) + stred.polohaX();
		this.y = (x0 * Math.sin(α)) + (y0 * Math.cos(α)) + stred.polohaY();
	}

	// Pozor! Každý setter – treba preťažiť v inštancii Poloha.stred


	/**
	 * <p>Overí, či sa poloha tohto bodu dokonale zhoduje so zadanými
	 * súradnicami. Ak je zistená zhoda, tak metóda vráti hodnotu {@code 
	 * valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param x x-ová súradnica, s ktorou má byť porovnaná poloha tohto bodu
	 * @param y y-ová súradnica, s ktorou má byť porovnaná poloha tohto bodu
	 * @return {@code valtrue} ak sa poloha tohto bodu zhoduje so zadanými
	 *     súradnicami, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(double x, double y)
	{
		return this.x == x && this.y == y;
	}

	/**
	 * <p>Overí, či sa poloha tohto bodu a poloha zadaného objektu dokonale
	 * zhodujú. Ak je zistená zhoda, tak metóda vráti hodnotu {@code valtrue},
	 * v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param poloha objekt, ktorého poloha má byť porovnaná s polohou tohto
	 *     bodu
	 * @return {@code valtrue} ak sa poloha tohto bodu zhoduje s polohou
	 *     zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(Poloha poloha)
	{
		if (poloha instanceof Bod)
			return ((Bod)poloha).x == x && ((Bod)poloha).y == y;
		return poloha.polohaX() == x && poloha.polohaY() == y;
	}


	/**
	 * <p>Zistí vzdialenosť tohto bodu od bodu zadaného prostredníctvom
	 * súradníc.</p>
	 * 
	 * @param súradnicaX x-ová súradnica bodu
	 * @param súradnicaY y-ová súradnica bodu
	 * @return vzdialenosť medzi týmto a zadaným bodom
	 */
	public double vzdialenosťOd(double súradnicaX, double súradnicaY)
	{ return Math.hypot(súradnicaX - x, súradnicaY - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(double, double) vzdialenosťOd}.</p> */
	// @ Alias ( " vzdialenosťOd " ) //
	public double vzdialenostOd(double súradnicaX, double súradnicaY)
	{ return vzdialenosťOd(súradnicaX, súradnicaY); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(double, double) vzdialenosťOd}.</p> */
	public double vzdialenosťK(double súradnicaX, double súradnicaY)
	{ return Math.hypot(súradnicaX - x, súradnicaY - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(double, double) vzdialenosťOd}.</p> */
	public double vzdialenostK(double súradnicaX, double súradnicaY)
	{ return vzdialenosťK(súradnicaX, súradnicaY); }


	/**
	 * <p>Zistí vzdialenosť medzi týmto bodom a zadaným objektom.</p>
	 * 
	 * @param objekt objekt implementujúci rozhranie poloha
	 * @return vzdialenosť medzi týmto bodom a zadaným objektom
	 * 
	 * @see #vzdialenosť()
	 * @see #vzdialenosťOd(double, double)
	 * @see #vzdialenosťOd(Poloha)
	 * @see #vzdialenosťOd(Shape)
	 * @see #vzdialenosťOdMyši()
	 * @see Svet#vzdialenosť(Poloha)
	 */
	public double vzdialenosťOd(Poloha objekt)
	{ return Math.hypot(objekt.polohaX() - x, objekt.polohaY() - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Poloha) vzdialenosťOd}.</p> */
	public double vzdialenostOd(Poloha objekt) { return vzdialenosťOd(objekt); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Poloha) vzdialenosťOd}.</p> */
	public double vzdialenosťK(Poloha objekt)
	{ return Math.hypot(objekt.polohaX() - x, objekt.polohaY() - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Poloha) vzdialenosťOd}.</p> */
	public double vzdialenostK(Poloha objekt) { return vzdialenosťK(objekt); }

	/**
	 * <p>Zistí vzdialenosť medzi týmto bodom a stredom
	 * hraníc<sup>[1]</sup> zadaného tvaru.</p>
	 * 
	 * <p><small>[1] – nejde presne o stred útvaru; je použitý
	 * najrýchlejší a najjednoduchší spôsob zistenia približného
	 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
	 * jeho stred – čiže „stred hraníc.“</small></p>
	 * 
	 * @param tvar tvar Javy ({@link Shape Shape})
	 * @return vzdialenosť medzi bodom a stredom hraníc zadaného tvaru
	 * 
	 * @see #vzdialenosť()
	 * @see #vzdialenosťOd(Poloha)
	 * @see #vzdialenosťOdMyši()
	 */
	public double vzdialenosťOd(Shape tvar)
	{
		Rectangle2D hranice = tvar.getBounds2D();

		return Math.hypot(
			(Svet.prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2) - x,
			(Svet.prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2) - y);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Shape) vzdialenosťOd}.</p> */
	public double vzdialenostOd(Shape tvar) { return vzdialenosťOd(tvar); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Shape) vzdialenosťOd}.</p> */
	public double vzdialenosťK(Shape tvar)
	{
		Rectangle2D hranice = tvar.getBounds2D();

		return Math.hypot(
			(Svet.prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2) - x,
			(Svet.prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2) - y);
	}

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOd(Shape) vzdialenosťOd}.</p> */
	public double vzdialenostK(Shape tvar) { return vzdialenosťK(tvar); }

	/**
	 * <p>Zistí vzdialenosť medzi týmto bodom a aktuálnymi súradnicami
	 * myši.</p>
	 * 
	 * @return vzdialenosť medzi týmto bodom a súradnicami myši
	 * 
	 * @see #vzdialenosť()
	 * @see #vzdialenosťOd(Poloha)
	 * @see Svet#vzdialenosť(Poloha)
	 */
	public double vzdialenosťOdMyši()
	{ return Math.hypot(ÚdajeUdalostí.súradnicaMyšiX - x,
		ÚdajeUdalostí.súradnicaMyšiY - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOdMyši() vzdialenosťOdMyši}.</p> */
	public double vzdialenostOdMysi() { return vzdialenosťOdMyši(); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOdMyši() vzdialenosťOdMyši}.</p> */
	public double vzdialenosťKMyši()
	{ return Math.hypot(ÚdajeUdalostí.súradnicaMyšiX - x,
		ÚdajeUdalostí.súradnicaMyšiY - y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťOdMyši() vzdialenosťOdMyši}.</p> */
	public double vzdialenostKMysi() { return vzdialenosťKMyši(); }


	/**
	 * <p>Zistí, aká je vzdialenosť tohto bodu od stredu súradnicovej
	 * sústavy.</p>
	 * 
	 * @return vzdialenosť bodu od stredu súradnicovej sústavy
	 */
	public double vzdialenosť() { return Math.hypot(x, y); }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť() vzdialenosť}.</p> */
	public double vzdialenost() { return vzdialenosť(); }


	/**
	 * <p>Táto statická metóda prepočíta súradnice stredu ohraničujúceho
	 * obdĺžnika zadaného tvaru (skrátene „stredu tvaru“) zo
	 * súradnicového priestoru Javy do súradnicového priestoru
	 * programovacieho rámca GRobot (skrátene „vráti polohu zadaného
	 * objektu“). O súradnicových priestoroch sa podrobnejšie píše
	 * napríklad v opisoch metód {@link GRobot#cesta() GRobot.cesta()},
	 * {@link SVGPodpora#zapíš(String, String, boolean)
	 * SVGpodpora.zapíš(…)}, {@link SVGPodpora#čítaj(String)
	 * SVGpodpora.čítaj(meno)} a priebežne v celej dokumentácii.</p>
	 * 
	 * @param tvar tvar, ktorého polohu chceme zistiť
	 * @return objekt typu {@link Bod Bod} so súradnicami „stredu tvaru“
	 */
	public static Bod polohaTvaru(Shape tvar)
	{
		Rectangle2D hranice = tvar.getBounds2D();
		double Δx = (Svet.prepočítajSpäťX(hranice.getX()) +
			hranice.getWidth() / 2);
		double Δy = (Svet.prepočítajSpäťY(hranice.getY()) -
			hranice.getHeight() / 2);
		return new Bod(Δx, Δy);
	}


	/**
	 * <p>Reťazcová reprezentácia tohto bodu na účely ladenia obsahujúca
	 * informácie o súradniciach. Pozri aj metódy
	 * {@link #polohaNaReťazec(Poloha) polohaNaReťazec},
	 * {@link #reťazecNaPolohu(String) reťazecNaPolohu},
	 * {@link #bodNaReťazec(Point2D) bodNaReťazec}
	 * a {@link #reťazecNaBod(String) reťazecNaBod}.</p>
	 * 
	 * @return reťazcová reprezentácia tohto bodu na účely ladenia
	 *     obsahujúca informácie o súradniciach
	 */
	@Override public String toString()
	{ return this.getClass().getName() + "[x=" + x + ",y=" + y + "]"; }


	/**
	 * <p>Prevedie zadanú implementáciu rozhrania {@link Poloha Poloha}
	 * (čiže nielen bodu – vstupom môže byť napríklad {@link GRobot
	 * robot}, {@link Oblasť oblasť}, {@link Tlačidlo tlačidlo},
	 * {@link Častica častica}…) do textovej podoby, ktorá bude
	 * reprezentovať polohu so súradnicami x a y.
	 * (Vhodné napríklad pri ukladaní údajov do konfiguračného súboru.)</p>
	 * 
	 * @param poloha poloha, ktorá má byť prevedená do reťazcovej podoby
	 * @return poloha prevedená do reťazcovej podoby
	 * 
	 * @see #reťazecNaPolohu(String)
	 * @see #správnyFormát(String)
	 */
	public static String polohaNaReťazec(Poloha poloha)
	{
		return "xy(" + poloha.polohaX() + ", " + poloha.polohaY() + ")";
	}

	/** <p><a class="alias"></a> Alias pre {@link #polohaNaReťazec(Poloha) polohaNaReťazec}.</p> */
	public static String polohaNaRetazec(Poloha poloha)
	{ return polohaNaReťazec(poloha); }

	/**
	 * <p>Prevedie súradnice inštancie triedy {@link Point2D Point2D} (bod
	 * Javy) do textovej podoby.</p>
	 * 
	 * @param bod bod, ktorý má byť prevedený do reťazcovej podoby
	 * @return súradnice bodu prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaBod(String)
	 * @see #správnyFormát(String)
	 */
	public static String bodNaReťazec(Point2D bod)
	{
		return "[" + bod.getX() + ", " + bod.getY() + "]";
	}

	/** <p><a class="alias"></a> Alias pre {@link #bodNaReťazec(Point2D) bodNaReťazec}.</p> */
	public static String bodNaRetazec(Point2D bod)
	{ return bodNaReťazec(bod); }

	/**
	 * <p>Prevedie zadanú implementáciu rozhrania {@link Poloha Poloha}
	 * (čiže nielen bodu – vstupom môže byť napríklad {@link GRobot robot},
	 * {@link Oblasť oblasť}, {@link Tlačidlo tlačidlo}, {@link Častica
	 * častica}…) do textovej podoby, ktorá bude reprezentovať polohu so
	 * súradnicami x a y.
	 * (Vhodné napríklad pri ukladaní údajov do konfiguračného súboru.)</p>
	 * 
	 * @param x x-ová súradnica polohy, ktorá má byť prevedená do
	 *     reťazcovej podoby
	 * @param y y-ová súradnica polohy, ktorá má byť prevedená do
	 *     reťazcovej podoby
	 * @return poloha prevedená do reťazcovej podoby
	 * 
	 * @see #reťazecNaPolohu(String)
	 * @see #správnyFormát(String)
	 */
	public static String polohaNaReťazec(double x, double y)
	{ return "xy(" + x + ", " + y + ")"; }

	/** <p><a class="alias"></a> Alias pre {@link #polohaNaReťazec(double, double) polohaNaReťazec}.</p> */
	public static String polohaNaRetazec(double x, double y)
	{ return polohaNaReťazec(x, y); }

	/**
	 * <p>Prevedie súradnice inštancie triedy {@link Point2D Point2D} (bod
	 * Javy) do textovej podoby.</p>
	 * 
	 * @param x x-ová súradnica bodu, ktorý má byť prevedený do
	 *     reťazcovej podoby
	 * @param y y-ová súradnica bodu, ktorý má byť prevedený do
	 *     reťazcovej podoby
	 * @return súradnice bodu prevedené do reťazcovej podoby
	 * 
	 * @see #reťazecNaBod(String)
	 * @see #správnyFormát(String)
	 */
	public static String bodNaReťazec(double x, double y)
	{ return "[" + x + ", " + y + "]"; }

	/** <p><a class="alias"></a> Alias pre {@link #bodNaReťazec(double, double) bodNaReťazec}.</p> */
	public static String bodNaRetazec(double x, double y)
	{ return bodNaReťazec(x, y); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci polohu alebo súradnice bodu
	 * na implementáciu rozhrania Poloha (triedu {@link Bod Bod}).
	 * Metóda rozpoznáva reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>xy(</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>súradnicaX</em> je x-ová a <em>súradnicaY</em> y-ová súradnica
	 * polohy/bodu.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metóde {@link 
	 * #polohaNaReťazec(Poloha) polohaNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený
	 * @return implementácia rozhrania Poloha (konkrétne trieda
	 *     {@link Bod Bod}) obsahujúca polohu získanú z reťazcovej
	 *     podoby
	 * 
	 * @see #polohaNaReťazec(Poloha)
	 * @see #správnyFormát(String)
	 */
	public static Bod reťazecNaPolohu(String text)
	{
		text = text.trim();
		double x = 0.0, y = 0.0;

		if (xyMatch.matcher(text).matches())
		{
			String súradnice[] = xySplit.split(text);

			// System.out.println("súradnice " + súradnice.length);
			// for (String s : súradnice) System.out.println(s);

			try
			{
				x = java.lang.Double.parseDouble(súradnice[1]);
				y = java.lang.Double.parseDouble(súradnice[2]);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}
		else if (coordMatch.matcher(text).matches())
		{
			String súradnice[] = coordSplit.split(text);

			// System.out.println("súradnice " + súradnice.length);
			// for (String s : súradnice) System.out.println(s);

			try
			{
				x = java.lang.Double.parseDouble(súradnice[1]);
				y = java.lang.Double.parseDouble(súradnice[2]);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		return new Bod(x, y);
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaPolohu(String) reťazecNaPolohu}.</p> */
	public static Poloha retazecNaPolohu(String text)
	{ return reťazecNaPolohu(text); }

	/**
	 * <p>Prevedie zadaný reťazec reprezentujúci polohu alebo súradnice bodu
	 * na inštanciu triedy {@link Point2D.Double Point2D.Double}.
	 * Metóda rozpoznáva reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>xy(</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>súradnicaX</em> je x-ová a <em>súradnicaY</em> y-ová súradnica
	 * polohy/bodu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda vnútorne používa
	 * metódu {@link #reťazecNaPolohu(String) reťazecNaPolohu}, ktorej
	 * výstup prevedie na bod Javy, takže z hľadiska výkonu je výhodnejšie
	 * použitie metódy {@link #reťazecNaPolohu(String) reťazecNaPolohu}.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metóde
	 * {@link #bodNaReťazec(Point2D) bodNaReťazec}.</p>
	 * 
	 * @param text reťazec, ktorý má byť prevedený
	 * @return inštancia triedy {@link Point2D.Double Point2D.Double}
	 *     so súradnicami získanými z reťazcovej podoby
	 * 
	 * @see #bodNaReťazec(Point2D)
	 * @see #správnyFormát(String)
	 */
	public static Point2D.Double reťazecNaBod(String text)
	{
		Poloha bod = reťazecNaPolohu(text);
		return new Point2D.Double(bod.polohaX(), bod.polohaY());
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaBod(String) reťazecNaBod}.</p> */
	public static Point2D.Double retazecNaBod(String text)
	{ return reťazecNaBod(text); }


	/**
	 * <p>Zistí, či je zadaný reťazec v rozpoznateľnom formáte
	 * reprezentujúcom polohu alebo súradnice bodu.
	 * Metóda uzná za správne reťazce v tvare:</p>
	 * 
	 * <ul>
	 * <li><code>xy(</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>)</code></li>
	 * <li>alebo <code>[</code><em>súradnicaX</em><code>,
	 * </code><em>súradnicaY</em><code>]</code>,</li>
	 * </ul>
	 * 
	 * <p>kde <em>súradnicaX</em> je x-ová a <em>súradnicaY</em> y-ová súradnica
	 * polohy/bodu.</p>
	 * 
	 * <p>Metóda je vhodná ako doplnok k metódam {@link 
	 * #polohaNaReťazec(Poloha) polohaNaReťazec} a {@link 
	 * #reťazecNaPolohu(String) reťazecNaPolohu}.</p>
	 * 
	 * @param text reťazec, ktorý má byť posúdený
	 * @return {@code valtrue} ak je zadaný teťazec v rozpoznateľnom
	 *     formáte
	 * 
	 * @see #polohaNaReťazec(double, double)
	 * @see #polohaNaReťazec(Poloha)
	 * @see #bodNaReťazec(double, double)
	 * @see #bodNaReťazec(Point2D)
	 * @see #reťazecNaPolohu(String)
	 * @see #reťazecNaBod(String)
	 * @see #správnyFormát(String)
	 */
	public static boolean správnyFormát(String text)
	{
		text = text.trim();
		double číslo = 0.0;

		if (xyMatch.matcher(text).matches())
		{
			String súradnice[] = xySplit.split(text);

			try
			{
				číslo = java.lang.Double.parseDouble(súradnice[1]);
				číslo = java.lang.Double.parseDouble(súradnice[2]);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		else if (coordMatch.matcher(text).matches())
		{
			String súradnice[] = coordSplit.split(text);

			try
			{
				číslo = java.lang.Double.parseDouble(súradnice[1]);
				číslo = java.lang.Double.parseDouble(súradnice[2]);
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


		// Panel polôh používaný v dialógoch výberu polohy a voľby
		// rôznych parametrov (pozri: Bod.vyberPolohu a Svet.dialóg).
		@SuppressWarnings("serial")
		/*packagePrivate*/ static class PanelPolohy extends JPanel
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
			private BasicStroke čiaraOsí = new BasicStroke(
				(float)1.5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

			// Čiary osí:
			private Line2D.Double osX = null, osY = null;

			// Objekty komponentu ukážky zvolenej polohy na paneli:
			private BufferedImage obrázokUkážky;
			private Graphics2D grafikaUkážky;
			private ImageIcon ikonaUkážky;
			private JLabel komponentUkážky;

			// Ovládacie prvky (vstupné polia, tlačidlo resetu)
			// s ich panelom:
			private JSpinner upravX;
			private JSpinner upravY;
			private JButton tlačidloReset;
			private JPanel panelPrvkov;

			// Atribúty tohto panela polôh:
			private double predvolenáPolohaX = 0;
			private double predvolenáPolohaY = 0;
			private double zvolenáPolohaX = 0;
			private double zvolenáPolohaY = 0;
			private double faktorMierky = 0.25;
			private int šírkaUkážky = 200;
			private int výškaUkážky = 150;


			/**
			 * <p>Konštruktor.</p>
			 * 
			 * @param textReset text tlačidla resetu polohy
			 * @param poloha predvolená poloha na paneli polôh
			 * @param mierka mierka plochy na výber polohy, ktorá je súčasne
			 *     ukážkou zvolenej polohy
			 */
			public PanelPolohy(String textReset, Poloha poloha, double mierka)
			{
				if (null != poloha)
				{
					predvolenáPolohaX = poloha.polohaX();
					predvolenáPolohaY = poloha.polohaY();
					zvolenáPolohaX = predvolenáPolohaX;
					zvolenáPolohaY = predvolenáPolohaY;
				}

				if (mierka > 0.0) faktorMierky = mierka;
				// System.out.println("faktorMierky 1: " + faktorMierky);

				if (0.25 != faktorMierky ||
					800 != Plátno.šírkaPlátna ||
					600 != Plátno.výškaPlátna)
				{
					double šírka = Plátno.šírkaPlátna * faktorMierky;
					double výška = Plátno.výškaPlátna * faktorMierky;
					// System.out.println("šírka 1: " + šírka);
					// System.out.println("výška 1: " + výška);
					if (100.0 >= šírka || 75.0 >= výška)
					{
						faktorMierky = Math.max(
							100.0 / (double)Plátno.šírkaPlátna,
							 75.0 / (double)Plátno.výškaPlátna);
						šírka = Plátno.šírkaPlátna * faktorMierky;
						výška = Plátno.výškaPlátna * faktorMierky;
						// System.out.println("šírka 2: " + šírka);
						// System.out.println("výška 2: " + výška);
						// System.out.println("faktorMierky 2: " + faktorMierky);
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
					upravX = new JSpinner(modelSpinera);

					JSpinner.NumberEditor editor = new JSpinner.
						NumberEditor(upravX);/*, Svet.formát.toPattern());
					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);*/

					upravX.setEditor(editor);

					JFormattedTextField textField = editor.getTextField();
					textField.setFormatterFactory(továreň);
					textField.setHorizontalAlignment(JTextField.CENTER);
					textField.setPreferredSize(new Dimension(50, 20));

					upravX.addChangeListener(e -> aktualizujPodľaSpinerov(e));

					JPanel panel = new JPanel();
					panel.add(new JLabel("X:"));
					panel.add(upravX);
					panel.setBorder(BorderFactory.
						createEmptyBorder(0, 10, 0, 10));

					panelPrvkov.add(panel);
					// panelPrvkov.add(upravX);

					// JFormattedTextField textField = null;
					// JComponent editor = upravX.getEditor();
					// if (editor instanceof JSpinner.DefaultEditor)
					// 	textField = ((JSpinner.DefaultEditor)editor).getTextField();
				}

				panelPrvkov.add(Box.createVerticalStrut(10));
				// panelPrvkov.add(Box.createRigidArea(new Dimension(100, 10)));

				{
					SpinnerNumberModel modelSpinera = new SpinnerNumberModel(
						(java.lang.Double)0.0, null, null,
						(java.lang.Double)1.0);
					upravY = new JSpinner(modelSpinera);

					JSpinner.NumberEditor editor = new JSpinner.
						NumberEditor(upravY);/*, Svet.formát.toPattern());
					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);*/

					upravY.setEditor(editor);

					JFormattedTextField textField = editor.getTextField();
					textField.setFormatterFactory(továreň);
					textField.setHorizontalAlignment(JTextField.CENTER);
					textField.setPreferredSize(new Dimension(50, 20));

					upravY.addChangeListener(e -> aktualizujPodľaSpinerov(e));

					JPanel panel = new JPanel();
					panel.add(new JLabel("Y:"));
					panel.add(upravY);
					panel.setBorder(BorderFactory.
						createEmptyBorder(0, 10, 0, 10));

					panelPrvkov.add(panel);
					// panelPrvkov.add(upravY);

					// JFormattedTextField textField = null;
					// JComponent editor = upravY.getEditor();
					// if (editor instanceof JSpinner.DefaultEditor)
					// 	textField = ((JSpinner.DefaultEditor)editor).getTextField();
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
						zvolenáPolohaX = predvolenáPolohaX;
						zvolenáPolohaY = predvolenáPolohaY;
						aktualizujUkážku();
						aktualizujEditory();
					});

				aktualizujUkážku();
				aktualizujEditory();
			}

			// Aktualizácia ukážky zvolenej polohy.
			private void aktualizujUkážku()
			{
				grafikaUkážky.setColor(Farebnosť.biela);
				grafikaUkážky.fillRect(0, 0, šírkaUkážky, výškaUkážky);

				if (null == osX) osX = new Line2D.Double(
					0, výškaUkážky / 2, šírkaUkážky, výškaUkážky / 2);
				if (null == osY) osY = new Line2D.Double(
					šírkaUkážky / 2, 0, šírkaUkážky / 2, výškaUkážky);

				int polomer = 2;
				Shape tvar = new Ellipse2D.Double(
					( zvolenáPolohaX * faktorMierky +
						(šírkaUkážky / 2)) - polomer,
					(-zvolenáPolohaY * faktorMierky +
						(výškaUkážky / 2)) - polomer,
					2 * polomer, 2 * polomer);

				grafikaUkážky.setColor(Farebnosť.svetlošedá);
				grafikaUkážky.setStroke(čiaraOsí);
				grafikaUkážky.draw(osX);
				grafikaUkážky.draw(osY);

				grafikaUkážky.setColor(Farebnosť.čierna);
				grafikaUkážky.draw(tvar);

				komponentUkážky.repaint();
			}

			// Aktualizuje súradnice zvolenej polohy podľa udalosti myši.
			private void aktualizujPodľaMyši(MouseEvent e)
			{
				// System.out.println("  Súradnice myši: " +
				// 	e.getX() + ", " + e.getY());
				zvolenáPolohaX = ( (e.getX() - 10) -
					(šírkaUkážky / 2)) / faktorMierky;
				zvolenáPolohaY = (-(e.getY() - 10) +
					(výškaUkážky / 2)) / faktorMierky;
				// System.out.println("  Zvolená poloha: " +
				// 	zvolenáPolohaX + ", " + zvolenáPolohaY);
				aktualizujUkážku();
				aktualizujEditory();
			}

			// Aktualizuje súradnice zvolenej polohy podľa zmeny v spineroch.
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
							zvolenáPolohaX = ((Number)v).doubleValue();
							aktualizujUkážku();
						}
						else if (spiner == upravY)
						{
							zvolenáPolohaY = ((Number)v).doubleValue();
							aktualizujUkážku();
						}
					}
				}
			}

			// Aktualizuje hodnoty zvolenej polohy v editoroch.
			private void aktualizujEditory()
			{
				upravX.setValue(zvolenáPolohaX);
				// {
				// 	JSpinner.NumberEditor editor =
				// 		(JSpinner.NumberEditor)upravX.getEditor();
				// 	editor.commitEdit();
				// }

				upravY.setValue(zvolenáPolohaY);
				// {
				// 	JSpinner.NumberEditor editor =
				// 		(JSpinner.NumberEditor)upravX.getEditor();
				// 	editor.commitEdit();
				// }
			}

			// Aktualizuje tento panel do takého stavu, v akom by sa
			// nachádzal po konštrukcii so zadanými hodnotami parametrov.
			// (Môžu nastať drobné odchýlky, ktoré sú neodsledovateľné, ale
			// zhruba by sa panel mal vizuálne aj vnútorne nachádzať
			// v požadovanom stave.)
			private void aktualizujPanel(String textReset,
				Poloha poloha, double mierka)
			{
				// aktualizujFormát();
				if (mierka <= 0.0 && faktorMierky != 0.25) mierka = 0.25;
				aktualizujVeľkosťUkážky(mierka);

				upravTextTlačidla(textReset);
				nastavPolohu(poloha);
			}

			/* *
			 * <p>Aktualizuje formáty čísiel podľa formátu sveta.</p>
			 * /
			public void aktualizujFormát()
			{
				{
					JSpinner.NumberEditor editor =
						(JSpinner.NumberEditor)upravX.getEditor();

					System.out.println("TEST");
					System.out.println("editor.getFormat(): " + editor.getFormat());

					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);
				}

				{
					JSpinner.NumberEditor editor =
						(JSpinner.NumberEditor)upravY.getEditor();

					DecimalFormat formát = editor.getFormat();
					formát.setDecimalFormatSymbols(
						Svet.formát.getDecimalFormatSymbols());
					formát.setMaximumFractionDigits(20);
				}

				aktualizujEditory();
			}*/

			/**
			 * <p>Aktualizuje veľkosť ukážky zvolenej polohy.</p>
			 * 
			 * @param mierka mierka plochy na výber polohy, ktorá je súčasne
			 *     ukážkou zvolenej polohy
			 */
			public void aktualizujVeľkosťUkážky(double mierka)
			{
				// System.out.println("faktorMierky 1: " + faktorMierky +
				// 	" (" + mierka + ")");
				if (mierka > 0.0) faktorMierky = mierka;

				if ((int)(Plátno.šírkaPlátna * faktorMierky) != šírkaUkážky ||
					(int)(Plátno.výškaPlátna * faktorMierky) != výškaUkážky)
				{
					double šírka = Plátno.šírkaPlátna * faktorMierky;
					double výška = Plátno.výškaPlátna * faktorMierky;
					// System.out.println("šírka 1: " + šírka);
					// System.out.println("výška 1: " + výška);
					if (100.0 >= šírka || 75.0 >= výška)
					{
						faktorMierky = Math.max(
							100.0 / (double)Plátno.šírkaPlátna,
							 75.0 / (double)Plátno.výškaPlátna);
						šírka = Plátno.šírkaPlátna * faktorMierky;
						výška = Plátno.výškaPlátna * faktorMierky;
						// System.out.println("šírka 2: " + šírka);
						// System.out.println("výška 2: " + výška);
						// System.out.println("faktorMierky 2: " +
						// 	faktorMierky);
					}
					šírkaUkážky = (int)šírka;
					výškaUkážky = (int)výška;
					osX = osY = null;

					obrázokUkážky = new BufferedImage(
						šírkaUkážky, výškaUkážky, BufferedImage.TYPE_INT_ARGB);
					grafikaUkážky = obrázokUkážky.createGraphics();
					grafikaUkážky.addRenderingHints(Obrázok.hints);
					ikonaUkážky = new ImageIcon(obrázokUkážky);
					komponentUkážky.setIcon(ikonaUkážky);
				}
			}

			/**
			 * <p>Nastavenie novej predvolenej polohy na paneli.</p>
			 * 
			 * @param nováPoloha nová predvolená poloha na paneli
			 */
			public void nastavPolohu(Poloha nováPoloha)
			{
				if (null == nováPoloha)
				{
					zvolenáPolohaX = predvolenáPolohaX = 0;
					zvolenáPolohaY = predvolenáPolohaY = 0;
				}
				else
				{
					predvolenáPolohaX = nováPoloha.polohaX();
					predvolenáPolohaY = nováPoloha.polohaY();
					zvolenáPolohaX = predvolenáPolohaX;
					zvolenáPolohaY = predvolenáPolohaY;
				}

				aktualizujUkážku();
				aktualizujEditory();
			}

			/**
			 * <p>Získanie zvolenej polohy na paneli.</p>
			 * 
			 * @param nováPoloha nová predvolená poloha na paneli
			 */
			public Bod dajPolohu()
			{
				return new Bod(zvolenáPolohaX, zvolenáPolohaY);
			}

			/**
			 * <p>Upraví predvolený text tlačidla resetu polohy na paneli.</p>
			 * 
			 * @param textReset text tlačidla resetu polohy na paneli
			 */
			public void upravTextTlačidla(String textReset)
			{
				if (null != textReset)
					tlačidloReset.setText(textReset);
			}


			// Statický panel dialógu voľby polohy.
			private static PanelPolohy panelPolohy = null;

			/**
			 * <p>Otvorí dialóg s panelom na výber polohy. Metóda prijíma
			 * parameter určujúci titulok dialógového okna a predvolenú polohu
			 * na paneli polôh. Ak je niektorá z hodnôt rovná {@code valnull},
			 * tak bude zvolená vhodná hodnota.</p>
			 * 
			 * @param titulok titulok okna dialógu
			 * @param predvolenáPoloha predvolená poloha na paneli
			 * @return zvolená poloha alebo {@code valnull} (ak používateľ
			 *     dialóg zavrel)
			 */
			public static Bod dialóg(String titulok, Poloha predvolenáPoloha)
			{
				if (null == panelPolohy)
					panelPolohy = new PanelPolohy(
						Svet.tlačidláDialógu[2], predvolenáPoloha, 0);
				else
					panelPolohy.aktualizujPanel(
						Svet.tlačidláDialógu[2], predvolenáPoloha, 0);

				Object[] komponenty = new Object[] {panelPolohy};

				if (JOptionPane.showOptionDialog(null == Svet.
					oknoCelejObrazovky ? GRobot.svet : Svet.
					oknoCelejObrazovky, komponenty, null == titulok ?
					"Voľba polohy" : titulok, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, Svet.odpovedeZadania,
					null) == JOptionPane.YES_OPTION)
					return panelPolohy.dajPolohu();

				return null;
			}
		}


	/**
	 * <p>Otvorí dialóg na výber polohy. Predvolenou polohou v otvorenom
	 * dialógu bude {@linkplain Poloha#stred stred súradnicovej sústavy}.
	 * Po zvolení želanej polohy používateľom, vráti metóda zvolenú polohu
	 * v novom objekte typu {@link Bod Bod}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return zvolená poloha alebo {@code valnull}
	 */
	public static Bod vyberPolohu()
	{ return PanelPolohy.dialóg(null, stred); }

	/**
	 * <p>Otvorí dialóg na výber polohy. Otvorený dialóg bude mať
	 * predvolenú zadanú polohu (argument {@code počiatočnáPoloha}). Po
	 * zvolení želanej polohy používateľom, vráti metóda zvolenú polohu
	 * v novom objekte typu {@link Bod Bod}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @param počiatočnáPoloha predvolená poloha v novo otvorenom dialógu
	 * @return zvolená poloha alebo {@code valnull}
	 */
	public static Bod vyberPolohu(Poloha počiatočnáPoloha)
	{ return PanelPolohy.dialóg(null, počiatočnáPoloha); }

	/**
	 * <p>Otvorí dialóg na výber polohy. Predvolenou polohou v otvorenom
	 * dialógu bude {@linkplain Poloha#stred stred súradnicovej sústavy}.
	 * Po zvolení želanej polohy používateľom, vráti metóda zvolenú polohu
	 * v novom objekte typu {@link Bod Bod}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Text tlačidla na reset je
	 * upraviteľný volaním metódy {@link Svet#textTlačidla(String, String)
	 * textTlačidla} triedy {@link Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @return zvolená poloha alebo {@code valnull}
	 */
	public static Bod vyberPolohu(String titulok)
	{ return PanelPolohy.dialóg(titulok, stred); }

	/**
	 * <p>Otvorí dialóg na výber polohy. Otvorený dialóg bude
	 * mať predvolenú zadanú polohu (argument {@code počiatočnáPoloha}). Po
	 * zvolení želanej polohy používateľom, vráti metóda zvolenú polohu
	 * v novom objekte typu {@link Bod Bod}. Ak používateľ dialóg
	 * zruší, tak metóda vráti hodnotu {@code valnull}. Programátor má
	 * možnosť zvoliť vlastný titulok dialógového okna.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Text tlačidla na reset je
	 * upraviteľný volaním metódy {@link Svet#textTlačidla(String, String)
	 * textTlačidla} triedy {@link Svet Svet}.</p>
	 * 
	 * @param titulok vlastný titulok dialógu
	 * @param počiatočnáPoloha predvolená poloha v novo otvorenom dialógu
	 * @return zvolená poloha alebo {@code valnull}
	 */
	public static Bod vyberPolohu(String titulok, Poloha počiatočnáPoloha)
	{ return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod dialógVýberPolohy() { return vyberPolohu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod dialogVyberPolohy() { return vyberPolohu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod dialógVýberPolohy(Poloha počiatočnáPoloha)
	{ return vyberPolohu(počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod dialogVyberPolohy(Poloha počiatočnáPoloha)
	{ return vyberPolohu(počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod dialógVýberPolohy(String titulok)
	{ return vyberPolohu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod dialogVyberPolohy(String titulok)
	{ return vyberPolohu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod dialógVýberPolohy(String titulok, Poloha počiatočnáPoloha) { return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod dialogVyberPolohy(String titulok, Poloha počiatočnáPoloha) { return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod zvoľPolohu()
	{ return PanelPolohy.dialóg(null, stred); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod zvolPolohu() { return zvoľPolohu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod dialógVoľbaPolohy() { return zvoľPolohu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu() vyberPolohu}.</p> */
	public static Bod dialogVolbaPolohy() { return zvoľPolohu(); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod zvoľPolohu(Poloha počiatočnáPoloha)
	{ return PanelPolohy.dialóg(null, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod zvolPolohu(Poloha počiatočnáPoloha)
	{ return zvoľPolohu(počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod dialógVoľbaPolohy(Poloha počiatočnáPoloha)
	{ return zvoľPolohu(počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(Poloha) vyberPolohu}.</p> */
	public static Bod dialogVolbaPolohy(Poloha počiatočnáPoloha)
	{ return zvoľPolohu(počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod zvoľPolohu(String titulok)
	{ return PanelPolohy.dialóg(titulok, stred); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod zvolPolohu(String titulok)
	{ return zvoľPolohu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod dialógVoľbaPolohy(String titulok)
	{ return zvoľPolohu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String) vyberPolohu}.</p> */
	public static Bod dialogVolbaPolohy(String titulok)
	{ return zvoľPolohu(titulok); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod zvoľPolohu(String titulok, Poloha počiatočnáPoloha)
	{ return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod zvolPolohu(String titulok, Poloha počiatočnáPoloha) { return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod dialógVoľbaPolohy(String titulok, Poloha počiatočnáPoloha) { return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }

	/** <p><a class="alias"></a> Alias pre {@link #vyberPolohu(String, Poloha) vyberPolohu}.</p> */
	public static Bod dialogVolbaPolohy(String titulok, Poloha počiatočnáPoloha) { return PanelPolohy.dialóg(titulok, počiatočnáPoloha); }


	/**
	 * <p>Vytvorí nový bod so súradnicami tvorenými súčtom súradníc zadaných
	 * bodov.</p>
	 * 
	 * @param bod1 prvý bod súčtu
	 * @param bod2 druhý bod súčtu
	 * @return výsledný bod súčtu
	 */
	public static Bod súčet(Bod bod1, Bod bod2)
	{ return new Bod(bod1.x + bod2.x, bod1.y + bod2.y); }

	/** <p><a class="alias"></a> Alias pre {@link #súčet(Bod, Bod) súčet}.</p> */
	public static Bod sucet(Bod bod1, Bod bod2) { return súčet(bod1, bod2); }

	/**
	 * <p>Vytvorí nový bod so súradnicami tvorenými rozdielom súradníc
	 * zadaných bodov.</p>
	 * 
	 * @param bod1 prvý bod rozdielu
	 * @param bod2 druhý bod rozdielu
	 * @return výsledný bod rozdielu
	 */
	public static Bod rozdiel(Bod bod1, Bod bod2)
	{ return new Bod(bod1.x - bod2.x, bod1.y - bod2.y); }
}
