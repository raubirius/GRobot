
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

package knižnica;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Calendar;
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
 * <p>Táto trieda slúži na prácu s údajovými archívmi vo formáte ZIP.
 * Vnútorne používa mierne prispôsobený balíček <a
 * href="https://ant.apache.org/manual/api/org/apache/tools/zip/package-summary.html"
 * target="_blank"><code>org.apache.tools.zip</code></a> (ktorý je
 * zameraný práve na prácu so ZIP archívmi) projektu <a
 * href="https://ant.apache.org/" target="_blank">Apache Ant</a>. Balíček
 * je uvoľnený pod podmienkami licencie <a target="_blank"
 * href="resources/apache-licence-2.0.html">Apache License – Version 2.0,
 * January 2004.</a></p>
 * 
 * <p>Trieda {@code currArchív} poskytuje programátorské rozhranie na čo
 * najjednoduchšie vytvorenie archívu jeho zápis, čítanie alebo analýzu.
 * Programovací rámec umožňuje prepojenie iných súčastí (napríklad
 * {@linkplain Súbor súboru}) s archívom a tým ešte väčšmi zjednodušiť
 * prácu s ním. (Pozri príklad na konci tohto opisu.)</p>
 * 
 * <p>Trieda je navrhnutá tak, aby bolo jej používanie čo najintuitívnejšie.
 * Napríklad, ak máme jestvujúci archív, rozbaliť ho (aj s výpisom počtu
 * rozbalených položiek) môžeme jednoducho takto:</p>
 * 
 * <pre CLASS="example">
	{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Počet rozbalených položiek: "},
		{@code kwdnew} {@link Archív#Archív(String) Archív}({@code srg"archív_na_rozbalenie.zip"}).
			{@link Archív#rozbaľArchív(String) rozbaľArchív}({@code srg"cieľový_priečinok"}));
	</pre>
 * 
 * <p>Naopak, vytvoriť jednoduchý archív môžeme takto:</p>
 * 
 * <pre CLASS="example">
	{@code currArchív} archív = {@code kwdnew} {@link Archív#Archív(String) Archív}({@code srg"archív_na_zbalenie.zip"});
	archív.{@link Archív#otvorNaZápis() otvorNaZápis}();
	archív.{@link Archív#pridajPoložku(String, String) pridajPoložku}({@code srg"názov_položky"}, {@code srg"cesta/názov_súboru_na_zbalenie"});
	{@code comm// archív.pridajPoložku(…}
	archív.{@link Archív#zavri() zavri}();
	</pre>
 * 
 * <p>Na vytvorenie archívu, ktorý (bez filtrovania) zbalí celý obsah
 * priečinka na disku môžeme využiť nasledujúce definície:</p>
 * 
 * <pre CLASS="example">
	{@code comm// Inštancia archívu.}
	{@code kwdprivate} {@code currArchív} archív = {@code kwdnew} {@link Archív#Archív() Archív}();

	{@code comm// Rekurzívna metóda pridávajúca do archívu obsah priečinka}
	{@code comm// a podpriečinkov.}
	{@code kwdprivate} {@code typevoid} pridajObsahPriečinka({@link String String} priečinok, {@link String String} podpriečinok)
		{@code kwdthrows} {@link IOException IOException}
	{
		{@link String String}[] zoznam = {@link Súbor Súbor}.{@link Súbor#zoznam(String) zoznam}(priečinok + podpriečinok);
		{@code kwdfor} ({@link String String} položka : zoznam)
		{
			{@link String String} položkaArchívu = podpriečinok + položka;
			{@code kwdif} ({@link Súbor Súbor}.{@link Súbor#jePriečinok(String) jePriečinok}(priečinok + položkaArchívu))
			{
				{@code comm// Rekurzívne pridávanie obsahu (pod)priečinka:}
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg'«'}, položkaArchívu, {@code srg'»'});
				archív.{@link Archív#pridajPriečinok(String) pridajPriečinok}(položkaArchívu);
				pridajObsahPriečinka(priečinok, položkaArchívu + {@code srg'/'});
			}
			{@code kwdelse}
			{
				{@code comm// Pridanie položky súboru:}
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(položkaArchívu);
				archív.{@link Archív#pridajPoložku(String, String) pridajPoložku}(položkaArchívu,
					priečinok + položkaArchívu);
			}
		}
	}

	{@code comm// Metóda slúžiaca na spustenie procesu balenia.}
	{@code kwdpublic} {@code typevoid} zbaľPriečinok({@link String String} názovArchívu, {@link String String} priečinok)
		{@code kwdthrows} {@link IOException IOException}
	{
		priečinok = priečinok.{@link String#replace(char, char) replace}({@code srg'\\'}, {@code srg'/'});
		{@code kwdif} (!priečinok.{@link String#endsWith(String) endsWith}({@code srg"/"})) priečinok += {@code srg'/'};

		archív.{@link Archív#otvorNaZápis(String) otvorNaZápis}(názovArchívu);
		pridajObsahPriečinka(priečinok, {@code srg""});
		archív.{@link Archív#zavri() zavri}();
	}
	</pre>
 * 
 * <p>Príkaz slúžiaci na zahájenie balenia s využitím týchto definícií
 * potom môže vyzerať takto:</p>
 * 
 * <pre CLASS="example">
	zbaľPriečinok({@code srg"názov_archívu.zip"}, {@code srg"priečinok"});
	</pre>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje ako pripojiť archív ku konfiguračnému súboru
 * (čím sa vytvorí zbalená konfigurácia) a naznačuje prácu s konfiguráciou
 * (ktorá je štandardná – rovnaká ako v prípade nepripojeného archívu –
 * pozri napríklad príklad v opise triedy {@link ObsluhaUdalostí
 * ObsluhaUdalostí} – v sekcii Ďalšie udalosti).</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} PripojenieArchívu {@code kwdextends} GRobot
	{
		{@code kwdprivate} PripojenieArchívu()
		{
			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}()
				{
					{@code comm// Overenie podmienok a ak treba zapísať konfiguráciu,}
					{@code comm// tak návrat: return true;}
					{@code kwdreturn} {@code valfalse};
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} java.io.{@link IOException IOException}
				{
					{@code comm// Zápis vlastných vlastností:}
					{@code comm// súbor.zapíšVlastnosť("názov", hodnota);}
					{@code comm// …}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} java.io.{@link IOException IOException}
				{
					{@code comm// Čítanie vlastných vlastností:}
					{@code comm// hodnota = súbor.čítajVlastnosť("názov", predvolenáHodnota);}
					{@code comm// …}
				}
			};
		}

		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code comm// Pripojenie archívu ku konfigurácii:}
			{@link Svet Svet}.{@link Svet#konfiguračnýSúbor() konfiguračnýSúbor}().{@link Súbor#pripojArchív(Archív) pripojArchív}(
				{@code kwdnew} {@link Archív#Archív(String) Archív}({@code srg"PripojenieArchívu.zip"}));

			{@code comm// Použitie konfigurácie:}
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"PripojenieArchívu.cfg"});

			{@code comm// Konštrukcia:}
			{@code kwdnew} PripojenieArchívu();
		}
	}
	</pre>
 * 
 * <p>Trieda je navrhnutá tak, aby sa dala používať aj bez nevyhnutnosti
 * obsluhy výnimiek.</p>
 */
public class Archív implements Closeable
{
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
	 * <p class="tip"><b>Tip:</b> Názov archívu môže byť od cesty oddelený
	 * pomocou konštruktora, ktorý prijíma cestu a názov archívu zvlášť
	 * {@link #Archív(String, String) Archív(cesta, názov)}, alebo s použitím
	 * metódy {@link #cestaNaDisku(String) cestaNaDisku} na dodatočné
	 * nastavenie cesty k archívu.</p>
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
	 * <p class="tip"><b>Tip:</b> Názov archívu môže byť od cesty oddelený
	 * pomocou použitia metódy {@link #cestaNaDisku(String) cestaNaDisku}
	 * alebo konštruktorom, ktorý prijíma cestu a názov archívu zvlášť:
	 * {@link #Archív(String, String) Archív(cesta, názov)}.</p>
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
	 * Súbor#pripojArchív(Archív) pripojeným archívom}. Automatické otvorenie
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
	 * {@code valnull} ruší nastavenie cesty. Cesta v archíve zameriava
	 * (presmerúva) všetky metódy pracujúce s položkami archívu do tohto
	 * vnútorného umiestnenia. Jej použitie treba zvážiť. V mnohých
	 * prípadoch môže byť výhodné (napríklad pri pridávaní viacerých
	 * položiek do rovnakého podpriečinka v archíve) a naopak, sú prípady,
	 * v ktorých by mohlo jej nastavenie prekážať (napríklad pri
	 * automatickom spracúvaní názvov položiek, ktoré už cestu v archíve
	 * obsahujú – na zamedzenie duplicít alebo nedorozumení).</p>
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
	 * @see #otvorNaČítanie(String, String)
	 * @see #otvorNaZápis(String, String)
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
	 * @see #otvorNaČítanie(String, String)
	 * @see #otvorNaZápis(String, String)
	 * @see #zavri()
	 */
	public void close() throws IOException { zavri(); }


	/**
	 * <p>Otvorí archív na čítanie. Archív musí mať vopred priradené meno
	 * (buď priamo {@linkplain #Archív(String) konštruktorom}, alebo
	 * metódou {@link #názov(String) názov}). Ak súbor s archívom
	 * nejestvuje, tak vznikne výnimka. Musí ísť o fyzický súbor na pevnom
	 * disku umiestnený na aktuálnej lokalite, ceste určenej v názve archívu
	 * alebo na ceste určenej vlastnosťou {@link #cestaNaDisku(String)
	 * cestaNaDisku}.</p>
	 * 
	 * @return „vstupný archív“ knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipFile.java"
	 *     target="_blank"><code>ZipFile</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára, úprava rôznych konfiguračných
	 *     položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #Archív(String)
	 * @see #názov(String)
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaČítanie(String, String)
	 * @see #otvorNaZápis()
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
	 * <p>Otvorí archív na zápis. Zapisovaný súbor bude vytvorený alebo
	 * prepísaný buď na aktuálnom umiestnení, alebo na ceste určenej
	 * vlastnosťou {@link #cestaNaDisku(String) cestaNaDisku} (prípadne
	 * inak, napríklad cestou určenou priamo v názve archívu).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak použijete objekt <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 * target="_blank"><code>ZipOutputStream</code></a>, ktorý je návratovou
	 * hodnotou tejto metódy na pridávanie nových položiek, tak táto trieda
	 * programovacieho rámca nebude schopná detegovať duplicitné položky, ani
	 * spätne pracovať s pridanými položkami, pretože ich evidenciu vykonáva
	 * vo vlastnej réžii nad rámec možností triedy <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 * target="_blank"><code>ZipOutputStream</code></a>.</p>
	 * 
	 * @return „výstupný archív“ (prúd) knižnice <a
	 *     href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 *     <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 *     target="_blank"><code>ZipOutputStream</code></a> na vykonanie
	 *     prípadných ďalších úprav (nastavenie komentára, úprava rôznych
	 *     konfiguračných položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #Archív(String)
	 * @see #názov(String)
	 * @see #otvorNaZápis(String)
	 * @see #otvorNaZápis(String, String)
	 * @see #otvorNaČítanie()
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
	 * disku umiestnený na aktuálnej lokalite, ceste určenej v názve archívu
	 * alebo na ceste určenej vlastnosťou {@link #cestaNaDisku(String)
	 * cestaNaDisku}.</p>
	 * 
	 * @param názov meno archívu
	 * @return „vstupný archív“ knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipFile.java"
	 *     target="_blank"><code>ZipFile</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára, úprava rôznych konfiguračných
	 *     položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaČítanie()
	 * @see #otvorNaČítanie(String, String)
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
	 * ceste určenej vlastnosťou {@link #cestaNaDisku(String) cestaNaDisku}
	 * (prípadne inak, napríklad cestou určenou priamo v názve archívu).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak použijete objekt <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 * target="_blank"><code>ZipOutputStream</code></a>, ktorý je návratovou
	 * hodnotou tejto metódy na pridávanie nových položiek, tak táto trieda
	 * programovacieho rámca nebude schopná detegovať duplicitné položky, ani
	 * spätne pracovať s pridanými položkami, pretože ich evidenciu vykonáva
	 * vo vlastnej réžii nad rámec možností triedy <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 * target="_blank"><code>ZipOutputStream</code></a>.</p>
	 * 
	 * @param názov meno archívu
	 * @return „výstupný archív“ (prúd) knižnice <a
	 *     href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 *     <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 *     target="_blank"><code>ZipOutputStream</code></a> na vykonanie
	 *     prípadných ďalších úprav (nastavenie komentára, úprava rôznych
	 *     konfiguračných položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaZápis()
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String, String)
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
	 * <p>Otvorí archív so zadaným menom na zadanej ceste na čítanie. Ďalšie
	 * informácie sú v opisoch metód {@link #otvorNaČítanie(String)
	 * otvorNaČítanie} a {@link #cestaNaDisku() cestaNaDisku}.</p>
	 * 
	 * @param cesta pracovná cesta (na pevnom disku)
	 * @param názov meno archívu
	 * @return „vstupný archív“ knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipFile.java"
	 *     target="_blank"><code>ZipFile</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára, úprava rôznych konfiguračných
	 *     položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaČítanie()
	 * @see #otvorNaČítanie(String)
	 * @see #otvorNaZápis(String, String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipFile otvorNaČítanie(String cesta, String názov)
		throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();
		cestaNaDisku(cesta);
		názov(názov);

		vstup = null == cestaNaDisku ? new ZipFile(new File(
			názov), "UTF-8") : new ZipFile(new File(cestaNaDisku,
				názov), "UTF-8");

		return vstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaČítanie(String, String) otvorNaČítanie}.</p> */
	public ZipFile otvorNaCitanie(String cesta, String názov)
	throws IOException { return otvorNaČítanie(cesta, názov); }


	/**
	 * <p>Otvorí archív so zadaným menom na zadanej ceste na zápis. Ďalšie
	 * informácie sú v opisoch metód {@link #otvorNaČítanie(String)
	 * otvorNaČítanie} a {@link #cestaNaDisku() cestaNaDisku}.</p>
	 * 
	 * @param cesta pracovná cesta (na pevnom disku)
	 * @param názov meno archívu
	 * @return „výstupný archív“ (prúd) knižnice <a
	 *     href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 *     <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipOutputStream.java"
	 *     target="_blank"><code>ZipOutputStream</code></a> na vykonanie
	 *     prípadných ďalších úprav (nastavenie komentára, úprava rôznych
	 *     konfiguračných položiek podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * 
	 * @see #otvorNaZápis()
	 * @see #otvorNaZápis(String)
	 * @see #otvorNaČítanie(String, String)
	 * @see #zavri()
	 * @see #close()
	 * @see #cestaNaDisku()
	 */
	public ZipOutputStream otvorNaZápis(String cesta, String názov)
		throws IOException
	{
		if (null == názov)
			throw new GRobotException(
				"Názov archívu nesmie byť zamlčaný.",
				"archiveNameOmitted", null, new NullPointerException());

		zavri();
		cestaNaDisku(cesta);
		názov(názov);

		výstup = null == cestaNaDisku ? new ZipOutputStream(
			new File(názov)) : new ZipOutputStream(new File(
				cestaNaDisku, názov));

		výstup.setEncoding("UTF-8");
		položkyVýstupu.clear();
		return výstup;
	}

	/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis(String, String) otvorNaZápis}.</p> */
	public ZipOutputStream otvorNaZapis(String cesta, String názov)
	throws IOException { return otvorNaZápis(cesta, názov); }


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
			int počet = 0; while (položky.hasMoreElements())
			{
				++počet;
				položky.nextElement();
			}
			return počet;
		}

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
	 * pričom hodiny, minúty a sekundy sú zľava zarovnané nulou
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
								dátum + ".“"/*, false*/);
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
						"znaku v reťazci „" + dátum + ".“"/*, false*/);
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
								formát + ".“"/*, false*/);
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
						"podľa formátu „" + formát + ".“"/*, false*/);
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


	// Overí a skoriguje názov položky podľa konfigurácie tohto archívu.
	/*packagePrivate*/ String overAKorigujNázovPoložky(String názovPoložky)
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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
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
	 * je prislúchajúcemu údaju ponechaná predvolená hodnota (podľa začiatku
	 * epochy spomenutého vyššie). Ostatné znaky (samozrejme okrem číslic) sú
	 * považované za neplatné a sú ignorované (opomenúc fakt, že sa zapisujú
	 * do {@linkplain GRobotException#denník denníka chýb}).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ak je archív otvorený len na
	 * čítanie, tak bude dátum zmenený len pre položku uloženú v pamäti.
	 * Položka v archíve uloženom na pevnom disku sa nezmení.</p>
	 * 
	 * <p>Príklady platných dátumov:</p>
	 * 
	 * <p><code>1 8 2001</code>: 1. 8. 2001, 01:00:00; <code>8.1.1999</code>:
	 * 8. 1. 1999, 01:00:00; <code>11.12.#,13:0:5</code>: 11. 12. 1970,
	 * 13:00:05; <code>11.#.1971,13:#</code>: 11. 1. 1971, 13:00:00.
	 * Staršie dátumy však môžu v niektorých softvéroch spôsobiť chybné
	 * zobrazenie.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Položka so zadaným názvom musí
	 * byť v archíve definovaná. Najmä pri archívoch určených na zápis to
	 * znamená, že najskôr musíme položku vytvoriť, až potom môžeme meniť
	 * jej parametre.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť nastavený
	 * @param dátumPoložky reťazcový tvar dátumu položky
	 * @return {@code valtrue} v prípade úspechu akcie
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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

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
	 * <p class="remark"><b>Poznámka:</b> Položka so zadaným názvom musí
	 * byť v archíve definovaná. Najmä pri archívoch určených na zápis to
	 * znamená, že najskôr musíme položku vytvoriť, až potom môžeme meniť
	 * jej parametre.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej dátum má byť nastavený
	 * @param dátum číselný tvar dátumu položky (počet milisekúnd
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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

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
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

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

		/*throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");*/
		return -1;
	}

	/** <p><a class="alias"></a> Alias pre {@link #komprimovanáVeľkosťPoložky(String) komprimovanáVeľkosťPoložky}.</p> */
	public long komprimovanaVelkostPolozky(String názovPoložky)
	{ return komprimovanáVeľkosťPoložky(názovPoložky); }


	/**
	 * <p>Vráti komentár zadanej položky.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej komentár má byť vrátený
	 * @return komentár položky
	 * 
	 * @throws GRobotException ak archív nie je otvorený (na čítanie alebo
	 *     zápis) alebo zadaná položka nebola nájdená
	 * 
	 * @see #komentárPoložky(String, String)
	 */
	public String komentárPoložky(String názovPoložky)
	{
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			return položka.getComment();
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			return položka.getComment();
		}

		throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");
	}

	/** <p><a class="alias"></a> Alias pre {@link #komentárPoložky(String) komentárPoložky}.</p> */
	public String komentarPolozky(String názovPoložky)
	{ return komentárPoložky(názovPoložky); }

	/**
	 * <p>Nastaví zadanej položke nový komentár.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ak je archív otvorený len na
	 * čítanie, tak bude komentár zmenený len pre položku uloženú v pamäti.
	 * Položka v archíve uloženom na pevnom disku sa nezmení.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Položka so zadaným názvom musí
	 * byť v archíve definovaná. Najmä pri archívoch určených na zápis to
	 * znamená, že najskôr musíme položku vytvoriť, až potom môžeme meniť
	 * jej parametre.</p>
	 * 
	 * @param názovPoložky názov položky, ktorej komentár má byť nastavený
	 * @param komentár nový komentár položky
	 * 
	 * @throws GRobotException ak archív nie je otvorený (na čítanie alebo
	 *     zápis) alebo zadaná položka nebola nájdená
	 * 
	 * @see #komentárPoložky(String)
	 */
	public void komentárPoložky(String názovPoložky, String komentár)
	{
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

		if (null != vstup)
		{
			ZipEntry položka = vstup.getEntry(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			položka.setComment(komentár);
			return;
		}

		if (null != výstup)
		{
			ZipEntry položka = položkyVýstupu.get(názovPoložky);
			if (null == položka)
				throw new GRobotException(
					"Položka „" + názovPoložky + "“ nebola nájdená.",
					"entryNotFound", názovPoložky);
			položka.setComment(komentár);
			return;
		}

		throw new GRobotException(
			"Archív nie je otvorený.",
			"archiveNotOpen");
	}

	/** <p><a class="alias"></a> Alias pre {@link #komentárPoložky(String, String) komentárPoložky}.</p> */
	public void komentarPolozky(String názovPoložky, String komentár)
	{ komentárPoložky(názovPoložky, komentár); }


	/**
	 * <p>Vráti jestvujúcu položku archívu knižnice <a
	 * href="https://ant.apache.org/" target="_blank">Apache Ant</a>
	 * <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 * target="_blank"><code>ZipEntry</code></a> na prácu s ňou. Ak súbor nie
	 * je otvorený na čítanie alebo zápis, alebo ak položka v archíve
	 * nejestvuje, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @param názovPoložky názov položky, ktorá má byť vrátená
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a>
	 */
	public ZipEntry dajPoložku(String názovPoložky)
	{
		if (null == názovPoložky) return null;
		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
		if (null != vstup) return vstup.getEntry(názovPoložky);
		if (null != výstup) return položkyVýstupu.get(názovPoložky);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajPoložku(String) dajPoložku}.</p> */
	public ZipEntry dajPolozku(String názovPoložky)
	{ return dajPoložku(názovPoložky); }


	/**
	 * <p>Uloží položku so zadaným názvom z archívu do súboru so zadaným
	 * názvom, pričom namiesto názvu súboru môže byť zadaná hodnota
	 * {@code valnull}. V takom prípade bude ako názov súboru použitý názov
	 * položky.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V špeciálom prípade, keď položka
	 * archívu reprezentuje priečinok, sú parametre {@code prepísať}
	 * a {@code vytvoriťCestu} ignorované a metóda sa správa tak, že
	 * automaticky priečinok vytvorí, ak nejestvuje.</p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @param názovSúboru názov súboru na disku (môže byť {@code valnull})
	 * @param prepísať príznak povolenia prepísania jestvujúceho súboru na
	 *     disku ({@code valtrue} – cieľový súbor smie byť prepísaný, ak
	 *     jestvuje)
	 * @param vytvoriťCestu ak je {@code true}, tak metóda automaticky vytvorí
	 *     cestu k cieľovému súboru, ak nejestvuje
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje a nie je povolená možnosť jeho prepísania (prípadne
	 *     ak na disku jestvuje položka s rovnakým názvom, ktorá nie je
	 *     súborom a preto nemôže byť prepísaná)
	 * @throws java.io.FileNotFoundException ak cieľová cesta nejestvuje
	 *     a nebolo povolené jej automatické vytvorenie
	 * 
	 * @see #uložPoložku(String, String, boolean)
	 * @see #uložPoložku(String, String)
	 * @see #uložPoložku(String, boolean, boolean)
	 * @see #uložPoložku(String, boolean)
	 * @see #uložPoložku(String)
	 */
	public ZipEntry uložPoložku(String názovPoložky, String názovSúboru,
		boolean prepísať, boolean vytvoriťCestu)
	{
		if (null == vstup)
			throw new GRobotException(
				"Archív nie je otvorený na čítanie.",
				"archiveNotOpenForReading");

		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
		ZipEntry položka = vstup.getEntry(názovPoložky);

		if (null == položka)
			throw new GRobotException(
				"Položka „" + názovPoložky + "“ nebola nájdená.",
				"entryNotFound", názovPoložky);

		if (null == názovSúboru) názovSúboru = názovPoložky;
			// už obsahuje cestu v archíve, ak je definovaná

		if (-1 != názovSúboru.indexOf('\\'))
			názovSúboru = názovSúboru.replace('\\', '/');

		// Treba vymazať cestu na disku zo začiatku, lebo tá je použitá nižšie.
		if (null != cestaNaDisku && názovSúboru.startsWith(cestaNaDisku))
			názovSúboru = názovSúboru.substring(cestaNaDisku.length());

		File súborCieľa = null == cestaNaDisku ? new File(názovSúboru) :
			new File(cestaNaDisku, názovSúboru);


		// Ak to je priečinok, tak sa proces končí tu (pričom v tomto
		// prípade sú parametre prepísať a vytvoriťCestu ignorované):
		if (názovPoložky.length() > 0 &&
			'/' == názovPoložky.charAt(názovPoložky.length() - 1)
			// názovPoložky.endsWith("/")
			)
		{
			if (!súborCieľa.exists())
			{
				if (!súborCieľa.mkdirs())
					GRobotException.vypíšChybovéHlásenie(
						"Nepodarilo sa vytvoriť cestu: " + názovSúboru);
			}
			return položka;
		}


		if (!prepísať && súborCieľa.exists())
			throw new GRobotException(
				"Cieľový súbor „" + názovSúboru + "“ už jestvuje.",
				"targetFileExists", názovSúboru);
		if (súborCieľa.exists() && !súborCieľa.isFile())
			throw new GRobotException(
				"Cieľový súbor „" + názovSúboru + "“ nie je súbor.",
				"targetObjectNotFile", názovSúboru);

		if (vytvoriťCestu)
		{
			String názovCesty = súborCieľa.getParent();
			if (null != názovCesty)
			{
				File súborCesty = new File(názovCesty);
				if (!súborCesty.exists())
				{
					if (!súborCesty.mkdirs())
						GRobotException.vypíšChybovéHlásenie(
							"Nepodarilo sa vytvoriť cestu: " + názovCesty);
				}
			}
		}

		try
		{
			InputStream čítanie = vstup.getInputStream(položka);
			FileOutputStream zápis = new FileOutputStream(súborCieľa);

			byte[] údaje = new byte[32768];
			int počet;

			while ((počet = čítanie.read(údaje)) > 0)
				zápis.write(údaje, 0, počet); // TODO sekvencia

			try { čítanie.close(); } catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e); }
			zápis.close();

			súborCieľa.setLastModified(položka.getTime());
			return položka;
		}
		catch (IOException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String, String, boolean, boolean) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky, String názovSúboru, boolean prepísať, boolean vytvoriťCestu)
	{ return uložPoložku(názovPoložky, názovSúboru, prepísať, vytvoriťCestu); }

	/**
	 * <p>Uloží položku so zadaným názvom z archívu do súboru so zadaným
	 * názvom, pričom namiesto názvu súboru môže byť zadaná hodnota
	 * {@code valnull}. V takom prípade bude ako názov súboru použitý názov
	 * položky. Metóda automaticky vytvorí cestu k cieľovému súboru (ak cesta
	 * nejestvuje). Ak chcete tomuto správaniu zabrániť, tak použite metódu
	 * {@link #uložPoložku(String, String, boolean, boolean)
	 * uložPoložku(názovPoložky, názovSúboru, prepísať, false)}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V špeciálom prípade, keď položka
	 * archívu reprezentuje priečinok, je parameter {@code prepísať}
	 * ignorovaný (pretože nemá význam uvažovať o jeho hodnote).</p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @param názovSúboru názov súboru na disku (môže byť {@code valnull})
	 * @param prepísať príznak povolenia prepísania jestvujúceho súboru na
	 *     disku ({@code valtrue} – cieľový súbor smie byť prepísaný, ak
	 *     jestvuje)
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje a nie je povolená možnosť jeho prepísania (prípadne
	 *     ak na disku jestvuje položka s rovnakým názvom, ktorá nie je
	 *     súborom a preto nemôže byť prepísaná)
	 * 
	 * @see #uložPoložku(String, String, boolean, boolean)
	 * @see #uložPoložku(String, String)
	 * @see #uložPoložku(String, boolean, boolean)
	 * @see #uložPoložku(String, boolean)
	 * @see #uložPoložku(String)
	 */
	public ZipEntry uložPoložku(String názovPoložky, String názovSúboru,
		boolean prepísať)
	{ return uložPoložku(názovPoložky, názovSúboru, prepísať, true); }

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String, String, boolean) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky, String názovSúboru, boolean prepísať)
	{ return uložPoložku(názovPoložky, názovSúboru, prepísať); }

	/**
	 * <p>Uloží položku so zadaným názvom z archívu do súboru so zadaným
	 * názvom, pričom namiesto názvu súboru môže byť zadaná hodnota
	 * {@code valnull}. V takom prípade bude ako názov súboru použitý názov
	 * položky. Cieľový súbor nesmie byť touto metódou prepísaný. Ak
	 * jestvuje, tak vznikne výnimka. (Ak chcete cieľový súbor prepísať,
	 * použite metódu {@link #uložPoložku(String, String, boolean)
	 * uložPoložku(názovPoložky, názovSúboru, true)}.) Metóda automaticky
	 * vytvorí cestu k cieľovému súboru (ak cesta nejestvuje). Ak chcete
	 * tomuto správaniu zabrániť, tak použite iný variant tejto metódy
	 * (napríklad {@link #uložPoložku(String, String, boolean, boolean)
	 * uložPoložku(názovPoložky, názovSúboru, prepísať, false)}).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @param názovSúboru názov súboru na disku (môže byť {@code valnull})
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje
	 * 
	 * @see #uložPoložku(String, String, boolean, boolean)
	 * @see #uložPoložku(String, String, boolean)
	 * @see #uložPoložku(String, boolean, boolean)
	 * @see #uložPoložku(String, boolean)
	 * @see #uložPoložku(String)
	 */
	public ZipEntry uložPoložku(String názovPoložky, String názovSúboru)
	{ return uložPoložku(názovPoložky, názovSúboru, false); }

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String, String) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky, String názovSúboru)
	{ return uložPoložku(názovPoložky, názovSúboru); }


	/**
	 * <p>Táto metóda funguje rovnako ako jej variant {@link 
	 * #uložPoložku(String, String, boolean, boolean) uložPoložku(názovPoložky,
	 * názovSúboru, prepísať, vytvoriťCestu)} so zadaným parametrom {@code 
	 * názovSúboru} rovným {@code valnull}. Podrobnejšie informácie sú v opise
	 * uvedenej metódy.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V špeciálom prípade, keď položka
	 * archívu reprezentuje priečinok, sú parametre {@code prepísať}
	 * a {@code vytvoriťCestu} ignorované a metóda sa správa tak, že
	 * automaticky priečinok vytvorí, ak nejestvuje.</p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @param prepísať príznak povolenia prepísania jestvujúceho súboru
	 * @param vytvoriťCestu ak je {@code true}, tak metóda automaticky vytvorí
	 *     cestu k cieľovému súboru, ak nejestvuje
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje a nie je povolená možnosť jeho prepísania (prípadne
	 *     ak na disku jestvuje položka s rovnakým názvom, ktorá nie je
	 *     súborom a preto nemôže byť prepísaná)
	 * @throws java.io.FileNotFoundException ak cieľová cesta nejestvuje
	 *     a nebolo povolené jej automatické vytvorenie
	 * 
	 * @see #uložPoložku(String, String, boolean, boolean)
	 * @see #uložPoložku(String, String, boolean)
	 * @see #uložPoložku(String, String)
	 * @see #uložPoložku(String, boolean)
	 * @see #uložPoložku(String)
	 */
	public ZipEntry uložPoložku(String názovPoložky,
		boolean prepísať, boolean vytvoriťCestu)
	{ return uložPoložku(názovPoložky, null, prepísať, vytvoriťCestu); }

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String, boolean, boolean) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky, boolean prepísať, boolean vytvoriťCestu)
	{ return uložPoložku(názovPoložky, null, prepísať, vytvoriťCestu); }

	/**
	 * <p>Táto metóda funguje rovnako ako jej variant {@link 
	 * #uložPoložku(String, String, boolean) uložPoložku(názovPoložky,
	 * názovSúboru, prepísať)} so zadaným parametrom {@code 
	 * názovSúboru} rovným {@code valnull}. Podrobnejšie informácie sú v opise
	 * uvedenej metódy.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V špeciálom prípade, keď položka
	 * archívu reprezentuje priečinok, je parameter {@code prepísať}
	 * ignorovaný (pretože nemá význam uvažovať o jeho hodnote).</p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @param prepísať príznak povolenia prepísania jestvujúceho súboru
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje a nie je povolená možnosť jeho prepísania (prípadne
	 *     ak na disku jestvuje položka s rovnakým názvom, ktorá nie je
	 *     súborom a preto nemôže byť prepísaná)
	 * 
	 * @see #uložPoložku(String, String, boolean, boolean)
	 * @see #uložPoložku(String, String, boolean)
	 * @see #uložPoložku(String, String)
	 * @see #uložPoložku(String, boolean, boolean)
	 * @see #uložPoložku(String)
	 */
	public ZipEntry uložPoložku(String názovPoložky, boolean prepísať)
	{ return uložPoložku(názovPoložky, null, prepísať, true); }

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String, boolean) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky, boolean prepísať)
	{ return uložPoložku(názovPoložky, null, prepísať); }

	/**
	 * <p>Táto metóda funguje rovnako ako jej variant {@link 
	 * #uložPoložku(String, String) uložPoložku(názovPoložky, názovSúboru)}
	 * so zadaným parametrom {@code názovSúboru} rovným {@code valnull}.
	 * Podrobnejšie informácie sú v opise uvedenej metódy.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * @param názovPoložky názov položky v archíve
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav
	 * @throws GRobotException ak archív nie je otvorený na čítanie; ak
	 *     zadaná položka nebola nájdená v archíve; ak cieľový súbor
	 *     jestvuje
	 * 
	 * @see #uložPoložku(String, String, boolean, boolean)
	 * @see #uložPoložku(String, String, boolean)
	 * @see #uložPoložku(String, String)
	 * @see #uložPoložku(String, boolean, boolean)
	 * @see #uložPoložku(String, boolean)
	 */
	public ZipEntry uložPoložku(String názovPoložky)
	{ return uložPoložku(názovPoložky, null, false); }

	/** <p><a class="alias"></a> Alias pre {@link #uložPoložku(String) uložPoložku}.</p> */
	public ZipEntry ulozPolozku(String názovPoložky)
	{ return uložPoložku(názovPoložky, null); }


	/**
	 * <p>Rozbalí tento balíček do cieľovej lokality. Metóda umožňuje
	 * spresniť pravidlá, ktorými sa bude riadiť pri procese rozbaľovania.
	 * Konkrétne, či môže prepisovať jestvujúce súbory a vytvárať nejestvujúce
	 * priečinky, ktoré nie sú explicitne vložené ako položky v archíve
	 * (čiže výsledok nezávisí len od parametrov tejto metódy, ale aj od
	 * situácie v rámci konkrétneho archívu).</p>
	 * 
	 * <p>Návratová hodnota metódy vyjadruje počet položiek, ktoré sa
	 * podarilo úspešne rozbaliť (alebo vytvoriť). Automaticky vytvorené
	 * priečinky, ktoré nie sú položkami archívu (pretože toto nie je
	 * v archívoch povinné), ale sú len súčasťou relatívnych ciest položiek
	 * archívu, nie sú do tohto počtu zarátané.</p>
	 * 
	 * <p>Ak otvorenie archívu zlyhá (prípadne zlyhá iná časť inicializácie
	 * procesu), tak je návratová hodnota metódy rovná −1.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je použiteľná
	 * len v prípade, že je archív otvorený alebo otvoriteľný na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie,}</b> to znamená, že
	 * musí jestvovať na umiestnení, na ktoré smeruje táto inštancia
	 * a nesmie byť otvorený na {@linkplain #otvorNaZápis(String) zápis}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Podobne ako pri metóde {@link 
	 * #uložPoložku(String, String, boolean, boolean) uložPoložku}, aj počas
	 * tohto procesu rozbaľovania v prípade, keď niektorá položka archívu
	 * reprezentuje priečinok, sú hodnoty parametrov {@code prepísať}
	 * a {@code vytvoriťCestu} pre túto položku ignorované a priečinok je
	 * automaticky vytvorený, ak nejestvuje. (Práve tieto priečinky sú
	 * zarátané do návratovej hodnoty tejto metódy – pretože sú
	 * reprezentované položkami v archíve.)</p>
	 * 
	 * @param cestaNaRozbalenie cieľová cesta na rozbalenie tohto archívu;
	 *     ak nie je zadaná (má hodnotu {@code valnull}), tak je obsah archívu
	 *     rozbalený na aktuálnom umiestnení (na spúšťacej ceste aplikácie)
	 * @param prepísať príznak povolenia prepísania jestvujúcich súborov na
	 *     disku ({@code valtrue} – jestvujúce cieľové súboru smú byť
	 *     prepísané, ak jestvujú)
	 * @param vytvoriťCestu ak je {@code true}, tak metóda bude automaticky
	 *     vytvárať cesty k rozbaľovaným súborom (ak nejestvujú)
	 * @return počet položiek archívu, ktoré boli úspešne rozbalené
	 *     (vytvorené); záporná hodnota označuje chybu
	 */
	public int rozbaľArchív(String cestaNaRozbalenie,
		boolean prepísať, boolean vytvoriťCestu)
	{
		if (null != výstup)
			throw new GRobotException(
				"Archív je otvorený na zápis.",
				"archiveIsOpenForWriting");

		int úspechov = -1;
		boolean zavri = null == vstup;
		String pôvodnáCestaNaDisku = cestaNaDisku;
		String pôvodnáCestaVArchíve = cestaVArchíve;

		try
		{
			if (null == vstup) otvorNaČítanie();
			if (null != cestaNaRozbalenie)
				cestaNaDisku(cestaNaRozbalenie);

			if (vytvoriťCestu && null != cestaNaDisku)
			{
				File súborCesty = new File(cestaNaDisku);
				if (!súborCesty.exists())
				{
					if (!súborCesty.mkdirs())
						GRobotException.vypíšChybovéHlásenie(
							"Nepodarilo sa vytvoriť cestu: " + cestaNaDisku);
				}
			}

			if (null != vstup)
			{
				Enumeration<? extends ZipEntry> položky = vstup.getEntries();
				úspechov = 0; cestaVArchíve = null;
				while (položky.hasMoreElements())
				{
					ZipEntry položka = položky.nextElement();
					// TODO sekvencia?

					try {
						if (null != uložPoložku(položka.getName(),
							prepísať, vytvoriťCestu)) ++úspechov;
					} catch (Exception e) {
						// Zamlčaný neúspech
					}
				}
			}
		}
		catch (GRobotException e)
		{
			// Zamlčané, aj tak je to v denníku
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}
		finally
		{
			cestaNaDisku = pôvodnáCestaNaDisku;
			cestaVArchíve = pôvodnáCestaVArchíve;
			try { if (zavri) zavri(); } catch (Throwable t) { /* ignorované */ }
		}

		return úspechov;
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozbaľArchív(String, boolean, boolean) rozbaľArchív}.</p> */
	public int rozbalArchiv(String cestaNaRozbalenie,
		boolean prepísať, boolean vytvoriťCestu)
	{ return rozbaľArchív(cestaNaRozbalenie, prepísať, vytvoriťCestu); }

	/**
	 * <p>Táto metóda sa správa rovnako ako jej „hlavná verzia“ spustená
	 * s nasledujúcimi hodnotami parametrov:</p>
	 * 
	 * <p>{@link #rozbaľArchív(String, boolean, boolean)
	 * rozbaľArchív}<code>(cestaNaRozbalenie, prepísať, </code>{@code 
	 * valtrue}<code>)</code></p>
	 * 
	 * <p>Detaily nájdete v opise metódy {@link #rozbaľArchív(String,
	 * boolean, boolean) rozbaľArchív}.</p>
	 * 
	 * @param cestaNaRozbalenie cieľová cesta na rozbalenie tohto archívu
	 * @param prepísať príznak povolenia prepísania jestvujúcich súborov na
	 *     disku
	 * @return počet položiek archívu, ktoré boli úspešne rozbalené (vytvorené)
	 */
	public int rozbaľArchív(String cestaNaRozbalenie, boolean prepísať)
	{ return rozbaľArchív(cestaNaRozbalenie, prepísať, true); }

	/** <p><a class="alias"></a> Alias pre {@link #rozbaľArchív(String, boolean, boolean) rozbaľArchív}.</p> */
	public int rozbalArchiv(String cestaNaRozbalenie, boolean prepísať)
	{ return rozbaľArchív(cestaNaRozbalenie, prepísať); }

	/**
	 * <p>Táto metóda sa správa rovnako ako jej „hlavná verzia“ spustená
	 * s nasledujúcimi hodnotami parametrov:</p>
	 * 
	 * <p>{@link #rozbaľArchív(String, boolean, boolean)
	 * rozbaľArchív}<code>(cestaNaRozbalenie,
	 * </code>{@code valfalse}<code>, </code>{@code valtrue}<code>)</code></p>
	 * 
	 * <p>Detaily nájdete v opise metódy {@link #rozbaľArchív(String,
	 * boolean, boolean) rozbaľArchív}.</p>
	 * 
	 * @param cestaNaRozbalenie cieľová cesta na rozbalenie tohto archívu
	 * @return počet položiek archívu, ktoré boli úspešne rozbalené (vytvorené)
	 */
	public int rozbaľArchív(String cestaNaRozbalenie)
	{ return rozbaľArchív(cestaNaRozbalenie, false, true); }

	/** <p><a class="alias"></a> Alias pre {@link #rozbaľArchív(String, boolean, boolean) rozbaľArchív}.</p> */
	public int rozbalArchiv(String cestaNaRozbalenie)
	{ return rozbaľArchív(cestaNaRozbalenie); }

	/**
	 * <p>Táto metóda sa správa rovnako ako jej „hlavná verzia“ spustená
	 * s nasledujúcimi hodnotami parametrov:</p>
	 * 
	 * <p>{@link #rozbaľArchív(String, boolean, boolean)
	 * rozbaľArchív}<code>(</code>{@code valnull}<code>, prepísať,
	 * </code>{@code valtrue}<code>)</code></p>
	 * 
	 * <p>Detaily nájdete v opise metódy {@link #rozbaľArchív(String,
	 * boolean, boolean) rozbaľArchív}.</p>
	 * 
	 * @param prepísať príznak povolenia prepísania jestvujúcich súborov na
	 *     disku
	 * @return počet položiek archívu, ktoré boli úspešne rozbalené (vytvorené)
	 */
	public int rozbaľArchív(boolean prepísať)
	{ return rozbaľArchív(null, prepísať, true); }

	/** <p><a class="alias"></a> Alias pre {@link #rozbaľArchív(String, boolean, boolean) rozbaľArchív}.</p> */
	public int rozbalArchiv(boolean prepísať) { return rozbaľArchív(prepísať); }

	/**
	 * <p>Táto metóda sa správa rovnako ako jej „hlavná verzia“ spustená
	 * s nasledujúcimi hodnotami parametrov:</p>
	 * 
	 * <p>{@link #rozbaľArchív(String, boolean, boolean)
	 * rozbaľArchív}<code>(</code>{@code valnull}<code>, </code>{@code 
	 * valfalse}<code>, </code>{@code valtrue}<code>)</code></p>
	 * 
	 * <p>Detaily nájdete v opise metódy {@link #rozbaľArchív(String,
	 * boolean, boolean) rozbaľArchív}.</p>
	 * 
	 * @return počet položiek archívu, ktoré boli úspešne rozbalené (vytvorené)
	 */
	public int rozbaľArchív() { return rozbaľArchív(null, false, true); }

	/** <p><a class="alias"></a> Alias pre {@link #rozbaľArchív(String, boolean, boolean) rozbaľArchív}.</p> */
	public int rozbalArchiv() { return rozbaľArchív(); }


	/**
	 * <p>Prečíta všetky dekomprimované údaje položky so zadaným názvom
	 * a vráti ich vo forme poľa bajtov.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
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

		názovPoložky = overAKorigujNázovPoložky(názovPoložky);
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
	 * <p>Prečíta do vnútornej pamäte sveta obrázok z tohto archívu a vytvorí
	 * z neho nový objekt typu {@link Obrázok Obrázok}. Táto metóda
	 * v skutočnosti volá metódu {@link Obrázok#čítaj(Archív, String)
	 * čítaj(archív, názovPoložky)}, ktorá sa spolieha na fungovanie ďalších
	 * metód rámca.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie.}</b></p>
	 * 
	 * <p>Pozri aj príklad v opise metódy {@link Obrázok#čítaj(Archív,
	 * String) Obrázok.čítaj(archív, názovPoložky)}.</p>
	 * 
	 * @param názovPoložky názov položky, z ktorej má byť prečítaný obrázok,
	 *     prípadne sekvencia animácie
	 * @return obrázok prečítaný zo zadanej položky alebo {@code valnull}, ak
	 *     pokus o prečítanie sekvencie súborov zlyhal z pamäťových dôvodov
	 * 
	 * @throws GRobotException ak archív nie je otvorený na čítanie
	 *     (identifikátor {@code archiveNotOpenForReading}) alebo zadaná
	 *     položka s obrázkom nebola nájdená (identifikátor
	 *     {@code entryNotFound})
	 * 
	 * @see Obrázok#čítaj(Archív, String)
	 * @see Obrázok#ulož(Archív, String)
	 * @see Archív#pridajPoložku(String, Obrázok)
	 */
	public Obrázok obrázok(String názovPoložky)
	{ return Obrázok.čítaj(this, názovPoložky); }

	/** <p><a class="alias"></a> Alias pre {@link #obrázok(String) obrázok}.</p> */
	public Obrazok obrazok(String názovPoložky)
	{ return Obrázok.citaj(this, názovPoložky); }


	/**
	 * <p>Pridá do archívu údaje zo zadaného súboru. Metóda dovoľuje zmeniť
	 * názov položky v archíve – ak prvý parameter ({@code názovPoložky})
	 * nie je {@code valnull}, tak zadaný názov bude použitý namiesto
	 * pôvodného názvu súboru. V opačnom prípade bude na pomenovanie položky
	 * v archíve použitý pôvodný názov súboru (z druhého parametra –
	 * {@code názovSúboru}).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis.}</b></p>
	 * 
	 * @param názovPoložky názov pridávanej položky (smie byť {@code valnull};
	 *     v tom prípade bude použitý názov súboru)
	 * @param názovSúboru názov súboru, z ktorého majú byť prečítané
	 *     údaje pridávanej položky
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak archív nie je otvorený na zápis, ak
	 *     bola namiesto názvu súboru zadaná hodnota {@code valnull} alebo
	 *     v prípade pokusu o vloženie duplicitnej položky
	 * 
	 * @see #pridajPoložku(String, byte[])
	 * @see #pridajPoložku(String, Obrázok)
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
				substring(indexOf + 1);
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
			výstup.write(údaje, 0, počet); // TODO sekvencia

		položka.setTime(súbor.lastModified());
		čítanie.close();
		return položka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String, String) pridajPoložku}.</p> */
	public ZipEntry pridajPolozku(String názovPoložky, String názovSúboru)
		throws IOException { return pridajPoložku(názovPoložky, názovSúboru); }

	/**
	 * <p>Zapíše údaje zadané vo forme poľa bajtov do položky archívu so
	 * zadaným názvom.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis.}</b></p>
	 * 
	 * @param názovPoložky názov pridávanej položky (nesmie byť zamlčaný)
	 * @param údajePoložky údaje pridávanej položky (vo forme poľa bajtov)
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak archív nie je otvorený na zápis, ak
	 *     bol názov položky zamlčaný (bola zadaná hodnota {@code valnull})
	 *     alebo v prípade pokusu o vloženie duplicitnej položky
	 * 
	 * @see #pridajPoložku(String, String)
	 * @see #pridajPoložku(String, Obrázok)
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


		názovPoložky = overAKorigujNázovPoložky(názovPoložky);

		if (položkyVýstupu.containsKey(názovPoložky))
			throw new GRobotException("Položka „" +
				názovPoložky + "“ už v archíve jestvuje.",
				"duplicateEntry", názovPoložky);

		ZipEntry položka = new ZipEntry(názovPoložky);
		položkyVýstupu.put(názovPoložky, položka);
		výstup.putNextEntry(položka);

		výstup.write(údajePoložky, 0, údajePoložky.length); // TODO sekvencia
		// Tu nemá zmysel: položka.setTime(súbor.lastModified());
		return položka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String, byte[]) pridajPoložku}.</p> */
	public ZipEntry pridajPolozku(String názovPoložky, byte[] údajePoložky)
		throws IOException { return pridajPoložku(názovPoložky, údajePoložky); }


	/**
	 * <p>Zapíše obrázok do položky archívu so zadaným názvom. Táto metóda
	 * v skutočnosti volá metódu {@link Obrázok#ulož(Archív, String)
	 * Obrázok.ulož(archív, názovPoložky)}, ktorá sa spolieha na fungovanie
	 * ďalších metód rámca.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis.}</b></p>
	 * 
	 * <p>Pozri aj príklad v opise metódy {@link Obrázok#čítaj(Archív,
	 * String) Obrázok.čítaj(archív, názovPoložky)}.</p>
	 * 
	 * @param názovPoložky názov pridávanej položky (nesmie byť zamlčaný)
	 * @param obrázok obrázok, ktorého obsah má byť pridaný ako ďalšia položka
	 *     (prípadne položky)
	 * @return položky archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentárov položiek, úprava ich dátumu
	 *     podľa vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak bol zadaný názov súboru s neplatnou
	 *     príponou, ak archív nie je otvorený na zápis, ak bol názov
	 *     položky zamlčaný (bola zadaná hodnota {@code valnull})
	 *     alebo v prípade pokusu o vloženie duplicitnej položky
	 * 
	 * @see #pridajPoložku(String, String)
	 * @see #pridajPoložku(String, byte[])
	 * @see #cestaVArchíve()
	 * @see #cestaNaDisku()
	 * @see Obrázok#čítaj(Archív, String)
	 * @see Obrázok#ulož(Archív, String)
	 * @see Archív#obrázok(String)
	 */
	public ZipEntry[] pridajPoložku(String názovPoložky, Obrázok obrázok)
		throws IOException { return obrázok.ulož(this, názovPoložky); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String, Obrázok) pridajPoložku}.</p> */
	public ZipEntry[] pridajPolozku(String názovPoložky, Obrázok obrázok)
		throws IOException { return pridajPoložku(názovPoložky, obrázok); }


	/**
	 * <p>Pridá do archívu položku reprezentujúcu priečinok. <b>Pozor!</b>
	 * Metóda pridá iba jednu položku vo význame priečinka. Metóda nepridáva
	 * do archívu prípadný obsah (vnorené položky) jestvujúceho priečinka na
	 * disku. (V skutočnosti priečinok na disku ani nemusí jestvovať a do
	 * archívu ho ako položku môžeme pridať.)</p>
	 * 
	 * <p>Prítomnosť tohto druhu položiek v archíve je nepovinná. Priečinky
	 * a podpriečinky sú softvérmi pracujúcimi s archívmi automaticky
	 * rozpoznávané vďaka ukladaniu ciest k súborom, ktoré sú v rámci
	 * archívu pripojené pred názvy položiek (t. j. zbalených súborov).
	 * Avšak pridanie položky priečinka má význam, a to v prípade, že
	 * chceme do archívu vložiť prázdny priečinok (ktorý sa pri
	 * rozbaľovaní archívu automaticky vytvorí).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis.}</b></p>
	 * 
	 * @param názov názov pridávaného priečinka (nesmie byť zamlčaný)
	 * @return položka archívu knižnice <a href="https://ant.apache.org/"
	 *     target="_blank">Apache Ant</a> <a href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/apacheAntZIP/ZipEntry.java"
	 *     target="_blank"><code>ZipEntry</code></a> na vykonanie prípadných
	 *     ďalších úprav (nastavenie komentára položky, úprava dátumu podľa
	 *     vlastných potrieb a podobne)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 * @throws GRobotException ak archív nie je otvorený na zápis, ak
	 *     bol názov položky zamlčaný (bola zadaná hodnota {@code valnull})
	 *     alebo v prípade pokusu o vloženie duplicitnej položky
	 */
	public ZipEntry pridajPriečinok(String názov) throws IOException
	{
		if (null == výstup)
			throw new GRobotException(
				"Archív nie je otvorený na zápis.",
				"archiveNotOpenForWriting");

		názov = overAKorigujNázovPoložky(názov);
		if (názov.length() > 0 &&
			'/' != názov.charAt(názov.length() - 1) // !názov.endsWith("/")
			) názov += '/';

		if (položkyVýstupu.containsKey(názov))
			throw new GRobotException("Položka „" +
				názov + "“ už v archíve jestvuje.",
				"duplicateEntry", názov);

		ZipEntry položka = new ZipEntry(názov);
		položkyVýstupu.put(názov, položka);
		výstup.putNextEntry(položka);
		return položka;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajPriečinok(String) pridajPriečinok}.</p> */
	public ZipEntry pridajPriecinok(String názov) throws IOException
	{ return pridajPriečinok(názov); }


	/**
	 * <p>Vráti komentár otvoreného archívu, ak je definovaný.
	 * V opačnom prípade vráti hodnotu {@code valnull}.</p>
	 * 
	 * <!-- p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaČítanie(String) čítanie}</b>.</p -->
	 * 
	 * @return komentár otvoreného archívu alebo {@code valnull}
	 * 
	 * @see #komentár(String)
	 */
	public String komentár()
	{
		if (null != vstup) return vstup.getComment();
			// (else) throw new GRobotException(
			// 	"Archív nie je otvorený na čítanie.",
			// 	"archiveNotOpenForReading");

		if (null != výstup) return výstup.getComment();

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #komentár() komentár}.</p>*/
	public String komentar() { return komentár(); }/**/

	/**
	 * <p>Nastaví nový komentár archívu.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda je
	 * použiteľná len v prípade, že je archív otvorený na
	 * <b>{@linkplain #otvorNaZápis(String) zápis}</b>. (V opačnom
	 * prípade vznikne {@linkplain GRobotException výnimka}
	 * {@code srg"archiveNotOpenForWriting"}.)</p>
	 * 
	 * @param komentár nový komentár archívu
	 * @return {@code valtrue} v prípade úspechu akcie
	 * 
	 * @throws GRobotException ak archív nie je otvorený na zápis
	 * 
	 * @see #komentár()
	 */
	public void komentár(String komentár)
	{
		if (null == výstup)
			throw new GRobotException(
				"Archív nie je otvorený na zápis.",
				"archiveNotOpenForWriting");

		if (null == komentár)
			výstup.setComment("");
		else
			výstup.setComment(komentár);
	}

	/** <p><a class="alias"></a> Alias pre {@link #komentár(String) komentár}.</p>*/
	public void komentar(String komentár) { komentár(komentár); }


	// TODO: Keď bude čas, implementovať mechanizmus (ne)ignorovania chýb.
	// 
	// • Metódou ignorujChyby(boolean) sa to zapne/vypne.
	// • Ak je to zapnuté, tak všetky metódy budú silou mocou obchádzať
	//   vrhanie výnimiek a ak to pre nich bude možné, tak sa budú usilovať
	//   indikovať chybový stav nejako inak (návratovou hodnotou, inak?;
	//   v každom prípade: denník funguje).
	// • Ak je to vypnuté, tak budú vrhať výnimky aj metódy ako veľkosťPoložky
	//   a pod.
	// 
	// (Lenže toto isté by sa hodilo implementovať aj do triedy Súbor, čo
	// je v retrospektíve relatívne „nemožné.“)

	// TODO: Významne rozšíriť možnosti pridávania položiek a/alebo prepájania
	// so súčasťami programovacieho rámca: ukladanie obrázkov (PNG, JPEG, GIF,
	// SVG) a zvukov do archívu. Rovnako ich čítanie z archívu.


}
