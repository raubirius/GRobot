
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
 * <p>Rozhranie slúži na implementáciu metódy slúžiacej na zistenie aktuálnej
 * úrovne priehľadnosti a na definíciu konštánt priehľadnosti.</p>
 */
public interface Priehľadnosť
{
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHĽADNÝ = 1.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHLADNY = 1.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHĽADNÁ = 1.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHLADNA = 1.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHĽADNÉ = 1.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne nepriehľadnému objektu.</p> */
	public final static float NEPRIEHLADNE = 1.0f;

	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITEĽNÝ = 0.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITELNY = 0.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITEĽNÁ = 0.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITELNA = 0.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITEĽNÉ = 0.0f;
	/** <p>Hodnota priehľadnosti zodpovedajúca úplne priehľadnému objektu.</p> */
	public final static float NEVIDITELNE = 0.0f;

	/** <p>Prototyp metódy na zistenie aktuálnej úrovne priehľadnosti objektu.</p> */
	public double priehľadnosť();
	/** <p>Prototyp metódy na zistenie aktuálnej úrovne priehľadnosti objektu.</p> */
	public double priehladnost();

	// Odporúčané metódy na nastavenie úrovne priehľadnosti
	/*
	public void priehľadnosť(double priehľadnosť);
	public void priehladnost(double priehľadnosť);
	public void priehľadnosť(Priehľadnosť objekt);
	public void priehladnost(Priehľadnosť objekt);
	public void upravPriehľadnosť(double zmena);
	public void upravPriehladnost(double zmena);
	*/
}
