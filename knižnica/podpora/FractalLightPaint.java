
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Rozširuje abstraktnú triedu AbstractLightPaint
// a slúži na generovanie svetelného náteru tvoriaceho fraktálom podobné
// difúzne svetelné obrazce. Licencia a zdroje sú uvedené nižšie v anglickom
// jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import java.util.Arrays;

/**
 * <p>This class provides custom paint type. The {@linkplain Paint paint}
 * is composed form several lights definition from which it will create
 * a nice fractal light pattern.</p>
 * 
 * <p><small>(Due its computational complexity this class is not intended
 * to be used in real-time renderings.)</small></p>
 * 
 * @author Roman Horváth
 * @version 20. 7. 2018
 * 
 * @exclude
 */
public class FractalLightPaint extends AbstractLightPaint
{
	/**
	 * <p>The paint context of the {@link FractalLightPaint
	 * FractalLightPaint} class.</p>
	 * 
	 * @see PaintContext
	 */
	public class FractalLightPaintContext extends AbstractLightPaintContext
	{
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
		@Override synchronized public Raster getRaster(
			int x, int y, int w, int h)
		{
			rasterImage = getImage(w, h);

			int[] rasterData = ((DataBufferInt)rasterImage.getRaster().
				getDataBuffer()).getData();

			if (0 == lightsCount)
			{
				Arrays.fill(rasterData, 0);
			}
			else if (1 == lightsCount)
			{
				int color =
					(colors[0][3] << 24) |
					(colors[0][2] << 16) |
					(colors[0][1] << 8)  |
					 colors[0][0];
				Arrays.fill(rasterData, color);
			}
			else
			{
				// computingStack indexes:
				//   0 – Δx
				//   1 – Δy
				//   2 – Dₘ = m × ((Δx − k)² + (Δy − j)²) + d

				// Compute Δx and Δy:
				for (int l = 0; l < lightsCount; ++l)
				{
					computingStack[l][0] = points[l][0] - x;
					computingStack[l][1] = points[l][1] - y;
				}

				// Fill the raster:
				for (int j = 0, i = 0; j < h; ++j)
					for (int k = 0; k < w; ++k, ++i)
					{
						int xx = computingStack[0][0] - k;
						int yy = computingStack[0][1] - j;
						int max = computingStack[0][2] = dims[0] *
							(xx * xx + yy * yy) + dilutions[0];

						// Compute Dₘ and find max:
						for (int l = 1; l < lightsCount; ++l)
						{
							xx = computingStack[l][0] - k;
							yy = computingStack[l][1] - j;
							computingStack[l][2] = dims[l] *
								(xx * xx + yy * yy) + dilutions[l];
							if (max < computingStack[l][2])
								max = computingStack[l][2];
						}

						/*if (0 == max)
						{
							System.err.println("This should not happen.");
							for (int l = 0; l < lightsCount; ++l)
								if (0 == computingStack[l][2])
								{
									rasterData[i] =
										(colors[l][3] << 24) |
										(colors[l][2] << 16) |
										(colors[l][1] << 8)  |
										 colors[l][0];
								}
						}
						else
						{*/
							int b = 0, g = 0, r = 0, a = 0, Σ = 0;

							for (int l = 0; l < lightsCount; ++l)
							{
								Σ += max / computingStack[l][2];
								b += colors[l][0] * max /
									computingStack[l][2];
								g += colors[l][1] * max /
									computingStack[l][2];
								r += colors[l][2] * max /
									computingStack[l][2];
								a += colors[l][3] * max /
									computingStack[l][2];
							}

							b /= Σ; g /= Σ; r /= Σ; a /= Σ;

							if (b < 0) b = 0; else if (b > 255) b = 255;
							if (g < 0) g = 0; else if (g > 255) g = 255;
							if (r < 0) r = 0; else if (r > 255) r = 255;
							if (a < 0) a = 0; else if (a > 255) a = 255;

							rasterData[i] = (r << 16) |
								(g << 8) | b | (a << 24);
						// }
					}
			}

			return rasterImage.getRaster();
		}
	}

	/**
	 * <p>Creates and returns the {@link FractalLightPaintContext
	 * FractalLightPaintContext} instance that will be used to
	 * generate an arbitrary (multiple color free positioned) gradient
	 * pattern.</p>
	 * 
	 * @param colorModel {@code null} – method ignores this parameter
	 * @param deviceBounds {@code null} – method ignores this parameter
	 * @param userBounds {@code null} – method ignores this parameter
	 * @param transform {@code null} – method ignores this parameter
	 * @param hints {@code null} – method ignores this parameter
	 * @return the {@linkplain FractalLightPaintContext arbitrary
	 *     gradient paint context} for generating the color patterns
	 */
	@Override synchronized public PaintContext createContext(
		ColorModel colorModel, Rectangle deviceBounds,
		Rectangle2D userBounds, AffineTransform transform,
		RenderingHints hints)
	{ return new FractalLightPaintContext(); }
}
