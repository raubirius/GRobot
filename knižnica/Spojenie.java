
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2022 by Roman Horváth
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

import java.util.Random;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import static knižnica.Konštanty.PREVZATIE_ÚDAJOV;
import static knižnica.Konštanty.ODOVZDANIE_ÚDAJOV;

/**
 * <p>Táto trieda slúži na vytvorenie a udržiavanie komunikácie so
 * serverom.</p>
 * 
 * <!-- TODO – doplniť opis. -->
 * 
 * <p> </p>
 * 
 * <p><b>Príklady použitia – využitie pri komunikácii s PHP
 * serverom:</b></p>
 * 
 * <p class="attention"><b>Upozornenie:</b> Na cieľovom serveri musia
 * jestvovať prislúchajúce PHP skripty, na ktoré sa nasledujúce príklady
 * odvolávajú. (Napríklad: {@code login.php}, {@code acceptFile.php}
 * a podobne.)</p>
 * 
 * <p>Test spojenia:</p>
 * 
 * <p>Táto ukážka použitia predpokladá, že lokálny ({@code srg"localhost"})
 * PHP server je nakonfigurovaný tak, aby počúval na porte 81 a že
 * v podpriečinku {@code bjpu-server} webového koreňa servera je uložený
 * jednoduchý PHP skript {@code test.php}, ktorého výpis je uvedený
 * nižšie.</p>
 * 
 * <!-- TODO – pridať výsledok… -->
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code typeboolean} test()
	{
		{@code currSpojenie} spojenie = {@code kwdnew} {@link Spojenie#Spojenie() Spojenie}();
		spojenie.{@link Spojenie#vzdialenáCesta(String) vzdialenáCesta}({@code srg"bjpu-server"});
		spojenie.{@link Spojenie#identifikátorÚdajov(String) identifikátorÚdajov}({@code srg"bluej-project-uploader"});
		spojenie.{@link Spojenie#port(int) port}({@code num81});

		{@code typeboolean} úspech = spojenie.{@link Spojenie#otvor(String) otvor}({@code srg"test.php"});
		{@code kwdreturn} spojenie.{@link Spojenie#zavri() zavri}() && úspech;
	}
	</pre>
 * 
 * <p>Príklad cieľového skriptu {@code test.php} umiestneného na cieľovom
 * serveri ({@code localhost}) v priečinku {@code bjpu-server} (toto je
 * jediný PHP príklad uvedený v tejto dokumentácii):</p>
 * 
 * <!-- TODO – pridať výsledok… -->
 * 
 * <pre CLASS="example">
	&lt;?php
		{@code kwdheader}({@code srg'Content-Type: text/plain; charset=UTF-8'});
		{@code kwdecho} {@code srg'ERR0: Connected.'};
	?&gt;
	</pre>
 * 
 * <p> </p>
 * 
 * <p>Príklad prihlásenia sa (od tohto miesta ďalej bez PHP príkladov):</p>
 * 
 * <p class="attention"><b>Upozornenie:</b> Na cieľovom serveri musí
 * jestvovať prihlasovací skript {@code login.php}, ktorý požaduje
 * prihlasovacie údaje (meno a heslo) v údajových poliach {@code login}
 * a {@code password}.</p>
 * 
 * <!-- TODO – pridať výsledok… -->
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code currSpojenie} spojenie = {@code kwdnew} {@link Spojenie#Spojenie() Spojenie}();

	{@code kwdpublic} {@code typeboolean} prihlásSa({@link String String} prihlasovacieMeno, {@link String String} heslo)
	{
		{@code typeboolean} úspech;

		{@code kwdif} (úspech = spojenie.{@link Spojenie#otvor(String) otvor}({@code srg"login.php"}))
		{
			úspech =
				spojenie.{@link Spojenie#pridajÚdaj(String, String) pridajÚdaj}({@code srg"login"}, prihlasovacieMeno) &&
				spojenie.{@link Spojenie#pridajÚdaj(String, String) pridajÚdaj}({@code srg"password"}, heslo);
		}

		{@code kwdreturn} spojenie.{@link Spojenie#zavri() zavri}() && úspech;
	}
	</pre>
 * 
 * <p> </p>
 * 
 * <p>Príklad {@linkplain #pošliTextovýSúbor(String) odoslania textového
 * súboru}:</p>
 * 
 * <p class="attention"><b>Upozornenie:</b> Na cieľovom serveri musí
 * jestvovať PHP skript {@code acceptFile.php}, ktorý očakáva prijatie
 * súboru, ktorý spracuje (napríklad uloží do databázy, vykoná určitú
 * akciu na základe jeho obsahu a podobne).</p>
 * 
 * <p class="remark"><b>Poznámka:</b> S použitím tejto triedy je v rámci
 * jedného {@linkplain #otvor(String) otvoreného spojenia} (ktoré je
 * ekvivaletné jednej obojsmernej komunikácii) možné odoslať iba jeden
 * súbor. To znamená, že príkaz {@link #pošliTextovýSúbor(String)
 * pošliTextovýSúbor} (prípadne {@link #pošliSúbor(String) pošliSúbor})
 * musí byť posledný príkazom spojenia a po ňom musí nasledovať volanie
 * metódy {@link #zavri() zavri}.</p>
 * 
 * <!-- TODO – overiť vzhľad a funkčnosť príkladu, pridať výsledok… -->
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code currSpojenie} spojenie = {@code kwdnew} {@link Spojenie#Spojenie() Spojenie}();
	{@code kwdprivate} {@code typeint} sessionID = &#45;{@code num152};    {@code comm// Jedinečný identifikátor sedenia}

	{@code kwdpublic} {@code typeboolean} odošliTextovýSúbor({@link String String} názovSúboru)
	{
		{@code typeboolean} úspech;

		{@code kwdif} (úspech = spojenie.{@link Spojenie#otvor(String) otvor}({@code srg"acceptFile.php"}))
		{
			úspech =
				spojenie.{@link Spojenie#pridajÚdaj(String, long) pridajÚdaj}({@code srg"sessionID"}, sessionID) &&
				spojenie.{@link Spojenie#pošliTextovýSúbor(String) pošliTextovýSúbor}(názovSúboru);
		}

		{@code kwdreturn} spojenie.{@link Spojenie#zavri() zavri}() && úspech;
	}
	</pre>
 * 
 * <p> </p>
 * 
 * <p>Príklad spracovania primitívnej textovej odpovede (s použitím
 * metódy {@link #dajOdpoveď() dajOdpoveď}):</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Budeme predpokladať, že odpoveď sa
 * bude zhodovať s nasledujúcou šablónou vyjadrenou s pomocou regulárneho
 * výrazu:<br />
 *  <br /><code>ERR[+\-]{0,1}[0-9]+: «správa»</code><br /> <br />
 * Príklady:<br />  {@code ERR0: OK}<br />  {@code ERR-1: No error.}<br
 * />  {@code ERR+100: „Nejaká správa“}<br />  a podobne.</p>
 * 
 * <!-- TODO – overiť vzhľad a funkčnosť príkladu, pridať výsledok… -->
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code currSpojenie} spojenie = {@code kwdnew} {@link Spojenie#Spojenie() Spojenie}();

	{@code kwdpublic} {@code typeint} prevezmiChybovýKód()
	{
		{@link String String} odpoveď = spojenie.{@link Spojenie#dajOdpoveď() dajOdpoveď}();

		{@code kwdif} ({@code valnull} != odpoveď && odpoveď.{@link String#startsWith(String) startsWith}({@code srg"ERR"}))
		{
			{@code typeint} index = odpoveď.{@link String#indexOf(int) indexOf}({@code srg':'});
			{@code kwdif} (&#45;{@code num1} != index) {@code kwdtry}
			{
				{@code kwdreturn} Integer.{@link Integer#parseInt(String) parseInt}(odpoveď.{@link String#substring(int, int) substring}({@code num3}, index));
			}
			{@code kwdcatch} ({@link Exception Exception} e)
			{
				e.{@link Throwable#printStackTrace() printStackTrace}();
			}
		}

		{@code kwdreturn} &#45;{@code num1};
	}
	</pre>
 * 
 * <p>Príklad vytvorenia metódy na prevzatie súboru zo servera (na
 * požiadanie):</p>
 * 
 * <!-- TODO – overiť vzhľad a funkčnosť príkladu, pridať výsledok (?)… -->
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code currSpojenie} spojenie = {@code kwdnew} {@link Spojenie#Spojenie() Spojenie}();

	{@code kwdpublic} {@code typevoid} prevezmiSúbor({@link String String} adresa)
	{
		{@code comm// Svet.vykonaťNeskôr(() -> // (ak chceme sledovať priebeh)}
		{
			{@code kwdif} (spojenie.{@link Spojenie#otvorLokalitu(String) otvorLokalitu}(adresa))
			{
				{@code kwdif} (!spojenie.{@link Spojenie#zavri() zavri}())
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Spojenie sa nepodarilo "} +
						{@code srg"korektne zavrieť."});

				{@link String String} uloženýAko = spojenie.{@link Spojenie#uložOdpoveď() uložOdpoveď}();

				{@code kwdif} ({@code valnull} == uloženýAko)
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}(
						{@code srg"Nepodarilo sa prevziať súbor z adresy: "} + adresa);
				{@code kwdelse}
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Súbor prevzatý z adresy: "} + adresa +
						{@code srg"\nbol uložený ako: "} + uloženýAko);

				{@code comm// Ak chceme získať ďalšie detaily o požiadavke:}
				{
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"\nPožiadavka:"});

					{@link String String}[] údaje = spojenie.{@link Spojenie#dajÚdajePožiadavky() dajÚdajePožiadavky}();
					{@code kwdif} ({@code valnull} == údaje)
						{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Žiadne údaje požiadavky."});
					{@code kwdelse}
						{@code kwdfor} ({@link String String} údaj : údaje)
							{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}(údaj);
				}

				{@code comm// Ak chceme získať ďalšie detaily o odpovedi:}
				{
					{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"\nOdpoveď:"});

					{@link String String}[] údaje = spojenie.{@link Spojenie#dajÚdajeOdpovede() dajÚdajeOdpovede}();
					{@code kwdif} ({@code valnull} == údaje)
						{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Žiadne údaje odpovede."});
					{@code kwdelse}
						{@code kwdfor} ({@link String String} údaj : údaje)
							{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}(údaj);
				}
			}
			{@code kwdelse}
				{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Nepodarilo sa ovoriť spojenie na "} +
					{@code srg"prevzatie súboru z adresy: "} + adresa);
		}
		{@code comm// , true); // (ak chceme sledovať priebeh)}
	}

	{@code comm// private static int percento = -1; // (ak chceme sledovať priebeh)}

	{@code comm// (ak chceme sledovať priebeh)}
	{@code comm//  @Override public void sekvencia(int kódSpracovania, Object zdroj,}
	{@code comm//  	Object cieľ, long stav, long celkovo)}
	{@code comm//  {}
	{@code comm//  	if (stav > celkovo) stav = celkovo;}
	{@code comm//  	percento = (int)((stav * 100) / celkovo);}
	{@code comm//  	System.out.println(percento + " %");}
	{@code comm//  }}
	</pre>
 * 
 * <p> </p>
 * 
 * <p><b>Použité a odporúčané zdroje:</b></p>
 * 
 * <ul>
 * <li><small>User:Albe – <a
 * href="https://stackoverflow.com/users/1788806/willi-mentzel"
 * target="_blank">User:Willi Mentzel</a> – and others</small>: <a
 * href="http://stackoverflow.com/questions/1314249/upload-and-post-file-to-php-page"
 * target="_blank"><em>Upload and POST file to PHP page with Java.</em> Stack
 * Overflow, 2009, 2017. Citované: 2018.</a></li>
 * 
 * <li><small>Vlad Patryshev</small>: <a
 * href="http://www.devx.com/java/Article/17679/1954" target="_blank"><em>Send
 * Form Data from Java: A Painless Solution.</em> developer.com, 2003.
 * Citované: 2018.</a></li>
 * 
 * <li><small><a href="http://www.mkyong.com/author/mkyong/"
 * target="_blank">mkyong</a></small>: <a
 * href="http://www.mkyong.com/java/how-to-get-mac-address-in-java/"
 * target="_blank"><em>How to get MAC address in Java.</em> mkyong.com,
 * 2010. Citované: 2018.</a></li>
 * </ul>
 */
public class Spojenie
{
	// TODO – Všetky implicitne predvolené hodnoty rámca preniesť do denníka
	// (bude to časovo náročné, ale potrebné), pretože sa môžu stať zdrojom
	// ťažko identifikovateľných chýb…


	// Generátor náhodných čísiel. (Je používaný pri vytváraní ohraničenia
	// údajov odosielaných počas komunikácie prostredníctvom inštancií
	// tejto triedy.)
	private final static Random random = new Random();

	// Konštanta konca riadka.
	private final static String EOL = "\r\n";


	// URL spojenie tejto inštancie.
	private URLConnection spojenie = null;

	// Výstupný prúd na zápis údajov počas komunikácie.
	private OutputStream výstup = null;

	// Vstupný prúd na čítanie údajov počas komunikácie.
	private InputStream vstup = null;

	// URL aktuálneho spojenia.
	private URL url = null;


	// Protokol používaný týmto spojením.
	private String protokol = "http";

	// Cieľová doména tohto spojenia.
	private String doména = "localhost";

	// Komunikačný port tohto spojenia.
	private int port = 80;

	// Vzdialená cesta používaná pri komunikácii prostredníctvom
	// tohto spojenia.
	private String vzdialenáCesta = "";

	// Koreň spojenia zostavený z protokolu, domény, portu a vzdialenej cesty.
	private String koreň = "http://localhost/";

	// Uchovanie aktuálneho vzdialeného súboru (alebo URI požiadavky).
	private String vzdialenýSúbor = null;


	// Unikátny identifikátor slúžiaci na vytvorenie jedinečného
	// ohraničenia údajov odosielaných pri komunikácii prostredníctvom
	// tohto spojenia.
	private String identifikátorÚdajov = "grobot-data-boundary";

	// Lokálna cesta používaná pri odosielaní súborov prostredníctvom
	// tohto spojenia.
	private String lokálnaCesta = "";


	// Atribút uchovávajúci hlavičku obsahu odosielanej správy.
	private String hlavičkaObsahuSprávy = "";

	// Atribút uchovávajúci chvost obsahu odosielanej správy.
	private String chvostObsahuSprávy = "";

	// Jedinečné ohraničenie údajov vytvorené s použitím identifikátora
	// údajov a generátora náhodných čísiel (vyššie).
	private String hranica = "";

	// Atribút slúžiaci na uchovanie typu obsahu poslednej odpovede servera.
	private String typObsahu = null;

	// Atribút slúžiaci na uchovanie veľkosti poslednej odpovede servera.
	private long veľkosťOdpovede = -1L;

	// Atribút slúžiaci na uchovanie niektorých údajov poslednej požiadavky
	// odosielanej na server.
	private Map<String, List<String>> údajePožiadavky = null;

	// Atribút slúžiaci na uchovanie hlavičky poslednej odpovede servera.
	private Map<String, List<String>> údajeOdpovede = null;

	// Atribút slúžiaci na uchovanie poslednej textovej odpovede servera.
	private String odpoveď = null;

	// Atribút slúžiaci na uchovanie kódu poslednej odpovede servera.
	private int kódOdpovede = -1;

	// Atribút slúžiaci na uchovanie poslednej binárnej (netextovej)
	// odpovede servera.
	private byte[] bajtyOdpovede = null;


	// Atribút s názvom údajového poľa, v ktorom bude odoslaný dátum
	// a čas poslednej úpravy odosielaného súboru v celočíselnej podobe.
	private String pošliNaposledyUpravený = "lastModified";

	// Veľkosť prílohy (súboru) odosielanej pri jednej komunikácii.
	private long veľkosťPrílohy = 0;

	// Príznak odoslania hlavičky správy počas jednej komunikácie.
	private boolean hlavičkaPoslaná = false;

	// Príznak odoslania chvosta správy počas jednej komunikácie.
	private boolean chvostPoslaný = false;

	// Príznak platnosti prečítania odpovede.
	private boolean odpoveďPrečítaná = false;


	/**
	 * <p>Vytvorí inštanciu komunikácie prostredníctvom počítačovej siete
	 * s predvolenými údajmi spojenia. Predvolenými sú nasledujúce údaje
	 * súvisiace so sieťovou komunikáciou:</p>
	 * 
	 * <ul>
	 * <li>{@linkplain #protokol(String) Protokol}: {@code srg"http"}</li>
	 * <li>{@linkplain #doména(String) Doména}: {@code srg"localhost"}</li>
	 * <li>{@linkplain #port(int) Port}: {@code num80}</li>
	 * <li>{@linkplain #vzdialenáCesta(String) Vzdialená cesta}:
	 * <em>«prázdna»</em></li>
	 * <li>{@linkplain #identifikátorÚdajov(String) Identifikátor údajov}:
	 * {@code srg"grobot-data-boundary"}</li>
	 * <li>{@linkplain #lokálnaCesta(String) Lokálna cesta}:
	 * <em>«prázdna»</em></li>
	 * </ul>
	 * 
	 * @see #Spojenie(String)
	 * @see #Spojenie(String, String)
	 * 
	 * @see #otvor(String)
	 * @see #zavri()
	 * 
	 * @see #dajSpojenie()
	 */
	public Spojenie() {}

	/**
	 * <p>Vytvorí inštanciu komunikácie prostredníctvom počítačovej siete
	 * so zadanou doménou a ostatnými údajmi spojenia nastavenými na
	 * predvolené hodnoty. Pozri konštruktor: {@link #Spojenie()
	 * Spojenie}</p>
	 * 
	 * @param doména cieľová doména tejto komunikácie
	 * 
	 * @throws GRobotException v prípade zadania chybných údajov spojenia
	 * 
	 * @see #Spojenie(String)
	 * @see #Spojenie(String, String)
	 * 
	 * @see #doména(String)
	 * 
	 * @see #otvor(String)
	 * @see #zavri()
	 * 
	 * @see #dajSpojenie()
	 */
	public Spojenie(String doména)
	{
		this.doména = doména;
		zostavKoreň();
	}

	/**
	 * <p>Vytvorí inštanciu komunikácie prostredníctvom počítačovej siete
	 * so zadanou doménou, vzdialenou cestou a ostatnými údajmi spojenia
	 * nastavenými na predvolené hodnoty. Pozri konštruktor: {@link 
	 * #Spojenie() Spojenie}</p>
	 * 
	 * @param doména cieľová doména tejto komunikácie
	 * @param vzdialenáCesta vzdialená cesta tejto komunikácie
	 * 
	 * @throws GRobotException v prípade zadania chybných údajov spojenia
	 * 
	 * @see #Spojenie(String)
	 * @see #Spojenie(String, String)
	 * 
	 * @see #doména(String)
	 * @see #vzdialenáCesta(String)
	 * 
	 * @see #otvor(String)
	 * @see #zavri()
	 * 
	 * @see #dajSpojenie()
	 */
	public Spojenie(String doména, String vzdialenáCesta)
	{
		this.doména = doména;
		this.vzdialenáCesta = vzdialenáCesta;
		zostavKoreň();
	}


	// Odošle hlavičku správy na server.
	private boolean pošliHlavičku()
	{
		if (hlavičkaPoslaná) return false;

		try
		{
			// System.out.println("Hlavička: „" + hlavičkaObsahuSprávy + "“");
			byte[] bytes = hlavičkaObsahuSprávy.getBytes("UTF-8");

			spojenie.setRequestProperty("Content-Length",
				String.valueOf((bytes.length + chvostObsahuSprávy.
					getBytes("UTF-8").length + veľkosťPrílohy)));

			údajePožiadavky = spojenie.getRequestProperties();

			// System.out.println("Do output: " + spojenie.getDoOutput());

			výstup = spojenie.getOutputStream();
			výstup.write(bytes);
			hlavičkaPoslaná = true;

			return true;
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenie("Nemôžem poslať hlavičku…");
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	// Odošle chvost správy na server.
	private boolean pošliChvost()
	{
		if (!hlavičkaPoslaná && !pošliHlavičku()) return false;
		if (chvostPoslaný) return false;

		try
		{
			// System.out.println("Chvost: „" + chvostObsahuSprávy + "“");
			výstup.write(chvostObsahuSprávy.getBytes("UTF-8"));
			výstup.flush();
			chvostPoslaný = true;

			return true;
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenie("Nemôžem poslať chvost…");
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	// Údaje súvisiace s reakciou na udalosť sekvencie prevzatia:
	private String sekvenciaTransferuZdroj = null;
	private String sekvenciaTransferuCieľ = null;
	private long sekvenciaTransferuStav = -1;
	private long sekvenciaTransferuCelkovo = -1;

	// Spustí reakcie na udalosti prevzatia údajov.
	private void sekvenciaTransferuÚdajov(int typTransferu)
	{
		if (null != ObsluhaUdalostí.počúvadlo)
			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				ObsluhaUdalostí.počúvadlo.sekvencia(
					typTransferu, sekvenciaTransferuZdroj,
					sekvenciaTransferuCieľ, sekvenciaTransferuStav,
					sekvenciaTransferuCelkovo);
			}

		synchronized (ÚdajeUdalostí.zámokUdalostí)
		{
			int početPočúvajúcich = GRobot.počúvajúciSúbory.size();
			for (int i = 0; i < početPočúvajúcich; ++i)
			{
				GRobot počúvajúci = GRobot.počúvajúciSúbory.get(i);
				počúvajúci.sekvencia(typTransferu,
					sekvenciaTransferuZdroj, sekvenciaTransferuCieľ,
					sekvenciaTransferuStav, sekvenciaTransferuCelkovo);
			}
		}

		// Na testovanie: Svet.čakaj(0.200);
	}

	// Zistí typ obsahu odpovede a prečíta odpoveď komunikácie prijatú zo
	// servera vo forme reťazca alebo poľa bajtov podľa typu obsahu.
	private boolean čítajOdpoveď(FileOutputStream zápis)
	{
		if (protokol.equals("file"))
		{
			if (!odpoveďPrečítaná) try
			{
				// System.out.println("Čítam súbor…");

				// Aj pri tomto protokole budú viaceré z údajov prečítané
				// korektne (rovnako, ako keby išlo o komunikáciu so
				// serverom)…

				try { údajeOdpovede = spojenie.getHeaderFields(); }
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

				try { typObsahu = spojenie.getContentType(); }
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

				try
				{
					veľkosťOdpovede = spojenie.getContentLengthLong();
					if (sekvenciaTransferuCelkovo < 0)
						sekvenciaTransferuCelkovo = veľkosťOdpovede;
					else
						sekvenciaTransferuCelkovo += veľkosťOdpovede;
				}
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

				try
				{
					if (spojenie instanceof HttpURLConnection)
						kódOdpovede = ((HttpURLConnection)spojenie).
							getResponseCode();
				}
				catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

				// System.out.println("Údaje odpovede: " + údajeOdpovede);
				// System.out.println("Typ obsahu: " + typObsahu);
				// System.out.println("Veľkosť: " + veľkosťOdpovede);
				// System.out.println("Kód odpovede: " + kódOdpovede);

				try
				{
					vstup = spojenie.getInputStream();
				}
				catch (FileNotFoundException ex)
				{
					if (spojenie instanceof HttpURLConnection)
						vstup = ((HttpURLConnection)spojenie).getErrorStream();
					else
						throw ex;
				}

				BufferedReader čítač = null;

				// Celý nasledujúci blok je pri tomto protokole pravdepodobne
				// zbytočný, pretože typ obsahu zrejme nikdy nebude obsahovať
				// údaj o kódovaní…
				if (null == zápis && null != typObsahu)
				{
					String typMalým = typObsahu.trim().toLowerCase();
					if (typMalým.startsWith("text/"))
					{
						if (-1 != typMalým.indexOf("utf-8"))
						{
							čítač = new BufferedReader(
								new InputStreamReader(vstup, "UTF-8"));
						}
						else
						{
							int indexOf = typMalým.indexOf("charset");
							if (-1 != indexOf)
							{
								StringBuffer charset = new StringBuffer(
									typObsahu.trim().substring(indexOf + 7));

								while (charset.length() > 0 &&
									(' ' == charset.charAt(0) ||
										'=' == charset.charAt(0)))
									charset.deleteCharAt(0);

								čítač = new BufferedReader(
									new InputStreamReader(vstup,
										charset.toString()));
							}
						}

						if (null != čítač)
						{
							StringBuffer zásobník = new StringBuffer();
							odpoveď = ""; String riadok; int počet = 0;
							while (null != (riadok = čítač.readLine()))
							{
								if (0 == počet++)
								{
									// odpoveď += riadok;
									zásobník.append(riadok);
									sekvenciaTransferuStav += riadok.length();
								}
								else
								{
									// odpoveď += EOL + riadok;
									zásobník.append(EOL);
									zásobník.append(riadok);
									sekvenciaTransferuStav +=
										2 + riadok.length();
								}
								sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
							}
							odpoveď = zásobník.toString();

							// Treba pre istotu overiť veľkosť odpovede,
							// lebo zdroj údajov mohol obsahovať kratšie
							// symboly koncov riadkov:
							if (veľkosťOdpovede != odpoveď.length())
							{
								if (veľkosťOdpovede >= 0)
									GRobotException.vypíšChybovéHlásenie(
										"Bola zaznamenaná nezrovnalosť " +
										"v počte bajtov prijatých zo " +
										"servera. V tomto prípade by " +
										"nemalo ísť o závažný problém, " +
										"server pravdepodobne používa " +
										"iné symboly na signalizáciu " +
										"koncov riadkov. Očakávaný počet " +
										"bajtov: " + veľkosťOdpovede +
										"; počet bajtov v zostavenej " +
										"odpovedi: " + odpoveď.length() + ".");
								veľkosťOdpovede = odpoveď.length();
							}

							// System.out.println("  Textová odpoveď: " +
							// 	odpoveď.length());
						}
					}
				}

				// Avšak bajty prečítame bez problémov:
				if (null == čítač)
				{
					if (null == zápis)
					{
						byte[] údaje = new byte[32768]; int prečítaných;
						ByteArrayOutputStream prečítané =
							new ByteArrayOutputStream();

						while ((prečítaných = vstup.read(údaje)) > 0)
						{
							prečítané.write(údaje, 0, prečítaných);
							sekvenciaTransferuStav += prečítaných;
							sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
						}

						bajtyOdpovede = prečítané.toByteArray();

						if (veľkosťOdpovede != bajtyOdpovede.length)
						{
							if (veľkosťOdpovede >= 0)
								GRobotException.vypíšChybovéHlásenie(
									"Bola zaznamenaná nezrovnalosť " +
									"v počte bajtov prijatých zo " +
									"servera. Pravdepodobne vznikla " +
									"chyba pri prenose údajov. " +
									"Očakávaný počet bajtov: " +
									veľkosťOdpovede + "; počet " +
									"bajtov v prijatej odpovedi: " +
									bajtyOdpovede.length + ".", true);
							veľkosťOdpovede = bajtyOdpovede.length;
						}
					}
					else
					{
						byte[] údaje = new byte[32768]; int prečítaných;
						while ((prečítaných = vstup.read(údaje)) > 0)
						{
							zápis.write(údaje, 0, prečítaných);
							sekvenciaTransferuStav += prečítaných;
							sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
						}
					}

					// System.out.println("  Bajty odpovede: " +
					// 	bajtyOdpovede.length);
				}
				else try { čítač.close(); } catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

				odpoveďPrečítaná = true;
				return true;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Chyba pri čítaní súboru…");
				GRobotException.vypíšChybovéHlásenia(e);
				return false;
			}
		}
		else if ((chvostPoslaný || pošliChvost()) && !odpoveďPrečítaná) try
		{
			// System.out.println("Čítam odpoveď…");

			údajeOdpovede = spojenie.getHeaderFields();
			typObsahu = spojenie.getContentType();
			veľkosťOdpovede = spojenie.getContentLengthLong();
			if (sekvenciaTransferuCelkovo < 0)
				sekvenciaTransferuCelkovo = veľkosťOdpovede;
			else
				sekvenciaTransferuCelkovo += veľkosťOdpovede;

			// System.out.println("Typ obsahu: " + typObsahu);
			// System.out.println("Veľkosť: " + veľkosťOdpovede);

			if (spojenie instanceof HttpURLConnection)
				kódOdpovede = ((HttpURLConnection)spojenie).getResponseCode();

			// System.out.println("Kód odpovede: " + kódOdpovede);

			try
			{
				vstup = spojenie.getInputStream();
			}
			catch (FileNotFoundException ex)
			{
				if (spojenie instanceof HttpURLConnection)
					vstup = ((HttpURLConnection)spojenie).getErrorStream();
				else
					throw ex;
			}

			BufferedReader čítač = null;

			if (null == zápis && null != typObsahu)
			{
				String typMalým = typObsahu.trim().toLowerCase();
				if (typMalým.startsWith("text/"))
				{
					if (-1 != typMalým.indexOf("utf-8"))
					{
						čítač = new BufferedReader(
							new InputStreamReader(vstup, "UTF-8"));
					}
					else
					{
						int indexOf = typMalým.indexOf("charset");
						if (-1 != indexOf)
						{
							StringBuffer charset = new StringBuffer(
								typObsahu.trim().substring(indexOf + 7));

							while (charset.length() > 0 &&
								(' ' == charset.charAt(0) ||
									'=' == charset.charAt(0)))
								charset.deleteCharAt(0);

							čítač = new BufferedReader(
								new InputStreamReader(vstup,
									charset.toString()));
						}
					}

					if (null != čítač)
					{
						StringBuffer zásobník = new StringBuffer();
						odpoveď = ""; String riadok; int počet = 0;
						while (null != (riadok = čítač.readLine()))
						{
							if (0 == počet++)
							{
								// odpoveď += riadok;
								zásobník.append(riadok);
								sekvenciaTransferuStav += riadok.length();
							}
							else
							{
								// odpoveď += EOL + riadok;
								zásobník.append(EOL);
								zásobník.append(riadok);
								sekvenciaTransferuStav +=
									2 + riadok.length();
							}
							sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
						}
						odpoveď = zásobník.toString();

						// Treba pre istotu overiť veľkosť odpovede,
						// lebo zdroj údajov mohol obsahovať kratšie
						// symboly koncov riadkov:
						if (veľkosťOdpovede != odpoveď.length())
						{
							if (veľkosťOdpovede >= 0)
								GRobotException.vypíšChybovéHlásenie(
									"Bola zaznamenaná nezrovnalosť " +
									"v počte bajtov prijatých zo " +
									"servera. V tomto prípade by " +
									"nemalo ísť o závažný problém, " +
									"server pravdepodobne používa " +
									"iné symboly na signalizáciu " +
									"koncov riadkov. Očakávaný počet " +
									"bajtov: " + veľkosťOdpovede +
									"; počet bajtov v zostavenej " +
									"odpovedi: " + odpoveď.length() + ".");
							veľkosťOdpovede = odpoveď.length();
						}

						// System.out.println("  Textová odpoveď: " +
						// 	odpoveď.length());
					}
				}
			}

			if (null == čítač)
			{
				if (null == zápis)
				{
					byte[] údaje = new byte[32768]; int prečítaných;
					ByteArrayOutputStream prečítané =
						new ByteArrayOutputStream();

					while ((prečítaných = vstup.read(údaje)) > 0)
					{
						prečítané.write(údaje, 0, prečítaných);
						sekvenciaTransferuStav += prečítaných;
						sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
					}

					bajtyOdpovede = prečítané.toByteArray();

					if (veľkosťOdpovede != bajtyOdpovede.length)
					{
						if (veľkosťOdpovede >= 0)
							GRobotException.vypíšChybovéHlásenie(
								"Bola zaznamenaná nezrovnalosť " +
								"v počte bajtov prijatých zo " +
								"servera. Pravdepodobne vznikla " +
								"chyba pri prenose údajov. " +
								"Očakávaný počet bajtov: " +
								veľkosťOdpovede + "; počet " +
								"bajtov v prijatej odpovedi: " +
								bajtyOdpovede.length + ".", true);
						veľkosťOdpovede = bajtyOdpovede.length;
					}
				}
				else
				{
					byte[] údaje = new byte[32768]; int prečítaných;
					while ((prečítaných = vstup.read(údaje)) > 0)
					{
						zápis.write(údaje, 0, prečítaných);
						sekvenciaTransferuStav += prečítaných;
						sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);
					}
				}

				// System.out.println("  Bajty odpovede: " +
				// 	bajtyOdpovede.length);
			}
			else try { čítač.close(); } catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }

			odpoveďPrečítaná = true;
			return true;
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenie("Chyba pri čítaní odpovede…");
			GRobotException.vypíšChybovéHlásenia(e);
			return false;
		}

		return false;
	}


	// Zostaví koreňovú URL adresu podľa protokolu, servera, portu a cesty.
	private void zostavKoreň()
	{
		if (null == protokol)
		{
			String prefixChyby = "Hodnota " + protokol +
				" nie je pre protokol povolená. ";
			protokol = "http";
			GRobotException.vypíšChybovéHlásenie(prefixChyby +
				"Nastavujem predvolenú hodnotu: " + protokol/*, false*/);
		}

		if (null == doména)
		{
			String prefixChyby = "Hodnota " + doména +
				" nie je pre doménu povolená. ";
			doména = "localhost";
			GRobotException.vypíšChybovéHlásenie(prefixChyby +
				"Nastavujem predvolenú hodnotu: " + doména/*, false*/);
		}

		if (port < 0 || port > 65535)
		{
			String prefixChyby = "Neplatná hodnota portu: " + port;
			port = 80;
			GRobotException.vypíšChybovéHlásenie(prefixChyby +
				" Nastavujem predvolenú hodnotu: " + port/*, false*/);
		}

		if (null == vzdialenáCesta)
		{
			String prefixChyby = "Hodnota " + vzdialenáCesta +
				" nie je pre vzdialenú cestu povolená. ";
			vzdialenáCesta = "";
			if (vzdialenáCesta.isEmpty())
				GRobotException.vypíšChybovéHlásenie(prefixChyby +
					"Nastavujem vzdialenú cestu na prázdny reťazec."
					/*, false*/);
			else
				GRobotException.vypíšChybovéHlásenie(prefixChyby +
					"Nastavujem predvolenú hodnotu: " + vzdialenáCesta
					/*, false*/);
		}

		if (-1 != protokol.indexOf('/') ||
			-1 != protokol.indexOf(':'))
			throw new GRobotException("Neplatný protokol: " +
				protokol, "invalidProtocol", protokol);

		if (-1 != doména.indexOf('/') ||
			-1 != doména.indexOf(':'))
			throw new GRobotException("Neplatná doména: " +
				doména, "invalidDomain", doména);

		// if (-1 != vzdialenáCesta.indexOf(':')) // nie je chyba
		// 	throw new GRobotException("Neplatná vzdialená cesta: " +
		// 		vzdialenáCesta, "invalidRemotePath", vzdialenáCesta);

		// while (vzdialenáCesta.endsWith('/') // neupravovať

		if (80 != port)
		{
			if (vzdialenáCesta.isEmpty())
				koreň = protokol + "://" + doména + ':' + port + '/';
			else
				koreň = protokol + "://" + doména + ':' + port + '/' +
					vzdialenáCesta + '/';
		}
		else
		{
			if (vzdialenáCesta.isEmpty())
				koreň = protokol + "://" + doména + '/';
			else
				koreň = protokol + "://" + doména + '/' +
					vzdialenáCesta + '/';
		}
	}


	/**
	 * <p>Nastaví nový protokol.</p>
	 * 
	 * @param protokol komunikačný protokol, napríklad http alebo https
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #protokol()
	 * 
	 * @see #Spojenie(String, String)
	 * 
	 * @see #doména(String)
	 * @see #port(int)
	 * @see #vzdialenáCesta(String)
	 * @see #identifikátorÚdajov(String)
	 * @see #lokálnaCesta(String)
	 */
	public boolean protokol(String protokol)
	{
		if (null == protokol)
		{
			String prefixChyby = "Hodnota " + protokol +
				" nie je pre protokol povolená. ";
			protokol = "http";
			GRobotException.vypíšChybovéHlásenie(prefixChyby +
				"Nastavujem predvolenú hodnotu: " + protokol/*, false*/);
		}

		if (-1 == protokol.indexOf('/') &&
			-1 == protokol.indexOf(':'))
		{
			this.protokol = protokol;
			zostavKoreň();
			return true;
		}

		return false;
	}

	/**
	 * <p>Vráti aktuálny protokol.</p>
	 * 
	 * @return aktuálny komunikačný protokol tohto spojenia
	 * 
	 * @see #protokol(String)
	 * 
	 * @see #doména()
	 * @see #port()
	 * @see #vzdialenáCesta()
	 * @see #identifikátorÚdajov()
	 * @see #lokálnaCesta()
	 */
	public String protokol() { return protokol; }


	/**
	 * <p>Nastaví novú doménu.</p>
	 * 
	 * @param doména sieťová doména servera, s ktorým má byť nadviazaná
	 *     komunikácia
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #doména()
	 * 
	 * @see #Spojenie(String)
	 * 
	 * @see #protokol(String)
	 * @see #port(int)
	 * @see #vzdialenáCesta(String)
	 * @see #identifikátorÚdajov(String)
	 * @see #lokálnaCesta(String)
	 */
	public boolean doména(String doména)
	{
		if (null == doména)
		{
			String prefixChyby = "Hodnota " + doména +
				" nie je pre doménu povolená. ";
			doména = "localhost";
			GRobotException.vypíšChybovéHlásenie(prefixChyby +
				"Nastavujem predvolenú hodnotu: " + doména/*, false*/);
		}

		if (-1 == doména.indexOf('/') &&
			-1 == doména.indexOf(':'))
		{
			this.doména = doména;
			zostavKoreň();
			return true;
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #doména(String) doména}.</p> */
	public boolean domena(String doména) { return doména(doména); }

	/**
	 * <p>Vráti aktuálnu doménu.</p>
	 * 
	 * @return aktuálna doména tejto inštancie spojenia
	 * 
	 * @see #doména(String)
	 * 
	 * @see #protokol()
	 * @see #port()
	 * @see #vzdialenáCesta()
	 * @see #identifikátorÚdajov()
	 * @see #lokálnaCesta()
	 */
	public String doména() { return doména; }

	/** <p><a class="alias"></a> Alias pre {@link #doména() doména}.</p> */
	public String domena() { return doména; }


	/**
	 * <p>Nastaví nový port. Hodnota musí byť v rozsahu 0 – 65 535. Ak je
	 * zadaná hodnota mimo tohto rozsahu, port nebude nastavený (zmenený).
	 * Predvolenou hodnotou je číslo 80.</p>
	 * 
	 * @param port číslo portu v rozsahu 0 – 65 535
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #port()
	 * 
	 * @see #protokol(String)
	 * @see #doména(String)
	 * @see #vzdialenáCesta(String)
	 * @see #identifikátorÚdajov(String)
	 * @see #lokálnaCesta(String)
	 */
	public boolean port(int port)
	{
		if (port >= 0 && port <= 65535)
		{
			this.port = port;
			zostavKoreň();
			return true;
		}
		return false;
	}

	/**
	 * <p>Vráti aktuálne číslo portu. Predvolenou hodnotou je číslo portu
	 * 80.</p>
	 * 
	 * @return aktuálne číslo portu
	 * 
	 * @see #port(int)
	 * 
	 * @see #protokol()
	 * @see #doména()
	 * @see #vzdialenáCesta()
	 * @see #identifikátorÚdajov()
	 * @see #lokálnaCesta()
	 */
	public int port() { return port; }


	/**
	 * <p>Nastaví novú vzdialenú cestu.</p>
	 * 
	 * @param vzdialenáCesta vzdialená cesta na serveri
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #vzdialenáCesta()
	 * 
	 * @see #Spojenie(String, String)
	 * 
	 * @see #protokol(String)
	 * @see #doména(String)
	 * @see #port(int)
	 * @see #identifikátorÚdajov(String)
	 * @see #lokálnaCesta(String)
	 */
	public boolean vzdialenáCesta(String vzdialenáCesta)
	{
		if (null == vzdialenáCesta)
		{
			String prefixChyby = "Hodnota " + vzdialenáCesta +
				" nie je pre vzdialenú cestu povolená. ";
			vzdialenáCesta = "";
			if (vzdialenáCesta.isEmpty())
				GRobotException.vypíšChybovéHlásenie(prefixChyby +
					"Nastavujem vzdialenú cestu na prázdny reťazec."
					/*, false*/);
			else
				GRobotException.vypíšChybovéHlásenie(prefixChyby +
					"Nastavujem predvolenú hodnotu: " + vzdialenáCesta
					/*, false*/);
		}

		// if (-1 == vzdialenáCesta.indexOf(':')) // nie je chyba
		// {
			this.vzdialenáCesta = vzdialenáCesta;
			zostavKoreň();
			return true;
		// }
		// 
		// return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenáCesta(String) vzdialenáCesta}.</p> */
	public boolean vzdialenaCesta(String vzdialenáCesta)
	{ return vzdialenáCesta(vzdialenáCesta); }

	/**
	 * <p>Vráti aktuálnu vzdialenú cestu.</p>
	 * 
	 * @return aktuálna vzdialená cesta tohto spojenia
	 * 
	 * @see #vzdialenáCesta(String)
	 * 
	 * @see #protokol()
	 * @see #doména()
	 * @see #port()
	 * @see #identifikátorÚdajov()
	 * @see #lokálnaCesta()
	 */
	public String vzdialenáCesta() { return vzdialenáCesta; }

	/** <p><a class="alias"></a> Alias pre {@link #vzdialenáCesta() vzdialenáCesta}.</p> */
	public String vzdialenaCesta() { return vzdialenáCesta; }


	/**
	 * <p>Nastaví nový identifikátor údajov. Tento identifikátor slúži na
	 * vytvorenie ohraničenia oblasti údajov HTTP požiadavky. Mal by byť
	 * zvolený tak, aby sa priamo nevyskytoval v odosielaných údajoch.
	 * Predvolená hodnota identifikátora je reťazec:
	 * {@code srg"grobot-data-boundary"}.</p>
	 * 
	 * @param identifikátorÚdajov nový identifikátor, ktorý poslúži na
	 *     vytvorenie oddeľovača odosielaných údajov
	 * 
	 * @see #identifikátorÚdajov()
	 * 
	 * @see #protokol(String)
	 * @see #doména(String)
	 * @see #port(int)
	 * @see #vzdialenáCesta(String)
	 * @see #lokálnaCesta(String)
	 */
	public void identifikátorÚdajov(String identifikátorÚdajov)
	{ this.identifikátorÚdajov = identifikátorÚdajov; }

	/** <p><a class="alias"></a> Alias pre {@link #identifikátorÚdajov(String) identifikátorÚdajov}.</p> */
	public void identifikatorUdajov(String identifikátorÚdajov)
	{ identifikátorÚdajov(identifikátorÚdajov); }

	/**
	 * <p>Vráti aktuálny identifikátor údajov.</p>
	 * 
	 * @return aktuálny identifikátor slúžiaci na vytvorenie ohraničenia
	 *     oblasti údajov posielaných prostredníctvom tohto spojenia
	 * 
	 * @see #identifikátorÚdajov(String)
	 * 
	 * @see #protokol()
	 * @see #doména()
	 * @see #port()
	 * @see #vzdialenáCesta()
	 * @see #lokálnaCesta()
	 */
	public String identifikátorÚdajov() { return identifikátorÚdajov; }

	/** <p><a class="alias"></a> Alias pre {@link #identifikátorÚdajov() identifikátorÚdajov}.</p> */
	public String identifikatorUdajov() { return identifikátorÚdajov; }


	/**
	 * <p>Nastaví novú lokálnu cestu k súborom na odoslanie.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Lokálnu cestu je vhodné nastaviť
	 * pred volaním niektorého z klonov metód {@link 
	 * #pošliTextovýSúbor(String) pošliTextovýSúbor}
	 * a {@link #pošliSúbor(String) pošliSúbor}.</p>
	 * 
	 * @param lokálnaCesta lokálna cesta k súborom na odoslanie
	 * 
	 * @see #lokálnaCesta()
	 * 
	 * @see #protokol(String)
	 * @see #doména(String)
	 * @see #port(int)
	 * @see #vzdialenáCesta(String)
	 * @see #identifikátorÚdajov(String)
	 */
	public void lokálnaCesta(String lokálnaCesta)
	{
		if (lokálnaCesta.endsWith("\\") ||
			lokálnaCesta.endsWith("/"))
			this.lokálnaCesta = lokálnaCesta;
		else
			this.lokálnaCesta = lokálnaCesta + File.separator;
	}

	/** <p><a class="alias"></a> Alias pre {@link #lokálnaCesta(String) lokálnaCesta}.</p> */
	public void lokalnaCesta(String lokálnaCesta)
	{ lokálnaCesta(lokálnaCesta); }

	/**
	 * <p>Vráti aktuálnu lokálnu cestu k súborom na odoslanie.</p>
	 * 
	 * @return aktuálna lokálna cesta použitá počas tejto komunikácie
	 * 
	 * @see #lokálnaCesta(String)
	 * 
	 * @see #protokol()
	 * @see #doména()
	 * @see #port()
	 * @see #vzdialenáCesta()
	 * @see #identifikátorÚdajov()
	 */
	public String lokálnaCesta() { return lokálnaCesta; }

	/** <p><a class="alias"></a> Alias pre {@link #lokálnaCesta() lokálnaCesta}.</p> */
	public String lokalnaCesta() { return lokálnaCesta; }


	/**
	 * <p>Vráti údaje súvisiace s poslednou správou (požiadavkou poslanou
	 * na server) vo forme poľa reťazcov. Volanie tejto metódy má zmysel
	 * až po ukončení spojenia metódou {@link #zavri() zavri}.</p>
	 * 
	 * <p>Prvé dva prvky vrátené v poli obsahujú cieľovú adresu URL
	 * rozdelenú na dve časti. Prvá časť je zostavená podľa konfigurácie
	 * tohto spojenia – {@linkplain #protokol(String) protokolu},
	 * {@linkplain #doména(String) domény}, {@linkplain #port(int) portu}
	 * a {@linkplain #vzdialenáCesta(String) vzdialenej cesty}. Druhá časť
	 * je vytvorená z parametra alebo parametrov niektorého z klonov metód
	 * {@link #otvor(String) otvor} ({@code vzdialenýSúbor} a pod.).</p>
	 * 
	 * <p> Ostatné prvky poľa sú tvorené údajmi hlavičky odoslanej správy.
	 * Táto metóda vráti len niektoré (HTTP) hlavičky poslednej požiadavky
	 * odoslanej na server a to tie, ktorých požiadavka na nastavenie bola
	 * aktívna v čase zahájenia posielania údajov na server. To znamená,
	 * že na server mohli byť poslané aj iné údaje (hlavičky), ktoré
	 * inštancia spojenia vytvorila podľa uváženia.</p>
	 * 
	 * <p>Ak sa spojenie zlyhalo alebo vznikla iná chyba, tak metóda vráti
	 * hodnotu {@code valnull}.</p>
	 * 
	 * @return údaje súvisiace s poslednou komunikáciou so serverom vo forme
	 *     poľa reťazcov alebo {@code valnull}
	 * 
	 * @see #dajÚdajeOdpovede()
	 * @see #zavri()
	 */
	public String[] dajÚdajePožiadavky()
	{
		if (null == údajePožiadavky) return null;

		Vector<String> zoznam = new Vector<>();

		for (Map.Entry<String, List<String>> údaj : údajePožiadavky.entrySet())
		{
			String kľúč = údaj.getKey();
			if (null == kľúč)
			{
				for (String hodnota : údaj.getValue())
					if (null != hodnota)
						zoznam.insertElementAt(hodnota, 0);
			}
			else
			{
				for (String hodnota : údaj.getValue())
					if (null == hodnota)
						zoznam.add(kľúč);
					else
						zoznam.add(kľúč + ": " + hodnota);
			}
		}

		zoznam.insertElementAt(vzdialenýSúbor, 0);
		zoznam.insertElementAt(koreň, 0);

		return zoznam.toArray(new String[zoznam.size()]);
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajÚdajePožiadavky() dajÚdajePožiadavky}.</p> */
	public String[] dajUdajePoziadavky() { return dajÚdajePožiadavky(); }


	/**
	 * <p>Overí, či bola alebo nebola prečítaná odpoveď servera. Túto
	 * skutočnosť má zmysel overovať až po ukončení spojenia metódou
	 * {@link #zavri() zavri}, pričom uvedená metóda vracia rovnakú
	 * hodnotu ako táto metóda.</p>
	 * 
	 * @return pravdivostná hodnota vyjadrujúca to, či bola odpoveď servera
	 *     korektne prečítaná alebo nie
	 * 
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public boolean odpoveďPrečítaná() { return odpoveďPrečítaná; }

	/** <p><a class="alias"></a> Alias pre {@link #odpoveďPrečítaná() odpoveďPrečítaná}.</p> */
	public boolean odpovedPrecitana() { return odpoveďPrečítaná; }

	/**
	 * <p>Vráti typ obsahu odpovede servera. Volanie tejto metódy má zmysel
	 * až po ukončení spojenia metódou {@link #zavri() zavri}. Ak je typ
	 * obsahu neznámy, tak metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return typ obsahu odpovede servera alebo {@code valnull}, ak je
	 *     neznámy
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public String dajTypObsahuOdpovede() { return typObsahu; }

	/**
	 * <p>Vráti veľkosť poslednej odpovede servera v bajtoch. Volanie tejto
	 * metódy má zmysel až po ukončení spojenia metódou {@link #zavri()
	 * zavri}. Ak sa odpoveď nepodarilo získať (alebo vznikla chyba), tak
	 * metóda vráti hodnotu {@code num-1L}.</p>
	 * 
	 * @return veľkosť odpovede servera alebo −1
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public long dajVeľkosťOdpovede() { return veľkosťOdpovede; }

	/** <p><a class="alias"></a> Alias pre {@link #dajVeľkosťOdpovede() dajVeľkosťOdpovede}.</p> */
	public long dajVelkostOdpovede() { return veľkosťOdpovede; }

	/**
	 * <p>Vráti údaje súvisiace s odpoveďou servera vo forme poľa reťazcov.
	 * Volanie tejto metódy má zmysel až po ukončení spojenia metódou
	 * {@link #zavri() zavri}.</p>
	 * 
	 * <p>Táto metóda v podstate vráti všetky HTTP hlavičky odpovede
	 * servera.</p>
	 * 
	 * <p>Ak sa odpoveď servera nepodarilo získať, tak metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return všetky údaje súvisiace s odpoveďou servera vo forme poľa
	 *     reťazcov alebo {@code valnull}
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #dajÚdajePožiadavky()
	 * @see #zavri()
	 */
	public String[] dajÚdajeOdpovede()
	{
		if (null == údajeOdpovede) return null;

		Vector<String> zoznam = new Vector<>();

		for (Map.Entry<String, List<String>> údaj : údajeOdpovede.entrySet())
		{
			String kľúč = údaj.getKey();
			if (null == kľúč)
			{
				for (String hodnota : údaj.getValue())
					if (null != hodnota)
						zoznam.insertElementAt(hodnota, 0);
			}
			else
			{
				for (String hodnota : údaj.getValue())
					if (null == hodnota)
						zoznam.add(kľúč);
					else
						zoznam.add(kľúč + ": " + hodnota);
			}
		}

		return zoznam.toArray(new String[zoznam.size()]);
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajÚdajeOdpovede() dajÚdajeOdpovede}.</p> */
	public String[] dajUdajeOdpovede() { return dajÚdajeOdpovede(); }


	/**
	 * <p>Vráti odpoveď servera v textovej forme. Volanie tejto metódy má
	 * zmysel až po ukončení spojenia metódou {@link #zavri() zavri} a len
	 * v prípade, že máme istotu, že odpoveď servera je v textovej forme.
	 * Typ obsahu odpovede je overiteľný metódou {@link 
	 * #dajTypObsahuOdpovede() dajTypObsahuOdpovede} – textové typy obsahu
	 * sú napríklad {@code text/plain} {@code text/html} a podobne. Ak
	 * odpoveď servera nie je textová, tak táto metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return odpoveď servera (v textovej forme) alebo {@code valnull}
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public String dajOdpoveď() { return odpoveď; }

	/** <p><a class="alias"></a> Alias pre {@link #dajOdpoveď() dajOdpoveď}.</p> */
	public String dajOdpoved() { return odpoveď; }

	/**
	 * <p>Vráti kód poslednej odpovede servera. Volanie tejto metódy má
	 * zmysel až po ukončení spojenia metódou {@link #zavri() zavri}.
	 * Ak sa kód odpovede servera nepodarilo zistiť, tak metóda vráti
	 * hodnotu {@code num-1}. Pozitívnym signálom toho, že komunikácia
	 * so serverom bola v poriadku je kód {@code num200} –
	 * {@link HttpURLConnection#HTTP_OK HTTP_OK}. Ďalšie kódy s podrobným
	 * opisom v anglickom jazyku môžete nájsť napríklad tu: <a
	 * href="https://tools.ietf.org/html/rfc2616#section-10"
	 * target="_blank">RFC 2616 – Hypertext Transfer Protocol –
	 * HTTP/1.1 – Section 10: Status Code Definitions</a> (súhrnný zoznam
	 * kódov sa nachádza v <a
	 * href="https://tools.ietf.org/html/rfc2616#section-6.1.1"
	 * target="_blank">sekcii 6.1.1</a> uvedeného dokumentu; citované: 2018).</p>
	 * 
	 * @return HTTP kód poslednej odpovede servera alebo −1
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public int dajKódOdpovede() { return kódOdpovede; }

	/** <p><a class="alias"></a> Alias pre {@link #dajKódOdpovede() dajKódOdpovede}.</p> */
	public int dajKodOdpovede() { return kódOdpovede; }

	/**
	 * <p>Vráti odpoveď servera vo forme poľa bajtov. Volanie tejto metódy
	 * má zmysel až po ukončení spojenia metódou {@link #zavri() zavri}.
	 * Ak bol typ odpovede textový, tak táto metóda vráti v poli bajtov
	 * bajty reťazca s použitím kódovania podľa typu obsahu odpovede
	 * (ktorý je overiteľný metódou {@link #dajTypObsahuOdpovede()
	 * dajTypObsahuOdpovede}) alebo UTF-8 (ak sa kódovanie nepodarilo
	 * zistiť). Ak sa odpoveď servera nepodarilo získať (alebo bola priamo
	 * uložená do súboru), tak táto metóda vráti hodnotu {@code valnull}.
	 * Ak chcete pri textovej odpovedi typ použitého kódovania vynútiť,
	 * použite {@linkplain #dajBajtyOdpovede(String) verziu tejto metódy}
	 * s parametrom {@code kódovanie}.</p>
	 * 
	 * @return odpoveď servera vo forme poľa bajtov alebo {@code valnull}
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public byte[] dajBajtyOdpovede()
	{
		if (null != bajtyOdpovede) return bajtyOdpovede;
		if (null != odpoveď)
		{
			String kódovanie = "UTF-8";

			if (null != typObsahu)
			{
				String typMalým = typObsahu.trim().toLowerCase();
				if (typMalým.startsWith("text/"))
				{
					if (-1 == typMalým.indexOf("utf-8"))
					{
						int indexOf = typMalým.indexOf("charset");
						if (-1 != indexOf)
						{
							StringBuffer charset = new StringBuffer(
								typObsahu.trim().substring(indexOf + 7));

							while (charset.length() > 0 &&
								(' ' == charset.charAt(0) ||
									'=' == charset.charAt(0)))
								charset.deleteCharAt(0);

							kódovanie = charset.toString();
						}
					}
				}
			}

			try
			{
				return odpoveď.getBytes(kódovanie);
			}
			catch (UnsupportedEncodingException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		return null;
	}

	/**
	 * <p>Vráti odpoveď servera vo forme poľa bajtov. Volanie tejto metódy
	 * má zmysel až po ukončení spojenia metódou {@link #zavri() zavri}.
	 * Ak bol typ odpovede textový, tak táto metóda vráti v poli bajtov
	 * bajty reťazca zakódované s použitím zadaného kódovania. (Typ obsahu
	 * odpovede je overiteľný metódou {@link #dajTypObsahuOdpovede()
	 * dajTypObsahuOdpovede}.) Ak sa odpoveď servera nepodarilo získať
	 * (alebo bola priamo uložená do súboru), tak táto metóda vráti hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return odpoveď servera vo forme poľa bajtov alebo {@code valnull}
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajNázovSúboruOdpovede()
	 * 
	 * @see #zavri()
	 */
	public byte[] dajBajtyOdpovede(String kódovanie)
	{
		if (null != bajtyOdpovede) return bajtyOdpovede;
		if (null != odpoveď)
			try
			{
				return odpoveď.getBytes(kódovanie);
			}
			catch (UnsupportedEncodingException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		return null;
	}


	// Extrahuje názov súboru uzavretý v úvodzovkách.
	private static void extrahujNázov(StringBuffer extrahuj)
	{
		while (extrahuj.length() > 0 &&
			(' ' == extrahuj.charAt(0) ||
				'=' == extrahuj.charAt(0)))
			extrahuj.deleteCharAt(0);

		if (0 < extrahuj.length())
		{
			if ('"' == extrahuj.charAt(0))
			{
				extrahuj.deleteCharAt(0);
				for (int i = 0; i < extrahuj.length(); ++i)
				{
					if ('"' == extrahuj.charAt(i))
					{
						extrahuj.setLength(i);
						break;
					}
					else if ('\\' == extrahuj.charAt(i))
					{
						extrahuj.deleteCharAt(i);
						if (i < extrahuj.length())
							switch (extrahuj.charAt(i))
							{
							case 't': extrahuj.setCharAt(i, '\t'); break;
							case 'b': extrahuj.setCharAt(i, '\b'); break;
							case 'n': extrahuj.setCharAt(i, '\n'); break;
							case 'r': extrahuj.setCharAt(i, '\r'); break;
							case 'f': extrahuj.setCharAt(i, '\f'); break;
							}
					}
				}
			}
			else
			{
				int indexOf = extrahuj.indexOf(";");
				if (-1 != indexOf) extrahuj.setLength(indexOf);
			}
		}
	}

	// Mapa MIME typov.
	private final static TreeMap<String, String> mimeMapa = new TreeMap<>();

	// Metóda slúžiaca na naplnenie MIME mapy.
	private static void naplňMIMEMapu()
	{
		mimeMapa.put("application/envoy", "evy");
		mimeMapa.put("application/fractals", "fif");
		mimeMapa.put("application/futuresplash", "spl");
		mimeMapa.put("application/hta", "hta");
		mimeMapa.put("application/internet-property-stream", "acx");
		mimeMapa.put("application/mac-binhex40", "hqx");
		mimeMapa.put("application/msword", "docx");
		mimeMapa.put("application/octet-stream", "bin");
		mimeMapa.put("application/oda", "oda");
		mimeMapa.put("application/olescript", "axs");
		mimeMapa.put("application/pdf", "pdf");
		mimeMapa.put("application/pics-rules", "prf");
		mimeMapa.put("application/pkcs10", "p10");
		mimeMapa.put("application/pkix-crl", "crl");
		mimeMapa.put("application/postscript", "eps");
		mimeMapa.put("application/rtf", "rtf");
		mimeMapa.put("application/set-payment-initiation", "setpay");
		mimeMapa.put("application/set-registration-initiation", "setreg");
		mimeMapa.put("application/vnd.ms-excel", "xlsx");
		mimeMapa.put("application/vnd.ms-pkicertstore", "sst");
		mimeMapa.put("application/vnd.ms-pkiseccat", "cat");
		mimeMapa.put("application/vnd.ms-pkistl", "stl");
		mimeMapa.put("application/vnd.ms-powerpoint", "pptx");
		mimeMapa.put("application/vnd.ms-project", "mpp");
		mimeMapa.put("application/vnd.ms-works", "wks");
		mimeMapa.put("application/vnd.wap.wbxml", "wbxml");
		mimeMapa.put("application/vnd.wap.wmlc", "wmlc");
		mimeMapa.put("application/vnd.wap.wmlscriptc", "wmlsc");
		mimeMapa.put("application/winhlp", "hlp");
		mimeMapa.put("application/x-bcpio", "bcpio");
		mimeMapa.put("application/x-cdf", "cdf");
		mimeMapa.put("application/x-compress", "z");
		mimeMapa.put("application/x-compressed", "tgz");
		mimeMapa.put("application/x-cpio", "cpio");
		mimeMapa.put("application/x-csh", "csh");
		mimeMapa.put("application/x-director", "dir");
		mimeMapa.put("application/x-dvi", "dvi");
		mimeMapa.put("application/x-gtar", "gtar");
		mimeMapa.put("application/x-gzip", "gz");
		mimeMapa.put("application/x-hdf", "hdf");
		mimeMapa.put("application/xhtml+xml", "xhtml");
		mimeMapa.put("application/x-internet-signup", "isp");
		mimeMapa.put("application/x-iphone", "iii");
		mimeMapa.put("application/x-javascript", "js");
		mimeMapa.put("application/x-latex", "latex");
		mimeMapa.put("application/x-msaccess", "mdb");
		mimeMapa.put("application/x-mscardfile", "crd");
		mimeMapa.put("application/x-msclip", "clp");
		mimeMapa.put("application/x-msdownload", "dll");
		mimeMapa.put("application/x-msmediaview", "mvb");
		mimeMapa.put("application/x-msmetafile", "wmf");
		mimeMapa.put("application/x-msmoney", "mny");
		mimeMapa.put("application/x-mspublisher", "pub");
		mimeMapa.put("application/x-msschedule", "scd");
		mimeMapa.put("application/x-msterminal", "trm");
		mimeMapa.put("application/x-mswrite", "wri");
		mimeMapa.put("application/x-perfmon", "pmr");
		mimeMapa.put("application/x-pkcs12", "p12");
		mimeMapa.put("application/x-pkcs7-certificates", "p7b");
		mimeMapa.put("application/x-pkcs7-certreqresp", "p7r");
		mimeMapa.put("application/x-pkcs7-mime", "p7c");
		mimeMapa.put("application/x-pkcs7-signature", "p7s");
		mimeMapa.put("application/x-sh", "sh");
		mimeMapa.put("application/x-shar", "shar");
		mimeMapa.put("application/x-shockwave-flash", "swf");
		mimeMapa.put("application/x-sql", "sql");
		mimeMapa.put("application/x-stuffit", "sit");
		mimeMapa.put("application/x-sv4cpio", "sv4cpio");
		mimeMapa.put("application/x-sv4crc", "sv4crc");
		mimeMapa.put("application/x-tar", "tar");
		mimeMapa.put("application/x-tcl", "tcl");
		mimeMapa.put("application/x-tex", "tex");
		mimeMapa.put("application/x-texinfo", "texinfo");
		mimeMapa.put("application/x-troff", "roff");
		mimeMapa.put("application/x-troff-man", "man");
		mimeMapa.put("application/x-troff-me", "me");
		mimeMapa.put("application/x-troff-ms", "ms");
		mimeMapa.put("application/x-ustar", "ustar");
		mimeMapa.put("application/x-wais-source", "src");
		mimeMapa.put("application/x-x509-ca-cert", "cer");
		mimeMapa.put("application/ynd.ms-pkipko", "pko");
		mimeMapa.put("application/zip", "zip");
		mimeMapa.put("audio/basic", "au");
		mimeMapa.put("audio/mid", "rmi");
		mimeMapa.put("audio/midi", "mid");
		mimeMapa.put("audio/mpeg", "mp3");
		mimeMapa.put("audio/x-aiff", "aiff");
		mimeMapa.put("audio/x-mpegurl", "m3u");
		mimeMapa.put("audio/x-pn-realaudio", "rm");
		mimeMapa.put("audio/x-pn-realaudio-plugin", "rpm");
		mimeMapa.put("audio/x-realaudio", "ra");
		mimeMapa.put("audio/x-wav", "wav");
		mimeMapa.put("image/bmp", "bmp");
		mimeMapa.put("image/cis-cod", "cod");
		mimeMapa.put("image/gif", "gif");
		mimeMapa.put("image/icon", "ico");
		mimeMapa.put("image/ief", "ief");
		mimeMapa.put("image/jpeg", "jpeg");
		mimeMapa.put("image/pipeg", "jfif");
		mimeMapa.put("image/png", "png");
		mimeMapa.put("image/svg+xml", "svg");
		mimeMapa.put("image/tiff", "tiff");
		mimeMapa.put("image/x-cmu-raster", "ras");
		mimeMapa.put("image/x-cmx", "cmx");
		mimeMapa.put("image/x-portable-anymap", "pnm");
		mimeMapa.put("image/x-portable-bitmap", "pbm");
		mimeMapa.put("image/x-portable-graymap", "pgm");
		mimeMapa.put("image/x-portable-pixmap", "ppm");
		mimeMapa.put("image/x-rgb", "rgb");
		mimeMapa.put("image/x-xbitmap", "xbm");
		mimeMapa.put("image/x-xpixmap", "xpm");
		mimeMapa.put("image/x-xwindowdump", "xwd");
		mimeMapa.put("message/rfc822", "mhtml");
		mimeMapa.put("text/css", "css");
		mimeMapa.put("text/h323", "323");
		mimeMapa.put("text/html", "html");
		mimeMapa.put("text/iuls", "uls");
		mimeMapa.put("text/plain", "txt");
		mimeMapa.put("text/richtext", "rtx");
		mimeMapa.put("text/scriptlet", "sct");
		mimeMapa.put("text/tab-separated-values", "tsv");
		mimeMapa.put("text/vnd.wap.wml", "wml");
		mimeMapa.put("text/vnd.wap.wmlscript", "wmls");
		mimeMapa.put("text/webviewhtml", "htt");
		mimeMapa.put("text/x-component", "htc");
		mimeMapa.put("text/xml", "xml");
		mimeMapa.put("text/x-setext", "etx");
		mimeMapa.put("text/x-vcard", "vcf");
		mimeMapa.put("video/mpeg", "mpeg");
		mimeMapa.put("video/quicktime", "mov");
		mimeMapa.put("video/x-la-asf", "lsx");
		mimeMapa.put("video/x-ms-asf", "asf");
		mimeMapa.put("video/x-msvideo", "avi");
		mimeMapa.put("video/x-ms-wmv", "wmv");
		mimeMapa.put("video/x-sgi-movie", "movie");
		mimeMapa.put("x-world/x-vrml", "vrml");
	}

	/**
	 * <p>Zistí názov súboru odpovede. Volanie tejto metódy má zmysel až po
	 * ukončení spojenia metódou {@link #zavri() zavri}. Ak nie je odpoveď
	 * textová, pokúsi sa názov prevziať z údajov prijatých v odpovedi. Ak
	 * zlyhá, prevezme názov z požiadavky odoslanej na server (z parametra
	 * {@code vzdialenýSúbor} metódy {@link #otvor(String) otvor} alebo jej
	 * klonu). V tom prípade overí {@linkplain  #dajTypObsahuOdpovede()
	 * typu obsahu odpovede} a ak je {@code text/html}, upraví príponu na
	 * {@code .html}.</p>
	 * 
	 * @see #odpoveďPrečítaná()
	 * @see #dajTypObsahuOdpovede()
	 * @see #dajVeľkosťOdpovede()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajOdpoveď()
	 * @see #dajKódOdpovede()
	 * @see #dajBajtyOdpovede()
	 * @see #dajBajtyOdpovede(String)
	 * 
	 * @see #uložOdpoveď()
	 * @see #uložOdpoveď(String)
	 * 
	 * @see #zavri()
	 */
	public String dajNázovSúboruOdpovede()
	{
		if (null == spojenie) return null;

		String názovSúboru = null;

		try
		{
			názovSúboru = spojenie.
				getHeaderField("Content-Disposition");
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		if (názovSúboru != null)
		{
			int indexOf1 = názovSúboru.indexOf("filename=");
			int indexOf2 = názovSúboru.indexOf("filename*");

			StringBuffer extrahuj1 = new StringBuffer();
			StringBuffer extrahuj2 = new StringBuffer();

			if (-1 != indexOf1)
			{
				extrahuj1.append(názovSúboru.substring(indexOf1 + 9));
				extrahujNázov(extrahuj1);
			}
			else
			{
				indexOf1 = názovSúboru.indexOf("filename ");
				if (-1 != indexOf1)
				{
					extrahuj1.append(názovSúboru.substring(indexOf1 + 9));
					extrahujNázov(extrahuj1);
				}
			}

			if (-1 != indexOf2)
			{
				extrahuj2.append(názovSúboru.substring(indexOf2 + 9));
				extrahujNázov(extrahuj2);
			}

			if (0 != extrahuj2.length())
				názovSúboru = extrahuj2.toString();
			else if (0 != extrahuj1.length())
				názovSúboru = extrahuj1.toString();
			else
				názovSúboru = null;

			if (null != názovSúboru)
			{
				try
				{
					názovSúboru = URLDecoder.
						decode(názovSúboru, "UTF-8");
				}
				catch (IllegalArgumentException |
					UnsupportedEncodingException e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}

				indexOf1 = názovSúboru.lastIndexOf('\\');
				indexOf2 = názovSúboru.lastIndexOf('/');

				if (-1 != indexOf1 || -1 != indexOf2)
				{
					názovSúboru = názovSúboru.substring(
						1 + Math.max(indexOf1, indexOf2));
				}
			}
		}

		if (null == názovSúboru)
		{
			if (null == vzdialenýSúbor) názovSúboru = "";
			else názovSúboru = vzdialenýSúbor;

			int indexOf1 = názovSúboru.lastIndexOf('\\');
			int indexOf2 = názovSúboru.lastIndexOf('/');

			if (-1 != indexOf1 || -1 != indexOf2)
			{
				názovSúboru = názovSúboru.substring(
					1 + Math.max(indexOf1, indexOf2));
			}

			indexOf1 = názovSúboru.indexOf('?');
			indexOf2 = názovSúboru.indexOf('#');

			if (-1 != indexOf1 && -1 != indexOf2)
			{
				názovSúboru = názovSúboru.substring(
					0, Math.min(indexOf1, indexOf2));
			}
			else if (-1 != indexOf1)
			{
				názovSúboru = názovSúboru.
					substring(0, indexOf1);
			}
			else if (-1 != indexOf2)
			{
				názovSúboru = názovSúboru.
					substring(0, indexOf2);
			}

			try
			{
				názovSúboru = URLDecoder.
					decode(názovSúboru, "UTF-8");
			}
			catch (IllegalArgumentException |
				UnsupportedEncodingException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}

			if (null != typObsahu)
			{
				String typMalým;

				int indexOf = typObsahu.indexOf(';');
				if (-1 == indexOf)
					typMalým = typObsahu.trim().toLowerCase();
				else
					typMalým = typObsahu.substring(0, indexOf).
						trim().toLowerCase();

				// System.out.println(typMalým);

				if (názovSúboru.isEmpty())
				{
					if (mimeMapa.isEmpty()) naplňMIMEMapu();

					String prípona = mimeMapa.get(typMalým);
					if (null == prípona || prípona.equals("html"))
						názovSúboru = "index.html";
					else
						názovSúboru = "default." + prípona;
				}

				if (typMalým.equals("text/html") &&
					!názovSúboru.endsWith(".html"))
				{
					indexOf = názovSúboru.lastIndexOf('.');
					if (-1 == indexOf)
						názovSúboru += ".html";
					else
						názovSúboru = názovSúboru.
							substring(0, indexOf) + ".html";
				}
			}
			else if (názovSúboru.isEmpty())
			{
				názovSúboru = "index.html";
			}
		}

		return názovSúboru;
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajNázovSúboruOdpovede() dajNázovSúboruOdpovede}.</p> */
	public String dajNazovSuboruOdpovede()
	{ return dajNázovSúboruOdpovede(); }

	/**
	 * <p>Uloží odpoveď servera do súboru. Názov súboru prevezme s použitím
	 * metódy {@link #dajNázovSúboruOdpovede() dajNázovSúboruOdpovede}.
	 * Súbor bude uložený na aktuálnu {@linkplain #lokálnaCesta(String)
	 * lokálnu cestu}. Ak súbor s názvom na uloženie v cieľovom umiestnení
	 * jestvuje, tak metóda upraví názov tak, aby nebol prepísaný žiadny
	 * súbor. Metóda sa správa rozdielne podľa toho, či bola volaná pred
	 * alebo po ukončení spojenia metódou {@link #zavri() zavri}. Ďalšie
	 * podrobnosti (nielen o uvedenom rozdiele) sú v opise metódy {@link 
	 * #uložOdpoveď(String) uložOdpoveď}.</p>
	 * 
	 * @return názov súboru, pod ktorým bola odpoveď uložená alebo
	 *     {@code valnull} v prípade zlyhania
	 * 
	 * @see #dajNázovSúboruOdpovede()
	 * @see #uložOdpoveď(String)
	 * 
	 * @see #zavri()
	 */
	public String uložOdpoveď()
	{
		String názovSúboru = dajNázovSúboruOdpovede();
		if (null != názovSúboru) return uložOdpoveď(názovSúboru);
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložOdpoveď() uložOdpoveď}.</p> */
	public String ulozOdpoved() { return uložOdpoveď(); }

	/**
	 * <p>Uloží odpoveď servera do súboru so zadaným názvom. Ak je táto
	 * metóda volaná po ukončení spojenia metódou {@link #zavri() zavri},
	 * tak ukladá tie údaje, ktoré boli vopred prečítané a ktoré sú zároveň
	 * dostupné prostredníctvom metódy {@link #dajÚdajeOdpovede()
	 * dajÚdajeOdpovede}. V opačnom prípade začne údaje čítať a priebežne
	 * ukladať sama, ale pri tomto spôsobe ich neukladá do vnútornej pamäte
	 * a nebudú po ukončení spojenia dostupné metódou {@link 
	 * #dajÚdajeOdpovede() dajÚdajeOdpovede}. To je výhodné pri preberaní
	 * väčších súborov, kedy by inak mohla nastať chyba v dôsledku preplnenia
	 * pamäte. V oboch prípadoch bude súbor uložený na aktuálnu {@linkplain 
	 * #lokálnaCesta(String) lokálnu cestu}. Ak súbor s názvom na uloženie
	 * v cieľovom umiestnení jestvuje, tak metóda upraví názov tak, aby nebol
	 * prepísaný žiadny súbor.</p>
	 * 
	 * <p>Metóda rozlišuje ešte jeden špeciálny prípad. Ak je volaná pred
	 * ukončením spojenia (pretože len v tom prípade ešte neboli na server
	 * odoslané údaje o požiadavke), tak ak pred začatím ukladania nájde
	 * súbor zodpovedajúci šablóne názvu súboru s pripojeným reťazcom
	 * {@code -part}, tak tento súbor bude považovať za časť preberaného
	 * súboru, ktorého preberanie predtým zlyhalo. Vtedy zo servera vyžiada
	 * zvyšnú časť súboru a prijímané údaje pripojí k nájdenému súboru.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Obnovenie zlyhaného
	 * preberania funguje len v tom prípade, ak server podporuje odosielanie
	 * rozsahov bajtov (čiže častí súborov, ktoré si klient vyžiadal). Ak
	 * server nie je nakonfigurovaný tak, aby bol schopný odoslať len
	 * vyžiadaný rozsah bajtov (prípadne táto voľba nie je podporovaná pre
	 * vyžiadaný typ súboru), tak obnovenie preberania nebude možné, čo
	 * metóda signalizuje vrátením hodnoty {@code valnull}.</p>
	 * 
	 * <p>V prípade okamžitého preberania údajov bude metóda spúšťať reakciu
	 * {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long, long)
	 * sekvencia} (s prislúchajúcim {@linkplain GRobot#sekvencia(int,
	 * Object, Object, long, long) variantom pre jednotlivé roboty})
	 * s kódom spracovania {@link Konštanty#PREVZATIE_ÚDAJOV
	 * PREVZATIE_ÚDAJOV}.</p>
	 * 
	 * @param názovSúboru názov súboru, pod ktorým by mala byť odpoveď
	 *     uložená
	 * @return názov súboru, pod ktorým bola odpoveď uložená alebo
	 *     {@code valnull} v prípade zlyhania
	 * 
	 * @see #dajNázovSúboruOdpovede()
	 * @see #uložOdpoveď()
	 * 
	 * @throws GRobotException ak bol názov súboru zamlčaný (bola zadaná
	 *     hodnota {@code valnull})
	 * 
	 * @see #zavri()
	 */
	public String uložOdpoveď(String názovSúboru)
	{
		if (null == názovSúboru)
			throw new GRobotException(
				"Názov súboru nesmie byť zamlčaný.",
				"fileNameOmitted", null, new NullPointerException());

		boolean obnoviťPrevzatie = false;
		sekvenciaTransferuZdroj = koreň + vzdialenýSúbor;
		sekvenciaTransferuCieľ = lokálnaCesta + názovSúboru;
		sekvenciaTransferuStav = 0;
		sekvenciaTransferuCelkovo = -1;

		try
		{
			if (!hlavičkaPoslaná && new File(lokálnaCesta +
				názovSúboru + "-part").exists() && !protokol.equals("file"))
			{
				obnoviťPrevzatie = true;
			}
			else if (new File(lokálnaCesta + názovSúboru).exists())
			{
				String meno, prípona;
				int indexOf = názovSúboru.lastIndexOf('.');
				if (-1 == indexOf)
				{
					meno = názovSúboru;
					prípona = "";
				}
				else
				{
					meno = názovSúboru.substring(0, indexOf);
					prípona = názovSúboru.substring(indexOf);
				}

				for (int i = 1; i < 10000; ++i)
				{
					názovSúboru = meno + " (" + i + ")" + prípona;
					if (!hlavičkaPoslaná && new File(lokálnaCesta +
						názovSúboru + "-part").exists() &&
						!protokol.equals("file"))
					{
						obnoviťPrevzatie = true;
						break;
					}
					else if (new File(lokálnaCesta + názovSúboru).exists())
						názovSúboru = null; else break;
				}
			}
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
			return null;
		}

		if (null != názovSúboru)
		{
			sekvenciaTransferuCieľ = lokálnaCesta + názovSúboru;

			if (obnoviťPrevzatie)
			{
				long začiatok = new File(lokálnaCesta +
					názovSúboru + "-part").length();

				spojenie.setRequestProperty("Range",
					"bytes=" + začiatok + "-");

				sekvenciaTransferuStav = začiatok;
				sekvenciaTransferuCelkovo = začiatok;
				sekvenciaTransferuÚdajov(PREVZATIE_ÚDAJOV);

				if (!chvostPoslaný && !pošliChvost()) return null;

				String acceptRanges = spojenie.
					getHeaderField("Accept-Ranges");
				if (null == acceptRanges ||
					!acceptRanges.equalsIgnoreCase("bytes"))
					return null;
			}

			try
			{
				FileOutputStream zápis;

				if (obnoviťPrevzatie)
					zápis = new FileOutputStream(new File(lokálnaCesta +
						názovSúboru + "-part"), true);
				else
					zápis = new FileOutputStream(new File(lokálnaCesta +
						názovSúboru + "-part"));

				if (null != bajtyOdpovede)
				{
					zápis.write(bajtyOdpovede);
				}
				else if (null != odpoveď)
				{
					byte[] bajtyOdpovede = null;

					try { bajtyOdpovede = odpoveď.getBytes("UTF-8"); }
					catch (UnsupportedEncodingException e)
					{ GRobotException.vypíšChybovéHlásenia(e); }

					if (null != bajtyOdpovede)
						zápis.write(bajtyOdpovede);
				}
				else
				{
					if (!čítajOdpoveď(zápis))
					{
						zápis.close();
						return null;
					}
				}

				zápis.close();
				new File(lokálnaCesta + názovSúboru + "-part").
					renameTo(new File(lokálnaCesta + názovSúboru));

				if (obnoviťPrevzatie && HttpURLConnection.
					HTTP_PARTIAL != kódOdpovede)
				{
					GRobotException.vypíšChybovéHlásenie(
						"Varovanie! Obnovené preberanie nebolo ukončené " +
						"očakávaným spôsobom. HTTP kód odpovede: " +
						kódOdpovede);
				}

				return názovSúboru;
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložOdpoveď(String) uložOdpoveď}.</p> */
	public String ulozOdpoved(String názovSúboru)
	{ return uložOdpoveď(názovSúboru); }


	/**
	 * <p>Poskytne objekt {@link URLConnection URLConnection} aktuálnej
	 * správy. Volaním niektorého klonu metódy {@link #otvor(String) otvor}
	 * vzniká vždy nové spojenie – objekt {@link URLConnection
	 * URLConnection}, ktorý je k dispozícii aj po volaní metódy
	 * {@link #zavri() zavri}, aby z neho bolo možné prevziať i také údaje,
	 * ktoré trieda {@link Spojenie Spojenie} nespracúva. Rovnako je možné
	 * prostredníctvom tohto objektu nastavovať v čase medzi {@linkplain 
	 * #otvor(String) otvorením} a {@linkplain #zavri() zatvorením} spojenia
	 * také údaje, s ktorými tiež trieda {@link Spojenie Spojenie}
	 * nepracuje. Ak doteraz nebolo vytvorené žiadne spojenie, tak metóda
	 * vráti hodnotu {@code valnull}.</p>
	 * 
	 * @return inštancia triedy {@link URLConnection URLConnection} alebo
	 *     hodnota {@code valnull}
	 * 
	 * @see #otvor(String)
	 * @see #otvor(String, String, String, String...)
	 * @see #zavri()
	 */
	public URLConnection dajSpojenie() { return spojenie; }


	/**
	 * <p>Otvorí novú komunikáciu (vytvorí novú správu) so serverom určeným
	 * konfiguráciou tejto inštancie ({@linkplain #protokol(String)
	 * protokolom}, {@linkplain #doména(String) doménou}, {@linkplain 
	 * #port(int) portom}, {@linkplain #vzdialenáCesta(String) vzdialenou
	 * cestou}) a parametrom {@code vzdialenýSúbor} tejto metódy.</p>
	 * 
	 * <!-- TODO príklad použitia -->
	 * 
	 * @param vzdialenýSúbor názov vzdialeného súboru (skriptu), ktorý má
	 *     byť prevzatý zo servera (ak ide o názov skriptu, tak tento bude
	 *     vykonaný na strane servera, ktorý odošle výsledok vykonania ako
	 *     odpoveď)
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #otvor(String, String, String, String...)
	 * @see #otvorLokalitu(String)
	 * @see #hlavičkyPoslané()
	 * @see #pridajHlavičky(String...)
	 * @see #pridajÚdaj(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #zavri()
	 * @see #dajSpojenie()
	 */
	public boolean otvor(String vzdialenýSúbor)
	{
		try
		{
			// Vyprázdnenie prípadných starých prúdov (pre istotu):
			if (null != výstup)
			{
				try { výstup.close(); } catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
				výstup = null;
			}

			if (null != vstup)
			{
				try { vstup.close(); } catch (Exception e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
				vstup = null;
			}

			// Otvorenie nového spojenia:
			this.vzdialenýSúbor = vzdialenýSúbor;
			url = new URL(koreň + vzdialenýSúbor);
			spojenie = url.openConnection();
			spojenie.setDoOutput(true);

			// Reset súvisiacich vlastností:
			hlavičkaPoslaná = false;
			chvostPoslaný = false;
			odpoveďPrečítaná = false;
			veľkosťPrílohy = 0L;
			typObsahu = null;
			veľkosťOdpovede = -1L;
			údajePožiadavky = null;
			údajeOdpovede = null;
			odpoveď = null;
			kódOdpovede = -1;
			bajtyOdpovede = null;

			// Inicializácia ďalších (dôležitých) súvisiacich údajov:
			hranica = "--" + identifikátorÚdajov + "--" + random.nextInt();
			hlavičkaObsahuSprávy = "";

			chvostObsahuSprávy = "";
			chvostObsahuSprávy += EOL + "--" + hranica + "--" + EOL;

			spojenie.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + hranica);
			// Pokus o vyriešenie problému s spojenie.getInputStream(),
			// ale problém sa tým nevyriešil:
			// spojenie.setRequestProperty("Accept", "*/*");
			// spojenie.setRequestProperty("User-Agent",
			// 	"Mozilla/5.0 (compatible)");


			return true;
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenie(
				"Chyba pri otváraní spojenia…");
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	/**
	 * <p>Otvorí novú komunikáciu (vytvorí novú správu) so serverom určeným
	 * konfiguráciou tejto inštancie ({@linkplain #protokol(String)
	 * protokolom}, {@linkplain #doména(String) doménou}, {@linkplain 
	 * #port(int) portom}, {@linkplain #vzdialenáCesta(String) vzdialenou
	 * cestou}) a parametrami tejto metódy ({@code vzdialenýSúbor},
	 * {@code prvýParameter}, {@code prváHodnota} a nepovinným variabilným
	 * zoznamom ďalších parametrov a hodnôt). Dvojice parametrov a hodnôt
	 * sú k adrese pridávané v tvare {@code parameter=hodnota} prvá dvojica
	 * je od predchádzajúcej adresy oddelená otáznikom, ďalšie sú oddeľované
	 * znakom {@code &amp;}. Ak je ľubovoľná hodnota (vrátane prvej) rovná
	 * {@code valnull}, tak je prislúchajúci parameter pridaný v bezhodnotovom
	 * tvare: {@code parameter}. Špeciálne znaky v názvoch parametrov aj
	 * v ich hodnotách sú kódované podľa pravidiel URL kódovania – pozri <a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/net/URLEncoder.html"
	 * target="_blank"><code>URLEncoder</code> (Java Platform SE 8).</a></p>
	 * 
	 * <!-- TODO príklad použitia -->
	 * 
	 * @param vzdialenýSúbor názov vzdialeného súboru (skriptu), ktorý má
	 *     byť prevzatý zo servera (ak ide o názov skriptu, tak tento bude
	 *     vykonaný na strane servera, ktorý odošle výsledok vykonania ako
	 *     odpoveď)
	 * @param prvýParameter názov prvého parametra, ktorý bude pridaný
	 *     k tejto požiadavke
	 * @param prváHodnota hodnota prvého parametra
	 * @param ďalšieParametreAHodnoty nepovinný zoznam ďalších dvojíc
	 *     názvov a hodnôt parametrov; každý názov musí mať párujúcu hodnotu;
	 *     ak má zoznam nepárny počet prvkov, tak posledný prvok nebude
	 *     použitý (ak aj chcete pridať posledný parameter v bezhodnotovom
	 *     tvare, stále musí byť prítomná posledná párujúca hodnota rovná
	 *     {@code valnull})
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #otvor(String)
	 * @see #otvorLokalitu(String)
	 * @see #hlavičkyPoslané()
	 * @see #pridajHlavičky(String...)
	 * @see #pridajÚdaj(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #dajÚdajePožiadavky()
	 * @see #dajÚdajeOdpovede()
	 * @see #zavri()
	 * @see #dajSpojenie()
	 */
	public boolean otvor(String vzdialenýSúbor,
		String prvýParameter, String prváHodnota,
		String... ďalšieParametreAHodnoty)
	{
		try
		{
			StringBuilder sb = new StringBuilder(vzdialenýSúbor);

			sb.append('?');
			sb.append(URLEncoder.encode(prvýParameter, "UTF-8"));
			if (null != prváHodnota)
			{
				sb.append('=');
				sb.append(URLEncoder.encode(prváHodnota, "UTF-8"));
			}

			boolean semafor = false; String parameter = null;
			for (String hodnota : ďalšieParametreAHodnoty)
			{
				if (semafor)
				{
					sb.append('&');
					sb.append(URLEncoder.encode(parameter, "UTF-8"));
					if (null != hodnota)
					{
						sb.append('=');
						sb.append(URLEncoder.encode(hodnota, "UTF-8"));
					}
				}
				else parameter = hodnota;
				semafor = !semafor;
			}

			return otvor(sb.toString());
		}
		catch (UnsupportedEncodingException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}
		return false;
	}

	/**
	 * <p>Otvorí novú komunikáciu podľa zadanej lokality. Metóda analyzuje
	 * zadanú adresu a na základe výsledku analýzy nastaví {@linkplain 
	 * #protokol(String) protokol}, {@linkplain #doména(String) doménu},
	 * {@linkplain #port(int) komunikačný port} a {@linkplain 
	 * #vzdialenáCesta(String) vzdialenú cestu} (zavolá vnútornú metódu
	 * na ich nastavenie). Potom spustí metódu {@link #otvor(String)
	 * otvor}. Ak bol výsledok volania všetkých metód pozitívny, tak táto
	 * metóda vráti hodnotu {@code valtrue}, inak vráti všetky zmeny
	 * a vráti hodnotu {@code valfalse}. V prípade úspechu môžu byť
	 * prostredníctvom prislúchajúcich metód {@linkplain #pridajÚdaj(String,
	 * String) pridávané ďalšie údaje} a/alebo {@linkplain #pošliSúbor(String,
	 * String, String) odoslaný súbor}. Nakoniec musí byť spojenie korektne
	 * {@linkplain #zavri() ukončené}.</p>
	 * 
	 * <!-- TODO príklad použitia -->
	 * 
	 * @param adresa úplná adresa na analýzu, podľa ktorej sa vykoná
	 *     konfigurácia inštancie a otvorí sa nové spojenie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #protokol(String)
	 * @see #doména(String)
	 * @see #port(int)
	 * @see #vzdialenáCesta(String)
	 * 
	 * @see #otvor(String)
	 * @see #hlavičkyPoslané()
	 * @see #pridajHlavičky(String...)
	 * @see #pridajÚdaj(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #zavri()
	 */
	public boolean otvorLokalitu(String adresa)
	{
		boolean vráťZmeny = true;
		String protokol = this.protokol;
		String doména = this.doména;
		int port = this.port;
		String vzdialenáCesta = this.vzdialenáCesta;
		String koreň = this.koreň;
		String vzdialenýSúbor = this.vzdialenýSúbor;

		try
		{
			StringBuffer sb = new StringBuffer(adresa);
			this.protokol = "http";
			this.doména = "localhost";
			this.port = 80;
			this.vzdialenáCesta = "";
			this.vzdialenýSúbor = "";
			int indexOf;

			// System.out.println("Analyzujem: " + sb);

			indexOf = sb.indexOf("://");
			if (-1 != indexOf)
			{
				this.protokol = sb.substring(0, indexOf);
				sb.delete(0, indexOf + 3);
				// System.out.println("Nový protokol: " + this.protokol);
				// System.out.println("  sb: " + sb);
			}

			indexOf = sb.indexOf("/");
			if (-1 != indexOf)
			{
				this.doména = sb.substring(0, indexOf);
				sb.delete(0, indexOf + 1);
				// System.out.println("Nová doména: " + this.doména);
				// System.out.println("  sb: " + sb);
			}
			else
			{
				this.doména = sb.toString();
				sb.setLength(0);
				// System.out.println("Nová doména: " + this.doména);
				// System.out.println("  sb: " + sb);
			}

			indexOf = this.doména.indexOf(":");
			if (-1 != indexOf)
			{
				try
				{
					this.port = Integer.parseInt(
						this.doména.substring(indexOf + 1));
					// System.out.println("Nový port: " + this.port);
				}
				catch (Exception e)
				{
					// System.out.println(e);

					GRobotException.vypíšChybovéHlásenie(
						"Chyba pri zisťovaní čísla portu…"/*, false*/);
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
					return false;
				}

				this.doména = this.doména.substring(0, indexOf);
				// System.out.println("Úprava domény: " + this.doména);
			}

			indexOf = sb.lastIndexOf("/");
			if (-1 != indexOf)
			{
				this.vzdialenáCesta = sb.substring(0, indexOf);
				sb.delete(0, indexOf + 1);
				// System.out.println("Nová vzdialená cesta: " +
				// 	this.vzdialenáCesta);
				// System.out.println("  sb: " + sb);

				this.vzdialenýSúbor = sb.toString();
				// System.out.println("Nový vzdialený súbor: " +
				// 	this.vzdialenýSúbor);
			}
			else if (0 != sb.length())
			{
				this.vzdialenýSúbor = sb.toString();
				// System.out.println("Nový vzdialený súbor: " +
				// 	this.vzdialenýSúbor);
			}


			try
			{
				zostavKoreň();
				// System.out.println("Koreň: " + this.koreň);
			}
			catch (Exception e)
			{
				// System.out.println(e);

				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				return false;
			}

			if (otvor(this.vzdialenýSúbor))
			{
				// System.out.println("Vzdialený súbor „" +
				// 	this.vzdialenýSúbor + "“ bol úspešne otvorený.");

				vráťZmeny = false;
				return true;
			}

			return false;
		}
		finally
		{
			if (vráťZmeny)
			{
				this.protokol = protokol;
				this.doména = doména;
				this.port = port;
				this.vzdialenáCesta = vzdialenáCesta;
				this.koreň = koreň;
				this.vzdialenýSúbor = vzdialenýSúbor;
			}
		}
	}


	/**
	 * <p>Overí, či boli na server odoslané hlavičky spojenia. Túto hodnotu má
	 * zmysel overovať v čase medzi {@linkplain #otvor(String) otvorením}
	 * a {@linkplain #zavri() zavretím} spojenia.</p>
	 * 
	 * @return {@code valtrue}/{@code valfalse} podľa toho, či boli hlavičky
	 *     poslané, alebo nie
	 * 
	 * @see #pridajHlavičky(String...)
	 * @see #otvor(String)
	 * @see #zavri()
	 */
	public boolean hlavičkyPoslané() { return hlavičkaPoslaná; }

	/** <p><a class="alias"></a> Alias pre {@link #hlavičkyPoslané() hlavičkyPoslané}.</p> */
	public boolean hlavickyPoslane() { return hlavičkaPoslaná; }

	/**
	 * <p>Pridá jednu alebo viacero hlavičiek požiadavky k tejto správe, ak
	 * ešte {@linkplain #hlavičkyPoslané() neboli poslané.} Správa vzniká
	 * v okamihu {@linkplain #otvor(String) otvorenia spojenia} a posielanie
	 * hlavičiek nastáva pred poslaním ľubovoľného údaja na server.</p>
	 * 
	 * <p>Hlavičky sú zadávané ako dvojice hodnôt: kľúč, hodnota. Na server sú
	 * potom posielané v tvare {@code kľúč: hodnota}. Ďalšie užitočné
	 * informácie o hlavičkách požiadavky sú dostupné <a
	 * href="https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html#setRequestProperty-java.lang.String-java.lang.String-"
	 * target="_blank">tu (v anglickom jazyku).</a></p>
	 * 
	 * @param hlavičky jedna alebo viacero hlavičiek – dvojíc kľúč/hodnota;
	 *     každý kľúč musí mať párujúcu hodnotu; ak má zoznam nepárny počet
	 *     prvkov, tak posledný prvok nebude použitý (ak aj chcete pridať
	 *     hlavičku v „bezhodnotovom“ tvare, tak zadajte párujúcu hodnotu
	 *     rovnú {@code valnull})
	 * @return {@code valtrue} v prípade úspechu (ak hlavičky ešte
	 *     {@linkplain #hlavičkyPoslané() neboli poslané}), inak
	 *     {@code valfalse}
	 * 
	 * @see #hlavičkyPoslané()
	 * @see #otvor(String)
	 * @see #zavri()
	 */
	public boolean pridajHlavičky(String... hlavičky)
	{
		if (hlavičkaPoslaná) return false;
		if (null == hlavičky) return true;

		boolean semafor = false; String kľúč = null;
		for (String hodnota : hlavičky)
		{
			if (semafor)
			try
			{
				spojenie.setRequestProperty(kľúč, hodnota);
			}
			catch (IllegalStateException | NullPointerException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				return false;
			}
			else kľúč = hodnota;
			semafor = !semafor;
		}

		return true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajHlavičky(String...) pridajHlavičky}.</p> */
	public boolean pridajHlavicky(String... hlavičky)
	{ return pridajHlavičky(hlavičky); }


	/**
	 * <p>Pridá údajové pole so zadaným názvom a hodnotou k tejto správe.
	 * Správa vzniká v okamihu {@linkplain #otvor(String) otvorenia
	 * spojenia}.</p>
	 * 
	 * @param názov názov údaja na odoslanie
	 * @param hodnota hodnota údaja na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pridajÚdaj(String, long)
	 * @see #pridajÚdaj(String, double)
	 * @see #otvor(String)
	 * @see #zavri()
	 */
	public boolean pridajÚdaj(String názov, String hodnota)
	{
		if (hlavičkaPoslaná) return false;

		if (hodnota.length() >= hranica.length() &&
			-1 != hodnota.indexOf(hranica))
		{
			GRobotException.vypíšChybovéHlásenie(
				"Nemôžem pridať údaj, pretože obsahuje reťazec " +
				"ohraničenia údajov. Zvoľte iný identifikátor údajov.");
			return false;
		}

		try
		{
			hlavičkaObsahuSprávy += "--" + hranica + EOL +
				"Content-Disposition: form-data; name=\"" +
				URLEncoder.encode(názov, "UTF-8") + '"' +
				EOL + EOL + hodnota + EOL + "--" + hranica + EOL;
			return true;
		}
		catch (UnsupportedEncodingException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pridajÚdaj(String, String) pridajÚdaj}.</p> */
	public boolean pridajUdaj(String názov, String hodnota)
	{ return pridajÚdaj(názov, hodnota); }

	/**
	 * <p>Pridá údajové pole so zadaným názvom a hodnotou k tejto správe.
	 * Správa vzniká v okamihu {@linkplain #otvor(String) otvorenia
	 * spojenia}.</p>
	 * 
	 * @param názov názov údaja na odoslanie
	 * @param hodnota hodnota údaja na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pridajÚdaj(String, String)
	 * @see #pridajÚdaj(String, double)
	 * @see #otvor(String)
	 * @see #zavri()
	 */
	public boolean pridajÚdaj(String názov, long hodnota)
	{ return pridajÚdaj(názov, "" + hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajÚdaj(String, long) pridajÚdaj}.</p> */
	public boolean pridajUdaj(String názov, long hodnota)
	{ return pridajÚdaj(názov, "" + hodnota); }

	/**
	 * <p>Pridá údajové pole so zadaným názvom a hodnotou k tejto správe.
	 * Správa vzniká v okamihu {@linkplain #otvor(String) otvorenia
	 * spojenia}.</p>
	 * 
	 * @param názov názov údaja na odoslanie
	 * @param hodnota hodnota údaja na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pridajÚdaj(String, String)
	 * @see #pridajÚdaj(String, long)
	 * @see #otvor(String)
	 * @see #zavri()
	 */
	public boolean pridajÚdaj(String názov, double hodnota)
	{ return pridajÚdaj(názov, "" + hodnota); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajÚdaj(String, double) pridajÚdaj}.</p> */
	public boolean pridajUdaj(String názov, double hodnota)
	{ return pridajÚdaj(názov, "" + hodnota); }

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Odosielaný súbor musí mať predvolené kódovanie UTF-8. Metóda použije
	 * predvolený názov údajového poľa tejto správy, pod ktorým bude obsah
	 * súboru dostupný na serveri: {@code srg"textFile"}.
	 * V jednej správe je možné poslať len jeden súbor. Viac podrobností
	 * nájdete v opise metódy {@link #pošliSúbor(String, String, String)
	 * pošliSúbor}. Po volaní tejto metódy musí nasledovať volanie metódy
	 * {@link #zavri()}.</p>
	 * 
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String, String)
	 * @see #pošliTextovýSúbor(String, String, String)
	 * 
	 * @see #pošliSúbor(String)
	 * @see #pošliSúbor(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliTextovýSúbor(String názovSúboru)
	{
		return pošliSúbor("textFile", názovSúboru,
			"text/plain; charset=UTF-8");
	}

	/** <p><a class="alias"></a> Alias pre {@link #pošliTextovýSúbor(String) pošliTextovýSúbor}.</p> */
	public boolean posliTextovySubor(String názovSúboru)
	{
		return pošliSúbor("textFile", názovSúboru,
			"text/plain; charset=UTF-8");
	}

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Táto metóda dovoľuje spresniť kódovanie odosielaného textového súboru.
	 * Metóda použije predvolený názov údajového poľa tejto správy, pod
	 * ktorým bude obsah súboru dostupný na serveri: {@code srg"textFile"}.
	 * V jednej správe je možné poslať len jeden súbor. Viac podrobností
	 * nájdete v opise metódy {@link #pošliSúbor(String, String, String)
	 * pošliSúbor}. Po volaní tejto metódy musí nasledovať volanie metódy
	 * {@link #zavri()}.</p>
	 * 
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @param kódovanie kódovanie textového súboru; predvoleným kódovaním je
	 *     UTF-8
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String)
	 * @see #pošliTextovýSúbor(String, String, String)
	 * 
	 * @see #pošliSúbor(String)
	 * @see #pošliSúbor(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliTextovýSúbor(String názovSúboru, String kódovanie)
	{
		return pošliSúbor("textFile", názovSúboru,
			"text/plain; charset=" + kódovanie);
	}

	/** <p><a class="alias"></a> Alias pre {@link #pošliTextovýSúbor(String, String) pošliTextovýSúbor}.</p> */
	public boolean posliTextovySubor(String názovSúboru, String kódovanie)
	{
		return pošliSúbor("textFile", názovSúboru,
			"text/plain; charset=" + kódovanie);
	}

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Táto metóda dovoľuje spresniť kódovanie odosielaného textového súboru
	 * a zadať názov údajového poľa tejto správy, pod ktorým bude súbor
	 * dostupný na serveri.
	 * V jednej správe je možné poslať len jeden súbor. Viac podrobností
	 * nájdete v opise metódy {@link #pošliSúbor(String, String, String)
	 * pošliSúbor}. Po volaní tejto metódy musí nasledovať volanie metódy
	 * {@link #zavri()}.</p>
	 * 
	 * @param názovPoľa názov údajového poľa tejto správy priradeného
	 *     k odosielanému súboru
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @param kódovanie kódovanie textového súboru; predvoleným kódovaním je
	 *     UTF-8
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String)
	 * @see #pošliTextovýSúbor(String, String)
	 * 
	 * @see #pošliSúbor(String)
	 * @see #pošliSúbor(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliTextovýSúbor(String názovPoľa,
		String názovSúboru, String kódovanie)
	{
		return pošliSúbor(názovPoľa, názovSúboru,
			"text/plain; charset=" + kódovanie);
	}

	/** <p><a class="alias"></a> Alias pre {@link #pošliTextovýSúbor(String, String) pošliTextovýSúbor}.</p> */
	public boolean posliTextovySubor(String názovPoľa,
		String názovSúboru, String kódovanie)
	{
		return pošliSúbor(názovPoľa, názovSúboru,
			"text/plain; charset=" + kódovanie);
	}

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Metóda použije predvolený názov údajového poľa tejto správy, pod
	 * ktorým bude súbor dostupný na serveri: {@code srg"uploadedFile"}
	 * a predvolený typ obsahu {@code srg"application/octet-stream"}.
	 * V jednej správe je možné poslať len jeden súbor. Viac podrobností
	 * nájdete v opise metódy {@link #pošliSúbor(String, String, String)
	 * pošliSúbor}. Po volaní tejto metódy musí nasledovať volanie metódy
	 * {@link #zavri()}.</p>
	 * 
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String)
	 * @see #pošliTextovýSúbor(String, String)
	 * @see #pošliTextovýSúbor(String, String, String)
	 * 
	 * @see #pošliSúbor(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliSúbor(String názovSúboru)
	{
		return pošliSúbor("uploadedFile", názovSúboru,
			"application/octet-stream");
	}

	/** <p><a class="alias"></a> Alias pre {@link #pošliSúbor(String) pošliSúbor}.</p> */
	public boolean posliSubor(String názovSúboru)
	{
		return pošliSúbor("uploadedFile", názovSúboru,
			"application/octet-stream");
	}

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Súbor bude na serveri dostupný prostredníctvom údajového poľa so
	 * zadaným názvom v parametri {@code názovPoľa}. Metóda použije
	 * predvolený typ obsahu {@code srg"application/octet-stream"}.
	 * V jednej správe je možné poslať len jeden súbor. Viac podrobností
	 * nájdete v opise metódy {@link #pošliSúbor(String, String, String)
	 * pošliSúbor}. Po volaní tejto metódy musí nasledovať volanie metódy
	 * {@link #zavri()}.</p>
	 * 
	 * @param názovPoľa názov údajového poľa tejto správy priradeného
	 *     k odosielanému súboru
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String)
	 * @see #pošliTextovýSúbor(String, String)
	 * @see #pošliTextovýSúbor(String, String, String)
	 * 
	 * @see #pošliSúbor(String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliSúbor(String názovPoľa, String názovSúboru)
	{ return pošliSúbor(názovPoľa, názovSúboru, "application/octet-stream"); }

	/** <p><a class="alias"></a> Alias pre {@link #pošliSúbor(String, String) pošliSúbor}.</p> */
	public boolean posliSubor(String názovPoľa, String názovSúboru)
	{ return pošliSúbor(názovPoľa, názovSúboru, "application/octet-stream"); }

	/**
	 * <p>Odošle všetky neodoslané údaje spolu so súborom so zadaným názvom.
	 * Táto metóda umožňuje spresniť typ obsahu súboru odosielaného na
	 * server a zadať názov údajového poľa tejto správy, pod ktorým bude
	 * súbor dostupný na serveri.</p>
	 * 
	 * <p>Trieda {@link Spojenie Spojenie} z optimalizačných a bezpečnostných
	 * dôvodov podporuje odoslanie len jedného súboru v rámci jednej
	 * obojstrannej komunikácie (jednej správy) medzi klientom a serverom,
	 * preto musí byť volanie tejto metódy posledným v rámci aktuálnej
	 * komunikácie (to jest v rámci odosielania tejto správy) a po jej
	 * vykonaní musí nasledovať volanie metódy {@link #zavri()}. Na odoslanie
	 * ďalšieho súboru je potrebné {@linkplain #otvor(String) otvoriť nové
	 * spojenie}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Testovanie ukázalo, že serverový
	 * PHP skript nie je schopný spracovať odoslaný súbor, ak spolu s ním
	 * nebol odoslaný aspoň jeden {@linkplain #pridajÚdaj(String, String)
	 * údaj iného typu}. Dôvod sa nám nepodarilo zistiť. Z toho dôvodu sme
	 * sa rozhodli predvolene spolu so súborom odosielať aj čas jeho
	 * poslednej úpravy v samostatnom údajovom poli, ktorého predvolený
	 * názov je {@code srg"lastModified"}. Predvolený názov sa dá zmeniť
	 * metódou {@link #posielaťDátumPoslednejÚpravy(String)
	 * posielaťDátumPoslednejÚpravy}, pričom hodnota {@code valnull} zruší
	 * posielanie tohto údaja na server. V tom prípade musíte buď na server
	 * poslať iný údaj, alebo použiť iný server, ktorý bude schopný súbor
	 * spracovať aj bez poslania doplnkového údaja.</p>
	 * 
	 * @param názovPoľa názov údajového poľa tejto správy priradeného
	 *     k odosielanému súboru
	 * @param názovSúboru názov lokálneho súboru na odoslanie
	 * @param typObsahu textový reťazec určujúci typ súboru – typ obsahu
	 *     tejto prílohy sieťovej komunikácie
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #pošliTextovýSúbor(String)
	 * @see #pošliTextovýSúbor(String, String)
	 * @see #pošliTextovýSúbor(String, String, String)
	 * 
	 * @see #pošliSúbor(String)
	 * @see #pošliSúbor(String, String)
	 * @see #posielaťDátumPoslednejÚpravy()
	 * 
	 * @see #zavri()
	 */
	public boolean pošliSúbor(String názovPoľa,
		String názovSúboru, String typObsahu)
	{
		if (!hlavičkaPoslaná) try
		{
			if (spojenie instanceof HttpURLConnection)
			{
				// System.out.println("Prevádzam požiadavku na POST.");
				((HttpURLConnection)spojenie).setRequestMethod("POST");
			}

			try
			{
				if (null != pošliNaposledyUpravený)
					pridajÚdaj(pošliNaposledyUpravený, new File(
						lokálnaCesta + názovSúboru).lastModified());
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Nepodarilo sa odoslať čas poslednej úpravy súboru…");
				GRobotException.vypíšChybovéHlásenia(e);
			}

			int indexOf1 = názovSúboru.lastIndexOf('\\');
			int indexOf2 = názovSúboru.lastIndexOf('/');

			if (-1 != indexOf1 || -1 != indexOf2)
			{
				String názovBezCesty = názovSúboru.substring(
					1 + Math.max(indexOf1, indexOf2));
				hlavičkaObsahuSprávy += "Content-Disposition: form-data; " +
					"name=\"" + URLEncoder.encode(názovPoľa, "UTF-8") +
					"\"; filename=\"" + URLEncoder.encode(názovBezCesty,
					"UTF-8") + "\"; filename*=UTF-8''" + URLEncoder.encode(
					názovBezCesty, "UTF-8") + EOL + "Content-Type: " +
					typObsahu + EOL + EOL;
			}
			else
				hlavičkaObsahuSprávy += "Content-Disposition: form-data; " +
					"name=\"" + URLEncoder.encode(názovPoľa, "UTF-8") +
					"\"; filename=\"" + URLEncoder.encode(názovSúboru,
					"UTF-8") + "\"; filename*=UTF-8''" + URLEncoder.encode(
					názovSúboru, "UTF-8") + EOL + "Content-Type: " +
					typObsahu + EOL + EOL;

			// Testovanie, či nepomôže odoslanie jednoduchšej hlavičky,
			// keď neboli odoslané žiadne ďalšie údaje (nie, nepomohlo to):
			// 
			// hlavičkaObsahuSprávy += "Content-Disposition: form-data; name=\"" +
			// 	názovPoľa + "\"; filename=\"" + názovSúboru + "\"" + EOL +
			// 	"Content-Type: " + typObsahu + EOL + EOL;

			veľkosťPrílohy = new File(lokálnaCesta + názovSúboru).length();

			sekvenciaTransferuZdroj = lokálnaCesta + názovSúboru;
			sekvenciaTransferuCieľ = názovPoľa;
			sekvenciaTransferuStav = 0;
			sekvenciaTransferuCelkovo = veľkosťPrílohy;

			// Dá sa poslať len jeden súbor:
			if (!pošliHlavičku()) return false;

			FileInputStream prúdSúboru =
				new FileInputStream(lokálnaCesta + názovSúboru);

			// Starší spôsob prevzatý z inej triedy:
			// 
			// byte[] údajeSúboru = new byte[prúdSúboru.available()];
			// prúdSúboru.read(údajeSúboru);
			// veľkosťPrílohy = údajeSúboru.length;
			// 
			// int index = 0;
			// int veľkosť = 1024;
			// 
			// do {
			// 	System.out.println("  Odosielam údaje: " + index);
			// 
			// 	if ((index + veľkosť) > údajeSúboru.length)
			// 		veľkosť = údajeSúboru.length - index;
			// 
			// 	výstup.write(údajeSúboru, index, veľkosť);
			// 	index += veľkosť;
			// 
			// } while (index < údajeSúboru.length);

			byte[] údajeSúboru = new byte[32768];
			int prečítaných; long prečítanáVeľkosť = 0L;

			while ((prečítaných = prúdSúboru.read(údajeSúboru)) > 0)
			{
				// System.out.println("  Odosielam údaje: " + prečítaných);
				výstup.write(údajeSúboru, 0, prečítaných);
				prečítanáVeľkosť += prečítaných;
				sekvenciaTransferuStav += prečítaných;
				sekvenciaTransferuÚdajov(ODOVZDANIE_ÚDAJOV);
			}

			// System.out.println("Odoslaných údajov: " + prečítanáVeľkosť);
			if (veľkosťPrílohy != prečítanáVeľkosť)
			{
				GRobotException.vypíšChybovéHlásenie(
					"Bola zaznamenaná chyba pri odosielaní súboru " +
					"na server. Počet prečítaných a odoslaných bajtov (" +
					prečítanáVeľkosť + ") sa nezhoduje s vopred zisteným " +
					"počtom bajtov prílohy (" + veľkosťPrílohy + ").", true);
			}

			try { prúdSúboru.close(); } catch (Exception ex)
			{ GRobotException.vypíšChybovéHlásenia(ex/*, false*/); }

			if (!pošliChvost()) return false;
			return true;
		}
		catch (Exception e)
		{
			GRobotException.vypíšChybovéHlásenie(
				"Chyba pri odosielaní súboru…");
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pošliSúbor(String, String, String) pošliSúbor}.</p> */
	public boolean posliSubor(String názovPoľa,
		String názovSúboru, String typObsahu)
	{ return pošliSúbor(názovPoľa, názovSúboru, typObsahu); }


	/**
	 * <p>Zistí názov údajového poľa, v ktorom bude odoslaný dátum poslednej
	 * úpravy súboru odosielaného na server. Hodnota {@code valnull} znamená,
	 * že tento údaj nie je na server odosielaný. Predvolenou hodnotou je
	 * {@code srg"lastModified"}. Ďalšie podrobnosti nájdete v opise metódy
	 * {@link #pošliSúbor(String, String, String) pošliSúbor}.</p>
	 * 
	 * @return názov údajového poľa na odoslanie dátumu poslednej úpravy
	 *     alebo {@code valnull}
	 * 
	 * @see #posielaťDátumPoslednejÚpravy(String)
	 * @see #pošliSúbor(String, String, String)
	 */
	public String posielaťDátumPoslednejÚpravy()
	{ return pošliNaposledyUpravený; }

	/** <p><a class="alias"></a> Alias pre {@link #posielaťDátumPoslednejÚpravy() posielaťDátumPoslednejÚpravy}.</p> */
	public String posielatDatumPoslednejUpravy()
	{ return posielaťDátumPoslednejÚpravy(); }

	/**
	 * <p>Zmení názov údajového poľa, v ktorom bude odoslaný dátum poslednej
	 * úpravy súboru odosielaného na server. Hodnota {@code valnull} znamená,
	 * že tento údaj nebude na server odoslaný. Predvolenou hodnotou je
	 * {@code srg"lastModified"}. Ďalšie podrobnosti nájdete v opise metódy
	 * {@link #pošliSúbor(String, String, String) pošliSúbor}.</p>
	 * 
	 * @param názovPoľa názov údajového poľa na odoslanie dátumu poslednej
	 *     úpravy (alebo {@code valnull})
	 * 
	 * @see #posielaťDátumPoslednejÚpravy()
	 * @see #pošliSúbor(String, String, String)
	 */
	public void posielaťDátumPoslednejÚpravy(String názovPoľa)
	{ pošliNaposledyUpravený = názovPoľa; }

	/** <p><a class="alias"></a> Alias pre {@link #posielaťDátumPoslednejÚpravy(String) posielaťDátumPoslednejÚpravy}.</p> */
	public void posielatDatumPoslednejUpravy(String názovPoľa)
	{ posielaťDátumPoslednejÚpravy(názovPoľa); }


	/**
	 * <p>Odošle všetky neodoslané údaje, prečíta odpoveď servera a zavrie
	 * aktuálne spojenie. Ak bolo vykonanie tejto metódy úspešné, má zmysel
	 * volať metódy slúžiace na zisťovanie údajov o odpovedi ako:
	 * {@link #dajOdpoveď() dajOdpoveď}, {@link #dajBajtyOdpovede()
	 * dajBajtyOdpovede} (podľa {@linkplain  #dajTypObsahuOdpovede()
	 * typu obsahu odpovede}), {@link #dajVeľkosťOdpovede()
	 * dajVeľkosťOdpovede}, {@link #dajÚdajePožiadavky() dajÚdajePožiadavky}
	 * {@link #dajÚdajeOdpovede() dajÚdajeOdpovede} a tak ďalej.</p>
	 * 
	 * @return {@code valtrue} v prípade úspechu, inak {@code valfalse}
	 * 
	 * @see #otvor(String)
	 * @see #otvor(String, String, String, String...)
	 * @see #hlavičkyPoslané()
	 * @see #pridajHlavičky(String...)
	 * @see #pridajÚdaj(String, String)
	 * @see #pošliSúbor(String, String, String)
	 * @see #dajOdpoveď()
	 * @see #uložOdpoveď()
	 * @see #dajÚdajePožiadavky()
	 * @see #dajÚdajeOdpovede()
	 * @see #dajSpojenie()
	 */
	public boolean zavri()
	{
		sekvenciaTransferuZdroj = koreň + vzdialenýSúbor;
		sekvenciaTransferuCieľ = null;
		sekvenciaTransferuStav = 0;
		sekvenciaTransferuCelkovo = -1;

		čítajOdpoveď(null);

		if (null != výstup)
		{
			try { výstup.close(); } catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
			výstup = null;
		}

		if (null != vstup)
		{
			try { vstup.close(); } catch (Exception e)
			{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
			vstup = null;
		}

		return odpoveďPrečítaná;
	}


	/**
	 * <p>Táto statická metóda slúži na zistenie aktuálneho názvu hosťa,
	 * to jest tohto zariadenia. (Má ešte jednu verziu, ktorá umožňuje
	 * zistiť kánonický názov hosťa.)</p>
	 * 
	 * @return názov zariadenia (hosťa)
	 * 
	 * @throws GRobotException ak sa nepodarilo získať názov zariadenia
	 *     (hosťa)
	 * 
	 * @see #dajNázovHosťa(boolean)
	 * @see #dajAdresu()
	 * @see #dajHardvérovúAdresu()
	 */
	public static String dajNázovHosťa()
	{
		try
		{
			InetAddress adresa = InetAddress.getLocalHost();
			return adresa.getHostName();
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Nepodarilo sa získať názov zariadenia (hosťa).",
				"cannotGetHostName", e);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajNázovHosťa() dajNázovHosťa}.</p> */
	public static String dajNazovHosta() { return dajNázovHosťa(); }


	/**
	 * <p>Táto statická metóda slúži na zistenie aktuálneho názvu hosťa,
	 * to jest tohto zariadenia. Hodnota parametra {@code kánonický} určuje,
	 * či má byť názov vrátený v kánonickej podobe.</p>
	 * 
	 * @param kánonický ak je {@code valtrue}, tak je vrátená kánonická
	 *     podoba názvu
	 * @return názov zariadenia (hosťa)
	 * 
	 * @throws GRobotException ak sa nepodarilo získať kánonický alebo
	 *     klasický názov zariadenia (hosťa)
	 * 
	 * @see #dajNázovHosťa()
	 * @see #dajAdresu()
	 * @see #dajHardvérovúAdresu()
	 */
	public static String dajNázovHosťa(boolean kánonický)
	{
		try
		{
			InetAddress adresa = InetAddress.getLocalHost();

			if (kánonický)
				return adresa.getCanonicalHostName();
			else
				return adresa.getHostName();
		}
		catch (Exception e)
		{
			if (kánonický)
				throw new GRobotException(
					"Nepodarilo sa získať kánonický názov zariadenia " +
					"(hosťa).", "cannotGetCanonicalHostName", e);
			else
				throw new GRobotException(
					"Nepodarilo sa získať názov zariadenia (hosťa).",
					"cannotGetHostName", e);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajNázovHosťa(boolean) dajNázovHosťa}.</p> */
	public static String dajNazovHosta(boolean kánonický)
	{ return dajNázovHosťa(kánonický); }

	/**
	 * <p>Táto statická metóda slúži na zistenie aktuálnej (IP) adresy
	 * tohto zariadenia. Adresa je vrátená v textovej podobe.</p>
	 * 
	 * @return adresa (IP) tohto zariadenia
	 * 
	 * @throws GRobotException ak sa nepodarilo získať adresu zariadenia
	 *     (hosťa)
	 * 
	 * @see #dajNázovHosťa()
	 * @see #dajNázovHosťa(boolean)
	 * @see #dajHardvérovúAdresu()
	 */
	public static String dajAdresu()
	{
		try
		{
			InetAddress adresa = InetAddress.getLocalHost();
			return adresa.getHostAddress();
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Nepodarilo sa získať adresu zariadenia.",
				"cannotGetAddress", e);
		}
	}

	/**
	 * <p>Táto statická metóda slúži na zistenie aktuálnej hardvérovej
	 * adresy tohto zariadenia. Adresa je vrátená v textovej podobe.</p>
	 * 
	 * @return hardvérová (obvykle MAC) adresa tohto zariadenia
	 * 
	 * @throws GRobotException ak sa nepodarilo získať hardvérovú adresu
	 *     zariadenia (hosťa)
	 * 
	 * @see #dajNázovHosťa()
	 * @see #dajNázovHosťa(boolean)
	 * @see #dajAdresu()
	 */
	public static String dajHardvérovúAdresu()
	{
		try
		{
			NetworkInterface rozhranie = NetworkInterface.
				getByInetAddress(InetAddress.getLocalHost());

			byte[] adresa = rozhranie.getHardwareAddress();

			String mac = "";
			for (int i = 0; i < adresa.length; i++)
			{
				if (mac.length() != 0) mac += '-';
				mac += String.format("%02X", adresa[i]);
			}

			return mac;
		}
		catch (Exception e)
		{
			throw new GRobotException(
				"Nepodarilo sa získať hardvérovú adresu zariadenia.",
				"cannotGetHardwareAddress", e);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #dajHardvérovúAdresu() dajHardvérovúAdresu}.</p> */
	public static String dajHardverovuAdresu()
	{ return dajHardvérovúAdresu(); }
}
