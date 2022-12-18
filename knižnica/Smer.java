
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
 * <p>Rozhranie slúži na implementáciu metód slúžiacich na zistenie aktuálnej
 * orientácie (smeru/uhla) objektu a na definíciu konštánt pre základné smery.</p>
 */
public interface Smer
{
	// …
	/** <p>Hodnota uhla zodpovedajúca smeru „východ“ – 0°.</p> */
	public final static double VÝCHOD = 0.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „východ“ – 0°.</p> */
	public final static double VYCHOD = 0.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „severovýchod“ – 45°.</p> */
	public final static double SEVEROVÝCHOD = 45.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „severovýchod“ – 45°.</p> */
	public final static double SEVEROVYCHOD = 45.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „sever“ – 90°.</p> */
	public final static double SEVER = 90.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „severozápad“ – 135°.</p> */
	public final static double SEVEROZÁPAD = 135.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „severozápad“ – 135°.</p> */
	public final static double SEVEROZAPAD = 135.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „západ“ – 180°.</p> */
	public final static double ZÁPAD = 180.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „západ“ – 180°.</p> */
	public final static double ZAPAD = 180.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „juhozápad“ – 225°.</p> */
	public final static double JUHOZÁPAD = 225.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „juhozápad“ – 225°.</p> */
	public final static double JUHOZAPAD = 225.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „juh“ – 270°.</p> */
	public final static double JUH = 270.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „juhovýchod“ – 315°.</p> */
	public final static double JUHOVÝCHOD = 315.0;
	/** <p>Hodnota uhla zodpovedajúca smeru „juhovýchod“ – 315°.</p> */
	public final static double JUHOVYCHOD = 315.0;


	// Poznámka: Nasledujúce konštanty musia byť definované tu, inak sa
	//     neprenesú do triedy GRobot (a iných):


	/** <p>Inštancia uhla zodpovedajúca smeru „východ“ – 0°.</p> */
	public final static Uhol východ = new Uhol(VÝCHOD);
	/** <p>Inštancia uhla zodpovedajúca smeru „východ“ – 0°.</p> */
	public final static Uhol vychod = new Uhol(VYCHOD);
	/** <p>Inštancia uhla zodpovedajúca smeru „severovýchod“ – 45°.</p> */
	public final static Uhol severovýchod = new Uhol(SEVEROVÝCHOD);
	/** <p>Inštancia uhla zodpovedajúca smeru „severovýchod“ – 45°.</p> */
	public final static Uhol severovychod = new Uhol(SEVEROVYCHOD);
	/** <p>Inštancia uhla zodpovedajúca smeru „sever“ – 90°.</p> */
	public final static Uhol sever = new Uhol(SEVER);
	/** <p>Inštancia uhla zodpovedajúca smeru „severozápad“ – 135°.</p> */
	public final static Uhol severozápad = new Uhol(SEVEROZÁPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „severozápad“ – 135°.</p> */
	public final static Uhol severozapad = new Uhol(SEVEROZAPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „západ“ – 180°.</p> */
	public final static Uhol západ = new Uhol(ZÁPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „západ“ – 180°.</p> */
	public final static Uhol zapad = new Uhol(ZAPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „juhozápad“ – 225°.</p> */
	public final static Uhol juhozápad = new Uhol(JUHOZÁPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „juhozápad“ – 225°.</p> */
	public final static Uhol juhozapad = new Uhol(JUHOZAPAD);
	/** <p>Inštancia uhla zodpovedajúca smeru „juh“ – 270°.</p> */
	public final static Uhol juh = new Uhol(JUH);
	/** <p>Inštancia uhla zodpovedajúca smeru „juhovýchod“ – 315°.</p> */
	public final static Uhol juhovýchod = new Uhol(JUHOVÝCHOD);
	/** <p>Inštancia uhla zodpovedajúca smeru „juhovýchod“ – 315°.</p> */
	public final static Uhol juhovychod = new Uhol(JUHOVYCHOD);


	// vpravo/doprava, vľavo… ; otoč[?] ; otočNa[Myš]

	/** <p>Prototyp metódy na zistenie smeru/uhla objektu.</p> */
	public double uhol();
	/** <p>Prototyp metódy na zistenie smeru/uhla objektu.</p> */
	public double smer();

	// Odporúčané metódy na nastavenie smeru/uhla objektu
	/* * <p>Prototyp metódy na nastavenie smeru/uhla objektu.</p> * /
	public void uhol(double uhol);
	/* * <p>Prototyp metódy na nastavenie smeru/uhla objektu.</p> * /
	public void smer(double uhol);
	/* * <p>Prototyp metódy na nastavenie smeru/uhla objektu.</p> * /
	public void otoč(double uhol);
	/* * <p>Prototyp metódy na nastavenie smeru/uhla objektu.</p> * /
	public void otoc(double uhol);
	*/
}
