
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.MenuItem; // len skrz systémovúIkonu (SystemTray>TrayIcon)
import java.awt.Point;
import java.awt.PopupMenu; // len skrz systémovúIkonu (SystemTray>TrayIcon)
import java.awt.Shape;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.net.URI;
import java.net.URLEncoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import java.util.Vector;

import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import javax.imageio.ImageIO;


import knižnica.podpora.BeepChannel;
import knižnica.podpora.ExecuteShellCommand;
import knižnica.podpora.ReadStandardInput;
import static knižnica.Farebnosť.*;

import static knižnica.Konštanty.majorVersion;
import static knižnica.Konštanty.minorVersion;
import static knižnica.Konštanty.versionString;
import static knižnica.Konštanty.riadok;

import static knižnica.Konštanty.CHYBA_VOLANIA_SKRIPTU;
import static knižnica.Konštanty.CHYBA_ČÍTANIA_SKRIPTU;
import static knižnica.Konštanty.CHYBA_NEZNÁMY_PRÍKAZ;
import static knižnica.Konštanty.SPÔSOB_DEAKTIVÁCIA;
import static knižnica.Konštanty.UKONČENIE_CHYBOU;
import static knižnica.Konštanty.UKONČENIE_SKRIPTU;
import static knižnica.Konštanty.VYKONAŤ_PRÍKAZ;
import static knižnica.Konštanty.VYPÍSAŤ_PRÍKAZ;
import static knižnica.Konštanty.ŽIADNA_CHYBA;


// --------------------- //
//  *** Trieda Svet ***  //
// --------------------- //

/*
	Organizácia metód

		vlastnosti sveta a funkcie sveta
			verzia
			hlavný robot a všetci roboti
			rozmery
			rozmery a poloha hlavného okna
			titulok
			okraje
			viditeľnosť, upevnenie a zbalenie
			vzdialenosti
			uhly – smery
			vymazanie
			viacnásobná obsluha udalostí
			výplň
			parametre obrázkovej výplne
			kreslenie tvarov
			priehľadnosť plátien
			automatické prekresľovanie
			hlavná ponuka
			text na riadky
			formát
			ikony, kurzory a ukončenie
			systémové

		vstupy, výstupy a náhodné čísla
			čítanie údajov cez dialógy
			úprava údajov cez dialógy
			dialógy – správy/otázky
			úvodná obrazovka
			vstupný riadok
			štandardný vstup
			interaktívny režim
			vnútorná konzola
			náhodné čísla
			schránka

		obrázky
			výška/šírka (obrázka)
			čítanie
			kreslenie
			ukladanie

		farba a písmo
			farba textu
			farba pozadia textov
			farba pozadia (sveta)
			farba plochy (sveta)
			farba bodu
			písmo

		zvuky
			čítanie
			prehrávanie
			hromadná úprava vlastností
			pípnutie
			generovanie tónov

		časovač a časomiera
			práca s časovačom
			čakanie
			časomiera

		interpolácie a ďalšia geometria

		celá obrazovka

		vlnenie

*/

/**
 * <p>Trieda, ktorá spája metódy obsluhujúce hlavné okno so statickými
 * metódami rôzneho významu.</p>
 * 
 * <p>Majiteľom sveta je {@linkplain #hlavnýRobot() hlavný robot}. Je to
 * prvý vytvorený robot, ktorý má o niečo vyššie postavenie oproti
 * ostatným robotom. Poskytuje alternatívu k definovaniu {@linkplain 
 * ObsluhaUdalostí obsluhy udalostí} a implicitne riadi niektoré procesy.</p>
 * 
 * <p>Vo svete grafického robota jestvujú dve kresliace {@linkplain Plátno
 * plátna} (podlaha a strop), na ktoré môžu roboty kresliť. Svet umožňuje
 * (okrem iného) napríklad prácu s {@linkplain #obrázok(String) obrázkami},
 * {@linkplain #zvuk(String) zvukmi}, {@linkplain #zadajReťazec(String)
 * vstupmi} a {@linkplain #vypíšRiadok(Object[]) výstupmi} údajov,
 * {@linkplain #náhodnéReálneČíslo() náhodnými číslami} a {@linkplain 
 * #spustiČasovač(double) časovačom}, ktorý požadujú niektoré
 * {@linkplain GRobot#aktivuj() aktivity} robotov.</p>
 * 
 * <p>Niektoré príkazy sveta sú zhromaždené v nasledujúcom jednoduchom
 * príklade hry na ozvenu – všetko, čo používateľ zadá do vstupného
 * riadka a potvrdí klávesom {@code Enter}, sa zopakuje vypísaním na
 * obrazovku:</p>
 * 
 * <pre CLASS="example">
	{@code currSvet}.{@link #skry() skry}();
	{@code currSvet}.{@link #začniVstup() začniVstup}();
	{@code currSvet}.{@link #neskrývajVstupnýRiadok() neskrývajVstupnýRiadok}();
	{@code currSvet}.{@link #vymažPonuku() vymažPonuku}();
	{@code currSvet}.{@link #zobraz() zobraz}();
	{@code currSvet}.{@link #farbaTextu(Color) farbaTextu}({@link Farebnosť#hnedá hnedá});
	{@code currSvet}.{@link #vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hra na ozvenu…"}, {@link Konštanty#riadok riadok});
	{@code currSvet}.{@link #farbaTextu(Color) farbaTextu}({@link Farebnosť#tmavotyrkysová tmavotyrkysová});

	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov}()
		{
			{@code currSvet}.{@link #vypíšRiadok(Object[]) vypíšRiadok}({@code srg"  Ozvena:"}, {@code currSvet}.{@link #prevezmiReťazec() prevezmiReťazec}());
		}
	};
	</pre>
 * 
 * <p>Nasledujúci rozsiahlejší príklad ukazuje implementáciu primitívneho
 * prehliadača obrázkov:</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} PrehliadačObrázkov {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Názov práve prehliadaného obrázka}
		{@code kwdprivate} {@link String String}   obrázok = {@code valnull};

		{@code comm// Zoznam obrázkov prečítaný z priečinka}
		{@code kwdprivate} {@link String String}[] zoznam  = {@code valnull};

		{@code comm// Index posledného obrázka zobrazeného zo zoznamu}
		{@code kwdprivate} {@code typeint} index = &#45;{@code num1};

		{@code comm// Konštruktor}
		{@code kwdprivate} PrehliadačObrázkov()
		{
			{@code comm// Nastavenie rozmerov plátna na rozmery prvého zobrazovacieho}
			{@code comm// zariadenia (prvého monitora)}
			{@code valsuper}({@code currSvet}.{@link Svet#šírkaZariadenia() šírkaZariadenia}(), {@code currSvet}.{@link Svet#výškaZariadenia() výškaZariadenia}());
		}

		{@code comm// Metóda slúžiaca na upravenie mierky obrázka podľa}
		{@code comm// rozmerov viditeľnej časti plátna}
		{@code kwdpublic} {@code typevoid} upravMierku()
		{
			{@code kwdif} ({@code valnull} != obrázok)
			{
				{@code kwdif} ({@code currSvet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}(obrázok) &gt; {@code currSvet}.{@link Svet#viditeľnáŠírka() viditeľnáŠírka}())
					{@link GRobot#veľkosť(double) veľkosť}({@code currSvet}.{@link Svet#viditeľnáŠírka() viditeľnáŠírka}());
				{@code kwdelse}
					{@link GRobot#veľkosť(double) veľkosť}({@code currSvet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}(obrázok));

				{@code kwdif} ({@link GRobot#veľkosť() veľkosť}() * {@code currSvet}.{@link Svet#výškaObrázka(String) výškaObrázka}(obrázok) /
					{@code currSvet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}(obrázok) &gt; {@code currSvet}.{@link Svet#viditeľnáVýška() viditeľnáVýška}())
					{@link GRobot#veľkosť(double) veľkosť}({@code currSvet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}(obrázok) *
						{@code currSvet}.{@link Svet#viditeľnáVýška() viditeľnáVýška}() / {@code currSvet}.{@link Svet#výškaObrázka(String) výškaObrázka}(obrázok));
			}
		}

		{@code comm// Nastavenie nového prehliadaného obrázka}
		{@code kwdpublic} {@code typeboolean} nastavObrázok({@link String String} názov)
		{
			{@code kwdif} ({@code valnull} != (obrázok = názov))
			{
				názov = názov.{@link String#toLowerCase() toLowerCase}();

				{@code kwdif} (názov.{@link String#endsWith(String) endsWith}({@code srg".png"}) || názov.{@link String#endsWith(String) endsWith}({@code srg".jpeg"}) ||
					názov.{@link String#endsWith(String) endsWith}({@code srg".jpg"}))
				{
					{@link GRobot#veľkosť(double) veľkosť}({@code currSvet}.{@link Svet#šírkaObrázka(String) šírkaObrázka}(obrázok));
					{@link GRobot#vlastnýTvar(String) vlastnýTvar}(obrázok);
					upravMierku();
					{@code kwdreturn} {@code valtrue};
				}
			}

			{@link GRobot#predvolenýTvar(boolean) predvolenýTvar}({@code valfalse});
			{@link GRobot#veľkosť(double) veľkosť}({@code num10});
			{@code kwdreturn} {@code valfalse};
		}

		{@code comm// Metóda slúžiaca na cyklické prehliadanie všetkých obrázkov v priečinku}
		{@code kwdpublic} {@code typevoid} prehliadaj()
		{
			{@code kwdif} ({@code valnull} == zoznam)
				nastavObrázok({@code valnull});
			{@code kwdelse}
			{
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; zoznam.length; ++i)
				{
					{@code kwdif} (++index &gt;= zoznam.length) index = {@code num0};
					{@code kwdif} (nastavObrázok(zoznam[index])) {@code kwdbreak};
				}
			}
		}

		{@code comm// Uvoľnenie ľubovoľného klávesu bude znamenať prehliadanie nasledujúceho}
		{@code comm// obrázka}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieKlávesu() uvoľnenieKlávesu}()
		{
			prehliadaj();
		}

		{@code comm// Zmena veľkosti okna bude mať za následok upravenie mierky prehliadaného}
		{@code comm// obrázka}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#zmenaVeľkostiOkna() zmenaVeľkostiOkna}()
		{
			upravMierku();
		}

		{@code comm// Pustenie súboru (priečinka) nad plátnom bude mať za následok spustenie}
		{@code comm// prehliadania}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#pustenieSúboru(String) pustenieSúboru}({@link String String} súbor)
		{
			{@code comm// Ak je pustený „súbor“ (resp. položka) priečinok, tak sa spustí}
			{@code comm// prehliadanie priečinka (ak táto akcia náhodou zlyhá, tak metóda}
			{@code comm// prehliadaj sa automaticky postará o to, aby bol zobrazený dutý}
			{@code comm// robot s veľkosťou 10)}
			{@code kwdif} ({@link Súbor Súbor}.{@link Súbor#jePriečinok(String) jePriečinok}(súbor))
			{
				{@code kwdtry}
				{
					{@code currSvet}.{@link Svet#priečinokObrázkov(String) priečinokObrázkov}(súbor);
					zoznam = {@link Súbor Súbor}.{@link Súbor#zoznamSúborov(String) zoznamSúborov}(súbor);
				}
				{@code kwdcatch} ({@link Exception Exception} e)
				{
					{@code currSvet}.{@link Svet#správa(String) správa}(e.{@link Exception#getMessage() getMessage}());
					zoznam = {@code valnull};
				}
				prehliadaj();
			}
			{@code comm// V opačnom prípade sa zoznam vymaže a prehliadač sa zadanú položku}
			{@code comm// pokúsi spracovať ako individuálny obrázok (jeho zobrazenie však}
			{@code comm// potrvá len do najbližšieho stlačenia, resp. uvoľnenia, klávesu; na}
			{@code comm// zabezpečenie inteligentnejšieho správania by bolo potrebné}
			{@code comm// implementovať inteligentnejší mechanizmus prehliadania)}
			{@code kwdelse}
			{
				zoznam = {@code valnull};
				nastavObrázok(súbor);
			}
		}

		{@code comm// Hlavná metóda}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code kwdnew} PrehliadačObrázkov();
		}
	}
	</pre>
 * 
 * @see javax.swing.JFrame
 */
@SuppressWarnings("serial")
public final class Svet extends JFrame
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/javax/swing/JFrame.html


	// Predvolená farba pozadia
	private final static Farba predvolenéPozadie = biela;

	// Aktuálna farba pozadia
	/*packagePrivate*/ static Farba farbaPozadia;

	// Počítadlo objektov v interaktívnom režime
	/*packagePrivate*/ static int početVInteraktívnomRežime = 0;


	// Okno režimu celej obrazovky.
	/*packagePrivate*/ static JFrame oknoCelejObrazovky = null;

	// Ikona systémovej oblasti
	private static TrayIcon systémováIkona = null;

	// Príznak uloženia konfigurácie sveta (a plátien)
	private static boolean uložKonfiguráciuSveta = false;

	// Aktívna inštancia v interaktívnom režime
	private static String interaktívnaInštancia = null;

	// Synchronizačný zámok prekresľovania (okolo automatického
	// a programovaného prekresľovania je v tomto programovacom
	// rámci celá „jadrová fyzika“…)
	private final static Object zámokPrekresľovania = new Object();

	// Atribúty udalostí čakania na kláves a klik:
	private static boolean čakajNaKláves = false;
	private static boolean čakajNaKlik = false;
	private static KeyEvent udalosťČakaniaNaKláves = null;
	private static MouseEvent udalosťČakaniaNaKlik = null;


	// -------------------------- //
	//  *** Vlastnosti sveta ***  //
	// -------------------------- //

		// Hlavný robot (prvý robot, ktorý kedysi inicializoval a spravoval
			// grafiku, teraz poskytuje metódy na prekrytie, ktoré sú
			// alternatívou obsluhy udalostí)

			/*packagePrivate*/ static GRobot hlavnýRobot;
			/*packagePrivate*/ static boolean inicializované = false;

		// Prvky a údaje rozloženia hlavného okna

			// Atribúty používané na uloženie parametrov okna

				private final static String
					predvolenýNázovKonfiguračnéhoSúboru = "grobot.cfg";
				private static String názovKonfiguračnéhoSúboru = null;
				/*packagePrivate*/ final static Súbor konfiguračnýSúbor = new Súbor();
				private static String predvolenáSekciaKonfigurácie = "";
				private static boolean prvéSpustenie = true;

				private static void uložKonfiguráciu()
				{
					if (null == názovKonfiguračnéhoSúboru) return;

					try { konfiguračnýSúbor.zavri(); } catch (Exception e)
					{ GRobotException.vypíšChybovéHlásenia(e); }

					int početRegistrovanýchRobotov = 0;

					for (GRobot tento : GRobot.zoznamRobotov)
						if (tento.jeRegistrovaný)
							++početRegistrovanýchRobotov;

					if (0 == početRegistrovanýchRobotov &&
						históriaVstupnéhoRiadkaNezmenená &&
						počiatočnáŠírka == svet.getWidth() &&
						počiatočnáVýška == svet.getHeight() &&
						počiatočnéX == svet.getLocation().x &&
						počiatočnéY == svet.getLocation().y &&
						počiatočnýStav == svet.getExtendedState() &&
						(null == ObsluhaUdalostí.počúvadlo ||
							(!ObsluhaUdalostí.počúvadlo.konfiguráciaZmenená() &&
							!ObsluhaUdalostí.počúvadlo.konfiguraciaZmenena())))
					{
						boolean žiadnaZmena = true;

						for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
						{
							if (počúvajúci.konfiguráciaZmenená() ||
								počúvajúci.konfiguraciaZmenena())
							{
								žiadnaZmena = false;
								break;
							}
						}

						if (žiadnaZmena) return;
					}

					Súbor.Sekcia pôvodnáSekcia =
						konfiguračnýSúbor.aktívnaSekcia;

					String mennýPriestor = konfiguračnýSúbor.
						aktívnaSekcia.mennýPriestorVlastností;

					try
					{
						konfiguračnýSúbor.otvorNaZápis(
							názovKonfiguračnéhoSúboru);

						pôvodnáSekcia = konfiguračnýSúbor.aktívnaSekcia;
						konfiguračnýSúbor.aktivujSekciu(
							predvolenáSekciaKonfigurácie);

						mennýPriestor = konfiguračnýSúbor.
							aktívnaSekcia.mennýPriestorVlastností;

						// Konfigurácia okna
						try
						{
							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "okno";

							int stav = svet.getExtendedState();

							if (NORMAL != stav)
							{
								konfiguračnýSúbor.zapíšVlastnosť(
									"x", poslednéX); // windowX
								konfiguračnýSúbor.zapíšVlastnosť(
									"y", poslednéY); // windowY

								konfiguračnýSúbor.zapíšVlastnosť(
									"šírka", poslednáŠírka); // windowWidth
								konfiguračnýSúbor.zapíšVlastnosť(
									"výška", poslednáVýška); // windowHeight
							}
							else
							{
								konfiguračnýSúbor.zapíšVlastnosť(
									"x", svet.getLocation().x); // windowX
								konfiguračnýSúbor.zapíšVlastnosť(
									"y", svet.getLocation().y); // windowY

								konfiguračnýSúbor.zapíšVlastnosť(
									"šírka", svet.getWidth()); // windowWidth
								konfiguračnýSúbor.zapíšVlastnosť(
									"výška", svet.getHeight()); // windowHeight
							}

							konfiguračnýSúbor.zapíšVlastnosť(
								"minimalizované", 0 != (stav & ICONIFIED));
							konfiguračnýSúbor.zapíšVlastnosť(
								"maximalizované", 0 != (stav & MAXIMIZED_BOTH));
							// ‼ Zmrzne:
							// ‼ if (0 != (stav & MAXIMIZED_BOTH))
							// ‼ 	svet.setExtendedState(stav & ~MAXIMIZED_BOTH);
						}
						catch (Exception e)
						{ GRobotException.vypíšChybovéHlásenia(e); }

						try
						{
							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "história";

							if (uchovajHistóriuVstupnéhoRiadka)
							{
								// Zápis histórie príkazov do konfigurácie:
								int dĺžkaHistórie =
									históriaVstupnéhoRiadka.veľkosť();
								konfiguračnýSúbor.zapíšVlastnosť(
									"dĺžka", dĺžkaHistórie);
								for (int i = 0; i < dĺžkaHistórie; ++i)
									konfiguračnýSúbor.zapíšVlastnosť(
										"riadok[" + i + "]",
										históriaVstupnéhoRiadka.daj(i));
								históriaVstupnéhoRiadkaNezmenená = true;
							}
							else
								konfiguračnýSúbor.vymažVlastnosť("dĺžkaHistórie");
						}
						catch (Exception e)
						{ GRobotException.vypíšChybovéHlásenia(e); }

						if (uložKonfiguráciuSveta) try
						{
							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "svet";

							konfiguračnýSúbor.zapíšVlastnosť(
								"farbaPozadia", null == farbaPozadia ?
								null : Farba.farbaNaReťazec(farbaPozadia));

							konfiguračnýSúbor.zapíšVlastnosť(
								"interaktívny", Svet.interaktívnyRežim);


							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "svet.časovač";

							konfiguračnýSúbor.zapíšVlastnosť(
								"aktívny", časovač.aktívny());

							konfiguračnýSúbor.zapíšVlastnosť(
								"čas", Long.valueOf(časovač.čas));


							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "svet.výplne";

							konfiguračnýSúbor.zapíšVlastnosť("posunutie",
								Bod.polohaNaReťazec(posunutieVýplneX,
									posunutieVýplneY));

							konfiguračnýSúbor.zapíšVlastnosť("mierka",
								Bod.polohaNaReťazec(
									mierkaVýplneX, mierkaVýplneY));

							konfiguračnýSúbor.zapíšVlastnosť("otočenie",
								otočVýplňΑ);

							konfiguračnýSúbor.zapíšVlastnosť("stredOtáčania",
								Bod.polohaNaReťazec(stredOtáčaniaVýplneX,
									stredOtáčaniaVýplneY));


							Farba farba;


							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "podlaha";

							farba = GRobot.podlaha.farbaTextu();
							konfiguračnýSúbor.zapíšVlastnosť("farbaTextu",
								null == farba ? null :
									Farba.farbaNaReťazec(farba));

							farba = GRobot.podlaha.farbaPozadiaTextu();
							konfiguračnýSúbor.zapíšVlastnosť(
								"farbaPozadiaTextu", null == farba ? null :
									Farba.farbaNaReťazec(farba));

							GRobot.podlaha.vnútornáKonzola.aktuálnePísmo.
								uložDoSúboru(konfiguračnýSúbor);

							konfiguračnýSúbor.zapíšVlastnosť("interaktívny",
								GRobot.podlaha.interaktívnyRežim);


							konfiguračnýSúbor.aktívnaSekcia.
								mennýPriestorVlastností = "strop";

							farba = GRobot.strop.farbaTextu();
							konfiguračnýSúbor.zapíšVlastnosť("farbaTextu",
								null == farba ? null :
									Farba.farbaNaReťazec(farba));

							farba = GRobot.strop.farbaPozadiaTextu();
							konfiguračnýSúbor.zapíšVlastnosť(
								"farbaPozadiaTextu", null == farba ? null :
									Farba.farbaNaReťazec(farba));

							GRobot.strop.vnútornáKonzola.aktuálnePísmo.
								uložDoSúboru(konfiguračnýSúbor);

							konfiguračnýSúbor.zapíšVlastnosť("interaktívny",
								GRobot.strop.interaktívnyRežim);
						}
						catch (Exception e)
						{ GRobotException.vypíšChybovéHlásenia(e); }

						konfiguračnýSúbor.aktívnaSekcia.
							mennýPriestorVlastností = mennýPriestor;

						for (GRobot tento : GRobot.zoznamRobotov)
						{
							try
							{
								if (tento.jeRegistrovaný)
									tento.uložDoSúboru(konfiguračnýSúbor);
							}
							catch (Exception e)
							{ GRobotException.vypíšChybovéHlásenia(e); }
						}

						if (null != ObsluhaUdalostí.počúvadlo)
						{
							ObsluhaUdalostí.počúvadlo.
								zapíšKonfiguráciu(konfiguračnýSúbor);
							ObsluhaUdalostí.počúvadlo.
								zapisKonfiguraciu(konfiguračnýSúbor);
						}

						for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
						{
							počúvajúci.zapíšKonfiguráciu(konfiguračnýSúbor);
							počúvajúci.zapisKonfiguraciu(konfiguračnýSúbor);
						}
					}
					catch (Exception e)
					{ GRobotException.vypíšChybovéHlásenia(e); }
					finally
					{
						konfiguračnýSúbor.aktívnaSekcia.
							mennýPriestorVlastností = mennýPriestor;
						konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia;
					}

					try { konfiguračnýSúbor.zavri(); }
					catch (Exception e)
					{ GRobotException.vypíšChybovéHlásenia(e); }
				}

				/*packagePrivate*/ static void čítajVlastnúKonfiguráciu()
				{
					if (null == názovKonfiguračnéhoSúboru) return;

					Súbor.Sekcia pôvodnáSekcia =
						konfiguračnýSúbor.aktívnaSekcia;

					try
					{
						if (null != ObsluhaUdalostí.počúvadlo)
							try
							{
								// konfiguračnýSúbor.otvorNaČítanie(
								// 	názovKonfiguračnéhoSúboru);

								konfiguračnýSúbor.aktivujSekciu(
									predvolenáSekciaKonfigurácie);

								ObsluhaUdalostí.počúvadlo.
									čítajKonfiguráciu(konfiguračnýSúbor);
								ObsluhaUdalostí.počúvadlo.
									citajKonfiguraciu(konfiguračnýSúbor);
							}
							catch (Exception e)
							{ GRobotException.vypíšChybovéHlásenia(e); }
					}
					finally { konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia; }

					try
					{
						for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
							try
							{
								// konfiguračnýSúbor.otvorNaČítanie(
								// 	názovKonfiguračnéhoSúboru);

								konfiguračnýSúbor.aktivujSekciu(
									predvolenáSekciaKonfigurácie);

								počúvajúci.čítajKonfiguráciu(konfiguračnýSúbor);
								počúvajúci.citajKonfiguraciu(konfiguračnýSúbor);
							}
							catch (Exception e)
							{ GRobotException.vypíšChybovéHlásenia(e); }
					}
					finally { konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia; }

					// try { konfiguračnýSúbor.zavri(); }
					// catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
				}

			// Počiatočné/posledné rozmery a poloha okna (s definovanými
				// predvolenými hodnotami) a počiatočný stav okna

				private static int počiatočnáŠírka = 600;
				private static int počiatočnáVýška = 500;
				private static int počiatočnéX = 25;
				private static int počiatočnéY = 25;
				private static int počiatočnýStav = NORMAL;

				private static int poslednáŠírka = 600;
				private static int poslednáVýška = 500;
				private static int poslednéX = 25;
				private static int poslednéY = 25;

			// Viditeľnosť pri štarte (aplikácie môžu oceniť, keď je hlavné
				// okno pri štarte skryté a aplikácia sa môže inicializovať
				// na pozadí…)

				private static boolean zobrazPriŠtarte = true;


			// Séria atribútov na transformáciu výplní (tých obrázkových –
			// kvázi textúr)

				/*packagePrivate*/ static double posunutieVýplneX = 0.0;
				/*packagePrivate*/ static double posunutieVýplneY = 0.0;
				/*packagePrivate*/ static double stredOtáčaniaVýplneX = 0.0;
				/*packagePrivate*/ static double stredOtáčaniaVýplneY = 0.0;

				/*packagePrivate*/ static double posuňVýplňX =
					Plátno.šírkaPlátna / 2.0 + posunutieVýplneX;
				/*packagePrivate*/ static double posuňVýplňY =
					Plátno.výškaPlátna / 2.0 - posunutieVýplneY;
				/*packagePrivate*/ static double mierkaVýplneX = 1.0;
				/*packagePrivate*/ static double mierkaVýplneY = 1.0;
				/*packagePrivate*/ static double otočVýplňΑ = 0.0;
				/*packagePrivate*/ static double otočVýplňX =
					(Plátno.šírkaPlátna / 2.0 +
					posunutieVýplneX) + stredOtáčaniaVýplneX;
				/*packagePrivate*/ static double otočVýplňY =
					(Plátno.výškaPlátna / 2.0 -
					posunutieVýplneY) - stredOtáčaniaVýplneY;

				// Pri zmene rozmerov plátna alebo pri úprave zdrojových
				// údajov pre parametre výplne ich treba prepočítať.
				/*packagePrivate*/ static void prepočítajParametreVýplne()
				{
					posuňVýplňX = prepočítajX(posunutieVýplneX);
					posuňVýplňY = prepočítajY(posunutieVýplneY);
					otočVýplňX = prepočítajX(posunutieVýplneX) +
						stredOtáčaniaVýplneX;
					otočVýplňY = prepočítajY(posunutieVýplneY) -
						stredOtáčaniaVýplneY;
				}

			// Komponent klientskej oblasti okna s príslušenstvom

				private static ImageIcon ikonaPlátna;
				private static JLabel komponentIkony;

				@SuppressWarnings("serial")
				/*packagePrivate*/ static JPanel hlavnýPanel = new JPanel()
				{
					@Override public boolean isOptimizedDrawingEnabled()
					{ return false; }


					// Ťahanie a pustenie súborov — začiatok

					private DropTarget cieľPusteniaSúboru;
					private ObsluhaPusteniaSúboru obsluhaPusteniaSúboru;

					private DropTarget cieľPusteniaSúboru()
					{
						if (cieľPusteniaSúboru == null)
							cieľPusteniaSúboru = new DropTarget(this,
								DnDConstants.ACTION_COPY, null);

						return cieľPusteniaSúboru;
					}

					private ObsluhaPusteniaSúboru obsluhaPusteniaSúboru()
					{
						if (obsluhaPusteniaSúboru == null)
							obsluhaPusteniaSúboru = new ObsluhaPusteniaSúboru();

						return obsluhaPusteniaSúboru;
					}

					@Override public void addNotify()
					{
						super.addNotify();

						try
						{
							cieľPusteniaSúboru().
								addDropTargetListener(
									obsluhaPusteniaSúboru());
						}
						catch (TooManyListenersException e)
						{ GRobotException.vypíšChybovéHlásenia(e); }
					}

					@Override public void removeNotify()
					{
						super.removeNotify();

						cieľPusteniaSúboru().
							removeDropTargetListener(
								obsluhaPusteniaSúboru());
					}

					// Ťahanie a pustenie súborov — koniec
				};

				// Takto to nefungovalo
				// private static javax.swing.JScrollPane rolovaciaTabla =
				// 	new javax.swing.JScrollPane(hlavnýPanel);

				// Trieda pracujúca so systémom Ťahaj a Pusti (súbor)
				private static class ObsluhaPusteniaSúboru
					implements DropTargetListener
				{
					private void povoliťPustenie(DropTargetDragEvent dtde)
					{
						if (dtde.isDataFlavorSupported(
							DataFlavor.javaFileListFlavor))
						{
							ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(
								dtde.getLocation().getX());
							ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(
								dtde.getLocation().getY());

							if (null != ObsluhaUdalostí.počúvadlo)
							{
								ObsluhaUdalostí.počúvadlo.ťahanieSúborov();
								ObsluhaUdalostí.počúvadlo.tahanieSuborov();
							}

							for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
							{
								počúvajúci.ťahanieSúborov();
								počúvajúci.tahanieSuborov();
							}

							dtde.acceptDrag(DnDConstants.ACTION_COPY);
						}
						else dtde.rejectDrag();
					}

					@Override public void dragEnter(
						DropTargetDragEvent dtde)
					{ povoliťPustenie(dtde); }

					@Override public void dragOver(
						DropTargetDragEvent dtde)
					{ povoliťPustenie(dtde); }

					@Override public void dropActionChanged(
						DropTargetDragEvent dtde) {}
					@Override public void dragExit(
						DropTargetEvent dte) {}

					@Override public void drop(DropTargetDropEvent dtde)
					{
						Transferable transferable = dtde.getTransferable();
						if (dtde.isDataFlavorSupported(
							DataFlavor.javaFileListFlavor))
						{
							dtde.acceptDrop(dtde.getDropAction());
							try
							{
								@SuppressWarnings("unchecked")
								List<File> transferData =
									(List<File>)transferable.
										getTransferData(
											DataFlavor.javaFileListFlavor);

								if (transferData != null &&
									transferData.size() > 0)
								{
									for (File file : transferData)
									{
										String menoSúboru =
											file.getCanonicalPath();

										if (null != ObsluhaUdalostí.počúvadlo)
										{
											ObsluhaUdalostí.počúvadlo.
												pustenieSúboru(menoSúboru);
											ObsluhaUdalostí.počúvadlo.
												pustenieSuboru(menoSúboru);
										}

										for (GRobot počúvajúci :
											GRobot.počúvajúciRozhranie)
										{
											počúvajúci.pustenieSúboru(
												menoSúboru);
											počúvajúci.pustenieSuboru(
												menoSúboru);
										}
									}

									dtde.dropComplete(true);
								}
							}
							catch (Exception e)
							{ GRobotException.vypíšChybovéHlásenia(e); }
						}
						else dtde.rejectDrop();
					}
				}


			// Položky hlavnej ponuky

				/*packagePrivate*/ static PoložkaPonuky
					položkaVymazať = null;
				/*packagePrivate*/ static PoložkaPonuky
					položkaPrekresliť = null;
				/*packagePrivate*/ static PoložkaPonuky
					položkaSkončiť = null;
				/*packagePrivate*/ static boolean
					ponukaVPôvodnomStave = true;
				/*packagePrivate*/ static int
					aktuálnaPonuka = 0;
				/*packagePrivate*/ static int
					aktuálnaPoložka = 0; // 3;

			// Dvojité „bufferovanie“ grafiky

				// Komponent okna „obrázok“
				/*packagePrivate*/ static BufferedImage obrázokSveta1 =
					new BufferedImage(Plátno.šírkaPlátna, Plátno.výškaPlátna,
						BufferedImage.TYPE_INT_ARGB);

				// Grafický objekt na kreslenie do „obrázka“ (ako komponentu)
				/*packagePrivate*/ static Graphics2D grafikaSveta1 =
					obrázokSveta1.createGraphics();

				// Vyrovnávacia pamäť (sekundárny buffer) komponentu okna
				/*packagePrivate*/ static BufferedImage obrázokSveta2 =
					new BufferedImage(Plátno.šírkaPlátna, Plátno.výškaPlátna,
						BufferedImage.TYPE_INT_ARGB);

				// Vyrovnávacia pamäť (sekundárny buffer) grafiky komponentu
				/*packagePrivate*/ static Graphics2D grafikaSveta2 =
					obrázokSveta2.createGraphics();

			// Vstupný riadok

				/*packagePrivate*/ final static JMenuBar panelVstupnéhoRiadka =
					new JMenuBar();
				private final static JLabel popisVstupnéhoRiadka =
					new JLabel();
				/*packagePrivate*/ final static JTextField vstupnýRiadok =
					new JTextField();
				private final static StringBuffer údajeVstupnéhoRiadka =
					new StringBuffer();
				private final static StringBuffer zrušenéÚdajeVstupnéhoRiadka =
					new StringBuffer();


		// Dialógy (správy a otázky)

			// Predvolený titulok okien s otázkami
			private final static String predvolenýTitulokOtázky = "Otázka";

			// Popisy tlačidiel v oknách s otázkami
			private final static Object[] odpovedeOtázky = {"Áno", "Nie"};

			// Predvolený titulok okien na vstup údajov
			private final static String predvolenýTitulokVstupu = "Vstup";

			// Predvolený titulok okien na zadanie hesla
			private final static String predvolenýTitulokHesla = "Heslo";

			// Predvolený titulok vstupných dialógových okien
			private final static String predvolenýTitulokDialógu = "Dialóg";

			// Prvky používané v zadávacích komunikačných dialógoch

				private final static RobotTextField textovýRiadok =
					new RobotTextField();
				private final static RobotPasswordField riadokSHeslom =
					new RobotPasswordField();
				/*packagePrivate*/ final static Object[] odpovedeZadania =
					{"OK", "Zrušiť"};
				/*packagePrivate*/ final static String[] tlačidláDialógu =
					{"Reset", "Miešať…", // Dialóg farieb
					"Reset",  // Dialóg polohy
					"Reset"}; // Dialóg smeru

			// Príznak zobrazenia dialógu metódou „dialóg“ – keďže táto metóda
			// používa zoznamy, kladieme väčší dôraz na to, aby sa nedostala do
			// vzájomného konfliktu…
			private static boolean dialógZobrazený = false;

			// Zoznam komponentov panelov farieb v poli dialógu spúšťaného
			// metódou „dialóg“
			private final static Vector<Farba.PanelFarieb> voľbyFariebDialógu =
				new Vector<>();

			// Zoznam komponentov panelov polôh v poli dialógu spúšťaného
			// metódou „dialóg“
			private final static Vector<Bod.PanelPolohy> voľbyPolohyDialógu =
				new Vector<>();

			// Zoznam komponentov panelov uhlov (smerov) v poli dialógu
			// spúšťaného metódou „dialóg“
			private final static Vector<Uhol.PanelSmeru> voľbySmeruDialógu =
				new Vector<>();

			// Zoznam komponentov textových riadkov (polí) dialógu spúšťaného
			// metódou „dialóg“
			private final static Vector<RobotTextField> textovéRiadkyDialógu =
				new Vector<>();

			// Zoznam komponentov volieb („checkboxov“) dialógu spúšťaného
			// metódou „dialóg“
			private final static Vector</*Robot/Nie!*/JCheckBox> voľbyDialógu =
				new Vector<>();

			// Zoznam vstupných komponentov na zadávanie hesiel dialógu
			// spúšťaného metódou „dialóg“
			private final static Vector<RobotPasswordField>
				riadkyHesielDialógu = new Vector<>();

			// Zoznam panelov vnútorne používaných pri procese tvorby dialógov
			// spúšťaných metódou „dialóg“
			private final static Vector<JPanel> panelyDialógu =
				new Vector<>();

			// Zoznam komponentov popisov prvkov, ktorý je v niektorých
			// prípadoch vnútorne používaných pri procese tvorby dialógov
			// spúšťaných metódou „dialóg“
			private final static Vector<JLabel> popisyDialógu =
				new Vector<>();

			// Úvodná obrazovka
			private static JWindow úvodnáObrazovka = null;



		// Generátor pseudonáhodných čísel
			// (určené na použitie funkciami generovania náhodných čísel)
			private final static Random generátor = new Random();



			// Zoznam kurzorov (a ďalšie pomocné premenné s nimi súvisiace)

				private final static Cursor prázdnyKurzor = Toolkit.
					getDefaultToolkit().createCustomCursor(
						new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
						new Point(0, 0), "prázdny");

				private final static TreeMap<String, Cursor> kurzory =
					new TreeMap<>();

				private final static TreeMap<String, Integer>
					systémovéNázvyKurzorov = new TreeMap<>();
				static
				{
					systémovéNázvyKurzorov.put("default",   0);
					systémovéNázvyKurzorov.put("crosshair", 1);
					systémovéNázvyKurzorov.put("text",      2);
					systémovéNázvyKurzorov.put("wait",      3);
					systémovéNázvyKurzorov.put("swResize",  4);
					systémovéNázvyKurzorov.put("seResize",  5);
					systémovéNázvyKurzorov.put("nwResize",  6);
					systémovéNázvyKurzorov.put("neResize",  7);
					systémovéNázvyKurzorov.put("nResize",   8);
					systémovéNázvyKurzorov.put("sResize",   9);
					systémovéNázvyKurzorov.put("wResize",   10);
					systémovéNázvyKurzorov.put("eResize",   11);
					systémovéNázvyKurzorov.put("hand",      12);
					systémovéNázvyKurzorov.put("move",      13);

					systémovéNázvyKurzorov.put("predvolený",      0);
					systémovéNázvyKurzorov.put("mieridlo",        1);
					systémovéNázvyKurzorov.put("textový",         2);
					systémovéNázvyKurzorov.put("čakaj",           3);
					systémovéNázvyKurzorov.put("jzZmeniťVeľkosť", 4);
					systémovéNázvyKurzorov.put("jvZmeniťVeľkosť", 5);
					systémovéNázvyKurzorov.put("szZmeniťVeľkosť", 6);
					systémovéNázvyKurzorov.put("svZmeniťVeľkosť", 7);
					systémovéNázvyKurzorov.put("sZmeniťVeľkosť",  8);
					systémovéNázvyKurzorov.put("jZmeniťVeľkosť",  9);
					systémovéNázvyKurzorov.put("zZmeniťVeľkosť",  10);
					systémovéNázvyKurzorov.put("vZmeniťVeľkosť",  11);
					systémovéNázvyKurzorov.put("ruka",            12);
					systémovéNázvyKurzorov.put("presunúť",        13);
				}



		// Príznaky a semafory

			// Príznak vypnutia automatického prekresľovania
			/*packagePrivate*/ static boolean nekresli = false;

			// Semafor vykonávania série metód pracuj()
			private static boolean pracujem = false;

			// Semafor prekreslenia po vykonaní metód pracuj(),
			// (pretože počas činnosti metód aktivácie je prekresľovanie
			// vypnuté)
			private static boolean žiadamPrekresleniePoPráci = false;

			// Semafor priebehu prekresľovania (proti sebazablokovaniu)
			/*packagePrivate*/ static boolean právePrekresľujem = false;

			// Príznak zavrhnutej požiadavky na automatické prekreslenie; Ak
			// je tento príznak rovný true, je pravdepodobné, že obsah
			// obrazovky nekorešponduje so skutočnými obsahmi pozadia
			// a plátien:
			/*packagePrivate*/ static boolean neboloPrekreslené = true;

			// Príznak na perzistentný vstupný riadok
			private static boolean vstupnýRiadokStáleViditeľný = false;

			// Príznak aktivovania histórie vstupného riadka.
			private static boolean aktívnaHistóriaVstupnéhoRiadka = false;

			// Príznak uchovávania histórie vstupného riadka do konfigurácie
			private static boolean uchovajHistóriuVstupnéhoRiadka = false;

			// Príznak zmeny histórie vstupného riadka (použité pri zápise
			// do konfigurácie)
			private static boolean históriaVstupnéhoRiadkaNezmenená = true;

			// Zoznam na uchovanie odoslaných vstupných (príkazových) riadkov
			private static Zoznam<String> históriaVstupnéhoRiadka =
				new Zoznam<>();

			// Záloha aktuálneho obsahu vstupného riadka pri začatí pohybu
			// v histórii príkazov; zálohovaná hodnota sa vráti do vstupného
			// riadka po opustení histórie príkazov (na jej konci)
			private static String aktuálnyVstupnýRiadok = "";

		// Štandardný vstup

			// Implementácia čítania štandardného vstupu. Pozri aj stručné
			// informácie v abstraktnej triede ReadStandardInput.
			private static class ŠtandardnýVstup extends ReadStandardInput
			{
				// Príznak ukončenia a uzavretia vlákna štandardného vstupu.
				private boolean koniecVstupu = false;

				// Zásobník reťazcov štandardného vstupu potrebný pre
				// metódu čakajNaVstup.
				private LinkedList<String> zásobník = null;

				// Konštruktor umožňujúci zvoliť kódovanie štandardného
				// vstupu (je možné určiť len jedno).
				public ŠtandardnýVstup(String kódovanie, boolean zbierajVstup)
					throws UnsupportedEncodingException
				{
					super(kódovanie);
					if (zbierajVstup) zbierajVstup();
					start();
				}

				// Predvolený konštruktor používajúci kódovanie UTF-8.
				public ŠtandardnýVstup(boolean zbierajVstup)
					throws UnsupportedEncodingException
				{
					super();
					if (zbierajVstup) zbierajVstup();
					start();
				}

				// Zapne zbieranie vstupu (potrebné pre metódu čakajNaVstup).
				public void zbierajVstup()
				{
					if (null == zásobník)
						zásobník = new LinkedList<>();
				}

				// Implementácia udalosti prijatia (a spracovania) riadka
				// zo štandardného vstupu.
				public void processInputLine(String riadokVstupu)
				{
					if (null != zásobník) zásobník.add(riadokVstupu);

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.
								spracujRiadokVstupu(riadokVstupu);
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciSystém)
						{
							počúvajúci.spracujRiadokVstupu(riadokVstupu);
						}
					}
				}

				// Implementácia udalosti ukončenia prúdu údajov zo
				// štandardného vstupu. (Vykoná sa len v prípade, keď je prúd
				// údajov štandardného vstupu konečný – konečný tok údajov
				// z iného procesu alebo presmerovanie údajov zo súboru.)
				public void endOfInput()
				{
					koniecVstupu = true;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.koniecVstupu();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciSystém)
						{
							počúvajúci.koniecVstupu();
						}
					}
				}
			}

			// Inštancia vlákna štandardného vstupu (predvolene neaktívneho).
			private static ŠtandardnýVstup štandardnýVstup = null;


	// ------------------------------- //
	//  *** Rôzne súkromné metódy ***  //
	// ------------------------------- //

		// Inicializácia grafiky

			/*packagePrivate*/ static void inicializujGrafiku()
			{
				if (inicializované) return;

				// Poznámka: Kedysi malo nesprávne kódovanie za následok
				//           zastavenie činnosti aplikácie s pokusom o jej
				//           opätovné spustenie so správnym kódovaním:
				// checkEncoding(null);
				inicializované = true;

				// Nastavenie systémového Look&Feel:
				try { UIManager.setLookAndFeel(UIManager.
					getSystemLookAndFeelClassName()); }
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e, true); }

				// Vymaž grafiku plátien a inicializuj konzoly
				Svet.vymaž();

				GRobot.strop.grafikaPlátna.addRenderingHints(Obrázok.hints);
				GRobot.podlaha.grafikaPlátna.addRenderingHints(Obrázok.hints);
				grafikaSveta1.addRenderingHints(Obrázok.hints);
				grafikaSveta2.addRenderingHints(Obrázok.hints);

				// Všetka grafika vo svete grafického robota (farba pozadia,
				// grafiky plátien a tvary robotov) je spojená do jedného
				// výsledného obrázka typu BufferedImage. Obrázok je uložený
				// v pamäti počítača a na jeho zobrazenie je vhodné použiť
				// drobný trik… Vytvoríme grafický popis JLabel, ktorému
				// priradíme ikonu (ImageIcon) s grafikou obrázka. Takže
				// grafika je zatvorená do ikony, ikona je vložená do popisu
				// (komponentu typu JLabel) a ten je potom umiestnený do
				// hlavného panela umiestneného v okne aplikácie. Panel má
				// definovaný špecifický spôsob rozmiestňovania komponentov
				// (tzv. LayoutManager). Všetko okrem obrázka je umiestňované
				// prostredníctvom absolútnych súradníc…

				ikonaPlátna = new ImageIcon(obrázokSveta1);
				komponentIkony = new JLabel(ikonaPlátna);

				@SuppressWarnings("serial")
				OverlayLayout overlay = new OverlayLayout(hlavnýPanel)
				{
					// Vlastný spôsob umiestňovania komponentov
					@Override public void layoutContainer(Container cieľ)
					{
						int x = 0, y = 0, šírka = cieľ.getWidth(),
							výška = cieľ.getHeight();

						x = (šírka - ikonaPlátna.getIconWidth()
							// komponentIkony.getSize().width
							) / 2;
						y = (výška -
							ikonaPlátna.getIconHeight()
							// komponentIkony.getSize().height
							) / 2;

						komponentIkony.setBounds(x, y,
							komponentIkony.getPreferredSize().width,
							komponentIkony.getPreferredSize().height);

						Component komponenty[] = cieľ.getComponents();

						// Až po vypočítaní polohy plátna môžeme upraviť
						// maximálne hranice šírky a výšky určenej na
						// umiestnenie ostatných komponentov:
						if (šírka > Plátno.šírkaPlátna)
							šírka = Plátno.šírkaPlátna;
						if (výška > Plátno.výškaPlátna)
							výška = Plátno.výškaPlátna;

						for (Component komponent : komponenty)
						{
							if (komponent instanceof Tlačidlo)
								((Tlačidlo)komponent).umiestni(
									x, y, šírka, výška);
							else if (komponent instanceof GRobot.UpravText)
								((GRobot.UpravText)komponent).umiestni(
									x, y, šírka, výška);
							else if (komponent instanceof RolovaciaLišta)
								((RolovaciaLišta)komponent).umiestni(
									x, y, šírka, výška);
							else if (komponent instanceof
								PoznámkovýBlok.RolovaniePoznámkovéhoBloku)
							{
								((PoznámkovýBlok.RolovaniePoznámkovéhoBloku)
									komponent).poznámkovýBlok.umiestni(
										x, y, šírka, výška);
							}
						}

						if (Plátno.konzolaPoužitá)
						{
							/*if (nekresli)
								neboloPrekreslené = true;
							else*/
								prekresli(); // noInvokeLater
						}
					}
				};
				hlavnýPanel.setLayout(overlay);
				hlavnýPanel.add(komponentIkony);

				// hlavnýPanel.enableInputMethods(false);
				// System.out.println("enableInputMethods(false)");

				// Keby sme chceli panel zviditeľniť čiernym ohraničením:
				// hlavnýPanel.setBorder(
				// 	new javax.swing.border.LineBorder(Color.BLACK));

				// Inicializuj vstupný riadok
				popisVstupnéhoRiadka.setBorder(BorderFactory.
					createEmptyBorder(0, 4, 0, 8));
				panelVstupnéhoRiadka.add(popisVstupnéhoRiadka);
				panelVstupnéhoRiadka.add(vstupnýRiadok);
				panelVstupnéhoRiadka.setVisible(false);

				// Poslucháč klávesnice vstupného riadka počúvajúci stlačenie
				// klávesov ENTER a ESCAPE…
				vstupnýRiadok.addKeyListener(new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							// Umiestnenie týchto príkazov do reakcie
							// keyTyped nefungovalo v macOS (predtým OS X
							// a Mac OS)
							if (e.getKeyChar() == KeyEvent.VK_ENTER)
								potvrďVstup();
							else if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
								zrušVstup();
							else if (aktívnaHistóriaVstupnéhoRiadka)
								pohybPoHistórii(e);
						}

						public void keyReleased(KeyEvent e) {}
						public void keyTyped(KeyEvent e) {}
					});

				// Do hlavného rámca pridáme hlavný panel (od verzie 1.82
				// obsiahnutý v rolovacom paneli) a vstupný riadok
				svet.add(hlavnýPanel, BorderLayout.CENTER);
				svet.add(panelVstupnéhoRiadka, BorderLayout.SOUTH);

				// Nastavenie poslucháčov myši a klávesnice
				hlavnýPanel.addMouseListener(udalostiOkna);
				hlavnýPanel.addMouseMotionListener(udalostiOkna);
				hlavnýPanel.addMouseWheelListener(udalostiOkna);

				// Hlavný panel bol kedysi JLabel, ktorý nedokáže prijímať
				// udalosti klávesnice:
				// svet.addKeyListener(udalostiOkna);

				// Už to však nie je pravda:
				hlavnýPanel.addKeyListener(udalostiOkna);
				hlavnýPanel.setFocusable(true);
				hlavnýPanel.doLayout();

				svet.addComponentListener(udalostiOkna);
				svet.addWindowFocusListener(udalostiOkna);
				svet.addWindowListener(udalostiOkna);
				svet.addWindowStateListener(udalostiOkna);

				// Nastavenie ďalších vlastností okna aplikácie
				// …

				// svet.setResizable(false);
				svet.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					// neurobí nič pri zavretí – resp. tento framework volá
					// všetky dostupné reakcie zavretie a zatvorenie, ktoré
					// predvolene vracajú hodnotu true; ak hociktorá z nich
					// vráti false, tak sa predvolená akcia Svet.koniec()
					// nevykoná, v opačnom prípade sa vykoná…
				// svet.setDefaultCloseOperation(EXIT_ON_CLOSE);
					// zavrie všetky okná
				// svet.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					// zavrie len aktuálne okno

				svet.setTitle(versionString);
				if (null != oknoCelejObrazovky)
					oknoCelejObrazovky.setTitle(versionString);
				svet.setJMenuBar(vytvorHlavnúPonuku());
				svet.pack();
				// svet.requestFocusInWindow();
				svet.setSize(počiatočnáŠírka, počiatočnáVýška);
				svet.setLocation(počiatočnéX, počiatočnéY);
				svet.setVisible(zobrazPriŠtarte);

				// TODO: Detekcia retina displejov a implementácia mechanizmu
				// automatického čítania obrázkov s vyšším rozlíšením.
				// Odoslať na System.err nasledujúce upozornenie pri
				// prečítaní prvého obrázka, ku ktorému nebola nájdená verzia
				// s vyšším rozlíšením:
				// 
				// Tip: Programovací rámec hľadá pri čítaní obrázkov súbory
				// s dvojnásobným rozlíšením v tvare:
				//     «názov-súboru» 2.«príp»,
				// kde «názov-súboru» je pôvodný názov súboru obrázka,
				// «príp» je prípona súboru (jpg, png, gif…) a  2 je reťazec,
				// ktorý treba pripojiť za pôvodný názov súboru, aby ho rámec
				// vedel identifikovať.
				// 
				// Vytvorte verzie obrázkov s vyšším rozlíšením a umiestnite
				// ich na rovnakú lokalitu ako pôvodné obrázky, aby ich rámec
				// mohol nájsť. Budú použité automaticky v prípade, že bude
				// pri spustení detegovaný displej s vysokým rozlíšením
				// v mierke 2 : 1.

				if (NORMAL != počiatočnýStav)
					svet.setExtendedState(počiatočnýStav);

				Svet.predvolenáFarbaPozadia();
				Svet.zalamujTexty();

				GRobot.podlaha.predvolenáFarbaPozadiaOznačenia();
				GRobot.podlaha.predvolenáFarbaTextuOznačenia();
				GRobot.podlaha.predvolenáFarbaPozadiaTextu();
				GRobot.podlaha.predvolenáFarbaTextu();

				GRobot.strop.predvolenáFarbaPozadiaOznačenia();
				GRobot.strop.predvolenáFarbaTextuOznačenia();
				GRobot.strop.predvolenáFarbaPozadiaTextu();
				GRobot.strop.predvolenáFarbaTextu();
				// inicializované = true;

				// Registruj „ukončovacie vlákno“
				Runtime.getRuntime().addShutdownHook(new Thread()
				{
					// Nasledujúca metóda je volaná pri ukončení aplikácie:
					public void run()
					{
						if (null != ObsluhaUdalostí.počúvadlo)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.ukončenie();
								ObsluhaUdalostí.počúvadlo.ukoncenie();
							}

						uložKonfiguráciu();

						if (null != Zvuk.kanál) Zvuk.kanál.close();

						for (GRobot tento : GRobot.zoznamRobotov) try
						{
							tento.súbor.zavri();
						}
						catch (Exception e)
						{
							GRobotException.vypíšChybovéHlásenia(e, true);
						}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci : GRobot.počúvajúciSystém)
							{
								počúvajúci.ukončenie();
								počúvajúci.ukoncenie();
							}
						}
					}
				});

				if (zobrazPriŠtarte)
				{
					SwingUtilities.invokeLater(() ->
					{
						if (svet.isVisible())
						{
							// System.out.println("Začiatok");
							hlavnýPanel.requestFocusInWindow();

							if (panelVstupnéhoRiadka.isVisible())
								vstupnýRiadok.requestFocus();
						}
					});
				}

				// Aby sa dala klávesnica použiť hneď:
				if (null == ÚdajeUdalostí.poslednáUdalosťKlávesnice)
					ÚdajeUdalostí.poslednáUdalosťKlávesnice =
						new KeyEvent(svet, 0, 0, 0, 0, '\0');

				// Aby sa dala myš použiť hneď:
				if (null == ÚdajeUdalostí.poslednáUdalosťMyši)
					ÚdajeUdalostí.poslednáUdalosťMyši = new MouseEvent(
						svet, 0, 0, 0, 0, 0, 0, false);
			}

			private static void novéPlátna()
			{
				if (inicializované)
				{
					GRobot.podlaha.vytvorNovéPlátno(
						Plátno.šírkaPlátna, Plátno.výškaPlátna);
					GRobot.strop.vytvorNovéPlátno(
						Plátno.šírkaPlátna, Plátno.výškaPlátna);

					obrázokSveta1 = new BufferedImage(
						Plátno.šírkaPlátna, Plátno.výškaPlátna,
						BufferedImage.TYPE_INT_ARGB);
					grafikaSveta1 = obrázokSveta1.createGraphics();

					if (null != Svet.vlnenie) Svet.odstráňVlnenie();
					obrázokSveta2 = new BufferedImage(
						Plátno.šírkaPlátna, Plátno.výškaPlátna,
						BufferedImage.TYPE_INT_ARGB);
					grafikaSveta2 = obrázokSveta2.createGraphics();

					GRobot.strop.grafikaPlátna.addRenderingHints(Obrázok.hints);
					GRobot.podlaha.grafikaPlátna.addRenderingHints(Obrázok.hints);
					grafikaSveta1.addRenderingHints(Obrázok.hints);
					grafikaSveta2.addRenderingHints(Obrázok.hints);


					// System.out.println(ikonaPlátna.getIconWidth() + " " +
					// 	ikonaPlátna.getIconHeight());

					ikonaPlátna.setImage(obrázokSveta1);
					// komponentIkony.invalidate();

					// System.out.println(ikonaPlátna.getIconWidth() + " " +
					// 	ikonaPlátna.getIconHeight());

					hlavnýPanel.doLayout();

					Svet.vymažGrafiku();


					for (GRobot tento : GRobot.zoznamRobotov)
					{
						if (tento.aktívnePlátno == GRobot.podlaha)
							tento.kresliNaPodlahu();
						else if (tento.aktívnePlátno == GRobot.strop)
							tento.kresliNaStrop();
					}

					for (GRobot tento : GRobot.záložnýZoznamRobotov)
					{
						if (tento.aktívnePlátno == GRobot.podlaha)
							tento.kresliNaPodlahu();
						else if (tento.aktívnePlátno == GRobot.strop)
							tento.kresliNaStrop();
					}
				}
			}



		// Transformácie súradníc, hľadanie priesečníkov a iná geometria

			// Prepočítanie súradníc myši do súradnicového priestoru frameworku

				private static double korekciaMyšiX(double x)
				{
					return x - (hlavnýPanel.getWidth() / 2.0);
				}

				private static double korekciaMyšiY(double y)
				{
					return -y + (hlavnýPanel.getHeight() / 2.0);
				}


			// Hľadanie priesečníkov

				// Hľadá priesečník dvoch úsečiek |AB| a |CD| alebo aspoň
				// priamok určených súradnicami počiatočných a koncových
				// bodov úsečiek: A[x0, y0], B[x1, y1], C[x2, y2] a D[x3, y3].
				// Zadaný bod (inštancia «priesečník») prijme súradnice
				// priesečníka (ak úsečky nie sú paralelné) a návratová
				// hodnota metódy vypovie o tom, či ide o priesečník ležiaci
				// na úsečkách (vymedzených úsekoch), alebo mimo nich – niekde
				// na priamkach určených bodmi. Ak sú úsečky paralelné, tak
				// vznikne výnimka.
				/*packagePrivate*/ static boolean priesečníkÚsečiek(
					double x0, double y0, double x1, double y1,
					double x2, double y2, double x3, double y3,
					Bod priesečník)
				{
					// Úlohu nájdenia priesečníka som riešil snáď stokrát…
					// Práve, keď som ju potreboval, nevedel som nájsť ani
					// jeden starší výpočet, takže som to riešil takto:
					// 
					// Based on 2D Line Segment Intersection example (C++)
					// Implementation of the theory provided by Paul Bourke
					// http://paulbourke.net/geometry/2d/
					// 
					// Written by Damian Coventry
					// Wednesday, 31 October 2007
					// 
					// Adaptované R. Horváthom v r. 2015

					double menovateľ = (y3 - y2) * (x1 - x0) -
						(x3 - x2) * (y1 - y0);
					if (menovateľ == 0.0)
						throw new GRobotException("Úsečky sú paralelné.",
							"segmentsAreParallel");

					double t1 = ((x3 - x2) * (y0 - y2) -
						(y3 - y2) * (x0 - x2)) / menovateľ;
					double t2 = ((x1 - x0) * (y0 - y2) -
						(y1 - y0) * (x0 - x2)) / menovateľ;

					priesečník.poloha(x0 + t1 * (x1 - x0), y0 + t1 * (y1 - y0));
					// Ak je návratová hodnota true, tak ide zároveň
					// o priesečník úsečiek určených zadanými súradnicami bodov
					return t1 >= 0.0 && t1 <= 1.0 && t2 >= 0.0 && t2 <= 1.0;
				}


		// Vytvorenie hlavnej ponuky

			private static JMenuBar vytvorHlavnúPonuku()
			{
				if (!inicializované) return null;

				JMenuBar hlavnáPonuka = new JMenuBar();
				JMenu položkaHlavnejPonuky = new JMenu("Ponuka");
				položkaHlavnejPonuky.setMnemonic(KeyEvent.VK_P);
				hlavnáPonuka.add(položkaHlavnejPonuky);

				položkaHlavnejPonuky.add(položkaSkončiť = new PoložkaPonuky(
					"Koniec", KeyEvent.VK_K, KeyEvent.VK_W));

				// ponukaVPôvodnomStave = true; – netreba, lebo ponuka ešte
				//     nie je priradená, t. j. nemôže dôjsť k automatickému
				//     vykonaniu metódy pridajDoHlavnejPonuky();

				return hlavnáPonuka;
			}

		// Automatické prekresľovanie

			/*packagePrivate*/ static void automatickéPrekreslenie()
			{
				if (nekresli)
					neboloPrekreslené = true;
				else
					prekresli();
			}


		// Vstupný riadok a interaktívny režim

			// Pohyb po histórii vstupného riadka (spustené len ak je aktívna)

				private static void pohybPoHistórii(KeyEvent e)
				{
					if (!(e.isAltDown() || e.isAltGraphDown() ||
						e.isControlDown() || e.isMetaDown() ||
						e.isShiftDown()))
					{
						// Pohyb späť v histórii potvrdených riadkov
						// (príkazov):
						if (e.getKeyCode() == KeyEvent.VK_UP)
						{
							// Pohyb skončí na prvej položke zoznamu:
							if (históriaVstupnéhoRiadka.počítadlo() > 0)
							{
								// Ak bola aktuálna pozícia histórie
								// za koncom zoznamu, tak sa uchová
								// do záložnej premennej reťazec
								// aktuálneho riadka:
								if (históriaVstupnéhoRiadka.počítadlo() ==
									históriaVstupnéhoRiadka.veľkosť())
									aktuálnyVstupnýRiadok =
										vstupnýRiadok.getText();

								vstupnýRiadok.setText(
									históriaVstupnéhoRiadka.
									predchádzajúci());
							}
							else Svet.pípni();
						}
						// Pohyb vpred v histórii potvrdených riadkov
						// (príkazov):
						else if (e.getKeyCode() == KeyEvent.VK_DOWN)
						{
							// Pohyb sa končí za poslednou položkou zoznamu:
							if (históriaVstupnéhoRiadka.počítadlo() <
								históriaVstupnéhoRiadka.veľkosť())
							{
								// Ak bola aktuálna pozícia na poslednej
								// položke, tak sa aktuálna pozícia nastaví
								// za poslednú položku v zozname
								// a do vstupného riadka sa vráti zálohovaná
								// hodnota aktuálneho riadka:
								if (históriaVstupnéhoRiadka.počítadlo() ==
									históriaVstupnéhoRiadka.veľkosť() - 1)
								{
									vstupnýRiadok.setText(
										aktuálnyVstupnýRiadok);
									históriaVstupnéhoRiadka.počítadlo(
										históriaVstupnéhoRiadka.veľkosť());
								}
								else
									vstupnýRiadok.setText(
										históriaVstupnéhoRiadka.ďalší());
							}
							else Svet.pípni();
						}
					}
				}


	// ------------------------------- //
	//  *** Rôzne súkromné triedy ***  //
	// ------------------------------- //


		// Upravené komponenty na použitie v dialógoch na čítanie údajov
		// Zdroj: https://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html
		@SuppressWarnings("serial")
		private static class RobotTextField extends JTextField
		{
			public RobotTextField()
			{
				addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						for (Component component = getParent();
							null != component; component =
							component.getParent())
						{
							if (component instanceof JOptionPane)
							{
								JOptionPane pane = (JOptionPane)component;
								pane.setValue(odpovedeZadania[0]);

								// Java chápe skrytie okna ako zavretie
								pane.setVisible(false);
								break;
							}
						}
					}
				});
			}
		}


		@SuppressWarnings("serial")
		private static class RobotPasswordField extends JPasswordField
		{
			public RobotPasswordField()
			{
				addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						for (Component component = getParent();
							null != component; component =
							component.getParent())
						{
							if (component instanceof JOptionPane)
							{
								JOptionPane pane = (JOptionPane)component;
								pane.setValue(odpovedeZadania[0]);
								pane.setVisible(false);
								break;
							}
						}
					}
				});
			}
		}


		// Časovač
		private static class Časovač implements ActionListener
		{
			private int čas = 40;
			private Timer časovanie;
			private Časovač() { časovanie = null; }

			public void actionPerformed(ActionEvent evt)
			{
				ÚdajeUdalostí.poslednýTik = evt;

				// boolean nekresli_záloha = nekresli;
				žiadamPrekresleniePoPráci = false;
				pracujem =
					// nekresli =
					true;

				GRobot.aspoňJedenAktívny = false;
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					GRobot.zoznamZmenený1 = false;
					if (GRobot.zámokZoznamuRobotov1)
					{
						GRobotException.vypíšChybovéHlásenie(
							"Príliš veľa požiadaviek časovača…");
					}
					else
					{
						GRobot.zámokZoznamuRobotov1 = true;

						for (GRobot tento : GRobot.zoznamRobotov)
						{
							tento.pracuj();
							if (GRobot.zoznamZmenený1)
							{
								GRobotException.vypíšChybovéHlásenie(
									"Zoznam robotov bol počas práce " +
									"zmenený, preto nie všetky roboty " +
									"mohli v tomto pracovnom cykle " +
									"vykonať svoju činnosť!");
								break;
							}
						}

						GRobot.zámokZoznamuRobotov1 = false;
						GRobot.zlúčiťZoznamy();
					}

					for (Obrázok animácia : Obrázok.animácie)
						animácia.animuj();

					if (!Vlnenie.vlnenia.isEmpty())
					{
						boolean vlnenieAktívne = false;

						for (Vlnenie vlnenie : Vlnenie.vlnenia)
							if (vlnenie.aktívne())
							{
								vlnenieAktívne = true;
								vlnenie.vykonaj();
							}

						if (vlnenieAktívne)
							automatickéPrekreslenie();
					}

					if (null != ObsluhaUdalostí.počúvadlo)
						ObsluhaUdalostí.počúvadlo.tik();

					for (GRobot počúvajúci : GRobot.počúvajúciSystém)
						počúvajúci.tik();
				}

				// nekresli = nekresli_záloha;
				pracujem = false;
				if (žiadamPrekresleniePoPráci)
				{
					/*if (nekresli)
						neboloPrekreslené = true;
					else*/
						prekresli(); // noInvokeLater
				}
				else if (GRobot.aspoňJedenAktívny)
					automatickéPrekreslenie();
			}

			public void spusti(int čas)
			{
				if (null == časovanie)
				{
					časovanie = new Timer(this.čas = čas, časovač);
					časovanie.start();
				}
				else
				{
					časovanie.stop();
					časovanie.setInitialDelay(this.čas = čas);
					časovanie.setDelay(čas);
					časovanie.start();
				}
			}

			public void spusti() { spusti(čas); }

			public void odlož(int čas)
			{
				if (null == časovanie)
				{
					časovanie = new Timer(čas, časovač);
					časovanie.setInitialDelay(čas);
					časovanie.setDelay(this.čas);
					časovanie.start();
				}
				else
				{
					časovanie.stop();
					časovanie.setInitialDelay(čas);
					časovanie.start();
				}
			}

			public boolean aktívny() { return null != časovanie &&
				časovanie.isRunning(); }

			public int interval()
			{ return null == časovanie ? čas : časovanie.getDelay(); }

			public void zastav()
			{
				if (null != časovanie) časovanie.stop();
			}
		}

		private final static Časovač časovač = new Časovač();

		// Udalosti okna – ObsluhaUdalostí.počúvadlo myši, klávesnice a udalostí komponentov
		private static class UdalostiOkna implements MouseListener,
			MouseMotionListener, MouseWheelListener, KeyListener,
			ComponentListener, WindowFocusListener, WindowListener,
			WindowStateListener
		{
			public void mouseClicked(MouseEvent e)
			{
				if (čakajNaKlik)
				{
					udalosťČakaniaNaKlik = e;
					čakajNaKlik = false;
					return;
				}

				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.poslednáUdalosťMyši = e;
					hlavnýPanel.requestFocusInWindow();

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.klik();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.klik();
						}
					}
				}
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(e.getX());
					ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(e.getY());
					e.translatePoint(
						(int)ÚdajeUdalostí.súradnicaMyšiX - e.getX(),
						(int)ÚdajeUdalostí.súradnicaMyšiY - e.getY());

					if (e.getButton() == MouseEvent.BUTTON1)
					{
						ÚdajeUdalostí.tlačidloMyši = 1;
						ÚdajeUdalostí.tlačidloMyši1 = true;
					}
					else if (e.getButton() == MouseEvent.BUTTON2)
					{
						ÚdajeUdalostí.tlačidloMyši = 2;
						ÚdajeUdalostí.tlačidloMyši2 = true;
					}
					else if (e.getButton() == MouseEvent.BUTTON3)
					{
						ÚdajeUdalostí.tlačidloMyši = 3;
						ÚdajeUdalostí.tlačidloMyši3 = true;
					}
					else
						ÚdajeUdalostí.tlačidloMyši = 0;

					ÚdajeUdalostí.poslednáUdalosťMyši = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.stlačenieTlačidlaMyši();
							ObsluhaUdalostí.počúvadlo.stlacenieTlacidlaMysi();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.stlačenieTlačidlaMyši();
							počúvajúci.stlacenieTlacidlaMysi();
						}
					}
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(e.getX());
					ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(e.getY());
					e.translatePoint(
						(int)ÚdajeUdalostí.súradnicaMyšiX - e.getX(),
						(int)ÚdajeUdalostí.súradnicaMyšiY - e.getY());

					if (e.getButton() == MouseEvent.BUTTON1)
					{
						ÚdajeUdalostí.tlačidloMyši = 1;
						ÚdajeUdalostí.tlačidloMyši1 = false;
					}
					else if (e.getButton() == MouseEvent.BUTTON2)
					{
						ÚdajeUdalostí.tlačidloMyši = 2;
						ÚdajeUdalostí.tlačidloMyši2 = false;
					}
					else if (e.getButton() == MouseEvent.BUTTON3)
					{
						ÚdajeUdalostí.tlačidloMyši = 3;
						ÚdajeUdalostí.tlačidloMyši3 = false;
					}
					else
						ÚdajeUdalostí.tlačidloMyši = 0;

					ÚdajeUdalostí.poslednáUdalosťMyši = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.uvoľnenieTlačidlaMyši();
							ObsluhaUdalostí.počúvadlo.uvolnenieTlacidlaMysi();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.uvoľnenieTlačidlaMyši();
							počúvajúci.uvolnenieTlacidlaMysi();
						}
					}
				}
			}

			public void mouseMoved(MouseEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(e.getX());
					ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(e.getY());
					e.translatePoint(
						(int)ÚdajeUdalostí.súradnicaMyšiX - e.getX(),
						(int)ÚdajeUdalostí.súradnicaMyšiY - e.getY());

					ÚdajeUdalostí.poslednáUdalosťMyši = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.pohybMyši();
							ObsluhaUdalostí.počúvadlo.pohybMysi();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.pohybMyši();
							počúvajúci.pohybMysi();
						}
					}
				}
			}

			public void mouseDragged(MouseEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(e.getX());
					ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(e.getY());
					e.translatePoint(
						(int)ÚdajeUdalostí.súradnicaMyšiX - e.getX(),
						(int)ÚdajeUdalostí.súradnicaMyšiY - e.getY());

					ÚdajeUdalostí.poslednáUdalosťMyši = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.ťahanieMyšou();
							ObsluhaUdalostí.počúvadlo.tahanieMysou();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.ťahanieMyšou();
							počúvajúci.tahanieMysou();
						}
					}
				}
			}

			public void mouseWheelMoved(MouseWheelEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokMyši)
				{
					ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(e.getX());
					ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(e.getY());
					e.translatePoint(
						(int)ÚdajeUdalostí.súradnicaMyšiX - e.getX(),
						(int)ÚdajeUdalostí.súradnicaMyšiY - e.getY());

					ÚdajeUdalostí.poslednáUdalosťMyši =
						ÚdajeUdalostí.poslednáUdalosťRolovania = e;

					if (e.isShiftDown())
					{
						ÚdajeUdalostí.rolovanieKolieskomMyšiX =
							e.getWheelRotation();
						ÚdajeUdalostí.rolovanieKolieskomMyšiY = 0;
					}
					else
					{
						ÚdajeUdalostí.rolovanieKolieskomMyšiX = 0;
						ÚdajeUdalostí.rolovanieKolieskomMyšiY =
							-e.getWheelRotation();
					}

					if (GRobot.podlaha.rolujTexty) GRobot.podlaha.rolujTexty();
					if (GRobot.strop.rolujTexty) GRobot.strop.rolujTexty();

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.rolovanieKolieskomMyši();
							ObsluhaUdalostí.počúvadlo.rolovanieKolieskomMysi();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciMyš)
						{
							počúvajúci.rolovanieKolieskomMyši();
							počúvajúci.rolovanieKolieskomMysi();
						}
					}
				}
			}

			public void keyPressed(KeyEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokKlávesnice)
				{
					ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.stlačenieKlávesu();
							ObsluhaUdalostí.počúvadlo.stlacenieKlavesu();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciKlávesnicu)
						{
							počúvajúci.stlačenieKlávesu();
							počúvajúci.stlacenieKlavesu();
						}
					}
				}
			}

			public void keyReleased(KeyEvent e)
			{
				synchronized (ÚdajeUdalostí.zámokKlávesnice)
				{
					ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.uvoľnenieKlávesu();
							ObsluhaUdalostí.počúvadlo.uvolnenieKlavesu();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciKlávesnicu)
						{
							počúvajúci.uvoľnenieKlávesu();
							počúvajúci.uvolnenieKlavesu();
						}
					}
				}
			}

			public void keyTyped(KeyEvent e)
			{
				if (čakajNaKláves)
				{
					udalosťČakaniaNaKláves = e;
					čakajNaKláves = false;
					return;
				}

				synchronized (ÚdajeUdalostí.zámokKlávesnice)
				{
					ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.zadanieZnaku();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciKlávesnicu)
						{
							počúvajúci.zadanieZnaku();
						}
					}
				}
			}


			public void componentHidden(ComponentEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.skrytieOkna();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.skrytieOkna();
					}
				}
			}

			public void componentShown(ComponentEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.zobrazenieOkna();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.zobrazenieOkna();
					}
				}
			}

			public void componentMoved(ComponentEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.presunutieOkna();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.presunutieOkna();
					}
				}
			}

			public void componentResized(ComponentEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.zmenaVeľkostiOkna();
						ObsluhaUdalostí.počúvadlo.zmenaVelkostiOkna();
					}
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.zmenaVeľkostiOkna();
						počúvajúci.zmenaVelkostiOkna();
					}
				}

				if (Plátno.konzolaPoužitá)
				{
					if (null == ObsluhaUdalostí.počúvadlo)
					{
						/*if (nekresli)
							neboloPrekreslené = true;
						else*/
							prekresli(); // noInvokeLater
					}
					else
						automatickéPrekreslenie();
				}
			}

			public void windowGainedFocus(WindowEvent e) {}
			public void windowLostFocus(WindowEvent e) {}


			public void windowActivated(WindowEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.aktiváciaOkna();
						ObsluhaUdalostí.počúvadlo.aktivaciaOkna();
					}
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.aktiváciaOkna();
						počúvajúci.aktivaciaOkna();
					}
				}
			}

			public void windowDeactivated(WindowEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.deaktiváciaOkna();
						ObsluhaUdalostí.počúvadlo.deaktivaciaOkna();
					}
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.deaktiváciaOkna();
						počúvajúci.deaktivaciaOkna();
					}
				}
			}


			public void windowClosed(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}

			public void windowOpened(WindowEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.otvorenie();
					}
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.otvorenie();
					}
				}
			}

			public void windowClosing(WindowEvent e)
			{
				ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;
				boolean zavrieť = true;

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						if (!ObsluhaUdalostí.počúvadlo.zavretie())
							zavrieť = false;
						if (!ObsluhaUdalostí.počúvadlo.zatvorenie())
							zavrieť = false;
					}
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						if (!počúvajúci.zavretie())
							zavrieť = false;
						if (!počúvajúci.zatvorenie())
							zavrieť = false;
					}
				}

				if (zavrieť) koniec();
			}


			public void windowStateChanged(WindowEvent e)
			{
				int stav = e.getNewState();

				if (NORMAL == stav)
				{
					poslednáŠírka = svet.getWidth();
					poslednáVýška = svet.getHeight();
					poslednéX = svet.getLocation().x;
					poslednéY = svet.getLocation().y;

				// Nasledujúci „divne odsadený“ komentár zahŕňa vetvu else:
				// 	System.out.println("  ukladám[" + poslednéX + "," +
				// 		poslednéY + "," + poslednáŠírka + "," + poslednáVýška +
				// 		"]");
				// }
				// else
				// {
				// 	System.out.println("  zachovávam[" + poslednéX + "," +
				// 		poslednéY + "," + poslednáŠírka + "," + poslednáVýška +
				// 		"]");
				}

				// Ak ma má zaujímať len cieľový stav, musím najprv
				// kontrolovať minimalizáciu, pretože pri „ikonifikácii“
				// z maximalizovaného stavu je tento stav uchovaný
				// a prekryl by minimalizáciu
				if (0 != (stav & ICONIFIED))
				{
					ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

					if (null != ObsluhaUdalostí.počúvadlo)
					{
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.minimalizovanie();
						}
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							počúvajúci.minimalizovanie();
						}
					}
				}
				else if (0 != (stav & MAXIMIZED_BOTH))
				{
					ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

					if (null != ObsluhaUdalostí.počúvadlo)
					{
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.maximalizovanie();
						}
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							počúvajúci.maximalizovanie();
						}
					}
				}
				else if (NORMAL == stav)
				{
					ÚdajeUdalostí.poslednáUdalosťAktivityOkna = e;

					if (null != ObsluhaUdalostí.počúvadlo)
					{
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.obnovenie();
						}
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							počúvajúci.obnovenie();
						}
					}
				}
			}
		}

		// Inštancia spracúvajúca udalosti okna
		/*packagePrivate*/ final static UdalostiOkna udalostiOkna =
			new UdalostiOkna();


	// Nie je možné vytvárať inštancie sveta
	private Svet()
	{
		super();
		setFont(Plátno.predvolenéPísmoKonzoly);
	}

	private final static Svet svet = new Svet();

	/*packagePrivate*/ static Svet dajSvet() { return svet; }


	// --- Vlastnosti sveta a funkcie sveta


		// Verzia

		/**
		 * <p>Porovná aktuálnu verziu robota so zadanou.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Svet Svet}.{@code currverzia}({@code num1}, {@code num50}) &lt; {@code num0})
			{
				{@link Svet Svet}.{@link Svet#pípni() pípni}();
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Na fungovanie tejto aplikácie\n"} +
					{@code srg"je potrebná vyššia verzia triedy GRobot!\n"} +
					{@code srg"(Najmenej 1.50)"});
				{@link Svet Svet}.{@link Svet#koniec() koniec}();
			}
			</pre>
		 * @return ak je zadaná verzia zhodná s aktuálnou, je vrátená
		 *     hodnota 0; ak je aktuálna verzia nižšia, než zadaná, je
		 *     vrátená záporná hodnota, inak kladná
		 */
		public static int verzia(int hlavná, int vedľajšia)
		{
			return
				(majorVersion * 100 + minorVersion) -
				(hlavná * 100 + vedľajšia);
		}


		// Hlavný robot a všetci roboti

		/**
		 * <p><a class="getter"></a> Vráti hlavného robota. Hlavný robot je
		 * predvolene prvý vytvorený robot. Tento robot je považovaný za
		 * vlastníka sveta. On asistuje pri vytvorení okna aplikácie,
		 * pomocou neho sú spracované niektoré úlohy… O svoje privilégium
		 * by mohol prísť jedine v prípade, že by bol (pravdepodobne omylom)
		 * {@linkplain #uvoľni(GRobot) uvoľnený}. V takom prípade by bol
		 * nahradený najbližším definovaným robotom s posunutím všetkých
		 * privilégií jemu. Problém by mohol nastať, keby nebol k dispozícii
		 * žiadny ďalší robot, ktorý by ho nahradil.</p>
		 * 
		 * @return hlavný (prvý) robot
		 */
		public static GRobot hlavnýRobot() { return hlavnýRobot; }

		/** <p><a class="alias"></a> Alias pre {@link #hlavnýRobot() hlavnýRobot}.</p> */
		public static GRobot hlavnyRobot() { return hlavnýRobot; }


		/**
		 * <p>Spustením tejto metódy sa pre každého robota (podľa ich
		 * aktuálneho poradia) vykoná reakcia {@link 
		 * GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy} bez určenia
		 * autora výzvy (autor je rovný {@code valnull}) a s hodnotou
		 * argumentu {@code kľúč} rovnou {@code -}{@code num1}. Prvotné
		 * poradie robotov je určené poradím ich vytvorenia (čiže ak sme
		 * nijako neovplyvnili poradie robotov, je prvý vyzvaný ten robot,
		 * ktorý bol vytvorený ako prvý, to jest robot, ktorý je nakreslený
		 * pod všetkými ostatnými robotmi). Dodatočne môžeme poradie
		 * ovplyvniť volaním metód {@link GRobot#naVrch() naVrch}, {@link 
		 * GRobot#naSpodok() naSpodok}, {@link GRobot#vyššie() vyššie},
		 * {@link GRobot#nižšie() nižšie}, {@link GRobot#pred(GRobot) pred}
		 * a {@link GRobot#za(GRobot) za}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak je poradie robotov zmenené
		 * počas {@linkplain GRobot#prijatieVýzvy(GRobot, int) spracovania
		 * výzviev}, dôjde z technických príčin k opätovnému spusteniu
		 * posielania výziev (inak povedané – posielanie výziev sa
		 * „reštartuje“). To znamená, že niektorí roboti budú vyzvaní dva
		 * alebo viac ráz. Buďte preto opatrní so zmenami poradia v rámci
		 * spracovania výziev, aby ste nespôsobili vznik nekonečného
		 * cyklu… (Rovnaký efekt má prípadné vytvorenie nového robota,
		 * resp. ľubovoľnej inštancie triedy odvodenej od robota.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Mechanizmus výziev nie je taký
		 * efektívny ako definícia vlastného {@linkplain Zoznam zoznamu robotov}
		 * (prípadne inštancií odvodených tried) a vykonanie hromadnej akcie
		 * pomocou tohto zoznamu. Slúži predovšedkým na umožnenie hromadného
		 * spracovania bez nevyhnutnosti vytvárania takého zoznamu (napríklad
		 * pri malom počte robotov alebo predtým, než sa programátor
		 * podrobnejšie oboznámi s možnosťami zoznamov).</p>
		 * 
		 * <p>Príklad použitia mechanizmu výziev je napríklad
		 * v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @see GRobot#prijatieVýzvy(GRobot, int)
		 * @see #vyzviRobotov(int)
		 * @see #vyzviRobotov(int, boolean)
		 * @see GRobot#vyzviRobotov()
		 */
		public static void vyzviRobotov()
		{
			if (GRobot.zámokZoznamuRobotov2)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Zdvojená výzva bola zamietnutá!");
			}
			else
			{
				GRobot.zámokZoznamuRobotov2 = true;

				boolean reštart;
				do {
					GRobot.zoznamZmenený2 = false;
					reštart = false;
					for (GRobot tento : GRobot.zoznamRobotov)
					{
						tento.prijatieVýzvy(null, -1);
						tento.prijatieVyzvy(null, -1);
						if (GRobot.zoznamZmenený2)
						{
							reštart = true;
							break;
						}
					}
				} while (reštart);

				GRobot.zámokZoznamuRobotov2 = false;
				GRobot.zlúčiťZoznamy();
			}
		}

		/**
		 * <p>Spustením tejto metódy sa pre každého robota (podľa ich
		 * aktuálneho poradia) vykoná reakcia {@link 
		 * GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy} bez určenia
		 * autora výzvy (autor je rovný {@code valnull}). Pomocou argumentu
		 * {@code kľúč} môžeme odlišovať rôzne druhy výziev a ovplyvňovať
		 * tým správanie reakcie {@link GRobot#prijatieVýzvy(GRobot, int)
		 * prijatieVýzvy}. Prvotné poradie robotov je určené poradím ich
		 * vytvorenia (čiže ak sme nijako neovplyvnili poradie robotov, je
		 * prvý vyzvaný ten robot, ktorý bol vytvorený ako prvý, to jest
		 * robot, ktorý je nakreslený pod všetkými ostatnými robotmi).
		 * Dodatočne môžeme poradie ovplyvniť volaním metód
		 * {@link GRobot#naVrch() naVrch}, {@link GRobot#naSpodok()
		 * naSpodok}, {@link GRobot#vyššie() vyššie}, {@link GRobot#nižšie()
		 * nižšie}, {@link GRobot#pred(GRobot) pred} a {@link 
		 * GRobot#za(GRobot) za}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak je poradie robotov zmenené
		 * počas {@linkplain GRobot#prijatieVýzvy(GRobot, int) spracovania
		 * výzviev}, dôjde z technických príčin k opätovnému spusteniu
		 * posielania výziev (inak povedané – posielanie výziev sa
		 * „reštartuje“). To znamená, že niektorí roboti budú vyzvaní dva
		 * alebo viac ráz. Buďte preto opatrní so zmenami poradia v rámci
		 * spracovania výziev, aby ste nespôsobili vznik nekonečného
		 * cyklu… (Rovnaký efekt má prípadné vytvorenie nového robota,
		 * resp. ľubovoľnej inštancie triedy odvodenej od robota.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Mechanizmus výziev nie je taký
		 * efektívny ako definícia vlastného {@linkplain Zoznam zoznamu robotov}
		 * (prípadne inštancií odvodených tried) a vykonanie hromadnej akcie
		 * pomocou tohto zoznamu. Slúži predovšedkým na umožnenie hromadného
		 * spracovania bez nevyhnutnosti vytvárania takého zoznamu (napríklad
		 * pri malom počte robotov alebo predtým, než sa programátor
		 * podrobnejšie oboznámi s možnosťami zoznamov).</p>
		 * 
		 * <p>Príklad použitia mechanizmu výziev je napríklad
		 * v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @param kľúč celočíselná hodnota, ktorá je poslaná do reakcie
		 *     {@link GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy}
		 *     každého robota; takto je možné odlíšiť rôzne druhy výziev,
		 *     vďaka čomu môžeme ovplyvniť správanie reakcie {@link 
		 *     GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy}
		 * 
		 * @see GRobot#prijatieVýzvy(GRobot, int)
		 * @see #vyzviRobotov()
		 * @see #vyzviRobotov(int, boolean)
		 * @see GRobot#vyzviRobotov(int)
		 */
		public static void vyzviRobotov(int kľúč)
		{
			if (GRobot.zámokZoznamuRobotov2)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Zdvojená výzva bola zamietnutá!");
			}
			else
			{
				GRobot.zámokZoznamuRobotov2 = true;

				boolean reštart;
				do {
					GRobot.zoznamZmenený2 = false;
					reštart = false;
					for (GRobot tento : GRobot.zoznamRobotov)
					{
						tento.prijatieVýzvy(null, kľúč);
						tento.prijatieVyzvy(null, kľúč);
						if (GRobot.zoznamZmenený2)
						{
							reštart = true;
							break;
						}
					}
				} while (reštart);

				GRobot.zámokZoznamuRobotov2 = false;
				GRobot.zlúčiťZoznamy();
			}
		}

		/**
		 * <p>Spustením tejto metódy sa pre každého robota (podľa ich
		 * aktuálneho poradia) vykoná reakcia {@link 
		 * GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy} bez určenia
		 * autora výzvy (autor je rovný {@code valnull}). Pomocou argumentu
		 * {@code kľúč} môžeme odlišovať rôzne druhy výziev a ovplyvňovať
		 * tým správanie reakcie {@link GRobot#prijatieVýzvy(GRobot, int)
		 * prijatieVýzvy}. Argument {@code obrátene} určuje smer
		 * spracovania. Ak je rovný {@code valtrue}, zoznam robotov bude
		 * spracovaný od konca, to znamená, že prvý bude vyzvaný buď
		 * naposledy vytvorený robot, alebo robot aktuálne umiestnený na
		 * konci vnútorného zoznamu robotov. Záleží od toho, či sme
		 * dodatočne ovplyvňovali poradie robotov metódami {@link 
		 * GRobot#naVrch() naVrch}, {@link GRobot#naSpodok() naSpodok},
		 * {@link GRobot#vyššie() vyššie}, {@link GRobot#nižšie() nižšie},
		 * {@link GRobot#pred(GRobot) pred} a {@link GRobot#za(GRobot) za}.
		 * Ak je hodnota argumentu {@code obrátene} rovná {@code valfalse},
		 * metóda sa správa rovnako ako {@link #vyzviRobotov(int)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak je poradie robotov zmenené
		 * počas {@linkplain GRobot#prijatieVýzvy(GRobot, int) spracovania
		 * výzviev}, dôjde z technických príčin k opätovnému spusteniu
		 * posielania výziev (inak povedané – posielanie výziev sa
		 * „reštartuje“). To znamená, že niektorí roboti budú vyzvaní dva
		 * alebo viac ráz. Buďte preto opatrní so zmenami poradia v rámci
		 * spracovania výziev, aby ste nespôsobili vznik nekonečného
		 * cyklu… (Rovnaký efekt má prípadné vytvorenie nového robota,
		 * resp. ľubovoľnej inštancie triedy odvodenej od robota.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Mechanizmus výziev nepovažujeme
		 * za taký efektívny ako je definícia vlastného {@linkplain Zoznam
		 * zoznamu robotov} (prípadne inštancií odvodených tried) a nasledujúce
		 * vykonanie hromadnej akcie pomocou tohto vlastného zoznamu. Výzvy
		 * slúžia predovšedkým na umožnenie hromadného spracovania bez
		 * nevyhnutnosti vytvárania takého zoznamu (napríklad v prípade, keď
		 * nepovažujeme vytvorenie vlastného zoznamu za významný prínos,
		 * alebo predtým, než sa programátor podrobnejšie oboznámi
		 * s možnosťami zoznamov).</p>
		 * 
		 * <p>Príklad použitia mechanizmu výziev je napríklad
		 * v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @param kľúč celočíselná hodnota, ktorá je poslaná do reakcie
		 *     {@link GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy}
		 *     každého robota; takto je možné odlíšiť rôzne druhy výziev,
		 *     vďaka čomu môžeme ovplyvniť správanie reakcie {@link 
		 *     GRobot#prijatieVýzvy(GRobot, int) prijatieVýzvy}
		 * @param obrátene týmto argumentom môžeme ovplyvniť smer
		 *     spracovania;
		 *     {@code valtrue} znamená spracovanie vnútorného zoznamu
		 *     robotov od konca; {@code valfalse} znamená rovnaký spôsob
		 *     spracovania ako pri metóde {@link #vyzviRobotov(int)}
		 * 
		 * @see GRobot#prijatieVýzvy(GRobot, int)
		 * @see #vyzviRobotov()
		 * @see #vyzviRobotov(int)
		 * @see GRobot#vyzviRobotov(int, boolean)
		 */
		public static void vyzviRobotov(int kľúč, boolean obrátene)
		{
			if (GRobot.zámokZoznamuRobotov2)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Zdvojená výzva bola zamietnutá!");
			}
			else
			{
				GRobot.zámokZoznamuRobotov2 = true;

				if (obrátene)
				{
					boolean reštart;
					do {
						GRobot.zoznamZmenený2 = false;
						reštart = false;
						for (int i = GRobot.zoznamRobotov.size() - 1; i >= 0; --i)
						{
							GRobot tento = GRobot.zoznamRobotov.get(i);
							tento.prijatieVýzvy(null, kľúč);
							tento.prijatieVyzvy(null, kľúč);
							if (GRobot.zoznamZmenený2)
							{
								reštart = true;
								break;
							}
						}
					} while (reštart);
				}
				else
				{
					boolean reštart;
					do {
						GRobot.zoznamZmenený2 = false;
						reštart = false;
						for (GRobot tento : GRobot.zoznamRobotov)
						{
							tento.prijatieVýzvy(null, kľúč);
							tento.prijatieVyzvy(null, kľúč);
							if (GRobot.zoznamZmenený2)
							{
								reštart = true;
								break;
							}
						}
					} while (reštart);
				}

				GRobot.zámokZoznamuRobotov2 = false;
				GRobot.zlúčiťZoznamy();
			}
		}


		// Rozmery

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu x-ovú súradnicu sveta.
		 * Plátna podlahy a stropu majú obmedzenú veľkosť, ktorá je o niečo
		 * väčšia ako predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najmenšia x-ová súradnica plátien
		 * 
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public static double najmenšieX() { return -Plátno.šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double najmensieX() { return -Plátno.šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double minimálneX() { return -Plátno.šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double minimalneX() { return -Plátno.šírkaPlátna / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu y-ovú súradnicu sveta.
		 * Plátna podlahy a stropu majú obmedzenú veľkosť, ktorá je o niečo
		 * väčšia ako predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najmenšia y-ová súradnica plátien
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najväčšieY()
		 */
		public static double najmenšieY() { return -(Plátno.výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double najmensieY() { return -(Plátno.výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double minimálneY() { return -(Plátno.výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double minimalneY() { return -(Plátno.výškaPlátna - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu x-ovú súradnicu sveta.
		 * Plátna podlahy a stropu majú obmedzenú veľkosť, ktorá je o niečo
		 * väčšia ako predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najväčšia x-ová súradnica plátien
		 * 
		 * @see #najmenšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public static double najväčšieX() { return (Plátno.šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double najvacsieX() { return (Plátno.šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double maximálneX() { return (Plátno.šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double maximalneX() { return (Plátno.šírkaPlátna - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu y-ovú súradnicu sveta.
		 * Plátna podlahy a stropu majú obmedzenú veľkosť, ktorá je o niečo
		 * väčšia ako predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najväčšia y-ová súradnica plátien
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 */
		public static double najväčšieY() { return Plátno.výškaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double najvacsieY() { return Plátno.výškaPlátna / 2; }


		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double maximálneY() { return Plátno.výškaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double maximalneY() { return Plátno.výškaPlátna / 2; }

		/**
		 * <p>Zistí šírku plátien sveta.</p>
		 * 
		 * @return šírka plátien sveta
		 */
		public static int šírka() { return Plátno.šírkaPlátna; }

		/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
		public static int sirka() { return Plátno.šírkaPlátna; }

		/**
		 * <p>Zistí výšku plátien sveta.</p>
		 * 
		 * @return výška plátien sveta
		 */
		public static int výška() { return Plátno.výškaPlátna; }

		/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
		public static int vyska() { return Plátno.výškaPlátna; }

		/**
		 * <p>Zistí aktuálnu šírku tzv. klientskej oblasti okna. Ide
		 * o viditeľnú časť komponentu okna zahŕňajúceho {@linkplain 
		 * Svet#farbaPlochy() obvykle šedú plochu} s predvolene bielou
		 * kresliacou oblasťou a prípadne ďalšími komponentmi ({@linkplain 
		 * #neskrývajVstupnýRiadok(boolean) vstupným riadkom}, {@linkplain 
		 * Tlačidlo tlačidlami}…). Ak je okno užšie, než sú rozmery plátien
		 * sveta, tak ide v podstate o viditeľnú šírku plátien.</p>
		 * 
		 * @return šírka klientskej oblasti okna (ak je okno užšie, než
		 *     plátno, tak ide o viditeľnú časť plátien sveta)
		 */
		public static int viditeľnáŠírka() { return hlavnýPanel.getWidth(); }

		/** <p><a class="alias"></a> Alias pre {@link #viditeľnáŠírka() viditeľnáŠírka}.</p> */
		public static int viditelnaSirka() { return hlavnýPanel.getWidth(); }

		/**
		 * <p>Zistí viditeľnú výšku tzv. klientskej oblasti okna. Pozri aj
		 * opis metódy {@link #viditeľnáŠírka() viditeľnáŠírka()} – rovnaké
		 * informácie platia aj pre túto metódu
		 * ({@code currviditeľnáVýška}).</p>
		 * 
		 * @return výška klientskej oblasti okna (ak je okno menšie, než
		 *     výška plátna, tak ide o viditeľnú časť plátien sveta)
		 */
		public static int viditeľnáVýška() { return hlavnýPanel.getHeight(); }

		/** <p><a class="alias"></a> Alias pre {@link #viditeľnáVýška() viditeľnáVýška}.</p> */
		public static int viditelnaVyska() { return hlavnýPanel.getHeight(); }

		// Rozmery a poloha hlavného okna

		/**
		 * <p>Zistí šírku hlavného okna aplikácie.</p>
		 * 
		 * @return šírka hlavného okna aplikácie
		 */
		public static int šírkaOkna()
		{
			if (null != oknoCelejObrazovky)
				return oknoCelejObrazovky.getWidth();
			if (null != svet) return svet.getWidth();
			return počiatočnáŠírka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #šírkaOkna() šírkaOkna}.</p> */
		public static int sirkaOkna() { return šírkaOkna(); }

		/**
		 * <p>Zistí výšku hlavného okna aplikácie.</p>
		 * 
		 * @return výška hlavného okna aplikácie
		 */
		public static int výškaOkna()
		{
			if (null != oknoCelejObrazovky)
				return oknoCelejObrazovky.getHeight();
			if (null != svet) return svet.getHeight();
			return počiatočnáŠírka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #výškaOkna() výškaOkna}.</p> */
		public static int vyskaOkna() { return výškaOkna(); }


		// Titulok

		/**
		 * <p>Vráti titulok hlavného okna aplikácie. Ak metóda zistí, že
		 * aktuálny titulok sa zhoduje s predvoleným titulkom (prípadne ak
		 * hlavné okno ešte nejestvuje), tak vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * @return titulok hlavného okna aplikácie alebo {@code valnull}
		 */
		public static String titulok()
		{
			if (null != oknoCelejObrazovky)
			{
				String titulok = oknoCelejObrazovky.getTitle();
				if (versionString.equals(titulok)) titulok = null;
				return titulok;
			}

			if (null != svet)
			{
				String titulok = svet.getTitle();
				if (versionString.equals(titulok)) titulok = null;
				return titulok;
			}

			return null;
		}

		/**
		 * <p>Nastaví titulok hlavného okna aplikácie. Ak je zadaná hodnota
		 * {@code valnull}, tak metóda nastaví predvolený titulok okna.</p>
		 * 
		 * @param titulok nový titulok hlavného okna aplikácie alebo
		 *     {@code valnull} na nastavenie predvoleného titulku okna
		 */
		public static void titulok(String titulok)
		{
			if (null == titulok) titulok = versionString;
			if (null != svet) svet.setTitle(titulok);
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setTitle(titulok);
		}


		// Okraje

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu aktuálne viditeľnú x-ovú súradnicu
		 * plátien (podlahy alebo stropu).</p>
		 * 
		 * @return najmenšia viditeľná x-ová súradnica plátien
		 * 
		 * @see #pravýOkraj()
		 * @see #spodnýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double ľavýOkraj()
		{
			int súradnica = -hlavnýPanel.getWidth() / 2;

			if (súradnica < (-Plátno.šírkaPlátna / 2))
				return -Plátno.šírkaPlátna / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #ľavýOkraj() ľavýOkraj}.</p> */
		public static double lavyOkraj() { return ľavýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu aktuálne viditeľnú y-ovú súradnicu
		 * plátien (podlahy alebo stropu).</p>
		 * 
		 * @return najmenšia viditeľná y-ová súradnica plátien
		 * 
		 * @see #ľavýOkraj()
		 * @see #pravýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double spodnýOkraj()
		{
			int súradnica = -(hlavnýPanel.getHeight() - 1) / 2;

			if (súradnica < (-(Plátno.výškaPlátna - 1) / 2))
				return -(Plátno.výškaPlátna - 1) / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #spodnýOkraj() spodnýOkraj}.</p> */
		public static double spodnyOkraj() { return spodnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #spodnýOkraj() spodnýOkraj}.</p> */
		public static double dolnýOkraj() { return spodnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #dolnýOkraj() dolnýOkraj}.</p> */
		public static double dolnyOkraj() { return spodnýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu aktuálne viditeľnú x-ovú súradnicu
		 * plátien (podlahy alebo stropu).</p>
		 * 
		 * @return najväčšia viditeľná x-ová súradnica plátien
		 * 
		 * @see #ľavýOkraj()
		 * @see #spodnýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double pravýOkraj()
		{
			int súradnica = (hlavnýPanel.getWidth() - 1) / 2;

			if (súradnica > ((Plátno.šírkaPlátna - 1) / 2))
				return (Plátno.šírkaPlátna - 1) / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pravýOkraj() pravýOkraj}.</p> */
		public static double pravyOkraj() { return pravýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu aktuálne viditeľnú y-ovú súradnicu
		 * plátien (podlahy alebo stropu).</p>
		 * 
		 * @return najväčšia viditeľná y-ová súradnica plátien
		 * 
		 * @see #ľavýOkraj()
		 * @see #pravýOkraj()
		 * @see #spodnýOkraj()
		 */
		public static double vrchnýOkraj()
		{
			int súradnica = hlavnýPanel.getHeight() / 2;

			if (súradnica > (Plátno.výškaPlátna / 2))
				return Plátno.výškaPlátna / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vrchnýOkraj() vrchnýOkraj}.</p> */
		public static double vrchnyOkraj() { return vrchnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #vrchnýOkraj() vrchnýOkraj}.</p> */
		public static double hornýOkraj() { return vrchnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #hornýOkraj() hornýOkraj}.</p> */
		public static double hornyOkraj() { return vrchnýOkraj(); }


		/**
		 * <p>Upraví rozmery plátna sveta, podlahy a stropu, čo je komplexný
		 * proces, preto by mal byť vykonaný len vo výnimočných prípadoch!
		 * Tento proces má za následok prebudovanie grafických objektov sveta,
		 * stropu a podlahy (čo má za následok vymazanie ich grafického
		 * obsahu), ich opätovné priradenie grafickým robotom, aktualizáciu
		 * umiestnenia komponentov sveta (tlačidiel – pozri triedu
		 * {@link Tlačidlo Tlačidlo}), zrušenie úprav textov (pozri metódu
		 * {@link GRobot#upravText(String) upravText}), odstránenie
		 * prípadných definovaných inštancií {@linkplain Vlnenie vlnenia}
		 * oboch plátien aj sveta (pretože sa menia ich rozmery; prípadné
		 * definované vlnenia obrázkov zostanú zachované), prepočet niektorých
		 * ďalších vnútorných parametrov a úpravu súradnicových systémov
		 * všetkých obrázkov programovacieho rámca.</p>
		 * 
		 * @param šírka nová šírka plátien
		 * @param výška nová výška plátien
		 * 
		 * @see GRobot#GRobot(int, int)
		 * @see GRobot#GRobot(int, int, String)
		 */
		public static void zmeňRozmeryPlátien(int šírka, int výška)
		{
			Component komponenty[] = hlavnýPanel.getComponents();

			for (Component komponent : komponenty)
			{
				if (komponent instanceof Tlačidlo)
				{
					Tlačidlo tlačidlo = (Tlačidlo)komponent;
					tlačidlo.x += (šírka - Plátno.šírkaPlátna) / 2;
					tlačidlo.y += (výška - Plátno.výškaPlátna) / 2;
				}
				else if (komponent instanceof GRobot.UpravText)
					((GRobot.UpravText)komponent).ukončiÚpravu(
						SPÔSOB_DEAKTIVÁCIA);
				else if (komponent instanceof RolovaciaLišta)
				{
					RolovaciaLišta rolovaciaLišta = (RolovaciaLišta)komponent;
					rolovaciaLišta.x += (šírka - Plátno.šírkaPlátna) / 2;
					rolovaciaLišta.y += (výška - Plátno.výškaPlátna) / 2;
				}
				else if (komponent instanceof
					PoznámkovýBlok.RolovaniePoznámkovéhoBloku)
				{
					PoznámkovýBlok poznámkovýBlok =
						((PoznámkovýBlok.RolovaniePoznámkovéhoBloku)komponent).
						poznámkovýBlok;
					poznámkovýBlok.x += (šírka - Plátno.šírkaPlátna) / 2;
					poznámkovýBlok.y += (výška - Plátno.výškaPlátna) / 2;
				}
			}

			Plátno.šírkaPlátna = šírka;
			Plátno.výškaPlátna = výška;
			prepočítajParametreVýplne();

			novéPlátna();
			for (Obrázok obrázok : Obrázok.zoznamObrázkovKnižnice)
				obrázok.upravPosun();

			// svet.doLayout();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p> */
		public static void zmenRozmeryPlatien(int šírka, int výška)
		{ zmeňRozmeryPlátien(šírka, výška); }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p> */
		public static void upravRozmeryPlátien(int šírka, int výška)
		{ zmeňRozmeryPlátien(šírka, výška); }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p> */
		public static void upravRozmeryPlatien(int šírka, int výška)
		{ zmeňRozmeryPlátien(šírka, výška); }


		// Viditeľnosť, upevnenie a zbalenie

		/**
		 * <p>Zistí, či je hlavné okno (svet) viditeľné. Ak okno sveta ešte
		 * nebolo inicializované (to platí v prípade, keď ešte nebola
		 * vytvorená ani jedna inštancia robota, to znamená, že nejestvuje
		 * hlavný robot), metóda informuje o tom, či bude okno zobrazené hneď
		 * po inicializácii. (Ak nie, znamená to, že sme volali metódu
		 * {@link #skry() skry}, prípadne niektorú z verzií metódy {@link 
		 * #zobrazÚvodnúObrazovku(Image) zobrazÚvodnúObrazovku}. V tom
		 * prípade musíme po inicializácii okno zobraziť metódou {@link 
		 * #zobraz() zobraz}, prípadne metódou {@link #skryÚvodnúObrazovku()
		 * skryÚvodnúObrazovku}.) Alternatívou tejto metódy je metóda {@link 
		 * #zobrazený() zobrazený}.</p>
		 * 
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public static boolean viditeľný()
		{
			if (null == svet) return zobrazPriŠtarte;
			if (null != oknoCelejObrazovky)
				return oknoCelejObrazovky.isVisible();
			return svet.isVisible();
		}

		/** <p><a class="alias"></a> Alias pre {@link #viditeľný() viditeľný}.</p> */
		public static boolean viditelny() { return viditeľný(); }

		/**
		 * <p>Zistí, či je hlavné okno (svet) viditeľné. Ak okno sveta ešte
		 * nebolo inicializované (to platí v prípade, keď ešte nebola
		 * vytvorená ani jedna inštancia robota, to znamená, že nejestvuje
		 * hlavný robot), metóda informuje o tom, či bude okno zobrazené hneď
		 * po inicializácii. (Ak nie, znamená to, že sme volali metódu
		 * {@link #skry() skry}, prípadne niektorú z verzií metódy {@link 
		 * #zobrazÚvodnúObrazovku(Image) zobrazÚvodnúObrazovku}. V tom
		 * prípade musíme po inicializácii okno zobraziť metódou {@link 
		 * #zobraz() zobraz}, prípadne metódou {@link #skryÚvodnúObrazovku()
		 * skryÚvodnúObrazovku}.) Alternatívou tejto metódy je metóda {@link 
		 * #viditeľný() viditeľný}.</p>
		 * 
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public static boolean zobrazený()
		{
			if (null == svet) return zobrazPriŠtarte;
			if (null != oknoCelejObrazovky)
				return oknoCelejObrazovky.isVisible();
			return svet.isVisible();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zobrazený() zobrazený}.</p> */
		public static boolean zobrazeny() { return zobrazený(); }

		/**
		 * <p>Skryje hlavné okno. Zároveň spôsobí, že okno sveta nebude
		 * automaticky zobrazené pri štarte. Aplikácia sa môže inicializovať
		 * a potom zobraziť hlavné okno metódou {@link #zobraz() zobraz}.</p>
		 */
		public static void skry()
		{
			zobrazPriŠtarte = false;
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setVisible(false);
			else if (null != svet)
				svet.setVisible(false);
		}

		/**
		 * <p>Zobrazí hlavné okno (svet). Metóda je protikladom metódy
		 * {@link #skry() skry}.</p>
		 */
		public static void zobraz()
		{
			zobrazPriŠtarte = true;
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setVisible(true);
			else if (null != svet)
				svet.setVisible(true);
		}

		/**
		 * <p>Upevní hlavné okno (svet) – okno bude mať pevnú veľkosť. Opakom
		 * je metóda {@link #uvoľni() uvoľni}. Veľkosť okna nemá vplyv na
		 * veľkosti plátien podlahy a stropu. Tie je v súčasnej verzii
		 * programovacieho rámca možné upraviť konštruktorom {@linkplain 
		 * #hlavnýRobot() hlavného robota} (čo je historicky starší spôsob –
		 * pozri aj konštruktor {@link GRobot#GRobot(int, int)}) alebo volaním
		 * metódy {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p>
		 * 
		 * @see #uvoľni()
		 */
		public static void upevni() { svet.setResizable(false); }

		/**
		 * <p>Uvoľní hlavné okno (svet) – okno nebude mať pevnú veľkosť
		 * (predvolené správanie sveta). Opakom je metóda {@link #upevni()
		 * upevni}. Veľkosť okna nemá vplyv na veľkosti plátien podlahy
		 * a stropu. Tie je v súčasnej verzii programovacieho rámca možné
		 * upraviť konštruktorom {@linkplain #hlavnýRobot() hlavného robota}
		 * (čo je historicky starší spôsob – pozri aj konštruktor
		 * {@link GRobot#GRobot(int, int)}) alebo volaním metódy
		 * {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni}
		 * má v programovacom rámci GRobot deväť rôznych metód:
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
		 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
		 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
		 * oblasť (čo je geometrická trieda),
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
		 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
		 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
		 * Svet#upevni() Svet.upevni}) a nakoniec
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
		 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
		 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
		 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
		 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
		 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
		 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
		 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
		 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
		 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
		 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
		 * disku zmenil).</p>
		 * 
		 * @see #upevni()
		 */
		public static void uvoľni() { svet.setResizable(true); }

		/** <p><a class="alias"></a> Alias pre {@link #uvoľni() uvoľni}.</p> */
		public static void uvolni() { svet.setResizable(true); }

		/**
		 * <p>Prispôsobí veľkosť okna tak, aby sa do neho pohodlne vošli všetky
		 * viditeľné komponenty.</p>
		 */
		public static void zbaľ() { svet.pack(); }

		/** <p><a class="alias"></a> Alias pre {@link #zbaľ() zbaľ}.</p> */
		public static void zbal() { svet.pack(); }

		/**
		 * <p>Presunie okno tak, aby sa nachádzalo v strede obrazovky.</p>
		 */
		public static void vystreď() { svet.setLocationRelativeTo(null); }

		/** <p><a class="alias"></a> Alias pre {@link #vystreď() vystreď}.</p> */
		public static void vystred() { svet.setLocationRelativeTo(null); }

		/**
		 * <p>Umiestni okno sveta na určené zobrazovacie zariadenie.
		 * Zariadenie je určené jeho poradovým číslom (indexom; čiže
		 * nula označuje prvé zariadenie). Okno je umiestnené tak, aby
		 * bola na určenom zariadení viditeľná celá jeho plocha, ak to
		 * rozmery okna dovoľujú. Ak je okno väčšie, než sú rozmery
		 * zariadenia, tak je umiestnené do ľavého horného rohu
		 * zariadenia. Ak zariadenie so zadaným indexom nejestvuje,
		 * tak nie je vykonaná žiadna operácia.</p>
		 * 
		 * @param zariadenie poradové číslo zariadenia, na ktoré má byť
		 *     okno umiestnené
		 * 
		 * @see #početZariadení()
		 * @see #premiestniNaZariadenie()
		 */
		public static void premiestniNaZariadenie(int zariadenie)
		{
			GraphicsDevice[] zariadenia = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getScreenDevices();

			if (zariadenie < zariadenia.length)
			{
				GraphicsConfiguration konfigurácia =
					zariadenia[zariadenie].getDefaultConfiguration();
				Rectangle2D hraniceZariadenia = konfigurácia.getBounds();
				Rectangle2D hraniceOkna = svet.getBounds();

				double polohaX =
					(hraniceOkna.getX() + hraniceOkna.getWidth()) >
						(hraniceZariadenia.getX() +
							hraniceZariadenia.getWidth()) ?
					(hraniceZariadenia.getX() + hraniceZariadenia.getWidth() -
						hraniceOkna.getWidth()) : hraniceOkna.getX();
				double polohaY =
					(hraniceOkna.getY() + hraniceOkna.getHeight()) >
						(hraniceZariadenia.getY() +
							hraniceZariadenia.getHeight()) ?
					(hraniceZariadenia.getY() + hraniceZariadenia.getHeight() -
						hraniceOkna.getHeight()) : hraniceOkna.getY();

				if (polohaX < hraniceZariadenia.getX())
					polohaX = hraniceZariadenia.getX();
				if (polohaY < hraniceZariadenia.getY())
					polohaY = hraniceZariadenia.getY();

				svet.setLocation((int)polohaX, (int)polohaY);
			}
		}

		/**
		 * <p>Umiestni okno sveta na predvolené zobrazovacie zariadenie.
		 * Okno je umiestnené tak, aby bola viditeľná celá jeho plocha,
		 * ak to jeho rozmery dovoľujú. Ak je okno väčšie, než sú rozmery
		 * zariadenia, tak je umiestnené do ľavého horného rohu.</p>
		 * 
		 * @see #početZariadení()
		 * @see #premiestniNaZariadenie(int)
		 */
		public static void premiestniNaZariadenie()
		{ premiestniNaZariadenie(0); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
		public static void premiestniNaObrazovku(int zariadenie)
		{ premiestniNaZariadenie(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
		public static void premiestniNaObrazovku()
		{ premiestniNaZariadenie(0); }


		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
		public static void presuňNaZariadenie(int zariadenie)
		{ premiestniNaZariadenie(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
		public static void presuňNaZariadenie()
		{ premiestniNaZariadenie(0); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
		public static void presunNaZariadenie(int zariadenie)
		{ premiestniNaZariadenie(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
		public static void presunNaZariadenie()
		{ premiestniNaZariadenie(0); }


		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
		public static void presuňNaObrazovku(int zariadenie)
		{ premiestniNaZariadenie(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
		public static void presuňNaObrazovku()
		{ premiestniNaZariadenie(0); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
		public static void presunNaObrazovku(int zariadenie)
		{ premiestniNaZariadenie(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
		public static void presunNaObrazovku()
		{ premiestniNaZariadenie(0); }


		/**
		 * <p>Zistí, či je okno sveta v maximalizovanom stave.</p>
		 * 
		 * @return ak je okno sveta v maximalizovanom stave, tak vráti hodnotu
		 *     {@code valtrue}, inak {@code valfalse}
		 * 
		 * @see ObsluhaUdalostí#maximalizovanie()
		 * @see #maximalizuj()
		 * @see #minimalizovaný()
		 * @see #minimalizuj()
		 * @see #normálny()
		 * @see #obnov()
		 */
		public static boolean maximalizovaný()
		{
			return 0 != (svet.getExtendedState() & MAXIMIZED_BOTH);
		}

		/** <p><a class="alias"></a> Alias pre {@link #maximalizovaný() maximalizovaný}.</p> */
		public static boolean maximalizovany()
		{
			return maximalizovaný();
		}


		/**
		 * <p>Pokúsi sa prepnúť okno sveta do maximalizovaného stavu. O tom,
		 * či sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
		 * #maximalizovaný() maximalizovaný}.</p>
		 * 
		 * @see ObsluhaUdalostí#maximalizovanie()
		 * @see #maximalizovaný()
		 * @see #minimalizovaný()
		 * @see #minimalizuj()
		 * @see #normálny()
		 * @see #obnov()
		 */
		public static void maximalizuj()
		{
			svet.setExtendedState(MAXIMIZED_BOTH);
		}


		/**
		 * <p>Zistí, či je okno sveta v minimalizovanom stave.</p>
		 * 
		 * @return ak je okno sveta v minimalizovanom stave, tak vráti hodnotu
		 *     {@code valtrue}, inak {@code valfalse}
		 * 
		 * @see ObsluhaUdalostí#minimalizovanie()
		 * @see #maximalizovaný()
		 * @see #maximalizuj()
		 * @see #minimalizuj()
		 * @see #normálny()
		 * @see #obnov()
		 */
		public static boolean minimalizovaný()
		{
			return 0 != (svet.getExtendedState() & ICONIFIED);
		}

		/** <p><a class="alias"></a> Alias pre {@link #minimalizovaný() minimalizovaný}.</p> */
		public static boolean minimalizovany()
		{
			return minimalizovaný();
		}


		/**
		 * <p>Pokúsi sa prepnúť okno sveta do minimalizovaného stavu. O tom,
		 * či sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
		 * #minimalizovaný() minimalizovaný}.</p>
		 * 
		 * @see ObsluhaUdalostí#minimalizovanie()
		 * @see #maximalizovaný()
		 * @see #maximalizuj()
		 * @see #minimalizovaný()
		 * @see #normálny()
		 * @see #obnov()
		 */
		public static void minimalizuj()
		{
			svet.setExtendedState(ICONIFIED);
		}


		/**
		 * <p>Zistí, či je okno sveta v normálnom stave.</p>
		 * 
		 * @return ak je okno sveta v normálnom stave, tak vráti hodnotu
		 *     {@code valtrue}, inak {@code valfalse}
		 * 
		 * @see ObsluhaUdalostí#obnovenie()
		 * @see #maximalizovaný()
		 * @see #maximalizuj()
		 * @see #minimalizovaný()
		 * @see #minimalizuj()
		 * @see #obnov()
		 */
		public static boolean normálny()
		{
			return NORMAL == svet.getExtendedState();
		}

		/** <p><a class="alias"></a> Alias pre {@link #normálny() normálny}.</p> */
		public static boolean normalny()
		{
			return normálny();
		}

		/**
		 * <p>Pokúsi sa prepnúť okno sveta do normálneho stavu. (V zmysle
		 * „nie maximalizovaného a nie minimalizovaného“ stavu.) O tom, či
		 * sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
		 * #normálny() normálny}.</p>
		 * 
		 * @see ObsluhaUdalostí#obnovenie()
		 * @see #maximalizovaný()
		 * @see #maximalizuj()
		 * @see #minimalizovaný()
		 * @see #minimalizuj()
		 * @see #normálny()
		 * @see #obnov()
		 */
		public static void obnov()
		{
			svet.setExtendedState(NORMAL);
		}

		/**
		 * <p>Poskytne komponent hlavného panela vloženého v hlavnom okne
		 * aplikácie (vo svete).</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Neodborná manipulácia
		 * s týmto komponentom môže mať nežiaduce vedľajšie účinky.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda bola do
		 * programovacieho rámca niekoľkokrát pridaná na testovacie účely,
		 * pričom hneď po skončení testov bola odobraná. Autor rámca nemal
		 * v úmysle trvalé ponechanie možnosti získania hlavného panela
		 * na ďalšiu manipuláciu, ale dňa 12. 6. 2019 ho okolnosti presvedčili
		 * o tom, aby túto možnosť predsa ponechal dostupnú natrvalo.
		 * Riaďte sa však upozornením vyššie.</p>
		 * 
		 * @return hlavný panel aplikačného okna
		 */
		public static JPanel hlavnýPanel() { return hlavnýPanel; }

		/** <p><a class="alias"></a> Alias pre {@link #hlavnýPanel() hlavnýPanel}.</p> */
		public static JPanel hlavnyPanel() { return hlavnýPanel; }


		// Vzdialenosti

		/**
		 * <p>Zistí vzdialenosť bodu so zadanými súradnicami od stredu
		 * súradnicovej sústavy.</p>
		 * 
		 * @param súradnicaBoduX x-ová súradnica bodu
		 * @param súradnicaBoduY y-ová súradnica bodu
		 * @return vzdialenosť medzi stredom súradnicovej sústavy
		 *     a zadaným bodom
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Poloha)
		 * @see Svet#vzdialenosť(Shape)
		 */
		public static double vzdialenosť(double súradnicaBoduX, double súradnicaBoduY)
		{
			// return Math.sqrt(Math.pow(súradnicaBoduX, 2) +
			//	Math.pow(súradnicaBoduY, 2));
			/*!!!
			return Math.sqrt(súradnicaBoduX * súradnicaBoduX +
				súradnicaBoduY * súradnicaBoduY);
			*/
			return Math.hypot(súradnicaBoduX, súradnicaBoduY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(double, double) vzdialenosť}.</p> */
		public static double vzdialenost(double súradnicaBoduX, double súradnicaBoduY)
		{ return vzdialenosť(súradnicaBoduX, súradnicaBoduY); }

		/**
		 * <p>Zistí, aká je vzdialenosť zadaného objektu od stredu súradnicovej
		 * sústavy.</p>
		 * 
		 * @param objekt objekt, ktorého vzdialenosť zisťujeme
		 * @return vzdialenosť objektu od stredu súradnicovej sústavy
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double)
		 * @see Svet#vzdialenosť(Shape)
		 */
		public static double vzdialenosť(Poloha objekt)
		{
			/*!!!
			return Math.sqrt(Math.pow(iný.aktuálneX, 2) +
				Math.pow(iný.aktuálneY, 2));
			*/
			return Math.hypot(objekt.polohaX(), objekt.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Poloha) vzdialenosť}.</p> */
		public static double vzdialenost(Poloha objekt) { return vzdialenosť(objekt); }

		/**
		 * <p>Zistí vzdialenosť stredu hraníc<sup>[1]</sup> zadaného tvaru od
		 * stredu súradnicovej sústavy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar tvar Javy ({@link Shape Shape}), stred hraníc
		 *     ktorého vzdialenosť od stredu zisťujeme
		 * @return vzdialenosť stredom súradnicovej sústavy a stredom
		 *     zadaného tvaru
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double)
		 * @see Svet#vzdialenosť(Poloha)
		 */
		public static double vzdialenosť(Shape tvar)
		{
			Rectangle2D hranice = tvar.getBounds2D();

			return Math.hypot(
				(prepočítajSpäťX(hranice.getX()) +
					hranice.getWidth() / 2),
				(prepočítajSpäťY(hranice.getY()) -
					hranice.getHeight() / 2));
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Shape) vzdialenosť}.</p> */
		public static double vzdialenost(Shape tvar) { return vzdialenosť(tvar); }


		/**
		 * <p>Zistí vzájomnú vzdialenosť medzi bodmi so zadanými súradnicami.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica prvého bodu
		 * @param súradnicaBoduY1 y-ová súradnica prvého bodu
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return vzájomná vzdialenosť medzi zadanými bodmi
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Poloha, Poloha)
		 * @see Svet#vzdialenosť(Shape, Shape)
		 */
		public static double vzdialenosť(
			double súradnicaBoduX1, double súradnicaBoduY1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			return Math.hypot(súradnicaBoduX2 - súradnicaBoduX1,
				súradnicaBoduY2 - súradnicaBoduY1);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(double, double, double, double) vzdialenosť}.</p> */
		public static double vzdialenost(
			double súradnicaBoduX1, double súradnicaBoduY1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{ return vzdialenosť(súradnicaBoduX1, súradnicaBoduY1,
			súradnicaBoduX2, súradnicaBoduY2); }

		/**
		 * <p>Zistí vzájomnú vzdialenosť zadaných objektov.</p>
		 * 
		 * @param objekt1 prvý objekt, ktorého súradnice sa berú do úvahy
		 * @param objekt2 druhý objekt, ktorého súradnice sa berú do úvahy
		 * @return vzájomná vzdialenosť medzi zadanými objektmi
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double, double, double)
		 * @see Svet#vzdialenosť(Shape, Shape)
		 */
		public static double vzdialenosť(Poloha objekt1, Poloha objekt2)
		{
			return Math.hypot(objekt2.polohaX() - objekt1.polohaX(),
				objekt2.polohaY() - objekt1.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Poloha, Poloha) vzdialenosť}.</p> */
		public static double vzdialenost(Poloha objekt1, Poloha objekt2) { return vzdialenosť(objekt1, objekt2); }

		/**
		 * <p>Zistí vzájomnú vzdialenosť stredov hraníc<sup>[1]</sup> zadaných
		 * tvarov.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvarov; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 prvý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param tvar2 druhý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return vzájomná vzdialenosť stredov hraníc zadaných tvarov
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double, double, double)
		 * @see Svet#vzdialenosť(Poloha, Poloha)
		 */
		public static double vzdialenosť(Shape tvar1, Shape tvar2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();
			Rectangle2D hranice2 = tvar2.getBounds2D();

			return Math.hypot(
				(prepočítajSpäťX(hranice2.getX()) +
					hranice2.getWidth() / 2) -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2),
				(prepočítajSpäťY(hranice2.getY()) -
					hranice2.getHeight() / 2) -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2));
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Shape, Shape) vzdialenosť}.</p> */
		public static double vzdialenost(Shape tvar1, Shape tvar2) { return vzdialenosť(tvar1, tvar2); }


		/**
		 * <p>Zistí vzájomnú vzdialenosť zadaného objektu a bodu so zadanými
		 * súradnicami.</p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica bodu
		 * @param súradnicaBoduY2 y-ová súradnica bodu
		 * @return vzájomná vzdialenosť medzi zadaným objektom a bodom
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Shape, double, double)
		 */
		public static double vzdialenosť(Poloha objekt1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			return Math.hypot(súradnicaBoduX2 - objekt1.polohaX(),
				súradnicaBoduY2 - objekt1.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Poloha, double, double) vzdialenosť}.</p> */
		public static double vzdialenost(Poloha objekt1, double súradnicaBoduX2, double súradnicaBoduY2) { return vzdialenosť(objekt1, súradnicaBoduX2, súradnicaBoduY2); }

		/**
		 * <p>Zistí vzájomnú vzdialenosť stredu hraníc<sup>[1]</sup> zadaného
		 * tvaru a bodu so zadanými súradnicami.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica bodu
		 * @param súradnicaBoduY2 y-ová súradnica bodu
		 * @return vzájomná vzdialenosť medzi stredom hraníc zadaného tvaru
		 *     a bodom
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Poloha, double, double)
		 */
		public static double vzdialenosť(Shape tvar1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			return Math.hypot(
				súradnicaBoduX2 -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2),
				súradnicaBoduY2 -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2));
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Shape, double, double) vzdialenosť}.</p> */
		public static double vzdialenost(Shape tvar1, double súradnicaBoduX2, double súradnicaBoduY2) { return vzdialenosť(tvar1, súradnicaBoduX2, súradnicaBoduY2); }


		/**
		 * <p>Zistí vzájomnú vzdialenosť medzi bodom so zadanými súradnicami
		 * a zadaným objektom.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica bodu
		 * @param súradnicaBoduY1 y-ová súradnica bodu
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return vzájomná vzdialenosť medzi zadaným bodom a objektom
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Shape, Poloha)
		 */
		public static double vzdialenosť(
			double súradnicaBoduX1, double súradnicaBoduY1,
			Poloha objekt2)
		{
			return Math.hypot(objekt2.polohaX() - súradnicaBoduX1,
				objekt2.polohaY() - súradnicaBoduY1);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(double, double, Poloha) vzdialenosť}.</p> */
		public static double vzdialenost(
			double súradnicaBoduX1, double súradnicaBoduY1, Poloha objekt2)
		{ return vzdialenosť(súradnicaBoduX1, súradnicaBoduY1, objekt2); }

		/**
		 * <p>Zistí vzájomnú vzdialenosť stredu hraníc<sup>[1]</sup> zadaného
		 * tvaru a polohy zadaného objektu.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return vzájomná vzdialenosť stredu hraníc zadaného tvaru
		 *     a polohy zadaného objektu
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double, Poloha)
		 */
		public static double vzdialenosť(Shape tvar1, Poloha objekt2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			return Math.hypot(
				objekt2.polohaX() -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2),
				objekt2.polohaY() -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2));
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Shape, Poloha) vzdialenosť}.</p> */
		public static double vzdialenost(Shape tvar1, Poloha objekt2) { return vzdialenosť(tvar1, objekt2); }


		/**
		 * <p>Zistí vzájomnú vzdialenosť medzi bodom so zadanými súradnicami
		 * a stredom hraníc<sup>[1]</sup> zadaného tvaru.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica bodu
		 * @param súradnicaBoduY1 y-ová súradnica bodu
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return vzájomná vzdialenosť medzi zadaným bodom a stredom hraníc
		 *     zadaného tvaru
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(Poloha, Shape)
		 */
		public static double vzdialenosť(
			double súradnicaBoduX1, double súradnicaBoduY1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			return Math.hypot(
				(prepočítajSpäťX(hranice2.getX()) +
					hranice2.getWidth() / 2) - súradnicaBoduX1,
				(prepočítajSpäťY(hranice2.getY()) -
					hranice2.getHeight() / 2) - súradnicaBoduY1);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(double, double, Shape) vzdialenosť}.</p> */
		public static double vzdialenost(
			double súradnicaBoduX1, double súradnicaBoduY1, Shape tvar2)
		{ return vzdialenosť(súradnicaBoduX1, súradnicaBoduY1, tvar2); }

		/**
		 * <p>Zistí vzájomnú vzdialenosť medzi zadaným objektom a stredom
		 * hraníc<sup>[1]</sup> zadaného tvaru.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return vzájomná vzdialenosť medzi zadaným objektom a stredom
		 *     hraníc zadaného tvaru
		 * 
		 * @see GRobot#vzdialenosť()
		 * @see Svet#vzdialenosť(double, double, Shape)
		 */
		public static double vzdialenosť(Poloha objekt1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			return Math.hypot(
				(prepočítajSpäťX(hranice2.getX()) +
					hranice2.getWidth() / 2) - objekt1.polohaX(),
				(prepočítajSpäťY(hranice2.getY()) -
					hranice2.getHeight() / 2) - objekt1.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosť(Poloha, Shape) vzdialenosť}.</p> */
		public static double vzdialenost(Poloha objekt1, Shape tvar2) { return vzdialenosť(objekt1, tvar2); }


		// Uhly – smery

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * stredom súradncovej sústavy a zadaným bodom.</p>
		 * 
		 * @param súradnicaBoduX x-ová súradnica určujúceho bodu
		 * @param súradnicaBoduY y-ová súradnica určujúceho bodu
		 * @return uhol (smer) medzi osou x a priamkou určenou stredom
		 *     súradnicovej sústavy a zadaným bodom
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#smer(Poloha)
		 * @see Svet#smer(Shape)
		 */
		public static double smer(
			double súradnicaBoduX, double súradnicaBoduY)
		{
			if (súradnicaBoduX == 0 && súradnicaBoduY == 0) return 360;

			double α = Math.toDegrees(
				Math.atan2(súradnicaBoduY, súradnicaBoduX));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * stredom súradncovej sústavy a bodom určeným polohou zadaného
		 * objektu.</p>
		 * 
		 * @param objekt objekt, ktorého poloha učuje súradnice určujúceho
		 *     bodu priamky
		 * @return uhol (smer) medzi osou x a priamkou určenou stredom
		 *     súradnicovej sústavy a bodom určeným polohou zadaného objektu
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#smer(double, double)
		 * @see Svet#smer(Shape)
		 */
		public static double smer(Poloha objekt)
		{
			double Δx = objekt.polohaX();
			double Δy = objekt.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * stredom súradncovej sústavy a bodom určeným polohou stredu
		 * hraníc<sup>[1]</sup> zadaného tvaru.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param objekt tvar Javy ({@link Shape Shape}), ktorého stred
		 *     hraníc učuje súradnice určujúceho bodu priamky
		 * @return uhol (smer) medzi osou x a priamkou určenou stredom
		 *     súradnicovej sústavy a bodom určeným stredom hraníc zadaného
		 *     tvaru Javy
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#smer(double, double)
		 * @see Svet#smer(Poloha)
		 */
		public static double smer(Shape tvar)
		{
			Rectangle2D hranice = tvar.getBounds2D();

			double Δx = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			double Δy = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * zadanými bodmi.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica prvého bodu
		 * @param súradnicaBoduY1 y-ová súradnica prvého bodu
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol (smer) medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#smer(Poloha, Poloha)
		 * @see Svet#smer(Shape, Shape)
		 */
		public static double smer(
			double súradnicaBoduX1, double súradnicaBoduY1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			double Δx = súradnicaBoduX2 - súradnicaBoduX1;
			double Δy = súradnicaBoduY2 - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * bodmi zadanými vo forme polôh objektov.</p>
		 * 
		 * @param objekt1 prvý objekt, ktorého súradnice sa berú do úvahy
		 * @param objekt2 druhý objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol (smer) medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#smer(double, double, double, double)
		 * @see Svet#smer(Shape, Shape)
		 */
		public static double smer(Poloha objekt1, Poloha objekt2)
		{
			double Δx = objekt2.polohaX() - objekt1.polohaX();
			double Δy = objekt2.polohaY() - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou
		 * bodmi vypočítanými zo stredov hraníc<sup>[1]</sup> zadaných tvarov
		 * Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvarov; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 prvý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param tvar2 druhý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol (smer) medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#smer(double, double, double, double)
		 * @see Svet#smer(Poloha, Poloha)
		 */
		public static double smer(Shape tvar1, Shape tvar2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
					hranice2.getWidth() / 2) -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2);
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
					hranice2.getHeight() / 2) -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými polohou zadaného objektu a zadanými súradnicami.</p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#smer(Shape, double, double)
		 */
		public static double smer(Poloha objekt1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			double Δx = súradnicaBoduX2 - objekt1.polohaX();
			double Δy = súradnicaBoduY2 - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými stredom hraníc<sup>[1]</sup> zadaného tvaru Javy
		 * a zadanými súradnicami.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#smer(Poloha, double, double)
		 */
		public static double smer(Shape tvar1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			double Δx = súradnicaBoduX2 - (prepočítajSpäťX(hranice1.getX()) +
				hranice1.getWidth() / 2);
			double Δy = súradnicaBoduY2 - (prepočítajSpäťY(hranice1.getY()) -
				hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými zadanými súradnicami a polohou zadaného objektu.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica prvého bodu
		 * @param súradnicaBoduY1 y-ová súradnica prvého bodu
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#smer(Shape, Poloha)
		 */
		public static double smer(
			double súradnicaBoduX1, double súradnicaBoduY1,
			Poloha objekt2)
		{
			double Δx = objekt2.polohaX() - súradnicaBoduX1;
			double Δy = objekt2.polohaY() - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými stredom hraníc<sup>[1]</sup> zadaného tvaru Javy
		 * a polohou zadaného objektu.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#smer(double, double, Poloha)
		 */
		public static double smer(Shape tvar1, Poloha objekt2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			double Δx = objekt2.polohaX() -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2);
			double Δy = objekt2.polohaY() -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými zadanými súradnicami a stredom hraníc<sup>[1]</sup>
		 * zadaného tvaru Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica bodu
		 * @param súradnicaBoduY1 y-ová súradnica bodu
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#smer(Poloha, Shape)
		 */
		public static double smer(
			double súradnicaBoduX1, double súradnicaBoduY1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
				hranice2.getWidth() / 2) - súradnicaBoduX1;
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
				hranice2.getHeight() / 2) - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol (smer) zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými polohou zadaného objektu a stredom hraníc<sup>[1]</sup>
		 * zadaného tvaru Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol (smer) medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#smer(double, double, Shape)
		 */
		public static double smer(Poloha objekt1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
				hranice2.getWidth() / 2) - objekt1.polohaX();
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
				hranice2.getHeight() / 2) - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}


		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou stredom
		 * súradncovej sústavy a zadaným bodom.</p>
		 * 
		 * @param súradnicaBoduX x-ová súradnica určujúceho bodu
		 * @param súradnicaBoduY y-ová súradnica určujúceho bodu
		 * @return uhol medzi osou x a priamkou určenou stredom súradnicovej
		 *     sústavy a zadaným bodom
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#uhol(Poloha)
		 * @see Svet#uhol(Shape)
		 */
		public static double uhol(
			double súradnicaBoduX, double súradnicaBoduY)
		{
			if (súradnicaBoduX == 0 && súradnicaBoduY == 0) return 360;

			double α = Math.toDegrees(
				Math.atan2(súradnicaBoduY, súradnicaBoduX));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou stredom
		 * súradncovej sústavy a bodom určeným polohou zadaného objektu.</p>
		 * 
		 * @param objekt objekt, ktorého poloha učuje súradnice určujúceho
		 *     bodu priamky
		 * @return uhol medzi osou x a priamkou určenou stredom súradnicovej
		 *     sústavy a bodom určeným polohou zadaného objektu
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#uhol(double, double)
		 * @see Svet#uhol(Shape)
		 */
		public static double uhol(Poloha objekt)
		{
			double Δx = objekt.polohaX();
			double Δy = objekt.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou stredom
		 * súradncovej sústavy a bodom určeným polohou stredu
		 * hraníc<sup>[1]</sup> zadaného tvaru.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param objekt tvar Javy ({@link Shape Shape}), ktorého stred
		 *     hraníc učuje súradnice určujúceho bodu priamky
		 * @return uhol medzi osou x a priamkou určenou stredom súradnicovej
		 *     sústavy a bodom určeným stredom hraníc zadaného tvaru Javy
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#uhol(double, double)
		 * @see Svet#uhol(Poloha)
		 */
		public static double uhol(Shape tvar)
		{
			Rectangle2D hranice = tvar.getBounds2D();

			double Δx = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			double Δy = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou zadanými
		 * bodmi.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica prvého bodu
		 * @param súradnicaBoduY1 y-ová súradnica prvého bodu
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#uhol(Poloha, Poloha)
		 * @see Svet#uhol(Shape, Shape)
		 */
		public static double uhol(
			double súradnicaBoduX1, double súradnicaBoduY1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			double Δx = súradnicaBoduX2 - súradnicaBoduX1;
			double Δy = súradnicaBoduY2 - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * zadanými vo forme polôh objektov.</p>
		 * 
		 * @param objekt1 prvý objekt, ktorého súradnice sa berú do úvahy
		 * @param objekt2 druhý objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#uhol(double, double, double, double)
		 * @see Svet#uhol(Shape, Shape)
		 */
		public static double uhol(Poloha objekt1, Poloha objekt2)
		{
			double Δx = objekt2.polohaX() - objekt1.polohaX();
			double Δy = objekt2.polohaY() - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * vypočítanými zo stredov hraníc<sup>[1]</sup> zadaných tvarov
		 * Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvarov; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 prvý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param tvar2 druhý tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol medzi osou x a priamkou vedúcou zadanými bodmi
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#uhol(double, double, double, double)
		 * @see Svet#uhol(Poloha, Poloha)
		 */
		public static double uhol(Shape tvar1, Shape tvar2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
					hranice2.getWidth() / 2) -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2);
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
					hranice2.getHeight() / 2) -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými polohou zadaného objektu a zadanými súradnicami.</p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#uhol(Shape, double, double)
		 */
		public static double uhol(Poloha objekt1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			double Δx = súradnicaBoduX2 - objekt1.polohaX();
			double Δy = súradnicaBoduY2 - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými stredom hraníc<sup>[1]</sup> zadaného tvaru Javy
		 * a zadanými súradnicami.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param súradnicaBoduX2 x-ová súradnica druhého bodu
		 * @param súradnicaBoduY2 y-ová súradnica druhého bodu
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(double, double)
		 * @see Svet#uhol(Poloha, double, double)
		 */
		public static double uhol(Shape tvar1,
			double súradnicaBoduX2, double súradnicaBoduY2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			double Δx = súradnicaBoduX2 - (prepočítajSpäťX(hranice1.getX()) +
				hranice1.getWidth() / 2);
			double Δy = súradnicaBoduY2 - (prepočítajSpäťY(hranice1.getY()) -
				hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými zadanými súradnicami a polohou zadaného objektu.</p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica prvého bodu
		 * @param súradnicaBoduY1 y-ová súradnica prvého bodu
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#uhol(Shape, Poloha)
		 */
		public static double uhol(
			double súradnicaBoduX1, double súradnicaBoduY1,
			Poloha objekt2)
		{
			double Δx = objekt2.polohaX() - súradnicaBoduX1;
			double Δy = objekt2.polohaY() - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými stredom hraníc<sup>[1]</sup> zadaného tvaru Javy
		 * a polohou zadaného objektu.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param tvar1 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @param objekt2 objekt, ktorého súradnice sa berú do úvahy
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Poloha)
		 * @see Svet#uhol(double, double, Poloha)
		 */
		public static double uhol(Shape tvar1, Poloha objekt2)
		{
			Rectangle2D hranice1 = tvar1.getBounds2D();

			double Δx = objekt2.polohaX() -
				(prepočítajSpäťX(hranice1.getX()) +
					hranice1.getWidth() / 2);
			double Δy = objekt2.polohaY() -
				(prepočítajSpäťY(hranice1.getY()) -
					hranice1.getHeight() / 2);

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými zadanými súradnicami a stredom hraníc<sup>[1]</sup>
		 * zadaného tvaru Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param súradnicaBoduX1 x-ová súradnica bodu
		 * @param súradnicaBoduY1 y-ová súradnica bodu
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#uhol(Poloha, Shape)
		 */
		public static double uhol(
			double súradnicaBoduX1, double súradnicaBoduY1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
				hranice2.getWidth() / 2) - súradnicaBoduX1;
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
				hranice2.getHeight() / 2) - súradnicaBoduY1;

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}

		/**
		 * <p>Zistí uhol zvieraný medzi osou x a priamkou vedúcou bodmi
		 * určenými polohou zadaného objektu a stredom hraníc<sup>[1]</sup>
		 * zadaného tvaru Javy.</p>
		 * 
		 * <p><small>[1] – nejde presne o stred útvaru; je použitý
		 * najrýchlejší a najjednoduchší spôsob zistenia približného
		 * stredu: vezme sa obdĺžnik tesne ohraničujúci útvar a určí sa
		 * jeho stred – čiže „stred hraníc.“</small></p>
		 * 
		 * @param objekt1 objekt, ktorého súradnice sa berú do úvahy
		 * @param tvar2 tvar Javy ({@link Shape Shape}), ktorého
		 *     stred hraníc sa berie do úvahy
		 * @return uhol medzi osou x a určenou priamkou
		 * 
		 * @see GRobot#smerNa(Shape)
		 * @see Svet#uhol(double, double, Shape)
		 */
		public static double uhol(Poloha objekt1, Shape tvar2)
		{
			Rectangle2D hranice2 = tvar2.getBounds2D();

			double Δx = (prepočítajSpäťX(hranice2.getX()) +
				hranice2.getWidth() / 2) - objekt1.polohaX();
			double Δy = (prepočítajSpäťY(hranice2.getY()) -
				hranice2.getHeight() / 2) - objekt1.polohaY();

			if (Δx == 0 && Δy == 0) return 360;

			double α = Math.toDegrees(Math.atan2(Δy, Δx));
			if (α < 0) return 360 + α;
			return α;
		}


		// Vymazanie

		/**
		 * <p>Vymaže obsah sveta. Má rovnaký efekt, ako keby sme za sebou
		 * spustili metódy {@link Plátno#vymaž() podlaha.vymaž()}
		 * a {@link Plátno#vymaž() strop.vymaž()}.</p>
		 */
		public static void vymaž()
		{ GRobot.podlaha.vymaž(); GRobot.strop.vymaž(); }

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public static void vymaz()
		{ GRobot.podlaha.vymaž(); GRobot.strop.vymaž(); }

		/**
		 * <p>Vymaže texty podlahy a stropu. Má rovnaký efekt, ako keby sme za
		 * sebou spustili metódy {@link Plátno#vymažTexty()
		 * podlaha.vymažTexty()} a {@link Plátno#vymažTexty()
		 * strop.vymažTexty()}.</p>
		 */
		public static void vymažTexty()
		{ GRobot.podlaha.vymažTexty(); GRobot.strop.vymažTexty(); }

		/** <p><a class="alias"></a> Alias pre {@link #vymažTexty() vymažTexty}.</p> */
		public static void vymazTexty()
		{ GRobot.podlaha.vymažTexty(); GRobot.strop.vymažTexty(); }

		/**
		 * <p>Vymaže plátno podlahy a stropu. Má rovnaký efekt, ako keby sme za
		 * sebou spustili metódy {@link Plátno#vymažGrafiku()
		 * podlaha.vymažGrafiku()} a {@link Plátno#vymažGrafiku()
		 * strop.vymažGrafiku()}.</p>
		 */
		public static void vymažGrafiku()
		{ GRobot.podlaha.vymažGrafiku(); GRobot.strop.vymažGrafiku(); }

		/** <p><a class="alias"></a> Alias pre {@link #vymažGrafiku() vymažGrafiku}.</p> */
		public static void vymazGrafiku()
		{ GRobot.podlaha.vymažGrafiku(); GRobot.strop.vymažGrafiku(); }


		// Viacnásobná obsluha udalostí

		/**
		 * <p>Po spustení tejto metódy bude možné vytváranie viacerých verzií
		 * {@linkplain ObsluhaUdalostí obsluhy udalostí}, ktoré môžu byť
		 * neskôr podľa potreby uvedené do činnosti metódou {@link 
		 * #presmerujObsluhuUdalostí(ObsluhaUdalostí)
		 * presmerujObsluhuUdalostí}. <b>Vždy môže byť v činnosti len jedna
		 * obsluha udalostí.</b> Vytváranie viacerých inštancií triedy {@link 
		 * ObsluhaUdalostí} (resp. inštancií odvodených tried) je predvolene
		 * zakázané, aby nedochádzalo k omylom. Po povolení tvorby viacerých
		 * inštancií obsluhy udalostí zostáva aktívna prvá vytvorená
		 * inštancia dokedy tok udalostí nepresmerujeme do inej metódou
		 * {@link #presmerujObsluhuUdalostí(ObsluhaUdalostí)
		 * presmerujObsluhuUdalostí}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Pri povoľovaní viacnásobnej
		 * obsluhy musíme dobre rozumieť mechanizmu spracovania udalostí. Nemali
		 * by sme ju povoliť bezdôvodne. Príkadom takého dôvodu môže byť potreba
		 * využitia odlišnej skupiny udalostí v rôznych situáciách hry, ktorú
		 * programujeme napríklad keď hráč vstúpi do hlavnej ponuky hry,
		 * má obsluha udalostí fungovať ináč, než keď hráč aktívne hrá hru…</p>
		 * 
		 * <p>Príklad, ktorý používa zapnutie viacnásobného spracovania
		 * udalostí je pri opise metódy {@link 
		 * #presmerujObsluhuUdalostí(ObsluhaUdalostí)
		 * presmerujObsluhuUdalostí}.</p>
		 * 
		 * @see ObsluhaUdalostí
		 * @see #presmerujObsluhuUdalostí(ObsluhaUdalostí)
		 */
		public static void povoľViacnásobnúObsluhuUdalostí()
		{ ObsluhaUdalostí.povoľViacnásobnúObsluhuUdalostí = true; }

		/** <p><a class="alias"></a> Alias pre {@link #povoľViacnásobnúObsluhuUdalostí() povoľViacnásobnúObsluhuUdalostí}.</p> */
		public static void povolViacnasobnuObsluhuUdalosti()
		{ ObsluhaUdalostí.povoľViacnásobnúObsluhuUdalostí = true; }

		/**
		 * <p>Presmeruje spracovanie udalostí do inej inštancie triedy {@link 
		 * ObsluhaUdalostí ObsluhaUdalostí}. Použitie tejto metódy má význam
		 * len po {@linkplain #povoľViacnásobnúObsluhuUdalostí() povolení
		 * vytvárania viacerých inštancií obsluhy udalostí} (čo je predvolene
		 * zakázané). Po použití metódy {@code currpresmerujObsluhuUdalostí}
		 * sú ďalšie vznikajúce udalosti presmerované do zadanej inštancie
		 * {@code obsluha}. Táto akcia automaticky vyradí z činnosti
		 * predchádzajúcu inštanciu obsluhy udalostí, z čoho vyplýva, že
		 * <em>v činnosti môže byť vždy len jedna obsluha udalostí.</em></p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>V tomto príklade sú definované dve obsluhy udalostí. Obidve
		 * definujú spracovanie {@linkplain ObsluhaUdalostí udalosti kliku
		 * myšou}. Príklad je demonštratívny. Po prvom kliku je (popri
		 * zobrazení robota, vymazaní a zmeny pozadia sveta) presmerovaný tok
		 * udalostí do druhej verzie obsluhy udalostí, ktorá má na starosti
		 * jediné: upravovať cieľ cesty {@linkplain #hlavnýRobot()
		 * hlavného robota}…</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} ViacnásobnáObsluhaUdalostí {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} {@link ObsluhaUdalostí ObsluhaUdalostí} obsluha1, obsluha2;

				{@code kwdprivate} ViacnásobnáObsluhaUdalostí()
				{
					{@code comm// Napísanie bieleho textu}
					{@link GRobot#farba(Color) farba}({@link Farebnosť#biela biela});
					{@link GRobot#text(String) text}({@code srg"…kliknite myšou…"});

					{@code comm// Nastavenie vlastností hlavného robota}
					{@link GRobot#farba(int, int, int, int) farba}({@code num200}, {@code num0}, {@code num0}, {@code num100});
					{@link GRobot#skry() skry}();
					{@link GRobot#veľkosť(double) veľkosť}({@code num30});
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num3});
					{@link GRobot#predvolenýTvar(boolean) predvolenýTvar}({@code valfalse});
					{@link GRobot#zrýchlenie(double, boolean) zrýchlenie}({@code num0.5}, {@code valfalse});
					{@link GRobot#maximálnaRýchlosť(double) maximálnaRýchlosť}({@code num20});
					{@link GRobot#rýchlosťOtáčania(double, boolean) rýchlosťOtáčania}({@code num5}, {@code valfalse});

					{@code comm// Prvá verzia obsluhy udalostí}
					obsluha1 = {@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
					{
						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}()
						{
							{@link GRobot#zobraz() zobraz}();
							{@link Svet Svet}.{@link #vymaž() vymaž}();
							{@link Svet Svet}.{@link #farbaPozadia(Color) farbaPozadia}({@link Farebnosť#svetlotyrkysová svetlotyrkysová}.{@link Farba#svetlejšia() svetlejšia}());
							{@link Svet Svet}.{@code currpresmerujObsluhuUdalostí}(obsluha2);
						}
					};

					{@code comm// Najneskôr tu musíme povoliť viacnásobnú obsluhu udalostí…}
					{@code comm// …inak nasledujúci príkaz „new ObsluhaUdalostí“ zlyhá.}
					{@link Svet Svet}.{@link #povoľViacnásobnúObsluhuUdalostí() povoľViacnásobnúObsluhuUdalostí}();

					{@code comm// Druhá verzia obsluhy udalostí}
					obsluha2 = {@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
					{
						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}()
						{
							{@link GRobot#cieľNaMyš() cieľNaMyš}();
						}
					};

					{@code comm// Nastavenie vlastností sveta}
					{@link Svet Svet}.{@link #farbaPozadia(Color) farbaPozadia}({@link Farebnosť#čierna čierna});
					{@link Svet Svet}.{@link #upevni() upevni}();
					{@link Svet Svet}.{@link #zbaľ() zbaľ}();
				}


				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code kwdnew} ViacnásobnáObsluhaUdalostí();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered">
		 * <tr><td
		 * style="vertical-align:top"><p><image>viacnasobnaObsluhaA.png<alt/></image>Fáza
		 * 1 – pred prvým kliknutím je robot skrytý<br /><small>(plátno
		 * na obrázku je zmenšené)</small>.</p></td><td> </td>
		 * <td
		 * style="vertical-align:top"><p><image>viacnasobnaObsluhaB.png<alt/></image>Fáza
		 * 2 – po prvom kliknutí sa robot zobrazí<br />a potom smeruje
		 * do cieľa určeného ďalším<br />klikom; každý ďalší klik určí nový
		 * cieľ<br /><small>(plátno na obrázku je zmenšené)</small>.</p></td></tr>
		 * </table>
		 * 
		 * @param obsluha inštancia {@linkplain ObsluhaUdalostí obsluhy
		 *     udalostí}, do ktorej budú presmerované ďalšie udalosti
		 *     spracúvané svetom grafického robota
		 * 
		 * @see ObsluhaUdalostí
		 * @see #povoľViacnásobnúObsluhuUdalostí()
		 */
		public static void presmerujObsluhuUdalostí(ObsluhaUdalostí obsluha)
		{ ObsluhaUdalostí.počúvadlo = obsluha; }

		/** <p><a class="alias"></a> Alias pre {@link #presmerujObsluhuUdalostí(ObsluhaUdalostí) presmerujObsluhuUdalostí}.</p> */
		public static void presmerujObsluhuUdalosti(ObsluhaUdalostí obsluha)
		{ ObsluhaUdalostí.počúvadlo = obsluha; }

		/**
		 * <p>Táto metóda slúži na získanie aktívnej inštancie obsluhy udalostí.
		 * Je užitočná v súvislosti s možnosťou {@linkplain 
		 * #povoľViacnásobnúObsluhuUdalostí() povolenia viacnásobnej obsluhy
		 * udalostí}. Ak obsluha udalostí nebola definovaná, prípadne nie je
		 * z nejakého dôvodu aktívna žiadna z jestvujúcich inštancií obsluhy
		 * udalostí, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @return aktívna inštancia obsluhy udalostí alebo {@code valnull}
		 * 
		 * @see ObsluhaUdalostí#aktívna()
		 */
		public static ObsluhaUdalostí aktívnaObsluhaUdalostí()
		{ return ObsluhaUdalostí.počúvadlo; }

		/** <p><a class="alias"></a> Alias pre {@link #aktívnaObsluhaUdalostí() aktívnaObsluhaUdalostí}.</p> */
		public static ObsluhaUdalosti aktivnaObsluhuUdalosti()
		{ return (ObsluhaUdalosti)ObsluhaUdalostí.počúvadlo; }

		// Výplň

		/**
		 * <p>Vyplní podlahu zadanou farbou, ktorá prekryje aj
		 * {@linkplain #farbaPozadia(Color) farbu pozadia} sveta.
		 * Má rovnaký efekt, ako keby sme volali metódu {@link 
		 * Plátno#vyplň(Color) podlaha.vyplň(farba)}.</p>
		 * 
		 * @param farba objekt určujúci farbu výplne podlahy
		 * 
		 * @see #vymaž()
		 */
		public static void vyplň(Color farba) { GRobot.podlaha.vyplň(farba); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Color) vyplň}.</p> */
		public static void vypln(Color farba) { vyplň(farba); }

		/**
		 * <p>Vyplní podlahu farbou zadaného objektu, ktorá prekryje aj
		 * {@linkplain #farbaPozadia(Color) farbu pozadia} sveta.
		 * Má rovnaký efekt, ako keby sme volali metódu {@link 
		 * Plátno#vyplň(Farebnosť) podlaha.vyplň(farba)}.</p>
		 * 
		 * @param objekt objekt určujúci farbu výplne podlahy
		 * 
		 * @see #vymaž()
		 */
		public static void vyplň(Farebnosť objekt)
		{ GRobot.podlaha.vyplň(objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Color) vyplň}.</p> */
		public static void vypln(Farebnosť objekt) { vyplň(objekt); }

		/**
		 * <p>Vyplní podlahu farbou zadanou prostredníctvom farebných zložiek. Má
		 * rovnaký efekt, ako keby sme volali metódu {@link 
		 * Plátno#vyplň(int, int, int) podlaha.vyplň(r, g, b)}.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vyplň(Color)
		 */
		public static Farba vyplň(int r, int g, int b)
		{ return GRobot.podlaha.vyplň(r, g, b); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int) vyplň}.</p> */
		public static Farba vypln(int r, int g, int b)
		{ return GRobot.podlaha.vyplň(r, g, b); }

		/**
		 * <p>Vyplní podlahu farbou zadanou prostredníctvom farebných zložiek
		 * a úrovne priehľadnosti. Má rovnaký efekt, ako keby sme volali
		 * metódu {@link Plátno#vyplň(int, int, int, int)
		 * podlaha.vyplň(r, g, b, a)}.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255
		 *     (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vyplň(Color)
		 */
		public static Farba vyplň(int r, int g, int b, int a)
		{ return GRobot.podlaha.vyplň(r, g, b, a); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int, int) vyplň}.</p> */
		public static Farba vypln(int r, int g, int b, int a)
		{ return GRobot.podlaha.vyplň(r, g, b, a); }

		/**
		 * <p>Vyplní podlahu zadanou textúrou. Textúra je súbor s obrázkom, ktorý
		 * bude použitý na dlaždicové vyplnenie plochy podlahy. Má rovnaký
		 * efekt, ako keby sme volali metódu {@link 
		 * Plátno#vyplň(String) podlaha.vyplň(súbor)}.</p>
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
		 */
		public static void vyplň(String súbor)
		{ GRobot.podlaha.vyplň(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(String) vyplň}.</p> */
		public static void vypln(String súbor)
		{ GRobot.podlaha.vyplň(súbor); }

		/**
		 * <p>Vyplní podlahu zadanou textúrou. Textúra je {@linkplain Obrázok
		 * obrázok} (objekt typu {@link Image Image} alebo odvodený), ktorý
		 * bude použitý na dlaždicové vyplnenie plochy podlahy. Má rovnaký
		 * efekt, ako keby sme volali metódu {@link Plátno#vyplň(Image)
		 * podlaha.vyplň(obrázok)}.</p>
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
		 * @param výplň obrázok textúry
		 * 
		 * @see #vyplň(Color)
		 */
		public static void vyplň(Image výplň)
		{ GRobot.podlaha.vyplň(výplň); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Image) vyplň}.</p> */
		public static void vypln(Image výplň)
		{ GRobot.podlaha.vyplň(výplň); }


		// Parametre obrázkovej výplne

		/**
		 * <p>Vráti bod so súradnicami relatívneho posunutia obrázkových
		 * dlaždicových výplní.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} určujúci súradnice
		 *     relatívneho posunutia obrázkových dlaždicových výplní
		 * 
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne()
		 * @see #otočenieVýplne()
		 * @see #stredOtáčaniaVýplne()
		 */
		public static Bod posunutieVýplne()
		{
			return new Bod(posunutieVýplneX, posunutieVýplneY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #posunutieVýplne() posunutieVýplne}.</p> */
		public static Bod posunutieVyplne()
		{
			return new Bod(posunutieVýplneX, posunutieVýplneY);
		}

		/**
		 * <p>Nastaví relatívne posunutie obrázkových dlaždicových výplní.
		 * Každé ďalšie spustenie niektorého z príkazov vypĺňania bude
		 * používať novú relatívnu polohu dlaždíc. Predvolené posunutie
		 * [0, 0] znamená, že dlaždicová textúra sa začína v strede plátna,
		 * čo je platné aj pre vypĺňanie útvarov a oblastí, alebo v strede
		 * vypĺňaného obrázka.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci príklad zhŕňa použitie viacerých metód súvisiacich
		 * s úpravou parametrov dlaždicových výplní. Príklad si sám
		 * vygeneruje textúru, ktorá je predvolene dlaždicovo sa nadpájajúca
		 * alebo bezšvová (anglicky <em>tileable</em> alebo
		 * <em>seamless</em>), ale môže byť úmyselne pregenerovaná aj tak,
		 * aby sa dlaždicovo nenadpájala (aby bolo lepšie vidno jej
		 * hranice). Textúrou sú potom vypĺňané rôzne druhy cieľov, vrátane
		 * celého plátna. Parametre textúry sa dajú možné dynamicky
		 * upravovať myšou.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestVypĺňaniaTextúrou {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Mliečna farba výplne stropu v režime zobrazenia pomocníka}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@link Farba Farba} mliečna = {@code kwdnew} {@link Farba#Farba(double, double, double, double) Farba}({@code num1.0}, {@code num1.0}, {@code num1.0}, {@code num0.8});

				{@code comm// Oblasť, ktorá je jedným z cieľov vypĺňania:}
				{@code kwdprivate} {@link Oblasť Oblasť} oblasť;

				{@code comm// Obrázok, ktorý je ďalším z cieľov vypĺňania:}
				{@code kwdprivate} {@link Obrázok Obrázok} obrázok;

				{@code comm// Obrázok s vygenerovanou výplňou (textúrou):}
				{@code kwdprivate} {@link Obrázok Obrázok} textúra;

				{@code comm// Rôzne nastavenia:}
				{@code kwdprivate} {@code typeboolean} bezšvová;
				{@code kwdprivate} {@code typeboolean} zamknuté;
				{@code kwdprivate} {@code typeboolean} upravTextúru;
				{@code kwdprivate} {@code typeint} zobrazPomoc;
				{@code kwdprivate} {@code typeint} cieľVýplne;
				{@code kwdprivate} {@code typeint} typ;

				{@code comm// Zálohy pre jeden z režimov práce:}
				{@code kwdprivate} {@link Poloha Poloha} zálohaPosunutia;
				{@code kwdprivate} {@code typedouble} zálohaPootočenia;

				{@code comm// Konštruktor.}
				{@code kwdprivate} TestVypĺňaniaTextúrou()
				{
					{@code comm// Úprava rozmerov plátna (volaním nadradeného konštruktora):}
					{@code valsuper}({@code num1200}, {@code num600});

					{@code comm// Vypnutie automatického prekresľovania:}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

					{@code comm// Vytvorenie relatívne komplikovaného tvaru oblasti – medzikružie}
					{@code comm// prekryté dvomi pootočenými hviezdami – „jež v obruči“:}
					{@link GRobot#veľkosť(double) veľkosť}({@code num200});
					oblasť = {@code kwdnew} {@link Oblasť#Oblasť() Oblasť}();
					oblasť.{@link Oblasť#pridaj(Shape) pridaj}({@link GRobot#kruh(double) kruh}({@link GRobot#veľkosť() veľkosť}() * {@code num4.0} / {@code num5.0}));
					oblasť.{@link Oblasť#odober(Shape) odober}({@link GRobot#kruh(double) kruh}({@link GRobot#veľkosť() veľkosť}() * {@code num3.0} / {@code num4.0}));
					{@link GRobot#vpravo(double) vpravo}({@code num18});
					oblasť.{@link Oblasť#pridaj(Shape) pridaj}({@link GRobot#hviezda() hviezda}());
					{@link GRobot#vľavo(double) vľavo}({@code num36});
					oblasť.{@link Oblasť#pridaj(Shape) pridaj}({@link GRobot#hviezda() hviezda}());
					{@link GRobot#vpravo(double) vpravo}({@code num18});
					oblasť.{@link Oblasť#pridaj(Shape) pridaj}({@link GRobot#kruh(double) kruh}({@link GRobot#veľkosť() veľkosť}() * {@code num2.0} / {@code num3.0}));

					{@code comm// Vytvorenie práznych inštancií obrázkov. Prvá inštancia „obrázok“}
					{@code comm// je jedným z cieľov vypĺňania. Druhá inštancia „textúra“ je výplň}
					{@code comm// použitá na rôzne ciele (podľa nastavenia, ktoré sa dá meniť počas}
					{@code comm// činnosti programu):}
					obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num200}, {@code num200});
					textúra = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num160}, {@code num120});


					{@code comm// Vykonanie počiatočných nastavení príkladu:}

					bezšvová = {@code valtrue};
					zamknuté = {@code valfalse};
					upravTextúru = {@code valtrue};
					zobrazPomoc = {@code num3};
					cieľVýplne = {@code num2};
					typ = {@code num0};

					vygenerujTextúru();
					vyplňTextúrou();
					pomoc();
					{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
				}

				{@code comm// Generovanie textúry aktuálneho typu v aktuálnom režime}
				{@code kwdprivate} {@code typevoid} vygenerujTextúru()
				{
					{@code comm// Záloha stavu robota pred generovaním textúry (nesúvisí so zálohou}
					{@code comm// pre jeden z režimov – toto je nezávislá záloha tých vlastností}
					{@code comm// robota, ktoré táto metóda ovplyvňuje):}
					{@link Farba Farba} zálohaFarby = {@link GRobot#farba() farba}();
					{@link Poloha Poloha} zálohaPolohy = {@link GRobot#poloha() poloha}();
					{@code typedouble} zálohaSmeru  = {@link GRobot#smer() smer}();
					{@code typedouble} zálohaHrúbky = {@link GRobot#hrúbkaČiary() hrúbkaČiary}();

					{@code comm// Presmerovanie kreslenia robota. Toto je jediná vlastnosť, ktorú}
					{@code comm// metóda nezálohuje poctivo – spoliehame sa na to, že robot bude}
					{@code comm// pred aj po vykonaní tejto metódy kresliť na podlahu (čo je}
					{@code comm// predvolené nastavenie robota):}
					{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(textúra);
					textúra.{@link Obrázok#vymaž() vymaž}();

					{@code comm// Generovanie textúry aktuálneho typu:}
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num280}; ++i)
					{
						{@code comm// Farba, poloha, smer aj hrúbka čiary sú náhodne zmenené:}
						{@link GRobot#náhodnáFarba() náhodnáFarba}();
						{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
						{@link GRobot#náhodnýSmer() náhodnýSmer}();
						{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num3}, {@code num5}));

						{@code kwdif} (bezšvová)
						{
							{@code comm// Ak má byť textúra bezšvová, tak musí byť každá čiara}
							{@code comm// a tvar nakreslený deväť ráz – na aktuálnej pozícii a na}
							{@code comm// ôsmich ďalších pozíciách:}
							{@code comm//  – dvoch posunutých o šírku obrázka vpravo a vľavo,}
							{@code comm//  – dvoch posunutých o výšku obrázka hore a dole}
							{@code comm//  – a štyroch zvyšných diagonálnych smerov podľa rozmerov}
							{@code comm//    textúry.}
							{@code comm// }
							{@code comm// To je najjednoduchší spôsob ako získať istotu, že ak čiara}
							{@code comm// alebo tvar presahujú v niektorom smere von, tak budú určite}
							{@code comm// nakreslené yak, aby zasahovali do obrázka aj z protiľahlej}
							{@code comm// strany. (Základným predpokladom je, že čiara alebo tvar nie}
							{@code comm// sú niekoľkonásobne väčšie než sú rozmery obrázka. Ak by to}
							{@code comm// tak, nebolo, tak by generovanie bezšvovej textúry bolo}
							{@code comm// komplikovanejšie.)}
							{@code comm// }
							{@code comm// (Efektívnejším spôsobom, no komplikovanejším na}
							{@code comm// naprogramovanie, by bolo overovať, ktorý okraj alebo okraje}
							{@code comm// obrázka čiara alebo tvar prekročili a podľa toho ich}
							{@code comm// nakresliť pokračujúc z opačnej strany alebo strán.}
							{@code comm// }
							{@code comm// Ďalšia úroveň spoľahlivosti algoritmu by dokázala overiť aj}
							{@code comm// to, o koľko čiara prípadne tvar prekračujú okraj(e)}
							{@code comm// a zabezpečiť nakreslenie viacnásobného prekročenia hraníc}
							{@code comm// obrázka.}
							{@code comm// }
							{@code comm// Pri kreslení čiary vo vodorovnom alebo zvislom smere by išlo}
							{@code comm// len o viacnásobné prekreslenie čiary na určitom riadku alebo}
							{@code comm// stĺpci. Výsledný efekt by bol málo viditeľný. No ak by bola}
							{@code comm// čiara nakreslená pod určitým uhlom, tak by dostatočne dlhá}
							{@code comm// vhodne umiestnená čiara dokázala vyšrafovať celú plochu}
							{@code comm// obrázka. No s tým algoritmom, ktorý je použitý tu, by to}
							{@code comm// nefungovalo…}
							{@code comm// }
							{@code comm// Posledný tip: Ďalším spôsobom, ktorý by ani nemusel byť}
							{@code comm// taký náročný na naprogramovanie, by mohlo byť využitie}
							{@code comm// pretáčania obrázka s textúrou. Ak by tvar alebo čiara,}
							{@code comm// ktoré by mali tvoriť textúru boli vždy menšie než rozmery}
							{@code comm// textúry, tak by stačilo, aby sme vždy nakreslili tvar}
							{@code comm// alebo čiaru presne do stredu textúry a potom ju}
							{@code comm// pretočili o náhodný počet bodov v oboch smeroch – pozri}
							{@code comm// metódu pretoč(Δx, Δy).)}

							{@code comm// Zálohujeme polohu – budeme ju recyklovať ako východiskovú}
							{@code comm// polohu:}
							{@link Poloha Poloha} poloha = {@link GRobot#poloha() poloha}();

							{@code comm// Podľa hodnoty atribútu typ vygenerujeme textúru:}
							{@code kwdswitch} (typ % {@code num3})
							{
							{@code kwdcase} {@code num1}: {@code comm// krúžky – kružnice kreslené hrubšou čiarou}
								{
									{@code comm// (Vtáčie zátvorky sú tu preto, aby sme mohli}
									{@code comm// v rámci jednej vetvy, presnejšie v rámci}
									{@code comm// zátvoriek, definovať lokálne premenné.)}

									{@code comm// Vygenerujeme veľkosť:}
									{@code typedouble} veľkosť = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num3}, {@code num12});
									{@code comm// Nakreslíme strednú kružnicu:}
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// Presunieme sa do východiskovej polohy, posunieme}
									{@code comm// sa doľava o šírku textúry a nakreslíme ďalšiu}
									{@code comm// kružnicu:}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Podobne doprava.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Hore.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Dole.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Vľavo dole.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Vľavo hore.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Vpravo dole.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);

									{@code comm// (Vpravo hore.)}
									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kružnica(double) kružnica}(veľkosť);
								}
								{@code kwdbreak};

							{@code kwdcase} {@code num2}: {@code comm// čiarky}
								{
									{@code comm// (Princíp je rovnaký ako pri krúžkoch vyššie, ale}
									{@code comm// vygenerovaná hodnota je použitá ako dĺžka čiary.)}
									{@code typedouble} dĺžka = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num60}, {@code num100});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#dopredu(double) dopredu}(dĺžka);
								}
								{@code kwdbreak};

							{@code kwddefault}: {@code comm// plné kruhy}
								{
									{@code comm// (Princíp je rovnaký ako pri krúžkoch vyššie,}
									{@code comm// len sú kreslené plné kruhy.)}
									{@code typedouble} veľkosť = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num3}, {@code num12});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, {@code num0});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}({@code num0}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(-textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, -textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);

									{@link GRobot#skočNa(Poloha) skočNa}(poloha);
									{@link GRobot#skoč(double, double) skoč}(textúra.{@link Obrázok#šírka šírka}, textúra.{@link Obrázok#výška výška});
									{@link GRobot#kruh(double) kruh}(veľkosť);
								}
							}
						}
						{@code kwdelse}
						{
							{@code comm// V režime švovej textúry kreslíme každý tvar len raz:}
							{@code kwdswitch} (typ % {@code num3})
							{
							{@code kwdcase} {@code num1}:
								{@link GRobot#kružnica(double) kružnica}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num3}, {@code num12}));
								{@code kwdbreak};

							{@code kwdcase} {@code num2}:
								{@link GRobot#dopredu(double) dopredu}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num60}, {@code num100}));
								{@code kwdbreak};

							{@code kwddefault}:
								{@link GRobot#kruh(double) kruh}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num3}, {@code num12}));
							}
						}
					}

					{@code comm// Presmerujeme kreslenie späť na podlahu:}
					{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();

					{@code comm// Vrátenie zálohy vykonanej na začiatku tejto metódy:}
					{@link GRobot#farba(Color) farba}(zálohaFarby);
					{@link GRobot#skočNa(Poloha) skočNa}(zálohaPolohy);
					{@link GRobot#smer(double) smer}(zálohaSmeru);
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}(zálohaHrúbky);
				}

				{@code comm// Vyplnenie aktuálneho cieľa vypĺňania textúrou}
				{@code kwdprivate} {@code typevoid} vyplňTextúrou()
				{
					{@code comm// Vymazanie grafiky podlahy:}
					{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();

					{@code kwdif} (zamknuté)
					{
						{@code comm// Ak je posúvanie a otáčanie textúry zamknuté, tak vykonáme}
						{@code comm// posunutie a otočenie výplne podľa aktuálnych parametrov robota}
						{@code comm// (zaujímavo sa to prejaví na vypĺňanom obrázku, ktorý pracuje}
						{@code comm// akoby so samostatnou súradnicovou sústavou):}
						{@link Svet Svet}.{@link Svet#posunutieVýplne(Poloha) posunutieVýplne}({@code valthis});
						{@link Svet Svet}.{@link Svet#pootočenieVýplne(double) pootočenieVýplne}({@code valthis});
					}

					{@code comm// Podľa cieľa výplne prekreslíme požadovanú súčasť (vždy použijeme}
					{@code comm// príkaz vyplň tej ktorej súčasti, aby bolo vidno výsledok}
					{@code comm// s aktuálnymi parametrami textúry):}
					{@code kwdswitch} (cieľVýplne)
					{
					{@code kwdcase} {@code num0}:
						obrázok.{@link Obrázok#vymaž() vymaž}();
						obrázok.{@link Obrázok#vyplň(Image) vyplň}(textúra);
						{@link GRobot#obrázok(Image, double) obrázok}(obrázok, {@code num1.0});
						{@code kwdbreak};

					{@code kwdcase} {@code num1}:
						{@code comm// Kreslenie tvarov nie je vypnuté, preto sa hviezda zároveň}
						{@code comm// nakreslí na plochu plátna aktuálnou hrúbkou a farbou čiary.}
						{@link GRobot#vyplňTvar(Shape) vyplňTvar}({@link GRobot#hviezda() hviezda}(), textúra);
						{@code kwdbreak};

					{@code kwdcase} {@code num2}:
						{@link GRobot#vyplňOblasť(Area, Image) vyplňOblasť}(oblasť, textúra);
						{@code kwdbreak};

					{@code kwdcase} {@code num3}:
						{@link Plátno podlaha}.{@link Plátno#vyplň(Image) vyplň}(textúra);
						{@code kwdbreak};
					}
				}

				{@code comm// Výpis obsahu elektronického pomocníka}
				{@code kwdprivate} {@code typevoid} pomoc()
				{
					{@link Plátno strop}.{@link Plátno#vymažTexty() vymažTexty}();
					{@link Plátno strop}.{@link Plátno#vymažGrafiku() vymažGrafiku}();

					{@code kwdif} (zobrazPomoc &gt; {@code num0})
					{
						{@link Plátno strop}.{@link Plátno#vyplň(Image) vyplň}(mliečna);

						{@code typeint} zobrazenie = (zobrazPomoc &#45; {@code num1}) % {@code num3};

						{@code kwdif} (zobrazenie &gt;= {@code num2})
						{
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Medzerníkom prepnete medzi režimom úprav (transformácie) textúry a tvaru."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Enterom sa prepnete medzi režimom zamknutého alebo voľného posunu a otáčania textúry s tvarom."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Klávesom F1 zobrazíte tohto pomocníka (viacnásobné stlačenie prepína mieru zobrazených informácií)."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Klávesom Escape skryjete pomocníka."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Klávesy 1 až 4 prepínajú ciele vypĺňania: obrázok, tvar hviezdy, oblasť ježa v obruči a celú plochu podlahy. (Všímajte si odlišnosti.)"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Kláves F prepne na ďalší typ textúry a vygeneruje novú náhodnú textúru (v aktuálnom režime)."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Kláves G vygeneruje novú náhodnú textúru aktuálneho typu v aktuálnom režime."});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Kláves H prepne medzi režimom bezšvovej a švovej textúry a vygeneruje novú náhodnú textúru (aktuálneho typu)."}, {@link Konštanty#riadok riadok});
						}

						{@code kwdif} (zobrazenie &gt;= {@code num1})
						{
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Kliknutím alebo ťahaním niektorým tlačidlom myši vykonáte jednu z nasledujúcich akcií s tvarom (v režime úprav tvaru):"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" — ľavé tlačidlo: posunutie tvaru,"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" — pravé tlačidlo: pootočenie tvaru."}, {@link Konštanty#riadok riadok});

							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Kliknutím alebo ťahaním niektorým tlačidlom myši vykonáte jednu z nasledujúcich akcií s textúrou (v režime úprav textúry):"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" — ak je posúvanie a otáčenie textúry voľné:"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"   — ľavé tlačidlo: posunutie textúry,"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"   — pravé tlačidlo: pootočenie textúry,"}, {@link Konštanty#riadok riadok});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" — ak je posúvanie a otáčenie textúry zamknuté:"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"   — ľavé tlačidlo: posunutie stredu otáčania textúry,"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"   — pravé tlačidlo: zmena mierky textúry."}, {@link Konštanty#riadok riadok});
						}

						{@code kwdif} (zobrazenie &gt;= {@code num0})
						{
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Aktuálny stav:"}, {@link Konštanty#riadok riadok});

							{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg" — Cieľ výplne: "});
							{@code kwdswitch} (cieľVýplne)
							{
							{@code kwdcase} {@code num0}: {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"obrázok"}); {@code kwdbreak};
							{@code kwdcase} {@code num1}: {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"tvar hviezdy"}); {@code kwdbreak};
							{@code kwdcase} {@code num2}: {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"oblasť ježa v obruči"}); {@code kwdbreak};
							{@code kwdcase} {@code num3}: {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"podlaha"}); {@code kwdbreak};
							}
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"."});

							{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg" — Režim úprav: "});
							{@code kwdif} (upravTextúru)
								{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"textúra"});
							{@code kwdelse}
								{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"tvaru"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"."});

							{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg" — Stav zámku textúry: "});
							{@code kwdif} (zamknuté)
								{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"zamknuté"});
							{@code kwdelse}
								{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"voľné"});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"."});
						}

						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@link Konštanty#riadok riadok}, {@code srg"(Tip: Ak je miera zobrazených textov pomocníka vyššia, tak kolieskom myši môžete rolovať text…)"});

						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@link Konštanty#riadok riadok}, {@code srg"Stlačte Escape…"});
					}
				}


				{@code comm// Obsluhy udalostí (časovača, myši a klávesnice):}


				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
				{
					{@link GRobot#ťahanieMyšou() ťahanieMyšou}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
				{
					{@code kwdif} (upravTextúru)
					{
						{@code kwdif} (zamknuté)
						{
							{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ}))
								{@link Svet Svet}.{@link Svet#stredOtáčaniaVýplne(double, double) stredOtáčaniaVýplne}(
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#súradnicaMyšiX() súradnicaMyšiX}() &#45; {@link GRobot#súradnicaX() súradnicaX}(),
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#súradnicaMyšiY() súradnicaMyšiY}() &#45; {@link GRobot#súradnicaY() súradnicaY}());
							{@code kwdelse}
								{@link Svet Svet}.{@link Svet#mierkaVýplne(double, double) mierkaVýplne}({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#súradnicaMyšiX() súradnicaMyšiX}() / {@code num100.0},
									{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#súradnicaMyšiY() súradnicaMyšiY}() / {@code num100.0});
						}
						{@code kwdelse}
						{
							{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ}))
								{@link Svet Svet}.{@link Svet#posunutieVýplneNaMyš() posunutieVýplneNaMyš}();
							{@code kwdelse}
								{@link Svet Svet}.{@link Svet#pootočenieVýplneNaMyš() pootočenieVýplneNaMyš}();
						}
					}
					{@code kwdelse}
					{
						{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#ĽAVÉ ĽAVÉ}))
							{@link GRobot#skočNaMyš() skočNaMyš}();
						{@code kwdelse}
							{@link GRobot#otočNaMyš() otočNaMyš}();
					}

					vyplňTextúrou();
				}

				{@code kwd@}SuppressWarnings({@code srg"fallthrough"}) {@code comm// toto je vysvetlené na konci metódy}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieKlávesu() uvoľnenieKlávesu}()
				{
					{@code kwdswitch} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves() kláves}())
					{
						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#MEDZERA MEDZERA}:
							upravTextúru = !upravTextúru;
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#ENTER ENTER}:
							{@code kwdif} (zamknuté)
							{
								{@link Svet Svet}.{@link Svet#posunutieVýplne(Poloha) posunutieVýplne}(zálohaPosunutia);
								{@link Svet Svet}.{@link Svet#pootočenieVýplne(double) pootočenieVýplne}(zálohaPootočenia);
							}
							{@code kwdelse}
							{
								zálohaPosunutia = {@link Svet Svet}.{@link Svet#posunutieVýplne() posunutieVýplne}();
								zálohaPootočenia = {@link Svet Svet}.{@link Svet#pootočenieVýplne() pootočenieVýplne}();
							}
							zamknuté = !zamknuté;
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#ESCAPE ESCAPE}:
							{@code kwdif} (zobrazPomoc &gt; {@code num0})
								zobrazPomoc = -zobrazPomoc;
							{@code kwdelse} {@code kwdif} ({@code num0} == zobrazPomoc)
								zobrazPomoc = -{@code num3};
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_F1 VK_F1}:
							{@code kwdif} (zobrazPomoc &lt; {@code num0})
								zobrazPomoc = -zobrazPomoc;
							{@code kwdelse} {@code kwdif} ({@code num0} == zobrazPomoc)
								zobrazPomoc = {@code num3};
							{@code kwdelse}
								++zobrazPomoc;
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_1 VK_1}:
							cieľVýplne = {@code num0};
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_2 VK_2}:
							cieľVýplne = {@code num1};
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_3 VK_3}:
							cieľVýplne = {@code num2};
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_4 VK_4}:
							cieľVýplne = {@code num3};
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_F VK_F}:
							++typ;
							bezšvová = !bezšvová;
							{@code comm// Na tomto mieste býva obvykle príkaz break. Jeho}
							{@code comm// neprítomnosť tu nie je chybou. Ide o úmyselné}
							{@code comm// prepadnutie sa do ďalšej vetvy, ktorá sa prepadáva}
							{@code comm// ešte hlbšie, takže posledné tri príkazy}
							{@code comm// (vygenerujTextúru – pomoc) sú vykonané vo všetkých}
							{@code comm// troch vetvách.}
							{@code comm// }
							{@code comm// Posledný príkaz v tejto vetve (bezšvová = !bezšvová)}
							{@code comm// iba kompenzuje účinok nasledujúcej vetvy tak, aby}
							{@code comm// bola hodnota príznaku „bešvová“ po vykonaní tejto}
							{@code comm// vetvy nezmenená (pretože stlačenie klávesu F má mať}
							{@code comm// za následok iba zmenu typu generovanej textúry, nie}
							{@code comm// švového režimu).}
							{@code comm// }
							{@code comm// Java by programátora varovala, že v tejto metóde sa}
							{@code comm// nachádza prepadnutie sa medzi vetvami riadiacej}
							{@code comm// štruktúry switch, preto je pred deklaračnou časťou}
							{@code comm// tejto metódy anotácia @SuppressWarnings("fallthrough"),}
							{@code comm// ktorá varovanie potlačí. Túto anotáciu smie}
							{@code comm// programátor použiť len v prípade, že o prepadnutí sa,}
							{@code comm// vie a úmyselne ho chce vo svojej metóde ponechať.}

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_H VK_H}:
							bezšvová = !bezšvová;

						{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VK_G VK_G}:
							vygenerujTextúru();
							vyplňTextúrou();
							pomoc();
							{@code kwdbreak};
					}
				}


				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestVypĺňaniaTextúrou.cfg"});
					{@code kwdnew} TestVypĺňaniaTextúrou();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>vyplnanieTexturou.png<alt/>Príklad ukazujúci možnosti
		 * výplne tesne po spustení.</image>Ukážka výsledku – úvodné
		 * zobrazenie.</p>
		 * 
		 * @param x x-ová súradnica určujúca novú hodnotu relatívneho
		 *     posunutia obrázkových dlaždicových výplní
		 * @param y y-ová súradnica určujúca novú hodnotu relatívneho
		 *     posunutia obrázkových dlaždicových výplní
		 * 
		 * @see #posunutieVýplne()
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #otočenieVýplne(double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void posunutieVýplne(double x, double y)
		{
			posunutieVýplneX = x;
			posunutieVýplneY = y;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #posunutieVýplne(double, double) posunutieVýplne}.</p> */
		public static void posunutieVyplne(double x, double y)
		{
			posunutieVýplne(x, y);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #posunutieVýplne(double, double) posunutieVýplne(x, y)}.
		 * Rozdielom je, že namiesto dvoch samostatných súradníc prijíma
		 * objekt, ktorého polohu použije namiesto zadania súradníc. Inak
		 * funguje táto metóda úplne rovnako ako spomenutá metóda. Všetky
		 * informácie uvedené v jej opise sú tiež relevantné, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param poloha objekt určujúci nové relatívne posunutie výplní
		 * 
		 * @see #posunutieVýplne(double, double)
		 */
		public static void posunutieVýplne(Poloha poloha)
		{
			posunutieVýplneX = poloha.polohaX();
			posunutieVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #posunutieVýplne(Poloha) posunutieVýplne}.</p> */
		public static void posunutieVyplne(Poloha poloha)
		{
			posunutieVýplne(poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #posunutieVýplne(double, double) posunutieVýplne(x, y)}.
		 * Rozdielom je, že namiesto dvoch samostatných súradníc prijíma
		 * objekt, ktorého polohu použije namiesto zadania súradníc. Inak
		 * funguje táto metóda úplne rovnako ako spomenutá metóda. Všetky
		 * informácie uvedené v jej opise sú tiež relevantné, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param tvar objekt určujúci nové relatívne posunutie výplní
		 * 
		 * @see #posunutieVýplne(double, double)
		 */
		public static void posunutieVýplne(Shape tvar)
		{
			Rectangle2D hranice = tvar.getBounds2D();
			posunutieVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			posunutieVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #posunutieVýplne(Shape) posunutieVýplne}.</p> */
		public static void posunutieVyplne(Shape tvar)
		{
			posunutieVýplne(tvar);
		}

		/**
		 * <p>Vráti bod s hodnotami mierky obrázkových dlaždicových výplní
		 * v osiach x a y.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} určujúci hodnoty mierky
		 *     obrázkových dlaždicových výplní v osiach x a y
		 * 
		 * @see #mierkaVýplne(double, double)
		 * @see #posunutieVýplne()
		 * @see #otočenieVýplne()
		 * @see #stredOtáčaniaVýplne()
		 */
		public static Bod mierkaVýplne()
		{
			return new Bod(mierkaVýplneX, mierkaVýplneY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #mierkaVýplne() mierkaVýplne}.</p> */
		public static Bod mierkaVyplne()
		{
			return new Bod(mierkaVýplneX, mierkaVýplneY);
		}

		/**
		 * <p>Nastaví mierku obrázkových dlaždicových výplní (v oboch osiach
		 * naraz). Každé ďalšie spustenie niektorého z príkazov vypĺňania
		 * bude používať novú mierku. Hodnota mierky 1.0 je neutrálna –
		 * znamená, že rozmer textúry nebude zmenený. Hodnoty menšie než
		 * 1.0 (ale väčšie než 0.0 – to jest neceločíselné hodnoty)
		 * znamenajú zmenšenie a hodnoty väčšie než 1.0 znamenajú
		 * zväčšenie.</p>
		 * 
		 * @param mierka hodnota určujúca novú mierku textúry
		 * 
		 * @see #mierkaVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double, double)
		 * @see #otočenieVýplne(double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void mierkaVýplne(double mierka)
		{
			mierkaVýplneX = mierkaVýplneY = mierka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #mierkaVýplne(double) mierkaVýplne}.</p> */
		public static void mierkaVyplne(double mierka)
		{
			mierkaVýplne(mierka);
		}

		/**
		 * <p>Nastaví mierku obrázkových dlaždicových výplní jednotlivo pre
		 * osi x a y. Každé ďalšie spustenie niektorého z príkazov
		 * vypĺňania bude používať nové mierky. Hodnota mierky 1.0 pre
		 * niektorú z osí je neutrálna – znamená, že rozmer textúry nebude
		 * v smere tejto osi zmenený. Hodnoty menšie než 1.0 (ale väčšie
		 * než 0.0 – to jest neceločíselné hodnoty) znamenajú zmenšenie
		 * v určenom smere a hodnoty väčšie než 1.0 znamenajú zväčšenie.</p>
		 * 
		 * @param mx hodnota určujúca novú mierku v smere osi x
		 * @param my hodnota určujúca novú mierku v smere osi y
		 * 
		 * @see #mierkaVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #otočenieVýplne(double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void mierkaVýplne(double mx, double my)
		{
			mierkaVýplneX = mx;
			mierkaVýplneY = my;
		}

		/** <p><a class="alias"></a> Alias pre {@link #mierkaVýplne(double, double) mierkaVýplne}.</p> */
		public static void mierkaVyplne(double mx, double my)
		{
			mierkaVýplne(mx, my);
		}

		/**
		 * <p>Vráti hodnotu pootočenia obrázkových dlaždicových výplní.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} určujúci mieru pootočenia
		 *     obrázkových dlaždicových výplní
		 * 
		 * @see #otočenieVýplne(double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see #posunutieVýplne()
		 * @see #mierkaVýplne()
		 * @see #stredOtáčaniaVýplne()
		 */
		public static double otočenieVýplne()
		{
			return otočVýplňΑ;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne() otočenieVýplne}.</p> */
		public static double otocenieVyplne()
		{
			return otočVýplňΑ;
		}

		/**
		 * <p>Vráti hodnotu pootočenia obrázkových dlaždicových výplní.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} určujúci mieru pootočenia
		 *     obrázkových dlaždicových výplní
		 * 
		 * @see #pootočenieVýplne(double)
		 * @see #pootočenieVýplne(double, double, double)
		 * @see #posunutieVýplne(double, double)
		 * @see #posunutieVýplne()
		 * @see #mierkaVýplne()
		 * @see #stredOtáčaniaVýplne()
		 */
		public static double pootočenieVýplne()
		{
			return otočVýplňΑ;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne() pootočenieVýplne}.</p> */
		public static double pootocenieVyplne()
		{
			return otočVýplňΑ;
		}

		/**
		 * <p>Nastaví nový uhol pootočenia obrázkových dlaždicových výplní.
		 * Každé ďalšie spustenie niektorého z príkazov vypĺňania bude
		 * pracovať s pootočenou textúrou. Predvolené je nulové, to jest
		 * žiadne, pootočenie so stredom otáčania v bode relatívneho
		 * posunutia textúry – pozri príkaz {@link #posunutieVýplne(double,
		 * double) posunutieVýplne(x, y)}.</p>
		 * 
		 * @param uhol nový uhol pootočenia obrázkových dlaždicových výplní
		 * 
		 * @see #otočenieVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void otočenieVýplne(double uhol)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(double) otočenieVýplne}.</p> */
		public static void otocenieVyplne(double uhol)
		{
			otočenieVýplne(uhol);
		}

		/**
		 * <p>Nastaví nový uhol pootočenia obrázkových dlaždicových výplní.
		 * Každé ďalšie spustenie niektorého z príkazov vypĺňania bude
		 * pracovať s pootočenou textúrou. Predvolené je nulové, to jest
		 * žiadne, pootočenie so stredom otáčania v bode relatívneho
		 * posunutia textúry – pozri príkaz {@link #posunutieVýplne(double,
		 * double) posunutieVýplne(x, y)}.</p>
		 * 
		 * @param uhol nový uhol pootočenia obrázkových dlaždicových výplní
		 * 
		 * @see #pootočenieVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #pootočenieVýplne(double, double, double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void pootočenieVýplne(double uhol)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(double) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(double uhol)
		{
			pootočenieVýplne(uhol);
		}

		/**
		 * <p>Nastaví nový uhol pootočenia a zároveň nový relatívny stred
		 * otáčania obrázkových dlaždicových výplní.
		 * Každé ďalšie spustenie niektorého z príkazov vypĺňania bude
		 * pracovať s novými parametrami.
		 * Predvolené je nulové, to jest žiadne, pootočenie so stredom
		 * otáčania [0, 0], ktorý označuje bod relatívneho posunutia textúry –
		 * pozri príkaz {@link #posunutieVýplne(double, double)
		 * posunutieVýplne(x, y)}.</p>
		 * 
		 * @param uhol nový uhol pootočenia obrázkových dlaždicových výplní
		 * @param sx x-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * @param sy y-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * 
		 * @see #otočenieVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #otočenieVýplne(double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void otočenieVýplne(double uhol, double sx, double sy)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = sx;
			stredOtáčaniaVýplneY = sy;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(double, double, double) otočenieVýplne}.</p> */
		public static void otocenieVyplne(double uhol, double sx, double sy)
		{
			otočenieVýplne(uhol, sx, sy);
		}

		/**
		 * <p>Nastaví nový uhol pootočenia a zároveň nový relatívny stred
		 * otáčania obrázkových dlaždicových výplní.
		 * Každé ďalšie spustenie niektorého z príkazov vypĺňania bude
		 * pracovať s novými parametrami.
		 * Predvolené je nulové, to jest žiadne, pootočenie so stredom
		 * otáčania [0, 0], ktorý označuje bod relatívneho posunutia textúry –
		 * pozri príkaz {@link #posunutieVýplne(double, double)
		 * posunutieVýplne(x, y)}.</p>
		 * 
		 * @param uhol nový uhol pootočenia obrázkových dlaždicových výplní
		 * @param sx x-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * @param sy y-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * 
		 * @see #pootočenieVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #pootočenieVýplne(double)
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void pootočenieVýplne(double uhol, double sx, double sy)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = sx;
			stredOtáčaniaVýplneY = sy;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(double, double, double) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(double uhol, double sx, double sy)
		{
			pootočenieVýplne(uhol, sx, sy);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double) otočenieVýplne(uhol)}. Rozdielom
		 * je, že namiesto číselnej hodnoty smeru prijíma objekt, ktorý
		 * musí byť implementáciou rozhrania {@link Smer Smer}. Inak
		 * funguje táto metóda úplne rovnako ako spomenutá metóda.
		 * Všetky informácie uvedené v jej opise sú tiež relevantné,
		 * preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * 
		 * @see #otočenieVýplne(double)
		 */
		public static void otočenieVýplne(Smer smer)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(Smer) otočenieVýplne}.</p> */
		public static void otocenieVyplne(Smer smer)
		{
			otočenieVýplne(smer);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double) pootočenieVýplne(uhol)}.
		 * Rozdielom je, že namiesto číselnej hodnoty smeru prijíma objekt,
		 * ktorý musí byť implementáciou rozhrania {@link Smer Smer}. Inak
		 * funguje táto metóda úplne rovnako ako spomenutá metóda.
		 * Všetky informácie uvedené v jej opise sú tiež relevantné,
		 * preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * 
		 * @see #pootočenieVýplne(double)
		 */
		public static void pootočenieVýplne(Smer smer)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(Smer) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(Smer smer)
		{
			pootočenieVýplne(smer);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double, double, double)
		 * otočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto číselnej
		 * hodnoty smeru prijíma objekt, ktorý musí byť implementáciou
		 * rozhrania {@link Smer Smer}. Inak funguje táto metóda úplne
		 * rovnako ako spomenutá metóda. Všetky informácie uvedené v jej
		 * opise sú tiež relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param sx nová x-ová súradnica relatívneho posunutia stredu
		 *     otáčania výplní
		 * @param sy nová y-ová súradnica relatívneho posunutia stredu
		 *     otáčania výplní
		 * 
		 * @see #otočenieVýplne(double, double, double)
		 */
		public static void otočenieVýplne(Smer smer, double sx, double sy)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = sx;
			stredOtáčaniaVýplneY = sy;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(Smer, double, double) otočenieVýplne}.</p> */
		public static void otocenieVyplne(Smer smer, double sx, double sy)
		{
			otočenieVýplne(smer, sx, sy);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double, double, double)
		 * otočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania prijíma
		 * objekt, ktorého polohu použije v relatívnom kontexte namiesto
		 * priameho zadania hodnôt súradníc (čiže absolútne súradnice polohy
		 * objektu použije priamo ako relatívne hodnoty – nie je vykonaný
		 * žiadny prepočet). Inak funguje táto metóda úplne rovnako ako
		 * spomenutá metóda. Všetky informácie uvedené v jej opise sú tiež
		 * relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param uhol nový uhol pootočenia výplní
		 * @param poloha objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #otočenieVýplne(double, double, double)
		 */
		public static void otočenieVýplne(double uhol, Poloha poloha)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = poloha.polohaX();
			stredOtáčaniaVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(double, Poloha) otočenieVýplne}.</p> */
		public static void otocenieVyplne(double uhol, Poloha poloha)
		{
			otočenieVýplne(uhol, poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double, double, double)
		 * otočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto číselnej
		 * hodnoty smeru prijíma objekt, ktorý musí byť implementáciou
		 * rozhrania {@link Smer Smer} a namiesto dvoch samostatných súradníc
		 * určujúcich vysunutie stredu otáčania prijíma objekt, ktorého
		 * polohu použije v relatívnom kontexte namiesto priameho zadania
		 * hodnôt súradníc (čiže absolútne súradnice polohy objektu použije
		 * priamo ako relatívne hodnoty – nie je vykonaný žiadny prepočet).
		 * Inak funguje táto metóda úplne rovnako ako spomenutá metóda.
		 * Všetky informácie uvedené v jej opise sú tiež relevantné, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param poloha objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #otočenieVýplne(double, double, double)
		 */
		public static void otočenieVýplne(Smer smer, Poloha poloha)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = poloha.polohaX();
			stredOtáčaniaVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(Smer, Poloha) otočenieVýplne}.</p> */
		public static void otocenieVyplne(Smer smer, Poloha poloha)
		{
			otočenieVýplne(smer, poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double, double, double)
		 * otočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania prijíma
		 * objekt, ktorého polohu použije v relatívnom kontexte namiesto
		 * priameho zadania hodnôt súradníc (čiže absolútne súradnice polohy
		 * objektu použije priamo ako relatívne hodnoty – nie je vykonaný
		 * žiadny prepočet). Inak funguje táto metóda úplne rovnako ako
		 * spomenutá metóda. Všetky informácie uvedené v jej opise sú tiež
		 * relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param uhol nový uhol pootočenia výplní
		 * @param tvar objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #otočenieVýplne(double, double, double)
		 */
		public static void otočenieVýplne(double uhol, Shape tvar)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			Rectangle2D hranice = tvar.getBounds2D();
			stredOtáčaniaVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			stredOtáčaniaVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(double, Shape) otočenieVýplne}.</p> */
		public static void otocenieVyplne(double uhol, Shape tvar)
		{
			otočenieVýplne(uhol, tvar);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #otočenieVýplne(double, double, double)
		 * otočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto číselnej
		 * hodnoty smeru prijíma objekt, ktorý musí byť implementáciou
		 * rozhrania {@link Smer Smer} a namiesto dvoch samostatných súradníc
		 * určujúcich vysunutie stredu otáčania prijíma objekt, ktorého
		 * polohu použije v relatívnom kontexte namiesto priameho zadania
		 * hodnôt súradníc (čiže absolútne súradnice polohy objektu použije
		 * priamo ako relatívne hodnoty – nie je vykonaný žiadny prepočet).
		 * Inak funguje táto metóda úplne rovnako ako spomenutá metóda.
		 * Všetky informácie uvedené v jej opise sú tiež relevantné, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param tvar objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #otočenieVýplne(double, double, double)
		 */
		public static void otočenieVýplne(Smer smer, Shape tvar)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			Rectangle2D hranice = tvar.getBounds2D();
			stredOtáčaniaVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			stredOtáčaniaVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplne(Smer, Shape) otočenieVýplne}.</p> */
		public static void otocenieVyplne(Smer smer, Shape tvar)
		{
			otočenieVýplne(smer, tvar);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double, double, double)
		 * pootočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto
		 * číselnej hodnoty smeru prijíma objekt, ktorý musí byť
		 * implementáciou rozhrania {@link Smer Smer}. Inak funguje táto
		 * metóda úplne rovnako ako spomenutá metóda. Všetky informácie
		 * uvedené v jej opise sú tiež relevantné, preto odporúčame prečítať
		 * si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param sx nová x-ová súradnica relatívneho posunutia stredu
		 *     otáčania výplní
		 * @param sy nová y-ová súradnica relatívneho posunutia stredu
		 *     otáčania výplní
		 * 
		 * @see #pootočenieVýplne(double, double, double)
		 */
		public static void pootočenieVýplne(Smer smer, double sx, double sy)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = sx;
			stredOtáčaniaVýplneY = sy;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(Smer, double, double) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(Smer smer, double sx, double sy)
		{
			pootočenieVýplne(smer, sx, sy);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double, double, double)
		 * pootočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania prijíma
		 * objekt, ktorého polohu použije v relatívnom kontexte namiesto
		 * priameho zadania hodnôt súradníc (čiže absolútne súradnice polohy
		 * objektu použije priamo ako relatívne hodnoty – nie je vykonaný
		 * žiadny prepočet). Inak funguje táto metóda úplne rovnako ako
		 * spomenutá metóda. Všetky informácie uvedené v jej opise sú tiež
		 * relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param uhol nový uhol pootočenia výplní
		 * @param poloha objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #pootočenieVýplne(double, double, double)
		 */
		public static void pootočenieVýplne(double uhol, Poloha poloha)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = poloha.polohaX();
			stredOtáčaniaVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(double, Poloha) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(double uhol, Poloha poloha)
		{
			pootočenieVýplne(uhol, poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double, double, double)
		 * pootočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto
		 * číselnej hodnoty smeru prijíma objekt, ktorý musí byť
		 * implementáciou rozhrania {@link Smer Smer} a namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania prijíma
		 * objekt, ktorého polohu použije v relatívnom kontexte namiesto
		 * priameho zadania hodnôt súradníc (čiže absolútne súradnice polohy
		 * objektu použije priamo ako relatívne hodnoty – nie je vykonaný
		 * žiadny prepočet). Inak funguje táto metóda úplne rovnako ako
		 * spomenutá metóda. Všetky informácie uvedené v jej opise sú tiež
		 * relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param poloha objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #pootočenieVýplne(double, double, double)
		 */
		public static void pootočenieVýplne(Smer smer, Poloha poloha)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			stredOtáčaniaVýplneX = poloha.polohaX();
			stredOtáčaniaVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(Smer, Poloha) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(Smer smer, Poloha poloha)
		{
			pootočenieVýplne(smer, poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double, double, double)
		 * pootočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania prijíma
		 * objekt, ktorého polohu použije v relatívnom kontexte namiesto
		 * priameho zadania hodnôt súradníc (čiže absolútne súradnice polohy
		 * objektu použije priamo ako relatívne hodnoty – nie je vykonaný
		 * žiadny prepočet). Inak funguje táto metóda úplne rovnako ako
		 * spomenutá metóda. Všetky informácie uvedené v jej opise sú tiež
		 * relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param uhol nový uhol pootočenia výplní
		 * @param tvar objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #pootočenieVýplne(double, double, double)
		 */
		public static void pootočenieVýplne(double uhol, Shape tvar)
		{
			otočVýplňΑ = uhol % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			Rectangle2D hranice = tvar.getBounds2D();
			stredOtáčaniaVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			stredOtáčaniaVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(double, Shape) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(double uhol, Shape tvar)
		{
			pootočenieVýplne(uhol, tvar);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #pootočenieVýplne(double, double, double)
		 * pootočenieVýplne(uhol, sx, sy)}. Rozdielom je, že namiesto
		 * číselnej hodnoty smeru prijíma objekt, ktorý musí byť
		 * implementáciou rozhrania {@link Smer Smer} a namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania
		 * prijíma objekt, ktorého polohu použije v relatívnom kontexte
		 * namiesto priameho zadania hodnôt súradníc (čiže absolútne
		 * súradnice polohy objektu použije priamo ako relatívne hodnoty –
		 * nie je vykonaný žiadny prepočet). Inak funguje táto metóda úplne
		 * rovnako ako spomenutá metóda. Všetky informácie uvedené v jej
		 * opise sú tiež relevantné, preto odporúčame prečítať si aj jej
		 * opis.</p>
		 * 
		 * @param smer objekt určujúci nový uhol pootočenia výplní
		 * @param tvar objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #pootočenieVýplne(double, double, double)
		 */
		public static void pootočenieVýplne(Smer smer, Shape tvar)
		{
			otočVýplňΑ = smer.smer() % 360;
			if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			Rectangle2D hranice = tvar.getBounds2D();
			stredOtáčaniaVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			stredOtáčaniaVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplne(Smer, Shape) pootočenieVýplne}.</p> */
		public static void pootocenieVyplne(Smer smer, Shape tvar)
		{
			pootočenieVýplne(smer, tvar);
		}

		/**
		 * <p>Vráti bod so súradnicami relatívneho posunutia stredu otáčania
		 * obrázkových dlaždicových výplní vztiahnutý k súradniciam posunutia
		 * výplne.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} určujúci súradnice
		 *     relatívneho posunutia stredu otáčania obrázkových
		 *     dlaždicových výplní vztiahnutý k súradniciam posunutia výplne
		 * 
		 * @see #stredOtáčaniaVýplne(double, double)
		 * @see #posunutieVýplne()
		 * @see #mierkaVýplne()
		 * @see #otočenieVýplne()
		 */
		public static Bod stredOtáčaniaVýplne()
		{
			return new Bod(stredOtáčaniaVýplneX, stredOtáčaniaVýplneY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaVýplne() stredOtáčaniaVýplne}.</p> */
		public static Bod stredOtacaniaVyplne()
		{
			return new Bod(stredOtáčaniaVýplneX, stredOtáčaniaVýplneY);
		}

		/**
		 * <p>Nastaví nový relatívny stred otáčania obrázkových dlaždicových
		 * výplní. Každé ďalšie spustenie niektorého z príkazov vypĺňania
		 * bude pracovať s novými parametrami. Tento parameter má na textúru
		 * vplyv len pri nenulovom {@linkplain #otočenieVýplne(double)
		 * uhle pootočenia} výplní.
		 * Predvolený stred otáčania [0, 0] označuje bod relatívneho
		 * posunutia textúry – pozri príkaz {@link #posunutieVýplne(double,
		 * double) posunutieVýplne(x, y)}.</p>
		 * 
		 * @param uhol nový uhol pootočenia obrázkových dlaždicových výplní
		 * @param sx x-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * @param sy y-ová súradnica nového relatívneho stredu otáčania
		 *     obrázkových dlaždicových výplní
		 * 
		 * @see #stredOtáčaniaVýplne()
		 * @see #posunutieVýplne(double, double)
		 * @see #mierkaVýplne(double)
		 * @see #mierkaVýplne(double, double)
		 * @see #otočenieVýplne(double)
		 * @see #otočenieVýplne(double, double, double)
		 * @see GRobot#vyplňTvar(Shape, String)
		 * @see GRobot#vyplňTvar(Shape, Image)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vyplňOblasť(Area, Image)
		 * @see Plátno#vyplň(String)
		 * @see Plátno#vyplň(Image)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#vyplň(Shape, Image)
		 * @see Obrázok#vyplň(String)
		 * @see Obrázok#vyplň(Image)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(Image)
		 */
		public static void stredOtáčaniaVýplne(double sx, double sy)
		{
			stredOtáčaniaVýplneX = sx;
			stredOtáčaniaVýplneY = sy;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaVýplne(double, double) stredOtáčaniaVýplne}.</p> */
		public static void stredOtacaniaVyplne(double sx, double sy)
		{
			stredOtáčaniaVýplne(sx, sy);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #stredOtáčaniaVýplne(double, double)
		 * stredOtáčaniaVýplne(sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania
		 * prijíma objekt, ktorého polohu použije v relatívnom kontexte
		 * namiesto priameho zadania hodnôt súradníc (čiže absolútne
		 * súradnice polohy objektu použije priamo ako relatívne hodnoty –
		 * nie je vykonaný žiadny prepočet). Inak funguje táto metóda úplne
		 * rovnako ako spomenutá metóda. Všetky informácie uvedené v jej
		 * opise sú tiež relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param poloha objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #stredOtáčaniaVýplne(double, double)
		 */
		public static void stredOtáčaniaVýplne(Poloha poloha)
		{
			stredOtáčaniaVýplneX = poloha.polohaX();
			stredOtáčaniaVýplneY = poloha.polohaY();
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaVýplne(Poloha) stredOtáčaniaVýplne}.</p> */
		public static void stredOtacaniaVyplne(Poloha poloha)
		{
			stredOtáčaniaVýplne(poloha);
		}

		/**
		 * <p>Táto metóda funguje podobne ako metóda
		 * {@link #stredOtáčaniaVýplne(double, double)
		 * stredOtáčaniaVýplne(sx, sy)}. Rozdielom je, že namiesto dvoch
		 * samostatných súradníc určujúcich vysunutie stredu otáčania
		 * prijíma objekt, ktorého polohu použije v relatívnom kontexte
		 * namiesto priameho zadania hodnôt súradníc (čiže absolútne
		 * súradnice polohy objektu použije priamo ako relatívne hodnoty –
		 * nie je vykonaný žiadny prepočet). Inak funguje táto metóda úplne
		 * rovnako ako spomenutá metóda. Všetky informácie uvedené v jej
		 * opise sú tiež relevantné, preto odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @param tvar objekt určujúci nové relatívne posunutie
		 *     stredu otáčania výplní
		 * 
		 * @see #stredOtáčaniaVýplne(double, double)
		 */
		public static void stredOtáčaniaVýplne(Shape tvar)
		{
			Rectangle2D hranice = tvar.getBounds2D();
			stredOtáčaniaVýplneX = prepočítajSpäťX(hranice.getX()) +
				hranice.getWidth() / 2;
			stredOtáčaniaVýplneY = prepočítajSpäťY(hranice.getY()) -
				hranice.getHeight() / 2;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaVýplne(Shape) stredOtáčaniaVýplne}.</p> */
		public static void stredOtacaniaVyplne(Shape tvar)
		{
			stredOtáčaniaVýplne(tvar);
		}


		/**
		 * <p>Táto metóda číta súradnice myši, ktoré použije na posunutie
		 * počiatku obrázkových výplní. Funguje tak, ako keby sme do metódy
		 * {@link #posunutieVýplne(double, double) posunutieVýplne(x, y)}
		 * zadali súradnice myši namiesto parametrov. Informácie nachádzajúce
		 * sa v opise uvedenej metódy sú relevantné aj v prípade tejto
		 * metódy, preto odporúčame prečítať si ho.</p>
		 * 
		 * @see #posunutieVýplne(double, double)
		 */
		public static void posunutieVýplneNaMyš()
		{
			posunutieVýplneX = ÚdajeUdalostí.súradnicaMyšiX;
			posunutieVýplneY = ÚdajeUdalostí.súradnicaMyšiY;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #posunutieVýplneNaMyš() posunutieVýplneNaMyš}.</p> */
		public static void posunutieVyplneNaMys()
		{
			posunutieVýplneNaMyš();
		}


		/**
		 * <p>Táto metóda použije súradnice myši na vypočítanie nového
		 * pootočenia obrázkových výplní. Najprv prevezme súradnice myši,
		 * vypočíta z nich uhol smerom k aktuálnemu stredu otáčania
		 * obrázkových výplní a uhol použije na nastavenie nového pootočenia.</p>
		 * 
		 * <p>Metóda funguje tak, ako keby sme vypočítaný uhol zadali ako
		 * parameter do metódy {@link #otočenieVýplne(double)
		 * otočenieVýplne(uhol)}. Informácie nachádzajúce sa v opise
		 * uvedenej metódy sú relevantné aj pre túto metódu, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @see #otočenieVýplne(double)
		 */
		public static void otočenieVýplneNaMyš()
		{
			double Δx = ÚdajeUdalostí.súradnicaMyšiX -
				posunutieVýplneX - stredOtáčaniaVýplneX;
			double Δy = ÚdajeUdalostí.súradnicaMyšiY -
				posunutieVýplneY - stredOtáčaniaVýplneY;

			if (Δx != 0 || Δy != 0)
			{
				otočVýplňΑ = Math.toDegrees(Math.atan2(Δy, Δx));
				if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočenieVýplneNaMyš() otočenieVýplneNaMyš}.</p> */
		public static void otocenieVyplneNaMys()
		{
			otočenieVýplneNaMyš();
		}

		/**
		 * <p>Táto metóda použije súradnice myši na vypočítanie nového
		 * pootočenia obrázkových výplní. Najprv prevezme súradnice myši,
		 * vypočíta z nich uhol smerom k aktuálnemu stredu otáčania
		 * obrázkových výplní a uhol použije na nastavenie nového pootočenia.</p>
		 * 
		 * <p>Metóda funguje tak, ako keby sme vypočítaný uhol zadali ako
		 * parameter do metódy {@link #pootočenieVýplne(double)
		 * pootočenieVýplne(uhol)}. Informácie nachádzajúce sa v opise
		 * uvedenej metódy sú relevantné aj pre túto metódu, preto
		 * odporúčame prečítať si aj jej opis.</p>
		 * 
		 * @see #pootočenieVýplne(double)
		 */
		public static void pootočenieVýplneNaMyš()
		{
			double Δx = ÚdajeUdalostí.súradnicaMyšiX -
				posunutieVýplneX - stredOtáčaniaVýplneX;
			double Δy = ÚdajeUdalostí.súradnicaMyšiY -
				posunutieVýplneY - stredOtáčaniaVýplneY;

			if (Δx != 0 || Δy != 0)
			{
				otočVýplňΑ = Math.toDegrees(Math.atan2(Δy, Δx));
				if (otočVýplňΑ < 0) otočVýplňΑ += 360;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pootočenieVýplneNaMyš() pootočenieVýplneNaMyš}.</p> */
		public static void pootocenieVyplneNaMys()
		{
			pootočenieVýplneNaMyš();
		}


		/**
		 * <p>Táto metóda číta súradnice myši, ktoré použije na nastavenie
		 * nového relatívneho stredu otáčania obrázkových výplní.
		 * Funguje tak, ako keby sme do parametrov metódy
		 * {@link #stredOtáčaniaVýplne(double, double)
		 * stredOtáčaniaVýplne(sx, sy)} zadali rozdiel súradníc myši
		 * a aktuálnych hodnôt posunutia textúr. Informácie nachádzajúce sa
		 * v opise dotknutej metódy sú relevantné aj v prípade tejto metódy,
		 * preto odporúčame prečítať si ho.</p>
		 * 
		 * @see #stredOtáčaniaVýplne(double, double)
		 */
		public static void stredOtáčaniaVýplneNaMyš()
		{
			stredOtáčaniaVýplneX = ÚdajeUdalostí.súradnicaMyšiX - posunutieVýplneX;
			stredOtáčaniaVýplneY = ÚdajeUdalostí.súradnicaMyšiY - posunutieVýplneY;
			prepočítajParametreVýplne();
		}

		/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaVýplneNaMyš() stredOtáčaniaVýplneNaMyš}.</p> */
		public static void stredOtacaniaVyplneNaMys()
		{
			stredOtáčaniaVýplneNaMyš();
		}


		// Kreslenie tvarov

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru na podlahu.
		 * Metóda potrebuje na svoje správne fungovanie robota „kresliča“,
		 * ktorého farbu a štýl čiary použije na kreslenie. Ak je do metódy
		 * namiesto konkrétneho kresliča zadaná hodnota {@code valnull}, tak
		 * je na získanie parametrov kreslenia použitý {@linkplain 
		 * #hlavnýRobot() hlavný robot}. Spustenie tejto metódy má rovnaký
		 * efekt, ako keby sme volali metódu
		 * {@link Plátno#kresli(Shape, GRobot) podlaha.kresli(tvar,
		 * kreslič)}.</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude
		 *     nakreslený aktuálnym štýlom čiary a farbou zadaného robota
		 *     („kresliča“)
		 * @param kreslič grafický robot, ktorého parametre budú použité
		 *     na kreslenie alebo {@code valnull}
		 * 
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape, GRobot)
		 * @see #vyplň(Shape)
		 */
		public static void kresli(Shape tvar, GRobot kreslič)
		{ GRobot.podlaha.kresli(tvar, kreslič); }

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov na podlahu.
		 * Metóda potrebuje na svoje správne fungovanie robota „kresliča“,
		 * ktorého farbu použije na vyplnenie zadaného tvaru. Ak je do metódy
		 * namiesto konkrétneho kresliča zadaná hodnota {@code valnull}, tak
		 * je na získanie parametrov kreslenia použitý {@linkplain 
		 * #hlavnýRobot() hlavný robot}. Spustenie tejto metódy má rovnaký
		 * efekt, ako keby sme volali metódu
		 * {@link Plátno#kresli(Shape, GRobot) podlaha.vyplň(tvar,
		 * kreslič)}.</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     aktuálnou farbou zadaného robota („kresliča“)
		 * @param kreslič grafický robot, ktorého parametre budú použité
		 *     na kreslenie alebo {@code valnull}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape)
		 */
		public static void vyplň(Shape tvar, GRobot kreslič)
		{ GRobot.podlaha.vyplň(tvar, kreslič); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape, GRobot) vyplň}.</p> */
		public static void vypln(Shape tvar, GRobot kreslič)
		{ GRobot.podlaha.vyplň(tvar, kreslič); }

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru na podlahu.
		 * Metóda používa na získanie parametrov kreslenia (štýlu a farby
		 * čiary) {@linkplain #hlavnýRobot() hlavného robota}. Spustenie
		 * tejto metódy má rovnaký efekt, ako keby sme volali metódu
		 * {@link Plátno#kresli(Shape) podlaha.kresli(tvar)}.</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude
		 *     nakreslený aktuálnym štýlom čiary a farbou {@linkplain 
		 *     #hlavnýRobot() hlavného robota}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #vyplň(Shape, GRobot)
		 * @see #vyplň(Shape)
		 */
		public static void kresli(Shape tvar)
		{ GRobot.podlaha.kresli(tvar); }

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov na podlahu.
		 * Metóda používa na získanie parametrov kreslenia (farby výplne)
		 * {@linkplain #hlavnýRobot() hlavného robota}. Spustenie tejto
		 * metódy má rovnaký efekt, ako keby sme volali metódu
		 * {@link Plátno#kresli(Shape) podlaha.vyplň(tvar)}.</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     farbou {@linkplain #hlavnýRobot() hlavného robota}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape, GRobot)
		 */
		public static void vyplň(Shape tvar) { GRobot.podlaha.vyplň(tvar); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape) vyplň}.</p> */
		public static void vypln(Shape tvar) { GRobot.podlaha.vyplň(tvar); }


		// Priehľadnosť plátien

		/**
		 * <p>Naraz nastaví {@linkplain Plátno#priehľadnosť(double)
		 * priehľadnosť} pre podlahu aj strop.</p>
		 * 
		 * @param prePodlahu nová úroveň (ne)priehľadnosti pre podlahu
		 *     (0.0 – 1.0)
		 * @param preStrop nová úroveň priehľadnosti pre strop (0.0 – 1.0)
		 * 
		 * @see #upravPriehľadnosť(double, double)
		 * @see Plátno#priehľadnosť(double)
		 * @see Plátno#priehľadnosť()
		 * @see Plátno#upravPriehľadnosť(double)
		 */
		public static void priehľadnosť(double prePodlahu, double preStrop)
		{
			GRobot.podlaha.priehľadnosť(prePodlahu);
			GRobot.strop.priehľadnosť(preStrop);
		}

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť(double, double) priehľadnosť}.</p> */
		public static void priehladnost(double prePodlahu, double preStrop)
		{ priehľadnosť(prePodlahu, preStrop); }

		/**
		 * <p>Naraz upraví {@linkplain Plátno#upravPriehľadnosť(double) úroveň
		 * priehľadnosti} pre podlahu aj strop. Parametre sú koeficienty,
		 * ktorými bude násobená aktuálna úroveň priehľadnosti stanoveného
		 * plátna – ak napríklad zadáme {@link Svet Svet}{@code .}{@code 
		 * currupravPriehľadnosť}{@code (}{@code num0.5}{@code 
		 * , }{@code num2}{@code )}, tak úroveň priehľadnosti podlahy klesne
		 * na polovicu a stropu stúpne dvojnásobne.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>V tomto príklade použijeme úpravu priehľadnosti plátien na
		 * vytvorenie prechodového efektu (<small>pozri zoznam zmien: <a
		 * href="zoznam-zmien.html">poďakovanie</a> uvedené pri
		 * verzii 1.35</small>):</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Najskôr zaplníme plátno a strop kresbou:}

			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num10.0});
			{@link GRobot#skry() skry}();

			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num2}; ++i)
			{
				{@link GRobot#farba(int, int, int) farba}(({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
					({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
					({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}));
				{@link GRobot#vyplň() vyplň}();

				{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num22}; ++j)
				{
					{@link GRobot#smer(double) smer}({@code num0});
					{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
					{@link GRobot#skoč(double, double) skoč}({@code num0}, &#45;{@code num113.5});

					{@code kwdfor} ({@code typeint} k = {@code num0}; k &lt; {@code num24}; ++k)
					{
						{@link GRobot#farba(int, int, int) farba}(({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
							({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
							({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}));
						{@link GRobot#doľava(double) doľava}({@code num15});
						{@link GRobot#dopredu(double) dopredu}({@code num30});
					}
				}

				{@link Plátno podlaha}.{@link Plátno#priehľadnosť(double) priehľadnosť}({@code num0.0});
				{@link GRobot#kresliNaStrop() kresliNaStrop}();
			}

			{@code comm// Definujeme obsluhu udalostí (z dôvodu obsluhy časovača):}

			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
				{
					{@link Svet Svet}.{@code currupravPriehľadnosť}({@code num1.1}, {@code num0.9});
					{@code kwdif} ({@link Plátno strop}.{@link Plátno#priehľadnosť() priehľadnosť}() &lt;= {@code num0.03})
					{
						{@link Svet Svet}.{@link Svet#pípni() pípni}();
						{@link Svet Svet}.{@link Svet#zastavČasovač() zastavČasovač}();
						{@link Svet Svet}.{@link Svet#priehľadnosť(double, double) priehľadnosť}({@code num1.0}, {@code num0.0});
					}
				}
			};

			{@code comm// A spustíme časovač:}

			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});
			{@link Svet Svet}.{@link Svet#spustiČasovač(double) spustiČasovač}({@code num0.1});
			{@link Svet Svet}.{@link Svet#pípni() pípni}();
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered">
		 * <tr><td
		 * style="vertical-align:top"><p><image>upravPriehladnost1.png<alt/></image>
		 * Prvý (náhodne) generovaný obrázok<br /><small>(plátno na obrázku
		 * je zmenšené)</small>.</p></td><td> </td>
		 * <td
		 * style="vertical-align:top"><p><image>upravPriehladnost2.png<alt/></image>
		 * Fáza prelivu obrázkov pomocou priehľadnosti<br />(zhruba
		 * v polovici procesu)<br /><small>(plátno na obrázku je
		 * zmenšené)</small>.</p></td><td> </td>
		 * <td
		 * style="vertical-align:top"><p><image>upravPriehladnost3.png<alt/></image>
		 * Druhý (náhodne) generovaný obrázok<br /><small>(plátno na obrázku
		 * je zmenšené)</small>.</p></td></tr>
		 * </table>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by sme chceli vytvoriť plynulý
		 * prechod priehľadnosti medzi dvomi kresbami pokrývajúcimi celé
		 * plátna, tak by bolo lepšie, keby sme postupovali tak, že by sme
		 * na začiatku nastavili obom plátnam úplnú nepriehľadnosť, spodnému
		 * plátnu by sme ponechali nepriehľadnosť nezmenenú a nepriehľadnosť
		 * horného plátna by sme plynule znižovali. Avšak pre nepravidelné
		 * kresby alebo rôzne veľkosti obrázkov by tento postup nefugoval.
		 * Podobne by sme s týmto prístupom neuspeli, keby sme chceli, aby
		 * bolo počas prelivu vidno určité (napríklad vzorkované) pozadie,
		 * ktoré by nebolo súčasťou ani jednej z kresieb.</p>
		 * 
		 * @param prePodlahu koeficient zmeny úrovne priehľadnosti podlahy
		 * @param preStrop koeficient zmeny úrovne priehľadnosti stropu
		 * 
		 * @see #priehľadnosť(double, double)
		 * @see Plátno#upravPriehľadnosť(double)
		 * @see Plátno#priehľadnosť(double)
		 * @see Plátno#priehľadnosť()
		 * @see Plátno#upravPriehľadnosť(double)
		 */
		public static void upravPriehľadnosť(double prePodlahu, double preStrop)
		{
			GRobot.podlaha.upravPriehľadnosť(prePodlahu);
			GRobot.strop.upravPriehľadnosť(preStrop);
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravPriehľadnosť(double, double) upravPriehľadnosť}.</p> */
		public static void upravPriehladnost(double prePodlahu, double preStrop)
		{ upravPriehľadnosť(prePodlahu, preStrop); }


		// Automatické prekresľovanie

		/**
		 * <p>Overí, či je automatické prekresľovanie sveta zapnuté. Vypnutie
		 * automatického prekresľovania zabezpečuje metóda {@link #nekresli()
		 * nekresli}, zapnutie zase metóda {@link #kresli() kresli}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolená implementácia
		 * automatického prekresľovania sveta nie je efektívna (ale má
		 * svoje príčiny). Pozrite si komentáre a príklady v opisoch metód
		 * {@link #prekresli() prekresli} a {@link #nebolPrekreslený()
		 * nebolPrekreslený}.</p>
		 * 
		 * @return {@code valtrue} ak je automatické prekresľovanie zapnuté,
		 *     {@code valfalse} ak je vypnuté
		 * 
		 * @see #nebolPrekreslený()
		 * @see #nekresli()
		 * @see #kresli()
		 * @see #prekresli()
		 * @see #žiadajPrekreslenie()
		 */
		public static boolean kreslenie() { return !nekresli; }

		/**
		 * <p>Zistí stav príznaku zamietnutej požiadavky na automatické
		 * prekreslenie. Ak je hodnota tohto príznaku rovná {@code valtrue},
		 * tak je pravdepodobné, že obsah okna na obrazovke nekorešponduje
		 * so skutočnými obsahmi pozadia a plátien (prípadne ďalších
		 * komponentov).</p>
		 * 
		 * <p>Táto metóda súvisí s vypnutím predvolenej implementácie
		 * automatického prekresľovania, ktoré i tak nie je efektívne.
		 * Nasledujúci príklad ukazuje lepší spôsob implementácie
		 * automatického (resp. pravidelného) prekresľovania. Ide
		 * o pravidelnú kontrolu nevyhnutnoti prekresľovanie sveta
		 * s využitím tejto metódy a prekreslenie v prípade potreby.
		 * (Ide vlastne o implementáciu prekresľovania v jednotkách snímok
		 * za sekundu – frames per second – FPS.)</p>
		 * 
		 * <p>Tento spôsob prekresľovania je v súvislosti s počítačovou
		 * grafikou ideálny, ale nemohol byť implementovaný v rámci
		 * programovacieho rámca predvolene, pretože by vyžadoval implicitné
		 * zapnutie časovača so stanovením predvoleného času obnovovania (čomu
		 * sa autor programovaceho rámca chcel vyhnúť). Pri implementácii
		 * programovacieho rámca bolo cieľom:</p>
		 * 
		 * <ol>
		 * <li>ponechať čo najviac možností slobodnej voľby na
		 * programátorovi (automatické zapnutie časovača by bolo považované
		 * za rozhodnutie nad rámec kompetencií programovacieho rámca, pretože
		 * časovač je jedným z obmedzovaných systémových zdrojov – nie je
		 * možné vytvoriť nekonečné množstvo časovačov a časovače ovplyvňujú
		 * činnosť celého systému)</li>
		 * <li>a zároveň minimalizácia objemu vstupných poznatkov
		 * pre začiatočníkov (zvolený prístup nevyžaduje od začiatočníka, aby
		 * sa najskôr podrobne oboznámil s nevyhnutnosťou prekresľovania
		 * a s možnosťami jeho implementácie – jednoducho si sadne k vhodne
		 * zvolenému a nakonfigurovanému programovaciemu prostrediu a začne
		 * používať príkazy grafického robota).</li>
		 * </ol>
		 * 
		 * <p>To viedlo k predvolenej implementácii automatického
		 * prekresľovania v súčasnej podobe, čiže po každej zmene plátna.
		 * Voľba tohto prístupu bola pri vzatí do úvahy uvedených cieľov
		 * najlepšou možnosťou. Tento <em>nevýhodný</em> spôsob vykresľovania
		 * <em>sa dá vypnúť</em> a nahradiť iným lepším spôsobom, napríklad
		 * tým, ktorý ukazuje nasledujúci príklad. Vďaka tomu môže učiteľ
		 * študentom ukázať výhody a nevýhody rôznych prístupov a vysvetliť
		 * dôvody ich použitia.</p>
		 * 
		 * <p class="caution"><b>Dôležité upozornenie!</b> V rámci používania
		 * programovacieho rámca môžu nastať určité špeciálne situácie, kedy nie
		 * je automaticky rozpoznaná potreba prekreslenia, napríklad pri použití
		 * vlastných metód obrázka na jeho úpravu, pričom obrázok je nastavený
		 * ako vlastný tvar niektorému z robotov. <b>Nepoužívajte</b> v takom
		 * prípade príkaz {@link Svet#prekresli() Svet.prekresli()}!
		 * Na tento účel je rezervovaný príkaz
		 * <b>{@link Svet#žiadajPrekreslenie() Svet.žiadajPrekreslenie()}</b>.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestPrekresľovania {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} TestPrekresľovania()
				{
					{@code valsuper}({@code num300}, {@code num200}); {@code comm// nadradený konštruktor upraví veľkosť plátna}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}(); {@code comm// vypne predvolený spôsob automatického prekresľovania}

					{@code comm// V nasledujúcej obsluje udalostí (v reakcii tik) bude zabezpečené}
					{@code comm// pravidelné prekresľovanie:}
					{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
					{
						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
						{
							{@code comm// Ak je treba, tak prekresli svet…}
							{@code kwdif} ({@link Svet Svet}.{@code currneboloPrekreslené}()) {@link Svet Svet}.{@link #prekresli() prekresli}();
						}

						{@code comm// …}
						{@code comm// Obsluha prípadných ďalších udalostí…}
						{@code comm// …}
					};

					{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();

					{@code comm// Príklad časovo náročnej činnosti (pri zapnutom pôvodnom}
					{@code comm// automatickom prekresľovaní by bola časovo ešte náročnejšia, pretože}
					{@code comm// prekreslenie by nastalo po každom príkaze v tele cyklu – dokončenie}
					{@code comm// vykonávania cyklu by trvalo možno aj niekoľko minút; takto sa celý}
					{@code comm// príklad vykoná za necelú sekundu…):}
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num1000000}; ++i)
					{
						{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
						{@link GRobot#bod() bod}();
					}
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@code comm// Príkaz „Svet.nekresli()“ môže byť prípadne uvedený aj tu…}
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"test-prekresľovania.cfg"});
					{@code kwdnew} TestPrekresľovania();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>testPrekreslovania1.png<alt/></image></td>
		 * <td><image>testPrekreslovania2.png<alt/></image></td>
		 * <td><image>testPrekreslovania3.png<alt/></image></td>
		 * <td><image>testPrekreslovania4.png<alt/></image></td></tr><tr>
		 * <td colspan="4"><p class="image">Niekoľko obrázkov zobrazujúcich
		 * priebeh zapĺňania plátna bodmi počas vykonávania tohto
		 * príkladu.</p></td></tr></table>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj komentáre a príklad
		 * v opise metódy {@link #prekresli() prekresli}.</p>
		 * 
		 * @return {@code valtrue} ak vznikla požiadavka na automatické
		 *     prekreslenie, zapnuté, no bola zamietnutá (napríklad
		 *     v dôsledku vypnutia automatického prekresľovania – pozri aj:
		 *     {@link #nekresli() nekresli}); {@code valfalse} v opačnom
		 *     prípade
		 * 
		 * @see #kreslenie()
		 * @see #nekresli()
		 * @see #kresli()
		 * @see #prekresli()
		 * @see #žiadajPrekreslenie()
		 */
		public static boolean nebolPrekreslený() { return neboloPrekreslené; }

		/** <p><a class="alias"></a> Alias pre {@link #nebolPrekreslený() nebolPrekreslený}.</p> */
		public static boolean nebolPrekresleny() { return neboloPrekreslené; }

		/** <p><a class="alias"></a> Alias pre {@link #nebolPrekreslený() nebolPrekreslený}.</p> */
		public static boolean neboloPrekreslené() { return neboloPrekreslené; }

		/** <p><a class="alias"></a> Alias pre {@link #nebolPrekreslený() nebolPrekreslený}.</p> */
		public static boolean neboloPrekreslene() { return neboloPrekreslené; }

		/**
		 * <p>Vypne predvolenú implementáciu automatického prekresľovanie
		 * sveta. Tá funguje tak, že prekresľovanie sveta je automaticky
		 * vykonávané po rôznych aktivitách robotov (zmena polohy,
		 * orientácie a podobne), pri zmene niektorých parametrov sveta,
		 * prípadne pri inej relevantnej príležitosti (to jest vždy, keď je
		 * pravdepodobné, že sa niečo na plátnach sveta vizuálne zmenilo).</p>
		 * 
		 * <p>Vypnutie automatického prekresľovania môže značne zvýšiť
		 * grafickú efektívnosť aplikácie, aby sme však po jeho vypnutí
		 * mohli vidieť výsledok kreslenia, musíme vždy (po akejkoľvek zmene,
		 * ktorá by sa mohla prejaviť graficky) použiť metódu {@link 
		 * #prekresli() prekresli} (napríklad pravidelne po určitom počte
		 * vykonaných príkazov, alebo v pravidelných časových intervaloch,
		 * prípadne po overení potreby prekreslenia sveta metódou
		 * {@link #nebolPrekreslený() nebolPrekreslený}). Túto predvolenú
		 * implementáciu automatického prekresľovania sveta opätovne
		 * zapneme metódou {@link #kresli() kresli}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolená implementácia
		 * automatického prekresľovania sveta nie je efektívna, i keď má
		 * svoje príčiny. Pozrite si komentáre a príklady v opisoch metód
		 * {@link #prekresli() prekresli} a {@link #nebolPrekreslený()
		 * nebolPrekreslený}.</p>
		 * 
		 * @see #kreslenie()
		 * @see #nebolPrekreslený()
		 * @see #kresli()
		 * @see #prekresli()
		 * @see #žiadajPrekreslenie()
		 */
		public static void nekresli() { nekresli = true; }

		/**
		 * <p>Zapne automatické prekresľovanie sveta, ktoré bolo vypnuté metódou
		 * {@link #nekresli() nekresli}. Ak nechceme prekresľovanie zapnúť,
		 * ale chceme napríklad vidieť, čo medzičasom nakreslil niektorý
		 * robot, môžeme použiť metódu {@link #prekresli() prekresli}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolená implementácia
		 * automatického prekresľovania sveta nie je efektívna (ale má
		 * svoje príčiny). Pozrite si komentáre a príklady v opisoch metód
		 * {@link #prekresli() prekresli} a {@link #nebolPrekreslený()
		 * nebolPrekreslený}.</p>
		 * 
		 * @see #kreslenie()
		 * @see #nebolPrekreslený()
		 * @see #nekresli()
		 * @see #prekresli()
		 * @see #žiadajPrekreslenie()
		 */
		public static void kresli()
		{
			nekresli = false;
			prekresli();
		}

		/**
		 * <p>Jednorazovo prekreslí obsah sveta (bez ohľadu na použitie metódy
		 * {@link #nekresli() nekresli}).</p>
		 * 
		 * <p>To znamená, že metóda prekreslí obsah sveta bez ohľadu na
		 * nastavenie automatického prekresľovania (pozri napríklad metódu
		 * {@link #nekresli() nekresli}). Metóda spustí rovnaký proces,
		 * aký je vykonávaný pri automatickom prekresľovaní. V prípade, že
		 * je automatické prekresľovanie sveta vypnuté (metódou {@link 
		 * #nekresli() nekresli}), nie je prekreslenie vykonané bez vedomia
		 * programátora, okrem prípadu, keď sú splnené nasledujúce podmienky:</p>
		 * 
		 * <ol>
		 * <li>bola aspoň raz použitá niektorá z metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}<br />
		 * a súčasne</li>
		 * <li>sa zmenila veľkosť hlavného okna (buď aktivitou
		 * používateľa, alebo z iných príčin)<br />
		 * a súčasne</li>
		 * <li>nie je aktívna obsluha udalostí (pozri {@link 
		 * ObsluhaUdalostí#zmenaVeľkostiOkna()
		 * ObsluhaUdalostí.zmenaVeľkostiOkna()}).</li>
		 * </ol>
		 * 
		 * <p>Vypnutie automatického prekresľovania môže značne zvýšiť
		 * grafickú efektívnosť aplikácie.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>V tomto príklade najskôr vypneme kreslenie a začneme kresliť
		 * vymyslenú mapu kruhového sveta… Svet bude mať dvadsať kontinentov.
		 * Po nakreslení každého z nich použijeme metódu {@code 
		 * currprekresli} na ukázanie čiastočného (a nakoniec konečného)
		 * výsledku (<small>pozri zoznam zmien: <a
		 * href="zoznam-zmien.html">poďakovanie</a> uvedené pri
		 * verzii 1.35</small>):</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// V tomto príklade nakreslíme „Novú Zem“:}

			{@link Svet Svet}.{@link Svet#farbaPozadia(Color) farbaPozadia}({@link Farebnosť#modrá modrá});
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num1.5});

			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num20}; ++i)
			{
				{@code comm// Pevninu rozbijeme na viacero kontinentov (kolízie nebudeme}
				{@code comm// kontrolovať, dovolíme, aby sa prekrývali). Každý kontinent}
				{@code comm// začneme na náhodnej pozícii:}
				{@link GRobot#náhodnáPoloha() náhodnáPoloha}();

				{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num25000}; ++j)
				{
					{@code comm// Tieto dva príkazy sa postarajú o kreslenie pevniny}
					{@code comm// (náhodným spôsobom):}
					{@link GRobot#náhodnýSmer() náhodnýSmer}();
					{@link GRobot#dopredu(double) dopredu}({@code num1.5});

					{@code comm// Nasledujúci výpočet a podmienka sa postarajú o vznik}
					{@code comm// červených „kruhov“ (tenkých obručí) na ploche pevniny:}
					{@code typedouble} výpočet = {@link GRobot#vzdialenosť() vzdialenosť}() / {@code num50} &#45; {@link GRobot#vzdialenosť() vzdialenosť}() % {@code num50};

					{@code kwdif} ((výpočet % {@code num2}) &gt;= {@code num1})
						{@link GRobot#farba(Color) farba}({@link Farebnosť#červená červená});
					{@code kwdelse}
						{@link GRobot#farba(Color) farba}({@link Farebnosť#zelená zelená});
				}

				{@link Svet Svet}.{@code currprekresli}();
			}

			{@link Svet Svet}.{@link Svet#kresli() kresli}();
			</pre>
		 * 
		 * <p>Nová Zem sa bude vykresľovať po fázach, výsledok bude náhodný
		 * a môže vyzerať napríklad takto:</p>
		 * 
		 * <p><image>novaZem-resized.png<alt/>Náhodne vygenerovaná Nová
		 * Zem.</image></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda súvisí s vypnutím
		 * predvolenej implementácie automatického prekresľovania, ktoré i tak
		 * nie je efektívne. Pozrite si aj komentáre a príklad v opise metódy
		 * {@link #nebolPrekreslený() nebolPrekreslený}.</p>
		 * 
		 * @see #kreslenie()
		 * @see #nebolPrekreslený()
		 * @see #nekresli()
		 * @see #kresli()
		 * @see #žiadajPrekreslenie()
		 */
		public static void prekresli()
		{
			if (pracujem)
			{
				žiadamPrekresleniePoPráci = true;
				return;
			}

			if (právePrekresľujem) return;
			právePrekresľujem = true;

			synchronized (zámokPrekresľovania) {

			// V súvislosti s týmto miestom vzniklo viacero úvah. Postupne
			// boli zamietnuté. Napríklad, aby tu bol implementovaný
			// mechanizmus, ktorý by zabraňoval príliš častému automatickéhu
			// prekresľovaniu, alebo aby bol mechanizmus automatického
			// prekresľovania s pomocou časovača predvolene implicitne
			// implementovaný. Žiadny spôsob by nebol ideálny alebo
			// dostatočne „efektívny“. Keby sa zabraňovalo „príliš častému
			// automatickému prekresľovaniu,“ tak by buď vznikali situácie,
			// kedy by posledné prekreslenie vôbec nenastalo, alebo by sa
			// niekde hromadili správy požadujúce neskoršie prekreslenie.
			// (To by nebolo dobré ani tak, ani tak.) Ak by bol implicitne
			// implementovaný mechanizmus automatického prekresľovania
			// časovačom, tak by to jednak zväzovalo ruky tvorivým
			// programátorom a jednak by to vyžadovalo automatické zapnutie
			// časovača aj pri produktoch, ktoré časovač nepotrebujú vôbec.
			// (Čo tiež nepovažujem za ideálne.)

			boolean chyba = false;

			try
			{
				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.prekreslenie();
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							počúvajúci.prekreslenie();
						}
					}

				grafikaSveta2.setColor(farbaPozadia);
				grafikaSveta2.fillRect(0, 0,
					Plátno.šírkaPlátna, Plátno.výškaPlátna);

				if (!GRobot.podlaha.prekresli(grafikaSveta2)) chyba = true;

				// Zobraz vzájomné spojnice všetkých viditeľných robotov
				for (GRobot tento : GRobot.zoznamRobotov)
				{
					if (tento.viditeľný)
						tento.kresliSpojnice(obrázokSveta2,
							grafikaSveta2);
				}

				// Zobraz všetkých viditeľných robotov vo vrstvách
				for (Map.Entry<Integer, GRobot.Vrstva> záznamVrstvy :
					GRobot.zoznamVrstiev.entrySet())
				{
					GRobot.Vrstva vrstva = záznamVrstvy.getValue();
					for (Map.Entry<Integer, GRobot>
						záznamRobota : vrstva.entrySet())
					{
						GRobot tento = záznamRobota.getValue();
						if (tento.viditeľný)
							tento.kresliRobota(obrázokSveta2,
								grafikaSveta2);
					}
				}

				// Zobraz pôsobiská všetkých robotov
				for (GRobot tento : GRobot.zoznamRobotov)
				{
					if (tento.kresliPôsobisko)
						tento.kresliPôsobisko(grafikaSveta2);
				}

				if (!GRobot.strop.prekresli(grafikaSveta2)) chyba = true;

				if (null == vlnenie)
					grafikaSveta1.drawImage(obrázokSveta2, 0, 0, null);
				else
					grafikaSveta1.drawImage(vlnenie.zvlnenýRaster(), 0, 0, null);

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.dokreslenie();
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							počúvajúci.dokreslenie();
						}
					}

				if (null == oknoCelejObrazovky)
					svet.repaint();
				else
					oknoCelejObrazovky.repaint();
			}
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); chyba = true; }
			finally
			{
				právePrekresľujem = false;
				neboloPrekreslené = chyba;
			}
			} // zámokPrekresľovania
		}

		/**
		 * <p>Nastaví vnútorný príznak (stav) prekreslenia sveta tak, aby metóda
		 * {@link #nebolPrekreslený() nebolPrekreslený} vrátila hodnotu
		 * {@code valtrue}. Táto metóda nedokáže zabezpečiť prekreslenie sveta.
		 * Jej účelom je len nastaviť uvedený príznak. Využitie nájde
		 * v prípadoch, kedy je vypnuté automatické prekresľovanie sveta
		 * a čas (interval) prekresľovania určuje aplikácia. (Pozri aj
		 * upozornenie v opise metódy {@link #nebolPrekreslený()
		 * nebolPrekreslený}.)</p>
		 * 
		 * @see #kreslenie()
		 * @see #nebolPrekreslený()
		 * @see #nekresli()
		 * @see #kresli()
		 * @see #prekresli()
		 */
		public static void žiadajPrekreslenie() { neboloPrekreslené = true; }

		/** <p><a class="alias"></a> Alias pre {@link #žiadajPrekreslenie() žiadajPrekreslenie}.</p> */
		public static void ziadajPrekreslenie() { neboloPrekreslené = true; }


		// Hlavná ponuka

		/**
		 * <p>Vymaže všetky položky hlavnej ponuky (vrátane predvolených). Metóda
		 * automaticky skryje hlavnú ponuku. Viditeľnosť ponuky je automaticky
		 * obnovená po začatí pridávania položiek.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuHlavnejPonuky(String)
		 * @see #pridajPoložkuHlavnejPonuky(String, int)
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajOddeľovačPonuky()
		 */
		public static void vymažPonuku()
		{
			if (!inicializované) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			for (;;)
			{
				JMenu položkaHlavnejPonuky =
					hlavnáPonuka.getMenu(aktuálnaPonuka);

				if (null != položkaHlavnejPonuky)
				{
					ponukaVPôvodnomStave = false;
					položkaHlavnejPonuky.removeAll();
				}

				if (aktuálnaPonuka > 0)
				{
					hlavnáPonuka.remove(aktuálnaPonuka);
					--aktuálnaPonuka;
				}
				else break;
			}
			aktuálnaPoložka = 0;
			hlavnáPonuka.setVisible(false);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPonuku() vymažPonuku}.</p> */
		public static void vymazPonuku() { vymažPonuku(); }

		/**
		 * <p>Rozšíri hlavnú ponuku o ďalšiu položku alebo prepíše aktuálnu
		 * položku hlavnej ponuky. Aktuálna hlavná položka je prepísaná
		 * v prípade, že zatiaľ neobsauje žiadne podpoložky (pozri {@link 
		 * #pridajPoložkuPonuky(String) pridajPoložkuPonuky} alebo jej
		 * „klony“).</p>
		 * 
		 * @param text reťazec textu pridávanej položky
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuHlavnejPonuky(String, int)
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajOddeľovačPonuky()
		 */
		public static void pridajPoložkuHlavnejPonuky(String text)
		{
			if (!inicializované) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			JMenu položkaHlavnejPonuky = hlavnáPonuka.getMenu(aktuálnaPonuka);
			hlavnáPonuka.setVisible(true);

			if (0 == položkaHlavnejPonuky.getItemCount())
			{
				položkaHlavnejPonuky.setText(text);
			}
			else
			{
				ponukaVPôvodnomStave = false;
				položkaHlavnejPonuky = new JMenu(text);
				hlavnáPonuka.add(položkaHlavnejPonuky);
				++aktuálnaPonuka; aktuálnaPoložka = 0;
			}

			// 31. 7. 2018 bol pri používaní programovacieho rámca odhalený
			// zaujímavý problém. Ak je okno sveta zobrazené, tak sa hlavná
			// ponuka počas jej vytvárania neaktualizuje… Tento problém
			// zostával dlho skrytý, pretože vo všetkých predchádzajúcich
			// prípadoch bolo okno sveta počas vytvárania ponuky
			// z optimalizačných dôvodov skryté. (Toto je aj naďalej
			// odporúčaný postup, no i napriek tomu sem bola pridaná
			// nasledujúca záplata, pretože sa tým zabráni zbytočnému
			// prekresľovaniu a prípadnému „blikaniu“.)
			if (zobrazený()) hlavnáPonuka.updateUI();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuHlavnejPonuky(String) pridajPoložkuHlavnejPonuky}.</p> */
		public static void pridajPolozkuHlavnejPonuky(String text)
		{ pridajPoložkuHlavnejPonuky(text); }

		/**
		 * <p>Rozšíri hlavnú ponuku o ďalšiu položku alebo prepíše aktuálnu
		 * položku hlavnej ponuky. Aktuálna hlavná položka je prepísaná
		 * v prípade, že zatiaľ neobsauje žiadne podpoložky (pozri {@link 
		 * #pridajPoložkuPonuky(String, int) pridajPoložkuPonuky} alebo jej
		 * „klony“). Význam parametra {@code mnemonickáSkratka} je rovnaký ako
		 * pri metóde {@link #pridajPoložkuPonuky(String, int)
		 * pridajPoložkuPonuky(text, mnemonickáSkratka)}.</p>
		 * 
		 * @param text reťazec textu pridávanej položky
		 * @param mnemonickáSkratka kód mnemonickej skratky (príklad:
		 *     {@code Kláves.VK_A})
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuHlavnejPonuky(String)
		 * @see #pridajPoložkuPonuky(String, int)
		 * @see #pridajOddeľovačPonuky()
		 */
		public static void pridajPoložkuHlavnejPonuky(String text, int mnemonickáSkratka)
		{
			if (!inicializované) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			JMenu položkaHlavnejPonuky = hlavnáPonuka.getMenu(aktuálnaPonuka);
			hlavnáPonuka.setVisible(true);

			if (0 == položkaHlavnejPonuky.getItemCount())
			{
				položkaHlavnejPonuky.setText(text);
				položkaHlavnejPonuky.setMnemonic(mnemonickáSkratka);
			}
			else
			{
				ponukaVPôvodnomStave = false;
				položkaHlavnejPonuky = new JMenu(text);
				položkaHlavnejPonuky.setMnemonic(mnemonickáSkratka);
				hlavnáPonuka.add(položkaHlavnejPonuky);
				++aktuálnaPonuka; aktuálnaPoložka = 0;
			}

			// 31. 7. 2018 bol pri používaní programovacieho rámca odhalený
			// zaujímavý problém. Ak je okno sveta zobrazené, tak sa hlavná
			// ponuka počas jej vytvárania neaktualizuje… Tento problém
			// zostával dlho skrytý, pretože vo všetkých predchádzajúcich
			// prípadoch bolo okno sveta počas vytvárania ponuky
			// z optimalizačných dôvodov skryté. (Toto je aj naďalej
			// odporúčaný postup, no i napriek tomu sem bola pridaná
			// nasledujúca záplata, pretože sa tým zabráni zbytočnému
			// prekresľovaniu a prípadnému „blikaniu“.)
			if (zobrazený()) hlavnáPonuka.updateUI();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuHlavnejPonuky(String, int) pridajPoložkuHlavnejPonuky}.</p> */
		public static void pridajPolozkuHlavnejPonuky(String text, int mnemonickáSkratka)
		{ pridajPoložkuHlavnejPonuky(text, mnemonickáSkratka); }

		/**
		 * <p>Pridá do hlavnej ponuky položku so zadaným textom. Zvolenie položky
		 * vyvolá reakciu {@link ObsluhaUdalostí#voľbaPoložkyPonuky()
		 * voľbaPoložkyPonuky} v triede obsluhy udalostí. V tej je možné
		 * položku identifikovať pomocou metódy
		 * {@link ÚdajeUdalostí#položkaPonuky() ÚdajeUdalostí.položkaPonuky}
		 * ktorá vracia objekt typu {@link PoložkaPonuky PoložkaPonuky}.
		 * Ten môžeme porovnať s hodnotou vrátenou z tejto metódy (ktorú si
		 * odložíme do pomocnej premennej) alebo použiť iný spôsob
		 * identifikácie. Napríklad metódu {@link PoložkaPonuky#aktivovaná()
		 * aktivovaná}, ktorá vráti {@code valtrue} v prípade, že bola táto
		 * položka naposledy aktivovaná.</p>
		 * 
		 * @param text reťazec textu pridávanej položky ponuky
		 * @return objekt novej položky ponuky ({@link PoložkaPonuky
		 *     PoložkaPonuky}) alebo {@code valnull} ak nastala chyba
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuPonuky(String, int)
		 * @see #pridajPoložkuPonuky(String, int, int)
		 * @see #pridajOddeľovačPonuky()
		 * @see #pridajPoložkuPonukyVymazať()
		 * @see #pridajPoložkuPonukyPrekresliť()
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static PoložkaPonuky pridajPoložkuPonuky(String text)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PoložkaPonuky(text);
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonuky(String) pridajPoložkuPonuky}.</p> */
		public static PolozkaPonuky pridajPolozkuPonuky(String text)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PolozkaPonuky(text);
		}

		/**
		 * <p>Funguje rovnako ako {@link #pridajPoložkuPonuky(String)} s tým, že
		 * definuje novej položke mnemonickú skratku (skratky, ktoré sú
		 * v položkách ponuky znázorňované podčiarknutým písmenom). Skratku je
		 * možné najjednoduchšie určiť konštantou triedy {@link 
		 * KeyEvent KeyEvent}.{@code VK_XXX} (tá je v robotovi na
		 * zjednodušenie prekrytá triedou {@link Kláves Kláves}, takže môžeme
		 * použiť napríklad {@code Kláves.VK_A}, {@code Kláves.VK_B}, {@code 
		 * Kláves.VK_C}…).</p>
		 * 
		 * @param text reťazec textu pridávanej položky ponuky
		 * @param mnemonickáSkratka kód mnemonickej skratky (príklad:
		 *     {@code Kláves.VK_A})
		 * @return objekt novej položky ponuky ({@link PoložkaPonuky
		 *     PoložkaPonuky}) alebo {@code valnull} ak nastala chyba
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajPoložkuPonuky(String, int, int)
		 * @see #pridajOddeľovačPonuky()
		 * @see #pridajPoložkuPonukyVymazať()
		 * @see #pridajPoložkuPonukyPrekresliť()
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static PoložkaPonuky pridajPoložkuPonuky(
			String text, int mnemonickáSkratka)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PoložkaPonuky(text, mnemonickáSkratka);
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonuky(String, int) pridajPoložkuPonuky}.</p> */
		public static PolozkaPonuky pridajPolozkuPonuky(String text, int mnemonickáSkratka)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PolozkaPonuky(text, mnemonickáSkratka);
		}

		/**
		 * <p>Funguje rovnako ako {@link #pridajPoložkuPonuky(String, int)}
		 * s tým, že definuje novej položke okrem mnemonickej aj klávesovú
		 * skratku (skratky, ktoré pri v položkách ponuky znázorňované
		 * v pravej časti textom Ctrl + písmeno). Skratky je možné
		 * najjednoduchšie určiť konštantou triedy {@link KeyEvent
		 * KeyEvent}.{@code VK_XXX} (tá je v robotovi na zjednodušenie
		 * prekrytá triedou {@link Kláves Kláves}, takže môžeme použiť
		 * napríklad {@code Kláves.VK_A}, {@code Kláves.VK_B}, {@code 
		 * Kláves.VK_C}…).</p>
		 * 
		 * @param text reťazec textu pridávanej položky ponuky
		 * @param mnemonickáSkratka kód mnemonickej skratky (príklad: {@code 
		 *     Kláves.VK_A})
		 * @param klávesováSkratka kód klávesovej skratky (príklad: {@code 
		 *     Kláves.VK_B})
		 * @return objekt novej položky ponuky ({@link PoložkaPonuky
		 *     PoložkaPonuky}) alebo {@code valnull} ak nastala chyba
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajPoložkuPonuky(String, int)
		 * @see #pridajOddeľovačPonuky()
		 * @see #pridajPoložkuPonukyVymazať()
		 * @see #pridajPoložkuPonukyPrekresliť()
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static PoložkaPonuky pridajPoložkuPonuky(String text,
			int mnemonickáSkratka, int klávesováSkratka)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PoložkaPonuky(text, mnemonickáSkratka, klávesováSkratka);
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonuky(String, int, int) pridajPoložkuPonuky}.</p> */
		public static PolozkaPonuky pridajPolozkuPonuky(String text,
			int mnemonickáSkratka, int klávesováSkratka)
		{
			if (!inicializované || null == svet.getJMenuBar().
				getMenu(aktuálnaPonuka)) return null;
			return new PolozkaPonuky(text, mnemonickáSkratka, klávesováSkratka);
		}

		/**
		 * <p>Pridá do hlavnej ponuky oddeľovač. Štandardne je oddeľovač pridaný
		 * za prvé dve predvolené položky ponuky. Toto správanie nie je možné
		 * potlačiť, iba ak by ponuka bola najskôr vymazaná a znova vytvorená
		 * s vlastnou obsluhou nových položiek. Táto metóda slúži na
		 * pridávanie vlastných oddeľovačov do ponuky.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #vymažPonuku()
		 * @see #pridajPoložkuHlavnejPonuky(String)
		 * @see #pridajPoložkuPonuky(String)
		 */
		public static void pridajOddeľovačPonuky()
		{
			if (!inicializované) return;

			JMenu položkaHlavnejPonuky = svet.
				getJMenuBar().getMenu(aktuálnaPonuka);

			if (null != položkaHlavnejPonuky)
			{
				ponukaVPôvodnomStave = false;
				položkaHlavnejPonuky.insertSeparator(aktuálnaPoložka++);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajOddeľovačPonuky() pridajOddeľovačPonuky}.</p> */
		public static void pridajOddelovacPonuky() { pridajOddeľovačPonuky(); }

		/**
		 * <p>Pridá do ponuky položku „Vymazať“. Položka je jednou
		 * z preddefinovaných položiek, ktoré majú vopred definovanú
		 * funkcionalitu. Táto po zvolení vymaže a prekreslí svet.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajPoložkuPonukyPrekresliť()
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static void pridajPoložkuPonukyVymazať()
		{
			// if (ponukaVPôvodnomStave) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			if (inicializované && null != hlavnáPonuka)
			{
				JMenu položkaHlavnejPonuky =
					hlavnáPonuka.getMenu(aktuálnaPonuka);
				if (null != položkaHlavnejPonuky)
				{
					položkaHlavnejPonuky.insert(
						položkaPonukyVymazať(),
						aktuálnaPoložka++);
					if (!hlavnáPonuka.isVisible())
						hlavnáPonuka.setVisible(true);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonukyVymazať() pridajPoložkuPonukyVymazať}.</p> */
		public static void pridajPolozkuPonukyVymazat()
		{ pridajPoložkuPonukyVymazať(); }

		/**
		 * <p>Pridá do ponuky položku „Prekresliť“. Položka je jednou
		 * z preddefinovaných položiek, ktoré majú vopred definovanú
		 * funkcionalitu. Táto vynúti prekreslenie sveta.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajPoložkuPonukyVymazať()
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static void pridajPoložkuPonukyPrekresliť()
		{
			// if (ponukaVPôvodnomStave) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			if (inicializované && null != hlavnáPonuka)
			{
				JMenu položkaHlavnejPonuky =
					hlavnáPonuka.getMenu(aktuálnaPonuka);
				if (null != položkaHlavnejPonuky)
				{
					položkaHlavnejPonuky.insert(
						položkaPonukyPrekresliť(),
						aktuálnaPoložka++);
					if (!hlavnáPonuka.isVisible())
						hlavnáPonuka.setVisible(true);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonukyPrekresliť() pridajPoložkuPonukyPrekresliť}.</p> */
		public static void pridajPolozkuPonukyPrekreslit()
		{ pridajPoložkuPonukyPrekresliť(); }

		/**
		 * <p>Pridá do ponuky preddefinovanú položku „Koniec“. Toto je
		 * využiteľné keď sme ponuku {@linkplain #vymažPonuku() vymazali}
		 * a chceme do nej pridať túto položku, ktorej funkcionalita je
		 * vopred naprogramovaná.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonuky(String)
		 * @see #pridajPoložkuPonukyVymazať()
		 * @see #pridajPoložkuPonukyPrekresliť()
		 */
		public static void pridajPoložkuPonukyKoniec()
		{
			if (ponukaVPôvodnomStave) return;

			JMenuBar hlavnáPonuka = svet.getJMenuBar();
			if (inicializované && null != hlavnáPonuka)
			{
				JMenu položkaHlavnejPonuky =
					hlavnáPonuka.getMenu(aktuálnaPonuka);
				if (null != položkaHlavnejPonuky)
				{
					položkaHlavnejPonuky.insert(položkaSkončiť,
						aktuálnaPoložka++);
					if (!hlavnáPonuka.isVisible())
						hlavnáPonuka.setVisible(true);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajPoložkuPonukyKoniec() pridajPoložkuPonukyKoniec}.</p> */
		public static void pridajPolozkuPonukyKoniec()
		{ pridajPoložkuPonukyKoniec(); }


		/**
		 * <p>Poskytne preddefinovanú položku „Vymazať“ na prípadné úpravy.
		 * Ak položka doteraz nebola použitá, bude automaticky pridaná do
		 * hlavnej ponuky. Toto správanie nie je možné ovplyvniť. Ak je
		 * položka v ponuke nežiaduca, je potrebné ju dodatočne odstrániť.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonukyVymazať()
		 */
		public static PoložkaPonuky položkaPonukyVymazať()
		{
			if (null == položkaVymazať)
				položkaVymazať = new PoložkaPonuky("Vymazať",
					KeyEvent.VK_M, KeyEvent.VK_T);
			return položkaVymazať;
		}

		/** <p><a class="alias"></a> Alias pre {@link #položkaPonukyVymazať() položkaPonukyVymazať}.</p> */
		public static PoložkaPonuky polozkaPonukyVymazat()
		{ return položkaPonukyVymazať(); }

		/**
		 * <p>Poskytne preddefinovanú položku „Prekresliť“ na prípadné úpravy.
		 * Ak položka doteraz nebola použitá, bude automaticky pridaná do
		 * hlavnej ponuky. Toto správanie nie je možné ovplyvniť. Ak je
		 * položka v ponuke nežiaduca, je potrebné ju dodatočne odstrániť.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonukyPrekresliť()
		 */
		public static PoložkaPonuky položkaPonukyPrekresliť()
		{
			if (null == položkaPrekresliť)
				položkaPrekresliť = new PoložkaPonuky("Prekresliť",
					KeyEvent.VK_R, KeyEvent.VK_R);
			return položkaPrekresliť;
		}

		/** <p><a class="alias"></a> Alias pre {@link #položkaPonukyPrekresliť() položkaPonukyPrekresliť}.</p> */
		public static PoložkaPonuky polozkaPonukyPrekreslit()
		{ return položkaPonukyPrekresliť(); }

		/**
		 * <p>Poskytne preddefinovanú položku „Koniec“ na prípadné úpravy.</p>
		 * 
		 * @see PoložkaPonuky
		 * @see #pridajPoložkuPonukyKoniec()
		 */
		public static PoložkaPonuky položkaPonukyKoniec()
		{ return položkaSkončiť; }

		/** <p><a class="alias"></a> Alias pre {@link #položkaPonukyKoniec() položkaPonukyKoniec}.</p> */
		public static PoložkaPonuky polozkaPonukyKoniec()
		{ return položkaPonukyKoniec(); }


		// Text na riadky

		/**
		 * <p>Rozdelí dlhý text na riadky s maximálnou zadanou dĺžkou
		 * v znakoch.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda zmení všetky znaky
		 * tabulátorov na medzery.</p>
		 * 
		 * @param text text, ktorý má byť rozdelený na riadky
		 * @param dĺžkaRiadka maximálna dĺžka jednotlivých riadkov udaná
		 *     v znakoch
		 * @return pole reťazcov – jednotlivé riadky pôvodného textu
		 */
		public static String[] textNaRiadky(String text, int dĺžkaRiadka)
		{
			if (text == null || dĺžkaRiadka <= 0 ||
				text.length() <= dĺžkaRiadka)
				return new String[] {text};

			StringBuffer naZmeny = new StringBuffer(text);
			Vector<String> zoznam = new Vector<>();

			int j;
			while (-1 != (j = naZmeny.indexOf("\t")))
				naZmeny.setCharAt(j, ' ');

			while (naZmeny.length() > 0)
			{
				if (-1 != (j = naZmeny.indexOf("\n")) && j <= dĺžkaRiadka)
				{
					int i = j;
					if (i >= 0) zoznam.add(naZmeny.substring(0, i));
					naZmeny.delete(0, j + 1);
					continue;
				}

				if (naZmeny.length() <= dĺžkaRiadka)
				{
					zoznam.add(naZmeny.toString());
					break;
				}

				int i = dĺžkaRiadka;
				while (i > 0 && naZmeny.charAt(i - 1) != 32) --i;
				if (i == 0) i = dĺžkaRiadka;

				zoznam.add(naZmeny.substring(0, i));
				naZmeny.delete(0, i);
			}

			String riadky[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i) riadky[i] = zoznam.get(i);
			return riadky;
		}

		/**
		 * <p>Rozdelí dlhý text na riadky s maximálnou zadanou dĺžkou
		 * v znakoch.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda zmení všetky znaky
		 * tabulátorov na medzery.</p>
		 * 
		 * @param text text, ktorý má byť rozdelený na riadky
		 * @param dĺžkaRiadka maximálna dĺžka jednotlivých riadkov udaná
		 *     v znakoch
		 * @param zachovajMedzery ak je {@code valfalse}, medzery budú
		 *     redukované na minimum – medzery na koncoch a začiatkoch riadkov
		 *     budú odstránené a viacnásobné medzery medzi slovami budú
		 *     redukované na jedinú medzeru
		 * @return pole reťazcov – jednotlivé riadky pôvodného textu
		 */
		public static String[] textNaRiadky(String text,
			int dĺžkaRiadka, boolean zachovajMedzery)
		{
			if (zachovajMedzery) return textNaRiadky(text, dĺžkaRiadka);

			if (text == null || dĺžkaRiadka <= 0 ||
				text.length() <= dĺžkaRiadka)
				return new String[] {text};

			StringBuffer naZmeny = new StringBuffer(text);
			Vector<String> zoznam = new Vector<>();

			int j;
			while (-1 != (j = naZmeny.indexOf("\t")))
				naZmeny.setCharAt(j, ' ');

			// Redukuj medzery
			{
				int i = naZmeny.length();
				while (i > 0 && naZmeny.charAt(i - 1) == 32) --i;
				naZmeny.setLength(i);

				i = 0;
				while (i < naZmeny.length() && naZmeny.charAt(i) == 32) ++i;
				naZmeny.delete(0, i);

				while (-1 != (i = naZmeny.indexOf("  ")))
					naZmeny.deleteCharAt(i);
			}

			while (naZmeny.length() > 0)
			{
				// Redukuj medzery
				{
					int i = 0;
					while (i < naZmeny.length() &&
						naZmeny.charAt(i) == 32) ++i;
					naZmeny.delete(0, i);
				}

				if (-1 != (j = naZmeny.indexOf("\n")) && j <= dĺžkaRiadka)
				{
					int i = j;

					// Redukuj medzery
					while (i > 0 && naZmeny.charAt(i - 1) == 32) --i;

					if (i >= 0) zoznam.add(naZmeny.substring(0, i));
					naZmeny.delete(0, j + 1);
					continue;
				}

				if (naZmeny.length() <= dĺžkaRiadka)
				{
					zoznam.add(naZmeny.toString());
					break;
				}

				int i = dĺžkaRiadka;
				while (i > 0 && naZmeny.charAt(i - 1) != 32) --i;

				// Redukuj medzery
				while (i > 0 && naZmeny.charAt(i - 1) == 32) --i;

				if (i == 0) i = dĺžkaRiadka;

				zoznam.add(naZmeny.substring(0, i));
				naZmeny.delete(0, i);
			}

			String riadky[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i) riadky[i] = zoznam.get(i);
			return riadky;
		}


		// Formát

		/**
		 * <p>Inštancia triedy {@link DecimalFormat DecimalFormat} slúžiaca na
		 * formátovanie číselných výstupov. Úpravou vlastností tejto inštancie
		 * je možné dosiahnuť zmenu spôsobu formátovania celých a reálnych
		 * čísiel na výstupe (konzolovom alebo pri použití metódy
		 * {@link Svet#F(double, int, int) F}). Niektoré vlastnosti tejto
		 * inštancie sú použité aj na úpravu formátu čísiel zadávaných
		 * vstupnými prvkami (dialógmi alebo vstupným riadkom).</p>
		 * 
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#zadajCeléČíslo(String)
		 * @see Svet#zadajReálneČíslo(String)
		 * @see Svet#upravCeléČíslo(long, String)
		 * @see Svet#upravReálneČíslo(double, String)
		 * @see Svet#prevezmiCeléČíslo()
		 * @see Svet#prevezmiReálneČíslo()
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public final static DecimalFormat formát;

		/** <p><a class="alias"></a> Alias pre {@link #formát formát}.</p> */
		public final static DecimalFormat format;

		static
		{
			NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

			if (nf instanceof DecimalFormat)
			{
				formát = (DecimalFormat)nf;
				format = formát;

				DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();

				// formát.setGroupingUsed(false);
				dfs.setGroupingSeparator(' ');
				dfs.setDecimalSeparator(',');
				dfs.setMonetaryDecimalSeparator(',');
				dfs.setMinusSign('−');
				dfs.setCurrencySymbol(" €");
				formát.setDecimalFormatSymbols(dfs);
				formát.setMaximumFractionDigits(20);
			}
			else
			{
				formát = format = null;
			}
		}

		// Odfiltruje (lokalizovaný) reťazec tak, aby bol rozpoznateľný
		// metódami číselných vstupov
		private static String filtrujReťazec(String reťazec)
		{
			DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();

			// Znak záporného znamienka:
			char ms = dfs.getMinusSign();
			if (ms != '-') reťazec = reťazec.replace(ms, '-');

			// Oddeľovač tisícov:
			char gs = formát.isGroupingUsed() ?
				dfs.getGroupingSeparator() : ',';
			if (gs != ',' && gs != ' ') reťazec = reťazec.replace(gs, ' ');

			// Oddeľovač desatinnej časti:
			char ds = dfs.getDecimalSeparator();
			if (ds != '.') reťazec = reťazec.replace(ds, '.');

			// Odfiltruje všetky bežné oddeľovače tisícov (bežne je na
			// vstupe očakávaná jediná hodnota, takže síce sa tu zlúčia
			// i prípadné konfliktne nastavené oddeľovače prvkov poľa,
			// no prípadný vstup viacerých prvkov (poľa) v jednom
			// reťazci musí byť i tak vyriešený mimo vstavaného
			// implementovaného mechanizmu, pretože ten ich aj tak
			// nie je schopný rozpoznať):
			return reťazec.replaceAll("[  ,']+", "");
		}


		// Oddeľovač prvkov poľa používaný počas výpisu poľa príkazmi vypíš
		/*packagePrivate*/ static String oddeľovačPrvkovPoľa = "; ";

		/**
		 * <p>Zistí, aký oddeľovač prvkov je aktuálne používaný pri výpisoch polí
		 * príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p>
		 * 
		 * @return oddeľovač prvkov poľa (reťazec)
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static String oddeľovačPrvkovPoľa() { return oddeľovačPrvkovPoľa; }

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačPrvkovPoľa() oddeľovačPrvkovPoľa}.</p> */
		public static String oddelovacPrvkovPola() { return oddeľovačPrvkovPoľa; }

		/**
		 * <p>Nastaví hodnotu oddeľovača prvkov poľa, ktorý bude používaný pri
		 * výpisoch polí príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p>
		 * 
		 * @param oddeľovač nový oddeľovač prvkov poľa
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa()
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static void oddeľovačPrvkovPoľa(String oddeľovač)
		{ oddeľovačPrvkovPoľa = oddeľovač; }

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačPrvkovPoľa(String) oddeľovačPrvkovPoľa}.</p> */
		public static void oddelovacPrvkovPola(String oddeľovač)
		{ oddeľovačPrvkovPoľa = oddeľovač; }


		/**
		 * <p>Zistí, aký oddeľovač desatinnej časti je aktuálne používaný pri
		 * výpisoch čísiel príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}. Tento oddeľovač je
		 * braný do úvahy aj pri rozpoznávaní číselných hodnôt rôznymi
		 * metódami vstupu.</p>
		 * 
		 * @return oddeľovač desatinnej časti
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static char oddeľovačDesatinnejČasti()
		{
			DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();
			return dfs.getDecimalSeparator();
		}

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačDesatinnejČasti() oddeľovačDesatinnejČasti}.</p> */
		public static char oddelovacDesatinnejCasti()
		{ return oddeľovačDesatinnejČasti(); }

		/**
		 * <p>Nastaví hodnotu oddeľovača desatinnej časti, ktorý bude používaný
		 * pri výpisoch príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}. Zadaný oddeľovač bude
		 * vzatý do úvahy aj pri rozpoznávaní číselných hodnôt rôznymi
		 * metódami vstupu.</p>
		 * 
		 * @param oddeľovač nový oddeľovač desatinnej časti
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti()
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static void oddeľovačDesatinnejČasti(char oddeľovač)
		{
			DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();
			if (dfs.getDecimalSeparator() != oddeľovač)
			{
				dfs.setDecimalSeparator(oddeľovač);
				dfs.setMonetaryDecimalSeparator(oddeľovač);
				formát.setDecimalFormatSymbols(dfs);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačDesatinnejČasti(char) oddeľovačDesatinnejČasti}.</p> */
		public static void oddelovacDesatinnejCasti(char oddeľovač)
		{ oddeľovačDesatinnejČasti(oddeľovač); }


		/**
		 * <p>Zistí, aký oddeľovač tisícov je aktuálne používaný pri výpisoch
		 * čísiel príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}. Tento oddeľovač je
		 * braný do úvahy aj pri rozpoznávaní číselných hodnôt rôznymi
		 * metódami vstupu. Ak oddeľovač tisícov nie je použitý (to znamená,
		 * že celá časť čísla je vždy vypísaná spolu a tisíce nie sú
		 * oddeľované), tak je výsledkom tejto metódy hodnota {@code valnull}.</p>
		 * 
		 * @return oddeľovač tisícov alebo {@code valnull}
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static Character oddeľovačTisícov()
		{
			if (formát.isGroupingUsed())
			{
				DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();
				return dfs.getGroupingSeparator();
			}
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačTisícov() oddeľovačTisícov}.</p> */
		public static Character oddelovacTisicov()
		{ return oddeľovačTisícov(); }

		/**
		 * <p>Nastaví hodnotu oddeľovača tisícov, ktorý bude používaný pri
		 * výpisoch príkazmi {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}. Zadaný oddeľovač bude
		 * vzatý do úvahy aj pri rozpoznávaní číselných hodnôt rôznymi
		 * metódami vstupu. Ak je požadované, aby vo výpisoch tisíce neboli
		 * oddeľované, tak je potrebné vložiť hodnotu {@code valnull}.</p>
		 * 
		 * @param oddeľovač nový oddeľovač tisícov alebo {@code valnull}
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov()
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static void oddeľovačTisícov(Character oddeľovač)
		{
			if (null == oddeľovač) formát.setGroupingUsed(false); else
			{
				formát.setGroupingUsed(true);
				DecimalFormatSymbols dfs = formát.getDecimalFormatSymbols();
				if (dfs.getGroupingSeparator() != oddeľovač)
				{
					dfs.setGroupingSeparator(oddeľovač);
					formát.setDecimalFormatSymbols(dfs);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #oddeľovačTisícov(Character) oddeľovačTisícov}.</p> */
		public static void oddelovacTisicov(Character oddeľovač)
		{ oddeľovačTisícov(oddeľovač); }


		/**
		 * <p>Táto metóda slúži na jednoduché formátovanie reálnych čísiel.
		 * Prijíma reálne číslo a hodnotu počtu desatinných miest, ktoré majú
		 * byť zobrazené.
		 * Na jednoduchšie používanie je rovnaká metóda definovaná aj v hlavnej
		 * triede: {@link GRobot#F(double, int) F(číslo, desatinné)}.
		 * (V jej opise je uvedený aj príklad použitia a ďalšie fakty.)
		 * Metóda má ešte jednu verziu: {@link Svet#F(double, int, int)
		 * Svet.F(číslo, šírka, desatinné)}.</p>
		 * 
		 * @param číslo hodnota, ktorá má byť naformátovaná
		 * @param desatinné počet desatinných miest, ktoré majú byť uvedené
		 *     vo výslednom (naformátovanom) tvare
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static String F(double číslo, int desatinné)
		{
			return Svet.F(číslo, 1, desatinné);
			// return String.format(Locale.ENGLISH,
			// 	"%." + desatinné + "f", číslo);
		}

		/**
		 * <p>Táto metóda slúži na jednoduché formátovanie reálnych čísiel.
		 * Prijíma reálne číslo, potom hodnotu určujúcu šírku výsledného
		 * reťazca (počet znakov), na ktorú má byť zarovnaný zľava a nakoniec
		 * počet desatinných miest, ktoré majú byť zobrazené.
		 * Na jednoduchšie používanie je rovnaká metóda definovaná aj v hlavnej
		 * triede: {@link GRobot#F(double, int, int)
		 * F(číslo, šírka, desatinné)}.
		 * (V jej opise je uvedený aj príklad použitia a ďalšie fakty.)
		 * Metóda má ešte jednu verziu: {@link Svet#F(double, int)
		 * Svet.F(číslo, desatinné)}.</p>
		 * 
		 * @param číslo hodnota, ktorá má byť naformátovaná
		 * @param šírka najmenší počet znakov výsledného naformátovaného
		 *     reťazca (pri krátkych číslach budú chýbajúce znaky doplnené
		 *     medzerami)
		 * @param desatinné počet desatinných miest, ktoré majú byť uvedené
		 *     vo výslednom (naformátovanom) tvare
		 * 
		 * @see Svet#formát
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#vypíš(Object[])
		 * @see Svet#F(double, int)
		 * @see GRobot#F(double, int)
		 * @see GRobot#F(double, int, int)
		 */
		public static String F(double číslo, int šírka, int desatinné)
		{
			if (šírka < 1) šírka = 1;

			int min = formát.getMinimumFractionDigits();
			int max = formát.getMaximumFractionDigits();

			formát.setMinimumFractionDigits(desatinné);
			formát.setMaximumFractionDigits(desatinné);

			String formátované = String.format(Locale.ENGLISH,
				"%" + šírka + "s", formát.format(číslo));

			formát.setMinimumFractionDigits(min);
			formát.setMaximumFractionDigits(max);

			return formátované;
			// return String.format(Locale.ENGLISH,
			// 	"%" + šírka + "." + desatinné + "f", číslo);
		}


		// Ikony, kurzory a ukončenie

		/**
		 * <p><a class="setter"></a> Nastaví oknu aplikácie novú ikonu.</p>
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
		 * @param súbor názov súboru s obrázkom
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void ikona(String súbor)
		{
			svet.setIconImage(Obrázok.súborNaObrázok(súbor));
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setIconImage(svet.getIconImage());
		}

		/**
		 * <p>Nastaví oknu aplikácie ikonu podľa zadaného obrázka.</p>
		 * 
		 * <!-- p class="remark"><b>Poznámka:</b> Z dôvodu konzistencie je
		 * k tejto metóde definovaný alias {@link #obrazok(Image)
		 * obrazok(obrázok)} (s názvom bez diakritiky), ktorý má korešpondovať
		 * s aliasom metódy {@link #ikona() ikona()}.</p> NEDÁ SA – TODO:
		 * vysvetliť prečo a podotknúť, že to neprekáža, lebo metóda
		 * akceptuje oboje -->
		 * 
		 * @param obrázok obrázok slúžiaci ako predloha pre ikonu
		 */
		public static void ikona(Image obrázok)
		{
			svet.setIconImage(obrázok);
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setIconImage(obrázok);
		}

		// /** <p><a class="alias"></a> Alias pre {@link #ikona(Image) ikona}.</p> */
		// public static void obrazok(Image obrázok) { ikona(obrázok); }

		/**
		 * <p>Prečíta ikonu okna aplikácie a prevedie ju na obrázok.</p>
		 * 
		 * <!-- p class="remark"><b>Poznámka:</b> Názov tejto metódy neobsahuje
		 * žiadnu diakritiku, preto nemohol byť definovaný prislúchajúci alias,
		 * ktorý by vracal objekt typu {@link Obrazok Obrazok} (t. j. triedy
		 * aliasu, ktorej názov tiež neobsahuje diakritiku). Z toho dôvodu je
		 * alias nahradený metódou {@link #obrazok() obrazok} (bez
		 * diakritiky).</p> NEDÁ SA – TODO: vysvetliť prečo a podotknúť, že to
		 * bolo vyriešené tak, že táto metóda vracia objekt typu Obrazok bez
		 * diakritiky, ktorý je akceptovaný aj všade tam, kde by sme inak
		 * očakávali objekt typu Obrázok s diakritikou. -->
		 * 
		 * @return obrázok s ikonou
		 */
		public static Obrazok ikona()
		{ return new Obrazok(svet.getIconImage()); }

		// /** <p><a class="alias"></a> Alias pre {@link #ikona() ikona}.</p> */
		// public static Obrazok obrazok()
		// { return new Obrazok(svet.getIconImage()); }

		/**
		 * <p>Definuje nový kurzor myši použiteľný v hlavnom okne aplikácie.
		 * Tvar kurzora určí aktuálna grafika zadaného obrázka (môže byť
		 * použitý aj objekt typu {@link Obrázok Obrázok}). Súradnice {@code 
		 * x} a {@code x} určujú aktívny bod kurzora (<em>hot spot</em>),
		 * t. j. bod, ktorý bude považovaný za „špičku“, či „stred“ kurzora
		 * (záleží na tvare kurzora). Súradnice [0, 0] sú považované za
		 * stred kurzora. Rozsah súradníc je určený rozmermi obrázka.
		 * Odporúčame zvoliť také rozmery obrázka, ktoré sú štandardné pre
		 * kurzory, napríklad 32 × 32 bodov, inak by mohlo dôjsť
		 * k nežiadanému posunu aktívneho bodu kurzora. Názov kurzora musí
		 * byť jedinečný. Ten treba použiť pri volaní metódy {@link 
		 * #zmeňKurzorMyši(String) zmeňKurzorMyši}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * <table><tr><td>
		 * 
		 * <pre CLASS="example">
			{@link Obrázok Obrázok} ihla = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num32}, {@code num32});

			{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(ihla);
			{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num2.5});

			{@link GRobot#farba(Color) farba}({@link Farebnosť#tyrkysová tyrkysová});
			{@link GRobot#choďNa(double, double) choďNa}(&#45;{@code num16}, &#45;{@code num16});

			{@link GRobot#domov() domov}();
			{@link GRobot#farba(Color) farba}({@link Farebnosť#modrá modrá});
			{@link GRobot#kruh(double) kruh}({@code num4.5});

			{@link Svet Svet}.{@code currnovýKurzorMyši}(ihla, &#45;{@code num15}, &#45;{@code num15}, {@code srg"ihla"});
			{@link Svet Svet}.{@link #zmeňKurzorMyši(String) zmeňKurzorMyši}({@code srg"ihla"});
			</pre>
		 * </td><td><image>novyKurzorMysi.png<alt/>Ukážka vlastného tvaru
		 * kurzora myši.</image>Maličký výrez plátna so zobrazeným novým
		 * kurzorom nad ním.</td></tr></table>
		 * 
		 * @param predloha obrázok, ktorého grafika bude použitá na
		 *     vytvorenie kurzora
		 * @param x x-ová súradnica aktívneho bodu nového kurzora
		 * @param y y-ová súradnica aktívneho bodu nového kurzora
		 * @param meno meno kurzora, ktoré bude neskôr použité pri volaní
		 *     metódy {@link #zmeňKurzorMyši(String) zmeňKurzorMyši}
		 * 
		 * @throws GRobotException ak kurzor so zadaným menom už jestvuje
		 * 
		 * @see #zmeňKurzorMyši(String)
		 * @see #zmeňNovýKurzorMyši(Image, int, int, String)
		 */
		public static void novýKurzorMyši(Image predloha,
			int x, int y, String meno)
		{
			if (null == meno) return;

			if (kurzory.containsKey(meno))
				throw new GRobotException(
					"Kurzor so zadaným menom (" + meno + ") už jestvuje.",
					"cursorAlreadyExists", meno);

			Image relevantný = Obrázok.dajRelevantnýRaster(predloha);

			Dimension veľkosť = Toolkit.getDefaultToolkit().
				getBestCursorSize(
					relevantný.getWidth(null),
					relevantný.getHeight(null));

			x = (veľkosť.width / 2) + x;
			y = (veľkosť.height / 2) - y;

			if (x < 0) x = 0; if (y < 0) y = 0;
			if (x >= veľkosť.width) x = veľkosť.width - 1;
			if (y >= veľkosť.height) y = veľkosť.height - 1;

			kurzory.put(meno, Toolkit.getDefaultToolkit().
				createCustomCursor(relevantný, new Point(x, y), meno));
		}

		/** <p><a class="alias"></a> Alias pre {@link #novýKurzorMyši(Image, int, int, String) novýKurzorMyši}.</p> */
		public static void novyKurzorMysi(Image predloha, int x, int y, String meno) { novýKurzorMyši(predloha, x, y, meno); }

		/** <p><a class="alias"></a> Alias pre {@link #novýKurzorMyši(Image, int, int, String) novýKurzorMyši}.</p> */
		public static void novýKurzorMyši(Image predloha, double x, double y, String meno) { novýKurzorMyši(predloha, (int)x, (int)y, meno); }

		/** <p><a class="alias"></a> Alias pre {@link #novýKurzorMyši(Image, int, int, String) novýKurzorMyši}.</p> */
		public static void novyKurzorMysi(Image predloha, double x, double y, String meno) { novýKurzorMyši(predloha, (int)x, (int)y, meno); }

		/**
		 * <p>Zmení hlavnému oknu tvar kurzora myši buď na vlastný tvar kurzora
		 * vytvorený prostredníctvom metódy {@link 
		 * #novýKurzorMyši(Image, int, int, String) novýKurzorMyši}
		 * (prípadne {@link #zmeňNovýKurzorMyši(Image, int, int,
		 * String) zmeňNovýKurzorMyši}), alebo na niektorý zo systémom
		 * preddefinovaných kurzorov. Systémové kurzory majú nasledujúce
		 * preddefinované názvy: {@code srg"predvolený"},
		 * {@code srg"mieridlo"}, {@code srg"text"}, {@code srg"čakaj"},
		 * {@code srg"swZmeniťVeľkosť"}, {@code srg"seZmeniťVeľkosť"},
		 * {@code srg"nwZmeniťVeľkosť"}, {@code srg"neZmeniťVeľkosť"},
		 * {@code srg"nZmeniťVeľkosť"}, {@code srg"sZmeniťVeľkosť"},
		 * {@code srg"wZmeniťVeľkosť"}, {@code srg"eZmeniťVeľkosť"},
		 * {@code srg"ruka"}, {@code srg"presunúť"}. Ak namiesto názvu
		 * kurzora zadáte {@code valnull}, bude použitý prázdny (neviditeľný)
		 * kurzor.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Testy ukázali, že táto metóda nemá
		 * vplyv na tvar kurzora v režime {@linkplain #celáObrazovka(int,
		 * boolean) celej obrazovky} v operačnom systéme Windows s viacerými
		 * zobrazovacími zariadeniami. Je možné, že nastavenie tvaru kurzora
		 * nebude fungovať aj v iných situáciách súvisiacich s režimom celej
		 * obrazovky.</p>
		 * 
		 * @param meno názov vlastného kurzora, systémového kurzora alebo
		 *     {@code valnull}
		 * 
		 * @throws GRobotException ak kurzor so zadaným menom nejestvuje
		 * 
		 * @see #novýKurzorMyši(Image, int, int, String)
		 * @see #zmeňNovýKurzorMyši(Image, int, int, String)
		 */
		public static void zmeňKurzorMyši(String meno)
		{
			if (null == meno)
			{
				svet.getContentPane().setCursor(prázdnyKurzor);

				// Pre celú obrazovku:
				if (null != oknoCelejObrazovky)
					oknoCelejObrazovky.setCursor(prázdnyKurzor);
				return;
			}

			if (kurzory.containsKey(meno))
			{
				svet.getContentPane().
					setCursor(kurzory.get(meno));

				// Pre celú obrazovku:
				if (null != oknoCelejObrazovky)
					oknoCelejObrazovky.setCursor(
						svet.getContentPane().getCursor());
				return;
			}

			if (systémovéNázvyKurzorov.containsKey(meno))
			{
				svet.getContentPane().setCursor(Cursor.
					getPredefinedCursor(systémovéNázvyKurzorov.get(meno)));

				// Pre celú obrazovku:
				if (null != oknoCelejObrazovky)
					oknoCelejObrazovky.setCursor(
						svet.getContentPane().getCursor());
				return;
			}

			throw new GRobotException(
				"Kurzor so zadaným menom (" + meno + ") nejestvuje.",
				"cursorNotExists", meno);
		}

		/** <p><a class="alias"></a> Alias pre {@link #zmeňKurzorMyši(String) zmeňKurzorMyši}.</p> */
		public static void zmenKurzorMysi(String meno) { zmeňKurzorMyši(meno); }

		/**
		 * <p>Táto metóda je kombináciou volania metód {@link 
		 * #novýKurzorMyši(Image, int, int, String) novýKurzorMyši}
		 * a {@link #zmeňKurzorMyši(String) zmeňKurzorMyši}. Platia pre ňu
		 * rovnaké pravidlá ako pre uvedené dve metódy…</p>
		 * 
		 * @see #novýKurzorMyši(Image, int, int, String)
		 * @see #zmeňKurzorMyši(String)
		 */
		public static void zmeňNovýKurzorMyši(Image predloha,
			int x, int y, String meno)
		{
			novýKurzorMyši(predloha, x, y, meno);
			zmeňKurzorMyši(meno);
		}

		/** <p><a class="alias"></a> Alias pre {@link #zmeňNovýKurzorMyši(Image, int, int, String) zmeňNovýKurzorMyši}.</p> */
		public static void zmenNovyKurzorMysi(Image predloha, int x, int y, String meno) { zmeňNovýKurzorMyši(predloha, x, y, meno); }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňNovýKurzorMyši(Image, int, int, String) zmeňNovýKurzorMyši}.</p> */
		public static void zmeňNovýKurzorMyši(Image predloha, double x, double y, String meno) { zmeňNovýKurzorMyši(predloha, (int)x, (int)y, meno); }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňNovýKurzorMyši(Image, int, int, String) zmeňNovýKurzorMyši}.</p> */
		public static void zmenNovyKurzorMysi(Image predloha, double x, double y, String meno) { zmeňNovýKurzorMyši(predloha, (int)x, (int)y, meno); }


		// Počúvadlo akcií položiek kontextovej ponuky
		// ikony v systémovej oblasti
		private static ActionListener akciaSystémovejPoložky =
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ÚdajeUdalostí.poslednáUdalosťSystémovejIkony = e;
					ÚdajeUdalostí.poslednáPoložkaSystémovejPonuky = -1;

					if (null != systémováIkona)
					{
						if (e.getSource() instanceof MenuItem)
						{
							MenuItem položka = (MenuItem)e.getSource();
							PopupMenu ponuka = systémováIkona.getPopupMenu();
							int počet = ponuka.getItemCount();

							for (int i = 0; i < počet; ++i)
							{
								if (položka == ponuka.getItem(i))
								{
									ÚdajeUdalostí.
										poslednáPoložkaSystémovejPonuky = i;
									break;
								}
							}
						}
					}

					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.voľbaSystémovejPoložky();
							ObsluhaUdalostí.počúvadlo.volbaSystemovejPolozky();
						}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
							{
								počúvajúci.voľbaSystémovejPoložky();
								počúvajúci.volbaSystemovejPolozky();
							}
						}
				}
			};

		/**
		 * <p>Vráti objekt systémovej ikony sveta, ak je {@linkplain 
		 * #systémováIkona(String, Image, String...) definovaná}.</p>
		 * 
		 * @return buď objekt typu {@link TrayIcon TrayIcon}, alebo
		 *     {@code valnull}
		 */
		public static TrayIcon systémováIkona() { return systémováIkona; }

		/** <p><a class="alias"></a> Alias pre {@link #systémováIkona() systémováIkona}.</p> */
		public static TrayIcon systemovaIkona() { return systémováIkona(); }

		/**
		 * <p>Definuje alebo aktualizuje systémovú ikonu nazývanú aj ikonou
		 * v systémovej oblasti alebo v oblasti oznámení (angl. system
		 * tray). Pri prvom volaní metódy ide o vytvorenie ikony. Vtedy
		 * nesmie byť ikona prázdna ({@code valnull}). Položky kontextovej
		 * ponuky sú určené voliteľným zoznamom reťazcov, v ktorom hodnota
		 * {@code valnull} znamená oddeľovač. Ak vytvorenie alebo
		 * aktualizácia zlyhá, tak metóda vráti hodnotu {@code valfalse}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad ukazuje vytvorenie primitívnej aplikácie,
		 * ktorá sa po zatvorení hlavého okna aplikácie skryje/presunie
		 * do ikony v systémovej oblasti.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} AplikáciaVSystémovejOblasti {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} AplikáciaVSystémovejOblasti()
				{
					{@code comm// Nastavenie titulku okna.}
					{@code valsuper}({@code srg"Aplikácia v systémovej oblasti…"});

					{@code comm// Vyrobenie grafiky ikony a jej použitie ako ikony aplikácie.}
					{@link Obrázok Obrázok} ikona = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num32}, {@code num32});
					ikona.{@link Obrázok#vyplň(Color) vyplň}({@link Farebnosť#žltá žltá});
					{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(ikona);
					{@link GRobot#kruh(double) kruh}({@code num12});
					{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();
					{@link Svet Svet}.{@link Svet#ikona(Image) ikona}(ikona);

					{@code comm// Pokúsime sa vytvoriť systémovú ikonu s práve vytvorenou grafikou}
					{@code comm// a dvomi položkami kontextovej ponuky:}
					{@code kwdif} ({@link Svet Svet}.{@link Svet#systémováIkona(Image, String...) systémováIkona}(ikona, {@code srg"Obnoviť"}, {@code valnull}, {@code srg"Ukončiť"}))
					{
						{@code comm// Ak je systémová ikona podporovaná, tak zmeníme akciu zatvorenia}
						{@code comm// okna z predvoleného zavretia aplikácie na jej skrytie…}
						{@link GRobot#svet svet}.{@link javax.swing.JFrame#setDefaultCloseOperation(int) setDefaultCloseOperation}(
							{@link javax.swing javax.swing}.{@link javax.swing.JFrame JFrame}.{@link javax.swing.JFrame#HIDE_ON_CLOSE HIDE_ON_CLOSE});

							{@code comm// (poznámka: teraz má používateľ len dve možnosti zavretia}
							{@code comm// tejto aplikácie s použitím jej grafického používateľského}
							{@code comm// rozhrania – predvolenou položkou „Koniec“ v hlavnej ponuke}
							{@code comm// alebo zvolením kontextovej položky „Ukončiť“ v kontextovej}
							{@code comm// ponuke systémovej ikony)}

						{@code comm// …a vypíšeme o tom do okna informáciu:}
						{@link GRobot#skoč(double) skoč}({@code num15});
						{@link GRobot#text(String) text}({@code srg"Tlačidlo „Zavrieť“ okna aplikácie teraz"});
						{@link GRobot#odskoč(double) odskoč}({@code num30});
						{@link GRobot#text(String) text}({@code srg"slúži na jeho skrytie do systémovej oblasti."});

						{@code comm// Systémovú ikonu nateraz skryjeme.}
						{@link Svet Svet}.{@link Svet#zobrazSystémovúIkonu(boolean) zobrazSystémovúIkonu}({@code valfalse});
					}
					{@code kwdelse}
					{
						{@code comm// V opačnom prípade vypíšeme informáciu o tom, že systém túto}
						{@code comm// vlastnosť nepodporuje (alebo jej použitiu bránia iné okolnosti).}
						{@link GRobot#farba(Color) farba}({@link Farebnosť#červená červená});
						{@link GRobot#text(String) text}({@code srg"Systémová ikona nie je podporovaná."});
					}

					{@code comm// Skryjeme robota a vystredíme okno aplikácie.}
					{@link GRobot#skry() skry}();
					{@link Svet Svet}.{@link Svet#vystreď() vystreď}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#voľbaSystémovejIkony() voľbaSystémovejIkony}()
				{
					{@code comm// Predvolenou akciou dvojitého kliknutia na ikonu v systémovej oblasti}
					{@code comm// bude skrytie systémovej ikony a zobrazenie okna aplikácie (ak je skryté).}
					{@link Svet Svet}.{@link Svet#zobrazSystémovúIkonu(boolean) zobrazSystémovúIkonu}({@code valfalse});
					{@code kwdif} (!{@link Svet Svet}.{@link Svet#viditeľný() viditeľný}()) {@link Svet Svet}.{@link Svet#zobraz() zobraz}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#voľbaSystémovejPoložky() voľbaSystémovejPoložky}()
				{
					{@code kwdswitch} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#položkaSystémovejPonuky() položkaSystémovejPonuky}())
					{
					{@code kwdcase} {@code num0}:
						{@code comm// Prvá položka kontextovej ponuky bude vykonávať rovnakú akciu}
						{@code comm// ako dvojklik na systémovú ikonu.}
						voľbaSystémovejIkony();
						{@code kwdbreak};

						{@code comm// Poznámka: Index 1 má oddeľovač.}

					{@code kwdcase} {@code num2}:
						{@code comm// Druhá položka (technicky tretia) kontextovej ponuky aplikáciu zavrie.}
						{@link Svet Svet}.{@link Svet#koniec() koniec}();
						{@code kwdbreak};
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link GRobot#zavretie zavretie}()
				{
					{@code comm// Touto reakciou vypneme klasickú akciu zavretia okna.}
					{@code kwdreturn} {@code valfalse};
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#skrytieOkna() skrytieOkna}()
				{
					{@code comm// Pri skrytí okna aplikácie bude zobrazená systémová ikona.}
					{@link Svet Svet}.{@link Svet#zobrazSystémovúIkonu(boolean) zobrazSystémovúIkonu}({@code valtrue});
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@link Svet Svet}.{@link Svet#skry() skry}();
					{@code kwdnew} AplikáciaVSystémovejOblasti();
					{@link Svet Svet}.{@link Svet#zobraz() zobraz}();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered"><tr><td>
		 * <image>aplsysobl-okno-windows-10.png<alt/></image>
		 * </td></tr><tr><td><p class="image">Okno aplikácie po spustení
		 * v stave, keď je<br />ikona v systémovej oblasti podporovaná.</p></td>
		 * </tr></table>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>aplsysobl-ikona-panel-windows-10.png<alt/></image></td>
		 * <td><image>aplsysobl-ikona+ponuka-sysobl-windows-10.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Ikona aplikácie na paneli úloh.</p></td>
		 * <td><p class="image">Ikona aplikácie (s ponukou) v systémovej
		 * oblasti.</p></td></tr>
		 * </table>
		 * 
		 * <!-- TODO – ak bude čas, tak ukážka vzhľadu
		 * na rôznych platformách. -->
		 * 
		 * @param popis popis zobrazovaný pri ukázaní na ikonu
		 *     v systémovej oblasti
		 * @param ikona obrázok určujúci vzhľad ikony v systémovej oblasti
		 * @param položkyPonuky voliteľný zoznam reťazcov, z ktorých bude
		 *     vytvorená kontextová ponuka ikony
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean systémováIkona(String popis, Image ikona,
			String... položkyPonuky)
		{
			// Zdroj: https://docs.oracle.com/javase/8/docs/api/java/awt/SystemTray.html

			Obrázok obrázok = null;

			if (null != ikona)
			{
				if (ikona instanceof Obrázok)
					obrázok = (Obrázok)ikona;
				else
					obrázok = new Obrázok(ikona);
			}

			// Ak ikona nejestvuje, tak ju vytvoríme.
			if (null == systémováIkona)
			{
				// Obrázok musí byť pre novú ikonu definovaný.
				if (null == obrázok) return false;

				if (SystemTray.isSupported()) try
				{
					SystemTray systémováOblasť = SystemTray.getSystemTray();


					PopupMenu ponuka = null;

					if (0 != položkyPonuky.length)
					{
						ponuka = new PopupMenu();

						for (String textPoložky : položkyPonuky)
						{
							if (null == textPoložky)
							{
								ponuka.addSeparator();
							}
							else
							{
								MenuItem položka = new MenuItem(textPoložky);
								položka.addActionListener(akciaSystémovejPoložky);
								ponuka.add(položka);
							}
						}
					}


					Dimension veľkosť = systémováOblasť.getTrayIconSize();

					if (obrázok.šírka != veľkosť.width ||
						obrázok.výška != veľkosť.height)
						obrázok = obrázok.zmeňVeľkosť(
							veľkosť.width, veľkosť.height);


					systémováIkona = new TrayIcon(obrázok, popis, ponuka);

					systémováIkona.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							ÚdajeUdalostí.poslednáUdalosťSystémovejIkony = e;

							if (null != ObsluhaUdalostí.počúvadlo)
								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									ObsluhaUdalostí.počúvadlo.
										voľbaSystémovejIkony();
									ObsluhaUdalostí.počúvadlo.
										volbaSystemovejIkony();
								}

								synchronized (ÚdajeUdalostí.zámokUdalostí)
								{
									for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
									{
										počúvajúci.voľbaSystémovejIkony();
										počúvajúci.volbaSystemovejIkony();
									}
								}
						}
					});

					systémováOblasť.add(systémováIkona);
					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}
			// Inak ju podľa zadaných údajov aktualizujeme.
			else
			{
				try
				{
					if (null != popis)
						systémováIkona.setToolTip(popis);

					if (0 != položkyPonuky.length)
					{
						PopupMenu ponuka = new PopupMenu();

						for (String textPoložky : položkyPonuky)
						{
							if (null == textPoložky)
							{
								ponuka.addSeparator();
							}
							else
							{
								MenuItem položka = new MenuItem(textPoložky);
								položka.addActionListener(akciaSystémovejPoložky);
								ponuka.add(položka);
							}
						}

						systémováIkona.setPopupMenu(ponuka);
					}

					if (null != obrázok)
					{
						SystemTray systémováOblasť = SystemTray.getSystemTray();

						Dimension veľkosť = systémováOblasť.getTrayIconSize();

						if (obrázok.šírka != veľkosť.width ||
							obrázok.výška != veľkosť.height)
							obrázok = obrázok.zmeňVeľkosť(
								veľkosť.width, veľkosť.height);

						systémováIkona.setImage(obrázok);
					}

					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #systémováIkona(String, Image, String...) systémováIkona}.</p> */
		public static boolean systemovaIkona(String popis, Image ikona,
			String... položkyPonuky)
		{ return systémováIkona(popis, ikona, položkyPonuky); }

		/**
		 * <p>Aktualizuje jestvujúcu ikonu v systémovej oblasti. Ďalšie
		 * podrobnosti čítajte v opise metódy {@link #systémováIkona(String,
		 * Image, String...) systémováIkona}{@code (popis, ikona,
		 * položkyPonuky)}.</p>
		 * 
		 * @param popis popis zobrazovaný pri ukázaní na ikonu
		 *     v systémovej oblasti
		 * @param položkyPonuky voliteľný zoznam reťazcov, z ktorých bude
		 *     vytvorená kontextová ponuka ikony
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean systémováIkona(String popis,
			String... položkyPonuky)
		{ return systémováIkona(popis, null, položkyPonuky); }

		/** <p><a class="alias"></a> Alias pre {@link #systémováIkona(String, String...) systémováIkona}.</p> */
		public static boolean systemovaIkona(String popis,
			String... položkyPonuky)
		{ return systémováIkona(popis, null, položkyPonuky); }

		/**
		 * <p>Definuje alebo aktualizuje ikonu v systémovej oblasti. Pri
		 * prvom volaní metódy ide o vytvorenie ikony, vtedy nesmie byť
		 * ikona prázdna ({@code valnull}). Ďalšie podrobnosti čítajte
		 * v opise metódy {@link #systémováIkona(String, Image, String...)
		 * systémováIkona}{@code (popis, ikona, položkyPonuky)}.</p>
		 * 
		 * @param ikona obrázok určujúci vzhľad ikony v systémovej oblasti
		 * @param položkyPonuky voliteľný zoznam reťazcov, z ktorých bude
		 *     vytvorená kontextová ponuka ikony
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean systémováIkona(Image ikona,
			String... položkyPonuky)
		{ return systémováIkona(null, ikona, položkyPonuky); }

		/** <p><a class="alias"></a> Alias pre {@link #systémováIkona(Image, String...) systémováIkona}.</p> */
		public static boolean systemovaIkona(Image ikona,
			String... položkyPonuky)
		{ return systémováIkona(null, ikona, položkyPonuky); }


		/**
		 * <p>Zistí, či je jestvujúca ikona v systémovej oblasti zobrazená.</p>
		 * 
		 * @return booleovská hodnota určujúca, či je ikona viditeľná
		 */
		public static boolean systémováIkonaZobrazená()
		{
			if (null != systémováIkona)
			{
				if (SystemTray.isSupported()) try
				{
					SystemTray systémováOblasť = SystemTray.getSystemTray();
					TrayIcon[] ikony = systémováOblasť.getTrayIcons();
					for (TrayIcon ikona : ikony)
						if (systémováIkona == ikona) return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #systémováIkonaZobrazená() systémováIkonaZobrazená}.</p> */
		public static boolean systemovaIkonaZobrazena()
		{ return systémováIkonaZobrazená(); }

		/**
		 * <p>Zobrazí alebo skryje jestvujúcu ikonu v systémovej oblasti.</p>
		 * 
		 * @param zobraz booleovská hodnota určuje, či má byť ikona
		 *     zobrazená alebo skrytá
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean zobrazSystémovúIkonu(boolean zobraz)
		{
			// (Ak ikona nejestvuje, tak to nemá zmysel…)
			if (null != systémováIkona)
			{
				if (systémováIkonaZobrazená() == zobraz) return true;

				if (SystemTray.isSupported()) try
				{
					SystemTray systémováOblasť = SystemTray.getSystemTray();
					if (zobraz)
						systémováOblasť.add(systémováIkona);
					else
						systémováOblasť.remove(systémováIkona);
					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zobrazSystémovúIkonu(boolean) zobrazSystémovúIkonu}.</p> */
		public static boolean zobrazSystemovuIkonu(boolean zobraz)
		{ return zobrazSystémovúIkonu(zobraz); }


		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadanú
		 * správu s titulkom.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param správa text správy
		 * @param titulok text titulku
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean správaSystémovejIkony(String správa,
			String titulok)
		{
			if (null != systémováIkona) try
			{
				systémováIkona.displayMessage(titulok,
					správa, TrayIcon.MessageType.NONE);
				return true;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
			return false;
		}

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadanú
		 * správu.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param správa text správy
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean správaSystémovejIkony(String správa)
		{ return správaSystémovejIkony(správa, null); }

		/** <p><a class="alias"></a> Alias pre {@link #správaSystémovejIkony(String, String) správaSystémovejIkony}.</p> */
		public static boolean spravaSystemovejIkony(String správa,
			String titulok)
		{ return správaSystémovejIkony(správa, titulok); }

		/** <p><a class="alias"></a> Alias pre {@link #správaSystémovejIkony(String) správaSystémovejIkony}.</p> */
		public static boolean spravaSystemovejIkony(String správa)
		{ return správaSystémovejIkony(správa, null); }

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * informačné oznámenie s titulkom.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param informácia text informácie
		 * @param titulok text titulku
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean informáciaSystémovejIkony(String informácia,
			String titulok)
		{
			if (null != systémováIkona) try
			{
				systémováIkona.displayMessage(titulok,
					informácia, TrayIcon.MessageType.INFO);
				return true;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
			return false;
		}

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * informačné oznámenie.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param informácia text informácie
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean informáciaSystémovejIkony(String informácia)
		{ return informáciaSystémovejIkony(informácia, null); }

		/** <p><a class="alias"></a> Alias pre {@link #informáciaSystémovejIkony(String, String) informáciaSystémovejIkony}.</p> */
		public static boolean informaciaSystemovejIkony(String informácia,
			String titulok)
		{ return informáciaSystémovejIkony(informácia, titulok); }

		/** <p><a class="alias"></a> Alias pre {@link #informáciaSystémovejIkony(String) informáciaSystémovejIkony}.</p> */
		public static boolean informaciaSystemovejIkony(String informácia)
		{ return informáciaSystémovejIkony(informácia, null); }

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * varovné oznámenie s titulkom.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param varovanie text varovania
		 * @param titulok text titulku
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean varovanieSystémovejIkony(String varovanie,
			String titulok)
		{
			if (null != systémováIkona) try
			{
				systémováIkona.displayMessage(titulok,
					varovanie, TrayIcon.MessageType.WARNING);
				return true;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
			return false;
		}

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * varovné oznámenie.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param varovanie text varovania
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean varovanieSystémovejIkony(String varovanie)
		{ return varovanieSystémovejIkony(varovanie, null); }

		/** <p><a class="alias"></a> Alias pre {@link #varovanieSystémovejIkony(String, String) varovanieSystémovejIkony}.</p> */
		public static boolean varovanieSystemovejIkony(String varovanie,
			String titulok)
		{ return varovanieSystémovejIkony(varovanie, titulok); }

		/** <p><a class="alias"></a> Alias pre {@link #varovanieSystémovejIkony(String) varovanieSystémovejIkony}.</p> */
		public static boolean varovanieSystemovejIkony(String varovanie)
		{ return varovanieSystémovejIkony(varovanie, null); }

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * chybové oznámenie s titulkom.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param chyba text chyby
		 * @param titulok text titulku
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean chybaSystémovejIkony(String chyba,
			String titulok)
		{
			if (null != systémováIkona) try
			{
				systémováIkona.displayMessage(titulok,
					chyba, TrayIcon.MessageType.ERROR);
				return true;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
			return false;
		}

		/**
		 * <p>Ak je definovaná {@linkplain Svet#systémováIkona(String, Image,
		 * String...) systémová ikona}, tak v jej kontexte zobrazí zadané
		 * chybové oznámenie.</p>
		 * 
		 * <!-- TODO – ukážka vzhľadu na rôznych platformách. -->
		 * 
		 * @param chyba text chyby
		 * @return {@code valtrue} pri úspechu; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean chybaSystémovejIkony(String chyba)
		{ return chybaSystémovejIkony(chyba, null); }

		/** <p><a class="alias"></a> Alias pre {@link #chybaSystémovejIkony(String, String) chybaSystémovejIkony}.</p> */
		public static boolean chybaSystemovejIkony(String chyba,
			String titulok)
		{ return chybaSystémovejIkony(chyba, titulok); }

		/** <p><a class="alias"></a> Alias pre {@link #chybaSystémovejIkony(String) chybaSystémovejIkony}.</p> */
		public static boolean chybaSystemovejIkony(String chyba)
		{ return chybaSystémovejIkony(chyba, null); }


		/**
		 * <p>Skončí aplikáciu.</p>
		 */
		public static void koniec() { koniec(0); }

		/**
		 * <p>Skončí aplikáciu s návratovým kódom pre opreračný systém.</p>
		 * 
		 * @param kód celočíselný návratový kód, ktorý je schopný prijať
		 *     operačný systém
		 */
		public static void koniec(int kód)
		{
			final int kódKonca = kód;

			// Keď to nebolo schované v metóde invokeLater, tak v niektorých
			// prípadoch aplikácia „zatvrdla natvrdo“…
			SwingUtilities.invokeLater(() ->
			{
				/* System.out.println("Koniec"); */
				System.exit(kódKonca);
			});

			// Keď tu nebolo toto čakanie, tak sa stávalo, že aplikácia ešte
			// chvíľu po „skončení“ vykonávala svoje príkazy
			try { /*System.out.println("Čakám…");*/ Thread.sleep(500); }
			catch (InterruptedException ie)
			{ /*System.out.println("Chyba: " + ie.getMessage());*/ }
		}


		// Systémové

		/**
		 * <p>Vráti objekt typu {@link BufferedImage BufferedImage} obsahujúci
		 * aktuálnu obrazovú informáciu sveta. Vrátený objekt obsahuje
		 * zobrazenie spájajúce dohromady grafiky {@linkplain Plátno plátien},
		 * {@linkplain GRobot robotov}, {@linkplain GRobot#spojnica(GRobot)
		 * spojníc}, {@linkplain Svet#vypíš(Object...) konzolových textov}
		 * a tak podobne. Tento objekt je pravidelne aktualizovaný, takže
		 * akékoľvek zmeny v ňom budú mať krátke trvanie. Vrátený objekt je
		 * skôr vhodný na získanie aktuálnej verzie grafickej informácie
		 * sveta.</p>
		 * 
		 * <p>Vrátený objekt (typu {@link BufferedImage BufferedImage}) nemá
		 * prispôsobený súradnicový priestor potrebám programovacieho rámca
		 * GRobot – pracuje v rovnakom súradnicovom systéme ako všetky
		 * grafické objekty Javy – začiatok súradnicovej sústavy je umiestnený
		 * v ľavom hornom rohu obrázka a y-ové súradnice sú v porovnaní
		 * s klasickým kartéziánskym súradnicovým systémom zrkadlovo
		 * prevrátené, to znamená, že y-ová súradnica stúpa smerom nadol.</p>
		 * 
		 * <p>(O súradnicových priestoroch sa podrobnejšie píše napríklad
		 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
		 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
		 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
		 * v celej dokumentácii.)</p>
		 * 
		 * @return objekt typu {@link BufferedImage BufferedImage}
		 *     obsahujúci aktuálnu obrazovú informáciu sveta
		 * 
		 * @see #grafika()
		 */
		public static BufferedImage obrázok() { return obrázokSveta1; }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok() obrázok}.</p> */
		public static BufferedImage obrazok() { return obrázokSveta1; }

		/**
		 * <p>Vráti grafický objekt sveta na kreslenie v reakcii {@link 
		 * ObsluhaUdalostí#dokreslenie() ObsluhaUdalostí.dokreslenie()}.
		 * Tento objekt pracuje v súradnicovom priestore Javy a všetko, čo
		 * pomocou neho nakreslíte, bude na obrazovke zobrazené len do
		 * najbližšieho prekreslenia. Jeho používanie je vyhradené
		 * viac-menej iba pre skúsených programátorov. Žiadny robot nedokáže
		 * priamo kresliť do tohto objektu, ale niektoré metódy robotov môžu
		 * byť využité, napríklad na generovanie tvarov, ktoré kresliace
		 * metódy tohto objektu dokážu akceptovať.</p>
		 * 
		 * <p>(Objekty generované robotmi sú vygenerované tak, aby boli
		 * použiteľné v súradnicovom priestore Javy, teda aj tohto objektu.
		 * O súradnicových priestoroch sa podrobnejšie píše napríklad
		 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()},
		 * {@link SVGPodpora#zapíš(String, String, boolean)
		 * SVGpodpora.zapíš(…)}, {@link SVGPodpora#čítaj(String)
		 * SVGpodpora.čítaj(meno)} a priebežne v celej dokumentácii.)</p>
		 * 
		 * @see #obrázok()
		 */
		public static Graphics2D grafika() { return grafikaSveta1; }

		/**
		 * <p>Po pamäťovo náročných operáciách môže byť vhodné uvoľniť
		 * systémové prostriedky. Metóda sa pokúsi uvoľniť čo najviac
		 * nepoužívaných prostriedkov. Vráti informáciu o objeme uvoľnenej
		 * pamäte.</p>
		 * 
		 * @return množstvo uvoľnenej pamäte
		 */
		public static long údržba()
		{
			Runtime runtime = Runtime.getRuntime();
			long pred = runtime.freeMemory();
			runtime.gc();
			long po = runtime.freeMemory();
			return po - pred;
		}

		/** <p><a class="alias"></a> Alias pre {@link #údržba() údržba}.</p> */
		public static long udrzba() { return údržba(); }


		/**
		 * <p>Metóda slúži na odstránenie konkrétneho robota (alebo inštancie
		 * odvodeného typu) z vnútorného zoznamu robotov. Prijíma jeden
		 * argument typu {@link GRobot GRobot} (to znamená, že môže ísť aj
		 * o hocijakú inštanciu odvodeného údajového typu). Zadaný objekt
		 * bude potom vymazaný z vnútorného zoznamu robotov. Keď
		 * aplikácia zabezpečí aj vynulovanie všetkých ostatných premenných,
		 * v ktorých je stanovená inštancia uložená, objekt by mal byť uvoľnený
		 * z pamäte počítača zberačom odpadkov Javy. <b>Uvoľneného robota už
		 * nie je možné do sveta vrátiť!</b> V prípade, že je uvoľnený
		 * {@linkplain #hlavnýRobot() hlavný robot}, nastúpi na jeho miesto
		 * najbližší jestvujúci robot. Problém by mohol nastať, keby nie je
		 * k dispozícii žiadny ďalší robot, ktorý by ho mohol nahradiť.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
		 * v programovacom rámci GRobot deväť rôznych metód:
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
		 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
		 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
		 * oblasť (čo je geometrická trieda),
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
		 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
		 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
		 * Svet#upevni() Svet.upevni}) a nakoniec
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
		 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
		 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
		 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
		 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
		 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
		 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
		 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
		 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
		 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
		 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
		 * disku zmenil).</p>
		 * 
		 * @param ktorý objekt, ktorý má byť odstránený
		 * 
		 * @see #uvoľni(java.lang.Class)
		 */
		public static void uvoľni(GRobot ktorý)
		{
			for (GRobot tento : GRobot.zoznamRobotov)
			{
				for (int i = 0; i < tento.spojnice.size(); ++i)
				{
					GRobot.Spojnica s = tento.spojnice.get(i);
					if (s.cieľ == ktorý) tento.spojnice.remove(i--);
				}
			}

			int index1 = GRobot.zoznamRobotov.indexOf(ktorý),
				index2 = GRobot.zoznamRobotov.size() - 1;
			GRobot.Vrstva.vymaž(index1, index2);

			ktorý.meno(null);
			ktorý.uvoľni();
			ktorý.spojnice.clear();
			GRobot.zoznamRobotov.remove(ktorý);
			GRobot.záložnýZoznamRobotov.remove(ktorý);

			GRobot.Vrstva.vlož(index1, --index2);

			if (ktorý == hlavnýRobot)
			{
				hlavnýRobot = null;
				if (GRobot.zoznamRobotov.size() > 0)
					hlavnýRobot = GRobot.zoznamRobotov.get(0);
				else if (GRobot.záložnýZoznamRobotov.size() > 0)
					hlavnýRobot = GRobot.záložnýZoznamRobotov.get(0);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uvoľni(GRobot) uvoľni}.</p> */
		public static void uvolni(GRobot ktorý) { uvoľni(ktorý); }

		/**
		 * <p>Metóda slúži na hromadné vymazanie robotov určitého typu
		 * z vnútorného zoznamu robotov. Očakáva určenie údajového typu
		 * prostredníctvom inštancie typu {@link java.lang.Class Class}. Tú
		 * môžeme získať, napríklad takto: {@code GRobot.class}, {@code 
		 * Postavička.class}…). Zo zoznamu budú v dôsledku toho vymazané všetky
		 * inštancie určeného typu, ale aj inštancie odvodených tried. Preto
		 * je dobré byť pri hromadnom odstraňovaní obozretný. Ak si nie ste
		 * istí, aký dopad by malo použitie tejto metódy, použite radšej na
		 * uvoľnenie konkrétnych robotov metódu {@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )}. <b>Uvoľnených
		 * robotov už nie je možné do sveta vrátiť!</b> V prípade, že nastane
		 * uvoľnenie {@linkplain #hlavnýRobot() hlavného robota}, nastúpi na
		 * jeho miesto najbližší jestvujúci robot. Problém by mohol nastať,
		 * keby nie je k dispozícii žiadny ďalší robot, ktorý by ho mohol
		 * nahradiť.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
		 * v programovacom rámci GRobot deväť rôznych metód:
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
		 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
		 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
		 * oblasť (čo je geometrická trieda),
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
		 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
		 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
		 * Svet#upevni() Svet.upevni}) a nakoniec
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
		 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
		 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
		 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
		 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
		 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
		 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
		 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
		 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
		 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
		 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
		 * disku zmenil).</p>
		 * 
		 * @param typ objekt typu {@link java.lang.Class Class}
		 * 
		 * @see #uvoľni(GRobot)
		 */
		@SuppressWarnings("rawtypes")
		public static void uvoľni(Class typ)
		{
			GRobot.Vrstva.vymaž(0, GRobot.zoznamRobotov.size() - 1);

			for (int i = 0; i < GRobot.zoznamRobotov.size(); ++i)
			{
				GRobot ktorý = GRobot.zoznamRobotov.get(i);
				if (typ.isInstance(ktorý))
				{
					for (GRobot tento : GRobot.zoznamRobotov)
					{
						for (int j = 0; j < tento.spojnice.size(); ++j)
						{
							GRobot.Spojnica s = tento.spojnice.get(j);
							if (s.cieľ == ktorý) tento.spojnice.remove(j--);
						}
					}

					ktorý.meno(null);
					ktorý.uvoľni();
					GRobot.zoznamRobotov.remove(i--);
					GRobot.záložnýZoznamRobotov.remove(ktorý);
					if (ktorý == hlavnýRobot) hlavnýRobot = null;
				}
			}

			GRobot.Vrstva.vlož(0, GRobot.zoznamRobotov.size() - 1);

			if (null == hlavnýRobot)
			{
				if (GRobot.zoznamRobotov.size() > 0)
					hlavnýRobot = GRobot.zoznamRobotov.get(0);
				else if (GRobot.záložnýZoznamRobotov.size() > 0)
					hlavnýRobot = GRobot.záložnýZoznamRobotov.get(0);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uvoľni(Class) uvoľni}.</p> */
		@SuppressWarnings("rawtypes")
		public static void uvolni(Class typ) { uvoľni(typ); }


		/**
		 * <p>Táto metóda slúži na odstránenie konkrétneho obrázka z vnútorného
		 * zoznamu obrázkov. Prijíma jeden argument typu {@link Obrázok
		 * Obrázok}, ktorý určuje objekt určený na vymazanie z vnútorného
		 * zoznamu obrázkov. Aplikácia musí zabezpečiť vynulovanie všetkých
		 * premenných, v ktorých je inštancia tohto obrázka uložená, pretože
		 * po vymazaní obrázka z vnútorného zoznamu prestane byť objekt
		 * synchronizovaný so zvyškom programovacieho rámca a mohol by sa správať
		 * inak než by sa od neho očakávalo. <b>Uvoľnený obrázok už nie je možné
		 * do sveta vrátiť!</b></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
		 * v programovacom rámci GRobot deväť rôznych metód:
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
		 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
		 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
		 * oblasť (čo je geometrická trieda),
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
		 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
		 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
		 * Svet#upevni() Svet.upevni}) a nakoniec
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
		 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
		 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
		 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
		 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
		 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
		 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
		 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
		 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
		 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
		 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
		 * disku zmenil).</p>
		 * 
		 * @param ktorý objekt, ktorý má byť odstránený z vnútorného zoznamu
		 */
		public static void uvoľni(Obrázok ktorý)
		{ Obrázok.zoznamObrázkovKnižnice.remove(ktorý); }

		/** <p><a class="alias"></a> Alias pre {@link #uvoľni(Obrázok) uvoľni}.</p> */
		public static void uvolni(Obrázok ktorý) { uvoľni(ktorý); }


		/**
		 * <p>Táto metóda slúži na odstránenie zdrojov (obrázkov a/alebo zvukov)
		 * z vnútorných zoznamov zdrojov sveta. Argument určuje názov súboru,
		 * z ktorého bol objekt zdroja prečítaný. Podľa neho sú vo vnútorných
		 * zoznamoch sveta (označovaných v tejto dokumentácii aj termínom
		 * „vnútorná pamäť sveta“) vyhľadané všetky korešpondujúce záznamy
		 * a sú z nich vymazané. Pri najbližšom čítaní tohto zdroja bude
		 * zdroj opätovne prečítaný z pevného disku (zdrojového súboru)
		 * a nová verzia bude opäť uložená do vnútorného zoznamu zdrojov
		 * sveta.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názov {@code curruvoľni} má
		 * v programovacom rámci GRobot deväť rôznych metód:
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link GRobot GRobot}{@code .}{@link GRobot#uvoľni(Oblasť)
		 * uvoľni}{@code (}{@link Oblasť Oblasť}{@code )},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni()
		 * uvoľni}{@code ()},
		 * {@link Oblasť Oblasť}{@code .}{@link Oblasť#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )} –
		 * slúžia na uvoľnenie robota zo zamestnania pre stanovenú
		 * oblasť (čo je geometrická trieda),
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni() uvoľni}{@code ()} –
		 * slúži na uvoľnenie hlavného okna sveta, t. j. umožnenie
		 * zmeny veľkosti okna používateľovi (ide o opak metódy {@link 
		 * Svet#upevni() Svet.upevni}) a nakoniec
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(GRobot)
		 * uvoľni}{@code (}{@link GRobot GRobot}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.Class)
		 * uvoľni}{@code (}{@link java.lang.Class Class}{@code )},
		 * {@link Svet Svet}{@code .}{@link Svet#uvoľni(Obrázok)
		 * uvoľni}{@code (}{@link Obrázok Obrázok}{@code )}
		 * a {@link Svet Svet}{@code .}{@link Svet#uvoľni(java.lang.String)
		 * uvoľni}{@code (}{@link java.lang.String String}{@code )} –
		 * slúžia na uvoľňovanie nepotrebných inštancií robotov,
		 * vytvorených alebo prečítaných obrázkov a prečítaných zvukov
		 * z vnútorných zoznamov zdrojov (robotov, obrázkov, zvukov), čo
		 * je jednak nevyhnutnou podmienkou ich úspešného vymazania
		 * z pamäte zberačom odpadkov Javy a jednak to môže byť niekedy
		 * potrebné (napríklad ak sa obsah súboru so zdrojom uloženým na
		 * disku zmenil).</p>
		 * 
		 * @param názovZdroja názov súboru so zdrojom, ktorý má byť uvoľnený
		 *     z vnútornej pamäte sveta
		 */
		public static void uvoľni(String názovZdroja)
		{
			int indexOf;

			// Vyhľadáme zdroj vo všetkých vnútorných zoznamoch a odstránime
			// ho odtiaľ…

			if (-1 != (indexOf = Obrázok.zoznamSúborovObrázkov.
				indexOf(názovZdroja)))
			{
				Obrázok.zoznamSúborovObrázkov.remove(indexOf);
				Obrázok.zoznamObrázkov.remove(indexOf);
				Obrázok.zoznamIkon.remove(indexOf);
			}

			Zvuk.ZoznamZvukov zoznamZvukov =
				Zvuk.zoznamSúborovZvukov.get(názovZdroja);
			if (null != zoznamZvukov)
			{
				for (Zvuk zvuk : zoznamZvukov) zvuk.uvoľni();
				zoznamZvukov.clear();
				Zvuk.zoznamSúborovZvukov.remove(názovZdroja);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uvoľni(String) uvoľni}.</p> */
		public static void uvolni(String názovZdroja) { uvoľni(názovZdroja); }


		/**
		 * <p>Vráti inštanciu konfiguračného súboru na účely úpravy jeho parametrov,
		 * napríklad na úpravu predvolenej sekcie automatickej konfigurácie.
		 * Neodporúčame túto inštanciu používať na vlastné účely! Tiež odporúčame
		 * vykonávať čo najmenej zásahov do jej vlastností…</p>
		 * 
		 * @return inštancia konfiguračného súboru
		 */
		public static Súbor konfiguračnýSúbor() { return konfiguračnýSúbor; }

		/** <p><a class="alias"></a> Alias pre {@link #konfiguračnýSúbor() konfiguračnýSúbor}.</p> */
		public static Súbor konfiguracnySubor() { return konfiguračnýSúbor; }

		/**
		 * <p>Vráti naposledy použitý názov konfiguračného súboru. Pozri aj
		 * napríklad metódu {@link #konfiguračnýSúbor() konfiguračnýSúbor}
		 * alebo reakciu {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor)
		 * zapíšKonfiguráciu}.</p>
		 * 
		 * @return názov konfiguračného súboru
		 */
		public static String názovKonfiguračnéhoSúboru() { return názovKonfiguračnéhoSúboru; }

		/** <p><a class="alias"></a> Alias pre {@link #názovKonfiguračnéhoSúboru() názovKonfiguračnéhoSúboru}.</p> */
		public static String nazovKonfiguracnehoSuboru() { return názovKonfiguračnéhoSúboru; }


		/**
		 * <p>Vráti názov predvolenej sekcie. (Pozri aj
		 * {@link #predvolenáSekciaKonfigurácie(String)
		 * predvolenáSekciaKonfigurácie}.)
		 * Prázdny reťazec označuje prvú bezmennú konfiguračnú pasáž.</p>
		 * 
		 * @return názov predvolenej sekcie alebo prázdny reťazec
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu()
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static String predvolenáSekciaKonfigurácie()
		{
			return predvolenáSekciaKonfigurácie;
			// if (null == predvolenáSekcia.názov)
			// {
			// 	predvolenáSekcia.názov = "";
			// 	predvolenáSekcia.haš = predvolenáSekcia.názov.hashCode();
			// }
			// return predvolenáSekcia.názov;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáSekciaKonfigurácie() predvolenáSekciaKonfigurácie}.</p> */
		public static String predvolenaSekciaKonfiguracie()
		{ return predvolenáSekciaKonfigurácie(); }

		/**
		 * <p>Zmení názov predvolenej sekcie, ktorú používajú metódy automatickej
		 * konfigurácie.</p>
		 * 
		 * @param názov názov novej predvolenej sekcie
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie()
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu()
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void predvolenáSekciaKonfigurácie(String názov)
		{
			if (null == názov) názov = "";
			názov = názov.trim();
			Súbor.overPlatnosťNázvuSekcie(názov);
			predvolenáSekciaKonfigurácie = názov;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáSekciaKonfigurácie(String) predvolenáSekciaKonfigurácie}.</p> */
		public static void predvolenaSekciaKonfiguracie(String názov)
		{ predvolenáSekciaKonfigurácie(názov); }


		/**
		 * <p>Zapne {@linkplain Svet#použiKonfiguráciu(String) automatickú
		 * konfiguráciu}, pričom umožní zadať počiatočnú polohu a rozmery
		 * okna. Parametre polohy a rozmerov okna opísané nižšie určujú
		 * predvolené hodnoty údajov – neskôr budú nahradené aktuálnymi
		 * hodnotami uloženými v konfigurácii. Súradnice {@code x} a {@code y}
		 * určujú, o koľko bodov je posunutý ľavý horný roh okna od ľavého
		 * horného rohu obrazovky (y-ová súradnica rastie smerom nadol).
		 * Viac informácií je v opise metódy {@link #použiKonfiguráciu(String)
		 * použiKonfiguráciu(názovSúboru)}.</p>
		 * 
		 * @param názovSúboru názov konfiguračného súboru ({@code .cfg})
		 * @param x predvolená (pozri vyššie) x-ová súradnica
		 *     polohy okna
		 * @param y predvolená (pozri vyššie) y-ová súradnica
		 *     polohy okna
		 * @param šírka predvolená (pozri vyššie) šírka okna
		 * @param výška predvolená (pozri vyššie) výška okna
		 * 
		 * @throws GRobotException ak svet už bol inicializovaný
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie()
		 * @see #predvolenáSekciaKonfigurácie(String)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu()
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void použiKonfiguráciu(String názovSúboru,
			int x, int y, int šírka, int výška)
		{
			if (inicializované)
				throw new GRobotException(
				"Konfiguráciu nie je možné použiť. " +
				"Svet už bol inicializovaný!", "configNotApplicable");

			// Poznámka: Pri zápise štandardných súborov typu konfiguračné
			//     súbory, denníky a podobné záležitosti, ktoré majú
			//     štandardnú príponu vždy kontrolujte, či je táto prípona
			//     uvedená v názve súboru a ak nie, tak ju pripojte. Je to
			//     bezpečnejšie. Jeden príklad za všetky: Predstavte si, že
			//     programátor píše kód, na začiatku ktorého registruje
			//     konfiguračný súbor s rovnakým názvom ako názov hlavnej
			//     triedy. Či už z dôvodu nočnej únavy, alebo nedostatku
			//     rannej kávy v úsilí zjednodušiť si veci skopíruje názov
			//     z dialógu triedy Uložiť ako… a zabudne prepísať príponu
			//     z .java na .cfg. Neuvedomí si to a program spustí. Po
			//     ukončení programu s úžasom zistí, že zdrojový kód jeho
			//     triedy je fuč a namiesto neho tam má zapísanú konfiguráciu.
			//     Ak to bola prvá vec, ktorú v hlavnej triede napísal, tak
			//     veľa nestratil, ak mal zdrojový kód zálohovaný, tiež asi
			//     veľa nestratil, ale ak my zabezpečíme, pridanie korektnej
			//     prípony, tak strata ani nenastane.
			// TODO – poznač medzi „postrehy programátora“.
			if (!názovSúboru.endsWith(".cfg")) názovSúboru += ".cfg";

			Súbor.Sekcia pôvodnáSekcia = konfiguračnýSúbor.aktívnaSekcia;
			String mennýPriestor = konfiguračnýSúbor.aktívnaSekcia.
				mennýPriestorVlastností;

			// Súbor je otvorený len raz, pretože je pravdepodobné,
			// že sa z neho bude veľa čítať. Aj tak je pri ukončení
			// a zápise zatvorený a opätovne otvorený.
			try
			{
				konfiguračnýSúbor.otvorNaČítanie(
					názovKonfiguračnéhoSúboru = názovSúboru);

				pôvodnáSekcia = konfiguračnýSúbor.aktívnaSekcia;
				konfiguračnýSúbor.aktivujSekciu(
					predvolenáSekciaKonfigurácie);
				// konfiguračnýSúbor.dumpSections("Počas čítania.");
				mennýPriestor = konfiguračnýSúbor.
					aktívnaSekcia.mennýPriestorVlastností;

				// Konfigurácia okna
				try
				{
					konfiguračnýSúbor.aktívnaSekcia.
						mennýPriestorVlastností = "okno";

					poslednéX = počiatočnéX = konfiguračnýSúbor.
						čítajVlastnosť("x", // windowX
							// Long.valueOf(x)).intValue();
							x);
					poslednéY = počiatočnéY = konfiguračnýSúbor.
						čítajVlastnosť("y", // windowY
							// Long.valueOf(y)).intValue();
							y);

					poslednáŠírka = počiatočnáŠírka = konfiguračnýSúbor.
						čítajVlastnosť("šírka", // windowWidth
							// Long.valueOf(šírka)).intValue();
							šírka);
					poslednáVýška = počiatočnáVýška = konfiguračnýSúbor.
						čítajVlastnosť("výška", // windowHeight
							// Long.valueOf(výška)).intValue();
							výška);

					počiatočnýStav = NORMAL;
					if (konfiguračnýSúbor.čítajVlastnosť(
						"minimalizované", false))
						počiatočnýStav |= ICONIFIED;
					if (konfiguračnýSúbor.čítajVlastnosť(
						"maximalizované", false))
						počiatočnýStav |= MAXIMIZED_BOTH;
				}
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e); }

				// Čítanie histórie vstupného riadka sa deje bez ohľadu na
				// jej aktiváciu; do úvahy sa berie existencia a hodnota
				// atribútu dĺžkaHistórie

				try
				{
					konfiguračnýSúbor.aktívnaSekcia.
						mennýPriestorVlastností = "história";

					int dĺžkaHistórie = konfiguračnýSúbor.
						čítajVlastnosť("dĺžka", -1);

					if (dĺžkaHistórie >= 0)
					{
						// Prečítanie a vloženie neprázdnych príkazov histórie:
						for (int i = 0; i < dĺžkaHistórie; ++i)
						{
							String položka = konfiguračnýSúbor.
								čítajVlastnosť("riadok[" + i + "]", "");

							if (null != položka && !položka.isEmpty())
								históriaVstupnéhoRiadka.pridaj(položka);
						}

						// Nastavenie počítadla za poslednú položku histórie:
						históriaVstupnéhoRiadka.počítadlo(
							históriaVstupnéhoRiadka.dĺžka());
					}
				}
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e); }

				prvéSpustenie = false;
			}
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); }
			finally
			{
				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = mennýPriestor;
				konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia;
			}

			// try { konfiguračnýSúbor.zavri(); }
			// catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
		}

		/** <p><a class="alias"></a> Alias pre {@link #použiKonfiguráciu(String, int, int, int, int) použiKonfiguráciu}.</p> */
		public static void pouziKonfiguraciu(String názovSúboru,
			int x, int y, int šírka, int výška)
		{ použiKonfiguráciu(názovSúboru, x, y, šírka, výška); }

		/**
		 * <p>Zapne {@linkplain Svet#použiKonfiguráciu(String) automatickú
		 * konfiguráciu}, pričom umožní zadať počiatočnú polohu a rozmery
		 * okna. Metóda použije predvolené meno konfiguračného súboru {@code 
		 * srg"grobot.cfg"}. Parametre polohy a rozmerov okna opísané nižšie
		 * určujú predvolené hodnoty údajov – neskôr budú nahradené aktuálnymi
		 * hodnotami uloženými v konfigurácii. Súradnice {@code x} a {@code y}
		 * určujú, o koľko bodov je posunutý ľavý horný roh okna od ľavého
		 * horného rohu obrazovky (y-ová súradnica rastie smerom nadol).
		 * Viac informácií je v opise metódy {@link #použiKonfiguráciu()
		 * použiKonfiguráciu()}.</p>
		 * 
		 * @param x predvolená (pozri vyššie) x-ová súradnica
		 *     polohy okna
		 * @param y predvolená (pozri vyššie) y-ová súradnica
		 *     polohy okna
		 * @param šírka predvolená (pozri vyššie) šírka okna
		 * @param výška predvolená (pozri vyššie) výška okna
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie()
		 * @see #predvolenáSekciaKonfigurácie(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu()
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void použiKonfiguráciu(
			int x, int y, int šírka, int výška)
		{ použiKonfiguráciu(predvolenýNázovKonfiguračnéhoSúboru,
			x, y, šírka, výška); }

		/** <p><a class="alias"></a> Alias pre {@link #použiKonfiguráciu(int, int, int, int) použiKonfiguráciu}.</p> */
		public static void pouziKonfiguraciu(
			int x, int y, int šírka, int výška)
		{ použiKonfiguráciu(predvolenýNázovKonfiguračnéhoSúboru,
			x, y, šírka, výška); }

		/**
		 * <p>Zapne automatické spracovanie konfiguračného súboru sveta. Metóda
		 * musí byť volaná pred vytvorením {@linkplain #hlavnýRobot()
		 * hlavného robota}, čiže ešte pred začatím inicializácie sveta
		 * (najlepšie v hlavnej metóde).
		 * V súčasnosti konfigurácia sveta zahŕňa polohu a rozmery hlavného
		 * okna (vrátane stavu minimalizovania/maximalizovania okna)
		 * a ukladanie vlastností registrovaných robotov. Meno
		 * konfiguračného súboru určuje parameter {@code 
		 * názovSúboru}. Súbor nemusí jestvovať, v takom prípade bude
		 * vytvorený automaticky pri ukončení aplikácie.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdpublic static} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@code currpoužiKonfiguráciu}({@code srg"grobot.cfg"});
				{@code comm// new …}
			}
			</pre>
		 * 
		 * <p>(Táto metóda použije predvolené hodnoty polohy [25, 25]
		 * a rozmerov 600 × 500 bodov.)</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Upozorňujeme na to, že
		 * nie je bezpečné používať súbor so zadaným názvom na iné účely.
		 * Prosím, venujte pozornosť upozorneniu pri metóde {@link 
		 * Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}!</p>
		 * 
		 * @param názovSúboru názov konfiguračného súboru ({@code .cfg})
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie()
		 * @see #predvolenáSekciaKonfigurácie(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #použiKonfiguráciu()
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void použiKonfiguráciu(String názovSúboru)
		{
			použiKonfiguráciu(názovSúboru, počiatočnéX,
				počiatočnéY, počiatočnáŠírka, počiatočnáVýška);
		}

		/** <p><a class="alias"></a> Alias pre {@link #použiKonfiguráciu(String) použiKonfiguráciu}.</p> */
		public static void pouziKonfiguraciu(String názovSúboru)
		{ použiKonfiguráciu(názovSúboru); }

		/**
		 * <p>Zapne automatické spracovanie konfiguračného súboru sveta. Metóda
		 * musí byť volaná pred vytvorením {@linkplain #hlavnýRobot()
		 * hlavného robota}, čiže ešte pred začatím inicializácie sveta
		 * (najlepšie v hlavnej metóde).
		 * V súčasnosti konfigurácia sveta zahŕňa polohu a rozmery hlavného
		 * okna (vrátane stavu minimalizovania/maximalizovania okna)
		 * a ukladanie vlastností registrovaných robotov. Metóda
		 * použije predvolené meno konfiguračného súboru {@code 
		 * srg"grobot.cfg"}. Súbor nemusí jestvovať, v takom prípade bude
		 * vytvorený automaticky pri ukončení aplikácie.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdpublic static} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@code currpoužiKonfiguráciu}();
				{@code comm// new …}
			}
			</pre>
		 * 
		 * <p>(Táto metóda použije predvolené hodnoty polohy [25, 25]
		 * a rozmerov 600 × 500 bodov.)</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Upozorňujeme na to, že
		 * nie je bezpečné používať súbor {@code srg"grobot.cfg"} na iné
		 * účely. Prosím, venujte pozornosť upozorneniu pri metóde {@link 
		 * Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}!</p>
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see #predvolenáSekciaKonfigurácie()
		 * @see #predvolenáSekciaKonfigurácie(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #použiKonfiguráciu(String)
		 * @see #registrujRobota(GRobot, String)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void použiKonfiguráciu()
		{ použiKonfiguráciu(predvolenýNázovKonfiguračnéhoSúboru); }

		/** <p><a class="alias"></a> Alias pre {@link #použiKonfiguráciu() použiKonfiguráciu}.</p> */
		public static void pouziKonfiguraciu()
		{ použiKonfiguráciu(predvolenýNázovKonfiguračnéhoSúboru); }

		/**
		 * <p>Účelom tejto metódy je overiť, či pred spustením aplikácie
		 * jestvoval konfiguračný súbor. Ak áno, nejde o prvé spustenie
		 * a metóda vráti hodnotu {@code valfalse}. Ak nie, tak je toto
		 * spustenie považované za prvé a aplikácia si v tejto situácii
		 * môže nakonfigurovať niektoré vlastnosti alebo vykonať určité
		 * aktivity (napríklad {@linkplain #zbaľ() zbaliť okno}) bez obáv,
		 * že by táto činnosť zničila údaje prečítané z konfiguračného
		 * súboru.</p>
		 * 
		 * @return {@code valtrue} ak konfigurácia nejestvovala; {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean prvéSpustenie() { return prvéSpustenie; }

		/** <p><a class="alias"></a> Alias pre {@link #prvéSpustenie() prvéSpustenie}.</p> */
		public static boolean prveSpustenie() { return prvéSpustenie; }

		/**
		 * <p>Registruje {@linkplain #hlavnýRobot() hlavného robota}
		 * v {@linkplain #použiKonfiguráciu(String) konfigurácii} sveta.
		 * Rezervované meno pre hlavného robota je {@code robot}.
		 * Pozri aj informácie pri metóde {@link #registrujRobota(GRobot)
		 * registrujRobota(robot)}.</p>
		 * 
		 * @param meno meno robota
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see GRobot#uložDoSúboru(Súbor)
		 * @see GRobot#čítajZoSúboru(Súbor)
		 * @see Písmo#uložDoSúboru(Súbor)
		 * @see Písmo#čítajZoSúboru(Súbor)
		 * @see #registrujRobota(String)
		 * @see #registrujRobota(GRobot)
		 * @see #registrujRobota(GRobot, String)
		 * @see #použiKonfiguráciu()
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void registrujRobota() { registrujRobota(hlavnýRobot); }

		/**
		 * <p>Registruje robota v {@linkplain #použiKonfiguráciu(String)
		 * konfigurácii} podľa jeho (vopred priradeného)
		 * {@linkplain GRobot#meno(String) mena}.
		 * Ak nejestvuje žiadny robot so zadaným menom, tak vznikne výnimka.
		 * Pozri aj informácie pri metóde {@link #registrujRobota(GRobot)
		 * registrujRobota(robot)}.</p>
		 * 
		 * @param meno meno robota
		 * 
		 * @throws GRobotException ak robot so zadaným menom nejestvuje
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see GRobot#uložDoSúboru(Súbor)
		 * @see GRobot#čítajZoSúboru(Súbor)
		 * @see Písmo#uložDoSúboru(Súbor)
		 * @see Písmo#čítajZoSúboru(Súbor)
		 * @see #registrujRobota()
		 * @see #registrujRobota(GRobot)
		 * @see #registrujRobota(GRobot, String)
		 * @see #použiKonfiguráciu()
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void registrujRobota(String meno)
		{
			GRobot robot = GRobot.menáRobotov.get(meno);
			if (null == robot) throw new GRobotException(
				"Robot so zadaným menom (" + meno + ") nejestvuje.",
				"noRobotWithSuchName", meno, new IllegalArgumentException());
			registrujRobota(robot);
		}

		/**
		 * <p>Registruje robota v {@linkplain #použiKonfiguráciu(String)
		 * konfigurácii}. Registrácia je vykonaná podľa jeho
		 * {@linkplain GRobot#meno(String) mena}. To znamená, že údaje
		 * o robotovi (poloha, smer, veľkosť, farba pera a tak ďalej) budú
		 * automaticky ukladané do a čítané z konfiguračného súboru.
		 * Ak robot nie je pomenovaný a nejde o {@linkplain #hlavnýRobot()
		 * hlavného robota}, tak vznikne výnimka.</p>
		 * 
		 * <p>Pred registráciou je vhodné vypnúť {@linkplain Svet#nekresli()
		 * automatické prekresľovanie} a po dokončení registrácie ho opätovne
		 * zapnúť. I tak nemusí byť garantovaná bezproblémová aktualizácia
		 * obrazovky, preto je vhodné vykonať registráciu podľa nasledujúcej
		 * schémy:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} UkážkaKonfigurácie {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} UkážkaKonfigurácie()
				{
					{@code comm// Inicializácia}
					{@code comm// ...}

					{@link Svet Svet}.{@code currregistrujRobota}({@code valthis});
					<code class="comment">// Prípadne sa dá použiť aj verzia metódy</code>
					<code class="comment">// bez argumentu: Svet.{@link #registrujRobota() registrujRobota}();</code>
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link #použiKonfiguráciu() použiKonfiguráciu}();
					{@link Svet Svet}.{@link #nekresli() nekresli}();
					{@code kwdnew} UkážkaKonfigurácie();
					{@link Svet Svet}.{@link #kresli() kresli}();
				}
			}
			</pre>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Čítanie údajov o robotovi je
		 * vykonané pri spustení tohto príkazu a zápis je vykonaný automaticky
		 * pri ukončení aplikácie. V prípade kombinácie registrácie robota
		 * s vlastnou konfiguráciou (pozri príklad {@code TestKonfigurácie}
		 * v triede {@link ObsluhaUdalostí ObsluhaUdalostí}) je vhodné tieto
		 * fakty zvážiť.</p>
		 * 
		 * @param robot robot, ktorý má byť zaregistrovaný
		 * 
		 * @throws GRobotException ak zadaný robot nemá definované meno
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see GRobot#uložDoSúboru(Súbor)
		 * @see GRobot#čítajZoSúboru(Súbor)
		 * @see Písmo#uložDoSúboru(Súbor)
		 * @see Písmo#čítajZoSúboru(Súbor)
		 * @see GRobot#registrujVKonfigurácii()
		 * @see GRobot#registrujVKonfigurácii(String)
		 * @see GRobot#jeRegistrovaný()
		 * @see #registrujRobota()
		 * @see #registrujRobota(String)
		 * @see #registrujRobota(GRobot, String)
		 * @see #dajRobota(String)
		 * @see #použiKonfiguráciu()
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 * @see #čítajKonfiguráciuSveta()
		 */
		public static void registrujRobota(GRobot robot)
		{
			if (null == robot) return;

			if (null == robot.menoRobota && robot != hlavnýRobot)
				throw new GRobotException(
					"Zadaný robot nemá meno.", "robotHasNoName");

			robot.jeRegistrovaný = true;

			try
			{
				// konfiguračnýSúbor.otvorNaČítanie(názovKonfiguračnéhoSúboru);
				robot.čítajZoSúboru(konfiguračnýSúbor);
			}
			catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }

			// try { konfiguračnýSúbor.zavri(); }
			// catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
		}

		/**
		 * <p>Registruje robota v {@linkplain #použiKonfiguráciu(String)
		 * konfigurácii} podľa zadaného {@linkplain GRobot#meno(String) mena}.
		 * Pozri aj informácie pri metóde {@link #registrujRobota(GRobot)
		 * registrujRobota(robot)}.</p>
		 * 
		 * @param meno meno robota
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see GRobot#uložDoSúboru(Súbor)
		 * @see GRobot#čítajZoSúboru(Súbor)
		 * @see #registrujRobota()
		 * @see #registrujRobota(String)
		 * @see #registrujRobota(GRobot)
		 * @see #dajRobota(String)
		 * @see #použiKonfiguráciu()
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 */
		public static void registrujRobota(GRobot robot, String meno)
		{
			robot.meno(meno);
			registrujRobota(robot);
		}

		/**
		 * <p>Vráti inštanciu robota podľa jeho mena. Pomenovanie robotov
		 * je nepovinné a dá sa vykonať rôznymi spôsobmi. Napríklad pri
		 * {@linkplain #registrujRobota(GRobot, String) registrácii}
		 * robota v {@linkplain Svet#použiKonfiguráciu(String) konfigurácii
		 * sveta} alebo priamo jeho {@linkplain GRobot#meno(String)
		 * pomenovaním}.</p>
		 * 
		 * <p>Hodnota parametra {@code valnull} vráti {@linkplain 
		 * #hlavnýRobot() hlavného robota} bez ohľadu na jeho aktuálne meno.
		 * V prípade, že hlavný robot nemá priradené špeciálne meno, je
		 * táto hodnota ekvivalentá hodnote {@code srg"robot"}, ktorá vráti
		 * hlavného robota len v prípade, že nemá špeciálne pomenovanie.</p>
		 * 
		 * @param meno meno robota, ktorého inštanciu chceme vrátiť alebo
		 *     {@code valnull}
		 * @return inštancia robota so zadaným menom alebo {@code valnull},
		 *     ak taký robot nejestvuje
		 * 
		 * @see GRobot#meno(String)
		 * @see #registrujRobota(GRobot, String)
		 */
		public static GRobot dajRobota(String menoRobota)
		{
			if (null == menoRobota) return hlavnýRobot;
			if (menoRobota.equals("robot") && null != hlavnýRobot &&
				null == hlavnýRobot.menoRobota) return hlavnýRobot;
			return GRobot.menáRobotov.get(menoRobota);
		}


		/**
		 * <p>Prečíta {@linkplain #použiKonfiguráciu(String) konfiguráciu}
		 * sveta (a plátien). Konfigurácia sveta ukladá viaceré informácie,
		 * napríklad: {@linkplain #farbaPozadia(Color) farbu
		 * pozadia}, {@linkplain Písmo#čítajZoSúboru(Súbor) písma}
		 * {@linkplain GRobot#podlaha podlahy} a {@linkplain GRobot#strop
		 * stropu}, {@linkplain #spustiČasovač(double) hodnotu časovača},
		 * {@linkplain #interaktívnyRežim(boolean) aktiváciu interaktívneho
		 * režimu} a podobne. Spustenie tejto metódy spôsobí automatické
		 * uloženie údajov o svete (a plátnach) do konfigurácie pri
		 * (korektnom) ukončení aplikácie.</p>
		 * 
		 * <p>Vhodné je použiť nasledujúcu schému:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} UkážkaKonfigurácie {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} UkážkaKonfigurácie()
				{
					{@code comm// Inicializácia}
					{@code comm// ...}

					{@link Svet Svet}.{@link #registrujRobota(GRobot, String) registrujRobota}({@code valthis}, {@code srg"robot"});
					{@link Svet Svet}.{@code currčítajKonfiguráciuSveta}();
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link #použiKonfiguráciu() použiKonfiguráciu}();
					{@link Svet Svet}.{@link #nekresli() nekresli}();
					{@code kwdnew} UkážkaKonfigurácie();
					{@link Svet Svet}.{@link #kresli() kresli}();
				}
			}
			</pre>
		 * 
		 * @param meno meno robota
		 * 
		 * @see ObsluhaUdalostí#konfiguráciaZmenená()
		 * @see GRobot#uložDoSúboru(Súbor)
		 * @see GRobot#čítajZoSúboru(Súbor)
		 * @see Písmo#uložDoSúboru(Súbor)
		 * @see Písmo#čítajZoSúboru(Súbor)
		 * @see #registrujRobota()
		 * @see #registrujRobota(String)
		 * @see #registrujRobota(GRobot)
		 * @see #registrujRobota(GRobot, String)
		 * @see #dajRobota(String)
		 * @see #použiKonfiguráciu()
		 * @see #použiKonfiguráciu(String)
		 * @see #použiKonfiguráciu(String, int, int, int, int)
		 * @see #použiKonfiguráciu(int, int, int, int)
		 */
		public static void čítajKonfiguráciuSveta()
		{
			uložKonfiguráciuSveta = true;

			Súbor.Sekcia pôvodnáSekcia =
				konfiguračnýSúbor.aktívnaSekcia;

			String mennýPriestor = konfiguračnýSúbor.
				aktívnaSekcia.mennýPriestorVlastností;

			try
			{
				// konfiguračnýSúbor.otvorNaČítanie(názovKonfiguračnéhoSúboru);
				pôvodnáSekcia = konfiguračnýSúbor.aktívnaSekcia;
				konfiguračnýSúbor.aktivujSekciu(
					predvolenáSekciaKonfigurácie);

				mennýPriestor = konfiguračnýSúbor.
					aktívnaSekcia.mennýPriestorVlastností;


				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = "svet";
				String pozadie = konfiguračnýSúbor.čítajVlastnosť(
					"farbaPozadia", null == farbaPozadia ? null :
						Farba.farbaNaReťazec(farbaPozadia));
				farbaPozadia(null == pozadie ? predvolenéPozadie :
					Farba.reťazecNaFarbu(pozadie));
				Svet.interaktívnyRežim = konfiguračnýSúbor.čítajVlastnosť(
					"interaktívny", Svet.interaktívnyRežim);


				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = "svet.časovač";
				boolean aktívny = konfiguračnýSúbor.čítajVlastnosť(
					"aktívny", časovač.aktívny());
				časovač.čas = konfiguračnýSúbor.čítajVlastnosť("čas",
					// Long.valueOf(časovač.čas)).intValue();
					časovač.čas);
				if (aktívny) časovač.spusti(); else časovač.zastav();


				String text; Bod bod;

				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = "svet.výplne";
				text = konfiguračnýSúbor.čítajVlastnosť("posunutie",
					Bod.polohaNaReťazec(posunutieVýplneX, posunutieVýplneY));
				if (null != text)
				{
					bod = Bod.reťazecNaPolohu(text);
					posunutieVýplneX = bod.polohaX();
					posunutieVýplneY = bod.polohaY();
				}
				text = konfiguračnýSúbor.čítajVlastnosť("mierka",
					Bod.polohaNaReťazec(mierkaVýplneX, mierkaVýplneY));
				if (null != text)
				{
					bod = Bod.reťazecNaPolohu(text);
					mierkaVýplneX = bod.polohaX();
					mierkaVýplneY = bod.polohaY();
				}
				otočVýplňΑ = konfiguračnýSúbor.čítajVlastnosť("otočenie",
					otočVýplňΑ);
				text = konfiguračnýSúbor.čítajVlastnosť("stredOtáčania",
					Bod.polohaNaReťazec(stredOtáčaniaVýplneX,
						stredOtáčaniaVýplneY));
				if (null != text)
				{
					bod = Bod.reťazecNaPolohu(text);
					stredOtáčaniaVýplneX = bod.polohaX();
					stredOtáčaniaVýplneY = bod.polohaY();
				}
				prepočítajParametreVýplne();


				Farba farba;


				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = "podlaha";

				farba = GRobot.podlaha.farbaTextu();
				text = konfiguračnýSúbor.čítajVlastnosť("farbaTextu",
					null == farba ? null : Farba.farbaNaReťazec(farba));
				if (null != text)
				{
					farba = Farba.reťazecNaFarbu(text);
					if (!farba.equals(GRobot.podlaha.farbaTextu()))
						GRobot.podlaha.farbaTextu(farba);
				}

				farba = GRobot.podlaha.farbaPozadiaTextu();
				text = konfiguračnýSúbor.čítajVlastnosť("farbaPozadiaTextu",
					null == farba ? null : Farba.farbaNaReťazec(farba));
				if (null != text)
				{
					farba = Farba.reťazecNaFarbu(text);
					if (null == farba) GRobot.podlaha.farbaPozadiaTextu((Color)null);
					else if (!farba.equals(GRobot.podlaha.farbaPozadiaTextu()))
						GRobot.podlaha.farbaPozadiaTextu(farba);
				}
				else GRobot.podlaha.farbaPozadiaTextu((Color)null);

				GRobot.podlaha.písmo(GRobot.podlaha.
					vnútornáKonzola.aktuálnePísmo.
					čítajZoSúboru(konfiguračnýSúbor));

				GRobot.podlaha.interaktívnyRežim = konfiguračnýSúbor.
					čítajVlastnosť("interaktívny", GRobot.podlaha.interaktívnyRežim);


				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = "strop";

				farba = GRobot.strop.farbaTextu();
				text = konfiguračnýSúbor.čítajVlastnosť("farbaTextu",
					null == farba ? null : Farba.farbaNaReťazec(farba));
				if (null != text)
				{
					farba = Farba.reťazecNaFarbu(text);
					if (!farba.equals(GRobot.strop.farbaTextu()))
						GRobot.strop.farbaTextu(farba);
				}

				farba = GRobot.strop.farbaPozadiaTextu();
				text = konfiguračnýSúbor.čítajVlastnosť("farbaPozadiaTextu",
					null == farba ? null : Farba.farbaNaReťazec(farba));
				if (null != text)
				{
					farba = Farba.reťazecNaFarbu(text);
					if (null == farba) GRobot.strop.farbaPozadiaTextu((Color)null);
					else if (!farba.equals(GRobot.strop.farbaPozadiaTextu()))
						GRobot.strop.farbaPozadiaTextu(farba);
				}
				else GRobot.strop.farbaPozadiaTextu((Color)null);

				GRobot.strop.písmo(GRobot.strop.vnútornáKonzola.
					aktuálnePísmo.čítajZoSúboru(konfiguračnýSúbor));

				GRobot.strop.interaktívnyRežim = konfiguračnýSúbor.
					čítajVlastnosť("interaktívny", GRobot.strop.interaktívnyRežim);
			}
			catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
			finally
			{
				konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = mennýPriestor;
				konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia;
			}

			// try { konfiguračnýSúbor.zavri(); }
			// catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciuSveta() čítajKonfiguráciuSveta}.</p> */
		public static void citajKonfiguraciuSveta()
		{ čítajKonfiguráciuSveta(); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciuSveta() čítajKonfiguráciuSveta}.</p> */
		public static void prečítajKonfiguráciuSveta()
		{ čítajKonfiguráciuSveta(); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciuSveta() čítajKonfiguráciuSveta}.</p> */
		public static void precitajKonfiguraciuSveta()
		{ čítajKonfiguráciuSveta(); }


		/**
		 * <p>Táto metóda slúži na zadanie príkazov Javy, ktoré majú byť
		 * vykonané „neskôr“ – asynchrónne vzhľadom na reťaz čakajúcich
		 * správ (udalostí) aktívneho vlákna. To znamená, že zadané príkazy
		 * budú vykonané v čase, kedy je zásobník správ (to jest udalostí)
		 * aktívneho vlákna prázdny.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Výpis veľkého množstva textov – na niekoľko strán…}

			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.05});
			{@link Svet Svet}.{@code currvykonaťNeskôr}(() -&gt;
				{@link Plátno strop}.{@link Plátno#posunutieTextovY(int) posunutieTextovY}(
					{@link Plátno strop}.{@link Plátno#poslednáVýškaTextu() poslednáVýškaTextu}()));
			</pre>
		 * 
		 * <p>Príkazy v príklade sú vykonané nielen s päťdesiatmilisekundovým
		 * oneskorením (spôsobeným príkazom {@link Svet#čakaj(double)
		 * čakaj}), ale aj až po vykonaní spracovania všetkých čakajúcich
		 * udalostí, čo by v tomto prípade mala byť prinajmenšom udalosť
		 * prekreslenia sveta, ktorá vznikla ako dôsledok výpisu textov
		 * (ktoré nie sú v tomto príklade uvedené).</p>
		 * 
		 * @param vykonať inštancia {@link Runnable Runnable} alebo
		 *     funkcionálny blok príkazov Javy na vykonanie
		 */
		public static void vykonaťNeskôr(Runnable vykonať)
		{ SwingUtilities.invokeLater(vykonať); }

		/** <p><a class="alias"></a> Alias pre {@link #vykonaťNeskôr(Runnable) vykonaťNeskôr}.</p> */
		public static void vykonatNeskor(Runnable vykonať)
		{ SwingUtilities.invokeLater(vykonať); }


		/**
		 * <p>Táto metóda slúži na zadanie príkazov Javy, ktoré majú byť
		 * vykonané „neskôr“ – pozri opis metódy {@link 
		 * #vykonaťNeskôr(Runnable) vykonaťNeskôr(vykonať)}. Toto je verzia
		 * metódy, ktorá umožňuje určiť, či má byť vytvorené samostatné
		 * vlákno, ktoré bude vykonávať blok zadaných príkazov.</p>
		 * 
		 * @param vykonať inštancia {@link Runnable Runnable} alebo
		 *     funkcionálny blok príkazov Javy na vykonanie
		 * @param samostatnéVlákno určuje, či má byť spustené samostatné
		 *     vlákno vykonávajúce zadané príkazy
		 */
		public static void vykonaťNeskôr(Runnable vykonať,
			boolean samostatnéVlákno)
		{
			if (samostatnéVlákno)
			{
				Thread vlákno = new Thread(vykonať);
				SwingUtilities.invokeLater(() -> vlákno.start());
			}
			else
				SwingUtilities.invokeLater(vykonať);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vykonaťNeskôr(Runnable, boolean) vykonaťNeskôr}.</p> */
		public static void vykonatNeskor(Runnable vykonať, boolean samostatnéVlákno)
		{ vykonaťNeskôr(vykonať, samostatnéVlákno); }


		/**
		 * <p>Pokúsi sa použiť zadaný textový reťazec ako webovú adresu
		 * a otvoriť ju v predvolenom prehliadači operačného systému.</p>
		 * 
		 * @param uri reťazec s webovou adresou, ktorá má byť otvorená
		 *     v predvolenom prehliadači OS
		 * @return {@code valtrue} ak metóda uspela, {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean otvorWebovýOdkaz(String uri)
		{
			Desktop desktop = Desktop.isDesktopSupported() ?
				Desktop.getDesktop() : null;

			if (desktop != null && desktop.isSupported(
				Desktop.Action.BROWSE))
			{
				try
				{
					desktop.browse(new URI(uri));
					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorWebovýOdkaz(String) otvorWebovýOdkaz}.</p> */
		public static boolean otvorWebovyOdkaz(String uri)
		{ return otvorWebovýOdkaz(uri); }

		/**
		 * <p>Otvorí okno na napísanie novej správy elektronickej pošty
		 * predvoleného e-mailového klienta OS.</p>
		 * 
		 * <p>Voliteľné parametre {@code údajeSprávy} môžu byť vynechané
		 * alebo môže byť zadaný nepárny počet prvkov. V tom prípade je
		 * prvý prvok adresa primárneho adresáta a zvyšné dvojice
		 * parametrov určujú ďalšie údaje správy. Prvý z páru je názov
		 * údaja a druhý jeho hodnota. Metóda podporuje tieto názvy:</p>
		 * 
		 * <ul>
		 * <li>{@code srg"komu"} alebo {@code srg"to"} – adresa ďalšieho
		 * primárneho adresáta</li>
		 * <li>{@code srg"kópia"} alebo {@code srg"cc"} – adresa adresáta
		 * v kópii</li>
		 * <li>{@code srg"skrytá"}, {@code srg"skrytá kópia"} alebo
		 * {@code srg"bcc"} – adresa adresáta v skrytej kópii</li>
		 * <li>{@code srg"predmet"} alebo {@code srg"subject"} – predmet
		 * správy</li>
		 * <li>{@code srg"telo"}, {@code srg"telo správy"} alebo
		 * {@code srg"body"} – telo správy</li>
		 * </ul>
		 * 
		 * @param údajeSprávy údaje súvisiace s novou správou (pozri vyššie)
		 * @return {@code valtrue} ak metóda uspela, {@code valfalse}
		 *     v opačnom prípade
		 * 
		 * @see #otvorWebovýOdkaz(String)
		 * @see #vytlač(String)
		 * @see #otvorVPredvolenejAplikácii(String)
		 * @see #otvorVPredvolenejAplikácii(String, boolean)
		 */
		public static boolean pošliEmail(String... údajeSprávy)
		{
			Desktop desktop = Desktop.isDesktopSupported() ?
				Desktop.getDesktop() : null;

			if (desktop != null && desktop.isSupported(
				Desktop.Action.MAIL))
			{
				if (0 == údajeSprávy.length)
				{
					try
					{
						desktop.mail();
						return true;
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenia(e);
					}
				}
				else
				{
					String správa = null;

					try
					{
						StringBuilder sb = new StringBuilder("mailto:");
						int čísloÚdaju = 0;
						boolean semafor = false;
						String parameter = null;

						for (String údajSprávy : údajeSprávy)
						{
							if (0 == čísloÚdaju)
							{
								sb.append(údajSprávy);
								++čísloÚdaju;
							}
							else
							{
								if (semafor)
								{
									if (parameter.equalsIgnoreCase(
										"komu")) parameter = "to";
									else if (parameter.equalsIgnoreCase(
										"kópia")) parameter = "cc";
									else if (parameter.equalsIgnoreCase(
										"kopia")) parameter = "cc";
									else if (parameter.equalsIgnoreCase(
										"skrytá")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"skryta")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"skrytá kópia")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"skryta kopia")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"skrytáKópia")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"skrytaKopia")) parameter = "bcc";
									else if (parameter.equalsIgnoreCase(
										"predmet")) parameter = "subject";
									else if (parameter.equalsIgnoreCase(
										"telo")) parameter = "body";
									else if (parameter.equalsIgnoreCase(
										"telo správy")) parameter = "body";
									else if (parameter.equalsIgnoreCase(
										"telo spravy")) parameter = "body";
									else if (parameter.equalsIgnoreCase(
										"teloSprávy")) parameter = "body";
									else if (parameter.equalsIgnoreCase(
										"teloSpravy")) parameter = "body";

									sb.append(1 == čísloÚdaju ?
										'?' : '&');
									sb.append(URLEncoder.encode(
										parameter, "UTF-8"));
									sb.append('=');
									sb.append(URLEncoder.encode(
										údajSprávy, "UTF-8"));
									++čísloÚdaju;
								}
								else parameter = údajSprávy;
								semafor = !semafor;
							}
						}

						správa = sb.toString();
					}
					catch (UnsupportedEncodingException e)
					{
						GRobotException.vypíšChybovéHlásenia(e);
					}

					try
					{
						if (null == správa)
							desktop.mail();
						else
							desktop.mail(new URI(správa));
						return true;
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenia(e);
					}
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pošliEmail(String...) pošliEmail}.</p> */
		public static boolean posliEmail(String... údajeSprávy)
		{ return pošliEmail(údajeSprávy); }

		/**
		 * <p>Ak je pre zadaný súbor asociovaný príkaz na tlač dokumentu,
		 * tak otvorí prislúchajúci tlačový dialóg operačného systému,
		 * s pomocou ktorého bude možné súbor (dokument) vytlačiť.</p>
		 * 
		 * @param súbor súbor (dokument), ktorý má byť vytlačený podľa
		 *     predvoleného nastavenia OS
		 * @return {@code valtrue} ak metóda uspela, {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean vytlač(String súbor)
		{
			Desktop desktop = Desktop.isDesktopSupported() ?
				Desktop.getDesktop() : null;

			if (desktop != null && desktop.isSupported(
				Desktop.Action.PRINT))
			{
				try
				{
					desktop.print(new File(súbor));
					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vytlač(String) vytlač}.</p> */
		public static boolean vytlac(String súbor) { return vytlač(súbor); }

		/**
		 * <p>Pokúsi sa otvoriť zadaný súbor v predvolenej aplikácii podľa
		 * nastavení operačného systému.</p>
		 * 
		 * @param súbor súbor (dokument), ktorý má byť otvorený v predvolenej
		 *     aplikácie (podľa nastavení OS)
		 * @return {@code valtrue} ak metóda uspela, {@code valfalse}
		 *     v opačnom prípade
		 */
		public static boolean otvor(String súbor)
		{
			Desktop desktop = Desktop.isDesktopSupported() ?
				Desktop.getDesktop() : null;

			if (desktop != null && desktop.isSupported(
				Desktop.Action.OPEN))
			{
				try
				{
					desktop.open(new File(súbor));
					return true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorVPredvolenejAplikácii(String) otvorVPredvolenejAplikácii}.</p> */
		public static boolean otvorVPredvolenejAplikácii(String súbor)
		{ return otvor(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #otvorVPredvolenejAplikácii(String) otvorVPredvolenejAplikácii}.</p> */
		public static boolean otvorVPredvolenejAplikacii(String súbor)
		{ return otvor(súbor); }

		/**
		 * <p>Pokúsi sa otvoriť (spustiť) alebo otvoriť na úpravy zadaný
		 * súbor v predvolenej aplikácii podľa nastavení operačného
		 * systému. Spôsob otvorenia určuje druhý parameter:
		 * {@code naÚpravu}. Ak je {@code valfalse}, tak sa metóda správa
		 * rovnako ako jej verzia bez tohto parametra ({@link #otvor(String)
		 * otvor}), čiže pokúsi sa súbor (dokument) otvoriť v zmysle jeho
		 * spustenia (čo nadobúda lepší význam napríklad v prípade skriptov).
		 * Ak sa hodnota parametra rovná {@code valtrue}, tak sa metóda
		 * pokúsi zadaný súbor (dokument) otvoriť v predvolenom editore.
		 * (Ak tento nie je definovaný, tak je pravdepodobné, že operačný
		 * systém súbor otvorí/spustí rovnakým spôsobom ako v prvom
		 * prípade.)</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Porovnajte nasledujúce dva príkazy:</p>
		 * 
		 * <pre CLASS="example">
			{@link Svet Svet}.{@link Svet#otvor(String) otvor}({@code srg"uprav.bat"});
			{@link Svet Svet}.{@code currotvor}({@code srg"uprav.bat"}, {@code valtrue});
			</pre>
		 * 
		 * <p>Obidva sa pokúšajú „otvoriť“ naledujúci skript (uložený v súbore
		 * {@code uprav.bat} s kódovaním UTF-8), ale každá iným spôsobom:</p>
		 * 
		 * <pre CLASS="example">
			@echo off
			chcp 65001>nul
			echo Ale toto nie je úprava…
			echo.
			echo (Stlačte ľubovoľný kláves.)
			echo.
			pause>nul
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p>Rozdiel môžete vidieť na nasledujúcich obrázkoch:</p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>otvor-spusti-mensie.png<alt/></image></td>
		 * <td><image>otvor-uprav-mensie.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Súbor spustený v konzole Windows.</p></td>
		 * <td><p class="image">Súbor otvorený v poznámkovom
		 * bloku.</p></td></tr>
		 * </table>
		 * 
		 * @param súbor súbor (dokument), ktorý má byť otvorený v predvolenej
		 *     aplikácie (podľa nastavení OS)
		 * @param naÚpravu ak je {@code valtrue}, tak sa metóda pokúsi
		 *     otvoriť zadaný súbor (dokument) v predvolenom editore (ak je
		 *     definovaný); ak je {@code valfalse}, tak sa metóda správa
		 *     rovnako ako jej verzia bez tohto parametra ({@link 
		 *     #otvor(String) otvor})
		 * @return {@code valtrue} ak metóda uspela, {@code valfalse}
		 *     v opačnom prípade
		 * 
		 * @see #otvorWebovýOdkaz(String)
		 * @see #pošliEmail(String...)
		 * @see #vytlač(String)
		 * @see #otvor(String)
		 * @see #otvor(String, boolean)
		 */
		public static boolean otvor(String súbor, boolean naÚpravu)
		{
			Desktop desktop = Desktop.isDesktopSupported() ?
				Desktop.getDesktop() : null;

			if (naÚpravu)
			{
				if (desktop != null && desktop.isSupported(
					Desktop.Action.EDIT))
				{
					try
					{
						desktop.edit(new File(súbor));
						return true;
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenia(e);
					}
				}
			}
			else
			{
				if (desktop != null && desktop.isSupported(
					Desktop.Action.OPEN))
				{
					try
					{
						desktop.open(new File(súbor));
						return true;
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenia(e);
					}
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorVPredvolenejAplikácii(String, boolean) otvorVPredvolenejAplikácii}.</p> */
		public static boolean otvorVPredvolenejAplikácii(
			String súbor, boolean naÚpravu) { return otvor(súbor, naÚpravu); }

		/** <p><a class="alias"></a> Alias pre {@link #otvorVPredvolenejAplikácii(String, boolean) otvorVPredvolenejAplikácii}.</p> */
		public static boolean otvorVPredvolenejAplikacii(
			String súbor, boolean naÚpravu) { return otvor(súbor, naÚpravu); }

		/**
		 * <p>Hlavná časť implementácie jednoduchého príkazového riadka
		 * operačného systému. Dovoľuje spravovať jeden spustený proces.
		 * Verejné atribúty tejto triedy a vnútorné rozhranie na
		 * presmerovanie výstupov nemajú z dôvodu výrazného zjednodušenia
		 * celej implementácie definované aliasy bez diakritiky.</p>
		 */
		public static class PríkazovýRiadok extends ExecuteShellCommand
		{
			/**
			 * <p>Toto rozhranie slúži na presmerovanie výstupov a denníkov
			 * inštancií príkazového riadka. Princíp je jednoduchý, len čo
			 * je do niektorého z atribútov …<!--TODO--> vložená implementácia
			 * tohto rozhrania (dá sa použiť aj funkcionálna syntax, keďže
			 * rozhranie má jedinú metódu), tak je výstup súvisiacej metódy
			 * (…<!--TODO-->) presmerovaný do metódy implementovaného rozhrania.</p>
			 */
			public static interface PresmerovanieVýstupu
			{
				public void spracuj(Object... texty);
			}

			static
			{
				langstring[CANNOT_EXECUTE] = "Nedá sa spustiť";
				langstring[CLEARED] = "zrušené"; // mapping
				langstring[COMMAND] = "príkaz"; // mapping
				langstring[COMMAND_MAPPING] = "Mapovanie príkazu"; // mapping
				langstring[COMMANDS_MAP] = "Mapa príkazov"; // mapping
				langstring[DELETED] = "vymazané"; // mapping
				langstring[DUPLICATE_REQUEST] = "Zdvojená požiadavka";
				langstring[ENCODING] = "Aktuálne kódovanie";
				langstring[ENCODING_FOR] = "Kódovanie pre"; // mapping
				langstring[ENCODING_CHANGED] = "Kódovanie zmenené";
				langstring[ENCODING_KEEPED] = "Kódovanie ponechané";
				langstring[ENCODING_RESTORED] = "Kódovanie obnovené";
				langstring[ENCODINGS_MAP] = "Mapa kódovaní"; // mapping
				langstring[ERROR_MESSAGE] = "Chybová správa"; // cannot execute
				langstring[ERROR_OCCURED] = "Vznikla chyba"; // cannot execute
				langstring[EXECUTE] = "spustenie"; // duplicate request
				langstring[EXIT_CODE] = "Návratový kód";
				langstring[INPUT] = "vstup"; // duplicate request
				langstring[IS_EMPTY] = "je prázdna"; // mapping
				langstring[MAPPED_COMMAND_USE] = "Použitie mapovaného príkazu";
				langstring[MAPPING] = "mapovanie"; // mapping
				langstring[MORE_PROCESSES] = "viac, než jeden proces"; // cannot execute
				langstring[SET] = "nastavené"; // mapping
				langstring[STREAM_READ_ERROR] = "Chyba pri čítaní prúdu";
				langstring[STREAM_WRITE_ERROR] = "Chyba pri zápise prúdu";
			}

			/**
			 * <p>Príznak toho, že nastavenia inštancie príkazového riadka
			 * boli zmenené a mali by byť uložené do konfiguračného súboru.
			 * O využitie tohto atribútu sa musí postarať externá
			 * implementácia.</p>
			 */
			public boolean konfiguráciaZmenená = false;

			/**
			 * <p>Prípadná záloha farby uložená po spustení nového
			 * procesu. Ak je nastavená, tak pre metódu výstupu {@link 
			 * #output(Object...) output} to znamená, že nemá použiť
			 * atribút {@link #farbaVýstupu farbaVýstupu}.</p>
			 */
			public Color zálohaFarby = null;

			/**
			 * <p>Farebné prekrytie všetkých textov štandardného výstupu
			 * zachytených touto inštanciou a výstupu niektorých vnútorných
			 * príkazov spracovaných metódou sveta {@link 
			 * Svet#spracujPríkaz(String) spracujPríkaz} (napríklad ECHO).
			 * Ak je nastavené, tak je použité v tele metódy výstupu {@link 
			 * #output(Object...) output}, okrem prípadu, keď je zároveň
			 * nastavená inštancia zálohy farby: {@link #zálohaFarby
			 * zálohaFarby}.</p>
			 */
			public Color farbaVýstupu = null;

			/**
			 * <p>Farebné odlíšenie výstupov externých procesov. Ak je
			 * nastavené, tak je použité pri spustení externého procesu
			 * metódou sveta {@link Svet#spracujPríkaz(String)
			 * spracujPríkaz}.</p>
			 */
			public Color farbaVýstupuProcesu = čierna;

			/**
			 * <p>Farebné odlíšenie chybových výstupov externých procesov.
			 * Ak je nastavené, tak je použité metódou chybového výstupu
			 * {@link #error(Object...) error}.</p>
			 */
			public Color farbaChybovéhoVýstupu = tmavočervená;

			/**
			 * <p>Farebné odlíšenie textov ladenia. Ak je nastavené a ak
			 * je zároveň aktívny {@linkplain Svet#režimLadenia(boolean)
			 * režim ladenia}, tak je použité metódou výstupu textov ladenia
			 * príkazového riadka {@link #debug(Object...) debug}.</p>
			 */
			public Color farbaLadenia = tmavooranžová;

			/**
			 * <p>Farebné odlíšenie textov denníka. Ak je nastavené, tak je
			 * použité metódou výstupu denníka {@link #log(Object...)
			 * log}.</p>
			 */
			public Color farbaDenníka = tmavozelená;

			/**
			 * <p>Farebné odlíšenie textov chybového denníka. Ak je nastavené,
			 * tak je použité metódou výstupu chybového denníka {@link 
			 * #err(Object...) err}.</p>
			 */
			public Color farbaDenníkaChýb = tmavoružová;

			// /**
			//  * <p>Denník príkazového riadka. Zhromažďuje doplnkové
			//  * informácie o vykonávaných príkazoch príkazového riadka
			//  * (teraz vypnuté).</p>
			//  */
			// public Zoznam<String> log = new Zoznam<>();

			/**
			 * <p>Toto presmerovanie je odlišné od ostatných. Ak je
			 * definované, tak sú do neho presmerované všetky udalosti
			 * vyčistenia alebo resetu denníkov, výstupných prúdov
			 * a podobne. Vstupné pole parametrov pritom bude obsahovať
			 * najmenej jeden prvok, ktorý slúži na odlíšenie typu udalosti.
			 * Je to objekt typu {@link Integer Integer}, ktorého hodnota
			 * môže byť: 0 – clearDebug, 1 – clearLog, 2 – clearErr, 3 –
			 * clearError, 4 – clearOutput. Druhý prvok je relevantný
			 * len pre prvé tri udalosti a jeho hodnota je reťazec
			 * s názvom metódy, ktorá udalosť spustila. Predvolené
			 * správanie súvisiacich metód je výpis prázdneho riadka pre
			 * denník (clearLog), vymazanie vnútornej konzoly metódami
			 * štandardných výstupov (clearError a clearOutput) a ignorovanie
			 * zvyšných dvoch udalostí (clearDebug a clearErr).</p>
			 */
			public PresmerovanieVýstupu vyčistenie = null;

			/**
			 * <p>Ak je definovaná (implementovaná) inštancia tohto atribútu,
			 * tak všetky (interné alebo externé) volania metódy {@link 
			 * #debug(Object...) debug} sú automaticky presmerované do
			 * metódy {@code spracuj} tejto implementácie (inštancie; pozri
			 * aj triedu {@link PresmerovanieVýstupu PresmerovanieVýstupu})
			 * a to bez ohľadu na to, či je alebo nie je zapnutý {@linkplain 
			 * Svet#režimLadenia(boolean) režim ladenia}.</p>
			 */
			public PresmerovanieVýstupu ladenie = null;

			/**
			 * <p>Ak je definovaná (implementovaná) inštancia tohto atribútu,
			 * tak všetky (interné alebo externé) volania metódy {@link 
			 * #log(Object...) log} sú automaticky presmerované do
			 * metódy {@code spracuj} tejto implementácie (inštancie). Pozri
			 * aj triedu {@link PresmerovanieVýstupu
			 * PresmerovanieVýstupu}.</p>
			 */
			public PresmerovanieVýstupu denník = null;

			/**
			 * <p>Ak je definovaná (implementovaná) inštancia tohto atribútu,
			 * tak všetky (interné alebo externé) volania metódy {@link 
			 * #err(Object...) err} sú automaticky presmerované do
			 * metódy {@code spracuj} tejto implementácie (inštancie). Pozri
			 * aj triedu {@link PresmerovanieVýstupu
			 * PresmerovanieVýstupu}.</p>
			 */
			public PresmerovanieVýstupu denníkChýb = null;

			/**
			 * <p>Ak je definovaná (implementovaná) inštancia tohto atribútu,
			 * tak všetky (interné alebo externé) volania metódy {@link 
			 * #error(Object...) error} sú automaticky presmerované do
			 * metódy {@code spracuj} tejto implementácie (inštancie). Pozri
			 * aj triedu {@link PresmerovanieVýstupu
			 * PresmerovanieVýstupu}.</p>
			 */
			public PresmerovanieVýstupu chybovýVýstup = null;

			/**
			 * <p>Ak je definovaná (implementovaná) inštancia tohto atribútu,
			 * tak všetky (interné alebo externé) volania metódy {@link 
			 * #output(Object...) output} sú automaticky presmerované do
			 * metódy {@code spracuj} tejto implementácie (inštancie). Pozri
			 * aj triedu {@link PresmerovanieVýstupu
			 * PresmerovanieVýstupu}.</p>
			 */
			public PresmerovanieVýstupu výstupProcesu = null;

			/**
			 * <p>Táto metóda je spúšťaná automaticky v priebehu práce
			 * tejto inštancie. Účelom je nastavenie titulku okna (konzoly),
			 * ktorým je v tomto prípade okno sveta. (Mechanizmus je
			 * implementovaný v rodičovskej triede
			 * <code>ExecuteShellCommand</code>.)</p>
			 * 
			 * <p class="remark"><b>Poznámka:</b> Na zlepšenie dojmu pri
			 * používaní programovacieho rámca bolo využitie tohto príkazu
			 * eliminované len na prípady vykonania vnútorného príkazu
			 * príkazového riadka TITLE. Titulok okna (konzoly) by inak mal
			 * byť aktualizovaný po každom vykonaní príkazu príkazového
			 * riadka (prípadne aj pred ním), pretože definícia titulku môže
			 * obsahovať referencie na (virtuálne) premenné prostredia,
			 * ktorých hodnoty sa menia (napríklad %CD% – aktuálna cesta,
			 * %TIME% – aktuálny čas…).</p>
			 * 
			 * @param titulok priebežne (niekedy automaticky) aktualizovaná
			 *     verzia titulku okna
			 */
			public void title(String titulok)
			{
				svet.setTitle(titulok);
			}

			/**
			 * <p>Táto metóda je spúšťaná automaticky v priebehu práce
			 * tejto inštancie.(Mechanizmus je implementovaný v rodičovskej
			 * triede <code>ExecuteShellCommand</code>.) Jej účelom je
			 * poskytnúť aktuálnu verziu programovacieho rámca.</p>
			 * 
			 * @return aktuálna verzia programovacieho rámca
			 */
			public String getVersion()
			{
				return versionString;
			}

			/**
			 * <p>Táto metóda je spúšťaná automaticky pri začatí čakania
			 * na doplnkový vstup, napríklad pri čítaní hodnoty virtuálnej
			 * premennej %READ% z príkazového riadka. (Mechanizmus je
			 * implementovaný v rodičovskej triede
			 * <code>ExecuteShellCommand</code>.)</p>
			 * 
			 * @param návesť návesť, ktorá má byť zobrazená pred začatím
			 *     čakania na doplnkový vstup
			 * @return prípadná predvolená hodnota doplnkového vstupu, ktorá
			 *     okamžite ukončí čakanie na vstup a spustí metódu {@link 
			 *     #finishExtraInput finishExtraInput}
			 */
			public String startExtraInput(String návesť)
			{
				// Predvolene nevracať žiadnu predvolenú hodnotu
				// a vypísať len neprázdne náveste.
				if (null != návesť && !návesť.isEmpty())
					Svet.vypíšRiadok(návesť);
				return null;
			}

			/**
			 * <p>Táto metóda je spúšťaná automaticky pri dokončení čakania
			 * na doplnkový vstup (pozri aj {@link #startExtraInput
			 * startExtraInput}). Metóda poskytuje externej implementácii
			 * poslednú možnosť kontroly, úpravy alebo zrušenia doplnkového
			 * vstupu. (Mechanizmus automatického spúšťania tejto metódy je
			 * implementovaný v rodičovskej triede
			 * <code>ExecuteShellCommand</code>.)</p>
			 * 
			 * @param hodnota hodnota potvrdená používateľom (prípadne
			 *     odoslaná iným procesom)
			 * @return upravená hodnota alebo {@code valnull} na zrušenie
			 *     vstupu
			 */
			public String finishExtraInput(String hodnota)
			{
				// Predvolene len nechať prejsť.
				return hodnota;
			}

			/**
			 * <p>Automaticky spúšťaná metóda signalizujúca buď vyčistenie
			 * alebo začatie novej sekcie v prúde ladiacich informácií.</p>
			 * 
			 * @param metóda názov metódy, ktorá udalosť vyvolala
			 */
			public void clearDebug(String metóda)
			{
				if (null != vyčistenie) vyčistenie.spracuj(0, metóda);
				// Predvolene ignorovať.
			}

			/**
			 * <p>Automaticky spúšťaná metóda signalizujúca buď vyčistenie
			 * denníka, alebo začatie novej sekcie záznamov.</p>
			 * 
			 * @param metóda názov metódy, ktorá udalosť vyvolala
			 */
			public void clearLog(String metóda)
			{
				// Predvolene zaznamenať do denníka ako
				// (kvázi) začiatok nového záznamu (teraz vypnuté):
				// log.add("\n*** " + metóda + " ***");

				if (null != vyčistenie) vyčistenie.spracuj(1, metóda);
				// Vypísať aspoň prázdny riadok
				// na vnútornú konzolu:
				else Svet.vypíšRiadok();
			}

			/**
			 * <p>Automaticky spúšťaná metóda signalizujúca buď vyčistenie
			 * denníka chýb, alebo začatie novej sekcie záznamov.</p>
			 * 
			 * @param metóda názov metódy, ktorá udalosť vyvolala
			 */
			public void clearErr(String metóda)
			{
				if (null != vyčistenie) vyčistenie.spracuj(2, metóda);
				// Predvolene ignorovať.
			}

			/**
			 * <p>Automaticky spúšťaná metóda signalizujúca vymazanie
			 * zásobníka (obrazovky), na ktorú je exportovaný chybový prúd
			 * procesu. Táto implementácia vymaže vnútornú konzolu sveta
			 * (stropu).</p>
			 */
			public void clearError()
			{
				if (null != vyčistenie) vyčistenie.spracuj(3);
				else Svet.vymažTexty();
			}

			/**
			 * <p>Automaticky spúšťaná metóda signalizujúca vymazanie
			 * zásobníka (obrazovky), na ktorú je exportovaný prúd
			 * štandardného výstupu procesu. Táto implementácia vymaže
			 * vnútornú konzolu sveta (stropu).</p>
			 */
			public void clearOutput()
			{
				if (null != vyčistenie) vyčistenie.spracuj(4);
				else Svet.vymažTexty();
			}

			/**
			 * <p>Metóda vypisujúca ladiace informácie. Jej automatické
			 * spúšťanie súvisí napríklad so spúšťaním a ukončovaním
			 * procesov metódou <code>ExecuteShellCommand.execute</code>.
			 * Jej použitie je otvorené aj pre externé implementácie.
			 * Funguje tak, že vypisuje texty na vnútornú konzolu sveta
			 * (stropu) len v prípade zapnutého {@linkplain 
			 * Svet#režimLadenia(boolean) režimu ladenia}.</p>
			 * 
			 * @param texty ladiace informácie
			 */
			public void debug(Object... texty)
			{
				if (null != ladenie) ladenie.spracuj(texty);
				else if (Skript.ladenie)
				{
					// Vypísať na vnútornú konzolu:
					if (null != farbaLadenia)
					{
						Color f = Svet.farbaTextu();
						Svet.farbaTextu(farbaLadenia);
						Svet.vypíš(texty);
						Svet.farbaTextu(f);
					}
					else
						Svet.vypíš(texty);
				}
			}

			/**
			 * <p>Metóda zapisujúca údaje do denníka. Je spúšťaná aj
			 * automaticky – vnútornými príkazmi inštancie príkazového
			 * riadka, ktoré ňou poskytujú informácie o sebe.</p>
			 * 
			 * @param texty texty, ktoré majú byť zapísané do denníka
			 */
			public void log(Object... texty)
			{
				// Zaznamenať do denníka:
				// StringBuffer sb = new StringBuffer();
				// for (Object o : texty) sb.append(texty.toString());
				// log.add(sb.toString());

				// Vypísať na vnútornú konzolu:
				if (null != denník) denník.spracuj(texty);
				else if (null != farbaDenníka)
				{
					Color f = Svet.farbaTextu();
					Svet.farbaTextu(farbaDenníka);
					Svet.vypíš(texty);
					Svet.farbaTextu(f);
				}
				else
					Svet.vypíš(texty);
			}

			/**
			 * <p>Metóda zapisujúca údaje do denníka chýb. Je spúšťaná aj
			 * automaticky – vnútornými príkazmi inštancie príkazového
			 * riadka, ktoré ňou poskytujú informácie o chybách.</p>
			 * 
			 * @param texty texty, ktoré majú byť zapísané do denníka chýb
			 */
			public void err(Object... texty)
			{
				// Vypísať na vnútornú konzolu:
				if (null != denníkChýb) denníkChýb.spracuj(texty);
				else if (null != farbaDenníkaChýb)
				{
					Color f = Svet.farbaTextu();
					Svet.farbaTextu(farbaDenníkaChýb);
					Svet.vypíš(texty);
					Svet.farbaTextu(f);
				}
				else
					Svet.vypíš(texty);
			}

			/**
			 * <p>Metóda, do ktorej je automaticky presmerovaný štandardný
			 * chybový výstup spustených procesov. (V niektorých prípadoch
			 * môže byť spustená aj pri iných príležitostiach, ale to je
			 * viac-menej výnimočné.)</p>
			 * 
			 * @param texty zachytené riadky chybového prúdu
			 */
			public void error(Object... texty)
			{
				// Vypísať na vnútornú konzolu:
				if (null != chybovýVýstup) chybovýVýstup.spracuj(texty);
				else if (null != farbaChybovéhoVýstupu)
				{
					Color f = Svet.farbaTextu();
					Svet.farbaTextu(farbaChybovéhoVýstupu);
					Svet.vypíš(texty);
					Svet.farbaTextu(f);
				}
				else
					Svet.vypíš(texty);
			}

			/**
			 * <p>Metóda, do ktorej je automaticky presmerovaný štandardný
			 * výstup spustených procesov. V niektorých prípadoch môže byť
			 * táto metóda spustená aj pri iných príležitostiach (napríklad
			 * vnútorným príkazom ECHO).</p>
			 * 
			 * @param texty zachytené riadky výstupného prúdu
			 */
			public void output(Object... texty)
			{
				// Vypísať na vnútornú konzolu:
				if (null != výstupProcesu) výstupProcesu.spracuj(texty);
				else if (null == zálohaFarby && null != farbaVýstupu)
				{
					Color f = Svet.farbaTextu();
					Svet.farbaTextu(farbaVýstupu);
					Svet.vypíš(texty);
					Svet.farbaTextu(f);
				}
				else
					Svet.vypíš(texty);
			}

			/**
			 * <p>Táto metóda je vykonaná automaticky po ukončení
			 * externého procesu.
			 * <!-- TODO dopracovať opis --></p>
			 * 
			 * @param návratovýKód kód, ktorý vrátil externý proces
			 *     pri ukončení
			 */
			public void processEnded(int návratovýKód)
			{
				// Svet.farbaTextu(svetlotyrkysová);
				// Svet.vypíšRiadok(riadok, "Proces skončil: ", návratovýKód);

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.
							ukončenieProcesu(this, návratovýKód);
						ObsluhaUdalostí.počúvadlo.
							ukoncenieProcesu(this, návratovýKód);
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciSystém)
					{
						počúvajúci.ukončenieProcesu(this, návratovýKód);
						počúvajúci.ukoncenieProcesu(this, návratovýKód);
					}
				}

				if (null != zálohaFarby)
					Svet.farbaTextu(zálohaFarby);
			}

			/**
			 * <p>Zapíše do konfiguračného súboru rôzne vlastnosti tejto
			 * inštancie. Sú to hodnoty vnútorných premenných prostredia,
			 * aktuálne kódovanie, definícu náveste, titulku okna a mapy
			 * príkazov a kódovaní.</p>
			 * 
			 * @param súbor inštancia konfiguračného súboru
			 * @throws IOException v prípade vzniku vstupno-výstupnej chyby
			 */
			public void zapíšKonfiguráciu(Súbor súbor) throws IOException
			{
				int počet, i;
				TreeMap<String, String> mapa;

				súbor.mennýPriestorVlastností("okno");

				// Ulož predvolenú definíciu titulku okna.
				súbor.zapíšVlastnosť("titulok", getDefaultTitle());

				súbor.mennýPriestorVlastností("konzola");

				// Ulož predvolenú návesť vstupu.
				súbor.zapíšVlastnosť("návesť", getDefaultPrompt());

				// Ulož predvolené kódovanie konzoly.
				súbor.zapíšVlastnosť("predvolenéKódovanie", getDefaultEncoding());

				// Ulož vnútorné premenné prostredia (tie, ktoré prekrývajú
				// hodnoty systémových nastavení).
				mapa = getInternalEnvironment();
				súbor.mennýPriestorVlastností("premenné");
				počet = mapa.size();
				súbor.zapíšVlastnosť("počet", počet);

				i = 0;
				for (Map.Entry<String, String> entry : mapa.entrySet())
				{
					súbor.zapíšVlastnosť("názov[" + i + "]", entry.getKey());
					súbor.zapíšVlastnosť("hodnota[" + i + "]", entry.getValue());
					++i;
				}

				// Ulož mapu vlastných kódovaní príkazov.
				mapa = getEncodingsMap();
				súbor.mennýPriestorVlastností("mapaKódovaní");
				počet = mapa.size();
				súbor.zapíšVlastnosť("počet", počet);

				i = 0;
				for (Map.Entry<String, String> entry : mapa.entrySet())
				{
					súbor.zapíšVlastnosť("príkaz[" + i + "]", entry.getKey());
					súbor.zapíšVlastnosť("mapovanie[" + i + "]", entry.getValue());
					++i;
				}

				// Ulož mapu transformujúcu vybrané príkazy.
				mapa = getCommandsMap();
				súbor.mennýPriestorVlastností("mapaPríkazov");
				počet = mapa.size();
				súbor.zapíšVlastnosť("počet", počet);

				i = 0;
				for (Map.Entry<String, String> entry : mapa.entrySet())
				{
					súbor.zapíšVlastnosť("príkaz[" + i + "]", entry.getKey());
					súbor.zapíšVlastnosť("mapovanie[" + i + "]", entry.getValue());
					++i;
				}
			}

			/** <p><a class="alias"></a> Alias pre {@link #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}.</p> */
			public void zapisKonfiguraciu(Súbor súbor) throws IOException
			{ zapíšKonfiguráciu(súbor); }

			/**
			 * <p>Prečíta rôzne vlastnosti tejto inštancie z konfiguračného
			 * súboru. Pozri aj {@link #zapíšKonfiguráciu(Súbor)
			 * zapíšKonfiguráciu}.</p>
			 * 
			 * @param súbor inštancia konfiguračného súboru
			 * @throws IOException v prípade vzniku vstupno-výstupnej chyby
			 */
			public void čítajKonfiguráciu(Súbor súbor) throws IOException
			{
				// try
				{
					int počet = 0; String prečítané;
					TreeMap<String, String> mapa;

					súbor.mennýPriestorVlastností("okno");

					// Čítaj definíciu titulku okna.
					prečítané = súbor.čítajVlastnosť("titulok", (String)null);
					setDefaultTitle(prečítané);
					restoreDefaultTitle();

					súbor.mennýPriestorVlastností("konzola");

					// Čítaj a nastav návesť.
					prečítané = súbor.čítajVlastnosť("návesť", (String)null);
					setDefaultPrompt(prečítané);
					restoreDefaultPrompt();

					// Čítaj a nastav kódovanie konzoly.
					prečítané = súbor.čítajVlastnosť(
						"predvolenéKódovanie", (String)null);
					setDefaultEncoding(prečítané);
					restoreDefaultEncoding();

					// Čítaj vnútorné premenné prostredia (tie, ktoré prekrývajú
					// hodnoty systémových nastavení).
					mapa = getInternalEnvironment();
					súbor.mennýPriestorVlastností("premenné");
					počet = súbor.čítajVlastnosť("počet", 0);
					mapa.clear();

					for (int i = 0; i < počet; ++i)
					{
						String názov = súbor.čítajVlastnosť(
							"názov[" + i + "]", (String)null);
						String hodnota = súbor.čítajVlastnosť(
							"hodnota[" + i + "]", (String)null);
						if (null != názov && !názov.isEmpty())
							mapa.put(názov, hodnota);
					}

					// Čítaj mapu vlastných kódovaní príkazov.
					mapa = getEncodingsMap();
					súbor.mennýPriestorVlastností("mapaKódovaní");
					počet = súbor.čítajVlastnosť("počet", 0);
					mapa.clear();

					for (int i = 0; i < počet; ++i)
					{
						String príkaz = súbor.čítajVlastnosť(
							"príkaz[" + i + "]", (String)null);
						String mapovanie = súbor.čítajVlastnosť(
							"mapovanie[" + i + "]", (String)null);
						if (null != príkaz && null != mapovanie &&
							!príkaz.isEmpty() && !mapovanie.isEmpty())
							mapa.put(príkaz, mapovanie);
					}

					// Čítaj mapu transformujúcu vybrané príkazy.
					mapa = getCommandsMap();
					súbor.mennýPriestorVlastností("mapaPríkazov");
					počet = súbor.čítajVlastnosť("počet", 0);
					mapa.clear();

					for (int i = 0; i < počet; ++i)
					{
						String príkaz = súbor.čítajVlastnosť(
							"príkaz[" + i + "]", (String)null);
						String mapovanie = súbor.čítajVlastnosť(
							"mapovanie[" + i + "]", (String)null);
						if (null != príkaz && null != mapovanie &&
							!príkaz.isEmpty() && !mapovanie.isEmpty())
							mapa.put(príkaz, mapovanie);
					}
				}
				// Aby sa to zbytočne nelogicky nemenilo:
				// finally
				// {
				// 	updateTitle();
				// }
			}

			/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciu(Súbor) čítajKonfiguráciu}.</p> */
			public void citajKonfiguraciu(Súbor súbor) throws IOException
			{ čítajKonfiguráciu(súbor); }
		}

		/** <p><a class="alias"></a> Alias pre {@link PríkazovýRiadok PríkazovýRiadok}.</p> */
		public static class PrikazovyRiadok extends PríkazovýRiadok {}

		/**
		 * <p>Inštancia triedy {@link PríkazovýRiadok PríkazovýRiadok}
		 * slúžiaca na komunikáciu s príkazovým prostredím operačného
		 * systému.</p>
		 */
		public static PrikazovyRiadok príkazovýRiadok = new PrikazovyRiadok();

		/** <p><a class="alias"></a> Alias pre {@link #príkazovýRiadok príkazovýRiadok}.</p> */
		public static PrikazovyRiadok prikazovyRiadok = príkazovýRiadok;


		// Pomocná metóda vykonávajúca príkaz príkazového riadka cd/chdir.
		private static void chdir(String argumenty)
		{
			// príkazovýRiadok.outputLine("Stará cesta: ",
			// 	príkazovýRiadok.getCurrentPath());
			try
			{
				príkazovýRiadok.changePath(argumenty);
				// príkazovýRiadok.outputLine("Nová cesta: ",
				// 	príkazovýRiadok.getCurrentPath());
			}
			catch (IOException e)
			{
				príkazovýRiadok.errorLine(
					"Neplatná cesta: ", argumenty,
					riadok, e.getMessage());
			}
		}

		/**
		 * <p>Spracuje jeden príkaz príkazového riadka. Táto metóda je
		 * jednoduchou implementáciou príkazového riadka operačného systému
		 * s úzkou množinou vnútorných príkazov vymenovaných nižšie. Metóda
		 * používa funkcie implementované v statickej inštancii {@link 
		 * #príkazovýRiadok príkazovýRiadok}. Napríklad metódu {@link 
		 * PríkazovýRiadok#execute(String, String...) execute} na spustenie
		 * externého procesu. Štandardný výstup procesov
		 * je presmerovaný na vnútornú konzolu sveta (stropu – pozri
		 * napríklad metódu {@link Svet#vypíšRiadok(Object[])
		 * vypíšRiadok}).</p>
		 * 
		 * <p>Vnútorne spracúvané príkazy (neposielané do OS):</p>
		 * 
		 * <ul>
		 * <li><code>CD</code>/<code>CHDIR</code> – zmení vnútorné
		 * nastavenie aktuálnej cesty, v ktorej má byť spustený nasledujúci
		 * proces. (Doplňujúce informácie v anglickom jazyku sú v opise
		 * metódy <code>ExecuteShellCommand.changePath(String)</code>.)</li>
		 * <li><code>CLS</code> – vymaže obsah vnútornej konzoly sveta
		 * (stropu).</li>
		 * <li><code>ECHO</code> – vypíše zadané argumenty na vnútornú
		 * konzolu sveta (stropu). Obsahy premenných prostredia sú
		 * rozvinuté.</li>
		 * <li><code>CHCP</code> – zmení kódovanie inštancie príkazového
		 * riadka alebo konfiguruje vnútornú mapu kódovaní príkazov
		 * podrobnosti v anglickom jazyku sú v opise metódy
		 * <code>ExecuteShellCommand.changeEncoding(String)</code>.</li>
		 * <li><code>MAPCMD</code> – upraví alebo overí mapovanie zadaného
		 * príkazu, prípadne vypíše zoznam všetkých mapovaní. (Doplňujúce
		 * informácie v anglickom jazyku sú v opise metódy
		 * <code>ExecuteShellCommand.mapCommand(String)</code>.)</li>
		 * <li><code>PROMPT</code> – zmení definíciu náveste príkazového
		 * riadka. (Použitie náveste je nepovinné. Doplňujúce informácie
		 * v anglickom jazyku sú v opise metódy
		 * <code>ExecuteShellCommand.setPrompt(String)</code>.)</li>
		 * <li><code>SET</code> – nastaví, vymaže, vypíše zoznam premenných
		 * prostredia príkazového riadka alebo si vyžiada doplňujúci vstup.
		 * Príkaz dokáže vyhodnocovať výrazy (Doplňujúce informácie
		 * v anglickom jazyku sú v opise metódy
		 * <code>ExecuteShellCommand.setVariable(String)</code>.)</li>
		 * <li><code>TITLE</code> – upraví definíciu titulku okna konzoly,
		 * ktorá sa automaticky prenesie do titulku okna sveta. (Doplňujúce
		 * informácie v anglickom jazyku sú v opise metódy
		 * <code>ExecuteShellCommand.setTitle(String)</code>.)</li>
		 * <li><hr/></li>
		 * <li><code>PAUSE, EXIT</code> – tieto dva príkazy sú vypnuté.
		 * Metóda ich rozpoznáva, pretože sú relatívne bežné a aby neboli
		 * omylom poslané operačnému systému ako žiadosť o spustenie
		 * procesu, ktorá by sa skončila s chybou.</li>
		 * </ul>
		 * 
		 * @param príkaz reťazec obsahujúci jediný príkaz príkazového riadka
		 *     na vykonanie
		 */
		public static void spracujPríkaz(String príkaz)
		{
			if (null == príkazovýRiadok) return;

			if (príkazovýRiadok.isInputActive())
				príkazovýRiadok.writeInput(príkaz);
			else
			{
				int[] zhoda = ExecuteShellCommand.matchCommand(
					príkaz, "chcp", "mapcmd", "set", "prompt",
						"chdir", "cd", "title", "echo.", "echo",
						"cls", "pause", "exit");

				if (null != zhoda && 3 != zhoda[0])
				{
					String argumenty = príkaz.substring(zhoda[2]);
					// System.out.println("args> " + argumenty);

					switch (zhoda[1])
					{
					case 0: // chcp
						if (príkazovýRiadok.changeEncoding(argumenty))
							príkazovýRiadok.konfiguráciaZmenená = true;
						break;

					case 1: // mapcmd
						if (príkazovýRiadok.mapCommand(argumenty))
							príkazovýRiadok.konfiguráciaZmenená = true;
						break;

					case 2: // set
						if (príkazovýRiadok.setVariable(argumenty))
							príkazovýRiadok.konfiguráciaZmenená = true;
						break;

					case 3: // prompt
						if (príkazovýRiadok.setPrompt(argumenty))
							príkazovýRiadok.konfiguráciaZmenená = true;
						break;

					case 4: case 5: // chdir, cd
						chdir(argumenty);
						break;

					case 6: // title
						príkazovýRiadok.setTitle(argumenty);
						break;

					case 7: // echo.
						príkazovýRiadok.outputLine(riadok,
							príkazovýRiadok.expandVariables(argumenty));
						break;

					case 8: // echo
						príkazovýRiadok.outputLine(riadok,
							príkazovýRiadok.expandVariables(argumenty));
						break;

					case 9: // cls
						Svet.vymažTexty();
						break;

					case 10: // pause
						príkazovýRiadok.errorLine(
							"Tento príkaz je momentálne vypnutý.");
						// Implementácia by mohla byť celkom jednoduchá:
						// 
						// Čakalo by sa na „extra vstup“ a buď by sa
						// ignoroval len prvý znak (ako v príkazovom riadku
						// OS Windows), alebo celý riadok – kvázi „readln.“
						// Ignorovanie celého riadka dáva lepší zmysel, lebo
						// ignorovanie jedného znaku v OS má (v prípade
						// vstupu z iného procesu) aj tak za následok chybné
						// spracovanie zvyšku odoslaného riadka (keďže prvý
						// znak chýba).
						// 
						// Iný návrh na implementáciu by bol: neviazať to
						// na príkazový riadok, ale pozastaviť činnosť
						// hlavného vlákna frameworku, dokedy nebude stlačený
						// ľubovoľný kláves.
						break;

					case 11: // exit
						príkazovýRiadok.errorLine(
							"Tento príkaz je momentálne vypnutý.");
						// Svet.koniec(0);
						break;
					}
				}
				else
				{
					zhoda = ExecuteShellCommand.
						matchCommand(príkaz, "chdir.", "cd.", "echo.");
					if (null != zhoda)
					{
						if (2 == zhoda[1]) // echo
							príkazovýRiadok.outputLine(riadok,
								príkazovýRiadok.expandVariables(
									príkaz.substring(zhoda[2])));
						else // cd/chdir
						{
							String argumenty = príkaz.substring(zhoda[2] - 1);
							// System.out.println("args> " + argumenty);
							chdir(argumenty);
						}
					}
					else
					{
						// Tu nemá zmysel vracať farbu naspäť,
						// metóda execute aj tak ihneď skončí…
						if (null != príkazovýRiadok.farbaVýstupuProcesu)
						{
							príkazovýRiadok.zálohaFarby = Svet.farbaTextu();
							Svet.farbaTextu(príkazovýRiadok.
								farbaVýstupuProcesu);
						}
						else
						{
							príkazovýRiadok.zálohaFarby = null;
						}
						príkazovýRiadok.execute(null, príkaz);
					}
				}

				// /*String[] split = ExecuteShellCommand.
				// 	splitCommand(príkaz);* /
				// 
				// /*Svet.vykonaťNeskôr(() -> new Thread(() -> {* /
				// 	// príkazovýRiadok.execute(null, split);
				// /*}).start());* /
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #spracujPríkaz(String) spracujPríkaz}.</p> */
		public static void spracujPrikaz(String príkaz) { spracujPríkaz(príkaz); }


	// --- Vstupy, výstupy a náhodné čísla


		// Čítanie údajov cez dialógy

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@link String String} meno = {@link Svet Svet}.{@code currzadajReťazec}({@code srg"Zadaj svoje meno:"});

			{@code kwdif} (meno != {@code valnull})
			{
				{@code kwdif} (meno.{@link String#equals(Object) equals}({@code srg""}))
				<code class="comment">// Prípadne lepšie: if (!meno.{@link String#isEmpty() isEmpty}())</code>
				{
					{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Nezadal si svoje meno…"});
				}
				{@code kwdelse}
				{
					{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Vitaj, "} + meno + {@code srg"!"});
				}
			}
			{@code kwdelse}
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zrušil si zadanie svojho mena…"});
			}
			</pre>
		 * </td><td><table>
		 * <tr><td><image>citajRetazec.png<alt/></image></td></tr>
		 * <tr><td><image>citajRetazecOK.png<alt/></image></td></tr>
		 * <tr><td><image>citajRetazecCancel.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Ukážky možného vzhľadu dialógov
		 * zobrazených<br />počas vykonávania tohto príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param výzva text výzvy
		 * @return objekt {@link java.lang.String String} s textom, ktorý
		 *     zadal používateľ, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravReťazec(String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String zadajReťazec(String výzva)
		{ return zadajReťazec(výzva, predvolenýTitulokVstupu); }

		/** <p><a class="alias"></a> Alias pre {@link #zadajReťazec(String) zadajReťazec}.</p> */
		public static String zadajRetazec(String výzva)
		{ return zadajReťazec(výzva, predvolenýTitulokVstupu); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov.</p>
		 * 
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link java.lang.String String} s textom, ktorý
		 *     zadal používateľ, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravReťazec(String, String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String zadajReťazec(String výzva, String titulok)
		{
			String reťazec = null;
			Object[] výzvaATextovýRiadok = {výzva, textovýRiadok};
			textovýRiadok.setText("");

			if (JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, výzvaATextovýRiadok,
				titulok, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, odpovedeZadania, null) == JOptionPane.YES_OPTION)
				reťazec = textovýRiadok.getText();

			return reťazec;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zadajReťazec(String, String) zadajReťazec}.</p> */
		public static String zadajRetazec(String výzva, String titulok)
		{ return zadajReťazec(výzva, titulok); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie hesla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda je určená na tvorbu
		 * príkladov na vzdelávacie účely. Riadi sa princípom jednoduchosti.
		 * Vyššiu bezpečnosť pri zadávaní hesla poskytuje metóda
		 * {@link #dialóg(String[], Object[], String) dialóg(popisy,
		 * údaje, titulok)} a jej klony.</p>
		 * 
		 * @param výzva text výzvy
		 * @return objekt {@link java.lang.String String} s heslom, ktoré
		 *     zadal používateľ, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String zadajHeslo(String výzva)
		{ return zadajHeslo(výzva, predvolenýTitulokHesla); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie hesla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda je určená na tvorbu
		 * príkladov na vzdelávacie účely. Riadi sa princípom jednoduchosti.
		 * Vyššiu bezpečnosť pri zadávaní hesla poskytuje metóda
		 * {@link #dialóg(String[], Object[], String) dialóg(popisy,
		 * údaje, titulok)} a jej klony.</p>
		 * 
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link java.lang.String String} s heslom, ktoré
		 *     zadal používateľ, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String zadajHeslo(String výzva, String titulok)
		{
			String heslo = null;
			Object[] výzvaAHeslo = {výzva, riadokSHeslom};
			riadokSHeslom.setText("");

			if (JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, výzvaAHeslo, titulok,
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				odpovedeZadania, null) == JOptionPane.YES_OPTION)
				heslo = new String(riadokSHeslom.getPassword());

			return heslo;
		}

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov (očakávaný je celočíselný údaj). Pozri príklad pri
		 * {@link #zadajCeléČíslo(String, String) zadajCeléČíslo}.</p>
		 * 
		 * @param výzva text výzvy
		 * @return objekt {@link Long} s celočíselnou hodnotou, ktorú zadal
		 *     používateľ, alebo {@code valnull} ak používateľ dialóg zrušil
		 *     alebo zadal reťazec, ktorý nebolo možné previesť na celé
		 *     číslo
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 * @see #formát
		 */
		public static Long zadajCeléČíslo(String výzva)
		{
			Long číslo = null;
			String reťazec = zadajReťazec(výzva);
			try { číslo = Long.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zadajCeléČíslo(String) zadajCeléČíslo}.</p> */
		public static Long zadajCeleCislo(String výzva) { return zadajCeléČíslo(výzva); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov (očakávaný je celočíselný údaj).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@link Long} l = {@link Svet Svet}.{@code currzadajCeléČíslo}({@code srg"Počet:"}, {@code srg"Zadaj celé číslo"});

			{@code kwdif} (l != {@code valnull})
			{
				{@code typeint} i = l.{@link Long#intValue() intValue}();
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zadané: "} + {@link #F(double, int) F}(i, {@code num0}));
			}
			{@code kwdelse}
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Vstup bol zrušený alebo nesprávny."});
			</pre>
		 * </td><td><table>
		 * <tr><td><image>citajCeleCislo.png<alt/></image></td></tr>
		 * <tr><td><image>citajCeleCisloOK.png<alt/></image></td></tr>
		 * <tr><td><image>citajCeleCisloCancel.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Ukážky možného vzhľadu dialógov
		 * zobrazených<br />počas vykonávania tohto príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link Long} s celočíselnou hodnotou, ktorú zadal
		 *     používateľ, alebo {@code valnull} ak používateľ dialóg zrušil
		 *     alebo zadal reťazec, ktorý nebolo možné previesť na celé
		 *     číslo
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 * @see #formát
		 */
		public static Long zadajCeléČíslo(String výzva, String titulok)
		{
			Long číslo = null;
			String reťazec = zadajReťazec(výzva, titulok);
			try { číslo = Long.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zadajCeléČíslo(String, String) zadajCeléČíslo}.</p> */
		public static Long zadajCeleCislo(String výzva, String titulok)
		{ return zadajCeléČíslo(výzva, titulok); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov (očakávaný je číselný údaj). Pozri príklad pri
		 * {@link #zadajReálneČíslo(String, String) zadajReálneČíslo}.</p>
		 * 
		 * @param výzva text výzvy
		 * @return objekt {@link java.lang.Double Double} s celočíselnou
		 *     hodnotou, ktorú zadal používateľ, alebo {@code valnull} ak
		 *     používateľ dialóg zrušil alebo zadal reťazec, ktorý
		 *     nebolo možné previesť na reálne číslo
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 * @see #formát
		 */
		public static Double zadajReálneČíslo(String výzva)
		{
			Double číslo = null;
			String reťazec = zadajReťazec(výzva);
			try { číslo = Double.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zadajReálneČíslo(String) zadajReálneČíslo}.</p> */
		public static Double zadajRealneCislo(String výzva)
		{ return zadajReálneČíslo(výzva); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * zadanie údajov (očakávaný je číselný údaj).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@link java.lang.Double Double} d = {@link Svet Svet}.{@code currzadajReálneČíslo}({@code srg"Počet:"}, {@code srg"Zadaj reálne číslo"});

			{@code kwdif} (d != {@code valnull})
			{
				{@code typedouble} a = d.{@link java.lang.Double#doubleValue() doubleValue}();
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zadané: "} + {@link #F(double, int) F}(a, {@code num2}));
			}
			{@code kwdelse}
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Vstup bol zrušený alebo nesprávny."});
			</pre>
		 * </td><td><table>
		 * <tr><td><image>citajRealneCislo.png<alt/></image></td></tr>
		 * <tr><td><image>citajRealneCisloOK.png<alt/></image></td></tr>
		 * <tr><td><image>citajRealneCisloCancel.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Ukážky možného vzhľadu dialógov
		 * zobrazených<br />počas vykonávania tohto príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link java.lang.Double Double} s celočíselnou
		 *     hodnotou, ktorú zadal používateľ, alebo {@code valnull} ak
		 *     používateľ dialóg zrušil alebo zadal reťazec, ktorý
		 *     nebolo možné previesť na reálne číslo
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 * @see #formát
		 */
		public static Double zadajReálneČíslo(String výzva, String titulok)
		{
			Double číslo = null;
			String reťazec = zadajReťazec(výzva, titulok);
			try { číslo = Double.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zadajReálneČíslo(String, String) zadajReálneČíslo}.</p> */
		public static Double zadajRealneCislo(String výzva, String titulok)
		{ return zadajReálneČíslo(výzva, titulok); }


		// Úprava údajov cez dialógy

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov. Funguje podobne ako metóda {@link 
		 * #zadajReťazec(String) zadajReťazec}, ibaže vyžaduje zadanie
		 * reťazca určeného na úpravu.</p>
		 * 
		 * @param reťazec reťazec určený na úpravu
		 * @param výzva text výzvy
		 * @return objekt {@link java.lang.String String} s textom, ktorý
		 *     používateľ upravil alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #upravReťazec(String, String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #zadajReťazec(String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String upravReťazec(String reťazec, String výzva)
		{ return upravReťazec(reťazec, výzva, predvolenýTitulokVstupu); }

		/** <p><a class="alias"></a> Alias pre {@link #upravReťazec(String, String) upravReťazec}.</p> */
		public static String upravRetazec(String reťazec, String výzva)
		{ return upravReťazec(reťazec, výzva, predvolenýTitulokVstupu); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov. Funguje podobne ako metóda {@link 
		 * #zadajReťazec(String, String) zadajReťazec}, ibaže vyžaduje
		 * zadanie reťazca určeného na úpravu.</p>
		 * 
		 * @param reťazec reťazec určený na úpravu
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link java.lang.String String} s textom, ktorý
		 *     používateľ upravil alebo {@code valnull} ak používateľ dialóg
		 *     zrušil
		 * 
		 * @see #upravReťazec(String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #zadajReťazec(String, String)
		 * @see #dialóg(String[], Object[])
		 * @see #dialóg(String[], Object[], String)
		 */
		public static String upravReťazec(String reťazec,
			String výzva, String titulok)
		{
			Object[] výzvaATextovýRiadok = {výzva, textovýRiadok};
			textovýRiadok.setText(reťazec);

			if (JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, výzvaATextovýRiadok,
				titulok, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, odpovedeZadania, null) == JOptionPane.YES_OPTION)
				return textovýRiadok.getText();

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravReťazec(String, String, String) upravReťazec}.</p> */
		public static String upravRetazec(String reťazec, String výzva, String titulok)
		{ return upravReťazec(reťazec, výzva, titulok); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov (očakávaný je celočíselný údaj). Funguje podobne ako
		 * metóda {@link #zadajCeléČíslo(String) zadajCeléČíslo}, ibaže
		 * vyžaduje zadanie celého čísla určeného na úpravu.</p>
		 * 
		 * @param celéČíslo celé číslo určené na úpravu
		 * @param výzva text výzvy
		 * @return objekt {@link Long} s celočíselnou hodnotou, ktorú
		 *     používateľ upravil, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil alebo zadal reťazec, ktorý nebolo možné previesť na
		 *     celé číslo
		 * 
		 * @see #upravReťazec(String, String)
		 * @see #upravReťazec(String, String, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #formát
		 */
		public static Long upravCeléČíslo(long celéČíslo, String výzva)
		{
			Long číslo = null;
			String reťazec = upravReťazec(formát.format(celéČíslo), výzva);
			try { číslo = Long.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravCeléČíslo(long, String) upravCeléČíslo}.</p> */
		public static Long upravCeleCislo(long celéČíslo, String výzva) { return upravCeléČíslo(celéČíslo, výzva); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov (očakávaný je celočíselný údaj). Funguje podobne ako
		 * metóda {@link #zadajCeléČíslo(String, String) zadajCeléČíslo},
		 * ibaže vyžaduje zadanie celého čísla určeného na úpravu.</p>
		 * 
		 * @param celéČíslo celé číslo určené na úpravu
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link Long} s celočíselnou hodnotou, ktorú
		 *     používateľ upravil, alebo {@code valnull} ak používateľ dialóg
		 *     zrušil alebo zadal reťazec, ktorý nebolo možné previesť na
		 *     celé číslo
		 * 
		 * @see #upravReťazec(String, String)
		 * @see #upravReťazec(String, String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #formát
		 */
		public static Long upravCeléČíslo(long celéČíslo,
			String výzva, String titulok)
		{
			Long číslo = null;
			String reťazec = upravReťazec(formát.
				format(celéČíslo), výzva, titulok);
			try { číslo = Long.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravCeléČíslo(long, String, String) upravCeléČíslo}.</p> */
		public static Long upravCeleCislo(long celéČíslo, String výzva, String titulok)
		{ return upravCeléČíslo(celéČíslo, výzva, titulok); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov (očakávaný je číselný údaj). Funguje podobne ako
		 * metóda {@link #zadajReálneČíslo(String) zadajReálneČíslo},
		 * ibaže vyžaduje zadanie reálneho čísla určeného na úpravu.</p>
		 * 
		 * @param reálneČíslo reálne číslo určené na úpravu
		 * @param výzva text výzvy
		 * @return objekt {@link java.lang.Double Double} s celočíselnou
		 *     hodnotou, ktorú používateľ upravil, alebo {@code valnull} ak
		 *     používateľ dialóg zrušil alebo zadal reťazec, ktorý
		 *     nebolo možné previesť na reálne číslo
		 * 
		 * @see #upravReťazec(String, String)
		 * @see #upravReťazec(String, String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #formát
		 */
		public static Double upravReálneČíslo(double reálneČíslo, String výzva)
		{
			Double číslo = null;
			String reťazec = upravReťazec(formát.format(reálneČíslo), výzva);
			try { číslo = Double.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravReálneČíslo(double, String) upravReálneČíslo}.</p> */
		public static Double upravRealneCislo(double reálneČíslo, String výzva)
		{ return upravReálneČíslo(reálneČíslo, výzva); }

		/**
		 * <p>Otvorí štandardný dialóg so zadanou výzvou a zadávacím poľom na
		 * úpravu údajov (očakávaný je číselný údaj). Funguje podobne ako
		 * metóda {@link #zadajReálneČíslo(String, String)
		 * zadajReálneČíslo}, ibaže vyžaduje zadanie reálneho čísla
		 * určeného na úpravu.</p>
		 * 
		 * @param reálneČíslo reálne číslo určené na úpravu
		 * @param výzva text výzvy
		 * @param titulok text titulku dialógového okna
		 * @return objekt {@link java.lang.Double Double} s celočíselnou
		 *     hodnotou, ktorú používateľ upravil, alebo {@code valnull} ak
		 *     používateľ dialóg zrušil alebo zadal reťazec, ktorý
		 *     nebolo možné previesť na reálne číslo
		 * 
		 * @see #upravReťazec(String, String)
		 * @see #upravReťazec(String, String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #formát
		 */
		public static Double upravReálneČíslo(double reálneČíslo,
			String výzva, String titulok)
		{
			Double číslo = null;
			String reťazec = upravReťazec(formát.
				format(reálneČíslo), výzva, titulok);
			try { číslo = Double.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravReálneČíslo(double, String, String) upravReálneČíslo}.</p> */
		public static Double upravRealneCislo(double reálneČíslo, String výzva, String titulok)
		{ return upravReálneČíslo(reálneČíslo, výzva, titulok); }

		/**
		 * <p>Otvorí prispôsobiteľný dialóg s prvkami vytvorenými podľa poľa
		 * {@code údaje}. Táto metóda funguje rovnako ako metóda
		 * {@link #dialóg(String[], Object[], String) dialóg(popisy,
		 * údaje, titulok)}, ale namiesto zadaného titulku dialógu je
		 * zobrazený predvolený text ({@code srg"Dialóg"}). Dalšie
		 * podrobnosti a príklad použitia nájdete v opise uvedenej metódy.</p>
		 * 
		 * @param popisy pole popisov komponentov
		 * @param údaje pole hodnôt komponentov, ktoré zároveň určuje typ
		 *     komponentu
		 * @return {@code valtrue} znamená, že používateľ dialóg potvrdil
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravReťazec(String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #dialóg(String[], Object[], String)
		 */
		public static boolean dialóg(String[] popisy, Object[] údaje)
		{ return dialóg(popisy, údaje, predvolenýTitulokDialógu); }

		/** <p><a class="alias"></a> Alias pre {@link #dialóg(String[], Object[]) dialóg}.</p> */
		public static boolean dialog(String[] popisy, Object[] údaje)
		{ return dialóg(popisy, údaje, predvolenýTitulokDialógu); }

		/**
		 * <p>Vytvorí a otvorí prispôsobiteľný dialóg s prvkami vytvorenými podľa
		 * obsahu poľa {@code údaje}. Metóda akceptuje v poli prvkov
		 * {@code údaje} tri údajové typy (resp. v poslednom prípade špeciálnu
		 * hodnotu) prvkov tohto poľa:</p>
		 * 
		 * <ol>
		 * <li>hodnota údajového typu {@link String String}, ktorá
		 * spôsobí, že na určenej pozícii v dialógu sa bude nachádzať
		 * textové vstupné pole s predvolenou zadanou hodnotou (môže to byť
		 * aj prázdny reťazec {@code srg""}, ale údajový typ musí byť
		 * {@link String String}),</li>
		 * 
		 * <li>hodnota údajového typu {@link Double Double}, ktorá
		 * spôsobí, že na určenej pozícii v dialógu sa bude nachádzať
		 * textové vstupné pole s predvolenou zadanou číselnou hodnotou;
		 * funguje podobne ako typ {@link String String}, ale výsledok je
		 * automaticky prevedený na reálne číslo metódou {@link 
		 * #reťazecNaReálneČíslo(String) reťazecNaReálneČíslo}; ak pri
		 * prevode nastane chyba, v objekte bude uložená hodnota
		 * {@link Double#NaN Double.NaN},</li>
		 * 
		 * <li>hodnota údajového typu {@link Boolean Boolean}, ktorá
		 * spôsobí, že na určenej pozícii v dialógu sa bude nachádzať
		 * prvok začiarkavacieho políčka s predvolenou zadanou hodnotou,</li>
		 * 
		 * <li>hodnota údajového typu {@link Farba Farba} (čo je trieda
		 * programovacieho rámca GRobot), ktorá spôsobí, že na určenej pozícii
		 * dialógu bude zobrazený panel na výber farby s predvolenou zadanou
		 * farbou, pričom zmenu môže používateľ vykonať výberom z paletoy
		 * predvolených farieb programovacieho rámca alebo prostredníctvom
		 * preddefinovaného dialógu Javy, ktorý je dostupný cez prislúchajúce
		 * tlačidlo,</li>
		 * 
		 * <li>hodnota údajového typu {@link Bod Bod} (čo je trieda
		 * programovacieho rámca GRobot), ktorá spôsobí, že na určenej pozícii
		 * dialógu bude zobrazený panel na grafickú voľbu polohy s predvolenou
		 * polohou podľa hodnôt v inštancii {@link Bod Bod} a s možnosťou
		 * zadania súradníc do vstupných polí,</li>
		 * 
		 * <li>hodnota údajového typu {@link Uhol Uhol} (čo je trieda
		 * programovacieho rámca GRobot), ktorá spôsobí, že na určenej pozícii
		 * dialógu bude zobrazený panel na grafickú voľbu uhla (smeru)
		 * s predvolenou hodnotou podľa zadanej inštancie {@link Uhol Uhol}
		 * a s možnosťou zadania (zmeny) uhla prostredníctvom vstupného
		 * poľa,</li>

		 * <li>hodnota údajového typu {@link Character Character},
		 * ktorá má špeciálny význam – je rezervovaná na vkladanie riadiacich
		 * znakov – v súčasnosti je platná len hodnota znaku nového riadka
		 * ({@code srg'\n' } – odporúčame použiť znakovú konštantu
		 * {@link Konštanty#riadok riadok}), ktorá prepne dialóg zo stĺpcového do
		 * riadkového režimu a všetky komponenty dialógu (<small>zodpovedajúce
		 * jednotlivým prvkom vstupného poľa – zdrojovým prvkom</small>),
		 * ktorých zdrojové prvky sú umiestnené medzi prvkami nového riadka
		 * (<small>to znamená, že každý nový riadok musí byť samostatným prvkom
		 * vstupného poľa {@code údaje}</small>), budú umiestnené na
		 * samostatnom neviditeľnom paneli dialógu – akoby v jednom riadku</li>
		 * 
		 * <li>a hodnotu {@code valnull}, ktorá spôsobí, že na určenej
		 * pozícii bude umiestnený vstupný prvok na zadanie hesla.</li>
		 * </ol>
		 * 
		 * <!-- TODO: Skontrolovať opis, otestovať, prípadne doplniť novšie
		 * prvky typu Farba, Bod, Uhol aj do príkladu použitia. -->
		 * 
		 * <p>Ku každému prvku poľa {@code údaje} musí byť zadaný
		 * korešpondujúci prvok poľa {@code popisy}, ktorý určí popis
		 * komponentu. Výnimku tvoria znaky nových riadkov (bod 3. vyššie),
		 * ktoré nesmú mať zadaný korešpondujúci reťazec – pri ňom treba zadať
		 * namiesto platného reťazca hodnotu {@code valnull}, pretože platný
		 * reťazec by sa automaticky priradil k ďalšiemu komponentu v dialógu,
		 * čím by sa významovo posunul na nesprávne miesto a to by sa reťazovo
		 * premietlo do celého dialógu.</p>
		 * 
		 * <p>Posledný argument metódy – reťazec {@code titulok} určí titulok
		 * dialógu.</p>
		 * 
		 * <p>Pravdivá návratová hodnota vypovedá o tom, že používateľ
		 * dialóg skutočne potvrdil. (Texy tlačidiel dialógu je možné
		 * upraviť metódou {@link #textTlačidla(String) textTlačidla}.)</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Predpokladajme, že v premenných „meno“ a „heslo“ sú prihlasovacie}
			{@code comm// údaje, pričom heslo môže byť zašifrované alebo zahešované…}
			{@link String String} meno = {@code srg"Mundos"};
			{@code typechar}[] heslo = {{@code srg'a'}, {@code srg'b'}, {@code srg'c'}, {@code srg'd'}};

			{@code comm// Pripravíme si pole údajov, s ktorými budeme pracovať:}
			{@link Object Object}[] údaje = {{@code srg""}, {@code valnull}, {@code valfalse}};

			{@code comm// Tento príznak zabezpečí ukončenie aplikácie v prípade zlyhania}
			{@code comm// prihlasovacieho procesu:}
			{@code typeboolean} prihlásenieZlyhalo = {@code valfalse};

			{@code comm// Spustíme dialóg a len v prípade, že ho používateľ potvrdil,}
			{@code comm// overíme správnosť prihlasovacích údajov:}
			{@code kwdif} ({@link Svet Svet}.{@code currdialóg}({@code kwdnew} {@link String String}[] {{@code srg"Meno:"}, {@code srg"Heslo:"},
				{@code srg"Zapamätaj si ma"}}, údaje, {@code srg"Prihlásenie sa…"}))
			{
				{@code comm// Najprv overujeme prihlasovacie meno:}
				{@code kwdif} ({@code valnull} != údaje[{@code num0}] && údaje[{@code num0}] {@code kwdinstanceof} {@link String String} &&
					meno.{@link String#equalsIgnoreCase(String) equalsIgnoreCase}(({@link String String})údaje[{@code num0}]))
				{
					{@code comm// Potom heslo:}
					{@code kwdif} ({@code valnull} != údaje[{@code num1}] && údaje[{@code num1}] {@code kwdinstanceof} {@code typechar}[])
					{
						{@code typechar} porovnaj[] = ({@code typechar}[])údaje[{@code num1}];

						{@code comm// Niekde na tomto mieste by mohlo byť vykonané hešovanie}
						{@code comm// hesla, ale pozor, pôvodné aj hešlované heslo treba}
						{@code comm// po použití vyčistiť v súlade s tým, čo je vysvetlené}
						{@code comm// nižšie… (Hľadaj text začínajúci sa „Oracle odporúča“…)}

						{@code kwdif} (porovnaj.length == heslo.length)
						{
							{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; porovnaj.length; ++i)
								{@code kwdif} (porovnaj[i] != heslo[i])
								{
									prihlásenieZlyhalo = {@code valtrue};
									{@code kwdbreak};
								}

							{@code comm// Na tomto mieste by sme mohli zužitkovať voľbu}
							{@code comm// „Zapamätaj si ma“ (ktorá je v tomto príklade}
							{@code comm// použitá v podstate len na demonštráciu možností}
							{@code comm// metódy „dialóg“):}
							{@code kwdif} (!prihlásenieZlyhalo && {@code valnull} != údaje[{@code num2}] &&
								údaje[{@code num2}] {@code kwdinstanceof} {@link Boolean Boolean} && ({@link Boolean Boolean})údaje[{@code num2}])
							{
								{@code comm//...}
								{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolili ste voľbu „Zapamätaj si ma“…"});
								{@code comm//...}
							}
						}
						{@code kwdelse} prihlásenieZlyhalo = {@code valtrue};
					}
					{@code kwdelse} prihlásenieZlyhalo = {@code valtrue};
				}
				{@code kwdelse} prihlásenieZlyhalo = {@code valtrue};
			}
			{@code kwdelse} prihlásenieZlyhalo = {@code valtrue};

			{@code comm// Oracle odporúča po použití hesla nastaviť každý jeho znak na nulu,}
			{@code comm// aby nezostávalo uložené a vystopovateľné v pamäti RAM (tam by}
			{@code comm// mohlo zostať aj po ukončení aplikácie, napr. v dôsledku}
			{@code comm// nekorektného prihlásenia sa, preto toto čistenie vykonávame}
			{@code comm// v každom prípade):}
			{@code kwdif} ({@code valnull} != údaje[{@code num1}] && údaje[{@code num1}] {@code kwdinstanceof} {@code typechar}[])
			{
				{@code typechar} vymaž[] = ({@code typechar}[])údaje[{@code num1}];
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; vymaž.length; ++i) vymaž[i] = {@code num0};
			}

			{@code comm// To isté urobíme aj so vzorom hesla…}
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; heslo.length; ++i) heslo[i] = {@code num0};

			{@code comm// V prípade zlyhania prihlasovacieho procesu aplikáciu ukončíme:}
			{@code kwdif} (prihlásenieZlyhalo)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Prihlasovanie zlyhalo!"});
				{@link Svet Svet}.{@link Svet#koniec() koniec}();
				{@code kwdreturn}; {@code comm// (Volanie metódy „koniec“ nemusí znamenať okamžité}
						{@code comm// ukončenie aplikácie, preto sem pridávame aj príkaz}
						{@code comm// „return“.)}
			}

			{@code comm// ...}
			{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Prihlásenie je v poriadku. Vitajte!"});
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p>Po spustení aplikácie je používateľovi zobrazený dialóg na
		 * obrázku nižšie.</p>
		 * 
		 * <p><image>dialog.png<alt/>Dialóg „zadaj meno
		 * a heslo.“</image>Vyplnený dialóg príkadu.</p>
		 * 
		 * <p>Ak používateľ zadal správne údaje, tak pri zvolení voľby
		 * „Zapamätaj si ma“ (ako na obrázku) sa najprv zobrazí správa
		 * ‚Zvolili ste voľbu „Zapamätaj si ma“…‘ a po potvrdení správa
		 * „Prihlásenie je v poriadku. Vitajte!“</p>
		 * 
		 * @param popisy pole popisov komponentov
		 * @param údaje pole hodnôt komponentov, ktoré zároveň určuje typ
		 *     komponentu
		 * @param titulok titulok dialógu
		 * @return {@code valtrue} znamená, že používateľ dialóg potvrdil
		 * 
		 * @see #zadajReťazec(String)
		 * @see #zadajReťazec(String, String)
		 * @see #zadajHeslo(String)
		 * @see #zadajHeslo(String, String)
		 * @see #zadajCeléČíslo(String)
		 * @see #zadajCeléČíslo(String, String)
		 * @see #zadajReálneČíslo(String)
		 * @see #zadajReálneČíslo(String, String)
		 * @see #upravReťazec(String, String)
		 * @see #upravCeléČíslo(long, String)
		 * @see #upravCeléČíslo(long, String, String)
		 * @see #upravReálneČíslo(double, String)
		 * @see #upravReálneČíslo(double, String, String)
		 * @see #dialóg(String[], Object[])
		 */
		public static boolean dialóg(String[] popisy, Object[] údaje,
			String titulok)
		{
			if (dialógZobrazený)
			{
				pípni();
				return false;
			}

			try
			{
				dialógZobrazený = true;
				int početKomponentov = 0, početPanelov = 0;
				int poslednáFarba = 0, poslednáPoloha = 0, poslednýSmer = 0,
					poslednýText = 0, poslednáVoľba = 0, poslednéHeslo = 0,
					poslednýPanel = 0;

				for (Object údaj : údaje)
				{
					if (null == údaj)
					{
						++poslednéHeslo;
						if (riadkyHesielDialógu.size() < poslednéHeslo)
							riadkyHesielDialógu.add(new RobotPasswordField()
							{{
								setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							}});
						početKomponentov += 2;
					}
					else if (údaj instanceof Boolean)
					{
						++poslednáVoľba;
						if (voľbyDialógu.size() < poslednáVoľba)
							voľbyDialógu.add(new /*Robot/Nie!*/JCheckBox());
						++početKomponentov;
					}
					else if (údaj instanceof Double)
					{
						++poslednýText;
						if (textovéRiadkyDialógu.size() < poslednýText)
							textovéRiadkyDialógu.add(new RobotTextField()
							{{
								setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							}});
						početKomponentov += 2;
					}
					else if (údaj instanceof String)
					{
						++poslednýText;
						if (textovéRiadkyDialógu.size() < poslednýText)
							textovéRiadkyDialógu.add(new RobotTextField()
							{{
								setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							}});
						početKomponentov += 2;
					}
					else if (údaj instanceof Farba)
					{
						++poslednáFarba;
						if (voľbyFariebDialógu.size() < poslednáFarba)
							voľbyFariebDialógu.add(new Farba.PanelFarieb(null,
								Svet.tlačidláDialógu[0],
								Svet.tlačidláDialógu[1], null, 0)
							// {{
								// setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							// }}
							);
						početKomponentov += 2;
					}
					else if (údaj instanceof Bod)
					{
						++poslednáPoloha;
						if (voľbyPolohyDialógu.size() < poslednáPoloha)
							voľbyPolohyDialógu.add(new Bod.PanelPolohy(
								Svet.tlačidláDialógu[2], null, 0)
							// {{
								// setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							// }}
							);
						početKomponentov += 2;
					}
					else if (údaj instanceof Uhol)
					{
						++poslednýSmer;
						if (voľbySmeruDialógu.size() < poslednýSmer)
							voľbySmeruDialógu.add(new Uhol.PanelSmeru(
								Svet.tlačidláDialógu[3], null, 0)
							// {{
								// setPreferredSize(new Dimension(120, 20));
								// setBackground(svetlozelená.svetlejšia());
								// System.out.println(getPreferredSize());
							// }}
							);
						početKomponentov += 2;
					}
					else if (údaj instanceof Character)
					{
						++poslednýPanel;
						if (panelyDialógu.size() < poslednýPanel)
							panelyDialógu.add(new JPanel());
						++početPanelov;
						++početKomponentov;
					}
				}

				// Predvolene sú komponenty údajov a dialógu totožné, ak
				// je počet panelov nenulový, tak je vytvorené extra pole
				// pre komponenty dialógu…
				Object[] komponentyÚdajov = new Object[početKomponentov];
				Object[] komponentyDialógu = komponentyÚdajov;
				int[] indexyZdrojov = new int[údaje.length]; int i = 0, j = 0;
				poslednáFarba = 0; poslednáPoloha = 0; poslednýSmer = 0;
				poslednýText = 0; poslednáVoľba = 0; poslednéHeslo = 0;
				poslednýPanel = 0;

				// V obidvoch vetvách – i rastie kontinuálne s cyklom foreach.
				if (početPanelov > 0)
				{
					// V tejto vetve – j slúži na indexovanie poľa popisy,
					// k slúži na indexovanie komponentov dialógu (panelov)
					// l slúži na indexovanie textových popisov (JLabel).
					komponentyDialógu = new Object[početPanelov + 1];
					JPanel aktívnyPanel = null; int k = 0; int l = 0;

					for (Object údaj : údaje)
					{
						if (null == aktívnyPanel)
						{
							if (panelyDialógu.size() < (poslednýPanel + 1))
								panelyDialógu.add(new JPanel());

							aktívnyPanel = panelyDialógu.get(poslednýPanel++);
							aktívnyPanel.removeAll();
							komponentyDialógu[k] = aktívnyPanel;
							++k;

							if (aktívnyPanel.getLayout() instanceof FlowLayout)
							{
								((FlowLayout)aktívnyPanel.getLayout()).
									setAlignment(FlowLayout.LEADING);
							}
							// aktívnyPanel.setBackground(ružová);
						}

						if (null == údaj)
						{
							RobotPasswordField heslo =
								riadkyHesielDialógu.get(poslednéHeslo++);
							heslo.setText("");

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = heslo;
							aktívnyPanel.add(heslo);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Boolean)
						{
							/*Robot/Nie!*/JCheckBox voľba =
								voľbyDialógu.get(poslednáVoľba++);
							voľba.setSelected((Boolean)údaj);
							if (j < popisy.length && null != popisy[j])
								voľba.setText(popisy[j]);
							else
								voľba.setText("");

							komponentyÚdajov[i] = voľba;
							aktívnyPanel.add(voľba);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Double)
						{
							RobotTextField text =
								textovéRiadkyDialógu.get(poslednýText++);
							text.setText(formát.format((Double)údaj));

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = text;
							aktívnyPanel.add(text);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof String)
						{
							RobotTextField text =
								textovéRiadkyDialógu.get(poslednýText++);
							text.setText((String)údaj);

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = text;
							aktívnyPanel.add(text);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Farba)
						{
							Farba.PanelFarieb panel =
								voľbyFariebDialógu.get(poslednáFarba++);
							panel.upravTextyTlačidiel(Svet.tlačidláDialógu[0],
								Svet.tlačidláDialógu[1]);
							panel.nastavFarbu((Farba)údaj);

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								panel.upravTitulok(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = panel;
							aktívnyPanel.add(panel);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Bod)
						{
							Bod.PanelPolohy panel =
								voľbyPolohyDialógu.get(poslednáPoloha++);
							panel.upravTextTlačidla(Svet.tlačidláDialógu[2]);
							panel.nastavPolohu((Bod)údaj);

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = panel;
							aktívnyPanel.add(panel);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Uhol)
						{
							Uhol.PanelSmeru panel =
								voľbySmeruDialógu.get(poslednýSmer++);
							panel.upravTextTlačidla(Svet.tlačidláDialógu[3]);
							panel.nastavSmer((Uhol)údaj);

							if (j < popisy.length && null != popisy[j])
							{
								if (popisyDialógu.size() < (l + 1))
									popisyDialógu.add(new JLabel());
								JLabel popis = popisyDialógu.get(l++);
								popis.setText(popisy[j]);
								aktívnyPanel.add(popis);
							}
							komponentyÚdajov[i] = panel;
							aktívnyPanel.add(panel);
							indexyZdrojov[i] = i;
						}
						else if (údaj instanceof Character)
						{
							if (riadok == (Character)údaj)
							{
								if (j < popisy.length && null != popisy[j]) --j;
								aktívnyPanel = null;
							}
							else
							{
								// Ignorujem, vypíšem hlásenie, ale premennú
								// j nemením – t. j. korešpondujúci popis sa
								// preskočí:
								GRobotException.vypíšChybovéHlásenie(
									"Neznámy znak pri tvorbe dialógu: " +
									údaj);
							}

							indexyZdrojov[i] = -1;
						}
						else
						{
							indexyZdrojov[i] = -1;
						}
						++i; ++j;
					}
				}
				else for (Object údaj : údaje)
				{
					// V tejto vetve – j slúži na indexovanie komponentov
					// dialógu (rôznych typov).
					if (null == údaj)
					{
						RobotPasswordField heslo =
							riadkyHesielDialógu.get(poslednéHeslo++);
						heslo.setText("");

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = heslo;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else if (údaj instanceof Boolean)
					{
						/*Robot/Nie!*/JCheckBox voľba =
							voľbyDialógu.get(poslednáVoľba++);
						voľba.setSelected((Boolean)údaj);
						if (i < popisy.length && null != popisy[i])
							voľba.setText(popisy[i]);
						else
							voľba.setText("");

						komponentyÚdajov[j] = voľba;

						indexyZdrojov[i] = j;
						++j;
					}
					else if (údaj instanceof Double)
					{
						RobotTextField text =
							textovéRiadkyDialógu.get(poslednýText++);
						text.setText(formát.format((Double)údaj));

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = text;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else if (údaj instanceof String)
					{
						RobotTextField text =
							textovéRiadkyDialógu.get(poslednýText++);
						text.setText((String)údaj);

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = text;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else if (údaj instanceof Farba)
					{
						Farba.PanelFarieb panel =
							voľbyFariebDialógu.get(poslednáFarba++);
						panel.nastavFarbu((Farba)údaj);

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = panel;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else if (údaj instanceof Bod)
					{
						Bod.PanelPolohy panel =
							voľbyPolohyDialógu.get(poslednáPoloha++);
						panel.nastavPolohu((Bod)údaj);

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = panel;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else if (údaj instanceof Uhol)
					{
						Uhol.PanelSmeru panel =
							voľbySmeruDialógu.get(poslednýSmer++);
						panel.nastavSmer((Uhol)údaj);

						if (i < popisy.length && null != popisy[i])
							komponentyÚdajov[j] = popisy[i];
						else
							komponentyÚdajov[j] = "";
						komponentyÚdajov[j + 1] = panel;

						indexyZdrojov[i] = j + 1;
						j += 2;
					}
					else indexyZdrojov[i] = -1;
					++i;
				}

				boolean výsledok = JOptionPane.showOptionDialog(
					null == oknoCelejObrazovky ? svet : oknoCelejObrazovky,
					komponentyDialógu, titulok, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, odpovedeZadania, null)
					== JOptionPane.YES_OPTION;

				i = 0;
				for (Object údaj : údaje)
				{
					if (0 > indexyZdrojov[i]);
					else if (null == údaj)
					{
						údaje[i] = ((RobotPasswordField)komponentyÚdajov
							[indexyZdrojov[i]]).getPassword();
						((RobotPasswordField)komponentyÚdajov
							[indexyZdrojov[i]]).setText("");
					}
					else if (údaj instanceof Boolean)
					{
						údaje[i] = ((/*Robot/Nie!*/JCheckBox)komponentyÚdajov
							[indexyZdrojov[i]]).isSelected();
					}
					else if (údaj instanceof Double)
					{
						Double číslo = reťazecNaReálneČíslo(
							((RobotTextField)komponentyÚdajov
							[indexyZdrojov[i]]).getText());
						if (null != číslo) údaje[i] = číslo;
						else údaje[i] = Double.NaN;
					}
					else if (údaj instanceof String)
					{
						údaje[i] = ((RobotTextField)komponentyÚdajov
							[indexyZdrojov[i]]).getText();
					}
					else if (údaj instanceof Farba)
					{
						údaje[i] = ((Farba.PanelFarieb)komponentyÚdajov
							[indexyZdrojov[i]]).dajFarbu();
					}
					else if (údaj instanceof Bod)
					{
						údaje[i] = ((Bod.PanelPolohy)komponentyÚdajov
							[indexyZdrojov[i]]).dajPolohu();
					}
					else if (údaj instanceof Uhol)
					{
						údaje[i] = ((Uhol.PanelSmeru)komponentyÚdajov
							[indexyZdrojov[i]]).dajSmer();
					}
					++i;
				}

				return výsledok;
			}
			finally
			{
				dialógZobrazený = false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #dialóg(String[], Object[], String) dialóg}.</p> */
		public static boolean dialog(String[] popisy, Object[] údaje,
			String titulok)
		{ return dialóg(popisy, údaje, titulok); }


		// Dialógy – správy/otázky

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou.</p>
		 * 
		 * <p><image>DialogSprava.png<alt/>Ukážka vzhľadu dialógu
		 * s informačnou správou</image>Ukážka vzhľadu dialógu
		 * s informačnou správou.</p>
		 * 
		 * @param správa text správy
		 * 
		 * @see #správa(String, String)
		 * @see #varovanie(String)
		 * @see #chyba(String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static void správa(String správa)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, správa, "Správa",
				JOptionPane.INFORMATION_MESSAGE);
		}

			// Jednoduchý kód na zobrazenie trojice dialógov (TODO – treba
				// vyrobiť obrázky na rôznych platformách a verziách OS):
				// @Override public void klik()
				// {
				// 	if (ÚdajeUdalostí.myš().isControlDown())
				// 	{
				// 		if (ÚdajeUdalostí.tlačidloMyši(ÚdajeUdalostí.ĽAVÉ))
				// 			Svet.varovanie("Text s obsahom varovania.");
				// 		else
				// 			Svet.chyba("Text s obsahom chyby.");
				// 	}
				// 	else Svet.správa("Text správy.");
				// }

		/** <p><a class="alias"></a> Alias pre {@link #správa(String) správa}.</p> */
		public static void sprava(String správa) { správa(správa); }

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou.</p>
		 * 
		 * <p><image>DialogSprava.png<alt/>Ukážka vzhľadu dialógu
		 * s informačnou správou</image>Ukážka vzhľadu dialógu
		 * s informačnou správou.</p>
		 * 
		 * @param správa text správy
		 * @param titulok text titulku dialógového okna správy
		 * 
		 * @see #správa(String)
		 * @see #varovanie(String, String)
		 * @see #chyba(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static void správa(String správa, String titulok)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, správa, titulok,
				JOptionPane.INFORMATION_MESSAGE);
		}

		/** <p><a class="alias"></a> Alias pre {@link #správa(String, String) správa}.</p> */
		public static void sprava(String správa, String titulok) { správa(správa, titulok); }

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou a ikonou
		 * varovania.</p>
		 * 
		 * <p><image>DialogVarovanie.png<alt/>Ukážka vzhľadu dialógu
		 * s varovaním</image>Ukážka vzhľadu dialógu s varovaním.</p>
		 * 
		 * @param varovanie text s obsahom varovania
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 */
		public static void varovanie(String varovanie)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, varovanie, "Varovanie",
				JOptionPane.WARNING_MESSAGE);
		}

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou a ikonou
		 * varovania.</p>
		 * 
		 * <p><image>DialogVarovanie.png<alt/>Ukážka vzhľadu dialógu
		 * s varovaním</image>Ukážka vzhľadu dialógu s varovaním.</p>
		 * 
		 * @param varovanie text s obsahom varovania
		 * @param titulok text titulku dialógového okna varovania
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 */
		public static void varovanie(String varovanie, String titulok)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, varovanie, titulok,
				JOptionPane.WARNING_MESSAGE);
		}

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou a ikonou
		 * chyby.</p>
		 * 
		 * <p><image>DialogChyba.png<alt/>Ukážka vzhľadu dialógu
		 * s chybovým hlásením</image>Ukážka vzhľadu dialógu s chybovým
		 * hlásením.</p>
		 * 
		 * @param chyba text s obsahom chyby
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 */
		public static void chyba(String chyba)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, chyba, "Chyba",
				JOptionPane.ERROR_MESSAGE);
		}

		/**
		 * <p>Zobrazí štandardný dialóg so zadanou textovou správou a ikonou
		 * chyby.</p>
		 * 
		 * <p><image>DialogChyba.png<alt/>Ukážka vzhľadu dialógu
		 * s chybovým hlásením</image>Ukážka vzhľadu dialógu s chybovým
		 * hlásením.</p>
		 * 
		 * @param chyba text s obsahom chyby
		 * @param titulok text titulku dialógového okna chyby
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 */
		public static void chyba(String chyba, String titulok)
		{
			JOptionPane.showMessageDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, chyba, titulok,
				JOptionPane.ERROR_MESSAGE);
		}


		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Dobre?"});
			{@code kwdif} ({@link Konštanty#ÁNO ÁNO} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Odpoveď: áno"});
			{@code kwdif} ({@link Konštanty#NIE NIE} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Odpoveď: nie"});
			{@code kwdif} ({@link Konštanty#ZAVRETÉ ZAVRETÉ} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Nechceš odpovedať…"});
			</pre>
		 * </td><td><table>
		 * <tr><td rowspan="3"><image>otazka1A.png<alt/></image></td>
		 * <td><image>otazka2A.png<alt/></image></td></tr>
		 * <tr><td><image>otazka3A.png<alt/></image></td></tr>
		 * <tr><td><image>otazka4A.png<alt/></image></td></tr>
		 * <tr><td colspan="2"><p class="image">Ukážky možného vzhľadu dialógov
		 * a odpovedí<br />zobrazených počas vykonávania tohto
		 * príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static int otázka(String otázka)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka,
				predvolenýTitulokOtázky, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, odpovedeOtázky,
				odpovedeOtázky[0]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String) otázka}.</p> */
		public static int otazka(String otázka) { return otázka(otázka); }

		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Dobre?"}, {@code srg"Otázočka na teba…"});
			{@code kwdif} ({@link Konštanty#ÁNO ÁNO} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Odpoveď: áno"});
			{@code kwdif} ({@link Konštanty#NIE NIE} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Odpoveď: nie"});
			{@code kwdif} ({@link Konštanty#ZAVRETÉ ZAVRETÉ} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Nechceš odpovedať…"});
			</pre>
		 * </td><td><table>
		 * <tr><td rowspan="3"><image>otazka1B.png<alt/></image></td>
		 * <td><image>otazka2A.png<alt/></image></td></tr>
		 * <tr><td><image>otazka3A.png<alt/></image></td></tr>
		 * <tr><td><image>otazka4A.png<alt/></image></td></tr>
		 * <tr><td colspan="2"><p class="image">Ukážky možného vzhľadu dialógov
		 * a odpovedí<br />zobrazených počas vykonávania tohto
		 * príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @param titulok text titulku okna s otázkou
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static int otázka(String otázka, String titulok)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka, titulok,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				odpovedeOtázky, odpovedeOtázky[0]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String, String) otázka}.</p> */
		public static int otazka(String otázka, String titulok) { return otázka(otázka, titulok); }

		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Ako?"}, {@code kwdnew} {@link String#String(java.lang.String) String}[]{{@code srg"Zle"}, {@code srg"Dobre"}});
			{@code comm// Spracovanie… Pozri príklad pri:} {@link #otázka(String, String, Object[], int) otázka(otázka, titulok, tlačidlá, predvolenéTlačidlo)}
			</pre>
		 * </td><td><image>otazka1C.png<alt/>Ukážka dialógu.</image>Ukážka
		 * možného vzhľadu dialógu<br />zobrazeného počas
		 * vykonávania tohto príkladu.</td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @param tlačidlá zoznam popisov tlačidiel
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static int otázka(String otázka, Object[] tlačidlá)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka,
				predvolenýTitulokOtázky, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, tlačidlá, tlačidlá[0]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String, String, Object[]) otázka}.</p> */
		public static int otazka(String otázka, Object[] tlačidlá) { return otázka(otázka, tlačidlá); }

		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Ako?"}, {@code srg"Otázočka na teba…"}, {@code kwdnew} {@link String#String(java.lang.String) String}[]{{@code srg"Zle"}, {@code srg"Dobre"}});
			{@code comm// Spracovanie… Pozri príklad pri:} {@link #otázka(String, String, Object[], int) otázka(otázka, titulok, tlačidlá, predvolenéTlačidlo)}
			</pre>
		 * </td><td><image>otazka1D.png<alt/>Ukážka dialógu.</image>Ukážka
		 * možného vzhľadu dialógu<br />zobrazeného počas
		 * vykonávania tohto príkladu.</td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @param titulok text titulku okna s otázkou
		 * @param tlačidlá zoznam popisov tlačidiel
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, Object[], int)
		 * @see #otázka(String, String, Object[], int)
		 */
		public static int otázka(String otázka, String titulok, Object[] tlačidlá)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka, titulok,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				tlačidlá, tlačidlá[0]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String, String, Object[]) otázka}.</p> */
		public static int otazka(String otázka, String titulok, Object[] tlačidlá)
		{ return otázka(otázka, titulok, tlačidlá); }

		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Ako?"}, {@code kwdnew} {@link String#String(java.lang.String) String}[]{{@code srg"Zle"}, {@code srg"Dobre"}}, {@code num1});
			{@code comm// Spracovanie… Pozri príklad pri:} {@link #otázka(String, String, Object[], int) otázka(otázka, titulok, tlačidlá, predvolenéTlačidlo)}
			</pre>
		 * </td><td><image>otazka1E.png<alt/>Ukážka dialógu.</image>Ukážka
		 * možného vzhľadu dialógu<br />zobrazeného počas
		 * vykonávania tohto príkladu.</td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @param tlačidlá zoznam popisov tlačidiel
		 * @param predvolenéTlačidlo poradové číslo predvoleného tlačidla
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, String, Object[], int)
		 */
		public static int otázka(String otázka,
			Object[] tlačidlá, int predvolenéTlačidlo)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka,
				predvolenýTitulokOtázky, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, tlačidlá,
				tlačidlá[predvolenéTlačidlo]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String, Object[], int) otázka}.</p> */
		public static int otazka(String otázka, Object[] tlačidlá, int predvolenéTlačidlo)
		{ return otázka(otázka, tlačidlá, predvolenéTlačidlo); }

		/**
		 * <p>Zobrazí štandardný dialóg s otázkou na používateľa
		 * (odpoveď používateľ volí stlačením tlačidla).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <table><tr><td>
		 * <pre CLASS="example">
			{@code typeint} i = {@link Svet Svet}.{@code currotázka}({@code srg"Ako?"}, {@code srg"Otázočka na teba…"}, {@code kwdnew} {@link String#String(java.lang.String) String}[]{{@code srg"Zle"}, {@code srg"Dobre"}}, {@code num1});
			{@code kwdif} (0 == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Zle, vravíš? Škoda…"});
			{@code kwdif} (1 == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Dobre, vravíš? Výborne!"});
			{@code kwdif} ({@link Konštanty#ZAVRETÉ ZAVRETÉ} == i) {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg"Nechceš odpovedať…"});
			</pre>
		 * </td><td><table>
		 * <tr><td><image>otazka1F.png<alt/></image></td></tr>
		 * <tr><td><image>otazka2F.png<alt/></image></td></tr>
		 * <tr><td><image>otazka3F.png<alt/></image></td></tr>
		 * <tr><td><image>otazka4F.png<alt/></image></td></tr>
		 * <tr><td><p class="image">Ukážky možného vzhľadu dialógov
		 * a odpovedí<br />zobrazených počas vykonávania tohto
		 * príkladu.</p></td></tr>
		 * </table></td></tr></table>
		 * 
		 * @param otázka text otázky
		 * @param titulok text titulku okna s otázkou
		 * @param tlačidlá zoznam popisov tlačidiel
		 * @param predvolenéTlačidlo poradové číslo predvoleného tlačidla
		 * @return celé číslo označujúce poradové číslo tlačidla zvoleného
		 *     ako odpoveď alebo {@link GRobot#ZAVRETÉ ZAVRETÉ}, keď
		 *     používateľ zavrel dialóg
		 * 
		 * @see #správa(String)
		 * @see #správa(String, String)
		 * @see #otázka(String)
		 * @see #otázka(String, String)
		 * @see #otázka(String, Object[])
		 * @see #otázka(String, String, Object[])
		 * @see #otázka(String, Object[], int)
		 */
		public static int otázka(String otázka, String titulok, Object[] tlačidlá, int predvolenéTlačidlo)
		{
			return JOptionPane.showOptionDialog(null == oknoCelejObrazovky ?
				svet : oknoCelejObrazovky, otázka, titulok,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				tlačidlá, tlačidlá[predvolenéTlačidlo]);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otázka(String, String, Object[], int) otázka}.</p> */
		public static int otazka(String otázka, String titulok, Object[] tlačidlá, int predvolenéTlačidlo)
		{ return otázka(otázka, titulok, tlačidlá, predvolenéTlačidlo); }


		/**
		 * <p>Umožňuje overiť aktuálne definované znenie textu tlačidla
		 * konkrétnej odpovede na otázku (pozri napríklad metódu
		 * {@link #otázka(String) otázka}) alebo zadania/zmeny údaju (pozri
		 * napríklad metódu {@link #zadajReťazec(String) zadajReťazec}).
		 * Predvolené znenie textov tlačidiel sa dá zmeniť metódou
		 * {@link #textTlačidla(String, String) textTlačidla(tlačidlo, text)}.
		 * Táto metóda prijíma text, ktorý považuje za univerzálny
		 * identifikátor tlačidla a vráti skutočný text tlačidla.</p>
		 * 
		 * @param tlačidlo musí byť jeden z nasledujúcich predvolených textov:
		 *     {@code srg"áno"}, {@code srg"nie"}, {@code srg"ok"},
		 *     {@code srg"zrušiť"}, {@code srg"reset farby"},
		 *     {@code srg"miešanie farby"}, {@code srg"reset polohy"} alebo
		 *     {@code srg"reset smeru"} (na veľkosti písmen nezáleží;
		 *     programovací rámec prijme aj predvolené texty bez diakritiky
		 *     alebo bez medzier, ale v tom prípade je potrebné ju/ich
		 *     vynechať v celom slove/texte)
		 * @return aktuálny text určeného tlačidla alebo {@code valnull} ak je
		 *     hodnota argumentu neplatná
		 */
		public static String textTlačidla(String tlačidlo)
		{
			if (tlačidlo.equalsIgnoreCase("áno") ||
				tlačidlo.equalsIgnoreCase("ano"))
				return (String)odpovedeOtázky[0];
			if (tlačidlo.equalsIgnoreCase("nie"))
				return (String)odpovedeOtázky[1];

			if (tlačidlo.equalsIgnoreCase("ok"))
				return (String)odpovedeZadania[0];
			if (tlačidlo.equalsIgnoreCase("zrušiť") ||
				tlačidlo.equalsIgnoreCase("zrusit"))
				return (String)odpovedeZadania[1];

			if (tlačidlo.equalsIgnoreCase("reset farby") ||
				tlačidlo.equalsIgnoreCase("resetfarby"))
				return tlačidláDialógu[0];
			if (tlačidlo.equalsIgnoreCase("miešanie farby") ||
				tlačidlo.equalsIgnoreCase("miesanie farby") ||
				tlačidlo.equalsIgnoreCase("miešaniefarby") ||
				tlačidlo.equalsIgnoreCase("miesaniefarby"))
				return tlačidláDialógu[1];
			if (tlačidlo.equalsIgnoreCase("reset polohy") ||
				tlačidlo.equalsIgnoreCase("resetpolohy"))
				return tlačidláDialógu[2];
			if (tlačidlo.equalsIgnoreCase("reset smeru") ||
				tlačidlo.equalsIgnoreCase("resetsmeru"))
				return tlačidláDialógu[3];

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #textTlačidla(String) textTlačidla}.</p> */
		public static String textTlacidla(String tlačidlo)
		{ return textTlačidla(tlačidlo); }

		/**
		 * <p>Umožňuje upraviť text tlačidla odpovede na otázku (pozri napríklad
		 * metódu {@link #otázka(String) otázka}) alebo zadania údaju (pozri
		 * napríklad metódu {@link #zadajReťazec(String) zadajReťazec}).</p>
		 * 
		 * @param tlačidlo musí byť jeden z nasledujúcich predvolených textov:
		 *     {@code srg"áno"}, {@code srg"nie"}, {@code srg"ok"},
		 *     {@code srg"zrušiť"}, {@code srg"reset farby"},
		 *     {@code srg"miešanie farby"}, {@code srg"reset polohy"} alebo
		 *     {@code srg"reset smeru"} (na veľkosti písmen nezáleží;
		 *     programovací rámec prijme aj predvolené texty bez diakritiky
		 *     alebo bez medzier, ale v tom prípade je potrebné ju/ich
		 *     vynechať v celom slove/texte)
		 * @param text nový text určeného tlačidla (ak je hodnota prvého
		 *     argumentu neplatná, nebude mať volanie tejto metódy žiadny
		 *     efekt)
		 */
		public static void textTlačidla(String tlačidlo, String text)
		{
			if (tlačidlo.equalsIgnoreCase("áno") ||
				tlačidlo.equalsIgnoreCase("ano")) odpovedeOtázky[0] = text;
			else if (tlačidlo.equalsIgnoreCase("nie")) odpovedeOtázky[1] = text;

			else if (tlačidlo.equalsIgnoreCase("ok")) odpovedeZadania[0] = text;
			else if (tlačidlo.equalsIgnoreCase("zrušiť") ||
				tlačidlo.equalsIgnoreCase("zrusit")) odpovedeZadania[1] = text;

			else if (tlačidlo.equalsIgnoreCase("reset farby") ||
				tlačidlo.equalsIgnoreCase("resetfarby"))
				tlačidláDialógu[0] = text;
			else if (tlačidlo.equalsIgnoreCase("miešanie farby") ||
				tlačidlo.equalsIgnoreCase("miesanie farby") ||
				tlačidlo.equalsIgnoreCase("miešaniefarby") ||
				tlačidlo.equalsIgnoreCase("miesaniefarby"))
				tlačidláDialógu[1] = text;
			else if (tlačidlo.equalsIgnoreCase("reset polohy") ||
				tlačidlo.equalsIgnoreCase("resetpolohy"))
				tlačidláDialógu[2] = text;
			else if (tlačidlo.equalsIgnoreCase("reset smeru") ||
				tlačidlo.equalsIgnoreCase("resetsmeru"))
				tlačidláDialógu[3] = text;
		}

		/** <p><a class="alias"></a> Alias pre {@link #textTlačidla(String, String) textTlačidla}.</p> */
		public static void textTlacidla(String tlačidlo, String text)
		{ textTlačidla(tlačidlo, text); }


		// Úvodná obrazovka

		/**
		 * <p>Zobrazí úvodnú obrazovku so zadaným obrázkom. Úvodná obrazovka
		 * (<em>splash screen</em>) je dekoračný obrázok zobrazovaný počas
		 * inicializácie aplikácie. Má upozorniť používateľa, že aplikácia sa
		 * práve spúšťa a potrebuje čas na svoju úplnú inicializáciu. Volanie
		 * tejto metódy zároveň spôsobí skrytie hlavného okna aplikácie
		 * (sveta), nezabudnite preto po skončení inicializácie použiť metódu
		 * {@link #skryÚvodnúObrazovku() skryÚvodnúObrazovku} na skrytie
		 * úvodnej obrazovky a zobrazenie hlavného okna sveta…</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Java má vlastný mechanizmus
		 * definovania úvodnej obrazovky ({@link java.awt.SplashScreen}),
		 * avšak takúto úvodnú obrazovku nie je možné vytvoriť programovo,
		 * preto používa programovací rámec GRobot svoj vlastný spôsob.</p>
		 * 
		 * <p>Obrázok pre túto verziu metódy musíte definovať sami. Nie je
		 * predpísaný spôsob, ale priam sa ponúka využitie triedy {@link 
		 * Obrázok Obrázok} a mechanizmu {@linkplain 
		 * GRobot#kresliNaObrázok(Obrázok) kreslenia na obrázok pomocou
		 * robota}. Treba pri tom zvážiť viacero okolností. Zobrazenie
		 * úvodnej obrazovky je očakávané v čase, keď ešte hlavné okno nie
		 * je zobrazené. V podstate v čase, keď ešte nejestvuje žiadny
		 * robot. Preto, ak chceme používať kreslenie pomocou robota, musíme
		 * jedného na tento účel vytvoriť. Prvý vytvorený robot je zároveň
		 * {@linkplain #hlavnýRobot() hlavný robot} zodpovedný za
		 * vytvorenie a zobrazenie hlavného okna. Preto musíme pred jeho
		 * vytvorením volať metódu {@link Svet Svet}{@code .}{@link 
		 * Svet#skry() skry}{@code ()}. Najlepšie je celý kód umiestniť do
		 * hlavnej metódy ({@code main}).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} HlavnáTrieda {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} HlavnáTrieda()
				{
					{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num3.0});
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link Svet#skry() skry}();

					{@link GRobot GRobot} kreslič = {@code kwdnew} {@link GRobot#GRobot() GRobot}();
					{@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num300}, {@code num200});
					kreslič.{@link GRobot#kresliNaObrázok(Obrázok) kresliNaObrázok}(obrázok);

					{@code comm// Kreslenie}
					obrázok.{@link Obrázok#vyplň(Color) vyplň}(svetlošedá);

					{@code comm// Chceli sme orámovať plochu obrázka. Robot pracuje s odlišným}
					{@code comm// súradnicovým priestorom, preto bolo na nájdenie vhodných hodnôt}
					{@code comm// potrebné trochu experimentovať:}
					kreslič.{@link GRobot#skoč(double, double) skoč}(&#45;{@code num0.5}, {@code num0.5});
					kreslič.{@link GRobot#obdĺžnik(double, double) obdĺžnik}({@code num149.5}, {@code num99.5});

					kreslič.{@link GRobot#písmo(Font) písmo}({@code srg"Arial"}, {@code num32});
					kreslič.{@link GRobot#text(String) text}({@code srg"Čakajte…"});

					{@code comm// kreslič.skry(); // ← týmto spôsobom by sme v podstate zamedzili,}
						{@code comm// aby sa inštancia hlavnej triedy stala hlavným robotom (čo}
						{@code comm// by nebolo podľa nášho očakávania) a hlavným robotom by zostal}
						{@code comm// skrytý kreslič; preto urobíme toto:}
					{@link Svet Svet}.{@link Svet#uvoľni(GRobot) uvoľni}(kreslič);

					{@code comm// Zobrazenie}
					{@link Svet Svet}.{@code currzobrazÚvodnúObrazovku}(obrázok);
					{@code kwdnew} HlavnáTrieda();
					{@link Svet Svet}.{@link Svet#skryÚvodnúObrazovku() skryÚvodnúObrazovku}();
				}
			}
			</pre>
		 * 
		 * <p>Vidíme, že tento spôsob prináša určité komplikácie (pozri text
		 * pred príkladom), preto odporúčame obrázok určený na zobrazenie
		 * počas inicializácie uložiť do súboru v niektorom z podporovaných
		 * súborových formátov a použiť ďalšiu verziu metódy
		 * {@code currzobrazÚvodnúObrazovku} prijímajúcu názov súboru:
		 * {@link #zobrazÚvodnúObrazovku(String)
		 * zobrazÚvodnúObrazovku(názovSúboru)}.</p>
		 * 
		 * <p>Vhodný obrázok si môžeme vyrobiť aj sami. Nasledujúci príklad
		 * nahrádza obsah metódy {@code main} predchádzajúceho príkladu.
		 * Využíva obe verzie metódy {@code currzobrazÚvodnúObrazovku}. Ak
		 * súbor s obrázkom jestvuje, zobrazí ho, inak ho najskôr vyrobí
		 * a potom zobrazí jeho vyrobenú verziu uloženú v pamäti:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Súbor Súbor}.{@link Súbor#jestvuje(String) jestvuje}({@code srg"môjObrázok.png"}))
				{@link Svet Svet}.{@link Svet#zobrazÚvodnúObrazovku(String) zobrazÚvodnúObrazovku}({@code srg"môjObrázok.png"});
			{@code kwdelse}
			{
				{@link Svet Svet}.{@link Svet#skry() skry}();

				{@link GRobot GRobot} kreslič = {@code kwdnew} {@link GRobot#GRobot() GRobot}();
				{@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num300}, {@code num200});
				kreslič.{@link GRobot#kresliNaObrázok(Obrázok) kresliNaObrázok}(obrázok);

				{@code comm// Kreslenie}
				kreslič.{@link GRobot#farba(int, int, int, int) farba}({@code num199}, {@code num201}, {@code num203}, {@code num9});
				{@code kwdfor} ({@code typedouble} i = {@code num1}; i &gt; {@code num0}; i &#45;= {@code num0.03})
					kreslič.{@link GRobot#vyplňElipsu(double, double) vyplňElipsu}({@code num150.0} * i, {@code num100.0} * i);

				kreslič.{@link GRobot#farba(int, int, int) farba}({@code num99}, {@code num101}, {@code num103});
				kreslič.{@link GRobot#písmo(Font) písmo}({@code srg"Arial"}, {@code num32});
				kreslič.{@link GRobot#text(String) text}({@code srg"Čakajte…"});

				obrázok.{@link Obrázok#ulož(String) ulož}({@code srg"môjObrázok.png"});

				{@code comm// Tento robot bol doteraz hlavným robotom. Aby bolo vykonávanie}
				{@code comm// programu konzistetné v oboch prípadoch – keď obrázok úvodnej}
				{@code comm// obrazovky nejestvuje a keď obrázok už bol vytvorený – odstránime}
				{@code comm// v tejto vetve kresliča z pamäte počítača. Hlavným robotom sa}
				{@code comm// potom stane inštancia triedy HlavnáTrieda – rovnako ako po}
				{@code comm// vykonaní hlavnej vetvy tohto vetvenia.}
				{@code comm// (Poznámka: Nastavenie farby robota na červenú je len}
				{@code comm//  ilustračné – aby bolo vidno, že robot naozaj nejestvuje.)}
				kreslič.{@link GRobot#farba(Color) farba}({@link Farebnosť#červená červená});
				{@link Svet Svet}.{@link Svet#uvoľni(GRobot) uvoľni}(kreslič);

				{@code comm// Zobrazenie}
				{@link Svet Svet}.{@code currzobrazÚvodnúObrazovku}(obrázok);
			}

			{@code kwdnew} HlavnáTrieda();
			{@link Svet Svet}.{@link Svet#skryÚvodnúObrazovku() skryÚvodnúObrazovku}();

			{@code comm// Ak by sme vo vedľajšej vetve vyššie uvedeného vetvenia neodstránili}
			{@code comm// robota kresliča, tak pri prvom spustení (keby ešte nejestvoval}
			{@code comm// obrázok) by sa nasledujúca kružnica nenakreslila a pod robotom}
			{@code comm// inštancie HlavnáTrieda by bolo vidno (aspoň presvitajúc) ďalšieho}
			{@code comm// červeného robota. Kružnica by sa v skutočnosti nakreslila –}
			{@code comm// červenou farbou, ale do inštancie obrázok, do ktorej má kreslič}
			{@code comm// presmerované kreslenie. (Obrázok je v tom čase už uložený, takže}
			{@code comm// v ňom by ste ju nenašli.) Pri každom ďalšom spustení by sa}
			{@code comm// nakreslila kružnica inštanciou triedy HlavnáTrieda (predvolene}
			{@code comm// čiernou farbou). Uvoľnenie kresliča z pamäte túto nekonzistenciu}
			{@code comm// odstraňuje.}
			{@link Svet Svet}.{@link #hlavnýRobot() hlavnýRobot}().{@link GRobot#kružnica(double) kružnica}({@code num50});
			</pre>
		 * 
		 * <p>O niečo zdokonalenú verziu môžete vidieť na nasledujúcom
		 * obrázku:</p>
		 * 
		 * <p><image>uvodnaObrazovka1.png<alt/>Obrázok zobrazený pri
		 * spustení aplikácie (príkladu).</image>Obrázok úvodnej obrazovky –
		 * „{@code uvodnaObrazovka1.png}.“</p>
		 * 
		 * <p>Obrázok je pre demonštráciu polopriehľadný. Zobrazenie
		 * priehľadnosti okna závisí od platformy a verzie Javy.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Robota kresliča síce
		 * odstránime z pamäti počítača, svet sa však už zrušiť nedá.
		 * Rovnako platí, že svet je vytvorený i pri vykonaní príkazu
		 * {@link Svet#zobrazÚvodnúObrazovku(String)
		 * zobrazÚvodnúObrazovku(názovSúboru)}. Z týchto dôvodov nie je
		 * možné po použití príkazov na zobrazenie úvodnej obrazovky
		 * použiť v konštruktore hlavnej triedy príkaz {@code valsuper}
		 * na nastavenie rozmerov plátna. Program by sa zrútil so vznikom
		 * výnimky „Svet už jestvuje!“ Dá sa však použiť príkaz
		 * {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p>
		 * 
		 * @param obrázok objekt obrázka; môže byť použitý aj objekt
		 *     typu {@link Obrázok Obrázok}
		 * 
		 * @see #zobrazÚvodnúObrazovku(String)
		 * @see #skryÚvodnúObrazovku()
		 */
		public static void zobrazÚvodnúObrazovku(Image obrázok)
		{
			final Image úvodnýObrázok = obrázok;

			if (null == úvodnáObrazovka)
			{
				úvodnáObrazovka = new JWindow()
				{ @Override public void paint(java.awt.Graphics g)
				{ g.drawImage(úvodnýObrázok, 0, 0, null); }};

				úvodnáObrazovka.setBackground(new Color(0, 0, 0, 0));

				// https://docs.oracle.com/javase/tutorial/uiswing/misc/trans_shaped_windows.html
				// com.sun.awt.AWTUtilities.setWindowOpaque(
				//	úvodnáObrazovka, false);

				úvodnáObrazovka.getContentPane().setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}

			úvodnáObrazovka.setSize(
				úvodnýObrázok.getWidth(null),
				úvodnýObrázok.getHeight(null));
			úvodnáObrazovka.setLocationRelativeTo(null);
			úvodnáObrazovka.setVisible(true);
			skry();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zobrazÚvodnúObrazovku(Image) zobrazÚvodnúObrazovku}.</p> */
		public static void zobrazUvodnuObrazovku(Image úvodnýObrázok)
		{ zobrazÚvodnúObrazovku(úvodnýObrázok); }

		/**
		 * <p>Zobrazí úvodnú obrazovku so zadaným obrázkom prečítaným zo súboru.
		 * Úvodná obrazovka (<em>splash screen</em>) je dekoračný obrázok
		 * zobrazovaný počas inicializácie aplikácie. Má upozorniť
		 * používateľa, že aplikácia sa práve spúšťa a potrebuje čas na svoju
		 * úplnú inicializáciu. Volanie tejto metódy zároveň spôsobí skrytie
		 * hlavného okna aplikácie (sveta), nezabudnite preto po skončení
		 * inicializácie použiť metódu {@link #skryÚvodnúObrazovku()
		 * skryÚvodnúObrazovku} na skrytie úvodnej obrazovky a zobrazenie
		 * hlavného okna sveta…</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Java má vlastný mechanizmus
		 * definovania úvodnej obrazovky ({@link java.awt.SplashScreen}),
		 * avšak takúto úvodnú obrazovku nie je možné vytvoriť programovo,
		 * preto používa programovací rámec GRobot svoj vlastný spôsob.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} HlavnáTrieda {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} HlavnáTrieda()
				{
					{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@code currzobrazÚvodnúObrazovku}({@code srg"môjObrázok.png"});
					{@code kwdnew} HlavnáTrieda();
					{@link Svet Svet}.{@link Svet#skryÚvodnúObrazovku() skryÚvodnúObrazovku}();
				}
			}
			</pre>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Vykonanie tohto príkazu
		 * má za následok inicializáciu sveta. Z toho dôvodu nie je možné
		 * po jeho použití použiť v konštruktore hlavnej triedy príkaz
		 * {@code valsuper} na nastavenie rozmerov plátna. Program by sa
		 * zrútil so vznikom výnimky „Svet už jestvuje!“ Dá sa však použiť
		 * príkaz {@link #zmeňRozmeryPlátien(int, int) zmeňRozmeryPlátien}.</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param názovSúboru názov súboru s obrázkom (prípona súboru musí
		 *     byť {@code .gif}, {@code .png} alebo {@code .jpg} <small>(resp.
		 *     {@code .jpeg})</small>)
		 * 
		 * @see #zobrazÚvodnúObrazovku(Image)
		 * @see #skryÚvodnúObrazovku()
		 * @see Svet#priečinokObrázkov(String)
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 */
		public static void zobrazÚvodnúObrazovku(String názovSúboru)
		{
			// Na platforme macOS (predtým OS X a Mac OS; na Windows to
			// funguje stále) je táto metóda veľmi citlivá pri práci
			// s transparentnými obrázkami. Nedokážem prísť na to, prečo.
			// Príliš mnoho záhad…
			//
			// „Nekonečným“ experimentovaním som prišiel na to, že nasledujúca
			// šialená kombinácia príkazov nepochopiteľne funguje a to len
			// spolu. Ak je niektorý z nich vynechaný, tak to jednoducho
			// nefunguje a absolútne nerozumiem príčinám:

				Svet.skry();
				inicializujGrafiku();
				zobrazÚvodnúObrazovku(Obrázok.čítaj(názovSúboru));
				// zobrazÚvodnúObrazovku(Obrázok.súborNaObrázok(názovSúboru));

			//
			// Výsledky pátrania:
			// ------------------
			//
			//  1. Keď vynechám volanie „Svet.skry();“, tak to má za následok
			//     len to, že hlavné okno sa pred zobrazením úvodnej obrazovky
			//     na chvíľu zobrazí. Už to dokáže rozhodiť kreslenie úvodnej
			//     obrazovky natoľko, aby zlikvidovala priehľadnosť
			//     v zobrazovanom obrázku.
			//  2. Z metódy „inicializujGrafiku()“ som skopíroval všetko do
			//     tejto metódy a postupne som vymazával jednotlivé príkazy
			//     (samozrejme, že som to tak vo finále nemohol nechať, preto
			//     je tu radšej volanie metódy inicializujGrafiku). Našiel som
			//     nasledujúcu šialenú kombináciu príkazov, ktoré majú priamy
			//     vplyv na to, či táto metóda funguje:
			//
			//         inicializované = true;
			//
			//         panelVstupnéhoRiadka.add(popisVstupnéhoRiadka);
			//         svet.add(panelVstupnéhoRiadka, BorderLayout.SOUTH);
			//
			//         svet.setJMenuBar(vytvorHlavnúPonuku());
			//         svet.pack();
			//         svet.setSize(počiatočnáŠírka, počiatočnáVýška);
			//         svet.setLocation(počiatočnéX, počiatočnéY);
			//
			//     Vymazaním jediného z nich sa funkčnosť tejto metódy stratí
			//     (toľko opätovných kompilácií v takom krátkom časovom úseku
			//     som ešte nerobil – pekne to zahrialo celý stroj).
			//  3. Posledné dva riadky z jadra tejto metódy obsahujúce
			//     volania metódy „zobrazÚvodnúObrazovku“ (jedno z nich je
			//     v komentári) by mali byť vzájomne ekvivaletné… Evidentne
			//     to tak nie je…
			//
			// Človek by si myslel, že keď príde tak ďaleko, tak odhalí koreň
			// toho všetkého, ale rezignoval som. Ktovie aké skryté
			// inicializačné procesy spôsobia, že to celé nakoniec funguje?
			// Veď už len príkazy setSize, setLocation alebo pack by na to
			// nemali mať absolútne žiadny vplyv. Celé to je šialené.
		}

		/** <p><a class="alias"></a> Alias pre {@link #zobrazÚvodnúObrazovku(String) zobrazÚvodnúObrazovku}.</p> */
		public static void zobrazUvodnuObrazovku(String názovSúboru)
		{ zobrazÚvodnúObrazovku(názovSúboru); }

		/**
		 * <p>Skryje úvodnú obrazovku a zobrazí hlavné okno aplikácie. Viac
		 * informácií nájdete v opisoch metód {@link 
		 * #zobrazÚvodnúObrazovku(Image) zobrazÚvodnúObrazovku(obrázok)}
		 * a {@link #zobrazÚvodnúObrazovku(String)
		 * zobrazÚvodnúObrazovku(názovSúboru)}…</p>
		 * 
		 * @see #zobrazÚvodnúObrazovku(Image)
		 * @see #zobrazÚvodnúObrazovku(String)
		 */
		public static void skryÚvodnúObrazovku()
		{
			if (null != úvodnáObrazovka)
				úvodnáObrazovka.setVisible(false);
			zobraz();
		}

		/** <p><a class="alias"></a> Alias pre {@link #skryÚvodnúObrazovku() skryÚvodnúObrazovku}.</p> */
		public static void skryUvodnuObrazovku() { skryÚvodnúObrazovku(); }


		// Vstupný riadok

		/**
		 * <p>Zobrazí vstupný riadok v spodnej časti okna. Aplikácia pokračuje
		 * v činnosti. Ak bol vstupný riadok zobrazený (t. j. už prebieha
		 * iný vstup), metóda sa predčasne ukončí a vráti {@code valfalse}.
		 * Riadok je možné zrušiť klávesom {@code Escape} alebo potvrdiť
		 * klávesom {@code Enter}. Ak bola použitá metóda {@link 
		 * #neskrývajVstupnýRiadok() neskrývajVstupnýRiadok}, kláves
		 * {@code Escape} iba vymaže obsah vstupného riadka, ale neskryje
		 * ho. Po potvrdení vstupu klávesom {@code Enter} aplikácia vyvolá
		 * metódu počúvadla {@link ObsluhaUdalostí#potvrdenieÚdajov()
		 * potvrdenieÚdajov}. V nej môže programátor získať zadané údaje
		 * metódami
		 * {@link Svet#prevezmiReťazec() prevezmiReťazec},
		 * {@link Svet#prevezmiCeléČíslo() prevezmiCeléČíslo} alebo
		 * {@link Svet#prevezmiReálneČíslo() prevezmiReálneČíslo}.
		 * V prípade, že potvrdené údaje nie sú v správnom tvare (platí pre
		 * celé alebo reálne číslo), vrátia metódy {@link 
		 * Svet#prevezmiCeléČíslo() prevezmiCeléČíslo} a {@link 
		 * Svet#prevezmiReálneČíslo() prevezmiReálneČíslo} hodnotu {@code 
		 * valnull}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda je použitá
		 * v príklade „Hra na ozvenu,“ ktorý sa nachádza v {@linkplain 
		 * Svet úvode dokumentácie tejto triedy}…</p>
		 * 
		 * @return {@code valtrue} ak bol pred volaním metódy vstupný riadok
		 *     skrytý, {@code valfalse} ak bol vstupný riadok zobrazený (čiže
		 *     ak vstup už prebieha)
		 * 
		 * @see #začniVstup(String)
		 * @see #potvrďVstup()
		 * @see #zrušVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static boolean začniVstup()
		{
			if (panelVstupnéhoRiadka.isVisible()) return false;
			panelVstupnéhoRiadka.setVisible(true);
			popisVstupnéhoRiadka.setVisible(false);
			vstupnýRiadok.setText("");
			vstupnýRiadok.requestFocus();
			// údajeVstupnéhoRiadka.setLength(0);
			automatickéPrekreslenie();
			return true;
		}

		/** <p><a class="alias"></a> Alias pre {@link #začniVstup() začniVstup}.</p> */
		public static boolean zacniVstup() { return začniVstup(); }

		/**
		 * <p>Funguje rovnako ako {@link #začniVstup()}, s tým rozdielom, že pred
		 * vstupným riadkom je zobrazená návesť s výzvou.</p>
		 * 
		 * @param výzva text výzvy zobrazenej vo forme náveste pred vstupným
		 *     riadkom
		 * @return {@code valtrue} ak bol pred volaním metódy vstupný riadok
		 *     skrytý, {@code valfalse} ak bol vstupný riadok zobrazený (čiže
		 *     ak už vstup prebieha)
		 * 
		 * @see #začniVstup()
		 * @see #potvrďVstup()
		 * @see #zrušVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static boolean začniVstup(String výzva)
		{
			if (panelVstupnéhoRiadka.isVisible()) return false;
			panelVstupnéhoRiadka.setVisible(true);
			popisVstupnéhoRiadka.setVisible(true);
			popisVstupnéhoRiadka.setText(výzva);
			vstupnýRiadok.setText("");
			vstupnýRiadok.requestFocus();
			automatickéPrekreslenie();
			return true;
		}

		/** <p><a class="alias"></a> Alias pre {@link #začniVstup(String) začniVstup}.</p> */
		public static boolean zacniVstup(String výzva) { return začniVstup(výzva); }

		/**
		 * <p>Vykoná pre vstupný riadok rovnakú akciu ako pri stlačení klávesu
		 * {@code Enter}.</p>
		 * 
		 * @see #začniVstup()
		 * @see #začniVstup(String)
		 * @see #zrušVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void potvrďVstup()
		{
			údajeVstupnéhoRiadka.setLength(0);
			údajeVstupnéhoRiadka.append(vstupnýRiadok.getText());

			// Vloženie potvrdeného reťazca do histórie
			if (aktívnaHistóriaVstupnéhoRiadka)
			{
				String reťazec = údajeVstupnéhoRiadka.toString();
				if (!reťazec.isEmpty())
				{
					históriaVstupnéhoRiadkaNezmenená = false;
					while (históriaVstupnéhoRiadka.odober(reťazec));
					históriaVstupnéhoRiadka.pridaj(reťazec);
					históriaVstupnéhoRiadka.počítadlo(
						históriaVstupnéhoRiadka.veľkosť());
				}
			}

			if (vstupnýRiadokStáleViditeľný)
				vstupnýRiadok.setText("");

			if (0 != početVInteraktívnomRežime)
			{
				String príkaz = údajeVstupnéhoRiadka.toString();
				Skript.poslednáChyba = ŽIADNA_CHYBA;
				int početÚspechov; boolean vykonaťRiadok;

				if (Skript.ladenie)
				{
					if (null == ObsluhaUdalostí.počúvadlo ||
						ObsluhaUdalostí.počúvadlo.
							ladenie(-1, príkaz, VYPÍSAŤ_PRÍKAZ))
					{
						if (null == príkaz || príkaz.isEmpty())
						{
							// nič
						}
						else if (Skript.začniVýpisRiadkuLadenia(príkaz))
						{
							// nič
						}
						else
							Skript.dokončiVýpisRiadkuLadenia(príkaz);
					}

					vykonaťRiadok = null == ObsluhaUdalostí.počúvadlo ||
						ObsluhaUdalostí.počúvadlo.
							ladenie(-1, príkaz, VYKONAŤ_PRÍKAZ);
				}
				else vykonaťRiadok = true;

				if (vykonaťRiadok)
				{
					if (null != príkaz && !príkaz.isEmpty() &&
						'@' == príkaz.charAt(0))
					{
						if (príkaz.length() == 1)
							interaktívnaInštancia = null;
						else
							interaktívnaInštancia =
								príkaz.substring(1)/*.toLowerCase()*/;

						početÚspechov = 1;
					}
					else
						početÚspechov = Skript.vykonajPríkaz(príkaz,
							interaktívnaInštancia);

					if (0 != početÚspechov)
					{
						if (null != ObsluhaUdalostí.počúvadlo)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.
									spracovaniePríkazu();
								ObsluhaUdalostí.počúvadlo.
									spracovaniePrikazu();
							}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci : GRobot.počúvajúciSystém)
							{
								počúvajúci.spracovaniePríkazu();
								počúvajúci.spracovaniePrikazu();
							}
						}
						return;
					}
					else if (ŽIADNA_CHYBA == Skript.poslednáChyba)
						Skript.poslednáChyba = CHYBA_NEZNÁMY_PRÍKAZ;
				}
			}

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.potvrdenieÚdajov();
					ObsluhaUdalostí.počúvadlo.potvrdenieUdajov();
					ObsluhaUdalostí.počúvadlo.potvrdenieVstupu();
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				for (GRobot počúvajúci : GRobot.počúvajúciVstupnýRiadok)
				{
					počúvajúci.potvrdenieÚdajov();
					počúvajúci.potvrdenieUdajov();
					počúvajúci.potvrdenieVstupu();
				}
			}

			if (!vstupnýRiadokStáleViditeľný)
			{
				panelVstupnéhoRiadka.setVisible(false);
				automatickéPrekreslenie();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #potvrďVstup() potvrďVstup}.</p> */
		public static void potvrdVstup() { potvrďVstup(); }

		/**
		 * <p>Vykoná pre vstupný riadok rovnakú akciu ako pri stlačení klávesu
		 * {@code Escape}.</p>
		 * 
		 * @see #začniVstup()
		 * @see #začniVstup(String)
		 * @see #potvrďVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 */
		public static void zrušVstup()
		{
			zrušenéÚdajeVstupnéhoRiadka.setLength(0);
			zrušenéÚdajeVstupnéhoRiadka.append(vstupnýRiadok.getText());
			if (vstupnýRiadokStáleViditeľný)
				vstupnýRiadok.setText("");
			else
			{
				panelVstupnéhoRiadka.setVisible(false);
				automatickéPrekreslenie();
			}

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.zrušenieÚdajov();
					ObsluhaUdalostí.počúvadlo.zrusenieUdajov();
					ObsluhaUdalostí.počúvadlo.zrušenieVstupu();
					ObsluhaUdalostí.počúvadlo.zrusenieVstupu();
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				for (GRobot počúvajúci : GRobot.počúvajúciVstupnýRiadok)
				{
					počúvajúci.zrušenieÚdajov();
					počúvajúci.zrusenieUdajov();
					počúvajúci.zrušenieVstupu();
					počúvajúci.zrusenieVstupu();
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zrušVstup() zrušVstup}.</p> */
		public static void zrusVstup() { zrušVstup(); }


		/**
		 * <p>Overí, či je zobrazený panel vstupného riadka.</p>
		 * 
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 * 
		 * @see #neskrývajVstupnýRiadok()
		 * @see #neskrývajVstupnýRiadok(boolean)
		 * @see #skrývajVstupnýRiadok()
		 * @see #skrývajVstupnýRiadok(boolean)
		 * @see #aktivujVstupnýRiadok()
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 */
		public static boolean vstupnýRiadokZobrazený() { return panelVstupnéhoRiadka.isVisible(); }

		/** <p><a class="alias"></a> Alias pre {@link #vstupnýRiadokZobrazený() vstupnýRiadokZobrazený}.</p> */
		public static boolean vstupnyRiadokZobrazeny() { return panelVstupnéhoRiadka.isVisible(); }

		/**
		 * <p>Prikáže vstupnému riadku, aby sa neskrýval po potvrdení klávesom
		 * {@code Enter}. Ak je v čase spustenia tejto metódy skrytý, je ho
		 * potrebné zobraziť metódou {@link #začniVstup() začniVstup} alebo
		 * použiť alternatívnu metódu {@link 
		 * #neskrývajVstupnýRiadok(boolean)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda je použitá
		 * v príklade „Hra na ozvenu,“ ktorý sa nachádza v {@linkplain 
		 * Svet úvode dokumentácie tejto triedy}…</p>
		 * 
		 * @see #vstupnýRiadokZobrazený()
		 * @see #neskrývajVstupnýRiadok(boolean)
		 * @see #skrývajVstupnýRiadok()
		 * @see #skrývajVstupnýRiadok(boolean)
		 * @see #aktivujVstupnýRiadok()
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void neskrývajVstupnýRiadok()
		{ vstupnýRiadokStáleViditeľný = true; }

		/** <p><a class="alias"></a> Alias pre {@link #neskrývajVstupnýRiadok() neskrývajVstupnýRiadok}.</p> */
		public static void neskryvajVstupnyRiadok() { neskrývajVstupnýRiadok(); }

		/**
		 * <p>Prikáže vstupnému riadku, aby sa neskrýval po potvrdení klávesom
		 * {@code Enter}. Táto metóda je alternatívou metódy {@link 
		 * #neskrývajVstupnýRiadok() neskrývajVstupnýRiadok}, pričom
		 * poskytuje rozšírenú funkcionalitu opísanú nižšie…</p>
		 * 
		 * @param začniVstup ak je {@code valtrue}, vykoná sa zároveň metóda
		 *     {@link #začniVstup() začniVstup}; ak je {@code valfalse},
		 *     vstupný riadok zostane v pôvodnom stave
		 * @return ak má parameter {@code začniVstup} hodnotu {@code valtrue},
		 *     tak návratová hodnota je návratovou hodnotou metódy {@link 
		 *     #začniVstup() začniVstup}; ak má parameter {@code začniVstup}
		 *     hodnotu {@code valfalse}, tak návratová hodnota je
		 *     {@code valtrue} ak je vstupný riadok zobrazený
		 *     a {@code valfalse} v opačnom prípade
		 * 
		 * @see #vstupnýRiadokZobrazený()
		 * @see #neskrývajVstupnýRiadok()
		 * @see #skrývajVstupnýRiadok()
		 * @see #skrývajVstupnýRiadok(boolean)
		 * @see #aktivujVstupnýRiadok()
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static boolean neskrývajVstupnýRiadok(boolean začniVstup)
		{
			vstupnýRiadokStáleViditeľný = true;
			if (začniVstup) return začniVstup();
			return panelVstupnéhoRiadka.isVisible();
		}

		/** <p><a class="alias"></a> Alias pre {@link #neskrývajVstupnýRiadok(boolean) neskrývajVstupnýRiadok}.</p> */
		public static boolean neskryvajVstupnyRiadok(boolean začniVstup)
		{ return neskrývajVstupnýRiadok(začniVstup); }

		/**
		 * <p>Umožní vstupnému riadku, aby sa skryl po potvrdení klávesom
		 * {@code Enter} alebo po zrušení vstupu klávesom {@code Escape}.
		 * Toto je predvolené správanie vstupného riadka.</p>
		 * 
		 * @see #vstupnýRiadokZobrazený()
		 * @see #neskrývajVstupnýRiadok()
		 * @see #neskrývajVstupnýRiadok(boolean)
		 * @see #skrývajVstupnýRiadok(boolean)
		 * @see #aktivujVstupnýRiadok()
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void skrývajVstupnýRiadok()
		{
			vstupnýRiadokStáleViditeľný = false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #skrývajVstupnýRiadok() skrývajVstupnýRiadok}.</p> */
		public static void skryvajVstupnyRiadok() { skrývajVstupnýRiadok(); }

		/**
		 * <p>Umožní vstupnému riadku, aby sa skryl po potvrdení klávesom
		 * {@code Enter} alebo po zrušení vstupu klávesom {@code Escape}.
		 * Toto je predvolené správanie vstupného riadka. Táto metóda je
		 * alternatívou metódy {@link #skrývajVstupnýRiadok()
		 * skrývajVstupnýRiadok}, pričom poskytuje rozšírenú funkcionalitu
		 * opísanú nižšie…</p>
		 * 
		 * @param zrušVstup ak je {@code valtrue}, vykoná sa zároveň metóda
		 *     {@link #zrušVstup() zrušVstup}; ak je {@code valfalse},
		 *     vstupný riadok zostane v pôvodnom stave
		 * 
		 * @see #vstupnýRiadokZobrazený()
		 * @see #neskrývajVstupnýRiadok()
		 * @see #neskrývajVstupnýRiadok(boolean)
		 * @see #skrývajVstupnýRiadok()
		 * @see #aktivujVstupnýRiadok()
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void skrývajVstupnýRiadok(boolean zrušVstup)
		{
			vstupnýRiadokStáleViditeľný = false;
			if (zrušVstup) zrušVstup();
		}

		/** <p><a class="alias"></a> Alias pre {@link #skrývajVstupnýRiadok(boolean) skrývajVstupnýRiadok}.</p> */
		public static void skryvajVstupnyRiadok(boolean zrušVstup)
		{ skrývajVstupnýRiadok(zrušVstup); }

		/**
		 * <p>V prípade, že vstupný riadok je stále viditeľný, môžeme do neho
		 * touto metódou presmerovať vstup – v riadku sa aktivuje blikajúci
		 * kurzor.</p>
		 * 
		 * @see #vstupnýRiadokZobrazený()
		 * @see #neskrývajVstupnýRiadok()
		 * @see #neskrývajVstupnýRiadok(boolean)
		 * @see #skrývajVstupnýRiadok()
		 * @see #skrývajVstupnýRiadok(boolean)
		 * @see #začniVstup()
		 * @see #vstupnýRiadok()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void aktivujVstupnýRiadok()
		{
			vstupnýRiadok.requestFocus();
		}

		/** <p><a class="alias"></a> Alias pre {@link #aktivujVstupnýRiadok() aktivujVstupnýRiadok}.</p> */
		public static void aktivujVstupnyRiadok() { aktivujVstupnýRiadok(); }


		/**
		 * <p>Vráti komponent vstupného riadka, aby s ním mohlo byť manipulované
		 * na nižšej úrovni (úrovni bližšej k systému).</p>
		 * 
		 * @return objekt typu {@link JTextField JTextField}
		 *     reprezentujúci vstupný riadok
		 * 
		 * @see #popisVstupnéhoRiadka()
		 * @see #popisVstupnéhoRiadka(String)
		 * @see #textVstupnéhoRiadka()
		 * @see #textVstupnéhoRiadka(String)
		 * @see #pripojTextVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static JTextField vstupnýRiadok() { return vstupnýRiadok; }

		/** <p><a class="alias"></a> Alias pre {@link #vstupnýRiadok() vstupnýRiadok}.</p> */
		public static JTextField vstupnyRiadok() { return vstupnýRiadok; }

		/**
		 * <p><a class="getter"></a> Získa text popisu vstupného riadka.
		 * Ak je popis vstupného riadka skrytý, metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * @return aktuálny text popisu vstupného riadka alebo {@code valnull}
		 * 
		 * @see #vstupnýRiadok()
		 * @see #popisVstupnéhoRiadka(String)
		 * @see #textVstupnéhoRiadka()
		 * @see #textVstupnéhoRiadka(String)
		 * @see #pripojTextVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static String popisVstupnéhoRiadka()
		{
			if (!popisVstupnéhoRiadka.isVisible()) return null;
			return popisVstupnéhoRiadka.getText();
		}

		/** <p><a class="alias"></a> Alias pre {@link #popisVstupnéhoRiadka() popisVstupnéhoRiadka}.</p> */
		public static String popisVstupnehoRiadka() { return popisVstupnehoRiadka(); }

		/**
		 * <p><a class="setter"></a> Nastaví text popisu vstupného riadka. Ak bola na
		 * zobrazenie vstupného riadka použitá metóda bez určenia výzvy
		 * ({@link #začniVstup()}), popis bude po volaní tejto metódy
		 * zobrazený. Ak je táto metóda volaná s argumentom {@code valnull},
		 * popis bude skrytý.</p>
		 * 
		 * @param výzva nový text popisu vstupného riadka
		 * 
		 * @see #vstupnýRiadok()
		 * @see #popisVstupnéhoRiadka()
		 * @see #textVstupnéhoRiadka()
		 * @see #textVstupnéhoRiadka(String)
		 * @see #pripojTextVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void popisVstupnéhoRiadka(String výzva)
		{
			if (null == výzva)
			{
				popisVstupnéhoRiadka.setVisible(false);
			}
			else
			{
				popisVstupnéhoRiadka.setVisible(true);
				popisVstupnéhoRiadka.setText(výzva);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #popisVstupnéhoRiadka(String) popisVstupnéhoRiadka}.</p> */
		public static void popisVstupnehoRiadka(String výzva) { popisVstupnéhoRiadka(výzva); }

		/**
		 * <p><a class="getter"></a> Získa text aktuálne zadaný do vstupného riadka. Touto
		 * metódou je možné overiť obsah vstupného riadka ešte pred jeho
		 * potvrdením klávesom {@code Enter}.</p>
		 * 
		 * @return aktuálny text vstupného riadka
		 * 
		 * @see #vstupnýRiadok()
		 * @see #popisVstupnéhoRiadka()
		 * @see #popisVstupnéhoRiadka(String)
		 * @see #textVstupnéhoRiadka(String)
		 * @see #pripojTextVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static String textVstupnéhoRiadka() { return vstupnýRiadok.getText(); }

		/** <p><a class="alias"></a> Alias pre {@link #textVstupnéhoRiadka() textVstupnéhoRiadka}.</p> */
		public static String textVstupnehoRiadka() { return vstupnýRiadok.getText(); }

		/**
		 * <p><a class="setter"></a> Nastaví text vstupného riadka na zadanú
		 * hodnotu. Pozor, metóda {@link #začniVstup()} vždy pred zobrazením
		 * panela so vstupným riadkom vyčistí vstupný riadok (vymaže jeho
		 * obsah).</p>
		 * 
		 * @param text nový text vstupného riadka
		 * 
		 * @see #vstupnýRiadok()
		 * @see #popisVstupnéhoRiadka()
		 * @see #popisVstupnéhoRiadka(String)
		 * @see #textVstupnéhoRiadka()
		 * @see #pripojTextVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 * @see #históriaVstupnéhoRiadka()
		 */
		public static void textVstupnéhoRiadka(String text) { vstupnýRiadok.setText(text); }

		/** <p><a class="alias"></a> Alias pre {@link #textVstupnéhoRiadka(String) textVstupnéhoRiadka}.</p> */
		public static void textVstupnehoRiadka(String text) { vstupnýRiadok.setText(text); }

		/**
		 * <p>Pripojí zadaný text na koniec vstupného riadka. Volanie metódy má
		 * význam len keď je vstupný riadok {@linkplain #začniVstup()
		 * zobrazený}.</p>
		 * 
		 * @param naPripojenie text na pripojenie
		 * 
		 * @see #vstupnýRiadok()
		 * @see #popisVstupnéhoRiadka()
		 * @see #popisVstupnéhoRiadka(String)
		 * @see #textVstupnéhoRiadka()
		 * @see #textVstupnéhoRiadka(String)
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #prevezmiReťazec()
		 */
		public static void pripojTextVstupnéhoRiadka(String naPripojenie)
		{
			String textVstupnéhoRiadka = vstupnýRiadok.getText();
			vstupnýRiadok.setText(textVstupnéhoRiadka + naPripojenie);
		}

		/** <p><a class="alias"></a> Alias pre {@link #pripojTextVstupnéhoRiadka(String) pripojTextVstupnéhoRiadka}.</p> */
		public static void pripojTextVstupnehoRiadka(String naPripojenie)
		{ pripojTextVstupnéhoRiadka(naPripojenie); }


		/**
		 * <p>Vráti zoznam reťazcov histórie vstupného riadka. Na to, aby sa
		 * zoznam automaticky plnil (počas činnosti aplikácie), musí byť
		 * {@linkplain #aktivujHistóriuVstupnéhoRiadka() história aktívna}.
		 * Vrátený zoznam je autentický. Môžete ho upravovať podľa vlastných
		 * potrieb (to jest jeho vymazanie skutočne spôsobí vymazanie histórie
		 * a podobne). Históriu je tiež možné nechať automaticky {@linkplain 
		 * #uchovajHistóriuVstupnéhoRiadka() uchovať v konfiguračnom súbore},
		 * avšak musí byť aktívna aj samotná {@linkplain 
		 * #použiKonfiguráciu(String) konfigurácia}.</p>
		 * 
		 * @return zoznam reťazcov (história vstupného riadka)
		 * 
		 * @see #začniVstup()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static Zoznam<String> históriaVstupnéhoRiadka()
		{
			// Nemôžeme si byť istí, čo zákaznícky programátor
			// v zozname zmení, takže od tohto okamihu:
			históriaVstupnéhoRiadkaNezmenená = false;
			return históriaVstupnéhoRiadka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #históriaVstupnéhoRiadka() históriaVstupnéhoRiadka}.</p> */
		public static Zoznam<String> historiaVstupnehoRiadka()
		{ return históriaVstupnéhoRiadka(); }


		/**
		 * <p>Zistí, či je automatické uchovávanie potvrdených vstupných
		 * riadkov aktívne. Tento stav vypovedá len o tom, či budú potvrdené
		 * vstupné riadky uchovávané počas aktuálnej činnosti aplikácie. Na
		 * to, aby boli tieto údaje dostupné aj pri ďalšom spustení
		 * aplikácie, musí byť aktivované {@linkplain 
		 * #uchovajHistóriuVstupnéhoRiadka() uchovávanie histórie
		 * v konfiguračnom súbore} a musí byť aktívna aj samotná
		 * {@linkplain #použiKonfiguráciu(String) konfigurácia}.</p>
		 * 
		 * @return stav aktivovania automatického uchovávania histórie
		 *     vstupného riadka počas činnosti aplikácie
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static boolean aktívnaHistóriaVstupnéhoRiadka()
		{ return aktívnaHistóriaVstupnéhoRiadka; }

		/** <p><a class="alias"></a> Alias pre {@link #aktívnaHistóriaVstupnéhoRiadka() aktívnaHistóriaVstupnéhoRiadka}.</p> */
		public static boolean aktivnaHistoriaVstupnehoRiadka()
		{ return aktívnaHistóriaVstupnéhoRiadka; }

		/**
		 * <p>Aktivuje automatické uchovávanie potvrdených vstupných riadkov.
		 * Ak je navyše aktivované {@linkplain 
		 * #uchovajHistóriuVstupnéhoRiadka() uchovávanie histórie
		 * v konfiguračnom súbore} ({@linkplain #použiKonfiguráciu(String)
		 * pričom konfigurácia musí byť tiež aktívna}), tak budú uchované
		 * potvrdené riadky dostupné aj pri ďalšom spustení aplikácie.</p>
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static void aktivujHistóriuVstupnéhoRiadka()
		{ aktívnaHistóriaVstupnéhoRiadka = true; }

		/** <p><a class="alias"></a> Alias pre {@link #aktivujHistóriuVstupnéhoRiadka() aktivujHistóriuVstupnéhoRiadka}.</p> */
		public static void aktivujHistoriuVstupnehoRiadka()
		{ aktívnaHistóriaVstupnéhoRiadka = true; }

		/**
		 * <p>Deaktivuje automatické uchovávanie potvrdených vstupných
		 * riadkov počas činnosti aplikácie.</p>
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static void deaktivujHistóriuVstupnéhoRiadka()
		{ aktívnaHistóriaVstupnéhoRiadka = false; }

		/** <p><a class="alias"></a> Alias pre {@link #deaktivujHistóriuVstupnéhoRiadka() deaktivujHistóriuVstupnéhoRiadka}.</p> */
		public static void deaktivujHistoriuVstupnehoRiadka()
		{ aktívnaHistóriaVstupnéhoRiadka = false; }


		/**
		 * <p>Zistí, či je aktívne automatické uchovanie histórie
		 * potvrdených vstupných riadkov do konfiguračného súboru. Na to,
		 * aby táto vlastnosť fungovala, musí byť aktívna aj samotná
		 * {@linkplain #použiKonfiguráciu(String) konfigurácia} a na to,
		 * aby sa história riadkov automaticky plnila, musí byť aktívne
		 * automatické {@linkplain #aktivujHistóriuVstupnéhoRiadka()
		 * uchovávanie histórie počas činnosti aplikácie}.</p>
		 * 
		 * @return stav aktivovania automatického uchovávania histórie
		 *     vstupného riadka do konfiguračného súboru
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static boolean uchovávaSaHistóriaVstupnéhoRiadka()
		{ return uchovajHistóriuVstupnéhoRiadka; }

		/** <p><a class="alias"></a> Alias pre {@link #uchovávaSaHistóriaVstupnéhoRiadka() uchovávaSaHistóriaVstupnéhoRiadka}.</p> */
		public static boolean uchovavaSaHistoriaVstupnehoRiadka()
		{ return uchovajHistóriuVstupnéhoRiadka; }

		/**
		 * <p>Aktivuje automatické uchovávanie potvrdených
		 * {@linkplain #začniVstup() vstupných riadkov} do konfiguračného
		 * súboru. Na to, aby táto vlastnosť fungovala, musí byť aktívna
		 * aj samotná {@linkplain #použiKonfiguráciu(String) konfigurácia}
		 * a na to, aby sa história riadkov automaticky plnila, musí byť
		 * aktívne automatické {@linkplain #aktivujHistóriuVstupnéhoRiadka()
		 * uchovávanie histórie počas činnosti aplikácie}.</p>
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #neuchovajHistóriuVstupnéhoRiadka()
		 */
		public static void uchovajHistóriuVstupnéhoRiadka()
		{ uchovajHistóriuVstupnéhoRiadka = true; }

		/** <p><a class="alias"></a> Alias pre {@link #uchovajHistóriuVstupnéhoRiadka() uchovajHistóriuVstupnéhoRiadka}.</p> */
		public static void uchovajHistoriuVstupnehoRiadka()
		{ uchovajHistóriuVstupnéhoRiadka = true; }

		/**
		 * <p>Deaktivuje automatické uchovávanie histórie vstupných riadkov
		 * do {@linkplain #použiKonfiguráciu(String) konfiguračného
		 * súboru}.</p>
		 * 
		 * @see #začniVstup()
		 * @see #históriaVstupnéhoRiadka()
		 * @see #aktívnaHistóriaVstupnéhoRiadka()
		 * @see #aktivujHistóriuVstupnéhoRiadka()
		 * @see #deaktivujHistóriuVstupnéhoRiadka()
		 * @see #uchovávaSaHistóriaVstupnéhoRiadka()
		 * @see #uchovajHistóriuVstupnéhoRiadka()
		 */
		public static void neuchovajHistóriuVstupnéhoRiadka()
		{ uchovajHistóriuVstupnéhoRiadka = false; }

		/** <p><a class="alias"></a> Alias pre {@link #neuchovajHistóriuVstupnéhoRiadka() neuchovajHistóriuVstupnéhoRiadka}.</p> */
		public static void neuchovajHistoriuVstupnehoRiadka()
		{ uchovajHistóriuVstupnéhoRiadka = false; }


		/**
		 * <p>Vráti reťazec zadaný do vstupného riadka po potvrdení klávesom
		 * {@code Enter}. Vstup pomocou vstupného riadka sa zahajuje metódou
		 * {@link #začniVstup() začniVstup}.</p>
		 * 
		 * @return objekt typu {@link java.lang.String String} obsahujúci
		 *     text zadaný používateľom
		 * 
		 * @see #prevezmiZrušenéÚdaje()
		 * @see #prevezmiCeléČíslo()
		 * @see #prevezmiReálneČíslo()
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 */
		public static String prevezmiReťazec() { return údajeVstupnéhoRiadka.toString(); }

		/** <p><a class="alias"></a> Alias pre {@link #prevezmiReťazec() prevezmiReťazec}.</p> */
		public static String prevezmiRetazec() { return údajeVstupnéhoRiadka.toString(); }

		/**
		 * <p>Vráti reťazec, ktorý obsahoval vstupný riadok tesne pred zrušením
		 * klávesom {@code Esc}.</p>
		 * 
		 * @return objekt typu {@link java.lang.String String} obsahujúci
		 *     text vstupného riadka pred zrušením
		 * 
		 * @see #prevezmiReťazec()
		 * @see #prevezmiCeléČíslo()
		 * @see #prevezmiReálneČíslo()
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 */
		public static String prevezmiZrušenéÚdaje() { return zrušenéÚdajeVstupnéhoRiadka.toString(); }

		/** <p><a class="alias"></a> Alias pre {@link #prevezmiZrušenéÚdaje() prevezmiZrušenéÚdaje}.</p> */
		public static String prevezmiZruseneUdaje() { return zrušenéÚdajeVstupnéhoRiadka.toString(); }

		/**
		 * <p>Vráti celé číslo zadané do vstupného riadka po potvrdení
		 * klávesom {@code Enter}. Metóda vráti hodnotu {@code valnull}
		 * v prípade, že do vstupného riadka nebolo zadané celé číslo.
		 * Vstup pomocou vstupného riadka sa zahajuje metódou {@link 
		 * #začniVstup() začniVstup}.</p>
		 * 
		 * @return objekt typu {@link Long} obsahujúci zadané celé číslo,
		 *     alebo {@code valnull} v prípade chyby
		 * 
		 * @see #prevezmiReťazec()
		 * @see #prevezmiReálneČíslo()
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #formát
		 */
		public static Long prevezmiCeléČíslo()
		{
			Long číslo = null;
			try { číslo = Long.valueOf(filtrujReťazec(
				údajeVstupnéhoRiadka.toString())); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #prevezmiCeléČíslo() prevezmiCeléČíslo}.</p> */
		public static Long prevezmiCeleCislo() { return prevezmiCeléČíslo(); }

		/**
		 * <p>Vráti reálne číslo zadané do vstupného riadka po potvrdení
		 * klávesom {@code Enter}. Metóda vráti hodnotu {@code valnull}
		 * v prípade, že do vstupného riadka nebolo zadané reálne číslo.
		 * Vstup pomocou vstupného riadka sa zahajuje metódou {@link 
		 * #začniVstup() začniVstup}.</p>
		 * 
		 * @return objekt typu {@link java.lang.Double Double} obsahujúci
		 *     zadané reálne číslo alebo {@code valnull} v prípade chyby
		 * 
		 * @see #prevezmiReťazec()
		 * @see #prevezmiCeléČíslo()
		 * @see #začniVstup()
		 * @see #vstupnýRiadokZobrazený()
		 * @see #vstupnýRiadok()
		 * @see #formát
		 */
		public static Double prevezmiReálneČíslo()
		{
			Double číslo = null;
			try { číslo = Double.valueOf(filtrujReťazec(
				údajeVstupnéhoRiadka.toString())); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #prevezmiReálneČíslo() prevezmiReálneČíslo}.</p> */
		public static Double prevezmiRealneCislo()
		{ return prevezmiReálneČíslo(); }


		/**
		 * <p>Pokúsi sa previesť zadaný reťazec na celé číslo. V prípade
		 * chyby pri prevode vráti metóda hodnotu {@code valnull}.</p>
		 * 
		 * @param reťazec reťazec s údajom na prevedenie
		 * @return objekt typu {@link Long} obsahujúci prevedené celé číslo,
		 *     alebo {@code valnull} v prípade chyby
		 * 
		 * @see #reťazecNaReálneČíslo(String)
		 * @see #formát
		 */
		public static Long reťazecNaCeléČíslo(String reťazec)
		{
			Long číslo = null;
			try { číslo = Long.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #reťazecNaCeléČíslo(String) reťazecNaCeléČíslo}.</p> */
		public static Long retazecNaCeleCislo(String reťazec) { return reťazecNaCeléČíslo(reťazec); }

		/**
		 * <p>Pokúsi sa previesť zadaný reťazec na reálne číslo. V prípade
		 * chyby pri prevode vráti metóda hodnotu {@code valnull}.</p>
		 * 
		 * @param reťazec reťazec s údajom na prevedenie
		 * @return objekt typu {@link java.lang.Double Double} obsahujúci
		 *     prevedené reálne číslo alebo {@code valnull} v prípade chyby
		 * 
		 * @see #reťazecNaCeléČíslo(String)
		 * @see #formát
		 */
		public static Double reťazecNaReálneČíslo(String reťazec)
		{
			Double číslo = null;
			try { číslo = Double.valueOf(filtrujReťazec(reťazec)); }
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); return null; }
			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #reťazecNaReálneČíslo(String) reťazecNaReálneČíslo}.</p> */
		public static Double retazecNaRealneCislo(String reťazec)
		{ return reťazecNaReálneČíslo(reťazec); }


		// Pole hodnôt slúžiacich na prevod číselných hodnôt na rímsky
		// numerický zápis. Hodnoty tohto poľa slúžia na odoberanie
		// stanovených číselných objemov zo zdrojového čísla počas prevodu
		// na rímsky zápis.
		private static int[] rímskeHodnoty =
		{
			1_000_000_000, 900_000_000, 500_000_000, 400_000_000, 100_000_000,
			90_000_000, 50_000_000, 40_000_000, 10_000_000, 9_000_000,
			5_000_000, 4_000_000, 1_000_000, 900_000, 500_000, 400_000,
			100_000, 90_000, 50_000, 40_000, 10_000, 9_000, 5_000, 4_000,
			1_000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
		};

		// Pole reťazcov používaných pri prevode číselných hodnôt na rímsky
		// numerický zápis. Reťazce v tomto poli sú pridávané do výsledného
		// zápisu podľa najvyššieho nájdeného objemu v poli „rímskeHodnoty.“
		private static String[] rímskeČíslice =
		{
			"M̿", "C̿M̿", "D̿", "C̿D̿", "C̿", "X̿C̿", "L̿", "X̿L̿", "X̿", "M̅X̿", "V̿", "M̅V̿",
			"M̅", "C̅M̅", "D̅", "C̅D̅", "C̅", "X̅C̅", "L̅", "X̅L̅", "X̅", "MX̅", "V̅", "MV̅",
			"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV",
			"I"
		};

		/**
		 * <p>Prevedie zadané celé číslo na reťazec s rímskou reprezentáciou
		 * celých čísiel. Metóda umožňuje vykonať reverzný proces k metóde
		 * {@link #rímskeNaCelé(String) rímskeNaCelé}, pričom výsledok nijako
		 * neoptimalizuje (v zmysle, že jej cieľom nie je dosiahnuť čo
		 * najkratšiu reprezentáciu celočíselnej hodnoty). Naopak, prevod je
		 * značne priamočiary s použitím vnútornej zotriedenej tabuľky
		 * obsahujúcej predpísané hodnoty použité pri prevode ako napríklad:
		 * M (1000), CM (900), D (500), CD (400) atď. Použité sú len veľké
		 * písmená. Písmeno O reprezentuje nulu. Záporné hodnoty sú vyjadrené
		 * uvedením Unicode znaku mínus (−) na začiatku prevedeného reťazca.
		 * Hodnoty vyššie od tisíc (písmeno M) sú reprezentované s použitím
		 * Unicode kombinačných znakov, ktoré však 2D stroj Javy nevie
		 * korektne zobraziť – podrobnosti sú uvedené v opise metódy {@link 
		 * #rímskeNaCelé(String) rímskeNaCelé}.</p>
		 * 
		 * @param celé celé číslo určené na prevod
		 * @return rímska reprezentácia hodnoty
		 * 
		 * @see #rímskeNaCelé(String)
		 */
		public static String celéNaRímske(long celé)
		{
			// Nulu budeme reprezentovať písmenom O.
			if (0 == celé) return "O";

			// Zásobník, do ktorého sa bude zostavovať reťazec rímskej
			// reprezetnácie zadaného celého čísla.
			StringBuffer rímske = new StringBuffer();

			// Ak je zadané číslo záporné, pridáme pred reťazec rímskej
			// reprezentácie znamienko mínus (−) a ďalej pracujeme s kladnou
			// hodnotou.
			if (celé < 0)
			{
				rímske.append("−");
				celé = -celé;
			}

			// Premennú parametra „celé“ si môžeme dovoliť „skartovať“.
			// Budeme z nej postupne odoberať hodnoty z poľa rímskeHodnoty
			// a súčasne s tým pridávať ekvivalentné reťazce do zásobníka
			// „rímske“.
			for (int i = 0; i < rímskeHodnoty.length; ++i)
			{
				while (celé >= rímskeHodnoty[i])
				{
					rímske.append(rímskeČíslice[i]);
					celé -= rímskeHodnoty[i];
				}
			}

			// Nakoniec prevedieme obsah zásobníka na reťazec, ktorý bude
			// vrátený ako výsledok prevodu.
			return rímske.toString();
		}

		/** <p><a class="alias"></a> Alias pre {@link #celéNaRímske(long) celéNaRímske}.</p> */
		public static String celeNaRimske(long celé)
		{ return celéNaRímske(celé); }

		// Toto je pomocná metóda na prevod rímskych čísiel na celé
		// čísla. Vracia hodnotu prijatého rímskeho znaku, ktorý je
		// známy z pohľadu tu implementovaný spôsob prevodu alebo
		// hodnotu −1, ak je znak neznámy.
		private static int hodnotaRímskehoZnaku(char znak)
		{
			switch (znak)
			{
			case 'O': case 'o': return 0;
			case 'I': case 'i': return 1;
			case 'V': case 'v': return 5;
			case 'X': case 'x': return 10;
			case 'L': case 'l': return 50;
			case 'C': case 'c': return 100;
			case 'D': case 'd': return 500;
			case 'M': case 'm': return 1000;
			}
			return -1;
		}

		// Toto je pomocná metóda na prevod rímskych čísiel na celé
		// čísla. Ak sa na zadanej pozícii v rámci zadaného reťazca
		// obsahujúceho rímsky číselný zápis nachádza niektorý
		// z modifikátorov hodnoty rímskeho znaku, ktorý je známy
		// z pohľadu tu implementovaného spôsobu prevodu, tak vráti
		// jeho činiteľ, inak vráti hodnotu 1.
		private static int rímskyModifikátor(String rímske, int pozícia)
		{
			if (pozícia >= rímske.length()) return 1;

			char znak = rímske.charAt(pozícia);
			switch (znak)
			{
			case '̅': return 1000;
			case '̿': return 1000000;
			}
			return 1;
		}

		/**
		 * <p>Prevedie zadaný reťazec obsahujúci číslo reprezentované
		 * v rímskej číselnej sústave na celé číslo.</p>
		 * 
		 * <p>Metóda je značne liberálna. Znak mínus na začiatku reťazca
		 * spôsobí, že analyzované číslo bude prevedené na záporné, písmeno
		 * O je považované za nulu (malé aj veľké) a metóda rozpoznáva aj
		 * dva Unicode modifikátory, ktoré násobia hodnotu rímskeho znaku
		 * tisícom alebo miliónom. Ide o kombinačné znaky jednoduchej
		 * a dvojitej čiary nad predchádzajúcim znakom:  ̅,  ̿. Jednoduchá
		 * čiara násobí hodnotu predchádzajúceho písmena tisícom a dvojitá
		 * miliónom. Nevýhodou týchto špeciálnych znakov je to, že vnútorná
		 * konzola plátien a pečiatkové texty programovacieho rámca ich
		 * nevedia korektne zobraziť z dôvodu predvoleného spôsobu
		 * vykresľovania textov 2D strojom jazyka Java a tiež to, že
		 * v niektorých softvéroch sú tieto znaky zobrazované nad
		 * nasledujúcim znakom namiesto predchádzajúceho. Ak sú však
		 * v zadanom reťazci prítomné, tak ich táto metóda berie do úvahy
		 * a priraďuje ich vždy k predchádzajúcemu symbolu.</p>
		 * 
		 * <p>Platné rímske číslice sú malé alebo veľké písmená: I (1),
		 * V (5), X (10), L (50), C (100), D (500) a M (1000). Ak je séria
		 * znakov s nižšou hodnotou uvedená pred znakom s vyššou hodnotou,
		 * tak je súčet hodnôt znakov s nižšou hodnotou od výsledku odrátaný
		 * a hodnota znaku s vyššou je okamžite spracovaná tak, že je
		 * k výsledku prirátaná bez ohľadu na to, či za ňou nasledujú ďalšie
		 * znaky s rovnakou hodnotou alebo znak s ešte vyššou hodnotou. To
		 * znamená, že napríklad reťazec IIIVVX má hodnotu 7 (čiže je
		 * oddelene spracovaná séria IIIV = 2 a VX = 5), pričom samostatný
		 * reťazec VVX má hodnotu 0.</p>
		 * 
		 * @param rímske rímska reprezentácia celého čísla
		 * @return prevedená hodnota
		 * 
		 * @see #celéNaRímske(long)
		 */
		public static Long rímskeNaCelé(String rímske)
		{
			// Prázdne alebo nulové reťazce spôsobia vrátenie hodnoty null.
			if (null == rímske) return null;
			rímske = rímske.trim();
			if (rímske.isEmpty()) return null;

			// Množina rôznych znakov, ktoré sa podobajú na znak mínus
			// sú považované za znak negácie a ak sa nachádzajú na začiatku
			// reťazca, tak sú vyhodnotené ako platné a sú z reťazca
			// odstránené. (Informácia o tom sa uloží do príznaku „záporné“.)
			boolean záporné;
			if ('-' == rímske.charAt(0) || '–' == rímske.charAt(0) ||
				'—' == rímske.charAt(0) || '−' == rímske.charAt(0) ||
				'―' == rímske.charAt(0) || '‒' == rímske.charAt(0) ||
				'­' == rímske.charAt(0) || '‑' == rímske.charAt(0))
			{
				záporné = true;
				rímske = rímske.substring(1);
				if (rímske.isEmpty()) return null;
			}
			else záporné = false;

			int začiatok = 1, modifikátor;

			// Pokúsi sa identifikovať prvý znak v reťazci. Najprv sa
			// berie do úvahy platná hodnota rímskeho znaku. Neplatné
			// znaky spôsobia okamžité vrátenie hodnoty null. Potom sa
			// s pomocou ďalšej metódy pokúsi algoritmus zvýšiť
			// identifikovanú hodnotu o vrátený činiteľ. (To platí aj
			// pri všetkých ďalších pokusoch o identifikáciu znaku.)
			int posledná = hodnotaRímskehoZnaku(rímske.charAt(0));
			if (-1 == posledná) return null;
			if (1 != (modifikátor = rímskyModifikátor(rímske, 1)))
			{
				posledná *= modifikátor;
				++začiatok;
			}
			int priebežná = posledná;

			long výsledok = 0;
			int dĺžka = rímske.length();

			// Potom sú identifikované všetky ďalšie znaky podľa pravidiel
			// rímskeho zápisu. Séria znakov s nižšou hodnotou pred znakom
			// s vyššou hodnotou je od výsledku odčítaná, ostatné znaky
			// sú k výsledku pripočítané. (Pripočítaním a odpočítaním sa
			// myslí na hodnoty, ktoré znaky reprezentujú.)
			for (int i = začiatok; i < dĺžka; ++i)
			{
				// Zistí sa hodnota aktuálneho znaku.
				int hodnota = hodnotaRímskehoZnaku(rímske.charAt(i));
				if (-1 == hodnota) return null;
				if (1 != (modifikátor = rímskyModifikátor(rímske, i + 1)))
				{
					hodnota *= modifikátor;
					++i;
				}

				if (posledná == hodnota)
					// Rovnaké znaky sa kumulujú.
					priebežná += hodnota;
				else if (posledná < hodnota)
				{
					// Ak je hodnota posledného znaku nižšia než hodnota
					// aktuálneho znaku, tak je nakumulovaná hodnota
					// odčítaná a aktuálna hodnota pripočítaná k výsledku.
					výsledok -= priebežná;
					výsledok += hodnota;

					// Aktuálny znak je preskočený a pokračuje sa ďalším
					// znakom nasledujúcim po ňom. (To znamená, že aktuálny
					// znak sa v tomto prípade neukumuluje s prípadnými
					// ďalšími rovnakými znakmi.)
					if (++i < dĺžka)
					{
						posledná = hodnotaRímskehoZnaku(rímske.charAt(i));
						if (-1 == posledná) return null;
						if (1 != (modifikátor = rímskyModifikátor(
							rímske, i + 1)))
						{
							posledná *= modifikátor;
							++i;
						}
						priebežná = posledná;
					}
					else priebežná = 0;
				}
				else
				{
					// Ak nie je hodnota posledného znaku menšia ani rovná
					// hodnote aktuálneho znaku (predchádzajúce podmienky to
					// eliminovali), tak sa nakumulovaná hodnota pripočíta
					// a pokračuje sa ďalej…
					výsledok += priebežná;
					priebežná = posledná = hodnota;
				}
			}

			// Nakoniec sa k výsledku pripočíta prípadná zostávajúca
			// nakumulovaná hodnota.
			výsledok += priebežná;

			// Ak bolo zo začiatku reťazca odstránené znamienko negácie,
			// tak výsledok negujeme…
			if (záporné) return -výsledok;
			return výsledok;
		}

		/** <p><a class="alias"></a> Alias pre {@link #rímskeNaCelé(String) rímskeNaCelé}.</p> */
		public static long rimskeNaCele(String reťazec)
		{ return rímskeNaCelé(reťazec); }


		// Štandardný vstup

		/**
		 * <p>Táto metóda vykoná jednorazovú akciu aktivovania štandardného
		 * vstupu s predvoleným kódovaním UTF-8. Jej volanie má zmysel len
		 * v prípade, že vstup ešte nebol úspešne aktivovaný. (Vstup nesmie
		 * byť aktívny, ani ukončený.) Skrytá inštancia prijímajúca údaje
		 * zo vstupu funguje asynchrónne v samostatnom vlákne. Pri prijatí
		 * údajov volá reakciu {@link 
		 * ObsluhaUdalostí#spracujRiadokVstupu(String) spracujRiadokVstupu}
		 * a z toho vyplýva, že v čase aktivovania štandardného vstupu by
		 * mala byť aktívna {@linkplain ObsluhaUdalostí obsluha udalostí},
		 * inak môže nastať strata prijatých údajov, najmä ak je štandardný
		 * vstup pripojený k externému prúdu vstupných údajov (napríklad
		 * z iného procesu alebo zo súboru). V prípade konečného vstupného
		 * prúdu je po dosiahnutí jeho konca spustená reakcia {@link 
		 * ObsluhaUdalostí#koniecVstupu() koniecVstupu}. Overiť to, či je
		 * štandardný vstup (stále) aktívny sa dá s pomocou metódy {@link 
		 * #štandardnýVstupAktívny() štandardnýVstupAktívny}. Určiť iné
		 * kódovanie údajov zo štandardného vstupu sa dá volaním inej
		 * verzie tejto metódy: {@link #aktivujŠtandardnýVstup(String)
		 * aktivujŠtandardnýVstup(kódovanie)}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} ŠtandardnýVstupNaVnútornúKonzolu {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} ŠtandardnýVstupNaVnútornúKonzolu()
				{
					{@link Svet Svet}.{@link Svet#aktivujŠtandardnýVstup() aktivujŠtandardnýVstup}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#spracujRiadokVstupu(java.lang.String) spracujRiadokVstupu}(String riadokVstupu)
				{
					{@link Svet Svet}.{@link Svet#farbaTextu(java.awt.Color) farbaTextu}({@link Farebnosť#tmavohnedá tmavohnedá});
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}(riadokVstupu);
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#koniecVstupu() koniecVstupu}()
				{
					{@link Svet Svet}.{@link Svet#farbaTextu(java.awt.Color) farbaTextu}({@link Farebnosť#oranžová oranžová});
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"koniec"});
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(java.lang.String) použiKonfiguráciu}({@code srg"ŠtandardnýVstupNaVnútornúKonzolu.cfg"});
					{@code kwdnew} ŠtandardnýVstupNaVnútornúKonzolu();
				}
			}
			</pre>
		 * <!-- TODO pridať výsledok príkladu použitia -->
		 * 
		 * @return ak bol štandardný vstup bezchybne aktivovaný, tak táto
		 *     metóda vráti hodnotu {@code valtrue}
		 * 
		 * @see #aktivujŠtandardnýVstup(String)
		 * @see #štandardnýVstupAktívny()
		 * @see #čakajNaVstup()
		 * @see ObsluhaUdalostí#spracujRiadokVstupu(String)
		 * @see ObsluhaUdalostí#koniecVstupu()
		 */
		public static boolean aktivujŠtandardnýVstup()
		{
			if (null == štandardnýVstup)
			{
				try { štandardnýVstup = new ŠtandardnýVstup(false); }
				catch (UnsupportedEncodingException e)
				{ GRobotException.vypíšChybovéHlásenia(e, false); }
				return null != štandardnýVstup;
			}
			else return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #aktivujŠtandardnýVstup() aktivujŠtandardnýVstup}.</p> */
		public static boolean aktivujStandardnyVstup()
		{ return aktivujŠtandardnýVstup(); }

		/**
		 * <p>Táto metóda vykoná jednorazovú akciu aktivovania štandardného
		 * vstupu so zadaným kódovaním. Platia pre ňu rovnaké pravidlá ako
		 * pre jej verziu bez parametra: {@link #aktivujŠtandardnýVstup()
		 * aktivujŠtandardnýVstup}{@code ()}.</p>
		 * 
		 * @param kódovanie kódovanie, ktoré má byť použité na dekódovanie
		 *     údajov prijímaných zo štandardného vstupu
		 * @return ak bol štandardný vstup bezchybne aktivovaný, tak táto
		 *     metóda vráti hodnotu {@code valtrue}
		 * 
		 * @see #aktivujŠtandardnýVstup()
		 * @see #štandardnýVstupAktívny()
		 * @see #čakajNaVstup()
		 * @see ObsluhaUdalostí#spracujRiadokVstupu(String)
		 * @see ObsluhaUdalostí#koniecVstupu()
		 */
		public static boolean aktivujŠtandardnýVstup(String kódovanie)
		{
			if (null == štandardnýVstup)
			{
				try { štandardnýVstup = new ŠtandardnýVstup(kódovanie, false); }
				catch (UnsupportedEncodingException e)
				{ GRobotException.vypíšChybovéHlásenia(e); }
				return null != štandardnýVstup;
			}
			else return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #aktivujŠtandardnýVstup(String) aktivujŠtandardnýVstup}.</p> */
		public static boolean aktivujStandardnyVstup(String kódovanie)
		{ return aktivujŠtandardnýVstup(kódovanie); }

		/**
		 * <p>Overí, či je štandardný vstup, ktorý bol aktivovaný metódou
		 * {@link #aktivujŠtandardnýVstup() aktivujŠtandardnýVstup} (alebo
		 * niektorou jej verziou) stále aktívny.</p>
		 * 
		 * @return ak je štandardný vstup stále aktívny, tak táto metóda
		 *     vráti hodnotu {@code valtrue}
		 * 
		 * @see #aktivujŠtandardnýVstup()
		 * @see #aktivujŠtandardnýVstup(String)
		 * @see #čakajNaVstup()
		 */
		public static boolean štandardnýVstupAktívny()
		{ return null != štandardnýVstup && !štandardnýVstup.koniecVstupu; }

		/** <p><a class="alias"></a> Alias pre {@link #štandardnýVstupAktívny() štandardnýVstupAktívny}.</p> */
		public static boolean standardnyVstupAktivny()
		{ return štandardnýVstupAktívny(); }

		/**
		 * <p>Táto metóda implementuje mechanizmus blokovania pri čakaní
		 * údajov zo štandardného vstupu (v samostatnom vlákne). Ak nie je
		 * {@linkplain #štandardnýVstupAktívny() vstup aktívny}, tak ho
		 * {@linkplain #aktivujŠtandardnýVstup() aktivuje} (s použitím
		 * predvoleného kódovania UTF-8). Ak už nie je možné prijať žiadne
		 * ďalšie údaje (vstupný prúd bol konečný a už sa skončil), tak
		 * táto metóda bez ďalšieho čakania vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <!-- TODO dokončiť vzhľad a výsledok príkladu použitia -->
		 * <pre CLASS="example">
			System.out.print("Zadaj svoje meno: ");
			String meno = Svet.čakajNaVstup();
			System.out.println("Ahoj, " + meno + "!");
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <!-- TODO dokončiť vzhľad a výsledok príkladu použitia -->
		 * <pre CLASS="example">
			import knižnica.*;

			public class ČakanieNaVstup extends GRobot
			{
				public static void main(String[] args)
				{
					// Dôležitá poznámka:
					// ------------------
					// 
					// Tento komentár obsahuje kód ukazujúci štandardný postup pri
					// čítaní údajov zo štandardného vstupu, ktorý nefunguje v kombinácii
					// so spôsobom čítania vstupu programovacím rámcom GRobot. Ak by sme
					// sa pokúsili použiť oboje, vznikla by výnimka.
					// 
					// try (java.io.BufferedReader čítač = new java.io.BufferedReader(
					// 	new java.io.InputStreamReader(System.in)))
					// {
					// 	String riadokVstupu = čítač.readLine();
					// 	System.out.println("Riadok vstupu: " + riadokVstupu);
					// }
					// catch (java.io.IOException e)
					// {
					// 	e.printStackTrace();
					// }
					// System.out.println("Vstupný prúd bol automaticky zavretý.");
					// 
					// Nasleduje skutočný príklad použitia čítania štandardného vstupu
					// s použitím programovacieho rámca GRobot…


					// Počet určíme s pomocou reťazca zadaného do prvého argumentu
					// aplikácie, pričom najmenší povolený počet je 1.
					int počet = 1;
					if (args.length > 0)
					{
						Long číslo = Svet.reťazecNaCeléČíslo(args[0]);
						if (null != číslo) počet = číslo.intValue();
					}
					if (počet <= 0) počet = 1;
					System.out.println("(Počet reťazcov: " + počet + ")");

					// Prečítame zo štandardného vstupu určený počet reťazcov.
					for (int i = 1; i <= počet; ++i)
					{
						System.out.print("Zadaj reťazec " + i + ": ");
						String vstup = Svet.čakajNaVstup();
						System.out.println("> " + vstup);
					}

					// Aplikáciu musíme „násilne“ uzavrieť, pretože z dôvodu prítomnosti
					// automaticky vytváraných objektov programovacieho rámca v pamäti,
					// by sa nebola schopná ukončiť spontánne. Fungovala by naďalej
					// na pozadí.
					Svet.koniec(0);
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * 
		 * @return reťazec prečítaný zo štandardného vstupu alebo
		 *     {@code valnull}
		 * 
		 * @see #aktivujŠtandardnýVstup()
		 * @see #aktivujŠtandardnýVstup(String)
		 * @see #štandardnýVstupAktívny()
		 */
		public static String čakajNaVstup()
		{
			if (null == štandardnýVstup)
			{
				try { štandardnýVstup = new ŠtandardnýVstup(true); }
				catch (UnsupportedEncodingException e)
				{ GRobotException.vypíšChybovéHlásenia(e, false); }
			}
			else if (null == štandardnýVstup.zásobník)
				štandardnýVstup.zbierajVstup();

			if (null != štandardnýVstup &&
				null != štandardnýVstup.zásobník &&
				(!štandardnýVstup.koniecVstupu ||
					!štandardnýVstup.zásobník.isEmpty()))
			{
				while (štandardnýVstup.zásobník.isEmpty())
				{
					if (štandardnýVstup.koniecVstupu) return null;
					try { Thread.sleep(200); }
					catch (InterruptedException ie) {} // …
				}
				return štandardnýVstup.zásobník.remove();
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaVstup() čakajNaVstup}.</p> */
		public static String cakajNaVstup() { return čakajNaVstup(); }

		/**
		 * <p>Slúži na pozastavenie (blokovanie) programu s cieľom čakania
		 * na stlačenie klávesu. Ide o čakanie na zadanie znaku (z toho
		 * vyplýva, že napríklad funkčné klávesy nie sú brané do úvahy).
		 * Znak je týmto procesom skonzumovaný (a vrátený v návratovej
		 * hodnote tejto metódy). To znamená, že prislúchajúce reakcie
		 * {@link ObsluhaUdalostí#zadanieZnaku() zadanieZnaku} nie sú
		 * spustené. Na to, aby bola táto metóda použiteľná, musí byť
		 * {@link Svet Svet} inicializovaný, inak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Návratovou hodnotou je udalosť {@link 
		 * KeyEvent KeyEvent}. Použite jej metódu {@link KeyEvent#getKeyChar()
		 * getKeyChar} na získanie stlačeného znaku.</p>
		 * 
		 * <!-- TODO: Overiť, kedy sú tieto metódy bez rizika použiteľné
		 * a uviesť to do dokumentácie. -->
		 * 
		 * @return udalosť klávesnice alebo hodnota {@code valnull}
		 */
		public static KeyEvent čakajNaKláves()
		{
			if (inicializované && !čakajNaKláves && !čakajNaKlik)
			{
				čakajNaKláves = true;
				while (čakajNaKláves)
				{
					try { Thread.sleep(300); }
					// Jedna z mála natvrdo ignorovaných výnimiek:
					catch (InterruptedException ie) {}
				}
				return udalosťČakaniaNaKláves;
			}
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaKláves() čakajNaKláves}.</p> */
		public static KeyEvent cakajNaKlaves() { return čakajNaKláves(); }

		/**
		 * <p>Slúži na pozastavenie (blokovanie) programu s cieľom čakania
		 * na kliknutie ľubovoľným tlačidlom myši. Udalosť je týmto procesom
		 * skonzumovaná (a vrátená v návratovej hodnote tejto metódy). To
		 * znamená, že prislúchajúce reakcie {@link 
		 * ObsluhaUdalostí#klik() klik} nie sú spustené. Na to, aby bola táto
		 * metóda použiteľná, musí byť {@link Svet Svet} inicializovaný, inak
		 * metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Návratovou hodnotou je udalosť {@link 
		 * MouseEvent MouseEvent}. Môžete použiť jej metódy {@link 
		 * MouseEvent#getButton() getButton}, {@link MouseEvent#getX() getX},
		 * {@link MouseEvent#getY() getY} a podobne na získanie podrobností
		 * o udalosti.</p>
		 * 
		 * @return udalosť myši alebo hodnota {@code valnull}
		 */
		public static MouseEvent čakajNaKlik()
		{
			if (inicializované && !čakajNaKláves && !čakajNaKlik)
			{
				čakajNaKlik = true;
				while (čakajNaKlik)
				{
					try { Thread.sleep(300); }
					// Jedna z mála natvrdo ignorovaných výnimiek:
					catch (InterruptedException ie) {}
				}
				return udalosťČakaniaNaKlik;
			}
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaKlik() čakajNaKlik}.</p> */
		public static MouseEvent cakajNaKlik() { return čakajNaKlik(); }

		/**
		 * <p>Slúži na pozastavenie (blokovanie) programu s cieľom čakania
		 * na stlačenie ľubovoľného klávesu alebo kliknutie ľubovoľným
		 * tlačidlom myši. Návratovou hodnotou je dvojprvkové pole udalostí
		 * všeobecného typu: {@link InputEvent InputEvent}. Prvý prvok je
		 * rezervovaný pre udalosť klávesnice ({@link KeyEvent KeyEvent})
		 * a druhý pre udalosť myši ({@link MouseEvent MouseEvent}). Tá
		 * udalosť, ktorá vznikne skôr, je týmto procesom skonzumovaná
		 * a vrátená v prislúchajúcom prvku návratovej hodnoty tejto metódy.
		 * (To znamená, že prislúchajúce reakcie {@link 
		 * ObsluhaUdalostí#zadanieZnaku() zadanieZnaku} a {@link 
		 * ObsluhaUdalostí#klik() klik} nie sú spustené.) Ten prvok
		 * návratovej hodnoty, ktorého udalosť nevznikla, bude nastavený na
		 * hodnotu {@code valnull}. Na to, aby bola táto metóda použiteľná,
		 * musí byť {@link Svet Svet} inicializovaný, inak bude pole
		 * návratovej hodnoty obsahovať dve hodnoty {@code valnull}.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Pozri aj opisy metód {@link 
		 * #čakajNaKláves() čakajNaKláves} a {@link #čakajNaKlik()
		 * čakajNaKlik}.</p>
		 * 
		 * <!-- TODO spracovať príklad čakania: -->
		 * <pre CLASS="example">
			import knižnica.GRobot;
			import knižnica.Svet;

			import java.awt.event.InputEvent;
			import java.awt.event.KeyEvent;
			import java.awt.event.MouseEvent;

			public class TestČakania extends GRobot
			{
				private KeyEvent kláves;
				private MouseEvent klik;
				private InputEvent[] udalosti;

				private TestČakania()
				{
					Svet.vypíšRiadok("Stlačte ľubovoľný kláves alebo kliknite " +
						"ľubovoľným tlačidlom myši.");
					Svet.vypíšRiadok("(Kliknutie ľavého tlačidla myši " +
						"ukončí aplikáciu.)");

					do
					{
						udalosti = Svet.čakajNaKlikAleboKláves();
						kláves = (KeyEvent)udalosti[0];
						klik = (MouseEvent)udalosti[1];

						if (null != kláves)
							Svet.vypíšRiadok("ASCII (", (int)kláves.getKeyChar(),
								")", kláves.getKeyChar());

						if (null != klik)
							Svet.vypíšRiadok(klik.getButton() + ": " +
								klik.getX() + ", " + klik.getY());
					}
					while (null == klik || 1 != klik.getButton());
				}

				public static void main(String[] args)
				{
					Svet.použiKonfiguráciu("TestČakania.cfg");
					new TestČakania();
					Svet.vypíšRiadok("Koniec.");
					Svet.čakaj(1.5);
					Svet.koniec();
				}
			}
			</pre>
		 * 
		 * @return dvojprvkové pole vstupných udalostí všeobecného typu
		 *     ({@link InputEvent InputEvent})
		 */
		public static InputEvent[] čakajNaKlikAleboKláves()
		{
			if (inicializované && !čakajNaKláves && !čakajNaKlik)
			{
				čakajNaKláves = čakajNaKlik = true;
				while (čakajNaKláves && čakajNaKlik)
				{
					try { Thread.sleep(300); }
					// Jedna z mála natvrdo ignorovaných výnimiek:
					catch (InterruptedException ie) {}
				}
				čakajNaKláves = čakajNaKlik = false;

				return new InputEvent[] {udalosťČakaniaNaKláves,
					udalosťČakaniaNaKlik};
			}
			return new InputEvent[] {null, null};
		}

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaKlikAleboKláves() čakajNaKlikAleboKláves}.</p> */
		public static InputEvent[] cakajNaKlikAleboKlaves() { return čakajNaKlikAleboKláves(); }

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaKlikAleboKláves() čakajNaKlikAleboKláves}.</p> */
		public static InputEvent[] čakajNaKlávesAleboKlik() { return čakajNaKlikAleboKláves(); }

		/** <p><a class="alias"></a> Alias pre {@link #čakajNaKlikAleboKláves() čakajNaKlikAleboKláves}.</p> */
		public static InputEvent[] cakajNaKlavesAleboKlik() { return čakajNaKlikAleboKláves(); }


		// Interaktívny režim

			// Príznak aktivácie interaktívneho režimu pre svet
			/*packagePrivate*/ static boolean interaktívnyRežim = false;

		/**
		 * <p>Zapne alebo vypne režim ladenia programovacieho rámca GRobot.
		 * V režime ladenia sú vypisované rôzne chybové hlásenia na
		 * štandardný chybový výstup (systémový terminál) a informácie
		 * o vykonávaní príkazov {@linkplain #interaktívnyRežim(boolean)
		 * interaktívneho režimu} alebo {@linkplain #vykonajSkript(String[])
		 * skriptu} na vnútornú konzolu programovacieho rámca (na strop). Tiež
		 * je pravidelne spúšťaná reakcia obsluhy udalostí {@link 
		 * ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ak je
		 * definovaná obsluha udalostí.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Zapnutie výpisu chybových
		 * hlásení na štandardný chybový výstup nemusí byť vždy žiaduce,
		 * preto vznikla ďalšia verzia tejto metódy: {@link 
		 * #režimLadenia(boolean, boolean) režimLadenia}{@code (zapniLadenie,
		 * vypíšChybovéHlásenia)}.</p>
		 * 
		 * @param zapniLadenie {@code valtrue} alebo {@code valfalse}
		 * 
		 * @see #režimLadenia()
		 * @see #lenRežimLadenia()
		 * @see #režimLadenia(boolean, boolean)
		 * @see #interaktívnyRežim(boolean)
		 * @see #vykonajPríkaz(String)
		 * @see #vykonajSkript(String[])
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public static void režimLadenia(boolean zapniLadenie)
		{ GRobotException.vypíšChybovéHlásenia = Skript.ladenie = zapniLadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #režimLadenia(boolean) režimLadenia}.</p> */
		public static void rezimLadenia(boolean zapniLadenie)
		{ režimLadenia(zapniLadenie); }

		/**
		 * <p>Zapne alebo vypne režim ladenia programovacieho rámca GRobot so
		 * spresnením zapnutia alebo vypnutia výpisu chybových hlásení
		 * programovacieho rámca. Vypnutie výpisu chybových hlásení na štandardný
		 * chybový výstup (systémový terminál) nevypne výpis informácií
		 * o vykonávaní príkazov {@linkplain #interaktívnyRežim(boolean)
		 * interaktívneho režimu} alebo {@linkplain #vykonajSkript(String[])
		 * skriptu} na vnútornú konzolu programovacieho rámca (na strop). Toto
		 * správanie sa dá ovplyvniť v tele reakcie {@link 
		 * ObsluhaUdalostí#ladenie(int, String, int) ladenie} (pozri napríklad
		 * správy ladenia: {@link GRobot#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}, {@link 
		 * GRobot#VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK} alebo {@link 
		 * GRobot#VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}).</p>
		 * 
		 * @param zapniLadenie {@code valtrue} alebo {@code valfalse}
		 * @param vypíšChybovéHlásenia {@code valtrue} alebo {@code valfalse}
		 * 
		 * @see #režimLadenia()
		 * @see #lenRežimLadenia()
		 * @see #režimLadenia(boolean)
		 * @see #výpisChybovýchHlásení()
		 * @see #interaktívnyRežim(boolean)
		 * @see #vykonajPríkaz(String)
		 * @see #vykonajSkript(String[])
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public static void režimLadenia(boolean zapniLadenie,
			boolean vypíšChybovéHlásenia)
		{
			// TODO – pridať reakciu vzniklaVýnimka(GRobotException výnimka)…
			Skript.ladenie = zapniLadenie;
			GRobotException.vypíšChybovéHlásenia = vypíšChybovéHlásenia;
		}

		/** <p><a class="alias"></a> Alias pre {@link #režimLadenia(boolean, boolean) režimLadenia}.</p> */
		public static void rezimLadenia(boolean zapniLadenie,
			boolean vypíšChybovéHlásenia)
		{ režimLadenia(zapniLadenie, vypíšChybovéHlásenia); }

		/**
		 * <p>Zistí, či je zapnutý režim ladenia programovacieho rámca
		 * GRobot alebo aspoň výpis chybových hlásení programovacieho rámca.
		 * (Stačí, ak je aktívna aspoň jedna z vlastností. Pozri: {@link 
		 * #režimLadenia(boolean, boolean) režimLadenia}. Táto metóda bola
		 * takto navrhnutá z dôvodu kompatibility s jej jednoparametrickou
		 * verziou: {@link #režimLadenia(boolean)
		 * režimLadenia(zapniLadenie)}.)</p>
		 * 
		 * @return {@code valtrue} alebo {@code valfalse}
		 * 
		 * @see #režimLadenia(boolean)
		 * @see #režimLadenia(boolean, boolean)
		 */
		public static boolean režimLadenia()
		{ return GRobotException.vypíšChybovéHlásenia || Skript.ladenie; }

		/** <p><a class="alias"></a> Alias pre {@link #režimLadenia() režimLadenia}.</p> */
		public static boolean rezimLadenia() { return režimLadenia(); }

		/**
		 * <p>Zistí, či je zapnutý režim ladenia programovacieho rámca
		 * GRobot bez ohľadu na režim výpisu chybových hlásení rámca.
		 * (Pozri: {@link #režimLadenia(boolean, boolean) režimLadenia}.)</p>
		 * 
		 * @return {@code valtrue} alebo {@code valfalse}
		 * 
		 * @see #režimLadenia()
		 * @see #režimLadenia(boolean)
		 * @see #režimLadenia(boolean, boolean)
		 * @see #výpisChybovýchHlásení()
		 */
		public static boolean lenRežimLadenia() { return Skript.ladenie; }

		/** <p><a class="alias"></a> Alias pre {@link #lenRežimLadenia() lenRežimLadenia}.</p> */
		public static boolean lenRezimLadenia() { return Skript.ladenie; }

		/**
		 * <p>Zistí, či je zapnutý výpis chybových hlásení programovacieho
		 * rámca. (Pozri: {@link #režimLadenia(boolean, boolean)
		 * režimLadenia}.)</p>
		 * 
		 * @return {@code valtrue} alebo {@code valfalse}
		 * 
		 * @see #režimLadenia()
		 * @see #lenRežimLadenia()
		 * @see #režimLadenia(boolean)
		 * @see #režimLadenia(boolean, boolean)
		 */
		public static boolean výpisChybovýchHlásení()
		{ return GRobotException.vypíšChybovéHlásenia; }

		/** <p><a class="alias"></a> Alias pre {@link #výpisChybovýchHlásení() výpisChybovýchHlásení}.</p> */
		public static boolean vypisChybovychHlaseni() { return výpisChybovýchHlásení(); }

		/**
		 * <p>Zistí, či premená {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu} so zadaným názvom a typom jestvuje (je
		 * definovaná).</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double Double.class},
		 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
		 *     {@link String String.class}
		 * @return ak premenná zadaného údajového typu jestvuje, tak je
		 *     návratovou hodnotou tejto metódy hodnota {@code valtrue};
		 *     ak premenná nejestvuje alebo bol zadaný nepovolený údajový
		 *     typ premennej, tak je návratovou hodnotou tejto metódy
		 *     hodnota {@code valfalse}
		 * 
		 * @see PremennéSkriptu#jestvuje(String, Class)
		 */
		public static boolean premennáJestvuje(String názov, Class<?> typ)
		{
			// názov = názov.toLowerCase();
			return Skript.premennáJestvuje(názov, typ);
		}

		/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
		public static boolean premennaJestvuje(String názov, Class<?> typ)
		{ return Skript.premennáJestvuje(názov, typ); }

		/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
		public static boolean premennáExistuje(String názov, Class<?> typ)
		{ return Skript.premennáJestvuje(názov, typ); }

		/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
		public static boolean premennaExistuje(String názov, Class<?> typ)
		{ return Skript.premennáJestvuje(názov, typ); }

		/**
		 * <p>Zistí hodnotu premennej zadaného údajového typu
		 * {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu}. (Ak hodnota jestvuje.)</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double Double.class},
		 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
		 *     {@link String String.class}
		 * @return ak premenná jestvuje, tak je návratovou hodnotou tejto
		 *     metódy hodnota tejto premennej; ak premenná nejestvuje,
		 *     prípadne bol zadaný nepovolený údajový typ premennej, tak je
		 *     návratovou hodnotou tejto metódy hodnota {@code valnull}
		 * 
		 * @see PremennéSkriptu#čítaj(String, Class)
		 */
		public static Object čítajPremennú(String názov, Class<?> typ)
		{
			// názov = názov.toLowerCase();
			return Skript.čítajPremennú(názov, typ);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajPremennú(String, Class) čítajPremennú}.</p> */
		public static Object citajPremennu(String názov, Class<?> typ)
		{ return Skript.čítajPremennú(názov, typ); }

		/**
		 * <p>Nastaví novú hodnotu premennej
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}.
		 * Ak je zadaná hodnota nepovoleného údajového typu, tak nebude
		 * nastavená hodnota žiadnej premennej. To isté platí pri pokuse
		 * o zapísanie hodnoty {@code valnull}. Úspešnosť (resp. neúspešnosť)
		 * nastavenia hodnoty premennej potvrdzuje návratová hodnota tejto
		 * metódy – {@code valtrue} (úspech) / {@code valfalse}
		 * (neúspech).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * automaticky definuje nejestvujúce premenné.</p>
		 * 
		 * @param názov názov premennej
		 * @param hodnota hodnota premennej povoleného údajového typu –
		 *     povolené sú: {@link Double Double},
		 *     {@link Color Color}, {@link Poloha Poloha} alebo
		 *     {@link String String}
		 * @return kontrolná návratová hodnota (ide najmä o overenie toho,
		 *     či zadaná hodnota naozaj bola niektorého povoleného typu);
		 *     ak je návratová hodnota {@code valtrue}, tak bola zadaná
		 *     hodnota zapísaná do premennej prislúchajúceho údajového typu
		 * 
		 * @see PremennéSkriptu#zapíš(String, Object)
		 */
		public static boolean zapíšPremennú(String názov, Object hodnota)
		{
			// názov = názov.toLowerCase();
			return Skript.zapíšPremennú(názov, hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšPremennú(String, Object) zapíšPremennú}.</p> */
		public static boolean zapisPremennu(String názov, Object hodnota)
		{ return Skript.zapíšPremennú(názov, hodnota); }

		/**
		 * <p>Vymaže definíciu premennej zadaného údajového typu
		 * {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu}. (Ak jestvuje.)</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double Double.class},
		 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
		 *     {@link String String.class}
		 * 
		 * @see PremennéSkriptu#vymaž(String, Class)
		 */
		public static void vymažPremennú(String názov, Class<?> typ)
		{
			// názov = názov.toLowerCase();
			Skript.vymažPremennú(názov, typ);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPremennú(String, Class) vymažPremennú}.</p> */
		public static void vymazPremennu(String názov, Class<?> typ)
		{ Skript.vymažPremennú(názov, typ); }


		/**
		 * <p>Vykoná skript uložený v poli reťazcov. Táto metóda vyžaduje,
		 * aby v čase vykonania prvého príkazu bol aspoň jeden robot, svet
		 * alebo niektoré z plátien v {@linkplain 
		 * Svet#interaktívnyRežim(boolean) interaktívnom režime}, inak sa
		 * vykonávanie skriptu skončí chybou.</p>
		 * 
		 * <p>Každý neprázdny riadok skriptu smie obsahovať niektorú
		 * z položiek v nasledujúcom zozname:</p>
		 * 
		 * <ul>
		 * <li>platný príkaz – taký, ktorý je zároveň použiteľný s metódou
		 * na vykonávanie príkazov robota (pozri: {@link 
		 * GRobot#vykonajPríkaz(String) vykonajPríkaz}), plátna ({@link 
		 * Plátno#vykonajPríkaz(String) vykonajPríkaz}) alebo sveta (pozri:
		 * {@link #vykonajPríkaz(String) vykonajPríkaz}),</li>
		 * <li>aktivovanie alebo deaktivovanie {@linkplain 
		 * #interaktívnaInštancia(String) interaktívnej inštancie} (pozri
		 * nižšie),</li>
		 * <li>komentár (pozri nižšie),</li>
		 * <li>definíciu menovky (pozri nižšie),</li>
		 * <li>alebo jeden z rezervovaných príkazov skriptu (podrobnosti
		 * sú opäť nižšie): nepodmienený skok, podmienený skok, podmienený
		 * skok s dekrementáciou premennej („cyklus“), podmienený skok
		 * s alternatívou, podmienený skok s dekrementáciou premennej
		 * a alternatívou („cyklus s alternatívou“).</li>
		 * </ul>
		 * 
		 * <p> </p>
		 * 
		 * <table class="commands">
		 * <tr><td><code>; </code><em>«text»</em></td><td>–</td><td
		 * >komentár – tento riadok bude ignorovaný</td></tr>
		 * <tr><td><code>:</code><em>«názov»</em></td><td>–</td><td>definícia
		 * menovky, ktorá je používaná na skoky (podmienené a nepodmienené –
		 * pozri nižšie)</td></tr>
		 * <tr><td><code>@</code><em
		 * >«názov inštancie»</em></td><td
		 * >–</td><td>aktivovanie {@linkplain #interaktívnaInštancia(String)
		 * interaktívnej inštancie}</td></tr>
		 * <tr><td><code>@</code></td><td>–</td><td>zrušenie aktivácie
		 * {@linkplain #interaktívnaInštancia(String) interaktívnej
		 * inštancie}</td></tr>
		 * <tr><td><code>na </code> <em>«menovka»</em></td><td>–</td>
		 * <td>nepodmienený skok – vykonávanie skriptu prejde (preskočí) na
		 * riadok označený menovkou (pozri vyššie)</td></tr>
		 * <tr><td><code>ak </code><em
		 * >«premenná alebo hodnota»</em> <em
		 * >«menovka»</em></td><td>–</td>
		 * <td>podmienený skok – ak je <em>«premenná alebo hodnota»</em>
		 * nenulová, tak vykonávanie skriptu prejde (preskočí) na riadok
		 * označený menovkou</td></tr>
		 * <tr><td><code>ak </code><em
		 * >«premenná alebo hodnota»</em> <em
		 * >«menovka1»</em><code> inak </code><em
		 * >«menovka2»</em></td><td>–</td><td>podmienený
		 * skok s alternatívou – ak je <em>«premenná alebo hodnota»</em>
		 * nenulová, tak vykonávanie skriptu prejde (preskočí) na riadok
		 * označený menovkou <em>«menovka1»</em>, inak na riadok označený
		 * menovkou <em>«menovka2»</em></td></tr>
		 * <tr><td><code>dokedy </code><em>«premenná»</em> <em
		 * >«menovka»</em></td><td>–</td><td>podmienený
		 * skok s dekrementáciou premennej („cyklus“) – najprv sa zníži
		 * hodnota premennej o 1 a ak je výsledok výpočtu záporný, tak sa
		 * jej hodnota nastaví na nulu; ak je konečná hodnota premennej
		 * kladná, tak vykonávanie skriptu prejde (preskočí) na riadok
		 * označený menovkou</td></tr>
		 * <tr><td><code>dokedy </code><em>«premenná»</em> <em
		 * >«menovka1»</em><code> inak </code><em>«menovka2»</em></td><td
		 * >–</td><td>podmienený skok s dekrementáciou premennej („cyklus“)
		 * s alternatívou – najprv sa zníži hodnota premennej o 1 a ak je
		 * výsledok výpočtu záporný, tak sa jej hodnota nastaví na nulu;
		 * ak je konečná hodnota premennej kladná, tak vykonávanie skriptu
		 * prejde (preskočí) na riadok označený menovkou <em>«menovka1»</em>,
		 * inak na riadok označený menovkou <em>«menovka2»</em></td></tr>
		 * <tr><td colspan="2"> </td>
		 * <td>podmienené skoky s dekrementáciou premennej sú navrhnuté tak,
		 * aby pomyselne predpokladali prítomnosť kladnej hodnoty v riadiacej
		 * premennej a aby sa opakovanie ukončilo v okamihu dosiahnutia nuly
		 * v riadiacej premennej (pričom nie je dovolené, aby sa v riadiacej
		 * premennej vyskytla záporná hodnota – záporné hodnoty sú prepísané
		 * nulou); to znamená, že všetky priebehy vykonania s hodnotou
		 * riadiacej premennej menšej alebo rovnej jednej sú identické<!--
		 * skrytá poznámka: dôvodom tohto návrhu bolo kladenie dôrazu na čo
		 * najvyššiu jednoduchosť pri implementácii a bolo to inšpirované
		 * jazykom symbolických inštrukcií --></td></tr>
		 * </table>
		 * 
		 * @param riadky pole reťazcov reprezentujúcich riadky skriptu
		 * @return riadok, na ktorom vznikla chyba (ak chyba nevznikla,
		 *     tak je vrátená nula)
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static int vykonajSkript(String[] riadky)
		{
			Skript skript = vyrobSkript(riadky);
			return skript.vykonaj();

			/* DEPRECATED:
				počítadloRiadkovLadenia = 0;
				poslednáChyba = ŽIADNA_CHYBA;
				if (Skript.ladenie) vypíšPremenné();

				final TreeMap<String, Integer> menovky =
					new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

				int i = 0, čr = 1;
				for (String riadok : riadky)
				{
					// Orezať všetky medzery zľava
					riadky[i] = riadok = ltrim.matcher(riadok).replaceAll("$1")
						// Pozor! Nesmiem tu previesť na malé písmená!
						// 	.toLowerCase()
						// Zničil by sa prípadný reťazec na vypísanie!
						;

					if (!riadok.isEmpty() && ':' == riadok.charAt(0))
					{
						riadok = dajMenovku(riadok);

						if (!riadok.isEmpty())
						{
							if (Skript.ladenie && (null == ObsluhaUdalostí.počúvadlo ||
								ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok,
								VYPÍSAŤ_MENOVKY))) vypíšRiadokLadenia(
									tmavooranžová, ":", String.format(
										Locale.ENGLISH, "%3d", čr), riadok);
							if (menovky.containsKey(riadok))
							{
								poslednáChyba = CHYBA_DVOJITÁ_MENOVKA;
								return čr; // chyba – dvojitá menovka
							}
							menovky.put(riadok, i);
						}
						// riadky[i] = "";
					}
					++i; ++čr;
				}

				if (počítadloRiadkovLadenia > 0)
					/*Skript.ladenie && (!menovky.isEmpty() ||
					!globálnePremenné.premenné.isEmpty() ||
					!globálnePremenné.premennéFarby.isEmpty() ||
					!globálnePremenné.premennéPolohy.isEmpty() ||
					!globálnePremenné.premennéReťazce.isEmpty())* /
					vypíšRiadok();

				String aktuálnaInštancia = interaktívnaInštancia;

				for (i = 0, čr = 1; i < riadky.length; ++i, ++čr)
				{
					String riadok = riadky[i];

					if (riadok.isEmpty())
					{
						if (Skript.ladenie && (null == ObsluhaUdalostí.počúvadlo ||
							ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK)))
							vypíšRiadokLadenia(šedá, " ", String.format(
								Locale.ENGLISH, "%3d", čr));

						if (prerušiťLadenie(čr, riadok)) break;

						continue;
					}
					else if (';' == riadok.charAt(0))
					{
						if (Skript.ladenie && (null == ObsluhaUdalostí.počúvadlo ||
							ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK)))
							vypíšRiadokLadenia(svetlošedá, ";", String.
								format(Locale.ENGLISH, "%3d", čr),
							riadok.substring(1));

						if (prerušiťLadenie(čr, riadok)) break;

						continue;
					}
					else if (':' == riadok.charAt(0))
					{
						if (Skript.ladenie && (null == ObsluhaUdalostí.počúvadlo ||
							ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK)))
						{
							riadok = dajMenovku(riadok);

							if (riadok.isEmpty())
								vypíšRiadokLadenia(šedá, " ", String.format(
									Locale.ENGLISH, "%3d", čr));
							else
								vypíšRiadokLadenia(tmavošedá, ":",
									String.format(Locale.ENGLISH, "%3d", čr),
									riadok);
						}

						if (prerušiťLadenie(čr, riadok)) break;

						continue;
					}
					else if ('@' == riadok.charAt(0))
					{
						if (Skript.ladenie && (null == ObsluhaUdalostí.počúvadlo ||
							ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK)))
						{
							vypíšRiadokLadenia(tmavohnedá, "@",
								String.format(Locale.ENGLISH, "%3d", čr),
								riadok.substring(1));
						}

						if (prerušiťLadenie(čr, riadok)) break;

						if (riadok.length() == 1)
							aktuálnaInštancia = null;
						else
							aktuálnaInštancia = riadok.substring(1).toLowerCase();
						continue;
					}
					else if (riadok.regionMatches(true, 0, "na ", 0, 3))
					{
						String[] slová = vykonajPríkazTokenizer.split(riadok);

						if (slová.length < 2)
						{
							poslednáChyba = CHYBA_CHÝBAJÚCA_MENOVKA;
							return čr;
						}

						Integer skoč1 = menovky.get(
							slová[1]// .toLowerCase()
							);
						if (null == skoč1)
						{
							poslednáChyba = CHYBA_NEZNÁMA_MENOVKA;
							return čr;
						}

						boolean vypísať = false;

						if (Skript.ladenie)
						{
							vypísať = null == ObsluhaUdalostí.počúvadlo ||
								ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK);
							if (vypísať)
								vypíšRiadokLadenia(modrá, " ",
									String.format(Locale.ENGLISH, "%3d", čr),
									slová[0], slová[1]);

							if (prerušiťLadenie(čr, riadok)) break;
						}

						čr = (i = skoč1) + 1;

						if (Skript.ladenie && vypísať)
						{
							riadok = dajMenovku(riadky[i]);
							vypíšRiadokLadenia(tmavošedá, ":",
								String.format(Locale.ENGLISH,
								"%3d", čr), riadok);
						}

						continue;
					}
					else if (riadok.regionMatches(true, 0, "ak ", 0, 3) ||
						riadok.regionMatches(true, 0, "dokedy ", 0, 7))
					{
						String[] slová = vykonajPríkazTokenizer.split(riadok);
						// for (String s : slová) System.out.println("Slovo: " + s);

						if (slová.length < 3)
						{
							poslednáChyba = CHYBA_CHÝBAJÚCA_MENOVKA;
							return čr;
						}

						Integer skoč1 = menovky.get(
							slová[2]// .toLowerCase()
							);
						if (null == skoč1)
						{
							poslednáChyba = CHYBA_NEZNÁMA_MENOVKA;
							return čr;
						}

						Integer skoč2;
						if (slová.length >= 5)
						{
							if (!slová[3].equalsIgnoreCase("inak"))
							{
								poslednáChyba = CHYBA_NEZNÁME_SLOVO;
								return čr;
							}
							skoč2 = menovky.get(
								slová[4]// .toLowerCase()
								);
							if (null == skoč2)
							{
								poslednáChyba = CHYBA_NEZNÁMA_MENOVKA;
								return čr;
							}
						}
						else if (slová.length >= 4)
						{
							skoč2 = menovky.get(
								slová[3]// .toLowerCase()
								);
							if (null == skoč2)
							{
								poslednáChyba = CHYBA_NEZNÁMA_MENOVKA;
								return čr;
							}
						}
						else skoč2 = null;

						Boolean podmienka;

						if (slová[0].equalsIgnoreCase("ak"))
						{
							podmienka = vyhodnoťLogickýVýraz(slová[1]);
							boolean vypísať = false;

							if (Skript.ladenie)
							{
								vypísať = null == ObsluhaUdalostí.počúvadlo ||
									ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK);
								if (vypísať)
									vypíšÚdajLadenia(purpurová, "?",
										String.format(Locale.ENGLISH, "%3d", čr),
										slová[0], slová[1], ": ");

								if (prerušiťLadenie(čr, riadok)) break;
							}

							if (podmienka) čr = (i = skoč1) + 1;
							else if (null != skoč2) čr = (i = skoč2) + 1;

							if (Skript.ladenie && vypísať)
							{
								if (podmienka)
									vypíšRiadokLadenia(tmavozelená,
										"pravda, prejsť na", slová[2]);
								else if (null != skoč2)
									vypíšRiadokLadenia(tmavooranžová,
										"lož, prejsť na", (slová.length >= 5 ?
											slová[4] : slová[3]));
								else
									vypíšRiadokLadenia(tmavočervená,
										"lož, neprejsť nikam");

								if (podmienka || null != skoč2)
								{
									riadok = dajMenovku(riadky[i]);
									vypíšRiadokLadenia(tmavošedá, ":",
										String.format(Locale.ENGLISH,
										"%3d", čr), riadok);
								}
							}
							continue;
						}
						else if (slová[0].equalsIgnoreCase("dokedy"))
						{
							// slová[1] = slová[1].toLowerCase();
							if (premennáJestvuje(slová[1], Double.class))
							{
								// Ak je výsledok výpočtu záporný, nastaví sa
								// hodnota premennej na nulu, aby mohla byť
								// podmienka v nasledujúcom kroku
								// (vyhodnoťLogickýVýraz) vyhodnotená ako
								// nepravdivá.

								// ✗ Test…
								zapíšPremennú(slová[1], new Double(Math.max(
									Math.rint((Double)čítajPremennú(slová[1],
										Double.class) - 1.0), 0.0)));
								vypíšPremennú(čr, slová[1], ČÍSELNÁ_PREMENNÁ);
								podmienka = vyhodnoťLogickýVýraz(slová[1]);
								boolean vypísať = false;

								if (Skript.ladenie)
								{
									vypísať = null == ObsluhaUdalostí.počúvadlo ||
										ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok,
											VYPÍSAŤ_RIADOK);
									if (vypísať)
										vypíšÚdajLadenia(tmavopurpurová, "@",
											String.format(Locale.ENGLISH, "%3d",
												čr), slová[0], slová[1], ": ");

									if (prerušiťLadenie(čr, riadok)) break;
								}

								if (podmienka) čr = (i = skoč1) + 1;
								else if (null != skoč2) čr = (i = skoč2) + 1;

								if (Skript.ladenie && vypísať)
								{
									if (podmienka)
										vypíšRiadokLadenia(tmavozelená,
											"pravda, prejsť na", slová[2]);
									else if (null != skoč2)
										vypíšRiadokLadenia(tmavooranžová,
											"lož, prejsť na", (slová.length >= 5 ?
												slová[4] : slová[3]));
									else
										vypíšRiadokLadenia(tmavočervená,
											"lož, neprejsť nikam");

									if (podmienka || null != skoč2)
									{
										riadok = dajMenovku(riadky[i]);
										vypíšRiadokLadenia(tmavošedá, ":",
											String.format(Locale.ENGLISH,
											"%3d", čr), riadok);
									}
								}
								continue;
							}
							// else
							// {
							// 	✗ Zvážiť: CHYBA_NEZNÁMA_PREMENNÁ ✗
							// }
						}

						poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA;
						return čr;
					}

					if (Skript.ladenie)
					{
						if (null == ObsluhaUdalostí.počúvadlo ||
							ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, VYPÍSAŤ_RIADOK))
							vypíšRiadokLadenia(tmavomodrá, " ", String.format(
								Locale.ENGLISH, "%3d", čr), riadok);

						if (prerušiťLadenie(čr, riadok)) break;
					}

					int početÚspechov = vykonajPríkaz(riadok, aktuálnaInštancia);
					if (0 == početÚspechov)
					{
						if (ŽIADNA_CHYBA == poslednáChyba)
							poslednáChyba = CHYBA_NEZNÁMY_PRÍKAZ;
						return čr;
					}
				}

				return 0;
			*/
		}

		/**
		 * <p>Vykoná skript zadaný vo forme reťazca. Riadky musia byť
		 * oddelené znakom nového reťazca <code>\n</code>.
		 * Pravidlá vykonávania sú rovnaké ako pri metóde
		 * {@link #vykonajSkript(String[]) vykonajSkript(riadky)}.</p>
		 * 
		 * @param skript skript uložený v jednom reťazci
		 * @return riadok, na ktorom vznikla chyba (ak chyba nevznikla,
		 *     tak je vrátená nula)
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static int vykonajSkript(String skript)
		{
			String riadky[] = Skript.vykonajSkriptRiadkovač.split(skript);
			return vykonajSkript(riadky);
		}

		/**
		 * <p>Vykoná skript zadaný vo forme parametrického zoznamu. Metóda
		 * vytvorí zo zoznamu kópiu do poľa reťazcov, aby nenastal konflikt
		 * pri prípadnej paralelnej úprave zoznamu, čiže ak sa zoznam počas
		 * vykonávania skriptu zmení, vykonávanie tým nebude ovplyvnené,
		 * pretože sa v skutočnosti vykonáva kópia skriptu vytvorená v čase
		 * volania tejto metódy. Z toho vyplýva, že pravidlá vykonávania sú
		 * rovnaké ako pri metóde {@link #vykonajSkript(String[])
		 * vykonajSkript(riadky)}.</p>
		 * 
		 * @param skript skript uložený v parametrickom zozname
		 * @return riadok, na ktorom vznikla chyba (ak chyba nevznikla,
		 *     tak je vrátená nula)
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static int vykonajSkript(List<String> skript)
		{
			String riadky[] = new String[skript.size()];
			int i = 0; for (String príkaz : skript)
			{
				riadky[i] = príkaz;
				++i;
			}

			return vykonajSkript(riadky);
		}

		/**
		 * <p>Vykoná skript zadaný vo forme reťazca alebo vo forme názvu
		 * súboru, z ktorého má byť prečítaný. Ak je druhý parameter rovný
		 * {@code valfalse}, tak je prvý parameter považovaný za skript
		 * a metóda sa správa rovnako ako metóda {@link 
		 * #vykonajSkript(String) vykonajSkript(skript)}. Ak je druhý
		 * parameter rovný {@code valtrue}, tak prvý parameter je
		 * považovaný za názov súboru, z ktorého má byť skript prečítaný
		 * a vykonaný. Pravidlá vykonávania sú v oboch prípadoch rovnaké
		 * ako pri metóde {@link #vykonajSkript(String[])
		 * vykonajSkript(riadky)}.</p>
		 * 
		 * <p><b>Príklad skriptu uloženého v súbore:</b></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Skript musí byť uložený
		 * s kódovaním UTF-8.</p>
		 * 
		 * <p>skoč -72.5, -30<br />
		 * hrúbka čiary 3.2<br />
		 * farba žltá<br />
		 * začni cestu<br />
		 * nech i = 8<br />
		 *  <br />
		 * :opakuj<br />
		 *   dopredu 60<br />
		 *   vpravo 45<br />
		 * dokedy i, opakuj<br />
		 *  <br />
		 * vyplň cestu<br />
		 * farba červená<br />
		 * obkresli cestu<br />
		 *  <br />
		 * farba čierna<br />
		 * domov<br />
		 * farba textu purpurová<br />
		 * vypíš riadok "Hotovo!</p>
		 * 
		 * <p><b>Program:</b></p>
		 * 
		 * <p>Ak predchádzajúci skript uložíme do súboru s názvom
		 * „Skript.GRScript“, tak na jeho spustenie môžeme použiť
		 * nasledujúci úryvok kódu:</p>
		 * 
		 * <pre CLASS="example">
			{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
			{@link Svet Svet}.{@link Svet#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});

			{@code typeint} kód = {@link Svet Svet}.{@link Svet#vykonajSkript(String, boolean) vykonajSkript}({@code srg"Skript.GRScript"}, {@code valtrue});

			{@code kwdif} ({@code num0} &gt; kód)
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Chyba pri čítaní súboru ("}, kód, {@code srg")."});
			{@code kwdelse}
				{@code kwdif} ({@code num0} &lt; kód) {@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Chyba na riadku: "}, kód);
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>skriptZoSuboru.png<alt/>Výsledok vykonania
		 * skriptu.</image>Výsledok vykonania skriptu
		 * vyššie uvedeným programom.</p>
		 * 
		 * @param skript názov súboru so skriptom alebo skript uložený
		 *     v jednom reťazci
		 * @param zoSúboru ak je {@code valtrue}, tak je prvý parameter
		 *     považovaný za názov súboru so skriptom; ak je {@code valfalse},
		 *     tak je prvý parameter považovaný za skript (rovnako ako pri
		 *     metóde {@link #vykonajSkript(String) vykonajSkript(skript)})
		 * @return riadok, na ktorom vznikla chyba (ak chyba nevznikla,
		 *     tak je vrátená nula); ak je návratová hodnota záporná, znamená
		 *     to, že vznikla chyba pri čítaní súboru
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak skript so zadaným menom nebol nájdený
		 *     alebo ak bolo vykonávanie niektorého príkazu skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static int vykonajSkript(String skript, boolean zoSúboru)
		{
			if (!zoSúboru) return vykonajSkript(skript);

			try
			{
				BufferedReader čítanie;
				Vector<String> riadky = new Vector<>();

				try
				{
					// Vyhľadá súbor na pevnom disku
					čítanie = new BufferedReader(new InputStreamReader(
						Súbor.dajVstupnýPrúdSúboru(skript), "UTF-8"));
				}
				catch (FileNotFoundException notFound)
				{
					try
					{
						// Pokúsi sa prečítať skript zo zdroja .jar súboru
						čítanie = new BufferedReader(new InputStreamReader(
							Súbor.dajVstupnýPrúdZdroja(skript), "UTF-8"));
					}
					catch (NullPointerException isNull)
					{
						throw new GRobotException(
							"Skript „" + skript + "“ nebol nájdený.",
							"scriptNotFound", skript, notFound);
					}
				}

				String riadok = čítanie.readLine();
				if (null != riadok)
				{
					if (!riadok.isEmpty() &&
						riadok.charAt(0) == '\uFEFF')
						riadok = riadok.substring(1);

					do
					{
						riadky.add(riadok);
						riadok = čítanie.readLine();
					}
					while (null != riadok);
				}

				čítanie.close();
				if (riadky.isEmpty()) return 0;

				return vykonajSkript(riadky.
					toArray(new String[riadky.size()]));
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				return -1;
			}
		}


		/**
		 * <p>Nahrá skript zo súboru a uloží ho do vnútornej pamäte pod
		 * zadaným názvom. Ak počas nahrávania skriptu vznikne chyba, tak
		 * metóda vráti hodnotu {@code valnull}, v opačnom prípade vráti
		 * inštanciu triedy {@link Skript Skript} na prípadné ďalšie
		 * spracovanie.</p>
		 * 
		 * @param názov názov skriptu, pod ktorým bude tento skript (v prípade
		 *     úspešného nahratia) evidovaný vo vnútornom zozname skriptov
		 * @param súbor súbor, z ktorého bude skript nahraný
		 * @return inštancia triedy {@link Skript Skript} na ďalšie
		 *     spracovanie, prípadne {@code valnull}
		 * 
		 * @see #nahrajSkript(String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript nahrajSkript(String názov, String súbor)
		{
			Skript skript = vyrobSkript(súbor, true);
			if (null != skript)
				Skript.zoznamSkriptov.put(názov, skript);
			return skript;
		}

		/**
		 * <p>Toto je klon metódy {@link #nahrajSkript(String, String)
		 * nahrajSkript(názov, súbor)}, ktorý bol definovaný preto, aby
		 * bolo možné nahrávať zo súborov skripty aj v rámci iných
		 * skriptov. Metóda bude považovať za názov skriptu meno súboru,
		 * to jest časť medzi posledným oddeľovačom cesty (buď {@code \},
		 * alebo {@code /}) a posledným oddeľovačom prípony
		 * ({@code .}).</p>
		 * 
		 * @param súbor súbor, z ktorého bude skript nahraný; názov skriptu
		 *     je tiež odvodený z tohto reťazca
		 * @return inštancia úspešne nahratého skriptu alebo hodnota
		 *     {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 */
		public static Skript nahrajSkript(String súbor)
		{
			int indexOf1 = 1 + Math.max(
				súbor.lastIndexOf('\\'), súbor.lastIndexOf('/'));
			int indexOf2 = súbor.lastIndexOf('.');
			if (indexOf1 >= indexOf2) indexOf2 = súbor.length();
			String názov = súbor.substring(indexOf1, indexOf2);
			if (názov.isEmpty()) názov = súbor;
			// if (!súbor.endsWith(".GRScript")) súbor += ".GRScript"; // NO‼
			return nahrajSkript(názov, súbor);
		}


		/**
		 * <p>Vyrobí skript (zo zadaného textového reťazca alebo zo súboru)
		 * a vráti ho v inštancii triedy {@link Skript Skript} na ďalšie
		 * spracovanie. Ak je parameter {@code zoSúboru} rovný
		 * {@code valfalse}, tak je v parametri {@code skript} očakávaný
		 * text skriptu. Ak je parameter {@code zoSúboru} rovný
		 * {@code valtrue}, tak je v parametri {@code skript} očakávaný
		 * názov súboru, z ktorého má byť skript prečítaný. Ak vznikne počas
		 * čítania alebo spracovania (výroby) skriptu chyba, tak metóda
		 * vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param skript názov súboru so skriptom alebo skript uložený
		 *     v jednom reťazci
		 * @param zoSúboru ak je {@code valtrue}, tak je prvý parameter
		 *     považovaný za názov súboru so skriptom; ak je {@code valfalse},
		 *     tak je prvý parameter považovaný za skript (rovnako ako pri
		 *     metóde {@link #vyrobSkript(String) vyrobSkript(skript)})
		 * @return inštancia triedy {@link Skript Skript} alebo {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript vyrobSkript(String skript, boolean zoSúboru)
		{
			if (zoSúboru)
			{
				try
				{
					BufferedReader čítanie;
					Vector<String> riadky = new Vector<>();

					try
					{
						// Vyhľadá súbor na pevnom disku
						čítanie = new BufferedReader(new InputStreamReader(
							Súbor.dajVstupnýPrúdSúboru(skript), "UTF-8"));
					}
					catch (FileNotFoundException notFound)
					{
						try
						{
							// Pokúsi sa prečítať skript zo zdroja .jar súboru
							čítanie = new BufferedReader(new InputStreamReader(
								Súbor.dajVstupnýPrúdZdroja(skript), "UTF-8"));
						}
						catch (NullPointerException isNull)
						{
							throw new GRobotException(
								"Skript „" + skript + "“ nebol nájdený.",
								"scriptNotFound", skript, notFound);
						}
					}

					String riadok = čítanie.readLine();
					if (null != riadok)
					{
						if (!riadok.isEmpty() &&
							riadok.charAt(0) == '\uFEFF')
							riadok = riadok.substring(1);

						do
						{
							riadky.add(riadok);
							riadok = čítanie.readLine();
						}
						while (null != riadok);
					}

					čítanie.close();
					if (riadky.isEmpty()) return null;

					return Skript.vyrob(riadky.toArray(
						new String[riadky.size()]));
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}
			else return vyrobSkript(skript);

			return null;
		}

		/**
		 * <p>Vyrobí zo zadaného textového reťazca skript a vráti ho
		 * v inštancii triedy {@link Skript Skript} na ďalšie spracovanie.
		 * Ak počas výroby skriptu vznikne chyba, tak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * @param skript skript v textovom tvare (reťazec obsahujúci celý
		 *     skript)
		 * @return inštancia triedy {@link Skript Skript} alebo {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript vyrobSkript(String skript)
		{
			String riadky[] = Skript.vykonajSkriptRiadkovač.split(skript);
			return vyrobSkript(riadky);
		}

		/**
		 * <p>Vyrobí zo zadaného zoznamu reťazcov reprezentujúcich riadky
		 * skriptu nový skript a vráti ho v inštancii triedy {@link Skript
		 * Skript} na ďalšie spracovanie. Ak vznikne počas výroby skriptu
		 * chyba, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param skript zoznam s riadkami skriptu
		 * @return inštancia triedy {@link Skript Skript} alebo {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript vyrobSkript(List<String> skript)
		{
			return vyrobSkript(skript.toArray(new String[skript.size()]));
		}

		/**
		 * <p>Vyrobí zo zadaného reťazcového poľa, ktorého prvky reprezentujú
		 * riadky skriptu, nový skript a vráti ho na ďalšie spracovanie
		 * v inštancii triedy {@link Skript Skript}. Ak vznikne počas výroby
		 * skriptu chyba, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param skript pole reťazcov reprezentujúcich riadky skriptu
		 * @return inštancia triedy {@link Skript Skript} alebo {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript vyrobSkript(String[] skript)
		{
			return Skript.vyrob(skript);
		}

		/**
		 * <p>Vyrobí zo zadaného reťazca nový skript, registruje ho pod
		 * zadaným názvom a vráti ho na prípadné ďalšie spracovanie.
		 * Metóda funguje podobne ako metóda {@link #nahrajSkript(String,
		 * String) nahrajSkript} s tým rozdielom, že skript nie je čítaný
		 * zo súboru, ale je prijatý vo forme reťazca.</p>
		 * 
		 * @param názov názov skriptu, pod ktorým bude tento skript (v prípade
		 *     úspešného vyrobenia) evidovaný vo vnútornom zozname skriptov
		 * @param skript skript v textovom tvare (reťazec obsahujúci celý
		 *     skript)
		 * @return inštancia triedy {@link Skript Skript} na ďalšie
		 *     spracovanie alebo {@code valnull} v prípade chyby
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript registrujSkript(String názov, String skript)
		{
			String riadky[] = Skript.vykonajSkriptRiadkovač.split(skript);
			return registrujSkript(názov, riadky);
		}

		/**
		 * <p>Vyrobí zo zadaného zoznamu reťazcov nový skript, registruje
		 * ho pod zadaným názvom a vráti ho na prípadné ďalšie spracovanie.
		 * Metóda funguje podobne ako metóda {@link #vyrobSkript(List)
		 * vyrobSkript} (ibaže skript navyše aj registruje vo vnútornej
		 * pamäti).</p>
		 * 
		 * @param názov názov skriptu, pod ktorým bude tento skript (v prípade
		 *     úspešného vyrobenia) evidovaný vo vnútornom zozname skriptov
		 * @param skript zoznam s riadkami skriptu
		 * @return inštancia triedy {@link Skript Skript} na ďalšie
		 *     spracovanie alebo {@code valnull} v prípade chyby
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript registrujSkript(String názov, List<String> skript)
		{
			return registrujSkript(názov, skript.toArray(
				new String[skript.size()]));
		}

		/**
		 * <p>Vyrobí zo zadaného reťazcového poľa nový skript, registruje
		 * ho pod zadaným názvom a vráti ho na prípadné ďalšie spracovanie.
		 * Metóda funguje podobne ako metóda {@link #vyrobSkript(String[])
		 * vyrobSkript} (ibaže skript navyše aj registruje vo vnútornej
		 * pamäti).</p>
		 * 
		 * @param názov názov skriptu, pod ktorým bude tento skript (v prípade
		 *     úspešného vyrobenia) evidovaný vo vnútornom zozname skriptov
		 * @param skript pole reťazcov reprezentujúcich riadky skriptu
		 * @return inštancia triedy {@link Skript Skript} na ďalšie
		 *     spracovanie alebo {@code valnull} v prípade chyby
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #dajSkript(String)
		 * @see #volajSkript(String)
		 */
		public static Skript registrujSkript(String názov, String[] skript)
		{
			Skript vyrobenýSkript = Skript.vyrob(skript);
			if (null != vyrobenýSkript)
				Skript.zoznamSkriptov.put(názov, vyrobenýSkript);
			return vyrobenýSkript;
		}

		/**
		 * <p>Vráti inštanciu skriptu registrovanú vo vnútornej pamäti
		 * programovacieho rámca pod zadaným menom.</p>
		 * 
		 * @param názov názov skriptu vopred registrovaného vo vnútornom
		 *     zozname skriptov (napríklad metódou {@link #nahrajSkript(String,
		 *     String) nahrajSkript})
		 * @return inštancia triedy {@link Skript Skript} alebo {@code valnull}
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #volajSkript(String)
		 */
		public static Skript dajSkript(String názov)
		{
			return Skript.zoznamSkriptov.get(názov);
		}

			// TODO.
			private static int hĺbkaVolania = 0;

		/**
		 * <p>Spustí skript registrovaný vo vnútornej pamäti rámca pod
		 * zadaným menom.</p>
		 * 
		 * @param názov názov skriptu vopred registrovaného vo vnútornom
		 *     zozname skriptov (napríklad metódou {@link #nahrajSkript(String,
		 *     String) nahrajSkript})
		 * @return {@link Konštanty#CHYBA_ČÍTANIA_SKRIPTU
		 *     CHYBA_ČÍTANIA_SKRIPTU} ({@code num-1}) v prípade, že skript
		 *     nebol nájdený, {@link Konštanty#ŽIADNA_CHYBA ŽIADNA_CHYBA}
		 *     v prípade bezchybného vykonania skriptu alebo kladné číslo
		 *     vyjadrujúce číslo riadka, na ktorom vznikla chyba
		 * 
		 * @see #nahrajSkript(String, String)
		 * @see #vyrobSkript(String, boolean)
		 * @see #vyrobSkript(String)
		 * @see #vyrobSkript(List)
		 * @see #vyrobSkript(String[])
		 * @see #registrujSkript(String, String)
		 * @see #registrujSkript(String, List)
		 * @see #registrujSkript(String, String[])
		 * @see #dajSkript(String)
		 */
		public static int volajSkript(String názov)
		{
			if (hĺbkaVolania > 15)
			{
				// TODO: Tento bezpečnostný mechanizmus nie je zďaleka
				// dokončený‼ Na jeho dokončenie by bolo treba zabezpečiť,
				// aby sa v tomto okamihu zastavilo vykonávanie všetkých
				// skriptov.
				hĺbkaVolania = 0;
				return CHYBA_VOLANIA_SKRIPTU;
			}
			else
			{
				Skript skript = Skript.zoznamSkriptov.get(názov);
				if (null == skript) return -1;
				++hĺbkaVolania;
				int kódSkriptu = skript.vykonaj();
				--hĺbkaVolania;
				return kódSkriptu;
			}
		}


		private static boolean skriptJeSpustený = false;

		/**
		 * <p>Zistí, či je práve vykonávaný skript, ktorý bol spustený príkazom
		 * {@link #spustiSkript(String[]) spustiSkript}
		 * (alebo niektorou jeho modifikáciou).</p>
		 * 
		 * @return {@code valtrue} ak je skript spustený
		 * 
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 */
		public static boolean skriptJeSpustený() { return skriptJeSpustený; }

		/** <p><a class="alias"></a> Alias pre {@link #skriptJeSpustený() skriptJeSpustený}.</p> */
		public static boolean skriptJeSpusteny() { return skriptJeSpustený; }


		/**
		 * <p>Funguje podobne ako {@link #vykonajSkript(String[])
		 * vykonajSkript(riadky)}, ale spustí vykonávanie v samostatnom vlákne
		 * Javy, takže môže byť ladené. Keďže pri tomto spôsobe nie je možné
		 * získať návratovú hodnotu priamo, tak je v prípade vzniku chyby
		 * počas vykonávania skriptu spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ktorej
		 * je poslané číslo chybového riadka v parametri {@code riadok}, text
		 * chybového hlásenia v parametri {@code príkaz} a {@code správa} má
		 * kód {@link GRobot#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}.
		 * Ak chyba nevznikne, tak je tiež spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ale prvé
		 * dva parametre sú prázdne a {@code správa} má kód
		 * {@link GRobot#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}. Dokedy nie je
		 * ukončená činnosť jedného skriptu, nie je možné spustiť vykonávanie
		 * ďalšieho, ale v priebehu jeho činnosti je možné (aj keď nie
		 * odporúčané) vykonať iný skript (metódou
		 * {@link #vykonajSkript(String[]) vykonajSkript} alebo jej „klonmi“)
		 * alebo príkaz (metódou {@link #vykonajPríkaz(String) vykonajPríkaz}
		 * alebo jej „klonmi“).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Zabezpečenie spustenia samotného príkazu
		 * {@code currspustiSkript} je jednoduché (v tomto príklade ho nájdete
		 * v metóde {@code spusti}), ale zabezpečenie spracovania rôznych
		 * situácií (udalostí) počas ladenia si vyžaduje náročnejší prístup.
		 * V tomto príklade je v komentároch stručne opísaný účel jednotlivých
		 * častí programu s prípadnými návrhmi na ďalšie vylepšovanie. Tento
		 * príklad v podstate iba naznačuje rôzne aspekty ladenia.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestLadenieSkriptu {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Nasledujúce premenné sú „semafory“ používané počas procesu ladenia:}
				{@code comm// }
				{@code comm//   – krok:     hodnota tejto premennej riadi krokovanie; hodnota true}
				{@code comm//               znamená posunutie programu o krok ďalej (na ďalší príkaz)}
				{@code comm//   – prerušiť: nastavením hodnoty tejto premennej na true je možné}
				{@code comm//               ladenie programu predčasne ukončiť}
				{@code kwdprivate} {@code typeboolean} krok = {@code valfalse}, prerušiť = {@code valfalse};

				{@code comm// Konštruktor.}
				{@code kwdprivate} TestLadenieSkriptu()
				{
					{@code comm// Definovanie obsluhy udalostí…}
					{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
					{
						{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}(
							{@code typeint} riadok, String príkaz, {@code typeint} správa)
						{
							{@code comm// Nasledujúce vetvenie zabezpečuje spracovanie rôznych}
							{@code comm// situácií počas ladenia:}
							{@code kwdswitch} (správa)
							{
								{@code kwdcase} {@link GRobot#ČAKAŤ ČAKAŤ}:
									{@code comm// Čakanie počas krokovania:}
									{@code kwdif} (krok)
									{
										krok = {@code valfalse};
										{@code kwdreturn} {@code valfalse};
									}
									{@code kwdreturn} {@code valtrue};

								{@code kwdcase} {@link GRobot#PRERUŠIŤ PRERUŠIŤ}:
									{@code comm// Predčasné ukončenie vykonávania skriptu:}
									{@code kwdif} (prerušiť)
									{
										{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#tmavooranžová tmavooranžová});
										{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vykonávanie bolo prerušené."});
										{@code comm// Ďalšie podrobnosti by sme mohli vypísať}
										{@code comm// napríklad pomocou nasledujúceho úryvku kódu:}
										{@code comm//    "na riadku", riadok, ":", GRobot.}{@link Konštanty#riadok riadok}{@code comm,}
										{@code comm//    príkaz}
									}
									{@code kwdreturn} prerušiť;

								{@code comm// Výpis definovaných menoviek skriptu odfiltrujeme:}
								{@code kwdcase} {@link GRobot#VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}: {@code kwdreturn} {@code valfalse};

								{@code kwdcase} {@link GRobot#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}:
									{@code kwdif} (!prerušiť)
									{
										{@code comm// Informáciu o ukončení vypíšeme len v prípade,}
										{@code comm// že program nebol prerušený:}
										{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#tmavotyrkysová tmavotyrkysová});
										{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vykonávanie bolo dokončené."});
									}
									{@code kwdreturn} {@code valfalse};

								{@code kwdcase} {@link GRobot#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}:
									{@code comm// Vypíšeme len text chyby…}
									{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
									{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(príkaz);
										{@code comm// Ďalšie podrobnosti by sme mohli vypísať}
										{@code comm// napríklad pomocou nasledujúcich úryvkov kódu:}
										{@code comm//    "Číslo chyby", Svet.}{@link Svet#kódPoslednejChyby() kódPoslednejChyby}{@code comm()}
										{@code comm//    "Riadok chyby ", riadok}
									{@code kwdreturn} {@code valfalse};

								{@code comm// Nefiltrujeme žiadne príkazy,}
								{@code comm// vykonávame všetko bez rozdielu:}
								{@code kwdcase} {@link GRobot#ZABRÁNIŤ_VYKONANIU ZABRÁNIŤ_VYKONANIU}: {@code kwdreturn} {@code valfalse};
							}

							{@code comm// Na všetky ostatné otázky režimu ladenia (}{@link GRobot#VYPÍSAŤ_PREMENNÉ VYPÍSAŤ_PREMENNÉ}{@code comm,}
							{@code comm// }{@link GRobot#VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK}{@code comm, }{@link GRobot#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}{@code comm, }{@link GRobot#VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}{@code comm) odpovedáme}
							{@code comm// kladne:}
							{@code kwdreturn} {@code valtrue};
						}

						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}()
						{
							{@code comm// Ak bolo stlačené pravé tlačidlo myši, zistíme hodnotu}
							{@code comm// číselnej premennej (toto je tu uvedené na demonštračné}
							{@code comm// účely, dalo by sa to rozpracovať tak, aby si používateľ}
							{@code comm// mohol zvoliť typ premennej).}
							{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#PRAVÉ PRAVÉ}))
							{
								{@link String String} názov = {@link Svet Svet}.{@link Svet#zadajReťazec(String) zadajReťazec}(
									{@code srg"Názov číselnej premennej:"});
								{@code kwdif} ({@code valnull} != názov)
								{
									{@code kwdif} ({@link Svet Svet}.{@link Svet#premennáJestvuje(String, Class) premennáJestvuje}(názov, Double.{@code typeclass}))
									{
										{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#tyrkysová tyrkysová});
										{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"     "}, názov, {@code srg" = "},
											{@link Svet Svet}.{@link Svet#čítajPremennú(String, Class) čítajPremennú}(názov, Double.{@code typeclass}));
									}
									{@code kwdelse}
									{
										{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
										{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"     Premenná "},
											názov, {@code srg"nejestvuje."});
									}
								}
							}
							{@code comm// Inak (t. j. ak boli stlačené ostatné tlačidlá myši):}
							{@code comm//   Ak prebieha vykonávanie skriptu, tak prejdeme na}
							{@code comm//   ďalší krok, inak začneme vykonávanie nového skriptu.}
							{@code kwdelse} {@code kwdif} ({@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}()) krok = {@code valtrue}; {@code kwdelse} spusti();
						}

						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}()
						{
							{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#ESCAPE ESCAPE}))
							{
								{@code comm// Klávesom ESC prerušíme ladenie…}
								prerušiť = {@code valtrue};
								krok = {@code valtrue};
							}
							{@code kwdelse} {@code kwdif} ({@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}() &&
								{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#MEDZERA MEDZERA}))
							{
								{@code comm// Klávesom medzera krokujeme…}
								krok = {@code valtrue};
							}
						}
					};

					{@code comm// Musíme zapnúť režim interaktívny režim (zapneme len Svet a robota),}
					{@code comm// režim ladenia a zabezpečíme, aby sa robot automaticky neskryl po}
					{@code comm// prvom výpise na vnútornú konzolu programovacieho rámca (pozri}
					{@code comm// poznámku na konci opisu metódy Plátno.}{@link Plátno#vypíš(Object[]) vypíš}{@code comm)}.
					{@link Svet Svet}.{@link Svet#režimLadenia(boolean) režimLadenia}({@code valtrue});
					{@link Svet Svet}.{@link Svet#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
					{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code valthis});
					{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});

					spusti();
				}

				{@code comm// Touto metódou sa spúšťa nové ladenie skriptu. Logicky, pokus}
				{@code comm// o spustenie nového ladenia bude vykonaný len v takom prípade,}
				{@code comm// keď nie je v činnosti iné ladenie. (Volanie metódy }{@code currspustiSkript}
				{@code comm// by v opačnom prípade i tak nemalo žiadny efekt.)}
				{@code kwdpublic} {@code typevoid} spusti()
				{
					{@code kwdif} (!{@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}())
					{
						krok = {@code valfalse};
						prerušiť = {@code valfalse};
						{@link GRobot#domov() domov}();
						{@link Svet Svet}.{@link Svet#vymaž() vymaž}();
						{@link Svet Svet}.{@link Svet#spustiSkript(String, boolean) spustiSkript}({@code srg"Test.GRScript"}, {@code valtrue});
					}
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}();
					{@code kwdnew} TestLadenieSkriptu();
				}
			}
			</pre>
		 * 
		 * @param riadky pole reťazcov reprezentujúcich riadky skriptu
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static void spustiSkript(String[] riadky)
		{
			if (!skriptJeSpustený)
			{
				skriptJeSpustený = true;
				new Thread(() ->
				{
					int chyba = vykonajSkript(riadky);

					if (null != ObsluhaUdalostí.počúvadlo)
					{
						if (ŽIADNA_CHYBA == chyba)
						{
							Skript.poslednáChyba = ŽIADNA_CHYBA;
							ObsluhaUdalostí.počúvadlo.ladenie(
								ŽIADNA_CHYBA, null, UKONČENIE_SKRIPTU);
						}
						else
							ObsluhaUdalostí.počúvadlo.ladenie(chyba,
								textPoslednejChyby(), UKONČENIE_CHYBOU);
					}

					skriptJeSpustený = false;
				}).start();
			}
		}

		/**
		 * <p>Funguje podobne ako {@link #vykonajSkript(String)
		 * vykonajSkript(skript)}, ale spustí vykonávanie v samostatnom vlákne
		 * Javy, takže môže byť ladené. Keďže pri tomto spôsobe nie je možné
		 * získať návratovú hodnotu priamo, tak je v prípade vzniku chyby
		 * počas vykonávania skriptu spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ktorej
		 * je poslané číslo chybového riadka v parametri {@code riadok}, text
		 * chybového hlásenia v parametri {@code príkaz} a {@code správa} má
		 * kód {@link GRobot#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}.
		 * Ak chyba nevznikne, tak je tiež spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ale prvé
		 * dva parametre sú prázdne a {@code správa} má kód
		 * {@link GRobot#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}. Dokedy nie je
		 * ukončená činnosť jedného skriptu, nie je možné spustiť vykonávanie
		 * ďalšieho, ale v priebehu jeho činnosti je možné (aj keď nie
		 * odporúčané) vykonať iný skript (metódou
		 * {@link #vykonajSkript(String[]) vykonajSkript} alebo jej „klonmi“)
		 * alebo príkaz (metódou {@link #vykonajPríkaz(String) vykonajPríkaz}
		 * alebo jej „klonmi“).</p>
		 * 
		 * @param skript skript uložený v jednom reťazci
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static void spustiSkript(String skript)
		{
			String riadky[] = Skript.vykonajSkriptRiadkovač.split(skript);
			spustiSkript(riadky);
		}

		/**
		 * <p>Funguje podobne ako {@link #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * vykonajSkript(skript)} a principiálne pre neho platia rovnaké
		 * pravidlá ako pre {@link #spustiSkript(String)
		 * spustiSkript(skript)}. Pozri opisy oboch metód na získanie
		 * úplného prehľadu o fungovaní tejto metódy. V skrátenej forme:
		 * táto metóda vytvorí zo zadaného zoznamu reťazcov kópiu do poľa
		 * reťazcov, ktorú spustí ako skript v samostatnom vlákne. Ďalšie
		 * podrobnosti o činnosti skriptov sú v opisoch dotknutých metód.</p>
		 * 
		 * @param skript skript uložený v zozname reťazcov
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String, boolean)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak bolo vykonávanie niektorého príkazu
		 *     skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static void spustiSkript(List<String> skript)
		{
			String riadky[] = new String[skript.size()];
			int i = 0; for (String príkaz : skript)
			{
				riadky[i] = príkaz;
				++i;
			}

			spustiSkript(riadky);
		}

		/**
		 * <p>Funguje podobne ako {@link #vykonajSkript(String, boolean)
		 * vykonajSkript(skript, zoSúboru)}, ale spustí vykonávanie
		 * v samostatnom vlákne Javy, takže môže byť ladené. Keďže pri tomto
		 * spôsobe nie je možné získať návratovú hodnotu priamo, tak je
		 * v prípade vzniku chyby počas vykonávania skriptu spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ktorej
		 * je poslané číslo chybového riadka v parametri {@code riadok}, text
		 * chybového hlásenia v parametri {@code príkaz} a {@code správa} má
		 * kód {@link GRobot#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}.
		 * Ak chyba nevznikne, tak je tiež spustená reakcia
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}, ale prvé
		 * dva parametre sú prázdne a {@code správa} má kód
		 * {@link GRobot#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}. Dokedy nie je
		 * ukončená činnosť jedného skriptu, nie je možné spustiť vykonávanie
		 * ďalšieho, ale v priebehu jeho činnosti je možné (aj keď nie
		 * odporúčané) vykonať iný skript (metódou
		 * {@link #vykonajSkript(String[]) vykonajSkript} alebo jej „klonmi“)
		 * alebo príkaz (metódou {@link #vykonajPríkaz(String) vykonajPríkaz}
		 * alebo jej „klonmi“).</p>
		 * 
		 * @param skript názov súboru so skriptom alebo skript uložený
		 *     v jednom reťazci
		 * @param zoSúboru ak je {@code valtrue}, tak je prvý parameter
		 *     považovaný za názov súboru so skriptom (uloženom s použitím
		 *     kódovania UTF-8); ak je {@code valfalse}, tak je prvý
		 *     parameter považovaný za skript (rovnako ako pri metóde
		 *     {@link #spustiSkript(String) spustiSkript(skript)})
		 * 
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #skriptJeSpustený()
		 * @see #spustiSkript(String[])
		 * @see #spustiSkript(String)
		 * <!-- @see #spustiSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #spustiSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 * 
		 * @throws GRobotException ak skript so zadaným menom nebol nájdený
		 *     alebo ak bolo vykonávanie niektorého príkazu skriptu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súviselo
		 *     vykonávanie niektorého príkazu skriptu nie je dostupná
		 *     (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súviselo vykonávanie niektorého z príkazov
		 *     skriptu nebol požadovaného typu, ani ho na taký typ nebolo
		 *     možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súviselo vykonávanie niektorého z príkazov skriptu
		 *     vznikla výnimka
		 */
		public static void spustiSkript(String skript, boolean zoSúboru)
		{
			if (zoSúboru)
			{
				try
				{
					BufferedReader čítanie;
					Vector<String> riadky = new Vector<>();

					try
					{
						// Vyhľadá súbor na pevnom disku
						čítanie = new BufferedReader(new InputStreamReader(
							Súbor.dajVstupnýPrúdSúboru(skript), "UTF-8"));
					}
					catch (FileNotFoundException notFound)
					{
						try
						{
							// Pokúsi sa prečítať skript zo zdroja .jar súboru
							čítanie = new BufferedReader(new InputStreamReader(
								Súbor.dajVstupnýPrúdZdroja(skript), "UTF-8"));
						}
						catch (NullPointerException isNull)
						{
							throw new GRobotException(
								"Skript „" + skript + "“ nebol nájdený.",
								"scriptNotFound", skript, notFound);
						}
					}

					String riadok = čítanie.readLine();
					if (null != riadok)
					{
						if (!riadok.isEmpty() &&
							riadok.charAt(0) == '\uFEFF')
							riadok = riadok.substring(1);

						do
						{
							riadky.add(riadok);
							riadok = čítanie.readLine();
						}
						while (null != riadok);
					}

					čítanie.close();
					if (!riadky.isEmpty())
						spustiSkript(riadky.toArray(new String[riadky.size()]));
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
					if (null != ObsluhaUdalostí.počúvadlo)
					{
						Skript.poslednáChyba = CHYBA_ČÍTANIA_SKRIPTU;
						ObsluhaUdalostí.počúvadlo.ladenie(Skript.poslednáChyba,
							textPoslednejChyby(), UKONČENIE_CHYBOU);
					}
				}
			}
			else spustiSkript(skript);
		}


		/**
		 * <p>Táto metóda je automaticky používaná {@linkplain 
		 * Svet#interaktívnyRežim(boolean) interaktívnym režimom}
		 * a umožňuje používať príkazy, ktoré sú dostupné v tomto režime
		 * za hranicami {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu} (t. j. bez nevyhnutnosti jeho aktivácie).</p>
		 * 
		 * @param príkaz príkazový riadok spĺňajúci pravidlá uvedené
		 *     v opise metódy {@link Svet#interaktívnyRežim(boolean)
		 *     interaktívnyRežim}
		 * @return {@code valtrue} ak bol príkaz nájdený a podarilo
		 *     sa ho vykonať
		 * 
		 * @see GRobot#vykonajPríkaz(String)
		 * @see Plátno#vykonajPríkaz(String)
		 * @see GRobot#interaktívnyRežim(boolean)
		 * @see Svet#interaktívnyRežim(boolean)
		 * @see Plátno#interaktívnyRežim(boolean)
		 * @see Svet#režimLadenia(boolean)
		 * 
		 * @throws GRobotException ak bolo vykonávanie príkazu zrušené
		 * @throws IllegalAccessException ak metóda, s ktorou súvisí
		 *     vykonávanie príkazu nie je dostupná (verejná)
		 * @throws IllegalArgumentException ak niektorý z argumentov
		 *     metódy, s ktorou súvisí vykonávanie príkazu nie je
		 *     požadovaného typu, ani ho na neho nie je možné previesť
		 * @throws InvocationTargetException ak pri vykonávní metódy,
		 *     s ktorou súvisí vykonávanie príkazu vznikla výnimka
		 */
		public static boolean vykonajPríkaz(String príkaz)
		{
			if (Skript.vykonajPríkaz(príkaz, Svet.class, null)) return true;
			if (Skript.vykonajPríkaz(príkaz, Math.class, null)) return true;
			if (Skript.vykonajPríkaz(príkaz, Bod.class, null)) return true;
			return Skript.vykonajPríkaz(príkaz, Farba.class, null);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vykonajPríkaz(String) vykonajPríkaz}.</p> */
		public static boolean vykonajPrikaz(String príkaz)
		{ return vykonajPríkaz(príkaz); }

		/**
		 * <p>Zapne alebo vypne interaktívny režim pre inštanciu sveta.</p>
		 * 
		 * <p>Režim umožňuje pracovať so zvoleným robotom/robotmi,
		 * svetom a/alebo plátnom tak, že mnohé príkazy môžu byť spracúvané
		 * priamo počas činnosti programu a to buď zadaním do zobrazeného
		 * príkazového riadka, alebo
		 * {@linkplain #vykonajSkript(String[]) spustením skriptu}.
		 * 
		 * Príkazy interaktívneho režimu môžu byť vykonávané aj bez jeho
		 * aktivácie a to volaním prislúchajúcej metódy –
		 * 
		 * {@link GRobot#vykonajPríkaz(String) robot.vykonajPríkaz(príkaz)},
		 * {@link Svet#vykonajPríkaz(String) Svet.vykonajPríkaz(príkaz)},
		 * {@link Plátno#vykonajPríkaz(String) podlaha.vykonajPríkaz(príkaz)}
		 * alebo
		 * {@link Plátno#vykonajPríkaz(String) strop.vykonajPríkaz(príkaz)}.</p>
		 * 
		 * <p>Vykonávanie príkazov interaktívneho režimu nie je z pohľadu
		 * spracovania príliš efektívne, ale je názorné. Začínajúci
		 * študent/&#8203;programátor môže ľahko vidieť, ktorý zo základných
		 * príkazov má aký efekt. Po zapnutí interaktívneho príkazového
		 * režimu pre niektorého z robotov, svet a/alebo plátno je automaticky
		 * zobrazený vstupný riadok, ktorý od tejto chvíle očakáva príkazy
		 * zadané v špecifickom tvare opísanom nižšie.</p>
		 * 
		 * <p>Po potvrdení vstupu alebo {@linkplain #vykonajSkript(String[])
		 * spustení skriptu} sú prehľadávané všetky inštancie robota
		 * (alebo jeho potomkov), ktoré majú tento režim aktivovaný, v rámci
		 * toho je prehľadaná trieda {@link Súbor Súbor} rovnakého robota,
		 * potom je prehľadná trieda {@link Svet Svet} spolu s triedou Javy
		 * {@link java.lang.Math Math} a statickou časťou tried
		 * {@link Bod Bod} a {@link Farba Farba}. Nakoniec sú prehľadané
		 * inštancie {@linkplain Plátno plátien} podlaha a strop. (Hľadajú sa
		 * príkazy, ktoré zodpovedajú schéme opísanej nižšie.)</p>
		 * 
		 * <p>To znamená, že príkazy sú prioritne posielané inštanciám robotov
		 * (ktoré sú v interaktívnom režime) a až potom ostatným inštanciám
		 * ({@link Svet Svet}, {@link java.lang.Math Math}…). Prvý pozitívny
		 * výsledok v dávke spracovania príkazového riadka ukončuje ďalšie
		 * spracovanie. To znamená, že ak zareaguje aspoň jeden robot (môžu
		 * zareagovať aj všetky), tak sa spracovanie príkazu končí.
		 * Iba v prípade, že na zadaný príkaz nezareagoval žiadny robot, je
		 * príkaz poslaný svetu a ten v prípade svojho neúspechu dodatočne
		 * prehľadá ďalšie triedy ({@link java.lang.Math Math}…).
		 * Ak nezareaguje ani jedna súčasť prehľadávaná svetom, tak sú na
		 * rade plátna. Ak majú interaktívny režim aktivované obe plátna,
		 * im je príkaz poslaný obom, ak iba jedno, tak iba jednému z nich.</p>
		 * 
		 * <p>Ak je aktívna obsluha udalostí, tak je v prípade pozitívneho
		 * výsledku spracovania príkazu potvrdeného v príkazovom riadku
		 * spustená reakcia
		 * 
		 * {@link ObsluhaUdalostí#spracovaniePríkazu() spracovaniePríkazu}.
		 * 
		 * V prípade negatívneho výsledku (t. j. ak potvrdený príkaz nebol
		 * rozpoznaný žiadnou z prehľadávaných súčastí) sú spustené reakcie
		 * na klasické potvrdenie vstupu
		 * 
		 * ({@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov},
		 * {@link ObsluhaUdalostí#potvrdenieVstupu() potvrdenieVstupu}).</p>
		 * 
		 * <p>Po deaktivovaní interaktívneho príkazového režimu všetkých
		 * dotknutých entít (robotov a/alebo sveta), je vstupný riadok
		 * automaticky skrytý a to bez ohľadu na to, či bol pred aktiváciou
		 * režimu zobrazený alebo nie.</p>
		 * 
		 * <p>Príkazy interaktívneho režimu <b>nie sú</b> zadávané
		 * v syntakticky identickom tvare ako pri volaní metódy Javy.
		 * Zápis príkazu musí byť ekvivalentný takej metóde robota (resp.
		 * sveta alebo metódy triedy <code>Math</code>), ktorá spĺňa jednu
		 * z nasledujúcich podmienok:</p>
		 * 
		 * <ul>
		 * <li>neprijíma žiadny argument,</li>
		 * <li>prijíma jeden až štyri číselné argumenty
		 * (typ {@code typedouble} alebo {@code typeint}),</li>
		 * <li>prijíma jeden booleovský argument (typ
		 * {@code typeboolean}),</li>
		 * <li>prijíma jeden argument určujúci farbu, ktorá musí byť
		 * v skripte alebo príkazovom riadku zapísaná slovne: červená,
		 * zelená, modrá… (tu sú dovolené len preddefinované farby,
		 * ale číselnými kombináciami argumentov (nasmerovaných do inej
		 * verzie tejto metódy) je možné namiešať ľubovoľnú farbu),</li>
		 * <li>všetky vyššie uvedené možnosti s jedným reťazcovým
		 * parametrom navyše, ktorý môže originálna metóda prijímať buď
		 * na prvom mieste, alebo poslednom mieste (zápis reťazca v skripte
		 * je povolený buď na poslednom mieste s jednou úvodzovkou, alebo
		 * na ľubovoľnom mieste s dvomi úvodzovkami).</li>
		 * </ul>
		 * 
		 * <p>Počet argumentov konkrétneho príkazu musí byť rovnaký ako
		 * počet parametrov metódy, ktorej volanie má reprezentovať. To,
		 * že počet číselných argumentov sa môže pohybovať v rozsahu nula
		 * až štyri znamená, že interaktívny režim vyhľadáva iba také
		 * metódy, ktoré majú deklarovaný uvedený počet parametrov.</p>
		 * 
		 * <p>Jednotlivé slová príkazov interaktívneho režimu musia byť
		 * oddelené medzerou, na veľkosti písmen nezáleží a diakritika musí
		 * byť buď úplne uvedená, alebo úplne vynechaná. Argumenty musia byť
		 * od príkazu aj od seba navzájom oddelené medzerou alebo čiarkou.
		 * Ich údajový typ musí byť v súlade s vyššie uvedenými bodmi.
		 * Reťazcový argument musí byť vždy uvedený ako posledný (aj keď
		 * ide o ekvivalent metódy, ktorá ho má v skutočnosti na začiatku)
		 * a musí sa začínať strojopisnými úvodzovkami {@code "}. (Všetok
		 * zvyšný obsah príkazového riadka nasledujúci za úvodzovkou je
		 * považovaný za obsah reťazca vrátane prípadných ďalších
		 * úvodzoviek a vrátane prípadnej úvodzovky na konci riadka.)</p>
		 * 
		 * <p>Interaktívny režim umožňuje pracovať s vnútornými premennými.
		 * Premenná vzniká pri prvom vložení hodnoty do nej. Na to slúži
		 * príkaz {@code nech}, za ktorým musí nasledovať názov premennej.
		 * Za názvom premennej môže byť uvedený operátor a ak nejde o unárny
		 * operátor, tak ďalej musí byť uvedená buď hodnota, alebo príkaz
		 * spĺňajúci podmienky uvedené vyššie. Binárne operácie fungujú tak,
		 * že uvedená hodnota alebo výsledok príkazu sú spracované spolu
		 * s aktuálnou hodnotou premennej – to znamená, že ak ide o sčítanie,
		 * tak je hodnota pripočítaná k hodnote premennej, ak ide o logický
		 * súčet, tak výsledok v premennej bude nula v prípade, že je hodnota
		 * premennej aj výrazu napravo boli nulové a jedna v opačnom prípade
		 * atď. Návratová hodnota príkazu (originálnej metódy programovacieho
		 * rámca) musí byť číslo (prípadne logická hodnota), farba, poloha alebo
		 * reťazec. Ak sa pracuje s číslami, tak operátor môže byť jedným
		 * z nasledujúceho zoznamu:</p>
		 * 
		 * <table>
		 * <tr><td rowspan="17"> </td>
		 * <td><code>=</code></td><td>–</td><td>priradenie</td></tr>
		 * <tr><td><code>+</code></td><td>–</td><td>sčítanie</td></tr>
		 * <tr><td><code>-</code></td><td>–</td><td>odčítanie</td></tr>
		 * <tr><td><code>*</code></td><td>–</td><td>násobenie</td></tr>
		 * <tr><td><code>/</code></td><td>–</td><td>delenie</td></tr>
		 * <tr><td><code>%</code></td><td>–</td><td>zvyšok po delení</td></tr>
		 * <tr><td><code>_</code></td><td>–</td><td>odseknutie – odstránenie desatinnej časti</td></tr>
		 * <tr><td><code>~</code></td><td>–</td><td>zmena znamienka (z kladného na záporné alebo naopak)</td></tr>
		 * <tr><td><code>==</code></td><td>–</td><td>zhoda</td></tr>
		 * <tr><td><code>!=</code></td><td>–</td><td>nezhoda</td></tr>
		 * <tr><td><code>!</code></td><td>–</td><td>logická negácia</td></tr>
		 * <tr><td><code>&amp;&amp;</code></td><td>–</td><td>logické a súčasne</td></tr>
		 * <tr><td><code>||</code></td><td>–</td><td>logické alebo</td></tr>
		 * <tr><td><code>&gt;</code></td><td>–</td><td>väčší než</td></tr>
		 * <tr><td><code>&lt;</code></td><td>–</td><td>menší než</td></tr>
		 * <tr><td><code>&gt;=</code></td><td>–</td><td>väčší alebo rovný než</td></tr>
		 * <tr><td><code>&lt;=</code></td><td>–</td><td>menší alebo rovný než</td></tr>
		 * </table>
		 * 
		 * <p><b>Napríklad:</b></p>
		 * 
		 * <p>    nech a = 10.0<br />
		 *     nech a + 2.0<br />
		 *     nech f = farba<br /></p>
		 * 
		 * <p>Príkazy sveta {@link Svet#vypíš(Object[]) vypíš} a {@link 
		 * Svet#vypíšRiadok(Object[]) vypíšRiadok} sú sprístupnené nad rámec
		 * vyššie uvedených podmienok (nespĺňali by podmienky, pretože
		 * prijímajú variabilný počet argumentov). Sú dostupné vždy a vždy
		 * posielajú svoj výpis prostredníctvom sveta (t. j. na vnútornú
		 * konzolu stropu). No ich správanie je mierne pozmenené.
		 * Reťazcový argument je vždy vypísaný ako prvý a po ňom nasledujú
		 * ďalšie argumenty (jeden až štyri číselné alebo farba).
		 * V prípade potreby je možné želaný výsledok dosiahnuť viacnásobným
		 * zadaním príkazu {@code vypíš} a {@code vypíš riadok}.</p>
		 * 
		 * <p>Ak sa riadok skriptu začína znakom {@code @}, tak je neprázdny
		 * zvyšok riadka považovaný za názov {@linkplain 
		 * #interaktívnaInštancia(String) interaktívnej inštancie}. Ak je
		 * zvyšok riadka prázdny, tak je {@linkplain 
		 * #interaktívnaInštancia(String) interaktívna inštancia} zrušená.</p>
		 * 
		 * <p>Niekoľko ďalších príkazov bolo transformovaných tak, aby lepšie
		 * zapadali do fungovania interaktívneho režimu, napríklad:
		 * zadaj číslo, uprav číslo, náhodné číslo, zadaj reťazec…</p>
		 * 
		 * <p> </p>
		 * 
		 * <p><b>Príklady príkazov uložených v skripte (pozri príkazy
		 * {@link #vykonajSkript(String[]) vykonajSkript(riadky)},
		 * {@link #spustiSkript(String[]) spustiSkript(riadky)}
		 * a im príbuzné; okrem opakovania sú všetky príkazy nasledujúceho
		 * skriptu použiteľné aj v príkazovom riadku interaktívneho
		 * režimu):</b></p>
		 * 
		 * <p>nech dĺžka = uprav číslo 100, "Zadaj stranu<br />
		 * nech koľko = uprav číslo 72, "Zadaj počet opakovaní<br />
		 * <br />
		 * skoč dĺžka, -50<br />
		 * farba zelená<br />
		 * <br />
		 * nech i = koľko<br />
		 * :opakuj1<br />
		 *   dopredu dĺžka<br />
		 *   vpravo 145<br />
		 * dokedy i, opakuj1<br />
		 * <br />
		 * nech dĺžka * 3<br />
		 * nech dĺžka ~<br />
		 * skoč dĺžka, 0<br />
		 * nech dĺžka ~<br />
		 * nech dĺžka / 3<br />
		 * <br />
		 * zdvihni pero<br />
		 * začni cestu<br />
		 * <br />
		 * nech i = 5<br />
		 * :opakuj2<br />
		 *   dopredu dĺžka<br />
		 *   vpravo 72<br />
		 * dokedy i, opakuj2<br />
		 * <br />
		 * farba žltá<br />
		 * vyplň cestu<br />
		 * farba čierna<br />
		 * hrúbka čiary 3.2<br />
		 * kresli cestu<br />
		 * <br />
		 * vypíš dĺžka, "Toto je výsledok kreslenia s dĺžkou <br />
		 * vypíš koľko, " a počtom opakovaní <br />
		 * vypíš riadok ":<br />
		 * <br />
		 * nech veľkosť = uprav číslo 30, "Zadaj veľkosť<br />
		 * veľkosť veľkosť<br />
		 * nech text = "Veľkosť: <br />
		 * nech text + veľkosť<br />
		 * <br />
		 * domov<br />
		 * dozadu veľkosť<br />
		 * dozadu 10<br />
		 * text text<br />
		 * zobraz<br />
		 * domov<br /></p>
		 * 
		 * <hr />
		 * 
		 * <p>Po potvrdení predvolených údajov (100, 72 a 30) bude výsledok
		 * takýto:</p>
		 * 
		 * <p><image>interaktivnyRezim.png<alt/>Výsledok vykonania
		 * skriptu.</image>Výsledok vykonania skriptu.</p>
		 * 
		 * <p> </p>
		 * 
		 * <!--
		 * ✗ Bolo nakoniec implementované inak:
		 * 
		 * Definuj novú metódu upravPríkaz(), ktorá vyhľadá v zadanom
		 * príkazovom riadku reťazce v tvare "…", odfiltruje
		 * začiatočné a koncové úvodzovky, pričom dvojité úvodzovky
		 * v nich bude považovať za úvodzovky, spojí ich do jedného
		 * reťazca (na čo treba upozorniť v dokumentácii) a presunie
		 * výsledný reťazec na koniec príkazového riadka (samozrejme
		 * s prefixom úvodzoviek. Môže tiež robiť ďalšie úpravy, to
		 * treba ešte zvážiť.
		 * 
		 * K tejto metóde definuj sériu metód zapniPredspracovanie(),
		 * zapniPredspracovanie(boolean), vypniPredspracovanie(),
		 * jePredspracovanieZapnuté(), ktoré budú zapínať/vypínať
		 * automatické predspracovanie príkazových (aj skriptových)
		 * riadkov. Pozor! Predvolene bude automatické predspracovanie
		 * zapnuté, na čo bude upozornené v dokumentácii.
		 * 
		 * Pozor: Nasledujúce „zlepšenie spätnej kompatibility“ by
		 * prinieslo viac problémov, než úžitku: Ak sa na jednom
		 * riadku nachádza len jedna úvodzovka, tak bude zvyšok
		 * riadka za ňou považovaný za reťazec (ako to bolo doteraz).
		 * 
		 * Jednak by to nebola stopercentná spätná kompatibilita
		 * a zároveň by mohlo nastať nechcené ignorovanie chýb‼
		 * -->
		 * 
		 * @param zapni ak je {@code valtrue}, režim bude pre svet
		 *     zapnutý, inak bude vypnutý
		 * 
		 * @see GRobot#interaktívnyRežim(boolean)
		 * @see Plátno#interaktívnyRežim(boolean)
		 * @see GRobot#vykonajPríkaz(String)
		 * @see #vykonajPríkaz(String)
		 * @see Plátno#vykonajPríkaz(String)
		 * @see #vykonajSkript(String[])
		 * @see #vykonajSkript(String)
		 * <!-- @see #vykonajSkript(List<String>) TODO: porovnaj nižšie -->
		 * @see #vykonajSkript(List) <!-- TODO: overiť, či to tak môže byť -->
		 * @see #vykonajSkript(String, boolean)
		 * @see #interaktívnaInštancia()
		 * @see #zrušInteraktívnuInštanciu()
		 * @see #režimLadenia(boolean)
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 */
		public static void interaktívnyRežim(boolean zapni)
		{
			if (zapni)
			{
				if (!interaktívnyRežim)
				{
					interaktívnyRežim = true;
					if (0 == početVInteraktívnomRežime)
						Svet.neskrývajVstupnýRiadok(true);
					++početVInteraktívnomRežime;
				}
			}
			else
			{
				if (interaktívnyRežim)
				{
					interaktívnyRežim = false;
					--početVInteraktívnomRežime;
					if (0 == početVInteraktívnomRežime)
						Svet.skrývajVstupnýRiadok(true);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnyRežim(boolean) interaktívnyRežim}.</p> */
		public static void interaktivnyRezim(boolean zapni)
		{ interaktívnyRežim(zapni); }

		/**
		 * <p>Zistí stav interaktívneho režimu pre svet.
		 * Pozri: {@link Svet#interaktívnyRežim(boolean)
		 * Svet.interaktívnyRežim(zapni)}.</p>
		 * 
		 * @return stav režimu pre svet
		 */
		public static boolean interaktívnyRežim() { return interaktívnyRežim; }

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnyRežim() interaktívnyRežim}.</p> */
		public static boolean interaktivnyRezim() { return interaktívnyRežim; }

		/**
		 * <p><a class="setter"></a> Nastaví alebo zruší aktívnu inštanciu
		 * pre {@linkplain Svet#interaktívnyRežim(boolean) interaktívny
		 * režim}. Keď je aktívna inštancia, tak príkazy odoslané
		 * v príkazovom riadku budú spracované len korektne rozpoznanou
		 * inštanciou.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto inštancia ovplyvňuje len
		 * príkazy zadávané cez príkazový riadok. Neovplyvňuje príkazy
		 * vykonávané ako {@linkplain #vykonajSkript(String[]) skript}.
		 * Aktívnu inštanciu {@linkplain #vykonajSkript(String[]) skriptu}
		 * je možné ovplyvňovať len priamo v {@linkplain 
		 * #vykonajSkript(String[]) skripte} riadkami so syntaxou:
		 * {@code @}<em>«názov inštancie»</em> (nastavenie inštancie)
		 * a {@code @} (zrušenie inštancie). Rovnaký príkaz je použiteľný
		 * aj v príkazovom riadku {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu} a táto metóda poskytuje nástroj na
		 * nastavenie aktívnej inštancie programátorom.</p>
		 * 
		 * <p>Medzi predvolené názvy inštancií patria: {@code svet},
		 * {@code podlaha}, {@code strop} a {@code robot}. Ďalšou inštanciou
		 * môže byť ľubovoľný {@linkplain GRobot#meno(String) pomenovaný
		 * robot}. Na zrušenie aktívnej inštancie treba zadať namiesto
		 * parametra {@code inštancia} {@code valnull} alebo zavolať metódu
		 * {@link #zrušInteraktívnuInštanciu() zrušInteraktívnuInštanciu}.</p>
		 * 
		 * @param inštancia nová aktívna inštancia {@linkplain 
		 *     Svet#interaktívnyRežim(boolean) interaktívneho režimu} alebo
		 *     {@code valnull}
		 * 
		 * @see #interaktívnaInštancia()
		 * @see #zrušInteraktívnuInštanciu()
		 * @see #interaktívnyRežim(boolean)
		 * @see #režimLadenia(boolean)
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public static void interaktívnaInštancia(String inštancia)
		{ interaktívnaInštancia = inštancia/*.toLowerCase()*/; }

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnaInštancia(String) interaktívnaInštancia}.</p> */
		public static void interaktivnaInstancia(String inštancia)
		{ interaktívnaInštancia = inštancia/*.toLowerCase()*/; }

		/**
		 * <p><a class="getter"></a> Vráti aktívnu inštanciu {@linkplain 
		 * Svet#interaktívnyRežim(boolean) interaktívneho režimu} alebo
		 * {@code valnull}.</p>
		 * 
		 * @return aktívna inštancia {@linkplain 
		 *     Svet#interaktívnyRežim(boolean) interaktívneho režimu} alebo
		 *     {@code valnull}
		 * 
		 * @see #interaktívnaInštancia(String)
		 */
		public static String interaktívnaInštancia()
		{ return interaktívnaInštancia; }

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnaInštancia() interaktívnaInštancia}.</p> */
		public static String interaktivnaInstancia()
		{ return interaktívnaInštancia; }

		/**
		 * <p>Zruší interaktívnu inštanciu.</p>
		 * 
		 * @see #interaktívnaInštancia(String)
		 */
		public static void zrušInteraktívnuInštanciu()
		{ interaktívnaInštancia = null; }

		/** <p><a class="alias"></a> Alias pre {@link #zrušInteraktívnuInštanciu() zrušInteraktívnuInštanciu}.</p> */
		public static void zrusInteraktivnuInstanciu()
		{ interaktívnaInštancia = null; }

		/**
		 * <p>Získa kód poslednej chyby, ktorá nastala počas vykonávania
		 * príkazov {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívneho režimu} alebo {@linkplain 
		 * Svet#vykonajSkript(String[]) skriptu}. Môže ísť o jeden
		 * z nasledujúcich kódov:</p>
		 * 
		 * <ul>
		 * <li>{@link GRobot#ŽIADNA_CHYBA ŽIADNA_CHYBA},</li>
		 * <li>{@link GRobot#CHYBA_VYKONANIA_PRÍKAZU CHYBA_VYKONANIA_PRÍKAZU},</li>
		 * <li>{@link GRobot#CHYBA_DVOJITÁ_MENOVKA CHYBA_DVOJITÁ_MENOVKA},</li>
		 * <li>{@link GRobot#CHYBA_CHÝBAJÚCA_MENOVKA CHYBA_CHÝBAJÚCA_MENOVKA},</li>
		 * <li>{@link GRobot#CHYBA_NEZNÁMA_MENOVKA CHYBA_NEZNÁMA_MENOVKA},</li>
		 * <li>{@link GRobot#CHYBA_NEZNÁME_SLOVO CHYBA_NEZNÁME_SLOVO},</li>
		 * <li>{@link GRobot#CHYBA_CHYBNÁ_ŠTRUKTÚRA CHYBA_CHYBNÁ_ŠTRUKTÚRA},</li>
		 * <li>{@link GRobot#CHYBA_NEZNÁME_MENO CHYBA_NEZNÁME_MENO},</li>
		 * <li>{@link GRobot#CHYBA_NEZNÁMY_PRÍKAZ CHYBA_NEZNÁMY_PRÍKAZ},</li>
		 * <li>{@link GRobot#CHYBA_ČÍTANIA_SKRIPTU CHYBA_ČÍTANIA_SKRIPTU}.</li>
		 * <li>alebo {@link GRobot#CHYBA_VOLANIA_SKRIPTU
		 * CHYBA_VOLANIA_SKRIPTU}.</li>
		 * </ul>
		 * 
		 * @return kód chyby – na získanie podrobností klikni na meno chyby
		 *     v zozname vyššie
		 * 
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 */
		public static int kódPoslednejChyby()
		{ return Skript.poslednáChyba % 100; }

		/** <p><a class="alias"></a> Alias pre {@link #kódPoslednejChyby() kódPoslednejChyby}.</p> */
		public static int kodPoslednejChyby()
		{ return Skript.poslednáChyba % 100; }

		/**
		 * <p>Ak posledná chyba vznikla na konkrétnom riadku {@linkplain 
		 * Svet#vykonajSkript(String[]) skriptu}, tak táto metóda vráti
		 * číslo tohto riadka.</p>
		 * 
		 * @return číslo riadka, na ktorom vznikla posledná chyba
		 * 
		 * @see #kódPoslednejChyby()
		 * @see #textPoslednejChyby()
		 * @see #textChyby(int)
		 */
		public static int riadokPoslednejChyby()
		{ return Skript.poslednáChyba / 100; }

		/**
		 * <p>Vráti vysvetľujúci text ku kódu poslednej chyby, ktorá nastala
		 * počas vykonávania príkazov
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo
		 * {@linkplain Svet#vykonajSkript(String[]) skriptu}.</p>
		 * 
		 * @return text ku kódu chyby
		 * 
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textChyby(int)
		 */
		public static String textPoslednejChyby()
		{ return Skript.textChyby(Skript.poslednáChyba); }

		/**
		 * <p>Vráti vysvetľujúci text ku kódu chyby určenej parametrom
		 * {@code kódChyby}. Ide o kódy vnútorne definované a používané
		 * pri hláseniach o nesprávnych stavoch počas vykonávania príkazov
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo
		 * {@linkplain Svet#vykonajSkript(String[]) skriptu}.</p>
		 * 
		 * @return text ku kódu chyby
		 * 
		 * @see #kódPoslednejChyby()
		 * @see #riadokPoslednejChyby()
		 * @see #textPoslednejChyby()
		 */
		public static String textChyby(int kódChyby)
		{
			return Skript.textChyby(kódChyby);
		}

		/**
		 * <p>Získa návratovú hodnotu naposledy vykonaného príkazu
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) skriptu}.</p>
		 * 
		 * @return návratová hodnota naposledy vykonaného príkazu
		 */
		public static Object poslednáNávratováHodnota()
		{ return Skript.poslednáNávratováHodnota(); }

		/** <p><a class="alias"></a> Alias pre {@link #poslednáNávratováHodnota() poslednáNávratováHodnota}.</p> */
		public static Object poslednaNavratovaHodnota()
		{ return Skript.poslednáNávratováHodnota(); }


		/**
		 * <p>Sformuluje znenie chyby skriptu na základe zadaného kódu chyby
		 * a zobrazí dialóg s chybovým hlásením rozdeleným na riadky so
		 * zadaným počtom znakov a zadaným titulkom chybového dialógu.
		 * Kód chyby je návratová hodnota skriptu (pozri {@link Skript
		 * Skript}{@code .}{@link Skript#vykonaj() vykonaj}{@code ()}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda bola pôvodne
		 * vytvorená na vnútorné účely – pre skripty tlačidiel a položiek
		 * ponuky, ktoré by inak nemali žiadnu možnosť poskytnutia spätnej
		 * väzby používateľovi. Tieto skripty stále metódu používajú, ale
		 * medzičasom sa metóda stala verejnou a vznikli jej ďalšie
		 * verzie.</p>
		 * 
		 * @param kódSkriptu chybový kód, na základe ktorého metóda
		 *     sformuluje a zobrazí chybové hlásenie v dialógovom okne
		 * @param titulokChyby text titulku dialógového okna chyby
		 * @param šírkaRiadka počet znakov na jednom riadku chybového
		 *     hlásenia
		 */
		public static void formulujChybuSkriptu(int kódSkriptu,
			String titulokChyby, int šírkaRiadka)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Chyba na riadku ");
			sb.append(kódSkriptu);
			sb.append(". ");
			sb.append(textPoslednejChyby());

			String[] riadky = textNaRiadky(sb.toString(), šírkaRiadka);

			sb.setLength(0);
			for (String riadok : riadky)
			{
				sb.append(riadok);
				sb.append("\n");
			}

			if (CHYBA_NEZNÁMY_PRÍKAZ == kódPoslednejChyby())
			{
				sb.append("\n");
				riadky = textNaRiadky("(Tip: Nemusí ísť nevyhnutne " +
					"o neznámy príkaz. Môže ísť o prípad nesprávne " +
					"nastavenej inštancie. Tento prípad má niekoľko možných " +
					"riešení. Napríklad: Vložte nový riadok pred riadkom, " +
					"na ktorom nastala chyba a použite syntax @«inštancia» " +
					"na nastavenie inštancie. Príklady: @svet, @podlaha, " +
					"@strop, @robot…)", šírkaRiadka);
				for (String riadok : riadky)
				{
					sb.append(riadok);
					sb.append("\n");
				}
			}

			Svet.chyba(sb.toString(), titulokChyby);
		}

		/**
		 * <p>Sformuluje znenie chyby skriptu na základe zadaného kódu chyby
		 * a zobrazí dialóg s chybovým hlásením rozdeleným na riadky so
		 * predvoleným počtom znakov (@code num60) a zadaným titulkom
		 * chybového dialógu. Kód chyby je návratová hodnota skriptu (pozri
		 * {@link Skript Skript}{@code .}{@link Skript#vykonaj()
		 * vykonaj}{@code ()}).</p>
		 * 
		 * @param kódSkriptu chybový kód, na základe ktorého metóda
		 *     sformuluje a zobrazí chybové hlásenie v dialógovom okne
		 * @param titulokChyby text titulku dialógového okna chyby
		 */
		public static void formulujChybuSkriptu(int kódSkriptu,
			String titulokChyby)
		{ formulujChybuSkriptu(kódSkriptu, titulokChyby, 60); }

		/**
		 * <p>Sformuluje znenie chyby skriptu na základe zadaného kódu chyby
		 * a zobrazí dialóg s chybovým hlásením rozdeleným na riadky so
		 * zadaným počtom znakov a predvoleným titulkom chybového dialógu
		 * ({@code srg"Chyba skriptu…"}). Kód chyby je návratová hodnota
		 * skriptu (pozri {@link Skript Skript}{@code .}{@link 
		 * Skript#vykonaj() vykonaj}{@code ()}).</p>
		 * 
		 * @param kódSkriptu chybový kód, na základe ktorého metóda
		 *     sformuluje a zobrazí chybové hlásenie v dialógovom okne
		 * @param šírkaRiadka počet znakov na jednom riadku chybového
		 *     hlásenia
		 */
		public static void formulujChybuSkriptu(int kódSkriptu,
			int šírkaRiadka)
		{ formulujChybuSkriptu(kódSkriptu, "Chyba skriptu…", šírkaRiadka); }

		/**
		 * <p>Sformuluje znenie chyby skriptu na základe zadaného kódu chyby
		 * a zobrazí dialóg s chybovým hlásením rozdeleným na riadky so
		 * predvoleným počtom znakov (@code num60) a predvoleným titulkom
		 * chybového dialógu ({@code srg"Chyba skriptu…"}).
		 * Kód chyby je návratová hodnota skriptu (pozri {@link Skript
		 * Skript}{@code .}{@link Skript#vykonaj() vykonaj}{@code ()}).</p>
		 * 
		 * @param kódSkriptu chybový kód, na základe ktorého metóda
		 *     sformuluje a zobrazí chybové hlásenie v dialógovom okne
		 */
		public static void formulujChybuSkriptu(int kódSkriptu)
		{ formulujChybuSkriptu(kódSkriptu, "Chyba skriptu…", 60); }


		// Vnútorná konzola

		/**
		 * <p><a class="getter"></a> Zistí stav/hodnotu vlastnosti zalamovania
		 * textov vnútornej konzoly stropu. Má rovnaký efekt ako keby sme
		 * volali metódu
		 * {@link Plátno#zalamujeTexty() strop.zalamujeTexty()}.</p>
		 * 
		 * @return {@code valtrue} ak sú texty stropu presahujúce pravý okraj
		 *     okna automaticky zalamované; {@code valfalse} v opačnom
		 *     prípade
		 */
		public static boolean zalamujeTexty()
		{ return GRobot.strop.zalamujeTexty(); }

		/**
		 * <p><a class="setter"></a> Nastaví vlastnosť zalamovania textov
		 * vnútornej konzoly stropu podľa hodnoty parametra. Má rovnaký efekt
		 * ako keby sme volali metódu {@link Plátno#zalamujTexty(boolean)
		 * strop.zalamujTexty(zalamuj)}.</p>
		 * 
		 * @param zalamuj {@code valtrue} ak chceme, aby boli texty stropu
		 *     presahujúce pravý okraj okna automaticky zalomené; {@code 
		 *     valfalse} v opačnom prípade
		 */
		public static void zalamujTexty(boolean zalamuj)
		{ GRobot.strop.zalamujTexty(zalamuj); }

		/**
		 * <p>Zapne zalamovanie textov vnútornej konzoly stropu. Má rovnaký efekt
		 * ako keby sme volali metódu {@link Plátno#zalamujTexty()
		 * strop.zalamujTexty()}.</p>
		 */
		public static void zalamujTexty() { GRobot.strop.zalamujTexty(true); }

		/**
		 * <p>Vypne zalamovanie textov vnútornej konzoly stropu. Má rovnaký
		 * efekt ako keby sme volali metódu {@link 
		 * Plátno#nezalamujTexty() strop.nezalamujTexty()}.</p>
		 */
		public static void nezalamujTexty() { GRobot.strop.zalamujTexty(false); }


		/**
		 * <p>Vypíše sériu argumentov v tvare textu na strope ako aktívne
		 * slová. Má rovnaký efekt ako keby sme volali metódu {@link 
		 * Plátno#vypíšAktívneSlovo(String, Object[])
		 * strop.vypíšAktívneSlovo(String, Object...)}.</p>
		 * 
		 * @param identifikátor identifikátor aktívneho slova, pomocou
		 *     ktorého bude toto slovo odlišované od ostatných aktívnych slov
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 */
		public static void vypíšAktívneSlovo(
			String identifikátor, Object... argumenty)
		{ GRobot.strop.vypíšAktívneSlovo(identifikátor, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public static void vypisAktivneSlovo(
			String identifikátor, Object... argumenty)
		{ GRobot.strop.vypíšAktívneSlovo(identifikátor, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public static void vypíšAktívneSlová(
			String identifikátor, Object... argumenty)
		{ GRobot.strop.vypíšAktívneSlovo(identifikátor, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public static void vypisAktivneSlova(
			String identifikátor, Object... argumenty)
		{ GRobot.strop.vypíšAktívneSlovo(identifikátor, argumenty); }

		/**
		 * <p>Overí, či sa bod identifikovaný zadanými súradnicami nachádza
		 * v oblasti niektorého z aktívnych slov. Funguje rovnako ako keby
		 * sme volali metódu {@link Plátno#bodVAktívnomSlove(double,
		 * double) strop.bodVAktívnomSlove(double, double)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return identifikátor aktívneho slova, v ktorom sa nachádza bod
		 *     so zadanými súradnicami alebo {@code valnull}
		 */
		public static String bodVAktívnomSlove(double x, double y)
		{ return GRobot.strop.bodVAktívnomSlove(x, y); }

		/** <p><a class="alias"></a> Alias pre {@link #bodVAktívnomSlove(double, double) bodVAktívnomSlove}.</p> */
		public static String bodVAktivnomSlove(double x, double y)
		{ return GRobot.strop.bodVAktívnomSlove(x, y); }

		/**
		 * <p>Funguje rovnako ako metóda {@link #bodVAktívnomSlove(double,
		 * double) bodVAktívnomSlove}, ale namiesto súradníc bodu je zadaný
		 * objekt, ktorý je implementáciou rozhrania {@link Poloha Poloha}.</p>
		 * 
		 * @param poloha poloha vyšetrovaného bodu zadaná prostredníctvom
		 *     objektu implementujúceho rozhranie {@link Poloha Poloha}.
		 * @return identifikátor aktívneho slova, v ktorom sa nachádza bod
		 *     so zadanými súradnicami alebo {@code valnull}
		 */
		public static String bodVAktívnomSlove(Poloha poloha)
		{
			return GRobot.strop.bodVAktívnomSlove(
				poloha.polohaX(), poloha.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #bodVAktívnomSlove(Poloha) bodVAktívnomSlove}.</p> */
		public static String bodVAktivnomSlove(Poloha poloha)
		{
			return GRobot.strop.bodVAktívnomSlove(
				poloha.polohaX(), poloha.polohaY());
		}

		/**
		 * <p>Overí, či sa súradnice kurzora myši nachádzajú v oblasti niektorého
		 * z aktívnych slov. Funguje rovnako ako keby sme volali metódu
		 * {@link Plátno#myšVAktívnomSlove() strop.myšVAktívnomSlove()}.</p>
		 * 
		 * @return identifikátor aktívneho slova, v ktorom sa nachádza
		 *     kurzor myši alebo {@code valnull}
		 */
		public static String myšVAktívnomSlove()
		{
			return GRobot.strop.bodVAktívnomSlove(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #myšVAktívnomSlove() myšVAktívnomSlove}.</p> */
		public static String mysVAktivnomSlove()
		{
			return GRobot.strop.bodVAktívnomSlove(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}


		/**
		 * <p>Vypíše sériu argumentov v tvare textu na strope. Má rovnaký efekt
		 * ako keby sme volali metódu {@link Plátno#vypíš(Object[])
		 * strop.vypíš(Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 * 
		 * @see Plátno#vypíš(Object[])
		 * @see #formát
		 */
		public static void vypíš(Object... argumenty)
		{ GRobot.strop.vypíš(argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíš(Object[]) vypíš}.</p> */
		public static void vypis(Object... argumenty)
		{ GRobot.strop.vypíš(argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na strope a posunie sa na
		 * ďalší riadok. Má rovnaký efekt ako keby sme volali metódu {@link 
		 * Plátno#vypíšRiadok(Object[])
		 * strop.vypíšRiadok(Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 * 
		 * @see #vypíš(Object[])
		 */
		public static void vypíšRiadok(Object... argumenty)
		{ GRobot.strop.vypíšRiadok(argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p> */
		public static void vypisRiadok(Object... argumenty)
		{ GRobot.strop.vypíšRiadok(argumenty); }

		/**
		 * <p>Vypíše sériu argumentov na zadaných súradniciach v tvare textu na
		 * strope. Má rovnaký efekt ako keby sme volali metódu {@link 
		 * Plátno#vypíšNa(double, double, Object[])
		 * strop.vypíšNa(double, double, Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu oddelený
		 *     čiarkami
		 */
		public static void vypíšNa(double x, double y, Object... argumenty)
		{ GRobot.strop.vypíšNa(x, y, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšNa(double, double, Object[]) vypíšNa}.</p> */
		public static void vypisNa(double x, double y, Object... argumenty)
		{ GRobot.strop.vypíšNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov na zadaných súradniciach v tvare textu na
		 * strope a posunie sa na ďalší riadok. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#vypíšRiadokNa(double, double,
		 * Object[]) strop.vypíšRiadokNa(double, double, Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu oddelený
		 *     čiarkami
		 */
		public static void vypíšRiadokNa(double x, double y, Object... argumenty)
		{ GRobot.strop.vypíšRiadokNa(x, y, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšRiadokNa(double, double, Object[]) vypíšRiadokNa}.</p> */
		public static void vypisRiadokNa(double x, double y, Object... argumenty)
		{ GRobot.strop.vypíšRiadokNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov na zadaných súradniciach v tvare textu na
		 * strope. Má rovnaký efekt ako keby sme volali metódu {@link 
		 * Plátno#píšNa(double, double, Object[])
		 * strop.píšNa(double, double, Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu oddelený
		 *     čiarkami
		 */
		public static void píšNa(double x, double y, Object... argumenty)
		{ GRobot.strop.píšNa(x, y, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #píšNa(double, double, Object[]) píšNa}.</p> */
		public static void pisNa(double x, double y, Object... argumenty)
		{ GRobot.strop.píšNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov na zadaných súradniciach v tvare textu na
		 * strope a posunie sa na ďalší riadok. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#píšRiadokNa(double, double,
		 * Object[]) strop.píšRiadokNa(double, double, Object...)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu oddelený
		 *     čiarkami
		 */
		public static void píšRiadokNa(double x, double y, Object... argumenty)
		{ GRobot.strop.píšRiadokNa(x, y, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #píšRiadokNa(double, double, Object[]) píšRiadokNa}.</p> */
		public static void pisRiadokNa(double x, double y, Object... argumenty)
		{ GRobot.strop.píšRiadokNa(x, y, argumenty); }


		// Náhodné čísla

		/**
		 * <p>Generovanie náhodného celého čísla v celom rozsahu {@code typelong}.
		 * (Pozri: {@link Long Long}{@code .}{@link Long#MIN_VALUE MIN_VALUE}
		 * a {@link Long Long}{@code .}{@link Long#MAX_VALUE MAX_VALUE}.)</p>
		 * 
		 * @return vygenerované náhodné celé číslo
		 */
		public static long náhodnéCeléČíslo() { return generátor.nextLong(); }

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéCeléČíslo() náhodnéCeléČíslo}.</p> */
		public static long nahodneCeleCislo() { return generátor.nextLong(); }

		/**
		 * <p>Generovanie náhodného celého čísla v rozsahu od nula
		 * po zadanú hodnotu parametra (vrátane).</p>
		 * 
		 * @param hodnota druhá hranica generovania náhodných čísel
		 * @return vygenerované náhodné celé číslo
		 */
		public static long náhodnéCeléČíslo(long hodnota)
		{
			if (0 == hodnota) return 0;
			if (hodnota > 0)
				return Math.abs(generátor.nextLong() % (hodnota + 1));
			return -Math.abs(generátor.nextLong() % (hodnota - 1));
		}

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéCeléČíslo(long) náhodnéCeléČíslo}.</p> */
		public static long nahodneCeleCislo(long hodnota) { return náhodnéCeléČíslo(hodnota); }

		/**
		 * <p>Generovanie náhodného celého čísla v zadanom rozsahu.</p>
		 * 
		 * @param min spodná hranica generovania náhodných čísel
		 * @param max horná hranica generovania náhodných čísel
		 * @return vygenerované náhodné celé číslo
		 */
		public static long náhodnéCeléČíslo(long min, long max)
		{
			if (min > max)
				return max + (Math.abs(generátor.nextLong()) % (1 + min - max));
			return min + (Math.abs(generátor.nextLong()) % (1 + max - min));
		}

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}.</p> */
		public static long nahodneCeleCislo(long min, long max) { return náhodnéCeléČíslo(min, max); }

		/**
		 * <p>Generovanie náhodného reálneho čísla v rozsahu od nula
		 * po jeden.</p>
		 * 
		 * @return vygenerované náhodné reálne číslo
		 */
		public static double náhodnéReálneČíslo() { return generátor.nextDouble(); }

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéReálneČíslo() náhodnéReálneČíslo}.</p> */
		public static double nahodneRealneCislo() { return generátor.nextDouble(); }

		/**
		 * <p>Generovanie náhodného reálneho čísla v rozsahu od nula
		 * po zadanú hodnotu parametra.</p>
		 * 
		 * @param hodnota horná hranica generovania náhodných čísel
		 * @return vygenerované náhodné reálne číslo
		 */
		public static double náhodnéReálneČíslo(double hodnota)
		{ return generátor.nextDouble() * hodnota; }

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéReálneČíslo(double) náhodnéReálneČíslo}.</p> */
		public static double nahodneRealneCislo(double hodnota)
		{ return generátor.nextDouble() * hodnota; }

		/**
		 * <p>Generovanie náhodného reálneho čísla v zadanom rozsahu.</p>
		 * 
		 * @param min spodná hranica generovania náhodných čísel
		 * @param max horná hranica generovania náhodných čísel
		 * @return vygenerované náhodné reálne číslo
		 */
		public static double náhodnéReálneČíslo(double min, double max)
		{
			if (min > max)
				return max + (generátor.nextDouble() * (min - max));
			return min + (generátor.nextDouble() * (max - min));
		}

		/** <p><a class="alias"></a> Alias pre {@link #náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}.</p> */
		public static double nahodneRealneCislo(double min, double max)
		{ return náhodnéReálneČíslo(min, max); }


		// Schránka

		/**
		 * <p>Vloží do schránky všetky texty vypísané na vnútorných konzolách
		 * podlahy a stropu.</p>
		 * 
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#text(String)
		 */
		public static boolean textyDoSchránky()
		{
			final StringBuffer texty = new StringBuffer();
			texty.setLength(0);
			GRobot.podlaha.vnútornáKonzola.prevezmiTexty(texty);
			GRobot.strop.vnútornáKonzola.prevezmiTexty(texty);
			return Schránka.text(texty.toString());
		}

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public static boolean textyDoSchranky() { return textyDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public static boolean textDoSchránky() { return textyDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public static boolean textDoSchranky() { return textyDoSchránky(); }

		/**
		 * <p>Vloží do schránky buď všetky texty vypísané na vnútorných
		 * konzolách podlahy a stropu, alebo len označené časti –
		 * v závislosti od hodnoty parametra {@code lenOznačené}.</p>
		 * 
		 * @param lenOznačené ak je hodnota tohto parametra rovná
		 *     {@code valtrue}, tak metóda skopíruje schránky len
		 *     označené texty konzol, inak sa správa rovnako ako metóda
		 *     {@link #textyDoSchránky() textyDoSchránky()}
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#text(String)
		 */
		public static boolean textyDoSchránky(boolean lenOznačené)
		{
			final StringBuffer texty = new StringBuffer();
			texty.setLength(0);
			GRobot.podlaha.vnútornáKonzola.prevezmiTexty(texty, lenOznačené);
			GRobot.strop.vnútornáKonzola.prevezmiTexty(texty, lenOznačené);
			return Schránka.text(texty.toString());
		}

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public static boolean textyDoSchranky(boolean lenOznačené) { return textyDoSchránky(lenOznačené); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public static boolean textDoSchránky(boolean lenOznačené) { return textyDoSchránky(lenOznačené); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public static boolean textDoSchranky(boolean lenOznačené) { return textyDoSchránky(lenOznačené); }

		/**
		 * <p>Vloží obrázok (grafiku) sveta do schránky. Grafika sveta je
		 * zlúčením grafík podlahy, robotov, stropu a textov vypísaných na
		 * vnútorné konzoly oboch plátien.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Obrázok je uchovaný
		 * v schránke len počas činnosti aplikácie robota. Po zatvorení
		 * okna sveta, je obrázok zo schránky odstránený.</p>
		 * 
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#obrázok(Image)
		 */
		public static boolean obrázokDoSchránky()
		{ return Schránka.obrázok(obrázokSveta2); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public static boolean obrazokDoSchranky() { return obrázokDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public static boolean grafikaDoSchránky() { return obrázokDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public static boolean grafikaDoSchranky() { return obrázokDoSchránky(); }


	// --- Obrázky


		// Výška/šírka (obrázka)

		/**
		 * <p><a class="getter"></a> Zistí šírku zadaného obrázka.</p>
		 * 
		 * <p>Táto metóda spôsobí prečítanie obrázka zo súboru a jeho
		 * uchovanie vo vnútornej pamäti sveta. Obrázok tam zostáva,
		 * pretože je chápaný ako zdroj. Môže však byť v prípade potreby
		 * (napríklad ak sa obsah súboru na disku zmenil) z vnútornej pamäte
		 * odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre všetky
		 * metódy pracujúce s obrázkami alebo zvukmi, ktoré prijímajú názov
		 * súboru ako parameter.)</p>
		 * 
		 * @param súbor názov súboru s obrázkom
		 * @return šírka zadaného obrázka
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static int šírkaObrázka(String súbor) { return Obrázok.súborNaObrázok(súbor).getWidth(null); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaObrázka(String) šírkaObrázka}.</p> */
		public static int sirkaObrazka(String súbor) { return Obrázok.súborNaObrázok(súbor).getWidth(null); }

		/**
		 * <p><a class="getter"></a> Zistí výšku zadaného obrázka.</p>
		 * 
		 * <p>Táto metóda spôsobí prečítanie obrázka zo súboru a jeho
		 * uchovanie vo vnútornej pamäti sveta. Obrázok tam zostáva,
		 * pretože je chápaný ako zdroj. Môže však byť v prípade potreby
		 * (napríklad ak sa obsah súboru na disku zmenil) z vnútornej pamäte
		 * odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre všetky
		 * metódy pracujúce s obrázkami alebo zvukmi, ktoré prijímajú názov
		 * súboru ako parameter.)</p>
		 * 
		 * @param súbor názov súboru s obrázkom
		 * @return výška zadaného obrázka
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static int výškaObrázka(String súbor) { return Obrázok.súborNaObrázok(súbor).getHeight(null); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaObrázka(String) výškaObrázka}.</p> */
		public static int vyskaObrazka(String súbor) { return Obrázok.súborNaObrázok(súbor).getHeight(null); }


		// Čítanie

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
		 * @see GRobot#obrázok(String)
		 * @see GRobot#obrázok(String, double)
		 * @see GRobot#obrázok(String, double, double)
		 * @see GRobot#obrázok(String, double, double, double)
		 * @see GRobot#obrázok(String, int)
		 * @see GRobot#obrázok(String, int, double)
		 * @see GRobot#obrázok(String, int, double, double)
		 * @see GRobot#obrázok(String, int, double, double, double)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vlastnýTvar(String)
		 * @see GRobot#vlastnýTvar(String, boolean)
		 * @see Svet#obrázok(String)
		 * @see Svet#vyplň(String)
		 * @see Svet#ikona(String)
		 * @see Svet#zobrazUvodnuObrazovku(String)
		 * @see Svet#šírkaObrázka(String)
		 * @see Svet#výškaObrázka(String)
		 * @see Svet#čítajObrázky(Object[])
		 * @see Svet#čítajObrázky(String[])
		 * @see Svet#čítajObrázok(String)
		 * @see Plátno#obrázok(String)
		 * @see Plátno#obrázok(double, double, String)
		 * @see Plátno#obrázok(Poloha, String)
		 * @see Plátno#vyplň(String)
		 * @see Obrázok#priečinokObrázkov(String)
		 * @see Obrázok#čítaj(String)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#kresli(String)
		 * @see Obrázok#kresli(double, double, String)
		 * @see Obrázok#kresli(Poloha, String)
		 * @see Obrázok#vyplň(String)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(GRobot, String)
		 * @see PoložkaPonuky#ikona(String)
		 * @see KontextováPoložka#ikona(String)
		 */
		public static void priečinokObrázkov(String priečinok)
		{ Obrázok.priečinokObrázkov = Súbor.upravLomky(priečinok, true); }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokObrázkov(String) priečinokObrázkov}.</p> */
		public static void priecinokObrazkov(String priečinok)
		{ Obrázok.priečinokObrázkov = Súbor.upravLomky(priečinok, true); }

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
		 * @see GRobot#obrázok(String)
		 * @see GRobot#obrázok(String, double)
		 * @see GRobot#obrázok(String, double, double)
		 * @see GRobot#obrázok(String, double, double, double)
		 * @see GRobot#obrázok(String, int)
		 * @see GRobot#obrázok(String, int, double)
		 * @see GRobot#obrázok(String, int, double, double)
		 * @see GRobot#obrázok(String, int, double, double, double)
		 * @see GRobot#vyplňOblasť(Area, String)
		 * @see GRobot#vlastnýTvar(String)
		 * @see GRobot#vlastnýTvar(String, boolean)
		 * @see Svet#obrázok(String)
		 * @see Svet#vyplň(String)
		 * @see Svet#ikona(String)
		 * @see Svet#zobrazUvodnuObrazovku(String)
		 * @see Svet#šírkaObrázka(String)
		 * @see Svet#výškaObrázka(String)
		 * @see Svet#čítajObrázky(Object[])
		 * @see Svet#čítajObrázky(String[])
		 * @see Svet#čítajObrázok(String)
		 * @see Plátno#obrázok(String)
		 * @see Plátno#obrázok(double, double, String)
		 * @see Plátno#obrázok(Poloha, String)
		 * @see Plátno#vyplň(String)
		 * @see Obrázok#priečinokObrázkov()
		 * @see Obrázok#čítaj(String)
		 * @see Obrázok#vyplň(Shape, String)
		 * @see Obrázok#kresli(String)
		 * @see Obrázok#kresli(double, double, String)
		 * @see Obrázok#kresli(Poloha, String)
		 * @see Obrázok#vyplň(String)
		 * @see Oblasť#vyplň(String)
		 * @see Oblasť#vyplň(GRobot, String)
		 * @see PoložkaPonuky#ikona(String)
		 * @see KontextováPoložka#ikona(String)
		 */
		public static String priečinokObrázkov()
		{ return Obrázok.priečinokObrázkov; }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokObrázkov() priečinokObrázkov}.</p> */
		public static String priecinokObrazkov()
		{ return Obrázok.priečinokObrázkov; }

		/**
		 * <p>Táto metóda slúži na čítanie zadaných obrázkov do vnútornej pamäte
		 * sveta (napríklad pri štarte aplikácie). Svet ukladá do vnútornej
		 * pamäte každý obrázok, s ktorým sa pracovalo (ak tam už nie je).
		 * Z nej môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * Táto metóda číta súbory s obrázkami vopred a tým môže prispieť
		 * k plynulejšej činnosti aplikácie po štarte (čas štartu sa
		 * predĺži, ale keďže súbory už nemusia byť čítané počas jej
		 * činnosti, pracuje plynulejšie).</p>
		 * 
		 * @param súbory ľubovoľný počet reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void čítajObrázky(Object... súbory)
		{
			GRobotException gre = null;
			for (Object súbor : súbory)
				try { Obrázok.súborNaObrázok(súbor.toString()); }
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(Object[]) čítajObrázky}.</p> */
		public static void citajObrazky(Object... súbory)
		{ čítajObrázky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(Object[]) čítajObrázky}.</p> */
		public static void prečítajObrázky(Object... súbory)
		{ čítajObrázky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(Object[]) čítajObrázky}.</p> */
		public static void precitajObrazky(Object... súbory)
		{ čítajObrázky(súbory); }

		/**
		 * <p>Táto metóda slúži na čítanie zadaných obrázkov do vnútornej pamäte
		 * sveta (napríklad pri štarte aplikácie). Svet ukladá do vnútornej
		 * pamäte ukladá každý obrázok, s ktorým sa pracovalo (ak tam už nie
		 * je). Z nej môže byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * Táto metóda číta súbory s obrázkami vopred a tým môže prispieť
		 * k plynulejšej činnosti aplikácie po štarte (čas štartu sa
		 * predĺži, ale keďže súbory už nemusia byť čítané počas jej
		 * činnosti, pracuje plynulejšie).</p>
		 * 
		 * @param súbory pole reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void čítajObrázky(String[] súbory)
		{
			GRobotException gre = null;
			for (String súbor : súbory)
				try { Obrázok.súborNaObrázok(súbor); }
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(String[]) čítajObrázky}.</p> */
		public static void citajObrazky(String[] súbory)
		{ čítajObrázky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(String[]) čítajObrázky}.</p> */
		public static void prečítajObrázky(String[] súbory)
		{ čítajObrázky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázky(String[]) čítajObrázky}.</p> */
		public static void precitajObrazky(String[] súbory)
		{ čítajObrázky(súbory); }

		/**
		 * <p>Prečíta do vnútornej pamäte sveta zadaný obrázok zo súboru a vráti
		 * ho v objekte typu {@link Image Image}. Obrázok nie je zobrazený.
		 * Podobnú úlohu plní metóda {@link #čítajObrázky(Object[])
		 * Svet.čítajObrázky(Object... súbory)}, ale pomocou nej nie je
		 * možné získať objekt typu {@link Image Image} na ďalšie
		 * spracovanie. Obrázok môže byť v prípade potreby (napríklad ak sa
		 * obsah súboru na disku zmenil) z vnútornej pamäte odstránený
		 * metódou {@link Svet#uvoľni(String) Svet.uvoľni(názovZdroja)}.</p>
		 * 
		 * @param súbor názov súboru s obrázkom
		 * @return obrázok v objekte typu {@link Image Image}
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 * 
		 * @see Obrázok#čítaj(String)
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static Image čítajObrázok(String súbor) { return Obrázok.súborNaObrázok(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázok(String) čítajObrázok}.</p> */
		public static Image citajObrazok(String súbor) { return Obrázok.súborNaObrázok(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázok(String) čítajObrázok}.</p> */
		public static Image prečítajObrázok(String súbor) { return Obrázok.súborNaObrázok(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajObrázok(String) čítajObrázok}.</p> */
		public static Image precitajObrazok(String súbor) { return Obrázok.súborNaObrázok(súbor); }


		// Kreslenie

		/**
		 * <p>Nakreslí v strede podlahy zadaný obrázok. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#obrázok(String)
		 * podlaha.obrázok(String)}. Napríklad:</p>
		 * 
		 * <pre CLASS="example">
			{@link Svet Svet}.{@code currobrázok}({@code srg"obrázok.png"});
			</pre>
		 * 
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void obrázok(String súbor) { GRobot.podlaha.obrázok(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(String) obrázok}.</p> */
		public static void obrazok(String súbor) { GRobot.podlaha.obrázok(súbor); }


		/**
		 * <p>Nakreslí na zadaných súradniciach na podlahe obrázok. Má rovnaký
		 * efekt ako keby sme volali metódu {@link 
		 * Plátno#obrázok(double, double, String)
		 * podlaha.obrázok(x, y, súbor)}.</p>
		 * 
		 * @param x x-ová súradnica polohy obrázka
		 * @param y y-ová súradnica polohy obrázka
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void obrázok(double x, double y, String súbor)
		{ GRobot.podlaha.obrázok(x, y, súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(double, double, String) obrázok}.</p> */
		public static void obrazok(double x, double y, String súbor)
		{ GRobot.podlaha.obrázok(x, y, súbor); }

		/**
		 * <p>Nakreslí na súradniciach zadaného objektu na podlahe obrázok.
		 * Má rovnaký efekt ako keby sme volali metódu
		 * {@link Plátno#obrázok(Poloha, String)
		 * podlaha.obrázok(objekt, súbor)}.</p>
		 * 
		 * @param objekt objekt určujúci polohu kreslenia obrázka
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public static void obrázok(Poloha objekt, String súbor)
		{ GRobot.podlaha.obrázok(objekt.polohaX(), objekt.polohaY(), súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Poloha, String) obrázok}.</p> */
		public static void obrazok(Poloha objekt, String súbor)
		{ GRobot.podlaha.obrázok(objekt.polohaX(), objekt.polohaY(), súbor); }


		/**
		 * <p>Nakreslí v strede podlahy zadaný obrázok. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#obrázok(Image)
		 * podlaha.obrázok(obrázok)}.</p>
		 * 
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 */
		public static void obrázok(Image obrázok)
		{ GRobot.podlaha.obrázok(obrázok); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Image) obrázok}.</p> */
		public static void obrazok(Image obrázok)
		{ GRobot.podlaha.obrázok(obrázok); }

		/**
		 * <p>Nakreslí na zadaných súradniciach na podlahe obrázok. Má rovnaký
		 * efekt ako keby sme volali metódu {@link Plátno#obrázok(double,
		 * double, Image) podlaha.obrázok(x, y, obrázok)}.</p>
		 * 
		 * @param x x-ová súradnica polohy obrázka
		 * @param y y-ová súradnica polohy obrázka
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 */
		public static void obrázok(double x, double y, Image obrázok)
		{ GRobot.podlaha.obrázok(x, y, obrázok); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(double, double, Image) obrázok}.</p> */
		public static void obrazok(double x, double y, Image obrázok)
		{ GRobot.podlaha.obrázok(x, y, obrázok); }

		/**
		 * <p>Nakreslí na súradniciach zadaného objektu na podlahe obrázok.
		 * Má rovnaký efekt ako keby sme volali metódu
		 * {@link Plátno#obrázok(Poloha, Image)
		 * podlaha.obrázok(objekt, obrázok)}.</p>
		 * 
		 * @param objekt objekt určujúci polohu kreslenia obrázka
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 */
		public static void obrázok(Poloha objekt, Image obrázok)
		{ GRobot.podlaha.obrázok(objekt.polohaX(), objekt.polohaY(), obrázok); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Poloha, Image) obrázok}.</p> */
		public static void obrazok(Poloha objekt, Image obrázok)
		{ GRobot.podlaha.obrázok(objekt.polohaX(), objekt.polohaY(), obrázok); }


		// Ukladanie

		/**
		 * <p>Uloží aktuálne zobrazený obsah sveta do súboru s obrázkom. Prípona
		 * súboru musí byť {@code .gif}, {@code .png} alebo {@code .jpg}
		 * (resp. {@code .jpeg}). Ak súbor jestvuje, tak vznikne výnimka
		 * oznamujúca, že súbor so zadaným menom už jestvuje. Ak chcete súbor
		 * prepísať, použite metódu {@link #uložObrázok(String, boolean)}
		 * s druhým parametrom rovným {@code valtrue}.</p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * 
		 * @throws GRobotException ak súbor jestvuje alebo nebol zadaný
		 *     názov súboru s platnou príponou
		 */
		public static void uložObrázok(String súbor) { uložObrázok(súbor, false); }

		/** <p><a class="alias"></a> Alias pre {@link #uložObrázok(String) uložObrázok}.</p> */
		public static void ulozObrazok(String súbor) { uložObrázok(súbor, false); }

		/**
		 * <p>Uloží aktuálny obsah sveta do súboru s obrázkom. Prípona súboru
		 * musí byť {@code .gif}, {@code .png} alebo {@code .jpg} (resp.
		 * {@code .jpeg}).</p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * @param prepísať ak je {@code valtrue}, prípadný jestvujúci
		 *     súbor bude prepísaný, inak sa správa rovnako ako metóda
		 *     {@link #uložObrázok(String)}
		 * 
		 * @throws GRobotException ak súbor jestvuje a parameter prepísať
		 *     je {@code valfalse} alebo ak bol zadaný názov súboru
		 *     s neplatnou príponou
		 */
		public static void uložObrázok(String súbor, boolean prepísať)
		{
			File uložiťDo = new File(súbor);

			if (!prepísať && uložiťDo.exists())
				throw new GRobotException("Obrázok „" + súbor +
					"“ už jestvuje.", "imageAlreadyExists", súbor);

			String prípona = súbor.substring(súbor.
				lastIndexOf('.') + 1).toLowerCase();

			if (prípona.equals("png"))
			{
				// Súbory png:
				try { ImageIO.write(obrázokSveta1, prípona, uložiťDo); }
				catch (IOException e)
				{ GRobotException.vypíšChybovéHlásenia(e, true); }
			}
			else if (prípona.equals("jpg") || prípona.equals("jpeg"))
			{
				// Pre jpeg musíme zmeniť farebný model z ARGB na RGB
				// Pozri: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727

				WritableRaster raster = obrázokSveta1.getRaster();
				WritableRaster novýRaster = raster.createWritableChild(
					0, 0, Plátno.šírkaPlátna, Plátno.výškaPlátna,
					0, 0, new int[] {0, 1, 2});

				DirectColorModel farebnýModel = (DirectColorModel)
					obrázokSveta1.getColorModel();

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
			else
			{
				throw new GRobotException("Neplatný formát obrázka: " +
					prípona.toUpperCase(), "invalidImageFormat", prípona);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uložObrázok(String, boolean) uložObrázok}.</p> */
		public static void ulozObrazok(String súbor, boolean prepísať) { uložObrázok(súbor, prepísať); }


	// --- Farba a písmo


		// Farba textu

		/**
		 * <p>Zistí aktuálnu farbu výpisu nových textov stropu. Má rovnaký efekt
		 * ako keby sme volali metódu {@link Plátno#farbaTextu()
		 * strop.farbaTextu()}.</p>
		 * 
		 * @return aktuálna farba textov stropu (objekt typu {@link Farba
		 *     Farba})
		 */
		public static Farba farbaTextu() { return GRobot.strop.farbaTextu(); }

		/**
		 * <p>Nastav farbu textov stropu. Má rovnaký efekt ako keby sme volali
		 * metódu {@link Plátno#farbaTextu(Color)
		 * strop.farbaTextu(Color)}.</p>
		 * 
		 * @param nováFarba objekt určujúci novú farbu pera
		 */
		public static void farbaTextu(Color nováFarba)
		{ GRobot.strop.farbaTextu(nováFarba); }

		/**
		 * <p>Nastav farbu textov stropu. Má rovnaký efekt ako keby sme volali
		 * metódu {@link Plátno#farbaTextu(Farebnosť)
		 * strop.farbaTextu(Color)}.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu pera
		 */
		public static void farbaTextu(Farebnosť objekt)
		{ GRobot.strop.farbaTextu(objekt); }

		/**
		 * <p>Nastav farbu textov stropu. Má rovnaký efekt ako keby sme volali
		 * metódu {@link Plátno#farbaTextu(int, int, int)
		 * strop.farbaTextu(int, int, int)}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba textov
		 */
		public static Farba farbaTextu(int r, int g, int b)
		{ return GRobot.strop.farbaTextu(r, g, b); }

		/**
		 * <p>Nastav farbu textov stropu. Má rovnaký efekt ako keby sme volali
		 * metódu {@link Plátno#farbaTextu(int, int, int, int)
		 * strop.farbaTextu(int, int, int, int)}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} – nová farba textov
		 */
		public static Farba farbaTextu(int r, int g, int b, int a)
		{ return GRobot.strop.farbaTextu(r, g, b, a); }

		/**
		 * <p>Zmení farbu textov stropu na predvolenú. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#predvolenáFarbaTextu()
		 * strop.predvolenáFarbaTextu()}.</p>
		 */
		public static void predvolenáFarbaTextu()
		{ GRobot.strop.predvolenáFarbaTextu(); }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextu() predvolenáFarbaTextu}.</p> */
		public static void predvolenaFarbaTextu()
		{ GRobot.strop.predvolenáFarbaTextu(); }


		// Farba pozadia textov

		/**
		 * <p>Zistí aktuálnu farbu pozadia výpisu nových textov stropu. Má
		 * rovnaký efekt ako keby sme volali metódu
		 * {@link Plátno#farbaPozadiaTextu() strop.farbaPozadiaTextu()}.</p>
		 * 
		 * @return aktuálna farba pozadia textov na strope (objekt typu
		 *     {@link Farba Farba})
		 */
		public static Farba farbaPozadiaTextu()
		{ return GRobot.strop.farbaPozadiaTextu(); }

		/**
		 * <p>Nastav farbu pozadia textov stropu. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#farbaPozadiaTextu(Color)
		 * strop.farbaPozadiaTextu(Color)}.</p>
		 * 
		 * @param nováFarba objekt určujúci novú farbu pozadia textov
		 */
		public static void farbaPozadiaTextu(Color nováFarba)
		{ GRobot.strop.farbaPozadiaTextu(nováFarba); }

		/**
		 * <p>Nastav farbu pozadia textov stropu. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#farbaPozadiaTextu(Farebnosť)
		 * strop.farbaPozadiaTextu(Color)}.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu pozadia textov
		 */
		public static void farbaPozadiaTextu(Farebnosť objekt)
		{ GRobot.strop.farbaPozadiaTextu(objekt); }

		/**
		 * <p>Nastav farbu pozadia textov stropu. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#farbaPozadiaTextu(int, int, int)
		 * strop.farbaPozadiaTextu(int, int, int)}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia textov
		 */
		public static Farba farbaPozadiaTextu(int r, int g, int b)
		{ return GRobot.strop.farbaPozadiaTextu(r, g, b); }

		/**
		 * <p>Nastav farbu pozadia textov stropu. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#farbaPozadiaTextu(int, int, int,
		 * int) strop.farbaPozadiaTextu(int, int, int, int)}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia textov
		 */
		public static Farba farbaPozadiaTextu(int r, int g, int b, int a)
		{ return GRobot.strop.farbaPozadiaTextu(r, g, b, a); }

		/**
		 * <p>Zmení farbu pozadia textov stropu na predvolenú. (Predvolenou
		 * farbou pozadia textov nie je žiadna farba – je to objekt farby
		 * s hodnotou {@code valnull}.) Volanie tejto metódy má rovnaký efekt
		 * ako keby sme volali metódu
		 * {@link Plátno#predvolenáFarbaPozadiaTextu()
		 * strop.predvolenáFarbaPozadiaTextu()}.</p>
		 */
		public static void predvolenáFarbaPozadiaTextu()
		{ GRobot.strop.predvolenáFarbaPozadiaTextu(); }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaTextu() predvolenáFarbaPozadiaTextu}.</p> */
		public static void predvolenaFarbaPozadiaTextu()
		{ GRobot.strop.predvolenáFarbaPozadiaTextu(); }


		// Farba pozadia (sveta)

		/**
		 * <p><a class="getter"></a> Čítaj farbu pozadia sveta.
		 * Ide o farbu podkladu, ktorá je použitá v prípade, že podlaha ani
		 * strop neboli vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
		 * 
		 * @return aktuálna farba pozadia sveta (objekt typu {@link Farba
		 *     Farba})
		 */
		public static Farba farbaPozadia() { return farbaPozadia; }

		/**
		 * <p><a class="setter"></a> Nastav farbu pozadia sveta.
		 * Ide o farbu podkladu, ktorá je použitá v prípade, že podlaha ani
		 * strop neboli vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Farbu pozadia môže prekryť
		 * {@linkplain Plátno#vyplň(Color) výplň plátna}, ale keď
		 * je obsah plátien prázdny ({@linkplain #vymažGrafiku() vymazaný}),
		 * je svet grafických robotov vyplnený farbou pozadia.</p>
		 * 
		 * @param nováFarba objekt určujúci novú farbu pozadia;
		 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
		 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
		 * 
		 * @see #vymaž()
		 */
		public static void farbaPozadia(Color nováFarba)
		{
			if (nováFarba instanceof Farba)
				farbaPozadia = (Farba)nováFarba;
			else
				farbaPozadia = new Farba(nováFarba);

			automatickéPrekreslenie();
		}

		/**
		 * <p>Nastaví farbu pozadia sveta podľa farby zadaného objektu.
		 * Farba pozadia je použitá v prípade, že podlaha ani strop neboli
		 * vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Farbu pozadia môže prekryť
		 * {@linkplain Plátno#vyplň(Color) výplň plátna}, ale keď
		 * je obsah plátien prázdny ({@linkplain #vymažGrafiku() vymazaný}),
		 * je svet grafických robotov vyplnený farbou pozadia.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu pozadia
		 * 
		 * @see #vymaž()
		 */
		public static void farbaPozadia(Farebnosť objekt)
		{ farbaPozadia(objekt.farba()); }

		/* *
		 * Toto je „klon“ metódy {@link #farbaPozadia(Farba)}. Farba
		 * pozadia je nastavená len v prípade, že v premennej typu {@link 
		 * Object} (zadanej ako parameter) je uložená inštancia triedy {@link 
		 * Farba Farba} alebo {@link Color Color}.
		 * /
		public static void farbaPozadia(Object nováFarba)
		{
			if (nováFarba instanceof Color)
				nováFarba = new Farba((Color)nováFarba);
			if (nováFarba instanceof Farba) farbaPozadia((Farba)nováFarba);
		}
		*/

		/**
		 * <p>Nastav farbu pozadia sveta.
		 * Ide o farbu podkladu, ktorá je použitá v prípade, že podlaha ani
		 * strop neboli vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia
		 * 
		 * @see #farbaPozadia(Color)
		 */
		public static Farba farbaPozadia(int r, int g, int b)
		{
			farbaPozadia = new Farba(r, g, b);
			automatickéPrekreslenie();
			return farbaPozadia;
		}

		/**
		 * <p>Nastav farbu a (ne)priehľadnosť pozadia sveta.
		 * Ide o farbu podkladu, ktorá je použitá v prípade, že podlaha ani
		 * strop neboli vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
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
		public static Farba farbaPozadia(int r, int g, int b, int a)
		{
			farbaPozadia = new Farba(r, g, b, a);
			automatickéPrekreslenie();
			return farbaPozadia;
		}

		/**
		 * <p>Nastav predvolenú farbu pozadia.
		 * Ide o farbu podkladu, ktorá je použitá v prípade, že podlaha ani
		 * strop neboli vyplnené žiadnou konkrétnou farbou (pozri {@link 
		 * Plátno#vymaž() vymaž}, {@link 
		 * Plátno#vyplň(Color) vyplň}).</p>
		 */
		public static void predvolenáFarbaPozadia()
		{ farbaPozadia(predvolenéPozadie); }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadia() predvolenáFarbaPozadia}.</p> */
		public static void predvolenaFarbaPozadia()
		{ farbaPozadia(predvolenéPozadie); }


		// Farba plochy (sveta)

		/**
		 * <p><a class="getter"></a> Číta farbu plochy sveta. Ide o farbu
		 * základného komponentu okna aplikácie, na ktorom je umiestnené
		 * plátno a prípadne ďalšie komponenty.</p>
		 * 
		 * @return aktuálna farba plochy sveta (objekt typu {@link Farba
		 *     Farba})
		 */
		public static Farba farbaPlochy()
		{
			Color farba = hlavnýPanel.getBackground();
			Farba farbaPlochy;
			if (farba instanceof Farba)
				farbaPlochy = (Farba)farba;
			else
				farbaPlochy = new Farba(farba);
			return farbaPlochy;
		}

		/**
		 * <p><a class="setter"></a> Nastaví farbu plochy sveta. Ide o farbu
		 * základného komponentu okna aplikácie, na ktorom je umiestnené
		 * plátno a prípadne ďalšie komponenty.</p>
		 * 
		 * <!-- TODO – overiť vzhľad ukážky -->
		 * <p><image>farba-plochy-small.png<alt/>Ukážka farieb
		 * plochy.</image>Ukážka troch farieb plochy. Zľava doprava:
		 * systémom predvolená, {@link Farebnosť#papierová papierová}
		 * a {@link Farebnosť#antracitová antracitová}.</p>
		 * 
		 * @param nováFarba objekt určujúci novú farbu plochy;
		 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
		 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
		 * 
		 * @see #vymaž()
		 */
		public static void farbaPlochy(Color nováFarba)
		{ hlavnýPanel.setBackground(nováFarba); }

		/**
		 * <p>Nastaví farbu plochy sveta podľa farby zadaného objektu. Plochou
		 * je myslený základný komponent okna aplikácie, na ktorom je umiestnené
		 * plátno a prípadne ďalšie komponenty.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu plochy
		 * 
		 * @see #farbaPlochy(Color)
		 */
		public static void farbaPlochy(Farebnosť objekt)
		{ farbaPlochy(objekt.farba()); }

		/**
		 * <p>Nastaví farbu plochy sveta. Plochou je myslený základný komponent
		 * okna aplikácie, na ktorom je umiestnené plátno a prípadne ďalšie
		 * komponenty.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba plochy
		 * 
		 * @see #farbaPlochy(Color)
		 */
		public static Farba farbaPlochy(int r, int g, int b)
		{
			Farba farbaPlochy = new Farba(r, g, b);
			farbaPlochy(farbaPlochy);
			return farbaPlochy;
		}

		/**
		 * <p>Nastaví farbu a (ne)priehľadnosť plochy sveta. Plochou je
		 * myslený základný komponent okna aplikácie, na ktorom je umiestnené
		 * plátno a prípadne ďalšie komponenty.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
		 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
		 *     farba)
		 * @return objekt typu {@link Farba Farba} – nová farba plochy
		 * 
		 * @see #farbaPlochy(Color)
		 */
		public static Farba farbaPlochy(int r, int g, int b, int a)
		{
			Farba farbaPlochy = new Farba(r, g, b, a);
			farbaPlochy(farbaPlochy);
			return farbaPlochy;
		}

		/**
		 * <p>Nastaví predvolenú farbu plochy. Ide o farbu základného komponentu
		 * okna aplikácie, na ktorom je umiestnené plátno a prípadne ďalšie
		 * komponenty. (Predvolenou farbou je obvykle šedá, ale farba sa môže
		 * líšiť v závislosti od operačného systému, jeho nastavení alebo podľa
		 * použitého dizajnu vzhľadu používateľského rozhrania – L&F.)</p>
		 */
		public static void predvolenáFarbaPlochy()
		{
			Color predvolenáFarba = UIManager.getColor("Panel.background");
			farbaPlochy(predvolenáFarba);
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPlochy() predvolenáFarbaPlochy}.</p> */
		public static void predvolenaFarbaPlochy() { predvolenáFarbaPlochy(); }


		// Farba bodu

		/**
		 * <p>Zistí farbu bodu (jedného pixela) na zadaných súradniciach.
		 * Ak sa zadané súradnice nachádzajú mimo plochy sveta, je vrátená
		 * inštancia farebnosti {@link Farebnosť#žiadna žiadna}. So získanou
		 * farbou môžeme ďalej pracovať – napríklad ju upravovať alebo
		 * zisťovať jej vlastnosti (farebné zložky…). Testovať, či má bod
		 * konkrétnu farbu, môžeme napríklad pomocou metódy
		 * {@link #farbaBodu(double, double, Color)
		 * farbaBodu(x, y, farba)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na zadanej
		 *     pozícii alebo inštancia {@link Farebnosť#žiadna žiadna}, ak sú
		 *     zadané súradnice mimo rozmerov sveta
		 */
		public static Farba farbaBodu(double x, double y)
		{
			int x0 = (int)prepočítajX(x);
			int y0 = (int)prepočítajY(y);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return žiadna;
			return new Farba(obrázokSveta1.getRGB(x0, y0));
		}

		/**
		 * <p>Zistí farbu bodu (jedného pixela) na pozícii zadaného objektu.
		 * Ak sa objekt nachádza mimo plochy sveta, je vrátená inštancia
		 * farebnosti {@link Farebnosť#žiadna žiadna} farba. So získanou farbou
		 * môžeme ďalej pracovať – napríklad ju upravovať alebo zisťovať jej
		 * vlastnosti (farebné zložky…). Testovať, či má bod konkrétnu farbu
		 * môžeme napríklad pomocou metódy
		 * {@link #farbaBodu(Poloha, Color)
		 * farbaBodu(objekt, farba)}.</p>
		 * 
		 * @param objekt objekt, na ktorého pozícii má byť zistená farba bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na pozícii
		 *     zadaného objektu alebo inštancia {@link Farebnosť#žiadna
		 *     žiadna}, ak sa súradnice zadaného objektu nachádzajú mimo
		 *     rozmerov sveta
		 */
		public static Farba farbaBodu(Poloha objekt)
		{
			int x0 = (int)prepočítajX(objekt.polohaX());
			int y0 = (int)prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return žiadna;
			return new Farba(obrázokSveta1.getRGB(x0, y0));
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje so zadanou farbou. Ak sú zadané súradnice mimo plochy
		 * sveta, je vrátená hodnota {@code valfalse}. Testovať farbu pomocou
		 * tejto metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Svet Svet}.{@code currfarbaBodu}({@code num3.0}, {@code num5.0}, {@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na zadanej
		 *     pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     sveta a farba bodu na zadaných súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(double x, double y, Color farba)
		{
			int x0 = (int)prepočítajX(x);
			int y0 = (int)prepočítajY(y);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (farba.getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach zadaného
		 * objektu zhoduje so zadanou farbou. Ak sú súradnice zadaného objektu
		 * mimo plochy sveta, je vrátená hodnota {@code valfalse}. Testovať
		 * farbu pomocou tejto metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Svet Svet}.{@code currfarbaBodu}({@code valthis}, {@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * @param objekt objekt, na ktorého pozícii chceme overiť farbu bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na pozícii
		 *     určeného objektu
		 * @return {@code valtrue} ak sa súradnice objektu nachádzajú v rámci
		 *     rozmerov sveta a farba bodu na jeho mieste sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(Poloha objekt, Color farba)
		{
			int x0 = (int)prepočítajX(objekt.polohaX());
			int y0 = (int)prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (farba.getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje s farbou zadaného objektu. Ak sú zadané súradnice mimo
		 * plochy sveta, je vrátená hodnota {@code valfalse}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param objekt objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     sveta a farba bodu na zadaných súradniciach sa zhoduje
		 *     s farbou zadaného objektu (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(double x, double y, Farebnosť objekt)
		{
			int x0 = (int)prepočítajX(x);
			int y0 = (int)prepočítajY(y);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (objekt.farba().getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje s farbou zadaného objektu. Ak sú súradnice zadaného objektu
		 * mimo plochy sveta, je vrátená hodnota {@code valfalse}.</p>
		 * 
		 * @param objekt objekt, na ktorého pozícii chceme overiť farbu bodu
		 * @param farebnosť objekt, ktorého farbu chceme porovnať s farbou
		 *     bodu na pozícii predchádzajúceho objektu
		 * @return {@code valtrue} ak sa súradnice objektu nachádzajú v rámci
		 *     rozmerov sveta a farba bodu na jeho mieste sa zhoduje s farbou
		 *     druhého zadaného objektu (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(Poloha objekt, Farebnosť farebnosť)
		{
			int x0 = (int)prepočítajX(objekt.polohaX());
			int y0 = (int)prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (farebnosť.farba().getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje s farbou zadanou prostredníctvom farebných zložiek.
		 * (Úroveň priehľadnosti je nastavená na hodnotu {@code num255},
		 * čiže na úplne nepriehľadnú farbu.)</p>
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
		 *     sveta a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(double x, double y, int r, int g, int b)
		{
			int x0 = (int)prepočítajX(x);
			int y0 = (int)prepočítajY(y);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje s farbou zadanou prostredníctvom farebných zložiek a úrovne
		 * priehľadnosti.</p>
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
		 *     sveta a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(double x, double y, int r, int g, int b, int a)
		{
			int x0 = (int)prepočítajX(x);
			int y0 = (int)prepočítajY(y);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach zadaného
		 * objektu zhoduje s farbou zadanou prostredníctvom farebných zložiek.
		 * (Úroveň priehľadnosti je nastavená na hodnotu {@code num255},
		 * čiže na úplne nepriehľadnú farbu.)</p>
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
		 *     sveta a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(Poloha objekt, int r, int g, int b)
		{
			int x0 = (int)((Plátno.šírkaPlátna / 2.0) + objekt.polohaX());
			int y0 = (int)((Plátno.výškaPlátna / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach zadaného
		 * objektu zhoduje s farbou zadanou prostredníctvom farebných zložiek
		 * a úrovne priehľadnosti.</p>
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
		 *     sveta a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaBodu(Poloha objekt, int r, int g, int b, int a)
		{
			int x0 = (int)((Plátno.šírkaPlátna / 2.0) + objekt.polohaX());
			int y0 = (int)((Plátno.výškaPlátna / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí farbu bodu (jedného pixela) na súradniciach myši. So získanou
		 * farbou môžeme ďalej pracovať – napríklad ju upravovať alebo
		 * zisťovať jej vlastnosti (farebné zložky…). Testovať, či má bod
		 * konkrétnu farbu, môžeme napríklad pomocou metódy {@link 
		 * #farbaNaMyši(Color) farbaNaMyši(farba)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy sveta, metóda by vrátila inštanciu farby {@link 
		 * Farebnosť#žiadna žiadna}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad demonštruje fungovanie metódy {@code 
		 * currfarbaNaMyši}. Zistenú farbu zobrazí v elegantnom
		 * indikátore umiestnenom v ľavom hornom rohu (<small>pozri
		 * zoznam zmien: <a href="zoznam-zmien.html">poďakovanie</a>
		 * uvedené pri verzii 1.35</small>):</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Zaplňme kresbu množstvom farebných štvorcov}

			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num1000}; ++j)
			{
				{@code typedouble} rozmer = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num20}, {@code num30});

				{@link GRobot#farba(int, int, int) farba}(({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
					({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}),
					({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num255}));

				{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
				{@link GRobot#náhodnýSmer() náhodnýSmer}();
				{@link GRobot#vyplňŠtvorec(double) vyplňŠtvorec}(rozmer);
			}

			{@link Svet Svet}.{@link Svet#kresli() kresli}();

			{@code comm// Definujme všetko potrebné (obsluhu udalostí, vlastný tvar) na to,}
			{@code comm// aby robot počas pohybu myšou nad plátnom v elegantnom kruhovom}
			{@code comm// indikátore ukazoval aktuálnu farbu nad kurzorom myši…}

			{@link GRobot#skočNa(double, double) skočNa}({@link Svet Svet}.{@link Svet#ľavýOkraj() ľavýOkraj}() + {@code num35}, {@link Svet Svet}.{@link Svet#hornýOkraj() hornýOkraj}() &#45; {@code num35});

			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#pohybMyši() pohybMyši}()
				{
					{@link GRobot#farba(Color) farba}({@link Svet Svet}.{@code currfarbaNaMyši}());
				}
			};

			{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}({@code kwdnew} {@link KreslenieTvaru KreslenieTvaru}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link KreslenieTvaru#kresli(GRobot) kresli}({@link GRobot GRobot} r)
				{
					{@link Farba Farba} mojaFarba = r.{@link GRobot#farba() farba}();
					{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
					{@link GRobot#kruh(double) kruh}({@code num30});
					{@link GRobot#farba(Color) farba}({@link Farebnosť#biela biela});
					{@link GRobot#kruh(double) kruh}({@code num25});
					{@link GRobot#farba(Color) farba}(mojaFarba);
					{@link GRobot#kruh(double) kruh}({@code num20});
				}
			});
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>farbaNaMysi.png<alt/>Zisťovanie farby pixela na pozícii
		 * kurzora myši.</image>Plátno je zaplnené farebnými štvorcami
		 * a farba pod kurzorom myši<br />je indikovaná v kruhovom indikátore
		 * v ľavom hornom rohu<br /><small>(plátno na obrázku je
		 * zmenšené)</small>.</p>
		 * 
		 * @return farba bodu (objekt typu {@link Farba Farba}) na pozícii
		 *     myši
		 */
		public static Farba farbaNaMyši()
		{
			int x0 = (int)prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return žiadna;
			return new Farba(obrázokSveta1.getRGB(x0, y0));
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši() farbaNaMyši}.</p> */
		public static Farba farbaNaMysi() { return farbaNaMyši(); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje so zadanou farbou. Testovať farbu pomocou tejto
		 * metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Svet Svet}.{@code currfarbaNaMyši}({@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy sveta, metóda by vrátila hodnotu {@code valfalse}.</p>
		 * 
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na
		 *     súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná so zadanou farbou (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaNaMyši(Color farba)
		{
			int x0 = (int)prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (farba.getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(Color) farbaNaMyši}.</p> */
		public static boolean farbaNaMysi(Color farba) { return farbaNaMyši(farba); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje so zadanou farbou. Testovať farbu pomocou tejto
		 * metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@link Svet Svet}.{@code currfarbaNaMyši}({@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy sveta, metóda by vrátila hodnotu {@code valfalse}.</p>
		 * 
		 * @param objekt objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná so zadanou farbou (musia sa zhodovať všetky
		 *     farebné zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaNaMyši(Farebnosť objekt)
		{
			int x0 = (int)prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			return (objekt.farba().getRGB() & 0x00ffffff) ==
				(obrázokSveta1.getRGB(x0, y0) & 0x00ffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(Farebnosť) farbaNaMyši}.</p> */
		public static boolean farbaNaMysi(Farebnosť objekt) { return farbaNaMyši(objekt); }

		/**
		 * <p>Zistí, či je farba bodu (jedného pixela) na súradniciach myši
		 * zhodná s farbou zadanou prostredníctvom farebných zložiek.
		 * (Úroveň priehľadnosti je nastavená na hodnotu {@code num255},
		 * čiže na úplne nepriehľadnú farbu.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo aktívneho kresliaceho plátna robota, metóda by vrátila
		 * hodnotu {@code valfalse}.</p>
		 * 
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná so zadanou farbou (musia sa zhodovať všetky tri
		 *     farebné zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaNaMyši(int r, int g, int b)
		{
			int x0 = (int)prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(int, int, int) farbaNaMyši}.</p> */
		public static boolean farbaNaMysi(int r, int g, int b) { return farbaNaMyši(r, g, b); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje s farbou zadanou prostredníctvom farebných zložiek
		 * a úrovne priehľadnosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy sveta, metóda by vrátila hodnotu {@code valfalse}.</p>
		 * 
		 * @param r červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @param g červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @param b červená zložka farby, ktorú chceme porovnať s farbou
		 *     bodu na súradniciach myši
		 * @param a úroveň priehľadnosti farby, ktorú chceme porovnať
		 *     s farbou bodu na súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná so zadanou farbou (musia sa zhodovať všetky tri
		 *     farebné zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public static boolean farbaNaMyši(int r, int g, int b, int a)
		{
			int x0 = (int)prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= Plátno.šírkaPlátna ||
				y0 < 0 || y0 >= Plátno.výškaPlátna) return false;
			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokSveta1.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(int, int, int, int) farbaNaMyši}.</p> */
		public static boolean farbaNaMysi(int r, int g, int b, int a) { return farbaNaMyši(r, g, b, a); }


		// Písmo

		/**
		 * <p>Čítaj aktuálny typ písma textov stropu. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#písmo() strop.písmo()}.</p>
		 */
		public static Písmo písmo() { return GRobot.strop.písmo(); }

		/** <p><a class="alias"></a> Alias pre {@link #písmo() písmo}.</p> */
		public static Pismo pismo() { return GRobot.strop.pismo(); }

		/**
		 * <p>Nastav nový typ písma textov stropu. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#písmo(Font)
		 * strop.písmo(Font)}. Písmo používajú metódy {@link 
		 * #vypíš(Object[]) vypíš} a {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}.</p>
		 * 
		 * @param novéPísmo objekt typu {@link Písmo} alebo {@link Font}
		 *     určujúci nový typ písma
		 */
		public static void písmo(Font novéPísmo)
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			GRobot.strop.písmo(novéPísmo);
			svet.setFont(novéPísmo);
		}

		/** <p><a class="alias"></a> Alias pre {@link #písmo(Font) písmo}.</p> */
		public static void pismo(Font novéPísmo)
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			GRobot.strop.písmo(novéPísmo);
			svet.setFont(novéPísmo);
		}

		/**
		 * <p>Nastav nový typ písma textov stropu. Má rovnaký efekt ako keby sme
		 * volali metódu {@link Plátno#písmo(String, double)
		 * strop.písmo(String, double)}.</p>
		 * 
		 * @param názov názov písma; môže byť všeobecný názov logického písma
		 *     (Dialog, DialogInput, Monospaced, Serif, SansSerif…) alebo
		 *     názov konkrétneho písma (Times New Roman, Arial…)
		 * @param veľkosť veľkosť písma v bodoch (hodnota je zaokrúhlená
		 *     na typ {@code typefloat})
		 * @return objekt typu {@link Písmo} určujúci nový typ písma
		 */
		public static Písmo písmo(String názov, double veľkosť)
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			Písmo novéPísmo = GRobot.strop.písmo(názov, veľkosť);
			svet.setFont(novéPísmo);
			return novéPísmo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #písmo(String, double) písmo}.</p> */
		public static Pismo pismo(String názov, double veľkosť)
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			Pismo novéPísmo = GRobot.strop.pismo(názov, veľkosť);
			svet.setFont(novéPísmo);
			return novéPísmo;
		}

		/**
		 * <p>Nastav predvolený typ písma textov stropu. Má rovnaký efekt ako keby
		 * sme volali metódu {@link Plátno#predvolenéPísmo()
		 * strop.predvolenéPísmo()}.</p>
		 */
		public static void predvolenéPísmo()
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			GRobot.strop.predvolenéPísmo();
			svet.setFont(GRobot.strop.písmo());
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenéPísmo() predvolenéPísmo}.</p> */
		public static void predvolenePismo()
		{
			// Inštancia písma je zároveň zálohovaná do inštancie svet, takže
			// naposledy nastavené písmo je možné kedykoľvek prečítať metódou
			// svet.getFont();
			GRobot.strop.predvolenéPísmo();
			svet.setFont(GRobot.strop.pismo());
		}


	// --- Zvuky


		// Čítanie

		/**
		 * <p>Ak sú všetky zvuky uložené v spoločnom priečinku, môžeme pre nich
		 * touto metódou nastaviť zdrojový priečinok čítania.
		 * Priečinok by sa mal nachádzať v hlavnom priečinku projektu alebo by
		 * k nemu mala viesť systémovo nezávislá relatívna cesta. Zadaním
		 * prázdneho reťazca alebo hodnoty {@code valnull} používanie
		 * priečinka zrušíme.</p>
		 * 
		 * @param priečinok názov priečinka, relatívna cesta, prípadne
		 *     prázdny reťazec alebo {@code valnull}
		 * 
		 * @see #priečinokZvukov()
		 * @see #čítajZvuky(Object[])
		 * @see #čítajZvuky(String[])
		 * @see #čítajZvuk(String)
		 * @see #čítajZvuk(String, boolean)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see #zastavZvuky(Object[])
		 * @see #hlasitosťPreZvuky(double, Object[])
		 * @see #váhaPreZvuky(double, Object[])
		 * @see #zastavZvuky(String[])
		 * @see #hlasitosťPreZvuky(double, String[])
		 * @see #váhaPreZvuky(double, String[])
		 * @see Zvuk
		 */
		public static void priečinokZvukov(String priečinok)
		{ Zvuk.priečinokZvukov = Súbor.upravLomky(priečinok, true); }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokZvukov(String) priečinokZvukov}.</p> */
		public static void priecinokZvukov(String priečinok)
		{ Zvuk.priečinokZvukov = Súbor.upravLomky(priečinok, true); }

		/**
		 * <p>Vráti reťazec s aktuálnym priečinkom, z ktorého sú zvuky čítané.
		 * Reťazec je obohatený o oddeľovací znak priečinkov {@link 
		 * File#separatorChar java.io.File.separatorChar} ({@code /} alebo
		 * {@code \} – záleží na type operačného systému), ktorý automaticky
		 * pridáva metóda {@link #priečinokZvukov(String)
		 * priečinokZvukov(priečinok)}. Rovnako všetky oddeľovacie znaky
		 * priečinkov v relatívnej ceste sú nahradené podľa typu operačného
		 * systému.</p>
		 * 
		 * @return aktuálny priečinok, z ktorého sú zvuky čítané
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #čítajZvuky(Object[])
		 * @see #čítajZvuky(String[])
		 * @see #čítajZvuk(String)
		 * @see #čítajZvuk(String, boolean)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see #zastavZvuky(Object[])
		 * @see #hlasitosťPreZvuky(double, Object[])
		 * @see #váhaPreZvuky(double, Object[])
		 * @see #zastavZvuky(String[])
		 * @see #hlasitosťPreZvuky(double, String[])
		 * @see #váhaPreZvuky(double, String[])
		 * @see Zvuk
		 * @see Zvuk#čítaj(String)
		 * @see Zvuk#čítaj(String, boolean)
		 */
		public static String priečinokZvukov()
		{ return Zvuk.priečinokZvukov; }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokZvukov() priečinokZvukov}.</p> */
		public static String priecinokZvukov()
		{ return Zvuk.priečinokZvukov; }

		/**
		 * <p>Táto metóda slúži na čítanie zadaných zvukových súborov do
		 * vnútornej pamäte sveta (napríklad pri štarte aplikácie). Svet
		 * ukladá do vnútornej pamäte každý prehrávaný zvuk (ak tam už nie
		 * je). Z nej môže byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. Táto metóda číta zvukové súbory vopred
		 * a tým môže prispieť k plynulejšej činnosti aplikácie po štarte (čas
		 * štartu sa predĺži, ale keďže súbory už nemusia byť čítané počas jej
		 * činnosti môže pracovať plynulejšie).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbory ľubovoľný počet reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #čítajZvuky(String[])
		 * @see #čítajZvuk(String)
		 * @see #čítajZvuk(String, boolean)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see Zvuk
		 * @see Zvuk#čítaj(String)
		 * @see Zvuk#čítaj(String, boolean)
		 */
		public static void čítajZvuky(Object... súbory)
		{
			GRobotException gre = null;
			for (Object súbor : súbory)
				try { Zvuk.súborNaZvuk(súbor.toString()); }
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}


		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(Object[]) čítajZvuky}.</p> */
		public static void citajZvuky(Object... súbory)
		{ čítajZvuky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(Object[]) čítajZvuky}.</p> */
		public static void prečítajZvuky(Object... súbory)
		{ čítajZvuky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(Object[]) čítajZvuky}.</p> */
		public static void precitajZvuky(Object... súbory)
		{ čítajZvuky(súbory); }

		/**
		 * <p>Táto metóda slúži na čítanie zadaných zvukových súborov do
		 * vnútornej pamäte sveta (napríklad pri štarte aplikácie). Svet
		 * ukladá do vnútornej pamäte každý prehrávaný zvuk (ak tam už nie
		 * je). Z nej môže byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. Táto metóda číta zvukové súbory vopred
		 * a tým môže prispieť k plynulejšej činnosti aplikácie po štarte (čas
		 * štartu sa predĺži, ale keďže súbory už nemusia byť čítané počas jej
		 * činnosti, pracuje plynulejšie).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbory pole reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #čítajZvuky(Object[])
		 * @see #čítajZvuk(String)
		 * @see #čítajZvuk(String, boolean)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see Zvuk
		 * @see Zvuk#čítaj(String)
		 * @see Zvuk#čítaj(String, boolean)
		 */
		public static void čítajZvuky(String[] súbory)
		{
			GRobotException gre = null;
			for (String súbor : súbory)
				try { Zvuk.súborNaZvuk(súbor); }
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(String[]) čítajZvuky}.</p> */
		public static void citajZvuky(String[] súbory)
		{ čítajZvuky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(String[]) čítajZvuky}.</p> */
		public static void prečítajZvuky(String[] súbory)
		{ čítajZvuky(súbory); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuky(String[]) čítajZvuky}.</p> */
		public static void precitajZvuky(String[] súbory)
		{ čítajZvuky(súbory); }

		/**
		 * <p>Prečíta do vnútornej pamäte sveta zadaný zvuk zo súboru a vráti
		 * ho v objekte typu {@link Zvuk Zvuk}. Zvuk nie je prehraný. Podobnú
		 * úlohu plní metóda {@link #čítajZvuky(Object[])
		 * Svet.čítajZvuky(Object... súbory)} (pozri pre viac informácií),
		 * ale pomocou nej nie je možné získať objekt typu {@link Zvuk Zvuk}
		 * na prípadné ďalšie spracovanie. Zvuk môže byť v prípade potreby
		 * (napríklad ak sa obsah súboru na disku zmenil) z vnútornej pamäte
		 * odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.</p>
		 * 
		 * <p>Táto metóda funguje tak, že podľa potreby vytvára nové unikátne
		 * inštancie zvuku identifikovaného názvom súboru. Nová inštancia
		 * vznikne vždy, ak sa vo vnútornej pamäti sveta nenájde taká
		 * inštancia určeného zvuku, ktorá práve nie je prehrávaná (to
		 * znamená, že metóda musí nájsť zvuk, ktorý je ticho). Takto fungujú
		 * všetky metódy pracujúce so zvukom. Ak by sa toto nedialo, tak by
		 * vznikalo nežiaduce správanie – vždy pri pokuse prehrať ten istý
		 * zvuk počas jeho prehrávania, by sa tento reštartoval. (Nebolo by
		 * možné prehrať sériu rovnakých zvukov, ktoré sa časovo prekrývajú.
		 * To by pôsobilo neprirodzene – každé reštartovanie zvuku by bolo
		 * počuteľné tak, ako keby sa zo zvuku zrazu časť „odsekla“.)</p>
		 * 
		 * @param súbor názov súboru so zvukom
		 * @return zvuk v objekte typu {@link Zvuk Zvuk}
		 * 
		 * @throws GRobotException ak súbor so zvukom nebol nájdený
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #čítajZvuky(Object[])
		 * @see #čítajZvuky(String[])
		 * @see #čítajZvuk(String, boolean)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see Zvuk
		 * @see Zvuk#čítaj(String)
		 * @see Zvuk#čítaj(String, boolean)
		 */
		public static Zvuk čítajZvuk(String súbor)
		{ return Zvuk.súborNaZvuk(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String) čítajZvuk}.</p> */
		public static Zvuk citajZvuk(String súbor)
		{ return Zvuk.súborNaZvuk(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String) čítajZvuk}.</p> */
		public static Zvuk prečítajZvuk(String súbor)
		{ return Zvuk.súborNaZvuk(súbor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String) čítajZvuk}.</p> */
		public static Zvuk precitajZvuk(String súbor)
		{ return Zvuk.súborNaZvuk(súbor); }

		/**
		 * <p>Prečíta do vnútornej pamäte sveta zadaný zvuk zo súboru a vráti
		 * ho v objekte typu {@link Zvuk Zvuk}. Zvuk nie je prehraný. Ak je
		 * parameter {@code unikátny} rovný {@code valfalse}, tak táto metóda
		 * funguje rovnako ako metóda {@link #čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * <p>V prípade, že je parameter {@code unikátny} rovný {@code 
		 * valtrue}, tak táto metóda <b>vždy</b> vráti unikátny objekt zvuku
		 * aj v prípade, že jestvuje aspoň jedna kópia tohto zvuku, ktorá sa
		 * práve neprehráva (t. j. je ticho).</p>
		 * 
		 * <p>Všetky kópie zvuku môžu byť v prípade potreby (napríklad ak sa
		 * obsah zvukového súboru na disku zmenil) z vnútornej pamäte
		 * odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.</p>
		 * 
		 * @param súbor názov súboru so zvukom
		 * @param unikátny určuje, či objekt vrátený touto metódou musí
		 *     byť unikátny
		 * @return zvuk v objekte typu {@link Zvuk Zvuk}
		 * 
		 * @throws GRobotException ak súbor so zvukom nebol nájdený
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #čítajZvuky(Object[])
		 * @see #čítajZvuky(String[])
		 * @see #čítajZvuk(String)
		 * @see #zvuk(String)
		 * @see #zvukNaPozadí(String)
		 * @see Zvuk
		 * @see Zvuk#čítaj(String)
		 * @see Zvuk#čítaj(String, boolean)
		 */
		public static Zvuk čítajZvuk(String súbor, boolean unikátny)
		{ return Zvuk.súborNaZvuk(súbor, unikátny); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String, boolean) čítajZvuk}.</p> */
		public static Zvuk citajZvuk(String súbor, boolean unikátny)
		{ return Zvuk.súborNaZvuk(súbor, unikátny); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String, boolean) čítajZvuk}.</p> */
		public static Zvuk prečítajZvuk(String súbor, boolean unikátny)
		{ return Zvuk.súborNaZvuk(súbor, unikátny); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZvuk(String, boolean) čítajZvuk}.</p> */
		public static Zvuk precitajZvuk(String súbor, boolean unikátny)
		{ return Zvuk.súborNaZvuk(súbor, unikátny); }

			// ‼TODO‼ – sprístupniť generovanie zvuku do tejto inštancie
			// a umožnenie jej uloženia


		// Prehrávanie

		/**
		 * <p>Prehrá zvukový súbor (formát {@code .wav}, {@code .au} alebo
		 * {@code .mp3}).</p>
		 * 
		 * <p>Napríklad:</p>
		 * 
		 * <pre CLASS="example">
			{@link Svet Svet}.{@code currzvuk}({@code srg"zvuk.wav"});
			</pre>
		 * 
		 * <p class="image"><a href="resources/zvuk.wav"
		 * target="_blank"><image>zvuk-small.png<alt/>Grafické znázornenie
		 * obsahu zvukového súboru „zvuk.wav“.</image>Zvuk
		 * „zvuk.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/zvuk.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p>
		 * 
		 * <p>Zvuk prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaného zvuku. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbor názov zvukového súboru, ktorý má byť prehraný
		 * 
		 * @throws GRobotException ak súbor so zvukom nebol nájdený
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #zvukNaPozadí(String)
		 * @see Zvuk
		 */
		public static void zvuk(String súbor)
		{ Zvuk.súborNaZvuk(súbor).prehraj(); }

		/**
		 * <p>Začne alebo ukončí prehrávanie zvuku zo súboru na pozadí (formát
		 * {@code .wav}, {@code .au} alebo {@code .mp3}). Ak chceme
		 * prehrávanie zvuku zastaviť, zadáme do argumentu metódy
		 * {@code valnull}.</p>
		 * 
		 * <p>Zvuk prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaného zvuku. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbor názov zvukového súboru, ktorý má byť prehrávaný,
		 *     alebo {@code valnull} na zastavenie prehrávania
		 * 
		 * @throws GRobotException ak súbor so zvukom nebol nájdený
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #zvuk(String)
		 * @see Zvuk
		 */
		public static void zvukNaPozadí(String súbor)
		{
			if (null == súbor)
			{
				if (null != Zvuk.zvukNaPozadí)
				{
					Zvuk.zvukNaPozadí.zastav();
					Zvuk.zvukNaPozadí = null;
				}
			}
			else
			{
				if (null != Zvuk.zvukNaPozadí)
					Zvuk.zvukNaPozadí.zastav();
				Zvuk.zvukNaPozadí = Zvuk.súborNaZvuk(súbor);
				Zvuk.zvukNaPozadí.prehrávaťDookola();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zvukNaPozadí(String) zvukNaPozadí}.</p> */
		public static void zvukNaPozadi(String súbor) { zvukNaPozadí(súbor); }

		/**
		 * <p>Zistí, či je aktívne prehrávanie zvuku na pozadí, ktoré bolo
		 * spustené metódou {@link #zvukNaPozadí(String) zvukNaPozadí}.</p>
		 * 
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public static boolean hráZvukNaPozadí()
		{ return null != Zvuk.zvukNaPozadí; }

		/** <p><a class="alias"></a> Alias pre {@link #hráZvukNaPozadí() hráZvukNaPozadí}.</p> */
		public static boolean hraZvukNaPozadi()
		{ return null != Zvuk.zvukNaPozadí; }


		// Hromadná úprava vlastností

		/**
		 * <p>Zastaví všetky uvedené zvuky. Zoznam je treba uviesť v tvare
		 * objektov, ktorých prevody na textové reťazce budú označovať
		 * názvy súborov (prípadne zdrojov), z ktorých zvuky boli alebo majú
		 * byť prečítané. (Najlepšie použiť priamo reťazce.)</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbory ľubovoľný počet reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #zastavZvuky(String[])
		 * @see Zvuk
		 */
		public static void zastavZvuky(Object... súbory)
		{
			GRobotException gre = null;
			for (Object súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor.toString());
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.zastav();
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/**
		 * <p>Nastaví hromadne hlasitosť pre všetky uvedené zvuky. Zoznam je
		 * treba uviesť v tvare objektov, ktorých prevody na textové reťazce
		 * budú označovať názvy súborov (prípadne zdrojov), z ktorých zvuky
		 * boli alebo majú byť prečítané. (Najlepšie použiť priamo reťazce.)</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param miera miera hlasitosti v percentách – 0 % zodpovedá hodnote
		 *     {@code num0.0}, 100 % hodnote {@code num1.0}
		 * @param súbory ľubovoľný počet reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Zvuk#hlasitosť(double)
		 * @see #priečinokZvukov(String)
		 * @see #hlasitosťPreZvuky(double, String[])
		 * @see Zvuk
		 */
		public static void hlasitosťPreZvuky(double miera, Object... súbory)
		{
			GRobotException gre = null;
			for (Object súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor.toString());
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.hlasitosť(miera);
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #hlasitosťPreZvuky(double, Object[]) hlasitosťPreZvuky}</p> */
		public static void hlasitostPreZvuky(double miera, Object... súbory)
		{ hlasitosťPreZvuky(miera, súbory); }

		/**
		 * <p>Nastaví hromadne stereováhu pre všetky uvedené zvuky. Zoznam je
		 * treba uviesť v tvare objektov, ktorých prevody na textové reťazce
		 * budú označovať názvy súborov (prípadne zdrojov), z ktorých zvuky
		 * boli alebo majú byť prečítané. (Najlepšie je použiť priamo reťazce.)</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param miera miera stereovyváženia – reálne číslo od
		 *     {@code -}{@code num1.0} (ľavý kanál) po
		 *     {@code +}{@code num1.0} (pravý kanál)
		 * @param súbory ľubovoľný počet reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Zvuk#váha(double)
		 * @see #priečinokZvukov(String)
		 * @see #váhaPreZvuky(double, String[])
		 * @see Zvuk
		 */
		public static void váhaPreZvuky(double miera, Object... súbory)
		{
			GRobotException gre = null;
			for (Object súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor.toString());
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.váha(miera);
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #váhaPreZvuky(double, Object[]) váhaPreZvuky}</p> */
		public static void vahaPreZvuky(double miera, Object... súbory)
		{ váhaPreZvuky(miera, súbory); }

		/**
		 * <p>Zastaví všetky uvedené zvuky. Zoznam je tvorený poľom textových
		 * reťazcov označujúcich súbory, z ktorých boli zvuky prečítané.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param súbory pole reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see #priečinokZvukov(String)
		 * @see #zastavZvuky(Object[])
		 * @see Zvuk
		 */
		public static void zastavZvuky(String[] súbory)
		{
			GRobotException gre = null;
			for (String súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor);
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.zastav();
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/**
		 * <p>Nastaví hromadne hlasitosť pre všetky uvedené zvuky. Zoznam je
		 * tvorený poľom textových reťazcov označujúcich súbory, z ktorých
		 * boli zvuky prečítané.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param miera miera hlasitosti v percentách – 0 % zodpovedá hodnote
		 *     {@code num0.0}, 100 % hodnote {@code num1.0}
		 * @param súbory pole reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Zvuk#hlasitosť(double)
		 * @see #priečinokZvukov(String)
		 * @see #hlasitosťPreZvuky(double, Object[])
		 * @see Zvuk
		 */
		public static void hlasitosťPreZvuky(double miera, String[] súbory)
		{
			GRobotException gre = null;
			for (String súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor);
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.hlasitosť(miera);
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #hlasitosťPreZvuky(double, String[]) hlasitosťPreZvuky}</p> */
		public static void hlasitostPreZvuky(double miera, String[] súbory)
		{ hlasitosťPreZvuky(miera, súbory); }

		/**
		 * <p>Nastaví hromadne stereováhu pre všetky uvedené zvuky. Zoznam je
		 * tvorený poľom textových reťazcov označujúcich súbory, z ktorých
		 * boli zvuky prečítané.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto metóda zároveň spôsobí prečítanie
		 * všetkých dotknutých zvukov do vnútornej pamäte (ak tam už nie sú).
		 * Z nej môžu byť v prípade potreby (napríklad ak sa obsah súboru
		 * na disku zmenil) odstránené metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}.
		 * (Táto informácia je platná pre všetky metódy pracujúce s obrázkami
		 * alebo zvukmi, ktoré prijímajú názov súboru ako parameter.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * a všetky také, ktoré pracujú so zvukmi identifikovanými podľa
		 * názvu súboru tak, že podľa potreby vytvára(jú) nové unikátne
		 * inštancie identifikovaných zvukov. Podrobnejšie vysvetlenie je
		 * v opise metódy {@link Svet#čítajZvuk(String)
		 * čítajZvuk(súbor)}.</p>
		 * 
		 * @param miera miera stereovyváženia – reálne číslo od
		 *     {@code -}{@code num1.0} (ľavý kanál) po
		 *     {@code +}{@code num1.0} (pravý kanál)
		 * @param súbory pole reťazcov označujúcich súbory
		 * 
		 * @throws GRobotException ak niektorý súbor nebol nájdený;
		 *     spracovanie sa v okamihu vzniku výnimky neskončí a vrhnutá je
		 *     len posledná vzniknutá výnimka, to znamená, že všetky
		 *     predchádzajúce výnimky sú ignorované
		 * 
		 * @see Zvuk#váha(double)
		 * @see #priečinokZvukov(String)
		 * @see #váhaPreZvuky(double, Object[])
		 * @see Zvuk
		 */
		public static void váhaPreZvuky(double miera, String[] súbory)
		{
			GRobotException gre = null;
			for (String súbor : súbory)
				try
				{
					Zvuk.ZoznamZvukov zoznamZvukov =
						Zvuk.zoznamZvukovSúboru(súbor);
					for (Zvuk zvuk : zoznamZvukov)
						zvuk.váha(miera);
				}
				catch (GRobotException e) { gre = e; }
			if (null != gre) throw gre;
		}

		/** <p><a class="alias"></a> Alias pre {@link #váhaPreZvuky(double, String[]) váhaPreZvuky}</p> */
		public static void vahaPreZvuky(double miera, String[] súbory)
		{ váhaPreZvuky(miera, súbory); }


		// Pípnutie

		/**
		 * <p>Aplikácia vydá štandardný zvuk operačného systému. (To je
		 * využiteľné napríklad pri jednoduchom overení, či sa podarilo
		 * spustiť nejakú časť programu…)</p>
		 */
		public static void pípni() { Toolkit.getDefaultToolkit().beep(); }

		/** <p><a class="alias"></a> Alias pre {@link #pípni() pípni}.</p> */
		public static void pipni() { Toolkit.getDefaultToolkit().beep(); }

		/**
		 * <p>Aplikácia vydá štandardný zvuk operačného systému Windows podľa
		 * vloženého pomenovania. Operačný systém Windows má definovaných
		 * viacero systémových zvukov, ktoré sú dostupné prostredníctvom
		 * tejto verzie metódy. Ak je metóda spustená na inom operačnom
		 * systéme alebo nastane iné zlyhanie, tak je prehraný rovnaký
		 * zvukový signál ako pri metóde {@link #pípni() pípni()} (bez
		 * parametra).</p>
		 * 
		 * @param názov názov zvuku; tu sú niektoré názvy zvukov, ktoré
		 *     sú podporované OS Windows: {@code srg"asterisk"},
		 *     {@code srg"close"}, {@code srg"default"},
		 *     {@code srg"exclamation"}, {@code srg"exit"},
		 *     {@code srg"hand"}, {@code srg"maximize"},
		 *     {@code srg"menuCommand"}, {@code srg"menuPopup"},
		 *     {@code srg"minimize"}, {@code srg"open"},
		 *     {@code srg"question"}, {@code srg"restoreDown"},
		 *     {@code srg"restoreUp"} alebo {@code srg"start"}.
		 */
		public static void pípni(String názov)
		{
			Runnable systémovýZvuk = (Runnable)Toolkit.getDefaultToolkit().
				getDesktopProperty("win.sound." + názov.trim().toLowerCase());
			if (null != systémovýZvuk) systémovýZvuk.run();
			else Toolkit.getDefaultToolkit().beep();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pípni(String) pípni}.</p> */
		public static void pipni(String názov) { pípni(názov); }


		// Generovanie tónov

		/**
		 * <p>Vypočíta harmonickú frekvenciu tónu na základe zadaného poradového
		 * čísla noty (odporúčané sú hodnoty 1 až 12) a oktávy (odporúčané sú
		 * hodnoty −1 až 9).</p>
		 * 
		 * <p style="text-align: center;">Poradové čísla nôt zodpovedajú
		 * nasledujúcim tónom:</p>
		 * 
		 * <table class="centered shadedTable">
		 * <tr><td>1</td><td>C</td>
		 * <td rowspan="3" style="background: none; border: none inherit;"> </td>
		 * <td>4</td><td>E♭/D♯</td>
		 * <td rowspan="3" style="background: none; border: none inherit;"> </td>
		 * <td>7</td><td>F♯/G♭</td>
		 * <td rowspan="3" style="background: none; border: none inherit;"> </td>
		 * <td>10</td><td>A</td></tr>
		 * <tr><td>2</td><td>C♯/D♭</td><td>5</td><td>E</td>
		 * <td>8</td><td>G</td><td>11</td><td>B♭/H♭/A♯</td></tr>
		 * <tr><td>3</td><td>D</td><td>6</td><td>F</td>
		 * <td>9</td><td>A♭/G♯</td><td>12</td><td>B/H</td></tr>
		 * </table>
		 * 
		 * <p>Táto metóda vypočíta frekvenciu aj v prípade, že zadané hodnoty
		 * tónu a oktávy sú mimo odporúčaných rozsahov, ale v prípade výrazného
		 * prekročenia rozsahu nemusí byť výsledná frekvencia počuteľná.
		 * 
		 * @param nota číslo noty v rámci oktávy (1 až 12 – pozri tabuľku vyššie)
		 * @param otáva číslo oktávy (−1 až 9, prípadne 10)
		 * @return harmonická frekvencia tónu vypočítaná z čísla noty a oktávy
		 * 
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static double frekvenciaNoty(int nota, int oktáva)
		{
			return Math.pow(2.0, (oktáva * 12.0 + nota - 58.0) / 12.0) * 440.0;
		}


		/**
		 * <p>Spustí prehrávanie tónu tvoreného jedinou harmonickou frekvenciou.
		 * Hlasitosť tónu je automaticky nastavená na 80 % ({@code num0.8}).
		 * Prehrávanie tónu zastaví volanie metódy {@link #zastavTón() zastavTón}.
		 * Spustením tejto metódy sa zruší prípadný plán prehrávaných tónov
		 * vytvorený volaním metódy {@link #hrajTón(double, double, double)
		 * hrajTón(frekvencia, hlasitosť, trvanie)}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci jednoduchý príklad vyrobí zvukový hvizd v rozmedzí
		 * frekvencií od 50 Hz do zhruba 15 kHz. Ukážka hvízdnutia je pod
		 * príkladom.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdfor} ({@code typedouble} f = {@code num50.0}; f &lt;= {@code num15_000.0}; f += {@code num150.0})
			{
				{@link Svet Svet}.{@link Svet#hrajTón(double) hrajTón}(f);
				{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.010});
			}
			{@link Svet Svet}.{@link Svet#zastavTón() zastavTón}();
			</pre>
		 * 
		 * <p><b>Ukážka hvízdnutia:</b></p>
		 * 
		 * <p class="image"><a href="resources/hvizd.wav"
		 * target="_blank"><image>hvizd-small.png<alt/>Grafické znázornenie
		 * obsahu zvukového súboru „hvizd.wav“.</image>Zvuk
		 * „hvizd.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/hvizd.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p>
		 * 
		 * @param frekvencia harmonická frekvencia tónu v hertzoch
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static void hrajTón(double frekvencia)
		{
			if (null == Zvuk.kanál) Zvuk.kanál = new BeepChannel();
			Zvuk.kanál.setFrequency(frekvencia);
			Zvuk.kanál.setVolume(0.8);
			Zvuk.kanál.clearSchedule();
			Zvuk.kanál.setDelay(-1);
			Zvuk.kanál.resume();
		}

		/** <p><a class="alias"></a> Alias pre {@link #hrajTón(double) hrajTón}.</p> */
		public static void hrajTon(double frekvencia)
		{ hrajTón(frekvencia); }


		/**
		 * <p>Spustí prehrávanie tónu tvoreného jedinou harmonickou frekvenciou
		 * so zadanou hlasitosťou (v rozsahu od {@code num0.0} do {@code num1.0}).
		 * Prehrávanie tónu zastaví volanie metódy {@link #zastavTón() zastavTón}.
		 * Spustením tejto metódy sa zruší prípadný plán prehrávaných tónov
		 * vytvorený volaním metódy {@link #hrajTón(double, double, double)
		 * hrajTón(frekvencia, hlasitosť, trvanie)}.</p>
		 * 
		 * @param frekvencia harmonická frekvencia tónu v hertzoch
		 * @param hlasitosť hlasitosť prehrávaného tónu
		 *     ({@code num0.0} – {@code num1.0})
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static void hrajTón(double frekvencia, double hlasitosť)
		{
			if (null == Zvuk.kanál) Zvuk.kanál = new BeepChannel();
			Zvuk.kanál.setFrequency(frekvencia);
			Zvuk.kanál.setVolume(hlasitosť);
			Zvuk.kanál.clearSchedule();
			Zvuk.kanál.setDelay(-1);
			Zvuk.kanál.resume();

			/* Nejaké milé „náhodniny“:
				for (int i = 16; i >= 0; --i)
				{
					Svet.hrajTón(880.0, i * 0.05);
					Svet.čakaj(0.010);

					Svet.hrajTón(770.0, i * 0.05);
					Svet.čakaj(0.010);

					Svet.hrajTón(990.0, i * 0.05);
					Svet.čakaj(0.010);
				}
				Svet.zastavTón();

				for (int i = 0; i < 64; ++i)
				{
					Svet.hrajTón(440.0 + i * 15);
					Svet.čakaj(0.005);

					Svet.hrajTón(880.0 - i * 15);
					Svet.čakaj(0.005);

					Svet.hrajTón(1110.0 + i * 25);
					Svet.čakaj(0.005);
				}
				Svet.zastavTón();
			*/
		}

		/** <p><a class="alias"></a> Alias pre {@link #hrajTón(double, double) hrajTón}.</p> */
		public static void hrajTon(double frekvencia, double hlasitosť)
		{ hrajTón(frekvencia, hlasitosť); }


		/**
		 * <p>Spustí prehrávanie tónu tvoreného jedinou harmonickou frekvenciou
		 * so zadanou hlasitosťou (v rozsahu od {@code num0.0} do {@code num1.0})
		 * a s dĺžkou trvania prehrávania v sekundách. To znamená, že prehrávanie
		 * tónu sa automaticky zastaví po uplynutí zadaného časového intervalu.
		 * Prehrávanie tónu tiež môže byť predčasne zastavené volaním metódy
		 * {@link #zastavTón() zastavTón}. Viacnásobné volanie tejto metódy
		 * vytvorí časový plán prehrávania tónov so zadanými frekvenciami,
		 * hlasitosťami a trvaním. Tento plán však rušia volania všetkých
		 * ostatných metód v kategórii prehrávania tónov:
		 * {@link #hrajTón(double) hrajTón(frekvencia)},
		 * {@link #hrajTón(double, double) hrajTón(frekvencia, hlasitosť)}
		 * a {@link #zastavTón() zastavTón()}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci príklad naplánuje a prehrá zvuk, ktorého ukážka
		 * je nižšie.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdfor} ({@code typedouble} n = {@code num1.0}; n &gt;= {@code num0.0}; n &#45;= {@code num0.05})
				{@link Svet Svet}.{@code currhrajTón}({@code num100.0} + n * {@code num25.0}, n, {@code num0.025});
			</pre>
		 * 
		 * <p><b>Ukážka výsledku:</b></p>
		 * 
		 * <p class="image"><a href="resources/brum.wav"
		 * target="_blank"><image>brum-small.png<alt/>Grafické znázornenie
		 * obsahu zvukového súboru „brum.wav“.</image>Zvuk
		 * „brum.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/brum.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p>
		 * 
		 * @param frekvencia harmonická frekvencia tónu v hertzoch
		 * @param hlasitosť hlasitosť prehrávaného tónu
		 *     ({@code num0.0} – {@code num1.0})
		 * @param trvanie trvanie prehrávania tónu v sekundách
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static void hrajTón(double frekvencia, double hlasitosť,
			double trvanie)
		{
			if (null == Zvuk.kanál)
				Zvuk.kanál = new BeepChannel();

			Zvuk.kanál.addSchedule(
				(int)(trvanie * 1000.0),
				frekvencia, hlasitosť);
		}

		/** <p><a class="alias"></a> Alias pre {@link #hrajTón(double, double, double) hrajTón}.</p> */
		public static void hrajTon(double frekvencia, double hlasitosť,
			double trvanie) { hrajTón(frekvencia, hlasitosť, trvanie); }


		/**
		 * <p>Okamžite zastaví prehrávanie generovaného harmonického signálu
		 * a vyčistí vnútorný zoznam tónov naplánovaných na prehranie.</p>
		 * 
		 * <p>Príklad použitia tejto metódy nájdete v opise metódy
		 * {@link #hrajTón(double) hrajTón(frekvencia)}.</p>
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static void zastavTón()
		{
			if (null != Zvuk.kanál)
			{
				Zvuk.kanál.clearSchedule();
				Zvuk.kanál.setDelay(-1);
				Zvuk.kanál.pause();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zastavTón() zastavTón}.</p> */
		public static void zastavTon() { zastavTón(); }


		/**
		 * <p>Umožní začať záznam všetkých generovaných tónov, ktorý sa po
		 * zavolaní párovej metódy {@link #zavriSúborNaUloženieTónu()
		 * zavriSúborNaUloženieTónu} uloží do súboru s požadovaným názvom.
		 * Podporované formáty súborov (určené prislúchajúcou príponou) sú:
		 * AIFF (<code>.aiff</code>, <code>.aif</code>), AU (<code>.au</code>),
		 * SND (<code>.snd</code>) a WAVE (<code>.wav</code>). Ak zadaný súbor
		 * jestvuje, tak vznikne výnimka (<code>soundAlreadyExists</code> –
		 * pozri opis triedy {@link GRobotException GRobotException}).
		 * Príklad použitia nájdete v opise metódy
		 * {@link #otvorSúborNaUloženieTónu(String, boolean)
		 * #otvorSúborNaUloženieTónu(názov, prepísať)}.</p>
		 * 
		 * @param názov názov súboru na uloženie generovaného tónu
		 * @return ak bolo všetko vykonané bezchybne, tak je návratová hodnota
		 *     {@code valtrue}
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static boolean otvorSúborNaUloženieTónu(String názov)
		{
			if (new File(názov).exists())
				throw new GRobotException("Zvuk „" + názov +
					"“ už jestvuje.", "soundAlreadyExists", názov);

			if (null == Zvuk.kanál) Zvuk.kanál = new BeepChannel();
			return Zvuk.kanál.openAudioCapture(názov);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorSúborNaUloženieTónu(String) otvorSúborNaUloženieTónu}.</p> */
		public static boolean otvorSuborNaUlozenieTonu(String názov)
		{ return otvorSúborNaUloženieTónu(názov); }

		/**
		 * <p>Umožní začať záznam všetkých generovaných tónov, ktorý sa po
		 * zavolaní párovej metódy {@link #zavriSúborNaUloženieTónu()
		 * zavriSúborNaUloženieTónu} uloží do súboru s požadovaným názvom.
		 * Podporované formáty súborov (určené prislúchajúcou príponou) sú:
		 * AIFF (<code>.aiff</code>, <code>.aif</code>), AU (<code>.au</code>),
		 * SND (<code>.snd</code>) a WAVE (<code>.wav</code>). Parameter
		 * prepísať určuje, či má byť prípadný jestvujúci súbor prepísaný
		 * alebo nie. (Pričom v prípade, že jestvuje, a nemá byť prepísaný,
		 * vznikne výnimka – <code>soundAlreadyExists</code>, pozri opis
		 * triedy {@link GRobotException GRobotException}.)</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci príklad vygeneruje nízky, mierne vibrujúci tón
		 * „gongu“ (v podsate iba zvuk s plynule klesajúcou hlasitosťou)
		 * a uloží ho do súboru <code>gong.wav</code>. (Ukážka zvuku je
		 * nižšie.)</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Návratové hodnoty metód otvorSúborNaUloženieTónu}
			{@code comm// a zavriSúborNaUloženieTónu by sme mohli aj ignorovať.}
			{@code comm// Celý príklad by sa vykonal aj v prípade ich zlyhania,}
			{@code comm// len by sa nič neuložilo…}
			{@code kwdif} ({@link Svet Svet}.{@code currotvorSúborNaUloženieTónu}({@code srg"gong.wav"}, {@code valtrue}))
			{
				{@code kwdfor} ({@code typeint} i = {@code num32}; i &gt;= {@code num0}; &#45;&#45;i)
				{
					{@link Svet Svet}.{@link Svet#hrajTón(double, double) hrajTón}({@code num212.0} + {@code num16} * (i % {@code num2}), i * {@code num0.025});
					{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.020});
				}

				{@link Svet Svet}.{@link Svet#zastavTón() zastavTón}();
				{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.200});

				{@code kwdif} (!{@link Svet Svet}.{@link Svet#zavriSúborNaUloženieTónu() zavriSúborNaUloženieTónu}())
					{@link System System}.err.{@link java.io.PrintStream#println(String) println}({@code srg"Súbor zvuku sa nepodarilo zapísať."});
			}
			{@code kwdelse} {@link System System}.err.{@link java.io.PrintStream#println(String) println}({@code srg"Súbor na zápis zvuku sa nepodarilo otvoriť."});

			{@code comm// Ak by sme nevykonali nasledujúci príkaz, inštancia sveta by}
			{@code comm// zostala stále otvorená (bez okna umožňujúceho jej zavretie)…}
			{@link Svet Svet}.{@link Svet#koniec() koniec}();
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p class="image"><a href="resources/gong.wav"
		 * target="_blank"><image>gong-small.png<alt/>Grafické znázornenie
		 * obsahu zvukového súboru „gong.wav“.</image>Zvuk
		 * „gong.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/gong.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p>
		 * 
		 * @param názov názov súboru na uloženie generovaného tónu
		 * @param prepísať určuje, či sa má prípadný jestvujúci súbor prepísať
		 * @return ak bolo všetko vykonané bezchybne, tak je návratová hodnota
		 *     {@code valtrue}
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #zavriSúborNaUloženieTónu()
		 * @see #generátorTónov()
		 */
		public static boolean otvorSúborNaUloženieTónu(String názov,
			boolean prepísať)
		{
			if (!prepísať && new File(názov).exists())
				throw new GRobotException("Zvuk „" + názov +
					"“ už jestvuje.", "soundAlreadyExists", názov);

			if (null == Zvuk.kanál) Zvuk.kanál = new BeepChannel();
			return Zvuk.kanál.openAudioCapture(názov);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorSúborNaUloženieTónu(String, boolean) otvorSúborNaUloženieTónu}.</p> */
		public static boolean otvorSuborNaUlozenieTonu(String názov,
			boolean prepísať)
		{ return otvorSúborNaUloženieTónu(názov, prepísať); }

		/**
		 * <p>Ukončí záznam generovaných tónov, ktorý bol začatý volaním
		 * niektorej verzie metódy {@link #otvorSúborNaUloženieTónu(String,
		 * boolean) #otvorSúborNaUloženieTónu}.</p>
		 * 
		 * @return ak bolo všetko vykonané bezchybne, tak je návratová hodnota
		 *     {@code valtrue}
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #generátorTónov()
		 */
		public static boolean zavriSúborNaUloženieTónu()
		{
			if (null == Zvuk.kanál) return true;
			return Zvuk.kanál.closeAudioCapture();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zavriSúborNaUloženieTónu() zavriSúborNaUloženieTónu}.</p> */
		public static boolean zavriSuborNaUlozenieTonu()
		{ return zavriSúborNaUloženieTónu(); }


		/**
		 * <p>Táto metóda sprostredkúva prístup k vnútornej inštancii
		 * generátora tónov (<code>BeepChannel</code>). Vlastnosti tohto
		 * generátora nie sú opísané v tejto dokumentácii, ale dajú sa
		 * naštudovať v angličtine z komentárov uvedených priamo v triedach
		 * <code>BeepChannel</code> a <code>Channel</code>. Aspoň jednou
		 * z nich sa zaoberá príklad uvedený nižšie.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Jednou zo základných vlastností generátora je úprava hladkosti
		 * prechodov medzi generovanými frekvenciami tónov (smoothness).</p>
		 * 
		 * <p>Tento príklad ukazuje rozdiel medzi dvomi rôznymi hodnotami
		 * hladkosti. Kód používa primitívny generátor pseudonáhodných čísiel,
		 * ktorý na vygenerovanie postupnosti frekvencií, ktoré majú byť
		 * prehraté používa jednoduché prepočty s prvočíslami. Aj pri zbežnom
		 * pohľade na kód by malo byť zjavné, že v obidvoch prípadoch budú
		 * „vygenerované“ (vypočítané) a prehrané rovnaké pseudonáhodné
		 * sekvencie frekvencií. Rozdiel je len v hladkosti použitej pri
		 * generovaní.</p>
		 * 
		 * <p>Prvé prehrávanie sekvencie je vykonané s hladkosťou 1000 (čo
		 * je predvolená hodnota) a druhé s hladkosťou 1 (čo je približne
		 * najmenšia dovolená hodnota hladkosti). Rozdiel si môžete vypočuť
		 * v ukážkach obidvoch prípadov, ktoré sú zverejnené nižšie.</p>
		 * 
		 * <pre CLASS="example">
			{@code typeint} n, n1, n2, n3;
			{@link Svet Svet}.{@code currgenerátorTónov}().setSmoothness({@code num1000.0});

			n1 = {@code num3}; n2 = {@code num5}; n3 = {@code num7};
			{@code kwdfor} (n = {@code num1}; n &lt;= {@code num100}; ++n)
			{
				{@link Svet Svet}.{@link Svet#hrajTón(double) hrajTón}((n1 + n2 + n3) * {@code num100});
				{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.020});

				n1 += {@code num7}; n1 %= {@code num11};
				n2 += {@code num11}; n2 %= {@code num13};
				n3 += {@code num13}; n3 %= {@code num17};
			}

			{@link Svet Svet}.{@link Svet#zastavTón() zastavTón}();
			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.200});

			{@link Svet Svet}.{@code currgenerátorTónov}().setSmoothness({@code num1.0});

			n1 = {@code num3}; n2 = {@code num5}; n3 = {@code num7};
			{@code kwdfor} (n = {@code num1}; n &lt;= {@code num100}; ++n)
			{
				{@link Svet Svet}.{@link Svet#hrajTón(double) hrajTón}((n1 + n2 + n3) * {@code num100});
				{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.020});

				n1 += {@code num7}; n1 %= {@code num11};
				n2 += {@code num11}; n2 %= {@code num13};
				n3 += {@code num13}; n3 %= {@code num17};
			}

			{@link Svet Svet}.{@link Svet#zastavTón() zastavTón}();
			{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num0.200});
			</pre>
		 * 
		 * <p><b>Ukážky vygenerovaných sekvencií:</b></p>
		 * 
		 * <table class="centered"><tr>
		 * 
		 * <td><p class="image"><a href="resources/nahoda-01.wav"
		 * target="_blank"><image>nahoda-01-small.png<alt/>Grafické
		 * znázornenie obsahu zvukového súboru „nahoda-01.wav“.</image>Zvuk
		 * „nahoda-01.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/nahoda-01.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p></td>
		 * 
		 * <td><p class="image"><a href="resources/nahoda-02.wav"
		 * target="_blank"><image>nahoda-02-small.png<alt/>Grafické
		 * znázornenie obsahu zvukového súboru „nahoda-02.wav“.</image>Zvuk
		 * „nahoda-02.wav“ na prevzatie.</a></p>
		 * 
		 * <p style="text-align: center;"><audio controls><source
		 * src="resources/nahoda-02.wav" type="audio/wav">Váš prehliadač
		 * neumožňuje prehratie zvuku.</audio></p></td>
		 * 
		 * </tr></table>
		 * 
		 * @see #frekvenciaNoty(int, int)
		 * @see #hrajTón(double)
		 * @see #hrajTón(double, double)
		 * @see #hrajTón(double, double, double)
		 * @see #zastavTón()
		 * @see #otvorSúborNaUloženieTónu(String)
		 * @see #otvorSúborNaUloženieTónu(String, boolean)
		 * @see #zavriSúborNaUloženieTónu()
		 */
		public static BeepChannel generátorTónov()
		{
			if (null == Zvuk.kanál) Zvuk.kanál = new BeepChannel();
			return Zvuk.kanál;
		}

		/** <p><a class="alias"></a> Alias pre {@link #generátorTónov() generátorTónov}.</p> */
		public static BeepChannel generatorTonov()
		{ return generátorTónov(); }


	// --- Časovač a časomiera


		// Práca s časovačom

		/**
		 * <p>Spustí časovač so zadaným časovým intervalom v sekundách.
		 * Časovač volá v zadanom intervale metódu {@link ObsluhaUdalostí
		 * ObsluhaUdalostí}.{@link ObsluhaUdalostí#tik() tik()}, v ktorej
		 * je možné získať objekt s informáciami o poslednej udalosti
		 * časovača metódou {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link 
		 * ÚdajeUdalostí#tik() tik()}. Pri viacnásobnom volaní tejto metódy
		 * je predchádzajúci časovač vždy zastavený. Časovač môže byť
		 * spustený niektorými metódami automaticky. Pozri napríklad:
		 * {@link GRobot#rýchlosť(double) rýchlosť}, {@link 
		 * GRobot#uhlováRýchlosť(double) uhlováRýchlosť}…</p>
		 * 
		 * @param čas časový interval v sekundách; desatinná časť je
		 *     zaokrúhlená na milisekundy
		 * 
		 * @see #spustiČasovač()
		 * @see #odložČasovač(double)
		 * @see #časovačAktívny()
		 * @see #intervalČasovača()
		 * @see #zastavČasovač()
		 */
		public static void spustiČasovač(double čas) { časovač.spusti((int)(čas * 1000)); }

		/** <p><a class="alias"></a> Alias pre {@link #spustiČasovač(double) spustiČasovač}.</p> */
		public static void spustiCasovac(double čas) { časovač.spusti((int)(čas * 1000)); }

		/**
		 * <p>Spustí časovač s naposledy zadaným časovým intervalom
		 * (v sekundách) alebo s predvoleným intervalom 40 milisekúnd (ak
		 * nebol v činnosti). (Štyridsať milisekúnd zodpovedá snímkovacej
		 * frekvencii 25 snímok za sekundu.) Časovač spúšťa v zadanom
		 * časovom intervale metódu {@link ObsluhaUdalostí
		 * ObsluhaUdalostí}.{@link ObsluhaUdalostí#tik() tik()}, v ktorej je
		 * možné získať objekt s informáciami o poslednej udalosti časovača
		 * metódou {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link 
		 * ÚdajeUdalostí#tik() tik()}. Ak časovač nie je v činnosti, tak je
		 * viacnásobné volanie tejto metódy (na rozdiel od {@link 
		 * #spustiČasovač(double) spustiČasovač(čas)}) ignorované. Časovač
		 * môže byť spustený niektorými metódami automaticky. Pozri
		 * napríklad: {@link GRobot#rýchlosť(double) rýchlosť}, {@link 
		 * GRobot#uhlováRýchlosť(double) uhlováRýchlosť}…</p>
		 * 
		 * @see #spustiČasovač(double)
		 * @see #odložČasovač(double)
		 * @see #časovačAktívny()
		 * @see #intervalČasovača()
		 * @see #zastavČasovač()
		 */
		public static void spustiČasovač()
		{
			if (null == časovač.časovanie) časovač.spusti();
			else if (!časovač.časovanie.isRunning()) časovač.časovanie.start();
		}

		/** <p><a class="alias"></a> Alias pre {@link #spustiČasovač(double) spustiČasovač}.</p> */
		public static void spustiCasovac() { spustiČasovač(); }

		/**
		 * <p>Odloží časovač o zadaný časový interval v sekundách. Ak časovač
		 * doteraz nebol spustený, tak funguje rovnako ako metóda {@link 
		 * #spustiČasovač(double) spustiČasovač}, inak iba odloží najbližšie
		 * spustenie časovača o zadaný časový údaj.</p>
		 * 
		 * @param čas časový interval v sekundách; desatinná časť je
		 *     zaokrúhlená na milisekundy
		 * 
		 * @see #spustiČasovač(double)
		 * @see #časovačAktívny()
		 * @see #intervalČasovača()
		 * @see #zastavČasovač()
		 */
		public static void odložČasovač(double čas) { časovač.odlož((int)(čas * 1000)); }

		/** <p><a class="alias"></a> Alias pre {@link #odložČasovač(double) odložČasovač}.</p> */
		public static void odlozCasovac(double čas) { časovač.odlož((int)(čas * 1000)); }

		/**
		 * <p>Zistí, či je časovač aktívny.</p>
		 * 
		 * @return {@code valtrue}/&#8203;{@code valfalse} – podľa toho, či je
		 *     časovač aktívny alebo nie
		 * 
		 * @see #spustiČasovač(double)
		 * @see #spustiČasovač()
		 * @see #odložČasovač(double)
		 * @see #intervalČasovača()
		 * @see #zastavČasovač()
		 */
		public static boolean časovačAktívny() { return časovač.aktívny(); }

		/** <p><a class="alias"></a> Alias pre {@link #časovačAktívny() časovačAktívny}.</p> */
		public static boolean casovacAktivny() { return časovač.aktívny(); }

		/** <p><a class="alias"></a> Alias pre {@link #časovačAktívny() časovačAktívny}.</p> */
		public static boolean časovačSpustený() { return časovač.aktívny(); }

		/** <p><a class="alias"></a> Alias pre {@link #časovačAktívny() časovačAktívny}.</p> */
		public static boolean casovacSpusteny() { return časovač.aktívny(); }

		/**
		 * <p>Vráti časový interval časovača v sekundách.</p>
		 * 
		 * @return časový interval v sekundách; desatinná časť je
		 *     zaokrúhlená na milisekundy
		 * 
		 * @see #spustiČasovač(double)
		 * @see #spustiČasovač()
		 * @see #odložČasovač(double)
		 * @see #časovačAktívny()
		 * @see #zastavČasovač()
		 */
		public static double intervalČasovača()
		{
			return (double)časovač.interval() / 1000.0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #intervalČasovača() intervalČasovača}.</p> */
		public static double intervalCasovaca() { return intervalČasovača(); }

		/**
		 * <p>Zastaví časovač, ktorý bol spustený metódou {@link 
		 * #spustiČasovač(double) spustiČasovač}.</p>
		 * 
		 * @see #spustiČasovač(double)
		 * @see #spustiČasovač()
		 * @see #odložČasovač(double)
		 * @see #časovačAktívny()
		 * @see #intervalČasovača()
		 */
		public static void zastavČasovač() { časovač.zastav(); }

		/** <p><a class="alias"></a> Alias pre {@link #zastavČasovač() zastavČasovač}.</p> */
		public static void zastavCasovac() { časovač.zastav(); }


		// Čakanie

		/**
		 * <p>Pozdrží vykonávanie programu na zadaný počet sekúnd.</p>
		 * 
		 * @param početSekúnd počet sekúnd, na ktorý sa má vykonávanie
		 *     programu zastaviť
		 */
		public static void čakaj(double početSekúnd)
		{
			try { Thread.sleep((long)(početSekúnd * 1000)); }
			// Jedna z mála natvrdo ignorovaných výnimiek:
			catch (InterruptedException ie) {}
			// { GRobotException.vypíšChybovéHlásenia(ie/*, false*/); }
		}

		/** <p><a class="alias"></a> Alias pre {@link #čakaj(double) čakaj}.</p> */
		public static void cakaj(double početSekúnd) { čakaj(početSekúnd); }


		// Časomiera

		// Atribút stopiek, čiže časomiery…
		private static long stopky = 0;

		/**
		 * <p>Táto metóda „spustí“ časomieru. V skutočnosti metóda pracuje
		 * tak, že do vnútorného atribútu uloží aktuálny čas virtuálneho
		 * stroja v nanosekundách, ktorý sa neskôr použije (metódou {@link 
		 * #zastavČasomieru() zastavČasomieru}) na vypočítanie trvania
		 * merania (v sekundách). Príklad použitia je v opise metódy {@link 
		 * #zastavČasomieru() zastavČasomieru}.</p>
		 * 
		 * @see #zastavČasomieru()
		 */
		public static void spustiČasomieru()
		{ stopky = System.nanoTime(); }

		/** <p><a class="alias"></a> Alias pre {@link #spustiČasomieru() spustiČasomieru}.</p> */
		public static void spustiCasomieru()
		{ stopky = System.nanoTime(); }

		/**
		 * <p>Táto metóda „zastaví“ časomieru a vráti výsledok merania
		 * v sekundách. V skutočnosti metóda pracuje tak, že odčíta od
		 * aktuálneho času virtuálneho stroja v nanosekundách čas, ktorý bol
		 * zapamätaný pri spustení merania metódou {@link #spustiČasomieru()
		 * spustiČasomieru} a výsledok prepočíta na reálne číslo vyjadrujúce
		 * čas v sekundách (s presnosťou, ktorá by nemala byť nižšia než
		 * tri desatinné miesta – teoreticky by mohla dosahovať presnosť
		 * deväť desatinných miest (presnosť nanosekúnd), avšak skutočná
		 * presnosť je nižšia).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@link Svet Svet}.{@link #spustiČasomieru() spustiČasomieru}();

			{@code comm// Časovo náročná operácia, ktorej trvanie chceme odmerať…}

			{@code typedouble} čas = {@link Svet Svet}.{@code currzastavČasomieru}(); {@code comm// Výsledok merania je uložený v premennej „čas“}

			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Výsledný čas: "}, čas, {@code srg"s"});
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p>Výsledkom môže byť napríklad takýto výpis:</p>
		 * 
		 * <pre CLASS="example">
			{@code Výsledný čas: 6,475 s}
			</pre>
		 * 
		 * @return nameraný čas v sekundách
		 * 
		 * @see #spustiČasomieru()
		 */
		public static double zastavČasomieru()
		{
			stopky = System.nanoTime() - stopky;
			return stopky / 1e9;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zastavČasomieru() zastavČasomieru}.</p> */
		public static double zastavCasomieru()
		{
			stopky = System.nanoTime() - stopky;
			return stopky / 1e9;
		}


	// --- Interpolácie a ďalšia geometria

		/**
		 * <p>Pomocou lineárnej interpolácie je možné získať ľubovoľnú
		 * „priamočiaru“ hodnotu ležiacu medzi hodnotami {@code a}
		 * a {@code b} a to s pomocou parametra {@code t}. Parameter
		 * {@code t} by mal nadobúdať hodnoty medzi {@code num0.0}
		 * a {@code num1.0}. Keď je lineárna interpolácia použitá
		 * napríklad so začiatočnými a koncovými súradnicami bodu
		 * (dva výpočty: jeden pre {@code x1} až {@code x2} a druhý
		 * pre {@code y1} až {@code y2} – s rovnakými hodnotami
		 * parametera {@code t}, ktoré by mali ležať v rozmedzí
		 * hodnôt {@code num0.0} až {@code num1.0}), tak výsledkom
		 * výpočtov lineárnej interpolácie budú body ležiace
		 * na úsečke medzi určenými súradnicami bodov.</p>
		 * 
		 * <p><image>linearnaInterpolacia.png<alt/>Body vypočítané
		 * s použitím lineárnej interpolácie.</image>Body vypočítané pomocou
		 * lineárnej interpolácie ležiace na nakreslenej úsečke.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Podmieňovací spôsob („by mali“)
		 * použitý výššie v súvislosti hodnotou parametra {@code t} bol
		 * použitý úmyselne. Zamerajme sa na príklad kreslenia bodov (podľa
		 * súradníc vypočítaných s pomocou lineárnej interpolácie) ležiacich
		 * na úsečke s počiatočným bodom A[x<sub>1</sub>, y<sub>1</sub>]
		 * a koncovým bodom B[x<sub>2</sub>, y<sub>2</sub>]. Ak sa hodnota
		 * parametra {@code t} bude nachádzať mimo povoleného (resp.
		 * odporúčaného) rozashu ⟨0; 1⟩, tak vypočítame bod ležiaci na priamke
		 * určenej hraničnými bodmi A a B, ktorý neleží na úsečke |AB|.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci príklad nakreslí body ležiace na úsečke vypočítané
		 * pomocou lineárnej interpolácie.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdfinal} {@link Bod Bod}[] body = {@code kwdnew} {@link Bod Bod}[{@code num2}];
			body[{@code num0}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}(&#45;{@code num160}, &#45;{@code num80});
			body[{@code num1}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}({@code num130}, {@code num60});

			{@code kwdfor} ({@code typedouble} t = {@code num0.0}; t &lt;= {@code num1.0}; t += {@code num0.1})
			{
				{@code typedouble} x = {@link Svet Svet}.{@code currlineárnaInterpolácia}(
					body[{@code num0}].{@link GRobot#polohaX() polohaX}(), body[{@code num1}].{@link GRobot#polohaX() polohaX}(), t);
				{@code typedouble} y = {@link Svet Svet}.{@code currlineárnaInterpolácia}(
					body[{@code num0}].{@link GRobot#polohaY() polohaY}(), body[{@code num1}].{@link GRobot#polohaY() polohaY}(), t);

				{@link GRobot#skočNa(double, double) skočNa}(x, y);
				{@link GRobot#kružnica(double) kružnica}({@code num6});
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>vysledokLinearnejInterpolacie.png<alt/>Body vypočítané
		 * s použitím lineárnej interpolácie.</image>Výsledok príkladu
		 * lineárnej interpolácie.</p>
		 * 
		 * @param a prvá hodnota určujúca lineárnu interpoláciu
		 * @param b druhá hodnota určujúca lineárnu interpoláciu
		 * @param t parameter interpolácie – mal by ležať v rozmedzí hodnôt
		 *     {@code num0.0} až {@code num1.0}
		 * @return výsledok interpolácie
		 * 
		 * @see #kvadratickáInterpolácia(double, double, double, double)
		 * @see #kubickáInterpolácia(double, double, double, double, double)
		 */

		public final static double lineárnaInterpolácia(
			double a, double b, double t) { return a + t * (b - a); }

		/** <p><a class="alias"></a> Alias pre {@link #lineárnaInterpolácia(double, double, double) lineárnaInterpolácia}.</p> */
		public final static double linearnaInterpolacia(
			double a, double b, double t) { return a + t * (b - a); }

		/**
		 * <p>Vypočíta polohu na kvadratickej (v tomto prípade bézierovej)
		 * krivke.</p><!-- TODO dokončiť opis (bézierova krivka má určité
		 * vlastnosti – overiť, aké?) a pridať lepší príklad s obrázkom -->
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúce dva riadky kódu vypočítajú súradnice bodu ([x, y])
		 * na kvadratickej (bézierovej) krivke v mieste, kde je hodnota
		 * parametra interpolácie (t) rovná 0,5:</p>
		 * 
		 * <pre CLASS="example">
			<!-- TODO dokončiť príklad -->
			x = kvadratickáInterpolácia(x1, x2, x3, 0.5);
			y = kvadratickáInterpolácia(y1, y2, y3, 0.5);
			</pre>
		 * 
		 * @param a prvá hodnota určujúca kvadratickú interpoláciu
		 * @param b druhá hodnota určujúca kvadratickú interpoláciu
		 * @param c tretia hodnota určujúca kvadratickú interpoláciu
		 * @param t parameter interpolácie – mal by ležať v rozmedzí hodnôt
		 *     {@code num0.0} až {@code num1.0}
		 * @return výsledok interpolácie
		 * 
		 * @see #lineárnaInterpolácia(double, double, double)
		 * @see #kubickáInterpolácia(double, double, double, double, double)
		 */
		public final static double kvadratickáInterpolácia(
			double a, double b, double c, double t)
		{
			double ti = 1.0 - t; // optimalizácia
			return (ti * a + 2.0 * t * b) * ti + t * t * c;
		}

		/** <p><a class="alias"></a> Alias pre {@link #kvadratickáInterpolácia(double, double, double, double) kvadratickáInterpolácia}.</p> */
		public final static double kvadratickaInterpolacia(
			double a, double b, double c, double t)
		{ return kvadratickáInterpolácia(a, b, c, t); }

		/**
		 * <p>Kubická interpolácia je počítaná zo štyroch hodnôt. Význam
		 * parametrov sa lepšie vysvetľuje na príklade kreslenia krivky
		 * pomocou tohto druhu interpolácie. Interpolujme dvojice súradníc
		 * štyroch kľúčových bodov ({@code [x0, y0]} až {@code [x3, y3]}).
		 * V takom prípade prvá a posledná dvojica súradníc určujú „smer“
		 * kubickej krivky a prostredné dve určujú počiatočný a koncový
		 * bod krivky. Výsledkom takejto interpolácie sú body ležiace na
		 * kubickej krivke.</p>
		 * 
		 * <p><image>kubickaInterpolacia.png<alt/>Body vypočítané s použitím
		 * kubickej interpolácie.</image>Body vypočítané pomocou kubickej
		 * interpolácie zo štyroch označených<br />kľúčových bodov –
		 * vypočítané body ležia na kubickej krivke.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci príklad ukazuje interaktívny test kubickej
		 * interpolácie (po preložení a spustení príkladu je klikaním
		 * a posúvaním bodov možné ovplyvňovať kresbu).</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestInterpolácie {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Aktívny bod (predvolene je to prvý bod – s indexom nula).}
				{@code kwdprivate} {@code typeint} bod = {@code num0};

				{@code comm// Definícia štvorice kľúčových bodov.}
				{@code kwdprivate} {@link Bod Bod}[] body = {@code kwdnew} {@link Bod Bod}[{@code num4}];
				{
					body[{@code num0}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}(&#45;{@code num100}, &#45;{@code num70});
					body[{@code num1}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}(&#45;{@code num100}, {@code num40});
					body[{@code num2}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}({@code num100}, {@code num40});
					body[{@code num3}] = {@code kwdnew} {@link Bod#Bod(double, double) Bod}({@code num100}, &#45;{@code num70});
				}

				{@code comm// Konštruktor.}
				{@code kwdprivate} TestInterpolácie()
				{
					{@link GRobot#zdvihniPero() zdvihniPero}();
					{@link GRobot#skry() skry}();
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
					prekresli();
				}

				{@code comm// Prekresľovanie kresby.}
				{@code kwdprivate} {@code typevoid} prekresli()
				{
					{@link Svet Svet}.{@link Svet#vymaž() vymaž}();

					{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});

					{@code kwdfor} ({@code typedouble} t = {@code num0.0}; t &lt;= {@code num1.0}; t += {@code num0.1})
					{
						{@code typedouble} x = {@link Svet Svet}.{@code currkubickáInterpolácia}(body[{@code num0}].{@link Bod#polohaX() polohaX}(),
							body[{@code num1}].{@link Bod#polohaX() polohaX}(), body[{@code num2}].{@link Bod#polohaX() polohaX}(), body[{@code num3}].{@link Bod#polohaX() polohaX}(), t);
						{@code typedouble} y = {@link Svet Svet}.{@code currkubickáInterpolácia}(body[{@code num0}].{@link Bod#polohaY() polohaY}(),
							body[{@code num1}].{@link Bod#polohaY() polohaY}(), body[{@code num2}].{@link Bod#polohaY() polohaY}(), body[{@code num3}].{@link Bod#polohaY() polohaY}(), t);

						{@link GRobot#skočNa(double, double) skočNa}(x, y);
						{@link GRobot#kružnica(double) kružnica}({@code num6});
					}

					{@link GRobot#farba(Color) farba}({@link Farebnosť#červená červená});

					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num4}; ++i)
					{
						{@link GRobot#skočNa(double, double) skočNa}(body[i]);
						{@link GRobot#kružnica(double) kružnica}({@code num3});
					}

					{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}

				{@code comm// Vyhľadanie a aktivácia bodu, na ktorý bolo kliknuté}
				{@code comm// (ak nebolo kliknuté na žiadny konkrétny bod, zostane}
				{@code comm// aktívny posledný aktivovaný bod).}
				{@code comm// Aktívny bod je zároveň presunutý na myš.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
				{
					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num4}; ++i)
					{
						{@link GRobot#skočNa(double, double) skočNa}(body[i]);
						{@code kwdif} ({@link GRobot#myšVKruhu(double) myšVKruhu}({@code num10})) bod = i;
					}

					body[bod] = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();
					prekresli();
				}

				{@code comm// Presúvanie aktívneho bodu na súradnice myši.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
				{
					body[bod] = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();
					prekresli();
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code kwdnew} TestInterpolácie();
				}
			}
			</pre>
		 * 
		 * @param v0 prvá hodnota určujúca kubickú interpoláciu
		 * @param v1 druhá hodnota určujúca kubickú interpoláciu
		 * @param v2 tretia hodnota určujúca kubickú interpoláciu
		 * @param v3 štvrtá hodnota určujúca kubickú interpoláciu
		 * @param t parameter interpolácie – mal by ležať v rozmedzí hodnôt
		 *     {@code num0.0} až {@code num1.0}
		 * @return výsledok interpolácie
		 * 
		 * @see #lineárnaInterpolácia(double, double, double)
		 * @see #kvadratickáInterpolácia(double, double, double, double)
		 */
		public final static double kubickáInterpolácia(
			double v0, double v1, double v2, double v3, double t)
		{
			double o = v0 - v1;
			double p = v3 - v2 - o;
			double q = o - p;
			double r = v2 - v0;
			// double s = v1; return ((p * t + q) * t + r) * t + s;
			return ((p * t + q) * t + r) * t + v1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #kubickáInterpolácia(double, double, double, double, double) kubickáInterpolácia}.</p> */
		public final static double kubickaInterpolacia(
			double v0, double v1, double v2, double v3, double t)
		{ return kubickáInterpolácia(v0, v1, v2, v3, t); }


		/**
		 * <p>Prepočíta zadanú x-ovú (horizontálnu) súradnicu zo súradnicového
		 * priestoru programovacieho rámca GRobot do súradnicového priestoru
		 * používaného v oblasti 2D počítačovej grafiky.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Programovací rámec ako
		 * prostriedok primárne vyvíjaný na účely výučby pracuje s „klasickým“
		 * karteziánskym súradnicovým systémom, pri používaní ktorého je
		 * počiatok súradnicovej sústavy („stred“ – bod so súradnicami [0, 0])
		 * obvykle umiestňovaný do stredu kresliacej plochy, horizontálna
		 * súradnica (x-ová) rastie zľava doprava a vertikálna súradnica
		 * (y-ová) rastie zospodu nahor. Naproti tomu, v 2D počítačovej grafike
		 * je bežné umiestnenie počiatku súradnicovej sústavy v ľavom hornom
		 * rohu kresliacej plochy (obrazovky) a z toho dôvodu býva rast
		 * vertikálnej súradnice prevrátený – súradnica narastá smerom zhora
		 * nadol. (Tento rozdiel je diskutovaný na viacerých miestach tejto
		 * dokumentácie. Pozri napríklad opis metódy {@link GRobot#cesta()
		 * cesta}.)</p>
		 * 
		 * @param x hodnota súradnice x v súradnicovom priestore
		 *     programovacieho rámca GRobot
		 * @return hodnota súradnice v súradnicovom priestore počítačovej
		 *     grafiky (to jest v tom súradnicovom priestore, ktorý predvolene
		 *     používa aj jazyk Java pri práci s 2D grafikou)
		 */
		public static double prepočítajX(double x)
		{
			return (Plátno.šírkaPlátna / 2.0) + x;
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepočítajX(double) prepočítajX}.</p> */
		public static double prepocitajX(double x) { return prepočítajX(x); }

		/**
		 * <p>Prepočíta zadanú y-ovú (vertikálnu) súradnicu zo súradnicového
		 * priestoru programovacieho rámca GRobot do súradnicového priestoru
		 * používaného v oblasti 2D počítačovej grafiky.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Programovací rámec ako
		 * prostriedok primárne vyvíjaný na účely výučby pracuje s „klasickým“
		 * karteziánskym súradnicovým systémom, pri používaní ktorého je
		 * počiatok súradnicovej sústavy („stred“ – bod so súradnicami [0, 0])
		 * obvykle umiestňovaný do stredu kresliacej plochy, horizontálna
		 * súradnica (x-ová) rastie zľava doprava a vertikálna súradnica
		 * (y-ová) rastie zospodu nahor. Naproti tomu, v 2D počítačovej grafike
		 * je bežné umiestnenie počiatku súradnicovej sústavy v ľavom hornom
		 * rohu kresliacej plochy (obrazovky) a z toho dôvodu býva rast
		 * vertikálnej súradnice prevrátený – súradnica narastá smerom zhora
		 * nadol. (Tento rozdiel je diskutovaný na viacerých miestach tejto
		 * dokumentácie. Pozri napríklad opis metódy {@link GRobot#cesta()
		 * cesta}.)</p>
		 * 
		 * @param y hodnota súradnice y v súradnicovom priestore
		 *     programovacieho rámca GRobot
		 * @return hodnota súradnice v súradnicovom priestore počítačovej
		 *     grafiky (to jest v tom súradnicovom priestore, ktorý predvolene
		 *     používa aj jazyk Java pri práci s 2D grafikou)
		 */
		public static double prepočítajY(double y)
		{
			return (Plátno.výškaPlátna / 2.0) - y;
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepočítajY(double) prepočítajY}.</p> */
		public static double prepocitajY(double y) { return prepočítajY(y); }

		/**
		 * <p>Prepočíta zadanú x-ovú (horizontálnu) súradnicu zo súradnicového
		 * priestoru používaného v oblasti 2D počítačovej grafiky do
		 * súradnicového priestoru programovacieho rámca GRobot.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Programovací rámec ako
		 * prostriedok primárne vyvíjaný na účely výučby pracuje s „klasickým“
		 * karteziánskym súradnicovým systémom, pri používaní ktorého je
		 * počiatok súradnicovej sústavy („stred“ – bod so súradnicami [0, 0])
		 * obvykle umiestňovaný do stredu kresliacej plochy, horizontálna
		 * súradnica (x-ová) rastie zľava doprava a vertikálna súradnica
		 * (y-ová) rastie zospodu nahor. Naproti tomu, v 2D počítačovej grafike
		 * je bežné umiestnenie počiatku súradnicovej sústavy v ľavom hornom
		 * rohu kresliacej plochy (obrazovky) a z toho dôvodu býva rast
		 * vertikálnej súradnice prevrátený – súradnica narastá smerom zhora
		 * nadol. (Tento rozdiel je diskutovaný na viacerých miestach tejto
		 * dokumentácie. Pozri napríklad opis metódy {@link GRobot#cesta()
		 * cesta}.)</p>
		 * 
		 * @param x hodnota súradnice v súradnicovom priestore počítačovej
		 *     grafiky (to jest v tom súradnicovom priestore, ktorý predvolene
		 *     používa aj jazyk Java pri práci s 2D grafikou)
		 * @return hodnota súradnice x v súradnicovom priestore programovacieho
		 *     rámca GRobot
		 */
		public static double prepočítajSpäťX(double x)
		{
			return x - (Plátno.šírkaPlátna / 2.0);
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepočítajSpäťX(double) prepočítajSpäťX}.</p> */
		public static double prepocitajSpatX(double x)
		{ return prepočítajSpäťX(x); }

		/**
		 * <p>Prepočíta zadanú y-ovú (vertikálnu) súradnicu zo súradnicového
		 * priestoru používaného v oblasti 2D počítačovej grafiky do
		 * súradnicového priestoru programovacieho rámca GRobot.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Programovací rámec ako
		 * prostriedok primárne vyvíjaný na účely výučby pracuje s „klasickým“
		 * karteziánskym súradnicovým systémom, pri používaní ktorého je
		 * počiatok súradnicovej sústavy („stred“ – bod so súradnicami [0, 0])
		 * obvykle umiestňovaný do stredu kresliacej plochy, horizontálna
		 * súradnica (x-ová) rastie zľava doprava a vertikálna súradnica
		 * (y-ová) rastie zospodu nahor. Naproti tomu, v 2D počítačovej grafike
		 * je bežné umiestnenie počiatku súradnicovej sústavy v ľavom hornom
		 * rohu kresliacej plochy (obrazovky) a z toho dôvodu býva rast
		 * vertikálnej súradnice prevrátený – súradnica narastá smerom zhora
		 * nadol. (Tento rozdiel je diskutovaný na viacerých miestach tejto
		 * dokumentácie. Pozri napríklad opis metódy {@link GRobot#cesta()
		 * cesta}.)</p>
		 * 
		 * @param y hodnota súradnice v súradnicovom priestore počítačovej
		 *     grafiky (to jest v tom súradnicovom priestore, ktorý predvolene
		 *     používa aj jazyk Java pri práci s 2D grafikou)
		 * @return hodnota súradnice y v súradnicovom priestore programovacieho
		 *     rámca GRobot
		 */
		public static double prepočítajSpäťY(double y)
		{
			return -y + (Plátno.výškaPlátna / 2.0);
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepočítajSpäťY(double) prepočítajSpäťY}.</p> */
		public static double prepocitajSpatY(double y)
		{ return prepočítajSpäťY(y); }

		/**
		 * <p>Vráti výpočet x-ovej súradnice zadaného bodu pootočeného okolo
		 * stredu súradnicovej sústavy o zadaný uhol.</p>
		 * 
		 * @param x x-ová súradnica pôvodného bodu
		 * @param y y-ová súradnica pôvodného bodu
		 * @param uhol uhol pootočenia
		 * 
		 * @see #rotovanéY(double, double, double)
		 */
		public final static double rotovanéX(double x, double y, double uhol)
		{
			if (0 == uhol) return x;
			double α = Math.toRadians(uhol);
			return (x * Math.cos(α)) - (y * Math.sin(α));
		}

		/** <p><a class="alias"></a> Alias pre {@link #rotovanéX(double, double, double) rotovanéX}.</p> */
		public final static double rotovaneX(double x, double y, double uhol)
		{ return rotovanéX(x, y, uhol); }

		/**
		 * <p>Vráti výpočet y-ovej súradnice zadaného bodu pootočeného okolo
		 * stredu súradnicovej sústavy o zadaný uhol.</p>
		 * 
		 * @param x x-ová súradnica pôvodného bodu
		 * @param y y-ová súradnica pôvodného bodu
		 * @param uhol uhol pootočenia
		 * 
		 * @see #rotovanéX(double, double, double)
		 */
		public final static double rotovanéY(double x, double y, double uhol)
		{
			if (0 == uhol) return y;
			double α = Math.toRadians(uhol);
			return (x * Math.sin(α)) + (y * Math.cos(α));
		}

		/** <p><a class="alias"></a> Alias pre {@link #rotovanéY(double, double, double) rotovanéY}.</p> */
		public final static double rotovaneY(double x, double y, double uhol)
		{ return rotovanéY(x, y, uhol); }


		/**
		 * <p>Hľadá priesečník dvoch úsečiek |AB| a |CD|. Úsečky sú určené
		 * súradnicami počiatočných a koncových bodov – |AB|: A[x0, y0],
		 * B[x1, y1] a |CD|: C[x2, y2], D[x3, y3]. Ak priesečník jestvuje,
		 * tak metóda vráti inštanciu triedy {@link Bod Bod} so súradnicami
		 * priesečníka, inak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie priesečníka… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * @param x0 x-ová súradnica bodu A
		 * @param y0 y-ová súradnica bodu A
		 * @param x1 x-ová súradnica bodu B
		 * @param y1 y-ová súradnica bodu B
		 * @param x2 x-ová súradnica bodu C
		 * @param y2 y-ová súradnica bodu C
		 * @param x3 x-ová súradnica bodu D
		 * @param y3 y-ová súradnica bodu D
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkÚsečiek(
			double x0, double y0, double x1, double y1,
			double x2, double y2, double x3, double y3)
		{
			try
			{
				Bod priesečník = new Bod();
				if (priesečníkÚsečiek(x0, y0, x1, y1,
					x2, y2, x3, y3, priesečník))
					return priesečník;
				return null;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkÚsečiek(double, double, double, double, double, double, double, double) priesečníkÚsečiek}.</p> */
		public final static Bod priesecnikUseciek(
			double x0, double y0, double x1, double y1,
			double x2, double y2, double x3, double y3)
		{ return priesečníkÚsečiek(x0, y0, x1, y1, x2, y2, x3, y3); }


		/**
		 * <p>Hľadá priesečník dvoch úsečiek |AB| a |CD|. Úsečky sú určené
		 * polohami bodov v štyroch parametroch. Ak priesečník jestvuje,
		 * tak metóda vráti inštanciu triedy {@link Bod Bod} so súradnicami
		 * priesečníka, inak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param A poloha bodu A
		 * @param B poloha bodu B
		 * @param C poloha bodu C
		 * @param D poloha bodu D
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkÚsečiek(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{
			try
			{
				Bod priesečník = new Bod();
				if (priesečníkÚsečiek(A.polohaX(), A.polohaY(),
					B.polohaX(), B.polohaY(), C.polohaX(), C.polohaY(),
					D.polohaX(), D.polohaY(), priesečník)) return priesečník;
				return null;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkÚsečiek(Poloha, Poloha, Poloha, Poloha) priesečníkÚsečiek}.</p> */
		public final static Bod priesecnikUseciek(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{ return priesečníkÚsečiek(A, B, C, D); }


		/**
		 * <p>Hľadá priesečník dvoch úsečiek |AB| a |CD|. Úsečky sú určené
		 * polohami bodov v poli parametra, ktoré musí obsahovať aspoň štyri
		 * prvky. Ak priesečník jestvuje, tak metóda vráti inštanciu triedy
		 * {@link Bod Bod} so súradnicami priesečníka, inak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * @param poleBodov polohy bodov úsečiek |AB| a |CD|
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkÚsečiek(Poloha[] poleBodov)
		{
			if (null == poleBodov || 4 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2] || null == poleBodov[3]*/) return null;
			try
			{
				Bod priesečník = new Bod();
				if (priesečníkÚsečiek(
					poleBodov[0].polohaX(), poleBodov[0].polohaY(),
					poleBodov[1].polohaX(), poleBodov[1].polohaY(),
					poleBodov[2].polohaX(), poleBodov[2].polohaY(),
					poleBodov[3].polohaX(), poleBodov[3].polohaY(),
					priesečník)) return priesečník;
				return null;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkÚsečiek(Poloha[]) priesečníkÚsečiek}.</p> */
		public final static Bod priesecnikUseciek(Poloha[] poleBodov)
		{ return priesečníkÚsečiek(poleBodov); }


		/**
		 * <p>Hľadá priesečník dvoch priamok určených bodmi A, B a C, D.
		 * Ak priesečník jestvuje, tak metóda vráti inštanciu triedy
		 * {@link Bod Bod} so súradnicami priesečníka, inak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie priesečníka… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * <p>Hľadanie priesečníka priamok je použité aj v príklade
		 * uvedenom v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @param x0 x-ová súradnica bodu A
		 * @param y0 y-ová súradnica bodu A
		 * @param x1 x-ová súradnica bodu B
		 * @param y1 y-ová súradnica bodu B
		 * @param x2 x-ová súradnica bodu C
		 * @param y2 y-ová súradnica bodu C
		 * @param x3 x-ová súradnica bodu D
		 * @param y3 y-ová súradnica bodu D
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkPriamok(
			double x0, double y0, double x1, double y1,
			double x2, double y2, double x3, double y3)
		{
			try
			{
				Bod priesečník = new Bod();
				priesečníkÚsečiek(x0, y0, x1, y1,
					x2, y2, x3, y3, priesečník);
				return priesečník;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkPriamok(double, double, double, double, double, double, double, double) priesečníkPriamok}.</p> */
		public final static Bod priesecnikPriamok(
			double x0, double y0, double x1, double y1,
			double x2, double y2, double x3, double y3)
		{ return priesečníkPriamok(x0, y0, x1, y1, x2, y2, x3, y3); }


		/**
		 * <p>Hľadá priesečník dvoch priamok určených bodmi A, B a C, D.
		 * Ak priesečník jestvuje, tak metóda vráti inštanciu triedy
		 * {@link Bod Bod} so súradnicami priesečníka, inak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * <p>Hľadanie priesečníka priamok je použité aj v príklade
		 * uvedenom v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @param A poloha bodu A
		 * @param B poloha bodu B
		 * @param C poloha bodu C
		 * @param D poloha bodu D
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkPriamok(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{
			try
			{
				Bod priesečník = new Bod();
				priesečníkÚsečiek(A.polohaX(), A.polohaY(),
					B.polohaX(), B.polohaY(), C.polohaX(), C.polohaY(),
					D.polohaX(), D.polohaY(), priesečník);
				return priesečník;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkPriamok(Poloha, Poloha, Poloha, Poloha) priesečníkPriamok}.</p> */
		public final static Bod priesecnikPriamok(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{
			return priesečníkPriamok(A, B, C, D);
		}


		/**
		 * <p>Hľadá priesečník dvoch priamok určených polohami bodov v poli
		 * parametra. Pole musí obsahovať aspoň štyri prvky. Ak priesečník
		 * jestvuje, tak metóda vráti inštanciu triedy {@link Bod Bod} so
		 * súradnicami priesečníka, inak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p>Hľadanie priesečníka priamok je použité aj v príklade
		 * uvedenom v opise metódy {@link GRobot#mimoHraníc(Bod[], double)
		 * mimoHraníc}.</p>
		 * 
		 * @param poleBodov polohy bodov určujúce priamky
		 * @return priesečník v inštancii triedy {@link Bod Bod} alebo
		 *     {@code valnull}
		 */
		public final static Bod priesečníkPriamok(Poloha[] poleBodov)
		{
			if (null == poleBodov || 4 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2] || null == poleBodov[3]*/) return null;
			try
			{
				Bod priesečník = new Bod();
				priesečníkÚsečiek(
					poleBodov[0].polohaX(), poleBodov[0].polohaY(),
					poleBodov[1].polohaX(), poleBodov[1].polohaY(),
					poleBodov[2].polohaX(), poleBodov[2].polohaY(),
					poleBodov[3].polohaX(), poleBodov[3].polohaY(),
					priesečník);
				return priesečník;
			}
			catch (GRobotException e)
			{
				return null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkPriamok(Poloha[]) priesečníkPriamok}.</p> */
		public final static Bod priesecnikPriamok(Poloha[] poleBodov)
		{ return priesečníkPriamok(poleBodov); }


		/**
		 * <p>Hľadá priesečníky dvoch kružníc určených súradnicami ich
		 * stredov S1[x1, y1] a S2[x2, y2] a polomermi r1 a r2. Ak jestvuje
		 * aspoň jeden priesečník, tak metóda vráti jedno- alebo dvojprvkové
		 * pole bodov určujúcich súradnice priesečníkov. Ak nejestvuje ani
		 * jeden priesečník, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie priesečníkov… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * @param x1 x-ová súradnica stredu prvej kružnice
		 * @param y1 y-ová súradnica stredu prvej kružnice
		 * @param r1 polomer prvej kružnice
		 * @param x2 x-ová súradnica stredu druhej kružnice
		 * @param y2 y-ová súradnica stredu druhej kružnice
		 * @param r2 polomer druhej kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyKružníc(
			double x1, double y1, double r1,
			double x2, double y2, double r2)
		{
			double Δx = x2 - x1;
			double Δy = y2 - y1;
			double d2 = Δx * Δx + Δy * Δy;
			double d = Math.sqrt(d2);

			// Nemá riešenie:
			if (d > r1 + r2 || d < Math.abs(r1 - r2)) return null;

			double r1_2 = r1 * r1;
			double r2_2 = r2 * r2;

			double a = (r1_2 - r2_2 + d2) / (2.0 * d);
			double h = Math.sqrt(r1_2 - a * a);
			double xx = x1 + a * Δx / d;
			double yy = y1 + a * Δy / d;

			// Zoznam<Poloha> priesečníky = new Zoznam<>();

			if (0.0 == h)
			{
				// priesečníky.pridaj(new Bod(xx, yy));
				// return priesečníky;
				return new Bod[] {new Bod(xx, yy)};
			}

			// priesečníky.pridaj(new Bod(xx + h * Δy / d, yy - h * Δx / d));
			// priesečníky.pridaj(new Bod(xx - h * Δy / d, yy + h * Δx / d));
			// return priesečníky;

			return new Bod[] {new Bod(xx + h * Δy / d, yy - h * Δx / d),
				new Bod(xx - h * Δy / d, yy + h * Δx / d)};
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyKružníc(double, double, double, double, double, double) priesečníkyKružníc}.</p> */
		public final static Bod[] priesecnikyKruznic(
			double x1, double y1, double r1,
			double x2, double y2, double r2)
		{
			return priesečníkyKružníc(x1, y1, r1, x2, y2, r2);
		}

		/**
		 * <p>Hľadá priesečníky dvoch kružníc určených polohami ich stredov
		 * S1 a S2 a polomermi r1 a r2. Ak jestvuje aspoň jeden priesečník,
		 * tak metóda vráti jedno- alebo dvojprvkové pole bodov určujúcich
		 * súradnice priesečníkov. Ak nejestvuje ani jeden priesečník, tak
		 * metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param S1 poloha stredu prvej kružnice
		 * @param r1 polomer prvej kružnice
		 * @param S2 poloha stredu druhej kružnice
		 * @param r2 polomer druhej kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyKružníc(
			Poloha S1, double r1, Poloha S2, double r2)
		{
			return priesečníkyKružníc(S1.polohaX(), S1.polohaY(), r1,
				S2.polohaX(), S2.polohaY(), r2);
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyKružníc(Poloha, double, Poloha, double) priesečníkyKružníc}.</p> */
		public final static Bod[] priesecnikyKruznic(
			Poloha S1, double r1, Poloha S2, double r2)
		{
			return priesečníkyKružníc(S1.polohaX(), S1.polohaY(), r1,
				S2.polohaX(), S2.polohaY(), r2);
		}


		/**
		 * <p>Hľadá priesečníky priamky |AB| a kružnice určenej súradnicami
		 * stredu S a polomeru r. Súradnice bodov priamky a stredu kružnice
		 * sú určené parametrami tejto metódy takto: A[x1, y1]; B[x2, y2];
		 * S[x3, y3]. Parameter r je polomerom kružnice. Ak jestvuje aspoň
		 * jeden priesečník, tak metóda vráti pole bodov určujúcich súradnice
		 * priesečníkov (pole môže obsahovať jeden alebo dva prvky/body). Ak
		 * nejestvuje ani jeden priesečník, tak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie priesečníkov… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * <p><b>Užitočné zdroje:</b></p>
		 * 
		 * <ul>
		 * <li><small><a href="https://stackoverflow.com/users/634135/cobie"
		 * target="_blank">User:cobie</a> – <a
		 * href="https://stackoverflow.com/users/633770/arne-b"
		 * target="_blank">User:arne.b</a> – and others</small>: <a
		 * href="https://stackoverflow.com/questions/13053061/circle-line-intersection-points"
		 * target="_blank"><em>Circle line intersection points.</em>
		 * Stack Overflow, 2012. Citované: 2016 – 2018.</a></li>
		 * 
		 * <li><small><a
		 * href="http://mathworld.wolfram.com/about/author.html"
		 * target="_blank">Weisstein, Eric W.</a></small>: <a
		 * href="http://mathworld.wolfram.com/Circle-LineIntersection.html"
		 * target="_blank"><em>Circle-Line Intersection.</em>
		 * Wolfram MathWorld, A Wolfram Web Resource. Citované: 2016 –
		 * 2018.</a></li>
		 * </ul>
		 * 
		 * @param x1 x-ová súradnica určujúceho bodu priamky A
		 * @param y1 y-ová súradnica určujúceho bodu priamky A
		 * @param x2 x-ová súradnica určujúceho bodu priamky B
		 * @param y2 y-ová súradnica určujúceho bodu priamky B
		 * @param x3 x-ová súradnica stredu kružnice
		 * @param y3 y-ová súradnica stredu kružnice
		 * @param r polomer kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyPriamkyAKružnice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{
			double Δxa = x2 - x1;
			double Δya = y2 - y1;
			double Δxb = x3 - x1;
			double Δyb = y3 - y1;

			double a = Δxa * Δxa + Δya * Δya;
			double b = Δxa * Δxb + Δya * Δyb;
			double c = Δxb * Δxb + Δyb * Δyb - r * r;

			double p = b / a;
			double q = c / a;

			double disc = p * p - q;
			if (disc < 0.0) return null;
			// Situáciu, keď „disc“ je rovný nule riešime neskôr.

			// Zoznam<Poloha> priesečníky = new Zoznam<>();

			double d = Math.sqrt(disc);
			double s = -p + d;
			double t = -p - d;

			// priesečníky.pridaj(
				Bod bod1 = new Bod(x1 - Δxa * s, y1 - Δya * s);

			if (disc == 0.0) // s == t
				// return priesečníky;
				return new Bod[] {bod1};

			// priesečníky.pridaj(
				return new Bod[] {bod1, new Bod(x1 - Δxa * t, y1 - Δya * t)};
			// return priesečníky;
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyPriamkyAKružnice(double, double, double, double, double, double, double) priesečníkyPriamkyAKružnice}.</p> */
		public final static Bod[] priesecnikyPriamkyAKruznice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{
			return priesečníkyPriamkyAKružnice(x1, y1, x2, y2, x3, y3, r);
		}

		/**
		 * <p>Hľadá priesečníky priamky |AB| a kružnice určenej polohou
		 * stredu S a polomeru r. Ak jestvuje aspoň jeden priesečník, tak
		 * metóda vráti pole bodov určujúcich súradnice priesečníkov (pole
		 * môže obsahovať jeden alebo dva prvky/body). Ak nejestvuje ani
		 * jeden priesečník, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param A poloha určujúceho bodu priamky A
		 * @param B poloha určujúceho bodu priamky B
		 * @param S poloha stredu kružnice
		 * @param r polomer kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyPriamkyAKružnice(
			Poloha A, Poloha B, Poloha S, double r)
		{
			return priesečníkyPriamkyAKružnice(A.polohaX(), A.polohaY(),
				B.polohaX(), B.polohaY(), S.polohaX(), S.polohaY(), r);
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyPriamkyAKružnice(Poloha, Poloha, Poloha, double) priesečníkyPriamkyAKružnice}.</p> */
		public final static Bod[] priesecnikyPriamkyAKruznice(
			Poloha A, Poloha B, Poloha S, double r)
		{
			return priesečníkyPriamkyAKružnice(A.polohaX(), A.polohaY(),
				B.polohaX(), B.polohaY(), S.polohaX(), S.polohaY(), r);
		}


		/**
		 * <p>Hľadá priesečníky úsečky |AB| a kružnice so stredom
		 * S a polomerom r. Ak jestvuje jeden priesečník, metóda vráti
		 * jednoprvkové pole. Ak jestvujú dva priesečníky, metóda vráti
		 * dvojprvkové pole bodov so súradnicami priesečníkov. Ak nejestvuje
		 * žiadny priesečník, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie priesečníkov… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * <!-- Pozri priamku: p><b>Užitočný zdroj:</b></p>
		 * 
		 * <ul>
		 * <li><small><a href="https://stackoverflow.com/users/634135/cobie"
		 * target="_blank">User:cobie</a> – <a
		 * href="https://stackoverflow.com/users/633770/arne-b"
		 * target="_blank">User:arne.b</a> – and others</small>: <a
		 * href="https://stackoverflow.com/questions/13053061/circle-line-intersection-points"
		 * target="_blank"><em>Circle line intersection points.</em>
		 * Stack Overflow, 2012. Citované: 2016 – 2018.</a></li>
		 * </ul -->
		 * 
		 * <!-- p>Zdroje, ktoré už nefungujú: https://sites.google.com/site/
		 * t3hprogrammer/research/line-circle-collision/tutorial
		 * http://keith-hair.net/blog/2008/08/05/line-to-circle-
		 * intersection-data/ (ActionScript)</p -->
		 * 
		 * @param x1 x-ová súradnica bodu A úsečky
		 * @param y1 y-ová súradnica bodu A úsečky
		 * @param x2 x-ová súradnica bodu B úsečky
		 * @param y2 y-ová súradnica bodu B úsečky
		 * @param x3 x-ová súradnica stredu kružnice
		 * @param y3 y-ová súradnica stredu kružnice
		 * @param r polomer kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyÚsečkyAKružnice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{
			double Δx = x2 - x1;
			double Δy = y2 - y1;
			double Δx_ = x1 - x3;
			double Δy_ = y1 - y3;

			double a = Δx * Δx + Δy * Δy;
			double b = 2.0 * (Δx * Δx_ + Δy * Δy_);
			double c = Δx_ * Δx_ + Δy_ * Δy_ - r * r;

			// double cc = x3 * x3 + y3 * y3 + x1 * x1 + y1 * y1 -
			// 	2.0 * (x3 * x1 + y3 * y1) - r * r;

			double determinant = b * b - 4.0 * a * c;

			if (determinant < 0.0) return null;

			// Zoznam<Poloha> priesečníky = new Zoznam<>();

			if (0.0 == determinant)
			{
				double u = -b / (2.0 * a);
				if (u >= 0.0 && u <= 1.0)
					// priesečníky.pridaj(
					return new Bod[] {new Bod(x1 + Δx * u, y1 + Δy * u)};
			}
			else
			{
				double e = Math.sqrt(determinant);
				double u1 = (-b + e) / (2.0 * a);
				double u2 = (-b - e) / (2.0 * a);

				Bod bod1, bod2;

				if (u1 >= 0.0 && u1 <= 1.0)
					// priesečníky.pridaj(
					bod1 = new Bod(x1 + Δx * u1, y1 + Δy * u1);
				else
					bod1 = null;

				if (u2 >= 0.0 && u2 <= 1.0)
					// priesečníky.pridaj(
					bod2 = new Bod(x1 + Δx * u2, y1 + Δy * u2);
				else
					bod2 = null;

				if (null != bod1 && null != bod2)
					return new Bod[] {bod1, bod2};

				if (null != bod1) return new Bod[] {bod1};
				if (null != bod2) return new Bod[] {bod2};
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyÚsečkyAKružnice(double, double, double, double, double, double, double) priesečníkyÚsečkyAKružnice}.</p> */
		public final static Bod[] priesecnikyUseckyAKruznice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{
			return priesečníkyÚsečkyAKružnice(x1, y1, x2, y2, x3, y3, r);
		}

		/**
		 * <p>Hľadá priesečníky úsečky |AB| a kružnice so stredom
		 * S a polomerom r. Ak jestvuje jeden priesečník, metóda vráti
		 * jednoprvkové pole. Ak jestvujú dva priesečníky, metóda vráti
		 * dvojprvkové pole bodov so súradnicami priesečníkov. Ak nejestvuje
		 * žiadny priesečník, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param A poloha bodu A úsečky
		 * @param B poloha bodu B úsečky
		 * @param S poloha stredu S kružnice
		 * @param r polomer kružnice
		 * @return pole priesečníkov alebo {@code valnull}, ak nejestvujú
		 */
		public final static Bod[] priesečníkyÚsečkyAKružnice(
			Poloha A, Poloha B, Poloha S, double r)
		{
			return priesečníkyÚsečkyAKružnice(A.polohaX(), A.polohaY(),
				B.polohaX(), B.polohaY(), S.polohaX(), S.polohaY(), r);
		}

		/** <p><a class="alias"></a> Alias pre {@link #priesečníkyÚsečkyAKružnice(Poloha, Poloha, Poloha, double) priesečníkyÚsečkyAKružnice}.</p> */
		public final static Bod[] priesecnikyUseckyAKruznice(
			Poloha A, Poloha B, Poloha S, double r)
		{
			return priesečníkyÚsečkyAKružnice(A.polohaX(), A.polohaY(),
				B.polohaX(), B.polohaY(), S.polohaX(), S.polohaY(), r);
		}


		/**
		 * <p>Nájde najbližší bod na priamke určenej dvomi bodmi A[x1, y1]
		 * a B[x2, y2] k zadanému voľnému bodu V[x0, y0] a vráti jeho
		 * súradnice v objekte typu {@link Bod Bod}.</p>
		 * 
		 * <p class="image"><img src="resources/najblizsi-bod-na-priamke.svg"
		 * alt="Hľadanie najbližšieho bodu na priamke."
		 * onerror="this.onerror=null; this.src='resources/najblizsi-bod-na-priamke.png';"
		 * /><br />Grafické znázornenie možnej situácie pri hľadaní
		 * najbližšieho bodu na priamke.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie bodu… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * <p><b>Užitočný zdroj:</b></p>
		 * 
		 * <ul>
		 * <li><small><a
		 * href="https://stackoverflow.com/users/157672/michael-lloyd-lee-mlk"
		 * target="_blank">Michael Lloyd Lee</a></small>: <a
		 * href="https://stackoverflow.com/questions/1459368/snap-point-to-a-line/1459397#1459397"
		 * target="_blank"><em>Snap point to a line.</em> Stack Overflow,
		 * 2017. Citované: 2017 – 2018.</a></li>
		 * </ul>
		 * 
		 * @param x0 x-ová súradnica voľného bodu V
		 * @param y0 y-ová súradnica voľného bodu V
		 * @param x1 x-ová súradnica určujúceho bodu A priamky
		 * @param y1 y-ová súradnica určujúceho bodu A priamky
		 * @param x2 x-ová súradnica určujúceho bodu B priamky
		 * @param y2 y-ová súradnica určujúceho bodu B priamky
		 * @return objekt so súradnicami najbližšieho bodu na priamke
		 */
		public final static Bod najbližšíBodNaPriamke(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{
			double Δx = x2 - x1;
			double Δy = y2 - y1;

			double c1 = Δy * x1 - Δx * y1;
			double c2 = Δy * y0 + Δx * x0;

			double determinant = Δy * Δy + Δx * Δx;

			if (determinant != 0)
				return new Bod(
					(Δy * c1 + Δx * c2) / determinant,
					(Δy * c2 - Δx * c1) / determinant);

			return new Bod(x0, y0);
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaPriamke(double, double, double, double, double, double) najbližšíBodNaPriamke}.</p> */
		public final static Bod najblizsiBodNaPriamke(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{
			return najbližšíBodNaPriamke(x0, y0, x1, y1, x2, y2);
		}

		/**
		 * <p>Nájde najbližší bod na priamke |AB| k zadanému voľnému bodu
		 * V a vráti jeho súradnice v objekte typu {@link Bod Bod}.</p>
		 * 
		 * @param V poloha voľného bodu V
		 * @param A poloha určujúceho bodu A priamky
		 * @param B poloha určujúceho bodu B priamky
		 * @return objekt so súradnicami najbližšieho bodu na priamke
		 */
		public final static Bod najbližšíBodNaPriamke(
			Poloha V, Poloha A, Poloha B)
		{
			return najbližšíBodNaPriamke(V.polohaX(), V.polohaY(),
				A.polohaX(), A.polohaY(), B.polohaX(), B.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaPriamke(Poloha, Poloha, Poloha) najbližšíBodNaPriamke}.</p> */
		public final static Bod najblizsiBodNaPriamke(
			Poloha V, Poloha A, Poloha B)
		{
			return najbližšíBodNaPriamke(V.polohaX(), V.polohaY(),
				A.polohaX(), A.polohaY(), B.polohaX(), B.polohaY());
		}

		/**
		 * <p>Nájde najbližší bod na priamke |AB| k zadanému voľnému bodu
		 * V a vráti jeho súradnice v objekte typu {@link Bod Bod}. Bod
		 * a priamka sú určené polohami bodov v poli parametra, ktoré musí
		 * obsahovať aspoň tri prvky. Prvý prvok určuje bod V a ďalšie dva
		 * prvky body A a B.</p>
		 * 
		 * @param poleBodov polohy bodov určujúce bod V a priamku A, B
		 * @return objekt so súradnicami najbližšieho bodu na priamke
		 */
		public final static Bod najbližšíBodNaPriamke(Poloha[] poleBodov)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/) return null;

			return najbližšíBodNaPriamke(
				poleBodov[0].polohaX(), poleBodov[0].polohaY(),
				poleBodov[1].polohaX(), poleBodov[1].polohaY(),
				poleBodov[2].polohaX(), poleBodov[2].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaPriamke(Poloha[]) najbližšíBodNaPriamke}.</p> */
		public final static Bod najblizsiBodNaPriamke(Poloha[] poleBodov)
		{ return najbližšíBodNaPriamke(poleBodov); }


		/**
		 * <p>Nájde najbližší bod na úsečke určenej dvomi bodmi A[x1, y1]
		 * a B[x2, y2] k zadanému voľnému bodu V[x0, y0] a vráti jeho
		 * súradnice v objekte typu {@link Bod Bod}.</p>
		 * 
		 * <p class="image"><img src="resources/najblizsi-bod-na-usecke.svg"
		 * alt="Hľadanie najbližšieho bodu na úsečke."
		 * onerror="this.onerror=null; this.src='resources/najblizsi-bod-na-usecke.png';"
		 * /><br />Grafické znázornenie možných situácií pri hľadaní
		 * najbližšieho bodu na úsečke.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie bodu… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * @param x0 x-ová súradnica voľného bodu V
		 * @param y0 y-ová súradnica voľného bodu V
		 * @param x1 x-ová súradnica krajného bodu A úsečky
		 * @param y1 y-ová súradnica krajného bodu A úsečky
		 * @param x2 x-ová súradnica krajného bodu B úsečky
		 * @param y2 y-ová súradnica krajného bodu B úsečky
		 * @return objekt so súradnicami najbližšieho bodu na úsečke
		 */
		public final static Bod najbližšíBodNaÚsečke(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{
			if (x1 == x2 && y1 == y2) return new Bod(x1, y1);

			double Δxa = x2 - x1;
			double Δya = y2 - y1;

			// double Δxb = x0 - x1;
			// double Δyb = y0 - y1;

			// double t = (Δxa * Δxb + Δya * Δyb) / (Δxa * Δxa + Δya * Δya);

			double t =
				(Δxa * (x0 - x1) + Δya * (y0 - y1)) / (Δxa * Δxa + Δya * Δya);

			if (t <= 0.0) // t = 0.0;
				return new Bod(x1, y1);
			else if (t >= 1.0) // t = 1.0;
				return new Bod(x2, y2);

			return new Bod(x1 + Δxa * t, y1 + Δya * t);
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaÚsečke(double, double, double, double, double, double) najbližšíBodNaÚsečke}.</p> */
		public final static Bod najblizsiBodNaUsecke(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{
			return najbližšíBodNaÚsečke(x0, y0, x1, y1, x2, y2);
		}

		/**
		 * <p>Nájde najbližší bod na úsečke |AB| k zadanému voľnému bodu
		 * V a vráti jeho súradnice v objekte typu {@link Bod Bod}.</p>
		 * 
		 * @param V poloha voľného bodu V
		 * @param A poloha krajného bodu A úsečky
		 * @param B poloha krajného bodu B úsečky
		 * @return objekt so súradnicami najbližšieho bodu na úsečke
		 */
		public final static Bod najbližšíBodNaÚsečke(
			Poloha V, Poloha A, Poloha B)
		{
			return najbližšíBodNaÚsečke(V.polohaX(), V.polohaY(),
				A.polohaX(), A.polohaY(), B.polohaX(), B.polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaÚsečke(Poloha, Poloha, Poloha) najbližšíBodNaÚsečke}.</p> */
		public final static Bod najblizsiBodNaUsecke(
			Poloha V, Poloha A, Poloha B)
		{
			return najbližšíBodNaÚsečke(V.polohaX(), V.polohaY(),
				A.polohaX(), A.polohaY(), B.polohaX(), B.polohaY());
		}

		/**
		 * <p>Nájde najbližší bod na úsečke |AB| k zadanému voľnému bodu
		 * V a vráti jeho súradnice v objekte typu {@link Bod Bod}. Bod
		 * a úsečka sú určené polohami bodov v poli parametra, ktoré musí
		 * obsahovať aspoň tri prvky. Prvý prvok určuje bod V a ďalšie dva
		 * prvky body A a B.</p>
		 * 
		 * @param poleBodov polohy bodov určujúce bod V a úsečku A, B
		 * @return objekt so súradnicami najbližšieho bodu na úsečke
		 */
		public final static Bod najbližšíBodNaÚsečke(Poloha[] poleBodov)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/) return null;

			return najbližšíBodNaÚsečke(
				poleBodov[0].polohaX(), poleBodov[0].polohaY(),
				poleBodov[1].polohaX(), poleBodov[1].polohaY(),
				poleBodov[2].polohaX(), poleBodov[2].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaÚsečke(Poloha[]) najbližšíBodNaÚsečke}.</p> */
		public final static Bod najblizsiBodNaUsecke(Poloha[] poleBodov)
		{ return najbližšíBodNaÚsečke(poleBodov); }


		/**
		 * <p>Nájde najbližší bod na kružnici so stredom S[x1, y1]
		 * a polomerom r k zadanému voľnému bodu V[x0, y0] a vráti jeho
		 * polohu v objekte typu {@link Bod Bod}. Úloha je neriešiteľná,
		 * ak sa voľný bod V nachádza v strede kružnice, pretože od stredu
		 * kružnice sú rovnako vzdialené všetky jej body. Ak nastane takáto
		 * situácia, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p class="image"><img src="resources/najblizsi-bod-na-kruznici.svg"
		 * alt="Hľadanie najbližšieho bodu na kružnici."
		 * onerror="this.onerror=null; this.src='resources/najblizsi-bod-na-kruznici.png';"
		 * /><br />Grafické znázornenie hľadania najbližšieho bodu na
		 * kružnici.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie bodu… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>«názov».png<alt/></image>«Popis…»<!-- TODO -->.</p>
		 * 
		 * <!-- TODO dokončiť opis. -->
		 * 
		 * @param x0 x-ová súradnica voľného bodu
		 * @param y0 y-ová súradnica voľného bodu
		 * @param x1 x-ová súradnica stredu kružnice
		 * @param y1 y-ová súradnica stredu kružnice
		 * @param r polomer kružnice
		 * @return poloha najbližšieho bodu na kružnici (v špeciálnom
		 *     prípade {@code valnull})
		 */
		public final static Bod najbližšíBodNaKružnici(
			double x0, double y0, double x1, double y1, double r)
		{
			if (x0 == x1 && y0 == y1) return null;
			double Δx = x0 - x1, Δy = y0 - y1;
			double t = r / Math.sqrt(Δx * Δx + Δy * Δy);
			return new Bod(x1 + Δx * t, y1 + Δy * t);
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaKružnici(double, double, double, double, double) najbližšíBodNaKružnici}.</p> */
		public final static Bod najblizsiBodNaKruznici(
			double x0, double y0, double x1, double y1, double r)
		{ return najbližšíBodNaKružnici(x0, y0, x1, y1, r); }

		/**
		 * <p>Nájde najbližší bod na kružnici so stredom S a polomerom
		 * r k zadanému voľnému bodu V a vráti jeho polohu v objekte typu
		 * {@link Bod Bod}. Úloha je neriešiteľná, ak sa voľný bod V nachádza
		 * v strede kružnice. Ak nastane takáto situácia, tak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * @param V poloha voľného bodu
		 * @param S poloha stredu kružnice
		 * @param r polomer kružnice
		 * @return poloha najbližšieho bodu na kružnici (v špeciálnom
		 *     prípade {@code valnull})
		 */
		public final static Bod najbližšíBodNaKružnici(
			Poloha V, Poloha S, double r)
		{
			return najbližšíBodNaKružnici(V.polohaX(), V.polohaY(),
				S.polohaX(), S.polohaY(), r);
		}

		/** <p><a class="alias"></a> Alias pre {@link #najbližšíBodNaKružnici(Poloha, Poloha, double) najbližšíBodNaKružnici}.</p> */
		public final static Bod najblizsiBodNaKruznici(
			Poloha V, Poloha S, double r)
		{
			return najbližšíBodNaKružnici(V.polohaX(), V.polohaY(),
				S.polohaX(), S.polohaY(), r);
		}


		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi bodmi so súradnicami [x1, y1]
		 * a [x2, y2]. Táto metóda volá metódu jazyka Java {@link 
		 * Point2D#distance(double, double, double, double)
		 * Point2D.distance(x1, y1, x2, y2)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V skutočnosti by sme nemali
		 * hovoriť, že ide o „metódu jazyka Java.“ Je to statická metóda
		 * triedy {@link Point2D Point2D} štandardného balíčka <a
		 * href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/package-summary.html"
		 * target="_blank"><code>java.awt.geom</code> jazyka Java (a aj na
		 * tomto opise by sa dalo ešte niečo spresňovať), ale z dôvodu
		 * pedagogickej transformácie ponechávame v opise zjednodušené
		 * označenie.</a></p>
		 * 
		 * <p><b>Zdroj:</b></p>
		 * 
		 * <ul><li><a href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Point2D.html#distance-double-double-double-double-"
		 * target="_blank"><em>Distance of Point2D (Java Platform SE 8).</em>
		 * Oracle. Citované: 2016 – 2018.</a></li></ul>
		 * 
		 * @param x1 x-ová súradnica prvého bodu
		 * @param y1 y-ová súradnica prvého bodu
		 * @param x2 x-ová súradnica druhého bodu
		 * @param y2 y-ová súradnica druhého bodu
		 * @return vzdialenosť medzi určenými bodmi
		 */
		public final static double vzdialenosťBodov(
			double x1, double y1, double x2, double y2)
		{ return Point2D.distance(x1, y1, x2, y2); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBodov(double, double, double, double) vzdialenosťBodov}.</p> */
		public final static double vzdialenostBodov(
			double x1, double y1, double x2, double y2)
		{ return Point2D.distance(x1, y1, x2, y2); }

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi bodmi A a B. Pozri aj opis
		 * metódy {@link #vzdialenosťBodov(double, double, double, double)
		 * vzdialenosťBodov}, ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param A poloha prvého bodu
		 * @param B poloha druhého bodu
		 * @return vzdialenosť medzi určenými bodmi
		 */
		public final static double vzdialenosťBodov(Poloha A, Poloha B)
		{ return Point2D.distance(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBodov(Poloha, Poloha) vzdialenosťBodov}.</p> */
		public final static double vzdialenostBodov(Poloha A, Poloha B)
		{ return Point2D.distance(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY()); }

		/**
		 * Vypočíta vzdialenosť medzi dvomi bodmi so súradnicami určenými
		 * polohami bodov v poli parametra. Pole musí obsahovať aspoň dva
		 * prvky. (V prípade vyššieho počtu prvkov sú brané do úvahy len
		 * prvé dva. Ostatné sú ignorované.) Pozri aj opis metódy {@link 
		 * #vzdialenosťBodov(double, double, double, double)
		 * vzdialenosťBodov}, ktorej správanie táto metóda kopíruje.
		 * 
		 * @param poleBodov polohy bodov
		 * @return vzdialenosť medzi určenými bodmi (prípadne hodnota
		 *     {@link Double#NaN Double.NaN} – v prípade chyby)
		 */
		public final static double vzdialenosťBodov(Poloha[] poleBodov)
		{
			if (null == poleBodov || 2 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1]*/)
			return Double.NaN;

			return Point2D.distance(
				poleBodov[0].polohaX(), poleBodov[0].polohaY(),
				poleBodov[1].polohaX(), poleBodov[1].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBodov(Poloha[]) vzdialenosťBodov}.</p> */
		public final static double vzdialenostBodov(Poloha[] poleBodov)
		{ return vzdialenosťBodov(poleBodov); }


		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V[x0, y0]
		 * k priamke určenej dvomi bodmi A[x1, y1] a B[x2, y2]. (Ak bod
		 * leží na priamke, tak je vzdialenosť rovná nule.) Táto metóda
		 * volá metódu jazyka Java {@link Line2D#ptLineDist(double, double,
		 * double, double, double, double) Line2D.ptLineDist(x1, y1, x2, y2,
		 * x0, y0)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V skutočnosti by sme nemali
		 * hovoriť, že ide o „metódu jazyka Java.“ Je to statická metóda
		 * triedy {@link Line2D Line2D} štandardného balíčka <a
		 * href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/package-summary.html"
		 * target="_blank"><code>java.awt.geom</code> jazyka Java (a aj na
		 * tomto opise by sa dalo ešte niečo spresňovať), ale z dôvodu
		 * pedagogickej transformácie ponechávame v opise zjednodušené
		 * označenie.</a></p>
		 * 
		 * <p class="image"><img src="resources/vzdialenost-bodu-od-priamky.svg"
		 * alt="Určovanie vzdialenosti bodu od priamky."
		 * onerror="this.onerror=null; this.src='resources/vzdialenost-bodu-od-priamky.png';"
		 * /><br />Grafické znázornenie možnej situácie pri určovaní
		 * vzdialenosti bodu od priamky.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie bodu… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><b>Zdroj:</b></p>
		 * 
		 * <ul><li><a href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Line2D.html#ptLineDist-double-double-double-double-double-double-"
		 * target="_blank"><em>Point to line distance of Line2D (Java
		 * Platform SE 8).</em> Oracle. Citované: 2016 – 2018.</a></li></ul>
		 * 
		 * @param x0 x-ová súradnica voľného bodu
		 * @param y0 y-ová súradnica voľného bodu
		 * @param x1 x-ová súradnica určujúceho bodu A priamky
		 * @param y1 y-ová súradnica určujúceho bodu A priamky
		 * @param x2 x-ová súradnica určujúceho bodu B priamky
		 * @param y2 y-ová súradnica určujúceho bodu B priamky
		 * @return vzdialenosť bodu od priamky
		 */
		public final static double vzdialenosťBoduOdPriamky(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{ return Line2D.ptLineDist(x1, y1, x2, y2, x0, y0); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdPriamky(double, double, double, double, double, double) vzdialenosťBoduOdPriamky}.</p> */
		public final static double vzdialenostBoduOdPriamky(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{ return Line2D.ptLineDist(x1, y1, x2, y2, x0, y0); }

		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V k priamke |AB|.
		 * Pozri aj opis metódy {@link #vzdialenosťBoduOdPriamky(double,
		 * double, double, double, double, double) vzdialenosťBoduOdPriamky},
		 * ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param V poloha voľného bodu
		 * @param A poloha určujúceho bodu A priamky
		 * @param B poloha určujúceho bodu B priamky
		 * @return vzdialenosť bodu od priamky
		 */
		public final static double vzdialenosťBoduOdPriamky(
			Poloha V, Poloha A, Poloha B)
		{ return Line2D.ptLineDist(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY(), V.polohaX(), V.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdPriamky(Poloha, Poloha, Poloha) vzdialenosťBoduOdPriamky}.</p> */
		public final static double vzdialenostBoduOdPriamky(
			Poloha V, Poloha A, Poloha B)
		{ return Line2D.ptLineDist(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY(), V.polohaX(), V.polohaY()); }

		/**
		 * <p>Vypočíta vzdialenosť medzi voľným bodom a priamkou, ktoré
		 * sú určené určenými polohami bodov v poli parametra. Pole musí
		 * obsahovať aspoň tri prvky. Prvý prvok obsahuje súradnice voľného
		 * bodu a ďalšie dva prvky určujúce body priamky. Pozri aj opis
		 * metódy {@link #vzdialenosťBoduOdPriamky(double, double, double,
		 * double, double, double) vzdialenosťBoduOdPriamky}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy voľného bodu a určujúcich bodov priamky
		 * @return vzdialenosť bodu od priamky (prípadne hodnota
		 *     {@link Double#NaN Double.NaN} – v prípade chyby)
		 */
		public final static double vzdialenosťBoduOdPriamky(Poloha[] poleBodov)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/) return Double.NaN;

			return Line2D.ptLineDist(
				poleBodov[1].polohaX(), poleBodov[1].polohaY(),
				poleBodov[2].polohaX(), poleBodov[2].polohaY(),
				poleBodov[0].polohaX(), poleBodov[0].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdPriamky(double, double, double, double, double, double) vzdialenosťBoduOdPriamky}.</p> */
		public final static double vzdialenostBoduOdPriamky(
			Poloha[] poleBodov)
		{ return vzdialenosťBoduOdPriamky(poleBodov); }


		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V[x0, y0]
		 * k úsečke určenej dvomi bodmi A[x1, y1] a B[x2, y2]. (Ak bod
		 * leží na úsečke, tak je vzdialenosť rovná nule.) Táto metóda
		 * volá metódu jazyka Java {@link Line2D#ptSegDist(double, double,
		 * double, double, double, double) Line2D.ptSegDist(x1, y1, x2, y2,
		 * x0, y0)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V skutočnosti by sme nemali
		 * hovoriť, že ide o „metódu jazyka Java.“ Je to statická metóda
		 * triedy {@link Line2D Line2D} štandardného balíčka <a
		 * href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/package-summary.html"
		 * target="_blank"><code>java.awt.geom</code></a> jazyka Java (a aj
		 * na tomto opise by sa dalo ešte niečo spresňovať), ale z dôvodu
		 * pedagogickej transformácie ponechávame v opise zjednodušené
		 * označenie. Podobne postupujeme na viacerých miestach tejto
		 * dokumentácie.</p>
		 * 
		 * <p class="image"><img src="resources/vzdialenost-bodu-od-usecky.svg"
		 * alt="Určovanie vzdialenosti bodu od úsečky."
		 * onerror="this.onerror=null; this.src='resources/vzdialenost-bodu-od-usecky.png';"
		 * /><br />Grafické znázornenie možnej situácie pri určovaní
		 * vzdialenosti bodu od úsečky.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			«príklad – ospravedlňujeme sa, pracujeme na doplnení…»
			<!-- TODO – nájdenie a grafické znázornenie bodu… -->
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><b>Zdroj:</b></p>
		 * 
		 * <ul><li><a href="https://docs.oracle.com/javase/8/docs/api/java/awt/geom/Line2D.html#ptSegDist-double-double-double-double-double-double-"
		 * target="_blank"><em>Point to line segment distance of Line2D (Java
		 * Platform SE 8).</em> Oracle. Citované: 2016 – 2018.</a></li></ul>
		 * 
		 * @param x0 x-ová súradnica voľného bodu
		 * @param y0 y-ová súradnica voľného bodu
		 * @param x1 x-ová súradnica určujúceho bodu A úsečky
		 * @param y1 y-ová súradnica určujúceho bodu A úsečky
		 * @param x2 x-ová súradnica určujúceho bodu B úsečky
		 * @param y2 y-ová súradnica určujúceho bodu B úsečky
		 * @return vzdialenosť bodu od úsečky
		 */
		public final static double vzdialenosťBoduOdÚsečky(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{ return Line2D.ptSegDist(x1, y1, x2, y2, x0, y0); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdÚsečky(double, double, double, double, double, double) vzdialenosťBoduOdÚsečky}.</p> */
		public final static double vzdialenostBoduOdUsecky(
			double x0, double y0, double x1, double y1, double x2, double y2)
		{ return Line2D.ptSegDist(x1, y1, x2, y2, x0, y0); }

		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V k úsečke |AB|.
		 * Pozri aj opis metódy {@link #vzdialenosťBoduOdÚsečky(double,
		 * double, double, double, double, double) vzdialenosťBoduOdÚsečky},
		 * ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param V poloha voľného bodu
		 * @param A poloha určujúceho bodu A úsečky
		 * @param B poloha určujúceho bodu B úsečky
		 * @return vzdialenosť bodu od úsečky
		 */
		public final static double vzdialenosťBoduOdÚsečky(
			Poloha V, Poloha A, Poloha B)
		{ return Line2D.ptSegDist(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY(), V.polohaX(), V.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdÚsečky(Poloha, Poloha, Poloha) vzdialenosťBoduOdÚsečky}.</p> */
		public final static double vzdialenostBoduOdUsecky(
			Poloha V, Poloha A, Poloha B)
		{ return Line2D.ptSegDist(A.polohaX(), A.polohaY(),
			B.polohaX(), B.polohaY(), V.polohaX(), V.polohaY()); }

		/**
		 * <p>Vypočíta vzdialenosť medzi voľným bodom V a úsečkou |AB|, ktoré
		 * sú určené polohami bodov v poli parametra {@code poleBodov}. Pole
		 * musí obsahovať aspoň tri prvky. Prvý prvok obsahuje súradnice
		 * voľného bodu V a ďalšie dva prvky určujúce body úsečky |AB|. Pozri
		 * aj opis metódy {@link #vzdialenosťBoduOdÚsečky(double, double,
		 * double, double, double, double) vzdialenosťBoduOdÚsečky}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy voľného bodu a určujúcich bodov úsečky
		 * @return vzdialenosť bodu od úsečky (prípadne hodnota
		 *     {@link Double#NaN Double.NaN} – v prípade chyby)
		 */
		public final static double vzdialenosťBoduOdÚsečky(Poloha[] poleBodov)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/) return Double.NaN;

			return Line2D.ptSegDist(
				poleBodov[1].polohaX(), poleBodov[1].polohaY(),
				poleBodov[2].polohaX(), poleBodov[2].polohaY(),
				poleBodov[0].polohaX(), poleBodov[0].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdÚsečky(Poloha[]) vzdialenosťBoduOdÚsečky}.</p> */
		public final static double vzdialenostBoduOdUsecky(
			Poloha[] poleBodov)
		{ return vzdialenosťBoduOdÚsečky(poleBodov); }


		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V ku kružnici
		 * určenej stredom S a polomerom r. Tento výpočet je v skutočnosti
		 * technicky veľmi jednoduchý. Stačí vypočítať vzdialenosť bodu
		 * od stredu kružnice a odpočítať polomer kružnice. Na výpočet
		 * vzdialenosti je použitá metóda {@link Math#hypot(double, double)
		 * Math.hypot(x, y)}.</p>
		 * 
		 * <p>Zopár úvah na kontrolu správnosti: Ak bod leží na kružnici,
		 * jeho vzdialenosť od stredu kružnice sa rovná polomeru kružnice,
		 * čiže vzdialenosť od kružnice je nulová. Ak bod leží vovnútri
		 * kružnice, tak bez použitia absolútnej hodnoty vyjde vzdialenosť
		 * záporná, čo je technicky nezmysel, ale na rýchle odlíšenie tejto
		 * situácie (a tiež na zjednodušenie pouźitia pri niektorých
		 * algoritmoch) je tejto metóde ponechaná schopnosť vracania zápornej
		 * vzdialenosti. Podobný prístup volia viaceré metódy programovacieho
		 * rámca.</p>
		 * 
		 * <!-- TODO pridať príklad použitia a/alebo obrázok. -->
		 * 
		 * <p>Parametre metódy určujú: V[x0, y0] – voľný bod; S[x1, y1] –
		 * stred kružnice; r – polomer kružnice.</p>
		 * 
		 * @param x0 x-ová súradnica voľného bodu
		 * @param y0 y-ová súradnica voľného bodu
		 * @param x1 x-ová súradnica stredu S kružnice
		 * @param y1 y-ová súradnica stredu S kružnice
		 * @param r polomer kružnice
		 * @return vzdialenosť bodu od kružnice; záporná hodnota signalizuje,
		 *     že bod sa nachádza vovnútri kružnice
		 */
		public final static double vzdialenosťBoduOdKružnice(
			double x0, double y0, double x1, double y1, double r)
		{ return Math.hypot(x1 - x0, y1 - y0) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdKružnice(double, double, double, double, double) vzdialenosťBoduOdKružnice}.</p> */
		public final static double vzdialenostBoduOdKruznice(
			double x0, double y0, double x1, double y1, double r)
		{ return Math.hypot(x1 - x0, y1 - y0) - r; }

		/**
		 * <p>Vypočíta vzdialenosť od zadaného voľného bodu V ku kružnici
		 * určenej stredom S a polomerom r. Pozri aj opis metódy {@link 
		 * #vzdialenosťBoduOdKružnice(double, double, double, double, double)
		 * vzdialenosťBoduOdKružnice}, ktorej správanie táto metóda
		 * kopíruje.</p>
		 * 
		 * @param V poloha voľného bodu
		 * @param S poloha stredu kružnice
		 * @param r polomer kružnice
		 * @return vzdialenosť bodu od úsečky; záporná hodnota signalizuje,
		 *     že bod sa nachádza vovnútri kružnice
		 */
		public final static double vzdialenosťBoduOdKružnice(
			Poloha V, Poloha S, double r)
		{ return Math.hypot(S.polohaX() - V.polohaX(),
			S.polohaY() - V.polohaY()) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdKružnice(Poloha, Poloha, double) vzdialenosťBoduOdKružnice}.</p> */
		public final static double vzdialenostBoduOdKruznice(
			Poloha V, Poloha S, double r)
		{ return Math.hypot(S.polohaX() - V.polohaX(),
			S.polohaY() - V.polohaY()) - r; }

		/**
		 * <p>Vypočíta vzdialenosť medzi voľným bodom V a kružnicou, ktorá
		 * je určená stredom S a polomerom r, pričom body V a S sú prvkami
		 * poľa {@code poleBodov} a r je uložený v parametri {@code polomer}).
		 * Z uvedeného vyplýva, že pole musí obsahovať aspoň dva prvky. Prvý
		 * uchováva súradnice voľného bodu V a druhý stredu kružnice S.
		 * (Posledný parameter {@code polomer} určuje polomer kružnice.) Pozri
		 * aj opis metódy {@link #vzdialenosťBoduOdKružnice(double,
		 * double, double, double, double) vzdialenosťBoduOdKružnice},
		 * ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy voľného bodu a stredu kružnice
		 * @param polomer polomer kružnice
		 * @return vzdialenosť bodu od kružnice (prípadne hodnota
		 *     {@link Double#NaN Double.NaN} – v prípade chyby); záporná
		 *     hodnota signalizuje, že bod sa nachádza vovnútri kružnice
		 */
		public final static double vzdialenosťBoduOdKružnice(
			Poloha[] poleBodov, double polomer)
		{
			if (null == poleBodov || 2 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1]*/)
				return Double.NaN;

			return Math.hypot(
				poleBodov[1].polohaX() - poleBodov[0].polohaX(),
				poleBodov[1].polohaY() - poleBodov[0].polohaY()) - polomer;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťBoduOdKružnice(Poloha[], double) vzdialenosťBoduOdKružnice}.</p> */
		public final static double vzdialenostBoduOdKruznice(
			Poloha[] poleBodov, double polomer)
		{ return vzdialenosťBoduOdKružnice(poleBodov, polomer); }


		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi kružnicami, ktoré sú určené
		 * svojími stredmi (S1, S2) a polomermi (r1, r2). Ak majú kružnice
		 * prienik, tak je vzdialenosť záporná. (Ak sa dotýkajú v jedinom
		 * bode, tak je nulová.)</p>
		 * 
		 * <!-- TODO pridať príklad použitia a/alebo obrázok. -->
		 * 
		 * <p>Parametre metódy určujú: S1[x1, y1] – stred prvej kružnice;
		 * r1 – polomer prvej kružnice; S2[x2, y2] – stred druhej kružnice;
		 * r2 – polomer druhej kružnice.</p>
		 * 
		 * @param x1 x-ová súradnica stredu S1 (prvej) kružnice
		 * @param y1 y-ová súradnica stredu S1 (prvej) kružnice
		 * @param r1 polomer prvej kružnice
		 * @param x2 x-ová súradnica stredu S2 (druhej) kružnice
		 * @param y2 y-ová súradnica stredu S2 (druhej) kružnice
		 * @param r2 polomer druhej kružnice
		 * @return vzdialenosť kružníc; záporná hodnota signalizuje,
		 *     že kružnice majú spoločný viac, než jeden bod (prekrývajú sa)
		 */
		public final static double vzdialenosťKružníc(
			double x1, double y1, double r1, double x2, double y2, double r2)
		{ return Math.hypot(x1 - x2, y1 - y2) - r1 - r2; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťKružníc(double, double, double, double, double, double) vzdialenosťKružníc}.</p> */
		public final static double vzdialenostKruznic(
			double x1, double y1, double r1, double x2, double y2, double r2)
		{ return Math.hypot(x1 - x2, y1 - y2) - r1 - r2; }

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi kružnicami, ktoré sú určené
		 * svojími stredmi (S1, S2) a polomermi (r1, r2). Pozri aj opis metódy
		 * {@link #vzdialenosťKružníc(double, double, double, double, double,
		 * double) vzdialenosťKružníc}, ktorej správanie táto metóda
		 * kopíruje.</p>
		 * 
		 * @param S1 poloha stredu prvej kružnice
		 * @param r1 polomer prvej kružnice
		 * @param S2 poloha stredu druhej kružnice
		 * @param r2 polomer druhej kružnice
		 * @return vzdialenosť kružníc; záporná hodnota signalizuje, že
		 *     kružnice majú spoločný viac, než jeden bod (prekrývajú sa)
		 */
		public final static double vzdialenosťKružníc(
			Poloha S1, double r1, Poloha S2, double r2)
		{ return Math.hypot(S2.polohaX() - S1.polohaX(),
			S2.polohaY() - S1.polohaY()) - r1 - r2; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťKružníc(Poloha, double, Poloha, double) vzdialenosťKružníc}.</p> */
		public final static double vzdialenostKruznic(
			Poloha S1, double r1, Poloha S2, double r2)
		{ return Math.hypot(S2.polohaX() - S1.polohaX(),
			S2.polohaY() - S1.polohaY()) - r1 - r2; }

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi kružnicami určenými stredmi
		 * S1 a S2 a polomermi r1 a r2, pričom stredy S1 a S2 sú prvkami
		 * poľa {@code poleBodov}. To znamená, že pole musí obsahovať aspoň
		 * dva prvky. Prvý obsahuje súradnice stredu prvej kružnice S1 a druhý
		 * druhej kružnice S2. Parametre r1 a r2 určujú polomery kružníc.
		 * Pozri aj opis metódy {@link #vzdialenosťKružníc(double,
		 * double, double, double, double, double) vzdialenosťKružníc},
		 * ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy stredov kružníc
		 * @param polomer1 polomer prvej kružnice
		 * @param polomer2 polomer druhej kružnice
		 * @return vzdialenosť kružníc (prípadne hodnota {@link Double#NaN
		 *     Double.NaN} – v prípade chyby); záporná hodnota signalizuje,
		 *     že kružnice majú spoločný viac, než jeden bod (prekrývajú sa)
		 */
		public final static double vzdialenosťKružníc(
			Poloha[] poleBodov, double polomer1, double polomer2)
		{
			if (null == poleBodov || 2 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1]*/)
				return Double.NaN;

			return Math.hypot(
				poleBodov[1].polohaX() - poleBodov[0].polohaX(),
				poleBodov[1].polohaY() - poleBodov[0].polohaY()) -
				polomer1 - polomer2;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťKružníc(Poloha[], double, double) vzdialenosťKružníc}.</p> */
		public final static double vzdialenostKruznic(
			Poloha[] poleBodov, double polomer1, double polomer2)
		{ return vzdialenosťKružníc(poleBodov, polomer1, polomer2); }


		// TODO – správne zaradiť:
		// Zdroj: http://www.vb-helper.com/howto_distance_segment_to_segment.html
		// Pozri tiež: http://www.geometrictools.com/Documentation/DistanceLine3Line3.pdf
		//     Uložené do: DistanceLine3Line3.pdf (TODO: dať kópiu k dispozícii)

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi úsečkami |AB| a |CD|. Ak sa
		 * úsečky pretínajú, tak je vzdialenosť nulová. V opačnom prípade
		 * metóda nájde najbližšie body úsečiek a vypočíta vzdialenosť medzi
		 * nimi.</p>
		 * 
		 * <!-- TODO pridať príklad použitia a/alebo obrázok. -->
		 * 
		 * <p>Parametre metódy vyjadrujú: A[x1, y1] – B[x2, y2] – krajné body
		 * prvej úsečky; C[x3, y3] – D[x4, y4] – krajné body druhej úsečky.</p>
		 * 
		 * @param x1 x-ová súradnica určujúceho bodu A prvej úsečky
		 * @param y1 y-ová súradnica určujúceho bodu A prvej úsečky
		 * @param x2 x-ová súradnica určujúceho bodu B prvej úsečky
		 * @param y2 y-ová súradnica určujúceho bodu B prvej úsečky
		 * @param x3 x-ová súradnica určujúceho bodu C druhej úsečky
		 * @param y3 y-ová súradnica určujúceho bodu C druhej úsečky
		 * @param x4 x-ová súradnica určujúceho bodu D druhej úsečky
		 * @param y4 y-ová súradnica určujúceho bodu D druhej úsečky
		 * @return vzájomná vzdialenosť dvoch úsečiek
		 */
		public final static double vzdialenosťÚsečiek(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4)
		{
			// Test toho, či sa úsečky [x1, y1] – [x2, y2]
			// a [x3, y3] – [x4, y4] pretínajú:
			if (Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4))
				return 0.0;

			// Vypočíta vzdialenosť od bodu [x0, y0] k úsečke
			// [x1, y1] – [x2, y2]: Line2D.ptSegDist(x1, y1, x2, y2, x0, y0);

			double vzdialenosť = Line2D.ptSegDist(x1, y1, x2, y2, x3, y3);
			double porovnaj = Line2D.ptSegDist(x1, y1, x2, y2, x4, y4);
			if (vzdialenosť > porovnaj) vzdialenosť = porovnaj;
			porovnaj = Line2D.ptSegDist(x3, y3, x4, y4, x1, y1);
			if (vzdialenosť > porovnaj) vzdialenosť = porovnaj;
			porovnaj = Line2D.ptSegDist(x3, y3, x4, y4, x2, y2);
			if (vzdialenosť > porovnaj) vzdialenosť = porovnaj;

			return vzdialenosť;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečiek(double, double, double, double, double, double, double, double) vzdialenosťÚsečiek}.</p> */
		public final static double vzdialenostUseciek(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4)
		{ return vzdialenosťÚsečiek(x1, y1, x2, y2, x3, y3, x4, y4); }

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi úsečkami |AB| a |CD|. Ak sa
		 * úsečky pretínajú, tak je vzdialenosť nulová. V opačnom prípade
		 * metóda nájde najbližšie body úsečiek a vypočíta vzdialenosť medzi
		 * nimi. Pozri aj opis metódy {@link #vzdialenosťÚsečiek(double,
		 * double, double, double, double, double, double, double)
		 * vzdialenosťÚsečiek}, ktorej správanie táto metóda kopíruje.</p>
		 * 
		 * @param A poloha určujúceho bodu A prvej úsečky
		 * @param B poloha určujúceho bodu B prvej úsečky
		 * @param C poloha určujúceho bodu C druhej úsečky
		 * @param D poloha určujúceho bodu D druhej úsečky
		 * @return vzájomná vzdialenosť dvoch úsečiek
		 */
		public final static double vzdialenosťÚsečiek(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{ return vzdialenosťÚsečiek(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), C.polohaX(), C.polohaY(), D.polohaX(), D.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečiek(Poloha, Poloha, Poloha, Poloha) vzdialenosťÚsečiek}.</p> */
		public final static double vzdialenostUseciek(
			Poloha A, Poloha B, Poloha C, Poloha D)
		{ return vzdialenosťÚsečiek(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), C.polohaX(), C.polohaY(), D.polohaX(), D.polohaY()); }

		/**
		 * <p>Vypočíta vzdialenosť medzi dvomi úsečkami |AB| a |CD|, pričom
		 * ich určujúce body sú prvkami poľa {@code poleBodov}. To znamená,
		 * že pole musí obsahovať aspoň štyri prvky. Prvé dva prvky sú
		 * určujúce body prvej úsečky (A, B) a ďalšie dva druhej (C, D).
		 * Ak sa úsečky pretínajú, tak je vzdialenosť nulová. V opačnom
		 * prípade metóda nájde najbližšie body úsečiek a vypočíta
		 * vzdialenosť medzi nimi. Pozri aj opis metódy {@link 
		 * #vzdialenosťÚsečiek(double, double, double, double, double,
		 * double, double, double) vzdialenosťÚsečiek}, ktorej správanie
		 * táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy určujúcich bodov úsečiek
		 * @return vzájomná vzdialenosť dvoch úsečiek (prípadne hodnota
		 *     {@link Double#NaN Double.NaN} – v prípade chyby)
		 */
		public final static double vzdialenosťÚsečiek(Poloha[] poleBodov)
		{
			if (null == poleBodov || 4 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2] || null == poleBodov[3]*/)
				return Double.NaN;

			return vzdialenosťÚsečiek(poleBodov[0].polohaX(),
				poleBodov[0].polohaY(), poleBodov[1].polohaX(),
				poleBodov[1].polohaY(), poleBodov[2].polohaX(),
				poleBodov[2].polohaY(), poleBodov[3].polohaX(),
				poleBodov[3].polohaY());
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečiek(Poloha[]) vzdialenosťÚsečiek}.</p> */
		public final static double vzdialenostUseciek(Poloha[] poleBodov)
		{ return vzdialenosťÚsečiek(poleBodov); }

		// Koreňové metódy slúžiace na výpočet vzdialenosti priamky od
		// kružnice a úsečky od kružnice som najskôr analyticky vyriešil
		// a potom som zistil, že Java má na tieto riešenia už definované
		// vlastné metódy. Rozhodol som sa týmto metódam dôverovať (dúfam,
		// že to nebola chyba ☺ ☺ ☺) a použiť ich.

		/**
		 * <p>Vypočíta vzdialenosť medzi určenou priamkou a kružnicou. Ak sa
		 * priamka a kružnica pretínajú, tak vrátená vzdialenosť je menšia od
		 * nuly, pričom absolútna hodnota tejto vzdialenosti je rovná
		 * vzdialenosti dvoch bodov určených takto: Body ležia na kolmici (K)
		 * so zadanou priamkou (P), ktorá tiež prechádza stredom zadanej
		 * kružnice (C). Jeden bod je priesečníkom oboch priamok K a P. Druhý
		 * bod je tým priesečníkom kolmice K a kružnice C, ktorý leží bližšie
		 * k prvému priesečníku.<!-- TODO: Obrázok‼ (Bez neho to je hrůza…) -->
		 * (Ak sa kružnica a priamka dotýkajú v jedinom bode, tak je
		 * vzdialenosť nulová.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Zaujímavosťou je, že relatívne
		 * komplikovaný opis uvedený vyššie (zahŕňajúci priesečníky, kolmicu,
		 * priamku a kružnicu) vznikol až po analytickom riešení problému ako
		 * vysvetlenie spôsobu riešenia, ktoré bolo zvolené tak, aby bolo čo
		 * najmenej výpočtovo náročné. V skutočnosti je veľmi priamočiare.
		 * Je to rozdiel:<br />
		 * – vzdialenosti stredu kružnice od priamky<br />
		 * – a polomeru kružnice.<br />
		 * (Algoritmus výpočtu vzdialenosti bodu od priamky nie je
		 * príliš výpočtovo náročný a aj v triedach Javy sa nachádza
		 * {@linkplain Line2D#ptLineDist(double, double, double, double,
		 * double, double) metóda}, ktorá tento algoritmus implementuje.)</p>
		 * 
		 * <!-- TODO pridať príklad použitia a/alebo obrázok. -->
		 * 
		 * <p>[x1, y1] – [x2, y2] – určujúce body priamky; [x3, y3] – stred;
		 * r – polomer</p>
		 * 
		 * @param x1 x-ová súradnica určujúceho bodu A priamky
		 * @param y1 y-ová súradnica určujúceho bodu A priamky
		 * @param x2 x-ová súradnica určujúceho bodu B priamky
		 * @param y2 y-ová súradnica určujúceho bodu B priamky
		 * @param x3 x-ová súradnica stredu kružnice
		 * @param y3 y-ová súradnica stredu kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťPriamkyOdKružnice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{ return Line2D.ptLineDist(x1, y1, x2, y2, x3, y3) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťPriamkyOdKružnice(double, double, double, double, double, double, double) vzdialenosťPriamkyOdKružnice}.</p> */
		public final static double vzdialenostPriamkyOdKruznice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{ return Line2D.ptLineDist(x1, y1, x2, y2, x3, y3) - r; }

		/**
		 * <p>Vypočíta vzdialenosť medzi určenou priamkou |AB| a kružnicou
		 * so stredom S a polomerom r. Pozri aj opis metódy {@link 
		 * #vzdialenosťPriamkyOdKružnice(double, double, double, double,
		 * double, double, double) vzdialenosťPriamkyOdKružnice}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param A poloha určujúceho bodu A priamky
		 * @param B poloha určujúceho bodu B priamky
		 * @param S poloha stredu kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťPriamkyOdKružnice(
			Poloha A, Poloha B, Poloha S, double r)
		{ return Line2D.ptLineDist(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), S.polohaX(), S.polohaY()) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťPriamkyOdKružnice(Poloha, Poloha, Poloha, double) vzdialenosťPriamkyOdKružnice}.</p> */
		public final static double vzdialenostPriamkyOdKruznice(
			Poloha A, Poloha B, Poloha S, double r)
		{ return Line2D.ptLineDist(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), S.polohaX(), S.polohaY()) - r; }

		/**
		 * <p>Vypočíta vzdialenosť medzi určenou priamkou |AB| a kružnicou
		 * so stredom S a polomerom r, pričom body A, B a stred S sú určené
		 * prvkami poľa {@code poleBodov}. To znamená, že pole musí obsahovať
		 * aspoň tri prvky. Prvé dva prvky sú určujúce body priamky (A a B)
		 * a tretí prvok je stred kružnice (S). Pozri aj opis metódy {@link 
		 * #vzdialenosťPriamkyOdKružnice(double, double, double, double,
		 * double, double, double) vzdialenosťPriamkyOdKružnice}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy určujúcich bodov priamky a poloha stredu
		 *     kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťPriamkyOdKružnice(
			Poloha[] poleBodov, double r)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/)
				return Double.NaN;

			return Line2D.ptLineDist(poleBodov[0].polohaX(),
				poleBodov[0].polohaY(), poleBodov[1].polohaX(),
				poleBodov[1].polohaY(), poleBodov[2].polohaX(),
				poleBodov[2].polohaY()) - r;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťPriamkyOdKružnice(Poloha[], double) vzdialenosťPriamkyOdKružnice}.</p> */
		public final static double vzdialenostPriamkyOdKruznice(
			Poloha[] poleBodov, double r)
		{ return vzdialenosťPriamkyOdKružnice(poleBodov, r); }


		/**
		 * <p>Vypočíta vzdialenosť medzi určenou úsečkou a kružnicou. Ak sa
		 * úsečka a kružnica pretínajú, tak vrátená vzdialenosť je menšia od
		 * nuly a jej absolútna hodnota je vzdialenosťou najbližšieho bodu
		 * na úsečke od stredu kružnice. (Ak sa kružnica a úsečka dotýkajú
		 * v jedinom bode, tak je vzdialenosť nulová.)</p>
		 * 
		 * <!-- TODO pridať príklad použitia a/alebo obrázok. -->
		 * 
		 * <p>[x1, y1] – [x2, y2] – krajné body úsečky; [x3, y3] – stred;
		 * r – polomer</p>
		 * @param x1 x-ová súradnica určujúceho bodu A úsečky
		 * @param y1 y-ová súradnica určujúceho bodu A úsečky
		 * @param x2 x-ová súradnica určujúceho bodu B úsečky
		 * @param y2 y-ová súradnica určujúceho bodu B úsečky
		 * @param x3 x-ová súradnica stredu kružnice
		 * @param y3 y-ová súradnica stredu kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťÚsečkyOdKružnice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{ return Line2D.ptSegDist(x1, y1, x2, y2, x3, y3) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečkyOdKružnice(double, double, double, double, double, double, double) vzdialenosťÚsečkyOdKružnice}.</p> */
		public final static double vzdialenostUseckyOdKruznice(
			double x1, double y1, double x2, double y2,
			double x3, double y3, double r)
		{ return Line2D.ptSegDist(x1, y1, x2, y2, x3, y3) - r; }

		/**
		 * <p>Vypočíta vzdialenosť medzi určenou úsečkou |AB| a kružnicou
		 * so stredom S a polomerom r. Pozri aj opis metódy {@link 
		 * #vzdialenosťÚsečkyOdKružnice(double, double, double, double,
		 * double, double, double) vzdialenosťÚsečkyOdKružnice}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param A poloha určujúceho bodu A úsečky
		 * @param B poloha určujúceho bodu B úsečky
		 * @param S poloha stredu kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťÚsečkyOdKružnice(
			Poloha A, Poloha B, Poloha S, double r)
		{ return Line2D.ptSegDist(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), S.polohaX(), S.polohaY()) - r; }

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečkyOdKružnice(Poloha, Poloha, Poloha, double) vzdialenosťÚsečkyOdKružnice}.</p> */
		public final static double vzdialenostUseckyOdKruznice(
			Poloha A, Poloha B, Poloha S, double r)
		{ return Line2D.ptSegDist(A.polohaX(), A.polohaY(), B.polohaX(),
			B.polohaY(), S.polohaX(), S.polohaY()) - r; }

		/**
		 * <p>Vypočíta vzdialenosť medzi určenou úsečkou |AB| a kružnicou
		 * so stredom S a polomerom r, pričom body A, B a stred S sú určené
		 * prvkami poľa {@code poleBodov}. To znamená, že pole musí obsahovať
		 * aspoň tri prvky. Prvé dva prvky sú určujúce body úsečky (A a B)
		 * a tretí prvok je stred kružnice (S). Pozri aj opis metódy {@link 
		 * #vzdialenosťÚsečkyOdKružnice(double, double, double, double,
		 * double, double, double) vzdialenosťÚsečkyOdKružnice}, ktorej
		 * správanie táto metóda kopíruje.</p>
		 * 
		 * @param poleBodov polohy určujúcich bodov úsečky a poloha stredu
		 *     kružnice
		 * @param r polomer kružnice
		 */
		public final static double vzdialenosťÚsečkyOdKružnice(
			Poloha[] poleBodov, double r)
		{
			if (null == poleBodov || 3 > poleBodov.length/* NOPE ||
				null == poleBodov[0] || null == poleBodov[1] ||
				null == poleBodov[2]*/)
				return Double.NaN;

			return Line2D.ptSegDist(poleBodov[0].polohaX(),
				poleBodov[0].polohaY(), poleBodov[1].polohaX(),
				poleBodov[1].polohaY(), poleBodov[2].polohaX(),
				poleBodov[2].polohaY()) - r;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vzdialenosťÚsečkyOdKružnice(Poloha[], double) vzdialenosťÚsečkyOdKružnice}.</p> */
		public final static double vzdialenostUseckyOdKruznice(
			Poloha[] poleBodov, double r)
		{ return vzdialenosťÚsečkyOdKružnice(poleBodov, r); }


	// --- Celá obrazovka

		/**
		 * <p>Zistí počet obrazovkových zariadení, ktoré sú dostupné na tomto
		 * počítači. Toto je vhodné overiť pred pokusom o prepnutie sveta do
		 * režimu celej obrazovky.</p>
		 * 
		 * @return počet zariadení
		 * 
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 * @see #premiestniNaZariadenie(int)
		 */
		public static int početZariadení()
		{
			return GraphicsEnvironment.getLocalGraphicsEnvironment().
				getScreenDevices().length;
		}

		/** <p><a class="alias"></a> Alias pre {@link #početZariadení() početZariadení}.</p> */
		public static int pocetZariadení() { return početZariadení(); }

		/** <p><a class="alias"></a> Alias pre {@link #početZariadení() početZariadení}.</p> */
		public static int početObrazoviek() { return početZariadení(); }

		/** <p><a class="alias"></a> Alias pre {@link #početZariadení() početZariadení}.</p> */
		public static int pocetObrazoviek() { return početZariadení(); }


		/**
		 * <p>Zistí šírku dostupného obrazovkového zariadenia.</p>
		 * 
		 * @return šírka obrazovkového zariadenia v pixeloch; hodnota
		 *     {@code -}{@code num1} znamená chybu
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static int šírkaZariadenia() { return šírkaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia() šírkaZariadenia}.</p> */
		public static int sirkaZariadenia() { return šírkaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia() šírkaZariadenia}.</p> */
		public static int šírkaObrazovky() { return šírkaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia() šírkaZariadenia}.</p> */
		public static int sirkaObrazovky() { return šírkaZariadenia(0); }

		/**
		 * <p>Zistí šírku dostupného obrazovkového zariadenia.
		 * Zariadenie je určené jeho poradovým číslom (indexom; čiže
		 * nula označuje prvé zariadenie).</p>
		 * 
		 * @param zariadenie poradové číslo zariadenia, ktorého šírka má byť
		 *     zistená
		 * @return šírka obrazovkového zariadenia v pixeloch; hodnota
		 *     {@code -}{@code num1} znamená chybu
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static int šírkaZariadenia(int zariadenie)
		{
			GraphicsDevice[] zariadenia = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getScreenDevices();
			if (zariadenie >= zariadenia.length) return -1;
			return zariadenia[zariadenie].getDisplayMode().getWidth();
		}

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia(int) šírkaZariadenia}.</p> */
		public static int sirkaZariadenia(int zariadenie) { return šírkaZariadenia(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia(int) šírkaZariadenia}.</p> */
		public static int šírkaObrazovky(int zariadenie) { return šírkaZariadenia(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #šírkaZariadenia(int) šírkaZariadenia}.</p> */
		public static int sirkaObrazovky(int zariadenie) { return šírkaZariadenia(zariadenie); }

		/**
		 * <p>Zistí výšku dostupného obrazovkového zariadenia.</p>
		 * 
		 * @return výška obrazovkového zariadenia v pixeloch; hodnota
		 *     {@code -}{@code num1} znamená chybu
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static int výškaZariadenia() { return výškaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia() výškaZariadenia}.</p> */
		public static int vyskaZariadenia() { return výškaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia() výškaZariadenia}.</p> */
		public static int výškaObrazovky() { return výškaZariadenia(0); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia() výškaZariadenia}.</p> */
		public static int vyskaObrazovky() { return výškaZariadenia(0); }

		/**
		 * <p>Zistí výšku dostupného obrazovkového zariadenia.
		 * Zariadenie je určené jeho poradovým číslom (indexom; čiže
		 * nula označuje prvé zariadenie).</p>
		 * 
		 * @param zariadenie poradové číslo zariadenia, ktorého výška má byť
		 *     zistená
		 * @return výška obrazovkového zariadenia v pixeloch; hodnota
		 *     {@code -}{@code num1} znamená chybu
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static int výškaZariadenia(int zariadenie)
		{
			GraphicsDevice[] zariadenia = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getScreenDevices();
			if (zariadenie >= zariadenia.length) return -1;
			return zariadenia[zariadenie].getDisplayMode().getHeight();
		}

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia(int) výškaZariadenia}.</p> */
		public static int vyskaZariadenia(int zariadenie) { return výškaZariadenia(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia(int) výškaZariadenia}.</p> */
		public static int výškaObrazovky(int zariadenie) { return výškaZariadenia(zariadenie); }

		/** <p><a class="alias"></a> Alias pre {@link #výškaZariadenia(int) výškaZariadenia}.</p> */
		public static int vyskaObrazovky(int zariadenie) { return výškaZariadenia(zariadenie); }


		/**
		 * <p>Pokúsi sa prepnúť svet do režimu celej obrazovky.</p>
		 * 
		 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
		 *     znamená úspech a {@code valfalse} neúspech
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static boolean celáObrazovka() { return celáObrazovka(0, true); }

		/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka() celáObrazovka}.</p> */
		public static boolean celaObrazovka() { return celáObrazovka(0, true); }


		/**
		 * <p>Pokúsi sa prepnúť svet do režimu celej obrazovky na určenom
		 * zobrazovacom zariadení. Zariadenie je určené jeho poradovým
		 * číslom (indexom; čiže nula označuje prvé zariadenie).</p>
		 * 
		 * <p>(Príklad použitia tejto metódy je pri opise metódy
		 * {@link #celáObrazovka(int, boolean)}.)</p>
		 * 
		 * @param zariadenie poradové číslo zariadenia, ktoré má byť použité
		 *     v režime celej obrazovky
		 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
		 *     znamená úspech a {@code valfalse} neúspech
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(boolean)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static boolean celáObrazovka(int zariadenie)
		{ return celáObrazovka(zariadenie, true); }

		/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(int) celáObrazovka}.</p> */
		public static boolean celaObrazovka(int zariadenie)
		{ return celáObrazovka(zariadenie, true); }


		/**
		 * <p>Pokúsi sa prepnúť svet do režimu celej obrazovky alebo späť.</p>
		 * 
		 * @param celáObrazovka ak je {@code valtrue}, tak má byť režim celej
		 *     obrazovky zapnutý, ak je {@code valfalse}, tak má byť režim celej
		 *     obrazovky vypnutý
		 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
		 *     znamená úspech a {@code valfalse} neúspech
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(int, boolean)
		 */
		public static boolean celáObrazovka(boolean celáObrazovka)
		{ return celáObrazovka(0, celáObrazovka); }

		/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(boolean) celáObrazovka}.</p> */
		public static boolean celaObrazovka(boolean celáObrazovka)
		{ return celáObrazovka(0, celáObrazovka); }


		/**
		 * <p>Hodnota tohto atribútu môže obsahovať vlastnú implementáciu
		 * spôsobu zmeny režimu celej obrazovky. (Predvolený spôsob je
		 * hardvérový – pozri aj {@link ZmenaCelejObrazovky
		 * ZmenaCelejObrazovky}{@code .}{@link ZmenaCelejObrazovky#hardvérová
		 * hardvérová}.)</p>
		 */
		public static ZmenaCelejObrazovky zmenaCelejObrazovky = null;

		/**
		 * <p>Pokúsi sa prepnúť svet do režimu celej obrazovky alebo späť
		 * na určenom zobrazovacom zariadení. Zariadenie je určené jeho
		 * poradovým číslom (indexom; čiže nula označuje prvé
		 * zariadenie).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V režime celej obrazovky nie
		 * je dostupná ponuka, pretože testovanie ukázalo, že v režime celej
		 * obrazovky spôsobujú všetky „ponukovo orientované“ prvky
		 * používateľského rozhrania (hlavná ponuka, rozbaľovací zoznam…)
		 * problémy. Nemali by ste ich preto v režime celej obrazovky
		 * používať.<br />
		 *  <br />
		 * Počas prechodu do režimu celej obrazovky sa však automaticky
		 * aktivuje klávesová skratka <code>Ctrl + W</code>, resp.
		 * <code>⌘ + W</code> (<small>Command + W</small></p>), ktorá bola
		 * pôvodne naviazaná na položku ponuky a ktorá nesie význam príkazu
		 * ukončenia aplikácie.<br />
		 *  <br />
		 * Počas testovania sme tiež zistili, že pri použití hardvérového
		 * prechodu do režimu celej obrazovky je prinajmenšom na platforme
		 * Windows automaticky skrytý kurzor myši a jeho viditeľnosť nie
		 * je možné nijakým spôsobom obnoviť. Pri prechode späť do
		 * „normálneho“ režimu je viditeľnosť kurzora automaticky obnovená.
		 * V prípade potreby je možné použiť na zobrazenie polohy kurzora
		 * niektorého robota, ktorý bude sledovať polohu myši.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestCelejObrazovky {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Ktoré zariadenie má byť použité v režime celej obrazovky?}
				{@code comm// Nula je predvolené. V hlavnej metóde je úprava tejto hodnoty…}
				{@code kwdprivate} {@code kwdstatic} {@code typeint} zariadenie = {@code num0};

				{@code kwdprivate} TestCelejObrazovky()
				{
					{@code comm// Nastavenie rozmerov plátna podľa rozmerov zariadenia.}
					{@code valsuper}({@link Svet Svet}.{@link Svet#šírkaZariadenia(int) šírkaZariadenia}(zariadenie),
						{@link Svet Svet}.{@link Svet#výškaZariadenia(int) výškaZariadenia}(zariadenie));

					{@link Svet Svet}.{@link Svet#upevni() upevni}();
					{@link Svet Svet}.{@link Svet#celáObrazovka(int) celáObrazovka}(zariadenie);
				}

				{@code comm// Obsluha klávesnice (na ukončenie aplikácie).}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieKlávesu() uvoľnenieKlávesu}()
				{
					{@code comm// Kláves ESC spôsobí vypnutie aplikácie.}
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#ESCAPE ESCAPE}))
						{@link Svet Svet}.{@link Svet#koniec() koniec}();
				}

				{@code comm// Hlavná metóda.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code comm// Je pravdepodobné, že druhé zariadenie je spätný projektor, preto}
					{@code comm// je tu tento test… Toto správanie odporúčame upraviť podľa potrieb.}
					{@code kwdif} ({@link Svet Svet}.{@link Svet#početZariadení() početZariadení}() &gt; {@code num1}) ++zariadenie;
					{@code kwdnew} TestCelejObrazovky();
				}
			}
			</pre>
		 * 
		 * @param zariadenie poradové číslo zariadenia, ktoré má byť použité
		 *     v režime celej obrazovky
		 * @param celáObrazovka ak je {@code valtrue}, tak má byť režim celej
		 *     obrazovky zapnutý, ak je {@code valfalse}, tak má byť režim celej
		 *     obrazovky vypnutý
		 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
		 *     znamená úspech a {@code valfalse} neúspech
		 * 
		 * @see #početZariadení()
		 * @see #šírkaZariadenia()
		 * @see #šírkaZariadenia(int)
		 * @see #výškaZariadenia()
		 * @see #výškaZariadenia(int)
		 * @see #celáObrazovka()
		 * @see #celáObrazovka(int)
		 * @see #celáObrazovka(boolean)
		 */
		public static boolean celáObrazovka(
			int zariadenie, boolean celáObrazovka)
		{
			ZmenaCelejObrazovky spôsobZmeny;
			if (null == zmenaCelejObrazovky)
				spôsobZmeny = ZmenaCelejObrazovky.hardvérová;
			else
				spôsobZmeny = zmenaCelejObrazovky;

			/*
				{@code comm// Na niektorých platformách spôsobuje zobrazenie ponuky alebo}
				{@code comm// manipulácia s tlačidlami v režime celej obrazovky problémy…}
				{@link Svet Svet}.{@link Svet#vymažPonuku() vymažPonuku}();
			*/

			GraphicsDevice[] zariadenia = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getScreenDevices();
			// System.out.println("Počet zariadení: " + zariadenia.length);
			if (zariadenie >= zariadenia.length || null == svet) return false;

			// JMenuBar hlavnáPonuka = svet.getJMenuBar();
			ComponentEvent componentEvent;

			if (spôsobZmeny.ponechajOkno())
			{
				if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
					spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
						celáObrazovka, svet);
			}
			else if (celáObrazovka && null == oknoCelejObrazovky &&
				null == spôsobZmeny.dajOkno(zariadenie,
					zariadenia[zariadenie]))
				// null == zariadenia[zariadenie].getFullScreenWindow())
			{
				// System.out.println("isFullScreenSupported: " +
				// 	zariadenia[zariadenie].isFullScreenSupported());
				// if (zariadenia[zariadenie].isFullScreenSupported())
				if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
				{
					svet.removeComponentListener(udalostiOkna);
					svet.removeWindowFocusListener(udalostiOkna);
					svet.remove(hlavnýPanel);
					svet.remove(panelVstupnéhoRiadka);

					oknoCelejObrazovky = new JFrame();
					oknoCelejObrazovky.setResizable(false);
					oknoCelejObrazovky.setUndecorated(true);
					oknoCelejObrazovky.setIconImage(svet.getIconImage());
					oknoCelejObrazovky.setTitle(svet.getTitle());
					oknoCelejObrazovky.add(hlavnýPanel, BorderLayout.CENTER);
					oknoCelejObrazovky.add(panelVstupnéhoRiadka,
						BorderLayout.SOUTH);
					oknoCelejObrazovky.addComponentListener(udalostiOkna);
					oknoCelejObrazovky.addWindowFocusListener(udalostiOkna);

					// oknoCelejObrazovky.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					// hlavnýPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					// oknoCelejObrazovky.setCursor(svet.getContentPane().getCursor());

					/*---*
					hlavnýPanel.getInputMap().put(KeyStroke.
						getKeyStroke('W', Toolkit.getDefaultToolkit().
							getMenuShortcutKeyMask())
						/**-/, "koniecSveta"
						);
					hlavnýPanel.getActionMap().put("koniecSveta"
						/**-/, koniecSveta
						);

					vstupnýRiadok.getInputMap().put(KeyStroke.
						getKeyStroke('W', Toolkit.getDefaultToolkit().
							getMenuShortcutKeyMask())
						/**-/, "koniecSveta"
						);
					vstupnýRiadok.getActionMap().put("koniecSveta"
						/**-/, koniecSveta
						);
					/*---*/

					KeyboardFocusManager.getCurrentKeyboardFocusManager().
						addKeyEventDispatcher(koniecSveta);


					// zariadenia[zariadenie].setFullScreenWindow(oknoCelejObrazovky);
					spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
						celáObrazovka, oknoCelejObrazovky);
					oknoCelejObrazovky.setVisible(true);
					svet.setVisible(false);

					componentEvent = new ComponentEvent(oknoCelejObrazovky,
						ComponentEvent.COMPONENT_MOVED);
					udalostiOkna.componentMoved(componentEvent);

					componentEvent = new ComponentEvent(oknoCelejObrazovky,
						ComponentEvent.COMPONENT_RESIZED);
					udalostiOkna.componentResized(componentEvent);

					return true;
				}
			}
			else if (!celáObrazovka && null != oknoCelejObrazovky &&
				oknoCelejObrazovky == spôsobZmeny.dajOkno(zariadenie,
					zariadenia[zariadenie]))
				// oknoCelejObrazovky == zariadenia[zariadenie].
				// 	getFullScreenWindow())
			{
				// if (zariadenia[zariadenie].isFullScreenSupported())
				if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
				{
					// zariadenia[zariadenie].setFullScreenWindow(null);
					spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
						celáObrazovka, oknoCelejObrazovky);
					oknoCelejObrazovky.setVisible(false);

					/*---*
					hlavnýPanel.getInputMap().remove(KeyStroke.
						getKeyStroke('W', Toolkit.getDefaultToolkit().
							getMenuShortcutKeyMask())
						// , "koniecSveta"
						);
					hlavnýPanel.getActionMap().remove("koniecSveta"
						// , koniecSveta
						);

					vstupnýRiadok.getInputMap().remove(KeyStroke.
						getKeyStroke('W', Toolkit.getDefaultToolkit().
							getMenuShortcutKeyMask())
						// , "koniecSveta"
						);
					vstupnýRiadok.getActionMap().remove("koniecSveta"
						// , koniecSveta
						);
					/*---*/

					KeyboardFocusManager.getCurrentKeyboardFocusManager().
						removeKeyEventDispatcher(koniecSveta);


					oknoCelejObrazovky.remove(hlavnýPanel);
					oknoCelejObrazovky.remove(panelVstupnéhoRiadka);
					oknoCelejObrazovky.removeComponentListener(udalostiOkna);
					oknoCelejObrazovky.removeWindowFocusListener(udalostiOkna);
					oknoCelejObrazovky = null;

					svet.add(hlavnýPanel, BorderLayout.CENTER);
					svet.add(panelVstupnéhoRiadka, BorderLayout.SOUTH);
					svet.addComponentListener(udalostiOkna);
					svet.addWindowFocusListener(udalostiOkna);
					svet.setVisible(true);

					componentEvent = new ComponentEvent(svet,
						ComponentEvent.COMPONENT_MOVED);
					udalostiOkna.componentMoved(componentEvent);

					componentEvent = new ComponentEvent(svet,
						ComponentEvent.COMPONENT_RESIZED);
					udalostiOkna.componentResized(componentEvent);

					return true;
				}
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(int, boolean) celáObrazovka}.</p> */
		public static boolean celaObrazovka(
			int zariadenie, boolean celáObrazovka)
		{ return celáObrazovka(zariadenie, celáObrazovka); }


		// Rôzne súkromné atribúty slúžiace potrebám režimu celej obrazovky.
		// private static boolean zobrazPonukuPoCelejObrazovke = false;
		// private static AbstractAction koniecSveta = new AbstractAction()
		// { public void actionPerformed(ActionEvent e) { System.exit(0); }};
		private static KeyEventDispatcher koniecSveta = new KeyEventDispatcher()
		{
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_W &&
					e.getModifiers() == Toolkit.getDefaultToolkit().
						getMenuShortcutKeyMask())
				{
					System.exit(0);
					return true;
				}

				return false;
			}
		};

		/*packagePrivate*/ static class KlávesováSkratka extends AbstractAction
		{
			public final String príkaz;
			// public final KeyStroke keyStroke;

			public KlávesováSkratka(String príkaz, KeyStroke keyStroke)
			{
				super(príkaz);
				this.príkaz = príkaz;
				// this.keyStroke = keyStroke;
				putValue(ACCELERATOR_KEY, keyStroke);
			}

			@Override public void actionPerformed(ActionEvent e)
			{
				// Podobné ako: poslednáUdalosťKlávesnice
				ÚdajeUdalostí.poslednáUdalosťSkratky = e;
				ÚdajeUdalostí.poslednýPríkazSkratky = príkaz;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						// Podobné ako: zadanieZnaku()
						ObsluhaUdalostí.počúvadlo.klávesováSkratka();
						ObsluhaUdalostí.počúvadlo.klavesovaSkratka();
					}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
						{
							// Podobné ako: zadanieZnaku()
							počúvajúci.klávesováSkratka();
							počúvajúci.klavesovaSkratka();
						}
					}
			}
		}

		// Zoznam klávesových skratiek podľa akcií
		/*packagePrivate*/ final static TreeMap<String, KlávesováSkratka>
			klávesovéSkratky = new TreeMap<>();

		/**
		 * <p>Definuje novú klávesovú skratku s modifikátorom pre ponuky, ktorá
		 * bude previazaná so zadaným príkazom. Klávesové skratky sú spracúvané
		 * udalosťou {@link ObsluhaUdalostí#klávesováSkratka
		 * ObsluhaUdalostí.klávesováSkratka}, ktorá používa metódu
		 * {@link ÚdajeUdalostí#príkazSkratky() ÚdajeUdalostí.príkazSkratky()}
		 * na identifikáciu príkazu.</p>
		 * 
		 * <p>Táto klávesová skratka je definovaná s predvoleným modifikátorom
		 * používaným pre klávesové skratky {@linkplain PoložkaPonuky položiek
		 * ponuky}. Ten je závislý od operačného systému, napríklad vo Windows
		 * je to kláves {@code Ctrl}, v macOS (predtým OS X a Mac OS) je to
		 * kláves {@code ⌘} (<small>Command</small>). Ak chcete definovať
		 * klávesovú skratku bez modifikátora, použite metódu
		 * {@link #pridajKlávesovúSkratku(String, int, int)
		 * pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor)}
		 * s hodnotou modifikátora {@code num0}.</p>
		 * 
		 * @param príkaz príkaz, ktorý bude previazaný s touto klávesovou
		 *     skratkou
		 * @param kódKlávesu kód klávesu, ktorý má byť použitý ako klávesová
		 *     skratka (v kombinácii s modifikátorom pre ponuky); môže to byť
		 *     ľubovoľný kód klávesu z triedy {@link Kláves Kláves}
		 *     ({@link Kláves#HORE Kláves.HORE}, {@link KeyEvent#VK_X
		 *     Kláves.VK_X}…)
		 * 
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #skratkaPríkazu(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static void pridajKlávesovúSkratku(String príkaz, int kódKlávesu)
		{
			pridajKlávesovúSkratku(príkaz, kódKlávesu,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratku(String, int) pridajKlávesovúSkratku}.</p> */
		public static void pridajKlavesovuSkratku(String príkaz, int kódKlávesu)
		{ pridajKlávesovúSkratku(príkaz, kódKlávesu); }

		/**
		 * <p>Definuje novú klávesovú skratku, ktorá bude previazaná so zadaným
		 * príkazom. Klávesové skratky sú spracúvané udalosťou {@link 
		 * ObsluhaUdalostí#klávesováSkratka ObsluhaUdalostí.klávesováSkratka},
		 * ktorá používa metódu {@link ÚdajeUdalostí#príkazSkratky()
		 * ÚdajeUdalostí.príkazSkratky()} na identifikáciu príkazu.</p>
		 * 
		 * @param príkaz príkaz, ktorý bude previazaný s touto klávesovou
		 *     skratkou
		 * @param kódKlávesu kód klávesu, ktorý má byť použitý ako klávesová
		 *     skratka; môže to byť ľubovoľný kód klávesu z triedy
		 *     {@link Kláves Kláves} ({@link Kláves#HORE Kláves.HORE},
		 *     {@link KeyEvent#VK_X Kláves.VK_X}…)
		 * @param modifikátor klávesový modifikátor tejto skratky (napríklad
		 *     kláves Ctrl – {@link java.awt.event.InputEvent#CTRL_MASK
		 *     Kláves.CTRL_MASK},
		 *     Shift – {@link java.awt.event.InputEvent#SHIFT_MASK
		 *     Kláves.SHIFT_MASK},
		 *     Alt – {@link java.awt.event.InputEvent#ALT_MASK
		 *     Kláves.ALT_MASK}…); klávesový modifikátor ponúk, ktorý je
		 *     závislý od operačného systému definuje rezervovaný
		 *     identifikátor {@link Kláves#SKRATKA_PONUKY
		 *     Kláves.SKRATKA_PONUKY}; klávesovú skratku bez modifikátora je
		 *     možné definovať zadaním hodnoty {@code num0}
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #skratkaPríkazu(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static void pridajKlávesovúSkratku(String príkaz, int kódKlávesu,
			int modifikátor)
		{ pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor, true); }

		/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratku(String, int, int) pridajKlávesovúSkratku}.</p> */
		public static void pridajKlavesovuSkratku(String príkaz, int kódKlávesu,
			int modifikátor)
		{ pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor); }

		/**
		 * <p>Definuje novú klávesovú skratku, ktorá bude previazaná so zadaným
		 * príkazom. Klávesové skratky sú spracúvané udalosťou {@link 
		 * ObsluhaUdalostí#klávesováSkratka ObsluhaUdalostí.klávesováSkratka},
		 * ktorá používa metódu {@link ÚdajeUdalostí#príkazSkratky()
		 * ÚdajeUdalostí.príkazSkratky()} na identifikáciu príkazu.</p>
		 * 
		 * <p>Niektoré klávesové skratky by mohli spôsobiť obmedzenie
		 * funkčnosti {@link #vstupnýRiadok() vstupného riadka}, preto táto
		 * verzia metódy umožňuje nepriradiť túto skratku vstupnému riadku.
		 * Naopak, v niektorých prípadoch je žiadúce definovať klávesovú
		 * skratku len pre vstupný riadok. Na tieto prípady je rezervovaný
		 * samostatná metóda
		 * {@link #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * pridajKlávesovúSkratkuVstupnéhoRiadka}.</p>
		 * 
		 * @param príkaz príkaz, ktorý bude previazaný s touto klávesovou
		 *     skratkou
		 * @param kódKlávesu kód klávesu, ktorý má byť použitý ako klávesová
		 *     skratka; môže to byť ľubovoľný kód klávesu z triedy
		 *     {@link Kláves Kláves} ({@link Kláves#HORE Kláves.HORE},
		 *     {@link KeyEvent#VK_X Kláves.VK_X}…)
		 * @param modifikátor klávesový modifikátor tejto skratky (napríklad
		 *     kláves Ctrl – {@link java.awt.event.InputEvent#CTRL_MASK
		 *     Kláves.CTRL_MASK},
		 *     Shift – {@link java.awt.event.InputEvent#SHIFT_MASK
		 *     Kláves.SHIFT_MASK},
		 *     Alt – {@link java.awt.event.InputEvent#ALT_MASK
		 *     Kláves.ALT_MASK}…); klávesový modifikátor ponúk, ktorý je
		 *     závislý od operačného systému definuje rezervovaný
		 *     identifikátor {@link Kláves#SKRATKA_PONUKY
		 *     Kláves.SKRATKA_PONUKY}; klávesovú skratku bez modifikátora je
		 *     možné definovať zadaním hodnoty {@code num0}
		 * @param ajVstupnýRiadok ak si neželáme, aby táto skratka fungovala
		 *     aj vo vstupnom riadku, zadáme {@code valfalse}
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #skratkaPríkazu(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static void pridajKlávesovúSkratku(String príkaz, int kódKlávesu,
			int modifikátor, boolean ajVstupnýRiadok)
		{
			if (klávesovéSkratky.containsKey(príkaz))
				odoberKlávesovúSkratku(príkaz);

			KeyStroke keyStroke = KeyStroke.getKeyStroke(kódKlávesu,
				modifikátor);
			KlávesováSkratka klávesováSkratka = new KlávesováSkratka(
				príkaz, keyStroke);

			klávesovéSkratky.put(príkaz, klávesováSkratka);

			hlavnýPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).
				put(keyStroke, príkaz);
			hlavnýPanel.getActionMap().put(príkaz, klávesováSkratka);

			if (ajVstupnýRiadok)
			{
				vstupnýRiadok.getInputMap().put(keyStroke, príkaz);
				vstupnýRiadok.getActionMap().put(príkaz, klávesováSkratka);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratku(String, int, int, boolean) pridajKlávesovúSkratku}.</p> */
		public static void pridajKlavesovuSkratku(String príkaz, int kódKlávesu,
			int modifikátor, boolean ajVstupnýRiadok)
		{ pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor, ajVstupnýRiadok); }

		/**
		 * <p>Definuje novú klávesovú skratku pre vstupný riadok, ktorá bude
		 * previazaná so zadaným príkazom. Klávesové skratky sú spracúvané
		 * udalosťou {@link ObsluhaUdalostí#klávesováSkratka
		 * ObsluhaUdalostí.klávesováSkratka}, ktorá používa metódu
		 * {@link ÚdajeUdalostí#príkazSkratky() ÚdajeUdalostí.príkazSkratky()}
		 * na identifikáciu príkazu.</p>
		 * 
		 * <p>Toto je doplnková metóda, ktorá umožňuje definovať skupinu
		 * klávesových skratiek, ktoré budú použiteľné len vo vstupnom riadku.
		 * Na definovanie klávesových skratiek hlavného okna slúži množina
		 * metód {@link #pridajKlávesovúSkratku(String, int)
		 * pridajKlávesovúSkratku}.</p>
		 * 
		 * @param príkaz príkaz, ktorý bude previazaný s touto klávesovou
		 *     skratkou
		 * @param kódKlávesu kód klávesu, ktorý má byť použitý ako klávesová
		 *     skratka; môže to byť ľubovoľný kód klávesu z triedy
		 *     {@link Kláves Kláves} ({@link Kláves#HORE Kláves.HORE},
		 *     {@link KeyEvent#VK_X Kláves.VK_X}…)
		 * @param modifikátor klávesový modifikátor tejto skratky (napríklad
		 *     kláves Ctrl – {@link java.awt.event.InputEvent#CTRL_MASK
		 *     Kláves.CTRL_MASK},
		 *     Shift – {@link java.awt.event.InputEvent#SHIFT_MASK
		 *     Kláves.SHIFT_MASK},
		 *     Alt – {@link java.awt.event.InputEvent#ALT_MASK
		 *     Kláves.ALT_MASK}…); klávesový modifikátor ponúk, ktorý je
		 *     závislý od operačného systému definuje rezervovaný
		 *     identifikátor {@link Kláves#SKRATKA_PONUKY
		 *     Kláves.SKRATKA_PONUKY}; klávesovú skratku bez modifikátora je
		 *     možné definovať zadaním hodnoty {@code num0}
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #skratkaPríkazu(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static void pridajKlávesovúSkratkuVstupnéhoRiadka(
			String príkaz, int kódKlávesu, int modifikátor)
		{
			if (klávesovéSkratky.containsKey(príkaz))
				odoberKlávesovúSkratku(príkaz);

			KeyStroke keyStroke = KeyStroke.getKeyStroke(kódKlávesu,
				modifikátor);
			KlávesováSkratka klávesováSkratka = new KlávesováSkratka(
				príkaz, keyStroke);

			klávesovéSkratky.put(príkaz, klávesováSkratka);

			vstupnýRiadok.getInputMap().put(keyStroke, príkaz);
			vstupnýRiadok.getActionMap().put(príkaz, klávesováSkratka);
		}

		/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int) pridajKlávesovúSkratkuVstupnéhoRiadka}.</p> */
		public static void pridajKlavesovuSkratkuVstupnehoRiadka(
			String príkaz, int kódKlávesu, int modifikátor)
		{ pridajKlávesovúSkratkuVstupnéhoRiadka(príkaz, kódKlávesu,
			modifikátor); }

		/**
		 * <p>Odoberie definovanú klávesovú skratku, ktorá je previazaná so
		 * zadaným príkazom. Príkaz odoberie skratky zo zoznamu skratiek
		 * hlavného okna aj vstupného riadka.</p>
		 * 
		 * @param príkaz príkaz, ktorý je previazaný s niektorou
		 *     klávesovou skratkou
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #skratkaPríkazu(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static void odoberKlávesovúSkratku(String príkaz)
		{
			if (klávesovéSkratky.containsKey(príkaz))
			{
				KeyStroke keyStroke = (KeyStroke)
					klávesovéSkratky.get(príkaz).//keyStroke;
					getValue(AbstractAction.ACCELERATOR_KEY);

				hlavnýPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).
					remove(keyStroke);
				hlavnýPanel.getActionMap().remove(príkaz);

				vstupnýRiadok.getInputMap().remove(keyStroke);
				vstupnýRiadok.getActionMap().remove(príkaz);

				klávesovéSkratky.remove(príkaz);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #odoberKlávesovúSkratku(String) odoberKlávesovúSkratku}.</p> */
		public static void odoberKlavesovuSkratku(String príkaz)
		{ odoberKlávesovúSkratku(príkaz); }


		/**
		 * <p>Táto metóda zistí, aká klávesová skratka je priradená zadanému
		 * príkazu. Ak taká skratka nejestvuje, metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p>Metóda nedokáže rozlíšiť, či je skratka platná pre hlavné
		 * okno a/alebo vstupný riadok, preto ak rozlišujete medzi skratkami
		 * definovanými pre vstupný riadok a hlavné okno, tak odporúčame pre
		 * nich zvoliť unikátne názvy.</p>
		 * 
		 * @param príkaz príkaz, ktorý by mal byť previazaný s niektorou
		 *     klávesovou skratkou
		 * @return objekt typu {@link KeyStroke KeyStroke} alebo {@code valnull}
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #reťazecSkratkyPríkazu(String)
		 */
		public static KeyStroke skratkaPríkazu(String príkaz)
		{
			if (klávesovéSkratky.containsKey(príkaz))
				return (KeyStroke)klávesovéSkratky.get(príkaz).
					getValue(AbstractAction.ACCELERATOR_KEY);
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #skratkaPríkazu(String) skratkaPríkazu}.</p> */
		public static KeyStroke skratkaPrikazu(String príkaz)
		{ return skratkaPríkazu(príkaz); }

		/**
		 * <p>Táto metóda prevedie definíciu klávesovej skratku, ktorá je
		 * priradená zadanému príkazu do textovej podoby. Ak taká skratka
		 * nejestvuje, tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p>Metóda nedokáže rozlíšiť, či je skratka platná pre hlavné
		 * okno a/alebo vstupný riadok, preto ak rozlišujete medzi skratkami
		 * definovanými pre vstupný riadok a hlavné okno, tak odporúčame pre
		 * nich zvoliť unikátne názvy.</p>
		 * 
		 * @param príkaz príkaz, ktorý by mal byť previazaný s niektorou
		 *     klávesovou skratkou
		 * @return text klávesovej skratky alebo {@code valnull}
		 * 
		 * @see #pridajKlávesovúSkratku(String, int)
		 * @see #pridajKlávesovúSkratku(String, int, int)
		 * @see #pridajKlávesovúSkratku(String, int, int, boolean)
		 * @see #pridajKlávesovúSkratkuVstupnéhoRiadka(String, int, int)
		 * @see #odoberKlávesovúSkratku(String)
		 * @see #skratkaPríkazu(String)
		 */
		public static String reťazecSkratkyPríkazu(String príkaz)
		{
			KeyStroke keyStroke = skratkaPríkazu(príkaz);
			if (null == keyStroke) return null;
			if (0 == keyStroke.getModifiers())
				return KeyEvent.getKeyText(keyStroke.getKeyCode());
			return KeyEvent.getKeyModifiersText(keyStroke.getModifiers()) +
				"+" + KeyEvent.getKeyText(keyStroke.getKeyCode());
		}

		/** <p><a class="alias"></a> Alias pre {@link #reťazecSkratkyPríkazu(String) reťazecSkratkyPríkazu}.</p> */
		public static String retazecSkratkyPrikazu(String príkaz)
		{ return reťazecSkratkyPríkazu(príkaz); }


	// --- Vlnenie

		// Inštancia vlnenia sveta
		/*packagePrivate*/ static Vlnenie vlnenie = null;

		/**
		 * <p>Overí, či má svet definovanú inštanciu vlnenia.</p>
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
		public static boolean máVlnenie() { return null != vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #máVlnenie() máVlnenie}.</p> */
		public static boolean maVlnenie() { return null != vlnenie; }

		/**
		 * <p>Vráti {@linkplain Vlnenie inštanciu vlnenia} sveta, aby s ňou
		 * bolo možné ďalej pracovať. Ak svet nemá definované vlnenie, tak
		 * metóda definuje nové neaktívne vlnenie s predvolenou úrovňou
		 * útlmu {@code num26}. <small>(Overiť to, či je definovaná
		 * inštancia vlnenia, je možné s pomocou metódy {@link #máVlnenie()
		 * máVlnenie}.)</small> Naopak, metóda {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie} vráti inštanciu vlnenia len v takom prípade,
		 * že jestvuje. <small>(V opačnom prípade vráti metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} hodnotu
		 * {@code valnull}.)</small></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje v prostredí grafického
		 * robota.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Ak svet grafického
		 * robota nemá aktívny {@linkplain Svet#spustiČasovač() časovač},
		 * tak vlnenie nebude fungovať ani po jeho aktivácii. Táto metóda
		 * <b>nespúšťa</b> časovač (ani vlnenie) automaticky! Účelom
		 * automatického vytvorenia inštancie vlnenia touto metódou
		 * v prípade jej neprítomnosti je len zabránenie vzniku chýb.
		 * Táto metóda nemá nahradiť metódu {@link #pridajVlnenie()
		 * pridajVlnenie}.</p>
		 * 
		 * @return metóda zaručuje vrátenie inštancie {@link Vlnenie Vlnenie}
		 *     definovanej pre svet aj v takom prípade, že pred jej
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
		public static Vlnenie vlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokSveta2, 26);
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
		public static Vlnenie jestvujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public static Vlnenie jestvujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public static Vlnenie existujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public static Vlnenie existujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public static Vlnenie definovanéVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public static Vlnenie definovaneVlnenie() { return vlnenie; }

		/**
		 * <p>Pridá alebo zresetuje vlnenie sveta. Ak svet nemá definované
		 * alebo aktívne vlnenie, tak volanie tejto metódy vytvorí a/alebo
		 * aktivuje novú inštanciu vlnenia s predvolenou úrovňou útlmu
		 * {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje v prostredí grafického
		 * robota.</p>
		 * 
		 * <p>Ak by svet grafického robota nemal aktívny
		 * {@linkplain Svet#spustiČasovač() časovač}, tak by vlnenie nemohlo
		 * fungovať, preto je časovač touto metódou spúšťaný automaticky.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou pomocou
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
		public static void pridajVlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokSveta2, 26);
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
		// public static void zacniVlnenie() { pridajVlnenie(); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie sveta. Ak svet nemá definované
		 * alebo aktívne vlnenie, tak volanie tejto metódy vytvorí a/alebo
		 * aktivuje novú inštanciu vlnenia s predvolenou úrovňou útlmu
		 * {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje v prostredí grafického
		 * robota.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak svet grafického robota nemá
		 * aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať. Táto metóda dovoľuje určiť, či má alebo nemá
		 * byť časovač spustený automaticky. Umožňuje to parameter
		 * {@code ajČasovač}.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou pomocou
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
		public static void pridajVlnenie(boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokSveta2, 26);
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
		// public static void zacniVlnenie(boolean ajČasovač) { pridajVlnenie(ajČasovač); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie sveta. Ak svet nemá definované
		 * alebo aktívne vlnenie, tak volanie tejto metódy vytvorí a/alebo
		 * aktivuje novú inštanciu vlnenia so zadanou úrovňou útlmu (pozri
		 * aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje v prostredí grafického
		 * robota.</p>
		 * 
		 * <p>Ak by svet grafického robota nemal aktívny
		 * {@linkplain Svet#spustiČasovač() časovač}, tak by vlnenie nemohlo
		 * fungovať, preto je časovač touto metódou spúšťaný automaticky.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou pomocou
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
		public static void pridajVlnenie(int útlm)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokSveta2, útlm);
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
		// public static void zacniVlnenie(int útlm) { pridajVlnenie(útlm); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie sveta. Ak svet nemá definované
		 * alebo aktívne vlnenie, tak volanie tejto metódy vytvorí a/alebo
		 * aktivuje novú inštanciu vlnenia so zadanou úrovňou útlmu (pozri
		 * aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)})</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje v prostredí grafického
		 * robota.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak svet grafického robota nemá
		 * aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať. Táto metóda dovoľuje určiť, či má alebo nemá
		 * byť časovač spustený automaticky. Umožňuje to parameter
		 * {@code ajČasovač}.</p>
		 * 
		 * <p>Inštanciu vlnenia je možné získať a pracovať s ňou pomocou
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
		public static void pridajVlnenie(int útlm, boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokSveta2, útlm);
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

		// /** <p><a class="alias"></a> Alias pre {@link #pridajVlnenie(int, boolean) pridajVlnenie}.</p> */
		// public static void zacniVlnenie(int útlm, boolean ajČasovač) { pridajVlnenie(útlm, ajČasovač); }

		/**
		 * <p>Ukončí vlnenie sveta a úplne odstráni inštanciu vlnenia,
		 * ktorá bola pre neho definovaná z prostredia programovacieho
		 * rámca GRobot.</p>
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 */
		public static void odstráňVlnenie()
		{
			if (null != vlnenie)
			{
				Vlnenie.vlnenia.remove(vlnenie);
				vlnenie = null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #odstráňVlnenie() odstráňVlnenie}.</p> */
		public static void odstranVlnenie() { odstráňVlnenie(); }
}

// :wrap=none:
