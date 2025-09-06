
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

// ---------------------------------- //
//  *** Trieda KontextováPoložka ***  //
// ---------------------------------- //

/**
 * <p>Trieda {@link KontextováPonuka KontextováPonuka} spolu s triedou
 * {@code currKontextováPoložka} umožňujú používanie kontextových ponúk.
 * Voľba položky kontextovej ponuky spôsobí vznik udalosti {@link 
 * ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}.
 * V uvedenej obsluhe udalosti môžeme zistiť, ktorá položka bola zvolená
 * metódou {@link ÚdajeUdalostí#kontextováPoložka()
 * ÚdajeUdalostí.kontextováPoložka()}. Komplexný príklad nájdete v opise
 * triedy {@link KontextováPonuka KontextováPonuka}.</p>
 */
@SuppressWarnings("serial")
public class KontextováPoložka extends JMenuItem
{
	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }


	// Počúvadlo udalostí zvolenia kontextovej položky
	private final static ActionListener
		voľbaKontextovejPoložky = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof KontextováPoložka)
				ÚdajeUdalostí.poslednáKontextováPoložka =
					(KontextováPoložka)e.getSource();
			else
				ÚdajeUdalostí.poslednáKontextováPoložka = null;

			if (null != ObsluhaUdalostí.počúvadlo)
			{
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.voľbaKontextovejPoložky();
					ObsluhaUdalostí.počúvadlo.volbaKontextovejPolozky();
				}
			}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.voľbaKontextovejPoložky();
					počúvajúci.volbaKontextovejPolozky();
				}
			}

			Skript skript;

			if (null != ÚdajeUdalostí.poslednáKontextováPoložka && (null !=
				(skript = ÚdajeUdalostí.poslednáKontextováPoložka.skript())))
			{
				int kódSkriptu = skript.vykonaj();
				if (0 != kódSkriptu)
					Svet.formulujChybuSkriptu(kódSkriptu,
						"Položka kontextovej ponuky…");
			}
		}
	};

	// Skript položky…
	private Skript skript = null;
	private String[] riadkySkriptu = null;

	/**
	 * <p>Vytvorí novú kontextovú položku so zadaným textom. Použite metódu
	 * {@link KontextováPonuka#pridajPoložku(JMenuItem) pridajPoložku}
	 * na jej pridanie do konkrétnej ponuky…</p>
	 * 
	 * @param text text položky ponuky
	 */
	public KontextováPoložka(String text)
	{
		super(text);
		addActionListener(voľbaKontextovejPoložky);
	}

	/**
	 * <p>Overí, či bola táto kontextová položka naposledy aktivovaná
	 * (zvolená). Metóda je použiteľná v reakcii {@link 
	 * ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívna() aktívna} – {@link #aktivovaná()
	 * aktivovaná} a {@link #označená() označená} – {@link #zvolená()
	 * zvolená}!</b>
	 * Metóda {@link #aktivovaná() aktivovaná} a jej alias {@link 
	 * #zvolená() zvolená} zisťujú, či bola stanovená položka naposledy
	 * aktivovaná (zvolená). Metóda {@link #aktívna() aktívna} overuje,
	 * či je stanovená položka použiteľná a metóda {@link #označená()
	 * označená} zisťuje, či bola položka takzvane {@linkplain #označ()
	 * označená} (čo môže mať subjektívny význam).</p>
	 * 
	 * @return {@code valtrue} ak bola pri poslednom vyvolaní kontextovej
	 *     ponuky aktivovaná práve táto položka, v opačnom prípade {@code 
	 *     valfalse}
	 */
	public boolean aktivovaná()
	{ return this == ÚdajeUdalostí.poslednáKontextováPoložka; }

	/** <p><a class="alias"></a> Alias pre {@link #aktivovaná() aktivovaná}.</p> */
	public boolean aktivovana() { return aktivovaná(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktivovaná() aktivovaná}.</p> */
	public boolean zvolená() { return this == ÚdajeUdalostí.poslednáKontextováPoložka; }

	/** <p><a class="alias"></a> Alias pre {@link #aktivovaná() aktivovaná}.</p> */
	public boolean zvolena() { return aktivovaná(); }


	/**
	 * <p>Overí, či je položka aktívna. Aktívna položka znamená, že je
	 * použiteľná používateľom. S deaktivovanými položkami používateľ
	 * nemôže manipulovať.</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívna() aktívna} – {@link #aktivovaná()
	 * aktivovaná} a {@link #označená() označená} – {@link #zvolená()
	 * zvolená}!</b>
	 * Metóda {@link #aktivovaná() aktivovaná} a jej alias {@link 
	 * #zvolená() zvolená} zisťujú, či bola stanovená položka naposledy
	 * aktivovaná (zvolená). Metóda {@link #aktívna() aktívna} overuje,
	 * či je stanovená položka použiteľná a metóda {@link #označená()
	 * označená} zisťuje, či bola položka takzvane {@linkplain #označ()
	 * označená} (čo môže mať subjektívny význam).</p>
	 * 
	 * @return {@code valtrue} – je aktívna;
	 *     {@code valfalse} – nie je aktívna
	 * 
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public boolean aktívna() { return isEnabled(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktívna() aktívna}.</p> */
	public boolean aktivna() { return isEnabled(); }

	/**
	 * <p>Aktivuje položku. Predvolene je položka aktívna. Ak ju {@linkplain 
	 * #deaktivuj() deaktivujeme} (pozri nižšie), tak po vykonaní tohto
	 * príkazu položky ({@code curraktivuj}), bude opäť použiteľná a bude
	 * reagovať na klikanie myšou aj voľbu klávesnicou.</p>
	 * 
	 * @see #aktívna()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void aktivuj() { setEnabled(true); }

	/**
	 * <p>Deaktivuje položku. Položka prestane byť použiteľná, prestane
	 * reagovať na myš a klávesnicu.</p>
	 * 
	 * @see #aktívna()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void deaktivuj() { setEnabled(false); }


	// Overí, či je položka označená. Označená položka zmení svoj vzhľad
	// (spôsob zmeny je závislý od operačného systému) a ak má položka
	// {@linkplain #ikona(Image) definovanú ikonu} môžeme určiť, aby sa
	// v položke s označeným stavom zobrazovala {@linkplain 
	// #ikonaOznačenej(Image) iná ikona}. (Interpretáciu tohto stavu
	// ponechávame na programátora…)

	/**
	 * <p>Overí, či je položka označená. Označená položka zmení svoj vzhľad,
	 * spôsob zmeny je závislý od operačného systému a interpretácia tohto
	 * stavu závisí od programátora…</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívna() aktívna} – {@link #aktivovaná()
	 * aktivovaná} a {@link #označená() označená} – {@link #zvolená()
	 * zvolená}!</b>
	 * Metóda {@link #aktivovaná() aktivovaná} a jej alias {@link 
	 * #zvolená() zvolená} zisťujú, či bola stanovená položka naposledy
	 * aktivovaná (zvolená). Metóda {@link #aktívna() aktívna} overuje,
	 * či je stanovená položka použiteľná a metóda {@link #označená()
	 * označená} zisťuje, či bola položka takzvane {@linkplain #označ()
	 * označená} (čo môže mať subjektívny význam).</p>
	 * 
	 * @return {@code valtrue} – je označená;
	 *     {@code valfalse} – nie je označená
	 * 
	 * @see #označ()
	 * @see #odznač()
	 * @see #zrušOznačenie()
	 */
	public boolean označená() { return isSelected(); }

	/** <p><a class="alias"></a> Alias pre {@link #označená() označená}.</p> */
	public boolean oznacena() { return isSelected(); }

	/**
	 * <p>Označí položku. (Pre viac informácií pozri opis metódy {@link 
	 * #označená() označená}.)</p>
	 * 
	 * @see #aktívna()
	 * @see #odznač()
	 * @see #zrušOznačenie()
	 */
	public void označ() { setSelected(true); }

	/** <p><a class="alias"></a> Alias pre {@link #označ() označ}.</p> */
	public void oznac() { setSelected(true); }

	/**
	 * <p>Zruší označenie položky. (Pre viac informácií pozri opis metódy
	 * {@link #označená() označená}.)</p>
	 * 
	 * @see #aktívna()
	 * @see #označ()
	 * @see #zrušOznačenie()
	 */
	public void odznač() { setSelected(false); }

	/** <p><a class="alias"></a> Alias pre {@link #odznač() odznač}.</p> */
	public void odznac() { setSelected(false); }

	/**
	 * <p>Zruší označenie položky. (Pre viac informácií pozri opis metódy
	 * {@link #označená() označená}.)</p>
	 * 
	 * @see #aktívna()
	 * @see #označ()
	 * @see #odznač()
	 */
	public void zrušOznačenie() { setSelected(false); }

	/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenie() zrušOznačenie}.</p> */
	public void zrusOznacenie() { setSelected(false); }


	/**
	 * <p>Zistí, či je položka viditeľná (zobrazená) alebo nie. Po
	 * vytvorení je položka predvolene viditeľná, môžeme ju skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. (Položky ponúk však namiesto skrývania a zobrazovania
	 * odporúčame {@linkplain #aktivuj() aktivovať} alebo {@linkplain 
	 * #deaktivuj() deaktivovať}.) Alternatívou tejto metódy je metóda
	 * {@link #zobrazená() zobrazená}.</p>
	 * 
	 * @see #zobrazená()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean viditeľná() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #viditeľná() viditeľná}.</p> */
	public boolean viditelna() { return isVisible(); }

	/**
	 * <p>Zistí, či je položka zobrazená (viditeľná) alebo nie. Po
	 * vytvorení je položka predvolene viditeľná, môžeme ju skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. (Položky ponúk však namiesto skrývania a zobrazovania
	 * odporúčame {@linkplain #aktivuj() aktivovať} alebo {@linkplain 
	 * #deaktivuj() deaktivovať}.) Alternatívou tejto metódy je metóda
	 * {@link #viditeľná() viditeľná}.</p>
	 * 
	 * @see #viditeľná()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean zobrazená() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #zobrazená() zobrazená}.</p> */
	public boolean zobrazena() { return isVisible(); }

	/**
	 * <p>Zobrazí položku. (Pre viac informácií pozri opis metódy
	 * {@link #zobrazená() zobrazená}.)</p>
	 * 
	 * @see #viditeľná()
	 * @see #zobrazená()
	 * @see #skry()
	 */
	public void zobraz() { setVisible(true); }

	/**
	 * <p>Skryje položku. (Pre viac informácií pozri opis metódy
	 * {@link #zobrazená() zobrazená}.)</p>
	 * 
	 * @see #viditeľná()
	 * @see #zobrazená()
	 * @see #zobraz()
	 */
	public void skry() { setVisible(false); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu farbu textu položky.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @return aktuálna farba textu položky (objekt typu {@link Farba
	 *     Farba})
	 */
	public Farba farbaTextu()
	{
		Color farba = getForeground();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastav farbu textu položky. Nastaví farbu
	 * a priehľadnosť textu položky podľa zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param nováFarba objekt typu {@link Color Color} (alebo
	 *     odvodeného napr. {@link Farba Farba}) s novou farbou textu
	 *     položky; jestvuje paleta predvolených farieb (pozri napr.:
	 *     {@link Farebnosť#biela biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna
	 *     čierna}…)
	 */
	public void farbaTextu(Color nováFarba) { setForeground(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť textu položky podľa zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu textu položky
	 */
	public void farbaTextu(Farebnosť objekt) { farbaTextu(objekt.farba()); }

	/**
	 * <p>Nastaví farbu textu položky podľa zadaných farebných zložiek.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba textu
	 * 
	 * @see #farbaTextu(Color)
	 */
	public Farba farbaTextu(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		setForeground(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť textu položky podľa zadaných
	 * farebných zložiek a úrovne priehľadnosti.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba textu
	 * 
	 * @see #farbaTextu(Color)
	 */
	public Farba farbaTextu(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		setForeground(farba);
		return farba;
	}

	/**
	 * <p>Nastaví zdedenú farbu textu položky. Farba textu položky bude
	 * zdedená od nadradeného komponentu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 */
	public void zdedenáFarbaTextu() { farbaTextu((Color)null); }

	/** <p><a class="alias"></a> Alias pre {@link #zdedenáFarbaTextu() zdedenáFarbaTextu}.</p> */
	public void zdedenaFarbaTextu() { farbaTextu((Color)null); }


	/**
	 * <p><a class="getter"></a> Číta farbu pozadia položky.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @return aktuálna farba pozadia položky (objekt typu {@link Farba
	 *     Farba})
	 */
	public Farba farbaPozadia()
	{
		Color farba = getBackground();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť pozadia položky
	 * podľa zadanej farebnej inštancie.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.
	 * (Najmenej na jednom testovanom systéme nemala zmena tejto
	 * vlastnosti žiadny efekt.)</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu pozadia;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna
	 *     čierna}…)
	 */
	public void farbaPozadia(Color nováFarba) { setBackground(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť pozadia položky podľa zadaného
	 * objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.
	 * (Najmenej na jednom testovanom systéme nemala zmena tejto
	 * vlastnosti žiadny efekt.)</p>
	 * 
	 * @param objekt objekt určujúci novú farbu pozadia
	 */
	public void farbaPozadia(Farebnosť objekt)
	{ farbaPozadia(objekt.farba()); }

	/**
	 * <p>Nastaví farbu pozadia položky podľa zadaných farebných zložiek.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.
	 * (Najmenej na jednom testovanom systéme nemala zmena tejto
	 * vlastnosti žiadny efekt.)</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia
	 * 
	 * @see #farbaPozadia(Color)
	 */
	public Farba farbaPozadia(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		setBackground(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť pozadia položky podľa zadaných
	 * farebných zložiek a úrovne priehľadnosti.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.
	 * (Najmenej na jednom testovanom systéme nemala zmena tejto
	 * vlastnosti žiadny efekt.)</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia
	 * 
	 * @see #farbaPozadia(Color)
	 */
	public Farba farbaPozadia(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		setBackground(farba);
		return farba;
	}

	/**
	 * <p>Nastaví zdedenú farbu pozadia položky.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.
	 * (Najmenej na jednom testovanom systéme nemala zmena tejto
	 * vlastnosti žiadny efekt.)</p>
	 */
	public void zdedenáFarbaPozadia() { farbaPozadia((Color)null); }

	/** <p><a class="alias"></a> Alias pre {@link #zdedenáFarbaPozadia() zdedenáFarbaPozadia}.</p> */
	public void zdedenaFarbaPozadia() { farbaPozadia((Color)null); }


	/**
	 * <p><a class="getter"></a> Číta aktuálny typ písma textu položky.</p>
	 * 
	 * @return objekt typu {@link Písmo} – aktuálne písmo
	 * 
	 * @see #písmo(Font)
	 * @see #písmo(String, double)
	 * @see #farbaTextu(Color)
	 * @see #farbaPozadia(Color)
	 */
	public Písmo písmo()
	{
		Font písmo = getFont();
		if (písmo instanceof Písmo) return (Písmo)písmo;
		return new Písmo(písmo);
	}

	/** <p><a class="alias"></a> Alias pre {@link #písmo() písmo}.</p> */
	public Pismo pismo()
	{
		Font písmo = getFont();
		if (písmo instanceof Pismo) return (Pismo)písmo;
		return new Pismo(písmo);
	}

	/**
	 * <p><a class="setter"></a> Nastaví nový typ písma textu položky.</p>
	 * 
	 * @param novéPísmo objekt typu {@link Písmo} alebo {@link Font}
	 *     určujúci nový typ písma
	 * 
	 * @see #písmo()
	 * @see #písmo(String, double)
	 * @see #farbaTextu(Color)
	 * @see #farbaPozadia(Color)
	 */
	public void písmo(Font novéPísmo) { setFont(novéPísmo); }

	/** <p><a class="alias"></a> Alias pre {@link #písmo(Font) písmo}.</p> */
	public void pismo(Font novéPísmo) { písmo(novéPísmo); }

	/**
	 * <p>Nastaví nový typ písma textu položky. (Nová inštancia triedy
	 * {@link Písmo Písmo} je touto metódou vrátená na prípadné ďalšie
	 * použitie.)</p>
	 * 
	 * @param názov názov písma; môže byť všeobecný názov logického
	 *     písma (Dialog, DialogInput, Monospaced, Serif, SansSerif…)
	 *     alebo názov konkrétneho písma (Times New Roman, Arial…)
	 * @param veľkosť veľkosť písma v bodoch (hodnota je zaokrúhlená
	 *     na typ {@code typefloat})
	 * @return nový objekt typu {@link Písmo}
	 * 
	 * @see #písmo()
	 * @see #písmo(Font)
	 * @see #farbaTextu(Color)
	 * @see #farbaPozadia(Color)
	 */
	public Písmo písmo(String názov, double veľkosť)
	{
		Písmo písmo = new Písmo(názov, Písmo.PLAIN, veľkosť);
		setFont(písmo);
		return písmo;
	}

	/** <p><a class="alias"></a> Alias pre {@link #písmo(String, double) písmo}.</p> */
	public Pismo pismo(String názov, double veľkosť)
	{ return new Pismo(písmo(názov, veľkosť)); }


	/**
	 * <p><a class="getter"></a> Prečíta aktuálny text položky.</p>
	 * 
	 * @return aktuálny text položky
	 * 
	 * @see #text(String)
	 */
	public String text() { return getText(); }

	/**
	 * <p><a class="setter"></a> Nastaví nový text položky.</p>
	 * 
	 * @param text nový text položky
	 * 
	 * @see #text()
	 */
	public void text(String text) { setText(text); }


	// Tieto definície nefungujú:
	// * @see #ikonaStlačenej(String)
	// * @see #ikonaDeaktivovanej(String)
	// * @see #ikonaOznačenej(String)
	// * @see #ikonaDeaktivovanejOznačenej(String)

	/**
	 * <p>Nastaví alebo odstráni ikonu položky uloženú v súbore s obrázkom.
	 * Táto metóda nastavuje základnú ikonu položky. (Bez nej nemá význam
	 * priraďovať položke ostatné druhy ikon…)</p>
	 * 
	 * <p>Ikona prečítaná zo súboru je prevedená z obrázka, ktorý je
	 * chápaný ako zdroj a ktorý po prečítaní zostane uložený vo
	 * vnútornej pamäti sveta rovnako ako prevedená verzia ikony.
	 * Obidva objekty môžu byť v prípade potreby (napríklad ak sa obsah
	 * súboru na disku zmenil) z vnútornej pamäte odstránené metódou
	 * {@link Svet#uvoľni(String) Svet.uvoľni(názovZdroja)}.
	 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
	 * (resp. ikonami) alebo zvukmi, ktoré prijímajú názov súboru ako
	 * parameter.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Z dôvodu konzistencie je k tejto
	 * metóde definovaný alias {@link #obrazok(String) obrazok(súbor)}
	 * (s názvom bez diakritiky), ktorý má korešpondovať s aliasom metódy
	 * {@link #ikona() ikona()} (určeným na čítanie ikony).</p>
	 * 
	 * @param súbor názov súboru s obrázkom, ktorý bude použitý na
	 *     vytvorenie ikony položky alebo {@code (String)}{@code valnull}
	 *     ak chceme ikonu položky odstrániť
	 * 
	 * @see #ikona(Image)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void ikona(String súbor)
	{
		if (null == súbor) setIcon(null);
		else setIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #ikona(String) ikona}.</p> */
	public void obrazok(String súbor) { ikona(súbor); }


	// Tieto definície nefungujú:
	// * @see #ikonaStlačenej(Image)
	// * @see #ikonaDeaktivovanej(Image)
	// * @see #ikonaOznačenej(Image)
	// * @see #ikonaDeaktivovanejOznačenej(Image)

	/**
	 * <p><a class="setter"></a> Nastaví alebo odstráni ikonu položky určenú
	 * obrázkom. Táto metóda nastavuje základnú ikonu položky. (Bez nej
	 * nemá význam priraďovať položke ostatné druhy ikon…)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Z dôvodu konzistencie je k tejto
	 * metóde definovaný alias {@link #obrazok(Image) obrazok(obrázok)}
	 * (s názvom bez diakritiky), ktorý má korešpondovať s aliasom metódy
	 * {@link #ikona() ikona()}.</p>
	 * 
	 * @param obrázok obrázok určujúci novú ikonu položky alebo
	 *     {@code (Image)}{@code valnull} ak chceme ikonu položky
	 *     odstrániť
	 * 
	 * @see #ikona(String)
	 */
	public void ikona(Image obrázok)
	{
		if (null == obrázok) setIcon(null);
		else setIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #ikona(Image) ikona}.</p> */
	public void obrazok(Image obrázok) { ikona(obrázok); }

	/**
	 * <p><a class="getter"></a> Vráti základnú ikonu položky alebo
	 * {@code valnull} ak položka nemá priradenú ikonu, ktorá bola
	 * vytvorená z inštancie typu {@link Obrázok Obrázok}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Názov tejto metódy neobsahuje
	 * žiadnu diakritiku, preto nemohol byť definovaný prislúchajúci alias,
	 * ktorý by vracal objekt typu {@link Obrazok Obrazok} (t. j. triedy
	 * aliasu, ktorej názov tiež neobsahuje diakritiku). Z toho dôvodu je
	 * alias nahradený metódou {@link #obrazok() obrazok} (bez diakritiky).</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #ikona(String)
	 * @see #ikona(Image)
	 */
	public Obrázok ikona()
	{
		Icon icon = getIcon();
		if (null != icon && icon instanceof ImageIcon)
		{
			Image image = ((ImageIcon)icon).getImage();
			if (null != image && image instanceof Obrázok)
				return (Obrázok)image;
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #ikona() ikona}.</p> */
	public Obrazok obrazok()
	{
		Obrázok obrázok = ikona();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}


	/**
	 * <p><a class="getter"></a> Zistí aktuálu veľkosť medzery medzi ikonou
	 * a textom tejto položky.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda zrkadlí definíciu
	 * z triedy {@link AbstractButton AbstractButton} (čiže položka je
	 * z pohľadu objektového modelu chápaná ako tlačidlo). V niektorých
	 * definíciách vzhľadu (L&F) však nemusia byť položky chápané ako
	 * typické tlačidlá, ich vzhľad sa môže odlišovať a pokus o prácu
	 * s medzerou medzi ikonou a textom nemusí mať očakávaný efekt.</p>
	 * 
	 * @return celé číslo vyjadrujúce počet pixelov medzi ikonou
	 *     a textom tejto položky
	 */
	public int medzeraMedziIkonouATextom()
	{ return getIconTextGap(); }

	/**
	 * <p><a class="setter"></a> Ak má toto položka nastavenú ikonu aj
	 * text, tak táto vlastnosť určuje veľkosť medzery medzi nimi.
	 * Predvolená hodnota je štyri pixely.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda zrkadlí definíciu
	 * z triedy {@link AbstractButton AbstractButton} (čiže položka je
	 * z pohľadu objektového modelu chápaná ako tlačidlo). V niektorých
	 * definíciách vzhľadu (L&F) však nemusia byť položky chápané ako
	 * typické tlačidlá, ich vzhľad sa môže odlišovať a pokus o prácu
	 * s medzerou medzi ikonou a textom nemusí mať očakávaný efekt.</p>
	 * 
	 * @param medzera celé číslo určujúce počet pixelov medzi ikonou
	 *     a textom
	 */
	public void medzeraMedziIkonouATextom(int medzera)
	{ setIconTextGap(medzera); }


	/**
	 * <p>Prepojí túto položku kontextovej ponuky s príkazom priradeným ku
	 * {@linkplain Svet#pridajKlávesovúSkratku(String, int)
	 * klávesovej skratke}.
	 * Odteraz odpoveďou na zvolenie tejto položky nebude spustenie reakcie
	 * {@link ObsluhaUdalostí#voľbaKontextovejPoložky()
	 * voľbaKontextovejPoložky}, ale reakcie
	 * {@link ObsluhaUdalostí#klávesováSkratka() klávesováSkratka}.
	 * Ak chcete priradenie príkazu k položke zrušiť, zadajte hodnotu
	 * {@code valnull}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Predtým, než
	 * {@linkplain Svet#odoberKlávesovúSkratku(String) odoberiete klávesovú
	 * skratku} zo sveta, zrušte aj priadenie príkazu k tejto položke
	 * (zadaním hodnoty {@code valnull}), inak by mohlo vzniknúť neočakávané
	 * správanie programu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Spustenie tejto metódy spôsobí
	 * aktualizáciu údajov vzhľadu súvisiacich s klávesovou skratkou
	 * zadaného príkazu podľa vzhľadu tejto položky. Každé ďalšie spustenie
	 * tejto metódy pre niektorú položku ponuky alebo kontextovej ponuky
	 * spôsobí novú aktualizáciu údajov vzhľadu a spätne aktualizáciu
	 * vzhľadu všetkých položiek, ktoré boli predtým s týmto príkazom
	 * previazané. Preto je vhodné vyvarovať sa previazaniu konkrétneho
	 * príkazu s viacerými položkami ponuky alebo kontextovej ponuky (ak
	 * to nie je vyslovene vyžadované).</p>
	 * 
	 * @param príkaz príkaz na priradenie alebo {@code valnull}
	 * 
	 * @see Svet#pridajKlávesovúSkratku(String, int)
	 * @see Svet#pridajKlávesovúSkratku(String, int, int)
	 */
	public void príkaz(String príkaz)
	{
		if (null == príkaz)
		{
			setAction(null);
			addActionListener(voľbaKontextovejPoložky);
		}
		else
		{
			removeActionListener(voľbaKontextovejPoložky);
			Svet.KlávesováSkratka skratka =
				Svet.klávesovéSkratky.get(príkaz);
			skratka.putValue(AbstractAction.NAME, getText());
			skratka.putValue(AbstractAction.MNEMONIC_KEY, getMnemonic());
			skratka.putValue(AbstractAction.SMALL_ICON, getIcon());
			setAction(skratka);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #príkaz(String) príkaz}.</p> */
	public void prikaz(String príkaz) { príkaz(príkaz); }


	/**
	 * <p>Vráti skript priradený k tejto položke alebo {@code valnull},
	 * ak k položke nebol priradený žiadny skript.</p>
	 * 
	 * @return skript priradený k tejto položke alebo {@code valnull}
	 * 
	 * @see #riadkySkriptu()
	 * @see #skript(String[])
	 * @see #skript(String)
	 * @see #skript(Skript)
	 * @see Svet#vykonajSkript(String[])
	 * @see Skript
	 */
	public Skript skript()
	{
		if (null == skript && null != riadkySkriptu)
			skript = Skript.vyrob(riadkySkriptu);
		return skript;
	}

	/**
	 * <p>Vráti riadky skriptu, ak bol skript k tejto položke priradený
	 * v textovej forme. V opačnom prípade vráti {@code valnull}, pričom
	 * položka môže mať definovaný skript – pozri aj metódu {@link 
	 * #skript() skript}.</p>
	 * 
	 * @return skript priradený k tejto položke alebo {@code valnull}
	 * 
	 * @see #skript()
	 * @see #skript(String[])
	 * @see #skript(String)
	 * @see #skript(Skript)
	 * @see Svet#vykonajSkript(String[])
	 * @see Skript
	 */
	public String[] riadkySkriptu() { return riadkySkriptu; }

	/**
	 * <p>Priradí k tejto položke skript, ktorý bude automaticky vykonaný
	 * po jej zvolení. (Pozri aj metódu {@link Svet#vykonajSkript(String[])
	 * vykonajSkript} a triedu {@link Skript Skript}.) Ak chcete skript
	 * položky vymazať, zadajte hodnotu {@code valnull}.</p>
	 * 
	 * @param riadky skript vo forme poľa reťazcov (riadkov skriptu)
	 * 
	 * @see #skript()
	 * @see #riadkySkriptu()
	 * @see #skript(String)
	 * @see #skript(Skript)
	 * @see Svet#vykonajSkript(String[])
	 * @see Skript
	 */
	public void skript(String[] riadky)
	{
		this.riadkySkriptu = riadky;
		this.skript = null;
	}

	/**
	 * <p>Priradí k tejto položke skript, ktorý bude automaticky vykonaný
	 * po jej zvolení. (Pozri aj metódu {@link Svet#vykonajSkript(String[])
	 * vykonajSkript} a triedu {@link Skript Skript}.) Ak chcete skript
	 * položky vymazať, zadajte hodnotu {@code valnull}.</p>
	 * 
	 * @param skript skript vo forme reťazca oddeľovaného znakmi
	 *     nového riadka
	 * 
	 * @see #skript()
	 * @see #riadkySkriptu()
	 * @see #skript(String[])
	 * @see #skript(Skript)
	 * @see Svet#vykonajSkript(String[])
	 * @see Skript
	 */
	public void skript(String skript)
	{
		if (null == skript) this.riadkySkriptu = null; else
		{
			String riadky[] = Skript.vykonajSkriptRiadkovač.split(skript);
			this.riadkySkriptu = riadky;
		}
		this.skript = null;
	}

	/**
	 * <p>Priradí k tejto položke skript, ktorý bude automaticky vykonaný
	 * po jej zvolení. (Pozri aj metódu {@link Svet#vykonajSkript(String[])
	 * vykonajSkript} a triedu {@link Skript Skript}.) Ak chcete skript
	 * položky vymazať, zadajte hodnotu {@code valnull}.</p>
	 * 
	 * @param skript inštanicia triedy {@link Skript Skript}
	 * 
	 * @see #skript()
	 * @see #riadkySkriptu()
	 * @see #skript(String[])
	 * @see #skript(String)
	 * @see Svet#vykonajSkript(String[])
	 * @see Skript
	 */
	public void skript(Skript skript)
	{
		this.riadkySkriptu = null;
		this.skript = skript;
	}


	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }
}
