
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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

// ------------------------- //
//  *** Trieda Tlačidlo ***  //
// ------------------------- //

/**
 * <p>Trieda {@code currTlačidlo} dovoľuje vytvárať v aplikácii používajúcej
 * programovací rámec robota tlačidlá, ktoré sú zobrazované nad plátnom (avšak
 * nie sú jeho súčasťou). Každé nové tlačidlo je automaticky umiestnené
 * na súradnice stredu plátna s predvolenými rozmermi 108 × 32 bodov
 * (šírka × výška).</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Tlačidlá používajú súradnicový priestor
 * rámca a používajú pri tom vlastný zabudovaný mechanizmus, preto na
 * manipuláciu s polohou a rozmermi tlačidiel používajte metódy
 * definované v tejto triede, ako: {@link Tlačidlo#polohaX(double)
 * polohaX}, {@link Tlačidlo#polohaY(double) polohaY}, {@link 
 * Tlačidlo#šírka(int) šírka}, {@link Tlačidlo#výška(int) výška}…, nie
 * zdedené metódy {@link JButton#setLocation(int, int) setLocation},
 * {@link JButton#setSize(int, int) setSize}…</p>
 * 
 * <p>Na obsluhu tlačidiel je určená metóda {@link ObsluhaUdalostí
 * ObsluhaUdalostí}{@code .}{@link ObsluhaUdalostí#voľbaTlačidla()
 * voľbaTlačidla}. Jej využitie ukazuje nasledujúci príklad:</p>
 * 
 * <pre CLASS="example">
	{@code comm// Vytvoríme tlačidlá, ktorými budeme ovládať robot}
	{@code comm// (Poznámka: Predpokladáme, že tento kód je umiestnený}
	{@code comm// v konštruktore hlavného robota…)}

	{@code kwdfinal} {@code currTlačidlo} tlačidloDopredu = {@code kwdnew} {@link #Tlačidlo(String) Tlačidlo}({@code srg"Dopredu"});
	{@code kwdfinal} {@code currTlačidlo} tlačidloDozadu = {@code kwdnew} {@link #Tlačidlo(String) Tlačidlo}({@code srg"Dozadu"});
	{@code kwdfinal} {@code currTlačidlo} tlačidloVpravo = {@code kwdnew} {@link #Tlačidlo(String) Tlačidlo}({@code srg"Vpravo"});
	{@code kwdfinal} {@code currTlačidlo} tlačidloVľavo = {@code kwdnew} {@link #Tlačidlo(String) Tlačidlo}({@code srg"Vľavo"});

	{@code comm// Rozmiestnime tlačidlá v ľavom hornom rohu tesne pod seba}

	tlačidloDopredu.{@link #prilepVľavo() prilepVľavo}();
	tlačidloDopredu.{@link #prilepHore() prilepHore}();

	tlačidloDozadu.{@link #prilepVľavo() prilepVľavo}();
	tlačidloDozadu.{@link #prilepHore() prilepHore}();
	tlačidloDozadu.{@link #poloha(Poloha) poloha}(tlačidloDopredu);
	tlačidloDozadu.{@link #skoč(double, double) skoč}({@code num0}, &#45;tlačidloDopredu.{@link #výška() výška}());

	tlačidloVpravo.{@link #prilepVľavo() prilepVľavo}();
	tlačidloVpravo.{@link #prilepHore() prilepHore}();
	tlačidloVpravo.{@link #poloha(Poloha) poloha}(tlačidloDozadu);
	tlačidloVpravo.{@link #skoč(double, double) skoč}({@code num0}, &#45;tlačidloDozadu.{@link #výška() výška}());

	tlačidloVľavo.{@link #prilepVľavo() prilepVľavo}();
	tlačidloVľavo.{@link #prilepHore() prilepHore}();
	tlačidloVľavo.{@link #poloha(Poloha) poloha}(tlačidloVpravo);
	tlačidloVľavo.{@link #skoč(double, double) skoč}({@code num0}, &#45;tlačidloVpravo.{@link #výška() výška}());

	{@code comm// Definujeme obsluhu udalostí}
	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}()
		{
			{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidlo() tlačidlo}() == tlačidloDopredu)
				{@link GRobot#dopredu(double) dopredu}({@code num10});
			{@code kwdelse} {@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidlo() tlačidlo}() == tlačidloDozadu)
				{@link GRobot#dozadu(double) dozadu}({@code num10});
			{@code kwdelse} {@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidlo() tlačidlo}() == tlačidloVpravo)
				{@link GRobot#vpravo(double) vpravo}({@code num10});
			{@code kwdelse} {@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidlo() tlačidlo}() == tlačidloVľavo)
				{@link GRobot#vľavo(double) vľavo}({@code num10});
		}
	};
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>robotOvladanyTlacidlami.png<alt/>Ukážka činnosti príkladu
 * s robotom ovládaným tlačidlami.</image>Ukážka nakreslenia
 * jednoduchej lomenej<br />čiary s pomocou robota ovládaného
 * tlačidlami<br /><small>(plátno ukážky je zmenšené)</small>.</p>
 */
@SuppressWarnings("serial")
public class Tlačidlo extends JButton implements Poloha
{
	// Parametre polohy a veľkosti tlačidla
	/*packagePrivate*/ int x, y, šírka, výška;

	// Parametre prilepenia k jednotlivým okrajom (kombinácia bitov)
	private byte prilepenie = 0;

	// Zálohy predvolených stavov “Opaque”, “ContentAreaFilled”
	// a “BorderPainted” (upravené metódami „vytvor“):
	private boolean zálohaStavuOpaque;
	private boolean zálohaStavuContentAreaFilled;
	private boolean zálohaStavuBorderPainted;

	// Previaže tlačidlo s obsluhou udalostí
	private final static ActionListener voľbaTlačidla =
		new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			ÚdajeUdalostí.poslednéTlačidlo = (Tlačidlo)e.getSource();

			if (null != ObsluhaUdalostí.počúvadlo)
			{
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.voľbaTlačidla();
					ObsluhaUdalostí.počúvadlo.volbaTlacidla();
				}
			}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.voľbaTlačidla();
						počúvajúci.volbaTlacidla();
					}
				}

			Skript skript;

			if (null != ÚdajeUdalostí.poslednéTlačidlo && (null !=
				(skript = ÚdajeUdalostí.poslednéTlačidlo.skript())))
			{
				int kódSkriptu = skript.vykonaj();
				if (0 != kódSkriptu)
					Svet.formulujChybuSkriptu(
						kódSkriptu, "Tlačidlo…");
			}
		}
	};

	// Skript položky…
	private Skript skript = null;
	private String[] riadkySkriptu = null;

	// TODO: umožniť definovať vlastný „(bez)menný“ priestor premenných
	//	s možnosťami prístupu k vlastnostiam tlačidla – podobne pre ostatné
	//	ovládacie prvky podporujúce skriptovanie

	// Určuje predvolené hodnoty vlastností tlačidla
	private void vytvor()
	{
		šírka = 108; výška = 32;
		x = (Plátno.šírkaPlátna - šírka) / 2;
		y = (Plátno.výškaPlátna - výška) / 2;
		Svet.hlavnýPanel.add(this, 0);
		Svet.hlavnýPanel.doLayout();
		addActionListener(voľbaTlačidla);
		addKeyListener(Svet.udalostiOkna);
		zálohaStavuOpaque = isOpaque();
		zálohaStavuContentAreaFilled = isContentAreaFilled();
		zálohaStavuBorderPainted = isBorderPainted();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void vytvor(int vlastnáŠírka, int vlastnáVýška)
	{
		šírka = vlastnáŠírka; výška = vlastnáVýška;
		x = (Plátno.šírkaPlátna - šírka) / 2;
		y = (Plátno.výškaPlátna - výška) / 2;
		Svet.hlavnýPanel.add(this, 0);
		Svet.hlavnýPanel.doLayout();
		addActionListener(voľbaTlačidla);
		addKeyListener(Svet.udalostiOkna);
		zálohaStavuOpaque = isOpaque();
		zálohaStavuContentAreaFilled = isContentAreaFilled();
		zálohaStavuBorderPainted = isBorderPainted();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	// Umiestnenie tlačidla na základe súkromných parametrov – použité
	// v metóde rozmiestňovania komponentov: Svet.hlavnýPanel.doLayout()
	/*packagePrivate*/ void umiestni(int x1, int y1, int šírka1, int výška1)
	{
		int x0 = x1 + x;
		int y0 = y1 + y;

		if (1 == (prilepenie & 3))
			x0 += ((šírka - šírka1) / 2) - (šírka1 % 2);
		else if (2 == (prilepenie & 3))
			x0 -= ((šírka - šírka1) / 2);

		if (4 == (prilepenie & 12))
			y0 += ((výška - výška1) / 2) - (výška1 % 2);
		else if (8 == (prilepenie & 12))
			y0 -= ((výška - výška1) / 2);

		setBounds(x0, y0, šírka, výška);
	}


	/**
	 * <p>Konštruktor textového tlačidla.
	 * Vytvorí tlačidlo umiestnené v strede plátna s predvolenými
	 * rozmermi 108 × 32 bodov a zadaným textom tlačidla.</p>
	 * 
	 * @param text text tlačidla
	 * 
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, Image)
	 * @see #Tlačidlo(Image, String)
	 * @see #text(String)
	 */
	public Tlačidlo(String text)
	{
		super(text);
		vytvor();
	}

	/**
	 * <p>Konštruktor obrázkového tlačidla. Vytvorí tlačidlo umiestnené
	 * v strede plátna s rozmermi a tvarom zadaného obrázka.</p>
	 * 
	 * @param obrázok obrázok tlačidla
	 * 
	 * @see #Tlačidlo(String)
	 * @see #Tlačidlo(Image, Image)
	 * @see #Tlačidlo(Image, String)
	 * @see #obrázok(Image)
	 */
	public Tlačidlo(Image obrázok)
	{
		super(Obrázok.obrázokNaIkonu(obrázok));
		vytvor(obrázok.getWidth(null), obrázok.getHeight(null));
		zrušDekor();
	}

	/**
	 * <p>Konštruktor obrázkového tlačidla s odlišným obrázkom v stlačenom
	 * stave. Vytvorí tlačidlo umiestnené v strede plátna s rozmermi
	 * obrázka určeného pre nestlačený stav (prvý parameter) a vzhľadom
	 * určeným zadanými obrázkami pre normálny a stlačený stav.</p>
	 * 
	 * @param obrázok obrázok tlačidla v normálnom (nestlačenom) stave
	 * @param obrázokStlačeného obrázok stlačeného tlačidla
	 * 
	 * @see #Tlačidlo(String)
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, String)
	 * @see #obrázok(Image)
	 */
	public Tlačidlo(Image obrázok, Image obrázokStlačeného)
	{
		super(Obrázok.obrázokNaIkonu(obrázok));
		if (null != obrázokStlačeného)
			setPressedIcon(Obrázok.obrázokNaIkonu(obrázokStlačeného));
		vytvor(obrázok.getWidth(null), obrázok.getHeight(null));
		zrušDekor();
	}

	/**
	 * <p>Konštruktor textového tlačidla s obrázkom.
	 * Vytvorí tlačidlo umiestnené v strede plátna s predvolenými
	 * rozmermi 108 × 32 bodov a so zadaným obrázkom vedľa textu
	 * tlačidla.</p>
	 * 
	 * @param obrázok obrázok tlačidla
	 * @param text text tlačidla
	 * 
	 * @see #Tlačidlo(String)
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, Image)
	 * @see #obrázok(Image)
	 * @see #text(String)
	 */
	public Tlačidlo(Image obrázok, String text)
	{
		super(text, Obrázok.obrázokNaIkonu(obrázok));
		vytvor();
	}


	/**
	 * <p>Overí, či bola toto tlačidlo naposledy aktivované (zvolené).
	 * Metóda je použiteľná v reakcii {@link 
	 * ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívne() aktívne} – {@link #aktivované()
	 * aktivované} a {@link #označené() označené} – {@link #zvolené()
	 * zvolené}!</b>
	 * Metóda {@link #aktivované() aktivované} a jej alias {@link 
	 * #zvolené() zvolené} zisťujú, či bolo stanovené tlačidlo naposledy
	 * aktivované (zvolené). Metóda {@link #aktívne() aktívne} overuje,
	 * či je stanovené tlačidlo použiteľné (stlačiteľné) a metóda {@link 
	 * #označené() označené} zisťuje, či bolo tlačidlo takzvane
	 * {@linkplain #označ() označené} (čo môže mať subjektívny
	 * význam).</p>
	 * 
	 * @return {@code valtrue} ak bolo naposledy aktivované toto
	 *     tlačidlo, v opačnom prípade {@code valfalse}
	 */
	public boolean aktivované() { return this == ÚdajeUdalostí.poslednéTlačidlo; }

	/** <p><a class="alias"></a> Alias pre {@link #aktivované() aktivované}. (Venujte pozornosť upozorneniu pri metóde {@link #aktivované() aktivované}!)</p> */
	public boolean aktivovane() { return aktivované(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktivované() aktivované}. (Venujte pozornosť upozorneniu pri metóde {@link #aktivované() aktivované}!)</p> */
	public boolean zvolené() { return aktivované(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktivované() aktivované}. (Venujte pozornosť upozorneniu pri metóde {@link #aktivované() aktivované}!)</p> */
	public boolean zvolene() { return aktivované(); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálny kód mnemonickej skratky
	 * nastavenej pre toto tlačidlo.</p>
	 * 
	 * @see #mnemonickáSkratka(int)
	 */
	public int mnemonickáSkratka() { return getMnemonic(); }

	/** <p><a class="alias"></a> Alias pre {@link #mnemonickáSkratka(int) mnemonickáSkratka}.</p> */
	public int mnemonickaSkratka() { return getMnemonic(); }

	/**
	 * <p><a class="setter"></a> Dovoľuje nastaviť tlačidlu mnemonickú
	 * skratku. Význam
	 * mnemonickej skratky je rovnaký ako pri položkách ponuky. (Pozri
	 * napríklad metódu {@link Svet#pridajPoložkuPonuky(String, int)
	 * Svet.pridajPoložkuPonuky(text, mnemonickáSkratka)}.)</p>
	 * 
	 * @param mnemonickáSkratka kód mnemonickej skratky (príklad: {@code 
	 *     Kláves.VK_A})
	 * 
	 * @see #mnemonickáSkratka()
	 */
	public void mnemonickáSkratka(int mnemonickáSkratka)
	{ setMnemonic(mnemonickáSkratka); }

	/** <p><a class="alias"></a> Alias pre {@link #mnemonickáSkratka(int) mnemonickáSkratka}.</p> */
	public void mnemonickaSkratka(int mnemonickáSkratka)
	{ setMnemonic(mnemonickáSkratka); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu x-ovú súradnicu polohy
	 * tlačidla.</p>
	 * 
	 * @return aktuálna x-ová súradnica polohy tlačidla
	 * 
	 * @see #polohaX(double)
	 */
	public double polohaX() { return x - ((Plátno.šírkaPlátna - šírka) / 2); }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu y-ovú súradnicu polohy
	 * tlačidla.</p>
	 * 
	 * @return aktuálna y-ová súradnica polohy tlačidla
	 * 
	 * @see #polohaY(double)
	 */
	public double polohaY() { return -y + ((Plátno.výškaPlátna - výška) / 2); }

	/**
	 * <p><a class="setter"></a> Presunie tlačidlo na zadanú súradnicu
	 * v smere x.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor tlačidla.</p>
	 * 
	 * @param novéX nová x-ová súradnica polohy tlačidla
	 * 
	 * @see #polohaX()
	 * @see #poloha(double, double)
	 */
	public void polohaX(double novéX)
	{
		x = ((Plátno.šírkaPlátna - šírka) / 2) + (int)novéX;
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p><a class="setter"></a> Presunie tlačidlo na zadanú súradnicu
	 * v smere y.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor tlačidla.</p>
	 * 
	 * @param novéY nová y-ová súradnica polohy tlačidla
	 * 
	 * @see #polohaY()
	 * @see #poloha(double, double)
	 * @see #poloha(Poloha)
	 */
	public void polohaY(double novéY)
	{
		y = ((Plátno.výškaPlátna - výška) / 2) - (int)novéY;
		Svet.hlavnýPanel.doLayout();
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
	 * <p>Presunie tlačidlo na zadané súradnice {@code x}, {@code y}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor tlačidla.</p>
	 * 
	 * @param x nová x-ová súradnica polohy tlačidla
	 * @param y nová y-ová súradnica polohy tlačidla
	 * 
	 * @see #polohaX(double)
	 * @see #polohaY(double)
	 * @see #poloha(Poloha)
	 */
	public void poloha(double x, double y)
	{
		this.x = ((Plátno.šírkaPlátna - šírka) / 2) + (int)x;
		this.y = ((Plátno.výškaPlátna - výška) / 2) - (int)y;
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Presunie tlačidlo na súradnice zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor tlačidla.</p>
	 * 
	 * @param objekt objekt, na ktorého súradnice bude tlačidlo presunuté
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
	 * <p>Vráti aktuálnu polohu tlačidla.</p>
	 * 
	 * @return aktuálna poloha tlačidla
	 * 
	 * @see #polohaX()
	 * @see #polohaY()
	 */
	public Bod poloha()
	{
		double x = this.x - ((Plátno.šírkaPlátna - šírka) / 2.0);
		double y = -this.y + ((Plátno.výškaPlátna - výška) / 2.0);
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

	/* * <p><a class="alias"></a> Alias pre {@link #poloha(Tlačidlo) poloha}.</p> * /
	public void skočNa(Tlačidlo tlačidlo) { poloha(tlačidlo); }

	/* * <p><a class="alias"></a> Alias pre {@link #poloha(Tlačidlo) poloha}.</p> * /
	public void skocNa(Tlačidlo tlačidlo) { poloha(tlačidlo); }
	*/

	/**
	 * <p>Presunie tlačidlo o zadaný počet bodov v horizontálnom
	 * a vertikálnom smere. Upozorňujeme, že zadané hodnoty sú
	 * automaticky zaokrúhlené na celé čísla, čiže ani viacnásobné
	 * posunutie tlačidla o hodnotu z otvoreného intervalu (−1; 1)
	 * nebude mať za následok posunutie tlačidla…</p>
	 * 
	 * @param Δx počet bodov v smere x
	 * @param Δy počet bodov v smere y
	 */
	public void skoč(double Δx, double Δy)
	{
		this.x += Δx;
		this.y -= Δy;
		Svet.hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #skoč(double, double) skoč}.</p> */
	public void skoc(double Δx, double Δy) { skoč(Δx, Δy); }


	/**
	 * <p>Overí, či sa poloha tohto tlačidla dokonale zhoduje so zadanými
	 * súradnicami. Ak je zistená zhoda, tak metóda vráti hodnotu {@code 
	 * valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param x x-ová súradnica, s ktorou má byť porovnaná poloha tohto
	 *     tlačidla
	 * @param y y-ová súradnica, s ktorou má byť porovnaná poloha tohto
	 *     tlačidla
	 * @return {@code valtrue} ak sa poloha tohto tlačidla zhoduje
	 *     so zadanými súradnicami, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(double x, double y)
	{
		double ox = this.x - ((Plátno.šírkaPlátna - šírka) / 2.0);
		double oy = -this.y + ((Plátno.výškaPlátna - výška) / 2.0);
		return ox == x && oy == y;
	}

	/**
	 * <p>Overí, či sa poloha tohto tlačidla a poloha zadaného objektu
	 * dokonale zhodujú. Ak je zistená zhoda, tak metóda vráti hodnotu
	 * {@code valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param poloha objekt, ktorého poloha má byť porovnaná s polohou tohto
	 *     tlačidla
	 * @return {@code valtrue} ak sa poloha tohto tlačidla zhoduje s polohou
	 *     zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(Poloha poloha)
	{
		double ox = this.x - ((Plátno.šírkaPlátna - šírka) / 2.0);
		double oy = -this.y + ((Plátno.výškaPlátna - výška) / 2.0);
		return poloha.polohaX() == ox && poloha.polohaY() == oy;
	}


	/**
	 * <p>Prilepí tlačidlo k ľavému okraju. Táto akcia zruší prípadné
	 * predchádzajúce prilepenie k pravému okraju. Každé prilepenie upravuje
	 * súradnicový systém tlačidla presunutím čo najbližšie
	 * k prilepovanému okraju. To znamená, že keď napríklad po prilepení
	 * k ľavému okraju posunieme tlačidlo na súradnice [10, 0], posunieme
	 * ho v skutočnosti na pozíciu desať bodov od ľavého okraja.</p>
	 * 
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepVľavo()
	{
		prilepenie &= 12;
		prilepenie |= 1;
		Svet.hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #prilepVľavo() prilepVľavo}.</p> */
	public void prilepVlavo() { prilepVľavo(); }

	/**
	 * <p>Prilepí tlačidlo k pravému okraju. Táto akcia zruší prípadné
	 * predchádzajúce prilepenie k ľavému okraju. Každé prilepenie upravuje
	 * súradnicový systém tlačidla presunutím čo najbližšie
	 * k prilepovanému okraju. To znamená, že keď napríklad po prilepení
	 * k pravému okraju posunieme tlačidlo na súradnice [-10, 0],
	 * tak ho v skutočnosti posunieme na pozíciu desať bodov od pravého
	 * okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepVpravo()
	{
		prilepenie &= 12;
		prilepenie |= 2;
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí tlačidlo k hornému okraju. Táto akcia zruší prípadné
	 * predchádzajúce prilepenie k dolnému okraju. Každé prilepenie upravuje
	 * súradnicový systém tlačidla presunutím čo najbližšie
	 * k prilepovanému okraju. To znamená, že keď napríklad po prilepení
	 * k hornému okraju posunieme tlačidlo na súradnice [0, -10],
	 * tak ho v skutočnosti posunieme na pozíciu desať bodov od horného
	 * okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepDole()
	 * @see #odlep()
	 */
	public void prilepHore()
	{
		prilepenie &= 3;
		prilepenie |= 4;
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí tlačidlo k dolnému okraju. Táto akcia zruší prípadné
	 * predchádzajúce prilepenie k hornému okraju. Každé prilepenie upravuje
	 * súradnicový systém tlačidla presunutím čo najbližšie
	 * k prilepovanému okraju. To znamená, že keď napríklad po prilepení
	 * k dolnému okraju posunieme tlačidlo na súradnice [0, 10],
	 * tak ho v skutočnosti posunieme na pozíciu desať bodov od dolného
	 * okraja.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #odlep()
	 */
	public void prilepDole()
	{
		prilepenie &= 3;
		prilepenie |= 8;
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Odlepí tlačidlo od všetkých okrajov.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 */
	public void odlep()
	{
		prilepenie = 0;
		Svet.hlavnýPanel.doLayout();
	}


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu šírku tlačidla.</p>
	 * 
	 * @return aktuálna šírka tlačidla
	 * 
	 * @see #šírka(int)
	 */
	public int šírka() { return šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
	public int sirka() { return šírka; }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu výšku tlačidla.</p>
	 * 
	 * @return aktuálna výška tlačidla
	 * 
	 * @see #výška(int)
	 */
	public int výška() { return výška; }

	/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
	public int vyska() { return výška; }

	/**
	 * <p><a class="setter"></a> Zmení šírku tlačidla.</p>
	 * 
	 * @param nováŠírka nová šírka tlačidla
	 * 
	 * @see #šírka()
	 */
	public void šírka(int nováŠírka)
	{
		double ox = polohaX();
		šírka = nováŠírka;
		polohaX(ox);
	}

	/** <p><a class="alias"></a> Alias pre {@link #šírka(int) šírka}.</p> */
	public void sirka(int nováŠírka) { šírka(nováŠírka); }

	/**
	 * <p><a class="setter"></a> Zmení výšku tlačidla.</p>
	 * 
	 * @param nováVýška nová výška tlačidla
	 * 
	 * @see #výška()
	 */
	public void výška(int nováVýška)
	{
		double oy = polohaY();
		výška = nováVýška;
		polohaY(oy);
	}

	/** <p><a class="alias"></a> Alias pre {@link #výška(int) výška}.</p> */
	public void vyska(int nováVýška) { výška(nováVýška); }


	// Keďže sme ani pri robote neboli zvyknutí na túto verziu metódy,
	// nedávame ju ani do tlačidla:
	// public void aktivuj(boolean áno) { setEnabled(b); }

	/**
	 * <p>Overí, či je tlačidlo aktívne. Aktívne tlačidlo znamená
	 * použiteľné tlačidlo. S deaktivovaným tlačidlom nemôže používateľ
	 * manipulovať.</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívne() aktívne} – {@link #aktivované()
	 * aktivované} a {@link #označené() označené} – {@link #zvolené()
	 * zvolené}!</b>
	 * Metóda {@link #aktivované() aktivované} a jej alias {@link 
	 * #zvolené() zvolené} zisťujú, či bolo stanovené tlačidlo naposledy
	 * aktivované (zvolené). Metóda {@link #aktívne() aktívne} overuje,
	 * či je stanovené tlačidlo použiteľné (stlačiteľné) a metóda {@link 
	 * #označené() označené} zisťuje, či bolo tlačidlo takzvane
	 * {@linkplain #označ() označené} (čo môže mať subjektívny
	 * význam).</p>
	 * 
	 * @return {@code valtrue} – je aktívne;
	 *     {@code valfalse} – nie je aktívne
	 * 
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public boolean aktívne() { return isEnabled(); }

	/** <p><a class="alias"></a> Alias pre {@link #aktívne() aktívne}.</p> */
	public boolean aktivne() { return isEnabled(); }

	/**
	 * <p>Aktivuje tlačidlo. Predvolene je tlačidlo aktívne. Ak ho
	 * {@linkplain #deaktivuj() deaktivujeme} (pozri nižšie), tak po
	 * vykonaní tohto príkazu ({@code curraktivuj}) tlačidla, bude opäť
	 * použiteľné a bude reagovať na klikanie myšou aj voľbu klávesnicou.</p>
	 * 
	 * @see #aktívne()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void aktivuj() { setEnabled(true); }

	/**
	 * <p>Deaktivuje tlačidlo. Tlačidlo prestane byť použiteľné, prestane
	 * reagovať na myš a klávesnicu.</p>
	 * 
	 * @see #aktívne()
	 * @see #aktivuj()
	 * @see #deaktivuj()
	 */
	public void deaktivuj() { setEnabled(false); }

	/**
	 * <p>Overí, či je tlačidlo označené. Označené tlačidlo zmení svoj
	 * vzhľad (spôsob zmeny je závislý od operačného systému) a ak má
	 * tlačidlo {@linkplain #obrázok(Image) definovaný obrázok} môžeme
	 * určiť, aby sa v tlačidle s označeným stavom zobrazoval {@linkplain 
	 * #obrázokOznačeného(Image) iný obrázok}. (Interpretáciu tohto stavu
	 * ponechávame na programátora…)</p>
	 * 
	 * <p class="attention"><b>Upozornenie: Pozor na podobnosť medzi
	 * názvami metód {@link #aktívne() aktívne} – {@link #aktivované()
	 * aktivované} a {@link #označené() označené} – {@link #zvolené()
	 * zvolené}!</b>
	 * Metóda {@link #aktivované() aktivované} a jej alias {@link 
	 * #zvolené() zvolené} zisťujú, či bolo stanovené tlačidlo naposledy
	 * aktivované (zvolené). Metóda {@link #aktívne() aktívne} overuje,
	 * či je stanovené tlačidlo použiteľné (stlačiteľné) a metóda {@link 
	 * #označené() označené} zisťuje, či bolo tlačidlo takzvane
	 * {@linkplain #označ() označené} (čo môže mať subjektívny
	 * význam).</p>
	 * 
	 * @return {@code valtrue} – je označené;
	 *     {@code valfalse} – nie je označené
	 * 
	 * @see #označ()
	 * @see #odznač()
	 * @see #zrušOznačenie()
	 */
	public boolean označené() { return isSelected(); }

	/** <p><a class="alias"></a> Alias pre {@link #označené() označené}.</p> */
	public boolean oznacene() { return isSelected(); }

	/**
	 * <p>Označí tlačidlo. (Pre viac informácií pozri opis metódy {@link 
	 * #označené() označené}.)</p>
	 * 
	 * @see #aktívne()
	 * @see #odznač()
	 * @see #zrušOznačenie()
	 */
	public void označ() { setSelected(true); }

	/** <p><a class="alias"></a> Alias pre {@link #označ() označ}.</p> */
	public void oznac() { setSelected(true); }

	/**
	 * <p>Zruší označenie tlačidla. (Pre viac informácií pozri opis metódy
	 * {@link #označené() označené}.)</p>
	 * 
	 * @see #aktívne()
	 * @see #označ()
	 * @see #zrušOznačenie()
	 */
	public void odznač() { setSelected(false); }

	/** <p><a class="alias"></a> Alias pre {@link #odznač() odznač}.</p> */
	public void odznac() { setSelected(false); }

	/**
	 * <p>Zruší označenie tlačidla. (Pre viac informácií pozri opis metódy
	 * {@link #označené() označené}.)</p>
	 * 
	 * @see #aktívne()
	 * @see #označ()
	 * @see #odznač()
	 */
	public void zrušOznačenie() { setSelected(false); }

	/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenie() zrušOznačenie}.</p> */
	public void zrusOznacenie() { setSelected(false); }


	/**
	 * <p>Zistí, či je tlačidlo viditeľné (zobrazené) alebo nie. Po
	 * vytvorení je tlačidlo predvolene viditeľné, môžeme ho skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda {@link #zobrazené()
	 * zobrazené}.</p>
	 * 
	 * @see #zobrazené()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean viditeľné() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #viditeľné() viditeľné}.</p> */
	public boolean viditelne() { return isVisible(); }

	/**
	 * <p>Zistí, či je tlačidlo viditeľné (zobrazené) alebo nie. Po
	 * vytvorení je tlačidlo predvolene viditeľné, môžeme ho skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda {@link #viditeľné()
	 * viditeľné}.</p>
	 * 
	 * @see #viditeľné()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean zobrazené() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #zobrazené() zobrazené}.</p> */
	public boolean zobrazene() { return isVisible(); }

	/**
	 * <p>Zobrazí tlačidlo. (Pre viac informácií pozri opis metódy
	 * {@link #zobrazené() zobrazené}.)</p>
	 * 
	 * @see #viditeľné()
	 * @see #zobrazené()
	 * @see #skry()
	 */
	public void zobraz() { setVisible(true); }

	/**
	 * <p>Skryje tlačidlo. (Pre viac informácií pozri opis metódy
	 * {@link #zobrazené() zobrazené}.)</p>
	 * 
	 * @see #viditeľné()
	 * @see #zobrazené()
	 * @see #zobraz()
	 */
	public void skry() { setVisible(false); }

	/**
	 * <p>Prekrytie originálnej metódy. Slúži predovšetkým na zabezpečenie
	 * postúpenia vstupu klávesnice (fokusu) hlavnému panelu (pri skrytí
	 * tlačidla).</p>
	 * 
	 * @param visible {@code valtrue} alebo {@code valfalse} podľa toho, či
	 *     má byť tlačidlo zobrazené alebo skryté
	 * @see JButton#setVisible(boolean)
	 */
	@Override public void setVisible(boolean visible)
	{
		if (!visible)
			Svet.hlavnýPanel.requestFocusInWindow();
		super.setVisible(visible);
	}


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu farbu textu tlačidla.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @return aktuálna farba textu tlačidla (objekt typu {@link Farba
	 *     Farba})
	 */
	public Farba farbaTextu()
	{
		Color farba = getForeground();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastav farbu textu tlačidla. Nastaví farbu
	 * a priehľadnosť textu tlačidla podľa zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param nováFarba objekt typu {@link Color Color} (alebo
	 *     odvodeného napr. {@link Farba Farba}) s novou farbou textu
	 *     tlačidla; jestvuje paleta predvolených farieb (pozri napr.:
	 *     {@link Farebnosť#biela biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna
	 *     čierna}…)
	 */
	public void farbaTextu(Color nováFarba) { setForeground(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť textu tlačidla podľa zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param objekt objekt určujúci novú farbu textu tlačidla
	 */
	public void farbaTextu(Farebnosť objekt) { farbaTextu(objekt.farba()); }

	/**
	 * <p>Nastaví farbu textu tlačidla podľa zadaných farebných zložiek.</p>
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
	 * <p>Nastaví farbu a (ne)priehľadnosť textu tlačidla podľa zadaných
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
	 * <p>Nastaví zdedenú farbu textu tlačidla. Farba textu tlačidla bude
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
	 * <p><a class="getter"></a> Číta farbu pozadia tlačidla.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @return aktuálna farba pozadia tlačidla (objekt typu {@link Farba
	 *     Farba})
	 */
	public Farba farbaPozadia()
	{
		Color farba = getBackground();
		if (farba instanceof Farba) return (Farba)farba;
		return new Farba(farba);
	}

	/**
	 * <p><a class="setter"></a> Nastaví farbu a priehľadnosť pozadia
	 * tlačidla podľa zadanej farebnej inštancie.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 * 
	 * @param nováFarba objekt určujúci novú farbu pozadia;
	 *     jestvuje paleta predvolených farieb (pozri: {@link Farebnosť#biela
	 *     biela}, {@link Farebnosť#červená červená}, {@link Farebnosť#čierna čierna}…)
	 */
	public void farbaPozadia(Color nováFarba) { setBackground(nováFarba); }

	/**
	 * <p>Nastaví farbu a priehľadnosť pozadia tlačidla podľa zadaného objektu.</p>
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
	 * <p>Nastaví farbu pozadia tlačidla podľa zadaných farebných zložiek.</p>
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
		setBackground(farba);
		return farba;
	}

	/**
	 * <p>Nastaví farbu a (ne)priehľadnosť pozadia tlačidla podľa zadaných
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
	 * <p>Nastaví zdedenú farbu pozadia tlačidla.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Nie je garantované, že táto
	 * vlastnosť bude rešpektovaná definíciou vzhľadu používateľského
	 * rozhrania (L&F). Každá definícia vzhľadu komponentov sa môže
	 * slobodne rozhodnúť o tom, či bude túto vlastnosť rešpektovať.</p>
	 */
	// TODO – overiť, či určite funguje
	public void zdedenáFarbaPozadia() { farbaPozadia((Color)null); }

	/** <p><a class="alias"></a> Alias pre {@link #zdedenáFarbaPozadia() zdedenáFarbaPozadia}.</p> */
	public void zdedenaFarbaPozadia() { farbaPozadia((Color)null); }


	/**
	 * <p><a class="getter"></a> Číta aktuálny typ písma textu tlačidla.</p>
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
	 * <p><a class="setter"></a> Nastaví nový typ písma textu tlačidla.</p>
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
	 * <p>Nastaví nový typ písma textu tlačidla. (Nová inštancia triedy
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
	 * <p><a class="getter"></a> Prečíta aktuálny text tlačidla.</p>
	 * 
	 * @return aktuálny text tlačidla
	 * 
	 * @see #text(String)
	 */
	public String text() { return getText(); }

	/**
	 * <p><a class="setter"></a> Nastaví nový text tlačidla.</p>
	 * 
	 * @param text nový text tlačidla
	 * 
	 * @see #Tlačidlo(String)
	 * @see #Tlačidlo(Image, String)
	 * @see #text()
	 */
	public void text(String text) { setText(text); }


	/**
	 * <p>Nastaví alebo odstráni obrázok tlačidla. Toto je základný obrázok
	 * tlačidla. Bez neho nemá význam priraďovať tlačidlu ostatné druhy
	 * obrázkov…</p>
	 * 
	 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
	 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
	 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
	 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
	 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
	 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
	 * prijímajú názov súboru ako parameter.)</p>
	 * 
	 * @param súbor názov súboru s obrázkom alebo
	 *     {@code (String)}{@code valnull} ak chceme odstrániť obrázok
	 *     tlačidla
	 * 
	 * @see #obrázok(Image)
	 * @see #obrázokStlačeného(String)
	 * @see #obrázokDeaktivovaného(String)
	 * @see #obrázokOznačeného(String)
	 * @see #obrázokDeaktivovanéhoOznačeného(String)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void obrázok(String súbor)
	{
		if (null == súbor) setIcon(null);
		else setIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázok(String) obrázok}.</p> */
	public void obrazok(String súbor) { obrázok(súbor); }

	/**
	 * <p>Nastaví alebo odstráni obrázok tlačidla. Toto je základný obrázok
	 * tlačidla. Bez neho nemá význam priraďovať tlačidlu ostatné druhy
	 * obrázkov…</p>
	 * 
	 * @param obrázok nový obrázok tlačidla alebo
	 *     {@code (Image)}{@code valnull}
	 *     ak chceme odstrániť obrázok tlačidla
	 * 
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, Image)
	 * @see #Tlačidlo(Image, String)
	 * @see #obrázok(String)
	 * @see #obrázokStlačeného(Image)
	 * @see #obrázokDeaktivovaného(Image)
	 * @see #obrázokOznačeného(Image)
	 * @see #obrázokDeaktivovanéhoOznačeného(Image)
	 */
	public void obrázok(Image obrázok)
	{
		if (null == obrázok) setIcon(null);
		else setIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázok(Image) obrázok}.</p> */
	public void obrazok(Image obrázok) { obrázok(obrázok); }

	/**
	 * <p>Vráti základný obrázok tlačidla alebo {@code valnull} ak tlačidlo
	 * nemá priradený obrázok, ktorý bol vytvorený z inštancie typu
	 * {@link Obrázok Obrázok}.</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #obrázokStlačeného()
	 * @see #obrázokDeaktivovaného()
	 * @see #obrázokOznačeného()
	 * @see #obrázokDeaktivovanéhoOznačeného()
	 */
	public Obrázok obrázok()
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

	/** <p><a class="alias"></a> Alias pre {@link #obrázok() obrázok}.</p> */
	public Obrazok obrazok()
	{
		Obrázok obrázok = obrázok();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}


	/**
	 * <p>Nastaví alebo odstráni obrázok stlačeného tlačidla. Ak tlačidlo
	 * nemá definovaný {@linkplain #obrázok(String) základný
	 * obrázok}, tak nemá nastavovanie obrázka pre stlačený stav význam.
	 * Síce toto nastavenie môžete bez negatívnych následkov vykonať aj
	 * bez toho, ale prejaví sa až po nastavení základného obrázka.</p>
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
	 * @param súbor názov súboru s obrázkom stlačeného tlačidla alebo
	 *     {@code (String)}{@code valnull} ak chceme odstrániť obrázok
	 *     stlačeného tlačidla
	 * 
	 * @see #obrázokStlačeného(Image)
	 * @see #obrázok(String)
	 * @see #obrázokDeaktivovaného(String)
	 * @see #obrázokOznačeného(String)
	 * @see #obrázokDeaktivovanéhoOznačeného(String)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void obrázokStlačeného(String súbor)
	{
		if (null == súbor) setPressedIcon(null);
		else setPressedIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokStlačeného(String) obrázokStlačeného}.</p> */
	public void obrazokStlaceneho(String súbor) { obrázokStlačeného(súbor); }

	/**
	 * <p>Nastaví alebo odstráni obrázok stlačeného tlačidla. Ak tlačidlo
	 * nemá definovaný {@linkplain #obrázok(Image) základný
	 * obrázok}, tak nemá nastavovanie obrázka pre stlačený stav význam.
	 * Síce toto nastavenie môžete bez negatívnych následkov vykonať aj
	 * bez toho, ale prejaví sa až po nastavení základného obrázka.</p>
	 * 
	 * @param obrázok nový obrázok stlačeného tlačidla alebo
	 *     {@code (Image)}{@code valnull} ak chceme odstrániť obrázok
	 *     stlačeného tlačidla
	 * 
	 * @see #obrázokStlačeného(String)
	 * @see #obrázok(Image)
	 * @see #obrázokDeaktivovaného(Image)
	 * @see #obrázokOznačeného(Image)
	 * @see #obrázokDeaktivovanéhoOznačeného(Image)
	 */
	public void obrázokStlačeného(Image obrázok)
	{
		if (null == obrázok) setPressedIcon(null);
		else setPressedIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokStlačeného(Image) obrázokStlačeného}.</p> */
	public void obrazokStlaceneho(Image obrázok) { obrázokStlačeného(obrázok); }

	/**
	 * <p>Vráti obrázok stlačeného tlačidla alebo {@code valnull} ak
	 * tlačidlo nemá definovaný obrázok pre stlačený stav, ktorý bol
	 * vytvorený z inštancie typu {@link Obrázok Obrázok}.</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #obrázok()
	 * @see #obrázokDeaktivovaného()
	 * @see #obrázokOznačeného()
	 * @see #obrázokDeaktivovanéhoOznačeného()
	 */
	public Obrázok obrázokStlačeného()
	{
		Icon icon = getPressedIcon();
		if (null != icon && icon instanceof ImageIcon)
		{
			Image image = ((ImageIcon)icon).getImage();
			if (null != image && image instanceof Obrázok)
				return (Obrázok)image;
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokStlačeného() obrázokStlačeného}.</p> */
	public Obrazok obrazokStlaceneho()
	{
		Obrázok obrázok = obrázokStlačeného();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}


	/**
	 * <p>Nastaví alebo odstráni obrázok {@linkplain #deaktivuj()
	 * deaktivovaného} tlačidla. Ak tlačidlo nemá definovaný {@linkplain 
	 * #obrázok(String) základný obrázok}, tak nemá nastavovanie
	 * obrázka deaktivovaného tlačidla význam. Síce toto nastavenie
	 * môžete bez negatívnych následkov vykonať aj bez toho, ale prejaví
	 * sa až po nastavení základného obrázka.</p>
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
	 * @param súbor názov súboru s obrázkom deaktivovaného tlačidla alebo
	 *     {@code (String)}{@code valnull} ak chceme odstrániť obrázok
	 *     deaktivovaného tlačidla
	 * 
	 * @see #obrázokDeaktivovaného(Image)
	 * @see #obrázok(String)
	 * @see #obrázokStlačeného(String)
	 * @see #obrázokOznačeného(String)
	 * @see #obrázokDeaktivovanéhoOznačeného(String)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void obrázokDeaktivovaného(String súbor)
	{
		if (null == súbor) setDisabledIcon(null);
		else setDisabledIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovaného(String) obrázokDeaktivovaného}.</p> */
	public void obrazokDeaktivovaneho(String súbor) { obrázokDeaktivovaného(súbor); }

	/**
	 * <p>Nastaví alebo odstráni obrázok {@linkplain #deaktivuj()
	 * deaktivovaného} tlačidla. Ak tlačidlo nemá definovaný {@linkplain 
	 * #obrázok(Image) základný obrázok}, tak nemá nastavovanie
	 * obrázka deaktivovaného tlačidla význam. Síce toto nastavenie
	 * môžete bez negatívnych následkov vykonať aj bez toho, ale prejaví
	 * sa až po nastavení základného obrázka.</p>
	 * 
	 * @param obrázok nový obrázok deaktivovaného tlačidla alebo
	 *     {@code (Image)}{@code valnull} ak chceme odstrániť obrázok
	 *     deaktivovaného tlačidla
	 * 
	 * @see #obrázokDeaktivovaného(String)
	 * @see #obrázok(Image)
	 * @see #obrázokStlačeného(Image)
	 * @see #obrázokOznačeného(Image)
	 * @see #obrázokDeaktivovanéhoOznačeného(Image)
	 */
	public void obrázokDeaktivovaného(Image obrázok)
	{
		if (null == obrázok) setDisabledIcon(null);
		else setDisabledIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovaného(Image) obrázokDeaktivovaného}.</p> */
	public void obrazokDeaktivovaneho(Image obrázok) { obrázokDeaktivovaného(obrázok); }

	/**
	 * <p>Vráti obrázok {@linkplain #deaktivuj() deaktivovaného} tlačidla
	 * alebo {@code valnull} ak tlačidlo nemá definovaný obrázok pre
	 * deaktivovaný stav, ktorý bol vytvorený z inštancie typu
	 * {@link Obrázok Obrázok}.</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #obrázok()
	 * @see #obrázokStlačeného()
	 * @see #obrázokOznačeného()
	 * @see #obrázokDeaktivovanéhoOznačeného()
	 */
	public Obrázok obrázokDeaktivovaného()
	{
		Icon icon = getDisabledIcon();
		if (null != icon && icon instanceof ImageIcon)
		{
			Image image = ((ImageIcon)icon).getImage();
			if (null != image && image instanceof Obrázok)
				return (Obrázok)image;
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovaného() obrázokDeaktivovaného}.</p> */
	public Obrazok obrazokDeaktivovaneho()
	{
		Obrázok obrázok = obrázokDeaktivovaného();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}


	/**
	 * <p>Nastaví alebo odstráni obrázok {@linkplain #označené() označeného
	 * tlačidla}. Ak tlačidlo nemá definovaný {@linkplain #obrázok(String)
	 * základný obrázok}, tak nemá nastavovanie obrázka označeného tlačidla
	 * význam. Síce toto nastavenie môžete bez negatívnych následkov
	 * vykonať aj bez toho, ale prejaví sa až po nastavení základného
	 * obrázka. Príklad rozdielu obrázkov normálneho (základného)
	 * a označeného tlačidla je v opise metódy
	 * {@link #obrázokOznačeného(Image) obrázokOznačeného(obrázok)}.</p>
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
	 * @param súbor názov súboru s obrázkom označeného tlačidla alebo
	 *     {@code (String)}{@code valnull} ak chceme odstrániť obrázok
	 *     označeného tlačidla
	 * 
	 * @see #obrázokOznačeného(Image)
	 * @see #obrázok(String)
	 * @see #obrázokStlačeného(String)
	 * @see #obrázokDeaktivovaného(String)
	 * @see #obrázokDeaktivovanéhoOznačeného(String)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void obrázokOznačeného(String súbor)
	{
		if (null == súbor) setSelectedIcon(null);
		else setSelectedIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokOznačeného(String) obrázokOznačeného}.</p> */
	public void obrazokOznaceneho(String súbor) { obrázokOznačeného(súbor); }

	// // Kreslenie príkladov obrázkov označeného a neoznačeného tlačidla:
		// 
		// hrúbkaČiary(1.5);
		// 
		// kresliDoObrázka(neoznačené);
		// štvorec(4.5);
		// neoznačené.ulož("neoznaceneTlacidlo.png");
		// 
		// kresliDoObrázka(označené);
		// štvorec(4.5); farba(modrá);
		// skočNa(-3.5, 3.5); choďNa(3.5, -3.5);
		// skočNa(-3.5, -3.5); choďNa(3.5, 3.5);
		// označené.ulož("oznaceneTlacidlo.png");

	/**
	 * <p>Nastaví alebo odstráni obrázok {@linkplain #označené() označeného
	 * tlačidla}. Ak tlačidlo nemá definovaný {@linkplain #obrázok(Image)
	 * základný obrázok}, tak nemá nastavovanie obrázka označeného tlačidla
	 * význam. Síce toto nastavenie môžete bez negatívnych následkov
	 * vykonať aj bez toho, ale prejaví sa až po nastavení základného
	 * obrázka. Tu je príklad rozdielu obrázkov normálneho (základného)
	 * a označeného tlačidla (oba boli nakreslené a uložené s pomocou
	 * robota):</p>
	 * 
	 * <table class="centered"><tr>
	 * <td><image>neoznaceneTlacidlo.png<alt/></image>Príklad vyrobeného
	 * obrázka<br />pre normálne (neoznačené) tlačidlo.</td>
	 * <td><image>oznaceneTlacidlo.png<alt/></image>Príklad vyrobeného
	 * obrázka<br />pre označené tlačidlo.</td></tr></table>
	 * 
	 * @param obrázok nový obrázok označeného tlačidla alebo
	 *     {@code (Image)}{@code valnull} ak chceme odstrániť obrázok
	 *     označeného tlačidla
	 * 
	 * @see #obrázokOznačeného(String)
	 * @see #obrázok(Image)
	 * @see #obrázokStlačeného(Image)
	 * @see #obrázokDeaktivovaného(Image)
	 * @see #obrázokDeaktivovanéhoOznačeného(Image)
	 */
	public void obrázokOznačeného(Image obrázok)
	{
		if (null == obrázok) setSelectedIcon(null);
		else setSelectedIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokOznačeného(Image) obrázokOznačeného}.</p> */
	public void obrazokOznaceneho(Image obrázok) { obrázokOznačeného(obrázok); }

	/**
	 * <p>Vráti obrázok {@linkplain #označené() označeného tlačidla} alebo
	 * {@code valnull} ak tlačidlo nemá definovaný obrázok pre označený
	 * stav, ktorý bol vytvorený z inštancie typu {@link Obrázok Obrázok}.</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #obrázok()
	 * @see #obrázokStlačeného()
	 * @see #obrázokDeaktivovaného()
	 * @see #obrázokDeaktivovanéhoOznačeného()
	 */
	public Obrázok obrázokOznačeného()
	{
		Icon icon = getSelectedIcon();
		if (null != icon && icon instanceof ImageIcon)
		{
			Image image = ((ImageIcon)icon).getImage();
			if (null != image && image instanceof Obrázok)
				return (Obrázok)image;
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokOznačeného() obrázokOznačeného}.</p> */
	public Obrazok obrazokOznaceneho()
	{
		Obrázok obrázok = obrázokOznačeného();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}


	/**
	 * <p>Nastaví alebo odstráni obrázok tlačidla, ktoré je zároveň
	 * {@linkplain #deaktivuj() deaktivované} a {@linkplain #označené()
	 * označeného}. Ak tlačidlo nemá definovaný {@linkplain 
	 * #obrázok(String) základný obrázok}, tak nemá nastavovanie tohto typu
	 * obrázka význam. Síce toto nastavenie môžete bez negatívnych
	 * následkov vykonať aj bez toho, ale prejaví sa až po nastavení
	 * základného obrázka.</p>
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
	 * @param súbor názov súboru s obrázkom deaktivovaného a označeného
	 *     tlačidla alebo {@code (String)}{@code valnull} ak chceme
	 *     odstrániť obrázok deaktivovaného a označeného tlačidla
	 * 
	 * @see #obrázokDeaktivovanéhoOznačeného(Image)
	 * @see #obrázok(String)
	 * @see #obrázokStlačeného(String)
	 * @see #obrázokDeaktivovaného(String)
	 * @see #obrázokOznačeného(String)
	 * @see Svet#priečinokObrázkov(String)
	 * 
	 * @throws GRobotException ak súbor s obrázkom nebol nájdený
	 *     (identifikátor {@code imageNotFound})
	 */
	public void obrázokDeaktivovanéhoOznačeného(String súbor)
	{
		if (null == súbor) setDisabledSelectedIcon(null);
		else setDisabledSelectedIcon(Obrázok.súborNaIkonu(súbor));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(String) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrazokDeaktivovanehoOznaceneho(String súbor) { obrázokDeaktivovanéhoOznačeného(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(String) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrázokOznačenéhoDeaktivovaného(String súbor) { obrázokDeaktivovanéhoOznačeného(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(String) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrazokOznacenehoDeaktivovaneho(String súbor) { obrázokDeaktivovanéhoOznačeného(súbor); }

	/**
	 * <p>Nastaví alebo odstráni obrázok tlačidla, ktoré je zároveň
	 * {@linkplain #deaktivuj() deaktivované} a {@linkplain #označené()
	 * označeného}. Ak tlačidlo nemá definovaný {@linkplain 
	 * #obrázok(Image) základný obrázok}, tak nemá nastavovanie tohto typu
	 * obrázka význam. Síce toto nastavenie môžete bez negatívnych
	 * následkov vykonať aj bez toho, ale prejaví sa až po nastavení
	 * základného obrázka.</p>
	 * 
	 * @param obrázok nový obrázok deaktivovaného a označeného tlačidla
	 *     alebo {@code (Image)}{@code valnull} ak chceme odstrániť obrázok
	 *     deaktivovaného a označeného tlačidla
	 * 
	 * @see #obrázokDeaktivovanéhoOznačeného(String)
	 * @see #obrázok(Image)
	 * @see #obrázokStlačeného(Image)
	 * @see #obrázokDeaktivovaného(Image)
	 * @see #obrázokOznačeného(Image)
	 */
	public void obrázokDeaktivovanéhoOznačeného(Image obrázok)
	{
		if (null == obrázok) setDisabledSelectedIcon(null);
		else setDisabledSelectedIcon(Obrázok.obrázokNaIkonu(obrázok));
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(Image) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrazokDeaktivovanehoOznaceneho(Image obrázok) { obrázokDeaktivovanéhoOznačeného(obrázok); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(Image) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrázokOznačenéhoDeaktivovaného(Image obrázok) { obrázokDeaktivovanéhoOznačeného(obrázok); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného(Image) obrázokDeaktivovanéhoOznačeného}.</p> */
	public void obrazokOznacenehoDeaktivovaneho(Image obrázok) { obrázokDeaktivovanéhoOznačeného(obrázok); }

	/**
	 * <p>Vráti obrázok {@linkplain #deaktivuj() deaktivovaného} a {@linkplain 
	 * #označené() označeného} tlačidla alebo {@code valnull} ak tlačidlo
	 * nemá definovaný obrázok pre túto kombináciu stavov, ktorý by bol
	 * vytvorený z inštancie typu {@link Obrázok Obrázok}.</p>
	 * 
	 * @return objekt typu {@link Obrázok Obrázok} alebo {@code valnull}
	 * 
	 * @see #obrázok()
	 * @see #obrázokStlačeného()
	 * @see #obrázokDeaktivovaného()
	 * @see #obrázokOznačeného()
	 */
	public Obrázok obrázokDeaktivovanéhoOznačeného()
	{
		Icon icon = getSelectedIcon();
		if (null != icon && icon instanceof ImageIcon)
		{
			Image image = ((ImageIcon)icon).getImage();
			if (null != image && image instanceof Obrázok)
				return (Obrázok)image;
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného() obrázokDeaktivovanéhoOznačeného}.</p> */
	public Obrazok obrazokDeaktivovanehoOznaceneho()
	{
		Obrázok obrázok = obrázokDeaktivovanéhoOznačeného();
		if (null == obrázok) return null;
		if (obrázok instanceof Obrazok)
			return (Obrazok)obrázok;
		return new Obrazok(obrázok);
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného() obrázokDeaktivovanéhoOznačeného}.</p> */
	public Obrázok obrázokOznačenéhoDeaktivovaného() { return obrázokDeaktivovanéhoOznačeného(); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázokDeaktivovanéhoOznačeného() obrázokDeaktivovanéhoOznačeného}.</p> */
	public Obrazok obrazokOznacenehoDeaktivovaneho() { return obrazokDeaktivovanehoOznaceneho(); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálu veľkosť medzery medzi
	 * obrázkom a textom zobrazenými v tomto tlačidle.</p>
	 * 
	 * @return celé číslo vyjadrujúce počet pixelov medzi obrázkom
	 *     a textom tohto tlačidla
	 */
	public int medzeraMedziObrázkomATextom()
	{ return getIconTextGap(); }

	/** <p><a class="alias"></a> Alias pre {@link #medzeraMedziObrázkomATextom() medzeraMedziObrázkomATextom}.</p> */
	public int medzeraMedziObrazkomATextom()
	{ return getIconTextGap(); }

	/**
	 * <p><a class="setter"></a> Ak má toto tlačidlo nastavený obrázok aj
	 * text, tak táto vlastnosť určuje veľkosť medzery medzi nimi.
	 * Predvolená hodnota je štyri pixely.</p>
	 * 
	 * @param medzera celé číslo určujúce počet pixelov medzi obrázkom
	 *     a textom
	 */
	public void medzeraMedziObrázkomATextom(int medzera)
	{ setIconTextGap(medzera); }

	/** <p><a class="alias"></a> Alias pre {@link #medzeraMedziObrázkomATextom(int) medzeraMedziObrázkomATextom}.</p> */
	public void medzeraMedziObrazkomATextom(int medzera)
	{ setIconTextGap(medzera); }


	/**
	 * <p>Zruší dekor tlačidla určený aktuálne použitým L&F (Look and Feel,
	 * v našom prípade používame L&F zhodný s dizajnom ovládacích prvkov
	 * operačného systému). Tento spôsob zobrazenia (t. j. bez dekoru)
	 * je predvolene používaný rýdzo obrázkovými konštruktormi tlačidla
	 * (t. j. konštruktormi, ktoré prijímajú len argumenty {@linkplain 
	 * Image obrázkového typu}).</p>
	 * 
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, Image)
	 * @see #obnovDekor()
	 */
	public void zrušDekor()
	{
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	/** <p><a class="alias"></a> Alias pre {@link #zrušDekor() zrušDekor}.</p> */
	public void zrusDekor() { zrušDekor(); }

	/**
	 * <p>Obnoví dekor tlačidla pôvodne určený aktuálne použitým L&F (Look
	 * and Feel, v našom prípade používame L&F zhodný s dizajnom
	 * ovládacích prvkov operačného systému). Iba rýdzo obrázkové
	 * konštruktory ovplyvňujú predvolený dekor tlačidiel (pozri opis
	 * metódy {@link #zrušDekor() zrušDekor}).</p>
	 * 
	 * @see #Tlačidlo(Image)
	 * @see #Tlačidlo(Image, Image)
	 * @see #zrušDekor()
	 */
	public void obnovDekor()
	{
		setOpaque(zálohaStavuOpaque);
		setContentAreaFilled(zálohaStavuContentAreaFilled);
		setBorderPainted(zálohaStavuBorderPainted);
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
	 * <p>Vráti skript priradený k tomuto tlačidlu alebo {@code valnull},
	 * ak k tlačidlu nebol priradený žiadny skript.</p>
	 * 
	 * @return skript priradený k tomuto tlačidlu alebo {@code valnull}
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
	 * <p>Vráti riadky skriptu, ak bol skript k tomuto tlačidlu priradený
	 * v textovej forme. V opačnom prípade vráti {@code valnull}, pričom
	 * tlačidlo môže mať definovaný skript – pozri aj metódu {@link 
	 * #skript() skript}.</p>
	 * 
	 * @return skript priradený k tomuto tlačidlu alebo {@code valnull}
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
	 * <p>Priradí k tomuto tlačidlu skript, ktorý bude automaticky vykonaný
	 * po jeho zvolení. (Pozri aj metódu {@link 
	 * Svet#vykonajSkript(String[]) vykonajSkript}.) Ak chcete skript
	 * tlačidla vymazať, zadajte hodnotu {@code valnull}.</p>
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
	 * <p>Priradí k tomuto tlačidlu skript, ktorý bude automaticky vykonaný
	 * po jeho zvolení. (Pozri aj metódu {@link 
	 * Svet#vykonajSkript(String[]) vykonajSkript}.) Ak chcete skript
	 * tlačidla vymazať, zadajte hodnotu {@code valnull}.</p>
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
	 * <p>Priradí k tomuto tlačidlu skript, ktorý bude automaticky vykonaný
	 * po jeho zvolení. (Pozri aj metódu {@link Svet#vykonajSkript(String[])
	 * vykonajSkript} a triedu {@link Skript Skript}.) Ak chcete skript
	 * tlačidla vymazať, zadajte hodnotu {@code valnull}.</p>
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
}
