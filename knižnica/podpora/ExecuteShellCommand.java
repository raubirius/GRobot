
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to trieda, ktorú implementoval autor
// programovacieho rámca podľa predchádzajúcich projektov, aby mohol
// implementovať v programovacom rámci relatívne komplexnú funkcionalitu
// príkazu triedy Svet: spustiProces(String príkaz, String... argumenty).
// Licencia a príklady použitia sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.InetAddress;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.SwingUtilities;

/**
 * <p>This abstract class is intended to implement custom command line.
 * It manages environment variables, current path, external processes
 * including standard input and output (that can be buffered and possibly
 * send from one process to another like some kind of discrete pipe). Only
 * one process can be handled by one instance at a time.</p>
 * 
 * <p>This class has been developed as a hidden part of the GRobot
 * framework, thus the framework’s class Svet contains the basic
 * implementation that may serve as an example (if analysed narrowly).</p>
 * 
 * @author Roman Horváth
 * @version 6. 10. 2018
 */
abstract public class ExecuteShellCommand
{
	// Language strings constants.
	protected final static int CANNOT_EXECUTE = 0;
	protected final static int CLEARED = 1;
	protected final static int COMMAND = 2;
	protected final static int COMMAND_MAPPING = 3;
	protected final static int COMMANDS_MAP = 4;
	protected final static int DELETED = 5;
	protected final static int DUPLICATE_REQUEST = 6;
	protected final static int ENCODING = 7;
	protected final static int ENCODING_FOR = 8;
	protected final static int ENCODING_CHANGED = 9;
	protected final static int ENCODING_KEEPED = 10;
	protected final static int ENCODING_RESTORED = 11;
	protected final static int ENCODINGS_MAP = 12;
	protected final static int ERROR_MESSAGE = 13;
	protected final static int ERROR_OCCURED = 14;
	protected final static int EXECUTE = 15;
	protected final static int EXIT_CODE = 16;
	protected final static int INPUT = 17;
	protected final static int IS_EMPTY = 18;
	protected final static int MAPPED_COMMAND_USE = 19;
	protected final static int MAPPING = 20;
	protected final static int MORE_PROCESSES = 21;
	protected final static int SET = 22;
	protected final static int STREAM_READ_ERROR = 23;
	protected final static int STREAM_WRITE_ERROR = 24;

	// Language strings.
	protected final static String[] langstring = new String[25]; static
	{
		langstring[CANNOT_EXECUTE] = "Cannot execute";
		langstring[CLEARED] = "cleared"; // mapping
		langstring[COMMAND] = "command"; // mapping
		langstring[COMMAND_MAPPING] = "Mapping of command"; // mapping
		langstring[COMMANDS_MAP] = "Commands map"; // mapping
		langstring[DELETED] = "deleted"; // mapping
		langstring[DUPLICATE_REQUEST] = "Duplicate request";
		langstring[ENCODING] = "Current encoding";
		langstring[ENCODING_FOR] = "Individual encoding for"; // mapping
		langstring[ENCODING_CHANGED] = "Encoding changed";
		langstring[ENCODING_KEEPED] = "Encoding keeped";
		langstring[ENCODING_RESTORED] = "Encoding restored";
		langstring[ENCODINGS_MAP] = "Encodings map"; // mapping
		langstring[ERROR_MESSAGE] = "Error message"; // cannot execute
		langstring[ERROR_OCCURED] = "Error occured"; // cannot execute
		langstring[EXECUTE] = "execute"; // duplicate request
		langstring[EXIT_CODE] = "Exit code";
		langstring[INPUT] = "input"; // duplicate request
		langstring[IS_EMPTY] = "is empty"; // mapping
		langstring[MAPPED_COMMAND_USE] = "Using mapped command";
		langstring[MAPPING] = "mapping"; // mapping
		langstring[MORE_PROCESSES] = "more than one process"; // cannot execute
		langstring[SET] = "set"; // mapping
		langstring[STREAM_READ_ERROR] = "Stream read error";
		langstring[STREAM_WRITE_ERROR] = "Stream write error";
	}

	// Global map of environment variables.
	private final static TreeMap<String, String> originalEnvironment =
		new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER); static
	{
		Map<String, String> envMap = System.getenv();
		if (null != envMap) originalEnvironment.putAll(envMap);
	}

	// Global variable storing the running path of current process (JVM).
	private final static String defaultPath; static
	{
		String getPath = ".";
		try { getPath = new File(".").getCanonicalPath(); }
		catch (Throwable t) { /*ignore*/ }
		defaultPath = getPath;
	}

	// Current path – if not null overrides the default path (which is the
	// current JVM running path).
	private String currentPath = null;

	// Global variable storing the current user name.
	private final static String userName; static
	{
		String getUserName = null;
		try { getUserName = System.getProperty("user.name"); }
		catch (Throwable t) { /*ignore*/ }
		userName = getUserName;
	}

	// Global variable storing the localhost machine name.
	private final static String localHostName; static
	{
		String getLocalHostName = null;
		try
		{
			InetAddress localHost = InetAddress.getLocalHost();
			getLocalHostName = localHost.getHostName();
		}
		catch (Throwable t) { /*ignore*/ }
		localHostName = getLocalHostName;
	}

	// Global refference to empty array of strings.
	private final static String[] emptyRefStringArray = new String[0];

	// Global constant for end of line character.
	private final static char EOL = '\n';


	// Global instance of ExpressionProcessor (used by the “setVariable”
	// method).
	private final static ExpressionProcessor expression =
		new ExpressionProcessor();


	// Date and time formats.
	// (TODO Consider: Make non-static, non-final, and allow to change?)
	private final static SimpleDateFormat dateFormat =
		new SimpleDateFormat("dd. MM. yyyy");
	private final static SimpleDateFormat timeFormat =
		new SimpleDateFormat("hh:mm:ss");


	// Local map of environment variables.
	private final TreeMap<String, String> internalEnvironment =
		new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	// Local list of executable files extensions.
	private final TreeSet<String> pathExt = new TreeSet<String>(
		String.CASE_INSENSITIVE_ORDER); { resetPathExt(); }

	// Resets the content of the “pathExt” set.
	private void resetPathExt()
	{
		pathExt.clear();
		String getPathExt = getVariable("PATHEXT");
		if (null != getPathExt)
		{
			String[] extSet = getPathExt.
				split(File.pathSeparator);

			for (String ext : extSet)
				if (null != ext && !ext.isEmpty())
					pathExt.add(ext);
		}
	}

	// Map of encodigns customized for selected commands.
	private final TreeMap<String, String> encodingsMap =
		new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	// Map of commands (substitutions).
	private final TreeMap<String, String> commandsMap =
		new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);


	// Prompt and title…
	private String defaultPrompt = "\n%CD%>"; // Prompt to be saved in config.
	private String promptString = "\n%CD%>";
	private String defaultTitle = null; // Title to be saved in config.
	private String titleString = null;


	// Flag signaling that this instance awaits an extra input.
	private boolean waitingForExtraInput = false;

	// Flag signaling that the next extra input should be evaluated as
	// expression.
	private boolean evaluateExtraInput = false;

	// Last extra input string (the read started by the “startExtraInput”
	// method and finished in the “writeInput” method.
	private String lastExtraInput = "";

	// Current error level. E.g. result code of build-in command, last exit
	// code of a finished process…
	private int errorLevel = 0;


	// Current encoding, default encoding and encoding backup.
	private String currentEncoding = "UTF-8";
	private String defaultEncoding = "UTF-8";
	private String restoreEncoding = null;


	// Stored last executed shell command line.
	private String runCommand = null;

	// Stored last executed shell command arguments.
	private String[] runArguments = null;

	// Stored last actual command array used to run the process.
	private String[] commandArray = null;

	// Stored the environment variables array used at a time of launching
	// the currently running process.
	private String[] runEnvironment = null;

	// A thread of currently running process.
	private Thread runningThread = null;

	// Currently running process.
	private Process runningProcess = null;

	// Input stream of currently running process.
	private BufferedWriter inputWriter = null;

	// Error output stream of currently running process.
	private StreamPrinter errorPrinter = null;

	// Output stream of currently running process.
	private StreamPrinter outputPrinter = null;


	/**
	 * <p>Splits entered command to array of strings. The result contains
	 * array of strings split by escape rules. See the “makeCommandArray”
	 * method.</p>
	 * 
	 * @param command command to be split
	 * @return split command
	 */
	public static String[] splitCommand(String command)
	{
		// See the “makeCommandArray” method’s body for more explanatory
		// comments.

		Vector<String> cmdArray = new Vector<String>();

		if (-1 == command.indexOf('"') &&
			-1 == command.indexOf('\\') &&
			-1 == command.indexOf(' ') &&
			-1 == command.indexOf('^'))
		{
			cmdArray.add(command);
		}
		else
		{
			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < command.length(); ++i)
			{
				char ch = command.charAt(i);

				if (' ' == ch)
				{
					cmdArray.add(builder.toString());
					builder.setLength(0);
				}
				else if ('\\' == ch && (i + 1) < command.length())
				{
					ch = command.charAt(++i);

					if ('"' == ch || ' ' == ch)
						builder.append(ch);
					else
					{
						builder.append('\\');
						builder.append(ch);
					}
				}
				else if ('^' == ch && (i + 1) < command.length())
				{
					ch = command.charAt(++i);

					if ('"' == ch || '^' == ch || '&' == ch ||
						'<' == ch || '>' == ch || '|' == ch)
						builder.append(ch);
					else
					{
						builder.append('^');
						builder.append(ch);
					}
				}
				else if ('"' == ch)
				{
					for (++i; i < command.length(); ++i)
					{
						ch = command.charAt(i);
						if ('"' == ch) break;

						if ((i + 1) < command.length())
						{
							if ('\\' == ch)
							{
								ch = command.charAt(++i);
								if ('"' == ch)
									builder.append(ch);
								else
								{
									builder.append('\\');
									builder.append(ch);
								}
							}
							else if ('^' == ch)
							{
								ch = command.charAt(++i);
								if ('"' == ch || '^' == ch || '&' == ch ||
									'<' == ch || '>' == ch || '|' == ch)
									builder.append(ch);
								else
								{
									builder.append('^');
									builder.append(ch);
								}
							}
							else
								builder.append(ch);
						}
						else
							builder.append(ch);
					}
				}
				else
					builder.append(ch);
			}

			if (0 != builder.length())
				cmdArray.add(builder.toString());
		}

		return cmdArray.toArray(emptyRefStringArray);
	}

	/**
	 * <p>This static method creates the command/arguments array of strings
	 * to be executed. Arguments in the “argStrings” parameter are processed
	 * as space-separated list, but the spaces in selected elements may be
	 * preserved using quotes or escape sequences. The first valid string is
	 * considered as the executable command and the others are its command
	 * line arguments.</p>
	 * 
	 * <p>The strings passed as arguments to this method are not joined
	 * together. Each string is processed as standalone
	 * (space-separated/quoted/escaped) list. Not only spaces, but also
	 * some other reserved characters may be preserved using two methods
	 * (mentioned already): quotes (e.g. "foo bar" foo "bar" means three
	 * arguments: foo bar, foo, bar) and escape sequences using the
	 * backslash (\) or the caret (^) character – see the “execute” method
	 * for the complete list of valid escape sequences.</p>
	 * 
	 * <p>If the “command” parameter is null, it is ignored and next split
	 * string in the “argStrings” list becomes the command. If some other
	 * parameter is null, the next parameter will be preserved without any
	 * processing (even if it’s null – two subsequent nulls will create one
	 * null string in the returned array).</p>
	 * 
	 * @param command the string that becomes the command (executable) or null
	 * @param argStrings the list of command line arguments; except, if the
	 *     first parameter is null, it includes the executable, too
	 * @return the array of string representing the command line array, usually
	 */
	public static String[] makeCommandArray(String command, String... argStrings)
	{
		Vector<String> cmdArray = new Vector<String>();
		StringBuilder builder = new StringBuilder();
		if (null != command) cmdArray.add(command);
		boolean nextIsFull = false;

		for (String argString : argStrings)
		{
			if (nextIsFull)
			{
				cmdArray.add(argString);
				nextIsFull = false;
			}
			else if (null == argString)
			{
				nextIsFull = true;
			}
			else if (-1 == argString.indexOf('"') &&
				-1 == argString.indexOf('\\') &&
				-1 == argString.indexOf(' ') &&
				-1 == argString.indexOf('^'))
			{
				cmdArray.add(argString);
			}
			else
			{
				builder.setLength(0);

				for (int i = 0; i < argString.length(); ++i)
				{
					char ch = argString.charAt(i);

					if (' ' == ch)
					{
						// Space will break the argument into two separate
						// elements of the final array.

						cmdArray.add(builder.toString());
						builder.setLength(0);
					}
					else if ('\\' == ch && (i + 1) < argString.length())
					{
						// Backslash within command line has special
						// behaviour. It backslashes only two characters:
						// the quotation mark (") and the space ( ).

						ch = argString.charAt(++i);

						if ('"' == ch || ' ' == ch)
							builder.append(ch);
						else
						{
							builder.append('\\');
							builder.append(ch);
						}
					}
					else if ('^' == ch && (i + 1) < argString.length())
					{
						// The caret acts like another escape character.
						// It escapes the characters listed below:

						ch = argString.charAt(++i);

						if ('"' == ch || '^' == ch || '&' == ch ||
							'<' == ch || '>' == ch || '|' == ch)
							builder.append(ch);
						else
						{
							// Invalid escape sequences are kept intouched.
							builder.append('^');
							builder.append(ch);
						}
					}
					else if ('"' == ch)
					{
						// Quotation mark joins everything until next
						// quotation mark unless it is an escaped one.

						for (++i; i < argString.length(); ++i)
						{
							ch = argString.charAt(i);
							if ('"' == ch) break;

							if ((i + 1) < argString.length())
							{
								if ('\\' == ch)
								{
									ch = argString.charAt(++i);
									if ('"' == ch)
										// (Only quotes can be escaped here.)
										builder.append(ch);
									else
									{
										builder.append('\\');
										builder.append(ch);
									}
								}
								else if ('^' == ch)
								{
									ch = argString.charAt(++i);
									if ('"' == ch || '^' == ch || '&' == ch ||
										'<' == ch || '>' == ch || '|' == ch)
										builder.append(ch);
									else
									{
										builder.append('^');
										builder.append(ch);
									}
								}
								else
									builder.append(ch);
							}
							else
								builder.append(ch);
						}
					}
					else
						builder.append(ch);
				}

				if (0 != builder.length())
					cmdArray.add(builder.toString());
			}
		}

		return cmdArray.toArray(emptyRefStringArray);
	}

	/**
	 * <p>Attempts to match the command with at least one of the entered
	 * matches. The comparison is case insensitive. If at least one
	 * match is positive, the return value is array containing three elements.
	 * The first element is the code of match type (see below), the second
	 * element is the index of matching argument, and the third element
	 * is the length of the match (pointing to the start of the command’s
	 * arguments and thus further called as the arguments’ pointer). In case
	 * of no match the null is returned.</p>
	 * 
	 * <p>Only non-empty non-null arguments are considered. The match may
	 * be one of this three types: 1. the whole command match (in that case
	 * the arguments’ pointer will be equal to the length of the command),
	 * 2. the match with the command separated from the arguments by at
	 * least one space (in that case the first space will not be included
	 * in the arguments’ pointer), or 3. the match with the command not
	 * separated from the arguments at all (in that case the arguments’
	 * pointer will be equal to the lenght of the matching string).</p>
	 * 
	 * @param command the command (possibly with arguments) to match
	 * @param matches list of strings to be matched with
	 * @return index of arguments’ start or −1
	 */
	public static int[] matchCommand(String command, String... matches)
	{
		if (null == command || command.isEmpty()) return null;
		command = command.toLowerCase();

		int i = -1;
		for (String match : matches)
		{
			++i;
			if (null == match || match.isEmpty()) continue;
			match = match.toLowerCase();

			if (command.equals(match))
				return new int[] {1, i, match.length()};

			if (command.startsWith(match + " "))
				return new int[] {2, i, 1 + match.length()};

			if (command.startsWith(match))
				return new int[] {3, i, match.length()};
		}

		return null;
	}

	/**
	 * <p>Encloses the string in quotation marks if it contains space or
	 * quotation mark. The quotation marks inside are escaped by
	 * backslashes.</p>
	 * 
	 * @param string string to be quoted or kept
	 * @return original string or quoted string
	 */
	public static String quoteSpacedString(String string)
	{
		if (-1 == string.indexOf(' ') && 
			-1 == string.indexOf('"')) return string;

		return "\"" + string.replace("\"", "\\\"") + "\"";
	}

	/**
	 * <p>Escapes new lines (and backslashes) by the backslash characters.
	 * Used on expanding the %PROMPT% virtual variable’s value.</p>
	 * 
	 * @param value string to be escaped
	 * @return escaped string
	 */
	public static String escapeNewLines(String value)
	{
		if (-1 != value.indexOf('\n') || -1 != value.indexOf('\r') ||
			-1 != value.indexOf('\\'))
		{
			StringBuilder escaped = new StringBuilder(value);

			int indexOf = 0;
			while (-1 != (indexOf = escaped.indexOf("\\", indexOf)))
			{
				escaped.replace(indexOf, indexOf + 1, "\\\\");
				indexOf += 2;
			}

			indexOf = 0;
			while (-1 != (indexOf = escaped.indexOf("\n", indexOf)))
				escaped.replace(indexOf, indexOf + 1, "\\n");

			indexOf = 0;
			while (-1 != (indexOf = escaped.indexOf("\r", indexOf)))
				escaped.replace(indexOf, indexOf + 1, "\\r");

			value = escaped.toString();
		}

		return value;
	}

	/**
	 * <p>Returns the stack trace of specified throwable object as
	 * string.</p>
	 * 
	 * @param t any throwable instance
	 * @return string containing stack trace of t
	 */
	public static String stackTraceToString(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}


	/**
	 * <p>Sends strings and the new line character (EOL) to the debugging
	 * stream.</p>
	 * 
	 * @param strings strings to send
	 */
	public void debugLine(Object... strings)
	{
		debug(strings);
		debug(EOL);
	}

	/**
	 * <p>Logs strings and puts new line character (EOL) to the end.</p>
	 * 
	 * @param strings strings to be logged
	 */
	public void logLine(Object... strings)
	{
		log(strings);
		log(EOL);
	}

	/**
	 * <p>Logs error strings and puts new line character (EOL) to the
	 * end.</p>
	 * 
	 * @param strings strings to be put to current error log
	 */
	public void errLine(Object... strings)
	{
		err(strings);
		err(EOL);
	}

	/**
	 * <p>Writes out error strings to standard error and puts new line
	 * character (EOL) after them.</p>
	 * 
	 * @param strings strings to be put to current error stream
	 */
	public void errorLine(Object... strings)
	{
		error(strings);
		error(EOL);
	}

	/**
	 * <p>Writes out the strings to standard output and puts new line
	 * character (EOL) after them.</p>
	 * 
	 * @param strings strings to be put to current output stream
	 */
	public void outputLine(Object... strings)
	{
		output(strings);
		output(EOL);
	}


	/**
	 * <p>Should take such action that corresponds to the application’s
	 * main window title change.</p>
	 * 
	 * @param title the new window title
	 */
	abstract public void title(String title);

	/**
	 * <p>Should return the current version of the implementation.</p>
	 */
	abstract public String getVersion();

	/**
	 * <p>Starts extra input announced by specified prompt. The method may
	 * return the new value for the %READ% variable or returns null if the
	 * value should be processed (received) later by manual invoke of the
	 * “writeInput” method. This input may be triggered by the “setVariable”
	 * method (see its description for more information).</p>
	 * 
	 * @param prompt the prompt for the extra input
	 * @return the new value or null
	 */
	abstract public String startExtraInput(String prompt);

	/**
	 * <p>Finishes the extra input allowing to modify the entered value or
	 * cancel the process. This method receives the value to be stored in
	 * the %READ% variable. This is the last chance to modify or cancel the
	 * input. If the string should be accepted, the modified or original
	 * string must be returned. If this method returns the null value, the
	 * value of the %READ% variable stays untouched.</p>
	 * 
	 * @param value the new value to be stored in the %READ% variable
	 * @return the original or modified value; null means “cancel”
	 */
	abstract public String finishExtraInput(String value);

	/**
	 * <p>Should clear (or reset) the debugging stream. Triggered on each
	 * invoke of method that uses the stream.</p>
	 * 
	 * @param method method that triggered this event
	 */
	abstract public void clearDebug(String method);

	/**
	 * <p>Should clear (or reset) the log. Triggered on each invoke of method
	 * that uses the log.</p>
	 * 
	 * @param method method that triggered this event
	 */
	abstract public void clearLog(String method);

	/**
	 * <p>Should clear (or reset) the error log. Triggered on each invoke of
	 * method that uses the error log.</p>
	 * 
	 * @param method method that triggered this event
	 */
	abstract public void clearErr(String method);

	/**
	 * <p>Should clear the error buffer (or put ASCII code 12 to the buffer).
	 * Triggered by the last ASCII code 12 within one line sent to the error
	 * stream.</p>
	 */
	abstract public void clearError();

	/**
	 * <p>Should clear the output buffer (or put ASCII code 12 to the
	 * buffer). Triggered by the last ASCII code 12 within one line sent to
	 * the output stream.</p>
	 */
	abstract public void clearOutput();

	/**
	 * <p>Should redirect the strings to the debugging stream. (This
	 * implementation does not produce lots of debugging messages. There
	 * are only few connected to the “execute” method.)</p>
	 * 
	 * @param strings strings to send
	 */
	abstract public void debug(Object... strings);

	/**
	 * <p>Should put strings to the log. These are the messages
	 * produced by this class internally.</p>
	 * 
	 * @param strings strings to be logged
	 */
	abstract public void log(Object... strings);

	/**
	 * <p>Should put strings to the error log. These are the messages
	 * produced by this class internally.</p>
	 * 
	 * @param strings strings to be put to current error log
	 */
	abstract public void err(Object... strings);

	/**
	 * <p>Should put strings to the output error buffer catching from
	 * the current process.</p>
	 * 
	 * @param strings strings to be put to current error stream
	 */
	abstract public void error(Object... strings);

	/**
	 * <p>Should put strings to the standard output buffer catching from
	 * the current process.</p>
	 * 
	 * @param strings strings to be put to current output stream
	 */
	abstract public void output(Object... strings);

	/**
	 * <p>Invoked after the last executed process terminates its
	 * activity. Receives its exit code.</p>
	 * 
	 * @param exitCode exit code of the process that finished recently
	 */
	abstract public void processEnded(int exitCode);


	/**
	 * <p>The thread class for handling the standard output and standard error
	 * streams of currently active process.</p>
	 */
	public class StreamPrinter extends Thread implements Closeable
	{
		private final InputStream is;
		private final boolean errorFlag;
		private boolean forceBreak = false;

		// Reads everything from “is” until empty.
		StreamPrinter(InputStream is, boolean errorFlag)
		{
			this.is = is;
			this.errorFlag = errorFlag;
		}

		public void run()
		{
			try
			{
				InputStreamReader isr =
					new InputStreamReader(
						is, currentEncoding);
				BufferedReader br =
					new BufferedReader(isr);

				String line;
				while (null != (line = br.readLine()))
				{
					if (forceBreak) break;

					// Handle the clear screen…
					if (-1 != line.indexOf((char)12))
					{
						int lastIndexOf = line.lastIndexOf((char)12);
						String linePart = line.substring(0, lastIndexOf);
						line = line.substring(1 + lastIndexOf);

						if (errorFlag)
						{
							error(linePart);
							clearError();
							if (!line.isEmpty())
								errorLine(line);
						}
						else
						{
							output(linePart);
							clearOutput();
							if (!line.isEmpty())
								outputLine(line);
						}
					}
					else
					{
						if (errorFlag)
							errorLine(line);
						else
							outputLine(line);
					}
				}
			}
			catch (IOException ioe)
			{
				errLine(langstring[STREAM_READ_ERROR], '.',
					EOL, ioe.getMessage());
				debugLine(stackTraceToString(ioe));
			}
		}

		public void close()
		{
			forceBreak = true;
		}
	}


	/**
	 * <p>Checks if there is some process active. Returns true if there is
	 * a process currently active (running).</p>
	 * 
	 * @return true if a process is currently running
	 */
	public boolean isProcessRunning()
	{ return null != runningThread; }

	/**
	 * <p>If there is a process running, this method returns the string
	 * that was passed to the “execute” method that launched the process
	 * as its “command” parameter. This may be null even if the process
	 * is running. In that case the method took for the command the first
	 * element of the “arguments” array – see the “getRunArguments”
	 * method.</p>
	 * 
	 * @return null or the string passed to the “execute” method when it
	 *     launched the current process
	 */
	public String getRunCommand()
	{ return runCommand; }

	/**
	 * <p>If there is a process running, this method returns the array
	 * of strings that was passed to the “execute” method that launched
	 * the process as its “arguments” parameter. This may be null even if
	 * the process is running. In that case the method took for the command
	 * the first element of the “arguments” array – see the “getRunArguments”
	 * method.</p>
	 * 
	 * @return null or the array passed to the “execute” method when it
	 *     launched the current process
	 */
	public String[] getRunArguments()
	{ return runArguments; }

	/**
	 * <p>If there is a process running, this method returns the actual
	 * command array used to run the process. The first element of the
	 * array is the executable and the other elements are the arguments.
	 * If there is no process running, the method returns null.</p>
	 * 
	 * @return the actual command array used to create current process
	 */
	public String[] getCommandArray()
	{ return commandArray; }

	/**
	 * <p>If there is a process running, this method returns the
	 * environment variables array used at a time of launching the
	 * currently running process. If there is no process running, the
	 * method returns null.</p>
	 * 
	 * @return the array of environment variables used at a time of
	 *     launching the current process
	 */
	public String[] getRunEnvironment()
	{ return runEnvironment; }


	/**
	 * <p>Gets instance of the currently running process or null. The
	 * returned instance may be used e.g. to kill the process.</p>
	 * 
	 * @return the instance of currently running process
	 */
	public Process getRunningProcess()
	{ return runningProcess; }


	/**
	 * <p>Expands the string searching for local, global, and virtual
	 * environment variables. The few virtual variables are (to be more
	 * schematic their names are enclosed in % already):</p>
	 * 
	 * <ul>
	 * <li>%ERRORLEVEL% – return code of last executed process/command that
	 *     has finished already (the exit code of last finished process or
	 *     internal command in some cases),</li>
	 * <li>%CD% – the current working path for new processes that may match
	 *     the current working path of the JVM – see the “changePath”
	 *     method,</li>
	 * <li>%RANDOM% – randomly generated number from 0 to 32767,</li>
	 * <li>%READ% – contains the last extra input string (the read that
	 *     starts by invoking of overridden method “startExtraInput” and
	 *     finishes either immediately – if the return value of the
	 *     “startExtraInput” is non-null – or after manual invoke the
	 *     “writeInput” method, which is invoked automatically when the
	 *     input ends immediately); this may also be the result of an
	 *     evaluated expression (or the error string; see the “setVariable”
	 *     method); notice: the whole process of putting any result to this
	 *     variable may be finalised (and/or confirmed) by the
	 *     “finishExtraInput” method – see the descriptions of the mentioned
	 *     methods,</li>
	 * <li>%DATE% – current date,</li>
	 * <li>%TIME% – current time,</li>
	 * <li>%USER% – current user (name),</li>
	 * <li>%MACHINE% – current localhost (name),</li>
	 * <li>%VERSION% – current version get by overridden method
	 *     “getVersion”,</li>
	 * <li>%PROMPT% – current prompt definition in the parsed form (as
	 *     near as it can be to state how the prompt should be displayed;
	 *     in the first phase the metaform of the prompt is returned: the
	 *     new lines are escaped, prompt key strings like $A, $B, $D, $P,
	 *     etc. are represented either by variables or the target characters
	 *     like &, |, %DATE%, %CD%, etc.; in the next phase (if allowed by
	 *     expanding depth restrictions) the nested variables are also
	 *     expanded).</li>
	 * </ul>
	 * 
	 * @param string the string that may contain variable names (enclosed
	 *     in % signs) that should be expanded
	 * @return string with variables expanded
	 */
	public String expandVariables(String string)
	{
		boolean restart = true;

		for (int x = 0; restart && x < 255; ++x)
		{
			int length = string.length(), start = -1;
			char mode = 0; restart = false;

			for (int i = 0; i < length; ++i)
			{
				char charAt = string.charAt(i);
				if (0 == mode)
				{
					if ('%' == charAt)
					{
						start = i + 1;
						mode = 1;
					}
				}
				else if (1 == mode)
				{
					if ('%' == charAt)
					{
						if (start == i) mode = 0; else
						{
							// boolean found = false;
							String variableName = string.substring(start, i);
							String variableValue = getVariable(variableName);

							// The “found” variable had sense until the
							// code using it has been moved to getVariable
							// method…

							if (/*found && */null != variableValue)
							{
								if (variableValue.isEmpty())
									string = string.substring(0, start - 1) +
										string.substring(i + 1);
								else
									string = string.substring(0, start - 1) +
										variableValue + string.substring(i + 1);

								restart = true;
								break;
							}
						}
					}
				}
			}

			if (-1 != string.indexOf("%%"))
				string = string.replace("%%", "%");
		}

		return string;
	}


	/**
	 * <p>Gets the default prompt string. This is the prompt that should be
	 * set at the startup for your custom console application. See also the
	 * “setPrompt” method.</p>
	 * 
	 * <p>It is recomended to store this string in the configuration
	 * file and not store the current prompt.</p>
	 * 
	 * @return the default prompt definition string
	 */
	public String getDefaultPrompt()
	{
		if (null == defaultPrompt || defaultPrompt.isEmpty()) return "$";

		StringBuilder pie = new StringBuilder(defaultPrompt);
		StringBuilder echoPrompt = new StringBuilder();

		while (pie.length() != 0)
		{
			char charAt = pie.charAt(0);
			pie.deleteCharAt(0);

			switch (charAt)
			{
			case '%':
				if (pie.length() != 0)
				{
					int indexOf = pie.indexOf("%");
					if (-1 == indexOf) echoPrompt.append('%'); else
					{
						String name = pie.substring(
							0, indexOf).toUpperCase();
						if (name.equals("CD"))
							echoPrompt.append("$P");
						else if (name.equals("USER"))
							echoPrompt.append("$U");
						else if (name.equals("MACHINE"))
							echoPrompt.append("$M");
						else if (name.equals("DATE"))
							echoPrompt.append("$D");
						else if (name.equals("TIME"))
							echoPrompt.append("$T");
						else if (name.equals("VERSION"))
							echoPrompt.append("$V");
						else
						{
							echoPrompt.append('%');
							echoPrompt.append(pie, 0, indexOf + 1);
						}
						pie.delete(0, indexOf + 1);
					}
				}
				else echoPrompt.append('%');
				break;

			case '\n': echoPrompt.append("$_"); break;
			case '$': echoPrompt.append("$$"); break;
			case 27: echoPrompt.append("$E"); break;
			case '&': echoPrompt.append("$A"); break;
			case '|': echoPrompt.append("$B"); break;
			case '(': echoPrompt.append("$C"); break;
			case ')': echoPrompt.append("$F"); break;
			case '>': echoPrompt.append("$G"); break;
			case '<': echoPrompt.append("$L"); break;
			case '=': echoPrompt.append("$Q"); break;
			case ' ': echoPrompt.append("$S"); break;

			default: echoPrompt.append(charAt);
			}
		}

		return echoPrompt.toString();
	}

	/**
	 * <p>Sets the new default prompt string. See also the “setPrompt”
	 * method.</p>
	 * 
	 * @param newDefaultPrompt the new default prompt string
	 * @return true if the setup has changed
	 */
	public boolean setDefaultPrompt(String newDefaultPrompt)
	{
		if (null != newDefaultPrompt && !newDefaultPrompt.isEmpty())
		{
			StringBuilder buildPrompt = new StringBuilder();
			char mode = 0; int length = newDefaultPrompt.length();

			for (int i = 0; i < length; ++i)
			{
				char charAt = newDefaultPrompt.charAt(i);
				if ('$' == charAt)
				{
					if (++i < length)
					{
						charAt = newDefaultPrompt.charAt(i);
						switch (charAt)
						{
						case '_': buildPrompt.append('\n'); break;
						case '$': buildPrompt.append('$'); break;
						case 'D': case 'd':
							buildPrompt.append("%DATE%"); break;
						case 'P': case 'p':
							buildPrompt.append("%CD%"); break;
						case 'T': case 't':
							buildPrompt.append("%TIME%"); break;
						case 'U': case 'u':
							buildPrompt.append("%USER%"); break;
						case 'M': case 'm':
							buildPrompt.append("%MACHINE%"); break;
						case 'V': case 'v':
							buildPrompt.append("%VERSION%"); break;
						case 'E': case 'e':
							buildPrompt.append((char)27); break;
						case 'A': case 'a': buildPrompt.append('&'); break;
						case 'B': case 'b': buildPrompt.append('|'); break;
						case 'C': case 'c': buildPrompt.append('('); break;
						case 'F': case 'f': buildPrompt.append(')'); break;
						case 'G': case 'g': buildPrompt.append('>'); break;
						case 'L': case 'l': buildPrompt.append('<'); break;
						case 'Q': case 'q': buildPrompt.append('='); break;
						case 'S': case 's': buildPrompt.append(' '); break;
						}
					}
				}
				else buildPrompt.append(charAt);
			}

			defaultPrompt = buildPrompt.toString();
			return true;
		}

		return false;
	}

	/**
	 * <p>If the current prompt differs from the default prompt copies
	 * the current prompt to the default one.</p>
	 * 
	 * @return true when the setup changed (when the copy was performed)
	 */
	public boolean storeDefaultPrompt()
	{
		if (null == defaultPrompt || null == promptString) return false;

		if (!defaultPrompt.equals(promptString))
		{
			defaultPrompt = promptString;
			return true;
		}
		return false;
	}

	/**
	 * <p>If the default prompt differs from the current prompt copies
	 * the default prompt to the current one.</p>
	 * 
	 * @return true when the setup changed (when the copy was performed)
	 */
	public boolean restoreDefaultPrompt()
	{
		if (null == promptString || null == defaultPrompt) return false;

		if (!promptString.equals(defaultPrompt))
		{
			promptString = defaultPrompt;
			return true;
		}
		return false;
	}

	/**
	 * <p>Returns the current (expanded) prompt string. (Exapansion may
	 * include current time and date – see $T, $D prompt directives.)</p>
	 */
	public String expandPrompt()
	{ return expandVariables(promptString); }

	/**
	 * <p>Gets current prompt definition. See the “setPrompt” method.</p>
	 * 
	 * @return current prompt definition string
	 */
	public String getPrompt()
	{
		if (null == promptString || promptString.isEmpty()) return "$";

		StringBuilder pie = new StringBuilder(promptString);
		StringBuilder echoPrompt = new StringBuilder();

		while (pie.length() != 0)
		{
			char charAt = pie.charAt(0);
			pie.deleteCharAt(0);

			switch (charAt)
			{
			case '%':
				if (pie.length() != 0)
				{
					int indexOf = pie.indexOf("%");
					if (-1 == indexOf) echoPrompt.append('%'); else
					{
						String name = pie.substring(
							0, indexOf).toUpperCase();
						if (name.equals("CD"))
							echoPrompt.append("$P");
						else if (name.equals("USER"))
							echoPrompt.append("$U");
						else if (name.equals("MACHINE"))
							echoPrompt.append("$M");
						else if (name.equals("DATE"))
							echoPrompt.append("$D");
						else if (name.equals("TIME"))
							echoPrompt.append("$T");
						else if (name.equals("VERSION"))
							echoPrompt.append("$V");
						else
						{
							echoPrompt.append('%');
							echoPrompt.append(pie, 0, indexOf + 1);
						}
						pie.delete(0, indexOf + 1);
					}
				}
				else echoPrompt.append('%');
				break;

			case '\n': echoPrompt.append("$_"); break;
			case '$': echoPrompt.append("$$"); break;
			case 27: echoPrompt.append("$E"); break;
			case '&': echoPrompt.append("$A"); break;
			case '|': echoPrompt.append("$B"); break;
			case '(': echoPrompt.append("$C"); break;
			case ')': echoPrompt.append("$F"); break;
			case '>': echoPrompt.append("$G"); break;
			case '<': echoPrompt.append("$L"); break;
			case '=': echoPrompt.append("$Q"); break;
			case ' ': echoPrompt.append("$S"); break;

			default: echoPrompt.append(charAt);
			}
		}

		return echoPrompt.toString();
	}

	/**
	 * <p>Parses and sets the new prompt string. This method is intended as
	 * the implementer of the internal command: PROMPT. The definition can
	 * contain following key strings:</p>
	 * 
	 * <ul>
	 * <li><code>$A</code> – & (ampersand),</li>
	 * <li><code>$B</code> – | (pipe/bar),</li>
	 * <li><code>$C</code> – ( (left parenthesis),</li>
	 * <li><code>$D</code> – current date (in fact the content of the
	 *     (possibly virtual) variable %DATE%),</li>
	 * <li><code>$E</code> – escape code (ASCII code 27),</li>
	 * <li><code>$F</code> – ) (right parenthesis),</li>
	 * <li><code>$G</code> – > (greater-than sign),</li>
	 * <li><code>$L</code> – < (less-than sign),</li>
	 * <li><code>$M</code> – current machine (in fact the content of the
	 *     (possibly virtual) variable %MACHINE%),</li>
	 * <li><code>$P</code> – current path (on Windows also with drive;
	 *     in fact the content of the (possibly virtual) variable %CD%),</li>
	 * <li><code>$Q</code> – = (equal sign),</li>
	 * <li><code>$S</code> –   (space),</li>
	 * <li><code>$T</code> – current time (in fact the content of the
	 *     (possibly virtual) variable %TIME%),</li>
	 * <li><code>$U</code> – current user (in fact the content of the
	 *     (possibly virtual) variable %USER%),</li>
	 * <li><code>$V</code> – current version (in fact the content of the
	 *     (possibly virtual) variable %VERSION%),</li>
	 * <li><code>$_</code> – new line,</li>
	 * <li><code>$$</code> – $ (dollar sign).</li>
	 * </ul>
	 * 
	 * <p>The virtual variables are expanded in real-time. The %VERSION%,
	 * for example, calls the “getVersion” method. If the value of virtual
	 * variable is overridden (by local variable), the overridden value will
	 * show up.</p>
	 * 
	 * @param newPrompt the new prompt string definition
	 * @return true if the setup has changed
	 */
	public boolean setPrompt(String newPrompt)
	{
		if (null != newPrompt && !newPrompt.isEmpty())
		{
			StringBuilder buildPrompt = new StringBuilder();
			char mode = 0; int length = newPrompt.length();

			for (int i = 0; i < length; ++i)
			{
				char charAt = newPrompt.charAt(i);
				if ('$' == charAt)
				{
					if (++i < length)
					{
						charAt = newPrompt.charAt(i);
						switch (charAt)
						{
						case '_': buildPrompt.append('\n'); break;
						case '$': buildPrompt.append('$'); break;
						case 'D': case 'd':
							buildPrompt.append("%DATE%"); break;
						case 'P': case 'p':
							buildPrompt.append("%CD%"); break;
						case 'T': case 't':
							buildPrompt.append("%TIME%"); break;
						case 'U': case 'u':
							buildPrompt.append("%USER%"); break;
						case 'M': case 'm':
							buildPrompt.append("%MACHINE%"); break;
						case 'V': case 'v':
							buildPrompt.append("%VERSION%"); break;
						case 'E': case 'e':
							buildPrompt.append((char)27); break;
						case 'A': case 'a': buildPrompt.append('&'); break;
						case 'B': case 'b': buildPrompt.append('|'); break;
						case 'C': case 'c': buildPrompt.append('('); break;
						case 'F': case 'f': buildPrompt.append(')'); break;
						case 'G': case 'g': buildPrompt.append('>'); break;
						case 'L': case 'l': buildPrompt.append('<'); break;
						case 'Q': case 'q': buildPrompt.append('='); break;
						case 'S': case 's': buildPrompt.append(' '); break;
						}
					}
				}
				else buildPrompt.append(charAt);
			}

			/*
			TODO (?):
				$N   current drive (on Windows)
				? $H   backspace (erases previous character)
				$+
					zero or more plus sign (+) characters depending upon
					the depth of the PUSHD directory stack, one character
					for each level pushed.

				$M
					Displays the remote name associated with the current
					drive letter or the empty string if current drive is
					not a network drive.
			*/

			promptString = buildPrompt.toString();
			return true;
		}

		return false;
	}


	/**
	 * <p>Gets the default title string. Default title string is not that
	 * same as the null value title string. The “default” means that this
	 * is the initial string for the title that should be set at the
	 * startup for your custom console application.</p>
	 * 
	 * @return the default title string
	 */
	public String getDefaultTitle() { return defaultTitle; }

	/**
	 * <p>Sets the new default title string. (See also the “getDefaultTitle”,
	 * “setTitle”, and “setPrompt” methods.)</p>
	 * 
	 * <p>It is recomended to store this string in the configuration
	 * file and not store the current title.</p>
	 * 
	 * @param newDefaultTitle the new default title string
	 */
	public void setDefaultTitle(String newDefaultTitle)
	{
		if (null == newDefaultTitle || newDefaultTitle.isEmpty())
		{
			defaultTitle = null;
		}
		else
		{
			if (newDefaultTitle.length() >= 2 &&
				newDefaultTitle.startsWith("\"") &&
				newDefaultTitle.endsWith("\""))
			{
				// Note: This should be the way how to set empty title!
				/*if (newDefaultTitle.length() == 2)
					defaultTitle = null;
				else*/
					defaultTitle = newDefaultTitle.
						substring(1, newDefaultTitle.length() - 1);
			}
			else
				defaultTitle = newDefaultTitle;
		}
	}

	/**
	 * <p>Copies the current title to default one.</p>
	 */
	public void storeDefaultTitle() { defaultTitle = titleString; }

	/**
	 * <p>Copies the default title to current title.</p>
	 */
	public void restoreDefaultTitle()
	{
		titleString = defaultTitle;
		updateTitle();
	}

	/**
	 * <p>This method is triggered automatically. It invokes the
	 * “title(expandTitle())” chain that is useful if the title
	 * contains date/time references (using virtual variables –
	 * see the description of the “expandVariables” method).</p>
	 */
	public void updateTitle() { title(expandTitle()); }

	/**
	 * <p>Expands the current title string. If the current title is null,
	 * this method invokes the “getVersion” method. Otherwise it expands
	 * variables in current title string and returns it.</p>
	 * 
	 * @return expanded title string
	 */
	public String expandTitle()
	{
		return null == titleString ? getVersion() :
			expandVariables(titleString);
	}

	/**
	 * <p>Gets current title string.</p>
	 * 
	 * @return current title string
	 */
	public String getTitle() { return titleString; }

	/**
	 * <p>Sets new title string; this method is intended as the implementer
	 * of the internal command: TITLE. The argument may contain environment
	 * variables that will be expanded at runtime. The empty argument string
	 * has the same meaning as the null string. You may set empty title using
	 * empty quotes string: "", since the first and the last quote is deleted
	 * from the title, if they both are present. See also the description of
	 * the “expandVariables” method to see the full list of reserved
	 * (virtual) variable names (like %CD%, %READ%…).</p>
	 * 
	 * @param newTitle new title definition string
	 */
	public void setTitle(String newTitle)
	{
		if (null == newTitle || newTitle.isEmpty())
		{
			titleString = null;
		}
		else
		{
			if (newTitle.length() >= 2 &&
				newTitle.startsWith("\"") &&
				newTitle.endsWith("\""))
			{
				// Note: This should be the way how to set empty title!
				/*if (newTitle.length() == 2)
					titleString = null;
				else*/
					titleString = newTitle.
						substring(1, newTitle.length() - 1);
			}
			else
				titleString = newTitle;
		}

		updateTitle();
	}


	/**
	 * <p>Gets map of internal environment variables. (Those which
	 * override the real system settings.)</p>
	 * 
	 * @return internal environment
	 */
	public TreeMap<String, String> getInternalEnvironment()
	{ return internalEnvironment; }

	/**
	 * <p>Gets the value of entered environment variable name or null if
	 * it does not exist. There are several reserved names that get a value
	 * even if the variable is not defined, e.g. %CD%, %READ%… They are
	 * called virtual variables. See the description of the “expandVariables”
	 * method for the full list.</p>
	 * 
	 * @param variableName the name of the variable
	 * @return the variable value
	 */
	public String getVariable(String variableName)
	{
		Date currentDate = new Date();
		String variableValue = null;

		if (internalEnvironment.containsKey(variableName))
		{
			// found = true;
			variableValue = internalEnvironment.get(variableName);
		}
		else if (originalEnvironment.containsKey(variableName))
		{
			// found = true;
			variableValue = originalEnvironment.get(variableName);
		}
		else if (variableName.equalsIgnoreCase("ERRORLEVEL"))
		{
			// found = true;
			variableValue = "" + errorLevel;
		}
		else if (variableName.equalsIgnoreCase("CD"))
		{
			// found = true;
			variableValue = null == currentPath ?
				defaultPath : currentPath;
		}
		else if (variableName.equalsIgnoreCase("RANDOM"))
		{
			// found = true;
			variableValue = "" + (int)(32767 * Math.random());
		}
		else if (variableName.equalsIgnoreCase("READ"))
		{
			// found = true;
			variableValue = lastExtraInput;
		}
		else if (variableName.equalsIgnoreCase("DATE"))
		{
			// found = true;
			variableValue = dateFormat.format(currentDate);
		}
		else if (variableName.equalsIgnoreCase("TIME"))
		{
			// found = true;
			variableValue = timeFormat.format(currentDate);
		}
		else if (variableName.equalsIgnoreCase("USER"))
		{
			// found = true;
			variableValue = userName;
		}
		else if (variableName.equalsIgnoreCase("MACHINE"))
		{
			// found = true;
			variableValue = localHostName;
		}
		else if (variableName.equalsIgnoreCase("VERSION"))
		{
			// found = true;
			variableValue = getVersion();
		}
		else if (variableName.equalsIgnoreCase("PROMPT"))
		{
			// found = true;
			variableValue = escapeNewLines(promptString);
		}
		else if (variableName.equalsIgnoreCase("TITLE"))
		{
			// found = true;
			variableValue = null == titleString ?
				getVersion() : titleString;
		}
		// …
		return variableValue;
	}

	/**
	 * <p>This method can be used to set, erase, or list environment
	 * variables. This method is intended as implementer of the internal
	 * command: SET. The “arguments” string is parsed using following
	 * rules:</p>
	 * 
	 * <ul>
	 * <li>the environment variables in the string (e.g. %CD%, %PATH%…)
	 * will be expanded,</li>
	 * <li>first appearance of operator = or := will divide the string
	 * to variable «name» and «value»,</li>
	 * <li>the «value» will override the original value (if any) of
	 * the environment variable specified by the «name»; the := operator
	 * means that the «value» should be evaluated as the expression (see
	 * the ExpressionProcessor class defined in this package),</li>
	 * <li>empty «value» after = operator means deletion of the internal
	 * variable value – the cancellation of the above mentioned override;
	 * the original system value becomes effective again (if there was
	 * any),</li>
	 * <li>empty «value» after := operator means that the original system
	 * variable should be hidden completely (until this state is cancelled
	 * using empty string after the = operator),</li>
	 * <li>empty «name» triggers the extra input gaining process – see the
	 * description of the %READ% virtual variable in the description of the
	 * “expandVariables” method; the := operator means that the extra input
	 * received will be processed as the expression,</li>
	 * <li>empty list of arguments will list all variables to the log.</li>
	 * </ul>
	 * 
	 * @param arguments argument(s) following the rules above
	 * @return true if the setup has changed
	 */
	public boolean setVariable(String arguments)
	{
		boolean setupChanged = false;
		clearLog("setVariable");
		if (null == arguments)
			arguments = "";
		else
			arguments = expandVariables(arguments.trim());

		if (arguments.isEmpty())
		{
			// Copy original environment variables to new TreeMap…
			TreeMap<String, String> envLocalHash =
				new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
			envLocalHash.putAll(originalEnvironment);
			// … and mix them with internal environment variables.
			envLocalHash.putAll(internalEnvironment);

			// List all variables:
			for (String envName : envLocalHash.keySet())
			{
				String value = envLocalHash.get(envName);
				if (null != value && // !value.isEmpty() &&
					!envName.startsWith("="))
					logLine(envName, "=", value);
			}

			return setupChanged;
		}

		int indexOfEval = arguments.indexOf(":=");
		int indexOfAssign = arguments.indexOf("=");
		String name = null, value = null;
		boolean eval = false;

		if (-1 != indexOfEval && -1 != indexOfAssign)
		{
			if (indexOfEval < indexOfAssign)
			{
				eval = true;
				name = arguments.substring(0, indexOfEval).trim();
				value = arguments.substring(indexOfEval + 2).trim();
			}
			else
			{
				name = arguments.substring(0, indexOfAssign).trim();
				value = arguments.substring(indexOfAssign + 1).trim();
			}
		}
		else if (-1 != indexOfEval)
		{
			eval = true;
			name = arguments.substring(0, indexOfEval).trim();
			value = arguments.substring(indexOfEval + 2).trim();
		}
		else if (-1 != indexOfAssign)
		{
			name = arguments.substring(0, indexOfAssign).trim();
			value = arguments.substring(indexOfAssign + 1).trim();
		}

		if (null != value && value.length() >= 2 &&
			value.startsWith("\"") && value.endsWith("\""))
			value = value.substring(1, value.length() - 1);

		if (null == name && null == value)
		{
			arguments = arguments.toLowerCase();

			// Copy original environment variables to new TreeMap…
			TreeMap<String, String> envLocalHash =
				new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
			envLocalHash.putAll(originalEnvironment);
			// … and mix them with internal environment variables.
			envLocalHash.putAll(internalEnvironment);

			// List all variables:
			for (String envName : envLocalHash.keySet())
			{
				value = envLocalHash.get(envName);
				if (null != value && !value.isEmpty())
				{
					name = envName.toLowerCase();
					if (name.startsWith(arguments))
						logLine(envName, "=", value);
				}
			}
		}
		else if (null == name || name.isEmpty())
		{
			if (waitingForExtraInput)
				errLine(langstring[DUPLICATE_REQUEST], ": ",
					langstring[INPUT], '.');
			else
			{
				// Start to read new value…
				evaluateExtraInput = eval;
				waitingForExtraInput = true;
				String input = startExtraInput(value);
				if (null != input) writeInput(input);
			}
		}
		else if (null == value || value.isEmpty())
		{
			if (eval)
			{
				// Override external (original) variable value:
				internalEnvironment.put(name, null);
			}
			else
			{
				// Delete internal variable:
				internalEnvironment.remove(name);
			}

			setupChanged = true;
			errorLevel = 0;
		}
		else
		{
			// Set (internal) variable
			if (eval)
			{
				if (expression.attachString(value))
				{
					if (expression.parse())
					{
						ExpressionProcessor.Value
							result = expression.getValue();
						internalEnvironment.put(
							name, result.toString());
						errorLevel = result.getError().ordinal();
					}
					else
					{
						internalEnvironment.put(name,
							expression.toString());
						errorLevel = expression.getValue().
							getError().ordinal();
					}
				}
				else
				{
					internalEnvironment.put(
						name, "Cannot attach string.");
					errorLevel = -1;
				}
			}
			else
			{
				internalEnvironment.put(name, value);
				errorLevel = 0;
			}

			if (name.equalsIgnoreCase("PATHEXT")) resetPathExt();
			setupChanged = true;
		}

		return setupChanged;
	}


	/**
	 * <p>Gets the default encoding. Default encoding is the initial
	 * encoding that should be set at the startup for your custom console
	 * application.</p>
	 * 
	 * @return the default encoding
	 */
	public String getDefaultEncoding() { return defaultEncoding; }

	/**
	 * <p>Sets the new default encoding. (See also the “getDefaultEncoding”
	 * and “changeEncoding” methods.)</p>
	 * 
	 * <p>It is recomended to store this value in the configuration
	 * file (and not to store the current encoding).</p>
	 * 
	 * @param newDefaultEncoding the new default encoding
	 */
	public void setDefaultEncoding(String newDefaultEncoding)
	{
		if (null == newDefaultEncoding || newDefaultEncoding.isEmpty())
			defaultEncoding = "UTF-8";
		else
			defaultEncoding = newDefaultEncoding;
	}

	/**
	 * <p>Copies the current encoding to default one.</p>
	 */
	public void storeDefaultEncoding() { defaultEncoding = currentEncoding; }

	/**
	 * <p>Copies the default encoding to current encoding.</p>
	 */
	public void restoreDefaultEncoding() { currentEncoding = defaultEncoding; }

	/**
	 * <p>Gets the encoding currently in use for this instace.</p>
	 * 
	 * @return current encoding
	 */
	public String getEncoding() { return currentEncoding; }

	/**
	 * <p>Allow to change the current encoding or define custom encoding for
	 * specified command. Commands are recognised after cutting off the path
	 * and the extension specified by PATHEXT environment variable.</p>
	 * 
	 * <p>This method is intended as implementer of the internal command:
	 * CHCP. The command is able to manage list of individual code page
	 * mappings for selected commands. If the first argument (included in the
	 * “arguments” string; the string is considered as the list of arguments
	 * separated by spaces; quotation marks and escape sequences can be used
	 * to specify an argument containing a space) of this command is the
	 * keyword “for”, it changes the behaviour as follows:</p>
	 * 
	 * <ul>
	 * <li>If there is no other argument the existing mappings will be
	 * listed to the log.</li>
	 * <li>If it is one more argument the mapping for specified argument
	 * will be deleted.</li>
	 * <li>If there are two more (or even more) arguments a new mapping
	 * will be created.</li>
	 * </ul>
	 * 
	 * <p>If the first argument is anything others, it will be considered and
	 * set as the new encoding (the current console encoding). Zero arguments
	 * mean print the current encoding to the log.</p>
	 * 
	 * @param arguments list of arguments separated by spaces; quotation
	 *     marks and escape sequences can be used effectively
	 * @return true if the setup has changed
	 */
	public boolean changeEncoding(String arguments)
	{
		boolean setupChanged = false;
		clearLog("changeEncoding");
		arguments = arguments.trim();

		if (!arguments.isEmpty())
		{
			String[] parts = splitCommand(arguments);

			if (parts.length > 0 && parts[0].equalsIgnoreCase("for"))
			{
				if (parts.length > 2)
				{
					encodingsMap.put(parts[1]
						/*.toLowerCase() — the map is case insensitive*/,
						parts[2]);
					logLine(langstring[ENCODING_FOR], ' ', parts[1],
						' ', langstring[SET], ": ", parts[2]);
					setupChanged = true;
				}
				else if (parts.length > 1)
				{
					encodingsMap.remove(parts[1]
						/*.toLowerCase() — the map is case insensitive*/);
					logLine(langstring[ENCODING_FOR], ' ', parts[1],
						' ', langstring[CLEARED], '.');
				}
				else
				{
					if (0 == encodingsMap.size())
						logLine(langstring[ENCODINGS_MAP], ' ',
							langstring[IS_EMPTY], '.');
					else
					{
						logLine(langstring[ENCODINGS_MAP], ':');
						for (Map.Entry<String, String> entry :
							encodingsMap.entrySet())
						{
							logLine("  ", langstring[COMMAND],
								": ", entry.getKey());
							logLine("  ", langstring[MAPPING],
								": ", entry.getValue());
							logLine();
						}
					}
				}
			}
			else if (currentEncoding.equals(arguments))
				logLine(langstring[ENCODING_KEEPED], ": ", currentEncoding);
			else
			{
				currentEncoding = arguments;
				logLine(langstring[ENCODING_CHANGED], ": ", currentEncoding);
				setupChanged = true;
			}
		}
		else logLine(langstring[ENCODING], ": ", currentEncoding);

		return setupChanged;
	}

	/**
	 * <p>Gets current map of encodings. (See the “changeEncoding” method.)</p>
	 * 
	 * @return current map of encodings
	 */
	public TreeMap<String, String> getEncodingsMap()
	{ return encodingsMap; }


	/**
	 * <p>This method allows to define mappings for specified commands.
	 * It is intended as the implementer of internal command: MAPCMD.
	 * Commands are recognised after cutting off the path and the extension
	 * specified by PATHEXT environment variable. The “arguments” string
	 * should be in one of following shapes:</p>
	 * 
	 * <ul>
	 * <li>«command» «mapping» – the mapping for the command will be set.</li>
	 * <li>«command» – the mapping for the command will be deleted.</li>
	 * <li>«empty» – the list of mappings will be printed out (to the log).</li>
	 * </ul>
	 * 
	 * @param arguments list of arguments separated by spaces; quotation
	 *     marks and escape sequences can be used effectively
	 * @return true if the setup has changed
	 */
	public boolean mapCommand(String arguments)
	{
		boolean setupChanged = false;
		clearLog("mapCommand");
		String[] split = splitCommand(arguments);

		if (2 <= split.length && !split[0].isEmpty())
		{
			boolean first = true;
			String map = "";

			for (int i = 1; i < split.length; ++i)
			{
				if (!split[i].isEmpty())
				{
					if (first) first = false; else map += " ";
					map += quoteSpacedString(split[i]);
				}
			}

			commandsMap.put(split[0]
				/*.toLowerCase() — the map is case insensitive*/, map);
			logLine(langstring[COMMAND_MAPPING], ' ',
				split[0], ' ', langstring[SET], ": ", map);
			setupChanged = true;
		}
		else if (1 <= split.length && !split[0].isEmpty())
		{
			commandsMap.remove(split[0]
				/*.toLowerCase() — the map is case insensitive*/);
			logLine(langstring[COMMAND_MAPPING], ' ',
				split[0], ' ', langstring[DELETED], '.');
			setupChanged = true;
		}
		else
		{
			if (0 == commandsMap.size())
				logLine(langstring[COMMANDS_MAP], ' ',
					langstring[IS_EMPTY], '.');
			else
			{
				logLine(langstring[COMMANDS_MAP], ' ');
				for (Map.Entry<String, String> entry : commandsMap.entrySet())
				{
					logLine("    ", langstring[COMMAND],
						": ", entry.getKey());
					logLine("    ", langstring[MAPPING],
						": ", entry.getValue());
					logLine();
				}
			}
		}

		return setupChanged;
	}


	/**
	 * <p>Gets current map of commands. (See the “mapCommand” method.)</p>
	 * 
	 * @return current map of commands
	 */
	public TreeMap<String, String> getCommandsMap()
	{ return commandsMap; }


	/**
	 * <p>Checks if this instance awaits some input. This may be extra input
	 * stored in the %READ% virtual variable or input for the currently
	 * running process. (If a process is active, the input should be active,
	 * too. Unless some error occurred. The extra input is activated after
	 * executing the “SET =” or “SET :=” command – see the “setVariable”
	 * method.)</p>
	 * 
	 * @return true if the instance awaits an input string
	 */
	public boolean isInputActive()
	{ return waitingForExtraInput || null != inputWriter; }

	/**
	 * <p>Handles the input string. It even stores the entered string or
	 * evalutates and stores the result in the %READ% (virtual) variable,
	 * or sends the entered string to the standard input of the currently
	 * running process. If the %READ% variable was set, this method sets the
	 * errorLevel, too.</p>
	 * 
	 * @param input input string
	 */
	public void writeInput(String input)
	{
		if (null != input)
		{
			if (waitingForExtraInput)
			{
				if (null == (input = finishExtraInput(input)))
				{
					// Do nothing…
				}
				else if (evaluateExtraInput)
				{
					if (expression.attachString(
						expandVariables(input)))
					{
						if (expression.parse())
						{
							ExpressionProcessor.Value result =
								expression.getValue();
							lastExtraInput = result.toString();
							errorLevel = result.getError().ordinal();
						}
						else
						{
							lastExtraInput = expression.toString();
							errorLevel = expression.getValue().
								getError().ordinal();
						}
					}
					else
					{
						lastExtraInput = "Cannot attach string.";
						errorLevel = -1;
					}
				}
				else
				{
					lastExtraInput = input;
					errorLevel = 0;
				}
				waitingForExtraInput = false;
			}
			else if (null != inputWriter)
			{
				// String input = input(input);
				try
				{
					if (!input.isEmpty())
						inputWriter.write(input);

					inputWriter.write('\n');
					inputWriter.flush();
				}
				catch (IOException ioe)
				{
					errLine(langstring[STREAM_WRITE_ERROR], '.',
						EOL, ioe.getMessage());
					debugLine(stackTraceToString(ioe));
				}
			}
		}
	}


	// Runs new process and keeps waiting for its exit code. (Executing of
	// this method must be unique within a time period – only one process
	// should be running for this instance at a time. This should be ensured
	// by the synchronized “execute” method.)
	// 
	// @return a return value of the process
	private int runProcess()
	{
		if (null != runningProcess)
		{
			errLine(langstring[DUPLICATE_REQUEST],
				": ", langstring[EXECUTE], '.');
			processEnded(-1); return -1;
		}

		int exitCode = 0;

		try
		{
			// Make command array (splits command from arguments):
			commandArray = makeCommandArray(runCommand, runArguments);


			// Expand variables in commandArray.
			for (int i = 0; i < commandArray.length; ++i)
				commandArray[i] = expandVariables(commandArray[i]);


			if (commandArray.length > 0)
			{
				// Remove path from the command to match the maps’ keys…
				String mapKey = commandArray[0]
					/*.toLowerCase() — the map is case insensitive*/;
				int indexOf1 = mapKey.lastIndexOf('/');
				int indexOf2 = mapKey.lastIndexOf('\\');
				if (-1 != indexOf1 || -1 != indexOf2)
					mapKey = mapKey.substring(1 + Math.max(
						indexOf1, indexOf2));

				// Remove executable extension from the command…
				indexOf1 = mapKey.lastIndexOf('.');
				if (-1 != indexOf1)
				{
					String mapExt = mapKey.substring(indexOf1);
					if (pathExt.contains(mapExt))
						mapKey = mapKey.substring(0, indexOf1);
				}

				// Use commands’ encodings map:
				String mapEntry = encodingsMap.get(mapKey);
				if (null != mapEntry && !mapEntry.isEmpty())
				{
					restoreEncoding = currentEncoding;
					currentEncoding = mapEntry;
					debugLine(langstring[ENCODING_CHANGED],
						": ", currentEncoding);
				}
				else debugLine(langstring[ENCODING],
					": ", currentEncoding);

				// Map command:
				mapEntry = commandsMap.get(mapKey);
				if (null != mapEntry) if (mapEntry.isEmpty()) return 0; else
				{
					debugLine(langstring[MAPPED_COMMAND_USE],
						": ", mapEntry);

					if (-1 == mapEntry.indexOf('%'))
						commandArray = makeCommandArray(null, mapEntry);
					else
					{
						StringBuilder builder = new StringBuilder();
						for (int i = 0; i < mapEntry.length(); ++i)
						{
							char ch = mapEntry.charAt(i);
							if ('%' == ch)
							{
								if (++i < mapEntry.length())
								{
									ch = mapEntry.charAt(i);
									if ('~' == ch)
									{
										if (2 <= commandArray.length)
										{
											builder.append(
												quoteSpacedString(
													commandArray[1]));
											for (int j = 2; j <
												commandArray.length; ++j)
											{
												builder.append(' ');
												builder.append(
													quoteSpacedString(
														commandArray[j]));
											}
										}
									}
									else if ('0' <= ch && '9' >= ch)
									{
										String num = "" + ch;
										while (++i < mapEntry.length())
										{
											ch = mapEntry.charAt(i);
											if ('0' <= ch && '9' >= ch)
												num += ch; else
											{
												int j = Integer.parseInt(num);
												if (j < commandArray.length)
													builder.append(
														quoteSpacedString(
															commandArray[j]));
												break;
											}
										}
										--i;
									}
									else builder.append(ch);
								}
							}
							else builder.append(ch);
						}
						commandArray = makeCommandArray(
							null, builder.toString());
					}
				}
			}
			else return 0;


			// Copy original environment variables to new TreeMap…
			TreeMap<String, String> envLocalHash =
				new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
			envLocalHash.putAll(originalEnvironment);
			// … and mix them with internal environment variables.
			envLocalHash.putAll(internalEnvironment);

			// Then create a vector of the records VARNAME=VALUE from it:
			Vector<String> envList = new Vector<String>();
			for (String envName : envLocalHash.keySet())
			{
				String value = envLocalHash.get(envName);
				if (null != value && !value.isEmpty())
					envList.add(envName + "=" + value);
			}


			// Echo the command (in a simple way, without quotes):
			debug("$ ");
			for (String cmdElement : commandArray)
				debug(cmdElement, ' ');
			debugLine();

			// Save the current running environment.
			runEnvironment = envList.toArray(new String[envList.size()]);

			// Run it!
			Runtime rt = Runtime.getRuntime();
			runningProcess = rt.exec(commandArray, runEnvironment,
				new File(null == currentPath ? defaultPath : currentPath));

			// Output both stdout and stderr data from
			// “runningProcess” to stdout of this process…

			errorPrinter = new StreamPrinter(
				runningProcess.getErrorStream(), true);
			outputPrinter = new StreamPrinter(
				runningProcess.getInputStream(), false);

			OutputStream os = runningProcess.getOutputStream();
			OutputStreamWriter isr = new OutputStreamWriter(
				os, currentEncoding);
			inputWriter = new BufferedWriter(isr);

			// TODO – žiadne konkrétne todo, len aby som to rýchlo našiel… ☺
			//
			// RUN: cmd /k prompt $
			// RUN: cls & Magnesium.bat & echo Exit code: %ERRORLEVEL%
			//
			// UTF-8
			// -----
			// Custom CMD: run cmd /a /k chcp 65001
			// TEST: run cmd /a /k "chcp 65001 & echo Čo"
			//
			// windows-1250
			// ------------
			// rem chcp Cp1250
			// chcp for cmd Cp1250
			// mapcmd cmd cmd /a /k chcp 1250 & %~
			// run cmd
			// rem run cmd /a /k "chcp 1250 & echo Čo"

			errorPrinter.start();
			outputPrinter.start();
			exitCode = runningProcess.waitFor();
		}
		catch (Exception e)
		{
			errLine(langstring[CANNOT_EXECUTE], '.', ' ',
				langstring[ERROR_OCCURED], '.', EOL,
				langstring[ERROR_MESSAGE], ": ", e.getMessage());
			debugLine(stackTraceToString(e));
			exitCode = -1;
		}
		finally
		{
			if (null != inputWriter)
			{
				try { inputWriter.close(); }
				catch (Throwable t) { /*ignore*/ }
				inputWriter = null;
			}

			if (null != errorPrinter)
			{
				try { errorPrinter.close(); }
				catch (Throwable t) { /*ignore*/ }
				errorPrinter = null;
			}

			if (null != outputPrinter)
			{
				try { outputPrinter.close(); }
				catch (Throwable t) { /*ignore*/ }
				outputPrinter = null;
			}

			processEnded(exitCode);

			runCommand = null;
			runArguments = null;
			commandArray = null;
			runEnvironment = null;

			runningProcess = null;
			runningThread = null;
		}

		return exitCode;
	}


	/**
	 * <p>Gets current path string. This path is used as the running path
	 * of the new process (see the “execute” method). Current path can be
	 * changed using “changePath” method.</p>
	 * 
	 * @return current path string (path used as the running path of the
	 *     new process)
	 */
	public String getCurrentPath()
	{ return null == currentPath ? defaultPath : currentPath; }

	/**
	 * <p>Sets the new path according to the string. If the string is
	 * null or empty, the default path (JVM running path) becomes active.
	 * The newPath string will be expanded (if it contains some variables),
	 * then it will be checked if it is an absolute path and eventually
	 * it will be converted to cannonical path.</p>
	 * 
	 * <p>The method may throw IOException.</p>
	 * 
	 * @param newPath the string changing the current path or null
	 * @return the new path or null; null means that the JVM running path
	 *     is active
	 */
	public String changePath(String newPath) throws IOException
	{
		if (null == newPath || newPath.isEmpty())
			currentPath = null;
		else
		{
			newPath = expandVariables(newPath);
			File path = new File(newPath);

			if (path.isAbsolute())
			{
				if (path.exists())
				{
					if (path.isDirectory())
						currentPath = path.getCanonicalPath();
					else
						throw new IOException(
							"Path is not a directory: " + path);
				}
				else
					throw new IOException(
						"Path does not exist: " + path);
			}
			else
			{
				if (null == currentPath || currentPath.isEmpty())
					path = new File(defaultPath + File.separator + newPath);
				else
					path = new File(currentPath + File.separator + newPath);

				if (path.exists())
				{
					if (path.isDirectory())
						currentPath = path.getCanonicalPath();
					else
						throw new IOException(
							"Path is not a directory: " + path);
				}
				else
					throw new IOException(
						"Path does not exist: " + path);
			}
		}

		return currentPath;
	}


	/**
	 * <p>Runs new process composing its command (the executable file
	 * possibly containing the relative or absolute path) and arguments from
	 * entered strings. The command is processed as is, without unescaping
	 * or splitting. The arguments are split and unescaped according to
	 * following rules:</p>
	 * 
	 * <ol>
	 * <li>Each space splits the string into separate arguments (unless it
	 * is escaped or quoted).</li>
	 * <li>Backslash \ escapes only next quote (") or space ( ), otherwise
	 * it stays the backslash in the meaning of a directory/folder separator.
	 * That means that only following two escape sequences are valid and
	 * processed: \" and \  (backslash with space).</li>
	 * <li>Similarly only the valid caret escape sequences are processed;
	 * they are as follows: ^", ^^, ^&, ^>, ^<, and ^|.</li>
	 * </ol>
	 * 
	 * <p>Unless some argument string is preceded by the null string
	 * (exculuding the “command” parameter itself). The argument after the
	 * null string argument is processed as is (like the “command”
	 * parameter).</p>
	 * 
	 * @param command a command to be executed (executable file used to
	 *     create new process)
	 * @param arguments arguments for the command
	 * @return true if the process (for the command) has started successfully
	 */
	public synchronized boolean execute(String command, String... arguments)
	{
		clearErr("execute");

		if (null != runningThread)
		{
			errLine(langstring[CANNOT_EXECUTE], ' ',
				langstring[MORE_PROCESSES], '.');
			return false;
		}
		else
		{
			clearLog("execute");
			clearDebug("execute");

			runCommand = command;
			runArguments = arguments;

			// System.out.println("Create thread…");
			runningThread = new Thread(() ->
			{
				// System.out.println("Run process…");
				errorLevel = runProcess();

				debugLine(langstring[EXIT_CODE], ": ", errorLevel);
				if (null != restoreEncoding)
				{
					currentEncoding = restoreEncoding;
					restoreEncoding = null;
					debugLine(langstring[ENCODING_RESTORED],
						": ", currentEncoding);
				}

				// System.out.println("Process exited…");
			});
			// System.out.println("Thread created…");

			SwingUtilities.invokeLater(() -> runningThread.start());
		}

		return true;
	}
}
