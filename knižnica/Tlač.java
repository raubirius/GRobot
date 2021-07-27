
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2021 by Roman Horváth
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;

import java.awt.image.BufferedImage;

import java.awt.print.Pageable;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.util.Vector;

/**
 * <p>Trieda prepájajúca kresliace funkcie robota s rozhraniami tlačového
 * výstupu. Prostredníctvom tejto triedy môžete nechať zvolený robot
 * tlačiť, pričom (pripomeňme, že) v závislosti od aktuálnych možností
 * (a inštalácie) operačného systému môže ísť aj o virtuálnu tlačiareň.
 * Napríklad o zachytávač obsahu ukladajúci tlačené objekty do PDF
 * dokumentu.</p>
 * 
 * <p>Trieda je abstraktná s jedinou abstraktnou metódou – {@link 
 * #kresli(int, PageFormat) kresli(strana, formát)}. V nej má programátor
 * implementovať „nakreslenie“ – rendrovanie každej tlačenej strany (podľa
 * prijatých parametrov). Je spúšťaná automaticky pre každú tlačenú stranu
 * (pozri aj informácie pri metóde {@link #print(Graphics, PageFormat, int)
 * print}, ktorá je implementáciou rozhrania {@link Printable Printable}),
 * pričom používateľ nemusí zvoliť tlač všetkých strán, preto musí byť každá
 * strana rendrovaná samostatne a principiálne tak, aby výsledok nebol
 * skreslený vynechaním tlačenia niektorých strán používateľom (čiže obsah
 * každej strany musí byť stabilný bez ohľadu na to, či boli alebo neboli
 * vykreslené ostatné strany). To musí mať programátor pri rendrovaní
 * strán na pamäti.</p>
 * 
 * <p> </p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje tlač dvoch stránok s pomocou nástrojov
 * programovacieho rámca.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;
	{@code kwdimport} {@code kwdstatic} knižnica.{@code currTlač}.{@link Tlač#cm cm};

	{@code kwdimport} java.awt.print.{@link PageFormat PageFormat};
	{@code kwdimport} java.awt.print.{@link Paper Paper};
	{@code kwdimport} java.awt.print.{@link PrinterException PrinterException};
	{@code kwdimport} java.awt.print.{@link PrinterJob PrinterJob};
	{@code kwdimport} javax.print.{@link javax.print.PrintService PrintService};

	{@code comm// Test tlače – trieda s vnútornou konkretizáciou abstraktnej triedy Tlač.}
	{@code kwdpublic} {@code typeclass} TestTlače {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Písmo, ktoré použijeme na posledný vykresľovaný odsek ukážky tlače:}
		{@code kwdprivate} {@code kwdstatic} {@link Písmo Písmo} písmo = {@code kwdnew} {@link Písmo#Písmo(String, int, double) Písmo}({@code srg"Cambria"}, {@link Písmo Písmo}.{@link Písmo#NORMÁLNE NORMÁLNE}, {@code num14});

		{@code comm// Konkretizácia triedy Tlač.}
		{@code kwdpublic} {@code currTlač} tlač = {@code kwdnew} {@link Tlač#Tlač(GRobot, int) Tlač}({@code valthis}, {@code num2})
		{
			{@code kwdpublic} {@code typevoid} {@link Tlač#kresli(int, PageFormat) kresli}({@code typeint} strana, {@link PageFormat PageFormat} formát)
			{
				{@code typedouble} rozsahX = formát.{@link PageFormat#getWidth() getWidth}() / {@code num2} - {@code num1.5} * {@link Tlač#cm cm};
				{@code typedouble} rozsahY = formát.{@link PageFormat#getHeight() getHeight}() / {@code num2} - {@code num1.5} * {@link Tlač#cm cm};

				{@code kwdswitch} (strana)
				{
				{@code kwdcase} {@code num0}:
					{@code comm// Na prvej strane budú jednoduché tvary rozmiestnené}
					{@code comm// v strede a rohoch.}

					{@link GRobot#pomer(double) pomer}({@code num2}); {@code comm// (úprava pomeru strán obdĺžnika a elipsy)}
					{@link GRobot#krúžok() krúžok}(); {@code comm// (krúžok v strede)}

					{@link GRobot#skočNa(double, double) skočNa}(rozsahX, rozsahY); {@link GRobot#štvorec() štvorec}();   {@code comm// (štvorec vpravo hore)}
					{@link GRobot#skočNa(double, double) skočNa}(-rozsahX, rozsahY); {@link GRobot#hviezda() hviezda}();  {@code comm// (hviezda vľavo hore)}
					{@link GRobot#skočNa(double, double) skočNa}(rozsahX, -rozsahY); {@link GRobot#obdĺžnik() obdĺžnik}(); {@code comm// (obdĺžnik vpravo dole)}
					{@link GRobot#skočNa(double, double) skočNa}(-rozsahX, -rozsahY); {@link GRobot#elipsa() elipsa}();  {@code comm// (elipsa vľavo dole)}
					{@code kwdbreak};

				{@code kwdcase} {@code num1}:
					{@code comm// Druhá strana bude obsahovať texty rendrované dvomi spôsobmi.}

					{@code comm// Textová informácia v tretine hornej časti papiera}
					{@code comm// (počítanej od stredu smerom nahor):}
					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, -rozsahY / {@code num3});
					{@link GRobot#text(String) text}({@code srg"Ukážka tlače… Písmo:"});
					{@link GRobot#odskoč(double) odskoč}({@code num0.75} * {@link Tlač#cm cm});
					{@link GRobot#text(String) text}({@link GRobot#písmo() písmo}().{@link Písmo#getFamily() getFamily}() + {@code srg", "} + {@link GRobot#písmo písmo}.{@link Písmo#getSize() getSize}() + {@code srg" pt"});

					{@code comm// Zmeníme písmo, vypíšeme jeho vlastnosti do tretiny spodnej}
					{@code comm// časti papiera (opäť počítanej od stredu, ale teraz nadol)…}
					{@link GRobot#písmo(Font) písmo}(písmo);
					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, rozsahY / {@code num3});
					{@link GRobot#text(String) text}({@code srg"^^ "} + {@link GRobot#písmo() písmo}().{@link Písmo#getFamily() getFamily}() + {@code srg", "} +
						{@link GRobot#písmo písmo}.{@link Písmo#getSize() getSize}() + {@code srg" pt ^^"});

					{@code comm// …a vykreslíme nasledujúci odsek do rámca (rámčeka) so}
					{@code comm// želanými rozmermi (určenými nižšie; poznámka: text je}
					{@code comm// upravený voľný preklad malej časti pôvodného zmysluplného}
					{@code comm// latinského textu, z ktorého vychádza novodobý nezmyselný}
					{@code comm// text Lorem Ipsum):}
					{@link String String} text = {@code srg"Musím vysvetliť, ako sa celá táto mylná idea "} +
						{@code srg"o odsúdení potešenia a chvále bolesti zrodila. Nikto "} +
						{@code srg"neodmieta, nemá rád, ani sa nevyhýba potešeniu, "} +
						{@code srg"pretože je príjemné, ale tí, ktorí nevedia, ako sa "} +
						{@code srg"ním dostatočne nasýtiť, narážajú na mimoriadne "} +
						{@code srg"bolestivé následky. Tiež nikto nemiluje, nevyhľadáva, "} +
						{@code srg"ani si neželá dobrovoľne zažiť utrpenie, pretože to "} +
						{@code srg"znamená bolesť, ale niekedy môže prekonanie bolesti "} +
						{@code srg"a utrpenia priniesť veľké potešenie. Vezmime si "} +
						{@code srg"triviálny príklad. Kto sa venuje namáhavému fyzickému "} +
						{@code srg"cvičeniu, získa nielen uznanie. Nikto však nemá právo "} +
						{@code srg"hľadať chybu v človeku, ktorý si užíva potešenie, "} +
						{@code srg"ktoré nemá nepríjemné následky."};
					{@code typeint} šírka = ({@code typeint})rozsahX, výška = ({@code typeint})({@code num3.5} * {@link Tlač#cm cm});

					{@code comm// (Niekoľko poznámok: Pred kreslením/výstupom textu nakreslíme}
					{@code comm// obdĺžnik, do ktorého bude text kreslený. Meno kresliTextDo}
					{@code comm// musíme „nasmerovať“ na inštanciu robota/testu tlače, lebo}
					{@code comm// predvolene by smerovala do inštancie tlače/triedy Tlač – na}
					{@code comm// jej statickú metódu kresliTextDo. Výška a šírka sú vo vnútri}
					{@code comm// metódy TestTlače.this.kresliTextDo vynásobené dvomi, lebo}
					{@code comm// metóda Tlač.kresliTextDo prijíma celkové rozmery rámca, nie}
					{@code comm// polomery vpísanej elipsy ako mnohé metódy robota.)}

					{@link GRobot#skočNa(double, double) skočNa}({@code num0}, {@code num2} * rozsahY / {@code num3});
					{@link GRobot#obdĺžnik(double, double) obdĺžnik}(šírka, výška);
					TestTlače.{@code valthis}.kresliTextDo(text, šírka, výška);
					{@code kwdbreak};
				}
			}
		};

		{@code comm// Zoznam služieb (virtuálnych, sieťových, lokálnych, prípadne iných}
		{@code comm// tlačiarní).}
		{@code kwdprivate} {@link javax.print.PrintService PrintService}[] služby;

		{@code comm// Uzavretie statickej metódy Tlač.kresliTextDo tak, aby komunikovala}
		{@code comm// s aktuálnym robotom a tlačovým rozhraním:}
		{@code kwdprivate} {@code typeint} kresliTextDo({@link String String} text, {@code typeint} šírka, {@code typeint} výška)
		{
			{@code comm// Ak chceme použiť robot na externé kreslenie, tak je dôležité}
			{@code comm// použiť dvojicu metód začniKreslenie a skončiKreslenie:}
			{@link GRobot#začniKreslenie() začniKreslenie}();

			{@code comm// Blok textu rozbijeme na slová vopred, aby sme mohli upraviť}
			{@code comm// jeho vlastnosti (v tomto prípade len jemné zriedenie riadkovania):}
			{@code currTlač}.{@link BlokSlov BlokSlov} blokSlov = {@code currTlač}.{@link Tlač#rozbiNaSlová(Graphics, CharSequence, int) rozbiNaSlová}(
				{@link GRobot#grafika() grafika}(), text, {@code num2} * šírka);
			blokSlov.{@link BlokSlov#riadkovanie riadkovanie} = {@code num1.15};

			{@code comm// Použitie statickej metódy Tlač.kresliTextDo:}
			{@code comm//   • prijíma vopred pripravený blokSlov s ktorým má pracovať,}
			{@code comm//     preto je dôležité, aby bol druhý parameter null,}
			{@code comm//   • objekt grafiky je rovnaký, aký má momentálne nastavený robot}
			{@code comm//     (čo je v prípade tlače kontext nasmerovaný priamo na tlač),}
			{@code comm//   • obdĺžnik, do ktorého sa má text zmestiť je prepočítaný podľa}
			{@code comm//     polohy robota a požadovaných rozmerov (zadaných do parametrov}
			{@code comm//     šírka a výška tejto metódy),}
			{@code comm//   • kreslenie textov presahujúcich spodný okraj obdĺžnika je}
			{@code comm//     zamedzené hodotnou true predposledného parametra (nekresliMimo)}
			{@code comm//   • a začína sa prvým riadkom (index 0 v posledom parametri).}
			{@code typeint} i = {@code currTlač}.{@link #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov, boolean, int) kresliTextDo}({@link GRobot#grafika() grafika}(), {@code valnull},
				({@code typeint}){@link Svet Svet}.{@link Svet#prepočítajX(double) prepočítajX}({@link GRobot#polohaX() polohaX}() - šírka),
				({@code typeint}){@link Svet Svet}.{@link Svet#prepočítajY(double) prepočítajY}({@link GRobot#polohaY() polohaY}() + výška),
				{@code num2} * šírka, {@code num2} * výška, blokSlov, {@code valtrue}, {@code num0});

			{@code comm// (Ukončenie kreslenia robotom – začaté vyššie volaním začniKreslenie.)}
			{@link GRobot#skončiKreslenie() skončiKreslenie}();
			{@code kwdreturn} i;
		}

		{@code comm// Konštruktor.}
		{@code kwdprivate} TestTlače()
		{
			{@link GRobot#veľkosť(double) veľkosť}({@code num0.5} * cm);
			služby = {@link PrinterJob PrinterJob}.{@link PrinterJob#lookupPrintServices() lookupPrintServices}();
			vypíšSlužby();
		}

		{@code comm// Výpis interaktívneho zoznamu aktuálne dostupných služieb. Kliknutie}
		{@code comm// na písmeno textu služby (tak fungujú aktívne slová vnútornej konzoly;}
		{@code comm// kliknutie ma medzeru je ignorované…) službu aktivuje.}
		{@code kwdprivate} {@code typevoid} vypíšSlužby()
		{
			{@link Svet Svet}.{@link Svet#vymažTexty() vymažTexty}();
			{@link javax.print.PrintService PrintService} aktuálnaSlužba = tlač.{@link Tlač#úloha() úloha}().{@link PrinterJob#getPrintService() getPrintService}();

			{@code typeint} i = {@code num0};
			{@code kwdfor} ({@link javax.print.PrintService PrintService} služba : služby)
			{
				{@code kwdif} (služba.{@link javax.print.PrintService#equals(Object) equals}(aktuálnaSlužba))
					{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#zelená zelená}); {@code kwdelse} {@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#modrá modrá});

				{@code comm// Do aktívneho slova „zakódujeme“ index služby:}
				{@link Svet Svet}.{@link Svet#vypíšAktívneSlovo(String, Object[]) vypíšAktívneSlovo}(i + {@code srg""}, služba.{@link javax.print.PrintService#getName() getName}());
				{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}();
				++i;
			}
		}

		{@code comm// Obsluha kliknutia.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			{@code comm// Kliknutie na aktívne slovo (ktoré je v našom prípade index služby)}
			{@code comm// aktivuje prislúchajúcu tlačovú službu.}
			{@link String String} aktívneSlovo = {@link Svet Svet}.{@link Svet#myšVAktívnomSlove() myšVAktívnomSlove}();
			{@code kwdif} ({@code valnull} != aktívneSlovo)
			{
				{@code typeint} i = {@link Integer Integer}.{@link Integer#parseInt(String) parseInt}(aktívneSlovo);
				{@code kwdtry}
				{
					tlač.{@link Tlač#úloha() úloha}().{@link PrinterJob#setPrintService(javax.print.PrintService) setPrintService}(služby[i]);
					vypíšSlužby();
				}
				{@code kwdcatch} ({@link PrinterException PrinterException} pe)
				{
					{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}({@link Farebnosť#červená červená});
					{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(pe.{@link PrinterException#getMessage() getMessage}());
				}
			}
			{@code comm// Pravé tlačidlo na prázdnu plochu aktivuje tlač:}
			{@code kwdelse} {@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši(int) tlačidloMyši}({@link Konštanty#PRAVÉ PRAVÉ}))
			{
				{@code kwdif} (tlač.{@link Tlač#tlačDialógom(String) tlačDialógom}({@code srg"Ukážka tlače"}))
				{
					{@code comm// Tlač bola vykonaná v poriadku.}
					{@code comm// …}
				}
			}
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu(String) použiKonfiguráciu}({@code srg"TestTlače.cfg"});
			{@code kwdnew} TestTlače();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p>Okno miniaplikácie môže po spustení vyzerať napríklad takto:</p>
 * 
 * <p><image>tlac-zoznam-sluzieb.png<alt/>Okno po spustení.</image>Ukážka
 * okna s výpisom dostupných tlačových služieb.</p>
 * 
 * <p>Takto by mali vyzerať stránky vytlačeného dokumentu (rozdiel môže byť
 * v predvolenom fonte, ktorým sú písané prvé texty „Ukážka tlače… atď.“):</p>
 * 
 * <p><image>tlac-vysledok-prikladu-small.png<alt/>Výsledok
 * tlače.</image>Výsledok tlače (<a href="resources/tlac-vysledok-prikladu.png"
 * target="_blank">ukážka vo vyššom rozlíšení</a>).</p>
 * 
 * <p> </p>
 */
public abstract class Tlač implements Printable, Pageable
{
	/**
	 * <p>Konštanta, násobenie ktorou prepočíta palce na tlačové body.
	 * Príklad: Výsledkom {@code num1.0}{@code  * }{@link Tlač Tlač}{@code 
	 * .}{@code currinch} bude posun v bodoch ekvivalentný jednému palcu.
	 * (Poznámka: Prirodzene, delenie touto konštantou prepočíta body na
	 * palce.)</p>
	 */
	public final static double inch = 72.0;

	/**
	 * <p>Konštanta, násobenie ktorou prepočíta centimetre na tlačové body.
	 * Príklad: Výsledkom {@code num1.0}{@code  * }{@link Tlač Tlač}{@code 
	 * .}{@code currcm} bude posun v bodoch ekvivalentný jednému centimetru.
	 * (Poznámka: Prirodzene, delenie touto konštantou prepočíta body na
	 * centimetre.)</p>
	 */
	public final static double cm = 72.0 / 2.54;


	// Aktuálna tlačová úloha. (V podstate by mala byť len jedna (systémovo).
	// Je získavaná automaticky, ale tu pre istotu budeme hovoriť
	// o „aktuálnej.“)
	private PrinterJob úloha;

	// Naposledy použitý obrázok na kreslenie. Obrázky sú ukladané do
	// vnútorného zoznamu a recyklované podľa potreby podľa aktuálnych
	// nastavení strán.
	private Obrazok obrázok = null;

	// Robot spárovaný s touto inštanciou tlače.
	private GRobot robot;

	// Zoznam obrázkov na recykláciu. Každý obrázok zodpovedá unikátnemu
	// rozmeru tlačenej strany. (Je možné a pravdepodobné, že v zozname
	// bude len jedna alebo dve inštancie obrázka – napríklad horizontálny
	// a vertikálny rozmer A4.)
	private final Vector<Obrazok> obrázky = new Vector<>();

	// Aktuálny počet strán.
	private int početStrán;

	// Predvolené nastavenie strany.
	private PageFormat nastavenieStrany;

	/**
	 * <p>Konštruktor vytvárajúci inštanciu tlače, ktorá bude používať
	 * zadaný robot na kreslenie (rendrovanie) tlačených stránok a ktorá
	 * obsahuje predvolene jednu stranu.</p>
	 * 
	 * <p>Robot je s touto inštanciou tlače spárovaný a vždy pred
	 * rendrovaním strany automaticky nastavený podľa kontextu.</p>
	 * 
	 * @param robot kresliaci robot používaný na rendrovanie obsahu strán
	 * 
	 * @see #Tlač(GRobot, int)
	 * @see #kresli(int, PageFormat)
	 * @see #print(Graphics, PageFormat, int)
	 */
	public Tlač(GRobot robot)
	{
		this(robot, 1);
	}

	/**
	 * <p>Konštruktor vytvárajúci inštanciu tlače, ktorá bude používať
	 * zadaný robot na kreslenie (rendrovanie) tlačených stránok a ktorá
	 * obsahuje zadaný počet strán.</p>
	 * 
	 * <p>Robot je s touto inštanciou tlače spárovaný a vždy pred
	 * rendrovaním strany automaticky nastavený podľa kontextu.</p>
	 * 
	 * @param robot kresliaci robot používaný na rendrovanie obsahu strán
	 * @param početStrán počet strán dokumentu
	 * 
	 * @see #Tlač(GRobot)
	 * @see #kresli(int, PageFormat)
	 * @see #print(Graphics, PageFormat, int)
	 */
	public Tlač(GRobot robot, int početStrán)
	{
		this.robot = robot;
		this.početStrán = početStrán;
		úloha = PrinterJob.getPrinterJob();
		úloha.setPrintable(this);
		úloha.setPageable(this);
		nastavenieStrany = úloha.defaultPage();

		Paper papier = nastavenieStrany.getPaper();
		papier.setImageableArea(0, 0,
			nastavenieStrany.getWidth(), nastavenieStrany.getHeight());
		nastavenieStrany.setPaper(papier);

		// Predvolené nastavenie (pozícia a veľkosť) tzv. „zobraziteľnej
		// oblasti“ (imageable area) pri papieri formátu A4 je:
		// 
		// 	0.0, 0.0, 595.32, 841.92
		// 
		// 
		// Ukážka nastavenia okrajov (ak by bolo treba):
		// úloha.defaultPage().getPaper().
		// 	setImageableArea(10.0, 10.0, 575.32, 821.92);
	}


	/**
	 * <p>Implementácia rozhrania {@link Printable Printable}. V tejto metóde
	 * sú prepojené všetky vlastnosti tejto triedy: metóda je automaticky
	 * cyklicky spúšťaná po úspešnom {@linkplain #tlač() zahájení tlače},
	 * kedy na základe špecifikovaného {@linkplain #početStrán() počtu strán}
	 * zaháji {@linkplain #kresli(int, PageFormat) „kreslenie“ (rendrovanie)}
	 * každej strany – pričom musí byť použitý ten kresliaci robot, ktorý bol
	 * zadaný do {@linkplain #Tlač(GRobot) konštruktora} tejto inštancie (inak
	 * by tlač nefungovala).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Na rendrovanie strán
	 * <b>musí</b> byť využitý presne ten robot, ktorý bol zadaný do
	 * {@linkplain #Tlač(GRobot) konštruktora} tejto inštancie tlače.
	 * Pokus o využitie iného robota zlyhá, pretože pri tlači je dôležité,
	 * aby mal grafický robot nasmerované kreslenie do správneho grafického
	 * kontextu ({@link Graphics Graphics}), čo sa deje automaticky na
	 * pozadí pre robota, ktorý je spárovaný s touto tlačou. </p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Používateľ nemusí pri zahájení
	 * tlače zvoliť vytlačenie všetkých strán, preto musí byť zaručené,
	 * aby každá strana mohla byť vyrendrovaná samostatne – bez ohľadu na
	 * to, či bol obsah ostatných strán vykreslený, alebo nie. Toto musí mať
	 * programátor pri rendrovaní strán na pamäti. Ideálne je už pri
	 * plánovaní implementácie tlače navrhnúť rendrovanie strán tak, aby
	 * mohla byť každá strana v ľubovoľnom čase vyrendrovaná izolovane od
	 * ostatných strán.</p>
	 * 
	 * <p>Táto metóda je finálna (neprekryteľná), lebo jej neúmyselným
	 * prekrytím by sa narušil celý princíp tlače tohto rámca. Využíva
	 * vnútorné mechanizmy programovacieho rámca na korektné nastavenie
	 * všetkých objektov.</p>
	 * 
	 * @param g grafický kontext na tlač (je generovaný automaticky systémom
	 *     tlače Javy)
	 * @param pf formát strany (tiež je priraďovaný automaticky podľa iných
	 *     nastavení – pozri napríklad metódu {@link #nastavenieStrany(int)
	 *     #nastavenieStrany(strana)}); tento parameter je odovzdaný do
	 *     metódy {@link #kresli(int, PageFormat) kresli}
	 * @param page číslo strany – cyklicky sa mení podľa toho, aký rozsah
	 *     tlače bol zvolený; tento parameter je odovzdaný do metódy {@link 
	 *     #kresli(int, PageFormat) kresli}
	 * @return táto hodnota je automaticky odovzdávaná systému tlače Javy
	 *     a je buď {@link Printable#PAGE_EXISTS PAGE_EXISTS}, alebo {@link 
	 *     Printable#NO_SUCH_PAGE NO_SUCH_PAGE} – podľa toho, či systém tlače
	 *     požadoval rendrovanie jestvujúcej strany (programátor používajúci
	 *     programovací rámec sa týmto princípom nemusí zaoberať, pretože
	 *     metóda {@link #kresli(int, PageFormat) kresli} je automaticky
	 *     spúšťaná len pre tie strany, ktoré majú byť vytlačené a ktoré majú
	 *     jestvovať – v súlade s aktuálnym {@linkplain #početStrán() počtom
	 *     strán})
	 * 
	 * @see #Tlač(GRobot)
	 * @see #Tlač(GRobot, int)
	 * @see #kresli(int, PageFormat)
	 */
	public final int print(Graphics g, PageFormat pf, int page)
		throws PrinterException
	{
		// Overenie, či stránka jestvuje:
		if (page < 0 || page >= početStrán) return NO_SUCH_PAGE;

		/*{
			// TEST…
			System.out.println("Žiadam formát…");
			PageFormat pf2 = nastavenieStrany(page);
			if (null != pf2)
			{
				System.out.println("Formát bol zmenený…");
				pf = úloha.defaultPage(pf2);
			}
		}*/

		GRobot.ZálohaVlastností záloha = new GRobot.ZálohaVlastností(robot);

		try
		{
			int šírka = (int)Math.round(pf.getImageableWidth());
			int výška = (int)Math.round(pf.getImageableHeight());

			if (null == obrázok ||
				šírka != obrázok.šírka ||
				výška != obrázok.výška)
			{
				obrázok = null;

				for (Obrazok hľadaj : obrázky)
					if (hľadaj.šírka == šírka && hľadaj.výška == výška)
					{
						obrázok = hľadaj;
						break;
					}

				if (null == obrázok)
				{
					obrázok = new Obrazok(šírka, výška);
					obrázky.add(obrázok);
				}
			}

			obrázok.vymaž();

			robot.aktívnePlátno = null;
			robot.obrázokAktívnehoPlátna = obrázok;
			Graphics2D grafika = robot.grafikaAktívnehoPlátna = (Graphics2D)g;
			grafika.translate(pf.getImageableX(), pf.getImageableY());

			/*double posunX = -(Plátno.šírkaPlátna / 2) + (šírka / 2);
			double posunY = -(Plátno.výškaPlátna / 2) + (výška / 2);*/
			grafika.translate(obrázok.posunX, obrázok.posunY);

			// System.out.println("\nPage format:");
			// System.out.println("pf.getImageableX(): " +
			// 	pf.getImageableX());
			// System.out.println("pf.getImageableY(): " +
			// 	pf.getImageableY());
			// System.out.println("pf.getImageableWidth(): " +
			// 	pf.getImageableWidth());
			// System.out.println("pf.getImageableHeight(): " +
			// 	pf.getImageableHeight());

			// Paper p = pf.getPaper();
			// System.out.println("\nPaper format:");
			// System.out.println("p.getImageableX(): " +
			// 	p.getImageableX());
			// System.out.println("p.getImageableY(): " +
			// 	p.getImageableY());
			// System.out.println("p.getImageableWidth(): " +
			// 	p.getImageableWidth());
			// System.out.println("p.getImageableHeight(): " +
			// 	p.getImageableHeight());

			// Rendrovanie stránky:
			kresli(page, pf);

			// Oznámenie volajúcemu, že táto stránka jestvuje
			// (a bola nakreslená).
			return PAGE_EXISTS;
		}
		finally
		{
			záloha.obnov();
		}
	}


	/** <p><a class="alias"></a> Alias pre {@link #početStrán() početStrán}. (Súčasť implementácie rozhrania {@link Pageable Pageable}.)</p> */
	public final int getNumberOfPages() { return početStrán(); }

	/**
	 * <p>Súčasť implementácie rozhrania {@link Pageable Pageable}. Vráti
	 * nastavenie (formát) požadovanej strany. Ak strana nejestvuje, tak je
	 * vrátené predvolené nastavenie strany (čiže táto implementácia
	 * negeneruje výnimky).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> S nastaveniami strany je to
	 * komplikovanejšie. Slovo v tomto procese má tlačová služba, ktorá
	 * preferuje také nastavenie strany, ktoré je kompatibilné s tlačiarňou,
	 * čiže ak tlačiareň (môže byť aj virtuálna) tlačí len strany formátu
	 * A4, vždy bude výsledkom tlače tento formát. Nastavenia strany, ktoré
	 * sa pokúsite zmeniť budú prispôsobené tomuto formátu. </p>
	 * 
	 * @param pageIndex index strany, ktorej nastavenie (formát) má byť
	 *     vrátené (je generovaný automaticky v rozsahu tlačených strán)
	 * @return nastavenie určenej strany, pričom pre neexistujúce strany je
	 *     vrátené predvolené nastavenie
	 */
	public PageFormat getPageFormat(int pageIndex)
	{
		PageFormat pageFormat = nastavenieStrany(pageIndex);
		if (null == pageFormat) return nastavenieStrany;
		return pageFormat;
	}

	/**
	 * <p>Súčasť implementácie rozhrania {@link Pageable Pageable}. Táto
	 * implementácia vracia pre všetky strany (vrátane neexistujúcich) túto
	 * (aktuálnu) inštanciu triedy {@link Tlač Tlač} (čiže samú seba).</p>
	 * 
	 * @param pageIndex index strany (je ignorovaný)
	 * @return táto inštancia tlače
	 */
	public Printable getPrintable(int pageIndex) { return this; }


	/**
	 * <p>Vráti aktuálnu tlačovú úlohu ({@link PrinterJob PrinterJob}) tejto
	 * inštancie tlače. Úloha môže byť použitá na konfiguráciu a rôzne
	 * aktivity pripravujúce tlač.</p>
	 * 
	 * @return aktuálna tlačová úloha ({@link PrinterJob PrinterJob}) tejto
	 *     inštancie
	 */
	public PrinterJob úloha()
	{
		return úloha;
	}

	/** <p><a class="alias"></a> Alias pre {@link #úloha() úloha}.</p> */
	public PrinterJob uloha() { return úloha(); }


	/**
	 * <p>Vráti aktuálny počet strán tejto inštancie tlače.</p>
	 * 
	 * @return aktuálny počet strán tejto inštancie tlače
	 * 
	 * @see #početStrán()
	 * @see #kresli(int, PageFormat)
	 */
	public int početStrán() { return početStrán; }

	/** <p><a class="alias"></a> Alias pre {@link #početStrán() početStrán}.</p> */
	public int pocetStran() { return početStrán(); }

	/**
	 * <p>Nastaví nový počet strán tejto inštancie tlače. Zadaná hodnota môže
	 * byť ľubovoľné celé číslo – záporné hodnoty majú rovnaký význam ako
	 * nula.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Je teoreticky možné využiť záporné
	 * hodnoty na uchovanie rôznych chybových stavov, ale treba vopred
	 * dostatočne dobre zvážiť, či by to neprinieslo komplikácie alebo rôzne
	 * nepredvídateľné dôsledky. Rozhodnutie ponechávame na autorov softvéru
	 * vytváraného s pomocou tohto rámca.</p>
	 * 
	 * @param početStrán nový počet strán tejto inštancie tlače
	 * 
	 * @see #početStrán(int)
	 * @see #kresli(int, PageFormat)
	 */
	public void početStrán(int početStrán)
	{ this.početStrán = početStrán; }

	/** <p><a class="alias"></a> Alias pre {@link #početStrán(int) početStrán}.</p> */
	public void pocetStran(int početStrán) { početStrán(početStrán); }


	/**
	 * <p>Zistí okraje predvolenej strany. Údaje sú vrátené v tlačových
	 * bodoch a na prepočet jednotiek sa dá použiť delenie konštantami:
	 * {@link #inch inch} alebo {@link #cm cm}. (Pozri aj informácie uvedené
	 * pri párujúcej metóde: {@link #okraje(double, double, double, double)
	 * okraje}.)</p>
	 * 
	 * @return štvorica hodnôt určujúcich okraje predvolených nastavení
	 *     strany tejto inštancie tlače (v tlačových bodoch)
	 * 
	 * @see #okraje(double, double, double, double)
	 * @see #nastavenieStrany()
	 * @see #nastavenieStrany(PageFormat)
	 * @see #nastavenieStrany(int)
	 */
	public double[] okraje()
	{
		Paper papier = nastavenieStrany.getPaper();
		double x = papier.getImageableX();
		double y = papier.getImageableY();

		return new double[] {x, y, papier.getWidth() -
			papier.getImageableWidth() - x, papier.getHeight() -
			papier.getImageableHeight() - y};
	}

	/**
	 * <p>Zmení okraje predvolenej strany. Údaje sú zadávané v tlačových
	 * bodoch. Na zmenu jednotiek použite násobenie konštantou {@link #inch
	 * inch} alebo {@link #cm cm}. Priestor za hranicami okrajov nebude
	 * zahrnutý do tlačového výstupu.</p>
	 * 
	 * @param ľavý   ľavý   okraj strany (v tlačových bodoch)
	 * @param horný  horný  okraj strany (v tlačových bodoch)
	 * @param pravý  pravý  okraj strany (v tlačových bodoch)
	 * @param spodný spodný okraj strany (v tlačových bodoch)
	 * 
	 * @see #okraje()
	 * @see #nastavenieStrany()
	 * @see #nastavenieStrany(PageFormat)
	 * @see #nastavenieStrany(int)
	 */
	public void okraje(double ľavý, double horný, double pravý, double spodný)
	{
		Paper papier = nastavenieStrany.getPaper();
		papier.setImageableArea(ľavý, horný,
			nastavenieStrany.getWidth() - ľavý - pravý,
			nastavenieStrany.getHeight() - horný - spodný);
		nastavenieStrany.setPaper(papier);
	}


	/**
	 * <p>Vráti inštanciu predvolených nastavení strany ({@link PageFormat
	 * PageFormat}), ktorá je (predvolene) používaná na tie strany, ktoré to
	 * nemajú určené inak: pozri prekryteľnú metódu {@link 
	 * #nastavenieStrany(int) nastavenieStrany(strana)}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> S nastaveniami strany je to
	 * komplikovanejšie. Slovo v tomto procese má tlačová služba, ktorá
	 * preferuje také nastavenie strany, ktoré je kompatibilné s tlačiarňou,
	 * čiže ak tlačiareň (môže byť aj virtuálna) tlačí len strany formátu
	 * A4, vždy bude výsledkom tlače tento formát. Nastavenia strany, ktoré
	 * sa pokúsite zmeniť budú prispôsobené tomuto formátu. </p>
	 * 
	 * @return vráti inštanciu predvolených nastavení strany ({@link 
	 *     PageFormat PageFormat})
	 * 
	 * @see #nastavenieStrany(PageFormat)
	 * @see #nastavenieStrany(int)
	 * @see #okraje()
	 * @see #okraje(double, double, double, double)
	 */
	public PageFormat nastavenieStrany() { return nastavenieStrany; }

	/**
	 * <p>Zamení inštanciu {@link PageFormat PageFormat}, ktorá je používaná
	 * na predvolené nastavenia strany.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> S nastaveniami strany je to
	 * komplikovanejšie. Slovo v tomto procese má tlačová služba, ktorá
	 * preferuje také nastavenie strany, ktoré je kompatibilné s tlačiarňou,
	 * čiže ak tlačiareň (môže byť aj virtuálna) tlačí len strany formátu
	 * A4, vždy bude výsledkom tlače tento formát. Nastavenia strany, ktoré
	 * sa pokúsite zmeniť budú prispôsobené tomuto formátu. </p>
	 * 
	 * @param nastavenieStrany nové predvolené nastavenie strany ({@link 
	 *     PageFormat PageFormat})
	 * 
	 * @see #nastavenieStrany()
	 * @see #nastavenieStrany(int)
	 * @see #okraje()
	 * @see #okraje(double, double, double, double)
	 */
	public void nastavenieStrany(PageFormat nastavenieStrany)
	{ this.nastavenieStrany = úloha.defaultPage(nastavenieStrany); }

	/**
	 * <p>Táto metóda je určená na prekrytie. Jej predvolená verzia vracia
	 * hodnotu {@code valnull} pre všetky strany.</p>
	 * 
	 * <p>Prekrytá verzia tejto metódy môže obsahovať úpravu nastavenia
	 * každej (individuálnej) strany (rozlišovanej podľa parametra {@code 
	 * strana}. Upravené nastavenie musí byť vrátené ako inštancia triedy
	 * {@link PageFormat PageFormat}.
	 * 
	 * Ak metóda nechce zmeniť nastavenie pre určitú stranu, tak musí
	 * vrátiť hodnotu {@code valnull} (v tom prípade sa použije predvolené
	 * nastavenie strany). Predvolené nastavenie strany sa dá získať
	 * volaním metódy {@link #nastavenieStrany() nastavenieStrany} (bez
	 * parametra).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> S nastaveniami strany je to
	 * komplikovanejšie. Slovo v tomto procese má tlačová služba, ktorá
	 * preferuje také nastavenie strany, ktoré je kompatibilné s tlačiarňou,
	 * čiže ak tlačiareň (môže byť aj virtuálna) tlačí len strany formátu
	 * A4, vždy bude výsledkom tlače tento formát. Nastavenia strany, ktoré
	 * sa pokúsite zmeniť budú prispôsobené tomuto formátu. </p>
	 * 
	 * @param strana poradové číslo kreslenej (tlačenej) strany
	 * @return vlastné nastavenie strany alebo {@code valnull}.
	 * 
	 * @see #nastavenieStrany()
	 * @see #nastavenieStrany(PageFormat)
	 * @see #okraje()
	 * @see #okraje(double, double, double, double)
	 * @see #kresli(int, PageFormat)
	 */
	public PageFormat nastavenieStrany(int strana) { return null; }


	/**
	 * <p>Vráti referenčný obrázok, ktorého obsah vzniká ako vedľajší
	 * produkt kreslenia robotom počas rendrovania stránok tlače alebo
	 * {@code valnull}, ak ešte tlač nebola spustená. Obrázok je pomocný
	 * objekt tlače, ktorý má zabrániť vzniku chýb v dôsledku chýbajúceho
	 * rastra, ale pri procese tlače nie je priamo využívaný. Obsah obrázka
	 * sa môže líšiť od reálneho obsahu tlačených stránok, ale za
	 * štandardných okolností by mal obsahovať rastrový obraz naposledy
	 * vytlačenej strany, pretože obsah tohto obrázka je pred tlačou novej
	 * strany vždy vymazaný (a ak je na tlač využívaný len konkrétny robot,
	 * tak by sa všetko malo premietať do tohto obrázka). Táto inštancia
	 * obrázka nemá byť použitá na kreslenie (nemalo by to zmysel). Je
	 * vhodná napríklad na zistenie rozmerov aktuálne tlačenej strany
	 * (pričom jeden pixel je zhruba jeden tlačový bod) počas jej
	 * {@linkplain #kresli(int, PageFormat) kreslenia,} prípadne
	 * (v prípade vhodných okolností) na získanie rastrového obrazu naposledy
	 * vytlačenej strany (na informatívne účely).</p>
	 * 
	 * <p>Táto metóda môže po vykonaní rôznych tlačových úloh vrátiť odlišný
	 * objekt. Vráti vždy objekt obrázka použitý pri poslednej tlačenej
	 * strane, pričom rozmery tohto obrázka sú určené podľa nastavení tejto
	 * strany {@link PageFormat PageFormat}). Všetky vytvorené inštancie
	 * obrázkov sú vnútorne ukladané a recyklované podľa potreby (pri stranách
	 * s rovnakými rozmermi je použitá tá istá inštancia).</p>
	 * 
	 * @return inštancia referenčného obrázka použiteľného napríklad na
	 *     overenie (prípadne obmedzenie) polohy robota na strate pri
	 *     rendrovaní (kreslení) tlačených strán
	 */
	public Obrazok obrázok() { return obrázok; }

	/** <p><a class="alias"></a> Alias pre {@link #obrázok() obrázok}.</p> */
	public Obrazok obrazok() { return obrázok(); }


	/**
	 * <p>Metóda určená na prekrytie. V jej tele má byť zabezpečené
	 * „nakreslenie“ – vyrendrovanie strany so zadaným číslom. Číslo je
	 * v rozmedzí od 0 po (nie vrátane) {@linkplain #početStrán(int) počet
	 * strán.} Na kreslenie bude využitý robot zadaný do {@linkplain 
	 * #Tlač(GRobot) konštruktora} tejto inštancie.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Na rendrovanie strán
	 * <b>musí</b> byť využitý presne ten robot, ktorý bol zadaný do
	 * {@linkplain #Tlač(GRobot) konštruktora} tejto inštancie tlače.
	 * Pokus o využitie iného robota zlyhá, pretože pri tlači je dôležité,
	 * aby mal grafický robot nasmerované kreslenie do správneho grafického
	 * kontextu ({@link Graphics Graphics}), čo sa deje automaticky na
	 * pozadí pre robota, ktorý je spárovaný s touto tlačou. </p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Používateľ nemusí pri zahájení
	 * tlače zvoliť vytlačenie všetkých strán, preto musí byť zaručené,
	 * aby každá strana mohla byť vyrendrovaná samostatne – bez ohľadu na
	 * to, či bol obsah ostatných strán vykreslený, alebo nie. Toto musí mať
	 * programátor pri rendrovaní strán na pamäti. Ideálne je už pri
	 * plánovaní implementácie tlače navrhnúť rendrovanie strán tak, aby
	 * mohla byť každá strana v ľubovoľnom čase vyrendrovaná izolovane od
	 * ostatných strán.</p>
	 * 
	 * @param strana poradové číslo kreslenej (tlačenej) strany
	 * @param formát nastavenie kreslenej (tlačenej) strany
	 * 
	 * @see #početStrán()
	 * @see #početStrán(int)
	 * @see #nastavenieStrany(int)
	 * 
	 * @see #Tlač(GRobot)
	 * @see #Tlač(GRobot, int)
	 * 
	 * @see #print(Graphics, PageFormat, int)
	 */
	public abstract void kresli(int strana, PageFormat formát);


	/**
	 * <p>Táto trieda slúži na uchovávanie slov bloku textu, ktorý bol
	 * {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) rozbitý}
	 * na {@linkplain #kresliTextDo(Graphics, CharSequence, int, int,
	 * int, int, BlokSlov, boolean, int) nakreslenie.}</p>
	 * 
	 * <p>{@linkplain BlokSlov Blok} pozostáva z {@linkplain RiadokSlov
	 * riadkov} a riadky pozostávajú zo {@linkplain Slovo slov.}
	 * {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) Rozbitie textu
	 * na slová} môže byť vykonané vopred (s cieľom úpravy/prispôsobenia
	 * generovaného bloku) alebo automaticky, vnútorne metódou {@link 
	 * #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov,
	 * boolean, int) kresliTextDo}. Na kreslenie bloku má vplyv hodnota
	 * tejto premennej: {@link #SNR SNR} („spojovník na riadku“).
	 */
	public static class Slovo
	{
		/**
		 * <p>Znenie tohto slova.</p>
		 */
		public final String slovo;

		/**
		 * <p>Šírka tohto slova v pixeloch. Je vypočítaná podľa fontu, ktorý
		 * bol aktívny v čase spracovania tohto slova.</p>
		 */
		public final int šírka;

		/** <p><a class="alias"></a> Alias pre {@link #šírka šírka}.</p> */
		public final int sirka;

		/**
		 * <p>Úplný konštruktor slova.</p>
		 * 
		 * @param slovo znenie slova
		 * @param šírka šírka slova v pixeloch
		 */
		public Slovo(String slovo, int šírka)
		{
			this.slovo = slovo;
			this.šírka = sirka = šírka;
		}
	}

	/**
	 * <p>Táto trieda slúži na uchovanie jedného riadka bloku textu
	 * {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) rozbitého}
	 * na {@linkplain #kresliTextDo(Graphics, CharSequence, int, int, int,
	 * int, BlokSlov, boolean, int) nakreslenie}.</p>
	 * 
	 * <p>{@linkplain BlokSlov Blok} pozostáva z {@linkplain RiadokSlov
	 * riadkov} a riadky pozostávajú zo {@linkplain Slovo slov.}
	 * {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) Rozbitie textu
	 * na slová} môže byť vykonané vopred (s cieľom úpravy/prispôsobenia
	 * generovaného bloku) alebo automaticky, vnútorne metódou {@link 
	 * #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov,
	 * boolean, int) kresliTextDo}. Na kreslenie bloku má vplyv hodnota
	 * tejto premennej: {@link #SNR SNR} („spojovník na riadku“).
	 */
	@SuppressWarnings("serial")
	public static class RiadokSlov extends Vector<Slovo>
	{
		/**
		 * <p>Spôsob zarovnania tohto riadka. Nula znamená na stred, kladné
		 * hodnoty doprava a záporné doľava. Kladná a záporná jednotka
		 * znamenajú zarovnanie ku konkrétnemu okraju a dvojky znamenajú
		 * zarovnanie k obidvom okrajom („do bloku“). Predvolenou hodnotou
		 * je záporná dvojka, ktorá je automaticky nastavená na zápornú
		 * jednotku pre riadky, ktoré sú posledné alebo ručne zalomené.</p>
		 * 
		 * <p>Ak potrebujeme predvolené hodnoty zmeniť, musíme vygenerovať
		 * {@linkplain BlokSlov blok slov} vopred metódou {@link 
		 * #rozbiNaSlová(Graphics, CharSequence, int) rozbiNaSlová} (ktorá
		 * vracia {@link BlokSlov BlokSlov} – čiže blok riadkov) a hodnoty
		 * zarovnania pri jednotlivých riadkoch upraviť.</p>
		 */
		public int zarovnaj = -2;

		/**
		 * <p>Medzera, ktorá bude pripočítaná k riadkovaniu pred kreslením
		 * tohto riadka. (Obvyklé je upraviť medzeru prvého riadka odseku,
		 * ktorá tým reprezentuje medzeru pred odsekom. Pozor, tieto medzery
		 * sa nijako automaticky nezlučujú, ako je to obvyklé v textových
		 * procesoroch. Zlučovanie musí byť doriešené dodatočne algoritmicky –
		 * úpravou hodnôt medzier pred a za konkrétnymi „odsekmi,“ t. j.
		 * prvými a poslednými riadkami blokov.)</p>
		 */
		public double medzeraPred = 0.0;

		/**
		 * <p>Medzera, ktorá bude pripočítaná k riadkovaniu po kreslení
		 * tohto riadka. (Obvyklé je upraviť medzeru posledného riadka
		 * odseku, ktorá tým reprezentuje medzeru za odsekom. Pozor, tieto
		 * medzery sa nijako automaticky nezlučujú, ako je to obvyklé
		 * v textových procesoroch. Zlučovanie musí byť doriešené dodatočne
		 * algoritmicky – úpravou hodnôt medzier pred a za konkrétnymi
		 * „odsekmi,“ t. j. prvými a poslednými riadkami blokov.)</p>
		 */
		public double medzeraZa = 0.0;
	}

	/**
	 * <p>Táto trieda slúži na uchovanie celého bloku textu {@linkplain 
	 * #rozbiNaSlová(Graphics, CharSequence, int) rozbitého} na {@linkplain 
	 * #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov,
	 * boolean, int) nakreslenie.}</p>
	 * 
	 * <p>{@linkplain BlokSlov Blok} pozostáva z {@linkplain RiadokSlov
	 * riadkov} a riadky pozostávajú zo {@linkplain Slovo slov.}
	 * {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) Rozbitie textu
	 * na slová} môže byť vykonané vopred (s cieľom úpravy/prispôsobenia
	 * generovaného bloku) alebo automaticky, vnútorne metódou {@link 
	 * #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov,
	 * boolean, int) kresliTextDo}. Na kreslenie bloku má vplyv hodnota
	 * tejto premennej: {@link #SNR SNR} („spojovník na riadku“).
	 */
	@SuppressWarnings("serial")
	public static class BlokSlov extends Vector<RiadokSlov>
	{
		/**
		 * <p>Riadkovanie tohto bloku slov. Predvolená je hodnota
		 * {@code num1.0}. Vyššia hodnota je redšie riadkovanie. Nižšia
		 * hodnota (medzi {@code num0.0} a {@code num1.0}) je hustejšie
		 * riadkovanie.</p>
		 */
		public double riadkovanie = 1.0;

		/**
		 * <p>Odstráni všetky prvky tohto bloku. Blok bude po vykonaní
		 * tejto metódy prázdny (ibaže by vznikla výnimka). Metóda resetuje
		 * riadkovanie na predvolenú hodnotu {@code num1.0}.</p>
		 */
		@Override public void clear()
		{
			super.clear();
			riadkovanie = 1.0;
		}

		/**
		 * <p>Zistí výšku zadaného rozsahu riadkov v pixeloch. Do rozsahov je
		 * automaticky zarátané riadkovanie textov podľa výšky riadka
		 * a medzery pred a za riadkami, ktoré môžu byť individuálne
		 * nastavované – pozri vlastnosti riadkov definovateľné v rámci
		 * triedy {@link RiadokSlov RiadokSlov}.</p>
		 * 
		 * <p>Výpočet prebieha od prvého indexu (vrátane) po posledný index
		 * (vynímajúc). Čiže aj sa hodnoty indexov začiatku a konca rozsahu
		 * rovnajú, výsledok bude nula.</p>
		 * 
		 * @param g grafický kontext, z ktorého bude vypočítaná aktuálna
		 *     výška riadka
		 * @param začiatokRozsahu index prvého riadka v rozsahu; ak je
		 *     záporný, vypočíta sa poradové číslo riadka od konca:
		 *     {@code num-1} znamená index posledného riadka
		 * @param koniecRozsahu index nasledujúci za posledným riadkom
		 *     v rozsahu; ak je záporný, vypočíta sa poradové číslo riadka
		 *     od konca: {@code num-1} znamená index za posledným riadkom,
		 *     čiže výpočet bude pokračovať po posledný riadok (vrátane
		 *     posledného riadka)
		 * @return výška rozsahu riadkov v pixeloch
		 */
		public double výškaRozsahu(Graphics g,
			int začiatokRozsahu, int koniecRozsahu)
		{
			Graphics2D g2d = (Graphics2D)g;
			FontMetrics fm = g2d.getFontMetrics();

			double výškaRiadka = fm.getHeight() * riadkovanie;
			double výškaRozsahu = 0;

			if (začiatokRozsahu < 0) začiatokRozsahu += size() - 1;
			if (koniecRozsahu < 0) koniecRozsahu += size();

			if (začiatokRozsahu >= 0 && koniecRozsahu <= size())
			for (int i = začiatokRozsahu; i < koniecRozsahu; ++i)
			{
				RiadokSlov riadokSlov = get(i);
				výškaRozsahu += riadokSlov.medzeraPred +
					výškaRiadka + riadokSlov.medzeraZa;
			}

			return výškaRozsahu;
		}

		/** <p><a class="alias"></a> Alias pre {@link #výškaRozsahu(Graphics, int, int) výškaRozsahu}.</p> */
		public double vyskaRozsahu(Graphics g, int začiatok, int koniec)
		{ return výškaRozsahu(g, začiatok, koniec); }
	}


	/**
	 * <p>Ak je táto konštanta rovná {@code valtrue} (predvolene), tak sa
	 * pri {@linkplain #rozbiNaSlová(Graphics, CharSequence, int) rozbíjaní
	 * textu na slová} v prípade výskytu klasického spojovníka na konci
	 * riadka automaticky pridá pevný spojovník na začiatok nového riadka.</p>
	 */
	public static boolean SNR = true;

	/**
	 * <p>Rozbije zadaný text do bloku textu podľa metriky aktuálneho
	 * {@linkplain Graphics#getFont() fontu} zadaného grafického kontextu.
	 * Text je rozbíjaný podľa maximálnej šírky (pravého okraja), ktorú môže
	 * dosiahnuť na riadku. Vrátený blok môže byť prispôsobený (napríklad
	 * mu môže byť upravené riadkovanie) a je pripravený na rendrovanie
	 * s pomocou metódy {@link #kresliTextDo(Graphics, CharSequence, int,
	 * int, int, int, BlokSlov, boolean, int) kresliTextDo} (spúšťanej
	 * s rovnakým grafickým kontextom a rovnakým nastavením fontu, inak bude
	 * výsledok nepredvídateľný).</p>
	 * 
	 * <p>Táto metóda môže byť spúšťaná automaticky metódou {@link 
	 * #kresliTextDo(Graphics, CharSequence, int, int, int, int, BlokSlov,
	 * boolean, int) kresliTextDo} – pozri informácie v jej opise.</p>
	 * 
	 * @param g grafický kontext
	 * @param chs reťazec na rozbitie
	 * @param šírka maximálna šírka textu na riadku
	 * @return blok textu rozbitého na riadky a slová
	 */
	public static BlokSlov rozbiNaSlová(Graphics g,
		CharSequence chs, int šírkaRiadka)
	{
		// Inicializácia niektorých objektov.
		Graphics2D g2d = (Graphics2D)g;
		StringBuffer sb = new StringBuffer();
		BlokSlov blokSlov = new BlokSlov();
		RiadokSlov riadokSlov = new RiadokSlov();
		blokSlov.add(riadokSlov);

		// FontMetrics poskytuje informácie o šírke, výške atď. textu
		// podľa aktuálnej inštancie fontu grafického objektu.
		FontMetrics fm = g2d.getFontMetrics();

		int šírkaMedzery = fm.stringWidth(" ");
		int výškaRiadka = fm.getHeight();
		int aktuálneX = 0;
		int dĺžka = chs.length();

		Slovo poslednéSlovo = null;
		// Minitabuľka:
		//    Spojovník: -	U+002D	&#45;
		//    Spojovník: ‐	U+2010	&#8208;
		//    Voliteľné rozdelenie: ­	U+00AD	&#173; &shy;
		//    	(soft hyphen/syllable hyphen – SHY)
		//    	TeX and LaTeX: \-
		//    Pevný spojovník: ‑	U+2011	&#8209; („nbhy“)
		//    Medzera s nulovou šírkou: ​	U+200B	&#8203;

		for (int i = 0; i <= dĺžka; ++i)
		{
			char znak = i < dĺžka ? chs.charAt(i) : '\n';
			if (' ' >= znak || '-' == znak || '‐' == znak || '­' == znak ||
				'​' == znak)
			{
				if (' ' < znak) sb.append(znak);

				String text = sb.toString();

				// Zisti šírku aktuálneho textu (slova alebo viacerých slov
				// oddelených pevnou medzerou).
				int šírkaTextu = fm.stringWidth(text);

				// Ak šírka textu prekročí požadovanú šírku riadka,
				// tak sa posunieme na ďalší riadok.
				if (aktuálneX + šírkaTextu >= šírkaRiadka)
				{
					if (null != poslednéSlovo &&
						!poslednéSlovo.slovo.isEmpty())
					{
						char poslednýZnak = poslednéSlovo.slovo.charAt(
							poslednéSlovo.slovo.length() - 1);
						if (SNR &&
							('-' == poslednýZnak || '‐' == poslednýZnak))
						{
							// Vloží „nbhy“ (&#8209;) na začiatok
							// nasledujúceho riadka.
							text = '‑' + text;
							šírkaTextu = fm.stringWidth(text);
						}
						else if ('​' == poslednýZnak)
						{
							// Odstráni nulovú medzeru aj na konci riadka.
							String novýText = poslednéSlovo.slovo.substring(0,
								poslednéSlovo.slovo.length() - 1);
							int šírkaNovéhoTextu = fm.stringWidth(novýText);
							Slovo novéSlovo = new Slovo(
								novýText, šírkaNovéhoTextu);

							riadokSlov.removeElement(poslednéSlovo);
							riadokSlov.add(novéSlovo);
							poslednéSlovo = novéSlovo;
						}
					}

					aktuálneX = 0;
					riadokSlov = new RiadokSlov();
					blokSlov.add(riadokSlov);
				}
				else
				{
					if (null != poslednéSlovo &&
						!poslednéSlovo.slovo.isEmpty())
					{
						char poslednýZnak = poslednéSlovo.slovo.charAt(
							poslednéSlovo.slovo.length() - 1);

						if ('­' == poslednýZnak || '​' == poslednýZnak)
						{
							// Odstráni &shy; ak nie je na konci riadka
							// a nulovú medzeru (v tejto časti „v strede“
							// riadka, vyššie aj na konci).
							text = poslednéSlovo.slovo.substring(0,
								poslednéSlovo.slovo.length() - 1) + text;
							šírkaTextu = fm.stringWidth(text);
							riadokSlov.removeElement(poslednéSlovo);
							aktuálneX -= poslednéSlovo.šírka + šírkaMedzery;
						}
						else if ('-' == poslednýZnak || '‐' == poslednýZnak)
						{
							// Spojí toto slovo s predchádzajúcim slovom.
							// Obidva znaky sú viditeľné spojovníky, ktoré
							// tam zostávajú (dĺžka sa nemení).
							text = poslednéSlovo.slovo + text;
							šírkaTextu = fm.stringWidth(text);
							riadokSlov.removeElement(poslednéSlovo);
							aktuálneX -= poslednéSlovo.šírka + šírkaMedzery;
						}
					}
				}

				sb.setLength(0);

				// Ak text neobsahuje pevnú medzeru, tak sa pridá ako jedno
				// slovo.
				if (-1 == text.indexOf(' '))
				{
					poslednéSlovo = new Slovo(text, šírkaTextu);
					riadokSlov.add(poslednéSlovo);
				}
				else
				{
					// V opačnom prípade sa podľa pevných medzier rozbije
					// na fragmenty a tie sa pridajú ako slová.
					String[] fragmenty = text.split(" ");
					for (String fragment : fragmenty)
					{
						int šírkaFragmentu = fm.stringWidth(fragment);
						poslednéSlovo = new Slovo(fragment, šírkaFragmentu);
						riadokSlov.add(poslednéSlovo);
					}
				}

				// Ak je aktuálny znak znakom nového riadka (alebo zalomenia
				// riadka), tak sa po tomto slove posunieme na ďalší riadok.
				if ('\n' == znak || '\r' == znak)
				{
					riadokSlov.zarovnaj = -1;
					aktuálneX = 0;
					riadokSlov = new RiadokSlov();
					blokSlov.add(riadokSlov);
				}
				else
				{
					// Inak sa presúvame doprava za aktuálne pridaný text
					// (podľa jeho šírky).
					aktuálneX += šírkaTextu + šírkaMedzery;
				}
			}
			else
			{
				sb.append(znak);
			}
		}

		return blokSlov;
	}

	/** <p><a class="alias"></a> Alias pre {@link #rozbiNaSlová(Graphics, CharSequence, int) rozbiNaSlová}.</p> */
	public static BlokSlov rozbiNaSlova(Graphics g,
		CharSequence chs, int šírkaRiadka)
	{ return rozbiNaSlová(g, chs, šírkaRiadka); }

	/**
	 * <p>Rendruje text na grafický kontext do priestoru zadaného rámčeka.
	 * Parametre rendrovania relatívne podrobne určujú parametre tejto
	 * metódy. Dá sa nimi dosiahnuť rendrovanie vopred {@linkplain 
	 * #rozbiNaSlová(Graphics, CharSequence, int) rozbitého bloku} a tiež
	 * rendrovanie do viacerých za sebou nasledujúcich rámčekov (napríklad
	 * pre text prekračujúci rozhranie strán).</p>
	 * 
	 * @param g grafický kontext kreslenia textu; jeho {@linkplain 
	 *     Graphics#getFont() font} ovplyvňuje {@linkplain 
	 *     #rozbiNaSlová(Graphics, CharSequence, int) rozbíjanie textu
	 *     na slová}
	 * @param chs text, ktorý má byť automaticky {@linkplain 
	 *     #rozbiNaSlová(Graphics, CharSequence, int) rozbitý na slová}
	 *     podľa rozmerov rámčeka (parametre {@code šírka} a {@code výška})
	 *     alebo hodnota {@code valnull} – pozri aj parameter {@code blokSlov}
	 * @param x ľavá súradnica rámčeka kreslenia bloku textu
	 * @param y horná súradnica rámčeka kreslenia bloku textu
	 * @param šírka šírka rámčeka kreslenia bloku textu
	 * @param výška výška rámčeka kreslenia bloku textu
	 * @param blokSlov tento parameter (ak nie je {@code valnull}) buď
	 *     obsahuje jestvujúce {@linkplain #rozbiNaSlová(Graphics,
	 *     CharSequence, int) rozbitie slov}, ktoré má byť použité na
	 *     vykreslenie bloku textu – v tom prípade musí byť parameter
	 *     {@code chs} rovný {@code valnull} ({@linkplain #rozbiNaSlová
	 *     (Graphics, CharSequence, int) rozbitie} musí byť vykonané s tou
	 *     istou konfiguráciou grafického objektu {@link Graphics Graphics},
	 *     ktorá je zadaná do prvého parametra {@code g} – najlepšie s tou
	 *     istou inštanciou, ktorej nebol medzitým zmenený font; inak môže
	 *     byť výsledok nepredvídateľný), alebo sa do neho uloží {@linkplain 
	 *     #rozbiNaSlová(Graphics, CharSequence, int) rozbitie na slová}
	 *     aktuálneho parametra {@code chs} (na ďalšie použitie)
	 * @param nekresliMimo príznak určujúci, že kreslenie textu sa má zastaviť
	 *     po prekročení spodnej hranice rámčeka
	 * @param začniRiadkom index riadka, ktorým sa má kreslenie bloku textu
	 *     začať; toto je využiteľné pri blokoch, ktorých kreslenie má byť
	 *     rozdelené do viacerých rámčekov s rozdielnou výškou (šírka musí
	 *     byť rovnaká, inak bude výsledok nepredvídateľný)
	 * @return index prvého riadka, ktorý presiahol požadovanú výšku bloku
	 *     (zadanú v parametri {@code výška}); hodnota -1 znamená, že táto
	 *     situácia nenastala
	 */
	public static int kresliTextDo(Graphics g, CharSequence chs,
		int x, int y, int šírka, int výška, BlokSlov blokSlov,
		boolean nekresliMimo, int začniRiadkom)
	{
		// Veľmi jednoduchá verzia sa dá nájsť na: ⟨stackoverflow.com/
		// questions/400566/full-justification-with-a-java-graphics-
		// drawstring-replacement⟩.

		if (null == blokSlov)
		{
			// Iba jeden z parametrov chs alebo blokSlov môže byť null.
			if (null == chs) return -2;
			blokSlov = rozbiNaSlová(g, chs, šírka);
		}
		else if (null != chs)
		{
			// Ak nie je ani chs, ani blokSlov null, tak sa inštancia
			// blokSlov použije na návrat výsledku rozbitia slov sekvencie
			// chs. (Pričom ak je chs rovné null, tak sa predpokladá,
			// že blokSlov obsahuje korektne rozbitý blok slov – s použitím
			// aktuálneho fontu a aktuálnej šírky bloku.)
			blokSlov.clear();
			blokSlov.addAll(rozbiNaSlová(g, chs, šírka));
		}

		Graphics2D g2d = (Graphics2D)g;

		// FontMetrics poskytuje informácie o šírke, výške atď. textu
		// podľa aktuálnej inštancie fontu grafického objektu.
		FontMetrics fm = g2d.getFontMetrics();

		// Prepočet základných často používaných údajov pri kreslení
		// bloku textu.
		int šírkaMedzery = fm.stringWidth(" ");
		double výškaRiadka = fm.getHeight();
		double aktuálneY = y + výškaRiadka;

		výškaRiadka *= blokSlov.riadkovanie;

		int návrat = -1; int početRiadkov = blokSlov.size();
		for (int i = začniRiadkom; i < početRiadkov; ++i)
		{
			RiadokSlov riadokSlov = blokSlov.get(i);
			aktuálneY += riadokSlov.medzeraPred;

			if (-1 == návrat && aktuálneY >= y + výška)
			{
				if (nekresliMimo) return i;
				návrat = i;
			}

			double zvyšok = šírka, šírkaRiadka = -šírkaMedzery;
			for (Slovo slovo : riadokSlov)
			{
				zvyšok -= slovo.šírka;
				šírkaRiadka += slovo.šírka + šírkaMedzery;
			}

			double Δm = ((zvyšok > 0) && (riadokSlov.size() > 1) &&
				Math.abs(riadokSlov.zarovnaj) > 1) ? (zvyšok /
				(riadokSlov.size() - 1.0)) : (double)šírkaMedzery;

			double aktuálneX = x;
			switch (riadokSlov.zarovnaj)
			{
			case 1: aktuálneX += šírka - šírkaRiadka; break;
			case 0: aktuálneX += (šírka - šírkaRiadka) / 2.0; break;
			}

			for (Slovo slovo : riadokSlov)
			{
				g2d.drawString(slovo.slovo, (int)aktuálneX, (int)aktuálneY);
				aktuálneX += slovo.šírka + Δm;
			}

			aktuálneY += výškaRiadka + riadokSlov.medzeraZa;
		}

		return návrat;
	}


	/**
	 * <p>Pokúsi sa spustiť proces tlače (dokumentu).</p>
	 * 
	 * @return ak bola tlač spustená, tak {@code valtrue}, inak
	 *     {@code valfalse}
	 * 
	 * @see #tlač(String)
	 * @see #tlačDialógom()
	 * @see #tlačDialógom(String)
	 */
	public boolean tlač()
	{
		try
		{
			úloha.print();
			return true;
		}
		catch (PrinterException e)
		{
			// Vznikla chyba počas tlače.
			GRobotException.vypíšChybovéHlásenia(e);
		}
		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlač() tlač}.</p> */
	public boolean tlac() { return tlač(); }


	/**
	 * <p>Pokúsi sa spustiť proces tlače dokumentu (tejto inštancie),
	 * pričom dokumentu nastaví zadaný názov.</p>
	 * 
	 * @param názovDokumentu názov tlačeného dokumentu
	 * @return ak bola tlač spustená, tak {@code valtrue}, inak
	 *     {@code valfalse}
	 * 
	 * @see #tlač()
	 * @see #tlačDialógom()
	 * @see #tlačDialógom(String)
	 */
	public boolean tlač(String názovDokumentu)
	{
		String zálohaNázvu = úloha.getJobName();
		try
		{
			úloha.setJobName(názovDokumentu);
			return tlač();
		}
		finally
		{
			úloha.setJobName(zálohaNázvu);
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlač(String) tlač}.</p> */
	public boolean tlac(String názovDokumentu) { return tlač(názovDokumentu); }


	/**
	 * <p>Pokúsi sa spustiť proces tlače dokumentu s vyvolaním systémového
	 * tlačového dialógu pred samotným procesom tlače. Ak je dialóg
	 * používateľom zrušený, tlač nie je spustená.</p>
	 * 
	 * @return ak bola tlač spustená, tak {@code valtrue}, inak
	 *     {@code valfalse} (pri zrušení dialógu alebo chybe spustenia tlače)
	 * 
	 * @see #tlač()
	 * @see #tlač(String)
	 * @see #tlačDialógom(String)
	 */
	public boolean tlačDialógom()
	{
		if (úloha.printDialog()) return tlač();
		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačDialógom() tlačDialógom}.</p> */
	public boolean tlacDialogom() { return tlačDialógom(); }


	/**
	 * <p>Pokúsi sa spustiť proces tlače dokumentu (tejto inštancie)
	 * s vyvolaním systémového tlačového dialógu pred samotným procesom
	 * tlače, pričom dokumentu nastaví zadaný názov. Ak je dialóg
	 * používateľom zrušený, tlač nie je spustená.</p>
	 * 
	 * @param názovDokumentu názov tlačeného dokumentu
	 * @return ak bola tlač spustená, tak {@code valtrue}, inak
	 *     {@code valfalse} (pri zrušení dialógu alebo chybe spustenia tlače)
	 * 
	 * @see #tlač()
	 * @see #tlač(String)
	 * @see #tlačDialógom()
	 */
	public boolean tlačDialógom(String názovDokumentu)
	{
		if (úloha.printDialog()) return tlač(názovDokumentu);
		return false;
	}

	/** <p><a class="alias"></a> Alias pre {@link #tlačDialógom(String) tlačDialógom}.</p> */
	public boolean tlacDialogom(String názovDokumentu)
	{ return tlačDialógom(názovDokumentu); }
}
