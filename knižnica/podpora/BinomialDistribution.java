
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Licencia a zdroje sú uvedené nižšie v anglickom
// jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;

/**
 * <p>Implementation of the binomial distribution. When the pseudorandom
 * generator passed to this class produces a uniform distribution, it
 * precomputes the scale of probabilities for the range 0 – 1 and then
 * selects the resulting integer using that scale. When the pseudorandom
 * generator passed to this class produces a non-uniform distribution, this
 * implementation uses the Galton board quasisimulation to generate the random
 * numbers. It is not as fast for a big number of trials (n), but the
 * precomputation would be too complicated (if even possible).</p>
 * 
 * <p>(The default, internal, generator is CERNMersenneTwister (MT19937)
 * that produces uniform distribution.)</p>
 * 
 * <p>In general, the distribution needs two parameters: the number of
 * trials (n) and the probability of success (p).</p>
 * 
 * <p>In the context of precomputing the scale: The formula taken from
 * Wolfram MathWorld is used with the BigDecimal class to create an increasing
 * scale made from values of probabilities. The algorithm was considerably
 * inspired by Casey Connor’s (2014) implementation released under the GNU
 * license (https://lacinato.com/cm/software/othersoft/binomdist). The integer
 * value is then selected from the scale using a passed random number
 * generator to generate a value from the scale and a binary search algorithm
 * to find a fitting range precomputed from probabilities.</p>
 * 
 * <p>In context of the Galton board simulation: The number of trials is
 * concurrently the number of rows in the simulated Galton board. The
 * probability of success concurrently specifies the boundary between moving
 * the ball to the left or right while falling through the board.</p>
 * 
 * <!-- The details are explained inside the nextInt method (below). -->
 * 
 * @author Roman Horváth
 * @version 26. 7. 2022
 * 
 * @see <a
 * href="https://mathworld.wolfram.com/BinomialDistribution.html">Binomial
 * Distribution (Wolfram MathWorld).</a>
 * @see <a href="https://en.wikipedia.org/wiki/Binomial_distribution">Binomial
 * distribution (Wikipedia).</a>
 * @see <a href="https://galtonboard.com/">Galton Board (Four Pines
 * Publishing).</a>
 * @see <a href="https://en.wikipedia.org/wiki/Galton_board">Galton board
 * (Wikipedia).</a>
 * 
 * @exclude
 */
public class BinomialDistribution
{
	// The default random generator to generate pseudorandom numbers:
	private final static RandomGenerator
		defaultGenerator = new CERNMersenneTwister();

	// The number of trials:
	private int n;

	// The probability of success and the inverse of it:
	private double p, q;

	// A RandomGenerator implementation to generate pseudorandom numbers:
	private RandomGenerator rand;

	// Flag of Galton board simulation method use:
	private boolean useGalton;

	// Flag specifying if the initialisation is already done:
	private boolean initDone;

	// The splits for non-Galtonian generator:
	private double splits[];

	// Creates the table of splits for non-Galtonian generator:
	private void recompute()
	{
		if (useGalton) return;
		if (splits.length != n) splits = new double[n];

		if (0 == n) return;
		if (1 == n)
		{
			splits[0] = p;
			return;
		}

		// // Some “fancy” experiment…
		// for (int i = 0; i < n; ++i) splits[i] = 1.0;
		// int m = n - 1;
		// for (int j = 0; j < m; ++j)
		// {
		// 	// for (int i = j; i < m - j; ++i) splits[i] /= q;
		// 	// Rewritten:
		// 	for (int i = m - j - 1; i >= j; --i) splits[i] /= q;
		// 	for (int i = 1 + j; i < n - j; ++i) splits[i] /= p;
		// }
		// 
		// for (int i = 0; i < n; ++i) System.out.print(splits[i] + " ");
		// System.out.println();

		/*	The original static double[] binomialDistTable(int numTrials,
			double probValue) method body: * /
			BigDecimal probValueBD = new BigDecimal(probValue);
			BigDecimal invProbValue = BigDecimal.ONE.subtract(probValueBD);

			BigDecimal factorial[] = new BigDecimal[numTrials + 1]; // (must
				// be +1; we will need also the factorial of numTrials)
			factorial[0] = factorial[1] = BigDecimal.ONE;
			for (int i = 2; i <= numTrials; ++i)
				factorial[i] = factorial[i - 1].multiply(new BigDecimal(i));

			BigDecimal table[] = new BigDecimal[numTrials + 1];
				// (+1 for full scale)

			// (numCorrect <= for full scale; the last term is, however, equal
			// to the sum of all terms subtracted from a one)
			for (int numCorrect = 0; numCorrect <= numTrials; ++numCorrect)
			{
				int numFailed = numTrials - numCorrect;

				BigDecimal denom = factorial[numCorrect]
					.multiply(factorial[numFailed]);
				BigDecimal quotient = factorial[numTrials].divide(
					denom, 40, RoundingMode.HALF_UP); // 40 – BigDecimal scale

				BigDecimal rest = probValueBD.pow(numCorrect)
					.multiply(invProbValue.pow(numFailed));

				table[numCorrect] = quotient.multiply(rest);
			}

			// Final conversion and return the table (all according to above)…
			double table2[] = new double[numTrials + 1];
			for (int i = 0; i <= numTrials; ++i)
				table2[i] = table[i].doubleValue();
			return table2;
		/* */

		BigDecimal pBD = new BigDecimal(p);
		BigDecimal p_i = BigDecimal.ONE.subtract(pBD);

		BigDecimal factorial[] = new BigDecimal[n + 1];
		factorial[0] = factorial[1] = BigDecimal.ONE;

		for (int i = 2; i <= n; ++i)
			factorial[i] = factorial[i - 1].multiply(new BigDecimal(i));

		BigDecimal table[] = new BigDecimal[n];

		// Precompute the first value:
		// Notes: 0! = 1; p⁰ = 1; so the quotient also is: (n! / n!) = 1;
		// thus the first value is: (1 − p)ⁿ
		table[0] = p_i.pow(n);

		for (int i = 1, j = n - 1; i < n; ++i, --j)
		{
			BigDecimal denom = factorial[i].multiply(factorial[j]);
			BigDecimal quotient = factorial[n].divide(denom, 40, HALF_UP);
			BigDecimal rest = pBD.pow(i).multiply(p_i.pow(j));

			// Current probability adds to the (sum of) previous ones to
			// get the continuous scale:
			table[i] = table[i - 1].add(quotient.multiply(rest));
		}

		for (int i = 0; i < n; ++i)
			splits[i] = table[i].doubleValue();
	}

	/**
	 * <p>Creates a binomial distribution with the given number of trials (n)
	 * and probability of success (p). This constructor uses internal default
	 * random number generator – as if the {@code generator} parameter in
	 * {@link #setGenerator(RandomGenerator) setGenerator} method would be
	 * {@code null}.</p>
	 * 
	 * @param n number of trials
	 * @param p probability of success
	 * 
	 * @throws IllegalArgumentException if the probability of success (p) is
	 *     outside a closed interval of 0.0 and 1.0 or number of trials (n) is
	 *     negative
	 */
	public BinomialDistribution(int n, double p)
	{
		initDone = false;
		setGenerator(null);
		setParameters(n, p);
		splits = new double[n];
		recompute();
		initDone = true;
	}

	/**
	 * <p>Creates a binomial distribution with the given number of trials (n),
	 * probability of success (p), and {@link RandomGenerator RandomGenerator}
	 * implementation.</p>
	 * 
	 * @param n number of trials
	 * @param p probability of success
	 * @param generator a generator instance (or {@code null} for default
	 *     generator)
	 * 
	 * @throws IllegalArgumentException if the probability of success (p) is
	 *     outside a closed interval of 0.0 and 1.0 or number of trials (n) is
	 *     negative
	 */
	public BinomialDistribution(int n, double p, RandomGenerator generator)
	{
		initDone = false;
		setGenerator(generator);
		setParameters(n, p);
		splits = new double[n];
		recompute();
		initDone = true;
	}

	/**
	 * <p>Gets current {@link RandomGenerator RandomGenerator} implementation
	 * for this distribution. If the generator used is the default generator,
	 * the return value will be {@code null}.</p>
	 * 
	 * @return current generator instance or {@code null}
	 */
	public RandomGenerator getGenerator()
	{
		if (defaultGenerator == rand) return null;
		return rand;
	}

	/**
	 * <p>Sets new {@link RandomGenerator RandomGenerator} implementation
	 * for this distribution. If you want to use the default generator, enter
	 * the {@code null} value.</p>
	 * 
	 * @param generator new generator instance or {@code null}
	 */
	public void setGenerator(RandomGenerator generator)
	{
		if (null == generator) rand = defaultGenerator;
		else rand = generator;
		useGalton = !rand.isUniform();
		if (initDone) recompute();
	}

	/**
	 * <p>Gets the number of trials (n) for this distribution.</p>
	 * 
	 * @return number of trials
	 */
	public int getNumberOfTrials()
	{
		return n;
	}

	/**
	 * <p>Gets the probability of success (p) for this distribution.</p>
	 * 
	 * @return probability of success
	 */
	public double getProbabilityOfSuccess()
	{
		return p;
	}

	/**
	 * <p>Sets the number of trials (n) and the probability of success (p)
	 * for this distribution.</p>
	 * 
	 * @param n number of trials
	 * @param p probability of success
	 * 
	 * @throws IllegalArgumentException if the probability of success (p) is
	 *     outside a closed interval of 0.0 and 1.0 or number of trials (n) is
	 *     negative
	 */
	public void setParameters(int n, double p)
	{
		if (p < 0.0 || p > 1.0)
			throw new IllegalArgumentException(
				"The probability of success (p) must be within " +
				"a closed interval of 0.0 and 1.0.");

		if (n < 0)
			throw new IllegalArgumentException(
				"The number of trials (n) must be non-negative.");

		this.n = n;
		this.p = p;
		q = 1.0 - p;

		if (initDone) recompute();
	}

	/**
	 * <p>Returns a random number from the distribution.</p>
	 * 
	 * @return random number from the distribution
	 */
	public int nextInt()
	{
		if (0.0 == p) return 0;
		if (1.0 == p) return n;

		if (0 == n) return 0;
		if (1 == n) return rand.nextDouble() > p ? 0 : 1;
		// {
		// 	// TEST of 1 == n:
		// 	double prop = -1; int retval = -1; try { return retval = ((prop =
		// 	rand.nextDouble()) > p ? 0 : 1); } finally { System.out.println(
		// 	"prop: " + prop + "; retval: " + retval);; }
		// }

		if (useGalton)
		{
			// A Galton board has n rows and n + 1 columns. A falling ball
			// starts falling at the center of the distribution (n / 2). The
			// probability of success specifies on each row whether the ball
			// moves to the left or to the right. However, there is a catch.
			// Each row is horizontally shifted by half of the row distance
			// from the previous one. This means that every odd or even row we
			// have to change the decision of what the “shift left or right”
			// means.  The easiest way is to divide the procedure into pairs
			// of steps so that each od and even condition would be the same.
			// In an even row, only a shift to the right will mean a change
			// of position by increasing the position (column) of the ball.
			// In the odd case, a change of position will mean only a shift
			// to the left by decreasing the position (column) of the ball.
			// In other cases, the ball remains in the same position (in the
			// same column).

			int row = n;
			int column = n / 2;

			// If the number of attempts is odd, we get one “extra” row there,
			// so we calculate it in advance because by the next division we
			// will “deduct” it from the total number of processed pairs anyway.
			if (1 == row % 2)
			{
				// This is an odd row, but the integer column division (above)
				// actually threw it out, so we have to treat it as even
				// (otherwise, for example, in the single trial we would get
				// a negative value with the probability 1 − p):
				// 
				// if (rand.nextDouble() > p) --column; // (odd row)
				// 
				if (rand.nextDouble() <= p) ++column;

				// --row; // (no need, integer division below will do it)
			}

			row >>= 1; // (integer division by 2)

			while (row > 0)
			{
				if (rand.nextDouble() <= p) ++column; // (even row)
				if (rand.nextDouble() > p) --column; // (odd row)
				--row;
			}

			return column;
		}

		double value = rand.nextDouble();

		if (value < splits[0]) return 0;
		if (value >= splits[n - 1]) return n;
		if (2 == n) return n / 2;

		int left = 1;
		int right = n - 1;
		int middle = (left + right) / 2;

		while (left != right)
		{
			if (value >= splits[middle])
				left = middle + 1;
			else if (value < splits[middle - 1])
				right = middle - 1;
			else
				return middle;
			middle = (left + right) / 2;
		}

		// If it comes here, the search was “unsuccessful.” This occurs always
		// when left gets equal to right (special case when n = 2 and the
		// random value is “in the middle” is filtered out in advance).

		return middle;
	}


	/**
	 * <p>Computes the numerical value of the mean of this distribution.</p>
	 * 
	 *  @return the mean of this distribution
	 */
	public double getMean()
	{
		return n * p;
	}

	/**
	 * <p>Computes the numerical value of the variance of this
	 * distribution.</p>
	 * 
	 *  @return the variance of this distribution
	 */
	public double getVariance()
	{
		return n * p * q;
	}

	/**
	 * <p>Computes the standard deviation of this distribution.</p>
	 * 
	 *  @return the standard deviation of this distribution
	 */
	public double getStandardDeviation()
	{
		return Math.sqrt(getVariance());
	}


	/**
	 * <p>Gets the lower bound of this distribution (of the support).</p>
	 * 
	 * @return lower bound of this distribution (0 or the number of trials)
	 */
	public int getLowerBound()
	{
		return p < 1.0 ? 0 : n;
	}

	/**
	 * <p>Gets the upper bound of this distribution (of the support).</p>
	 * 
	 * @return upper bound of this distribution (number of trials or 0)
	 */
	public int getUpperBound()
	{
		return p > 0.0 ? n : 0;
	}


	/**
	 * <p>Returns a string with brief information about this distribution.</p>
	 * 
	 * @return information about this distribution
	 */
	public String toString()
	{
		return getClass().getSimpleName() + "{n=" + n + "; p=" + p + "}";
	}


	// TODO: static getPoissonLike(λ) with variant getPoissonLike(λ, N)
	// If N → ∞ and p → 0 in such a way that Np → λ, then the binomial
	// distribution converges to the Poisson distribution with mean λ.
	// See: https://mathworld.wolfram.com/BinomialDistribution.html
}
