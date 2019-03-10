
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

package knižnica;

/**
 * <p>Rozhranie slúži na implementáciu metód slúžiacich na zistenie polohy
 * objektu. Rozhranie vhodne dopĺňa jeho implementácia {@link Bod Bod}.</p>
 */
public interface Poloha
	// Ukázalo sa, že trieda Point2D.Double je na tieto účely nepoužiteľná.
	// Pri jej použití vznikali obrovské problémy dotýkajúce sa funkčnosti
	// prekrývania metódy kresliTvar() triedy GRobot. Možno išlo o nahlásiteľný
	// opraviteľný bug Javy, ale jednoduchšie pre nás bolo triedu nepoužiť,
	// navyše sme zhodnotili, že použitie rozhrania bude výhodnejšie.
{
	// public class Stred extends Bod

	/**
	 * <p>Preddefinovaná inštancia stredu súradnicovej sústavy. Ide o polohu
	 * so súradnicami 0, 0. Hodnotu tohto bodu nie je možné zmeniť!</p>
	 */
	public final static Bod stred = new Bod()
	{
		// /**
		//  * Konštruktor, ktorý umožňuje korektnú inicializáciu tohto objektu.
		//  * (Bez tohto prekrytia, by inicializácia stredu zlyhala.)
		//  */
		// private Stred(double x, double y) { super.setLocation(x, y); }

		@Override public void setLocation(double x, double y)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void polohaX(double x)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void polohaY(double y)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void súradnicaX(double x)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void suradnicaX(double x)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void súradnicaY(double y)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void suradnicaY(double y)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void poloha(double x, double y)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }

		@Override public void poloha(Poloha poloha)
		{ throw new GRobotException("Polohu stredu nie je možné meniť!",
			"zeroPositionCannotBeChanged"); }
	};

	// …
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double polohaX();
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double polohaY();
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double súradnicaX();
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double suradnicaX();
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double súradnicaY();
	/** <p>Prototyp metódy na zistenie súradnice objektu.</p> */
	public double suradnicaY();

	/** <p>Prototyp metódy na zistenie obidvoch súradníc objektu.</p> */
	public Bod poloha();

	// Odporúčané protipóly metód na nastavenie súradníc alebo polohy objektu
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void polohaX(double novéX);
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void polohaY(double novéY);
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void súradnicaX(double novéX);
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void suradnicaX(double novéX);
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void súradnicaY(double novéY);
	/* * Prototyp metódy na nastavenie súradnice objektu. * /
	public void suradnicaY(double novéY);

	/* * Prototyp metódy na nastavenie obidvoch súradníc objektu. * /
	public void poloha(Poloha poloha);
	*/

	// skočNa[Myš]
}
