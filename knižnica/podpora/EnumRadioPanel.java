
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to obojsmerná mapa slúžiaca napríklad na
// vytvorenie slovníka. Ten môže byť použitý rôzne. Rámec ho vnútorne
// používa na preklad direktív a hodnôt konfiguračných súborov, ktorých
// predvolené tvary by inak nebolo možné zmeniť. Licencia a zdroje sú
// uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.GridLayout;

import java.util.EnumSet;

// import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * <p>This class creates a radio button pane whose choices match the item
 * of an enumeration. The input is one of the enumeration items that
 * specifies the default selection.</p>
 * 
 * <p>Example:</p>
 * 
 * <pre>
 * // Define some enumeration:
 * enum Animals
 * {
 * 	BIRD, CAT, DOG, RABBIT, PIG
 * }
 * 
 * 	// Create, set up, and show the GUI window.
 * 	JFrame frame = new JFrame("EnumRadioPanel");
 * 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * 
 * 	// Create the content pane using one enumeration’s item.
 * 	EnumRadioPanel contentPanel = new EnumRadioPanel(Animals.DOG);
 * 
 * 	frame.setContentPane(contentPanel);
 * 
 * 	// Display the window.
 * 	frame.pack();
 * 	frame.setVisible(true);
 * </pre>
 * 
 * @author Roman Horváth
 * @version 6. 12. 2020
 * 
 * @exclude
 */
@SuppressWarnings("serial")
public class EnumRadioPanel extends JPanel
{
	/**
	 * Interface providing the string conversion capability. The Enum elements
	 * used to create the radio button items pass through this interface
	 * (according to current settings) and are converted to more readable
	 * form.
	 */
	public interface ConvertString
	{
		/**
		 * Single method providing the string conversion.
		 * 
		 * @param item the Enum item to be converted
		 * @return converted string
		 */
		public String convert(Enum<?> item);
	}

	/**
	 * Default ConvertString instance that converts all letters to lower
	 * case, except the first (which is left alone) and that converts all
	 * underscores to spaces.
	 */
	public final static ConvertString toLowerCaseExceptFirst =
		new ConvertString()
	{
		public String convert(Enum<?> item)
		{
			// System.out.println("Converting item 1: " + item);

			boolean changed = false;
			String aString = item.toString();
			char[] chars = aString.toCharArray();

			if (0 < chars.length && '_' == chars[0])
			{
				chars[0] = ' ';
				changed = true;
			}

			for (int i = 1; i < chars.length; ++i)
			{
				if ('_' == chars[i])
				{
					chars[i] = ' ';
					changed = true;
				}
				else if (Character.isUpperCase(chars[i]))
				{
					chars[i] = Character.toLowerCase(chars[i]);
					changed = true;
				}
			}

			if (changed) return new String(chars);
			return aString;
		}
	};

	/**
	 * Default ConvertString instance that converts all letters to lower
	 * case and all underscores to spaces.
	 */
	public final static ConvertString toLowerCase =
		new ConvertString()
	{
		public String convert(Enum<?> item)
		{
			boolean changed = false;
			String aString = item.toString();
			char[] chars = aString.toCharArray();

			for (int i = 0; i < chars.length; ++i)
			{
				if ('_' == chars[i])
				{
					chars[i] = ' ';
					changed = true;
				}
				else if (Character.isUpperCase(chars[i]))
				{
					chars[i] = Character.toLowerCase(chars[i]);
					changed = true;
				}
			}

			if (changed) return new String(chars);
			return aString;
		}
	};

	/**
	 * Default ConvertString instance that converts all letters to upper
	 * case and all underscores to spaces.
	 */
	public final static ConvertString toUpperCase =
		new ConvertString()
	{
		public String convert(Enum<?> item)
		{
			boolean changed = false;
			String aString = item.toString();
			char[] chars = aString.toCharArray();

			for (int i = 0; i < chars.length; ++i)
			{
				if ('_' == chars[i])
				{
					chars[i] = ' ';
					changed = true;
				}
				else if (Character.isLowerCase(chars[i]))
				{
					chars[i] = Character.toUpperCase(chars[i]);
					changed = true;
				}
			}

			if (changed) return new String(chars);
			return aString;
		}
	};

	/**
	 * Default ConvertString instance that only converts underscores to spaces.
	 */
	public final static ConvertString justUnderscores =
		new ConvertString()
	{
		public String convert(Enum<?> item)
		{
			boolean changed = false;
			String aString = item.toString();
			char[] chars = aString.toCharArray();

			for (int i = 0; i < chars.length; ++i)
			{
				if ('_' == chars[i])
				{
					chars[i] = ' ';
					changed = true;
				}
			}

			if (changed) return new String(chars);
			return aString;
		}
	};

	/**
	 * Instance providing the default conversion method. It is allowed to
	 * change it or even to set it to null.
	 */
	public static ConvertString convertString = toLowerCaseExceptFirst;


	// The Enum set connected to this panel.
	private final EnumSet<?> enumSet;

	// All radio buttons on this panel.
	private final JRadioButton[] buttons;

	/**
	 * The panel constructor.
	 * 
	 * @param item the default item for the radio button selection
	 */
	public EnumRadioPanel(Enum<?> item)
	{
		super(new GridLayout(0, 1));

		// stackoverflow.com/questions/2269803/how-to-get-all-enum-values-in-java
		enumSet = EnumSet.allOf(item.getDeclaringClass());

		String[] strings = new String[enumSet.size()];

		if (item instanceof ConvertString)
		{
			int i = 0;
			for (Enum<?> element : enumSet)
			{
				strings[i] = ((ConvertString)element).convert(element);
				++i;
			}
		}
		else
		{
			boolean convert = true;
			{
				int i = 0;
				for (Enum<?> element : enumSet)
				{
					strings[i] = element.toString();
					if (!element.name().equals(strings[i])) convert = false;
					++i;
				}
			}

			if (convert && null != convertString)
			{
				int i = 0;
				for (Enum<?> element : enumSet)
				{
					strings[i] = convertString.convert(element);
					++i;
				}
			}
		}

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		{
			int i = 0;
			buttons = new JRadioButton[enumSet.size()];
			for (Enum<?> element : enumSet)
			{
				// Create the radio buttons.
				buttons[i] = new JRadioButton(strings[i]);
				buttons[i].setSelected(element == item);
				group.add(buttons[i]);

				// Put the radio buttons in the panel.
				add(buttons[i]);
				++i;
			}
		}

		// setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}


	/**
	 * Returns the number of buttons in this panel. (Is equal to the total
	 * number of values in the enumeration used to create this group.)
	 * 
	 * @return number of buttons in this panel
	 */
	public int length()
	{
		return buttons.length;
	}

	/**
	 * Provides the button instance requested by its index.
	 * 
	 * @param index the index number of the component
	 * @return requested radio button or null (if the index was out of range)
	 */
	public JRadioButton getButton(int index)
	{
		if (index < 0 || index >= buttons.length) return null;
		return buttons[index];
	}

	/**
	 * Gets the instance of the selected button.
	 * 
	 * @return instance of the selected button or null when there is no
	 *     selection
	 */
	public JRadioButton getSelectedButton()
	{
		// stackoverflow.com/questions/201287/how-do-i-get-which-
		// jradiobutton-is-selected-from-a-buttongroup
		for (JRadioButton button : buttons)
			if (button.isSelected()) return button;
		return null;
	}


	/**
	 * Gets the enumeration item corresponding to the selected button.
	 * 
	 * @return selected item or null when there is no selection
	 */
	public Enum<?> getSelection()
	{
		// stackoverflow.com/questions/201287/how-do-i-get-which-
		// jradiobutton-is-selected-from-a-buttongroup
		int i = 0;
		for (Enum<?> element : enumSet)
		// for (JRadioButton button : buttons)
		{
			// if (button.isSelected()) return enumSet.get(i);
			if (buttons[i].isSelected()) return element;
			++i;
		}
		return null;
	}


	/**
	 * Sets the selected button corresponding to the entered item of the
	 * enumeration.
	 * 
	 * @param item item that’s radio buton is to be selected
	 */
	public void setSelection(Enum<?> item)
	{
		int i = 0;
		for (Enum<?> element : enumSet)
		{
			buttons[i].setSelected(element == item);
			++i;
		}
	}
}
