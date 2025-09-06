
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

package knižnica;

import knižnica.Zoznam;

import java.util.Collection;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Táto trieda zoskupuje a zjednodušuje prácu s regulárnymi výrazmi.
 * V podstate ide o zaobalenie práce triedami {@link Pattern Pattern}
 * a {@link Matcher Matcher}. Okrem toho implementuje jedno z častých
 * použití regulárnych výrazov – aplikovanie zoznamu filtrov na zadaný
 * reťazec.</p>
 * 
 * <p>Táto trieda reprezentuje {@linkplain Zoznam zoznam} filtrov, ktoré
 * budú postupne aplikované na reťazec zadaný ako parameter metódy {@link 
 * #použi(String) použi}. Každý z filtrov môže mať nastavený príznak {@link 
 * Filter#zastav zastav}, ktorého hodnota {@code valtrue} indikuje, že sa
 * má spracovanie po úspešnej aplikácii tohto filtra (ako prvku v zozname
 * filtrov) zastaviť (a nemá sa pokračovať ďalším filtrom v rámci zoznamu).</p>
 * 
 * <p><b>Príklady použitia:</b></p>
 * 
 * <p>Nižšie sú fragmenty kódov demonštrujúce rôzne situácie. (Sú v nich
 * použité rôzne spôsoby konštrukcie filtrov.)</p>
 * 
 * <p>Prvý príklad odfiltruje meno šablóny PNG sekvencie a vytvorí z neho meno
 * konfiguračného súboru časovania.</p>
 * 
 * <pre CLASS="example">
	{@code kwdfinal} {@link String String} názovPoložky = {@code srg"animácia-***.png"};

	{@code kwdfinal} {@code currFiltre} filterNázvuSúboru = {@code kwdnew} {@link Filtre#Filtre(Filter...) Filtre}(
		{@code kwdnew} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter}({@code srg"-?\\*+-?"}, {@code srg"-"}),
		{@code kwdnew} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter}({@code srg"(?i:\\.png$)"}, {@code srg".tim"}),
		{@code kwdnew} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter}({@code srg"-+\\.tim"}, {@code srg".tim"}));

	{@link String String} názovSúboru = filterNázvuSúboru.{@link Filtre#použi(String) použi}(názovPoložky);
	{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"názovSúboru: "} + názovSúboru);
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <pre CLASS="example">
	názovSúboru: animácia.tim
	</pre>
 * 
 * <p>V druhom príklade filtre hľadajú adresy v atribútoch {@code href} HTML
 * kódu, ale len v tele HTML dokumentu:</p>
 * 
 * <pre CLASS="example">
	{@link String String} odpoveď = {@code srg"<!DOCTYPE html>\r\n<html>\r\n<head>"} +
		{@code srg"\r\n<meta charset=\"UTF-8\" />\r\n<title>Titulok</title>"} +
		{@code srg"\r\n<link href=\"style.css\" rel=\"stylesheet\" "} +
		{@code srg"type=\"text/css\" />\r\n</head>\r\n\r\n<body>\r\n\r\n"} +
		{@code srg"<header><h1>Nadpis v hlavičke</h1></header>\r\n\r\n"} +
		{@code srg"<section>\r\n\r\n<div>\r\n\t<p><a href=\"https://"} +
		{@code srg"pdf.truni.sk/\">Pedagogická fakulta</a></p>\r\n</div>"} +
		{@code srg"\r\n\r\n</section>\r\n</body>\r\n</html>"};

	{@code kwdfinal} {@code currFiltre} hľadajAdresy = {@code kwdnew} {@link Filtre#Filtre(String...) Filtre}(
		{@code srg"[ \r\n\t]+"}, {@code srg" "},
		{@code srg".*?<body"}, {@code srg""},
		{@code srg".*?href=\"([^\"]+)"}, {@code srg"$1\n"},
		{@code srg"[^\n]+$"}, {@code srg""});

	{@code kwdfinal} {@link Pattern Pattern} rozdeľAdresy = {@link Pattern Pattern}.{@link Pattern#compile(String) compile}("\n");

	odpoveď = hľadajAdresy.{@link Filtre#použi(String) použi}(odpoveď);
	{@code kwdfinal} {@link String String}[] adresy = rozdeľAdresy.{@link Pattern#split(CharSequence) split}(odpoveď);
	{@code kwdfor} ({@link String String} adresa : adresy)
		{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"adresa: "} + adresa);
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <pre CLASS="example">
	adresa: https://pdf.truni.sk/
	</pre>
 * 
 * <p>Tretí príklad spojí riadky kódované algoritmom Base64 do jedného reťazca
 * a odfiltruje začiatočný identifikátor {@code data:…}.</p>
 * 
 * <pre CLASS="example">
	{@code kwdfinal} {@code currFiltre} spojBase64 = {@code kwdnew} {@link Filtre#Filtre() Filtre}();
	spojBase64.{@link Filtre#pridaj(String, String) pridaj}({@code srg"^\\s*data:.*?;base64,"}, {@code srg""});
	spojBase64.{@link Filtre#pridaj(String, String) pridaj}({@code srg"\\s+"}, {@code srg""});

	{@link String String} naSpojenie = {@code srg"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAg"} +
		{@code srg"CAYAAABzenr0AAABb0lEQVR42mN48IDh/0BihlEHjDqA\r\n\tFMV37jP/X3JZ/3/8kdT/"} +
		{@code srg"ert6/rNvXPWfYcMGMFbePuV/xOFssDxIHdUdMPmC7X+F7dPhFuLDIHUg\r\n\t9VRxwM37"} +
		{@code srg"bP8DDxUSZTE69j1QDNZPtgNAmp3316AYyrd56f/MY7H/V1/R/H/pLjdY3dW7HP83XVP5\r"} +
		{@code srg"\n\tX3Ai4r/QlkUo6m32NuJ1BF4HoPs8+nDG/3N3+PD6COSopKPJGCFBsgNAcYhsCMh3pC"} +
		{@code srg"RYkHpk/bjS\r\n\tBAOu1I6c4EA+JyeLIYcEyDxsuQOrA0BZCTnOCQU7vugQ3TIfbtbCy0"} +
		{@code srg"bEOQCUz2GaQAmOkoIGOSpA\r\n\t5QRRDgAVMjBNoNROiQNAuQO5sCLKASwb18I1wbIauR"} +
		{@code srg"iURZETI1EOwKeBrPKeVAcMeAgMeBoY8Fww\r\n\t4OXAgJeEA14X4KsNCeUKkHzasQTKa0"} +
		{@code srg"Ny2wPIcU5xe2DAW0SDok2Ir1WMXGKCLAXlc1BWo0mreLRj\r\n\tMmwdAABfpbXzmG2kBQ"} +
		{@code srg"AAAABJRU5ErkJggg=="};

	{@link String String} spojené = spojBase64.{@link Filtre#použi(String) použi}(naSpojenie);
	{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"spojené: "} + spojené);
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p>Výsledkom bude reťazec vhodný na prevod z kódovania Base64 do binárneho
 * tvaru (tu je umelo zalomený na zlepšenie čitateľnosti):</p>
 * 
 * <pre CLASS="example">
	spojené: iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABb0lEQVR42mN48IDh/0BihlE
	HjDqAFMV37jP/X3JZ/3/8kdT/ert6/rNvXPWfYcMGMFbePuV/xOFssDxIHdUdMPmC7X+F7dPhFuLDIHU
	g9VRxwM37bP8DDxUSZTE69j1QDNZPtgNAmp3316AYyrd56f/MY7H/V1/R/H/pLjdY3dW7HP83XVP5X3A
	i4r/QlkUo6m32NuJ1BF4HoPs8+nDG/3N3+PD6COSopKPJGCFBsgNAcYhsCMh3pCRYkHpk/bjSBAOu1I6
	c4EA+JyeLIYcEyDxsuQOrA0BZCTnOCQU7vugQ3TIfbtbCy0bEOQCUz2GaQAmOkoIGOSpA5QRRDgAVMjB
	NoNROiQNAuQO5sCLKASwb18I1wbIauRiURZETI1EOwKeBrPKeVAcMeAgMeBoY8Fww4OXAgJeEA14X4Ks
	NCeUKkHzasQTKa0Ny2wPIcU5xe2DAW0SDok2Ir1WMXGKCLAXlc1BWo0mreLRjMmwdAABfpbXzmG2kBQA
	AAABJRU5ErkJggg==
	</pre>
 * 
 * <p>Posledný príklad hľadá riadky časových značiek titulkov vo formáte SRT
 * a v nájdených riadkoch identifikuje počiatočnú a koncovú časovú známku.</p>
 * 
 * <pre CLASS="example">
	{@code kwdfinal} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter} filterČasu = {@code kwdnew} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter}(
		{@code srg"([0-9]+):([0-9]+):([0-9]+),([0-9]+)"}, {@code valnull});

	{@code typelong} dajČas({@link String String} s)
	{
		{@link Matcher Matcher} ma = filterČasu.zhoda(s);
		{@code kwdif} (ma.matches())
		{
			{@code typedouble} mili = {@link Svet Svet}.{@link Svet#reťazecNaReálneČíslo(String) reťazecNaReálneČíslo}({@code srg"0,"} + ma.group({@code num4}));
			{@code kwdreturn} {@link Svet Svet}.{@link Svet#reťazecNaCeléČíslo(String) reťazecNaCeléČíslo}(ma.{@link Matcher#group(int) group}({@code num1})) * {@code num3600000} + {@code comm// hr}
				{@link Svet Svet}.{@link Svet#reťazecNaCeléČíslo(String) reťazecNaCeléČíslo}(ma.{@link Matcher#group(int) group}({@code num2})) * {@code num60000} + {@code comm// mt}
				{@link Svet Svet}.{@link Svet#reťazecNaCeléČíslo(String) reťazecNaCeléČíslo}(ma.{@link Matcher#group(int) group}({@code num3})) * {@code num1000} + {@code comm// sc}
				(long)(mili * {@code num1000.0}); {@code comm// ms}
		}
		{@code kwdreturn} -{@code num1};
	}

	<hr/>
	{@code kwdfinal} {@code currFiltre}.{@link Filtre.Filter#Filtre.Filter(String, String) Filter} časTitulkov = {@code kwdnew} Filtre.{@link Filtre.Filter#Filtre.Filter(String, String) Filter}(
		{@code srg"^\\s*([0-9:,]+)\\s*-->\\s*([0-9:,]+)\\s*$"}, {@code valnull});

	{@link String String}[] titulky = {
		{@code srg"1"},
		{@code srg"00:00:00,000 --> 00:00:02,750"},
		{@code srg"No a teraz úplná zmena."},
		{@code srg""},
		{@code srg"2"},
		{@code srg"00:00:03,500 --> 00:00:07,500"},
		{@code srg"Toto je úplne neznámy chlapík:"},
		{@code srg"Thomas Fantet de Lagny"},
		{@code srg""},
		{@code srg"3"},
		{@code srg"00:00:07,875 --> 00:00:09,750"},
		{@code srg"To je matematik…"},
	};

	{@code kwdfor} ({@link String String} údaj : titulky)
	{
		{@code kwdif} (údaj.{@link String#isEmpty() isEmpty}()) {@code kwdcontinue};

		{@link Matcher Matcher} ma = časTitulkov.{@link Filter#zhoda(String) zhoda}(údaj);

		{@code kwdif} (ma.{@link Matcher#matches() matches}())
		{
			{@code typelong} začiatok = dajČas(ma.{@link Matcher#group(int) group}({@code num1}));
			{@code typelong} koniec = dajČas(ma.{@link Matcher#group(int) group}({@code num2}));
			{@code comm// …}
			{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"začiatok: "} + začiatok);
			{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"koniec: "} + koniec);
		}
		{@code kwdelse} {@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"nie je čas"});
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <pre CLASS="example">
	nie je čas
	začiatok: 0
	koniec: 2750
	nie je čas
	nie je čas
	začiatok: 3500
	koniec: 7500
	nie je čas
	nie je čas
	nie je čas
	začiatok: 7875
	koniec: 9750
	nie je čas
	</pre>
 * 
 * 
 * 
 * @see Zoznam
 * @see Filtre.Filter
 */
@SuppressWarnings("serial")
public class Filtre extends Zoznam<Filtre.Filter>
{
	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }


	/**
	 * <p>Táto vnorená trieda je trieda prvkov zoznamu triedy {@link Filtre
	 * Filtre} a zároveň môže slúžiť na sprostredkovanie jednoduchších úkonov
	 * s regulárnymi výrazmi.</p>
	 * 
	 * <hr />
	 * 
	 * <p>Príklad využitia tejto triedy ukazuje miniprojekt dostupný tu: <a
	 * target="_blank" href="https://github.com/raubirius/FilterFiles"
	 * >FilterFiles</a>. (Tento miniprojekt bol pôvodne inšpiráciou na vznik
	 * tejto triedy rámca, ktorá bola potom spätne do tohto miniprojektu
	 * zapracovaná.)</p>
	 * 
	 * @see Filtre
	 */
	public static class Filter
	{
		/**
		 * <p>Do tohto atribútu sa ukladá inštancia vzoru vytvoreného podľa
		 * parametra {@code vzor} vloženého do {@linkplain 
		 * Filtre.Filter#Filtre.Filter(String, String) konštruktora}. (Atribút
		 * je dostupný na čítanie aj zápis. Ak sa jeho obsah zmení, filter
		 * bude pracovať so zmeneným obsahom.)</p>
		 */
		public Pattern vzor;

		/**
		 * <p>Tento atribút obsahuje šablónu nahrádzania vloženú do parametra
		 * {@linkplain Filtre.Filter#Filtre.Filter(String, String)
		 * konštruktora} {@code nahradenie}.</p>
		 */
		public String nahradenie;

		/**
		 * <p>Tento atribút je používaný v rámci nadradenej triedy {@link 
		 * Filtre Filtre}. Metóda {@link Filtre#použi(String)
		 * Filtre.použi(reťazec)} ho používa na overenie toho, či sa má
		 * proces fitrovania zastaviť po úspešnej aplikácii tohto filtra.
		 * (Pozri aj opis metódy {@link Filtre#čítaj(String)
		 * Filtre.čítaj(reťazec)}.)</p>
		 */
		public boolean zastav = false;

		/**
		 * <p>Tento atribút je naplnený po volaní metód {@link #zhoda(String)
		 * zhoda}, {@link #nahradenie(String) nahradenie} a {@link 
		 * Filtre#použi(String) Filtre.použi(reťazec)}. Ak ho nenaplníme sami
		 * iným spôsobom, tak bude jeho hodnota rovná {@code valnull}.</p>
		 */
		public Matcher zhoda = null;

		/**
		 * <p>Konštruktor filtra. Parameter {@code vzor} musí obsahovať
		 * platný regulárny výraz, inak vznikne výnimka. Pozri aj metódu:
		 * {@link Pattern Pattern}{@code .}{Pattern#compile(String)
		 * compile}{@code (regex)}.</p>
		 * 
		 * @param vzor regulárny výraz na porovnávanie reťazcov (na ktoré bude
		 *     aplikovaný tento filter)
		 * @param nahradenie šablóna reťazca na nahrádzanie zhôd podľa
		 *     regulárneho výrazu v parametri {@code vzor}
		 */
		public Filter(String vzor, String nahradenie)
		{
			this.vzor = Pattern.compile(vzor);
			this.nahradenie = nahradenie;
		}

		/**
		 * <p>Porovná zadaný reťazec so vzorom zadávaným do {@linkplain 
		 * Filtre.Filter#Filtre.Filter(String, String) konštruktora} a vráti
		 * zodpovedajúci objekt {@link Matcher Matcher}.</p>
		 * 
		 * @param reťazec reťazec na porovnanie podľa vzoru (regulárneho
		 *     výrazu) tohto filtra
		 * @return objekt {@link Matcher Matcher}, ktorý dovoľuje overiť, či
		 *     zadaný reťazec alebo jeho časť vyhovuje vzoru tohto filtra
		 */
		public Matcher zhoda(String reťazec)
		{
			return zhoda = vzor.matcher(reťazec);
		}

		/**
		 * <p>Nahradí všetky časti zadaného reťazca, ktoré vyhovujú vzoru
		 * (regulárnemu výrazu) tohto filtra šablónou zadanou do konštruktora.
		 * Pozri {@link Filtre.Filter#Filtre.Filter(String, String)
		 * Filter}{@code (vzor, nahradenie)}. Výsledok nahrádzania vráti
		 * v návratovej hodnote. Ak nebolo vykonané žiadne nahradenie, tak
		 * metóda vráti obsah záložného reťazca.</p>
		 * 
		 * @param reťazec reťazec, v ktorom sa budú hľadať zhody podľa vzoru
		 *     (regulárneho výrazu), ktoré budú nahradené podľa šablóny
		 *     nahrádzania zadanej do konštruktora tohto filtra
		 * @param záložnýReťazec záložný reťazec, ktorý bude vrátený
		 *     v prípade, že nebolo vykonané žiadne nahradenie;
		 *     (tip: ak je argument priamo parametrom, dá sa použiť operátor
		 *     zhody ({@code ==}) na overenie toho, či bolo niečo nahradené)
		 * @return výsledok spracovania (podľa pravidiel opísaných vyššie)
		 */
		public String nahradenie(String reťazec, String záložnýReťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.find()) return zhoda.replaceAll(nahradenie);
			return záložnýReťazec;
		}

		/**
		 * <p>Nahradí všetky časti zadaného reťazca, ktoré vyhovujú vzoru
		 * (regulárnemu výrazu) tohto filtra šablónou zadanou do konštruktora.
		 * Pozri {@link Filtre.Filter#Filtre.Filter(String, String)
		 * Filter}{@code (vzor, nahradenie)}. Výsledok nahrádzania vráti
		 * v návratovej hodnote. Ak nebolo vykonané žiadne nahradenie, tak
		 * metóda vráti pôvodný tvar reťazca.</p>
		 * 
		 * @param reťazec reťazec, v ktorom sa budú hľadať zhody podľa vzoru
		 *     (regulárneho výrazu), ktoré budú nahradené podľa šablóny
		 *     nahrádzania zadanej do konštruktora tohto filtra;
		 *     (tip: ak je argument priamo parametrom, dá sa použiť operátor
		 *     zhody ({@code ==}) na overenie toho, či bolo niečo nahradené)
		 * @return výsledok spracovania (podľa pravidiel opísaných vyššie)
		 */
		public String nahradenie(String reťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.find()) return zhoda.replaceAll(nahradenie);
			return reťazec;
		}


		/**
		 * <p>Nahradí všetky časti zadaného reťazca, ktoré vyhovujú vzoru
		 * (regulárnemu výrazu) tohto filtra prostredníctvom funkcie zadanej
		 * do druhého parametra. Funkcia bude spustená pri každom nahrádzaní.
		 * Jej parametrom je aktuálna inštancia zhody ({@link Matcher Matcher})
		 * a návratovou hodnotou reťazec, ktorým bude nahradená časť vyhovujúca
		 * vzoru. Po dokončení spracovania je výsledok vrátený v návratovej
		 * hodnote tejto metódy. Ak nebolo vykonané žiadne nahradenie, tak
		 * metóda vráti pôvodný tvar reťazca.</p>
		 * 
		 * <p><b>Zdroje:</b></p>
		 * 
		 * <ul>
		 * <li><a
		 * href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#appendReplacement-java.lang.StringBuffer-java.lang.String-"
		 * target="_blank"><em>Matcher (Java Platform SE 8) –
		 * appendReplacement(StringBuffer sb, String replacement).</em> 1993,
		 * 2023, Oracle and/or its affiliates.</a> Citované: 30. júla
		 * 2023.</li>
		 * 
		 * <li><a
		 * href="https://www.whitebyte.info/programming/string-replace-with-callback-in-java-like-in-javascript"
		 * target="_blank">Russler, Nick: <em>String replace with callback in
		 * Java (like in JavaScript).</em> WhiteByte.</a> 8. december 2014
		 * Citované: 30. júla 2023. (Nájdené vďaka: <a
		 * href="https://stackoverflow.com/a/27359491" target="_blank">What
		 * is the equivalent of Regex-replace-with-function-evaluation in Java
		 * 7? – Stack Overflow.</a>)</li>
		 * </ul>
		 * 
		 * @param reťazec predloha – reťazec určený na spracovanie;
		 *     (tip: ak je argument priamo parametrom, dá sa použiť operátor
		 *     zhody ({@code ==}) na overenie toho, či bolo niečo nahradené)
		 * @param funkcia funkcia, ktorá bude spustená pri každom nahrádzaní
		 * @return výsledok spracovania (podľa pravidiel opísaných vyššie)
		 */
		public String nahradenie(
			String reťazec, Function<Matcher, String> funkcia)
		{
			zhoda = vzor.matcher(reťazec);

			if (zhoda.find())
			{
				StringBuffer sb = new StringBuffer();
				do {
					zhoda.appendReplacement(sb, funkcia.apply(zhoda));
				} while (zhoda.find());
				zhoda.appendTail(sb);
				return sb.toString();
			}

			return reťazec;
		}


		/**
		 * <p>Nahradí celý zadaný reťazec šablónou toho filtra (zadanou do
		 * konštruktora) ak ako celok vyhovuje vzoru (regulárnemu výrazu)
		 * tohto filtra. (Pozri aj vzor a nahradenie v konštruktore: {@link 
		 * Filtre.Filter#Filtre.Filter(String, String) Filter}{@code (vzor,
		 * nahradenie)}.) Ak reťazec vyhovie filtru, tak metóda vráti
		 * v návratovej hodnote výsledok nahrádzania. V opačnom prídade vráti
		 * zadaný záložný reťazec.</p>
		 * 
		 * @param reťazec reťazec, ktorý bude porovnaný s regulárnym výrazom
		 *     vzoru filtra ako celok a v prípade zhody nahradený podľa šablóny
		 *     nahrádzania (zadanej do konštruktora tohto filtra)
		 * @param záložnýReťazec záložný reťazec, ktorý bude vrátený
		 *     v prípade, že tento reťazec ako celok nevyhovie vzoru filtra;
		 *     (tip: ak je argument priamo parametrom, dá sa použiť operátor
		 *     zhody ({@code ==}) na overenie toho, či bolo niečo nahradené)
		 * @return výsledok spracovania (podľa pravidiel opísaných vyššie)
		 */
		public String nahradenieCelku(String reťazec, String záložnýReťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.matches()) return zhoda.replaceFirst(nahradenie);
			return záložnýReťazec;
		}

		/**
		 * <p>Funguje rovnako ako metóda {@link #nahradenieCelku(String,
		 * String) nahradenieCelku}{@code (reťazec, záložnýReťazec)}, ale
		 * namiesto záložného reťazca dosadí pôvodný reťazec.</p>
		 * 
		 * @param reťazec reťazec, ktorý bude porovnaný s regulárnym výrazom
		 *     vzoru filtra ako celok a v prípade zhody nahradený podľa šablóny
		 *     nahrádzania (zadanej do konštruktora tohto filtra);
		 *     (tip: ak je argument priamo parametrom, dá sa použiť operátor
		 *     zhody ({@code ==}) na overenie toho, či bolo niečo nahradené)
		 * @return výsledok spracovania
		 */
		public String nahradenieCelku(String reťazec)
		{
			zhoda = vzor.matcher(reťazec);
			if (zhoda.matches()) return zhoda.replaceFirst(nahradenie);
			return reťazec;
		}
	}


	// Atribúty pomáhajúce pri parsovaní riadkov:
	private String riadokParsovania = null, vzorParsovania = null;

	// Súkromná metóda slúžiaca na parsovanie riadkov čítaných zo súboru
	// alebo extrahovaných zo zadaného bloku (konfiguračného) textu.
	private int pridajRiadokParsovania()
	{
		int početChýb = 0;
		if (null == vzorParsovania)
		{
			if (riadokParsovania.equals("*"))
			{
				if (0 != size())
				{
					Filter filter = posledný();
					filter.zastav = true;
				}
				else ++početChýb;
			}
			else if (!riadokParsovania.isEmpty())
				vzorParsovania = riadokParsovania;
		}
		else
		{
			try
			{
				pridaj(new Filter(vzorParsovania, riadokParsovania));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				// GRobotException.vypíšChybovéHlásenia(t);
				++početChýb;
			}
			vzorParsovania = null;
		}
		return početChýb;
	}


	/**
	 * <p>Predvolený konštruktor – vytvorí prázdny zoznam filtrov.</p>
	 */
	public Filtre() { super(); }

	/**
	 * <p>Vytvorí zoznam filtrov obsahujúci zadané filtre.</p>
	 * 
	 * @param filtre zoznam filtrov oddelený čiarkami
	 */
	public Filtre(Filter... filtre) { super(filtre); }

	/**
	 * <p>Vytvorí zoznam filtrov obsahujúci zadané filtre.</p>
	 * 
	 * @param filtre kolekcia obsahujúca zoznam filtrov
	 */
	public Filtre(Collection<Filter> filtre) { super(filtre); }

	/**
	 * <p>Vytvorí zoznam filtrov obsahujúci zadané filtre.</p>
	 * 
	 * @param filtre zoznam reťazcov oddelený čiarkami, prípadne pole, ktoré
	 *     budú spracované metódou {@link #parsuj(String[])
	 *     parsuj}{@code (konfiguračnéPole)}
	 */
	public Filtre(String... filtre) { parsuj(filtre); }

	/**
	 * <p>Prečíta zoznam filtrov zo zadaného konfiguračného súboru. Metóda
	 * očakáva v súbore zoradené dvojice alebo trojice riadkov s významom
	 * „regulárny výraz“ (vzor), „nahradenie“ (šablóna) a nepovinný riadok
	 * <i>príznaku zastavenia spracovania</i> (<code>*</code>).
	 * Dvojice/trojice riadkov môžu byť oddelené ľubovoľným množstvom
	 * prázdnych riadkov.</p>
	 * 
	 * <p>Trojice sú od dvojíc odlíšené jednoducho: jediným platným tretím
	 * riadkom je riadok obsahujúci iba hviezdičku (čo je neplatný regulárny
	 * výraz, takže nemôže byť začiatkom ďalšej dvojice alebo trojice).
	 * Dvojice/trojice riadkov môžu byť oddelené prázdnymi riadkami na
	 * zlepšenie čitateľnosti konfiguračného súboru. Prázdne riadky sú
	 * ignorované, okrem jediného prípadu – ak ide o druhý riadok
	 * dvojice/trojice.</p>
	 * 
	 * <p>Technicky je fungovanie metódy je zariadené tak, aby nenastávali
	 * sporné situácie. Riadky sú čítané sekvenčne a sú vyhodnocované
	 * takto:</p>
	 * 
	 * <ol>
	 * <li>Prvý neprázdny riadok v sekvencii je považovaný za regulárny výraz,
	 *     ktorý označuje filter.</li>
	 * <li>Ďalší tesne za ním nasledujúci riadok je považovaný za nahradenie.
	 *     Toto nahradenie môže byť aj prázdne (keď chceme, aby filter obsah
	 *     zadaného reťazca úplne vymazal). Ak je regulárny výraz z prvého
	 *     riadka korektný, tak po spracovaní druhého riadka pribudne do
	 *     zoznamu nový filter, inak sa navýši počítadlo chýb. (Chyby v druhom
	 *     riadku sa počas vytvárania filtra nedajú overiť. Tie sa prejavia až
	 *     počas používania filtra.)</li>
	 * <li>Ak je na ďalšom samostatnom riadku nájdená hviezdička, ktorá nesie
	 *     význam <i>príznaku zastavenia,</i> tak sa použije na posledný nad
	 *     ňou definovaný filter. (<b>Pozor!</b> Ak je hviezdička prvým
	 *     neprázdnym riadkom súboru a zoznam filtrov už obsahuje nejaké
	 *     položky, tak sa príznak použije na posledný jestvujúci filter
	 *     v zozname. Naopak, ak je zoznam filtrov prázdny, tak „osamelá“
	 *     hviezdička navýši počítadlo chýb.)</li>
	 * <li>Ďalej sa opakovane postupuje od bodu jeden až do konca súboru.
	 *     Neúplné alebo chybné definície sú ignorované (nezastavia
	 *     spracovanie súboru), pričom počet chýb bude v návratovej hodnote
	 *     tejto metódy. Počet chýb bude zahŕňať aj neúplnú definíciu.</li>
	 * </ol>
	 * 
	 * <p><i>Príznak zastavenia</i> indikuje, že proces filtrovania sa má po
	 * úspešnom spracovaní toho filtra, ktorý má tento príznak nastavený
	 * zastaviť (t. j., že sa nemá pokračovať ďalšími filtrami v zozname).
	 * Keby mali tento príznak nastavený všetky filtre v zozname, tak sa
	 * spracovanie zastaví po prvom úspešnom spracovaní reťazca niektorým
	 * z filtrov.</p>
	 * 
	 * <p><b>Príklady zoznamov filtrov:</b></p>
	 * 
	 * <p>Toto sú ukážky toho, ako môžu byť uložené v súboroch zoznamy filtrov
	 * prvých troch príkladov z {@linkplain Filtre hlavného opisu triedy}:</p>
	 * 
	 * <pre CLASS="example">
		-?\*+-?
		-

		(?i:\.png$)
		.tim

		-+\.tim
		.tim


		</pre>
	 * 
	 * <hr />
	 * 
	 * <pre CLASS="example">
		[ \r\n\t]+
		 

		.*?&lt;body


		.*?href="([^"]+)
		$1\n

		[^\n]+$


		</pre>
	 * 
	 * <hr />
	 * 
	 * <pre CLASS="example">
		^\\s*data:.*?;base64,

		\\s+
		§

		</pre>
	 * 
	 * <hr />
	 * 
	 * <p class="remark"><b>Poznámka:</b> Na indikáciu toho, že aktuálny
	 * riadok neoznačuje začiatok nového filtra, ale vzťahuje sa k poslednému
	 * filtru a mení jeho nastavenie bol úmyselne vybraný taký reťazec, aby
	 * regulárny výraz, ktorý by mal oznaćovať nový filtre bol nezmyselný –
	 * samostatná {@code *} by znamenala „ľubovoľné opakovanie ničoho.“</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda nemaže zoznam filtrov,
	 * takže počet úspešne pridaných filtrov sa dá zistiť podľa toho, ako sa
	 * zmenila veľkosti zoznamu filtrov pred a po volaní tejto metódy. (Na
	 * zistenie veľkosti zoznamu použite napríklad odvodenú metódu {@link 
	 * Zoznam#veľkosť() veľkosť}.)</p>
	 * 
	 * @param názovSúboru názov súboru, z ktorého majú byť prečítané definície
	 *     filtrov
	 * @return počet chýb zaznamenaných pri čítaní filtrov alebo −1, ak súbor
	 *     nejestvuje; počet chýb 
	 */
	public int čítaj(String názovSúboru)
	{
		if (!Súbor.jestvuje(názovSúboru)) return -1;

		Súbor súbor = new Súbor();
		int početChýb = 0;

		try
		{
			súbor.otvorNaČítanie(názovSúboru);
			vzorParsovania = null;

			while (null != (riadokParsovania = súbor.čítajRiadok()))
			{
				if (-1 != riadokParsovania.indexOf('\\'))
				{
					StringBuffer upravenýRiadok =
						new StringBuffer(riadokParsovania);

					for (int indexOf = 0; -1 != (indexOf =
						upravenýRiadok.indexOf("\\", indexOf)); ++indexOf)
					{
						upravenýRiadok.deleteCharAt(indexOf);
						if (indexOf < upravenýRiadok.length())
						switch (upravenýRiadok.charAt(indexOf))
						{
							case 'n': upravenýRiadok.
								setCharAt(indexOf, '\n'); break;
							case 'r': upravenýRiadok.
								setCharAt(indexOf, '\r'); break;
							case 't': upravenýRiadok.
								setCharAt(indexOf, '\t'); break;
							case 'b': upravenýRiadok.
								setCharAt(indexOf, '\b'); break;
							case 'f': upravenýRiadok.
								setCharAt(indexOf, '\f'); break;
						}
					}

					riadokParsovania = new String(upravenýRiadok);
				}

				početChýb += pridajRiadokParsovania();
			}

			if (null != vzorParsovania) ++početChýb;
			súbor.zavri();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			// GRobotException.vypíšChybovéHlásenia(t);
			++početChýb;
		}

		return početChýb;
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítaj(String) čítaj}.</p> */
	public int citaj(String názovSúboru) { return čítaj(názovSúboru); }


	/**
	 * <p>Pridá filtre podľa zadaného bloku konfiguračného textu. Blok textu
	 * je rozdelený na riadky, ktoré sú spracúvané rovnako ako keby boli
	 * prečítané zo súboru: pozri opis metódy {@link #čítaj(String) čítaj}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda nemaže zoznam filtrov,
	 * takže počet úspešne pridaných filtrov sa dá zistiť podľa toho, ako sa
	 * zmenila veľkosti zoznamu filtrov pred a po volaní tejto metódy. (Na
	 * zistenie veľkosti zoznamu použite napríklad odvodenú metódu {@link 
	 * Zoznam#veľkosť() veľkosť}.)</p>
	 * 
	 * @param blokTextu blok konfiguračného textu obsahujúci definície filtrov
	 * @return počet chýb zaznamenaných pri parsovaní zadaného reťazca;
	 *     −1 ak bol zadaný nulový parameter ({@code valnull}),
	 *     −2 ak bol zadaný prázdny reťazec ({@code srg""} alebo
	 *     {@code srg"\u005CuFEFF"} – BOM)
	 */
	public int parsuj(String blokTextu)
	{
		if (null == blokTextu) return -1;
		if (blokTextu.isEmpty()) return -2;

		int začiatok = 0;
		int koniec = blokTextu.length();

		if ('\uFEFF' == blokTextu.charAt(0))
		{
			if (1 == koniec) return -2;
			++začiatok;
		}

		int početChýb = 0;
		vzorParsovania = null;

		for (int i = začiatok; i < koniec; ++i)
		{
			char znak = blokTextu.charAt(i);
			if ('\r' == znak)
			{
				riadokParsovania = blokTextu.substring(začiatok, i);
				početChýb += pridajRiadokParsovania();
				if (i + 1 < koniec && '\n' == blokTextu.charAt(i + 1)) ++i;
				začiatok = i + 1;
			}
			else if ('\n' == znak)
			{
				riadokParsovania = blokTextu.substring(začiatok, i);
				početChýb += pridajRiadokParsovania();
				if (i + 1 < koniec && '\r' == blokTextu.charAt(i + 1)) ++i;
				začiatok = i + 1;
			}
		}

		if (začiatok < koniec)
		{
			riadokParsovania = blokTextu.substring(začiatok, koniec);
			početChýb += pridajRiadokParsovania();
		}

		if (null != vzorParsovania) ++početChýb;

		return početChýb;
	}


	/**
	 * <p>Pridá filtre podľa zadaného konfiguračného poľa. Metóda považuje
	 * pole reťazcov za riadky, ktoré sú spracúvané rovnako ako keby boli
	 * prečítané zo súboru: pozri opis metódy {@link #čítaj(String) čítaj}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda nemaže zoznam filtrov,
	 * takže počet úspešne pridaných filtrov sa dá zistiť podľa toho, ako sa
	 * zmenila veľkosti zoznamu filtrov pred a po volaní tejto metódy. (Na
	 * zistenie veľkosti zoznamu použite napríklad odvodenú metódu {@link 
	 * Zoznam#veľkosť() veľkosť}.)</p>
	 * 
	 * @param konfiguračnéPole konfiguračné pole obsahujúce definície filtrov
	 * @return počet chýb zaznamenaných pri parsovaní zadaného poľa;
	 *     −1 ak bol zadaný nulový parameter ({@code valnull}),
	 *     −2 ak bolo zadané prázdne pole
	 */
	public int parsuj(String[] konfiguračnéPole)
	{
		if (null == konfiguračnéPole) return -1;
		if (0 == konfiguračnéPole.length) return -2;

		int početChýb = 0;
		vzorParsovania = null;
		boolean prvý = true;

		for (int i = 0; i < konfiguračnéPole.length; ++i)
		{
			riadokParsovania = konfiguračnéPole[i];
			if (null == riadokParsovania) continue;

			if (prvý && riadokParsovania.length() > 0 &&
				'\uFEFF' == riadokParsovania.charAt(0))
			{
				riadokParsovania = riadokParsovania.substring(1);
				prvý = false;
			}

			početChýb += pridajRiadokParsovania();
		}

		if (null != vzorParsovania) ++početChýb;

		return početChýb;
	}


	/**
	 * <p>Táto metóda slúži na pridanie nového filtra do tohto zoznamu
	 * filtrov, ktorý bude vytvorený podľa zadaných parametrov (vzoru
	 * a zhody). V rámci tohto procesu je volaný konštruktor filtra, takže
	 * platia rovnaké informácie ako v jeho opise: {@code Filter.}{@link 
	 * Filtre.Filter#Filtre.Filter(String, String) Filter}{@code (vzor,
	 * nahradenie)}.</p>
	 * 
	 * @param vzor regulárny výraz na porovnávanie reťazcov (ktoré budú
	 *     odovzdávané tomuto zoznamu filtrov prostredníctvom metódy {@link 
	 *     #použi(String) použi}
	 * @param nahradenie šablóna reťazca na nahrádzanie zhôd podľa regulárneho
	 *     výrazu v parametri {@code vzor}
	 */
	public void pridaj(String vzor, String nahradenie)
	{
		pridaj(new Filter(vzor, nahradenie));
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu ku vzoru filtra.
	 * Rovnaký efekt sa dá dosiahnuť volaním:
	 * {@link #daj(int) daj}{@code (i).}{@link Filter#vzor vzor}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia vzoru i-teho filtra ({@link Pattern Pattern})
	 */
	public Pattern dajVzor(int i)
	{
		return daj(i).vzor;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k šablóne nahradenia
	 * filtra. Rovnaký efekt sa dá dosiahnuť volaním:
	 * {@link #daj(int) daj}{@code (i).}{@link Filter#nahradenie
	 * nahradenie}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia nahradenia i-teho filtra ({@link Pattern Pattern})
	 */
	public String dajNahradenie(int i)
	{
		return daj(i).nahradenie;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k inštancii zhody
	 * filtra ({@link Matcher Matcher}). Rovnaký efekt sa dá dosiahnuť
	 * volaním: {@link #daj(int) daj}{@code (i).}{@link Filter#zhoda zhoda}.
	 * Z toho vyplýva, že platia rovnaké pravidlá, aké sú uvedené v opise
	 * atribútu filtra {@link Filter#zhoda zhoda}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return inštancia zhody i-teho filtra ({@link Matcher Matcher})
	 */
	public Matcher dajZhodu(int i)
	{
		return daj(i).zhoda;
	}

	/**
	 * <p>Táto metóda slúži na zvýšenie pohodlia prístupu k metóde
	 * porovnávania zhody i-teho filtra so zadaným reťazcom. Rovnaký efekt sa
	 * dá dosiahnuť volaním metódy: {@link #daj(int) daj}{@code (i).}{@link 
	 * Filter#zhoda(String) zhoda}{@code (reťazec)}.</p>
	 * 
	 * @param i index prvku tohto zoznamu filtrov; pozri aj: {@link #daj(int)
	 *     daj(i)}
	 * @return nová inštancia zhody i-teho filtra ({@link Matcher Matcher})
	 *     vytvorená podľa pravidiel opísaných v metóde filtra {@link 
	 *     Filter#zhoda(String) zhoda}
	 */
	public Matcher dajZhodu(int i, String reťazec)
	{
		return daj(i).zhoda(reťazec);
	}


	/**
	 * <p>Použije na zadaný reťazec všetky filtre tejto inštancie. Ak má
	 * niektorý z filtrov v zozname nastavený príznak {@link Filter#zastav
	 * zastav}, tak sa spracovanie po úspešnom použití tohto filtra
	 * automaticky zastaví.</p>
	 * 
	 * @param reťazec reťazec na spracovanie
	 * @return spracovaný reťazec, ak bola nájdená aspoň jedna zhoda v tomto
	 *     zozname filtrov alebo pôvodný reťazec, ak jeho obsah nevyhovel ani
	 *     jednej definícii tohto zoznamu
	 */
	public String použi(String reťazec)
	{
		for (Filter filter : this)
		{
			filter.zhoda = filter.vzor.matcher(reťazec);
			if (filter.zhoda.find())
			{
				reťazec = filter.zhoda.replaceAll(filter.nahradenie);
				if (filter.zastav) break;
			}
		}
		return reťazec;
	}

	/** <p><a class="alias"></a> Alias pre {@link #použi(String) použi}.</p> */
	public String pouzi(String reťazec) { return použi(reťazec); }


	// static { System.out.println("Log " + new Throwable().getStackTrace()[0]); }
}
