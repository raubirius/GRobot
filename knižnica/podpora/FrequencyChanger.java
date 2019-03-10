
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
 * <p>This interface is used by GeneratorChannel class to customize the change
 * of the channel playing frequency over time.</p>
 * 
 * @author Roman Horváth
 * @version 11. 12. 2016
 * 
 * @exclude
 */
public interface FrequencyChanger
{
	/**
	 * <p>This method recieves the timer value as integer holding the number
	 * of ticks elapsed from the start of playing and it should return the
	 * value of the new frequency for that specified time. This can also
	 * pause the channel – returning 0.0 as the frequency value.</p>
	 */
	public double getFrequency(int tick);
}
