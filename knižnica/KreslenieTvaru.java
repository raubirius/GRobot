
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

package knižnica;

/**
 * <p>Rozhranie používané na {@linkplain  GRobot#vlastnýTvar(KreslenieTvaru)
 * kreslenie (vlastných) tvarov robota}, {@linkplain 
 * Spojnica#definujZnačkuZačiatku(KreslenieTvaru) začiatočných} a {@linkplain 
 * Spojnica#definujZnačkuKonca(KreslenieTvaru) koncových} značiek spojníc
 * robotov, definíciu kreslenia a export prvkov {@linkplain Roj roja} do
 * formátu SVG, prípadne na ďalšie zákaznícky definované účely.</p>
 * 
 * <p>Implementácia tohto rozhrania je vstupom prislúchajúcich metód,
 * napríklad metódy definovania vlastného tvaru robota: {@link 
 * GRobot#vlastnýTvar(KreslenieTvaru) GRobot.vlastnýTvar(tvar)}; metód
 * na definovanie tvarov značiek spojníc: {@link 
 * Spojnica#definujZnačkuZačiatku(KreslenieTvaru)
 * Spojnica.definujZnačkuZačiatku(kreslenie)},
 * {@link Spojnica#definujZnačkuKonca(KreslenieTvaru)
 * Spojnica.definujZnačkuKonca(kreslenie)} a podobne.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} knižnica.*;

	{@code kwdpublic} {@code typeclass} GKruh {@code kwdextends} {@link GRobot GRobot}
	{
		{@code comm// Statická inštancia kreslenia vlastného tvaru – statickosť znamená, že}
		{@code comm// bude vytvorený jediný objekt pre všetky inštancie triedy GKruh (pozri}
		{@code comm// hlavnú metódu nižšie).}
		{@code kwdpublic} {@code kwdstatic} {@link KreslenieTvaru KreslenieTvaru} tvarKruhu = {@code kwdnew} {@link KreslenieTvaru KreslenieTvaru}()
		{
			{@code kwdpublic} {@code typevoid} {@link KreslenieTvaru#kresli(GRobot) kresli}({@link GRobot GRobot} r)
			{
				{@code comm// Vlastné kreslenie:}
				r.{@link GRobot#kruh() kruh}();
			}
		};

		{@code comm// Konštruktor}
		{@code kwdprivate} GKruh()
		{
			{@code comm// Všetky objekty budú mať spoločné kreslenie vlastného tvaru, ktoré}
			{@code comm// však bude prispôsobované podľa individuálnych vlastností robotov:}
			{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(tvarKruhu);
		}

		{@code comm// Hlavná metóda}
		{@code kwdpublic} {@code kwdstatic} {@code typevoid} main({@link String String}[] args)
		{
			{@code comm// Vytvoríme tri objekty typu GKruh:}
			GKruh gKruh1 = {@code kwdnew} GKruh();
			GKruh gKruh2 = {@code kwdnew} GKruh();
			GKruh gKruh3 = {@code kwdnew} GKruh();

			{@code comm// Každý umiestnime na náhodnú pozíciu:}
			gKruh1.{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
			gKruh2.{@link GRobot#náhodnáPoloha() náhodnáPoloha}();
			gKruh3.{@link GRobot#náhodnáPoloha() náhodnáPoloha}();

			{@code comm// Zmeníme mu náhodú farbu:}
			gKruh1.{@link GRobot#náhodnáFarba() náhodnáFarba}();
			gKruh2.{@link GRobot#náhodnáFarba() náhodnáFarba}();
			gKruh3.{@link GRobot#náhodnáFarba() náhodnáFarba}();

			{@code comm// A určíme mu náhodnú veľkosť v rozmedzí 10}
			{@code comm// (čo je aktuálna veľkosť pri jednotkovej mierke) až 30}
			{@code comm// (čo je trojnásobok tejto hodnoty):}
			gKruh1.{@link GRobot#náhodnáVeľkosť(double) náhodnáVeľkosť}({@code num3}));
			gKruh2.{@link GRobot#náhodnáVeľkosť(double) náhodnáVeľkosť}({@code num3}));
			gKruh3.{@link GRobot#náhodnáVeľkosť(double) náhodnáVeľkosť}({@code num3}));
		}
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>ukazkaVlastnehoTvaru.png<alt/>Výsledok príkladu
 * využívajúceho kreslenie vlastného tvaru robota.</image>Ukážka
 * možného vzhľadu po vykonaní príkladu<br /><small>(plátno na
 * obrázku je zmenšené)</small>.</p>
 * 
 * <p> </p>
 * 
 * <p>Použitie rozhrania môže vyzerať komplikovane, preto bolo
 * robotu umožnené zmeniť tvar aj (z hľadiska začínajúceho
 * programátora) jednoduchším spôsobom. Metódou {@link GRobot
 * GRobot}{@code .}{@link GRobot#kresliTvar() kresliTvar}{@code ()}
 * (myslíme tým jej prekrytím). Z vnútorného (technického)
 * hľadiska je však činnosť spomínanej metódy zabezpečovaná
 * komplikovanejším mechanizmom s mierne vyššími režijnými
 * nákladmi na vykonávanie, preto skúsenejším programátorom
 * odporúčame využitie tohto rozhrania.</p>
 * 
 * <p>Táto verzia programovacieho rámca je závislá od použitia
 * verzie Javy 8. V tejto verzii Javy sa dá použiť aj skrátená
 * (funkcionálna) syntax kreslenia vlastného tvaru s použitím
 * tohto rozhrania. Ide o takzvaný lambda výraz (priamo vo volaní
 * metódy robota {@link GRobot#vlastnýTvar(KreslenieTvaru)
 * vlastnýTvar}, ako to ukazuje nasledujúci príklad, ho odporúčame
 * použiť iba v prípade unikátneho kreslenia tvaru jedinej
 * inštancie robota, keďže pri každom takomto použití by bola
 * vytvorená unikátna inštancia implementácie rozhrania, čím by
 * sa strácal celkový zmysel jeho použitia):</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(({@link GRobot GRobot} r) -> r.{@link GRobot#krúžok() krúžok}());
	</pre>
 * 
 * <p>Prípadne, ak „jednoducho vieme,“ že {@code r} je robot
 * a nepotrebujeme to dávať najavo nikomu tretiemu, tak môžeme
 * zápis ešte viac skrátiť:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(r -> r.{@link GRobot#krúžok() krúžok}());
	</pre>
 * 
 * <p>Viac príkazov kreslenia je potrebné uzavrieť do bloku:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(r ->
		{
			r.{@link GRobot#kružnica() kružnica}();
			r.{@link GRobot#dopredu() dopredu}();
			{@code comm// …}
		});
	</pre>
 * 
 * <p>A nakoniec, lambda výraz je použiteľný aj priamo na
 * univerzálnu implementáciu rozhrania použiteľnú s viacerými
 * inštanciami robota:</p>
 * 
 * <pre CLASS="example">
	{@link GRobot GRobot} prvý = {@code kwdnew} {@link GRobot#GRobot() GRobot}();
	{@link GRobot GRobot} druhý = {@code kwdnew} {@link GRobot#GRobot() GRobot}();
	{@link GRobot GRobot} tretí = {@code kwdnew} {@link GRobot#GRobot() GRobot}();
	{@code comm// …}

	{@code currKreslenieTvaru} tvar = r ->
	{
		r.{@link GRobot#kružnica() kružnica}();
		r.{@link GRobot#dopredu() dopredu}();
		{@code comm// …}
	};

	prvý.{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(tvar);
	druhý.{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(tvar);
	tretí.{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}(tvar);
	{@code comm// …}
	</pre>
 * 
 * @see GRobot#vlastnýTvar(KreslenieTvaru)
 */
public interface KreslenieTvaru
{
	/**
	 * <p>Jediná metóda rozhrania určená na vykonanie určenej činnosti
	 * (napríklad kreslenie vlastného tvaru robota, zabezpečenie exportu
	 * prvkov do externého formátu a podobne). Prijíma parameter typu
	 * {@link GRobot GRobot}, teda objekt (inštanciu) robota, ktorý má
	 * primárne slúžiť na vykonanie alebo podporu vykonania určenej
	 * činnosti – kreslenie vlastného tvaru, kreslenie značiek spojníc,
	 * kedy je automaticky použitý {@linkplain Spojnica#zdroj() zdrojový}
	 * alebo {@linkplain Spojnica#cieľ() cieľový} robot) a podobne.
	 * V prípade kreslenia vlastného tvaru dodržujte zásadu, že kreslenie
	 * má byť čo najjednoduchšie a malo by využívať len metódy kreslenia
	 * inštancie „r“ prijatej v argumente tejto metódy.</p>
	 * 
	 * <p><b>Príklad:</b></p>
	 * 
	 * <pre CLASS="example">
		{@link GRobot#vlastnýTvar(KreslenieTvaru) vlastnýTvar}({@code kwdnew} {@link KreslenieTvaru KreslenieTvaru}()
		{
			{@code kwdpublic} {@code typevoid} {@code currkresli}({@link GRobot GRobot} r)
			{
				{@code comm// Vždy modrá kružnica s „nosom“}
				r.{@link GRobot#farba(Color) farba}({@link Farebnosť#modrá modrá});
				r.{@link GRobot#kružnica(double) kružnica}();
				r.{@link GRobot#dopredu(double) dopredu}({@code num2} * r.{@link GRobot#veľkosť() veľkosť}());
			}
		});
		</pre>
	 * 
	 * <p><image>kruznica-s-nosom.svg<alt/>Výsledok ukážky kreslenia
	 * vlastného tvaru.<onerror>kruznica-s-nosom.png</onerror></image>Ukážka
	 * vzhľadu.</p>
	 * 
	 * <p class="remark"><b>Poznámky:</b> Niektoré metódy upravujú
	 * v rámci kreslenia vlastného tvaru svoje správanie. Napríklad
	 * skupina metód {@link GRobot#domov() domov} vráti a otočí robot
	 * do aktuálnej pozície a smeru – to jest do polohy a smeru v čase
	 * začatia vlastného kreslenia a to bez ohľadu na to, ako je
	 * v skutočnosti definovaný aktuálny domov robota. Metódy {@link 
	 * GRobot#uhol() uhol}, {@link GRobot#smer() smer}, {@link 
	 * GRobot#uholDoma() uholDoma} a {@link GRobot#smerDoma() smerDoma}
	 * vracajú hodnotu upravenú o aktuálne pootočenie tvaru (pozri metódu
	 * {@link GRobot#pootočenieTvaru(double) pootočenieTvaru}).<br />
	 *  <br />
	 * Dôležitou informáciou je tiež to, že hodnoty takmer všetkých
	 * (dostatočne elementárnych) vlastností ako poloha, smer, stav pera
	 * (a mnoho iných) sú pred začatím kreslenia vlastného tvaru robota
	 * zálohované a po dokončení tejto činnosti sú opätovne vrátené do
	 * pôvodného stavu. Pero robota je pri začatí kreslenia vlastného tvaru
	 * vždy {@linkplain GRobot#položPero() položené} bez ohľadu na jeho
	 * skutočný stav a tiež je (bez ohľadu na skutočný stav) zapnuté
	 * {@linkplain GRobot#kresliTvary() kreslenie tvarov} robotom.<br />
	 *  <br />
	 * Zálohovanie sa nevzťahuje na iné použitia tohto rozhrania (napríklad
	 * kreslenie roja kresliacim robotom). Každé iné použitie tohto rozhrania
	 * má svoje vlastné pravidlá.</p>
	 * 
	 * @param r inštancia robota použitý na vykonanie určenej činnosti
	 *     alebo jej podporu (nakreslenie vlastného tvaru a pod.);
	 *     v prípade kreslenia vlastného tvaru je to presne tá inštancia,
	 *     ktorá požaduje kreslenie vlastného tvaru
	 */
	public void kresli(GRobot r);
}
