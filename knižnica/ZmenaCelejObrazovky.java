
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

import java.awt.GraphicsDevice;
import java.awt.Window;

import java.util.TreeMap;

import javax.swing.JFrame;

/**
 * <p>Toto rozhranie slúži na obsluhu zmeny režimu celej obrazovky. Rámec
 * definuje dva predvolené spôsoby zmeny ({@link #hardvérová hardvérová}
 * a {@link #softvérová softvérová}) a vďaka tomuto rozhraniu je možné
 * definovať ďalšie vlastné spôsoby zmeny režimu celej obrazovky. Rozhranie
 * umožňuje spresniť komunikáciu medzi aplikáciou a programovacím rámcom
 * počas prechodu do aj z režimu celej obrazovky a tým tento proces
 * dokonalejšie riadiť zo strany aplikácie. Metóda {@linkplain Svet sveta}
 * {@link Svet#celáObrazovka(int, boolean) celáObrazovka}{@code (}{@code 
 * zariadenie, celáObrazovka}{@code )} vždy pracuje s niektorou verziou
 * implementácie tohto rozhrania. Predvolene je to inštancia {@code 
 * currZmenaCelejObrazovky}{@code .}{@link #hardvérová hardvérová}, ktorú je
 * možné zmeniť úpravou hodnoty atribútu {@link Svet Svet}{@code .}{@link 
 * Svet#zmenaCelejObrazovky zmenaCelejObrazovky} (pričom ak je hodnota tohto
 * atribútu {@code valnull}, tak je obsluha presmerovaná späť do inštancie
 * {@link #hardvérová hardvérová}).</p>
 */
public interface ZmenaCelejObrazovky
{
	/**
	 * <p>Toto je predvolený hardverový spôsob zmeny režimu celej obrazovky.
	 * Tento spôsob priamo využíva možnosti zariadenia pre ktoré sa pokúsi
	 * získať výhradný režim prístupu. Zmena režimu je závislá od
	 * konkrétnej platformy a hardvéru.</p>
	 */
	public final static ZmenaCelejObrazovky hardvérová =
		new ZmenaCelejObrazovky()
		{
			public boolean jePodpora(int indexZariadenia,
				GraphicsDevice zariadenie)
			{
				return zariadenie.isFullScreenSupported();
			}

			public Window dajOkno(int indexZariadenia,
				GraphicsDevice zariadenie)
			{
				return zariadenie.getFullScreenWindow();
			}

			public void zmena(int indexZariadenia, GraphicsDevice zariadenie,
				boolean prejsťDoRežimu, JFrame oknoCelejObrazovky)
			{
				if (prejsťDoRežimu)
					zariadenie.setFullScreenWindow(oknoCelejObrazovky);
				else
					zariadenie.setFullScreenWindow(null);
			}

			public boolean ponechajOkno() { return false; }
		};

	/** <p><a class="alias"></a> Alias pre {@link #hardvérová hardvérová}.</p> */
	public final static ZmenaCelejObrazovky hardverova = hardvérová;

	/**
	 * <p>Toto je predvolený softvérový spôsob zmeny režimu celej obrazovky.
	 * Tento spôsob iba zistí rozmery cieľového zariadenia a upraví podľa
	 * nich rozmery okna celej obrazovky.</p>
	 */
	public final static ZmenaCelejObrazovky softvérová =
		new ZmenaCelejObrazovky()
		{
			private final TreeMap<Integer, Window> oknáRežimov =
				new TreeMap<>();

			public boolean jePodpora(int indexZariadenia,
				GraphicsDevice zariadenie)
			{
				return true;
			}

			public Window dajOkno(int indexZariadenia,
				GraphicsDevice zariadenie)
			{
				return oknáRežimov.get(indexZariadenia);
			}

			public void zmena(int indexZariadenia, GraphicsDevice zariadenie,
				boolean prejsťDoRežimu, JFrame oknoCelejObrazovky)
			{
				if (prejsťDoRežimu)
				{
					oknáRežimov.put(indexZariadenia, oknoCelejObrazovky);
					oknoCelejObrazovky.setBounds(zariadenie.
						getDefaultConfiguration().getBounds());
				}
				else
					oknáRežimov.remove(indexZariadenia);
			}

			public boolean ponechajOkno() { return false; }
		};

	/** <p><a class="alias"></a> Alias pre {@link #softvérová softvérová}.</p> */
	public final static ZmenaCelejObrazovky softverova = softvérová;

	/**
	 * <p>Táto metóda má programovacom rámci poskytnúť informáciu o tom, či
	 * je pre zadané zariadenie dostupná podpora režimu celej obrazovky. Na
	 * základe návratovej hodnoty bude programovací rámec vedieť, či má alebo
	 * nemá zmysel skúšať vykonať akciu súvisiacu so zmenou režimu celej
	 * obrazovky. Ak je návratová hodnota {@code valfalse}, programovací
	 * rámec nevykoná požadovanú akciu.</p>
	 * 
	 * @param indexZariadenia index zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @param zariadenie inštancia zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @return hodnota {@code valtrue} znamená, že režim celej obrazovky
	 *     je pre toto zariadenie podporovaný
	 */
	public boolean jePodpora(int indexZariadenia, GraphicsDevice zariadenie);

	/**
	 * <p>Táto metóda má poskytnúť programovacom rámci aktívne okno režimu
	 * celej obrazovky. Ak je zariadenie v režime celej obrazovky, tak metóda
	 * musí vrátiť správne okno, aby programovací rámec bol schopný
	 * indentifikovať, či on sám bol zdrojom prechodu zariadenia do režimu
	 * celej obrazovky. Ak vrátené okno nie je to, ktoré programovací rámec
	 * režimu poskytol, tak programovací rámec nevykoná akciu prechodu späť
	 * z režimu celej obrazovky.</p>
	 * 
	 * @param indexZariadenia index zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @param zariadenie inštancia zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @return inštancia okna použitého v režime celej obrazovky
	 */
	public Window dajOkno(int indexZariadenia, GraphicsDevice zariadenie);

	/**
	 * <p>Táto metóda je zodpovedná za vykonanie prechodu do režimu celej
	 * obrazovky alebo späť. Metóda dostane od programovacieho rámca všetky
	 * potrebné informácie – či bola žiadaná zmena na režim celej obrazovky
	 * alebo naspäť, ktoré zariadenie má byť použité a ktoré okno bude použité
	 * pre tento režim.</p>
	 * 
	 * @param indexZariadenia index zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @param zariadenie inštancia zariadenia, pre ktoré má byť vyžiadaný
	 *     režim celej obrazovky
	 * @param prejsťDoRežimu hodnota {@code valtrue} znamená žiadanie
	 *     prechodu do režimu celej obrazovky a naopak, hodnota
	 *     {@code valflase} znamená prechod späť do „normálneho“ režimu
	 *     (režimu okien používateľského rozhrania operačného systému)
	 * @param oknoCelejObrazovky inštancia okna, ktoré bude použité v režime
	 *     celej obrazovky
	 */
	public void zmena(int indexZariadenia, GraphicsDevice zariadenie,
		boolean prejsťDoRežimu, JFrame oknoCelejObrazovky);

	/**
	 * <p>Táto metóda umožňuje spresniť to, či pri prechode do režimu celej
	 * obrazovky má programovací rámec vyrobiť nové samostatné okno (čo je
	 * predvolený spôsob správania) alebo sa použije (ponechá) jestvujúce
	 * okno sveta (čo môže byť vyžadované pri softvérovej zmene režimu na
	 * niektorých operačných systémoch – ako je macOS).</p>
	 * 
	 * @return ak má byť pri prechode do režimu celej obrazovky vyrobené nové
	 *     samostatné okno, tak musí byť návratová tejto metódy
	 *     {@code valfalse}
	 */
	public boolean ponechajOkno();
}
