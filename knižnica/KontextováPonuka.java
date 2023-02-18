
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2023 by Roman Horváth
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
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

// --------------------------------- //
//  *** Trieda KontextováPonuka ***  //
// --------------------------------- //

/**
 * <p>Trieda {@code currKontextováPonuka} spolu s triedou {@link 
 * KontextováPoložka KontextováPoložka} umožňujú používanie kontextových
 * ponúk. Voľba položky kontextovej ponuky spôsobí vznik udalosti {@link 
 * ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}.
 * V uvedenej obsluhe udalosti môžeme zistiť, ktorá položka bola zvolená
 * metódou {@link ÚdajeUdalostí#kontextováPoložka()
 * ÚdajeUdalostí.kontextováPoložka()}.</p>
 * 
 * <pre CLASS="example">
	{@code comm// Vytvoríme ponuku:}
	{@code kwdfinal} {@code currKontextováPonuka} mojaKontextováPonuka = {@code kwdnew} {@link #KontextováPonuka() KontextováPonuka}();

	{@code comm// Vytvoríme všetky položky, niektoré priamo pridávajúc do ponuky:}
	{@code kwdfinal} {@link KontextováPoložka KontextováPoložka} novýObjekt =
		mojaKontextováPonuka.{@link #pridajPoložku(String) pridajPoložku}({@code srg"Nový objekt"});
	{@code kwdfinal} {@link KontextováPoložka KontextováPoložka} preusporiadať =
		mojaKontextováPonuka.{@link #pridajPoložku(String) pridajPoložku}({@code srg"Preusporiadať"});

	{@code kwdfinal} {@link KontextováPoložka KontextováPoložka} ďalšiaAkcia1 =
		{@code kwdnew} {@link KontextováPoložka#KontextováPoložka(String) KontextováPoložka}({@code srg"Ďalšia akcia 1"});
	{@code kwdfinal} {@link KontextováPoložka KontextováPoložka} ďalšiaAkcia2 =
		{@code kwdnew} {@link KontextováPoložka#KontextováPoložka(String) KontextováPoložka}({@code srg"Ďalšia akcia 2"});
	{@code kwdfinal} {@link KontextováPoložka KontextováPoložka} ďalšiaAkcia3 =
		{@code kwdnew} {@link KontextováPoložka#KontextováPoložka(String) KontextováPoložka}({@code srg"Ďalšia akcia 3"});

	{@code comm// Pridáme oddeľovač a vnorenú ponuku:}
	mojaKontextováPonuka.{@link #pridajOddeľovač() pridajOddeľovač}();
	mojaKontextováPonuka.{@link #pridajPonuku(String, JMenuItem[]) pridajPonuku}({@code srg"Ďalšie akcie"},
		ďalšiaAkcia1, ďalšiaAkcia2, {@code valnull}, ďalšiaAkcia3);

	{@code comm// Definujeme obsluhu udalostí:}
	{@code kwdnew} {@link ObsluhaUdalostí#ObsluhaUdalostí() ObsluhaUdalostí}()
	{
		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#klik() klik}()
		{
			{@code comm// Kontextová ponuka sa zobrazí po stlačení pravého tlačidla:}
			{@code kwdif} ({@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#tlačidloMyši() tlačidloMyši}({@link Konštanty#PRAVÉ PRAVÉ}))
				mojaKontextováPonuka.{@link #zobraz() zobraz}();
		}

		{@code kwd@}Override {@code kwdpublic} {@code typevoid} {@link ObsluhaUdalostí#voľbaKontextovejPoložky() voľbaKontextovejPoložky}()
		{
			{@code comm// Zistíme, ktorá kontextová položka bola zvolená:}
			{@link KontextováPoložka KontextováPoložka} položka = {@link ÚdajeUdalostí ÚdajeUdalostí}.{@link ÚdajeUdalostí#kontextováPoložka() kontextováPoložka}();

			{@code comm// Na základe voľby vykonáme akciu:}
			{@code comm// …}

			{@code kwdif} (novýObjekt == položka)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka „Nový objekt.“"});
			}
			{@code kwdelse if} (preusporiadať == položka)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka „Preusporiadať.“"});
			}
			{@code kwdelse if} (ďalšiaAkcia1 == položka)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka „Ďalšia akcia 1.“"});
			}
			{@code kwdelse if} (ďalšiaAkcia2 == položka)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka „Ďalšia akcia 2.“"});
			}
			{@code kwdelse if} (ďalšiaAkcia3 == položka)
			{
				{@link Svet Svet}.{@link Svet#správa(String) správa}({@code srg"Zvolená položka „Ďalšia akcia 3.“"});
			}
		}
	};
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <table class="centered"><tr>
 * <td><image>ukazkaKontextovejPonukyWin.png<alt/></image></td>
 * <td rowspan="2"> </td>
 * <td><image>ukazkaKontextovejPonukyMac.png<alt/></image></td></tr>
 * <tr><td><p class="image">Ukážka kontextovej ponuky vytvorenej
 * príkladom<br />a zobrazenej v OS Windows.</p></td>
 * <td><p class="image">Ukážka kontextovej ponuky vytvorenej
 * príkladom<br />a zobrazenej v macOS (predtým OS X
 * a Mac OS).</p></td></tr></table>
 */
@SuppressWarnings("serial")
public class KontextováPonuka extends JPopupMenu
{
	// Popis…
	private String pôvodnýPopis = null;
	private final JLabel popis;

	// Nahradí niektoré znaky v texte, aby nenastali prípadné ťažkosti
	private static String nahradiťHTMLEntity(String text)
	{
		if (-1 != text.indexOf("<") ||
			-1 != text.indexOf(">") ||
			-1 != text.indexOf("&"))
		{
			text = text.replaceAll("&", "&amp;");
			text = text.replaceAll("<", "&lt;");
			text = text.replaceAll(">", "&gt;");
		}
		return text;
	}

	/**
	 * <p>Vytvorí novú kontextovú ponuku (bez titulného popisu, ktorý
	 * nebude možné pridať ani dodatočne).</p>
	 */
	public KontextováPonuka()
	{
		super();
		this.popis = null;
	}

	/**
	 * <p>Vytvorí kontextovú ponuku s titulným popisom. {@code text} nesmie
	 * byť {@code valnull} a môže byť dodatočne upravovaný metódou
	 * {@link #popis(String)}.</p>
	 * 
	 * @param popis text popisu ponuky; nesmie byť {@code valnull}
	 */
	public KontextováPonuka(String popis)
	{
		super();
		pôvodnýPopis = popis;
		if (popis.startsWith("<html>"))
			this.popis = new JLabel(popis);
		else
			this.popis = new JLabel("<html><b>" +
				nahradiťHTMLEntity(popis) + "</b></html>");
		this.popis.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.popis);
		addSeparator();
	}


	/**
	 * <p>Pripojí na koniec ponuky novú kontextovú položku, ktorej
	 * vytvorenie musí zabezpečiť programátor.</p>
	 * 
	 * @param položka nová kontextová položka (môže byť aj typu
	 *     {@link KontextováPoložka KontextováPoložka}), ktorú treba
	 *     pridať na koniec ponuky
	 * @return pridaná kontextová položka
	 * 
	 * @see #pridajPoložku(String)
	 * @see #pridajOddeľovač()
	 * @see #pridajPonuku(String, JMenuItem[])
	 * @see #vytvorPonuku(String, JMenuItem[])
	 */
	public JMenuItem pridajPoložku(JMenuItem položka)
	{ return add(položka); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(JMenuItem) pridajPoložku}.</p> */
	public JMenuItem pridajPolozku(JMenuItem položka)
	{ return add(položka); }

	/**
	 * <p>Vytvorí novú položku ponuky so zadaným textom a pripojí ju na
	 * koniec ponuky.</p>
	 * 
	 * @param text text novej kontextovej položky
	 * @return nová kontextová položka
	 * 
	 * @see #pridajPoložku(JMenuItem)
	 * @see #pridajOddeľovač()
	 * @see #pridajPonuku(String, JMenuItem[])
	 * @see #vytvorPonuku(String, JMenuItem[])
	 */
	public KontextováPoložka pridajPoložku(String text)
	{ return (KontextováPoložka)add(text); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajPoložku(String) pridajPoložku}.</p> */
	public KontextováPoložka pridajPolozku(String text)
	{ return (KontextováPoložka)add(text); }

	/**
	 * <p>Pridá nový oddeľovač položiek na koniec ponuky.</p>
	 * 
	 * @see #pridajPoložku(JMenuItem)
	 * @see #pridajPoložku(String)
	 * @see #pridajPonuku(String, JMenuItem[])
	 * @see #vytvorPonuku(String, JMenuItem[])
	 */
	public void pridajOddeľovač() { addSeparator(); }

	/** <p><a class="alias"></a> Alias pre {@link #pridajOddeľovač() pridajOddeľovač}.</p> */
	public void pridajOddelovac() { addSeparator(); }

	/**
	 * <p>Vytvorí novú vnorenú ponuku zo zadaných položiek a pripojí ju na
	 * koniec aktuálnej ponuky. Ak je niektorá zo zadaných položiek
	 * {@code valnull}, je do ponuky vložený oddeľovač.</p>
	 * 
	 * @param text text položky novej vnorenej ponuky
	 * @param položky zoznam položiek
	 * @return vytvorená (a pridaná) ponuka
	 * 
	 * @see #pridajPoložku(JMenuItem)
	 * @see #pridajOddeľovač()
	 * @see #vytvorPonuku(String, JMenuItem[])
	 */
	public JMenuItem pridajPonuku(String text, JMenuItem... položky)
	{ return add(vytvorPonuku(text, položky)); }

	/**
	 * <p>Vytvorí novú ponuku zo zadaných položiek. Využiteľné na vytváranie
	 * kaskádových ponúk. Ak je niektorá zo zadaných položiek {@code 
	 * valnull}, tak je do ponuky vložený oddeľovač.</p>
	 * 
	 * @param text text položky novej vnorenej ponuky
	 * @param položky zoznam položiek
	 * @return vytvorená ponuka
	 * 
	 * @see #pridajPoložku(JMenuItem)
	 * @see #pridajOddeľovač()
	 * @see #pridajPonuku(String, JMenuItem[])
	 */
	public static JMenu vytvorPonuku(String text, JMenuItem... položky)
	{
		JMenu ponuka = new JMenu(text);
		for (JMenuItem položka : položky)
		{
			if (null == položka) ponuka.addSeparator();
			else ponuka.add(položka);
		}
		return ponuka;
	}


	/**
	 * <p><a class="setter"></a> Nastaví text popisu ponuky.
	 * Má zmysel len v prípade, že ponuka bola vytvorená konštruktorom:
	 * {@link KontextováPonuka#KontextováPonuka(String)
	 * KontextováPonuka(popis)}. {@code text} nesmie byť {@code valnull}.</p>
	 * 
	 * <p><small>(<b>Pozri aj:</b>
	 * {@link JPopupMenu#setLabel(String)}.)</small></p>
	 * 
	 * @param text text popisu ponuky; nesmie byť {@code valnull}
	 */
	public void popis(String text) { setLabel(text); }

	/**
	 * <p><a class="getter"></a> Vráti popis kontextovej ponuky.
	 * Má zmysel len v prípade, že ponuka bola vytvorená konštruktorom:
	 * {@link KontextováPonuka#KontextováPonuka(String)
	 * KontextováPonuka(popis)}.</p>
	 * 
	 * @return text popisu ponuky alebo {@code valnull} ak ponuka nemôže
	 *     mať popis
	 */
	public String popis() { return getLabel(); }


	/**
	 * <p>Zobrazí kontextovú ponuku na poslednej zaznamenanej pozícii myši.</p>
	 */
	public void zobraz()
	{
		int x = ÚdajeUdalostí.poslednáUdalosťMyši.getX();
		int y = ÚdajeUdalostí.poslednáUdalosťMyši.getY();

		if (ÚdajeUdalostí.poslednáUdalosťMyši.getSource() instanceof Component)
			show((Component)ÚdajeUdalostí.poslednáUdalosťMyši.getSource(), x, y);
		else
			show(Svet.hlavnýPanel, x, y);
	}

	/**
	 * <p>Zobrazí kontextovú ponuku nad plátnom na zadanej pozícii.</p>
	 * 
	 * @param x x-ová súradnica polohy na zobrazenie ponuky
	 * @param y y-ová súradnica polohy na zobrazenie ponuky
	 */
	public void zobraz(double x, double y)
	{ show(Svet.hlavnýPanel, (int)Svet.prepočítajX(x), (int)Svet.prepočítajY(y)); }


	/**
	 * <p>Vytvorí novú položku ponuky so zadaným textom a pripojí ju na
	 * koniec ponuky.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JPopupMenu#add(String)}. </p>
	 * 
	 * <!-- TODO – priebežne dopĺňať rovnaké poznámky ku všetkým prekrytým
	 * metódam v programovacom rámci. -->
	 * 
	 * @param text text novej kontextovej položky
	 * @return nová kontextová položka; aj keď je návratová hodnota
	 *     typu {@link JMenuItem JMenuItem}, vrátený objekt je typu {@link 
	 *     KontextováPoložka KontextováPoložka}, t. j. výsledok môže byť
	 *     bez rizika pretypovaný
	 * 
	 * @see #pridajPoložku(String)
	 */
	@Override public JMenuItem add(String text)
	{ return add(new KontextováPoložka(text)); }

	/**
	 * <p><a class="setter"></a> Nastaví text popisu ponuky. Má zmysel len
	 * v prípade, že ponuka bola vytvorená konštruktorom: {@link 
	 * KontextováPonuka#KontextováPonuka(String) KontextováPonuka(popis)}.
	 * {@code text} nesmie byť {@code valnull}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JPopupMenu#setLabel(String)}. </p>
	 * 
	 * @param text text popisu ponuky; nesmie byť {@code valnull}
	 * 
	 * @see #popis(String)
	 */
	@Override public void setLabel(String text)
	{
		if (null == popis) return;
		pôvodnýPopis = text;

		if (text.startsWith("<html>"))
			popis.setText(text);
		else
			popis.setText("<html><b>" +
				nahradiťHTMLEntity(text) +
				"</b></html>");
	}

	/**
	 * <p><a class="getter"></a> Vráti popis kontextovej ponuky. Má zmysel
	 * len v prípade, že ponuka bola vytvorená konštruktorom: {@link 
	 * KontextováPonuka#KontextováPonuka(String)
	 * KontextováPonuka(popis)}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Táto metóda prekrýva originálnu
	 * metódu {@link JPopupMenu#getLabel()}. </p>
	 * 
	 * @return text popisu ponuky alebo {@code valnull} ak ponuka nemôže
	 *     mať popis
	 * 
	 * @see #popis()
	 */
	@Override public String getLabel() { return pôvodnýPopis; }
}
