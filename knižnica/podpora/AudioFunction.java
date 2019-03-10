
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je súčasťou pôvodného balíčka generator slúžiaceho
// na generovanie zvukov v reálnom čase. Licencia a zdroje sú uvedené nižšie
// v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

/**
 * <p>This interface is used by GeneratorChannel class to customize the audio
 * generating function.</p>
 * 
 * @author Roman Horváth
 * @version 11. 12. 2016
 * 
 * @exclude
 */
public interface AudioFunction
{
	/**
	 * <p>This method recieves the position of desired sample which is the
	 * current phase in radians and it should return the audio signal
	 * sample for the position that should be the value between -1.0 and 1.0.
	 * The simplest implementation is:</p>
	 * 
	 * <pre>		return Math.sin(currentPosition); // Generate sine wave…</pre>
	 */
	public double compute(double currentPosition);
}
