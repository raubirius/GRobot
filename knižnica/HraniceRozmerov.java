
 /////////////////////////////////////////////////////////////////////////////
 // This source code is part of the graphical framework called “Programovací
 // rámec GRobot”. (The name is Slovak like the vast majority of own
 // identifiers used in this project.) The name translated to English means
 // “The GRobot Framework.”
 // 
 // Copyright © 2010 – 2026 by Roman Horváth
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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Spoločné pre Svet a Okno. Každé bežné okno má vlastné hranice.
// Panel môže byť dočasne prenesený do iného okna (napr. na celú obrazovku).
final class HraniceRozmerov extends ComponentAdapter
{
	// Poznámka: Tento mechanizmus bol implementovaný s použitím umelej
	// inteligencie.

	final static int MIN_PANEL = 0, MAX_PANEL = 1, MIN_OKNO = 2, MAX_OKNO = 3;
	private final int[][] hranice = new int[4][2];
	private final JFrame okno;
	private final JPanel panel;
	private boolean čaká, počúva, nastaviloMinimum, nastaviloMaximum;

	HraniceRozmerov(JFrame okno, JPanel panel)
	{
		this.okno = okno;
		this.panel = panel;
	}

	synchronized int[] čítaj(int druh) { return hranice[druh].clone(); }

	synchronized void nastav(int druh, int šírka, int výška)
	{
		if (šírka < 0 || výška < 0) return;
		hranice[druh][0] = šírka;
		hranice[druh][1] = výška;
		naplánuj();
	}

	// Zlúčenie udalostí umožní najprv dokončiť rozmiestnenie komponentov.
	private synchronized void naplánuj()
	{
		if (čaká) return;
		čaká = true;
		SwingUtilities.invokeLater(this::uplatni);
	}

	@Override public void componentResized(ComponentEvent e) { naplánuj(); }
	@Override public void componentMoved(ComponentEvent e) { naplánuj(); }

	private static int vonkajší(int vnútorný, int navyše)
	{
		return vnútorný == 0 ? 0 :
			(int)Math.min(Integer.MAX_VALUE, (long)vnútorný + navyše);
	}

	private synchronized void uplatni()
	{
		čaká = false;
		if (!počúva)
		{
			okno.addComponentListener(this);
			panel.addComponentListener(this);
			panel.addHierarchyListener(e -> {
				if ((e.getChangeFlags() & (HierarchyEvent.SHOWING_CHANGED |
					HierarchyEvent.PARENT_CHANGED |
					HierarchyEvent.DISPLAYABILITY_CHANGED)) != 0)
					naplánuj();
			});
			okno.addWindowStateListener(e -> naplánuj());
			počúva = true;
		}
		if (!okno.isShowing() ||
			SwingUtilities.getWindowAncestor(panel) != okno ||
			(okno.getExtendedState() & Frame.ICONIFIED) != 0) return;

		okno.validate();
		Dimension veľkosť = okno.getSize();
		int[] minimum = new int[2], maximum = new int[2];
		for (int i = 0; i < 2; ++i)
		{
			int navyše = Math.max(0, i == 0 ? veľkosť.width - panel.getWidth() :
				veľkosť.height - panel.getHeight());
			minimum[i] = Math.max(hranice[MIN_OKNO][i],
				vonkajší(hranice[MIN_PANEL][i], navyše));
			int von = hranice[MAX_OKNO][i], dnu =
				vonkajší(hranice[MAX_PANEL][i], navyše);
			int limit = von == 0 ? dnu : dnu == 0 ? von : Math.min(von, dnu);
			maximum[i] = limit == 0 ? Integer.MAX_VALUE : Math.max(minimum[i],
				limit);
		}

		Dimension min = new Dimension(minimum[0], minimum[1]);
		if (min.width != 0 || min.height != 0)
		{
			if (!okno.isMinimumSizeSet() || !min.equals(okno.getMinimumSize()))
				okno.setMinimumSize(min);
			nastaviloMinimum = true;
		}
		else if (nastaviloMinimum)
		{
			okno.setMinimumSize(null);
			nastaviloMinimum = false;
		}

		if (maximum[0] != Integer.MAX_VALUE || maximum[1] != Integer.MAX_VALUE)
		{
			GraphicsConfiguration gc = okno.getGraphicsConfiguration();
			Rectangle plocha = gc.getBounds();
			Insets okraje = Toolkit.getDefaultToolkit().getScreenInsets(gc);
			plocha.x += okraje.left; plocha.y += okraje.top;
			plocha.width = Math.min(maximum[0], plocha.width - okraje.left -
				okraje.right);
			plocha.height = Math.min(maximum[1], plocha.height - okraje.top -
				okraje.bottom);
			if (!plocha.equals(okno.getMaximizedBounds()))
				okno.setMaximizedBounds(plocha);
			nastaviloMaximum = true;
		}
		else if (nastaviloMaximum)
		{
			okno.setMaximizedBounds(null);
			nastaviloMaximum = false;
		}

		int šírka = Math.max(minimum[0],
			Math.min(maximum[0], okno.getWidth()));
		int výška = Math.max(minimum[1],
			Math.min(maximum[1], okno.getHeight()));

		if (šírka != okno.getWidth() || výška != okno.getHeight())
		{
			int stav = okno.getExtendedState();
			if ((stav & Frame.MAXIMIZED_BOTH) != 0)
				okno.setExtendedState(stav & ~Frame.MAXIMIZED_BOTH);
			okno.setSize(šírka, výška);
		}
	}
}
