
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

 // Táto trieda bola do verzie 1.85 vnorenou triedou ústrednej triedy GRobot.
 // Po tejto verzii sa osamostatnila a teraz tvorí samostatnú triedu balíčka
 // programového rámca skupiny tried grafického robota.

package knižnica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

// -------------------------------- //
//  *** Trieda GRobotException ***  //
// -------------------------------- //

/**
 * <p>Výnimka generovaná programovacím rámcom so slovenským textom opisujúcim
 * udalosť a univerzálnym jazykovým identifikátorom umožňujúcim preklad do
 * ľubovoľného jazyka. Tento objekt bol do prostredia programovacieho rámca
 * pridaný potom, čo začala byť využívaná za hranicami svojho pôvodného
 * určenia. Jestvuje len veľmi málo výnimiek v rámci programovacieho rámca,
 * ktoré nie sú priamo generované prostredníctvom tejto triedy (napríklad
 * systémové správy, ktoré sú posúvané programovacím rámcom: {@link 
 * NoSuchElementException}, {@link NullPointerException}, {@link 
 * UnsupportedFlavorException} alebo priamy vznik správ {@link 
 * ArrayIndexOutOfBoundsException}, {@link IllegalArgumentException}, ktoré
 * sú generované niektorými metódami Javy, ktoré programovací rámec vnútorne
 * používa…).</p>
 * 
 * <p>V nasledujúcej tabuľke uvádzame zoznam všetkých jazykových
 * identifikátorov výnimiek {@code currGRobotException} s prislúchajúcimi
 * slovenskými textami a stručným vysvetlnením príčin vzniku týchto
 * výnimiek:</p>
 * 
 * <table class="langIDTable">
 * <tr><th>Jazykový identifikátor</th><th>Text výnimky v slovenskom
 * jazyku</th><th>Stručné vysvetlenie</th></tr>
 * 
 * <tr><td><code>archiveNameOmitted</code></td><td>Názov archívu nesmie byť
 * zamlčaný.</td><td>Vzniká pri pokuse o prácu s archívom so zamlčaným
 * (nulovým – {@code valnull}) menom. Môže ísť priamo o pokus o otvorenie
 * archívu metódami triedy {@link Archív Archív}, o pokus o pridanie položky
 * sprostredkovane, napríklad triedou {@link Súbor Súbor} s {@linkplain 
 * Súbor#pripojArchív(Archív) pripojeným archívom} a podobne. Spresňujúcim
 * objektom je inštancia {@link NullPointerException} (bez textu).</td></tr>
 * 
 * <tr><td><code>archiveNotOpenForReading</code></td><td>Archív nie je
 * otvorený na čítanie.</td><td>Vzniká pri pokuse o čítanie prostredníctvom
 * inštancie typu {@link Archív Archív}, ak nebol otvorený žiadny archív na
 * čítanie.</td></tr>
 * 
 * <tr><td><code>archiveNotOpen</code></td><td>Archív nie je
 * otvorený.</td><td>Vzniká pri pokuse o čítanie alebo zápis údaju
 * v rámci inštancie typu {@link Archív Archív} v situácii, keď nebol
 * otvorený žiadny archív na čítanie ani zápis.</td></tr>
 * 
 * <tr><td><code>archiveNotOpenForWriting</code></td><td>Archív nie je
 * otvorený na zápis.</td><td>Vzniká pri pokuse o zápis prostredníctvom
 * inštancie typu {@link Archív Archív}, ak nebol otvorený žiadny archív
 * na zápis.</td></tr>
 * 
 * <tr><td><code>cannotAppendDataToEntry</code></td><td>Nemôžem pripojiť
 * údaje k položke archívu.</td><td>Vzniká pri pokuse {@linkplain 
 * Súbor#otvorNaZápis(String, boolean) otvoriť súbor na zápis} s príznakom
 * {@code pripojiť} rovným {@code valtrue}, keď je k súboru {@link 
 * Súbor#pripojArchív(Archív) pripojený archív}.</td></tr>
 * 
 * <tr><td><code>cannotCloseArchive</code></td><td>Nepodarilo sa zavrieť
 * archív: <em>«názov»</em></td><td>Metóda {@link Archív#zavri() zavri}
 * triedy {@link Archív Archív} zlyhala. Spresneňujúcim objektom
 * (zistiteľným metódou {@link Throwable#getCause() getCause}) je pôvodná
 * výnimka, ktorá spôsobila vznik chyby počas spracovania.</td></tr>
 * 
 * <tr><td><code>cannotGetAddress</code></td><td>Nepodarilo sa získať
 * adresu zariadenia.</td><td>Zlyhal pokus o zistenie adresy zariadenia
 * metódou {@link Spojenie#dajAdresu() dajAdresu} triedy {@link Spojenie
 * Spojenie}.</td></tr>
 * 
 * <tr><td><code>cannotGetCanonicalHostName</code></td><td>Nepodarilo
 * sa získať kánonický názov zariadenia (hosťa).</td><td>Zlyhal pokus
 * o zistenie kánonického názvu zariadenia metódou
 * {@link Spojenie#dajNázovHosťa(boolean) dajNázovHosťa} s hodnotou
 * parametra {@code kánonický} rovnou {@code valtrue} (v triede
 * {@link Spojenie Spojenie}).</td></tr>
 * 
 * <tr><td><code>cannotGetHostName</code></td><td>Nepodarilo sa získať
 * názov zariadenia (hosťa).</td><td>Zlyhal pokus o zistenie názvu
 * zariadenia metódou {@link Spojenie#dajNázovHosťa() dajNázovHosťa}
 * triedy {@link Spojenie Spojenie}).</td></tr>
 * 
 * <tr><td><code>cannotGetHardwareAddress</code></td><td>Nepodarilo sa získať
 * harvérovú adresu zariadenia.</td><td>Zlyhal pokus o zistenie hardvérovej
 * (obvykle MAC) adresy metódou {@link Spojenie#dajHardvérovúAdresu()
 * dajHardvérovúAdresu} triedy {@link Spojenie Spojenie}.</td></tr>
 * 
 * <tr><td><code>cannotOpenArchive</code></td><td>Nepodarilo sa otvoriť
 * archív: <em>«názov»</em></td><td>Konštruktor triedy {@link Archív
 * Archív} nemohol otvoriť archív so zadaným názvom. Spresneňujúcim
 * objektom (zistiteľným metódou {@link Throwable#getCause() getCause})
 * je pôvodná výnimka, ktorá spôsobila vznik chyby počas
 * spracovania.</td></tr>
 * 
 * <tr><td><code>commandCancelled</code></td><td>Vykonanie príkazu bolo
 * zrušené: <em>«príkaz»</em></td><td>Znamená, že vykonávanie príkazu
 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom režime}
 * bolo zrušené. Znenie príkazu je zistiteľné metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>configNotApplicable</code></td><td>Konfiguráciu nie je
 * možné použiť. Svet už bol inicializovaný!</td><td>Vzniká pri pokuse
 * o použitie automatickej konfigurácie vo svete, ktorý už
 * jestvuje.</td></tr>
 * 
 * <tr><td><code>cursorAlreadyExists</code></td><td>Kurzor so zadaným
 * menom (<em>«meno»</em>) už jestvuje.</td><td>Vzniká pri pokuse
 * o vytvorenie kurzora. Meno je zistiteľné metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>cursorNotExists</code></td><td>Kurzor so zadaným menom
 * (<em>«meno»</em>) nejestvuje.</td><td>Vzniká pri pokuse o zmenu
 * kurzora, ktorý nebol definovaný. Meno je zistiteľné metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>duplicateEntry</code></td><td>Položka „<em>«názov
 * položky»</em>“ už v archíve jestvuje.</td><td>Vzniká pri pokuse
 * o pridanie duplicitnej položky do archívu. Názov položky je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>entryNameOmitted</code></td><td>Názov položky nesmie byť
 * zamlčaný.</td><td>Vzniká pri pokuse o pridanie položky s „prázdnym“
 * (nulovým – {@code valnull}) menom do {@linkplain Archív archívu}, ak
 * meno nie je možné získať iným spôsobom (napríklad z názvu pridávaného
 * súboru). Spresňujúcim objektom je inštancia {@link NullPointerException}
 * (bez textu).</td></tr>
 * 
 * <tr><td><code>eventFactoryAlreadyExists</code></td><td>Obsluha
 * udalostí už bola definovaná!</td><td>Vzniká pri pokuse o vytvorenie
 * ďalšej obsluhy udalostí v prípade, že nebolo explicitne povolené
 * viacnásobné vytváranie obsluhy udalostí.</td></tr>
 * 
 * <tr><td><code>fileNameOmitted</code></td><td>Názov súboru nesmie byť
 * zamlčaný.</td><td>Vzniká pri rôznych príležitostiach (je generovaná
 * viacerými triedamy rámca – {@link Archív Archív}, {@link Obrázok
 * Obrázok}, {@link Plátno Plátno}, {@link Spojenie Spojenie},
 * {@link Súbor Súbor}, {@link SVGPodpora SVGPodpora}). Vzniká pri
 * pokusoch o otvorenie, čítanie, zápis alebo pridanie súboru s „prázdnym“
 * (nulovým – {@code valnull}) menom. Spresňujúcim objektom je inštancia
 * {@link NullPointerException} (bez textu).</td></tr>
 * 
 * <tr><td><code>fileNotFound</code></td><td>Súbor „<em>«súbor»</em>“
 * nebol nájdený.</td><td>Vzniká pri pokuse o otvorenie nejestvujúceho
 * súboru (<em>«súbor»</em>) na čítanie. Názov súboru je zistiteľný
 * metódou {@link #getParameter() getParameter}. Spresňujúcim objektom
 * je pôvodná systémová výnimka {@link FileNotFoundException}.</td></tr>
 * 
 * <tr><td><code>fileNotOpenForReading</code></td><td>Súbor nie je
 * otvorený na čítanie.</td><td>Vzniká pri pokuse o čítanie prostredníctvom
 * inštancie typu {@link Súbor Súbor}, ak nebol otvorený žiadny súbor na
 * čítanie.</td></tr>
 * 
 * <tr><td><code>fileNotOpenForWriting</code></td><td>Súbor nie je
 * otvorený na zápis.</td><td>Vzniká pri pokuse o zápis prostredníctvom
 * inštancie typu {@link Súbor Súbor}, ak nebol otvorený žiadny súbor
 * na zápis.</td></tr>
 * 
 * <tr><td><code>firstFileNotExists</code></td><td>Prvý súbor
 * „<em>«súbor»</em>“ nejestvuje.</td><td>Vzniká pri porovnaní súborov,
 * ak prvý súbor nejestvuje. Názov súboru je zistiteľný metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je
 * inštancia {@link FileNotFoundException} (bez textu).</td></tr>
 * 
 * <tr><td><code>firstObjectNotFile</code></td><td>Prvý súbor
 * „<em>«súbor»</em>“ nie je súbor.</td><td>Vzniká pri porovnaní súborov,
 * ak prvý z objektov nie je súbor. Názov objektu je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>imageAlreadyExists</code></td><td>Obrázok
 * „<em>«súbor»</em>“ už jestvuje.</td><td>Vzniká pri pokuse o zápis
 * obrázka do súboru, ktorý už jestvuje (<em>«súbor»</em>), ale len
 * v prípade, že nebola explicitne zadaná hodnota parametra prepísania
 * súboru. Názov súboru je zistiteľný metódou {@link #getParameter()
 * getParameter}.</td></tr>
 * 
 * <tr><td><code>imageBroken</code></td><td>Obrázok je
 * poškodený!</td><td>Znamená, že spracovanie obrázka zlyhalo, pretože
 * je pravdepodobne poškodený.</td></tr>
 * 
 * <tr><td><code>imageFileBroken</code></td><td>Obrázok „<em>«súbor»</em>“
 * je poškodený!</td><td>Znamená, že spracovanie obrázka prečítaného zo
 * súboru zlyhalo pravdepodobne preto, lebo súbor bol poškodený. Názov
 * súboru je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>imageNotFound</code></td><td>Obrázok „<em>«súbor»</em>“
 * nebol nájdený.</td><td>Znamená, že pokus o prečítanie súboru s obrázkom
 * (<em>«súbor»</em>) zlyhal z dôvodu nenájdenia súboru. Názov súboru je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>invalidDomain</code></td><td>Neplatná doména:
 * <em>«doména»</em></td><td>Zlyhal pokus o zostavenie koreňa cieľa
 * komunikácie inštancie triedy {@link Spojenie Spojenie} v dôsledku
 * zadania neplatnej domény. Doména je zistiteľná metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>invalidImageFormat</code></td><td>Neplatný formát
 * obrázka: <em>«prípona»</em></td><td>Vzniká pri pokuse o zápis obrázka
 * do súboru s nepodporovaným formátom určeným podľa prípony súboru
 * (<em>«prípona»</em>). Prípona súboru je zistiteľná metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>invalidImageTemplate</code></td><td>Šablóna názvov
 * sekvencie obrázkov „<em>«šablóna»</em>“ nie je použiteľná.</td><td>Vzniká
 * pri pokuse o čítanie alebo zápis sekvencie obrázkov vo formáte PNG,
 * pričom prípona súboru neurčuje formát PNG. Šablóna je zistiteľná
 * metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>invalidProtocol</code></td><td>Neplatný protokol:
 * <em>«protokol»</em></td><td>Zlyhal pokus o zostavenie koreňa cieľa
 * komunikácie inštancie triedy {@link Spojenie Spojenie} v dôsledku
 * zadania neplatného komunikačného protokolu. Protokol je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <!-- tr><td><code>invalidRemotePath</code></td><td>Neplatná vzdialená
 * cesta: <em>«vzdialená cesta»</em></td><td>Zlyhal pokus o zostavenie
 * koreňa cieľa komunikácie inštancie triedy {@link Spojenie Spojenie}
 * v dôsledku zadania neplatnej vzdialenej cesty. Vzdialená cesta je
 * zistiteľná metódou {@link #getParameter() getParameter}.</td></tr -->
 * 
 * <tr><td><code>namespaceContainsEquals</code></td><td>Menný priestor
 * nesmie obsahovať znak rovná sa.</td><td>Vzniká pri pokuse o prácu
 * s menným priestorom, ktorého názov obsahuje znak rovná sa. Názov
 * menného priestoru je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>namespaceEmpty</code></td><td>Menný priestor nesmie
 * byť prázdny.</td><td>Vzniká pri pokuse o prácu s menným priestorom
 * bez názvu.</td></tr>
 * 
 * <tr><td><code>namespaceEndsWithDot</code></td><td>Menný priestor
 * sa nesmie končiť bodkou.</td><td>Vzniká pri pokuse o prácu s menným
 * priestorom, ktorého názov sa končí bodkou. Názov menného priestoru
 * je zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>namespaceStartsWithDot</code></td><td>Menný priestor
 * sa nesmie začínať bodkou.</td><td>Vzniká pri pokuse o prácu s menným
 * priestorom, ktorého názov sa začína bodkou. Názov menného priestoru
 * je zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>namespaceStartsWithSemicolon</code></td><td>Menný
 * priestor sa nesmie začínať znakom komentára.</td><td>Vzniká pri
 * pokuse o prácu s menným priestorom, ktorého názov sa začína
 * bodkočiarkou (znakom komentára). Názov menného priestoru je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>negativeLineWidth</code></td><td>Hrúbka čiary nesmie
 * byť záporná!</td><td>Vzniká pri pokuse o zadanie zápornej hrúbky
 * čiary spojnice. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>negativePenWidth</code></td><td>Hrúbka čiary pera
 * nesmie byť záporná!</td><td>Vzniká pri pokuse o zadanie zápornej
 * hrúbky pera. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>negativeRadius</code></td><td>Polomer <em>«objektu»</em>
 * nesmie byť záporný!</td><td>Vzniká pri pokuse o zadanie záporného
 * polomeru kružnice, kruh, vpísanej kružnice alebo opísanej kružnice
 * (doplnené namiesto <em>«objektu»</em>), čo spresňuje aj
 * {@link #getParameter() voliteľný parameter} výnimky, ktorý môže
 * nadobúdať hodnoty <code>circle</code>, <code>filledCircle</code>,
 * <code>incircle</code> alebo <code>circumcircle</code>. Spresňujúcim
 * objektom je výnimka {@link IllegalArgumentException}
 * (bez textu).</td></tr>
 * 
 * <tr><td><code>negativeSemiaxe</code></td><td>Dĺžka poloosy
 * <em>«objektu»</em> nesmie byť záporná!</td><td>Vzniká pri pokuse
 * o zadanie záporného rozmeru poloosy elipsy, vyplnenej elipsy alebo
 * vpísanej elipsy (doplnené namiesto <em>«objektu»</em>), čo spresňuje
 * aj {@link #getParameter() voliteľný parameter} výnimky, ktorý môže
 * nadobúdať hodnoty <code>ellipse</code>, <code>filledEllipse</code>
 * alebo <code>inellipse</code>. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>noRobotWithSuchName</code></td><td>Robot so zadaným
 * menom (<em>«meno»</em>) nejestvuje.</td><td>Vzniká pri pokuse
 * o registráciu robota v konfigurácii podľa mena, ktoré nepatrí žiadnemu
 * robotovi. Meno údajného robota, ktorého registrácia zlyhala je
 * zistiteľné metódou {@link #getParameter() getParameter}. Spresňujúcim
 * objektom je výnimka {@link IllegalArgumentException}
 * (bez textu).</td></tr>
 * 
 * <tr><td><code>pathEmpty</code></td><td>Názov priečinka nesmie by
 * prázdny.</td><td>Vzniká pri pokuse o vypísanie zoznamu súborov
 * s prázdnym názvom priečinka.</td></tr>
 * 
 * <tr><td><code>pathInvalid</code></td><td>Zadaná cesta (<em>«cesta»</em>)
 * nesmeruje na priečinok.</td><td>Vzniká pri pokuse o vypísanie zoznamu
 * súborov na ceste, ktorej cieľom nie je priečinok. Cesta je zistiteľná
 * metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>pathNotFound</code></td><td>Cesta „<em>«cesta»</em>“
 * nebola nájdená.</td><td>Vzniká pri pokuse o vypísanie zoznamu súborov
 * na neplatnej ceste (<em>«cesta»</em>). Cesta je zistiteľná metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je výnimka
 * {@link FileNotFoundException} generovaná inou súčasťou
 * programovacieho rámca.</td></tr>
 * 
 * <tr><td><code>pathUnreadable</code></td><td>Cestu „<em>«cesta»</em>“
 * nie je možné čítať.</td><td>Vzniká pri pokuse o vypísanie zoznamu
 * súborov na ceste, ktorú nie je možné čítať. Cesta je zistiteľná
 * metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>propertyContainsDot</code></td><td>Názov vlastnosti
 * nesmie obsahovať bodku.</td><td>Vzniká pri pokuse o prácu
 * s vlastnosťou, ktorej názov obsahuje bodku. Názov vlastnosti je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>propertyContainsEquals</code></td><td>Názov vlastnosti
 * nesmie obsahovať znak rovná sa.</td><td>Vzniká pri pokuse o prácu
 * s vlastnosťou pri zadaní názvu vlastnosti obsahujúceho znak rovná sa.
 * Názov vlastnosti je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>propertyDuplicate</code></td><td>V konfiguračnom súbore
 * sa nachádza zdvojená vlastnosť: <em>«názov»</em></td><td>Vzniká pri
 * čítaní konfiguračného súboru, ktorý obsahuje duplikát určitej
 * vlastnosti. Názov vlastnosti je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>propertyNameEmpty</code></td><td>Názov vlastnosti
 * nesmie byť prázdny,“ prípadne „V konfiguračnom súbore sa nachádza
 * vlastnosť bez názvu.</td><td>Vzniká pri pokuse o prácu s vlastnosťou
 * bez názvu.</td></tr>
 * 
 * <tr><td><code>propertyStartsWithBracket</code></td><td>Názov vlastnosti
 * sa nesmie začínať znakom hranatej zátvorky.</td><td>Vzniká pri pokuse
 * o prácu s vlastnosťou, ktorej názov sa začína znakom hranatej zátvorky
 * ({@code [}). Názov vlastnosti je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>propertyStartsWithSemicolon</code></td><td>Názov
 * vlastnosti sa nesmie začínať znakom komentára,“ prípadne
 * „V konfiguračnom súbore sa nachádza vlastnosť začínajúca sa znakom
 * komentára.</td><td>Vzniká pri pokuse o prácu s vlastnosťou, ktorej
 * názov sa začína bodkočiarkou (znakom komentára). Názov vlastnosti je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>robotAlreadyEmployed</code></td><td>Tento robot už je
 * zamestnaný na účely tvorby inej oblasti.</td><td>Vzniká pri pokuse
 * o zamestnanie robota na účely tvorby oblasti, ktorý už je zamestnancom
 * inej oblasti.</td></tr>
 * 
 * <tr><td><code>robotHasNoName</code></td><td>Robot nemá meno,“
 * prípadne „Zadaný robot nemá meno.</td><td>Vzniká pri pokuse
 * o prečítanie konfigurácie zo súboru alebo o registráciu v konfigurácii
 * takého robota, ktorý nebol pomenovaný.</td></tr>
 * 
 * <tr><td><code>robotHeadsToOpositeDirection</code></td><td>Robot je
 * obrátený presne opačným smerom od cieľového miesta kreslenia oblúka –
 * musel by prejsť nekonečnom, aby sa do cieľa dostal.</td><td>Vzniká pri
 * pokuse o kreslenie takého oblúka, ktorý by smeroval do
 * nekonečna.</td></tr>
 * 
 * <tr><td><code>robotNameContainsEquals</code></td><td>Meno robota nesmie
 * obsahovať znak rovná sa.</td><td>Vzniká pri pokuse o priradenie takého
 * mena robotovi, ktoré obsahuje znak rovná sa. Meno robota, ktorého
 * priradenie zlyhalo je zistiteľné metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>robotNameEmpty</code></td><td>Meno robota nesmie byť
 * prázdne.</td><td>Vzniká pri pokuse o priradenie prázdneho mena
 * robotovi. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>robotNameEndsWithDot</code></td><td>Meno robota sa
 * nesmie končiť bodkou.</td><td>Vzniká pri pokuse o priradenie takého
 * mena robotovi, ktoré sa končí znakom bodky. Meno robota, ktorého
 * priradenie zlyhalo je zistiteľné metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je
 * výnimka {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>robotNameMustBeUnique</code></td><td>Meno robota
 * (<em>«meno»</em>) musí byť unikátne.</td><td>Vzniká pri pokuse
 * o priradenie takého mena robotovi, ktoré už bolo použité. Meno robota,
 * ktorého priradenie zlyhalo je zistiteľné metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>robotNameReserved</code></td><td>Zadané meno robota je
 * rezervované.</td><td>Vzniká pri pokuse o priradenie takého mena robota,
 * ktoré je rezervované. Zadané meno robota (ktorého priradenie zlyhalo)
 * je zistiteľné metódou {@link #getParameter() getParameter}. Spresňujúcim
 * objektom je výnimka {@link IllegalArgumentException} (bez
 * textu).</td></tr>
 * 
 * <tr><td><code>robotNameStartsWithDot</code></td><td>Meno robota sa
 * nesmie začínať bodkou.</td><td>Vzniká pri pokuse o priradenie takého
 * mena robotovi, ktoré sa začína znakom bodky. Meno robota, ktorého
 * priradenie zlyhalo je zistiteľné metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je
 * výnimka {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>robotNameStartsWithSemicolon</code></td><td>Meno robota
 * sa nesmie začínať znakom komentára.</td><td>Vzniká pri pokuse
 * o priradenie takého mena robotovi, ktoré sa začína znakom bodkočiarky
 * (znak komentára). Meno robota, ktorého priradenie zlyhalo je
 * zistiteľné metódou {@link #getParameter() getParameter}. Spresňujúcim
 * objektom je výnimka {@link IllegalArgumentException}
 * (bez textu).</td></tr>
 * 
 * <tr><td><code>scriptNotFound</code></td><td>Skript „<em>«skript»</em>“
 * nebol nájdený.</td><td>Znamená, že pokus o prečítanie skriptu zo súboru
 * (<em>«skript»</em>) zlyhal z dôvodu nenájdenia súboru. Názov súboru je
 * zistiteľný metódou {@link #getParameter() getParameter}. Spresňujúcim
 * objektom je pôvodná systémová výnimka
 * {@link FileNotFoundException}.</td></tr>
 * 
 * <tr><td><code>secondFileNotExists</code></td><td>Druhý súbor
 * „<em>«súbor»</em>“ nejestvuje.</td><td>Vzniká pri porovnaní súborov,
 * ak druhý súbor nejestvuje. Názov súboru je zistiteľný metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je
 * inštancia {@link FileNotFoundException} (bez textu).</td></tr>
 * 
 * <tr><td><code>secondObjectNotFile</code></td><td>Druhý súbor
 * „<em>«súbor»</em>“ nie je súbor.</td><td>Vzniká pri porovnaní súborov,
 * ak druhý z objektov nie je súbor. Názov objektu je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>sectionAlreadyExists</code></td><td>Sekcia so zadaným
 * názvom „<em>«názov»</em>“ už jestvuje.</td><td>Vzniká pri pokuse
 * o premenovanie sekcie na názov, ktorý patrí inej sekcii. Názov sekcie
 * je zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>sectionStartsWithSemicolon</code></td><td>Názov sekcie
 * sa nesmie začínať znakom komentára,“ prípadne „V konfiguračnom súbore
 * sa nachádza sekcia alebo prvok poľa vlastností začínajúci sa znakom
 * komentára.</td><td>Vzniká pri pokuse o prácu so sekciou alebo prvkom
 * poľa vlastností, ktorého názov sa začína bodkočiarkou (znakom
 * komentára). Názov objektu je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>segmentsAreParallel</code></td><td>Úsečky sú
 * paralelné.</td><td>Táto výnimka vzniká pri hľadaní priesečníkov
 * úsečiek.</td></tr>
 * 
 * <tr><td><code>soundAlreadyExists</code></td><td>Zvuk
 * „<em>«súbor»</em>“ už jestvuje.</td><td>Vzniká pri pokuse o zápis zvuku
 * do súboru, ktorý už jestvuje (<em>«súbor»</em>), ale len v prípade, že
 * nebola explicitne zadaná hodnota parametra prepísania súboru. Názov súboru
 * je zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>soundNotFound</code></td><td>Zvuk „<em>«súbor»</em>“ nebol
 * nájdený.</td><td>Znamená, že pokus o prečítanie súboru so zvukom
 * (<em>«súbor»</em>) zlyhal z dôvodu nenájdenia súboru. Názov súboru je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>soundUnreadable</code></td><td>Zvuk „<em>«súbor»</em>“
 * nie je možné čítať.</td><td>Znamená, že pokus o prečítanie súboru
 * so zvukom (<em>«súbor»</em>) zlyhal z dôvodu nesprávneho formátu,
 * prípadne iných príčin. Názov súboru je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>sourceFileNotExists</code></td><td>Zdrojový súbor
 * „<em>«súbor»</em>“ nejestvuje.</td><td>Vzniká pri kopírovaní alebo
 * pripájaní súborov, ak zdrojový súbor nejestvuje. Názov súboru je
 * zistiteľný metódou {@link #getParameter() getParameter}. Spresňujúcim
 * objektom je inštancia {@link FileNotFoundException}
 * (bez textu).</td></tr>
 * 
 * <tr><td><code>sourceObjectNotFile</code></td><td>Zdrojový súbor
 * „<em>«súbor»</em>“ nie je súbor.</td><td>Vzniká pri kopírovaní alebo
 * pripájaní súborov, ak zdrojový objekt nie je súbor. Názov objektu je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>svgImportError</code></td><td>Chyba pri spracovaní SVG
 * údajov.</td><td>Vzniká pri spracovaní SVG údajov zadaných vo forme
 * XML/SVG reťazca (do metódy {@link SVGPodpora#pridajSVG(String)
 * pridajSVG}). Spresneňujúcim objektom (zistiteľným metódou {@link 
 * Throwable#getCause() getCause}) je pôvodná výnimka, ktorá spôsobila
 * vznik chyby počas spracovania.</td></tr>
 * 
 * <tr><td><code>svgReadError</code></td><td>Chyba pri spracovaní SVG
 * súboru „<em>«meno»</em>“.</td><td>Vzniká pri spracovaní SVG súboru.
 * Spresneňujúcim objektom (zistiteľným metódou
 * {@link Throwable#getCause() getCause}) je pôvodná výnimka, ktorá
 * vyvolala chybu spracovania.</td></tr>
 * 
 * <tr><td><code>svgWriteError</code></td><td>Chyba pri zápise SVG
 * súboru „<em>«meno»</em>“.</td><td>Vzniká pri zapisovaní SVG súboru.
 * Spresneňujúcim objektom (zistiteľným metódou
 * {@link Throwable#getCause() getCause}) je pôvodná výnimka, ktorá
 * vyvolala chybu spracovania.</td></tr>
 * 
 * <tr><td><code>targetFileExists</code></td><td>Cieľový súbor
 * „<em>«súbor»</em>“ už jestvuje.</td><td>Vzniká pri kopírovaní súborov,
 * ak cieľový súbor jestvuje a nebol explicitne zadaný príznak prepísania
 * súboru. Názov súboru je zistiteľný metódou
 * {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>targetObjectNotFile</code></td><td>Cieľový súbor
 * „<em>«súbor»</em>“ nie je súbor.</td><td>Vzniká pri kopírovaní alebo
 * pripájaní súborov, ak cieľový objekt nie je súbor. Názov objektu je
 * zistiteľný metódou {@link #getParameter() getParameter}.</td></tr>
 * 
 * <tr><td><code>unsupportedDataType</code></td><td>Počas čítania zo
 * súboru nastal pokus o vloženie údajov do premennej nasledujúceho
 * nepodporovaného údajového typu: <em>«meno triedy»</em></td><td>Vzniká
 * pri pokuse o prečítanie údajov nepodporovaného údajového typu zo
 * súboru. Ekvivalentný druh výnimky nevzniká pri pokuse o zápis údajov
 * do súboru, pretože pri zápise je každý objekt jednoducho prevedený
 * na reťazec metódou <code>toString</code>. Kanonický názov triedy,
 * ktorej čítanie zlyhalo je zistiteľný metódou
 * {@link #getParameter() getParameter}. Spresňujúcim objektom je výnimka
 * {@link IllegalArgumentException} (bez textu).</td></tr>
 * 
 * <tr><td><code>worldAlreadyExists</code></td><td>Svet už
 * jestvuje!</td><td>Vzniká pri pokuse o inicializáciu nového robota
 * (v jestvujúcom svete), ktorý sa pokúša predefinovať rozmery
 * kresliacich plátien.</td></tr>
 * 
 * <tr><td><code>zeroPositionCannotBeChanged</code></td><td>Polohu stredu
 * nie je možné meniť!</td><td>Vzniká pri pokuse o zmenu polohy inštancie
 * {@link Poloha#stred stred}.</td></tr>
 * 
 * </table>
 */
@SuppressWarnings("serial")
public class GRobotException extends RuntimeException
{
	// Regulárny výraz na vyhľadanie pasáží súvisiacich s výnimkami v rámci
	// celého programovacieho rámca:
	// (?<!Runtime|IllegalArgument|FileNotFound|IO|catch \(|@|NullPointer)Exception


	/**
	 * <p>Trieda zoznamu chybových hlásení a evidencie výnimiek vypísaných
	 * a/alebo vzniknutých počas činnosti programovacieho rámca. Aktívna
	 * inštancia denníka je dostupná prostredníctvom statického atribútu
	 * {@linkplain GRobotException#denník denník}.</p>
	 * 
	 * @see Chyba
	 * @see #denník
	 * @see #poslednáVýnimka()
	 */
	@SuppressWarnings("serial")
	public static class Denník extends Zoznam<Chyba>
	{
		/**
		 * <p>Pripojí aktuálny obsah denníka do súboru so zadaným menom.
		 * Súbor denníka musí mať príponu {@code .log}. Ak ju nemá,
		 * metóda ju automaticky doplní.</p>
		 * 
		 * @param názovSúboru názov súboru s denníkom na pripojenie
		 *     aktuálneho obsahu tejto inštancie denníka
		 */
		public void pripoj(String názovSúboru)
		{
			// Poznámka: Pri zápise štandardných súborov typu konfiguračné
			//     súbory, denníky a podobné záležitosti, ktoré majú
			//     štandardnú príponu vždy kontrolujte, či je táto prípona
			//     uvedená v názve súboru a ak nie, tak ju pripojte. Je to
			//     bezpečnejšie. Jeden príklad za všetky: Predstavte si, že
			//     programátor píše kód, na začiatku ktorého registruje
			//     konfiguračný súbor s rovnakým názvom ako názov hlavnej
			//     triedy. Či už z dôvodu nočnej únavy, alebo nedostatku
			//     rannej kávy v úsilí zjednodušiť si veci skopíruje názov
			//     z dialógu triedy Uložiť ako… a zabudne prepísať príponu
			//     z .java na .cfg. Neuvedomí si to a program spustí. Po
			//     ukončení programu s úžasom zistí, že zdrojový kód jeho
			//     triedy je fuč a namiesto neho tam má zapísanú konfiguráciu.
			//     Ak to bola prvá vec, ktorú v hlavnej triede napísal, tak
			//     veľa nestratil, ak mal zdrojový kód zálohovaný, tiež asi
			//     veľa nestratil, ale ak my zabezpečíme, pridanie korektnej
			//     prípony, tak strata ani nenastane.
			if (!názovSúboru.endsWith(".log"))
				názovSúboru += ".log";

			// TODO – otestuj
			try
			{
				File súborDenníka = new File(názovSúboru);

				// Súbor chceme pripojiť:
				BufferedWriter pripoj = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
						súborDenníka, true), "UTF-8"));

				for (Chyba ch : this)
				{
					if (null != ch.správa)
						pripoj.write(ch.správa);
					if (null != ch.výnimka)
					{
						pripoj.write(ch.výnimka.getClass().getName());
						pripoj.write(ch.výnimka.getMessage());

						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						ch.výnimka.printStackTrace(pw);
						pripoj.write(sw.toString());
					}
				}
				pripoj.close();
			}
			catch (Exception e)
			{
				Svet.pípni();
				e.printStackTrace();
			}
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link Denník Denník}.</p> */
	public static class Dennik extends Denník {}


	/**
	 * <p>Denník chybových správ generovaných rámcom. Denník sa začína
	 * utvárať v čase inicializácie programovacieho rámca a po ukončení
	 * aplikácie je vymazaný.</p>
	 * 
	 * @see Denník
	 * @see Chyba
	 * @see #poslednáVýnimka()
	 */
	public final static Dennik denník = new Dennik();

	/** <p><a class="alias"></a> Alias pre {@link #denník denník}.</p> */
	public final static Dennik dennik = denník;


	/**
	 * <p>Trieda uchovávajúca záznam denníka. Obsahuje dve údajové polia
	 * (atribúty), z ktorých je relevantné vždy len jedno.</p>
	 * 
	 * @see Denník
	 * @see #denník
	 * @see #poslednáVýnimka()
	 */
	public static class Chyba
	{
		// TODO – pridaj do denníka dátum a čas štartu aplikácie…

		/** <p>Jednoduché textové chybové hlásenie programovacieho rámca.</p> */
		public final String správa;

		/** <p>Inštancia výnimky generovanej programovacím rámcom.</p> */
		public final Exception výnimka;

		/** <p><a class="alias"></a> Alias pre {@link #správa správa}.</p> */
		public final String sprava;

		/** <p><a class="alias"></a> Alias pre {@link #výnimka výnimka}.</p> */
		public final Exception vynimka;

		// Úplný konštruktor.
		private Chyba(String správa, Exception výnimka)
		{
			this.správa = správa;
			this.výnimka = výnimka;

			this.sprava = správa;
			this.vynimka = výnimka;

			denník.pridaj(this);
		}
	}


	// Príznak výpisu chybových hlásení na chybovú konzolu procesu počas
	// vykonávania programu.
	/*packagePrivate*/ static boolean vypíšChybovéHlásenia = false;

	// Posledná výnimka generovaná rámcom. Dá sa prevziať rovnomennou metódou.
	private static GRobotException poslednáVýnimka = null;


	/**
	 * <p>Vráti objekt s detailami o poslednej výnimke vygenerovanej
	 * programovacím rámcom.</p>
	 * 
	 * @return inštancia naposledy generovanej výnimky programovacím rámcom
	 *     (môže byť aj {@code valnull}, ak ešte žiadna výnimka nevznikla)
	 * 
	 * @see Denník
	 * @see Chyba
	 * @see #denník
	 */
	public static GRobotException poslednáVýnimka() { return poslednáVýnimka; }

	/** <p><a class="alias"></a> Alias pre {@link #poslednáVýnimka poslednáVýnimka}.</p> */
	public static GRobotException poslednaVynimka() { return poslednáVýnimka; }


	// Výpis textového chybového hlásenia – napríklad pri zadaní nesprávneho
	// parametra, ktorý bude ignorovaný alebo výpis doplňujúceho chybového
	// hlásenia pri vzniku výnimky.
	/*packagePrivate*/ static void vypíšChybovéHlásenie(String s)
	{
		if (vypíšChybovéHlásenia)
			System.err.println(s);

		new Chyba(s, null);
	}

	// Výpis textového chybového hlásenia.
	/*packagePrivate*/ static void vypíšChybovéHlásenie(
		String s, boolean vždy)
	{
		if (vždy || vypíšChybovéHlásenia)
			System.err.println(s);

		new Chyba(s, null);
	}


	// Výpis podrobných chybových hlásení pri vzniku výnimiek.
	/*packagePrivate*/ static void vypíšChybovéHlásenia(Exception e)
	{
		if (vypíšChybovéHlásenia)
		{
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		new Chyba(null, e);
	}

	// Výpis podrobných chybových hlásení pri vzniku výnimiek.
	/*packagePrivate*/ static void vypíšChybovéHlásenia(
		Exception e, boolean vždy)
	{
		if (vždy || vypíšChybovéHlásenia)
		{
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		new Chyba(null, e);
	}


	// Jazykový identifikátor
	private String langIDString;

	// Voliteľný argument správy
	private String parameter;

	/**
	 * <p>Konštruktor umožňujúci vytvorenie výnimky s textom správy
	 * a univerzálnym jazykovým identifikátorom.</p>
	 * 
	 * @param message text správy (spravidla v slovenčine) dostupný
	 *     prostredníctvom zdedenej metódy {@link Throwable#getMessage()}
	 * @param langIDString univerzálny jazykový identifikátor dostupný
	 *     prostredníctvom metódy {@link #getLangIDString}
	 */
	public GRobotException(String message, String langIDString)
	{
		super(message);
		this.langIDString = langIDString;
		this.parameter = null;

		poslednáVýnimka = this;
		new Chyba(null, this);
	}

	/**
	 * <p>Konštruktor umožňujúci vytvorenie výnimky s textom správy
	 * univerzálnym jazykovým identifikátorom a ďalším objektom výnimky,
	 * ktorý spresňuje okolnosti vzniku tejto výnimky. Spresňujúcim
	 * objektom môže byť napríklad: {@link IllegalArgumentException},
	 * {@link FileNotFoundException}, {@link IOException},
	 * {@link NullPointerException}…</p>
	 * 
	 * @param message text správy (spravidla v slovenčine) dostupný
	 *     prostredníctvom zdedenej metódy {@link Throwable#getMessage()}
	 * @param langIDString univerzálny jazykový identifikátor dostupný
	 *     prostredníctvom metódy {@link #getLangIDString}
	 * @param cause spresňujúci objekt výnimky dostupný prostredníctvom
	 *     metódy {@link Throwable#getCause() getCause}
	 */
	public GRobotException(String message, String langIDString,
		Throwable cause)
	{
		super(message, cause);
		this.langIDString = langIDString;
		this.parameter = null;

		poslednáVýnimka = this;
		new Chyba(null, this);
	}

	/**
	 * <p>Konštruktor umožňujúci vytvorenie výnimky s textom správy,
	 * univerzálnym jazykovým identifikátorom a parametrom.</p>
	 * 
	 * @param message text správy (spravidla v slovenčine) dostupný
	 *     prostredníctvom zdedenej metódy {@link Throwable#getMessage()}
	 * @param langIDString univerzálny jazykový identifikátor dostupný
	 *     prostredníctvom metódy {@link #getLangIDString()}
	 * @param parameter parameter dopĺňajúci informáciu o vzniknutej
	 *     výnimke (napríklad meno súboru) dostupný prostredníctvom
	 *     metódy {@link #getParameter() getParameter}
	 */
	public GRobotException(String message, String langIDString,
		String parameter)
	{
		super(message);
		this.langIDString = langIDString;
		this.parameter = parameter;

		poslednáVýnimka = this;
		new Chyba(null, this);
	}

	/**
	 * <p>Konštruktor umožňujúci vytvorenie výnimky s textom správy,
	 * univerzálnym jazykovým identifikátorom, parametrom a ďalším
	 * objektom výnimky, ktorý spresňuje okolnosti vzniku tejto výnimky.
	 * Spresňujúcim objektom môže byť napríklad:
	 * {@link IllegalArgumentException}, {@link FileNotFoundException},
	 * {@link IOException}, {@link NullPointerException}…</p>
	 * 
	 * @param message text správy (spravidla v slovenčine) dostupný
	 *     prostredníctvom zdedenej metódy {@link Throwable#getMessage()}
	 * @param langIDString univerzálny jazykový identifikátor dostupný
	 *     prostredníctvom metódy {@link #getLangIDString()}
	 * @param parameter parameter dopĺňajúci informáciu o vzniknutej
	 *     výnimke (napríklad meno súboru) dostupný prostredníctvom
	 *     metódy {@link #getParameter() getParameter}
	 * @param cause spresňujúci objekt výnimky dostupný prostredníctvom
	 *     metódy {@link Throwable#getCause() getCause}
	 */
	public GRobotException(String message, String langIDString,
		String parameter, Throwable cause)
	{
		super(message, cause);
		this.langIDString = langIDString;
		this.parameter = parameter;

		poslednáVýnimka = this;
		new Chyba(null, this);
	}

	/**
	 * <p>Metóda vracajúca univerzálny jazykový identifikátor.</p>
	 * 
	 * @return univerzálny jazykový identifikátor využiteľný pri
	 *     prekladoch prostredia
	 */
	public String getLangIDString() { return langIDString; }

	/**
	 * <p>Metóda vracajúca nepovinný parameter spresňujúci vznik výnimky
	 * alebo {@code valnull}.</p>
	 * 
	 * @return nepovinný spresňujúci parameter výnimky, napríklad meno
	 *     súboru
	 */
	public String getParameter() { return parameter; }

	/**
	 * <p>Metóda vracajúca stopu zásobníka volaní metód v čase vzniku tejto
	 * výnimky v reťazcovej podobe. Termín stopa zásobníka znamená, že ak
	 * je obsah zásobníka príliš veľký, tak nejde o úplný výpis obsahu
	 * zásobníka, ale len o stopu (trasu) vracajúcu sa späť od miesta vzniku
	 * výnimky po určitú maximálnu hĺbku.</p>
	 * 
	 * @return reťazec s výpisom stopy (trasy) volaní metód v čase vzniku
	 *     tejto výnimky
	 */
	public String stackTraceToString()
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * <p>Metóda vracajúca stopu zásobníka volaní metód v čase vzniku
	 * zadanej výnimky v reťazcovej podobe. Podrobnosti nájdete v opise
	 * {@linkplain #stackTraceToString() dynamickej verzie tejto metódy}.
	 * Táto metóda funguje rovnako, len slúži na získanie výpisu stopy
	 * zásobníka z ľubovoľnej inštancie {@link Throwable Throwable}.
	 * (Čiže aj z inštancií výnimiek iného typu, než {@link GRobotException
	 *  GRobotException}.</p>
	 * 
	 * @param t ľubovoľná inštancia {@link Throwable Throwable}
	 * @return reťazec s výpisom stopy (trasy) volaní metód v čase vzniku
	 *     zadanej výnimky
	 */
	public static String stackTraceToString(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
