
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to trieda, ktorú implementoval autor
// programovacieho rámca podľa predchádzajúcich projektov, aby mohol
// v programovacom rámci jednoduchšie implementovať pomerne jednoduchú
// funkciu čítania údajov zo štandardného vstupu.
// 
// Ide o kombináciu metódy triedy Svet: aktivujŠtandardnýVstup()
// a reakcie obsluhy udalostí (a tiež samotnej triedy robota, ako pri
// väčšine udalostí) spracujRiadokVstupu(String riadokVstupu). Okrem
// nich môže pri (asynchrónnom) spracovaní štandardného vstupu asistovať
// aj metóda sveta štandardnýVstupAktívny() a reakcia (obsluhy udalostí
// alebo robota) koniecVstupu(). V prípade, že programátor potrebuje
// synchrónne spracovanie vstupu, môže použiť metódu sveta čakajNaVstup,
// ktorá implementuje dodatočný mechanizmus blokovania počas čakania na
// údaje zo vstupného vlákna. Táto abstraktná trieda je implementovaná
// v triede Svet ako súkromná trieda ŠtandardnýVstup.
// 
// Bez vlastného vlákna by táto funkcia spôsobila pozastavenie činnosti
// aplikácie s pravdepodobnosťou zatuhnutia v prípade neprijímania ďalších
// údajov zo vstupu.
// 
// Licencia je (stručne) referovaná nižšie (v anglickom jazyku).
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * <p>This abstract class helps the author of the framework to implement
 * the asynchronous data reading from the standard input (pipe). The true
 * implementation is spread in more than one class of the framework. Namely:
 * Svet (World), ObsluhaUdalostí (EventFactory), and GRobot itself.</p>
 */
abstract public class ReadStandardInput extends Thread implements Closeable
{
	// Stream that will be connected to standard input using specified
	// encoding.
	private final InputStreamReader inputReader;

	// Stream buffer that will be attached to the inputReader instance.
	private final BufferedReader standardInput;

	// The line read from the standardInput buffer.
	private String inputLine = "";

	// Flag specifying that this thread should continue to run.
	private boolean continueThread = true;


	/**
	 * <p>Constructor allowing to specify the encoding for the data
	 * received from the standard input. Constructor opens the data stream
	 * and runs this instance as an thread waiting for the input data and
	 * sending it to further processing through the method
	 * processInputLine(String inputLine) that must be defined by the
	 * offspring of this abstract class.</p>
	 */
	public ReadStandardInput(String encoding)
		throws UnsupportedEncodingException
	{
		Console konzola = System.console();
		// System.out.println("konzola: " + konzola);
		if (null != konzola)
		{
			inputReader = null;
			standardInput = new BufferedReader(konzola.reader());
		}
		else
		{
			inputReader = new InputStreamReader(System.in, encoding);
			standardInput = new BufferedReader(inputReader);
		}

		// Not a good idea:
		// start();
	}

	/**
	 * <p>Default constructors. Opens the standard input using UTF-8
	 * encoding. (See the ReadStandardInput(String encoding) constructor
	 * for more information.)</p>
	 */
	public ReadStandardInput() throws UnsupportedEncodingException
	{ this("UTF-8"); }


	/**
	 * <p>Method to be defined in the offspring class. It should process
	 * the lines received from standard input by this thread.</p>
	 * 
	 * @param inputLine the line received from the input stream
	 */
	abstract public void processInputLine(String inputLine);

	/**
	 * <p>Method to be defined in the offspring class. It should process
	 * the end of input stream case. This is possible only if the standard
	 * input is read from a finite source like a file.</p>
	 */
	abstract public void endOfInput();

	/**
	 * <p>Runs this thread waiting for and sending the data from standard
	 * input to further processing. This method should not be called
	 * manually. It is executed automatically (launching it through the
	 * process started by the thread’s start() method originally called in
	 * the constructor, now leaved to offspring class).</p>
	 */
	public void run()
	{
		try
		{
			// Blocking way:
			while (null != (inputLine = standardInput.readLine()))
			{
				if (!continueThread) break;
				processInputLine(inputLine);
				// See also: ExecuteShellCommand.writeInput(String)
			}
			endOfInput();

			// Unblocking way (not very useful – cannot detect endOfInput):
			/*breakAll:
			while (continueThread)
			{
				while (standardInput.ready())
				{
					if (null == (inputLine = standardInput.readLine()))
						break breakAll;
					processInputLine(inputLine);
					// See also: ExecuteShellCommand.writeInput(String)
				}
				try { Thread.sleep(350); }
				catch (InterruptedException ie) {} // …
			}*/

			try { standardInput.close(); }
			catch (Throwable t) { /*ignore*/ }
		}
		catch (IOException ioe)
		{
			System.err.println(
				"Error reading the input stream.\n" +
				ioe.getMessage());

			ioe.printStackTrace();
		}
	}

	/**
	 * <p>Enables this thread to be closed – to properly close its internal
	 * stream instance(s).</p>
	 */
	public void close()
	{
		continueThread = false;
	}
}
