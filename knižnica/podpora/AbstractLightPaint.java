
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to abstraktná trieda slúžiaca ako predloha
// pre niekoľko ďalších tried, ktoré generujú svetelné nátery. Licencia
// a zdroje sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import java.util.TreeMap;

/**
 * <p>This class provides custom paint type. The {@linkplain Paint paint}
 * is composed form several lights definition from which it will create
 * a light pattern – depending on the concrete implementation.</p>
 * 
 * <p><small>(Due its computational complexity this class (in fact its
 * descendants, because this class is abstract) is not intended to be
 * used in real-time renderings.)</small></p>
 * 
 * @author Roman Horváth
 * @version 20. 7. 2018
 * 
 * @exclude
 */
public abstract class AbstractLightPaint implements Paint
{
	/**
	 * <p>The transparency of this paint object. May change within the
	 * instance lifespan.</p>
	 */
	private int transparency;

	/**
	 * <p>Number of lights in this pattern.</p>
	 */
	int lightsCount;

	/**
	 * <p>The coordinates of the points (lights’ average locations).</p>
	 */
	int[][] points;

	/**
	 * <p>The colors of the lights.</p>
	 */
	int[][] colors;

	/**
	 * <p>The dilution factors of the lights.</p>
	 */
	int[] dilutions;

	/**
	 * <p>The relative dimming factors of the lights.</p>
	 */
	int[] dims;

	/**
	 * <p>A computing stack used internally by paint context.</p>
	 */
	int[][] computingStack;


	/**
	 * <p>Full constructor receiving all information about the new light
	 * pattern. The process was implemented as robust as possible. If the
	 * {@code points} array is {@code null}, then empty pattern is created.
	 * You can add lights by the {@link #putLight(int, int[], Color, int)
	 * putLight} method.</p>
	 * 
	 * <p>If the {@code points} array is not {@code null}, the gradient will
	 * contain as many lights as there is valid points in the array. Valid
	 * point must have two coordinates – the second dimension of array must
	 * be at least two elements long. Each missing color is substituted by
	 * transparent black. Each missing or invalid dilution is substituted by
	 * default minimal value (1) and the same applies for each missing
	 * relative dimming factor.</p>
	 * 
	 * @param points list of the lights’ average locations
	 * @param colors list of the lights’ colors
	 * @param dilution list of the lights’ dilutions; a dilution dulls the
	 *     light a bit and moves it away from the center of the pattern; for
	 *     more agressive action choose big number (like 1000 or 10000…)
	 * @param dims list of the lights’ relative dimming factors; if this
	 *     value is bigger for some light, the light dims and its light
	 *     cone focuses a bit (note: for more precise distinguish choose
	 *     bigger values for all lights, but too high values will distort
	 *     the pattern’s edges by white light)
	 */
	public AbstractLightPaint(int[][] points, Color[] colors,
		int[] dilutions, int[] dims)
	{
		transparency = TRANSLUCENT;

		if (null == points)
		{
			lightsCount = 0;
			this.points = new int[10][2]; // Default buffer size(s): 10
			this.colors = new int[10][4];
			this.dilutions = new int[10];
			this.dims = new int[10];
			computingStack = new int[10][3];
		}
		else
		{
			int bufferSizes = points.length;
			bufferSizes /= 10; ++bufferSizes; bufferSizes *= 10;

			this.points = new int[bufferSizes][2];
			this.colors = new int[bufferSizes][4];
			this.dilutions = new int[bufferSizes];
			this.dims = new int[bufferSizes];
			computingStack = new int[bufferSizes][3];

			lightsCount = 0;

			if (null == colors && null == dilutions && null == dims)
			{
				for (int i = 0; i < points.length; ++i)
					if (points[i].length >= 2)
					{
						this.points[lightsCount][0] = points[i][0];
						this.points[lightsCount][1] = points[i][1];
						this.colors[lightsCount][0] = 0;
						this.colors[lightsCount][1] = 0;
						this.colors[lightsCount][2] = 0;
						this.colors[lightsCount][3] = 0;
						this.dilutions[lightsCount] = 1;
						this.dims[lightsCount] = 1;
						++lightsCount;
					}
			}
			else
			{
				if (null == colors) colors = new Color[] {};
				if (null == dilutions) dilutions = new int[] {};
				if (null == dims) dims = new int[] {};

				for (int i = 0; i < points.length; ++i)
					if (points[i].length >= 2)
					{
						this.points[lightsCount][0] = points[i][0];
						this.points[lightsCount][1] = points[i][1];

						if (colors.length > i && null != colors[i])
						{
							this.colors[lightsCount][0] = colors[i].getBlue();
							this.colors[lightsCount][1] = colors[i].getGreen();
							this.colors[lightsCount][2] = colors[i].getRed();
							this.colors[lightsCount][3] = colors[i].getAlpha();
						}
						else
						{
							this.colors[lightsCount][0] = 0;
							this.colors[lightsCount][1] = 0;
							this.colors[lightsCount][2] = 0;
							this.colors[lightsCount][3] = 0;
						}

						if (dilutions.length > i && dilutions[i] > 1)
							this.dilutions[lightsCount] = dilutions[i];
						else
							this.dilutions[lightsCount] = 1;

						if (dims.length > i && dims[i] > 1)
							this.dims[lightsCount] = dims[i];
						else
							this.dims[lightsCount] = 1;

						++lightsCount;
					}
			}
		}
	}


	/**
	 * <p>Lightweight constructor – the relative dimmings are omitted.</p>
	 * 
	 * <p>See the {@linkplain #AbstractLightPaint(int[][], Color[], int[],
	 * int[]) full constructor} for more details.</p>
	 * 
	 * @param points list of the lights’ average locations
	 * @param colors list of the lights’ colors
	 * @param dilution list of the lights’ dilutions (see {@linkplain 
	 * #AbstractLightPaint(int[][], Color[], int[], int[]) full constructor}…)
	 */
	public AbstractLightPaint(int[][] points, Color[] colors, int[] dilutions)
	{ this(points, colors, dilutions, null); }

	/**
	 * <p>Lightweight constructor – the dilutions and relative dimmings are
	 * omitted.</p>
	 * 
	 * <p>See the {@linkplain #AbstractLightPaint(int[][], Color[], int[],
	 * int[]) full constructor} for more details.</p>
	 * 
	 * @param points list of the lights’ average locations
	 * @param colors list of the lights’ colors
	 */
	public AbstractLightPaint(int[][] points, Color[] colors)
	{ this(points, colors, null, null); }


	/**
	 * <p>Basic constructor – creates empty pattern. You can add lights by
	 * the {@link #putLight(int, int[], Color, int) putLight} method.</p>
	 * 
	 * <p>See the {@linkplain #AbstractLightPaint(int[][], Color[], int[],
	 * int[]) full constructor} for more details.</p>
	 */
	public AbstractLightPaint() { this(null, null, null, null); }


	/**
	 * <p>Inflates all internal buffers to a new size. If the new size
	 * is not a multiple of ten it will be roofed (ceiled) to next multiple
	 * of ten.</p>
	 * 
	 * @param newSize the new size of buffers demanded
	 */
	private void inflateBuffers(int newSize)
	{
		if (newSize <= points.length) newSize = 1 + points.length;
		newSize /= 10; ++newSize; newSize *= 10;

		int[][] newPoints = new int[newSize][2];
		int[][] newColors = new int[newSize][4];
		int[] newDilutions = new int[newSize];
		int[] newDims = new int[newSize];
		int[][] newComputingStack = new int[newSize][3];

		for (int i = 0; i < points.length; ++i)
			System.arraycopy(points[i], 0, newPoints[i], 0, points[i].length);

		for (int i = 0; i < colors.length; ++i)
			System.arraycopy(colors[i], 0, newColors[i], 0, colors[i].length);

		System.arraycopy(dilutions, 0, newDilutions, 0, dilutions.length);
		System.arraycopy(dims, 0, newDims, 0, dims.length);

		System.arraycopy(computingStack, 0, newComputingStack, 0,
			computingStack.length);

		points = newPoints; colors = newColors; dilutions = newDilutions;
		dims = newDims; computingStack = newComputingStack;
	}


	/**
	 * <p>Removes all lights from this instance.</p>
	 */
	public void clear() { lightsCount = 0; }


	/**
	 * <p>Clears this instance and copies all light parameters from the
	 * source instance.</p>
	 * 
	 * @param paint source instance of any light paint implementation
	 */
	public void copyLights(AbstractLightPaint paint)
	{
		if (paint == this) return;

		if (paint.lightsCount >= points.length)
			inflateBuffers(paint.lightsCount);

		for (int i = 0; i < paint.lightsCount; ++i)
		{
			System.arraycopy(paint.points[i], 0, points[i], 0, 2);
			System.arraycopy(paint.colors[i], 0, colors[i], 0, 4);
		}

		System.arraycopy(paint.dilutions, 0, dilutions, 0, paint.lightsCount);
		System.arraycopy(paint.dims, 0, dims, 0, paint.lightsCount);

		lightsCount = paint.lightsCount;
	}


	/**
	 * <p>Overwrites the light at specified index. If the index is less
	 * than zero, or greater or equal to current number of lights, a new
	 * light is add at the end of the lights’ list. Parameters having
	 * {@code null} or invalid value are handled the same way like in
	 * the constructors (see the {@linkplain #AbstractLightPaint(int[][],
	 * Color[], int[], int[]) full constructor} for more details).</p>
	 * 
	 * @param index index of the light that should be rewritten (or added)
	 * @param point new coordinates of the average light location
	 * @param color new color of the light
	 * @param dilution new dilution of the light
	 * @param dim new relative dimming of the light
	 * 
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public void putLight(int index, int[] point, Color color,
		int dilution, int dim)
	{
		if (null != point && point.length >= 2)
		{
			if (index < 0) index = lightsCount;

			if (index >= lightsCount)
			{
				index = lightsCount;
				++lightsCount;
				if (lightsCount >= points.length)
					inflateBuffers(lightsCount);
			}

			points[index][0] = point[0];
			points[index][1] = point[1];

			if (null == color)
			{
				colors[index][0] = 0;
				colors[index][1] = 0;
				colors[index][2] = 0;
				colors[index][3] = 0;
			}
			else
			{
				colors[index][0] = color.getBlue();
				colors[index][1] = color.getGreen();
				colors[index][2] = color.getRed();
				colors[index][3] = color.getAlpha();
			}

			if (dilution > 1)
				dilutions[index] = dilution;
			else
				dilutions[index] = 1;

			if (dim > 1)
				dims[index] = dim;
			else
				dims[index] = 1;
		}
	}

	/**
	 * <p>Overwrites the light at specified index. If the index is less
	 * than zero, or greater or equal to current number of lights, a new
	 * light is add at the end of the lights’ list. Parameters having
	 * {@code null} or invalid value are handled the same way like in
	 * the constructors (see the {@linkplain #AbstractLightPaint(int[][],
	 * Color[], int[], int[]) full constructor} for more details).</p>
	 * 
	 * @param index index of the light that should be rewritten (or added)
	 * @param point new coordinates of the average light location
	 * @param color new color of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public void putLight(int index, int[] point, Color color)
	{
		if (null != point && point.length >= 2)
		{
			if (index < 0) index = lightsCount;

			if (index >= lightsCount)
			{
				index = lightsCount;
				++lightsCount;
				if (lightsCount >= points.length)
					inflateBuffers(points.length + 1);
			}

			points[index][0] = point[0];
			points[index][1] = point[1];

			if (null == color)
			{
				colors[index][0] = 0;
				colors[index][1] = 0;
				colors[index][2] = 0;
				colors[index][3] = 0;
			}
			else
			{
				colors[index][0] = color.getBlue();
				colors[index][1] = color.getGreen();
				colors[index][2] = color.getRed();
				colors[index][3] = color.getAlpha();
			}

			dilutions[index] = 1;
			dims[index] = 1;
		}
	}

	/**
	 * <p>Removes the light with specified index. If the index is out of
	 * bounds 0 – (lighnts count − 1), the method does not remove
	 * anything.</p>
	 * 
	 * @param index the index of light to be removed
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public void removeLight(int index)
	{
		if (index >= 0 && index < lightsCount)
		{
			if (index < lightsCount - 1)
			{
				// ‼You cannot do this‼ Your 2-dimensional arrays wolud jumble‼
				// ‼System.arraycopy(points,    index + 1, points,    index,
				// ‼	lightsCount - index - 1);
				// ‼System.arraycopy(colors,    index + 1, colors,    index,
				// ‼	lightsCount - index - 1);
				for (int i = index; i < lightsCount - 1; ++i)
				{
					System.arraycopy(points[i + 1], 0, points[i], 0, 2);
					System.arraycopy(colors[i + 1], 0, colors[i], 0, 4);
				}
				System.arraycopy(dilutions, index + 1, dilutions, index,
					lightsCount - index - 1);
				System.arraycopy(dims, index + 1, dims, index,
					lightsCount - index - 1);
			}
			--lightsCount;
		}
	}

	/**
	 * <p>Gets the current number of lights.</p>
	 * 
	 * @return number of lights in this paint instance
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public int getLightsCount()
	{
		return lightsCount;
	}

	/**
	 * <p>Gets location of the light with specified index. If the index
	 * if out of range, the value for nearest existing light is returned.
	 * If the pattern is empty, the {@code null} is returned.</p>
	 * 
	 * @param index index of the demanded light
	 * @return the average location of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public int[] getLightPoint(int index)
	{
		if (index >= lightsCount) index = lightsCount - 1;
		if (index < 0) index = 0;
		if (index >= lightsCount) return null;
		return points[index];
	}

	/**
	 * <p>Gets color of the light with specified index. If the index
	 * if out of range, the value for nearest existing light is returned.
	 * If the pattern is empty, the {@code null} is returned.</p>
	 * 
	 * @param index index of the demanded light
	 * @return the color of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public Color getLightColor(int index)
	{
		if (index >= lightsCount) index = lightsCount - 1;
		if (index < 0) index = 0;
		if (index >= lightsCount) return null;
		return new Color(
			colors[index][2],
			colors[index][1],
			colors[index][0],
			colors[index][3]);
	}

	/**
	 * <p>Gets dilution of the light with specified index. If the index
	 * if out of range, the value for nearest existing light is returned.
	 * If the pattern is empty, the value {@code 0} is returned (this value
	 * is not allowed for dilution).</p>
	 * 
	 * @param index index of the demanded light
	 * @return the dilution of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public int getLightDilution(int index)
	{
		if (index >= lightsCount) index = lightsCount - 1;
		if (index < 0) index = 0;
		if (index >= lightsCount) return 0;
		return dilutions[index];
	}

	/**
	 * <p>Gets relative dimming of the light with specified index. If the
	 * index if out of range, the value for nearest existing light is
	 * returned. If the pattern is empty, the value {@code 0} is returned
	 * (this value is not allowed for the relative dimming).</p>
	 * 
	 * @param index index of the demanded light
	 * @return the relative dimming of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public int getLightDim(int index)
	{
		if (index >= lightsCount) index = lightsCount - 1;
		if (index < 0) index = 0;
		if (index >= lightsCount) return 0;
		return dims[index];
	}

	/**
	 * <p>Sets the average location of the light specified by its index.
	 * If the index is out of bounds 0 – (lighnts count − 1), the method
	 * does not change anything.</p>
	 * 
	 * @param index the index of light to be changed
	 * @param point new average location of the light
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public void setLightPoint(int index, int[] point)
	{
		if (index >= 0 && index < lightsCount)
		{
			if (null != point && point.length >= 2)
			{
				points[index][0] = point[0];
				points[index][1] = point[1];
			}
		}
	}

	/**
	 * <p>Sets the color of the light specified by the index.
	 * If the index is out of bounds 0 – (lighnts count − 1), the method
	 * does not change anything.</p>
	 * 
	 * @param index the index of light to be changed
	 * @param color new color of the light; the {@code null} value means
	 *     transparent black
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightDilution(int, int)
	 * @see #setLightDim(int, int)
	 */
	public void setLightColor(int index, Color color)
	{
		if (index >= 0 && index < lightsCount)
		{
			if (null == color)
			{
				colors[index][0] = 0;
				colors[index][1] = 0;
				colors[index][2] = 0;
				colors[index][3] = 0;
			}
			else
			{
				colors[index][0] = color.getBlue();
				colors[index][1] = color.getGreen();
				colors[index][2] = color.getRed();
				colors[index][3] = color.getAlpha();
			}
		}
	}

	/**
	 * <p>Sets the dilution of the light specified by the index.
	 * If the index is out of bounds 0 – (lighnts count − 1), the method
	 * does not change anything.</p>
	 * 
	 * @param index the index of light to be changed
	 * @param dilution new dilution of the light; values less than 1
	 *     are set to 1
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDim(int, int)
	 */
	public void setLightDilution(int index, int dilution)
	{
		if (index >= 0 && index < lightsCount)
		{
			if (dilution > 1)
				dilutions[index] = dilution;
			else
				dilutions[index] = 1;
		}
	}

	/**
	 * <p>Sets the relative dimming of the light specified by the index.
	 * If the index is out of bounds 0 – (lighnts count − 1), the method
	 * does not change anything.</p>
	 * 
	 * @param index the index of light to be changed
	 * @param dim new relative dimming of the light; values less than 1
	 *     are set to 1
	 * 
	 * @see #putLight(int, int[], Color, int, int)
	 * @see #putLight(int, int[], Color)
	 * @see #removeLight(int)
	 * @see #getLightsCount()
	 * @see #getLightPoint(int)
	 * @see #getLightColor(int)
	 * @see #getLightDilution(int)
	 * @see #getLightDim(int)
	 * @see #setLightPoint(int, int[])
	 * @see #setLightColor(int, Color)
	 * @see #setLightDilution(int, int)
	 */
	public void setLightDim(int index, int dim)
	{
		if (index >= 0 && index < lightsCount)
		{
			if (dim > 1)
				dims[index] = dim;
			else
				dims[index] = 1;
		}
	}


	/**
	 * <p>The paint context of the {@link AbstractLightPaint
	 * AbstractLightPaint} class.</p>
	 * 
	 * @see PaintContext
	 */
	abstract class AbstractLightPaintContext implements PaintContext
	{
		/**
		 * <p>Current working image to provide the raster.</p>
		 */
		BufferedImage rasterImage;

		/**
		 * <p>Buffer of images of all demanded sizes for recycling. We always
		 * need the image of exact size, because we work with its data
		 * buffer.</p>
		 */
		private final TreeMap<Long, BufferedImage> images =
			new TreeMap<Long, BufferedImage>();

		/**
		 * <p>Default paint context constructor.</p>
		 */
		public AbstractLightPaintContext()
		{
			rasterImage = getImage(32, 32);
		}

		/**
		 * <p>Gets image of demanded size from the buffer.</p>
		 * 
		 * @param w demanded width
		 * @param h demanded height
		 * @return the image of demanded size
		 */
		BufferedImage getImage(int w, int h)
		{
			long size = (h << 32) | w;
			BufferedImage image = images.get(size);
			if (null == image)
			{
				image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				images.put(size, image);
			}
			return image;
		}

		/**
		 * <p>Releases the resources allocated for the operation.</p>
		 */
		@Override public void dispose() {}

		/**
		 * <p>Returns the {@link ColorModel ColorModel} of the output. Note
		 * that this {@link ColorModel ColorModel} might be different from
		 * the hint specified in the {@link Paint#createContext(ColorModel,
		 * Rectangle, Rectangle2D, AffineTransform, RenderingHints)
		 * createContext} method of {@link Paint Paint}. Not all {@link 
		 * PaintContext PaintContext} objects are capable of generating color
		 * patterns in an arbitrary {@link ColorModel ColorModel}.</p>
		 * 
		 * @return the {@link ColorModel ColorModel} of the output
		 */
		@Override synchronized public ColorModel getColorModel()
		{
			if (null == rasterImage) return null;
			return rasterImage.getColorModel();
		}

		/**
		 * <p>Returns a {@link Raster Raster} containing the colors generated
		 * for the graphics operation.</p>
		 * 
		 * @param x the {@code x} coordinate of the area in device space
		 *     for which colors are generated
		 * @param y the {@code y} coordinate of the area in device space
		 *     for which colors are generated
		 * @param w the width of the area in the device space
		 * @param h the height of the area in the device space
		 * @return a {@link Raster Raster} representing the specified
		 *     rectangular area and containing the colors generated for the
		 *     graphics operation
		 */
		@Override public abstract Raster getRaster(
			int x, int y, int w, int h);
	}


	/**
	 * <p>Creates and returns the {@link AbstractLightPaintContext
	 * AbstractLightPaintContext} instance that will be used to
	 * generate an arbitrary (multiple color free positioned) gradient
	 * pattern.</p>
	 * 
	 * @param colorModel {@code null} – method ignores this parameter
	 * @param deviceBounds {@code null} – method ignores this parameter
	 * @param userBounds {@code null} – method ignores this parameter
	 * @param transform {@code null} – method ignores this parameter
	 * @param hints {@code null} – method ignores this parameter
	 * @return the {@linkplain AbstractLightPaintContext arbitrary
	 *     gradient paint context} for generating the color patterns
	 */
	@Override public abstract PaintContext createContext(
		ColorModel colorModel, Rectangle deviceBounds,
		Rectangle2D userBounds, AffineTransform transform,
		RenderingHints hints);

	/**
	 * <p>Returns the transparency mode for this {@linkplain Paint paint
	 * object}.</p>
	 * 
	 * @return {@link #OPAQUE OPAQUE} if all colors used by this {@linkplain 
	 *     Paint paint object} are opaque; {@link #TRANSLUCENT TRANSLUCENT}
	 *     if at least one of the colors used by this {@linkplain Paint
	 *     paint object} object is not opaque
	 * 
	 * @see java.awt.Transparency
	 */
	@Override public final int getTransparency() { return transparency; }
}
