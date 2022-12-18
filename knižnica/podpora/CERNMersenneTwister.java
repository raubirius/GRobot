
package knižnica.podpora;

/*
 * Copyright © 1999 CERN – European Organization for Nuclear Research.
 *
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that copyright
 * notice and this permission notice appear in supporting documentation. CERN
 * makes no representations about the suitability of this software for any
 * purpose. It is provided “as is” without expressed or implied warranty.
 */

import java.util.Date;

/**
 * CERNMersenneTwister (MT19937) is one of the strongest uniform pseudo-random
 * number generators known so far; at the same time it is quick. Produces
 * uniformly distributed <tt>int</tt>’s and <tt>long</tt>’s in the closed
 * intervals <tt>[Integer.MIN_VALUE, Integer.MAX_VALUE]</tt> and
 * <tt>[Long.MIN_VALUE, Long.MAX_VALUE]</tt>, respectively, as well as
 * <tt>float</tt>’s and <tt>double</tt>’s in the half-open unit intervals
 * <tt>[0.0f, 1.0f)</tt> and <tt>[0.0, 1.0)</tt>, respectively. The seed can
 * be any 32-bit integer except <tt>0</tt>. Shawn J. Cokus commented that
 * perhaps the seed should preferably be odd.</p>
 * 
 * <p><b>Quality:</b> CERNMersenneTwister is designed to pass the
 * k-distribution test. It has an astronomically large period of
 * 2<sup>19937</sup> – 1 (= 10<sup>6001</sup>) and 623-dimensional
 * equidistribution up to 32-bit accuracy. It passes many stringent
 * statistical tests, including the <a
 * href="http://stat.fsu.edu/~geo/diehard.html">diehard</a> test of G.
 * Marsaglia and the load test of P. Hellekalek and S. Wegenkittl.</p>
 * 
 * <p><b>Performance:</b> Its speed is comparable to other modern generators
 * (in particular, as fast as <tt>java.util.Random.nextFloat()</tt>).
 * 2.5 million calls to <tt>raw()</tt> per second (Pentium Pro 200 Mhz,
 * JDK 1.2, NT). Be aware, however, that there is a non-negligible amount of
 * overhead required to initialize the data structures used by
 * a CERNMersenneTwister. Code like:
 * 
 * <pre>
	double sum = 0.0;
	for (int i = 0; i < 100000; ++i)
	{
		RandomElement twister = new CERNMersenneTwister(new java.util.Date());
		sum += twister.raw();
	}
	</pre>
 * 
 * will be wildly inefficient. Consider using:
 * 
 * <pre>
	double sum = 0.0;
	RandomElement twister = new CERNMersenneTwister(new java.util.Date());
	for (int i = 0; i < 100000; ++i) sum += twister.raw();
	</pre>
 * 
 * instead. This allows the cost of constructing the CERNMersenneTwister
 * object to be borne only once, rather than once for each iteration in the
 * loop.</p>
 * 
 * <p><b>Implementation:</b> After M. Matsumoto and T. Nishimura, “Mersenne
 * Twister: A 623-Dimensionally Equidistributed Uniform Pseudo-Random Number
 * Generator”, ACM Transactions on Modeling and Computer Simulation, Vol. 8,
 * No. 1, January 1998, pp 3–30.</p>
 * 
 * <dt>More info on <a href="http://www.math.keio.ac.jp/~matumoto/eindex.html">
 * Masumoto’s homepage</a>.</dt>
 * 
 * <dt>More info on <a
 * href="http://www.ncsa.uiuc.edu/Apps/CMP/RNG/www-rng.html"> Pseudo-random
 * number generators is on the Web</a>.</dt>
 * 
 * <dt>Yet <a href="http://nhse.npac.syr.edu/random"> some more info</a>.</dt>
 * 
 * <p>The correctness of this implementation has been verified against the
 * published output sequence <a
 * href="http://www.math.keio.ac.jp/~nisimura/random/real2/mt19937-2.out">mt19937-2.out</a>
 * of the C-implementation <a
 * href="http://www.math.keio.ac.jp/~nisimura/random/real2/mt19937-2.c">mt19937-2.c</a>.
 * (Call <tt>test(1000)</tt> to print the sequence).</p>
 * 
 * <dt>Note that this implementation is <b>not synchronized</b>.</dt>
 * 
 * <p><b>Details:</b> CERNMersenneTwister is designed with consideration of
 * the flaws of various existing generators in mind. It is an improved version
 * of TT800, a very successful generator. CERNMersenneTwister is based on
 * linear recurrences modulo 2. Such generators are very fast, have extremely
 * long periods, and appear quite robust. CERNMersenneTwister produces 32-bit
 * numbers, and every <tt>k</tt>-dimensional vector of such numbers appears
 * the same number of times as <tt>k</tt> successive values over the period
 * length, for each <tt>k &lt;= 623</tt> (except for the zero vector, which
 * appears one time less). If one looks at only the first <tt>n &lt;= 16</tt>
 * bits of each number, then the property holds for even larger <tt>k</tt>,
 * as shown in the following table (taken from the publication cited
 * above):</p>
 * 
 * <div align="center">
 * <table width="75%" border="1" cellspacing="0" cellpadding="0">
 * <tr>
 * <td width="2%"><div align="center">n</div></td>
 * <td width="6%"><div align="center">1</div></td>
 * <td width="5%"><div align="center">2</div></td>
 * <td width="5%"><div align="center">3</div></td>
 * <td width="5%"><div align="center">4</div></td>
 * <td width="5%"><div align="center">5</div></td>
 * <td width="5%"><div align="center">6</div></td>
 * <td width="5%"><div align="center">7</div></td>
 * <td width="5%"><div align="center">8</div></td>
 * <td width="5%"><div align="center">9</div></td>
 * <td width="5%"><div align="center">10</div></td>
 * <td width="5%"><div align="center">11</div></td>
 * <td width="10%"><div align="center">12 .. 16</div></td>
 * <td width="10%"><div align="center">17 .. 32</div></td>
 * </tr>
 * <tr>
 * <td width="2%"><div align="center">k</div></td>
 * <td width="6%"><div align="center">19937</div></td>
 * <td width="5%"><div align="center">9968</div></td>
 * <td width="5%"><div align="center">6240</div></td>
 * <td width="5%"><div align="center">4984</div></td>
 * <td width="5%"><div align="center">3738</div></td>
 * <td width="5%"><div align="center">3115</div></td>
 * <td width="5%"><div align="center">2493</div></td>
 * <td width="5%"><div align="center">2492</div></td>
 * <td width="5%"><div align="center">1869</div></td>
 * <td width="5%"><div align="center">1869</div></td>
 * <td width="5%"><div align="center">1248</div></td>
 * <td width="10%"><div align="center">1246</div></td>
 * <td width="10%"><div align="center">623</div></td>
 * </tr>
 * </table>
 * </div>
 * 
 * <p>CERNMersenneTwister generates random numbers in batches of 624 numbers
 * at a time, so the caching and pipelining of modern systems is exploited.
 * The generator is implemented to generate the output by using the fastest
 * arithmetic operations only: 32-bit additions and bit operations (no
 * division, no multiplication, no mod). These operations generate sequences
 * of 32 random bits (<tt>int</tt>’s). <tt>long</tt>’s are formed by
 * concatenating two 32-bit <tt>int</tt>’s. <tt>float</tt>’s are formed by
 * dividing the interval <tt>[0.0, 1.0]</tt> into 2<sup>32</sup> subintervals,
 * then randomly choosing one subinterval. <tt>double</tt>’s are formed by
 * dividing the interval <tt>[0.0, 1.0]</tt> into 2<sup>64</sup> subintervals,
 * then randomly choosing one subinterval.</p>
 *
 * @author wolfgang.hoschek@cern.ch
 * @version 1.0, 09/24/99
 * @see java.util.Random
 */
public class CERNMersenneTwister implements RandomGenerator
{
	private int mti;
	private int[] mt = new int[N];
		// set initial seeds: N = 624 words

	// Period parameters:
	private static final int N = 624;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908b0df;
		// constant vector a

	private static final int UPPER_MASK = 0x80000000;
		// most significant w – r bits

	private static final int LOWER_MASK = 0x7fffffff;
		// least significant r bits

	// For tempering:
	private static final int TEMPERING_MASK_B = 0x9d2c5680;
	private static final int TEMPERING_MASK_C = 0xefc60000;

	private static final int mag0 = 0x0;
	private static final int mag1 = MATRIX_A;
	// private static final int[] mag01 = new int[] {0x0, MATRIX_A};
	// mag01[x] = x * MATRIX_A  for x = 0, 1

	public static final int DEFAULT_SEED = 4357;

	/**
	 * <p>Constructs and returns a random number generator with <!--a default
	 * seed, which is a <b>constant</b>. Thus using this constructor will
	 * yield generators that always produce exactly the same sequence.
	 * This method is mainly intended to ease testing and debugging.--> seed
	 * created from curent system startup time: System.currentTimeMillis().</p>
	 * 
	 * <p>To get generator initialized by a constant value call {@linkplain 
	 * CERNMersenneTwister#CERNMersenneTwister(int) other constructor},
	 * e.g.:</p>
	 * <pre>
		CERNMersenneTwister twister = new CERNMersenneTwister(
			CERNMersenneTwister.DEFAULT_SEED);
		</pre>
	 */
	public CERNMersenneTwister()
	{
		// this(DEFAULT_SEED);
		this((int)System.currentTimeMillis());
	}

	/**
	 * <p>Constructs and returns a random number generator with the given
	 * seed.</p>
	 * 
	 * @param seed initial seed for this generator.
	 */
	public CERNMersenneTwister(int seed)
	{
		setSeed(seed);
	}

	/**
	 * <p>Constructs and returns a random number generator seeded with the
	 * given date.</p>
	 *
	 * @param d typically <tt>new java.util.Date()</tt>.
	 */
	public CERNMersenneTwister(Date d)
	{
		this((int)d.getTime());
	}

	/**
	 * <p>Returns a copy of the receiver; the copy will produce identical
	 * sequences. After this call has returned, the copy and the receiver
	 * have equal but separate state.</p>
	 *
	 * @return a copy of the receiver.
	 */
	public Object clone()
	{
		try
		{
			CERNMersenneTwister clone = (CERNMersenneTwister)super.clone();
			clone.mt = // (int[]) // warning: [cast] redundant cast to int[]
				this.mt.clone();
			return clone;
		}
		catch (CloneNotSupportedException exc)
		{
			throw new InternalError(exc);
		}
	}


	/**
	 * <p>Returns {@code true}, because this generator produces a uniform
	 * distribution of random numbers.</p>
	 * 
	 * @return {@code true}
	 */
	public boolean isUniform() { return true; }


	// To test the differences:
	// public boolean optimised = true;

	/**
	 * <p>Generates N words at one time.</p>
	 */
	protected void nextBlock()
	{
		// To test the differences:
		/*if (optimised)
		{*/
			// ******************** OPTIMIZED **********************
			// Only 5–10% faster?
			int y, kk;

			int[] cache = mt; // cached for speed
			int kkM;
			int limit = N - M;

			for (kk = 0, kkM = kk + M; kk < limit; ++kk, ++kkM)
			{
				y = (cache[kk] & UPPER_MASK) | (cache[kk + 1] & LOWER_MASK);
				cache[kk] = cache[kkM] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
					mag0 : mag1);
			}

			limit = N - 1;

			for (kkM = kk + (M - N); kk < limit; ++kk, ++kkM)
			{
				y = (cache[kk] & UPPER_MASK) | (cache[kk + 1] & LOWER_MASK);
				cache[kk] = cache[kkM] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
					mag0 : mag1);
			}

			y = (cache[N - 1] & UPPER_MASK) | (cache[0] & LOWER_MASK);
			cache[N - 1] = cache[M - 1] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
				mag0 : mag1);

			this.mt = cache;
			this.mti = 0;
		/*}
		else
		{
			// ******************** UNOPTIMIZED **********************
			int y, kk;

			for (kk = 0; kk < N - M; ++kk)
			{
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + M] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
					mag0 : mag1);
			}

			for (; kk < N - 1; ++kk)
			{
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
					mag0 : mag1);
			}

			y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
			mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ ((y & 0x1) == 0 ?
				mag0 : mag1);

			this.mti = 0;
		}*/
	}


	/**
	 * <p>Returns a 32-bit uniformly distributed random number in the closed
	 * interval <tt>[Integer.MIN_VALUE, Integer.MAX_VALUE]</tt> (including
	 * <tt>Integer.MIN_VALUE</tt> and <tt>Integer.MAX_VALUE</tt>).</p>
	 * 
	 * @return 32-bit uniformly distributed random integer (see above).
	 */
	public int nextInt()
	{
		// Each single bit including the sign bit will be random:
		if (mti == N) nextBlock(); // generate N ints at one time

		int y = mt[mti++];

		y ^= y >>> 11;
		// y ^= TEMPERING_SHIFT_U(y);

		y ^= (y << 7) & TEMPERING_MASK_B;
		// y ^= TEMPERING_SHIFT_S(y) & TEMPERING_MASK_B;

		y ^= (y << 15) & TEMPERING_MASK_C;
		// y ^= TEMPERING_SHIFT_T(y) & TEMPERING_MASK_C;

		// y &= 0xffffffff; // you may delete this line if word size = 32

		y ^= y >>> 18; // y ^= TEMPERING_SHIFT_L(y);

		return y;
	}


	/**
	 * <p>Returns a 64-bit uniformly distributed random number in the half-open
	 * unit interval <code>[0.0, 1.0)</code> (including 0.0 and excluding
	 * 1.0).</p>
	 * 
	 * @return 64-bit uniformly distributed random number (see above).
	 */
	public double nextDouble()
	{
		double nextDouble;

		do
		{
			// -9.223372036854776E18 == (double)Long.MIN_VALUE
			// 5.421010862427522E-20 == 1 / Math.pow(2, 64) == 1 /
			// 	((double)Long.MAX_VALUE - (double)Long.MIN_VALUE);
			nextDouble = ((double)nextLong() +
				9.223372036854776E18) * 5.421010862427522E-20;
		}
		// catch loss of precision of long —> double conversion
		while (nextDouble < 0.0 || nextDouble >= 1.0);

		// —> in [0.0, 1.0)
		return nextDouble;

		/*
			nextLong == Long.MAX_VALUE           —> 1.0
			nextLong == Long.MIN_VALUE           —> 0.0
			nextLong == Long.MAX_VALUE - 1       —> 1.0
			nextLong == Long.MAX_VALUE - 100000L —> 0.9999999999999946
			nextLong == Long.MIN_VALUE + 1       —> 0.0
			nextLong == Long.MIN_VALUE - 100000L —> 0.9999999999999946
			nextLong == 1L                       —> 0.5
			nextLong == -1L                      —> 0.5
			nextLong == 2L                       —> 0.5
			nextLong == -2L                      —> 0.5
			nextLong == 2L + 100000L             —> 0.5000000000000054
			nextLong == -2L - 100000L            —> 0.49999999999999456
		*/
	}


	/**
	 * <p>Returns a 32-bit uniformly distributed random number in the half-open
	 * unit interval <code>[0.0f, 1.0f)</code> (including 0.0f and excluding
	 * 1.0f).</p>
	 * 
	 * @return 32-bit uniformly distributed random real number (see above).
	 */
	public float nextFloat()
	{
		// catch loss of precision of double —> float conversion
		float nextFloat;
		do { nextFloat = (float)raw(); }
		while (nextFloat >= 1.0f);

		// —> in [0.0f, 1.0f)
		return nextFloat;
	}


	/**
	 * <p>Returns a 64-bit uniformly distributed random number in the closed
	 * interval <tt>[Long.MIN_VALUE, Long.MAX_VALUE]</tt> (including
	 * <tt>Long.MIN_VALUE</tt> and <tt>Long.MAX_VALUE</tt>).</p>
	 * 
	 * @return 64-bit uniformly distributed random integer (see above).
	 */
	public long nextLong()
	{
		// Concatenate two 32-bit strings into one 64-bit string:
		return ((nextInt() & 0xFFFFFFFFL) << 32) |
			((nextInt() & 0xFFFFFFFFL));
	}


	/**
	 * <p>Returns a 32-bit uniformly distributed random number in the half-open
	 * unit interval <code>[0.0, 1.0)</code> (including 0.0 and excluding
	 * 1.0).</p>
	 * 
	 * @return 32-bit uniformly distributed random real number (see above).
	 */
	public double raw()
	{
		/*
		int nextInt;

		// Accept anything but zero in interval ⟨Integer.MIN_VALUE,
		// Integer.MAX_VALUE⟩:
		do { nextInt = nextInt(); } while (0 == nextInt);

		// Transform to interval (0.0, 1.0):
		// 2.3283064365386963E-10 == 1.0 / Math.pow(2, 32)
		return (double)(nextInt & 0xFFFFFFFFL) * 2.3283064365386963E-10;
		*/

		// Accept anything in interval ⟨Integer.MIN_VALUE, Integer.MAX_VALUE⟩
		// and transform it to interval [0.0, 1.0), where
		// 2.3283064365386963E-10 == 1.0 / Math.pow(2, 32).
		return (double)(nextInt() & 0xFFFFFFFFL) * 2.3283064365386963E-10;

		/*
			nextInt == Integer.MAX_VALUE     —> 0.49999999976716936
			nextInt == Integer.MIN_VALUE     —> 0.5
			nextInt == Integer.MAX_VALUE - 1 —> 0.4999999995343387
			nextInt == Integer.MIN_VALUE + 1 —> 0.5000000002328306
			nextInt == 1                     —> 2.3283064365386963E-10
			nextInt == -1                    —> 0.9999999997671694
			nextInt == 2                     —> 4.6566128730773926E-10
			nextInt == -2                    —> 0.9999999995343387
		*/
	}


	/**
	 * <p>Sets the receiver’s seed. This method resets the receiver’s entire
	 * internal state.</p>
	 * 
	 * @param seed the new seed for the generator.
	 */
	public void setSeed(int seed)
	{
		mt[0] = seed & 0xffffffff;
		for (int i = 1; i < N; ++i)
		{
			mt[i] = (1812433253 * (mt[i - 1] ^ (mt[i - 1] >> 30)) + i);
			/* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
			/* In the previous versions, MSBs of the seed affect   */
			/* only MSBs of the array mt[].                        */
			/* 2002/01/09 modified by Makoto Matsumoto             */
			mt[i] &= 0xffffffff;
			/* for >32-bit machines */
		}
		mti = N;

		/* * /
		// The old version was:
		for (int i = 0; i < N; ++i)
		{
			mt[i] = seed & 0xffff0000;
			seed = 69069 * seed + 1;
			mt[i] |= (seed & 0xffff0000) >>> 16;
			seed = 69069 * seed + 1;
		}
		mti = N;
		/* */
	}
}
