
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2024 by Roman Horváth
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

/**
 * <p>Trieda {@code currRad} umožňuje vytvárať inštancie generujúce pílové
 * číselné postupnosti. To znamená, že každá inštancia tejto triedy generuje
 * periodický stúpajúco-klesajúci číselný rad podľa zadaných hraníc
 * a kroku.</p>
 * 
 * <p><image>grafy-rad.svg<alt/>Ukážky pílových číselných
 * postupností.<onerror>grafy-rad.png</onerror></image>Ukážky pílových
 * číselných postupností.</p>
 * 
 * <p>Metóda {@link #daj() daj} generuje rad algoritmicky (pomocou
 * niekoľkých súkromných stavových premenných). Zjednodušene (s jednotkovým
 * krokom a bez korekcií hodnôt) je postup generovania takýto:</p>
 * 
 * <p>Ak je smer vzostupný, tak<br />
 *     zvýš hodnotu;<br />
 *     ak hodnota dosiahne vrchnú hranicu, tak<br />
 *         obráť smer;<br />
 * inak<br />
 *     zníž hodnotu;<br />
 *     ak hodnota dosiahne spodnú hranicu, tak<br />
 *         obráť smer.</p>
 * 
 * <p>Naproti tomu, metóda {@link #dajHodnotu(int) dajHodnotu}, ktorá
 * vyžaduje zadanie číselného parametra počíta členy radu aritmeticky.
 * Aritmetické riešenie generovania číselného radu (na zjednodušenie
 * s jednotkovým krokom) vyzerá takto:</p>
 * 
 * <p><b>Výpočet ľubovoľného člena radu podľa parametra pa:</b></p>
 * 
 * <p>vh − |(pa mod (2 × (vh − sh))) − (vh − sh)|</p>
 * 
 * <p><b>Kde:</b></p>
 * 
 * <p>sh – spodná hranica,<br />
 * vh – vrchná hranica,<br />
 * pa – parameter („fáza“ výpočtu radu),</p>
 * 
 * <p>mod – operátor zvyšku po delení (v Jave symbol {@code %}),<br />
 * |…| – absolútna hodnota.</p>
 * 
 * <p><b>Po zjednodušení s použitím pomocných premenných:</b></p>
 * 
 * <p>vh − |(pa mod de) − rh|</p>
 * 
 * <p><b>Kde:</b></p>
 * 
 * <p>rh – rozdiel hraníc: vh − sh,<br />
 * de – deliteľ: 2 × rh.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Tento jednoduchý príklad používa dva rady na plynulé nastavovanie
 * horizontálnej a vertikálnej rýchlosti robota.</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} PríkladPoužitiaRadu {@code kwdextends} GRobot
	{
		{@code kwdprivate} {@code currRad} x = {@code kwdnew} {@link Rad#Rad(int, int) Rad}(&#45;{@code num2000}, {@code num1000}, {@code num20});
		{@code kwdprivate} {@code currRad} y = {@code kwdnew} {@link Rad#Rad(int, int) Rad}(&#45;{@code num1000}, {@code num2000}, {@code num30});

		{@code kwdprivate} PríkladPoužitiaRadu()
		{
			{@code valsuper}({@code num400}, {@code num400});
			{@link GRobot#ohranič() ohranič}();
			{@link GRobot#aktivuj() aktivuj}();
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link GRobot#aktivita() aktivita}()
		{
			{@link GRobot#rýchlosťPosunu(double) rýchlosťPosunu}(x.{@link Rad#daj() daj}() / {@code num1000.0});
			{@link GRobot#rýchlosť(double) rýchlosť}(y.{@link Rad#daj() daj}() / {@code num1000.0});
		}

		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code kwdnew} PríkladPoužitiaRadu();
			{@link Svet Svet}.{@link Svet#zbaľ() zbaľ}();
		}
	}
	</pre>
 * 
 * <p><image>priklad-pouzitia-radu-1.png<alt/>Výsledok príkladu použitia
 * radu.</image>Ukážka niekoľkých prvých sekúnd kreslenia príkladu použitia
 * radu.</p>
 * 
 * <p>Úpravou parametrov konštruktorov radov {@code x} a {@code y}:</p>
 * 
 * <pre CLASS="example">
	{@code kwdprivate} {@code currRad} x = {@code kwdnew} Rad(-{@code num1800}, {@code num800}, {@code num20});
	{@code kwdprivate} {@code currRad} y = {@code kwdnew} Rad(-{@code num1100}, {@code num1400}, {@code num30});
	</pre>
 * 
 * <p>a prípadne aj deliteľov rýchlostí robota:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#rýchlosťPosunu(double) rýchlosťPosunu}(x.{@link Rad#daj() daj}() / {@code num800.0});
	{@link GRobot#rýchlosť(double) rýchlosť}(y.{@link Rad#daj() daj}() / {@code num800.0});
	</pre>
 * 
 * <p>môžeme dosiahnuť zaujímavé výsledky:</p>
 * 
 * <p><image>priklad-pouzitia-radu-1b.png<alt/>Príklad použitia
 * radu po úprave.</image>Ukážka kreslenia príkladu použitia radu
 * po úpravách.</p>
 * 
 * <p>Samozrejme, že tu sa možnosti použitia generátora radu nekončia.
 * Použiť sa dá všade, kde je výhodné využívať cyklickú postupnosť,
 * napríklad na priame určovanie polohy pendlujúceho nepriateľa
 * a podobne.</p>
 */
public class Rad
{
	// Spodná hranica radu.
	private int spodnáHranica = 0;

	// Vrchná hranica radu.
	private int vrchnáHranica = 9;

	// Miera prírastku resp. úbytku hodnôt radu.
	private int krok = 1;

	// Aktuálna hodnota radu.
	private int hodnota = 0;

	// Smer zmeny (nárastu alebo poklesu) hodnôt radu.
	private boolean vzostupne = true;


	// Súkromná metóda na korekciu aktuálnej hodnoty radu podľa
	// konfigurácie tejto inštancie.
	private void napravHodnotu()
	{
		if (hodnota < spodnáHranica)
			hodnota = spodnáHranica;
		else if (hodnota > vrchnáHranica)
			hodnota = vrchnáHranica;

		if (1 != krok)
		{
			int zvyšok = (hodnota - spodnáHranica) % krok;
			if (0 != zvyšok) hodnota -= zvyšok;
		}
	}


	/**
	 * <p>Predvolený konštruktor. Ponechá všetky predvolené nastavenia
	 * inštancie, čo znamená: spodnú hranicu na hodnote 0, vrchnú na 9,
	 * krok (mieru zmeny pohybu) na 1, počiatočnú hodnotu na 0 a smer
	 * (zmeny hodnôt) na vzostupný.</p>
	 */
	public Rad() {}

	/**
	 * <p>Konštruktor nastavujúci generátoru radu nové predvolené hranice.
	 * Na to, aby generátor fungoval správne, musí byť medzi hranicami vždy
	 * minimálne jednotkový rozdiel, preto ak zú zadané hodnoty hraníc
	 * rovnaké, tak je vrchná hranica zväčšená o jednotku. Ak je zadaná
	 * spodná hranica väčšia, než vrchná, tak sú hranice vymenené
	 * a predvolene je nastavený zostupný smer zmeny hodnôt. Počiatočná
	 * hodnota inštancie je potom nastavená na výslednú hodnotu spodnej
	 * hranice.</p>
	 * 
	 * @param spodnáHranica požadovaná spodná hranica radu (ak je väčšia od
	 *     vrchnej hranice, tak sa stáva vrchnou hranicou)
	 * @param vrchnáHranica požadovaná vrchná hranica radu (ak je menšia od
	 *     spodnej hranice, tak sa stáva spodnou hranicou)
	 */
	public Rad(int spodnáHranica, int vrchnáHranica)
	{
		if (spodnáHranica == vrchnáHranica)
			++vrchnáHranica;

		if (spodnáHranica > vrchnáHranica)
		{
			this.spodnáHranica = vrchnáHranica;
			this.vrchnáHranica = spodnáHranica;
			vzostupne = false;
		}
		else
		{
			this.spodnáHranica = spodnáHranica;
			this.vrchnáHranica = vrchnáHranica;
		}

		hodnota = this.spodnáHranica;
	}

	/**
	 * <p>Konštruktor nastavujúci generátoru radu predvolené hranice a krok.
	 * Ak sú zadané rovnaké hodnoty hraníc, horná hranica je zväčšená
	 * o absolútnu hodnotu kroku, pričom ak je krok záporný, tak je nastavený
	 * zostupný predvolený smer zmeny hodnôt (čo je vzhľadom na rozdiel hraníc
	 * rovný kroku irelevantné, ale môže to mať vplyv pri včasnej zmene
	 * parametrov inštancie inými metódami).</p>
	 * 
	 * <p>V opačnom prípade (v prípade rozdielnych hodnôt hraníc) môže byť,
	 * naopak, hodnota kroku prispôsobená rozdielu hraníc (pozri aj metódu
	 * {@link #krok(int) krok}). Postup je v tomto prípade taký, že najprv sa
	 * porovnajú hodnoty hraníc. Ak je spodná hranica väčšia, než vrchná, tak
	 * sú hranice vymenené a predvolene je nastavený zostupný smer zmeny
	 * hodnôt (ktorý môže byť ešte raz zvrátený zápornou hodnotou kroku).
	 * Potom je nastavená hodnota kroku presne podľa pravidiel stanovených
	 * metódou {@link #krok(int) krok}.
	 * 
	 * <p>Vo všetkých prípadoch je nakoniec nastavená počiatočná hodnota
	 * generátora a to na výslednú hodnotu spodnej hranice.</p>
	 * 
	 * @param spodnáHranica požadovaná spodná hranica radu (ak je väčšia od
	 *     vrchnej hranice, tak sa stáva vrchnou hranicou)
	 * @param vrchnáHranica požadovaná vrchná hranica radu (ak je menšia od
	 *     spodnej hranice, tak sa stáva spodnou hranicou)
	 * @param krok požadovaný krok radu (miera prírastku resp. úbytku hodnôt;
	 *     môže byť upravený podľa hodnôt hraníc – pozri aj metódu
	 *     {@link #krok(int) krok})
	 */
	public Rad(int spodnáHranica, int vrchnáHranica, int krok)
	{
		if (spodnáHranica == vrchnáHranica)
		{
			if (0 == krok) this.krok = 1;
			else if (krok < 0)
			{
				vzostupne = !vzostupne;
				this.krok = -krok;
			}
			else
				this.krok = krok;

			this.spodnáHranica = spodnáHranica;
			this.vrchnáHranica = vrchnáHranica + this.krok;
		}
		else
		{
			if (spodnáHranica > vrchnáHranica)
			{
				this.spodnáHranica = vrchnáHranica;
				this.vrchnáHranica = spodnáHranica;
				vzostupne = false;
			}
			else
			{
				this.spodnáHranica = spodnáHranica;
				this.vrchnáHranica = vrchnáHranica;
			}

			krok(krok);
		}

		hodnota = this.spodnáHranica;
	}


	/**
	 * <p>Vráti aktuálnu hodnotu spodnej hranice radu.</p>
	 * 
	 * <p>Táto metóda je alternatívou metódy {@link #dolnáHranica()}.
	 * Ich implementácia je nezávislá (pri prípadnom prekrytí jednej metódy
	 * zostáva funkčnosť druhej nezmenená).</p>
	 * 
	 * @return aktuálna spodná hranica radu
	 */
	public int spodnáHranica()
	{ return spodnáHranica; }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica() spodnáHranica}.</p> */
	public int spodnaHranica() { return spodnáHranica; }

	/**
	 * <p>Upraví spodnú hranicu tohto generátora pri zachovaní najmenej
	 * jednotkového rozdielu medzi hranicami a zabezpečení hodnoty spodnej
	 * hranice nižšej od vrchnej hranice. To znamená, že ak je zadaná spodná
	 * hranica rovná vrchnej, tak je vrchná hranica zvýšená o jedna a ak je
	 * zadaná spodná hranica vyššia od vrchnej, tak sú ich hodnoty vymenené.
	 * Metóda zároveň zabezpečí, aby aktuálna hodnota kroku nebola väčšia,
	 * než je výsledný rozdiel hraníc.</p>
	 * 
	 * @param spodnáHranica požadovaná spodná hranica radu
	 */
	public void spodnáHranica(int spodnáHranica)
	{
		if (spodnáHranica == vrchnáHranica)
		{
			this.spodnáHranica = spodnáHranica;
			++vrchnáHranica;
		}
		else if (spodnáHranica > vrchnáHranica)
		{
			this.spodnáHranica = vrchnáHranica;
			vrchnáHranica = spodnáHranica;
		}
		else
		{
			this.spodnáHranica = spodnáHranica;
		}

		// if (this.spodnáHranica + krok > vrchnáHranica)
		// 	vrchnáHranica = this.spodnáHranica + krok;
		{
			int rozdiel = this.vrchnáHranica - this.spodnáHranica;
			if (this.krok > rozdiel) this.krok = rozdiel;
		}

		napravHodnotu();
	}

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica(int) spodnáHranica}.</p> */
	public void spodnaHranica(int dolnáHranica)
	{ spodnáHranica(dolnáHranica); }


	/**
	 * <p>Vráti aktuálnu hodnotu vrchnej hranice radu.</p>
	 * 
	 * <p>Táto metóda je alternatívou metódy {@link #hornáHranica()}.
	 * Ich implementácia je nezávislá (pri prípadnom prekrytí jednej metódy
	 * zostáva funkčnosť druhej nezmenená).</p>
	 * 
	 * @return aktuálna vrchná hranica radu
	 */
	public int vrchnáHranica()
	{ return vrchnáHranica; }

	/** <p><a class="alias"></a> Alias pre {@link #vrchnáHranica() vrchnáHranica}.</p> */
	public int vrchnaHranica() { return vrchnáHranica; }

	/**
	 * <p>Upraví vrchnú hranicu tohto generátora pri zachovaní najmenej
	 * jednotkového rozdielu medzi hranicami a zabezpečení hodnoty spodnej
	 * hranice nižšej od vrchnej hranice. To znamená, že ak je zadaná vrchná
	 * hranica rovná spodnej, tak je spodná hranica znížená o jedna a ak je
	 * zadaná vrchná hranica nižšia od spodnej, tak sú ich hodnoty vymenené.
	 * Metóda zároveň zabezpečí, aby aktuálna hodnota kroku nebola väčšia,
	 * než je výsledný rozdiel hraníc.</p>
	 * 
	 * @param vrchnáHranica požadovaná vrchná hranica radu
	 */
	public void vrchnáHranica(int vrchnáHranica)
	{
		if (vrchnáHranica == spodnáHranica)
		{
			this.vrchnáHranica = vrchnáHranica;
			--spodnáHranica;
		}
		else if (vrchnáHranica < spodnáHranica)
		{
			this.vrchnáHranica = spodnáHranica;
			spodnáHranica = vrchnáHranica;
		}
		else
		{
			this.vrchnáHranica = vrchnáHranica;
		}

		// if (spodnáHranica > this.vrchnáHranica - krok)
		// 	spodnáHranica = this.vrchnáHranica - krok;
		{
			int rozdiel = this.vrchnáHranica - this.spodnáHranica;
			if (this.krok > rozdiel) this.krok = rozdiel;
		}

		napravHodnotu();
	}

	/** <p><a class="alias"></a> Alias pre {@link #vrchnáHranica(int) vrchnáHranica}.</p> */
	public void vrchnaHranica(int hornáHranica)
	{ vrchnáHranica(hornáHranica); }


	/**
	 * <p>Vráti aktuálny krok radu (mieru prírastku resp. úbytku hodnôt).</p>
	 * 
	 * @return aktuálny krok radu (miera prírastku resp. úbytku hodnôt)
	 */
	public int krok() { return krok; }

	/**
	 * <p>Nastaví novú hodnotu kroku radu (čiže mieru prírastku resp. úbytku
	 * hodnôt radu počas ich aktívneho čítania).</p>
	 * 
	 * <p>Veľkosť kroku nesmie byť nulová (vo vzorcoch na výpočet hodnôt
	 * radu podľa parametra by vznikalo delenie nulou). Ak je zadaná nulová
	 * hodnota kroku, tak bude krok nastavený na jednotku.</p>
	 * 
	 * <p>Hodnota kroku nesmie byť záporná (vzorce na výpočet hodnôt radu
	 * podľa parametra boli zostavené tak, že predpokladajú len kladné
	 * hodnoty kroku; pri záporných hodnotách kroku by poskytovali úplne
	 * nesprávne výsledky). Ak je zadaná záporná hodnota kroku, tak
	 * inštancia vnútorne prepne aktuálny smer zmeny hodnôt radu (zo
	 * vzostupného na zostupný alebo naopak) a zmení znamienko kroku zo
	 * záporného na kladné.</p>
	 * 
	 * <p>Veľkosť kroku (v absolútnej hodnote) nesmie presiahnuť rozdiel
	 * hraníc radu (vrchná hranica mínus spodná hranica). Ak je zadaná
	 * hodnota s väčším rozpätím, tak je orezaná presne na hodnotu rozdielu
	 * hraníc radu.</p>
	 * 
	 * @param krok nový krok (miera prírastku resp. úbytku hodnôt) radu
	 */
	public void krok(int krok)
	{
		if (0 == krok) this.krok = 1;
		else
		{
			if (krok < 0)
			{
				vzostupne = !vzostupne;
				krok = -krok;
			}

			int rozdiel = vrchnáHranica - spodnáHranica;
			if (krok > rozdiel) this.krok = rozdiel;
			else this.krok = krok;
		}

		napravHodnotu();
	}


	/**
	 * <p>Overí, či je aktuálny smer zmeny hodnôt radu vzostupný
	 * (stúpajúci).</p>
	 * 
	 * <p>Táto metóda je opakom metódy {@link #zostupne()}.</p>
	 * 
	 * @return ak je aktuálny smer zmeny hodnôt radu vzostupný (stúpajúci),
	 *     tak vráti hodnotu {@code valtrue}, inak {@code valfalse}
	 */
	public boolean vzostupne() { return vzostupne; }

	/**
	 * <p>Nastaví smer zmeny hodnôt radu podľa zadaného parametera. Ak má byť
	 * aktuálny smer vzostupný (stúpajúci), tak treba zadať hodnotu parametra
	 * {@code valtrue}, v opačnom prípade {@code valfalse}.</p>
	 * 
	 * <p>Ak je aktuálna hodnota radu najväčšia povolená, tak sa smer pri
	 * najbližšom aktívnom čítaní hodnoty i tak zmení na zostupný</p>
	 * 
	 * <p>Táto metóda je opakom metódy {@link #zostupne(boolean zostupne)}.</p>
	 * 
	 * @param vzostupne ak má byť aktuálny smer zmeny hodnôt radu
	 *     vzostupný (stúpajúci), tak treba zadať hodnotu {@code valtrue},
	 *     v opačnom prípade {@code valfalse}
	 */
	public void vzostupne(boolean vzostupne)
	{ this.vzostupne = vzostupne; }


	// ——— Alternatívy ———


	/**
	 * <p>Vráti aktuálnu hodnotu spodnej hranice radu.</p>
	 * 
	 * <p>Táto metóda je alternatívou metódy {@link #spodnáHranica()}.
	 * Ich implementácia je nezávislá (pri prípadnom prekrytí jednej metódy
	 * zostáva funkčnosť druhej nezmenená).</p>
	 * 
	 * @return aktuálna spodná hranica radu
	 */
	public int dolnáHranica() { return spodnáHranica; }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica() spodnáHranica}.</p> */
	public int dolnaHranica() { return spodnáHranica; }

	/**
	 * <p>Upraví spodnú hranicu tohto generátora pri zachovaní najmenej
	 * jednotkového rozdielu medzi hranicami a zabezpečení hodnoty spodnej
	 * hranice nižšej od vrchnej hranice.</p>
	 * 
	 * <p>Táto metóda je aliasom metódy {@link #spodnáHranica(int
	 * dolnáHranica)},
	 * to znamená, že táto metóda vnútorne volá uvedenú metódu (preto
	 * funguje rovnako ako ona).</p>
	 * 
	 * @param dolnáHranica požadovaná spodná hranica radu
	 */
	public void dolnáHranica(int dolnáHranica)
	{ spodnáHranica(dolnáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #spodnáHranica(int) spodnáHranica}.</p> */
	public void dolnaHranica(int dolnáHranica)
	{ spodnáHranica(dolnáHranica); }


	/**
	 * <p>Vráti aktuálnu hodnotu vrchnej hranice radu.</p>
	 * 
	 * <p>Táto metóda je alternatívou metódy {@link #vrchnáHranica()}.
	 * Ich implementácia je nezávislá (pri prípadnom prekrytí jednej metódy
	 * zostáva funkčnosť druhej nezmenená).</p>
	 * 
	 * @return aktuálna vrchná hranica radu
	 */
	public int hornáHranica() { return vrchnáHranica; }

	/** <p><a class="alias"></a> Alias pre {@link #vrchnáHranica() vrchnáHranica}.</p> */
	public int hornaHranica() { return vrchnáHranica; }

	/**
	 * <p>Upraví vrchnú hranicu tohto generátora pri zachovaní najmenej
	 * jednotkového rozdielu medzi hranicami a zabezpečení hodnoty spodnej
	 * hranice nižšej od vrchnej hranice.</p>
	 * 
	 * <p>Táto metóda je aliasom k metóde {@link #vrchnáHranica(int
	 * vrchnáHranica)}, to znamená, že táto metóda vnútorne volá uvedenú
	 * metódu (preto funguje rovnako ako ona).</p>
	 * 
	 * @param hornáHranica požadovaná vrchná hranica radu
	 */
	public void hornáHranica(int hornáHranica)
	{ vrchnáHranica(hornáHranica); }

	/** <p><a class="alias"></a> Alias pre {@link #vrchnáHranica(int) vrchnáHranica}.</p> */
	public void hornaHranica(int hornáHranica)
	{ vrchnáHranica(hornáHranica); }


	/**
	 * <p>Overí, či je aktuálny smer zmeny hodnôt radu zostupný
	 * (klesajúci).</p>
	 * 
	 * <p>Táto metóda je opakom metódy {@link #vzostupne()}.</p>
	 * 
	 * @return ak je aktuálny smer zmeny hodnôt radu zostupný (klesajúci),
	 *     tak vráti hodnotu {@code valtrue}, inak {@code valfalse}
	 */
	public boolean zostupne() { return !vzostupne; }

	/**
	 * <p>Nastaví smer zmeny hodnôt radu podľa zadaného parametera. Ak má byť
	 * aktuálny smer zostupný (klesajúci), tak treba zadať hodnotu parametra
	 * {@code valtrue}, v opačnom prípade {@code valfalse}.</p>
	 * 
	 * <p>Ak je aktuálna hodnota radu najnižšia povolená, tak sa smer pri
	 * najbližšom aktívnom čítaní hodnoty i tak zmení na vzostupný.</p>
	 * 
	 * <p>Táto metóda je opakom metódy {@link #vzostupne(boolean
	 * vzostupne)}.</p>
	 * 
	 * @param zostupne ak má byť aktuálny smer zmeny hodnôt radu
	 *     zostupný (klesajúci), tak treba zadať hodnotu {@code valtrue},
	 *     v opačnom prípade {@code valfalse}
	 */
	public void zostupne(boolean zostupne)
	{ this.vzostupne = !zostupne; }

	/**
	 * <p>Prevráti smer zmeny hodnôt radu na opačný k aktuálnemu. Ak je
	 * aktuálna hodnota radu najnižšia povolená a smer sa zmení na zostupný
	 * alebo naopak, ak je aktuálna hodnota radu najväčšia povolená a smer
	 * sa zmení na vzostupný, tak sa pri najbližšom aktívnom čítaní hodnoty
	 * smer spätne prevráti.</p>
	 */
	public void naopak() { vzostupne = !vzostupne; }


	// ——— Čítanie hodnôt radu ———


	/**
	 * <p>Aktívne čítanie hodnôt radu. Pri aktívnom čítaní sa vnútorná
	 * (aktuálna) hodnota radu po prečítaní vždy posunie na nasledujúcu podľa
	 * konfigurácie radu. Ak je pri vzostupnom smere zmien hodnôt prekročená
	 * horná hranica, tak je hodnota upravená späť do rozmedzia hraníc a smer
	 * zmien hodnôt je prevrátený. Podobne to je pri klesajúcom smere
	 * a prekročení spodnej hranice.</p>
	 * 
	 * @return aktuálna hodnota radu s posunom na nasledujúcu (čiže každé
	 *     ďalšie volanie tejto metódy vráti ďalšiu hodnotu radu)
	 */
	public int daj()
	{
		int pôvodná = hodnota;

		if (vzostupne)
		{
			hodnota += krok;
			if (hodnota > vrchnáHranica)
			{
				vzostupne = false;
				hodnota -= 2 * krok;
			}
		}
		else
		{
			hodnota -= krok;
			if (hodnota < spodnáHranica)
			{
				vzostupne = true;
				hodnota += 2 * krok;
			}
		}

		return pôvodná;
	}

	/**
	 * <p>Pasívne prečítanie aktuálnej hodnoty radu. Pri pasívnom čítaní
	 * sa okrem vrátenia hodnoty nedejú žiadne iné akcie. Pozri meódu
	 * {@link #daj() daj}, ktorá vykonáva aktívne čítanie hodnôt radu.</p>
	 * 
	 * @return aktuálna hodnota radu
	 */
	public int hodnota()
	{
		return hodnota;
	}

	/**
	 * <p>Nastavenie novej aktuálnej hodnoty tejto inštancie. Požadovaná
	 * hodnota musí vyhovovať konfigurácii radu, inak bude upravená.
	 * Konkrétne: ak je zadaná hodnota nižšia od spodnej hranice, tak je
	 * výsledná hodnota nastavená na spodnú hranicu, ak je vyššia od hornej,
	 * tak je nastavená na najbližší vyhovujúci násobok kroku, pričom to
	 * isté platí aj v prípade, že požadovaná hodnota nie je násobkom
	 * kroku so začiatkom v spodnej hranici radu.</p>
	 * 
	 * @param hodnota požadovaná hodnota tohto radu (ak nevyhovie
	 *     konfiugrácii tejto inštancie, tak bude upravená)
	 */
	public void hodnota(int hodnota)
	{
		this.hodnota = hodnota;
		napravHodnotu();
	}

	/**
	 * <p>Nastavenie a prečítanie novej aktuálnej hodnoty tejto inštancie
	 * výpočtom z parametra. Parameter sa dá chápať ako „poradové číslo“
	 * (resp. index) hodnoty radu vygenerovanej podľa konfigurácie tejto
	 * inštancie. Parameter môže byť ľubovoľné celé číslo. Príklad: Hodnota
	 * radu s parametrom 0 je rovná spodnej hranici radu, parameter
	 * 1 vygeneruje ďalšiu hodnotu radu (to jest hodnotu zvýšenú o krok),
	 * ďalšie parametre v poradí vygenerujú presne také hodnoty
	 * trojuholníkovej postupnosti, aké by boli vygenerované pri sekvenčnom
	 * volaní metódy na aktívne čítanie hodnôt radu – {@link #daj() daj}.</p>
	 * 
	 * <p>Na zistenie hodnoty radu podľa parametra bez jej nastavenia ako
	 * aktuálnej novej hodnoty slúži metóda {@link #dajHodnotu(int)
	 * dajHodnotu}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Metóda na čítanie hodnoty
	 * parametra nie je definovaná, pretože výpočet hodnoty parametra má
	 * nekonečne veľa riešení.</p>
	 * 
	 * @param parameter hodnota parametra, podľa ktorej bude nastavená
	 *     (a vrátená) aktuálna hodnota radu
	 * @return aktuálna hodnota radu nastavená podľa zadaného parametra
	 */
	public int parameter(int parameter)
	{
		return hodnota = dajHodnotu(parameter);
	}

	/**
	 * <p>Výpočet hodnoty radu podľa zadaného parametra, ktorý sa dá chápať
	 * aj ako „poradové číslo“ (alebo skôr index) hodnoty radu. Viac detailov
	 * nájdete v opise metódy {@link #parameter(int) parameter}.</p>
	 * 
	 * <p>Na súčasné zistenie hodnoty radu podľa parametra a jej nastavenie
	 * ako novej aktuálnej hodnoty slúži metóda {@link #parameter(int)
	 * parameter}.</p>
	 * 
	 * @param parameter hodnota parametra, z ktorej bude vypočítaná a vrátená
	 *     hodnota radu (podľa jeho nastavení; aktuále vlastnosti radu,
	 *     hodnota, smer a podobne, zostanú nezmenené)
	 * @return hodnota radu vypočítaná zo zadaného parametra
	 */
	public int dajHodnotu(int parameter)
	{
		int rozdielHraníc = vrchnáHranica - spodnáHranica;
		int deliteľ = ((2 * rozdielHraníc) - (rozdielHraníc % krok)) / krok;
		int odčítanec = deliteľ / 2;

		return spodnáHranica + (krok * (odčítanec - Math.abs(
			(parameter % deliteľ) - odčítanec)));
	}

	/**
	 * <p>Zamieša tento rad. Metóda nastaví aktuálnu hodnotu radu na náhodné
	 * číslo v rámci jeho hraníc, s ohľadom na hodnotu kroku a náhodne zmení
	 * aktuálny smer radu.</p>
	 */
	public void zamiešaj()
	{
		hodnota = (int)Svet.náhodnéCeléČíslo(vrchnáHranica, spodnáHranica);
		napravHodnotu();
		vzostupne = 0 == Svet.náhodnéCeléČíslo(0, 1);
	}

	/** <p><a class="alias"></a> Alias pre {@link #zamiešaj() zamiešaj}.</p> */
	public void zamiesaj() { zamiešaj(); }
}
