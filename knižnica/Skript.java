
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

 // Táto trieda je výsledkom dlhodbého postupného a pomalého vývoja
 // vnútorného skriptovacieho stroja programovacieho rámca GRobot.
 // Do verzie s pracovným číslom 2.00 (približne v apríli roku 2019)
 // bola vnorenou triedou triedy Svet, po tomto dátume sa oddelila.
 // (Zachovala si však množstvo väzieb so zvyškom rámca.) Mechanizmus
 // skriptovania je teraz úplne odkázaný na obsah tejto triedy.

package knižnica;

import knižnica.podpora.ExpressionProcessor;

import java.awt.Color;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

// import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static knižnica.Farebnosť.*;

import static knižnica.Konštanty.ČAKAŤ;
import static knižnica.Konštanty.ČÍSELNÁ_PREMENNÁ;
import static knižnica.Konštanty.FAREBNÁ_PREMENNÁ;
import static knižnica.Konštanty.CHYBA_VOLANIA_SKRIPTU;
import static knižnica.Konštanty.CHYBA_ČÍTANIA_SKRIPTU;
import static knižnica.Konštanty.CHYBA_DVOJITÁ_MENOVKA;
import static knižnica.Konštanty.CHYBA_CHÝBAJÚCA_MENOVKA;
import static knižnica.Konštanty.CHYBA_NEZNÁMA_MENOVKA;
import static knižnica.Konštanty.CHYBA_CHYBNÁ_ŠTRUKTÚRA;
import static knižnica.Konštanty.CHYBA_NEZNÁME_MENO;
import static knižnica.Konštanty.CHYBA_NEZNÁME_SLOVO;
import static knižnica.Konštanty.CHYBA_NEZNÁMY_PRÍKAZ;
import static knižnica.Konštanty.CHYBA_VYKONANIA_PRÍKAZU;
import static knižnica.Konštanty.POLOHOVÁ_PREMENNÁ;
import static knižnica.Konštanty.PRERUŠIŤ;
import static knižnica.Konštanty.REŤAZCOVÁ_PREMENNÁ;
import static knižnica.Konštanty.VYPÍSAŤ_MENOVKY;
import static knižnica.Konštanty.VYPÍSAŤ_PREMENNÉ;
import static knižnica.Konštanty.VYPÍSAŤ_RIADOK;
import static knižnica.Konštanty.VYPÍSAŤ_SKRIPT;
import static knižnica.Konštanty.ZABRÁNIŤ_VYKONANIU;
import static knižnica.Konštanty.ŽIADNA_CHYBA;

/**
 * <p>Táto abstraktná trieda vnútorne implementuje celý mechanizmus
 * interpretácie skriptov programovacieho rámca GRobot. Jej použitie
 * je automatické.</p>
 * 
 * <p class="remark"><b>Poznámka:</b>Verejné statické metódy poskytujú
 * základné rozhranie na prácu so skriptovacím strojom, napríklad:
 * {@link #vyrob(String[]) vyrob}, {@link #ladenie(boolean) ladenie},
 * {@link #čítajPremennú(String, Class) čítajPremennú},
 * {@link #zapíšPremennú(String, Object) zapíšPremennú},
 * {@link #kódPoslednejChyby() kódPoslednejChyby}, {@link #vyrob(String[])
 * vyrob} a podobne. Dve inštančné metódy komunikujú s konkrétnymi
 * inštanciami skriptov: {@link #vykonaj() vykonaj} a {@link #vypíš()
 * vypíš}. Tie sú užitočné v prípade, že pracujeme s vyrobenou inštanciou
 * tejto triedy.</p>
 * 
 * <p><b>Na prácu so skriptami sú určené tieto metódy triedy {@link Svet
 * Svet} (a ich klony):</b></p>
 * 
 * <ul>
 * <li>{@link Svet#vykonajSkript(String[] riadky) vykonajSkript(riadky)},</li>
 * <li>{@link Svet#nahrajSkript(String názov, String súbor)
 * nahrajSkript(názov, súbor)},</li>
 * <li>{@link Svet#vyrobSkript(String skript, boolean zoSúboru)
 * vyrobSkript(skript, zoSúboru)},</li>
 * <li>{@link Svet#registrujSkript(String názov, String[] skript)
 * registrujSkript(názov, skript)},</li>
 * <li>{@link Svet#dajSkript(String názov) dajSkript(názov)}</li>
 * <li>a {@link Svet#volajSkript(String názov) volajSkript(názov)}.</li>
 * </ul>
 * 
 * <p>Užitočná je tiež táto metóda (a jej klony):</p>
 * 
 * <ul>
 * <li>{@link Svet#formulujChybuSkriptu(int kódSkriptu, String titulokChyby,
 * int šírkaRiadka) formulujChybuSkriptu(kódSkriptu, titulokChyby,
 * šírkaRiadka)}</li>
 * </ul>
 * 
 * <p><b>Na ladenie skriptov sú určené nasledujúce metódy (</b>vrátane
 * ich rôznych variánt a príbuzných metód<b>):</b></p>
 * 
 * <ul>
 * <li>{@link Svet#režimLadenia(boolean) režimLadenia(zapniLadenie)},</li>
 * <li>{@link Svet#premennáJestvuje(String, Class) premennáJestvuje(názov,
 * typ)},</li>
 * <li>{@link Svet#čítajPremennú(String, Class) čítajPremennú(názov,
 * typ)},</li>
 * <li>{@link Svet#zapíšPremennú(String, Object) zapíšPremennú(názov,
 * hodnota)},</li>
 * <li>{@link Svet#vymažPremennú(String, Class) vymažPremennú(názov,
 * typ)};</li>
 * <li>a špeciálne vyhradená reakcia {@linkplain ObsluhaUdalostí obsluhy
 * udalostí}:</li>
 * <li>{@link ObsluhaUdalostí#ladenie(int, String, int) ladenie(riadok,
 * príkaz, správa)} (jej opis obsahuje základný príklad ladenia;
 * komplexnejší príklad ladenia je nižšie v sekcii Príklad ladenia
 * skriptov).</li>
 * </ul>
 * 
 * <p>Pričom opis chybových stavov je v opise triedy {@link GRobotException
 * GRobotException} a tiež pri viacerých konštantách definovaných v triede
 * {@link Konštanty Konštanty} (v ktorej sú zároveň zhromaždené konštanty
 * reprezentujúce kódy správ, oznamov a iných informácií používaných
 * skriptovacím strojom, napríklad {@link Konštanty#ČAKAŤ ČAKAŤ},
 * {@link Konštanty#PRERUŠIŤ PRERUŠIŤ},
 * {@link Konštanty#VYPÍSAŤ_SKRIPT VYPÍSAŤ_SKRIPT},
 * {@link Konštanty#VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK},
 * {@link Konštanty#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ},
 * {@link Konštanty#VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ},
 * {@link Konštanty#VYPÍSAŤ_PREMENNÉ VYPÍSAŤ_PREMENNÉ} a podobne).</p>
 * 
 * <p class="remark"><b>Poznámka:</b> Krátky úryvok kódu ukazujúci
 * najjednoduchší spôsob vykonania skriptu uloženého v súbore je v opise
 * metódy {@link Svet#vykonajSkript(String, boolean) vykonajSkript(skript,
 * zoSúboru)}. Komplexnejší príklad spúšťania je nižšie v rámci príkladu
 * ladenia skriptov…</p>
 * 
 * <p> </p>
 * 
 * <p><b>Vybrané pravidlá vykonávania</b></p>
 * 
 * <p class="tip"><b>Informácia:</b> Ďalšie podrobnosti súvisiace
 * s témou vykonávania príkazov interaktívneho režimu a skriptov
 * sú v opise metódy {@link Svet#interaktívnyRežim(boolean)
 * Svet.interaktívnyRežim}.</p>
 * 
 * <p>Vykonávanie skriptu úzko súvisí s interaktívnou inštanciou a tým
 * aj s {@linkplain Svet#interaktívnyRežim(boolean) interaktívnym režimom.}
 * Na korektné vykonanie príkazu, ktorý nie je riadiacim príkazom (riadiacou
 * štruktúrou) skriptu, musí byť aktívna tzv. interaktívna inštancia robota,
 * plátna alebo sveta, bez ktorej by nebolo možné príkaz správne vyhodnotiť,
 * pretože by nebolo jasné, metódu ktorej inštancie je treba vykonať:</p>
 * 
 * <ul>
 * <li>{@link GRobot#interaktívnyRežim(boolean)
 * GRobot.interaktívnyRežim(zapni)} (v skriptoch je možné roboty odlíšiť
 * ich {@linkplain GRobot#meno(String) menami});</li>
 * <li>{@link Plátno#interaktívnyRežim(boolean)
 * Plátno.interaktívnyRežim(zapni)} (v skriptoch sa plátno rozlišuje buď
 * ako podlaha, alebo ako strop);</li>
 * <li>{@link Svet#interaktívnyRežim(boolean) Svet.interaktívnyRežim(zapni)}
 * (opis metódy sveta obsahuje ďalšie podrobnejšie informácie).</li>
 * </ul>
 * 
 * <p>Ak v čase vykonania tzv. platného príkazu skriptu (pozri nižšie) nie
 * je určená aktívna interaktívna inštancia (dá sa určiť aj priamo v skripte
 * špeciálnym „príkazom“ {@code @} – pozri nižšie), tak sa vykonávanie
 * skriptu skončí chybou.</p>
 * 
 * <p>Každý neprázdny riadok skriptu smie obsahovať niektorú
 * z nasledujúcich položiek:</p>
 * 
 * <ul>
 * <li>platný príkaz – <small>taký, ktorý je zároveň použiteľný s metódami
 * na vykonávanie príkazov robota ({@link GRobot#vykonajPríkaz(String)
 * GRobot.vykonajPríkaz}), plátna ({@link Plátno#vykonajPríkaz(String)
 * Plátno.vykonajPríkaz}) alebo sveta ({@link Svet#vykonajPríkaz(String)
 * Svet.vykonajPríkaz}), respektíve príkazy platné pre aktívnu interaktívnu
 * inštanciu (pozri opis <b>{@linkplain Svet#interaktívnyRežim(boolean)
 * interaktívneho režimu tu}</b>; obsahuje ďalšie dôležité informácie,
 * najmä o príkaze <code>nech</code>)</small>,</li>
 * <li>komentár (pozri nižšie),</li>
 * <li>definíciu menovky (pozri nižšie),</li>
 * <li>aktivovanie alebo deaktivovanie {@linkplain 
 * Svet#interaktívnaInštancia(String) interaktívnej inštancie} (pozri
 * nižšie),</li>
 * <li>alebo jeden z rezervovaných príkazov skriptu (podrobnosti
 * sú opäť nižšie):
 *   <ul>
 *   <li>nepodmienený skok (<code>na</code>),</li>
 *   <li>podmienený skok (<code>ak</code>),</li>
 *   <li>podmienený skok s alternatívou (<code>ak-inak</code>),</li>
 *   <li>podmienený skok s dekrementáciou premennej („cyklus“ –
 *   <code>dokedy</code>),</li>
 *   <li>podmienený skok s dekrementáciou premennej a alternatívou
 *   („cyklus s alternatívou“ – <code>dokedy-inak</code>),</li>
 *   <li>cyklus s inkrementáciou premennej v rozsahu od 1 po zadanú
 *   hodnotu (<code>opakuj</code>),</li>
 *   <li>určenie obzoru premenných (<code>obzor</code>).</li>
 *   </ul>
 * </li>
 * </ul>
 * 
 * <p>Skripty môžu byť rozdelené na <b>bloky,</b> ktoré môžu s výhodou
 * využívať rezervované príkazy (kvázi riadiace štruktúry) skriptov,
 * pretože rovnako ako pri iných programovacích jazykoch aj tu platí, že
 * ak niektorý príkaz (riadiaca štruktúra) vyžaduje na svoje fungovanie
 * ďalší príkaz (napríklad verzie pomieneného spracovania a opakovania bez
 * využitia menoviek), tak tento príkaz môže byť nahradený blokom. <b>Bloky
 * v skriptoch vymedzuje rovnaká veľkosť (úroveň) odsadenia riadkov
 * tabulátormi (čiže úroveň opakujúca sa vo vymedzenej sérii riadkov).</b></p>
 * 
 * <p> </p>
 * 
 * <style><!--
 * 	.vl1
 * 	{
 * 		border-right: 1px solid silver;
 * 		padding-left: 10px;
 * 	}
 * 
 * 	.vl2
 * 	{
 * 		border-left: 1px solid gray;
 * 		padding-right: 10px;
 * 	}
 * --></style>
 * 
 * <table class="commands">
 * <tr><td><code>; </code><em>«text»</em></td><td>–</td><td
 * >komentár – tento riadok bude ignorovaný</td></tr>
 * <tr><td><code>:</code><em>«názov»</em></td><td>–</td><td>definícia
 * menovky, ktorá je používaná na skoky (podmienené a nepodmienené –
 * pozri nižšie); názov menovky „koniec“ je rezervovaný (a označuje
 * koniec skriptu)</td></tr>
 * <tr><td><code>@</code><em
 * >«názov inštancie»</em></td><td
 * >–</td><td>aktivovanie {@linkplain Svet#interaktívnaInštancia(String)
 * interaktívnej inštancie}</td></tr>
 * <tr><td><code>@</code></td><td>–</td><td>zrušenie aktivácie
 * {@linkplain Svet#interaktívnaInštancia(String) interaktívnej
 * inštancie}</td></tr>
 * <tr><td><code>na </code> <em>«menovka»</em></td><td>–</td>
 * <td>nepodmienený skok – vykonávanie skriptu prejde (preskočí) na
 * riadok označený menovkou (pozri vyššie); názov menovky „koniec“ označuje
 * koniec skriptu</td></tr>
 * <tr><td><code>ak </code><em
 * >«premenná alebo hodnota»</em> <em
 * >«menovka»</em></td><td>–</td>
 * <td>podmienený skok – ak je <em>«premenná alebo hodnota»</em>
 * nenulová, tak vykonávanie skriptu prejde (preskočí) na riadok
 * označený menovkou</td></tr>
 * <tr><td><code>ak </code><em
 * >«premenná alebo hodnota»</em> <em
 * >«menovka1»</em><code> inak </code><em
 * >«menovka2»</em></td><td>–</td><td>podmienený
 * skok s alternatívou – ak je <em>«premenná alebo hodnota»</em>
 * nenulová, tak vykonávanie skriptu prejde (preskočí) na riadok
 * označený menovkou <em>«menovka1»</em>, inak na riadok označený
 * menovkou <em>«menovka2»</em></td></tr>
 * <tr><td><code>dokedy </code><em>«premenná»</em> <em
 * >«menovka»</em></td><td>–</td><td>podmienený
 * skok s dekrementáciou premennej („cyklus“) – najprv sa zníži
 * hodnota premennej o 1 a ak je výsledok výpočtu záporný, tak sa
 * jej hodnota nastaví na nulu; ak je konečná hodnota premennej
 * kladná, tak vykonávanie skriptu prejde (preskočí) na riadok
 * označený menovkou</td></tr>
 * <tr><td><code>dokedy </code><em>«premenná»</em> <em
 * >«menovka1»</em><code> inak </code><em>«menovka2»</em></td><td
 * >–</td><td>podmienený skok s dekrementáciou premennej („cyklus“)
 * s alternatívou – najprv sa zníži hodnota premennej o 1 a ak je
 * výsledok výpočtu záporný, tak sa jej hodnota nastaví na nulu;
 * ak je konečná hodnota premennej kladná, tak vykonávanie skriptu
 * prejde (preskočí) na riadok označený menovkou <em>«menovka1»</em>,
 * inak na riadok označený menovkou <em>«menovka2»</em></td></tr>
 * <tr><td colspan="2"> </td>
 * <td>podmienené skoky s dekrementáciou premennej sú navrhnuté tak,
 * aby pomyselne predpokladali prítomnosť kladnej hodnoty v riadiacej
 * premennej a aby sa opakovanie ukončilo v okamihu dosiahnutia nuly
 * v riadiacej premennej (pričom nie je dovolené, aby sa v riadiacej
 * premennej vyskytla záporná hodnota – záporné hodnoty sú prepísané
 * nulou); to znamená, že všetky priebehy vykonania s hodnotou
 * riadiacej premennej menšej alebo rovnej jednej sú identické<!--
 * skrytá poznámka: dôvodom tohto návrhu bolo kladenie dôrazu na čo
 * najvyššiu jednoduchosť pri implementácii; bolo to inšpirované
 * jazykom symbolických inštrukcií; ďalší dôkaz toho, že (žiaľ) čím
 * je niečo jednoduchšie na implementáciu, tým ťažšie sa to potom
 * používa; je to vlastne prirodzené – čím je niečo bližšie „jazyku
 * strojov“ (v skutočnosti „formálnemu zápisu,“ ktorý jestvuje aj bez
 * kontextu strojov), tým je to vzdialenejšie prirodzenému („ľudskému“)
 * jazyku (v skutočnosti to môže byť niečo ako „jazyk vedomia a myslenia,“
 * ale tento filozofický kontext v tejto fáze nemám doštudovaný/domyslený)…
 * --></td></tr>
 * 
 * 
 * <tr><td colspan="3"><p> <br />Príkazy <code>ak</code> a <code>dokedy</code>
 * sa dajú použiť aj v režime bez menovky, kedy menia svoje správanie – pozri
 * nižšie.</p><p><b>Vysvetlivky:</b></p><p><em>«podmienka»</em> je slovná
 * skratka, ktorá má v tomto prípade viacero významov – môže to byť premenná,
 * hodnota alebo výraz za mriežkou (pozri poznámku nižšie);</p><p><em
 * >«hranica»</em> môže byť literárna hodnota alebo názov premennej,
 * z ktorej sa hodnota prevezme;</p><p><em
 * >«príkaz/blok», «príkaz/blok 2»</em> môže byť jediný príkaz na samostatnom
 * riadku alebo <b>blok</b> (pozri vyššie)</p><p class="remark"
 * style="margin-bottom: 6px"><b>Poznámka:</b> Syntax s mriežkou je bližšie
 * spomenutá napríklad v opise metódy {@link Svet#interaktívnyRežim(boolean)
 * Svet.interaktívnyRežim}.</p></td></tr>
 * 
 * 
 * <tr><td><!--#--><table><tr><td><code>ak </code><em>«podmienka»</em><br
 * />    <em>«príkaz/blok»</em> </td><td class="vl1"></td><td
 * class="vl2"></td><td><code>ak </code><em>«podmienka»</em><br />    <em
 * >«príkaz/blok»</em> <br /><code>inak </code><br />    <em
 * >«príkaz/blok 2»</em> </td></tr></table><!--#--></td><td>–</td><td
 * >podmienené spracovanie (s alternatívou – <code>inak</code>);<br />ak je
 * výsledok/hodnota <em>«podmienky»</em> nenulová, tak sa vykoná <em
 * >«príkaz/blok»;</em><br />v opačnom prípade, ak je prítomná alternatíva,
 * sa vykoná <em>«príkaz/blok 2»</em>;<br />(pozri príklad 1 nižšie)</td></tr>
 * 
 * <tr><td colspan="3"><hr /></td></tr>
 * 
 * <tr><td><code>opakuj </code>[<em>«názov premennej»</em>]<code> </code><em
 * >«hranica»</em><br />    <em>«príkaz/blok»</em> </td><td rowspan="3"
 * >–</td><td rowspan="3">opakovanie/cyklus (s alternatívou – <code
 * >inak</code>);<br /><em>«názov premennej»</em> je nepovinný; určuje
 * premennú, do ktorej sa bude ukladať „číslo iterácie“ (čo je stúpajúca
 * hodnota meniaca sa podľa hodnoty <em>«hranice»</em>); v prípade
 * jej neuvedenia je cyklus anonymný;<br />absolútna hodnota <em
 * >«hranice»</em> určuje počet opakovaní; ak je nenulová, tak sa cyklicky
 * vykonáva <em>«príkaz/blok»</em>;<br />ak je kladná, tak sa hodnota
 * premennej (ak nie je cyklus anonymný) bude meniť od hodnoty 1 po hodnotu
 * <em>«hranice»</em>; ak je záporná, tak sa bude meniť od jej hodnoty po
 * hodnotu −1 (vďaka tomu sa dá pomocou absolútnej hodnoty simulovať
 * klesajúci cyklus, ale má to háčik pri niektorých algoritmoch, pretože
 * nenulová hodnota hranice má pri tomto cykle vždy za následok nenulový
 * počet opakovaní; prípadné zamedzenie vykonania cyklu pri zápornej hranici
 * musí byť ošetrené dodatočnou podmienkou);<br />ak je (už pri prvom pokuse
 * o vykonanie cyklu, to jest pred prvou iteráciou) <em>«hranica»</em>
 * nulová, tak sa v prípade prítomnosti alternatívy (<code>inak</code>)
 * vykoná <em>«príkaz/blok 2»</em> (v ostatných prípadoch je alternatíva
 * ignorovaná, to jest, ak sa vykoná aspoň jedna iterácia cyklu,
 * alternatíva nebude spustená);<br />ak je pri anonymnom cykle hranica
 * určená hodnotou premennej, tak je jej hodnota prevzatá a premenná je
 * použitá tak, ako keby cyklus nebol anonymný (alebo to môžeme chápať tak,
 * že cyklus prestáva byť anonymným) – pozri príklad 2 nižšie</td></tr>
 * 
 * <tr><td><hr /></td></tr>
 * 
 * <tr><td><code>opakuj </code>[<em>«názov premennej»</em>]<code> </code><em
 * >«hranica»</em><br />    <em>«príkaz/blok»</em> <br /><code>inak </code><br
 * />    <em>«príkaz/blok 2»</em> </td></tr>
 * 
 * <tr><td colspan="3"><hr /></td></tr>
 * 
 * <tr><td>    <em>«príkaz/blok»</em> <br /><code>dokedy </code><em
 * >«názov premennej»</em><code> </code></td><td rowspan="3">–</td><td
 * rowspan="3">zopakovanie predchádzajúceho <em>«príkazu/bloku»</em>
 * (s alternatívou – <code>inak</code>);<br /><em>«názov premennej»</em>
 * je povinný a jej hodnota určuje počet opätovných zopakovaní
 * predchádzajúceho <em>«príkazu/bloku»</em> (ten sa vždy vykoná najmenej
 * raz – pred prvým vyhodnotením hodnoty <em>«premennej»</em>);<br />ak
 * je hodnota <em>«premennej»</em> väčšia od jednej, tak sa <em
 * >«príkaz/blok»</em> zopakuje a jej hodnota sa zníži o 1;<br />ak je
 * hodnota <em>«premennej»</em> rovná jednej, tak sa vynuluje a prejde
 * sa na vykonávanie príkazov za cyklom (pretože <em>«príkaz/blok»</em>
 * sa už raz vykonal) – v tomto prípade sa nevykonáva <em
 * >«príkaz/blok 2»</em> alternatívy (<code>inak</code>);<br />ak je hodnota
 * <em>«premennej»</em> nulová alebo záporná, tak sa jej hodnota vynuluje
 * (čo sa viditeľne prejaví len pri záporných hodnotách) a v prípade
 * prítomnosti alternatívy (<code>inak</code>) sa vykoná <em
 * >«príkaz/blok 2»</em>;<br />(pozri príklad 3 nižšie)</td></tr>
 * 
 * <tr><td><hr /></td></tr>
 * 
 * <tr><td>    <em>«príkaz/blok»</em> <br /><code>dokedy </code><em
 * >«názov premennej»</em><code> </code><br /><code>inak </code><br
 * />    <em>«príkaz/blok 2»</em> </td></tr>
 * 
 * <tr><td colspan="3"><hr /></td></tr>
 * 
 * 
 * <tr><td><code>obzor </code><em>«názov obzoru»</em></td><td
 * >–</td><td>nastaví obzor pre nasledujúci <b>blok</b> (pozri vyššie;
 * uvedenie bloku za týmto príkazom je povinné);<br />obzory predstavujú
 * „menné priestory“ premenných (dajú sa nimi celkom dobre simulovať
 * objekty); „menný priestor,“ t. j. obzor, je od názvu premennej oddelený
 * bodkou; obzory sa nedajú prostredníctvom príkazu <code>obzor</code>
 * „skladať“ – aktívny obzor je platný ako celok a akákoľvek „časť“ mena
 * obzoru uvedená pred názvom premennej ruší aktívny obzor a nastavuje pre
 * túto premennú nový obzor (opäť ako celok)<!-- TODO? Treba ešte spresniť?
 * Neviem. Možno… -->;<br />(pozri príklad 4 nižšie)</td></tr>
 * 
 * </table>
 * 
 * <style><!--
 * 	table.GRScript
 * 	{
 * 		margin-left: 2em;
 * 		border-collapse: collapse;
 * 	}
 * 
 * 	table.GRScript td.GRtab { width: 2em; }
 * --></style>
 * 
 * <p><b>Príklad 1:</b></p>
 * 
 * <p>Prvý skript ukazuje využitie premenných, blokov a príkazu
 * podmieneného spracovania s alternatívou.</p>
 * 
 * <table class="GRScript">
 * <tr><td colspan="2">@svet</td></tr>
 * <tr><td colspan="2">nech a = uprav číslo "Číslo A:", 5</td></tr>
 * <tr><td colspan="2">nech b = uprav číslo "Číslo B:", 3</td></tr>
 * <tr><td colspan="2">nech c = otázka "Sčítať = áno; násobiť = nie."</td></tr>
 * <tr><td colspan="2">vypíš a</td></tr>
 * <tr><td colspan="2">ak c</td></tr>
 * <tr><td class="GRtab"> </td><td>; Pretože nie je 1.</td></tr>
 * <tr><td class="GRtab"> </td><td>vypíš " × ", b</td></tr>
 * <tr><td class="GRtab"> </td><td>nech c = b</td></tr>
 * <tr><td class="GRtab"> </td><td>nech c * a</td></tr>
 * <tr><td colspan="2">inak</td></tr>
 * <tr><td class="GRtab"> </td><td>; Pretože áno je 0.</td></tr>
 * <tr><td class="GRtab"> </td><td>vypíš " + ", b</td></tr>
 * <tr><td class="GRtab"> </td><td>nech c = a</td></tr>
 * <tr><td class="GRtab"> </td><td>nech c + b</td></tr>
 * <tr><td colspan="2">vypíš riadok " = ", c</td></tr>
 * </table>
 * 
 * <p><b>Výsledky:</b></p>
 * 
 * <p>Toto sú ukážky výsledných výpisov na obrazovke po potvrdení
 * rôznych vstupných hodnôt.</p>
 * 
 * <table>
 * <tr><td><b>Výsledok po potvrdení predvolených hodnôt a voľbe „Áno“:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení predvolených hodnôt a voľbe „Nie“:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnôt −5, 3 a voľbe „Áno“:</b></td></tr>
 * 
 * <tr><td>5 + 3 = 8</td>
 * <td>5 × 3 = 15</td>
 * <td>−5 + 3 = −2</td></tr>
 * </table>
 * 
 * <p><b>Príklad 2:</b></p>
 * 
 * <p>Ďalší skript je jednoduchou ukážkou príkazu (resp. riadiacej štruktúry)
 * opakovania s vopred zadaným počtom opakovaní (iterácií), pričom počiatočná
 * hodnota premennej zároveň určuje počet opakovaní (hranicu).</p>
 * 
 * <table class="GRScript">
 * <tr><td colspan="2">@svet</td></tr>
 * <tr><td colspan="2">nech i = uprav číslo "Počet opakovaní:", 1</td></tr>
 * <tr><td colspan="2">opakuj i</td></tr>
 * <tr><td class="GRtab"> </td><td>vypíš " ", i</td></tr>
 * <tr><td colspan="2">inak</td></tr>
 * <tr><td colspan="2">vypíš "Žiadne opakovanie."</td></tr>
 * <tr><td colspan="2">vypíš riadok</td></tr>
 * </table>
 * 
 * <p><b>Výsledky:</b></p>
 * 
 * <p>Nasledujúce tri výpisy ukazujú, aké budú výsledky (výpisy na
 * obrazovke) po potvrdení troch rôznych hodnôt.</p>
 * 
 * <table>
 * <tr><td><b>Výsledok po potvrdení hodnoty −5:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnoty 0:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnoty 5:</b></td></tr>
 * <tr><td> −5 −4 −3 −2 −1</td>
 * <td>Žiadne opakovanie.</td>
 * <td> 1 2 3 4 5</td></tr>
 * </table>
 * 
 * <p><b>Príklad 3:</b></p>
 * 
 * <p>Tento skript ukazuje správanie skriptu, ktorý používa zopakovanie
 * predchádzajúceho príkazu/bloku s alternatívou. Skript, aby ho nebolo
 * potrebné zakaždým manuálne upravovať, si na začiatku vyžiada potvrdenie
 * hodnoty počtu zopakovaní.</p>
 * 
 * <table class="GRScript">
 * <tr><td colspan="2">@svet</td></tr>
 * <tr><td colspan="2">nech i = uprav číslo "Počet zopakovaní:", 3</td></tr>
 * <tr><td class="GRtab"> </td><td>vypíš "Vykonanie s hodnotou i = ",
 * i</td></tr>
 * <tr><td class="GRtab"> </td><td>vypíš riadok "."</td></tr>
 * <tr><td colspan="2">dokedy i</td></tr>
 * <tr><td colspan="2">inak</td></tr>
 * <tr><td colspan="2">vypíš riadok "(Nemalo sa zopakovať.)"</td></tr>
 * <tr><td colspan="2">vypíš "Po opakovaniach (i = ", i</td></tr>
 * <tr><td colspan="2">vypíš riadok ")."</td></tr>
 * </table>
 * 
 * <p><b>Výsledky:</b></p>
 * 
 * <p>Nasledujúce štyri výpisy ukazujú, aké budú výsledky (výpisy na
 * obrazovke) po potvrdení štyroch rôznych hodnôt.</p>
 * 
 * <table>
 * <tr><td><b>Výsledok po vykonaní s predvolenou hodnotou 3:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnoty 1:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnoty 0:</b></td>
 * <td rowspan="2" class="vl1"></td><td rowspan="2" class="vl2">
 * <td><b>Výsledok po potvrdení hodnoty −2:</b></td></tr>
 * <tr><td>Vykonanie s hodnotou i = 3.<br
 * />Vykonanie s hodnotou i = 2.<br
 * />Vykonanie s hodnotou i = 1.<br
 * />Po opakovaniach (i = 0).</td>
 * <td>Vykonanie s hodnotou i = 1.<br
 * />Po opakovaniach (i = 0).</td>
 * <td>Vykonanie s hodnotou i = 0.<br
 * />(Nemalo sa zopakovať.)<br
 * />Po opakovaniach (i = 0).</td>
 * <td>Vykonanie s hodnotou i = −2.<br
 * />(Nemalo sa zopakovať.)<br
 * />Po opakovaniach (i = 0).</td></tr>
 * </table>
 * 
 * <p><b>Príklad 4:</b></p>
 * 
 * <p>Tento skript využíva prácu s obzormi. Umiestňuje premennú x do troch
 * rôznych obzorov a ukazuje „neskladateľnosť“ obzorov pri vzájomnom vnorení
 * dvoch príkazov <code>obzor</code> (raz pre obzor a, potom pre obzor b,
 * pričom napriek vnoreniu nevzniká odkazovanie sa na obzor a.b).</p>
 * 
 * <table class="GRScript">
 * <tr><td colspan="3">@svet</td></tr>
 * <tr><td colspan="3">nech x = -2</td></tr>
 * <tr><td colspan="3">nech a.x = 3</td></tr>
 * <tr><td colspan="3">nech b.x = 12</td></tr>
 * <tr><td colspan="3">nech a.b.x = 8</td></tr>
 * <tr><td colspan="3"> </td></tr>
 * <tr><td colspan="3">vypíš "Hodnoty pred obzorom: x = ", x</td></tr>
 * <tr><td colspan="3">vypíš "; a.x = ", a.x</td></tr>
 * <tr><td colspan="3">vypíš "; b.x = ", b.x</td></tr>
 * <tr><td colspan="3">vypíš riadok "; a.b.x = ", a.b.x</td></tr>
 * <tr><td colspan="3"> </td></tr>
 * <tr><td colspan="3">obzor a</td></tr>
 * <tr><td class="GRtab"> </td><td colspan="2">vypíš
 * "Hodnoty v obzore a: x = ", x</td></tr>
 * <tr><td class="GRtab"> </td><td colspan="2">vypíš "; a.x = ", a.x</td></tr>
 * <tr><td class="GRtab"> </td><td colspan="2">vypíš "; b.x = ", b.x</td></tr>
 * <tr><td class="GRtab"> </td><td colspan="2">vypíš riadok "; a.b.x = ",
 * a.b.x</td></tr>
 * <tr><td class="GRtab"> </td><td colspan="2">obzor b</td></tr>
 * <tr><td class="GRtab"> </td><td class="GRtab"> </td><td>vypíš "Hodnoty
 * v obzore b: x = ", x</td></tr>
 * <tr><td class="GRtab"> </td><td class="GRtab"> </td><td>vypíš "; a.x = ",
 * a.x</td></tr>
 * <tr><td class="GRtab"> </td><td class="GRtab"> </td><td>vypíš "; b.x = ",
 * b.x</td></tr>
 * <tr><td class="GRtab"> </td><td class="GRtab"> </td><td>vypíš riadok
 * "; a.b.x = ", a.b.x</td></tr>
 * </table>
 * 
 * <p><b>Výsledný výpis na obrazovke bude vyzerať takto:</b></p>
 * 
 * <p>Hodnoty pred obzorom: x = −2; a.x = 3; b.x = 12; a.b.x = 8<br />
 * Hodnoty v obzore a: x = 3; a.x = 3; b.x = 12; a.b.x = 8<br />
 * Hodnoty v obzore b: x = 12; a.x = 3; b.x = 12; a.b.x = 8</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad ladenia skriptov</b></p>
 * 
 * <p>Tento príklad predstavuje jednoduchý nástroj na ladenie skriptov.
 * Dá sa ovládať klávesnicou a myšou. Klávesové skratky na posúvanie
 * (krokovanie), zastavenie a štart nového skriptu (zoznam názvov je
 * prebraný z príkazového riadka operačného systému) sú medzerník,
 * {@code Escape} a {@code Enter}, ale keďže popri {@linkplain 
 * Svet#interaktívnyRežim(boolean) interaktívnom režime} je aktivovaný
 * {@linkplain Svet#neskrývajVstupnýRiadok() vstupný riadok,} ktorý v tomto
 * prípade slúži ako {@linkplain Svet#interaktívnaInštancia(String) príkazový
 * riadok interaktívneho režimu,} je treba stlačiť kláves tabulátor,
 * prípadne kliknúť na plochu, aby riadok stratil smerovanie vstupu (fokus).
 * (Prípadne môžete v príkazovom riadku zadať a potvrdiť príkaz: skrývaj
 * vstupný riadok, ale príklad neimplementuje žiadnu možnosť opätovného
 * zobrazenia príkazového riadka, takže ak ho neobnoví niektorý zo skriptov,
 * tak ho až do nasledujúceho spustenia aplikácie neuvidíte.)</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} TestLadeniaSkriptu {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Nasledujúce premenné sú „semafory“ používané počas procesu ladenia:}
		{@code comm// }
		{@code comm//   – krok:     hodnota tejto premennej riadi krokovanie; hodnota true}
		{@code comm//               znamená posunutie programu o krok ďalej (na ďalší príkaz)}
		{@code comm//   – prerušiť: nastavením hodnoty tejto premennej na true je možné}
		{@code comm//               ladenie programu predčasne ukončiť}
		{@code kwdprivate} {@code typeboolean} krok = {@code valfalse}, prerušiť = {@code valfalse};

		{@code comm// V tomto súkromnom poli sú uchovávané mená skriptov na cyklické}
		{@code comm// spúšťanie:}
		{@code kwdprivate} {@code kwdfinal} {@link String String}[] zoznamSkriptov;

		{@code comm// Táto premenná slúži na počítanie spúšťania skriptov (a zároveň}
		{@code comm// pomáha pri ich cyklickom spúšťaní):}
		{@code kwdprivate} {@code typeint} počítadloSkriptov = {@code num0};

		{@code comm// Konštruktor.}
		{@code kwdprivate} TestLadeniaSkriptu({@link String String}[] zoznamSkriptov)
		{
			{@code comm// Uchovanie zoznamu s menami skriptov.}
			{@code valthis}.zoznamSkriptov = zoznamSkriptov;

			{@code comm// Presmerovanie výpisov skriptov na podlahu.}
			{@link Skript Skript}.{@link Skript#presmerujNaPodlahu() presmerujNaPodlahu}();

			{@code comm// Definovanie obsluhy udalostí…}
			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}(
					{@code typeint} riadok, {@link String String} príkaz, {@code typeint} správa)
				{
					{@code comm// Nasledujúce vetvenie zabezpečuje spracovanie rôznych}
					{@code comm// situácií počas ladenia:}
					{@code kwdswitch} (správa)
					{
						{@code kwdcase} {@link Konštanty#ČAKAŤ ČAKAŤ}:
							{@code comm// Čakanie počas krokovania:}
							{@code kwdif} (krok)
							{
								krok = {@code valfalse};
								{@link Svet Svet}.{@link Svet#prekresli() prekresli}();
								{@code kwdreturn} {@code valfalse};
							}
							{@code kwdreturn} {@code valtrue};

						{@code kwdcase} {@link Konštanty#PRERUŠIŤ PRERUŠIŤ}:
							{@code comm// Predčasné ukončenie vykonávania skriptu:}
							{@code kwdif} (prerušiť)
							{
								{@link Svet Svet}.{@link Svet#farbaTextu(Farebnosť) farbaTextu}({@link Farebnosť#tmavooranžová tmavooranžová});
								{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vykonávanie bolo prerušené."});
								{@link Svet Svet}.{@link Svet#predvolenáFarbaTextu() predvolenáFarbaTextu}();
								{@code comm// Ďalšie podrobnosti by sme mohli vypísať}
								{@code comm// napríklad pomocou nasledujúceho úryvku kódu:}
								{@code comm//    "na riadku", riadok, ":", GRobot.riadok,}
								{@code comm//    príkaz}
							}
							{@code kwdreturn} prerušiť;

						{@code comm// Výpis definovaných menoviek skriptu odfiltrujeme:}
						{@code kwdcase} {@link Konštanty#VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}: {@code kwdreturn} {@code valfalse};

						{@code kwdcase} {@link Konštanty#UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}:
							{@code kwdif} (!prerušiť)
							{
								{@code comm// Informáciu o ukončení vypíšeme len v prípade,}
								{@code comm// že program nebol prerušený:}
								{@link Svet Svet}.{@link Svet#farbaTextu(Farebnosť) farbaTextu}({@link Farebnosť#tmavotyrkysová tmavotyrkysová});
								{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Vykonávanie bolo dokončené."});
								{@link Svet Svet}.{@link Svet#predvolenáFarbaTextu() predvolenáFarbaTextu}();
							}
							{@code kwdreturn} {@code valfalse};

						{@code kwdcase} {@link Konštanty#UKONČENIE_CHYBOU UKONČENIE_CHYBOU}:
							{@code comm// Vypíšeme len text chyby…}
							{@link Svet Svet}.{@link Svet#farbaTextu(Farebnosť) farbaTextu}({@link Farebnosť#červená červená});
							{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(príkaz);
							{@link Svet Svet}.{@link Svet#predvolenáFarbaTextu() predvolenáFarbaTextu}();
								{@code comm// Ďalšie podrobnosti by sme mohli vypísať}
								{@code comm// napríklad pomocou nasledujúcich úryvkov kódu:}
								{@code comm//    "Číslo chyby", Svet.kódPoslednejChyby()}
								{@code comm//    "Riadok chyby ", riadok}
							{@code kwdreturn} {@code valfalse};

						{@code comm// Nefiltrujeme žiadne príkazy,}
						{@code comm// vykonávame všetko bez rozdielu:}
						{@code kwdcase} {@link Konštanty#ZABRÁNIŤ_VYKONANIU ZABRÁNIŤ_VYKONANIU}: {@code kwdreturn} {@code valfalse};
					}

					{@code comm// Na všetky ostatné otázky režimu ladenia (VYPÍSAŤ_PREMENNÉ,}
					{@code comm// VYPÍSAŤ_RIADOK, VYPÍSAŤ_PRÍKAZ, VYKONAŤ_PRÍKAZ,}
					{@code comm// VYPÍSAŤ_SKRIPT) odpovedáme kladne:}
					{@code kwdreturn} {@code valtrue};
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}()
				{
					{@code comm// Ak bolo stlačené pravé tlačidlo myši, zistíme hodnotu}
					{@code comm// číselnej premennej (toto je tu uvedené na demonštračné}
					{@code comm// účely, dalo by sa to rozpracovať tak, aby si používateľ}
					{@code comm// mohol zvoliť typ premennej).}
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#PRAVÉ PRAVÉ}))
					{
						{@link String String} názov = {@link Svet Svet}.{@link Svet#zadajReťazec(String) zadajReťazec}(
							{@code srg"Názov číselnej premennej:"});
						{@code kwdif} ({@code valnull} != názov)
						{
							{@code kwdif} ({@link Svet Svet}.{@link Svet#premennáJestvuje(String, Class) premennáJestvuje}(názov, {@link Double Double}.{@code typeclass}))
							{
								{@link Svet Svet}.{@link Svet#farbaTextu(Farebnosť) farbaTextu}({@link Farebnosť#tyrkysová tyrkysová});
								{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"     "}, názov, {@code srg" = "},
									{@link Svet Svet}.{@link Svet#čítajPremennú(String, Class) čítajPremennú}(názov, {@link Double Double}.{@code typeclass}));
								{@link Svet Svet}.{@link Svet#predvolenáFarbaTextu() predvolenáFarbaTextu}();
							}
							{@code kwdelse}
							{
								{@link Svet Svet}.{@link Svet#farbaTextu(Farebnosť) farbaTextu}({@link Farebnosť#červená červená});
								{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"     Premenná "},
									názov, {@code srg"nejestvuje."});
								{@link Svet Svet}.{@link Svet#predvolenáFarbaTextu() predvolenáFarbaTextu}();
							}
						}
					}
					{@code comm// Inak (t. j. ak boli stlačené ostatné tlačidlá myši):}
					{@code comm//   Ak prebieha vykonávanie skriptu, tak prejdeme na}
					{@code comm//   ďalší krok, inak začneme vykonávanie nového skriptu.}
					{@code kwdelse} {@code kwdif} ({@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}()) krok = {@code valtrue}; {@code kwdelse} spusti();
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}()
				{
					{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#ESCAPE ESCAPE}))
					{
						{@code comm// Klávesom ESC prerušíme ladenie…}
						prerušiť = {@code valtrue};
						krok = {@code valtrue};
					}
					{@code kwdelse} {@code kwdif} ({@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}())
					{
						{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#MEDZERA MEDZERA}))
						{
							{@code comm// Klávesom medzera krokujeme…}
							krok = {@code valtrue};
						}
					}
					{@code kwdelse}
					{
						{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kláves(int) kláves}({@link Kláves Kláves}.{@link Kláves#ENTER ENTER}))
						{
							{@code comm// Klávesom ENTER opätovne spúšťame skript…}
							spusti();
						}
					}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}()
				{
					{@code kwdif} ({@link Svet Svet}.{@link Svet#neboloPrekreslené() neboloPrekreslené}()) {@link Svet Svet}.{@link Svet#prekresli() prekresli}();
				}
			};

			{@code comm// Musíme zapnúť režim interaktívny režim (zapneme len Svet a robot),}
			{@code comm// režim ladenia a zabezpečíme, aby nebol robot automaticky skrytý po}
			{@code comm// prvom výpise na vnútornú konzolu programovacieho rámca (pozri}
			{@code comm// poznámku na konci opisu metódy Plátno.vypíš).}
			{@link Svet Svet}.{@link Svet#režimLadenia(boolean) režimLadenia}({@code valtrue});
			{@link Svet Svet}.{@link Svet#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
			{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code valthis});
			{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});

			spusti();
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}

		{@code comm// Touto metódou sa spúšťa ladenie ďalšieho skriptu. Logicky, pokus}
		{@code comm// o spustenie nového ladenia bude vykonaný len v takom prípade,}
		{@code comm// keď nie je v činnosti iné ladenie. (Volanie metódy spustiSkript}
		{@code comm// by v opačnom prípade i tak nemalo žiadny efekt.)}
		{@code kwdpublic} {@code typevoid} spusti()
		{
			{@code kwdif} (!{@link Svet Svet}.{@link Svet#skriptJeSpustený() skriptJeSpustený}())
			{
				{@code comm// Nastavenie predvolených parametrov robota, rôznych vizuálnych}
				{@code comm// parametrov plátien sveta atď., aby malo každé ladenie čo}
				{@code comm// najviac podobné podmienky…}

				{@link Skript Skript}.{@link Skript#globálnePremenné() globálnePremenné}().{@link PremenneSkriptu#vymaž() vymaž}();
				{@code kwdwhile} ({@code valnull} != {@link Skript Skript}.{@link Skript#priestorNaVrchu() priestorNaVrchu}())
					{@link Skript Skript}.{@link Skript#vynorPriestor() vynorPriestor}();
				krok = {@code valfalse};
				prerušiť = {@code valfalse};
				{@link GRobot#domov() domov}();

				{@link GRobot#predvolenáHrúbkaPera() predvolenáHrúbkaPera}();
				{@link GRobot#predvolenáFarba() predvolenáFarba}();
				{@link GRobot#predvolenéPísmo() predvolenéPísmo}();
				{@link GRobot#predvolenýTvar() predvolenýTvar}();

				{@link Plátno podlaha}.{@link Plátno#predvolenáFarbaPozadiaTextu() predvolenáFarbaPozadiaTextu}();
				{@link Plátno strop}.{@link Plátno#farbaPozadiaTextu(Farebnosť) farbaPozadiaTextu}({@link Farebnosť#snehová snehová});
				{@link Plátno strop}.{@link Plátno#písmo(String, double) písmo}({@code srg"Cambria"}, {@code num16});
				{@link Plátno podlaha}.{@link Plátno#písmo(String, double) písmo}({@code srg"Consolas"}, {@code num11});
				{@link Svet Svet}.{@link Svet#predvolenáFarbaPlochy() predvolenáFarbaPlochy}();
				{@link Svet Svet}.{@link Svet#predvolenáFarbaPozadia() predvolenáFarbaPozadia}();

				{@link Svet Svet}.{@link Svet#vymaž() vymaž}();
				{@link Svet Svet}.{@link Svet#titulok(String) titulok}(zoznamSkriptov[
					počítadloSkriptov % zoznamSkriptov.length]);
				{@link Svet Svet}.{@link Svet#spustiSkript(String, boolean) spustiSkript}(zoznamSkriptov[
					počítadloSkriptov % zoznamSkriptov.length], {@code valtrue});
				++počítadloSkriptov;
			}
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code kwdif} ({@code num0} == args.length)
			{
				{@link System System}.{@link System#out out}.{@link java.io.PrintStream#println(String) println}({@code srg"Zadajte mená skriptov ako argumenty!"});
			}
			{@code kwdelse}
			{
				{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestLadeniaSkriptu.cfg"});
				{@code kwdnew} TestLadeniaSkriptu(args);
			}
		}
	}
	</pre>
 * 
 * <p> </p>
 * 
 * <p><b>Ďalšie využitie skriptov</b></p>
 * 
 * <p>Skripty sa dajú použiť aj na oživenie ovládacích prvkov rozhrania
 * programovacieho rámca. Pozri napríklad:
 * 
 * {@link Tlačidlo#skript(String[]) Tlačidlo.skript(riadky)},
 * {@link PoložkaPonuky#skript(String[]) PoložkaPonuky.skript(riadky)},
 * {@link KontextováPoložka#skript(String[])
 * KontextováPoložka.skript(riadky)}.</p>
 * 
 * <!-- p>Vývoj skriptovacieho stroja nebol úplne priamočiary a informácie,
 * ktoré s ním úzko súvisia sú rozmiestnené na viacerých miestach
 * dokumentácie. Užitočné príklady použitia nájdete napríklad […]</p -->
 * 
 * <!-- TODO: Zvážiť výrobu komplexného príkladu použitia (vyššie sa
 * aspoň odkazujem na príklady použitia v rámci dokumentácie). -->
 * 
<!--
Všetky kľúčové slová:


	Riadiace príkazy:

		ak
		dokedy
		inak
		na
		nech
		obzor
		opakuj


	Logické hodnoty:

		ano
		áno
		false
		loz
		lož
		nie
		pravda
		true

-->
 */
@SuppressWarnings("serial")
public abstract class Skript
{
	//		✓	Odkázať sa na inú dokumentáciu.
	//		✓	Dokončiť metódy zmeny plátna. (Opisy.)
	//		✓	Dopracovať úpravu farebnej schémy.
	//		✓	Spracovať princíp nového príkazu „with-do“ – obzor.
		// 
		// Detaily priestoroch obzorov.
		// ----------------------------
		// 
		// Obzory sú (neanonymné) *menné* priestory premenných skriptov.
		// Čiže ide o *pomenované priestory premenných skriptov*.
		// 
		// Vďaka obzorom vznikol priestor na implementáciu krásneho spôsobu
		// definovania kvázi „inštančných“ premenných (zjednodušene len
		// menných priestorov premenných) skriptov. Obzory sa dajú ovládať
		// riadiacim príkazom „obzor“ ekvivalentnou Pascalovskému „with-do.“
		// Funguje principiálne jednoducho: Jej blok netvára anonymný
		// priestor premenných skriptov, namiesto toho čerpá (alebo vytvára
		// nový) pomenovaný obzor, čiže menný priestor premenných skriptov.
		// 
		// (*20. 4. 2019*)
		// 
	//		✓	Zakomponovať ExpressionProcessor.
	//	TODO?	Zafarbiť logické hodnoty zvlášť?


	// Implementácia vyhodnocovača výrazov:
	private static class Výraz extends ExpressionProcessor
	{
		// Posledná inštancia, s ktorou mal pracovať tento
		// vyhodnocovač výrazov:
		/*packagePrivate*/ String poslednáInštancia = null;

		// Inicializačný blok. (Vlastné nastavenia.)
		{
			joinTheIdentifiers(true);
			useCustomValueProviders(true);
		}

		/**
		 * <p>Prekrytá metóda, ktorá má na starosti spracovanie zákazníckych
		 * funkcií triedy <a 
		 * href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/podpora/ExpressionProcessor.java"
		 * target="_blank"><code>ExpressionProcessor</code></a>.</p>
		 * 
		 * @param funcID identifikátor zákazníckej funkcie
		 * @param params hodnoty vstupných parametrov funkcie – trieda Value
		 *     je schopná uchovávať zoznamy hodnôt
		 * @return návratová hodnota zákazníckej funkcie (ak zákaznícka
		 *     funkcia nejestvuje, tak je návratová hodnota {@code valnull})
		 */
		@Override public Value customFunction(String funcID, Value params)
		{
			// System.out.println("funcID: " + funcID);
			// System.out.println("params: " + params);

			final StringBuffer príkaz = new StringBuffer(funcID);

			if (null != params)
			{
				if (params.isList())
				{
					if (params.size() > 0)
					{
						ExpressionProcessor.Literal[] indexes =
							params.getIndexes();
						for (ExpressionProcessor.Literal index : indexes)
						{
							ExpressionProcessor.Value value =
								params.getValue(index);
							if (value.isValidNumber())
							{
								príkaz.append(' ');
								príkaz.append(value.getString());
							}
							else if (value.isString())
							{
								príkaz.append(" \"");
								príkaz.append(value.getString());
							}
							else
							{
								GRobotException.vypíšChybovéHlásenie(
									"Neplatný údajový typ parametra " +
									"zákazníckej funkcie vnútorného " +
									"vyhodnocovača výrazov v skriptoch: " +
									value);
							}
						}
					}
				}
				else if (params.isValidNumber())
				{
					príkaz.append(' ');
					príkaz.append(params.getString());
				}
				else if (params.isString())
				{
					príkaz.append(" \"");
					príkaz.append(params.getString());
				}
				else
				{
					GRobotException.vypíšChybovéHlásenie(
						"Neplatný údajový typ parametra " +
						"zákazníckej funkcie vnútorného " +
						"vyhodnocovača výrazov v skriptoch: " +
						params);
				}
			}

			// System.out.println("Príkaz: " + príkaz);
			// System.out.println("Posledná inštancia: " + poslednáInštancia);
			// int a = vykonajPríkaz(príkaz.toString(), poslednáInštancia);
			// System.out.println("Počet úspechov: " + a);
			// if (0 != a)

			if (0 != vykonajPríkaz(príkaz.toString(), poslednáInštancia))
			{
				// System.out.println("Návratová hodnota: " +
				// 	poslednáNávratováHodnota.toString() + " " +
				// 	poslednáNávratováHodnota.getClass().getName());

				if (poslednáNávratováHodnota instanceof Double)
					return new Value((Double)poslednáNávratováHodnota);

				if (poslednáNávratováHodnota instanceof String)
					return new Value((String)poslednáNávratováHodnota);
			}

			return new Value(Value.TypeOrError.UNKNOWN_VALUE);
		}

		/**
		 * <p>Prekrytá metóda, ktorá má na starosti spracovanie zákazníckych
		 * premenných triedy <a 
		 * href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/podpora/ExpressionProcessor.java"
		 * target="_blank"><code>ExpressionProcessor</code></a>.</p>
		 * 
		 * @param varID identifikátor zákazníckej premennej
		 * @param write hodnota na zápis, pričom ak je {@code valnull}, tak
		 *     je hodnota premennej čítaná
		 * @return návratová hodnota – ak išlo o zápis, tak je to rovnaká
		 *     hodnota, ktorá bola zapísaná; v opačnom prípade je to pôvodná
		 *     hodnota, ktorá bola uložená v premennej (ak zákaznícka premenná
		 *     nejestvuje, tak je návratová hodnota {@code valnull})
		 */
		@Override public Value customVariable(String varID, Value write)
		{
			// System.out.println("varID: " + varID);
			// System.out.println("write: " + write);

			if (null != write)
			{
				if (write.isString())
				{
					zapíšPremennú(varID, write.getString());
					vypíšPremennú(-1, varID, REŤAZCOVÁ_PREMENNÁ);
					return write;
				}
				else if (write.isValidNumber())
				{
					zapíšPremennú(varID, write.get());
					vypíšPremennú(-1, varID, ČÍSELNÁ_PREMENNÁ);
					return write;
				}
				return new Value(Value.TypeOrError.INVALID_ASSIGNMENT);
			}

			if (premennáJestvuje(varID, String.class))
				return new Value((String)čítajPremennú(varID, String.class));

			if (premennáJestvuje(varID, Double.class))
				return new Value((Double)čítajPremennú(varID, Double.class));

			// Namiesto chyby bude nasledovať posledný pokus – volanie
			// vlastnej funkcie bez parametrov:
			// —return new Value(Value.TypeOrError.UNKNOWN_VALUE);—
			return customFunction(varID, null);
		}

		/* TODO

		Implementovať

			Number * String – zopakovanie reťazca
			String * Number – zopakovanie reťazca
			String - String – vyňatie (vymazanie) reťazca

		Zvážiť:
			String / Number – rozdelenie na zoznam
			String * String – skombinovanie reťazcov po znakoch
			String / String – rozdelenie na časti (zoznam) podľa „menovateľa“
			String % String – ako „podiel,“ ale zostane „zvyšok“ – čiže
				zoznam obsahujúci nula, jeden až viacero prvkov „menovateľa,“
				podľa počtu ich výskytu v pôvodnom reťazci (hlbší zmysel by
				to malo pri menovateli vo forme regulárneho výrazu)

			…
		TODO */
	}
	private final static Výraz výraz = new Výraz();


	// Zoznam pomenovaných skriptov.
	/*packagePrivate*/ final static TreeMap<String, Skript>
		zoznamSkriptov = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Konštanty smerujúce na indexy prislúchajúcich farieb vo
	// farebnej schéme ladenia skriptov (farbyLadenia – nižšie).
	/*packagePrivate*/ final static int FARBA_LADENIA_ČÍSLO_RIADKA = 0;
	/*packagePrivate*/ final static int FARBA_LADENIA_MENOVKA = 1;
	/*packagePrivate*/ final static int FARBA_LADENIA_SYMBOL = 2;
	/*packagePrivate*/ final static int FARBA_LADENIA_MENO_INŠTANCIE = 3;
	/*packagePrivate*/ final static int FARBA_LADENIA_NÁZOV_PREMENNEJ = 4;

	/*packagePrivate*/ final static int FARBA_LADENIA_ČÍSLO = 5;
	/*packagePrivate*/ final static int FARBA_LADENIA_FARBA = 6;
	/*packagePrivate*/ final static int FARBA_LADENIA_POLOHA = 7;
	/*packagePrivate*/ final static int FARBA_LADENIA_REŤAZEC = 8;

	/*packagePrivate*/ final static int FARBA_LADENIA_RIADIACI_PRÍKAZ = 9;
	/*packagePrivate*/ final static int FARBA_LADENIA_PRÍKAZ = 10;
	/*packagePrivate*/ final static int FARBA_LADENIA_AKTÍVNY_RIADOK = 11;
	/*packagePrivate*/ final static int FARBA_LADENIA_ZAČIATOK_BLOKU = 12;
	/*packagePrivate*/ final static int FARBA_LADENIA_KONIEC_BLOKU = 13;
	/*packagePrivate*/ final static int FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ = 14;
	/*packagePrivate*/ final static int FARBA_LADENIA_ZLOŽENÝ_VÝRAZ = 15;
	/*packagePrivate*/ final static int FARBA_LADENIA_CHYBA = 16;

	// Mapa farieb ladiacich výpisov.
	/*packagePrivate*/ final static Farba farbyLadenia[] =
	{
		tmavofialová,      // FARBA_LADENIA_ČÍSLO_RIADKA
		tmavooranžová,     // FARBA_LADENIA_MENOVKA
		svetlopurpurová,   // FARBA_LADENIA_SYMBOL
		tmavohnedá,        // FARBA_LADENIA_MENO_INŠTANCIE
		svetloatramentová, // FARBA_LADENIA_NÁZOV_PREMENNEJ

		tyrkysová,         // FARBA_LADENIA_ČÍSLO
		tmavošedá,         // FARBA_LADENIA_FARBA
		zelená,            // FARBA_LADENIA_POLOHA
		tmavočervená,      // FARBA_LADENIA_REŤAZEC

		svetlomodrá,       // FARBA_LADENIA_RIADIACI_PRÍKAZ
		čierna,            // FARBA_LADENIA_PRÍKAZ
		svetložltá,        // FARBA_LADENIA_AKTÍVNY_RIADOK
		svetlošedá,        // FARBA_LADENIA_ZAČIATOK_BLOKU
		svetlošedá,        // FARBA_LADENIA_KONIEC_BLOKU
		svetlozelená,      // FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ
		svetlotyrkysová,   // FARBA_LADENIA_ZLOŽENÝ_VÝRAZ
		svetločervená,     // FARBA_LADENIA_CHYBA
	};


	// Príznak režimu ladenia.
	/*packagePrivate*/ static boolean ladenie = false;

	// Kód poslednej chyby.
	/*packagePrivate*/ static int poslednáChyba = 0;

	// Vzor na orezanie riadka zľava. (Musí byť nahradený referenciou $1,
	// inak sa stratia tabulátory, ktoré symbolizujú bloky.)
	private final static Pattern ltrim = Pattern.compile("^ *(\\t*) *");

	// Vzor na rozdelenie príkazového riadka na slová.
	private final static Pattern vykonajPríkazTokenizer =
		Pattern.compile(" *, *| +");

	// Vzor (používaný ostatnými triedami rámca) na rozdelenie skriptu
	// uloženého v jednom reťazci na riadky. Berie do úvahy v prvom rade
	// znak nového riadka (newline, \n, ASCII 10), ktorý smie susediť so
	// znakom návratu vozíka (carriage return, \r, ASCII 13) z oboch strán
	// a potom (v druhom rade) samostatný znak návratu vozíka.
	/*packagePrivate*/ final static Pattern vykonajSkriptRiadkovač =
		Pattern.compile("\\r?\\n\\r?|\\r");


	// Mapa aritmetických operácií.
	private final static TreeMap<String, Method> operácie =
		new TreeMap<String, Method>()
	{
		private void pridaj(String symbol, String metóda)
		{
			try
			{
				put(symbol, Operácie.class.getMethod(metóda,
					double.class, double.class));
			}
			catch (Exception e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		{
			pridaj("+", "sčítanie");
			pridaj("-", "odčítanie");
			pridaj("*", "násobenie");
			pridaj("/", "delenie");
			pridaj("%", "zvyšok");
			pridaj("_", "odseknutie");
			pridaj("~", "zmenaZnamienka");
			pridaj("==", "zhoda");
			pridaj("!=", "nezhoda");
			pridaj("!", "negácia");
			pridaj(">", "väčší");
			pridaj("<", "menší");
			pridaj(">=", "väčšíRovný");
			pridaj("<=", "menšíRovný");
			pridaj("&&", "aSúčasne");
			pridaj("||", "alebo");
		}
	};

	// Vyhodnocuje reálnočíselné literály alebo názvy premenných.
	private static double vyhodnoťReálnyVýraz(String výraz)
	{
		if (výraz.equalsIgnoreCase("true") ||
			výraz.equalsIgnoreCase("áno") ||
			výraz.equalsIgnoreCase("ano") ||
			výraz.equalsIgnoreCase("pravda")) return 1.0;

		if (výraz.equalsIgnoreCase("false") ||
			výraz.equalsIgnoreCase("nie") ||
			výraz.equalsIgnoreCase("lož") ||
			výraz.equalsIgnoreCase("loz")) return 0.0;

		try { return Double.parseDouble(výraz); }
		catch (Exception e) { /* Nevadí, to je len prvý pokus. */ }
		Double value = (Double)čítajPremennú(výraz, Double.class);
		if (null != value) return value;
		return 0.0;
	}

	// Spracúva logické literály, prípadne reálnu číselnú hodnotu,
	// kedy nula je lož, ostatné hodnoty sú pravda.
	private static boolean vyhodnoťLogickýVýraz(String výraz)
	{ return 0.0 != vyhodnoťReálnyVýraz(výraz); }

	// Vyhodnocuje farebné literály alebo názvy farebných premenných.
	private static Color vyhodnoťFarbu(String výraz, Color predvolená)
	{
		try { return (Color)Farebnosť.class.getField(výraz.toLowerCase()).
			get(null); } catch (Exception e) { /* Nevadí… */ }
		Color value = (Color)čítajPremennú(výraz, Color.class);
		if (null != value) return value;
		return predvolená;
	}

	// Vyhodnocuje názvy polohových premenných.
	private static Poloha vyhodnoťPolohu(String výraz, Poloha predvolená)
	{
		Poloha value = (Poloha)čítajPremennú(výraz, Poloha.class);
		if (null != value) return value;
		return predvolená;
	}

	// Vyhodnocuje názvy reťazcových premenných.
	private static String vyhodnoťReťazec(String výraz, String predvolená)
	{
		String value = (String)čítajPremennú(výraz, String.class);
		if (null != value) return value;
		return predvolená;
	}

	// Premapovanie niektorých metód.
	private final static TreeMap<String, String> premapovanieMetód =
		new TreeMap<String, String>()
	{{
		put("náhodnéČíslo", "náhodnéReálneČíslo");
		put("nahodneCislo", "náhodnéReálneČíslo");
		put("náhodnéCeléČíslo", "náhodnéReálneČíslo");
		put("nahodneCeleCislo", "náhodnéReálneČíslo");

		put("upravČíslo", "upravReálneČíslo");
		put("upravCislo", "upravReálneČíslo");
		put("upravCeléČíslo", "upravReálneČíslo");
		put("upravCeleCislo", "upravReálneČíslo");

		put("zadajČíslo", "zadajReálneČíslo");
		put("zadajCislo", "zadajReálneČíslo");
		put("zadajCeléČíslo", "zadajReálneČíslo");
		put("zadajCeleCislo", "zadajReálneČíslo");
		put("zadajReálneČíslo", "zadajReálneČíslo");
		put("zadajRealneCislo", "zadajReálneČíslo");

		// TODO: zdokumentovať:
		put("reťazecNaČíslo", "reťazecNaReálneČíslo");
		put("retazecNaCislo", "reťazecNaReálneČíslo");
		put("reťazecNaCeléČíslo", "reťazecNaReálneČíslo");
		put("retazecNaCeleCislo", "reťazecNaReálneČíslo");
		put("retazecNaRealneCislo", "reťazecNaReálneČíslo");

		put("zadajReťazec", "zadajReťazec");
		put("zadajRetazec", "zadajReťazec");
		put("zadajHeslo", "zadajHeslo");

		// Poznámka: Hľadaj poznámky pri metódach označVšetkyTexty
		//     a zrušOznačenieTextov nižšie.
		put("označTexty", "označVšetkyTexty");
		put("oznacTexty", "označVšetkyTexty");
		put("zrušOznačenieVšetkýchTextov", "zrušOznačenieTextov");
		put("zrusOznacenieVsetkychTextov", "zrušOznačenieTextov");

		// Nasledujúce premapovania sú dôležité, aby sa zjednodušila
		// relatívne netriviálna kontrola, ktorá je vykonávaná pri
		// príkaze „vypíš riadok na“:
		put("vypisRiadokNa", "vypíšRiadokNa");
		put("píšRiadokNa", "vypíšRiadokNa");
		put("pisRiadokNa", "vypíšRiadokNa");

		// Rovnako ako pri predchádzajúcej sérii príkazov to platí
		// aj pri týchto premapovaniach:
		put("píšRiadok", "vypíšRiadok");
		put("pisRiadok", "vypíšRiadok");
		put("vypisNa", "vypíšNa");
		put("píšNa", "vypíšNa");
		put("pisNa", "vypíšNa");
		put("vypis", "vypíš");
		put("píš", "vypíš");
		put("pis", "vypíš");

		put("vp", "vpravo");
		put("vľ", "vľavo");
		put("vl", "vľavo");
		put("do", "dopredu");
		put("vz", "vzad");

		put("ph", "zdvihniPero");
		put("pd", "položPero");
	}};

	// Pokúsi sa nájsť v zadanej triede metódu podľa typickej schémy
	// Javy: «názov metódy»(«typy parameterov»).
	private static Method nájdiMetódu(Class<?> trieda,
		String názovMetódy, Class<?>... typyParametrov)
	{
		try
		{
			Method metóda = trieda.getMethod(názovMetódy, typyParametrov);
			return metóda;
		}
		catch (Exception e)
		{
			// V tomto prípade musia byť chybové
			// hlásenia ignorované – metóda s nimi
			// počíta –> určite nejaké vzniknú
			// GRobotException.vypíšChybovéHlásenia(e/*, false*/);
		}

		return null;
	}

	// Príznak poslednej návratovej hodnoty metódy volanej pomocou
	// mechanizmu reflexie Javy.
	private static Object poslednáNávratováHodnota = null;

	// Metóda volajúca inú metódu rámca prostredníctvom mechanizmu reflexie
	// Javy. (TODO: Overiť, či vlastne tento mechanizmus funguje aj v JRE.
	// Podľa dokumentácie by mal, ale chcelo by to urobiť jednoduchý test:
	// spustiť skript v aplikácii spustenej s JRE.)
	private static Object vykonaj(Method metóda, Object obj, Object...
		args) throws IllegalAccessException, IllegalArgumentException,
		InvocationTargetException
	{
		if (ladenie)
		{
			final StringBuffer volanie = new StringBuffer();
			volanie.setLength(0);
			volanie.append(metóda.getName());
			volanie.append('(');
			boolean prvý = true;
			for (Object arg : args)
			{
				if (arg instanceof Object[])
				{
					Object[] subargs = (Object[])arg;
					for (Object subarg : subargs)
					{
						if (prvý) prvý = false;
						else volanie.append(", ");
						if (subarg instanceof CharSequence)
						{
							volanie.append('"');
							volanie.append(subarg.toString());
							volanie.append('"');
						}
						else
							volanie.append(subarg.toString());
					}
				}
				else
				{
					if (prvý) prvý = false; else volanie.append(", ");
					if (arg instanceof CharSequence)
					{
						volanie.append('"');
						volanie.append(arg.toString());
						volanie.append('"');
					}
					else
						volanie.append(arg.toString());
				}
			}
			volanie.append(')');
			String príkaz = volanie.toString();
			if (null != ObsluhaUdalostí.počúvadlo && ObsluhaUdalostí.
				počúvadlo.ladenie(-1, príkaz, ZABRÁNIŤ_VYKONANIU))
			{
				throw new GRobotException(
					"Vykonanie príkazu bolo zrušené: " + príkaz,
					"commandCancelled", príkaz);
			}
		}
		poslednáNávratováHodnota = metóda.invoke(obj, args);
		return poslednáNávratováHodnota;
	}

	// Zásobník metódy na opakovanie znakov.
	private final static StringBuffer opakujZnak = new StringBuffer();

	// Metóda na opakovanie znakov. (Napríklad na generovanie odsadenia.)
	private static String opakujZnak(char znak, int opakuj)
	{
		opakujZnak.setLength(0);
		for (int i = 0; i < opakuj; ++i)
			opakujZnak.append(znak);
		return opakujZnak.toString();
	}

	// Príznak pokračovania (ukončenia) vykonávania skriptu počas ladenia.
	private static boolean neprerušené = true;

	// Metóda overujúca, či má byť ladenie prerušené, alebo nie.
	// (Rozhodnutie má v skutočnosti na starosti reakcia „ladenie“
	// obsluhy udalostí. Jedna z možných implementácií je v príklade
	// použitia v opise metódy Svet.spustiSkript(String[]).)
	private static boolean prerušiťLadenie(int čr, String riadok)
	{
		if (null == ObsluhaUdalostí.počúvadlo) return false;

		while (ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, ČAKAŤ))
			try { Thread.sleep(350); }
			catch (InterruptedException ie) {} // …

		if (ObsluhaUdalostí.počúvadlo.ladenie(čr, riadok, PRERUŠIŤ))
		{
			neprerušené = false;
			return true;
		}
		return false;
	}


	// Mapa menných priestorov obzorov – prehľadáva sa len podľa mena
	// alebo keď je priestor skopírovaný do zásobníka lokálnych
	// priestorov.
	private final static TreeMap<String, PremenneSkriptu>
		obzory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Zásobník lokálnych priestorov premenných… (prehľadáva sa celý zásobník
	// od konca).
	private final static Stack<PremennéSkriptu>
		lokálnePremenné = new Stack<>();

	// Globálny priestor premenných – musí tu jestvovať takáto „poistka“
	// (zároveň je to programátorsky korektné), keby sa „náhodou“ vyčerpal
	// zásobník lokálnych premenných. (Toto je niečo ako hlavný priestor
	// premenných.)
	private final static PremenneSkriptu globálnePremenné =
		new PremenneSkriptu(
			new TreeMap<String, Double>(String.CASE_INSENSITIVE_ORDER),
			new TreeMap<String, Color>(String.CASE_INSENSITIVE_ORDER),
			new TreeMap<String, Poloha>(String.CASE_INSENSITIVE_ORDER),
			new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER));

	private final static PremenneSkriptu globalnePremenne =
		globálnePremenné; /*new PremenneSkriptu(
			globálnePremenné.premenné,
			globálnePremenné.premennéFarby,
			globálnePremenné.premennéPolohy,
			globálnePremenné.premennéReťazce);*/


	// Trieda aritmetických operácií.
	private final static class Operácie
	{
		public static double sčítanie(double a, double b)
		{ return a + b; }
		public static double odčítanie(double a, double b)
		{ return a - b; }
		public static double násobenie(double a, double b)
		{ return a * b; }
		public static double delenie(double a, double b)
		{ return a / b; }
		public static double zvyšok(double a, double b)
		{ return a % b; }
		public static double odseknutie(double a, double b)
		{ return (long)b; }
		public static double zmenaZnamienka(double a, double b)
		{ return -b; }

		public static double zhoda(double a, double b)
		{ return a == b ? 1.0 : 0.0; }
		public static double nezhoda(double a, double b)
		{ return a != b ? 1.0 : 0.0; }
		public static double negácia(double a, double b)
		{ return 0.0 != b ? 0.0 : 1.0; }
		public static double väčší(double a, double b)
		{ return a > b ? 1.0 : 0.0; }
		public static double menší(double a, double b)
		{ return a < b ? 1.0 : 0.0; }
		public static double väčšíRovný(double a, double b)
		{ return a >= b ? 1.0 : 0.0; }
		public static double menšíRovný(double a, double b)
		{ return a <= b ? 1.0 : 0.0; }

		public static double aSúčasne(double a, double b)
		{ return (0.0 != a && 0.0 != b) ? 1.0 : 0.0; }
		public static double alebo(double a, double b)
		{ return (0.0 != a || 0.0 != b) ? 1.0 : 0.0; }
	}

	// Plátno, na ktoré má režim krokovania smerovať svoje výpisy.
	private static Plátno plátno = GRobot.strop;

	// Táto metóda hrá úlohu writeln/println (s novým riadkom
	// a zároveň počítadlom riadkov).
	/*packagePrivate*/ static void vypíšRiadokLadenia(
		Farba f, Object... argumenty)
	{
		Color fb = plátno.farbaTextu();
		plátno.farbaTextu(f);
		for (Object argument : argumenty)
			if (argument instanceof Farba)
				plátno.farbaTextu((Farba)argument);
			else
				plátno.vypíš(argument);
		plátno.vypíšRiadok();
		plátno.farbaTextu(fb);
	}

	// Táto metóda hrá úlohu write/print (bez nového riadka).
	/*packagePrivate*/ static void vypíšÚdajLadenia(
		Farba f, Object... argumenty)
	{
		Color fb = plátno.farbaTextu();
		plátno.farbaTextu(f);
		for (Object argument : argumenty)
			if (argument instanceof Farba)
				plátno.farbaTextu((Farba)argument);
			else
				plátno.vypíš(argument);
		plátno.farbaTextu(fb);
	}

	// Trieda záznamov zásobníka výpisov premenných.
	private static class ZáznamPremennej
	{
		private int riadok;
		private String názov;
		private int typ;

		ZáznamPremennej(int riadok, String názov, int typ)
		{
			this.riadok = riadok;
			this.názov = názov;
			this.typ = typ;
		}

		void vypíš()
		{
			Double hodnota1 = (0 == typ || ČÍSELNÁ_PREMENNÁ == typ) ?
				(Double)čítajPremennú(názov, Double.class) : null;
			Color hodnota2  = (0 == typ || FAREBNÁ_PREMENNÁ == typ) ?
				(Color)čítajPremennú(názov, Color.class) : null;
			Poloha hodnota3 = (0 == typ || POLOHOVÁ_PREMENNÁ == typ) ?
				(Poloha)čítajPremennú(názov, Poloha.class) : null;
			String hodnota4 = (0 == typ || REŤAZCOVÁ_PREMENNÁ == typ) ?
				(String)čítajPremennú(názov, String.class) : null;

			if (null == hodnota1 && null == hodnota2 &&
				null == hodnota3 && null == hodnota4)
				vypíšRiadokLadenia(farbyLadenia[
					FARBA_LADENIA_NÁZOV_PREMENNEJ], "     ",
					názov, farbyLadenia[
					FARBA_LADENIA_CHYBA], " ?");
			else
			{
				if (null != hodnota1)
					vypíšRiadokLadenia(farbyLadenia[
							FARBA_LADENIA_NÁZOV_PREMENNEJ],
						"     ", názov, farbyLadenia[
							FARBA_LADENIA_SYMBOL], " = ",
						farbyLadenia[
							FARBA_LADENIA_ČÍSLO], hodnota1);

				if (null != hodnota2)
					vypíšRiadokLadenia(farbyLadenia[
							FARBA_LADENIA_NÁZOV_PREMENNEJ],
						"     ", názov, farbyLadenia[
							FARBA_LADENIA_SYMBOL], " = ",
						farbyLadenia[
							FARBA_LADENIA_FARBA],
						Farba.farbaNaReťazec(hodnota2));

				if (null != hodnota3)
					vypíšRiadokLadenia(farbyLadenia[
							FARBA_LADENIA_NÁZOV_PREMENNEJ],
						"     ", názov, farbyLadenia[
							FARBA_LADENIA_SYMBOL], " = ",
						farbyLadenia[
							FARBA_LADENIA_POLOHA],
						Bod.polohaNaReťazec(hodnota3));

				if (null != hodnota4)
					vypíšRiadokLadenia(farbyLadenia[
							FARBA_LADENIA_NÁZOV_PREMENNEJ],
						"     ", názov, farbyLadenia[
							FARBA_LADENIA_SYMBOL], " = ",
						farbyLadenia[
							FARBA_LADENIA_REŤAZEC],
						"\"", hodnota4, "\"");
			}
		}
	}

	// Zásobník výpisov premenných.
	private final static Vector<ZáznamPremennej>
		zásobníkPremenných = new Vector<>();

	// Vyprázdňovač zásobníka výpisov premenných.
	private static void vyprázdniZásobníkPremenných()
	{
		for (ZáznamPremennej záznam : zásobníkPremenných)
			záznam.vypíš();
		zásobníkPremenných.clear();
	}

	// Vypíše hodnotu premennej alebo premenných so zadaným menom a typom.
	// Ak je zadaný príznak typu 0, tak sú vypísané jestvujúce premenné so
	// zadaným menom všetkých typov.
	/*packagePrivate*/ static void vypíšPremennú(int riadok,
		String názov, int typ) // 0 == všetky typy
	{
		if (ladenie && (null == ObsluhaUdalostí.počúvadlo ||
			ObsluhaUdalostí.počúvadlo.ladenie(riadok, názov, typ)))
		{
			// Správanie metódy bolo upravené tak, aby sa záznamy o výpisoch
			// premenných najskôr ukladali do zásobníka a potom sa vypísali
			// naraz pred vykonaním ďalšieho príkazu.
			zásobníkPremenných.add(new ZáznamPremennej(riadok, názov, typ));
		}
	}


	// Implementácia príkazov „interaktívneho režimu“ (vlastne aj skriptov).
	// Táto časť implementácie vracia úspešnosť vykonania príkazu. (To je
	// kľúčové, lebo pri neúspechu sa to dá zopakovať iným spôsobom.)
	@SuppressWarnings("fallthrough")
	/*packagePrivate*/ static boolean vykonajPríkaz(
		String príkaz, Class<?> trieda, Object objektInštancie)
	{
		final StringBuffer názovMetódy = new StringBuffer();
		String reťazec, slová[];

		int prvéÚvodzovky = príkaz.indexOf('"');
		int mriežka = príkaz.indexOf('#');
		if (-1 != prvéÚvodzovky && (-1 == mriežka ||
			mriežka >= prvéÚvodzovky))
		{
			int poslednéÚvodzovky = príkaz.lastIndexOf('"');
			if (-1 != poslednéÚvodzovky &&
				prvéÚvodzovky != poslednéÚvodzovky)
			{
				reťazec = príkaz.substring(prvéÚvodzovky + 1,
					poslednéÚvodzovky);
				príkaz = príkaz.substring(0, prvéÚvodzovky) +
					príkaz.substring(poslednéÚvodzovky + 1);
			}
			else
			{
				reťazec = príkaz.substring(prvéÚvodzovky + 1);
				príkaz = príkaz.substring(0, prvéÚvodzovky);
			}
			mriežka = príkaz.indexOf('#');
		}
		else reťazec = null;

		if (-1 != mriežka)
		{
			String textVýrazu = príkaz.substring(mriežka + 1);
			príkaz = príkaz.substring(0, mriežka);
			if (výraz.attachString(textVýrazu))
			{
				if (výraz.parse())
				{
					// System.out.println("OK!\n" + výraz.getRoot().
					// 	dumpRecursive() + " : " + výraz.getValue());
					ExpressionProcessor.Value výsledok = výraz.getValue();
					if (výsledok.isError())
					{
						poslednáChyba = 51 + výsledok.getError().ordinal();
						return false;
					}
					else if (výsledok.isString())
					{
						reťazec = výsledok.getString();
						// System.out.println("Príkaz parsera: „" + príkaz +
						// 	"“; reťazec: „" + reťazec + "“");
					}
					else
					{
						if (0 == mriežka) return true;
						príkaz += " " + výsledok.toString();
						// System.out.println("Príkaz parsera: " + príkaz +
						// 	" (reťazec: " + reťazec + ")");
					}
				}
				else
				{
					// System.out.println(výraz);
					poslednáChyba = 51 + výraz.
						getValue().getError().ordinal();
					return false;
				}
			}
			else
			{
				// System.out.println("Cannot attach this string…");
				poslednáChyba = 50;
				return false;
			}
		}

		slová = vykonajPríkazTokenizer.split(príkaz.trim());

		double argument1 = 0.0, argument2 = 0.0,
			argument3 = 0.0, argument4 = 0.0;
		Color farba = null; Poloha poloha = null;
		String medzivýsledok = null;

		Object výsledok = null; Method operácia = null;
		int prvéSlovo = 0; String výsledokDoPremennej = null;
		Double hodnotaPremennej = null;

		if (slová.length > 1 && null == reťazec)
		{
			reťazec = vyhodnoťReťazec(slová[slová.length - 1], null);
			if (null != reťazec)
				slová = Arrays.copyOf(slová, slová.length - 1);
		}

		try {

		if (slová.length > 1 && slová[0].equalsIgnoreCase("nech"))
		{
			prvéSlovo = 2;
			výsledokDoPremennej = slová[1];

			if (slová.length > 2)
			{
				if (slová[2].equals("=")) ++prvéSlovo; else
				{
					operácia = operácie.get(slová[2].toLowerCase());
					if (null != operácia)
					{
						++prvéSlovo;
						if (prvéSlovo >= slová.length &&
							(slová[2].equals("_") ||
							slová[2].equals("!") ||
							slová[2].equals("~")))
						{
							// Prejsť na okamžité spracovanie
							// unárnych operácií (v bloku finally).
							výsledok = (Double)čítajPremennú(
								výsledokDoPremennej, Double.class);
							if (null == výsledok) výsledok = 0.0;
							return true;
						}
					}
				}

				if (slová.length > prvéSlovo)
				{
					// Overenie, či sa hodnota nedá vyhodnotiť okamžite.
					// Skutočné priradenie je v bloku finally.
					try
					{
						// Literál.
						hodnotaPremennej =
							Double.parseDouble(slová[prvéSlovo]);
					}
					catch (Exception e)
					{
						// Iná číselná premenná.
						hodnotaPremennej = (Double)čítajPremennú(
							slová[prvéSlovo], Double.class);
					}

					if (null == hodnotaPremennej)
					{
						// Iná farebná premenná.
						výsledok = vyhodnoťFarbu(
							slová[prvéSlovo], null);

						if (null == výsledok)
						{
							// Iná polohová premenná.
							výsledok = vyhodnoťPolohu(
								slová[prvéSlovo], null);

							if (null == výsledok)
							{
								// Iná reťazcová premenná.
								výsledok = vyhodnoťReťazec(
									slová[prvéSlovo], null);

								if (null != výsledok) return true;
							}
							else return true;
						}
						else return true;
					}
					else
					{
						výsledok = hodnotaPremennej;
						return true;
					}
				}
				else if (null != reťazec)
				{
					výsledok = reťazec;
					return true;
				}
			}
		}

		for (int početArgumentov = 0;
			početArgumentov + prvéSlovo < slová.length;
			++početArgumentov)
		{
			názovMetódy.setLength(0);
			názovMetódy.append(slová[prvéSlovo].toLowerCase());

			for (int i = prvéSlovo + 1; i < slová.length -
				početArgumentov; ++i)
			{
				if (slová[i].length() > 1)
				{
					názovMetódy.append(Character.
						toUpperCase(slová[i].charAt(0)));
					názovMetódy.append(slová[i].
						substring(1).toLowerCase());
				}
				else if (slová[i].length() == 1)
				{
					názovMetódy.append(Character.
						toUpperCase(slová[i].charAt(0)));
				}
			}

			String premapovanie = premapovanieMetód.get(
				názovMetódy.toString());
			if (null != premapovanie)
			{
				názovMetódy.setLength(0);
				názovMetódy.append(premapovanie);
			}

			String názovMetódyS = názovMetódy.toString();

			try {

			// Pozor! Vyšší počet argumentov obracia poradie ich
			// zadania do volanej metódy!
			switch (početArgumentov)
			{
			case 4: argument4 = vyhodnoťReálnyVýraz(
					slová[slová.length - 4]);
			case 3: argument3 = vyhodnoťReálnyVýraz(
					slová[slová.length - 3]);
			case 2: argument2 = vyhodnoťReálnyVýraz(
					slová[slová.length - 2]);
			case 1: argument1 = vyhodnoťReálnyVýraz(
					slová[slová.length - 1]);
			}

			Method metóda = null;


			// Metódy typu vypíšRiadok.
			if (názovMetódyS.equals("vypíšRiadok"))
			{
				metóda = nájdiMetódu(Svet.class,
					"vypíšRiadok", Object[].class);
			}
			else if (názovMetódyS.equals("vypíš"))
			{
				metóda = nájdiMetódu(Svet.class,
					"vypíš", Object[].class);
			}

			if (null != metóda)
			{
				if (null == reťazec)
				{
					switch (početArgumentov)
					{
					case 0: vykonaj(metóda, null,
							(Object)new Object[]{}); break;
					case 1:
						{
							farba = vyhodnoťFarbu(
								slová[slová.length - 1], null);
							if (null == farba)
							{
								poloha = vyhodnoťPolohu(
									slová[slová.length - 1], null);
								if (null == poloha)
								{
									medzivýsledok = vyhodnoťReťazec(
										slová[slová.length - 1], null);
									if (null == medzivýsledok)
										vykonaj(metóda, null, (Object)new
											Object[]{argument1});
									else
										vykonaj(metóda, null, (Object)new
											Object[]{medzivýsledok});
								}
								else
									vykonaj(metóda, null, (Object)new
										Object[]{Bod.
										polohaNaReťazec(poloha)});
							}
							else
								vykonaj(metóda, null, (Object)new
									Object[]{Farba.
									farbaNaReťazec(farba)});
						}
						break;
					// Pozor! Vyšší počet argumentov obracia poradie
					// ich zadania do volanej metódy!
					case 2: vykonaj(metóda, null, (Object)new
							Object[]{argument2, argument1}); break;
					case 3: vykonaj(metóda, null, (Object)new
							Object[]{argument3, argument2,
							argument1}); break;
					case 4: vykonaj(metóda, null, (Object)new
							Object[]{argument4, argument3,
							argument2, argument1}); break;
					}
				}
				else
				{
					switch (početArgumentov)
					{
					case 0: vykonaj(metóda, null, (Object)new
							Object[]{reťazec}); break;
					case 1:
						{
							farba = vyhodnoťFarbu(
								slová[slová.length - 1], null);
							if (null == farba)
							{
								poloha = vyhodnoťPolohu(
									slová[slová.length - 1], null);
								if (null == poloha)
								{
									medzivýsledok = vyhodnoťReťazec(
										slová[slová.length - 1], null);
									if (null == medzivýsledok)
										vykonaj(metóda, null, (Object)new
											Object[]{reťazec, argument1});
									else
										vykonaj(metóda, null, (Object)new
											Object[]{reťazec, medzivýsledok});
								}
								else
									vykonaj(metóda, null, (Object)new
										Object[]{reťazec, Bod.
										polohaNaReťazec(poloha)});
							}
							else
								vykonaj(metóda, null, (Object)new
									Object[]{reťazec,
									Farba.farbaNaReťazec(farba)});
						}
						break;
					// Pozor! Vyšší počet argumentov obracia poradie
					// ich zadania do volanej metódy!
					case 2: vykonaj(metóda, null, (Object)new
							Object[]{reťazec, argument2, argument1});
							break;
					case 3: vykonaj(metóda, null, (Object)new
							Object[]{reťazec, argument3, argument2,
							argument1}); break;
					case 4: vykonaj(metóda, null, (Object)new
							Object[]{reťazec, argument4, argument3,
							argument2, argument1}); break;
					}
				}

				return true;
			}


			// Metódy typu vypíšRiadokNa.
			if (početArgumentov >= 2)
			{
				if (názovMetódyS.equals("vypíšRiadokNa"))
				{
					metóda = nájdiMetódu(Svet.class,
						"vypíšRiadokNa", double.class,
						double.class, Object[].class);
				}
				else if (názovMetódyS.equals("vypíšNa"))
				{
					metóda = nájdiMetódu(Svet.class, "vypíšNa",
						double.class, double.class, Object[].class);
				}

				if (null != metóda)
				{
					if (null == reťazec)
					{
						switch (početArgumentov)
						{
						// Pozor! Vyšší počet argumentov obracia poradie
						// ich zadania do volanej metódy!
						case 2: vykonaj(metóda, null, argument2,
								argument1, (Object)new
								Object[]{}); break;
						case 3: vykonaj(metóda, null, argument3,
								argument2,(Object)new Object[]{
								argument1}); break;
						case 4: vykonaj(metóda, null, argument4,
								argument3, (Object)new Object[]{
								argument2, argument1}); break;
						}
					}
					else
					{
						switch (početArgumentov)
						{
						// Pozor! Vyšší počet argumentov obracia poradie
						// ich zadania do volanej metódy!
						case 2: vykonaj(metóda, null, argument2,
								argument1,(Object)new Object[]{
								reťazec}); break;
						case 3: vykonaj(metóda, null, argument3,
								argument2,(Object)new Object[]{
								reťazec, argument1}); break;
						case 4: vykonaj(metóda, null, argument4,
								argument3, (Object)new Object[]{
								reťazec, argument2, argument1}); break;
						}
					}

					return true;
				}
			}


			// Klasické spracovanie.
			if (null == reťazec)
			{
				switch (početArgumentov)
				{
				case 0:
					metóda = nájdiMetódu(trieda, názovMetódyS);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie);
						return true;
					}
					break;

				case 1:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						boolean.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							0.0 != argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Color.class);
					if (null != metóda)
					{
						farba = vyhodnoťFarbu(
							slová[slová.length - 1], žiadna);
						výsledok = vykonaj(metóda, objektInštancie, farba);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Poloha.class);
					if (null != metóda)
					{
						poloha = vyhodnoťPolohu(
							slová[slová.length - 1], null);
						if (null == poloha)
							výsledok = vykonaj(metóda,
								objektInštancie, Poloha.stred);
						else
							výsledok = vykonaj(metóda,
								objektInštancie, poloha);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class);
					if (null != metóda)
					{
						medzivýsledok = vyhodnoťReťazec(
							slová[slová.length - 1], null);
						if (null == medzivýsledok)
							výsledok = vykonaj(metóda,
								objektInštancie, "");
						else
							výsledok = vykonaj(metóda,
								objektInštancie, medzivýsledok);
						return true;
					}

					break;

				// Pozor! Vyšší počet argumentov obracia poradie
				// ich zadania do volanej metódy!
				case 2:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument2, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument2, (int)argument1);
						return true;
					}
					break;

				case 3:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class, double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument3, argument2, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument3, (int)argument2,
							(int)argument1);
						return true;
					}
					break;

				case 4:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class, double.class,
						double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument4, argument3, argument2, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class, int.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument4, (int)argument3,
							(int)argument2, (int)argument1);
						return true;
					}
					break;

				default: return false;
				}
			}
			else
			{
				switch (početArgumentov)
				{
				case 0:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec);
						return true;
					}
					break;

				case 1:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Double.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						boolean.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							0.0 != argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Color.class, String.class);
					if (null != metóda)
					{
						farba = vyhodnoťFarbu(
							slová[slová.length - 1], žiadna);
						výsledok = vykonaj(metóda, objektInštancie,
							farba, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						Poloha.class, String.class);
					if (null != metóda)
					{
						poloha = vyhodnoťPolohu(
							slová[slová.length - 1], null);
						if (null == poloha)
							výsledok = vykonaj(metóda, objektInštancie,
								Poloha.stred, reťazec);
						else
							výsledok = vykonaj(metóda, objektInštancie,
								poloha, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, String.class);
					if (null != metóda)
					{
						medzivýsledok = vyhodnoťReťazec(
							slová[slová.length - 1], null);
						if (null == medzivýsledok)
							výsledok = vykonaj(metóda, objektInštancie,
								"", reťazec);
						else
							výsledok = vykonaj(metóda, objektInštancie,
								medzivýsledok, reťazec);
						return true;
					}


					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, Double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, (int)argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, boolean.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, 0.0 != argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, Color.class);
					if (null != metóda)
					{
						farba = vyhodnoťFarbu(
							slová[slová.length - 1], žiadna);
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, farba);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, Poloha.class);
					if (null != metóda)
					{
						poloha = vyhodnoťPolohu(
							slová[slová.length - 1], null);
						if (null == poloha)
							výsledok = vykonaj(metóda, objektInštancie,
								reťazec, Poloha.stred);
						else
							výsledok = vykonaj(metóda, objektInštancie,
								reťazec, poloha);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, String.class);
					if (null != metóda)
					{
						medzivýsledok = vyhodnoťReťazec(
							slová[slová.length - 1], null);
						if (null == medzivýsledok)
							výsledok = vykonaj(metóda, objektInštancie,
								reťazec, "");
						else
							výsledok = vykonaj(metóda, objektInštancie,
								reťazec, medzivýsledok);
						return true;
					}
					break;

				// Pozor! Vyšší počet argumentov obracia poradie
				// ich zadania do volanej metódy!
				case 2:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument2, argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument2, (int)argument1, reťazec);
						return true;
					}
					break;

				case 3:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class, double.class,
						String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							argument3, argument2, argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class, int.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument3, (int)argument2,
							(int)argument1, reťazec);
						return true;
					}


					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, double.class, double.class,
						double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie, reťazec,
							argument3, argument2, argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, int.class, int.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, (int)argument3, (int)argument2,
							(int)argument1);
						return true;
					}
					break;

				case 4:
					metóda = nájdiMetódu(trieda, názovMetódyS,
						double.class, double.class, double.class,
						double.class, String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie, argument4,
							argument3, argument2, argument1, reťazec);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						int.class, int.class, int.class, int.class,
						String.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							(int)argument4, (int)argument3,
							(int)argument2, (int)argument1, reťazec);
						return true;
					}


					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, double.class, double.class,
						double.class, double.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda, objektInštancie,
							reťazec, argument4, argument3, argument2,
							argument1);
						return true;
					}

					metóda = nájdiMetódu(trieda, názovMetódyS,
						String.class, int.class, int.class,
						int.class, int.class);
					if (null != metóda)
					{
						výsledok = vykonaj(metóda,
							objektInštancie, reťazec,
							(int)argument4, (int)argument3,
							(int)argument2, (int)argument1);
						return true;
					}
					break;

				default: return false;
				}
			}

			} catch (Exception e) {
				poslednáChyba = CHYBA_VYKONANIA_PRÍKAZU;
				GRobotException.vypíšChybovéHlásenia(e, true);
			}
		} // for

		} // try
		finally
		{
			if (null != výsledokDoPremennej && null != výsledok)
			{
				if (výsledok instanceof Color)
				{
					// TODO sčítanie/odčítanie farieb
					zapíšPremennú(výsledokDoPremennej, (Color)výsledok);
					vypíšPremennú(-1, výsledokDoPremennej, FAREBNÁ_PREMENNÁ);
				}
				else if (výsledok instanceof Poloha)
				{
					// TODO sčítanie/odčítanie polôh (prípadné ďalšie operácie)
					zapíšPremennú(výsledokDoPremennej, (Poloha)výsledok);
					vypíšPremennú(-1, výsledokDoPremennej, POLOHOVÁ_PREMENNÁ);
				}
				else if (výsledok instanceof String)
				{
					if (null != operácia) výsledok = (String)čítajPremennú(
						výsledokDoPremennej, String.class) + výsledok;

					zapíšPremennú(výsledokDoPremennej, (String)výsledok);
					vypíšPremennú(-1, výsledokDoPremennej, REŤAZCOVÁ_PREMENNÁ);
				}
				else if (premennáJestvuje(výsledokDoPremennej, String.class))
				{
					if (null != operácia) výsledok = (String)čítajPremennú(
						výsledokDoPremennej, String.class) + výsledok;

					zapíšPremennú(výsledokDoPremennej, výsledok.toString());
					vypíšPremennú(-1, výsledokDoPremennej, REŤAZCOVÁ_PREMENNÁ);
				}
				else
				{
					for (;;)
					{
						if (výsledok instanceof Double)
						{
							argument1 = (Double)výsledok;
						}
						else if (výsledok instanceof Long)
						{
							argument1 = ((Long)výsledok).doubleValue();
						}
						else if (výsledok instanceof Integer)
						{
							argument1 = ((Integer)výsledok).doubleValue();
						}
						else if (výsledok instanceof Boolean)
						{
							argument1 = ((Boolean)výsledok) ? 1.0 : 0.0;
						}
						else
						{
							argument1 = 0.0;
						}

						if (null != operácia)
						{
							hodnotaPremennej = (Double)čítajPremennú(
								výsledokDoPremennej, Double.class);

							if (null == hodnotaPremennej)
								hodnotaPremennej = 0.0;

							try
							{
								výsledok = operácia.invoke(
									null, hodnotaPremennej, argument1);
							}
							catch (Exception e)
							{
								// chybná operácia je ignorovaná,
								// ale ten prípad by nemal nastať,
								// lebo operácie sú definované
								// v očakávanom tvare (binárnej op.)
							}

							operácia = null;
							continue;
						}

						break;
					}

					zapíšPremennú(výsledokDoPremennej, argument1);
					vypíšPremennú(-1, výsledokDoPremennej, ČÍSELNÁ_PREMENNÁ);
				}
			}
		}

		return false;
	}

	// Detto ako rovnomenná metóda vyššie, len iná pasáž (s inou návratovou
	// hodnotou)… Táto časť implementácie vracia počet úspechov.
	/*packagePrivate*/ static int vykonajPríkaz(
		String príkaz, String aktuálnaInštancia)
	{
		int početÚspechov = 0;
		String poslednáInštancia = výraz.poslednáInštancia;
		výraz.poslednáInštancia = aktuálnaInštancia;

		// Menné priestory ExpressionProcessora sú platné len pre premenné
		// (chcelo by to veľa prerábok, za ktoré to asi nestojí)…
		// 
		// if (-1 != príkaz.indexOf('.'))
		// {
		// 	boolean ešteHľadám = true;
		// 	for (String rezervovaná : new String[] {"svet", "podlaha",
		// 		"strop", "robot"})
		// 	{
		// 		Pattern hľadaj = Pattern.compile("^(?i)" +
		// 			rezervovaná + " *:: *");
		// 		Matcher matcher = hľadaj.matcher(príkaz);
		// 		if (matcher.find())
		// 		{
		// 			výraz.poslednáInštancia = aktuálnaInštancia =
		// 				matcher.group();
		// 			príkaz = matcher.replaceFirst("");
		// 			ešteHľadám = false;
		// 			break;
		// 		}
		// 	}
		// 
		// 	System.out.println("ešteHľadám: " + ešteHľadám);
		// 	System.out.println("príkaz: " + príkaz);
		// 	System.out.println("aktuálnaInštancia: " + aktuálnaInštancia);
		// 
		// 	// if (ešteHľadám)
		// 	// {
		// 	// 	for (GRobot robot : GRobot.zoznamRobotov)
		// 	// 	{
		// 	// 		if (null != robot.menoRobota)
		// 	// 		hľadaj = Pattern.compile("^" + …);
		// 	// 	}
		// 	// }
		// }

		try {

		if (null == aktuálnaInštancia)
		{
			int početRobotov = GRobot.zoznamRobotov.size();
			for (int i = 0; i < početRobotov; ++i)
			{
				GRobot robot = GRobot.zoznamRobotov.get(i);
				if (robot.interaktívnyRežim && robot.vykonajPríkaz(príkaz))
					++početÚspechov;
			}

			if (0 == početÚspechov && Svet.interaktívnyRežim &&
				Svet.vykonajPríkaz(príkaz)) ++početÚspechov;

			if (0 == početÚspechov)
			{
				if (GRobot.podlaha.interaktívnyRežim &&
					GRobot.podlaha.vykonajPríkaz(príkaz)) ++početÚspechov;

				if (GRobot.strop.interaktívnyRežim &&
					GRobot.strop.vykonajPríkaz(príkaz)) ++početÚspechov;
			}
		}
		else
		{
			if (aktuálnaInštancia.equalsIgnoreCase("svet"))
			{
				if (Svet.vykonajPríkaz(príkaz))
					++početÚspechov;
			}
			else if (aktuálnaInštancia.equalsIgnoreCase("podlaha"))
			{
				if (GRobot.podlaha.vykonajPríkaz(príkaz))
					++početÚspechov;
			}
			else if (aktuálnaInštancia.equalsIgnoreCase("strop"))
			{
				if (GRobot.strop.vykonajPríkaz(príkaz))
					++početÚspechov;
			}
			else if (aktuálnaInštancia.equalsIgnoreCase("robot"))
			{
				if (Svet.hlavnýRobot.vykonajPríkaz(príkaz))
					++početÚspechov;
			}
			else
			{
				GRobot robot = GRobot.menáRobotov.
					get(aktuálnaInštancia);
				if (null == robot)
				{
					poslednáChyba = CHYBA_NEZNÁME_MENO;
				}
				else
				{
					if (robot.vykonajPríkaz(príkaz))
						++početÚspechov;
				}
			}
		}

		return početÚspechov;

		} finally { výraz.poslednáInštancia = poslednáInštancia; }
	}


	// Inštancia koreňa tohto skriptu. Koreň si uchováva základné informácie,
	// inštanciu hlavného bloku a na koreň sa spätne odkazujú všetky bloky,
	// ktoré sú v tomto skripte vnorené.
	private static class Koreň extends Skript
	{
		// Inštancia hlavného bloku tohto skriptu.
		final Blok hlavný = new Blok(this, 0);

		// Tento zásobník je využívaný pri vnáraní do a vynáraní sa
		// z blokov tohto skriptu. Vďaka tomu je po každom vynorení sa
		// z bloku vrátená posledná aktívna inštancia (známa aj ako
		// interaktívna inštancia).
		final Stack<String> zásobníkInštancií = new Stack<>();

		// Aktuálna (interaktívna) inštancia tohto skriptu.
		String aktuálnaInštancia = null;

		// Signalizácia aktuálneho riadka pri výpise (obnovení) celého
		// skriptu.
		int aktívnyRiadok = -1;

		// Príkaz „na «menovka»“ má výnimku, že môže skočiť aj na menovku,
		// ktorá je definovaná v nadradených blokoch. Toto je súčasť
		// implementácie tohto mechanizmu. // TODO uviesť v dokumentácii //
		String hľadajMenovku = null;
		Integer skočNa = null;

		// Konštruktor – spracuje skript. Riziko je, že počas spracovania
		// vznikne chyba CHYBA_DVOJITÁ_MENOVKA, preto treba vždy kontrolovať
		// stav premennej poslednáChyba.
		Koreň(String[] skript)
		{
			poslednáChyba = ŽIADNA_CHYBA;
			Stack<Blok> zásobník = new Stack<>();
			StringBuffer riadok = new StringBuffer();
			/*#*/ Blok aktívny = hlavný;
			int úroveň = 0;

			for (int číslovanie = 0; číslovanie <
				skript.length; ++číslovanie)
			{
				riadok.setLength(0);
				riadok.append(ltrim.matcher(
					skript[číslovanie]).replaceAll("$1"));

				try
				{
					int staráÚroveň = úroveň;
					for (int i = 0; i < staráÚroveň; ++i)
					{
						if (0 == riadok.length() ||
							'\t' != riadok.charAt(0))
						{
							aktívny = zásobník.pop();
							--úroveň;
						}
						else riadok.deleteCharAt(0);
					}
				}
				catch (EmptyStackException e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}

				while (0 != riadok.length() &&
					'\t' == riadok.charAt(0))
				{
					++úroveň;
					Blok blok = new Blok(this, úroveň);
					aktívny.obsah.add(blok);
					zásobník.push(aktívny);
					aktívny = blok;
					riadok.deleteCharAt(0);
				}

				if (0 != riadok.length())
				{
					if (':' == riadok.charAt(0))
					{
						vytiahniMenovku(riadok);
						if (0 != riadok.length())
						{
							String menovka = riadok.toString();
							if (aktívny.menovky.
								containsKey(menovka))
							{
								// V tomto prípade je v čísle
								// poslednej chyby je zakódované
								// aj číslo riadka, na ktorom
								// chyba vznikla. (V tejto fáze
								// je to unikátny prípad, ale asi
								// sa to prenesie do zvyšku
								// frameworku.)
								poslednáChyba = CHYBA_DVOJITÁ_MENOVKA +
									(100 * (1 + číslovanie));
							}
							else
							{
								aktívny.menovky.put(menovka,
									aktívny.obsah.size());
							}
						}
					}
					else if (';' != riadok.charAt(0))
						aktívny.obsah.add(new Riadok(aktívny,
							1 + číslovanie, riadok.toString()));
				}
			}
		}

		// „Vytiahne“ (extrahuje) menovku zo zadaného StringBuffera.
		private static void vytiahniMenovku(StringBuffer riadok)
		{
			int i = 1;
			for (;i < riadok.length() && riadok.charAt(0) <= ' '; ++i);
			riadok.delete(0, i);

			i = 0;
			for (;i < riadok.length() &&
				riadok.charAt(0) > ' '; ++i);
			if (i < riadok.length())
				riadok.delete(i, riadok.length());
		}

		// „Prekreslí“ (opätovne vypíše) celé znenie tohto skriptu
		// na podlahu sveta (aktívny riadok bude mať farbu schémy
		// FARBA_LADENIA_AKTÍVNY_RIADOK).
		private void prekresli(int aktívnyRiadok)
		{
			boolean stavNekreslenia = Svet.nekresli;
			Svet.nekresli = true;
			plátno.vymažTexty();

			final int posunutieTextovY = plátno.posunutieTextovY();
			int zálohaRiadka = this.aktívnyRiadok;
			this.aktívnyRiadok = aktívnyRiadok;
			// vypíšRiadok("Skript: " + názovAktuálnehoSkriptu); // ¿TODO?
			try { this.vypíš(); } finally
			{ this.aktívnyRiadok = zálohaRiadka; }

			if (Svet.nekresli = stavNekreslenia)
			{
				Svet.neboloPrekreslené = true;
				Svet.vykonaťNeskôr(() -> plátno.
					posunutieTextovY(posunutieTextovY));
			}
			else
			{
				Svet.prekresli();
				plátno.posunutieTextovY(posunutieTextovY);
			}
		}

		// Semafor.
		private boolean spustený = false;

		/**
		 * <p>Zabezpečí vykonanie tej súčasti skriptu, za ktorú je zodpovedná
		 * táto inštancia.</p>
		 */
		public int vykonaj()
		{
			if (spustený) return CHYBA_VOLANIA_SKRIPTU;
			try
			{
				spustený = true;

				neprerušené = true;
				poslednáChyba = ŽIADNA_CHYBA;
				zásobníkInštancií.clear();
				aktuálnaInštancia = null;
				hľadajMenovku = null;
				skočNa = null;

				if (ladenie)
				{
					if (null == ObsluhaUdalostí.počúvadlo ||
						ObsluhaUdalostí.počúvadlo.ladenie(-1, null,
							VYPÍSAŤ_PREMENNÉ))
						vypíšPremenné();

					if (null != ObsluhaUdalostí.počúvadlo &&
						ObsluhaUdalostí.počúvadlo.ladenie(-1, null,
							VYPÍSAŤ_MENOVKY)) hlavný.vypíšMenovky();
				}

				try
				{
					int kód = hlavný.vykonaj();
					if (CHYBA_NEZNÁMA_MENOVKA == (poslednáChyba % 100) &&
						null != hľadajMenovku &&
						"koniec".equalsIgnoreCase(hľadajMenovku))
					{
						poslednáChyba = ŽIADNA_CHYBA;
						kód = 0;
					}
					return kód;
				}
				finally
				{
					if (ladenie && null != ObsluhaUdalostí.počúvadlo &&
						ObsluhaUdalostí.počúvadlo.ladenie(-1, null,
							VYPÍSAŤ_SKRIPT))
					{
						prekresli(-1);
						plátno.vypíšRiadok();
						vyprázdniZásobníkPremenných();
					}
					else zásobníkPremenných.clear();
				}
			}
			finally
			{
				spustený = false;
			}
		}

		/**
		 * <p>Vypíše obsah tejto inštancie na aktuálnu konzolu.
		 * (Predvolene na {@linkplain GRobot#strop strop}. Pozri aj
		 * {@link Skript#presmerujNaPodlahu() presmerujNaPodlahu}.)</p>
		 */
		public void vypíš()
		{
			hlavný.vypíš();
		}
	}

	// Súkromná trieda uchovávajúca blok skriptu.
	private static class Blok extends Skript
	{
		// Koreň bloku skriptu – hlavná inštancia pre tento skript.
		final Koreň koreň;

		// Odsadenie aktuálneho bloku (na účely výpisu).
		final int odsadenie;

		// Zoznam vnorených blokov alebo riadkov skriptu.
		final Vector<Skript> obsah = new Vector<>();

		// Priestor lokálnych premenných alebo obzor.
		PremenneSkriptu premennéBloku = null;

		// Definície menoviek platných pre tento blok skriptu.
		final TreeMap<String, Integer> menovky =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		// Konštruktor.
		Blok(Koreň koreň, int odsadenie)
		{
			this.koreň = koreň;
			this.odsadenie = odsadenie;
		}

		// Vypíše všetky menovky.
		private void vypíšMenovky()
		{
			for (Map.Entry<String, Integer>
				menovka : menovky.entrySet())
			{
				vypíšRiadokLadenia(
					farbyLadenia[FARBA_LADENIA_MENOVKA],
					menovka.getKey(),
					farbyLadenia[FARBA_LADENIA_SYMBOL], ": ",
					farbyLadenia[FARBA_LADENIA_ČÍSLO_RIADKA],
					menovka.getValue());
			}

			for (Skript časť : obsah)
				if (časť instanceof Blok)
					((Blok)časť).vypíšMenovky();
		}

		// Súčasť mechanizmu hľadania menoviek v nadradených blokoch
		// príkazom „na «menovka».“ (Pozri aj premenné koreňa: hľadajMenovku
		// a skočNa.)
		private boolean hľadajMenovku(int kód)
		{
			if (CHYBA_NEZNÁMA_MENOVKA == (poslednáChyba % 100) &&
				null != koreň.hľadajMenovku)
			{
				koreň.skočNa = menovky.get(koreň.hľadajMenovku);
				if (null != koreň.skočNa)
				{
					poslednáChyba = 0;
					koreň.hľadajMenovku = null;
					--koreň.skočNa;
					return true;
				}
			}
			return false;
		}

		/**
		 * <p>Zabezpečí vykonanie tej súčasti skriptu, za ktorú je zodpovedná
		 * táto inštancia.</p>
		 */
		public int vykonaj()
		{
			koreň.zásobníkInštancií.push(koreň.aktuálnaInštancia);

			try
			{
				for (int i = 0; neprerušené && i < obsah.size(); ++i)
				{
					Skript časť = obsah.get(i);
					if (časť instanceof Riadok)
					{
						Riadok riadok = (Riadok)časť;
						if (ladenie)
						{
							if (null != ObsluhaUdalostí.počúvadlo)
							{
								if (ObsluhaUdalostí.počúvadlo.ladenie(
									riadok.číslo, riadok.riadok,
									VYPÍSAŤ_SKRIPT))
								{
									koreň.prekresli(riadok.číslo);
									plátno.vypíšRiadok();
									vyprázdniZásobníkPremenných();
								}
								else if (ObsluhaUdalostí.počúvadlo.ladenie(
									riadok.číslo, riadok.riadok,
									VYPÍSAŤ_RIADOK))
								{
									vyprázdniZásobníkPremenných();
									riadok.vypíš();
								}
								else
									vyprázdniZásobníkPremenných();
							}

							if (prerušiťLadenie(riadok.číslo,
								riadok.riadok)) break;
						}

						if ('@' == riadok.riadok.charAt(0))
						{
							if (riadok.riadok.length() == 1)
								koreň.aktuálnaInštancia = null;
							else
								koreň.aktuálnaInštancia =
									riadok.riadok.substring(1);
							continue;
						}
						else if (riadok.riadok.regionMatches(
							true, 0, "na ", 0, 3))
						{
							String[] slová = vykonajPríkazTokenizer.
								split(riadok.riadok);

							if (slová.length < 2)
							{
								poslednáChyba = CHYBA_CHÝBAJÚCA_MENOVKA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							Integer skočNa = menovky.get(slová[1]);
							if (null == skočNa)
							{
								koreň.hľadajMenovku = slová[1];
								poslednáChyba = CHYBA_NEZNÁMA_MENOVKA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							i = skočNa - 1;
							continue;
						}
						else if (riadok.riadok.equalsIgnoreCase("inak"))
						{
							++i;
							continue;
						}
						else if (riadok.riadok.regionMatches(
							true, 0, "obzor ", 0, 6))
						{
							String[] slová = vykonajPríkazTokenizer.
								split(riadok.riadok);

							if (slová.length < 2 || (i + 1) >= obsah.size())
							{
								poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							Skript telo = obsah.get(i + 1); // telo obzoru

							if (!(telo instanceof Blok))
							{
								poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							((Blok)telo).premennéBloku =
								PremennéSkriptu.dajObzor(slová[1]);
							continue;
						}
						else if (riadok.riadok.regionMatches(
							true, 0, "opakuj ", 0, 7))
						{
							String[] slová = vykonajPríkazTokenizer.
								split(riadok.riadok);

							if (slová.length < 2 || ++i >= obsah.size())
							{
								poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							String premenná = slová[1];

							if (!premennáJestvuje(premenná, Double.class))
							{
								if (slová.length >= 3)
								{
									zapíšPremennú(premenná, new Double((int)
										vyhodnoťReálnyVýraz(slová[2])));
								}
								else
								{
									// Anonymný cyklus:
									premenná = null;

									// poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
									// 	100 * riadok.číslo;
									// return riadok.číslo;
								}
							}

							Skript telo = obsah.get(i);
							int opakovania;

							if (slová.length >= 3)
								opakovania = (int)
									vyhodnoťReálnyVýraz(slová[2]);
							else
								opakovania = (int)
									vyhodnoťReálnyVýraz(slová[1]);

							if (0 == opakovania)
							{
								if (i + 1 < obsah.size())
								{
									Skript časť1 = obsah.get(i + 1);
									if (časť1 instanceof Riadok)
									{
										Riadok riadok1 = (Riadok)časť1;

										if (riadok1.riadok.
											equalsIgnoreCase("inak")) ++i;
									}
								}
							}
							else if (telo instanceof Blok)
							{
								// Lokálne premenné bloku alebo obzor.
								if (null == ((Blok)telo).premennéBloku)
									((Blok)telo).premennéBloku =
										PremennéSkriptu.novyPriestor();

								lokálnePremenné.push(
									((Blok)telo).premennéBloku);
								try
								{
									// Blok opakovania.
									if (0 > opakovania)
									{
										for (int j = opakovania;
											neprerušené && j <= -1; ++j)
										{
											if (null != premenná)
											{
												zapíšPremennú(premenná,
													new Double(j));
												vypíšPremennú(riadok.číslo,
													premenná, ČÍSELNÁ_PREMENNÁ);
											}

											int kód = telo.vykonaj();
											if (hľadajMenovku(kód))
											{
												i = koreň.skočNa;
												koreň.skočNa = null;
												continue;
											}
											if (0 != kód) return kód;
										}
									}
									else
									{
										for (int j = 1; neprerušené &&
											j <= opakovania; ++j)
										{
											if (null != premenná)
											{
												zapíšPremennú(premenná,
													new Double(j));
												vypíšPremennú(riadok.číslo,
													premenná, ČÍSELNÁ_PREMENNÁ);
											}

											int kód = telo.vykonaj();
											if (hľadajMenovku(kód))
											{
												i = koreň.skočNa;
												koreň.skočNa = null;
												continue;
											}
											if (0 != kód) return kód;
										}
									}
								}
								finally
								{
									lokálnePremenné.pop();
								}
							}
							else if (telo instanceof Riadok)
							{
								Riadok riadok1 = (Riadok)telo;

								// Riadok opakovania…
								if (0 > opakovania)
								{
									for (int j = opakovania;
										neprerušené && j <= -1; ++j)
									{
										if (ladenie)
										{
											if (null != ObsluhaUdalostí.
												počúvadlo)
											{
												if (ObsluhaUdalostí.
													počúvadlo.ladenie(
													riadok1.číslo,
													riadok1.riadok,
													VYPÍSAŤ_SKRIPT))
												{
													koreň.prekresli(
														riadok1.číslo);
													plátno.vypíšRiadok();
													vyprázdniZásobníkPremenných();
												}
												else if (ObsluhaUdalostí.
													počúvadlo.ladenie(
													riadok1.číslo,
													riadok1.riadok,
													VYPÍSAŤ_RIADOK))
												{
													vyprázdniZásobníkPremenných();
													riadok1.vypíš();
												}
												else
													vyprázdniZásobníkPremenných();
											}

											if (prerušiťLadenie(riadok1.číslo,
												riadok1.riadok)) break;
										}

										if (null != premenná)
										{
											zapíšPremennú(premenná,
												new Double(j));
											vypíšPremennú(riadok.číslo,
												premenná, ČÍSELNÁ_PREMENNÁ);
										}

										int kód = telo.vykonaj();
										if (hľadajMenovku(kód))
										{
											i = koreň.skočNa;
											koreň.skočNa = null;
											continue;
										}
										if (0 != kód) return kód;
									}
								}
								else
								{
									for (int j = 1; neprerušené &&
										j <= opakovania; ++j)
									{
										if (ladenie)
										{
											if (null != ObsluhaUdalostí.
												počúvadlo)
											{
												if (ObsluhaUdalostí.
													počúvadlo.ladenie(
													riadok1.číslo,
													riadok1.riadok,
													VYPÍSAŤ_SKRIPT))
												{
													koreň.prekresli(
														riadok1.číslo);
													plátno.vypíšRiadok();
													vyprázdniZásobníkPremenných();
												}
												else if (ObsluhaUdalostí.
													počúvadlo.ladenie(
													riadok1.číslo,
													riadok1.riadok,
													VYPÍSAŤ_RIADOK))
												{
													vyprázdniZásobníkPremenných();
													riadok1.vypíš();
												}
												else
													vyprázdniZásobníkPremenných();
											}

											if (prerušiťLadenie(riadok1.číslo,
												riadok1.riadok)) break;
										}

										if (null != premenná)
										{
											zapíšPremennú(premenná,
												new Double(j));
											vypíšPremennú(riadok.číslo,
												premenná, ČÍSELNÁ_PREMENNÁ);
										}

										int kód = telo.vykonaj();
										if (hľadajMenovku(kód))
										{
											i = koreň.skočNa;
											koreň.skočNa = null;
											continue;
										}
										if (0 != kód) return kód;
									}
								}
							}
							else
							{
								// Poistka – ak by to nebol (z hocijakého
								// dôvodu) ani riadok, ani blok opakovania…
								if (0 > opakovania)
								{
									for (int j = opakovania;
										neprerušené && j <= -1; ++j)
									{
										if (null != premenná)
										{
											zapíšPremennú(premenná,
												new Double(j));
											vypíšPremennú(riadok.číslo,
												premenná, ČÍSELNÁ_PREMENNÁ);
										}

										int kód = telo.vykonaj();
										if (hľadajMenovku(kód))
										{
											i = koreň.skočNa;
											koreň.skočNa = null;
											continue;
										}
										if (0 != kód) return kód;
									}
								}
								else
								{
									for (int j = 1; neprerušené &&
										j <= opakovania; ++j)
									{
										if (null != premenná)
										{
											zapíšPremennú(premenná,
												new Double(j));
											vypíšPremennú(riadok.číslo,
												premenná, ČÍSELNÁ_PREMENNÁ);
										}

										int kód = telo.vykonaj();
										if (hľadajMenovku(kód))
										{
											i = koreň.skočNa;
											koreň.skočNa = null;
											continue;
										}
										if (0 != kód) return kód;
									}
								}
							}

							continue;
						}
						else if (riadok.riadok.regionMatches(
								true, 0, "ak ", 0, 3) ||
							riadok.riadok.regionMatches(
								true, 0, "dokedy ", 0, 7))
						{ // i
							String[] slová = vykonajPríkazTokenizer.
								split(riadok.riadok);

							if (slová[0].equalsIgnoreCase("ak"))
							{
								int mriežka = riadok.riadok.indexOf('#');

								if (-1 != mriežka)
								{
									if (výraz.attachString(riadok.riadok.
										substring(mriežka + 1)))
									{
										if (výraz.parse())
										{
											ExpressionProcessor.Value
												výsledok = výraz.getValue();

											if (výsledok.isError())
											{
												poslednáChyba = 51 + výsledok.
													getError().ordinal() +
													100 * riadok.číslo;
												return riadok.číslo;
											}

											slová = vykonajPríkazTokenizer.
												split(riadok.riadok.substring(
													0, mriežka) + " " +
													výsledok.toString());
										}
										else
										{
											poslednáChyba = 51 + výraz.
												getValue().getError().
												ordinal() + 100 * riadok.číslo;
											return riadok.číslo;
										}
									}
									else
									{
										poslednáChyba = 50 + 100 * riadok.číslo;
										return riadok.číslo;
									}
								}
							}

							Integer skoč1 = null, skoč2 = null;

							if (slová.length < 3)
							{
								if (slová.length >= 2)
								{
									if (slová[0].equalsIgnoreCase("ak"))
									{
										skoč1 = i + 1;

										if (skoč1 + 1 < obsah.size())
										{
											Skript časť1 = obsah.get(skoč1 + 1);
											if (časť1 instanceof Riadok)
											{
												Riadok riadok1 = (Riadok)časť1;

												if (riadok1.riadok.
													equalsIgnoreCase("inak"))
													skoč2 = skoč1 + 2;
											}
										}
									}
									else if (slová[0].equalsIgnoreCase(
										"dokedy") && i >= 1)
									{
										skoč1 = i - 1;

										if (i + 1 < obsah.size())
										{
											Skript časť1 = obsah.get(i + 1);

											if (časť1 instanceof Riadok)
											{
												Riadok riadok1 = (Riadok)časť1;

												if (riadok1.riadok.
													equalsIgnoreCase("inak"))
													skoč2 = i + 2;
											}
										}
									}
									// else „neznáma štruktúra (príkaz)“ –
									// 	overené nižšie:
									// 	if (null == skoč1) …
								}
								else
								{
									poslednáChyba = CHYBA_CHÝBAJÚCA_MENOVKA +
										100 * riadok.číslo;
									return riadok.číslo;
								}
							}
							else
							{
								skoč1 = menovky.get(slová[2]);
								if (null == skoč1)
								{
									koreň.hľadajMenovku = null;
									koreň.skočNa = null;
									poslednáChyba = CHYBA_NEZNÁMA_MENOVKA +
										100 * riadok.číslo;
									return riadok.číslo;
								}

								if (slová.length >= 5)
								{
									if (!slová[3].
										equalsIgnoreCase("inak"))
									{
										poslednáChyba = CHYBA_NEZNÁME_SLOVO +
											100 * riadok.číslo;
										return riadok.číslo;
									}
									skoč2 = menovky.get(slová[4]);
									if (null == skoč2)
									{
										koreň.hľadajMenovku = null;
										koreň.skočNa = null;
										poslednáChyba = CHYBA_NEZNÁMA_MENOVKA +
											100 * riadok.číslo;
										return riadok.číslo;
									}
								}
								else if (slová.length >= 4)
								{
									skoč2 = menovky.get(slová[3]);
									if (null == skoč2)
									{
										koreň.hľadajMenovku = null;
										koreň.skočNa = null;
										poslednáChyba = CHYBA_NEZNÁMA_MENOVKA +
											100 * riadok.číslo;
										return riadok.číslo;
									}
								}
								else skoč2 = null;
							}

							if (null == skoč1)
							{
								poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
									100 * riadok.číslo;
								return riadok.číslo;
							}

							Boolean podmienka;

							if (slová[0].equalsIgnoreCase("ak"))
							{
								podmienka =vyhodnoťLogickýVýraz(slová[1]);

								if (podmienka) i = skoč1 - 1;
								else if (null != skoč2) i = skoč2 - 1;
								else ++i;
								continue;
							}
							else if (slová[0].equalsIgnoreCase("dokedy"))
							{
								if (premennáJestvuje(slová[1], Double.class))
								{
									// Ak je výsledok výpočtu záporný, tak
									// sa hodnota premennej nastaví na nulu,
									// aby mohla byť podmienka v nasledujúcom
									// kroku (vyhodnoťLogickýVýraz)
									// vyhodnotená ako nepravdivá.
									// 
									// Zároveň, ak je hodnota premennej ešte
									// pred výpočtom menšia alebo rovná nule,
									// tak sa vezme do úvahy prípadná hodnota
									// skoku „inak,“ inak sa ignoruje.
									// 
									// (Totiž, nulová hodnota pred výpočtom
									// naznačuje, že cyklus by sa nemal ani
									// raz zopakovať. To by malo dávať právo
									// na vykonanie toho, čo nasleduje za
									// „inak.“)

									Double hodnota = (Double)čítajPremennú(
										slová[1], Double.class);
									if (0.0 < hodnota) skoč2 = null;

									zapíšPremennú(slová[1], new Double(Math.
										max(Math.rint(hodnota - 1.0), 0.0)));

									vypíšPremennú(riadok.číslo,
										slová[1], ČÍSELNÁ_PREMENNÁ);

									podmienka = vyhodnoťLogickýVýraz(slová[1]);
									if (podmienka) i = skoč1 - 1;
									else if (null != skoč2) i = skoč2 - 1;
									continue;
								}
							}

							poslednáChyba = CHYBA_CHYBNÁ_ŠTRUKTÚRA +
								100 * riadok.číslo;
							return riadok.číslo;
						} // i

						int kód = časť.vykonaj();
						if (hľadajMenovku(kód))
						{
							i = koreň.skočNa;
							koreň.skočNa = null;
							continue;
						}
						if (0 != kód) return kód;
					}
					else if (časť instanceof Blok)
					{
						// Lokálne premenné bloku alebo obzor.
						if (null == ((Blok)časť).premennéBloku)
							((Blok)časť).premennéBloku =
								PremennéSkriptu.novyPriestor();

						lokálnePremenné.push(((Blok)časť).premennéBloku);
						try
						{
							int kód = časť.vykonaj();
							if (hľadajMenovku(kód))
							{
								i = koreň.skočNa;
								koreň.skočNa = null;
								continue;
							}
							if (0 != kód) return kód;
						}
						finally
						{
							lokálnePremenné.pop();
						}
					}
					else
					{
						// Iný typ ako Blok alebo Riadok by sa v podstate
						// nemal vyskytnúť, ale ak by sa to z ľubovoľného
						// dôvodu stalo, toto zabezpečí jeho vykonanie:
						int kód = časť.vykonaj();
						if (hľadajMenovku(kód))
						{
							i = koreň.skočNa;
							koreň.skočNa = null;
							continue;
						}
						if (0 != kód) return kód;
					}
				}

				return 0;
			}
			finally
			{
				koreň.aktuálnaInštancia = koreň.zásobníkInštancií.pop();
			}
		}

		/**
		 * <p>Vypíše obsah tejto inštancie na aktuálnu konzolu.
		 * (Predvolene na {@linkplain GRobot#strop strop}. Pozri aj
		 * {@link Skript#presmerujNaPodlahu() presmerujNaPodlahu}.)</p>
		 */
		public void vypíš()
		{
			int i = 0;
			for (Skript časť : obsah)
			{
				for (Map.Entry<String, Integer> menovka :
					menovky.entrySet())
				{
					if (i == menovka.getValue())
					{
						vypíšRiadokLadenia(
							farbyLadenia[FARBA_LADENIA_SYMBOL],
							opakujZnak(' ', 4 + 2 * odsadenie), ':',
							farbyLadenia[FARBA_LADENIA_MENOVKA],
							menovka.getKey());
					}
				}

				if (časť instanceof Blok)
				{
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_ZAČIATOK_BLOKU],
						opakujZnak(' ', 4 + (2 * odsadenie)),
						"«začiatok bloku»");

					časť.vypíš();

					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_KONIEC_BLOKU],
						opakujZnak(' ', 4 + (2 * odsadenie)),
						"«koniec bloku»");
				}
				else
					časť.vypíš();
				++i;
			}

			for (Map.Entry<String, Integer> menovka :
				menovky.entrySet())
			{
				if (i == menovka.getValue())
				{
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_SYMBOL],
						opakujZnak(' ', 4 + 2 * odsadenie), ':',
						farbyLadenia[FARBA_LADENIA_MENOVKA],
						menovka.getKey());
				}
			}
		}
	}

	// Súkromná trieda uchovávajúca riadok skriptu.
	private static class Riadok extends Skript
	{
		// Inštancia rodičovského bloku (ktorá si zase pamätá koreň).
		final Blok rodič;

		// Číslo tohto riadka — poradové číslo zo súboru, poľa alebo zoznamu…
		// Hlavne na účely ladiacich výpisov a identifikácie chýb.
		final int číslo;

		// Obsah riadka.
		final String riadok;

		// Konštruktor. Prijíma rodiča, číslo riadka a obsah riadka.
		Riadok(Blok rodič, int číslo, String riadok)
		{
			this.rodič = rodič;
			this.číslo = číslo;
			this.riadok = riadok;
		}

		/**
		 * <p>Zabezpečí vykonanie tej súčasti skriptu, za ktorú je zodpovedná
		 * táto inštancia.</p>
		 */
		public int vykonaj()
		{
			int početÚspechov = vykonajPríkaz(
				this.riadok, rodič.koreň.aktuálnaInštancia);

			if (0 == početÚspechov)
			{
				if (ŽIADNA_CHYBA == poslednáChyba)
					poslednáChyba = CHYBA_NEZNÁMY_PRÍKAZ + 100 * číslo;
				return číslo;
			}

			return 0;
		}

		/**
		 * <p>Vypíše obsah tejto inštancie na aktuálnu konzolu.
		 * (Predvolene na {@linkplain GRobot#strop strop}. Pozri aj
		 * {@link Skript#presmerujNaPodlahu() presmerujNaPodlahu}.)</p>
		 */
		public void vypíš()
		{
			boolean obnovFarbuPozadia = false;
			Farba farbaPozadiaTextu = null;

			try {

			if (číslo == rodič.koreň.aktívnyRiadok)
			{
				obnovFarbuPozadia = true;
				farbaPozadiaTextu = plátno.farbaPozadiaTextu();
				plátno.farbaPozadiaTextu(farbyLadenia[
					FARBA_LADENIA_AKTÍVNY_RIADOK]);
			}

			vypíšÚdajLadenia(
				farbyLadenia[FARBA_LADENIA_ČÍSLO_RIADKA],
				String.format(Locale.ENGLISH, "%3d", číslo),
				opakujZnak(' ', 1 + 2 * rodič.odsadenie));

			if (začniVýpisRiadkuLadenia(this.riadok))
			{
				// nič
			}
			else if (this.riadok.regionMatches(true, 0, "na ", 0, 3))
			{
				String[] slová = vykonajPríkazTokenizer.split(this.riadok);

				vypíšRiadokLadenia(
					farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
					slová[0], farbyLadenia[FARBA_LADENIA_MENOVKA],
					' ', slová[1]);
			}
			else if (this.riadok.equalsIgnoreCase("inak"))
			{
				vypíšRiadokLadenia(
					farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
					this.riadok);
			}
			else if (this.riadok.regionMatches(true, 0, "opakuj ", 0, 7))
			{
				String[] slová = vykonajPríkazTokenizer.split(this.riadok);

				vypíšÚdajLadenia(
					farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
					slová[0], ' ');

				if (slová.length >= 3)
				{
					vypíšÚdajLadenia(
						farbyLadenia[
							FARBA_LADENIA_NÁZOV_PREMENNEJ],
						slová[1], ' ', farbyLadenia[
							FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ],
						slová[2]);
				}
				else
				{
					vypíšÚdajLadenia(farbyLadenia[
						FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ], slová[1]);
				}

				plátno.vypíšRiadok();
			}
			else if (this.riadok.regionMatches(true, 0, "ak ", 0, 3) ||
				this.riadok.regionMatches(true, 0, "dokedy ", 0, 7))
			{
				String[] slová = vykonajPríkazTokenizer.split(this.riadok);

				int mriežka = this.riadok.indexOf('#');

				if (slová[0].equalsIgnoreCase("ak") && -1 != mriežka)
				{
					String textVýrazu = this.riadok.substring(mriežka + 1);
					slová = vykonajPríkazTokenizer.split(this.riadok.
						substring(0, mriežka));

					vypíšÚdajLadenia(farbyLadenia[
						FARBA_LADENIA_RIADIACI_PRÍKAZ], slová[0]);

					for (int i = 1; i < slová.length; ++i)
						vypíšÚdajLadenia(farbyLadenia[
							FARBA_LADENIA_CHYBA], ' ', slová[i]);

					vypíšÚdajLadenia(farbyLadenia[
						FARBA_LADENIA_ZLOŽENÝ_VÝRAZ], " #", textVýrazu);
				}
				else if (slová.length < 2)
				{
					vypíšÚdajLadenia(
						farbyLadenia[FARBA_LADENIA_CHYBA],
						slová[0]);
				}
				else
				{
					vypíšÚdajLadenia(farbyLadenia
						[FARBA_LADENIA_RIADIACI_PRÍKAZ],
						slová[0], ' ');

					if (slová[0].equalsIgnoreCase("dokedy"))
					{
						vypíšÚdajLadenia(farbyLadenia
							[FARBA_LADENIA_NÁZOV_PREMENNEJ],
							slová[1]);
					}
					else
					{
						vypíšÚdajLadenia(farbyLadenia
							[FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ], slová[1]);
					}

					if (slová.length >= 3)
					{
						if (null == rodič.menovky.get(slová[2]))
						{
							vypíšÚdajLadenia(
								farbyLadenia[FARBA_LADENIA_CHYBA],
								' ', slová[2]);
						}
						else
						{
							vypíšÚdajLadenia(
								farbyLadenia[FARBA_LADENIA_MENOVKA],
								' ', slová[2]);
						}

						if (slová.length >= 5)
						{
							if (slová[3].equalsIgnoreCase("inak"))
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_RIADIACI_PRÍKAZ],
									' ', slová[3]);
							}
							else
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_CHYBA], ' ',
									slová[3]);
							}

							if (null == rodič.menovky.get(slová[4]))
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_CHYBA], ' ',
									slová[4]);
							}
							else
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_MENOVKA],
									' ', slová[4]);
							}
						}
						else if (slová.length >= 4)
						{
							if (null == rodič.menovky.get(slová[3]))
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_CHYBA], ' ',
									slová[3]);
							}
							else
							{
								vypíšÚdajLadenia(farbyLadenia
									[FARBA_LADENIA_MENOVKA],
									' ', slová[3]);
							}
						}
					}
				}

				plátno.vypíšRiadok();
			}
			else if (this.riadok.regionMatches(true, 0, "obzor ", 0, 6))
			{
				String[] slová = vykonajPríkazTokenizer.split(this.riadok);

				if (slová.length > 1)
				{
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
						slová[0],' ',
						farbyLadenia[FARBA_LADENIA_NÁZOV_PREMENNEJ],
						slová[1]);
				}
				else
				{
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_CHYBA],
						slová[0], " ?");
				}
			}
			else dokončiVýpisRiadkuLadenia(this.riadok);

			} finally {
				if (obnovFarbuPozadia)
					plátno.farbaPozadiaTextu(farbaPozadiaTextu);
			}
		}
	}

		// Pomocná metóda na výpis tých častí farebnej syntaxe, ktorá je
		// spoločná pre príkazový riadok aj skripty.
		/*packagePrivate*/ static boolean začniVýpisRiadkuLadenia(String riadok)
		{
			if ('@' == riadok.charAt(0))
			{
				vypíšRiadokLadenia(
					farbyLadenia[FARBA_LADENIA_SYMBOL], '@',
					farbyLadenia[FARBA_LADENIA_MENO_INŠTANCIE],
					riadok.substring(1));
				return true;
			}
			return false;
		}

		// Pomocná metóda na výpis tých častí farebnej syntaxe, ktorá je
		// spoločná pre príkazový riadok aj skripty.
		/*packagePrivate*/ static void dokončiVýpisRiadkuLadenia(String riadok)
		{
			if (riadok.regionMatches(
				true, 0, "nech ", 0, 5))
			{
				String reťazec, textVýrazu = null, príkaz = riadok;
				int prvéÚvodzovky = príkaz.indexOf('"');
				int mriežka = príkaz.indexOf('#');
				if (-1 != prvéÚvodzovky && (-1 == mriežka ||
					mriežka >= prvéÚvodzovky))
				{
					int poslednéÚvodzovky = príkaz.lastIndexOf('"');
					if (-1 != poslednéÚvodzovky &&
						prvéÚvodzovky != poslednéÚvodzovky)
					{
						reťazec = príkaz.substring(prvéÚvodzovky + 1,
							poslednéÚvodzovky);
						príkaz = príkaz.substring(0, prvéÚvodzovky) +
							príkaz.substring(poslednéÚvodzovky + 1);
					}
					else
					{
						reťazec = príkaz.substring(prvéÚvodzovky + 1);
						príkaz = príkaz.substring(0, prvéÚvodzovky);
					}
					mriežka = príkaz.indexOf('#');
				}
				else reťazec = null;

				if (-1 != mriežka)
				{
					textVýrazu = príkaz.substring(mriežka + 1);
					príkaz = príkaz.substring(0, mriežka);
				}

				String[] slová = vykonajPríkazTokenizer.split(príkaz);

				if (slová.length > 3)
				{
					vypíšÚdajLadenia(
						farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
						slová[0],' ',
						farbyLadenia[FARBA_LADENIA_NÁZOV_PREMENNEJ],
						slová[1], ' ',
						farbyLadenia[FARBA_LADENIA_SYMBOL],
						slová[2], ' ',
						farbyLadenia[FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ],
						slová[3]);

					for (int i = 4; i < slová.length; ++i)
						vypíšÚdajLadenia(farbyLadenia[
							FARBA_LADENIA_JEDNODUCHÝ_VÝRAZ], ' ', slová[i]);

					if (null != reťazec)
						vypíšÚdajLadenia(
							farbyLadenia[FARBA_LADENIA_REŤAZEC],
								" \"", reťazec, '"');

					if (null != textVýrazu)
						vypíšÚdajLadenia(
							farbyLadenia[FARBA_LADENIA_ZLOŽENÝ_VÝRAZ],
								" #", textVýrazu);

					plátno.vypíšRiadok();
				}
				else if (slová.length > 2)
				{
					if (null != reťazec || null != textVýrazu)
					{
						vypíšÚdajLadenia(
							farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
							slová[0],' ',
							farbyLadenia[FARBA_LADENIA_NÁZOV_PREMENNEJ],
							slová[1], ' ',
							farbyLadenia[FARBA_LADENIA_SYMBOL],
							slová[2]);

						if (null != reťazec)
							vypíšÚdajLadenia(
								farbyLadenia[FARBA_LADENIA_REŤAZEC],
									" \"", reťazec, '"');

						if (null != textVýrazu)
							vypíšÚdajLadenia(
								farbyLadenia[FARBA_LADENIA_ZLOŽENÝ_VÝRAZ],
									" #", textVýrazu);

						plátno.vypíšRiadok();
					}
					else if (slová[2].equals("_") || slová[2].equals("!") ||
						slová[2].equals("~"))
						vypíšRiadokLadenia(
							farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
							slová[0],' ',
							farbyLadenia[FARBA_LADENIA_NÁZOV_PREMENNEJ],
							slová[1], ' ',
							farbyLadenia[FARBA_LADENIA_SYMBOL],
							slová[2]);
					else
						vypíšRiadokLadenia(
							farbyLadenia[FARBA_LADENIA_CHYBA],
							slová[0], " ?");
				}
				else
				{
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_CHYBA],
						slová[0], " ?");
				}
			}
			else
			{
				// TODO dokonči výpis výrazu #

				int prvéÚvodzovky = riadok.indexOf('"');
				if (-1 != prvéÚvodzovky)
				{
					String reťazec, prefix, postfix;
					int poslednéÚvodzovky = riadok.
						lastIndexOf('"');

					if (-1 != poslednéÚvodzovky &&
						prvéÚvodzovky != poslednéÚvodzovky)
					{
						reťazec = riadok.substring(
							prvéÚvodzovky + 1, poslednéÚvodzovky);
						prefix = riadok.substring(0,
							prvéÚvodzovky);
						postfix = riadok.substring(
							poslednéÚvodzovky + 1);
					}
					else
					{
						reťazec = riadok.
							substring(prvéÚvodzovky + 1);
						prefix = riadok.
							substring(0, prvéÚvodzovky);
						postfix = "";
					}

					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_PRÍKAZ],
						prefix, farbyLadenia[FARBA_LADENIA_REŤAZEC],
						'"', reťazec, '"',
						farbyLadenia[FARBA_LADENIA_PRÍKAZ],
						postfix);
				}
				else
					vypíšRiadokLadenia(
						farbyLadenia[FARBA_LADENIA_PRÍKAZ],
						riadok);
			}
		}


	/**
	 * <p>Táto trieda uchováva a pracuje s mapami premenných skriptov. Jedna
	 * inštancia reprezentuje unikátny priestor premenných, v ktorom môžu byť
	 * uchované premenné, ktoré sú lokálne, globálne, obzorové, prípadne iné.
	 * Vždy je definovaný jeden hlavný (globálny) priestor premenných,
	 * s ktorým sa pracuje napríklad vtedy, keď prehľadávanie v ostatných
	 * aktívnych priesoroch zlyhá (pri čítaní premenných; pričom nie je
	 * zaručené, že premenná je definovaná v hlavnom priestore – ide len
	 * o stanovenie hierarchie prehľadávania).</p>
	 */
	public static class PremennéSkriptu
	{
		// Mapy (zväčša lokálnych) premenných (pričom jedna inštancia tejto
		// triedy je rezervovaná na globálne premenné).
		/*packagePrivate*/ final TreeMap<String, Double> premenné;
		/*packagePrivate*/ final TreeMap<String, Color>  premennéFarby;
		/*packagePrivate*/ final TreeMap<String, Poloha> premennéPolohy;
		/*packagePrivate*/ final TreeMap<String, String> premennéReťazce;

		// Konštrukor.
		private PremennéSkriptu(
			TreeMap<String, Double> premenné,
			TreeMap<String, Color>  premennéFarby,
			TreeMap<String, Poloha> premennéPolohy,
			TreeMap<String, String> premennéReťazce)
		{
			this.premenné        = premenné;
			this.premennéFarby   = premennéFarby;
			this.premennéPolohy  = premennéPolohy;
			this.premennéReťazce = premennéReťazce;
		}

		/**
		 * <p>Vytvorí zoznam premenných, ktoré sú definované v tomto
		 * priestore premenných skriptu so zadaným údajovým typom. Ak
		 * je zadaný údajový typ neplatný (nepovolený, resp.
		 * nepodporovaný), tak je namiesto zoznamu vrátená hodnota
		 * {@code valnull}. Ak je vrátený prázdny zoznam reťazcov,
		 * tak to znamená, že v tomto priestore premenných nie je
		 * definovaná žiadna premenná so zadaným údajovým typom.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Názvy premenných nie sú
		 * citlivé na veľkosť písmen. Ich zoznamy sú konštruované
		 * pomocou parametra {@link String String}<code>.</code>{@link 
		 * String#CASE_INSENSITIVE_ORDER}.</p>
		 * 
		 * @param typ typ premenných zoznamu – povolené sú len:
		 *     {@link Double Double.class}, {@link Color Color.class},
		 *     {@link Poloha Poloha.class} a {@link String String.class}
		 * @return zoznam definovaných premenných podľa zadaného
		 *     údajového typu alebo hodnota {@code valnull}
		 */
		public Zoznam<String> zoznam(Class<?> typ)
		{
			if (typ == Double.class)
			{
				Zoznam <String> zoznam = new Zoznam<>();
				for (Map.Entry<String, Double> premenná :
					premenné.entrySet()) zoznam.add(premenná.getKey());
				return zoznam;
			}
			else if (typ == Color.class)
			{
				Zoznam <String> zoznam = new Zoznam<>();
				for (Map.Entry<String, Color> premenná :
					premennéFarby.entrySet()) zoznam.add(
						premenná.getKey());
				return zoznam;
			}
			else if (typ == Poloha.class)
			{
				Zoznam <String> zoznam = new Zoznam<>();
				for (Map.Entry<String, Poloha> premenná :
					premennéPolohy.entrySet()) zoznam.add(
						premenná.getKey());
				return zoznam;
			}
			else if (typ == String.class)
			{
				Zoznam <String> zoznam = new Zoznam<>();
				for (Map.Entry<String, String> premenná :
					premennéReťazce.entrySet()) zoznam.add(
						premenná.getKey());
				return zoznam;
			}

			return null;
		}

		/**
		 * <p>Zistí, či je premenná so zadaným názvom a typom
		 * definovaná v tomto priestore premenných skriptu.</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double
		 *     Double.class}, {@link Color Color.class}, {@link Poloha
		 *     Poloha.class} alebo {@link String String.class}
		 * @return ak premenná zadaného údajového typu jestvuje (to
		 *     jest: premenná je definovaná), tak je návratovou
		 *     hodnotou tejto metódy hodnota {@code valtrue};
		 *     ak premenná nejestvuje alebo bol zadaný nepovolený
		 *     údajový typ premennej, tak je návratovou hodnotou
		 *     tejto metódy hodnota {@code valfalse}
		 * 
		 * @see Skript#premennáJestvuje(String, Class)
		 */
		public boolean jestvuje(String názov, Class<?> typ)
		{
			if (null == názov || názov.isEmpty()) return false;

			if (typ == Double.class)
				return premenné.containsKey(názov);
			else if (typ == Color.class)
				return premennéFarby.containsKey(názov);
			else if (typ == Poloha.class)
				return premennéPolohy.containsKey(názov);
			else if (typ == String.class)
				return premennéReťazce.containsKey(názov);

			return false;
		}

		/** <p><a class="alias"></a> Alias pre {@link #jestvuje(String, Class) jestvuje}.</p> */
		public boolean existuje(String názov, Class<?> typ)
		{ return jestvuje(názov, typ); }

		/**
		 * <p>Zistí hodnotu premennej zadaného údajového typu, ktorá
		 * by mala byť definovaná v tomto priestore premenných
		 * skriptu. Ak taká premenná nie je definovaná, tak bude
		 * návratová hodnota tejto metódy rovná hodnote
		 * {@code valnull}.</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double
		 *     Double.class}, {@link Color Color.class},
		 *     {@link Poloha Poloha.class} alebo {@link String
		 *     String.class}
		 * @return ak premenná jestvuje, tak je návratovou hodnotou
		 *     tejto metódy hodnota tejto premennej; ak premenná
		 *     nejestvuje, prípadne bol zadaný nepovolený údajový
		 *     typ premennej, tak je návratovou hodnotou tejto
		 *     metódy hodnota {@code valnull}
		 * 
		 * @see Skript#čítajPremennú(String, Class)
		 */
		public Object čítaj(String názov, Class<?> typ)
		{
			if (null == názov || názov.isEmpty()) return null;

			if (typ == Double.class)
				return premenné.get(názov);
			else if (typ == Color.class)
				return premennéFarby.get(názov);
			else if (typ == Poloha.class)
				return premennéPolohy.get(názov);
			else if (typ == String.class)
				return premennéReťazce.get(názov);

			return null;
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítaj(String, Class) čítaj}.</p> */
		public Object citaj(String názov, Class<?> typ)
		{ return čítaj(názov, typ); }

		/**
		 * <p>Nastaví novú hodnotu premennej v tomto priestore
		 * premenných skriptu. Ak je zadaná hodnota nepovoleného
		 * údajového typu, tak nebude nastavená hodnota žiadnej
		 * premennej. To isté platí pri pokuse o zapísanie hodnoty
		 * {@code valnull} alebo pri pokuse o nastavenie premennej
		 * s prázdnym menom. Úspešnosť (resp. neúspešnosť) nastavenia
		 * hodnoty premennej potvrdzuje návratová hodnota tejto
		 * metódy – {@code valtrue} (úspech) / {@code valfalse}
		 * (neúspech).</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Táto metóda
		 * automaticky definuje nejestvujúce premenné.</p>
		 * 
		 * @param názov názov premennej
		 * @param hodnota hodnota premennej povoleného údajového typu –
		 *     povolené sú: {@link Double Double},
		 *     {@link Color Color}, {@link Poloha Poloha} alebo
		 *     {@link String String}
		 * @return kontrolná návratová hodnota (ide najmä o overenie
		 *     toho, či bola naozaj zadaná hodnota niektorého
		 *     z povolených údajových typov); ak je návratová hodnota
		 *     {@code valtrue}, tak bola zadaná hodnota zapísaná do
		 *     premennej prislúchajúceho údajového typu, v opačnom
		 *     prípade je návratová hodnota rovná {@code valfalse}
		 * 
		 * @see Skript#zapíšPremennú(String, Object)
		 */
		public boolean zapíš(String názov, Object hodnota)
		{
			if (null == hodnota || null == názov ||
				názov.isEmpty()) return false;
			if (hodnota instanceof Double)
				premenné.put(názov, (Double)hodnota);
			else if (hodnota instanceof Color)
				premennéFarby.put(názov, (Color)hodnota);
			else if (hodnota instanceof Poloha)
				premennéPolohy.put(názov, (Poloha)hodnota);
			else if (hodnota instanceof String)
				premennéReťazce.put(názov, (String)hodnota);
			else return false;
			return true;
		}

		/** <p><a class="alias"></a> Alias pre {@link #zapíš(String, Object) zapíš}.</p> */
		public void zapis(String názov, Object hodnota)
		{ zapíš(názov, hodnota); }

		/**
		 * <p>Vymaže definíciu premennej zadaného údajového typu, ak
		 * je definovaná v tomto priestore premenných skriptu.</p>
		 * 
		 * @param názov názov premennej
		 * @param typ typ premennej – povolené sú: {@link Double
		 *     Double.class}, {@link Color Color.class},
		 *     {@link Poloha Poloha.class} alebo {@link String
		 *     String.class}
		 * 
		 * @see Skript#vymažPremennú(String, Class)
		 */
		public void vymaž(String názov, Class<?> typ)
		{
			if (null == názov || názov.isEmpty()) return;

			if (typ == Double.class)
				premenné.remove(názov);
			else if (typ == Color.class)
				premennéFarby.remove(názov);
			else if (typ == Poloha.class)
				premennéPolohy.remove(názov);
			else if (typ == String.class)
				premennéReťazce.remove(názov);
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž(String, Class) vymaž}.</p> */
		public void vymaz(String názov, Class<?> typ)
		{ vymaž(názov, typ); }

		/**
		 * <p>Odstráni všetky premenné v tomto priestore premenných skriptov.
		 * Po vykonaní tejto metódy bude tento priestor premenných skriptov
		 * prázdny.</p>
		 */
		public void vymaž()
		{
			premenné.clear();
			premennéFarby.clear();
			premennéPolohy.clear();
			premennéReťazce.clear();
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public void vymaz() { vymaž(); }

		/**
		 * <p>Vytvorí nový priestor premenných skriptov. Interpreter skriptov
		 * túto metódu využíva na vytvorenie nového priestoru pri každom
		 * vstupe (vnorení) do vnoreného bloku.</p>
		 * 
		 * @return nový priestor premenných skriptov
		 */
		public static PremenneSkriptu novýPriestor()
		{
			return new PremenneSkriptu(
				new TreeMap<String, Double>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, Color>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, Poloha>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, String>(
					String.CASE_INSENSITIVE_ORDER));
		}

		/** <p><a class="alias"></a> Alias pre {@link #novýPriestor() novýPriestor}.</p> */
		public static PremenneSkriptu novyPriestor()
		{
			return new PremenneSkriptu(
				new TreeMap<String, Double>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, Color>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, Poloha>(
					String.CASE_INSENSITIVE_ORDER),
				new TreeMap<String, String>(
					String.CASE_INSENSITIVE_ORDER));
		}

		/**
		 * <p>Zistí, či je definovaný pomenovaný priestor skriptov zvaný
		 * „obzor“ (pozri aj {@link #dajObzor(String) dajObzor}). Metóda
		 * vráti {@code valtrue} len vtedy, ak priestor jestvuje. Pozor,
		 * pri zadaní prázdneho názvu obzoru vráti metóda vždy hodnotu
		 * {@code valtrue}, pretože prázdne meno má význam globálneho
		 * priestoru premenných, ktorý jestvuje vždy.</p>
		 * 
		 * @param názovObzoru identifikátor (meno) menného priestoru
		 *     premenných skriptov
		 * @return {@code valtrue}, ak priestor so zadaným menom jestvuje
		 * 
		 * @see #dajObzor(String)
		 * @see PremennéSkriptu
		 */
		public static boolean obzorJestvuje(String názovObzoru)
		{
			// Globálne premenné:
			if (null == názovObzoru || názovObzoru.isEmpty()) return true;
			return obzory.containsKey(názovObzoru);
		}

		/** <p><a class="alias"></a> Alias pre {@link #obzorJestvuje(String) obzorJestvuje}.</p> */
		public static boolean obzorExistuje(String názovObzoru)
		{
			return obzorJestvuje(názovObzoru);
		}

		/**
		 * <p>Vráti pomenovaný priestor premenných skriptov, ktorý je
		 * v tomto programovacom rámci označovaný termínom „obzor.“ Ak
		 * priestor nejestvuje, tak je automaticky vytvorený. Špeciálny
		 * prípad nastáva pri zadaní prázdneho názvu obzoru. Vtedy metóda
		 * vráti globálny priestor premenných…</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Globálny priestor premenných
		 * získame aj volaním metódy {@link Skript Skript}{@code .}{@link 
		 * Skript#globálnePremenné() globálnePremenné}.</p>
		 * 
		 * @param názovObzoru identifikátor (meno) menného priestoru
		 *     premenných skriptov
		 * @return inštancia triedy premenných skriptov (pomenovaný
		 *     priestor premenných skriptov – obzor)
		 * 
		 * @see #obzorJestvuje(String)
		 * @see PremennéSkriptu
		 */
		public static PremenneSkriptu dajObzor(String názovObzoru)
		{
			if (null == názovObzoru || názovObzoru.isEmpty())
				return globalnePremenne;

			PremenneSkriptu obzor = obzory.get(názovObzoru);
			if (null == obzor)
			{
				obzor = novyPriestor(); // ‼Pozor‼ táto metóda pracuje
					// s aliasom triedy bez diakritiky.
				obzory.put(názovObzoru, obzor);
			}

			return obzor;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link PremennéSkriptu PremennéSkriptu}.</p> */
	public static class PremenneSkriptu extends PremennéSkriptu
	{
		// Konštruktor.
		private PremenneSkriptu(
			TreeMap<String, Double> premenné,
			TreeMap<String, Color>  premennéFarby,
			TreeMap<String, Poloha> premennéPolohy,
			TreeMap<String, String> premennéReťazce)
		{ super(premenné, premennéFarby, premennéPolohy,
			premennéReťazce); }
	}


	/**
	 * <p>Táto metóda je implementovaná v každej odvodenej triede a jej
	 * účelom je zabezpečiť vykonanie skriptu. Vykonávanie paralelne
	 * zabezpečuje aj ladenie skriptu.</p>
	 * 
	 * @return číslo riadka, na ktorom vznikla chyba; ak chyba nevznikla,
	 *     tak návratovou hodnotou je nula = {@link Konštanty#ŽIADNA_CHYBA
	 *     ŽIADNA_CHYBA}
	 */
	abstract public int vykonaj();

	/**
	 * <p>Táto metóda je implementovaná v každej odvodenej triede a jej
	 * účelom je zabezpečiť vypísanie skriptu na aktuálnu vnútornú konzolu
	 * (predvolene na {@linkplain GRobot#strop strop}; pozri aj {@link 
	 * #presmerujNaPodlahu() presmerujNaPodlahu}). Výpis skriptu je
	 * využívaný počas ladenia.</p>
	 */
	abstract public void vypíš();

	/** <p><a class="alias"></a> Alias pre {@link #vypíš() vypíš}.</p> */
	public void vypis() { vypíš(); }


	/**
	 * <p>Táto metóda vyrobí inštanciu skriptu zo zadaného poľa riadkov.
	 * Túto metódu využívajú všetky statické metódy sveta, ktoré spúšťajú
	 * alebo vytvárajú skripty (zo súborov alebo iných údajových štruktúr),
	 * napríklad:</p>
	 * 
	 * <ul>
	 * <li>{@link Svet#vyrobSkript(String, boolean) vyrobSkript(skript,
	 * zoSúboru)},</li>
	 * <li>{@link Svet#vyrobSkript(String[]) vyrobSkript(skript)},</li>
	 * <li>{@link Svet#nahrajSkript(String, String) nahrajSkript(názov,
	 * súbor)},</li>
	 * <li>{@link Svet#vykonajSkript(String[]) vykonajSkript(riadky)}</li>
	 * <li>a podobne.</li>
	 * </ul>
	 * 
	 * @param skript pole riadkov skriptu, z ktorých bude vyrobená nová
	 *     inštancia {@linkplain Skript skriptu}
	 * @return inštancia {@linkplain Skript skriptu} alebo {@code valnull)}
	 *     (v takom prípade by metóda {@link #kódPoslednejChyby()
	 *     kódPoslednejChyby} mala vrátiť kód
	 *     {@link Konštanty#CHYBA_DVOJITÁ_MENOVKA CHYBA_DVOJITÁ_MENOVKA})
	 */
	public static Skript vyrob(String[] skript)
	{
		Skript novýSkript = new Koreň(skript); // resetuje poslednú chybu
		if (ŽIADNA_CHYBA != poslednáChyba) return null;
		return novýSkript;
	}


	/**
	 * <p>Zapne alebo vypne ladenie skriptov programovacieho rámca
	 * GRobot.</p>
	 * 
	 * <p>Ak je ladenie zapnuté, tak sú počas činnosti skriptov vypisované
	 * rôzne informácie o vykonávaní príkazov skriptu na vnútornú konzolu
	 * programovacieho rámca (predvolene na {@linkplain GRobot#strop strop};
	 * pozri aj {@link Skript#presmerujNaPodlahu() presmerujNaPodlahu}).
	 * Tiež je pravidelne spúšťaná reakcia obsluhy udalostí {@link 
	 * ObsluhaUdalostí#ladenie(int, String, int) ladenie} (ak je obsluha
	 * udalostí definovaná).</p>
	 * 
	 * @param zapniLadenie {@code valtrue} alebo {@code valfalse}
	 * 
	 * @see #ladenie()
	 * @see ObsluhaUdalostí#ladenie(int, String, int)
	 */
	public static void ladenie(boolean zapniLadenie)
	{ ladenie = zapniLadenie; }

	/**
	 * <p>Zistí, či je zapnuté ladenie skriptov programovacieho rámca
	 * GRobot.</p>
	 * 
	 * @return {@code valtrue} alebo {@code valfalse}
	 * 
	 * @see #ladenie(boolean)
	 */
	public static boolean ladenie() { return ladenie; }


	/**
	 * <p>Vráti globálny priestor premenných skriptov. Globálny priestor
	 * premenných je spoločný pre všetky skripty. Lokálne priestory
	 * premenných sú vytvárané a likvidované priebežne počas činnosti
	 * skriptov a preto k nim nie je možné získať prístup „zvonka.“ (Pozri
	 * opis metódy {@link #objemPriestorov() objemPriestorov}.)</p>
	 * 
	 * <p>Podrobnosti o spôsobe práce s priestorom premenných skriptov sú
	 * v dokumentácii triedy {@link PremennéSkriptu PremennéSkriptu}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ten istý výsledok dosiahneme
	 * volaním metódy {@link Skript Skript}{@code .}{@link PremennéSkriptu
	 * PremennéSkriptu}{@code .}{@link PremennéSkriptu#dajObzor(String)
	 * dajObzor}{@code (}{@code valnull}{@code )} (resp. {@link Skript
	 * Skript}{@code .}{@link PremennéSkriptu
	 * PremennéSkriptu}{@code .}{@link PremennéSkriptu#dajObzor(String)
	 * dajObzor}{@code (}{@code srg""}{@code )}).</p>
	 */
	public static PremenneSkriptu globálnePremenné()
	{ return globalnePremenne; }

	/** <p><a class="alias"></a> Alias pre {@link #globálnePremenné() globálnePremenné}.</p> */
	public static PremenneSkriptu globalnePremenne()
	{ return globalnePremenne; }


	/**
	 * <p>Vráti aktuálny objem vnútorného zásobníka vnorených priestorov
	 * premenných skriptov. Ide o aktuálny počet položiek v zásobníku.
	 * Zásobník, o ktorom je reč, uchováva priestory lokálnych premenných
	 * spustených skriptov a premenných definovaných vo vnorených blokoch
	 * skriptov. Ak nie je spustený žiadny skript, mal by byť jeho objem
	 * nulový. V opačnom prípade sa môže hodnota objemu rýchlo meniť.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V opise metódy {@link 
	 * #globálnePremenné() globálnePremenné} je spomenutý proces
	 * dynamického vytvárania a likvidácie priestorov lokálnych premenných.
	 * Nejde len o lokálne premenné v zmysle individuálnych premenných
	 * skriptu, ale aj o premenné platné len v rámci konkrétneho bloku.
	 * Priestory ich existencie su spravované prostredníctvom vnútorného
	 * zásobníka (tzv. „zásobníka priestorov“), ktorý je sprístupnený
	 * prostredníctvom skupiny metód, do ktorej patrí aj táto metóda.
	 * (Proces dynamického vytvárania a likvidácie lokálnych priestorov môže
	 * byť značne komplikovaný.)</p>
	 * 
	 * @return počet položiek (priestorov premenných) v zásobníku
	 * 
	 * @see #priestorNaVrchu()
	 * @see #vnorPriestor(PremennéSkriptu)
	 * @see #vynorPriestor()
	 */
	public static int objemPriestorov()
	{
		return lokálnePremenné.size();
	}

	/**
	 * <p>Vráti najvrchnejší priestor vnútorného zásobníka vnorených
	 * priestorov premenných skriptov. (Pozri aj opis metódy {@link 
	 * #objemPriestorov() objemPriestorov}.) Ak je zásobník prázdny, tak
	 * táto metóda vráti hodnotu {@code valnull}.</p>
	 * 
	 * <p>Ak sa na vrchu zásobníka priestorov nenachádza ten priestor, ktorý
	 * by ste očakávali (ten, ktorý ste tam mali naposledy vložiť), tak to
	 * znamená, že sa niekde stala chyba a tento stav treba vhodne
	 * prehodnotiť (odladiť) a prípadne ohlásiť používateľovi aplikácie
	 * (chybovým hlásením; prípadne, ak si myslíte, že ide o vnútornú chybu
	 * rámca, tak aj autorovi rámca).</p>
	 * 
	 * @return najvrchnejší (aktuálny) priestor premenných skriptov
	 *     v zásobníku alebo {@code valnull} (pri chybe)
	 * 
	 * @see #objemPriestorov()
	 * @see #vnorPriestor(PremennéSkriptu)
	 * @see #vynorPriestor()
	 */
	public static PremennéSkriptu priestorNaVrchu()
	{
		if (lokálnePremenné.empty()) return null;
		return lokálnePremenné.peek();
	}

	/**
	 * <p>Vloží do vnútorného zásobníka vnorených priestorov premenných
	 * skriptov novú položku – nový priestor. (Pozri aj opis metódy {@link 
	 * #objemPriestorov() objemPriestorov}.) Tento zásobník je bežne
	 * spravovaný automaticky, čiže sa nepredpokladá aktívne využívanie
	 * tejto metódy, avšak môže byť užitočná napríklad pri simulácii práce
	 * s objektmi a ich vnútornými premennými (atribútmi).</p>
	 * 
	 * <p>Návratová hodnota {@code valnull} signalizuje chybu. Pri tejto
	 * metóde to znamená buď pokus o vloženie hodnoty {@code valnull}
	 * namiesto platného priestoru, alebo pokus o duplicitné vloženie
	 * priestoru, čiže takého, ktorý sa už vo vnútornom zásobníku priestorov
	 * nachádza.</p>
	 * 
	 * @param priestor priestor premenných na vloženie do zásobníka; nesmie
	 *     byť {@code valnull}
	 * @return práve vložený priestor do zásobníka alebo {@code valnull}
	 *     (pri chybe)
	 * 
	 * @see #objemPriestorov()
	 * @see #priestorNaVrchu()
	 * @see #vynorPriestor()
	 */
	public static PremennéSkriptu vnorPriestor(PremennéSkriptu priestor)
	{
		if (null == priestor || -1 != lokálnePremenné.search(priestor))
			return null;
		return lokálnePremenné.push(priestor);
	}

	/**
	 * <p>Vyberie (a vráti) najvrchnejšiu položku zo zásobníka vnorených
	 * priestorov premenných skriptov. (Pozri aj opis metódy {@link 
	 * #objemPriestorov() objemPriestorov}.) Táto metóda je doplnkom k metóde
	 * {@link #vnorPriestor(PremennéSkriptu) vnorPriestor}.</p>
	 * 
	 * <p>Návratová hodnota {@code valnull} signalizuje chybu. Pri tejto
	 * metóde to znamená, že zásobník priestorov je prázdny.</p>
	 * 
	 * @return najvrchnejší (práve vybraný) priestor premenných skriptov
	 *     v zásobníku alebo {@code valnull} (pri chybe)
	 * 
	 * @see #objemPriestorov()
	 * @see #priestorNaVrchu()
	 * @see #vnorPriestor(PremennéSkriptu)
	 */
	public static PremennéSkriptu vynorPriestor()
	{
		if (lokálnePremenné.empty()) return null;
		return lokálnePremenné.pop();
	}


	/**
	 * <p>Vypíše všetky premenné dostupné v aktuálne vykonávanom bloku
	 * (to jest prostredí) na konzolu aktuálneho plátna (predvolene na
	 * {@linkplain GRobot#strop strop}; pozri aj {@link #presmerujNaPodlahu()
	 * presmerujNaPodlahu}). Totiž, vnorenie do každého bloku mení prostredie
	 * premenných stroja skriptov – vždy ho prekryje novým priestorom
	 * lokálnych premenných.</p>
	 * 
	 * <p>Toto je príkaz ladenia. Má uľahčiť proces ladenia formátovaným
	 * výpisom premenných na aktuálnu konzolu. (Informácie o globálnych
	 * premenných sa dajú získať aj prostredníctvom inštancie, ktorú vracia
	 * metóda {@link #globálnePremenné() globálnePremenné}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> To znamená, že globálne premenné
	 * by mohli byť v tomto vnorenom priestore prekryté lokálnymi (prípadne
	 * tzv. obzorovými) premennými, ale v skutočnosti je vznik takejto
	 * situácie veľmi nepravdepodobný, keďže pri zápise (to jest aj
	 * definícii) premenných sa vždy najskôr kontroluje dostupnosť premennej
	 * vo vyšších priestoroch premenných…</p>
	 * 
	 * <p>Na výpis je použitý aktuálny štýl (farebná schéma). Pozri aj:
	 * {@link #farbaLadenia(String) farbaLadenia}.</p>
	 */
	public static void vypíšPremenné()
	{
		TreeMap<String, Double> premenné =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		TreeMap<String, Color>  premennéFarby =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		TreeMap<String, Poloha> premennéPolohy =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		TreeMap<String, String> premennéReťazce =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		premenné.putAll(globálnePremenné.premenné);
		premennéFarby.putAll(globálnePremenné.premennéFarby);
		premennéPolohy.putAll(globálnePremenné.premennéPolohy);
		premennéReťazce.putAll(globálnePremenné.premennéReťazce);

		// Globálne premenné budú prepísané lokálnymi premennými:
		if (!lokálnePremenné.empty())
		{
			for (PremennéSkriptu priestor : lokálnePremenné)
			{
				premenné.putAll(priestor.premenné);
				premennéFarby.putAll(priestor.premennéFarby);
				premennéPolohy.putAll(priestor.premennéPolohy);
				premennéReťazce.putAll(priestor.premennéReťazce);
			}
		}

		for (Map.Entry<String, Double> premenná :
			premenné.entrySet())
			vypíšRiadokLadenia(farbyLadenia[
					FARBA_LADENIA_NÁZOV_PREMENNEJ],
				"     ", premenná.getKey(), farbyLadenia[
					FARBA_LADENIA_SYMBOL], " = ",
				farbyLadenia[FARBA_LADENIA_ČÍSLO],
				premenná.getValue());

		for (Map.Entry<String, Color> premenná :
			premennéFarby.entrySet())
			vypíšRiadokLadenia(farbyLadenia[
					FARBA_LADENIA_NÁZOV_PREMENNEJ],
				"     ", premenná.getKey(), farbyLadenia[
					FARBA_LADENIA_SYMBOL], " = ",
				farbyLadenia[FARBA_LADENIA_FARBA],
				Farba.farbaNaReťazec(premenná.getValue()));

		for (Map.Entry<String, Poloha> premenná :
			premennéPolohy.entrySet())
			vypíšRiadokLadenia(farbyLadenia[
					FARBA_LADENIA_NÁZOV_PREMENNEJ],
				"     ", premenná.getKey(), farbyLadenia[
					FARBA_LADENIA_SYMBOL], " = ",
				farbyLadenia[FARBA_LADENIA_POLOHA],
				Bod.polohaNaReťazec(premenná.getValue()));

		for (Map.Entry<String, String> premenná :
			premennéReťazce.entrySet())
			vypíšRiadokLadenia(farbyLadenia[
					FARBA_LADENIA_NÁZOV_PREMENNEJ],
				"     ", premenná.getKey(), farbyLadenia[
					FARBA_LADENIA_SYMBOL], " = ",
				farbyLadenia[FARBA_LADENIA_REŤAZEC],
				"\"", premenná.getValue(), "\"");

		// Obzorové premenné budú vypísané zvlášť.
		for (Map.Entry<String, PremenneSkriptu> položkaObzoru :
			obzory.entrySet())
		{
			vypíšRiadokLadenia(farbyLadenia[FARBA_LADENIA_RIADIACI_PRÍKAZ],
				"     ", farbyLadenia[FARBA_LADENIA_NÁZOV_PREMENNEJ],
				položkaObzoru.getKey());

			PremennéSkriptu obzor = položkaObzoru.getValue();

			for (Map.Entry<String, Double> premenná :
				obzor.premenné.entrySet())
				vypíšRiadokLadenia(farbyLadenia[
						FARBA_LADENIA_NÁZOV_PREMENNEJ],
					"         ", premenná.getKey(), farbyLadenia[
						FARBA_LADENIA_SYMBOL], " = ",
					farbyLadenia[FARBA_LADENIA_ČÍSLO],
					premenná.getValue());

			for (Map.Entry<String, Color> premenná :
				obzor.premennéFarby.entrySet())
				vypíšRiadokLadenia(farbyLadenia[
						FARBA_LADENIA_NÁZOV_PREMENNEJ],
					"         ", premenná.getKey(), farbyLadenia[
						FARBA_LADENIA_SYMBOL], " = ",
					farbyLadenia[FARBA_LADENIA_FARBA],
					Farba.farbaNaReťazec(premenná.getValue()));

			for (Map.Entry<String, Poloha> premenná :
				obzor.premennéPolohy.entrySet())
				vypíšRiadokLadenia(farbyLadenia[
						FARBA_LADENIA_NÁZOV_PREMENNEJ],
					"         ", premenná.getKey(), farbyLadenia[
						FARBA_LADENIA_SYMBOL], " = ",
					farbyLadenia[FARBA_LADENIA_POLOHA],
					Bod.polohaNaReťazec(premenná.getValue()));

			for (Map.Entry<String, String> premenná :
				obzor.premennéReťazce.entrySet())
				vypíšRiadokLadenia(farbyLadenia[
						FARBA_LADENIA_NÁZOV_PREMENNEJ],
					"         ", premenná.getKey(), farbyLadenia[
						FARBA_LADENIA_SYMBOL], " = ",
					farbyLadenia[FARBA_LADENIA_REŤAZEC],
					"\"", premenná.getValue(), "\"");
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #vypíšPremenné() vypíšPremenné}.</p> */
	public static void vypisPremenne() { vypíšPremenné(); }


	/**
	 * <p>Zistí, či jestvuje (je definovaná) premenná so zadaným názvom
	 * a typom.</p>
	 * 
	 * @param názov názov premennej
	 * @param typ typ premennej – povolené sú: {@link Double Double.class},
	 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
	 *     {@link String String.class}
	 * @return ak premenná zadaného údajového typu jestvuje, tak je
	 *     návratovou hodnotou tejto metódy hodnota {@code valtrue};
	 *     ak premenná nejestvuje alebo bol zadaný nepovolený údajový
	 *     typ premennej, tak je návratovou hodnotou tejto metódy
	 *     hodnota {@code valfalse}
	 * 
	 * @see PremennéSkriptu#jestvuje(String, Class)
	 */
	public static boolean premennáJestvuje(String názov, Class<?> typ)
	{
		if (null == názov || názov.isEmpty()) return false;

		// Najprv skontrolujeme, či sa názov nepokúša pristupovať k obzorovej
		// premennej (s bodkou). Potom odzadu prehľadávame zásobník lokálnych
		// premenných a nakoniec spracujeme globálne premenné.

		int indexOf = názov.indexOf('.');
		if (-1 != indexOf)
		{
			// Prázdny menný priestor sa odkazuje na globálny priestor,
			// ale prázdne meno premennej je zakázané:
			if (indexOf >= názov.length() - 1) return false;

			// Napriek tomu musím kontrolovať, či menný priestor jestvuje,
			// aby mi ho nevytvorilo:
			String názovObzoru = názov.substring(0, indexOf);
			if (!PremennéSkriptu.obzorJestvuje(názovObzoru)) return false;

			PremennéSkriptu obzor = PremennéSkriptu.dajObzor(názovObzoru);
			názov = názov.substring(indexOf + 1);

			if (typ == Double.class)
			{
				if (obzor.premenné.containsKey(názov)) return true;
			}
			else if (typ == Color.class)
			{
				if (obzor.premennéFarby.containsKey(názov)) return true;
			}
			else if (typ == Poloha.class)
			{
				if (obzor.premennéPolohy.containsKey(názov)) return true;
			}
			else if (typ == String.class)
			{
				if (obzor.premennéReťazce.containsKey(názov)) return true;
			}

			return false;
		}

		if (typ == Double.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premenné.containsKey(názov))
						return true;
				}
			}

			return globálnePremenné.premenné.containsKey(názov);
		}
		else if (typ == Color.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéFarby.containsKey(názov))
						return true;
				}
			}

			return globálnePremenné.premennéFarby.containsKey(názov);
		}
		else if (typ == Poloha.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéPolohy.containsKey(názov))
						return true;
				}
			}

			return globálnePremenné.premennéPolohy.containsKey(názov);
		}
		else if (typ == String.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéReťazce.containsKey(názov))
						return true;
				}
			}

			return globálnePremenné.premennéReťazce.containsKey(názov);
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
	public static boolean premennaJestvuje(String názov, Class<?> typ)
	{ return premennáJestvuje(názov, typ); }

	/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
	public static boolean premennáExistuje(String názov, Class<?> typ)
	{ return premennáJestvuje(názov, typ); }

	/** <p><a class="alias"></a> Alias pre {@link #premennáJestvuje(String, Class) premennáJestvuje}.</p> */
	public static boolean premennaExistuje(String názov, Class<?> typ)
	{ return premennáJestvuje(názov, typ); }

	/**
	 * <p>Zistí hodnotu premennej zadaného údajového typu.
	 * (Ak jestvuje – je definovaná.)</p>
	 * 
	 * @param názov názov premennej
	 * @param typ typ premennej – povolené sú: {@link Double Double.class},
	 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
	 *     {@link String String.class}
	 * @return ak premenná jestvuje, tak je návratovou hodnotou tejto
	 *     metódy hodnota tejto premennej; ak premenná nejestvuje,
	 *     prípadne bol zadaný nepovolený údajový typ premennej, tak je
	 *     návratovou hodnotou tejto metódy hodnota {@code valnull}
	 * 
	 * @see PremennéSkriptu#čítaj(String, Class)
	 */
	public static Object čítajPremennú(String názov, Class<?> typ)
	{
		if (null == názov || názov.isEmpty()) return null;

		// Najprv skontrolujeme, či sa názov nepokúša pristupovať k obzorovej
		// premennej (s bodkou). Potom odzadu prehľadávame zásobník lokálnych
		// premenných a nakoniec spracujeme globálne premenné.

		int indexOf = názov.indexOf('.');
		if (-1 != indexOf)
		{
			// Prázdny menný priestor sa odkazuje na globálny priestor,
			// ale prázdne meno premennej je zakázané:
			if (indexOf >= názov.length() - 1) return null;

			// Napriek tomu musím kontrolovať, či menný priestor jestvuje,
			// aby mi ho nevytvorilo:
			String názovObzoru = názov.substring(0, indexOf);
			if (!PremennéSkriptu.obzorJestvuje(názovObzoru)) return null;

			PremennéSkriptu obzor = PremennéSkriptu.dajObzor(názovObzoru);
			názov = názov.substring(indexOf + 1);

			if (typ == Double.class)
			{
				Double premenná = obzor.premenné.get(názov);
				if (null != premenná) return premenná;
			}
			else if (typ == Color.class)
			{
				Color premenná = obzor.premennéFarby.get(názov);
				if (null != premenná) return premenná;
			}
			else if (typ == Poloha.class)
			{
				Poloha premenná = obzor.premennéPolohy.get(názov);
				if (null != premenná) return premenná;
			}
			else if (typ == String.class)
			{
				String premenná = obzor.premennéReťazce.get(názov);
				if (null != premenná) return premenná;
			}

			return null;
		}

		if (typ == Double.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					Double premenná = priestor.premenné.get(názov);
					if (null != premenná) return premenná;
				}
			}

			return globálnePremenné.premenné.get(názov);
		}
		else if (typ == Color.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					Color premenná = priestor.premennéFarby.get(názov);
					if (null != premenná) return premenná;
				}
			}

			return globálnePremenné.premennéFarby.get(názov);
		}
		else if (typ == Poloha.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					Poloha premenná = priestor.premennéPolohy.get(názov);
					if (null != premenná) return premenná;
				}
			}

			return globálnePremenné.premennéPolohy.get(názov);
		}
		else if (typ == String.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					String premenná = priestor.premennéReťazce.get(názov);
					if (null != premenná) return premenná;
				}
			}

			return globálnePremenné.premennéReťazce.get(názov);
		}

		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajPremennú(String, Class) čítajPremennú}.</p> */
	public static Object citajPremennu(String názov, Class<?> typ)
	{ return čítajPremennú(názov, typ); }

	/**
	 * <p>Nastaví novú hodnotu určenej premennej alebo definuje novú
	 * premennú. Ak je zadaná hodnota nepovoleného údajového typu, tak
	 * nebude nastavená hodnota žiadnej premennej. To isté platí pri pokuse
	 * o zapísanie hodnoty {@code valnull}. Úspešnosť (resp. neúspešnosť)
	 * nastavenia hodnoty premennej potvrdzuje návratová hodnota tejto
	 * metódy – {@code valtrue} (úspech) / {@code valfalse} (neúspech).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Nová premenná je prednostne
	 * vytvorená v aktuálnom lokálnom priestore premenných (pozri napríklad
	 * opis metódy {@link #objemPriestorov() objemPriestorov}). Iba
	 * v prípade, že nie je aktívny žiadny lokálny priestor, sa premenná
	 * vytvorí v globálnom priestore. Na zápis premennej do konkrétneho
	 * priestoru premenných (vrátane {@linkplain #globálnePremenné()
	 * globálneho}) slúži metóda {@link PremennéSkriptu#zapíš(String,
	 * Object) PremennéSkriptu.zapíš}.</p>
	 * 
	 * @param názov názov premennej
	 * @param hodnota hodnota premennej povoleného údajového typu –
	 *     povolené sú: {@link Double Double},
	 *     {@link Color Color}, {@link Poloha Poloha} alebo
	 *     {@link String String}
	 * @return kontrolná návratová hodnota (ide najmä o overenie toho,
	 *     či zadaná hodnota naozaj bola niektorého povoleného typu);
	 *     ak je návratová hodnota {@code valtrue}, tak bola zadaná
	 *     hodnota zapísaná do premennej prislúchajúceho údajového typu
	 * 
	 * @see PremennéSkriptu#zapíš(String, Object)
	 */
	public static boolean zapíšPremennú(String názov, Object hodnota)
	{
		if (null == hodnota || null == názov || názov.isEmpty()) return false;

		// Najprv skontrolujeme, či názov nepristupuje k obzorovej
		// premennej (s bodkou).
		int indexOf = názov.indexOf('.');
		if (-1 != indexOf)
		{
			// Prázdny menný priestor sa odkazuje na globálny priestor,
			// ale prázdne meno premennej je zakázané:
			if (indexOf >= názov.length() - 1) return false;

			// V tomto prípade, ak obzor nejestvuje, tak ho vytvoríme:
			PremennéSkriptu obzor = PremennéSkriptu.
				dajObzor(názov.substring(0, indexOf));
			názov = názov.substring(indexOf + 1);

			if (hodnota instanceof Double)
			{
				obzor.premenné.put(názov, (Double)hodnota);
				return true;
			}
			else if (hodnota instanceof Color)
			{
				obzor.premennéFarby.put(názov, (Color)hodnota);
				return true;
			}
			else if (hodnota instanceof Poloha)
			{
				obzor.premennéPolohy.put(názov, (Poloha)hodnota);
				return true;
			}
			else if (hodnota instanceof String)
			{
				obzor.premennéReťazce.put(názov, (String)hodnota);
				return true;
			}

			return false;
		}

		// Ďalší zápis je mierne komplikovanejší. Premenná sa musí najskôr
		// skúsiť nájsť vo všetkých priestoroch a až keď sa nenájde, zapíše
		// sa do prvého dostupného priestoru.
		if (hodnota instanceof Double)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());
				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premenné.containsKey(názov))
					{
						priestor.premenné.put(názov, (Double)hodnota);
						return true;
					}
				}
			}

			if (globálnePremenné.premenné.containsKey(názov))
			{
				globálnePremenné.premenné.put(názov, (Double)hodnota);
				return true;
			}

			if (!lokálnePremenné.empty())
			{
				PremennéSkriptu priestor = lokálnePremenné.peek();
				priestor.premenné.put(názov, (Double)hodnota);
				return true;
			}
			else
			{
				globálnePremenné.premenné.put(názov, (Double)hodnota);
				return true;
			}
		}
		else if (hodnota instanceof Color)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());
				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéFarby.containsKey(názov))
					{
						priestor.premennéFarby.put(názov, (Color)hodnota);
						return true;
					}
				}
			}

			if (globálnePremenné.premennéFarby.containsKey(názov))
			{
				globálnePremenné.premennéFarby.put(názov, (Color)hodnota);
				return true;
			}

			if (!lokálnePremenné.empty())
			{
				PremennéSkriptu priestor = lokálnePremenné.peek();
				priestor.premennéFarby.put(názov, (Color)hodnota);
				return true;
			}
			else
			{
				globálnePremenné.premennéFarby.put(názov, (Color)hodnota);
				return true;
			}
		}
		else if (hodnota instanceof Poloha)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());
				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéPolohy.containsKey(názov))
					{
						priestor.premennéPolohy.put(názov, (Poloha)hodnota);
						return true;
					}
				}
			}

			if (globálnePremenné.premennéPolohy.containsKey(názov))
			{
				globálnePremenné.premennéPolohy.put(názov, (Poloha)hodnota);
				return true;
			}

			if (!lokálnePremenné.empty())
			{
				PremennéSkriptu priestor = lokálnePremenné.peek();
				priestor.premennéPolohy.put(názov, (Poloha)hodnota);
				return true;
			}
			else
			{
				globálnePremenné.premennéPolohy.put(názov, (Poloha)hodnota);
				return true;
			}
		}
		else if (hodnota instanceof String)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());
				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéReťazce.containsKey(názov))
					{
						priestor.premennéReťazce.put(názov, (String)hodnota);
						return true;
					}
				}
			}

			if (globálnePremenné.premennéReťazce.containsKey(názov))
			{
				globálnePremenné.premennéReťazce.put(názov, (String)hodnota);
				return true;
			}

			if (!lokálnePremenné.empty())
			{
				PremennéSkriptu priestor = lokálnePremenné.peek();
				priestor.premennéReťazce.put(názov, (String)hodnota);
				return true;
			}
			else
			{
				globálnePremenné.premennéReťazce.put(názov, (String)hodnota);
				return true;
			}
		}

		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zapíšPremennú(String, Object) zapíšPremennú}.</p> */
	public static boolean zapisPremennu(String názov, Object hodnota)
	{ return zapíšPremennú(názov, hodnota); }

	/**
	 * <p>Vymaže definíciu premennej zadaného údajového typu.
	 * (Ak jestvuje.)</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Premenná je prednostne
	 * vymazaná z aktuálneho lokálneho priestoru premenných (pozri napríklad
	 * opis metódy {@link #objemPriestorov() objemPriestorov}). V prípade,
	 * že nie je aktívny žiadny lokálny priestor, sa premenná vymaže
	 * z globálneho priestoru. Na vymazanie premennej z konkrétneho priestoru
	 * premenných (vrátane {@linkplain #globálnePremenné() globálneho}) slúži
	 * metóda {@link PremennéSkriptu#vymaž(String, Class)
	 * PremennéSkriptu.vymaž}.</p>
	 * 
	 * @param názov názov premennej
	 * @param typ typ premennej – povolené sú: {@link Double Double.class},
	 *     {@link Color Color.class}, {@link Poloha Poloha.class} alebo
	 *     {@link String String.class}
	 * 
	 * @see PremennéSkriptu#vymaž(String, Class)
	 */
	public static void vymažPremennú(String názov, Class<?> typ)
	{
		if (null == názov || názov.isEmpty()) return;

		// Najprv skontrolujeme, či sa názov nepokúša pristupovať k obzorovej
		// premennej (s bodkou). Potom odzadu prehľadávame zásobník lokálnych
		// premenných a nakoniec spracujeme globálne premenné.

		int indexOf = názov.indexOf('.');
		if (-1 != indexOf)
		{
			// Prázdny menný priestor sa odkazuje na globálny priestor,
			// ale prázdne meno premennej je zakázané:
			if (indexOf >= názov.length() - 1) return;

			// Napriek tomu musím kontrolovať, či menný priestor jestvuje,
			// aby mi ho nevytvorilo:
			String názovObzoru = názov.substring(0, indexOf);
			if (!PremennéSkriptu.obzorJestvuje(názovObzoru)) return;

			PremennéSkriptu obzor = PremennéSkriptu.dajObzor(názovObzoru);
			názov = názov.substring(indexOf + 1);

			if (typ == Double.class)
			{
				if (obzor.premenné.containsKey(názov))
				{
					obzor.premenné.remove(názov);
					return;
				}
			}
			else if (typ == Color.class)
			{
				if (obzor.premennéFarby.containsKey(názov))
				{
					obzor.premennéFarby.remove(názov);
					return;
				}
			}
			else if (typ == Poloha.class)
			{
				if (obzor.premennéPolohy.containsKey(názov))
				{
					obzor.premennéPolohy.remove(názov);
					return;
				}
			}
			else if (typ == String.class)
			{
				if (obzor.premennéReťazce.containsKey(názov))
				{
					obzor.premennéReťazce.remove(názov);
					return;
				}
			}
		}
		else if (typ == Double.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premenné.containsKey(názov))
					{
						priestor.premenné.remove(názov);
						return;
					}
				}
			}

			if (globálnePremenné.premenné.containsKey(názov))
				globálnePremenné.premenné.remove(názov);
		}
		else if (typ == Color.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéFarby.containsKey(názov))
					{
						priestor.premennéFarby.remove(názov);
						return;
					}
				}
			}

			if (globálnePremenné.premennéFarby.containsKey(názov))
				globálnePremenné.premennéFarby.remove(názov);
		}
		else if (typ == Poloha.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéPolohy.containsKey(názov))
					{
						priestor.premennéPolohy.remove(názov);
						return;
					}
				}
			}

			if (globálnePremenné.premennéPolohy.containsKey(názov))
				globálnePremenné.premennéPolohy.remove(názov);
		}
		else if (typ == String.class)
		{
			if (!lokálnePremenné.empty())
			{
				ListIterator<PremennéSkriptu> iterátor =
					lokálnePremenné.listIterator(lokálnePremenné.size());

				while (iterátor.hasPrevious())
				{
					PremennéSkriptu priestor = iterátor.previous();
					if (priestor.premennéReťazce.containsKey(názov))
					{
						priestor.premennéReťazce.remove(názov);
						return;
					}
				}
			}

			if (globálnePremenné.premennéReťazce.containsKey(názov))
				globálnePremenné.premennéReťazce.remove(názov);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymažPremennú(String, Class) vymažPremennú}.</p> */
	public static void vymazPremennu(String názov, Class<?> typ)
	{ vymažPremennú(názov, typ); }


	/**
	 * <p>Získa kód poslednej chyby, ktorá nastala počas vykonávania
	 * príkazov {@linkplain Svet#interaktívnyRežim(boolean)
	 * interaktívneho režimu} alebo {@linkplain 
	 * Svet#vykonajSkript(String[]) skriptu}. Môže ísť buď o jeden
	 * z nasledujúcich kódov:</p>
	 * 
	 * <ul>
	 * <li>{@link Konštanty#ŽIADNA_CHYBA ŽIADNA_CHYBA},</li>
	 * <li>{@link Konštanty#CHYBA_VYKONANIA_PRÍKAZU
	 * CHYBA_VYKONANIA_PRÍKAZU},</li>
	 * <li>{@link Konštanty#CHYBA_DVOJITÁ_MENOVKA CHYBA_DVOJITÁ_MENOVKA},</li>
	 * <li>{@link Konštanty#CHYBA_CHÝBAJÚCA_MENOVKA
	 * CHYBA_CHÝBAJÚCA_MENOVKA},</li>
	 * <li>{@link Konštanty#CHYBA_NEZNÁMA_MENOVKA CHYBA_NEZNÁMA_MENOVKA},</li>
	 * <li>{@link Konštanty#CHYBA_NEZNÁME_SLOVO CHYBA_NEZNÁME_SLOVO},</li>
	 * <li>{@link Konštanty#CHYBA_CHYBNÁ_ŠTRUKTÚRA CHYBA_CHYBNÁ_ŠTRUKTÚRA},</li>
	 * <li>{@link Konštanty#CHYBA_NEZNÁME_MENO CHYBA_NEZNÁME_MENO},</li>
	 * <li>{@link Konštanty#CHYBA_NEZNÁMY_PRÍKAZ CHYBA_NEZNÁMY_PRÍKAZ},</li>
	 * <li>{@link Konštanty#CHYBA_ČÍTANIA_SKRIPTU CHYBA_ČÍTANIA_SKRIPTU},</li>
	 * <li>alebo {@link Konštanty#CHYBA_VOLANIA_SKRIPTU
	 * CHYBA_VOLANIA_SKRIPTU},</li>
	 * </ul>
	 * 
	 * <p>alebo o kód s celočíselnou hodnotou väčšou alebo rovnou
	 * {@code num50}, kedy ide o chybu spôsobenú pri spracovaní
	 * (matematického) výrazu vyhodnoteného prostredníctvom vnútorného
	 * vyhodnocovača výrazov (pozri
	 * <a 
	 * href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/podpora/ExpressionProcessor.java"
	 * target="_blank"><code>ExpressionProcessor</code></a>), ktorý
	 * (syntakticky) nasleduje za znakom mriežky {@code #} (chybové kódy
	 * vnútorného vyhodnocovača (matematických) výrazov nie sú uvedené v tejto
	 * dokumentácii, ale syntax s mriežkou je bližšie spomenutá napríklad
	 * v opise metódy {@link Svet#interaktívnyRežim(boolean)
	 * Svet.interaktívnyRežim}; chybové kódy vyhodnocovača sú uvedené v <a
	 * href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/podpora/ExpressionProcessor.java#L312"
	 * target="_blank">jeho zdrojovom kóde</a>).<!-- TODO možno ešte lepšie
	 * zdokumentovať, ale na iných miestach --></p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Tabuľka chýb je v opise
	 * metódy {@link #textChyby(int) textChyby}.</p>
	 * 
	 * @return kód chyby – ďalšie podrobnosti môžete získať kliknutím
	 *     na kód chyby v zozname vyššie
	 * 
	 * @see #riadokPoslednejChyby()
	 * @see #textPoslednejChyby()
	 * @see #textChyby(int)
	 */
	public static int kódPoslednejChyby() { return poslednáChyba % 100; }

	/** <p><a class="alias"></a> Alias pre {@link #kódPoslednejChyby() kódPoslednejChyby}.</p> */
	public static int kodPoslednejChyby() { return poslednáChyba % 100; }

	/**
	 * <p>Ak posledná chyba vznikla na konkrétnom riadku skriptu, tak
	 * táto metóda vráti číslo tohto riadka.</p>
	 * 
	 * @return číslo riadka, na ktorom vznikla posledná chyba
	 * 
	 * @see #kódPoslednejChyby()
	 * @see #textPoslednejChyby()
	 * @see #textChyby(int)
	 */
	public static int riadokPoslednejChyby()
	{ return poslednáChyba / 100; }

	/**
	 * <p>Vráti vysvetľujúci text ku kódu poslednej chyby, ktorá nastala
	 * počas vykonávania príkazov
	 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
	 * alebo skriptu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Tabuľka chýb je v opise
	 * metódy {@link #textChyby(int) textChyby}.</p>
	 * 
	 * @return text ku kódu chyby
	 * 
	 * @see #kódPoslednejChyby()
	 * @see #riadokPoslednejChyby()
	 * @see #textChyby(int)
	 */
	public static String textPoslednejChyby()
	{ return textChyby(poslednáChyba); }

	/**
	 * <p>Vráti vysvetľujúci text ku kódu chyby určenej parametrom
	 * {@code kódChyby}. Ide o kódy vnútorne definované a používané
	 * pri hláseniach o nesprávnych stavoch počas vykonávania príkazov
	 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
	 * alebo skriptu.</p>
	 * 
	 * <p><b>Tabuľka chýb</b></p>
	 * 
	 * <p>V tabuľke sú zosumarizované všetky konštanty reprezentujúce kódy
	 * chýb skriptov programovacieho rámca a prislúchajúce textové znenia
	 * patriace k týmto kódom. Kliknutím na kód prejdete na ďalšie
	 * podrobnosti.</p>
	 * 
	 * <p>Výnimku tvoria kódy s hodnotou rovnou alebo vyššou než
	 * {@code num50}, ktoré sú rezervované pre chyby spôsobené pri
	 * spracovaní výrazu vyhodnoteného prostredníctvom vnútorného
	 * vyhodnocovača výrazov – pozri opis metódy {@link #kódPoslednejChyby()
	 * kódPoslednejChyby}.</p>
	 * 
	 * <table class="langIDTable">
	 * <tr><th>Kód chyby</th><th>Textové znenie chyby</th></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_VOLANIA_SKRIPTU
	 * CHYBA_VOLANIA_SKRIPTU}</td><td>Vznikla chyba pri volaní vnoreného
	 * skriptu. (Bola zaznamenaná príliš veľká hĺbka volaní vnorených
	 * skriptov.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_ČÍTANIA_SKRIPTU
	 * CHYBA_ČÍTANIA_SKRIPTU}</td><td>Vznikla chyba pri čítaní skriptu.
	 * (Súbor alebo zdroj s obsahom skriptu nemusel byť nájdený alebo mohla
	 * vzniknúť chyba pri čítaní údajov.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#ŽIADNA_CHYBA ŽIADNA_CHYBA}</td><td>Nenastala
	 * žiadna chyba. (Prípadne mohla nastať neznáma
	 * chyba.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_VYKONANIA_PRÍKAZU
	 * CHYBA_VYKONANIA_PRÍKAZU}</td><td>Nastala chyba počas vykonávania
	 * posledného príkazu. (Mohlo ísť napríklad o zadanie nesprávneho
	 * argumentu a pod.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_DVOJITÁ_MENOVKA
	 * CHYBA_DVOJITÁ_MENOVKA}</td><td>V skripte sa vyskytla dvojnásobná
	 * definícia menovky. (Vykonávanie skriptu nie je bezpečné, pretože
	 * skript pri vykonávaní riadiacich príkazov („na,“ „ak,“ „dokedy“
	 * s prípadnou alternatívou „inak“ pri posledných dvoch) nemusí správne
	 * identifikovať, ktorým riadkom má jeho vykonávanie
	 * pokračovať.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_CHÝBAJÚCA_MENOVKA
	 * CHYBA_CHÝBAJÚCA_MENOVKA}</td><td>Za riadiacim príkazom „na,“ „ak,“
	 * „dokedy“ alebo za alternatívou „inak“ chýba zadanie menovky. (Za
	 * uvedenými riadiacimi príkazmi nie je zadaná menovka, prípadne nie je
	 * k dispozícii žiadna náhrada za chýbajúcu menovku, ako napríklad blok
	 * príkazov na vykonanie.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_NEZNÁMA_MENOVKA
	 * CHYBA_NEZNÁMA_MENOVKA}</td><td>Menovka za riadiacim príkazom „na,“
	 * „ak,“ „dokedy“ alebo za alternatívou „inak“ je neznáma. (Nie je
	 * definovaná v rámci aktuálneho bloku skriptu alebo pri príkaze „na“
	 * ani v niektorom z nadradených blokov skriptu.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_NEZNÁME_SLOVO
	 * CHYBA_NEZNÁME_SLOVO}</td><td>Za menovkou riadiaceho príkazu „ak“
	 * alebo „dokedy“ sa vyskytlo neznáme slovo. (Za zadaním prvej menovky
	 * môže nasledovať ďalšia menovka a to buď bezprostredne, alebo za slovom
	 * určujúcim alternatívu – „inak.“ Ostatné slová sú považované za
	 * neznáme.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_CHYBNÁ_ŠTRUKTÚRA
	 * CHYBA_CHYBNÁ_ŠTRUKTÚRA}</td><td>Pokus o korektné rozpoznanie
	 * riadiaceho príkazu „obzor,“ „ak,“ „opakuj“ alebo „dokedy“ zlyhal.
	 * (Chyba vzniká napríklad, ak riadiaci príkaz nenašiel ďalší príkaz
	 * alebo blok na vykonanie/opakovanie. Pri opakovaniach vzniká chyba
	 * aj vtedy, ak nie je definovaná riadiaca premenná, ktorú sa riadiaci
	 * príkaz pokúša použiť na svoju činnosť.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_NEZNÁME_MENO
	 * CHYBA_NEZNÁME_MENO}</td><td>Naposledy aktivovaná inštancia už alebo
	 * ešte nejestvuje. (Zadané meno inštancie je neznáme.)</td></tr>
	 * 
	 * <tr><td>{@link Konštanty#CHYBA_NEZNÁMY_PRÍKAZ
	 * CHYBA_NEZNÁMY_PRÍKAZ}</td><td>Zadaný príkaz nebol rozpoznaný.
	 * (Najčastejšími príčinami sú syntaktické chyby alebo neaktivovanie
	 * správnej (prípadne žiadnej) inštancie, to jest takej, ktorá skutočne
	 * obsahuje definíciu metódy zodpovedajúcej príkazu
	 * skriptu.)</td></tr>
	 * 
	 * <tr><td>{@code num50}</td><td>Nepodarilo sa priradiť reťazec
	 * vyhodnocovaču výrazov. (Vnútorný vyhodnocovač (matematických) výrazov
	 * z určitého dôvodu neprijal reťazec na spracovanie.)</td></tr>
	 * 
	 * <tr><td><code>&gt; </code>{@code num50}</td><td>Vznikla chyba pri
	 * vyhodnocovaní výrazu. (Vnútorný vyhodnocovač (matematických) výrazov
	 * ohlásil chybu pri spracovaní reťazca, ktorý pravdepodobne nie je
	 * korektným (matematickým) výrazom. Text chyby: <em>«pôvodný text chyby
	 * v angličtine»</em>.)</td></tr>
	 * 
	 * </table>
	 * 
	 * <p>Texty chýb vyhodnocovača (matematických) výrazov sú uvedené v <a
	 * href="https://github.com/raubirius/GRobot/blob/master/kni%C5%BEnica/podpora/ExpressionProcessor.java#L312"
	 * target="_blank">jeho zdrojovom kóde</a>.</p>
	 * 
	 * @return text ku kódu chyby
	 * 
	 * @see #kódPoslednejChyby()
	 * @see #riadokPoslednejChyby()
	 * @see #textPoslednejChyby()
	 */
	public static String textChyby(int kódChyby)
	{
		int kód = kódChyby % 100;

		if (kód >= 50)
		{
			if (50 == kód) return "Nepodarilo sa priradiť reťazec " +
				"vyhodnocovaču výrazov. (Vnútorný vyhodnocovač " +
				"matematických výrazov z určitého dôvodu neprijal reťazec " +
				"na spracovanie.)";

			String text = "kód chyby: " + (kód - 51); try { text = "" +
				ExpressionProcessor.Value.TypeOrError.values()[kód - 51]; }
				catch (Throwable t) { }

			return "Vznikla chyba pri vyhodnocovaní výrazu. (Vnútorný " +
				"vyhodnocovač matematických výrazov ohlásil chybu pri " +
				"spracovaní reťazca, ktorý pravdepodobne nie je korektným " +
				"matematickým výrazom. Text chyby: " + výraz.toString() +
				" – " + text + ".)";
		}

		switch (kód)
		{
			case CHYBA_VOLANIA_SKRIPTU: return "Vznikla chyba pri volaní " +
				"vnoreného skriptu. (Buď bolo zaznamenané rekurzívne " +
				"spustenie skriptu, alebo bola zaznamenaná príliš veľká " +
				"hĺbka volaní vnorených skriptov.)";

			case CHYBA_ČÍTANIA_SKRIPTU: return "Vznikla chyba pri " +
				"čítaní skriptu. (Súbor alebo zdroj s obsahom skriptu " +
				"nemusel byť nájdený alebo mohla vzniknúť chyba pri " +
				"čítaní údajov.)";

			case ŽIADNA_CHYBA: return "Nenastala žiadna chyba. " +
				"(Prípadne mohla nastať neznáma chyba.)";

			case CHYBA_VYKONANIA_PRÍKAZU: return "Nastala chyba počas " +
				"vykonávania posledného príkazu. (Mohlo ísť napríklad " +
				"o zadanie nesprávneho argumentu a pod.)";

			case CHYBA_DVOJITÁ_MENOVKA: return "V skripte sa vyskytla " +
				"dvojnásobná definícia menovky. (Vykonávanie skriptu " +
				"nie je bezpečné, pretože skript pri vykonávaní " +
				"riadiacich príkazov („na,“ „ak,“ „dokedy“ s prípadnou " +
				"alternatívou „inak“ pri posledných dvoch) nemusí " +
				"správne identifikovať, ktorým riadkom má jeho " +
				"vykonávanie pokračovať.)";

			case CHYBA_CHÝBAJÚCA_MENOVKA: return "Za riadiacim " +
				"príkazom „na,“ „ak,“ „dokedy“ alebo za alternatívou " +
				"„inak“ chýba zadanie menovky. (Za uvedenými riadiacimi " +
				"príkazmi nie je zadaná menovka, prípadne nie je " +
				"k dispozícii žiadna náhrada za chýbajúcu menovku, ako " +
				"napríklad blok príkazov na vykonanie.)";

			case CHYBA_NEZNÁMA_MENOVKA: return "Menovka za riadiacim " +
				"príkazom „na,“ „ak,“ „dokedy“ alebo za alternatívou " +
				"„inak“ je neznáma. (Nie je definovaná v rámci " +
				"aktuálneho bloku skriptu alebo pri príkaze „na“ ani " +
				"v niektorom z nadradených blokov skriptu.)";

			case CHYBA_NEZNÁME_SLOVO: return "Za menovkou riadiaceho " +
				"príkazu „ak“ alebo „dokedy“ sa vyskytlo neznáme slovo. " +
				"(Za zadaním prvej menovky môže nasledovať ďalšia " +
				"menovka a to buď bezprostredne, alebo za slovom " +
				"určujúcim alternatívu – „inak.“ Ostatné slová sú " +
				"považované za neznáme.)";

			case CHYBA_CHYBNÁ_ŠTRUKTÚRA: return "Pokus o korektné " +
				"rozpoznanie riadiaceho príkazu „obzor,“ „ak,“ „opakuj“ " +
				"alebo „dokedy“ zlyhal. (Chyba vzniká napríklad, ak " +
				"riadiaci príkaz nenašiel ďalší príkaz alebo blok na " +
				"vykonanie/opakovanie. Pri opakovaniach vzniká chyba aj " +
				"vtedy, ak nie je definovaná riadiaca premenná, ktorú sa " +
				"riadiaci príkaz pokúša použiť na svoju činnosť.)";

			case CHYBA_NEZNÁME_MENO: return "Naposledy aktivovaná " +
				"inštancia už alebo ešte nejestvuje. (Zadané meno " +
				"inštancie je neznáme.)";

			case CHYBA_NEZNÁMY_PRÍKAZ: return "Zadaný príkaz nebol " +
				"rozpoznaný. (Najčastejšími príčinami sú syntaktické " +
				"chyby alebo neaktivovanie správnej (prípadne žiadnej) " +
				"inštancie, to jest takej, ktorá skutočne obsahuje " +
				"definíciu metódy zodpovedajúcej príkazu skriptu.)";
		}

		return "Nastala neznáma chyba s kódom: " + kódChyby +
			". (Text chyby s týmto kódom nie je uložený.)";
	}

	/**
	 * <p>Získa návratovú hodnotu naposledy vykonaného „príkazu“
	 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu},
	 * ktorý bol vykonaný vo forme volania niektorej metódy triedy rámca
	 * a to buď samostatne, alebo v rámci vykonávania skriptu.</p>
	 * 
	 * @return návratová hodnota naposledy vykonaného príkazu
	 *     (metódy rámca zadanej vo forme príkazu {@linkplain 
	 *     Svet#interaktívnyRežim(boolean) interaktívneho režimu})
	 */
	public static Object poslednáNávratováHodnota()
	{ return poslednáNávratováHodnota; }

	/** <p><a class="alias"></a> Alias pre {@link #poslednáNávratováHodnota() poslednáNávratováHodnota}.</p> */
	public static Object poslednaNavratovaHodnota()
	{ return poslednáNávratováHodnota; }

	/**
	 * <p>Presmeruje všetky aktivity súvisiace s {@linkplain 
	 * Svet#režimLadenia(boolean) režimom ladenia} (výpisy, mazanie textov,
	 * úprava farebnej schémy…) na vnútornú konzolu {@linkplain 
	 * GRobot#podlaha podlahy}. Od okamihu vykonania tejto metódy, budú
	 * všetky aktivity presmerované na vnútornú konzolu podlahy.</p>
	 * 
	 * @see #presmerujNaStrop()
	 * @see #používaPodlahu()
	 * @see #používaStrop()
	 */
	public static void presmerujNaPodlahu()
	{
		plátno = GRobot.podlaha;
	}

	/**
	 * <p>Presmeruje všetky aktivity súvisiace s {@linkplain 
	 * Svet#režimLadenia(boolean) režimom ladenia} (výpisy, mazanie textov,
	 * úprava farebnej schémy…) na vnútornú konzolu {@linkplain 
	 * GRobot#strop stropu}. (Toto je predvolené správanie.) Od okamihu
	 * vykonania tejto metódy, budú všetky aktivity presmerované na
	 * vnútornú konzolu podlahy (ak to tak dovtedy nebolo).</p>
	 * 
	 * @see #presmerujNaPodlahu()
	 * @see #používaPodlahu()
	 * @see #používaStrop()
	 */
	public static void presmerujNaStrop()
	{
		plátno = GRobot.strop;
	}

	/**
	 * <p>Metóda zistí, či stroj skriptov používa na aktivity súvisiace
	 * s {@linkplain Svet#režimLadenia(boolean) režimom ladenia} (výpisy,
	 * mazanie textov, úprava farebnej schémy…) vnútornú konzolu {@linkplain 
	 * GRobot#podlaha podlahy}. Ak áno, tak je jej návratová hodnota
	 * {@code valtrue}.</p>
	 * 
	 * @return {@code valtrue} v prípade, že
	 *     {@linkplain Svet#režimLadenia(boolean) režim ladenia} používa
	 *     vnútornú konzolu {@linkplain GRobot#podlaha podlahy},
	 *     {@code valfalse} v opačnom prípade
	 * 
	 * @see #presmerujNaPodlahu()
	 * @see #presmerujNaStrop()
	 * @see #používaStrop()
	 */
	public static boolean používaPodlahu()
	{
		return plátno == GRobot.podlaha;
	}

	/** <p><a class="alias"></a> Alias pre {@link #používaPodlahu() používaPodlahu}.</p> */
	public static boolean pouzivaPodlahu()
	{
		return plátno == GRobot.podlaha;
	}

	/**
	 * <p>Metóda zistí, či stroj skriptov používa na aktivity súvisiace
	 * s {@linkplain Svet#režimLadenia(boolean) režimom ladenia} (výpisy,
	 * mazanie textov, úprava farebnej schémy…) vnútornú konzolu {@linkplain 
	 * GRobot#strop stropu}. Ak áno, tak je jej návratová hodnota
	 * {@code valtrue}.</p>
	 * 
	 * @return {@code valtrue} v prípade, že
	 *     {@linkplain Svet#režimLadenia(boolean) režim ladenia} používa
	 *     vnútornú konzolu {@linkplain GRobot#strop stropu}, {@code valfalse}
	 *     v opačnom prípade
	 * 
	 * @see #presmerujNaPodlahu()
	 * @see #presmerujNaStrop()
	 * @see #používaPodlahu()
	 */
	public static boolean používaStrop()
	{
		return plátno == GRobot.strop;
	}

	/** <p><a class="alias"></a> Alias pre {@link #používaStrop() používaStrop}.</p> */
	public static boolean pouzivaStrop()
	{
		return plátno == GRobot.strop;
	}

	// Zoznam reťazcov pomáhajúcich pri prevode reťazcov na indexy.
	private final static String[] reťazceNázvovFarieb =
		{"číslo riadka", "menovka", "symbol", "meno inštancie",
		"názov premennej", "číslo", "farba", "poloha", "reťazec",
		"riadiaci príkaz", "príkaz", "aktívny riadok", "začiatok bloku",
		"koniec bloku", "jednoduchý výraz", "zložený výraz", "chyba"};

	// Prevedie reťazec na číselnú konštantu.
	private static int názovNaIndex(String názov)
	{
		názov = názov.trim().replaceAll("  ", " ").toLowerCase();
		int i = 0; for (String názovFarby : reťazceNázvovFarieb)
		{
			if (názovFarby.equals(názov)) return i;
			++i;
		}
		return -1;
	}

	/**
	 * <p>Zistí farbu určeného prvku syntaxe používanej pri zobrazovaní
	 * zdrojového kódu v {@linkplain Svet#režimLadenia(boolean) režime
	 * ladenia}. Ak zadaný názov farebného prvku syntaxe nekorešponduje
	 * ani s jedným z povolených názvov, tak je vrátená hodnota
	 * {@code valnull}.</p>
	 * 
	 * @param názov názov farby vo forme reťazca (povolené sú tieto názvy:
	 *     {@code srg"číslo riadka"}, {@code srg"menovka"},
	 *     {@code srg"symbol"}, {@code srg"meno inštancie"},
	 *     {@code srg"názov premennej"}, {@code srg"číslo"},
	 *     {@code srg"farba"}, {@code srg"poloha"}, {@code srg"reťazec"},
	 *     {@code srg"riadiaci príkaz"}, {@code srg"príkaz"},
	 *     {@code srg"aktívny riadok"}, {@code srg"začiatok bloku"},
	 *     {@code srg"koniec bloku"}, {@code srg"jednoduchý výraz"},
	 *     {@code srg"zložený výraz"} a {@code srg"chyba"})
	 * @return farba priradená k určenému prvku syntaxe alebo hodnota
	 *     {@code valnull}, ak názov nebo rozpoznaný
	 */
	public static Farba farbaLadenia(String názov)
	{
		int index = názovNaIndex(názov);
		if (-1 == index) return null;
		return farbyLadenia[index];
	}

	/**
	 * <p>Určuje novú farbu určeného prvku syntaxe používanej pri zobrazovaní
	 * zdrojového kódu v {@linkplain Svet#režimLadenia(boolean) režime
	 * ladenia}. Hodnota {@code valnull} nie je povolená a je ignorovaná.
	 * Farba prvku na nastavenie, je cielená názvom prvku vo forme
	 * reťazca. Ak zadaný názov nekorešponduje ani s jedným z povolených
	 * názvov, tak je zadaná hodnota farby ignorovaná.</p>
	 * 
	 * @param názov názov farby vo forme reťazca (povolené sú tieto názvy:
	 *     {@code srg"číslo riadka"}, {@code srg"menovka"},
	 *     {@code srg"symbol"}, {@code srg"meno inštancie"},
	 *     {@code srg"názov premennej"}, {@code srg"číslo"},
	 *     {@code srg"farba"}, {@code srg"poloha"}, {@code srg"reťazec"},
	 *     {@code srg"riadiaci príkaz"}, {@code srg"príkaz"},
	 *     {@code srg"aktívny riadok"}, {@code srg"začiatok bloku"},
	 *     {@code srg"koniec bloku"}, {@code srg"jednoduchý výraz"},
	 *     {@code srg"zložený výraz"} a {@code srg"chyba"})
	 * @param farba nová farba zadaného prvku syntaxe
	 */
	public static void farbaLadenia(String názov, Color nováFarba)
	{
		int index = názovNaIndex(názov);
		if (-1 != index && null != nováFarba)
		{
			if (nováFarba instanceof Farba)
				farbyLadenia[index] = (Farba)nováFarba;
			else
				farbyLadenia[index] = new Farba(nováFarba);
		}
	}

	/**
	 * <p>Určuje novú farbu určeného prvku syntaxe používanej pri
	 * zobrazovaní zdrojového kódu v {@linkplain Svet#režimLadenia(boolean)
	 * režime ladenia}. Farba je určená prostredníctvom objektu, ktorý
	 * implementuje rozhranie {@link Farebnosť Farebnosť}. Hodnota
	 * {@code valnull} nie je povolená a je ignorovaná.
	 * Farba prvku na nastavenie, je cielená názvom prvku vo forme
	 * reťazca. Ak zadaný názov nekorešponduje ani s jedným z povolených
	 * názvov, tak nie je vykonaná žiadna akcia.</p>
	 * 
	 * @param názov názov farby vo forme reťazca (povolené sú tieto názvy:
	 *     {@code srg"číslo riadka"}, {@code srg"menovka"},
	 *     {@code srg"symbol"}, {@code srg"meno inštancie"},
	 *     {@code srg"názov premennej"}, {@code srg"číslo"},
	 *     {@code srg"farba"}, {@code srg"poloha"}, {@code srg"reťazec"},
	 *     {@code srg"riadiaci príkaz"}, {@code srg"príkaz"},
	 *     {@code srg"aktívny riadok"}, {@code srg"začiatok bloku"},
	 *     {@code srg"koniec bloku"}, {@code srg"jednoduchý výraz"},
	 *     {@code srg"zložený výraz"} a {@code srg"chyba"})
	 * @param objekt objekt určujúci novú farbu zadaného prvku syntaxe
	 */
	public static void farbaLadenia(String názov, Farebnosť objekt)
	{
		if (null != objekt) farbaLadenia(názov, objekt.farba());
	}
}
