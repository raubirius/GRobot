
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

import java.awt.Font;
import java.awt.font.TextAttribute;

import java.io.IOException;

import java.text.AttributedCharacterIterator;

import java.util.HashMap;
import java.util.Map;

// ---------------------- //
//  *** Trieda Písmo ***  //
// ---------------------- //

/**
 * <p>Trieda obaľuje triedu Javy {@link Font Font}. Táto trieda kopíruje
 * najdôležitejšie vlastnosti pôvodnej triedy. Veľa užitočných informácií
 * o písmach nájdete pri podrobnostiach konštruktora
 * {@link #Písmo(String, int, double) Písmo(názov, štýl, veľkosť)}.</p>
 * 
 * <p><b>Príklad:</b></p>
 * 
 * <p>Výsledkom kreslenia tohto príkladu sú nápisy napísané rôznymi
 * veľkosťami a rezmi písma z rodiny písiem Arial (<small>pozri zoznam
 * zmien: <a href="zoznam-zmien.html">poďakovanie</a> uvedené pri
 * verzii 1.35</small>):</p>
 * 
 * <pre CLASS="example">
	{@link GRobot#skočNa(double, double) skočNa}({@code num0}, &#45;{@code num200});

	{@code kwdfor} ({@code typeint} i = {@code num0}; i &lt; {@code num10}; ++i)
	{
		{@code typeint} a = ({@code typeint}){@link Svet Svet}.{@link Svet#náhodnéCeléČíslo(long, long) náhodnéCeléČíslo}({@code num10}, {@code num55});

		{@code kwdif} (a &lt;= {@code num25})
			{@link GRobot#písmo(Font) písmo}({@code kwdnew} {@link Písmo#Písmo(String, int, double) Písmo}({@code srg"Arial"}, {@code currPísmo}.{@link Písmo#OBYČAJNÉ OBYČAJNÉ}, a));
		{@code kwdelse if} (a &lt;= {@code num40})
			{@link GRobot#písmo(Font) písmo}({@code kwdnew} {@link Písmo#Písmo(String, int, double) Písmo}({@code srg"Arial "}, {@code currPísmo}.{@link Písmo#TUČNÉ TUČNÉ}, a));
		{@code kwdelse}
			{@link GRobot#písmo(Font) písmo}({@code kwdnew} {@link Písmo#Písmo(String, int, double) Písmo}({@code srg"Arial"}, {@code currPísmo}.{@link Písmo#TUČNÉ TUČNÉ} | {@code currPísmo}.{@link Písmo#KURZÍVA KURZÍVA}, a));

		{@link GRobot#text(String) text}({@code srg"Zmena písma"});
		{@link GRobot#skoč(double, double) skoč}({@code num0}, {@code num40});
	}
	</pre>
 * 
 * <p><b>Výsledok:</b></p>
 * 
 * <p><image>zmenaPisma.png<alt/>Náhodne generované písma.</image>Jeden
 * z možných výsledkov vykonávania tohto príkladu.</p>
 * 
 * @see java.awt.Font
 */
@SuppressWarnings("serial")
public class Písmo extends Font
{
	// Pozri aj: https://docs.oracle.com/javase/8/docs/api/java/awt/Font.html

	/**
	 * <p>Konštanta označujúca obyčajné písmo. Nachádza využitie
	 * v konštruktore prijímajúcom štýl: {@link 
	 * Písmo#Písmo(String, int, double) Písmo(názov, štýl,
	 * veľkosť)}.</p>
	 */
	public final static int OBYČAJNÉ = Font.PLAIN;

	/** <p><a class="alias"></a> Alias pre {@link #OBYČAJNÉ OBYČAJNÉ}.</p> */
	public final static int OBYCAJNE = Font.PLAIN;

	/** <p><a class="alias"></a> Alias pre {@link #OBYČAJNÉ OBYČAJNÉ}.</p> */
	public final static int NORMÁLNE = Font.PLAIN;

	/** <p><a class="alias"></a> Alias pre {@link #OBYČAJNÉ OBYČAJNÉ}.</p> */
	public final static int NORMALNE = Font.PLAIN;

	/**
	 * <p>Konštanta označujúca tučné písmo. Môže byť skombinovaná
	 * s {@linkplain #KURZÍVA kuzívou}. Konštanta nachádza využitie
	 * v konštruktore prijímajúcom štýl: {@link 
	 * Písmo#Písmo(String, int, double) Písmo(názov, štýl,
	 * veľkosť)}.</p>
	 */
	public final static int TUČNÉ = Font.BOLD;

	/** <p><a class="alias"></a> Alias pre {@link #TUČNÉ TUČNÉ}.</p> */
	public final static int TUCNE = Font.BOLD;

	/** <p><a class="alias"></a> Alias pre {@link #TUČNÉ TUČNÉ}.</p> */
	public final static int SILNÉ = Font.BOLD;

	/** <p><a class="alias"></a> Alias pre {@link #TUČNÉ TUČNÉ}.</p> */
	public final static int SILNE = Font.BOLD;

	/**
	 * <p>Konštanta označujúca písmo kurzívy. Môže byť skombinovaná
	 * s {@linkplain #TUČNÉ tučným písmom}. Konštanta nachádza využitie
	 * v konštruktore prijímajúcom štýl: {@link 
	 * Písmo#Písmo(String, int, double) Písmo(názov, štýl,
	 * veľkosť)}.</p>
	 */
	public final static int KURZÍVA = Font.ITALIC;

	/** <p><a class="alias"></a> Alias pre {@link #KURZÍVA KURZÍVA}.</p> */
	public final static int KURZIVA = Font.ITALIC;

	/** <p><a class="alias"></a> Alias pre {@link #KURZÍVA KURZÍVA}.</p> */
	public final static int ŠIKMÉ = Font.ITALIC;

	/** <p><a class="alias"></a> Alias pre {@link #KURZÍVA KURZÍVA}.</p> */
	public final static int SIKME = Font.ITALIC;

	/**
	 * <p>Vytvorí nové písmo z jestvujúceho objektu typu {@link Font Font}.</p>
	 * 
	 * @param font objekt {@link Font Font}, z ktorého má byť vytvorené
	 *     nové písmo
	 */
	public Písmo(Font font) { super(font); }

	/**
	 * <p>Vytvorí nové písmo s atribútmi určenými v parametri {@code 
	 * attributes}. Ak je tento parameter rovný {@code valnull}, písmo
	 * bude vytvorené s predvolenými hodnotami.</p>
	 * 
	 * @param attributes atribúty, ktoré majú byť priradené novému písmu,
	 *     alebo {@code valnull}
	 */
	public Písmo(Map<? extends AttributedCharacterIterator.Attribute, ?> attributes)
	{ super(attributes); }

	/**
	 * <p>Vytvorí nové písmo s určeným názvom, štýlom a veľkosťou.</p>
	 * 
	 * <p>Názov písma môže byť (z technického pohľadu) buď názov rodiny
	 * písiem (anlg. font family name; ten je vo svete robota z dôvodu
	 * zjednodušenia považovaný za {@linkplain #názov() názov písma}),
	 * alebo názov rezu písma (ang. font face name), čo je užšia
	 * špecifikácia (príklad: Helvetica Bold, Arial Italic). Pri
	 * vytváraní písma je názov kombinovaný so štýlom, aby bolo nájdené
	 * vyhovujúce písmo. Ak napríklad zadáme názov písma ako {@code 
	 * srg"Arial Bold"} (čo znamená tučné písmo z rodiny písiem Arial)
	 * a za štýl dosadíme {@link #KURZÍVA Písmo.KURZÍVA} (anglicky
	 * „italic“), systém spravujúci písma vyhľadá písmo v rodine Arial,
	 * ktoré bude aj tučné aj šikmé – {@code srg"Arial Bold Italic"}.
	 * Názov je so štýlom zlučovaný, nie sčítavnaný, ani odčítavaný. Ak
	 * určíme názov písma {@code srg"Arial Bold"} a štýl {@link #TUČNÉ
	 * Písmo.TUČNÉ}, nezískame dvojnásobne tučné písmo, ale iba
	 * {@linkplain #TUČNÉ TUČNÉ}, rovnako písmo nebude stenčené ak
	 * určíme názov {@code srg"Arial Bold"} a štýl {@link #OBYČAJNÉ
	 * Písmo.OBYČAJNÉ}. Názov môže byť aj názov logického písma
	 * v angličtine: Dialog, DialogInput, Monospaced, Serif alebo
	 * SansSerif. Logické názvy<sup>[1]</sup> určujú všeobecný typ písma –
	 * písmo pre dialógy, vstupné dialógy, s pevnou šírkou znakov,
	 * serifové (pätkové) alebo bezserifové (bezpätkové).</p>
	 * 
	 * <p>Ak nie je možné nájsť konkrétny rez písma, systém na správu
	 * písiem môže algoritmicky písmo vyrobiť. Ak napríklad požadujeme
	 * {@linkplain #ŠIKMÉ šikmé} písmo, ale v stanovenej rodine taký rez
	 * nejestvuje, systém môže jednotlivé znaky obyčajného písma
	 * zošikmiť. Ak systém písmo nedokáže nájsť vôbec, vytvorí písmo
	 * typu Dialog (čiže vytvorí logické písmo).</p>
	 * 
	 * <p><small>[1] – termín „logický“ je v počítačovej terminológii
	 * často používaný ako opak „fyzického“; logický môže znamenať
	 * všeobecný, nekonkrétny, patriaci do určitej množiny, vytvorený na
	 * určitý účel… naopak fyzický znamená kokrétny, pevný,
	 * stanovený…</small></p>
	 * 
	 * @param názov názov písma (názov rezu alebo rodiny písiem) alebo
	 *     logického písma; ak zadáme {@code valnull} systém vytvorí
	 *     písmo, ktorému priradí logický názov {@code srg"Default"}
	 * @param štýl konštanta štýlu písma – {@link #OBYČAJNÉ
	 *     Písmo.OBYČAJNÉ}, {@link #TUČNÉ Písmo.TUČNÉ}, {@link #KURZÍVA
	 *     Písmo.KURZÍVA} alebo kombinácia {@link #TUČNÉ
	 *     Písmo.TUČNÉ}<code> | </code>{@link #KURZÍVA Písmo.KURZÍVA};
	 *     pri nesprávne zadanom štýle je vytvorené {@linkplain 
	 *     #OBYČAJNÉ obyčajné} písmo
	 * @param veľkosť veľkosť písma v bodoch (hodnota je zaokrúhlená na
	 *     typ {@code typefloat})
	 */
	public Písmo(String názov, int štýl, double veľkosť)
	{
		// Old:
		// public Písmo(String názov, int štýl, int veľkosť)
		// { super(názov, štýl, veľkosť); }
		super(zostavAtribúty(
			TextAttribute.FAMILY, názov,
			TextAttribute.WEIGHT, 0 == (štýl & Font.BOLD) ?
				TextAttribute.WEIGHT_REGULAR :
				TextAttribute.WEIGHT_BOLD,
			TextAttribute.POSTURE, 0 == (štýl & Font.ITALIC) ?
				TextAttribute.POSTURE_REGULAR :
				TextAttribute.POSTURE_OBLIQUE,
			TextAttribute.SIZE, new Float((float)veľkosť)));
	}

	/**
	 * <p>Vytvorí nové písmo podľa zadaných atribútov určujúcich názov, štýl,
	 * veľkosť písma, horný/dolný index, prečiarknutie alebo podčiarknutie.</p>
	 * 
	 * <p>Tento konštruktor prijíma variabilný počet parametrov rôznych
	 * údajových typov. Niektoré parametre figurujú samostatne, iné vo
	 * dvojiciach. Konštruktor ich spracúva postupne:</p>
	 * 
	 * <ol>
	 * <li>Ak nájde reťazec, zistí jeho význam a podľa potreby spracuje
	 * ďalší parameter.</li>
	 * <li>Ak nájde údajový typ Javy {@link TextAttribute TextAttribute},
	 * tak vždy prevezme ďalší parameter, ktorý použije ako hodnotu.</li>
	 * </ol>
	 * 
	 * <p>V druhom prípade musí mať ďalší parameter aj správny údajový
	 * typ, pričom konštruktor dokáže zabezpečiť konverziu reťazcov na
	 * čísla alebo logické hodnoty pre zvolené atribúty
	 * ({@link TextAttribute TextAttribute}). Konkrétne:
	 * {@link TextAttribute#WEIGHT WEIGHT},
	 * {@link TextAttribute#WIDTH WIDTH},
	 * {@link TextAttribute#POSTURE POSTURE},
	 * {@link TextAttribute#SIZE SIZE},
	 * {@link TextAttribute#JUSTIFICATION JUSTIFICATION},
	 * {@link TextAttribute#TRACKING TRACKING},
	 * 
	 * {@link TextAttribute#SUPERSCRIPT SUPERSCRIPT},
	 * {@link TextAttribute#UNDERLINE UNDERLINE},
	 * {@link TextAttribute#BIDI_EMBEDDING BIDI_EMBEDDING},
	 * {@link TextAttribute#INPUT_METHOD_UNDERLINE INPUT_METHOD_UNDERLINE},
	 * {@link TextAttribute#KERNING KERNING},
	 * {@link TextAttribute#LIGATURES LIGATURES},
	 * 
	 * {@link TextAttribute#STRIKETHROUGH STRIKETHROUGH},
	 * {@link TextAttribute#RUN_DIRECTION RUN_DIRECTION}
	 * a {@link TextAttribute#SWAP_COLORS SWAP_COLORS}.
	 * (Úplný zoznam atribútov nájdete v opise triedy
	 * {@link TextAttribute TextAttribute}.)</p>
	 * 
	 * <p>Nasledujúca tabuľka zhŕňa povolené významy reťazcov pre prvý
	 * prípad a očakávané tvary ďalšieho parametra (pričom reťazce
	 * v druhej časti nespracúvajú žiadny ďalší parameter, takže ak
	 * nasledujúci parameter jestvuje, tak pôsobí opäť samostatne):</p>
	 * 
	 * <style><!--
	 * table.txtAttrList tr th:nth-child(1),
	 * table.txtAttrList tr td:nth-child(1)
	 * { width: 20%; }
	 * 
	 * table.txtAttrList tr th:nth-child(2),
	 * table.txtAttrList tr td:nth-child(2)
	 * { width: 20%; }
	 * 
	 * table.txtAttrList tr th:nth-child(3),
	 * table.txtAttrList tr td:nth-child(3)
	 * { width: 60%; }
	 * --></style><!-- TODO – presunúť do štýlu – pohľadať duplicity. -->
	 * 
	 * <table class="shadedTable txtAttrList">
	 * <tr><th>Tvar atribútu</th><th>Význam atribútu</th><th>Očakávané
	 * hodnoty</th></tr>
	 * 
	 * <tr><td>{@code srg"Názov"}, {@code srg"Rodina"}, {@code srg"Name"},
	 * {@code srg"FamilyName"}, {@code srg"FaceName"}…</td><td>Názov rodiny
	 * písiem s prípadným určením konkrétneho rezu.</td>
	 * <td>Napríklad: {@code srg"Default"}, {@code srg"Arial Bold"},
	 * {@code srg"Courier New"}…</td></tr>
	 * 
	 * <tr><td>{@code srg"Štýl"}, {@code srg"Rez"}, {@code srg"Style"},
	 * {@code srg"TypeFace"}…</td><td>Spresnenie rodiny písiem a určenie
	 * prípadných ďalších znakov.</td>
	 * <td>Môže byť kombinácia slov (v jednom reťazci):
	 * {@code prečiarknutý}/&#8203;{@code prečiarknutá}/&#8203;{@code 
	 * prečiarknuté},
	 * {@code podčiarknutý}/&#8203;{@code podčiarknutá}/&#8203;{@code 
	 * podčiarknuté},
	 * {@code tučný}/&#8203;{@code tučná}/&#8203;{@code tučné},
	 * {@code silný}/&#8203;{@code silná}/&#8203;{@code silné},
	 * {@code šikmý}/&#8203;{@code šikmá}/&#8203;{@code 
	 * šikmé}/&#8203;{@code kurzíva},
	 * {@code horný}/&#8203;{@code dolný} ({@code index}) alebo ich
	 * anglických alternatív, príklady: {@code srg"Tučná kurzíva"},
	 * {@code srg"Šikmý horný index"}, {@code 
	 * srg"Prečiarknuté tučné"}…</td></tr>
	 * 
	 * <tr><td>{@code srg"Veľkosť"}, {@code srg"Size"}</td><td>Určenie
	 * veľkosti písma v bodoch.</td>
	 * <td>Veľkosť písma v bodoch, napríklad: {@code srg"14"}, {@code 
	 * num24}, {@code num12.5}…</td></tr>
	 * 
	 * <tr><th colspan="2">Tvar atribútu</th><th>Význam
	 * atribútu</th></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Prečiarknuté"}, {@code 
	 * srg"Strikethrough"}, {@code srg"Strike"}…</td><td>Nastaví príznak
	 * prečiarknutého písma.</td></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Podčiarknuté"}, {@code 
	 * srg"Underline"}</td><td>Nastaví príznak podčiarknutého
	 * písma.</td></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Tučné"}, {@code srg"Silné"}, {@code 
	 * srg"Bold"}, {@code srg"Strong"}</td><td>Nastaví príznak tučného
	 * rezu písma.</td></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Kurzíva"}, {@code srg"Šikmé"},
	 * {@code srg"Italic"}, {@code srg"Oblique"}</td><td>Nastaví príznak
	 * rezu kurzívy písma.</td></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Horný index"}, {@code srg"Horné"},
	 * {@code srg"Superscript"}, {@code srg"Super"}…</td><td>Nastaví
	 * príznak horného indexu písma.</td></tr>
	 * 
	 * <tr><td colspan="2">{@code srg"Dolný index"}, {@code srg"Dolné"},
	 * {@code srg"Subscript"}, {@code srg"Sub"}…</td><td>Nastaví príznak
	 * dolného indexu písma.</td></tr>
	 * 
	 * </table>
	 * 
	 * <p><b>Príklady použitia:</b></p>
	 * 
	 * <pre CLASS="example">
		{@link Písmo Písmo} písmo = {@code kwdnew} {@code currPísmo}({@code srg"Názov"}, {@code srg"Cambria"}, {@code srg"Veľkosť"}, {@code num22});
		{@link GRobot#písmo(java.awt.Font) písmo}(písmo);
		{@link GRobot#text(String) text}({@code srg"Text písmom Cambria veľkého 22 bodov."});
		</pre>
	 * 
	 * <p><image>novyKonstruktorPisma1.png<alt/></image>Výsledok vyššieho
	 * príkladu použitia; veľkosť bola spracovaná vo forme celého čísla.</p>
	 * 
	 * <p> </p>
	 * 
	 * <pre CLASS="example">
		{@link GRobot#písmo(java.awt.Font) písmo}({@code kwdnew} {@code currPísmo}({@code srg"Názov"}, {@code srg"Times New Roman"}, {@code srg"Veľkosť"}, {@code srg"12"}, {@code srg"Dolný index"}));
		{@link GRobot#text(String) text}({@code srg"Dolný index „Times New Roman“, 12 bodov."});
		</pre>
	 * 
	 * <p><image>novyKonstruktorPisma2.png<alt/></image>Výsledok vyššieho
	 * príkladu použitia; Veľkosť bola spracovaná vo forme reťazca
	 * a dolný index je uvedený ako samostatný atribút.</p>
	 * 
	 * <p> </p>
	 * 
	 * <pre CLASS="example">
		{@link Svet Svet}.{@link Svet#písmo(java.awt.Font) písmo}({@code kwdnew} {@code currPísmo}({@code srg"Názov"}, {@code srg"Arial"}, {@code srg"Štýl"}, {@code srg"Podčiarknutá kurzíva"}, {@code srg"Veľkosť"}, {@code num22.5}));
		{@link Svet Svet}.{@link Svet#vypíšRiadok(Object...) vypíšRiadok}({@code srg"Konzola podčiarknutou kurzívou Arial 22.5 boda."});
		</pre>
	 * 
	 * <p><image>novyKonstruktorPisma3.png<alt/></image>Výsledok vyššieho
	 * príkladu použitia; Veľkosť bola spracovaná vo forme reálneho
	 * čísla a štýl bol určený hodnotou atribútu, ktorá smie obsahovať
	 * viac príznakov naraz.</p>
	 * 
	 * <p> </p>
	 * 
	 * <p class="attention"><b>Upozornenie:</b> Tento konštruktor je
	 * najliberálnejší. Je schopný prijať a spracovať prakticky akékoľvek
	 * údaje rozdelené na dostatočne malé jednovýznamové celky. Cenou za
	 * to je mierne zvýšenie jeho výpočtovej náročnosti. Odporúčame ho
	 * používať buď na jednorazové vytvorenie písma, alebo napríklad na
	 * spracovanie „surových“ údajov prijatých z nejakého externého
	 * zdroja…</p>
	 * 
	 * @param atribúty séria atribútov podľa opisu
	 */
	public Písmo(Object... atribúty)
	{
		super(zostavAtribúty(atribúty));
	}

	// Zostaví mapu atribútov
	private static HashMap<TextAttribute, Object>
		zostavAtribúty(Object... atribúty)
	{
		HashMap<TextAttribute, Object> mapa = new HashMap<>();
		int hranica = atribúty.length;
		for (int i = 0; i < hranica; ++i)
		{
			if (atribúty[i] instanceof TextAttribute)
			{
				if (i + 1 < hranica)
				{
					if (TextAttribute.WEIGHT ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.WIDTH ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.POSTURE ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.SIZE ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.JUSTIFICATION ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.TRACKING ==
							(TextAttribute)atribúty[i])
					{
						Float číslo = 0.0F;

						if (TextAttribute.WEIGHT ==
							(TextAttribute)atribúty[i])
							číslo = TextAttribute.WEIGHT_REGULAR;
						else if (TextAttribute.WIDTH ==
							(TextAttribute)atribúty[i])
							číslo = TextAttribute.WIDTH_REGULAR;
						else if (TextAttribute.POSTURE ==
							(TextAttribute)atribúty[i])
							číslo = TextAttribute.POSTURE_REGULAR;
						else if (TextAttribute.SIZE ==
							(TextAttribute)atribúty[i])
							číslo = 12.0F;
						else if (TextAttribute.JUSTIFICATION ==
							(TextAttribute)atribúty[i])
							číslo = TextAttribute.JUSTIFICATION_FULL;

						if (null == atribúty[i + 1]) ; else
						if (atribúty[i + 1] instanceof Number)
							číslo = ((Number)atribúty[i + 1]).
								floatValue();
						else try
						{
							číslo = Float.valueOf(
								atribúty[i + 1].toString());
						}
						catch (Exception e)
						{
							GRobotException.vypíšChybovéHlásenia(e/*, false*/);
						}

						mapa.put((TextAttribute)atribúty[i], číslo);
					}
					else if (TextAttribute.SUPERSCRIPT ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.UNDERLINE ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.BIDI_EMBEDDING ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.INPUT_METHOD_UNDERLINE ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.KERNING ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.LIGATURES ==
							(TextAttribute)atribúty[i])
					{
						Integer číslo = 0;

						if (TextAttribute.UNDERLINE ==
								(TextAttribute)atribúty[i] ||
							TextAttribute.INPUT_METHOD_UNDERLINE ==
								(TextAttribute)atribúty[i])
							číslo = -1;

						if (null == atribúty[i + 1]) ; else
						if (atribúty[i + 1] instanceof Number)
							číslo = ((Number)atribúty[i + 1]).
								intValue();
						else try
						{
							číslo = Integer.valueOf(
								atribúty[i + 1].toString());
						}
						catch (Exception e)
						{
							GRobotException.vypíšChybovéHlásenia(e/*, false*/);
						}

						mapa.put((TextAttribute)atribúty[i], číslo);
					}
					else if (TextAttribute.STRIKETHROUGH ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.RUN_DIRECTION ==
							(TextAttribute)atribúty[i] ||
						TextAttribute.SWAP_COLORS ==
							(TextAttribute)atribúty[i])
					{
						Boolean hodnota = false;

						if (TextAttribute.RUN_DIRECTION ==
							(TextAttribute)atribúty[i])
							hodnota = null;

						if (null == atribúty[i + 1]) ; else
						if (atribúty[i + 1] instanceof Boolean)
							hodnota = (Boolean)atribúty[i + 1];
						else try
						{
							hodnota = Boolean.valueOf(
								atribúty[i + 1].toString());
						}
						catch (Exception e)
						{
							GRobotException.vypíšChybovéHlásenia(e/*, false*/);
						}

						mapa.put((TextAttribute)atribúty[i], hodnota);
					}
					else
						mapa.put((TextAttribute)atribúty[i],
							atribúty[i + 1]);
				}

				++i;
			}
			else if (atribúty[i] instanceof String)
			{
				String atribút = ((String)atribúty[i]).toLowerCase();


				// Od verzie 1.84 metóda „odrezáva“ reťazce „font “
				// a „font“ zo začiatku atribútu a reťazce „ písma“,
				// „písma“, „ pisma“ a „pisma“ z konca atribútu, čím
				// mierne rozširuje rozsah akceptovaných atribútov.
				if (atribút.startsWith("font "))
					atribút = atribút.substring(5);
				else if (atribút.startsWith("font"))
					atribút = atribút.substring(4);

				if (atribút.endsWith(" písma") ||
					atribút.endsWith(" pisma"))
					atribút = atribút.substring(0, atribút.length() - 6);
				else if (atribút.endsWith("písma") ||
					atribút.endsWith("pisma"))
					atribút = atribút.substring(0, atribút.length() - 5);


				if (atribút.equals("názov") || atribút.equals("nazov") ||
					atribút.equals("rodina") || atribút.equals("name") ||
					atribút.equals("family") ||
					atribút.equals("familyname") ||
					atribút.equals("family name")||
					atribút.equals("face") ||
					atribút.equals("facename") ||
					atribút.equals("face name"))
				{
					if (i + 1 < hranica)
						mapa.put(TextAttribute.FAMILY, atribúty[i + 1]);

					++i;
				}
				else if (atribút.equals("štýl") || atribút.equals("styl") ||
					atribút.equals("rez") || atribút.equals("style") ||
					atribút.equals("typeface") ||
					atribút.equals("type face"))
				{
					if (i + 1 < hranica)
					{
						String štýl = ((String)atribúty[i + 1]).toLowerCase();

						if (-1 != štýl.indexOf("horn") ||
							-1 != štýl.indexOf("sup"))
							mapa.put(TextAttribute.SUPERSCRIPT,
								TextAttribute.SUPERSCRIPT_SUPER);

						if (-1 != štýl.indexOf("doln") ||
							-1 != štýl.indexOf("sub"))
							mapa.put(TextAttribute.SUPERSCRIPT,
								TextAttribute.SUPERSCRIPT_SUB);

						if (-1 != štýl.indexOf("prečiarknut") ||
							-1 != štýl.indexOf("preciarknut") ||
							-1 != štýl.indexOf("strike"))
							mapa.put(TextAttribute.STRIKETHROUGH,
								TextAttribute.STRIKETHROUGH_ON);

						if (-1 != štýl.indexOf("podčiarknut") ||
							-1 != štýl.indexOf("podciarknut") ||
							-1 != štýl.indexOf("underline"))
							mapa.put(TextAttribute.UNDERLINE,
								TextAttribute.UNDERLINE_ON);

						if (-1 != štýl.indexOf("tučn") ||
							-1 != štýl.indexOf("tucn") ||
							-1 != štýl.indexOf("siln") ||
							-1 != štýl.indexOf("bold") ||
							-1 != štýl.indexOf("strong"))
							mapa.put(TextAttribute.WEIGHT,
								TextAttribute.WEIGHT_BOLD);

						if (-1 != štýl.indexOf("kurzíva") ||
							-1 != štýl.indexOf("kurziva") ||
							-1 != štýl.indexOf("šikm") ||
							-1 != štýl.indexOf("sikm") ||
							-1 != štýl.indexOf("italic") ||
							-1 != štýl.indexOf("oblique"))
							mapa.put(TextAttribute.POSTURE,
								TextAttribute.POSTURE_OBLIQUE);
					}

					++i;
				}
				else if (atribút.equals("veľkosť") ||
					atribút.equals("velkost") || atribút.equals("size"))
				{
					if (i + 1 < hranica)
					{
						Float veľkosť = 12.0F;
						if (null == atribúty[i + 1]) ; else
						if (atribúty[i + 1] instanceof Number)
							veľkosť = ((Number)atribúty[i + 1]).
								floatValue();
						else try
						{
							veľkosť = Float.valueOf(
								atribúty[i + 1].toString());
						}
						catch (Exception e)
						{
							GRobotException.vypíšChybovéHlásenia(e/*, false*/);
						}

						mapa.put(TextAttribute.SIZE, veľkosť);
					}

					++i;
				}
				else if (atribút.equals("prečiarknuté") ||
					atribút.equals("preciarknute") ||
					atribút.equals("prečiarknutá") ||
					atribút.equals("preciarknuta") ||
					atribút.equals("prečiarknutý") ||
					atribút.equals("preciarknuty") ||
					atribút.equals("strike") ||
					atribút.equals("strikethrough") ||
					atribút.equals("strike through"))
					mapa.put(TextAttribute.STRIKETHROUGH,
						TextAttribute.STRIKETHROUGH_ON);
				else if (atribút.equals("podčiarknuté") ||
					atribút.equals("podciarknute") ||
					atribút.equals("podčiarknutá") ||
					atribút.equals("podciarknuta") ||
					atribút.equals("podčiarknutý") ||
					atribút.equals("podciarknuty") ||
					atribút.equals("underline"))
					mapa.put(TextAttribute.UNDERLINE,
						TextAttribute.UNDERLINE_ON);
				else if (atribút.equals("tučné") ||
					atribút.equals("tucne") || atribút.equals("tučná") ||
					atribút.equals("tucna") || atribút.equals("silné") ||
					atribút.equals("silne") || atribút.equals("silná") ||
					atribút.equals("silna") || atribút.equals("silný") ||
					atribút.equals("silny") || atribút.equals("bold")||
					atribút.equals("strong"))
					mapa.put(TextAttribute.WEIGHT,
						TextAttribute.WEIGHT_BOLD);
				else if (atribút.equals("kurzíva") ||
					atribút.equals("kurziva") || atribút.equals("šikmé") ||
					atribút.equals("sikme") || atribút.equals("šikmá") ||
					atribút.equals("sikma") || atribút.equals("šikmý") ||
					atribút.equals("sikmy") || atribút.equals("italic") ||
					atribút.equals("oblique"))
					mapa.put(TextAttribute.POSTURE,
						TextAttribute.POSTURE_OBLIQUE);
				else if (atribút.equals("horný") ||
					atribút.equals("horny") || atribút.equals("horné") ||
					atribút.equals("horne") || atribút.equals("horná") ||
					atribút.equals("horna") ||
					atribút.equals("hornýindex") ||
					atribút.equals("hornyindex") ||
					atribút.equals("horný index") ||
					atribút.equals("horny index") ||
					atribút.equals("sup") || atribút.equals("super") ||
					atribút.equals("superscript") ||
					atribút.equals("super script"))
					mapa.put(TextAttribute.SUPERSCRIPT,
						TextAttribute.SUPERSCRIPT_SUPER);
				else if (atribút.equals("dolný") ||
					atribút.equals("dolny") || atribút.equals("dolné") ||
					atribút.equals("dolne") || atribút.equals("dolná") ||
					atribút.equals("dolna") ||
					atribút.equals("dolnýindex") ||
					atribút.equals("dolnyindex") ||
					atribút.equals("dolný index") ||
					atribút.equals("dolny index") ||
					atribút.equals("sub") || atribút.equals("subscript") ||
					atribút.equals("sub script"))
					mapa.put(TextAttribute.SUPERSCRIPT,
						TextAttribute.SUPERSCRIPT_SUB);
			}
		}
		return mapa;
	}

	/**
	 * <p>Vráti veľkosť písma v bodoch. Táto veľkosť zodpovedá približne
	 * 1/72 palca.</p>
	 * 
	 * @return veľkosť písma v bodoch
	 */
	public float veľkosť() { return getSize2D(); }

	/** <p><a class="alias"></a> Alias pre {@link #veľkosť() veľkosť}.</p> */
	public float velkost() { return getSize2D(); }

	/**
	 * <p>Overí, či bolo písmo vytvorené so štýlom {@link #OBYČAJNÉ OBYČAJNÉ}
	 * alebo {@link #NORMÁLNE NORMÁLNE}.</p>
	 * 
	 * @return vráti {@code valtrue} ak bolo písmo vytvorené so štýlom
	 *     {@link #OBYČAJNÉ OBYČAJNÉ} alebo {@link #NORMÁLNE NORMÁLNE},
	 *     inak vráti {@code valfalse}
	 */
	public boolean obyčajné() { return isPlain(); }

	/** <p><a class="alias"></a> Alias pre {@link #obyčajné() obyčajné}.</p> */
	public boolean obycajne() { return isPlain(); }

	/** <p><a class="alias"></a> Alias pre {@link #obyčajné() obyčajné}.</p> */
	public boolean normálne() { return isPlain(); }

	/** <p><a class="alias"></a> Alias pre {@link #obyčajné() obyčajné}.</p> */
	public boolean normalne() { return isPlain(); }

	/**
	 * <p>Overí, či bolo písmo vytvorené so štýlom {@link #TUČNÉ TUČNÉ}
	 * alebo {@link #SILNÉ SILNÉ}.</p>
	 * 
	 * @return vráti {@code valtrue} ak bolo písmo vytvorené so štýlom
	 *     {@link #TUČNÉ TUČNÉ} alebo {@link #SILNÉ SILNÉ},
	 *     inak vráti {@code valfalse}
	 */
	public boolean tučné() { return isBold(); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean tucne() { return isBold(); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean silné() { return isBold(); }

	/** <p><a class="alias"></a> Alias pre {@link #tučné() tučné}.</p> */
	public boolean silne() { return isBold(); }

	/**
	 * <p>Overí, či bolo písmo vytvorené so štýlom {@link #KURZÍVA KURZÍVA}
	 * alebo {@link #ŠIKMÉ ŠIKMÉ}.</p>
	 * 
	 * @return vráti {@code valtrue} ak bolo písmo vytvorené so štýlom
	 *     {@link #KURZÍVA KURZÍVA} alebo {@link #ŠIKMÉ ŠIKMÉ},
	 *     inak vráti {@code valfalse}
	 */
	public boolean kurzíva() { return isItalic(); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean kurziva() { return isItalic(); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean šikmé() { return isItalic(); }

	/** <p><a class="alias"></a> Alias pre {@link #kurzíva() kurzíva}.</p> */
	public boolean sikme() { return isItalic(); }

	/**
	 * <p>Vráti názov tohto písma. Konkrétne ide o názov rodiny písiem (angl.
	 * font family name), čo môže byť napríklad Helvetica, Arial, Verdana
	 * a podobne.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Ešte rozlišujeme názov rezu písma
	 * (angl. font face name), čo môže byť napríklad Arial Bold, Verdana
	 * Italic a podobne.</p>
	 * 
	 * @return reťazec reprezentujúci názov písma (konkrétne rodiny
	 *     písiem)
	 */
	public String názov() { return getFamily(); }

	/** <p><a class="alias"></a> Alias pre {@link #názov() názov}.</p> */
	public String nazov() { return getFamily(); }


	/**
	 * <p>Prečíta záznam o tomto písme zo zadaného konfiguračného súboru
	 * a vráti inštanciu {@linkplain Písmo písma}, ktorá sa zhoduje so
	 * záznamom v súbore. Táto metóda použije aktuálnu inštanciu ako
	 * predvolenú hodnotu a v prípade potreby vytvorí novú inštanciu
	 * písma. Ak je záznam písma v súbore zhodný s týmto písmom, tak
	 * metóda novú inštanciu nevytvára. Namiesto toho vráti hodnotu
	 * tejto (to jest aktuálnej) inštancie (čiže samú seba –
	 * {@code valthis}). Súbor musí byť otvorený na čítanie.</p>
	 * 
	 * <p>Metóda je používaná {@linkplain Svet#použiKonfiguráciu(String)
	 * automatickou konfiguráciou} sveta.</p>
	 * 
	 * @param súbor inštancia triedy {@linkplain Súbor súbor}
	 *     otvorená na čítanie
	 * @return inštancia, ktorá sa zhoduje so záznamom v súbore
	 * 
	 * @see ObsluhaUdalostí#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 * @see Svet#čítajKonfiguráciuSveta()
	 * @see #uložDoSúboru(Súbor)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	@SuppressWarnings("unchecked")
	public Písmo čítajZoSúboru(Súbor súbor) throws IOException
	{
		String mennýPriestor = súbor.aktívnaSekcia.mennýPriestorVlastností;

		try
		{
			if (null == mennýPriestor)
				súbor.aktívnaSekcia.mennýPriestorVlastností = "písmo";
			else
				súbor.aktívnaSekcia.mennýPriestorVlastností =
					mennýPriestor + ".písmo";

			// int veľkosť = súbor.čítajVlastnosť("veľkosť",
			// 	Long.valueOf(getSize())).intValue();
			float veľkosť = súbor.čítajVlastnosť("veľkosť",
				// Double.valueOf(getSize2D())).floatValue();
				getSize2D());

			String štýlStr = súbor.čítajVlastnosť(
				"štýl", "normálny").toLowerCase();
			int štýl = Písmo.OBYČAJNÉ;
			if (null != štýlStr)
			{
				if (-1 != štýlStr.indexOf("tučné") ||
					-1 != štýlStr.indexOf("tucne") ||
					-1 != štýlStr.indexOf("tučná") ||
					-1 != štýlStr.indexOf("tucna"))
					štýl |= Písmo.TUČNÉ;
				if (-1 != štýlStr.indexOf("kurzíva") ||
					-1 != štýlStr.indexOf("kurziva"))
					štýl |= Písmo.KURZÍVA;
			}

			String názov = súbor.čítajVlastnosť("názov", getFamily());

			Map<TextAttribute, Object> atribúty =
				(Map<TextAttribute, Object>)getAttributes();
			Object hodnota;

			Integer indexFontu = null;
			hodnota = atribúty.get(TextAttribute.SUPERSCRIPT);
			if (null != hodnota && hodnota instanceof Number)
				indexFontu = ((Number)hodnota).intValue();
			Integer indexSúboru = null;
			hodnota = súbor.čítajVlastnosť("index", (Long)null);
			if (null != hodnota) indexSúboru = ((Long)hodnota).intValue();

			Boolean prečiarknutieFontu = null;
			hodnota = atribúty.get(TextAttribute.STRIKETHROUGH);
			if (null != hodnota && hodnota instanceof Boolean)
				prečiarknutieFontu = (Boolean)hodnota;
			Boolean prečiarknutieSúboru = súbor.čítajVlastnosť(
				"prečiarknutie", (Boolean)null);

			Integer podčiarknutieFontu = null;
			hodnota = atribúty.get(TextAttribute.UNDERLINE);
			if (null != hodnota && hodnota instanceof Number)
				podčiarknutieFontu = ((Number)hodnota).intValue();
			Integer podčiarknutieSúboru = null;
			hodnota = súbor.čítajVlastnosť("podčiarknutie", (Long)null);
			if (null != hodnota)
				podčiarknutieSúboru = ((Long)hodnota).intValue();

			if (indexFontu == indexSúboru &&
				prečiarknutieFontu == prečiarknutieSúboru &&
				podčiarknutieFontu == podčiarknutieSúboru)
			{
				if (!názov.equalsIgnoreCase(getFamily()) ||
					veľkosť != getSize2D() || štýl != getStyle())
					return new Písmo(názov, štýl, veľkosť);

				return this;
			}

			atribúty.remove(TextAttribute.SUPERSCRIPT);
			atribúty.remove(TextAttribute.STRIKETHROUGH);
			atribúty.remove(TextAttribute.UNDERLINE);

			if (!názov.equalsIgnoreCase(getFamily()))
				atribúty.put(TextAttribute.FAMILY, názov);

			if (veľkosť != getSize2D())
				atribúty.put(TextAttribute.SIZE, veľkosť);

			if (štýl != getStyle())
			{
				atribúty.put(TextAttribute.WEIGHT,
					0 == (štýl & Písmo.TUČNÉ) ?
					TextAttribute.WEIGHT_REGULAR :
					TextAttribute.WEIGHT_BOLD);
				atribúty.put(TextAttribute.POSTURE,
					0 == (štýl & Písmo.KURZÍVA) ?
					TextAttribute.POSTURE_REGULAR :
					TextAttribute.POSTURE_OBLIQUE);
			}

			if (null != indexSúboru)
				atribúty.put(TextAttribute.SUPERSCRIPT, indexSúboru);

			if (null != prečiarknutieSúboru)
				atribúty.put(TextAttribute.STRIKETHROUGH,
					prečiarknutieSúboru);

			if (null != podčiarknutieSúboru)
				atribúty.put(TextAttribute.UNDERLINE, podčiarknutieSúboru);

			return new Písmo(atribúty);
		}
		finally
		{
			súbor.aktívnaSekcia.mennýPriestorVlastností = mennýPriestor;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor) čítajZoSúboru}.</p> */
	public Pismo citajZoSuboru(Súbor súbor) throws IOException
	{ return new Pismo(čítajZoSúboru(súbor)); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor) čítajZoSúboru}.</p> */
	public Písmo prečítajZoSúboru(Súbor súbor) throws IOException
	{ return čítajZoSúboru(súbor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor) čítajZoSúboru}.</p> */
	public Pismo precitajZoSuboru(Súbor súbor) throws IOException
	{ return new Pismo(čítajZoSúboru(súbor)); }

	/**
	 * <p>Uloží záznam o tomto písme do zadaného konfiguračného súboru.
	 * Súbor musí byť otvorený na zápis. Metóda je používaná
	 * {@linkplain Svet#použiKonfiguráciu(String) automatickou
	 * konfiguráciou} sveta.</p>
	 * 
	 * @param súbor inštancia triedy {@linkplain Súbor súbor}
	 *     otvorená na zápis
	 * 
	 * @see ObsluhaUdalostí#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 * @see Svet#čítajKonfiguráciuSveta()
	 * @see #čítajZoSúboru(Súbor)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void uložDoSúboru(Súbor súbor) throws IOException
	{
		String mennýPriestor = súbor.aktívnaSekcia.mennýPriestorVlastností;

		try
		{
			if (null == mennýPriestor)
				súbor.aktívnaSekcia.mennýPriestorVlastností = "písmo";
			else
				súbor.aktívnaSekcia.mennýPriestorVlastností =
					mennýPriestor + ".písmo";

			súbor.zapíšVlastnosť("názov", getFamily());
			// súbor.zapíšVlastnosť("veľkosť", Long.valueOf(getSize()));
			súbor.zapíšVlastnosť("veľkosť", Double.valueOf(getSize2D()));
			if (isPlain())
				súbor.zapíšVlastnosť("štýl", "normálny");
			else if (isBold() && isItalic())
				súbor.zapíšVlastnosť("štýl", "tučná kurzíva");
			else if (isBold())
				súbor.zapíšVlastnosť("štýl", "tučné");
			else if (isItalic())
				súbor.zapíšVlastnosť("štýl", "kurzíva");
			else súbor.zapíšVlastnosť("štýl", null);

			// Ďalšie vlastnosti – index, prečiarknutie a podčiarknutie
			Map<TextAttribute, ?> atribúty = getAttributes();
			Object hodnota;

			hodnota = atribúty.get(TextAttribute.SUPERSCRIPT);
			if (null != hodnota && hodnota instanceof Number)
				súbor.zapíšVlastnosť("index",
					((Number)hodnota).intValue());
			else súbor.vymažVlastnosť("index");

			hodnota = atribúty.get(TextAttribute.STRIKETHROUGH);
			if (null != hodnota && hodnota instanceof Boolean)
				súbor.zapíšVlastnosť("prečiarknutie",
					(Boolean)hodnota);
			else súbor.vymažVlastnosť("prečiarknutie");

			hodnota = atribúty.get(TextAttribute.UNDERLINE);
			if (null != hodnota && hodnota instanceof Number)
				súbor.zapíšVlastnosť("podčiarknutie",
					((Number)hodnota).intValue());
			else súbor.vymažVlastnosť("podčiarknutie");
		}
		finally
		{
			súbor.aktívnaSekcia.mennýPriestorVlastností = mennýPriestor;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor) uložDoSúboru}.</p> */
	public void ulozDoSuboru(Súbor súbor) throws IOException
	{ uložDoSúboru(súbor); }


	/**
	 * <p>Prečíta záznam o písme zo zadaného konfiguračného súboru a vráti
	 * inštanciu {@linkplain Písmo písma} vytvorenú podľa záznamu v súbore.
	 * Druhý argument určuje predvolenú hodnotu. V prípade, že je záznam
	 * v súbore zhodný s predvolenou hodnotou, tak táto metóda nevytvorí
	 * novú inštanciu, ale vráti inštanciu predvolenej hodnoty.
	 * Súbor musí byť otvorený na čítanie.</p>
	 * 
	 * <p>Metóda je používaná {@linkplain Svet#použiKonfiguráciu(String)
	 * automatickou konfiguráciou} sveta.</p>
	 * 
	 * @param súbor inštancia triedy {@linkplain Súbor súbor}
	 *     otvorená na čítanie
	 * @return inštancia písma vytvorená podľa záznamu v súbore alebo
	 *     predvolená hodnota
	 * 
	 * @see ObsluhaUdalostí#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 * @see Svet#čítajKonfiguráciuSveta()
	 * @see #uložDoSúboru(Súbor)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	@SuppressWarnings("unchecked")
	public static Písmo čítajZoSúboru(Súbor súbor, Písmo písmo)
		throws IOException
	{
		String mennýPriestor = súbor.aktívnaSekcia.mennýPriestorVlastností;

		try
		{
			if (súbor.vlastnosťJestvuje("písmo")) return null;

			if (null == mennýPriestor)
				súbor.aktívnaSekcia.mennýPriestorVlastností = "písmo";
			else
				súbor.aktívnaSekcia.mennýPriestorVlastností =
					mennýPriestor + ".písmo";

			// int veľkosť = súbor.čítajVlastnosť("veľkosť",
			// 	Long.valueOf(null == písmo ? new Long(16) :
			// 		písmo.getSize())).intValue();
			float veľkosť = súbor.čítajVlastnosť("veľkosť",
				// Double.valueOf(null == písmo ? new Float(16.0) :
				// 	písmo.getSize2D())).floatValue();
				null == písmo ? new Float(16.0) : písmo.getSize2D());

			String štýlStr = súbor.čítajVlastnosť(
				"štýl", "normálny").toLowerCase();
			int štýl = Písmo.OBYČAJNÉ;
			if (null != štýlStr)
			{
				if (-1 != štýlStr.indexOf("tučné") ||
					-1 != štýlStr.indexOf("tucne") ||
					-1 != štýlStr.indexOf("tučná") ||
					-1 != štýlStr.indexOf("tucna"))
					štýl |= Písmo.TUČNÉ;
				if (-1 != štýlStr.indexOf("kurzíva") ||
					-1 != štýlStr.indexOf("kurziva"))
					štýl |= Písmo.KURZÍVA;
			}

			String názov = súbor.čítajVlastnosť("názov",
				null == písmo ? "Helvetica" : písmo.getFamily());

			Map<TextAttribute, Object> atribúty =
				(Map<TextAttribute, Object>)písmo.getAttributes();
			Object hodnota;

			Integer indexFontu = null;
			hodnota = atribúty.get(TextAttribute.SUPERSCRIPT);
			if (null != hodnota && hodnota instanceof Number)
				indexFontu = ((Number)hodnota).intValue();
			Integer indexSúboru = null;
			hodnota = súbor.čítajVlastnosť("index", (Long)null);
			if (null != hodnota) indexSúboru = ((Long)hodnota).intValue();

			Boolean prečiarknutieFontu = null;
			hodnota = atribúty.get(TextAttribute.STRIKETHROUGH);
			if (null != hodnota && hodnota instanceof Boolean)
				prečiarknutieFontu = (Boolean)hodnota;
			Boolean prečiarknutieSúboru = súbor.čítajVlastnosť(
				"prečiarknutie", (Boolean)null);

			Integer podčiarknutieFontu = null;
			hodnota = atribúty.get(TextAttribute.UNDERLINE);
			if (null != hodnota && hodnota instanceof Number)
				podčiarknutieFontu = ((Number)hodnota).intValue();
			Integer podčiarknutieSúboru = null;
			hodnota = súbor.čítajVlastnosť("podčiarknutie", (Long)null);
			if (null != hodnota)
				podčiarknutieSúboru = ((Long)hodnota).intValue();

			if (indexFontu == indexSúboru &&
				prečiarknutieFontu == prečiarknutieSúboru &&
				podčiarknutieFontu == podčiarknutieSúboru)
			{
				if (null == písmo || !názov.equalsIgnoreCase(
					písmo.getFamily()) || veľkosť != písmo.getSize2D() ||
					štýl != písmo.getStyle())
					return new Písmo(názov, štýl, veľkosť);

				return písmo;
			}

			atribúty.remove(TextAttribute.SUPERSCRIPT);
			atribúty.remove(TextAttribute.STRIKETHROUGH);
			atribúty.remove(TextAttribute.UNDERLINE);

			if (!názov.equalsIgnoreCase(písmo.getFamily()))
				atribúty.put(TextAttribute.FAMILY, názov);

			if (veľkosť != písmo.getSize2D())
				atribúty.put(TextAttribute.SIZE, veľkosť);

			if (štýl != písmo.getStyle())
			{
				atribúty.put(TextAttribute.WEIGHT,
					0 == (štýl & Písmo.TUČNÉ) ?
					TextAttribute.WEIGHT_REGULAR :
					TextAttribute.WEIGHT_BOLD);
				atribúty.put(TextAttribute.POSTURE,
					0 == (štýl & Písmo.KURZÍVA) ?
					TextAttribute.POSTURE_REGULAR :
					TextAttribute.POSTURE_OBLIQUE);
			}

			if (null != indexSúboru)
				atribúty.put(TextAttribute.SUPERSCRIPT, indexSúboru);

			if (null != prečiarknutieSúboru)
				atribúty.put(TextAttribute.STRIKETHROUGH,
					prečiarknutieSúboru);

			if (null != podčiarknutieSúboru)
				atribúty.put(TextAttribute.UNDERLINE, podčiarknutieSúboru);

			return new Písmo(atribúty);
		}
		finally
		{
			súbor.aktívnaSekcia.mennýPriestorVlastností = mennýPriestor;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, Písmo) čítajZoSúboru}.</p> */
	public static Pismo citajZoSuboru(Súbor súbor, Písmo písmo)
		throws IOException
	{ return new Pismo(čítajZoSúboru(súbor, písmo)); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, Písmo) čítajZoSúboru}.</p> */
	public static Písmo prečítajZoSúboru(Súbor súbor, Písmo písmo)
		throws IOException
	{ return čítajZoSúboru(súbor, písmo); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, Písmo) čítajZoSúboru}.</p> */
	public static Pismo precitajZoSuboru(Súbor súbor, Písmo písmo)
		throws IOException
	{ return new Pismo(čítajZoSúboru(súbor, písmo)); }

	/**
	 * <p>Uloží záznam o zadanom písme do zadaného konfiguračného súboru.
	 * Súbor musí byť otvorený na zápis. Metóda je používaná
	 * {@linkplain Svet#použiKonfiguráciu(String) automatickou
	 * konfiguráciou} sveta.</p>
	 * 
	 * @param súbor inštancia triedy {@linkplain Súbor súbor}
	 *     otvorená na zápis
	 * 
	 * @see ObsluhaUdalostí#konfiguráciaZmenená()
	 * @see Svet#použiKonfiguráciu()
	 * @see Svet#čítajKonfiguráciuSveta()
	 * @see #čítajZoSúboru(Súbor)
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public static void uložDoSúboru(Súbor súbor, Písmo písmo)
		throws IOException
	{
		String mennýPriestor = súbor.aktívnaSekcia.mennýPriestorVlastností;

		try
		{
			if (null == písmo)
			{
				súbor.zapíšVlastnosť("písmo", null);

				if (null == mennýPriestor)
					súbor.aktívnaSekcia.mennýPriestorVlastností = "písmo";
				else
					súbor.aktívnaSekcia.mennýPriestorVlastností =
						mennýPriestor + ".písmo";

				súbor.vymažVlastnosť("názov");
				súbor.vymažVlastnosť("veľkosť");
				súbor.vymažVlastnosť("štýl");
				súbor.vymažVlastnosť("index");
				súbor.vymažVlastnosť("prečiarknutie");
				súbor.vymažVlastnosť("podčiarknutie");
			}
			else
			{
				súbor.vymažVlastnosť("písmo");

				if (null == mennýPriestor)
					súbor.aktívnaSekcia.mennýPriestorVlastností = "písmo";
				else
					súbor.aktívnaSekcia.mennýPriestorVlastností =
						mennýPriestor + ".písmo";

				súbor.zapíšVlastnosť("názov", písmo.getFamily());
				// súbor.zapíšVlastnosť("veľkosť",
				// 	Long.valueOf(písmo.getSize()));
				súbor.zapíšVlastnosť("veľkosť",
					Double.valueOf(písmo.getSize2D()));
				if (písmo.isPlain())
					súbor.zapíšVlastnosť("štýl", "normálny");
				else if (písmo.isBold() && písmo.isItalic())
					súbor.zapíšVlastnosť("štýl", "tučná kurzíva");
				else if (písmo.isBold())
					súbor.zapíšVlastnosť("štýl", "tučné");
				else if (písmo.isItalic())
					súbor.zapíšVlastnosť("štýl", "kurzíva");
				else súbor.zapíšVlastnosť("štýl", null);

				// Ďalšie vlastnosti – index, prečiarknutie a podčiarknutie
				Map<TextAttribute, ?> atribúty = písmo.getAttributes();
				Object hodnota;

				hodnota = atribúty.get(TextAttribute.SUPERSCRIPT);
				if (null != hodnota && hodnota instanceof Number)
					súbor.zapíšVlastnosť("index",
						((Number)hodnota).intValue());
				else súbor.vymažVlastnosť("index");

				hodnota = atribúty.get(TextAttribute.STRIKETHROUGH);
				if (null != hodnota && hodnota instanceof Boolean)
					súbor.zapíšVlastnosť("prečiarknutie",
						(Boolean)hodnota);
				else súbor.vymažVlastnosť("prečiarknutie");

				hodnota = atribúty.get(TextAttribute.UNDERLINE);
				if (null != hodnota && hodnota instanceof Number)
					súbor.zapíšVlastnosť("podčiarknutie",
						((Number)hodnota).intValue());
				else súbor.vymažVlastnosť("podčiarknutie");
			}
		}
		finally
		{
			súbor.aktívnaSekcia.mennýPriestorVlastností = mennýPriestor;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor, Písmo) uložDoSúboru}.</p> */
	public static void ulozDoSuboru(Súbor súbor, Písmo písmo)
		throws IOException
	{ uložDoSúboru(súbor, písmo); }
}
