
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2026 by Roman Horváth
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
// import java.awt.KeyEventDispatcher;
import java.awt.Toolkit;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.geom.Rectangle2D;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import java.awt.event.ActionEvent;
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

import java.io.File;

import java.util.TooManyListenersException;
import java.util.TreeMap;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.UIManager;

// import static knižnica.Konštanty.versionString;

import static javax.swing.JFrame.DO_NOTHING_ON_CLOSE;
import static javax.swing.JFrame.ICONIFIED;
import static javax.swing.JFrame.MAXIMIZED_BOTH;
import static javax.swing.JFrame.NORMAL;

/**
 * <p>Táto trieda je určená na tvorbu a manipuláciu s doplnkovými oknami
 * aplikácie. Každé nové okno obsahuje komponent, v ktorom je umiestnený
 * obrázok (inštancia triedy {@link Obrázok Obrázok}), prípadne ďalšie
 * komponenty. Okno sa dá vytvoriť s použitím jedného z konštruktorov.
 * K dispozícii sú rôzne verzie. Niektoré zobrazujú okno automaticky po
 * skonštruovaní a/alebo pomenúvajú okno, ale všetky vyžadujú zadanie
 * inštancie obrázka. Tie, ktoré neumožňujú určiť, či má byť okno po
 * konštrukcii automaticky zobrazené nechávajú okno skryté. Jeho zobrazenie
 * musí byť potom vykonané (vo vhodnom čase) metódou {@link #zobraz()
 * zobraz}.</p>
 * 
 * <p>Trieda sprostredkúva viacero užitočných funkcií na manipuláciu
 * s vlastnosťami okna, napríklad na úpravu farby plochy, kurzora myši,
 * minimalizáciu, prepnutie z a do režimu celej obrazovky a podobne.
 * V súvislosti s režimom celej obrazovky treba podotknúť, že inštancia
 * komponentu okna sa mení. Prístup k aktuálnej inštancii sprostredkúva metóda
 * {@link #okno() okno}. Metódy ako {@link #farbaPlochy(Color) farbaPlochy}
 * umožňujú nastaviť farbu plochy okna rôznymi spôsobmi – inštanciou triedy
 * {@link Color Color} (alebo odvodených), implementáciou rozhrania {@link 
 * Farebnosť Farebnosť} alebo definovaním RGB hodnôt. Okno môže mať tiež
 * zmenený tvar kurzora myši pri pohybe nad ním. Dá sa to zmeniť metódou
 * {@link #zmeňKurzorMyši(String) zmeňKurzorMyši}.</p>
 * 
 * <p>Nastavenie režimu celej obrazovky je možné meniť rôznymi verziami
 * metódy {@link #celáObrazovka(boolean) celáObrazovka}. Metóda {@link 
 * #oknoCelejObrazovky() oknoCelejObrazovky} vráti inštanciu JFrame, ak je
 * okno v režime celej obrazovky, inak vráti hodnotu {@code valnull}. Okno
 * môže obsahovať vlastnú implementáciu spôsobu zmeny režimu celej obrazovky.
 * Atribút {@link #zmenaCelejObrazovky zmenaCelejObrazovky } má predvolene
 * hodnotu {@code valnull}, ale môže byť nastavený na ľubovoľnú z predvolených
 * inštancií triedy {@link ZmenaCelejObrazovky ZmenaCelejObrazovky}.</p>
 * 
 * <!-- TODO – príklady -->
 * 
 */
public class Okno
{
	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }


	// TODO: Implementovať možnosť presunu tlačidiel (prípadne iných
	// komponentov) do okna alebo priamo ich vytvorenia v okne…

	// Príznak overovania počiatočnej polohy okna (po inicializácii):
	private static boolean overujPočiatočnúPolohuOkna = true;

	// Toto je počiatočný stav okna, ktorý môže byť zmenený konštruktorom
	// alebo konfiguráciou:
	private int počiatočnáŠírka = 600;
	private int počiatočnáVýška = 500;
	private int počiatočnéX = 25;
	private int počiatočnéY = 75;
	private int počiatočnýStav = NORMAL;
	private boolean počiatočnýStavNaVrchu = false;

	/**
	 * <p>Tento rezervovaný atribút je predvolene nastavený na
	 * {@code valfalse}. Vtedy rámec pri ukladaní {@linkplain 
	 * Svet#použiKonfiguráciu(String) konfigurácie} (po ukončení aplikácie)
	 * neberie do úvahy aktuálny stav príznaku okna {@linkplain 
	 * JFrame#setAlwaysOnTop(boolean) „vždy na vrchu“ (always on top).} Keď je
	 * však atribút pred ukončením aplikácie nastavený na {@code valtrue},
	 * tak rámec uloží aktuálny stav príznaku okna do konfigurácie.</p>
	 */
	public boolean ukladajStavOknaNaVrchu = false;

	// Tieto atribúty sú upravované pri zmene veľkosti a polohy okna a sú
	// použité pri zápise konfigurácie:
	private int poslednáŠírka = 600;
	private int poslednáVýška = 500;
	private int poslednéX = 25;
	private int poslednéY = 75;


	// Aktuálna farba pozadia.
	//*packagePrivate*/ Farba farbaPozadia;  // ??? TODO ???
		// Okno nemá plátno a preto ani pozadie. Má plochu. Treba však overiť,
		// ako sa bude správať obrázok, ktorý je priehľadný.

	// Štandardné okno.
	/*packagePrivate*/ JFrame okno = new JFrame();

	// Okno režimu celej obrazovky.
	/*packagePrivate*/ JFrame oknoCelejObrazovky = null;

	// Príznak uloženia okna do konfigurácie.
	// private boolean uložKonfiguráciu = false; // TODO – zvážiť, či treba,
		// lebo teraz to je riešené tak, že pomenované okno sa ukladá
		// automaticky, ak sa zmenili jeho parametre…

	// Obrázkový komponent okna zadávaný do konštruktora:
	private Obrázok obrázok;

	// Príznak zobrazenia okna pri štarte. Okná predvolene nie sú zobrazované
	// pri štarte. Tak by to malo byť so všetkými objektmi (svetom, robotmi),
	// ale pri ostatných by to bola nedidakticky pôsobiaca komplikácia. Keďže
	// však okno je pokročilejší koncept, môžeme si to dovoliť.
	private boolean zobrazPriŠtarte = false;

	// Meno okna na uloženie parametrov do konfigurácie:
	private String menoOkna = null;

	// Mená všetkých okien
	private final static TreeMap<String, Okno>
		menáOkien = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Zoznam všetkých okien
	/*packagePrivate*/ final static Vector<Okno> všetkyOkná = new Vector<>();

	// Na umožnenie zavretria aj v režime celej obrazovky
	// (inicializuje sa až keď treba…)
	// private KeyEventDispatcher zavriOkno = null; // TODO – nefunguje,
		// blokuje sa s hlavným oknom; zisti prečo


	/**
	 * <p>Zistí aktuálny stav automatického overovania počiatočnej polohy
	 * okna po jeho inicializácii. Viac podrobností nájdete v opise metódy
	 * {@link #overujPočiatočnúPolohuOkna(boolean)
	 * overujPočiatočnúPolohuOkna}{@code (overuj)}.</p>
	 * 
	 * @return aktuálny stav overovania: {@code valtrue}/{@code valfalse}
	 */
	public static boolean overujPočiatočnúPolohuOkna()
	{ return overujPočiatočnúPolohuOkna; }

	/**
	 * <p>Zmení stav automatického overovania počiatočnej polohy každého
	 * nového okna počas jeho inicializácie. Ak je stav overovania nastavený
	 * na {@code valtrue}, tak sa počas inicializácie každého nového okna
	 * overí, či je umiestnené v rámci aktuálnych obrazoviek (zariadení) a ak
	 * nie, tak bude automaticky presunuté na primárnu obrazovku (resp.
	 * predvolené zobrazovacie zariadenie).
	 * 
	 * @param overuj nový stav overovania: {@code valtrue}/{@code valfalse}
	 */
	public static void overujPočiatočnúPolohuOkna(boolean overuj)
	{ overujPočiatočnúPolohuOkna = overuj; }

	/** <p><a class="alias"></a> Alias pre {@link #overujPočiatočnúPolohuOkna() overujPočiatočnúPolohuOkna}.</p> */
	public static boolean overujPociatocnuPolohuOkna()
	{ return overujPočiatočnúPolohuOkna; }

	/** <p><a class="alias"></a> Alias pre {@link #overujPočiatočnúPolohuOkna(boolean) overujPočiatočnúPolohuOkna}.</p> */
	public static void overujPociatocnuPolohuOkna(boolean overuj)
	{ overujPočiatočnúPolohuOkna = overuj; }


	// Komponent klientskej oblasti okna s príslušenstvom:

	private ImageIcon ikonaOkna;
	private JLabel komponentIkony;

	@SuppressWarnings("serial")
	/*packagePrivate*/ JPanel hlavnýPanel = new JPanel()
	{
		@Override public boolean isOptimizedDrawingEnabled() { return false; }


		// Ťahanie a pustenie súborov — začiatok.

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

		// Ťahanie a pustenie súborov — koniec.
	};

	// Takto to nefungovalo
	// private javax.swing.JScrollPane rolovaciaTabla =
	// 	new javax.swing.JScrollPane(hlavnýPanel);

	// Prepočítanie súradníc myši do správneho súradnicového priestoru:
	private double korekciaMyšiX(double x)
	{ return x - (hlavnýPanel.getWidth() / 2.0); }
	private double korekciaMyšiY(double y)
	{ return -y + (hlavnýPanel.getHeight() / 2.0); }

	// Trieda pracujúca so systémom Ťahaj a Pusti (súbor).
	private class ObsluhaPusteniaSúboru implements DropTargetListener
	{
		private void povoliťPustenie(DropTargetDragEvent dtde)
		{
			if (dtde.isDataFlavorSupported(
				DataFlavor.javaFileListFlavor))
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = (int)dtde.getLocation().getX();
				int eGetY = (int)dtde.getLocation().getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);

				if (null != ObsluhaUdalostí.počúvadlo)
				{
					ObsluhaUdalostí.počúvadlo.ťahanieSúborov();
					ObsluhaUdalostí.počúvadlo.tahanieSuborov();
				}

				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.ťahanieSúborov();
					počúvajúci.tahanieSuborov();
				}

				// TODO: Urobiť konfigurovateľné!
				// dtde.acceptDrag(DnDConstants.ACTION_COPY);
				dtde.acceptDrag(dtde.getSourceActions());
			}
			else dtde.rejectDrag();
		}

		@Override public void dragEnter(DropTargetDragEvent dtde)
		{ povoliťPustenie(dtde); }

		@Override public void dragOver(DropTargetDragEvent dtde)
		{ povoliťPustenie(dtde); }

		@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
		@Override public void dragExit(DropTargetEvent dte) {}

		@Override public void drop(DropTargetDropEvent dtde)
		{
			Transferable transferable = dtde.getTransferable();
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				ÚdajeUdalostí.akciaPustenia = dtde.getDropAction();
				dtde.acceptDrop(ÚdajeUdalostí.akciaPustenia);
				try
				{
					@SuppressWarnings("unchecked")
					List<File> transferData = (List<File>)transferable.
						getTransferData(DataFlavor.javaFileListFlavor);

					if (transferData != null && transferData.size() > 0)
					{
						ÚdajeUdalostí.oknoUdalosti = Okno.this;

						// ťahanieUkončené
						for (File file : transferData)
						{
							String menoSúboru = file.getCanonicalPath();

							if (null != ObsluhaUdalostí.počúvadlo)
							{
								ObsluhaUdalostí.počúvadlo.
									pustenieSúboru(menoSúboru);
								ObsluhaUdalostí.počúvadlo.
									pustenieSuboru(menoSúboru);
							}

							int početPočúvajúcich =
								GRobot.počúvajúciRozhranie.size();
							for (int i = 0; i < početPočúvajúcich; ++i)
							{
								GRobot počúvajúci = GRobot.
									počúvajúciRozhranie.get(i);
								počúvajúci.pustenieSúboru(menoSúboru);
								počúvajúci.pustenieSuboru(menoSúboru);
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


	// Inicializácia okna.
	private void inicializuj()
	{
		všetkyOkná.add(this);

		// Nastavenie systémového Look&Feel:
		if (Svet.zmenaLAF) try { UIManager.setLookAndFeel(UIManager.
			getSystemLookAndFeelClassName());
			// Tento „hack“ je prenesený zo Sveta. (Viac tam.) Tu je možno
			// zbytočný, ale… „better safe than sorry…“ – „istota je guľomet…“
			javax.swing.plaf.FileChooserUI ui =
				new javax.swing.JFileChooser().getUI();
		} catch (Exception e)
		{ GRobotException.vypíšChybovéHlásenia(e, true); }

		ikonaOkna = new ImageIcon(obrázok);
		komponentIkony = new JLabel(ikonaOkna);

		@SuppressWarnings("serial")
		OverlayLayout overlay = new OverlayLayout(hlavnýPanel)
		{
			// Vlastný spôsob umiestňovania komponentov
			@Override public void layoutContainer(Container cieľ)
			{
				int x = 0, y = 0, šírka = cieľ.getWidth(),
					výška = cieľ.getHeight();

				x = (šírka - ikonaOkna.getIconWidth()) / 2;
				y = (výška - ikonaOkna.getIconHeight()) / 2;

				/*System.out.println("ikonaOkna.getIconWidth(): " + ikonaOkna.getIconWidth());
				System.out.println("ikonaOkna.getIconHeight(): " + ikonaOkna.getIconHeight());*/

				komponentIkony.setBounds(x, y,
					komponentIkony.getPreferredSize().width,
					komponentIkony.getPreferredSize().height);

				/* */
				if (šírka > obrázok.šírka()) šírka = (int)obrázok.šírka();
				if (výška > obrázok.výška()) výška = (int)obrázok.výška();
				/* */
				// x = šírka / 2; y = výška / 3;

				synchronized (cieľ.getTreeLock())
				{
					Component komponenty[] = cieľ.getComponents();

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
				}

				// TODO: (zvážiť) Toto asi už tu nie…
				// if (Plátno.konzolaPoužitá)
				// {
				// 	/*if (nekresli)
				// 		neboloPrekreslené = true;
				// 	else*/
				// 		prekresli(); // noInvokeLater
				// }
			}
		};
		hlavnýPanel.setLayout(overlay);
		hlavnýPanel.add(komponentIkony);

		// Do hlavného rámca pridáme hlavný panel (tohto okna):
		okno.add(hlavnýPanel, BorderLayout.CENTER);

		hlavnýPanel.setFocusTraversalKeysEnabled(false);

		// Nastavenie poslucháčov myši a klávesnice:
		hlavnýPanel.addMouseListener(udalostiOkna);
		hlavnýPanel.addMouseMotionListener(udalostiOkna);
		hlavnýPanel.addMouseWheelListener(udalostiOkna);

		hlavnýPanel.addKeyListener(udalostiOkna);
		hlavnýPanel.setFocusable(true);
		hlavnýPanel.doLayout();

		okno.addComponentListener(udalostiOkna);
		okno.addWindowFocusListener(udalostiOkna);
		okno.addWindowListener(udalostiOkna);
		okno.addWindowStateListener(udalostiOkna);

		// Nastavenie ďalších vlastností okna:
		// …

		okno.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		okno.setTitle(/*versionString*/Svet.svet.getTitle());
		if (null != Svet.svet) okno.setIconImage(Svet.svet.getIconImage());

		if (null != oknoCelejObrazovky)
			oknoCelejObrazovky.setTitle(/*versionString*/Svet.svet.getTitle());

		// FIX: 2023-08-24:
		pridajKlávesovúSkratku(close, Kláves.VK_W, Kláves.SKRATKA_PONUKY);

		okno.pack();
		// okno.requestFocusInWindow();
		okno.setSize(počiatočnáŠírka, počiatočnáVýška);
		okno.setLocation(počiatočnéX, počiatočnéY);

		if (overujPočiatočnúPolohuOkna && -1 == zistiZariadenieOkna())
			premiestniNaZariadenie();

		okno.setVisible(zobrazPriŠtarte);

		if (NORMAL != počiatočnýStav)
			okno.setExtendedState(počiatočnýStav);

		if (počiatočnýStavNaVrchu)
			okno.setAlwaysOnTop(true);

		// TODO: del?
		// 
		// // Aby sa dala klávesnica použiť hneď:
		// if (null == ÚdajeUdalostí.poslednáUdalosťKlávesnice)
		// 	ÚdajeUdalostí.poslednáUdalosťKlávesnice =
		// 		new KeyEvent(okno, 0, 0, 0, 0, '\0');
		// 
		// // Aby sa dala myš použiť hneď:
		// if (null == ÚdajeUdalostí.poslednáUdalosťMyši)
		// 	ÚdajeUdalostí.poslednáUdalosťMyši = new MouseEvent(
		// 		okno, 0, 0, 0, 0, 0, 0, false);
	}


	// Toto je použité v konfigurácii sveta. Popri overovaní zmien stavov
	// robotov sa touto metódou dá overiť zmena stavu jednotlivých okien.
	/*packagePrivate*/ boolean konfiguráciaZmenená()
	{
		return počiatočnáŠírka != okno.getWidth() ||
			počiatočnáVýška != okno.getHeight() ||
			počiatočnéX != okno.getLocation().x ||
			počiatočnéY != okno.getLocation().y ||
			počiatočnýStav != okno.getExtendedState() ||
			(ukladajStavOknaNaVrchu &&
				počiatočnýStavNaVrchu != okno.isAlwaysOnTop());
	}

	// Toto je použité pri zapisovaní konfigurácie sveta. (Ak bol zmenený
	// hoci aj jediný parameter konfigurácie, zapíše sa konfigurácia všetkých
	// pomenovaných okien, podobne robotov a iných konfiguračných direktív.)
	/*packagePrivate*/ void uložKonfiguráciu()
	{
		if (null == menoOkna) return;
		Súbor.Sekcia pôvodnáSekcia = Svet.konfiguračnýSúbor.aktívnaSekcia;

		String mennýPriestor = Svet.konfiguračnýSúbor.
			aktívnaSekcia.mennýPriestorVlastností;

		try
		{
			pôvodnáSekcia = Svet.konfiguračnýSúbor.aktívnaSekcia;
			Svet.konfiguračnýSúbor.aktivujSekciu(
				Svet.predvolenáSekciaKonfigurácie);

			mennýPriestor = Svet.konfiguračnýSúbor.
				aktívnaSekcia.mennýPriestorVlastností;

			// Konfigurácia okna
			try
			{
				Svet.konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = menoOkna;

				int stav = okno.getExtendedState();
				/* TODO – zvážiť uloženie režimu celej obrazovky
				int stav = (null == oknoCelejObrazovky) ?
					okno.getExtendedState() : oknoCelejObrazovky.
					getExtendedState();
				*/

				if (NORMAL != stav)
				{
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"x", poslednéX); // windowX
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"y", poslednéY); // windowY

					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"šírka", poslednáŠírka); // windowWidth
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"výška", poslednáVýška); // windowHeight
				}
				else
				{
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"x", okno.getLocation().x); // windowX
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"y", okno.getLocation().y); // windowY

					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"šírka", okno.getWidth()); // windowWidth
					Svet.konfiguračnýSúbor.zapíšVlastnosť(
						"výška", okno.getHeight()); // windowHeight
				}

				Svet.konfiguračnýSúbor.zapíšVlastnosť(
					"minimalizované", 0 != (stav & ICONIFIED));
				Svet.konfiguračnýSúbor.zapíšVlastnosť(
					"maximalizované", 0 != (stav & MAXIMIZED_BOTH));
				// ‼ Zmrzne:
				// ‼ if (0 != (stav & MAXIMIZED_BOTH))
				// ‼ 	okno.setExtendedState(stav & ~MAXIMIZED_BOTH);
			}
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); }
		}
		catch (Exception e)
		{ GRobotException.vypíšChybovéHlásenia(e); }
		finally
		{
			Svet.konfiguračnýSúbor.aktívnaSekcia.
				mennýPriestorVlastností = mennýPriestor;
			Svet.konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia;
		}

		try { Svet.konfiguračnýSúbor.zavri(); }
		catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
	}


	/**
	 * <p>Prečíta konfiguráciu tohto okna z konfiguračného súboru
	 * s predvolenými parametrami. Viac podrobností je v opise metódy
	 * {@link #čítajKonfiguráciu(int, int, int, int) čítajKonfiguráciu(x, y,
	 * šírka, výška).}</p>
	 * 
	 * @return ak bolo čítanie parametrov okna z konfigurácie úspešné, tak
	 *     vráti {@code valtrue}, inak {@code valfalse}
	 */
	public boolean čítajKonfiguráciu()
	{
		return čítajKonfiguráciu(počiatočnéX, počiatočnéY,
			počiatočnáŠírka, počiatočnáVýška);
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciu(int, int, int, int) čítajKonfiguráciu}.</p> */
	public boolean citajKonfiguraciu() { return čítajKonfiguráciu(); }

	/**
	 * <p>Prečíta konfiguráciu tohto okna z konfiguračného súboru.
	 * Konfigurácia musí byť zapnutá (pozri napríklad metódu {@link 
	 * Svet#použiKonfiguráciu(String) Svet.použiKonfiguráciu(názovSúboru)})
	 * a okno musí mať meno (pozri metódu {@link #meno(String) meno}).</p>
	 * 
	 * <p>Parametre polohy a rozmerov okna tejto metódy určujú počiatočné
	 * (predvolené) hodnoty týchto údajov. Neskôr budú nahradené aktuálnymi
	 * hodnotami uloženými v konfigurácii. Súradnice {@code x} a {@code y}
	 * určujú, o koľko bodov je posunutý ľavý horný roh okna od ľavého
	 * horného rohu obrazovky (y-ová súradnica rastie smerom nadol).</p>
	 * 
	 * @param x predvolená (pozri vyššie) x-ová súradnica polohy okna
	 * @param y predvolená (pozri vyššie) y-ová súradnica polohy okna
	 * @param šírka predvolená (pozri vyššie) šírka okna
	 * @param výška predvolená (pozri vyššie) výška okna
	 * @return ak bolo čítanie parametrov okna z konfigurácie úspešné, tak
	 *     vráti {@code valtrue}, inak {@code valfalse}
	 */
	public boolean čítajKonfiguráciu(int x, int y, int šírka, int výška)
	{
		if (null == menoOkna) return false;

		Súbor.Sekcia pôvodnáSekcia = Svet.konfiguračnýSúbor.aktívnaSekcia;
		String mennýPriestor = Svet.konfiguračnýSúbor.aktívnaSekcia.
			mennýPriestorVlastností;

		// Súbor je otvorený len raz, pretože je pravdepodobné,
		// že sa z neho bude veľa čítať. Aj tak je pri ukončení
		// a zápise zatvorený a opätovne otvorený.
		try
		{
			pôvodnáSekcia = Svet.konfiguračnýSúbor.aktívnaSekcia;
			Svet.konfiguračnýSúbor.aktivujSekciu(
				Svet.predvolenáSekciaKonfigurácie);
			mennýPriestor = Svet.konfiguračnýSúbor.
				aktívnaSekcia.mennýPriestorVlastností;

			// Konfigurácia okna
			try
			{
				Svet.konfiguračnýSúbor.aktívnaSekcia.
					mennýPriestorVlastností = menoOkna;

				poslednéX = počiatočnéX = Svet.konfiguračnýSúbor.
					čítajVlastnosť("x", x); // windowX
				poslednéY = počiatočnéY = Svet.konfiguračnýSúbor.
					čítajVlastnosť("y", y); // windowY

				poslednáŠírka = počiatočnáŠírka = Svet.konfiguračnýSúbor.
					čítajVlastnosť("šírka", šírka); // windowWidth
				poslednáVýška = počiatočnáVýška = Svet.konfiguračnýSúbor.
					čítajVlastnosť("výška", výška); // windowHeight

				počiatočnýStav = NORMAL;
				if (Svet.konfiguračnýSúbor.čítajVlastnosť(
					"minimalizované", false))
					počiatočnýStav |= ICONIFIED;
				if (Svet.konfiguračnýSúbor.čítajVlastnosť(
					"maximalizované", false))
					počiatočnýStav |= MAXIMIZED_BOTH;

				if (Svet.konfiguračnýSúbor.čítajVlastnosť("vždyNaVrchu",
					false)) počiatočnýStavNaVrchu = true;

				return true;
			}
			catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); }
		}
		catch (Exception e)
		{ GRobotException.vypíšChybovéHlásenia(e); }
		finally
		{
			Svet.konfiguračnýSúbor.aktívnaSekcia.
				mennýPriestorVlastností = mennýPriestor;
			Svet.konfiguračnýSúbor.aktívnaSekcia = pôvodnáSekcia;
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciu(int, int, int, int) čítajKonfiguráciu}.</p> */
	public boolean citajKonfiguraciu(int x, int y, int šírka, int výška)
	{ return čítajKonfiguráciu(x, y, šírka, výška); }


	// Nakoniec netreba. Nedáva zmysel.
	// /* *
	//  * <p>Nastaví počiatočné vlastnosti (polohu, rozmery a stav maximalizácie
	//  * alebo minimalizácie) okna. Buď ide o počiatočné vlastnosti nastavené
	//  * počas konštrukcie, alebo o vlastnosti prečítané z konfigurácie.</p>
	//  * 
	//  * <p>Počiatočné vlastnosti okna nie je možné meniť. Buď sú nastavené pri
	//  * konštrukcii, alebo sú prečítané z konfigurácie (ako bolo napísané).
	//  * Cieľom tejto metódy je len obnovenie okna do počiatočného stavu,
	//  * ktorý bol získaný jedným z týchto spôsobov.</p>
	//  * 
	//  * <p><em>(Metóda je oddelená od metódy {@link #čítajKonfiguráciu()
	//  * čítajKonfiguráciu}, aby sa čítanie konfigurácie dalo používať
	//  * nezávisle.)</em></p>
	//  */
	// public void resetujOkno()
	// {
	// 	okno.setSize(počiatočnáŠírka, počiatočnáVýška);
	// 	okno.setLocation(počiatočnéX, počiatočnéY);
	// 	okno.setExtendedState(počiatočnýStav);
	// }


	// Inštancia spracúvajúca udalosti okna
	/*packagePrivate*/ final UdalostiOkna udalostiOkna = new UdalostiOkna();

	/** 
	 * <p>Vygeneruje udalosť zavretia tohto okna. Ak nie je zavretie
	 * odmietnuté žiadnou z reakcií na zavretie okna (pozri napríklad
	 * {@link ObsluhaUdalostí ObsluhaUdalostí}{@code .}{@link 
	 * ObsluhaUdalostí#zavretie() zavretie}{@code ()}), tak bude toto okno
	 * zavreté.</p>
	 <!--
	 TODO (je to ok?):
	 Otázka: Čo to znamená? Ukončí sa aplikácia ako pri svete? Či?
	 Odpoveď: Asi iba skrytie – pozri reakciu windowClosing.
	 -->
	 */
	public void zavrieť()
	{
		udalostiOkna.windowClosing(new WindowEvent(
			null == oknoCelejObrazovky ? okno : oknoCelejObrazovky,
			WindowEvent.WINDOW_CLOSING));
	}

	/** <p><a class="alias"></a> Alias pre {@link #zavrieť() zavrieť}.</p> */
	public void zavriet() { zavrieť(); }


	// Udalosti okna – ObsluhaUdalostí.počúvadlo myši, klávesnice
	// a udalostí komponentov…
	private class UdalostiOkna implements MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener,
		ComponentListener, WindowFocusListener, WindowListener,
		WindowStateListener
	{
		public void mouseClicked(MouseEvent e)
		{
			synchronized (ÚdajeUdalostí.zámokMyši)
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáUdalosťMyši = e;
				hlavnýPanel.requestFocusInWindow();

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.klik();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = e.getX();
				int eGetY = e.getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);
				e.translatePoint(
					(int)ÚdajeUdalostí.súradnicaMyšiX - eGetX,
					(int)ÚdajeUdalostí.súradnicaMyšiY - eGetY);

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
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = e.getX();
				int eGetY = e.getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);
				e.translatePoint(
					(int)ÚdajeUdalostí.súradnicaMyšiX - eGetX,
					(int)ÚdajeUdalostí.súradnicaMyšiY - eGetY);

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
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = e.getX();
				int eGetY = e.getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);

				e.translatePoint(
					(int)ÚdajeUdalostí.súradnicaMyšiX - eGetX,
					(int)ÚdajeUdalostí.súradnicaMyšiY - eGetY);

				ÚdajeUdalostí.poslednáUdalosťMyši = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.pohybMyši();
						ObsluhaUdalostí.počúvadlo.pohybMysi();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = e.getX();
				int eGetY = e.getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);

				e.translatePoint(
					(int)ÚdajeUdalostí.súradnicaMyšiX - eGetX,
					(int)ÚdajeUdalostí.súradnicaMyšiY - eGetY);

				ÚdajeUdalostí.poslednáUdalosťMyši = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.ťahanieMyšou();
						ObsluhaUdalostí.počúvadlo.tahanieMysou();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáSúradnicaMyšiX =
					ÚdajeUdalostí.súradnicaMyšiX;
				ÚdajeUdalostí.poslednáSúradnicaMyšiY =
					ÚdajeUdalostí.súradnicaMyšiY;

				int eGetX = e.getX();
				int eGetY = e.getY();

				ÚdajeUdalostí.pôvodnáSúradnicaMyšiX = eGetX;
				ÚdajeUdalostí.pôvodnáSúradnicaMyšiY = eGetY;

				ÚdajeUdalostí.súradnicaMyšiX = korekciaMyšiX(eGetX);
				ÚdajeUdalostí.súradnicaMyšiY = korekciaMyšiY(eGetY);
				e.translatePoint(
					(int)ÚdajeUdalostí.súradnicaMyšiX - eGetX,
					(int)ÚdajeUdalostí.súradnicaMyšiY - eGetY);

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

				/*if (0 == (e.getModifiersEx() & (
					InputEvent.ALT_DOWN_MASK |
					InputEvent.CTRL_DOWN_MASK |
					InputEvent.META_DOWN_MASK)))
				{
					if (GRobot.podlaha.rolujTexty)
						GRobot.podlaha.rolujTexty();
					if (GRobot.strop.rolujTexty)
						GRobot.strop.rolujTexty();
				}*/

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.rolovanieKolieskomMyši();
						ObsluhaUdalostí.počúvadlo.rolovanieKolieskomMysi();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciMyš.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciMyš.get(i);
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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

				// Focus traversal: (S+)VK_TAB…
				if (Svet.spracujFokus(e/*, true // TODO: del */))
				{
					if (null != ObsluhaUdalostí.počúvadlo)
						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							ObsluhaUdalostí.počúvadlo.stlačenieKlávesu();
							ObsluhaUdalostí.počúvadlo.stlacenieKlavesu();
						}

					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						int početPočúvajúcich =
							GRobot.počúvajúciKlávesnicu.size();
						for (int i = 0; i < početPočúvajúcich; ++i)
						{
							GRobot počúvajúci =
								GRobot.počúvajúciKlávesnicu.get(i);
							počúvajúci.stlačenieKlávesu();
							počúvajúci.stlacenieKlavesu();
						}
					}
				}
			}
		}

		public void keyReleased(KeyEvent e)
		{
			synchronized (ÚdajeUdalostí.zámokKlávesnice)
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.uvoľnenieKlávesu();
						ObsluhaUdalostí.počúvadlo.uvolnenieKlavesu();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich =
						GRobot.počúvajúciKlávesnicu.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci =
							GRobot.počúvajúciKlávesnicu.get(i);
						počúvajúci.uvoľnenieKlávesu();
						počúvajúci.uvolnenieKlavesu();
					}
				}
			}
		}

		public void keyTyped(KeyEvent e)
		{
			synchronized (ÚdajeUdalostí.zámokKlávesnice)
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
				ÚdajeUdalostí.poslednáUdalosťKlávesnice = e;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.zadanieZnaku();
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich =
						GRobot.počúvajúciKlávesnicu.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.
							počúvajúciKlávesnicu.get(i);
						počúvajúci.zadanieZnaku();
					}
				}
			}
		}


		public void componentHidden(ComponentEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
			ÚdajeUdalostí.poslednáUdalosťOkna = e;

			// Moment 🤔, nemôžem predsa zavrieť aplikáciu a keď ju nezavriem,
			// tak nemôžem nie je dobré predčasne ukončiť reakciu, lebo
			// aplikácia nedostane notifikáciu o skrytí okna…
			// 
			// TODO: Niekde dať upozornenie, že programátor musí dávať pozor
			// na to, či je zobrazené aspoň jedno okno alebo či je zabezpečené
			// to, aby sa aspoň jedno mohlo zobraziť (napr. cez system tray).
			// 
			// Druhý moment (o deň neskôr) – to nemá byť zavieranie aplikácie,
			// ale reálne iba okna… Ale aj tak: Je tu fix „nečakané skrytie
			// celej obrazovky,“ ktorý už neviem na čo presne bol…
			// 
			// if (null != oknoCelejObrazovky // &&
			// 	// nečakanéSkrytieCelejObrazovky
			// 	// TODO: overiť, či netreba
			// 	)
			// {
			// 	// System.out.println("nečakané skrytie celej obrazovky");
			// 	// windowClosing(new WindowEvent(oknoCelejObrazovky,
			// 	// 	WindowEvent.WINDOW_CLOSING));
			// 
			// 	/*Svet.*/zavrieť(/*0*/);
			// 	return;
			// }

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.skrytieOkna();
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.skrytieOkna();
				}
			}
		}

		public void componentShown(ComponentEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
			ÚdajeUdalostí.poslednáUdalosťOkna = e;

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.zobrazenieOkna();
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.zobrazenieOkna();
				}
			}
		}

		public void componentMoved(ComponentEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
			ÚdajeUdalostí.poslednáUdalosťOkna = e;

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.presunutieOkna();
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.presunutieOkna();
				}
			}
		}

		public void componentResized(ComponentEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.zmenaVeľkostiOkna();
					počúvajúci.zmenaVelkostiOkna();
				}
			}

			// TODO: Toto asi už nedáva zmysel… del?
			// if (Plátno.konzolaPoužitá)
			// {
			// 	if (null == ObsluhaUdalostí.počúvadlo)
			// 	{
			// 		/*if (nekresli)
			// 			neboloPrekreslené = true;
			// 		else*/
			// 			prekresli(); // noInvokeLater
			// 	}
			// 	/*else
			// 		automatickéPrekreslenie();*/
			// }
		}

		public void windowGainedFocus(WindowEvent e) {}
		public void windowLostFocus(WindowEvent e) {}


		public void windowActivated(WindowEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.aktiváciaOkna();
					počúvajúci.aktivaciaOkna();
				}
			}
		}

		public void windowDeactivated(WindowEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
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
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					počúvajúci.otvorenie();
				}
			}
		}

		public void windowClosing(WindowEvent e)
		{
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
				int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
				for (int i = 0; i < početPočúvajúcich; ++i)
				{
					GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
					if (!počúvajúci.zavretie())
						zavrieť = false;
					if (!počúvajúci.zatvorenie())
						zavrieť = false;
				}
			}

			if (zavrieť) okno.setVisible(false);
		}


		public void windowStateChanged(WindowEvent e)
		{
			int stav = e.getNewState();

			if (NORMAL == stav)
			{
				poslednáŠírka = okno.getWidth();
				poslednáVýška = okno.getHeight();
				poslednéX = okno.getLocation().x;
				poslednéY = okno.getLocation().y;

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
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
					int početPočúvajúcich =
						GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.
							počúvajúciRozhranie.get(i);
						počúvajúci.minimalizovanie();
					}
				}
			}
			else if (0 != (stav & MAXIMIZED_BOTH))
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
					int početPočúvajúcich =
						GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.
							počúvajúciRozhranie.get(i);
						počúvajúci.maximalizovanie();
					}
				}
			}
			else if (NORMAL == stav)
			{
				ÚdajeUdalostí.oknoUdalosti = Okno.this;
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
					int početPočúvajúcich =
						GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.
							počúvajúciRozhranie.get(i);
						počúvajúci.obnovenie();
					}
				}
			}
		}
	}


	/**
	 * <p>Najjednoduchší konštruktor. Vytvára nové okno s vloženým obrázkom,
	 * ktorý je povinný pri všetkých konštruktoroch.</p>
	 * 
	 * <p>Parameter {@code obrázok} slúži na definovanie grafického obsahu
	 * okna.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Toto okno je predvolene
	 * skryté!</p>
	 * 
	 * @param obrázok inštancia triedy {@link Obrázok Obrázok}, ktorá bude
	 *     tvoriť grafický obsah okna
	 */
	public Okno(Obrázok obrázok)
	{
		this.obrázok = obrázok;
		inicializuj();
	}

	/**
	 * <p>Konštruktor, ktorý vytvára nové okno s vloženým obrázkom
	 * a s možnosťou zobrazenia okna po vytvorení.</p>
	 * 
	 * <p>Parameter {@code obrázok} slúži na definovanie grafického obsahu
	 * okna.</p>
	 * 
	 * <p>Parameter {@code zobraz} slúži na rozhodnutie o tom, či sa okno
	 * po inicializácii zobrazí, alebo nie.</p>
	 * 
	 * @param obrázok inštancia triedy {@link Obrázok Obrázok}, ktorá bude
	 *     tvoriť grafický obsah okna
	 * @param zobraz ak je tento parameter {@code valtrue}, tak bude okno po
	 *     inicializácii zobrazené; inak zostane skryté
	 */
	public Okno(Obrázok obrázok, boolean zobraz)
	{
		this.obrázok = obrázok;
		zobrazPriŠtarte = zobraz;
		inicializuj();
	}

	/**
	 * <p>Konštruktor, ktorý vytvára nové pomenované okno s vloženým
	 * obrázkom.</p>
	 * 
	 * <p>Parameter {@code obrázok} slúži na definovanie grafického obsahu
	 * okna.</p>
	 * 
	 * <p>Parameter {@code meno} slúži na priradenie mena oknu. (Pozri aj
	 * metódu {@link #meno(String) meno}.)</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Toto okno je predvolene
	 * skryté!</p>
	 * 
	 * @param obrázok inštancia triedy {@link Obrázok Obrázok}, ktorá bude
	 *     tvoriť grafický obsah okna
	 * @param meno meno, ktoré bude priradené oknu
	 */
	public Okno(Obrázok obrázok, String meno)
	{
		this.obrázok = obrázok;

		// boolean resetujOkno = meno(meno) && čítajKonfiguráciu();
			// (alternatíva, ktorá nebola využitá)

		if (meno(meno)) čítajKonfiguráciu();
		inicializuj();

		// if (resetujOkno) resetujOkno();
			// (netreba, deje sa v inicializácii)
	}

	/**
	 * <p>Konštruktor, ktorý vytvára nové okno s vloženým obrázkom, menom
	 * a s možnosťou zobrazenia okna po vytvorení.</p>
	 * 
	 * <p>Parameter {@code obrázok} slúži na definovanie grafického obsahu
	 * okna.</p>
	 * 
	 * <p>Parameter {@code meno} slúži na priradenie mena oknu. (Pozri aj
	 * metódu {@link #meno(String) meno}.)</p>
	 * 
	 * <p>Parameter {@code zobraz} slúži na rozhodnutie o tom, či sa okno
	 * po inicializácii zobrazí, alebo nie.</p>
	 * 
	 * @param obrázok inštancia triedy {@link Obrázok Obrázok}, ktorá bude
	 *     tvoriť grafický obsah okna
	 * @param meno meno, ktoré bude priradené oknu
	 * @param zobraz ak je tento parameter {@code valtrue}, tak bude okno po
	 *     inicializácii zobrazené; inak zostane skryté
	 */
	public Okno(Obrázok obrázok, String meno, boolean zobraz)
	{
		this.obrázok = obrázok;
		zobrazPriŠtarte = zobraz;

		// boolean resetujOkno = meno(meno) && čítajKonfiguráciu();
			// (alternatíva, ktorá nebola využitá)

		if (meno(meno)) čítajKonfiguráciu();
		inicializuj();

		// if (resetujOkno) resetujOkno();
			// (netreba, deje sa v inicializácii)
	}


	// Rozhodnutia o zamietnutých konštruktoroch:
	//  ✗ titulkom – kolidovalo s menom
	//  ✗ rozmermi? (zvážiť verzie: ak nie je meno, má zmysel dávať rozmery?)
	//    nakoniec zamietnuté – nedávalo by zmysel; ani konštruktor robota
	//    neprijíma polohu a rozmery okna (sveta); dá sa „vyskladať“ vhodným
	//    použitím konštruktorov a čítania konfigurácie; dať niekde príklad
	//    použitia…


	// Meno okna

	/**
	 * <p><a class="setter"></a> Pomenuje alebo zruší meno tohto okna. Meno
	 * okna je používané pri zápise a obnove vlastností okna s použitím
	 * {@linkplain Svet#použiKonfiguráciu(String) konfiguračného súboru}.</p>
	 * 
	 * <p>Zadané meno musí byť unikátne, nesmie byť prázdne ani rezervované
	 * (v súčasnosti je rezervované iba meno {@code srg"okno"}), nesmie sa
	 * začínať bodkočiarkou, začínať ani končiť bodkou a nesmie
	 * obsahovať znak rovná sa. Na zrušenie mena okna treba zavolať túto
	 * metódu s argumentom {@code valnull}.</p>
	 * 
	 * <!-- p>TODO – ako sa pracuje s nepomenovanými oknami?</p -->
	 * 
	 * @param novéMeno nového meno okna alebo {@code valnull}
	 *     na zrušenie mena tohto okna
	 * @return príznak zlyhania; ak je vrátená hodnota {@code valfalse}, tak
	 *     to znamená, že priradenie mena oknu zlyhalo a okno mohlo zostať
	 *     nepomenované
	 */
	public boolean meno(String novéMeno)
	{
		if (null != novéMeno)
		{
			if (novéMeno.equalsIgnoreCase("okno") || novéMeno.equals("") ||
				novéMeno.endsWith(".") || novéMeno.startsWith(";") ||
				novéMeno.startsWith(".") || -1 != novéMeno.indexOf('='))
				return false;
		}

		if (null != menoOkna)
		{
			menáOkien.remove(menoOkna);
			menoOkna = null;
		}

		// Argument null len zruší meno okna:
		if (null == novéMeno) return true;

		if (menáOkien.containsKey(novéMeno)) return false;

		menoOkna = novéMeno;
		menáOkien.put(menoOkna, this);

		return true;
	}

	/**
	 * <p><a class="getter"></a> Vráti meno okna alebo {@code valnull} ak okno
	 * nemá meno.</p>
	 * 
	 * @return meno okna alebo {@code valnull}
	 * 
	 * @see #meno(String)
	 */
	public String meno()
	{
		return menoOkna;
	}


	// Rozmery okna

	private final HraniceRozmerov hraniceRozmerov =
		new HraniceRozmerov(okno, hlavnýPanel);

	// Poznámka: Mechanizmus obmedzovania minimálnych a maximálnych rozmerov
	// panelu a okna bol implementovaný s použitím umelej inteligencie.

	/**
	 * <p>Vráti nastavené minimálne rozmery viditeľného hlavného panela.
	 * Prvý prvok poľa je šírka a druhý výška v pixeloch. Nula označuje
	 * rozmer bez vlastného obmedzenia. Zmena vráteného poľa nastavenie
	 * neovplyvní.</p>
	 * 
	 * @return nové dvojprvkové pole so šírkou a výškou
	 * @see #minimálneRozmeryPanela(int, int)
	 */
	public int[] minimálneRozmeryPanela()
	{ return hraniceRozmerov.čítaj(HraniceRozmerov.MIN_PANEL); }

	/**
	 * <p>Nastaví minimálne rozmery viditeľného hlavného panela.
	 * Nulou sa obmedzenie príslušného rozmeru zruší. Ak je niektorý
	 * argument záporný, ponechajú sa obe doterajšie hodnoty.</p>
	 * 
	 * <p>Hranice panela a okna platia súčasne. Ak sa maximum nedá
	 * dodržať spolu s minimom, prednosť má minimum. Rozmery plátna
	 * sa nemenia. Hranice sa uplatnia pri najbližšom spracovaní
	 * udalostí bežného okna; režim celej obrazovky má vlastné rozmery.</p>
	 * 
	 * @param šírka požadovaná šírka v pixeloch
	 * @param výška požadovaná výška v pixeloch
	 * @see #minimálneRozmeryPanela()
	 */
	public void minimálneRozmeryPanela(int šírka, int výška)
	{ hraniceRozmerov.nastav(HraniceRozmerov.MIN_PANEL, šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela() minimálneRozmeryPanela}.</p> */
	public int[] minimalneRozmeryPanela() { return minimálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela(int, int) minimálneRozmeryPanela}.</p> */
	public void minimalneRozmeryPanela(int šírka, int výška)
	{ minimálneRozmeryPanela(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela() minimálneRozmeryPanela}.</p> */
	public int[] najmenšieRozmeryPanela() { return minimálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela(int, int) minimálneRozmeryPanela}.</p> */
	public void najmenšieRozmeryPanela(int šírka, int výška)
	{ minimálneRozmeryPanela(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela() minimálneRozmeryPanela}.</p> */
	public int[] najmensieRozmeryPanela() { return minimálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryPanela(int, int) minimálneRozmeryPanela}.</p> */
	public void najmensieRozmeryPanela(int šírka, int výška)
	{ minimálneRozmeryPanela(šírka, výška); }

	/**
	 * <p>Vráti nastavené minimálne rozmery celého okna vrátane jeho okrajov a ponuky.
	 * Prvý prvok poľa je šírka a druhý výška v pixeloch. Nula označuje
	 * rozmer bez vlastného obmedzenia. Zmena vráteného poľa nastavenie
	 * neovplyvní.</p>
	 * 
	 * @return nové dvojprvkové pole so šírkou a výškou
	 * @see #minimálneRozmeryOkna(int, int)
	 */
	public int[] minimálneRozmeryOkna()
	{ return hraniceRozmerov.čítaj(HraniceRozmerov.MIN_OKNO); }

	/**
	 * <p>Nastaví minimálne rozmery celého okna vrátane jeho okrajov a ponuky.
	 * Nulou sa obmedzenie príslušného rozmeru zruší. Ak je niektorý
	 * argument záporný, ponechajú sa obe doterajšie hodnoty.</p>
	 * 
	 * <p>Hranice panela a okna platia súčasne. Ak sa maximum nedá
	 * dodržať spolu s minimom, prednosť má minimum. Rozmery plátna
	 * sa nemenia. Hranice sa uplatnia pri najbližšom spracovaní
	 * udalostí bežného okna; režim celej obrazovky má vlastné rozmery.</p>
	 * 
	 * @param šírka požadovaná šírka v pixeloch
	 * @param výška požadovaná výška v pixeloch
	 * @see #minimálneRozmeryOkna()
	 */
	public void minimálneRozmeryOkna(int šírka, int výška)
	{ hraniceRozmerov.nastav(HraniceRozmerov.MIN_OKNO, šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna() minimálneRozmeryOkna}.</p> */
	public int[] minimalneRozmeryOkna() { return minimálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna(int, int) minimálneRozmeryOkna}.</p> */
	public void minimalneRozmeryOkna(int šírka, int výška)
	{ minimálneRozmeryOkna(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna() minimálneRozmeryOkna}.</p> */
	public int[] najmenšieRozmeryOkna() { return minimálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna(int, int) minimálneRozmeryOkna}.</p> */
	public void najmenšieRozmeryOkna(int šírka, int výška)
	{ minimálneRozmeryOkna(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna() minimálneRozmeryOkna}.</p> */
	public int[] najmensieRozmeryOkna() { return minimálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #minimálneRozmeryOkna(int, int) minimálneRozmeryOkna}.</p> */
	public void najmensieRozmeryOkna(int šírka, int výška)
	{ minimálneRozmeryOkna(šírka, výška); }

	/**
	 * <p>Vráti nastavené maximálne rozmery viditeľného hlavného panela.
	 * Prvý prvok poľa je šírka a druhý výška v pixeloch. Nula označuje
	 * rozmer bez vlastného obmedzenia. Zmena vráteného poľa nastavenie
	 * neovplyvní.</p>
	 * 
	 * @return nové dvojprvkové pole so šírkou a výškou
	 * @see #maximálneRozmeryPanela(int, int)
	 */
	public int[] maximálneRozmeryPanela()
	{ return hraniceRozmerov.čítaj(HraniceRozmerov.MAX_PANEL); }

	/**
	 * <p>Nastaví maximálne rozmery viditeľného hlavného panela.
	 * Nulou sa obmedzenie príslušného rozmeru zruší. Ak je niektorý
	 * argument záporný, ponechajú sa obe doterajšie hodnoty.</p>
	 * 
	 * <p>Hranice panela a okna platia súčasne. Ak sa maximum nedá
	 * dodržať spolu s minimom, prednosť má minimum. Rozmery plátna
	 * sa nemenia. Hranice sa uplatnia pri najbližšom spracovaní
	 * udalostí bežného okna; režim celej obrazovky má vlastné rozmery.</p>
	 * 
	 * @param šírka požadovaná šírka v pixeloch
	 * @param výška požadovaná výška v pixeloch
	 * @see #maximálneRozmeryPanela()
	 */
	public void maximálneRozmeryPanela(int šírka, int výška)
	{ hraniceRozmerov.nastav(HraniceRozmerov.MAX_PANEL, šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela() maximálneRozmeryPanela}.</p> */
	public int[] maximalneRozmeryPanela() { return maximálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela(int, int) maximálneRozmeryPanela}.</p> */
	public void maximalneRozmeryPanela(int šírka, int výška)
	{ maximálneRozmeryPanela(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela() maximálneRozmeryPanela}.</p> */
	public int[] najväčšieRozmeryPanela() { return maximálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela(int, int) maximálneRozmeryPanela}.</p> */
	public void najväčšieRozmeryPanela(int šírka, int výška)
	{ maximálneRozmeryPanela(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela() maximálneRozmeryPanela}.</p> */
	public int[] najvacsieRozmeryPanela() { return maximálneRozmeryPanela(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryPanela(int, int) maximálneRozmeryPanela}.</p> */
	public void najvacsieRozmeryPanela(int šírka, int výška)
	{ maximálneRozmeryPanela(šírka, výška); }

	/**
	 * <p>Vráti nastavené maximálne rozmery celého okna vrátane jeho okrajov a ponuky.
	 * Prvý prvok poľa je šírka a druhý výška v pixeloch. Nula označuje
	 * rozmer bez vlastného obmedzenia. Zmena vráteného poľa nastavenie
	 * neovplyvní.</p>
	 * 
	 * @return nové dvojprvkové pole so šírkou a výškou
	 * @see #maximálneRozmeryOkna(int, int)
	 */
	public int[] maximálneRozmeryOkna()
	{ return hraniceRozmerov.čítaj(HraniceRozmerov.MAX_OKNO); }

	/**
	 * <p>Nastaví maximálne rozmery celého okna vrátane jeho okrajov a ponuky.
	 * Nulou sa obmedzenie príslušného rozmeru zruší. Ak je niektorý
	 * argument záporný, ponechajú sa obe doterajšie hodnoty.</p>
	 * 
	 * <p>Hranice panela a okna platia súčasne. Ak sa maximum nedá
	 * dodržať spolu s minimom, prednosť má minimum. Rozmery plátna
	 * sa nemenia. Hranice sa uplatnia pri najbližšom spracovaní
	 * udalostí bežného okna; režim celej obrazovky má vlastné rozmery.</p>
	 * 
	 * @param šírka požadovaná šírka v pixeloch
	 * @param výška požadovaná výška v pixeloch
	 * @see #maximálneRozmeryOkna()
	 */
	public void maximálneRozmeryOkna(int šírka, int výška)
	{ hraniceRozmerov.nastav(HraniceRozmerov.MAX_OKNO, šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna() maximálneRozmeryOkna}.</p> */
	public int[] maximalneRozmeryOkna() { return maximálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna(int, int) maximálneRozmeryOkna}.</p> */
	public void maximalneRozmeryOkna(int šírka, int výška)
	{ maximálneRozmeryOkna(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna() maximálneRozmeryOkna}.</p> */
	public int[] najväčšieRozmeryOkna() { return maximálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna(int, int) maximálneRozmeryOkna}.</p> */
	public void najväčšieRozmeryOkna(int šírka, int výška)
	{ maximálneRozmeryOkna(šírka, výška); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna() maximálneRozmeryOkna}.</p> */
	public int[] najvacsieRozmeryOkna() { return maximálneRozmeryOkna(); }

	/** <p><a class="alias"></a> Alias pre {@link #maximálneRozmeryOkna(int, int) maximálneRozmeryOkna}.</p> */
	public void najvacsieRozmeryOkna(int šírka, int výška)
	{ maximálneRozmeryOkna(šírka, výška); }

	/**
	 * <p>Zistí šírku tohto okna.</p>
	 * 
	 * @return šírka tohto okna
	 */
	public int šírkaOkna()
	{
		if (null != oknoCelejObrazovky)
			return oknoCelejObrazovky.getWidth();
		if (null != okno) return okno.getWidth();
		return počiatočnáŠírka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #šírkaOkna() šírkaOkna}.</p> */
	public int sirkaOkna() { return šírkaOkna(); }

	/**
	 * <p>Zistí výšku tohto okna.</p>
	 * 
	 * @return výška tohto okna
	 */
	public int výškaOkna()
	{
		if (null != oknoCelejObrazovky)
			return oknoCelejObrazovky.getHeight();
		if (null != okno) return okno.getHeight();
		return počiatočnáŠírka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #výškaOkna() výškaOkna}.</p> */
	public int vyskaOkna() { return výškaOkna(); }


	/**
	 * <p>Vráti inštanciu aktuálneho okna podľa aktuálneho režimu. Ak je okno
	 * v klasickom režime, tak je vrátená inštancia klasického okna, v režime
	 * celej obrazovky je to inštancia okna celej obrazovky. Ak nie je
	 * inicializované žiadne okno (čo by sa nemalo stať, jedine v prípade
	 * chyby), tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return inštancia okna alebo {@code valnull}
	 */
	public JFrame okno()
	{
		if (null != oknoCelejObrazovky) return oknoCelejObrazovky;
		if (null != okno) return okno;
		return null;
	}


	// Titulok

	/**
	 * <p>Vráti titulok tohto okna. Ak metóda zistí, že
	 * aktuálny titulok sa zhoduje s predvoleným titulkom (prípadne ak
	 * toto okno ešte nejestvuje), tak vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return titulok tohto okna alebo {@code valnull}
	 */
	public String titulok()
	{
		if (null != oknoCelejObrazovky)
		{
			String titulok = oknoCelejObrazovky.getTitle();
			if (/*versionString*/Svet.svet.getTitle().equals(titulok))
				titulok = null;
			return titulok;
		}

		if (null != okno)
		{
			String titulok = okno.getTitle();
			if (/*versionString*/Svet.svet.getTitle().equals(titulok))
				titulok = null;
			return titulok;
		}

		return null;
	}

	/**
	 * <p>Nastaví titulok tohto okna. Ak je zadaná hodnota
	 * {@code valnull}, tak metóda nastaví predvolený titulok okna.</p>
	 * 
	 * @param titulok nový titulok tohto okna alebo
	 *     {@code valnull} na nastavenie predvoleného titulku okna
	 */
	public void titulok(String titulok)
	{
		if (null == titulok) titulok = /*versionString*/Svet.svet.getTitle();
		if (null != okno) okno.setTitle(titulok);
		if (null != oknoCelejObrazovky)
			oknoCelejObrazovky.setTitle(titulok);
	}


	// Viditeľnosť

	/**
	 * <p>Zistí, či je toto okno viditeľné. <!-- TODO? -->
	 * Alternatívou tejto metódy je metóda {@link #zobrazené() zobrazené}.</p>
	 * 
	 * @return {@code valtrue} – áno; {@code valfalse} – nie
	 * 
	 * @see #zobrazené()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean viditeľné()
	{
		if (null == okno && null == oknoCelejObrazovky) return zobrazPriŠtarte;
		if (null != oknoCelejObrazovky) return oknoCelejObrazovky.isVisible();
		return okno.isVisible();
	}

	/** <p><a class="alias"></a> Alias pre {@link #viditeľné() viditeľné}.</p> */
	public boolean viditelne() { return viditeľné(); }

	/**
	 * <p>Zistí, či je toto okno viditeľné. <!-- TODO? -->
	 * Alternatívou tejto metódy je metóda {@link #viditeľné() viditeľné}.</p>
	 * 
	 * @return {@code valtrue} – áno; {@code valfalse} – nie
	 * 
	 * @see #viditeľné()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean zobrazené()
	{
		if (null == okno && null == oknoCelejObrazovky) return zobrazPriŠtarte;
		if (null != oknoCelejObrazovky) return oknoCelejObrazovky.isVisible();
		return okno.isVisible();
	}

	/** <p><a class="alias"></a> Alias pre {@link #zobrazené() zobrazené}.</p> */
	public boolean zobrazene() { return zobrazené(); }

	/**
	 * <p>Skryje toto okno.</p>
	 * 
	 * @see #zobraz()
	 * @see #viditeľné()
	 * @see #zobrazené()
	 */
	public void skry()
	{
		// zobrazPriŠtarte = false;
		if (null != oknoCelejObrazovky) oknoCelejObrazovky.setVisible(false);
		else if (null != okno) okno.setVisible(false);
	}

	/**
	 * <p>Zobrazí toto okno. Metóda je protikladom metódy {@link #skry()
	 * skry}.</p>
	 * 
	 * @see #skry()
	 * @see #viditeľné()
	 * @see #zobrazené()
	 */
	public void zobraz()
	{
		// zobrazPriŠtarte = true;
		if (null != oknoCelejObrazovky) oknoCelejObrazovky.setVisible(true);
		else if (null != okno) okno.setVisible(true);
	}


	// Rôzne: (ne)upevnenie, zbalenie, vystredenie, umiestnenie na zariadenie,
	// minimalizovanie…

	/**
	 * <p>Upevní toto okno. To znamená, že od tohto okamihu bude mať okno
	 * pevnú veľkosť. Opakom je metóda {@link #uvoľni() uvoľni}.</p>
	 * 
	 * @see #uvoľni()
	 * @see #zbaľ()
	 * @see #vystreď()
	 */
	public void upevni() { okno.setResizable(false); }

	/**
	 * <p>Uvoľní toto okno. To znamená, že od tohto okamihu okno nebude mať
	 * pevnú veľkosť (čo je predvolené správanie okna).</p>
	 * 
	 * @see #upevni()
	 * @see #zbaľ()
	 * @see #vystreď()
	 */
	public void uvoľni() { okno.setResizable(true); }

	/** <p><a class="alias"></a> Alias pre {@link #uvoľni() uvoľni}.</p> */
	public void uvolni() { okno.setResizable(true); }

	/**
	 * <p>Prispôsobí veľkosť okna tak, aby sa do neho pohodlne vošli všetky
	 * viditeľné komponenty.</p>
	 * 
	 * @see #upevni()
	 * @see #uvoľni()
	 * @see #vystreď()
	 */
	public void zbaľ() { okno.pack(); }

	/** <p><a class="alias"></a> Alias pre {@link #zbaľ() zbaľ}.</p> */
	public void zbal() { okno.pack(); }

	/**
	 * <p>Presunie okno tak, aby sa nachádzalo v strede obrazovky.</p>
	 * 
	 * @see #upevni()
	 * @see #uvoľni()
	 * @see #zbaľ()
	 */
	public void vystreď() { okno.setLocationRelativeTo(null); }

	/** <p><a class="alias"></a> Alias pre {@link #vystreď() vystreď}.</p> */
	public void vystred() { okno.setLocationRelativeTo(null); }


	/**
	 * <p>Umiestni okno na určené zobrazovacie zariadenie. Zariadenie je
	 * určené jeho „poradovým číslom“ (indexom; čiže nula označuje prvé
	 * zariadenie). Okno je umiestnené tak, aby bola na určenom zariadení
	 * viditeľná celá jeho plocha, ak to rozmery okna dovoľujú. Ak je okno
	 * väčšie, než sú rozmery zariadenia, tak je umiestnené do ľavého horného
	 * rohu zariadenia. Ak zariadenie so zadaným indexom nejestvuje, tak nie
	 * je vykonaná žiadna operácia.</p>
	 * 
	 * @param zariadenie číslo zariadenia, na ktoré má byť okno umiestnené
	 * 
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public void premiestniNaZariadenie(int zariadenie)
	{
		GraphicsDevice[] zariadenia = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getScreenDevices();

		if (zariadenie < zariadenia.length)
		{
			GraphicsConfiguration konfigurácia =
				zariadenia[zariadenie].getDefaultConfiguration();
			Rectangle2D hraniceZariadenia = konfigurácia.getBounds();

			Rectangle2D hraniceOkna = (null == oknoCelejObrazovky) ?
				okno.getBounds() : oknoCelejObrazovky.getBounds();
			// TODO – otestovať okno celej obrazovky.

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

			if (null == oknoCelejObrazovky)
				okno.setLocation((int)polohaX, (int)polohaY);
			else
				oknoCelejObrazovky.setLocation((int)polohaX, (int)polohaY);
			// TODO – otestovať okno celej obrazovky.
		}
	}

	/**
	 * <p>Umiestni okno na predvolené zobrazovacie zariadenie.
	 * Okno je umiestnené tak, aby bola viditeľná celá jeho plocha,
	 * ak to jeho rozmery dovoľujú. Ak je okno väčšie, než sú rozmery
	 * zariadenia, tak je umiestnené do ľavého horného rohu.</p>
	 * 
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public void premiestniNaZariadenie()
	{ premiestniNaZariadenie(0); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
	public void premiestniNaObrazovku(int zariadenie)
	{ premiestniNaZariadenie(zariadenie); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
	public void premiestniNaObrazovku()
	{ premiestniNaZariadenie(0); }


	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
	public void presuňNaZariadenie(int zariadenie)
	{ premiestniNaZariadenie(zariadenie); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
	public void presuňNaZariadenie()
	{ premiestniNaZariadenie(0); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
	public void presunNaZariadenie(int zariadenie)
	{ premiestniNaZariadenie(zariadenie); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
	public void presunNaZariadenie()
	{ premiestniNaZariadenie(0); }


	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
	public void presuňNaObrazovku(int zariadenie)
	{ premiestniNaZariadenie(zariadenie); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
	public void presuňNaObrazovku()
	{ premiestniNaZariadenie(0); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie(int) premiestniNaZariadenie}.</p> */
	public void presunNaObrazovku(int zariadenie)
	{ premiestniNaZariadenie(zariadenie); }

	/** <p><a class="alias"></a> Alias pre {@link #premiestniNaZariadenie() premiestniNaZariadenie}.</p> */
	public void presunNaObrazovku()
	{ premiestniNaZariadenie(0); }


	/**
	 * <p>Zistí číslo zariadenia, na ktorom sa nachádza toto okno.
	 * Zariadenie je určené jeho „poradovým číslom“ (indexom; čiže
	 * nula označuje prvé zariadenie). Pri zisťovaní polohy je braný do
	 * úvahy stred okna. Ak také zariadenie, do ktorého plochy by patril
	 * stred okna nejestvuje, tak je vrátená hodnota {@code num-1}.</p>
	 * 
	 * @return číslo zariadenia, do ktorého patrí stred okna alebo
	 *     {@code num-1}, ak také zariadenie nejestvuje
	 * 
	 * @see #premiestniNaZariadenie()
	 * @see #premiestniNaZariadenie(int)
	 * @see Svet#početZariadení()
	 */
	public int zistiZariadenieOkna()
	{
		GraphicsDevice[] zariadenia = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getScreenDevices();

		Rectangle2D hraniceOkna = (null == oknoCelejObrazovky) ?
			okno.getBounds() : oknoCelejObrazovky.getBounds();
		// TODO – otestovať okno celej obrazovky.

		double stredX = hraniceOkna.getX() + (hraniceOkna.getWidth()  / 2);
		double stredY = hraniceOkna.getY() + (hraniceOkna.getHeight() / 2);

		for (int zariadenie = 0; zariadenie <
			zariadenia.length; ++zariadenie)
		{
			GraphicsConfiguration konfigurácia =
				zariadenia[zariadenie].getDefaultConfiguration();
			Rectangle2D hraniceZariadenia = konfigurácia.getBounds();

			if (stredX >= hraniceZariadenia.getX() &&
				(stredX <= hraniceZariadenia.getX() +
					hraniceZariadenia.getWidth()) &&
				stredY >= hraniceZariadenia.getY() &&
				(stredY <= hraniceZariadenia.getY() +
					hraniceZariadenia.getHeight()))
				return zariadenie;
		}

		return -1;
	}


	/**
	 * <p>Zistí, či je toto okno v maximalizovanom stave.</p>
	 * 
	 * @return ak je okno v maximalizovanom stave, tak vráti hodnotu
	 *     {@code valtrue}, inak {@code valfalse}
	 * 
	 * @see ObsluhaUdalostí#maximalizovanie()
	 * @see #minimalizované()
	 * @see #normálne()
	 * @see #maximalizuj()
	 * @see #minimalizuj()
	 * @see #obnov()
	 */
	public boolean maximalizované()
	{
		if (null == oknoCelejObrazovky)
			return 0 != (okno.getExtendedState() & MAXIMIZED_BOTH);

		return 0 != (oknoCelejObrazovky.getExtendedState() & MAXIMIZED_BOTH);
	}

	/** <p><a class="alias"></a> Alias pre {@link #maximalizované() maximalizované}.</p> */
	public boolean maximalizovane()
	{
		return maximalizované();
	}


	/**
	 * <p>Pokúsi sa prepnúť toto okno do maximalizovaného stavu. O tom,
	 * či sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
	 * #maximalizované() maximalizované}.</p>
	 * 
	 * @see ObsluhaUdalostí#maximalizovanie()
	 * @see #maximalizované()
	 * @see #minimalizované()
	 * @see #normálne()
	 * @see #minimalizuj()
	 * @see #obnov()
	 */
	public void maximalizuj()
	{
		if (null == oknoCelejObrazovky)
			okno.setExtendedState(MAXIMIZED_BOTH);
		else
			oknoCelejObrazovky.setExtendedState(MAXIMIZED_BOTH);
	}


	/**
	 * <p>Zistí, či je toto okno v minimalizovanom stave.</p>
	 * 
	 * @return ak je okno v minimalizovanom stave, tak vráti hodnotu
	 *     {@code valtrue}, inak {@code valfalse}
	 * 
	 * @see ObsluhaUdalostí#minimalizovanie()
	 * @see #maximalizované()
	 * @see #normálne()
	 * @see #maximalizuj()
	 * @see #minimalizuj()
	 * @see #obnov()
	 */
	public boolean minimalizované()
	{
		if (null == oknoCelejObrazovky)
			return 0 != (okno.getExtendedState() & ICONIFIED);

		return 0 != (oknoCelejObrazovky.getExtendedState() & ICONIFIED);
	}

	/** <p><a class="alias"></a> Alias pre {@link #minimalizované() minimalizované}.</p> */
	public boolean minimalizovane()
	{
		return minimalizované();
	}


	/**
	 * <p>Pokúsi sa prepnúť toto okno do minimalizovaného stavu. O tom,
	 * či sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
	 * #minimalizované() minimalizované}.</p>
	 * 
	 * @see ObsluhaUdalostí#minimalizovanie()
	 * @see #maximalizované()
	 * @see #minimalizované()
	 * @see #normálne()
	 * @see #maximalizuj()
	 * @see #obnov()
	 */
	public void minimalizuj()
	{
		if (null == oknoCelejObrazovky)
			okno.setExtendedState(ICONIFIED);
		else
			oknoCelejObrazovky.setExtendedState(ICONIFIED);
	}


	/**
	 * <p>Zistí, či je toto okno v normálnom stave.</p>
	 * 
	 * @return ak je okno v normálnom stave, tak vráti hodnotu
	 *     {@code valtrue}, inak {@code valfalse}
	 * 
	 * @see ObsluhaUdalostí#obnovenie()
	 * @see #maximalizované()
	 * @see #minimalizované()
	 * @see #maximalizuj()
	 * @see #minimalizuj()
	 * @see #obnov()
	 */
	public boolean normálne()
	{
		if (null == oknoCelejObrazovky)
			return NORMAL == okno.getExtendedState();

		return NORMAL == oknoCelejObrazovky.getExtendedState();
	}

	/** <p><a class="alias"></a> Alias pre {@link #normálne() normálne}.</p> */
	public boolean normalne()
	{
		return normálne();
	}

	/**
	 * <p>Pokúsi sa prepnúť toto okno do normálneho stavu. (V zmysle
	 * „nie maximalizovaného a nie minimalizovaného“ stavu.) O tom, či
	 * sa táto akcia podarila sa dá presvedčiť volaním metódy {@link 
	 * #normálne() normálne}.</p>
	 * 
	 * @see ObsluhaUdalostí#obnovenie()
	 * @see #maximalizované()
	 * @see #minimalizované()
	 * @see #normálne()
	 * @see #maximalizuj()
	 * @see #minimalizuj()
	 */
	public void obnov()
	{
		if (null == oknoCelejObrazovky)
			okno.setExtendedState(NORMAL);
		else
			oknoCelejObrazovky.setExtendedState(NORMAL);
	}


	// Presúvanie komponentov

	/**
	 * <p>Prenesie tlačidlo zo sveta do tohto okna alebo späť.</p>
	 * 
	 * <p>Ak je parameter {@code sem} rovný {@code valtrue} a zároveň sa
	 * tlačidlo nachádza vo svete (pozor, nie v inom okne, musí byť umestnené
	 * vo svete), tak bude prenesené do tohto okna. Ak je paramete
	 * r {@code sem} rovný {@code valfalse} a zároveň sa tlačidlo nachádza
	 * v tomto okne (musí to byť presne toto okno), tak bude prenesené z tohto
	 * okna do sveta.</p>
	 * 
	 * <p>V uvedených dvoch situáciách je návratová hodnota tejto metódy
	 * inštancia zadaného tlačidla, čo umožňuje zreťazené volanie ďalšej
	 * metódy tlačidla – pri inicializácii rozhrania a prenose tlačidla zo
	 * sveta do tohto okna sa na to dá spoľahnúť. Ak je však prenos neúspešný,
	 * tak je vrátená hodnota {@code valnull}.</p>
	 * 
	 * <p>Volanie tejto metódy je ekvivalentné volaniu metódy
	 * {@link Tlačidlo#prenes(Okno okno, boolean tam)}.</p>
	 * 
	 * @param tlačidlo tlačidlo, ktoré má byť prenesené
	 * @param sem smer prenosu (pozri opis vyššie)
	 * @return inštancia zadaného tlačidla alebo {@code valnull}
	 */
	public Tlačidlo prenes(Tlačidlo tlačidlo, boolean sem)
	{
		if (sem && Svet.hlavnýPanel.isAncestorOf(tlačidlo))
		{
			tlačidlo.removeKeyListener(Svet.udalostiOkna);
			Svet.hlavnýPanel.remove(tlačidlo);
			Svet.hlavnýPanel.doLayout();

			double x = tlačidlo.polohaX();
			double y = tlačidlo.polohaY();
			tlačidlo.šírkaRodiča = ikonaOkna.getIconWidth();
			tlačidlo.výškaRodiča = ikonaOkna.getIconHeight();
			tlačidlo.poloha(x, y);

			hlavnýPanel.add(tlačidlo, 0);
			hlavnýPanel.doLayout();
			tlačidlo.addKeyListener(udalostiOkna);
			tlačidlo.hlavnýPanel = hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return tlačidlo;
		}
		else if (!sem && hlavnýPanel.isAncestorOf(tlačidlo))
		{
			tlačidlo.removeKeyListener(udalostiOkna);
			hlavnýPanel.remove(tlačidlo);
			hlavnýPanel.doLayout();

			double x = tlačidlo.polohaX();
			double y = tlačidlo.polohaY();
			tlačidlo.šírkaRodiča = Plátno.šírkaPlátna;
			tlačidlo.výškaRodiča = Plátno.výškaPlátna;
			tlačidlo.poloha(x, y);

			Svet.hlavnýPanel.add(tlačidlo, 0);
			Svet.hlavnýPanel.doLayout();
			tlačidlo.addKeyListener(Svet.udalostiOkna);
			tlačidlo.hlavnýPanel = Svet.hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return tlačidlo;
		}

		return null;
	}

	/**
	 * <p>Zistí, či je zadané tlačidlo umiestnené v tomto okne.</p>
	 * 
	 * @return {@code valtrue} ak je tlačidlo v tomto okne, {@code valfalse}
	 *     v opačnom prípade
	 */
	public boolean jeTu(Tlačidlo tlačidlo)
	{
		return hlavnýPanel.isAncestorOf(tlačidlo);
	}


	/**
	 * <p>Prenesie rolovaciu lištu zo sveta do tohto okna alebo späť.</p>
	 * 
	 * <p>Ak je parameter {@code sem} rovný {@code valtrue} a zároveň sa
	 * lišta nachádza vo svete (pozor, nie v inom okne, musí byť umestnená
	 * vo svete), tak bude prenesená do tohto okna. Ak je parameter
	 * {@code sem} rovný {@code valfalse} a zároveň sa lišta nachádza v tomto
	 * okne (musí to byť presne toto okno), tak bude prenesená z tohto okna
	 * do sveta.</p>
	 * 
	 * <p>V uvedených dvoch situáciách je návratová hodnota tejto metódy
	 * inštancia zadanej lišty, čo umožňuje zreťazené volanie ďalšej metódy
	 * lišty – pri inicializácii rozhrania a prenose lišty zo sveta do tohto
	 * okna sa na to dá spoľahnúť. Ak je však prenos neúspešný, tak je vrátená
	 * hodnota {@code valnull}.</p>
	 * 
	 * <p>Volanie tejto metódy je ekvivalentné volaniu metódy
	 * {@link RolovaciaLišta#prenes(Okno okno, boolean tam)}.</p>
	 * 
	 * @param rolovaciaLišta rolovacia lišta, ktorá má byť prenesená
	 * @param sem smer prenosu (pozri opis vyššie)
	 * @return inštancia zadanej lišty alebo {@code valnull}
	 */
	public RolovaciaLišta prenes(RolovaciaLišta rolovaciaLišta, boolean sem)
	{
		if (sem && Svet.hlavnýPanel.isAncestorOf(rolovaciaLišta))
		{
			Svet.hlavnýPanel.remove(rolovaciaLišta);
			Svet.hlavnýPanel.doLayout();

			double x = rolovaciaLišta.polohaX();
			double y = rolovaciaLišta.polohaY();
			rolovaciaLišta.šírkaRodiča = ikonaOkna.getIconWidth();
			rolovaciaLišta.výškaRodiča = ikonaOkna.getIconHeight();
			rolovaciaLišta.poloha(x, y);

			hlavnýPanel.add(rolovaciaLišta, 0);
			hlavnýPanel.doLayout();
			rolovaciaLišta.hlavnýPanel = hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return rolovaciaLišta;
		}
		else if (!sem && hlavnýPanel.isAncestorOf(rolovaciaLišta))
		{
			hlavnýPanel.remove(rolovaciaLišta);
			hlavnýPanel.doLayout();

			double x = rolovaciaLišta.polohaX();
			double y = rolovaciaLišta.polohaY();
			rolovaciaLišta.šírkaRodiča = Plátno.šírkaPlátna;
			rolovaciaLišta.výškaRodiča = Plátno.výškaPlátna;
			rolovaciaLišta.poloha(x, y);

			Svet.hlavnýPanel.add(rolovaciaLišta, 0);
			Svet.hlavnýPanel.doLayout();
			rolovaciaLišta.hlavnýPanel = Svet.hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return rolovaciaLišta;
		}

		return null;
	}

	/**
	 * <p>Zistí, či je zadaná rolovacia lišta umiestnená v tomto okne.</p>
	 * 
	 * @return {@code valtrue} ak je lišta v tomto okne, {@code valfalse}
	 *     v opačnom prípade
	 */
	public boolean jeTu(RolovaciaLišta rolovaciaLišta)
	{
		return hlavnýPanel.isAncestorOf(rolovaciaLišta);
	}


	/**
	 * <p>Prenesie poznámkový blok zo sveta do tohto okna alebo späť.</p>
	 * 
	 * <p>Ak je parameter {@code sem} rovný {@code valtrue} a zároveň sa blok
	 * nachádza vo svete (pozor, nie v inom okne, musí byť umestnený vo
	 * svete), tak bude prenesený do tohto okna. Ak je parameter {@code sem}
	 * rovný {@code valfalse} a zároveň sa blok nachádza v tomto okne (musí
	 * to byť presne toto okno), tak bude prenesený z tohto okna do sveta.</p>
	 * 
	 * <p>V uvedených dvoch situáciách je návratová hodnota tejto metódy
	 * inštancia zadaného poznámkového bloku, čo umožňuje zreťazené volanie
	 * ďalšej metódy bloku – pri inicializácii rozhrania a prenose bloku zo
	 * sveta do tohto okna sa na to dá spoľahnúť. Ak je však prenos neúspešný,
	 * tak je vrátená hodnota {@code valnull}.</p>
	 * 
	 * <p>Volanie tejto metódy je ekvivalentné volaniu metódy
	 * {@link PoznámkovýBlok#prenes(Okno okno, boolean tam)}.</p>
	 * 
	 * @param poznámkovýBlok poznámkový blok, ktorý má byť prenesený
	 * @param sem smer prenosu (pozri opis vyššie)
	 * @return inštancia zadaného poznámkového bloku alebo {@code valnull}
	 */
	public PoznámkovýBlok prenes(PoznámkovýBlok poznámkovýBlok, boolean sem)
	{
		if (sem && Svet.hlavnýPanel.isAncestorOf(poznámkovýBlok.rolovanie()))
		{
			Svet.hlavnýPanel.remove(poznámkovýBlok.rolovanie());
			Svet.hlavnýPanel.doLayout();

			double x = poznámkovýBlok.polohaX();
			double y = poznámkovýBlok.polohaY();
			poznámkovýBlok.šírkaRodiča = ikonaOkna.getIconWidth();
			poznámkovýBlok.výškaRodiča = ikonaOkna.getIconHeight();
			poznámkovýBlok.poloha(x, y);

			hlavnýPanel.add(poznámkovýBlok.rolovanie(), 0);
			hlavnýPanel.doLayout();
			poznámkovýBlok.hlavnýPanel = hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return poznámkovýBlok;
		}
		else if (!sem && hlavnýPanel.isAncestorOf(poznámkovýBlok.rolovanie()))
		{
			hlavnýPanel.remove(poznámkovýBlok.rolovanie());
			hlavnýPanel.doLayout();

			double x = poznámkovýBlok.polohaX();
			double y = poznámkovýBlok.polohaY();
			poznámkovýBlok.šírkaRodiča = Plátno.šírkaPlátna;
			poznámkovýBlok.výškaRodiča = Plátno.výškaPlátna;
			poznámkovýBlok.poloha(x, y);

			Svet.hlavnýPanel.add(poznámkovýBlok.rolovanie(), 0);
			Svet.hlavnýPanel.doLayout();
			poznámkovýBlok.hlavnýPanel = Svet.hlavnýPanel;

			Svet.prekresli();
			prekresli();

			return poznámkovýBlok;
		}

		return null;
	}

	/**
	 * <p>Zistí, či je zadaný poznámkový blok umiestnený v tomto okne.</p>
	 * 
	 * @return {@code valtrue} ak je blok v tomto okne, {@code valfalse}
	 *     v opačnom prípade
	 */
	public boolean jeTu(PoznámkovýBlok poznámkovýBlok)
	{
		return hlavnýPanel.isAncestorOf(poznámkovýBlok.rolovanie());
	}


	// Hlavný panel a prekreslenie

	/**
	 * <p>Poskytne komponent tohto panela vloženého v tomto okne.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Neodborná manipulácia
	 * s týmto komponentom môže mať nežiaduce vedľajšie účinky.</p>
	 * 
	 * @return hlavný panel tohto okna
	 */
	public JPanel hlavnýPanel() { return hlavnýPanel; }

	/** <p><a class="alias"></a> Alias pre {@link #hlavnýPanel() hlavnýPanel}.</p> */
	public JPanel hlavnyPanel() { return hlavnýPanel; }


	/**
	 * <p>Prikáže aktualizovať obsah tohto okna. Túto metódu sa oplatí vykonať
	 * vždy po úprave grafického obsahu vnútorného obrázka (napríklad po
	 * kreslení niektorého z robotov do neho) – pozri aj parameter
	 * {@code obrázok} všetkých konštruktorov.</p>
	 */
	public void prekresli()
	{
		try
		{
			if (null == oknoCelejObrazovky) okno.repaint();
			else oknoCelejObrazovky.repaint();
		}
		catch (Exception e) { GRobotException.vypíšChybovéHlásenia(e); }
	}


	// Ikona okona

	/**
	 * <p><a class="setter"></a> Nastaví oknu novú ikonu.</p>
	 * 
	 * <p>Pozri aj {@link Svet#ikona(String) Svet.ikona(súbor)}.</p>
	 * 
	 * @param súbor názov súboru s obrázkom
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 */
	public void ikona(String súbor)
	{
		okno.setIconImage(Obrázok.súborNaObrázok(súbor));
		if (null != oknoCelejObrazovky)
			oknoCelejObrazovky.setIconImage(okno.getIconImage());
	}

	/**
	 * <p>Nastaví oknu ikonu podľa zadaného obrázka.</p>
	 * 
	 * @param obrázok obrázok slúžiaci ako predloha pre ikonu
	 */
	public void ikona(Image obrázok)
	{
		okno.setIconImage(obrázok);
		if (null != oknoCelejObrazovky)
			oknoCelejObrazovky.setIconImage(obrázok);
	}

	/**
	 * <p>Prečíta ikonu okna a prevedie ju na obrázok.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda vracia objekt typu
	 * {@link Obrazok Obrazok} (t. j. triedy, ktorá je aliasom triedy
	 * {@link Obrázok Obrázok}). Dôvod je uvedený v poznámke v opise metódy
	 * {@link #ikona(String) ikona(súbor)}.</p>
	 * 
	 * @return obrázok s ikonou
	 */
	public Obrazok ikona()
	{ return new Obrazok(okno.getIconImage()); }


	/**
	 * <p>Vráti inštanciu {@linkplain Obrázok obrázka} obsiahnutého v tomto
	 * okne.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda vracia typ {@link 
	 * Obrázok Obrázok} (s diakritikou), pretože nie je technicky možné, aby
	 * cielene vracala inštanciu všeobecnejšieho (odvodeného) typu {@link 
	 * Obrazok Obrazok} (bez diakritiky). Tento typ však môže do okna vložiť
	 * programátor metódou {@link #obrázok(Obrázok) obrázok(obrázok)}. Vtedy
	 * je možné vrátenú inštancia dodatočne pretypovať (po typovej kontrole)
	 * na typ {@link Obrazok Obrazok}.</p>
	 * 
	 * @return inštancia obrázka, ktorý tvorí grafický obsah tohto okna
	 */
	public Obrázok obrázok()
	{
		return obrázok;
	}

	/**
	 * <p>Nastaví nový obrázok oknu. Obrázok je povinný parameter všetkých
	 * konštruktorov. Touto metódou sa však dá vymeniť.</p>
	 * 
	 * @param obrázok inštancia triedy {@link Obrázok Obrázok}, ktorá bude
	 *     tvoriť grafický obsah okna
	 */
	public void obrázok(Obrázok obrázok)
	{
		// Obrázok sa dá nastaviť v konštruktore a tu sa dá zmeniť. Je to však
		// mierne komplikované, lebo sa vkladá do ikony, ktorá sa vkladá do
		// popisu (JLabel), ktorý sa vkladá do panela, ktorý je vlastne obsahom
		// okna. To všetko sa deje pri inicializácii. Tu meníme len obsah
		// ikony:
		this.obrázok = obrázok;
		ikonaOkna.setImage(obrázok);
		prekresli();
	}


	// Farba plochy

	/**
	 * <p><a class="getter"></a> Číta farbu plochy okna. Ide o farbu
	 * základného komponentu okna aplikácie, na ktorom je umiestnený obrázok,
	 * prípadne ďalšie komponenty.</p>
	 * 
	 * @return aktuálna farba plochy okna (objekt typu {@link Farba Farba})
	 */
	public Farba farbaPlochy()
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
	 * <p><a class="setter"></a> Nastaví farbu plochy okna. Ide o farbu
	 * základného komponentu okna, na ktorom je umiestnený obrázok,
	 * prípadne ďalšie komponenty.</p>
	 * 
	 * <p><image>farba-plochy-small.png<alt/>Ukážka farieb
	 * plochy.</image>Ukážka troch farieb plochy. Zľava doprava:
	 * systémom predvolená, {@link Farebnosť#papierová papierová}
	 * a {@link Farebnosť#antracitová antracitová}.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu plochy;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna
	 *     čierna}…)
	 */
	public void farbaPlochy(Color nováFarba)
	{ hlavnýPanel.setBackground(nováFarba); }

	/**
	 * <p>Nastaví farbu plochy okna podľa farby zadaného objektu. Plochou
	 * je myslený základný komponent okna aplikácie, na ktorom je umiestnený
	 * obrázok, prípadne ďalšie komponenty.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu plochy
	 * 
	 * @see #farbaPlochy(Color)
	 */
	public void farbaPlochy(Farebnosť objekt)
	{ farbaPlochy(objekt.farba()); }

	/**
	 * <p>Nastaví farbu plochy okna. Plochou je myslený základný komponent
	 * okna aplikácie, na ktorom je umiestnený obrázok, prípadne ďalšie
	 * komponenty.</p>
	 * 
	 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
	 * @return objekt typu {@link Farba Farba} – nová farba plochy
	 * 
	 * @see #farbaPlochy(Color)
	 */
	public Farba farbaPlochy(int r, int g, int b)
	{
		Farba farbaPlochy = new Farba(r, g, b);
		farbaPlochy(farbaPlochy);
		return farbaPlochy;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť plochy okna. Plochou je myslený
	 * základný komponent okna aplikácie, na ktorom je umiestnený obrázok,
	 * prípadne ďalšie komponenty.</p>
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
	public Farba farbaPlochy(int r, int g, int b, int a)
	{
		Farba farbaPlochy = new Farba(r, g, b, a);
		farbaPlochy(farbaPlochy);
		return farbaPlochy;
	}

	/**
	 * <p>Nastaví predvolenú farbu plochy. Ide o farbu základného komponentu
	 * okna aplikácie, na ktorom je umiestnený obrázok, prípadne ďalšie
	 * komponenty. (Predvolenou farbou je obvykle šedá, ale farba sa môže
	 * líšiť v závislosti od operačného systému, jeho nastavení alebo podľa
	 * použitého dizajnu vzhľadu používateľského rozhrania – L&F.)</p>
	 */
	public void predvolenáFarbaPlochy()
	{
		Color predvolenáFarba = UIManager.getColor("Panel.background");
		farbaPlochy(predvolenáFarba);
	}

	/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPlochy() predvolenáFarbaPlochy}.</p> */
	public void predvolenaFarbaPlochy() { predvolenáFarbaPlochy(); }


	// Kurzor myši

	/**
	 * <p>Zmení tomuto oknu tvar kurzora myši buď na vlastný tvar kurzora
	 * vytvorený prostredníctvom metódy {@link Svet#novýKurzorMyši(Image, int,
	 * int, String) Svet.novýKurzorMyši}, alebo na niektorý zo systémom
	 * preddefinovaných kurzorov. Pozri {@link Svet#zmeňKurzorMyši(String)
	 * Svet.zmeňKurzorMyši}. Ak namiesto názvu kurzora zadáte {@code valnull},
	 * bude použitý prázdny (neviditeľný) kurzor.</p>
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
	 */
	public void zmeňKurzorMyši(String meno)
	{
		if (null == meno)
		{
			okno.getContentPane().setCursor(Svet.prázdnyKurzor);

			// Pre celú obrazovku:
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setCursor(Svet.prázdnyKurzor);
			return;
		}

		Cursor kurzor = Svet.dajKurzorMyši(meno);

		if (null != kurzor)
		{
			okno.getContentPane().setCursor(kurzor);

			// Pre celú obrazovku:
			if (null != oknoCelejObrazovky)
				oknoCelejObrazovky.setCursor(kurzor);
			return;
		}

		throw new GRobotException(
			"Kurzor so zadaným menom (" + meno + ") nejestvuje.",
			"cursorNotExists", meno);
	}

	/** <p><a class="alias"></a> Alias pre {@link #zmeňKurzorMyši(String) zmeňKurzorMyši}.</p> */
	public void zmenKurzorMysi(String meno) { zmeňKurzorMyši(meno); }


	// Celá obrazovka

	/**
	 * <p>Pokúsi sa prepnúť toto okno do režimu celej obrazovky.</p>
	 * 
	 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
	 *     znamená úspech a {@code valfalse} neúspech
	 * 
	 * @see #celáObrazovka(int)
	 * @see #celáObrazovka(boolean)
	 * @see #celáObrazovka(int, boolean)
	 * @see #oknoCelejObrazovky()
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public boolean celáObrazovka()
	{ return celáObrazovka(zistiZariadenieOkna(), true); }

	/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka() celáObrazovka}.</p> */
	public boolean celaObrazovka()
	{ return celáObrazovka(zistiZariadenieOkna(), true); }


	/**
	 * <p>Pokúsi sa prepnúť toto okno do režimu celej obrazovky na určenom
	 * zobrazovacom zariadení. Zariadenie je určené jeho poradovým
	 * číslom (indexom; čiže nula označuje prvé zariadenie).</p>
	 * 
	 * @param zariadenie číslo zariadenia, ktoré má byť použité v režime
	 *     celej obrazovky
	 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
	 *     znamená úspech a {@code valfalse} neúspech
	 * 
	 * @see #celáObrazovka()
	 * @see #celáObrazovka(boolean)
	 * @see #celáObrazovka(int, boolean)
	 * @see #oknoCelejObrazovky()
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public boolean celáObrazovka(int zariadenie)
	{ return celáObrazovka(zariadenie, true); }

	/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(int) celáObrazovka}.</p> */
	public boolean celaObrazovka(int zariadenie)
	{ return celáObrazovka(zariadenie, true); }


	/**
	 * <p>Pokúsi sa prepnúť toto okno do režimu celej obrazovky alebo späť.</p>
	 * 
	 * @param celáObrazovka ak je {@code valtrue}, tak má byť režim celej
	 *     obrazovky zapnutý, ak je {@code valfalse}, tak má byť režim celej
	 *     obrazovky vypnutý
	 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
	 *     znamená úspech a {@code valfalse} neúspech
	 * 
	 * @see #celáObrazovka()
	 * @see #celáObrazovka(int)
	 * @see #celáObrazovka(int, boolean)
	 * @see #oknoCelejObrazovky()
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public boolean celáObrazovka(boolean celáObrazovka)
	{ return celáObrazovka(zistiZariadenieOkna(), celáObrazovka); }

	/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(boolean) celáObrazovka}.</p> */
	public boolean celaObrazovka(boolean celáObrazovka)
	{ return celáObrazovka(zistiZariadenieOkna(), celáObrazovka); }

	/**
	 * <p>Hodnota tohto atribútu môže obsahovať vlastnú implementáciu
	 * spôsobu zmeny režimu celej obrazovky. (Predvolený spôsob je
	 * hardvérový – pozri aj {@link ZmenaCelejObrazovky
	 * ZmenaCelejObrazovky}{@code .}{@link ZmenaCelejObrazovky#hardvérová
	 * hardvérová}.)</p>
	 */
	public ZmenaCelejObrazovky zmenaCelejObrazovky = null;

	/**
	 * <p>Pokúsi sa prepnúť toto okno do režimu celej obrazovky alebo späť
	 * na určenom zobrazovacom zariadení. Zariadenie je určené jeho
	 * „poradovým číslom“ (indexom; čiže nula označuje prvé zariadenie).</p>
	 * 
	 * @param zariadenie číslo zariadenia, ktoré má byť použité v režime
	 *     celej obrazovky
	 * @param celáObrazovka ak je {@code valtrue}, tak má byť režim celej
	 *     obrazovky zapnutý, ak je {@code valfalse}, tak má byť režim celej
	 *     obrazovky vypnutý
	 * @return informuje o úspešnosti operácie – hodnota {@code valtrue}
	 *     znamená úspech a {@code valfalse} neúspech
	 * 
	 * @see #celáObrazovka()
	 * @see #celáObrazovka(int)
	 * @see #celáObrazovka(boolean)
	 * @see #oknoCelejObrazovky()
	 * @see #zistiZariadenieOkna()
	 * @see Svet#početZariadení()
	 */
	public boolean celáObrazovka(int zariadenie, boolean celáObrazovka)
	{
		ZmenaCelejObrazovky spôsobZmeny;
		if (null == zmenaCelejObrazovky)
			spôsobZmeny = ZmenaCelejObrazovky.hardvérová;
		else
			spôsobZmeny = zmenaCelejObrazovky;

		GraphicsDevice[] zariadenia = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getScreenDevices();
		if (zariadenie >= zariadenia.length || null == okno) return false;

		ComponentEvent componentEvent;

		// if (null == zavriOkno)
		// 	zavriOkno = new KeyEventDispatcher()
		// 	{
		// 		public boolean dispatchKeyEvent(KeyEvent e)
		// 		{
		// 			if (e.getKeyCode() == KeyEvent.VK_W &&
		// 				e.getModifiers() == Toolkit.getDefaultToolkit().
		// 					getMenuShortcutKeyMask())
		// 			{
		// 				// System.exit(0);
		// 				/*Svet.*/zavrieť(/*0*/);
		// 				return true;
		// 			}
		// 
		// 			return false;
		// 		}
		// 	};

		if (spôsobZmeny.ponechajOkno())
		{
			if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
				spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
					celáObrazovka, okno);
		}
		else if (celáObrazovka && null == oknoCelejObrazovky &&
			null == spôsobZmeny.dajOkno(zariadenie,
				zariadenia[zariadenie]))
		{
			if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
			{
				okno.removeComponentListener(udalostiOkna);
				okno.removeWindowFocusListener(udalostiOkna);
				okno.remove(hlavnýPanel);

				oknoCelejObrazovky = new JFrame();
				oknoCelejObrazovky.setResizable(false);
				oknoCelejObrazovky.setUndecorated(true);
				oknoCelejObrazovky.setIconImage(okno.getIconImage());
				oknoCelejObrazovky.setTitle(okno.getTitle());
				oknoCelejObrazovky.add(hlavnýPanel, BorderLayout.CENTER);
				oknoCelejObrazovky.addComponentListener(udalostiOkna);
				oknoCelejObrazovky.addWindowFocusListener(udalostiOkna);

				// KeyboardFocusManager.getCurrentKeyboardFocusManager().
				// 	addKeyEventDispatcher(zavriOkno);

				spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
					celáObrazovka, oknoCelejObrazovky);
				oknoCelejObrazovky.setVisible(true);
				okno.setVisible(false);

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
		{
			if (spôsobZmeny.jePodpora(zariadenie, zariadenia[zariadenie]))
			{
				spôsobZmeny.zmena(zariadenie, zariadenia[zariadenie],
					celáObrazovka, oknoCelejObrazovky);
				oknoCelejObrazovky.setVisible(false);

				// KeyboardFocusManager.getCurrentKeyboardFocusManager().
				// 	removeKeyEventDispatcher(zavriOkno);

				oknoCelejObrazovky.remove(hlavnýPanel);
				oknoCelejObrazovky.removeComponentListener(udalostiOkna);
				oknoCelejObrazovky.removeWindowFocusListener(udalostiOkna);
				oknoCelejObrazovky = null;

				okno.add(hlavnýPanel, BorderLayout.CENTER);
				okno.addComponentListener(udalostiOkna);
				okno.addWindowFocusListener(udalostiOkna);
				okno.setVisible(true);

				componentEvent = new ComponentEvent(okno,
					ComponentEvent.COMPONENT_MOVED);
				udalostiOkna.componentMoved(componentEvent);

				componentEvent = new ComponentEvent(okno,
					ComponentEvent.COMPONENT_RESIZED);
				udalostiOkna.componentResized(componentEvent);

				return true;
			}
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #celáObrazovka(int, boolean) celáObrazovka}.</p> */
	public boolean celaObrazovka(int zariadenie, boolean celáObrazovka)
	{ return celáObrazovka(zariadenie, celáObrazovka); }

	/**
	 * <p>Ak je okno v {@linkplain #celáObrazovka() režime celej obrazovky},
	 * tak táto metóda vráti inštanciu {@link JFrame okna} celej
	 * obrazovky, inak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return inštancia {@link JFrame okna} alebo {@code valnull}
	 * 
	 * @see #celáObrazovka()
	 */
	public JFrame oknoCelejObrazovky()
	{ return oknoCelejObrazovky; }


	// FIX: 2023-08-24:
	private final static String close = "close";

	@SuppressWarnings("serial")
	private class KlávesováSkratka extends AbstractAction
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
			// FIX: 2023-08-24:
			if (príkaz == close)
			{
				// FIX: 2024-03-26: Musí to byť konzistentné.
				// Alt+F4 tiež „iba“ skrýva okno. Zavretie okna
				// v podstate ani nemôže nastať…
				// skry();

				// FIX: 2025-03-27: Tvrdenie vyššie je nezmysel. Keď som
				// zamedzil zatváraniu cez reakciu zavretie, tak jediné Ctrl+W
				// stále fungovalo (pretože iba skrývalo okno).
				zavrieť(); // TODO: Test on macOS.
				return;
			}

			// Podobné ako: poslednáUdalosťKlávesnice
			ÚdajeUdalostí.oknoUdalosti = Okno.this;
			ÚdajeUdalostí.poslednáUdalosťSkratky = e;
			Svet.aktuálnyIntervalKofeínu = Svet.intervalKofeínu;
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
					int početPočúvajúcich =
						GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.
							počúvajúciRozhranie.get(i);
						// Podobné ako: zadanieZnaku()
						počúvajúci.klávesováSkratka();
						počúvajúci.klavesovaSkratka();
					}
				}
		}
	}

	// Zoznam klávesových skratiek podľa akcií:
	private final TreeMap<String, KlávesováSkratka>
		klávesovéSkratky = new TreeMap<>();

	/**
	 * <p>Definuje novú klávesovú skratku s modifikátorom pre ponuky, ktorá
	 * bude previazaná so zadaným príkazom. Klávesové skratky sú spracúvané
	 * udalosťou {@link ObsluhaUdalostí#klávesováSkratka()
	 * ObsluhaUdalostí.klávesováSkratka()}, ktorá používa metódu
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
	 * @see #odoberKlávesovúSkratku(String)
	 * @see #skratkaPríkazu(String)
	 * @see #reťazecSkratkyPríkazu(String)
	 */
	public void pridajKlávesovúSkratku(String príkaz, int kódKlávesu)
	{
		pridajKlávesovúSkratku(príkaz, kódKlávesu,
			Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratku(String, int) pridajKlávesovúSkratku}.</p> */
	public void pridajKlavesovuSkratku(String príkaz, int kódKlávesu)
	{ pridajKlávesovúSkratku(príkaz, kódKlávesu); }

	/**
	 * <p>Definuje novú klávesovú skratku, ktorá bude previazaná so zadaným
	 * príkazom. Klávesové skratky sú spracúvané udalosťou {@link 
	 * ObsluhaUdalostí#klávesováSkratka() ObsluhaUdalostí.klávesováSkratka()},
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
	 * @see #odoberKlávesovúSkratku(String)
	 * @see #skratkaPríkazu(String)
	 * @see #reťazecSkratkyPríkazu(String)
	 */
	public void pridajKlávesovúSkratku(String príkaz,
		int kódKlávesu, int modifikátor)
	{
		if (klávesovéSkratky.containsKey(príkaz))
			odoberKlávesovúSkratku(príkaz);

		KeyStroke keyStroke = KeyStroke.getKeyStroke(kódKlávesu,
			modifikátor);
		KlávesováSkratka klávesováSkratka = new KlávesováSkratka(
			príkaz, keyStroke);

		klávesovéSkratky.put(príkaz, klávesováSkratka);

		hlavnýPanel.getInputMap(JPanel.
			// WHEN_IN_FOCUSED_WINDOW
			WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
			).
			put(keyStroke, príkaz);
		hlavnýPanel.getActionMap().put(príkaz, klávesováSkratka);
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajKlávesovúSkratku(String, int, int) pridajKlávesovúSkratku}.</p> */
	public void pridajKlavesovuSkratku(String príkaz,
		int kódKlávesu, int modifikátor)
	{ pridajKlávesovúSkratku(príkaz, kódKlávesu, modifikátor); }

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
	 * @see #skratkaPríkazu(String)
	 * @see #reťazecSkratkyPríkazu(String)
	 */
	public void odoberKlávesovúSkratku(String príkaz)
	{
		if (klávesovéSkratky.containsKey(príkaz))
		{
			KeyStroke keyStroke = (KeyStroke)
				klávesovéSkratky.get(príkaz).//keyStroke;
				getValue(AbstractAction.ACCELERATOR_KEY);

			hlavnýPanel.getInputMap(JPanel.
				// WHEN_IN_FOCUSED_WINDOW
				WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
				).
				remove(keyStroke);
			hlavnýPanel.getActionMap().remove(príkaz);

			klávesovéSkratky.remove(príkaz);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #odoberKlávesovúSkratku(String) odoberKlávesovúSkratku}.</p> */
	public void odoberKlavesovuSkratku(String príkaz)
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
	 * @see #odoberKlávesovúSkratku(String)
	 * @see #reťazecSkratkyPríkazu(String)
	 */
	public KeyStroke skratkaPríkazu(String príkaz)
	{
		if (klávesovéSkratky.containsKey(príkaz))
			return (KeyStroke)klávesovéSkratky.get(príkaz).
				getValue(AbstractAction.ACCELERATOR_KEY);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #skratkaPríkazu(String) skratkaPríkazu}.</p> */
	public KeyStroke skratkaPrikazu(String príkaz)
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
	 * @see #odoberKlávesovúSkratku(String)
	 * @see #skratkaPríkazu(String)
	 */
	public String reťazecSkratkyPríkazu(String príkaz)
	{
		KeyStroke keyStroke = skratkaPríkazu(príkaz);
		if (null == keyStroke) return null;
		if (0 == keyStroke.getModifiers())
			return KeyEvent.getKeyText(keyStroke.getKeyCode());
		return KeyEvent.getKeyModifiersText(keyStroke.getModifiers()) +
			"+" + KeyEvent.getKeyText(keyStroke.getKeyCode());
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecSkratkyPríkazu(String) reťazecSkratkyPríkazu}.</p> */
	public String retazecSkratkyPrikazu(String príkaz)
	{ return reťazecSkratkyPríkazu(príkaz); }


	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }
}
