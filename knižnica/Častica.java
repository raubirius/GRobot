
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

// ------------------------ //
//  *** Trieda Častica ***  //
// ------------------------ //

/**
 * <p>Toto je pomocná trieda určená na tvorbu časticových simulácií.
 * Implementuje dve základné rozhrania {@link Poloha Poloha}
 * a {@link Smer Smer}, čiže táto trieda obsahuje iba metódy na zistenie
 * polohy a smeru častice, ostatnú funkcionalitu je potrebné
 * doprogramovať.</p>
 * 
 * <p>Výhodou takejto implementácie je jej odľahčenosť a široké možnosti
 * použitia. Častica rezervuje omnoho menej prostriedkov ako robot, ale
 * dá sa použiť na mnohých miestach v programovacom rámci GRobot namiesto
 * robota alebo iného objektu implementujúceho rozhranie polohy alebo
 * smeru.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento príklad ukazuje využitie triedy {@code currČastica} pri
 * implementácii simulácie ohňostroja.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} Ohňostroj {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Vnorená súkromná trieda reprezentujúca raketu ohňostroja. Je}
		{@code comm// odvodená od triedy Častica a rozširuje ju len mierne. Definuje}
		{@code comm// novú metódu (reakciu) aktivita, ktorá sa stará o fungovanie aktívnej}
		{@code comm// rakety (spúšťa rovnomennú reakciu aktívnych čiastočiek – pozri}
		{@code comm// nižšie).}
		{@code kwdprivate} {@code typeclass} Raketa {@code kwdextends} {@code currČastica}
		{
			{@code comm// Ďalšia vnorená tried (druhá úroveň), ktorá reprezentuje čiastočku}
			{@code comm// vybuchnutej rakety. Tiež je odvodená od triedy Častica, no}
			{@code comm// rozširuje ju o málo viac než trieda Raketa. Čiastočka totiž musí}
			{@code comm// mať okrem aktivity definovanú ešte rýchlosť a zrýchlenie}
			{@code comm// (vlastnosti, ktoré má robot definované predvolene, ale použitie}
			{@code comm// robota by nebolo efektívne – bolo by to ako použiť Ferrari na}
			{@code comm// jazdu do obchodu, ktorý je krížom cez ulicu). Metóda reset slúži}
			{@code comm// na opätovné nastavenie počiatočných hodnôt vlastností čiastočky.}
			{@code kwdprivate} {@code typeclass} Čiastočka {@code kwdextends} {@code currČastica}
			{
				{@code comm// Atribút rýchlosti posunu v smere osi x.}
				{@code kwdpublic} {@code typedouble} vx = {@code num0};

				{@code comm// Atribút rýchlosti posunu v smere osi y.}
				{@code kwdpublic} {@code typedouble} vy = {@code num0};

				{@code comm// Atribút zrýchlenia posunu v smere osi x.}
				{@code kwdpublic} {@code typedouble} ax = {@code num0};

				{@code comm// Atribút zrýchlenia posunu v smere osi y.}
				{@code kwdpublic} {@code typedouble} ay = {@code num0};

				{@code comm// Atribút životnosti čiastočky v tikoch.}
				{@code kwdpublic} {@code typeint} život = {@code num0};

				{@code comm// Definovanie vlastnej reakcie na časovač – má za úlohu}
				{@code comm// zabezpečiť činnosť aktívnych čiastočiek a tiež postupné}
				{@code comm// ubúdanie života čiastočky.}
				{@code kwdpublic} {@code typevoid} aktivita()
				{
					{@code kwdif} (život &gt; {@code num0})
					{
						{@link Častica#x x} += vx;
						{@link Častica#y y} += vy;
						vx += ax;
						vy += ay;
						--život;
					}
				}

				{@code comm// Táto metóda slúži na nastavenie počiatočných hodnôt}
				{@code comm// vlastností čiastočky.}
				{@code kwdpublic} {@code typevoid} reset()
				{
					{@code comm// Nastavenie súradníc podľa nadradenej triedy rakety:}
					{@link Častica#x x} = Raketa.{@code valthis}.{@link Častica#x x};
					{@link Častica#y y} = Raketa.{@code valthis}.{@link Častica#y y};

					{@code comm// Voľba nových parametrov (využíva sa aktuálna inštancia}
					{@code comm// najvyššej hierarchicky, nie dedične, nadradenej triedy):}
					Ohňostroj.{@code valthis}.{@link GRobot#domov() domov}();
					Ohňostroj.{@code valthis}.{@link GRobot#náhodnýSmer() náhodnýSmer}();
					{@code typedouble} a = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}(-{@code num0.25}, -{@code num0.8});
					{@code typedouble} v = {@link Svet Svet}.{@link Svet#náhodnéReálneČíslo(double, double) náhodnéReálneČíslo}({@code num8}, {@code num10});

					{@code comm// Nastavenie nového zrýchlenia:}
					Ohňostroj.{@code valthis}.{@link GRobot#dopredu(double) dopredu}(a);
					ax = Ohňostroj.{@code valthis}.{@link GRobot#polohaX() polohaX}();
					ay = Ohňostroj.{@code valthis}.{@link GRobot#polohaY() polohaY}();

					{@code comm// Nastavenie novej (počiatočnej) rýchlosti:}
					Ohňostroj.{@code valthis}.{@link GRobot#dopredu(double) dopredu}(v &#45; a);
					vx = Ohňostroj.{@code valthis}.{@link GRobot#polohaX() polohaX}();
					vy = Ohňostroj.{@code valthis}.{@link GRobot#polohaY() polohaY}();

					{@code comm// Gravitácia:}
					ay -= {@code num0.1};

					{@code comm// Náhodná dĺžka života:}
					život = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num8}, {@code num16});
				}
			}

			{@code comm// Vnútorný zoznam čiastočiek každej rakety. (V konštruktore nižšie}
			{@code comm// je určené, že každá raketa bude obsahovať presne 50 čiastočiek.)}
			{@code kwdprivate} {@code kwdfinal} {@link Zoznam Zoznam}&lt;Čiastočka&gt; čiastočky = {@code kwdnew} {@link Zoznam#Zoznam() Zoznam}&lt;Čiastočka&gt;();

			{@code comm// Konštruktor rakety.}
			{@code kwdpublic} Raketa()
			{
				{@code comm// Vytvorenie 50 čiastočiek:}
				{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num50}; ++i)
					čiastočky.pridaj({@code kwdnew} Čiastočka());
			}

			{@code comm// Táto metóda sa stará o „štart“ rakety. V tejto implementácii je to}
			{@code comm// iba odpálenie rakety na náhodnej pozícii (raketa nevyletuje).}
			{@code kwdpublic} {@code typevoid} štart()
			{
				{@link #náhodnáPoloha() náhodnáPoloha}();
				{@code kwdfor} (Čiastočka čiastočka : čiastočky) čiastočka.reset();
			}

			{@code comm// Definovanie vlastnej reakcie na časovač – má za úlohu}
			{@code comm// zabezpečiť činnosť a kreslenie aktívnych rakiet.}
			{@code kwdpublic} {@code typevoid} aktivita()
			{
				{@code kwdfor} (Čiastočka čiastočka : čiastočky)
					{@code kwdif} (čiastočka.život &gt; {@code num0})
					{
						čiastočka.aktivita();
						{@link GRobot#skočNa(Poloha) skočNa}(čiastočka);
						{@link GRobot#krúžok() krúžok}();
					}
			}
		}

		{@code comm// Vnútorný zoznam rakiet ohňostroja. (V konštruktore je ich vytvorených}
		{@code comm// desať.)}
		{@code kwdprivate} {@code kwdfinal} {@link Zoznam Zoznam}&lt;Raketa&gt; rakety = {@code kwdnew} {@link Zoznam#Zoznam() Zoznam}&lt;Raketa&gt;();

		{@code comm// Konštruktor ohňostroja.}
		{@code kwdprivate} Ohňostroj()
		{
			{@code comm// Nastavenie parametrov jedinej inštancie robota, ktorá bude použitá}
			{@code comm// na kreslenie čiastočiek rakiet:}
			{@link GRobot#zdvihniPero() zdvihniPero}();
			{@link GRobot#veľkosť(double) veľkosť}({@code num3});
			{@link GRobot#skry() skry}();

			{@code comm// Pridanie desiatich rakiet do vnútorného zoznamu:}
			{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num10}; ++i)
				rakety.{@link Zoznam#pridaj(Object) pridaj}({@code kwdnew} Raketa());

			{@code comm// Spustenie časovača (inak by ohňostroj nefungoval):}
			{@link Svet Svet}.{@link Svet#spustiČasovač() spustiČasovač}();
		}

		{@code comm// Po každom kliknutí sa odpáli ďalšia raketa (po odpálení poslednej}
		{@code comm// sa pokračuje opäť prvou).}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#klik() klik}()
		{
			rakety.{@link Zoznam#ďalší() ďalší}().štart();
		}

		{@code comm// Pri každom tiku sa prekreslí obrazovka a zabezpečí sa fungovanie}
		{@code comm// aktívnych rakiet.}
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#tik() tik}()
		{
			{@link Svet Svet}.{@link Svet#vymažGrafiku() vymažGrafiku}();
			{@code kwdfor} (Raketa raketa : rakety) raketa.aktivita();
		}

		{@code comm// Hlavná metóda.}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}... args)
		{
			Svet.použiKonfiguráciu({@code srg"ohnostroj.cfg"});
			{@code kwdnew} Ohňostroj();
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>ohnostroj.png<alt/>Ohňostroj (ukážka).</image>Ukážka
 * náhodného výsledku po niekoľkých klikoch.</p>
 * 
 * <p> </p>
 * 
 * <p>Ďalší príklad, ktorý používa triedu {@code currČastica} je v opise
 * metódy {@link Súbor#vnorMennýPriestorVlastností(String)
 * vnorMennýPriestorVlastností}</p>
 */
public class Častica implements Poloha, Smer
{
	/**
	 * <p>Atribút súradnice x polohy častice.</p>
	 */
	public double x = 0;

	/**
	 * <p>Atribút súradnice y polohy častice.</p>
	 */
	public double y = 0;

	/**
	 * <p>Atribút smeru častice.</p>
	 */
	public double uhol = 90;

	/** <p><a class="getter"></a> Metóda vráti súradnicu x polohy častice.</p> */
	public double polohaX() { return x; }

	/** <p><a class="getter"></a> Metóda vráti súradnicu y polohy častice.</p> */
	public double polohaY() { return y; }

	/** <p><a class="getter"></a> Metóda vráti súradnicu x polohy častice.</p> */
	public double súradnicaX() { return x; }

	/** <p><a class="getter"></a> Metóda vráti súradnicu x polohy častice.</p> */
	public double suradnicaX() { return x; }

	/** <p><a class="getter"></a> Metóda vráti súradnicu y polohy častice.</p> */
	public double súradnicaY() { return y; }

	/** <p><a class="getter"></a> Metóda vráti súradnicu y polohy častice.</p> */
	public double suradnicaY() { return y; }

	/** <p><a class="getter"></a> Metóda vráti objekt spájajúce obidve súradnice polohy častice.</p> */
	public Bod poloha() { return new Bod(x, y); }


	// /**
	//  * <p><a class="setter"></a> Metóda nastaví nové súradnice
	//  * častice podľa zadaného bodu.</p>
	//  * 
	//  * @param bod bod určujúci novú polohu častice
	//  */
	// public void poloha(Point2D bod)
	// {
	// 	x = bod.getX();
	// 	y = bod.getY();
	// }

	/**
	 * <p><a class="setter"></a> Metóda nastaví nové súradnice častice
	 * podľa zadaného bodu.</p>
	 * 
	 * @param bod bod určujúci novú polohu častice
	 */
	public void poloha(Poloha bod)
	{
		x = bod.polohaX();
		y = bod.polohaY();
	}


	/**
	 * <p>Overí, či sa poloha tejto častice dokonale zhoduje so zadanými
	 * súradnicami. Ak je zistená zhoda, tak metóda vráti hodnotu {@code 
	 * valtrue}, v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param x x-ová súradnica, s ktorou má byť porovnaná poloha tejto častice
	 * @param y y-ová súradnica, s ktorou má byť porovnaná poloha tejto častice
	 * @return {@code valtrue} ak sa poloha tejto častice zhoduje so zadanými
	 *     súradnicami, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(double x, double y)
	{
		return this.x == x && this.y == y;
	}

	/**
	 * <p>Overí, či sa poloha tejto častice a poloha zadaného objektu dokonale
	 * zhodujú. Ak je zistená zhoda, tak metóda vráti hodnotu {@code valtrue},
	 * v opačnom prípade hodnotu {@code valfalse}.</p>
	 * 
	 * @param poloha objekt, ktorého poloha má byť porovnaná s polohou tejto
	 *     častice
	 * @return {@code valtrue} ak sa poloha tejto častice zhoduje s polohou
	 *     zadaného objektu, {@code valfalse} v opačnom prípade
	 */
	public boolean jeNa(Poloha poloha)
	{
		if (poloha instanceof Častica)
			return ((Častica)poloha).x == x && ((Častica)poloha).y == y;
		return poloha.polohaX() == x && poloha.polohaY() == y;
	}


	/** <p><a class="getter"></a> Metóda vráti smer častice.</p> */
	public double uhol() { return uhol; }

	/** <p><a class="getter"></a> Metóda vráti smer častice.</p> */
	public double smer() { return uhol; }


	/**
	 * <p><a class="setter"></a> Metóda nastaví nový smer častice.</p>
	 * 
	 * @param uhol hodnota určujúca nový uhol častice
	 */
	public void uhol(double uhol) { this.uhol = uhol; }

	/**
	 * <p><a class="setter"></a> Metóda nastaví nový smer častice.</p>
	 * 
	 * @param uhol hodnota určujúca nový uhol častice
	 */
	public void smer(double uhol) { this.uhol = uhol; }


	/**
	 * <p><a class="setter"></a> Metóda nastaví nový smer častice.</p>
	 * 
	 * @param uhol objekt určujúci nový uhol častice
	 */
	public void uhol(Smer objekt) { uhol = objekt.uhol(); }

	/**
	 * <p><a class="setter"></a> Metóda nastaví nový smer častice.</p>
	 * 
	 * @param uhol objekt určujúci nový uhol častice
	 */
	public void smer(Smer objekt) { uhol = objekt.uhol(); }


	/**
	 * <p>Vygeneruje pre časticu náhodné súradnice x a y, ktoré budú
	 * v rozsahu hraničných hodnôt plochy plátien sveta grafického
	 * robota.</p>
	 */
	public void náhodnáPoloha()
	{
		x = Svet.náhodnéReálneČíslo(Svet.najmenšieX(), Svet.najväčšieX());
		y = Svet.náhodnéReálneČíslo(Svet.najmenšieY(), Svet.najväčšieY());
	}

	/**
	 * <p>Vygeneruje pre časticu náhodnú hodnotu smeru.</p>
	 */
	public void náhodnýSmer()
	{
		uhol = Svet.náhodnéReálneČíslo(0, 360);
	}

	/**
	 * <p>Táto metóda je predvolene prázdna. Je určená na prekrytie
	 * v triedach odvodených od častice. Častica môže byť chápaná ako
	 * odľahčená verzia robota. O celú jej funkčnosť (vrátane aktivity
	 * alebo pasivity) sa musí postarať programátor, ktorý sa ju rozhodne
	 * rozšíriť a využiť na svoje účely. Aktivita (rovnako ako pasivita)
	 * častice nie je v programovacom rámci GRobot nikde vnútorne použitá,
	 * ale jej definícia v tejto triede poskytuje spoločný bod použitia
	 * všetkých odvodených tried.</p>
	 */
	public void aktivita() {}

	/**
	 * <p>Táto metóda je predvolene prázdna. Je určená na prekrytie
	 * v triedach odvodených od častice. Častica môže byť chápaná ako
	 * odľahčená verzia robota. O celú jej funkčnosť (vrátane aktivity
	 * alebo pasivity) sa musí postarať programátor, ktorý sa ju rozhodne
	 * rozšíriť a využiť na svoje účely. Pasivita (rovnako ako aktivita)
	 * častice nie je v programovacom rámci GRobot nikde vnútorne použitá,
	 * ale jej definícia v tejto triede poskytuje spoločný bod použitia
	 * všetkých odvodených tried.</p>
	 */
	public void pasivita() {}
}
