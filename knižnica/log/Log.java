
package knižnica.log;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Vznikla pôvodne ako súčasť iného projektu (SHO)
// s iným názvom (Debug), ale keďže sa osvedčila, bola presunutá sem, do
// knižnice a bola premenovaná (pôvodný názov nevyjadroval presne jej účel –
// trieda nemá, ani nemala žiadne ladiace schopnosti vo význame skutočného
// krokovania v procese ladenia). Slúži na zjednodušenie tvorby ladiacich
// výpisov (denníka – logu) a pozostáva výhradne zo statických prvkov.
// Licencia a zdroje sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.util.function.Function;
// import static java.lang.System.*;

/**
 * <p>This class is intended to simplify the creation of logging dumps.
 * It contains exclusively static elements.</p>
 * 
 * <p><b>Example of recommended usage:</b></p>
 * 
 * <pre>

	// Static import:
	import static knižnica.log.Log.*;
		// 
		// You can also import it as non-static class. Then you’ll have to
		// prefix each method (logIn, logOut…) with the class name like
		// this:
		// 	Log.logIn();
		// 
		// On the other hand, static import may get some parts in conflict
		// with your project, but this is always the risk of using static
		// import. The example uses it because it may be more convenient.
		// 
	<hr />
	void plainMethod()
	{
		// Tip: You can always add optional log info in the logIn.
		logIn(); try {

			// Method code…

			// Some optional log dumps…

		} finally { logOut(); }
		// Tip: You can always add optional log info in the logOut.
	}

	void methodParams(int x, int y)
	{
		logIn("x: ", x, ", y: ", y); try {

			// Method code…

		} finally { logOut(); }
	}

	int methodParamsAndRetval(int x, int y)
	{
		// Define some helper variable with the same
		// type as the return value of the method here:
		logIn("x: ", x, ", y: ", y); int retval = -1; try {

			// Method code…

		return retval = (x * 4 + y); // Do this with each return.

		} finally { logOut(retval); } // Use the helper variable here.
	}

	</pre>
 * 
 * @author Roman Horváth
 * @version 30. 7. 2023
 * 
 * @exclude
 */
public class Log
{
	/**
	 * <p>An instance of an output stream that is directed to standard
	 * system output ({@link System#out}) by default. By changing this
	 * instance, the output of the entire logging can be redirected to
	 * where we need it.</p>
	 */
	public static java.io.PrintStream out = System.out;

	/**
	 * <p>Turns the logging on or off. This value must be true to make
	 * most of the methods in this class active. The default value is
	 * {@code false}.</p>
	 */
	public static boolean logOn = false;

	/**
	 * <p>User-defined level of logging. It is used to indent the logging
	 * output. The programmer can change this value at will or use the {@link 
	 * #logIn(Object... args)}, {@link #logOut(Object... args)}
	 * methods.</p>
	 * 
	 * @see #printLevel()
	 */
	public static int level = 0;

	/**
	 * <p>The indentation prefix used by the {@link #printLevel()} method.</p>
	 */
	public static String indentPrefix = null;

	/**
	 * <p>The indentation string used by the {@link #printLevel()} method.</p>
	 */
	public static String indent = " ¦";

	/**
	 * <p>The indentation postfix used by the {@link #printLevel()} method.</p>
	 */
	public static String indentPostfix = " ";

	/**
	 * <p>Formats the {@link StackTraceElement} as desired. There is a default
	 * function you can replace with your own.</p>
	 */
	public static Function<StackTraceElement, String>
		formatTraceElement = (ste) ->
		{
			StringBuffer sb = new StringBuffer();

			sb.append(ste.getClassName());
			sb.append('.');
			sb.append(ste.getMethodName());
			if (ste.isNativeMethod()) sb.append(", native");
			sb.append(" (");
			sb.append(ste.getFileName());
			sb.append(':');
			sb.append(ste.getLineNumber());
			sb.append(") ");

			return sb.toString();
		};

	/**
	 * <p>If this field is set, it is used to prefix the arguments in the
	 * logging dump. See the {@link #printLog(Object... args)} and
	 * {@link #printlnLog(Object... args)} methods.</p>
	 */
	public static String prefix = null;

	/**
	 * <p>If this field is set, it is used to separate the arguments in the
	 * logging dump (for convenience). See the {@link #printLog(Object...
	 * args)} and {@link #printlnLog(Object... args)} methods.</p>
	 */
	public static String separator = null;

	/**
	 * <p>If this field is set, it is used to postfix the arguments in the
	 * logging dump. See the {@link #printLog(Object... args)} and
	 * {@link #printlnLog(Object... args)} methods.</p>
	 */
	public static String postfix = null;

	// Private constructor. Creating instances is irrelevant.
	private Log() {}

	/**
	 * <p>Prints ranges of trace elements.</p>
	 * 
	 * @param t throwable instance (presumably generated by the calling method)
	 * @param es pairs of integers specifying the ranges
	 */
	public static void printTraceRanges(Throwable t, Integer... es)
	{
		if (!logOn) return;

		StackTraceElement[] stes = t.getStackTrace();
		Integer s = null;
		for (Integer e : es)
			if (null != e)
			{
				if (e < 0) e = stes.length + e;
				if (null == s) s = e; else
				if (s >= 0 && s < stes.length &&
					e >= 0 && e < stes.length)
				{
					for (int i = s; i <= e; ++i)
						out.print(formatTraceElement.apply(stes[i]));
					s = null;
				}
			}
	}

	/**
	 * <p>Prints ranges of trace elements. This method generates the {@link 
	 * Throwable} internally, which shifts the indexes by one.</p>
	 * 
	 * @param es pairs of integers specifying the ranges
	 */
	public static void printTraceRanges(Integer... es)
	{ printTraceRanges(new Throwable(), es); }

	/**
	 * <p>Prints single trace elements.</p>
	 * 
	 * @param t throwable instance (presumably generated by the calling method)
	 * @param es list of integers specifying desired elements
	 */
	public static void printTraceElements(Throwable t, Integer... es)
	{
		if (!logOn) return;

		StackTraceElement[] stes = t.getStackTrace();
		for (Integer e : es)
			if (null != e)
			{
				if (e < 0) e = stes.length + e;
				if (e >= 0 && e < stes.length)
					out.print(formatTraceElement.apply(stes[e]));
			}
	}

	/**
	 * <p>Prints single trace elements. This method generates the {@link 
	 * Throwable} internally, which shifts the indexes by one.</p>
	 * 
	 * @param es list of integers specifying desired elements
	 */
	public static void printTraceElements(Integer... es)
	{ printTraceElements(new Throwable(), es); }


	/**
	 * <p>Gets string of single trace element.</p>
	 * 
	 * @param t throwable instance (presumably generated by the calling method)
	 * @param i index of desired element
	 */
	public static String getTraceElement(Throwable t, int i)
	{
		StackTraceElement[] stes = t.getStackTrace();
		String result = "";

		if (i < 0) i = stes.length + i;
		if (i >= 0 && i < stes.length)
			result = formatTraceElement.apply(stes[i]);

		return result;
	}

	/**
	 * <p>Gets string of single trace element. This method generates the
	 * {@link Throwable} internally, which shifts the indexes by one.</p>
	 * 
	 * @param i index of desired element
	 */
	public static String getTraceElement(int i)
	{ return getTraceElement(new Throwable(), i); }

	/**
	 * <p>Gets string of second trace element of internally generated {@link 
	 * Throwable} instance, which supposed to be the element of the calling
	 * method.</p>
	 */
	public static String getTraceElement()
	{ return getTraceElement(new Throwable(), 1); }


	/**
	 * <p>Performs the indentation according to the {@link #level} value.</p>
	 */
	public static void printLevel()
	{
		if (!logOn) return;

		if (null != indentPrefix) out.print(indentPrefix);
		if (null != indent) for (int i = 0; i <= level; ++i) out.print(indent);
		if (null != indentPostfix) out.print(indentPostfix);
	}

	/**
	 * <p>Universal output method used by several other methods to perform
	 * the logging output. It checks each element of the {@code args} for
	 * the {@code null} value, safely converts the argument to {@link String},
	 * and outputs it. If the {@link #separator} is not {@code null}, it
	 * separates the output of arguments. If {@link #prefix} or {@link 
	 * #postfix} is not {@code null}, it used to prefix or postfix the
	 * output of arguments.</p>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void printLog(Object... args)
	{
		if (!logOn) return;

		if (null != prefix) out.print(prefix);

		boolean first = true;
		for (Object arg : args)
		{
			if (first) first = false;
			else if (null != separator) out.print(separator);

			if (null == arg) out.print("null");
			else out.print(arg.toString());
		}

		if (null != postfix) out.print(postfix);
	}

	/**
	 * <p>Like the {@link #printLog(Object... args)} method, but outputs
	 * the new line after the list of arguments.</p>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void printlnLog(Object... args)
	{
		if (!logOn) return;

		printLog(args);
		out.println();
	}

	/**
	 * <p>Prints the same information as the {@link #logTrace(Object...
	 * args)} method and increases the {@link #level} by one.</p>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void logIn(Object... args)
	{
		if (!logOn) return;

		// Warning! Do not call logTrace here. The index of trace element
		// would be invalid! // logTrace(args);
		printLevel();
		printTraceElements(new Throwable(), 1);
		printlnLog(args);
		++level;
	}

	/**
	 * <p>Prints the optional list of arguments (using the {@link 
	 * #logInfo(Object... args)} method) and decreases the {@link #level}
	 * value.</p>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void logOut(Object... args)
	{
		if (!logOn) return;

		if (0 != args.length) logInfo(args);
		--level;
	}

	/**
	 * <p>Prints the following:</p>
	 * 
	 * <ul>
	 * 	<li>indentation according to the {@link #level} value (see {@link 
	 * 	#printLevel()}),</li>
	 * 	<li>the trace element of the calling method,</li>
	 * 	<li>and the optional list of arguments.</li>
	 * </ul>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void logTrace(Object... args)
	{
		if (!logOn) return;

		printLevel();
		printTraceElements(new Throwable(), 1);
		printlnLog(args);
	}

	/**
	 * <p>Prints the indentation according to the {@link #level} value (using
	 * the {@link #printLevel()} method) and the optional list of arguments
	 * afterwards.</p>
	 * 
	 * @param args a list of arbitrary arguments
	 */
	public static void logInfo(Object... args)
	{
		if (!logOn) return;

		printLevel();
		printlnLog(args);
	}
}
