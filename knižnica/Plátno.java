
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.imageio.ImageIO;

import static knižnica.Farebnosť.*;
import static knižnica.Konštanty.VODOROVNÁ;
import static knižnica.Konštanty.ZVISLÁ;

// ----------------------- //
//  *** Trieda Plátno ***  //
// ----------------------- //

/*
	Organizácia metód

		rozmery
		okraje
		záloha a obnovenie
		kreslenie tvarov
		nastavenie farby bodu
		farba bodu
		farba textu
		farba pozadia textu
		predvolená farba označenia
		zisťovanie farieb označenia výpisov
		písmo (konzoly)
		označenie
		odsadenie sprava/zľava…
		priehľadnosť
		grafika a obrázok plátna
		vnútorná konzola (zväčša práca s textami konzoly)
		kreslenie obrázka
		uloženie obrázka
		vymazanie
		výplň
		maska
		rozmazanie a filter
		rolovanie a pretáčanie
		schránka
		interaktívny režim
		vlnenie

*/

/**
 * <p>Trieda implementujúca rôzne akcie s {@linkplain GRobot#podlaha
 * podlahou} a {@linkplain GRobot#strop stropom}.</p>
 * 
 * <p>Kresliace roboty fungujú vo svete s dvomi kresliacimi plátnami,
 * ktoré sa podľa ich polohy voči robotom nazývajú {@link GRobot#podlaha
 * podlaha} a {@link GRobot#strop strop}. Plátna sú priehľadné plochy
 * umiestnené jedna pod a druhá nad úrovňou robotov. Majú svoj rozmer
 * a dovoľujú vykonávanie aj takých úkonov, ktoré nesúvisia so žiadnym
 * konkrétnym grafickým robotom (napríklad {@linkplain #rozmaž()
 * rozmazanie}, {@linkplain #vyplň(Color) výplň}, {@linkplain 
 * #uložObrázok(String) ukladanie svojho obsahu do súboru} a podobne).
 * V prípade, že sú obe plátna prázdne, je plocha {@linkplain Svet
 * sveta} vyplnená {@linkplain Svet#farbaPozadia(Color) farbou
 * pozadia}. Každý robot smie {@linkplain GRobot#kresliNaPodlahu()
 * kresliť na podlahu} alebo {@linkplain GRobot#kresliNaStrop() strop}
 * podľa výberu. Kresby, ktoré vytvoria sú potom vidno nad podkladom,
 * pričom kresba na podlahe je zobrazená pod robotmi a kresba na strope
 * nad robotmi.</p>
 * 
 * <p>Kresba každého plátna je uložená mimo zobrazovaný priestor
 * {@linkplain Svet sveta}, preto všetko, čo sa nachádza na plátne
 * nakreslené, nemusí byť automaticky viditeľné. Pri pohybe robota však
 * dochádza k automatickému prekresľovaniu, ktoré je v prípade potreby
 * možné {@linkplain Svet#nekresli() vypnúť}. Môžeme tým významne zvýšiť
 * grafický výkon aplikácie, najmä ak manipulujeme viacerými robotmi
 * naraz alebo vykonávame naraz viacero kresliacich operácií, z ktorých
 * potrebujeme vidieť iba výsledok, nie jednotlivé čiastkové kroky.</p>
 * 
 * <p>Nasledujúci príklad naprogramuje grafický robot tak, aby
 * reagoval na kurzorové šípky pohybom a aby kláves medzerník prepínal
 * medzi kreslením na strop tyrkysovou farbou a kreslením na podlahu
 * hnedou farbou:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#hrúbkaPera(double) hrúbkaPera}({@code num5.5});
	{@link GRobot#farba(Color) farba}({@link Farebnosť#hnedá hnedá});

	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}()
		{
			{@code kwdswitch} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves() kláves}())
			{

				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#MEDZERNÍK MEDZERNÍK}:

					{@code kwdif} ({@link GRobot#kreslímNaPodlahu() kreslímNaPodlahu}())
					{
						{@link GRobot#kresliNaStrop() kresliNaStrop}();
						{@link GRobot#farba(Color) farba}({@link Farebnosť#tyrkysová tyrkysová});
					}
					{@code kwdelse}
					{
						{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();
						{@link GRobot#farba(Color) farba}({@link Farebnosť#hnedá hnedá});
					}

					{@code kwdbreak};

				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#HORE HORE}:   {@link GRobot#dopredu(double) dopredu}({@code num10}); {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}: {@link GRobot#vpravo(double) vpravo}({@code num10}); {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:  {@link GRobot#vľavo(double) vľavo}({@code num10}); {@code kwdbreak};
				{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DOLE DOLE}:   {@link GRobot#dozadu(double) dozadu}({@code num10}); {@code kwdbreak};
			}
		}
	};
	</pre>
 * 
 * <p><b>Výsledok:</b>
 * 
 * <p><image>kreslenieStropPodlaha.png<alt/>Čiary nakreslené na strop
 * a podlahu.</image>Čiary nakreslené kurzorovými šípkami počas
 * činnosti<br />programu tohto príkladu; všimnite si, že hnedá
 * čiara<br />nie je nikdy nakreslená nad tyrkysovou; je to preto,
 * že<br />je kreslená na podlahu a tyrkysová je kreslená na
 * strop<br /><small>(veľkosť plátna na obrázku je
 * zmenšená)</small>.</p>
 * 
 * <p>Okrem grafickej časti si každé plátno dokáže uchovávať informácie
 * o textoch, ktoré na neho boli vypísané prostredníctvom metód, ktorých
 * prvotným cieľom bolo nahradiť funkčnosť textového režimu známu aj pod
 * termínom konzola. Preto vznikla v plátne relatívne rozsiahla definícia
 * skrytých objektov, ktorú nazývame <em>vnútorná konzola.</em></p>
 * 
 * <p>Na prácu s jej obsahom slúži niekoľko skupín metód vymenovaných
 * a utriedených v <a href="kategorie-metod.html#Plátno">kategóriách
 * metód plátna.</a> Úplne najstaršími a základnými je dvojica metód
 * {@link #vypíš(Object[]) vypíš} a {@link #vypíšRiadok(Object[])
 * vypíšRiadok}, ktoré zastupujú funkčnosť procedúr {@code write}
 * a {@code writeln} z jazyka Pascal alebo metód {@code print}
 * a {@code println} statického atribútu (objektu prúdu) {@code out}
 * triedy {@code System} jazyka Java. Zdôrazňujeme, že v rámci tejto
 * programovacieho rámca ide o simuláciu konzoly, ktorá má svoje obmedzenia,
 * ale prináša aj určité rozšírenia, ktoré sa dajú výhodne využiť. Viaceré
 * z metód, no zďaleka nie všetky sú premietnuté aj do triedy {@link 
 * Svet Svet}, v rámci ktorej kopírujú správanie vnútornej konzoly
 * „horného“ plátna – {@linkplain GRobot#strop stropu}.</p>
 * 
 * <p>Aby sme boli úplní, treba dodať, že doplnkom simulácie výstupu
 * sú metódy slúžiace na čítanie údajov od používateľa. Tie sú
 * definované len v triede {@link Svet Svet}. Majú niekoľko variant
 * a sú vymenované v kategórii sveta <a
 * href="kategorie-metod.html#Svet-Vstupné-dialógy">Vstupné
 * dialógy.</a> (V podstate ide o simuláciu štandardného vstupu,
 * ktorý je napríklad v jazyku Pascal reprezentovaný procedúrami
 * {@code read} a {@code readln}.)</p>
 */
public class Plátno implements Priehľadnosť
{
	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }


	// Predvolený typ písma vnútornej konzoly
	/*packagePrivate*/ final static Písmo predvolenéPísmoKonzoly =
		new Písmo("Lucida Console", Písmo.PLAIN, 18.0);

	// Rozmery plátna (s definovanými predvolenými hodnotami)
	/*packagePrivate*/ static int šírkaPlátna = 800, výškaPlátna = 600;

	// Príznak použitia príkazov konzoly
	/*packagePrivate*/ static boolean konzolaPoužitá = false;

	// Predvolená farba písma konzoly (pozadie je transparentné)
	private final static Farba predvolenáFarbaKonzoly = tmavomodrá;

	// Plátno reprezentované obrázkom a grafikou
	/*packagePrivate*/ BufferedImage obrázokPlátna;
	/*packagePrivate*/ Graphics2D grafikaPlátna;

	// Vnútorné pole na umožnenie zálohovanie plátna (podlahy
	// alebo stropu) a operácií s ním
	private int[] originálPlátna;

	// Vnútorné pole na zálohovanie plátna (podlahy alebo stropu)
	private int[] zálohaPlátna;

	// Vnútorné pole na operácie s plátnom (podlahou alebo stropom)
	// private int[] operáciePlátna;

	// Priehľadnosť plátna
	private float priehľadnosť = 1.0f;

	// Interaktívny režim
	/*packagePrivate*/ boolean interaktívnyRežim = false;

	// Vnútorná konzola
	/*packagePrivate*/ final VnútornáKonzola vnútornáKonzola;


	// ------------------------------- //
	//  *** Rôzne súkromné triedy ***  //
	// ------------------------------- //

		// Triedy vnútornej konzoly

		private static class PrototypKonzoly {}

		private static class PríkazKonzoly extends PrototypKonzoly
		{
			public Farba farba = null;
			public Farba pozadie = null;
			public boolean zrušPozadie = false;
			public Point2D poloha = null;
			public Integer odsaďPrvý = null;
			public Integer odsaďZľava = null;
			public Integer odsaďSprava = null;
			public Integer odsaďTabulátorom = null;
			public boolean zamrazOdsadenie = false;
		}

		private static class Slovo
		{
			public String slovo = null;
			public Rectangle2D hranice = null; // sledovanie polohy slova
		}

		private static class AktívneSlovo extends Slovo
		{
			public String identifikátor = null;
			// public Rectangle2D hranice = null; // stalo sa reduntantným…
		}

		private static class ObsahKonzoly extends PrototypKonzoly
		{
			public StringBuffer obsah = null, záloha = null;
			public Rectangle2D hranice = null; // sledovanie polohy textu
			public AktívneSlovo aktívneSlovo = null;
			public Slovo[] slová = null; // na zalamovanie textov

			public boolean označený = false;
			public Color označenéPozadie = null;
			public Color označenéPopredie = null;

			public void zafarbiAOznač(Color... farby)
			{
				označený = true;
				if (null != aktívneSlovo)
				{
					if (farby.length > 3)
					{
						označenéPozadie = farby[2];
						označenéPopredie = farby[3];
					}
					else if (farby.length > 2)
					{
						označenéPozadie = farby[2];
						označenéPopredie = farby[1];
					}
					else if (farby.length > 1)
					{
						označenéPozadie = farby[0];
						označenéPopredie = farby[1];
					}
					else if (farby.length > 0)
					{
						označenéPozadie = farby[0];
						označenéPopredie = null;
					}
					else
					{
						označenéPozadie = null;
						označenéPopredie = null;
					}
				}
				else
				{
					if (farby.length > 1)
					{
						označenéPozadie = farby[0];
						označenéPopredie = farby[1];
					}
					else if (farby.length > 0)
					{
						označenéPozadie = farby[0];
						označenéPopredie = null;
					}
					else
					{
						označenéPozadie = null;
						označenéPopredie = null;
					}
				}
			}
		}

		@SuppressWarnings("serial")
		private static class RiadokKonzoly extends Vector<PrototypKonzoly>
		{ public boolean označenýKoniecRiadka = false; }

		/*packagePrivate*/ /*static*/ class ZálohaKonzoly
		{
			public final Vector<RiadokKonzoly> záloha = new Vector<>();
		}


		// Pomocná metóda na zalamovanie textov ()
		private static Slovo[] rozbiNaSlová(
			StringBuffer text, AktívneSlovo aktívneSlovo)
		{
			// Nevyhnutné deklarácie, definície a zistenie počtu slov…
			Slovo[] slová; int počet = 1;
			for (int i = 0; -1 != (i = text.indexOf(" ", i)); ++i, ++počet);
			int index = 0, začiatok = 0, koniec = text.indexOf(" ", začiatok);

			if (null == aktívneSlovo)
			{
				slová = new Slovo[počet];

				while (-1 != koniec)
				{
					slová[index] = new Slovo();
					slová[index].slovo = text.substring(začiatok, koniec);

					začiatok = koniec + 1; ++index;
					koniec = text.indexOf(" ", začiatok);
				}

				slová[index] = new Slovo();
				slová[index].slovo = text.substring(začiatok, text.length());
			}
			else
			{
				slová = new AktívneSlovo[počet];

				while (-1 != koniec)
				{
					slová[index] = new AktívneSlovo();
					slová[index].slovo = text.substring(začiatok, koniec);
					((AktívneSlovo)slová[index]).identifikátor =
						aktívneSlovo.identifikátor;

					začiatok = koniec + 1; ++index;
					koniec = text.indexOf(" ", začiatok);
				}

				slová[index] = new AktívneSlovo();
				slová[index].slovo = text.substring(začiatok, text.length());
				((AktívneSlovo)slová[index]).identifikátor =
					aktívneSlovo.identifikátor;
			}

			return slová;
		}


		// Posunutie zadaného obdĺžnika – výhodné pre oblasť aktívneho slova
		private static void posuňObdĺžnik(
			Rectangle2D obdĺžnik, float x, float y)
		{
			if (obdĺžnik instanceof Rectangle2D.Float)
			{
				((Rectangle2D.Float)obdĺžnik).x += x;
				((Rectangle2D.Float)obdĺžnik).y += y;
			}
			else if (obdĺžnik instanceof Rectangle2D.Double)
			{
				((Rectangle2D.Double)obdĺžnik).x += x;
				((Rectangle2D.Double)obdĺžnik).y += y;
			}
		}

		// Rozšírenie zadaného obdĺžnika
		private static void rozšírObdĺžnik(
			Rectangle2D obdĺžnik, float Δx, float Δy)
		{
			if (obdĺžnik instanceof Rectangle2D.Float)
			{
				((Rectangle2D.Float)obdĺžnik).width += Δx;
				((Rectangle2D.Float)obdĺžnik).height += Δy;
			}
			else if (obdĺžnik instanceof Rectangle2D.Double)
			{
				((Rectangle2D.Double)obdĺžnik).width += Δx;
				((Rectangle2D.Double)obdĺžnik).height += Δy;
			}
		}

		// Overenie toho, či sa má farba aktívneho slova zmeniť alebo nie
		private static Color dajFarbuAktívnehoSlova(String slovo)
		{
			Color výsledok = null, zistenie;

			if (null != ObsluhaUdalostí.počúvadlo)
			{
				zistenie = ObsluhaUdalostí.počúvadlo.farbaAktívnehoSlova(slovo);
				if (null != zistenie) výsledok = zistenie;
				zistenie = ObsluhaUdalostí.počúvadlo.farbaAktivnehoSlova(slovo);
				if (null != zistenie) výsledok = zistenie;
			}

			int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
			for (int i = 0; i < početPočúvajúcich; ++i)
			{
				GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
				zistenie = počúvajúci.farbaAktívnehoSlova(slovo);
				if (null != zistenie) výsledok = zistenie;
				zistenie = počúvajúci.farbaAktivnehoSlova(slovo);
				if (null != zistenie) výsledok = zistenie;
			}

			return výsledok;
		}

		// Pridávanie medzier na účely výpisov vnútornej konzoly.
		// 
		private final static char[] nepridajMedzeruZa =
			{' ', ' ', '\t', '\n', '(', '[', '{', '„', '“', '«', '‹', '\'',
			 '\"'};
		private final static char[] nepridajMedzeruPred =
			{' ', ' ', '\t', '\n', '.', ',', ';', ':', '!', '?', ')', ']',
			 '}', '%', '“', '”', '»', '›', '\'', '\"'};
		// 

		/*packagePrivate*/ static void pridajMedzeru(StringBuffer obsah, Object časť)
		{
			String reťazec = časť.toString();
			if (reťazec.isEmpty()) return;

			boolean pridajMedzeru = obsah.length() != 0;

			if (pridajMedzeru)
			{
				char znak = obsah.charAt(obsah.length() - 1);
				for (char nepridaj : nepridajMedzeruZa)
					if (nepridaj == znak)
					{
						pridajMedzeru = false;
						break;
					}
			}

			if (pridajMedzeru)
			{
				char znak = reťazec.charAt(0);
				for (char nepridaj : nepridajMedzeruPred)
					if (nepridaj == znak)
					{
						pridajMedzeru = false;
						break;
					}
			}

			if (pridajMedzeru) obsah.append(" ");
		}

		/*packagePrivate*/ /*static*/ class VnútornáKonzola
		{
			// Písmo:
			/*packagePrivate*/ Písmo aktuálnePísmo = predvolenéPísmoKonzoly;

			// Synchronizačný zámok obsahu konzoly:
			private final Object zámokKonzoly = new Object();

			// Obsah vnútornej konzoly:
			private final Vector<RiadokKonzoly> riadky = new Vector<>();

			// Objekt slúžiaci na vytvorenie zálohy konzoly:
			private ZálohaKonzoly záloha;

			// Príznak slúžiaci na riadenie mazania znakov pri výskyte
			// kontrolného znaku BACKSPACE (ASCII 8 – \b):
			private boolean príznakBackspace = false;

			// // Príznak sledovania polohy textov vnútornej konzoly
			// // (predvolene vypnuté, aby boli nižšie pamäťové nároky na konzolu):
			// private boolean sledujPolohuTextu = false; // «zamietnuté»

			// Príznak zobrazenia textov:
			private boolean textyZobrazené = true;

			// Atribút zalamovania textov:
			private boolean zalamujTexty = false;

			// Úroveň posunutia textov vnútornej konzoly (pri rolovaní):
			private int posunutieTextovX = 0, posunutieTextovY = 0;
			private int poslednáVýškaTextu = 0, poslednáŠírkaTextu = 0;

			// Inštancie a príznaky rolovacích líšt…
			private RolovaciaLištaKonzoly zvislá = null, vodorovná = null;
			private boolean automatickéLišty = false;
			private Tlačidlo roh = null;

			// Uchovanie údajov o oblasti orezania, ktorú používajú aj
			// rolovacie lišty:
			private int xOrezania = 0, šírkaOrezania = 0,
				yOrezania = 0, výškaOrezania = 0;

			// Aktuálny objekt grafiky:
			private Graphics2D grafikaKonzoly = null;

			// Rozmery aktuálneho písma:
			private FontMetrics rozmeryPísma = null;

			// Pomocné premenné vykresľovania:
			private int x = 0, y = 0, x0 = 0;
			private int šírkaZalomenia = 0;
			private int šírkaMedzery = 0;
			private int výškaRiadka = 0, výškaKlienta = 0;
			private int počiatokX = 0, počiatokY = 0;
			private int výškaTextu = 0, šírkaTextu = 0;
			private int // odsadeniePrvého = 0,
				odsadenieZľava = 0, odsadenieSprava = 0;
			private Color zálohaFarby = null;
			private Color farbaAktívnehoSlova = null;
			private boolean aplikujOdsadeniaZľava = false;

			// Okraje kreslenia textov konzoly:
			private int ľavýOkraj = 0, hornýOkraj = 0,
				pravýOkraj = 0, dolnýOkraj = 0;

			// Zoznam aktívnych slov:
			private final Vector<AktívneSlovo> aktívneSlová = new Vector<>();

			// Príznak meniaci správanie príkazov výpisu tak, aby zadanie
			// farieb menilo farebnosť textov a pozadí textov namiesto
			// vypísania informácie o inštancii farby:
			private boolean nevypisujFarby = false;

			// Premenná upravujúca farbu textu konzoly
			// (aktivuje sa počas procesu písania textov):
			private Farba zmenaFarby = null;
			private Farba počiatočnáFarba = null;

			// Poznámka: Farba textov je menená priebežne počas výpisu
				// textov príkazmi inštancií PríkazKonzoly. Na začiatku
				// sa o nastavenie predvolenej farby postará konštruktor
				// robota, potom je pri každom vymazaní konzoly predvolená
				// tá farba, ktorá bola použitá naposledy. Keď je použité
				// pozadie, farbu textov je potrebné neustále zálohovať
				// podobne ako pri kreslení aktívnych slov.

			// Aktuálna faba pozadia textu (null znamená žiadna):
			private Farba farbaPozadia = null;

			// Slúži na zmenu farby pozadia počas písania textov:
			private Farba zmenaFarbyPozadia = null;
			private Farba počiatočnáFarbaPozadia = null;
			private boolean zrušeniePozadia = false;

			// Aplikácia príkazu píšNa:
			private boolean súradniceAplikované = false;
			private Point2D zmenaSúradníc = null;

			// Konfigurovateľné predvolené farby:
			private Farba predvolenáFarbaOznačenia = svetložltá;
			private Farba predvolenáFarbaTextuOznačenia = null;

			// Premenná upravujúca aktuálne aktívne slovo
			// (aktivuje sa počas procesu písania textov):
			private String identifikátorAktívnehoSlova = null;

			// Premenné upravujúce odsadenie (zľava a sprava…)
			// (aktivujú sa počas procesu písania textov):
			private Integer odsadeniePrvéhoRiadka = null;
			private Integer zmenaOdsadeniaZľava = null;
			private Integer zmenaOdsadeniaSprava = null;
			private Integer virtuálnyTabulátor = null;

			// Premenná požadujúca zamrazenie ľavého odsadenia
			// na aktuálnej pozícii:
			private boolean zamrazOdsadenie = false;
			private boolean resetOdsadenia = false;
			private int zálohaOdsadenia = 0;

			private void vykonajMazanieBackspace()
			{
				int riadkov = riadky.size();
				boolean nahradenieVykonané = true;

				while (nahradenieVykonané)
				{
					nahradenieVykonané = false;

					for (int i = 0; i < riadkov; ++i)
					{
						RiadokKonzoly riadok = riadky.get(i);
						int prototypov = riadok.size();

						for (int j = 0; j < prototypov; ++j)
						{
							PrototypKonzoly prototyp = riadok.get(j);

							if (prototyp instanceof ObsahKonzoly)
							{
								ObsahKonzoly obsah = (ObsahKonzoly)prototyp;
								if (null != obsah.obsah)
								{
									int indexOf = obsah.obsah.indexOf("\b");
									while (-1 != indexOf)
									{
										if (null == obsah.záloha)
											obsah.záloha = new StringBuffer(
												obsah.obsah);

										// Ak je backspace na začiatku riadka…
										if (0 == indexOf)
										{
											// … jednoducho ho vymaže a pripojí
											// na koniec predchádzajúceho bloku:
											obsah.obsah.deleteCharAt(indexOf);
											obsah.slová = null;
											obsah.hranice = null;

											for (int k = j - 1; k >= 0; --k)
											{
												prototyp = riadok.get(k);
												if (prototyp instanceof
													ObsahKonzoly)
												{
													ObsahKonzoly obsah2 =
														(ObsahKonzoly)prototyp;
													if (null != obsah2.obsah)
													{
														if (null ==
															obsah2.záloha)
															obsah2.záloha = new
																StringBuffer(
																	obsah2.obsah);
														obsah2.obsah.append("\b");
														obsah2.slová = null;
														obsah.hranice = null;
														break;
													}
												}
											}
										}
										else
										{
											// Inak vymaže dvojicu znakov:
											obsah.obsah.delete(
												indexOf - 1, indexOf + 1);
											obsah.slová = null;
											obsah.hranice = null;
										}

										indexOf = obsah.obsah.indexOf("\b");
										nahradenieVykonané = true;
									}
								}
							}
						}
					}
				}

				príznakBackspace = false;
			}

			private void inicializuj()
			{
				grafikaKonzoly.setFont(aktuálnePísmo);
				grafikaKonzoly.setColor(počiatočnáFarba);
				rozmeryPísma = grafikaKonzoly.getFontMetrics();
				výškaRiadka = rozmeryPísma.getHeight();
				počiatokX = 1;
				počiatokY = výškaRiadka - rozmeryPísma.getDescent()
					// - (rozmeryPísma.getDescent() / 5)
					;
				aktívneSlová.clear();
				if (príznakBackspace)
					vykonajMazanieBackspace();

				zálohaFarby = null;
				farbaAktívnehoSlova = null;

				// odsadeniePrvého = 0;
				odsadenieZľava = 0;
				odsadenieSprava = 0;

				// Nejako to blbne, jednoduchšie to bude bez toho:
				// if (null != zvislá && zvislá.isVisible())
				// 	pravýOkraj = zvislá.pôvodnýOkraj - zvislá.šírka;
				// TODO: stále? (nemám na to nervy už…)
				// if (null != vodorovná && vodorovná.isVisible())
				// 	dolnýOkraj = vodorovná.pôvodnýOkraj - vodorovná.výška;

				x = x0 = (šírkaPlátna - Svet.hlavnýPanel.getWidth()) / 2 +
					počiatokX + ľavýOkraj;
				y = (výškaPlátna - hornýOkraj - dolnýOkraj -
					(výškaKlienta = Svet.hlavnýPanel.getHeight() -
						hornýOkraj - dolnýOkraj) - 1) /
					2 + počiatokY + hornýOkraj;

				if (x < (počiatokX + ľavýOkraj))
					x = x0 = počiatokX + ľavýOkraj;
				if (y < (počiatokY + hornýOkraj))
					y = počiatokY + hornýOkraj;
			}

			private boolean vyhodnoťĽahkýPríkaz(PrototypKonzoly prototyp)
			{
				if (prototyp instanceof PríkazKonzoly)
				{
					PríkazKonzoly príkaz = (PríkazKonzoly)prototyp;

					if (null != príkaz.odsaďZľava)
					{
						odsadenieZľava = príkaz.odsaďZľava;

						if (aplikujOdsadeniaZľava)
						{
							x = odsadenieZľava + x0;
							// aplikujOdsadeniaZľava = false;
						}
					}

					if (null != príkaz.odsaďPrvý)
					{
						// odsadeniePrvého = príkaz.odsaďPrvý;
						if (aplikujOdsadeniaZľava)
						{
							// if (x < odsadenieZľava + x0 + príkaz.odsaďPrvý)
								x = odsadenieZľava + x0 + príkaz.odsaďPrvý;
						}
					}

					if (null != príkaz.odsaďTabulátorom)
					{
						x = odsadenieZľava + x0 + príkaz.odsaďTabulátorom;
						// aplikujOdsadeniaZľava = false;
					}

					if (null != príkaz.odsaďSprava)
						odsadenieSprava = príkaz.odsaďSprava;

					if (príkaz.zamrazOdsadenie)
					{
						odsadenieZľava = x - x0;
						//if (!súradniceAplikované)
						//	odsadenieZľava += posunutieTextovX;
					}
					return true;
				}
				return false;
			}

			private boolean vyhodnoťPríkaz(PrototypKonzoly prototyp)
			{
				if (prototyp instanceof PríkazKonzoly)
				{
					PríkazKonzoly príkaz = (PríkazKonzoly)prototyp;

					if (null != príkaz.odsaďZľava)
					{
						odsadenieZľava = príkaz.odsaďZľava;

						if (aplikujOdsadeniaZľava)
						{
							x = odsadenieZľava + x0;
							if (!súradniceAplikované)
								x -= posunutieTextovX;
							// aplikujOdsadeniaZľava = false;
						}
					}

					if (null != príkaz.odsaďPrvý)
					{
						// odsadeniePrvého = príkaz.odsaďPrvý;
						if (aplikujOdsadeniaZľava)
						{
							// if (x < odsadenieZľava + x0 + príkaz.odsaďPrvý)
								x = odsadenieZľava + x0 + príkaz.odsaďPrvý;
							if (!súradniceAplikované)
								x -= posunutieTextovX;
						}
					}

					if (null != príkaz.odsaďTabulátorom)
					{
						x = odsadenieZľava + x0 + príkaz.odsaďTabulátorom;
						if (!súradniceAplikované)
							x -= posunutieTextovX;
					}

					if (null != príkaz.odsaďSprava)
						odsadenieSprava = príkaz.odsaďSprava;

					if (príkaz.zamrazOdsadenie)
					{
						odsadenieZľava = x - x0;
						if (!súradniceAplikované)
							odsadenieZľava += posunutieTextovX;
					}

					if (null != príkaz.farba)
						grafikaKonzoly.setColor(príkaz.farba);

					if (null != príkaz.pozadie)
						farbaPozadia = príkaz.pozadie;

					if (príkaz.zrušPozadie)
						farbaPozadia = null;

					if (null != príkaz.poloha)
					{
						x = x0 = (int)príkaz.poloha.getX() + počiatokX;
						y = (int)príkaz.poloha.getY() + počiatokY;
					}

					return true;
				}
				return false;
			}

			private ObsahKonzoly dajObsah(PrototypKonzoly prototyp)
			{
				if (prototyp instanceof ObsahKonzoly)
				{
					ObsahKonzoly obsah = (ObsahKonzoly)prototyp;
					if (null == obsah.obsah) return null;
					return obsah;
				}
				return null;
			}

			private void kresliTextyZalamované()
			{
				// int xx = x, yy = y;

				for (RiadokKonzoly riadok : riadky)
				{
					for (PrototypKonzoly prototyp : riadok)
					{
						if (vyhodnoťPríkaz(prototyp)) continue;
						ObsahKonzoly obsah = dajObsah(prototyp);
						if (null == obsah) continue;

						if (null != obsah.slová)
						{
							boolean vložMedzeru = false;

							for (Slovo slovo : obsah.slová)
							{
								int šírkaSlova = rozmeryPísma.
									stringWidth(slovo.slovo);

								if (vložMedzeru) x += šírkaMedzery;

								if (0 != šírkaSlova)
								{
									if (x + šírkaSlova + 2 +
										odsadenieSprava >
										x0 + šírkaZalomenia -
										(súradniceAplikované ? 0 :
											posunutieTextovX))
									{
										if (šírkaSlova < šírkaZalomenia - 2 -
											odsadenieZľava - odsadenieSprava)
										{
											y += výškaRiadka;
											x = x0 + odsadenieZľava;
											if (!súradniceAplikované)
												x -= posunutieTextovX;
											// vložMedzeru = false;
										}
									}

									// Výpočet hraníc textu:
									slovo.hranice = rozmeryPísma.
										getStringBounds(slovo.slovo,
											grafikaKonzoly);

									if (vložMedzeru)
									{
										posuňObdĺžnik(slovo.hranice,
											x - 0.5f - šírkaMedzery,
											y - 0.5f);
										rozšírObdĺžnik(slovo.hranice,
											1.0f + šírkaMedzery, 2);
									}
									else
									{
										posuňObdĺžnik(slovo.hranice,
											x - 0.5f, y - 0.5f);
										rozšírObdĺžnik(slovo.hranice,
											1.0f, 2);
									}

									if (null != farbaPozadia || obsah.označený)
									{
										zálohaFarby = grafikaKonzoly.getColor();
										if (obsah.označený)
											grafikaKonzoly.setColor(null ==
												obsah.označenéPozadie ?
												predvolenáFarbaOznačenia :
												obsah.označenéPozadie);
										else
											grafikaKonzoly.setColor(farbaPozadia);
										grafikaKonzoly.fill(slovo.hranice);
										grafikaKonzoly.setColor(zálohaFarby);
									}

									if (slovo instanceof AktívneSlovo)
									{
										AktívneSlovo aktívne =
											(AktívneSlovo)slovo;

										// (Pozri aktívneSlováFIX.)
										// aktívneSlová.add(aktívne);

										farbaAktívnehoSlova =
											dajFarbuAktívnehoSlova(
												aktívne.identifikátor);

										// Použi farbu popredia označenia:
										if (obsah.označený && (null !=
											obsah.označenéPopredie || null !=
											predvolenáFarbaTextuOznačenia))
										{
											zálohaFarby = grafikaKonzoly.getColor();
											grafikaKonzoly.setColor(null ==
												obsah.označenéPopredie ?
												predvolenáFarbaTextuOznačenia :
												obsah.označenéPopredie);
											grafikaKonzoly.drawString(
												slovo.slovo, x, y);
											grafikaKonzoly.setColor(zálohaFarby);
										}
										else if (null == farbaAktívnehoSlova)
											grafikaKonzoly.drawString(
												slovo.slovo, x, y);
										else
										{
											zálohaFarby = grafikaKonzoly.getColor();
											grafikaKonzoly.setColor(
												farbaAktívnehoSlova);
											grafikaKonzoly.drawString(
												slovo.slovo, x, y);
											grafikaKonzoly.setColor(zálohaFarby);
										}

										aktívne.hranice = rozmeryPísma.
											getStringBounds(slovo.slovo,
												grafikaKonzoly);
										posuňObdĺžnik(aktívne.hranice, x, y);
									}
									else
									{
										// Použi farbu popredia označenia:
										if (obsah.označený && (null !=
											obsah.označenéPopredie || null !=
											predvolenáFarbaTextuOznačenia))
										{
											zálohaFarby = grafikaKonzoly.getColor();
											grafikaKonzoly.setColor(null ==
												obsah.označenéPopredie ?
												predvolenáFarbaTextuOznačenia :
												obsah.označenéPopredie);
											grafikaKonzoly.drawString(
												slovo.slovo, x, y);
											grafikaKonzoly.setColor(zálohaFarby);
										}
										else
											grafikaKonzoly.drawString(slovo.slovo, x, y);
									}
								}
								else
								{
									// Výpočet hraníc textu:
									slovo.hranice = rozmeryPísma.
										getStringBounds("", grafikaKonzoly);

									if (vložMedzeru)
									{
										posuňObdĺžnik(slovo.hranice,
											x - 0.5f - šírkaMedzery,
											y - 0.5f);
										rozšírObdĺžnik(slovo.hranice,
											1.0f + šírkaMedzery, 2);
									}
									else
									{
										posuňObdĺžnik(slovo.hranice,
											x - 0.5f, y - 0.5f);
										rozšírObdĺžnik(slovo.hranice,
											1.0f, 2);
									}

									if (null != farbaPozadia || obsah.označený)
									{
										zálohaFarby = grafikaKonzoly.getColor();
										if (obsah.označený)
											grafikaKonzoly.setColor(null ==
												obsah.označenéPozadie ?
												predvolenáFarbaOznačenia :
												obsah.označenéPozadie);
										else
											grafikaKonzoly.setColor(farbaPozadia);
										grafikaKonzoly.fill(slovo.hranice);
										grafikaKonzoly.setColor(zálohaFarby);
									}
								}

								if (!vložMedzeru) vložMedzeru = true;

								x += šírkaSlova //+ šírkaMedzery
								;
							}
						}

						aplikujOdsadeniaZľava = false;
						//x -= šírkaMedzery;
					}

					y += výškaRiadka;
					x = x0 + odsadenieZľava;
					aplikujOdsadeniaZľava = true;
					if (!súradniceAplikované)
						x -= posunutieTextovX;
				}
			}

			private void kresliTextyNezalamované()
			{
				for (RiadokKonzoly riadok : riadky)
				{
					for (PrototypKonzoly prototyp : riadok)
					{
						if (vyhodnoťPríkaz(prototyp)) continue;
						ObsahKonzoly obsah = dajObsah(prototyp);
						if (null == obsah) continue;

						String text = obsah.obsah.toString();

						// Výpočet hraníc textu
						obsah.hranice = rozmeryPísma.
							getStringBounds(text, grafikaKonzoly);
						posuňObdĺžnik(obsah.hranice, x, y);
						rozšírObdĺžnik(obsah.hranice, 0, 2);

						if (null != farbaPozadia || obsah.označený)
						{
							zálohaFarby = grafikaKonzoly.getColor();
							if (obsah.označený)
								grafikaKonzoly.setColor(null ==
									obsah.označenéPozadie ?
									predvolenáFarbaOznačenia :
									obsah.označenéPozadie);
							else
								grafikaKonzoly.setColor(farbaPozadia);
							grafikaKonzoly.fill(obsah.hranice);
							grafikaKonzoly.setColor(zálohaFarby);
						}

						if (null != obsah.aktívneSlovo)
						{
							// (Pozri aktívneSlováFIX.)
							// aktívneSlová.add(obsah.aktívneSlovo);

							farbaAktívnehoSlova = dajFarbuAktívnehoSlova(
								obsah.aktívneSlovo.identifikátor);

							// Použi farbu popredia označenia:
							if (obsah.označený && (null !=
								obsah.označenéPopredie || null !=
								predvolenáFarbaTextuOznačenia))
							{
								zálohaFarby = grafikaKonzoly.getColor();
								grafikaKonzoly.setColor(null ==
									obsah.označenéPopredie ?
									predvolenáFarbaTextuOznačenia :
									obsah.označenéPopredie);
								grafikaKonzoly.drawString(text, x, y);
								grafikaKonzoly.setColor(zálohaFarby);
							}
							else if (null == farbaAktívnehoSlova)
								grafikaKonzoly.drawString(text, x, y);
							else
							{
								zálohaFarby = grafikaKonzoly.getColor();
								grafikaKonzoly.setColor(farbaAktívnehoSlova);
								grafikaKonzoly.drawString(text, x, y);
								grafikaKonzoly.setColor(zálohaFarby);
							}

							obsah.aktívneSlovo.hranice = rozmeryPísma.
								getStringBounds(text, grafikaKonzoly);
							posuňObdĺžnik(obsah.aktívneSlovo.hranice, x, y);
						}
						else
						{
							// Použi farbu popredia označenia:
							if (obsah.označený && (null !=
								obsah.označenéPopredie || null !=
								predvolenáFarbaTextuOznačenia))
							{
								zálohaFarby = grafikaKonzoly.getColor();
								grafikaKonzoly.setColor(null ==
									obsah.označenéPopredie ?
									predvolenáFarbaTextuOznačenia :
									obsah.označenéPopredie);
								grafikaKonzoly.drawString(text, x, y);
								grafikaKonzoly.setColor(zálohaFarby);
							}
							else
								grafikaKonzoly.drawString(text, x, y);
						}

						x += rozmeryPísma.stringWidth(text);
						aplikujOdsadeniaZľava = false;
					}

					y += výškaRiadka;
					x = x0 + odsadenieZľava;
					aplikujOdsadeniaZľava = true;
					x -= posunutieTextovX;
				}
			}

			private void aktívneSlováFIX()
			{
				Color zálohaFarby = grafikaKonzoly.getColor();

				// int zálohaOdsadeniaPrvého = odsadeniePrvého;
				int zálohaOdsadeniaZľava = odsadenieZľava;
				int zálohaOdsadeniaSprava = odsadenieSprava;

				int zálohaX = x;
				int zálohaY = y;

				// Pokus o opravu zafarbovania aktívnych slov.
				for (RiadokKonzoly riadok : riadky)
				{
					for (PrototypKonzoly prototyp : riadok)
					{
						if (vyhodnoťPríkaz(prototyp)) continue;
						ObsahKonzoly obsah = dajObsah(prototyp);
						if (null == obsah) continue;

						if (null != obsah.slová)
						{
							for (Slovo slovo : obsah.slová)
								if (slovo instanceof AktívneSlovo &&
									0 != rozmeryPísma.stringWidth(slovo.slovo))
									aktívneSlová.add((AktívneSlovo)slovo);
						}
						else if (null != obsah.aktívneSlovo)
							aktívneSlová.add(obsah.aktívneSlovo);
					}
				}

				// odsadeniePrvého = zálohaOdsadeniaPrvého;
				odsadenieZľava = zálohaOdsadeniaZľava;
				odsadenieSprava = zálohaOdsadeniaSprava;

				x = zálohaX;
				y = zálohaY;

				grafikaKonzoly.setColor(zálohaFarby);
			}

			private void prekresli(Graphics2D grafika)
			{
				if (textyZobrazené) synchronized (zámokKonzoly) {

				grafikaKonzoly = grafika;
				inicializuj();

				Shape clip = grafikaKonzoly.getClip();

				boolean orezať = (0 != ľavýOkraj) || (0 != pravýOkraj) ||
					(0 != hornýOkraj) || (0 != dolnýOkraj);

				try {

				if (orezať)
				{
					int šírkaPanela = Svet.hlavnýPanel.getWidth();
					int výškaPanela = Svet.hlavnýPanel.getHeight();

					if (šírkaPanela > šírkaPlátna)
					{
						xOrezania = 0;
						šírkaOrezania = šírkaPlátna - 1;
					}
					else
					{
						xOrezania = (šírkaPlátna - šírkaPanela) / 2;
						// šírkaOrezania = (šírkaPlátna + šírkaPanela) / 2;
						// --šírkaOrezania;
						šírkaOrezania = šírkaPanela - 1;
					}

					if (výškaPanela > výškaPlátna)
					{
						yOrezania = 0;
						výškaOrezania = výškaPlátna - 1;
					}
					else
					{
						yOrezania = výškaPlátna - výškaPanela;
						yOrezania -= yOrezania % 2;
						yOrezania /= 2;

						// výškaOrezania = výškaPlátna + výškaPanela;
						// výškaOrezania -= výškaOrezania % 2;
						// výškaOrezania /= 2;
						// --výškaOrezania;

						výškaOrezania = výškaPanela - 1;
					}

					xOrezania += ľavýOkraj;
					šírkaOrezania -= pravýOkraj + ľavýOkraj;
					yOrezania += hornýOkraj;
					výškaOrezania -= dolnýOkraj + hornýOkraj;

					grafikaKonzoly.clipRect(xOrezania, yOrezania,
						šírkaOrezania, výškaOrezania);
				}

				if (zalamujTexty)
				{
					šírkaMedzery = rozmeryPísma.stringWidth(" ");
					šírkaZalomenia = Svet.hlavnýPanel.getWidth();
					if (šírkaZalomenia > šírkaPlátna)
						šírkaZalomenia = šírkaPlátna;
					šírkaZalomenia -= ľavýOkraj + pravýOkraj;

					výškaTextu = 0; šírkaTextu = 0;

					// Príprava na kreslenie:
					for (RiadokKonzoly riadok : riadky)
					{
						for (PrototypKonzoly prototyp : riadok)
						{
							if (vyhodnoťĽahkýPríkaz(prototyp)) continue;
							ObsahKonzoly obsah = dajObsah(prototyp);
							if (null == obsah) continue;

							if (null == obsah.slová)
								obsah.slová = rozbiNaSlová(
									obsah.obsah, obsah.aktívneSlovo);

							for (Slovo slovo : obsah.slová)
							{
								int šírkaSlova = rozmeryPísma.
									stringWidth(slovo.slovo);

								if (x + šírkaSlova + 2 + odsadenieSprava >
									x0 + šírkaZalomenia)
								{
									if (šírkaSlova < šírkaZalomenia - 2 -
										odsadenieZľava - odsadenieSprava)
									{
										výškaTextu += výškaRiadka;
										if ((x - x0 - odsadenieZľava) >
											šírkaTextu) šírkaTextu =
												x - x0 - odsadenieZľava;
										x = x0 + odsadenieZľava;
									}
								}

								x += šírkaSlova + šírkaMedzery;
							}

							x -= šírkaMedzery;
							aplikujOdsadeniaZľava = false;
						}

						výškaTextu += výškaRiadka;
						if ((x - x0 - odsadenieZľava) > šírkaTextu)
							šírkaTextu = x - x0 - odsadenieZľava;
						x = x0 + odsadenieZľava;
						aplikujOdsadeniaZľava = true;
					}

					// odsadeniePrvého =
					odsadenieZľava = odsadenieSprava = 0;
					x = x0;

					if (!súradniceAplikované)
					{
						if (výškaTextu > výškaKlienta ||
							výškaTextu > (výškaPlátna - hornýOkraj - dolnýOkraj))
						{
							int yRolujOd;

							if (výškaKlienta > (výškaPlátna -
								hornýOkraj - dolnýOkraj))
							{
								y = výškaPlátna - dolnýOkraj - výškaTextu;
								yRolujOd = počiatokY + hornýOkraj;
							}
							else
							{
								y = (výškaPlátna - hornýOkraj -
									dolnýOkraj + výškaKlienta) /
									2 - výškaTextu + hornýOkraj;
								yRolujOd = (výškaPlátna - hornýOkraj
									- dolnýOkraj - výškaKlienta) /
									2 + počiatokY + hornýOkraj;
							}

							int yRolujDo = y;
							if (posunutieTextovX < 0) posunutieTextovX = 0;
							//else if (posunutieTextovX <= šírkaZalomenia)
							//{??? prečo to nejde ???
							//	if (posunutieTextovX > 0) posunutieTextovX = 0;
							//}
							else if (posunutieTextovX > šírkaTextu
								//- šírkaZalomenia + 3 * šírkaMedzery
								)
								posunutieTextovX = šírkaTextu
								//- šírkaZalomenia + 3 * šírkaMedzery
								;

							y += posunutieTextovY;
							x -= posunutieTextovX;
							// kedysi posunutieTextovX kolidovalo so
							// zalamovaním, ale pri Pasquile sa to podarilo
							// opraviť, tak som to spravil aj tu

							if (y > yRolujOd)
							{
								int Δy = y - yRolujOd;
								y -= posunutieTextovY;
								posunutieTextovY -= Δy;
								y += posunutieTextovY;
							}

							if (y < yRolujDo)
							{
								int Δy = y - yRolujDo;
								y -= posunutieTextovY;
								posunutieTextovY -= Δy;
								y += posunutieTextovY;
							}
						}
						else
						{
							if (posunutieTextovX < 0) posunutieTextovX = 0;
							//else if (posunutieTextovX <= šírkaZalomenia)
							//{??? prečo to nejde ???
							//	if (posunutieTextovX > 0) posunutieTextovX = 0;
							//}
							else if (posunutieTextovX > šírkaTextu
								//- šírkaZalomenia + 3 * šírkaMedzery
								)
								posunutieTextovX = šírkaTextu
								//- šírkaZalomenia + 3 * šírkaMedzery
								;

							// y += posunutieTextovY;
							x -= posunutieTextovX;
						}
					}

					aktívneSlováFIX();
					kresliTextyZalamované();
				}
				else
				{
					výškaTextu = riadky.size() * výškaRiadka;
					šírkaTextu = 0;

					if (!súradniceAplikované)
					{
						for (RiadokKonzoly riadok : riadky)
						{
							for (PrototypKonzoly prototyp : riadok)
							{
								if (vyhodnoťĽahkýPríkaz(prototyp)) continue;
								ObsahKonzoly obsah = dajObsah(prototyp);
								if (null == obsah) continue;

								x += rozmeryPísma.stringWidth(
									obsah.obsah.toString());
								aplikujOdsadeniaZľava = false;
							}

							if ((x - x0 - odsadenieZľava) > šírkaTextu)
								šírkaTextu = x - x0 - odsadenieZľava;
							x = x0 + odsadenieZľava;
							aplikujOdsadeniaZľava = true;
						}

						// odsadeniePrvého =
						odsadenieZľava = odsadenieSprava = 0;
						x = x0;

						if (výškaTextu > výškaKlienta ||
							výškaTextu > (výškaPlátna - hornýOkraj - dolnýOkraj))
						{
							int yRolujOd;

							if (výškaKlienta > (výškaPlátna -
								hornýOkraj - dolnýOkraj))
							{
								y = výškaPlátna - dolnýOkraj - výškaTextu;
								yRolujOd = počiatokY + hornýOkraj;
							}
							else
							{
								y = (výškaPlátna - hornýOkraj -
									dolnýOkraj + výškaKlienta) /
									2 - výškaTextu + hornýOkraj;
								yRolujOd = (výškaPlátna - hornýOkraj -
									dolnýOkraj - výškaKlienta) /
									2 + počiatokY + hornýOkraj;
							}

							int yRolujDo = y;
							if (posunutieTextovX < 0) posunutieTextovX = 0;
							else if (posunutieTextovX > šírkaTextu)
								posunutieTextovX = šírkaTextu;

							y += posunutieTextovY;
							x -= posunutieTextovX;

							if (y > yRolujOd)
							{
								int Δy = y - yRolujOd;
								y -= posunutieTextovY;
								posunutieTextovY -= Δy;
								y += posunutieTextovY;
							}

							if (y < yRolujDo)
							{
								int Δy = y - yRolujDo;
								y -= posunutieTextovY;
								posunutieTextovY -= Δy;
								y += posunutieTextovY;
							}
						}
						else
						{
							if (posunutieTextovX < 0) posunutieTextovX = 0;
							else if (posunutieTextovX > šírkaTextu)
								posunutieTextovX = šírkaTextu;

							// y += posunutieTextovY;
							x -= posunutieTextovX;
						}

						aktívneSlováFIX();
						kresliTextyNezalamované();
					}
					else
					{
						aktívneSlováFIX();
						for (RiadokKonzoly riadok : riadky)
						{
							for (PrototypKonzoly prototyp : riadok)
							{
								if (vyhodnoťPríkaz(prototyp)) continue;
								ObsahKonzoly obsah = dajObsah(prototyp);
								if (null == obsah) continue;

								String text = obsah.obsah.toString();

								// Výpočet hraníc textu:
								obsah.hranice = rozmeryPísma.
									getStringBounds(text, grafikaKonzoly);
								posuňObdĺžnik(obsah.hranice, x, y);
								rozšírObdĺžnik(obsah.hranice, 0, 2);

								if (null != farbaPozadia || obsah.označený)
								{
									zálohaFarby = grafikaKonzoly.getColor();
									if (obsah.označený)
										grafikaKonzoly.setColor(null ==
											obsah.označenéPozadie ?
											predvolenáFarbaOznačenia :
											obsah.označenéPozadie);
									else
										grafikaKonzoly.setColor(farbaPozadia);
									grafikaKonzoly.fill(obsah.hranice);
									grafikaKonzoly.setColor(zálohaFarby);
								}

								if (null != obsah.aktívneSlovo)
								{
									// (Pozri aktívneSlováFIX.)
									// aktívneSlová.add(obsah.aktívneSlovo);

									farbaAktívnehoSlova =
										dajFarbuAktívnehoSlova(
											obsah.aktívneSlovo.identifikátor);

									// Použi farbu popredia označenia:
									if (obsah.označený && (null !=
										obsah.označenéPopredie || null !=
										predvolenáFarbaTextuOznačenia))
									{
										zálohaFarby = grafikaKonzoly.getColor();
										grafikaKonzoly.setColor(null ==
											obsah.označenéPopredie ?
											predvolenáFarbaTextuOznačenia :
											obsah.označenéPopredie);
										grafikaKonzoly.drawString(text, x, y);
										grafikaKonzoly.setColor(zálohaFarby);
									}
									else if (null == farbaAktívnehoSlova)
										grafikaKonzoly.drawString(text, x, y);
									else
									{
										zálohaFarby = grafikaKonzoly.getColor();
										grafikaKonzoly.setColor(farbaAktívnehoSlova);
										grafikaKonzoly.drawString(text, x, y);
										grafikaKonzoly.setColor(zálohaFarby);
									}

									obsah.aktívneSlovo.hranice = rozmeryPísma.
										getStringBounds(text, grafikaKonzoly);
									posuňObdĺžnik(obsah.aktívneSlovo.
										hranice, x, y);
								}
								else
								{
									// Použi farbu popredia označenia:
									if (obsah.označený && (null !=
										obsah.označenéPopredie || null !=
										predvolenáFarbaTextuOznačenia))
									{
										zálohaFarby = grafikaKonzoly.getColor();
										grafikaKonzoly.setColor(null ==
											obsah.označenéPopredie ?
											predvolenáFarbaTextuOznačenia :
											obsah.označenéPopredie);
										grafikaKonzoly.drawString(text, x, y);
										grafikaKonzoly.setColor(zálohaFarby);
									}
									else
										grafikaKonzoly.drawString(text, x, y);
								}

								x += rozmeryPísma.stringWidth(text);
								aplikujOdsadeniaZľava = false;
							}

							y += výškaRiadka;
							x = x0 + odsadenieZľava;
							aplikujOdsadeniaZľava = true;
						}
					}
				}

				poslednáVýškaTextu = výškaTextu;
				poslednáŠírkaTextu = šírkaTextu;

				aktualizujLišty();

				} finally { if (orezať) grafikaKonzoly.setClip(clip); } }
			}


			public void prevezmiTexty(StringBuffer texty)
			{
				for (RiadokKonzoly riadok : riadky)
				{
					for (PrototypKonzoly prototyp : riadok)
					{
						ObsahKonzoly obsah = dajObsah(prototyp);
						if (null == obsah) continue;
						if (null != obsah.záloha)
							texty.append(obsah.záloha);
						else
							texty.append(obsah.obsah);
					}
					texty.append("\n");
				}
			}

			public void prevezmiTexty(StringBuffer texty, boolean lenOznačené)
			{
				if (lenOznačené)
				{
					boolean prvýVýpis = true;
					int početVynechaných = 0;
					boolean počítajVynechané = false;

					for (RiadokKonzoly riadok : riadky)
					{
						boolean prvýVRiadku = true;
						for (PrototypKonzoly prototyp : riadok)
						{
							ObsahKonzoly obsah = dajObsah(prototyp);
							if (null == obsah) continue;
							if (!obsah.označený)
							{
								if (počítajVynechané)
								{
									početVynechaných = 1;
									počítajVynechané = false;
								}
								continue;
							}

							počítajVynechané = true;
							if (prvýVýpis)
							{
								prvýVýpis = false;
								prvýVRiadku = false;
							}
							else if (prvýVRiadku)
							{
								for (int i = 0; i < početVynechaných; ++i)
									texty.append("\n");
								početVynechaných = 0;
								prvýVRiadku = false;
							}

							if (null != obsah.záloha)
								texty.append(obsah.záloha);
							else
								texty.append(obsah.obsah);
						}

						if (počítajVynechané)
							++početVynechaných;
					}
				}
				else prevezmiTexty(texty);
			}

			public boolean jestvujeOznačenie()
			{
				for (RiadokKonzoly riadok : riadky)
					for (PrototypKonzoly prototyp : riadok)
					{
						ObsahKonzoly obsah = dajObsah(prototyp);
						if (null == obsah) continue;
						if (obsah.označený) return true;
					}

				return false;
			}

			public Farba farba()
			{
				if (null != zmenaFarby) return zmenaFarby;

				for (int i = riadky.size() - 1; i >= 0; --i)
				{
					RiadokKonzoly riadok = riadky.get(i);
					for (int j = riadok.size() - 1; j >= 0; --j)
					{
						PrototypKonzoly prototyp = riadok.get(j);
						if (prototyp instanceof PríkazKonzoly)
						{
							PríkazKonzoly príkaz = (PríkazKonzoly)prototyp;
							if (null != príkaz.farba) return príkaz.farba;
						}
					}
				}

				return počiatočnáFarba;
			}

			public Farba farbaPozadia()
			{
				if (zrušeniePozadia) return null;
				if (null != zmenaFarbyPozadia) return zmenaFarbyPozadia;

				for (int i = riadky.size() - 1; i >= 0; --i)
				{
					RiadokKonzoly riadok = riadky.get(i);
					for (int j = riadok.size() - 1; j >= 0; --j)
					{
						PrototypKonzoly prototyp = riadok.get(j);
						if (prototyp instanceof PríkazKonzoly)
						{
							PríkazKonzoly príkaz = (PríkazKonzoly)prototyp;
							if (príkaz.zrušPozadie) return null;
							if (null != príkaz.pozadie) return príkaz.pozadie;
						}
					}
				}

				return počiatočnáFarbaPozadia;
			}

			public void vypíš(Object... argumenty)
			{
				synchronized (zámokKonzoly) {

				if (0 == riadky.size()) novýRiadok();

				if (!konzolaPoužitá)
				{
					konzolaPoužitá = true;
					if (null != Svet.hlavnýRobot)
						Svet.hlavnýRobot.skry();
				}

				RiadokKonzoly riadok = riadky.lastElement();
				PríkazKonzoly príkaz = null;

				Farba zálohaFarby, zálohaPozadia;
				boolean farbyUpravené = false;

				if (nevypisujFarby)
				{
					zálohaFarby = farba();
					zálohaPozadia = farbaPozadia();
				}
				else
					zálohaFarby = zálohaPozadia = null;

				if (null != zmenaFarby)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.farba = zmenaFarby;
					zmenaFarby = null;
				}

				if (null != zmenaFarbyPozadia)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.pozadie = zmenaFarbyPozadia;
					zmenaFarbyPozadia = null;
				}

				if (zrušeniePozadia)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.zrušPozadie = true;
					zrušeniePozadia = false;
				}

				if (null != zmenaSúradníc)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.poloha = zmenaSúradníc;
					súradniceAplikované = true;
					zmenaSúradníc = null;
				}

				if (null != odsadeniePrvéhoRiadka)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.odsaďPrvý = odsadeniePrvéhoRiadka;
					odsadeniePrvéhoRiadka = null;
				}

				if (null != zmenaOdsadeniaZľava)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.odsaďZľava = zmenaOdsadeniaZľava;
					zálohaOdsadenia = zmenaOdsadeniaZľava;
					zmenaOdsadeniaZľava = null;
				}

				if (null != zmenaOdsadeniaSprava)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.odsaďSprava = zmenaOdsadeniaSprava;
					zmenaOdsadeniaSprava = null;
				}

				if (null != virtuálnyTabulátor)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.odsaďTabulátorom = virtuálnyTabulátor;
					virtuálnyTabulátor = null;
				}

				if (zamrazOdsadenie)
				{
					if (null == príkaz) príkaz = new PríkazKonzoly();
					príkaz.zamrazOdsadenie = zamrazOdsadenie;
					zamrazOdsadenie = false;
					resetOdsadenia = true;
				}

				if (null != príkaz)
				{
					riadok.add(príkaz);
					príkaz = null;
				}

				ObsahKonzoly obsah = null;

				if (null != identifikátorAktívnehoSlova)
				{
					obsah = new ObsahKonzoly();
					obsah.aktívneSlovo = new AktívneSlovo();
					obsah.aktívneSlovo.identifikátor =
						identifikátorAktívnehoSlova;
				}

				int indexFarby = 0;

				for (Object argument : argumenty)
				{
					if (argument instanceof GRobot)
					{
						((GRobot)argument).ukáž();
						continue;
					}

					if (nevypisujFarby)
					{
						if (argument instanceof Farba)
						{
							Farba nováFarba = (Farba)argument;

							switch (indexFarby++)
							{
							case 0:
								if (žiadna != nováFarba)
								{
									if (null == príkaz)
										príkaz = new PríkazKonzoly();
									príkaz.farba = nováFarba;
								}
								break;

							case 1:
								if (null == príkaz)
									príkaz = new PríkazKonzoly();
								if (žiadna != nováFarba)
									príkaz.pozadie = nováFarba;
								else
									príkaz.zrušPozadie = true;
								break;
							}

							continue;
						}
						else if (argument instanceof Color)
						{
							Color nováFarba = (Farba)argument;

							switch (indexFarby++)
							{
							case 0:
								if (null == príkaz)
									príkaz = new PríkazKonzoly();
								príkaz.farba = new Farba(nováFarba);
								break;

							case 1:
								if (null == príkaz)
									príkaz = new PríkazKonzoly();
								príkaz.pozadie = new Farba(nováFarba);
								break;
							}

							continue;
						}

						if (null != príkaz)
						{
							if (null != obsah)
							{
								riadok.add(obsah);
								obsah = null;
							}

							indexFarby = 0;
							riadok.add(príkaz);
							príkaz = null;
							farbyUpravené = true;
						}
					}

					if (null == obsah) obsah = new ObsahKonzoly();

					if (null == obsah.obsah)
						obsah.obsah = new StringBuffer();

					if (null == argument)
					{
						pridajMedzeru(obsah.obsah, Súbor.nullString);
						obsah.obsah.append(Súbor.nullString);
					}
					else if (argument instanceof byte[])
					{
						byte[] pole = (byte[])argument;
						pridajMedzeru(obsah.obsah, 0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof short[])
					{
						short[] pole = (short[])argument;
						pridajMedzeru(obsah.obsah, 0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof int[])
					{
						int[] pole = (int[])argument;
						pridajMedzeru(obsah.obsah, 0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof long[])
					{
						long[] pole = (long[])argument;
						pridajMedzeru(obsah.obsah, 0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof float[])
					{
						float[] pole = (float[])argument;
						pridajMedzeru(obsah.obsah, 0.0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof double[])
					{
						double[] pole = (double[])argument;
						pridajMedzeru(obsah.obsah, 0.0);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(
								Svet.formát.format(pole[i]));
						}
					}
					else if (argument instanceof boolean[])
					{
						boolean[] pole = (boolean[])argument;
						pridajMedzeru(obsah.obsah, false);
						for (int i = 0; i < pole.length; ++i)
						{
							if (i > 0) obsah.obsah.append(
								Svet.oddeľovačPrvkovPoľa);
							obsah.obsah.append(pole[i]);
						}
					}
					else if (argument instanceof Uhol)
					{
						String formátované = ((Uhol)argument).toString();
						pridajMedzeru(obsah.obsah, formátované);
						obsah.obsah.append(formátované);
					}
					else if (argument instanceof Number)
					{
						String formátované = Svet.formát.format(argument);
						pridajMedzeru(obsah.obsah, formátované);
						obsah.obsah.append(formátované);
					}
					else
					{
						StringBuffer naPridanie;
						if (argument instanceof char[])
							naPridanie = new StringBuffer(
								new String((char[])argument));
						else
							naPridanie = new StringBuffer(
								argument.toString());

						if (-1 != naPridanie.indexOf("\b"))
							príznakBackspace = true;

						pridajMedzeru(obsah.obsah, naPridanie);

						// Prvá fáza nahrádzania tabulátorov –
						// doterajší riadok:
						int tabPos = naPridanie.indexOf("\t");
						int newLine = naPridanie.indexOf("\n");

						if (-1 == newLine)
						{
							// Zisti doterajší počet znakov na riadku:
							int početZnakov = 0;
							for (PrototypKonzoly prototyp : riadok)
							{
								ObsahKonzoly obsah2 = dajObsah(prototyp);
								if (null == obsah2) continue;
								if (null != obsah2.záloha)
									početZnakov += obsah2.záloha.length();
								else
									početZnakov += obsah2.obsah.length();
							}
							početZnakov += obsah.obsah.length();

							while (tabPos != -1)
							{
								switch ((početZnakov + tabPos) % 4)
								{
									case 0:
										naPridanie.replace(
											tabPos, tabPos + 1,
											"    "); break;
									case 1:
										naPridanie.replace(
											tabPos, tabPos + 1,
											"   "); break;
									case 2:
										naPridanie.replace(
											tabPos, tabPos + 1,
											"  "); break;
									default:
										naPridanie.setCharAt(tabPos, ' ');
								}
								tabPos = naPridanie.indexOf("\t");
							}
						}
						else
						{
							if (tabPos != -1 && tabPos < newLine)
							{
								// Zisti doterajší počet znakov na riadku:
								int početZnakov = 0;
								for (PrototypKonzoly prototyp : riadok)
								{
									ObsahKonzoly obsah2 = dajObsah(prototyp);
									if (null == obsah2) continue;
									if (null != obsah2.záloha)
										početZnakov += obsah2.záloha.length();
									else
										početZnakov += obsah2.obsah.length();
								}
								početZnakov += obsah.obsah.length();

								while (tabPos != -1 &&
									(tabPos < newLine || -1 == newLine))
								{
									switch ((početZnakov + tabPos) % 4)
									{
										case 0:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"    "); break;
										case 1:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"   "); break;
										case 2:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"  "); break;
										default:
											naPridanie.setCharAt(tabPos, ' ');
									}
									tabPos = naPridanie.indexOf("\t");
									newLine = naPridanie.indexOf("\n");
								}
							}

							// Nahrádzanie nových riadkov:
							while (newLine != -1)
							{
								obsah.obsah.append(
									naPridanie.substring(0, newLine));
								riadok.add(obsah);
								novýRiadok();
								riadok = riadky.lastElement();
								obsah = new ObsahKonzoly();
								if (null != identifikátorAktívnehoSlova)
								{
									obsah.aktívneSlovo = new AktívneSlovo();
									obsah.aktívneSlovo.identifikátor =
										identifikátorAktívnehoSlova;
								}
								obsah.obsah = new StringBuffer();
								naPridanie.delete(0, newLine + 1);

								// Druhá fáza nahrádzania tabulátorov –
								// ďalšie riadky:
								tabPos = naPridanie.indexOf("\t");
								newLine = naPridanie.indexOf("\n");
								while (tabPos != -1 &&
									(tabPos < newLine || -1 == newLine))
								{
									switch (tabPos % 4)
									{
										case 0:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"    "); break;
										case 1:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"   "); break;
										case 2:
											naPridanie.replace(
												tabPos, tabPos + 1,
												"  "); break;
										default:
											naPridanie.setCharAt(tabPos, ' ');
									}
									tabPos = naPridanie.indexOf("\t");
									newLine = naPridanie.indexOf("\n");
								}
							}
						}

						obsah.obsah.append(naPridanie);
					}
				}

				if (null != obsah) riadok.add(obsah);

				if (farbyUpravené)
				{
					farbaTextu(zálohaFarby);
					farbaPozadiaTextu(zálohaPozadia);
				}}
			}

			public void novýRiadok()
			{
				RiadokKonzoly riadok = new RiadokKonzoly();
				riadky.add(riadok);

				if (resetOdsadenia)
				{
					PríkazKonzoly príkaz = new PríkazKonzoly();
					príkaz.odsaďZľava = zálohaOdsadenia;
					riadok.add(príkaz);
				}
			}

			public void vymaž()
			{
				posunutieTextovX = posunutieTextovY = 0;
				poslednáVýškaTextu = 0;
				poslednáŠírkaTextu = 0;
				počiatočnáFarbaPozadia = farbaPozadia();
				// zmenaFarbyPozadia = null;
				zrušeniePozadia = null == počiatočnáFarbaPozadia;
				počiatočnáFarba = farba();
				// zmenaFarby = null;
				riadky.clear();
				aktívneSlová.clear();
				súradniceAplikované = false;
				identifikátorAktívnehoSlova = null;
				odsadeniePrvéhoRiadka = null;
				zmenaOdsadeniaZľava = null;
				zmenaOdsadeniaSprava = null;
				virtuálnyTabulátor = null;
				zmenaSúradníc = null;
				zamrazOdsadenie = false;
				zálohaOdsadenia = 0;
				resetOdsadenia = false;
			}

			public void skryTexty()
			{
				textyZobrazené = false;
			}

			public void zobrazTexty()
			{
				textyZobrazené = true;
			}

			public boolean textyZobrazené()
			{
				return textyZobrazené;
			}


			@SuppressWarnings("serial")
			private class RolovaciaLištaKonzoly extends RolovaciaLišta
				implements AdjustmentListener
			{
				public RolovaciaLištaKonzoly(int orientácia)
				{
					super(orientácia);

					// Pridá vlastnú obsluhu rolovania:
					addAdjustmentListener(this);
				}

				public void adjustmentValueChanged(AdjustmentEvent e)
				{
					// Poznámka: Nie je celkom okay, ale asi ani nebude.
					// (Pozri aj poznámku v dokumentácii k triede
					// RolovaciaLišta.)
					if (e.getAdjustable() instanceof RolovaciaLištaKonzoly)
					{
						RolovaciaLištaKonzoly lišta =
							(RolovaciaLištaKonzoly)e.getAdjustable();

						if (lišta.getValueIsAdjusting())
						{
							if (vodorovná == lišta)
							{
								int šírkaOblasti = Svet.hlavnýPanel.getWidth();
								if (šírkaOblasti > šírkaPlátna)
									šírkaOblasti = šírkaPlátna;
								šírkaOblasti -= ľavýOkraj + pravýOkraj + 20;

								posunutieTextovX = e.getValue();
								Svet.automatickéPrekreslenie(); // noInvokeLater
							}
							else if (zvislá == lišta)
							{
								int výškaOblasti = Svet.hlavnýPanel.getHeight();
								if (výškaOblasti > výškaPlátna)
									výškaOblasti = výškaPlátna;
								výškaOblasti -= hornýOkraj + dolnýOkraj + 20;

								posunutieTextovY = poslednáVýškaTextu -
									e.getValue() - výškaOblasti;
								Svet.automatickéPrekreslenie(); // noInvokeLater
							}
						}
					}
				}

				@Override /*packagePrivate*/ void umiestni(int x1, int y1,
					int šírka1, int výška1)
				{
					int x = x1 + super.x,
						y = y1 + super.y,
						šírka = super.šírka,
						výška = super.výška;

					if (getOrientation() == ZVISLÁ)
					{
						výška = výška1;
						y += ((super.výška - výška1) / 2) - (výška1 % 2);

						if (y < y1 || y < 0)
						{
							if (y1 < 0)
							{
								výška += y;
								y = 0;
							}
							else if (y < y1)
							{
								výška += y - y1;
								y = y1;
							}
						}

						if (y1 < 0)
						{
							if ((výška + y) > výška1)
								výška = výška1 - y;
						}
						else
						{
							if ((výška + y - y1) > výška1)
								výška = výška1 + y1 - y;
						}

						y += hornýOkraj;
						výška -= hornýOkraj + dolnýOkraj;

						x -= ((super.šírka - šírka1) / 2) + pravýOkraj;

						if (null != vodorovná && vodorovná.isVisible())
						{
							výška -= vodorovná.výška;
						}
					}

					if (getOrientation() == VODOROVNÁ)
					{
						šírka = šírka1;
						x += ((super.šírka - šírka1) / 2) - (šírka1 % 2);

						if (x < x1 || x < 0)
						{
							if (x1 < 0)
							{
								šírka += x;
								x = 0;
							}
							else if (x < x1)
							{
								šírka += x - x1;
								x = x1;
							}
						}

						if (x1 < 0)
						{
							if ((šírka + x) > šírka1)
								šírka = šírka1 - x;
						}
						else
						{
							if ((šírka + x - x1) > šírka1)
								šírka = šírka1 + x1 - x;
						}

						x += ľavýOkraj;
						šírka -= ľavýOkraj + pravýOkraj;

						y -= ((super.výška - výška1) / 2) + dolnýOkraj;

						if (null != zvislá && zvislá.isVisible())
						{
							šírka -= zvislá.šírka;
						}
					}

					setBounds(x, y, šírka, výška);
				}
			}

			private void zobrazVytvorRoh()
			{
				if (null != zvislá && zvislá.isVisible() &&
					null != vodorovná && vodorovná.isVisible())
				{
					if (null == roh)
					{
						// Vytvorenie výplne rohu:
						Obrázok výplňRohu = new Obrázok(
							zvislá.šírka, vodorovná.výška);
						Color výplň = UIManager.getColor(
							"Panel.background");
						if (null == výplň) výplň = šedá.svetlejšia();
						výplňRohu.vyplň(výplň);

						// Vytvorenie samotného rohu, ktorý bude
						// umiestnený v kúte medzi lištami:
						roh = new Tlačidlo(výplňRohu);
						roh.setVisible(false);

						// SwingUtilities.invokeLater(() ->
						// 	{
								roh.deaktivuj();
								roh.obrázokDeaktivovaného(výplňRohu);
								roh.prilepVpravo();
								roh.prilepDole();

						// 		roh.polohaX(-pravýOkraj);
						// 		roh.polohaY(dolnýOkraj);
						// 		roh.setVisible(true);
						// 	});
					}
					// else
					// {
						roh.polohaX(-pravýOkraj);
						roh.polohaY(dolnýOkraj);
						roh.setVisible(true);
					// }
				}
				else
				{
					if (null != roh) roh.setVisible(false);
				}
			}

			private void zobrazVytvorZvislú(boolean ánoNie)
			{
				if (ánoNie)
				{
					if (null == zvislá)
						zvislá = new RolovaciaLištaKonzoly(ZVISLÁ);

					if (!zvislá.isVisible())
					{
						zvislá.setVisible(true);
					}
				}
				else
				{
					if (null != zvislá && zvislá.isVisible())
					{
						zvislá.setVisible(false);
					}
				}
			}

			private void zobrazVytvorVodorovnú(boolean ánoNie)
			{
				if (ánoNie)
				{
					if (null == vodorovná)
						vodorovná = new RolovaciaLištaKonzoly(VODOROVNÁ);

					if (!vodorovná.isVisible())
					{
						vodorovná.setVisible(true);
					}
				}
				else
				{
					if (null != vodorovná && vodorovná.isVisible())
					{
						vodorovná.setVisible(false);
					}
				}
			}

			private void aktualizujLišty()
			{
				int výškaOblasti = Svet.hlavnýPanel.getHeight();
				int šírkaOblasti = Svet.hlavnýPanel.getWidth();

				if (výškaOblasti > výškaPlátna)
					výškaOblasti = výškaPlátna;
				if (šírkaOblasti > šírkaPlátna)
					šírkaOblasti = šírkaPlátna;

				// System.out.println("výškaOblasti1: " + výškaOblasti);
				// System.out.println("šírkaOblasti1: " + šírkaOblasti);

				výškaOblasti -= hornýOkraj + dolnýOkraj + 20;
				šírkaOblasti -= ľavýOkraj + pravýOkraj + 20;

				if (automatickéLišty)
				{
					// System.out.println("Evidujem potrebu autolíšt.");
					// System.out.println("  poslednáVýškaTextu: " +
					// 	poslednáVýškaTextu);
					// System.out.println("  výškaOblasti: " +
					// 	výškaOblasti);
					// System.out.println("  poslednáŠírkaTextu: " +
					// 	poslednáŠírkaTextu);
					// System.out.println("  šírkaOblasti: " +
					// 	šírkaOblasti);

					zobrazVytvorZvislú(poslednáVýškaTextu > výškaOblasti);
					zobrazVytvorVodorovnú(poslednáŠírkaTextu > šírkaOblasti);

					SwingUtilities.invokeLater(() ->
					{
						if (GRobot.svet.isVisible())
						{
							Svet.hlavnýPanel.requestFocusInWindow();
							if (Svet.panelVstupnéhoRiadka.isVisible())
								Svet.vstupnýRiadok.requestFocus();
						}
					});
				}

				if (null != zvislá && zvislá.isVisible())
				{
					// Pokúsi sa kompenzovať sabotáže:
					if (zvislá.getOrientation() != ZVISLÁ)
						zvislá.setOrientation(ZVISLÁ);

					// Aktualizuje údaje:
					zvislá.setMaximum(poslednáVýškaTextu);
					zvislá.setValue(poslednáVýškaTextu -
						posunutieTextovY - výškaOblasti);
					zvislá.setVisibleAmount(výškaOblasti);
				}

				if (null != vodorovná && vodorovná.isVisible())
				{
					// Pokúsi sa kompenzovať sabotáže:
					if (vodorovná.getOrientation() != VODOROVNÁ)
						vodorovná.setOrientation(VODOROVNÁ);

					// Aktualizuje údaje:
					vodorovná.setMaximum(poslednáŠírkaTextu);
					vodorovná.setValue(posunutieTextovX);
					vodorovná.setVisibleAmount(šírkaOblasti);
				}

				zobrazVytvorRoh();
			}

			public void automatickéZobrazovanie(boolean zapnúť)
			{
				automatickéLišty = zapnúť;
			}

			public void zobrazZvislúLištu(boolean zobraz)
			{
				zobrazVytvorZvislú(zobraz);
				automatickéLišty = false;
			}

			public void zobrazVodorovnúLištu(boolean zobraz)
			{
				zobrazVytvorVodorovnú(zobraz);
				automatickéLišty = false;
			}
		}


	// Nie je možné vytvárať vlastné inštancie plátna.
	/*packagePrivate*/ Plátno()
	{
		vnútornáKonzola = new VnútornáKonzola();

		if (null != vlnenie) odstráňVlnenie();
		if (null != osvetlenie) osvetlenie(false);
		obrázokPlátna = new BufferedImage(
			šírkaPlátna, výškaPlátna, BufferedImage.TYPE_INT_ARGB);
		grafikaPlátna = obrázokPlátna.createGraphics();
		originálPlátna = zálohaPlátna =
			// operáciePlátna =
			null;
	}

	// Vytvorenie nového plátna (pre konštruktory hlavného robota).
	/*packagePrivate*/ void vytvorNovéPlátno(int šírkaPlátna, int výškaPlátna)
	{
		if (null != vlnenie) odstráňVlnenie();
		if (null != osvetlenie) osvetlenie(false);
		obrázokPlátna = new BufferedImage(
			šírkaPlátna, výškaPlátna, BufferedImage.TYPE_INT_ARGB);
		grafikaPlátna = obrázokPlátna.createGraphics();
		originálPlátna = zálohaPlátna =
			// operáciePlátna =
			null;
	}

	// Prekreslenie plátna.
	/*packagePrivate*/ boolean prekresli(Graphics2D grafika)
	{
		if (priehľadnosť > 0)
		{
			if (priehľadnosť < 1)
			{
				Composite záloha = grafika.getComposite();
				grafika.setComposite(
					AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, priehľadnosť));

				if (null != osvetlenie)
				{
					BufferedImage rasterNaOsvetlenie;

					if (null == vlnenie)
						rasterNaOsvetlenie = obrázokPlátna;
					else
						rasterNaOsvetlenie = vlnenie.zvlnenýRaster();

					grafika.drawImage(osvetliRaster(
						rasterNaOsvetlenie), 0, 0, null);
				}
				else if (null == vlnenie)
					grafika.drawImage(obrázokPlátna, 0, 0, null);
				else
					grafika.drawImage(
						vlnenie.zvlnenýRaster(), 0, 0, null);

				grafika.setComposite(záloha);
			}
			else
			{
				if (null != osvetlenie)
				{
					BufferedImage rasterNaOsvetlenie;

					if (null == vlnenie)
						rasterNaOsvetlenie = obrázokPlátna;
					else
						rasterNaOsvetlenie = vlnenie.zvlnenýRaster();

					grafika.drawImage(osvetliRaster(
						rasterNaOsvetlenie), 0, 0, null);
				}
				else if (null == vlnenie)
					grafika.drawImage(obrázokPlátna, 0, 0, null);
				else
					grafika.drawImage(
						vlnenie.zvlnenýRaster(), 0, 0, null);
			}
		}

		if (konzolaPoužitá)
		{
			try
			{
				vnútornáKonzola.prekresli(grafika);
			}
			catch (ConcurrentModificationException e)
			{
				// Texty konzoly plátna sú neprekreslené, ale informácia
				// sa prenáša do metódy Svet.prekresli a ukladá sa do
				// vnútorného atribútu Svet.neboloPrekreslené. Tým sa zvyšuje
				// pravdepodobnosť toho, že texty sa čoskoro opäť prekreslia
				// (a tento raz úspešne).
				// 
				// Toto riešenie bolo zvolené na základe skúsenosti, že táto
				// chyba sa prejavovala väčšinou v režime prekresľovania
				// pomocou časovača (a dlho sa ju nedarilo odhaliť, pretože
				// autor frameworku pozabudol na to, že pred časom
				// implementoval režim ladenia, ktorý bolo treba zapnúť –
				// Svet.režimLadenia(true); bez neho by sa k zdroju chyby
				// nedopracoval).
				// 
				// Uložením informácie o zlyhaní do vyššie uvedeného atribútu
				// sa problém pre tento spôsob prekresľovania vyrieši.
				// Nevyriešil by sa v režime automatického prekresľovania bez
				// časovača, ale tam je zase vysoká pravdepodobnosť toho, že
				// čoskoro po zlyhaní nastane ďalšia udalosť prekreslenia
				// (práve preto zostával tento problém dlho skrytý).

				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				return false;
			}
		}

		return true;
	}


	// Rozmery

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu x-ovú súradnicu plátna.
		 * Podlaha a strop majú určitú veľkosť, ktorá je o niečo väčšia ako
		 * predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najmenšia x-ová súradnica plátna
		 * 
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public static double najmenšieX() { return -šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double najmensieX() { return -šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double minimálneX() { return -šírkaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieX() najmenšieX}.</p> */
		public static double minimalneX() { return -šírkaPlátna / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu y-ovú súradnicu plátna.
		 * Podlaha a strop majú určitú veľkosť, ktorá je o niečo väčšia ako
		 * predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najmenšia y-ová súradnica plátna
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najväčšieY()
		 */
		public static double najmenšieY() { return -(výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double najmensieY() { return -(výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double minimálneY() { return -(výškaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najmenšieY() najmenšieY}.</p> */
		public static double minimalneY() { return -(výškaPlátna - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu x-ovú súradnicu plátna.
		 * Podlaha a strop majú určitú veľkosť, ktorá je o niečo väčšia ako
		 * predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najväčšia x-ová súradnica plátna
		 * 
		 * @see #najmenšieX()
		 * @see #najmenšieY()
		 * @see #najväčšieY()
		 */
		public static double najväčšieX() { return (šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double najvacsieX() { return (šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double maximálneX() { return (šírkaPlátna - 1) / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieX() najväčšieX}.</p> */
		public static double maximalneX() { return (šírkaPlátna - 1) / 2; }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu y-ovú súradnicu plátna.
		 * Podlaha a strop majú určitú veľkosť, ktorá je o niečo väčšia ako
		 * predvolená veľkosť okna aplikácie.</p>
		 * 
		 * @return najväčšia y-ová súradnica plátna
		 * 
		 * @see #najmenšieX()
		 * @see #najväčšieX()
		 * @see #najmenšieY()
		 */
		public static double najväčšieY() { return výškaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double najvacsieY() { return výškaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double maximálneY() { return výškaPlátna / 2; }

		/** <p><a class="alias"></a> Alias pre {@link #najväčšieY() najväčšieY}.</p> */
		public static double maximalneY() { return výškaPlátna / 2; }


		/**
		 * <p>Zistí šírku plátna (podlahy alebo stropu).</p>
		 * 
		 * @return šírka plátna (podlahy alebo stropu)
		 */
		public static int šírka() { return šírkaPlátna; }

		/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
		public static int sirka() { return šírkaPlátna; }

		/**
		 * <p>Zistí výšku plátna (podlahy alebo stropu).</p>
		 * 
		 * @return výška plátna (podlahy alebo stropu)
		 */
		public static int výška() { return výškaPlátna; }

		/** <p><a class="alias"></a> Alias pre {@link #šírka() šírka}.</p> */
		public static int vyska() { return výškaPlátna; }

		/**
		 * <p>Zistí aktuálnu šírku tzv. klientskej oblasti okna. Ide
		 * o viditeľnú časť komponentu okna zahŕňajúceho {@linkplain 
		 * Svet#farbaPlochy() obvykle šedú plochu} s predvolene bielou
		 * kresliacou oblasťou (plátnami) a prípadne ďalšími komponentmi
		 * ({@linkplain Svet#neskrývajVstupnýRiadok(boolean) vstupným
		 * riadkom}, {@linkplain Tlačidlo tlačidlami}…). Ak je okno užšie,
		 * než sú rozmery plátien, tak ide v podstate o viditeľnú šírku
		 * plátna (podlahy alebo stropu).</p>
		 * 
		 * @return šírka klientskej oblasti okna (ak je okno užšie, než
		 *     plátno, tak ide o viditeľnú časť podlahy alebo stropu)
		 */
		public static int viditeľnáŠírka() { return Svet.hlavnýPanel.getWidth(); }

		/** <p><a class="alias"></a> Alias pre {@link #viditeľnáŠírka() viditeľnáŠírka}.</p> */
		public static int viditelnaSirka() { return Svet.hlavnýPanel.getWidth(); }

		/**
		 * <p>Zistí viditeľnú výšku tzv. klientskej oblasti okna. Pozri aj
		 * opis metódy {@link #viditeľnáŠírka() viditeľnáŠírka()} – rovnaké
		 * informácie platia aj pre túto metódu
		 * ({@code currviditeľnáVýška}).</p>
		 * 
		 * @return výška klientskej oblasti okna (ak je okno menšie, než
		 *     výška plátna, tak ide o viditeľnú časť podlahy alebo stropu)
		 */
		public static int viditeľnáVýška() { return Svet.hlavnýPanel.getHeight(); }

		/** <p><a class="alias"></a> Alias pre {@link #viditeľnáVýška() viditeľnáVýška}.</p> */
		public static int viditelnaVyska() { return Svet.hlavnýPanel.getHeight(); }


	// Okraje

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu aktuálne viditeľnú x-ovú
		 * súradnicu na plátne (podlahe alebo strope).</p>
		 * 
		 * @return najmenšia viditeľná x-ová súradnica na plátne
		 * 
		 * @see #pravýOkraj()
		 * @see #spodnýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double ľavýOkraj()
		{
			int súradnica = -Svet.hlavnýPanel.getWidth() / 2;

			if (súradnica < (-šírkaPlátna / 2))
				return -šírkaPlátna / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #ľavýOkraj() ľavýOkraj}.</p> */
		public static double lavyOkraj() { return ľavýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najmenšiu aktuálne viditeľnú y-ovú
		 * súradnicu na plátne (podlahe alebo strope).</p>
		 * 
		 * @return najmenšia viditeľná y-ová súradnica na plátne
		 * 
		 * @see #ľavýOkraj()
		 * @see #pravýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double spodnýOkraj()
		{
			int súradnica = -(Svet.hlavnýPanel.getHeight() - 1) / 2;

			if (súradnica < (-(výškaPlátna - 1) / 2))
				return -(výškaPlátna - 1) / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #spodnýOkraj() spodnýOkraj}.</p> */
		public static double spodnyOkraj() { return spodnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #spodnýOkraj() spodnýOkraj}.</p> */
		public static double dolnýOkraj() { return spodnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #dolnýOkraj() dolnýOkraj}.</p> */
		public static double dolnyOkraj() { return spodnýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu aktuálne viditeľnú x-ovú súradnicu na
		 * plátne (podlahe alebo strope).</p>
		 * 
		 * @return najväčšia viditeľná x-ová súradnica na plátne
		 * 
		 * @see #ľavýOkraj()
		 * @see #spodnýOkraj()
		 * @see #vrchnýOkraj()
		 */
		public static double pravýOkraj()
		{
			int súradnica = (Svet.hlavnýPanel.getWidth() - 1) / 2;

			if (súradnica > ((šírkaPlátna - 1) / 2))
				return (šírkaPlátna - 1) / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #pravýOkraj() pravýOkraj}.</p> */
		public static double pravyOkraj() { return pravýOkraj(); }

		/**
		 * <p><a class="getter"></a> Zistí najväčšiu aktuálne viditeľnú y-ovú súradnicu na
		 * plátne (podlahe alebo strope).</p>
		 * 
		 * @return najväčšia viditeľná y-ová súradnica na plátne
		 * 
		 * @see #ľavýOkraj()
		 * @see #pravýOkraj()
		 * @see #spodnýOkraj()
		 */
		public static double vrchnýOkraj()
		{
			int súradnica = Svet.hlavnýPanel.getHeight() / 2;

			if (súradnica > (výškaPlátna / 2))
				return výškaPlátna / 2;

			return súradnica;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vrchnýOkraj() vrchnýOkraj}.</p> */
		public static double vrchnyOkraj() { return vrchnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #vrchnýOkraj() vrchnýOkraj}.</p> */
		public static double hornýOkraj() { return vrchnýOkraj(); }

		/** <p><a class="alias"></a> Alias pre {@link #hornýOkraj() hornýOkraj}.</p> */
		public static double hornyOkraj() { return vrchnýOkraj(); }


	// Záloha a obnovenie

		/**
		 * <p>Zálohuje kresbu na plátne (podlahe alebo strope), aby mohla byť
		 * v prípade potreby obnovená metódou {@link #obnovGrafiku()
		 * obnovGrafiku}.</p>
		 * 
		 * @return objekt obsahujúci zálohu kresby na plátne
		 * 
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public Object zálohujGrafiku()
		{
			if (null == originálPlátna)
				originálPlátna = ((DataBufferInt)
					obrázokPlátna.getRaster().
						getDataBuffer()).getData();

			zálohaPlátna = new int[originálPlátna.length];

			System.arraycopy(originálPlátna, 0,
				zálohaPlátna, 0, originálPlátna.length);

			return zálohaPlátna;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujGrafiku() zálohujGrafiku}.</p> */
		public Object zalohujGrafiku() { return zálohujGrafiku(); }

		/**
		 * <p>Zálohuje kresbu plátna (podlahy alebo stropu) do pamäte zadaného
		 * robota. Obsah môže byť v prípade potreby obnovený metódou {@link 
		 * #obnovGrafiku(Object) obnovGrafiku(Object)} alebo pridaný
		 * k aktuálnemu obsahu kresby na plátne metódou {@link 
		 * #pridajGrafiku(GRobot) pridajGrafiku(GRobot)}.</p>
		 * 
		 * @param ktorý ktorý robot si má zapamätať kresbu na plátne
		 * 
		 * @see #zálohujGrafiku()
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void zálohujGrafiku(GRobot ktorý)
		{
			if (null == originálPlátna)
				originálPlátna = ((DataBufferInt)obrázokPlátna.
					getRaster().getDataBuffer()).getData();

			if (null == ktorý.zálohaPlátna ||
				ktorý.zálohaPlátna.length != originálPlátna.length)
			{
				ktorý.obrázokZálohyPlátna = new BufferedImage(šírkaPlátna,
					výškaPlátna, BufferedImage.TYPE_INT_ARGB);
				ktorý.zálohaPlátna = ((DataBufferInt)ktorý.obrázokZálohyPlátna.
					getRaster().getDataBuffer()).getData();
			}

			System.arraycopy(originálPlátna, 0,
				ktorý.zálohaPlátna, 0, originálPlátna.length);
			// return obrázokPlátna.getData();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujGrafiku(GRobot) zálohujGrafiku}.</p> */
		public void zalohujGrafiku(GRobot ktorý) { zálohujGrafiku(ktorý); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujGrafiku(GRobot) zálohujGrafiku}.</p> */
		public void zapamätajGrafiku(GRobot ktorý) { zálohujGrafiku(ktorý); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujGrafiku(GRobot) zálohujGrafiku}.</p> */
		public void zapamatajGrafiku(GRobot ktorý) { zálohujGrafiku(ktorý); }

		/**
		 * <p>Obnoví kresbu plátna (podlahy alebo stropu), ktorá bol zálohovaná
		 * metódou {@link #zálohujGrafiku() zálohujGrafiku}. Ak kresba predtým
		 * nebola zálohovaná, volanie tejto metódy nebude mať žiadny efekt.</p>
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void obnovGrafiku()
		{
			if (null == zálohaPlátna || null == originálPlátna) return;
			System.arraycopy(zálohaPlátna, 0,
				originálPlátna, 0, originálPlátna.length);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/**
		 * <p>Obnoví kresbu plátna (podlahy alebo stropu), ktorá bola
		 * zálohovaná buď metódou {@link #zálohujGrafiku() zálohujGrafiku()},
		 * alebo metódou {@link #zálohujGrafiku(GRobot) zálohujGrafiku(GRobot)}.
		 * Ak zadaný objekt neobsahuje zálohu, tak volanie tejto metódy nebude
		 * mať žiadny efekt.</p>
		 * 
		 * @param záloha záloha plátna vytvorená buď metódou {@link 
		 *     #zálohujGrafiku() zálohujGrafiku()}, alebo metódou {@link 
		 *     #zálohujGrafiku(GRobot) zálohujGrafiku(GRobot)} (čiže buď objekt,
		 *     ktorý bol výstupom metódy {@link #zálohujGrafiku()
		 *     zálohujGrafiku()}, alebo robot, ktorý bol použitý na zálohovanie
		 *     metódou {@link #zálohujGrafiku(GRobot) zálohujGrafiku(GRobot)})
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void obnovGrafiku(Object záloha)
		{
			if (null != záloha && null != originálPlátna)
			{
				int[] obnova = null;

				if (záloha instanceof int[])
					obnova = (int[])záloha;
				else if (záloha instanceof GRobot &&
					null != ((GRobot)záloha).zálohaPlátna)
					obnova = ((GRobot)záloha).zálohaPlátna;

				if (null != obnova && obnova.length == originálPlátna.length)
				{
					System.arraycopy(obnova, 0,
						originálPlátna, 0, originálPlátna.length);
					if (!Svet.právePrekresľujem)
						Svet.automatickéPrekreslenie();
				}
			}
		}

		/**
		 * <p>Nakreslí obsah zadanej zálohy plátna vytvorenej metódou
		 * {@link #zálohujGrafiku(GRobot) zálohujGrafiku(GRobot)}) cez
		 * jestvujúcu kresbu na plátne (podlahe alebo strope). Táto metóda
		 * s využitím priehľadnosti dokreslí kresbu zo zálohy ku kresbe, ktorá
		 * sa na plátne práve nachádza.</p>
		 * 
		 * <p>Testy ukázali, že proces dokreslenia je približne rovnako rýchly
		 * ako proces {@linkplain #obnovGrafiku(Object) obnovenia} zo zálohy.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri použití metódy {@link 
		 * #obnovGrafiku(Object) obnovGrafiku(Object)} je kresba plátna úplne
		 * nahradená obsahom zálohy a na rozdiel od tejto metódy sú pri nej
		 * použiteľné aj objekty vytvárané metódou {@link #zálohujGrafiku()
		 * zálohujGrafiku()}, ktorých štruktúra je jednoduchšia.</p>
		 * 
		 * @param ktorý robot, ktorý obsahuje zálohu plátna vytvorenú
		 *     metódou {@link #zálohujGrafiku(GRobot) zálohujGrafiku(GRobot)}
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void pridajGrafiku(GRobot ktorý)
		{
			if (null == ktorý.obrázokZálohyPlátna) return;
			grafikaPlátna.drawImage(ktorý.obrázokZálohyPlátna, 0, 0, null);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}


		/**
		 * <p>Táto metóda vytvorí zálohu textového obsahu vnútornej konzoly tohto
		 * plátna, uloží si ju do vnútornej pamäte a zároveň vráti vo forme
		 * objektu. Vrátený objekt je použiteľný s metódami
		 * {@link #obnovTexty(Object) obnovTexty}
		 * a {@link #pridajTexty(Object) pridajTexty(Object)}.</p>
		 * 
		 * @return záloha, ktorá je použiteľná s metódami
		 *     {@link #obnovTexty(Object) obnovTexty}
		 *     a {@link #pridajTexty(Object) pridajTexty(Object)}
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public Object zálohujTexty()
		{
			vnútornáKonzola.záloha = new ZálohaKonzoly();
			vnútornáKonzola.záloha.záloha.addAll(vnútornáKonzola.riadky);
			return vnútornáKonzola.záloha;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty() zálohujTexty}.</p> */
		public Object zalohujTexty() { return zálohujTexty(); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty() zálohujTexty}.</p> */
		public Object zapamätajTexty() { return zálohujTexty(); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty() zálohujTexty}.</p> */
		public Object zapamatajTexty() { return zálohujTexty(); }


		/**
		 * <p>Táto metóda vytvorí zálohu textového obsahu vnútornej konzoly tohto
		 * plátna s použitím vnútornej pamäte zadaného robota. Obsah môže byť
		 * v prípade potreby obnovený metódou {@link #obnovTexty(Object)
		 * obnovTexty(Object)} alebo pridaný k aktuálnym textom na plátne
		 * metódou {@link #pridajTexty(Object) pridajTexty(Object)}.</p>
		 * 
		 * @param ktorý robot, do ktorého bude uložená záloha textov
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void zálohujTexty(GRobot ktorý)
		{
			if (null == ktorý) return;
			if (null == ktorý.zálohaKonzoly)
				ktorý.zálohaKonzoly = new ZálohaKonzoly();
			else
				ktorý.zálohaKonzoly.záloha.clear();
			ktorý.zálohaKonzoly.záloha.addAll(vnútornáKonzola.riadky);
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty(GRobot) zálohujTexty}.</p> */
		public void zalohujTexty(GRobot ktorý) { zálohujTexty(ktorý); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty(GRobot) zálohujTexty}.</p> */
		public void zapamätajTexty(GRobot ktorý) { zálohujTexty(ktorý); }

		/** <p><a class="alias"></a> Alias pre {@link #zálohujTexty(GRobot) zálohujTexty}.</p> */
		public void zapamatajTexty(GRobot ktorý) { zálohujTexty(ktorý); }


		/**
		 * <p>Táto metóda obnoví stav textového obsahu vnútornej konzoly, ktorý
		 * bol uložený metódou {@link #zálohujTexty() zálohujTexty}. Ak záloha
		 * predtým nebola vytvorená, tak volanie tejto metódy nebude mať
		 * žiadny efekt.</p>
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void obnovTexty()
		{
			if (null != vnútornáKonzola.záloha)
			{
				vnútornáKonzola.riadky.clear();
				vnútornáKonzola.riadky.addAll(vnútornáKonzola.záloha.záloha);
				if (!Svet.právePrekresľujem)
					Svet.automatickéPrekreslenie(); // noInvokeLater
			}
		}

		/**
		 * <p>Táto metóda obnoví stav textového obsahu vnútornej konzoly
		 * tohto plátna podľa objektu, ktorý bol vytvorený niektorou
		 * z metód {@link #zálohujTexty() zálohujTexty()},
		 * {@link #zálohujTexty(GRobot) zálohujTexty(GRobot)},
		 * {@link #zálohujChvost(int) zálohujChvost(int)} alebo
		 * {@link #zálohujChvost(int, GRobot) zálohujChvost(int, GRobot)}
		 * (<small>prípadne inou metódou, ktorá mohla byť do programovacieho
		 * rámca pridaná neskôr</small>).</p>
		 * 
		 * @param záloha objekt, ktorý obsahuje zálohu textov konzoly
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #pridajTexty(Object)
		 */
		public void obnovTexty(Object záloha)
		{
			if (null == záloha) return;

			ZálohaKonzoly obnova = null;

			if (záloha instanceof ZálohaKonzoly)
				obnova = (ZálohaKonzoly)záloha;
			else if (záloha instanceof GRobot &&
				null != ((GRobot)záloha).zálohaKonzoly)
				obnova = ((GRobot)záloha).zálohaKonzoly;

			if (null != obnova)
			{
				vnútornáKonzola.riadky.clear();
				vnútornáKonzola.riadky.addAll(obnova.záloha);
				if (!Svet.právePrekresľujem)
					Svet.automatickéPrekreslenie(); // noInvokeLater
			}
		}

		/**
		 * <p>Táto metóda pridá k aktuálnemu obsahu vnútornej konzoly
		 * tohto plátna obsah zo zadanej zálohy konzoly, ktorá bola
		 * vytvorená niektorou z metód {@link #zálohujTexty()
		 * zálohujTexty()}, {@link #zálohujTexty(GRobot)
		 * zálohujTexty(GRobot)}, {@link #zálohujChvost(int)
		 * zálohujChvost(int)} alebo {@link #zálohujChvost(int, GRobot)
		 * zálohujChvost(int, GRobot)} (<small>prípadne inou metódou,
		 * ktorá mohla byť do programovacieho rámca pridaná neskôr</small>).</p>
		 * 
		 * @param záloha objekt, ktorý obsahuje zálohu textov konzoly
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 */
		public void pridajTexty(Object záloha)
		{
			if (null == záloha) return;

			ZálohaKonzoly obnova = null;

			if (záloha instanceof ZálohaKonzoly)
				obnova = (ZálohaKonzoly)záloha;
			else if (záloha instanceof GRobot &&
				null != ((GRobot)záloha).zálohaKonzoly)
				obnova = ((GRobot)záloha).zálohaKonzoly;

			if (null != obnova)
			{
				vnútornáKonzola.riadky.addAll(obnova.záloha);
				if (!Svet.právePrekresľujem)
					Svet.automatickéPrekreslenie(); // noInvokeLater
			}
		}


	// Kreslenie tvarov

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru na
		 * plátno. Metóda potrebuje na svoje správne fungovanie robota
		 * „kresliča,“ ktorého farbu alebo náter a štýl čiary použije
		 * na kreslenie. Ak je do metódy namiesto konkrétneho kresliča
		 * zadaná hodnota {@code valnull}, tak je na získanie parametrov
		 * kreslenia použitý {@linkplain Svet#hlavnýRobot() hlavný robot}
		 * (ak ten nejestvuje, kreslenie nebude vykonané).</p>
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
		public void kresli(Shape tvar, GRobot kreslič)
		{
			if (null == kreslič)
				kreslič = Svet.hlavnýRobot;

			if (null == kreslič) return;

			kreslič.nastavVlastnostiGrafiky(grafikaPlátna);
			kreslič.nastavFarbuAleboVýplňPodľaRobota(grafikaPlátna);
			grafikaPlátna.setStroke(kreslič.čiara);
			grafikaPlátna.draw(tvar);
			kreslič.obnovVlastnostiGrafiky(grafikaPlátna);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov na plátno.
		 * Metóda potrebuje na svoje správne fungovanie robota „kresliča,“
		 * ktorého farbu alebo náter použije na vyplnenie zadaného tvaru.
		 * Ak je do metódy namiesto konkrétneho kresliča zadaná hodnota
		 * {@code valnull}, tak je na získanie parametrov kreslenia použitý
		 * {@linkplain Svet#hlavnýRobot() hlavný robot} (ak ten nejestvuje,
		 * kreslenie nebude vykonané).</p>
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
		public void vyplň(Shape tvar, GRobot kreslič)
		{
			if (null == kreslič)
				kreslič = Svet.hlavnýRobot;

			if (null == kreslič) return;

			kreslič.nastavVlastnostiGrafiky(grafikaPlátna);
			kreslič.nastavFarbuAleboVýplňPodľaRobota(grafikaPlátna);
			grafikaPlátna.fill(tvar);
			kreslič.obnovVlastnostiGrafiky(grafikaPlátna);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape, GRobot) vyplň}.</p> */
		public void vypln(Shape tvar, GRobot kreslič) { vyplň(tvar, kreslič); }

		/**
		 * <p>Táto metóda slúži na kreslenie obrysov zadaného tvaru na plátno.
		 * Metóda používa na získanie parametrov kreslenia (štýlu a farby
		 * čiary alebo náteru) {@linkplain Svet#hlavnýRobot() hlavného
		 * robota} (ak nejestvuje, kreslenie nebude vykonané).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude
		 *     nakreslený aktuálnym štýlom čiary a farbou {@linkplain 
		 *     Svet#hlavnýRobot() hlavného robota}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #vyplň(Shape, GRobot)
		 * @see #vyplň(Shape)
		 */
		public void kresli(Shape tvar)
		{
			if (null == Svet.hlavnýRobot) return;

			Svet.hlavnýRobot.nastavVlastnostiGrafiky(grafikaPlátna);
			Svet.hlavnýRobot.nastavFarbuAleboVýplňPodľaRobota(grafikaPlátna);
			grafikaPlátna.setStroke(Svet.hlavnýRobot.čiara);
			grafikaPlátna.draw(tvar);
			Svet.hlavnýRobot.obnovVlastnostiGrafiky(grafikaPlátna);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/**
		 * <p>Táto metóda slúži na kreslenie vyplnených tvarov na plátno.
		 * Metóda používa na získanie parametrov kreslenia (farby výplne
		 * alebo náteru) {@linkplain Svet#hlavnýRobot() hlavného robota}
		 * (ak nejestvuje, kreslenie nebude vykonané).</p>
		 * 
		 * @param tvar objekt typu {@link Shape Shape}, ktorý bude vyplnený
		 *     farbou {@linkplain Svet#hlavnýRobot() hlavného robota}
		 * 
		 * @see #kresli(Shape, GRobot)
		 * @see #kresli(Shape)
		 * @see #vyplň(Shape, GRobot)
		 */
		public void vyplň(Shape tvar)
		{
			if (null == Svet.hlavnýRobot) return;

			Svet.hlavnýRobot.nastavVlastnostiGrafiky(grafikaPlátna);
			Svet.hlavnýRobot.nastavFarbuAleboVýplňPodľaRobota(grafikaPlátna);
			grafikaPlátna.fill(tvar);
			Svet.hlavnýRobot.obnovVlastnostiGrafiky(grafikaPlátna);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Shape) vyplň}.</p> */
		public void vypln(Shape tvar) { vyplň(tvar); }


	// Nastavenie farby bodu

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu plátna na
		 * zadanej pozícii zadanou farbou.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param farba objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(double x, double y, Color farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + x),
					(int)((výškaPlátna / 2.0) - y),
					farba.getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu plátna na
		 * zadanej pozícii farbou zadaného objektu.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param objekt objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(double x, double y, Farebnosť objekt)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + x),
					(int)((výškaPlátna / 2.0) - y),
					objekt.farba().getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti bodu plátna na
		 * zadanej pozícii zadanou kombináciou zložiek ARGB zakódovaných
		 * v celočíselnej hodnote.</p>
		 * 
		 * @param x x-ová súradnica bodu v súradnicovom priestore rámca
		 * @param y y-ová súradnica bodu v súradnicovom priestore rámca
		 * @param farba celé číslo obsahujúce kombináciu farebných zložiek
		 *     RGB a priehľadnosti
		 */
		public void prepíšBod(double x, double y, int farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + x),
					(int)((výškaPlátna / 2.0) - y),
					farba);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, Color) prepíšBod}.</p> */
		public void prepisBod(double x, double y, Color farba)
		{ prepíšBod(x, y, farba); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, Farebnosť) prepíšBod}.</p> */
		public void prepisBod(double x, double y, Farebnosť objekt)
		{ prepíšBod(x, y, objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(double, double, int) prepíšBod}.</p> */
		public void prepisBod(double x, double y, int farba)
		{ prepíšBod(x, y, farba); }


		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu na plátne zadanou farbou.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(Poloha bod, Color farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + bod.polohaX()),
					(int)((výškaPlátna / 2.0) - bod.polohaY()),
					farba.getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu farbou zadaného objektu.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param objekt objekt určujúci novú farbu bodu
		 */
		public void prepíšBod(Poloha bod, Farebnosť objekt)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + bod.polohaX()),
					(int)((výškaPlátna / 2.0) - bod.polohaY()),
					objekt.farba().getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu na plátne zadanou kombináciou zložiek ARGB
		 * zakódovaných v celočíselnej hodnote.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba celé číslo obsahujúce kombináciu farebných zložiek
		 *     RGB a priehľadnosti
		 */
		public void prepíšBod(Poloha bod, int farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + bod.polohaX()),
					(int)((výškaPlátna / 2.0) - bod.polohaY()),
					farba);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, Color) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, Color farba)
		{ prepíšBod(bod, farba); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, Farebnosť) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, Farebnosť objekt)
		{ prepíšBod(bod, objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBod(Poloha, int) prepíšBod}.</p> */
		public void prepisBod(Poloha bod, int farba)
		{ prepíšBod(bod, farba); }


		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu na plátne zadanou farbou.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba objekt určujúci novú farbu bodu
		 */
		public void prepíšBodNaMyši(Color farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + ÚdajeUdalostí.súradnicaMyšiX),
					(int)((výškaPlátna / 2.0) - ÚdajeUdalostí.súradnicaMyšiY),
					farba.getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu farbou zadaného objektu.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param objekt objekt určujúci novú farbu bodu
		 */
		public void prepíšBodNaMyši(Farebnosť objekt)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + ÚdajeUdalostí.súradnicaMyšiX),
					(int)((výškaPlátna / 2.0) - ÚdajeUdalostí.súradnicaMyšiY),
					objekt.farba().getRGB());
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/**
		 * <p>Prepíše farebné zložky a zložku priehľadnosti na pozícii
		 * zadaného bodu na plátne zadanou kombináciou zložiek ARGB
		 * zakódovaných v celočíselnej hodnote.</p>
		 * 
		 * @param bod objekt reprezentujúci súradnice bodu (v súradnicovom
		 *     priestore robota)
		 * @param farba celé číslo obsahujúce kombináciu farebných zložiek
		 *     RGB a priehľadnosti
		 */
		public void prepíšBodNaMyši(int farba)
		{
			try
			{
				obrázokPlátna.setRGB(
					(int)((šírkaPlátna / 2.0) + ÚdajeUdalostí.súradnicaMyšiX),
					(int)((výškaPlátna / 2.0) - ÚdajeUdalostí.súradnicaMyšiY),
					farba);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				// Túto chybu zamlčíme – prejdeme to potichu…
				// (Jednoducho bod nebude nastavený a hotovo.)
				// e.printStackTrace()
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBodNaMyši(Color) prepíšBodNaMyši}.</p> */
		public void prepisBodNaMysi(Color farba)
		{ prepíšBodNaMyši(farba); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBodNaMyši(Farebnosť) prepíšBodNaMyši}.</p> */
		public void prepisBodNaMysi(Farebnosť objekt)
		{ prepíšBodNaMyši(objekt); }

		/** <p><a class="alias"></a> Alias pre {@link #prepíšBodNaMyši(int) prepíšBodNaMyši}.</p> */
		public void prepisBodNaMysi(int farba)
		{ prepíšBodNaMyši(farba); }


	// Farba bodu

		/**
		 * <p>Zistí farbu bodu (jedného pixela) na zadaných súradniciach.
		 * Ak sa zadané súradnice nachádzajú mimo plochy plátna, je vrátená
		 * inštancia farebnosti {@link Farebnosť#žiadna žiadna}. So získanou
		 * farbou môžeme ďalej pracovať – napríklad ju upravovať alebo
		 * zisťovať jej vlastnosti (farebné zložky…). Testovať, či má bod
		 * konkrétnu farbu, môžeme napríklad pomocou metódy
		 * {@link #farbaBodu(double, double,
		 * Color) farbaBodu(x, y, farba)}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na zadanej
		 *     pozícii alebo inštancia {@link Farebnosť#žiadna žiadna}, ak sú
		 *     zadané súradnice mimo rozmerov plátna
		 */
		public Farba farbaBodu(double x, double y)
		{
			int x0 = (int)Svet.prepočítajX(x);
			int y0 = (int)Svet.prepočítajY(y);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return Farebnosť.žiadna;

			return new Farba(obrázokPlátna.getRGB(x0, y0), true);
		}

		/**
		 * <p>Zistí farbu bodu (jedného pixela) plátna na pozícii zadaného
		 * objektu. Ak sa objekt nachádza mimo plochy plátna, je vrátená
		 * inštancia farebnosti {@link Farebnosť#žiadna žiadna}. So získanou
		 * farbou môžeme ďalej pracovať – napríklad ju upravovať alebo
		 * zisťovať jej vlastnosti (farebné zložky…). Testovať, či má bod
		 * konkrétnu farbu, môžeme napríklad pomocou metódy
		 * {@link #farbaBodu(Poloha, Color)
		 * farbaBodu(objekt, farba)}.</p>
		 * 
		 * @param objekt objekt, na ktorého pozícii má byť zistená farba bodu
		 * @return farba bodu (objekt typu {@link Farba Farba}) na pozícii
		 *     zadaného objektu alebo inštancia {@link Farebnosť#žiadna žiadna},
		 *     ak sa zadaný objekt nachádza mimo rozmerov plátna
		 */
		public Farba farbaBodu(Poloha objekt)
		{
			int x0 = (int)Svet.prepočítajX(objekt.polohaX());
			int y0 = (int)Svet.prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return žiadna;

			return new Farba(obrázokPlátna.getRGB(x0, y0), true);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje so zadanou farbou. Ak sú zadané súradnice mimo plochy
		 * plátna, je vrátená hodnota {@code valfalse}. Testovať farbu
		 * pomocou tejto metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@code currfarbaBodu}({@code num3.0}, {@code num5.0}, {@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na
		 *     zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     plátna a farba bodu na zadaných súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, Color farba)
		{
			int x0 = (int)Svet.prepočítajX(x);
			int y0 = (int)Svet.prepočítajY(y);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (farba.getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach zadaného
		 * objektu zhoduje so zadanou farbou. Ak sú zadané súradnice mimo plochy
		 * plátna, je vrátená hodnota {@code valfalse}. Testovať farbu
		 * pomocou tejto metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@code currfarbaBodu}({@code valthis}, {@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * @param objekt objekt, na ktorého pozícii chceme overiť farbu bodu
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na
		 *     pozícii určeného objektu
		 * @return {@code valtrue} ak sa objekt nachádza v rámci rozmerov
		 *     plátna a farba bodu na jeho mieste sa zhoduje so zadanou
		 *     farbou (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, Color farba)
		{
			int x0 = (int)Svet.prepočítajX(objekt.polohaX());
			int y0 = (int)Svet.prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (farba.getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na zadaných súradniciach
		 * zhoduje s farbou zadaného objektu. Ak sú zadané súradnice mimo
		 * plochy plátna, je vrátená hodnota {@code valfalse}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @param objekt objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na zadanej pozícii
		 * @return {@code valtrue} ak sú zadané súradnice v rámci rozmerov
		 *     plátna a farba bodu na zadaných súradniciach sa zhoduje
		 *     s farbou zadaného objektu farbou (musia sa zhodovať všetky
		 *     farebné zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, Farebnosť objekt)
		{
			int x0 = (int)Svet.prepočítajX(x);
			int y0 = (int)Svet.prepočítajY(y);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (objekt.farba().getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach zadaného
		 * objektu zhoduje s farbou zadaného objektu. Ak sú zadané súradnice
		 * mimo plochy plátna, je vrátená hodnota {@code valfalse}.</p>
		 * 
		 * @param objekt objekt, na ktorého pozícii chceme overiť farbu bodu
		 * @param farebnosť objekt, ktorého farbu chceme porovnať s farbou
		 *     bodu na pozícii určenej predchádzajúcim objektom
		 * @return {@code valtrue} ak sa objekt nachádza v rámci rozmerov
		 *     plátna a farba bodu na jeho mieste sa zhoduje s farbou
		 *     zadaného objektu farbou (musia sa zhodovať všetky farebné
		 *     zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, Farebnosť farebnosť)
		{
			int x0 = (int)Svet.prepočítajX(objekt.polohaX());
			int y0 = (int)Svet.prepočítajY(objekt.polohaY());
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (farebnosť.farba().getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
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
		 *     plátna a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, int r, int g, int b)
		{
			int x0 = (int)Svet.prepočítajX(x);
			int y0 = (int)Svet.prepočítajY(y);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
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
		 *     plátna a farba bodu na zadaných súradniciach sa zhoduje
		 *     so zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(double x, double y, int r, int g, int b, int a)
		{
			int x0 = (int)Svet.prepočítajX(x);
			int y0 = (int)Svet.prepočítajY(y);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
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
		 *     plátna a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, int r, int g, int b)
		{
			int x0 = (int)((šírkaPlátna / 2.0) + objekt.polohaX());
			int y0 = (int)((výškaPlátna / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
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
		 *     plátna a farba bodu na jeho súradniciach sa zhoduje so
		 *     zadanou farbou (musia sa zhodovať všetky farebné zložky aj
		 *     úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaBodu(Poloha objekt, int r, int g, int b, int a)
		{
			int x0 = (int)((šírkaPlátna / 2.0) + objekt.polohaX());
			int y0 = (int)((výškaPlátna / 2.0) - objekt.polohaY());
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}


		/**
		 * <p>Zistí farbu bodu (jedného pixela) na súradniciach myši. So
		 * získanou farbou môžeme ďalej pracovať – napríklad ju upravovať
		 * alebo zisťovať jej vlastnosti (farebné zložky…). Testovať, či má
		 * bod konkrétnu farbu, môžeme napríklad pomocou metódy {@link 
		 * #farbaNaMyši(Color) farbaNaMyši(farba)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy plátna, metóda by vrátila inštanciu farby {@link 
		 * Farebnosť#žiadna žiadna}.</p>
		 * 
		 * @return farba bodu (objekt typu {@link Farba Farba}) na pozícii
		 *     myši
		 */
		public Farba farbaNaMyši()
		{
			int x0 = (int)Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return žiadna;

			return new Farba(obrázokPlátna.getRGB(x0, y0), true);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši() farbaNaMyši}.</p> */
		public Farba farbaNaMysi() { return farbaNaMyši(); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje so zadanou farbou. Testovať farbu pomocou tejto
		 * metódy môžeme napríklad takto:</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdif} ({@code currfarbaNaMyši}({@link Farebnosť#modrá modrá})) …
			</pre>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy plátna, metóda by vrátila hodnotu {@code 
		 * valfalse}.</p>
		 * 
		 * @param farba farba, ktorú chceme porovnať s farbou bodu na
		 *     súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná so zadanou farbou (musia sa zhodovať všetky
		 *     farebné zložky aj úroveň priehľadnosti), inak {@code valfalse}
		 */
		public boolean farbaNaMyši(Color farba)
		{
			int x0 = (int)Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (farba.getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(Color) farbaNaMyši}.</p> */
		public boolean farbaNaMysi(Color farba) { return farbaNaMyši(farba); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje s farbou zadaného objektu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy plátna, metóda by vrátila hodnotu {@code 
		 * valfalse}.</p>
		 * 
		 * @param objekt objekt, ktorého farbu chceme porovnať s farbou bodu
		 *     na súradniciach myši
		 * @return {@code valtrue} ak je farba bodu na súradniciach myši
		 *     zhodná s farbou zadaného objektu farbou (musia sa zhodovať
		 *     všetky farebné zložky aj úroveň priehľadnosti),
		 *     inak {@code valfalse}
		 */
		public boolean farbaNaMyši(Farebnosť objekt)
		{
			int x0 = (int)Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			return (objekt.farba().getRGB() & 0xffffffff) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(Farebnosť) farbaNaMyši}.</p> */
		public boolean farbaNaMysi(Farebnosť objekt) { return farbaNaMyši(objekt); }

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
		public boolean farbaNaMyši(int r, int g, int b)
		{
			int x0 = (int)Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff;
			return (0xff000000 | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(int, int, int) farbaNaMyši}.</p> */
		public boolean farbaNaMysi(int r, int g, int b) { return farbaNaMyši(r, g, b); }

		/**
		 * <p>Zistí, či sa farba bodu (jedného pixela) na súradniciach myši
		 * zhoduje s farbou zadanou prostredníctvom farebných zložiek
		 * a úrovne priehľadnosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by súradnice myši boli náhodou
		 * mimo plochy plátna, metóda by vrátila hodnotu {@code valfalse}.</p>
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
		public boolean farbaNaMyši(int r, int g, int b, int a)
		{
			int x0 = (int)Svet.prepočítajX(ÚdajeUdalostí.súradnicaMyšiX);
			int y0 = (int)Svet.prepočítajY(ÚdajeUdalostí.súradnicaMyšiY);
			if (x0 < 0 || x0 >= šírkaPlátna ||
				y0 < 0 || y0 >= výškaPlátna) return false;

			r &= 0xff; g &= 0xff; b &= 0xff; a &= 0xff;
			return ((a << 24) | (r << 16) | (g << 8) | b) ==
				(obrázokPlátna.getRGB(x0, y0) & 0xffffffff);
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaNaMyši(int, int, int, int) farbaNaMyši}.</p> */
		public boolean farbaNaMysi(int r, int g, int b, int a) { return farbaNaMyši(r, g, b, a); }


	// Farba textu

		/**
		 * <p><a class="getter"></a> Zistí aktuálnu farbu výpisu nových textov
		 * vnútornej konzoly plátna (podlahy alebo stropu).</p>
		 * 
		 * @return aktuálna farba textov konzoly plátna (objekt typu
		 *     {@link Farba Farba})
		 */
		public Farba farbaTextu() { return vnútornáKonzola.farba(); }

		/**
		 * <p><a class="setter"></a> Nastav farbu textov vnútornej konzoly
		 * plátna (podlahy alebo stropu). Nastaví farbu a priehľadnosť textov
		 * konzoly podlahy alebo stropu podľa zadaného objektu.</p>
		 * 
		 * @param nováFarba objekt typu {@link Color Color} (alebo
		 *     odvodeného napr. {@link Farba Farba}) s novou farbou textov
		 *     plátna; jestvuje paleta predvolených farieb (pozri napr.:
		 *     {@link Farebnosť#biela biela}, {@link Farebnosť#červená
		 *     červená}, {@link Farebnosť#čierna
		 *     čierna}…)
		 */
		public void farbaTextu(Color nováFarba)
		{
			if (nováFarba instanceof Farba)
				vnútornáKonzola.zmenaFarby = (Farba)nováFarba;
			else
				vnútornáKonzola.zmenaFarby = new Farba(nováFarba);
		}

		/**
		 * <p>Nastav farbu textov plátna (podlahy alebo stropu) podľa farby
		 * zadaného objektu. Nastaví farbu a priehľadnosť textov podlahy alebo
		 * stropu podľa zadaného objektu.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu textov plátna
		 */
		public void farbaTextu(Farebnosť objekt)
		{ farbaTextu(objekt.farba()); }

		/* *
		 * <p>Toto je „klon“ metódy {@link #farbaTextu(Color nováFarba)}.
		 * Farba je nastavená len v prípade, že v premennej typu {@link Object}
		 * (zadanej ako parameter) je uložená inštancia triedy {@link Farba
		 * Farba} alebo {@link Color Color}.</p>
		 * /
		public void farbaTextu(Object nováFarba)
		{
			if (nováFarba instanceof Color)
				nováFarba = new Farba((Color)nováFarba);
			if (nováFarba instanceof Farba) farbaTextu((Farba)nováFarba);
		}
		*/

		/**
		 * <p>Nastav farbu textov plátna (podlahy alebo stropu). Nastaví farbu
		 * textov vnútornej konzoly podlahy alebo stropu podľa zadaných
		 * farebných zložiek.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba textov
		 * 
		 * @see #farbaTextu(Color)
		 */
		public Farba farbaTextu(int r, int g, int b)
		{ return vnútornáKonzola.zmenaFarby = new Farba(r, g, b); }

		/**
		 * <p>Nastav farbu textov plátna (podlahy alebo stropu). Nastaví farbu
		 * a (ne)priehľadnosť textov vnútornej konzoly podlahy alebo stropu
		 * podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
		 *     v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} – nová farba textov
		 * 
		 * @see #farbaTextu(Color)
		 */
		public Farba farbaTextu(int r, int g, int b, int a)
		{ return vnútornáKonzola.zmenaFarby = new Farba(r, g, b, a); }

		/**
		 * <p>Zmení farbu textov vnútornej konzoly plátna (podlahy alebo stropu)
		 * na predvolenú.</p>
		 */
		public void predvolenáFarbaTextu()
		{
			if (konzolaPoužitá)
				vnútornáKonzola.zmenaFarby = predvolenáFarbaKonzoly;
			else
				vnútornáKonzola.počiatočnáFarba = predvolenáFarbaKonzoly;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextu() predvolenáFarbaTextu}.</p> */
		public void predvolenaFarbaTextu() { predvolenáFarbaTextu(); }


	// Zmena správania metód výpisov textov konzoly pri prijatí inštancie farby

		/**
		 * <p>Zistí, ako sa bude správať vnútorná konzola toho plátna pri
		 * pokuse o vypísanie inštancie farby. Ak je toto nastavenie aktívne
		 * ({@code valtrue}), tak pri prijatí inštancií farieb upraví farebnosť
		 * textov a pozadia vypisovaných textov namiesto výpisu informácií
		 * o zadaných inštanciách farieb. Podrobnosti o tomto správaní sú
		 * v opise metódy {@link #nevypisujFarby(boolean)
		 * nevypisujFarby(nevypisuj)}.</p>
		 * 
		 * @return {@code valtrue} ak je nastavenie aktívne, {@code valfalse}
		 *     v opačnom prípade
		 */
		public boolean nevypisujFarby()
		{ return vnútornáKonzola.nevypisujFarby; }

		/**
		 * <p>Zmení správanie vnútornej konzoly tohto plátna pri pokuse
		 * o vypísanie inštancie farby. Po aktivovaní tejto vlastnosti sa
		 * správanie konzoly zmení tak, že:</p>
		 * 
		 * <ul>
		 * <li>pred každým jednotlivým výpisom (volaním metód {@link 
		 * #vypíš(Object[]) vypíš} alebo {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}) bude zálohovaná aktuálna farba textov a pozadia
		 * textov konzoly,</li>
		 * <li>pri každom pokuse o výpis farby sa namiesto výpisu informácie
		 * o inštancii farby zmení farba textov konzoly, okrem zadania
		 * inštancie {@link Farebnosť#žiadna žiadna} (tá zachová farbu textov,
		 * ale dá sa to využiť na zmenu pozadia textov – pozri ďalší bod),</li>
		 * <li>ak za sebou nasledujú dve inštancie farieb, tak druhá v poradí
		 * zmení farbu pozadia textov konzoly, pričom v tomto prípade
		 * zadanie inštancie {@link Farebnosť#žiadna žiadna} bude znamenať
		 * zrušenie farebnosti pozadia,</li>
		 * <li>tretia a ďalšie po sebe idúce inštancie farieb sú
		 * ignorované,</li>
		 * <li>po poslednom výpise (v rámci každého volania metódy metód
		 * {@link #vypíš(Object[]) vypíš} alebo {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}) budú obnovené farby textov a pozadia textov konzoly
		 * podľa zálohovaného stavu – pozri prvý bod.</li>
		 * </ul>
		 * 
		 * @param nevypisuj {@code valtrue} ak má byť nastavenie aktivované,
		 *     {@code valfalse} v opačnom prípade
		 */
		public void nevypisujFarby(boolean nevypisuj)
		{ vnútornáKonzola.nevypisujFarby = nevypisuj; }

	// Farba pozadia textu

		/**
		 * <p><a class="getter"></a> Zistí aktuálnu farbu pozadia výpisu
		 * nových textov vnútornej konzoly plátna (podlahy alebo stropu).</p>
		 * 
		 * @return aktuálna farba pozadia textov konzoly plátna
		 *     (objekt typu {@link Farba Farba})
		 */
		public Farba farbaPozadiaTextu()
		{ return vnútornáKonzola.farbaPozadia(); }

		/**
		 * <p><a class="setter"></a> Nastav farbu pozadia textov plátna
		 * (podlahy alebo stropu). Nastaví farbu a priehľadnosť pozadia
		 * textov vnútornej konzoly podlahy alebo stropu podľa zadaného
		 * objektu. Ak je zadaná pretypovaná hodnota {@code (}{@link Farba
		 * Farba}{@code )}{@code valnull}, tak je farba pozadia zrušená.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param nováFarba objekt typu {@link Color Color} (alebo
		 *     odvodeného napr. {@link Farba Farba}) s novou farbou pozadia
		 *     textov plátna; jestvuje paleta predvolených farieb (pozri
		 *     napríklad: {@link Farebnosť#biela biela}, {@link Farebnosť#červená červená},
		 *     {@link Farebnosť#čierna čierna}…)
		 */
		public void farbaPozadiaTextu(Color nováFarba)
		{
			if (null == nováFarba)
			{
				vnútornáKonzola.zmenaFarbyPozadia = null;
				vnútornáKonzola.zrušeniePozadia = true;
			}
			else if (nováFarba instanceof Farba)
			{
				vnútornáKonzola.zmenaFarbyPozadia = (Farba)nováFarba;
				vnútornáKonzola.zrušeniePozadia = false;
			}
			else
			{
				vnútornáKonzola.zmenaFarbyPozadia = new Farba(nováFarba);
				vnútornáKonzola.zrušeniePozadia = false;
			}
		}

		/**
		 * <p>Nastav farbu pozadia textov plátna (podlahy alebo stropu) podľa
		 * farby zadaného objektu. Nastaví farbu a priehľadnosť pozadia textov
		 * podlahy alebo stropu podľa zadaného objektu. Ak je zadaná
		 * pretypovaná hodnota {@code (}{@link Farebnosť Farebnosť}{@code 
		 * )}{@code valnull}, tak je farba pozadia zrušená.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu pozadia textov plátna
		 */
		public void farbaPozadiaTextu(Farebnosť objekt)
		{
			if (null == objekt) farbaPozadiaTextu((Color)null);
			else farbaPozadiaTextu(objekt.farba());
		}

		/**
		 * <p>Nastav farbu pozadia textov plátna (podlahy alebo stropu). Nastaví
		 * farbu pozadia textov vnútornej konzoly podlahy alebo stropu podľa
		 * zadaných farebných zložiek.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia
		 * 
		 * @see #farbaPozadiaTextu(Color)
		 */
		public Farba farbaPozadiaTextu(int r, int g, int b)
		{
			vnútornáKonzola.zrušeniePozadia = false;
			return vnútornáKonzola.zmenaFarbyPozadia = new Farba(r, g, b);
		}

		/**
		 * <p>Nastav farbu pozadia textov plátna (podlahy alebo stropu). Nastaví
		 * farbu a (ne)priehľadnosť pozadia textov vnútornej konzoly podlahy
		 * alebo stropu podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
		 *     v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia
		 * 
		 * @see #farbaPozadiaTextu(Color)
		 */
		public Farba farbaPozadiaTextu(int r, int g, int b, int a)
		{
			vnútornáKonzola.zrušeniePozadia = false;
			return vnútornáKonzola.zmenaFarbyPozadia = new Farba(r, g, b, a);
		}

		/**
		 * <p>Zmení farbu pozadia textov vnútornej konzoly plátna (podlahy
		 * alebo stropu) na predvolenú. Predvolenou farbou pozadia textov
		 * konzoly plátna je „prázdna“ farba, čiže žiadna farba. Je to objekt
		 * typu farba s hodnotou {@code valnull}.</p>
		 */
		public void predvolenáFarbaPozadiaTextu()
		{
			vnútornáKonzola.zrušeniePozadia = true;
			vnútornáKonzola.zmenaFarbyPozadia = null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaTextu() predvolenáFarbaPozadiaTextu}.</p> */
		public void predvolenaFarbaPozadiaTextu()
		{ predvolenáFarbaPozadiaTextu(); }


	// Predvolená farba označenia

		/**
		 * <p><a class="getter"></a> Zistí aktuálne predvolenú farbu pozadia
		 * označených textov vnútornej konzoly plátna (podlahy alebo stropu).</p>
		 * 
		 * @return aktuálna predvolená farba pozadia označených textov
		 *     konzoly plátna (objekt typu {@link Farba Farba})
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public Farba predvolenáFarbaPozadiaOznačenia()
		{ return vnútornáKonzola.predvolenáFarbaOznačenia; }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaOznačenia() predvolenáFarbaPozadiaOznačenia}.</p> */
		public Farba predvolenaFarbaPozadiaOznacenia()
		{ return predvolenáFarbaPozadiaOznačenia(); }

		/**
		 * <p><a class="setter"></a> Nastav predvolenú farbu pozadia označenia
		 * textov vnútornej konzoly plátna (podlahy alebo stropu). Nastaví
		 * predvolenú farbu a priehľadnosť pozadia označenia textov konzoly
		 * podlahy alebo stropu podľa zadaného objektu. Ak je zadaná
		 * pretypovaná hodnota {@code (}{@link Farba Farba}{@code )}{@code 
		 * valnull}, tak je farba pozadia označenia nastavená na predvolenú
		 * {@linkplain Farebnosť#svetložltá svetložltú}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param nováFarba objekt typu {@link Color Color} (alebo
		 *     odvodeného napr. {@link Farba Farba}) s novou farbou pozadia
		 *     označenia textov konzoly plátna; jestvuje paleta predvolených
		 *     farieb (pozri napríklad: {@link Farebnosť#biela biela}, {@link Farebnosť#červená
		 *     červená}, {@link Farebnosť#čierna čierna}…)
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public void predvolenáFarbaPozadiaOznačenia(Color nováFarba)
		{
			if (null == nováFarba)
				vnútornáKonzola.predvolenáFarbaOznačenia = svetložltá;
			else if (nováFarba instanceof Farba)
				vnútornáKonzola.predvolenáFarbaOznačenia = (Farba)nováFarba;
			else
				vnútornáKonzola.predvolenáFarbaOznačenia = new Farba(nováFarba);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaOznačenia(Color) predvolenáFarbaPozadiaOznačenia}.</p> */
		public void predvolenaFarbaPozadiaOznacenia(Color nováFarba)
		{ predvolenáFarbaPozadiaOznačenia(nováFarba); }

		/**
		 * <p>Nastav predvolenú farbu pozadia označenia textov plátna (podlahy
		 * alebo stropu) podľa farby zadaného objektu. Nastaví predvolenú
		 * farbu a priehľadnosť pozadia označenia textov podlahy alebo
		 * stropu podľa zadaného objektu. Ak je zadaná pretypovaná hodnota
		 * {@code (}{@link Farebnosť Farebnosť}{@code )}{@code valnull},
		 * tak je farba pozadia označenia nastavená na
		 * {@linkplain Farebnosť#svetložltá svetložltú}, ktorá je predvolenou
		 * farbou pri štarte programovacieho rámca (angl. frameworku).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu pozadia označenia
		 *     textov plátna
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public void predvolenáFarbaPozadiaOznačenia(Farebnosť objekt)
		{
			if (null == objekt) predvolenáFarbaPozadiaOznačenia((Color)null);
			else predvolenáFarbaPozadiaOznačenia(objekt.farba());
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaOznačenia(Farebnosť) predvolenáFarbaPozadiaOznačenia}.</p> */
		public void predvolenaFarbaPozadiaOznacenia(Farebnosť objekt)
		{ predvolenáFarbaPozadiaOznačenia(objekt); }

		/**
		 * <p>Nastav predvolenú farbu pozadia označenia textov konzoly plátna
		 * (podlahy alebo stropu). Nastaví predvolenú farbu pozadia označenia
		 * textov vnútornej konzoly podlahy alebo stropu podľa zadaných
		 * farebných zložiek.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia označenia
		 *     vnútornej konzoly
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public Farba predvolenáFarbaPozadiaOznačenia(int r, int g, int b)
		{
			vnútornáKonzola.predvolenáFarbaOznačenia = new Farba(r, g, b);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
			return vnútornáKonzola.predvolenáFarbaOznačenia;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaOznačenia(int, int, int) predvolenáFarbaPozadiaOznačenia}.</p> */
		public Farba predvolenaFarbaPozadiaOznacenia(int r, int g, int b)
		{ return predvolenáFarbaPozadiaOznačenia(r, g, b); }

		/**
		 * <p>Nastav predvolenú farbu pozadia označenia textov konzoly plátna
		 * (podlahy alebo stropu). Nastaví predvolenú farbu a (ne)priehľadnosť
		 * pozadia označenia textov vnútornej konzoly podlahy alebo stropu
		 * podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
		 *     v rozsahu 0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná
		 *     farba)
		 * @return objekt typu {@link Farba Farba} – nová farba pozadia označenia
		 *     vnútornej konzoly
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public Farba predvolenáFarbaPozadiaOznačenia(int r, int g, int b, int a)
		{
			vnútornáKonzola.predvolenáFarbaOznačenia = new Farba(r, g, b, a);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
			return vnútornáKonzola.predvolenáFarbaOznačenia;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaPozadiaOznačenia(int, int, int, int) predvolenáFarbaPozadiaOznačenia}.</p> */
		public Farba predvolenaFarbaPozadiaOznacenia(int r, int g, int b, int a)
		{ return predvolenáFarbaPozadiaOznačenia(r, g, b, a); }


		// (predvolená farba textu označenia)


		/**
		 * <p><a class="getter"></a> Zistí aktuálne predvolenú farbu písma
		 * označených textov konzoly plátna (podlahy alebo stropu). Ak je
		 * vrátená hodnota {@code valnull}, znamená to, že farba textov zostáva
		 * pri označovaní pôvodná (mení sa len pozadie).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * @return aktuálna farba označených textov plátna (objekt typu
		 *     {@link Farba Farba} alebo {@code valnull})
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public Farba predvolenáFarbaTextuOznačenia()
		{ return vnútornáKonzola.predvolenáFarbaTextuOznačenia; }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextuOznačenia() predvolenáFarbaTextuOznačenia}.</p> */
		public Farba predvolenaFarbaTextuOznacenia()
		{ return predvolenáFarbaTextuOznačenia(); }

		/**
		 * <p><a class="setter"></a> Nastav predvolenú farbu písma označených
		 * textov konzoly plátna (podlahy alebo stropu). Nastaví predvolenú
		 * farbu a priehľadnosť písma označených textov vnútornej konzoly
		 * podlahy alebo stropu podľa zadaného objektu.
		 * <!--   -->
		 * Ak je zadaná pretypovaná hodnota {@code (}{@link Farba
		 * Farba}{@code )}{@code valnull}, tak je predvolenú farba textu
		 * označenia zrušená – bude sa meniť iba pozadie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param nováFarba objekt typu {@link Color Color} (alebo
		 *     odvodeného napr. {@link Farba Farba}) s novou farbou
		 *     označených textov plátna; jestvuje paleta predvolených
		 *     farieb (pozri napr.: {@link Farebnosť#biela biela}, {@link Farebnosť#červená
		 *     červená}, {@link Farebnosť#čierna čierna}…)
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public void predvolenáFarbaTextuOznačenia(Color nováFarba)
		{
			if (null == nováFarba)
				vnútornáKonzola.predvolenáFarbaTextuOznačenia = null;
			else if (nováFarba instanceof Farba)
				vnútornáKonzola.predvolenáFarbaTextuOznačenia =
					(Farba)nováFarba;
			else
				vnútornáKonzola.predvolenáFarbaTextuOznačenia =
					new Farba(nováFarba);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextuOznačenia(Color) predvolenáFarbaTextuOznačenia}.</p> */
		public void predvolenaFarbaTextuOznacenia(Color nováFarba)
		{ predvolenáFarbaTextuOznačenia(nováFarba); }

		/**
		 * <p>Nastav predvolenú farbu označenia textov plátna (podlahy alebo
		 * stropu) podľa farby zadaného objektu. Nastaví predvolenú farbu
		 * a priehľadnosť označenia textov podlahy alebo stropu podľa
		 * zadaného objektu.
		 * <!--   -->
		 * Ak je zadaná pretypovaná hodnota {@code (}{@link Farebnosť
		 * Farebnosť}{@code )}{@code valnull}, tak je predvolenú farba textu
		 * označenia zrušená – bude sa meniť iba pozadie.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * nemá žiadny údajový typ. Je akoby „spoločná pre všetky
		 * údajové typy.“ Táto hodnota vyjadruje neprítomnosť
		 * žiadneho objektu, neprítomnosť žiadnej inštancie.
		 * V skutočnosti nie je a nikdy nebude možné určiť jej typ.
		 * Pri volaní metódy musí byť pretypovanie prítomné preto,
		 * aby kompilátor dokázal určiť, ktorú verziu z preťažených
		 * metód má volať.</p>
		 * 
		 * @param objekt objekt určujúci novú farbu označenia textov plátna
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public void predvolenáFarbaTextuOznačenia(Farebnosť objekt)
		{ predvolenáFarbaTextuOznačenia(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextuOznačenia(Farebnosť) predvolenáFarbaTextuOznačenia}.</p> */
		public void predvolenaFarbaTextuOznacenia(Farebnosť objekt)
		{ predvolenáFarbaTextuOznačenia(objekt); }

		/**
		 * <p>Nastav predvolenú farbu písma označených textov konzoly plátna
		 * (podlahy alebo stropu). Nastaví predvolenú farbu písma označených
		 * textov vnútornej konzoly podlahy alebo stropu podľa zadaných
		 * farebných zložiek.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} – nová farba označenia textov
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int, int)
		 */
		public Farba predvolenáFarbaTextuOznačenia(int r, int g, int b)
		{
			vnútornáKonzola.predvolenáFarbaTextuOznačenia = new Farba(r, g, b);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
			return vnútornáKonzola.predvolenáFarbaTextuOznačenia;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextuOznačenia(int, int, int) predvolenáFarbaTextuOznačenia}.</p> */
		public Farba predvolenaFarbaTextuOznacenia(int r, int g, int b)
		{ return predvolenáFarbaTextuOznačenia(r, g, b); }

		/**
		 * <p>Nastav predvolenú farbu písma označených textov konzoly plátna
		 * (podlahy alebo stropu). Nastaví predvolenú farbu a (ne)priehľadnosť
		 * písma označených textov vnútornej konzoly podlahy alebo stropu
		 * podľa zadaných farebných zložiek a úrovne priehľadnosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Predvolenú farbu pozadia a textu,
		 * ktorý je označený je možné individuálne prekryť pre každý označený
		 * výpis. Pozri napríklad metódu {@link #označVýpis(int, int, Color...)
		 * označVýpis}.</p>
		 * 
		 * @param r červená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka novej farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti novej farby; celé číslo
		 *     v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} – nová farba označenia textov
		 * 
		 * @see #predvolenáFarbaPozadiaOznačenia()
		 * @see #predvolenáFarbaPozadiaOznačenia(Color)
		 * @see #predvolenáFarbaPozadiaOznačenia(Farebnosť)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int)
		 * @see #predvolenáFarbaPozadiaOznačenia(int, int, int, int)
		 * @see #predvolenáFarbaTextuOznačenia()
		 * @see #predvolenáFarbaTextuOznačenia(Color)
		 * @see #predvolenáFarbaTextuOznačenia(Farebnosť)
		 * @see #predvolenáFarbaTextuOznačenia(int, int, int)
		 */
		public Farba predvolenáFarbaTextuOznačenia(int r, int g, int b, int a)
		{
			vnútornáKonzola.predvolenáFarbaTextuOznačenia = new Farba(r, g, b, a);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
			return vnútornáKonzola.predvolenáFarbaTextuOznačenia;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáFarbaTextuOznačenia(int, int, int, int) predvolenáFarbaTextuOznačenia}.</p> */
		public Farba predvolenaFarbaTextuOznacenia(int r, int g, int b, int a)
		{ return predvolenáFarbaTextuOznačenia(r, g, b, a); }


	// Zisťovanie farieb označenia výpisov

		/**
		 * <p>Vráti farbu pozadia označenia špecifikovaného výpisu bloku textu,
		 * ak je označený.</p>
		 * 
		 * @param riadok index vyšetrovaného riadka
		 * @param index index vyšetrovaného bloku výpisu
		 * @return ak je blok výpisu nájdený a ak je označený, tak metóda
		 *     jeho farbu pozadia označenia, inak vráti hodnotu {@code valnull}
		 */
		public Farba farbaPozadiaOznačeniaVýpisu(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return null;
			}

			if (riadok >= vnútornáKonzola.riadky.size()) return null;

			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return null;
			}

			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;

			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo)
				{
					if (obsah.označený)
					{
						Color farba = null == obsah.označenéPozadie ?
							vnútornáKonzola.predvolenáFarbaOznačenia :
							obsah.označenéPozadie;

						if (null != farba)
						{
							if (farba instanceof Farba)
								return (Farba)farba;
							return new Farba(farba);
						}
					}
					return null;
				}
				++počítadlo;
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaPozadiaOznačeniaVýpisu(int, int) farbaPozadiaOznačeniaVýpisu}.</p> */
		public Farba farbaPozadiaOznaceniaVypisu(int riadok, int index)
		{ return farbaPozadiaOznačeniaVýpisu(riadok, index); }


		/**
		 * <p>Vráti farbu textu označenia špecifikovaného výpisu bloku textu,
		 * ak je označený a ak jestvuje nastavenie farby označeného textu.</p>
		 * 
		 * @param riadok index vyšetrovaného riadka
		 * @param index index vyšetrovaného bloku výpisu
		 * @return ak je blok výpisu nájdený, označený a ak jestvuje také
		 *     nastavenie, ktoré určí farbu textu v prípade jeho označenia,
		 *     tak metóda túto farbu vráti, inak vracia hodnotu {@code valnull}
		 */
		public Farba farbaTextuOznačeniaVýpisu(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return null;
			}

			if (riadok >= vnútornáKonzola.riadky.size()) return null;

			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return null;
			}

			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;

			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo)
				{
					if (obsah.označený)
					{
						Color farba = null == obsah.označenéPopredie ?
							vnútornáKonzola.predvolenáFarbaTextuOznačenia :
							obsah.označenéPopredie;

						if (null != farba)
						{
							if (farba instanceof Farba)
								return (Farba)farba;
							return new Farba(farba);
						}
					}
					return null;
				}
				++počítadlo;
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #farbaTextuOznačeniaVýpisu(int, int) farbaTextuOznačeniaVýpisu}.</p> */
		public Farba farbaTextuOznaceniaVypisu(int riadok, int index)
		{ return farbaTextuOznačeniaVýpisu(riadok, index); }


	// Písmo (konzoly)

		/**
		 * <p><a class="getter"></a> Čítaj aktuálny typ písma textov vnútornej
		 * konzoly plátna (podlahy alebo stropu). Na výpis textov na vnútornú
		 * konzolu slúžia metódy {@link #vypíš(Object[]) vypíš}
		 * a {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p>
		 * 
		 * @return objekt typu {@link Písmo} – aktuálne písmo
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #predvolenéPísmo()
		 * @see #vymaž()
		 * @see #vymažTexty()
		 */
		public Písmo písmo() { return vnútornáKonzola.aktuálnePísmo; }

		/** <p><a class="alias"></a> Alias pre {@link #písmo() písmo}.</p> */
		public Pismo pismo() { return new Pismo(vnútornáKonzola.aktuálnePísmo); }

		/**
		 * <p><a class="setter"></a> Nastav nový typ písma textov vnútornej
		 * konzoly plátna (podlahy alebo stropu). Na výpis textov na vnútornú
		 * konzolu slúžia metódy {@link #vypíš(Object[]) vypíš}
		 * a {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p>
		 * 
		 * @param novéPísmo objekt typu {@link Písmo} alebo {@link Font}
		 *     určujúci nový typ písma
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #predvolenéPísmo()
		 * @see #vymaž()
		 * @see #vymažTexty()
		 */
		public void písmo(Font novéPísmo)
		{
			if (novéPísmo instanceof Písmo)
				vnútornáKonzola.aktuálnePísmo = (Písmo)novéPísmo;
			else
				vnútornáKonzola.aktuálnePísmo = new Písmo(novéPísmo);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #písmo(Font) písmo}.</p> */
		public void pismo(Font novéPísmo) { písmo(novéPísmo); }

		/**
		 * <p>Nastav nový typ písma textov vnútornej konzoly plátna (podlahy
		 * alebo stropu). Na výpis textov na vnútornú konzolu slúžia metódy
		 * {@link #vypíš(Object[]) vypíš} a {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}.</p>
		 * 
		 * @param názov názov písma; môže byť všeobecný názov logického
		 *     písma (Dialog, DialogInput, Monospaced, Serif, SansSerif…)
		 *     alebo názov konkrétneho písma (Times New Roman, Arial…)
		 * @param veľkosť veľkosť písma v bodoch (hodnota je zaokrúhlená
		 *     na typ {@code typefloat})
		 * @return objekt typu {@link Písmo} určujúci nový typ písma
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #písmo(Font)
		 * @see #predvolenéPísmo()
		 * @see #vymaž()
		 * @see #vymažTexty()
		 */
		public Písmo písmo(String názov, double veľkosť)
		{
			vnútornáKonzola.aktuálnePísmo =
				new Písmo(názov, Písmo.PLAIN, veľkosť);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
			return vnútornáKonzola.aktuálnePísmo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #písmo(String, double) písmo}.</p> */
		public Pismo pismo(String názov, double veľkosť)
		{ return new Pismo(písmo(názov, veľkosť)); }

		/**
		 * <p>Nastaví predvolený typ písma textov plátna (podlahy alebo stropu).
		 * Tento typ písma používajú metódy {@link #vypíš(Object[]) vypíš}
		 * a {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p>
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #písmo(Font)
		 * @see #vymaž()
		 * @see #vymažTexty()
		 */
		public void predvolenéPísmo() { písmo(predvolenéPísmoKonzoly); }

		/** <p><a class="alias"></a> Alias pre {@link #predvolenéPísmo() predvolenéPísmo}.</p> */
		public void predvolenePismo() { písmo(predvolenéPísmoKonzoly); }


	// Označenie

		/**
		 * <p>Zistí, či je učený blok výpisu vnútornej konzoly plátna označený.
		 * (Záporné hodnoty číselných parametrov určujú index od konca.)</p>
		 * 
		 * @param riadok index riadka, ktorého stav označenia má byť overený
		 * @param index index výpisu, ktorého stav označenia má byť overený
		 * @return {@code valtrue} alebo {@code valfalse} podľa toho, či je
		 *     alebo nie je určený blok označený
		 */
		public boolean výpisJeOznačený(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return false;
			}

			if (riadok >= vnútornáKonzola.riadky.size()) return false;

			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return false;
			}

			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;

			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo) return obsah.označený;
				++počítadlo;
			}

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #výpisJeOznačený(int, int) výpisJeOznačený}.</p> */
		public boolean vypisJeOznaceny(int riadok, int index)
		{ return výpisJeOznačený(riadok, index); }


		/**
		 * <p>Označí zadaný riadok vnútornej konzoly plátna. Zadanie farieb je
		 * nepovinné a pre farby platia rovnaké informácie ako pre rovnomenný
		 * parameter v opise metódy {@link #označVýpis(int, int, Color...)
		 * označVýpis}. Tiež platí, že záporná hodnota číselného parametra
		 * určuje index riadka od konca, čiže hodnota {@code num-1} označuje
		 * posledný riadok. Návratová hodnota určuje počet blokov, ktoré sa
		 * podarilo označiť ({@code num0} nemusí nevyhnutne znamenať chybu,
		 * riadok jednoducho nemusí obsahovať ani jeden blok textu; prípadný
		 * starý stav označenia blokov je volaním tejto metódy prekrytý).</p>
		 * 
		 * @param index index riadka, ktorý má byť označený
		 * @param farby nula až štyri farby – pozri informácie pri
		 *     rovnomennom parametri v opise metódy {@link #označVýpis(int,
		 *     int, Color...) označVýpis}
		 * @return počet blokov textu, ktoré sa podarilo označiť
		 */
		public int označRiadok(int index, Color... farby)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) return 0;
			}

			if (index >= vnútornáKonzola.riadky.size()) return 0;
			RiadokKonzoly riadok = vnútornáKonzola.riadky.get(index);
			int početBlokov = 0; riadok.označenýKoniecRiadka = true;
			for (PrototypKonzoly prototyp : riadok)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				obsah.zafarbiAOznač(farby);
				++početBlokov;
			}

			if (0 != početBlokov && !Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
			return početBlokov;
		}

		/**
		 * <p>Označí konkrétny blok výpisu na konkrétnom riadku vnútornej
		 * konzoly tohto plátna. Zadanie farieb je nepovinné. Plátno má
		 * definovanú {@linkplain 
		 * #predvolenáFarbaPozadiaOznačenia(Color) predvolenú
		 * farbu označenia}. Tá môže byť prekrytá farbami určenými touto
		 * metódou, rovnako ako farba popredia (to jest textu) označenia.
		 * Dodatočné dva parametere farieb môžu určiť farebnosť označených
		 * {@linkplain #vypíšAktívneSlovo(String, Object...) aktívnych
		 * slov} – pozri opis parametra {@code farby}.
		 * (Záporné hodnoty číselných parametrov určujú index od konca.)</p>
		 * 
		 * @param riadok index riadka, v ktorom má byť označený blok výpisu
		 * @param index index výpisu, ktorý má byť v určenom riadku označený
		 * @param farby toto je variabilný zoznam parametrov a akceptuje nula
		 *     až štyri farby (prípadné ďalšie zadané farby sú ignorované);
		 *     zadané farby budú použité takto: prvá na prekrytie farby
		 *     pozadia označenia, druhá na prekrytie farby popredia (textu)
		 *     označenia, tretia na prekrytie farby pozadia označenia
		 *     {@linkplain #vypíšAktívneSlovo(String, Object...) aktívneho
		 *     slova} a štvrtá na prekrytie farby popredia (textu) označenia
		 *     {@linkplain #vypíšAktívneSlovo(String, Object...) aktívneho
		 *     slova}; ak je namiesto niektorej z farieb zadaná hodnota
		 *     {@code valnull}, bude použitá
		 *     {@linkplain #predvolenáFarbaPozadiaOznačenia(Color)
		 *     predvolená farba označenia}
		 * @return vráti počet označených blokov (v tomto prípade môže byť
		 *     návratová hodnota len {@code num0} alebo {@code num1};
		 *     {@code num0} znamená zlyhanie; prípadný starý stav označenia
		 *     bloku je volaním tejto metódy prekrytý)
		 */
		public int označVýpis(int riadok, int index, Color... farby)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return 0;
			}
			if (riadok >= vnútornáKonzola.riadky.size()) return 0;
			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return 0;
			}
			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;
			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo)
				{
					obsah.zafarbiAOznač(farby);
					if (!Svet.právePrekresľujem)
						Svet.automatickéPrekreslenie();
					return 1;
				}
				++počítadlo;
			}
			return 0;
		}

		/**
		 * <p>Označí všetky riadky medzi hodnotami parametrov {@code prvýRiadok}
		 * a {@code poslednýRiadok}, vrátane nich.</p>
		 * 
		 * <p>Ak je hodnota niektorého z parametrov záporná, tak zmení význam
		 * na poradové číslo riadka od konca, pričom hodnota {@code num-1}
		 * znamená posledný riadok.</p>
		 * 
		 * <p>Ak je hodnota parametra {@code prvýRiadok} väčšia než hodnota
		 * parametra {@code poslednýRiadok}, tak budú tiež označené riadky
		 * nachádzajúce sa medzi nimi a vrátane nich, čiže metóda sa správa
		 * tak, ako keby boli hodnoty uvedené v správnom poradí.</p>
		 * 
		 * <p>Zadanie farieb je nepovinné. Pre parameter {@code farby} v tejto
		 * metóde platia rovnaké informácie ako pre rovnomenný parameter
		 * v opise metódy {@link #označVýpis(int, int, Color...) označVýpis}.</p>
		 * 
		 * <p>Návratová hodnota metódy určuje počet blokov textu, ktoré sa
		 * v zadanom rozsahu riadkov podarilo označiť, pričom bloky
		 * v platnom rozsahu indexov sú označené bez ohľadu na ich
		 * predchádzajúci stav označenia. To znamená, že prípadná staršia
		 * farba označenia bude prekrytá podľa variabilného zoznamu
		 * parametrov {@code farby}. (Návratová hodnota {@code num0}
		 * nemusí nevyhnutne znamenať chybu. V rozmedzí riadkov sa nemusel
		 * nachádzať žiadny blok textu – riadky boli prázdne.)</p>
		 * 
		 * @param prvýRiadok prvý označovaný riadok
		 * @param poslednýRiadok posledný označovaný riadok
		 * @param farby nula až štyri farby – pozri informácie pri
		 *     rovnomennom parametri v opise metódy {@link #označVýpis(int,
		 *     int, Color...) označVýpis}
		 * @return počet blokov textu, ktoré sa podarilo označiť
		 *     (hodnota nezodpovedá počtu riadkov, jeden riadok môže obsahovať
		 *     ľubovoľný počet textových blokov, vrátane nuly)
		 */
		public int označRiadky(int prvýRiadok, int poslednýRiadok,
			Color... farby)
		{
			// (Vysvetlenie k nasledujúcemu postupu je v metóde označVýpisy.)
			if (prvýRiadok >= vnútornáKonzola.riadky.size())
				prvýRiadok = vnútornáKonzola.riadky.size() - 1;

			if (prvýRiadok < 0)
			{
				prvýRiadok += vnútornáKonzola.riadky.size();
				if (prvýRiadok < 0) prvýRiadok = 0;
			}

			if (poslednýRiadok >= vnútornáKonzola.riadky.size())
				poslednýRiadok = vnútornáKonzola.riadky.size() - 1;

			if (poslednýRiadok < 0)
			{
				poslednýRiadok += vnútornáKonzola.riadky.size();
				if (poslednýRiadok < 0) poslednýRiadok = 0;
			}

			if (prvýRiadok > poslednýRiadok)
			{
				int vymeň = prvýRiadok;
				prvýRiadok = poslednýRiadok;
				poslednýRiadok = vymeň;
			}

			int početOznačených = 0;

			for (int i = prvýRiadok; i <= poslednýRiadok; ++i)
			{
				RiadokKonzoly riadok = vnútornáKonzola.riadky.get(i);
				riadok.označenýKoniecRiadka = true;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					obsah.zafarbiAOznač(farby);
					++početOznačených;
				}
			}

			if (0 != početOznačených)
				Svet.automatickéPrekreslenie();
			return početOznačených;
		}

		/**
		 * <p>Označí výpisy v rozmedzí stanovenom hodnotami štyroch číselných
		 * parametrov. Platia rovnaké informácie ako pri metóde
		 * {@link #označRiadky(int, int, Color...) označRiadky}, ibaže rozsahy
		 * sú spresnené o číselné indexy textových blokov na jednotlivých
		 * riadkoch. (Záporné hodnoty číselných parametrov určujú indexy od
		 * konca. Ak je niektorá začiatočná hodnota väčšia než koncová, tak
		 * je výsledok procesu rovnaký ako keby boli hodnoty vymenené. Farby
		 * sú nepovinné. Ich význam je rovnaký ako pri metóde {@link 
		 * #označVýpis(int, int, Color...) označVýpis}.)</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad vygeneruje niekoľko náhodných slov zoradených
		 * do viet a zoskupených do odsekov. Výpis je možné označovať
		 * ťahaním myši. Pri podržaní klávesu {@code Ctrl} a označovaní
		 * myšou sa dá vytvoriť viacnásobné označenie. Provizórne
		 * naprogramovaná reakcia klávesnice spracúva klávesovú kombináciu
		 * {@code Ctrl + C} (na všetkých platformách), na ktorú zareaguje
		 * skopírovaním označených textov do schránky.</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} TestovanieOznačenia {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Atribúty slúžiace na uchovanie indexov riadkov a blokov v priebehu}
				{@code comm// označovania:}
				{@code kwdprivate} {@code typeint} počiatočnýRiadok, koncovýRiadok, počiatočnýBlok, koncovýBlok;

				{@code comm// Polia s používanými samohláskami a spoluhláskami v náhodne}
				{@code comm// generovaných slovách:}
				{@code kwdprivate} {@code kwdfinal} {@code typechar}[] samohlásky = {{@code srg'a'}, {@code srg'e'}, {@code srg'i'}, {@code srg'o'}, {@code srg'u'}},
					spoluhlásky = {{@code srg'b'}, {@code srg'c'}, {@code srg'd'}, {@code srg'f'}, {@code srg'g'}, {@code srg'h'}, {@code srg'j'}, {@code srg'k'},
						{@code srg'l'}, {@code srg'm'}, {@code srg'n'}, {@code srg'p'}, {@code srg'r'}, {@code srg's'}, {@code srg't'}, {@code srg'v'}, {@code srg'z'}};
				<hr/>
				{@code comm// Konštruktor:}
				{@code kwdprivate} TestovanieOznačenia()
				{
					počiatočnýRiadok = koncovýRiadok = počiatočnýBlok = koncovýBlok = {@code num0};

					{@link Svet Svet}.{@link Svet#písmo(String, double) písmo}({@code srg"Garamond"}, {@code num18});
					{@link StringBuffer StringBuffer} text = {@code kwdnew} {@link java.lang.StringBuffer#StringBuffer() StringBuffer}();
					{@code typeint} početOdsekov = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num9}, {@code num12});

					{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početOdsekov; ++i)
					{
						{@code typeint} početViet = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num4}, {@code num7});

						{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; početViet; ++j)
						{
							{@code typeint} početSlov = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num1}, {@code num8});

							{@code kwdfor} ({@code typeint} k = {@code num0}; k &lt; početSlov; ++k)
								generujĎalšieSlovo(text, {@code num0} == k, početSlov &#45; {@code num1} == k);

							{@code kwdif} (j &lt; početViet &#45; {@code num1}) text.{@link StringBuffer#append(char) append}({@code srg' '});

							{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}(text);
							text.{@link StringBuffer#setLength(int) setLength}({@code num0});
						}

						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}();
					}
				}

				{@code comm// Metóda na vygenerovanie ďalšieho slova:}
				{@code kwdprivate} {@code typevoid} generujĎalšieSlovo({@link StringBuffer StringBuffer} text,
					{@code typeboolean} prvé, {@code typeboolean} posledné)
				{
					{@code typeint} početSlabík = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num1}, {@code num4});

					{@code kwdif} ({@code num0} == {@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, {@code num1}))
					{
						{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početSlabík; ++i)
						{
							text.{@link StringBuffer#append(char) append}(samohlásky[({@code typeint}){@link Svet Svet}.
								{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, samohlásky.length &#45; {@code num1})]);
							text.{@link StringBuffer#append(char) append}(spoluhlásky[({@code typeint}){@link Svet Svet}.
								{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, spoluhlásky.length &#45; {@code num1})]);
						}
					}
					{@code kwdelse}
					{
						{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početSlabík; ++i)
						{
							text.{@link StringBuffer#append(char) append}(spoluhlásky[({@code typeint}){@link Svet Svet}.
								{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, spoluhlásky.length &#45; {@code num1})]);
							text.{@link StringBuffer#append(char) append}(samohlásky[({@code typeint}){@link Svet Svet}.
								{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num0}, samohlásky.length &#45; {@code num1})]);
						}
					}

					{@code kwdif} (prvé)
					{
						text.{@link StringBuffer#setCharAt(int, char) setCharAt}({@code num0}, {@link Character Character}.{@link Character#toUpperCase(char) toUpperCase}(text.{@link StringBuffer#charAt(int) charAt}({@code num0})));
						{@code kwdif} (posledné) text.{@link StringBuffer#append(char) append}({@code srg'.'});
					}
					{@code kwdelse} {@code kwdif} (posledné) text.{@link StringBuffer#append(char) append}({@code srg'.'});
					{@code kwdelse} text.{@link StringBuffer#append(char) append}({@code srg' '});
				}
				<hr/>
				{@code comm// ---------------- Obsluha udalostí ----------------}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
				{
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
					{
						{@code typeint}[] výpis = {@link Plátno strop}.{@link #výpisPriMyši() výpisPriMyši}();
						{@code kwdif} ({@code valnull} != výpis)
						{
							{@code kwdif} ({@link Plátno strop}.{@link #výpisJeOznačený(int, int) výpisJeOznačený}(výpis[{@code num0}], výpis[{@code num1}]))
								{@link Plátno strop}.{@link #zrušOznačenieVýpisu(int, int) zrušOznačenieVýpisu}(výpis[{@code num0}], výpis[{@code num1}]);
							{@code kwdelse}
								{@link Plátno strop}.{@link #označVýpis(int, int, Color...) označVýpis}(výpis[{@code num0}], výpis[{@code num1}]);
						}
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
				{
					{@code kwdif} (!{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
						{@link Plátno strop}.{@link #zrušOznačenieTextov() zrušOznačenieTextov}();

					{@code typeint}[] výpis = {@link Plátno strop}.{@link #výpisPriMyši() výpisPriMyši}();
					{@code kwdif} ({@code valnull} != výpis)
					{
						počiatočnýRiadok = koncovýRiadok = výpis[{@code num0}];
						počiatočnýBlok   = koncovýBlok   = výpis[{@code num1}];
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
				{
					{@code typeint}[] výpis = {@link Plátno strop}.{@link #výpisPriMyši() výpisPriMyši}();
					{@code kwdif} ({@code valnull} != výpis)
					{
						{@link Plátno strop}.{@link #zrušOznačenieVýpisov(int, int, int, int) zrušOznačenieVýpisov}(
							počiatočnýRiadok, počiatočnýBlok,
							koncovýRiadok, koncovýBlok);

						koncovýRiadok = výpis[{@code num0}];
						koncovýBlok   = výpis[{@code num1}];

						{@link Plátno strop}.{@code curroznačVýpisy}(
							počiatočnýRiadok, počiatočnýBlok,
							koncovýRiadok, koncovýBlok);
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieKlávesu() stlačenieKlávesu}()
				{
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#VK_C VK_C}) &&
						{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
					{
						{@link Plátno strop}.{@link #textyDoSchránky(boolean) textyDoSchránky}({@code valtrue});
						{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Texty boli vložené do schránky."});
					}
				}
				<hr/>
				{@code comm// Hlavná metóda:}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestovanieOznačenia.cfg"});
					{@code kwdnew} TestovanieOznačenia();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>oznac-vypisy.png<alt/>Ukážka označených
		 * textov.</image>Ukážka procesu označovania textov.</p>
		 * 
		 * @param prvýRiadok riadok, na ktorom sa označovanie začne
		 * @param prvýVýpis začiatočný výpis na riadku s indexom {@code prvýRiadok}
		 * @param poslednýRiadok riadok, na ktorom sa označovanie skončí
		 * @param poslednýVýpis konečný výpis na riadku s indexom
		 *     {@code poslednýRiadok}
		 * @param farby nula až štyri farby – pozri informácie pri
		 *     rovnomennom parametri v opise metódy {@link #označVýpis(int,
		 *     int, Color...) označVýpis}
		 * @return počet blokov textu (výpisov), ktoré sa podarilo označiť
		 */
		public int označVýpisy(int prvýRiadok, int prvýVýpis,
			int poslednýRiadok, int poslednýVýpis, Color... farby)
		{
			int početVýpisov;

			// Ak je konzola prázdna, tak táto prvá kontrola bude mať
			// za následok vytvorenie záporných indexov, ale…
			if (prvýRiadok >= vnútornáKonzola.riadky.size())
			{
				prvýRiadok = vnútornáKonzola.riadky.size() - 1;
				početVýpisov = početVýpisov(prvýRiadok);
				prvýVýpis = početVýpisov - 1;
			}
			else početVýpisov = početVýpisov(prvýRiadok);

			// … to napraví táto druhá kontrola.
			if (prvýRiadok < 0)
			{
				prvýRiadok += vnútornáKonzola.riadky.size();
				if (prvýRiadok < 0)
				{
					prvýRiadok = 0;
					prvýVýpis = 0;
				}
			}

			// To isté platí pre tieto dve kontroly…
			if (prvýVýpis >= početVýpisov)
				prvýVýpis = početVýpisov - 1;

			if (prvýVýpis < 0)
			{
				prvýVýpis += početVýpisov;
				if (prvýVýpis < 0) prvýVýpis = 0;
			}


			// Opakovane: Ak je konzola prázdna, tak táto prvá kontrola
			// bude mať za následok vytvorenie záporných indexov, ale…
			if (poslednýRiadok >= vnútornáKonzola.riadky.size())
			{
				poslednýRiadok = vnútornáKonzola.riadky.size() - 1;
				početVýpisov = početVýpisov(poslednýRiadok);
				poslednýVýpis = početVýpisov - 1;
			}
			else početVýpisov = početVýpisov(poslednýRiadok);

			// … to napraví táto druhá kontrola.
			if (poslednýRiadok < 0)
			{
				poslednýRiadok += vnútornáKonzola.riadky.size();
				if (poslednýRiadok < 0)
				{
					poslednýRiadok = 0;
					poslednýVýpis = 0;
				}
			}

			// Opäť, to isté platí pre tieto dve kontroly (v kontexte
			// jedného riadka)…
			if (poslednýVýpis >= početVýpisov)
				poslednýVýpis = početVýpisov - 1;

			if (poslednýVýpis < 0)
			{
				poslednýVýpis += početVýpisov;
				if (poslednýVýpis < 0) poslednýVýpis = 0;
			}


			// Teraz overíme orientácie rozsahov:
			if (prvýRiadok == poslednýRiadok)
			{
				if (prvýVýpis > poslednýVýpis)
				{
					int vymeň = prvýVýpis;
					prvýVýpis = poslednýVýpis;
					poslednýVýpis = vymeň;
				}
			}
			else if (prvýRiadok > poslednýRiadok)
			{
				int vymeň = prvýRiadok;
				prvýRiadok = poslednýRiadok;
				poslednýRiadok = vymeň;

				vymeň = prvýVýpis;
				prvýVýpis = poslednýVýpis;
				poslednýVýpis = vymeň;
			}

			RiadokKonzoly riadok;
			int počítadlo, početOznačených = 0;

			// Pri označovaní je dôležité rozlišovať minimálne jednu
			// špeciálnu situáciu.
			if (prvýRiadok == poslednýRiadok)
			{
				// A tou je, keď sa indexy prvého a posledného riadka
				// zhodujú. Vtedy prvý a posledný výpis/blok (resp. pri
				// označovaní po znakoch stĺpec alebo znak) vymedzujú
				// rozsah označenia v rámci jediného riadka a preto ho
				// treba spracovať zvlášť.
				riadok = vnútornáKonzola.riadky.get(prvýRiadok);
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (poslednýVýpis < počítadlo) break;
					if (prvýVýpis <= počítadlo)
					{
						obsah.zafarbiAOznač(farby);
						++početOznačených;
					}
					++počítadlo;
				}
			}
			else
			{
				// V ostatných prípadoch je prvý riadok označený od
				// začiatku prvého výpisu po koniec…
				riadok = vnútornáKonzola.riadky.get(prvýRiadok);
				riadok.označenýKoniecRiadka = true;
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (prvýVýpis <= počítadlo)
					{
						obsah.zafarbiAOznač(farby);
						++početOznačených;
					}
					++počítadlo;
				}

				// … riadky medzi prvým a posledným sú označené celé…
				for (int i = prvýRiadok + 1; i < poslednýRiadok; ++i)
				{
					riadok = vnútornáKonzola.riadky.get(i);
					riadok.označenýKoniecRiadka = true;
					for (PrototypKonzoly prototyp : riadok)
					{
						ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
						if (null == obsah) continue;
						obsah.zafarbiAOznač(farby);
						++početOznačených;
					}
				}

				// … a posledný riadok je označený od začiatku po index
				// posledného výpisu.
				riadok = vnútornáKonzola.riadky.get(poslednýRiadok);
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (poslednýVýpis < počítadlo) break;
					obsah.zafarbiAOznač(farby);
					++početOznačených; ++počítadlo;
				}
			}

			if (0 != početOznačených)
				Svet.automatickéPrekreslenie();
			return početOznačených;
		}

		/**
		 * <p>Označí všetky texty vnútornej konzoly. (Prekryje prípadné
		 * jestvujúce označenie.)</p>
		 * 
		 * @param farby toto je variabilný zoznam parametrov a akceptuje
		 *     nula až štyri farby – pozri informácie pri rovnomennom
		 *     parametri v opise metódy {@link #označVýpis(int, int,
		 *     Color...) označVýpis}
		 * @return počet blokov textu, ktoré boli označené (bez ohľadu na
		 *     ich predchádzajúci stav označenia, to znamená, že prípadná
		 *     staršia farba označenia bude prekrytá podľa variabilného
		 *     zoznamu parametrov {@code farby})
		 */
		public int označVšetkyTexty(Color... farby)
		{
			int početOznačených = 0;

			for (RiadokKonzoly riadok : vnútornáKonzola.riadky)
			{
				riadok.označenýKoniecRiadka = true;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					obsah.zafarbiAOznač(farby);
					++početOznačených;
				}
			}

			if (0 != početOznačených)
				Svet.automatickéPrekreslenie();
			return početOznačených;
		}

			// Poznámka: Uvažoval som o pridaní aliasov: označTexty
			// a oznacTexty, ale potom som to zamietol. Označ texty?
			// Aké, ktoré? (Aj tak som o tom uvažoval len z toho
			// dôvodu, aby sa rozšírili možnosti skriptovania a medzi
			// aliasy príkazov skriptov som to pridal.)


		/**
		 * <p>Zistí a vráti rozsah oblasti označenia, ktorá obsahuje zadaný
		 * výpis na zadanom riadku. Oblasťou označenia sa rozumie taká
		 * oblasť (množina výpisov), ktoré majú rovnaký stav označenia, čiže
		 * ak je špecifikovaný výpis označený, tak sa hľadá rozsah všetkých
		 * susediacich blokov, ktoré sú tiež označené a naopak.</p>
		 * 
		 * @param riadok index riadka, v ktorom sa má začať hľadanie oblasti
		 * @param index index výpisu, od ktorého sa obojsmerne začne hľadanie
		 *     rozsahu oblasti
		 * @return ak sa všetko vykonalo bez chýb, tak metóda vráti
		 *     štvorprvkové pole, ktorého prvé dva prvky určujú prvý riadok
		 *     a prvý výpis oblasti a ďalšie dva posledný riadok a posledný
		 *     výpis celej oblasti; v prípade chyby vráti metóda hodnotu
		 *     {@code valnull}
		 */
		public int[] rozsahOblastiOznačenia(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return null;
			}

			if (riadok >= vnútornáKonzola.riadky.size()) return null;

			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return null;
			}

			// Najskôr nájdeme určený blok na určenom riadku:
			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			ObsahKonzoly obsah = null; int počítadlo = 0, i = 0;

			for (; i < riadok2.size(); ++i)
			{
				PrototypKonzoly prototyp = riadok2.get(i);
				obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo) break;
				++počítadlo;
			}

			if (null == obsah) return null;

			// Potom vykonáme hľadanie rozsahu:
			int j = i, prvýRiadok = riadok, prvýVýpis = index,
				poslednýRiadok = riadok, poslednýVýpis = index;
			boolean stav = obsah.označený;

			// Spätné rozširovanie od aktuálnej pozície po začiatok konzoly.
			// Index „i“ ukazuje na aktuálny prototyp, premenná „riadok“ na
			// aktuálny riadok a „počítadlo“ na aktuálny blok.
			do
			{
				for (--i; i >= 0; --i)
				{
					PrototypKonzoly prototyp = riadok2.get(i);
					obsah = vnútornáKonzola.dajObsah(prototyp);

					if (null == obsah) continue;
					--počítadlo;

					if (stav == obsah.označený)
					{
						// Keď sa stav zhoduje, zapamätáme si
						// nové hodnoty začiatku rozsahu:
						prvýRiadok = riadok;
						prvýVýpis = počítadlo;
					}
					else
					{
						// Inak hľadanie ukončíme:
						riadok = 0;
						break;
					}
				}

				// Prechod na predchádzajúci riadok…
				if (--riadok >= 0)
				{
					riadok2 = vnútornáKonzola.riadky.get(riadok);
					if (riadok2.označenýKoniecRiadka)
					{
						i = riadok2.size();
						počítadlo = početVýpisov(riadok);
					}
					else
					{
						// Ak nie je koniec predchádzajúceho riadka označený,
						// tak hľadanie tiež ukončíme (predchádzajúce bloky
						// už nepatria do rovnakého rozsahu – prípadné
						// zlievanie rozsahov nastáva len ak sa dva
						// označované rozsahy stretnú v jednom riadku)…
						riadok = -1;
					}
				}
			}
			while (riadok >= 0);

			// Reflektujúc na komentáre vyššie začneme dopredné rozširovanie
			// (po koniec konzoly). Premenná „riadok“ opäť ukazuje na aktuálny
			// riadok, aktuálny prototyp ukazuje premenná „j“ a aktuálny
			// blok premenná „index“ (tá mala v čase začiatku spätného
			// rozširovania rovnakú hodnotu ako „počítadlo“).
			riadok = poslednýRiadok;

			do
			{
				for (++j; j < riadok2.size(); ++j)
				{
					PrototypKonzoly prototyp = riadok2.get(j);
					obsah = vnútornáKonzola.dajObsah(prototyp);

					if (null == obsah) continue;
					++index;

					if (stav == obsah.označený)
					{
						// Ak sa stav zhoduje, zapamätáme si
						// nové hodnoty konca rozsahu:
						poslednýRiadok = riadok;
						poslednýVýpis = index;
					}
					else
					{
						// Inak hľadanie ukončíme:
						riadok = 0;
						break;
					}
				}

				if (riadok2.označenýKoniecRiadka)
				{
					// Prechod na nasledujúci riadok
					if (++riadok < vnútornáKonzola.riadky.size())
					{
						riadok2 = vnútornáKonzola.riadky.get(riadok);
						j = -1; index = -1;
					}
				}
				else
				{
					// Predčasné ukončenie podľa stavu označenia konca
					// aktuálneho riadka:
					riadok = vnútornáKonzola.riadky.size();
				}
			}
			while (riadok < vnútornáKonzola.riadky.size());

			return new int[] {prvýRiadok, prvýVýpis,
				poslednýRiadok, poslednýVýpis};
		}


		/**
		 * <p>Zruší označenie zadaného riadka vnútornej konzoly plátna.
		 * Záporná hodnota číselného parametra určuje index riadka od
		 * konca. Návratová hodnota určuje počet blokov, ktorým bolo
		 * reálne odobraté označenie.</p>
		 * 
		 * @param index index riadka, ktorého blokom má byť odobraté označenie
		 * @return počet blokov textu, ktorým bolo reálne odobraté označenie
		 */
		public int zrušOznačenieRiadka(int index)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) return 0;
			}

			if (index >= vnútornáKonzola.riadky.size()) return 0;
			RiadokKonzoly riadok = vnútornáKonzola.riadky.get(index);
			int početBlokov = 0; riadok.označenýKoniecRiadka = false;
			for (PrototypKonzoly prototyp : riadok)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah || !obsah.označený) continue;
				obsah.označený = false;
				++početBlokov;
			}

			if (0 != početBlokov) Svet.automatickéPrekreslenie();
			return početBlokov;
		}

		/**
		 * <p>Zruší označenie konkrétnemu bloku výpisu na konkrétnom riadku
		 * vnútornej konzoly plátna. Záporné hodnoty číselných parametrov
		 * určujú index od konca.</p>
		 * 
		 * @param riadok index riadka, v ktorom má byť zrušené označenie
		 *     bloku výpisu
		 * @param index index výpisu, ktorému má byť odobraté označenie
		 * @return vráti počet blokov, ktorým bolo reálne odobraté označenie
		 */
		public int zrušOznačenieVýpisu(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return 0;
			}
			if (riadok >= vnútornáKonzola.riadky.size()) return 0;
			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return 0;
			}
			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;
			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo)
				{
					if (obsah.označený)
					{
						obsah.označený = false;
						if (!Svet.právePrekresľujem)
							Svet.automatickéPrekreslenie();
						return 1;
					}
					return 0;
				}
				++počítadlo;
			}
			return 0;
		}

		/**
		 * <p>Zruší označenie všetkých riadkov medzi hodnotami parametrov
		 * {@code prvýRiadok} a {@code poslednýRiadok}, vrátane nich.</p>
		 * 
		 * <p>Ak je hodnota niektorého z parametrov záporná, tak zmení význam
		 * na poradové číslo riadka od konca, pričom hodnota {@code num-1}
		 * znamená posledný riadok.</p>
		 * 
		 * <p>Ak je hodnota parametra {@code prvýRiadok} väčšia než hodnota
		 * parametra {@code poslednýRiadok}, tak budú spracované riadky tak,
		 * ako keby boli hodnoty uvedené v obrátenom poradí.</p>
		 * 
		 * <p>Návratová hodnota metódy určuje počet blokov textu, ktorým
		 * bolo reálne odobraté označenie.</p>
		 * 
		 * @param prvýRiadok prvý riadok, ktorého stav označenia má byť zrušený
		 * @param poslednýRiadok posledný, ktorého stav označenia má byť zrušený
		 * @return počet blokov textu, ktorým bolo reálne odobraté označenie
		 *     (hodnota nezodpovedá počtu riadkov, jeden riadok môže obsahovať
		 *     ľubovoľný počet textových blokov, vrátane nuly)
		 */
		public int zrušOznačenieRiadkov(int prvýRiadok, int poslednýRiadok)
		{
			// (Vysvetlenie k nasledujúcemu postupu je v metóde
			// zrušOznačenieVýpisov.)
			if (prvýRiadok >= vnútornáKonzola.riadky.size())
				prvýRiadok = vnútornáKonzola.riadky.size() - 1;

			if (prvýRiadok < 0)
			{
				prvýRiadok += vnútornáKonzola.riadky.size();
				if (prvýRiadok < 0) prvýRiadok = 0;
			}

			if (poslednýRiadok >= vnútornáKonzola.riadky.size())
				poslednýRiadok = vnútornáKonzola.riadky.size() - 1;

			if (poslednýRiadok < 0)
			{
				poslednýRiadok += vnútornáKonzola.riadky.size();
				if (poslednýRiadok < 0) poslednýRiadok = 0;
			}

			if (prvýRiadok > poslednýRiadok)
			{
				int vymeň = prvýRiadok;
				prvýRiadok = poslednýRiadok;
				poslednýRiadok = vymeň;
			}

			int početOdznačených = 0;

			for (int i = prvýRiadok; i <= poslednýRiadok; ++i)
			{
				RiadokKonzoly riadok = vnútornáKonzola.riadky.get(i);
				riadok.označenýKoniecRiadka = false;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah || !obsah.označený) continue;
					obsah.označený = false;
					++početOdznačených;
				}
			}

			if (0 != početOdznačených)
				Svet.automatickéPrekreslenie();
			return početOdznačených;
		}

		/**
		 * <p>Zruší označenie výpisov v rozmedzí stanovenom hodnotami štyroch
		 * číselných parametrov. Platia rovnaké informácie ako pri metóde
		 * {@link #zrušOznačenieRiadkov(int, int) zrušOznačenieRiadkov},
		 * ibaže rozsahy sú spresnené o číselné indexy textových blokov na
		 * jednotlivých riadkoch. (Záporné hodnoty číselných parametrov
		 * určujú indexy od konca. Ak je niektorá začiatočná hodnota väčšia
		 * než koncová, tak je výsledok procesu rovnaký ako keby boli hodnoty
		 * vymenené.)</p>
		 * 
		 * @param prvýRiadok riadok, na ktorom sa proces rušenia označovania
		 *     začne
		 * @param prvýVýpis začiatočný výpis na riadku s indexom
		 *     {@code prvýRiadok}
		 * @param poslednýRiadok riadok, na ktorom sa proces rušenia
		 *     označovania skončí
		 * @param poslednýVýpis konečný výpis na riadku s indexom
		 *     {@code poslednýRiadok}
		 * @return počet blokov textu (výpisov), ktorým bolo reálne odobraté
		 *     označenie
		 */
		public int zrušOznačenieVýpisov(int prvýRiadok, int prvýVýpis,
			int poslednýRiadok, int poslednýVýpis)
		{
			int početVýpisov;

			// Ak je konzola prázdna, tak táto prvá kontrola bude mať
			// za následok vytvorenie záporných indexov, ale…
			if (prvýRiadok >= vnútornáKonzola.riadky.size())
			{
				prvýRiadok = vnútornáKonzola.riadky.size() - 1;
				početVýpisov = početVýpisov(prvýRiadok);
				prvýVýpis = početVýpisov - 1;
			}
			else početVýpisov = početVýpisov(prvýRiadok);

			// … to napraví táto druhá kontrola.
			if (prvýRiadok < 0)
			{
				prvýRiadok += vnútornáKonzola.riadky.size();
				if (prvýRiadok < 0)
				{
					prvýRiadok = 0;
					prvýVýpis = 0;
				}
			}

			// To isté platí pre tieto dve kontroly…
			if (prvýVýpis >= početVýpisov)
				prvýVýpis = početVýpisov - 1;

			if (prvýVýpis < 0)
			{
				prvýVýpis += početVýpisov;
				if (prvýVýpis < 0) prvýVýpis = 0;
			}


			// Opakovane: Ak je konzola prázdna, tak táto prvá kontrola
			// bude mať za následok vytvorenie záporných indexov, ale…
			if (poslednýRiadok >= vnútornáKonzola.riadky.size())
			{
				poslednýRiadok = vnútornáKonzola.riadky.size() - 1;
				početVýpisov = početVýpisov(poslednýRiadok);
				poslednýVýpis = početVýpisov - 1;
			}
			else početVýpisov = početVýpisov(poslednýRiadok);

			// … to napraví táto druhá kontrola.
			if (poslednýRiadok < 0)
			{
				poslednýRiadok += vnútornáKonzola.riadky.size();
				if (poslednýRiadok < 0)
				{
					poslednýRiadok = 0;
					poslednýVýpis = 0;
				}
			}

			// Opäť, to isté platí pre tieto dve kontroly (v kontexte
			// jedného riadka)…
			if (poslednýVýpis >= početVýpisov)
				poslednýVýpis = početVýpisov - 1;

			if (poslednýVýpis < 0)
			{
				poslednýVýpis += početVýpisov;
				if (poslednýVýpis < 0) poslednýVýpis = 0;
			}


			// Teraz overíme orientácie rozsahov:
			if (prvýRiadok == poslednýRiadok)
			{
				if (prvýVýpis > poslednýVýpis)
				{
					int vymeň = prvýVýpis;
					prvýVýpis = poslednýVýpis;
					poslednýVýpis = vymeň;
				}
			}
			else if (prvýRiadok > poslednýRiadok)
			{
				int vymeň = prvýRiadok;
				prvýRiadok = poslednýRiadok;
				poslednýRiadok = vymeň;

				vymeň = prvýVýpis;
				prvýVýpis = poslednýVýpis;
				poslednýVýpis = vymeň;
			}

			RiadokKonzoly riadok;
			int počítadlo, početOdznačených = 0;

			// Pri tomto procese je dôležité rozlišovať minimálne jednu
			// špeciálnu situáciu.
			if (prvýRiadok == poslednýRiadok)
			{
				// A tou je, keď sa indexy prvého a posledného riadka
				// zhodujú. Vtedy prvý a posledný výpis/blok (resp. pri
				// postupe po znakoch stĺpec alebo znak) vymedzujú
				// rozsah v rámci jediného riadka a preto ho treba
				// spracovať zvlášť.
				riadok = vnútornáKonzola.riadky.get(prvýRiadok);
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (poslednýVýpis < počítadlo) break;
					if (prvýVýpis <= počítadlo && obsah.označený)
					{
						obsah.označený = false;
						++početOdznačených;
					}
					++počítadlo;
				}
			}
			else
			{
				// V ostatných prípadoch je prvý riadok spracovaný od
				// začiatku prvého výpisu po koniec…
				riadok = vnútornáKonzola.riadky.get(prvýRiadok);
				riadok.označenýKoniecRiadka = false;
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (prvýVýpis <= počítadlo && obsah.označený)
					{
						obsah.označený = false;
						++početOdznačených;
					}
					++počítadlo;
				}

				// … riadky medzi prvým a posledným sú spracované celé…
				for (int i = prvýRiadok + 1; i < poslednýRiadok; ++i)
				{
					riadok = vnútornáKonzola.riadky.get(i);
					riadok.označenýKoniecRiadka = false;
					for (PrototypKonzoly prototyp : riadok)
					{
						ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
						if (null == obsah || !obsah.označený) continue;
						obsah.označený = false;
						++početOdznačených;
					}
				}

				// … a posledný riadok je spracované od začiatku po index
				// posledného výpisu.
				riadok = vnútornáKonzola.riadky.get(poslednýRiadok);
				počítadlo = 0;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (poslednýVýpis < počítadlo) break;
					if (obsah.označený)
					{
						obsah.označený = false;
						++početOdznačených;
					}
					++počítadlo;
				}
			}

			if (0 != početOdznačených)
				Svet.automatickéPrekreslenie();
			return početOdznačených;
		}

		/**
		 * <p>Zruší jestvujúce označenie všetkých textov vnútornej konzoly.</p>
		 * 
		 * @return počet blokov textu, ktorým bolo reálne zrušené označenie
		 */
		public int zrušOznačenieTextov()
		{
			int početOdznačených = 0;

			for (RiadokKonzoly riadok : vnútornáKonzola.riadky)
			{
				riadok.označenýKoniecRiadka = false;
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah || !obsah.označený) continue;
					obsah.označený = false; ++početOdznačených;
				}
			}

			if (0 != početOdznačených)
				Svet.automatickéPrekreslenie();
			return početOdznačených;
		}

			// Poznámka: Uvažoval som o pridaní aliasov:
			// zrušOznačenieVšetkýchTextov a zrusOznacenieVsetkychTextov,
			// ale potom som to zamietol. Zdalo sa mi to byť príliš dlhé…
			// (Aj tak som o tom uvažoval len z toho dôvodu, aby sa
			// rozšírili možnosti skriptovania a medzi aliasy príkazov
			// skriptov som to pridal.)


		/** <p><a class="alias"></a> Alias pre {@link #označRiadok(int, Color...) označRiadok}.</p> */
		public int oznacRiadok(int index, Color... farby)
		{ return označRiadok(index, farby); }

		/** <p><a class="alias"></a> Alias pre {@link #označVýpis(int, int, Color...) označVýpis}.</p> */
		public int oznacVypis(int riadok, int index, Color... farby)
		{ return označVýpis(riadok, index, farby); }

		/** <p><a class="alias"></a> Alias pre {@link #označRiadky(int, int, Color...) označRiadky}.</p> */
		public int oznacRiadky(int prvýRiadok, int poslednýRiadok, Color... farby)
		{ return označRiadky(prvýRiadok, poslednýRiadok, farby); }

		/** <p><a class="alias"></a> Alias pre {@link #označVýpisy(int, int, int, int, Color...) označVýpisy}.</p> */
		public int oznacVypisy(int prvýRiadok, int prvýVýpis, int poslednýRiadok,
			int poslednýVýpis, Color... farby) { return označVýpisy(prvýRiadok,
			prvýVýpis, poslednýRiadok, poslednýVýpis, farby); }

		/** <p><a class="alias"></a> Alias pre {@link #označVšetkyTexty(Color...) označVšetkyTexty}.</p> */
		public int oznacVsetkyTexty(Color... farby) { return označVšetkyTexty(farby); }

			// Poznámka: Uvažoval som o pridaní aliasov: označTexty
			// a oznacTexty, ale potom som to zamietol. Označ texty?
			// Aké, ktoré? (Aj tak som o tom uvažoval len z toho
			// dôvodu, aby sa rozšírili možnosti skriptovania a medzi
			// aliasy príkazov skriptov som to pridal.)

		/** <p><a class="alias"></a> Alias pre {@link #rozsahOblastiOznačenia(int, int) rozsahOblastiOznačenia}.</p> */
		public int[] rozsahOblastiOznacenia(int riadok, int index)
		{ return rozsahOblastiOznačenia(riadok, index); }


		/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenieRiadka(int) zrušOznačenieRiadka}.</p> */
		public int zrusOznacenieRiadka(int index)
		{ return zrušOznačenieRiadka(index); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenieVýpisu(int, int) zrušOznačenieVýpisu}.</p> */
		public int zrusOznacenieVypisu(int riadok, int index)
		{ return zrušOznačenieVýpisu(riadok, index); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenieRiadkov(int, int) zrušOznačenieRiadkov}.</p> */
		public int zrusOznacenieRiadkov(int prvýRiadok, int poslednýRiadok)
		{ return zrušOznačenieRiadkov(prvýRiadok, poslednýRiadok); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenieVýpisov(int, int, int, int) zrušOznačenieVýpisov}.</p> */
		public int zrusOznacenieVypisov(int prvýRiadok, int prvýVýpis,
			int poslednýRiadok, int poslednýVýpis)
		{ return zrušOznačenieVýpisov(prvýRiadok,
			prvýVýpis, poslednýRiadok, poslednýVýpis); }

		/** <p><a class="alias"></a> Alias pre {@link #zrušOznačenieTextov() zrušOznačenieTextov}.</p> */
		public int zrusOznacenieTextov() { return zrušOznačenieTextov(); }

			// Poznámka: Uvažoval som o pridaní aliasov:
			// zrušOznačenieVšetkýchTextov a zrusOznacenieVsetkychTextov,
			// ale potom som to zamietol. Zdalo sa mi to byť príliš dlhé…
			// (Aj tak som o tom uvažoval len z toho dôvodu, aby sa
			// rozšírili možnosti skriptovania a medzi aliasy príkazov
			// skriptov som to pridal.)

		/**
		 * <p>Zistí či jestvuje označenie v rámci textov vnútornej konzoly.</p>
		 * 
		 * @return ak jestvuje aspoň jeden označený prvok v rámci textov
		 *     vnútornej konzoly, tak metóda vráti {@code valtrue}, inak
		 *     {@code valfalse}
		 */
		public boolean jestvujeOznačenie()
		{ return vnútornáKonzola.jestvujeOznačenie(); }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujeOznačenie() jestvujeOznačenie}.</p> */
		public boolean jestvujeOznacenie() { return jestvujeOznačenie(); }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujeOznačenie() jestvujeOznačenie}.</p> */
		public boolean existujeOznačenie() { return jestvujeOznačenie(); }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujeOznačenie() jestvujeOznačenie}.</p> */
		public boolean existujeOznacenie() { return jestvujeOznačenie(); }


	// Odsadenie sprava/zľava…

		/**
		 * <p><a class="getter"></a> Zistí (dočasnú) hodnotu požiadavky na
		 * nastavenie odsadenia prvého riadka najbližšie vypísaného odseku
		 * konzolového textu plátna (podlahy alebo stropu). Odsek je jeden
		 * dlhý zalamovaný riadok konzoly. Hodnota požiadavky na nastavenie
		 * odsadenia (prvého riadka) je platná vždy do najbližšieho použitia
		 * príkazov určených na vypísanie textu na vnútornú konzolu
		 * (napríklad {@link #vypíš(Object[]) vypíš} alebo {@link 
		 * #vypíšRiadok(Object[]) vypíšRiadok}). V okamihu jej použitia sa
		 * hodnota aplikuje a spotrebuje. (Ďalší odsek už nebude mať odsadený
		 * prvý riadok. To je rozdiel pri porovnaní požiadaviek na zmenu
		 * odsadzovania celých odsekov {@linkplain #zmeňOdsadenieZľava()
		 * zľava} alebo {@linkplain #zmeňOdsadenieSprava() sprava}.) Ak nie
		 * je aktívna žiadna požiadavka na zmenu odsadenia, tak táto metóda
		 * vráti hodnotu {@code valnull}.</p>
		 * 
		 * @return aktuálna hodnota požiadavky na zmenu odsadenia prvého
		 *     riadka textov plátna zľava alebo {@code valnull}
		 * 
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public Integer odsadeniePrvéhoRiadka()
		{ return vnútornáKonzola.odsadeniePrvéhoRiadka; }

		/** <p><a class="alias"></a> Alias pre {@link #odsadeniePrvéhoRiadka() odsadeniePrvéhoRiadka}.</p> */
		public Integer odsadeniePrvehoRiadka()
		{ return vnútornáKonzola.odsadeniePrvéhoRiadka; }

		/**
		 * <p><a class="setter"></a> Nastaví novú (dočasnú) hodnotu požiadavky
		 * na zmenu odsadenia prvého riadka (v zmysle riadka odseku)
		 * konzolových textov plátna (podlahy alebo stropu). Platí to isté ako
		 * je uvedené v opise metódy
		 * {@link #odsadeniePrvéhoRiadka() odsadeniePrvéhoRiadka()}. Ak je
		 * potrebné požiadavku zrušiť, stačí zadať do parametra
		 * {@code novéOdsadenie} hodnotu {@code valnull}.</p>
		 * 
		 * @param novéOdsadenie nová požiadavka na zmenu odsadenia
		 *     textov konzoly zľava
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public void odsadeniePrvéhoRiadka(Integer novéOdsadenie)
		{ vnútornáKonzola.odsadeniePrvéhoRiadka = novéOdsadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #odsadeniePrvéhoRiadka(Integer) odsadeniePrvéhoRiadka}.</p> */
		public void odsadeniePrvehoRiadka(Integer novéOdsadenie)
		{ vnútornáKonzola.odsadeniePrvéhoRiadka = novéOdsadenie; }

		/**
		 * <p><a class="getter"></a> Zistí (dočasnú) hodnotu <b>požiadavky na
		 * zmenu</b> odsadenia konzolových textov plátna (podlahy alebo
		 * stropu) zľava. Hodnota <b>požiadavky na zmenu</b> odsadenia je
		 * platná vždy do najbližšieho použitia príkazov určených na vypísanie
		 * textu na vnútornú konzolu (napríklad {@link #vypíš(Object[]) vypíš}
		 * alebo {@link #vypíšRiadok(Object[]) vypíšRiadok}). V okamihu jej
		 * použitia sa hodnota aplikuje a spotrebuje. To, že sa spotrebuje
		 * hodnota <b>požiadavky na zmenu</b> znamená, že už nebude
		 * zistiteľná (merateľná) touto metódou, samotné odsadenie však
		 * zostáva aktívne pre všetky ďalšie výpisy textov na konzolu. Ak
		 * nie je aktívna žiadna <b>požiadavka na zmenu</b> odsadenia, tak
		 * táto metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @return aktuálna hodnota požiadavky na zmenu odsadenia textov
		 *     plátna zľava alebo {@code valnull}
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public Integer zmeňOdsadenieZľava()
		{ return vnútornáKonzola.zmenaOdsadeniaZľava; }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňOdsadenieZľava() zmeňOdsadenieZľava}.</p> */
		public Integer zmenOdsadenieZlava()
		{ return vnútornáKonzola.zmenaOdsadeniaZľava; }

		/**
		 * <p><a class="setter"></a> Nastaví novú (dočasnú) hodnotu požiadavky
		 * na zmenu odsadenia konzolových textov plátna (podlahy alebo stropu)
		 * zľava. Platí to isté ako je uvedené v opise metódy
		 * {@link #zmeňOdsadenieZľava() zmeňOdsadenieZľava()}. Ak je
		 * potrebné požiadavku zrušiť, stačí zadať do parametra
		 * {@code novéOdsadenie} hodnotu {@code valnull}.</p>
		 * 
		 * @param novéOdsadenie nová požiadavka na zmenu odsadenia
		 *     textov konzoly zľava
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public void zmeňOdsadenieZľava(Integer novéOdsadenie)
		{ vnútornáKonzola.zmenaOdsadeniaZľava = novéOdsadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňOdsadenieZľava(Integer) zmeňOdsadenieZľava}.</p> */
		public void zmenOdsadenieZlava(Integer novéOdsadenie)
		{ vnútornáKonzola.zmenaOdsadeniaZľava = novéOdsadenie; }

		/**
		 * <p><a class="getter"></a> Zistí (dočasnú) hodnotu <b>požiadavky na
		 * zmenu</b> odsadenia konzolových textov plátna (podlahy alebo
		 * stropu) sprava. Hodnota <b>požiadavky na zmenu</b> odsadenia je
		 * platná vždy do najbližšieho použitia príkazov určených na vypísanie
		 * textu na vnútornú konzolu (napríklad {@link #vypíš(Object[]) vypíš}
		 * alebo {@link #vypíšRiadok(Object[]) vypíšRiadok}). V okamihu jej
		 * použitia sa hodnota aplikuje a spotrebuje. To, že sa spotrebuje
		 * hodnota <b>požiadavky na zmenu</b> znamená, že už nebude
		 * zistiteľná (merateľná) touto metódou, samotné odsadenie však
		 * zostáva aktívne pre všetky ďalšie výpisy textov na konzolu. Ak
		 * nie je aktívna žiadna <b>požiadavka na zmenu</b> odsadenia, tak
		 * táto metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @return aktuálna hodnota požiadavky na zmenu odsadenia textov
		 *     plátna sprava alebo {@code valnull}
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public Integer zmeňOdsadenieSprava()
		{ return vnútornáKonzola.zmenaOdsadeniaSprava; }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňOdsadenieSprava() zmeňOdsadenieSprava}.</p> */
		public Integer zmenOdsadenieSprava()
		{ return vnútornáKonzola.zmenaOdsadeniaSprava; }

		/**
		 * <p><a class="setter"></a> Nastaví novú (dočasnú) hodnotu požiadavky
		 * na zmenu odsadenia konzolových textov plátna (podlahy alebo stropu)
		 * sprava. Platí to isté ako je uvedené v opise metódy
		 * {@link #zmeňOdsadenieSprava() zmeňOdsadenieSprava()}. Ak je
		 * potrebné požiadavku zrušiť, stačí zadať do parametra
		 * {@code novéOdsadenie} hodnotu {@code valnull}.</p>
		 * 
		 * @param novéOdsadenie nová požiadavka na zmenu odsadenia
		 *     textov konzoly sprava
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public void zmeňOdsadenieSprava(Integer novéOdsadenie)
		{ vnútornáKonzola.zmenaOdsadeniaSprava = novéOdsadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #zmeňOdsadenieSprava(Integer) zmeňOdsadenieSprava}.</p> */
		public void zmenOdsadenieSprava(Integer novéOdsadenie)
		{ vnútornáKonzola.zmenaOdsadeniaSprava = novéOdsadenie; }


		/**
		 * <p><a class="getter"></a> Zistí (dočasnú) hodnotu <b>požiadavky na
		 * zmenu</b> odsadenia konzolových textov plátna (podlahy alebo
		 * stropu) pomocou virtuálneho tabulátora. Ak nie je aktívna žiadna
		 * <b>požiadavka na zmenu</b> odsadenia, tak táto metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p>Virtuálny tabulátor nie je v texte konzoly nijako reprezentovaný,
		 * preto ani nebude skopírovaný do schránky v prípade kopírovania
		 * textov konzoly do schránky. Ide o vnútorný príkaz, ktorý upraví
		 * pozíciu najbližšieho výpisu textu konzoly podľa požadovanej
		 * hodnoty.</p>
		 * 
		 * <p>Hodnota <b>požiadavky na zmenu</b> odsadenia tabulátorom je
		 * platná vždy do najbližšieho použitia príkazov určených na vypísanie
		 * textu na vnútornú konzolu (napríklad {@link #vypíš(Object[]) vypíš}
		 * alebo {@link #vypíšRiadok(Object[]) vypíšRiadok}). V okamihu jej
		 * použitia sa hodnota aplikuje a spotrebuje. To, že sa spotrebuje
		 * hodnota <b>požiadavky na zmenu</b> znamená, že už nebude
		 * zistiteľná (merateľná) touto metódou, samotné odsadenie však
		 * bude použité na tomto mieste pri výpise textov konzoly.</p>
		 * 
		 * @return aktuálna hodnota požiadavky na zmenu odsadenia textov
		 *     plátna pomocou virtuálneho tabulátora alebo {@code valnull}
		 * 
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public Integer virtuálnyTabulátor()
		{ return vnútornáKonzola.virtuálnyTabulátor; }

		/** <p><a class="alias"></a> Alias pre {@link #virtuálnyTabulátor() virtuálnyTabulátor}.</p> */
		public Integer virtualnyTabulator()
		{ return vnútornáKonzola.virtuálnyTabulátor; }

		/**
		 * <p><a class="setter"></a> Nastaví novú (dočasnú) hodnotu požiadavky
		 * na zmenu odsadenia konzolových textov plátna (podlahy alebo stropu)
		 * pomocou virtuálneho tabulátora.</p>
		 * 
		 * <p>Platí to isté ako je uvedené v opise metódy
		 * {@link #virtuálnyTabulátor() virtuálnyTabulátor()}. Ak je
		 * potrebné požiadavku zrušiť, stačí zadať do parametra
		 * {@code odsadenie} hodnotu {@code valnull}.</p>
		 * 
		 * @param odsadenie nová požiadavka na zmenu odsadenia
		 *     textov konzoly pomocou virtuálneho tabulátora
		 * 
		 * @see #virtuálnyTabulátor()
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #zamrazOdsadenie()
		 * @see #zamrazOdsadenie(boolean)
		 */
		public void virtuálnyTabulátor(Integer odsadenie)
		{ vnútornáKonzola.virtuálnyTabulátor = odsadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #virtuálnyTabulátor(Integer) virtuálnyTabulátor}.</p> */
		public void virtualnyTabulator(Integer odsadenie)
		{ vnútornáKonzola.virtuálnyTabulátor = odsadenie; }


		/**
		 * <p><a class="getter"></a> Zistí či bola zadaná požiadavka na
		 * nastavenie („zamrazenie“) ľavého odsadenia konzolových textov
		 * plátna (podlahy alebo stropu) na aktuálnej pozícii. Táto
		 * požiadavka sa pri najbližšom použití príkazov určených na
		 * vypísanie textu na vnútornú konzolu (napríklad
		 * {@link #vypíš(Object[]) vypíš} alebo {@link #vypíšRiadok(Object[])
		 * vypíšRiadok}) zapamätá a zresetuje. Použije sa v čase
		 * vykresľovania textov nad plátnom. Nastavenie sa použije tak, ako
		 * keby sme v čase vykresľovania textov zavolali metódu
		 * {@link #zmeňOdsadenieZľava(Integer) zmeňOdsadenieZľava(Integer)}
		 * s hodnotou aktuálnej horizontálnej pozície miesta výpisu na
		 * konzole (x-ovú súradnicu, pričom túto hodnotu vopred nie je možné
		 * zistiť, pretože je dynamicky sa meniaca a v čase vykreslenia
		 * textov môže byť prakticky ľubovoľná).</p>
		 * 
		 * @return {@code valtrue} ak je tento druh požiadavky aktívny, inak
		 *     {@code valfalse}
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie(boolean)
		 */
		public boolean zamrazOdsadenie()
		{ return vnútornáKonzola.zamrazOdsadenie; }

		/** <p><a class="alias"></a> Alias pre {@link #zamrazOdsadenie() zamrazOdsadenie}.</p> */
		public boolean zmrazOdsadenie()
		{ return vnútornáKonzola.zamrazOdsadenie; }

		/**
		 * <p><a class="setter"></a> Nastaví špeciálnu požiadavku na
		 * nastavenie („zamrazenie“) ľavého odsadenia konzolových textov
		 * plátna (podlahy alebo stropu) na aktuálnej pozícii textu. Platí
		 * to isté ako je uvedené v opise metódy {@link #zamrazOdsadenie()
		 * zamrazOdsadenie()}.</p>
		 * 
		 * @param zamraziť {@code valtrue} ak má byť táto požiadavka
		 *     aktivovaná, inak {@code valfalse}
		 * 
		 * @see #odsadeniePrvéhoRiadka()
		 * @see #odsadeniePrvéhoRiadka(Integer)
		 * @see #zmeňOdsadenieZľava()
		 * @see #zmeňOdsadenieZľava(Integer)
		 * @see #zmeňOdsadenieSprava()
		 * @see #zmeňOdsadenieSprava(Integer)
		 * @see #virtuálnyTabulátor()
		 * @see #virtuálnyTabulátor(Integer)
		 * @see #zamrazOdsadenie()
		 */
		public void zamrazOdsadenie(boolean zamraziť)
		{ vnútornáKonzola.zamrazOdsadenie = zamraziť; }

		/** <p><a class="alias"></a> Alias pre {@link #zamrazOdsadenie(boolean) zamrazOdsadenie}.</p> */
		public void zmrazOdsadenie(boolean zamraziť)
		{ vnútornáKonzola.zamrazOdsadenie = zamraziť; }

		/**
		 * <p>Nastaví okraje konzoly, ktoré obmedzia výpisy vnútornej konzoly na
		 * nimi vymedzenú oblasť.</p>
		 * 
		 * @param ľavýOkraj hodnota ľavého okraja oblasti textov konzoly
		 * @param hornýOkraj hodnota horného okraja oblasti textov konzoly
		 * @param pravýOkraj hodnota praho okraja oblasti textov konzoly
		 * @param dolnýOkraj hodnota dolného okraja oblasti textov konzoly
		 * 
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public void nastavOkrajeKonzoly(int ľavýOkraj, int hornýOkraj,
			int pravýOkraj, int dolnýOkraj)
		{
			boolean automatickéLišty = vnútornáKonzola.automatickéLišty;
			boolean zobrazZvislú = null != vnútornáKonzola.zvislá &&
				vnútornáKonzola.zvislá.isVisible();
			boolean zobrazVodorovnú = null != vnútornáKonzola.vodorovná &&
				vnútornáKonzola.vodorovná.isVisible();

			vnútornáKonzola.zobrazZvislúLištu(false);
			vnútornáKonzola.zobrazVodorovnúLištu(false);

			vnútornáKonzola.ľavýOkraj = ľavýOkraj;
			vnútornáKonzola.hornýOkraj = hornýOkraj;
			vnútornáKonzola.pravýOkraj = pravýOkraj;
			vnútornáKonzola.dolnýOkraj = dolnýOkraj;

			if (automatickéLišty)
			{
				vnútornáKonzola.automatickéZobrazovanie(true);
			}
			else if (zobrazZvislú || zobrazVodorovnú)
			{
				vnútornáKonzola.zobrazZvislúLištu(zobrazZvislú);
				vnútornáKonzola.zobrazVodorovnúLištu(zobrazVodorovnú);
			}
			else
			{
				Svet.hlavnýPanel.doLayout();
			}

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Vráti okraje konzoly v poli celočíselných hodnôt. Prvý prvok poľa
		 * určuje ľavý okraj, druhý horný, tretí pravý a posledný dolný.</p>
		 * 
		 * @return pole celočíselných hodnôt
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public int[] zistiOkrajeKonzoly()
		{
			return new int[] {vnútornáKonzola.ľavýOkraj,
				vnútornáKonzola.hornýOkraj, vnútornáKonzola.pravýOkraj,
				vnútornáKonzola.dolnýOkraj};
		}

		/**
		 * <p>Zapne alebo vypne automatické zobrazovanie líšt vzťahujúcich sa
		 * k rolovaniu textov vnútornej konzoly. Automatické zobrazovanie
		 * znamená, že zvislá alebo vodorovná lišta bude zobrazená v prípade,
		 * že objem textov vnútornej konzoly presiahne rozsah, ktorý môže byť
		 * zobrazený bez rolovania. Volanie metódy
		 * {@link #zobrazLištyKonzoly(boolean, boolean) zobrazLištyKonzoly}
		 * zruší automatické zobrazovanie líšt.</p>
		 * 
		 * @param zapnúť hodnota {@code valtrue} zanamená, že automatické
		 *     zobrazovanie líšt má byť zapnuté a hodnota {@code valfalse}
		 *     znamená opak
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public void automatickéZobrazovanieLíšt(boolean zapnúť)
		{
			vnútornáKonzola.automatickéZobrazovanie(zapnúť);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #automatickéZobrazovanieLíšt(boolean) automatickéZobrazovanieLíšt}.</p> */
		public void automatickeZobrazovanieList(boolean zapnúť)
		{ automatickéZobrazovanieLíšt(zapnúť); }

		/**
		 * <p>Zistí, či je zapnuté automatické zobrazovanie líšt vnútornej konzoly.</p>
		 * 
		 * @return {@code valtrue} ak je automatické zobrazovanie líšt zapnuté,
		 *     inak {@code valfalse}
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public boolean automatickéZobrazovanieLíšt()
		{
			return vnútornáKonzola.automatickéLišty;
		}

		/** <p><a class="alias"></a> Alias pre {@link #automatickéZobrazovanieLíšt() automatickéZobrazovanieLíšt}.</p> */
		public boolean automatickeZobrazovanieList()
		{ return automatickéZobrazovanieLíšt(); }

		/**
		 * <p>Jednotlivo zobrazí alebo skryje posuvné lišty previazané
		 * s výpismi vnútornej konzoly. Táto akcia zruší prípadné automatické
		 * zobrazovanie líšt. (Pozri metódu
		 * {@link #automatickéZobrazovanieLíšt(boolean)
		 * automatickéZobrazovanieLíšt}.)</p>
		 * 
		 * @param zvislá určuje, či má byť zobrazená zvislá lišta
		 * @param vodorovná určuje, či má byť zobrazená vodorovná lišta
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public void zobrazLištyKonzoly(boolean zvislá, boolean vodorovná)
		{
			vnútornáKonzola.zobrazZvislúLištu(zvislá);
			vnútornáKonzola.zobrazVodorovnúLištu(vodorovná);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #zobrazLištyKonzoly(boolean, boolean) zobrazLištyKonzoly}.</p> */
		public void zobrazListyKonzoly(boolean zvislá, boolean vodorovná)
		{ zobrazLištyKonzoly(zvislá, vodorovná); }

		/**
		 * <p>Overí viditeľnosť líšt konzoly a vráti údaje o ich viditeľnosti
		 * v poli booleovských hodnôt. Prvý prvok poľa sa vzťahuje na zvislú
		 * lištu a druhý na vodorovnú.</p>
		 * 
		 * @return pole celočíselných hodnôt
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #dajLištyKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public boolean[] zistiZobrazenieLíštKonzoly()
		{
			return new boolean[] {null != vnútornáKonzola.zvislá &&
				vnútornáKonzola.zvislá.isVisible(),
				null != vnútornáKonzola.vodorovná &&
				vnútornáKonzola.vodorovná.isVisible()};
		}

		/** <p><a class="alias"></a> Alias pre {@link #zistiZobrazenieLíštKonzoly() zistiZobrazenieLíštKonzoly}.</p> */
		public boolean[] zistiZobrazenieListKonzoly()
		{ return zistiZobrazenieLíštKonzoly(); }

		/**
		 * <p>Vráti pole, ktoré obsahuje buď inštancie rolovacích líšt konzoly
		 * (ak boli použité), alebo hodnoty {@code valnull} (pre tú lištu,
		 * ktorá nebola nikdy zobrazená). Prvý prvok poľa sa vzťahuje na zvislú
		 * lištu a druhý na vodorovnú.</p>
		 * 
		 * @return dvojprvkové pole, ktoré obsahuje buď inštancie typu
		 *     {@link RolovaciaLišta RolovaciaLišta} pre jednotlivé lišty,
		 *     alebo hodnoty {@code valnull}
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajRohLíštKonzoly()
		 */
		public RolovaciaLišta[] dajLištyKonzoly()
		{
			return new RolovaciaLišta[]
				{vnútornáKonzola.zvislá,
				vnútornáKonzola.vodorovná};
		}

		/** <p><a class="alias"></a> Alias pre {@link #dajLištyKonzoly() dajLištyKonzoly}.</p> */
		public RolovaciaLišta[] dajListyKonzoly()
		{ return dajLištyKonzoly(); }

		/**
		 * <p>Ak nastala situácia, že boli zobrazené obe lišty konzoly, tak
		 * táto metóda vráti inštanciu tlačidla, ktoré je vizuálne upravené
		 * tak, aby reprezentovalo roh medzi lištami konzoly (v čase, keď
		 * boli alebo sú zobrazené obe naraz). Inak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * @return buď inštancia typu {@link Tlačidlo Tlačidlo} alebo
		 *     hodnota {@code valnull}
		 * 
		 * @see #nastavOkrajeKonzoly(int, int, int, int)
		 * @see #zistiOkrajeKonzoly()
		 * @see #automatickéZobrazovanieLíšt(boolean)
		 * @see #automatickéZobrazovanieLíšt()
		 * @see #zobrazLištyKonzoly(boolean, boolean)
		 * @see #zistiZobrazenieLíštKonzoly()
		 * @see #dajLištyKonzoly()
		 */
		public Tlačidlo dajRohLíštKonzoly()
		{ return vnútornáKonzola.roh; }

		/** <p><a class="alias"></a> Alias pre {@link #dajRohLíštKonzoly() dajRohLíštKonzoly}.</p> */
		public Tlačidlo dajRohListKonzoly()
		{ return dajRohLíštKonzoly(); }


	// Priehľadnosť

		/**
		 * <p><a class="getter"></a> Zistí aktuálnu úroveň (ne)priehľadnosti
		 * tohto plátna.</p>
		 * 
		 * @return aktuálna úroveň priehľadnosti tohto plátna
		 * 
		 * @see #priehľadnosť(double)
		 * @see #upravPriehľadnosť(double)
		 * @see Svet#priehľadnosť(double, double)
		 * @see Svet#upravPriehľadnosť(double, double)
		 */
		public double priehľadnosť() { return priehľadnosť; }

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť() priehľadnosť}.</p> */
		public double priehladnost() { return priehľadnosť; }

		/**
		 * <p><a class="setter"></a> Nastaví novú úroveň (ne)priehľadnosti
		 * tohto plátna, pričom jednotlivé body plátna môžu mať svoju vlastnú
		 * úroveň priehľadnosti. Úroveň 0.0 znamená, že obsah plátna nebude
		 * zobrazený. Úroveň 1.0 znamená, že jednotlivé body plátna budú
		 * zobrazené s ich vlastnou úrovňou priehľadnosti – nepriehľadné body
		 * budú plne viditeľné. Úroveň priehľadnosti plátna nemá vplyv na
		 * {@linkplain #uložObrázok(String) uloženie obsahu plátna}, iba na
		 * jeho zobrazenie a {@linkplain Svet#uložObrázok(String) uloženie
		 * obsahu sveta} (grafického obsahu).</p>
		 * 
		 * @param priehľadnosť nová úroveň priehľadnosti (0.0 – 1.0)
		 * 
		 * @see #priehľadnosť()
		 * @see #upravPriehľadnosť(double)
		 * @see Svet#priehľadnosť(double, double)
		 * @see Svet#upravPriehľadnosť(double, double)
		 */
		public void priehľadnosť(double priehľadnosť)
		{
			this.priehľadnosť = (float)priehľadnosť;
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť(double) priehľadnosť}.</p> */
		public void priehladnost(double priehľadnosť)
		{ priehľadnosť(priehľadnosť); }

		/**
		 * <p>Skopíruje úroveň (ne)priehľadnosti zo zadaného objektu.</p>
		 * 
		 * @param objekt objekt určujúci novú úroveň priehľadnosti
		 * 
		 * @see #priehľadnosť()
		 * @see #upravPriehľadnosť(double)
		 * @see Svet#priehľadnosť(double, double)
		 * @see Svet#upravPriehľadnosť(double, double)
		 */
		public void priehľadnosť(Priehľadnosť objekt)
		{
			this.priehľadnosť = (float)objekt.priehľadnosť();
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #priehľadnosť(Priehľadnosť) priehľadnosť}.</p> */
		public void priehladnost(Priehľadnosť objekt)
		{ priehľadnosť(objekt); }

		/**
		 * <p>Upraví úroveň (ne)priehľadnosti plátna. Pre viac informácií
		 * o priehľadnosti pozri {@link #priehľadnosť(double) priehľadnosť}.</p>
		 * 
		 * @param zmena hodnota, ktorou bude násobená aktuálna hodnota
		 *     priehľadnosti; príklady: 0.5 – priehľadnosť bude znížená
		 *     o polovicu, 2.0 – úroveň priehľadnosti bude zdvojnásobená
		 * 
		 * @see #priehľadnosť(double)
		 * @see #priehľadnosť()
		 * @see Svet#priehľadnosť(double, double)
		 * @see Svet#upravPriehľadnosť(double, double)
		 */
		public void upravPriehľadnosť(double zmena)
		{
			if (0 == priehľadnosť) priehľadnosť = 0.1f;
			else priehľadnosť *= (float)zmena;
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #upravPriehľadnosť(double) upravPriehľadnosť}.</p> */
		public void upravPriehladnost(double zmena)
		{ upravPriehľadnosť(zmena); }


	// Grafika a obrázok plátna

		/**
		 * <p>Vráti objekt typu {@link BufferedImage BufferedImage} obsahujúci
		 * aktuálny obrázok tohto plátna. Niekedy by mohlo byť potrebné
		 * pracovať priamo s obrázkom plátna. Na priamy prístup k nemu slúži
		 * táto metóda.
		 * Vrátený objekt typu {@link BufferedImage BufferedImage} nemá
		 * prispôsobený súradnicový priestor potrebám prostredia rámca – pracuje
		 * v rovnakom súradnicovom systéme ako všetky grafické objekty Javy –
		 * začiatok súradnicového systému sa nachádza vľavo hore a y-ové
		 * súradnice sú zrkadlovo prevrátené, to znamená, že y-ová súradnica
		 * stúpa smerom nadol.
		 * (O súradnicových priestoroch sa podrobnejšie píše napríklad
		 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
		 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
		 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
		 * v celej dokumentácii.)</p>
		 * 
		 * @return objekt typu {@link BufferedImage BufferedImage} – obrázok
		 *     plátna
		 * 
		 * @see #grafika()
		 */
		public BufferedImage obrázok() { return obrázokPlátna; }

		/** <p><a class="alias"></a> Alias pre {@link #obrázok() obrázok}.</p> */
		public BufferedImage obrazok() { return obrázokPlátna; }

		/**
		 * <p>Vráti objekt typu {@link Graphics2D Graphics2D} použiteľný na
		 * rozmanité kreslenie na plátno (napríklad v reakciách {@link 
		 * ObsluhaUdalostí#vymazanie() vymazanie} alebo {@link 
		 * ObsluhaUdalostí#prekreslenie() prekreslenie}). Robot má dostatok
		 * nástrojov (metód) na kreslenie. Ak potrebujete priamy prístup ku
		 * grafickému objektu plátna (napríklad ak by ste chceli využívať
		 * metódy triedy {@link Graphics2D Graphics2D}), použite na prístup
		 * k nemu túto metódu. Grafický objekt pracuje so súradnicovým
		 * priestorom Javy, podobne ako je to spomenuté pri {@linkplain 
		 * #obrázok() obrázku plátna}.</p>
		 * 
		 * @return objekt typu {@link Graphics2D Graphics2D} – grafika plátna
		 * 
		 * @see #obrázok()
		 */
		public Graphics2D grafika() { return grafikaPlátna; }

		/**
		 * <p>Zruší oblasť na obmedzenie kreslenia na toto plátno.</p>
		 * 
		 * @see GRobot#kresliVšade()
		 * @see #kresliDo(Shape)
		 * @see #nekresliDo(Shape)
		 */
		public void kresliVšade()
		{
			grafikaPlátna.setClip(null);
		}

		/** <p><a class="alias"></a> Alias pre {@link #kresliVšade() kresliVšade}.</p> */
		public void kresliVsade() { kresliVšade(); }

		/**
		 * <p>Obmedzí kreslenie na toto plátno na plochu zadaného útvaru
		 * ({@link Shape Shape}).</p>
		 * 
		 * <p>Robot disponuje množinou metód na kreslenie tvarov ({@link 
		 * GRobot#kružnica(double) kružnica}, {@link GRobot#elipsa(double,
		 * double) elipsa}, {@link GRobot#štvorec(double) štvorec}…), ktoré
		 * zároveň generujú tvary. Na ich použitie s touto metódou je dobré
		 * predtým kreslenie tvarov {@linkplain GRobot#nekresliTvary()
		 * zakázať} a neskôr opäť {@linkplain GRobot#kresliTvary() povoliť}.
		 * Metóda {@link GRobot#text(String) text} dokonca zákaz kreslenia
		 * tvarov požaduje, aby mohla vygenerovať tvar (bez zákazu má
		 * návratovú hodnotu {@code valnull}). Tvar je možné vytvoriť aj
		 * z {@linkplain GRobot#cesta() cesty}…</p>
		 * 
		 * <p>Obmedzenie zužuje aktuálny priestor kreslenia, to znamená, že
		 * sa priebežne vytvára oblasť, ktorá je prienikom všetkých
		 * obmedzení. Ak chceme vytvoriť obmedzenie tvaru, ktorý je
		 * možné vytvoriť inou množinovou operáciou, môžeme na obmedzenie
		 * kreslenia použiť {@link Oblasť Oblasť} (zadanú namiesto
		 * parametra {@code tvar}). Obmedzenia sú platné pre všetky
		 * roboty a zrušíme ich volaním metódy {@link #kresliVšade()
		 * kresliVšade}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri takomto orezávaní nie
		 * je na všetkých platformách a/alebo implementáciách virtuálneho
		 * stroja Javy dostupná funkcia anti-aliasingu, čo zjednodušene
		 * povedané znamená, že okraje orezanej kresby môžu byť „zúbkaté.“
		 * Ak sa chcete tejto nedokonalosti vyhnúť, použite radšej funkciu
		 * {@linkplain #použiMasku masky}. Tá dovoľuje ovplyvňovať
		 * úroveň priehľadnosti s jemnosťou na jednotlivé body rastra.
		 * (<b>Poznámka:</b> Naopak, pri {@linkplain GRobot#svgExport
		 * exporte} kreslenia do inštancie {@link SVGPodpora SVGPodpora}
		 * je výhodnejšie pracovať s orezávaním – čiže práve s touto
		 * metódou alebo jej variantmi.)</p>
		 * 
		 * @param tvar tvar ({@link Shape Shape}) alebo {@link Oblasť
		 *     Oblasť}
		 * 
		 * @see GRobot#kresliDo(Shape)
		 * @see #kresliVšade()
		 * @see #nekresliDo(Shape)
		 */
		public void kresliDo(Shape tvar)
		{
			if (null != tvar) grafikaPlátna.clip(tvar);
		}

		// <stroke>Pomocná súkromná metóda na vytvorenie
		// negatívneho orezania oblasti</stroke>

		/**
		 * <p>Vytvára obmedzenie kreslenia na toto plátno.
		 * Funguje rovnako ako metóda {@link #kresliDo(Shape)
		 * kresliDo}, ibaže obrátene – kreslenie je možné všade, okrem
		 * zadaného tvaru alebo {@linkplain Oblasť oblasti}.</p>
		 * 
		 * <p>Rovnako ako pri metóde {@link #kresliDo(Shape) kresliDo}, sa
		 * aj toto obmedzenie kombinuje s aktuálnymi obmedzeniami kreslenia
		 * a je platné pre všetky roboty. Všetky ombedzenia zrušíme
		 * volaním metódy {@link #kresliVšade() kresliVšade}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pri takomto orezávaní nie
		 * je na všetkých platformách a/alebo implementáciách virtuálneho
		 * stroja Javy dostupná funkcia anti-aliasingu, čo zjednodušene
		 * povedané znamená, že okraje orezanej kresby môžu byť „zúbkaté.“
		 * Ak sa chcete tejto nedokonalosti vyhnúť, použite radšej funkciu
		 * {@linkplain #použiMasku masky}. Tá dovoľuje ovplyvňovať
		 * úroveň priehľadnosti s jemnosťou na jednotlivé body rastra.
		 * (<b>Poznámka:</b> Naopak, pri {@linkplain GRobot#svgExport
		 * exporte} kreslenia do inštancie {@link SVGPodpora SVGPodpora}
		 * je výhodnejšie pracovať s orezávaním – čiže práve s touto
		 * metódou alebo jej variantmi.)</p>
		 * 
		 * @param tvar tvar ({@link Shape Shape}) alebo {@link Oblasť
		 *     Oblasť}
		 * 
		 * @see GRobot#nekresliDo(Shape)
		 * @see #kresliDo(Shape)
		 * @see #kresliVšade()
		 */
		public void nekresliDo(Shape tvar)
		{
			if (null != tvar)
			{
				Shape clip = grafikaPlátna.getClip();
				if (null == clip) clip = new Rectangle.
					Double(0, 0, šírkaPlátna, výškaPlátna);
				Area oblasťA = new Area(clip);
				Area oblasťB = new Area(tvar);
				oblasťA.subtract(oblasťB);
				grafikaPlátna.setClip(oblasťA);
			}
		}


	// Vnútorná konzola (zväčša práca s textami konzoly)

		/**
		 * <p>Príznak automatického rolovania textov vnútornej konzoly pomocou
		 * kolieska myši. Predvolená hodnota tohto príznaku je {@code valtrue}.
		 * Rovnomenná metóda ({@link #rolujTexty() rolujTexty}) slúži na
		 * okamžité vykonanie rolovania textov vnútornej konzoly podľa
		 * posledných hodnôt miery rolovania kolieskom myši a v prípade
		 * ponechania hodnoty {@code valtrue} v tomto príznaku je uvedená
		 * metóda volaná automaticky.</p>
		 * 
		 * @see #rolujTexty()
		 */
		public boolean rolujTexty = true;

		/**
		 * <p><a class="getter"></a> Zistí stav/hodnotu vlastnosti zalamovania
		 * textov vnútornej konzoly tohto plátna. Vlastnosť ovplyvňuje
		 * správanie metód {@link #vypíš(Object[]) vypíš} a {@link 
		 * #vypíšRiadok(Object[]) vypíšRiadok}. Predvolene je (aspoň
		 * v súvislosti s plátnom ako takým) táto vlastnosť vypnutá.</p>
		 * 
		 * @return {@code valtrue} ak sú texty presahujúce pravý okraj okna
		 *     automaticky zalamované; {@code valfalse} v opačnom prípade
		 */
		public boolean zalamujeTexty() { return vnútornáKonzola.zalamujTexty; }

		/**
		 * <p><a class="setter"></a> Nastaví vlastnosť zalamovania textov
		 * vnútornej konzoly tohto plátna podľa zadanej hodnoty parametra.
		 * Vlastnosť ovplyvňuje správanie metód {@link #vypíš(Object[]) vypíš}
		 * a {@link #vypíšRiadok(Object[]) vypíšRiadok}. Predvolene je (aspoň
		 * v súvislosti s plátnom ako takým) táto vlastnosť vypnutá.</p>
		 * 
		 * @param zalamuj {@code valtrue} ak chceme, aby boli texty
		 *     presahujúce pravý okraj okna automaticky zalomené; {@code 
		 *     valfalse} v opačnom prípade
		 */
		public void zalamujTexty(boolean zalamuj)
		{
			vnútornáKonzola.zalamujTexty = zalamuj;
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Zapne zalamovanie textov vnútornej konzoly tohto plátna. Má
		 * rovnaký efekt ako keby sme volali metódu {@link 
		 * #zalamujTexty(boolean) zalamujTexty(true)}.</p>
		 */
		public void zalamujTexty() { zalamujTexty(true); }

		/**
		 * <p>Vypne zalamovanie textov vnútornej konzoly tohto plátna. Má
		 * rovnaký efekt ako keby sme volali metódu {@link 
		 * #zalamujTexty(boolean) zalamujTexty(false)}.</p>
		 */
		public void nezalamujTexty() { zalamujTexty(false); }


		/**
		 * <p>Zistí výšku riadka vnútornej konzoly pri použití aktuálneho
		 * typu písma. Ak metóda z nejakého dôvodu nedokáže získať použiteľný
		 * kontext grafiky, tak vráti nulu – {@code num0}.</p>
		 * 
		 * @return výška riadka pri použití aktuálneho písma
		 */
		public int výškaRiadka()
		{
			if (null != vnútornáKonzola.grafikaKonzoly)
			{
				vnútornáKonzola.grafikaKonzoly.setFont(
					vnútornáKonzola.aktuálnePísmo);
				return vnútornáKonzola.grafikaKonzoly.
					getFontMetrics().getHeight();
			}
			else if (null != grafikaPlátna)
			{
				grafikaPlátna.setFont(vnútornáKonzola.aktuálnePísmo);
				return grafikaPlátna.getFontMetrics().getHeight();
			}
			else if (null != Svet.grafikaSveta1)
			{
				Svet.grafikaSveta1.setFont(vnútornáKonzola.aktuálnePísmo);
				return Svet.grafikaSveta1.getFontMetrics().getHeight();
			}
			return 0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #výškaRiadka() výškaRiadka}.</p> */
		public int vyskaRiadka() { return výškaRiadka(); }

		/**
		 * <p>Zistí šírku zadaného reťazca v bodoch pri použití aktuálneho
		 * typu písma vnútornej konzoly. Ak metóda z nejakého dôvodu nedokáže
		 * získať použiteľný kontext grafiky, tak vráti nulu – {@code num0}.</p>
		 * 
		 * @param reťazec reťazec, ktorého šírka má byť vrátená
		 * @return šírka zadaného reťazca v bodoch pri použití aktuálneho
		 *     písma konzoly
		 */
		public int šírkaReťazca(String reťazec)
		{
			if (null != vnútornáKonzola.grafikaKonzoly)
			{
				vnútornáKonzola.grafikaKonzoly.setFont(
					vnútornáKonzola.aktuálnePísmo);
				return vnútornáKonzola.grafikaKonzoly.
					getFontMetrics().stringWidth(reťazec);
			}
			else if (null != grafikaPlátna)
			{
				grafikaPlátna.setFont(vnútornáKonzola.aktuálnePísmo);
				return grafikaPlátna.getFontMetrics().stringWidth(reťazec);
			}
			else if (null != Svet.grafikaSveta1)
			{
				Svet.grafikaSveta1.setFont(vnútornáKonzola.aktuálnePísmo);
				return Svet.grafikaSveta1.getFontMetrics().stringWidth(reťazec);
			}
			return 0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #šírkaReťazca(String) šírkaReťazca}.</p> */
		public int sirkaRetazca(String reťazec)
		{ return šírkaReťazca(reťazec); }

		/**
		 * <p>Zistí šírku medzery vnútornej konzoly pri použití aktuálneho typu
		 * písma. Ak metóda z nejakého dôvodu nedokáže získať použiteľný
		 * kontext grafiky, tak vráti nulu – {@code num0}.</p>
		 * 
		 * @return šírka medzery pri použití aktuálneho písma
		 */
		public int šírkaMedzery()
		{
			if (null != vnútornáKonzola.grafikaKonzoly)
			{
				vnútornáKonzola.grafikaKonzoly.setFont(
					vnútornáKonzola.aktuálnePísmo);
				return vnútornáKonzola.grafikaKonzoly.
					getFontMetrics().stringWidth(" ");
			}
			else if (null != grafikaPlátna)
			{
				grafikaPlátna.setFont(vnútornáKonzola.aktuálnePísmo);
				return grafikaPlátna.getFontMetrics().stringWidth(" ");
			}
			else if (null != Svet.grafikaSveta1)
			{
				Svet.grafikaSveta1.setFont(vnútornáKonzola.aktuálnePísmo);
				return Svet.grafikaSveta1.getFontMetrics().stringWidth(" ");
			}
			return 0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #šírkaMedzery() šírkaMedzery}.</p> */
		public int sirkaMedzery() { return šírkaMedzery(); }

		/**
		 * <p>Zistí najväčšiu šírku znaku vnútornej konzoly pri použití
		 * aktuálneho typu písma. Ak metóda z nejakého dôvodu nedokáže získať
		 * použiteľný kontext grafiky, tak vráti nulu – {@code num0}.</p>
		 * 
		 * @return najväčšia šírka znaku pri použití aktuálneho písma
		 */
		public int najväčšiaŠírkaZnaku()
		{
			if (null != vnútornáKonzola.grafikaKonzoly)
			{
				vnútornáKonzola.grafikaKonzoly.setFont(
					vnútornáKonzola.aktuálnePísmo);
				return vnútornáKonzola.grafikaKonzoly.
					getFontMetrics().getMaxAdvance();
			}
			else if (null != grafikaPlátna)
			{
				grafikaPlátna.setFont(vnútornáKonzola.aktuálnePísmo);
				return grafikaPlátna.getFontMetrics().getMaxAdvance();
			}
			else if (null != Svet.grafikaSveta1)
			{
				Svet.grafikaSveta1.setFont(vnútornáKonzola.aktuálnePísmo);
				return Svet.grafikaSveta1.getFontMetrics().getMaxAdvance();
			}
			return 0;
		}

		/** <p><a class="alias"></a> Alias pre {@link #najväčšiaŠírkaZnaku() najväčšiaŠírkaZnaku}.</p> */
		public int najvacsiaSirkaZnaku() { return najväčšiaŠírkaZnaku(); }


		/**
		 * <p>Vypíše sériu argumentov v tvare textu na plátne (podlahe alebo
		 * strope) ako aktívne slová identifikované zadaným identifikátorom.
		 * Ak je zoznam argumentov prázdny, vypíše sa namiesto nich zadaný
		 * identifikátor. (To znamená, že nie je povolené, ani odporúčané
		 * vypísať „prázdne aktívne slovo.“) V rôznych aspektoch výpisu textov
		 * na vnútornú konzolu funguje táto metóda podobne ako metóda
		 * {@link #vypíš(Object[]) vypíš} – platia pre ňu v podstate rovnaké
		 * pravidlá. Rozdiel je v tom, že výpis vykonaný touto metódou bude
		 * považovaný za takzvané aktívne slovo (resp. slová). Farbu
		 * textu v oblasti aktívneho slova je možné ovplyvňovať prostrednícvom
		 * reakcie {@link ObsluhaUdalostí#farbaAktívnehoSlova(String)
		 * farbaAktívnehoSlova}. To, či sa určitý bod nachádza v niektorej
		 * z oblastí aktívnych slov je možné overiť napríklad metódou
		 * {@link #bodVAktívnomSlove(double, double) bodVAktívnomSlove}
		 * (s variantom na overenie polohy myši v niektorom slove: {@link 
		 * #myšVAktívnomSlove() myšVAktívnomSlove}).
		 * S pomocou aktívnych slov je možné naprogramovať podobné správanie
		 * aké majú hypertextové odkazy webových stránok.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Tento príklad ukazuje najjednoduchší spôsob vytvorenia webového
		 * odkazu priamo v rámci textov konzoly. (Je to len jedna z možností
		 * využitia aktívneho slova.) Prejdením myšou nad odkaz sa zmení jeho
		 * farba a kurzor myši. Kliknutím na odkaz sa otvorí zadaná webová
		 * adresa (pozri v rámci príkladu).</p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} OtvorWebovýOdkaz {@code kwdextends} {@link GRobot GRobot}
			{
				{@code kwdprivate} OtvorWebovýOdkaz()
				{
					{@code comm// Rozmer plátna = rozmer nultého zariadenia (obrazovky).}
					{@code valsuper}({@link Svet Svet}.{@link Svet#šírkaZariadenia() šírkaZariadenia}(), {@link Svet Svet}.{@link Svet#výškaZariadenia() výškaZariadenia}());

					{@code comm// Skrytie hlavného robota:}
					{@link GRobot#skry() skry}();

					{@code comm// Vypnutie automatického prekresľovania:}
					{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

					{@code comm// Aktivovanie rozširujúcich funkcií konzoly stropu (v tomto}
					{@code comm// príklade je to zbytočné, ale keby sme potrebovali vkladať}
					{@code comm// viac textov, zišlo by sa to).}
					{@link Svet Svet}.{@link Svet#skratkyStropu(boolean) skratkyStropu}({@code valtrue});
					{@link Plátno strop}.{@link Plátno#automatickéZobrazovanieLíšt(boolean) automatickéZobrazovanieLíšt}({@code valtrue});

					{@code comm// Vloženie textov konzoly (vrátane aktívneho „slova“ – to môže}
					{@code comm// byť aj viac slov, ktoré bude fungovať ako webový odkaz):}
					{@link Svet Svet}.{@link Svet#farbaTextu(java.awt.Color) farbaTextu}({@link Farebnosť#čierna čierna});
					{@link Svet Svet}.{@link Svet#vypíš(Object...) vypíš}({@code srg"Toto: "});
					{@link Svet Svet}.{@link Svet#farbaTextu(java.awt.Color) farbaTextu}({@link Farebnosť#atramentová atramentová});
					{@link Svet Svet}.{@link Svet#vypíšAktívneSlovo(String, Object...) vypíšAktívneSlovo}({@code srg"https://pdf.truni.sk/"}, {@code srg"je webový odkaz."});
					{@link Svet Svet}.{@link Svet#farbaTextu(java.awt.Color) farbaTextu}({@link Farebnosť#čierna čierna});
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg" Kliknutím na neho otvoríte stránku "} +
						{@code srg"Pedagogickej fakulty TU."});

					{@code comm// Spustenie časovača, v ktorom bude zabezpečené prekresľovanie}
					{@code comm// plátna podľa potreby:}
					{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
				}
				<hr/>
				{@code comm// V reakcii na tik je prekresľované plátno v prípade potreby:}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}
				<hr/>
				{@code comm// Ďalšie reakcie, ktorými je oživené fungovanie odkazu:}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#pohybMyši() pohybMyši}()
				{
					{@link String String} aktívneSlovo = {@link Svet Svet}.{@link Svet#myšVAktívnomSlove() myšVAktívnomSlove}();
					{@code kwdif} ({@code valnull} == aktívneSlovo)
						{@link Svet Svet}.{@link Svet#zmeňKurzorMyši(String) zmeňKurzorMyši}({@code srg"predvolený"});
					{@code kwdelse}
						{@link Svet Svet}.{@link Svet#zmeňKurzorMyši(String) zmeňKurzorMyši}({@code srg"ruka"});
					{@link Svet Svet}.{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
				{
					{@link String String} aktívneSlovo = {@link Svet Svet}.{@link Svet#myšVAktívnomSlove() myšVAktívnomSlove}();
					{@code kwdif} ({@code valnull} != aktívneSlovo)
					{
						{@link String String} URL = aktívneSlovo;
						{@code comm// (prípadné spracovanie)}
						{@link Svet Svet}.{@link Svet#otvorWebovýOdkaz(String) otvorWebovýOdkaz}(URL);
					}
				}

				{@code kwd@}Override {@code kwdpublic} java.awt.{@link Color Color} {@link GRobot#farbaAktívnehoSlova(String) farbaAktívnehoSlova}({@link String String} slovo)
				{
					{@link String String} aktívneSlovo = {@link Svet Svet}.{@link Svet#myšVAktívnomSlove() myšVAktívnomSlove}();
					{@code kwdif} (slovo.{@link String#equals(Object) equals}(aktívneSlovo)) {@code kwdreturn} {@link Farebnosť#tmavoakvamarínová tmavoakvamarínová};
					{@code kwdreturn} {@code valnull};
				}
				<hr/>
				{@code comm// (Hlavná metóda – vstupný bod programu.)}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"OtvorWebovýOdkaz.cfg"});
					{@code kwdnew} OtvorWebovýOdkaz();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>otvor-webovy-odkaz.png<alt/>Ukážka okna s webovým
		 * odkazom.</image>Ukážka výsledného okna.</p>
		 * 
		 * @param identifikátor identifikátor aktívneho slova, pomocou
		 *     ktorého bude toto slovo odlišované od ostatných aktívnych slov
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami (ako pri metóde {@link #vypíš(Object[])
		 *     vypíš})
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #bodVAktívnomSlove(double, double)
		 * @see #myšVAktívnomSlove()
		 * @see ObsluhaUdalostí#farbaAktívnehoSlova(String)
		 */
		public void vypíšAktívneSlovo(
			String identifikátor, Object... argumenty)
		{
			vnútornáKonzola.identifikátorAktívnehoSlova = identifikátor;

			if (0 == argumenty.length)
				vypíš(identifikátor);
			else
				vypíš(argumenty);

			vnútornáKonzola.identifikátorAktívnehoSlova = null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public void vypisAktivneSlovo(String identifikátor, Object... argumenty)
		{ vypíšAktívneSlovo(identifikátor, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public void vypíšAktívneSlová(String identifikátor, Object... argumenty)
		{ vypíšAktívneSlovo(identifikátor, argumenty); }

		/** <p><a class="alias"></a> Alias pre {@link #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.</p> */
		public void vypisAktivneSlova(String identifikátor, Object... argumenty)
		{ vypíšAktívneSlovo(identifikátor, argumenty); }

		/**
		 * <p>Overí, či sa bod identifikovaný zadanými súradnicami nachádza
		 * v oblasti niektorého z aktívnych slov. (Pozri metódu {@link 
		 * #vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}.) Ak je
		 * na zadaných súradniciach nájdené aktívne slovo, tak je návratovou
		 * hodnotou tejto metódy jeho identifikátor, inak je vrátená
		 * hodnota {@code valnull}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return identifikátor aktívneho slova, v ktorom sa nachádza bod
		 *     so zadanými súradnicami alebo {@code valnull}
		 */
		public String bodVAktívnomSlove(double x, double y)
		{
			double nx = Svet.prepočítajX(x);
			double ny = Svet.prepočítajY(y);

			for (AktívneSlovo aktívneSlovo : vnútornáKonzola.aktívneSlová)
			{
				if (null != aktívneSlovo.hranice &&
					aktívneSlovo.hranice.contains(nx, ny))
					return aktívneSlovo.identifikátor;
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #bodVAktívnomSlove(double, double) bodVAktívnomSlove}.</p> */
		public String bodVAktivnomSlove(double x, double y) { return bodVAktívnomSlove(x, y); }

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
		public String bodVAktívnomSlove(Poloha poloha) { return bodVAktívnomSlove(poloha.polohaX(), poloha.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #bodVAktívnomSlove(Poloha) bodVAktívnomSlove}.</p> */
		public String bodVAktivnomSlove(Poloha poloha) { return bodVAktívnomSlove(poloha.polohaX(), poloha.polohaY()); }

		/**
		 * <p>Overí, či sa súradnice kurzora myši nachádzajú v oblasti niektorého
		 * z aktívnych slov. (Pozri metódu {@link #vypíšAktívneSlovo(String,
		 * Object[]) vypíšAktívneSlovo}.) Ak je na súradniciach myši nájdené
		 * aktívne slovo, tak je návratovou hodnotou tejto metódy jeho
		 * identifikátor, inak je vrátená hodnota {@code valnull}.</p>
		 * 
		 * @return identifikátor aktívneho slova, v ktorom sa nachádza
		 *     kurzor myši alebo {@code valnull}
		 */
		public String myšVAktívnomSlove()
		{
			return bodVAktívnomSlove(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #myšVAktívnomSlove() myšVAktívnomSlove}.</p> */
		public String mysVAktivnomSlove()
		{
			return bodVAktívnomSlove(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}


		/**
		 * <p>Zistí, či sa súradnice zadaného bodu nachádzajú v niektorom
		 * z blokov výpisov vnútornej konzoly tohto plátna. Ak áno, tak
		 * metóda vráti dvojprvkové celočíselné pole, ktorého prvý prvok
		 * bude určovať index riadka výpisu a druhý index bloku výpisu.
		 * Ak sa nenájde zhoda so žiadnym výpisom, tak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return dvojprvkové pole s indexami výpisu alebo {@code valnull}
		 */
		public int[] výpisNaBode(double x, double y)
		{
			double nx = Svet.prepočítajX(x);
			double ny = Svet.prepočítajY(y);
			int čísloRiadka = 0;

			for (RiadokKonzoly riadok : vnútornáKonzola.riadky)
			{
				int čísloVýpisu = 0;

				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;

					if (vnútornáKonzola.zalamujTexty)
					{
						if (null != obsah.slová)
							for (Slovo slovo : obsah.slová)
								if (null != slovo.hranice &&
									slovo.hranice.contains(nx, ny))
									return new int[] {čísloRiadka,
										čísloVýpisu};
					}
					else
					{
						if (null != obsah.hranice &&
							obsah.hranice.contains(nx, ny))
							return new int[] {čísloRiadka, čísloVýpisu};
					}

					++čísloVýpisu;
				}

				++čísloRiadka;
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #výpisNaBode(double, double) výpisNaBode}.</p> */
		public int[] vypisNaBode(double x, double y) { return výpisNaBode(x, y); }

		/**
		 * <p>Zistí, či sa súradnice zadaného bodu nachádzajú v niektorom
		 * z blokov výpisov vnútornej konzoly tohto plátna. Ak áno, tak
		 * metóda vráti dvojprvkové celočíselné pole, ktorého prvý prvok
		 * bude určovať index riadka výpisu a druhý index bloku výpisu.
		 * Ak sa nenájde zhoda so žiadnym výpisom, tak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * @param bod poloha vyšetrovaného bodu
		 * @return dvojprvkové pole s indexami výpisu alebo {@code valnull}
		 */
		public int[] výpisNaBode(Poloha bod)
		{ return výpisNaBode(bod.polohaX(), bod.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #výpisNaBode(Poloha) výpisNaBode}.</p> */
		public int[] vypisNaBode(Poloha bod) { return výpisNaBode(bod); }

		/**
		 * <p>Zistí, či sa súradnice myši nachádzajú v niektorom z blokov
		 * výpisov vnútornej konzoly tohto plátna. Ak áno, tak metóda
		 * vráti dvojprvkové celočíselné pole, ktorého prvý prvok bude
		 * určovať index riadka výpisu a druhý index bloku výpisu.
		 * Ak sa nenájde zhoda so žiadnym výpisom, tak metóda vráti
		 * hodnotu {@code valnull}.</p>
		 * 
		 * @return dvojprvkové pole s indexami výpisu alebo {@code valnull}
		 */
		public int[] výpisNaMyši()
		{
			return výpisNaBode(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #výpisNaMyši() výpisNaMyši}.</p> */
		public int[] vypisNaMysi() { return výpisNaMyši(); }


		/**
		 * <p>Nájde indexy najbližšieho bloku konzoly k zadaným súradniciam
		 * bodu. Metóda vráti indexy vo forme dvojprvkového celočíselného
		 * poľa – prvý prvok bude určovať index riadka a druhý index bloku
		 * výpisu. V prípade zlyhania vráti metóda hodnotu {@code valnull}.</p>
		 * 
		 * @param x x-ová súradnica vyšetrovaného bodu
		 * @param y y-ová súradnica vyšetrovaného bodu
		 * @return dvojprvkové pole s indexami výpisu (prípadne
		 *     {@code valnull})
		 */
		public int[] výpisPriBode(double x, double y)
		{
			double nx = Svet.prepočítajX(x);
			double ny = Svet.prepočítajY(y);
			int čísloRiadka = 0;

			int najbližšíRiadok = -1, najbližšíVýpis = -1;
			double poslednáVzdialenosť = -1.0;

			for (RiadokKonzoly riadok : vnútornáKonzola.riadky)
			{
				int čísloVýpisu = 0;

				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;

					if (vnútornáKonzola.zalamujTexty)
					{
						if (null != obsah.slová)
							for (Slovo slovo : obsah.slová)
								if (null != slovo.hranice)
								{
									if (slovo.hranice.contains(nx, ny))
										return new int[] {čísloRiadka,
											čísloVýpisu};

									double vzdialenosť = Math.hypot(nx -
										slovo.hranice.getX() -
										slovo.hranice.getWidth() / 2.0, ny -
										slovo.hranice.getY() -
										slovo.hranice.getHeight() / 2.0);

									if (0 > poslednáVzdialenosť ||
										poslednáVzdialenosť > vzdialenosť)
									{
										poslednáVzdialenosť = vzdialenosť;
										najbližšíRiadok = čísloRiadka;
										najbližšíVýpis = čísloVýpisu;
									}
								}
					}
					else
					{
						if (null != obsah.hranice)
						{
							if (obsah.hranice.contains(nx, ny))
								return new int[] {čísloRiadka, čísloVýpisu};

							double vzdialenosť = Math.hypot(nx -
								obsah.hranice.getX() -
								obsah.hranice.getWidth() / 2.0, ny -
								obsah.hranice.getY() -
								obsah.hranice.getHeight() / 2.0);

							if (0 > poslednáVzdialenosť ||
								poslednáVzdialenosť > vzdialenosť)
							{
								poslednáVzdialenosť = vzdialenosť;
								najbližšíRiadok = čísloRiadka;
								najbližšíVýpis = čísloVýpisu;
							}
						}
					}

					++čísloVýpisu;
				}

				++čísloRiadka;
			}

			if (0 > poslednáVzdialenosť) return null;
			return new int[] {najbližšíRiadok, najbližšíVýpis};
		}

		/** <p><a class="alias"></a> Alias pre {@link #výpisPriBode(double, double) výpisPriBode}.</p> */
		public int[] vypisPriBode(double x, double y) { return výpisPriBode(x, y); }

		/**
		 * <p>Nájde indexy najbližšieho bloku konzoly k súradniciam zadaného
		 * bodu. Metóda vráti indexy vo forme dvojprvkového celočíselného
		 * poľa – prvý prvok bude určovať index riadka a druhý index bloku
		 * výpisu. V prípade zlyhania vráti metóda hodnotu {@code valnull}.</p>
		 * 
		 * @param bod poloha vyšetrovaného bodu
		 * @return dvojprvkové pole s indexami výpisu
		 *     (prípadne {@code valnull})
		 */
		public int[] výpisPriBode(Poloha bod)
		{ return výpisPriBode(bod.polohaX(), bod.polohaY()); }

		/** <p><a class="alias"></a> Alias pre {@link #výpisPriBode(Poloha) výpisPriBode}.</p> */
		public int[] vypisPriBode(Poloha bod) { return výpisPriBode(bod); }

		/**
		 * <p>Nájde indexy najbližšieho bloku konzoly k aktuálnym súradniciam
		 * myši. Metóda vráti indexy vo forme dvojprvkového celočíselného
		 * poľa – prvý prvok bude určovať index riadka a druhý index bloku
		 * výpisu. V prípade zlyhania vráti metóda hodnotu {@code valnull}.</p>
		 * 
		 * @return dvojprvkové pole s indexami výpisu (prípadne {@code valnull})
		 */
		public int[] výpisPriMyši()
		{
			return výpisPriBode(
				ÚdajeUdalostí.súradnicaMyšiX,
				ÚdajeUdalostí.súradnicaMyšiY);
		}

		/** <p><a class="alias"></a> Alias pre {@link #výpisPriMyši() výpisPriMyši}.</p> */
		public int[] vypisPriMysi() { return výpisPriMyši(); }


		/**
		 * <p>Vypíše sériu argumentov v tvare textu na plátne (podlahe alebo
		 * strope). Texty sú vypísané aktuálnou {@linkplain 
		 * #farbaTextu(Color) farbou textov} plátna (predvolene
		 * tmavomodrá) a aktuálnym {@linkplain #písmo(Font) písmom}
		 * plátna. Ak je nastavená {@linkplain 
		 * #farbaPozadiaTextu(Color) farba pozadia textov}, tak je
		 * podklad písma zafarbený na túto farbu. Predvolene nie je nastavená
		 * žiadna farba pozadia textov. Texty plátna (ako takého) nie sú
		 * automaticky zalamované, to znamená, že ak je text na riadku príliš
		 * dlhý, presiahne pravý okraj plátna. Toto správanie je možné zmeniť
		 * metódou {@link #zalamujTexty(boolean) zalamujTexty}. Avšak počas
		 * inicializácie {@linkplain Svet#hlavnýRobot() hlavného robota} je
		 * automaticky zapnuté zalamovanie textov stropu, takže pri bežnom
		 * používaní programovacieho rámca GRobot (čiže ak nie je používaná
		 * konzola podlahy, ale len stropu) netreba s uvedenou vlastnosťou
		 * manipulovať.</p>
		 * 
		 * <p>Ak oddeľujeme sériu argumentov čiarkami, metóda automaticky
		 * dopĺňa medzi jednotlivé argumenty medzery podľa zaužívaných
		 * pravidiel typografie. To znamená, že metóda rešpektuje znaky pred
		 * ktoré sa medzera nepridáva (ako {@code srg,}  
		 * {@code srg:}   {@code srg?}   {@code srg.}   {@code srg)}
		 * a podobne) a za ktoré sa nepridáva (znaky ako {@code srg(}  
		 * {@code srg„}   {@code srg"} a podobne). Toto správanie
		 * platí len v rámci jedného volania metódy (argumenty musia byť
		 * oddelené čiarkami). Dve (alebo viaceré) samostatné volania metódy
		 * idúce za sebou:</p>
		 * 
		 * <pre CLASS="example">
			{@link Plátno podlaha}.{@code currvypíš}({@code srg"Tu, za dvojbodkou:"});
			{@link Plátno podlaha}.{@code currvypíš}({@code srg"sa medzera neobjaví…"});
			</pre>
		 * žiadne automatické medzery nepridávajú.
		 * 
		 * <p>Výstup:</p>
		 * 
		 * <pre CLASS="example">Tu, za dvojbodkou:sa medzera neobjaví…
			</pre>
		 * 
		 * <p>Nasledujúce výpisy sú (takmer) ekvivalentné:</p>
		 * 
		 * <pre CLASS="example">
			{@code typeint} a = {@code num10};  {@code comm// pre potreby ukážky}
			{@link Plátno podlaha}.{@code currvypíš}({@code srg"Hodnota:"}, a, {@link Konštanty#riadok riadok});
			{@link Plátno podlaha}.{@link #vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hodnota:"}, a);
			{@link Plátno podlaha}.{@link #vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hodnota: "} + a);
			</pre>
		 * 
		 * <p>Výstup:</p>
		 * 
		 * <pre CLASS="example">
			Hodnota: 10
			Hodnota: 10
			Hodnota: 10
			</pre>
		 * 
		 * <p>V prípade, že chceme úplne zabrániť automatickému pridávaniu
		 * medzier (neodporúčame), použijeme namiesto oddeľovania argumentov
		 * čiarkami operátor zlučovania reťazcov {@code +} (posledný riadok
		 * vo vyššie uvedenom príklade). Avšak použitie tohto operátora
		 * spôsobí, že číselné hodnoty budú formátované s použitím systémových
		 * pravidiel. Zmeniť to je možné formátovaním číselnej hodnoty
		 * inštanciou {@link Svet#formát Svet.formát}:
		 * {@link Svet Svet}{@code .}{@link Svet#formát formát}{@code .}{@link 
		 * NumberFormat#format(double) format}{@code (}{@code num10.3}{@code 
		 * ) + …}</p>
		 * 
		 * <p>Ak sú číselné hodnoty zadávané ako samostatné argumenty (t. j.
		 * sú oddelené čiarkami), tak sú naformátované automaticky. Znak
		 * použitý na oddelenie desatinných miest naformátovaných čísiel je
		 * možné zmeniť volaním metódy {@link 
		 * Svet#oddeľovačDesatinnejČasti(char) oddeľovačDesatinnejČasti}.
		 * Použitie znaku na oddelenie tisícov je možné upraviť (vypnúť,
		 * zmeniť) volaním metódy {@link Svet#oddeľovačTisícov(Character)
		 * oddeľovačTisícov}. Ak je argumentom tejto metódy ({@code currvypíš})
		 * jednorozmerné pole číselného (alebo logického) údajového typu, tak
		 * metóda vypíše jeho obsah, pričom na oddelenie prvkov použije
		 * oddeľovač, ktorý je možné upraviť volaním metódy {@link 
		 * Svet#oddeľovačPrvkovPoľa(String) oddeľovačPrvkovPoľa}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b>
		 * Prvý výpis (prvé spustenie ľubovoľného príkazu výpisu na konzolu)
		 * vždy automaticky skryje {@linkplain Svet#hlavnýRobot() hlavný
		 * robot}. Dôvodom je úsilie o automatické odlíšenie konzolovo
		 * orientovaných úloh od graficky orientovaných úloh. (Je malá šanca,
		 * že bude úloha orientovaná tak, aby vyžadovala ponechanie
		 * zobrazeného {@linkplain Svet#hlavnýRobot() hlavného robota}
		 * a zároveň pracovala s konzolovými výpismi.) Toto správanie je
		 * v prípade potreby možné zmeniť – automatickému skrytiu
		 * {@linkplain Svet#hlavnýRobot() hlavného robota} sa dá zamedziť
		 * jeho „poslatím“ do úplne prvého výpisu vnútornej konzoly –
		 * napríklad {@link Plátno podlaha}{@code .}{@code 
		 * currvypíš}{@code (}{@link Svet Svet}{@code .}{@link 
		 * Svet#hlavnýRobot() hlavnýRobot}{@code ());} (samozrejme, že
		 * v konštruktore {@linkplain Svet#hlavnýRobot() hlavného robota}
		 * môžeme namiesto príkazu {@link Svet#hlavnýRobot() Svet.hlavnýRobot}
		 * s výhodou použiť hodnotu {@code valthis}).</p>
		 * 
		 * <p>Konzola spracúva aj znaky spätného vymazania znaku (angl.
		 * backspace, ASCII {@code num8}, úniková (escape) sekvencia {@code 
		 * srg"\b"}). Funkciou tohto riadiaceho znaku je vymazanie
		 * predchádzajúceho znaku. Vo vnútornej konzole je tento riadiaci
		 * kód spracúvaný najviac v rámci šírky jedného riadka konzoly.</p>
		 * 
		 * <p>Od verzie 2.21 je možné zmeniť správanie tejto a príbuzných
		 * metód v súvislosti so spôsobom spracovania inštancií farieb, ktoré
		 * sa vyskytnú v zozname argumentov. Podrobnosti sú v opise metódy
		 * {@link #nevypisujFarby(boolean) nevypisujFarby(nevypisuj)}.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 * 
		 * @see #vypíšAktívneSlovo(String, Object[])
		 *     vypíšAktívneSlovo(String, Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #predvolenáFarbaTextu()
		 * @see #farbaPozadiaTextu(Color)
		 * @see #predvolenáFarbaPozadiaTextu()
		 * @see #písmo(Font)
		 * @see #predvolenéPísmo()
		 * @see #vymaž()
		 * @see #vymažTexty()
		 * @see GRobot#text(String)
		 * @see Svet#formát
		 * @see Svet#F(double, int)
		 * @see Svet#oddeľovačDesatinnejČasti(char)
		 * @see Svet#oddeľovačTisícov(Character)
		 * @see Svet#oddeľovačPrvkovPoľa(String)
		 */
		public void vypíš(Object... argumenty)
		{
			vnútornáKonzola.vypíš(argumenty);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypíš(Object[]) vypíš}.</p> */
		public void vypis(Object... argumenty) { vypíš(argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na podlahe alebo strope
		 * a presunie sa na ďalší riadok. Ďalšie informácie pozri v opise
		 * metódy {@link #vypíš(Object[]) vypíš(Object...)}, ktorej správanie
		 * je, okrem prechodu na nový riadok, zhodné.</p>
		 * 
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 * 
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #farbaTextu(Color)
		 * @see #predvolenáFarbaTextu()
		 * @see #farbaPozadiaTextu(Color)
		 * @see #predvolenáFarbaPozadiaTextu()
		 * @see #písmo(Font)
		 * @see #predvolenéPísmo()
		 * @see #vymaž()
		 * @see #vymažTexty()
		 * @see GRobot#text(String)
		 * @see Svet#formát
		 */
		public void vypíšRiadok(Object... argumenty)
		{
			vypíš(argumenty);
			vnútornáKonzola.novýRiadok();
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypíšRiadok(Object[]) vypíšRiadok}.</p> */
		public void vypisRiadok(Object... argumenty) { vypíšRiadok(argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na zadaných súradniciach
		 * podlahy alebo stropu. Funguje podobne ako metóda {@link 
		 * #vypíš(Object[]) vypíš}, ibaže pred výpisom nastaví súradnice
		 * polohy výpisu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b>
		 * Po použití tejto metódy nie je (až do najbližšieho
		 * {@linkplain #vymažTexty() vymazania}) možné vykonávať
		 * {@linkplain #rolujTexty(int, int) rolovanie textov konzoly}.</p>
		 * 
		 * @param x x-ová súradnica polohy výpisu
		 * @param y y-ová súradnica polohy výpisu
		 * @param argumenty zoznam argumentov rôzneho údajového typu oddelený
		 *     čiarkami
		 */
		public void vypíšNa(double x, double y, Object... argumenty)
		{
			vnútornáKonzola.zmenaSúradníc = new Point2D.Double(
				Svet.prepočítajX(x), Svet.prepočítajY(y));
			vypíš(argumenty);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypíšNa(double, double, Object[]) vypíšNa}.</p> */
		public void vypisNa(double x, double y, Object... argumenty) { vypíšNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na zadaných súradniciach
		 * podlahy alebo stropu a presunie sa na ďalší riadok. Funguje podobne
		 * ako metóda {@link #vypíšRiadok(Object[]) vypíšRiadok}, ibaže pred
		 * výpisom nastaví súradnice polohy výpisu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b>
		 * Po použití tejto metódy nie je (až do najbližšieho
		 * {@linkplain #vymažTexty() vymazania}) možné vykonávať
		 * {@linkplain #rolujTexty(int, int) rolovanie textov konzoly}.</p>
		 * 
		 * @param x x-ová súradnica polohy výpisu
		 * @param y y-ová súradnica polohy výpisu
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 */
		public void vypíšRiadokNa(double x, double y, Object... argumenty)
		{
			vnútornáKonzola.zmenaSúradníc = new Point2D.Double(
				Svet.prepočítajX(x), Svet.prepočítajY(y));
			vypíšRiadok(argumenty);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vypíšRiadokNa(double, double, Object[]) vypíšRiadokNa}.</p> */
		public void vypisRiadokNa(double x, double y, Object... argumenty) { vypíšRiadokNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na zadaných súradniciach
		 * podlahy alebo stropu. Funguje podobne ako metóda {@link 
		 * #vypíš(Object[]) vypíš}, ibaže pred výpisom nastaví súradnice
		 * polohy výpisu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b>
		 * Po použití tejto metódy nie je (až do najbližšieho
		 * {@linkplain #vymažTexty() vymazania}) možné vykonávať
		 * {@linkplain #rolujTexty(int, int) rolovanie textov konzoly}.</p>
		 * 
		 * @param x x-ová súradnica polohy výpisu
		 * @param y y-ová súradnica polohy výpisu
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 */
		public void píšNa(double x, double y, Object... argumenty)
		{
			vnútornáKonzola.zmenaSúradníc = new Point2D.Double(
				Svet.prepočítajX(x), Svet.prepočítajY(y));
			vypíš(argumenty);
		}

		/** <p><a class="alias"></a> Alias pre {@link #píšNa(double, double, Object[]) píšNa}.</p> */
		public void pisNa(double x, double y, Object... argumenty) { píšNa(x, y, argumenty); }

		/**
		 * <p>Vypíše sériu argumentov v tvare textu na zadaných súradniciach
		 * podlahy alebo stropu a presunie sa na ďalší riadok. Funguje podobne
		 * ako metóda {@link #vypíšRiadok(Object[]) vypíšRiadok}, ibaže pred
		 * výpisom nastaví súradnice polohy výpisu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b>
		 * Po použití tejto metódy nie je (až do najbližšieho
		 * {@linkplain #vymažTexty() vymazania}) možné vykonávať
		 * {@linkplain #rolujTexty(int, int) rolovanie textov konzoly}.</p>
		 * 
		 * @param x x-ová súradnica polohy výpisu
		 * @param y y-ová súradnica polohy výpisu
		 * @param argumenty zoznam argumentov rôzneho údajového typu
		 *     oddelený čiarkami
		 */
		public void píšRiadokNa(double x, double y, Object... argumenty)
		{
			vnútornáKonzola.zmenaSúradníc = new Point2D.Double(
				Svet.prepočítajX(x), Svet.prepočítajY(y));
			vypíšRiadok(argumenty);
		}

		/** <p><a class="alias"></a> Alias pre {@link #píšRiadokNa(double, double, Object[]) píšRiadokNa}.</p> */
		public void pisRiadokNa(double x, double y, Object... argumenty) { píšRiadokNa(x, y, argumenty); }


		/**
		 * <p>Vráti v reťazci všetky texty, ktoré sa nachádzajú aktuálne
		 * vypísané na vnútornej konzole tohto plátna.</p>
		 * 
		 * @return textový obsah celej konzoly
		 */
		public String textKonzoly()
		{
			StringBuffer texty = new StringBuffer();
			vnútornáKonzola.prevezmiTexty(texty);
			return texty.toString();
		}

		/**
		 * <p>Vráti v reťazci všetky texty alebo všetky označené texty,
		 * ktoré sa nachádzajú aktuálne vypísané na vnútornej konzole
		 * tohto plátna v závislosti od hodnoty parametra
		 * {@code lenOznačený}.</p>
		 * 
		 * @param lenOznačený ak je hodnota tohto parametra {@code valtrue},
		 *     tak metóda vráti len označené texty konzoly, inak sa správa
		 *     rovnako ako metóda {@link #textKonzoly() textKonzoly()}
		 * @return textový obsah celej konzoly alebo len jej označených častí
		 */
		public String textKonzoly(boolean lenOznačený)
		{
			StringBuffer texty = new StringBuffer();
			vnútornáKonzola.prevezmiTexty(texty, lenOznačený);
			return texty.toString();
		}


		/**
		 * <p>Zistí aktuálny počet riadkov vypísaných vo vnútornej konzole
		 * tohto plátna.</p>
		 * 
		 * @return počet riadkov konzoly
		 */
		public int početRiadkov()
		{
			return vnútornáKonzola.riadky.size();
		}

		/** <p><a class="alias"></a> Alias pre {@link #početRiadkov() početRiadkov}.</p> */
		public int pocetRiadkov() { return početRiadkov(); }


		/**
		 * <p>Vráti textový obsah určeného riadka vypísaného v textovej
		 * konzole tohto plátna. Záporné hodnoty parametra určujú index
		 * od konca. Ak je index riadka mimo rozsahu, tak
		 * metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda (a jej podobné)
		 * sú vhodné na prevzatie jedného konkrétneho bloku textu. Na
		 * prevzatie série textov je vhodnejšie použiť označenie a prevziať
		 * označený text. (Pozrite príklad vo vnorenej triede {@link 
		 * Svet.PríkazovýRiadok Svet.PríkazovýRiadok}.)</p>
		 * 
		 * @param index index riadka, ktorého obsah má byť prevzatý
		 * @return obsah určeného riadka alebo {@code valnull}
		 */
		public String textRiadka(int index)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) return null;
			}
			if (index >= vnútornáKonzola.riadky.size()) return null;
			RiadokKonzoly riadok = vnútornáKonzola.riadky.get(index);
			StringBuffer texty = new StringBuffer();
			for (PrototypKonzoly prototyp : riadok)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (null != obsah.záloha)
					texty.append(obsah.záloha);
				else
					texty.append(obsah.obsah);
			}
			return texty.toString();
		}

		/**
		 * <p>Vráti textový obsah určeného riadka vnútornej konzoly tohto plátna
		 * alebo len jeho označených častí – podľa hodnoty parametra
		 * {@code lenOznačený}. (Ak je hodnota uvedeného parametra rovná
		 * {@code valtrue}, tak táto metóda vráti len označené časti riadka.)
		 * Ak je index riadka mimo rozsahu, tak metóda vráti hodnotu
		 * {@code valnull}. (Záporné hodnoty určujú index od konca.)</p>
		 * 
		 * @param index index riadka, ktorého obsah má byť prevzatý
		 * @param lenOznačený ak je hodnota tohto parametra {@code valtrue},
		 *     tak metóda vráti len označené časti riadka, inak sa správa
		 *     rovnako ako metóda {@link #textRiadka(int) textRiadka(index)}.
		 * @return obsah určeného riadka alebo len jeho označených častí,
		 *     podľa hodnoty parametra {@code lenOznačený}, prípadne hodnota
		 *     {@code valnull}
		 */
		public String textRiadka(int index, boolean lenOznačený)
		{
			if (lenOznačený)
			{
				if (index < 0)
				{
					index += vnútornáKonzola.riadky.size();
					if (index < 0) return null;
				}
				if (index >= vnútornáKonzola.riadky.size()) return null;
				RiadokKonzoly riadok = vnútornáKonzola.riadky.get(index);
				StringBuffer texty = new StringBuffer();
				for (PrototypKonzoly prototyp : riadok)
				{
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah || !obsah.označený) continue;
					if (null != obsah.záloha)
						texty.append(obsah.záloha);
					else
						texty.append(obsah.obsah);
				}
				return texty.toString();
			}
			else return textRiadka(index);
		}


		/**
		 * <p>Vymaže určený riadok z vnútornej konzoly tohto plátna.
		 * (Záporné hodnoty určujú index od konca.)</p>
		 * 
		 * @param index index riadka, ktorý má byť vymazaný
		 */
		public void vymažRiadok(int index)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) return;
			}
			if (index < vnútornáKonzola.riadky.size())
				vnútornáKonzola.riadky.remove(index);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažRiadok(int) vymažRiadok}.</p> */
		public void vymazRiadok(int index) { vymažRiadok(index); }


		/**
		 * <p>Vloží riadok určený zoznamom {@code argumenty} na pozíciu
		 * špecifikovanú číselným indexom – hodnota {@code num0} vloží riadok
		 * na prvú pozíciu. Záporné hodnoty určujú index od konca.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Použitie tejto metódy
		 * nemusí v niektorých prípadoch priniesť očákávaný výsledok. Metóda
		 * neresetuje odsadenie takým spôsobom ako sa to deje pri štandardnom
		 * kontinuálnom vypisovaní textov a nenastavuje automaticky farebnosť
		 * textov konzoly, takže texty výpisu predvolene prevezmú farebnosť
		 * od textov umiestnených pred nimi, podobne môžu byť od
		 * predchádzajúcich textov prevzaté ďalšie vlastnosti výpisov.</p>
		 * 
		 * @param index pozícia, na ktorú má byť vložený zadaný výpis
		 * @param argumenty výpis, ktorý môže byť rozdelený do viacerých argumentov
		 */
		public void vložRiadok(int index, Object... argumenty)
		{
			if (index < 0) index += vnútornáKonzola.riadky.size();
			RiadokKonzoly riadok = new RiadokKonzoly();
			vnútornáKonzola.riadky.add(riadok); vypíš(argumenty);
			if (index > 0 && index < vnútornáKonzola.riadky.size() - 1)
			{
				vnútornáKonzola.riadky.remove(riadok);
				vnútornáKonzola.riadky.add(index, riadok);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vložRiadok(int, Object...) vložRiadok}.</p> */
		public void vlozRiadok(int index, Object... argumenty)
		{ vložRiadok(index, argumenty); }


		/**
		 * <p>Zálohuje textový obsah vnútornej konzoly tohto plátna od
		 * určeného indexu riadka vyššie (chvost). Zálohu uloží do
		 * vnútornej pamäte konzoly a zároveň ju vráti vo forme objektu.
		 * Vrátený objekt je použiteľný napríklad s metódou {@link 
		 * #obnovTexty(Object) obnovTexty} alebo {@link 
		 * #pridajTexty(Object) pridajTexty(Object)}. (Záporné hodnoty
		 * určujú index riadka od konca. Podobne, ako to je pri ostatných
		 * metódach prijímajúcich index riadka.)</p>
		 * 
		 * @param index index, od ktorého má byť vykonaná záloha textov
		 * @return záloha chvosta, ktorá je použiteľná s metódami
		 *     {@link #obnovTexty(Object) obnovTexty},
		 *     {@link #pridajTexty(Object) pridajTexty}
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int, GRobot)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public Object zálohujChvost(int index)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) index = 0;
			}

			vnútornáKonzola.záloha = new ZálohaKonzoly();
			if (index < vnútornáKonzola.riadky.size())
				vnútornáKonzola.záloha.záloha.addAll(
					vnútornáKonzola.riadky.subList(index,
						vnútornáKonzola.riadky.size()));

			return vnútornáKonzola.záloha;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujChvost(int) zálohujChvost}.</p> */
		public void zalohujChvost(int index) { zálohujChvost(index); }


		/**
		 * <p>Zálohuje textový obsah vnútornej konzoly tohto plátna od
		 * určeného indexu riadka vyššie (chvost). Zálohu uloží do
		 * vnútornej pamäte zadaného robota, ktorý je po tejto akcii
		 * použiteľný s metódami {@link #obnovTexty(Object) obnovTexty}
		 * a {@link #pridajTexty(Object) pridajTexty(Object)}. (Záporné
		 * hodnoty určujú index riadka od konca. Podobne, ako to je pri
		 * ostatných metódach prijímajúcich index riadka.)</p>
		 * 
		 * @param index index, od ktorého má byť vykonaná záloha textov
		 * @param ktorý inštancia robota, do ktorej sa záloha uloží; inštancia
		 *     bude potom použiteľná s metódami
		 *     {@link #obnovTexty(Object) obnovTexty},
		 *     {@link #pridajTexty(Object) pridajTexty}
		 * 
		 * @see #zálohujGrafiku()
		 * @see #zálohujGrafiku(GRobot)
		 * @see #obnovGrafiku()
		 * @see #obnovGrafiku(Object)
		 * @see #pridajGrafiku(GRobot)
		 * @see #zálohujTexty()
		 * @see #zálohujTexty(GRobot)
		 * @see #zálohujChvost(int)
		 * @see #obnovTexty()
		 * @see #obnovTexty(Object)
		 * @see #pridajTexty(Object)
		 */
		public void zálohujChvost(int index, GRobot ktorý)
		{
			if (null == ktorý) return;
			if (null == ktorý.zálohaKonzoly)
				ktorý.zálohaKonzoly = new ZálohaKonzoly();
			else
				ktorý.zálohaKonzoly.záloha.clear();

			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) index = 0;
			}

			if (index < vnútornáKonzola.riadky.size())
				ktorý.zálohaKonzoly.záloha.addAll(
					vnútornáKonzola.riadky.subList(index,
						vnútornáKonzola.riadky.size()));
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujChvost(int, GRobot) zálohujChvost}.</p> */
		public void zalohujChvost(int index, GRobot ktorý)
		{ zálohujChvost(index, ktorý); }


		/**
		 * <p>Vymaže obsah vnútornej konzoly tohto plátna od určeného indexu
		 * nižšie. (Záporné hodnoty určujú index od konca.)</p>
		 * 
		 * @param index index, od ktorého má byť obsah konzoly vymazaný
		 */
		public void vymažChvost(int index)
		{
			if (index < 0)
			{
				index += vnútornáKonzola.riadky.size();
				if (index < 0) index = 0;
			}

			if (index < vnútornáKonzola.riadky.size())
				vnútornáKonzola.riadky.subList(index,
					vnútornáKonzola.riadky.size()).clear();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažChvost(int) vymažChvost}.</p> */
		public void vymazChvost(int index) { vymažChvost(index); }


		/**
		 * <p>Vráti počet výpisov (blokov, ktoré boli vypísané v rámci jedného
		 * volania metód {@link #vypíš(Object[])
		 * vypíš}/{@link #vypíšRiadok(Object[]) vypíšRiadok}) na určenom riadku.
		 * (Záporná hodnota parametra {@code riadok} určuje index riadka od
		 * konca – hodnota {@code num-1} určuje posledný riadok.)</p>
		 * 
		 * @param riadok index riadka, ktorého počet výpisov chceme zistiť
		 * @return počet výpisov uložených v určneom riadku
		 */
		public int početVýpisov(int riadok)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return 0;
			}
			if (riadok >= vnútornáKonzola.riadky.size()) return 0;
			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počet = 0;
			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null != obsah) ++počet;
			}
			return počet;
		}

		/** <p><a class="alias"></a> Alias pre {@link #početVýpisov(int) početVýpisov}.</p> */
		public int pocetVypisov(int riadok) { return početVýpisov(riadok); }


		/**
		 * <p>Vráti text určeného výpisu (bloku, ktorý bol vypísaný v rámci jedného
		 * volania metód {@link #vypíš(Object[])
		 * vypíš}/{@link #vypíšRiadok(Object[]) vypíšRiadok}) z určeného riadka.
		 * (Záporné hodnoty číselných parametrov určujú indexy od konca.)
		 * Ak zadané indexy nie sú platné, tak metóda vráti hodnotu
		 * {@code valnull}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda (a jej podobné)
		 * sú vhodné na prevzatie jedného konkrétneho bloku textu. Na
		 * prevzatie série textov je vhodnejšie použiť označenie a prevziať
		 * označený text. (Pozrite príklad vo vnorenej triede {@link 
		 * Svet.PríkazovýRiadok Svet.PríkazovýRiadok}.)</p>
		 * 
		 * @param riadok index riadka, z ktorého má byť prevzatý text
		 * @param index index výpisu v rámci riadka, ktorý má byť prevzatý
		 * @return text určeného výpisu z určeného riadka alebo {@code valnull}
		 */
		public String textVýpisu(int riadok, int index)
		{
			if (riadok < 0)
			{
				riadok += vnútornáKonzola.riadky.size();
				if (riadok < 0) return null;
			}
			if (riadok >= vnútornáKonzola.riadky.size()) return null;
			if (index < 0)
			{
				index += početVýpisov(riadok);
				if (index < 0) return null;
			}
			RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
			int počítadlo = 0;
			for (PrototypKonzoly prototyp : riadok2)
			{
				ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
				if (null == obsah) continue;
				if (index == počítadlo)
				{
					if (null != obsah.záloha)
						return obsah.záloha.toString();
					else
						return obsah.obsah.toString();
				}
				++počítadlo;
			}
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #textVýpisu(int, int) textVýpisu}.</p> */
		public String textVypisu(int riadok, int index)
		{ return textVýpisu(riadok, index); }

		/**
		 * <p>Vráti text určeného výpisu (bloku, ktorý bol vypísaný v rámci
		 * jedného volania metód {@link #vypíš(Object[])
		 * vypíš}/{@link #vypíšRiadok(Object[]) vypíšRiadok}) z určeného
		 * riadka. Ak je hodnota parametra {@code lenOznačený} rovná
		 * {@code valtrue}, tak metóda vráti obsah výpisu len ak je označený
		 * a v prípade žiadosti o text neoznačeného výpisu vráti prázdny
		 * reťazec. Záporné hodnoty číselných parametrov určujú polohu
		 * riadka alebo výpisu od konca. Ak zadané indexy nie sú platné,
		 * tak metóda vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param riadok index riadka, z ktorého má byť prevzatý text
		 * @param index index výpisu v rámci riadka, ktorý má byť prevzatý
		 * @param lenOznačený ak je hodnota tohto parametra {@code valtrue},
		 *     tak metóda vráti obsah výpisu len ak je označený, v opačnom
		 *     prípade vráti prázdny reťazec ({@code srg""}); ak je hodnota
		 *     tohto parametra {@code valfalse}, tak sa táto metóda správa
		 *     rovnako ako metóda {@link #textVýpisu(int, int)
		 *     textVýpisu(riadok, index)}
		 * @return text určeného výpisu, prázdny reťazec ({@code srg""})
		 *     alebo hodnota {@code valnull}
		 */
		public String textVýpisu(int riadok, int index, boolean lenOznačený)
		{
			if (lenOznačený)
			{
				if (riadok < 0)
				{
					riadok += vnútornáKonzola.riadky.size();
					if (riadok < 0) return null;
				}

				if (riadok >= vnútornáKonzola.riadky.size()) return null;

				if (index < 0)
				{
					index += početVýpisov(riadok);
					if (index < 0) return null;
				}

					RiadokKonzoly riadok2 = vnútornáKonzola.riadky.get(riadok);
					int počítadlo = 0;
					for (PrototypKonzoly prototyp : riadok2)
					{
						ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
						if (null == obsah) continue;
						if (index == počítadlo)
						{
							if (!obsah.označený)
								return "";
							else if (null != obsah.záloha)
								return obsah.záloha.toString();
							else
								return obsah.obsah.toString();
						}
						++počítadlo;
					}
					return null;

			}
			else return textVýpisu(riadok, index);
		}

		/** <p><a class="alias"></a> Alias pre {@link #textVýpisu(int, int, boolean) textVýpisu}.</p> */
		public String textVypisu(int riadok, int index, boolean lenOznačený)
		{ return textVýpisu(riadok, index, lenOznačený); }


	// Kreslenie obrázka

		/**
		 * <p>Nakreslí v strede plátna (podlahy alebo stropu) zadaný obrázok.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@link GRobot#podlaha podlaha}.{@code currobrázok}({@code srg"parkety.png"});
			{@link GRobot#strop strop}.{@code currobrázok}({@code srg"vzor.png"});
			</pre>
		 * 
		 * <p><b>Zdroje:</b></p>
		 * 
		 * <table class="centered"><tr>
		 * <td><p><image>parkety.png<alt/>Parkety.</image><a
		 * href="resources/parkety.png" target="_blank">parkety.png</a> –
		 * obrázok parkiet na prevzatie.</p></td>
		 * <td><p><image>vzor.png<alt/>Bodkovaný vzor.</image><a
		 * href="resources/vzor.png" target="_blank">vzor.png</a> –
		 * obrázok vzoru na prevzatie.</p></td></tr></table>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je súbor poškodený alebo nebol
		 *     nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void obrázok(String súbor)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			Image obrázok = Obrázok.súborNaObrázok(súbor);

			double prepočítanéX = Svet.prepočítajX(0);
			double prepočítanéY = Svet.prepočítajY(0);

			int šírkaObrázka = obrázok.getWidth(null);
			int výškaObrázka = obrázok.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok „" + súbor +
					"“ je poškodený!", "imageFileBroken", súbor);

			grafikaPlátna.drawImage(obrázok,
				(int)(prepočítanéX - (šírkaObrázka / 2.0)),
				(int)(prepočítanéY - (výškaObrázka / 2.0)), null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(String) obrázok}.</p> */
		public void obrazok(String súbor) { obrázok(súbor); }


		/**
		 * <p>Nakreslí na zadaných súradniciach na plátne (podlahe alebo
		 * strope) obrázok, pričom na zadaných súradniciach sa bude nachádzať
		 * ľavý horný roh obrázka. Táto metóda slúži na nakreslenie obrázka
		 * s čo najmenšou vnútornou komplikovanosťou (v súvislosti so
		 * spôsobom kreslenia). Napríklad kreslenie obrázkov so stredom na
		 * zadanej pozícii by vyžadovalo dodatočný výpočet. Ak chcete
		 * kresliť obrázky vystredené na zadanej pozícii, tak použite na
		 * nakreslenie niektorý robot. (To je najjednoduchší spôsob.)</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param x x-ová súradnica polohy obrázka
		 * @param y y-ová súradnica polohy obrázka
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je súbor poškodený alebo nebol
		 *     nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void obrázok(double x, double y, String súbor)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			Image obrázok = Obrázok.súborNaObrázok(súbor);

			double prepočítanéX = Svet.prepočítajX(x);
			double prepočítanéY = Svet.prepočítajY(y);

			int šírkaObrázka = obrázok.getWidth(null);
			int výškaObrázka = obrázok.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok „" + súbor +
					"“ je poškodený!", "imageFileBroken", súbor);

			grafikaPlátna.drawImage(obrázok,
				(int)prepočítanéX, (int)prepočítanéY, null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(double, double, String) obrázok}.</p> */
		public void obrazok(double x, double y, String súbor) { obrázok(x, y, súbor); }


		/**
		 * <p>Nakreslí na súradniciach zadaného objektu na plátne (podlahe alebo
		 * strope) obrázok, pričom na zadaných súradniciach sa bude nachádzať
		 * ľavý horný roh obrázka. Táto metóda slúži na nakreslenie obrázka
		 * s čo najmenšou vnútornou komplikovanosťou (v súvislosti so
		 * spôsobom kreslenia). Napríklad kreslenie obrázkov so stredom na
		 * zadanej pozícii by vyžadovalo dodatočný výpočet. Ak chcete
		 * kresliť obrázky vystredené na zadanej pozícii, tak použite na
		 * nakreslenie niektorý robot. (To je najjednoduchší spôsob.)</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
		 * 
		 * @param objekt objekt určujúci polohu kreslenia obrázka
		 * @param súbor názov súboru s obrázkom, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je súbor poškodený alebo nebol
		 *     nájdený
		 * 
		 * @see Svet#priečinokObrázkov(String)
		 */
		public void obrázok(Poloha objekt, String súbor)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			Image obrázok = Obrázok.súborNaObrázok(súbor);

			double prepočítanéX = Svet.prepočítajX(objekt.polohaX());
			double prepočítanéY = Svet.prepočítajY(objekt.polohaY());

			int šírkaObrázka = obrázok.getWidth(null);
			int výškaObrázka = obrázok.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok „" + súbor +
					"“ je poškodený!", "imageFileBroken", súbor);

			grafikaPlátna.drawImage(obrázok,
				(int)prepočítanéX, (int)prepočítanéY, null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Poloha, String) obrázok}.</p> */
		public void obrazok(Poloha objekt, String súbor) { obrázok(objekt, súbor); }


		/**
		 * <p>Nakreslí v strede plátna (podlahy alebo stropu) zadaný obrázok.</p>
		 * 
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je obrázok poškodený
		 */
		public void obrázok(Image obrázok)
		{
			Image relevantný = Obrázok.dajRelevantnýRaster(obrázok);

			double prepočítanéX = Svet.prepočítajX(0);
			double prepočítanéY = Svet.prepočítajY(0);

			int šírkaObrázka = relevantný.getWidth(null);
			int výškaObrázka = relevantný.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok je poškodený!",
					"imageBroken");

			if (relevantný instanceof Obrázok)
				((Obrázok)relevantný).
					kresliNaStred((int)prepočítanéX, (int)prepočítanéY, grafikaPlátna);
			else
				grafikaPlátna.drawImage(relevantný,
					(int)(prepočítanéX - (šírkaObrázka / 2.0)),
					(int)(prepočítanéY - (výškaObrázka / 2.0)), null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Image) obrázok}.</p> */
		public void obrazok(Image obrázok) { obrázok(obrázok); }


		/**
		 * <p>Nakreslí na zadaných súradniciach na plátne (podlahe alebo
		 * strope) obrázok, pričom na zadaných súradniciach sa bude nachádzať
		 * ľavý horný roh obrázka. Táto metóda slúži na nakreslenie obrázka
		 * s čo najmenšou vnútornou komplikovanosťou (v súvislosti so
		 * spôsobom kreslenia). Napríklad kreslenie obrázkov so stredom na
		 * zadanej pozícii by vyžadovalo dodatočný výpočet. Ak chcete
		 * kresliť obrázky vystredené na zadanej pozícii, tak použite na
		 * nakreslenie niektorý robot. (To je najjednoduchší spôsob.)</p>
		 * 
		 * @param x x-ová súradnica polohy obrázka
		 * @param y y-ová súradnica polohy obrázka
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je obrázok poškodený
		 */
		public void obrázok(double x, double y, Image obrázok)
		{
			Image relevantný = Obrázok.dajRelevantnýRaster(obrázok);

			double prepočítanéX = Svet.prepočítajX(x);
			double prepočítanéY = Svet.prepočítajY(y);

			int šírkaObrázka = relevantný.getWidth(null);
			int výškaObrázka = relevantný.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok je poškodený!",
					"imageBroken");

			if (relevantný instanceof Obrázok)
				((Obrázok)relevantný).
					kresliNa((int)prepočítanéX, (int)prepočítanéY, grafikaPlátna);
			else
				grafikaPlátna.drawImage(relevantný,
				(int)prepočítanéX, (int)prepočítanéY, null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(double, double, Image) obrázok}.</p> */
		public void obrazok(double x, double y, Image obrázok) { obrázok(x, y, obrázok); }


		/**
		 * <p>Nakreslí na súradniciach zadaného objektu na plátne (podlahe alebo
		 * strope) obrázok, pričom na zadaných súradniciach sa bude nachádzať
		 * ľavý horný roh obrázka. Táto metóda slúži na nakreslenie obrázka
		 * s čo najmenšou vnútornou komplikovanosťou (v súvislosti so
		 * spôsobom kreslenia). Napríklad kreslenie obrázkov so stredom na
		 * zadanej pozícii by vyžadovalo dodatočný výpočet. Ak chcete
		 * kresliť obrázky vystredené na zadanej pozícii, tak použite na
		 * nakreslenie niektorý robot. (To je najjednoduchší spôsob.)</p>
		 * 
		 * @param objekt objekt určujúci polohu kreslenia obrázka
		 * @param obrázok obrázok, ktorý má byť vykreslený
		 * 
		 * @throws GRobotException ak je obrázok poškodený
		 */
		public void obrázok(Poloha objekt, Image obrázok)
		{
			Image relevantný = Obrázok.dajRelevantnýRaster(obrázok);

			double prepočítanéX = Svet.prepočítajX(objekt.polohaX());
			double prepočítanéY = Svet.prepočítajY(objekt.polohaY());

			int šírkaObrázka = relevantný.getWidth(null);
			int výškaObrázka = relevantný.getHeight(null);
			if (šírkaObrázka < 0 || výškaObrázka < 0)
				throw new GRobotException("Obrázok je poškodený!",
					"imageBroken");

			if (relevantný instanceof Obrázok)
				((Obrázok)relevantný).
					kresliNa((int)prepočítanéX, (int)prepočítanéY, grafikaPlátna);
			else
				grafikaPlátna.drawImage(relevantný,
				(int)prepočítanéX, (int)prepočítanéY, null);

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #obrázok(Poloha, Image) obrázok}.</p> */
		public void obrazok(Poloha objekt, Image obrázok) { obrázok(objekt, obrázok); }


	// Uloženie obrázka

		/**
		 * <p>Uloží aktuálnu kresbu na plátne (podlahe alebo strope) do súboru
		 * s obrázkom. Prípona súboru musí byť {@code .gif}, {@code .png}
		 * alebo {@code .jpg} (resp. {@code .jpeg}). Ak súbor jestvuje, tak
		 * vznikne výnimka oznamujúca, že súbor so zadaným menom už jestvuje.
		 * Ak chcete súbor prepísať, použite metódu {@link 
		 * #uložObrázok(String súbor, boolean prepísať)} s druhým parametrom
		 * rovným {@code valtrue}.</p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * 
		 * @throws GRobotException ak súbor jestvuje alebo bol zadaný
		 *     názov súboru s neplatnou príponou
		 */
		public void uložObrázok(String súbor)
		{
			uložObrázok(súbor, false);
		}

		/** <p><a class="alias"></a> Alias pre {@link #uložObrázok(String) uložObrázok}.</p> */
		public void ulozObrazok(String súbor) { uložObrázok(súbor); }

		/**
		 * <p>Uloží aktuálnu kresbu na plátne (podlahe alebo strope) do súboru
		 * s obrázkom. Prípona súboru musí byť {@code .gif}, {@code .png}
		 * alebo {@code .jpg} (resp. {@code .jpeg}).</p>
		 * 
		 * @param súbor názov súboru s požadovanou príponou
		 * @param prepísať ak je {@code valtrue}, prípadný jestvujúci
		 *     súbor bude prepísaný, inak sa správa rovnako ako metóda
		 *     {@link #uložObrázok(String súbor)}
		 * 
		 * @throws GRobotException ak súbor jestvuje a parameter prepísať
		 *     je {@code valfalse} alebo ak bol zadaný názov súboru
		 *     s neplatnou príponou
		 */
		public void uložObrázok(String súbor, boolean prepísať)
		{
			if (null == súbor)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			File uložiťDo = new File(súbor);

			if (!prepísať && uložiťDo.exists())
				throw new GRobotException("Obrázok „" + súbor +
					"“ už jestvuje.", "imageAlreadyExists", súbor);

			String prípona = súbor.substring(súbor.lastIndexOf('.') + 1);

			if (prípona.toLowerCase().equals("png"))
			{
				// Súbory png:
				try
				{
					BufferedImage obrázokNaUloženie;

					if (null == vlnenie)
						obrázokNaUloženie = obrázokPlátna;
					else
						obrázokNaUloženie = vlnenie.zvlnenýRaster();
	
					if (null != osvetlenie)
						obrázokNaUloženie = osvetliRaster(obrázokNaUloženie);

					ImageIO.write(obrázokNaUloženie, prípona, uložiťDo);
				}
				catch (IOException e) { GRobotException.
					vypíšChybovéHlásenia(e, true); }
			}
			else if (prípona.toLowerCase().equals("jpg") ||
				prípona.toLowerCase().equals("jpeg"))
			{
				// Pre jpeg musíme zmeniť farebný model z ARGB na RGB
				// Pozri: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=
				// 	java2d-interest&D=0&P=2727

				BufferedImage obrázokNaUloženie;

				if (null == vlnenie)
					obrázokNaUloženie = obrázokPlátna;
				else
					obrázokNaUloženie = vlnenie.zvlnenýRaster();

				if (null != osvetlenie)
					obrázokNaUloženie = osvetliRaster(obrázokNaUloženie);

				WritableRaster raster = obrázokNaUloženie.getRaster();
				WritableRaster novýRaster = raster.createWritableChild(
					0, 0, šírkaPlátna, výškaPlátna,
					0, 0, new int[] {0, 1, 2});

				DirectColorModel farebnýModel;

				/*
				if (null == vlnenie)
					farebnýModel = (DirectColorModel)
						obrázokPlátna.getColorModel();
				else
					farebnýModel = (DirectColorModel)
						vlnenie.zvlnenýRaster().getColorModel();
				*/
				farebnýModel = (DirectColorModel)
					obrázokNaUloženie.getColorModel();

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
					prípona, "invalidImageFormat", prípona);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uložObrázok(String, boolean) uložObrázok}.</p> */
		public void ulozObrazok(String súbor, boolean prepísať) { uložObrázok(súbor, prepísať); }


	// Vymazanie

		/**
		 * <p>Vymaže obsah plátna (podlahy alebo stropu), aby bola viditeľná
		 * len farba pozadia sveta. Metóda zároveň vymaže všetky texty
		 * vnútornej konzoly podlahy, resp. stropu (napísané napríklad
		 * prostredníctvom metód {@link #vypíš(Object[]) vypíš},
		 * {@link #vypíšRiadok(Object[]) vypíšRiadok}). Vymazanie grafiky
		 * má za následok vznik udalosti {@link ObsluhaUdalostí#vymazanie()
		 * vymazanie}, v rámci ktorej je možné získať práve vymazané plátno
		 * metódou {@link ÚdajeUdalostí ÚdajeUdalostí}{@code .}{@link 
		 * ÚdajeUdalostí#vymazanéPlátno() vymazanéPlátno}{@code ()}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt reálne prejavil.</p>
		 * 
		 * @see GRobot#farba(Color)
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #písmo(Font)
		 * @see #predvolenéPísmo()
		 * @see #vymažTexty()
		 * @see #vymažGrafiku()
		 */
		public void vymaž()
		{
			vnútornáKonzola.vymaž();

			if (null == originálPlátna) originálPlátna = ((DataBufferInt)
				obrázokPlátna.getRaster().getDataBuffer()).getData();
			Arrays.fill(originálPlátna, 0);
			if (Svet.právePrekresľujem) return;

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ÚdajeUdalostí.poslednéVymazanéPlátno = this;
					ObsluhaUdalostí.počúvadlo.vymazanie();
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
						ÚdajeUdalostí.poslednéVymazanéPlátno = this;
						počúvajúci.vymazanie();
					}
				}

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public void vymaz() { vymaž(); }

		/**
		 * <p>Metóda vymaže všetky texty vnútornej konzoly podlahy alebo
		 * stropu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.
		 * 
		 * Pečiatkové texty sú grafické a nie sú touto metódou nijako
		 * ovplyvnené.</p>
		 * 
		 * <p>Táto metóda <b>nevyvolá</b> vznik udalosti vymazania plátna,
		 * takže reakcia {@link ObsluhaUdalostí#vymazanie() vymazanie} nie
		 * je spustená. (Uvedená udalosť sa dotýka výhradne {@linkplain 
		 * #vymažGrafiku() mazania grafiky}…)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt vizuálne prejavil.</p>
		 * 
		 * @see #vymaž()
		 * @see #vymažGrafiku()
		 * @see #vypíš(Object[]) vypíš(Object...)
		 * @see #vypíšRiadok(Object[]) vypíšRiadok(Object...)
		 * @see #farbaTextu(Color)
		 * @see #farbaPozadiaTextu(Color)
		 * @see #písmo(Font)
		 * @see #predvolenéPísmo()
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 */
		public void vymažTexty()
		{
			vnútornáKonzola.vymaž();
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažTexty() vymažTexty}.</p> */
		public void vymazTexty() { vymažTexty(); }


		/**
		 * <p>Vymaže všetky označené texty vnútornej konzoly. Metóda postupne
		 * prechádza všetky bloky výpisov vnútornej konzoly a odstraňuje tie,
		 * ktoré sú označené (pozri napríklad metódu {@link #označVýpis(int,
		 * int, Color...) označVýpis}).</p>
		 * 
		 * @return súčet počtu vymazaných blokov textu a počtu vymazaných
		 *     riadkov (označovanie koncov riadkov je riešené vnútorne
		 *     v niektorých metódach slúžiacich na označovanie blokov)
		 */
		public int vymažOznačenéTexty()
		{
			int početVymazaných = 0;

			for (int i = 0; i < vnútornáKonzola.riadky.size(); ++i)
			{
				RiadokKonzoly riadok = vnútornáKonzola.riadky.get(i);
				boolean celýVymazaný = true;
				for (int j = 0; j < riadok.size(); ++j)
				{
					PrototypKonzoly prototyp = riadok.get(j);
					ObsahKonzoly obsah = vnútornáKonzola.dajObsah(prototyp);
					if (null == obsah) continue;
					if (!obsah.označený)
					{
						celýVymazaný = false;
						continue;
					}
					riadok.remove(prototyp);
					++početVymazaných; --j;
				}

				if (celýVymazaný && riadok.označenýKoniecRiadka)
				{
					vnútornáKonzola.riadky.remove(riadok);
					++početVymazaných; --i;
				}
			}

			if (0 != početVymazaných && !Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
			return početVymazaných;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažOznačenéTexty() vymažOznačenéTexty}.</p> */
		public int vymazOznaceneTexty() { return vymažOznačenéTexty(); }


		/**
		 * <p>Vymaže grafický obsah plátna (podlahy alebo stropu). Po tejto akcii
		 * zostane plátno priehľadné. Ak sú vymazané obe plátna, tak bude
		 * viditeľná farba pozadia sveta. Texty vnútornej konzoly<sup>[1]</sup>,
		 * ktoré mohli byť vypísané napríklad prostredníctvom metód
		 * {@link #vypíš(Object[]) vypíš} a {@link #vypíšRiadok(Object[])
		 * vypíšRiadok} zostávajú v tomto prípade zachované. Vymazanie grafiky má
		 * za následok vznik udalosti {@link ObsluhaUdalostí#vymazanie()
		 * vymazanie}, v rámci ktorej je možné získať práve vymazané plátno
		 * metódou {@link ÚdajeUdalostí ÚdajeUdalostí}{@code .}{@link 
		 * ÚdajeUdalostí#vymazanéPlátno() vymazanéPlátno}{@code ()}.</p>
		 * 
		 * <p><small>[1] – Vnútorná konzola obsahuje predovšetkým textové
		 * informácie. Tieto texty nie sú (na rozdiel od textov
		 * {@linkplain GRobot#text(String) „nakreslených“ robotmi}) vnímané ako
		 * grafické.</small></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt prejavil.</p>
		 * 
		 * <p>Keď je obsah plátien úplne vymazaný, tak obsah „miestnosti“ –
		 * {@linkplain Svet sveta} grafických robotov – vyplnený {@linkplain 
		 * Svet#farbaPozadia(Color) farbou pozadia}.</p>
		 * 
		 * @see #vymaž()
		 * @see #vymažTexty()
		 * @see GRobot#farba(Color)
		 */
		public void vymažGrafiku()
		{
			if (null == originálPlátna) originálPlátna = ((DataBufferInt)
				obrázokPlátna.getRaster().getDataBuffer()).getData();
			Arrays.fill(originálPlátna, 0);
			if (Svet.právePrekresľujem) return;

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ÚdajeUdalostí.poslednéVymazanéPlátno = this;
					ObsluhaUdalostí.počúvadlo.vymazanie();
				}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					int početPočúvajúcich = GRobot.počúvajúciRozhranie.size();
					for (int i = 0; i < početPočúvajúcich; ++i)
					{
						GRobot počúvajúci = GRobot.počúvajúciRozhranie.get(i);
						ÚdajeUdalostí.poslednéVymazanéPlátno = this;
						počúvajúci.vymazanie();
					}
				}

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažGrafiku() vymažGrafiku}.</p> */
		public void vymazGrafiku() { vymažGrafiku(); }


	// Zobrazenie/skrytie textov konzoly

		/**
		 * <p>Metóda skryje texty vnútornej konzoly podlahy alebo stropu.
		 * 
		 * Táto metóda nemá vplyv na zobrazenie {@linkplain 
		 * #zobrazLištyKonzoly(boolean, boolean) líšt} (čiže ani {@linkplain 
		 * #dajRohLíštKonzoly() rohu}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.
		 * 
		 * Pečiatkové texty sú grafické a nie sú touto metódou nijako
		 * ovplyvnené.</p>
		 * 
		 * <p>Táto metóda <b>nevyvolá</b> vznik žiadnej udalosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt vizuálne prejavil.</p>
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 * @see #vymažTexty()
		 */
		public void skryTexty()
		{
			vnútornáKonzola.skryTexty();
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Metóda zobrazí texty vnútornej konzoly podlahy alebo stropu.
		 * (Ak nejaké jestvujú a ak boli predtým {@linkplain #skryTexty()
		 * skryté.})
		 * 
		 * Táto metóda nemá vplyv na zobrazenie {@linkplain 
		 * #zobrazLištyKonzoly(boolean, boolean) líšt} (čiže ani {@linkplain 
		 * #dajRohLíštKonzoly() rohu}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.
		 * 
		 * Pečiatkové texty sú grafické a nie sú touto metódou nijako
		 * ovplyvnené.</p>
		 * 
		 * <p>Táto metóda <b>nevyvolá</b> vznik žiadnej udalosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt vizuálne prejavil.</p>
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 * @see #vymažTexty()
		 */
		public void zobrazTexty()
		{
			vnútornáKonzola.zobrazTexty();
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Metóda overí, či sú viditeľné texty vnútornej konzoly podlahy
		 * alebo stropu. (Ak nejaké jestvujú.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.</p>
		 * 
		 * @return {@code valtrue} ak sú texty vnútornej konzoly viditeľné
		 *     {@code valfalse} v opačnom prípade
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 * @see #vymažTexty()
		 */
		public boolean textyViditeľné()
		{ return vnútornáKonzola.textyZobrazené(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyViditeľné() textyViditeľné}.</p> */
		public boolean textyViditelne()
		{ return vnútornáKonzola.textyZobrazené(); }

		/**
		 * <p>Metóda overí, či sú zobrazené texty vnútornej konzoly podlahy
		 * alebo stropu. (Ak nejaké jestvujú.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.</p>
		 * 
		 * @return {@code valtrue} ak sú texty vnútornej konzoly zobrazené
		 *     {@code valfalse} v opačnom prípade
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 * @see #vymažTexty()
		 */
		public boolean textyZobrazené()
		{ return vnútornáKonzola.textyZobrazené(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyZobrazené() textyZobrazené}.</p> */
		public boolean textyZobrazene()
		{ return vnútornáKonzola.textyZobrazené(); }

		/**
		 * <p>Metóda overí, či sú skryté texty vnútornej konzoly podlahy
		 * alebo stropu. (Ak nejaké jestvujú.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že texty konzoly
		 * môžu byť vypisované napríklad prostredníctvom metód {@link 
		 * #vypíš(Object[]) vypíš}, {@link #vypíšRiadok(Object[]) vypíšRiadok}.
		 * 
		 * Pre úplnosť dodávame, že texty konzoly majú iné vlastnosti
		 * v porovnaní s pečiatkovými textami, ktoré môžu grafické roboty
		 * „nakresliť“ napríklad metódou {@link GRobot#text(String) text}.</p>
		 * 
		 * @return {@code valtrue} ak sú texty vnútornej konzoly skryté
		 *     {@code valfalse} v opačnom prípade
		 * 
		 * @see #skryTexty()
		 * @see #zobrazTexty()
		 * @see #textyViditeľné()
		 * @see #textyZobrazené()
		 * @see #textySkryté()
		 * @see #vymažTexty()
		 */
		public boolean textySkryté()
		{ return !vnútornáKonzola.textyZobrazené(); }

		/** <p><a class="alias"></a> Alias pre {@link #textySkryté() textySkryté}.</p> */
		public boolean textySkryte()
		{ return !vnútornáKonzola.textyZobrazené(); }


	// Výplň

		/**
		 * <p>Vyplní podlahu alebo strop zadanou farbou, ktorá prekryje aj
		 * {@linkplain Svet#farbaPozadia(Color) farbu pozadia}
		 * sveta.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt prejavil.</p>
		 * 
		 * <p>Výplň plátna prekrýva farbu pozadia. Keď je obsah plátien
		 * {@linkplain #vymažGrafiku() vymazaný}, obsah „miestnosti“ – sveta
		 * grafických robotov – je vyplnený {@linkplain 
		 * Svet#farbaPozadia(Color) farbou pozadia}.</p>
		 * 
		 * @param farba objekt určujúci farbu na výplň plátna
		 * 
		 * @see #vymaž()
		 */
		public void vyplň(Color farba)
		{
			grafikaPlátna.setColor(farba);
			grafikaPlátna.fillRect(0, 0,
				šírkaPlátna, výškaPlátna);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Color) vyplň}.</p> */
		public void vypln(Color farba) { vyplň(farba); }

		/**
		 * <p>Vyplní podlahu alebo strop farbou zadaného objektu, ktorá
		 * prekryje aj {@linkplain Svet#farbaPozadia(Color) farbu
		 * pozadia} sveta.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Metóda berie do úvahy použitie
		 * metód {@link Svet#nekresli() nekresli} a {@link Svet#kresli()
		 * kresli}. Čiže ak je automatické prekresľovanie vypnuté, treba po
		 * tejto metóde zavolať metódu {@link Svet#prekresli() prekresli},
		 * aby sa efekt prejavil.</p>
		 * 
		 * <p>Výplň plátna prekrýva farbu pozadia. Keď je obsah plátien
		 * {@linkplain #vymažGrafiku() vymazaný}, obsah „miestnosti“ – sveta
		 * grafických robotov – je vyplnený {@linkplain 
		 * Svet#farbaPozadia(Color) farbou pozadia}.</p>
		 * 
		 * @param objekt objekt určujúci farbu na výplň plátna
		 * 
		 * @see #vymaž()
		 */
		public void vyplň(Farebnosť objekt) { vyplň(objekt.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Farebnosť) vyplň}.</p> */
		public void vypln(Farebnosť objekt) { vyplň(objekt); }

		/* *
		 * <p>Toto je „klon“ metódy {@link #vyplň(Color farba)}. Podlaha alebo
		 * strop sú vyplnené len v prípade, že je v premennej typu {@link 
		 * Object} (zadanej ako parameter) uložená inštancia triedy {@link 
		 * Farba Farba} alebo {@link Color Color}.</p>
		 * /
		public void vyplň(Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vyplň((Farba)farba);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Object) vyplň}.</p> * /
		public void vypln(Object farba) { vyplň(farba); }
		*/

		/**
		 * <p>Vyplní podlahu alebo strop farbou zadanou prostredníctvom
		 * farebných zložiek. Na ďalšie informácie pozri metódu {@link 
		 * #vyplň(Color farba)}. Správanie tejto metódy je odvodené od
		 * nej.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vyplň(Color)
		 */
		public Farba vyplň(int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vyplň(nováFarba);
			return nováFarba;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int) vyplň}.</p> */
		public Farba vypln(int r, int g, int b) { return vyplň(r, g, b); }

		/**
		 * <p>Vyplní podlahu alebo strop farbou zadanou prostredníctvom
		 * farebných zložiek a úrovne (ne)priehľadnosti. Na ďalšie informácie
		 * pozri metódu {@link #vyplň(Color farba)}. Správanie tejto metódy
		 * je odvodené od nej.</p>
		 * 
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vyplň(Color)
		 */
		public Farba vyplň(int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vyplň(nováFarba);
			return nováFarba;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(int, int, int, int) vyplň}.</p> */
		public Farba vypln(int r, int g, int b, int a) { return vyplň(r, g, b, a); }

		/**
		 * <p>Vyplní podlahu alebo strop zadanou textúrou. Textúra je súbor
		 * s obrázkom, ktorý bude použitý na dlaždicové vyplnenie plochy
		 * plátna (podlahy alebo stropu).</p>
		 * 
		 * <p>Obrázok prečítaný zo súboru je chápaný ako zdroj a po
		 * prečítaní zostane uložený vo vnútornej pamäti sveta. Z nej
		 * môže byť v prípade potreby (napríklad ak sa obsah súboru na
		 * disku zmenil) odstránený metódou {@link Svet#uvoľni(String)
		 * Svet.uvoľni(názovZdroja)}. (Táto informácia je platná pre
		 * všetky metódy pracujúce s obrázkami alebo zvukmi, ktoré
		 * prijímajú názov súboru ako parameter.)</p>
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
		 * @see #obrázok(String)
		 * 
		 * @throws GRobotException ak súbor s obrázkom nebol nájdený
		 */
		public void vyplň(String súbor)
		{
			BufferedImage obrázok = Obrázok.súborNaObrázok(súbor);

			grafikaPlátna.setPaint(
				new TexturePaint(obrázok, new Rectangle2D.Double(
					Svet.posuňVýplňX, Svet.posuňVýplňY,
					obrázok.getWidth()  * Svet.mierkaVýplneX,
					obrázok.getHeight() * Svet.mierkaVýplneY)));

			if (0 == Svet.otočVýplňΑ)
				grafikaPlátna.fillRect(0, 0, šírkaPlátna, výškaPlátna);
			else
			{
				double β = Math.toRadians(Svet.otočVýplňΑ);
				grafikaPlátna.rotate(-β, Svet.otočVýplňX, Svet.otočVýplňY);

				Shape s = AffineTransform.getRotateInstance(β,
					Svet.otočVýplňX, Svet.otočVýplňY).createTransformedShape(
						new Rectangle(0, 0, šírkaPlátna, výškaPlátna));

				grafikaPlátna.fill(s);
				grafikaPlátna.rotate(β, Svet.otočVýplňX, Svet.otočVýplňY);
			}

			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(String) vyplň}.</p> */
		public void vypln(String súbor) { vyplň(súbor); }

		/**
		 * <p>Vyplní podlahu alebo strop zadanou textúrou. Textúra je
		 * {@linkplain Obrázok obrázok} (objekt typu {@link Image
		 * Image} alebo odvodený), ktorý bude použitý na dlaždicové
		 * vyplnenie plochy plátna (podlahy alebo stropu).</p>
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
		 * @param výplň obrázok s textúrou
		 * 
		 * @see #vyplň(Color)
		 */
		public void vyplň(Image výplň)
		{
			BufferedImage obrázok =
				Obrázok.preveďNaBufferedImage(výplň);
			BufferedImage relevantný =
				Obrázok.dajRelevantnýRaster(obrázok);

			float priehľadnosť = (obrázok instanceof Obrázok) ?
				((Obrázok)obrázok).priehľadnosť : 1.0f;

			if (priehľadnosť > 0)
			{
				grafikaPlátna.setPaint(
					new TexturePaint(relevantný, new Rectangle2D.Double(
						Svet.posuňVýplňX, Svet.posuňVýplňY,
						relevantný.getWidth(null)  * Svet.mierkaVýplneX,
						relevantný.getHeight(null) * Svet.mierkaVýplneY)));

				Shape s = new Rectangle(0, 0, šírkaPlátna, výškaPlátna);
				double β = 0.0;

				if (0 != Svet.otočVýplňΑ)
				{
					β = Math.toRadians(Svet.otočVýplňΑ);
					grafikaPlátna.rotate(-β, Svet.otočVýplňX, Svet.otočVýplňY);

					s = AffineTransform.getRotateInstance(β,
						Svet.otočVýplňX, Svet.otočVýplňY).
						createTransformedShape(s);
				}

				if (priehľadnosť < 1)
				{
					Composite záloha = grafikaPlátna.getComposite();
					grafikaPlátna.setComposite(
						AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, priehľadnosť));
					grafikaPlátna.fill(s);
					grafikaPlátna.setComposite(záloha);
				}
				else
					grafikaPlátna.fill(s);


				if (0 != Svet.otočVýplňΑ)
				{
					grafikaPlátna.rotate(β, Svet.otočVýplňX, Svet.otočVýplňY);
				}

				if (!Svet.právePrekresľujem)
					Svet.automatickéPrekreslenie();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyplň(Image) vyplň}.</p> */
		public void vypln(Image výplň) { vyplň(výplň); }

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu, ktorá sa odtiaľ rozšíri
		 * po okraje tej časti kresby, v ktorej sa zadaný bod nachádza.</p>
		 * 
		 * <p>Toto je doplnková metóda umožňujúca vypĺňanie prázdnych častí
		 * kresieb na plátne. Jej efekt nemusí byť dokonalý. Farba sa
		 * napríklad nemusí dostať do úzkych častí kresby. Naopak,
		 * v niektorých situáciách môže farba mierne presiaknuť popod tenký
		 * okraj vypĺňanej časti kresby. Ak sa pokúsime vyplniť už vyplnenú
		 * časť kresby, farba dobre nezatečie k jej okrajom. Najlepšie
		 * výsledky sa dajú dosiahnuť pri vyplnení prázdnych častí kresby
		 * s dostatočne hrubým okrajom, ktorá neobsahuje žiadne úzke ťažko
		 * priechodné miesta.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že aj táto metóda
		 * (ako všetky ostatné) berie do úvahy použitie metód
		 * {@link Svet#nekresli() nekresli} a {@link Svet#kresli() kresli}.
		 * Čiže ak je automatické prekresľovanie vypnuté, treba po tejto metóde
		 * zavolať metódu {@link Svet#prekresli() prekresli}, aby sa efekt
		 * prejavil.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Pozrite si aj opis metódy
		 * {@link GRobot#vylejFarbu() GRobot.vylejFarbu()}.
		 * Je v ňom pripojený aj príklad jej použitia.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param farba objekt určujúci farbu výplne
		 */
		public void vylejFarbu(double x, double y, Color farba)
		{
			Obrázok.VykonajVObrázku.vylejFarbu(obrázokPlátna, x, y, farba);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu, ktorá sa odtiaľ rozšíri
		 * po okraje tej časti kresby, v ktorej sa zadaný bod nachádza.</p>
		 * 
		 * <p>Toto je doplnková metóda umožňujúca vypĺňanie prázdnych častí
		 * kresieb na plátne. Jej efekt nemusí byť dokonalý. Farba sa
		 * napríklad nemusí dostať do úzkych častí kresby. Naopak,
		 * v niektorých situáciách môže farba mierne presiaknuť popod tenký
		 * okraj vypĺňanej časti kresby. Ak sa pokúsime vyplniť už vyplnenú
		 * časť kresby, farba dobre nezatečie k jej okrajom. Najlepšie
		 * výsledky sa dajú dosiahnuť pri vyplnení prázdnych častí kresby
		 * s dostatočne hrubým okrajom, ktorá neobsahuje žiadne úzke ťažko
		 * priechodné miesta.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pripomíname, že aj táto metóda
		 * (ako všetky ostatné) berie do úvahy použitie metód {@link 
		 * Svet#nekresli() nekresli} a {@link Svet#kresli() kresli}. Čiže ak
		 * je automatické prekresľovanie vypnuté, treba po tejto metóde
		 * zavolať metódu {@link Svet#prekresli() prekresli}, aby sa efekt
		 * prejavil.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Pozrite si aj opis metódy
		 * {@link GRobot#vylejFarbu() GRobot.vylejFarbu()}.
		 * Je v ňom pripojený aj príklad jej použitia.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param objekt objekt určujúci farbu výplne
		 */
		public void vylejFarbu(double x, double y, Farebnosť objekt)
		{ vylejFarbu(x, y, objekt.farba()); }

		/* *
		 * <p>Toto je „klon“ metódy {@link #vylejFarbu(double x, double y,
		 * Color farba)}. Proces vyplnenia sa uskutoční len v prípade, že je
		 * v premennej typu {@link Object} (zadanej ako parameter) uložená
		 * inštancia triedy {@link Farba Farba} alebo {@link Color Color}.</p>
		 * /
		public void vylejFarbu(double x, double y, Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vylejFarbu(x, y, (Farba)farba);
		}
		*/

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu zadanú prostredníctvom
		 * farebných zložiek, ktorá sa určeného bodu rozšíri po okraje
		 * okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double x, double y, Color farba)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vylejFarbu(double, double, Color)
		 */
		public Farba vylejFarbu(double x, double y, int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vylejFarbu(x, y, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu zadanú prostredníctvom
		 * farebných zložiek a úrovne (ne)priehľadnosti, pričom farba sa
		 * zo stanoveného bodu rozšíri k okrajom okolitej kresby. Na ďalšie
		 * informácie pozri metódu {@link #vylejFarbu(double x, double y,
		 * Color farba)}. Správanie tejto metódy je odvodené od nej.</p>
		 * 
		 * @param x x-ová súradnica počiatočného bodu
		 * @param y y-ová súradnica počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vylejFarbu(double, double, Color)
		 */
		public Farba vylejFarbu(double x, double y, int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vylejFarbu(x, y, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu, ktorá sa odtiaľ rozšíri
		 * po okraje tej časti kresby, v ktorej sa zadaný bod nachádza. Na
		 * ďalšie informácie pozri metódu {@link #vylejFarbu(double x,
		 * double y, Color farba)}. Správanie tejto metódy je odvodené od
		 * nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param farba objekt určujúci farbu výplne
		 */
		public void vylejFarbu(Poloha bod, Color farba)
		{
			Obrázok.VykonajVObrázku.vylejFarbu(obrázokPlátna,
				bod.polohaX(), bod.polohaY(), farba);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu, ktorá sa odtiaľ rozšíri
		 * po okraje tej časti kresby, v ktorej sa zadaný bod nachádza. Na
		 * ďalšie informácie pozri metódu {@link #vylejFarbu(double x,
		 * double y, Color farba)}. Správanie tejto metódy je odvodené od
		 * nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param objekt objekt určujúci farbu výplne
		 */
		public void vylejFarbu(Poloha bod, Farebnosť objekt)
		{ vylejFarbu(bod, objekt.farba()); }

		/* *
		 * <p>Toto je „klon“ metódy {@link #vylejFarbu(double x, double y,
		 * Color farba)}. Proces vyplnenia sa uskutoční len v prípade, že je
		 * v premennej typu {@link Object} (zadanej ako parameter) uložená
		 * inštancia triedy {@link Farba Farba} alebo {@link Color Color}.</p>
		 * /
		public void vylejFarbu(Poloha bod, Object farba)
		{
			if (farba instanceof Color) farba = new Farba((Color)farba);
			if (farba instanceof Farba) vylejFarbu(bod, (Farba)farba);
		}
		*/

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu zadanú prostredníctvom
		 * farebných zložiek, ktorá sa určeného bodu rozšíri po okraje
		 * okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double x, double y, Color farba)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     zložiek
		 * 
		 * @see #vylejFarbu(Poloha, Color)
		 */
		public Farba vylejFarbu(Poloha bod, int r, int g, int b)
		{
			Farba nováFarba = new Farba(r, g, b);
			vylejFarbu(bod, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Vyleje do zadaného bodu na plátno farbu zadanú prostredníctvom
		 * farebných zložiek a úrovne (ne)priehľadnosti, pričom farba sa
		 * zo stanoveného bodu rozšíri k okrajom okolitej kresby. Na ďalšie
		 * informácie pozri metódu {@link #vylejFarbu(double x, double y,
		 * Color farba)}. Správanie tejto metódy je odvodené od nej.</p>
		 * 
		 * @param bod súradnice počiatočného bodu
		 * @param r červená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param g zelená zložka farby; celé číslo v rozsahu 0 – 255
		 * @param b modrá zložka farby; celé číslo v rozsahu 0 – 255
		 * @param a úroveň (ne)priehľadnosti farby; celé číslo v rozsahu
		 *     0 – 255 (0 – neviditeľná farba; 255 – nepriehľadná farba)
		 * @return objekt typu {@link Farba Farba} vytvorený podľa zadaných
		 *     hodnôt parametrov
		 * 
		 * @see #vylejFarbu(Poloha, Color)
		 */
		public Farba vylejFarbu(Poloha bod, int r, int g, int b, int a)
		{
			Farba nováFarba = new Farba(r, g, b, a);
			vylejFarbu(bod, nováFarba);
			return nováFarba;
		}

		/**
		 * <p>Zadaný robot vyleje na svojej pozícii na plátno svoju aktuálnu
		 * {@linkplain GRobot#farba() farbu}, ktorá sa odtiaľ rozšíri po
		 * okraje okolitej kresby. Na ďalšie informácie pozri metódu {@link 
		 * #vylejFarbu(double x, double y, Color farba)}. Správanie tejto
		 * metódy je odvodené od nej.</p>
		 * 
		 * @param ktorý robot, ktorého poloha a farba sú použité na výplň
		 */
		public void vylejFarbu(GRobot ktorý)
		{ vylejFarbu(ktorý.aktuálneX, ktorý.aktuálneY, ktorý.farbaRobota); }


	// Maska

		/**
		 * <p>Zruší priehľadnosť všetkých bodov na plátne. Predvolene sú
		 * všetky body plátna priehľadné. Aj priehľadné body však môžu mať
		 * svoje hodnoty farebnosti. Ukazuje to príklad nižšie. Keby sme
		 * použili túto metódu na plátno v predvolenom stave, zostalo by
		 * celé čierne.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Volanie tejto metódy neovplyvní
		 * celkovú priehľadnosť plátna ovplyvňovanú metódami
		 * {@link #priehľadnosť(double) priehľadnosť(priehľadnosť)},
		 * {@link #priehľadnosť(Priehľadnosť) priehľadnosť(objekt)}
		 * a {@link #upravPriehľadnosť(double) upravPriehľadnosť(zmena)}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúci fragment kódu (určený priamo na vloženie do
		 * konštruktora triedy odvodenej od robota) ukazuje ako funguje
		 * zrušenie priehľadnosti:</p>
		 * 
		 * <pre CLASS="example">
			{@code valsuper}({@code num200}, {@code num200});

			{@code comm// Vypnutie automatického prekresľovania:}
			{@link Svet Svet}.{@link Svet#nekresli() nekresli}();

			{@code comm// Kreslenie šumu:}
			{@code kwdfor} ({@code typedouble} y = {@link Svet Svet}.{@link Svet#najmenšieY() najmenšieY}(); y &lt;= {@link Svet Svet}.{@link Svet#najväčšieY() najväčšieY}(); ++y)
				{@code kwdfor} ({@code typedouble} x = {@link Svet Svet}.{@link Svet#najmenšieX() najmenšieX}(); x &lt;= {@link Svet Svet}.{@link Svet#najväčšieX() najväčšieX}(); ++x)
				{
					{@link GRobot#skočNa(double, double) skočNa}(x, y);

					{@link GRobot#kresliNaStrop() kresliNaStrop}();
					{@link GRobot#náhodnáFarba() náhodnáFarba}();
					{@link GRobot#bod() bod}();

					{@link GRobot#kresliNaPodlahu() kresliNaPodlahu}();
					{@link GRobot#náhodnáFarba() náhodnáFarba}();
					{@link GRobot#bod() bod}();
				}

			{@code comm// Rozmazanie šumu na podlahe (na lepšie rozlíšenie):}
			{@link Plátno podlaha}.{@link Plátno#rozmaž(int, int) rozmaž}({@code num1}, {@code num5});

			{@code comm// Vymazanie kruhovej oblasti zo stropu (vo vytvorenej diere sa}
			{@code comm// bude zobrazovať rozmazaný šum podlahy):}
			{@code kwdfinal} {@link Obrázok Obrázok} vymaž = {@code kwdnew} {@link Obrázok Obrázok}();
			{@link GRobot#kresliDoObrázka(Obrázok) kresliDoObrázka}(vymaž);
			{@link GRobot#skočNa(Poloha) skočNa}({@link Poloha#stred stred});
			{@link GRobot#farba(Color) farba}({@link Farebnosť#biela biela});
			{@link GRobot#kruh(double) kruh}({@code num80});
			{@link Plátno strop}.{@link Plátno#vymažKresbu(Image) vymažKresbu}(vymaž);
				{@code comm// (Poznámka: Vymazanie je v skutočnosti skôr nastavenie}
				{@code comm// priehľadnosti bodov. Hodnoty farebných zložiek zostávajú}
				{@code comm// nezmenené, čo sa prejaví po obnovení nepriehľadnosti stropu.)}

			{@code comm// Dokončenie (skrytie robota a obnovenie automatického prekresľovania):}
			{@link GRobot#skry() skry}();
			{@link Svet Svet}.{@link Svet#kresli() kresli}();

			{@code comm// Čakanie na stlačenie klávesu:}
			{@link Svet Svet}.{@link Svet#čakajNaKláves() čakajNaKláves}();
			{@link Svet Svet}.{@link Svet#pípni() pípni}();

			{@code comm// Zrušenie priehľadnosti (spôsobí zmiznutie rozmazanej oblasti}
			{@code comm// šumu podlahy, pretože sa obnoví nepriehľadnosť diery v strope,}
			{@code comm// namiesto ktorej sa ukáže pôvodný šum nakreslený na strope pred}
			{@code comm// „vymazaním“):}
			{@link Plátno strop}.{@link Plátno#zrušPriehľadnosť() zrušPriehľadnosť}();
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <p><image>zrus-priehladnost.gif<alt/></image>Ukážka vzhľadu
		 * príkladu (animácia strieda vzhľad tesne po spustení a po stlačení
		 * klávesu).</p>
		 */
		public void zrušPriehľadnosť()
		{
			Obrázok.VykonajVObrázku.zrušPriehľadnosť(obrázokPlátna);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #zrušPriehľadnosť() zrušPriehľadnosť}.</p> */
		public void zrusPriehladnost() { zrušPriehľadnosť(); }


		/**
		 * <p>Použije na toto plátno masku vyrobenú zo zadaného obrázka.
		 * Obrázok masky musí mať rovnaké rozmery ako plátno, inak operácia
		 * zlyhá. Maska je vyrobená z kombinácie intenzity farieb
		 * a priehľadnosti jednotlivých bodov zadaného obrázka. Čím je bod
		 * masky tmavší, tým bude viditeľnejší, čím svetlejší, tým menej
		 * viditeľný. Jas je korigovaný aj zložkami priehľadnosti obidvoch
		 * bodov, teda bodu, ktorý bol pôvodne na plátne aj bodu na obrázku
		 * masky. Ak je súčin zložiek priehľadnosti rovný nule, tak bude
		 * výsledný bod neviditeľný. Viditeľnosť bodov v ostatných prípadoch
		 * záleží na výsledku výpočtov algoritmu. Napríklad úplne čierne
		 * úplne nepriehľadné body masky neovplyvnia priehľadnosť bodov
		 * na plátne.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Maska nie je pri
		 * {@linkplain GRobot#svgExport exporte} kreslenia do inštancie
		 * {@link SVGPodpora SVGPodpora} použitá priamo, pretože pracuje
		 * s rastrom obrázkov/plátien. Ak chcete do {@linkplain 
		 * SVGPodpora SVG podpory} exportovať vlastnú masku, použite
		 * kombináciu {@linkplain SVGPodpora#definície() vlastnej
		 * definície} (vložením záznamu <code>&lt;mask
		 * id="<em>ID masky</em>"&gt;…&lt;/mask&gt;</code>) a {@linkplain 
		 * SVGPodpora#prepíšAtribút(int, String, Object) atribútu}
		 * <code>mask="url(#<em>ID masky</em>)"</code>. Podrobnosti o SVG
		 * maskách nájdete napríklad tu:<br /><i>Clipping and masking –
		 * SVG : Scalable Vector Graphics | MDN.</i> Mozilla Developer
		 * Network, <a
		 * href="https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Clipping_and_masking#masking"
		 * target="_blank">developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Clipping_and_masking#masking.</a>
		 * Naposledy pristúpené: 28. januára 2023.<br />Môžete tiež použiť
		 * orezávanie – {@linkplain #kresliDo(Shape) pozri napríklad
		 * tu.}</p>
		 * 
		 * @param maska obrázok, ktorý bude použitý ako maska
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean použiMasku(BufferedImage maska)
		{
			boolean úspech = Obrázok.VykonajVObrázku.
				použiMasku(obrázokPlátna, maska);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
			return úspech;
		}

		/** <p><a class="alias"></a> Alias pre {@link #použiMasku(BufferedImage) použiMasku}.</p> */
		public boolean pouziMasku(BufferedImage maska)
		{ return použiMasku(maska); }


		/**
		 * <p>Vyrobí z obsahu tohto plátna do zadaného obrázka masku
		 * priehľadnosti. Zadaný obrázok musí mať rovnaké rozmery ako plátno,
		 * inak operácia zlyhá. Vyrobená maska bude obsahovať čierne body
		 * s rôznou úrovňou priehľadnosti podľa priehľadnosti bodov na plátne.
		 * Pôvodný obsah zadaného obrázka (argumentu {@code nováMaska}) bude
		 * nahradený.</p>
		 * 
		 * @param nováMaska obrázok, do ktorého bude nová maska vyrobená
		 *     (pôvodný obsah obrázka bude nahradený maskou)
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean vyrobMasku(BufferedImage nováMaska)
		{
			// boolean úspech =
			return null != Obrázok.VykonajVObrázku.
				vyrobMasku(obrázokPlátna, nováMaska);
			// if (!Svet.právePrekresľujem)
			// 	Svet.automatickéPrekreslenie();
			// return úspech;
		}

		/**
		 * <p>Vyrobí z obsahu tohto plátna masku priehľadnosti. Metóda vytvorí
		 * masku do nového obrázka (typu {@link BufferedImage BufferedImage}),
		 * ktorý sama automaticky vytvorí a vráti ho ako svoju návratovú
		 * hodnotu. Nová maska bude obsahovať čierne body s rôznou úrovňou
		 * priehľadnosti vychádzajúc z priehľadnosti bodov na plátne.</p>
		 * 
		 * @return nový obrázok obsahujúci vyrobenú masku
		 */
		public BufferedImage vyrobMasku()
		{ return Obrázok.VykonajVObrázku.vyrobMasku(obrázokPlátna, null); }


	// Rozmazanie a filter

		/**
		 * <p>Rozmaže grafiku tohto plátna. Pre priehľadné (neviditeľné) body je
		 * pri procese rozmazania použitá zadaná farba pozadia. Opakovanie
		 * a rozsah majú z vizuálneho hľadiska podobný dopad na výsledný efekt
		 * rozmazania, ale matice lineárneho kruhového rozmazania sú
		 * vygenerované len do úrovne rozsahu 5 (vrátane). Pri zadaní vyššej
		 * hodnoty rozsahu získame rovnaký efekt ako keby sme zadali hodnotu
		 * 5. Ak chceme dosiahnuť vyššiu mieru rozmazania, musíme zvýšiť počet
		 * opakovaní procesu rozmazania (argument opakovanie). Čím vyššie sú
		 * hodnoty opakovania a rozsahu, tým vyššie sú nároky metódy na
		 * výpočtový výkon.</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania; reálny efekt dosiahneme len
		 *     v rámci hodnôt 1 – 5, vyššie čísla sú zaokrúhlené na 5, nižšie
		 *     nespôsobia žiadne rozmazanie
		 * @param pozadie farba použitá pri procese rozmazania pre priehľadné
		 *     body
		 */
		public void rozmaž(int opakovanie, int rozsah, Color pozadie)
		{
			Obrázok.VykonajVObrázku.rozmaž(obrázokPlátna,
				grafikaPlátna, opakovanie, rozsah, pozadie);
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, Color) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, Color pozadie)
		{ rozmaž(opakovanie, rozsah, pozadie); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , pozadie);}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(int opakovanie, Color pozadie)
		{ rozmaž(opakovanie, 1, pozadie); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, Color) rozmaž}.</p> */
		public void rozmaz(int opakovanie, Color pozadie)
		{ rozmaž(opakovanie, 1, pozadie); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code 
		 * , pozadie);}</p>
		 * 
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(Color pozadie) { rozmaž(1, 1, pozadie); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(Color) rozmaž}.</p> */
		public void rozmaz(Color pozadie) { rozmaž(1, 1, pozadie); }


		/**
		 * <p>Rozmaže grafiku tohto plátna. Pre priehľadné (neviditeľné) body je
		 * pri procese rozmazania použitá zadaná farba pozadia. Opakovanie
		 * a rozsah majú z vizuálneho hľadiska podobný dopad na výsledný efekt
		 * rozmazania, ale matice lineárneho kruhového rozmazania sú
		 * vygenerované len do úrovne rozsahu 5 (vrátane). Pri zadaní vyššej
		 * hodnoty rozsahu získame rovnaký efekt ako keby sme zadali hodnotu
		 * 5. Ak chceme dosiahnuť vyššiu mieru rozmazania, musíme zvýšiť počet
		 * opakovaní procesu rozmazania (argument opakovanie). Čím vyššie sú
		 * hodnoty opakovania a rozsahu, tým vyššie sú nároky metódy na
		 * výpočtový výkon.</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania; reálny efekt dosiahneme len
		 *     v rámci hodnôt 1 – 5, vyššie čísla sú zaokrúhlené na 5, nižšie
		 *     nespôsobia žiadne rozmazanie
		 * @param pozadie farba použitá pri procese rozmazania pre priehľadné
		 *     body
		 */
		public void rozmaž(int opakovanie, int rozsah, Farebnosť pozadie)
		{
			Obrázok.VykonajVObrázku.rozmaž(obrázokPlátna,
				grafikaPlátna, opakovanie, rozsah, pozadie.farba());
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, Color) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, Farebnosť pozadie)
		{ rozmaž(opakovanie, rozsah, pozadie); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , pozadie);}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(int opakovanie, Farebnosť pozadie)
		{ rozmaž(opakovanie, 1, pozadie.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, Farebnosť) rozmaž}.</p> */
		public void rozmaz(int opakovanie, Farebnosť pozadie)
		{ rozmaž(opakovanie, 1, pozadie.farba()); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code 
		 * , pozadie);}</p>
		 * 
		 * @param pozadie farba použitá pre neviditeľné body
		 */
		public void rozmaž(Farebnosť pozadie) { rozmaž(1, 1, pozadie.farba()); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(Farebnosť) rozmaž}.</p> */
		public void rozmaz(Farebnosť pozadie) { rozmaž(1, 1, pozadie.farba()); }


		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, rozsah, }{@link Svet
		 * Svet}<code>.</code>{@link Svet#farbaPozadia()
		 * farbaPozadia}{@code ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>V tomto príklade použijeme rozmazanie na napodobnenie obláčikov
		 * na oblohe (<small>pozri zoznam zmien: <a
		 * href="zoznam-zmien.html">poďakovanie</a> uvedené pri verzii
		 * 1.35</small>):</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Nakreslíme niekoľko skupín čiastočne priehľadných bielych}
			{@code comm// elíps na tyrkysovom pozadí:}

			{@link Svet Svet}.{@link Svet#farbaPozadia(Color) farbaPozadia}({@link Farebnosť#tyrkysová tyrkysová});

			{@link GRobot#farba(int, int, int, int) farba}({@code num250}, {@code num250}, {@code num250}, {@code num150});
			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#skry() skry}();

			{@code kwdfor} ({@code typeint} j = {@code num0}; j &lt; {@code num16}; ++j)
			{
				{@link GRobot#skočNa(double, double) skočNa}({@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}(
						{@link Svet Svet}.{@link Svet#ľavýOkraj() ľavýOkraj}(), {@link Svet Svet}.{@link Svet#pravýOkraj() pravýOkraj}()),
					{@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}(
						{@link Svet Svet}.{@link Svet#dolnýOkraj() dolnýOkraj}(), {@link Svet Svet}.{@link Svet#hornýOkraj() hornýOkraj}()));

				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num32}; ++i)
				{
					{@link GRobot#smer(double) smer}({@code num90});
					{@link GRobot#vyplňElipsu(double, double) vyplňElipsu}({@code num10}, {@code num5});
					{@link GRobot#náhodnýSmer() náhodnýSmer}();
					{@link GRobot#dopredu(double) dopredu}({@code num12});
				}
			}

			{@code comm// Rozmazaním dosiahneme zaujímavý efekt…}
			{@link Plátno podlaha}.{@code currrozmaž}({@code num12}, {@code num3});
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>rozmazaneOblaciky-0.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-1.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-2.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-3.png<alt/></image></td>
		 * </tr><tr>
		 * <td><image>rozmazaneOblaciky-4.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-5.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-6.png<alt/></image></td>
		 * <td><image>rozmazaneOblaciky-7.png<alt/></image></td>
		 * </tr><tr><td colspan="4"><p class="image">Niekoľko ukážok výstupu
		 * príkladu – výsledok každého spustenia je unikátny <small>(plátno
		 * ukážok je úmyselne zmenšené)</small>.</p></td></tr></table>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania
		 */
		public void rozmaž(int opakovanie, int rozsah)
		{ rozmaž(opakovanie, rozsah, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah)
		{ rozmaž(opakovanie, rozsah, Svet.farbaPozadia); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , }{@link Svet
		 * Svet}<code>.</code>{@link Svet#farbaPozadia() farbaPozadia}{@code ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 */
		public void rozmaž(int opakovanie)
		{ rozmaž(opakovanie, 1, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int) rozmaž}.</p> */
		public void rozmaz(int opakovanie)
		{ rozmaž(opakovanie, 1, Svet.farbaPozadia); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color) rozmaž}{@code 
		 * (}{@code num1}{@code , }{@code num1}{@code , }{@link Svet
		 * Svet}<code>.</code>{@link Svet#farbaPozadia() farbaPozadia}{@code 
		 * ());}
		 * <!--   -->
		 * To znamená, že pre priehľadné (neviditeľné) body je pri procese
		 * rozmazania použitá aktuálna farba pozadia sveta (pozri: {@link 
		 * Svet#farbaPozadia(Color) Svet.farbaPozadia(farba)}).</p>
		 */
		public void rozmaž() { rozmaž(1, 1, Svet.farbaPozadia); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž() rozmaž}.</p> */
		public void rozmaz() { rozmaž(1, 1, Svet.farbaPozadia); }


		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, rozsah, }{@code kwdnew}{@code  }{@link 
		 * Farba Farba}{@code (bgr, bgg, bgb));}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param rozsah rozsah rozmazania
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int opakovanie, int rozsah,
			int bgr, int bgg, int bgb)
		{
			rozmaž(opakovanie, rozsah, new Farba(bgr, bgg, bgb));
			/*
			// Pôvodný spôsob rozmazávania a pôvodný opis metódy

				 * Rozmaže grafiku tohto plátna. Pre priehľadné (neviditeľné)
				 * body sú pri procese výpočtu rozmazania použité zadané
				 * farebné komponenty {@code bgr}, {@code bgg}, {@code bgb}.
				 * Opakovanie a rozsah majú podobný dopad na výsledný efekt
				 * rozmazania, ale viacnásobné opakovanie je o niečo rýchlejšie
				 * ako výpočet s väčším rozsahom. Väčší rozsah sa usiluje
				 * zahrnúť do procesu výpočtu čo najširšie okolie rozmazávaných
				 * bodov, čím môže navyše spôsobiť grafický efekt mierneho
				 * rušenia popri rozmazaní…
				 * 
				 * @param opakovanie počet opakovaní rozmazania; pri vyšších
				 *     hodnotách opakovania narastajú nároky na výkon
				 * @param rozsah rozsah rozmazania; odporúčame nízke hodnoty
				 *     (1 – 4); pri vyšších hodnotách nadmerne narastajú
				 *     nároky na výkon
				 * @param bgr červený komponent pozadia
				 * @param bgg zelený komponent pozadia
				 * @param bgb modrý komponent pozadia

			if (rozsah < 1 || opakovanie < 1) return;

			if (null == originálPlátna)
				originálPlátna = ((DataBufferInt)
					obrázokPlátna.getRaster().
						getDataBuffer()).getData();

			if (null == operáciePlátna)
				operáciePlátna = new int[originálPlátna.length];


			int šírka = obrázokPlátna.getWidth();
			int výška = obrázokPlátna.getHeight();

			// Použité skratky:
			//  xč, yč – súradnice čítaného bodu
			//  sč – súradnica čítaného bodu prepočítaná na polohu v poli
			//    (xč + yč * šírka)
			//  xz, yz – súradnice zapisovaného bodu
			//  sz – súradnica zapisovaného bodu prepočítaná na polohu v poli
			//  h – aktuálna prečítaná hodnota bodu (farby)
			//  vr, vg, vb, va – prepočítavané hodnoty zložiek výsledku
			//    (červená, zelená, modrá a alfa)
			//  p – počet hodnôt vo výsledku

			for (int i = 0; i < opakovanie; ++i)
			{
				for (int sz = 0, yz = 0; yz < výška; ++yz)
				{
					for (int xz = 0; xz < šírka; ++xz, ++sz)
					{
						int vr = 0;
						int vg = 0;
						int vb = 0;
						int va = 0;
						int p = 0;

						for (int yč = yz - rozsah; yč <= yz + rozsah; ++yč)
						{
							if (yč < 0 || yč >= výška) continue;

							for (int xč = xz - rozsah, sč = xč + yč * šírka;
								xč <= xz + rozsah; ++xč, ++sč)
							{
								if (xč < 0 || xč >= šírka) continue;

								int h = originálPlátna[sč];

								if (h >> 24 == 0)
								{
									vb += bgb;
									vg += bgg;
									vr += bgr;
								}
								else
								{
									vb += h & 0xff; h >>= 8;
									vg += h & 0xff; h >>= 8;
									vr += h & 0xff; h >>= 8;
									va += h & 0xff; h >>= 8;
								}

								++p;
							}
						}

						if (p > 0)
						{
							vr /= p;
							vg /= p;
							vb /= p;
							va /= p;
						}

						operáciePlátna[sz] =
							(((((va << 8) | vr) << 8) | vg) << 8) | vb;
					}
				}

				System.arraycopy(operáciePlátna, 0,
					originálPlátna, 0, originálPlátna.length);
			}

			Svet.automatickéPrekreslenie();
			*/
		}

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int, int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int rozsah, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, rozsah, new Farba(bgr, bgg, bgb)); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (opakovanie, }{@code num1}{@code , }{@code 
		 * kwdnew}{@code  }{@link Farba Farba}{@code (bgr, bgg, bgb));}</p>
		 * 
		 * @param opakovanie počet opakovaní rozmazania
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int opakovanie, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, 1, new Farba(bgr, bgg, bgb)); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int, int) rozmaž}.</p> */
		public void rozmaz(int opakovanie, int bgr, int bgg, int bgb)
		{ rozmaž(opakovanie, 1, new Farba(bgr, bgg, bgb)); }

		/**
		 * <p>Rozmaže grafiku tohto plátna. Dosiahneme rovnaký efekt, ako keby
		 * sme volali metódu: {@link #rozmaž(int, int, Color)
		 * rozmaž}{@code (}{@code num1}{@code , }{@code num1}{@code , }{@code 
		 * kwdnew}{@code  }{@link Farba Farba}{@code (bgr, bgg,
		 * bgb));}</p>
		 * 
		 * @param bgr červený komponent pozadia
		 * @param bgg zelený komponent pozadia
		 * @param bgb modrý komponent pozadia
		 */
		public void rozmaž(int bgr, int bgg, int bgb)
		{ rozmaž(1, 1, new Farba(bgr, bgg, bgb)); }

		/** <p><a class="alias"></a> Alias pre {@link #rozmaž(int, int, int) rozmaž}.</p> */
		public void rozmaz(int bgr, int bgg, int bgb)
		{ rozmaž(1, 1, new Farba(bgr, bgg, bgb)); }


		/**
		 * <p>Použije na plátno filter vyrobený zo zložky jasu kresby
		 * zadaného/predloženého obrázka. Zložka priehľadnosti bodov na
		 * obrázku, ktorý poslúži ako predloha pre filter nie je braná do
		 * úvahy. Hodnoty farebných zložiek úplne priehľadných (neviditeľných)
		 * bodov sú nepredvídateľné, preto by mal byť obrázok predlohy filtra
		 * úplne pokrytý nepriehľadnou kresbou. Svetlé/biele body na obrázku
		 * predlohy spôsobia „vymazanie“ bodov na plátne (nastavenie hodnôt
		 * ich priehľadnosti na nulu). Tmavé/čierne body na predloženom
		 * obrázku nespôsobia na plátne žiadnu zmenu priehľadnosti.
		 * (Ostatné odtiene šedej a farebné body vo filtri spôsobia nastavenie
		 * úrovne priehľadnosti bodov plátna na hodnotu jasu vypočítanú
		 * z priemeru farebných zložiek bodov filtra.)
		 * Obrázok s predlohou musí mať rovnaký rozmer ako plátno, inak
		 * operácia zlyhá.</p>
		 * 
		 * @param kresba obrázok, ktorý bude použitý ako predloha na filter
		 * @return {@code valtrue} ak bola operácia úspešná
		 */
		public boolean vymažKresbu(Image kresba)
		{
			boolean úspech = Obrázok.VykonajVObrázku.vymažKresbu(
				obrázokPlátna, Obrázok.preveďNaBufferedImage(kresba));
			if (!Svet.právePrekresľujem)
				Svet.automatickéPrekreslenie();
			return úspech;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažKresbu(Image) vymažKresbu}.</p> */
		public boolean vymazKresbu(Image kresba) { return vymažKresbu(kresba); }


	// Rolovanie a pretáčanie

		/**
		 * <p>Posunie obsah plátna o zadaný počet bodov v horizontálnom a/alebo
		 * vertikálnom smere. Tá časť plátna, ktorá opustí jeho rozmery, bude
		 * stratená, pričom na protiľahlej strane vznikne prázdna oblasť.
		 * Metóda má využitie napríklad pri posune hracej plochy, pričom
		 * musíme zabezpečiť, aby boli vzniknuté prázdne časti plochy
		 * dokreslené. Ak chceme obsah plátna pretočiť dookola (t. j. bez
		 * straty obsahu), musíme použiť metódu {@link #pretoč(double,
		 * double) pretoč}.</p>
		 * 
		 * @param Δx posun v horizontálnom (vodorovnom) smere
		 * @param Δy posun vo vertikálnom (zvislom) smere
		 */
		public void roluj(double Δx, double Δy)
		{
			Obrázok.VykonajVObrázku.roluj(obrázokPlátna, (int)Δx, (int)Δy);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #roluj(double, double) roluj}.</p> */
		public void rolujGrafiku(double Δx, double Δy) { roluj(Δx, Δy); }

		/**
		 * <p>Pretočí obsah plátna o zadaný počet bodov v horizontálnom a/alebo
		 * vertikálnom smere. Tá časť plátna, ktorá by mala pri pretočení
		 * opustiť jeho rozmery, sa objaví na protiľahlej strane. Pretáčaním
		 * plátna v ľubovoľnom smere nikdy nestratíme grafickú informáciu
		 * a spätným posunom dostaneme pôvodný stav. Ak z rôznych dôvodov
		 * potrebujeme, aby sa pri pretáčaní tie časti, ktoré opustia
		 * rozmery plátna stratili a aby vzniknuté prázdne časti zostali
		 * skutočne prázdne (pripravené na ďalšie kreslenie), musíme použiť
		 * metódu {@link #roluj(double, double) roluj}.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <pre CLASS="example">
			{@code kwdimport} knižnica.*;

			{@code kwdpublic} {@code typeclass} PretočeniePlátna {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Konštruktor.}
				{@code kwdprivate} PretočeniePlátna()
				{
					{@code valsuper}({@code num300}, {@code num300});

					{@code comm// Krátky inicializačný kód na nakreslenie pečiatkového farebného}
					{@code comm// vzoru na plátno. (Aby bolo čo pretáčať.)}

					{@link GRobot#skoč(double) skoč}({@code num10});
					{@link GRobot#vpravo(double) vpravo}({@code num30});
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num3});

					{@code kwdfor} ({@code typeint} i = {@code num150}, f = -{@code num5}; i &gt;= {@code num25}; i -= {@code num25})
					{
						{@link GRobot#veľkosť(double) veľkosť}(i);
						{@link GRobot#vypĺňajTvary() vypĺňajTvary}();
						{@link GRobot#farba(Color) farba}({@link Farebnosť#preddefinovanéFarby preddefinovanéFarby}[f += {@code num7}]);
						{@link GRobot#pečiatka() pečiatka}();
						{@link GRobot#nevypĺňajTvary() nevypĺňajTvary}();
						{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
						{@link GRobot#pečiatka() pečiatka}();
						{@link GRobot#odskoč(double) odskoč}({@code num13});
					}

					{@link GRobot#skry() skry}();
				}

				{@code comm// Počiatočná poloha myši používaná na vypočítanie rozdielu určujúceho}
				{@code comm// mieru pretáčania plátna.}
				{@code kwdprivate} {@link Bod Bod} poloha1 = {@code valnull};

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}()
				{
					{@code comm// Uloženie počiatočnej polohy.}
					poloha1 = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
				{
					{@code comm// Ak je počiatočná poloha neprázdna, […]}
					{@code kwdif} ({@code valnull} != poloha1)
					{
						{@code comm// […] tak prevezmeme aktuálnu polohu (pre nás koncovú), […]}
						{@link Bod Bod} poloha2 = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#polohaMyši() polohaMyši}();

						{@code comm// […] vypočítame z oboch polôh rozdiel, o ktorý sa plátno}
						{@code comm// pretočí […]}
						{@link Plátno podlaha}.{@link Plátno#pretoč(double, double) pretoč}(
							poloha2.{@link Poloha#polohaX() polohaX}() - poloha1.{@link Poloha#polohaX() polohaX}(),
							poloha2.{@link Poloha#polohaY() polohaY}() - poloha1.{@link Poloha#polohaY() polohaY}());

						{@code comm// […] a uložíme aktuálnu polohu ako novú počiatočnú polohu.}
						poloha1 = poloha2;
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}()
				{
					{@code comm// Vyprázdnenie počiatočnej polohy.}
					poloha1 = {@code valnull};
				}

				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"PretočeniePlátna.cfg"});
					{@code kwdnew} PretočeniePlátna();
				}
			}
			</pre>
		 * 
		 * <p><b>Výsledok:</b></p>
		 * 
		 * <table class="centered"><tr>
		 * <td><image>pretoc-podlahu-01.png<alt/></image></td>
		 * <td><image>pretoc-podlahu-02.png<alt/></image></td>
		 * </tr><tr><td colspan="2"><p class="image">Ukážka obsahu plátna
		 * tesne po spustení a po chvíli pretáčania.</p></td></tr></table>
		 * 
		 * <!-- TODO: Dopracovať inú ukážku použitia, ktorá predvedie spôsob
		 * výroby bezšvového vzoru. -->
		 * 
		 * @param Δx posun v horizontálnom (vodorovnom) smere
		 * @param Δy posun vo vertikálnom (zvislom) smere
		 */
		public void pretoč(double Δx, double Δy)
		{
			Obrázok.VykonajVObrázku.pretoč(obrázokPlátna, (int)Δx, (int)Δy);
			if (!Svet.právePrekresľujem) Svet.automatickéPrekreslenie();
		}

		/** <p><a class="alias"></a> Alias pre {@link #pretoč(double, double) pretoč}.</p> */
		public void pretoc(double Δx, double Δy) { pretoč(Δx, Δy); }

		/**
		 * <p>Posunie texty vnútornej konzoly o zadaný počet bodov v smere
		 * osí x a y. Použitie metódy má alebo naopak nemá za určitých
		 * okolností zmysel. Okolnosti sú zhrnuté v nasledujúcich bodoch:</p>
		 * 
		 * <ul>
		 * <li>Ak objem textu vnútornej konzoly rozmerovo prekročí rozmery
		 * zobrazenej časti plátna, stáva sa rolovateľným.</li>
		 * <li>Po použití polohovania textu (napríklad metódou {@link 
		 * #píšNa(double, double, Object[]) píšNa(x, y, …)}) je rolovanie
		 * až do najbližšieho {@linkplain #vymažTexty() vymazania textov}
		 * konzoly vypnuté.</li>
		 * <li>Rolovanie v smere osi x je možné len v prípade, že je vypnuté
		 * {@linkplain #nezalamujTexty() zalamovanie textov} konzoly.</li>
		 * <li>Automatické rolovanie kolieskom myši je možné vypnúť
		 * nastavením hodnoty {@code valfalse} (rovnomenného) príznaku
		 * {@link #rolujTexty rolujTexty}.</li>
		 * <li>Aktuálna úroveň posunutia textov je automaticky korigovaná až
		 * počas aktualizácie (zobrazenia) textov na obrazovke tak, aby sa
		 * texty príliš neposunuli voči okrajom plátna smerom dovnútra.</li>
		 * </ul>
		 * 
		 * <p>Všetky relevantné z uvedených informácií sú platné aj pre
		 * ostatné metódy pracujúce s posunom textov vnútornej konzoly…</p>
		 * 
		 * @param Δx zmena posutia v smere x; kladná hodnota posúva texty
		 *     smerom doľava (smerom od pravého okraja odkrývajúc text
		 *     vpravo), záporná naopak (výsledná hodnota posunutia textov môže
		 *     byť prispôsobená počas prekresľovania)
		 * @param Δy zmena posutia v smere y; kladná hodnota posúva texty
		 *     smerom dole (smerom od horného okraja nadol odkrývajúc text
		 *     napísaný vyššie), záporná naopak (výsledná hodnota posunutia
		 *     textov môže byť prispôsobená počas prekresľovania)
		 * 
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 */
		public void rolujTexty(int Δx, int Δy)
		{
			vnútornáKonzola.posunutieTextovX += Δx;
			vnútornáKonzola.posunutieTextovY += Δy;
			Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Posunie texty vnútornej konzoly podľa poslednej udalosti
		 * {@linkplain ÚdajeUdalostí#kolieskoMyši kolieska myši}. Použitie
		 * metódy má alebo naopak nemá za určitých okolností zmysel –
		 * podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * <p>Ak je hodnota rovnomenného príznaku
		 * {@link #rolujTexty rolujTexty} rovná {@code valtrue} (čo je
		 * predvolená hodnota tohto príznaku), tak je táto metóda automaticky
		 * spustenná pri vzniku udalostí rolovania kolieskom myši.</p>
		 * 
		 * <p>Aby bolo rolovanie svižnejšie, je jeden krok rolovania
		 * násobený osemnásobkom objemu rolovania (stanoveného systémom;
		 * zistiteľného volaním: {@link ÚdajeUdalostí#kolieskoMyši()
		 * kolieskoMyši()}{@code .}{@link MouseWheelEvent#getScrollAmount()
		 * getScrollAmount()}).</p>
		 * 
		 * @see ObsluhaUdalostí#rolovanieKolieskomMyši()
		 * @see #rolujTexty(int, int)
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 * @see #rolujTexty
		 */
		public void rolujTexty()
		{
			vnútornáKonzola.posunutieTextovX += 8 *
				ÚdajeUdalostí.rolovanieKolieskomMyšiX *
				ÚdajeUdalostí.poslednáUdalosťRolovania.getScrollAmount();
			vnútornáKonzola.posunutieTextovY += 8 *
				ÚdajeUdalostí.rolovanieKolieskomMyšiY *
				ÚdajeUdalostí.poslednáUdalosťRolovania.getScrollAmount();
			Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Nastaví posunutie textov vnútornej konzoly v smere osi x.
		 * Použitie metódy má alebo naopak nemá za určitých okolností zmysel –
		 * podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @param nováHodnota nová hodnota posunutia textov v smere x
		 *     (pozri aj informácie pri parametroch metódy {@link 
		 *     #rolujTexty(int, int) rolujTexty(Δx, Δy)})
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 */
		public void posunutieTextovX(int nováHodnota)
		{
			vnútornáKonzola.posunutieTextovX = nováHodnota;
			Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Nastaví posunutie textov vnútornej konzoly v smere osi y.
		 * Použitie metódy má alebo naopak nemá za určitých okolností zmysel –
		 * podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @param nováHodnota nová hodnota posunutia textov v smere y
		 *     (pozri aj informácie pri parametroch metódy {@link 
		 *     #rolujTexty(int, int) rolujTexty(Δx, Δy)})
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 */
		public void posunutieTextovY(int nováHodnota)
		{
			vnútornáKonzola.posunutieTextovY = nováHodnota;
			Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Vráti aktuálnu hodnotu posunutia textov vnútornej konzoly v smere
		 * osi x. Použitie metódy má alebo naopak nemá za určitých okolností
		 * zmysel – podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @return aktuálna hodnota posunutia textov v smere x
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 */
		public int posunutieTextovX()
		{ return vnútornáKonzola.posunutieTextovX; }

		/**
		 * <p>Vráti aktuálnu hodnotu posunutia textov vnútornej konzoly v smere
		 * osi y. Použitie metódy má alebo naopak nemá za určitých okolností
		 * zmysel – podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @return aktuálna hodnota posunutia textov v smere y
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextov(int, int)
		 * @see #posunutieTextov()
		 */
		public int posunutieTextovY()
		{ return vnútornáKonzola.posunutieTextovY; }

		/**
		 * <p>Nastaví posunutie textov vnútornej konzoly v smere osí x a y.
		 * Použitie metódy má alebo naopak nemá za určitých okolností zmysel –
		 * podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @param x nová hodnota posunutia textov v smere x
		 *     (pozri aj informácie pri parametroch metódy {@link 
		 *     #rolujTexty(int, int) rolujTexty(Δx, Δy)})
		 * @param y nová hodnota posunutia textov v smere y
		 *     (pozri aj informácie pri parametroch metódy {@link 
		 *     #rolujTexty(int, int) rolujTexty(Δx, Δy)})
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov()
		 */
		public void posunutieTextov(int x, int y)
		{
			vnútornáKonzola.posunutieTextovX = x;
			vnútornáKonzola.posunutieTextovY = y;
			Svet.automatickéPrekreslenie(); // noInvokeLater
		}

		/**
		 * <p>Vráti aktuálnu hodnotu posunutia textov vnútornej konzoly v smere
		 * osí x a y. Použitie metódy má alebo naopak nemá za určitých
		 * okolností zmysel – podrobnosti sú uvedené v opise metódy
		 * {@link #rolujTexty(int, int) rolujTexty(Δx, Δy)}.</p>
		 * 
		 * @return objekt typu {@link Bod Bod} obsahujúci aktuálne
		 *     hodnoty posunutia textov v smere x a y
		 * 
		 * @see #rolujTexty(int, int)
		 * @see #rolujTexty()
		 * @see #posunutieTextovX(int)
		 * @see #posunutieTextovY(int)
		 * @see #posunutieTextovX()
		 * @see #posunutieTextovY()
		 * @see #posunutieTextov(int, int)
		 */
		public Bod posunutieTextov()
		{
			return new Bod(vnútornáKonzola.posunutieTextovX,
				vnútornáKonzola.posunutieTextovY);
		}

		/**
		 * <p>Táto metóda zistí poslednú hodnotu celkovej výšky textu
		 * (v bodoch) uloženého vo vnútornej konzole. <small>(To jest,
		 * aký grafický priestor meraný v bodoch zaberá výpis všetkých
		 * textov vnútornej konzoly na výšku.)</small> Z uvedeného
		 * vyplýva, že na túto hodnotu má vplyv viacero faktorov:
		 * veľkosť písma vnútornej konzoly, zalamovanie riadkov
		 * a samotný objem textu uložený vo vnútornej konzole. Hodnota
		 * nemusí byť aktuálna, ale môže byť prinajmenšom rámcovo
		 * použitá pri určení toho, či má byť používateľovi poskytnutá
		 * možnosť rolovania v smere osi y.</p>
		 * 
		 * @return posledná výška textu vnútornej konzoly v bodoch
		 */
		public int poslednáVýškaTextu()
		{ return vnútornáKonzola.poslednáVýškaTextu; }

		/** <p><a class="alias"></a> Alias pre {@link #poslednáVýškaTextu() poslednáVýškaTextu}.</p> */
		public int poslednaVyskaTextu()
		{ return vnútornáKonzola.poslednáVýškaTextu; }

		/**
		 * <p>Táto metóda zistí poslednú hodnotu maximálnej šírky textu
		 * (v bodoch) uloženého vo vnútornej konzole. <small>(To jest,
		 * aký grafický priestor meraný v bodoch zaberá výpis všetkých
		 * textov vnútornej konzoly na šírku.)</small> Situácia je podobná
		 * ako pri historicky staršej metóde {@link #poslednáVýškaTextu
		 * poslednáVýškaTextu}, podobne aj na túto hodnotu má vplyv
		 * viacero faktorov: veľkosť písma, zalamovanie riadkov… a táto
		 * hodnota môže byť rovnako použitá pri určení toho, či má byť
		 * používateľovi poskytnutá možnosť rolovania v smere osi x.</p>
		 * 
		 * @return posledná šírka textu vnútornej konzoly v bodoch
		 */
		public int poslednáŠírkaTextu()
		{ return vnútornáKonzola.poslednáŠírkaTextu; }

		/** <p><a class="alias"></a> Alias pre {@link #poslednáŠírkaTextu() poslednáŠírkaTextu}.</p> */
		public int poslednaSirkaTextu()
		{ return vnútornáKonzola.poslednáŠírkaTextu; }


	// Schránka

		/**
		 * <p>Vloží do schránky všetky texty vypísané do vnútornej konzoly
		 * tohto plátna.</p>
		 * 
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#text(String)
		 */
		public boolean textyDoSchránky()
		{
			final StringBuffer texty = new StringBuffer();
			texty.setLength(0); vnútornáKonzola.prevezmiTexty(texty);
			return Schránka.text(texty.toString());
		}

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public boolean textyDoSchranky() { return textyDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public boolean textDoSchránky() { return textyDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky() textyDoSchránky}.</p> */
		public boolean textDoSchranky() { return textyDoSchránky(); }


		/**
		 * <p>Vloží do schránky buď všetky texty vypísané do vnútornej konzoly
		 * tohto plátna, alebo len označené časti – v závislosti od hodnoty
		 * parametra {@code lenOznačené}.</p>
		 * 
		 * @param lenOznačené ak je hodnota tohto parametra rovná
		 *     {@code valtrue}, tak metóda skopíruje do schránky len
		 *     označené texty konzoly, inak sa správa rovnako ako metóda
		 *     {@link #textyDoSchránky() textyDoSchránky()}
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#text(String)
		 */
		public boolean textyDoSchránky(boolean lenOznačené)
		{
			final StringBuffer texty = new StringBuffer(); texty.setLength(0);
			vnútornáKonzola.prevezmiTexty(texty, lenOznačené);
			return Schránka.text(texty.toString());
		}

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public boolean textyDoSchranky(boolean lenOznačené) { return textyDoSchránky(lenOznačené); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public boolean textDoSchránky(boolean lenOznačený) { return textyDoSchránky(lenOznačený); }

		/** <p><a class="alias"></a> Alias pre {@link #textyDoSchránky(boolean) textyDoSchránky}.</p> */
		public boolean textDoSchranky(boolean lenOznačený) { return textyDoSchránky(lenOznačený); }


		/**
		 * <p>Vloží obrázok (grafiku) plátna do schránky.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Obrázok je uchovaný
		 * v schránke len počas činnosti aplikácie robota. Po zatvorení
		 * okna sveta, je obrázok zo schránky odstránený.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Zvlnenú verziu obrázka
		 * plátna je možné vložiť do schránky pomocou metódy
		 * {@link Schránka#obrázok(Image) Schránka.obrázok(obrázok)}
		 * a inštancie zvlneného obrázka, ktorá sa dá získať volaním metódy
		 * {@link Vlnenie#zvlnenýRaster() Vlnenie.zvlnenýRaster()}.</p>
		 * 
		 * @return {@code valtrue} ak bola operácia úspešná
		 * 
		 * @see Schránka#obrázok(Image)
		 */
		public boolean obrázokDoSchránky()
		{ return Schránka.obrázok(obrázokPlátna); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public boolean obrazokDoSchranky() { return obrázokDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public boolean grafikaDoSchránky() { return obrázokDoSchránky(); }

		/** <p><a class="alias"></a> Alias pre {@link #obrázokDoSchránky() obrázokDoSchránky}.</p> */
		public boolean grafikaDoSchranky() { return obrázokDoSchránky(); }


	// Interaktívny režim

		/**
		 * <p>Táto metóda má rovnaké jadro ako mechanizmus vykonávania
		 * príkazov v {@linkplain Svet#interaktívnyRežim(boolean)
		 * interaktívnom režime} a umožňuje používať príkazy, ktoré sú
		 * dostupné v tomto režime aj za jeho hranicami (t. j. bez
		 * nevyhnutnosti jeho aktivácie).</p>
		 * 
		 * @param príkaz príkazový riadok spĺňajúci pravidlá uvedené
		 *     v opise metódy {@link Svet#interaktívnyRežim(boolean)
		 *     interaktívnyRežim}
		 * @return {@code valtrue} ak bol príkaz nájdený a podarilo
		 *     sa ho vykonať
		 * 
		 * @see GRobot#vykonajPríkaz(String)
		 * @see Svet#vykonajPríkaz(String)
		 * @see GRobot#interaktívnyRežim(boolean)
		 * @see Svet#interaktívnyRežim(boolean)
		 * @see #interaktívnyRežim(boolean)
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
		public boolean vykonajPríkaz(String príkaz)
		{
			Class<? extends Plátno> tátoTrieda = this.getClass();
			// Táto metóda síce volá súkromnú metódu sveta, ale zadaný
			// príkaz je vykonaný pre túto triedu a inštanciu:
			if (Skript.vykonajPríkaz(príkaz, tátoTrieda, this)) return true;
			return Skript.vykonajPríkaz(príkaz, tátoTrieda, null);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vykonajPríkaz(String) vykonajPríkaz}.</p> */
		public boolean vykonajPrikaz(String príkaz)
		{ return vykonajPríkaz(príkaz); }

		/**
		 * <p>Zapne alebo vypne interaktívny režim pre toto plátno.
		 * Pozri: {@link Svet#interaktívnyRežim(boolean)
		 * Svet.interaktívnyRežim(zapni)}.</p>
		 * 
		 * @param zapni ak je {@code valtrue}, režim bude pre toto plátno
		 *     zapnutý, inak bude vypnutý
		 * 
		 * @see GRobot#interaktívnyRežim(boolean)
		 * @see Svet#interaktívnyRežim(boolean)
		 * @see Svet#režimLadenia(boolean)
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public void interaktívnyRežim(boolean zapni)
		{
			if (zapni)
			{
				if (!interaktívnyRežim)
				{
					interaktívnyRežim = true;
					if (0 == Svet.početVInteraktívnomRežime)
						Svet.neskrývajVstupnýRiadok(true);
					++Svet.početVInteraktívnomRežime;
				}
			}
			else
			{
				if (interaktívnyRežim)
				{
					interaktívnyRežim = false;
					--Svet.početVInteraktívnomRežime;
					if (0 == Svet.početVInteraktívnomRežime)
						Svet.skrývajVstupnýRiadok(true);
				}
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnyRežim(boolean) interaktívnyRežim}.</p> */
		public void interaktivnyRezim(boolean zapni)
		{ interaktívnyRežim(zapni); }

		/**
		 * <p>Zistí stav interaktívneho režimu pre toto plátno.
		 * Pozri: {@link Svet#interaktívnyRežim(boolean)
		 * Svet.interaktívnyRežim(zapni)}.</p>
		 * 
		 * @return stav režimu pre tohto robota
		 */
		public boolean interaktívnyRežim() { return interaktívnyRežim; }

		/** <p><a class="alias"></a> Alias pre {@link #interaktívnyRežim() interaktívnyRežim}.</p> */
		public boolean interaktivnyRezim() { return interaktívnyRežim; }


	// Vlnenie

		// Inštancia vlnenia tohto plátna
		private Vlnenie vlnenie = null;

		/**
		 * <p>Overí, či je definovaná inštancia vlnenia pre toto plátno.</p>
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
		public boolean máVlnenie() { return null != vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #máVlnenie() máVlnenie}.</p> */
		public boolean maVlnenie() { return null != vlnenie; }

		/**
		 * <p>Vráti {@linkplain Vlnenie inštanciu vlnenia} pre toto plátno,
		 * aby bolo možné s vlnením ďalej pracovať. Ak vlnenie nie je pre
		 * toto plátno definované, tak metóda definuje nové neaktívne vlnenie
		 * s predvolenou úrovňou útlmu {@code num26}. <small>(Overiť to,
		 * či je definovaná inštancia vlnenia, je možné pomocou metódy
		 * {@link #máVlnenie() máVlnenie}.)</small> Naopak, metóda
		 * {@link #jestvujúceVlnenie() jestvujúceVlnenie} vráti inštanciu
		 * vlnenia len v takom prípade, že jestvuje. <small>(V opačnom
		 * prípade vráti metóda {@link #jestvujúceVlnenie()
		 * jestvujúceVlnenie} hodnotu {@code valnull}.)</small></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Ak svet grafického robota
		 * nemá aktívny {@linkplain Svet#spustiČasovač() časovač}, tak vlnenie
		 * nebude fungovať ani po jeho aktivácii. Táto metóda <b>nespúšťa</b>
		 * časovač (ani vlnenie) automaticky! Účelom automatického
		 * vytvorenia inštancie vlnenia touto metódou v prípade jej
		 * neprítomnosti je len zabránenie vzniku chýb. Táto metóda nemá
		 * nahradiť metódu {@link #pridajVlnenie() pridajVlnenie}.</p>
		 * 
		 * @return metóda zaručuje vrátenie inštancie {@link Vlnenie Vlnenie}
		 *     definovanej pre toto plátno aj v takom prípade, že pred jej
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
		public Vlnenie vlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokPlátna, 26);
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
		public Vlnenie jestvujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie jestvujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie existujúceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie existujuceVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie definovanéVlnenie() { return vlnenie; }

		/** <p><a class="alias"></a> Alias pre {@link #jestvujúceVlnenie() jestvujúceVlnenie}.</p> */
		public Vlnenie definovaneVlnenie() { return vlnenie; }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto plátna. Ak nie je definované
		 * alebo aktívne vlnenie pre toto plátno, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia s predvolenou
		 * úrovňou útlmu {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
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
		 * je definovaná inštancia vlnenia, je možné pomocou metódy
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
		public void pridajVlnenie()
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokPlátna, 26);
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
		// public void zacniVlnenie() { pridajVlnenie(); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto plátna. Ak nie je definované
		 * alebo aktívne vlnenie pre toto plátno, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia s predvolenou
		 * úrovňou útlmu {@code num26}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
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
		 * <p class="caution"><b>Pozor!</b> ak vlnenie nie je definované,
		 * tak metóda {@link #vlnenie() vlnenie} definuje nové neaktívne
		 * vlnenie s predvolenou úrovňou útlmu {@code num26}. Overiť to, či
		 * je definovaná inštancia vlnenia, je možné pomocou metódy
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
		public void pridajVlnenie(boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokPlátna, 26);
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
		// public void zacniVlnenie(boolean ajČasovač) { pridajVlnenie(ajČasovač); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto plátna. Ak nie je definované
		 * alebo aktívne vlnenie pre toto plátno, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia so zadanou úrovňou
		 * útlmu (pozri aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)}).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
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
		 * je definovaná inštancia vlnenia, je možné pomocou metódy
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
		public void pridajVlnenie(int útlm)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokPlátna, útlm);
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
		// public void zacniVlnenie(int útlm) { pridajVlnenie(útlm); }

		/**
		 * <p>Pridá alebo zresetuje vlnenie tohto plátna. Ak nie je definované
		 * alebo aktívne vlnenie pre toto plátno, tak volanie tejto metódy
		 * vytvorí a/alebo aktivuje novú inštanciu vlnenia so zadanou úrovňou
		 * útlmu (pozri aj {@link Vlnenie#útlm(int) Vlnenie.útlm(útlm)})</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Aby mohlo byť vlnenie automaticky
		 * vykonávané, tak v prípade vytvorenia novej inštancie ju táto
		 * metóda automaticky registruje vo svete grafického robota.</p>
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
		 * je definovaná inštancia vlnenia, je možné pomocou metódy
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
		public void pridajVlnenie(int útlm, boolean ajČasovač)
		{
			if (null == vlnenie)
			{
				vlnenie = new Vlnenie(obrázokPlátna, útlm);
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

		/**
		 * <p>Ukončí vlnenie a úplne odstráni inštanciu vlnenia tohto plátna
		 * z prostredia programovacieho rámca GRobot.</p>
		 * 
		 * @see #máVlnenie()
		 * @see #vlnenie()
		 * @see #jestvujúceVlnenie()
		 * @see #pridajVlnenie()
		 * @see #pridajVlnenie(boolean)
		 * @see #pridajVlnenie(int)
		 * @see #pridajVlnenie(int, boolean)
		 */
		public void odstráňVlnenie()
		{
			if (null != vlnenie)
			{
				Vlnenie.vlnenia.remove(vlnenie);
				vlnenie = null;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #odstráňVlnenie() odstráňVlnenie}.</p> */
		public void odstranVlnenie() { odstráňVlnenie(); }


	// --- Osvetlenie

		// Rastre osvetlenia:
		private Obrazok osvetlenie = null;
		private BufferedImage osvetlenýRaster = null;

		// Údaje rastra osvetlenia:
		private int[] údajeOsvetleného = null, údajeOsvetlenia = null;

		// Metóda aplikujúca osvetlenie
		private BufferedImage osvetliRaster(BufferedImage naOsvetlenie)
		{
			int[] údajeNaOsvetlenie =
				((DataBufferInt)naOsvetlenie.getRaster().
					getDataBuffer()).getData();

			for (int i = 0; i < údajeOsvetleného.length; ++i)
			{
				int a = údajeNaOsvetlenie[i] & 0xff000000;
				int r = ((údajeNaOsvetlenie[i] >> 16) & 0xff) *
					((údajeOsvetlenia[i] >> 16) & 0xff) / 0x80;
				int g = ((údajeNaOsvetlenie[i] >>  8) & 0xff) *
					((údajeOsvetlenia[i] >>  8) & 0xff) / 0x80;
				int b = ( údajeNaOsvetlenie[i]        & 0xff) *
					( údajeOsvetlenia[i]        & 0xff) / 0x80;

				if (r > 0xff) r = 0xff;
				if (g > 0xff) g = 0xff;
				if (b > 0xff) b = 0xff;

				// a je už „prepočítané“/má správnu hodnotu (a << 24):
				údajeOsvetleného[i] = a | (r << 16) | (g << 8) | b;
			}

			return osvetlenýRaster;
		}

		/**
		 * <p>Overí, či má toto plátno aktívne osvetlenie. Ak je osvetlenie
		 * aktívne, tak metóda vráti inštanciu obrázka, ktorý je vnútorne
		 * používaný ako mapa osvetlenia plátna – pozri aj opis metód
		 * {@link #osvetlenie(boolean) osvetlenie} a {@link 
		 * Obrázok#svetlo(Obrázok, Obrázok) Obrázok.svetlo(…)}, inak vráti
		 * {@code valnull}.</p>
		 * 
		 * @return inštancia obrázka používaného ako svetelná mapa alebo
		 *     {@code valnull}
		 */
		public Obrazok osvetlenie() { return osvetlenie; }

		/**
		 * <p>Zapne alebo vypne osvetlenie tohto plátna. Pri zmene stavu: Ak
		 * je osvetlenie aktivované, tak sú vytvorené potrebné inštancie
		 * a zároveň táto metóda vráti inštanciu obrázka, ktorý bude vnútorne
		 * používaný ako mapa osvetlenia – pozri aj opis metódy {@link 
		 * Obrázok#svetlo(Obrázok, Obrázok) Obrázok.svetlo(…)}. Ak je
		 * osvetlenie deaktivované alebo jeho aktivácia zlyhá, tak metóda
		 * vráti {@code valnull}.</p>
		 * 
		 * @param osvetli ak je {@code valtrue} tak má byť osvetlenie
		 *     aktivované, ak je {@code valfalse} tak má byť osvetlenie
		 *     deaktivované
		 * @return inštancia obrázka používaného ako svetelná mapa alebo
		 *     {@code valnull}
		 */
		public Obrazok osvetlenie(boolean osvetli)
		{
			try
			{
				if (osvetli && null == osvetlenie)
				{
					osvetli = false;
	
					osvetlenie = new Obrazok(Plátno.šírkaPlátna,
						Plátno.výškaPlátna);
	
					údajeOsvetlenia =
						((DataBufferInt)osvetlenie.getRaster().
						getDataBuffer()).getData();
	
					osvetlenýRaster = new BufferedImage(
						Plátno.šírkaPlátna, Plátno.výškaPlátna,
						BufferedImage.TYPE_INT_ARGB);
	
					údajeOsvetleného =
						((DataBufferInt)osvetlenýRaster.getRaster().
						getDataBuffer()).getData();
	
					osvetli = true;
				}
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
			finally
			{
				if (!osvetli)
				{
					// if (null != údajeOsvetlenia)
						údajeOsvetlenia = null;

					// if (null != údajeOsvetleného)
						údajeOsvetleného = null;

					// if (null != osvetlenýRaster)
						osvetlenýRaster = null;

					if (null != osvetlenie)
					{
						try { Svet.uvoľni(osvetlenie); } catch (Exception e)
						{ GRobotException.vypíšChybovéHlásenia(e, true); }
						catch (Throwable t) { t.printStackTrace(); }
						osvetlenie = null;
					}
				}

				try { Svet.automatickéPrekreslenie(); } catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e, true); }
				catch (Throwable t) { t.printStackTrace(); }
			}

			return osvetlenie;
		}


	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }
}

// :wrap=none:
