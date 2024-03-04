
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
 * <p>Rozhranie slúži na implementáciu metód slúžiacich na zistenie
 * a nastavenie rozmeru (šírky a výšky) objektu. Implementujú ho viaceré
 * triedy programovacieho rámca.</p>
 */
public interface Rozmer
{
	/** <p>Prototyp metódy na zistenie šírky objektu.</p> */
	public double šírka();
	/** <p>Prototyp metódy na zistenie výšky objektu.</p> */
	public double výška();
	/** <p>Prototyp metódy na zistenie šírky objektu.</p> */
	public double sirka();
	/** <p>Prototyp metódy na zistenie výšky objektu.</p> */
	public double vyska();

	/** <p>Prototyp metódy na nastavenie šírky objektu.</p> */
	public void šírka(double šírka);
	/** <p>Prototyp metódy na nastavenie výšky objektu.</p> */
	public void výška(double výška);
	/** <p>Prototyp metódy na nastavenie šírky objektu.</p> */
	public void sirka(double šírka);
	/** <p>Prototyp metódy na nastavenie výšky objektu.</p> */
	public void vyska(double výška);


	/** <p>Prototyp metódy na zistenie obidvoch rozmerov objektu.</p> */
	public Rozmer rozmery();
	/** <p>Prototyp metódy na nastavenie obidvoch rozmerov objektu.</p> */
	public void rozmery(Rozmer rozmer);


	/**
	 * <p>Prototyp metódy na porovnanie zhody šírky so zadanou hodnotou.</p>
	 */
	public boolean máŠírku(double šírka);
	/**
	 * <p>Prototyp metódy na porovnanie zhody šírky so zadanou hodnotou.</p>
	 */
	public boolean maSirku(double šírka);

	/**
	 * <p>Prototyp metódy na porovnanie zhody výšky so zadanou hodnotou.</p>
	 */
	public boolean máVýšku(double výška);
	/**
	 * <p>Prototyp metódy na porovnanie zhody výšky so zadanou hodnotou.</p>
	 */
	public boolean maVysku(double výška);

	/**
	 * <p>Prototyp metódy na porovnanie zhody obidvoch zadaných rozmerov.</p>
	 */
	public boolean máRozmer(double šírka, double výška);
	/**
	 * <p>Prototyp metódy na porovnanie zhody obidvoch zadaných rozmerov.</p>
	 */
	public boolean maRozmer(double šírka, double výška);

	/** <p>Prototyp metódy na porovnanie zhody obidvoch rozmerov objektu.</p> */
	public boolean máRozmer(Rozmer rozmer);
	/** <p>Prototyp metódy na porovnanie zhody obidvoch rozmerov objektu.</p> */
	public boolean maRozmer(Rozmer rozmer);
}
