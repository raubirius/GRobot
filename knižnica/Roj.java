
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2019 by Roman Horváth
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

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import static java.lang.Math.*;

/**
 * <p>Táto trieda umožňuje definovať a pracovať so sériou bodov
 * v trojrozmernom priestore. Body môžu byť prepojené spojmi a v každom bode
 * môže byť definovaný objekt na nakreslenie.</p>
 * 
 * <p>TODO.</p>
 * 
 * <p>Súradnicový systém roja je orientovaný pravotočivo.
 * <!-- TODO – overiť, doplniť obrázok a opis. -->…</p>
 * 
 * <p>TODO.</p>
 * 
 * <p> </p>
 * 
 * <!-- TODO – download: ovladac-roja.java » OvládačRoja.java. -->
 * 
 * <p><b>Informačné zdroje, ktoré môžu pomôcť pri riešení matematických
 * problémov súvisiacich s touto kapitolou (triedou):</b></p>
 * 
 * <p class="remark"><b>Poznámka:</b> Zdroje môžu obsahovať chyby (v čase
 * ich citovania ich obsahovali), preto je vhodné informácie z nich
 * konfrontovať s inou (napríklad tlačenou) literatúrou. Autor pri tvorbe
 * tejto triedy použil uvedené zdroje najmä na pripomenutie si informácií,
 * ktoré získal počas svojho vysokoškolského štúdia.</p>
 * 
 * <ul>
 * <li><small>Liekens, Anthony</small>: <em>Computers » Rendering
 * Tutorial.</em> anthony.liekens.net, 2000–2013. Dostupné na:
 * <a href="http://anthony.liekens.net/index.php/Computers/RenderingTutorial"
 * target="_blank"
 * >http://anthony.liekens.net/index.php/Computers/RenderingTutorial</a>.
 * Naposledy pristúpené: 15. 10. 2017.</li>
 * <li><em>Matrix multiplication – Wikipedia.</em> Dostupné na:
 * <a href="https://en.wikipedia.org/wiki/Matrix_multiplication"
 * target="_blank">https://en.wikipedia.org/wiki/Matrix_multiplication</a>.
 * Naposledy pristúpené: 15. 10. 2017.</li>
 * <li><em>Rotation matrix – Wikipedia.</em> Dostupné na:
 * <a href="https://en.wikipedia.org/wiki/Rotation_matrix"
 * target="_blank">https://en.wikipedia.org/wiki/Rotation_matrix</a>.
 * Naposledy pristúpené: 15. 10. 2017.</li>
 * </ul>
 */
public class Roj
{
	/**
	 * <p>Trieda uchovávajúca údaje o jednom bode roja. Kreslenie, aktivitu
	 * a potrebné prepočty zabezpečuje programátor s pomocou vstavaných metód
	 * alebo na základe údajov o bode.</p>
	 */
	public class Bod implements Comparable<Bod>
	{
		/**
		 * <p>Porovná polohu tohto bodu s polohou zadaného bodu a vráti
		 * hodnotu {@code num0.0}, ak sú vzdialenosti oboch bodov od kamery
		 * zhodné, kladnú hodnotu, ak je vzdialenosť druhého bodu od kamery
		 * väčšia a zápornú hodnotu v opačnom prípade.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Rozhranie {@link Comparable}
		 * bolo implementované z dôvodu zaručenia správneho poradia kreslenia
		 * bodov roja.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia, ktorý demonštruje vyššie
		 * uvedené informácie. -->
		 * 
		 * @param iný bod, s ktorým má byť tento bod porovnaný
		 * @return celé číslo vyjadrujúce rozdiel vzdialeností porovnávaných
		 *     bodov od kamery
		 */
		public int compareTo(Bod iný)
		{
			//// Upravovať súradnice x a y bola chyba…
			// double Δx = kx + this.x2;
			// double Δy = ky + this.y2;
			double Δz = kz + this.z2;
			// double d1 = (Δx * Δx + Δy * Δy + Δz * Δz);
			double d1 = (this.x2 * this.x2 + this.y2 * this.y2 + Δz * Δz);

			// Δx = kx + iný.x2;
			// Δy = ky + iný.y2;
			Δz = kz + iný.z2;
			// double d2 = (Δx * Δx + Δy * Δy + Δz * Δz);
			double d2 = (iný.x2 * iný.x2 + iný.y2 * iný.y2 + Δz * Δz);

			return (int)(d2 - d1);

			//// Iná chyba bola skúsiť to riešiť takto:
			// if (this.z2 <= 0 && iný.z2 <= 0) return 0;
			// if (this.z2 <= 0) return 1;
			// if (iný.z2 <= 0) return -1;
			// return (int)(1000 / this.z2) - (int)(1000 / iný.z2);
		}

		/**
		 * <p>Originálna súradnica polohy bodu. Atribúty {@code x0},
		 * {@code y0} a {@code z0} určujú pôvodnú netransformovanú polohu
		 * bodu v priestore.</p>
		 */
		public double x0 = 0.0, y0 = 0.0, z0 = 0.0;

		/**
		 * <p>Posunutie originálnej súradnice polohy bodu. Atribúty
		 * {@code dx}, {@code dy} a {@code dz} určujú voliteľné posunutie
		 * (transfromovanie posunutím) originálnej polohy bodu (pozri
		 * {@link #x0 x0}, {@link #y0 y0} alebo {@link #z0 z0}) v priestore.
		 * Takto sa dá bod ľubovoľne posúvať v priestore bez toho, aby sme
		 * stratili originálne hodnoty súradníc jeho polohy.</p>
		 */
		public double dx = 0.0, dy = 0.0, dz = 0.0;

		/**
		 * <p>Súradnica stredu otáčania bodu. Atribúty {@code xs}, {@code ys},
		 * {@code zs} určujú polohu stredu otáčania bodu v priestore.
		 * (Pozri aj {@link #alfa alfa}, {@link #beta beta} alebo
		 * {@link #gama gama}.)</p>
		 */
		public double xs = 0.0, ys = 0.0, zs = 0.0;

		/**
		 * <p>Uhly pootočenia bodu okolo stredu otáčania. Atribúty
		 * {@code alfa}, {@code beta} a {@code gama} určujú uhly pootočenia
		 * (transformovania otáčaním) bodu v priestore okolo stredu otáčania
		 * [{@link #xs xs}, {@link #ys ys}, {@link #zs zs}].</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public double alfa = 0.0, beta = 0.0, gama = 0.0;

		/**
		 * <p>Parameter rozmeru objektu kresleného na polohe bodu. (Pozri aj
		 * {@link #faktor faktor}.)</p>
		 */
		public double rozmer = 1.0;

		/**
		 * <p>Tento príznak určuje, že súradnice {@link #x1 x1},
		 * {@link #y1 y1} a {@link #z1 z1} musia byť prepočítané.</p>
		 */
		public boolean transformuj = true;

		/**
		 * <p>Prepočítaná (transformovaná) súradnica polohy bodu. Atribúty
		 * {@code x1}, {@code y1} a {@code z1} určujú transformovanú polohu
		 * bodu v priestore. Sú vypočítané z atribútov originálnych súradníc
		 * bodu [{@link #x0 x0}, {@link #y0 y0}, {@link #z0 z0}], hodnôt
		 * posunutia bodu v priestore [{@link #dx dx}, {@link #dy dy},
		 * {@link #dz dz}] (transformácia posunutím) a hodnôt pootočenia bodu
		 * v priestore (uhly {@link #alfa alfa}, {@link #beta beta}
		 * a {@link #gama gama}) okolo stredu otáčania [{@link #xs xs},
		 * {@link #ys ys}, {@link #zs zs}] (transformovania otáčaním).</p>
		 * 
		 * <p class="caution"><b>Upozornenie:</b> Z uvedeného vyplýva, že ak
		 * sa hodnota ktoréhokoľvek z vyššie spomenutých atribútov zmení, tak
		 * súradnice {@code x1}, {@code y1} a {@code z1} musia byť opätovne
		 * prepočítané. To znamená, že súčasne so zmenou hociktorého
		 * z uvedených atribútov je nevyhnutné nastaviť príznak
		 * {@link #transformuj transformuj} na {@code valtrue}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public double x1 = 0.0, y1 = 0.0, z1 = 0.0;

		/**
		 * <p>Tento atribút určuje, či bude objekt na polohe tohto bodu
		 * zobrazený. V každom bode roja môže alebo nemusí byť kreslený
		 * nejaký objekt – 2D objekt (prípadne určitým vlastným spôsobom
		 * generovaný 3D objekt – autor takéhoto kódu by musel doriešiť
		 * zistenie orientácie objektu). Tento atribút určuje, či má byť
		 * spustené jeho kreslenie.</p>
		 */
		public boolean zobraz = true;

		/**
		 * <p>Tento atribút určuje zmenu farby robota vykonanú pred kreslením
		 * objektu na polohe tohto bodu.</p>
		 */
		public Farba farba = null;

		/**
		 * <p>Tento atribút určuje zmenu hrúbky čiary robota vykonanú pred
		 * kreslením objektu na polohe tohto bodu.</p>
		 */
		public double dho = 0.0;

		/**
		 * <p>Konečná súradnica polohy bodu. Atribúty {@code x2}, {@code y2}
		 * a {@code z2} určujú konečnú polohu bodu v priestore, ktorá je
		 * vypočítaná z hodnôt {@link #x1 x1}, {@link #y1 y1} a {@link #z1 z1}
		 * a z atribútov transformácie roja (pootočenia a polohy kamery,
		 * mierky…).</p>
		 */
		public double x2 = 0.0, y2 = 0.0, z2 = 0.0;

		/**
		 * <p>Tento atribút určuje, či bude medzi polohou predchádzajúceho
		 * a tohto bodu roja kreslená spojovacia čiara. (Pri kreslení roja
		 * ako celku je táto hodnota pre prvý bod roja irelevantná.)</p>
		 */
		public boolean spoj = true;

		/**
		 * <p>Tento atribút určuje zmenu farby robota vykonanú pred
		 * kreslením spoja smerujúceho do tohto bodu.</p>
		 */
		public Farba farbaSpoja = null;

		/**
		 * <p>Tento atribút určuje zmenu hrúbky čiary robota vykonanú pred
		 * kreslením spoja smerujúceho do tohto bodu.</p>
		 */
		public double dh = 0.0;

		/**
		 * <p>Atribút cieľovej (zobrazovanej) polohy a veľkosti objektu na
		 * bode. Atribúty {@code x3} a {@code y3} určujú zobrazovanú
		 * (premietanú) polohu objektu (kresleného na polohe tohto bodu) na
		 * plátno sveta. Atribút {@code z3} určuje prepočítaný rozmer objektu.
		 * Hodnoty týchto atribútov sú vypočítané len v prípade, že je faktor
		 * rozmeru a zobrazovanej polohy (atribút {@link #faktor faktor})
		 * kladný. Počítajú sa z hodnôt súradníc konečnej polohy bodu
		 * [{@link #x2 x2}, {@link #y2 y2}, {@link #z2 z2}], rozmeru objektu
		 * kresleného na bode – {@link #rozmer rozmer}, faktora (deliteľa)
		 * rozmeru a zobrazovanej polohy objektu na bode – {@link #faktor
		 * faktor} a z atribútov pohľadu na roj.</p>
		 */
		public double x3 = 0.0, y3 = 0.0, z3 = 0.0;

		/**
		 * <p>Faktor (deliteľ) rozmeru a zobrazovanej polohy objektu na bode,
		 * ktorý je prepočítavaný podľa konečných súradníc bodu ([{@link #x2
		 * x2}, {@link #y2 y2}, {@link #z2 z2}]) a aktuálnej vzdialenosti
		 * kamery od roja.</p>
		 * 
		 * <p>Ak je faktor menší alebo rovný nule, tak je objekt
		 * nepozorovateľný – nachádza sa za pozorovateľom alebo na jeho
		 * úrovni. Ak je faktor kladný, stáva sa deliteľom zobrazovanej
		 * polohy a rozmeru objektu, ktoré sú uložené v atribútoch {@link #x3
		 * x3}, {@link #y3 y3} a {@link #z3 z3}.</p>
		 */
		public double faktor = 0.0;

		/**
		 * <p>Tento atribút umožňuje rozlíšiť body patriace do zákaznícky
		 * definovateľných skupín. Atribút je dostupný na voľné použitie.
		 * Nemá žiadne vnútorné využitie v rámci tried roja.</p>
		 */
		public int skupina = 0;

		/**
		 * <p>Ľubovoľná inštancia s dodatočnými informáciami súvisiacimi
		 * s týmto bodom. Do tohto atribútu sa dá uložiť ľubovoľný
		 * zákaznícky objekt, ktorý môže niesť ďalšie informácie súvisiace
		 * s týmto bodom.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia. -->
		 */
		public Object objekt = null;

		/**
		 * <p>Mapa umožňujúca spravovanie zákazníckeho zoznamu celočíselných
		 * parametrov bodu. Význam parametrov určuje programátor podľa
		 * svojich potrieb.</p>
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #reálneParametre
		 */
		public final TreeMap<Integer, Integer> celočíselnéParametre =
			new TreeMap<>();

		/** <p><a class="alias"></a> Alias pre {@link #celočíselnéParametre celočíselnéParametre}.</p> */
		public final TreeMap<Integer, Integer> celociselneParametre =
			celočíselnéParametre;

		/**
		 * <p>Mapa umožňujúca spravovanie zákazníckeho zoznamu reálnych
		 * parametrov bodu. Význam parametrov určuje programátor podľa
		 * svojich potrieb.</p>
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 */
		public final TreeMap<Integer, Double> reálneParametre =
			new TreeMap<>();

		/** <p><a class="alias"></a> Alias pre {@link #reálneParametre reálneParametre}.</p> */
		public final TreeMap<Integer, Double> realneParametre =
			reálneParametre;

		/**
		 * <p>Toto je atribút, do ktorého sa priebežne (automaticky) ukladá
		 * posledná orientácia grafického robota počas kreslenia tohto bodu.
		 * Dá sa to využiť na rôzne účely, napríklad nakreslenie šípky ako
		 * objektu v tomto bode podľa poslednej orientácie v čase kreslenia
		 * spoja, pričom samotný spoj môže byť skrytý.</p>
		 */
		public double smer = 90.0;

		/**
		 * <p>Toto kreslenie umožňuje upraviť tvar objektu nakresleného na
		 * súradniciach tohto bodu roja.</p>
		 */
		public KreslenieTvaru kreslenie = null;

		/**
		 * <p>Toto kreslenie umožňuje definovať komplexnejší spôsob exportu
		 * objektu na súradiciach tohto bodu roja do formátu SVG. Toto
		 * kreslenie má prioritu pred atribútom {@link #svgTvar svgTvar},
		 * ktorý poskytuje veľmi primitívnu možnosť exportu tvarov roja
		 * (resp. priemetov jeho bodov a objektov na nich) do formátu SVG.</p>
		 * 
		 * <!-- TODO príklad použitia, inak bude nepochopiteľné, ako to bolo
		 * vôbec myslené… -->
		 */
		public KreslenieTvaru svgKreslenie = null;

		/**
		 * <p>Tento atribút umožňuje definovať jednoduchý tvar, ktorý
		 * bude exportovaný ako objekt umiestnený na súradniciach tohto
		 * bodu roja do formátu SVG. Prioritu pred týmto atribútom má
		 * kreslenie, ktoré môže byť uložené v atribúte {@link 
		 * #svgKreslenie svgKreslenie}.</p>
		 */
		public Shape svgTvar = null;
		// ✓ Treba domyslieť to, čo presne sa bude ukladať do tejto inštancie
			// a ako sa s tým bude manipulovať. To jest, či to má byť tvar vo
			// finálnej podobe (transformovaný), alebo má byť ovplyvňovaný
			// mierkou robota. Ak bude treba, tak treba implementovať
			// prislúchajúcu podporu k tomuto mechanizmu.
			// <!-- TODO ešte treba otestovať -->

		/**
		 * <p>Ak je hodnota atribútu {@link #transformuj transformuj} rovná
		 * {@code valtrue}, tak táto metóda prepočíta (vypočíta nové) hodnoty
		 * transformovaných súradníc {@link #x1 x1}, {@link #y1 y1}
		 * a {@link #z1 z1}. Potom automaticky nastaví atribútu {@link 
		 * #transformuj transformuj} hodnotu {@code valfalse}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 */
		public void transformuj()
		{
			if (transformuj)
			{
				double αr = toRadians(alfa);
				double βr = toRadians(beta);
				double γr = toRadians(gama);

				double sinα = sin(αr);
				double sinβ = sin(βr);
				double sinγ = sin(γr);

				double cosα = cos(αr);
				double cosβ = cos(βr);
				double cosγ = cos(γr);

				double T00 = cosβ * cosγ;
				double T01 = -cosβ * sinγ;
				double T02 = sinβ;

				double T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
				double T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
				double T12 = -sinα * cosβ;

				double T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
				double T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
				double T22 = cosα * cosβ;

				double xx = x0 + dx - xs;
				double yy = y0 + dy - ys;
				double zz = z0 + dz - zs;

				x1 = xs + (xx * T00) + (yy * T01) + (zz * T02);
				y1 = ys + (xx * T10) + (yy * T11) + (zz * T12);
				z1 = zs + (xx * T20) + (yy * T21) + (zz * T22);

				transformuj = false;
			}
		}

		/**
		 * <p>Táto metóda fyzicky presunie bod na jeho transformovanú polohu
		 * a zruší transformácie posunutia a pootočenia bodu. To znamená, že
		 * transformované súradnice [{@link #x1 x1}, {@link #y1 y1}, {@link 
		 * #z1 z1}] budú skopírované do originálnych súradníc [{@link #x0
		 * x0}, {@link #y0 y0}, {@link #z0 z0}] a hodnoty transformácií
		 * posunutia ({@link #dx dx}, {@link #dy dy}, {@link #dz dz})
		 * a pootočenia (uhly {@link #alfa alfa}, {@link #beta beta}
		 * a {@link #gama gama}) bodu v priestore budú vynulované. (Hodnoty
		 * súradníc stredu otáčania [{@link #xs xs}, {@link #ys ys},
		 * {@link #zs zs}] táto metóda ponecháva v pôvodnom stave. Ak je
		 * príznak {@link #transformuj transformuj} rovný {@code valtrue},
		 * tak je pred upevnením automaticky spustená metóda {@link 
		 * #transformuj() transformuj()}.)</p>
		 */
		public void upevni()
		{
			if (transformuj) transformuj();
			x0 = x1; y0 = y1; z0 = z1;
			dx = dy = dz = 0.0;
			alfa = beta = gama = 0.0;
		}

		/**
		 * <p>Táto metóda využije kresliaceho robota roja na nakreslenie spoja
		 * (čiary) smerujúceho z aktuálnej polohy robota do tohto bodu.
		 * (<b style="color: red">Ak má robot položené pero!</b>) Predpokladá
		 * sa, že aktuálna poloha robota je polohou predchádzajúceho bodu
		 * roja.</p>
		 */
		public void kresliSpoj()
		{
			if (faktor > 0.0)
			{
				if (neplatnáSúradnica)
				{
					neplatnáSúradnica = false;
					smer = kresli.smerNa(x3, y3);
					kresli.skočNa(x3, y3);
				}
				else if (spoj)
				{
					if (null == farbaSpoja)
					{
						if (0.0 == dh)
						{
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dh);
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dh);
						}
					}
					else
					{
						Farba f = kresli.farba();
						kresli.farba(farbaSpoja);

						if (0.0 == dh)
						{
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dh);
							smer = kresli.smerNa(x3, y3);
							kresli.choďNa(x3, y3);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dh);
						}

						kresli.farba(f);
					}
				}
				else
				{
					smer = kresli.smerNa(x3, y3);
					kresli.skočNa(x3, y3);
				}
			}
			else neplatnáSúradnica = true;
		}

		/**
		 * <p>Táto metóda využije kresliaceho robota roja na nakreslenie
		 * objektu umiestneného na polohe tohto bodu (ak je viditeľný – pozri
		 * atribút {@link #zobraz zobraz}). Ak je definované vlastné
		 * {@link #kreslenie kreslenie}, tak je touto metódou využité, inak
		 * je tvarom objektu pečiatka robota (čo môže byť ľubovoľný tvar,
		 * pretože robot môže mať definované vlastné kreslenie).</p>
		 */
		public void kresliTeleso()
		{
			if (faktor > 0.0)
			{
				if (zobraz)
				{
					if (null == farba)
					{
						kresli.smer(smer);
						kresli.skočNa(x3, y3);
						kresli.veľkosť(z3);

						if (0.0 == dho)
						{
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						}
					}
					else
					{
						Farba f = kresli.farba();
						kresli.farba(farba);

						kresli.smer(smer);
						kresli.skočNa(x3, y3);
						kresli.veľkosť(z3);

						if (0.0 == dho)
						{
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
						}
						else
						{
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);
							if (null == kreslenie)
								kresli.pečiatka();
							else
								kreslenie.kresli(kresli);
							kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						}

						kresli.farba(f);
					}
				}
			}
		}


		/**
		 * <p>Umožňuje prevziať hodnotu zákaznícky definovaného celočíselného
		 * parametra bodu. Metóda používa zoznam {@link #celočíselnéParametre
		 * celočíselnéParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param predvolenáHodnota hodnota, ktorá bude vrátená v prípade,
		 *     že parameter s určeným indexom nie je definovaný
		 * @return hodnota parametra alebo predvolená hodnota
		 * 
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public int dajParameter(int index, int predvolenáHodnota)
		{
			Integer parameter = celočíselnéParametre.get(index);
			if (null == parameter) return predvolenáHodnota;
			return parameter;
		}

		/**
		 * <p>Umožňuje nastaviť hodnotu zákaznícky definovaného celočíselného
		 * parametra bodu. Metóda používa zoznam {@link #celočíselnéParametre
		 * celočíselnéParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param hodnota nová hodnota parametra
		 * 
		 * @see #dajParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public void nastavParameter(int index, int hodnota)
		{ celočíselnéParametre.put(index, hodnota); }

		/**
		 * <p>Umožňuje prevziať hodnotu zákaznícky definovaného reálneho
		 * parametra bodu. Metóda používa zoznam {@link #reálneParametre
		 * reálneParametre}.</p>
		 * 
		 * <!-- TODO – vyrobiť príklad použitia. -->
		 * 
		 * @param index index parametra
		 * @param predvolenáHodnota hodnota, ktorá bude vrátená v prípade,
		 *     že parameter s určeným indexom nie je definovaný
		 * @return hodnota parametra alebo predvolená hodnota
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #nastavParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public double dajParameter(int index, double predvolenáHodnota)
		{
			Double parameter = reálneParametre.get(index);
			if (null == parameter) return predvolenáHodnota;
			return parameter;
		}

		/**
		 * <p>Umožňuje nastaviť hodnotu zákaznícky definovaného reálneho
		 * parametra bodu. Metóda používa zoznam {@link #reálneParametre
		 * reálneParametre}.</p>
		 * 
		 * @param index index parametra
		 * @param hodnota nová hodnota parametra
		 * 
		 * @see #dajParameter(int, int)
		 * @see #nastavParameter(int, int)
		 * @see #dajParameter(int, double)
		 * @see #celočíselnéParametre
		 * @see #reálneParametre
		 */
		public void nastavParameter(int index, double hodnota)
		{ reálneParametre.put(index, hodnota); }


		/**
		 * <p>Táto metóda umožňuje exportovať spoj definovaný k tomuto bodu
		 * do zadanej inštancie {@linkplain SVGPodpora SVG podpory}. Ide
		 * o metódu, ktorá je v prípade exportu do formátu SVG pomyselným
		 * obrazom metódy {@link #kresliSpoj() kresliSpoj}. Táto metóda je
		 * automaticky volaná metódou roja {@link 
		 * Roj#pridajDoSVG(SVGPodpora) pridajDoSVG}.</p>
		 * 
		 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
		 *     do ktorej bude exportovaný {@linkplain Shape tvar Javy
		 *     (čiary)} reprezentujúci viditeľný spoj medzi prechádzajúcim
		 *     a týmto bodom roja
		 * 
		 * @see Roj#pridajDoSVG(SVGPodpora)
		 */
		public void spojDoSVG(SVGPodpora svgPodpora)
		{
			if (faktor > 0.0)
			{
				double x0 = Svet.prepočítajX(kresli.polohaX());
				double y0 = Svet.prepočítajY(kresli.polohaY());
				kresli.skočNa(x3, y3);
				double x1 = Svet.prepočítajX(kresli.polohaX());
				double y1 = Svet.prepočítajY(kresli.polohaY());

				if (neplatnáSúradnica)
					neplatnáSúradnica = false;
				else if (spoj)
				{
					svgPodpora.pridaj(new Line2D.Double(x0, y0, x1, y1));
					svgPodpora.hrúbkaČiary(-1, kresli.hrúbkaČiary() + dh);
					if (null == farbaSpoja)
						svgPodpora.farbaČiary(-1, kresli.farba());
					else
						svgPodpora.farbaČiary(-1, farbaSpoja);
				}
			}
			else neplatnáSúradnica = true;
		}

		/**
		 * <p>Táto metóda umožňuje exportovať objekt definovaný pre tento
		 * bod do zadanej inštancie {@linkplain SVGPodpora SVG podpory}.
		 * Ide o metódu, ktorá je v prípade exportu do formátu SVG
		 * pomyselným obrazom metódy {@link #kresliTeleso() kresliTeleso}.
		 * Metóda využíva hodnoty atribútov {@link #svgKreslenie
		 * svgKreslenie} a {@link #svgTvar svgTvar}, ak sú neprázdne.
		 * (Pozri aj ich opisy.) Táto metóda je automaticky volaná metódou
		 * roja {@link Roj#pridajDoSVG(SVGPodpora) pridajDoSVG}.</p>
		 * 
		 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
		 *     do ktorej budú exportované {@linkplain Shape tvary Javy}
		 *     reprezentujúce teleso umiestnené v tomto bode roja
		 */
		public void telesoDoSVG(SVGPodpora svgPodpora)
		{
			if (faktor > 0.0)
			{
				if (zobraz)
				{
					kresli.smer(smer);
					kresli.skočNa(x3, y3);
					kresli.veľkosť(z3);

					if (null != svgKreslenie)
					{
						// TODO – otestovať
						Farba f = kresli.farba();
						if (null != farba)
							kresli.farba(farba);
						kresli.hrúbkaČiary(kresli.hrúbkaČiary() + dho);

						if (kresli.kreslenieTvarovPovolené())
						{
							kresli.nekresliTvary();
							svgKreslenie.kresli(kresli);
							kresli.kresliTvary();
						}
						else svgKreslenie.kresli(kresli);

						kresli.hrúbkaČiary(kresli.hrúbkaČiary() - dho);
						kresli.farba(f);
					}
					else
					{
						if (null == svgTvar)
						{
							if (kresli.kreslenieTvarovPovolené())
							{
								kresli.nekresliTvary();
								svgPodpora.pridaj(kresli.kruh());
								kresli.kresliTvary();
							}
							else svgPodpora.pridaj(kresli.kruh());
						}
						else
						{
							// svgPodpora.pridaj(svgTvar); – „Zlepšené“ (resp.
							// opravené) nasledujúcim kódom, pričom treba
							// povedať, že tento pôvodný riadok (aj keď nebol
							// nikdy otestovaný) by isto aj tak dobre
							// nefungoval. Pôvodne to bolo myslené tak, že:
							// „Však ak bude tento tvar definovaný, vloží sa
							// do SVG ten. Však ho netreba nejako extra
							// transformovať… Rotáciu a mierku obetujeme…“
							// Lenže sám od seba by sa nedostal ani na
							// správnu pozíciu, haha…

							// TODO – overiť, či toto riešenie dostatočne
							// dobre funguje:
							if (kresli.kreslenieTvarovPovolené())
							{
								kresli.nekresliTvary();
								svgPodpora.pridaj(kresli.
									kresliTvar(svgTvar, true));
								kresli.kresliTvary();
							}
							else svgPodpora.pridaj(kresli.
								kresliTvar(svgTvar, true));
						}

						svgPodpora.hrúbkaČiary(-1, kresli.hrúbkaČiary() + dho);
						if (null == farba)
							svgPodpora.farbaČiary(-1, kresli.farba());
						else
							svgPodpora.farbaČiary(-1, farba);
					}
				}
			}
		}


		/**
		 * <p>Prečíta údaje o inštancii tohto bodu z konfiguračného súboru
		 * otvoreného na čítanie. Metóda vyžaduje identifikátor menného
		 * priestoru, ktorý bol vyhradený pre tento bod. <small>(Roj priraďuje
		 * automaticky ku každému bodu identifikátor v tvare {@code 
		 * bod[}<em>«index»</em>{@code ]}.)</small></p>
		 * 
		 * (Pozri aj informácie uvedené v opise metódy {@link 
		 * #uložDoSúboru(Súbor, String) uložDoSúboru}.)
		 * 
		 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
		 *     na čítanie
		 * @param identifikátor vnorený menný priestor, z ktorého budú
		 *     prevzaté údaje o bode
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public void čítajZoSúboru(Súbor súbor, String identifikátor)
			throws IOException
		{
			súbor.vnorMennýPriestorVlastností(identifikátor);

			try
			{
				{
					double[] predvolené = null; // new double[] {0.0, 0.0, 0.0};
					double[] prečítané;

					prečítané = súbor.čítajVlastnosť("poloha", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						x0 = prečítané[0];
						y0 = prečítané[1];
						z0 = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("posun", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						dx = prečítané[0];
						dy = prečítané[1];
						dz = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("stred", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						xs = prečítané[0];
						ys = prečítané[1];
						zs = prečítané[2];
					}

					prečítané = súbor.čítajVlastnosť("uhly", predvolené);
					if (null != prečítané && prečítané.length >= 3)
					{
						alfa = prečítané[0];
						beta = prečítané[1];
						gama = prečítané[2];
					}
				}

				rozmer = súbor.čítajVlastnosť("rozmer", rozmer);
				zobraz = súbor.čítajVlastnosť("zobraz", zobraz);
				spoj = súbor.čítajVlastnosť("spoj", spoj);
				skupina = súbor.čítajVlastnosť("skupina", skupina);

				dho = súbor.čítajVlastnosť("dho", dho);
				dh = súbor.čítajVlastnosť("dh", dh);

				farba = súbor.čítajVlastnosť("farba", farba);
				farbaSpoja = súbor.čítajVlastnosť("farbaSpoja", farbaSpoja);

				{
					int[] predvolené = null, prečítané;

					celočíselnéParametre.clear();
					prečítané = súbor.čítajVlastnosť("parametre", predvolené);
					if (null != prečítané)
					{
						int[] hodnoty = súbor.čítajVlastnosť(
							"hodnoty", predvolené);

						if (null == hodnoty)
							for (int j : prečítané)
								celočíselnéParametre.put(j, 0);
						else
						{
							int i = 0;
							for (int j : prečítané)
							{
								if (i < hodnoty.length)
									celočíselnéParametre.put(j, hodnoty[i]);
								else
									celočíselnéParametre.put(j, 0);
								++i;
							}
						}
					}

					prečítané = súbor.čítajVlastnosť("prázdne", predvolené);
					if (null != prečítané)
						for (int j : prečítané)
							celočíselnéParametre.put(j, null);

					reálneParametre.clear();
					prečítané = súbor.čítajVlastnosť(
						"reálneParametre", predvolené);
					if (null != prečítané)
					{
						double[] hodnoty = súbor.čítajVlastnosť(
							"reálneHodnoty", (double[])null);

						if (null == hodnoty)
							for (int j : prečítané)
								reálneParametre.put(j, 0.0);
						else
						{
							int i = 0;
							for (int j : prečítané)
							{
								if (i < hodnoty.length)
									reálneParametre.put(j, hodnoty[i]);
								else
									reálneParametre.put(j, 0.0);
								++i;
							}
						}
					}

					prečítané = súbor.čítajVlastnosť(
						"reálnePrázdne", predvolené);
					if (null != prečítané)
						for (int j : prečítané)
							reálneParametre.put(j, null);
				}

				transformuj = true;
			}
			finally
			{
				súbor.vynorMennýPriestorVlastností();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void citajZoSuboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void prečítajZoSúboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
		public void precitajZoSuboru(Súbor súbor, String identifikátor)
		throws IOException { čítajZoSúboru(súbor, identifikátor); }

		/**
		 * <p>Zapíše údaje o inštancii tohto bodu do konfiguračného súboru
		 * otvoreného na zápis. Metóda vyžaduje identifikátor menného
		 * priestoru, ktorý bude použitý na bezpečné uloženie množiny
		 * údajov o bode. Roj priraďuje automaticky ku každému bodu
		 * identifikátor v tvare {@code bod[}<em>«index»</em>{@code ]}. Ak
		 * je namiesto identifikátora zadaná hodnota {@code valnull}, tak
		 * bude použitý aktuálny menný priestor a ak by to bol hlavný menný
		 * priestor, tak jestvuje vysoké riziko toho, že údaje budú
		 * kolidovať s inými vlastnosťami zapísanými v súbore.
		 * <small>(V skutočnosti by zápis a následné čítanie bez
		 * identifikátora menného podpriestoru fungoval len pre jediný bod
		 * roja, čo nie je príliš užitočné.)</small></p>
		 * 
		 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
		 *     na zápis
		 * @param identifikátor vnorený menný priestor, do ktorého budú
		 *     vložené údaje o bode
		 * 
		 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
		 *     operácii
		 */
		public void uložDoSúboru(Súbor súbor, String identifikátor)
			throws IOException
		{
			súbor.vnorMennýPriestorVlastností(identifikátor);

			try
			{
				súbor.zapíšVlastnosť("poloha", new double[] {x0, y0, z0});
				súbor.zapíšVlastnosť("posun", new double[] {dx, dy, dz});
				súbor.zapíšVlastnosť("stred", new double[] {xs, ys, zs});
				súbor.zapíšVlastnosť("uhly", new double[] {alfa, beta, gama});

				súbor.zapíšVlastnosť("rozmer", rozmer);
				súbor.zapíšVlastnosť("zobraz", zobraz);
				súbor.zapíšVlastnosť("spoj", spoj);
				súbor.zapíšVlastnosť("skupina", skupina);

				súbor.zapíšVlastnosť("dho", dho);
				súbor.zapíšVlastnosť("dh", dh);

				súbor.zapíšVlastnosť("farba", farba);
				súbor.zapíšVlastnosť("farbaSpoja", farbaSpoja);

				StringBuilder kľúče = new StringBuilder();
				StringBuilder hodnoty = new StringBuilder();
				StringBuilder prázdne = new StringBuilder();

				for (Map.Entry<Integer, Integer> záznam :
					celočíselnéParametre.entrySet())
				{
					Integer kľúč = záznam.getKey();
					Integer hodnota = záznam.getValue();
					if (null == hodnota)
					{
						prázdne.append(kľúč);
						prázdne.append(" ");
					}
					else
					{
						kľúče.append(kľúč);
						kľúče.append(" ");
						hodnoty.append(hodnota);
						hodnoty.append(" ");
					}
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("parametre");
				else
				{
					súbor.zapíšVlastnosť("parametre", kľúče);
					súbor.zapíšVlastnosť("hodnoty", hodnoty);
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("prázdne");
				else
					súbor.zapíšVlastnosť("prázdne", prázdne);

				kľúče.setLength(0);
				hodnoty.setLength(0);
				prázdne.setLength(0);

				for (Map.Entry<Integer, Integer> záznam :
					celočíselnéParametre.entrySet())
				{
					Integer kľúč = záznam.getKey();
					Integer hodnota = záznam.getValue();
					if (null == hodnota)
					{
						prázdne.append(kľúč);
						prázdne.append(" ");
					}
					else
					{
						kľúče.append(kľúč);
						kľúče.append(" ");
						hodnoty.append(hodnota);
						hodnoty.append(" ");
					}
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("reálneParametre");
				else
				{
					súbor.zapíšVlastnosť("reálneParametre", kľúče);
					súbor.zapíšVlastnosť("reálneHodnoty", hodnoty);
				}

				if (0 == kľúče.length())
					súbor.vymažVlastnosť("reálnePrázdne");
				else
					súbor.zapíšVlastnosť("reálnePrázdne", prázdne);
			}
			finally
			{
				súbor.vynorMennýPriestorVlastností();
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor, String) uložDoSúboru}.</p> */
		public void ulozDoSuboru(Súbor súbor, String identifikátor)
		throws IOException { uložDoSúboru(súbor, identifikátor); }
	}


	/**
	 * <p>Pomocná trieda na riadené vkladanie bodov do roja. Smerník je
	 * jednoduchá neviditeľná pomôcka na generovanie (pridávanie) bodov
	 * roja. Smerník je orientovaný kurzor umožňujúci pridávanie bodov
	 * na svojej pozícii. Dá sa presúvať, otáčať, orientovať smerom na
	 * zadaný bod, uchovávať (zálohovať) a obnovovať svoju polohu
	 * a orientáciu s použitím vnútorných zásobníkov a podobne.</p>
	 * 
	 * <!-- TODO – vyrobiť príklad použitia. -->
	 */
	public class Smerník
	{
		// Aktuálna poloha smerníka.
		private double x = 0.0;
		private double y = 0.0;
		private double z = 0.0;

		// Aktuálny smer (orientácia; zavrhnuté – miskoncepcia).
		// private double α = 0.0;
		// private double β = 0.0;
		// private double γ = 0.0;

		// Jednotkový vektor posunu – smer smerníka.
		private double Δx = 0.0;
		private double Δy = 0.0;
		private double Δz = 1.0;

		// Stred otáčania (nepoužité; zavrhnuté – miskoncepcia).
		// private double sx = 0.0;
		// private double sy = 0.0;
		// private double sz = 0.0;

		// Zásobníky na zálohovanie polohy a smeru.
		private final Stack<double[]> zásobník = new Stack<>();
		private final Stack<double[]> zásobníkPolôh = new Stack<>();
		private final Stack<double[]> zásobníkSmerov = new Stack<>();

		/**
		 * <p>Príznak toho, či nové vkladané body majú mať viditeľné
		 * (definované) spoje.</p>
		 */
		public boolean vkladajSpoje = true;

		/**
		 * <p>Príznak toho, či nové vkladané body majú mať viditeľné
		 * objekty – či majú byť objekty definované na ich polohe kreslené.</p>
		 */
		public boolean vkladajObjekty = true;


		// // Na testovanie:
		// public void dumpPos()
		// { System.out.println("Pos[" + x + ", " + y + ", " + z + "]"); }
		// // public void dumpDir()
		// // { System.out.println("Dir[" + α + ", " + β + ", " + γ + "]"); }
		// public void dumpUni()
		// { System.out.println("Uni[" + Δx + ", " + Δy + ", " + Δz + "]"); }
		// public void dump() { dumpPos(); // dumpDir();
		// dumpUni(); }


		/**
		 * <p>Vráti aktuálnu polohu smerníka vo forme trojprvkového
		 * poľa [x, y, z].</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] poloha() { return new double[] {x, y, z}; }

		/**
		 * <p>Presunie smerník na zadané súradnice.</p>
		 * 
		 * @param x x-ová súradnica novej polohy
		 * @param y y-ová súradnica novej polohy
		 * @param z z-ová súradnica novej polohy
		 */
		public void poloha(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/**
		 * <p>Presunie smerník na súradnice zadané vo forme trojprvkového
		 * poľa. (Ak má pole menší počet prvkov, poloha nebude nastavená.
		 * Pri väčšom počte prvkov sú brané do úvahy len prvé tri.)</p>
		 * 
		 * @param bod najmenej trojprvkové pole súradníc novej polohy
		 */
		public void poloha(double[] bod)
		{
			if (null != bod && bod.length >= 3)
			{
				x = bod[0];
				y = bod[1];
				z = bod[2];
			}
		}

		/**
		 * <p>Posunie smerník o zadaný rozdiel súradníc.</p>
		 * 
		 * @param Δx prírastok x-ovej súradnice
		 * @param Δy prírastok y-ovej súradnice
		 * @param Δz prírastok z-ovej súradnice
		 */
		public void posuň(double Δx, double Δy, double Δz)
		{
			x += Δx;
			y += Δy;
			z += Δz;
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double, double, double) posuň}.</p> */
		public void posun(double Δx, double Δy, double Δz)
		{ posuň(Δx, Δy, Δz); }

		/**
		 * <p>Posunie smerník o zadaný rozdiel súradníc zadaný vo forme
		 * trojprvkového poľa. (Ak má pole menší počet prvkov, poloha nebude
		 * upravená. Pri väčšom počte prvkov sú brané do úvahy len prvé
		 * tri.)</p>
		 * 
		 * @param bod najmenej trojprvkové pole prírastkov súradníc polohy
		 */
		public void posuň(double[] bod)
		{
			if (null != bod && bod.length >= 3)
			{
				x += bod[0];
				y += bod[1];
				z += bod[2];
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double[]) posuň}.</p> */
		public void posun(double[] bod) { posuň(bod); }

		/**
		 * <p>Posunie smerník o zadanú dĺžku v aktuálnom smere (jeho
		 * orientácie).</p>
		 * 
		 * @param dĺžka dĺžka, o ktorú sa má smerník posunúť v aktuálnom
		 *     smere jednotkového vektora
		 */
		public void posuň(double dĺžka)
		{
			x += Δx * dĺžka;
			y += Δy * dĺžka;
			z += Δz * dĺžka;
		}

		/** <p><a class="alias"></a> Alias pre {@link #posuň(double) posuň}.</p> */
		public void posun(double dĺžka) { posuň(dĺžka); }


		/**
		 * <p>Vráti aktuálny „smer“ (jednotkový vektor pohybu) smerníka vo
		 * forme trojprvkového poľa [Δx, Δy, Δz].</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] smer() { return new double[] {Δx, Δy, Δz}; }

		/**
		 * <p>Nastaví „smer“ (jednotkový vektor pohybu) smerníka podľa zadaných
		 * súradníc smerového vektora. Súradnice nemusia byť normalizované,
		 * metóda ich normalizuje.</p>
		 * 
		 * @param x x-ová súradnica smerového vektora
		 * @param y y-ová súradnica smerového vektora
		 * @param z z-ová súradnica smerového vektora
		 */
		public void smer(double x, double y, double z)
		{
			double Î = sqrt(x * x + y * y + z * z);
			Δx = x / Î; Δy = y / Î; Δz = z / Î;
		}

		/**
		 * <p>Nastaví „smer“ (jednotkový vektor pohybu) smerníka podľa
		 * zadaných súradníc smerového vektora. Súradnice nemusia byť
		 * normalizované, metóda ich normalizuje.</p>
		 * 
		 * @param vektor najmenej trojprvkové pole súradníc smerového vektora
		 */
		public void smer(double[] vektor)
		{
			if (null != vektor && vektor.length >= 3)
			{
				double Î = sqrt(vektor[0] * vektor[0] +
					vektor[1] * vektor[1] + vektor[2] * vektor[2]);
				Δx = vektor[0] / Î; Δy = vektor[1] / Î; Δz = vektor[2] / Î;
			}
		}

		/**
		 * <p>Pootočí smerník (zmení orientáciu) o zadané uhly v smere osí x,
		 * y a z. Ide o pootočenie jednotkového vektora pohybu, takže aj keď
		 * je výpočtovo stred otáčania v strede súradnicovej sústavy, výsledný
		 * praktický efekt pootočenia vektora je zmena orientácie smerníka,
		 * ktorý sa od tohto momentu bude pohybovať zmeneným smerom.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 * 
		 * @param Δα hodnota zmeny uhla v smere osi x
		 * @param Δβ hodnota zmeny uhla v smere osi y
		 * @param Δγ hodnota zmeny uhla v smere osi z
		 */
		public void otoč(double Δα, double Δβ, double Δγ)
		{
			double αr = toRadians(Δα);
			double βr = toRadians(Δβ);
			double γr = toRadians(Δγ);

			double sinα = sin(αr);
			double sinβ = sin(βr);
			double sinγ = sin(γr);

			double cosα = cos(αr);
			double cosβ = cos(βr);
			double cosγ = cos(γr);

			double T00 = cosβ * cosγ;
			double T01 = -cosβ * sinγ;
			double T02 = sinβ;

			double T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
			double T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
			double T12 = -sinα * cosβ;

			double T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
			double T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
			double T22 = cosα * cosβ;

			double x1 = (Δx * T00) + (Δy * T01) + (Δz * T02);
			double y1 = (Δx * T10) + (Δy * T11) + (Δz * T12);
			double z1 = (Δx * T20) + (Δy * T21) + (Δz * T22);

			Δx = x1; Δy = y1; Δz = z1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double, double, double) otoč}.</p> */
		public void otoc(double Δα, double Δβ, double Δγ)
		{ otoč(Δα, Δβ, Δγ); }

		/**
		 * <p>Pootočí smerník o uhly v smere osí x, y a z zadané vo forme
		 * poľa. Ak pole obsahuje aspoň tri prvky, tak sa metóda správa
		 * rovnako ako metóda {@link #otoč(double, double, double) otoč(Δα, Δβ,
		 * Δγ)}, ktorej boli poslané tri uhly z prvých troch prvkov poľa. Ak
		 * pole obsahuje len dva prvky, tak je to ekvivalentné volaniu metódy
		 * {@link #otoč(double, double, double) otoč(Δα, Δβ, 0.0)}, kde sú do
		 * prvých dvoch parametrov vložené dva prvky poľa a podobne pri
		 * jednoprvkovom poli ide o volanie ekvivalentné vykonaniu {@link 
		 * #otoč(double, double, double) otoč(Δα, 0.0, 0.0)}.</p>
		 * 
		 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
		 * zložená z troch samostatných transformácií rotácie okolo
		 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
		 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
		 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
		 * 
		 * @param uhly najmenej trojprvkové pole uhlov rotácie okolo osí
		 *     x, y a z
		 */
		public void otoč(double[] uhly)
		{
			if (null != uhly)
			{
				if (uhly.length >= 3)
					otoč(uhly[0], uhly[1], uhly[2]);
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double[]) otoč}.</p> */
		public void otoc(double[] uhly) { otoč(uhly); }

		/**
		 * <p>Pootočí smerník (zmení jeho orientáciu) o uhol Θ okolo osi
		 * prechádzajúcej stredom súradnicovej sústavy (<smer>pretože ide
		 * o zmenu smerového vektora a zjednodušene môžeme povedať, že
		 * tento je lokalizovaný v strede súradnicovej sústavy</smer>)
		 * a orientovanej podľa smerového vektora ⟨u, v, w⟩.</p>
		 * 
		 * @param u x-ová súradnica smerového vektora určujúceho os rotácie
		 * @param v y-ová súradnica smerového vektora určujúceho os rotácie
		 * @param w z-ová súradnica smerového vektora určujúceho os rotácie
		 * @param Θ uhol rotácie smerníka
		 * 
		 * @see #otoč(double)
		 */
		public void otoč(double u, double v, double w, double Θ)
		{
			// TODO testuj…

			// Zdroje:
			// — Rovnaké ako v otočOkolo(a, b, c, u, v, w, Θ, î) – metóda je
			//   jej zjednodušením.

			// Normalizuj vektor (meníme smer, musíme mať istotu):
			double Î = sqrt(u * u + v * v + w * w);
			u /= Î; v /= Î; w /= Î;

			double sinΘ = sin(Θ), cosΘ = cos(Θ);
			double cosΘ_1 = 1 - cosΘ;
			double uΔx_vΔy_wΔz = u * Δx + v * Δy + w * Δz;

			double Δx1 = -u *  uΔx_vΔy_wΔz * cosΘ_1 +
				Δx * cosΘ + (-w * Δy + v * Δz) * sinΘ;
			double Δy1 = -v *  uΔx_vΔy_wΔz * cosΘ_1 +
				Δy * cosΘ + ( w * Δx - u * Δz) * sinΘ;
			double Δz1 = -w * -uΔx_vΔy_wΔz * cosΘ_1 +
				Δz * cosΘ + (-v * Δx + u * Δy) * sinΘ;

			Δx = Δx1; Δy = Δy1; Δz = Δz1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double, double, double, double) otoč}.</p> */
		public void otoc(double u, double v, double w, double Θ)
		{ otoč(u, v, w, Θ); }

		/**
		 * <p>Pootočí smerník (zmení jeho orientáciu) o zadaný uhol okolo
		 * osi prechádzajúcej stredom súradnicovej sústavy a smerujúcej
		 * k poslednému {@linkplain #zálohujSmer() uloženému smeru} smerníka.
		 * Ak je zásobník smerov prázdny, tak metóda nevykoná nič.</p>
		 * 
		 * <p>(Pozri aj informácie v metóde {@link #otoč(double, double,
		 * double, double) otoč(u, v, w, Θ)}.)</p>
		 * 
		 * @param uhol uhol pootočenia smerníka okolo osi prechádzajúcej
		 *     stredom súradnicovej sústavy a smerujúcej podľa orientácie
		 *     prevzatej z vrchola zásobníka smerov
		 * 
		 * @see #otoč(double, double, double, double)
		 */
		public void otoč(double uhol)
		{
			double[] a = dajSmer();
			if (null != a) otoč(a[0], a[1], a[2], uhol);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otoč(double) otoč}.</p> */
		public void otoc(double uhol) { otoč(uhol); }


		/**
		 * <p>Zmení polohu smerníka (orientácia zostane zachovaná) jeho
		 * pootočením o uhol Θ okolo osi prechádzajúcej bodom [a, b, c]
		 * a smerujúcej buď k bodu [u, v, w] (ak je parameter {@code î}
		 * rovný {@code valfalse}), alebo orientovanej v smere jednotkového
		 * vektora ⟨u, v, w⟩ (ak je parameter {@code î} rovný {@code 
		 * valtrue}).</p>
		 * 
		 * <p class="caution"><b>Pozor!</b> Ak je parameter {@code î}
		 * rovný {@code valtrue}, tak hodnoty parametrov u, v a w
		 * musia<small>(!)</small> obsahovať <em>normalizované</em>
		 * súradnice (súradnice jednotkového vektora), inak bude výsledok
		 * nesprávny!</p>
		 * 
		 * @param a x-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param b y-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param c z-ová súradnica bodu, ktorým prechádza os rotácie
		 * @param u x-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param v y-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param w z-ová súradnica bodu alebo vektora určujúceho smer
		 *     osi rotácie
		 * @param Θ uhol rotácie smerníka
		 * @param î rozlišovací parameter toho, či parametre u, v a w určuje
		 *     bod, do ktorého má os rotácie smerovať (hodnota {@code 
		 *     valfalse}), alebo jednotkový vektor (hodnota {@code valtrue})
		 * 
		 * @see #otočOkolo(double)
		 */
		public void otočOkolo(double a, double b, double c, double u, double v,
			double w, double Θ, boolean î)
		{
			// Zdroje:
			// — Weisstein, Eric W. “Unit Vector.” From MathWorld – A Wolfram
			//   Web Resource. [cit. 2018-07-14] ⟨http://mathworld.wolfram.
			//   com/UnitVector.html⟩
			// — Weisstein, Eric W. “Norm.” From MathWorld – A Wolfram Web
			//   Resource. [cit. 2018-07-14] ⟨http://mathworld.wolfram.com/
			//   Norm.html⟩
			// — Murray, Glenn. May 6, 2013. Rotation About an Arbitrary Axis
			//   in 3 Dimensions. [cit. 2018-07-14] ⟨https://sites.google.
			//   com/site/glennmurray/Home/rotation-matrices-and-formulas/
			//   rotation-about-an-arbitrary-axis-in-3-dimensions⟩

			if (!î)
			{
				// Normalizuj vektor:
				u -= a; v -= b; w -= c;
				double Î = sqrt(u * u + v * v + w * w);
				u /= Î; v /= Î; w /= Î;
			}

			double u2 = u * u, v2 = v * v, w2 = w * w;
			double sinΘ = sin(Θ), cosΘ = cos(Θ);
			double cosΘ_1 = 1 - cosΘ;

			double au = a * u, bv = b * v;
			double ux_vy_wz = u * x + v * y + w * z;
			double cw_ux_vy_wz = c * w - ux_vy_wz;

			double x1 = a * (v2 + w2) - u * (bv + cw_ux_vy_wz) * cosΘ_1 +
				x * cosΘ + (-c * v + b * w - w * y + v * z) * sinΘ;
			double y1 = b * (u2 + w2) - v * (au + cw_ux_vy_wz) * cosΘ_1 +
				y * cosΘ + ( c * u - a * w + w * x - u * z) * sinΘ;
			double z1 = c * (u2 + v2) - w * (au + bv - ux_vy_wz) * cosΘ_1 +
				z * cosΘ + (-b * u + a * w - v * x + u * y) * sinΘ;

			x = x1; y = y1; z = z1;
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočOkolo(double, double, double, double, double, double, double, boolean) otoč}.</p> */
		public void otocOkolo(double a, double b, double c, double u, double v,
			double w, double Θ, boolean î) { otočOkolo(a, b, c, u, v, w, Θ, î); }

		/**
		 * <p>Zmení polohu smerníka (orientácia zostane zachovaná) jeho
		 * pootočením o zadaný uhol okolo osi určenej posledným {@linkplain 
		 * #zálohuj() uloženým stavom} smerníka (<small>pri ukladaní stavu
		 * je uchovaná poloha aj smer smerníka, takže na jednoznačné určenie
		 * osi rotácie je k dispozícii dostatočné množstvo údajov</small>).
		 * Ak je zásobník stavov prázdny, tak metóda nevykoná nič.</p>
		 * 
		 * <p>(Pozri aj informácie v metóde {@link #otočOkolo(double,
		 * double, double, double, double, double, double, boolean)
		 * otočOkolo(a, b, c, u, v, w, Θ, î)}.)</p>
		 * 
		 * @param uhol uhol pootočenia smerníka okolo osi prevzatej
		 *     z vrchola zásobníka stavov
		 * 
		 * @see #otočOkolo(double, double, double, double, double,
		 *     double, double, boolean)
		 */
		public void otočOkolo(double uhol)
		{
			double[] a = dajStav();
			if (null != a)
				otočOkolo(a[0], a[1], a[2], a[3], a[4], a[5], uhol, true);
		}

		/** <p><a class="alias"></a> Alias pre {@link #otočOkolo(double) otoč}.</p> */
		public void otocOkolo(double uhol) { otočOkolo(uhol); }


		// „Malý“ omyl v rámci zlého počiatočného stanovenia konceptu smerníka…
			// (Ale využije sa to aspoň pri zistení uhlov (orientácie)
			// všeobecného vektora. Pozri: uhlyK(…).)
			// 
			// Nastaví orientáciu smerníka tak, aby smeroval k zadanému bodu.
			// public void smerNa(double x, double y, double z)
			// {
			// 	// Fungovalo podľa User:Beta v:
			// 	// ⟨https://stackoverflow.com/questions/1251828/calculate-
			// 	// rotations-to-look-at-a-3d-point⟩
			// 	// (Asked asked Aug 9 ’09 at 17:41 by User:Robinicks, Calculate
			// 	// rotations to look at a 3D point? Answered Oct 27 ’10 at
			// 	// 17:54.)
			// 
			// 	x -= this.x;
			// 	y -= this.y;
			// 	z -= this.z;
			// 	α = -toDegrees(atan2(y, z));
			// 	β = toDegrees(atan2(x, hypot(y, z)));
			// 	γ = 0.0;
			// 
			// 	— prepočítajPosun(); —
			// 
			// 	// Prepočíta jednotkový vektor posunu podľa aktuálneho smeru.
			// 	// (Prepočet je zjednodušením všeobecných transformácií
			// 	// použitých aj v metódach Bod.transformuj() a prepočítajT() –
			// 	// ide v podstate o rotáciu jednotkového vektora paralelného
			// 	// s osou z.)
			// 	private void — prepočítajPosun() —
			// 	{
			// 		double αr = toRadians(α), βr = toRadians(β), cosβ = cos(βr);
			// 		Δx = sin(βr); Δy = -sin(αr) * cosβ; Δz = cos(αr) * cosβ;
			// 	}
			// }


		/**
		 * <p>Nastaví orientáciu smerníka tak, aby smeroval k zadanému
		 * bodu.</p>
		 * 
		 * @param x x-ová súradnica cieľového bodu
		 * @param y y-ová súradnica cieľového bodu
		 * @param z z-ová súradnica cieľového bodu
		 */
		public void smerNa(double x, double y, double z)
		{
			x -= this.x; y -= this.y; z -= this.z;
			double Î = sqrt(x * x + y * y + z * z);
			Δx = x / Î; Δy = y / Î; Δz = z / Î;
		}


		/**
		 * <p>Vloží do vnútorného zásobníka stavu smerníka jeho aktuálnu
		 * polohu aj smer. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohuj()
		{
			try
			{
				return zásobník.add(new double[]
					{x, y, z, Δx, Δy, Δz});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohuj() zálohuj}.</p> */
		public boolean zalohuj() { return zálohuj(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka stavu smerníka
		 * poslednú uloženú polohu aj smer. (Ak metóda uspeje, tak vráti
		 * {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnov()
		{
			try
			{
				double[] a = zásobník.pop();
				x = a[0]; y = a[1]; z = a[2];
				Δx = a[3]; Δy = a[4]; Δz = a[5];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka stavov smerníka vo forme šesťprvkového
		 * poľa [x, y, z, Δx, Δy, Δz]. Ak je zásobník prázdny alebo nastane
		 * iné zlyhanie, tak metóda vráti hodnotu {@code valnull}. Zásobník
		 * zostane nezmenený.</p>
		 * 
		 * @return šesťprvkové pole stavu (x, y, z, Δx, Δy, Δz) z vrchola
		 *     zásobníka stavov alebo {@code valnull}, ak je zásobník prázdny
		 */
		public double[] dajStav()
		{
			try
			{
				double[] a = zásobník.peek();
				return new double[] {a[0], a[1], a[2], a[3], a[4], a[5]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník stavov smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdni()
		{
			try
			{
				int počet = zásobník.size();
				zásobník.clear();
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdni() vyprázdni}.</p> */
		public int vyprazdni() { return vyprázdni(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka stavov smerníka.
		 * (Porovnaj s metódou {@link #vyprázdni() vyprázdni}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymaž()
		{
			try
			{
				int počet = zásobník.size();
				zásobník.pop();
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
		public int vymaz() { return vymaž(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka stavov smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymaž(int koľko)
		{
			try
			{
				int počet = zásobník.size();
				if (koľko > počet) koľko = počet;
				zásobník.setSize(počet - koľko);
				počet -= zásobník.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymaž(int) vymaž}.</p> */
		public int vymaz(int koľko) { return vymaž(koľko); }


		/**
		 * <p>Vloží do vnútorného zásobníka polôh smerníka jeho aktuálnu
		 * polohu. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohujPolohu()
		{
			try
			{
				return zásobníkPolôh.add(new double[] {x, y, z});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujPolohu() zálohujPolohu}.</p> */
		public boolean zalohujPolohu() { return zálohujPolohu(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka polôh smerníka poslednú
		 * uloženú polohu. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnovPolohu()
		{
			try
			{
				double[] a = zásobníkPolôh.pop();
				x = a[0]; y = a[1]; z = a[2];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka polôh smerníka vo forme trojprvkového
		 * poľa [x, y, z]. Ak je zásobník prázdny alebo nastane iné zlyhanie,
		 * tak metóda vráti hodnotu {@code valnull}. Zásobník zostane
		 * nezmenený.</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] dajPolohu()
		{
			try
			{
				double[] a = zásobníkPolôh.peek();
				return new double[] {a[0], a[1], a[2]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník polôh smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdniPolohy()
		{
			try
			{
				int počet = zásobníkPolôh.size();
				zásobníkPolôh.clear();
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniPolohy() vyprázdniPolohy}.</p> */
		public int vyprazdniPolohy() { return vyprázdniPolohy(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka polôh smerníka. (Porovnaj
		 * s metódou {@link #vyprázdniPolohy() vyprázdniPolohy}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažPolohu()
		{
			try
			{
				int počet = zásobníkPolôh.size();
				zásobníkPolôh.pop();
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPolohu() vymažPolohu}.</p> */
		public int vymazPolohu() { return vymažPolohu(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka polôh smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažPolohy(int koľko)
		{
			try
			{
				int počet = zásobníkPolôh.size();
				if (koľko > počet) koľko = počet;
				zásobníkPolôh.setSize(počet - koľko);
				počet -= zásobníkPolôh.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažPolohy(int) vymažPolohy}.</p> */
		public int vymazPolohy(int koľko) { return vymažPolohy(koľko); }


		/**
		 * <p>Vloží do vnútorného zásobníka smerov smerníka jeho aktuálny smer.
		 * (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean zálohujSmer()
		{
			try
			{
				return zásobníkSmerov.add(new double[] {Δx, Δy, Δz});
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #zálohujSmer() zálohujSmer}.</p> */
		public boolean zalohujSmer() { return zálohujSmer(); }

		/**
		 * <p>Vyberie (obnoví) z vnútorného zásobníka smerov smerníka posledný
		 * uložený smer. (Ak metóda uspeje, tak vráti {@code valtrue}.)</p>
		 * 
		 * @return {@code valtrue} pri úspechu; {@code valfalse} v opačnom
		 *     prípade
		 */
		public boolean obnovSmer()
		{
			try
			{
				double[] a = zásobníkSmerov.pop();
				Δx = a[0]; Δy = a[1]; Δz = a[2];
				return true;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return false;
			}
		}

		/**
		 * <p>Vráti vrchol zásobníka smerov smerníka vo forme trojprvkového
		 * poľa [Δx, Δy, Δz]. Ak je zásobník prázdny alebo nastane iné
		 * zlyhanie, tak metóda vráti hodnotu {@code valnull}. Zásobník
		 * zostane nezmenený.</p>
		 * 
		 * @return trojprvkové pole súradníc
		 */
		public double[] dajSmer()
		{
			try
			{
				double[] a = zásobníkSmerov.peek();
				return new double[] {a[0], a[1], a[2]};
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return null;
			}
		}

		/**
		 * <p>Vyprázdni vnútorný zásobník smerov smerníka. Metóda vráti počet
		 * záznamov odstránených zo zásobníka. Ak vznikne iné zlyhanie,
		 * tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vyprázdniSmery()
		{
			try
			{
				int počet = zásobníkSmerov.size();
				zásobníkSmerov.clear();
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniSmery() vyprázdniSmery}.</p> */
		public int vyprazdniSmery() { return vyprázdniSmery(); }

		/**
		 * <p>Odstráni posledný záznam zo zásobníka smerov smerníka. (Porovnaj
		 * s metódou {@link #vyprázdniSmery() vyprázdniSmery}.) Metóda
		 * vráti počet záznamov odstránených zo zásobníka. Ak vznikne iné
		 * zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažSmer()
		{
			try
			{
				int počet = zásobníkSmerov.size();
				zásobníkSmerov.pop();
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSmer() vymažSmer}.</p> */
		public int vymazSmer() { return vymažSmer(); }

		/**
		 * <p>Odstráni z vnútorného zásobníka smerov smerníka zadaný počet
		 * posledných záznamov. Metóda vráti počet záznamov odstránených
		 * zo zásobníka. Ak vznikne iné zlyhanie, tak metóda vráti −1.</p>
		 * 
		 * @param koľko požadovaný počet záznamov, ktoré majú byť odstránené
		 * @return počet skutočne odstránených záznamov zo zásobníka alebo
		 *     −1 v prípade, že nastala chyba
		 */
		public int vymažSmery(int koľko)
		{
			try
			{
				int počet = zásobníkSmerov.size();
				if (koľko > počet) koľko = počet;
				zásobníkSmerov.setSize(počet - koľko);
				počet -= zásobníkSmerov.size();
				return počet;
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				return -1;
			}
		}

		/** <p><a class="alias"></a> Alias pre {@link #vymažSmery(int) vymažSmery}.</p> */
		public int vymazSmery(int koľko) { return vymažSmery(koľko); }


		/**
		 * <p>Vyprázdni všetky vnútorné zásobníky smerníka. Metóda vráti
		 * trojprvkové pole, ktoré bude obsahovať, koľko záznamov bolo
		 * odstránených zo zásobníka stavov, polôh a smerov. Hodnota
		 * −1 niektorého z prvkov znamená, že vyprázdňovanie konkrétneho
		 * zásobníka zlyhalo.</p>
		 * 
		 * @return trojprvkové pole počtov skutočne odstránených záznamov
		 *     v poradí: stavy, polohy a smery; hodnota −1 prvka značí chybu
		 */
		public int[] vyprázdniVšetko()
		{
			int stavy = vyprázdni();
			int polohy = vyprázdniPolohy();
			int smery = vyprázdniSmery();
			return new int[] {stavy, polohy, smery};
		}

		/** <p><a class="alias"></a> Alias pre {@link #vyprázdniVšetko() vyprázdniVšetko}.</p> */
		public int[] vyprazdniVsetko() { return vyprázdniVšetko(); }

		/**
		 * <p>Vloží do roja bod na aktuálnej polohe smerníka s nastavenými
		 * príznakmi kreslenia spoja a objektu na základe hodnôt príznakov
		 * smerníka vkladajSpoje a vkladajObjekty. Nový bod je návratovou
		 * hodnotou tejto metódy.</p>
		 * 
		 * @return inštancia bodu, ktorý bol práve pridaný do roja
		 */
		public Bod pridajBod()
		{
			Bod bod = new Bod();
			bod.x0 = x;
			bod.y0 = y;
			bod.z0 = z;
			bod.spoj = vkladajSpoje;
			bod.zobraz = vkladajObjekty;
			body.pridaj(bod);
			poradieKreslenia.pridaj(bod);
			transformuj = true;
			prepočítaj = true;
			return bod;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link Smerník Smerník}.</p> */
	public class Smernik extends Smerník {}

	/**
	 * <p>Inštancia smerníka. Podrobnosti v dokumentácii triedy
	 * {@link Smerník Smerník}.</p>
	 */
	public final Smernik smerník = new Smernik();

	/** <p><a class="alias"></a> Alias pre {@link #smerník smerník}.</p> */
	public final Smernik smernik = smerník;


	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla alfa metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla alfa metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int ALFA = 1;

	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla beta metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla beta metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int BETA = 2;

	/**
	 * <p>Konštanta slúžiaca na určenie čítania uhla gama metódou
	 * {@link #uhol(int) uhol(ktorý)} alebo nastavenia uhla gama metódou
	 * {@link #uhol(int, double) uhol(ktorý, hodnota)}.</p>
	 */
	public final static int GAMA = 4;


	/**
	 * <p>Toto je kresliaci robot roja. Ak nie je nastavený, tak roj nemôže
	 * byť kreslený a pri individuálnom pokuse o nakreslenie bodu roja nastane
	 * chyba. Tvar tohto robota určuje predvolený tvar objektov na polohách
	 * bodov roja. Vlastnosti pera ovplyvňujú kreslenie spojov medzi bodmi
	 * roja – hrúbka určuje predvolenú hrúbku a poloha to, či budú všetky
	 * spoje paušálne nakreslené alebo nie.</p>
	 */
	public GRobot kresli = null;


	/**
	 * <p>Inštancia aktuálne spracúvaného bodu. Počas kreslenia spojníc
	 * a objektov alebo počas exportu do SVG formátu je do tohto atribútu
	 * ukladaná inštancia aktuálne spracúvaného bodu.</p>
	 */
	public Bod bod = null;


	// Vnútorný príznak toho, že naposledy bol kreslený bod s neplatnou
	// súradnicou (alebo ide o prvý kreslený bod).
	private boolean neplatnáSúradnica = false;

	// Vnútorné zoznamy bodov. Prvý zoznam slúži na prechádzanie bodov
	// v poradí ich pridania, čo je dôležité pri kreslení spojov a druhý
	// zoznam je pravidelne zoraďovaný podľa vzdialenosti bodov od kamery,
	// čo je dôležité pri kreslení telies umiestnených na súradniciach
	// bodov roja.
	private final Zoznam<Bod> body = new Zoznam<>();
	private final Zoznam<Bod> poradieKreslenia = new Zoznam<>(); // „Z-bufer“
		// (Poznámka: Pomenovanie z-bufer (angl. z-buffer) je prevzaté
		//     („požičané“) z názvu zásobníka bodov (pixelov) v 3D grafike
		//     slúžiaceho na rýchle rozpoznanie toho, ktorý z kreslených
		//     bodov obrazu je viac v popredí. Tento z-bufer má v podstate
		//     rovnaký účel, ale pricíp je odlišný (ako je spomenuté vyššie).
		//     Tiež body roja nie sú pixely, ale 3D lokality s množstvom
		//     rôznych parametrov.)


	// Atribúty súvisiace s orientáciou celého roja (ktorá sa dá vnímať aj
	// ako orientácia kamery) a príznak nevyhnuntosti prepočítania hodnôt
	// konečných súradníc bodov roja (x2, y2, z2). Konkrétne: xs, ys a zs
	// určujú stred otáčania roja (kamery) a α, β a γ sú uhly pootočenia
	// roja (kamery).
	private double xs = 0.0, ys = 0.0, zs = 0.0;
	private double α = -110.0, β = -360.0, γ = 45.0;
	private boolean transformuj = false;


	// Atribúty súvisiace s kamerou a príznak nevyhnutnosti prepočítania
	// cieľových (v súvislosti s kreslením) súradníc bodov roja (x3, y3, z3).
	private double mierka = 1000.0;
	private double kx = 0.0, ky = -125.0, kz = 200.0;
	private boolean prepočítaj = false;


	// Koeficienty transformácií a príznak nevyhnutnosti ich prepočítania.
	private double T00 =  0.707106781186548;
	private double T01 = -0.707106781186548;
	private double T02 =  0.0;

	private double T10 = -0.241844762647975;
	private double T11 = -0.241844762647975;
	private double T12 =  0.939692620785908;

	private double T20 = -0.664463024388675;
	private double T21 = -0.664463024388675;
	private double T22 = -0.342020143325669;

	private boolean prepočítajT = false;


	// Prepočíta koeficienty transformácií.
	private void prepočítajT()
	{
		/* Transformačná matica:
		(cosβ.cosγ                  | −cosβ.sinγ                 | sinβ      )
		(sinα.sinβ.cosγ + cosα.sinγ | cosα.cosγ − sinα.sinβ.sinγ | −sinα.cosβ)
		(sinα.sinγ − cosα.sinβ.cosγ | sinα.cosγ + cosα.sinβ.sinγ | cosα.cosβ )
		*/
		if (prepočítajT)
		{
			// „Ľavoruký“ (left-handed) systém:
			double αr = toRadians(α);
			double βr = toRadians(β);
			double γr = toRadians(γ);

			// „Pravoruký“ (right-handed) systém:
			// double αr = toRadians(-β);
			// double βr = toRadians(-α);
			// double γr = toRadians(-γ);

			double sinα = sin(αr);
			double sinβ = sin(βr);
			double sinγ = sin(γr);

			double cosα = cos(αr);
			double cosβ = cos(βr);
			double cosγ = cos(γr);

			T00 = cosβ * cosγ;
			T01 = -cosβ * sinγ;
			T02 = sinβ;

			T10 = (sinα * sinβ * cosγ) + (cosα * sinγ);
			T11 = (cosα * cosγ) - (sinα * sinβ * sinγ);
			T12 = -sinα * cosβ;

			T20 = (sinα * sinγ) - (cosα * sinβ * cosγ);
			T21 = (sinα * cosγ) + (cosα * sinβ * sinγ);
			T22 = cosα * cosβ;

			prepočítajT = false;
		}
	}


	/**
	 * <p>Konštruktor roja. Prijíma inštanciu grafického robota, ktorý bude
	 * kresličom roja. Bez kresliča roj nemôže byť nakreslený.</p>
	 * 
	 * @param kreslič kreslič roja
	 */
	public Roj(GRobot kreslič)
	{
		this.kresli = kreslič;
	}


	/**
	 * <p>Pridá nový bod do roja. Bod bude mať hodnoty všetkých atribútov
	 * nastavené na predvolené. Návratová hodnota tejto metódy je inštancia
	 * nového bodu, ktorá môže byť použitá na nastavenie všetkých vlastností
	 * bodu.</p>
	 */
	public Bod pridajBod()
	{
		Bod bod = new Bod();
		body.pridaj(bod);
		poradieKreslenia.pridaj(bod);
		transformuj = true;
		prepočítaj = true;
		return bod;
	}

	/**
	 * <p>Odstráni bod z roja.</p>
	 * 
	 * @param bod inštancia bodu, ktorý má byť z roja odstránený
	 */
	public void vymažBod(Bod bod)
	{
		body.odober(bod);
		poradieKreslenia.odober(bod);
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymažBod(Bod) vymažBod}.</p> */
	public void vymazBod(Bod bod) { vymažBod(bod); }

	/**
	 * <p>Odstráni všetky body z roja.</p>
	 */
	public void vymaž()
	{
		body.vymaž();
		poradieKreslenia.vymaž();
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #vymaž() vymaž}.</p> */
	public void vymaz() { vymaž(); }

	/**
	 * <p>Vráti zoznam bodov roja. Zoznam je určený predovšetkým na
	 * prechádzanie a úpravu vlastností jednotlivých bodov. Vymazaním
	 * bodu z tohto zoznamu sa bod nevymaže, len sa stratí možnosť
	 * prístupu k nemu cez tento zoznam a funkčnosť bodu bude výrazne
	 * obmedzená. Na vymazanie bodu slúži metóda {@link #vymažBod(Roj.Bod)
	 * vymažBod}.</p>
	 */
	public Zoznam<Bod> body() { return body; }

	/**
	 * <p>Vráti zoznam bodov roja zoradený podľa poradia kreslenia.
	 * Zoznam je určený predovšetkým na prechádzanie na účely preslenia
	 * objektov. Vymazaním bodu z tohto zoznamu sa bod nevymaže, len sa
	 * stratí možnosť prístupu k nemu cez tento zoznam a funkčnosť bodu
	 * bude obmedzená. Na vymazanie bodu z roja slúži metóda {@link 
	 * #vymažBod(Roj.Bod) vymažBod}.</p>
	 */
	public Zoznam<Bod> poradieKreslenia() { return poradieKreslenia; }


	/**
	 * <p>Vráti aktuálne hodnoty uhlov alfa (α), beta (β) a gama (γ) roja
	 * vo forme trojprvkového poľa [α, β, γ].</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálne hodnoty uhlov roja v trojprvkovom poli
	 * 
	 * @see #nastavUhly(double[])
	 */
	public double[] uhly() { return new double[] {α, β, γ}; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla alfa (vnútorne značeného
	 * {@code α}). Tento uhol je spätý s rotáciou okolo osi x, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla alfa
	 * 
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholAlfa() { return α; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla beta (vnútorne značeného
	 * {@code β}). Tento uhol je spätý s rotáciou okolo osi y, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla beta
	 * 
	 * @see #uholAlfa()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholBeta() { return β; }

	/**
	 * <p>Vráti aktuálnu hodnotu uhla gama (vnútorne značeného
	 * {@code γ}). Tento uhol je spätý s rotáciou okolo osi z, pričom
	 * sa do úvahy berie aj aktuálny {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stred otáčania}.
	 * Rotácia zmenou uhlov alfa (α), beta (β) a gama (γ) je v podstate
	 * rotáciou kamery roja.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @return aktuálna hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uholGama() { return γ; }

	/**
	 * <p>Nastaví novú hodnotu uhlu alfa (vnútorne značeného {@code α}).
	 * Pozri aj opis metódy {@link #uholAlfa() uholAlfa()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla alfa
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholAlfa(double hodnota)
	{
		α = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú hodnotu uhlu beta (vnútorne značeného {@code β}).
	 * Pozri aj opis metódy {@link #uholBeta() uholBeta()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla beta
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholBeta(double hodnota)
	{
		β = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú hodnotu uhlu gama (vnútorne značeného {@code γ}).
	 * Pozri aj opis metódy {@link #uholGama() uholGama()}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param hodnota nová hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uholGama(double hodnota)
	{
		γ = hodnota;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Vráti hodnotu zadaného uhla, pričom ak zadaná konštanta obsahuje
	 * kombináciu príznakov viacerých uhlov, tak prioritne vracia uhol
	 * {@link #ALFA ALFA}, potom {@link #BETA BETA} a nakoniec
	 * {@link #GAMA GAMA}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param ktorý konštanta určujúca, ktorý uhol má byť vrátený
	 *     ({@link #ALFA ALFA}, {@link #BETA BETA}, {@link #GAMA GAMA})
	 * @return hodnota uhla určeného parametrom {@code ktorý}
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public double uhol(int ktorý)
	{
		if (0 != (ktorý & ALFA)) return α;
		if (0 != (ktorý & BETA)) return β;
		if (0 != (ktorý & GAMA)) return γ;
		return 0.0;
	}

	/**
	 * <p>Nastaví jeden alebo viacero uhlov na stanovenú hodnotu.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param ktorý konštanta určujúca, ktorý uhol alebo uhly majú byť
	 *     nastavené; parameter môže byť kombináciou konštánt {@link #ALFA
	 *     ALFA}, {@link #BETA BETA} a {@link #GAMA GAMA})
	 * @param hodnota nová hodnota uhla alebo uhlov určených parametrom
	 *     {@code ktorý}
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #nastavUhly(double, double, double)
	 * @see #pootoč(double, double, double)
	 */
	public void uhol(int ktorý, double hodnota)
	{
		if (0 != (ktorý & ALFA)) α = hodnota;
		if (0 != (ktorý & BETA)) β = hodnota;
		if (0 != (ktorý & GAMA)) γ = hodnota;

		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví hodnoty všetkých troch uhlov {@linkplain #uholAlfa() alfa},
	 * {@linkplain #uholBeta() beta} a {@linkplain #uholGama() gama}. (Pozri
	 * aj {@linkplain #nastavStredOtáčania(double, double, double) stred
	 * otáčania}.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param nα nová hodnota uhla alfa
	 * @param nβ nová hodnota uhla beta
	 * @param nγ nová hodnota uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #pootoč(double, double, double)
	 */
	public void nastavUhly(double nα, double nβ, double nγ)
	{
		α = nα; β = nβ; γ = nγ;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví hodnoty uhlov roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie orientácie roja (napríklad
	 * v súvislosti zo zálohou a obnovou stavu roja; pozri aj {@link 
	 * #uhly() uhly}). Ak je zadané pole aspoň šesťprvkové,
	 * tak metóda pracuje s druhou trojicou prvkov poľa. Ak je pole
	 * aspoň trojprvkové, tak metóda berie do úvahy jeho prvé tri
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná
	 * nič.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param pole pole na základe ktorého budú nastavené uhly roja
	 * 
	 * @see #uhly()
	 * @see #nastavUhly(double, double, double)
	 */
	public void nastavUhly(double[] pole)
	{
		if (null != pole && pole.length >= 3)
		{
			if (pole.length >= 6)
				{ α = pole[3]; β = pole[4]; γ = pole[5]; }
			else
				{ α = pole[0]; β = pole[1]; γ = pole[2]; }
			transformuj = true;
			prepočítajT = true;
			prepočítaj = true;
		}
	}

	/**
	 * <p>Pozmení hodnoty všetkých troch uhlov {@linkplain #uholAlfa() alfa},
	 * {@linkplain #uholBeta() beta} a {@linkplain #uholGama() gama}.</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param Δα miera zmeny hodnoty uhla alfa
	 * @param Δβ miera zmeny hodnoty uhla beta
	 * @param Δγ miera zmeny hodnoty uhla gama
	 * 
	 * @see #uholAlfa()
	 * @see #uholBeta()
	 * @see #uholGama()
	 * @see #uholAlfa(double)
	 * @see #uholBeta(double)
	 * @see #uholGama(double)
	 * @see #uhol(int)
	 * @see #uhol(int, double)
	 * @see #nastavUhly(double, double, double)
	 */
	public void pootoč(double Δα, double Δβ, double Δγ)
	{
		α += Δα; β += Δβ; γ += Δγ;
		transformuj = true;
		prepočítajT = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #pootoč(double, double, double) pootoč}.</p> */
	public void pootoc(double Δα, double Δβ, double Δγ)
	{ pootoč(Δα, Δβ, Δγ); }

	/**
	 * <p>Pozmení hodnoty uhlov roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu orientácie roja. Ak je zadané pole
	 * aspoň šesťprvkové, tak metóda pracuje s druhou trojicou prvkov
	 * poľa. Ak je pole aspoň trojprvkové, tak metóda berie do úvahy
	 * jeho prvé tri prvky. (Ak má pole menší počet prvkov, tak metóda
	 * nevykoná nič.)</p>
	 * 
	 * <p class="remark"><b>Poznámka:</b> Transformácia pootočenia je
	 * zložená z troch samostatných transformácií rotácie okolo
	 * jednotlivých osí súradnicovej sústavy. Skladanie transformácií
	 * nie je komutatívne, takže výsledok rotácie nie je intuitívny
	 * a môže sa líšiť od (intuitívne) predpokladaného stavu.</p>
	 * 
	 * @param pole pole na základe ktorého budú pozmenené uhly roja
	 * 
	 * @see #nastavUhly(double[])
	 * @see #pootoč(double, double, double)
	 */
	public void pootoč(double[] pole)
	{
		if (null != pole && pole.length >= 3)
		{
			if (pole.length >= 6)
				{ α += pole[3]; β += pole[4]; γ += pole[5]; }
			else
				{ α += pole[0]; β += pole[1]; γ += pole[2]; }
			transformuj = true;
			prepočítajT = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #pootoč(double[]) pootoč}.</p> */
	public void pootoc(double[] pole) { pootoč(pole); }


	/**
	 * <p>Vráti aktuálny {@linkplain #nastavStredOtáčania(double, double,
	 * double) stred otáčania} vo forme trojprvkového poľa [xs, ys, zs].</p>
	 * 
	 * @return aktuálny {@linkplain #nastavStredOtáčania(double, double,
	 *     double) stred otáčania}
	 * 
	 * @see #nastavStredOtáčania(double[])
	 */
	public double[] stredOtáčania() { return new double[] {xs, ys, zs}; }

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčania() stredOtáčania}.</p> */
	public double[] stredOtacania() { return stredOtáčania(); }

	/**
	 * <p>Vráti x-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota x-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaX()
	{
		return xs;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaX() stredOtáčaniaX}.</p> */
	public double stredOtacaniaX()
	{ return stredOtáčaniaX(); }

	/**
	 * <p>Vráti y-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota y-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaY()
	{
		return ys;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaY() stredOtáčaniaY}.</p> */
	public double stredOtacaniaY()
	{ return stredOtáčaniaY(); }

	/**
	 * <p>Vráti z-ovú súradnicu {@linkplain #nastavStredOtáčania(double,
	 * double, double) stredu otáčania}.</p>
	 * 
	 * @return aktuálna hodnota z-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public double stredOtáčaniaZ()
	{
		return zs;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaZ() stredOtáčaniaZ}.</p> */
	public double stredOtacaniaZ()
	{ return stredOtáčaniaZ(); }

	/**
	 * <p>Nastaví novú hodnotu x-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota x-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaX(double hodnota)
	{
		xs = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaX(double) stredOtáčaniaX}.</p> */
	public void stredOtacaniaX(double hodnota)
	{ stredOtáčaniaX(hodnota); }

	/**
	 * <p>Nastaví novú hodnotu y-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota y-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaY(double hodnota)
	{
		ys = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaY(double) stredOtáčaniaY}.</p> */
	public void stredOtacaniaY(double hodnota)
	{ stredOtáčaniaY(hodnota); }

	/**
	 * <p>Nastaví novú hodnotu z-ovej súradnice {@linkplain 
	 * #nastavStredOtáčania(double, double, double) stredu otáčania}.</p>
	 * 
	 * @param hodnota nová hodnota z-ovej súradnice {@linkplain 
	 *     #nastavStredOtáčania(double, double, double) stredu otáčania}
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void stredOtáčaniaZ(double hodnota)
	{
		zs = hodnota;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #stredOtáčaniaZ(double) stredOtáčaniaZ}.</p> */
	public void stredOtacaniaZ(double hodnota)
	{ stredOtáčaniaZ(hodnota); }

	/**
	 * <p>Nastaví nové súradnice stredu otáčania. (Pozri aj uhly {@linkplain 
	 * #uholAlfa() alfa}, {@linkplain #uholBeta() beta} a {@linkplain 
	 * #uholGama() gama}.)</p>
	 * 
	 * @param nxs nová hodnota x-ovej súradnice stredu otáčania
	 * @param nys nová hodnota y-ovej súradnice stredu otáčania
	 * @param nzs nová hodnota z-ovej súradnice stredu otáčania
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void nastavStredOtáčania(double nxs, double nys, double nzs)
	{
		xs = nxs; ys = nys; zs = nzs;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #nastavStredOtáčania(double, double, double) nastavStredOtáčania}.</p> */
	public void nastavStredOtacania(double nxs, double nys, double nzs)
	{ nastavStredOtáčania(nxs, nys, nzs); }

	/**
	 * <p>Nastaví stred otáčania roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie polohy stredu otáčania roja berúc
	 * do úvahy spôsob práce {@linkplain #smerník smerníka}. Ak je zadané
	 * pole aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu určujúce novú polohu stredu otáčania roja
	 * 
	 * @see #stredOtáčania()
	 * @see #nastavStredOtáčania(double, double, double)
	 */
	public void nastavStredOtáčania(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			xs = bod[0]; ys = bod[1]; zs = bod[2];
			transformuj = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #nastavStredOtáčania(double[]) nastavStredOtáčania}.</p> */
	public void nastavStredOtacania(double[] bod) { nastavStredOtáčania(bod); }

	/**
	 * <p>Posunie súradnice {@linkplain #nastavStredOtáčania(double, double,
	 * double) stredu otáčania}. (Pozri aj uhly {@linkplain #uholAlfa()
	 * alfa}, {@linkplain #uholBeta() beta} a {@linkplain #uholGama()
	 * gama}.)</p>
	 * 
	 * @param Δxs miera zmeny x-ovej súradnice stredu otáčania
	 * @param Δys miera zmeny y-ovej súradnice stredu otáčania
	 * @param Δzs miera zmeny z-ovej súradnice stredu otáčania
	 * 
	 * @see #stredOtáčaniaX()
	 * @see #stredOtáčaniaY()
	 * @see #stredOtáčaniaZ()
	 * @see #stredOtáčaniaX(double)
	 * @see #stredOtáčaniaY(double)
	 * @see #stredOtáčaniaZ(double)
	 * @see #nastavStredOtáčania(double, double, double)
	 */
	public void posuňStredOtáčania(double Δxs, double Δys, double Δzs)
	{
		xs += Δxs; ys += Δys; zs += Δzs;
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňStredOtáčania(double, double, double) posuňStredOtáčania}.</p> */
	public void posunStredOtacania(double Δxs, double Δys, double Δzs)
	{ posuňStredOtáčania(Δxs, Δys, Δzs); }

	/**
	 * <p>Posunie stred otáčania roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu polohy stredu otáčania roja berúc
	 * do úvahy spôsob práce {@linkplain #smerník smerníka}. Ak je zadané
	 * pole aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho
	 * prvky. (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod trojica polohových súradníc, o ktoré bude posunutá
	 *     poloha stredu otáčania roja
	 * 
	 * @see #nastavStredOtáčania(double[])
	 * @see #posuňStredOtáčania(double, double, double)
	 */
	public void posuňStredOtáčania(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			xs += bod[0]; ys += bod[1]; zs += bod[2];
			transformuj = true;
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňStredOtáčania(double[]) posuňStredOtáčania}.</p> */
	public void posunStredOtacania(double[] bod) { posuňStredOtáčania(bod); }


	/**
	 * <p>Vráti aktuálnu polohu {@linkplain #nastavKameru(double, double,
	 * double) kamery} vo forme trojprvkového poľa [kx, ky, kz].</p>
	 * 
	 * @return aktuálna poloha {@linkplain #nastavKameru(double, double,
	 *     double) kamery}
	 * 
	 * @see #nastavKameru(double[])
	 */
	public double[] kamera() { return new double[] {kx, ky, kz}; }

	/**
	 * <p>Vráti aktuálnu x-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota x-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraX()
	{
		return kx;
	}

	/**
	 * <p>Vráti aktuálnu y-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota y-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraY()
	{
		return ky;
	}

	/**
	 * <p>Vráti aktuálnu z-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @return aktuálna hodnota z-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public double kameraZ()
	{
		return kz;
	}

	/**
	 * <p>Nastaví novú x-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota x-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraX(double hodnota)
	{
		kx = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú y-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota y-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraY(double hodnota)
	{
		ky = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú z-ovú súradnicu polohy {@linkplain 
	 * #nastavKameru(double, double, double) kamery}.</p>
	 * 
	 * @param hodnota nová hodnota z-ovej súradnice polohy {@linkplain 
	 *     #nastavKameru(double, double, double) kamery}
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void kameraZ(double hodnota)
	{
		kz = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví novú polohu kamery.</p>
	 * 
	 * @param nkx nová hodnota x-ovej súradnice polohy kamery
	 * @param nky nová hodnota y-ovej súradnice polohy kamery
	 * @param nkz nová hodnota z-ovej súradnice polohy kamery
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #posuňKameru(double, double, double)
	 */
	public void nastavKameru(double nkx, double nky, double nkz)
	{
		kx = nkx; ky = nky; kz = nkz;
		prepočítaj = true;
	}

	/**
	 * <p>Nastaví polohu kamery roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť nastavenie polohy kamery roja berúc do úvahy
	 * spôsob práce {@linkplain #smerník smerníka}. Ak je zadané pole
	 * aspoň trojprvkové, tak metóda berie do úvahy prvé tri jeho prvky.
	 * (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu, na ktoré bude nastavená poloha kamery
	 * 
	 * @see #kamera()
	 * @see #nastavKameru(double, double, double)
	 */
	public void nastavKameru(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			kx = bod[0]; ky = bod[1]; kz = bod[2];
			prepočítaj = true;
		}
	}

	/**
	 * <p>Posunie polohu kamery.</p>
	 * 
	 * @param Δkx miera zmeny hodnoty x-ovej súradnice polohy kamery
	 * @param Δky miera zmeny hodnoty y-ovej súradnice polohy kamery
	 * @param Δkz miera zmeny hodnoty z-ovej súradnice polohy kamery
	 * 
	 * @see #kameraX()
	 * @see #kameraY()
	 * @see #kameraZ()
	 * @see #kameraX(double)
	 * @see #kameraY(double)
	 * @see #kameraZ(double)
	 * @see #nastavKameru(double, double, double)
	 */
	public void posuňKameru(double Δkx, double Δky, double Δkz)
	{
		kx += Δkx; ky += Δky; kz += Δkz;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňKameru(double, double, double) posuňKameru}.</p> */
	public void posunKameru(double Δkx, double Δky, double Δkz)
	{ posuňKameru(Δkx, Δky, Δkz); }

	/**
	 * <p>Posunie polohu kamery roja na základe hodnôt zadaného poľa.
	 * Metóda má zjednodušiť zmenu polohy kamery roja berúc do úvahy
	 * spôsob práce {@linkplain #smerník smerníka}. Ak je zadané pole
	 * aspoň trojprvkové, metóda berie do úvahy prvé tri jeho prvky.
	 * (Ak má pole menší počet prvkov, tak metóda nevykoná nič.)</p>
	 * 
	 * @param bod súradnice bodu, o ktoré bude posunutá poloha kamery
	 * 
	 * @see #nastavKameru(double[])
	 * @see #posuňKameru(double, double, double)
	 */
	public void posuňKameru(double[] bod)
	{
		if (null != bod && bod.length >= 3)
		{
			kx += bod[0]; ky += bod[1]; kz += bod[2];
			prepočítaj = true;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #posuňKameru(double[]) posuňKameru}.</p> */
	public void posunKameru(double[] bod) { posuňKameru(bod); }


	/**
	 * <p>Vráti aktuálnu hodnotu mierky roja. Mierka vo veľkej miere
	 * ovplyvňuje zobrazenie roja. Ak je nesprávne nastavená, výsledok
	 * je skreslený.</p>
	 * 
	 * @return aktuálna hodnota mierky zobrazenia roja
	 * 
	 * @see #mierka(double)
	 * @see #zmeňMierku(double)
	 */
	public double mierka()
	{
		return mierka;
	}

	/**
	 * <p>Nastaví novú hodnotu mierky roja. Pozri aj opis metódy {@link 
	 * #mierka() mierka()}.</p>
	 * 
	 * @param hodnota nová hodnota mierky zobrazenia roja
	 * 
	 * @see #mierka()
	 * @see #zmeňMierku(double)
	 */
	public void mierka(double hodnota)
	{
		mierka = hodnota;
		prepočítaj = true;
	}

	/**
	 * <p>Upraví hodnotu mierky roja. Pozri aj opis metódy {@link 
	 * #mierka() mierka()}.</p>
	 * 
	 * @param Δmierka miera zmeny hodnoty mierky zobrazenia roja
	 * 
	 * @see #mierka()
	 * @see #mierka(double)
	 */
	public void zmeňMierku(double Δmierka)
	{
		mierka += Δmierka;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #zmeňMierku(double) zmeňMierku}.</p> */
	public void zmenMierku(double Δmierka)
	{ zmeňMierku(Δmierka); }


	/**
	 * <p>Táto metóda vynúti prepočet konečných súradníc {@code x2},
	 * {@code y2}, {@code z2} všetkých bodov roja pri najbližšom kreslení
	 * alebo pri volaní metódy {@link #transformuj() transformuj}. Volanie
	 * tejto metódy zároveň nastaví príznak prepočítania atribútov použitých
	 * pri kreslení (premietaní) bodov roja. (Pozri metódu: {@link 
	 * #prepočítať() prepočítať}.)</p>
	 * 
	 * @see #prepočítať()
	 * @see #transformuj()
	 */
	public void transformovať()
	{
		transformuj = true;
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #transformovať() transformovať}.</p> */
	public void transformovat() { transformovať(); }

	/**
	 * <p>Táto metóda prepočíta hodnoty konečných súradníc {@link Roj.Bod#x2
	 * x2}, {@link Roj.Bod#y2 y2}, {@link Roj.Bod#z2 z2} všetkých bodov roja.
	 * Metóda používa vnútorný príznak na overenie toho, či je prepočítanie
	 * potrebné. Ak chcete prepočítanie vynútiť, musíte pred volaním tejto
	 * metódy zavolať metódu {@link #transformovať() transformovať}.</p>
	 * 
	 * @see #transformovať()
	 * @see #prepočítaj()
	 * @see #transformuj(Roj.Bod)
	 */
	public void transformuj()
	{
		if (transformuj)
		{
			prepočítajT();

			for (Bod bod : body)
			{
				bod.transformuj();

				double xx = bod.x1 - xs;
				double yy = bod.y1 - ys;
				double zz = bod.z1 - zs;

				// „Ľavoruký“ (left-handed) systém:
				bod.x2 = xs + (xx * T00) + (yy * T01) + (zz * T02);
				bod.y2 = ys + (xx * T10) + (yy * T11) + (zz * T12);
				bod.z2 = zs + (xx * T20) + (yy * T21) + (zz * T22);

				// „Pravoruký“ (right-handed) systém:
				// bod.x2 = xs + (yy * T00) + (xx * T01) + (zz * T02);
				// bod.y2 = ys + (yy * T10) + (xx * T11) + (zz * T12);
				// bod.z2 = zs + (yy * T20) + (xx * T21) + (zz * T22);
			}

			transformuj = false;
		}
	}

	/**
	 * <p>Toto je metóda, ktorá prepočíta hodnoty konečných súradníc
	 * {@link Roj.Bod#x2 x2}, {@link Roj.Bod#y2 y2}, {@link Roj.Bod#z2 z2}
	 * zadaného bodu roja.</p>
	 * 
	 * @param bod inštancia bodu roja, ktorého atribúty majú byť prepočítané
	 * 
	 * @see #transformuj()
	 */
	public void transformuj(Bod bod)
	{
		prepočítajT();
		bod.transformuj();

		double xx = bod.x1 - xs;
		double yy = bod.y1 - ys;
		double zz = bod.z1 - zs;

		// „Ľavoruký“ (left-handed) systém:
		bod.x2 = xs + (xx * T00) + (yy * T01) + (zz * T02);
		bod.y2 = ys + (xx * T10) + (yy * T11) + (zz * T12);
		bod.z2 = zs + (xx * T20) + (yy * T21) + (zz * T22);

		// „Pravoruký“ (right-handed) systém:
		// bod.x2 = xs + (yy * T00) + (xx * T01) + (zz * T02);
		// bod.y2 = ys + (yy * T10) + (xx * T11) + (zz * T12);
		// bod.z2 = zs + (yy * T20) + (xx * T21) + (zz * T22);

		bod.faktor = bod.z2 + kz;
		if (bod.faktor > 0)
		{
			bod.x3 = kx + mierka * bod.x2 / bod.faktor;
			bod.y3 = ky + mierka * bod.y2 / bod.faktor;
			bod.z3 = mierka * bod.rozmer / bod.faktor;
		}

		Collections.sort(poradieKreslenia);
	}

	/**
	 * <p>Táto metóda vynúti prepočet premietaných súradníc ({@link 
	 * Roj.Bod#x3 x3}, {@link Roj.Bod#y3 y3}) a veľkostí kreslených objektov
	 * ({@link Roj.Bod#z3 z3}) všetkých bodov roja pri najbližšom kreslení
	 * alebo pri volaní metódy {@link #prepočítaj() prepočítaj}.</p>
	 * 
	 * @see #transformovať()
	 * @see #prepočítaj()
	 */
	public void prepočítať()
	{
		prepočítaj = true;
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepočítať() prepočítať}.</p> */
	public void prepocitat() { prepočítať(); }

	/**
	 * <p>Táto metóda prepočíta pre všetky body roja atribúty cieľovej
	 * (premietanej) polohy na plátne a veľkosti objektu kresleného na
	 * polohe bodu – hodnoty atribútov {@link Roj.Bod#x3 x3},
	 * {@link Roj.Bod#y3 y3} a {@link Roj.Bod#z3 z3}. Metóda používa
	 * vnútorný príznak na overenie toho, či je prepočítanie potrebné.
	 * Ak chcete prepočítanie vynútiť, musíte pred volaním tejto metódy
	 * zavolať metódu {@link #prepočítať() prepočítať}.</p>
	 * 
	 * @see #prepočítať()
	 * @see #transformuj()
	 */
	public void prepočítaj()
	{
		if (prepočítaj)
		{
			for (Bod bod : body)
			{
				bod.faktor = bod.z2 + kz;
				if (bod.faktor > 0)
				{
					bod.x3 = kx + mierka * bod.x2 / bod.faktor;
					bod.y3 = ky + mierka * bod.y2 / bod.faktor;
					bod.z3 = mierka * bod.rozmer / bod.faktor;
				}
			}

			Collections.sort(poradieKreslenia);
			prepočítaj = false;
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #prepočítaj() prepočítaj}.</p> */
	public void prepocitaj() { prepočítaj(); }


	/**
	 * <p>Prekreslí roj s použitím {@linkplain #kresli kresliaceho robota
	 * roja}.</p>
	 */
	public void kresli()
	{
		transformuj();
		prepočítaj();
		neplatnáSúradnica = true;

		Farba f = kresli.farba();
		double hč = kresli.hrúbkaČiary();

		if (!body.isEmpty())
		{
			Bod bod = body.get(0);
			kresli.smer(90);
			kresli.skočNa(bod.x3, bod.y3);
		}

		for (Bod bod : body)
		{
			this.bod = bod;
			bod.kresliSpoj();
		}

		for (Bod bod : poradieKreslenia)
		{
			this.bod = bod;
			bod.kresliTeleso();
		}

		kresli.hrúbkaČiary(hč);
		kresli.farba(f);
	}


	/**
	 * <p>Táto metóda slúži na export grafiky roja do zadanej inštancie
	 * {@linkplain SVGPodpora SVG podpory}. Táto metóda automaticky spúšťa
	 * metódy {@link Roj.Bod#spojDoSVG(SVGPodpora) spojDoSVG}
	 * a {@link Roj.Bod#telesoDoSVG(SVGPodpora) telesoDoSVG} pre
	 * jednotlivé body roja.</p>
	 * 
	 * @param svgPodpora inštancia {@linkplain SVGPodpora SVG podpory},
	 *     do ktorej budú exportované {@linkplain Shape tvary Javy}
	 *     reprezentujúce všetky viditeľné spoje medzi bodmi roja a telesá
	 *     umiestnené v bodoch roja
	 * 
	 * @see Roj.Bod#spojDoSVG(SVGPodpora)
	 * @see Roj.Bod#telesoDoSVG(SVGPodpora)
	 */
	public void pridajDoSVG(SVGPodpora svgPodpora)
	{
		transformuj();
		prepočítaj();
		neplatnáSúradnica = true;

		if (!body.isEmpty())
		{
			Bod bod = body.get(0);
			kresli.smer(90);
			kresli.skočNa(bod.x3, bod.y3);
		}

		for (Bod bod : body)
		{
			this.bod = bod;
			bod.spojDoSVG(svgPodpora);
		}

		for (Bod bod : poradieKreslenia)
		{
			this.bod = bod;
			bod.telesoDoSVG(svgPodpora);
		}
	}


	/**
	 * <p>Vypočíta zo zadaných súradníc troch bodov určujúcich plochu
	 * v priestore súradnice normálového vektora k tejto ploche a vráti
	 * jeho <i><b>ne</b></i>normalizované súradnice.</p>
	 * 
	 * <p>Inak povedané, metóda vypočíta súradnice smerového vektora kolmice
	 * k ploche, tento vektor však <i><b>ne</b></i>prepočíta na jednotkový,
	 * ale ho vráti v (kvázi) „surovom“ stave. (Na väčšinu účelov by mal
	 * postačovať aj nenormalizovaný vektor. V prípade potreby použite metódu
	 * {@link #normalizuj(double[]) normalizuj}.)</p>
	 * 
	 * @param x1 x-ová súradnica prvého bodu
	 * @param y1 y-ová súradnica prvého bodu
	 * @param z1 z-ová súradnica prvého bodu
	 * @param x2 x-ová súradnica druhého bodu
	 * @param y2 y-ová súradnica druhého bodu
	 * @param z2 z-ová súradnica druhého bodu
	 * @param x3 x-ová súradnica tretieho bodu
	 * @param y3 y-ová súradnica tretieho bodu
	 * @param z3 z-ová súradnica tretieho bodu
	 * @return trojprvkové pole so súradnicami normálového vektora plochy
	 *     (určenej tromi bodmi v parametroch)
	 */
	public static double[] normála(double x1, double y1, double z1,
		double x2, double y2, double z2, double x3, double y3, double z3)
	{
		// Zdroje:
		// 
		// https://www.ma.utexas.edu/users/m408m/Display12-5-4.shtml
		// https://en.wikipedia.org/wiki/Cross_product
		// (https://math.stackexchange.com/questions/137538/calculate-the-vector-normal-to-the-plane-by-given-points)
		// 
		double Δx1 = x2 - x1;
		double Δy1 = y2 - y1;
		double Δz1 = z2 - z1;
		double Δx2 = x3 - x2;
		double Δy2 = y3 - y2;
		double Δz2 = z3 - z2;

		double nx = Δy1 * Δz2 - Δz1 * Δy2;
		double ny = Δz1 * Δx2 - Δx1 * Δz2;
		double nz = Δx1 * Δy2 - Δy1 * Δx2;

		return new double[] {nx, ny, nz};
	}

	/** <p><a class="alias"></a> Alias pre {@link #normála(double, double, double, double, double, double, double, double, double) normála}.</p> */
	public static double[] normala(double x1, double y1, double z1,
		double x2, double y2, double z2, double x3, double y3, double z3)
	{ return normála(x1, y1, z1, x2, y2, z2, x3, y3, z3); }


	/**
	 * <p>Normalizuje vektor zadaných súradníc. Táto metóda pracuje
	 * s ľubovoľne veľkým vektorom a normalizuje všetky súradnice podľa
	 * dĺžky vektora. To znamená, že ak má vektor šesť súradníc
	 * (pozri {@link Smerník#dajStav() smerník.dajStav()}), tak metóda
	 * vzájomne normalizuje všetkých šesť súradníc dohromady (akoby išlo
	 * napríklad o šesťrozmerný bod)! Návratovou hodnotou je rovnaké
	 * pole, ktoré bolo zadané vo vstupnom parametri {@code vektor}, len
	 * s prepočítanými súradnicami. (To znamená, že všetky zmeny sa
	 * rovnako prejavia aj vo vstupnom poli – ide o rovnakú inštanciu
	 * poľa.)</p>
	 * 
	 * @param vektor vektor súradníc určených na normalizáciu
	 * @return vektor s normalizovanými súradnicami
	 */
	public static double[] normalizuj(double[] vektor)
	{
		if (null != vektor && 1 < vektor.length)
		{
			double Î = 0.0;
			for (int i = 0; i < vektor.length; ++i)
				Î += vektor[i] * vektor[i];
			Î = sqrt(Î);
			for (int i = 0; i < vektor.length; ++i)
				vektor[i] /= Î;
		}
		return vektor;
	}


	/**
	 * <p>Vráti uhly potrebné na pootočenie jednotkového vektora paralelného
	 * s osou z okolo osí x (prvý prvok poľa v návratovej hodnote označovaný
	 * v tejto dokomentácii aj ako uhol α) a y (druhý prvok poľa v návratovej
	 * hodnote označovaný v tejto dokomentácii aj ako uhol β) tak, aby
	 * výsledný vektor smeroval k zadanému polohovému vektoru.</p>
	 * 
	 * @param x x-ová súradnica polohového vektora
	 * @param y y-ová súradnica polohového vektora
	 * @param z z-ová súradnica polohového vektora
	 * @return dvojprvkové pole s požadovanými uhlami
	 */
	public static double[] uhlyK(double x, double y, double z)
	{
		// Zdroje: Pozri ‚„Malý“ omyl…‘ v komentároch niekde vyššie.
		return new double[] { -toDegrees(atan2(y, z)),
			toDegrees(atan2(x, hypot(y, z))) };
	}

	/**
	 * <p>Vráti uhly potrebné na pootočenie jednotkového vektora paralelného
	 * s osou z okolo osí x (prvý prvok poľa v návratovej hodnote označovaný
	 * v tejto dokomentácii aj ako uhol α) a y (druhý prvok poľa v návratovej
	 * hodnote označovaný v tejto dokomentácii aj ako uhol β) tak, aby
	 * výsledný vektor smeroval k zadanému polohovému vektoru. Ak zadaný
	 * vektor neobsahuje aspoň tri prvky, tak metóda vráti hodnotu {@code 
	 * valnull}.</p>
	 * 
	 * @param vektor polohový vektor súradníc
	 * @return dvojprvkové pole s požadovanými uhlami alebo {@code valnull}
	 */
	public static double[] uhlyK(double[] vektor)
	{
		// Zdroje: Pozri ‚„Malý“ omyl…‘ v komentároch niekde vyššie.
		if (null != vektor && vektor.length >= 3)
			return new double[]
				{
					-toDegrees(atan2(vektor[1], vektor[2])),
					toDegrees(atan2(vektor[0], hypot(vektor[1], vektor[2])))
				};
		return null;
	}


	/**
	* <p>Prečíta údaje o roji z konfiguračného súboru otvoreného na čítanie.
	 * Metóda prijíma identifikátor menného priestoru. Identifikátor smie mať
	 * hodnotu {@code valnull}. V takom prípade sú údaje čítané z aktuálneho
	 * menného priestoru zadaného konfiguračného súboru.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
	 *     na čítanie
	 * @param identifikátor vnorený menný priestor, z ktorého budú
	 *     prevzaté údaje o roji
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void čítajZoSúboru(Súbor súbor, String identifikátor)
		throws IOException
	{
		súbor.vnorMennýPriestorVlastností(identifikátor);

		try
		{
			vymaž();

			double[] predvolené = null;
			double[] prečítané;

			prečítané = súbor.čítajVlastnosť("uhly", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				α = prečítané[0];
				β = prečítané[1];
				γ = prečítané[2];
			}

			prečítané = súbor.čítajVlastnosť("stred", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				xs = prečítané[0];
				ys = prečítané[1];
				zs = prečítané[2];
			}

			prečítané = súbor.čítajVlastnosť("kamera", predvolené);
			if (null != prečítané && prečítané.length >= 3)
			{
				kx = prečítané[0];
				ky = prečítané[1];
				kz = prečítané[2];
			}

			súbor.čítajVlastnosť("mierka", mierka);

			int početBodov = súbor.čítajVlastnosť("početBodov", 0);
			for (int i = 0; i < početBodov; ++i)
			{
				Bod bod = new Bod();
				body.pridaj(bod);
				poradieKreslenia.pridaj(bod);
				bod.čítajZoSúboru(súbor, "bod[" + i + "]");
			}

			transformuj = true;
			prepočítaj  = true;
			prepočítajT = true;
		}
		finally
		{
			súbor.vynorMennýPriestorVlastností();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void citajZoSuboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void prečítajZoSúboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/** <p><a class="alias"></a> Alias pre {@link #čítajZoSúboru(Súbor, String) čítajZoSúboru}.</p> */
	public void precitajZoSuboru(Súbor súbor, String identifikátor)
	throws IOException { čítajZoSúboru(súbor, identifikátor); }

	/**
	 * <p>Zapíše údaje o roji do konfiguračného súboru otvoreného na zápis.
	 * Metóda prijíma identifikátor menného priestoru. Identifikátor smie mať
	 * hodnotu {@code valnull}. V takom prípade sú údaje ukladané do
	 * aktuálneho menného priestoru zadaného konfiguračného súboru.</p>
	 * 
	 * @param súbor inštancia triedy {@link Súbor Súbor} otvorená
	 *     na zápis
	 * @param identifikátor vnorený menný priestor, do ktorého budú
	 *     vložené údaje o roji
	 * 
	 * @exception IOException ak vznikla chyba vo vstupno-výstupnej
	 *     operácii
	 */
	public void uložDoSúboru(Súbor súbor, String identifikátor)
		throws IOException
	{
		súbor.vnorMennýPriestorVlastností(identifikátor);

		try
		{
			súbor.zapíšPrázdnyRiadokVlastností();
			súbor.zapíšVlastnosť("uhly", new double[] {α, β, γ});
			súbor.zapíšVlastnosť("stred", new double[] {xs, ys, zs});
			súbor.zapíšVlastnosť("kamera", new double[] {kx, ky, kz});
			súbor.zapíšVlastnosť("mierka", mierka);

			int početBodov = body.veľkosť();
			súbor.zapíšPrázdnyRiadokVlastností();
			súbor.zapíšVlastnosť("početBodov", početBodov);

			for (int i = 0; i < početBodov; ++i)
			{
				súbor.zapíšPrázdnyRiadokVlastností();
				body.daj(i).uložDoSúboru(súbor, "bod[" + i + "]");
			}
		}
		finally
		{
			súbor.vynorMennýPriestorVlastností();
		}
	}

	/** <p><a class="alias"></a> Alias pre {@link #uložDoSúboru(Súbor, String) uložDoSúboru}.</p> */
	public void ulozDoSuboru(Súbor súbor, String identifikátor)
	throws IOException { uložDoSúboru(súbor, identifikátor); }
}
