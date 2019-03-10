
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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>This class simply encapsulates two TreeMaps that store keys and values of
 * a bidirectional dictionary. The front map contains values sorted
 * (and accessible) by so-called keys (you can imagine them as the left column
 * of a dictionary) and the reverse map does the opposite – stores keys by
 * the values (you can imagine them as the right column of a dictionary).</p>
 * 
 * @author Roman Horváth
 * @version 19. 10. 2017 (the code), 2. 6. 2018 (Javadoc)
 * 
 * @exclude
 */
public class BiTreeMap<K, V>
{
	// The front map. (You need two maps to search the dictionary data quickly.)
	private final TreeMap<K, V> front = new TreeMap<K, V>();

	// The reversed map. (You need two maps to search the dictionary data quickly.)
	private final TreeMap<V, K> reverse = new TreeMap<V, K>();

	/**
	 * <p>Default constructor.</p>
	 */
	public BiTreeMap() {}

	/**
	 * <p>Constructor receiving dictionary initialisation data.</p>
	 * 
	 * @param k array of front map entries (the keys)
	 * @param v array of reverse map entries (the values)
	 */
	public BiTreeMap(K[] keys, V[] values)
	{
		if (null == keys)
			throw new NullPointerException("Array of keys cannot be null.");

		if (null == values)
			throw new NullPointerException("Array of values cannot be null.");

		if (keys.length != values.length)
			throw new RuntimeException("Lengths of both arrays must match.");

		for (int i = 0; i < keys.length; ++i)
		{
			if (front.containsKey(keys[i]))
				throw new RuntimeException("Duplicate key entry: " + keys[i]);

			if (reverse.containsKey(values[i]))
				throw new RuntimeException("Duplicate value entry: " + values[i]);

			front.put(keys[i], values[i]);
			reverse.put(values[i], keys[i]);
		}
	}


	/**
	 * <p>Clears the dictionary. (Clears both front and reverse map.)</p>
	 */
	public void clear()
	{
		front.clear();
		reverse.clear();
	}


	/**
	 * <p>Puts the entry into dictionary without throwing an exception, that means
	 * that the existing entries are overwriten.</p>
	 * 
	 * @param k the front map entry (the key)
	 * @param v the reverse map entry (the value)
	 */
	public void put(K k, V v)
	{
		if (front.containsKey(k)) removeKey(k);
		if (reverse.containsKey(v)) removeValue(v);

		front.put(k, v);
		reverse.put(v, k);
	}

	/**
	 * <p>Puts new entry into dictionary. If the entry (even by key or by value)
	 * exists a RuntimeException is thrown.</p>
	 * 
	 * @param k the front map entry (the key)
	 * @param v the reverse map entry (the value)
	 */
	public void putKey(K k, V v)
	{
		if (front.containsKey(k))
			throw new RuntimeException("Duplicate key entry: " + k);

		if (reverse.containsKey(v))
			throw new RuntimeException("Duplicate value entry: " + v);

		front.put(k, v);
		reverse.put(v, k);
	}

	/**
	 * <p>Puts new entry into dictionary. If the entry (even by key or by value)
	 * exists a RuntimeException is thrown.</p>
	 * 
	 * @param v the reverse map entry (the value)
	 * @param k the front map entry (the key)
	 */
	public void putValue(V v, K k)
	{
		if (front.containsKey(k))
			throw new RuntimeException("Duplicate key entry: " + k);

		if (reverse.containsKey(v))
			throw new RuntimeException("Duplicate value entry: " + v);

		front.put(k, v);
		reverse.put(v, k);
	}


	/**
	 * <p>Checks if the entry (by key or by value) exists in the dictionary.</p>
	 * 
	 * @param k the front map entry (the key)
	 * @param v the reverse map entry (the value)
	 * @return true or false value meaning the key or value existence
	 */
	public boolean contains(K k, V v)
	{
		return front.containsKey(k) ||
			reverse.containsKey(v);
	}

	/**
	 * <p>Checks if there is an entry with specified key within the dictionary.</p>
	 * 
	 * @param k the front map entry (the key)
	 * @return true or false value meaning the key existence
	 */
	public boolean containsKey(K k)
	{
		return front.containsKey(k);
	}

	/**
	 * <p>Checks if there is an entry with specified value within the
	 * dictionary.</p>
	 * 
	 * @param v the reverse map entry (the value)
	 * @return true or false value meaning the value existence
	 */
	public boolean containsValue(V v)
	{
		return reverse.containsKey(v);
	}


	/**
	 * <p>Gets the key by its value.</p>
	 * 
	 * @param v the reverse map entry (the value)
	 * @return key specified by the value or null
	 */
	public K getKey(V v)
	{
		return reverse.get(v);
	}

	/**
	 * <p>Gets the value by its key.</p>
	 * 
	 * @param k the front map entry (the key)
	 * @return value specified by the key or null
	 */
	public V getValue(K k)
	{
		return front.get(k);
	}


	/**
	 * <p>Removes the entry from dictionary by the key.</p>
	 * 
	 * @param k the front map entry (the key)
	 */
	public void removeKey(K k)
	{
		V v = front.get(k);
		front.remove(k);
		reverse.remove(v);
	}

	/**
	 * <p>Removes the entry from dictionary by the value.</p>
	 * 
	 * @param v the reverse map entry (the value)
	 */
	public void removeValue(V v)
	{
		K k = reverse.get(v);
		front.remove(k);
		reverse.remove(v);
	}


	/**
	 * <p>Gets front map iterator.</p>
	 * 
	 * @return entry set of the front map
	 */
	public Set<Map.Entry<K, V>> iterateKeys()
	{
		return front.entrySet();
	}

	/**
	 * <p>Gets reverse map iterator.</p>
	 * 
	 * @return entry set of the reverse map
	 */
	public Set<Map.Entry<V, K>> iterateValues()
	{
		return reverse.entrySet();
	}


	// Example
	//public static void main(String... args)
	//{
	//	String[] a = {"a", "c", "e"}, b = {"b", "d", "f"};
	//	BiTreeMap<String, String> t = new BiTreeMap<String, String>(a, b);
	//
	//	for (Map.Entry<String, String> entry : t.iterateKeys())
	//	{
	//		System.out.print(entry.getKey());
	//		System.out.print(": ");
	//		System.out.println(entry.getValue());
	//	}
	//
	//	System.out.println();
	//
	//	for (Map.Entry<String, String> entry : t.iterateValues())
	//	{
	//		System.out.print(entry.getKey());
	//		System.out.print(": ");
	//		System.out.println(entry.getValue());
	//	}
	//
	//	/* Output:
	//		a: b
	//		c: d
	//		e: f
	//
	//		b: a
	//		d: c
	//		f: e
	//	*/
	//}
}
