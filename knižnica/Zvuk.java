
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2023 by Roman Horváth
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

import java.io.File;

import java.util.TreeMap;
import java.util.Vector;

import java.net.URL;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


import knižnica.podpora.BeepChannel;


// --------------------- //
//  *** Trieda Zvuk ***  //
// --------------------- //

/**
 * <p>Táto trieda dovoľuje programátorovi podrobnejšie pracovať so zvukom,
 * ktorý prečítal pomocou metódy {@link Svet#čítajZvuk(String)
 * čítajZvuk}.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <pre CLASS="example">
	{@code currZvuk} môjZvuk = {@link Svet Svet}.{@link Svet#čítajZvuk(String) čítajZvuk}({@code srg"zvuk.wav"});
	môjZvuk.{@link #prehraj() prehraj}();
	{@code comm// …}
	</pre>
 * 
 * <p class="image"><a href="resources/zvuk.wav"
 * target="_blank"><image>zvuk-small.png<alt/>Grafické znázornenie
 * obsahu zvukového súboru „zvuk.wav.“</image>Zvuk
 * „zvuk.wav“ na prevzatie.</a></p>
 * 
 * <p style="text-align: center;"><audio controls><source
 * src="resources/zvuk.wav" type="audio/wav">Váš prehliadač
 * neumožňuje prehratie zvuku.</audio></p>
 * 
 * <p class="remark"><b>Poznámka:</b> Programovací rámec obsahuje aj
 * nástroje na generovanie zvukov (resp. tónov). Ich implementácia je
 * realizovaná v rámci skupiny podporných tried rámca, ku ktorým nie je
 * priamo vyhotovená dokumentácia, ale základ je sprístupnený
 * prostredníctvom skupiny metód v triede {@link Svet Svet}. Príklady na
 * generovanie zvukov (resp. tónov) sa nachádzajú v opisoch metód {@link 
 * Svet#otvorSúborNaUloženieTónu(String, boolean)
 * Svet.otvorSúborNaUloženieTónu(názov, prepísať)} a {@link 
 * Svet#generátorTónov() Svet.generátorTónov()}.</p>
 * 
 * @see Svet#priečinokZvukov(String)
 * @see Svet#priečinokZvukov()
 * @see Svet#čítajZvuky(Object[])
 * @see Svet#čítajZvuky(String[])
 * @see Svet#čítajZvuk(String)
 * @see Svet#zvuk(String)
 * @see Svet#zvukNaPozadí(String)
 * @see Svet#zastavZvuky(Object[])
 * @see Svet#hlasitosťPreZvuky(double, Object[])
 * @see Svet#váhaPreZvuky(double, Object[])
 * @see Svet#zastavZvuky(String[])
 * @see Svet#hlasitosťPreZvuky(double, String[])
 * @see Svet#váhaPreZvuky(double, String[])
 */
public class Zvuk
{
	// Užitočné zdroje informácií:
	// 
	//  http://www.jsresources.org/faq_audio.html
	//  http://onjava.com/onjava/excerpt/jenut3_ch17/index.html
	//  http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
	//  http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml

	// Zoznam zvukových klipov prečítaných zo súboru

		/*packagePrivate*/ static String priečinokZvukov = "";

		@SuppressWarnings("serial")
		/*packagePrivate*/ static class ZoznamZvukov extends Vector<Zvuk> {}

		/*packagePrivate*/ final static TreeMap<String, ZoznamZvukov>
			zoznamSúborovZvukov = new TreeMap<>();


	// Zvuk prehrávaný na pozadí metódou zvukNaPozadí()

		/*packagePrivate*/ static Zvuk zvukNaPozadí = null;

	// Kanál generátora tónov

		/*packagePrivate*/ static BeepChannel kanál = null;


	// Práca s vnútornými zoznamami objektov

		// Metódy prijímajú názov súboru ako reťazec, vyhľadajú súbor
		// a prečítajú z neho objekt. Ak už bol raz objekt prečítaný,
		// metóda ho vráti priamo, bez opätovného čítania zo súboru.

		// Transformuje názov súboru na zvuk

			/*packagePrivate*/ static ZoznamZvukov zoznamZvukovSúboru(String súbor)
			{
				ZoznamZvukov zoznamZvukov = zoznamSúborovZvukov.get(súbor);
				if (null == zoznamZvukov)
				{
					súborNaZvuk(súbor, true);
					zoznamZvukov = zoznamSúborovZvukov.get(súbor);
				}
				return zoznamZvukov;
			}

			/*packagePrivate*/ static Zvuk súborNaZvuk(String súbor)
			{
				return súborNaZvuk(súbor, false);
			}

			/*packagePrivate*/ static Zvuk súborNaZvuk(
				String súbor, boolean unikátny)
			{
				ZoznamZvukov zoznamZvukov = zoznamSúborovZvukov.get(súbor);

				if (null == zoznamZvukov)
				{
					zoznamZvukov = new ZoznamZvukov();
					zoznamSúborovZvukov.put(súbor, zoznamZvukov);
					unikátny = true;
				}

				if (!unikátny)
					for (Zvuk zvuk : zoznamZvukov)
						if (!zvuk.prehrávaSa()) return zvuk;


				URL url = null;

				try
				{
					File zvukovýSúbor = Súbor.nájdiSúbor(
						priečinokZvukov + súbor);
					if (zvukovýSúbor.canRead())
						url = zvukovýSúbor.toURI().toURL();
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}

				if (null == url)
				{
					// Prečítaj z lokality URL (mimo pevného disku)
					try
					{
						url = new URL(súbor);
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenia(e/*, false*/);
					}
				}

				// V prípade, že by bol v .jar súbore
				if (null == url) url = Súbor.nájdiZdroj(
					priečinokZvukov + súbor);
				if (null == url) throw new GRobotException("Zvuk „" +
					súbor + "“ nebol nájdený.", "soundNotFound", súbor);

				Zvuk zvuk = čítaj(url);
				if (null == zvuk) throw new GRobotException
					("Zvuk „" + súbor + "“ nie je možné prečítať.",
						"soundUnreadable", súbor);

				zoznamZvukov.add(zvuk);
				return zvuk;
			}


	private Clip klip;
	private FloatControl hlasitosť = null;
	private FloatControl váha = null;
	private boolean dookola = false;

	private MediaPlayer prehrávač;

	private Zvuk(Clip klip)
	{
		this.klip = klip;
		prehrávač = null;
	}

	private Zvuk(MediaPlayer prehrávač)
	{
		klip = null;
		this.prehrávač = prehrávač;
		this.prehrávač.setOnEndOfMedia(() ->
		{
			if (MediaPlayer.INDEFINITE !=
					this.prehrávač.getCycleCount() &&
				this.prehrávač.getCurrentCount() >=
					this.prehrávač.getCycleCount())
				this.prehrávač.stop();
		});
	}

	private static JFXPanel jfxPanel = null;

	////////////////////////////////////////////////////////////////
	// Táto metóda je využívaná automaticky všetkými metódami
	// pracujúcimi so zvukmi. Pozri napríklad:
	//
	//    Svet.čítajZvuky(Object[])
	//    Svet.čítajZvuk(String)
	//    Svet.zvuk(String)
	//    Svet.zvukNaPozadí(String)
	//
	private static Zvuk čítaj(URL url)
	{
		String súbor = url.getFile();
		String typ = súbor.substring(súbor.
			lastIndexOf('.') + 1).toLowerCase();

		if (typ.equals("mp3")
			// Unsupported: || typ.equals("ogg")
			)
		{
			try
			{
				if (null == jfxPanel) jfxPanel = new JFXPanel();
				Media médium = new Media(url.toString());
				MediaPlayer prehrávač = new MediaPlayer(médium);
				return new Zvuk(prehrávač);
			}
			catch (Exception e)
			{
				//*##*/ e.printStackTrace();
				// Nevadí, pokračujeme ďalej…
			}
		}


		Clip klip = null;
		AudioInputStream vstup = null;

		try
		{
			vstup = AudioSystem.getAudioInputStream(url);
		}
		catch (Exception e) { return null; }

		try
		{
			klip = AudioSystem.getClip();
			klip.open(vstup);
		}
		catch (Exception e) { return null; }
		finally
		{
			try
			{
				vstup.close();
			}
			catch (Exception e) { return null; }
		}

		return new Zvuk(klip);
	}

	// Uvoľní zdroje zvuku
	/*packagePrivate*/ void uvoľni()
	{
		if (null != prehrávač)
		{
			prehrávač.dispose();
			prehrávač = null;
		}

		if (null != klip)
		{
			try
			{
				klip.close();
			}
			catch (Exception e)
			{
				//*##*/ e.printStackTrace();
				// Čo už, neurobíme s tým nič…
			}

			klip = null;
			hlasitosť = null;
			váha = null;
			dookola = false;
		}
	}


		/**
		 * <p>Ak sú všetky zvuky uložené v spoločnom priečinku, môžeme pre
		 * nich touto metódou nastaviť zdrojový priečinok čítania.
		 * Priečinok by sa mal nachádzať v hlavnom priečinku projektu alebo by
		 * k nemu mala viesť systémovo nezávislá relatívna cesta. Zadaním
		 * prázdneho reťazca alebo hodnoty {@code valnull} používanie
		 * priečinka zrušíme.</p>
		 * 
		 * @param priečinok názov priečinka, relatívna cesta, prípadne
		 *     prázdny reťazec alebo {@code valnull}
		 * 
		 * @see Svet#priečinokZvukov()
		 * @see Zvuk
		 */
		public static void priečinokZvukov(String priečinok)
		{ priečinokZvukov = Súbor.upravLomky(priečinok, true); }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokZvukov(String) priečinokZvukov}.</p> */
		public static void priecinokZvukov(String priečinok)
		{ priečinokZvukov = Súbor.upravLomky(priečinok, true); }

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
		 * @see Svet#priečinokZvukov(String)
		 * @see #čítaj(String)
		 * @see #čítaj(String, boolean)
		 */
		public static String priečinokZvukov()
		{ return priečinokZvukov; }

		/** <p><a class="alias"></a> Alias pre {@link #priečinokZvukov() priečinokZvukov}.</p> */
		public static String priecinokZvukov()
		{ return priečinokZvukov; }


	/**
	 * <p>Prečíta a uloží do vnútornej pamäte programovacieho rámca
	 * (metaforicky sveta) zadaný zvuk zo súboru a vráti ho ako inštanciu
	 * tejto triedy ({@link Zvuk Zvuk}). (Zvuk nie je prehraný.) Táto
	 * metóda funguje rovnako ako metóda sveta {@link 
	 * Svet#čítajZvuk(String) Svet.čítajZvuk(súbor)} (v jej opise sa dá
	 * nájsť o niečo viac informácií – najmä odkazy na súvisiace
	 * metódy sveta).</p>
	 * 
	 * <p>Táto metóda funguje tak, že podľa potreby vytvára nové unikátne
	 * inštancie zvuku identifikovaného názvom súboru. Nová inštancia
	 * vznikne vždy, ak sa vo vnútornej pamäti sveta nenájde taká
	 * inštancia určeného zvuku, ktorá práve nie je prehrávaná (to
	 * znamená, že metóda musí nájsť zvuk, ktorý je ticho). Takto fungujú
	 * všetky metódy {@linkplain Svet sveta} pracujúce so zvukom. Ak by
	 * sa takto nesprávali, tak by vznikalo nežiaduce správanie – vždy
	 * pri pokuse o prehranie toho istého zvuku (identifikovaného názvom
	 * súboru) počas jeho prehrávania, by sa tento reštartoval. (Nebolo
	 * by možné prehrať sériu rovnakých zvukov, ktoré sa časovo
	 * prekrývajú (hoci niekedy len mierne). To by pôsobilo neprirodzene –
	 * každé reštartovanie zvuku by bolo počuteľné tak, ako keby sa zo
	 * zvuku zrazu časť „odsekla.“)</p>
	 * 
	 * @param súbor názov súboru so zvukom
	 * @return zvuk v objekte typu {@link Zvuk Zvuk}
	 * 
	 * @throws GRobotException ak súbor so zvukom nebol nájdený
	 * 
	 * @see #čítaj(String, boolean)
	 * @see Svet#priečinokZvukov(String)
	 * @see Svet#čítajZvuk(String)
	 * @see Svet#čítajZvuk(String, boolean)
	 */
	public static Zvuk čítaj(String súbor)
	{ return súborNaZvuk(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public static Zvuk citaj(String súbor)
	{ return súborNaZvuk(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public static Zvuk prečítaj(String súbor)
	{ return súborNaZvuk(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public static Zvuk precitaj(String súbor)
	{ return súborNaZvuk(súbor); }


	/**
	 * <p>Prečíta a uloží do vnútornej pamäte programovacieho rámca
	 * (metaforicky sveta) zadaný zvuk zo súboru a vráti ho ako inštanciu
	 * tejto triedy ({@link Zvuk Zvuk}). (Zvuk nie je prehraný.) Ak je
	 * parameter {@code unikátny} rovný {@code valfalse}, tak táto metóda
	 * funguje rovnako ako metóda {@link #čítaj(String) čítaj(súbor)}.</p>
	 * 
	 * <p>V prípade, že je parameter {@code unikátny} rovný {@code 
	 * valtrue}, tak táto metóda <b>vždy</b> vráti unikátny objekt zvuku,
	 * čiže aj v prípade, že jestvuje aspoň jedna kópia tohto zvuku, ktorá
	 * sa práve neprehráva.</p>
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
	 * @see #čítaj(String)
	 * @see Svet#priečinokZvukov(String)
	 * @see Svet#čítajZvuk(String)
	 * @see Svet#čítajZvuk(String, boolean)
	 */
	public static Zvuk čítaj(String súbor, boolean unikátny)
	{ return súborNaZvuk(súbor, unikátny); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String, boolean) čítaj}.</p> */
	public static Zvuk citaj(String súbor, boolean unikátny)
	{ return súborNaZvuk(súbor, unikátny); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String, boolean) čítaj}.</p> */
	public static Zvuk prečítaj(String súbor, boolean unikátny)
	{ return súborNaZvuk(súbor, unikátny); }

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String, boolean) čítaj}.</p> */
	public static Zvuk precitaj(String súbor, boolean unikátny)
	{ return súborNaZvuk(súbor, unikátny); }


	/**
	 * <p>Zastaví prehrávanie zvuku.</p>
	 */
	public void zastav()
	{
		if (null != prehrávač)
		{
			prehrávač.stop();
			// ## prehrávač.seek(Duration.ZERO);
			// prehrávač.seek(Duration.INDEFINITE);
		}

		if (null != klip)
		{
			klip.stop();
			klip.setFramePosition(0);
		}
	}

	/**
	 * <p>Spustí prehrávanie zvuku.</p>
	 */
	public void prehraj()
	{
		if (null != prehrávač)
		{
			// ## prehrávač.stop();
			prehrávač.seek(Duration.ZERO);

			prehrávač.setCycleCount(1);
			prehrávač.play();
		}

		if (null != klip)
		{
			klip.setFramePosition(0);
			klip.start();
			dookola = false;
		}
	}

	/**
	 * <p>Spustí prehrávanie zvuku v nekonečnom cykle.</p>
	 */
	public void prehrávaťDookola()
	{
		if (null != prehrávač)
		{
			// ## prehrávač.stop();
			prehrávač.seek(Duration.ZERO);

			prehrávač.setCycleCount(MediaPlayer.INDEFINITE);
			prehrávač.play();
		}

		if (null != klip)
		{
			klip.setFramePosition(0);
			klip.loop(Clip.LOOP_CONTINUOUSLY);
			dookola = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaťDookola() prehrávaťDookola}.</p> */
	public void prehravatDookola() { prehrávaťDookola(); }

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaťDookola() prehrávaťDookola}.</p> */
	public void opakovaťDookola() { prehrávaťDookola(); }

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaťDookola() prehrávaťDookola}.</p> */
	public void opakovatDookola() { prehrávaťDookola(); }

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaťDookola() prehrávaťDookola}.</p> */
	public void cyklickyOpakovať() { prehrávaťDookola(); }

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaťDookola() prehrávaťDookola}.</p> */
	public void cyklickyOpakovat() { prehrávaťDookola(); }


	/**
	 * <p>Pozastaví prehrávanie zvuku.</p>
	 */
	public void pozastav()
	{
		if (null != prehrávač)
		{
			prehrávač.pause();
		}

		if (null != klip)
		{
			klip.stop();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pozastav() pozastav}.</p> */
	public void pauza() { pozastav(); }

	/**
	 * <p>Spustí pozastavené prehrávanie zvuku.</p>
	 */
	public void pokračuj()
	{
		if (null != prehrávač)
		{
			prehrávač.play();
		}

		if (null != klip)
		{
			if (dookola)
				klip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				klip.start();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pokračuj() pokračuj}.</p> */
	public void pokracuj() { pokračuj(); }

	/**
	 * <p>Zistí trvanie zvuku v sekundách.</p>
	 * 
	 * @return trvanie zvuku v sekundách
	 */
	public double trvanie()
	{
		if (null != prehrávač)
			return prehrávač.getStopTime().toSeconds();

		if (null != klip)
			return (double)klip.getMicrosecondLength() / 1E6D;

		return 0;
	}

	/**
	 * <p>Nastaví polohu prehrávania zvuku v sekundách (vzhľadom od
	 * začiatku).</p>
	 * 
	 * @param poloha poloha zvuku v sekundách
	 */
	public void poloha(double poloha)
	{
		if (null != prehrávač)
			prehrávač.seek(Duration.seconds(poloha));

		if (null != klip)
			klip.setMicrosecondPosition((long)(poloha * 1E6D));
	}

	/**
	 * <p>Zistí polohu prehrávania zvuku v sekundách (vzhľadom od
	 * začiatku).</p>
	 * 
	 * @return poloha zvuku v sekundách
	 */
	public double poloha()
	{
		if (null != prehrávač)
			return prehrávač.getCurrentTime().toSeconds();

		if (null != klip)
			return (double)klip.getMicrosecondPosition() / 1E6D;

		return 0;
	}

	/**
	 * <p>Zistí, či sa zvuk práve prehráva alebo nie.</p>
	 * 
	 * @return {@code valtrue} – zvuk sa prehráva;
	 *     {@code valfalse} – zvuk sa neprehráva
	 */
	public boolean prehrávaSa()
	{
		if (null != prehrávač)
		{
			// ## System.out.println("Current time: " + prehrávač.getCurrentTime());
			// ## System.out.println("Stop time: " + prehrávač.getStopTime());
			// ## System.out.println("Compare: " + prehrávač.getCurrentTime().greaterThanOrEqualTo(prehrávač.getStopTime()));

			// ## if (prehrávač.getCurrentTime().greaterThanOrEqualTo(
			// ## 	prehrávač.getStopTime()))
			// ## {
			// ## 	// ## System.out.println("Considered as not playing.");
			// ## 	return false;
			// ## }

			// ## System.out.println("Status: " + prehrávač.getStatus() + " (" + MediaPlayer.Status.PLAYING + " = " + (prehrávač.getStatus() == MediaPlayer.Status.PLAYING) + ")");

			return prehrávač.getStatus() == MediaPlayer.Status.PLAYING;
		}

		if (null != klip)
			return klip.isActive();

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #prehrávaSa() prehrávaSa}.</p> */
	public boolean prehravaSa() { return prehrávaSa(); }


	/**
	 * <p>Zistí, či je (resp. nie je) týmto objektom podporovaná schopnosť
	 * zmeny hlasitosti. (Za normálnych okolností by metóda mala vracať
	 * hodnotu {@code valfalse}. To znamená, že hlasitosť zvuku je
	 * podporovaná.)</p>
	 * 
	 * @return {@code valtrue} – vlastnosť nie je podporovaná;
	 *     {@code valfalse} – vlastnosť je podporovaná
	 */
	public boolean hlasitosťNepodporovaná()
	{
		if (null != prehrávač) return false;

		if (null == hlasitosť)
		{
			if (klip.isControlSupported(FloatControl.Type.MASTER_GAIN))
				hlasitosť = (FloatControl)
					klip.getControl(FloatControl.Type.MASTER_GAIN);
			else return true;
		}

		return null == hlasitosť;
	}

	/** <p><a class="alias"></a> Alias pre {@link #hlasitosťNepodporovaná() hlasitosťNepodporovaná}.</p> */
	public boolean hlasitostNepodporovana()
	{ return hlasitosťNepodporovaná(); }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu mieru hlasitosti
	 * prehrávania tohto zvuku.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@link Zvuk Zvuk} môjZvuk = {@link Svet Svet}.{@link Svet#čítajZvuk(String) čítajZvuk}({@code srg"zvuk.wav"});

		môjZvuk.{@link #hlasitosť(double) hlasitosť}({@code num2.0});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť "} + ({@code typeint})(môjZvuk.{@code currhlasitosť}() * {@code num100}) + {@code srg"%"});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		môjZvuk.{@link #hlasitosť(double) hlasitosť}({@code num0.5});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť "} + ({@code typeint})(môjZvuk.{@code currhlasitosť}() * {@code num100}) + {@code srg"%"});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		môjZvuk.{@link #hlasitosť(double) hlasitosť}(&#45;{@code num1.0});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť "} + ({@code typeint})(môjZvuk.{@code currhlasitosť}() * {@code num100}) + {@code srg"%"});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});
		</pre>
	 * 
	 * <p class="image"><a href="resources/zvuk.wav"
	 * target="_blank"><image>zvuk-small.png<alt/>Grafické znázornenie
	 * obsahu zvukového súboru „zvuk.wav.“</image>Zvuk
	 * „zvuk.wav“ na prevzatie.</a></p>
	 * 
	 * <p style="text-align: center;"><audio controls><source
	 * src="resources/zvuk.wav" type="audio/wav">Váš prehliadač
	 * neumožňuje prehratie zvuku.</audio></p>
	 * 
	 * @return reálne číslo vyjadrujúce mieru hlasitosti v percentách;
	 *     0 % – {@code num0.0}; 100 % – {@code num1.0}
	 */
	public double hlasitosť()
	{
		if (null != prehrávač)
			return prehrávač.getVolume();

		if (hlasitosťNepodporovaná()) return 1.0;

		float dB = hlasitosť.getValue();
		double miera = Math.pow(10.0, (double)dB / 20.0);

		return miera;
	}

	/** <p><a class="alias"></a> Alias pre {@link #hlasitosť() hlasitosť}.</p> */
	public double hlasitost() { return hlasitosť(); }

	/**
	 * <p><a class="setter"></a> Upraví mieru hlasitosti pri prehrávaní
	 * tohto zvuku. Metóda prijíma reálne číslo vyjadrujúce mieru
	 * hlasitosti v percentách, pričom 100 % zodpovedá hodnote
	 * {@code num1.0}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@link Zvuk Zvuk} môjZvuk = {@link Svet Svet}.{@link Svet#čítajZvuk(String) čítajZvuk}({@code srg"zvuk.wav"});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť 75%"});
		môjZvuk.{@code currhlasitosť}({@code num0.75});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť 50%"});
		môjZvuk.{@code currhlasitosť}({@code num0.5});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Hlasitosť 25%"});
		môjZvuk.{@code currhlasitosť}({@code num0.25});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});
		</pre>
	 * 
	 * <p class="image"><a href="resources/zvuk.wav"
	 * target="_blank"><image>zvuk-small.png<alt/>Grafické znázornenie
	 * obsahu zvukového súboru „zvuk.wav.“</image>Zvuk
	 * „zvuk.wav“ na prevzatie.</a></p>
	 * 
	 * <p style="text-align: center;"><audio controls><source
	 * src="resources/zvuk.wav" type="audio/wav">Váš prehliadač
	 * neumožňuje prehratie zvuku.</audio></p>
	 * 
	 * @param miera miera hlasitosti v percentách – 0 % zodpovedá hodnote
	 *     {@code num0.0}, 100 % hodnote {@code num1.0}
	 */
	public void hlasitosť(double miera)
	{
		if (null != prehrávač)
		{
			prehrávač.setVolume(miera);
			return;
		}

		if (hlasitosťNepodporovaná()) return;

		float dB = (float)(Math.log10(miera) * 20.0);

		if (dB > hlasitosť.getMaximum()) dB = hlasitosť.getMaximum();
		if (dB < hlasitosť.getMinimum()) dB = hlasitosť.getMinimum();


		if (klip.isActive())
		{
			klip.stop();
			hlasitosť.setValue(dB);
			if (dookola)
				klip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				klip.start();
		}
		else
		{
			hlasitosť.setValue(dB);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #hlasitosť(double) hlasitosť}.</p> */
	public void hlasitost(double miera) { hlasitosť(miera); }


	/**
	 * <p>Zistí, či je (resp. nie je) týmto objektom podporovaná schopnosť
	 * zmeny stereováhy (to jest určenie vyváženia prehrávania v ľavom
	 * alebo pravom kanáli).</p>
	 * 
	 * @return {@code valtrue} – vlastnosť nie je podporovaná;
	 *     {@code valfalse} – vlastnosť je podporovaná
	 */
	public boolean váhaNepodporovaná()
	{
		if (null != prehrávač) return false;

		if (null == váha)
		{
			if (klip.getFormat().getChannels() == 1 &&
				klip.isControlSupported(FloatControl.Type.PAN))
				váha = (FloatControl)
					klip.getControl(FloatControl.Type.PAN);
			else if (klip.getFormat().getChannels() == 2 &&
				klip.isControlSupported(FloatControl.Type.BALANCE))
				váha = (FloatControl)
					klip.getControl(FloatControl.Type.BALANCE);
			else return true;
		}

		return null == váha;
	}

	/** <p><a class="alias"></a> Alias pre {@link #váhaNepodporovaná() váhaNepodporovaná}.</p> */
	public boolean vahaNepodporovana() { return váhaNepodporovaná(); }

	/**
	 * <p><a class="getter"></a> Vráti aktuálnu mieru stereovyváženia
	 * prehrávania tohto zvuku.</p>
	 * 
	 * @return reálne číslo od {@code -}{@code num1.0} (ľavý kanál)
	 *     po {@code +}{@code num1.0} (pravý kanál) vyjadrujúce mieru
	 *     stereovyváženia
	 */
	public double váha()
	{
		if (null != prehrávač)
			return prehrávač.getBalance();

		if (váhaNepodporovaná()) return 0.0;
		return váha.getValue();
	}

	/** <p><a class="alias"></a> Alias pre {@link #váha() váha}.</p> */
	public double vaha() { return váha(); }

	/**
	 * <p><a class="setter"></a> Upraví mieru stereovyváženia prehrávania
	 * tohto zvuku vzhľadom na ľavý a pravý kanál. Metóda prijíma reálne
	 * číslo vyjadrujúce mieru vyváženia v rozsahu od
	 * {@code -}{@code num1.0} (ľavý kanál) po
	 * {@code +}{@code num1.0} (pravý kanál).</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@link Zvuk Zvuk} môjZvuk = {@link Svet Svet}.{@link Svet#čítajZvuk(String) čítajZvuk}({@code srg"zvuk.wav"});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Prehraj"});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vľavo 50%"});
		môjZvuk.{@code currváha}(&#45;{@code num0.5});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vpravo 50%"});
		môjZvuk.{@code currváha}({@code num0.5});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vľavo 100%"});
		môjZvuk.{@code currváha}(&#45;{@code num1.0});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vpravo 100%"});
		môjZvuk.{@code currváha}({@code num1.0});
		môjZvuk.{@link #prehraj() prehraj}();
		{@link Svet Svet}.{@link Svet#čakaj(double) čakaj}({@code num1.0});

		môjZvuk.{@code currváha}({@code num0.0});    {@code comm// na stred}
		</pre>
	 * 
	 * <p class="image"><a href="resources/zvuk.wav"
	 * target="_blank"><image>zvuk-small.png<alt/>Grafické znázornenie
	 * obsahu zvukového súboru „zvuk.wav.“</image>Zvuk
	 * „zvuk.wav“ na prevzatie.</a></p>
	 * 
	 * <p style="text-align: center;"><audio controls><source
	 * src="resources/zvuk.wav" type="audio/wav">Váš prehliadač
	 * neumožňuje prehratie zvuku.</audio></p>
	 * 
	 * @param miera miera stereovyváženia – reálne číslo od
	 *     {@code -}{@code num1.0} (ľavý kanál) po
	 *     {@code +}{@code num1.0} (pravý kanál)
	 */
	public void váha(double miera)
	{
		if (null != prehrávač)
		{
			prehrávač.setBalance(miera);
			return;
		}

		if (váhaNepodporovaná()) return;

		if (miera > váha.getMaximum()) miera = váha.getMaximum();
		if (miera < váha.getMinimum()) miera = váha.getMinimum();


		if (klip.isActive())
		{
			klip.stop();
			váha.setValue((float)miera);
			if (dookola)
				klip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				klip.start();
		}
		else
		{
			váha.setValue((float)miera);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #váha(double) váha}.</p> */
	public void vaha(double miera) { váha(miera); }
}
