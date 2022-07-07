
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

import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

// ------------------- //
//  *** Konštanty ***  //
// ------------------- //

/**
 * <p>Toto rozhranie zhromažďuje všetky konštanty používané v programovacom
 * rámci. Trieda {@link GRobot GRobot} ho implementuje (čo je povedané kvázi
 * obrazne, striktne v súlade s terminológiou Javy; v skutočnosti toto
 * rozhranie nedefinuje žiadne metódy na implementáciu), preto sú všetky
 * konštanty použiteľné priamo (bez odkazu na rozhranie) v každej triede
 * odvodenej od triedy {@link GRobot GRobot}. V prípade potreby môže toto
 * rozhranie implementovať ľubovoľná trieda, čím získa priamy prístup
 * k definovaným konštantám.</p>
 * 
 * <p>Mnohé triedy programovacieho rámca staticky importujú konkrétne
 * konštanty z tohto rozhrania, aby sa dal lepšie udržať prehľad v tom,
 * ktorá trieda používa konkrétne konštanty (napríklad trieda {@link 
 * Spojenie Spojenie} importuje konštanty {@link #PREVZATIE_ÚDAJOV
 * PREVZATIE_ÚDAJOV} a {@link #ODOVZDANIE_ÚDAJOV ODOVZDANIE_ÚDAJOV}).</p>
 * 
 * <p>Statický import je totiž iným (a z pohľadu filozofie jazyka Java
 * možno „správnejším“) spôsobom získania priameho prístupu ku konštantám
 * rozhrania (alebo triedy). V prípade potreby môžu byť v konkrétnej triede
 * staticky importované všetky konštanty tohto rozhrania:</p>
 * 
 * <pre CLASS="example">
	{@code kwdimport} {@code kwdstatic} knižnica.{@code currKonštanty}.*;
	</pre>
 */
public interface Konštanty
{
	// Verzia a autor…

		/** <p>Konštanta majoritnej verzie tohto rámca.</p> */
		public final static int majorVersion = 2;
		/** <p>Konštanta minoritnej verzie tohto rámca.</p> */
		public final static int minorVersion = 15;
		/** <p>Konštanta poznámky verzie tohto rámca.</p> */
		public final static String versionNote = " (2022-07-07)";
		/** <p>Konštanta rozpätia rokov vývoja tejto verzie rámca
			(v podstate na účel výpisu copyrightu…)</p> */
		public final static String years = "2010 – 2022";
		/** <p>Konštanta rozpätia rokov a mesiacov vývoja tejto verzie rámca
			(na účely automatického spracovania dokumentácie…)</p> */
		public final static String yearsMonths = "august 2010 – júl 2022";
		/** <p>Konštanta s menom hlavného (zatiaľ jediného) vývojára tejto
			verzie rámca (v podstate na účel výpisu copyrightu… kto vie, či
			časom pribudnú aj nejakí ďalší…)</p> */
		public final static String mainDeveloper = "Roman Horváth";
		/** <p>Konštanta s menom a titulmi hlavného vývojára tejto verzie
			rámca (na účely automatického spracovania dokumentácie…)</p> */
		public final static String mainDeveloperTitled =
			"Mgr. Ing. Roman Horváth, PhD.";

		/** <p>Úplný reťazec poskytujúci informácie o tejto verzii rámca
			(angl. frameworku).</p> */
		public final static String versionString =
			"Programovací rámec GRobot " + majorVersion + "." +
			Integer.toString(minorVersion + 100).substring(1) +
			versionNote + ", © " + years + ", " + mainDeveloper;


	// Spôsoby kreslenia

		/**
		 * <p>Príznak priameho spôsobu písania textov a kreslenia obrázkov –
		 * nerotuje, necentruje. Nachádza využitie pri metódach {@link 
		 * GRobot#spôsobKreslenia(int) spôsobKreslenia}, {@link 
		 * GRobot#text(String, int) text} alebo {@link GRobot#obrázok(String,
		 * int) obrázok}.</p>
		 */
		public final static int KRESLI_PRIAMO = 0;
		/**
		 * <p>Príznak centrovania textu alebo obrázka na polohu (stred) robota.
		 * Nachádza využitie pri metódach {@link GRobot#spôsobKreslenia(int)
		 * spôsobKreslenia}, {@link GRobot#text(String, int) text} alebo {@link 
		 * GRobot#obrázok(String, int) obrázok}.</p>
		 */
		public final static int KRESLI_NA_STRED = 1;
		/**
		 * <p>Príznak rotovania textu alebo obrázka kolmo na smer robota.
		 * Nachádza využitie pri metódach {@link GRobot#spôsobKreslenia(int)
		 * spôsobKreslenia}, {@link GRobot#text(String, int) text} alebo
		 * {@link GRobot#obrázok(String, int) obrázok}.</p>
		 */
		public final static int KRESLI_ROTOVANÉ = 2;

		/** <p><a class="alias"></a> Alias pre {@link #KRESLI_ROTOVANÉ}.</p> */
		public final static int KRESLI_ROTOVANE = KRESLI_ROTOVANÉ;
		/** <p><a class="alias"></a> Alias pre {@link #KRESLI_NA_STRED}.</p> */
		public final static int KRESLI_CENTROVANÉ = KRESLI_NA_STRED;
		/** <p><a class="alias"></a> Alias pre {@link #KRESLI_CENTROVANÉ}.</p> */
		public final static int KRESLI_CENTROVANE = KRESLI_NA_STRED;
		/** <p><a class="alias"></a> Alias pre {@link #KRESLI_ROTOVANÉ}.</p> */
		public final static int KRESLI_V_SMERE = KRESLI_ROTOVANÉ;


	// Spôsoby ohraničenia robota

		/**
		 * <p>Číselná konštanta, ktorá môže mať v budúcnosti aj iný význam, ale
		 * aktuálne len určuje, že robot nepoužíva žiadny spôsob
		 * {@linkplain GRobot#ohranič(double, double, double, double, int)
		 * ohraničenia}.</p>
		 */
		public final static int ŽIADNE = 0;
		/** <p><a class="alias"></a> Alias pre {@link #ŽIADNE}.</p> */
		public final static int ZIADNE = ŽIADNE;

		/**
		 * <p>Číselná konštanta spôsobu {@linkplain GRobot#ohranič(double,
		 * double, double, double, int) ohraničenia robota}, kedy sa robot po
		 * kontakte s hranicami odrazí naspäť do priestoru vymedzeného
		 * hranicami.</p>
		 */
		public final static int ODRAZ = 1;
		/** <p><a class="alias"></a> Alias pre {@link #ODRAZ}.</p> */
		public final static int ODRAZENIE = ODRAZ;
		/** <p><a class="alias"></a> Alias pre {@link #ODRAZ}.</p> */
		public final static int ODRAZIŤ = ODRAZ;
		/** <p><a class="alias"></a> Alias pre {@link #ODRAZ}.</p> */
		public final static int ODRAZIT = ODRAZ;

		/**
		 * <p>Číselná konštanta spôsobu {@linkplain GRobot#ohranič(double,
		 * double, double, double, int) ohraničenia robota}, kedy sa robot po
		 * kontakte s hranicami kĺže pozdĺž hranice, dokedy nedosiahne bod,
		 * ktorý je najbližšie k cieľovému umiestneniu.</p>
		 */
		public final static int PLOT = 2;
		/** <p><a class="alias"></a> Alias pre {@link #PLOT}.</p> */
		public final static int OPLOTENIE = PLOT;
		/** <p><a class="alias"></a> Alias pre {@link #PLOT}.</p> */
		public final static int OPLOTIŤ = PLOT;
		/** <p><a class="alias"></a> Alias pre {@link #PLOT}.</p> */
		public final static int OPLOTIT = PLOT;

		/**
		 * <p>Číselná konštanta spôsobu {@linkplain GRobot#ohranič(double,
		 * double, double, double, int) ohraničenia robota}, kedy sa robot po
		 * prekročení hranice presunie na protiľahnú stranu ohraničenia
		 * a pokračuje ďalej v rámci vymedzenej plochy.</p>
		 */
		public final static int PRETOČ = 3;
		/** <p><a class="alias"></a> Alias pre {@link #PRETOČ}.</p> */
		public final static int PRETOC = PRETOČ;
		/** <p><a class="alias"></a> Alias pre {@link #PRETOČ}.</p> */
		public final static int PRETOČENIE = PRETOČ;
		/** <p><a class="alias"></a> Alias pre {@link #PRETOČ}.</p> */
		public final static int PRETOCENIE = PRETOČ;
		/** <p><a class="alias"></a> Alias pre {@link #PRETOČ}.</p> */
		public final static int PRETOČIŤ = PRETOČ;
		/** <p><a class="alias"></a> Alias pre {@link #PRETOČ}.</p> */
		public final static int PRETOCIT = PRETOČ;


	// Tlačidlá myši

		/**
		 * <p>Číselná konštanta ľavého tlačidla myši ({@code num1}).
		 * Použiteľná napríklad s metódami {@link ÚdajeUdalostí#tlačidloMyši()
		 * tlačidloMyši()}, {@link ÚdajeUdalostí#tlačidloMyši(int)
		 * tlačidloMyši(ktoré)}, {@link ÚdajeUdalostí#tlačidloMyšiDole(int)
		 * tlačidloMyšiDole(ktoré)} alebo
		 * {@link ÚdajeUdalostí#tlačidloMyšiHore(int)
		 * tlačidloMyšiHore(ktoré)}.</p>
		 */
		public final static int ĽAVÉ = 1;
		/** <p><a class="alias"></a> Alias pre {@link #ĽAVÉ}.</p> */
		public final static int LAVE = ĽAVÉ;
		/**
		 * <p>Číselná konštanta stredného tlačidla myši ({@code num2}).
		 * Použiteľná napríklad s metódami {@link ÚdajeUdalostí#tlačidloMyši()
		 * tlačidloMyši()}, {@link ÚdajeUdalostí#tlačidloMyši(int)
		 * tlačidloMyši(ktoré)}, {@link ÚdajeUdalostí#tlačidloMyšiDole(int)
		 * tlačidloMyšiDole(ktoré)} alebo
		 * {@link ÚdajeUdalostí#tlačidloMyšiHore(int)
		 * tlačidloMyšiHore(ktoré)}.</p>
		 */
		public final static int STREDNÉ = 2;
		/** <p><a class="alias"></a> Alias pre {@link #STREDNÉ}.</p> */
		public final static int STREDNE = STREDNÉ;
		/**
		 * <p>Číselná konštanta pravého tlačidla myši ({@code num3}).
		 * Použiteľná napríklad s metódami {@link ÚdajeUdalostí#tlačidloMyši()
		 * tlačidloMyši()}, {@link ÚdajeUdalostí#tlačidloMyši(int)
		 * tlačidloMyši(ktoré)}, {@link ÚdajeUdalostí#tlačidloMyšiDole(int)
		 * tlačidloMyšiDole(ktoré)} alebo
		 * {@link ÚdajeUdalostí#tlačidloMyšiHore(int)
		 * tlačidloMyšiHore(ktoré)}.</p>
		 */
		public final static int PRAVÉ = 3;
		/** <p><a class="alias"></a> Alias pre {@link #PRAVÉ}.</p> */
		public final static int PRAVE = PRAVÉ;


	// Ukončenie úprav textu

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesom {@code ESC}.</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_ESCAPE = -2;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_ESCAPE SPÔSOB_ESCAPE}.</p> */
		public final static int SPOSOB_ESCAPE = -2;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesom {@code ENTER}.</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_ENTER = 0;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_ENTER SPÔSOB_ENTER}.</p> */
		public final static int SPOSOB_ENTER = 0;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesom {@code TAB} (čo je aliasom kávesu
		 * {@code TABULÁTOR}).</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_TAB = 1;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_TAB SPÔSOB_TAB}.</p> */
		public final static int SPOSOB_TAB = 1;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesom {@code TABULÁTOR} (čo je aliasom
		 * kávesu {@code TAB}).</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_TABULÁTOR = 1;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_TABULÁTOR SPÔSOB_TABULÁTOR}.</p> */
		public final static int SPOSOB_TABULATOR = 1;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesovou kombináciou {@code Shift + TAB}
		 * (resp. {@code Shift + tabulátor}).</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_SHIFT_TAB = -1;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_SHIFT_TAB SPÔSOB_SHIFT_TAB}.</p> */
		public final static int SPOSOB_SHIFT_TAB = -1;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} klávesovou kombináciou
		 * {@code Shift + tabulátor} (resp. {@code Shift + TAB}).</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_SHIFT_TABULÁTOR = -1;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_SHIFT_TABULÁTOR SPÔSOB_SHIFT_TABULÁTOR}.</p> */
		public final static int SPOSOB_SHIFT_TABULATOR = -1;

		/**
		 * <p>Spôsob {@linkplain GRbobot#ukončenieÚpravyTextu(String, int)
		 * ukončenia úprav textu} dekativáciou textového komponentu
		 * (napríklad kliknutím myšou na plátne).</p>
		 * 
		 * @see GRobot#upravText(String)
		 * @see GRobot#upravText(String, double)
		 * @see GRbobot#ukončenieÚpravyTextu(String, int)
		 */
		public final static int SPÔSOB_DEAKTIVÁCIA = 2;

		/** <p><a class="alias"></a> Alias pre {@link #SPÔSOB_DEAKTIVÁCIA SPÔSOB_DEAKTIVÁCIA}.</p> */
		public final static int SPOSOB_DEAKTIVACIA = 2;


	// Sekvencia

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota
		 * {@link GRobot#sekvencia(int, Object, Object, long, long)
		 * sekvencia} a reakciu aktívnej obsluhy udalostí
		 * {@link ObsluhaUdalostí#sekvencia(int, Object, Object,
		 * long, long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave čítania PNG sekvencie obrázkov
		 * metódou {@link Obrázok Obrázok}{@code .}{@link 
		 * Obrázok#čítaj(String) čítaj}{@code (názovSúboru)}.</p>
		 */
		public final static int ČÍTANIE_PNG_SEKVENCIE = 1;

		/** <p><a class="alias"></a> Alias pre {@link #ČÍTANIE_PNG_SEKVENCIE ČÍTANIE_PNG_SEKVENCIE}.</p> */
		public final static int CITANIE_PNG_SEKVENCIE = 1;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota
		 * {@link GRobot#sekvencia(int, Object, Object, long, long)
		 * sekvencia} a reakciu aktívnej obsluhy udalostí
		 * {@link ObsluhaUdalostí#sekvencia(int, Object, Object,
		 * long, long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu v prípade nedostatku voľnej pamäte na prečítanie celej
		 * PNG sekvencie obrázkov. (V takom prípade je všetko, čo bolo doteraz
		 * prečítané uvoľnené a metóda {@link Obrázok Obrázok}{@code .}{@link 
		 * Obrázok#čítaj(String) čítaj}{@code (názovSúboru)} vráti hodnotu
		 * {@code valnull}.)</p>
		 */
		public final static int CHYBA_ČÍTANIA_PNG_SEKVENCIE = -1;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_ČÍTANIA_PNG_SEKVENCIE CHYBA_ČÍTANIA_PNG_SEKVENCIE}.</p> */
		public final static int CHYBA_CITANIA_PNG_SEKVENCIE = -1;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave zápisu PNG sekvencie obrázkov
		 * metódou {@link Obrázok Obrázok}{@code .}{@link 
		 * Obrázok#ulož(String, boolean) ulož}{@code (názovSúboru, prepísať)}
		 * (resp. jej inou verziou).</p>
		 */
		public final static int ZÁPIS_PNG_SEKVENCIE = 2;

		/** <p><a class="alias"></a> Alias pre {@link #ZÁPIS_PNG_SEKVENCIE ZÁPIS_PNG_SEKVENCIE}.</p> */
		public final static int ZAPIS_PNG_SEKVENCIE = 2;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave čítania animovaného obrázka
		 * vo formáte GIF metódou {@link Obrázok Obrázok}{@code .}{@link 
		 * Obrázok#čítaj(String) čítaj}{@code (názovSúboru)}.</p>
		 */
		public final static int ČÍTANIE_GIF_ANIMÁCIE = 3;

		/** <p><a class="alias"></a> Alias pre {@link #ČÍTANIE_GIF_ANIMÁCIE ČÍTANIE_GIF_ANIMÁCIE}.</p> */
		public final static int CITANIE_GIF_ANIMACIE = 3;

		// /**
		//  * REZERVOVANÉ
		//  */
		// public final static int CHYBA_ČÍTANIA_GIF_ANIMÁCIE = -2;
		// 
		// // Alias REZERVOVANÉ
		// public final static int CHYBA_CITANIA_GIF_ANIMACIE = -2;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave zápisu animovaného obrázka
		 * vo formáte GIF metódou {@link Obrázok Obrázok}{@code .}{@link 
		 * Obrázok#ulož(String, boolean) ulož}{@code (názovSúboru, prepísať)}
		 * (resp. jej inou verziou).</p>
		 */
		public final static int ZÁPIS_GIF_ANIMÁCIE = 4;

		/** <p><a class="alias"></a> Alias pre {@link #ZÁPIS_GIF_ANIMÁCIE ZÁPIS_GIF_ANIMÁCIE}.</p> */
		public final static int ZAPIS_GIF_ANIMACIE = 4;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave kopírovania súboru metódou
		 * {@link Súbor Súbor}{@code .}{@link Súbor#kopíruj(String, String,
		 * boolean) kopíruj}{@code (zdroj, cieľ, prepísať)} (resp. niektorou
		 * jej verziou).</p>
		 */
		public final static int KOPÍROVANIE_SÚBOROV = 5;

		/** <p><a class="alias"></a> Alias pre {@link #KOPÍROVANIE_SÚBOROV KOPÍROVANIE_SÚBOROV}.</p> */
		public final static int KOPIROVANIE_SUBOROV = 5;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave pripájania súboru metódou
		 * {@link Súbor Súbor}{@code .}{@link Súbor#pripoj(String, String)
		 * pripoj}{@code (zdroj, cieľ)}.</p>
		 */
		public final static int PRIPÁJANIE_SÚBOROV = 6;

		/** <p><a class="alias"></a> Alias pre {@link #PRIPÁJANIE_SÚBOROV PRIPÁJANIE_SÚBOROV}.</p> */
		public final static int PRIPAJANIE_SUBOROV = 6;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave porovnávania súborov metódou
		 * {@link Súbor Súbor}{@code .}{@link Súbor#porovnaj(String, String)
		 * porovnaj}{@code (názov1, názov2)}.</p>
		 */
		public final static int POROVNANIE_SÚBOROV = 7;

		/** <p><a class="alias"></a> Alias pre {@link #POROVNANIE_SÚBOROV POROVNANIE_SÚBOROV}.</p> */
		public final static int POROVNANIE_SUBOROV = 7;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave odovzdávania údajov relevantnou
		 * metódou triedy {@link Spojenie Spojenie}. (Napríklad {@link 
		 * Spojenie#pošliSúbor(String) pošliSúbor}{@code (názovSúboru)}.)</p>
		 */
		public final static int ODOVZDANIE_ÚDAJOV = 8;

		/** <p><a class="alias"></a> Alias pre {@link #ODOVZDANIE_ÚDAJOV ODOVZDANIE_ÚDAJOV}.</p> */
		public final static int ODOVZDANIE_UDAJOV = 8;

		/**
		 * <p>Táto konštanta opisuje stav súvisiaci s reakciou
		 * {@code sekvencia}.
		 * <!--   -->
		 * (Je relevantná pre reakcie robota {@link GRobot#sekvencia(int,
		 * Object, Object, long, long) sekvencia} a reakciu aktívnej obsluhy
		 * udalostí {@link ObsluhaUdalostí#sekvencia(int, Object, Object, long,
		 * long) sekvencia}.)
		 * <!--   -->
		 * Parameter {@code kódSpracovania} uvedenej reakcie nadobúda túto
		 * hodnotu počas informovania o stave prijímania údajov relevantnou
		 * metódou triedy {@link Spojenie Spojenie}. (Napríklad {@link 
		 * Spojenie#uložOdpoveď(String) uložOdpoveď}{@code (názovSúboru)}.</p>
		 */
		public final static int PREVZATIE_ÚDAJOV = 9;

		/** <p><a class="alias"></a> Alias pre {@link #PREVZATIE_ÚDAJOV PREVZATIE_ÚDAJOV}.</p> */
		public final static int PREVZATIE_UDAJOV = 9;


	// Dialógy

		/**
		 * <p>Hodnota, ktorú vráti metóda v prípade, že používateľ nezvolil
		 * žiadnu možnosť a zavrel dialóg. (Pozri skupinu metód {@link 
		 * Svet#otázka(String) otázka}.)</p>
		 */
		public final static int ZAVRETÉ = JOptionPane.CLOSED_OPTION;

		/** <p><a class="alias"></a> Alias pre {@link #ZAVRETÉ ZAVRETÉ}.</p> */
		public final static int ZAVRETE = JOptionPane.CLOSED_OPTION;

		/**
		 * <p>Hodnota, ktorú vráti metóda v prípade, že používateľ dialóg zrušil.
		 * (Použiteľné napríklad pri metóde {@link Svet#otázka(String)
		 * otázka}.)</p>
		 */
		public final static int ZRUŠIŤ = JOptionPane.CANCEL_OPTION;

		/** <p><a class="alias"></a> Alias pre {@link #ZRUŠIŤ ZRUŠIŤ}.</p> */
		public final static int ZRUSIT = JOptionPane.CANCEL_OPTION;

		/**
		 * <p>Hodnota, ktorú vráti metóda v prípade, že používateľ zvolil
		 * z predvolených odpovedí „áno.“
		 * (Použiteľné napríklad pri metóde {@link Svet#otázka(String)
		 * otázka}.)</p>
		 */
		public final static int ÁNO = JOptionPane.YES_OPTION;

		/** <p><a class="alias"></a> Alias pre {@link #ÁNO ÁNO}.</p> */
		public final static int ANO = JOptionPane.YES_OPTION;

		/**
		 * <p>Hodnota, ktorú vráti metóda v prípade, že používateľ zvolil
		 * z predvolených odpovedí „nie.“
		 * (Použiteľné napríklad pri metóde {@link Svet#otázka(String)
		 * otázka}.)</p>
		 */
		public final static int NIE = JOptionPane.NO_OPTION;


	// Ladenie

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má vypísať obsah všetkých premenných na
		 * vnútornú konzolu pred začatím vykonávania skriptu.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYPÍSAŤ_PREMENNÉ = 0;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_PREMENNÉ VYPÍSAŤ_PREMENNÉ}.</p> */
		public final static int VYPISAT_PREMENNE = VYPÍSAŤ_PREMENNÉ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má na vnútornú konzolu pred začatím
		 * vykonávania skriptu vypísať definície všetkých menoviek v skripte,
		 * ktoré sa mu podarilo vyhľadať počas analýzy skriptu.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> V priebehu vývoja
		 * programovacieho rámca sa automatický výpis menoviek stal
		 * irelevantným, takže táto akcia sa teraz vykonáva len na
		 * požiadanie. (Pôvodne sa mechanizmus ladenia pýtal na každú
		 * menovku zvlášť, preto zostali definované aliasy aj v singulári
		 * (t. j. {@link #VYPÍSAŤ_MENOVKU VYPÍSAŤ_MENOVKU} a jeho alias bez
		 * diakritiky), ktoré teraz rámec aktívne nevyužíva.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYPÍSAŤ_MENOVKY = 1;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}.</p> */
		public final static int VYPISAT_MENOVKY = VYPÍSAŤ_MENOVKY;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}.</p> */
		public final static int VYPÍSAŤ_MENOVKU = VYPÍSAŤ_MENOVKY;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_MENOVKY VYPÍSAŤ_MENOVKY}.</p> */
		public final static int VYPISAT_MENOVKU = VYPÍSAŤ_MENOVKY;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má vypísať riadok skriptu na vnútornú
		 * konzolu.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYPÍSAŤ_RIADOK = 2;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_RIADOK VYPÍSAŤ_RIADOK}.</p> */
		public final static int VYPISAT_RIADOK = VYPÍSAŤ_RIADOK;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má čakať pred vykonaním riadka skriptu.
		 * Ak je odpoveď na túto otázku {@code valtrue}, tak táto správa
		 * vznikne opakovane po 350 ms, kým nebude odpoveď {@code valfalse}.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int ČAKAŤ = 3;

		/** <p><a class="alias"></a> Alias pre {@link #ČAKAŤ ČAKAŤ}.</p> */
		public final static int CAKAT = ČAKAŤ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má prerušiť vykonávanie skriptu.
		 * Ak je odpoveď {@code valtrue}, tak sa vykonávanie skriptu ukončí.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int PRERUŠIŤ = 4;

		/** <p><a class="alias"></a> Alias pre {@link #PRERUŠIŤ PRERUŠIŤ}.</p> */
		public final static int PRERUSIT = PRERUŠIŤ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim sa pokúša zistiť, či má v poslednej chvíli zabrániť
		 * vykonaniu príkazu s konkrétnymi hodnotami parametrov. Táto otázka
		 * ladenia sa dá využiť aj na overenie toho, s akými hodnotami
		 * parametrov bude v skutočnosti metóda zavolaná.
		 * Ak je odpoveď {@code valtrue}, tak sa príkaz nevykoná a stroj
		 * skriptu pokračuje tak, akoby metóda príkazu nebola nájdená (to
		 * znamená, že stroj ešte môže nájsť inú alternatívu na vykonanie
		 * podobného príkazu).</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int ZABRÁNIŤ_VYKONANIU = 5;

		/** <p><a class="alias"></a> Alias pre {@link #ZABRÁNIŤ_VYKONANIU ZABRÁNIŤ_VYKONANIU}.</p> */
		public final static int ZABRANIT_VYKONANIU = ZABRÁNIŤ_VYKONANIU;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia}
		 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom
		 * režime} – režim sa pokúša overiť, či smie vypísať ozvenu
		 * potvrdeného príkazového riadka na vnútornú konzolu.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYPÍSAŤ_PRÍKAZ = 6;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_PRÍKAZ VYPÍSAŤ_PRÍKAZ}.</p> */
		public final static int VYPISAT_PRIKAZ = VYPÍSAŤ_PRÍKAZ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia}
		 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom
		 * režime} – režim sa pokúša overiť, či smie vykonať potvrdený
		 * príkazový riadok.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYKONAŤ_PRÍKAZ = 7;

		/** <p><a class="alias"></a> Alias pre {@link #VYKONAŤ_PRÍKAZ VYKONAŤ_PRÍKAZ}.</p> */
		public final static int VYKONAT_PRIKAZ = VYKONAŤ_PRÍKAZ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia}
		 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom
		 * režime} – režim oznamuje, že ladenie bolo ukončené (bez chyby).</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int UKONČENIE_SKRIPTU = 8;

		/** <p><a class="alias"></a> Alias pre {@link #UKONČENIE_SKRIPTU UKONČENIE_SKRIPTU}.</p> */
		public final static int UKONCENIE_SKRIPTU = UKONČENIE_SKRIPTU;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia}
		 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom
		 * režime} – režim oznamuje, že ladenie bolo ukončené chybou.
		 * V parametri {@code riadok} metódy
		 * {@link ObsluhaUdalostí#ladenie(int, String, int) ladenie}
		 * je číslo riadka, na ktorom vznikla chyba a reťazec parametra
		 * {@code príkaz} obsahuje v tomto prípade jednoduchý text chybového
		 * hlásenia.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int UKONČENIE_CHYBOU = 9;

		/** <p><a class="alias"></a> Alias pre {@link #UKONČENIE_CHYBOU UKONČENIE_CHYBOU}.</p> */
		public final static int UKONCENIE_CHYBOU = UKONČENIE_CHYBOU;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia}
		 * v {@linkplain Svet#interaktívnyRežim(boolean) interaktívnom
		 * režime} – režim sa pokúša overiť, či smie vypísať úplné znenie
		 * aktuálne vykonávaného skriptu na vnútornú konzolu.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int VYPÍSAŤ_SKRIPT = 10;

		/** <p><a class="alias"></a> Alias pre {@link #VYPÍSAŤ_SKRIPT VYPÍSAŤ_SKRIPT}.</p> */
		public final static int VYPISAT_SKRIPT = VYPÍSAŤ_SKRIPT;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim oznamuje, že hodnota číselnej premennej sa zmenila a pokúša
		 * sa zistiť, či má novú hodnotu vypísať.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int ČÍSELNÁ_PREMENNÁ = -1;

		/** <p><a class="alias"></a> Alias pre {@link #ČÍSELNÁ_PREMENNÁ ČÍSELNÁ_PREMENNÁ}.</p> */
		public final static int CISELNA_PREMENNA = ČÍSELNÁ_PREMENNÁ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim oznamuje, že hodnota farebnej premennej sa zmenila a pokúša
		 * sa zistiť, či má novú hodnotu vypísať.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int FAREBNÁ_PREMENNÁ = -2;

		/** <p><a class="alias"></a> Alias pre {@link #FAREBNÁ_PREMENNÁ FAREBNÁ_PREMENNÁ}.</p> */
		public final static int FAREBNA_PREMENNA = FAREBNÁ_PREMENNÁ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim oznamuje, že hodnota polohovej premennej sa zmenila a pokúša
		 * sa zistiť, či má novú hodnotu vypísať.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int POLOHOVÁ_PREMENNÁ = -3;

		/** <p><a class="alias"></a> Alias pre {@link #POLOHOVÁ_PREMENNÁ POLOHOVÁ_PREMENNÁ}.</p> */
		public final static int POLOHOVA_PREMENNA = POLOHOVÁ_PREMENNÁ;

		/**
		 * <p>Konštanta {@linkplain Svet#režimLadenia(boolean) režimu ladenia} –
		 * režim oznamuje, že hodnota reťazcovej premennej sa zmenila a pokúša
		 * sa zistiť, či má novú hodnotu vypísať.</p>
		 * 
		 * @see ObsluhaUdalostí#ladenie(int, String, int)
		 */
		public final static int REŤAZCOVÁ_PREMENNÁ = -4;

		/** <p><a class="alias"></a> Alias pre {@link #REŤAZCOVÁ_PREMENNÁ REŤAZCOVÁ_PREMENNÁ}.</p> */
		public final static int RETAZCOVA_PREMENNA = REŤAZCOVÁ_PREMENNÁ;


		/**
		 * <p>Toto je kód chyby rezervovaný na signalizovanie situácie, keď
		 * vykonávanie (volanie) vnorených skriptov zaznamenalo príliš mnoho
		 * vnorení, čo z bezpečnostných dôvodov nie je povolené.</p>
		 * 
		 * <p>Táto chyba súvisí s metódou sveta {@link Svet#volajSkript(String)
		 * volajSkript}, ktorá môže byť použitá ako príkaz skriptu, čím vzniká
		 * možnosť uviaznutia v nekonečnom množstve vzájomných volaní skriptov
		 * (túto situáciu môže nechtiac spôsobiť pisateľ skriptov).</p>
		 * 
		 * <!-- TODO tento bezpečnostný mechanizmus nie je zďaleka dokončený.
		 * Pozri Svet.volajSkript… -->
		 */
		public final static int CHYBA_VOLANIA_SKRIPTU = -2;

		/**
		 * <p>Toto je kód chyby rezervovaný na signalizovanie situácie, keď
		 * sa vykonávanie skriptu ani nemohlo začať pretože skript mal byť
		 * prečítaný zo súboru (prípadne zdroja), pričom táto akcia
		 * zlyhala.</p>
		 */
		public final static int CHYBA_ČÍTANIA_SKRIPTU = -1;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_ČÍTANIA_SKRIPTU CHYBA_ČÍTANIA_SKRIPTU}.</p> */
		public final static int CHYBA_CITANIA_SKRIPTU = CHYBA_ČÍTANIA_SKRIPTU;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * nenastala žiadna chyba.</p>
		 */
		public final static int ŽIADNA_CHYBA = 0;

		/** <p><a class="alias"></a> Alias pre {@link #ŽIADNA_CHYBA ŽIADNA_CHYBA}.</p> */
		public final static int ZIADNA_CHYBA = ŽIADNA_CHYBA;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * nastala chyba počas vykonávania príkazu. Mohlo ísť napríklad
		 * o zadanie nesprávneho argumentu.</p>
		 */
		public final static int CHYBA_VYKONANIA_PRÍKAZU = 1;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_VYKONANIA_PRÍKAZU CHYBA_VYKONANIA_PRÍKAZU}.</p> */
		public final static int CHYBA_VYKONANIA_PRIKAZU = CHYBA_VYKONANIA_PRÍKAZU;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * v skripte sa vyskytla dvojnásobná definícia menovky. Táto chyba
		 * nastáva pri konštrukcii skriptu. Konštrukcia síce (vnútorne)
		 * pokračuje aj po jej vzniku, ale vykonávanie takéhoto skriptu by
		 * nebolo bezpečné, pretože skript by pri riadiacich príkazoch
		 * {@code na}, {@code ak}, {@code dokedy} (s prípadnou alternatívou
		 * {@code inak} pri posledných dvoch) nemusel správne identifikovať
		 * (a pravdepodobne ani neidentifikoval) tú menovku, ktorou mal
		 * autor skriptu v úmysle pokračovať, preto metódy produkujúce
		 * skripty takéto chybné inštancie „skartujú.“</p>
		 */
		public final static int CHYBA_DVOJITÁ_MENOVKA = 2;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_DVOJITÁ_MENOVKA CHYBA_DVOJITÁ_MENOVKA}.</p> */
		public final static int CHYBA_DVOJITA_MENOVKA = CHYBA_DVOJITÁ_MENOVKA;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * za riadiacim príkazom {@code na}, {@code ak}, {@code dokedy} alebo
		 * za alternatívou {@code inak}) chýba menovka.</p>
		 */
		public final static int CHYBA_CHÝBAJÚCA_MENOVKA = 3;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_CHÝBAJÚCA_MENOVKA CHYBA_CHÝBAJÚCA_MENOVKA}.</p> */
		public final static int CHYBA_CHYBAJUCA_MENOVKA = CHYBA_CHÝBAJÚCA_MENOVKA;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * menovka za riadiacim príkazom {@code na}, {@code ak}, {@code dokedy}
		 * alebo za alternatívou {@code inak} je neznáma (nie je definovaná
		 * v rámci aktuálneho bloku skriptu).</p>
		 */
		public final static int CHYBA_NEZNÁMA_MENOVKA = 4;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_NEZNÁMA_MENOVKA CHYBA_NEZNÁMA_MENOVKA}.</p> */
		public final static int CHYBA_NEZNAMA_MENOVKA = CHYBA_NEZNÁMA_MENOVKA;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * za menovkou riadiaceho príkazu {@code ak} alebo {@code dokedy}
		 * sa vyskytlo neznáme slovo. Za prvou menovkou môže nasledovať ďalšia
		 * menovka a to buď bezprostredne, alebo za slovom určujúcim
		 * alternatívu – {@code inak}. Ostatné slová sú považované za
		 * neznáme.</p>
		 */
		public final static int CHYBA_NEZNÁME_SLOVO = 5;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_NEZNÁME_SLOVO CHYBA_NEZNÁME_SLOVO}.</p> */
		public final static int CHYBA_NEZNAME_SLOVO = CHYBA_NEZNÁME_SLOVO;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * pokus o korektné rozpoznanie riadiaceho príkazu {@code obzor},
		 * {@code ak}, {@code opakuj} alebo {@code dokedy} zlyhal. Môže
		 * ísť o prípad, výskytu riadiaceho príkazu {@code obzor} bez bloku,
		 * o prípad, keď nie je definovaná riadiaca premenná príkazov
		 * opakovania ({@code opakuj}, {@code dokedy}), o prípad, keď riadiaci
		 * príkaz opakovania nenájde blok na opakovanie (a nemôže túto chybu
		 * nijako opraviť) alebo môže ísť o neznámu syntaktickú chybu.)</p>
		 */
		public final static int CHYBA_CHYBNÁ_ŠTRUKTÚRA = 6;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_CHYBNÁ_ŠTRUKTÚRA CHYBA_CHYBNÁ_ŠTRUKTÚRA}.</p> */
		public final static int CHYBA_CHYBNA_STRUKTURA = CHYBA_CHYBNÁ_ŠTRUKTÚRA;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * naposledy {@linkplain Svet#interaktívnaInštancia(String) aktivovaná
		 * inštancia} už alebo ešte nejestvuje. (Zadané meno inštancie je
		 * neznáme.)</p>
		 */
		public final static int CHYBA_NEZNÁME_MENO = 7;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_NEZNÁME_MENO CHYBA_NEZNÁME_MENO}.</p> */
		public final static int CHYBA_NEZNAME_MENO = CHYBA_NEZNÁME_MENO;

		/**
		 * <p>{@linkplain Svet#kódPoslednejChyby() Kód poslednej chyby}
		 * {@linkplain Svet#interaktívnyRežim(boolean) interaktívneho režimu}
		 * alebo {@linkplain Svet#vykonajSkript(String[]) vykonávania skriptu},
		 * ktorý signalizuje, že<!-- koniec spoločnej časti opisu chyby -->
		 * zadaný príkaz nebol rozpoznaný. Najčastejšími príčinami sú
		 * syntaktické chyby alebo neaktivovanie správnej (prípadne žiadnej)
		 * inštancie, to jest takej, ktorá skutočne obsahuje definíciu metódy
		 * zodpovedajúcej príkazu skriptu.</p>
		 */
		public final static int CHYBA_NEZNÁMY_PRÍKAZ = 8;

		/** <p><a class="alias"></a> Alias pre {@link #CHYBA_NEZNÁMY_PRÍKAZ CHYBA_NEZNÁMY_PRÍKAZ}.</p> */
		public final static int CHYBA_NEZNAMY_PRIKAZ = CHYBA_NEZNÁMY_PRÍKAZ;


	// Rolovacia lišta

		/** <p>Konštanta slúžiaca na určenie zvislej orientácie lišty.</p> */
		public static final int VERTIKÁLNA = JScrollBar.VERTICAL;

		/** <p>Konštanta slúžiaca na určenie zvislej orientácie lišty.</p> */
		public static final int VERTIKALNA = JScrollBar.VERTICAL;

		/** <p>Konštanta slúžiaca na určenie zvislej orientácie lišty.</p> */
		public static final int ZVISLÁ = JScrollBar.VERTICAL;

		/** <p>Konštanta slúžiaca na určenie zvislej orientácie lišty.</p> */
		public static final int ZVISLA = JScrollBar.VERTICAL;

		/** <p>Konštanta slúžiaca na určenie vodorovnej orientácie lišty.</p> */
		public static final int HORIZONTÁLNA = JScrollBar.HORIZONTAL;

		/** <p>Konštanta slúžiaca na určenie vodorovnej orientácie lišty.</p> */
		public static final int HORIZONTALNA = JScrollBar.HORIZONTAL;

		/** <p>Konštanta slúžiaca na určenie vodorovnej orientácie lišty.</p> */
		public static final int VODOROVNÁ = JScrollBar.HORIZONTAL;

		/** <p>Konštanta slúžiaca na určenie vodorovnej orientácie lišty.</p> */
		public static final int VODOROVNA = JScrollBar.HORIZONTAL;


	// Iné

		/**
		 * <p>Znaková konštanta nového riadka.
		 * (Použiteľné napríklad pri metóde
		 * {@link Plátno#vypíš(Object[]) vypíš}.
		 * Tiež má svoj význam pri používaní metódy
		 * {@link Svet#dialóg(String[], Object[], String) dialóg}
		 * a jej klonov.)</p>
		 */
		public final static char riadok = '\n';

}
