
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Licencia a zdroje sú uvedené nižšiev anglickom
// jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

/**
 * <p>Interface for pseudorandom number generators. It is used by the class
 * {@link knižnica.podpora.BinomialDistribution}. (For the needs of the class
 * there are just two methods needed: {@link #isUniform()} and
 * {@link #nextDouble()}.)</p>
 * 
 * @author Roman Horváth
 * @version 14. 11. 2022
 * 
 * @exclude
 */
public interface RandomGenerator
{
	/**
	 * <p>Returns {@code true} if this generator produces a uniform
	 * distribution of random numbers.</p>
	 * 
	 * @return {@code true} if this generator produces a uniform random
	 *     numbers’ distribution; {@code false} otherwise
	 */
	boolean isUniform();

	/**
	 * <p>Returns the next pseudorandom {@code int} value between
	 * {@code Integer.MIN_VALUE} and {@code Integer.MAX_VALUE} from this
	 * random number generator’s sequence.</p>
	 * 
	 * @return the next pseudorandom value between {@code Integer.MIN_VALUE}
	 *     and {@code Integer.MAX_VALUE}
	 */
	int nextInt();

	/**
	 * <p>Returns the next pseudorandom {@code double} value between
	 * {@code 0.0} and {@code 1.0} from this random number generator’s
	 * sequence.</p>
	 * 
	 * @return the next pseudorandom value between {@code 0.0} and {@code 1.0}
	 */
	double nextDouble();

	/**
	 * <p>Returns the next pseudorandom {@code float} value between
	 * {@code 0.0f} and {@code 1.0f} from this random number generator’s
	 * sequence.</p>
	 * 
	 * @return the next pseudorandom value between {@code 0.0f} and
	 *     {@code 1.0f}
	 */
	float nextFloat();

	/**
	 * <p>Returns the next pseudorandom {@code long} value between
	 * {@code Long.MIN_VALUE} and {@code Long.MAX_VALUE} from this
	 * random number generator’s sequence.</p>
	 * 
	 * @return the next pseudorandom value between {@code Long.MIN_VALUE}
	 *     and {@code Long.MAX_VALUE}
	 */
	long nextLong();
}
