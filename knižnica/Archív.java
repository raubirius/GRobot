
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

package knižnica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
// import java.io.UnsupportedEncodingException;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import knižnica.apacheAntZIP.ZipEntry;
import knižnica.apacheAntZIP.ZipFile;
import knižnica.apacheAntZIP.ZipOutputStream;

import static java.util.Calendar.*;

/**
 * <p>TODO</p>
 * 
 * 
 * 
 * <p><b>Príklad:</b></p>
 * <!-- TODO dokončiť príklad aj jeho opis -->
 * <pre CLASS="example">
	import knižnica.*;

	public class PripojenieArchívu extends GRobot
	{
		private PripojenieArchívu()
		{
			new ObsluhaUdalostí()
			{
				@Override public boolean konfiguráciaZmenená()
				{
					// Overenie podmienok a ak treba zapísať konfiguráciu,
					// tak návrat: return true;
					return false;
				}

				@Override public void zapíšKonfiguráciu(Súbor súbor)
					throws java.io.IOException
				{
					// Zápis vlastných vlastností:
					// súbor.zapíšVlastnosť("názov", hodnota);
					// …
				}

				@Override public void čítajKonfiguráciu(Súbor súbor)
					throws java.io.IOException
				{
					// Čítanie vlastných vlastností:
					// hodnota = súbor.čítajVlastnosť("názov", predvolenáHodnota);
					// …
				}
			};
		}

		public static void main(String[] args)
		{
			// Pripojenie archívu ku konfigurácii:
			Svet.konfiguračnýSúbor().pripojArchív(
				new Archív("PripojenieArchívu.zip"));

			// Použitie konfigurácie:
			Svet.použiKonfiguráciu("PripojenieArchívu.cfg");

			// Konštrukcia:
			new PripojenieArchívu();
		}
	}
	</pre>

 * <p>Trieda je navrhnutá tak, aby sa dala používať aj bez nevyhnutnosti
 * obsluhy výnimiek.</p>
 */
public class Archív implements Closeable
{
	// ‼TODO‼ Aliasy!

	// TODO: Keď bude čas, implementovať mechanizmus (ne)ignorovania chýb.
	// metódou ignorujChyby(boolean) sa to zapne/vypne, keď je to zapnuté,
	// tak všetky metódy budú silou mocou obchádzať vrhanie výnimiek a ak
	// to pre nich bude možné, tak sa budú usilovať indikovať chybový stav
	// nejako inak (návratovou hodnotou, inak?). (V každom prípade: denník
	// funguje.)
	// Ak je to vypnuté, tak budú vrhať výnimky aj metódy ako veľkosťPoložky
	// a pod.
	// Lenže toto isté by sa hodilo implementovať aj do triedy Súbor.


	// Príznak signalizujúci, či môžu inštancie iných tried používajúcich
	// tento archív ho automaticky otvoriť v prípadoch, keď nebol otvorený.
	/*packagePrivate*/ boolean umožniťAutomatickéOtvorenie;

	// Meno súboru s archívom.
	private String názov = null;

	// Vstupný archív.
	/*packagePrivate*/ ZipFile vstup = null;

	// Výstupný archív.
	/*packagePrivate*/ ZipOutputStream výstup = null;

	// Aktuálna cesta vo vnútri archívu.
	/*packagePrivate*/ String cestaVArchíve = null;

	// Akutálna cesta na pevnom disku.
	private String cestaNaDisku = null;

	// Mapa položiek výstupu na ich dodatočné spracovanie.
	/*packagePrivate*/ TreeMap<String, ZipEntry> položkyVýstupu =
		new TreeMap<>();


	/**
	 * <p>Predvolený konštruktor. Po konštrukcii môže programátor otvoriť
	 * archív na {@linkplain #otvorNaČítanie(String) čítanie} alebo
	 * {@linkplain #otvorNaZápis(String) zápis}.</p>
	 * 
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 */
	public Archív() { umožniťAutomatickéOtvorenie = false; }

	/**
	 * <p>Konštruktor prijímajúci názov archívu. Po tomto type konštrukcie
	 * môže programátor otvoriť archív metódami {@link #otvorNaČítanie()
	 * otvorNaČítanie()} a {@link #otvorNaZápis() otvorNaZápis()}, prípade
	 * prekryť názov súboru a to buď metódou {@link #názov(String) názov},
	 * alebo otvorením metódami {@link #otvorNaČítanie(String)
	 * otvorNaČítanie(názov)} a {@link #otvorNaZápis(String)
	 * otvorNaZápis(názov)}. Ak bude zadaný názov archívu nenulový (nemajúci
	 * hodnotu {@code valnull}), tak bude zároveň povolené {@linkplain 
	 * #umožniťAutomatickéOtvorenie(boolean) automatické otvorenie}
	 * archívu.</p>
	 * 
	 * @param názov meno archívu
	 * 
	 * @see #názov(String)
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 */
	public Archív(String názov)
	{
		názov(názov);
		umožniťAutomatickéOtvorenie = null != názov;
	}

	/**
	 * <p>Konštruktor prijímajúci cestu na disku a názov archívu. Po tomto
	 * type konštrukcie môže programátor otvoriť archív metódami {@link 
	 * #otvorNaČítanie() otvorNaČítanie()} a {@link #otvorNaZápis()
	 * otvorNaZápis()}, prípade prekryť cestu a/alebo názov súboru niektorou
	 * zo súvisiacich metód: {@link #cestaNaDisku(String) cestaNaDisku(cesta)},
	 * {@link #názov(String) názov}, {@link #otvorNaČítanie(String)
	 * otvorNaČítanie(názov)} alebo {@link #otvorNaZápis(String)
	 * otvorNaZápis(názov)}. Ak bude zadaný názov archívu nenulový (nemajúci
	 * hodnotu {@code valnull}), tak bude zároveň povolené {@linkplain 
	 * #umožniťAutomatickéOtvorenie(boolean) automatické otvorenie}
	 * archívu.</p>
	 * 
	 * @param cesta pracovná cesta (na pevnom disku)
	 * @param názov meno archívu (tiež)
	 * 
	 * @see #cestaNaDisku(String)
	 * @see #názov(String)
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 */
	public Archív(String cesta, String názov)
	{
		cestaNaDisku(cesta);
		názov(názov);
		umožniťAutomatickéOtvorenie = null != názov;
	}


	/**
	 * <p>Vráti aktuálny názov tohto archívu alebo {@code valnull}, ak nie
	 * je nastavený žiadny názov.</p>
	 * 
	 * @return aktuálny názov archívu alebo {@code valnull}
	 * 
	 * @see #názov(String)
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 */
	public String názov() { return názov; }

	/** <p><a class="alias"></a> Alias pre {@link #názov() názov}.</p> */
	public String nazov() { return názov(); }

	/**
	 * <p>Nastaví alebo zruší názov tohto archívu. Táto metóda zavrie
	 * prípadný otvorený archív. Hodnota {@code valnull} ruší nastavenie
	 * názvu archívu.</p>
	 * 
	 * @param názov nový názov archívu alebo {@code valnull}
	 * 
	 * @see #názov()
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 */
	public void názov(String názov)
	{
		try { zavri(); } catch (IOException e)
		{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
		this.názov = názov;
	}

	/** <p><a class="alias"></a> Alias pre {@link #názov(String) názov}.</p> */
	public void nazov(String názov) { názov(názov); }


	/**
	 * <p>Overí, či smie byť umožnené automatické otvorenie tohto archívu
	 * inými inštanciami programovacieho rámca, ktoré ho budú chcieť použiť.
	 * Podrobnosti o tejto vlastnosti sú v opise metódy {@link 
	 * #umožniťAutomatickéOtvorenie(boolean) umožniťAutomatickéOtvorenie}</p>
	 * 
	 * @return pravdivostná hodnota vyjadrujúca stav povolenia tejto
	 *     vlastnosti
	 * 
	 * @see #umožniťAutomatickéOtvorenie(boolean)
	 * @see #Archív(String)
	 * @see #Archív(String, String)
	 */
	public boolean umožniťAutomatickéOtvorenie()
	{ return umožniťAutomatickéOtvorenie; }

	/** <p><a class="alias"></a> Alias pre {@link #umožniťAutomatickéOtvorenie() umožniťAutomatickéOtvorenie}.</p> */
	public boolean umoznitAutomatickeOtvorenie()
	{ return umožniťAutomatickéOtvorenie(); }

	/**
	 * <p>Nastaví príznak toho, či smie byť umožnené automatické otvorenie
	 * tohto archívu inými inštanciami programovacieho rámca, ktoré ho budú
	 * chcieť použiť. Napríklad trieda {@link Súbor Súbor} s {@linkplain 
	 * #pripojArchív(Archív) pripojeným archívom}. Automatické otvorenie
	 * vyžaduje, aby mal archív priradený {@linkplain #názov(String) názov}.
	 * To sa dá docieliť rôznymi spôsobmi – prislúchajúcim konštruktorom
	 * ({@link #Archív(String) Archív(názov)} alebo {@link #Archív(String,
	 * String) Archív(cesta, názov)}), metódou {@link #názov(String) názov}
	 * alebo otvorením archívu niektorou z metód:
	 * {@link #otvorNaČítanie(String) otvorNaČítanie(názov)},
	 * {@link #otvorNaZápis(String) otvorNaZápis(názov)}. Predvolená hodnota
	 * závisí od spôsobu konštrukcie. Platí zásada, že ak je pri konštrukcii
	 * inštancie zadaný nenulový názov (nemajúci hodnotu {@code valnull}),
	 * tak je hodnota vlastnosti automatického otvorenia nastavená na
	 * {@code valtrue}, inak na {@code valfalse}.</p>
	 * 
	 * @param umožniťAutomatickéOtvorenie pravdivostná hodnota vyjadrujúca
	 *     stav povolenia tejto vlastnosti
	 * 
	 * @see #umožniťAutomatickéOtvorenie()
	 * @see #Archív(String)
	 * @see #Archív(String, String)
	 * @see #názov(String)
	 */
	public void umožniťAutomatickéOtvorenie(
		boolean umožniťAutomatickéOtvorenie)
	{
		this.umožniťAutomatickéOtvorenie =
			umožniťAutomatickéOtvorenie;
	}

	/** <p><a class="alias"></a> Alias pre {@link #umožniťAutomatickéOtvorenie(boolean) umožniťAutomatickéOtvorenie}.</p> */
	public void umoznitAutomatickeOtvorenie(
		boolean umožniťAutomatickéOtvorenie)
	{ umožniťAutomatickéOtvorenie(umožniťAutomatickéOtvorenie); }


	/**
	 * <p>Vráti aktuálnu cestu nastavenú v rámci obsahu archívu alebo
	 * {@code valnull}, ak nie je nastavená žiadna cesta.</p>
	 * 
	 * @return aktuálna cesta vo vnútri archívu alebo {@code valnull}
	 * 
	 * @see #cestaVArchíve(String)
	 * @see #cestaNaDisku()
	 * @see #cestaNaDisku(String)
	 */
	public String cestaVArchíve() { return cestaVArchíve; }

	/** <p><a class="alias"></a> Alias pre {@link #cestaVArchíve() cestaVArchíve}.</p> */
	public String cestaVArchive() { return cestaVArchíve(); }

	/**
	 * <p>Nastaví alebo zruší novú cestu v rámci obsahu archívu. Hodnota
	 * {@code valnull} ruší nastavenie cesty.</p>
	 * 
	 * @param cesta nová cesta vo vnútri archívu alebo {@code valnull}
	 * 
	 * @see #cestaVArchíve()
	 * @see #cestaNaDisku()
	 * @see #cestaNaDisku(String)
	 */
	public void cestaVArchíve(String cesta)
	{
		if (null == cesta)
			cestaVArchíve = null;
		else if (-1 != cesta.indexOf('\\'))
			cestaVArchíve = cesta.replace('\\', '/');
		else
			cestaVArchíve = cesta;

		if (null != cestaVArchíve &&
			!cestaVArchíve.endsWith("/"))
			cestaVArchíve += "/";
	}

	/** <p><a class="alias"></a> Alias pre {@link #cestaVArchíve(String) cestaVArchíve}.</p> */
	public void cestaVArchive(String cesta) { cestaVArchíve(cesta); }


	/**
	 * <p>Vráti aktuálnu pracovnú cestu na pevnom disku alebo
	 * {@code valnull}, ak nie je nastavená žiadna cesta.</p>
	 * 
	 * <p>Pracovná cesta je použitá pri otváraní archívu (na
	 * {@linkplain #otvorNaČítanie(String) čítanie} alebo
	 * {@linkplain #otvorNaZápis(String) zápis}) a pri
	 * {@linkplain #pridajPoložku(String, String) pridávaní
	 * položiek z pevného disku}.</p>
	 * 
	 * @return aktuálna pracovná cesta alebo {@code valnull}
	 * 
	 * @see #cestaNaDisku(String)
	 * @see #cestaVArchíve()
	 * @see #cestaVArchíve(String)
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 * @see #pridajPoložku(String, String)
	 */
	public String cestaNaDisku() { return cestaNaDisku; }

	/**
	 * <p>Nastaví alebo zruší pracovnú cestu na pevnom disku. Hodnota
	 * {@code valnull} ruší nastavenie cesty.</p>
	 * 
	 * <p>Pracovná cesta je použitá pri otváraní archívu (na
	 * {@linkplain #otvorNaČítanie(String) čítanie} alebo
	 * {@linkplain #otvorNaZápis(String) zápis}) a pri
	 * {@linkplain #pridajPoložku(String, String) pridávaní
	 * položiek z pevného disku}.</p>
	 * 
	 * @param cesta nová pracovná cesta alebo {@code valnull}
	 * 
	 * @see #cestaNaDisku()
	 * @see #cestaVArchíve()
	 * @see #cestaVArchíve(String)
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 * @see #pridajPoložku(String, String)
	 */
	public void cestaNaDisku(String cesta)
	{
		if (null == cesta)
			cestaNaDisku = null;
		else if (-1 != cesta.indexOf('\\'))
			cestaNaDisku = cesta.replace('\\', '/');
		else
			cestaNaDisku = cesta;

		if (null != cestaNaDisku &&
			!cestaNaDisku.endsWith("/"))
			cestaNaDisku += "/";
	}


	/**
	 * <p>Zavrie archív, ktorý bol predtým otvorený na {@linkplain 
	 * #otvorNaČítanie(String) čítanie} alebo {@linkplain 
	 * #otvorNaZápis(String) zápis}.</p>
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 * @see #close()
	 */
	public void zavri() throws IOException
	{
		IOException ioe = null;

		if (null != vstup)
		{
			try
			{
				vstup.close();
			}
			catch (IOException e)
			{
				ioe = e;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}
			vstup = null;
		}

		if (null != výstup)
		{
			try
			{
				výstup.close();
			}
			catch (IOException e)
			{
				ioe = e;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}
			výstup = null;
		}

		if (null != ioe) throw ioe;
	}

	/**
	 * <p>Zavrie archív, ktorý bol predtým otvorený na {@linkplain 
	 * #otvorNaČítanie(String) čítanie} alebo {@linkplain 
	 * #otvorNaZápis(String) zápis}. Táto metóda je definovaná ako
	 * súčasť implementácie rozhrania {@link Closeable Closeable}.
	 * Vnútorne volá metódu {@link #zavri() zavri}.</p>
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis()
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 * @see #zavri()
	 */
	public void close() throws IOException { zavri(); }


	/**
	 * <p>Otvorí archív so zadaným menom na čítanie. Ak súbor s archívom
	 * nejestvuje, tak vznikne výnimka. Musí ísť o fyzický súbor na pevnom
	 * disku umiestnený na aktuálnej lokalite alebo na ceste určenej
	 * vlastnosťou {@link #cestaNaDisku(String) cestaNaDisku}.</p>
	 * 
	 * @return „vstupný archív“ knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> {@link ZipFile ZipFile} na
	 *     vykonanie prípadných ďalších úprav (nastavenie komentára, úprava
	 *     rôznych konfiguračných položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #Archív(String)
	 * @see #názov(String)
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipFile otvorNaČítanie() throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();

		vstup = null == cestaNaDisku ? new ZipFile(new File(
			názov), "UTF-8") : new ZipFile(new File(cestaNaDisku,
				názov), "UTF-8");

		return vstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaČítanie() otvorNaČítanie}.</p> */
	public ZipFile otvorNaCitanie() throws IOException
	{ return otvorNaČítanie(); }

	/**
	 * <p>Otvorí archív so zadaným menom na zápis. Zapisovaný súbor bude
	 * vytvorený alebo prepísaný buď na aktuálnom umiestnení, alebo na
	 * ceste určenej vlastnosťou {@link #cestaNaDisku(String)
	 * cestaNaDisku}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak použijete objekt {@link 
	 * ZipOutputStream ZipOutputStream}, ktorý je návratovou hodnotou tejto
	 * metódy na pridávanie nových položiek, tak táto trieda programovacieho
	 * rámca nebude schopná detegovať duplicitné položky, ani spätne
	 * pracovať s pridanými položkami, pretože ich evidenciu vykonáva
	 * vo vlastnej réžii nad rámec možností triedy {@link ZipOutputStream
	 * ZipOutputStream}.</p>
	 * 
	 * @return „výstupný archív“ (prúd) knižnice <a
	 *     href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 *     {@link ZipOutputStream ZipOutputStream} na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára, úprava rôznych konfiguračných
	 *     položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #Archív(String)
	 * @see #názov(String)
	 * @see #otvorNaZápis(String)
	 * @see #otvorNaČítanie(String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipOutputStream otvorNaZápis() throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();

		výstup = null == cestaNaDisku ? new ZipOutputStream(
			new File(názov)) : new ZipOutputStream(new File(
				cestaNaDisku, názov));

		výstup.setEncoding("UTF-8");
		položkyVýstupu.clear();
		return výstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis() otvorNaZápis}.</p> */
	public ZipOutputStream otvorNaZapis() throws IOException
	{ return otvorNaZápis(); }


	/**
	 * <p>Otvorí archív so zadaným menom na čítanie. Ak súbor s archívom
	 * nejestvuje, tak vznikne výnimka. Musí ísť o fyzický súbor na pevnom
	 * disku umiestnený na aktuálnej lokalite alebo na ceste určenej
	 * vlastnosťou {@link #cestaNaDisku(String) cestaNaDisku}.</p>
	 * 
	 * @param názov meno archívu
	 * @return „vstupný archív“ knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> {@link ZipFile ZipFile} na
	 *     vykonanie prípadných ďalších úprav (nastavenie komentára, úprava
	 *     rôznych konfiguračných položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaČítanie()
	 * @see #otvorNaZápis(String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipFile otvorNaČítanie(String názov) throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();
		názov(názov);

		vstup = null == cestaNaDisku ? new ZipFile(new File(
			názov), "UTF-8") : new ZipFile(new File(cestaNaDisku,
				názov), "UTF-8");

		return vstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaČítanie(String) otvorNaČítanie}.</p> */
	public ZipFile otvorNaCitanie(String názov) throws IOException
	{ return otvorNaČítanie(názov); }


	/**
	 * <p>Otvorí archív so zadaným menom na zápis. Zapisovaný súbor bude
	 * vytvorený alebo prepísaný buď na aktuálnom umiestnení, alebo na
	 * ceste určenej vlastnosťou {@link #cestaNaDisku(String)
	 * cestaNaDisku}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak použijete objekt {@link 
	 * ZipOutputStream ZipOutputStream}, ktorý je návratovou hodnotou tejto
	 * metódy na pridávanie nových položiek, tak táto trieda programovacieho
	 * rámca nebude schopná detegovať duplicitné položky, ani spätne
	 * pracovať s pridanými položkami, pretože ich evidenciu vykonáva
	 * vo vlastnej réžii nad rámec možností triedy {@link ZipOutputStream
	 * ZipOutputStream}.</p>
	 * 
	 * @param názov meno archívu
	 * @return „výstupný archív“ (prúd) knižnice <a
	 *     href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 *     {@link ZipOutputStream ZipOutputStream} na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára, úprava rôznych konfiguračných
	 *     položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaZápis()
	 * @see #otvorNaČítanie(String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipOutputStream otvorNaZápis(String názov) throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();
		názov(názov);

		výstup = null == cestaNaDisku ? new ZipOutputStream(
			new File(názov)) : new ZipOutputStream(new File(
				cestaNaDisku, názov));

		výstup.setEncoding("UTF-8");
		položkyVýstupu.clear();
		return výstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis(String) otvorNaZápis}.</p> */
	public ZipOutputStream otvorNaZapis(String názov) throws IOException
	{ return otvorNaZápis(názov); }


	/**
	 * <p>Vráti aktuálny počet položiek archívu.</p>
	 * 
	 * @return počet položiek uložených v archíve
	 */
	public int početPoložiek()
	{
		if (null != výstup)
			return položkyVýstupu.size();

		if (null != vstup)
		{
			Enumeration<? extends ZipEntry> položky = vstup.getEntries();
			int počet = 0; while (položky.hasMoreElements()) ++počet;
			return počet;
		}

		// TODO TEST

		return -1;
	}

	/** <p><a class="alias"></a> Alias pre {@link #početPoložiek() početPoložiek}.</p> */
	public int pocetPoloziek() { return početPoložiek(); }

	/**
	 * <p>Vytvorí a vráti aktuálny zoznam položiek v archíve vo forme
	 * poľa reťazcov.</p>
	 * 
	 * @return aktuálny zoznam položiek v archíve vo forme poľa reťazcov
	 */
	public String[] zoznamPoložiek()
	{
		if (null != výstup)
		{
			Set<String> položky = položkyVýstupu.keySet();
			return položky.toArray(new String[položky.size()]);
		}

		if (null != vstup)
		{
			Enumeration<? extends ZipEntry> položky = vstup.getEntries();
			TreeSet<String> položkyVstupu = new TreeSet<>();

			while (položky.hasMoreElements())
			{
				ZipEntry položka = položky.nextElement();
				// InputStream stream = vstup.getInputStream(položka);
				položkyVstupu.add(položka.getName());
			}

			return položkyVstupu.toArray(
				new String[položkyVstupu.size()]);
		}

		// TODO TEST

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zoznamPoložiek() zoznamPoložiek}.</p> */
	public String[] zoznamPoloziek() { return zoznamPoložiek(); }


	/**
	 * <p>Zostaví reťazec dátumu v predvolenom formáte zo zadaného počtu
	 * milisekúnd počítaných od začiatku takzvanej epochy – polnoc 1.
	 * januára 1970 greenwichského času (v našom časovom pásme to znamená
	 * jednu hodinu v noci).</p>
	 * 
	 * <p>Predvolený formát má tvar:
	 * <em>«deň»</em>. <em>«mesiac»</em>. <em>«rok»</em>,
	 * <em>«hodina»</em>:<em>«minúta»</em>:<em>«sekunda»</em>,
	 * pričom hodiny, minúty a sekundy sú nulou zarovnané zľava
	 * na dva znaky.</p>
	 * 
	 * @param miliDátum dátum v milisekundách epochy
	 * @return reťazec dátumu v predvolenom formáte
	 * 
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 */
	public static String dátumNaReťazec(long miliDátum)
	{
		Calendar kalendár = Calendar.getInstance();
		kalendár.setTimeInMillis(miliDátum);
		int deň = kalendár.get(DAY_OF_MONTH);
		int mesiac = kalendár.get(MONTH) + 1;
		int rok = kalendár.get(YEAR);
		int hodina = kalendár.get(HOUR_OF_DAY);
		int minúta = kalendár.get(MINUTE);
		int sekunda = kalendár.get(SECOND);
		return deň + ". " + mesiac + ". " + rok + ", " +
			String.format(Locale.ENGLISH, "%02d", hodina) + ":" +
			String.format(Locale.ENGLISH, "%02d", minúta) + ":" +
			String.format(Locale.ENGLISH, "%02d", sekunda);
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumNaReťazec(long) dátumNaReťazec}.</p> */
	public static String datumNaRetazec(long miliDátum)
	{ return dátumNaReťazec(miliDátum); }

	/**
	 * <p>Podľa zadaných údajov vytvorí reťazec dátumu. Prvý parameter je
	 * počet milisekúnd počítaných od začiatku takzvanej epochy – polnoc 1.
	 * januára 1970 greenwichského času (v našom časovom pásme to znamená
	 * jednu hodinu v noci) a druhý parameter určuje požadovaný
	 * formát vo veľmi priamočiarom tvare. Vo výslednom reťazci budú
	 * nahradené tieto rezervované písmená (záleží aj na ich veľkosti)
	 * nasledujúcimi údajmi: D - deň, M - mesiac, R alebo Y - rok,
	 * h - hodina, m - minúta, s - sekunda. Ak za sebou nasleduje niekoľko
	 * rovnakých písmen, tak výsledný číselný údaj bude zarovnaný nulami
	 * zľava na zadaný počet znakov.<!-- Ak sa za sebou opakujú iné znaky,
	 * budú zlúčené do jedného výskytu toho znaku.--></p>
	 * 
	 * @param miliDátum dátum v milisekundách epochy
	 * @param formát reťazec určujúci formát dátumu
	 * @return reťazec dátumu v zadanom formáte
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 */
	public static String dátumNaReťazec(long miliDátum, String formát)
	{
		if (null == formát || formát.isEmpty()) return "";
		Calendar kalendár = Calendar.getInstance();
		kalendár.setTimeInMillis(miliDátum);
		int deň = kalendár.get(DAY_OF_MONTH);
		int mesiac = kalendár.get(MONTH) + 1;
		int rok = kalendár.get(YEAR);
		int hodina = kalendár.get(HOUR_OF_DAY);
		int minúta = kalendár.get(MINUTE);
		int sekunda = kalendár.get(SECOND);
		if (1 == formát.length())
		{
			switch (formát.charAt(0))
			{
			case 'D': return "" + deň;
			case 'M': return "" + mesiac;
			case 'R': case 'Y': return "" + rok;
			case 'h': return "" + hodina;
			case 'm': return "" + minúta;
			case 's': return "" + sekunda;
			default: return formát;
			}
		}
		StringBuilder sb = new StringBuilder();
		int zarovnaj = 1; char posledný = formát.charAt(0), znak = 0;
		for (int i = 1; i < formát.length(); ++i)
		{
			znak = formát.charAt(i);
			if (znak != posledný)
			{
				switch (posledný)
				{
				case 'D': sb.append(String.format(Locale.ENGLISH, "%0" +
					zarovnaj + "d", deň)); break;
				case 'M': sb.append(String.format(Locale.ENGLISH, "%0" +
					zarovnaj + "d", mesiac)); break;
				case 'R': case 'Y': sb.append(String.format(Locale.ENGLISH,
					"%0" + zarovnaj + "d", rok)); break;
				case 'h': sb.append(String.format(Locale.ENGLISH, "%0" +
					zarovnaj + "d", hodina)); break;
				case 'm': sb.append(String.format(Locale.ENGLISH, "%0" +
					zarovnaj + "d", minúta)); break;
				case 's': sb.append(String.format(Locale.ENGLISH, "%0" +
					zarovnaj + "d", sekunda)); break;
				default: for (int j = 0; j < zarovnaj; ++j)
					sb.append(posledný);
				}
				zarovnaj = 1;
			}
			else ++zarovnaj;
			posledný = znak;
		}
		switch (posledný)
		{
		case 'D': sb.append(String.format(Locale.ENGLISH, "%0" +
			zarovnaj + "d", deň)); break;
		case 'M': sb.append(String.format(Locale.ENGLISH, "%0" +
			zarovnaj + "d", mesiac)); break;
		case 'R': case 'Y': sb.append(String.format(Locale.ENGLISH,
			"%0" + zarovnaj + "d", rok)); break;
		case 'h': sb.append(String.format(Locale.ENGLISH, "%0" +
			zarovnaj + "d", hodina)); break;
		case 'm': sb.append(String.format(Locale.ENGLISH, "%0" +
			zarovnaj + "d", minúta)); break;
		case 's': sb.append(String.format(Locale.ENGLISH, "%0" +
			zarovnaj + "d", sekunda)); break;
		default: for (int j = 0; j < zarovnaj; ++j)
			sb.append(posledný);
		}
		return sb.toString();
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumNaReťazec(long, String) dátumNaReťazec}.</p> */
	public static String datumNaRetazec(long miliDátum, String formát)
	{ return dátumNaReťazec(miliDátum, formát); }

	/**
	 * <p>Vráti dátum v milisekundách počítaných od začiatku takzvanej
	 * epochy (polnoc 1. januára 1970 greenwichského času – v našom časovom
	 * pásme to znamená jednu hodinu v noci) podľa zadaného reťazca
	 * v predvolenom formáte.</p>
	 * 
	 * <p>Predvolený formát reťazca má tvar skupiny celých čísiel oddelených
	 * ľubovoľnými nečíselnými znakmi. Jediné, na čom záleží, je poradie
	 * výskytu číselných údajov v reťazci a to je stanovené takto: deň,
	 * mesiac, rok, hodina, minúta, sekunda. Znak mriežky ({@code srg'#'})
	 * je rezervovaný a je považovaný za jeden úplný prázdny číselný údaj,
	 * to znamená, že reťazec {@code srg"###"} znamená prázdny deň, mesiac
	 * a rok (hoci medzi znakmi nie sú oddeľovače). Pri výskyte tohto znaku
	 * je prislúchajúcemu údaju ponechaná predvolená hodnota. Chýbajúcim
	 * údajom sú ponechané predvolené hodnoty (pri začatí rozpoznávania
	 * dátumu je jeho hodnota nastavená na začiatok epochy).</p>
	 * 
	 * <!--p>Platnými znakmi oddeľovačov sú: medzera ({@code srg' '}), pevná
	 * medzera ({@code srg' '}), bodka ({@code srg'.'}), čiarka ({@code 
	 * srg','}), dvojbodka ({@code srg':'}), tabulátor ({@code srg'\t'}),
	 * nový riadok ({@code srg'\n'}), návrat vozíka ({@code srg'\r'})
	 * a posun formulára ({@code srg'\f'}). Za jeden oddeľovač je považovaná
	 * ľubovoľne veľká skupina zložená z uvedených znakov. Ostatné znaky
	 * (samozrejme okrem číslic) sú považované za neplatné a sú ignorované
	 * (opomenúc fakt, že sa zapisujú do {@linkplain GRobotException#denník
	 * denníka chýb}).</p-->
	 * 
	 * @param dátum reťazec dátumu v predvolenom formáte
	 * @return dátum v milisekundách epochy
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String, String)
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 */
	public static long reťazecNaDátum(String dátum)
	{
		Calendar kalendár = Calendar.getInstance();
		kalendár.setTimeInMillis(0);

		int údaj = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < dátum.length(); ++i)
		{
			char ch = dátum.charAt(i);

			if ('#' == ch) ++údaj;
			else if ('0' <= ch && '9' >= ch) sb.append(ch);
			else
			{
				if (0 != sb.length())
				{
					try
					{
						int číslo = Integer.parseInt(sb.toString());

						switch (údaj)
						{
						case 0: kalendár.set(DAY_OF_MONTH, číslo); break;
						case 1: kalendár.set(MONTH, číslo - 1); break;
						case 2: kalendár.set(YEAR, číslo); break;
						case 3: kalendár.set(HOUR_OF_DAY, číslo); break;
						case 4: kalendár.set(MINUTE, číslo); break;
						case 5: kalendár.set(SECOND, číslo); break;

						default:
							GRobotException.vypíšChybovéHlásenie(
								"Príliš veľa (" + (údaj + 1) +
								") číselných údajov v dátume – na znaku " +
								(i + 1) + " (" + ch + ") v reťazci „" +
								dátum + "“."/*, false*/);
						}
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenie(
							"Chyba pri nastavovaní údaju dátumu…"/*, false*/);
						GRobotException.vypíšChybovéHlásenia(e/*, false*/);
					}

					sb.setLength(0);
					++údaj;
				}

				/*if (' ' == ch || ' ' == ch || '.' == ch ||
					',' == ch || ':' == ch || '\t' == ch ||
					'\n' == ch || '\r' == ch || '\f' == ch)
					GRobotException.vypíšChybovéHlásenie(
						"Neplatný znak pri zisťovaní dátumu: " +
						ch/*, false* /);*/
			}
		}

		if (0 != sb.length())
		{
			try
			{
				int číslo = Integer.parseInt(sb.toString());

				switch (údaj)
				{
				case 0: kalendár.set(DAY_OF_MONTH, číslo); break;
				case 1: kalendár.set(MONTH, číslo - 1); break;
				case 2: kalendár.set(YEAR, číslo); break;
				case 3: kalendár.set(HOUR_OF_DAY, číslo); break;
				case 4: kalendár.set(MINUTE, číslo); break;
				case 5: kalendár.set(SECOND, číslo); break;

				default:
					GRobotException.vypíšChybovéHlásenie(
						"Príliš veľa (" + (údaj + 1) +
						") číselných údajov v dátume – na poslednom " +
						"znaku v reťazci „" + dátum + "“."/*, false*/);
				}
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Chyba pri nastavovaní údaju dátumu…"/*, false*/);
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}
		}

		return kalendár.getTimeInMillis();
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaDátum(String) reťazecNaDátum}.</p> */
	public static long retazecNaDatum(String dátum)
	{ return reťazecNaDátum(dátum); }

	/**
	 * <p>Vráti dátum v milisekundách počítaných od začiatku takzvanej
	 * epochy (polnoc 1. januára 1970 greenwichského času — v našom časovom
	 * pásme to znamená jednu hodinu v noci) podľa zadaných
	 * reťazcov dátumu a formátu.</p>
	 * 
	 * <p>Formát musí mať tvar skupiny rezervovaných znakov oddelených
	 * ľubovoľnými nerezervovanými znakmi. Rezervované znaky formátu určia
	 * význam číselných údajov v dátume podľa tohto kľúča (záleží na
	 * veľkosti písmen): D - deň, M - mesiac, R alebo Y - rok, h - hodina,
	 * m - minúta, s - sekunda.</p>
	 * 
	 * <p>Podľa formátu je rozpoznaný dátum, pričom chýbajúcim údajom
	 * sú ponechané predvolené hodnoty (pri začatí rozpoznávania sú
	 * hodnoty údajov dátumu nastavené na začiatok epochy). V dátume (nie
	 * vo formáte) je odlišne spracovaný jeden rezervovaný znak – mriežka
	 * {@code srg'#'}. Tento znak bude považovaný za jeden úplný prázdny
	 * číselný údaj, to znamená, že reťazec {@code srg"###"} bude znamenať
	 * tri prázdne údaje a pri výskyte tohto znaku bude prislúchajúcemu
	 * údaju (podľa poradia určeného formátom) ponechaná jeho predvolená
	 * hodnota.</p>
	 * 
	 * @param dátum reťazec dátumu v predvolenom formáte
	 * @param formát očakávaný formát dátumu
	 * @return dátum v milisekundách epochy
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 */
	public static long reťazecNaDátum(String dátum, String formát)
	{
		Calendar kalendár = Calendar.getInstance();
		kalendár.setTimeInMillis(0);

		if (null == formát || formát.isEmpty())
			return kalendár.getTimeInMillis();

		Vector<Character> znaky = new Vector<>();

		char posledný = formát.charAt(0), znak = 0;
		for (int i = 1; i < formát.length(); ++i)
		{
			znak = formát.charAt(i);
			if (znak != posledný)
			{
				switch (posledný)
				{
				case 'R': case 'Y': znaky.add('Y'); break;
				case 'D': case 'M': case 'h': case 'm': case 's':
					znaky.add(posledný); break;
				}
			}
			posledný = znak;
		}
		switch (posledný)
		{
		case 'R': case 'Y': znaky.add('Y'); break;
		case 'D': case 'M': case 'h': case 'm': case 's':
			znaky.add(posledný); break;
		}

		int údaj = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < dátum.length(); ++i)
		{
			char ch = dátum.charAt(i);

			if ('#' == ch) ++údaj;
			else if ('0' <= ch && '9' >= ch) sb.append(ch);
			else
			{
				if (0 != sb.length())
				{
					try
					{
						int číslo = Integer.parseInt(sb.toString());

						if (údaj < znaky.size())
							switch (znaky.get(údaj))
							{
							case 'D': kalendár.set(DAY_OF_MONTH, číslo); break;
							case 'M': kalendár.set(MONTH, číslo - 1); break;
							case 'Y': kalendár.set(YEAR, číslo); break;
							case 'h': kalendár.set(HOUR_OF_DAY, číslo); break;
							case 'm': kalendár.set(MINUTE, číslo); break;
							case 's': kalendár.set(SECOND, číslo); break;
							}
						else
							GRobotException.vypíšChybovéHlásenie(
								"Príliš veľa (" + (údaj + 1) +
								") číselných údajov v dátume – na znaku " +
								(i + 1) + " (" + ch + ") v reťazci „" +
								dátum + "“ rozpoznávaných podľa formátu „" +
								formát + "“."/*, false*/);
					}
					catch (Exception e)
					{
						GRobotException.vypíšChybovéHlásenie(
							"Chyba pri nastavovaní údaju dátumu…"/*, false*/);
						GRobotException.vypíšChybovéHlásenia(e/*, false*/);
					}

					sb.setLength(0);
					++údaj;
				}

				/*if (' ' == ch || ' ' == ch || '.' == ch ||
					',' == ch || ':' == ch || '\t' == ch ||
					'\n' == ch || '\r' == ch || '\f' == ch)
					GRobotException.vypíšChybovéHlásenie(
						"Neplatný znak pri zisťovaní dátumu: " +
						ch/*, false* /);*/
			}
		}

		if (0 != sb.length())
		{
			try
			{
				int číslo = Integer.parseInt(sb.toString());

				if (údaj < znaky.size())
					switch (znaky.get(údaj))
					{
					case 'D': kalendár.set(DAY_OF_MONTH, číslo); break;
					case 'M': kalendár.set(MONTH, číslo - 1); break;
					case 'Y': kalendár.set(YEAR, číslo); break;
					case 'h': kalendár.set(HOUR_OF_DAY, číslo); break;
					case 'm': kalendár.set(MINUTE, číslo); break;
					case 's': kalendár.set(SECOND, číslo); break;
					}
				else
					GRobotException.vypíšChybovéHlásenie(
						"Príliš veľa (" + (údaj + 1) +
						") číselných údajov v dátume – na poslednom " +
						"znaku v reťazci „" + dátum + "“ rozpoznávaných " +
						"podľa formátu „" + formát + "“."/*, false*/);
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Chyba pri nastavovaní údaju dátumu…"/*, false*/);
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}
		}

		return kalendár.getTimeInMillis();
	}

	/** <p><a class="alias"></a> Alias pre {@link #reťazecNaDátum(String, String) reťazecNaDátum}.</p> */
	public static long retazecNaDatum(String dátum, String formát)
	{ return reťazecNaDátum(dátum, formát); }


	// Upraví názov položky podľa konfigurácie tohto archívu.
	/*packagePrivate*/ String upravNázovPoložky(String názovPoložky)
	{
		if (null == názovPoložky)
			throw new GRobotException(
				"Názov položky nesmie byť zamlčaný.",
				"entryNameOmitted", null, new NullPointerException());

		if (-1 != názovPoložky.indexOf('\\'))
			názovPoložky = názovPoložky.replace('\\', '/');

		if (null != cestaVArchíve &&
			!názovPoložky.startsWith(cestaVArchíve))
			názovPoložky = cestaVArchíve + názovPoložky;

		return názovPoložky;
	}


	/**
	 * <p>Vráti dátum položky v reťazcovej podobe. Táto metóda používa na
	 * prevod metódu {@link #dátumNaReťazec(long) dátumNaReťazec}, jej výstup
	 * je preto rovnaký.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť vrátený
	 * @return návratová hodnota metódy {@link #dátumNaReťazec(long)
	 *     dátumNaReťazec} (s argumentom číselnej podoby dátumu položky
	 *     vrátenej metódou {@link #dátumPoložkyAkoČíslo(String)
	 *     dátumPoložkyAkoČíslo})
	 * 
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 */
	public String dátumPoložky(String názovPoložky)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);
		return dátumNaReťazec(dátumPoložkyAkoČíslo(názovPoložky));
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumPoložky(String, String) dátumPoložky}.</p> */
	public String datumPolozky(String názovPoložky)
	{ return dátumPoložky(názovPoložky); }

	/* *
	 * <p>NEDÁ SA – metóda koliduje so setterom dátumu položky…</p>
	 * 
	 * @param názovPoložky —
	 * @param formátDátumu —
	 * @return —
	 * 
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 * /
	public String dátumPoložky(String názovPoložky, String formátDátumu)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);
		return dátumNaReťazec(dátumPoložkyAkoČíslo(
			názovPoložky), formátDátumu);
	}*/

	/**
	 * <p>Nastaví zadanej položke nový časový údaj jej vytvorenia. Údaj
	 * je očakávaný v tvare reťazca v presne stanovenom formáte – v tvare
	 * skupiny celých čísiel oddelených množinami znakov platných
	 * oddeľovačov (pre tento formát). Celočíselné údaje musia nasledovať
	 * v stanovenom poradí: deň, mesiac, rok, hodina, minúta, sekunda.
	 * Chýbajúcim údajom sú ponechané predvolené hodnoty (pri začatí
	 * rozpoznávania dátumu je jeho hodnota nastavená na začiatok takzvanej
	 * epochy – polnoc 1. januára 1970 greenwichského času – v našom
	 * časovom pásme to znamená jednu hodinu v noci).</p>
	 * 
	 * <p>Platnými znakmi oddeľovačov sú: medzera ({@code srg' '}), pevná
	 * medzera ({@code srg' '}), bodka ({@code srg'.'}), čiarka ({@code 
	 * srg','}), dvojbodka ({@code srg':'}), tabulátor ({@code srg'\t'}),
	 * nový riadok ({@code srg'\n'}), návrat vozíka ({@code srg'\r'})
	 * a posun formulára ({@code srg'\f'}). Za jeden oddeľovač je považovaná
	 * ľubovoľne veľká skupina zložená z uvedených znakov. Znak mriežky
	 * ({@code srg'#'}) je považovaný za jeden úplný prázdny číselný údaj,
	 * to znamená, že reťazec {@code srg"###"} znamená prázdny deň, mesiac
	 * a rok (hoci medzi znakmi nie sú oddeľovače). Pri výskyte tohto znaku
	 * je prislúchajúcemu údaju ponechaná predvolená hodnota. Ostatné znaky
	 * (samozrejme okrem číslic) sú považované za neplatné a sú ignorované
	 * (opomenúc fakt, že sa zapisujú do {@linkplain GRobotException#denník
	 * denníka chýb}).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ak je archív otvorený len na
	 * čítanie, tak bude dátum zmenený len pre položku uloženú v pamäti.
	 * Položka v archíve uloženom na pevnom disku sa nezmení.</p>
	 * 
	 * <p>Príklady platných dátumov: TODO</p>
	 * 
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť nastavený
	 * @param dátumPoložky reťazcový tvar dátumu položky
	 * @return {@link valtrue} v prípade úspechu akcie
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 */
	public void dátumPoložky(String názovPoložky, String dátumPoložky)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);
		long dátum = reťazecNaDátum(dátumPoložky);
		dátumPoložkyAkoČíslo(názovPoložky, dátum);
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumPoložky(String, String) dátumPoložky}.</p> */
	public void datumPolozky(String názovPoložky, String dátumPoložky)
	{ dátumPoložky(názovPoložky, dátumPoložky); }

	/**
	 * <p>Vráti dátum položky v číselnej podobe. Číselný údaj vyjadruje
	 * počet milisekúnd od začiatku takzvanej epochy – polnoc 1. januára
	 * 1970 greenwichského času – v našom časovom pásme to znamená jednu
	 * hodinu po polnoci.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť vrátený
	 * @return dátum položky v číselnej podobe
	 * 
	 * @throws GRobotException ak archív nie je otvorený (na čítanie alebo
	 *     zápis) alebo zadaná položka nebola nájdená
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String, long)
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 */
	public long dátumPoložkyAkoČíslo(String názovPoložky)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			return položka.getTime();
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			return položka.getTime();
		}

		// TODO TEST

		throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumPoložkyAkoČíslo(String) dátumPoložkyAkoČíslo}.</p> */
	public long datumPolozkyAkoCislo(String názovPoložky)
	{ return dátumPoložkyAkoČíslo(názovPoložky); }

	/**
	 * <p>Nastaví zadanej položke nový časový údaj jej vytvorenia podľa
	 * zadaného číselného údaja. Údaj vyjadruje počet milisekúnd od začiatku
	 * takzvanej epochy – polnoc 1. januára 1970 greenwichského času –
	 * v našom časovom pásme to znamená jednu hodinu po polnoci.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ak je archív otvorený len na
	 * čítanie, tak bude dátum zmenený len pre položku uloženú v pamäti.
	 * Položka v archíve uloženom na pevnom disku sa nezmení.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť nastavený
	 * @param dátumPoložky číselný tvar dátumu položky (počet milisekúnd
	 *     od začiatku epochy)
	 * 
	 * @throws GRobotException ak archív nie je otvorený (na čítanie alebo
	 *     zápis) alebo zadaná položka nebola nájdená
	 * 
	 * @see #dátumPoložky(String)
	 * @see #dátumPoložky(String, String)
	 * @see #dátumPoložkyAkoČíslo(String)
	 * 
	 * @see #dátumNaReťazec(long)
	 * @see #dátumNaReťazec(long, String)
	 * @see #reťazecNaDátum(String)
	 * @see #reťazecNaDátum(String, String)
	 */
	public void dátumPoložkyAkoČíslo(String názovPoložky, long dátum)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			položka.setTime(dátum);
			return;
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			položka.setTime(dátum);
			return;
		}

		// TODO TEST

		throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");
	}

	/** <p><a class="alias"></a> Alias pre {@link #dátumPoložkyAkoČíslo(String, long) dátumPoložkyAkoČíslo}.</p> */
	public void datumPolozkyAkoCislo(String názovPoložky, long dátum)
	{ dátumPoložkyAkoČíslo(názovPoložky, dátum); }


	/**
	 * <p>Vráti aktuálnu (nekomprimovanú) veľkosť položky (identifikovanej
	 * názvom).</p>
	 * 
	 * @param názovPoložky názov položky, ktorej veľkosť má byť vrátená
	 * @return aktuálna veľkosť požadovanej položky
	 * 
	 * @see #komprimovanáVeľkosťPoložky(String)
	 */
	public long veľkosťPoložky(String názovPoložky)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				/*throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);*/
				return -1;
			return položka.getSize();
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				/*throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);*/
				return -1;
			return položka.getSize();
		}

		// TODO TEST

		/*throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");*/
		return -1;
	}

	/** <p><a class="alias"></a> Alias pre {@link #veľkosťPoložky(String) veľkosťPoložky}.</p> */
	public long velkostPolozky(String názovPoložky)
	{ return veľkosťPoložky(názovPoložky); }

	/**
	 * <p>Vráti komprimovanú veľkosť zadanej položky.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej komprimovaná veľkosť
	 *     má byť vrátená
	 * @return komprimovaná veľkosť požadovanej položky
	 * 
	 * @see #veľkosťPoložky(String)
	 */
	public long komprimovanáVeľkosťPoložky(String názovPoložky)
	{
		názovPoložky = upravNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				/*throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);*/
				return -1;
			return položka.getCompressedSize();
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				/*throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);*/
				return -1;
			return položka.getCompressedSize();
		}

		// TODO

		/*throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");*/
		return -1;
	}

	/** <p><a class="alias"></a> Alias pre {@link #komprimovanáVeľkosťPoložky(String) komprimovanáVeľkosťPoložky}.</p> */
	public long komprimovanaVelkostPolozky(String názovPoložky)
	{ return komprimovanáVeľkosťPoložky(názovPoložky); }


	/*
	TODO
	public ZipEntry uložPoložku(String názovPoložky, String názovSúboru)
	{
		sekvencia
		return spracovaná (uložená?/prečítaná?) položka
	}
	*/

	/*
	TODO
	public int rozbaľArchív(String názovPodpriečinka)
	{
		sekvencia
		return počet úspešne rozbalených položiek
	}
	*/

	/**
	 * <p>TODO</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie}</b>.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej údaje majú byť prečítané
	 *     (pozri aj {@link #zoznamPoložiek() zoznamPoložiek})
	 * @return údaje položky vo forme poľa bajtov
	 * 
	 * @throws GRobotException ak archív nie je otvorený na čítanie alebo
	 *     zadaná položka nebola nájdená
	 */
	public byte[] údajePoložky(String názovPoložky)
	{
		if (null == vstup)
			throw new GRobotException(
				"Archív nie je otvorený na čítanie.",
				"archiveNotOpenForReading");

		// TODO TEST

		názovPoložky = upravNázovPoložky(názovPoložky);
		ZipEntry položka = vstup.getEntry(názovPoložky);

		if (null == položka)
			throw new GRobotException(
				"Položka „" + názovPoložky + "“ nebola nájdená.",
				"entryNotFound", názovPoložky);

		try
		{
			InputStream čítanie = vstup.getInputStream(položka);
			ByteArrayOutputStream prečítané = new ByteArrayOutputStream();

			byte[] údaje = new byte[32768];
			int počet;

			while ((počet = čítanie.read(údaje)) > 0)
				prečítané.write(údaje, 0, počet);

			byte[] bajty = prečítané.toByteArray();
			try { čítanie.close(); } catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); }
			prečítané.close();

			return bajty;
		}
		catch (IOException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #údajePoložky(String) údajePoložky}.</p> */
	public byte[] udajePolozky(String názovPoložky)
	{ return údajePoložky(názovPoložky); }


	/**
	 * <p>TODO</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis}</b>.</p>
	 * 
	 * @param názovPoložky názov pridávanej položky (smie byť {@code valnull};
	 *     v tom prípade bude použitý názov súboru)
	 * @param názovSúboru názov súboru, z ktorého majú byť prečítané
	 *     údaje pridávanej položky
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> {@link ZipEntry ZipEntry} na
	 *     vykonanie prípadných ďalších úprav (nastavenie komentára
	 *     položky, úprava dátumu podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak archív nie je otvorený na zápis, ak
	 *     bola namiesto názvu súboru zadaná hodnota {@code valnull} alebo
	 *     v prípade pokusu o vloženie duplicitnej položky
	 * 
	 * @see #pridajPoložku(String, byte[])
	 * @see #cestaVArchíve()
	 * @see #cestaNaDisku()
	 */
	public ZipEntry pridajPoložku(String názovPoložky, String názovSúboru)
		throws IOException
	{
		if (null == výstup)
			throw new GRobotException(
				"Archív nie je otvorený na zápis.",
				"archiveNotOpenForWriting");

		if (null == názovSúboru)
			throw new GRobotException(
				"Názov súboru nesmie byť zamlčaný.",
				"fileNameOmitted", null, new NullPointerException());

		if (null == názovPoložky)
		{
			názovPoložky = názovSúboru;
			if (-1 != názovPoložky.indexOf('\\'))
				názovPoložky = názovPoložky.replace('\\', '/');

			int indexOf = názovPoložky.lastIndexOf('/');
			if (-1 != indexOf) názovPoložky = názovPoložky.
				substring(indexOf + 1); // TODO TEST
		}
		else if (-1 != názovPoložky.indexOf('\\'))
			názovPoložky = názovPoložky.replace('\\', '/');

		if (null != cestaVArchíve &&
			!názovPoložky.startsWith(cestaVArchíve))
			názovPoložky = cestaVArchíve + názovPoložky;

		if (položkyVýstupu.containsKey(názovPoložky))
			throw new GRobotException("Položka „" +
				názovPoložky + "“ už v archíve jestvuje.",
				"duplicateEntry", názovPoložky);

		File súbor = null == cestaNaDisku ? new File(názovSúboru) :
			new File(cestaNaDisku, názovSúboru);

		FileInputStream čítanie = new FileInputStream(súbor);
		ZipEntry položka = new ZipEntry(názovPoložky);

		položkyVýstupu.put(názovPoložky, položka);
		výstup.putNextEntry(položka);

		byte[] údaje = new byte[32768];
		int počet;

		while ((počet = čítanie.read(údaje)) > 0)
			výstup.write(údaje, 0, počet);

		položka.setTime(súbor.lastModified());
		čítanie.close();
		return položka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String, String) pridajPoložku}.</p> */
	public ZipEntry pridajPolozku(String názovPoložky, String názovSúboru)
		throws IOException { return pridajPoložku(názovPoložky, názovSúboru); }

	/**
	 * <p>TODO</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis}</b>.</p>
	 * 
	 * @param názovPoložky názov pridávanej položky (nesmie byť zamlčaný)
	 * @param údajePoložky údaje pridávanej položky (vo forme poľa bajtov)
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> {@link ZipEntry ZipEntry} na
	 *     vykonanie prípadných ďalších úprav (nastavenie komentára
	 *     položky, úprava dátumu podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak archív nie je otvorený na zápis, ak
	 *     bol názov položky zamlčaný (bola zadaná hodnota {@code valnull})
	 *     alebo v prípade pokusu o vloženie duplicitnej položky
	 * 
	 * @see #pridajPoložku(String, String)
	 * @see #cestaVArchíve()
	 * @see #cestaNaDisku()
	 */
	public ZipEntry pridajPoložku(String názovPoložky, byte[] údajePoložky)
		throws IOException
	{
		if (null == výstup)
			throw new GRobotException(
				"Archív nie je otvorený na zápis.",
				"archiveNotOpenForWriting");


		názovPoložky = upravNázovPoložky(názovPoložky);

		if (položkyVýstupu.containsKey(názovPoložky))
			throw new GRobotException("Položka „" +
				názovPoložky + "“ už v archíve jestvuje.",
				"duplicateEntry", názovPoložky);

		ZipEntry položka = new ZipEntry(názovPoložky);
		položkyVýstupu.put(názovPoložky, položka);
		výstup.putNextEntry(položka);

		výstup.write(údajePoložky, 0, údajePoložky.length);
		// položka.setTime(súbor.lastModified());
		return položka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String, byte[]) pridajPoložku}.</p> */
	public ZipEntry pridajPolozku(String názovPoložky, byte[] údajePoložky)
		throws IOException { return pridajPoložku(názovPoložky, údajePoložky); }


	/* *
	 * <p>NEDÁ SA – použité súčasti nepodporujú čítanie komentárov „súboru“
	 * (to jest celého archívu). Jestvuje len podpora práce s komentármi
	 * položiek archívu. Je nelogické poskytnúť len možnosť zápisu komentára
	 * „súboru“ (to jest celého archívu).</p>
	 * 
	 * @return —
	 * 
	 * @see #komentár(String)
	 * /
	public String komentár()
	{
		if (null != vstup)
		{
			// return vstup.getComment();
		}

		if (null != výstup)
		{
			// return výstup.getComment();
		}

		return null;
	}/* */

	/* *
	 * <p>NEDÁ SA – použité súčasti nepodporujú čítanie komentárov „súboru“
	 * (to jest celého archívu). Jestvuje len podpora práce s komentármi
	 * položiek archívu. Je nelogické poskytnúť len možnosť zápisu komentára
	 * „súboru“ (to jest celého archívu).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis}</b>.</p>
	 * 
	 * @param komentár —
	 * @return {@link valtrue} v prípade úspechu akcie
	 * 
	 * @throws GRobotException ak archív nie je otvorený na zápis
	 * 
	 * @see #komentár()
	 * /
	public void komentár(String komentár)
	{
		if (null == výstup)
			throw new GRobotException(
				"Archív nie je otvorený na zápis.",
				"archiveNotOpenForWriting");
	}/* */
}
