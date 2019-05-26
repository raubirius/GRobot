
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

import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;

import static knižnica.Konštanty.ĽAVÉ;
import static knižnica.Konštanty.STREDNÉ;
import static knižnica.Konštanty.PRAVÉ;

// ------------------------------ //
//  *** Trieda ÚdajeUdalostí ***  //
// ------------------------------ //

/**
 * <p>Toto je statická trieda disponujúca informáciami o rôznych udalostiach
 * sveta. Svet pravidelne automaticky aktualizuje staické údaje uložené
 * v rámci tejto triedy. Využite nachádza pri {@linkplain ObsluhaUdalostí
 * spracovaní udalostí} (pozri triedu {@link ObsluhaUdalostí
 * ObsluhaUdalostí}).</p>
 */
public class ÚdajeUdalostí
{
	// ------------------------------------ //
	//  *** Vlastnosti údajov udalostí ***  //
	// ------------------------------------ //


		// Na účely synchronizácie
		/*packagePrivate*/ final static Object zámokMyši       = new Object();
		/*packagePrivate*/ final static Object zámokKlávesnice = new Object();
		/*packagePrivate*/ final static Object zámokAktivít    = new Object();
		/*packagePrivate*/ final static Object zámokUdalostí   = new Object();


		// Stav myši
		/*packagePrivate*/ static boolean tlačidloMyši1 = false;
		/*packagePrivate*/ static boolean tlačidloMyši2 = false;
		/*packagePrivate*/ static boolean tlačidloMyši3 = false;
		/*packagePrivate*/ static int tlačidloMyši = 0;
		/*packagePrivate*/ static double súradnicaMyšiX = 0;
		/*packagePrivate*/ static double súradnicaMyšiY = 0;
		/*packagePrivate*/ static int rolovanieKolieskomMyšiX = 0;
		/*packagePrivate*/ static int rolovanieKolieskomMyšiY = 0;

		// Poslený posun rolovania rolovacej lišty
		/*packagePrivate*/ static int poslednýPosunRolovacejLišty = 0;

		// Objekty vzťahujúce sa k posledným akciám a udalostiam
		/*packagePrivate*/ static ActionEvent poslednýTik = null;
		/*packagePrivate*/ static ComponentEvent poslednáUdalosťOkna = null;
		/*packagePrivate*/ static WindowEvent
			poslednáUdalosťAktivityOkna = null;

		/*packagePrivate*/ static PoložkaPonuky poslednáPoložkaPonuky = null;
		/*packagePrivate*/ static KontextováPoložka
			poslednáKontextováPoložka = null;
		/*packagePrivate*/ static Tlačidlo poslednéTlačidlo = null;
		/*packagePrivate*/ static RolovaciaLišta poslednáRolovaciaLišta = null;

		/*packagePrivate*/ static AdjustmentEvent poslednáUdalosťPosunu = null;
		/*packagePrivate*/ static KeyEvent poslednáUdalosťKlávesnice = null;
		/*packagePrivate*/ static ActionEvent poslednáUdalosťSkratky = null;
		/*packagePrivate*/ static String poslednýPríkazSkratky = null;
		/*packagePrivate*/ static MouseEvent poslednáUdalosťMyši = null;
		/*packagePrivate*/ static MouseWheelEvent
			poslednáUdalosťRolovania = null;

		/*packagePrivate*/ static Plátno poslednéVymazanéPlátno = null;

		/*packagePrivate*/ static String poslednýOdkaz = null;
		/*packagePrivate*/ static PoznámkovýBlok poslednýPoznámkovýBlok = null;

		/*packagePrivate*/ static ActionEvent
			poslednáUdalosťSystémovejIkony = null;
		/*packagePrivate*/ static int
			poslednáPoložkaSystémovejPonuky = -1;


	// Technická poznámka: V skutočnosti je z technického hľadiska výrok
	// „údaje inštancie údajeUdalostí sú pravidelne aktualizované“
	// nepravdivý; všetky údaje sú totiž statické, rovnako ako sú všetky
	// metódy tejto triedy…


	// Súkromný konštruktor = nie je možné vytvoriť vlastnú inštanciu triedy
	/*packagePrivate*/ ÚdajeUdalostí() {}


	/**
	 * <p>Ak je aktívny {@linkplain Svet#spustiČasovač(double) časovač},
	 * tak vráti objekt s informáciami o poslednej udalosti časovača.
	 * Využiteľné v reakcii {@link ObsluhaUdalostí#tik()
	 * ObsluhaUdalostí.tik()}.</p>
	 * 
	 * @return objekt typu {@link ActionEvent ActionEvent}
	 *     s informáciami o poslednej udalosti časovača
	 */
	public static ActionEvent tik() { return poslednýTik; }


	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti okna.
	 * Využiteľné v metódach {@link ObsluhaUdalostí#zobrazenieOkna()
	 * zobrazenieOkna}, {@link ObsluhaUdalostí#skrytieOkna()
	 * skrytieOkna}, {@link ObsluhaUdalostí#presunutieOkna()
	 * presunutieOkna} alebo {@link ObsluhaUdalostí#zmenaVeľkostiOkna()
	 * zmenaVeľkostiOkna}.</p>
	 * 
	 * @return objekt typu {@link ComponentEvent
	 *     ComponentEvent} s informáciami o poslednej udalosti okna
	 */
	public static ComponentEvent okno() { return poslednáUdalosťOkna; }

	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti okna súvisiacej
	 * s jeho {@linkplain ObsluhaUdalostí#aktiváciaOkna()
	 * aktiváciou}, {@linkplain ObsluhaUdalostí#deaktiváciaOkna()
	 * deaktiváciou}, {@linkplain ObsluhaUdalostí#maximalizovanie()
	 * maximalizáciou}, {@linkplain ObsluhaUdalostí#minimalizovanie()
	 * minimalizáciou}, {@linkplain ObsluhaUdalostí#obnovenie()
	 * obnovením}, {@linkplain ObsluhaUdalostí#otvorenie()
	 * otvorením} alebo {@linkplain ObsluhaUdalostí#zavretie()
	 * zavretím}.
	 * Využiteľné v metódach {@link ObsluhaUdalostí#aktiváciaOkna()
	 * aktiváciaOkna}, {@link ObsluhaUdalostí#deaktiváciaOkna()
	 * deaktiváciaOkna}, {@link ObsluhaUdalostí#maximalizovanie()
	 * maximalizovanie}, {@link ObsluhaUdalostí#minimalizovanie()
	 * minimalizovanie}, {@link ObsluhaUdalostí#obnovenie()
	 * obnovenie}, {@link ObsluhaUdalostí#otvorenie()
	 * otvorenie} alebo {@link ObsluhaUdalostí#zavretie()
	 * zavretie}.</p>
	 * 
	 * @return objekt typu {@link WindowEvent WindowEvent} s informáciami
	 *     o poslednej udalosti okna súvisiacej s jeho {@linkplain 
	 *     ObsluhaUdalostí#aktiváciaOkna() aktiváciou}, {@linkplain 
	 *     ObsluhaUdalostí#deaktiváciaOkna() deaktiváciou}, {@linkplain 
	 *     ObsluhaUdalostí#maximalizovanie() maximalizáciou}, {@linkplain 
	 *     ObsluhaUdalostí#minimalizovanie() minimalizáciou}, {@linkplain 
	 *     ObsluhaUdalostí#obnovenie() obnovením}, {@linkplain 
	 *     ObsluhaUdalostí#otvorenie() otvorením} alebo {@linkplain 
	 *     ObsluhaUdalostí#zavretie() zavretím}
	 */
	public static WindowEvent aktivitaOkna()
	{ return poslednáUdalosťAktivityOkna; }


	/**
	 * <p>Vráti objekt {@link PoložkaPonuky PoložkaPonuky} s naposledy
	 * zvolenou položkou ponuky. Využiteľné v metóde {@link 
	 * ObsluhaUdalostí#voľbaPoložkyPonuky() voľbaPoložkyPonuky}.</p>
	 * 
	 * @return objekt typu {@link PoložkaPonuky PoložkaPonuky} naposledy
	 *     zvolenej položky ponuky
	 */
	public static PoložkaPonuky položkaPonuky() { return poslednáPoložkaPonuky; }

	/** <p><a class="alias"></a> Alias pre {@link #položkaPonuky() položkaPonuky}.</p> */
	public static PolozkaPonuky polozkaPonuky() { return (PolozkaPonuky)poslednáPoložkaPonuky; }


	/**
	 * <p>Vráti objekt {@link KontextováPoložka KontextováPoložka}
	 * s naposledy zvolenou kontextovou položkou. Využiteľné v metóde
	 * {@link ObsluhaUdalostí#voľbaKontextovejPoložky()
	 * voľbaKontextovejPoložky}.</p>
	 * 
	 * @return objekt typu {@link KontextováPoložka KontextováPoložka}
	 *     naposledy zvolenej kontextovej položky
	 */
	public static KontextováPoložka kontextováPoložka() { return poslednáKontextováPoložka; }

	/** <p><a class="alias"></a> Alias pre {@link #kontextováPoložka() kontextováPoložka}.</p> */
	public static KontextovaPolozka kontextovaPolozka() { return (KontextovaPolozka)poslednáKontextováPoložka; }


	/**
	 * <p>Vráti objekt {@link Tlačidlo Tlačidlo} naposledy
	 * zvoleného tlačidla. Využiteľné v metóde {@link 
	 * ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}.
	 * Praktický príklad nájdete v opise triedy {@link Tlačidlo
	 * Tlačidlo}.</p>
	 * 
	 * @return objekt typu {@link Tlačidlo Tlačidlo} naposledy
	 *     zvoleného tlačidla
	 */
	public static Tlačidlo tlačidlo() { return poslednéTlačidlo; }

	/** <p><a class="alias"></a> Alias pre {@link #tlačidlo() tlačidlo}.</p> */
	public static Tlacidlo tlacidlo() { return (Tlacidlo)poslednéTlačidlo; }


	/**
	 * <p>Vráti objekt {@link RolovaciaLišta RolovaciaLišta} naposledy
	 * posunutej rolovacej lišty. Využiteľné v metóde {@link 
	 * ObsluhaUdalostí#zmenaPosunuLišty() zmenaPosunuLišty}.
	 * Praktický príklad nájdete v opise triedy {@link RolovaciaLišta
	 * RolovaciaLišta}.</p>
	 * 
	 * @return objekt typu {@link RolovaciaLišta RolovaciaLišta} naposledy
	 *     posunutej rolovacej lišty
	 */
	public static RolovaciaLišta rolovaciaLišta()
	{ return poslednáRolovaciaLišta; }

	/** <p><a class="alias"></a> Alias pre {@link #rolovaciaLišta() rolovaciaLišta}.</p> */
	public static RolovaciaLista rolovaciaLista()
	{ return (RolovaciaLista)poslednáRolovaciaLišta; }


	/**
	 * <p>Vráti hodnotu posledného rolovania rolovacej lišty.
	 * To je využiteľné v metóde {@link ObsluhaUdalostí#zmenaPosunuLišty()
	 * zmenaPosunuLišty}.</p>
	 * 
	 * @return hodnota posledného posunu rolovacej lišty
	 */
	public static int posunRolovacejLišty()
	{ return poslednýPosunRolovacejLišty; }

	/** <p><a class="alias"></a> Alias pre {@link #posunRolovacejLišty() posunRolovacejLišty}.</p> */
	public static int posunRolovacejListy()
	{ return poslednýPosunRolovacejLišty; }


	/**
	 * <p>Vráti objekt poslednej udalosti posunu rolovacej lišty
	 * ({@link AdjustmentEvent AdjustmentEvent}). Objekt je využiteľný
	 * v metóde {@link ObsluhaUdalostí#zmenaPosunuLišty()
	 * zmenaPosunuLišty}.</p>
	 * 
	 * @return objekt poslednej udalosti posunu rolovacej lišty
	 */
	public static AdjustmentEvent udalosťPosunu()
	{ return poslednáUdalosťPosunu; }

	/** <p><a class="alias"></a> Alias pre {@link #udalosťPosunu() udalosťPosunu}.</p> */
	public static AdjustmentEvent udalostPosunu()
	{ return poslednáUdalosťPosunu; }


	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti {@linkplain 
	 * Svet#systémováIkona(String, Image, String...) systémovej ikony}
	 * alebo položky jej kontextovej ponuky. Využiteľné v metódach {@link 
	 * ObsluhaUdalostí#voľbaSystémovejIkony() voľbaSystémovejIkony}
	 * a {@link ObsluhaUdalostí#voľbaSystémovejPoložky()
	 * voľbaSystémovejPoložky}.</p>
	 * 
	 * @return objekt typu {@link ActionEvent ActionEvent} s informáciami
	 *     o poslednej udalosti systémovej ikony
	 */
	public static ActionEvent udalosťSystémovejIkony()
	{ return poslednáUdalosťSystémovejIkony; }

	/** <p><a class="alias"></a> Alias pre {@link #udalosťSystémovejIkony() udalosťSystémovejIkony}.</p> */
	public static ActionEvent udalostSystemovejIkony()
	{ return poslednáUdalosťSystémovejIkony; }

	/**
	 * <p>Vráti index naposledy zvolenej položky kontextovej
	 * ponuky {@linkplain Svet#systémováIkona(String, Image, String...)
	 * systémovej ikony}. Využiteľné v metóde
	 * a {@link ObsluhaUdalostí#voľbaSystémovejPoložky()
	 * voľbaSystémovejPoložky}. Ak kontextová ponuka nejestvuje,
	 * metóda vráti hodnotu −1. Index zahŕňa aj prípadné oddeľovače.</p>
	 * 
	 * @return index naposledy zvolenej položky kontextovej ponuky
	 *     systémovej ikony
	 */
	public static int položkaSystémovejPonuky()
	{ return poslednáPoložkaSystémovejPonuky; }

	/** <p><a class="alias"></a> Alias pre {@link #položkaSystémovejPonuky() položkaSystémovejPonuky}.</p> */
	public static int polozkaSystemovejPonuky()
	{ return poslednáPoložkaSystémovejPonuky; }


	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti klávesnice.
	 * Využiteľné napríklad v metódach {@link 
	 * ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu} alebo {@link 
	 * ObsluhaUdalostí#uvoľnenieKlávesu() uvoľnenieKlávesu}.</p>
	 * 
	 * @return objekt typu {@link KeyEvent KeyEvent}
	 *     s informáciami o poslednej udalosti klávesnice
	 * 
	 * @see #kláves()
	 * @see #kláves(int)
	 * @see #znak()
	 * @see #znak(char)
	 */
	public static KeyEvent klávesnica() { return poslednáUdalosťKlávesnice; }

	/** <p><a class="alias"></a> Alias pre {@link #klávesnica() klávesnica}.</p> */
	public static KeyEvent klavesnica() { return poslednáUdalosťKlávesnice; }

	/**
	 * <p>Overí kód klávesu poslednej udalosti klávesnice.</p>
	 * 
	 * @param kód kód klávesu – použiteľné sú kódy definované v triede
	 *     {@link Kláves Kláves} alebo {@link KeyEvent KeyEvent}
	 * @return {@code valtrue} ak sa zadaný {@code kód} zhoduje s kódom
	 *     klávesu naposledy vzniknutej udalosti {@linkplain 
	 *     #klávesnica() klávesnice}
	 * 
	 * @see #klávesnica()
	 * @see #kláves()
	 */
	public static boolean kláves(int kód)
	{ return poslednáUdalosťKlávesnice.getKeyCode() == kód; }

	/** <p><a class="alias"></a> Alias pre {@link #kláves(int) kláves}.</p> */
	public static boolean klaves(int kód)
	{ return poslednáUdalosťKlávesnice.getKeyCode() == kód; }

	/**
	 * <p>Vráti kód klávesu poslednej udalosti klávesnice.</p>
	 * 
	 * @return kód klávesu naposledy vzniknutej udalosti {@linkplain 
	 *     #klávesnica() klávesnice} – kódy sú zhodné s kódmi
	 *     definovanými v triede {@link Kláves Kláves} alebo {@link 
	 *     KeyEvent KeyEvent}
	 * 
	 * @see #klávesnica()
	 * @see #kláves(int)
	 */
	public static int kláves()
	{ return poslednáUdalosťKlávesnice.getKeyCode(); }

	/** <p><a class="alias"></a> Alias pre {@link #kláves() kláves}.</p> */
	public static int klaves()
	{ return poslednáUdalosťKlávesnice.getKeyCode(); }

	/**
	 * <p>Overí či pri poslednej udalosti klávesnice bol zadaný určený
	 * {@code znak}.</p>
	 * 
	 * @param znak znak, ktorého zhodu chceme overiť
	 * @return {@code valtrue} ak sa zadaný {@code znak} zhoduje so
	 *     znakom zadaným pri poslednej vzniknutej udalosti {@linkplain 
	 *     #klávesnica() klávesnice}
	 * 
	 * @see #klávesnica()
	 * @see #znak()
	 */
	public static boolean znak(char znak)
	{ return poslednáUdalosťKlávesnice.getKeyChar() == znak; }

	/**
	 * <p>Vráti znak zadaný pri poslednej udalosti klávesnice.</p>
	 * 
	 * @return znak naposledy registrovanej udalosti {@linkplain 
	 *     #klávesnica() klávesnice}
	 * 
	 * @see #klávesnica()
	 * @see #znak(char)
	 */
	public static char znak()
	{ return poslednáUdalosťKlávesnice.getKeyChar(); }


	/**
	 * <p>Vráti príkaz poslednej udalosti klávesovej skratky.
	 * Využiteľné v metóde {@link ObsluhaUdalostí#klávesováSkratka()
	 * klávesováSkratka}.</p>
	 * 
	 * @return príkaz poslednej udalosti klávesovej skratky
	 */
	public static String príkazSkratky() { return poslednýPríkazSkratky; }

	/** <p><a class="alias"></a> Alias pre {@link #príkazSkratky() príkazSkratky}.</p> */
	public static String prikazSkratky() { return poslednýPríkazSkratky; }

	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti klávesovej skratky.
	 * Využiteľné v metóde {@link ObsluhaUdalostí#klávesováSkratka()
	 * klávesováSkratka}.</p>
	 * 
	 * @return objekt typu {@link ActionEvent ActionEvent} s informáciami
	 *     o poslednej udalosti klávesovej skratky
	 */
	public static ActionEvent udalosťSkratky()
	{ return poslednáUdalosťSkratky; }

	/** <p><a class="alias"></a> Alias pre {@link #udalosťSkratky() udalosťSkratky}.</p> */
	public static ActionEvent udalostSkratky()
	{ return poslednáUdalosťSkratky; }


	/**
	 * <p>Vráti objekt s informáciami o poslednej udalosti myši.
	 * Využiteľné v metódach {@link ObsluhaUdalostí#klik() klik}
	 * {@link ObsluhaUdalostí#stlačenieTlačidlaMyši()
	 * stlačenieTlačidlaMyši},
	 * {@link ObsluhaUdalostí#uvoľnenieTlačidlaMyši()
	 * uvoľnenieTlačidlaMyši},
	 * {@link ObsluhaUdalostí#pohybMyši() pohybMyši}
	 * a {@link ObsluhaUdalostí#ťahanieMyšou() ťahanieMyšou}.</p>
	 * 
	 * @return objekt typu {@link MouseEvent MouseEvent}
	 *     s informáciami o poslednej udalosti myši
	 * 
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 * @see #súradnicaMyšiX()
	 * @see #súradnicaMyšiY()
	 */
	public static MouseEvent myš() { return poslednáUdalosťMyši; }

	/** <p><a class="alias"></a> Alias pre {@link #myš() myš}.</p> */
	public static MouseEvent mys() { return poslednáUdalosťMyši; }

	/**
	 * <p>Vráti spresnenie objektu s informáciami o poslednej udalosti myši,
	 * ktorá sa dotýkala rolovania kolieskom myši. Táto udalosť je
	 * rozšírením udalosti {@link MouseEvent MouseEvent}, ktorú je možné
	 * získať metódou {@link #myš() myš}. Informácie sú využiteľné
	 * v metóde {@link ObsluhaUdalostí#rolovanieKolieskomMyši()
	 * rolovanieKolieskomMyši}.</p>
	 * 
	 * <p>Nasledujúci príklad ukazuje, ako sa dajú na rozhýbanie robota
	 * použiť metódy {@link #rolovanieKolieskomMyšiX()
	 * rolovanieKolieskomMyšiX} a {@link #rolovanieKolieskomMyšiY()
	 * rolovanieKolieskomMyšiY} (ak nemáte horizontálne koliesko alebo
	 * zariadenie, ktoré umožňuje horizontálne rolovanie, skúste použiť
	 * s vertikálnym kolieskom kláves {@code Shift}):</p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.{@link GRobot GRobot};

		{@code kwdpublic} {@code typeclass} RolovanieKolieskom {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} {@code typeint} Δx = {@code num0};
			{@code kwdprivate} {@code typeint} Δy = {@code num0};

			{@code kwdprivate} RolovanieKolieskom()
			{
				{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
				{
					{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
					{
						{@link GRobot#otoč(double, double) otoč}(Δx, Δy);
						{@link GRobot#choď(double, double) choď}(Δx, Δy);
						Δx = Δy = {@code num0};
					}

					{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#rolovanieKolieskomMyši() rolovanieKolieskomMyši}()
					{
						Δx += {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#rolovanieKolieskomMyšiX() rolovanieKolieskomMyšiX}();
						Δy += {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#rolovanieKolieskomMyšiY() rolovanieKolieskomMyšiY}();
					}
				};

				<!-- TODO overiť, či tento príklad funguje ako má – s dutým tvarom – niekde bol aj príklad, kde dutý tvar znamenal zmenu režimu… nájsť a overiť, či je správny… {@link GRobot#predvolenýTvar(boolean) predvolenýTvar}({@code valfalse});-->
				{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@code kwdnew} RolovanieKolieskom();
			}
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <table class="centered"><tr>
	 * <td><image>rolovanieKolieskomWin.png<alt/></image></td>
	 * <td rowspan="2"> </td>
	 * <td><image>rolovanieKolieskomMac.png<alt/></image></td></tr><tr>
	 * <td><p class="image">Pokus o nakreslenie obrázka iba
	 * s použitím<br />rolovania kolieska myši na OS
	 * Windows.</p></td><td><p class="image">Pokus o nakreslenie obrázka
	 * iba s použitím<br />rolovania kolieska myši na macOS (predtým OS X
	 * a Mac OS).</p></td></tr></table>
	 * 
	 * @return spresnený objekt typu {@link MouseWheelEvent
	 *     MouseWheelEvent} s informáciami o poslednej udalosti myši,
	 *     ktorá sa dotýkala rolovania kolieskom myši
	 * 
	 * @see #myš()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 * @see #súradnicaMyšiX()
	 * @see #súradnicaMyšiY()
	 * @see #rolovanieKolieskomMyšiX()
	 * @see #rolovanieKolieskomMyšiY()
	 */
	public static MouseWheelEvent kolieskoMyši() { return poslednáUdalosťRolovania; }

	/** <p><a class="alias"></a> Alias pre {@link #kolieskoMyši() kolieskoMyši}.</p> */
	public static MouseWheelEvent kolieskoMysi() { return poslednáUdalosťRolovania; }

	/**
	 * <p>Vráti počet jednotiek rolovania kolieskom myši v smere x.
	 * Volanie tejto metódy má význam len v reakcii {@link 
	 * ObsluhaUdalostí#rolovanieKolieskomMyši() rolovanieKolieskomMyši}
	 * obsluhy udalostí. Kladná hodnota znamená doprava, záporná doľava.</p>
	 * 
	 * <p>Príklad použitia tejto metódy sa nachádza pri opise metódy
	 * {@link #kolieskoMyši()}.</p>
	 * 
	 * @return počet jednotiek rolovania kolieskom myši v smere x;
	 *     kladná hodnota znamená doprava, záporná doľava
	 * 
	 * @see #kolieskoMyši()
	 * @see ObsluhaUdalostí#rolovanieKolieskomMyši()
	 */
	public static int rolovanieKolieskomMyšiX()
	{
		synchronized (zámokMyši)
		{
			return rolovanieKolieskomMyšiX;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #rolovanieKolieskomMyšiX() rolovanieKolieskomMyšiX}.</p> */
	public static int rolovanieKolieskomMysiX() { return rolovanieKolieskomMyšiX(); }

	/**
	 * <p>Vráti počet jednotiek rolovania kolieskom myši v smere osi y.
	 * Volanie tejto metódy má význam len v reakcii {@link 
	 * ObsluhaUdalostí#rolovanieKolieskomMyši() rolovanieKolieskomMyši}
	 * obsluhy udalostí. Kladná hodnota znamená hore, záporná dole, čo
	 * je v súlade so súradnicovým priestorom rámca, ale v protiklade
	 * s hodnotou vrátenou systémom (metódou {@link 
	 * ÚdajeUdalostí#kolieskoMyši() kolieskoMyši()}{@code .}{@link 
	 * MouseWheelEvent#getWheelRotation() getWheelRotation()}).</p>
	 * 
	 * <p>Príklad použitia tejto metódy sa nachádza pri opise metódy
	 * {@link #kolieskoMyši()}.</p>
	 * 
	 * @return počet jednotiek rolovania kolieskom myši v smere y;
	 *     kladná hodnota znamená hore, záporná dole (v súlade so
	 *     súradnicovým priestorom rámca)
	 * 
	 * @see #kolieskoMyši()
	 * @see ObsluhaUdalostí#rolovanieKolieskomMyši()
	 */
	public static int rolovanieKolieskomMyšiY()
	{
		synchronized (zámokMyši)
		{
			return rolovanieKolieskomMyšiY;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #rolovanieKolieskomMyšiY() rolovanieKolieskomMyšiY}.</p> */
	public static int rolovanieKolieskomMysiY() { return rolovanieKolieskomMyšiY(); }

	/**
	 * <p>Vráti hodnotu x-ovej súradnice myši prepočítanú do súradníc
	 * plátna. Dáva presnejší výsledok ako súradnice získané cez metódy
	 * {@link #myš() ÚdajeUdalostí.myš()}.{@link MouseEvent#getX()
	 * getX()} a {@link #myš() ÚdajeUdalostí.myš()}.{@link 
	 * MouseEvent#getY() getY()}.</p>
	 * 
	 * @return hodnota x-ovej súradnice myši
	 * 
	 * @see #myš()
	 * @see #súradnicaMyšiY()
	 */
	public static double súradnicaMyšiX()
	{
		synchronized (zámokMyši)
		{
			return súradnicaMyšiX;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaMyšiX() súradnicaMyšiX}.</p> */
	public static double suradnicaMysiX() { return súradnicaMyšiX(); }

	/**
	 * <p>Vráti hodnotu y-ovej súradnice myši prepočítanú do súradníc
	 * plátna. Dáva presnejší výsledok ako súradnice získané cez metódy
	 * {@link #myš() ÚdajeUdalostí.myš()}.{@link MouseEvent#getX()
	 * getX()} a {@link #myš() ÚdajeUdalostí.myš()}.{@link 
	 * MouseEvent#getY() getY()}.</p>
	 * 
	 * @return hodnota y-ovej súradnice myši
	 * 
	 * @see #myš()
	 * @see #súradnicaMyšiX()
	 */
	public static double súradnicaMyšiY()
	{
		synchronized (zámokMyši)
		{
			return súradnicaMyšiY;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaMyšiY() súradnicaMyšiY}.</p> */
	public static double suradnicaMysiY() { return súradnicaMyšiY(); }

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaMyšiX() súradnicaMyšiX}.</p> */
	public static double polohaMyšiX() { return súradnicaMyšiX(); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaMyšiX() polohaMyšiX}.</p> */
	public static double polohaMysiX() { return súradnicaMyšiX(); }

	/** <p><a class="alias"></a> Alias pre {@link #súradnicaMyšiY() súradnicaMyšiY}.</p> */
	public static double polohaMyšiY() { return súradnicaMyšiY(); }

	/** <p><a class="alias"></a> Alias pre {@link #polohaMyšiY() polohaMyšiY}.</p> */
	public static double polohaMysiY() { return súradnicaMyšiY(); }

	/**
	 * <p>Vráti polohu kurzora myši prepočítanú do súradníc plátna.</p>
	 * 
	 * @return aktuálna poloha kurzora myši
	 * 
	 * @see #súradnicaMyšiX()
	 * @see #súradnicaMyšiY()
	 */
	public static Bod polohaMyši()
	{
		return new Bod(súradnicaMyšiX, súradnicaMyšiY);
	}

	/** <p><a class="alias"></a> Alias pre {@link #polohaMyši() polohaMyši}.</p> */
	public static Bod polohaMysi()
	{
		return polohaMyši();
	}


	/**
	 * <p>Je stlačené prvé tlačidlo myši?</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse} – podľa toho, či je
	 *     tlačidlo stlačené alebo nie
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 */
	public static boolean tlačidloMyši1()
	{
		synchronized (zámokMyši)
		{
			return tlačidloMyši1;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyši1() tlačidloMyši1}.</p> */
	public static boolean tlacidloMysi1() { return tlačidloMyši1(); }

	/**
	 * <p>Je stlačené druhé tlačidlo myši?</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse} – podľa toho, či je
	 *     tlačidlo stlačené alebo nie
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 */
	public static boolean tlačidloMyši2()
	{
		synchronized (zámokMyši)
		{
			return tlačidloMyši2;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyši2() tlačidloMyši2}.</p> */
	public static boolean tlacidloMysi2() { return tlačidloMyši2(); }

	/**
	 * <p>Je stlačené tretie tlačidlo myši?</p>
	 * 
	 * @return {@code valtrue}/&#8203;{@code valfalse} – podľa toho, či je
	 *     tlačidlo stlačené alebo nie
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 */
	public static boolean tlačidloMyši3()
	{
		synchronized (zámokMyši)
		{
			return tlačidloMyši3;
		}
	}


	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyši3() tlačidloMyši3}.</p> */
	public static boolean tlacidloMysi3() { return tlačidloMyši3(); }

	/**
	 * <p>Vráti informáciu o stave požadovaného tlačidla myši. Ak je
	 * požadované tlačidlo dole (stlačené), je návratová hodnota {@code 
	 * valtrue}, inak {@code valfalse}.</p>
	 * 
	 * @param ktoré poradové číslo tlačidla myši: 1 až 3 – {@link 
	 *     ÚdajeUdalostí#ĽAVÉ ĽAVÉ}, {@link ÚdajeUdalostí#STREDNÉ STREDNÉ},
	 *     {@link ÚdajeUdalostí#PRAVÉ PRAVÉ}
	 * @return {@code valtrue} ak je stanovené tlačidlo stlačené,
	 *     v opačnom prípade {@code valfalse}; ak je parameter
	 *     {@code ktoré} mimo povolených hodnôt (1 – 3), metóda vráti
	 *     {@code valfalse}
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 */
	public static boolean tlačidloMyšiDole(int ktoré)
	{
		synchronized (zámokMyši)
		{
			switch (ktoré)
			{
			case ĽAVÉ: return tlačidloMyši1;
			case STREDNÉ: return tlačidloMyši2;
			case PRAVÉ: return tlačidloMyši3;
			}
		}
		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiDole(int) tlačidloMyšiDole}.</p> */
	public static boolean tlacidloMysiDole(int ktoré)
	{ return tlačidloMyšiDole(ktoré); }

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiDole(int) tlačidloMyšiDole}.</p> */
	public static boolean tlačidloMyšiStlačené(int ktoré)
	{ return tlačidloMyšiDole(ktoré); }

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiDole(int) tlačidloMyšiDole}.</p> */
	public static boolean tlacidloMysiStlacene(int ktoré)
	{ return tlačidloMyšiDole(ktoré); }

	/**
	 * <p>Vráti informáciu o stave požadovaného tlačidla myši. Ak je
	 * požadované tlačidlo hore (uvoľnené), je návratová hodnota {@code 
	 * valtrue}, inak {@code valfalse}.</p>
	 * 
	 * @param ktoré poradové číslo tlačidla myši: 1 až 3 – {@link 
	 *     ÚdajeUdalostí#ĽAVÉ ĽAVÉ}, {@link ÚdajeUdalostí#STREDNÉ STREDNÉ},
	 *     {@link ÚdajeUdalostí#PRAVÉ PRAVÉ}
	 * @return {@code valtrue} ak je stanovené tlačidlo uvoľnené,
	 *     v opačnom prípade {@code valfalse}; ak je parameter
	 *     {@code ktoré} mimo povolených hodnôt (1 – 3), metóda vráti
	 *     {@code valtrue}
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyši()
	 * @see #tlačidloMyši(int)
	 */
	public static boolean tlačidloMyšiHore(int ktoré)
	{
		synchronized (zámokMyši)
		{
			switch (ktoré)
			{
			case ĽAVÉ: return tlačidloMyši1;
			case STREDNÉ: return tlačidloMyši2;
			case PRAVÉ: return tlačidloMyši3;
			}
		}
		return true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiHore(int) tlačidloMyšiHore}.</p> */
	public static boolean tlacidloMysiHore(int ktoré)
	{ return tlačidloMyšiHore(ktoré); }

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiHore(int) tlačidloMyšiHore}.</p> */
	public static boolean tlačidloMyšiUvoľnené(int ktoré)
	{ return tlačidloMyšiHore(ktoré); }

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyšiHore(int) tlačidloMyšiHore}.</p> */
	public static boolean tlacidloMysiUvolnene(int ktoré)
	{ return tlačidloMyšiHore(ktoré); }

	/**
	 * <p>Vráti poradové číslo tlačidla myši, s ktorým bolo naposledy
	 * manipulované. (Či už bolo stlačené alebo uvoľnené.)</p>
	 * 
	 * @return 0 – so žiadnym; 1 až 3 – {@link ÚdajeUdalostí#ĽAVÉ ĽAVÉ},
	 *     {@link ÚdajeUdalostí#STREDNÉ STREDNÉ}, {@link ÚdajeUdalostí#PRAVÉ
	 *     PRAVÉ}
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši(int)
	 */
	public static int tlačidloMyši()
	{
		synchronized (zámokMyši)
		{
			return tlačidloMyši;
		}
	}


	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyši() tlačidloMyši}.</p> */
	public static int tlacidloMysi()
	{ return tlačidloMyši(); }

	/**
	 * <p>Overí, či bolo naposledy manipulované (či bolo stlačené alebo
	 * uvoľnené) s tlačidlom myši so zadaným poradovým číslom.</p>
	 * 
	 * @param ktoré 0 – žiadne; 1 až 3 – {@link ÚdajeUdalostí#ĽAVÉ ĽAVÉ},
	 *     {@link ÚdajeUdalostí#STREDNÉ STREDNÉ}, {@link ÚdajeUdalostí#PRAVÉ
	 *     PRAVÉ}
	 * 
	 * @see #myš()
	 * @see #kolieskoMyši()
	 * @see #tlačidloMyši1()
	 * @see #tlačidloMyši2()
	 * @see #tlačidloMyši3()
	 * @see #tlačidloMyšiDole(int)
	 * @see #tlačidloMyšiHore(int)
	 * @see #tlačidloMyši()
	 */
	public static boolean tlačidloMyši(int ktoré)
	{
		synchronized (zámokMyši)
		{
			return ktoré == tlačidloMyši;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačidloMyši(int) tlačidloMyši}.</p> */
	public static boolean tlacidloMysi(int ktoré)
	{ return tlačidloMyši(ktoré); }


	/**
	 * <p>Vráti posledné (aktuálne) plátno, ktorého grafika bola vymazaná.
	 * Táto informácia je aktualizovaná vždy a výhradne pred volaním
	 * reakcie {@link ObsluhaUdalostí#vymazanie() vymazanie} pre ktorú
	 * je táto informácia primárne určená. Odporúča sa použiť túto metódu
	 * práve v nej.</p>
	 * 
	 * @return {@linkplain Plátno plátno}, ktorého grafika bola naposledy
	 *     vymazaná ({@link GRobot#podlaha podlaha} alebo {@link GRobot#strop
	 *     strop})
	 * 
	 * @see ObsluhaUdalostí#vymazanie()
	 */
	public static Plátno vymazanéPlátno()
	{ return poslednéVymazanéPlátno; }

	/** <p><a class="alias"></a> Alias pre {@link #vymazanéPlátno() vymazanéPlátno}.</p> */
	public static Plátno vymazanePlatno()
	{ return poslednéVymazanéPlátno; }

	/**
	 * <p>Overí, či zadané plátno ({@link GRobot#podlaha podlaha} alebo
	 * {@link GRobot#strop strop}) bolo posledným vymazaným plátnom.
	 * Informácia o tom, ktoré plátno bolo vymazané, je aktualizovaná
	 * vždy a výhradne pred volaním reakcie
	 * {@link ObsluhaUdalostí#vymazanie() vymazanie} pre ktorú je táto
	 * informácia primárne určená. Odporúča sa použiť túto metódu práve
	 * v nej.</p>
	 * 
	 * @param plátno {@linkplain Plátno plátno} na overenie toho, či
	 *     práve jeho grafila bola naposledy vymazaná
	 * @return {@code valtrue} ak sa zadané plátno zhoduje s tým, ktoré
	 *     bolo vymazané naposledy
	 * 
	 * @see ObsluhaUdalostí#vymazanie()
	 */
	public static boolean vymazanéPlátno(Plátno plátno)
	{ return plátno == poslednéVymazanéPlátno; }

	/** <p><a class="alias"></a> Alias pre {@link #vymazanéPlátno(Plátno) vymazanéPlátno}.</p> */
	public static boolean vymazanePlatno(Plátno plátno)
	{ return plátno == poslednéVymazanéPlátno; }


	/**
	 * <p>Vráti text cieľa (adresu) posledného aktivovaného odkazu
	 * poznámkového bloku. To je yužiteľné v metóde {@link 
	 * ObsluhaUdalostí#aktiváciaOdkazu() aktiváciaOdkazu}.</p>
	 * 
	 * @return text cieľa (adresa) naposledy aktivovaného hypertextového
	 *     odkazu v niektorom poznámkovom bloku
	 */
	public static String poslednýOdkaz() { return poslednýOdkaz; }

	/** <p><a class="alias"></a> Alias pre {@link #poslednýOdkaz() poslednýOdkaz}.</p> */
	public static String poslednyOdkaz() { return poslednýOdkaz; }


	/**
	 * <p>Vráti objekt toho {@linkplain PoznámkovýBlok poznámkového
	 * bloku}, v ktorom bol naposledy aktivovaný (hypertextový) odkaz.
	 * To je využiteľné napríklad v metóde {@link 
	 * ObsluhaUdalostí#aktiváciaOdkazu() aktiváciaOdkazu}.</p>
	 * 
	 * @return text naposledy aktivovaného hypertextového odkazu
	 *     v niektorom poznámkovom bloku
	 */
	public static PoznámkovýBlok poslednýPoznámkovýBlok()
	{ return poslednýPoznámkovýBlok; }

	/** <p><a class="alias"></a> Alias pre {@link #poslednýPoznámkovýBlok() poslednýPoznámkovýBlok}.</p> */
	public static PoznámkovýBlok poslednyPoznamkovyBlok()
	{ return poslednýPoznámkovýBlok; }
}
