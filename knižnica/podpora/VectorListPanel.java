
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to panel implementujúci dynamický zoznam
// položiek vytváraný z položiek zadaného zoznamu Javy (Vector). Rámec ho
// používa v rámci komunikačných dialógov Sveta. Licencia a zdroje sú
// uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import java.util.HashMap;
import java.util.Vector;

import static javax.swing.ListSelectionModel.*;

/**
 * <p>This class creates a list pane whose choices match the items
 * of the vector. TODO. (This is just the very basic functionality.
 * The class could be extended a bit…)</p>
 * 
 * <p>Example:</p>
 * 
 * <pre>
 	TODO
 * </pre>
 * 
 * @author Roman Horváth
 * @version 6. – 15. 12. 2021
 * 
 * @exclude
 */
@SuppressWarnings("serial")
public class VectorListPanel extends JPanel
{
	public final HashMap<Image, ImageIcon> iconsMap = new HashMap<>();
	public final JList<?> list;

	public VectorListPanel(Vector<?> vector)
	/*{ this(vector, null); }

	public VectorListPanel(Vector<?> vector, String title)*/ // The title is ignored
	{
		super(new BorderLayout());

		final int indexOf = (vector.size() > 1) ?
			vector.indexOf(vector.get(0), 1) : -1;

		Vector<Object> items = new Vector<>();
		boolean vertical = true;
		for (int i = 1; i < vector.size(); ++i)
		{
			Object object = vector.get(i);
			if (object instanceof Image)
			{
				Image image = (Image)object;
				ImageIcon icon = new ImageIcon(image);
				iconsMap.put(image, icon);
			}
			else vertical = false;
			items.add(object);
		}

		list = new JList<>(items);
		list.setSelectionMode(SINGLE_SELECTION);
		// list.setSelectionMode(SINGLE_INTERVAL_SELECTION);
		// list.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
		if (vertical) // TODO configurable?
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setCellRenderer(new CustomCellRenderer());
		if (indexOf > 0) list.setSelectedIndex(indexOf - 1);

		// TODO — make it more universal
		// list.setPreferredSize(new Dimension(400, 200));

		JScrollPane listScroller = new JScrollPane(list);

		if (vertical)
			listScroller.setPreferredSize(new Dimension(420, 200));
		else
			listScroller.setPreferredSize(new Dimension(200, 220));

		// if (null == title) add(listScroller); else
		{
			JPanel listContainer = new JPanel(new GridLayout(1, 1));
			// listContainer.setBorder(BorderFactory.createTitledBorder(title));
			listContainer.add(listScroller);
			add(listContainer);
		}

		if (indexOf > 0)
			SwingUtilities.invokeLater(() ->
				list.ensureIndexIsVisible(indexOf - 1));
	}

	/** /
	private static void createAndShowGUI()
	{
		// Create and set up the window.
		JFrame frame = new JFrame("VectorListPanel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		VectorListPanel demo = new VectorListPanel();
		demo.setOpaque(true);
		frame.setContentPane(demo);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	/**/


	// Display an icon or string for each object in the list.
	class CustomCellRenderer extends JLabel implements ListCellRenderer<Object>
	{
		public Component getListCellRendererComponent(
			JList<?> list,        // the list
			Object value,         // value to display
			int index,            // cell index
			boolean isSelected,   // is the cell selected
			boolean cellHasFocus) // does the cell have focus
		{
			if (value instanceof Image)
				setIcon(iconsMap.get((Image)value));
			else if (value instanceof ImageIcon)
				setIcon((ImageIcon)value);
			else
				setText(value.toString());

			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}
}
