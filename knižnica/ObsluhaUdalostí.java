
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2018 by Roman Horváth
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
import java.io.IOException;

import static knižnica.Konštanty.VYKONAŤ_PRÍKAZ;

// -------------------------------- //
//  *** Trieda ObsluhaUdalostí ***  //
// -------------------------------- //

/**
 * <p>Trieda obsluhy udalostí slúži na obsluhu rozmanitých udalostí. Napríklad:
 * akcie myši, klávesnice, voľby položky ponuky, vymazania alebo
 * prekreslenia sveta… Keď chcete, aby aplikácia vytvorená s podporou
 * programovacieho rámca GRobot poskytovala funkcionalitu spojenú
 * s klávesnicou, myšou a podobne, je potrebné nasledujúcim spôsobom
 * vytvoriť inštanciu tejto triedy slúžiacej na obsluhu udalostí, pričom
 * stačí uviesť tie metódy (reakcie), ktoré plánujete využiť:</p>
 * 
 * <pre CLASS="example">
	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#tik() tik}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaPoložkyPonuky() voľbaPoložkyPonuky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zmenaPosunuLišty() zmenaPosunuLišty}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaSystémovejIkony() voľbaSystémovejIkony}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaSystémovejPoložky() voľbaSystémovejPoložky}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#vymazanie() vymazanie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#prekreslenie() prekreslenie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zobrazenieOkna() zobrazenieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#skrytieOkna() skrytieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#presunutieOkna() presunutieOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zmenaVeľkostiOkna() zmenaVeľkostiOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#aktiváciaOkna() aktiváciaOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#deaktiváciaOkna() deaktiváciaOkna}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ukončenie() ukončenie}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zrušenieÚdajov() zrušenieÚdajov}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#pohybMyši() pohybMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ťahanieMyšou() ťahanieMyšou}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#rolovanieKolieskomMyši() rolovanieKolieskomMyši}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#stlačenieKlávesu() stlačenieKlávesu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#uvoľnenieKlávesu() uvoľnenieKlávesu}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zadanieZnaku() zadanieZnaku}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klávesováSkratka() klávesováSkratka}() {}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#ťahanieSúborov() ťahanieSúborov}() {}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#pustenieSúboru(String) pustenieSúboru}(String súbor) {}

		{@code kwd@}Override {@code kwdpublic} {@link Color Color} {@link ObsluhaUdalostí#farbaAktívnehoSlova(String) farbaAktívnehoSlova}({@link String String} slovo) { {@code kwdreturn} {@code valnull}; }

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#aktiváciaOdkazu() aktiváciaOdkazu}() {}
		{@code comm// atď.}
	};
	</pre>
 * 
 * <p>Musí byť presne dodržaná syntax metód:
 * {@code kwd@}{@code Override }{@code kwdpublic }{@code 
 * typevoid }<em>«názov reakcie»</em>{@code () }<code>{ … }</code>, kde
 * <em>«názov reakcie»</em> je jeden z názvov vyššie uvedených reakcií.
 * <small>(Klauzula {@code Override} slúži na kontrolu. Keby sme ju
 * neuviedli a omylom urobili v názve metódy (reakcie) hoci nepatrný
 * preklep, obsluha určenej udalosti by nefungovala (aplikácia by
 * nereagovala).)</small> Medzi zložené zátvorky sa <code>{}</code>,
 * samozrejme, vpisuje kód, ktorý bude spustený v prípade, že nastane
 * (vznikne) prislúchajúca udalosť (napríklad používateľ stlačí kláves,
 * tlačidlo myši a podobne…).</p>
 * 
 * <p>Vytváranie obsluhy udalostí sa dá obísť tak, že prekryjete
 * prislúchajúce metódy ľubovoľného robota, ktoré sú na tento účel pripravené:
 * {@link GRobot GRobot}{@code .}{@link GRobot#tik() tik}{@code ()},
 * {@link GRobot GRobot}{@code .}{@link GRobot#klik() klik}{@code ()}…
 * Rozhranie robotov však nemusí poskytnúť úplný zoznam metód obsluhy
 * udalostí. Napríklad neposkytuje metódu {@link #ladenie(int, String, int)
 * ladenie}. Pri tých metódach, ktoré má k dispozícii platí, že metódy
 * obsluhy udalostí majú prednosť. Z toho dôvodu je možné, že ak nastane
 * počas spracovania metódy obsluhy udalostí výnimka, ktorá nebude v jej
 * tele zachytená, tak spracovanie udalosti robotmi nemusí nastať.</p>
 * 
 * <p>Pre jeden svet je možné <b>spravidla</b> definovať len
 * <b>jednu</b> inštanciu obsluhy udalostí. <small>(Výnimku z tohto
 * pravidla spomenieme neskôr.)</small> Pri pokuse o vytvorenie
 * viacerých inštancií vznikne výnimka oznamujúca, že obsluha udalostí
 * už bola definovaná.</p>
 * 
 * <p>Nasledujúci príklad demonštruje vytvorenie obsluhy udalostí na
 * {@linkplain #klik() klik myšou}. Robotovi najskôr nastavuje
 * {@linkplain GRobot#rýchlosť(double, boolean) rýchlosť}, inak by sa
 * nehýbal, a vzápätí je v obsluhe udalostí definované, aby sa robot po
 * {@linkplain #klik() kliknutí myšou} na plátno rozbehol smerom
 * {@linkplain GRobot#cieľNaMyš() na pozíciu, kam bolo kliknuté}.</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#rýchlosť(double, boolean) rýchlosť}({@code num10}, {@code valfalse});

	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link #klik() klik}()
		{
			{@link GRobot#cieľNaMyš() cieľNaMyš}();
		}
	};
	</pre>
 * 
 * <p><b>Ďalšie udalosti</b></p>
 * 
 * <p>Udalosti zverejnené vo vyššie uvedenom zozname môžeme považovať za
 * „štandardné“. (Z pohľadu programovacieho rámca GRobot.) Obsluha
 * udalostí podporuje ešte tri ďalšie udalosti, s pomocou ktorých je možné
 * využiť automaticky vytváraný konfiguračný súbor programovacieho rámca
 * GRobot. Sú to: {@link ObsluhaUdalostí#konfiguráciaZmenená()
 * konfiguráciaZmenená()},
 * {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor)
 * zapíšKonfiguráciu(súbor)}
 * a {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor)
 * čítajKonfiguráciu(súbor)}. Ich význam je podrobnejšie opísaný
 * v komentároch v príklade nižšie.</p>
 * 
 * <p>Automatická konfigurácia sa spúšťa príkazom
 * {@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}
 * pred vytvorením sveta. Predvolene je v konfigurácii uložená len
 * informácia o veľkosti a polohe hlavného okna aplikácie. K týmto údajom
 * je možné pridať skupinu vlastných konfiguračných údajov a to
 * nasledujúcim spôsobom:</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.{@link GRobot GRobot};

	{@code kwdpublic} {@code typeclass} TestKonfigurácie {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Stručný princíp:}
		{@code comm// }
		{@code comm//  1. Pre každú vlastnosť definujeme dvojicu atribútov.}
		{@code comm//     A) Jeden atribút bude slúžiť na uchovanie skutočnej hodnoty}
		{@code comm//        vlastnosti. S ním budeme pracovať v rámci celej aplikácie.}
		{@code comm//     B) Druhý atribút bude slúžiť na uchovanie počiatočnej (prečítanej)}
		{@code comm//        hodnoty vlastnosti. S ním budeme pracovať len pri čítaní}
		{@code comm//        počiatočnej hodnoty a overení toho, či nastala zmena hodnoty}
		{@code comm//        sledovanej vlastnosti.}
		{@code comm// }
		{@code comm//  2. Prekryjeme trojicu rekacií: konfiguráciaZmenená, zapíšKonfiguráciu}
		{@code comm//     a čítajKonfiguráciu.}
		{@code comm//     A) konfiguráciaZmenená – poskytuje od nás programovaciemu rámcu GRobot}
		{@code comm//        informáciu o tom, či je potrebné zapísať konfiguračný}
		{@code comm//        súbor, to jest, či bola konfigurácia zmenená…}
		{@code comm//     B) zapíšKonfiguráciu – slúži na zapísanie našich vlastných}
		{@code comm//        konfiguračných atribútov}
		{@code comm//     C) čítajKonfiguráciu – slúži na čítanie našich vlastných}
		{@code comm//        konfiguračných atribútov}
		{@code comm//}
		{@code comm//  3. Nesmieme zabudnúť spustiť príkaz Svet.použiKonfiguráciu… Tento}
		{@code comm//     príkaz musí byť spustený ešte pred inicializáciou (naštartovaním)}
		{@code comm//     sveta, to znamená – pred spustením konštruktora (pozri metódu main}
		{@code comm//     nižšie).}
		{@code comm//}


		{@code comm// Definícia vlastných konfiguračných vlastností:}
		{@code kwdprivate} {@code typeboolean} vlastnosť = {@code valfalse};         {@code comm// „skutočná“ vlastnosť}
		{@code kwdprivate} {@code typeboolean} prečítanáVlastnosť = {@code valfalse}; {@code comm// „tieň“ – detektor zmien}

		{@code comm// Prípadne ďalšie dvojice…}
		{@code comm// ...}

		{@code kwdprivate} TestKonfigurácie()
		{
			{@code comm// Toto tlačidlo bude meniť stav vlastnosti:}
			{@code kwdfinal} {@link Tlačidlo Tlačidlo} tlačidlo = {@code kwdnew} {@link Tlačidlo#Tlačidlo(java.lang.String) Tlačidlo}({@code srg"Zapnúť"});

			{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
			{
				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaTlačidla() voľbaTlačidla}()
				{
					{@code kwdif} (tlačidlo.{@link Tlačidlo#aktivované() aktivované}())
					{
						{@code comm// Ak bolo zvolené, zmení sa hodnota vlastnosti aj text}
						{@code comm// tlačidla}
						vlastnosť = !vlastnosť;
						tlačidlo.{@link Tlačidlo#text(java.lang.String) text}(vlastnosť ? {@code srg"Vypnúť"} : {@code srg"Zapnúť"});
					}
				}

				{@code comm// Na to, aby vlastná konfigurácia fungovala je nevyhnutné}
				{@code comm// naprogramovanie nasledujúcich troch reakcií:}

				{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link ObsluhaUdalostí#konfiguráciaZmenená() konfiguráciaZmenená}()
				{
					{@code kwdif} (vlastnosť != prečítanáVlastnosť) {@code kwdreturn} {@code valtrue};
					{@code comm// ...}

					{@code kwdreturn} {@code valfalse};
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} {@code java.io.IOException}
				{
					{@code comm// Zapisujeme hodnoty skutočných vlastností:}
					{@link Súbor súbor}.{@link Súbor#zapíšVlastnosť(java.lang.String, java.lang.Object) zapíšVlastnosť}({@code srg"vlastnosť"}, vlastnosť);
					{@code comm// ...}
				}

				{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#čítajKonfiguráciu(Súbor) čítajKonfiguráciu}({@link Súbor Súbor} súbor)
					{@code kwdthrows} {@code java.io.IOException}
				{
					{@code comm// Pri čítaní zmeníme naraz hodnotu skutočnej vlastnosti}
					{@code comm// aj hodnotu premennej prečítanáVlastnosť, ktorá slúži ako}
					{@code comm// detektor zmien a ktorá je vyhodnocovaná v reakcii}
					{@code comm// konfiguráciaZmenená.}
					vlastnosť = prečítanáVlastnosť = {@link Súbor súbor}.{@link Súbor#čítajVlastnosť(java.lang.String, java.lang.Boolean) čítajVlastnosť}(
						{@code srg"vlastnosť"}, vlastnosť).{@link Boolean#booleanValue() booleanValue}();
					{@code comm// ...}
				}
			};

			{@code comm// Pre korektnosť je po prečítaní konfigurácie vhodné upraviť}
			{@code comm// ovládacie prvky tak, aby ich vzhľad zodpovedal aktuálnej hodnote}
			{@code comm// vlastnosti s ktorou súvisia:}

			tlačidlo.{@link Tlačidlo#text(java.lang.String) text}(vlastnosť ? {@code srg"Vypnúť"} : {@code srg"Zapnúť"});
			{@code comm// ...}
		}

		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code comm// Dôležité je uviesť príkaz použiKonfiguráciu pred inicializáciou}
			{@code comm// aplikácie (pred spustením konštruktora). Inak by bol pokus}
			{@code comm// o vykonanie tohto príkazu zamietnutý}
			{@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}({@code srg"test.cfg"});
			{@code kwdnew} TestKonfigurácie();
		}
	}
	</pre>
 * 
 * Na to, aby mala vlastná konfigurácia zmysel (aby sa aktuálne hodnoty
 * konfiguračných premenných vždy korektne uložili pri ukončení aplikácie
 * a prečítali pri jej opätovnom štarte), je potrebné prekryť
 * (naprogramovať obsah) všetkých troch reakcií: {@link 
 * #konfiguráciaZmenená() konfiguráciaZmenená}, {@link 
 * #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu} a {@link 
 * #čítajKonfiguráciu(Súbor) čítajKonfiguráciu}. Syntax pri
 * prekrývaní je potrebné detailne dodržať, to znamená, že pri tých
 * reakciách, pri ktorých je to vyžadované, je nunté uviesť parameter
 * {@code Súbor} aj klauzulu {@code kwdthrows}.
 * 
 * <p><b>Výnimka z pravidla jedinej obsluhy udalostí</b></p>
 * 
 * <p>Pôvodne nebolo možné v tomto smere v programovacom rámci GRobot robiť
 * žiadne výnimky. Z bezpečnostných dôvodov bolo v rámci jednej aplikácie
 * možné vytvoriť len jednu obsluhu udalostí a tá bola pre stanovenú aplikáciu
 * konečná. Možnosť {@linkplain Svet#povoľViacnásobnúObsluhuUdalostí()
 * povolenia vytvárania viacerých inštancií obsluhy udalostí} pribudla
 * v priebehu vývoja, no odporúčame túto možnosť využiť len v nevyhnutnom
 * prípade. Príklad, ktorý používa zapnutie viacnásobného spracovania udalostí
 * nájdete pri opise metódy {@link Svet#presmerujObsluhuUdalostí(ObsluhaUdalostí)
 * presmerujObsluhuUdalostí}.</p>
 * 
 * @see Svet#povoľViacnásobnúObsluhuUdalostí()
 * @see Svet#presmerujObsluhuUdalostí(ObsluhaUdalostí)
 */
public class ObsluhaUdalostí
{

	// Počúvadlo udalostí
	/*packagePrivate*/ static ObsluhaUdalostí počúvadlo;

	// Príznak umožnenia vytvorenia viacerých inštancií obsluhy udalostí
	/*packagePrivate*/ static boolean povoľViacnásobnúObsluhuUdalostí = false;



	/**
	 * <p>Vráti aktuálny objekt obsluhy udalostí (aktívnu obsluhu udalostí)
	 * alebo {@code valnull}, ak nie je aktívna žiadna obsluha udalostí.
	 * (Túto metódu nie je možné prekryť – neslúži na obsluhu žiadnej
	 * udalosti.)</p>
	 * 
	 * @return aktívna obsluha udalostí
	 * 
	 * @see Svet#aktívnaObsluhaUdalostí()
	 */
	public final static ObsluhaUdalostí aktívna() { return počúvadlo; }

	/** <p><a class="alias"></a> Alias pre {@link #aktívna() aktívna}.</p> */
	public final static ObsluhaUdalosti aktivna()
	{ return (ObsluhaUdalosti)počúvadlo; }


	/**
	 * <p>Predvolený konštruktor obsluhy udalostí.</p>
	 * 
	 * @throws GRobotException ak nie je povolená viacnásobná obsluha
	 *     udalostí a už bola vytvorená inštancia obsluhy udalostí
	 *     (identifikátor {@code eventFactoryAlreadyExists})
	 */
	public ObsluhaUdalostí()
	{
		if (povoľViacnásobnúObsluhuUdalostí)
		{
			if (null == počúvadlo)
			{
				počúvadlo = this;
				Svet.čítajVlastnúKonfiguráciu();
			}
		}
		else
		{
			if (null != počúvadlo)
				throw new GRobotException(
					"Obsluha udalostí už bola definovaná!",
					"eventFactoryAlreadyExists");
			počúvadlo = this;
			Svet.čítajVlastnúKonfiguráciu();
		}
	}

	/**
	 * <p>Spustená v pravidelnom časovom intervale časovača, ktorý sa dá
	 * odštartovať metódou {@link Svet#spustiČasovač(double)
	 * spustiČasovač} a zastaviť metódou {@link Svet#zastavČasovač()
	 * zastavČasovač}. Informácie o poslednej udalosti časovača sa dajú
	 * získať pomocou metódy {@link ÚdajeUdalostí#tik()
	 * ÚdajeUdalostí.tik()}.</p>
	 * 
	 * <p>Časovač pre všetkých robotov automaticky spúšta metódu {@link 
	 * GRobot#pracuj() pracuj} a časovač môže byť niektorými metódami
	 * spustený automaticky. Pozri napríklad: {@link GRobot#rýchlosť(double)
	 * rýchlosť}, {@link  GRobot#uhlováRýchlosť(double) uhlováRýchlosť}…</p>
	 * 
	 * @see GRobot#tik()
	 */
	public void tik() {}

	/**
	 * <p>Spustená pri kliknutí tlačidlom myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#klik()
	 */
	public void klik() {}

	/**
	 * <p>Spustená pri zvolení položky ponuky. Na získanie naposledy zvolenej
	 * položky ponuky použite metódu {@link  ÚdajeUdalostí#položkaPonuky()
	 * ÚdajeUdalostí.položkaPonuky()} alebo využite metódu {@link 
	 * PoložkaPonuky#aktivovaná() aktivovaná} triedy {@link PoložkaPonuky
	 * PoložkaPonuky} na overenie aktivácie konkrétnej položky ponuky.</p>
	 * 
	 * @see GRobot#voľbaPoložkyPonuky()
	 */
	public void voľbaPoložkyPonuky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaPoložkyPonuky() voľbaPoložkyPonuky}.</p> */
	public void volbaPolozkyPonuky() {}

	/**
	 * <p>Spustená pri zvolení kontextovej položky. Na získanie naposledy
	 * zvolenej kontextovej položky použite metódu {@link 
	 * ÚdajeUdalostí#kontextováPoložka() ÚdajeUdalostí.kontextováPoložka()}
	 * alebo využite metódu {@link KontextováPoložka#aktivovaná()
	 * aktivovaná} triedy {@link KontextováPoložka KontextováPoložka} na
	 * overenie aktivácie konkrétnej kontextovej položky.</p>
	 * 
	 * @see GRobot#voľbaKontextovejPoložky()
	 */
	public void voľbaKontextovejPoložky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaKontextovejPoložky() voľbaKontextovejPoložky}.</p> */
	public void volbaKontextovejPolozky() {}


	/**
	 * <p>Spustená pri zvolení tlačidla. Použite metódu {@link 
	 * ÚdajeUdalostí#tlačidlo() ÚdajeUdalostí.tlačidlo()}
	 * na získanie inštancie naposledy zvoleného tlačidla, prípadne
	 * metódu {@link Tlačidlo Tlačidlo}{@code .}{@link 
	 * Tlačidlo#aktivované() aktivované}{@code ()} na overenie toho, či
	 * bolo toto tlačidlo naposledny aktivované. Praktický príklad
	 * nájdete v opise triedy {@link Tlačidlo Tlačidlo}.</p>
	 * 
	 * @see Tlačidlo
	 * @see GRobot#voľbaTlačidla()
	 */
	public void voľbaTlačidla() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaTlačidla() voľbaTlačidla}.</p> */
	public void volbaTlacidla() {}


	/**
	 * <p>Spustená pri zmene posunu rolovacej lišty. Použite metódu
	 * {@link ÚdajeUdalostí#rolovaciaLišta() ÚdajeUdalostí.rolovaciaLišta()}
	 * na získanie inštancie posunutej lišty, metódu
	 * {@link ÚdajeUdalostí#posunRolovacejLišty()
	 * ÚdajeUdalostí.posunRolovacejLišty()} na získanie konkrétnej
	 * hodnoty posunu lišty alebo metódu
	 * {@link ÚdajeUdalostí#udalosťPosunu() ÚdajeUdalostí.udalosťPosunu()}
	 * na získanie objektu udalosti rolovania
	 * {@link AdjustmentEvent AdjustmentEvent}.
	 * Praktický príklad nájdete v opise triedy {@link RolovaciaLišta
	 * RolovaciaLišta}.</p>
	 * 
	 * @see RolovaciaLišta
	 * @see GRobot#zmenaPosunuLišty()
	 */
	public void zmenaPosunuLišty() {}

	/** <p><a class="alias"></a> Alias pre {@link #zmenaPosunuLišty() zmenaPosunuLišty}.</p> */
	public void zmenaPosunuListy() {}


	/**
	 * <p>Spustená pri zvolení {@linkplain Svet#systémováIkona(String,
	 * Image, String...) systémovej ikony}. Použite metódu {@link 
	 * ÚdajeUdalostí#udalosťSystémovejIkony()
	 * ÚdajeUdalostí.udalosťSystémovejIkony()} na získanie všetkých
	 * údajov o tejto udalosti.</p>
	 * 
	 * @see ÚdajeUdalostí#udalosťSystémovejIkony()
	 * @see Svet#systémováIkona(String, Image, String...)
	 * @see #voľbaSystémovejPoložky()
	 */
	public void voľbaSystémovejIkony() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaSystémovejIkony() voľbaSystémovejIkony}.</p> */
	public void volbaSystemovejIkony() {}

	/**
	 * <p>Spustená pri zvolení položky kontextovej ponuky {@linkplain 
	 * Svet#systémováIkona(String, Image, String...) systémovej ikony}.
	 * Použite metódu {@link ÚdajeUdalostí#udalosťSystémovejIkony()
	 * ÚdajeUdalostí.udalosťSystémovejIkony()} na získanie všetkých
	 * údajov o tejto udalosti.</p>
	 * 
	 * @see ÚdajeUdalostí#udalosťSystémovejIkony()
	 * @see Svet#systémováIkona(String, Image, String...)
	 * @see #voľbaSystémovejIkony()
	 */
	public void voľbaSystémovejPoložky() {}

	/** <p><a class="alias"></a> Alias pre {@link #voľbaSystémovejPoložky() voľbaSystémovejPoložky}.</p> */
	public void volbaSystemovejPolozky() {}


	/**
	 * <p>Spustená po vymazaní grafiky sveta. Táto reakcia môže
	 * byť použitá na obnovenie (znovu nakreslenie) grafického obsahu
	 * {@linkplain Plátno plátien} po úplnom vymazaní. Je to
	 * alternatívny spôsob. Robot má dostatok nástrojov (metód) na
	 * kreslenie (a kreslenie každého robota môže byť nasmerované ako na
	 * {@linkplain GRobot#kresliNaPodlahu() podlahu}, tak na {@linkplain 
	 * GRobot#kresliNaStrop() strop}). Ak potrebujete využívať metódy
	 * triedy {@link Graphics2D Graphics2D}, použite metódu {@link 
	 * Plátno#grafika() podlaha.grafika()} (resp. {@link 
	 * Plátno#grafika() strop.grafika()}) na prístup k nej. Majte
	 * na pamäti, že grafika plátna pracuje s odlišným súradnicovým
	 * priestorom, než grafický programovací rámec.</p>
	 * 
	 * <p>V súčasnej verzii rámca je možné rozlišovať, ktoré plátno bolo
	 * práve vymazané. Slúžia na to metódy {@linkplain ÚdajeUdalostí údajov
	 * udalostí}: {@link ÚdajeUdalostí#vymazanéPlátno() vymazanéPlátno()}
	 * a {@link ÚdajeUdalostí#vymazanéPlátno(Plátno)
	 * vymazanéPlátno(plátno)}</p>
	 * 
	 * @see GRobot#vymazanie()
	 */
	public void vymazanie() {}

	/**
	 * <p>Spustená pri (pred) prekreslení sveta. Táto reakcia môže byť
	 * využitá na dokreslenie obsahu {@linkplain Plátno plátien}
	 * (ich „finálne úpravy“) pred prekreslením sveta. Robot má dostatok
	 * nástrojov (metód) na kreslenie (a kreslenie každého robota môže
	 * byť nasmerované ako na {@linkplain GRobot#kresliNaPodlahu()
	 * podlahu}, tak na {@linkplain GRobot#kresliNaStrop() strop}). Ak
	 * potrebujete využívať metódy (nástroje) triedy {@link Graphics2D
	 * Graphics2D}, použite metódu {@link Plátno#grafika()
	 * podlaha.grafika()} (resp. {@link Plátno#grafika()
	 * strop.grafika()}) na prevzatie grafického objektu, pomocou
	 * ktorého môžete tiež dokresľovať obsah plátien (v podstate
	 * rovnocenne ako pri kreslení pomocou robota, avšak treba mať na
	 * pamäti, že grafika plátna pracuje s odlišným súradnicovým
	 * priestorom, než grafický programovací rámec).</p>
	 * 
	 * @see GRobot#prekreslenie()
	 */
	public void prekreslenie() {}

	/**
	 * <p>Spustená po prekreslení sveta. Funguje podobne ako reakcia {@link 
	 * #prekreslenie() prekreslenie}, ibaže je spustená po prekreslení
	 * sveta, tesne pred finálnym zobrazením grafiky v okne (na
	 * obrazovke). Takže v tomto momente je čas na posledné finálne
	 * úpravy obsahu, ktorý bude reálne zobrazený na obrazovke. Čokoľvek
	 * sa v tomto momente pokúsite nakresliť na {@linkplain 
	 * Plátno plátno} podlahy alebo stropu, už nestihne byť
	 * zobrazené v tomto pracovnom cykle, avšak môžete použiť grafický
	 * objekt sveta (získaný metódou {@link Svet#grafika()
	 * Svet.grafika()}) na dokreslenie (krátkodobého) grafického obsahu.
	 * Treba podotknúť že spomenutý objekt {@code grafika} pracuje
	 * v súradnicovom priestore Javy a všetko, čo pomocou neho
	 * nakreslíte, bude na obrazovke zobrazené len do najbližšieho
	 * prekreslenia.
	 * (O súradnicových priestoroch sa podrobnejšie píše napríklad
	 * v opisoch metód {@link GRobot#cesta() GRobot.cesta()}, {@link 
	 * SVGPodpora#zapíš(String, String, boolean) SVGpodpora.zapíš(…)},
	 * {@link SVGPodpora#čítaj(String) SVGpodpora.čítaj(meno)} a priebežne
	 * v celej dokumentácii.)</p>
	 * 
	 * @see GRobot#dokreslenie()
	 */
	public void dokreslenie() {}

	/**
	 * <p>Spustená pri zobrazení okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * @see GRobot#zobrazenieOkna()
	 */
	public void zobrazenieOkna() {}

	/**
	 * <p>Spustená pri skrytí okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Ak {@linkplain Svet#skry()
	 * skryjete} okno sveta a neponecháte systému žiadnu možnosť
	 * interakcie s aplikáciou (napríklad {@linkplain 
	 * Svet#systémováIkona(String, Image, String...) systémovú ikonu}),
	 * tak virtuálny stroj Javy po chvíli Vašu aplikáciu (pri nečinnosti)
	 * automaticky ukončí.</p>
	 * 
	 * @see GRobot#skrytieOkna()
	 */
	public void skrytieOkna() {}

	/**
	 * <p>Spustená pri presunutí okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}.</p>
	 * 
	 * @see GRobot#presunutieOkna()
	 */
	public void presunutieOkna() {}

	/**
	 * <p>Spustená pri zmene veľkosti okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu {@link 
	 * ÚdajeUdalostí#okno() ÚdajeUdalostí.okno()}. (Po vykonaní tejto
	 * metódy je v prípade, že sa pracovalo s {@linkplain 
	 * Plátno#vypíš(Object[]) výpismi textov} na podlahu alebo strop,
	 * spustené automatické prekreslenie. Ak je automatické prekreslenie
	 * {@linkplain Svet#nekresli() vypnuté}, musí sa o prekreslenie
	 * sveta pri zmene veľkosti okna {@linkplain Svet#prekresli()
	 * postarať programátor}.)</p>
	 * 
	 * @see GRobot#zmenaVeľkostiOkna()
	 */
	public void zmenaVeľkostiOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #zmenaVeľkostiOkna() zmenaVeľkostiOkna}.</p> */
	public void zmenaVelkostiOkna() {}

	/**
	 * <p>Spustená pri aktivácii okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * @see GRobot#aktiváciaOkna()
	 */
	public void aktiváciaOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #aktiváciaOkna() aktiváciaOkna}.</p> */
	public void aktivaciaOkna() {}

	/**
	 * <p>Spustená pri deaktivácii okna sveta. Na získanie objektu
	 * s podrobnejšími údajmi o tejto udalosti použite metódu
	 * {@link ÚdajeUdalostí#aktivitaOkna() ÚdajeUdalostí.aktivitaOkna()}.</p>
	 * 
	 * @see GRobot#deaktiváciaOkna()
	 */
	public void deaktiváciaOkna() {}

	/** <p><a class="alias"></a> Alias pre {@link #deaktiváciaOkna() deaktiváciaOkna}.</p> */
	public void deaktivaciaOkna() {}


	/**
	 * <p>Spustená pri ukončení aplikácie. V tejto reakcii je možné vykonať
	 * niektoré záverečné upratovacie akcie, napríklad uloženie stavu
	 * aplikácie a podobne.</p>
	 * 
	 * <p>Priorita spúšťania tejto obsluhy udalosti prekrytej v niektorom
	 * robotovi a v obsluhe udalostí je upravená tak, že udalosť v obsluhe
	 * udalostí je spustená pred automatickým uložením konfigurácie a udalosť
	 * v robotovi (prípadne vo viacerých robotoch) po ňom.</p>
	 * 
	 * @see GRobot#ukončenie()
	 */
	public void ukončenie() {}

	/** <p><a class="alias"></a> Alias pre {@link #ukončenie() ukončenie}.</p> */
	public void ukoncenie() {}

	/**
	 * <p>Spustená po stlačení klávesu {@code Enter} vo vstupnom riadku
	 * zobrazenom po zavolaní metódy {@link Svet#začniVstup()
	 * začniVstup}. Na získanie potvrdených údajov použite metódy
	 * {@link Svet#prevezmiReťazec() prevezmiReťazec},
	 * {@link Svet#prevezmiCeléČíslo() prevezmiCeléČíslo} alebo
	 * {@link Svet#prevezmiReálneČíslo() prevezmiReálneČíslo}.
	 * V prípade, že potvrdené údaje nie sú v správnom tvare (platí pre
	 * celé alebo reálne číslo), je prislúchajúcou metódou „prevezmi“
	 * vrátené {@code valnull}.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nepracuje
	 * so štandardným vstupom údajov, ktorý je schopný prijímať údaje
	 * z iného procesu. Pozri reakciu {@link #spracujRiadokVstupu(String)
	 * #spracujRiadokVstupu}.</p>
	 * 
	 * @see GRobot#potvrdenieÚdajov()
	 */
	public void potvrdenieÚdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #potvrdenieÚdajov() potvrdenieÚdajov}.</p> */
	public void potvrdenieUdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #potvrdenieÚdajov() potvrdenieÚdajov}.</p> */
	public void potvrdenieVstupu() {}

	/**
	 * <p>Spustená po pozitívnom spracovaní príkazu v {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívnom režime}.</p>
	 * 
	 * @see GRobot#spracovaniePríkazu()
	 */
	public void spracovaniePríkazu() {}

	/** <p><a class="alias"></a> Alias pre {@link #spracovaniePríkazu() spracovaniePríkazu}.</p> */
	public void spracovaniePrikazu() {}

	/**
	 * <p>Spustená po stlačení klávesu {@code ESC} vo vstupnom riadku
	 * zobrazenom po zavolaní metódy {@link Svet#začniVstup()
	 * začniVstup}.
	 * Reakciou na zrušenie vstupu môže byť napríklad {@linkplain 
	 * Svet#koniec() ukončenie} primitívnej priamočiarej aplikácie
	 * vyžadujúcej na svouju činnosť potvrdenie vstupu.</p>
	 * 
	 * @see GRobot#zrušenieÚdajov()
	 */
	public void zrušenieÚdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrusenieUdajov() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrušenieVstupu() {}

	/** <p><a class="alias"></a> Alias pre {@link #zrušenieÚdajov() zrušenieÚdajov}.</p> */
	public void zrusenieVstupu() {}

	/**
	 * <p>Asynchrónne prijíma údaje (ak sú k dispozícii) zo štandardného
	 * vstupu (externého zdroja), ktorý bol aktivovaný metódou {@link 
	 * Svet#aktivujŠtandardnýVstup() aktivujŠtandardnýVstup} (alebo
	 * niektorou jej verziou).</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Táto metóda nepracuje
	 * so zabudovaným vstupným riadkom programovacieho rámca. Pozri reakciu
	 * {@link #potvrdenieÚdajov() #potvrdenieÚdajov}.</p>
	 * 
	 * @param riadokVstupu riadok údajov prijatý zo štandardného vstupu
	 * 
	 * @see GRobot#spracujRiadokVstupu()
	 * @see #koniecVstupu()
	 * @see Svet#aktivujŠtandardnýVstup()
	 * @see Svet#aktivujŠtandardnýVstup(String)
	 * @see Svet#štandardnýVstupAktívny()
	 * @see Svet#čakajNaVstup()
	 */
	public void spracujRiadokVstupu(String riadokVstupu) {}

	/**
	 * <p>Táto reakcia je spustená len v prípade, že štandardný vstup
	 * (aktivovaný metódou {@link Svet#aktivujŠtandardnýVstup()
	 * aktivujŠtandardnýVstup} alebo niektorou jej verziou) je konečný.
	 * Jej spustenie znamená, že prúd údajov prijímaný zo štandardného
	 * vstupu dosiahol svoj koniec.</p>
	 * 
	 * @see GRobot#koniecVstupu()
	 * @see #spracujRiadokVstupu(String)
	 * @see Svet#aktivujŠtandardnýVstup()
	 * @see Svet#aktivujŠtandardnýVstup(String)
	 * @see Svet#štandardnýVstupAktívny()
	 * @see Svet#čakajNaVstup()
	 */
	public void koniecVstupu() {}


	/**
	 * <p>Spustená pri stlačení tlačidla myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#stlačenieTlačidlaMyši()
	 */
	public void stlačenieTlačidlaMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #stlačenieTlačidlaMyši() stlačenieTlačidlaMyši}.</p> */
	public void stlacenieTlacidlaMysi() {}

	/**
	 * <p>Spustená pri uvoľnení tlačidla myši. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#uvoľnenieTlačidlaMyši()
	 */
	public void uvoľnenieTlačidlaMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľnenieTlačidlaMyši() uvoľnenieTlačidlaMyši}.</p> */
	public void uvolnenieTlacidlaMysi() {}

	/**
	 * <p>Spustená pri pohybe myši nad plátnom podlahy. Použite metódu
	 * {@link ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši
	 * je aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#pohybMyši()
	 */
	public void pohybMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #pohybMyši() pohybMyši}.</p> */
	public void pohybMysi() {}

	/**
	 * <p>Spustená pri pohybe myši nad plátnom podlahy počas držania
	 * tlačidla myši. Použite metódu {@link ÚdajeUdalostí#myš()
	 * ÚdajeUdalostí.myš()} na získanie podrobnejších údajov o tejto
	 * udalosti. Stav myši je aktualizovaný aj vo vnútorných premenných
	 * sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see GRobot#ťahanieMyšou()
	 */
	public void ťahanieMyšou() {}

	/** <p><a class="alias"></a> Alias pre {@link #ťahanieMyšou() ťahanieMyšou}.</p> */
	public void tahanieMysou() {}

	/**
	 * <p>Spustená pri rolovaní kolieskom myši. Použite metódu {@link 
	 * ÚdajeUdalostí#myš() ÚdajeUdalostí.myš()} alebo {@link 
	 * ÚdajeUdalostí#kolieskoMyši() ÚdajeUdalostí.kolieskoMyši()} na
	 * získanie podrobnejších údajov o tejto udalosti. Stav myši je
	 * aktualizovaný aj vo vnútorných premenných sveta.</p>
	 * 
	 * @see ÚdajeUdalostí#tlačidloMyši1()
	 * @see ÚdajeUdalostí#tlačidloMyši2()
	 * @see ÚdajeUdalostí#tlačidloMyši3()
	 * @see ÚdajeUdalostí#myš()
	 * @see ÚdajeUdalostí#kolieskoMyši()
	 * @see ÚdajeUdalostí#súradnicaMyšiX()
	 * @see ÚdajeUdalostí#súradnicaMyšiY()
	 * @see ÚdajeUdalostí#rolovanieKolieskomMyšiX()
	 * @see ÚdajeUdalostí#rolovanieKolieskomMyšiY()
	 * @see GRobot#rolovanieKolieskomMyši()
	 */
	public void rolovanieKolieskomMyši() {}

	/** <p><a class="alias"></a> Alias pre {@link #rolovanieKolieskomMyši() rolovanieKolieskomMyši}.</p> */
	public void rolovanieKolieskomMysi() {}

	/**
	 * <p>Spustená pri stlačení klávesu. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#kláves() kláves()}
	 * a {@link ÚdajeUdalostí#kláves(int) kláves(int)}</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#stlačenieKlávesu()
	 */
	public void stlačenieKlávesu() {}

	/** <p><a class="alias"></a> Alias pre {@link #stlačenieKlávesu() stlačenieKlávesu}.</p> */
	public void stlacenieKlavesu() {}

	/**
	 * <p>Spustená pri uvoľnení klávesu. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#kláves() kláves()}
	 * a {@link ÚdajeUdalostí#kláves(int) kláves(int)}</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#uvoľnenieKlávesu()
	 */
	public void uvoľnenieKlávesu() {}

	/** <p><a class="alias"></a> Alias pre {@link #uvoľnenieKlávesu() uvoľnenieKlávesu}.</p> */
	public void uvolnenieKlavesu() {}

	/**
	 * <p>Spustená pri zadaní znaku z klávesnice. Nie každý kláves generuje
	 * túto udalosť. Táto udalosť vzniká len pri stlačení a uvoľnení
	 * klávesu, ktorý produkuje znak, to znamená, že pri klávesoch ako
	 * kurzorové šípky, {@code Shift}, {@code Ctrl}, {@code Cmd} a podobne
	 * táto udalosť nevzniká. Použite metódu
	 * {@link ÚdajeUdalostí#klávesnica() ÚdajeUdalostí.klávesnica()}
	 * na získanie podrobnejších údajov o tejto udalosti.
	 * Užitočné sú aj ďalšie metódy triedy {@link ÚdajeUdalostí
	 * ÚdajeUdalostí} uvedené v zozname nižšie.
	 * Pre túto udalosť najmä {@link ÚdajeUdalostí#znak() znak()}
	 * a {@link ÚdajeUdalostí#znak(char) znak(char)}</p>
	 * 
	 * @see ÚdajeUdalostí#klávesnica()
	 * @see ÚdajeUdalostí#kláves()
	 * @see ÚdajeUdalostí#kláves(int)
	 * @see ÚdajeUdalostí#znak()
	 * @see ÚdajeUdalostí#znak(char)
	 * @see GRobot#zadanieZnaku()
	 */
	public void zadanieZnaku() {}


	/**
	 * <p>Spustená pri zvolení {@linkplain Svet#pridajKlávesovúSkratku(String,
	 * int, int) klávesovej skratky}. Použite metódu {@link 
	 * ÚdajeUdalostí#príkazSkratky() ÚdajeUdalostí.príkazSkratky()} na
	 * získanie textu príkazu súvisiaceho s touto klávesovou skratkou,
	 * prípadne metódu {@link ÚdajeUdalostí#udalosťSkratky()
	 * ÚdajeUdalostí.udalosťSkratky()} na získanie doplňujúcich údajov
	 * o tejto udalosti.</p>
	 * 
	 * @see ÚdajeUdalostí#príkazSkratky()
	 * @see ÚdajeUdalostí#udalosťSkratky()
	 * @see GRobot#klávesováSkratka()
	 */
	public void klávesováSkratka() {}

	/** <p><a class="alias"></a> Alias pre {@link #klávesováSkratka() klávesováSkratka}.</p> */
	public void klavesovaSkratka() {}


	/* *
	 * <p>Táto reakcia je spustená vždy, keď sa má aplikácia vyjadriť, či je
	 * ochotná akceptovať pustenie súboru alebo nie. Aplikácia to môže
	 * zvážiť napríklad podľa polohy kurzora myši. Predvolenie je pustenie
	 * súboru zakázané.</p>
	 * 
	 * <p>Príklad najjednoduchšieho spracovania pustených súborov je
	 * v opise metódy {@link #pustenieSúboru(String)}.</p>
	 * 
	 * @return {@code valtrue} ak má byť pustenie súboru povolené,
	 *     inak {@code valfalse}
	 * /
	public boolean povoliťPustenieSúboru() { return false; }

	/** <p><a class="alias"></a> Alias pre {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}.</p> * /
	public boolean povolitPustenieSuboru() { return false; }

	Treba povoliť pustenie súborov prekrytím
	 * metódy {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}
	 * a spracovať súbory v tejto metóde (t. j. v metóde {@code 
	 * currpustenieSúboru} – tu ich len vypisujeme):

		{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@link #povoliťPustenieSúboru() povoliťPustenieSúboru}()
		{
			{@code kwdreturn} {@code valtrue};
		}*/

	/**
	 * <p>Táto reakcia je spúšťaná počas ťahania súboru a lebo súborov nad
	 * plátnom. To umožňuje aplikácii v prípade potreby graficky
	 * zareagovať.</p>
	 */
	public void ťahanieSúborov() {}

	/** <p><a class="alias"></a> Alias pre {@link #ťahanieSúborov() ťahanieSúborov}.</p> */
	public void tahanieSuborov() {}

	/**
	 * <p>Spustená po dokončení ťahania súboru z externej aplikácie. Ak bolo
	 * z externej aplikácie potiahnutých viac súborov, tak bude táto
	 * reakcia spustená pre každý z nich osobitne.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Toto je najjednoduchší príklad spracovania súborov potiahnutých
	 * a pustených nad plátnom.</p>
	 * 
	 * <pre CLASS="example">
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@code currpustenieSúboru}({@link String String} súbor)
		{
			{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}(súbor);
		}
		</pre>
	 * 
	 * @param súbor úplná cesta a meno súboru
	 * 
	 * @see GRobot#pustenieSúboru(String)
	 */
	public void pustenieSúboru(String súbor) {}

	/** <p><a class="alias"></a> Alias pre {@link #pustenieSúboru(String) pustenieSúboru}.</p> */
	public void pustenieSuboru(String súbor) {}


	/**
	 * <p>Spustená pri overovaní zmeny farby, ktorou má byť vypísané určité
	 * (zadané) aktívne slovo vnútornej konzoly. Pomocou tejto reakcie je
	 * možné paušálne meniť farbu všetkých aktívnych slov, odlišovať farby
	 * takých slov, ktoré už boli používateľom zvolené od takých, ktoré
	 * doteraz používateľ ani raz nezvolil a podobne. Ak si farbu aktívneho
	 * slova neželáme zmeniť, treba vrátiť hodnotu {@code valnull}.</p>
	 * 
	 * @param slovo identifikátor toho aktívneho slova vnútornej konzoly,
	 *     ktorého farba má byť ovplyvnená
	 * @return nová farba aktívneho slova alebo {@code valnull}
	 * 
	 * @see GRobot#farbaAktívnehoSlova(String)
	 * @see Plátno#vypíšAktívneSlovo(String, Object[])
	 */
	public Color farbaAktívnehoSlova(String slovo) { return null; }

	/** <p><a class="alias"></a> Alias pre {@link #farbaAktívnehoSlova(String) farbaAktívnehoSlova}.</p> */
	public Color farbaAktivnehoSlova(String slovo) { return null; }


	/**
	 * <p>Spustená po aktivácii hypertextového odkazu umiestneného v rámci
	 * niektorej z inštancií triedy {@link PoznámkovýBlok PoznámkovýBlok}.
	 * Užitočné doplňujúce údaje súvisiace s touto udalosťou poskytujú metódy
	 * {@link ÚdajeUdalostí#poslednýOdkaz() poslednýOdkaz} a {@link 
	 * ÚdajeUdalostí#poslednýPoznámkovýBlok() poslednýPoznámkovýBlok}.
	 * Metóda {@link Svet#otvorWebovýOdkaz(String} otvorWebovýOdkaz}
	 * umožňuje otvorenie poskytnutého odkazu v predvolenom webovom
	 * prehliadači.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Na to, aby klikanie na webové
	 * odkazy v poznámkovom bloku fungovalo, nesmie byť poznámkový blok
	 * v režime úprav – pozri metódy {@link PoznámkovýBlok#upraviteľný()
	 * upraviteľný}, {@link PoznámkovýBlok#upravuj() upravuj} a {@link 
	 * PoznámkovýBlok#neupravuj() neupravuj}.</p>
	 * 
	 * <!-- TODO skontrolovať vzhľad a funkčnosť odkazov príkladu použitia -->
	 * <p><b>Príklad:</b></p>
	 * 
	 * <p>Tento príklad ukazuje minimálne požiadavky, ktoré musia byť
	 * splnené na to, aby fungovalo klikanie na webové odkazy v poznámkovom
	 * bloku. Navyše je len zmena veľkosti plátna (volaním nadradeného
	 * konštruktora {@code valsuper} v prvom riadku konštruktora triedy)
	 * a nastavovanie automatického rozťahovania (výšky a šírky) poznámkového
	 * bloku. Ostatné prvky príkladu sú nevyhnutné.</p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.*;

		{@code kwdpublic} {@code typeclass} OtvoriťWebovýOdkaz {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} OtvoriťWebovýOdkaz()
			{
				{@code valsuper}({@link Svet Svet}.{@link Svet#šírkaZariadenia() šírkaZariadenia}(), {@link Svet Svet}.{@link Svet#výškaZariadenia() výškaZariadenia}());
				{@link PoznámkovýBlok PoznámkovýBlok} blok = {@code kwdnew} {@link PoznámkovýBlok#PoznámkovýBlok() PoznámkovýBlok}();
				blok.{@link PoznámkovýBlok#roztiahniNaŠírku() roztiahniNaŠírku}();
				blok.{@link PoznámkovýBlok#roztiahniNaVýšku() roztiahniNaVýšku}();
				blok.{@link PoznámkovýBlok#html(String) html}({@code srg"&lt;a href=\"http://pdf.truni.sk/\"&gt;Klikni na odkaz.&lt;/a&gt;"});
				blok.{@link PoznámkovýBlok#neupravuj() neupravuj}();
				{@link GRobot#skry() skry}();
			}

			{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktiváciaOdkazu() aktiváciaOdkazu}()
			{
				{@link Svet Svet}.{@link Svet#otvorWebovýOdkaz(String) otvorWebovýOdkaz}(
					{@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#poslednýOdkaz() poslednýOdkaz}());
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@code kwdnew} OtvoriťWebovýOdkaz();
			}
		}
		</pre>
	 * 
	 * <p><b>Výsledok:</b></p>
	 * 
	 * <p><image>vysledok-aktivacia-odkazu.png<alt/>Výsledok príkladu
	 * použitia aktivácie odkazu.</image>Výsledok príkladu použitia
	 * aktivácie odkazu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> V príklade je použité prekrytie
	 * reakcie {@link GRobot#aktiváciaOdkazu() aktiváciaOdkazu} v triede
	 * odvodenej od triedy {@link GRobot GRobot}. Princíp je však rovnaký
	 * aj pri použití reakcie z triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @see GRobot#aktiváciaOdkazu()
	 */
	public void aktiváciaOdkazu() {}

	/** <p><a class="alias"></a> Alias pre {@link #aktiváciaOdkazu() aktiváciaOdkazu}.</p> */
	public void aktivaciaOdkazu() {}


	/**
	 * <p>Výsledok (návratová hodnota) tejto reakcie môže byť overená pred
	 * zápisom {@linkplain Svet#použiKonfiguráciu() automatickej
	 * konfigurácie}. Ak iné súčasti programovacieho rámca oznámia, že
	 * konfiguráciu je potrebné uložiť, tak táto reakcia vôbec nemusí byť
	 * spustená. V prípade, že je spustená, tak ak je jej návratová hodnota
	 * rovná {@code valtrue}, tak bude spustená reakcia
	 * {@link #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}.
	 * (V opačnom prípade by bola uvedená reakcia spustená len
	 * vtedy, ak by bola zmenená niektorá z predvolených položiek
	 * {@linkplain Svet#použiKonfiguráciu() automatickej
	 * konfigurácie} – t. j. napríklad používateľ by presunul hlavné okno
	 * aplikácie, zmenil jeho veľkosť alebo by bol registrovaný niektorý
	 * robot na uloženie konfigurácie.)
	 * Príklad použitia je uvedený v hlavnej sekcii opisu triedy {@link 
	 * ObsluhaUdalostí ObsluhaUdalostí}.</p>
	 * 
	 * @return signál dovoľujúci programovaciemu rámcu overiť, či bola zmenená
	 *     aspoň jedna položka konfigurácie a či je potrebné zapísať
	 *     konfiguráciu na disk; ak majú byť zmenené prvky konfigurácie
	 *     korektne zapísané na disk je nevyhnutné zariadiť, aby
	 *     návratová hodnota prekrývajúcej verzie tejto metódy bola
	 *     rovná {@code valtrue}
	 * 
	 * @see #čítajKonfiguráciu(Súbor)
	 * @see #zapíšKonfiguráciu(Súbor)
	 * @see GRobot#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 */
	public boolean konfiguráciaZmenená() { return false; }

	/** <p><a class="alias"></a> Alias pre {@link #konfiguráciaZmenená() konfiguráciaZmenená}.</p> */
	public boolean konfiguraciaZmenena() { return false; }

	/**
	 * <p>Reakcia je vykonaná počas čítania {@linkplain 
	 * Svet#použiKonfiguráciu() automatickej konfigurácie}. Je určená na
	 * prečítanie hodnôt vlastnej konfigurácie. Príklad použitia je
	 * uvedený v hlavnej sekcii opisu triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} reprezentujúca
	 *     otvorený konfiguračný súbor pripravený na čítanie údajov
	 * 
	 * @see #konfiguráciaZmenená()
	 * @see #zapíšKonfiguráciu(Súbor)
	 * @see GRobot#čítajKonfiguráciu(Súbor)
	 * @see Svet#použiKonfiguráciu()
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void čítajKonfiguráciu(Súbor súbor) throws IOException {}

	/** <p><a class="alias"></a> Alias pre {@link #čítajKonfiguráciu(Súbor) čítajKonfiguráciu}.</p> */
	public void citajKonfiguraciu(Súbor subor) throws IOException {}

	/**
	 * <p>Reakcia je vykonaná počas zápisu {@linkplain 
	 * Svet#použiKonfiguráciu() automatickej konfigurácie}. Je určená na
	 * zápis hodnôt vlastnej konfigurácie. Príklad použitia je uvedený
	 * v hlavnej sekcii opisu triedy {@link ObsluhaUdalostí
	 * ObsluhaUdalostí}.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} reprezentujúca
	 *     otvorený konfiguračný súbor pripravený na zápis údajov
	 * 
	 * @see #konfiguráciaZmenená()
	 * @see #čítajKonfiguráciu(Súbor)
	 * @see GRobot#zapíšKonfiguráciu(Súbor)
	 * @see Svet#použiKonfiguráciu()
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void zapíšKonfiguráciu(Súbor súbor) throws IOException {}

	/** <p><a class="alias"></a> Alias pre {@link #zapíšKonfiguráciu(Súbor) zapíšKonfiguráciu}.</p> */
	public void zapisKonfiguraciu(Súbor subor) throws IOException {}

	/**
	 * <p>Táto reakcia súvisí s časovo náročným spracovaním údajov počas
	 * práce s pevným diskom. Napríklad s {@link Obrázok#čítaj(String)
	 * čítaním} a {@link Obrázok#ulož(String, boolean) zápisom} sekvencie
	 * PNG súborov (pozri triedu {@link Obrázok Obrázok} a jej metódy
	 * {@link Obrázok#čítaj(String) čítaj(súbor)}
	 * a {@link Obrázok#ulož(String, boolean) ulož(súbor, prepísať)})
	 * alebo {@linkplain Súbor#kopíruj(String, String) kopírovaním
	 * súborov} a podobne. Typ udalosti, ktorá spôsobila spustenie tejto
	 * reakcie je určený prvým parametrom – {@code kódSpracovania}.
	 * Ostatné parametre sú naplnené podľa ich relevantnosti.</p>
	 * 
	 * @param kódSpracovania obsahuje kód činnosti, ktorá spôsobila
	 *     vznik tejto udalosti; aktuálne platné kódy sú:
	 *     {@link #ČÍTANIE_PNG_SEKVENCIE ČÍTANIE_PNG_SEKVENCIE},
	 *     {@link #CHYBA_ČÍTANIA_PNG_SEKVENCIE
	 *     CHYBA_ČÍTANIA_PNG_SEKVENCIE}, {@link #ZÁPIS_PNG_SEKVENCIE
	 *     ZÁPIS_PNG_SEKVENCIE}, {@link #ČÍTANIE_GIF_ANIMÁCIE
	 *     ČÍTANIE_GIF_ANIMÁCIE}, {@link #ZÁPIS_GIF_ANIMÁCIE
	 *     ZÁPIS_GIF_ANIMÁCIE}, {@link #KOPÍROVANIE_SÚBOROV
	 *     KOPÍROVANIE_SÚBOROV}, {@link #PRIPÁJANIE_SÚBOROV
	 *     PRIPÁJANIE_SÚBOROV}, {@link #POROVNANIE_SÚBOROV
	 *     POROVNANIE_SÚBOROV}, {@link #ODOVZDANIE_ÚDAJOV
	 *     ODOVZDANIE_ÚDAJOV} a {@link #PREVZATIE_ÚDAJOV
	 *     PREVZATIE_ÚDAJOV}
	 * @param zdroj objekt súvisiaci so zdrojom alebo {@code valnull};
	 *     napríklad v prípade kopírovania súborov je to {@linkplain 
	 *     String reťazec} s názvom zdrojového súboru, v prípade zápisu
	 *     PNG sekvencie je to {@linkplain BufferedImage obrázkový
	 *     objekt} so zdrojovou snímkou animácie a podobne
	 * @param cieľ objekt súvisiaci s cieľom alebo {@code valnull};
	 *     napríklad pri kopírovaní súborov je to {@linkplain String
	 *     reťazec} s názvom cieľového súboru; v prípade čítania PNG
	 *     sekvencie je to {@linkplain BufferedImage obrázkový objekt}
	 *     s cieľovou snímkou animácie (čiže tou, do ktorej sú ukladané
	 *     prečítané údaje) a podobne
	 * @param stav hodnota vyjadrujúca aktuálne spracovaný objem údajov
	 *     alebo {@code num-1}, ak táto hodnota nie je známa alebo je
	 *     irelevantná; môže to byť počet spracovaných bajtov, poradové
	 *     číslo snímky a podobne
	 * @param celkovo hodnota vyjadrujúca celkový objem údajov, ktorý má byť
	 *     spracovaný alebo {@code num-1}, ak táto hodnota nie je známa
	 *     alebo je irelevantná; môže to byť veľkosť súboru, celkový počet
	 *     snímok a podobne
	 */
	public void sekvencia(int kódSpracovania, Object zdroj,
		Object cieľ, long stav, long celkovo) {}

	/**
	 * <p>Táto reakcia je spustená pri zastavení prehrávania animácie
	 * obrázka. Reakcia prijíma inštanciu obrázka, ktorého prehrávanie bolo
	 * zastavené, pričom zastavenie prehrávania môže byť iniciované
	 * automaticky (pozri aj metódu obrázka {@link Obrázok#zastav() zastav})
	 * alebo na podnet používateľa (programátora).</p>
	 * 
	 * @param obrázok inštancia obrázka, ktorého prehrávanie bolo zastavené
	 */
	public void zastavenieAnimácie(Obrázok obrázok) {}

	/** <p><a class="alias"></a> Alias pre {@link #zastavenieAnimácie(Obrázok) zastavenieAnimácie}.</p> */
	public void zastavenieAnimacie(Obrázok obrázok) {}

	/**
	 * <p>Táto reakcia je spustená po ukončení externého procesu, ktorý
	 * spustila zadaná inštancia triedy {@link Svet.PríkazovýRiadok
	 * PríkazovýRiadok}. Číselný kód, ktorý prijíma táto reakcia v druhom
	 * parametri je návratový kód, ktorý vrátil proces pri svojom
	 * ukončení.</p>
	 * 
	 * @param príkazovýRiadok inštancia príkazového riadka, ktorá proces
	 *     spustila (jej zdedenými metódami
	 *     <code>ExecuteShellCommand.getRunCommand</code>,
	 *     <code>ExecuteShellCommand.getRunArguments</code>,
	 *     <code>ExecuteShellCommand.getCommandArray</code>
	 *     a <code>ExecuteShellCommand.getRunEnvironment</code>sa dajú
	 *     získať doplnkové informácie o procese v čase jeho spúšťania)
	 * @param návratovýKód kód, ktorý vrátil externý proces pri ukončení
	 */
	public void ukončenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok,
		int návratovýKód) {}

	/** <p><a class="alias"></a> Alias pre {@link #ukončenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok, int) ukončenieProcesu}.</p> */
	public void ukoncenieProcesu(Svet.PríkazovýRiadok príkazovýRiadok, int návratovýKód) {}

	/**
	 * <p>Táto reakcia je vykonávaná v {@linkplain 
	 * Svet#režimLadenia(boolean) režime ladenia}. Návratová hodnota
	 * reakcie je odpoveďou na otázku položenú {@linkplain 
	 * Svet#režimLadenia(boolean) režimom ladenia} nadradenému procesu
	 * prostredníctvom parametra {@code správa}. Hodnota parametra
	 * {@code správa} môže byť jedna z nasledujúcich:</p>
	 * 
	 * <p> </p>
	 * 
	 * <table class="commands">
	 * <tr><td>{@link #VYPÍSAŤ_PREMENNÉ VYPÍSAŤ_PREMENNÉ}</td><td
	 * >–</td><td>Má režim ladenia vypísať obsah všetkých premenných
	 * (pred začatím vykonávania skriptu)?</td></tr>
	 * <tr><td>{@link #VYPÍSAŤ_MENOVKU VYPÍSAŤ_MENOVKU}</td><td
	 * >–</td><td>Má režim ladenia vypísať (pred začatím vykonávania
	 * skriptu) definíciu menovky, ktorú našiel pri predbežnej analýze
	 * skriptu?</td></tr>
	 * <tr><td>{@link #VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK}</td><td>–</td><td>Má
	 * režim ladenia vypísať aktuálny riadok skriptu?</td></tr>
	 * <tr><td>{@link #ČAKAŤ ČAKAŤ}</td><td>–</td><td>Má režim ladenia
	 * čakať pred vykonaním riadka skriptu? Ak je odpoveď „áno“,
	 * tak je táto správa posielaná opakovane (každých 350 ms).</td></tr>
	 * <tr><td>{@link #PRERUŠIŤ PRERUŠIŤ}</td><td>–</td><td>Má režim
	 * prerušiť vykonávanie skriptu?</td></tr>
	 * <tr><td>{@link #ZABRÁNIŤ_VYKONANIU ZABRÁNIŤ_VYKONANIU}</td><td
	 * >–</td><td>Má režim v poslednej chvíli zabrániť vykonaniu
	 * príkazu s konkrétnymi hodnotami argumentov?</td></tr>
	 * <tr><td>{@link #VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}</td><td>–</td><td>Má
	 * režim vypísať ozvenu potvrdeného príkazu {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívneho režimu}?</td></tr>
	 * <tr><td>{@link #VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}</td><td>–</td><td>Má
	 * režim vykonať potvrdený príkaz {@linkplain 
	 * Svet#interaktívnyRežim(boolean) interaktívneho režimu}?</td></tr>
	 * <tr><td>{@link #UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}</td><td
	 * >–</td><td>{@linkplain Svet#spustiSkript(String[]) Vykonávanie
	 * skriptu v samostatnom vlákne} oznamuje, že jeho činnosť bola
	 * ukončená (bez chyby).</td></tr>
	 * <tr><td>{@link #UKONČENIE_CHYBOU UKONČENIE_CHYBOU}</td><td
	 * >–</td><td>{@linkplain Svet#spustiSkript(String[]) Vykonávanie
	 * skriptu v samostatnom vlákne} oznamuje, že jeho činnosť bola
	 * ukončená chybou. V parametri {@code riadok} je číslo riadka, na
	 * ktorom vznikla chyba a reťazec parametra {@code príkaz} obsahuje
	 * v tomto prípade jednoduchý text chybového hlásenia.</td></tr>
	 * 
	 * <tr><td>{@link #ČÍSELNÁ_PREMENNÁ ČÍSELNÁ_PREMENNÁ}<br />
	 * {@link #FAREBNÁ_PREMENNÁ FAREBNÁ_PREMENNÁ}<br />
	 * {@link #POLOHOVÁ_PREMENNÁ POLOHOVÁ_PREMENNÁ}<br />
	 * {@link #REŤAZCOVÁ_PREMENNÁ REŤAZCOVÁ_PREMENNÁ}</td><td
	 * >–</td><td>Má režim vypísať obsah premennej prislúchajúceho
	 * údajového typu? Táto otázka vznikne vždy, keď sa zmení hodnota
	 * premennej.</td></tr>
	 * </table>
	 * 
	 * <p> </p>
	 * 
	 * <p>Ak je {@linkplain Svet#režimLadenia(boolean) režim ladenia}
	 * zapnutý počas {@linkplain Svet#interaktívnyRežim(boolean)
	 * interaktívneho režimu}, tak pred vykonaním každého potvrdeného
	 * riadka vznikne otázka typu {@link #VYKONAŤ_PRÍKAZ
	 * VYKONAŤ_PRÍKAZ}.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@code kwdimport} knižnica.{@link GRobot GRobot};

		{@code kwdpublic} {@code typeclass} RežimLadeniaInteraktívne {@code kwdextends} {@link GRobot GRobot}
		{
			{@code kwdprivate} RežimLadeniaInteraktívne()
			{
				{@link GRobot#interaktívnyRežim(boolean) interaktívnyRežim}({@code valtrue});
				{@link Svet Svet}.{@link Svet#režimLadenia(boolean) režimLadenia}({@code valtrue});
				{@link Svet Svet}.{@link Svet#vypíš(Object[]) vypíš}({@code valthis});

				{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
				{
					{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#potvrdenieÚdajov() potvrdenieÚdajov}()
					{
						{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(červená);
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg"Chyba"});
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
					}

					{@code kwd@}Override {@code kwdpublic} {@code typeboolean} {@code currladenie}(
						{@code typeint} riadok, String príkaz, {@code typeint} správa)
					{
						{@code kwdswitch} (správa)
						{
						{@code kwdcase} {@link GRobot#VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}:
						{@code kwdcase} {@link GRobot#VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}:
							{@code kwdreturn} {@code valtrue};
						}

						{@code kwdif} (správa &lt; {@code num0}) {@code kwdreturn} {@code valtrue};

						{@link Farba Farba} záloha = {@link Svet Svet}.{@link Svet#farbaTextu() farbaTextu}();
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(purpurová);
						{@link Svet Svet}.{@link Svet#vypíšRiadok(Object[]) vypíšRiadok}({@code srg" × "}, príkaz);
						{@link Svet Svet}.{@link Svet#farbaTextu(Color) farbaTextu}(záloha);
						{@code kwdreturn} {@code valfalse};
					}
				};

				{@link Svet Svet}.{@link Svet#registrujRobota() registrujRobota}();
				{@link Svet Svet}.{@link Svet#čítajKonfiguráciuSveta() čítajKonfiguráciuSveta}();
			}

			{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
			{
				{@link Svet Svet}.{@link Svet#použiKonfiguráciu() použiKonfiguráciu}();
				{@link Svet Svet}.{@link Svet#nekresli() nekresli}();
				{@code kwdnew} RežimLadeniaInteraktívne();
				{@link Svet Svet}.{@link Svet#kresli() kresli}();
				{@link Svet Svet}.{@link Svet#aktivujVstupnýRiadok() aktivujVstupnýRiadok}();
			}
		}
		</pre>
	 * 
	 * @param riadok poradové číslo riadka skriptu alebo hodnota −1
	 *     (ktorá signalizuje, že číslo riadka nie je známe)
	 * @param príkaz príkaz skriptu, prípadne názov premennej
	 * @param správa typ správy, na ktorú sa režim pokúša získať odpoveď
	 *     od nadradeného procesu (pozri vyššie)
	 * @return návratová hodnota je odpoveďou na správu poslanú parametrom
	 *     <code>správa</code>; {@code valtrue} – kladná odpoveď na
	 *     niektorú z vyššie položených otázok; {@code valfalse} – záporná
	 *     odpoveď na niektorú z vyššie položených otázok
	 * 
	 * @see Svet#interaktívnyRežim(boolean)
	 * @see Svet#interaktívnaInštancia(String)
	 * @see Svet#režimLadenia(boolean)
	 * @see Svet#vykonajPríkaz(String)
	 * @see Svet#vykonajSkript(String[])
	 */
	public boolean ladenie(int riadok, String príkaz, int správa)
	{
		if (VYKONAŤ_PRÍKAZ == správa) return true;
		return false;
	}
}
