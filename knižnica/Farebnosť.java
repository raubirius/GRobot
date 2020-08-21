
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

package knižnica;

/**
 * <p>Rozhranie slúži na implementáciu metódy slúžiacej na zistenie farby
 * objektu a obsahuje definície konštánt farieb.</p>
 * 
 * @see Farba
 */
public interface Farebnosť
{
	// …
	// Farby

	/**
	 * <p>Farba, ktorá má všetky farebné zložky (aj zložku priehľadnosti) rovnú
	 * nule (<small>{@link Farba#Farba(int, int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num0}{@code , }
	 * {@code num0}{@code , }{@code num0}{@code )}</small>).
	 * Technicky ide o neviditeľnú čiernu farbu. Táto inštancia je používaná
	 * v špeciálnych prípadoch.</p>
	 */
	public final static Farba žiadna = new Farba(0, 0, 0, 0);

	/** <p><a class="alias"></a> Alias pre {@link #žiadna žiadna}.</p> */
	public final static Farba ziadna = žiadna;


	/**
	 * <p>Biela farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num255}{@code , }{@code num255}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- biela --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #FFFFFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba biela = new Farba(255, 255, 255);

	/**
	 * <p>Svetlošedá farba (<small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num176}{@code , }{@code num176}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlošedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B0B0B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlošedá = new Farba(176, 176, 176);

	/** <p><a class="alias"></a> Alias pre {@link #svetlošedá svetlošedá}.</p> */
	public final static Farba svetloseda = svetlošedá;

	/**
	 * <p>Šedá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num144}{@code , }{@code num144}{@code , }
	 * {@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- šedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #909090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba šedá = new Farba(144, 144, 144);

	/** <p><a class="alias"></a> Alias pre {@link #šedá šedá}.</p> */
	public final static Farba seda = šedá;

	/**
	 * <p>Tmavošedá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num96}{@code , }{@code num96}{@code , }
	 * {@code num96}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavošedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #606060"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavošedá = new Farba(96, 96, 96);

	/** <p><a class="alias"></a> Alias pre {@link #tmavošedá tmavošedá}.</p> */
	public final static Farba tmavoseda = tmavošedá;

	/**
	 * <p>{@linkplain #svetlošedá Svetlošedá} farba (čiže
	 * <a class="alias"></a> alias pre {@linkplain #svetlošedá svetlošedú}
	 * farbu; <small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num176}{@code , }{@code num176}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlošedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B0B0B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlosivá = svetlošedá;

	/** <p><a class="alias"></a> Alias pre {@link #svetlošedá svetlošedá}.</p> */
	public final static Farba svetlosiva = svetlošedá;

	/**
	 * <p>{@linkplain #šedá Šedá} farba (čiže <a class="alias"></a> alias
	 * pre {@linkplain #šedá šedú} farbu; <small>{@link Farba#Farba(int,
	 * int, int) Farba}{@code (}{@code num144}{@code ,
	 * }{@code num144}{@code , }{@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- šedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #909090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba sivá = šedá;

	/** <p><a class="alias"></a> Alias pre {@link #šedá šedá}.</p> */
	public final static Farba siva = šedá;

	/**
	 * <p>{@linkplain #tmavošedá Tmavošedá} farba (čiže
	 * <a class="alias"></a> alias pre {@linkplain #tmavošedá tmavošedú}
	 * farbu; <small>{@link Farba#Farba(int, int, int) Farba}{@code 
	 * (}{@code num96}{@code , }{@code num96}{@code , }
	 * {@code num96}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavošedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #606060"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavosivá = tmavošedá;

	/** <p><a class="alias"></a> Alias pre {@link #tmavošedá tmavošedá}.</p> */
	public final static Farba tmavosiva = tmavošedá;

	/**
	 * <p>Čierna farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num0}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- čierna --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #000000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba čierna = new Farba(0, 0, 0);

	/** <p><a class="alias"></a> Alias pre {@link #čierna čierna}.</p> */
	public final static Farba cierna = čierna;


	/**
	 * <p>Svetločervená farba (<small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num224}{@code , }{@code num0}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetločervená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #E00000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetločervená = new Farba(224, 0, 0);

	/** <p><a class="alias"></a> Alias pre {@link #svetločervená svetločervená}.</p> */
	public final static Farba svetlocervena = svetločervená;

	/**
	 * <p>Červená farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num176}{@code , }{@code num0}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- červená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B00000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba červená = new Farba(176, 0, 0);

	/** <p><a class="alias"></a> Alias pre {@link #červená červená}.</p> */
	public final static Farba cervena = červená;

	/**
	 * <p>Tmavočervená farba (<small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num144}{@code , }{@code num0}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavočervená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #900000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavočervená = new Farba(144, 0, 0);

	/** <p><a class="alias"></a> Alias pre {@link #tmavočervená tmavočervená}.</p> */
	public final static Farba tmavocervena = tmavočervená;


	/**
	 * <p>Svetlozelená farba (<small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num0}{@code , }{@code num224}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlozelená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00E000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlozelená = new Farba(0, 224, 0);

	/** <p><a class="alias"></a> Alias pre {@link #svetlozelená svetlozelená}.</p> */
	public final static Farba svetlozelena = svetlozelená;

	/**
	 * <p>Zelená farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num176}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- zelená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00B000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba zelená = new Farba(0, 176, 0);

	/** <p><a class="alias"></a> Alias pre {@link #zelená zelená}.</p> */
	public final static Farba zelena = zelená;

	/**
	 * <p>Tmavozelená farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num144}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavozelená --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #009000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavozelená = new Farba(0, 144, 0);

	/** <p><a class="alias"></a> Alias pre {@link #tmavozelená tmavozelená}.</p> */
	public final static Farba tmavozelena = tmavozelená;


	/**
	 * <p>Svetlomodrá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num224}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlomodrá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #0000E0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlomodrá = new Farba(0, 0, 224);

	/** <p><a class="alias"></a> Alias pre {@link #svetlomodrá svetlomodrá}.</p> */
	public final static Farba svetlomodra = svetlomodrá;

	/**
	 * <p>Modrá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num0}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- modrá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #0000B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba modrá = new Farba(0, 0, 176);

	/** <p><a class="alias"></a> Alias pre {@link #modrá modrá}.</p> */
	public final static Farba modra = modrá;

	/**
	 * <p>Tmavomodrá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num0}{@code , }
	 * {@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavomodrá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #000090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavomodrá = new Farba(0, 0, 144);

	/** <p><a class="alias"></a> Alias pre {@link #tmavomodrá tmavomodrá}.</p> */
	public final static Farba tmavomodra = tmavomodrá;


	/**
	 * <p>Svetlotyrkysová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num224}{@code , }
	 * {@code num224}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlotyrkysová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00E0E0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlotyrkysová = new Farba(0, 224, 224);

	/** <p><a class="alias"></a> Alias pre {@link #svetlotyrkysová svetlotyrkysová}.</p> */
	public final static Farba svetlotyrkysova = svetlotyrkysová;

	/**
	 * <p>Tyrkysová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num176}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tyrkysová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00B0B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tyrkysová = new Farba(0, 176, 176);

	/** <p><a class="alias"></a> Alias pre {@link #tyrkysová tyrkysová}.</p> */
	public final static Farba tyrkysova = tyrkysová;

	/**
	 * <p>Tmavotyrkysová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num144}{@code , }
	 * {@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavotyrkysová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #009090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavotyrkysová = new Farba(0, 144, 144);

	/** <p><a class="alias"></a> Alias pre {@link #tmavotyrkysová tmavotyrkysová}.</p> */
	public final static Farba tmavotyrkysova = tmavotyrkysová;


	/**
	 * <p>Svetlopurpurová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num224}{@code , }{@code num0}{@code , }
	 * {@code num224}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlopurpurová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #E000E0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlopurpurová = new Farba(224, 0, 224);

	/** <p><a class="alias"></a> Alias pre {@link #svetlopurpurová svetlopurpurová}.</p> */
	public final static Farba svetlopurpurova = svetlopurpurová;

	/**
	 * <p>Purpurová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num176}{@code , }{@code num0}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- purpurová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B000B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba purpurová = new Farba(176, 0, 176);

	/** <p><a class="alias"></a> Alias pre {@link #purpurová purpurová}.</p> */
	public final static Farba purpurova = purpurová;

	/**
	 * <p>Tmavopurpurová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num144}{@code , }{@code num0}{@code , }
	 * {@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavopurpurová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #900090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavopurpurová = new Farba(144, 0, 144);

	/** <p><a class="alias"></a> Alias pre {@link #tmavopurpurová tmavopurpurová}.</p> */
	public final static Farba tmavopurpurova = tmavopurpurová;


	/**
	 * <p>Svetložltá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num240}{@code , }{@code num240}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetložltá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #F0F000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetložltá = new Farba(240, 240, 0);

	/** <p><a class="alias"></a> Alias pre {@link #svetložltá svetložltá}.</p> */
	public final static Farba svetlozlta = svetložltá;

	/**
	 * <p>Žltá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num224}{@code , }{@code num224}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Pozorné oko si môže všimnúť, že
	 * odtiene žltej sú definované z iných pomerov zložiek r, g, b, než
	 * odtiene iných základných farieb (červenej, zelenej, modrej…).
	 * Jednoduchým experimentom sa nám podarilo ukázať, že vnímanie „žltej“
	 * je natoľko špecifické, že na to, aby bola vnímaná ako skutočne „žltá,“
	 * musí byť veľmi jasná. Preto pôvodná jasnožltá je teraz definovaná ako
	 * žltá a jej odtiene k nej majú veľmi blízko. Sami sa môžete presvedčiť,
	 * že na monitore pôsobia tmavšie odtiene „žltej“ ako žltozelená, olivová,
	 * lilavá, khaky… Ide čiastočne o psychochromatickú záležitosť a vplyv
	 * môže mať aj to, že vo farebnom priestore sRGB farba „žltá“ ako taká
	 * nejestvuje – je miešaná zo zložiek červenej a zelenej. „Technická“ žltá
	 * nie je taká žltá, aká je vnímaná ľuďmi.</p>
	 * 
	 * <table><tr><!-- žltá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #E0E000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba žltá = new Farba(224, 224, 0);

	/** <p><a class="alias"></a> Alias pre {@link #žltá žltá}.</p> */
	public final static Farba zlta = žltá;

	/**
	 * <p>Tmavožltá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num208}{@code , }{@code num208}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavožltá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #D0D000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavožltá = new Farba(208, 208, 0);

	/** <p><a class="alias"></a> Alias pre {@link #tmavožltá tmavožltá}.</p> */
	public final static Farba tmavozlta = tmavožltá;


	/**
	 * <p>Svetlohnedá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num180}{@code , }{@code num96}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlohnedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B46000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlohnedá = new Farba(180, 96, 0); // b46000

	/** <p><a class="alias"></a> Alias pre {@link #svetlohnedá svetlohnedá}.</p> */
	public final static Farba svetlohneda = svetlohnedá;

	/**
	 * <p>Hnedá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num160}{@code , }{@code num80}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- hnedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #A05000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba hnedá = new Farba(160, 80, 0); // a05000

	/** <p><a class="alias"></a> Alias pre {@link #hnedá hnedá}.</p> */
	public final static Farba hneda = hnedá;

	/**
	 * <p>Tmavohnedá farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num140}{@code , }{@code num64}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavohnedá --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #8C4000"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavohnedá = new Farba(140, 64, 0); // 8c4000

	/** <p><a class="alias"></a> Alias pre {@link #tmavohnedá tmavohnedá}.</p> */
	public final static Farba tmavohneda = tmavohnedá;


	/**
	 * <p>Svetlooranžová farba (<small>{@link Farba#Farba(int,
	 * int, int) Farba}{@code (}{@code num240}{@code , }{@code num180}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlooranžová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #F0B400"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlooranžová = new Farba(240, 180, 0); // f0b400

	/** <p><a class="alias"></a> Alias pre {@link #svetlooranžová svetlooranžová}.</p> */
	public final static Farba svetlooranzova = svetlooranžová;

	/**
	 * <p>Oranžová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num220}{@code , }{@code num150}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- oranžová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #DC9600"
	 * /></svg></td></tr></table>
	 */
	public final static Farba oranžová = new Farba(220, 150, 0); // dc9600

	/** <p><a class="alias"></a> Alias pre {@link #oranžová oranžová}.</p> */
	public final static Farba oranzova = oranžová;

	/**
	 * <p>Tmavooranžová farba (<small>{@link Farba#Farba(int, int,
	 * int) Farba}{@code (}{@code num200}{@code , }{@code num120}{@code , }
	 * {@code num0}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavooranžová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #C87800"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavooranžová = new Farba(200, 120, 0); // c87800

	/** <p><a class="alias"></a> Alias pre {@link #tmavooranžová tmavooranžová}.</p> */
	public final static Farba tmavooranzova = tmavooranžová;


	/**
	 * <p>Svetloružová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num255}{@code , }{@code num217}{@code , }
	 * {@code num217}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetloružová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #FFD9D9"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetloružová = new Farba(255, 217, 217); // ffd9d9

	/** <p><a class="alias"></a> Alias pre {@link #svetloružová svetloružová}.</p> */
	public final static Farba svetloruzova = svetloružová;

	/**
	 * <p>Ružová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num255}{@code , }{@code num179}{@code , }
	 * {@code num179}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- ružová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #FFB3B3"
	 * /></svg></td></tr></table>
	 */
	public final static Farba ružová = new Farba(255, 179, 179); // ffb3b3

	/** <p><a class="alias"></a> Alias pre {@link #ružová ružová}.</p> */
	public final static Farba ruzova = ružová;

	/**
	 * <p>Tmavoružová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num255}{@code , }{@code num140}{@code , }
	 * {@code num140}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavoružová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #FF8C8C"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavoružová = new Farba(255, 140, 140); // ff8c8c

	/** <p><a class="alias"></a> Alias pre {@link #tmavoružová tmavoružová}.</p> */
	public final static Farba tmavoruzova = tmavoružová;


	/**
	 * <p>Uhlíková farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num16}{@code , }{@code num16}{@code , }
	 * {@code num16}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- uhlíková --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #303030"
	 * /></svg></td></tr></table>
	 */
	// public final static Farba uhlíková = new Farba(16, 16, 16); // 101010
	public final static Farba uhlíková = new Farba(48, 48, 48); // 303030

	/** <p><a class="alias"></a> Alias pre {@link #uhlíková uhlíková}.</p> */
	public final static Farba uhlikova = uhlíková;

	/**
	 * <p>Antracitová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num32}{@code , }{@code num32}{@code , }
	 * {@code num32}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- antracitová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #404040"
	 * /></svg></td></tr></table>
	 */
	// public final static Farba antracitová = new Farba(32, 32, 32); // 202020
	public final static Farba antracitová = new Farba(64, 64, 64); // 404040

	/** <p><a class="alias"></a> Alias pre {@link #antracitová antracitová}.</p> */
	public final static Farba antracitova = antracitová;

	/**
	 * <p>Papierová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num224}{@code , }{@code num224}{@code , }
	 * {@code num224}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- papierová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #E0E0E0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba papierová = new Farba(224, 224, 224); // E0E0E0

	/** <p><a class="alias"></a> Alias pre {@link #papierová papierová}.</p> */
	public final static Farba papierova = papierová;

	/**
	 * <p>Snehová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num240}{@code , }{@code num240}{@code , }
	 * {@code num240}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- snehová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #F0F0F0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba snehová = new Farba(240, 240, 240); // F0F0F0

	/** <p><a class="alias"></a> Alias pre {@link #snehová snehová}.</p> */
	public final static Farba snehova = snehová;

	/**
	 * <p>Tmavofialová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num144}{@code , }{@code num48}{@code , }
	 * {@code num144}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavofialová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #903090"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavofialová = new Farba(144, 48, 144); // 903090

	/** <p><a class="alias"></a> Alias pre {@link #tmavofialová tmavofialová}.</p> */
	public final static Farba tmavofialova = tmavofialová;

	/**
	 * <p>Fialová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num176}{@code , }{@code num80}{@code , }
	 * {@code num176}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- fialová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B050B0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba fialová = new Farba(176, 80, 176); // B050B0

	/** <p><a class="alias"></a> Alias pre {@link #fialová fialová}.</p> */
	public final static Farba fialova = fialová;

	/**
	 * <p>Svetlofialová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num224}{@code , }{@code num112}{@code , }
	 * {@code num224}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetlofialová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #E070E0"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetlofialová = new Farba(224, 112, 224); // E070E0

	/** <p><a class="alias"></a> Alias pre {@link #svetlofialová svetlofialová}.</p> */
	public final static Farba svetlofialova = svetlofialová;

	/**
	 * <p>Tmavoatramentová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num45}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavoatramentová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #002DFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavoatramentová = new Farba(0, 45, 255); // 002DFF

	/** <p><a class="alias"></a> Alias pre {@link #tmavoatramentová tmavoatramentová}.</p> */
	public final static Farba tmavoatramentova = tmavoatramentová;

	/**
	 * <p>Atramentová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num15}{@code , }{@code num75}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- atramentová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #0F4BFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba atramentová = new Farba(15, 75, 255); // 0F4BFF

	/** <p><a class="alias"></a> Alias pre {@link #atramentová atramentová}.</p> */
	public final static Farba atramentova = atramentová;

	/**
	 * <p>Svetloatramentová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num35}{@code , }{@code num105}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetloatramentová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #2369FF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetloatramentová = new Farba(35, 105, 255); // 2369FF

	/** <p><a class="alias"></a> Alias pre {@link #svetloatramentová svetloatramentová}.</p> */
	public final static Farba svetloatramentova = svetloatramentová;

	/**
	 * <p>Tmavoakvamarínová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num205}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmavoakvamarínová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00CDFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmavoakvamarínová = new Farba(0, 205, 255); // 00CDFF

	/** <p><a class="alias"></a> Alias pre {@link #tmavoakvamarínová tmavoakvamarínová}.</p> */
	public final static Farba tmavoakvamarinova = tmavoakvamarínová;

	/**
	 * <p>Akvamarínová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num0}{@code , }{@code num225}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- akvamarínová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #00E1FF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba akvamarínová = new Farba(0, 225, 255); // 00E1FF

	/** <p><a class="alias"></a> Alias pre {@link #akvamarínová akvamarínová}.</p> */
	public final static Farba akvamarinova = akvamarínová;

	/**
	 * <p>Svetloakvamarínová farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num20}{@code , }{@code num245}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetloakvamarínová --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #14F5FF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetloakvamarínová = new Farba(20, 245, 255); // 14F5FF

	/** <p><a class="alias"></a> Alias pre {@link #svetloakvamarínová svetloakvamarínová}.</p> */
	public final static Farba svetloakvamarinova = svetloakvamarínová;

	/**
	 * <p>Tmavá nebeská farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num140}{@code , }{@code num255}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- tmaváNebeská --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #8CFFFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba tmaváNebeská = new Farba(140, 255, 255); // 8CFFFF

	/** <p><a class="alias"></a> Alias pre {@link #tmaváNebeská tmaváNebeská}.</p> */
	public final static Farba tmavaNebeska = tmaváNebeská;

	/**
	 * <p>Nebeská farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num179}{@code , }{@code num255}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- nebeská --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #B3FFFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba nebeská = new Farba(179, 255, 255); // B3FFFF

	/** <p><a class="alias"></a> Alias pre {@link #nebeská nebeská}.</p> */
	public final static Farba nebeska = nebeská;

	/**
	 * <p>Svetlá nebeská farba (<small>{@link Farba#Farba(int, int, int)
	 * Farba}{@code (}{@code num217}{@code , }{@code num255}{@code , }
	 * {@code num255}{@code )}</small>).</p>
	 * 
	 * <table><tr><!-- svetláNebeská --><td>Ukážka:</td><td> </td><td><svg
	 * class="color-box"><rect style="fill: #D9FFFF"
	 * /></svg></td></tr></table>
	 */
	public final static Farba svetláNebeská = new Farba(217, 255, 255); // D9FFFF

	/** <p><a class="alias"></a> Alias pre {@link #svetláNebeská svetláNebeská}.</p> */
	public final static Farba svetlaNebeska = svetláNebeská;


	/**
	 * <p>Zoznam (pole) všetkých preddefinovaných farieb. Farby v tomto
	 * poli sú usporiadané podľa zobrazenia v dialógu {@linkplain 
	 * Farba#vyberFarbu() výberu farby} (a príbuzných komponentov), s tým
	 * drobným rozdielom, že zobrazenie v dialógu je transponované. Pri
	 * zoraďovaní boli brané do úvahy najmä tieto dve pravidlá: svetlé
	 * farby umiestňovať vyššie (prípadne vľavo) a príbuzné farby podľa
	 * možností (s ohľadom na rôznorodosť palety) čo najbližšie k sebe.</p>
	 * 
	 * <p><b>Tabuľka farieb zoradených tak, ako sú umiestnené v tomto
	 * poli farieb:</b></p>
	 * 
	 * <style>
	 * svg.color-box
	 * {
	 * 	width: 24;
	 * 	height: 24;
	 * }
	 * 
	 * svg.color-box rect
	 * {
	 * 	x: 1; y: 1;
	 * 	width: 22px;
	 * 	height: 22px;
	 * 	stroke-width: 2;
	 * 	stroke: black;
	 * }
	 * </style>
	 * 
	 * <table>
	 * <tr>
	 * 
	 * <td>{@link #biela biela} <br
	 * /><svg class="color-box"><rect style="fill: #FFFFFF" /></svg></td>
	 * <td>{@link #snehová snehová} <br
	 * /><svg class="color-box"><rect style="fill: #F0F0F0" /></svg></td>
	 * <td>{@link #papierová papierová} <br
	 * /><svg class="color-box"><rect style="fill: #E0E0E0" /></svg></td>
	 * <td>{@link #svetlošedá svetlošedá} <br
	 * /><svg class="color-box"><rect style="fill: #B0B0B0" /></svg></td>
	 * <td>{@link #šedá šedá} <br
	 * /><svg class="color-box"><rect style="fill: #909090" /></svg></td>
	 * <td>{@link #tmavošedá tmavošedá} <br
	 * /><svg class="color-box"><rect style="fill: #606060" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetláNebeská svetláNebeská} <br
	 * /><svg class="color-box"><rect style="fill: #D9FFFF" /></svg></td>
	 * <td>{@link #nebeská nebeská} <br
	 * /><svg class="color-box"><rect style="fill: #B3FFFF" /></svg></td>
	 * <td>{@link #tmaváNebeská tmaváNebeská} <br
	 * /><svg class="color-box"><rect style="fill: #8CFFFF" /></svg></td>
	 * <td>{@link #svetloakvamarínová svetloakvamarínová} <br
	 * /><svg class="color-box"><rect style="fill: #14F5FF" /></svg></td>
	 * <td>{@link #akvamarínová akvamarínová} <br
	 * /><svg class="color-box"><rect style="fill: #00E1FF" /></svg></td>
	 * <td>{@link #tmavoakvamarínová tmavoakvamarínová} <br
	 * /><svg class="color-box"><rect style="fill: #00CDFF" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetlotyrkysová svetlotyrkysová} <br
	 * /><svg class="color-box"><rect style="fill: #00E0E0" /></svg></td>
	 * <td>{@link #tyrkysová tyrkysová} <br
	 * /><svg class="color-box"><rect style="fill: #00B0B0" /></svg></td>
	 * <td>{@link #tmavotyrkysová tmavotyrkysová} <br
	 * /><svg class="color-box"><rect style="fill: #009090" /></svg></td>
	 * <td>{@link #svetlozelená svetlozelená} <br
	 * /><svg class="color-box"><rect style="fill: #00E000" /></svg></td>
	 * <td>{@link #zelená zelená} <br
	 * /><svg class="color-box"><rect style="fill: #00B000" /></svg></td>
	 * <td>{@link #tmavozelená tmavozelená} <br
	 * /><svg class="color-box"><rect style="fill: #009000" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetloatramentová svetloatramentová} <br
	 * /><svg class="color-box"><rect style="fill: #2369FF" /></svg></td>
	 * <td>{@link #atramentová atramentová} <br
	 * /><svg class="color-box"><rect style="fill: #0F4BFF" /></svg></td>
	 * <td>{@link #tmavoatramentová tmavoatramentová} <br
	 * /><svg class="color-box"><rect style="fill: #002DFF" /></svg></td>
	 * <td>{@link #svetlomodrá svetlomodrá} <br
	 * /><svg class="color-box"><rect style="fill: #0000E0" /></svg></td>
	 * <td>{@link #modrá modrá} <br
	 * /><svg class="color-box"><rect style="fill: #0000B0" /></svg></td>
	 * <td>{@link #tmavomodrá tmavomodrá} <br
	 * /><svg class="color-box"><rect style="fill: #000090" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetloružová svetloružová} <br
	 * /><svg class="color-box"><rect style="fill: #FFD9D9" /></svg></td>
	 * <td>{@link #ružová ružová} <br
	 * /><svg class="color-box"><rect style="fill: #FFB3B3" /></svg></td>
	 * <td>{@link #tmavoružová tmavoružová} <br
	 * /><svg class="color-box"><rect style="fill: #FF8C8C" /></svg></td>
	 * <td>{@link #svetločervená svetločervená} <br
	 * /><svg class="color-box"><rect style="fill: #E00000" /></svg></td>
	 * <td>{@link #červená červená} <br
	 * /><svg class="color-box"><rect style="fill: #B00000" /></svg></td>
	 * <td>{@link #tmavočervená tmavočervená} <br
	 * /><svg class="color-box"><rect style="fill: #900000" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetložltá svetložltá} <br
	 * /><svg class="color-box"><rect style="fill: #F0F000" /></svg></td>
	 * <td>{@link #žltá žltá} <br
	 * /><svg class="color-box"><rect style="fill: #E0E000" /></svg></td>
	 * <td>{@link #tmavožltá tmavožltá} <br
	 * /><svg class="color-box"><rect style="fill: #D0D000" /></svg></td>
	 * <td>{@link #svetlopurpurová svetlopurpurová} <br
	 * /><svg class="color-box"><rect style="fill: #E000E0" /></svg></td>
	 * <td>{@link #purpurová purpurová} <br
	 * /><svg class="color-box"><rect style="fill: #B000B0" /></svg></td>
	 * <td>{@link #tmavopurpurová tmavopurpurová} <br
	 * /><svg class="color-box"><rect style="fill: #900090" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetlooranžová svetlooranžová} <br
	 * /><svg class="color-box"><rect style="fill: #F0B400" /></svg></td>
	 * <td>{@link #oranžová oranžová} <br
	 * /><svg class="color-box"><rect style="fill: #DC9600" /></svg></td>
	 * <td>{@link #tmavooranžová tmavooranžová} <br
	 * /><svg class="color-box"><rect style="fill: #C87800" /></svg></td>
	 * <td>{@link #svetlofialová svetlofialová} <br
	 * /><svg class="color-box"><rect style="fill: #E070E0" /></svg></td>
	 * <td>{@link #fialová fialová} <br
	 * /><svg class="color-box"><rect style="fill: #B050B0" /></svg></td>
	 * <td>{@link #tmavofialová tmavofialová} <br
	 * /><svg class="color-box"><rect style="fill: #903090" /></svg></td>
	 * </tr><tr>
	 * <td>{@link #svetlohnedá svetlohnedá} <br
	 * /><svg class="color-box"><rect style="fill: #B46000" /></svg></td>
	 * <td>{@link #hnedá hnedá} <br
	 * /><svg class="color-box"><rect style="fill: #A05000" /></svg></td>
	 * <td>{@link #tmavohnedá tmavohnedá} <br
	 * /><svg class="color-box"><rect style="fill: #8C4000" /></svg></td>
	 * <td>{@link #antracitová antracitová} <br
	 * /><svg class="color-box"><rect style="fill: #404040" /></svg></td>
	 * <td>{@link #uhlíková uhlíková} <br
	 * /><svg class="color-box"><rect style="fill: #303030" /></svg></td>
	 * <td>{@link #čierna čierna} <br
	 * /><svg class="color-box"><rect style="fill: #000000" /></svg></td>
	 * </tr>
	 * </table>
	 * 
	 * <p>(Pozri aj opis metódy {@linkplain Farba#vyberFarbu() dialógu
	 * na výber farby.})</p>
	 */
	public final static Farba preddefinovanéFarby[] = {
		biela, snehová, papierová,
		svetlošedá, šedá, tmavošedá,

		svetláNebeská, nebeská, tmaváNebeská,
		svetloakvamarínová, akvamarínová, tmavoakvamarínová,

		svetlotyrkysová, tyrkysová, tmavotyrkysová,
		svetlozelená, zelená, tmavozelená,

		svetloatramentová, atramentová, tmavoatramentová,
		svetlomodrá, modrá, tmavomodrá,

		svetloružová, ružová, tmavoružová,
		svetločervená, červená, tmavočervená,

		svetložltá, žltá, tmavožltá,
		svetlopurpurová, purpurová, tmavopurpurová,

		svetlooranžová, oranžová, tmavooranžová,
		svetlofialová, fialová, tmavofialová,

		svetlohnedá, hnedá, tmavohnedá,
		antracitová, uhlíková, čierna};

	/** <p><a class="alias"></a> Alias pre {@link #preddefinovanéFarby preddefinovanéFarby}.</p> */
	public final static Farba preddefinovaneFarby[] = preddefinovanéFarby;


	/** <p>Prototyp metódy na zistenie farby objektu.</p> */
	public Farba farba();

	// Odporúčané metódy na nastavenie novej farby objektu
	/* * <p>Prototyp metódy na nastavenie farby objektu.</p> * /
	public void farba(Color nováFarba);
	/* * <p>Prototyp metódy na kopírovanie farby objektu.</p> * /
	public void farba(Farebnosť podľaObjektu);
	/* * <p>Prototyp metódy na nastavenie farby objektu.</p> * /
	public Farba farba(int r, int g, int b);
	/* * <p>Prototyp metódy na nastavenie farby objektu.</p> * /
	public Farba farba(int r, int g, int b, int a);
	*/
}
