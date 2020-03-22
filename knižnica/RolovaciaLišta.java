
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

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import static knižnica.Konštanty.ZVISLÁ;
import static knižnica.Konštanty.VODOROVNÁ;

// ------------------------------- //
//  *** Trieda RolovaciaLišta ***  //
// ------------------------------- //

/**
 * <p>Trieda {@code currRolovaciaLišta} uzatvára a rozširuje funkciu triedy
 * Javy {@link JScrollBar JScrollBar}. Polohovanie a prilepovanie líšt
 * je rovnaké ako pri triede {@link Tlačidlo Tlačidlo}, ale komponenty
 * lišty majú navyše možnosť zapnutia automatickej zmeny veľkosti podľa
 * práve zobrazenej časti {@linkplain Plátno plátna}.</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Pri podrobnom testovaní funkčnosti
 * rolovacej lišty sa ukázalo, že predvolená implementácia triedy
 * {@link JScrollBar JScrollBar} nie je príliš dokonalá. Rolovacie
 * lišty fungujú dokonale v súčinnosti s rolovacím panelom
 * {@link javax.swing.JScrollPane JScrollPane}, ktorý v rámci programovacieho
 * rámca nie je použiteľný z dôvodu jej komplexnosti a samostatné použitie
 * líšt je menej funkčné. S najväčšou pravdepodobnosťou je príčinou
 * predvolene používaný model ohraničeného rozsahu, ktorý nerozlišuje
 * medzi rôznymi typmi udalostí. Implementácia nedokáže rozlíšiť, či
 * bola hodnota posunu lišty zmenená v dôsledku externej žiadosti alebo
 * v dôsledku vnútorného posunu prostredníctvom tlačidiel so šípkami,
 * v dôsledku čoho nie sú v rámci tejto programovacieho rámca tlačidlá šípok
 * na lište použiteľné. Rôzne pokusy o obídenie tohto problému situáciu len
 * zhoršovali. Dokedy nevznikne iná implementácia, treba sa zmieriť
 * s tým, že tlačidlá líšt jednoducho nefungujú. Zdrojové kódy
 * niektorých implementácií, ktoré by mohli poslúžiť ako vzory
 * do budúcna, sú dostupné tu: <a
 * href="http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b27/javax/swing/JScrollBar.java"
 * target="_blank">grepcode.com</a>, <a
 * href="http://developer.classpath.org/doc/javax/swing/JScrollBar-source.html"
 * target="_blank">developer.classpath.org</a>, <a
 * href="https://book2s.com/java/src/package/javax/swing/jscrollbar.html"
 * target="_blank">book2s.com</a>. Zálohy zdrojových kódov
 * z 28. 10. 2017 sú dostupné v <a href="resources/JScrollBar.7z"
 * target="_blank">tomto balíčku (7z)</a>.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>V tomto príklade je poloha grafického robota ovládaná lištami
 * a kolieskom myši. Obidva spôsoby ovládania sú prepojené
 * aktualizáciou polôh líšt pri každej zmene polohy robota kolieskom.
 * Všimnite si, že zmena hodnoty rolovacej lišty smie vyvolať akciu
 * (zmenu polohy robota) len v prípade, že je ťahaná. Inak by vznikala
 * neželaná spätná väzba a mechanizmus by nefungoval tak, ako má.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdimport} {@code kwdstatic} knižnica.{@link Svet Svet}.*;
	{@code kwdimport} {@code kwdstatic} knižnica.{@link ÚdajeUdalostí ÚdajeUdalostí}.*;

	{@code kwdpublic} {@code typeclass} TestRolovacíchLíšt {@code kwdextends} {@link GRobot GRobot}
	{
		{@code kwdprivate} {@link RolovaciaLišta RolovaciaLišta} zvislá, vodorovná;

		{@code comm// Konštruktor…}
		{@code kwdprivate} TestRolovacíchLíšt()
		{
			{@code comm// Vytvorenie obrázka pre tlačidlo}
			{@link Obrázok Obrázok} obrázok = {@code kwdnew} {@link Obrázok#Obrázok(int, int) Obrázok}({@code num20}, {@code num20});
			obrázok.{@link Obrázok#vyplň(Color) vyplň}({@link Farebnosť#šedá šedá}.{@link Farba#svetlejšia() svetlejšia}());

			{@code comm// Vytvorenie tlačidla, ktoré umiestnime do rohu medzi lišty}
			{@link Tlačidlo Tlačidlo} tlačidlo = {@code kwdnew} {@link Tlačidlo#Tlačidlo(Image) Tlačidlo}(obrázok);
			tlačidlo.{@link Tlačidlo#deaktivuj() deaktivuj}();
			tlačidlo.{@link Tlačidlo#obrázokDeaktivovaného(Image) obrázokDeaktivovaného}(obrázok);
			tlačidlo.{@link Tlačidlo#prilepVpravo() prilepVpravo}();
			tlačidlo.{@link Tlačidlo#prilepDole() prilepDole}();

			{@code comm// Vytvorenie zvislej rolovacej lišty}
			zvislá = {@code kwdnew} {@link RolovaciaLišta#RolovaciaLišta(int) RolovaciaLišta}({@link RolovaciaLišta RolovaciaLišta}.{@link Konštanty#ZVISLÁ ZVISLÁ});
			zvislá.{@link GRobot#skoč(double, double) skoč}({@code num0}, {@code num20});
			zvislá.{@link RolovaciaLišta#prilepVpravo() prilepVpravo}();
			zvislá.{@link RolovaciaLišta#roztiahniNaVýšku() roztiahniNaVýšku}();<!--
			zvislá.{@link RolovaciaLišta#zobraz() zobraz}();-->
			zvislá.{@link RolovaciaLišta#spodnáHranica(int) spodnáHranica}(-{@link Svet#výška() výška}() / {@code num2});
			zvislá.{@link RolovaciaLišta#hornáHranica(int) hornáHranica}({@link Svet#výška() výška}() / {@code num2});

			{@code comm// Vytvorenie vodorovnej rolovacej lišty}
			vodorovná = {@code kwdnew} {@link RolovaciaLišta#RolovaciaLišta(int) RolovaciaLišta}({@link RolovaciaLišta RolovaciaLišta}.{@link Konštanty#VODOROVNÁ VODOROVNÁ});
			vodorovná.{@link GRobot#skoč(double, double) skoč}(-{@code num20}, {@code num0});
			vodorovná.{@link RolovaciaLišta#prilepDole() prilepDole}();
			vodorovná.{@link RolovaciaLišta#roztiahniNaŠírku() roztiahniNaŠírku}();<!--
			vodorovná.{@link RolovaciaLišta#zobraz() zobraz}();-->
			vodorovná.{@link RolovaciaLišta#spodnáHranica(int) spodnáHranica}(-{@link Svet#šírka() šírka}() / {@code num2});
			vodorovná.{@link RolovaciaLišta#hornáHranica(int) hornáHranica}({@link Svet#šírka() šírka}() / {@code num2});
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#rolovanieKolieskomMyši() rolovanieKolieskomMyši}()
		{
			{@code comm// Posun robota podľa hodnôt rolovania}
			{@link GRobot#choď(double, double) choď}({@link ÚdajeUdalostí#rolovanieKolieskomMyšiX() rolovanieKolieskomMyšiX}(), {@link ÚdajeUdalostí#rolovanieKolieskomMyšiY() rolovanieKolieskomMyšiY}());

			{@code comm// Obmedzenie polohy robota tak, aby sa pohyboval len v rámci}
			{@code comm// hraníc posuvných líšt, ale by lištám neprekážalo, ani keby}
			{@code comm// robot vyšiel poza hranice, leby nedokázali zobraziť polohu}
			{@code comm// mimo týchto hraníc…}
			{@code kwdif} ({@link GRobot#polohaX() polohaX}() &lt; -{@link Svet#šírka() šírka}() / {@code num2}) {@link GRobot#polohaX(double) polohaX}(-{@link Svet#šírka() šírka}() / {@code num2});
			{@code kwdif} ({@link GRobot#polohaX() polohaX}() &gt; {@link Svet#šírka() šírka}() / {@code num2}) {@link GRobot#polohaX(double) polohaX}({@link Svet#šírka() šírka}() / {@code num2});
			{@code kwdif} ({@link GRobot#polohaY() polohaY}() &lt; -{@link Svet#výška() výška}() / {@code num2}) {@link GRobot#polohaY(double) polohaY}(-{@link Svet#výška() výška}() / {@code num2});
			{@code kwdif} ({@link GRobot#polohaY() polohaY}() &gt; {@link Svet#výška() výška}() / {@code num2}) {@link GRobot#polohaY(double) polohaY}({@link Svet#výška() výška}() / {@code num2});

			{@code comm// Aktualizácia posunov líšt}
			vodorovná.{@link RolovaciaLišta#posun(int) posun}(({@code typeint}){@link GRobot#polohaX() polohaX}());
			zvislá.{@link RolovaciaLišta#posun(int) posun}(-({@code typeint}){@link GRobot#polohaY() polohaY}());
		}

		{@code comm// Reakcia na udalosť posunu lišty}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#zmenaPosunuLišty() zmenaPosunuLišty}()
		{
			{@code comm// Treba vždy overiť, či ide o udalosť ťahania lištou, alebo}
			{@code comm// udalosť spôsobenú aktualizáciou hodnoty lišty (napríklad pri}
			{@code comm// vytvorení lišty alebo v reakcii rolovanieKolieskomMyši vyššie):}
			{@code kwdif} ({@link ÚdajeUdalostí#rolovaciaLišta() rolovaciaLišta}().{@link RolovaciaLišta#jeŤahaná() jeŤahaná}())
			{
				{@code comm// Robotom hýbeme len v prípade, že išlo o udalosť ťahania lištou…}
				{@code kwdif} (vodorovná == {@link ÚdajeUdalostí#rolovaciaLišta() rolovaciaLišta}())
				{
					{@link GRobot#choďNa(double, double) choďNa}({@link ÚdajeUdalostí#posunRolovacejLišty() posunRolovacejLišty}(), {@link GRobot#polohaY() polohaY}());
				}
				{@code kwdelse} {@code kwdif} (zvislá == {@link ÚdajeUdalostí#rolovaciaLišta() rolovaciaLišta}())
				{
					{@link GRobot#choďNa(double, double) choďNa}({@link GRobot#polohaX() polohaX}(), -{@link ÚdajeUdalostí#posunRolovacejLišty() posunRolovacejLišty}());
				}
			}
		}

		{@code comm// Hlavná metóda…}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestRolovacíchLíšt.cfg"});
			{@code kwdnew} TestRolovacíchLíšt();
		}
	}
	</pre>
 */
@SuppressWarnings("serial")
public class RolovaciaLišta extends JScrollBar implements Poloha
{
	// Pozri aj:
	// (https://docs.oracle.com/javase/8/docs/api/javax/swing/plaf/ScrollBarUI.html)
	// https://docs.oracle.com/javase/8/docs/api/javax/swing/JScrollBar.html
	// 
	// (https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html)
	// http://www.zentut.com/java-swing/jscrollbar/
	// http://www.java2s.com/Code/Java/Swing-JFC/AquickdemonstrationofJScrollBarbothverticalandhorizontal.htm
	// 
	// https://www.tutorialspoint.com/swing/swing_jscrollbar.htm
	// 
	// Počúvadlo:
	// https://www.java-tips.org/java-se-tips-100019/15-javax-swing/979-how-to-use-a-scrollbar-in-both-vertical-and-horizontal-direction.html
	// https://www.javatpoint.com/java-jscrollbar
	// 
	// Viac:
	// https://www.programcreek.com/java-api-examples/javax.swing.JScrollBar


	/**
	 * <p>Parameter polohy alebo veľkosti rolovacej lišty.</p>
	 * 
	 * <p><small>(Toto je opis spoločný pre štyri chránené
	 * atribúty.)</small></p>
	 */
	protected int x, y, šírka, výška;

	// Parametre prilepenia k jednotlivým okrajom a roztiahnutia
	// v dvoch smeroch (kombinácia bitov)
	private byte prilepenieRoztiahnutie = 0;


	// Príznaky slúžiace na identifikáciu toho, či má byť lišta zobrazená
	// pri štarte a či tento proces už bol vykonaný
	private boolean zobrazPriŠtarte = true, predŠtartom = true;


	// Previaže tlačidlo s obsluhou udalostí
	private final static AdjustmentListener zmenaPosunuLišty =
		new AdjustmentListener()
	{
		public void adjustmentValueChanged(AdjustmentEvent e)
		{
			ÚdajeUdalostí.poslednáRolovaciaLišta = (RolovaciaLišta)e.getSource();
			ÚdajeUdalostí.poslednýPosunRolovacejLišty = e.getValue();
			ÚdajeUdalostí.poslednáUdalosťPosunu = e;

			if (null != ObsluhaUdalostí.počúvadlo)
			{
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.zmenaPosunuLišty();
					ObsluhaUdalostí.počúvadlo.zmenaPosunuListy();
				}
			}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciRozhranie)
					{
						počúvajúci.zmenaPosunuLišty();
						počúvajúci.zmenaPosunuListy();
					}
				}
		}
	};


	// Určuje predvolené hodnoty vlastností rolovacej lišty
	private void vytvor()
	{
		int velkosť = UIManager.getInt("ScrollBar.width");
		if (0 >= velkosť) velkosť = 20;
		else if (8 >= velkosť) velkosť = 8;

		if (ZVISLÁ == getOrientation())
		{ šírka = velkosť; výška = 100 + 2 * velkosť; } else
		{ šírka = 100 + 2 * velkosť; výška = velkosť; }

		x = (Plátno.šírkaPlátna - šírka) / 2;
		y = (Plátno.výškaPlátna - výška) / 2;

		Svet.hlavnýPanel.add(this, 0);
		Svet.hlavnýPanel.doLayout();

		addAdjustmentListener(zmenaPosunuLišty);
		super.setVisible(false);
		SwingUtilities.invokeLater(() ->
		{
			predŠtartom = false;
			super.setVisible(zobrazPriŠtarte);
		});
	}


	// Súkromná metóda balíčka slúžiaca na automatické umiestnenie rolovacej
	// lišty na hlavnom paneli okna frameworku. Umiestňovanie sa deje základe
	// hodnôt chránených a súkromných atribútov a tiež parametrov, ktoré
	// sú vnútorne vypočítané a použité v metóde rozmiestňovania vnorených
	// komponentov súkromného komponentu hlavnýPanel a vykonávanej ako reakcia
	// na výzvu doLayout().
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

		setBounds(x2, y2, šírka2, výška2);
	}

	/**
	 * <p>Vytvorí zvislú rolovaciu lištu v strede plátna. Predvolený
	 * rozsah hodnôt je 0 až 100, počiatočná hodnota posunu je
	 * rovná 0 a predvolená výška zobrazovanej oblasti je 10
	 * jednotiek. Rozmery lišty sú predvolene nastavené podľa
	 * hodnoty získanej z manažéra vzhľadu. Napríklad, ak je
	 * predvolená hrúbka lišty rovná 17 bodov, tak táto lišta
	 * bude mať šírku 17 bodov a výšku 134 bodov (100 + 2 × hrúbka).</p>
	 */
	public RolovaciaLišta()
	{
		super();
		vytvor();
	}

	/**
	 * <p>Vytvorí v strede plátna rolovaciu lištu so zadanou orientáciou
	 * (pozri konštanty {@link Konštanty#ZVISLÁ ZVISLÁ},
	 * {@link Konštanty#VODOROVNÁ VODOROVNÁ} a ich klony). Predvolený rozsah
	 * hodnôt je 0 až 100, počiatočná hodnota posunu je rovná 0 a predvolená
	 * veľkosť zobrazovanej oblasti je 10 jednotiek. Rozmery lišty sú
	 * predvolene nastavené podľa hodnoty získanej z manažéra vzhľadu.
	 * Napríklad, ak je predvolená hrúbka lišty rovná 17 bodov, tak
	 * táto lišta bude mať hrúbku 17 bodov a dĺžku 134 bodov
	 * (100 + 2 × hrúbka), kde hrúbka a dĺžka znamenajú výšku
	 * alebo šírku podľa orientácie.</p>
	 * 
	 * @param orientácia orientácia lišty (môže byť len
	 *     {@linkplain Konštanty#ZVISLÁ zvislá} alebo
	 *     {@linkplain Konštanty#VODOROVNÁ vodorovná})
	 */
	public RolovaciaLišta(int orientácia)
	{
		super(orientácia);
		vytvor();
	}

	/**
	 * <p>Vytvorí v strede plátna rolovaciu lištu so zadanou orientáciou
	 * (pozri konštanty {@link Konštanty#ZVISLÁ ZVISLÁ},
	 * {@link Konštanty#VODOROVNÁ VODOROVNÁ} a ich klony), posunom, rozsahom
	 * hodnôt a veľkosťou zobrazovanej oblasti. Rozmery lišty sú predvolene
	 * nastavené podľa hodnoty získanej z manažéra vzhľadu. Napríklad, ak je
	 * predvolená hrúbka lišty rovná 17 bodov, tak táto lišta bude
	 * mať hrúbku 17 bodov a dĺžku 134 bodov (100 + 2 × hrúbka),
	 * kde hrúbka a dĺžka znamenajú výšku alebo šírku podľa orientácie.</p>
	 * 
	 * @param orientácia orientácia lišty (môže byť len
	 *     {@linkplain Konštanty#ZVISLÁ zvislá} alebo {@linkplain 
	 *     Konštanty#VODOROVNÁ vodorovná})
	 * @param posun hodnota aktuálneho posunu v rámci povoleného
	 *     rozsahu hodnôt (t. j. hodnôt parametrov
	 *     {@code spodnáHranica} až {@code hornáHranica})
	 * @param veľkosťOblasti objem jednotiek určujúci aktuálne
	 *     viditeľnú oblasť z celého rozsahu ku ktorému sa vzťahuje táto
	 *     lišta
	 * @param spodnáHranica najmenšia hodnota povolená pri používaní
	 *     tejto lišty
	 * @param hornáHranica najväčšia hodnota povolená pri používaní
	 *     tejto lišty
	 */
	public RolovaciaLišta(int orientácia, int posun,
		int veľkosťOblasti, int spodnáHranica, int hornáHranica)
	{
		super(orientácia, posun, veľkosťOblasti,
			spodnáHranica, hornáHranica);
		vytvor();
	}


	/**
	 * <p>Overí, či je lišta práve ťahaná, čo je dôležité rozlišovať pri
	 * {@linkplain ObsluhaUdalostí obsluhe udalostí}. Pozri príklad
	 * v {@linkplain RolovaciaLišta úvodnom opise tejto triedy}.</p>
	 * 
	 * @return {@code valtrue}, ak je lišta práve ťahaná, inak
	 *     {@code valfalse}
	 */
	public boolean jeŤahaná() { return getValueIsAdjusting(); }

	/** <p><a class="alias"></a> Alias pre {@link #jeŤahaná() jeŤahaná}.</p> */
	public boolean jeTahana() { return getValueIsAdjusting(); }


	/**
	 * <p>Vráti celočíselnú hodnotu konštanty určujúcej orientáciu tejto lišty.
	 * Pozri konštanty: {@link Konštanty#ZVISLÁ ZVISLÁ},
	 * {@link Konštanty#VODOROVNÁ VODOROVNÁ} (a ich klony).</p>
	 * 
	 * @return vráti aktuálnu orientáciu lišty ({@linkplain Konštanty#VODOROVNÁ
	 *     vodorovnú} alebo {@linkplain Konštanty#ZVISLÁ zvislú})
	 */
	public int orientácia() { return getOrientation(); }

	/** <p><a class="alias"></a> Alias pre {@link #orientácia() orientácia}.</p> */
	public int orientacia() { return getOrientation(); }

	/**
	 * <p>Overí aktuálnu hodnotu posunu rolovacej lišty.</p>
	 * 
	 * @return aktuálna hodnota posunu lišty
	 */
	public int posun() { return getValue(); }

	/**
	 * <p>Zistí veľkosť oblasti, ktorú má lišta nastavenú ako aktuálnu
	 * šírku alebo výšku „okna“. Ide o veľkosť tej časti zobrazenia,
	 * ktorá má byť naraz viditeľná.</p>
	 * 
	 * @return aktuálna veľkosť zobrazovanej oblasti (výška alebo šírka;
	 *     podľa orientácie lišty)
	 */
	public int veľkosťOblasti() { return getVisibleAmount(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosťOblasti() veľkosťOblasti}.</p> */
	public int velkostOblasti() { return getVisibleAmount(); }

	/**
	 * <p>Zistí spodnú hranicu rozsahu povolených hodnôt pre túto lištu.</p>
	 * 
	 * @return aktuálna hodnota najnižšej dovolenej hodnoty tejto
	 *     rolovacej lišty
	 */
	public int spodnáHranica() { return getMinimum(); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica() spodnáHranica}.</p> */
	public int spodnaHranica() { return getMinimum(); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica() spodnáHranica}.</p> */
	public int dolnáHranica() { return getMinimum(); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica() spodnáHranica}.</p> */
	public int dolnaHranica() { return getMinimum(); }

	/**
	 * <p>Zistí hornú hranicu rozsahu povolených hodnôt pre túto lištu.</p>
	 * 
	 * @return aktuálna hodnota najvyššej dovolenej hodnoty tejto
	 *     rolovacej lišty
	 */
	public int hornáHranica() { return getMaximum(); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica() hornáHranica}.</p> */
	public int hornaHranica() { return getMaximum(); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica() hornáHranica}.</p> */
	public int vrchnáHranica() { return getMaximum(); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica() hornáHranica}.</p> */
	public int vrchnaHranica() { return getMaximum(); }


	/**
	 * <p>Zmení orientáciu tejto lišty.</p>
	 * 
	 * @param orientácia orientácia, ktorá môže byť len
	 *     {@linkplain Konštanty#VODOROVNÁ vodorovná} alebo
	 *     {@linkplain Konštanty#ZVISLÁ zvislá}
	 */
	public void orientácia(int orientácia)
	{ setOrientation(orientácia); }

	/** <p><a class="alias"></a> Alias pre {@link #orientácia(int) orientácia}.</p> */
	public void orientacia(int orientácia)
	{ setOrientation(orientácia); }

	/**
	 * <p>Nastaví novú hodnotu posunu tejto lišty.</p>
	 * 
	 * @param posun nová hodnota posunu tejto lišty
	 */
	public void posun(int posun) { setValue(posun); }

	/**
	 * <p>Nastaví novú veľkosť viditeľnej oblasti vzťahujúcej sa k rozsahu
	 * tejto lišty. Veľkosť tejto oblasti má význam aktualizovať napríklad
	 * pri zmene veľkosti okna, v ktorom sa lišta nachádza.</p>
	 * 
	 * @param veľkosťOblasti nová veľkosť zobrazovanej oblasti (výška
	 *     alebo šírka; podľa orientácie lišty)
	 */
	public void veľkosťOblasti(int veľkosťOblasti)
	{ setVisibleAmount(veľkosťOblasti); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosťOblasti(int) veľkosťOblasti}.</p> */
	public void velkostOblasti(int veľkosťOblasti)
	{ setVisibleAmount(veľkosťOblasti); }

	/**
	 * <p>Aktualizuje spodnú hranicu rozsahu povolených hodnôt pre túto lištu.</p>
	 * 
	 * @param spodnáHranica nová hodnota najnižšej dovolenej hodnoty
	 *     tejto rolovacej lišty
	 */
	public void spodnáHranica(int spodnáHranica)
	{ setMinimum(spodnáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica(int) spodnáHranica}.</p> */
	public void spodnaHranica(int spodnáHranica)
	{ setMinimum(spodnáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica(int) spodnáHranica}.</p> */
	public void dolnáHranica(int spodnáHranica)
	{ setMinimum(spodnáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica(int) spodnáHranica}.</p> */
	public void dolnaHranica(int spodnáHranica)
	{ setMinimum(spodnáHranica); }

	/**
	 * <p>Aktualizuje spodnú hranicu rozsahu povolených hodnôt pre túto lištu.</p>
	 * 
	 * @param hornáHranica nová hodnota najvyššej dovolenej hodnoty
	 *     tejto rolovacej lišty
	 */
	public void hornáHranica(int hornáHranica)
	{ setMaximum(hornáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica(int) hornáHranica}.</p> */
	public void hornaHranica(int hornáHranica)
	{ setMaximum(hornáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica(int) hornáHranica}.</p> */
	public void vrchnáHranica(int hornáHranica)
	{ setMaximum(hornáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #hornáHranica(int) hornáHranica}.</p> */
	public void vrchnaHranica(int hornáHranica)
	{ setMaximum(hornáHranica); }


	/**
	 * <p>Zistí jednotkový prírastok pohybu tejto rolovacej lišty.</p>
	 * 
	 * @return jednotkový prírastok pohybu pri používaní tejto lišty
	 */
	public int jednotkovýPrírastok() { return getUnitIncrement(); }

	/** <p><a class="alias"></a> Alias pre {@link #jednotkovýPrírastok() jednotkovýPrírastok}.</p> */
	public int jednotkovyPrirastok() { return getUnitIncrement(); }

	/**
	 * <p>Zistí blokový prírastok pohybu tejto rolovacej lišty.</p>
	 * 
	 * @return blokový prírastok pohybu pri používaní tejto lišty
	 */
	public int blokovýPrírastok() { return getBlockIncrement(); }

	/** <p><a class="alias"></a> Alias pre {@link #blokovýPrírastok() blokovýPrírastok}.</p> */
	public int blokovyPrirastok() { return getBlockIncrement(); }


	/**
	 * <p>Určí jednotkový prírastok pohybu tejto rolovacej lišty.</p>
	 * 
	 * @param jednotkovýPrírastok jednotkový prírastok pohybu pri
	 *     používaní tejto lišty
	 */
	public void jednotkovýPrírastok(int jednotkovýPrírastok)
	{ setUnitIncrement(jednotkovýPrírastok); }

	/** <p><a class="alias"></a> Alias pre {@link #jednotkovýPrírastok(int) jednotkovýPrírastok}.</p> */
	public void jednotkovyPrirastok(int jednotkovýPrírastok)
	{ setUnitIncrement(jednotkovýPrírastok); }

	/**
	 * <p>Určí blokový prírastok pohybu tejto rolovacej lišty.</p>
	 * 
	 * @param blokovýPrírastok blokový prírastok pohybu pri používaní
	 *     tejto lišty
	 */
	public void blokovýPrírastok(int blokovýPrírastok)
	{ setBlockIncrement(blokovýPrírastok); }

	/** <p><a class="alias"></a> Alias pre {@link #blokovýPrírastok(int) blokovýPrírastok}.</p> */
	public void blokovyPrirastok(int blokovýPrírastok)
	{ setBlockIncrement(blokovýPrírastok); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu x-ovú súradnicu polohy
	 * rolovacej lišty.</p>
	 * 
	 * @return aktuálna x-ová súradnica polohy rolovacej lišty
	 * 
	 * @see #polohaX(double)
	 */
	public double polohaX() { return x - ((Plátno.šírkaPlátna - šírka) / 2); }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu y-ovú súradnicu polohy
	 * rolovacej lišty.</p>
	 * 
	 * @return aktuálna y-ová súradnica polohy rolovacej lišty
	 * 
	 * @see #polohaY(double)
	 */
	public double polohaY() { return -y + ((Plátno.výškaPlátna - výška) / 2); }

	/**
	 * <p><a class="setter"></a> Presunie rolovaciu lištu na zadanú
	 * súradnicu v smere osi x.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param novéX nová x-ová súradnica polohy rolovacej lišty
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
	 * <p><a class="setter"></a> Presunie rolovaciu lištu na zadanú
	 * súradnicu v smere osi y.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param novéY nová y-ová súradnica polohy rolovacej lišty
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
	 * <p>Presunie rolovaciu lištu na zadané súradnice {@code x}, {@code y}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param x nová x-ová súradnica polohy rolovacej lišty
	 * @param y nová y-ová súradnica polohy rolovacej lišty
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
	 * <p>Presunie rolovaciu lištu na súradnice zadaného objektu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> {@linkplain #prilepVľavo()
	 * Prilepovanie} upravuje súradnicový priestor poznámkového bloku.</p>
	 * 
	 * @param objekt objekt, na ktorého súradnice bude rolovaciu lištu
	 *     presunuté
	 * 
	 * @see #polohaX(double)
	 * @see #polohaY(double)
	 * @see #poloha(double, double)
	 */
	// public void poloha(GRobot robot)
	public void poloha(Poloha objekt)
	{
		poloha((int)objekt.polohaX(), (int)objekt.polohaY());
	}

	/**
	 * <p>Vráti aktuálnu polohu rolovacej lišty.</p>
	 * 
	 * @return aktuálna poloha rolovacej lišty
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


	/**
	 * <p>Presunie rolovaciu lištu o zadaný počet bodov v horizontálnom
	 * a vertikálnom smere. Upozorňujeme, že zadané hodnoty sú
	 * automaticky zaokrúhlené na celé čísla, čiže ani viacnásobné
	 * posunutie rolovacej lišty o hodnotu z otvoreného intervalu
	 * (−1; 1) nebude mať za následok posunutie rolovacej lišty…</p>
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
	 * <p>Prilepí rolovaciu lištu k ľavému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k pravému okraju. Každé
	 * prilepenie upravuje súradnicový systém rolovacej lišty
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená
	 * napríklad, že keď po prilepení k ľavému okraju posunieme
	 * rolovaciu lištu na súradnice [10, 0], posunieme ju v skutočnosti
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
		Svet.hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #prilepVľavo() prilepVľavo}.</p> */
	public void prilepVlavo() { prilepVľavo(); }

	/**
	 * <p>Prilepí rolovaciu lištu k pravému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k ľavému okraju. Každé
	 * prilepenie upravuje súradnicový systém rolovacej lišty
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená
	 * napríklad, že keď po prilepení k pravému okraju posunieme
	 * rolovaciu lištu na súradnice [-10, 0], posunieme ju v skutočnosti
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
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí rolovaciu lištu k hornému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k dolnému okraju. Každé
	 * prilepenie upravuje súradnicový systém rolovacej lišty
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená
	 * napríklad, že keď po prilepení k hornému okraju posunieme
	 * rolovaciu lištu na súradnice [0, -10], posunieme ju v skutočnosti
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
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Prilepí rolovaciu lištu k dolnému okraju. Táto akcia zruší
	 * prípadné predchádzajúce prilepenie k hornému okraju. Každé
	 * prilepenie upravuje súradnicový systém rolovacej lišty
	 * presunutím čo najbližšie k prilepovanému okraju. To znamená
	 * napríklad, že keď po prilepení k dolnému okraju posunieme
	 * rolovaciu lištu na súradnice [0, 10], posunieme ju v skutočnosti
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
		Svet.hlavnýPanel.doLayout();
	}

	/**
	 * <p>Odlepí rolovaciu lištu od všetkých okrajov.</p>
	 * 
	 * @see #prilepVľavo()
	 * @see #prilepVpravo()
	 * @see #prilepHore()
	 * @see #prilepDole()
	 */
	public void odlep()
	{
		prilepenieRoztiahnutie &= 48;
		Svet.hlavnýPanel.doLayout();
	}


	/**
	 * <p>Roztiahne rolovaciu lištu na celú výšku zobrazovanej plochy tak,
	 * aby bola celá viditeľná, čoho dôsledkom je aj to, že najväčšia
	 * možná výška lišty je rovná výške plátien sveta.</p>
	 * 
	 * @see #roztiahniNaŠírku()
	 * @see #nerozťahuj()
	 */
	public void roztiahniNaVýšku()
	{
		prilepenieRoztiahnutie |= 16;
		Svet.hlavnýPanel.doLayout();
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
	 * <p>Roztiahne rolovaciu lištu na celú šírku zobrazovanej plochy tak,
	 * aby bola celá viditeľná, čoho dôsledkom je aj to, že najväčšia
	 * možná šírka lišty je rovná šírke plátien sveta.</p>
	 * 
	 * @see #roztiahniNaVýšku()
	 * @see #nerozťahuj()
	 */
	public void roztiahniNaŠírku()
	{
		prilepenieRoztiahnutie |= 32;
		Svet.hlavnýPanel.doLayout();
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
	 * <p>Zruší roztiahnutie rolovacej lišty v oboch smeroch.</p>
	 * 
	 * @see #roztiahniNaVýšku()
	 * @see #roztiahniNaŠírku()
	 */
	public void nerozťahuj()
	{
		prilepenieRoztiahnutie &= 15;
		Svet.hlavnýPanel.doLayout();
	}

	/** <p><a class="alias"></a> Alias pre {@link #nerozťahuj() nerozťahuj}.</p> */
	public void neroztahuj() { nerozťahuj(); }


	/**
	 * <p><a class="getter"></a> Zistí aktuálnu šírku rolovacej lišty.</p>
	 * 
	 * @return aktuálna šírka rolovacej lišty
	 * 
	 * @see #šírka(int)
	 */
	public int šírka() { return šírka; }

	/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
	public int sirka() { return šírka; }

	/**
	 * <p><a class="getter"></a> Zistí aktuálnu výšku rolovacej lišty.</p>
	 * 
	 * @return aktuálna výška rolovacej lišty
	 * 
	 * @see #výška(int)
	 */
	public int výška() { return výška; }

	/** <p><a class="alias"></a> Alias pre {@link #výška() výška}.</p> */
	public int vyska() { return výška; }

	/**
	 * <p><a class="setter"></a> Zmení šírku rolovacej lišty.</p>
	 * 
	 * @param nováŠírka nová šírka rolovacej lišty
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
	 * <p><a class="setter"></a> Zmení výšku rolovacej lišty.</p>
	 * 
	 * @param nováVýška nová výška rolovacej lišty
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


	/**
	 * <p>Zistí, či je rolovacia lišta viditeľná (zobrazená) alebo nie.
	 * <!-- Po vytvorení je rolovacia lišta predvolene <b>skrytá</b>.
	 * To znamená, že je potrebné ju zobraziť. -->Lištu môžeme skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda
	 * {@link #zobrazená() zobrazená}.</p>
	 * 
	 * @return {@code valtrue} znamená, že lišta je zobrazená,
	 *     {@code valfalse} znamená opak
	 * 
	 * @see #zobrazená()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean viditeľná() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #viditeľná() viditeľná}.</p> */
	public boolean viditelna() { return isVisible(); }

	/**
	 * <p>Zistí, či je rolovacia lišta viditeľná (zobrazená) alebo nie.
	 * <!-- Po vytvorení je rolovacia lišta predvolene <b>skrytá</b>.
	 * To znamená, že je potrebné ju zobraziť. -->Lištu môžeme skrývať
	 * a zobrazovať metódami {@link #skry() skry} a {@link #zobraz()
	 * zobraz}. Alternatívou tejto metódy je metóda
	 * {@link #viditeľná() viditeľná}.</p>
	 * 
	 * @return {@code valtrue} znamená, že lišta je zobrazená,
	 *     {@code valfalse} znamená opak
	 * 
	 * @see #viditeľná()
	 * @see #zobraz()
	 * @see #skry()
	 */
	public boolean zobrazená() { return isVisible(); }

	/** <p><a class="alias"></a> Alias pre {@link #zobrazená() zobrazená}.</p> */
	public boolean zobrazena() { return isVisible(); }

	/**
	 * <p>Zobrazí rolovaciu lištu. (Viac informácií nájdete v opise metódy
	 * {@link #zobrazená() zobrazená}.)</p>
	 * 
	 * @see #viditeľná()
	 * @see #zobrazená()
	 * @see #skry()
	 */
	public void zobraz() { setVisible(true); }

	/**
	 * <p>Skryje rolovaciu lištu. (Viac informácií nájdete v opise metódy
	 * {@link #zobrazená() zobrazená}.)</p>
	 * 
	 * @see #viditeľná()
	 * @see #zobrazená()
	 * @see #zobraz()
	 */
	public void skry() { setVisible(false); }

	/**
	 * <p>Zobrazí alebo skryhe rolovaciu lištu podľa parametra {@code zobraz}.
	 * Táto metóda prekrýva originálnu metódu z dôvodu implementácie
	 * mechanizmu zabezpečujúceho správne zobrazenie lišty po inicializácii
	 * aplikácie.
	 * (Viac informácií nájdete v opise metódy {@link #zobrazená()
	 * zobrazená}.)</p>
	 * 
	 * @param zobraz {@code valtrue} znamená, že lišta má byť zobrazená,
	 *     {@code valfalse} znamená opak
	 */
	@Override public void setVisible(boolean zobraz)
	{
		if (predŠtartom) zobrazPriŠtarte = zobraz;
		else super.setVisible(zobraz);
	}

	/**
	 * <p>Zistí, či je rolovacia lišta viditeľná (zobrazená) alebo nie.
	 * Táto metóda prekrýva originálnu metódu z dôvodu implementácie
	 * mechanizmu zabezpečujúceho správne zobrazenie lišty po inicializácii
	 * aplikácie.
	 * (Viac informácií nájdete v opise metódy {@link #zobrazená()
	 * zobrazená}.)</p>
	 * 
	 * @return {@code valtrue} znamená, že lišta je zobrazená,
	 *     {@code valfalse} znamená opak
	 */
	@Override public boolean isVisible()
	{
		if (predŠtartom) return zobrazPriŠtarte;
		else return super.isVisible();
	}
}
