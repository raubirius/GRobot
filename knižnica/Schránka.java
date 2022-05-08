
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

 // Táto trieda bola do verzie 1.85 vnorenou triedou ústrednej triedy GRobot.
 // Po tejto verzii sa osamostatnila a teraz tvorí samostatnú triedu balíčka
 // programového rámca skupiny tried grafického robota.

package knižnica;

import java.awt.Image;
import java.awt.Toolkit;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import knižnica.podpora.SVGClip;

// ------------------------- //
//  *** Trieda Schránka ***  //
// ------------------------- //

/**
 * <p>Táto trieda pokrýva základné činnosti so schránkou operačného
 * systému. Schránka (angl. Clipboard) je odkladací priestor operačného
 * systému, ktorý dovoľuje prenášať informácie rôzneho druhu medzi
 * aplikáciami. Obvykle sú základným operáciám na prácu so schránkou
 * priradené známe klávesové skratky: {@code Ctrl + C} (Windows), {@code 
 * ⌘ + C} (<small>Command + C</small> – Macintosh) na vloženie
 * (kopírovanie) informácie do schránky; {@code Ctrl + V} (Windows),
 * {@code ⌘ + V} (<small>Command + C</small>, Macintosh) na prevzatie
 * (prilepenie, „vloženie“) informácie zo schránky (terminológia v tejto
 * oblasti sa v súčasnosti, žiaľ, značne rôzni). Vo svete robota tieto
 * skratky nefungujú automaticky. Ak chceme, aby aplikácia vytvorená
 * s pomocou robota používala schránku, musíme prepojiť náležité
 * {@linkplain PoložkaPonuky položky ponuky} (ktoré musíme vytvoriť)
 * a prepojiť s funkcionalitou (volaním metód a prípadným
 * doprogramovaním) tejto statickej triedy. Ako bolo práve čiastočne
 * podotknuté, táto trieda i všetky jej metódy sú statické. Z toho
 * vyplýva, že nemá zmysel vytvárať jej inštancie ({@linkplain 
 * GRobot#schránka jedna je i tak vytvorená automaticky}). Použitie
 * triedy je jednoduché, stačí napísať názov triedy (prípadne jej
 * statickej inštancie – rozdiel je vo veľkosti prvého písmena)
 * a za bodkou názov metódy, ktorú chceme použiť. Napríklad na
 * vloženie textu do schránky slúži nasledujúci riadok kódu:</p>
 * 
 * <pre CLASS="example">
	{@code currschránka}.{@link #text(String) text}({@code srg"Tento text bude skopírovaný do schránky…"});
	</pre>
 * 
 * <p>Text zo schránky, naopak, prevezmeme nasledujúcim riadkom kódu:</p>
 * 
 * <pre CLASS="example">
	{@link String String} text = {@code currschránka}.{@link #text() text}();
	</pre>
 * 
 * <p>Ďalšie spracovanie prevzatého textu zariadime podľa potreby,
 * napríklad: text pripojíme do ďalšej premennej, {@linkplain 
 * Svet#vypíšRiadok(Object[]) vypíšeme} ho na obrazovku, zobrazíme
 * v dialógu ({@linkplain Svet#správa(String) správe}) a podobne.</p>
 */
public class Schránka
{
	// Toto sa podarilo vyriešiť až na Vianoce roku 2021 a nakoniec to šlo
	// úplne inak (vďaka tomu, že medzitým to niekto dosť významne posunul
	// vo fórach, takže som si to už len trochu prispôsobil a „doťukol“ –
	// síce som spočiatku pol roka čakal na odpoveď, ktorá neprichádzala,
	// ale potom som našiel riešenie aj bez nej):
	// 
	// | Zatiaľ sa z časových dôvodov nepodarilo doriešiť transfer vektorových
	// | tvarov cez schránku OS. *** História stavu *** Stiahol som originál
	// | aj extrahovanú verziu schránky z knižnice JSesh: http://comp.
	// | qenherkhopeshef.org/blog/jvectcutandpaste, https://codeload.github.
	// | com/rosmord/jvectclipboard/zip/master, ale:
	// | 
	// | Bude treba odstrániť závislosť „lowagie,“ ktorá súvisí s prácou s PDF
	// | súbormi, pretože „lowagie classes have been deprecated in 2012 and
	// | should no longer be used both for technical as well as legal
	// | reasons“ (https://www.lowagie.com/iText, https://itextpdf.com/en/
	// | resources/faq/legal/itext-5-legacy/can-itext-217-itextsharp-416-or-
	// | earlier-be-used-commercially), ale myslím si, že sa vieme bez PDF
	// | transferu zaobísť.


	// Táto trieda uchováva obrazovú informáciu počas prítomnosti
	// v schránke
	// Zdroj: http://www.exampledepot.com/egs/java.awt.datatransfer/ToClipImg.html
	private static class ImageSelection implements Transferable
	{
		// Konštanta „príchutí“ dát…
		private final static DataFlavor[] dataFlavors =
			new DataFlavor[] { DataFlavor.imageFlavor };

		// Údaje
		private Image image;

		// Konštruktor
		public ImageSelection(Image image) { this.image = image; }

		// Vráť podporované „príchute“ (typy údajov v schránke)
		public DataFlavor[] getTransferDataFlavors() { return dataFlavors; }

		// Vráti true, ak je stanovená „príchuť“ (stanovený typ údajov)
		// podporovaná
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{ return DataFlavor.imageFlavor.equals(flavor); }

		// Vráti obrázok
		public Object getTransferData(DataFlavor flavor) throws
			UnsupportedFlavorException, IOException
		{
			if (!DataFlavor.imageFlavor.equals(flavor))
				throw new UnsupportedFlavorException(flavor);
			return image;
		}
	}

	// Prázdna implementácia poslucháča rozhrania
	private static class Poslucháč implements ClipboardOwner { public void
		lostOwnership(Clipboard aClipboard, Transferable aContents) {}}
	private static Poslucháč poslucháč = new Poslucháč();

	// Objekt systémovej schránky
	private static Clipboard schránka = Toolkit.
		getDefaultToolkit().getSystemClipboard();

	// Objekt schránky na čítanie
	private static Transferable obsahSchránky = null;

	// Nie je možné (a aj zbytočné) vytvárať inštancie
	/*packagePrivate*/ Schránka() {}

	/**
	 * <p>Prevezme zo schránky text, ak schránka obsahuje textovú
	 * informáciu.</p>
	 * 
	 * @return text, ktorý bol obsiahnutý v schránke alebo {@code valnull}
	 *     ak schránka neobsahovala textovú informáciu
	 */
	public static String text()
	{
		String prevzatýText = null;
		obsahSchránky = schránka.getContents(null);

		if (null != obsahSchránky && obsahSchránky.
			isDataFlavorSupported(DataFlavor.stringFlavor))
		{
			try
			{
				prevzatýText = (String)obsahSchránky.getTransferData
					(DataFlavor.stringFlavor);
			}
			catch (UnsupportedFlavorException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				// Vysoko nepravdepodobné, keďže používame
				// štandardný DataFlavor
				// System.out.println(e);
				// e.printStackTrace();
			}
			catch (IOException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				// System.out.println(e);
				// e.printStackTrace();
			}
		}

		return prevzatýText;
	}

	/**
	 * <p>Vloží do schránky zadaný text.</p>
	 * 
	 * @param reťazec text na vloženie do schránky
	 * @return {@code valtrue} ak bola operácia úspešná
	 */
	public static boolean text(String reťazec)
	{
		StringSelection textovýVýber = new StringSelection(reťazec);

		try
		{
			schránka.setContents(textovýVýber, poslucháč);
			return true;
		}
		catch (IllegalStateException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}


	/**
	 * <p>Prevezme zo schránky obrázok, ak schránka obsahuje obrazovú
	 * informáciu. (V prípade úspešného prevzatia obrazovej informácie zo
	 * schránky vráti metóda nový objekt typu {@link Obrázok Obrázok}.
	 * Tento je automaticky registrovaný vo vnútornom zozname obrázkov.
	 * Ak je objekt s obrázkom určený len na jedno použitie, mal by byť po
	 * skončení práce s ním {@linkplain Svet#uvoľni(Obrázok)
	 * uvoľnený}, inak zostane prítomný v pamäti počítača až do ukončenia
	 * činnosti aplikácie.)</p>
	 * 
	 * @return nový objekt s obrázkom, ktorý bol obsiahnutý v schránke,
	 *     alebo {@code valnull} ak schránka neobsahovala obrazovú
	 *     informáciu
	 */
	public static Obrázok obrázok()
	{
		Obrázok prevzatýObrázok = null;
		obsahSchránky = schránka.getContents(null);

		if (null != obsahSchránky && obsahSchránky.
			isDataFlavorSupported(DataFlavor.imageFlavor))
		{
			try
			{
				Image obrázok = (Image)obsahSchránky.
					getTransferData(DataFlavor.imageFlavor);

				prevzatýObrázok = new Obrázok(
					obrázok.getWidth(null), obrázok.getHeight(null));
				prevzatýObrázok.grafika.translate(
					-prevzatýObrázok.posunX, -prevzatýObrázok.posunY);
				prevzatýObrázok.grafika.drawImage(obrázok, 0, 0, null);
				prevzatýObrázok.grafika.translate(
					prevzatýObrázok.posunX, prevzatýObrázok.posunY);
			}
			catch (UnsupportedFlavorException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				// Vysoko nepravdepodobné, keďže používame
				// štandardný DataFlavor
				// System.out.println(e);
				// e.printStackTrace();
			}
			catch (IOException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
				// System.out.println(e);
				// e.printStackTrace();
			}
		}

		return prevzatýObrázok;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázok() obrázok}.</p> */
	public static Obrazok obrazok() { return new Obrazok(obrázok()); }

	/**
	 * <p>Vloží do schránky zadaný obrázok.</p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Obrázok je uchovaný
	 * v schránke len počas činnosti aplikácie robota. Po zatvorení
	 * okna sveta, je obrázok zo schránky odstránený.</p>
	 * 
	 * @param obrázok rastrový obrázok; môže byť aj objekt typu {@link 
	 *     Obrázok Obrázok}
	 * @return {@code valtrue} ak bola operácia úspešná
	 */
	public static boolean obrázok(Image obrázok)
	{
		ImageSelection obrázkovýVýber = new ImageSelection(obrázok);

		try
		{
			schránka.setContents(obrázkovýVýber, poslucháč);
			return true;
		}
		catch (IllegalStateException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}

	// Pomocná metóda na importovanie SVG kresby.
	private static String inputStreamToString(Object obj)
	{
		if (obj instanceof InputStream)
			return new BufferedReader(new InputStreamReader(
				(InputStream)obj, StandardCharsets.UTF_8)).lines().
					collect(Collectors.joining("\n"));
		return null;
	}

	/** <p><a class="alias"></a> Alias pre {@link #obrázok(Image) obrázok}.</p> */
	public static boolean obrazok(Image obrázok)
	{ return obrázok(obrázok); }

	/**
	 * <p>Prevezme zo schránky kresbu vo formáte SVG, ak schránka obsahuje
	 * informáciu v tomto formáte. V prípade úspešného prevzatia kresby zo
	 * schránky vráti metóda nový objekt typu {@link SVGPodpora SVGPodpora},
	 * ktorý bude obsahovať objekty rozpoznané z SVG definície, ktorá bola
	 * uložená v schránke. V opačnom prípade vráti táto metóda hodnotu
	 * {@code valnull}.</p>
	 * 
	 * @return nový objekt typu {@link SVGPodpora SVGPodpora} s rozpoznanými
	 *     grafickými objektmi z SVG definície v schránke alebo
	 *     {@code valnull}, ak schránka neobsahovala relevantnú informáciu
	 */
	public static SVGPodpora kresba()
	{
		String kresba = null;
		SVGPodpora prevzatáKresba = null;
		obsahSchránky = schránka.getContents(null);

		if (null != obsahSchránky)
		{
			if (obsahSchránky.isDataFlavorSupported(
				SVGClip.svgFlavor))
			{
				try
				{
					kresba = inputStreamToString(obsahSchránky.
						getTransferData(SVGClip.svgFlavor));
				}
				catch (UnsupportedFlavorException | IOException e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}
			else if (obsahSchránky.isDataFlavorSupported(
				SVGClip.inkscapeFlavor))
			{
				try
				{
					kresba = inputStreamToString(obsahSchránky.
						getTransferData(SVGClip.inkscapeFlavor));
				}
				catch (UnsupportedFlavorException | IOException e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}
			else
			{
				try
				{
					DataFlavor[] flavors = schránka.getAvailableDataFlavors();

					for (DataFlavor flavor : flavors)
					{
						if (flavor.equals(SVGClip.svgFlavor))
						{
							kresba = inputStreamToString(schránka.
								getData(SVGClip.svgFlavor));
						}
						else if (flavor.equals(SVGClip.inkscapeFlavor))
						{
							kresba = inputStreamToString(schránka.
								getData(SVGClip.inkscapeFlavor));
						}
					}
				}
				catch (UnsupportedFlavorException | IOException e)
				{
					GRobotException.vypíšChybovéHlásenia(e);
				}
			}
		}

		if (null != kresba)
		{
			prevzatáKresba = new SVGPodpora();
			try
			{
				prevzatáKresba.pridajSVG(kresba);
			}
			catch (GRobotException e)
			{
				GRobotException.vypíšChybovéHlásenia(e);
			}
		}

		return prevzatáKresba;
	}

	/**
	 * <p>Vloží do schránky kresbu v SVG formáte zadanú vo forme objektu
	 * {@link SVGPodpora SVGPodpora}.</p>
	 * 
	 * @param svgPodpora inštancia triedy {@link SVGPodpora SVGPodpora}
	 * @return {@code valtrue} ak bola operácia úspešná
	 */
	public static boolean kresba(SVGPodpora svgPodpora)
	{
		try
		{
			SVGClip klipKresby = new SVGClip(svgPodpora.dajSVG());
			schránka.setContents(klipKresby, poslucháč);
			return true;
		}
		catch (IllegalStateException | IOException e)
		{
			GRobotException.vypíšChybovéHlásenia(e);
		}

		return false;
	}
}
