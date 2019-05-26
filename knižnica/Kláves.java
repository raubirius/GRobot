
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2019 by Roman Horváth
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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

// ----------------------- //
//  *** Trieda Kláves ***  //
// ----------------------- //

/**
 * <p>Trieda je určená na použitie s udalosťami klávesnice ({@link 
 * KeyEvent KeyEvent}). Definuje niekoľko konštánt kódov klávesov, čím
 * uľahčuje použitie klávesnice (pozri metódy obsluhy
 * udalostí {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu},
 * {@link ObsluhaUdalostí#uvoľnenieKlávesu() uvoľnenieKlávesu}).
 * Nasledujúci príklad pri stlačení klávesu {@code VĽAVO} pípne:</p>
 * 
 * <pre CLASS="example">
	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}()
		{
			{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@code currKláves}.{@link Kláves#VĽAVO VĽAVO}))
			{
				{@link Svet Svet}.{@link Svet#pípni() pípni}();
			}
		}
	};
	</pre>
 * @see java.awt.event.KeyEvent
 */
@SuppressWarnings("serial")
public class Kláves extends KeyEvent
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/event/KeyEvent.html

	// Táto trieda má jediný dôvod svojho bytia – definícia konštánt –
	// nie je inštancionalizovateľná
	/*packagePrivate*/ Kláves() { super(null, 0, 0, 0, 0, ' '); }

	/** <p>Kód klávesu vľavo ({@link KeyEvent#VK_LEFT}).</p> */
	public final static int VĽAVO = KeyEvent.VK_LEFT;

	/** <p>Kód klávesu vľavo ({@link KeyEvent#VK_LEFT}).</p> */
	public final static int VLAVO = KeyEvent.VK_LEFT;

	/** <p>Kód klávesu vpravo ({@link KeyEvent#VK_RIGHT}).</p> */
	public final static int VPRAVO = KeyEvent.VK_RIGHT;

	/** <p>Kód klávesu hore ({@link KeyEvent#VK_UP}).</p> */
	public final static int HORE = KeyEvent.VK_UP;

	/** <p>Kód klávesu dole ({@link KeyEvent#VK_DOWN}).</p> */
	public final static int DOLE = KeyEvent.VK_DOWN;

	/** <p>Kód klávesu medzerník ({@link KeyEvent#VK_SPACE}).</p> */
	public final static int MEDZERA = KeyEvent.VK_SPACE;

	/** <p>Kód klávesu medzerník ({@link KeyEvent#VK_SPACE}).</p> */
	public final static int MEDZERNÍK = KeyEvent.VK_SPACE;

	/** <p>Kód klávesu medzerník ({@link KeyEvent#VK_SPACE}).</p> */
	public final static int MEDZERNIK = KeyEvent.VK_SPACE;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_PAGE_UP}).</p> */
	public final static int PAGE_UP = KeyEvent.VK_PAGE_UP;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_PAGE_UP}).</p> */
	public final static int STRÁNKA_HORE = KeyEvent.VK_PAGE_UP;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_PAGE_UP}).</p> */
	public final static int STRANKA_HORE = KeyEvent.VK_PAGE_UP;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_PAGE_UP}).</p> */
	public final static int STRANA_HORE = KeyEvent.VK_PAGE_UP;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_PAGE_DOWN}).</p> */
	public final static int PAGE_DOWN = KeyEvent.VK_PAGE_DOWN;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_PAGE_DOWN}).</p> */
	public final static int STRÁNKA_DOLE = KeyEvent.VK_PAGE_DOWN;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_PAGE_DOWN}).</p> */
	public final static int STRANKA_DOLE = KeyEvent.VK_PAGE_DOWN;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_PAGE_DOWN}).</p> */
	public final static int STRANA_DOLE = KeyEvent.VK_PAGE_DOWN;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_HOME}).</p> */
	public final static int HOME = KeyEvent.VK_HOME;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_HOME}).</p> */
	public final static int ZAČIATOK = KeyEvent.VK_HOME;

	/** <p>Kód klávesu Page Up ({@link KeyEvent#VK_HOME}).</p> */
	public final static int ZACIATOK = KeyEvent.VK_HOME;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_END}).</p> */
	public final static int END = KeyEvent.VK_END;

	/** <p>Kód klávesu Page Down ({@link KeyEvent#VK_END}).</p> */
	public final static int KONIEC = KeyEvent.VK_END;

	/** <p>Kód klávesu tabulátor ({@link KeyEvent#VK_TAB}).</p> */
	public final static int TAB = KeyEvent.VK_TAB;

	/** <p>Kód klávesu tabulátor ({@link KeyEvent#VK_TAB}).</p> */
	public final static int TABULÁTOR = KeyEvent.VK_TAB;

	/** <p>Kód klávesu tabulátor ({@link KeyEvent#VK_TAB}).</p> */
	public final static int TABULATOR = KeyEvent.VK_TAB;

	/** <p>Kód klávesu Enter ({@link KeyEvent#VK_ENTER}).</p> */
	public final static int ENTER = KeyEvent.VK_ENTER;

	/** <p>Kód klávesu Escape ({@link KeyEvent#VK_ESCAPE}).</p> */
	public final static int ESCAPE = KeyEvent.VK_ESCAPE;

	/** <p>Kód klávesu Backspace ({@link KeyEvent#VK_BACK_SPACE}).</p> */
	public final static int BACKSPACE = KeyEvent.VK_BACK_SPACE;

	/** <p>Kód klávesu Backspace ({@link KeyEvent#VK_BACK_SPACE}).</p> */
	public final static int BACK_SPACE = KeyEvent.VK_BACK_SPACE;

	/** <p>Kód klávesu Delete ({@link KeyEvent#VK_DELETE}).</p> */
	public final static int DELETE = KeyEvent.VK_DELETE;

	/**
	 * <p>Kombinačný kód klávesových skratiek ponuky, čo je na niektorých
	 * operačných systémoch (ako Windows) kód pre kláves {@code Ctrl},
	 * na iných kód pre iný kláves, napríklad {@code ⌘} ({@code Command})
	 * na macOS (predtým OS X a Mac OS) pre Apple Macintosh.
	 * Použitie tohto kódu má zmysel napríklad pri volaní metódy {@link 
	 * Svet#pridajKlávesovúSkratku(String, int, int)
	 * Svet.pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor)}
	 * v argumente {@code modifikátor}.</p>
	 */
	public final static int SKRATKA_PONUKY = Toolkit.
		getDefaultToolkit().getMenuShortcutKeyMask();
}
