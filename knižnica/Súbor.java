
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
import java.awt.FileDialog;

import java.awt.geom.Point2D;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

import java.util.Enumeration;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


import knižnica.apacheAntZIP.ZipEntry;
import knižnica.apacheAntZIP.ZipFile;
import knižnica.apacheAntZIP.ZipOutputStream;
import knižnica.podpora.BiTreeMap;

import static knižnica.Konštanty.KOPÍROVANIE_SÚBOROV;
import static knižnica.Konštanty.POROVNANIE_SÚBOROV;
import static knižnica.Konštanty.PRIPÁJANIE_SÚBOROV;


// ---------------------- //
//  *** Trieda Súbor ***  //
// ---------------------- //

/*
	Organizácia metód

		sekcie
		zoznamy súborov a priečinkov
		rôzne statické
			existencia súboru (priečinka)
			overenie, či ide o súbor alebo priečinok
			kopírovanie, presun, mazanie súborov
		dialógy
		otvorenie a zatvorenie súboru
		menné priestory vlastností
		čítanie vlastností
		zápis vlastností
		čítanie údajov
		zápis údajov
		ďalšie vlastnosti
*/

/**
 * <p>Trieda na prácu so súbormi. Definuje metódy na otváranie,
 * zatváranie, zápis a čítanie textových súborov a metódy na prácu
 * s takzvanými „vlastnosťami“ (pozri nižšie).</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>V tomto príklade je ukázaný jednoduchý spôsob zápisu niekoľkých
 * riadkov textu do súboru a vzápätí vypísania obsahu súboru na obrazovku.</p>
 * 
 * <pre CLASS="example">
	{@code kwdtry}
	{
		{@code comm// Otvoriť súbor na zápis a zapísať niekoľko riadkov}
		{@code currsúbor}.{@link #otvorNaZápis(String) otvorNaZápis}({@code srg"pokus.txt"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Toto"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Je"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Pokusný"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Zápis"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Do"});
		{@code currsúbor}.{@link #zapíšRiadok(String) zapíšRiadok}({@code srg"Súboru"});
		{@code currsúbor}.{@link #zavri() zavri}();    {@code comm// Každý súbor musí byť po použití zatvorený}

		{@code comm// Vzápätí otvoriť súbor na čítanie a vypísať ho po riadkoch}
		{@code currsúbor}.{@link #otvorNaČítanie(String) otvorNaČítanie}({@code srg"pokus.txt"});
		{@link String String} riadok;
		{@code kwdwhile} ({@code valnull} != (riadok = {@code currsúbor}.{@link #čítajRiadok() čítajRiadok}()))
			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"„"}, riadok, {@code srg"“"});
	}
	{@code kwdcatch} ({@link IOException IOException} e)   {@code comm// vyžaduje import java.io.IOException;}
	{@code comm// alternatívne je možné použiť namiesto IOException všeobecný}
	{@code comm// typ Exception, ktorý nevyžaduje žiadny import}
	{
		{@code comm// Keby sme chceli text chyby vypísať na štandardný výstup,}
		{@code comm// použili by sme:}
		{@code comm//   System.out.println(e.getMessage());}

		{@code comm// Keby sme chceli vypísať úplný výpis chybovej stopy na štandardný}
		{@code comm// chybový výstup, použili by sme:}
		{@code comm//   e.printStackTrace();}

		{@code comm// Použijeme vnútornú konzolu robota na výpis textu chyby červenou}
		{@code comm// farbou:}
		{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IOException#getMessage() getMessage}());
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
	}

	{@code comm// Keby nastala chyba v predchádzajúcom bloku, súbor by nemusel byť korektne}
	{@code comm// zavretý, preto ho zatvárame v samostatnom bloku try-catch}
	{@code kwdtry}
	{
		{@code currsúbor}.{@link #zavri() zavri}();
	}
	{@code kwdcatch} ({@link IOException IOException} e)
	{
		{@code comm// Chyby vypíše červenou farbou…}
		{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IOException#getMessage() getMessage}());
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
	}
	</pre>
 * 
 * <p>Ak sa všetko vykoná korektne, súbor bude obsahovať nasledujúce
 * riadky:</p>
 * 
 * <pre CLASS="example">
	Toto
	Je
	Pokusný
	Zápis
	Do
	Súboru
	</pre>
 * 
 * <p>Na obrazovke budú jednotlivé riadky vypísané v úvodzovkách („“).</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>V tomto príklade je ukázaný jednoduchý spôsob čítania série celých
 * čísiel zo súboru.</p>
 * 
 * <p>Predpokladajme, že v priečinku s projektom BlueJa jestvuje textový
 * súbor {@code _čísla.txt} s nasledujúcim obsahom (medzery sú úmyselne
 * podfarbené tyrkysovou farbou):</p>
 * 
 * <pre CLASS="example">
	<span style="background: cyan;">   </span>8<span style="background: cyan;"> </span>23<span style="background: cyan;">  </span>3<span style="background: cyan;"> </span>4<span style="background: cyan;"> </span>32<span style="background: cyan;">  </span>0<span style="background: cyan;">  </span>-14<span style="background: cyan;">  </span>
	<span style="background: cyan;">   </span>
	<span style="background: cyan;"> </span>21<span style="background: cyan;">  </span>-2<span style="background: cyan;">  </span>+8<span style="background: cyan;"> </span>14<span style="background: cyan;"> </span>

	64<span style="background: cyan;">  </span>-0<span style="background: cyan;">  </span>-24<span style="background: cyan;">  </span>

	</pre>
 * 
 * <p>Nasledujúci úryvok kódu bude zo súboru čítať a vypisovať na
 * obrazovku celé čísla dovtedy, kým sa tam nejaké vyskytujú:</p>
 * 
 * <pre CLASS="example">
	{@code kwdtry}
	{
		{@code currsúbor}.{@link #otvorNaČítanie(String) otvorNaČítanie}({@code srg"_čísla.txt"});
		Long číslo; {@code typeboolean} prvé = {@code valtrue};
		{@code kwdwhile} ({@code valnull} != (číslo = {@code currsúbor}.{@link #čítajCeléČíslo() čítajCeléČíslo}()))
		{
			{@code kwdif} (prvé) prvé = {@code valfalse}; {@code kwdelse} {@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code srg", "});
			{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}(číslo);
		}

		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"."});
	}
	{@code kwdcatch} ({@link IOException IOException} e)   {@code comm// vyžaduje import java.io.IOException;}
	{@code comm// alternatívne je možné použiť namiesto IOException všeobecný}
	{@code comm// typ Exception, ktorý nevyžaduje žiadny import}
	{
		{@code comm// Nasledujúci príkaz vypíše zápis o prípadnej chybe na štandardný}
		{@code comm// chybový výstup (System.err). Zápis by sa objavil v okne}
		{@code comm// terminálu BlueJa.}
		e.{@link IOException#printStackTrace() printStackTrace}();
	}
	</pre>
 * 
 * <p>Výsledný výpis bude vyzerať takto:</p>
 * 
 * <pre CLASS="example">
	8, 23, 3, 4, 32, 0, &#45;14, 21, &#45;2, 8, 14, 64, 0, &#45;24.
	</pre>
 * 
 * <p> </p>
 * 
 * <p>Okrem klasických metód na čítanie a zápis, obsahuje trieda {@code 
 * currSúbor} aj metódy na zápis a čítanie vlastností, ktoré sú do textového
 * súboru ukladané v tvare {@code názov=hodnota}.</p>
 * 
 * <p>Neodporúčame kombinovať použitie klasických metód na čítanie
 * a zápis s metódami na čítanie a zápis vlastností. Rozdiel medzi oboma
 * prístupmi je, že „klasické“ údaje musia byť čítané zo súboru
 * v rovnakom poradí ako boli zapísané a vlastnosti môžu byť čítané
 * a zapisované v ľubovoľnom poradí. Vlastnosti tiež ošetrujú výskyt
 * takzvaných únikových (escape) sekvencií vo svojich hodnotách. Na lepšie
 * pochopenie práce s vlastnosťami poslúži nasledujúci príklad:</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Cenou za náhodný („nesekvenčný“)
 * prístup k hodnotám vlastností je vyššia pamäťová zložitosť
 * vnútorných algoritmov a z toho vyplývajúce pomalšie spracovanie pri
 * čítaní a zápise. Náhodný experiment pri tvorbe dokumentácie ukázal,
 * že trieda {@code currSúbor} dokáže spracovať aj relatívne veľké
 * konfiguračné súbory (určite najmenej päť megabajtov), ale ich spracovanie
 * (zostavenie, zápis, čítanie a analýza) trvalo niekoľko desiatok
 * sekúnd až minút (experiment bol náhodný, nie účelový a program na tvorbu
 * dokumentácie zostavoval a analyzoval asi päťdesiat rôzne veľkých
 * konfiguračných súborov – od kilobajtov po megabajty; procesy zostavenia
 * so zápisom a čítania s analýzou trvali niekoľko minút – experiment bol
 * vykonaný v roku 2018 na prenosnom počítači so štandardným výkonom<!--
 * Opakovanie na stolnom počítači a ďalšom prenosnom počítači? TODO? -->).</p>
 * 
 * <pre CLASS="example">
	{@code kwdtry}
	{
		{@code comm// Pokúsi sa otvoriť súbor na čítanie (ak nejestvuje, nastane}
		{@code comm// výnimka a program skončí, preto je potrebné vopred vytvoriť}
		{@code comm// prázdny súbor s názvom vlastnosti.txt – v operačnom systéme}
		{@code comm// zvonka BlueJa alebo pripísať pred tento program úryvok kódu}
		{@code comm// zverejnený nižšie – čítajte ďalej…)}
		{@code currsúbor}.{@link #otvorNaČítanie(String) otvorNaČítanie}({@code srg"vlastnosti.txt"});

		{@code comm// Definícia premenných obsahujúca čítanie vlastností zo súboru}
		{@code typedouble}[] pole = {@code kwdnew} {@code typedouble}[]{{@code num2.2}, {@code num4.4}, {@code num2.8}, {@code num14.0}, {@code num18}};
		{@link java.lang.Double Double} číslo = {@code currsúbor}.{@link #čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"číslo"}, {@code kwdnew} {@link java.lang.Double#Double(double) Double}({@code num0}));
		{@link String String} reťazec = {@code currsúbor}.{@link #čítajVlastnosť(String, String) čítajVlastnosť}({@code srg"reťazec"}, {@code kwdnew} {@link String#String(java.lang.String) String}({@code srg"pokus"}));
		pole = {@code currsúbor}.{@link #čítajVlastnosť(String, double[]) čítajVlastnosť}({@code srg"pole"}, pole);

		{@code comm// Zavretie súboru}
		{@code currsúbor}.{@link #zavri() zavri}();

		{@code comm// Výpis hodnôt premenných}
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"číslo: "}, číslo);
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"reťazec: "}, reťazec);
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"pole: "}, pole);

		{@code comm// Zmena hodnoty prvku poľa (na demonštráciu)}
		pole[{@code num3}] *= {@code num2};

		{@code comm// Otvorenie súboru na zápis a zápis vlastností do súboru}
		{@code comm// (namiesto obsahu premennej číslo zapíšeme konštantnú hodnotu}
		{@code comm// 4 a v prvku pole[3] bude dvojnásobok pôvodnej hodnoty,}
		{@code comm// všetky ostatné údaje zostanú v pôvodnom tvare)}
		{@code currsúbor}.{@link #otvorNaZápis(String) otvorNaZápis}({@code srg"vlastnosti.txt"});
		{@code currsúbor}.{@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"číslo"}, {@code num4});
		{@code currsúbor}.{@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"reťazec"}, reťazec);
		{@code currsúbor}.{@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"pole"}, pole);
	}
	{@code kwdcatch} ({@link IOException IOException} e)   {@code comm// vyžaduje import java.io.IOException;}
	{@code comm// alternatívne je možné použiť namiesto IOException všeobecný}
	{@code comm// typ Exception, ktorý nevyžaduje žiadny import, ale potom by}
	{@code comm// bolo treba vymazať blok catch (IllegalArgumentException…}
	{
		{@code comm// Chyby vypíše červenou farbou…}
		{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IOException#getMessage() getMessage}());
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
	}
	{@code kwdcatch} ({@link IllegalArgumentException IllegalArgumentException} e)
	{
		{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IllegalArgumentException#getMessage() getMessage}());
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
	}
	{@code kwdfinally}
	{
		{@code kwdtry}
		{
			{@code comm// Zavretie súboru}
			{@code currsúbor}.{@link Súbor#zavri() zavri}();
		}
		{@code kwdcatch} ({@link IOException IOException} e)
		{
			{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
			{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IOException#getMessage() getMessage}());
			{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
		}
	}
	</pre>
 * 
 * <p>Ak sa všetko vykoná korektne (pozorne si prečítajte aj komentáre
 * v príklade), na obrazovke bude výpis:</p>
 * 
 * <pre CLASS="example">
	číslo: 0.0
	reťazec: pokus
	pole: 2.2 4.4 2.8 14.0 18.0
	</pre>
 * 
 * <p>V súbore však budú údaje zapísané s drobnými rozdielmi:</p>
 * 
 * <pre CLASS="example">
	číslo=4
	reťazec=pokus
	pole=2.2 4.4 2.8 28.0 18.0 
	</pre>
 * 
 * <p>Po každom ďalšom spustení programu bude hodnota štvrtého prvku poľa
 * dvojnásobná.</p>
 * 
 * <p>Ak nechcete ručne vytvárať prázdny súbor {@code vlastnosti.txt}
 * zvonka BlueJa, tak pripíšte na začiatok programu nasledujúci úryvok
 * kódu, ktorý vykoná kontrolu, či súbor jestvuje a v prípade, že
 * nejestvuje ho vytvorí (na vytvorenie súboru ho stačí otvoriť na zápis
 * a zavrieť).
 * 
 * <pre CLASS="example">
	{@code kwdtry}
	{
		{@code kwdif} (!{@link Súbor Súbor}.{@link Súbor#jestvuje(String) jestvuje}({@code srg"vlastnosti.txt"}))
		{
			{@code currsúbor}.{@link #otvorNaZápis(String) otvorNaZápis}({@code srg"vlastnosti.txt"});
			{@code currsúbor}.{@link Súbor#zavri() zavri}();
		}
	}
	{@code kwdcatch} ({@link IOException IOException} | {@link IllegalArgumentException IllegalArgumentException} e)
	{
		{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(e.{@link IOException#getMessage() getMessage}());
		{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
	}
	</pre>
 * 
 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť upozorneniu pri
 * metóde {@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}!</p>
 */
public class Súbor implements Closeable
{
	// /*packagePrivate*/ Súbor() {} // zrušenie tohto komentára by znamenalo
	//	zákazanie vytvárania vlastných inštancií triedy Súbor; programátor
	//	by sa musel uspokojiť s tým, že každý robot môže otvoriť jeden súbor…

	// Reťazcová konštanta slúžiaca ako náhradný výpis pre hodnoty null.
	// (V súčasnosti je hodnota konštanty zhodná s reťazcom, ktorý by sme
	// získali rýchlou „konverziou“ tejto hodnoty na reťazec: "" + null;)
	/*packagePrivate*/ final static String nullString = "null";

	// Práca so súbormi

		/*packagePrivate*/ static String upravLomky(String cesta,
			boolean lomkaNaKonci)
		{
			if (null == cesta) return "";

			if (!cesta.isEmpty())
			{
				/*
				if (File.separatorChar == '/' && -1 != cesta.indexOf('\\'))
				{
					StringBuffer konverzia = new StringBuffer(cesta);
					int index;

					while (-1 != (index = cesta.indexOf('\\')))
					{
						konverzia.setCharAt(index, File.separatorChar);
					}

					if (lomkaNaKonci && konverzia.charAt(konverzia.
						length() - 1) != File.separatorChar)
						konverzia.append(File.separatorChar);

					return konverzia.toString();
				}

				if (File.separatorChar == '\\' && -1 != cesta.indexOf('/'))
				{
					StringBuffer konverzia = new StringBuffer(cesta);
					int index;

					while (-1 != (index = cesta.indexOf('/')))
					{
						konverzia.setCharAt(index, File.separatorChar);
					}

					if (lomkaNaKonci && konverzia.charAt(konverzia.
						length() - 1) != File.separatorChar)
						konverzia.append(File.separatorChar);

					return konverzia.toString();
				}

				if (lomkaNaKonci && cesta.charAt
					(cesta.length() - 1) != File.separatorChar)
					return cesta + File.separator;
				*/

				if (lomkaNaKonci && cesta.charAt
					(cesta.length() - 1) != '/')
					return cesta + '/';
			}

			return cesta;
		}


		/*packagePrivate*/ static String[] zoznamZJarSúboru(String názovJaru,
			String cesta, boolean súbory, boolean priečinky)
		{
			Vector<String> zoznam = null;
			JarFile súborJar = null;
			int indexOf1 = -1;

			if (null != cesta)
			{
				if (cesta.isEmpty()) cesta = null; else
				{
					// #staré# if (-1 != cesta.indexOf('\\'))
					// #staré# {
					// #staré# 	/* // ineffective way
					// #staré# 	StringBuffer replace =
					// #staré# 		new StringBuffer(cesta.toUpperCase());
					// #staré# 
					// #staré# 	while (-1 != (indexOf1 = replace.indexOf("\\")))
					// #staré# 		replace.setCharAt(indexOf1, '/');
					// #staré# 
					// #staré# 	replace.append('/');
					// #staré# 	cesta = replace.toString();
					// #staré# 	*/
					// #staré# 	cesta = cesta.replace('\\', '/').
					// #staré# 		toUpperCase() + '/';
					// #staré# }
					// #staré# else
					// #staré# 	cesta = cesta.toUpperCase() + '/';

					cesta = cesta.toUpperCase();
					String[] časti = cesta.split("[\\\\/]+");

					zoznam = new Vector<String>();

					for (String časť : časti)
					{
						if (časť.equals(".."))
						{
							if (!zoznam.isEmpty())
								zoznam.remove(zoznam.size() - 1);
						}
						else if (!časť.equals(".")) zoznam.add(časť);
					}

					if (zoznam.isEmpty()) cesta = null; else
					{
						StringBuffer nová = new StringBuffer();

						for (String časť : zoznam)
						{
							nová.append(časť);
							nová.append('/');
						}

						cesta = nová.toString();
					}

					zoznam = null;
				}
			}

			try
			{
				if (null == názovJaru)
					súborJar = new JarFile(getJarPathName());
				else
					súborJar = new JarFile(názovJaru);

				Enumeration<JarEntry> položkyJaru = súborJar.entries();
				zoznam = new Vector<String>();

				while (položkyJaru.hasMoreElements())
				{
					String názovPoložky =
						položkyJaru.nextElement().getName();

					String názovPriečinka = null;
					String názovSúboru = null;
					int indexOf2 = -1;

					if (null != cesta)
					{
						if (!názovPoložky.toUpperCase().
							startsWith(cesta)) continue;

						názovPoložky = názovPoložky.
							substring(cesta.length(),
								názovPoložky.length());
					}

					if (-1 != (indexOf1 = názovPoložky.indexOf("/")))
					{
						názovPriečinka = názovPoložky.
							substring(0, indexOf1);
					}
					else
					{
						if (-1 == (indexOf2 = názovPoložky.
							indexOf("/", ++indexOf1)))
						{
							názovSúboru = názovPoložky.
								substring(indexOf1,
									názovPoložky.length());
						}
					}

					if (priečinky && null != názovPriečinka)
					{
						if (!zoznam.contains(názovPriečinka))
							zoznam.add(názovPriečinka);
					}

					if (súbory && null != názovSúboru)
						zoznam.add(názovSúboru);
				}
			}
			catch (IOException e)
			{
				// Nevadí, zoznam bude jednoducho null.
			}
			catch (NullPointerException e)
			{
				// Nevadí… (detto)
			}
			finally
			{
				if (súborJar != null)
				{
					try { súborJar.close(); }
					catch (IOException ioe)
					{
						// Čo už… Aj tak by sme s tým nič nespravili…
					}
				}
			}

			if (null == zoznam) return null;

			String výslednýZoznam[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i)
				výslednýZoznam[i] = zoznam.get(i);

			return výslednýZoznam;
		}


		// Vráti vstupný prúd fyzického súboru na disku
		/*packagePrivate*/ static InputStream
			dajVstupnýPrúdSúboru(String názovSúboru)
			throws FileNotFoundException
		{
			InputStream vstupnýPrúd; FileNotFoundException notFound = null;

			// Najprv skúsi otvoriť prúd na aktuálnej ceste:
			try
			{
				vstupnýPrúd = new FileInputStream(názovSúboru);
				return vstupnýPrúd;
			}
			catch (FileNotFoundException e)
			{
				notFound = e;
			}

			// Potom skúsi otvoriť prúd zo zoznamu tried classpath:
			ClassLoader nahrávačTried = ClassLoader.getSystemClassLoader();

			if (nahrávačTried instanceof URLClassLoader)
			{
				URL[] urls = ((URLClassLoader)nahrávačTried).getURLs();

				for (URL url: urls)
				{
					try
					{
						vstupnýPrúd = new FileInputStream(
							new File(url.toURI()).getCanonicalPath() +
							File.separator + názovSúboru);
						return vstupnýPrúd;
					}
					catch (FileNotFoundException | URISyntaxException e)
					{
						// Tento raz ignorované… („sorry“)
						// notFound = e;
					}
					catch (IOException e)
					{
						// Tiež ignorované… (druhý raz „sorry“)
					}
				}
			}

			throw notFound;
		}

		// Vráti vstupný prúd (input stream) zdroja (of resource)
		/*packagePrivate*/ static InputStream
			dajVstupnýPrúdZdroja(String názovSúboru)
		{
			String uriZdroja = názovSúboru.replace('\\', '/');
			InputStream vstupnýPrúd =
				Súbor.class.getResourceAsStream(uriZdroja);

			if (null == vstupnýPrúd && !uriZdroja.startsWith("/"))
			{
				uriZdroja = "/" + uriZdroja;
				vstupnýPrúd = Súbor.class.getResourceAsStream(uriZdroja);
			}

			return vstupnýPrúd;
		}

		// Pokúsi sa vytvoriť zo zdroja súbor a vrátiť jeho veľkosť
		/*packagePrivate*/ static long dajVeľkosťZdroja(String názovSúboru)
			throws URISyntaxException
		{
			String uriZdroja = názovSúboru.replace('\\', '/');
			URL url = Súbor.class.getResource(uriZdroja);

			if (null == url && !uriZdroja.startsWith("/"))
			{
				uriZdroja = "/" + uriZdroja;
				url = Súbor.class.getResource(uriZdroja);
			}

			return new File(url.toURI()).length();
		}

		// Vyhľadá súbor na disku a vráti ho v objekte typu File
		/*packagePrivate*/ static File nájdiSúbor(String názovSúboru)
			throws FileNotFoundException
		{
			File súbor;

			// Najprv skúsi nájsť súbor v aktuálnej lokalite:
			súbor = new File(názovSúboru);
			if (súbor.exists()) return súbor;

			// Potom skúsi nájsť súbor v zozname ciest classpath:
			ClassLoader nahrávačTried = ClassLoader.getSystemClassLoader();

			if (nahrávačTried instanceof URLClassLoader)
			{
				URL[] urls = ((URLClassLoader)nahrávačTried).getURLs();

				for (URL url: urls)
				{
					try
					{
						súbor = new File(new File(url.toURI()).
							getCanonicalPath() + File.separator +
							názovSúboru);
						if (súbor.exists()) return súbor;
					}
					catch (URISyntaxException e)
					{
						// Tento raz ignorované… („sorry“)
						// notFound = e;
					}
					catch (IOException e)
					{
						// Tiež ignorované… (druhý raz „sorry“)
					}
				}
			}

			return null;
		}

		// Vyhľadá súbor v .jar súbore a vráti jeho URL
		/*packagePrivate*/ static URL nájdiZdroj(String názovSúboru)
		{
			String uriZdroja = názovSúboru.replace('\\', '/');
			URL url = Súbor.class.getResource(uriZdroja);

			if (null == url && !uriZdroja.startsWith("/"))
			{
				uriZdroja = "/" + uriZdroja;
				url = Súbor.class.getResource(uriZdroja);
			}

			return url;
		}


		// Získanie názvu a cesty aktuálneho .jar balíčka.
		// Vráti null v prípade, že nejde o spustenie z .jar balíčka.

		/*packagePrivate*/ static String getJarPathName()
		{
			StringBuffer jarName = new StringBuffer(Súbor.class.
				getProtectionDomain().getCodeSource().getLocation().getPath());
			int indexOf;
			if (-1 != (indexOf = jarName.lastIndexOf("/")))
				jarName.delete(0, 1 + indexOf);
			if (0 == jarName.length()) return null; // not a .jar file
			jarName.insert(0, System.getProperty("user.dir") +
				File.separator);
			return jarName.toString();
		}


	// Atribúty triedy (všetko súkromné)

	// inštancie na prácu so súborom na zápis a čítanie
	private BufferedWriter zápis = null;
	private BufferedReader čítanie = null;

	// inštancia pripojeného archívu na čítanie alebo zápis
	private Archív archív = null;

	// premenná príznaku prvého riadka na ignorovanie prvého znaku
	// v UTF-8 súbore, ktorý signalizuje endianitu
	private boolean prvýRiadok = false;

	// Ukázalo sa, že tento príznak je nevyhnutný – indikuje koniec súboru
	private boolean koniecSúboru = false;

	// Ukázalo sa, že aj tento príznak je nevyhnutný – koordinuje metódy:
	// koniecSúboru, čítanieRiadka, čítajRiadok…
	private boolean overĎalšíRiadok = true;

	// inštancia textu, ktorý je využitý pri spracovaní čítania čísel
	private final StringBuffer prečítanýRiadok = new StringBuffer();


	// Zoznam reťazcov, ktoré sa smú nachádzať len na začiatku názvu
	// vlastnosti
	private final BiTreeMap<String, String> začiatkyNázvov = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa smú nachádzať len na konci názvu
	// vlastnosti
	private final BiTreeMap<String, String> konceNázvov = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa smú nachádzať na ľubovoľnej pozícii
	// v rámci názvu vlastnosti
	private final BiTreeMap<String, String> stredyNázvov = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa musia presne zhodovať s názvom vlastnosti
	private final BiTreeMap<String, String> presnéNázvy = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa smú nachádzať len na začiatku hodnoty
	// vlastnosti
	private final BiTreeMap<String, String> začiatkyHodnôt = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa smú nachádzať len na konci hodnoty
	// vlastnosti
	private final BiTreeMap<String, String> konceHodnôt = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa smú nachádzať na ľubovoľnej pozícii
	// v rámci hodnoty vlastnosti
	private final BiTreeMap<String, String> stredyHodnôt = new BiTreeMap<>();

	// Zoznam reťazcov, ktoré sa musia presne zhodovať s hodnotou vlastnosti
	private final BiTreeMap<String, String> presnéHodnoty = new BiTreeMap<>();


	/**
	 * <p>Táto trieda reprezentuje záznam v rámci množiny vlastností
	 * (pozri triedu {@link Vlastnosti}). Záznam môže uchovávať
	 * informáciu o konkrétnej konfiguračnej položke, to jest vlastnosti,
	 * o riadku komentára alebo o prázdnom riadku zapísanom do alebo
	 * prečítanom z konfiguračného súboru.</p>
	 */
	private class Vlastnosť
	{
		// Názov, haš kód, hodnota a stav pripravenosti na zápis:
		public final String názov;
		public final int haš;
		public String hodnota;
		public boolean naZápis;


		/** <p>Základný konštruktor.</p> */
		public Vlastnosť(String názov)
		{
			this.názov = názov;
			this.hodnota = null;
			this.naZápis = false;
			this.haš = názov.hashCode();
		}

		/** <p>Konštruktor s hodnotou.</p> */
		public Vlastnosť(String názov, String hodnota)
		{
			this.názov = názov;
			this.hodnota = hodnota;
			this.naZápis = false;
			this.haš = názov.hashCode();
		}

		/** <p>Konštruktor s hodnotou a príznakom pripravenosti na zápis.</p> */
		public Vlastnosť(String názov, String hodnota, boolean zapíšMa)
		{
			this.názov = názov;
			this.hodnota = hodnota;
			this.naZápis = zapíšMa;
			this.haš = názov.hashCode();
		}

		/**
		 * <p>Vráti haš kód.</p>
		 * @return haš kód
		 * @see Object#hashCode()
		 */
		@Override public int hashCode()
		{
			return haš;
		}

		/**
		 * <p>Overí zhodu dvoch objektov, ak sú kompatibilné. Zhoda môže byť
		 * vyhodnotená voči inej {@linkplain Vlastnosť vlastnosti} alebo
		 * názvu vlastnosti, čo môže byť ľubovoľná sekvencia znakov
		 * ({@link CharSequence}, napríklad reťazec {@link String}).</p>
		 * @param obj objekt na vyhodnotenie zhody
		 * @return {@code valtrue} v prípade zhody; {@code valfalse}
		 *     v opačnom prípade
		 * @see Object#equals(Object)
		 */
		@Override public boolean equals(Object obj)
		{
			if (null == obj) return null == názov;

			if (obj instanceof Vlastnosť)
			{
				Vlastnosť oth = (Vlastnosť)obj;
				if (null == názov && null == oth.názov) return true;
				if (null == názov || null == oth.názov) return false;
				return názov.equals(oth.názov); // TODO: zvážiť použitie IgnoreCase (možno by to malo byť voliteľné)
			}

			if (obj instanceof CharSequence)
			{
				CharSequence chs = (CharSequence)obj;
				if (null == názov && null == chs) return true;
				if (null == názov || null == chs) return false;
				return názov.equals(chs.toString()); // TODO: zvážiť použitie IgnoreCase (možno by to malo byť voliteľné)
			}

			return false;
		}
	}

	/**
	 * <p>Trieda záznamov o vlastnostiach – ukladá informácie o množine
	 * vlastností, ktoré sú čítané z alebo zapisované do konfiguračného
	 * súboru. Pozri tiež triedu {@link Vlastnosť}.</p>
	 */
	@SuppressWarnings("serial")
	private class Vlastnosti extends Vector<Vlastnosť>
	{
		/** <p>Pridá nový základný záznam o vlastnosti.</p> */
		public void pridaj(String názov)
		{
			add(new Vlastnosť(názov));
		}

		/** <p>Pridá nový záznam o vlastnosti s hodnotou.</p> */
		public void pridaj(String názov, String hodnota)
		{
			add(new Vlastnosť(názov, hodnota));
		}

		/**
		 * <p>Pridá nový záznam o vlastnosti s hodnotou a predvoleným stavom
		 * pripravenosti na zápis.</p>
		 */
		public void pridaj(String názov, String hodnota, boolean zapíšMa)
		{
			add(new Vlastnosť(názov, hodnota, zapíšMa));
		}

		/**
		 * <p>Nastaví hodnotu určenej vlastnosti na {@code valnull}, kde
		 * {@code názov} je názov vlastnosti.</p>
		 */
		public void píš(String názov)
		{
			int indexOf = indexOf(názov);
			if (-1 == indexOf) add(new Vlastnosť(názov));
			else get(indexOf).hodnota = null;
		}

		/**
		 * <p>Zapíše novú hodnotu určenej vlastnosti, kde {@code názov} je
		 * názov vlastnosti a {@code hodnota} je jej nová hodnota.</p>
		 */
		public void píš(String názov, String hodnota)
		{
			int indexOf = indexOf(názov);
			if (-1 == indexOf) add(new Vlastnosť(názov, hodnota));
			else get(indexOf).hodnota = hodnota;
		}

		/**
		 * <p>Zapíše novú hodnotu a stav pripravenosti na zápis určenej
		 * vlastnosti, kde {@code názov} je názov vlastnosti,
		 * {@code hodnota} je jej nová hodnota, a {@code zapíšMa} je nový
		 * stav pripravenosti na zápis.</p>
		 */
		public void píš(String názov, String hodnota, boolean zapíšMa)
		{
			int indexOf = indexOf(názov);
			if (-1 == indexOf) add(new Vlastnosť(názov, hodnota, zapíšMa));
			else
			{
				Vlastnosť vlastnosť = get(indexOf);
				vlastnosť.hodnota = hodnota;
				vlastnosť.naZápis = zapíšMa;
			}
		}

		/**
		 * <p>Zapíše stav pripravenosti na zápis určenej vlastnosti, kde
		 * {@code názov} je názov vlastnosti a {@code zapíšMa} je nový
		 * stav pripravenosti na zápis.</p>
		 */
		public void píš(String názov, boolean zapíšMa)
		{
			int indexOf = indexOf(názov);
			if (-1 == indexOf) add(new Vlastnosť(názov, null, zapíšMa));
			else get(indexOf).naZápis = zapíšMa;
		}


		/**
		 * <p>Vloží nový základný záznam o vlastnosti na určenú pozíciu
		 * v rámci tejto množiny vlastností.</p>
		 */
		public void vlož(int index, String názov)
		{
			insertElementAt(new Vlastnosť(názov), index);
		}

		/**
		 * <p>Vloží nový záznam o vlastnosti s hodnotou na určenú pozíciu
		 * v rámci tejto množiny vlastností.</p>
		 */
		public void vlož(int index, String názov, String hodnota)
		{
			insertElementAt(new Vlastnosť(názov, hodnota), index);
		}

		/**
		 * <p>Vloží nový záznam o vlastnosti s hodnotou a predvoleným stavom
		 * pripravenosti na zápis na určenú pozíciu v rámci tejto množiny
		 * vlastností.</p>
		 */
		public void vlož(int index, String názov, String hodnota,
			boolean zapíšMa)
		{
			insertElementAt(new Vlastnosť(názov, hodnota, zapíšMa), index);
		}


		/**
		 * <p>Nájde index určeného objektu v rámci tejto množiny vlastností,
		 * ak je určený objekt kompatibilný so záznamom vlastnosti (môže to
		 * byť iná vlastnosť alebo názov vlastnosti – reťazec; pozri aj
		 * {@link Vlastnosť#equals(Object)}).</p>
		 * @param o objekt na porovnanie
		 * @return index (pozícia) na ktorom bol objekt nájdený alebo
		 *     {@code num-1} (v opačnom prípade)
		 * @see Vector#indexOf(Object)
		 */
		@Override public int indexOf(Object o)
		{
			for (int i = 0; i < size(); ++i)
				if (o == null ? get(i) == null : get(i).equals(o)) return i;
			return -1;
		}

		/**
		 * <p>Nájde index určeného objektu v rámci tejto množiny vlastností
		 * začnúc hľadanie na stanovenej pozícii, ak je určený objekt
		 * kompatibilný so záznamom vlastnosti (môže to byť iná vlastnosť
		 * alebo názov vlastnosti – reťazec; pozri aj
		 * {@link Vlastnosť#equals(Object)}).</p>
		 * @param o objekt na porovnanie
		 * @param index počiatočný index vyhľadávania
		 * @return index (pozícia) na ktorom bol objekt nájdený alebo
		 *     {@code num-1} (v opačnom prípade)
		 * @see Vector#indexOf(Object, int)
		 */
		@Override public int indexOf(Object o, int index)
		{
			for (int i = index; i < size(); ++i)
				if (o == null ? get(i) == null : get(i).equals(o)) return i;
			return -1;
		}
	}

	/**
	 * <p>Trieda sekcií záznamov o vlastnostiach – ukladá informácie
	 * o množine sekcií vlastností, ktoré uľahčujú orientáciu zoskupovaním
	 * vlastností konfiguračného súboru. Pozri tiež triedu {@link 
	 * Vlastnosti}.</p>
	 */
	/*packagePrivate*/ class Sekcia
	{
		protected String názov = ""; // Prázdny názov môže mať len prvá sekcia!
		protected int haš = názov.hashCode();
		protected final Vlastnosti vlastnosti = new Vlastnosti();
		/*packagePrivate*/ String mennýPriestorVlastností = null;
		protected int naposledyZapísanáVlastnosť = -1;
		protected final Stack<String> zásobníkPriestorov = new Stack<>();

		public Sekcia()
		{
			// V podstate netreba nič, všetko je nastavené
		}

		public Sekcia(String názov)
		{
			if (null == názov) názov = "";
			názov = názov.trim();
			overPlatnosťNázvuSekcie(názov);
			this.názov = názov;
			this.haš = názov.hashCode();
		}

		/**
		 * <p>Vráti haš kód.</p>
		 * @return haš kód
		 * @see Object#hashCode()
		 */
		@Override public int hashCode()
		{
			return haš;
		}

		/**
		 * <p>Overí zhodu dvoch objektov, ak sú kompatibilné. Zhoda môže byť
		 * vyhodnotená voči inej {@linkplain Sekcia sekcii} alebo názvu
		 * sekcie, čo môže byť ľubovoľná sekvencia znakov
		 * ({@link CharSequence}, napríklad reťazec {@link String}).</p>
		 * @param obj objekt na vyhodnotenie zhody
		 * @return {@code valtrue} v prípade zhody; {@code valfalse}
		 *     v opačnom prípade
		 * @see Object#equals(Object)
		 */
		@Override public boolean equals(Object obj)
		{
			if (null == obj) obj = "";
			if (null == názov)
			{
				názov = "";
				haš = názov.hashCode();
			}

			if (obj instanceof Sekcia)
			{
				Sekcia oth = (Sekcia)obj;
				return názov.equals(oth.názov);
			}

			if (obj instanceof CharSequence)
			{
				CharSequence chs = (CharSequence)obj;
				return názov.equals(chs.toString());
			}

			return false;
		}

		/**
		 * <p>Vyčistí túto sekciu aj jej atribúty.</p>
		 */
		public void clear()
		{
			názov = "";
			haš = názov.hashCode();
			vlastnosti.clear();
			mennýPriestorVlastností = null;
			naposledyZapísanáVlastnosť = -1;
		}
	}

	/**
	 * <p>Trieda sekcií záznamov o vlastnostiach. Ukladá informácie
	 * o všetkých sekciách konfiguračných vlastností. Sekcie uľahčujú
	 * orientáciu v konfiguračnom súbore zoskupovaním vlastností do
	 * prehľadnejších celkov a tiež vytvárajú ďalšiu úroveň unikátnych
	 * menných priestorov. Pozri tiež triedu {@link Vlastnosti}.</p>
	 */
	@SuppressWarnings("serial")
	private class Sekcie extends Vector<Sekcia>
	{
		/**
		 * <p>Vymaže všetky sekcie aj zoznam sekcií.</p>
		 * @see Vector#clear()
		 */
		@Override public void clear()
		{
			for (Sekcia sekcia : this) sekcia.clear();
			super.clear();
		}

		/**
		 * <p>Nájde index určeného objektu v rámci množiny sekcií,
		 * ak je určený objekt kompatibilný so sekciou (môže to
		 * byť iná sekcia alebo názov sekcie – reťazec; pozri aj
		 * {@link Sekcia#equals(Object)}).</p>
		 * @param o objekt na porovnanie
		 * @return index (pozícia) na ktorom bol objekt nájdený alebo
		 *     {@code num-1} (v opačnom prípade)
		 * @see Vector#indexOf(Object)
		 */
		@Override public int indexOf(Object o)
		{
			for (int i = 0; i < size(); ++i)
				if (o == null ? get(i) == null : get(i).equals(o)) return i;
			return -1;
		}

		/**
		 * <p>Nájde index určeného objektu v rámci množiny sekcií
		 * začnúc hľadanie na stanovenej pozícii, ak je určený objekt
		 * kompatibilný so sekciou (môže to byť iná sekcia
		 * alebo názov sekcie – reťazec; pozri aj
		 * {@link Sekcia#equals(Object)}).</p>
		 * @param o objekt na porovnanie
		 * @param index počiatočný index vyhľadávania
		 * @return index (pozícia) na ktorom bol objekt nájdený alebo
		 *     {@code num-1} (v opačnom prípade)
		 * @see Vector#indexOf(Object, int)
		 */
		@Override public int indexOf(Object o, int index)
		{
			for (int i = index; i < size(); ++i)
				if (o == null ? get(i) == null : get(i).equals(o)) return i;
			return -1;
		}
	}


	// Atribúty vlastností:

		private final Sekcie sekcie = new Sekcie();
		private boolean zachovajNepoužitéVlastnosti = true;
		private boolean vlastnostiPrečítané = false;
		private boolean čítamVlastnosti = false;

		/*packagePrivate*/ Sekcia aktívnaSekcia = new Sekcia();
		// private Sekcia predvolenáSekcia = aktívnaSekcia;

	// Statické súkromné metódy

		private static void overPlatnosťPriečinka(File priečinok)
			throws FileNotFoundException
		{
			if (null == priečinok)
				throw new GRobotException(
					"Názov priečinka nesmie by prázdny.",
					"pathEmpty");

			if (!priečinok.exists())
				throw new FileNotFoundException("Cesta „" +
					priečinok + "“ nejestvuje.");

			if (!priečinok.isDirectory())
				throw new GRobotException("Zadaná cesta (" +
					priečinok + ") nesmeruje na priečinok.", "pathInvalid",
					priečinok.toString());

			if (!priečinok.canRead())
				throw new GRobotException("Cestu „" + priečinok +
					"“ nie je možné čítať.", "pathUnreadable",
					priečinok.toString());
		}

	// Súkromné pomocné metódy na zápis a čítanie vlastností

		private void zapisujVlastnosti() throws IOException
		{
			zaraďRezervovanéSekcie();
			Sekcia pôvodnáSekcia = aktívnaSekcia;

			// dumpSections("Idem zapisovať.");

			for (Sekcia sekcia : sekcie)
			{
				aktívnaSekcia = sekcia;
				if (!sekcia.názov.isEmpty())
					zapíšRiadok("[" + sekcia.názov + "]");

				for (Vlastnosť vlastnosť : aktívnaSekcia.vlastnosti)
				{
					if (zachovajNepoužitéVlastnosti || vlastnosť.naZápis)
					{
						if (vlastnosť.názov.equals("")) zapíšRiadok(); else
						{
							if (null == vlastnosť.hodnota)
							{
								// ‼TODO‼ Preklad názvov – otestovať
								String názov = vlastnosť.názov;
								String hodnota = vlastnosť.hodnota;

								if (presnéNázvy.containsKey(názov))
									názov = presnéNázvy.getValue(názov);
								else
								{
									for (Map.Entry<String, String> záznam :
										začiatkyNázvov.iterateKeys())
									{
										if (názov.startsWith(záznam.getKey()))
											názov = záznam.getValue() +
												názov.substring(záznam.
													getKey().length());
									}

									for (Map.Entry<String, String> záznam :
										konceNázvov.iterateKeys())
									{
										if (názov.endsWith(záznam.getKey()))
											názov = názov.substring(0,
												názov.length() -
												záznam.getKey().length()) +
												záznam.getValue();
									}

									for (Map.Entry<String, String> záznam :
										stredyNázvov.iterateKeys())
									{
										int indexZáznamu = názov.
											indexOf(záznam.getKey());
										if (-1 != indexZáznamu)
											názov.replace(záznam.getKey(),
												záznam.getValue());
									}
								}

								zapíšRiadok(názov);
							}
							else if (
								-1 != vlastnosť.hodnota.indexOf('\n') ||
								-1 != vlastnosť.hodnota.indexOf('\r') ||
								-1 != vlastnosť.hodnota.indexOf('\\'))
							{
								int indexOf = 0;
								StringBuffer upravenáHodnota =
									new StringBuffer(vlastnosť.hodnota);

								while (-1 != (indexOf = upravenáHodnota.
									indexOf("\\", indexOf)))
								{
									upravenáHodnota.replace(indexOf,
										indexOf + 1, "\\\\");
									// 4debug:
									// hodnota = new String(upravenáHodnota);
									indexOf += 2;
								}

								indexOf = 0;
								while (-1 != (indexOf = upravenáHodnota.
									indexOf("\n", indexOf)))
									upravenáHodnota.replace(indexOf,
										indexOf + 1, "\\n");

								indexOf = 0;
								while (-1 != (indexOf = upravenáHodnota.
									indexOf("\r", indexOf)))
									upravenáHodnota.replace(indexOf,
										indexOf + 1, "\\r");

								if (vlastnosť.názov.startsWith(";"))
									zapíšRiadok(";" + upravenáHodnota);
								else
								{
									// ‼TODO‼ Preklad názvov a hodnôt – otestovať
									String názov = vlastnosť.názov;
									String hodnota = upravenáHodnota.toString();

									if (presnéNázvy.containsKey(názov))
										názov = presnéNázvy.
											getValue(názov);
									else
									{
										for (Map.Entry<String, String> záznam :
											začiatkyNázvov.iterateKeys())
										{
											if (názov.startsWith(
												záznam.getKey()))
												názov = záznam.
													getValue() + názov.
													substring(záznam.
														getKey().length());
										}

										for (Map.Entry<String, String> záznam :
											konceNázvov.iterateKeys())
										{
											if (názov.endsWith(záznam.getKey()))
												názov = názov.
													substring(0,
													názov.length() -
													záznam.getKey().
													length()) + záznam.
													getValue();
										}

										for (Map.Entry<String, String> záznam :
											stredyNázvov.iterateKeys())
										{
											int indexZáznamu = názov.
												indexOf(záznam.getKey());
											if (-1 != indexZáznamu)
												názov.replace(
													záznam.getKey(),
													záznam.getValue());
										}
									}

									if (presnéHodnoty.containsKey(hodnota))
										hodnota = presnéHodnoty.
											getValue(hodnota);
									else
									{
										for (Map.Entry<String, String> záznam :
											začiatkyHodnôt.iterateKeys())
										{
											if (hodnota.startsWith(
												záznam.getKey()))
												hodnota = záznam.getValue() +
													hodnota.substring(záznam.
														getKey().length());
										}

										for (Map.Entry<String, String> záznam :
											konceHodnôt.iterateKeys())
										{
											if (hodnota.endsWith(
												záznam.getKey()))
												hodnota =
													hodnota.substring(0,
													hodnota.length() -
													záznam.getKey().
													length()) + záznam.
													getValue();
										}

										for (Map.Entry<String, String> záznam :
											stredyHodnôt.iterateKeys())
										{
											int indexZáznamu = hodnota.
												indexOf(záznam.getKey());
											if (-1 != indexZáznamu)
												hodnota.replace(
													záznam.getKey(),
													záznam.getValue());
										}
									}

									zapíšRiadok(názov + "=" + hodnota);
								}
							}
							else
							{
								if (vlastnosť.názov.startsWith(";"))
									zapíšRiadok(";" + vlastnosť.hodnota);
								else
								{
									// ‼TODO‼ Preklad názvov a hodnôt – otestovať
									String názov = vlastnosť.názov;
									String hodnota = vlastnosť.hodnota;

									if (presnéNázvy.containsKey(názov))
										názov = presnéNázvy.
											getValue(názov);
									else
									{
										for (Map.Entry<String, String> záznam :
											začiatkyNázvov.iterateKeys())
										{
											if (názov.startsWith(
												záznam.getKey()))
												názov = záznam.
													getValue() + názov.
													substring(záznam.
														getKey().length());
										}

										for (Map.Entry<String, String> záznam :
											konceNázvov.iterateKeys())
										{
											if (názov.
												endsWith(záznam.getKey()))
												názov = názov.
													substring(0,
													názov.length() -
													záznam.getKey().
													length()) + záznam.
													getValue();
										}

										for (Map.Entry<String, String> záznam :
											stredyNázvov.iterateKeys())
										{
											int indexZáznamu = názov.
												indexOf(záznam.getKey());
											if (-1 != indexZáznamu)
												názov.replace(
													záznam.getKey(),
													záznam.getValue());
										}
									}

									if (presnéHodnoty.containsKey(
										hodnota))
										hodnota = presnéHodnoty.
											getValue(hodnota);
									else
									{
										for (Map.Entry<String, String> záznam :
											začiatkyHodnôt.iterateKeys())
										{
											if (hodnota.startsWith(
												záznam.getKey()))
												hodnota =
													záznam.getValue() +
													hodnota.substring(
														záznam.getKey().length());
										}

										for (Map.Entry<String, String> záznam :
											konceHodnôt.iterateKeys())
										{
											if (hodnota.endsWith(
												záznam.getKey()))
												hodnota =
													hodnota.substring(0,
													hodnota.length() -
													záznam.getKey().
													length()) + záznam.
													getValue();
										}

										for (Map.Entry<String, String> záznam :
											stredyHodnôt.iterateKeys())
										{
											int indexZáznamu = hodnota.
												indexOf(záznam.getKey());
											if (-1 != indexZáznamu)
												hodnota.replace(záznam.getKey(),
													záznam.getValue());
										}
									}

									zapíšRiadok(názov + "=" + hodnota);
								}
							}
						}
					}
				}
			}

			aktívnaSekcia = pôvodnáSekcia;
		}

		private static void overPlatnosťNázvuVlastnosti(String názov)
		{
			if (názov.equals(""))
				throw new GRobotException(
					"Názov vlastnosti nesmie byť prázdny.",
					"propertyNameEmpty");

			if (názov.startsWith(";"))
				throw new GRobotException(
					"Názov vlastnosti sa nesmie začínať znakom komentára.",
					"propertyStartsWithSemicolon", názov);

			if (názov.startsWith("["))
				throw new GRobotException(
					"Názov vlastnosti sa nesmie začínať znakom hranatej " +
					"zátvorky.", "propertyStartsWithBracket", názov);

			if (-1 != názov.indexOf('.'))
				throw new GRobotException(
					"Názov vlastnosti nesmie obsahovať bodku.",
					"propertyContainsDot", názov);

			if (-1 != názov.indexOf('='))
				throw new GRobotException(
					"Názov vlastnosti nesmie obsahovať znak rovná sa.",
					"propertyContainsEquals", názov);
		}

		/*packagePrivate*/ static void overPlatnosťNázvuSekcie(String názov)
		{
			if (null != názov && !názov.equals(""))
			{
				if (názov.startsWith(";"))
					throw new GRobotException(
						"Názov sekcie sa nesmie začínať znakom komentára.",
						"sectionStartsWithSemicolon", názov);

				// Asi to nie je v konflikte… Znaky sa jednoducho pridajú
				// alebo odstránia pri zápise alebo čítaní.
				// if (-1 != názov.indexOf('[') || -1 != názov.indexOf(']'))
				// 	throw new GRobotException(
				// 		"Názov sekcie nesmie obsahovať znak hranatej zátvorky.",
				// 		"sectionContainsBracket", názov);
			}
		}

		private static Long prevezmiCeléČíslo(StringBuffer reťazec)
		{
			if (0 == reťazec.length()) return null;
			Long číslo = null;
			int indexOf = reťazec.indexOf(" ");

			while (0 == indexOf)
			{
				reťazec.deleteCharAt(0);
				if (0 == reťazec.length()) return null;
				indexOf = reťazec.indexOf(" ");
			}

			if (indexOf == -1)
			{
				try
				{
					if (!reťazec.toString().equalsIgnoreCase(nullString))
						číslo = Long.valueOf(reťazec.toString());
					reťazec.setLength(0);
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}
			else
			{
				try
				{
					if (!reťazec.substring(0,
						indexOf).equalsIgnoreCase(nullString))
						číslo = Long.valueOf(reťazec.substring(0, indexOf));
					reťazec.delete(0, indexOf + 1);
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return číslo;
		}

		private static Double prevezmiReálneČíslo(StringBuffer reťazec)
		{
			if (0 == reťazec.length()) return null;
			Double číslo = null;
			int indexOf = reťazec.indexOf(" ");

			while (0 == indexOf)
			{
				reťazec.deleteCharAt(0);
				if (0 == reťazec.length()) return null;
				indexOf = reťazec.indexOf(" ");
			}

			if (indexOf == -1)
			{
				try
				{
					if (!reťazec.toString().equalsIgnoreCase(nullString))
						číslo = Double.valueOf(reťazec.toString());
					reťazec.setLength(0);
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}
			else
			{
				try
				{
					if (!reťazec.substring(0, indexOf).equalsIgnoreCase
						(nullString)) číslo = Double.valueOf
							(reťazec.substring(0, indexOf));
					reťazec.delete(0, indexOf + 1);
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}

			return číslo;
		}

		private static Boolean prevezmiBoolean(StringBuffer reťazec)
		{
			if (0 == reťazec.length()) return null;
			Boolean hodnota = null;
			int indexOf = reťazec.indexOf(" ");

			while (0 == indexOf)
			{
				reťazec.deleteCharAt(0);
				if (0 == reťazec.length()) return null;
				indexOf = reťazec.indexOf(" ");
			}

			if (indexOf == -1)
			{
				if (!reťazec.toString().equalsIgnoreCase(nullString))
					hodnota = Boolean.valueOf(reťazec.toString());
				reťazec.setLength(0);
			}
			else
			{
				if (!reťazec.substring(0, indexOf).equalsIgnoreCase(nullString))
					hodnota = Boolean.valueOf(reťazec.substring(0, indexOf));
				reťazec.delete(0, indexOf + 1);
			}

			return hodnota;
		}

		private void čítajVlastnosti() throws IOException
		{
			if (vlastnostiPrečítané || čítamVlastnosti) return;
			čítamVlastnosti = true; try {

			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			Sekcia pôvodnáSekcia = aktívnaSekcia;
			String reťazec, názov, hodnota;
			while (null != (reťazec = čítajRiadok()))
			{
				if (reťazec.equals(""))
				{
					aktívnaSekcia.vlastnosti.pridaj("");
					continue;
				}

				int indexOf;
				if (reťazec.startsWith(";"))
				{
					hodnota = reťazec.substring(1);

					if (-1 != hodnota.indexOf('\\'))
					{
						StringBuffer upravenáHodnota =
							new StringBuffer(hodnota);

						for (indexOf = 0; -1 != (indexOf =
							upravenáHodnota.indexOf("\\", indexOf)); ++indexOf)
						{
							upravenáHodnota.deleteCharAt(indexOf);
							if (indexOf < upravenáHodnota.length())
							switch (upravenáHodnota.charAt(indexOf))
							{
								case 'n': upravenáHodnota.
									setCharAt(indexOf, '\n'); break;
								case 'r': upravenáHodnota.
									setCharAt(indexOf, '\r'); break;
								case 't': upravenáHodnota.
									setCharAt(indexOf, '\t'); break;
								case 'b': upravenáHodnota.
									setCharAt(indexOf, '\b'); break;
								case 'f': upravenáHodnota.
									setCharAt(indexOf, '\f'); break;
							}
						}

						hodnota = new String(upravenáHodnota);
					}

					aktívnaSekcia.vlastnosti.pridaj(";", hodnota);
					continue;
				}

				indexOf = reťazec.indexOf("[");
				if (-1 != indexOf)
				{
					názov = reťazec.trim();
					if (názov.startsWith(";"))	// ak je komentár odmedzerovaný
						throw new GRobotException(
							"V konfiguračnom súbore sa nachádza sekcia " +
							"alebo prvok poľa vlastností začínajúci sa " +
							"znakom komentára.",
							"sectionStartsWithSemicolon", názov);

					if (názov.startsWith("["))
					{
						if (názov.endsWith("]"))
							názov = názov.substring(1, názov.length() - 1);
						else
							názov = názov.substring(1);

						názov = názov.trim();

						// dumpSections("Pridávam sekciu (" + názov + ").");
						zaraďRezervovanéSekcie();

						if (1 >= sekcie.size() &&
							aktívnaSekcia.názov.isEmpty() &&
							aktívnaSekcia.vlastnosti.isEmpty())
						{
							// System.out.println("(Premenuj)");
							premenujSekciu(názov);
						}
						else
						{
							// System.out.println("(Aktivuj/vytvor)");
							aktivujSekciu(názov);
						}

						// dumpSections("Po pridaní sekcie.");
						continue;
					}
				}

				indexOf = reťazec.indexOf("=");
				if (-1 == indexOf)
				{
					názov = new String(reťazec);
					hodnota = null;
				}
				else
				{
					názov = reťazec.substring(0, indexOf);
					hodnota = reťazec.substring(1 + indexOf);
				}

				názov = názov.trim();
				if (názov.equals(""))
					throw new GRobotException(
						"V konfiguračnom súbore sa nachádza" +
						" vlastnosť bez názvu.", "propertyNameEmpty");

				if (názov.startsWith(";"))	// ak je komentár odmedzerovaný
					throw new GRobotException(
						"V konfiguračnom súbore sa nachádza vlastnosť " +
						"začínajúca sa znakom komentára.",
						"propertyStartsWithSemicolon",
						názov);

				if (-1 != aktívnaSekcia.vlastnosti.indexOf(názov))
					throw new GRobotException(
						"V konfiguračnom súbore sa nachádza zdvojená " +
						"vlastnosť: " + názov, "propertyDuplicate", názov);

				if (null != hodnota && -1 != hodnota.indexOf('\\'))
				{
					StringBuffer upravenáHodnota = new StringBuffer(hodnota);

					for (indexOf = 0; -1 != (indexOf =
						upravenáHodnota.indexOf("\\", indexOf)); ++indexOf)
					{
						upravenáHodnota.deleteCharAt(indexOf);
						if (indexOf < upravenáHodnota.length())
						switch (upravenáHodnota.charAt(indexOf))
						{
							case 'n': upravenáHodnota.
								setCharAt(indexOf, '\n'); break;
							case 'r': upravenáHodnota.
								setCharAt(indexOf, '\r'); break;
							case 't': upravenáHodnota.
								setCharAt(indexOf, '\t'); break;
							case 'b': upravenáHodnota.
								setCharAt(indexOf, '\b'); break;
							case 'f': upravenáHodnota.
								setCharAt(indexOf, '\f'); break;
						}
					}

					hodnota = new String(upravenáHodnota);
				}

				// ‼TODO‼ Spätný preklad názvov a hodnôt – otestovať
				if (presnéNázvy.containsValue(názov))
					názov = presnéNázvy.getKey(názov);
				else
				{
					for (Map.Entry<String, String> záznam :
						začiatkyNázvov.iterateValues())
					{
						if (názov.startsWith(záznam.getKey()))
							názov = záznam.getValue() +
								názov.substring(záznam.getKey().length());
					}

					for (Map.Entry<String, String> záznam :
						konceNázvov.iterateValues())
					{
						if (názov.endsWith(záznam.getKey()))
							názov = názov.substring(0, názov.length() -
								záznam.getKey().length()) + záznam.getValue();
					}

					for (Map.Entry<String, String> záznam :
						stredyNázvov.iterateValues())
					{
						int indexZáznamu = názov.indexOf(záznam.getKey());
						if (-1 != indexZáznamu)
							názov.replace(záznam.getKey(),
								záznam.getValue());
					}
				}

				if (null != hodnota)
				{
					if (presnéHodnoty.containsValue(hodnota))
						hodnota = presnéHodnoty.getKey(hodnota);
					else
					{
						for (Map.Entry<String, String> záznam :
							začiatkyHodnôt.iterateValues())
						{
							if (hodnota.startsWith(záznam.getKey()))
								hodnota = záznam.getValue() +
									hodnota.substring(záznam.
										getKey().length());
						}

						for (Map.Entry<String, String> záznam :
							konceHodnôt.iterateValues())
						{
							if (hodnota.endsWith(záznam.getKey()))
								hodnota = hodnota.substring(0,
									hodnota.length() - záznam.getKey().
									length()) + záznam.getValue();
						}

						for (Map.Entry<String, String> záznam :
							stredyHodnôt.iterateValues())
						{
							int indexZáznamu = hodnota.indexOf(
								záznam.getKey());
							if (-1 != indexZáznamu)
								hodnota.replace(záznam.getKey(),
									záznam.getValue());
						}
					}
				}

				aktívnaSekcia.vlastnosti.pridaj(názov, hodnota);
			}

			aktívnaSekcia = pôvodnáSekcia;
			vlastnostiPrečítané = true; }
			finally { čítamVlastnosti = false; }
		}


	// Súkromné metódy pomáhajúce pri zápise a čítaní údajov

		private void čítanieRiadka() throws IOException
		{
			if (koniecSúboru) return;

			prečítanýRiadok.setLength(0);
			String riadok = čítanie.readLine();
			overĎalšíRiadok = false;

			if (null == riadok) koniecSúboru = true;
			else if (prvýRiadok)
			{
				if (!riadok.isEmpty())
				{
					// Korekcia značky endianity kódovania UTF-8
					// (všetky údaje sú čítané prostredníctvom tejto metódy
					// a žiadne údaje nie sú čítané mimo nej, takže záplata
					// je na správnom mieste)

					if (riadok.charAt(0) == '\uFEFF')
						prečítanýRiadok.append(riadok.substring(1));
					else
						prečítanýRiadok.append(riadok);
				}

				prvýRiadok = false;
			}
			else prečítanýRiadok.append(riadok);
		}

		private boolean preskočiťMedzery() throws IOException
		{
			int dĺžka, poloha;
			if (prvýRiadok) čítanieRiadka();

			do
			{
				do
				{
					if (koniecSúboru) return true;
					dĺžka = prečítanýRiadok.length();
					if (0 == dĺžka) čítanieRiadka();
				}
				while (0 == dĺžka);

				poloha = 0;

				while (poloha < dĺžka && ' ' == prečítanýRiadok.charAt(poloha))
					++poloha;

				prečítanýRiadok.delete(0, poloha);
				overĎalšíRiadok = true;
			}
			while (poloha >= dĺžka);

			return false;
		}


	// Sekcie

		// Vráti sekciu so zadaným názvom alebo null, ak nejestvuje…
		private Sekcia dajSekciu(String názov)
		{
			if (null == názov) názov = "";
			názov = názov.trim();
			int indexOf = sekcie.indexOf(názov);
			if (-1 == indexOf) return null;
			return sekcie.get(indexOf);
		}

		// Zaradí aktívnu a prevolenú sekciu do zoznamu sekcií…
		private void zaraďRezervovanéSekcie()
		{
			if (null != aktívnaSekcia)
			{
				if (null == aktívnaSekcia.názov)
				{
					aktívnaSekcia.názov = "";
					aktívnaSekcia.haš = aktívnaSekcia.názov.hashCode();
				}

				int indexOf = sekcie.indexOf(aktívnaSekcia);
				if (-1 == indexOf)
				{
					if (aktívnaSekcia.názov.isEmpty())
						sekcie.add(0, aktívnaSekcia);
					else
						sekcie.add(aktívnaSekcia);
				}
				else if (aktívnaSekcia.názov.isEmpty() && 0 != indexOf)
				{
					sekcie.remove(aktívnaSekcia);
					sekcie.add(0, aktívnaSekcia);
				}
			}
		}


		/**
		 * <p>Vráti názov aktívnej sekcie. Prázdny reťazec označuje prvú
		 * bezmennú konfiguračnú pasáž.</p>
		 * 
		 * @return názov aktívnej sekcie alebo prázdny reťazec
		 * 
		 * @see #aktivujSekciu(String)
		 * @see #vymažSekciu(String)
		 * @see #premenujSekciu(String)
		 * @see #zoznamSekcií()
		 */
		public String aktívnaSekcia()
		{
			if (null == aktívnaSekcia.názov)
			{
				aktívnaSekcia.názov = "";
				aktívnaSekcia.haš = aktívnaSekcia.názov.hashCode();
			}
			return aktívnaSekcia.názov;
		}

		/** <p><a class="alias"></a> Alias pre {@link #aktívnaSekcia() aktívnaSekcia}.</p> */
		public String aktivnaSekcia() { return aktívnaSekcia(); }

		/**
		 * <p>Pridá novú alebo aktivuje jestvujúcu sekciu konfiguračných
		 * direktív. Prázdny reťazec označuje prvú bezmennú konfiguračnú
		 * pasáž. (Tá je definovaná predvolene.)</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Hodnota {@code valnull}
		 * je v tomto prípade nahradená hodnotou prázdneho reťazca
		 * {@code srg""}, takže tiež označuje predvolený (bezmenný)
		 * konfiguračný priestor.</p>
		 * 
		 * @param názov názov novej alebo jestvujúcej sekcie
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * 
		 * @see #aktívnaSekcia()
		 * @see #vymažSekciu(String)
		 * @see #premenujSekciu(String)
		 * @see #zoznamSekcií()
		 */
		public void aktivujSekciu(String názov) throws IOException
		{
			if (null == názov) názov = "";
			názov = názov.trim();

			if (null != čítanie) čítajVlastnosti();
			zaraďRezervovanéSekcie();
			// dumpSections("Aktivuj sekciu A.");

			Sekcia sekcia = dajSekciu(názov);
			if (null == sekcia) sekcia = new Sekcia(názov);

			aktívnaSekcia = sekcia;
			zaraďRezervovanéSekcie();
			// dumpSections("Aktivuj sekciu B.");
		}


		/**
		 * <p>Vymaže sekciu so zadaným názvom.</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Táto akcia je nevratná!</p>
		 * 
		 * @param názov názov sekcie na vymazanie
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * 
		 * @see #aktívnaSekcia()
		 * @see #aktivujSekciu(String)
		 * @see #premenujSekciu(String)
		 * @see #zoznamSekcií()
		 */
		public void vymažSekciu(String názov) throws IOException
		{
			if (null == názov) názov = "";
			názov = názov.trim();

			if (null != čítanie) čítajVlastnosti();
			// dumpSections("Pred mazaním.");

			if (null != aktívnaSekcia && aktívnaSekcia.equals(názov))
				aktívnaSekcia = null;

			// if (null != predvolenáSekcia && predvolenáSekcia.equals(názov))
			// 	predvolenáSekcia = null;

			// dumpSections("Po kontrole rezervovaných.");

			try
			{
				int indexOf = sekcie.indexOf(názov);
				if (-1 != indexOf)
				{
					Sekcia sekcia = sekcie.get(indexOf);
					sekcie.remove(indexOf);
					sekcia.clear();
					sekcia = null;
				}
			}
			finally
			{
				if (null == aktívnaSekcia)
				{
					aktívnaSekcia = dajSekciu("");
					if (null == aktívnaSekcia)
					{
						aktívnaSekcia = new Sekcia();
						zaraďRezervovanéSekcie();
					}
				}

				// if (null == predvolenáSekcia)
				// {
				// 	predvolenáSekcia = dajSekciu("");
				// 	if (null == predvolenáSekcia)
				// 	{
				// 		predvolenáSekcia = new Sekcia();
				// 		zaraďRezervovanéSekcie();
				// 	}
				// }

				// dumpSections("Po mazaní.");
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSekciu(String) vymažSekciu}.</p> */
		public void vymazSekciu(String názov) throws IOException
		{ vymažSekciu(názov); }

		/**
		 * <p>Zmení názov aktuálnej sekcie. Nový názov nesmie byť v konflikte
		 * s pomenovaním jestvujúcej sekcie. Ak je použitý prázny názov, tak
		 * (v prípade, že prázdna sekcia nejestvuje) bude táto sekcia
		 * premiestnená na prvé miesto v konfiguračnom súbore.</p>
		 * 
		 * @param názov nový názov sekcie
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * 
		 * @see #aktívnaSekcia()
		 * @see #aktivujSekciu(String)
		 * @see #vymažSekciu(String)
		 * @see #zoznamSekcií()
		 */
		public void premenujSekciu(String názov) throws IOException
		{
			if (null == názov) názov = "";
			názov = názov.trim();

			if (null != čítanie) čítajVlastnosti();

			if (null == aktívnaSekcia)
			{
				aktívnaSekcia = dajSekciu("");
				if (null == aktívnaSekcia)
					aktívnaSekcia = new Sekcia();
			}
			zaraďRezervovanéSekcie();

			overPlatnosťNázvuSekcie(názov);
			if (aktívnaSekcia.názov.equals(názov)) return;

			int indexOf = sekcie.indexOf(názov);
			if (-1 != indexOf)
				throw new GRobotException(
					"Sekcia so zadaným názvom „" + názov + "“ už jestvuje.",
					"sectionAlreadyExists", názov);

			aktívnaSekcia.názov = názov;
			aktívnaSekcia.haš = názov.hashCode();
			zaraďRezervovanéSekcie();
		}


		/**
		 * <p>Vráti zoznam aktuálnych sekcií definovaných v tomto
		 * konfiguračnom súbore. Zmeny vykonané vo vrátenom zozname
		 * nemajú žiadny vplyv na skutočné sekcie konfigurácie.</p>
		 * 
		 * @return zoznam reťazcov označujúcich názvy aktuálne definovaných
		 *     sekcií v tomto konfiguračnom súbore
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie,
		 *     prípadne obsahuje chybu
		 * 
		 * @see #aktívnaSekcia()
		 * @see #aktivujSekciu(String)
		 * @see #vymažSekciu(String)
		 * @see #premenujSekciu(String)
		 * @see #zoznamVlastností()
		 */
		public Zoznam<String> zoznamSekcií() throws IOException
		{
			čítajVlastnosti();
			Zoznam<String> zoznam = new Zoznam<>();
			for (Sekcia sekcia : sekcie)
				zoznam.add(sekcia.názov);
			if (0 == zoznam.dĺžka() && null != aktívnaSekcia)
				zoznam.add(aktívnaSekcia.názov);
			return zoznam;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zoznamSekcií() zoznamSekcií}.</p> */
		public Zoznam<String> zoznamSekcii() throws IOException
		{ return zoznamSekcií(); }


	// Zoznamy súborov a priečinkov

		/**
		 * <p>Vytvorí zoznam súborov umiestnených v zadanom priečinku.</p>
		 * 
		 * @param cesta cesta a názov priečinka, z obsahu ktorého má byť
		 *     vytvorený zoznam
		 * @return pole typu {@link java.lang.String String} so zoznamom
		 *     súborov
		 * 
		 * @throws GRobotException ak zadaná cesta nebola nájdená alebo
		 *     je neplatná; ak zadaná cesta nespĺňa niektoré pravidlo platnej
		 *     cesty
		 */
		public static String[] zoznamSúborov(String cesta)
		{
			File priečinok = new File(cesta);
			try
			{
				overPlatnosťPriečinka(priečinok);
			}
			catch (FileNotFoundException notFound)
			{
				String výslednýZoznam[] =
					zoznamZJarSúboru(null, cesta, true, false);
				if (null == výslednýZoznam)
				{
					if (null == cesta || !cesta.isEmpty())
						throw new GRobotException("Cesta „" + cesta +
							"“ nebola nájdená.", "pathNotFound", cesta,
							notFound);
					priečinok = new File(".");
				}
				else return výslednýZoznam;
			}

			File zoznamPoložiek[] = priečinok.listFiles();
			Vector<String> zoznam = new Vector<>();

			for (int i = 0; i < zoznamPoložiek.length; ++i)
				if (zoznamPoložiek[i].isFile())
					zoznam.add(zoznamPoložiek[i].getName());

			if (0 == zoznam.size()) return null;

			String výslednýZoznam[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i)
				výslednýZoznam[i] = zoznam.get(i);

			return výslednýZoznam;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zoznamSúborov(String) zoznamSúborov}.</p> */
		public static String[] zoznamSuborov(String cesta)
			// throws FileNotFoundException, IllegalArgumentException
		{ return zoznamSúborov(cesta); }

		/**
		 * <p>Vytvorí zoznam priečinkov umiestnených na zadanej ceste.</p>
		 * 
		 * @param cesta cesta na ktorej sa majú hľadať priečinky
		 * @return pole typu {@link java.lang.String String} so zoznamom
		 *     priečinkov
		 * 
		 * @throws GRobotException ak zadaná cesta nebola nájdená alebo
		 *     je neplatná; ak zadaná cesta nespĺňa niektoré pravidlo platnej
		 *     cesty
		 */
		public static String[] zoznamPriečinkov(String cesta)
		{
			File priečinok = new File(cesta);
			try
			{
				overPlatnosťPriečinka(priečinok);
			}
			catch (FileNotFoundException notFound)
			{
				String výslednýZoznam[] =
					zoznamZJarSúboru(null, cesta, false, true);
				if (null == výslednýZoznam)
				{
					if (null == cesta || !cesta.isEmpty())
						throw new GRobotException("Cesta „" + cesta +
							"“ nebola nájdená.", "pathNotFound", cesta,
							notFound);
					priečinok = new File(".");
				}
				else return výslednýZoznam;
			}

			File zoznamPoložiek[] = priečinok.listFiles();
			Vector<String> zoznam = new Vector<>();

			for (int i = 0; i < zoznamPoložiek.length; ++i)
				if (zoznamPoložiek[i].isDirectory())
					zoznam.add(zoznamPoložiek[i].getName());

			if (0 == zoznam.size()) return null;

			String výslednýZoznam[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i)
				výslednýZoznam[i] = zoznam.get(i);

			return výslednýZoznam;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zoznamPriečinkov(String) zoznamPriečinkov}.</p> */
		public static String[] zoznamPriecinkov(String cesta)
			// throws FileNotFoundException, IllegalArgumentException
		{ return zoznamPriečinkov(cesta); }

		/**
		 * <p>Vytvorí zoznam súborov a priečinkov umiestnených v zadanom
		 * priečinku.</p>
		 * 
		 * @param cesta cesta a názov priečinka, z obsahu ktorého má byť
		 *     vytvorený zoznam
		 * @return pole typu {@link java.lang.String String} so zoznamom
		 *     súborov a priečinkov
		 * 
		 * @throws GRobotException ak zadaná cesta nebola nájdená alebo
		 *     je neplatná; ak zadaná cesta nespĺňa niektoré pravidlo platnej
		 *     cesty
		 */
		public static String[] zoznamSúborovAPriečinkov(String cesta)
		{
			File priečinok = new File(cesta);
			try
			{
				overPlatnosťPriečinka(priečinok);
			}
			catch (FileNotFoundException notFound)
			{
				String výslednýZoznam[] =
					zoznamZJarSúboru(null, cesta, true, true);
				if (null == výslednýZoznam)
				{
					if (null == cesta || !cesta.isEmpty())
						throw new GRobotException("Cesta „" + cesta +
							"“ nebola nájdená.", "pathNotFound", cesta,
							notFound);
					priečinok = new File(".");
				}
				else return výslednýZoznam;
			}

			File zoznamPoložiek[] = priečinok.listFiles();
			Vector<String> zoznam = new Vector<>();

			for (int i = 0; i < zoznamPoložiek.length; ++i)
				if (zoznamPoložiek[i].isFile() ||
					zoznamPoložiek[i].isDirectory())
					zoznam.add(zoznamPoložiek[i].getName());

			if (0 == zoznam.size()) return null;

			String výslednýZoznam[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i)
				výslednýZoznam[i] = zoznam.get(i);

			return výslednýZoznam;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zoznamSúborovAPriečinkov(String) zoznamSúborovAPriečinkov}.</p> */
		public static String[] zoznamSuborovAPriecinkov(String cesta)
			// throws FileNotFoundException, IllegalArgumentException
		{ return zoznamSúborovAPriečinkov(cesta); }

		/**
		 * <p>Vytvorí zoznam všetkých položiek umiestnených na zadanej ceste.
		 * Je pravdepodobné, že zoznam bude zhodný so zoznamom vytvoreným
		 * s pomocou metódy {@link #zoznamSúborovAPriečinkov(String)
		 * zoznamSúborovAPriečinkov}, no tento zoznam môže obsahovať aj
		 * také položky, ktoré nie sú klasifikované ani ako súbory, ani ako
		 * priečinky (napríklad systémové odkazy, aliasy, jednotky…).</p>
		 * 
		 * @param cesta cesta a názov priečinka, z obsahu ktorého má byť
		 *     vytvorený zoznam
		 * @return pole typu {@link java.lang.String String} so zoznamom
		 *     položiek
		 * 
		 * @throws GRobotException ak zadaná cesta nebola nájdená alebo
		 *     je neplatná; ak zadaná cesta nespĺňa niektoré pravidlo platnej
		 *     cesty
		 */
		public static String[] zoznam(String cesta)
		{
			File priečinok = new File(cesta);
			try
			{
				overPlatnosťPriečinka(priečinok);
			}
			catch (FileNotFoundException notFound)
			{
				String výslednýZoznam[] =
					zoznamZJarSúboru(null, cesta, true, true);
				if (null == výslednýZoznam)
				{
					if (null == cesta || !cesta.isEmpty())
						throw new GRobotException("Cesta „" + cesta +
							"“ nebola nájdená.", "pathNotFound", cesta,
							notFound);
					priečinok = new File(".");
				}
				else return výslednýZoznam;
			}

			File zoznamPoložiek[] = priečinok.listFiles();
			Vector<String> zoznam = new Vector<>();

			for (int i = 0; i < zoznamPoložiek.length; ++i)
				zoznam.add(zoznamPoložiek[i].getName());

			if (0 == zoznam.size()) return null;

			String výslednýZoznam[] = new String[zoznam.size()];
			for (int i = 0; i < zoznam.size(); ++i)
				výslednýZoznam[i] = zoznam.get(i);

			return výslednýZoznam;
		}


	// Rôzne statické

		// Existencia súboru (priečinka)

		/**
		 * <p>Overí, či súbor alebo priečinok so zadaným názvom jestvuje.
		 * Metóda overuje len jestvovanie reálnych súborov na pevnom disku
		 * a len v rámci aktuálneho umiestnenia – neprehľadáva cestu
		 * <code>classpath</code> ani balíček {@code .jar}. V prípade
		 * potreby môžete použiť metódu {@link #kdeJeSúbor(String)
		 * kdeJeSúbor}, ktorá uvedené umiestnenia prehľadáva, a ktorá
		 * v prípade nenájdenia súboru vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 */
		public static boolean existuje(String názov)
		{ return new File(názov).exists(); }

		/**
		 * <p>Overí, či súbor alebo priečinok so zadaným názvom jestvuje.</p>
		 * 
		 * <p>Metóda overuje len jestvovanie reálnych súborov na pevnom
		 * disku a len v rámci aktuálneho umiestnenia (resp. na presne
		 * zadanej ceste) – neprehľadáva cestu <code>classpath</code> ani
		 * balíček {@code .jar}. V prípade potreby môžete použiť metódu
		 * {@link #kdeJeSúbor(String) kdeJeSúbor}, ktorá uvedené
		 * umiestnenia prehľadáva, a ktorá v prípade nenájdenia súboru
		 * vráti hodnotu {@code valnull}.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} – áno; {@code valfalse} – nie
		 * 
		 * @see #kdeJeSúbor(String)
		 */
		public static boolean jestvuje(String názov)
		{ return new File(názov).exists(); }

		// Vytvorenie priečinka

		/**
		 * <p>Vytvorí nový priečinok so zadaným názvom buď v aktuálnom
		 * priečinku, alebo na zadanej ceste (pripojenej pred názvom).
		 * Ak je parameter ajRodičov rovný {@code valtrue}, tak sa metóda
		 * pokúsi vytvoriť aj rodičovské priečinky, ktoré nejestvujú.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @param ajRodičov určuje, či sa má metóda pokúsiť vytvoriť aj
		 *     prípadných rodičov
		 * @return {@code valtrue} vtedy a len vtedy, ak bol priečinok
		 *     (vrátane prípadných rodičovských priečinkov) úspešne vytvorený
		 *     úspešne, inak {@code valfalse}
		 */
		public static boolean vytvorPriečinok(String názov, boolean ajRodičov)
		{ return ajRodičov ? new File(názov).mkdirs() : new File(názov).mkdir(); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String, boolean) vytvorPriečinok}.</p> */
		public static boolean vytvorPriecinok(String názov, boolean ajRodičov)
		{ return vytvorPriečinok(názov, ajRodičov); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String, boolean) vytvorPriečinok}.</p> */
		public static boolean novýPriečinok(String názov, boolean ajRodičov)
		{ return vytvorPriečinok(názov, ajRodičov); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String, boolean) vytvorPriečinok}.</p> */
		public static boolean novyPriecinok(String názov, boolean ajRodičov)
		{ return vytvorPriečinok(názov, ajRodičov); }

		/**
		 * <p>Vytvorí nový priečinok so zadaným názvom buď v aktuálnom
		 * priečinku, alebo na zadanej ceste (pripojenej pred názvom).</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} vtedy a len vtedy, ak bol priečinok
		 *     úspešne vytvorený úspešne, inak {@code valfalse}
		 */
		public static boolean vytvorPriečinok(String názov)
		{ return new File(názov).mkdir(); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String) vytvorPriečinok}.</p> */
		public static boolean vytvorPriecinok(String názov)
		{ return new File(názov).mkdir(); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String) vytvorPriečinok}.</p> */
		public static boolean novýPriečinok(String názov)
		{ return new File(názov).mkdir(); }

		/** <p><a class="alias"></a> Alias pre {@link #vytvorPriečinok(String) vytvorPriečinok}.</p> */
		public static boolean novyPriecinok(String názov)
		{ return new File(názov).mkdir(); }

		// Overenie, či ide o súbor alebo priečinok

		/**
		 * <p>Overí, či zadaný názov určuje obyčajný súbor. Obyčajný súbor
		 * znamená nie priečinok, ani iná špeciálna položka v rámci
		 * súborového systému.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} v prípade, že súbor so zadaným názvom
		 *     jestvuje a ide o normálny súbor, inak {@code valfalse}
		 */
		public static boolean jeSúbor(String názov)
		{ return new File(názov).isFile(); }

		/** <p><a class="alias"></a> Alias pre {@link #jeSúbor(String) jeSúbor}.</p> */
		public static boolean jeSubor(String názov)
		{ return new File(názov).isFile(); }

		/**
		 * <p>Overí, či zadaný názov označuje priečinok.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} v prípade, že položka so zadaným názvom
		 *     jestvuje a je to priečinok, inak {@code valfalse}
		 */
		public static boolean jePriečinok(String názov)
		{ return new File(názov).isDirectory(); }

		/** <p><a class="alias"></a> Alias pre {@link #jePriečinok(String) jePriečinok}.</p> */
		public static boolean jePriecinok(String názov)
		{ return new File(názov).isDirectory(); }

		// Porovnanie, kopírovanie, pripájanie, presun, mazanie súborov

		/**
		 * <p>Porovná obsah dvoch súborov. Oba názvy musia označovať súbory,
		 * inak vznikne výnimka. Ak obidva súbory nejestvujú, je to
		 * vyhodnotené ako zhoda. Ak nejestvuje iba jeden zo súborov, vznikne
		 * výnimka, preto musí byť príkaz uzavretý do bloku {@code try-catch}
		 * (pozri napríklad príklad v opise metódy {@link #kopíruj(String,
		 * String, boolean) kopíruj}). Ak obidva súbory jestvujú, tak sú
		 * porovnané a v prípade ich zhody je návratová hodnota rovná
		 * {@code valtrue}, inak je rovná {@code valfalse}.</p>
		 * 
		 * <p>V rámci obsluhy udalostí je k dispozícii reakcia – {@link 
		 * ObsluhaUdalostí#sekvencia(int, Object, Object, long, long)
		 * sekvencia} (s prislúchajúcim {@linkplain GRobot#sekvencia(int,
		 * Object, Object, long, long) variantom pre jednotlivé roboty}).
		 * Počas procesu porovnávania je uvedená reakcia spúšťaná (samozrejme
		 * aj so všetkými jej variantmi) s kódom spracovania
		 * {@link Konštanty#POROVNANIE_SÚBOROV POROVNANIE_SÚBOROV}.</p>
		 * 
		 * @param názov1 názov (a cesta) prvého súboru
		 * @param názov2 názov (a cesta) druhého súboru
		 * @return {@code valtrue} v prípade, že sú súbory zhodné,
		 *     inak {@code valfalse}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException jeden zo zadaných objektov nie je súbor
		 *     alebo nejestvuje len jeden zo súborov (pričom druhý áno)
		 */
		public static boolean porovnaj(String názov1, String názov2)
			throws IOException
		{
			/*NO!
			 * @exception FileNotFoundException táto výnimka by nemala
			 *     vzniknúť – prítomnosť súborov je overená ešte pred pokusom
			 *     o ich otvorenie, teoreticky je však technicky možné, že by
			 *     bol niektorý zo súborov vymazaný až po overení jeho
			 *     jestvovania, tesne pred pokusom o jeho otvorenie; vtedy má
			 *     šancu vzniknúť táto výnimka
			*/
			File súbor1 = nájdiSúbor(názov1);
			File súbor2 = nájdiSúbor(názov2);

			InputStream čítanie1 = null;
			InputStream čítanie2 = null;

			boolean obidvaSúSúbory = true;

			if (null != súbor1)
			{
				if (!súbor1.isFile())
					throw new GRobotException(
						"Prvý súbor „" + názov1 + "“ nie je súbor.",
						"firstObjectNotFile", názov1);

				čítanie1 = new FileInputStream(súbor1);
			}
			else
			{
				try
				{
					// Pokúsi sa prečítať súbor z .jar súboru (ako zdroj)
					čítanie1 = dajVstupnýPrúdZdroja(názov1);
					obidvaSúSúbory = false;
				}
				catch (Exception e)
				{
					čítanie1 = null;
				}
			}

			if (null != súbor2)
			{
				if (!súbor2.isFile())
					throw new GRobotException(
						"Druhý súbor „" + názov2 + "“ nie je súbor.",
						"secondObjectNotFile", názov2);

				čítanie2 = new FileInputStream(súbor2);
			}
			else
			{
				try
				{
					// Pokúsi sa prečítať súbor z .jar súboru (ako zdroj)
					čítanie2 = dajVstupnýPrúdZdroja(názov2);
					obidvaSúSúbory = false;
				}
				catch (Exception e)
				{
					čítanie2 = null;
				}
			}

			if (null == čítanie1)
			{
				if (null == čítanie2)
				{
					// Ak obidva súbory nejestvujú, znamená to zhodu
					return true;
				}

				throw new GRobotException(
					"Prvý súbor „" + názov1 + "“ nejestvuje.",
					"firstFileNotExists", názov1, new FileNotFoundException());
			}

			if (null == čítanie2)
			{
				throw new GRobotException(
					"Druhý súbor „" + názov2 + "“ nejestvuje.",
					"secondFileNotExists", názov2, new FileNotFoundException());
			}

			if (obidvaSúSúbory)
			{
				if (súbor1.length() != súbor2.length())
				{
					čítanie1.close();
					čítanie2.close();
					return false;
				}

				if (súbor1.getCanonicalFile().equals(súbor2.getCanonicalFile()))
				{
					čítanie1.close();
					čítanie2.close();
					return true;
				}
			}

			byte[] zásobník1 = new byte[32768];
			byte[] zásobník2 = new byte[32768];
			boolean rovnakýObsah = true;

			int dĺžka1 = čítanie1.read(zásobník1);
			int dĺžka2 = čítanie2.read(zásobník2);
			int ukaz1 = 0, ukaz2 = 0;
			long fáza = dĺžka1, celkovo = -1;

			try
			{
				if (null != súbor1)
					celkovo = súbor1.length();
				else
					celkovo = dajVeľkosťZdroja(názov1);
			}
			catch (Exception e)
			{
				// Ignorujeme… (poznať veľkosť nie je až také dôležité)
			}

			if (null != ObsluhaUdalostí.počúvadlo)
				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					ObsluhaUdalostí.počúvadlo.sekvencia(
						POROVNANIE_SÚBOROV, názov1, názov2,
						fáza, celkovo);
				}

			synchronized (ÚdajeUdalostí.zámokUdalostí)
			{
				for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
				{
					počúvajúci.sekvencia(POROVNANIE_SÚBOROV,
						názov1, názov2, fáza, celkovo);
				}
			}

			// Ide o to, že zásobníky sa môžu plniť nerovnako veľkými časťami
			// údajov. Napriek tomu môžu byť súbory rovnaké!
			if (dĺžka1 > 0 || dĺžka2 > 0)
			{
				while (ukaz1 < dĺžka1 && ukaz2 < dĺžka2)
				{
					if (zásobník1[ukaz1] != zásobník2[ukaz2])
					{
						rovnakýObsah = false;
						break;
					}

					++ukaz1; ++ukaz2;

					if (ukaz1 >= dĺžka1)
					{
						dĺžka1 = čítanie1.read(zásobník1);
						ukaz1 = 0;

						fáza += dĺžka1;

						if (null != ObsluhaUdalostí.počúvadlo)
							synchronized (ÚdajeUdalostí.zámokUdalostí)
							{
								ObsluhaUdalostí.počúvadlo.sekvencia(
									POROVNANIE_SÚBOROV, názov1, názov2,
									fáza, celkovo);
							}

						synchronized (ÚdajeUdalostí.zámokUdalostí)
						{
							for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
							{
								počúvajúci.sekvencia(POROVNANIE_SÚBOROV,
									názov1, názov2, fáza, celkovo);
							}
						}
					}

					if (ukaz2 >= dĺžka2)
					{
						dĺžka2 = čítanie2.read(zásobník2);
						ukaz2 = 0;
					}
				}

				if (rovnakýObsah && (dĺžka1 > 0 || dĺžka2 > 0))
					rovnakýObsah = false;
			}

			čítanie1.close();
			čítanie2.close();
			return rovnakýObsah;
		}

		/**
		 * <p>Skopíruje zdrojový súbor do cieľového súboru. Oba názvy musia
		 * označovať súbory, inak vznikne výnimka. Zdrojový súbor musí
		 * jestvovať a ak nie je argument {@code prepísať} rovný {@code 
		 * valtrue}, cieľový súbor nesmie jestvovať. Počas procesu
		 * kopírovania môže vzniknúť výnimka, preto musí byť príkaz uzavretý
		 * do bloku {@code try-catch}:</p>
		 * 
		 * <pre CLASS="example">
			{@link String String} zdroj = {@code srg"citajma.txt"};
			{@link String String} cieľ = {@code srg"zaloha_citajma.txt"};

			{@code kwdtry}
			{
				{@link Súbor Súbor}.{@code currkopíruj}(zdroj, cieľ, {@code valtrue});
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Súbor „"} + zdroj + {@code srg"“ úspešne skopírovaný do „"} + cieľ + {@code srg"“…"});
			}
			{@code kwdcatch} ({@link Exception Exception} e)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}(e.{@link Exception#getMessage() getMessage}());
			}
			</pre>
		 * 
		 * <p>V rámci obsluhy udalostí je k dispozícii reakcia – {@link 
		 * ObsluhaUdalostí#sekvencia(int, Object, Object, long, long)
		 * sekvencia} (s prislúchajúcim {@linkplain GRobot#sekvencia(int,
		 * Object, Object, long, long) variantom pre jednotlivé roboty}).
		 * Počas procesu kopírovania je uvedená reakcia spúšťaná (samozrejme
		 * aj so všetkými jej variantmi) s kódom spracovania {@link 
		 * Konštanty#KOPÍROVANIE_SÚBOROV KOPÍROVANIE_SÚBOROV}.</p>
		 * 
		 * @param zdroj názov (a cesta) zdrojového súboru
		 * @param cieľ názov (a cesta) cieľového súboru
		 * @param prepísať ak je {@code valtrue}, cieľový súbor bude
		 *     prepísaný (ak jestvuje)
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak argument prepísať je {@code valfalse}
		 *     a cieľový súbor jestvuje; ak cieľový súbor nemôže byť
		 *     prepísaný, lebo nie je súborom; ak zdrojový súbor nie je
		 *     súborom; ak zdrojový súbor nejestvuje
		 */
		public static void kopíruj(String zdroj, String cieľ, boolean prepísať)
			throws /*FileNotFoundException,*/ IOException
		{
			// Táto metóda bola mnoho ráz opravovaná. Na prvý pohľad má robiť
			// primitívny úkon – skopírovať všetky bajty z jedného miesta na
			// druhé. Je to vec, ktorú riešilo snáď milión programátorov
			// milión ráz. Problém pri tvorbe tejto metódy bol ten, že má byť
			// schopná automaticky rozpoznať, či zdroj (zadaný ako reťazec)
			// je jestvujúci súbor na pevnom disku alebo zdroj (chápaný ako
			// objekt) prítomný v JAR balíčku, v ktorom je práve umiestnená
			// preložená kópia programovacieho rámca (napr. v nejakom
			// projekte). Aj keď už bolo všetko otestované a fungovalo, do
			// metódy boli vnesené ďalšie chyby počas rozširovania možností
			// programovacieho rámca. Napríklad vznikla reakcia sekvencia,
			// všetko bolo pripravené, metóda otestovaná, ale na testovanie
			// v JAR balíčku sa akosi zabudlo a chyba sa prejavila až po
			// dlhšom čase, ked po prenesení programovacieho rámca do JAR
			// balíčka zarazu určitý projekt nefungoval – hľadanie chyby však
			// nebolo jednoduché a konečné zistenie, že chyba je v skutočnosti
			// v tejto metóde bolo prekvapujúce…

			/* @exception FileNotFoundException */
			File súborZdroja = nájdiSúbor(zdroj);
			File súborCieľa = new File(cieľ);

			if (!prepísať && súborCieľa.exists())
				throw new GRobotException(
					"Cieľový súbor „" + cieľ + "“ už jestvuje.",
					"targetFileExists", cieľ);
			if (súborCieľa.exists() && !súborCieľa.isFile())
				throw new GRobotException(
					"Cieľový súbor „" + cieľ + "“ nie je súbor.",
					"targetObjectNotFile", cieľ);

			InputStream čítanie;

			if (null != súborZdroja)
			{
				if (!súborZdroja.isFile())
					throw new GRobotException(
						"Zdrojový súbor „" + zdroj + "“ nie je súbor.",
						"sourceObjectNotFile", zdroj);

				čítanie = new FileInputStream(súborZdroja);
			}
			else
			{
				try
				{
					// Pokúsi sa prečítať súbor z .jar súboru (ako zdroj)
					čítanie = dajVstupnýPrúdZdroja(zdroj);
				}
				catch (Exception e)
				{
					čítanie = null;
				}
			}

			if (null == čítanie)
			{
				throw new GRobotException(
					"Zdrojový súbor „" + zdroj + "“ nejestvuje.",
					"sourceFileNotExists", zdroj,
					new FileNotFoundException());
			}


			// V tejto fáze súbor vytvoríme (ak nejestvuje) alebo
			// prepíšeme (ak jestvuje):
			FileOutputStream zápis = new FileOutputStream(súborCieľa);

			byte[] zásobník = new byte[32768];
			int dĺžka;
			long fáza = 0, celkovo = -1;

			try
			{
				if (null != súborZdroja)
					celkovo = súborZdroja.length();
				else
					celkovo = dajVeľkosťZdroja(zdroj);
			}
			catch (Exception e)
			{
				// Ignorujeme… (poznať veľkosť nie je až také dôležité –
				// slúži len na informovanie prostredníctvom reakcie
				// „sekvencia“)
			}

			while ((dĺžka = čítanie.read(zásobník)) > 0)
			{
				zápis.write(zásobník, 0, dĺžka);
				fáza += dĺžka;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.sekvencia(
							KOPÍROVANIE_SÚBOROV, zdroj, cieľ,
							fáza, celkovo);
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
					{
						počúvajúci.sekvencia(KOPÍROVANIE_SÚBOROV,
							zdroj, cieľ, fáza, celkovo);
					}
				}
			}

			čítanie.close();
			zápis.close();

			if (null != súborZdroja && súborZdroja.exists())
				súborCieľa.setLastModified(súborZdroja.lastModified());
			else
			{
				URL url = nájdiZdroj(zdroj);
				long čas = url.openConnection().getLastModified();
				if (0 != čas) súborCieľa.setLastModified(čas);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #kopíruj(String, String, boolean) kopíruj}.</p> */
		public static void kopiruj(String zdroj, String cieľ,
			boolean prepísať) throws /*FileNotFoundException,*/ IOException
		{ kopíruj(zdroj, cieľ, prepísať); }

		/**
		 * <p>Skopíruje zdrojový súbor do cieľového súboru. Oba názvy musia
		 * označovať súbory, inak vznikne výnimka. Zdrojový súbor musí
		 * jestvovať a cieľový súbor nesmie jestvovať. Počas procesu
		 * kopírovania môže vzniknúť výnimka, preto musí byť príkaz
		 * uzavretý do bloku {@code try-catch} (pozri príklad pri {@link 
		 * #kopíruj(String, String, boolean) kopíruj(zdroj, cieľ,
		 * prepísať}).</p>
		 * 
		 * @param zdroj názov (a cesta) zdrojového súboru
		 * @param cieľ názov (a cesta) cieľového súboru
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public static void kopíruj(String zdroj, String cieľ)
			throws /*FileNotFoundException,*/ IOException
		{ kopíruj(zdroj, cieľ, false); }

		/** <p><a class="alias"></a> Alias pre {@link #kopíruj(String, String) kopíruj}.</p> */
		public static void kopiruj(String zdroj, String cieľ)
			throws /*FileNotFoundException,*/ IOException
		{ kopíruj(zdroj, cieľ, false); }

		/**
		 * <p>Pripojí zdrojový súbor k cieľovému súboru. Oba názvy musia
		 * označovať súbory a oba musia jestvovať, inak vznikne výnimka.
		 * Počas procesu pripájania môže vzniknúť výnimka, preto musí byť
		 * príkaz uzavretý do bloku {@code try-catch} (podobne ako ostatné
		 * príkazy tejto triedy)…</p>
		 * 
		 * <p>V rámci obsluhy udalostí je k dispozícii reakcia – {@link 
		 * ObsluhaUdalostí#sekvencia(int, Object, Object, long, long)
		 * sekvencia} (s prislúchajúcim {@linkplain GRobot#sekvencia(int,
		 * Object, Object, long, long) variantom pre jednotlivé roboty}).
		 * Počas procesu pripájania je uvedená reakcia spúšťaná (samozrejme
		 * aj so všetkými jej variantmi) s kódom spracovania {@link 
		 * Konštanty#PRIPÁJANIE_SÚBOROV PRIPÁJANIE_SÚBOROV}.</p>
		 * 
		 * @param zdroj názov (a cesta) zdrojového súboru
		 * @param cieľ názov (a cesta) cieľového súboru
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak cieľový súbor nemôže byť použitý,
		 *     lebo nie je súborom; ak zdrojový súbor nie je súborom; alebo
		 *     ak zdrojový súbor nejestvuje
		 */
		public static void pripoj(String zdroj, String cieľ) throws IOException
		{
			File súborZdroja = nájdiSúbor(zdroj);
			File súborCieľa = new File(cieľ);

			if (súborCieľa.exists() && !súborCieľa.isFile())
				throw new GRobotException(
					"Cieľový súbor „" + cieľ + "“ nie je súbor.",
					"targetObjectNotFile", cieľ);

			InputStream čítanie;

			if (null != súborZdroja)
			{
				if (!súborZdroja.isFile())
					throw new GRobotException(
						"Zdrojový súbor „" + zdroj + "“ nie je súbor.",
						"sourceObjectNotFile", zdroj);

				čítanie = new FileInputStream(súborZdroja);
			}
			else
			{
				try
				{
					// Pokúsi sa prečítať súbor z .jar súboru (ako zdroj)
					čítanie = dajVstupnýPrúdZdroja(zdroj);
				}
				catch (Exception e)
				{
					čítanie = null;
				}
			}

			if (null == čítanie)
			{
				throw new GRobotException(
					"Zdrojový súbor „" + zdroj + "“ nejestvuje.",
					"sourceFileNotExists", zdroj,
					new FileNotFoundException());
			}


			// Súbor chceme pripojiť:
			FileOutputStream zápis = new FileOutputStream(súborCieľa, true);

			byte[] zásobník = new byte[32768];
			int dĺžka;
			long fáza = 0, celkovo = -1;

			try
			{
				if (null != súborZdroja)
					celkovo = súborZdroja.length();
				else
					celkovo = dajVeľkosťZdroja(zdroj);
			}
			catch (Exception e)
			{
				// Ignorujeme… (poznať veľkosť nie je až také dôležité –
				// slúži len na informovanie prostredníctvom reakcie
				// „sekvencia“)
			}

			while ((dĺžka = čítanie.read(zásobník)) > 0)
			{
				zápis.write(zásobník, 0, dĺžka);
				fáza += dĺžka;

				if (null != ObsluhaUdalostí.počúvadlo)
					synchronized (ÚdajeUdalostí.zámokUdalostí)
					{
						ObsluhaUdalostí.počúvadlo.sekvencia(
							PRIPÁJANIE_SÚBOROV, zdroj, cieľ,
							fáza, celkovo);
					}

				synchronized (ÚdajeUdalostí.zámokUdalostí)
				{
					for (GRobot počúvajúci : GRobot.počúvajúciSúbory)
					{
						počúvajúci.sekvencia(PRIPÁJANIE_SÚBOROV,
							zdroj, cieľ, fáza, celkovo);
					}
				}
			}

			čítanie.close();
			zápis.close();

			// Pri pripájaní ponecháme dátum poslednej úpravy tak, ako bol
			// nastavený systémom. Nasledujúci kód je aktívny v metóde
			// „kopíruj,“ z ktorej bol prevzatý kód tejto metódy:
			// if (null != súborZdroja && súborZdroja.exists())
			//	súborCieľa.setLastModified(súborZdroja.lastModified());
			// else
			// {
			// 	URL url = nájdiZdroj(zdroj);
			// 	long čas = url.openConnection().getLastModified();
			// 	if (0 != čas) súborCieľa.setLastModified(čas);
			// }
		}

		/**
		 * <p>Premenuje (presunie) zdrojový súbor alebo priečinok na cieľový
		 * súbor alebo priečinok (do cieľovej cesty). Počas vykonania tejto
		 * operácie musí byť braných do úvahy veľa platformovo závislých
		 * aspektov. Premenovanie (presun) môže zlyhať z mnohých dôvodov:
		 * presun medzi rôznymi súborovými systémami, pokus o presun
		 * netriviálnej položky, pokus o premenovanie na názov jestvujúceho
		 * súboru… Vždy by ste mali overiť návratovú hodnotu tejto metódy,
		 * aby ste si mohli byť istí, že operácia prebehla úspešne.</p>
		 * 
		 * @param zdroj názov (a cesta) zdrojového súboru alebo priečinka
		 * @param cieľ názov (a cesta) cieľového súboru alebo priečinka
		 * @return {@code valtrue} vtedy a len vtedy, ak operácia
		 *     premenovania prebehla úspešne, inak {@code valfalse}
		 */
		public static boolean premenuj(String zdroj, String cieľ)
		{ return new File(zdroj).renameTo(new File(cieľ)); }

		/**
		 * <p>Presunie zdrojový súbor alebo priečinok do cieľovej cesty. Počas
		 * vykonania tejto operácie musí byť braných do úvahy veľa
		 * platformovo závislých aspektov. Presun môže zlyhať z mnohých
		 * dôvodov: presun medzi rôznymi súborovými systémami, pokus
		 * o presun netriviálnej položky, pokus o nahradenie jestvujúcej
		 * položky pri presune… Vždy by ste mali overiť návratovú hodnotu
		 * tejto metódy, aby ste si mohli byť istí, že operácia prebehla
		 * úspešne.</p>
		 * 
		 * @param zdroj názov (a cesta) zdrojového súboru alebo priečinka
		 * @param cieľovýPriečinok cesta do cieľového priečinka
		 * @return {@code valtrue} vtedy a len vtedy, ak operácia presunu
		 *     prebehla úspešne, inak {@code valfalse}
		 */
		public static boolean presuň(String zdroj, String cieľovýPriečinok)
		{
			File zdrojováPoložka = new File(zdroj);
			File cieľováPoložka = new File(cieľovýPriečinok);

			return zdrojováPoložka.renameTo(
				new File(cieľováPoložka, zdrojováPoložka.getName()));
		}

		/** <p><a class="alias"></a> Alias pre {@link #presuň(String, String) presuň}.</p> */
		public static boolean presun(String zdroj, String cieľovýPriečinok)
		{ return presuň(zdroj, cieľovýPriečinok); }

		/**
		 * <p>Vymaže súbor alebo priečinok so zadaným názvom. Ak názov
		 * označuje priečinok, tak určený priečinok musí byť prázdny, inak
		 * nebude môcť byť vymazaný.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return {@code valtrue} vtedy a len vtedy, ak operácia vymazania
		 *     prebehla úspešne, inak {@code valfalse}
		 */
		public static boolean vymaž(String názov)
		{ return new File(názov).delete(); }

		/** <p><a class="alias"></a> Alias pre {@link #vymaž(String) vymaž}.</p> */
		public static boolean vymaz(String názov)
		{ return new File(názov).delete(); }

		// Ďalšie vlastnosti

		/**
		 * <p>Zostaví reťazec dátumu v predvolenom formáte zo zadaného počtu
		 * milisekúnd počítaných od začiatku takzvanej epochy. Táto metóda je
		 * kópiou metódy {@link Archív#dátumNaReťazec(long) dátumNaReťazec}.
		 * Ďalšie detaily nájdete v jej opise.</p>
		 * 
		 * @param miliDátum dátum v milisekundách epochy
		 * @return reťazec dátumu v predvolenom formáte
		 * 
		 * @see #dátumNaReťazec(long, String)
		 * @see #reťazecNaDátum(String)
		 * @see #reťazecNaDátum(String, String)
		 * @see #naposledyUpravený(String)
		 */
		public static String dátumNaReťazec(long miliDátum)
		{
			return Archív.dátumNaReťazec(miliDátum);
		}

		/** <p><a class="alias"></a> Alias pre {@link #dátumNaReťazec(long) dátumNaReťazec}.</p> */
		public static String datumNaRetazec(long miliDátum)
		{ return dátumNaReťazec(miliDátum); }

		/**
		 * <p>Podľa zadaných údajov vytvorí reťazec dátumu. Prvý parameter je
		 * počet milisekúnd počítaných od začiatku takzvanej epochy a druhý
		 * parameter určuje požadovaný formát. Táto metóda je kópiou metódy
		 * {@link Archív#dátumNaReťazec(long, String) dátumNaReťazec}. Ďalšie
		 * detaily nájdete v jej opise.</p>
		 * 
		 * @param miliDátum dátum v milisekundách epochy
		 * @param formát reťazec určujúci formát dátumu
		 * @return reťazec dátumu v zadanom formáte
		 * 
		 * @see #dátumNaReťazec(long)
		 * @see #reťazecNaDátum(String)
		 * @see #reťazecNaDátum(String, String)
		 * @see #naposledyUpravený(String)
		 */
		public static String dátumNaReťazec(long miliDátum, String formát)
		{
			return Archív.dátumNaReťazec(miliDátum, formát);
		}

		/** <p><a class="alias"></a> Alias pre {@link #dátumNaReťazec(long, String) dátumNaReťazec}.</p> */
		public static String datumNaRetazec(long miliDátum, String formát)
		{ return dátumNaReťazec(miliDátum, formát); }

		/**
		 * <p>Vráti dátum v milisekundách počítaných od začiatku takzvanej
		 * epochy. Táto metóda je kópiou metódy {@link 
		 * Archív#reťazecNaDátum(String) reťazecNaDátum}. Ďalšie detaily
		 * nájdete v jej opise.</p>
		 * 
		 * @param dátum reťazec dátumu v predvolenom formáte
		 * @return dátum v milisekundách epochy
		 * 
		 * @see #dátumNaReťazec(long)
		 * @see #dátumNaReťazec(long, String)
		 * @see #reťazecNaDátum(String, String)
		 * @see #naposledyUpravený(String, long)
		 */
		public static long reťazecNaDátum(String dátum)
		{
			return Archív.reťazecNaDátum(dátum);
		}

		/** <p><a class="alias"></a> Alias pre {@link #reťazecNaDátum(String) reťazecNaDátum}.</p> */
		public static long retazecNaDatum(String dátum)
		{ return reťazecNaDátum(dátum); }

		/**
		 * <p>Vráti dátum v milisekundách počítaných od začiatku takzvanej
		 * epochy. Táto metóda je kópiou metódy {@link 
		 * Archív#reťazecNaDátum(String, String) reťazecNaDátum}. Ďalšie
		 * detaily nájdete v jej opise.</p>
		 * 
		 * @param dátum reťazec dátumu v predvolenom formáte
		 * @param formát očakávaný formát dátumu
		 * @return dátum v milisekundách epochy
		 * 
		 * @see #dátumNaReťazec(long)
		 * @see #dátumNaReťazec(long, String)
		 * @see #reťazecNaDátum(String)
		 * @see #naposledyUpravený(String, long)
		 */
		public static long reťazecNaDátum(String dátum, String formát)
		{
			return Archív.reťazecNaDátum(dátum, formát);
		}

		/** <p><a class="alias"></a> Alias pre {@link #reťazecNaDátum(String, String) reťazecNaDátum}.</p> */
		public static long retazecNaDatum(String dátum, String formát)
		{ return reťazecNaDátum(dátum, formát); }

		/**
		 * <p>Zistí dátum a čas poslednej úpravy zadaného súboru alebo
		 * priečinka. Hodnota je udaná v milisekundách od začiatku takzvanej
		 * epochy – polnoc 1. januára 1970, pričom v našom časovom pásme to
		 * znamená hodinový posun, čiže jednu hodinu v noci rovnakého
		 * dátumu.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @return celočíselná hodnota reprezentujúca čas poslednej úpravy
		 *     súboru; meraná je v milisekundách od začiatku epochy; ak súbor
		 *     nejestvuje, návratová hodnota je 0 (začiatok epochy)
		 * 
		 * @see #naposledyUpravený(String, long)
		 * @see #dátumNaReťazec(long)
		 * @see #dátumNaReťazec(long, String)
		 */
		public static long naposledyUpravený(String názov)
		{
			return new File(názov).lastModified();
		}

		/** <p><a class="alias"></a> Alias pre {@link #naposledyUpravený(String) naposledyUpravený}.</p> */
		public static long naposledyUpraveny(String názov) { return naposledyUpravený(názov); }

		/**
		 * <p>Nastaví dátum a čas poslednej úpravy zadaného súboru alebo
		 * priečinka. Hodnota je udaná v milisekundách od začiatku takzvanej
		 * epochy – polnoc 1. januára 1970, pričom v našom časovom pásme to
		 * znamená hodinový posun, čiže jednu hodinu v noci rovnakého
		 * dátumu.</p>
		 * 
		 * @param názov názov súboru alebo priečinka
		 * @param miliDátum celočíselná hodnota reprezentujúca dátum a čas
		 *     v milisekundách meraný od začiatku epochy
		 * 
		 * @see #naposledyUpravený(String)
		 * @see #reťazecNaDátum(String)
		 * @see #reťazecNaDátum(String, String)
		 */
		public static void naposledyUpravený(String názov, long miliDátum)
		{
			new File(názov).setLastModified(miliDátum);
		}

		/** <p><a class="alias"></a> Alias pre {@link #naposledyUpravený(String, long) naposledyUpravený}.</p> */
		public static void naposledyUpraveny(String názov, long miliDátum) { naposledyUpravený(názov, miliDátum); }

		/**
		 * <p>Zistí veľkosť zadaného súboru alebo zdroja. Ak súbor alebo zdroj
		 * nemohli byť nájdené, tak metóda vráti hodnotu −1.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Ak metóda {@link #jestvuje(String)
		 * jestvuje} vráti pre zadaný názov hodnotu {@code valfalse}, tak
		 * vrátená <b>platná</b> veľkosť je veľkosťou zdroja.</p>
		 * 
		 * @param názov názov súboru alebo zdroja
		 * @return celočíselná hodnota reprezentujúca veľkosť zadaného
		 *     súboru alebo zdroja
		 * 
		 * @see #kdeJeSúbor(String)
		 */
		public static long veľkosť(String názov)
		{
			try
			{
				File súbor = new File(názov);
				if (súbor.exists())
					return súbor.length();
				else
					return dajVeľkosťZdroja(názov);
			}
			catch (Exception e)
			{
				return -1L;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #veľkosť(String) veľkosť}.</p> */
		public static long velkost(String názov) { return veľkosť(názov); }

	// Menné priestory vlastností

		/**
		 * <p><a class="getter"></a> Zistí aktuálne používaný menný priestor
		 * na čítanie a zápis vlastností. Pre viac informácií o účele menného
		 * priestoru pozri {@link #mennýPriestorVlastností(String)
		 * mennýPriestorVlastností(String)}.</p>
		 * 
		 * @return vráti aktuálne používaný menný priestor v objekte typu
		 *     {@link java.lang.String String} alebo {@code valnull} ak nie
		 *     je používaný žiadny menný priestor
		 */
		public String mennýPriestorVlastností()
		{ return aktívnaSekcia.mennýPriestorVlastností; }

		/** <p><a class="alias"></a> Alias pre {@link #mennýPriestorVlastností() mennýPriestorVlastností}.</p> */
		public String mennyPriestorVlastnosti()
		{ return aktívnaSekcia.mennýPriestorVlastností; }

		/**
		 * <p><a class="setter"></a> Stanoví nový menný priestor na čítanie
		 * a zápis vlastností.</p>
		 * 
		 * <p>Menný priestor slúži na oddelenie určitej skupiny vlastností od
		 * ostatných vlastností alebo skupín vlastností. Vďaka nemu je
		 * napríklad možné zapisovať vlastnosti s rovnakým názvom pre rôzne
		 * inštancie objektov.</p>
		 * 
		 * <p>Menný priestor nesmie byť prázdny, nesmie sa začínať alebo
		 * končiť bodkou a nesmie obsahovať znak rovná sa. Ak chceme zrušiť
		 * používanie menného priestoru, treba volať túto metódu s argumentom
		 * {@code valnull}.</p>
		 * 
		 * <p>Vo všeobecnosti menný priestor buď je, alebo nie je nastavený.
		 * V súčasnej verzii rámca je však implementovaný zásobník menných
		 * priestorov a menné priestory je možné s jednoduchosťou {@linkplain 
		 * #vnorMennýPriestorVlastností(String) vnárať} a {@linkplain 
		 * #vynorMennýPriestorVlastností() vynárať}.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param novýMennýPriestor názov nového menného priestoru alebo
		 *     {@code valnull} na zrušenie používania menného priestoru
		 * 
		 * @throws GRobotException ak názov menného priestoru nespĺňa
		 *     niektorú požiadavku
		 */
		public void mennýPriestorVlastností(String novýMennýPriestor)
		{
			if (null == novýMennýPriestor)
			{
				aktívnaSekcia.mennýPriestorVlastností = null;
				return;
			}

			if (novýMennýPriestor.equals(""))
				throw new GRobotException(
					"Menný priestor nesmie byť prázdny.", "namespaceEmpty");

			if (novýMennýPriestor.endsWith("."))
				throw new GRobotException(
					"Menný priestor sa nesmie končiť bodkou.",
					"namespaceEndsWithDot", novýMennýPriestor);

			if (novýMennýPriestor.startsWith(";"))
				throw new GRobotException(
					"Menný priestor sa nesmie začínať znakom komentára.",
					"namespaceStartsWithSemicolon", novýMennýPriestor);

			if (novýMennýPriestor.startsWith("."))
				throw new GRobotException(
					"Menný priestor sa nesmie začínať bodkou.",
					"namespaceStartsWithDot", novýMennýPriestor);

			if (-1 != novýMennýPriestor.indexOf('='))
				throw new GRobotException(
					"Menný priestor nesmie obsahovať znak rovná sa.",
					"namespaceContainsEquals", novýMennýPriestor);

			aktívnaSekcia.mennýPriestorVlastností = novýMennýPriestor;
		}

		/** <p><a class="alias"></a> Alias pre {@link #mennýPriestorVlastností(String) mennýPriestorVlastností}.</p> */
		public void mennyPriestorVlastnosti(String novýMennýPriestor)
		{ mennýPriestorVlastností(novýMennýPriestor); }


		/**
		 * <p>Zmení menný priestor na čítanie a zápis vlastností tak, aby
		 * vznikol vnorený priestor podľa konvencie opísanej nižšie.
		 * Pozri aj informácie v opise metódy {@link 
		 * #mennýPriestorVlastností(String) mennýPriestorVlastností}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Každá {@linkplain 
		 * #aktivujSekciu(String) sekcia} disponuje vlastným zásobníkom
		 * menných priestorov</p>
		 * 
		 * <p>Ak je namiesto názvu vnoreného menného priestoru zadaná
		 * hodnota {@code valnull}, tak je aktuálny menný priestor
		 * zálohovaný do vnútorného zásobníka a zostáva nezmenený.
		 * (Je to ekvivalentné volaniu metódy {@link 
		 * #vnorMennýPriestorVlastností() vnorMennýPriestorVlastností} bez
		 * parametra.) Obnoviť ho môžete volaním metódy {@link 
		 * #vynorMennýPriestorVlastností() vynorMennýPriestorVlastností}.
		 * Až do volania uvedenej metódy môžete ľubovoľne meniť menný
		 * priestor metódou {@link #mennýPriestorVlastností(String)
		 * mennýPriestorVlastností} a nakoniec jej volaním obnoviť posledný
		 * zálohovaný stav.</p>
		 * 
		 * <p>Ak je zadaný korektný názov vnoreného menného priestoru,
		 * tak je okrem zálohovania pozmenený aktuálny priestor takto:</p>
		 * 
		 * <ol>
		 * <li>Ak predtým nebol aktívny žiadny menný priestor, tak zadaný
		 * menný priestor sa stáva hlavným menným priestorom.</li>
		 * <li>Ak bol predtým aktívny ľubovoľný menný priestor, tak je
		 * vytvorený vnorený menný priestor podľa konvencie: <em>«aktuálny
		 * menný priestor»</em><code>.</code><em>«vnorený menný
		 * priestor»</em>; príklad:
		 * <code>starýPriestor.vnorenýPriestor</code>.</li>
		 * </ol>
		 * 
		 * <p>Obnoviť predchádzajúci menný priestor môžete aj v tomto prípade
		 * rovnako ako v prípade volania tejto metódy s hodnotou {@code 
		 * valnull}) – volaním metódy {@link #vynorMennýPriestorVlastností()
		 * vynorMennýPriestorVlastností}.</p>
		 * 
		 * <p> </p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Nasledujúci príklad
		 * staticky importuje triedy {@link Svet Svet}, {@link ÚdajeUdalostí
		 * ÚdajeUdalostí} a {@link Math Math}. Je to ukážka toho, ako je
		 * možné zjednodušiť písanie kódu v Jave. Negatívnym dôsledkom je
		 * nižšia čitateľnosť kódu, pretože statické metódy týchto tried
		 * (čo sú v prípade uvedených tried prakticky všetky metódy) nie sú
		 * jednoduchým spôsobom odlíšiteľné (nemajú prefix triedy).</p>
		 * 
		 * <p>Tento príklad ukazuje využitie vnárania menných priestorov
		 * v rámci jednoduchej aplikácie – skicára miestností.</p>
		 * 
		 * <pre CLASS="example">
			{@code comm// Upozornenie! Tento príklad staticky importuje triedy Svet, ÚdajeUdalostí}
			{@code comm// a Math. Je to ukážka toho, ako je možné zjednodušiť písanie kódu v Jave.}
			{@code comm// Negatívnym dôsledkom je nižšia čitateľnosť kódu, pretože statické metódy}
			{@code comm// týchto tried (čo sú v prípade uvedených tried prakticky všetky metódy)}
			{@code comm// nie sú jednoduchým spôsobom odlíšiteľné (nemajú prefix triedy).}

			{@code kwdimport} knižnica.*;
			{@code kwdimport} java.io.{@link IOException IOException};

			{@code kwdimport} {@code kwdstatic} java.lang.{@link Math Math}.*;
			{@code kwdimport} {@code kwdstatic} knižnica.{@link Svet Svet}.*;
			{@code kwdimport} {@code kwdstatic} knižnica.{@link ÚdajeUdalostí ÚdajeUdalostí}.*;

			{@code kwdpublic} {@code typeclass} SkicárMiestností {@code kwdextends} {@link GRobot GRobot}
			{
				{@code comm// Menovka miestnosti použitá v prípade, keď je názov miestnosti}
				{@code comm// rovný null. To môže nastať v prípade, že používateľ zruší zadávanie}
				{@code comm// názvu novej miestnosti – miestnosť je síce vytvorená, ale nemá meno.}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@link String String} bezMena = {@code srg"«bez mena»"};

				{@code comm// Trieda zhromažďujúca informácie o miestnosti.}
				{@code kwdprivate} {@code typeclass} Miestnosť {@code kwdextends} {@link Častica Častica}
				{
					{@code kwdpublic} {@link String String} meno = {@code valnull};
					{@code kwdpublic} {@code typedouble} šírka = {@code num10}, výška = {@code num10};

					{@code comm// Prečítanie záznamu o miestnosti z konfiguračného súboru.}
					{@code kwdpublic} {@code typevoid} čítajZoSúboru({@link Súbor Súbor} súbor, {@link String String} identifikátor)
						{@code kwdthrows} {@link IOException IOException}
					{
						{@link Súbor súbor}.{@code currvnorMennýPriestorVlastností}(identifikátor);
						{@code kwdtry}
						{
							meno = orežMeno({@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, String) čítajVlastnosť}({@code srg"meno"}, meno));
							x = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"x"}, x);
							y = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"y"}, y);
							uhol = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"uhol"}, uhol);
							šírka = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"šírka"}, šírka);
							výška = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Double) čítajVlastnosť}({@code srg"výška"}, výška);
						}
						{@code kwdfinally}
						{
							{@link Súbor súbor}.{@link Súbor#vynorMennýPriestorVlastností() vynorMennýPriestorVlastností}();
						}
					}

					{@code comm// Zapísanie záznamu o miestnosti do konfiguračného súboru.}
					{@code kwdpublic} {@code typevoid} uložDoSúboru({@link Súbor Súbor} súbor, {@link String String} identifikátor)
						{@code kwdthrows} {@link IOException IOException}
					{
						{@link Súbor súbor}.{@code currvnorMennýPriestorVlastností}(identifikátor);
						{@code kwdtry}
						{
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"meno"}, meno);
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"x"}, x);
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"y"}, y);
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"uhol"}, uhol);
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"šírka"}, šírka);
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"výška"}, výška);
						}
						{@code kwdfinally}
						{
							{@link Súbor súbor}.{@link Súbor#vynorMennýPriestorVlastností() vynorMennýPriestorVlastností}();
						}
					}
				}

				{@code comm// Zoznam pracovných režimov.}
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} PASÍVNY = {@code num0};
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} UPRAV_MIESTNOSŤ = {@code num1};
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} ŤAHAJ_MIESTNOSŤ = {@code num2};
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} PRESUŇ_MIESTNOSŤ = {@code num3};
				{@code kwdprivate} {@code kwdfinal} {@code kwdstatic} {@code typeint} PRESUŇ_VŠETKO = {@code num4};

				{@code comm// Aktuálny pracovný režim.}
				{@code kwdprivate} {@code typeint} režim = PASÍVNY;

				{@code comm// Aktuálna inštancia miestnosti.}
				{@code kwdprivate} Miestnosť miestnosť = {@code valnull};

				{@code comm// Pracovné súradnice myši.}
				{@code kwdprivate} {@code typedouble} myšX = {@code num0.0}, myšY = {@code num0.0};
				{@code kwdprivate} {@code typedouble} myšZX = {@code num0.0}, myšZY = {@code num0.0};

				{@code comm// Zoznam vytvorených miestností.}
				{@code kwdprivate} {@code kwdfinal} {@link Zoznam Zoznam}&lt;Miestnosť&gt; miestnosti = {@code kwdnew} {@link Zoznam#Zoznam() Zoznam}&lt;Miestnosť&gt;();

				{@code comm// Konštruktor.}
				{@code kwdprivate} SkicárMiestností()
				{
					{@code valsuper}({@code srg"Skicár miestností…"});
					{@link Svet#farbaPozadia(Color) farbaPozadia}({@link Farebnosť#šedá šedá});
					{@link Svet#farbaPlochy(Color) farbaPlochy}({@link Farebnosť#tmavošedá tmavošedá});
					{@link GRobot#písmo(String, double) písmo}({@code srg"Calibri"}, {@code num14});
					{@link GRobot#hrúbkaČiary(double) hrúbkaČiary}({@code num2.5});
					{@link GRobot#skry() skry}();

					{@code comm// Čítanie a zápis konfigurácie. Zapisuje sa počet miestností a každá}
					{@code comm// miestnosť je zapísaná do vnoreného menného priestoru s unikátnym}
					{@code comm// názvom (identifikátorom).}
					{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
					{
						{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}()
						{ {@code kwdreturn} {@code valtrue}; }

						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor)
							{@code kwdthrows} {@link IOException IOException}
						{
							{@code typeint} početMiestností = miestnosti.{@link GRobot#veľkosť() veľkosť}();
							{@link Súbor súbor}.{@link Súbor#zapíšPrázdnyRiadokVlastností() zapíšPrázdnyRiadokVlastností}();
							{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(String, Object) zapíšVlastnosť}({@code srg"početMiestností"}, početMiestností);

							{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početMiestností; ++i)
							{
								{@link Súbor súbor}.{@link Súbor#zapíšPrázdnyRiadokVlastností() zapíšPrázdnyRiadokVlastností}();
								miestnosti.{@link Zoznam#daj(int) daj}(i).uložDoSúboru(súbor,
									{@code srg"miestnosť["} + i + {@code srg"]"});
							}
						}

						{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor)
							{@code kwdthrows} {@link IOException IOException}
						{
							{@code typeint} početMiestností = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(String, Integer) čítajVlastnosť}(
								{@code srg"početMiestností"}, {@code num0});
							{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; početMiestností; ++i)
							{
								Miestnosť miestnosť = {@code kwdnew} Miestnosť();
								miestnosť.čítajZoSúboru(súbor, {@code srg"miestnosť["} + i + {@code srg"]"});
								miestnosti.{@link Zoznam#pridaj(Object) pridaj}(miestnosť);
							}
						}
					};
				}

				{@code comm// Pomocná metóda zaokrúhľujúca súradnice myši do mriežky.}
				{@code kwdprivate} {@code typevoid} zaokrúhliMyš()
				{
					myšX = {@link ÚdajeUdalostí#polohaMyšiX() polohaMyšiX}(); myšX += {@link Svet#najmenšieX() najmenšieX}();
					myšY = {@link ÚdajeUdalostí#polohaMyšiY() polohaMyšiY}(); myšY += {@link Svet#najmenšieY() najmenšieY}();

					myšX /= {@code num10}; myšX = ({@code typeint})myšX; myšX *= {@code num10};
					myšY /= {@code num10}; myšY = ({@code typeint})myšY; myšY *= {@code num10};

					myšX -= {@link Svet#najmenšieX() najmenšieX}();
					myšY -= {@link Svet#najmenšieY() najmenšieY}();
				}

				{@code comm// Pomocná metóda zbavujúca nadbytočných medzier názov miestnosti}
				{@code comm// (prípadne iného objektu).}
				{@code kwdprivate} {@link String String} orežMeno({@link String String} meno)
				{
					{@code kwdreturn} meno.{@link String#replaceAll(String, String) replaceAll}({@code srg"\\s+"}, {@code srg" "}).{@link String#trim() trim}();
				}

				{@code comm// Pomocná metóda slúžiaca na zadanie alebo úpravu názvu miestnosti.}
				{@code kwdprivate} {@code typevoid} premenuj(Miestnosť miestnosť)
				{
					{@code kwdif} ({@code valnull} != miestnosť)
					{
						{@link String String} meno = {@code valnull} == miestnosť.meno ? {@link Svet#zadajReťazec(String) zadajReťazec}(
							{@code srg"Zadaj meno miestnosti"}) : {@link Svet#upravReťazec(String, String) upravReťazec}(miestnosť.meno,
							{@code srg"Uprav meno miestnosti"});
						{@code kwdif} ({@code valnull} != meno) miestnosť.meno = orežMeno(meno);
					}
				}

				{@code comm// Časť ovládania myšou.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#ťahanieMyšou() ťahanieMyšou}()
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} PASÍVNY:
						{@code comm// Začatie ťahania myšou so stlačeným klávesom Ctrl v pasívnom}
						{@code comm// režime začne definíciu novej miestnosti.}
						{@code kwdif} ({@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
						{
							zaokrúhliMyš();
							miestnosť = {@code kwdnew} Miestnosť();
							miestnosť.{@link Častica#x x} = myšZX = myšX;
							miestnosť.{@link Častica#y y} = myšZY = myšY;
							miestnosti.{@link Zoznam#pridaj(Object) pridaj}(miestnosť);
							režim = ŤAHAJ_MIESTNOSŤ;
							{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						}

						{@code comm// Klasické ťahanie myšou v pasívnom režime buď ťahá všetko,}
						{@code comm// alebo miestnosť na polohe myši.}
						{@code kwdelse}
						{
							zaokrúhliMyš();
							myšZX = myšX;
							myšZY = myšY;
							režim = PRESUŇ_VŠETKO;
							{@code kwdfor} (Miestnosť miestnosť : miestnosti)
							{
								{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
								{@code kwdif} ({@link GRobot#myšVObdĺžniku(double, double) myšVObdĺžniku}({@code num5} + miestnosť.šírka, {@code num5} + miestnosť.výška))
								{
									{@code valthis}.miestnosť = miestnosť;
									režim = PRESUŇ_MIESTNOSŤ;
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
									{@code kwdbreak};
								}
							}
						}
						{@code kwdbreak};

					{@code kwdcase} UPRAV_MIESTNOSŤ:
						{@code comm// Ťahanie v režime úpravy miestnosti (t. j. keď je niektorá}
						{@code comm// miestnosť označená) ukončí tento režim a vykoná to isté ako}
						{@code comm// v pasívnom režime.}
						režim = PASÍVNY;
						miestnosť = {@code valnull};
						ťahanieMyšou();
						{@code kwdbreak};

					{@code kwdcase} ŤAHAJ_MIESTNOSŤ:
						{@code comm// Režim ťahania novej miestnosti – mení rozmery miestnosti.}
						zaokrúhliMyš();
						miestnosť.šírka = {@code num10} + {@link Math#abs(double) abs}(myšX &#45; myšZX);
						miestnosť.výška = {@code num10} + {@link Math#abs(double) abs}(myšY &#45; myšZY);
						{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						{@code kwdbreak};

					{@code kwdcase} PRESUŇ_MIESTNOSŤ:
						{@code comm// Režim presúvania alebo zmeny rozmeru označenej miestnosti.}
						zaokrúhliMyš();
						{@code kwdif} ({@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
						{
							{@code comm// (Stlačenie klávesu Shift znamená zmenu rozmerov miestnosti.)}
							{@code kwdif} (myšX &gt;= miestnosť.{@link Častica#x x})
								miestnosť.šírka += myšX &#45; myšZX;
							{@code kwdelse}
								miestnosť.šírka += myšZX &#45; myšX;

							{@code kwdif} (myšY &gt;= miestnosť.{@link Častica#y y})
								miestnosť.výška += myšY &#45; myšZY;
							{@code kwdelse}
								miestnosť.výška += myšZY &#45; myšY;
						}
						{@code kwdelse}
						{
							miestnosť.{@link Častica#x x} += myšX &#45; myšZX;
							miestnosť.{@link Častica#y y} += myšY &#45; myšZY;
						}
						myšZX = myšX;
						myšZY = myšY;
						{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						{@code kwdbreak};

					{@code kwdcase} PRESUŇ_VŠETKO:
						{@code comm// Režim presúvania všetkého definovaného…}
						zaokrúhliMyš();
						{@code kwdfor} (Miestnosť miestnosť : miestnosti)
						{
							miestnosť.{@link Častica#x x} += myšX &#45; myšZX;
							miestnosť.{@link Častica#y y} += myšY &#45; myšZY;
						}
						myšZX = myšX;
						myšZY = myšY;
						{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						{@code kwdbreak};
					}
				}

				{@code comm// Časť ovládania myšou – ukončuje ťahanie myšou.}
				{@code comm// (Kód je krátky a samovysvetľujúci.)}
				{@code kwd@}SuppressWarnings({@code srg"fallthrough"})
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}()
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} ŤAHAJ_MIESTNOSŤ:
						premenuj(miestnosť);
						{@code comm// no break – fallthrough}

					{@code kwdcase} PRESUŇ_MIESTNOSŤ:
						režim = UPRAV_MIESTNOSŤ;
						{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						{@code kwdbreak};

					{@code kwdcase} PRESUŇ_VŠETKO:
						režim = PASÍVNY;
						{@code kwdbreak};
					}
				}

				{@code comm// Časť ovládania myšou – klinutie sa vykoná vtedy, keď nenastalo}
				{@code comm// ťahanie myšou.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} PASÍVNY:
						{@code comm// Kliknutie v pasívnom režime so stlačeným klávesom Ctrl začne}
						{@code comm// definíciu novej miestnosti, ale na rozdiel od ťahania myšou si}
						{@code comm// najskôr vyžiada názov miestnosti.}
						{@code kwdif} ({@link ÚdajeUdalostí#myš() myš}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
						{
							zaokrúhliMyš();
							miestnosť = {@code kwdnew} Miestnosť();
							miestnosť.šírka = {@code num20};
							miestnosť.{@link Častica#x x} = myšX;
							miestnosť.{@link Častica#y y} = myšY;
							premenuj(miestnosť);
							miestnosti.{@link Zoznam#pridaj(Object) pridaj}(miestnosť);
							režim = UPRAV_MIESTNOSŤ;
							{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						}
						{@code kwdelse}
						{
							{@code comm// Kliknutie na niektorú miestnosť ju označí.}
							{@code kwdfor} (Miestnosť miestnosť : miestnosti)
							{
								{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
								{@code kwdif} ({@link GRobot#myšVObdĺžniku(double, double) myšVObdĺžniku}({@code num5} + miestnosť.šírka, {@code num5} + miestnosť.výška))
								{
									{@code valthis}.miestnosť = miestnosť;
									režim = UPRAV_MIESTNOSŤ;
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
									{@code kwdbreak};
								}
							}
						}
						{@code kwdbreak};

					{@code kwdcase} UPRAV_MIESTNOSŤ:
						{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
						{@code comm// Kliknutie na označenú miestnosť spustí úpravu jej názvu.}
						{@code kwdif} ({@link GRobot#myšVObdĺžniku(double, double) myšVObdĺžniku}({@code num5} + miestnosť.šírka, {@code num5} + miestnosť.výška))
						{
							premenuj(miestnosť);
						}
						{@code comm// Kliknutie mimo označenej miestnosti buď prejde do pasívneho}
						{@code comm// režimu, alebo označí inú miestnosť (podľa polohy myši).}
						{@code kwdelse}
						{
							režim = PASÍVNY;
							miestnosť = {@code valnull};
							{@code kwdfor} (Miestnosť miestnosť : miestnosti)
							{
								{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
								{@code kwdif} ({@link GRobot#myšVObdĺžniku(double, double) myšVObdĺžniku}({@code num5} + miestnosť.šírka, {@code num5} + miestnosť.výška))
								{
									{@code valthis}.miestnosť = miestnosť;
									režim = UPRAV_MIESTNOSŤ;
									{@code kwdbreak};
								}
							}
						}
						{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
						{@code kwdbreak};
					}
				}

				{@code comm// Časovač – implementuje prekresľovanie scény.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
				{
					{@code kwdif} ({@link Svet#neboloPrekreslené() neboloPrekreslené}())
					{
						{@link Plátno podlaha}.{@link Plátno#vymažGrafiku() vymažGrafiku}();

						{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
						{@code kwdfor} (Miestnosť miestnosť : miestnosti)
						{
							{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
							{@code kwdif} (miestnosť.šírka &lt; {@code num0.0}) miestnosť.šírka = {@code num0.0};
							{@code kwdif} (miestnosť.výška &lt; {@code num0.0}) miestnosť.výška = {@code num0.0};
							{@code kwdif} (miestnosť == {@code valthis}.miestnosť)
							{
								{@link GRobot#farba(Color) farba}({@link Farebnosť#oranžová oranžová});
								{@code kwdif} ({@code num0.0} == miestnosť.šírka && {@code num0.0} == miestnosť.výška)
									{@link GRobot#kruh(double) kruh}({@code num2.0});
								{@code kwdelse}
									{@link GRobot#kresliObdĺžnik(double, double) kresliObdĺžnik}(miestnosť.šírka, miestnosť.výška);
								{@link GRobot#farba(Color) farba}({@link Farebnosť#čierna čierna});
							}
							{@code kwdelse} {@code kwdif} ({@code num0.0} == miestnosť.šírka && {@code num0.0} == miestnosť.výška)
								{@link GRobot#kruh(double) kruh}({@code num2.0});
							{@code kwdelse}
								{@link GRobot#kresliObdĺžnik(double, double) kresliObdĺžnik}(miestnosť.šírka, miestnosť.výška);
						}

						{@link GRobot#farba(Color) farba}({@link Farebnosť#biela biela});
						{@code kwdfor} (Miestnosť miestnosť : miestnosti)
						{
							{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
							{@code kwdif} ({@code num0.0} &lt;= miestnosť.šírka && {@code num0.0} &lt;= miestnosť.výška)
								{@link GRobot#vyplňObdĺžnik(double, double) vyplňObdĺžnik}(miestnosť.šírka, miestnosť.výška);
						}

						{@link GRobot#farba(Color) farba}({@link Farebnosť#hnedá hnedá});
						{@code kwdfor} (Miestnosť miestnosť : miestnosti)
						{
							{@link GRobot#skočNa(Poloha) skočNa}(miestnosť);
							{@code kwdif} (miestnosť == {@code valthis}.miestnosť)
							{
								{@code kwdif} ({@code valnull} != miestnosť.meno)
								{
									{@link GRobot#farba(Color) farba}({@link Farebnosť#šedá šedá});
									{@code kwdif} ({@code num90.0} != miestnosť.{@link Častica#uhol uhol})
									{
										{@link GRobot#vľavo(double) vľavo}({@code num90} &#45; miestnosť.{@link Častica#uhol uhol});
										{@link GRobot#text(String) text}(miestnosť.meno);
										{@link GRobot#vpravo(double) vpravo}({@code num90} &#45; miestnosť.{@link Častica#uhol uhol});
									}
									{@code kwdelse} {@link GRobot#text(String) text}(miestnosť.meno);
									{@link GRobot#farba(Color) farba}({@link Farebnosť#hnedá hnedá});
								}
							}
							{@code kwdelse} {@code kwdif} ({@code num90.0} != miestnosť.{@link Častica#uhol uhol})
							{
								{@link GRobot#vľavo(double) vľavo}({@code num90} &#45; miestnosť.{@link Častica#uhol uhol});
								{@code kwdif} ({@code valnull} == miestnosť.meno)
									{@link GRobot#text(String) text}(bezMena); {@code kwdelse} {@link GRobot#text(String) text}(miestnosť.meno);
								{@link GRobot#vpravo(double) vpravo}({@code num90} &#45; miestnosť.{@link Častica#uhol uhol});
							}
							{@code kwdelse} {@code kwdif} ({@code valnull} == miestnosť.meno)
								{@link GRobot#text(String) text}(bezMena); {@code kwdelse} {@link GRobot#text(String) text}(miestnosť.meno);
						}

						{@link Svet#prekresli() prekresli}();
					}
				}

				{@code comm// Ovládanie klávesnicou.}
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#stlačenieKlávesu() stlačenieKlávesu}()
				{
					{@code kwdswitch} (režim)
					{
					{@code kwdcase} PASÍVNY:
						{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
						{
							{@code kwdswitch} ({@link ÚdajeUdalostí#kláves() kláves}())
							{
							{@code comm// Ctrl + kurzorové klávesy v pasívnom režime otáčajú scénu.}
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}:
								{
									{@code typedouble} a, b;
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										a = miestnosť.{@link Častica#x x};
										b = miestnosť.{@link Častica#y y};
										miestnosť.{@link Častica#x x} = b;
										miestnosť.{@link Častica#y y} = -a;
										a = miestnosť.šírka;
										b = miestnosť.výška;
										miestnosť.šírka = b;
										miestnosť.výška = a;
										{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
											miestnosť.{@link Častica#uhol uhol} += {@code num90};
									}
								}
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:
								{
									{@code typedouble} a, b;
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										a = miestnosť.{@link Častica#x x};
										b = miestnosť.{@link Častica#y y};
										miestnosť.{@link Častica#x x} = -b;
										miestnosť.{@link Častica#y y} = a;
										a = miestnosť.šírka;
										b = miestnosť.výška;
										miestnosť.šírka = b;
										miestnosť.výška = a;
										{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
											miestnosť.{@link Častica#uhol uhol} -= {@code num90};
									}
								}
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code comm// Ctrl + plus v pasívnom režime zväčší všetky miestnosti}
							{@code comm// a odsunie ich od stredu scény (pokus o jemnejšiu zmenu}
							{@code comm// mierky – s aktívnou mriežkou to nebolo možné implementovať}
							{@code comm// lepšie).}
							{@code comm// Ctrl + Shift + plus urobí opak – to isté, čo Ctrl + mínus.}
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_PLUS VK_PLUS}:
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_ADD VK_ADD}:
								{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										{@code kwdif} (miestnosť.{@link Častica#x x} &gt;= {@code num0.0})
											miestnosť.{@link Častica#x x} -= {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#x x} += {@code num20.0};
										{@code kwdif} (miestnosť.{@link Častica#y y} &gt;= {@code num0.0})
											miestnosť.{@link Častica#y y} -= {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#y y} += {@code num20.0};
										miestnosť.šírka -= {@code num10.0};
										miestnosť.výška -= {@code num10.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdelse}
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										{@code kwdif} (miestnosť.{@link Častica#x x} &gt;= {@code num0.0})
											miestnosť.{@link Častica#x x} += {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#x x} -= {@code num20.0};
										{@code kwdif} (miestnosť.{@link Častica#y y} &gt;= {@code num0.0})
											miestnosť.{@link Častica#y y} += {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#y y} -= {@code num20.0};
										miestnosť.šírka += {@code num10.0};
										miestnosť.výška += {@code num10.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdbreak};

							{@code comm// Ctrl + mínus v pasívnom režime zmenší všetky miestnosti}
							{@code comm// a priblíži ich k stredu scény…}
							{@code comm// Ctrl + Shift + mínus urobí opak – to isté, čo Ctrl + plus.}
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_MINUS VK_MINUS}:
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_SUBTRACT VK_SUBTRACT}:
								{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										{@code kwdif} (miestnosť.{@link Častica#x x} &gt;= {@code num0.0})
											miestnosť.{@link Častica#x x} += {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#x x} -= {@code num20.0};
										{@code kwdif} (miestnosť.{@link Častica#y y} &gt;= {@code num0.0})
											miestnosť.{@link Častica#y y} += {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#y y} -= {@code num20.0};
										miestnosť.šírka += {@code num10.0};
										miestnosť.výška += {@code num10.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdelse}
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										{@code kwdif} (miestnosť.{@link Častica#x x} &gt;= {@code num0.0})
											miestnosť.{@link Častica#x x} -= {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#x x} += {@code num20.0};
										{@code kwdif} (miestnosť.{@link Častica#y y} &gt;= {@code num0.0})
											miestnosť.{@link Častica#y y} -= {@code num20.0};
										{@code kwdelse}
											miestnosť.{@link Častica#y y} += {@code num20.0};
										miestnosť.šírka -= {@code num10.0};
										miestnosť.výška -= {@code num10.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdbreak};

							{@code comm// Ctrl + hviezdička v pasívnom režime zdvojnásobí}
							{@code comm// mierku scény.}
							{@code comm// Ctrl + Shift + hviezdička urobí opak, len s vyžiadaním}
							{@code comm// potvrdenia akcie – to isté, čo Ctrl + lomka.}
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_ASTERISK VK_ASTERISK}:
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_MULTIPLY VK_MULTIPLY}:
								{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
								{
									{@code kwdif} ({@link GRobot#ÁNO ÁNO} == {@link Svet#otázka(String) otázka}({@code srg"Určite chcete znížiť "} +
										{@code srg"rozlíšenie plánu?"}))
									{
										{@code kwdfor} (Miestnosť miestnosť : miestnosti)
										{
											miestnosť.{@link Častica#x x} /= {@code num2.0};
											miestnosť.{@link Častica#y y} /= {@code num2.0};
											miestnosť.šírka /= {@code num2.0};
											miestnosť.výška /= {@code num2.0};
										}
										{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
									}
								}
								{@code kwdelse}
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										miestnosť.{@link Častica#x x} *= {@code num2.0};
										miestnosť.{@link Častica#y y} *= {@code num2.0};
										miestnosť.šírka *= {@code num2.0};
										miestnosť.výška *= {@code num2.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdbreak};

							{@code comm// Ctrl + lomka v pasívnom režime zmenší mierku scény na}
							{@code comm// polovicu, avšak predtým si vyžiada potvrdenie akcie,}
							{@code comm// pretože hrozí deformácia scény mimo mriežku (toto nie}
							{@code comm// je v tejto verzii ošetrené).}
							{@code comm// Ctrl + Shift + lomka urobí opak – to isté, čo}
							{@code comm// Ctrl + hviezdička.}
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_SLASH VK_SLASH}:
							{@code kwdcase} {@link Kláves Kláves}.{@link java.awt.event.KeyEvent#VK_DIVIDE VK_DIVIDE}:
								{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
								{
									{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									{
										miestnosť.{@link Častica#x x} *= {@code num2.0};
										miestnosť.{@link Častica#y y} *= {@code num2.0};
										miestnosť.šírka *= {@code num2.0};
										miestnosť.výška *= {@code num2.0};
									}
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdelse}
								{
									{@code kwdif} ({@link GRobot#ÁNO ÁNO} == {@link Svet#otázka(String) otázka}({@code srg"Určite chcete znížiť "} +
										{@code srg"rozlíšenie plánu?"}))
									{
										{@code kwdfor} (Miestnosť miestnosť : miestnosti)
										{
											miestnosť.{@link Častica#x x} /= {@code num2.0};
											miestnosť.{@link Častica#y y} /= {@code num2.0};
											miestnosť.šírka /= {@code num2.0};
											miestnosť.výška /= {@code num2.0};
										}
										{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
									}
								}
								{@code kwdbreak};
							}
						}
						{@code comm// Kurzorové klávesy (bez Ctrl) v pasívnom režime presúvajú scénu.}
						{@code kwdelse}
						{
							{@code kwdswitch} ({@link ÚdajeUdalostí#kláves() kláves}())
							{
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#HORE HORE}:
								{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									miestnosť.{@link Častica#y y} += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DOLE DOLE}:
								{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									miestnosť.{@link Častica#y y} -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}:
								{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									miestnosť.{@link Častica#x x} += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:
								{@code kwdfor} (Miestnosť miestnosť : miestnosti)
									miestnosť.{@link Častica#x x} -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};
							}
						}
						{@code kwdbreak};

					{@code kwdcase} UPRAV_MIESTNOSŤ:
						{@code comm// Režim úpravy miestnosti…}
						{@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isShiftDown() isShiftDown}())
						{
							{@code comm// Shift + kurzorové klávesy menia veľkosť}
							{@code comm// označenej miestnosti.}
							{@code kwdswitch} ({@link ÚdajeUdalostí#kláves() kláves}())
							{
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#HORE HORE}:
								miestnosť.výška += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DOLE DOLE}:
								miestnosť.výška -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}:
								miestnosť.šírka += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:
								miestnosť.šírka -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};
							}
						}
						{@code kwdelse} {@code kwdif} ({@link ÚdajeUdalostí#klávesnica() klávesnica}().{@link java.awt.event.InputEvent#isControlDown() isControlDown}())
						{
							{@code comm// Ctrl + kurzorové klávesy menia orientáciu textu názvu}
							{@code comm// označenej miestnosti. Klávesy vpravo a vľavo otáčajú}
							{@code comm// text o 90°, klávesy hore a dole jemnejšie – o 15°.}
							{@code kwdswitch} ({@link ÚdajeUdalostí#kláves() kláves}())
							{
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#HORE HORE}:
								miestnosť.{@link Častica#uhol uhol} -= {@code num15};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DOLE DOLE}:
								miestnosť.{@link Častica#uhol uhol} += {@code num15};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}:
								miestnosť.{@link Častica#uhol uhol} += {@code num90};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:
								miestnosť.{@link Častica#uhol uhol} -= {@code num90};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};
							}
						}
						{@code kwdelse}
						{
							{@code comm// Kurzorové klávesy (bez Ctrl a Shift) presúvajú}
							{@code comm// označenú miestnosť.}
							{@code kwdswitch} ({@link ÚdajeUdalostí#kláves() kláves}())
							{
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#HORE HORE}:
								miestnosť.{@link Častica#y y} += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DOLE DOLE}:
								miestnosť.{@link Častica#y y} -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VPRAVO VPRAVO}:
								miestnosť.{@link Častica#x x} += {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#VĽAVO VĽAVO}:
								miestnosť.{@link Častica#x x} -= {@code num10};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code comm// Klávesy Escape a Enter ukončia režim úprav – zrušia}
							{@code comm// označenie miestnosti.}
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#ESCAPE ESCAPE}:
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#ENTER ENTER}:
								režim = PASÍVNY;
								miestnosť = {@code valnull};
								{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								{@code kwdbreak};

							{@code comm// Kláves Delete si vyžiada potvrdenie na vymazanie}
							{@code comm// miestnosti a v prípade pozitívnej odpovede ju vymaže.}
							{@code kwdcase} {@link Kláves Kláves}.{@link Kláves#DELETE DELETE}:
								{@code kwdif} ({@link GRobot#ÁNO ÁNO} == {@link Svet#otázka(String) otázka}({@code srg"Určite chcete vymazať miestnosť?"}))
								{
									miestnosti.{@link Zoznam#odober(Object) odober}(miestnosť);
									režim = PASÍVNY;
									miestnosť = {@code valnull};
									{@link Svet#žiadajPrekreslenie() žiadajPrekreslenie}();
								}
								{@code kwdbreak};
							}
						}
						{@code kwdbreak};
					}
				}

				{@code comm// Vstupný bod programu.}
				{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
				{
					{@code comm// (Poznámka: Okrem konštruktora príkladu sú všetky metódy}
					{@code comm// v tomto bloku statickými metódami sveta – pozri upozornenie}
					{@code comm// na začiatku príkladu.)}
					{@link Svet#nekresli() nekresli}();
					{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"SkicárMiestností.cfg"});
					{@code kwdnew} SkicárMiestností();

					{@code kwdif} ({@link Svet#prvéSpustenie() prvéSpustenie}())
					{
						{@link Svet#zbaľ() zbaľ}();
						{@link Svet#vystreď() vystreď}();
					}

					{@link Svet#spustiČasovač() spustiČasovač}();
				}
			}
			</pre>
		 * 
		 * <p><b>Ukážka aplikácie:</b></p>
		 * 
		 * <p><image>skicar-miestnosti.png<alt/>Skicár
		 * miestností.</image>Ukážka vzhľadu miniaplikácie „Skicár
		 * miestností.“</p>
		 * 
		 * @param vnorenýMennýPriestor názov vnoreného menného priestoru
		 *     alebo {@code valnull} na zálohovanie aktuálneho menného
		 *     priestoru
		 * 
		 * @throws GRobotException ak názov vnoreného menného priestoru
		 *     nespĺňa niektorú požiadavku – pozri informácie v opise metódy
		 *     {@link #mennýPriestorVlastností(String)
		 *     mennýPriestorVlastností}
		 */
		public void vnorMennýPriestorVlastností(String vnorenýMennýPriestor)
		{
			// Konfigurácia miestností v príklade v opise (zredukované):
			// početMiestností=22
			// miestnosť[0].meno=Kuchyňa\nmiestnosť[0].x=-120.0\nmiestnosť[0].y=20.0\nmiestnosť[0].uhol=90.0\nmiestnosť[0].šírka=80.0\nmiestnosť[0].výška=60.0
			// miestnosť[1].meno=Obývačka\nmiestnosť[1].x=60.0\nmiestnosť[1].y=20.0\nmiestnosť[1].uhol=90.0\nmiestnosť[1].šírka=80.0\nmiestnosť[1].výška=60.0
			// miestnosť[2].meno=Predsieň\nmiestnosť[2].x=-30.0\nmiestnosť[2].y=160.0\nmiestnosť[2].uhol=90.0\nmiestnosť[2].šírka=70.0\nmiestnosť[2].výška=60.0
			// miestnosť[3].meno=Chodba\nmiestnosť[3].x=10.0\nmiestnosť[3].y=-110.0\nmiestnosť[3].uhol=0.0\nmiestnosť[3].šírka=30.0\nmiestnosť[3].výška=50.0
			// miestnosť[4].meno=Komora\nmiestnosť[4].x=-160.0\nmiestnosť[4].y=160.0\nmiestnosť[4].uhol=0.0\nmiestnosť[4].šírka=40.0\nmiestnosť[4].výška=60.0
			// miestnosť[5].meno=Chodba\nmiestnosť[5].x=-30.0\nmiestnosť[5].y=-190.0\nmiestnosť[5].uhol=90.0\nmiestnosť[5].šírka=70.0\nmiestnosť[5].výška=30.0
			// miestnosť[6].meno=WC\nmiestnosť[6].x=-70.0\nmiestnosť[6].y=-100.0\nmiestnosť[6].uhol=90.0\nmiestnosť[6].šírka=30.0\nmiestnosť[6].výška=40.0
			// miestnosť[7].meno=Kúpeľňa\nmiestnosť[7].x=-160.0\nmiestnosť[7].y=-140.0\nmiestnosť[7].uhol=90.0\nmiestnosť[7].šírka=40.0\nmiestnosť[7].výška=80.0
			// miestnosť[8].meno=Detská izba\nmiestnosť[8].x=140.0\nmiestnosť[8].y=160.0\nmiestnosť[8].uhol=90.0\nmiestnosť[8].šírka=80.0\nmiestnosť[8].výška=60.0
			// miestnosť[9].meno=Spálňa\nmiestnosť[9].x=140.0\nmiestnosť[9].y=-140.0\nmiestnosť[9].uhol=90.0\nmiestnosť[9].šírka=80.0\nmiestnosť[9].výška=80.0
			// miestnosť[10].meno=Balkón\nmiestnosť[10].x=190.0\nmiestnosť[10].y=20.0\nmiestnosť[10].uhol=0.0\nmiestnosť[10].šírka=30.0\nmiestnosť[10].výška=60.0
			// miestnosť[11].meno=\nmiestnosť[11].x=-110.0\nmiestnosť[11].y=-190.0\nmiestnosť[11].uhol=90.0\nmiestnosť[11].šírka=0.0\nmiestnosť[11].výška=20.0
			// miestnosť[12].meno=\nmiestnosť[12].x=-70.0\nmiestnosť[12].y=-150.0\nmiestnosť[12].uhol=90.0\nmiestnosť[12].šírka=20.0\nmiestnosť[12].výška=0.0
			// miestnosť[13].meno=\nmiestnosť[13].x=10.0\nmiestnosť[13].y=-50.0\nmiestnosť[13].uhol=90.0\nmiestnosť[13].šírka=20.0\nmiestnosť[13].výška=0.0
			// miestnosť[14].meno=\nmiestnosť[14].x=-70.0\nmiestnosť[14].y=90.0\nmiestnosť[14].uhol=90.0\nmiestnosť[14].šírka=20.0\nmiestnosť[14].výška=0.0
			// miestnosť[15].meno=\nmiestnosť[15].x=100.0\nmiestnosť[15].y=90.0\nmiestnosť[15].uhol=90.0\nmiestnosť[15].šírka=20.0\nmiestnosť[15].výška=0.0
			// miestnosť[16].meno=\nmiestnosť[16].x=0.0\nmiestnosť[16].y=230.0\nmiestnosť[16].uhol=90.0\nmiestnosť[16].šírka=30.0\nmiestnosť[16].výška=10.0
			// miestnosť[17].meno=\nmiestnosť[17].x=-110.0\nmiestnosť[17].y=160.0\nmiestnosť[17].uhol=90.0\nmiestnosť[17].šírka=0.0\nmiestnosť[17].výška=20.0
			// miestnosť[18].meno=\nmiestnosť[18].x=50.0\nmiestnosť[18].y=-190.0\nmiestnosť[18].uhol=90.0\nmiestnosť[18].šírka=0.0\nmiestnosť[18].výška=20.0
			// miestnosť[19].meno=\nmiestnosť[19].x=150.0\nmiestnosť[19].y=0.0\nmiestnosť[19].uhol=90.0\nmiestnosť[19].šírka=0.0\nmiestnosť[19].výška=20.0
			// miestnosť[20].meno=\nmiestnosť[20].x=-30.0\nmiestnosť[20].y=40.0\nmiestnosť[20].uhol=90.0\nmiestnosť[20].šírka=0.0\nmiestnosť[20].výška=20.0
			// miestnosť[21].meno=\nmiestnosť[21].x=0.0\nmiestnosť[21].y=250.0\nmiestnosť[21].uhol=90.0\nmiestnosť[21].šírka=20.0\nmiestnosť[21].výška=0.0

			aktívnaSekcia.zásobníkPriestorov.push(
				aktívnaSekcia.mennýPriestorVlastností);

			if (null != vnorenýMennýPriestor)
				mennýPriestorVlastností((null == aktívnaSekcia.
					mennýPriestorVlastností ? "" : aktívnaSekcia.
					mennýPriestorVlastností + ".") + vnorenýMennýPriestor);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vnorMennýPriestorVlastností(String) vnorMennýPriestorVlastností}.</p> */
		public void vnorMennyPriestorVlastnosti(String vnorenýMennýPriestor)
		{ vnorMennýPriestorVlastností(vnorenýMennýPriestor); }

		/**
		 * <p>Zálohuje aktuálny menný priestor na čítanie a zápis
		 * vlastností do vnútorného zásobníka. Obnoviť ho môžete volaním
		 * metódy {@link #vynorMennýPriestorVlastností()
		 * vynorMennýPriestorVlastností}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Každá {@linkplain 
		 * #aktivujSekciu(String) sekcia} disponuje vlastným zásobníkom
		 * menných priestorov</p>
		 * 
		 * <p>Pozri aj informácie a príklad v opise metódy {@link 
		 * #vnorMennýPriestorVlastností(String)
		 * vnorMennýPriestorVlastností}.</p>
		 */
		public void vnorMennýPriestorVlastností()
		{
			vnorMennýPriestorVlastností(null);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vnorMennýPriestorVlastností() vnorMennýPriestorVlastností}.</p> */
		public void vnorMennyPriestorVlastnosti()
		{ vnorMennýPriestorVlastností(); }

		/**
		 * <p>Obnoví posledný zálohovaný menný priestor na čítanie a zápis
		 * vlastností z vnútorného zásobníka. Zásobník nesmie byť prázdny,
		 * inak vznikne výnimka. Pozri aj informácie v opise metódy {@link 
		 * #vnorMennýPriestorVlastností(String)
		 * vnorMennýPriestorVlastností}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> Každá {@linkplain 
		 * #aktivujSekciu(String) sekcia} disponuje vlastným zásobníkom
		 * menných priestorov</p>
		 * 
		 * <p>Pozri aj informácie a príklad v opise metódy {@link 
		 * #vnorMennýPriestorVlastností(String)
		 * vnorMennýPriestorVlastností}.</p>
		 * 
		 * @throws java.util.EmptyStackException ak je vnútorný zásobník
		 *     menných priestorov prázdny
		 */
		public void vynorMennýPriestorVlastností()
		{
			aktívnaSekcia.mennýPriestorVlastností =
				aktívnaSekcia.zásobníkPriestorov.pop();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vynorMennýPriestorVlastností() vynorMennýPriestorVlastností}.</p> */
		public void vynorMennyPriestorVlastnosti()
		{ vynorMennýPriestorVlastností(); }


	// Dialógy

		// Ak je predvolená cesta dialógov rovná null, tak nie je ovplyvňovaná
		// cesta, ktorú predvolí systém.
		private static String predvolenáCestaDialógov = null;

		/**
		 * <p>Zistí, aká je aktuálna predvolená cesta (systémových pomocných)
		 * dialógov na {@linkplain #dialógOtvoriť(String) otvorenie}
		 * a {@linkplain #dialógUložiť(String) uloženie} súborov. Ak je
		 * návratová hodnota rovná {@code valnull} (alebo prázdny reťazec),
		 * tak to znamená, že dialógy nemajú žiadnu predvolenú cestu a budú
		 * otvorené na umiestnení, ktoré určí operačný systém.</p>
		 * 
		 * @return aktuálna predvolená cesta dialógov na
		 *     {@linkplain #dialógOtvoriť(String) otvorenie}
		 *     a {@linkplain #dialógUložiť(String) uloženie} súborov
		 */
		public static String predvolenáCestaDialógov()
		{
			return predvolenáCestaDialógov;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáCestaDialógov() predvolenáCestaDialógov}.</p> */
		public static String predvolenaCestaDialogov()
		{
			return predvolenáCestaDialógov;
		}

		/**
		 * <p>Nastaví novú predvolenú cestu (systémových pomocných)
		 * dialógov na {@linkplain #dialógOtvoriť(String) otvorenie}
		 * a {@linkplain #dialógUložiť(String) uloženie} súborov. Ak je
		 * zadaná hodnota rovná {@code valnull} (alebo prázdny reťazec),
		 * tak to znamená, že dialógy nemajú žiadnu predvolenú cestu a budú
		 * otvorené na umiestnení, ktoré určí operačný systém.</p>
		 * 
		 * <p class="tip"><b>Tip:</b> Neoficiálnym trikom na voľbu
		 * priečinka aktuálneho pri spustení aplikácie (ktorý nie je
		 * podpoprovaný všetkými operačnými systémami) je zadanie reťazca
		 * {@code srg"."} (ktorý má v podporovaných OS presne význam
		 * aktuálneho priečinka).</p>
		 * 
		 * @param cesta nová predvolená cesta dialógov na
		 *     {@linkplain #dialógOtvoriť(String) otvorenie}
		 *     a {@linkplain #dialógUložiť(String) uloženie} súborov
		 */
		public static void predvolenáCestaDialógov(String cesta)
		{
			predvolenáCestaDialógov = cesta;
		}

		/** <p><a class="alias"></a> Alias pre {@link #predvolenáCestaDialógov(String) predvolenáCestaDialógov}.</p> */
		public static void predvolenaCestaDialogov(String cesta)
		{
			predvolenáCestaDialógov = cesta;
		}

		/**
		 * <p>Otvorí používateľský dialóg na otvorenie súboru, ktorý vráti
		 * reťazec s úplnou cestou a názvom súboru zvoleného v dialógu
		 * používateľom. Ak používateľ dialóg zruší, návratová hodnota je
		 * {@code valnull}. Programátor zadáva titulok dialógu, s ktorým
		 * bude dialóg otvorený používateľovi.</p>
		 * 
		 * @param titulok titulok dialógu
		 * @return úplná cesta a názov súboru alebo {@code valnull}
		 */
		public static String dialógOtvoriť(String titulok)
		{
			FileDialog dialóg = new FileDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, FileDialog.LOAD);
			dialóg.setDirectory(predvolenáCestaDialógov);
			dialóg.setVisible(true);
			if (null != dialóg.getFile())
				return dialóg.getDirectory() + File.separator +
					dialóg.getFile();
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #dialógOtvoriť(String) dialógOtvoriť}.</p> */
		public static String dialogOtvorit(String titulok) { return dialógOtvoriť(titulok); }

		private final static Pattern príponaFiltraDialóguSúborov =
			Pattern.compile("\\*\\.([^;,|)…]+)");

		/**
		 * <p>Otvorí používateľský dialóg na otvorenie súboru, ktorý vráti
		 * reťazec s úplnou cestou a názvom súboru zvoleného v dialógu
		 * používateľom.
		 * Ak používateľ dialóg zruší, návratová hodnota je {@code valnull}.
		 * Programátor zadáva titulok dialógu, predvolený názov súboru,
		 * s ktorými bude dialóg otvorený používateľovi a prípadne aj jeden
		 * alebo viacero filtrov obsahujúcich šablóny masiek, ktoré určia
		 * súbory, ktoré sú v dialógu buď viditeľné (v OS Windows), alebo
		 * dovolené zvoliť (v macOS, predtým OS X a Mac OS – v tomto systéme
		 * sú vždy viditeľné všetky bežne zobrazované súbory, ale tie, ktoré
		 * nevyhovujú kritériám filtra sú „šedé“ – používateľovi nie je
		 * umožnené ich zvoliť).
		 * Filtre sú zadávané vo forme reťazca, ktorý bude k dispozícii na
		 * výber v dialógu, a ktorý musí obsahovať aspoň jeden reťazec v tvare
		 * <code>*.</code><em>«prípona»«oddeľovač»</em>, pričom platnými
		 * oddeľovačmi sú bodkočiarka ({@code ;}), čiarka ({@code ,}), zvislá
		 * čiara ({@code |}), koncová zátvorka ({@code )}) a elipsa
		 * ({@code …}), inak bude filter ignorovaný. Príkladom platného filtra
		 * môže byť: {@code srg"Obrázky vo formátoch GIF, PNG a JPEG (*.gif,
		 * *.png, *.jpeg…)"}</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Filtre sú dostupné iba pri
		 * tejto verzii metódy z dôvodu rozlíšiteľnosti dvoch verzií metódy.
		 * Ide o principiálne obmedzenie jazyka Java. Ak si neželáte zadať
		 * predvolený názov súboru, použite hodnotu {@code valnull}. Z dôvodu
		 * použitia filtrov otvára táto verzia metódy principiálne iný typ
		 * dialógu ako jej staršia verzia.</p>
		 * 
		 * @param titulok titulok dialógu
		 * @param predvolenýNázovSúboru predvolený názov súboru poskytovaný
		 *     dialógom, ktorý je v niektorých operačných systémoch
		 *     (konkrétne macOS – predtým OS X a Mac OS) žiaľ aj tak
		 *     ignorovaný (pri dialógu slúžiacom na voľbu súboru na uloženie
		 *     to neplatí)
		 * @param filtre jeden alebo viac nepovinných reťazcov filtrov
		 *     (obsahujúcich masky) určujúcich, ktoré súbory majú byť v dialógu
		 *     zobrazené (v OS Windows) alebo dovolené vybrať (v macOS –
		 *     predtým OS X a Mac OS)
		 * @return úplná cesta a názov súboru alebo {@code valnull}
		 */
		public static String dialógOtvoriť(String titulok,
			String predvolenýNázovSúboru, String... filtre)
		{
			JFileChooser dialóg = new JFileChooser(predvolenáCestaDialógov);
			dialóg.setDialogTitle(titulok);
			if (null == predvolenýNázovSúboru)
				dialóg.setSelectedFile(null);
			else
				dialóg.setSelectedFile(new File(predvolenýNázovSúboru));

			boolean prvý = true;
			for (String filter : filtre)
			{
				Vector<String> zoznamPrípon = new Vector<>();
				Matcher match = príponaFiltraDialóguSúborov.matcher(filter);
				while (match.find()) zoznamPrípon.add(match.group(1));

				if (!zoznamPrípon.isEmpty())
				{
					FileNameExtensionFilter filterPrípon =
						new FileNameExtensionFilter(
							filter, zoznamPrípon.toArray(
								new String[zoznamPrípon.size()]));

					if (prvý)
					{
						dialóg.setFileFilter(filterPrípon);
						prvý = false;
					}
					else
						dialóg.addChoosableFileFilter(filterPrípon);
				}
			}

			if (dialóg.showOpenDialog(null == Svet.oknoCelejObrazovky ?
				GRobot.svet : Svet.oknoCelejObrazovky) ==
				JFileChooser.APPROVE_OPTION) try
			{
				return dialóg.getSelectedFile().getCanonicalPath();
			}
			catch (IOException e) { /* Ignorované… („sorry“) */ }

			/*
			FileDialog dialóg = new FileDialog(GRobot.svet, titulok,
				FileDialog.LOAD);
			dialóg.setDirectory(predvolenáCestaDialógov);
			dialóg.setFile(predvolenýNázovSúboru);

			// Filtre – začiatok
			// 
			// (Poznámka: Dalo by sa to implementovať aj efektívnejšie cez
			// Pattern, StringBuffer a vlastnú implementáciu rozhrania
			// FilenameFilter, ale myslím si, že takto to stačí…)
			// 
			// (Poznámka 2: Dobre som urobil, aj tak to bolo na nič.)
			// 
			String filtruj = "";
			for (String filter : filtre)
			{
				filter = filter.replaceAll(
					"([\\.\\+\\^\\$\\(\\)\\[\\]\\{\\}\\\\])", "\\\\$1").
						replace('?', '.').replace("*", ".*");

				if (!filter.isEmpty())
				{
					if (filtruj.isEmpty()) filtruj = filter;
					else filtruj += "|" + filter;
				}
			}
			final String konštantaFiltra = filtruj;
			if (!filtruj.isEmpty())
				dialóg.setFilenameFilter((dir, name) ->
					{
						boolean b = name.matches(konštantaFiltra);
						System.out.println(name + ": " + b);
						return b;
					}
					);
			// Filtre – koniec

			dialóg.setVisible(true);
			if (null != dialóg.getFile())
				return dialóg.getDirectory() + File.separator +
					dialóg.getFile();
			*/

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #dialógOtvoriť(String, String, String...) dialógOtvoriť}.</p> */
		public static String dialogOtvorit(String titulok,
			String predvolenýNázovSúboru, String... filtre)
		{
			return dialógOtvoriť(titulok, predvolenýNázovSúboru, filtre);
		}

		/**
		 * <p>Otvorí používateľský dialóg na uloženie súboru, ktorý vráti
		 * reťazec s úplnou cestou a názvom súboru zvoleného v dialógu
		 * používateľom. Ak používateľ dialóg zruší, návratová hodnota je
		 * {@code valnull}. Programátor zadáva titulok dialógu, s ktorým
		 * bude dialóg otvorený používateľovi.</p>
		 * 
		 * @param titulok titulok dialógu
		 * @return úplná cesta a názov súboru alebo {@code valnull}
		 */
		public static String dialógUložiť(String titulok)
		{
			FileDialog dialóg = new FileDialog(
				null == Svet.oknoCelejObrazovky ? GRobot.svet :
				Svet.oknoCelejObrazovky, titulok, FileDialog.SAVE);
			dialóg.setDirectory(predvolenáCestaDialógov);
			dialóg.setVisible(true);
			if (null != dialóg.getFile())
				return dialóg.getDirectory() + File.separator +
					dialóg.getFile();
			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #dialógUložiť(String) dialógUložiť}.</p> */
		public static String dialogUlozit(String titulok) { return dialógUložiť(titulok); }

		/**
		 * <p>Otvorí používateľský dialóg na uloženie súboru, ktorý vráti
		 * reťazec s úplnou cestou a názvom súboru zvoleného v dialógu
		 * používateľom. Ak používateľ dialóg zruší, návratová hodnota je
		 * {@code valnull}. Programátor zadáva titulok dialógu, predvolený
		 * názov súboru, s ktorými bude dialóg otvorený používateľovi
		 * a prípadne aj jeden alebo viacero filtrov obsahujúcich šablóny
		 * masiek, ktoré určia súbory, ktoré sú v dialógu buď viditeľné
		 * (v OS Windows), alebo dovolené zvoliť (v macOS, predtým OS X
		 * a Mac OS – v tomto systéme sú vždy viditeľné všetky bežne
		 * zobrazované súbory, ale tie, ktoré nevyhovujú kritériám filtra
		 * sú „šedé“ – používateľovi nie je umožnené ich zvoliť).
		 * Filtre sú zadávané vo forme reťazca, ktorý bude k dispozícii na
		 * výber v dialógu, a ktorý musí obsahovať aspoň jeden reťazec v tvare
		 * <code>*.</code><em>«prípona»«oddeľovač»</em>, pričom platnými
		 * oddeľovačmi sú bodkočiarka ({@code ;}), čiarka ({@code ,}), zvislá
		 * čiara ({@code |}), koncová zátvorka ({@code )}) a elipsa
		 * ({@code …}), inak bude filter ignorovaný. Príkladom platného filtra
		 * môže byť: {@code srg"Obrázky vo formátoch GIF, PNG a JPEG (*.gif,
		 * *.png, *.jpeg…)"}</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Filtre sú dostupné iba pri
		 * tejto verzii metódy z dôvodu rozlíšiteľnosti dvoch verzií metódy.
		 * Ide o principiálne obmedzenie jazyka Java. Ak si neželáte zadať
		 * predvolený názov súboru, použite hodnotu {@code valnull}. Z dôvodu
		 * použitia filtrov otvára táto verzia metódy principiálne iný typ
		 * dialógu ako jej staršia verzia.</p>
		 * 
		 * @param titulok titulok dialógu
		 * @param predvolenýNázovSúboru predvolený názov súboru
		 *     poskytovaný dialógom
		 * @param filtre jeden alebo viac nepovinných reťazcov filtrov
		 *     (obsahujúcich masky) určujúcich, ktoré súbory majú byť v dialógu
		 *     zobrazené (v OS Windows) alebo dovolené vybrať (v macOS –
		 *     predtým OS X a Mac OS)
		 * @return úplná cesta a názov súboru alebo {@code valnull}
		 */
		public static String dialógUložiť(String titulok,
			String predvolenýNázovSúboru, String... filtre)
		{
			JFileChooser dialóg = new JFileChooser(predvolenáCestaDialógov);
			dialóg.setDialogTitle(titulok);
			if (null == predvolenýNázovSúboru)
				dialóg.setSelectedFile(null);
			else
				dialóg.setSelectedFile(new File(predvolenýNázovSúboru));

			boolean prvý = true;
			for (String filter : filtre)
			{
				Vector<String> zoznamPrípon = new Vector<>();
				Matcher match = príponaFiltraDialóguSúborov.matcher(filter);
				while (match.find()) zoznamPrípon.add(match.group(1));

				if (!zoznamPrípon.isEmpty())
				{
					FileNameExtensionFilter filterPrípon =
						new FileNameExtensionFilter(
							filter, zoznamPrípon.toArray(
								new String[zoznamPrípon.size()]));

					if (prvý)
					{
						dialóg.setFileFilter(filterPrípon);
						prvý = false;
					}
					else
						dialóg.addChoosableFileFilter(filterPrípon);
				}
			}

			if (dialóg.showSaveDialog(null == Svet.oknoCelejObrazovky ?
				GRobot.svet : Svet.oknoCelejObrazovky) ==
				JFileChooser.APPROVE_OPTION) try
			{
				return dialóg.getSelectedFile().getCanonicalPath();
			}
			catch (IOException e) { /* Ignorované… („sorry“) */ }

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #dialógUložiť(String, String, String...) dialógUložiť}.</p> */
		public static String dialogUlozit(String titulok,
			String predvolenýNázovSúboru, String... filtre)
		{
			return dialógUložiť(titulok, predvolenýNázovSúboru, filtre);
		}


	// Vyhľadanie súboru na čítanie

		/**
		 * <p>Vyhľadá skutočné umiestnenie súboru určeného na čítanie alebo
		 * vráti hodnotu {@code valnull}, ak súbor nebol nájdený. Súbor je
		 * prednostne vyhľadaný v rámci aktuálneho umiestnenia (cesty)
		 * určeného na čítanie (a zápis) súborov. Ak nie je nájdený, tak je
		 * prehľadaný zoznam ciest <code>classpath</code>. Ak i tak nie je
		 * nájdený, tak je nakoniec prehľadaný aktuálny balíček {@code .jar}.
		 * Ak súbor nie je nájdený ani tam, tak je vrátená hodnota
		 * {@code valnull}. V prípade, že sa súbor nachádza v {@code .jar}
		 * súbore, tak je vrátená cesta v nasledujúcom tvare:</p>
		 * 
		 * <p><code>jar:file:</code><small><em>«úplná cesta k .jar
		 * súboru»</em></small>!<small><em>«cesta a názov súboru v rámci
		 * .jar súboru»</em></small></p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pre používateľov OS Windows
		 * môže byť neštandardné, že obe cesty súvisiace s {@code .jar}
		 * súborom budú v úlohe oddeľovačov priečinkov lomky (<code>/</code>)
		 * obsahovať namiesto spätných lomiek (<code>\</code>) a zároveň
		 * sa budú lomkou aj začínať (a to aj napriek tomu, že za touto
		 * prvou lomkou bude uvedená jednotka, na ktorej je {@code .jar}
		 * súbor umiestnený).</p>
		 * 
		 * @param názovSúboru názov súboru (určeného na čítanie)
		 * @return skutočné umiestnenie súboru alebo {@code valnull}, ak
		 *     súbor nebol nájdený na žiadnej z prehľadávaných lokalít
		 * 
		 * @see #jestvuje(String)
		 */
		public static String kdeJeSúbor(String názovSúboru)
		{
			try
			{
				File súbor = nájdiSúbor(názovSúboru);
				if (null != súbor) return súbor.getCanonicalPath();
			}
			catch (FileNotFoundException e)
			{
				// Ignorované… („sorry“)
			}
			catch (IOException e)
			{
				// Ignorované… („sorry“)
			}

			try
			{
				URL url = nájdiZdroj(názovSúboru);
				if (null != url)
					return URLDecoder.decode(url.toString(), "UTF-8");
					// Pozor! Toto UTF-8 nemôže byť zamenené – ide
					// o dekódovanie do reťazca Javy!
			}
			catch (IOException e)
			{
				// Ignorované… („sorry“)
			}

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #kdeJeSúbor(String) kdeJeSúbor}.</p> */
		public String kdeJeSubor(String názovSúboru)
		{ return kdeJeSúbor(názovSúboru); }


	// Otvorenie a zatvorenie súboru

		/**
		 * <p>Priradí k tomuto súboru zadanú inštanciu {@linkplain Archív
		 * archívu}. Metódy…

		 TODO

		 * … automatické otvorenie…

		 Zápis:

		 …
		 * vrhne výnimku {@link GRobotException GRobotException} s jazykovým
		 * identifikátorom {@code duplicateEntry} (Položka „<em>názov
		 * položky</em>“ už v archíve jestvuje.

		 * Hodnota {@code valnull} znamená, že prípadný pripojený archív má
		 * byť od tohto súboru odpojený a súbor má opäť fungovať samostatne
		 * (zapisujúc a čítajúc údaje priamo z pevného disku, prípadne zo
		 * zdrojov).</p>
		 * 

		 * <!-- TODO príklad použitia -->

		 * @param archív TODO
		 * 
		 * @see #archívPriradený()
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #zavri()
		 * @see #close()
		 */
		public void pripojArchív(Archív archív)
		{
			if (this.archív != archív)
			{
				try { zavri(); } catch (IOException e)
				{ GRobotException.vypíšChybovéHlásenia(e/*, false*/); }
				this.archív = archív;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #pripojArchív(Archív) pripojArchív}.</p> */
		public void pripojArchiv(Archív archív) { pripojArchív(archív); }

		/**
		 * <p>Zistí, či je k tomuto súboru pripojený {@linkplain Archív
		 * archív}.</p>
		 * 
		 * @return {@code valtrue} ak je archív priradený, {@code valfalse}
		 *     v opačnom prípade
		 * 
		 * @see #pripojArchív(Archív)
		 */
		public boolean archívPriradený() { return null != archív; }

		/** <p><a class="alias"></a> Alias pre {@link #archívPriradený() archívPriradený}.</p> */
		public boolean archivPriradeny() { return archívPriradený(); }

		/**
		 * <p>Otvorí zadaný súbor na zápis. Ak súbor jestvuje, bude
		 * prepísaný. Metóda použije kódovanie UTF-8.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V prípade, že plánujete používať
		 * mechanizmus vlastností, venujte pozornosť upozorneniu pri metóde
		 * {@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}!</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj informácie
		 * uvedené v opise metódy {@link #pripojArchív(Archív)
		 * pripojArchív}.</p>
		 * 
		 * @param názovSúboru názov súboru, ktorý má byť otvorený na zápis
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak je meno súboru zamlčané
		 *     ({@code valnull})
		 * 
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #zavri()
		 * @see #close()
		 */
		public void otvorNaZápis(String názovSúboru) throws IOException
		{
			// TODO: Poskytnúť možnosť uloženia „BOM mark-u“ do súboru
			// (dôležité najmä ak ide o konfiguračný súbor – môže byť
			// totiž čítaný iným softvérom).

			if (null == názovSúboru)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			zavri();

			if (null != archív)
			{
				ZipOutputStream výstup = archív.výstup;

				if (null == výstup && archív.umožniťAutomatickéOtvorenie)
					výstup = archív.otvorNaZápis();

				if (null == výstup)
					throw new GRobotException(
						"Archív nie je otvorený na zápis.",
						"archiveNotOpenForWriting");

				TreeMap<String, ZipEntry> položkyVýstupu =
					archív.položkyVýstupu;

				if (-1 != názovSúboru.indexOf('\\'))
					názovSúboru = názovSúboru.replace('\\', '/');

				String cestaVArchíve = archív.cestaVArchíve;

				if (null != cestaVArchíve &&
					!názovSúboru.startsWith(cestaVArchíve))
					názovSúboru = cestaVArchíve + názovSúboru;

				if (položkyVýstupu.containsKey(názovSúboru))
					throw new GRobotException("Položka „" +
						názovSúboru + "“ už v archíve jestvuje.",
						"duplicateEntry", názovSúboru);

				ZipEntry položka = new ZipEntry(názovSúboru);
				položkyVýstupu.put(názovSúboru, položka);
				výstup.putNextEntry(položka);

				zápis = new BufferedWriter(
					new OutputStreamWriter(výstup, "UTF-8"));
			}
			else
				zápis = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(názovSúboru), "UTF-8"));
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis(String) otvorNaZápis}.</p> */
		public void otvorNaZapis(String názovSúboru) throws IOException
		{ otvorNaZápis(názovSúboru); }

		/**
		 * <p>Otvorí zadaný súbor na zápis. Ak je druhý parameter {@code 
		 * valtrue}, zapisovaný obsah bude pripojený k obsahu jestvujúceho
		 * súboru, inak bude obsah súboru prepísaný. Metóda použije kódovanie
		 * UTF-8.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj informácie
		 * uvedené v opise metódy {@link #pripojArchív(Archív)
		 * pripojArchív}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> V prípade, že plánujete
		 * používať mechanizmus vlastností, <b>nepoužívajte</b> túto metódu
		 * so zadanou hodnotou {@code valtrue} druhého argumentu!</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Pripájanie (pozri parameter
		 * {@code pripojiť}) nesmie byť použité s konfiguračnými súbormi,
		 * pretože tieto by sa stali pri ďalšom otvorení nečitateľné!</p>
		 * 
		 * @param názovSúboru názov súboru, ktorý má byť otvorený na zápis
		 * @param pripojiť ak je hodnota tohto parametra {@code valtrue},
		 *     zapisovaný obsah bude pripojený na koniec jestvujúceho súboru
		 *     (nesmie byť použité s konfiguračnými súbormi‼)
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak je meno súboru zamlčané
		 *     ({@code valnull})
		 * 
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #zavri()
		 * @see #close()
		 */
		public void otvorNaZápis(String názovSúboru, boolean pripojiť)
			throws IOException
		{ otvorNaZápis(názovSúboru, "UTF-8", pripojiť); }

		/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis(String, boolean) otvorNaZápis}.</p> */
		public void otvorNaZapis(String názovSúboru, boolean pripojiť)
			throws IOException
		{ otvorNaZápis(názovSúboru, pripojiť); }

		/**
		 * <p>Otvorí zadaný súbor na zápis. Ak je druhý parameter {@code 
		 * valtrue}, zapisovaný obsah bude pripojený k obsahu jestvujúceho
		 * súboru, inak bude obsah súboru prepísaný.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj informácie
		 * uvedené v opise metódy {@link #pripojArchív(Archív)
		 * pripojArchív}.</p>
		 * 
		 * <p class="attention"><b>Upozornenie:</b> V prípade, že plánujete
		 * používať mechanizmus vlastností, <b>nepoužívajte</b> túto metódu
		 * so zadanou hodnotou {@code valtrue} druhého argumentu!</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Pripájanie (pozri parameter
		 * {@code pripojiť}) nesmie byť použité s konfiguračnými súbormi,
		 * pretože tieto by sa stali pri ďalšom otvorení nečitateľné!</p>
		 * 
		 * @param názovSúboru názov súboru, ktorý má byť otvorený na zápis
		 * @param kódovanie typ kódovania, ktorý má byť použitý na uloženie
		 *     textového súboru
		 * @param pripojiť ak je hodnota tohto parametra {@code valtrue},
		 *     zapisovaný obsah bude pripojený na koniec jestvujúceho súboru
		 *     (nesmie byť použité s konfiguračnými súbormi‼)
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak je meno súboru zamlčané
		 *     ({@code valnull})
		 * 
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #zavri()
		 * @see #close()
		 */
		public void otvorNaZápis(String názovSúboru, String kódovanie,
			boolean pripojiť) throws IOException
		{
			if (null == názovSúboru)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			zavri();

			if (null != archív)
			{
				if (pripojiť)
					throw new GRobotException(
						"Nemôžem pripojiť údaje k položke archívu.",
						"cannotAppendDataToEntry");

				ZipOutputStream výstup = archív.výstup;

				if (null == výstup && archív.umožniťAutomatickéOtvorenie)
					výstup = archív.otvorNaZápis();

				if (null == výstup)
					throw new GRobotException(
						"Archív nie je otvorený na zápis.",
						"archiveNotOpenForWriting");

				TreeMap<String, ZipEntry> položkyVýstupu =
					archív.položkyVýstupu;

				if (-1 != názovSúboru.indexOf('\\'))
					názovSúboru = názovSúboru.replace('\\', '/');

				String cestaVArchíve = archív.cestaVArchíve;

				if (null != cestaVArchíve &&
					!názovSúboru.startsWith(cestaVArchíve))
					názovSúboru = cestaVArchíve + názovSúboru;

				if (položkyVýstupu.containsKey(názovSúboru))
					throw new GRobotException("Položka „" +
						názovSúboru + "“ už v archíve jestvuje.",
						"duplicateEntry", názovSúboru);

				ZipEntry položka = new ZipEntry(názovSúboru);
				položkyVýstupu.put(názovSúboru, položka);
				výstup.putNextEntry(položka);

				zápis = new BufferedWriter(
					new OutputStreamWriter(výstup, kódovanie));
			}
			else
				zápis = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(názovSúboru, pripojiť), kódovanie));
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorNaZápis(String, String, boolean) otvorNaZápis}.</p> */
		public void otvorNaZapis(String názovSúboru, String kódovanie,
			boolean pripojiť) throws IOException
		{ otvorNaZápis(názovSúboru, pripojiť); }

		/**
		 * <p>Otvorí zadaný súbor s kódovaním UTF-8 na čítanie. Ak súbor
		 * nejestvuje, vznikne výnimka. Ďalšie informácie sú v opise verzie
		 * tejto metódy prijímajúcej dva parametre: {@link 
		 * #otvorNaČítanie(String, String) otvorNaČítanie}.</p>
		 * 
		 * @param názovSúboru názov súboru, ktorý má byť otvorený na čítanie
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak je meno súboru zamlčané
		 *     ({@code valnull}) alebo súbor nejestvuje
		 * 
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #otvorNaČítanie(String, String)
		 * @see #zavri()
		 * @see #close()
		 */
		public void otvorNaČítanie(String názovSúboru) throws IOException
		{ otvorNaČítanie(názovSúboru, "UTF-8"); }

		/** <p><a class="alias"></a> Alias pre {@link #otvorNaČítanie(String) otvorNaČítanie}.</p> */
		public void otvorNaCitanie(String názovSúboru) throws IOException
		{ otvorNaČítanie(názovSúboru); }

		/**
		 * <p>Otvorí zadaný súbor na čítanie. Ak súbor nejestvuje, vznikne
		 * výnimka.</p>
		 * 
		 * <p class="remark"><b>Poznámky a upozornenia:</b> Ak bol z projektu
		 * vytvorený {@code .jar} súbor (spustiteľný balíček), tak sa
		 * v prípade spustenia projektu cez tento súbor pokúsi metóda hľadať
		 * požadovaný súbor aj v {@code .jar} balíčku. Prednosť však majú
		 * súbory umiestnené mimo balíčka. So súbormi na zápis to neplatí –
		 * tie nie je možné zapisovať do {@code .jar} balíčkov.<br />
		 *  <br />
		 * V súčasnej verzii rámca metóda hľadá súbory na čítanie aj v rámci
		 * zoznamu ciest <code>classpath</code>. <b style="color:maroon">To
		 * znamená, že súbor, ktorý ste prvý raz prečítali, nemusí byť tým
		 * istým súborom ako ten, do ktorého budete zapisovať.</b> Na overenie
		 * prítomnosti súboru v aktuálnom umiestnení na zápis môžete použiť
		 * metódu {@link #jestvuje(String) jestvuje} a na zistenie
		 * umiestnenia súboru na čítanie môžete použiť metódu
		 * {@link #kdeJeSúbor(String) kdeJeSúbor}.</p>
		 * 
		 * @param názovSúboru názov súboru, ktorý má byť otvorený na čítanie
		 * @param kódovanie typ kódovania textového súboru
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak je meno súboru zamlčané
		 *     ({@code valnull}) alebo súbor nejestvuje
		 * 
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #otvorNaČítanie(String)
		 * @see #zavri()
		 * @see #close()
		 */
		public void otvorNaČítanie(String názovSúboru, String kódovanie)
			throws IOException
		{
			if (null == názovSúboru)
				throw new GRobotException(
					"Názov súboru nesmie byť zamlčaný.",
					"fileNameOmitted", null, new NullPointerException());

			zavri();

			if (null != archív)
			{
				ZipFile vstup = archív.vstup;

				if (null == vstup && archív.umožniťAutomatickéOtvorenie)
					vstup = archív.otvorNaČítanie();

				if (null == vstup)
					throw new GRobotException(
						"Archív nie je otvorený na čítanie.",
						"archiveNotOpenForReading");

				if (-1 != názovSúboru.indexOf('\\'))
					názovSúboru = názovSúboru.replace('\\', '/');

				String cestaVArchíve = archív.cestaVArchíve;

				if (null != cestaVArchíve &&
					!názovSúboru.startsWith(cestaVArchíve))
					názovSúboru = cestaVArchíve + názovSúboru;

				ZipEntry položka = vstup.getEntry(názovSúboru);

				if (null == položka)
					throw new GRobotException(
						"Položka „" + názovSúboru + "“ nebola nájdená.",
						"entryNotFound", názovSúboru);

				čítanie = new BufferedReader(new InputStreamReader(
					vstup.getInputStream(položka), kódovanie));

				prvýRiadok = true;
				koniecSúboru = false;
				overĎalšíRiadok = true;

				vlastnostiPrečítané = false;
				čítamVlastnosti = false;
				aktívnaSekcia.clear();
				sekcie.clear();
				return;
			}

			FileNotFoundException notFound = null;

			// Skúsi otvoriť prúd z aktuálnej lokality
			// alebo z aktuálneho zoznamu ciest classpath:
			try
			{
				čítanie = new BufferedReader(new InputStreamReader(
					dajVstupnýPrúdSúboru(názovSúboru), kódovanie));

				prvýRiadok = true;
				koniecSúboru = false;
				overĎalšíRiadok = true;

				vlastnostiPrečítané = false;
				čítamVlastnosti = false;
				aktívnaSekcia.clear();
				sekcie.clear();
				return;
			}
			catch (FileNotFoundException e)
			{
				notFound = e;
			}

			// Posledný pokus – otvoriť súbor ako zdroj:
			try
			{
				čítanie = new BufferedReader(new InputStreamReader(
					dajVstupnýPrúdZdroja(názovSúboru), kódovanie));

				prvýRiadok = true;
				koniecSúboru = false;
				overĎalšíRiadok = true;

				vlastnostiPrečítané = false;
				čítamVlastnosti = false;
				aktívnaSekcia.clear();
				sekcie.clear();
			}
			catch (NullPointerException isNull)
			{
				if (null != notFound)
					throw new GRobotException("Súbor „" + názovSúboru +
						"“ nebol nájdený.", "fileNotFound", názovSúboru,
						notFound);
				throw isNull;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #otvorNaČítanie(String, String) otvorNaČítanie}.</p> */
		public void otvorNaCitanie(String názovSúboru, String kódovanie)
		throws IOException { otvorNaČítanie(názovSúboru, kódovanie); }

		/**
		 * <p>Zavrie súbor, ktorý bol predtým otvorený na {@linkplain 
		 * #otvorNaČítanie(String) čítanie} alebo {@linkplain 
		 * #otvorNaZápis(String) zápis}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj informácie
		 * uvedené v opise metódy {@link #pripojArchív(Archív)
		 * pripojArchív}.</p>
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * 
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #close()
		 */
		public void zavri() throws IOException
		{
			IOException ioe = null;

			if (null != zápis)
			{
				try
				{
					zapisujVlastnosti();
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}

				try
				{
					zápis.close();
				}
				catch (IOException e)
				{
					ioe = e;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}

				zápis = null;

				if (!zachovajNepoužitéVlastnosti)
				{
					for (Vlastnosť vlastnosť : aktívnaSekcia.vlastnosti)
						vlastnosť.naZápis = false;
					for (Sekcia sekcia : sekcie)
						for (Vlastnosť vlastnosť : sekcia.vlastnosti)
							vlastnosť.naZápis = false;
				}
			}

			if (null != čítanie)
			{
				try
				{
					čítanie.close();
				}
				catch (IOException e)
				{
					ioe = e;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}

				čítanie = null;
			}

			if (null != ioe) throw ioe;
		}

		/**
		 * <p>Zavrie súbor, ktorý bol predtým otvorený na {@linkplain 
		 * #otvorNaČítanie(String) čítanie} alebo {@linkplain 
		 * #otvorNaZápis(String) zápis}. Táto metóda je definovaná ako
		 * súčasť implementácie rozhrania {@link Closeable Closeable}.
		 * Vnútorne volá metódu {@link #zavri() zavri}.</p>
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * 
		 * @see #otvorNaZápis(String)
		 * @see #otvorNaZápis(String, boolean)
		 * @see #otvorNaZápis(String, String, boolean)
		 * @see #otvorNaČítanie(String)
		 * @see #otvorNaČítanie(String, String)
		 * @see #zavri()
		 */
		public void close() throws IOException { zavri(); }


	// Čítanie vlastností

		/**
		 * <p>Zistí, či je v súbore otvorenom na čítanie (vlastností)
		 * definovaná zadaná vlastnosť. Táto metóda slúži na rozlíšenie
		 * špeciálnych prípadov pri čítaní konfigurácie. Každá metóda slúžiaca
		 * na čítanie vlastností musí mať určenú predvolenú hodnotu, ktorá je
		 * použitá v prípade, že vlastnosť v súbore nejestvuje, avšak niekedy
		 * môže nedefinovaná vlastnosť určovať špeciálny prípad konfigurácie,
		 * ktorý je potrebné rozlíšiť.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @return {@code valtrue} alebo {@code valfalse} podľa toho, či je
		 *     vlastnosť súbore definovaná alebo nie
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public boolean vlastnosťJestvuje(String názov) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti();
			return -1 != aktívnaSekcia.vlastnosti.indexOf(názov);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vlastnosťJestvuje(String) vlastnosťJestvuje}.</p> */
		public boolean vlastnostJestvuje(String názov) throws IOException
		{ return vlastnosťJestvuje(názov); }

		/** <p><a class="alias"></a> Alias pre {@link #vlastnosťJestvuje(String) vlastnosťJestvuje}.</p> */
		public boolean vlastnosťExistuje(String názov) throws IOException
		{ return vlastnosťJestvuje(názov); }

		/** <p><a class="alias"></a> Alias pre {@link #vlastnosťJestvuje(String) vlastnosťJestvuje}.</p> */
		public boolean vlastnostExistuje(String názov) throws IOException
		{ return vlastnosťJestvuje(názov); }


		/**
		 * <p>Číta hodnotu vlastnosti v tvare reťazca.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v objekte typu {@link 
		 *     java.lang.String String}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public String čítajVlastnosť(String názov, String predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new String(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			return new String(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, String) čítajVlastnosť}.</p> */
		public String citajVlastnost(String názov, String predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare reťazca, pričom výsledok je
		 * vrátený v objekte typu {@link java.lang.StringBuffer
		 * StringBuffer}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v objekte typu {@link 
		 *     java.lang.StringBuffer StringBuffer}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public StringBuffer čítajVlastnosť(String názov,
			StringBuffer predvolenáHodnota) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new StringBuffer(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			return new StringBuffer(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, StringBuffer) čítajVlastnosť}.</p> */
		public StringBuffer citajVlastnost(String názov,
			StringBuffer predvolenáHodnota) throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare celého čísla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená alebo pri prevode textu na celé
		 *     číslo nastala chyba
		 * @return hodnota vlastnosti v objekte typu {@link java.lang.Long
		 *     Long}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Long čítajVlastnosť(String názov, Long predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Long(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Long.valueOf(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Long) čítajVlastnosť}.</p> */
		public Long citajVlastnost(String názov, Long predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare celého čísla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pred verziou 1.85 bolo
		 * hodnoty s údajovým typom {@code typeint} alebo {@link 
		 * java.lang.Integer Integer} potrebné prevádzať z typu {@link 
		 * java.lang.Long Long}. Pre primitívny typ {@code typeint} to
		 * znamenalo použiť nasledujúci zápis:<br />
		 * <code>    premenná = súbor.čítajVlastnosť("názovVlastnosti",<br />
		 *         (long)predvolenáHodnota).intValue();</code></p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená alebo pri prevode textu na celé
		 *     číslo nastala chyba
		 * @return hodnota vlastnosti v objekte typu {@link java.lang.Integer
		 *     Integer}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Integer čítajVlastnosť(String názov, Integer predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Integer(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Integer.valueOf(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Integer) čítajVlastnosť}.</p> */
		public Integer citajVlastnost(String názov, Integer predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare reálneho čísla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pred verziou 1.85 bolo
		 * hodnoty s údajovým typom {@code typefloat} alebo {@link 
		 * java.lang.Float Float} potrebné prevádzať z typu {@link 
		 * java.lang.Double Double}. Pre primitívny typ {@code typefloat} to
		 * znamenalo použiť nasledujúci zápis:<br />
		 * <code>    premenná = súbor.čítajVlastnosť("názovVlastnosti",<br />
		 *         (double)predvolenáHodnota).floatValue();</code></p>
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená alebo pri prevode textu na
		 *     reálne číslo nastala chyba
		 * @return hodnota vlastnosti v objekte typu {@link 
		 *     java.lang.Float Float}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Float čítajVlastnosť(String názov, Float predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Float(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Float.valueOf(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Float) čítajVlastnosť}.</p> */
		public Float citajVlastnost(String názov, Float predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare reálneho čísla.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená alebo pri prevode textu na
		 *     reálne číslo nastala chyba
		 * @return hodnota vlastnosti v objekte typu {@link 
		 *     java.lang.Double Double}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Double čítajVlastnosť(String názov, Double predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Double(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Double.valueOf(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Double) čítajVlastnosť}.</p> */
		public Double citajVlastnost(String názov, Double predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti v tvare logickej hodnoty {@code valtrue}
		 * / {@code valfalse}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v objekte typu {@link 
		 *     java.lang.Boolean Boolean}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Boolean čítajVlastnosť(String názov, Boolean predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Boolean(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Boolean.valueOf(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Boolean) čítajVlastnosť}.</p> */
		public Boolean citajVlastnost(String názov, Boolean predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do celočíselného poľa typu {@code 
		 * typeint}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole, ktoré
		 *     je kópiou tohto argumentu – čiže pole s rovnakou dĺžkou
		 *     a rovnakými hodnotami jednotlivých prvkov; toto pole je
		 *     vytvorené a vrátené v prípade, že vlastnosť s uvedeným názvom
		 *     nebola v textovom súbore nájdená alebo je hodnota vlastnosti
		 *     prázdna
		 * @return nové pole prvkov typu {@code typeint}, ktoré obsahuje
		 *     toľko prvkov, koľko celých čísiel sa z hodnoty vlastnosti
		 *     podarilo úspešne previesť (proces prevodu textov na celé čísla
		 *     sa zastavuje pri prvom neúspechu) alebo kópia poľa zadaného do
		 *     predvolenej hodnoty
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public int[] čítajVlastnosť(String názov, int[] predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			int[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new int[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnotaS = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnotaS) return null;
			StringBuffer reťazec = new StringBuffer(hodnotaS);
			Vector<Long> vektor = new Vector<>();
			Long hodnota;

			while (null != (hodnota = prevezmiCeléČíslo(reťazec)))
				vektor.add(hodnota);

			if (0 == vektor.size() && null != predvolenáHodnota)
			{
				pole = new int[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			pole = new int[vektor.size()];
			for (int i = 0; i < vektor.size(); ++i)
				pole[i] = vektor.elementAt(i).intValue();

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, int[]) čítajVlastnosť}.</p> */
		public int[] citajVlastnost(String názov, int[] predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do poľa reálnych čísel typu {@code 
		 * typefloat}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole, ktoré
		 *     je kópiou tohto argumentu – čiže pole s rovnakou dĺžkou
		 *     a rovnakými hodnotami jednotlivých prvkov; toto pole je
		 *     vytvorené a vrátené v prípade, že vlastnosť s uvedeným názvom
		 *     nebola v textovom súbore nájdená alebo je hodnota vlastnosti
		 *     prázdna
		 * @return nové pole prvkov typu {@code typefloat}, ktoré obsahuje
		 *     toľko prvkov, koľko reálnych čísiel sa z hodnoty vlastnosti
		 *     podarilo úspešne previesť (proces prevodu textov na reálne
		 *     čísla sa zastavuje pri prvom neúspechu) alebo kópia poľa
		 *     zadaného do predvolenej hodnoty
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public float[] čítajVlastnosť(String názov, float[] predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			float[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new float[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnotaS = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnotaS) return null;
			StringBuffer reťazec = new StringBuffer(hodnotaS);
			Vector<Double> vektor = new Vector<>();
			Double hodnota;

			while (null != (hodnota = prevezmiReálneČíslo(reťazec)))
				vektor.add(hodnota);

			if (0 == vektor.size() && null != predvolenáHodnota)
			{
				pole = new float[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			pole = new float[vektor.size()];
			for (int i = 0; i < vektor.size(); ++i)
				pole[i] = vektor.elementAt(i).floatValue();

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, float[]) čítajVlastnosť}.</p> */
		public float[] citajVlastnost(String názov, float[] predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do celočíselného poľa typu {@code 
		 * typelong}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole, ktoré
		 *     je kópiou tohto argumentu – čiže pole s rovnakou dĺžkou
		 *     a rovnakými hodnotami jednotlivých prvkov; toto pole je
		 *     vytvorené a vrátené v prípade, že vlastnosť s uvedeným názvom
		 *     nebola v textovom súbore nájdená alebo je hodnota vlastnosti
		 *     prázdna
		 * @return nové pole prvkov typu {@code typelong}, ktoré obsahuje
		 *     toľko prvkov, koľko celých čísiel sa z hodnoty vlastnosti
		 *     podarilo úspešne previesť (proces prevodu textov na celé čísla
		 *     sa zastavuje pri prvom neúspechu) alebo kópia poľa zadaného do
		 *     predvolenej hodnoty
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public long[] čítajVlastnosť(String názov, long[] predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			long[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new long[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnotaS = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnotaS) return null;
			StringBuffer reťazec = new StringBuffer(hodnotaS);
			Vector<Long> vektor = new Vector<>();
			Long hodnota;

			while (null != (hodnota = prevezmiCeléČíslo(reťazec)))
				vektor.add(hodnota);

			if (0 == vektor.size() && null != predvolenáHodnota)
			{
				pole = new long[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			pole = new long[vektor.size()];
			for (int i = 0; i < vektor.size(); ++i)
				pole[i] = vektor.elementAt(i).longValue();

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, long[]) čítajVlastnosť}.</p> */
		public long[] citajVlastnost(String názov, long[] predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do poľa reálnych čísel typu {@code 
		 * typedouble}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole, ktoré
		 *     je kópiou tohto argumentu – čiže pole s rovnakou dĺžkou
		 *     a rovnakými hodnotami jednotlivých prvkov; toto pole je
		 *     vytvorené a vrátené v prípade, že vlastnosť s uvedeným názvom
		 *     nebola v textovom súbore nájdená alebo je hodnota vlastnosti
		 *     prázdna
		 * @return nové pole prvkov typu {@code typedouble}, ktoré obsahuje
		 *     toľko prvkov, koľko reálnych čísiel sa z hodnoty vlastnosti
		 *     podarilo úspešne previesť (proces prevodu textov na reálne
		 *     čísla sa zastavuje pri prvom neúspechu) alebo kópia poľa
		 *     zadaného do predvolenej hodnoty
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public double[] čítajVlastnosť(String názov,
			double[] predvolenáHodnota) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			double[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new double[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnotaS = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnotaS) return null;
			StringBuffer reťazec = new StringBuffer(hodnotaS);
			Vector<Double> vektor = new Vector<>();
			Double hodnota;

			while (null != (hodnota = prevezmiReálneČíslo(reťazec)))
				vektor.add(hodnota);

			if (0 == vektor.size() && null != predvolenáHodnota)
			{
				pole = new double[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			pole = new double[vektor.size()];
			for (int i = 0; i < vektor.size(); ++i)
				pole[i] = vektor.elementAt(i).doubleValue();

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, double[]) čítajVlastnosť}.</p> */
		public double[] citajVlastnost(String názov, double[] predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do poľa logických hodnôt typu {@code 
		 * typeboolean}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole, ktoré
		 *     je kópiou tohto argumentu – čiže pole s rovnakou dĺžkou
		 *     a rovnakými hodnotami jednotlivých prvkov; toto pole je
		 *     vytvorené a vrátené v prípade, že vlastnosť s uvedeným názvom
		 *     nebola v textovom súbore nájdená alebo je hodnota vlastnosti
		 *     prázdna
		 * @return nové pole prvkov typu {@code typeboolean}, ktoré obsahuje
		 *     toľko prvkov, koľko logických hodnôt sa podarilo z vlastnosti
		 *     úspešne prečítať alebo kópia poľa zadaného do predvolenej
		 *     hodnoty
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public boolean[] čítajVlastnosť(String názov,
			boolean[] predvolenáHodnota) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			boolean[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new boolean[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnotaS = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnotaS) return null;
			StringBuffer reťazec = new StringBuffer(hodnotaS);
			Vector<Boolean> vektor = new Vector<>();
			Boolean hodnota;

			while (null != (hodnota = prevezmiBoolean(reťazec)))
				vektor.add(hodnota);

			if (0 == vektor.size() && null != predvolenáHodnota)
			{
				pole = new boolean[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			pole = new boolean[vektor.size()];
			for (int i = 0; i < vektor.size(); ++i)
				pole[i] = vektor.elementAt(i).booleanValue();

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, boolean[]) čítajVlastnosť}.</p> */
		public boolean[] citajVlastnost(String názov,
			boolean[] predvolenáHodnota) throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti do poľa znakov ({@code typechar}{@code 
		 * []}). Metóda vracia nové pole znakov, ktorého prvky tvoria všetky
		 * znaky získané z hodnoty tejto vlastnosti.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota pole, ktoré je použité na vytvorenie
		 *     „predvolenej hodnoty,“ čo je v tomto prípade nové pole znakov,
		 *     ktoré je kópiou tohto argumentu; toto pole je vytvorené
		 *     a vrátené v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return nové pole znakov ({@code typechar}{@code []}), ktoré
		 *     tvoria hodnotu tejto vlastnosti, prípadne kópia poľa zadaného
		 *     do predvolenej hodnoty (ak vlastnosť v otvorenom súbore
		 *     nejestvuje)
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public char[] čítajVlastnosť(String názov, char[] predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			char[] pole;

			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				pole = new char[predvolenáHodnota.length];
				for (int i = 0; i < predvolenáHodnota.length; ++i)
					pole[i] = predvolenáHodnota[i];
				return pole;
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			StringBuffer reťazec = new StringBuffer(hodnota);

			pole = new char[reťazec.length()];
			for (int i = 0; i < reťazec.length(); ++i)
				pole[i] = reťazec.charAt(i);

			return pole;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, char[]) čítajVlastnosť}.</p> */
		public char[] citajVlastnost(String názov, char[] predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti ako objekt, ktorý je implementáciou
		 * rozhrania {@link Poloha Poloha}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v implementácii rozhrania {@link 
		 *     Poloha Poloha} (konkrétne typ {@link Bod Bod})
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Bod čítajVlastnosť(String názov, Poloha predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Bod(predvolenáHodnota.polohaX(),
					predvolenáHodnota.polohaY());
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Bod.reťazecNaPolohu(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Poloha) čítajVlastnosť}.</p> */
		public Bod citajVlastnost(String názov, Poloha predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti ako objekt, ktorý je odvodený od typu
		 * {@link Point2D Point2D}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v implementácii rozhrania {@link 
		 *     Point2D Point2D} (konkrétne typ {@link Point2D.Double
		 *     Point2D.Double})
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Point2D.Double čítajVlastnosť(String názov,
			Point2D predvolenáHodnota) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				return new Point2D.Double(predvolenáHodnota.getX(),
					predvolenáHodnota.getY());
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Bod.reťazecNaBod(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Point2D) čítajVlastnosť}.</p> */
		public Point2D.Double citajVlastnost(String názov,
			Point2D predvolenáHodnota) throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti ako objekt, ktorý je implementáciou
		 * rozhrania {@link Farebnosť Farebnosť}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v implementácii rozhrania {@link 
		 *     Farebnosť Farebnosť} (konkrétne typ {@link Farba Farba})
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Farba čítajVlastnosť(String názov, Farebnosť predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				if (predvolenáHodnota instanceof Farba)
					return (Farba)predvolenáHodnota;
				return new Farba(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Farba.reťazecNaFarbu(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Farebnosť) čítajVlastnosť}.</p> */
		public Farba citajVlastnost(String názov, Farebnosť predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }

		/**
		 * <p>Číta hodnotu vlastnosti ako objekt, ktorý je odvodený od typu
		 * {@link Color Color}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param predvolenáHodnota predvolená hodnota vlastnosti, ktorá je
		 *     vrátená v prípade, že vlastnosť s uvedeným názvom nebola
		 *     v textovom súbore nájdená
		 * @return hodnota vlastnosti v implementácii rozhrania {@link 
		 *     Color Color} (konkrétne typ {@link Farba Farba})
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak názov vlastnosti nespĺňa niektorú
		 *     požiadavku alebo súbor nie je otvorený na čítanie, prípadne
		 *     súbor obsahuje chybu
		 */
		public Farba čítajVlastnosť(String názov, Color predvolenáHodnota)
			throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			čítajVlastnosti(); int indexOf;
			if (-1 == (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				if (null == predvolenáHodnota) return null;
				if (predvolenáHodnota instanceof Farba)
					return (Farba)predvolenáHodnota;
				return new Farba(predvolenáHodnota);
			}

			String hodnota = aktívnaSekcia.vlastnosti.get(indexOf).hodnota;
			if (null == hodnota) return null;
			hodnota = hodnota.trim();
			if (hodnota.equalsIgnoreCase(nullString)) return null;
			return Farba.reťazecNaFarbu(hodnota);
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajVlastnosť(String, Color) čítajVlastnosť}.</p> */
		public Farba citajVlastnost(String názov, Color predvolenáHodnota)
			throws IOException
		{ return čítajVlastnosť(názov, predvolenáHodnota); }


		/**
		 * <p>Vráti zoznam vlastností definovaných v aktuálnej sekcii
		 * konfigurácie. (Pozri aj: {@link #aktívnaSekcia() aktívnaSekcia}.)
		 * Všetky názvy sú vrátené v úplnom znení, to jest vrátane
		 * prípadných menných priestorov, do ktorých patria. (Pozri aj:
		 * {@link #mennýPriestorVlastností() mennýPriestorVlastností}.)
		 * Zmeny vykonané vo vrátenom zozname nemajú žiadny vplyv na
		 * skutočné vlastnosti v rámci konfigurácie. Detekciu prípadných
		 * menných priestorov musí vykonať používateľ (rámca).</p>
		 * 
		 * <p>Metóda identifikuje a v zozname vracia aj dva špeciálne
		 * prípady:</p>
		 * 
		 * <ul>
		 * <li>komentáre: ako názov vlastnosti, ktorý sa začína znakom
		 * {@code srg";"} (čo nie je povolené),</li>
		 * <li>a prázdne riadky: ako prázdny názov vlastnosti
		 * {@code srg""} (čo tiež nie je povolené).</li>
		 * </ul>
		 * 
		 * <!-- p class="remark"><b>Poznámka:</b> Obsah komentárov nie je
		 * možné získať.</p -->
		 * 
		 * @return zoznam reťazcov označujúcich názvy vlastností v aktuálnej
		 *     sekcii tohto konfiguračného súboru
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie,
		 *     prípadne obsahuje chybu
		 * 
		 * @see #aktívnaSekcia()
		 * @see #zoznamSekcií()
		 * @see #mennýPriestorVlastností()
		 */
		public Zoznam<String> zoznamVlastností() throws IOException
		{
			čítajVlastnosti();
			Zoznam<String> zoznam = new Zoznam<>();
			for (Vlastnosť vlastnosť : aktívnaSekcia.vlastnosti)
				if (null != vlastnosť.názov &&
					vlastnosť.názov.startsWith(";"))
					zoznam.add(vlastnosť.názov + vlastnosť.hodnota);
				else
					zoznam.add(vlastnosť.názov);
			return zoznam;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zoznamVlastností() zoznamVlastností}.</p> */
		public Zoznam<String> zoznamVlastnosti() throws IOException
		{ return zoznamVlastností(); }


	// Zápis vlastností

		/**
		 * <p>Zistí v akom režime sa nachádza táto inštancia triedy
		 * {@link Súbor Súbor}.</p>
		 * 
		 * @return {@code valtrue} – inštancia pri zápise zachováva nepoužité
		 *     vlastnosti; {@code valfalse} – inštancia odstraňuje nepoužité
		 *     vlastnosti
		 * 
		 * @see #zachovajNepoužitéVlastnosti()
		 * @see #odstraňujNepoužitéVlastnosti()
		 * @see #vymažVlastnosť(String)
		 * @see #zapíšVlastnosť(String, Object)
		 */
		public boolean zachovávaNepoužitéVlastnosti()
		{ return zachovajNepoužitéVlastnosti; }

		/** <p><a class="alias"></a> Alias pre {@link #zachovávaNepoužitéVlastnosti() zachovávaNepoužitéVlastnosti}.</p> */
		public boolean zachovavaNepouziteVlastnosti()
		{ return zachovajNepoužitéVlastnosti; }

		/**
		 * <p>Prepne inštanciu do režimu zachovávania nepoužitých vlastností.
		 * V súbore zostanú po zápise zachované aj tie vlastnosti, ktoré
		 * z neho boli pôvodne prečítané, ale neboli opätovne zapísané.
		 * Toto je predvolené správanie triedy {@link Súbor Súbor}.</p>
		 * 
		 * @see #zachovávaNepoužitéVlastnosti()
		 * @see #odstraňujNepoužitéVlastnosti()
		 * @see #vymažVlastnosť(String)
		 * @see #zapíšVlastnosť(String, Object)
		 */
		public void zachovajNepoužitéVlastnosti()
		{ zachovajNepoužitéVlastnosti = true; }

		/** <p><a class="alias"></a> Alias pre {@link #zachovajNepoužitéVlastnosti() zachovajNepoužitéVlastnosti}.</p> */
		public void zachovajNepouziteVlastnosti()
		{ zachovajNepoužitéVlastnosti = true; }

		/**
		 * <p>Prepne inštanciu do režimu odstraňovania nepoužitých vlastností.
		 * Do súboru budú zapísané len tie vlastnosti, ktoré programátor
		 * prikázal {@linkplain #zapíšVlastnosť(String, Object) zapísať}.
		 * Zapisované vlastnosti, prázdne riadky a komentáre sa v súbore
		 * nemusia vyskytnúť v rovnakom poradí ako boli zapísané. Poradie
		 * zápisu vychádza z pôvodného poradia vlastností v čase čítania
		 * súboru.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Ak by uvedené správanie niekomu
		 * z ľubovoľného dôvodu prekážalo, môže pred zápisom otvoriť a zavrieť
		 * súbor na čítanie, pretože v metóde {@link #otvorNaČítanie(String)
		 * otvorNaČítanie} nastáva vyčistenie vnútornej pamäte vlastností.
		 * V takom prípade by v skutočnosti nebolo nevyhnutné použiť túto metódu
		 * ({@code currodstraňujNepoužitéVlastnosti}), ktorej účelom je iba
		 * zariadiť to, aby sa do súboru zapísali len relevantné riadky
		 * v ľubovoľnom poradí.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Odstraňovanie sa dotýka aj
		 * {@linkplain #zapíšKomentárVlastností(String) komentárov}
		 * a {@linkplain #zapíšPrázdnyRiadokVlastností() prázdnych riadkov}.
		 * Avšak, ak by bola rovnaká inštancia triedy {@link Súbor Súbor}
		 * použitá na zápis vlastností do viacerých súborov, zapisované
		 * vlastnosti by sa mohli premiešať, preto radšej nepoužívajte jednu
		 * inštanciu na zápis do viacerých súborov – pozri tiež poznámku pri
		 * metóde {@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}.</p>
		 * 
		 * @see #zachovávaNepoužitéVlastnosti()
		 * @see #zachovajNepoužitéVlastnosti()
		 * @see #vymažVlastnosť(String)
		 * @see #zapíšVlastnosť(String, Object)
		 */
		public void odstraňujNepoužitéVlastnosti()
		{ zachovajNepoužitéVlastnosti = false; }

		/** <p><a class="alias"></a> Alias pre {@link #odstraňujNepoužitéVlastnosti() odstraňujNepoužitéVlastnosti}.</p> */
		public void odstranujNepouziteVlastnosti()
		{ zachovajNepoužitéVlastnosti = false; }


		/**
		 * <p>Vymaže všetky vlastnosti z vnútornej pamäte, ktoré mohli zostať
		 * zapamätané po poslednom čítaní súboru. Po prečítaní konfiguračného
		 * súboru musia všetky vlastnosti zostať zachované vo vnútornej
		 * pamäti, aby ich bolo možné obnoviť (to jest zapísať) pri zápise
		 * konfiguračného súboru na disk aj v prípade, že neboli pri procese
		 * zápisu použité (čiže ich hodnoty neboli aktualizované niektorou
		 * metódou na zápis vlastností). Táto metóda môže pri správnom použití
		 * zabrániť nežiadúcemu premiešaniu vlastností pri spracovaní
		 * viacerých súborov prostredníctvom tej istej inštancie triedy
		 * {@link Súbor Súbor}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Pozrite si aj upozornenie pri
		 * metóde {@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}.</p>
		 */
		public void vymažVlastnosti()
		{
			aktívnaSekcia.clear();
			sekcie.clear();
			vlastnostiPrečítané = false;
			čítamVlastnosti = false;
		}

		/**
		 * <p>Odstráni zo súboru záznam o vlastnosti so zadaným menom. Použitie
		 * tejto metódy môže byť zbytočné v prípade, že je zapnuté
		 * {@linkplain #odstraňujNepoužitéVlastnosti() odstraňovanie
		 * nepoužitých vlastností}. Naopak, v prípade, že je odstraňovanie
		 * nepoužitých vlastností vypnuté, môžu byť prostredníctvom tejto
		 * metódy odstránené individuálne nepotrebné záznamy.</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * 
		 * @see #zapíšVlastnosť(String, Object)
		 * @see #zachovávaNepoužitéVlastnosti()
		 * @see #zachovajNepoužitéVlastnosti()
		 * @see #odstraňujNepoužitéVlastnosti()
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na zápis alebo
		 *     ak názov vlastnosti nespĺňa niektorú požiadavku
		 */
		public void vymažVlastnosť(String názov) throws IOException
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			if (null == zápis)
				throw new GRobotException(
					"Súbor nie je otvorený na zápis.",
					"fileNotOpenForWriting");

			int indexOf;

			if (-1 != (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
				aktívnaSekcia.vlastnosti.remove(indexOf);

			if (aktívnaSekcia.naposledyZapísanáVlastnosť >=
				aktívnaSekcia.vlastnosti.size())
				aktívnaSekcia.naposledyZapísanáVlastnosť =
					aktívnaSekcia.vlastnosti.size() - 1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažVlastnosť(String) vymažVlastnosť}.</p> */
		public void vymazVlastnost(String názov) throws IOException
		{ vymažVlastnosť(názov); }


		/**
		 * <p>Táto metóda zapne alebo vypne automatický preklad názvov a hodnôt
		 * vlastností tak, aby aj tie vlastnosti konfiguračných súborov, ktoré
		 * programovací rámec zapisuje automaticky, mohli byť v prípade potreby
		 * používané v cudzojazyčnom prostredí. Preklad funguje automaticky pri
		 * zápise a čítaní, takže v rámci programu treba používať pôvodné
		 * pomenovania a/alebo hodnoty vlastností, ale v konfiguračnom súbore
		 * budú uložené preložené pomenovania a/alebo hodnoty vlastností.</p>
		 * 
		 * <p>Obidve polia ({@code prekladyNázvov} a {@code prekladyHodnôt})
		 * sú dvojrozmerné reťazcové. Počet prvkov prvého rozmeru je ľubovoľný,
		 * určuje počet záznamov v slovníku. Počet prvkov druhého rozmeru je
		 * predpísaný a musí sa rovnať trom, inak bude záznam ignorovaný.
		 * Význam jednotlivých prvkov druhého rozmeru je nasledujúci:</p>
		 * 
		 * <table class="shadedTable">
		 * <tr><th>index</th><th>význam</th></tr>
		 * <tr><td><code>0</code></td><td>zdrojový reťazec – používaný
		 * v rámci programovacieho rámca</td></tr>
		 * <tr><td><code>1</code></td><td>cieľový reťazec – zapisovaný
		 * do súboru</td></tr>
		 * <tr><td><code>2</code></td><td>spôsob vyhľadávania – {@code srg"Z"} –
		 * výskyt na začiatku reťazca, {@code srg"K"} – výskyt na konci reťazca,
		 * {@code srg"S"} – výskyt v ľubovoľnej časti reťazca, {@code srg"P"} –
		 * presná zhoda reťazca; pri inej hodnote je záznam ignorovaný</td></tr>
		 * </table>
		 * 
		 * <p>Každé spustenie tejto metódy spôsobí úplné vymazanie všetkých
		 * vnútorných slovníkov. Chybné prvky, záznamy (napr. duplicitné)
		 * alebo polia sú ignorované. To znamená, že zadanie prázdneho poľa
		 * alebo hodnoty {@code valnull} má za následok vypnutie tej časti
		 * prekladu, ktorej pole je prázdne.</p>
		 * 
		 * <p><b>Príklad:</b></p>
		 * 
		 * <p>Nasledujúca ukážka zariadi preklad základných vlastností okna
		 * a slovných vyjadrení farieb do anglického jazyka.</p>
		 * 
		 * <pre CLASS="example">
			{@link Svet Svet}.{@link Svet#konfiguračnýSúbor() konfiguračnýSúbor}().{@code currprekladajVlastnosti}({@code kwdnew} {@link String String}[][]{
				{{@code srg"okno."}, {@code srg"window."}, {@code srg"Z"}},
				{{@code srg".šírka"}, {@code srg".width"}, {@code srg"K"}}, {{@code srg".výška"}, {@code srg".height"}, {@code srg"K"}},
				{{@code srg".maximalizované"}, {@code srg".maximized"}, {@code srg"K"}}, {{@code srg".minimalizované"}, {@code srg".minimized"}, {@code srg"K"}}
			}, {@code kwdnew} {@link String String}[][]{
				{{@code srg"žiadna"}, {@code srg"none"}, {@code srg"P"}}, {{@code srg"biela"}, {@code srg"white"}, {@code srg"P"}},
				{{@code srg"svetlošedá"}, {@code srg"lightgray"}, {@code srg"P"}}, {{@code srg"šedá"}, {@code srg"gray"}, {@code srg"P"}},
				{{@code srg"tmavošedá"}, {@code srg"darkgray"}, {@code srg"P"}}, {{@code srg"čierna"}, {@code srg"black"}, {@code srg"P"}},
				{{@code srg"svetločervená"}, {@code srg"lightred"}, {@code srg"P"}}, {{@code srg"červená"}, {@code srg"red"}, {@code srg"P"}},
				{{@code srg"tmavočervená"}, {@code srg"darkred"}, {@code srg"P"}},
				{{@code srg"svetlozelená"}, {@code srg"lightgreen"}, {@code srg"P"}},
				{{@code srg"zelená"}, {@code srg"green"}, {@code srg"P"}}, {{@code srg"tmavozelená"}, {@code srg"darkgreen"}, {@code srg"P"}},
				{{@code srg"svetlomodrá"}, {@code srg"lightblue"}, {@code srg"P"}},
				{{@code srg"modrá"}, {@code srg"blue"}, {@code srg"P"}}, {{@code srg"tmavomodrá"}, {@code srg"darkblue"}, {@code srg"P"}},
				{{@code srg"svetlotyrkysová"}, {@code srg"lightcyan"}, {@code srg"P"}},
				{{@code srg"tyrkysová"}, {@code srg"cyan"}, {@code srg"P"}}, {{@code srg"tmavotyrkysová"}, {@code srg"darkcyan"}, {@code srg"P"}},
				{{@code srg"svetlopurpurová"}, {@code srg"lightmagenta"}, {@code srg"P"}},
				{{@code srg"purpurová"}, {@code srg"magenta"}, {@code srg"P"}},
				{{@code srg"tmavopurpurová"}, {@code srg"darkmagenta"}, {@code srg"P"}},
				{{@code srg"svetložltá"}, {@code srg"lightyellow"}, {@code srg"P"}}, {{@code srg"žltá"}, {@code srg"yellow"}, {@code srg"P"}},
				{{@code srg"tmavožltá"}, {@code srg"darkyellow"}, {@code srg"P"}},
				{{@code srg"svetlohnedá"}, {@code srg"lightbrown"}, {@code srg"P"}},
				{{@code srg"hnedá"}, {@code srg"brown"}, {@code srg"P"}}, {{@code srg"tmavohnedá"}, {@code srg"darkbrown"}, {@code srg"P"}},
				{{@code srg"svetlooranžová"}, {@code srg"lightorange"}, {@code srg"P"}},
				{{@code srg"oranžová"}, {@code srg"orange"}, {@code srg"P"}}, {{@code srg"tmavooranžová"}, {@code srg"darkorange"}, {@code srg"P"}},
				{{@code srg"svetloružová"}, {@code srg"lightpink"}, {@code srg"P"}},
				{{@code srg"ružová"}, {@code srg"pink"}, {@code srg"P"}}, {{@code srg"tmavoružová"}, {@code srg"darkpink"}, {@code srg"P"}},
				{{@code srg"uhlíková"}, {@code srg"coal"}, {@code srg"P"}}, {{@code srg"antracitová"}, {@code srg"anthracite"}, {@code srg"P"}},
				{{@code srg"papierová"}, {@code srg"paper"}, {@code srg"P"}}, {{@code srg"snehová"}, {@code srg"snow"}, {@code srg"P"}},
				{{@code srg"tmavofialová"}, {@code srg"darkpurple"}, {@code srg"P"}}, {{@code srg"fialová"}, {@code srg"purple"}, {@code srg"P"}},
				{{@code srg"svetlofialová"}, {@code srg"lightpurple"}, {@code srg"P"}},
				{{@code srg"tmavoatramentová"}, {@code srg"darkink"}, {@code srg"P"}}, {{@code srg"atramentová"}, {@code srg"ink"}, {@code srg"P"}},
				{{@code srg"svetloatramentová"}, {@code srg"lightink"}, {@code srg"P"}},
				{{@code srg"tmavoakvamarínová"}, {@code srg"darkaqua"}, {@code srg"P"}}, {{@code srg"akvamarínová"}, {@code srg"aqua"}, {@code srg"P"}},
				{{@code srg"svetloakvamarínová"}, {@code srg"lightaqua"}, {@code srg"P"}},
				{{@code srg"tmaváNebeská"}, {@code srg"darkceleste"}, {@code srg"P"}}, {{@code srg"nebeská"}, {@code srg"celeste"}, {@code srg"P"}},
				{{@code srg"svetláNebeská"}, {@code srg"lightceleste"}, {@code srg"P"}}
			});
			</pre>
		 * 
		 * @param prekladyNázvov pole záznamov k prekladom názvov vlastností
		 * @param prekladyHodnôt pole záznamov k prekladom hodnôt vlastností
		 */
		public void prekladajVlastnosti(
			String[][] prekladyNázvov, String[][] prekladyHodnôt)
		{
			začiatkyNázvov.clear();
			konceNázvov.clear();
			stredyNázvov.clear();
			presnéNázvy.clear();

			if (null != prekladyNázvov) try
			{
				for (String[] záznam : prekladyNázvov)
				{
					if (null != záznam && 3 <= záznam.length)
					{
						if (null != záznam[2] && null != záznam[1] &&
							null != záznam[0] && !záznam[2].isEmpty() &&
							!záznam[1].isEmpty() && !záznam[0].isEmpty())
						{
							záznam[2] = záznam[2].toUpperCase();
							if (záznam[2].equals("Z") &&
								!začiatkyNázvov.contains(záznam[0], záznam[1]))
								začiatkyNázvov.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("K") &&
								!konceNázvov.contains(záznam[0], záznam[1]))
								konceNázvov.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("S") &&
								!stredyNázvov.contains(záznam[0], záznam[1]))
								stredyNázvov.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("P") &&
								!presnéNázvy.contains(záznam[0], záznam[1]))
								presnéNázvy.put(záznam[0], záznam[1]);
						}
					}
				}
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}

			začiatkyHodnôt.clear();
			konceHodnôt.clear();
			stredyHodnôt.clear();
			presnéHodnoty.clear();

			if (null != prekladyHodnôt) try
			{
				for (String[] záznam : prekladyHodnôt)
				{
					if (null != záznam && 3 <= záznam.length)
					{
						if (null != záznam[2] && null != záznam[1] &&
							null != záznam[0] && !záznam[2].isEmpty() &&
							!záznam[1].isEmpty() && !záznam[0].isEmpty())
						{
							záznam[2] = záznam[2].toUpperCase();
							if (záznam[2].equals("Z") &&
								!začiatkyHodnôt.contains(záznam[0], záznam[1]))
								začiatkyHodnôt.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("K") &&
								!konceHodnôt.contains(záznam[0], záznam[1]))
								konceHodnôt.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("S") &&
								!stredyHodnôt.contains(záznam[0], záznam[1]))
								stredyHodnôt.put(záznam[0], záznam[1]);
							else if (záznam[2].equals("P") &&
								!presnéHodnoty.contains(záznam[0], záznam[1]))
								presnéHodnoty.put(záznam[0], záznam[1]);
						}
					}
				}
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e/*, false*/);
			}
		}

		/**
		 * <p>Zapíše vlastnosť podporovaného údajového typu. Ak zavoláme túto
		 * metódu opakovane s rovnakým názvom vlastnosti, tak nastane
		 * prepísanie hodnoty tej istej vlastnosti. (V prípade kolízií
		 * odporúčame použiť {@link #mennýPriestorVlastností(String)
		 * menný priestor}.)</p>
		 * 
		 * <p class="caution"><b>Dôležité upozornenie na prípadnú
		 * kolíziu pri práci so súbormi obsahujúcimi vlastnosti!</b> <br />
		 *  <br />
		 * Najskôr niekoľko faktov. Prostredie grafického robota
		 * umožňuje otvoriť viacero súborov naraz. (<em>Za prvé každý robot
		 * smie pracovať s vlastným súborom, za druhé programátor smie
		 * vytvárať vlastné inštancie typu {@link Súbor Súbor}.</em>) Metóda
		 * {@link #otvorNaZápis(String) otvorNaZápis} okamžite po svojom
		 * úspešnom vykonaní kompletne vymaže celý obsah otváraného súboru
		 * bez ohľadu na to, čo obsahoval. Ak bol súbor otvorený na čítanie
		 * a použijeme ľubovoľnú z metód slúžiacich na čítanie vlastností,
		 * okamžite sa do inštancie súboru prečíta a spracuje celý obsah
		 * súboru predpokladajúc, že obsahuje údaje o vlastnostiach.
		 * Spracované údaje zostávajú v pamäti (v inštancii súboru) aj po
		 * zavretí súboru a sú opätovne použité pri zápise vlastností…
		 * Kolíziám a premiešavaniu obsahu rôznych súborov spracúvaných
		 * jednou inštanciou by sa za určitých okolností dalo zabrániť včasným
		 * zapnutím {@linkplain #odstraňujNepoužitéVlastnosti() režimu
		 * nepoužitých vlastností} (ktorý sa dotýka aj {@linkplain 
		 * #zapíšKomentárVlastností(String) komentárov} a {@linkplain 
		 * #zapíšPrázdnyRiadokVlastností() prázdnych riadkov}). Kolíziám
		 * spôsobeným spracovaním jedného súboru rôznymi inštanciami triedy
		 * {@link Súbor Súbor} sa zabrániť nedá. <br />
		 *  <br />
		 * <b>Aby ste sa vyhli prípadným problémom, používajte vždy na prácu
		 * s jedným súborom vlastností tú istú inštanciu triedy {@link Súbor
		 * Súbor} (buď {@linkplain GRobot#súbor inštanciu súboru v niektorom
		 * z robotov}, alebo vlastnú vytvorenú inštanciu).</b> <br />
		 *  <br />
		 * K dispozícii je aj metóda {@link #vymažVlastnosti() vymažVlastnosti},
		 * ktorú je možné použiť pred každým čítaním vlastností zo súboru a tým
		 * zabrániť nežiadúcim kolíziám.</p>
		 * 
		 * @param názov názov vlastnosti; nesmie byť prázdny ani obsahovať
		 *     nepovolené znaky (bodku, hranatú zátvorku alebo rovná sa)
		 * @param hodnota hodnota vlastnosti podporovaných údajových typov,
		 *     čo sú jednorozmerné polia primitívnych údajových typov a všetky
		 *     objekty vyjadriteľné v textovej podobe; v súčasnej verzii rámca
		 *     táto metóda špeciálne zaobchádza s objektmi farieb a polôh –
		 *     používa prislúchajúce metódy prevodu dostupné v rámci
		 *     programovacieho rámca; v súvislosti s tým je definovaná aj
		 *     prislúchajúca skupina metód slúžiaca na čítanie polôh a farieb
		 * 
		 * @see #vymažVlastnosť(String)
		 * @see #zapíšKomentárVlastností(String)
		 * @see #zapíšPrázdnyRiadokVlastností()
		 * @see #zachovávaNepoužitéVlastnosti()
		 * @see #zachovajNepoužitéVlastnosti()
		 * @see #odstraňujNepoužitéVlastnosti()
		 * 
		 * @throws GRobotException ak súbor nie je otvorený na zápis alebo
		 *     ak názov vlastnosti nespĺňa niektorú požiadavku
		 */
		public void zapíšVlastnosť(String názov, Object hodnota)
		{
			názov = názov.trim();
			overPlatnosťNázvuVlastnosti(názov);

			if (null != aktívnaSekcia.mennýPriestorVlastností)
				názov = aktívnaSekcia.mennýPriestorVlastností + '.' + názov;

			if (null == zápis)
				throw new GRobotException(
					"Súbor nie je otvorený na zápis.",
					"fileNotOpenForWriting");

			int indexOf;

			if (null == hodnota)
			{
				if (-1 != (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
				{
					Vlastnosť vlastnosť = aktívnaSekcia.vlastnosti.get(indexOf);
					vlastnosť.hodnota = null;
					vlastnosť.naZápis = true;
					aktívnaSekcia.naposledyZapísanáVlastnosť = indexOf;
				}
				else
				{
					aktívnaSekcia.vlastnosti.pridaj(názov, null, true);
					aktívnaSekcia.naposledyZapísanáVlastnosť =
						aktívnaSekcia.vlastnosti.size() - 1;
				}

				return;
			}

			StringBuffer reťazec;

			if (hodnota instanceof int[])
			{
				int[] pole = (int[])hodnota;
				reťazec = new StringBuffer();
				for (int i = 0; i < pole.length; ++i)
					reťazec.append(pole[i] + " ");
				hodnota = reťazec;
			}
			else if (hodnota instanceof float[])
			{
				float[] pole = (float[])hodnota;
				reťazec = new StringBuffer();
				for (int i = 0; i < pole.length; ++i)
					reťazec.append(pole[i] + " ");
				hodnota = reťazec;
			}
			else if (hodnota instanceof long[])
			{
				long[] pole = (long[])hodnota;
				reťazec = new StringBuffer();
				for (int i = 0; i < pole.length; ++i)
					reťazec.append(pole[i] + " ");
				hodnota = reťazec;
			}
			else if (hodnota instanceof double[])
			{
				double[] pole = (double[])hodnota;
				reťazec = new StringBuffer();
				for (int i = 0; i < pole.length; ++i)
					reťazec.append(pole[i] + " ");
				hodnota = reťazec;
			}
			else if (hodnota instanceof boolean[])
			{
				boolean[] pole = (boolean[])hodnota;
				reťazec = new StringBuffer();
				for (int i = 0; i < pole.length; ++i)
					reťazec.append(pole[i] + " ");
				hodnota = reťazec;
			}
			else if (hodnota instanceof char[])
			{
				reťazec = new StringBuffer();
				reťazec.append((char[])hodnota);
				hodnota = reťazec;
			}
			else if (hodnota instanceof Poloha)
			{
				hodnota = Bod.polohaNaReťazec((Poloha)hodnota);
			}
			else if (hodnota instanceof Point2D)
			{
				hodnota = Bod.bodNaRetazec((Point2D)hodnota);
			}
			else if (hodnota instanceof Color)
			{
				hodnota = Farba.farbaNaReťazec((Color)hodnota);
			}
			else if (hodnota instanceof Farebnosť)
			{
				hodnota = Farba.farbaNaReťazec((Farebnosť)hodnota);
			}

			if (-1 != (indexOf = aktívnaSekcia.vlastnosti.indexOf(názov)))
			{
				Vlastnosť vlastnosť = aktívnaSekcia.vlastnosti.get(indexOf);
				vlastnosť.hodnota = hodnota.toString();
				vlastnosť.naZápis = true;
				aktívnaSekcia.naposledyZapísanáVlastnosť = indexOf;
			}
			else
			{
				aktívnaSekcia.vlastnosti.pridaj(názov, hodnota.toString(), true);
				aktívnaSekcia.naposledyZapísanáVlastnosť =
					aktívnaSekcia.vlastnosti.size() - 1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšVlastnosť(String, Object) zapíšVlastnosť}.</p> */
		public void zapisVlastnost(String názov, Object hodnota)
			// throws IOException, IllegalArgumentException
		{ zapíšVlastnosť(názov, hodnota); }

		/**
		 * <p>Dovoľuje vkladať do textových súborov vlastností jednoriadkové
		 * komentáre. Ak s textovým súborom nebolo manipulované zvonka
		 * aplikácie a nebolo v ňom zmenené poradie zapísaných vlastností,
		 * nemali by sa v ňom vyskytnúť nežiaduce kópie komentárov.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @param komentár text komentára
		 * 
		 * @see #zapíšVlastnosť(String, Object)
		 * @see #zapíšPrázdnyRiadokVlastností()
		 */
		public void zapíšKomentárVlastností(String komentár)
		{
			++aktívnaSekcia.naposledyZapísanáVlastnosť;
			if (aktívnaSekcia.naposledyZapísanáVlastnosť >=
				aktívnaSekcia.vlastnosti.size())
			{
				aktívnaSekcia.vlastnosti.pridaj(";", komentár, true);
				aktívnaSekcia.naposledyZapísanáVlastnosť =
					aktívnaSekcia.vlastnosti.size();
			}
			else
			{
				Vlastnosť vlastnosť = aktívnaSekcia.vlastnosti.get(
					aktívnaSekcia.naposledyZapísanáVlastnosť);
				if (vlastnosť.názov.equals(";"))
				{
					vlastnosť.hodnota = komentár;
					vlastnosť.naZápis = true;
				}
				else
					aktívnaSekcia.vlastnosti.vlož(
						aktívnaSekcia.naposledyZapísanáVlastnosť,
						";", komentár, true);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšKomentárVlastností(String) zapíšKomentárVlastností}.</p> */
		public void zapisKomentarVlastnosti(String komentár) { zapíšKomentárVlastností(komentár); }

		/**
		 * <p>Dovoľuje vkladať do textových súborov vlastností prázdne riadky
		 * slúžiace ako oddeľovače. Ak s textovým súborom nebolo
		 * manipulované zvonka aplikácie a nebolo v ňom zmenené poradie
		 * zapísaných vlastností, nemali by sa v ňom vyskytnúť nežiaduce
		 * kópie prázdnych riadkov.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Prosím, venujte pozornosť
		 * upozorneniu pri metóde {@link #zapíšVlastnosť(String, Object)
		 * zapíšVlastnosť}!</p>
		 * 
		 * @see #zapíšVlastnosť(String, Object)
		 * @see #zapíšKomentárVlastností(String)
		 */
		public void zapíšPrázdnyRiadokVlastností()
		{
			++aktívnaSekcia.naposledyZapísanáVlastnosť;
			if (aktívnaSekcia.naposledyZapísanáVlastnosť >=
				aktívnaSekcia.vlastnosti.size())
			{
				aktívnaSekcia.vlastnosti.pridaj("", null, true);
				aktívnaSekcia.naposledyZapísanáVlastnosť =
					aktívnaSekcia.vlastnosti.size();
			}
			else
			{
				Vlastnosť vlastnosť = aktívnaSekcia.vlastnosti.get(
					aktívnaSekcia.naposledyZapísanáVlastnosť);
				if (vlastnosť.názov.equals(""))
					vlastnosť.naZápis = true;
				else
					aktívnaSekcia.vlastnosti.vlož(
						aktívnaSekcia.naposledyZapísanáVlastnosť,
						"", null, true);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšPrázdnyRiadokVlastností() zapíšPrázdnyRiadokVlastností}.</p> */
		public void zapisPrazdnyRiadokVlastnosti() { zapíšPrázdnyRiadokVlastností(); }


	// Čítanie údajov

		/**
		 * <p>Overí, či sa prúd údajov čítaný zo súboru momentálne nachádza na
		 * konci riadka. Súbor musí byť otvorený na čítanie.</p>
		 * 
		 * @return ak je aktuálna poloha čítania práve na konci riadka, tak
		 *     táto metóda vráti {@code valtrue}, inak {@code valfalse}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 */
		public boolean koniecRiadka() throws IOException
		{
			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			if (prvýRiadok) čítanieRiadka();
			return 0 == prečítanýRiadok.length();
		}

		/**
		 * <p>Overí, či sa prúd údajov čítaný zo súboru skončil. Súbor musí byť
		 * otvorený na čítanie.</p>
		 * 
		 * @return ak už nie sú v súbore otvorenom na čítanie k dispozícii
		 *     žiadne údaje, tak táto metóda vráti {@code valtrue}, inak
		 *     {@code valfalse}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 */
		public boolean koniecSúboru() throws IOException
		{
			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			if (prvýRiadok || (0 == prečítanýRiadok.length() &&
				overĎalšíRiadok)) čítanieRiadka();
			return koniecSúboru;
		}

		/** <p><a class="alias"></a> Alias pre {@link #koniecSúboru() koniecSúboru}.</p> */
		public boolean koniecSuboru() throws IOException
		{ return koniecSúboru(); }

		/**
		 * <p>Prečíta zo súboru otvoreného na čítanie riadok textu a vráti ho
		 * v objekte typu {@link java.lang.String String}.</p>
		 * 
		 * @return prečítaný riadok textu
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 */
		public String čítajRiadok() throws IOException
		{
			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			if (prvýRiadok || (0 == prečítanýRiadok.length() &&
				overĎalšíRiadok)) čítanieRiadka();
			if (koniecSúboru) return null;

			String riadok = new String(prečítanýRiadok);
			prečítanýRiadok.setLength(0);
			overĎalšíRiadok = true;
			return riadok;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajRiadok() čítajRiadok}.</p> */
		public String citajRiadok() throws IOException { return čítajRiadok(); }

		/**
		 * <p>Umožňuje dočítať zo súboru otvoreného na čítanie aktuálne
		 * spracúvaný riadok textu. Ak vo vnútornom zásobníku súboru jestvujú
		 * zvyškové údaje z naposledy spracúvaného riadka, tak sú vrátené
		 * v objekte typu {@link java.lang.String String} a následne sú
		 * vymazané z vnútorného zásobníka súboru. Inak je vrátený prázdny
		 * reťazec. To znamená, že spracovanie ďalšieho riadka sa nezačne ako
		 * tomu je pri volaní metódy {@link #čítajRiadok() čítajRiadok}.</p>
		 * 
		 * @return zvyškové údaje z aktuálne spracúvaného riadka textu alebo
		 *     prázdny reťazec
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 */
		public String dočítajRiadok() throws IOException
		{
			if (koniecRiadka())
			{
				overĎalšíRiadok = true;
				return "";
			}

			String riadok = new String(prečítanýRiadok);
			prečítanýRiadok.setLength(0);
			overĎalšíRiadok = true;
			return riadok;
		}

		/** <p><a class="alias"></a> Alias pre {@link #dočítajRiadok() dočítajRiadok}.</p> */
		public String docitajRiadok() throws IOException { return dočítajRiadok(); }

		/**
		 * <p>Prečíta zo súboru otvoreného na nasledujúci znak a vráti jeho
		 * hodnotu. Znaky nových riadkov sú touto metódou ignorované. Ak
		 * metóda dosiahne koniec súboru, vráti hodnotu {@code valnull}.</p>
		 * 
		 * @return prečítaný znak v objekte typu {@link java.lang.Character
		 *     Character} alebo {@code valnull}
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 */
		public Character čítajZnak() throws IOException
		{
			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			if (prvýRiadok) čítanieRiadka();

			while (0 == prečítanýRiadok.length())
			{
				čítanieRiadka();
				if (koniecSúboru) return null;
			}

			Character znak = new Character(prečítanýRiadok.charAt(0));
			prečítanýRiadok.deleteCharAt(0);
			overĎalšíRiadok = true;
			return znak;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajZnak() čítajZnak}.</p> */
		public Character citajZnak() throws IOException { return čítajZnak(); }

		/**
		 * <p>Prečíta zo súboru otvoreného na čítanie text od aktuálnej polohy
		 * v súbore po najbližšiu medzeru alebo koniec riadka a pokúsi sa ho
		 * previesť na celé číslo…</p>
		 * 
		 * @return výsledok v objekte typu {@link java.lang.Long Long} ak je
		 *     prečítaný text možné previesť na celé číslo alebo {@code 
		 *     valnull} v prípade zlyhania
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public Long čítajCeléČíslo() throws IOException
		{
			if (preskočiťMedzery()) return null;

			Long číslo = null;
			int indexOf = prečítanýRiadok.indexOf(" ");

			if (-1 == indexOf)
			{
				try
				{
					číslo = Long.valueOf(prečítanýRiadok.toString());
					prečítanýRiadok.setLength(0);
					overĎalšíRiadok = true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}
			else
			{
				try
				{
					číslo = Long.valueOf(prečítanýRiadok.substring(0, indexOf));
					prečítanýRiadok.delete(0, indexOf + 1);
					overĎalšíRiadok = true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}

			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajCeléČíslo() čítajCeléČíslo}.</p> */
		public Long citajCeleCislo() throws IOException { return čítajCeléČíslo(); }

		/**
		 * <p>Prečíta zo súboru otvoreného na čítanie text od aktuálnej polohy
		 * v súbore po najbližšiu medzeru alebo koniec riadka a pokúsi sa ho
		 * previesť na reálne číslo…</p>
		 * 
		 * @return výsledok v objekte typu {@link java.lang.Double
		 *     Double} ak je prečítaný text možné previesť na rálne
		 *     číslo alebo {@code valnull} v prípade zlyhania
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public Double čítajReálneČíslo() throws IOException
		{
			if (preskočiťMedzery()) return null;

			Double číslo = null;
			int indexOf = prečítanýRiadok.indexOf(" ");

			if (-1 == indexOf)
			{
				try
				{
					číslo = Double.valueOf(prečítanýRiadok.toString());
					prečítanýRiadok.setLength(0);
					overĎalšíRiadok = true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}
			else
			{
				try
				{
					číslo = Double.valueOf(prečítanýRiadok.substring(0,
						indexOf));
					prečítanýRiadok.delete(0, indexOf + 1);
					overĎalšíRiadok = true;
				}
				catch (Exception e)
				{
					GRobotException.vypíšChybovéHlásenia(e/*, false*/);
				}
			}

			return číslo;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajReálneČíslo() čítajReálneČíslo}.</p> */
		public Double citajRealneCislo() throws IOException
		{ return čítajReálneČíslo(); }

		/**
		 * <p>Prečíta zo súboru otvoreného na čítanie text od aktuálnej polohy
		 * v súbore po najbližšiu medzeru alebo koniec riadka a pokúsi sa ho
		 * previesť na objekt typu {@link java.lang.Boolean Boolean}
		 * obsahujúci pravdivostnú hodnotu {@code valtrue}/&#8203;{@code 
		 * valfalse}. Platí, že ak bol nájdený text „{@code valtrue}“ bez
		 * ohľadu na veľkosť písmen, bude hodnota objektu {@code valtrue},
		 * inak {@code valfalse}.</p>
		 * 
		 * @return výsledok v objekte typu {@link java.lang.Boolean
		 *     Boolean} alebo {@code valnull} v prípade zlyhania
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public Boolean čítajBoolean() throws IOException
		{
			if (preskočiťMedzery()) return null;

			Boolean hodnota = null;
			int indexOf = prečítanýRiadok.indexOf(" ");

			if (-1 == indexOf)
			{
				if (!prečítanýRiadok.toString().equalsIgnoreCase(nullString))
					hodnota = Boolean.valueOf(prečítanýRiadok.toString());
				prečítanýRiadok.setLength(0);
				overĎalšíRiadok = true;
			}
			else
			{
				if (!prečítanýRiadok.substring(0, indexOf).
					equalsIgnoreCase(nullString))
					hodnota = Boolean.valueOf(prečítanýRiadok.
						substring(0, indexOf));
				prečítanýRiadok.delete(0, indexOf + 1);
				overĎalšíRiadok = true;
			}

			return hodnota;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajBoolean() čítajBoolean}.</p> */
		public Boolean citajBoolean() throws IOException { return čítajBoolean(); }

		/**
		 * <p>Pokúsi sa prečítať zo súboru otvoreného na čítanie toľko textu,
		 * koľko je potrebné na naplnenie zadaného počtu objektov, pričom
		 * objem prečítaného textu je závislý od typu objektu. Sú povolené
		 * len niektoré údajové typy:</p>
		 * 
		 * <ul>
		 * <li>{@link java.lang.StringBuffer StringBuffer} – prečíta riadok
		 * textu;</li>
		 * <li>{@code typeint}{@code []}, {@code typelong}{@code []} – prečíta
		 * toľko celých čísel, koľko prvkov má pole;</li>
		 * <li>{@code typefloat}{@code []}, {@code typedouble}{@code []} –
		 * prečíta toľko reálnych čísel, koľko prvkov má pole;</li>
		 * <li>{@code typeboolean}{@code []} – prečíta toľko pravdivostných
		 * hodnôt, koľko prvkov má pole;</li>
		 * <li>{@code typechar}{@code []} – prečíta toľko znakov, koľko prvkov
		 * má pole.</li>
		 * </ul>
		 * 
		 * <p>Ak metóda prijme nepovolený údajový typ, vrhne výnimku
		 * {@link GRobotException GRobotException} s jazykovým
		 * identifikátorom {@code unsupportedDataType}.</p>
		 * 
		 * @param objekty variabilný zoznam parametrov s povolenými údajovými
		 *     typmi – pozri zoznam vyššie
		 * @return počet údajových jednotiek, ktorými sa podarilo naplniť
		 *     zadané objekty; návratová hodnota je sumárna a je silno
		 *     závislá od údajových typov objektov, ktoré boli zadané do
		 *     variabilného zoznamu parametrov metódy; napríklad objekt poľa
		 *     typu {@code typeint} prispeje do súčtu počtom textových
		 *     blokov (oddelených medzerami a nasledujúcich v súbore za
		 *     sebou, hoci na viacerých riadkoch), ktoré sa podarilo úspešne
		 *     previesť na celé čísla a zapísať do prvkov poľa, objekt
		 *     poľa typu {@code typechar} prispeje do súčtu počtom znakov,
		 *     ktoré sa podarilo zapísať do prvkov poľa (znaky nových riadkov
		 *     sú ignorované), objekt typu {@link java.lang.StringBuffer
		 *     StringBuffer} prispeje dĺžkou prečítaného riadka a podobne;
		 *     ak potrebujete jednotlivé hodnoty odlíšiť, tak vykonajte
		 *     viacero samostatných volaní tejto metódy vždy s jedným
		 *     parametrom
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na čítanie
		 *     alebo metóda prijala argument nepodporovaného údajového typu
		 *     (čím sa spracovanie automaticky ukončí)
		 */
		public int čítaj(Object... objekty) throws IOException
		{
			if (null == čítanie)
				throw new GRobotException(
					"Súbor nie je otvorený na čítanie.",
					"fileNotOpenForReading");

			int prečítané = 0;

			for (Object obj : objekty)
			{
				if (obj instanceof StringBuffer)
				{
					((StringBuffer)obj).setLength(0);
					String riadok = čítajRiadok();
					((StringBuffer)obj).append(riadok);
					prečítané += riadok.length();
				}
				else if (obj instanceof int[])
				{
					int[] pole = (int[])obj;
					Long číslo;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (číslo = this.čítajCeléČíslo()))
						{
							pole[i] = číslo.intValue();
							++prečítané;
						}
						else
							pole[i] = 0;
					}
				}
				else if (obj instanceof float[])
				{
					float[] pole = (float[])obj;
					Double číslo;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (číslo = this.čítajReálneČíslo()))
						{
							pole[i] = číslo.floatValue();
							++prečítané;
						}
						else
							pole[i] = 0;
					}
				}
				else if (obj instanceof long[])
				{
					long[] pole = (long[])obj;
					Long číslo;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (číslo = this.čítajCeléČíslo()))
						{
							pole[i] = číslo.longValue();
							++prečítané;
						}
						else
							pole[i] = 0;
					}
				}
				else if (obj instanceof double[])
				{
					double[] pole = (double[])obj;
					Double číslo;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (číslo = this.čítajReálneČíslo()))
						{
							pole[i] = číslo.doubleValue();
							++prečítané;
						}
						else
							pole[i] = 0;
					}
				}
				else if (obj instanceof boolean[])
				{
					boolean[] pole = (boolean[])obj;
					Boolean hodnota;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (hodnota = this.čítajBoolean()))
						{
							pole[i] = hodnota;//.booleanValue();
							++prečítané;
						}
						else
							pole[i] = false;
					}
				}
				else if (obj instanceof char[])
				{
					char[] pole = (char[])obj;
					// čítanie.read(pole);
					Character hodnota;
					for (int i = 0; i < pole.length; ++i)
					{
						if (null != (hodnota = this.čítajZnak()))
						{
							pole[i] = hodnota;
							++prečítané;
						}
						else
							pole[i] = 0;
					}
				}
				else
				{
					throw new GRobotException(
						"Počas čítania zo súboru nastal pokus o vloženie " +
						"údajov do premennej nasledujúceho nepodporovaného " +
						"údajového typu: : " +
						obj.getClass().getCanonicalName(),
						"unsupportedDataType",
						obj.getClass().getCanonicalName(),
						new IllegalArgumentException());
				}
			}

			return prečítané;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítaj(Object[]) čítaj}.</p> */
		public void citaj(Object... objekty) throws IOException
		{ čítaj(objekty); }


	// Zápis údajov

		/**
		 * <p>Zapíše do súboru otvoreného na zápis určený reťazec.</p>
		 * 
		 * @param text text, ktorý má byť zapísaný do súboru otvoreného na
		 *     zápis
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na zápis
		 */
		public void zapíšReťazec(String text) throws IOException
		{
			if (null == zápis)
				throw new GRobotException(
					"Súbor nie je otvorený na zápis.",
					"fileNotOpenForWriting");
			zápis.write(text);
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšReťazec(String) zapíšReťazec}.</p> */
		public void zapisRetazec(String text) throws IOException { zapíšReťazec(text); }

		/**
		 * <p>Zapíše do súboru otvoreného na zápis riadok textu.</p>
		 * 
		 * @param text text, ktorý má byť zapísaný do samostatného riadka
		 *     súboru otvoreného na zápis
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na zápis
		 */
		public void zapíšRiadok(String text) throws IOException
		{
			if (null == zápis)
				throw new GRobotException(
					"Súbor nie je otvorený na zápis.",
					"fileNotOpenForWriting");
			zápis.write(text + "\r\n");
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšRiadok(String) zapíšRiadok}.</p> */
		public void zapisRiadok(String text) throws IOException { zapíšRiadok(text); }

		/**
		 * <p>Zapíše do súboru otvoreného na zápis prázdny riadok.</p>
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 * @throws GRobotException ak súbor nie je otvorený na zápis
		 */
		public void zapíšRiadok() throws IOException
		{
			if (null == zápis)
				throw new GRobotException(
					"Súbor nie je otvorený na zápis.",
					"fileNotOpenForWriting");
			zápis.write("\r\n");
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíšRiadok() zapíšRiadok}.</p> */
		public void zapisRiadok() throws IOException { zapíšRiadok(); }

		/**
		 * <p>Zapíše do súboru otvoreného na zápis textovú podobu zoznamu
		 * objektov rôzneho údajového typu (napríklad celé čísla {@code 
		 * typeint} budú zapísané ako séria číslic – čísla vo forme textu).
		 * Táto metóda automaticky oddeľuje číselné argumenty medzerami
		 * (resp. argumenty, ktoré nie sú priamo objektami typu {@link 
		 * java.lang.String String} ani {@link java.lang.StringBuffer
		 * StringBuffer}), čo je výhodné pri čítaní údajov zo súboru.</p>
		 * 
		 * @param objekty séria objektov rôzneho údajového typu; objekt musí
		 *     byť vyjadriteľný v textovej podobe
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public void zapíš(Object... objekty) throws IOException
		{
			for (Object obj : objekty)
			{
				if (obj instanceof String || obj instanceof StringBuffer)
					zapíšReťazec(obj.toString());
				else if (obj instanceof int[])
				{
					int[] pole = (int[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i] + " ");
				}
				else if (obj instanceof float[])
				{
					float[] pole = (float[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i] + " ");
				}
				else if (obj instanceof long[])
				{
					long[] pole = (long[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i] + " ");
				}
				else if (obj instanceof double[])
				{
					double[] pole = (double[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i] + " ");
				}
				else if (obj instanceof boolean[])
				{
					boolean[] pole = (boolean[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i] + " ");
				}
				else if (obj instanceof char[])
				{
					zapíšReťazec(new String((char[])obj));
					/*
					char[] pole = (char[])obj;

					for (int i = 0; i < pole.length; ++i)
						zapíšReťazec(pole[i]);
					*/
				}
				else
					zapíšReťazec(obj + " ");
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíš(Object[]) zapíš}.</p> */
		public void zapis(Object... objekty) throws IOException { zapíš(objekty); }
}
