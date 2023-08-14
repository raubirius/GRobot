
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to rolovacia tabla (JScrollPane) obsahujúca
// viacriadkový textový komponent (JTextPane). Rámec ho používa v rámci
// komunikačných dialógov Sveta. Licencia a zdroje sú uvedené nižšie
// v anglickom jazyku. Ďalšie komentáre sú však (výnimočne; v rámci balíčka
// podpory knižnice) po slovensky…
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Set;
import java.util.function.Function;

// import javax.swing.Action;
// import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
// import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import javax.swing.border.Border;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
// import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
// import javax.swing.text.Element;
// import javax.swing.text.MutableAttributeSet;
// import javax.swing.text.SimpleAttributeSet;
// import javax.swing.text.StyleConstants;

import knižnica.Svet;

/**
 * <p>Trieda slúžiaca na vytvorenie textového bloku na úpravu väčšieho objemu
 * textu, ktorý je automaticky rolovateľný. Trieda automaticky vnútorne
 * vytvára (a uzatvára do rolovacej tably) komponent {@link JTextPane
 * JTextPane}. Sprostredkúva k nemu prístup viacerými predvolenými metódami
 * a tiež ho umožňuje vrátiť metódou {@link #getTextPane() getTextPane()}.</p>
 * 
 * <p>Trieda bola primárne určená na vkladanie do konfiguračných dialógov
 * sveta, dá sa však využiť ľubovoľne mimo rámca. V budúcnosti môže byť
 * rozširovaná…</p>
 * 
 * @author Roman Horváth
 * @version 31. 1. 2022
 */
@SuppressWarnings("serial")
public class ScrollTextPane extends JScrollPane
{
	// Inštancia priesvitnej farby (na zrušenie dekoru):
	private final static Color transparentColor = new Color(0, 0, 0, 0);

	// Inštancia tably úpravy textu JTextPane:
	private final JTextPane textPane = new JTextPane();

	// Vlastnosti upravujúce fungovanie klávesov TAB (a pôvodne aj Enter):
	private boolean forbidTabulator = false;
	// private boolean forbidEnter = false;

	// Príznak zruśenia dekoru komponentu:
	private boolean undecorated = false;

	// Inštancie slúžiace na zálohovanie dekoru komponentu:
	private Border scrollBorder = null;
	private Color scrollBackground = null;
	private boolean scrollOpaque = false;

	private Border viewportBorder = null;
	private Color viewportBackground = null;
	private boolean viewportOpaque = false;

	private Border textPaneBorder = null;
	private Color textPaneBackground = null;
	private boolean textPaneOpaque = false;


	private class DocFilter extends DocumentFilter
	{
		public void insertString(FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException
		{
			if (forbidTabulator) string = string.replaceAll("\\t+", "");
			// if (forbidEnter) string = string.replaceAll("[\\r\\n]+", "");
			fb.insertString(offset, string, attr);
		}

		public void replace(FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException
		{
			if (forbidTabulator) string = string.replaceAll("\\t+", "");
			// if (forbidEnter) string = string.replaceAll("[\\r\\n]+", "");
			fb.replace(offset, length, string, attr);
		}
	}

	private final DocFilter docFilter = new DocFilter();

	public static Function<KeyEvent, Boolean> spracovanieFokusu = null;

	// Určuje predvolené hodnoty vlastností komponentu.
	private void create()
	{
		setViewportView(textPane);

		Document document = textPane.getDocument();
		if (document instanceof AbstractDocument)
			((AbstractDocument)document).setDocumentFilter(docFilter);

		textPane.addHyperlinkListener(new HyperlinkListener()
			{
				public void hyperlinkUpdate(HyperlinkEvent e)
				{
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
						hyperlinkActivated(e.getDescription());
				}
			});

		textPane.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if (forbidTabulator && null != spracovanieFokusu)
						// spracujFokus(e); // TODO: del
						spracovanieFokusu.apply(e);

					/*
					// TODO: Bolo by treba otestovať na macOS, lebo v tomto
					// sú isté nekonzistencie v implementáciách…
					if (forbidTabulator && e.getKeyCode() == KeyEvent.VK_TAB &&
						!e.isAltDown() && !e.isMetaDown())
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
					*/
					/* else if (forbidEnter && e.getKeyCode() ==
						KeyEvent.VK_ENTER && !e.isAltDown() &&
						!e.isControlDown() && !e.isMetaDown() &&
						!e.isShiftDown()) {} */
				}

				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});
	}


	// Toto je obdoba originálnej metódy sveta, pretože metóda sveta je
	// súkromná pre balíček (čiže nie je viditeľná mimo balíčka) a táto metóda
	// nepotrebuje volať zákaznícke obsluhy udalostí. Avšak všetky relevantné
	// zmeny v oboch metódach by mali byť reflektované v rámci obidvoch metód.
	// 
	// Overí, či bola stlačená kombinácia zodpovedajúca zmene fokusu
	// (focus traversal keys), ak áno, spracuje to ako udalosť a ak
	// nebola táto udalosť „zjedená,“ tak vráti false, aby ju mohli
	// spracovať ďalšie súčasti.
	/*
	private static boolean spracujFokus(KeyEvent e) // TODO: del
	{
		Component komponent = e.getComponent();
		if (null == komponent) return true;

		// Vytvorenie AWTKeyStroke z KeyEventu:
		AWTKeyStroke aks = AWTKeyStroke.getAWTKeyStrokeForEvent(e);

		// Prevzatie klávesových skratiek zmeny fokusu pre aktuálny
		// komponent:
		Set<AWTKeyStroke> skratkyVpred = komponent.getFocusTraversalKeys(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> skratkyVzad = komponent.getFocusTraversalKeys(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);

		final KeyboardFocusManager manažér = KeyboardFocusManager.
			getCurrentKeyboardFocusManager();

		// Overenie voľby klávesových skratiek zmeny fokusu:
		if (skratkyVpred.contains(aks))
		{
			e.consume();

			manažér.focusNextComponent();
			SwingUtilities.invokeLater(() ->
			{
				if (manažér.getFocusOwner() instanceof JScrollBar)
					manažér.focusNextComponent();
			});

			return false;
		}
		else if (skratkyVzad.contains(aks))
		{
			e.consume();

			manažér.focusPreviousComponent();
			SwingUtilities.invokeLater(() ->
			{
				if (manažér.getFocusOwner() instanceof JScrollBar)
					manažér.focusPreviousComponent();
			});

			return false;
		}

		return true;
	}
	*/


	/**
	 * <p>Základný konštruktor komponentu.</p>
	 * 
	 * <p>Vytvorí základný komponent.</p>
	 * 
	 * @see #ScrollTextPane(String)
	 * @see #ScrollTextPane(String, boolean)
	 */
	public ScrollTextPane()
	{
		create();
	}

	/**
	 * <p>Konštruktor komponentu s predvoleným textom.</p>
	 * 
	 * <p>Vytvorí základný komponent so zadaným textom komponentu.</p>
	 * 
	 * <p>Ak sa zadnaný text začína značkou <code>&lt;html&gt;</code>
	 * (malými písmenami), tak je typ dokumentu komponentu nastavený na
	 * {@code "text/html"}, inak na {@code "text/plain"}.</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param text predvolený text komponentu
	 * 
	 * @see #ScrollTextPane()
	 * @see #ScrollTextPane(String, boolean)
	 * @see #setText(String)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 */
	public ScrollTextPane(String text)
	{
		if (text.startsWith("<html>"))
			textPane.setContentType("text/html");
		else
			textPane.setContentType("text/plain");
		textPane.setText(text);
		create();
	}

	/**
	 * <p>Konštruktor komponentu s predvoleným textom a stavom povolenia
	 * úprav.</p>
	 * 
	 * <p>Vytvorí základný komponent so zadaným textom komponentu, pričom
	 * pri inicializácii nastaví stav povolenia úprav textu podľa zadanej
	 * logickej hodnoty.</p>
	 * 
	 * <p>Ak sa zadnaný text začína značkou <code>&lt;html&gt;</code>
	 * (malými písmenami), tak je typ dokumentu komponentu nastavený na
	 * {@code "text/html"}, inak na {@code "text/plain"}.</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param text predvolený text komponentu
	 * @param editable parameter určujúci, či bude komponent určený aj na
	 *     úpravy – {@code true} alebo len na čítanie – {@code false}
	 * 
	 * @see #ScrollTextPane()
	 * @see #ScrollTextPane(String)
	 * @see #setText(String)
	 * @see #setEditable(boolean)
	 */
	public ScrollTextPane(String text, boolean editable)
	{
		if (text.startsWith("<html>"))
			textPane.setContentType("text/html");
		else
			textPane.setContentType("text/plain");
		textPane.setText(text);
		textPane.setEditable(editable);
		create();
	}


	/**
	 * <p>Táto metóda poskytuje vnútorne vytváranú inštanciu triedy
	 * {@link JTextPane JTextPane}, aby s ňou bolo možné komunikovať
	 * a dodatočne manipulovať.</p>
	 * 
	 * @return komponent {@link JTextPane JTextPane} uzavretý v tejto
	 *     inštancii
	 */
	public JTextPane getTextPane() { return textPane; }


	/**
	 * <p>Vráti text dokumentu vo forme čistého textu. Ak je typ dokumentu
	 * {@code "text/plain"} alebo {@code "text/html"}, tak vráti
	 * aktuálny obsah komponentu ako čistý text, inak vráti hodnotu
	 * {@code null} (Na rozdiel od pôvodnej verzie metódy {@link 
	 * JEditorPane#getText() JEditorPane.getText()}, ktorá vždy vracala úplný
	 * text komponentu.)</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @return čistý text komponentu alebo {@code null}
	 * 
	 * @see #getText(boolean)
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 * @see #getSelectedText()
	 */
	public String getText()
	{
		if (textPane.getContentType().equalsIgnoreCase("text/plain"))
			return textPane.getText();

		if (textPane.getContentType().equalsIgnoreCase("text/html"))
		{
			Document document = textPane.getDocument();
			if (null != document)
				try
				{
					return document.getText(0, document.getLength());
				}
				catch (BadLocationException bad)
				{
					// GRobotException.vypíšChybovéHlásenia(bad);
					bad.printStackTrace();
				}
		}

		return null;
	}

	/**
	 * <p>Vráti text dokumentu vo forme čistého textu alebo hodnotu
	 * {@code null}. Ak je parameter {@code ignoreType} rovný {@code true},
	 * tak sa táto metóda bude správať rovnako ako metóda {@link 
	 * JEditorPane#getText() JEditorPane.getText()}. Ak je hodnota tohto
	 * prarametra rovná {@code false}, tak sa metóda bude správať rovnako
	 * ako verzia metódy tejto triedy: {@link #getText() getText()}, ktorá
	 * vracia aj hodnotu {@code null}.</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param ignoreType určí, či má metóda kontrolovať typ obsahu, alebo nie
	 * @return čistý text komponentu alebo {@code null}
	 * 
	 * @see #getText()
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 * @see #getSelectedText()
	 */
	public String getText(boolean ignoreType)
	{
		if (ignoreType) return textPane.getText();
		return getText();
	}

	/**
	 * <p>Nastaví nový čistý text komponentu. Táto metóda, na rozdiel od
	 * pôvodnej metódy {@link JEditorPane#setText(String)
	 * JEditorPane.setText(t)}, nastaví aj typ obsahu komponentu a to na
	 * {@code "text/plain"} (čo je v podstate predvolený typ obsahu).</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param text nový čistý text komponentu
	 * 
	 * @see #ScrollTextPane(String)
	 * @see #ScrollTextPane(String, boolean)
	 * @see #getText()
	 * @see #getText(boolean)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 * @see #replaceSelection(String)
	 * @see #replaceSelection(String, boolean)
	 */
	public void setText(String text)
	{
		textPane.setContentType("text/plain");
		textPane.setText(text);
	}

	/**
	 * <p>Nastaví nový čistý text komponentu. Táto metóda dovoľuje
	 * parametrom {@code keepContentType} zvoliť, či má byť nastavený aj typ
	 * obsahu komponentu (na {@code "text/plain"}; pozri aj {@link 
	 * #setText(String) setText(text)}).</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param text nový čistý text komponentu
	 * @param keepContentType ak je {@code true}, tak nenastaví (nezmení)
	 *     typ obsahu; v opačnom prípade sa metóda správa rovnako ako metóda
	 *     {@link #setText(String) setText(text)}
	 * 
	 * @see #ScrollTextPane(String)
	 * @see #ScrollTextPane(String, boolean)
	 * @see #getText()
	 * @see #getText(boolean)
	 * @see #setText(String)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 * @see #replaceSelection(String)
	 * @see #replaceSelection(String, boolean)
	 */
	public void setText(String text, boolean keepContentType)
	{
		if (!keepContentType) textPane.setContentType("text/plain");
		textPane.setText(text);
	}


	/**
	 * <p>Ak je typ dokumentu {@code "text/html"}, tak metóda vráti
	 * aktuálny HTML obsah komponentu (ako čistý text so syntaxou HTML),
	 * inak metóda vráti hodnotu {@code null}.</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @return HTML text komponentu alebo {@code null}
	 * 
	 * @see #setHTML(String)
	 * @see #getText()
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getSelectedText()
	 */
	public String getHTML()
	{
		if (!textPane.getContentType().equalsIgnoreCase("text/html"))
			return null;
		return textPane.getText();
	}

	/**
	 * <p>Nastaví nový HTML obsah komponentu. Metóda očakáva obsah vo forme
	 * čistého textu, ktorý obsahuje HTML syntax, ale prijatý obsah nijako
	 * neoveruje, ani nemodifikuje. Táto metóda nastaví typ obsahu na
	 * {@code "text/html"}, ktorý je automaticky nastavený konštruktorom
	 * v prípade, že sa ním nastavovaný text začína značkou
	 * <code>&lt;html&gt;</code>.</p>
	 * 
	 * <p><b>Poznámka:</b> Typ dokumentu je možné overiť metódou
	 * {@link #getContentType() getContentType()}, ktorá vráti typ
	 * v reťazcovej podobe. Príklady: {@code "text/plain"},
	 * {@code "text/html"}.</p>
	 * 
	 * @param text nový HTML obsah komponentu
	 * 
	 * @see #ScrollTextPane(String)
	 * @see #ScrollTextPane(String, boolean)
	 * @see #getHTML()
	 * @see #getText()
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #replaceSelection(String)
	 * @see #replaceSelection(String, boolean)
	 */
	public void setHTML(String html)
	{
		textPane.setContentType("text/html");
		textPane.setText(html);
	}


	/**
	 * <p>Vráti označený text dokumentu. Metóda vracia čistý text.</p>
	 * 
	 * <p><b>Poznámka:</b> Na rozdiel od pôvodnej metódy {@link 
	 * JTextPane#getSelectedText() JTextPane.getSelectedText()} nevracia
	 * táto metóda <small>(v prípade neúspechu alebo neočakávaného
	 * stavu)</small> hodnotu {@code null}, ale prázdny reťazec
	 * {@code ""}.</p>
	 * 
	 * @return čistý text označenej časti dokumentu v komponente
	 * 
	 * @see #getText()
	 * @see #getText(boolean)
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 * @see #replaceSelection(String)
	 * @see #replaceSelection(String, boolean)
	 */
	public String getSelectedText()
	{
		String text = null;
		try
		{
			text = textPane.getSelectedText();
		}
		catch (IllegalArgumentException e)
		{
			text = "";
		}
		if (null == text) text = "";
		return text;
	}


	/**
	 * <p>Nahradí aktuálne označený obsah zadaným obsahom (reťazcom). Táto
	 * metóda sa správa rovnako, ako keby sme volali metódu: {@link 
	 * #replaceSelection(String, boolean) replaceSelection}{@code (content,
	 * }{@code false}{@code );}. <small>(Ďalšie podrobnosti nájdete v jej
	 * opise.)</small></p>
	 * 
	 * @param obsah obsah, ktorým má byť nahradené aktuálne označenie
	 * 
	 * @see #getSelectedText()
	 * @see #replaceSelection(String, boolean)
	 * @see #getText()
	 * @see #getText(boolean)
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 */
	public void replaceSelection(String content)
	{ replaceSelection(content, false); }


	/**
	 * <p>Nahradí aktuálne označený obsah zadaným obsahom (reťazcom). Ak nie
	 * je označený žiadny text, zadaný reťazec bude vložený na aktuálnu
	 * pozíciu v komponentu. Ak je zadaný reťazec prázdny, tak text aktuálneho
	 * označenia (ak jestvuje) bude vymazaný. Vkladaný text bude mať také
	 * atribúty textu, ktoré sú platné v mieste vkladania. Ak dokument nie je
	 * upraviteľný, tak metóda prehrá zvuk pípnutia a skončí.</p>
	 * 
	 * <p>Parameter {@code keepSelection} dovoľuje určiť, či má byť po
	 * vykonaní nahradenia pôvodné označenie zrušené, alebo má zostať
	 * v pôvodnom stave (ak to objem textu, ktorý zostane v dokumente po
	 * nahradení dovolí). Ak je hodnota tohto parametra {@code true}, tak
	 * označenie zostane nedotknuté – v pôvodnom rozsahu, pričom nahradený
	 * text sa môže, ale nemusí (čiastočne alebo úplne) nachádzať v ňom.
	 * Ak je hodnota parametra {@code false}, tak bude označenie po vykonaní
	 * nahradenia zrušené (toto je očakávané správanie, ale pôvodná metóda ho
	 * nemala, preto má upravená verzia metódy odlišné správanie a vznikla
	 * k nej táto verzia metódy).</p>
	 * 
	 * @param content obsah, ktorým má byť nahradené aktuálne označenie
	 * @param keepSelection ak je {@code true}, tak napriek nahradeniu
	 *     zostane v komponente aktívne aktuálne označenie <small>(pričom
	 *     nahradený text sa môže, a nemusí celý alebo čiastočne nachádzať
	 *     v tomto označení)</small>
	 * 
	 * @see #getSelectedText()
	 * @see #replaceSelection(String)
	 * @see #getText()
	 * @see #getText(boolean)
	 * @see #setText(String)
	 * @see #setText(String, boolean)
	 * @see #getHTML()
	 * @see #setHTML(String)
	 */
	public void replaceSelection(String content, boolean keepSelection)
	{
		textPane.replaceSelection(content);
		if (!keepSelection)
		{
			Caret caret = textPane.getCaret();
			int dot = caret.getDot();
			int mark = caret.getMark();
			if (dot < mark) caret.setDot(dot);
			else if (mark < dot) caret.setDot(mark);
		}
	}


	/**
	 * <p>Nastaví tento komponent na úpravy alebo len na čítanie podľa hodnoty
	 * parametra {@code editable}. Ak je parameter rovný {@code true}, tak je
	 * komponent určený na úpravy, v opačnom prípade len na čítanie.</p>
	 * 
	 * <p>Volanie tejto metódy spôsobí pri zmene stavu spustenie udalosti
	 * {@code PropertyChange} ({@code "editable"}).</p>
	 * 
	 * @param editable {@code true}/{@code false} podľa toho, či má byť
	 *     komponent určený na úpravy, alebo nie
	 * 
	 * @see #isEditable()
	 */
	public void setEditable(boolean editable)
	{ textPane.setEditable(editable); }

	/**
	 * <p>Zistí, či je alebo nie je komponent určený na úpravy. Ak je
	 * návratová hodnota tejto metódy rovná {@code true}, tak je komponent
	 * určený na úpravy, v opačnom prípade je určený len na čítanie.</p>
	 * 
	 * @return {@code true}/{@code false} podľa toho, je komponent určený na
	 *     úpravy, alebo nie
	 * 
	 * @see #setEditable(boolean)
	 */
	public boolean isEditable() { return textPane.isEditable(); }


	/**
	 * <p>Zakáže alebo povolí predvolenú funkciu klávesu tabulátora
	 * a vkladanie znaku tabulátora pre tento komponent. Predvolene je
	 * hodnota tejto vlastnosti nastavená na {@code false} a kláves
	 * tabulátora vkladá znak tabulátora do textu komponentu. Ak túto funkciu
	 * zakážeme, tak kláves tabulátora bude plniť funkciu prechodu na ďalší
	 * (prípadne so shiftom predchádzajúci) komponent aktuálneho manažéra
	 * komponentov, ktorý je k tomuto komponentu priradený automaticky
	 * a vkladanie znaku tabulátora bude zakázané aj inými mechanizmami.</p>
	 * 
	 * <!-- p><b>Poznámka:</b> Nastavenie tejto vlastnosti neznamená, že
	 * tabulátor nemôže byť do komponentu vložený iným spôsobom.</p -->
	 * 
	 * @param forbidTabulator {@code true} ak má byť predvolená funkčnosť
	 *     klávesu a vkladanie znaku tabulátora zakázané; {@code false}
	 *     v opačnom prípade
	 * 
	 * @see #forbidTabulator()
	 */
	public void forbidTabulator(boolean forbidTabulator)
	{ this.forbidTabulator = forbidTabulator; }

	/**
	 * <p>Zistí, či je zakázaná predvolená funkcia tabulátora a vkladanie
	 * znaku tabulátora pre tento komponent.</p>
	 * 
	 * @return {@code true} ak je predvolená funkčnosť klávesu a vkladanie
	 *     znaku tabulátora zakázané; {@code false} v opačnom prípade
	 * 
	 * @see #forbidTabulator(boolean)
	 */
	public boolean forbidTabulator() { return forbidTabulator; }


	// /**
		//  * <p>Zakáže alebo povolí predvolenú funkciu klávesu {@code Enter} a vkladania znakov nových riadkov pre
		//  * tento komponent. Predvolene je hodnota tejto vlastnosti nastavená na
		//  * {@code false} a kláves {@code Enter} vkladá nový riadok do textu
		//  * tohto komponentu. Ak túto vlastnosť nastavíme na {@code true}, tak
		//  * {@code Enter} prestane túto funkciu plniť a vkladanie znakov nových riadkov bude zakázané.</p>
		//  * 
		//  * <!-- p><b>Poznámka:</b> Nastavenie tejto vlastnosti neznamená, že nový
		//  * riadok nemôže byť do komponentu vložený iným spôsobom.</p -->
		//  * 
		//  * @param forbidEnter {@code true} ak má byť predvolená funkčnosť
		//  *     klávesu Enter a vkladanie znakov nových riadkov zakázané; {@code false} v opačnom prípade
		//  * 
		//  * @see #forbidEnter()
		//  */
		// public void forbidEnter(boolean forbidEnter)
		// { this.forbidEnter = forbidEnter; }

	// /**
		//  * <p>Zistí, či je zakázaná predvolená funkcia
		//  * klávesu {@code Enter} a vkladanie znakov nových riadkov pre tento komponent</p>
		//  * 
		//  * @return {@code true} ak je predvolená funkčnosť klávesu Enter a vkladanie znakov nových riadkov zakázané;
		//  *     {@code false} v opačnom prípade
		//  * 
		//  * @see #forbidEnter(boolean)
		//  */
		// public boolean forbidEnter() { return forbidEnter; }


	// /**
		//  * <p>Nastaví farbu a priehľadnosť pozadia komponentu podľa zadanej
		//  * farebnej inštancie.</p>
		//  * 
		//  * <p><b>Poznámka:</b> Nie je garantované, že táto vlastnosť bude
		//  * rešpektovaná definíciou vzhľadu používateľského rozhrania (L&F).
		//  * Každá definícia vzhľadu komponentov sa môže slobodne rozhodnúť o tom,
		//  * či bude túto vlastnosť rešpektovať.</p>
		//  * 
		//  * @param color objekt určujúci novú farbu pozadia
		//  */
		// public void setBackground(Color color)
		// {
		// 	super.setBackground(color);
		// 
		// 	/* if (null == textPane)
		// 	{
		// 		SwingUtilities.invokeLater(() ->
		// 		{
		// 			textPane.setOpaque(0 != color.getAlpha());
		// 			textPane.setBackground(color);
		// 		});
		// 	}
		// 	else
		// 	{
		// 		textPane.setOpaque(0 != color.getAlpha());
		// 		textPane.setBackground(color);
		// 	}*/
		// }


	/**
	 * <p>Vráti aktuálne nastavený typ obsahu pre tento komponent.</p>
	 * 
	 * @return typ obsahu alebo {@code null} ak nie je nastavený; príklady:
	 *     {@code "text/plain"}, {@code "text/html"}
	 * 
	 * @see #setContentType(String)
	 */
	public String getContentType() { return textPane.getContentType(); }

	/**
	 * <p>Nastaví nový typ obsahu pre tento komponent.</p>
	 * 
	 * <p>Ak je v parametri určená aj tabuľka znakov, tak bude táto použitá
	 * pri čítaní vstupných údajov. Ak typ nie je rozpoznaný, tak bude
	 * použitý typ {@code "text/plain"}.</p>
	 * 
	 * @param type nový typ obsahu pre tento komponent; príklady:
	 *     {@code "text/plain"}, {@code "text/html"}
	 * 
	 * @throws NullPointerException ak bol parameter {@code type} rovný
	 *     {@code null}
	 * 
	 * @see #getContentType()
	 */
	public void setContentType(String type) { textPane.setContentType(type); }


	/**
	 * <p>Táto metóda je určená na prekrytie.</p>
	 * 
	 * @param hyperlinkDescription opis prepojenia; pozri: {@link 
	 *     HyperlinkEvent#getDescription() HyperlinkEvent.getDescription()}
	 */
	public void hyperlinkActivated(String hyperlinkDescription) {}


	/**
	 * <p>Zistí, či bola volaná metóda {@link #setUndecorated(boolean)
	 * setUndecorated} s parametrom {@code true}. Predvolene je každý
	 * komponent vytvorený s dekorom a až volanie metódy {@link 
	 * #setUndecorated(boolean) setUndecorated} s parametrom {@code true}
	 * zálohuje dekor a odstráni ho zo všetkých súčastí komponentu.</p>
	 * 
	 * @return {@code true} ak bol dekor zálohovaný a odstránený metódou
	 *     {@link #setUndecorated(boolean) setUndecorated}, {@code false}
	 *     v opačnom prípade
	 */
	public boolean isUndecorated() { return undecorated; }

	/**
	 * <p>Obnoví alebo zruší dekor komponentu. Ak je hodnota parametra
	 * {@code undecorated} rovná {@code true} a predtým (naposledy) nebola
	 * táto metóda volaná s týmto parametrom rovným {@code true} (čiže stav
	 * má byť zmenený), tak je aktuálny dekor (orámovanie, pozadie a stav
	 * nepriehľadnosti) zálohovaný a vymazaný. V opačnom prípade, čiže keď je
	 * parameter {@code undecorated} rovný {@code false} a posledný stav bol
	 * {@code true} (čiže opäť má nastať zmena stavu, ale opačným smerom) je
	 * obnovený posledný zálohovaný dekor. Pozor na priame nastavovanie
	 * orámovania, farby pozadia a nepriehľadnosti tohto komponentu a jeho
	 * súčastí. Tieto mechanizmy spolu nekomunikujú a nemali by sa miešať.</p>
	 * 
	 * @param undecorated pravdivostná hodnota {@code true}/{@code false},
	 *     ktorá má určiť, či má byť tento komponent dekorovaný, alebo nie
	 */
	public void setUndecorated(boolean undecorated)
	{
		if (undecorated && !this.undecorated)
		{
			scrollBorder = getBorder();
			scrollBackground = getBackground();
			scrollOpaque = isOpaque();

			setBorder(null);
			setBackground(transparentColor);
			setOpaque(false);

			JViewport viewport = getViewport();
			if (null != viewport)
			{
				viewportBorder = viewport.getBorder();
				viewportBackground = viewport.getBackground();
				viewportOpaque = viewport.isOpaque();

				viewport.setBorder(null);
				viewport.setBackground(transparentColor);
				viewport.setOpaque(false);
			}

			textPaneBorder = textPane.getBorder();
			textPaneBackground = textPane.getBackground();
			textPaneOpaque = textPane.isOpaque();

			textPane.setBorder(null);
			textPane.setBackground(transparentColor);
			textPane.setOpaque(false);

			this.undecorated = true;
		}
		else if (!undecorated && this.undecorated)
		{
			textPane.setBorder(textPaneBorder);
			textPane.setBackground(textPaneBackground);
			textPane.setOpaque(textPaneOpaque);

			JViewport viewport = getViewport();
			if (null != viewport)
			{
				viewport.setBorder(viewportBorder);
				viewport.setBackground(viewportBackground);
				viewport.setOpaque(viewportOpaque);
			}

			setBorder(scrollBorder);
			setBackground(scrollBackground);
			setOpaque(scrollOpaque);

			this.undecorated = false;
		}
	}
}
