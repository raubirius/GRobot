
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import javax.swing.border.Border;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import static knižnica.Farebnosť.*;
import static javax.swing.ScrollPaneConstants.*;

import static javax.swing.text.StyleConstants.ALIGN_CENTER;
import static javax.swing.text.StyleConstants.ALIGN_JUSTIFIED;
import static javax.swing.text.StyleConstants.ALIGN_LEFT;
import static javax.swing.text.StyleConstants.ALIGN_RIGHT;

// ------------------------------- //
//  *** Trieda PoznámkovýBlok ***  //
// ------------------------------- //

/**
 * <p>Trieda {@code currPoznámkovýBlok} dovoľuje vytvárať v aplikácii
 * používajúcej robot textové bloky, ktoré sú zobrazované nad plátnom
 * (t. j. nie sú jeho súčasťou) a dovoľujú zobraziť alebo upraviť väčšie
 * množstvo textu. Každý nový poznámkový blok je automaticky umiestnený
 * na súradnice stredu plátna s predvolenými rozmermi 400 × 300 bodov
 * (šírka × výška). Táto trieda uzatvára a rozširuje funkcie triedy
 * Javy {@link JTextPane JTextPane} (umiestnenej v komponente {@link 
 * JScrollPane JScrollPane}). Polohovanie, prilepovanie
 * a automatické rozťahovanie poznámkových blokov je podobné ako pri
 * triede {@link RolovaciaLišta RolovaciaLišta}, to znamená, že
 * komponenty poznámkových blokov majú i možnosť zapnutia automatickej
 * zmeny veľkosti podľa práve zobrazenej časti {@linkplain Plátno
 * plátna}.</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Poznámkové bloky používajú
 * súradnicový priestor rámca a používajú na jeho realizáciu vlastný
 * zabudovaný mechanizmus, preto používajte na manipuláciu s polohou
 * a rozmermi blokov metódy definované v tejto triede, ako:
 * {@link PoznámkovýBlok#polohaX(double) polohaX},
 * {@link PoznámkovýBlok#polohaY(double) polohaY},
 * {@link PoznámkovýBlok#šírka(double) šírka},
 * {@link PoznámkovýBlok#výška(double) výška}…, a nie zdedené metódy
 * ako {@link JTextPane#setLocation(int, int) setLocation},
 * {@link JTextPane#setSize(int, int) setSize}…</p>
 */
@SuppressWarnings("serial")
public class PoznámkovýBlok extends JTextPane implements Poloha, Rozmer
{
	/**
	 * <p>Hodnota konštanty zarovnania odseku na stred. Pozri metódu
	 * {@link #zarovnať(int) zarovnať}.</p>
	 */
	public static final int ZAROVNAŤ_NA_STRED = ALIGN_CENTER;

	/**
	 * <p>Hodnota konštanty zarovnania odseku podľa okrajov. Pozri metódu
	 * {@link #zarovnať(int) zarovnať}.</p>
	 */
	public static final int ZAROVNAŤ_PODĽA_OKRAJOV = ALIGN_JUSTIFIED;

	/**
	 * <p>Hodnota konštanty zarovnania odseku doľava. Pozri metódu
	 * {@link #zarovnať(int) zarovnať}.</p>
	 */
	public static final int ZAROVNAŤ_DOĽAVA = ALIGN_LEFT;

	/**
	 * <p>Hodnota konštanty zarovnania odseku doprava. Pozri metódu
	 * {@link #zarovnať(int) zarovnať}.</p>
	 */
	public static final int ZAROVNAŤ_DOPRAVA = ALIGN_RIGHT;


	/** <p>Alias pre {@link #ZAROVNAŤ_NA_STRED ZAROVNAŤ_NA_STRED}.</p> */
	public static final int ZAROVNAT_NA_STRED = ALIGN_CENTER;

	/** <p>Alias pre {@link #ZAROVNAŤ_PODĽA_OKRAJOV ZAROVNAŤ_PODĽA_OKRAJOV}.</p> */
	public static final int ZAROVNAT_PODLA_OKRAJOV = ALIGN_JUSTIFIED;

	/** <p>Alias pre {@link #ZAROVNAŤ_DOĽAVA ZAROVNAŤ_DOĽAVA}.</p> */
	public static final int ZAROVNAT_DOLAVA = ALIGN_LEFT;

	/** <p>Alias pre {@link #ZAROVNAŤ_DOPRAVA ZAROVNAŤ_DOPRAVA}.</p> */
	public static final int ZAROVNAT_DOPRAVA = ALIGN_RIGHT;


	// Príznak zruśenia dekoru poznámkového bloku:
	private boolean zrušDekor = false;

	// Inštancie slúžiace na zálohovanie dekoru poznámkového bloku:
	private Border okrajRolovania = null;
	private Color pozadieRolovania = null;
	private boolean nepriehľadnosťRolovania = false;

	private Border okrajVýrezu = null;
	private Color pozadieVýrezu = null;
	private boolean nepriehľadnosťVýrezu = false;

	private Border okrajBloku = null;
	private Color pozadieBloku = null;
	private boolean textPaneOpaque = false;

	// Tabla rolovania poznámkového bloku
	@SuppressWarnings("serial")
	/*packagePrivate*/ class RolovaniePoznámkovéhoBloku extends JScrollPane
	{
		/**
		 * <p>Atribút uchovávajúci komponent poznámkového bloku v tomto
		 * kontajneri (t. j. v table rolovania).</p>
		 */
		public PoznámkovýBlok poznámkovýBlok;

		/** <p>Konštruktor.</p> */
		public RolovaniePoznámkovéhoBloku(Component komponent)
		{
			super(komponent);
			poznámkovýBlok = (PoznámkovýBlok)komponent;

			// setBorder(null);
			// setBackground(žiadna);
			// setOpaque(false);

			okrajRolovania = getBorder();
			pozadieRolovania = getBackground();
			nepriehľadnosťRolovania = isOpaque();

			// JViewport viewport = getViewport();
			// viewport.setBorder(null);
			// viewport.setBackground(žiadna);
			// viewport.setOpaque(false);

			JViewport viewport = getViewport();
			if (null != viewport)
			{
				okrajVýrezu = viewport.getBorder();
				pozadieVýrezu = viewport.getBackground();
				nepriehľadnosťVýrezu = viewport.isOpaque();
			}
		}

		/** <p>Zrušenie/obnovenie dekoru.</p> */
		public void zrušDekor(boolean zrušiť)
		{
			if (zrušiť)
			{
				setBorder(null);
				setBackground(žiadna);
				setOpaque(false);

				JViewport viewport = getViewport();
				if (null != viewport)
				{
					viewport.setBorder(null);
					viewport.setBackground(žiadna);
					viewport.setOpaque(false);
				}
			}
			else
			{
				JViewport viewport = getViewport();
				if (null != viewport)
				{
					viewport.setBorder(okrajVýrezu);
					viewport.setBackground(pozadieVýrezu);
					viewport.setOpaque(nepriehľadnosťVýrezu);
				}

				setBorder(okrajRolovania);
				setBackground(pozadieRolovania);
				setOpaque(nepriehľadnosťRolovania);
			}
		}
	}


	// Užitočné odkazy:
	// https://docs.oracle.com/javase/8/docs/api/javax/swing/JTextPane.html
	// https://docs.oracle.com/javase/tutorial/uiswing/components/editorpane.html
	// https://docs.oracle.com/javase/tutorial/uiswing/components/text.html
	// https://docs.oracle.com/javase/tutorial/uiswing/components/generaltext.html
	// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#TextSamplerDemo
	// https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextSamplerDemoProject/src/components/TextSamplerDemo.java
	// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#TextComponentDemo
	// https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextComponentDemoProject/src/components/TextComponentDemo.java

	// Parametre polohy a veľkosti poznámkového bloku:
	/*packagePrivate*/ int x, y, šírka, výška, šírkaRodiča, výškaRodiča;

	// Kde je blok umiestnený:
	/*packagePrivate*/ JPanel hlavnýPanel;

	// Parametre prilepenia k jednotlivým okrajom a roztiahnutia
	// v dvoch smeroch (kombinácia bitov):
	private byte prilepenieRoztiahnutie = 0;

	// Tabla (pane) rolovania:
	private RolovaniePoznámkovéhoBloku rolovanie;

	// Určuje predvolené hodnoty vlastností poznámkového bloku:
	private void vytvor()
	{
		vytvor(400, 300);
	}

	// Vlastnosti upravujúce fungovanie klávesov TAB a Enter:
	private boolean zakážTabulátor = false;
	private boolean zakážEnter = false;

	private final static Pattern ibaPrvýRiadok = Pattern.compile(
		"^([^\\r\\n]*)(.*)$", Pattern.DOTALL);

	private class FilterDokumentu extends DocumentFilter
	{
		public void insertString(FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException
		{
			if (zakážTabulátor) string = string.replace("\t", " ");

			// System.out.println("insert: " + string);
			// System.out.println("string.indexOf('\r'): " +
			// 	string.indexOf('\r'));
			// System.out.println("string.indexOf('\n'): " +
			// 	string.indexOf('\n'));
			// System.out.println("replaceFirst: " +
			// 	ibaPrvýRiadok.matcher(string).replaceFirst("v$1w"));

			if (zakážEnter && (-1 != string.indexOf('\r') ||
				-1 != string.indexOf('\n')))
				string = ibaPrvýRiadok.matcher(string).replaceFirst("$1");

			fb.insertString(offset, string, attr);
		}

		public void replace(FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException
		{
			if (zakážTabulátor) string = string.replace("\t", " ");

			// System.out.println("replace: " + string);
			// System.out.println("string.indexOf('\r'): " +
			// 	string.indexOf('\r'));
			// System.out.println("string.indexOf('\n'): " +
			// 	string.indexOf('\n'));
			// System.out.println("replaceFirst: " +
			// 	ibaPrvýRiadok.matcher(string).replaceFirst("x$1y"));

			if (zakážEnter && (-1 != string.indexOf('\r') ||
				-1 != string.indexOf('\n')))
				string = ibaPrvýRiadok.matcher(string).replaceFirst("$1");

			fb.replace(offset, length, string, attr);
		}
	}

	private final FilterDokumentu filterDokumentu = new FilterDokumentu();

	private void vytvor(int vlastnáŠírka, int vlastnáVýška)
	{
		rolovanie = new RolovaniePoznámkovéhoBloku(this);
		šírka = vlastnáŠírka; výška = vlastnáVýška;

		šírkaRodiča = Plátno.šírkaPlátna;
		výškaRodiča = Plátno.výškaPlátna;

		x = (šírkaRodiča - šírka) / 2;
		y = (výškaRodiča - výška) / 2;
		setPreferredSize(new Dimension(vlastnáŠírka, vlastnáVýška));

		// setFocusTraversalKeysEnabled(false);

		Svet.hlavnýPanel.add(rolovanie, 0);
		Svet.hlavnýPanel.doLayout();
		hlavnýPanel = Svet.hlavnýPanel;

		// setBorder(null);
		// setBackground(žiadna);
		// setOpaque(false);

		okrajBloku = getBorder();
		pozadieBloku = getBackground();
		textPaneOpaque = isOpaque();

		zrušDekor(true);

		Document dokument = getDocument();
		if (dokument instanceof AbstractDocument)
			((AbstractDocument)dokument).setDocumentFilter(filterDokumentu);

		addHyperlinkListener(new HyperlinkListener()
			{
				public void hyperlinkUpdate(HyperlinkEvent e)
				{
					if (e.getEventType() ==
						HyperlinkEvent.EventType.ACTIVATED)
					{
						// System.out.println("URL: " + e.getURL());
						// if (null != e.getURL())
						// 	System.out.println("URL: " +
						// 		e.getURL().toString());
						// System.out.println("URL: " +
						// 	e.getDescription());

						ÚdajeUdalostí.poslednýOdkaz = e.getDescription();
						ÚdajeUdalostí.poslednýPoznámkovýBlok =
							PoznámkovýBlok.this;

						if (null != ObsluhaUdalostí.počúvadlo)
							// ‼TODO‼ – Dočasné riešenia‼
							// Použiť tie z triedy GRobot‼
							// 
							// (Poznámka: 25. 7. 2021 – netuším, aká je
							// táto pripomienka stará a ešte menej tuším,
							// čo som ňou v čase jej vytvorenia myslel.)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.aktiváciaOdkazu();
								ObsluhaUdalostí.počúvadlo.aktivaciaOdkazu();
							}

							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								int početPočúvajúcich =
									GRobot.počúvajúciRozhranie.size();
								for (int i = 0; i < početPočúvajúcich; ++i)
								{
									GRobot počúvajúci =
										GRobot.počúvajúciRozhranie.get(i);
									počúvajúci.aktiváciaOdkazu();
									počúvajúci.aktivaciaOdkazu();
								}
							}
					}
				}
			});

		addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					// knižnica.log.Log.logOn = true;
					// knižnica.log.Log.logIn(
					// 	e.getComponent().getClass().getName());
					// knižnica.log.Log.logOut(getFocusTraversalKeysEnabled());

					// TODO: Bolo by treba otestovať na macOS, lebo v tomto
					// sú isté nekonzistencie v implementáciách…

					// V tomto komponente globálna obslužná metóda na zmenu
					// fokusu nefunguje.
					// Svet.spracujFokus(e/*, false // TODO: del */)
					if (zakážTabulátor && e.getKeyCode() == KeyEvent.VK_TAB &&
						!e.isAltDown() && !e.isMetaDown())
					{
						if (!Svet.volajObsluhyFokusu(!e.isShiftDown()))
						{
							e.consume();

							final KeyboardFocusManager
								manažér = KeyboardFocusManager.
								getCurrentKeyboardFocusManager();

							if (e.isShiftDown())
							{
								manažér.focusPreviousComponent();
								SwingUtilities.invokeLater(() ->
								{
									if (manažér.getFocusOwner()
										instanceof JScrollBar)
										manažér.focusPreviousComponent();
								});
							}
							else
							{
								manažér.focusNextComponent();
								SwingUtilities.invokeLater(() ->
								{
									if (manažér.getFocusOwner()
										instanceof JScrollBar)
										manažér.focusNextComponent();
								});
							}
						}
					}
					else if (zakážEnter && e.getKeyCode() ==
						KeyEvent.VK_ENTER && !e.isAltDown() &&
						!e.isControlDown() && !e.isMetaDown() &&
						!e.isShiftDown())
					{
						Svet.KlávesováSkratka klávesováSkratka = null;
						Object o = hlavnýPanel.getInputMap(JPanel.
							// WHEN_IN_FOCUSED_WINDOW
							WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
							KeyStroke.getKeyStrokeForEvent(e));
						if (null != o)
						{
							Action a = hlavnýPanel.getActionMap().get(o);
							if (a instanceof Svet.KlávesováSkratka)
								klávesováSkratka = (Svet.KlávesováSkratka)a;
						}
						e.consume();
						if (null != klávesováSkratka)
							klávesováSkratka.actionPerformed(null);
					}
				}

				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});
	}


	// Umiestnenie poznámkového bloku na základe súkromných parametrov –
	// použité v metóde rozmiestňovania komponentov:
	//    hlavnýPanel.doLayout()
	/*packagePrivate*/ void umiestni(int x1, int y1, int šírka1, int výška1)
	{
		int x2 = x1 + x, y2 = y1 + y, šírka2 = šírka, výška2 = výška;

		if (0 != (prilepenieRoztiahnutie & 16))
		{
			výška2 = výška1;
			y2 += ((výška - výška1) / 2) - (výška1 % 2);

			if (y2 < y1 || y2 < 0)
			{
				if (y1 < 0)
				{
					výška2 += y2;
					y2 = 0;
				}
				else if (y2 < y1)
				{
					výška2 += y2 - y1;
					y2 = y1;
				}
			}

			if (y1 < 0)
			{
				if ((výška2 + y2) > výška1)
					výška2 = výška1 - y2;
			}
			else
			{
				if ((výška2 + y2 - y1) > výška1)
					výška2 = výška1 + y1 - y2;
			}
		}

		if (0 != (prilepenieRoztiahnutie & 32))
		{
			šírka2 = šírka1;
			x2 += ((šírka - šírka1) / 2) - (šírka1 % 2);

			if (x2 < x1 || x2 < 0)
			{
				if (x1 < 0)
				{
					šírka2 += x2;
					x2 = 0;
				}
				else if (x2 < x1)
				{
					šírka2 += x2 - x1;
					x2 = x1;
				}
			}

			if (x1 < 0)
			{
				if ((šírka2 + x2) > šírka1)
					šírka2 = šírka1 - x2;
			}
			else
			{
				if ((šírka2 + x2 - x1) > šírka1)
					šírka2 = šírka1 + x1 - x2;
			}
		}

		if (1 == (prilepenieRoztiahnutie & 3))
			x2 += ((šírka - šírka1) / 2) - (šírka1 % 2);
		else if (2 == (prilepenieRoztiahnutie & 3))
			x2 -= ((šírka - šírka1) / 2);

		if (4 == (prilepenieRoztiahnutie & 12))
			y2 += ((výška - výška1) / 2) - (výška1 % 2);
		else if (8 == (prilepenieRoztiahnutie & 12))
			y2 -= ((výška - výška1) / 2);

		setBounds(0, 0, šírka2, výška2);
		rolovanie.setBounds(x2, y2, šírka2, výška2);
	}


	/**
	 * <p>Základný konštruktor poznámkového bloku.</p>
	 * 
	 * <p>Vytvorí poznámkový blok umiestnený v strede plátna
	 * s predvolenými rozmermi 400 × 300 bodov.</p>
	 * 
	 * @see #PoznámkovýBlok(String)
	 * @see #PoznámkovýBlok(String, boolean)
	 */
	public PoznámkovýBlok()
	{
		super();
		vytvor();
	}


	/**
	 * <p>Konštruktor poznámkového bloku s predvoleným textom.</p>
	 * 
	 * <p>Vytvorí poznámkový blok umiestnený v strede plátna
	 * s predvolenými rozmermi 400 × 300 bodov a zadaným textom
	 * poznámkového bloku.</p>
	 * 
	 * <p>Ak sa zadnaný text začína značkou <code>&lt;html&gt;</code>
	 * (malými písmenami), tak je typ dokumentu poznámkového bloku
	 * nastavený na <code>text/html</code>, inak na
	 * <code>text/plain</code>.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @param text predvolený text poznámkového bloku
	 * 
	 * @see #PoznámkovýBlok()
	 * @see #PoznámkovýBlok(String, boolean)
	 * @see #text(String)
	 */
	public PoznámkovýBlok(String text)
	{
		super();
		if (text.startsWith("<html>"))
			setContentType("text/html");
		else
			setContentType("text/plain");
		setText(text);
		vytvor();
	}

	/**
	 * <p>Konštruktor poznámkového bloku s predvoleným textom a stavom
	 * povolenia úprav.</p>
	 * 
	 * <p>Vytvorí poznámkový blok umiestnený v strede plátna
	 * s predvolenými rozmermi 400 × 300 bodov a zadaným textom
	 * poznámkového bloku, pričom pri inicializácii nastaví stav
	 * povolenia úprav textu podľa zadanej logickej hodnoty.</p>
	 * 
	 * <p>Ak sa zadnaný text začína značkou <code>&lt;html&gt;</code>
	 * (malými písmenami), tak je typ dokumentu poznámkového bloku
	 * nastavený na <code>text/html</code>, inak na
	 * <code>text/plain</code>.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @param text predvolený text poznámkového bloku
	 * @param úpravy parameter určujúci, či bude blok určený aj na
	 *     úpravy – {@code valtrue} alebo len na čítanie –
	 *     {@code valfalse}
	 * 
	 * @see #PoznámkovýBlok()
	 * @see #PoznámkovýBlok(String)
	 * @see #text(String)
	 * @see #neupravuj()
	 * @see #upravuj()
	 */
	public PoznámkovýBlok(String text, boolean úpravy)
	{
		super();
		if (text.startsWith("<html>"))
			setContentType("text/html");
		else
			setContentType("text/plain");
		setText(text);
		setEditable(úpravy);
		vytvor();
	}

		// TODO – konštruktor s nejakým určením presmerovania (prúdu?)…
		// Minimálne určenie plátna, z ktorého výstup sem bude nasmerovaný.
		// (Ak sa dá, tak aj štandardný výstup…)
		// S plátnom sa to dá aj obojsmerne – aj v plátne môže byť metóda
		// presmerujKonzoluDo(PoznámkovýBlok …)
		// V konzole sa pracuje s prúdmi. To by sa dalo využiť.


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu x-ovú súradnicu polohy
	 * poznámkového bloku.</p>
	 * 
	 * @return aktuálna x-ová súradnica polohy poznámkového bloku
	 * 
	 * @see #polohaX(double)
	 */
	public double polohaX() { return x - ((šírkaRodiča - šírka) / 2); }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu y-ovú súradnicu polohy
	 * poznámkového bloku.</p>
	 * 
	 * @return aktuálna y-ová súradnica polohy poznámkového bloku
	 * 
	 * @see #polohaY(double)
	 */
	public double polohaY() { return -y + ((výškaRodiča - výška) / 2); }

	/**
	 * <p><a class="setter"></a> Presunie poznámkový blok na zadanú súradnicu
	 * v smere x.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param novéX nová x-ová súradnica polohy poznámkového bloku
	 * 
	 * @see #polohaX()
	 * @see #poloha(double, double)
	 */
	public void polohaX(double novéX)
	{
		x = ((šírkaRodiča - šírka) / 2) + (int)novéX;
		hlavnýPanel.doLayout();
	}

	/**
	 * <p><a class="setter"></a> Presunie poznámkový blok na zadanú súradnicu
	 * v smere y.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param novéY nová y-ová súradnica polohy poznámkového bloku
	 * 
	 * @see #polohaY()
	 * @see #poloha(double, double)
	 * @see #poloha(Poloha)
	 */
	public void polohaY(double novéY)
	{
		y = ((výškaRodiča - výška) / 2) - (int)novéY;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #polohaX(double) polohaX}.</p> */
	public void súradnicaX(double novéX) { polohaX(novéX); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaX(double) polohaX}.</p> */
	public void suradnicaX(double novéX) { polohaX(novéX); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaY(double) polohaY}.</p> */
	public void súradnicaY(double novéY) { polohaY(novéY); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaY(double) polohaY}.</p> */
	public void suradnicaY(double novéY) { polohaY(novéY); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaX() polohaX}.</p> */
	public double súradnicaX() { return polohaX(); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaY() polohaY}.</p> */
	public double súradnicaY() { return polohaY(); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaX() polohaX}.</p> */
	public double suradnicaX() { return polohaX(); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaY() polohaY}.</p> */
	public double suradnicaY() { return polohaY(); }


	/**
	 * <p>Presunie poznámkový blok na zadané súradnice {@code x},
	 * {@code y}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param x nová x-ová súradnica polohy poznámkového bloku
	 * @param y nová y-ová súradnica polohy poznámkového bloku
	 * 
	 * @see #polohaX(double)
	 * @see #polohaY(double)
	 * @see #poloha(Poloha)
	 */
	public void poloha(double x, double y)
	{
		this.x = ((šírkaRodiča - šírka) / 2) + (int)x;
		this.y = ((výškaRodiča - výška) / 2) - (int)y;
		hlavnýPanel.doLayout();
	}


	/**
	 * <p>Presunie poznámkový blok na súradnice zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param objekt objekt, na ktorého súradnice bude poznámkový blok
	 *     presunutý
	 * 
	 * @see #polohaX(double)
	 * @see #polohaY(double)
	 * @see #poloha(double, double)
	 */
	public void poloha(Poloha objekt)
	{
		poloha((int)objekt.polohaX(), (int)objekt.polohaY());
	}

	/**
	 * <p>Vráti aktuálnu polohu poznámkového bloku.</p>
	 * 
	 * @return aktuálna poloha poznámkového bloku
	 * 
	 * @see #polohaX()
	 * @see #polohaY()
	 */
	public Bod poloha()
	{
		double x = this.x - ((šírkaRodiča - šírka) / 2.0);
		double y = -this.y + ((výškaRodiča - výška) / 2.0);
		return new Bod(x, y);
	}


	/** <p><a class="alias"></a> Alias pre {@link #poloha(double, double) poloha}.</p> */
	public void skočNa(double x, double y) { poloha(x, y); }

	/** <p><a class="alias"></a> Alias pre {@link #poloha(double, double) poloha}.</p> */
	public void skocNa(double x, double y) { poloha(x, y); }

	/** <p><a class="alias"></a> Alias pre {@link #poloha(Poloha) poloha}.</p> */
	public void skočNa(Poloha objekt)
	{ poloha(objekt.polohaX(), objekt.polohaY()); }

	/** <p><a class="alias"></a> Alias pre {@link #poloha(Poloha) poloha}.</p> */
	public void skocNa(Poloha objekt)
	{ poloha(objekt.polohaX(), objekt.polohaY()); }


	/**
	 * <p>Presunie poznámkový blok o zadaný počet bodov
	 * v horizontálnom a vertikálnom smere.</p>
	 * 
	 * <p>Táto metóda presúva poznámkový blok nad plátnom. Upozorňujeme
	 * na to, že zadané hodnoty sú automaticky zaokrúhlené na celé čísla,
	 * čiže ani viacnásobné posunutie poznámkového bloku o hodnotu
	 * z otvoreného intervalu (−1; 1) nebude mať za následok posunutie
	 * poznámkového bloku…</p>
	 * 
	 * @param Δx počet bodov v smere x
	 * @param Δy počet bodov v smere y
	 */
	public void skoč(double Δx, double Δy)
	{
		this.x += Δx;
		this.y -= Δy;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #skoč(double, double) skoč}.</p> */
	public void skoc(double Δx, double Δy) { skoč(Δx, Δy); }


	/**
	 * <p>Overí, či sa poloha tohto poznámkového bloku dokonale zhoduje so
	 * zadanými súradnicami. Ak je zistená zhoda, tak metóda vráti hodnotu
	 * {@code valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param x x-ová súradnica, s ktorou má byť porovnaná poloha tohto
	 *     poznámkového bloku
	 * @param y y-ová súradnica, s ktorou má byť porovnaná poloha tohto
	 *     poznámkového bloku
	 * @return {@code valtrue} ak sa poloha tohto poznámkového bloku zhoduje
	 *     so zadanými súradnicami, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(double x, double y)
	{
		double ox = this.x - ((šírkaRodiča - šírka) / 2.0);
		double oy = -this.y + ((výškaRodiča - výška) / 2.0);
		return ox == x && oy == y;
	}

	/**
	 * <p>Overí, či sa poloha tohto poznámkového bloku a poloha zadaného
	 * objektu dokonale zhodujú. Ak je zistená zhoda, tak metóda vráti
	 * hodnotu {@code valtrue}, v opačnom prípade hodnotu
	 * {@code valfalse}.</p>
	 * 
	 * @param poloha objekt, ktorého poloha má byť porovnaná s polohou tohto
	 *     poznámkového bloku
	 * @return {@code valtrue} ak sa poloha tohto poznámkového bloku zhoduje
	 *     s polohou zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(Poloha poloha)
	{
		double ox = this.x - ((šírkaRodiča - šírka) / 2.0);
		double oy = -this.y + ((výškaRodiča - výška) / 2.0);
		return poloha.polohaX() == ox && poloha.polohaY() == oy;
	}


	/**
	 * <p>Prilepí poznámkový blok k ľavému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k pravému okraju. Každé
	 * prilepenie upravuje súradnicový systém poznámkového bloku
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená,
	 * že keď napríklad po prilepení k ľavému okraju posunieme
	 * poznámkový blok na súradnice [10, 0], tak ho v skutočnosti posunieme
	 * na pozíciu desať bodov od ľavého okraja.</p>
	 * 
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepVľavo()
	{
		prilepenieRoztiahnutie &= 60;
		prilepenieRoztiahnutie |= 1;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #prilepVľavo() prilepVľavo}.</p> */
	public void prilepVlavo() { prilepVľavo(); }

	/**
	 * <p>Prilepí poznámkový blok k pravému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k ľavému okraju. Každé
	 * prilepenie upravuje súradnicový systém poznámkového bloku
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená,
	 * že keď napríklad po prilepení k pravému okraju posunieme
	 * poznámkový blok na súradnice [-10, 0], tak ho v skutočnosti posunieme
	 * na pozíciu desať bodov od pravého okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepVpravo()
	{
		prilepenieRoztiahnutie &= 60;
		prilepenieRoztiahnutie |= 2;
		hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí poznámkový blok k hornému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k dolnému okraju. Každé
	 * prilepenie upravuje súradnicový systém poznámkového bloku
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená,
	 * že keď napríklad po prilepení k hornému okraju posunieme
	 * poznámkový blok na súradnice [0, -10], tak ho v skutočnosti posunieme
	 * na pozíciu desať bodov od horného okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepHore()
	{
		prilepenieRoztiahnutie &= 51;
		prilepenieRoztiahnutie |= 4;
		hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí poznámkový blok k dolnému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k hornému okraju. Každé
	 * prilepenie upravuje súradnicový systém poznámkového bloku
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená,
	 * že keď napríklad po prilepení k dolnému okraju posunieme
	 * poznámkový blok na súradnice [0, 10], tak ho v skutočnosti posunieme
	 * na pozíciu desať bodov od dolného okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #odlep()
	 */
	public void prilepDole()
	{
		prilepenieRoztiahnutie &= 51;
		prilepenieRoztiahnutie |= 8;
		hlavnýPanel.doLayout();
	}

	/**
	 * <p>Odlepí poznámkový blok od všetkých okrajov.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 */
	public void odlep()
	{
		prilepenieRoztiahnutie &= 48;
		hlavnýPanel.doLayout();
	}


	/**
	 * <p>Roztiahne poznámkový blok na celú výšku zobrazovanej plochy tak,
	 * aby bol celý viditeľný, čoho dôsledkom je aj to, že najväčšia
	 * možná výška bloku je rovná výške plátien sveta.</p>
	 * 
	 * @see #roztiahniNaŠírku()
	 * @see #nerozťahuj()
	 */
	public void roztiahniNaVýšku()
	{
		prilepenieRoztiahnutie |= 16;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaVýšku() roztiahniNaVýšku}.</p> */
	public void roztiahniNaVysku() { roztiahniNaVýšku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaVýšku() roztiahniNaVýšku}.</p> */
	public void roztiahniVertikálne() { roztiahniNaVýšku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaVýšku() roztiahniNaVýšku}.</p> */
	public void roztiahniVertikalne() { roztiahniNaVýšku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaVýšku() roztiahniNaVýšku}.</p> */
	public void roztiahniZvislo() { roztiahniNaVýšku(); }

	/**
	 * <p>Roztiahne poznámkový blok na celú šírku zobrazovanej plochy tak,
	 * aby bol celý viditeľný, čoho dôsledkom je aj to, že najväčšia
	 * možná šírka bloku je rovná šírke plátien sveta.</p>
	 * 
	 * @see #roztiahniNaVýšku()
	 * @see #nerozťahuj()
	 */
	public void roztiahniNaŠírku()
	{
		prilepenieRoztiahnutie |= 32;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaŠírku() roztiahniNaŠírku}.</p> */
	public void roztiahniNaSirku() { roztiahniNaŠírku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaŠírku() roztiahniNaŠírku}.</p> */
	public void roztiahniHorizontálne() { roztiahniNaŠírku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaŠírku() roztiahniNaŠírku}.</p> */
	public void roztiahniHorizontalne() { roztiahniNaŠírku(); }

	/** <p><a class="alias"></a> Alias pre {@link #roztiahniNaŠírku() roztiahniNaŠírku}.</p> */
	public void roztiahniVodorovne() { roztiahniNaŠírku(); }

	/**
	 * <p>Zruší roztiahnutie poznámkového bloku v oboch smeroch.</p>
	 * 
	 * @see #roztiahniNaVýšku()
	 * @see #roztiahniNaŠírku()
	 */
	public void nerozťahuj()
	{
		prilepenieRoztiahnutie &= 15;
		hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #nerozťahuj() nerozťahuj}.</p> */
	public void neroztahuj() { nerozťahuj(); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu šírku poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @return aktuálna šírka poznámkového bloku
	 * 
	 * @see #šírka(double)
	 * 
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public double šírka() { return šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
	public double sirka() { return šírka; }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu výšku poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @return aktuálna výška poznámkového bloku
	 * 
	 * @see #výška(double)
	 * 
	 * @see #šírka()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public double výška() { return výška; }

	/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
	public double vyska() { return výška; }


	/**
	 * <p><a class="setter"></a> Zmení šírku poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param nováŠírka nová šírka poznámkového bloku
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public void šírka(double nováŠírka)
	{
		double ox = polohaX();
		šírka = (int)nováŠírka;
		polohaX(ox);
	}

	/** <p><a class="alias"></a> Alias pre {@link #šírka(double) šírka}.</p> */
	public void sirka(double nováŠírka) { šírka(nováŠírka); }

	/**
	 * <p><a class="setter"></a> Zmení výšku poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param nováVýška nová výška poznámkového bloku
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public void výška(double nováVýška)
	{
		double oy = polohaY();
		výška = (int)nováVýška;
		polohaY(oy);
	}

	/** <p><a class="alias"></a> Alias pre {@link #výška(double) výška}.</p> */
	public void vyska(double nováVýška) { výška(nováVýška); }


	/**
	 * <p>Zistí aktuálne rozmery poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @return objekt vytvorený podľa aktuálnych rozmerov poznámkového bloku
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public Rozmer rozmery() { return new Rozmery(šírka, výška); }

	/**
	 * <p>Nastaví nové rozmery poznámkového bloku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param nováŠírka nová šírka poznámkového bloku
	 * @param nováVýška nová výška poznámkového bloku
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public void rozmery(double nováŠírka, double nováVýška)
	{
		double ox = polohaX();
		double oy = polohaY();
		šírka = (int)nováŠírka;
		výška = (int)nováVýška;
		polohaX(ox);
		polohaY(oy);
	}

	/**
	 * <p>Nastaví nové rozmery poznámkového bloku podľa zadanej implementácie
	 * rozmeru.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param rozmer inštancia obsahujúca nové rozmery poznámkového bloku
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public void rozmery(Rozmer rozmer)
	{
		double ox = polohaX();
		double oy = polohaY();
		šírka = (int)rozmer.šírka();
		výška = (int)rozmer.výška();
		polohaX(ox);
		polohaY(oy);
	}


	/**
	 * <p>Zistí, či má poznámkový blok zadanú šírku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param šírka šírka, ktorá má byť porovnaná so šírkou poznámkového bloku
	 * @return {@code valtrue} ak sa šírka poznámkového bloku zhoduje so
	 *     zadanou šírkou, {@code valfalse} v opačnom prípade
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public boolean máŠírku(double šírka) { return this.šírka == (int)šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #máŠírku(double) máŠírku}.</p> */
	public boolean maSirku(double šírka) { return this.šírka == (int)šírka; }


	/**
	 * <p>Zistí, či má poznámkový blok zadanú výšku.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param výška výška, ktorá má byť porovnaná s výškou poznámkového bloku
	 * @return {@code valtrue} ak sa výška poznámkového bloku zhoduje so
	 *     zadanou výškou, {@code valfalse} v opačnom prípade
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máRozmer(Rozmer)
	 * @see #máRozmer(double, double)
	 */
	public boolean máVýšku(double výška) { return this.výška == (int)výška; }

	/** <p><a class="alias"></a> Alias pre {@link #máVýšku(double) máVýšku}.</p> */
	public boolean maVysku(double výška) { return this.výška == (int)výška; }


	/**
	 * <p>Overí, či sa rozmery poznámkového bloku dokonale zhodujú so zadanými
	 * rozmermi. Ak je zistená zhoda, tak je výsledkom
	 * {@code valtrue}, v opačnom prípade hodnota {@code valfalse}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param šírka šírka porovnávaná so šírkou poznámkového bloku
	 * @param výška výška porovnávaná s výškou poznámkového bloku
	 * @return {@code valtrue} ak sa rozmery poznámkového bloku zhodujú so
	 *     zadanými rozmermi, {@code valfalse} v opačnom prípade
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(Rozmer)
	 */
	public boolean máRozmer(double šírka, double výška)
	{ return this.šírka == (int)šírka && this.výška == (int)výška; }

	/** <p><a class="alias"></a> Alias pre {@link #máRozmer(double, double) máRozmer}.</p> */
	public boolean maRozmer(double šírka, double výška)
	{ return máRozmer(šírka, výška); }


	/**
	 * <p>Overí, či sa rozmery poznámkového bloku a rozmery zadaného objektu
	 * dokonale zhodujú. Ak je zistená zhoda, tak je výsledkom
	 * {@code valtrue}, v opačnom prípade hodnota {@code valfalse}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Z dôvodu kompatibility
	 * s rozhraním {@link Rozmer Rozmer} pracujú všetky metódy súvisiace
	 * s rozmerom poznámkového bloku s údajovým typom {@code typedouble}, ale
	 * z optimalizačných (a iných) dôvodov sú rozmery bloku vnútorne
	 * uchovávané v atribútoch s údajovým typom {@code typeint}. (Z toho
	 * dôvodu, ak priradíte/zapíšete do rozmeru bloku neceločíselnú hodnotu,
	 * spätne z neho prečítate celočíselnú hodnotu získanú zanedbaním
	 * neceločíselnej časti.)</p>
	 * 
	 * @param rozmer iný objekt, ktorého rozmery majú byť porovnané
	 *     s rozmermi poznámkového bloku
	 * @return {@code valtrue} ak sa rozmery poznámkového bloku zhodujú
	 *     s rozmermi zadaného objektu, {@code valfalse} v opačnom prípade
	 * 
	 * @see #šírka()
	 * @see #výška()
	 * @see #rozmery()
	 * @see #rozmery(double, double)
	 * @see #rozmery(Rozmer)
	 * @see #máŠírku(double)
	 * @see #máVýšku(double)
	 * @see #máRozmer(double, double)
	 */
	public boolean máRozmer(Rozmer rozmer)
	{
		if (null == rozmer) return false;
		return (int)rozmer.šírka() == šírka && (int)rozmer.výška() == výška;
	}

	/** <p><a class="alias"></a> Alias pre {@link #máRozmer(Rozmer) máRozmer}.</p> */
	public boolean maRozmer(Rozmer rozmer) { return máRozmer(rozmer); }


	/**
	 * <p>Overí, či je poznámkový blok aktívny. Aktívny poznámkový blok
	 * je použiteľný na úpravy alebo kopírovanie obsahu. S deaktivovaným
	 * poznámkovým blokom nemôže používateľ manipulovať.</p>
	 * 
	 * @return {@code valtrue} – je aktívny;
	 *     {@code valfalse} – nie je aktívny
	 * 
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public boolean aktívny() { return isEnabled(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktívny() aktívny}.</p> */
	public boolean aktivny() { return isEnabled(); }

	/**
	 * <p>Aktivuje poznámkový blok. Predvolene je poznámkový blok aktívny.
	 * Ak ho {@linkplain #deaktivuj() deaktivujeme} (pozri nižšie), tak po
	 * vykonaní tohto príkazu ({@code curraktivuj}) poznámkového bloku,
	 * bude opäť použiteľný a bude reagovať na klikanie myšou aj voľbu
	 * klávesnicou.</p>
	 * 
	 * @see #aktívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void aktivuj() { setEnabled(true); }

	/**
	 * <p>Deaktivuje poznámkový blok. Poznámkový blok prestane byť schopný
	 * reagovať na ovládanie myšou alebo klávesnicou.</p>
	 * 
	 * @see #aktívny()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void deaktivuj() { setEnabled(false); }


	/**
	 * <p>Overí, či má poznámkový blok povolenú úpravu textu. Každý
	 * poznámkový blok môže mať {@linkplain #upravuj() povolené} alebo
	 * {@linkplain #neupravuj() zakázané} upravovanie textu. Ak sú
	 * úpravy zakázané, poznámkový blok slúži len na zobrazenie textu,
	 * ktorý z neho môže byť prípadne skopírovaný.</p>
	 * 
	 * @return {@code valtrue} – úprava textu je povolená;
	 *     {@code valfalse} – úprava textu je zakázaná
	 * 
	 * @see #upravuj()
	 * @see #neupravuj()
	 */
	public boolean upraviteľný() { return isEditable(); }

	/** <p><a class="alias"></a> Alias pre {@link #upraviteľný() upraviteľný}.</p> */
	public boolean upravitelny() { return isEditable(); }

	/**
	 * <p>Povolí úpravu textu v poznámkovom bloku. Poznámkový blok má
	 * predvolene povolenú úpravu textu. Môžeme ju však {@linkplain 
	 * #neupravuj() zakázať} a neskôr týmto príkazom opätovne povoliť.</p>
	 * 
	 * @see #upraviteľný()
	 * @see #upravuj()
	 * @see #neupravuj()
	 */
	public void upravuj() { setEditable(true); }

	/**
	 * <p>Zakáže úpravu textu poznámkového bloku. Po vykonaní tohto
	 * príkazu prestane byť poznámkový blok použiteľný na úpravu textu,
	 * ktorý obsahuje.</p>
	 * 
	 * @see #upraviteľný()
	 * @see #upravuj()
	 * @see #neupravuj()
	 */
	public void neupravuj() { setEditable(false); }


	/**
	 * <p>Zistí, či je poznámkový blok viditeľný (zobrazený) alebo nie. Po
	 * vytvorení je poznámkový blok predvolene viditeľný, môžeme ho skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda {@link #zobrazený()
	 * zobrazený}.</p>
	 * 
	 * @see #zobrazený()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean viditeľný() { return isVisible() && rolovanie.isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #viditeľný() viditeľný}.</p> */
	public boolean viditelny() { return viditeľný(); }

	/**
	 * <p>Zistí, či je poznámkový blok zobrazený (viditeľný) alebo nie. Po
	 * vytvorení je poznámkový blok predvolene zobrazený, môžeme ho skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda {@link #viditeľný()
	 * viditeľný}.</p>
	 * 
	 * @see #viditeľný()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean zobrazený() { return viditeľný(); }

	/** <p><a class="alias"></a> Alias pre {@link #zobrazený() zobrazený}.</p> */
	public boolean zobrazeny() { return viditeľný(); }

	/**
	 * <p>Zobrazí poznámkový blok. (Viac informácií nájdete v opise metódy
	 * {@link #zobrazený() zobrazený}.)</p>
	 * 
	 * @see #viditeľný()
	 * @see #zobrazený()
	 * @see #skry()
	 */
	public void zobraz() { setVisible(true); }

	/**
	 * <p>Skryje poznámkový blok. (Viac informácií nájdete v opise metódy
	 * {@link #zobrazený() zobrazený}.)</p>
	 * 
	 * @see #viditeľný()
	 * @see #zobrazený()
	 * @see #zobraz()
	 */
	public void skry() { setVisible(false); }

	/**
	 * <p>Prekrytie originálnej metódy. (Na zabezpečenie postúpenia vstupu
	 * klávesnice (fokusu) hlavnému panelu (pri skrytí poznámkového
	 * bloku) a tiež na synchronizované zobrazenie/skrytie všetkých súčastí
	 * poznámkového bloku – {@link JTextPane#setVisible(boolean)},
	 * {@link JScrollPane#setVisible(boolean)}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JTextPane#setVisible(boolean)}. </p>
	 * 
	 * <!-- TODO – priebežne dopĺňať rovnaké poznámky ku všetkým prekrytým
	 * metódam v programovacom rámci. -->
	 * 
	 * @param visible {@code valtrue} alebo {@code valfalse} podľa toho, či
	 *     má byť poznámkový blok zobrazený alebo skrytý
	 * @see JTextPane#setVisible(boolean)
	 */
	@Override public void setVisible(boolean visible)
	{
		if (!visible)
			hlavnýPanel.requestFocusInWindow();
		super.setVisible(visible);
		rolovanie.setVisible(visible);
	}


	/**
	 * <p><a class="setter"></a> Zakáže alebo povolí predvolenú funkciu
	 * klávesu tabulátora a vkladania znakov tabulátora pre tento poznámkový
	 * blok.
	 * Predvolene je hodnota tejto vlastnosti nastavená na {@code valfalse}
	 * a kláves tabulátora vkladá znak tabulátora do textu poznámkového bloku.
	 * Ak túto funkciu zakážeme, tak kláves tabulátora bude plniť funkciu
	 * prechodu na ďalší (prípadne so Shiftom predchádzajúci) komponent
	 * sveta a vkladanie tabulátorov bude zakázané aj inými mechanizmami.</p>
	 * 
	 * <!-- p class="remark"><b>Poznámka:</b> Nastavenie tejto vlastnosti
	 * neznamená, že tabulátor nemôže byť vložený do poznámkového bloku
	 * iným spôsobom.</p -->
	 * 
	 * @param zakážTabulátor {@code valtrue} ak má byť predvolená funkčnosť
	 *     klávesu a vkladanie znaku tabulátora zakázané; {@code valfalse}
	 *     v opačnom prípade
	 * 
	 * @see #zakážTabulátor()
	 */
	public void zakážTabulátor(boolean zakážTabulátor)
	{ this.zakážTabulátor = zakážTabulátor; }

	/**
	 * <p><a class="getter"></a> Zistí, či je zakázaná predvolená funkcia
	 * tabulátora a vkladanie znaku tabulátora pre tento poznámkový blok.</p>
	 * 
	 * @return {@code valtrue} ak je predvolená funkčnosť klávesu a vkladanie
	 *     znaku tabulátora zakázané; {@code valfalse} v opačnom prípade
	 * 
	 * @see #zakážTabulátor(boolean)
	 */
	public boolean zakážTabulátor() { return zakážTabulátor; }

	/** <p><a class="alias"></a> Alias pre {@link #zakážTabulátor(boolean) zakážTabulátor}.</p> */
	public void zakazTabulator(boolean zakážTabulátor)
	{ zakážTabulátor(zakážTabulátor); }

	/** <p><a class="alias"></a> Alias pre {@link #zakážTabulátor() zakážTabulátor}.</p> */
	public boolean zakazTabulator() { return zakážTabulátor(); }


	/**
	 * <p><a class="setter"></a> Zakáže alebo povolí predvolenú funkciu
	 * klávesu {@code Enter} a vkladania znakov nového riadka pre tento
	 * poznámkový blok.
	 * Predvolene je hodnota tejto vlastnosti nastavená na {@code valfalse},
	 * kláves {@code Enter} vkladá nový riadok do textu poznámkového bloku
	 * a vkladanie nových riadkov je povolené aj inými mechanizmami. Ak túto
	 * vlastnosť nastavíme na {@code valtrue}, tak {@code Enter} prestane
	 * túto funkciu plniť a do poznámkového bloku bude zakázané vkladanie
	 * znakov nových riadkov.
	 * Ak v takom prípade pre kláves {@code Enter} definovaná {@link 
	 * Svet#pridajKlávesovúSkratku(String, int, int) klávesová skratka sveta},
	 * tak je vykonaný príkaz prislúchajúci tejto skratke.</p>
	 * 
	 * <!-- p class="remark"><b>Poznámka:</b> Nastavenie tejto vlastnosti
	 * neznamená, že nový riadok nemôže byť vložený do poznámkového bloku
	 * iným spôsobom.</p -->
	 * 
	 * @param zakážEnter {@code valtrue} ak má byť predvolená funkčnosť
	 *     klávesu a vkladanie znakov nového riadka zakázané;
	 *     {@code valfalse} v opačnom prípade
	 * 
	 * @see #zakážEnter()
	 */
	public void zakážEnter(boolean zakážEnter)
	{ this.zakážEnter = zakážEnter; }

	/**
	 * <p><a class="getter"></a> Zistí, či je zakázaná predvolená funkcia
	 * klávesu {@code Enter} a vkladania znakov nových riadkov pre tento
	 * poznámkový blok.</p>
	 * 
	 * @return {@code valtrue} ak je predvolená funkčnosť klávesu a vkladanie
	 *     znakov nových riadkov zakázané; {@code valfalse} v opačnom prípade
	 * 
	 * @see #zakážEnter(boolean)
	 */
	public boolean zakážEnter() { return zakážEnter; }

	/** <p><a class="alias"></a> Alias pre {@link #zakážEnter(boolean) zakážEnter}.</p> */
	public void zakazEnter(boolean zakážEnter)
	{ zakážEnter(zakážEnter); }

	/** <p><a class="alias"></a> Alias pre {@link #zakážEnter() zakážEnter}.</p> */
	public boolean zakazEnter() { return zakážEnter(); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu farbu textu poznámkového
	 * bloku.</p>
	 * 
	 * @return aktuálna farba textu poznámkového bloku (objekt typu
	 *     {@link Farba Farba})
	 */
	public Farba farbaTextu()
	{
		/*
		if (getContentType().equalsIgnoreCase("text/html"))
		{
			Document dokument = getDocument();
			if (dokument instanceof DefaultStyledDocument)
			{
				DefaultStyledDocument štýlovýDokument =
					(DefaultStyledDocument)dokument;
				Element element = štýlovýDokument.
					getCharacterElement(getCaretPosition());

				AttributeSet atribúty = element.getAttributes();
				Color farba = StyleConstants.getForeground(atribúty);
				System.out.println("farba atribútu: " + farba);
				if (null == farba) return null;
				if (farba instanceof Farba) return (Farba)farba;
				return new Farba(farba);
			}
		}
		*/

		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		Color farba = StyleConstants.getForeground(atribúty);
		if (null == farba) return null;
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť textu
	 * poznámkového bloku podľa zadaného objektu.</p>
	 * 
	 * @param nováFarba objekt typu {@link Color Color} (alebo
	 *     odvodeného napr. {@link Farba Farba}) s novou farbou textu
	 *     poznámkového bloku; jestvuje paleta predvolených farieb
	 *     (pozri napr.: {@link Farebnosť#biela biela}, {@link Farebnosť#červená červená},
	 *     {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaTextu(Color nováFarba)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setForeground(upravAtribúty, nováFarba);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// // else if (atribúty instanceof MutableAttributeSet)
		// // 	upravAtribúty = (MutableAttributeSet)atribúty;
		// else
		// 	upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// // StyleConstants.setFontFamily(upravAtribúty, "SansSerif");
		// // StyleConstants.setFontSize(upravAtribúty, 16);
		// 
		// StyleConstants.setForeground(upravAtribúty, nováFarba);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/**
	 * <p>Nastaví farbu a priehľadnosť textu poznámkového bloku podľa
	 * zadaného objektu.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu textu poznámkového bloku
	 */
	public void farbaTextu(Farebnosť objekt) { farbaTextu(objekt.farba()); }

	/**
	 * <p>Nastaví farbu textu poznámkového bloku podľa zadaných farebných
	 * zložiek.</p>
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
		farbaTextu(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť textu poznámkového bloku podľa
	 * zadaných farebných zložiek a úrovne priehľadnosti.</p>
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
		farbaTextu(farba);
		return farba;
	}


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu farbu pozadia textu
	 * poznámkového bloku.</p>
	 * 
	 * @return aktuálna farba pozadia textu poznámkového bloku (objekt typu
	 *     {@link Farba Farba})
	 */
	public Farba farbaPozadiaTextu()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		Color farba = StyleConstants.getBackground(atribúty);
		if (null == farba) return null;
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť pozadia
	 * textu poznámkového bloku podľa zadaného objektu.</p>
	 * 
	 * @param nováFarba objekt typu {@link Color Color} (alebo
	 *     odvodeného napr. {@link Farba Farba}) s novou farbou pozadia
	 *     textu poznámkového bloku; jestvuje paleta predvolených farieb
	 *     (pozri napr.: {@link Farebnosť#biela biela}, {@link Farebnosť#červená červená},
	 *     {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaPozadiaTextu(Color nováFarba)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setBackground(upravAtribúty, nováFarba);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// else
		// 	upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// StyleConstants.setBackground(upravAtribúty, nováFarba);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/**
	 * <p>Nastaví farbu a priehľadnosť pozadia textu poznámkového bloku
	 * podľa zadaného objektu.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu pozadia textu poznámkového
	 *     bloku
	 */
	public void farbaPozadiaTextu(Farebnosť objekt)
	{ farbaPozadiaTextu(objekt.farba()); }

	/**
	 * <p>Nastaví farbu pozadia textu poznámkového bloku podľa zadaných
	 * farebných zložiek.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia textu
	 * 
	 * @see #farbaPozadiaTextu(Color)
	 */
	public Farba farbaPozadiaTextu(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		farbaPozadiaTextu(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť pozadia textu poznámkového
	 * bloku podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia textu
	 * 
	 * @see #farbaPozadiaTextu(Color)
	 */
	public Farba farbaPozadiaTextu(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaPozadiaTextu(farba);
		return farba;
	}


	/**
	 * <p><a class="getter"></a> Číta farbu označenia (označeného textu)
	 * poznámkového bloku.</p>
	 * 
	 * @return aktuálna farba textu označenia poznámkového bloku (objekt
	 *     typu {@link Farba Farba})
	 */
	public Farba farbaTextuOznačenia()
	{
		Color farba = getSelectedTextColor();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť bloku
	 * označenia poznámkového bloku podľa zadanej farebnej inštancie.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu textu označenia;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaTextuOznačenia(Color nováFarba)
	{ setSelectedTextColor(nováFarba); }

	/**
	 * <p>Nastaví podľa zadaného objektu farbu a priehľadnosť bloku
	 * označenia poznámkového bloku.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu textu označenia
	 */
	public void farbaTextuOznačenia(Farebnosť objekt)
	{ farbaTextuOznačenia(objekt.farba()); }

	/**
	 * <p>Nastaví podľa zadaných farebných zložiek farbu bloku
	 * označenia poznámkového bloku.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba textu označenia
	 * 
	 * @see #farbaTextuOznačenia(Color)
	 */
	public Farba farbaTextuOznačenia(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		farbaTextuOznačenia(farba);
		return farba;
	}

	/**
	 * <p>Nastaví podľa zadaných farebných zložiek a úrovne priehľadnosti
	 * farbu a (ne)priehľadnosť bloku označenia poznámkového bloku.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba textu označenia
	 * 
	 * @see #farbaTextuOznačenia(Color)
	 */
	public Farba farbaTextuOznačenia(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaTextuOznačenia(farba);
		return farba;
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačenia() farbaTextuOznačenia}.</p> */
	public Farba farbaTextuOznacenia()
	{ return farbaTextuOznačenia(); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačenia(Color) farbaTextuOznačenia}.</p> */
	public void farbaTextuOznacenia(Color nováFarba)
	{ farbaTextuOznačenia(nováFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačenia(Farebnosť) farbaTextuOznačenia}.</p> */
	public void farbaTextuOznacenia(Farebnosť objekt)
	{ farbaTextuOznačenia(objekt); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačenia(int, int, int) farbaTextuOznačenia}.</p> */
	public Farba farbaTextuOznacenia(int r, int g, int b)
	{ return farbaTextuOznačenia(r, g, b); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačenia(int, int, int, int) farbaTextuOznačenia}.</p> */
	public Farba farbaTextuOznacenia(int r, int g, int b, int a)
	{ return farbaTextuOznačenia(r, g, b, a); }


	/**
	 * <p><a class="getter"></a> Číta farbu pozadia označenia (označeného
	 * textu) poznámkového bloku.</p>
	 * 
	 * @return aktuálna farba pozadia označenia poznámkového bloku (objekt
	 *     typu {@link Farba Farba})
	 */
	public Farba farbaPozadiaOznačenia()
	{
		Color farba = getSelectionColor();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť pozadia
	 * označenia (označeného textu) poznámkového bloku podľa zadanej
	 * farebnej inštancie.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu pozadia označenia;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaPozadiaOznačenia(Color nováFarba)
	{ setSelectionColor(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť pozadia označenia (označeného textu)
	 * poznámkového bloku podľa zadaného objektu.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu pozadia označenia
	 */
	public void farbaPozadiaOznačenia(Farebnosť objekt)
	{ farbaPozadiaOznačenia(objekt.farba()); }

	/**
	 * <p>Nastaví farbu pozadia označenia (označeného textu) poznámkového
	 * bloku podľa zadaných farebných zložiek.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia
	 *     označenia
	 * 
	 * @see #farbaPozadiaOznačenia(Color)
	 */
	public Farba farbaPozadiaOznačenia(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		farbaPozadiaOznačenia(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť pozadia označenia (označeného
	 * textu) poznámkového bloku podľa zadaných farebných zložiek a úrovne
	 * priehľadnosti.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia
	 *     označenia
	 * 
	 * @see #farbaPozadiaOznačenia(Color)
	 */
	public Farba farbaPozadiaOznačenia(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaPozadiaOznačenia(farba);
		return farba;
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačenia() farbaPozadiaOznačenia}.</p> */
	public Farba farbaPozadiaOznacenia()
	{ return farbaPozadiaOznačenia(); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačenia(Color) farbaPozadiaOznačenia}.</p> */
	public void farbaPozadiaOznacenia(Color nováFarba)
	{ farbaPozadiaOznačenia(nováFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačenia(Farebnosť) farbaPozadiaOznačenia}.</p> */
	public void farbaPozadiaOznacenia(Farebnosť objekt)
	{ farbaPozadiaOznačenia(objekt); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačenia(int, int, int) farbaPozadiaOznačenia}.</p> */
	public Farba farbaPozadiaOznacenia(int r, int g, int b)
	{ return farbaPozadiaOznačenia(r, g, b); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačenia(int, int, int, int) farbaPozadiaOznačenia}.</p> */
	public Farba farbaPozadiaOznacenia(int r, int g, int b, int a)
	{ return farbaPozadiaOznačenia(r, g, b, a); }


	/**
	 * <p><a class="getter"></a> Číta farbu pozadia poznámkového bloku.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @return aktuálna farba pozadia poznámkového bloku (objekt typu
	 *     {@link Farba Farba})
	 */
	public Farba farbaPozadia()
	{
		Color farba = getBackground();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť pozadia
	 * poznámkového bloku podľa zadanej farebnej inštancie.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu pozadia;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna
	 *     čierna}…)
	 */
	public void farbaPozadia(Color nováFarba)
	{
		setOpaque(0 != nováFarba.getAlpha());
		setBackground(nováFarba);
	}

	/**
	 * <p>Nastaví farbu a priehľadnosť pozadia poznámkového bloku podľa
	 * zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu pozadia
	 */
	public void farbaPozadia(Farebnosť objekt)
	{ farbaPozadia(objekt.farba()); }

	/**
	 * <p>Nastaví farbu pozadia poznámkového bloku podľa zadaných
	 * farebných zložiek.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
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
		farbaPozadia(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť pozadia poznámkového bloku
	 * podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
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
	 * @return objekt typu {@link Farba Farba} – nová farba pozadia
	 * 
	 * @see #farbaPozadia(Color)
	 */
	public Farba farbaPozadia(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaPozadia(farba);
		return farba;
	}


	/**
	 * <p><a class="getter"></a> Číta farbu kurzora poznámkového bloku.</p>
	 * 
	 * @return aktuálna farba kurzora poznámkového bloku (objekt typu
	 *     {@link Farba Farba})
	 */
	public Farba farbaKurzora()
	{
		Color farba = getCaretColor();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť kurzora
	 * poznámkového bloku podľa zadanej farebnej inštancie.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu kurzora;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaKurzora(Color nováFarba)
	{ setCaretColor(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť kurzora poznámkového bloku podľa
	 * zadaného objektu.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu kurzora
	 */
	public void farbaKurzora(Farebnosť objekt)
	{ farbaKurzora(objekt.farba()); }

	/**
	 * <p>Nastaví farbu kurzora poznámkového bloku podľa zadaných
	 * farebných zložiek.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba kurzora
	 * 
	 * @see #farbaKurzora(Color)
	 */
	public Farba farbaKurzora(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		farbaKurzora(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť kurzora poznámkového bloku
	 * podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba kurzora
	 * 
	 * @see #farbaKurzora(Color)
	 */
	public Farba farbaKurzora(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaKurzora(farba);
		return farba;
	}


	/**
	 * <p><a class="getter"></a> Číta farbu neaktívneho textu poznámkového
	 * bloku. Neaktívny text je všetok text {@linkplain #deaktivuj()
	 * deaktivovaného} poznámkového bloku.</p>
	 * 
	 * @return aktuálna farba neaktívneho textu poznámkového bloku (objekt
	 *     typu {@link Farba Farba})
	 */
	public Farba farbaNeaktívnehoTextu()
	{
		Color farba = getDisabledTextColor();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť neaktívneho
	 * textu poznámkového bloku podľa zadanej farebnej inštancie.
	 * (Neaktívny text je všetok text {@linkplain #deaktivuj()
	 * deaktivovaného} poznámkového bloku.)</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu neaktívneho textu;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaNeaktívnehoTextu(Color nováFarba)
	{ setDisabledTextColor(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť neaktívneho textu poznámkového
	 * bloku podľa zadaného objektu.
	 * (Neaktívny text je všetok text {@linkplain #deaktivuj()
	 * deaktivovaného} poznámkového bloku.)</p>
	 * 
	 * @param objekt objekt určujúci novú farbu neaktívneho textu
	 */
	public void farbaNeaktívnehoTextu(Farebnosť objekt)
	{ farbaNeaktívnehoTextu(objekt.farba()); }

	/**
	 * <p>Nastaví farbu neaktívneho textu poznámkového bloku podľa
	 * zadaných farebných zložiek.
	 * (Neaktívny text je všetok text {@linkplain #deaktivuj()
	 * deaktivovaného} poznámkového bloku.)</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba neaktívneho
	 *     textu
	 * 
	 * @see #farbaNeaktívnehoTextu(Color)
	 */
	public Farba farbaNeaktívnehoTextu(int r, int g, int b)
	{
		Farba farba = new Farba(r, g, b);
		farbaNeaktívnehoTextu(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť neaktívneho textu poznámkového
	 * bloku podľa zadaných farebných zložiek a úrovne priehľadnosti.
	 * (Neaktívny text je všetok text {@linkplain #deaktivuj()
	 * deaktivovaného} poznámkového bloku.)</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
	 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
	 *     farba)
	 * @return objekt typu {@link Farba Farba} – nová farba neaktívneho
	 *     textu
	 * 
	 * @see #farbaNeaktívnehoTextu(Color)
	 */
	public Farba farbaNeaktívnehoTextu(int r, int g, int b, int a)
	{
		Farba farba = new Farba(r, g, b, a);
		farbaNeaktívnehoTextu(farba);
		return farba;
	}

	/** <p><a class="alias"></a> Alias pre {@link #farbaNeaktívnehoTextu() farbaNeaktívnehoTextu}.</p> */
	public Farba farbaNeaktivnehoTextu()
	{ return farbaNeaktívnehoTextu(); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNeaktívnehoTextu(Color) farbaNeaktívnehoTextu}.</p> */
	public void farbaNeaktivnehoTextu(Color nováFarba)
	{ farbaNeaktívnehoTextu(nováFarba); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNeaktívnehoTextu(Farebnosť) farbaNeaktívnehoTextu}.</p> */
	public void farbaNeaktivnehoTextu(Farebnosť objekt)
	{ farbaNeaktívnehoTextu(objekt); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNeaktívnehoTextu(int, int, int) farbaNeaktívnehoTextu}.</p> */
	public Farba farbaNeaktivnehoTextu(int r, int g, int b)
	{ return farbaNeaktívnehoTextu(r, g, b); }

	/** <p><a class="alias"></a> Alias pre {@link #farbaNeaktívnehoTextu(int, int, int, int) farbaNeaktívnehoTextu}.</p> */
	public Farba farbaNeaktivnehoTextu(int r, int g, int b, int a)
	{ return farbaNeaktívnehoTextu(r, g, b, a); }


	// ‼Pozor‼ Pre tento komponent nefunguje…
		// /**
		//  * Nastaví zdedenú farbu pozadia poznámkového bloku.
		//  * 
		//  * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
		//  * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
		//  * rozhrania (L&F). Každá definícia vzhľadu komponentov môže
		//  * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
		//  */
		// public void zdedenáFarbaPozadia() { farbaPozadia((Color)null); }
		// 
		// ‼Pozor‼ Pre tento komponent nefunguje…
		// /** <p><a class="alias"></a> Alias pre {@link #zdedenáFarbaPozadia() zdedenáFarbaPozadia}.</p> */
		// public void zdedenaFarbaPozadia() { farbaPozadia((Color)null); }
		// ‼Pozor‼ Pre tento komponent nefunguje…


	/**
	 * <p><a class="getter"></a> Zistí názov písma textu na aktuálnej
	 * pozícii kurzora v poznámkovom bloku.</p>
	 * 
	 * @return aktuálny názov písma textu poznámkového bloku
	 */
	public String názovPísma()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		return StyleConstants.getFontFamily(atribúty);
	}

	/** <p><a class="alias"></a> Alias pre {@link #názovPísma() názovPísma}.</p> */
	public String nazovPisma() { return názovPísma(); }

	/**
	 * <p><a class="setter"></a> Nastaví názov písma označeného textu
	 * alebo textu, ktorý má byť vkladaný na aktuálnu pozíciu
	 * poznámkového bloku.</p>
	 * 
	 * @param názovPísma reťazec s novým názvom písma
	 */
	public void názovPísma(String názovPísma)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setFontFamily(upravAtribúty, názovPísma);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// else upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// StyleConstants.setFontFamily(upravAtribúty, názovPísma);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/** <p><a class="alias"></a> Alias pre {@link #názovPísma(String) názovPísma}.</p> */
	public void nazovPisma(String názovPísma) { názovPísma(názovPísma); }


	/**
	 * <p><a class="getter"></a> Zistí veľkosť písma textu na aktuálnej
	 * pozícii kurzora v poznámkovom bloku.</p>
	 * 
	 * @return veľkosť písma
	 */
	public int veľkosťPísma()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		return StyleConstants.getFontSize(atribúty);
	}

	/** <p><a class="alias"></a> Alias pre {@link #veľkosťPísma() veľkosťPísma}.</p> */
	public int velkostPisma() { return veľkosťPísma(); }

	/**
	 * <p><a class="setter"></a> Nastaví veľkosť písma označeného textu
	 * alebo textu, ktorý má byť vkladaný na aktuálnu pozíciu
	 * poznámkového bloku.</p>
	 * 
	 * @param veľkosťPísma nová veľkosť písma
	 */
	public void veľkosťPísma(int veľkosťPísma)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setFontSize(upravAtribúty, veľkosťPísma);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// else upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// StyleConstants.setFontSize(upravAtribúty, veľkosťPísma);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/** <p><a class="alias"></a> Alias pre {@link #veľkosťPísma(int) veľkosťPísma}.</p> */
	public void velkostPisma(int veľkosťPísma) { veľkosťPísma(veľkosťPísma); }


	/**
	 * <p><a class="getter"></a> Zistí či je písmo na aktuálnej
	 * pozícii kurzora tučné.</p>
	 * 
	 * @return príznak tučného písma
	 */
	public boolean tučné()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		return StyleConstants.isBold(atribúty);
	}

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean tucne() { return tučné(); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean silné() { return tučné(); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean silne() { return tučné(); }

	/**
	 * <p><a class="setter"></a> Nastaví tučné písmo označeného textu
	 * alebo textu, ktorý má byť vkladaný na aktuálnu pozíciu
	 * poznámkového bloku.</p>
	 * 
	 * @param tučné príznak tučného písma
	 */
	public void tučné(boolean tučné)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setBold(upravAtribúty, tučné);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// else upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// StyleConstants.setBold(upravAtribúty, tučné);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/** <p><a class="alias"></a> Alias pre {@link #tučné(boolean) tučné}.</p> */
	public void tucne(boolean tučné) { tučné(tučné); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné(boolean) tučné}.</p> */
	public void silné(boolean tučné) { tučné(tučné); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné(boolean) tučné}.</p> */
	public void silne(boolean tučné) { tučné(tučné); }


	/**
	 * <p><a class="getter"></a> Zistí či je písmo na aktuálnej
	 * pozícii kurzora v poznámkovom bloku písané kurzívou.</p>
	 * 
	 * @return stav kurzívy
	 */
	public boolean kurzíva()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		return StyleConstants.isItalic(atribúty);
	}

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean kurziva() { return kurzíva(); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean šikmé() { return kurzíva(); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean sikme() { return kurzíva(); }

	/**
	 * <p><a class="setter"></a> Nastaví kurzívu písmu označeného textu
	 * alebo textu, ktorý má byť vkladaný na aktuálnu pozíciu
	 * poznámkového bloku.</p>
	 * 
	 * @param kurzíva stav kurzívy
	 */
	public void kurzíva(boolean kurzíva)
	{
		// if (getContentType().equalsIgnoreCase("text/html"))
		// {
			MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
			StyleConstants.setItalic(upravAtribúty, kurzíva);
			setCharacterAttributes(upravAtribúty, false);
		// 	return;
		// }
		// 
		// AttributeSet atribúty = getCharacterAttributes();
		// MutableAttributeSet upravAtribúty;
		// 
		// if (null == atribúty) upravAtribúty = new SimpleAttributeSet();
		// else upravAtribúty = new SimpleAttributeSet(atribúty);
		// 
		// StyleConstants.setItalic(upravAtribúty, kurzíva);
		// setCharacterAttributes(upravAtribúty, true);
	}

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva(boolean) kurzíva}.</p> */
	public void kurziva(boolean kurzíva) { kurzíva(kurzíva); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva(boolean) kurzíva}.</p> */
	public void šikmé(boolean kurzíva) { kurzíva(kurzíva); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva(boolean) kurzíva}.</p> */
	public void sikme(boolean kurzíva) { kurzíva(kurzíva); }


	// ### Nahradené skupinou metód: ###
		// 
		//     názovPísma() veľkosťPísma() tučné() kurzíva()
		// 
		// /**
		//  * <p><a class="getter"></a> Číta aktuálny typ písma textu poznámkového
		//  * bloku.
		//  * 
		//  * @return objekt typu {@link Písmo} – aktuálne písmo
		//  * @see #písmo(Font)
		//  * @see #písmo(String, double)
		//  * @see #farbaTextu(Color)
		//  * @see #farbaPozadia(Color)
		//  */
		// public Písmo písmo()
		// {
		// 	………
		// 	………
		// 	return new Písmo(………);
		// }
		// 
		// /** <p><a class="alias"></a> Alias pre {@link #písmo() písmo}.</p> */
		// public Pismo pismo() { return písmo(); }
		// 
		// /**
		//  * <p><a class="setter"></a> Nastaví nový typ písma textu poznámkového
		//  * bloku.
		//  * 
		//  * @param novéPísmo objekt typu {@link Písmo} alebo {@link Font}
		//  *      určujúci nový typ písma
		//  * @see #písmo()
		//  * @see #písmo(String, double)
		//  * @see #farbaTextu(Color)
		//  * @see #farbaPozadia(Color)
		//  */
		// public void písmo(Font novéPísmo)
		// {
		// 	if (novéPísmo.isBold())
		// 		………
		// }
		// 
		// /** <p><a class="alias"></a> Alias pre {@link #písmo(Font) písmo}.</p> */
		// public void pismo(Font novéPísmo) { písmo(novéPísmo); }
		// 
		// /**
		//  * Nastaví nový typ písma textu poznámkového bloku. (Nová inštancia
		//  * triedy {@link Písmo Písmo} je touto metódou vrátená na prípadné
		//  * ďalšie použitie.)
		//  * 
		//  * @param názov názov písma; môže byť všeobecný názov logického
		//  *      písma (Dialog, DialogInput, Monospaced, Serif, SansSerif…)
		//  *      alebo názov konkrétneho písma (Times New Roman, Arial…)
		//  * @param veľkosť veľkosť písma v bodoch (hodnota je zaokrúhlená
		//  *      na typ {@code typefloat})
		//  * @return nový objekt typu {@link Písmo}
		//  * @see #písmo()
		//  * @see #písmo(Font)
		//  * @see #farbaTextu(Color)
		//  * @see #farbaPozadia(Color)
		//  */
		// public Písmo písmo(String názov, double veľkosť)
		// {
		// 	// Písmo vytvárame len z dôvodu kompatibility…
		// 	Písmo písmo = new Písmo(názov, Písmo.PLAIN, veľkosť);
		// 	názovPísma(názov);
		// 	veľkosťPísma(veľkosť);
		// 	tučné(false);
		// 	kurzíva(false);
		// 	return písmo;
		// }
		// 
		// /** <p><a class="alias"></a> Alias pre {@link #písmo(String, double) písmo}.</p> */
		// public Pismo pismo(String názov, double veľkosť)
		// { return new Pismo(písmo(názov, veľkosť)); }


	/**
	 * <p><a class="getter"></a> Zistí, aké je zarovnanie textu na aktuálnej
	 * pozícii kurzora v poznámkovom bloku.</p>
	 * 
	 * @return stav zarovnania; na porovnanie môžete použiť konštanty
	 *     {@link #ZAROVNAŤ_NA_STRED ZAROVNAŤ_NA_STRED},
	 *     {@link #ZAROVNAŤ_PODĽA_OKRAJOV ZAROVNAŤ_PODĽA_OKRAJOV},
	 *     {@link #ZAROVNAŤ_DOĽAVA ZAROVNAŤ_DOĽAVA}
	 *     a {@link #ZAROVNAŤ_DOPRAVA ZAROVNAŤ_DOPRAVA}
	 */
	public int zarovnať()
	{
		// AttributeSet atribúty = getCharacterAttributes();
		AttributeSet atribúty = getInputAttributes();
		return StyleConstants.getAlignment(atribúty);
	}

	/** <p><a class="alias"></a> Alias pre {@link #zarovnať() zarovnať}.</p> */
	public int zarovnat() { return zarovnať(); }


	/**
	 * <p><a class="setter"></a> Nastaví zarovnanie označeného textu
	 * alebo textu, ktorý má byť vkladaný na aktuálnu pozíciu
	 * poznámkového bloku.</p>
	 * 
	 * @param zarovnať stav zarovnania; môžete použiť konštanty
	 *     {@link #ZAROVNAŤ_NA_STRED ZAROVNAŤ_NA_STRED},
	 *     {@link #ZAROVNAŤ_PODĽA_OKRAJOV ZAROVNAŤ_PODĽA_OKRAJOV},
	 *     {@link #ZAROVNAŤ_DOĽAVA ZAROVNAŤ_DOĽAVA}
	 *     a {@link #ZAROVNAŤ_DOPRAVA ZAROVNAŤ_DOPRAVA}
	 */
	public void zarovnať(int zarovnať) // , int začiatok, int koniec)
	{
		MutableAttributeSet upravAtribúty = new SimpleAttributeSet();
		StyleConstants.setAlignment(upravAtribúty, zarovnať);

		// int dĺžka = 0;
			// 
			// Document dokument = getDocument();
			// if (null != dokument) try { dĺžka = dokument.getLength(); }
			// catch (BadLocationException bad)
			// { GRobotException.vypíšChybovéHlásenia(bad); }
			// 
			// if (začiatok < 0) začiatok += 1 + dĺžka;
			// if (začiatok < 0) začiatok = 0;
			// if (koniec < 0) koniec += 1 + dĺžka;
			// if (koniec < 0) koniec = 0;
			// 
			// dokument.setParagraphAttributes(
			// 	začiatok, koniec, upravAtribúty, false);

		setParagraphAttributes(upravAtribúty, false);
	}

	/** <p><a class="alias"></a> Alias pre {@link #zarovnať(int) zarovnať}.</p> */
	public void zarovnat(int zarovnať) { zarovnať(zarovnať); }


	/**
	 * <p><a class="getter"></a> Vráti text dokumentu vo forme čistého
	 * textu. Ak je typ dokumentu <code>text/plain</code> alebo
	 * <code>text/html</code>, tak vráti aktuálny obsah poznámkového
	 * bloku ako čistý text, inak vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @return čistý text poznámkového bloku alebo {@code valnull}
	 * 
	 * @see #text(String)
	 * @see #označenýText()
	 * @see #polohaKurzora(int)
	 */
	public String text()
	{
		if (getContentType().equalsIgnoreCase("text/plain"))
			return getText();

		if (getContentType().equalsIgnoreCase("text/html"))
		{
			Document dokument = getDocument();
			if (null != dokument)
			try
			{
				return dokument.getText(0, dokument.getLength());
			}
			catch (BadLocationException bad)
			{
				GRobotException.vypíšChybovéHlásenia(bad);
			}
		}

		return null;
	}

	/**
	 * <p><a class="setter"></a> Nastaví nový čistý text poznámkového
	 * bloku. Táto metóda nastaví typ obsahu na <code>text/plain</code>,
	 * čo je predvolený typ obsahu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @param text nový čistý text poznámkového bloku
	 * 
	 * @see #PoznámkovýBlok(String)
	 * @see #PoznámkovýBlok(String, boolean)
	 * @see #text()
	 */
	public void text(String text)
	{
		setContentType("text/plain");
		setText(text);
	}


	/**
	 * <p><a class="getter"></a> Ak je typ dokumentu <code>text/html</code>,
	 * tak vráti aktuálny HTML obsah poznámkového bloku (vo forme čistého
	 * textu obsahujúceho HTML syntax), inak vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @return HTML text poznámkového bloku alebo {@code valnull}
	 * 
	 * @see #html(String)
	 */
	public String html()
	{
		if (!getContentType().equalsIgnoreCase("text/html")) return null;
		return getText();
	}

	/**
	 * <p><a class="setter"></a> Nastaví nový HTML obsah poznámkového
	 * bloku. Metóda očakáva obsah vo forme čistého textu, ktorý obsahuje
	 * HTML syntax, ale prijatý obsah nijako neoveruje, ani nemodifikuje.
	 * Táto metóda nastaví typ obsahu na <code>text/html</code>,
	 * ktorý je automaticky nastavený konštruktorom v prípade, že sa
	 * ním nastavovaný text začína značkou <code>&lt;html&gt;</code>.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Typ dokumentu je možné overiť
	 * zdedenou metódou {@link #getContentType() getContentType()},
	 * ktorá vráti typ v reťazcovej podobe. Príklady: {@code 
	 * srg"text/plain"}, {@code srg"text/html"}.</p>
	 * 
	 * @param text nový HTML obsah poznámkového bloku
	 * 
	 * @see #PoznámkovýBlok(String)
	 * @see #PoznámkovýBlok(String, boolean)
	 * @see #html()
	 */
	public void html(String html)
	{
		setContentType("text/html");
		setText(html);
	}


	/**
	 * <p>Zistí a vráti aktuálnu polohu textového kurzora (angl.
	 * caret).</p>
	 * 
	 * @return aktuálna poloha textového kurzora
	 * 
	 * @see #polohaKurzora()
	 * @see #polohaKurzora(int)
	 * @see #presuňKurzor(int)
	 * @see #označenýText()
	 */
	public int polohaKurzora() { return getCaretPosition(); }

	/**
	 * <p>Prekrytie originálnej metódy, aby boli filtrované nekorektné
	 * hodnoty pozície kurzora a aby metóda nevrhala výnimky. Slovenská
	 * verzia tejto metódy sa volá {@link #polohaKurzora(int)
	 * polohaKurzora}.</p>
	 * 
	 * <p>Nastaví novú polohu kurzora (angl. caret). Majte na pamäti,
	 * že aktuálnu polohu kurzora ovplyvňujú aj zmeny v dokumente,
	 * takže sa môže zmeniť, ak sa mení objem textu pred kurzorom.
	 * Ak je hodnota parametra {@code nováPozícia} záporná tak, táto
	 * prekrytá verzia metódy z nej vypočíta platnú pozíciu od konca
	 * textu, pričom hodnota −1 ukazuje na koniec textu. Tiež, ak
	 * hodnota parametra {@code nováPozícia} prekračuje objem textu,
	 * tak je automaticky zaokrúhlená tak, aby ukazovala na koniec
	 * textu. Vďaka týmto opatreniam by metóda nemala vrhať výnimky.
	 * Ak je dokument poznámkového bloku prázdny ({@code valnull}),
	 * tak sa nestane nič.</p>
	 * 
	 * <p>Na presunutie kurzora na novú pozíciu a súčasné vytvorenie
	 * označenia slúži metóda {@link #moveCaretPosition(int)
	 * moveCaretPosition}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JTextPane#setCaretPosition(int)}. </p>
	 * 
	 * @param nováPozícia nová pozícia textového kurzora
	 * 
	 * @see #moveCaretPosition(int)
	 * @see #polohaKurzora()
	 * @see #polohaKurzora(int)
	 * @see #presuňKurzor(int)
	 * @see #označenýText()
	 */
	@Override public void setCaretPosition(int nováPozícia)
	{
		Document dokument = getDocument();
		int dĺžka = (null == dokument) ? 0 : dokument.getLength();

		// System.out.println("DEBUG —setCaretPosition—");
		// System.out.println("  dĺžka: " + dĺžka);
		// System.out.println("  pozícia: " + nováPozícia);

		if (nováPozícia < 0)
		{
			nováPozícia += 1 + dĺžka;
			if (nováPozícia < 0)
				nováPozícia = 0;
		}

		if (nováPozícia > dĺžka)
			nováPozícia = dĺžka;

		// System.out.println("  prepočet: " + nováPozícia);

		super.setCaretPosition(nováPozícia);

		// System.out.println("  úprava: " + getCaretPosition());
	}

	/**
	 * <p>Nastaví novú polohu kurzora (angl. caret). Táto metóda
	 * využíva prekrytú metódu {@link #setCaretPosition(int)
	 * setCaretPosition}, takže pre ňu platia tie isté informácie,
	 * ktoré sú uvedené v opise prekrytej metódy.</p>
	 * 
	 * @param nováPozícia nová pozícia textového kurzora
	 * 
	 * @see #polohaKurzora()
	 * @see #presuňKurzor(int)
	 * @see #setCaretPosition(int)
	 * @see #označenýText()
	 */
	public void polohaKurzora(int nováPozícia) { setCaretPosition(nováPozícia); }

	/**
	 * <p>Prekrytie originálnej metódy, aby boli filtrované nekorektné
	 * hodnoty pozície kurzora a aby metóda nevrhala výnimky. Slovenská
	 * verzia tejto metódy sa volá {@link #presuňKurzor(int)
	 * presuňKurzor}.</p>
	 * 
	 * <p>Presunie kurzor (angl. caret) na novú pozíciu a vytvorí pri
	 * tom označenie. Ak je hodnota parametra {@code nováPozícia}
	 * záporná, tak táto prekrytá verzia metódy z nej vypočíta platnú
	 * pozíciu od konca textu, pričom hodnota −1 ukazuje na koniec
	 * textu. Tiež, ak hodnota parametra {@code nováPozícia} prekračuje
	 * objem textu, tak je automaticky zaokrúhlená tak, aby ukazovala
	 * na koniec textu. Vďaka týmto opatreniam by metóda nemala vrhať
	 * výnimky. Ak je dokument poznámkového bloku prázdny
	 * ({@code valnull}), tak sa nestane nič.</p>
	 * 
	 * <p>Na nastavenie novej polohy kurzora a zrušenie označenia
	 * slúži metóda {@link #setCaretPosition(int) setCaretPosition}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JTextPane#moveCaretPosition(int)}. </p>
	 * 
	 * @param nováPozícia nová pozícia textového kurzora
	 * 
	 * @see #setCaretPosition(int)
	 * @see #polohaKurzora()
	 * @see #polohaKurzora(int)
	 * @see #presuňKurzor(int)
	 * @see #označenýText()
	 */
	@Override public void moveCaretPosition(int nováPozícia)
	{
		Document dokument = getDocument();
		int dĺžka = (null == dokument) ? 0 : dokument.getLength();

		// System.out.println("DEBUG —moveCaretPosition—");
		// System.out.println("  dĺžka: " + dĺžka);
		// System.out.println("  pozícia: " + nováPozícia);

		if (nováPozícia < 0)
		{
			nováPozícia += 1 + dĺžka;
			if (nováPozícia < 0)
				nováPozícia = 0;
		}

		if (nováPozícia > dĺžka)
			nováPozícia = dĺžka;

		// System.out.println("  prepočet: " + nováPozícia);

		super.moveCaretPosition(nováPozícia);

		// System.out.println("  úprava: " + getCaretPosition());
	}

	/**
	 * <p>Presunie kurzor (angl. caret) na novú pozíciu a vytvorí pri
	 * tom označenie. Táto metóda využíva prekrytú metódu
	 * {@link #moveCaretPosition(int) moveCaretPosition}, takže pre
	 * ňu platia tie isté informácie, ktoré sú uvedené v opise
	 * prekrytej metódy.</p>
	 * 
	 * @param nováPozícia nová pozícia textového kurzora
	 * 
	 * @see #polohaKurzora()
	 * @see #polohaKurzora(int)
	 * @see #moveCaretPosition(int)
	 * @see #označenýText()
	 */
	public void presuňKurzor(int nováPozícia) { moveCaretPosition(nováPozícia); }

	/** <p><a class="alias"></a> Alias pre {@link #presuňKurzor(int) presuňKurzor}.</p> */
	public void presunKurzor(int nováPozícia) { moveCaretPosition(nováPozícia); }


	/**
	 * <p>Vráti označený text dokumentu. Metóda vracia čistý text.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Na rozdiel od zdedenej metódy
	 * {@link #getSelectedText() getSelectedText()} nevracia táto metóda
	 * <small>(v prípade neúspechu alebo neočakávaného stavu)</small>
	 * hodnotu {@code valnull}, ale prázdny reťazec {@code srg""}.</p>
	 * 
	 * @return čistý text označenej časti dokumentu poznámkového bloku
	 * 
	 * @see #text()
	 * @see #text(String)
	 * @see #nahraďOznačenie(String)
	 * @see #nahraďOznačenie(String, boolean)
	 * @see #polohaKurzora(int)
	 */
	public String označenýText()
	{
		String text = null;
		try
		{
			text = getSelectedText();
		}
		catch (IllegalArgumentException e)
		{
			text = "";
		}
		if (null == text) text = "";
		return text;
	}

	/** <p><a class="alias"></a> Alias pre {@link #označenýText() označenýText}.</p> */
	public String oznacenyText() { return označenýText(); }

	/**
	 * <p>Nahradí aktuálne označený obsah zadaným obsahom (reťazcom).
	 * Táto metóda sa správa rovnako, ako keby sme volali metódu:
	 * {@link #nahraďOznačenie(String, boolean)
	 * nahraďOznačenie}{@code (obsah, }{@code valfalse}{@code );}.
	 * <small>(Ďalšie podrobnosti nájdete v jej opise.)</small></p>
	 * 
	 * @param obsah obsah, ktorým má byť nahradené aktuálne označenie
	 * 
	 * @see #označenýText()
	 * @see #nahraďOznačenie(String, boolean)
	 * @see #polohaKurzora(int)
	 */
	public void nahraďOznačenie(String obsah)
	{ nahraďOznačenie(obsah, false); }

	/** <p><a class="alias"></a> Alias pre {@link #nahraďOznačenie(String) nahraďOznačenie}.</p> */
	public void nahradOznacenie(String obsah)
	{ nahraďOznačenie(obsah, false); }

	/**
	 * <p>Nahradí aktuálne označený obsah zadaným obsahom (reťazcom).
	 * Ak nie je označený žiadny text, zadaný reťazec bude vložený
	 * na aktuálnu pozíciu v poznámkovom bloku. Ak je zadaný reťazec
	 * prázdny, tak text aktuálneho označenia (ak jestvuje) bude vymazaný.
	 * Vkladaný text bude mať také atribúty textu, ktoré sú platné
	 * v mieste vkladania. Ak dokument nie je {@link #upraviteľný()
	 * upraviteľný}, metóda prehrá zvuk pípnutia a skončí.</p>
	 * 
	 * <p>Parameter {@code ponechajOznačenie} dovoľuje určiť, či má
	 * byť po vykonaní nahradenia pôvodné označenie zrušené, alebo
	 * má zostať v pôvodnom stave (ak to objem textu, ktorý zostane
	 * v dokumente po nahradení dovolí). Ak je hodnota tohto parametra
	 * {@code valtrue}, tak označenie zostane nedotknuté – v pôvodnom
	 * rozsahu, pričom nahradený text sa môže, ale nemusí (čiastočne
	 * alebo úplne) nachádzať v ňom. Ak je hodnota parametra
	 * {@code valfalse}, tak bude označenie po vykonaní nahradenia
	 * zrušené.</p>
	 * 
	 * @param obsah obsah, ktorým má byť nahradené aktuálne označenie
	 * @param ponechajOznačenie ak je {@code valtrue}, tak napriek
	 *     nahradeniu zostane v bloku aktívne aktuálne označenie
	 *     <small>(pričom nahradený text sa môže, a nemusí celý
	 *     alebo čiastočne nachádzať v ňom)</small>
	 * 
	 * @see #označenýText()
	 * @see #nahraďOznačenie(String)
	 * @see #polohaKurzora(int)
	 */
	public void nahraďOznačenie(String obsah, boolean ponechajOznačenie)
	{
		replaceSelection(obsah);
		if (!ponechajOznačenie)
		{
			Caret kurzor = getCaret();
			int bodka = kurzor.getDot();
			int značka = kurzor.getMark();
			if (bodka < značka)
				kurzor.setDot(bodka);
			else if (značka < bodka)
				kurzor.setDot(značka);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #nahraďOznačenie(String, boolean) nahraďOznačenie}.</p> */
	public void nahradOznacenie(String obsah, boolean ponechajOznačenie)
	{ nahraďOznačenie(obsah, ponechajOznačenie); }


	/**
	 * <p>Prenesie poznámkový blok zo sveta do určeného okna alebo späť.</p>
	 * 
	 * <p>Ak je parameter {@code tam} rovný {@code valtrue} a zároveň sa blok
	 * nachádza vo svete (pozor, nie v inom okne, musí byť umestnený vo
	 * svete), tak bude prenesený do zadaného okna. Ak je parameter
	 * {@code tam} rovný {@code valfalse} a zároveň sa blok nachádza v zadanom
	 * okne (musí to byť presne to okno), tak bude prenesený zo zadaného okna
	 * do sveta.</p>
	 * 
	 * <p>V uvedených dvoch situáciách je návratová hodnota tejto metódy
	 * inštancia tohto poznámkového bloku, čo umožňuje zreťazené volanie
	 * ďalšej metódy bloku – pri inicializácii rozhrania a prenose bloku zo
	 * sveta do želaného okna sa na to dá spoľahnúť. Ak je však prenos
	 * neúspešný, tak je vrátená hodnota {@code valnull}.</p>
	 * 
	 * <p>Volanie tejto metódy je ekvivalentné volaniu metódy
	 * {@link Okno#prenes(PoznámkovýBlok poznámkovýBlok, boolean sem)}.</p>
	 * 
	 * @param okno okno, do ktorého alebo z ktorého má byť blok prenesený
	 * @param tam smer prenosu (pozri opis vyššie)
	 * @return inštancia tohto poznámkového bloku alebo {@code valnull}
	 */
	public PoznámkovýBlok prenes(Okno okno, boolean tam)
	{
		return okno.prenes(this, tam);
	}

	/**
	 * <p>Zistí, či je tento poznámkový blok umiestnený v zadanom okne.</p>
	 * 
	 * @return {@code valtrue} ak je blok v zadanom okne, {@code valfalse}
	 *     v opačnom prípade
	 */
	public boolean jeV(Okno okno)
	{
		return okno.jeTu(this);
	}


	// Nie je vhodné prekrývať tieto metódy:

	// @Override public void setLocation(int x, int y) { polohaX(x); polohaY(y) }
	// @Override public void setLocation(Point p) { polohaX(p.x); polohaY(p.y) }
	// @Override public int getX() { return x; }
	// @Override public int getY() { return y; }
	// @Override public int getWidth() { return šírka; }
	// @Override public int getHeight() { return výška; }
	// ...


	/**
	 * <p>Zistí, či má poznámkový blok zrušený dekor. Toto je predvolený stav
	 * a programovo sa dá dosiahnuť volaním metódy {@link #zrušDekor(boolean)
	 * zrušDekor} s parametrom {@code true}, čím sa pôvodný dekor odstráni zo
	 * všetkých súčastí poznámkového bloku.</p>
	 * 
	 * @return {@code true} ak bol dekor odstránený (predvolenou konštrukciou
	 *     alebo metódou {@link #zrušDekor(boolean) zrušDekor}), {@code false}
	 *     v opačnom prípade
	 * 
	 * @see #máDekor()
	 * @see #zrušDekor(boolean)
	 */
	public boolean jeDekorZrušený() { return zrušDekor; }

	/** <p><a class="alias"></a> Alias pre {@link #jeDekorZrušený() jeDekorZrušený}.</p> */
	public boolean jeDekorZruseny() { return zrušDekor; }

	/**
	 * <p>Zistí, či má poznámkový blok dekor. Táto metóda vracia opačnú
	 * logickú hodnotu ako metóda {@link #jeDekorZrušený() jeDekorZrušený}
	 * (pozr jej opis) a je jej doplnkom.</p>
	 * 
	 * <!-- p>(Zrušenie dekoru sa programovo dá dosiahnuť volaním metódy
	 * {@link #zrušDekor(boolean) zrušDekor} s parametrom {@code true}.)</p -->
	 * 
	 * @return {@code true} ak nebol dekor odstránený (predvolenou
	 *     konštrukciou alebo metódou {@link #zrušDekor(boolean) zrušDekor}),
	 *     {@code false} v opačnom prípade
	 * 
	 * @see #jeDekorZrušený()
	 * @see #zrušDekor(boolean)
	 */
	public boolean máDekor() { return !zrušDekor; }

	/** <p><a class="alias"></a> Alias pre {@link #máDekor() máDekor}.</p> */
	public boolean maDekor() { return !zrušDekor; }

	/**
	 * <p>Obnoví pôvodný alebo zruší aktuálny dekor poznámkového bloku. Ak je
	 * hodnota parametra {@code zrušiť} rovná {@code true}, tak je aktuálny
	 * dekor (orámovanie, pozadie a stav nepriehľadnosti) vymazaný (to je
	 * predvolený stav poznámkových blokov tohto rámca, pričom pôvodný dekor
	 * je uložený do vnútorných stavov blokov, aby mohol byť touto metódou
	 * obnovený). V opačnom prípade (čiže keď je parameter {@code zrušiť}
	 * rovný {@code false}) je obnovený ten dekor, ktorý bol aktívny v čase
	 * vytvárania komponentov (súčastí) poznámkového bloku.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Pozor na priame nastavovanie
	 * orámovania, farby pozadia a nepriehľadnosti tohto poznámkového bloku
	 * a jeho súčastí. Tento mechanizus ich ruší.</p>
	 * 
	 * @param zrušiť pravdivostná hodnota {@code true}/{@code false},
	 *     ktorá má určiť, či má byť tento komponent dekorovaný, alebo nie
	 * 
	 * @see #jeDekorZrušený()
	 * @see #máDekor()
	 */
	public void zrušDekor(boolean zrušiť)
	{
		if (zrušiť)
		{
			rolovanie.zrušDekor(true);

			setBorder(null);
			setBackground(žiadna);
			setOpaque(false);

			this.zrušDekor = true;
		}
		else
		{
			setBorder(okrajBloku);
			setBackground(pozadieBloku);
			setOpaque(textPaneOpaque);

			rolovanie.zrušDekor(false);

			this.zrušDekor = false;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #zrušDekor(boolean) zrušDekor}.</p> */
	public void zrusDekor(boolean zrušiť) { zrušDekor(zrušiť); }


	/**
	 * <p>Nastaví režim zobrazenia rolovacích líšt poznámkového bloku. Ak je
	 * parameter pre prislúchajúcu lištu ({@code horizontálna} alebo
	 * {@code vertikálna}) rovný hodnote {@code valtrue}, tak bude lišta stále
	 * viditeľná, ak je rovný {@code valfalse}, tak bude stále skrytá a ak je
	 * rovný {@code valnull}, tak bude lišta fungovať v automatickom režime
	 * a bude sa zobrazovať alebo skrývať podľa potreby (toto je predvolený
	 * režim zobrazenia líšt).</p>
	 * 
	 * @param horizontálna režim zobrazenia horizontálnej (vodorovnej) lišty
	 * @param vertikálna režim zobrazenia vertikálnej (zvislej) lišty
	 * 
	 * @see #režimZobrazeniaHorizontálnejLišty()
	 * @see #režimZobrazeniaVertikálnejLišty()
	 */
	public void režimZobrazeniaRolovacíchLíšt(
		Boolean horizontálna, Boolean vertikálna)
	{
		int h, v;
		if (null == horizontálna) h = HORIZONTAL_SCROLLBAR_AS_NEEDED;
		else if (horizontálna) h = HORIZONTAL_SCROLLBAR_ALWAYS;
		else h = HORIZONTAL_SCROLLBAR_NEVER;

		if (null == vertikálna) v = VERTICAL_SCROLLBAR_AS_NEEDED;
		else if (vertikálna) v = VERTICAL_SCROLLBAR_ALWAYS;
		else v = VERTICAL_SCROLLBAR_NEVER;

		rolovanie.setHorizontalScrollBarPolicy(h);
		rolovanie.setVerticalScrollBarPolicy(v);
	}

	/** <p><a class="alias"></a> Alias pre {@link #režimZobrazeniaRolovacíchLíšt(Boolean, Boolean) režimZobrazeniaRolovacíchLíšt}. */
	public void rezimZobrazeniaRolovacichList(
		Boolean horizontálna, Boolean vertikálna)
	{ režimZobrazeniaRolovacíchLíšt(horizontálna, vertikálna); }

	/**
	 * <p>Zistí režim zobrazenia horizontálnej rolovacej lišty poznámkového
	 * bloku. Hodnota {@code valtrue} znamená, že lišta je stále viditeľná.
	 * Hodnota {@code valfalse} znamená, že lišta je stále skrytá. Hodnota
	 * {@code valnull} znamená, že lišta funguje v automatickom režime –
	 * zobrazuje alebo skrýva sa podľa potreby (toto je predvolený režim
	 * zobrazenia líšt). (Hodnota {@code valnull} je vrátená aj v prípade
	 * chybného nastavenia režimu tejto lišty, ale tento prípad by nemal
	 * nastať.)</p>
	 * 
	 * @return režim zobrazenia horizontálnej rolovacej lišty ({@code valtrue},
	 *     {@code valfalse}, {@code valnull})
	 * 
	 * @see #režimZobrazeniaRolovacíchLíšt(Boolean, Boolean)
	 * @see #režimZobrazeniaVertikálnejLišty()
	 */
	public Boolean režimZobrazeniaHorizontálnejLišty()
	{
		int h = rolovanie.getHorizontalScrollBarPolicy();
		switch (h)
		{
			// case HORIZONTAL_SCROLLBAR_AS_NEEDED:
			case HORIZONTAL_SCROLLBAR_ALWAYS: return true;
			case HORIZONTAL_SCROLLBAR_NEVER: return false;
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #režimZobrazeniaHorizontálnejLišty() režimZobrazeniaHorizontálnejLišty}. */
	public Boolean rezimZobrazeniaHorizontalnejListy()
	{ return režimZobrazeniaHorizontálnejLišty(); }

	/**
	 * <p>Zistí režim zobrazenia vertikálnej rolovacej lišty poznámkového
	 * bloku. Hodnota {@code valtrue} znamená, že lišta je stále viditeľná.
	 * Hodnota {@code valfalse} znamená, že lišta je stále skrytá. Hodnota
	 * {@code valnull} znamená, že lišta funguje v automatickom režime –
	 * zobrazuje alebo skrýva sa podľa potreby (toto je predvolený režim
	 * zobrazenia líšt). (Hodnota {@code valnull} je vrátená aj v prípade
	 * chybného nastavenia režimu tejto lišty, ale tento prípad by nemal
	 * nastať.)</p>
	 * 
	 * @return režim zobrazenia vertikálnej rolovacej lišty ({@code valtrue},
	 *     {@code valfalse}, {@code valnull})
	 * 
	 * @see #režimZobrazeniaRolovacíchLíšt(Boolean, Boolean)
	 * @see #režimZobrazeniaHorizontálnejLišty()
	 */
	public Boolean režimZobrazeniaVertikálnejLišty()
	{
		int v = rolovanie.getVerticalScrollBarPolicy();
		switch (v)
		{
			// case VERTICAL_SCROLLBAR_AS_NEEDED;
			case VERTICAL_SCROLLBAR_ALWAYS: return true;
			case VERTICAL_SCROLLBAR_NEVER: return false;
		}
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #režimZobrazeniaVertikálnejLišty() režimZobrazeniaVertikálnejLišty}. */
	public Boolean rezimZobrazeniaVertikalnejListy()
	{ return režimZobrazeniaVertikálnejLišty(); }

	/**
	 * <p>Poskytne komponent rolovania {@link JScrollPane JScrollPane}
	 * poznámkového bloku.</p>
	 * 
	 * @return komponent rolovania {@link JScrollPane JScrollPane}
	 */
	public JScrollPane rolovanie() { return rolovanie; }
}
