
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Jej účelom je uchovávať informácie o textoch
// kreslených na obrazovku (plátno) a má využitie pri exporte textov do
// formátu SVG. Licencia a zdroje sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;

import java.awt.font.TextLayout;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

/**
 * <p>This class allows to store all data used to create text outline at one
 * place. Text outline is used to paint text data as a shape (2D path) that
 * can be graphically manipulated (transformed). This is useful to create
 * nice graphical effect, but usually you will lose the access to the data
 * that created the original shape (text outline). This class stores all the
 * data connected to the shape creation.</p>
 * 
 * @author Roman Horváth
 * @version 23. 6. 2018
 * 
 * @exclude
 */
public class SimpleTextShape extends Path2D.Float // implements Cloneable
{
	// Shapes in Java are (usualy) implementations of Serializable interface.
	// This is the serial version identification number.
	private static final long serialVersionUID = -6558291335499261239L;

	// Coordinates of the text.
	private double x, y;

	// Current font (used to create this shape).
	private Font font;

	// Textual data of this instance.
	private String text;

	// Last graphics context used to create this shape.
	private Graphics2D lastG2d;

	/**
	 * <p>Constructs the simple text shape object getting all necessary data.</p>
	 * 
	 * @param x horizontal coordinate of the text (left = zero)
	 * @param y vertical coordinate of the text (top = zero)
	 * @param font font object used to create this shape
	 * @param text textual data used to create this shape
	 * @param g2d graphics context necessary for creating the text outline
	 *     (the actual shape)
	 */
	public SimpleTextShape(double x, double y,
		Font font, String text, Graphics2D g2d)
	{
		this.x = x;
		this.y = y;
		this.font = font;
		this.text = text;
		lastG2d = g2d;
		rebuildShape(g2d);
	}

	/**
	 * <p>Rebuilds this text shape after some changes have been made. (E.g.
	 * position of the text has changed, fond was modified…)</p>
	 */
	public Shape rebuildShape()
	{
		reset();
		// lastG2d.setFont(font);

		TextLayout textLayout = new TextLayout(
			text, font, lastG2d.getFontRenderContext());

		AffineTransform affineTransform =
			AffineTransform.getTranslateInstance(x, y);

		append(textLayout.getOutline(affineTransform), false);
		return this;
	}

	/**
	 * <p>Rebuilds this text shape allowing to chagne the graphics context.</p>
	 * 
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public Shape rebuildShape(Graphics2D g2d)
	{
		lastG2d = g2d;
		return rebuildShape();
	}


	/**
	 * <p>Gets horizontal coordinate of the point specifying the text (shape)
	 * position.</p>
	 * 
	 * @return horizontal coordinate of the text (shape) position
	 */
	public double getX() { return x; }

	/**
	 * <p>Gets vertical coordinate of the point specifying the text (shape)
	 * position.</p>
	 * 
	 * @return vertical coordinate of the text (shape) position
	 */
	public double getY() { return y; }


	/**
	 * <p>Sets new horizontal coordinate of the point specifying the text (shape)
	 * position and regenerates the shape.</p>
	 * 
	 * @param newX new horizontal coordinate of the text (shape) position
	 */
	public void setX(double newX)
	{
		x = newX;
		rebuildShape();
	}

	/**
	 * <p>Sets new vertical coordinate of the point specifying the text (shape)
	 * position and regenerates the shape.</p>
	 * 
	 * @param newY new vertical coordinate of the text (shape) position
	 */
	public void setY(double newY)
	{
		y = newY;
		rebuildShape();
	}

	/**
	 * <p>Sets new horizontal coordinate of the point specifying the text (shape)
	 * position, changes the graphics context and regenerates the shape.</p>
	 * 
	 * @param newX new horizontal coordinate of the text (shape) position
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public void setX(double newX, Graphics2D g2d)
	{
		x = newX;
		lastG2d = g2d;
		rebuildShape();
	}

	/**
	 * <p>Sets new vertical coordinate of the point specifying the text (shape)
	 * position, changes the graphics context and regenerates the shape.</p>
	 * 
	 * @param newX new vertical coordinate of the text (shape) position
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public void setY(double newY, Graphics2D g2d)
	{
		y = newY;
		lastG2d = g2d;
		rebuildShape();
	}

	/**
	 * <p>Sets new position for generating the text shape and regenerates it.</p>
	 * 
	 * @param newX new horizontal coordinate of the text (shape) position
	 * @param newY new vertical coordinate of the text (shape) position
	 */
	public void setPosition(double newX, double newY)
	{
		x = newX;
		y = newY;
		rebuildShape();
	}

	/**
	 * <p>Sets new position for generating the text shape, changes the graphics
	 * context and regenerates the shape.</p>
	 * 
	 * @param newX new horizontal coordinate of the text (shape) position
	 * @param newY new vertical coordinate of the text (shape) position
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public void setPosition(double newX, double newY, Graphics2D g2d)
	{
		x = newX;
		y = newY;
		lastG2d = g2d;
		rebuildShape();
	}


	/**
	 * <p>Gets font used to generate this text outline shape.</p>
	 * 
	 * @return font of the shape
	 */
	public Font getFont()
	{
		return font;
	}


	/**
	 * <p>Sets new font for generating the shape and regenerates it.</p>
	 * 
	 * @param font new font for generating the shape
	 */
	public void setFont(Font font)
	{
		this.font = font;
		rebuildShape();
	}

	/**
	 * <p>Sets new font for generating the shape, changes the graphics
	 * context and regenerates the shape.</p>
	 * 
	 * @param font new font for generating the shape
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public void setFont(Font font, Graphics2D g2d)
	{
		this.font = font;
		lastG2d = g2d;
		rebuildShape();
	}


	/**
	 * <p>Gets text stored inside this shape.</p>
	 * 
	 * @return text of the shape
	 */
	public String getText()
	{
		return text;
	}


	/**
	 * <p>Sets new text for generating the shape and regenerates it.</p>
	 * 
	 * @param text new text for generating the shape
	 */
	public void setText(String text)
	{
		this.text = text;
		rebuildShape();
	}

	/**
	 * <p>Sets new text for generating the shape, changes the graphics
	 * context and regenerates the shape.</p>
	 * 
	 * @param text new text for generating the shape
	 * @param g2d new {@linkplain Graphics2D graphics context}
	 */
	public void setText(String text, Graphics2D g2d)
	{
		this.text = text;
		lastG2d = g2d;
		rebuildShape();
	}


	// @Override public final Object clone() throws CloneNotSupportedException
	// { return new SimpleTextShape(x, y, font, text, lastG2d); }
}
