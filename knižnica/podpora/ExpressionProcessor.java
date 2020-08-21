
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorej nie je priamo
// vyhotovená  dokumentácia.  Je to trieda, ktorú  implementoval autor
// programovacieho  rámca mnoho  rokov pred jeho vznikom – v tom  čase
// v jazyku C++.  (Podľa pôvodných poznámok  sa jej vývoj začal v roku
// 2003 a posledná revízia a oprava  chýb bola vykonaná  v roku 2010.)
// O niekoľko rokov neskôr (po viacerých rokoch vývoja programovacieho
// rámca) ju portoval do jazyka Java,  ale súčasťou podporného balíčka
// programovacieho   rámca   sa  stala  až  v roku   2018.  (Pozri  aj
// podrobnosti  v  anglickom  jazyku  nižšie.)   Licencia  a  príklady
// použitia sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students  the basics about this class  and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.io.IOException;
import java.io.StringReader;

import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.*;

/**
 * <p>This class with a group of static  nested classes is based on  C++ class
 * CExpression  programmed by the same author.  The author rewrote it in 2014.
 * The original C++ class was created in the years 2003 – 2010. It has started
 * on  19-May-2003.  (The  C++ source  code  was  transformed  to  Unicode  on
 * 4-Mar-2009.)  On 8-Aug-2010  came some fixes and on 19-Mar-2014  the author
 * started  to create  this  Java version  of the original  source  code.  The
 * process  of rewriting was  finished  (in rough form) on 27-Mar-2014.  First
 * tests  showed that this library  is powerful enough to replace ScriptEngine
 * in existing projects (of the author).  One bug was fixed during the process
 * of rewriting, and the list of supported functions was extended.  There were
 * also  some minor improvements.  On 1-Jul-2015 creation  of a new version of
 * this  class has started.  Lots of code was optimized by changing  it to  be
 * more  Java-like  than c/C++-like.  For example, VariableScope’s Vector  was
 * substituted  by TreeMap.  Enumerations were  extended by abstract  methods.
 * Some  useless  stuff  has  been  removed.  Etc.  On  the  other  hand,  the
 * possibility to delay the processing  of variable names after the expression
 * parsing (that means during the evaluation process) was added.</p>
 * 
 * <p>On August 2nd 2018, this class became  the part of supporting  package
 * of  the GRobot  framework.  Several  unfinished features  (including  the
 * claimed customFunction) were completed and  new were add.  The steps made
 * should make the class more compatible with the framework.</p>
 * 
 * <p>On April 26th 2019, another  feature was  implemented.  The ability
 * to  join  the  identifiers  to  make  the  processor  compatible  with
 * the GRobot’s scripting engine. (See the methods joinsTheIdentifiers(),
 * joinTheIdentifiers(join), and joinIdentifiers(id1, id2).)</p>
 * 
 * <p>On June 1st 2020, the class was extended with thirteen new static
 * methods (from advancedOr to advancedPow). The purpose of the methods
 * is to extend the class by new features in connection to some special
 * operations, like “multiplying/dividing the strings (or other types)”
 * (methods: advancedMul, advancedDiv) and similarly.</p>
 * 
 * <p>(Notice: The author did not fix the bug(s) in the original C++ class
 * because he  had no C++ compiler  installed at the time  and it would be
 * time-consuming to get one, install it, set up it, and fix the bug(s).)</p>
 * 
 * <p><b>Example:</b></p>
 * 
<pre>
	String[] strings = {
		"(1 + 2) (12 / 3)",   // parse error
		"(1 + 2) * (12 / 3)", // 3 * 4 = 12
		"1 + 2 * (12 / 3)",   // 1 + 2 * 4 = 9
		"(1 + 2) * 12 / 3",   // 3 * 12 / 3 = 12
		"1 + 2 * 12 / 3",     // 1 + 24 / 3 = 9
	};

	for (String string : strings)
	{
		System.out.println("****************\nParse: " + string);

		ExpressionProcessor expression = new ExpressionProcessor();

		if (expression.attachString(string))
		{
			if (expression.parse())
			{
				System.out.println("OK!");
				System.out.println(expression.getRoot().
					dumpRecursive() + " : " + expression.getValue());
			}
			else
			{
				System.out.println(expression);
			}
		}
		else System.out.println("Cannot attach this string…");

		System.out.println();
	}
</pre>
 * 
 * <p>Output:</p>
 * 
<pre>
	****************
	Parse: (1 + 2) (12 / 3)
	Error: parse error – invalid parentheses at 16

	****************
	Parse: (1 + 2) * (12 / 3)
	OK!
	*([+](1.0, 2.0), [/](12.0, 3.0)) : 12.0

	****************
	Parse: 1 + 2 * (12 / 3)
	OK!
	+(1.0, *(2.0, [/](12.0, 3.0))) : 9.0

	****************
	Parse: (1 + 2) * 12 / 3
	OK!
	/(*([+](1.0, 2.0), 12.0), 3.0) : 12.0

	****************
	Parse: 1 + 2 * 12 / 3
	OK!
	+(1.0, /(*(2.0, 12.0), 3.0)) : 9.0
</pre>
 * 
 * <p>Full list of operators and functions is in the introduction
 * (in the source code) of an appropriate enumeration class.</p>
 * 
 * @author Roman Horváth
 * @version 26. 4. 2019
 * 
 * @exclude
 */
public class ExpressionProcessor implements ValueProvider
{
	//////////////////////////////////////////////////////////////////////
	// Literal nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class represents literal values in expressions. It may be number
	 * or string.
	 */
	public static class Literal implements ValueProvider, Comparable<Literal>
	{
		// Numeric value
		private double value;

		// String value
		private String string;

		//////////////////
		// Constructors

		/**
		 * Copy constructor. Copies everything from the source instance
		 * (one to one).
		 */
		public Literal(Literal source)
		{
			value = source.value;
			string = source.string;
		}

		/**
		 * Value initialiser constructor.
		 */
		public Literal(Value source)
		{
			value = source.value;
			string = source.string;
		}

		/**
		 * Numeric value constructor.
		 */
		public Literal(double value)
		{
			this.value = value;
			string = null;
		}

		/**
		 * String type value constructor.
		 */
		public Literal(String string)
		{
			value = Double.NaN;
			this.string = string;
		}

		// Getters and type checks

		/**
		 * Checks whether this instance is representing valid numeric value.
		 */
		public boolean isValidNumber()
		{ return !Double.isNaN(value) && !Double.isInfinite(value); }

		/**
		 * This method returns numeric (double) value stored inside
		 * this instance.
		 */
		public double get() { return value; }

		/**
		 * This is part of implementation the ValueProvider interface. It
		 * creates a new Value instance from numeric or string value stored in
		 * this instance.
		 */
		public Value getValue()
		{
			if (null != string) return new Value(string);
			return new Value(value);
		}

		/**
		 * Checks whether this instance is representing a string value.
		 */
		public boolean isString() { return null != string; }

		/**
		 * This is part of implementation the ValueProvider interface. It
		 * returns either string value stored in this instance or string
		 * representation of numeric value of this instance converted to
		 * string.
		 */
		public String getString()
		{
			if (null != string) return string;
			return Double.toString(value);
		}

		// The value of literal MUST NOT change over time, so there are no
		// setters defined.


		// Common overrides

		/**
		 * Returns either valid real value converted to string or original
		 * string value stored in this instance.
		 */
		@Override public String toString()
		{
			if (null != string) return '"' + string + '"';
			return Double.toString(value);
		}

		/**
		 * This method is implementation of Comparable interface, it is
		 * dedicated to be internally used by associative list mechanism
		 * and MUST NOT be used to compare values during evaluation process.
		 */
		@Override public int compareTo(Literal another)
		{
			if (isString() && another.isString())
				return getString().compareTo(another.getString());
			else if (isString()) return 1;
			else if (another.isString()) return -1;

			return Double.compare(get(), another.get());
		}
	}

	//////////////////////////////////////////////////////////////////////
	// Value nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class represents typed variable values, therefore this is
	 * the superclass of Variable class. It also stores all error states.
	 */
	public static class Value implements ValueProvider
	{
		/**
		 * List of types and error states.
		 * (See also: Node.Class.TYPE_OR_ERROR)
		 */
		public static enum TypeOrError
		{
			NUMERIC_TYPE			("numeric type value"),
			STRING_TYPE				("string type value"),
			LIST_TYPE				("list type value"),

			DIVISION_BY_ZERO		("compute error – division by zero"),
			UNSUPPORTED_OPERATION	("compute error – unsupported operation"),
			ARGUMENT_DOMAIN_ERROR	("compute error – argument domain error"),
			TOO_MANY_ARGUMENTS		("compute error – too many arguments"),
			OVERFLOW				("compute error – overflow range error"),
			INVALID_EXPRESSION		("compute error – invalid expression"),
			INVALID_ARGUMENT		("compute error – invalid argument"),
			MISSING_ARGUMENT		("compute error – missing argument"),
			INVALID_LIST			("compute error – invalid list"),

			// Next two are fatal errors:
			UNKNOWN_FUNCTION		("compute error – unknown function code"),
			UNKNOWN_OPERATOR		("compute error – unknown operator code"),

			UNKNOWN_VALUE			("compute error – unknown value"),
			UNKNOWN					("compute error – unknown error"),
			MISSING_IDENTIFIER		("compute error – missing identifier"),
			UNKNOWN_VARIABLE_COMP	("compute error – unknown variable"),
			UNKNOWN_NAMESPACE		("compute error – unknown namespace"),
			NO_EXPRESSION			("expression does not exist"),

			INVALID_NUMERIC_LITERAL	("parse error – invalid numeric literal"),
			INVALID_STRING_LITERAL	("parse error – invalid string literal"),
			INVALID_CHARACTER		("parse error – invalid character"),
			INVALID_PARENTHESES		("parse error – invalid parentheses"),
			INVALID_BRACKETS		("parse error – invalid brackets"),
			MISSING_EXPRESSION		("parse error – missing expression"),
			UNKNOWN_VARIABLE_PARS	("parse error – unknown variable"),

			OPERATOR_EXPECTED		("parse error – operator expected"),
			IDENTIFIER_EXPECTED		("parse error – identifier expected"),
			ODD_OPERAND				("parse error – odd operand"),
			ODD_PARAMETER			("parse error – odd parameter"),
			MISSING_OPERAND			("parse error – missing operand"),
			MISSING_PARAMETER		("parse error – missing parameter"),
			ILLEGAL_FUNCTION_CALL	("parse error – illegal function call"),
			INVALID_OPERATOR		("parse error – invalid operator"),
			INVALID_CUSTOM_FUNCTION	("parse error – invalid custom function"),

			UNKNOWN_VARIABLE		("unknown variable"),
			VARIABLE_EXISTS			("variable already exists"),
			INVALID_ASSIGNMENT		("invalid assignment"),
			NO_SUCH_ELEMENT			("no such element"),

			PARSE_ERROR				("parse error"); // all other parse errors

			// Examples of some parse errors:
			//
			//	INVALID_NUMERIC_LITERAL – 1.1.8
			//	INVALID_CHARACTER       – # &
			//	INVALID_PARENTHESES     – sin 0.4 (1+1)
			//	OPERATOR_EXPECTED       – (1+2) (8/4)
			//	ODD_OPERAND             – 1 + 1 2
			//	ODD_PARAMETER           – sin 1 4
			//	ILLEGAL_FUNCTION_CALL   – 2 sin, (1+2) abs
			//	INVALID_OPERATOR        – &+
			//	INVALID_ASSIGNMENT      – 11 = 23

			/** Description of the type or error state. */
			public final String description;

			// The default constructor.
			private TypeOrError(String description)
			{
				this.description = description;
			}

			/** Converts the instance to string. */
			@Override public String toString()
			{
				return description;
			}
		}


		// Valid numeric value used if this is numeric type; typeOrError is
		// set to NUMERIC_TYPE
		private double value;

		// String value for string type output; typeOrError is set to
		// STRING_TYPE
		private String string;

		// List type; typeOrError is set to LIST_TYPE
		private TreeMap<Literal, Value> list;

		// Type of this instance – value or error (NUMERIC_TYPE – valid
		// numeric value; STRING_TYPE – valid string value; LIST_TYPE – valid
		// list)
		private TypeOrError typeOrError;

		// Used to indicate an error location
		private long errorLocation;


		//////////////////
		// Constructors

		/**
		 * Copy constructor. Copies everything from source one to one.
		 */
		public Value(Value source)
		{
			value = source.value;
			string = source.string;
			if (null == source.list) list = null;
			else list = new TreeMap<Literal, Value>(source.list);
			typeOrError = source.typeOrError;
			errorLocation = source.errorLocation;
		}

		/**
		 * Numeric value constructor. If the entered value is Double.NaN or
		 * infinite then the error state of the instance is set to
		 * Value.TypeOrError.OVERFLOW.
		 */
		public Value(double value)
		{
			if (Double.isNaN(value) || Double.isInfinite(value))
			{
				this.value = value;
				string = null;
				list = null;
				typeOrError = TypeOrError.OVERFLOW;
				errorLocation = 0;
			}
			else
			{
				this.value = value;
				string = null;
				list = null;
				typeOrError = TypeOrError.NUMERIC_TYPE;
				errorLocation = 0;
			}
		}

		/**
		 * String type value constructor. This sets the numeric value to
		 * Double.NaN and error state to Value.TypeOrError.STRING_TYPE.
		 */
		public Value(String string)
		{
			value = Double.NaN;
			this.string = string;
			list = null;
			typeOrError = TypeOrError.STRING_TYPE;
			errorLocation = 0;
		}

		/**
		 * This constructor even sets the value to specified value or sets
		 * specified error state for this instance. It depends on entered
		 * double value: (1) if it is valid value (valid real number) the
		 * typeOrError state is set to Value.TypeOrError.NUMERIC_TYPE and
		 * the instance will represent valid double value, (2) if the value
		 * is Double.NaN or infinite, then the error state is set to specified
		 * error coming from specified location.
		 */
		public Value(double value, TypeOrError typeOrError, long errorLocation)
		{
			string = null; list = null;
			if (Double.isNaN(value) || Double.isInfinite(value))
			{
				this.value = 0.0;
				this.typeOrError = typeOrError;
				this.errorLocation = errorLocation;
			}
			else
			{
				this.value = value;
				this.typeOrError = TypeOrError.NUMERIC_TYPE;
				this.errorLocation = 0;
			}
		}

		/**
		 * Basic error state constructor. It creates specified error state at
		 * unknown location (0).
		 */
		public Value(TypeOrError error)
		{
			value = 0.0;
			string = null;
			list = null;
			this.typeOrError = error;
			errorLocation = 0;
		}

		/**
		 * Full error state constructor. It creates specified error state at
		 * specified location.
		 */
		public Value(TypeOrError error, long errorLocation)
		{
			value = 0.0;
			string = null;
			list = null;
			this.typeOrError = error;
			this.errorLocation = errorLocation;
		}


		// Universal methods

		/**
		 * Copies everything from source instance to this instance one to one.
		 */
		public void copy(Value source)
		{
			value = source.value;
			string = source.string;
			if (null != list) list.clear();
			if (null != source.list)
			{
				if (null == list)
					list = new TreeMap<Literal, Value>(source.list);
				else
					list.putAll(source.list);
			}
			typeOrError = source.typeOrError;
			errorLocation = source.errorLocation;
		}

		// It must be told that there is possible to create infinite recursive
		// structure of list and preventing this showed to be complicated and
		// it is possible that preventing it would decrease the effectivity of
		// ExpressionProcessor’s algorithms more than necessary (the gain
		// from prevention would be less than the loss of algorithm’s
		// effectivity). But this method helps to detect such jams.
		private boolean recursiveJam(Value instance)
		{
			if (instance == this) return true;

			if (TypeOrError.LIST_TYPE == typeOrError)
			{
				if (null != list)
				{
					for (Value value : list.values())
						if (value.recursiveJam(instance)) return true;
				}
			}

			return false;
		}


		// Methods connected to single numeric value

		/**
		 * Checks whether this instance is representing valid numeric value.
		 */
		public boolean isValidNumber()
		{
			return TypeOrError.NUMERIC_TYPE == typeOrError &&
				!Double.isNaN(value) && !Double.isInfinite(value);
		}

		/**
		 * Checks whether this instance is representing some kind of
		 * numeric value (invalid or valid).
		 */
		public boolean isNumber()
		{ return TypeOrError.NUMERIC_TYPE == typeOrError; }

		/**
		 * Sets the numeric value according to entered primitive double value.
		 * If the entered value is Double.NaN or infinite, then the typeOrError
		 * state of this instance is set to Value.TypeOrError.OVERFLOW.
		 */
		public void set(double value)
		{
			if (Double.isNaN(value) || Double.isInfinite(value))
			{
				this.value = value;
				string = null;
				if (null != list) list.clear();
				typeOrError = TypeOrError.OVERFLOW;
				errorLocation = 0;
			}
			else
			{
				this.value = value;
				string = null;
				if (null != list) list.clear();
				typeOrError = TypeOrError.NUMERIC_TYPE;
				errorLocation = 0;
			}
		}

		/**
		 * This method even sets the value to specified value or sets
		 * specified error state for this instance. It depends on entered
		 * double value: (1) if it is valid value (valid real number) the
		 * typeOrError state is set to Value.TypeOrError.NUMERIC_TYPE and
		 * the instance will represent valid double value, (2) if the value
		 * is Double.NaN or infinite, then the error state is set to specified
		 * error coming from specified location.
		 */
		public void set(double value, TypeOrError typeOrError,
			long errorLocation)
		{
			if (Double.isNaN(value) || Double.isInfinite(value))
			{
				this.value = 0.0;
				string = null;
				if (null != list) list.clear();
				this.typeOrError = typeOrError;
				this.errorLocation = errorLocation;
			}
			else
			{
				this.value = value;
				string = null;
				if (null != list) list.clear();
				this.typeOrError = TypeOrError.NUMERIC_TYPE;
				this.errorLocation = 0;
			}
		}

		/**
		 * This method either returns valid real value represented by this
		 * instance or Double.NaN value if this instance represents an error
		 * state or another type (like string or list). For detailed
		 * information about error state use getError and getErrorLocation
		 * methods.
		 */
		public double get()
		{
			return TypeOrError.NUMERIC_TYPE == typeOrError ?
				value : Double.NaN;
		}

		/**
		 * This is part of implementation the ValueProvider interface. It just
		 * returns itself because this class IS the Value class.
		 */
		public Value getValue()
		{
			return this;
		}


		// Methods connected to advanced data types (errors, strings,
		// and lists)

		/**
		 * Checks whether this instance is representing a string value.
		 */
		public boolean isString()
		{ return TypeOrError.STRING_TYPE == typeOrError; }

		/**
		 * Checks whether this instance is representing list.
		 */
		public boolean isList()
		{ return TypeOrError.LIST_TYPE == typeOrError; }

		// /**
		//  * Checks whether this instance is representing an error state.
		//  */
		// public boolean isError() { return TypeOrError.NUMERIC_TYPE != typeOrError; }

		/**
		 * Checks whether this instance is representing an error state.
		 * That means it does not contain a valid type (numeric, string,
		 * nor list). The error state of this instance may indicate various
		 * error states (like parse errors).
		 */
		public boolean isError()
		{
			return TypeOrError.NUMERIC_TYPE != typeOrError &&
				TypeOrError.STRING_TYPE != typeOrError &&
				TypeOrError.LIST_TYPE != typeOrError;
		}


		/**
		 * Sets the string value of this instance. This sets the numeric
		 * value to Double.NaN and typeOrError state to
		 * Value.TypeOrError.STRING_TYPE.
		 */
		public void set(String string)
		{
			value = Double.NaN;
			this.string = string;
			if (null != list) list.clear();
			typeOrError = TypeOrError.STRING_TYPE;
			errorLocation = 0;
		}

		/**
		 * This is part of implementation the ValueProvider interface. It
		 * returns either the valid string value (TypeOrError.STRING_TYPE),
		 * list converted to string (TypeOrError.LIST_TYPE; see listToString()
		 * method), string representation of valid numeric value or the
		 * Double.NaN converted to string.
		 */
		public String getString()
		{
			if (TypeOrError.STRING_TYPE == typeOrError)
			{
				if (null == string) return "" + null;
				return string;
			}

			if (TypeOrError.LIST_TYPE == typeOrError)
			{
				if (null == list) return "" + null;
				return listToString();
			}

			if (TypeOrError.NUMERIC_TYPE == typeOrError)
				return Double.toString(value);

			return Double.toString(Double.NaN);
		}


		/**
		 * This is one of several possibilities of how to set value of one
		 * element at specified index of list. (Not all possibilities are
		 * covered. There are too many combinations.) If this instance is
		 * not list type, it is converted to list automatically. Then value
		 * of element at specified index (first parameter) is set to entered
		 * value (second parameter).
		 */
		public void set(Literal index, Value value)
		{
			convertToEmptyList();
			list.put(index, new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(Literal index, double value)
		{
			convertToEmptyList();
			list.put(index, new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(Literal index, String value)
		{
			convertToEmptyList();
			list.put(index, new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(Value index, Value value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(Value index, double value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(Value index, String value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(double index, Value value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(double index, double value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(double index, String value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(String index, Value value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(String index, double value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}

		/**
		 * This is another possibility of how to set element of list.
		 * Behaviour is similar to behaviour of method set(Literal, Value).
		 * (See its description.)
		 */
		public void set(String index, String value)
		{
			convertToEmptyList();
			list.put(new Literal(index), new Value(value));
		}


		/**
		 * This method tries to get numeric value of list item. It either
		 * returns valid real value of the specified element (on associated
		 * index) or Double.NaN, if there is no such element exists or element
		 * represents an error state. (For detailed information about error
		 * state use getValue(Value) with getError() or getErrorLocation
		 * method.)
		 */
		public double get(Literal index)
		{
			if (TypeOrError.LIST_TYPE == typeOrError)
			{
				Value element = list.get(index);
				if (null != element)
					return TypeOrError.NUMERIC_TYPE == element.typeOrError ?
						element.value : Double.NaN;
			}
			return Double.NaN;
		}

		/**
		 * This method tries to get numeric value of list item. It either
		 * returns valid real value of the specified element or Double.NaN.
		 * (See description of method Value.get(Value) for more details.)
		 */
		public double get(double index)
		{
			return get(new Literal(index));
		}

		/**
		 * This method tries to get numeric value of list item. It either
		 * returns valid real value of the specified element or Double.NaN.
		 * (See description of method Value.get(Value) for more details.)
		 */
		public double get(String index)
		{
			return get(new Literal(index));
		}


		/**
		 * This method checks whether specified element exists.
		 */
		public boolean elementExists(Literal index)
		{
			if (TypeOrError.LIST_TYPE == typeOrError)
				return list.containsKey(index);
			return false;
		}

		/**
		 * This method checks whether specified element exists.
		 */
		public boolean elementExists(Value index)
		{
			return elementExists(new Literal(index));
		}

		/**
		 * This method checks whether specified element exists.
		 */
		public boolean elementExists(double index)
		{
			return elementExists(new Literal(index));
		}

		/**
		 * This method checks whether specified element exists.
		 */
		public boolean elementExists(String index)
		{
			return elementExists(new Literal(index));
		}


		/**
		 * Gets the number of elements in the list or -1 if the type of
		 * this value is not a list.
		 */
		public int size()
		{
			if (TypeOrError.LIST_TYPE == typeOrError)
				return list.size();
			return -1;
		}

		/**
		 * This method returns list of valid indexes (in the form of array),
		 * if this instance is the list or null in the other case.
		 */
		public Literal[] getIndexes()
		{
			if (TypeOrError.LIST_TYPE == typeOrError)
				return (Literal[])list.keySet().toArray();
			return null;
		}

		/**
		 * This method returns either value of the specified element (the
		 * element associated with the specified index) or
		 * TypeOrError.NO_SUCH_ELEMENT, if there is no such element exists.
		 */
		public Value getValue(Literal index)
		{
			if (TypeOrError.LIST_TYPE == typeOrError)
			{
				Value element = list.get(index);
				if (null != element) return element;
			}
			return new Value(TypeOrError.NO_SUCH_ELEMENT);
		}

		/**
		 * This method returns either value of the specified element (the element
		 * associated with the specified index) or TypeOrError.NO_SUCH_ELEMENT, if
		 * there is no such element exists.
		 */
		public Value getValue(Value index)
		{
			return getValue(new Literal(index));
		}

		/**
		 * This method returns either value of the specified element (the element
		 * associated with the specified index) or TypeOrError.NO_SUCH_ELEMENT, if
		 * there is no such element exists.
		 */
		public Value getValue(double index)
		{
			return getValue(new Literal(index));
		}

		/**
		 * This method returns either value of the specified element (the element
		 * associated with the specified index) or TypeOrError.NO_SUCH_ELEMENT, if
		 * there is no such element exists.
		 */
		public Value getValue(String index)
		{
			return getValue(new Literal(index));
		}


		/**
		 * Converts this instance to empty list. That means that the original
		 * value of non-list type is dismissed. If this instance is already
		 * a list type, no change is made.
		 */
		public void convertToEmptyList()
		{
			if (TypeOrError.LIST_TYPE != typeOrError)
			{
				if (null == list)
					list = new TreeMap<Literal, Value>();
				else
					list.clear();

				value = Double.NaN;
				string = null;
				typeOrError = TypeOrError.LIST_TYPE;
				errorLocation = 0;
			}
		}

		/**
		 * Converts this instance to list (if this instance was not list).
		 * The original instance value is stored as first element of the list.
		 */
		public void convertToList()
		{
			if (TypeOrError.LIST_TYPE != typeOrError)
			{
				if (null == list)
				{
					TreeMap<Literal, Value> newList =
						new TreeMap<Literal, Value>();
					newList.put(new Literal(0.0), new Value(this));
					list = newList;
				}
				else
				{
					list.clear();
					TreeMap<Literal, Value> listBackup = list;
					list = null;
					listBackup.put(new Literal(0.0), new Value(this));
					list = listBackup;
				}

				value = Double.NaN;
				string = null;
				typeOrError = TypeOrError.LIST_TYPE;
				errorLocation = 0;
			}
		}

		/**
		 * Converts this instance to list type and sets its length to zero.
		 */
		public void clear()
		{
			if (TypeOrError.LIST_TYPE != typeOrError)
			{
				value = Double.NaN;
				string = null;
				typeOrError = TypeOrError.LIST_TYPE;
				errorLocation = 0;
			}

			if (null == list)
				list = new TreeMap<Literal, Value>();
			else
				list.clear();
		}

		/**
		 * If this instance is not list, converts it to list type, existing
		 * original value of this instance will be copied to first element.
		 * Eventually adds entered element to this list with next index
		 * number. However, if the entered element is also list, then each
		 * of its elements will be added individually at the end of this
		 * list, which will be performed recursively.
		 */
		public void add(Value element)
		{
			convertToList();

			if (element.isList())
			{
				if (null != element.list)
					for (Value value : element.list.values())
						add(value);
			}
			else if (list.isEmpty())
			{
				list.put(new Literal(0.0), new Value(element));
			}
			else
			{
				Literal nextKey, lastKey = list.lastKey();
				if (null == lastKey)
				{
					nextKey = new Literal(0.0);
				}
				else if (lastKey.isValidNumber())
				{
					nextKey = new Literal(lastKey.get() + 1);
				}
				else
				{
					do { lastKey = list.lowerKey(lastKey); }
					while (null != lastKey && !lastKey.isValidNumber());

					if (null == lastKey)
						nextKey = new Literal(0.0);
					else
						nextKey = new Literal(lastKey.get() + 1);
				}

				list.put(nextKey, new Value(element));
			}
		}

		/**
		 * Behaves just like add(Value) method.
		 */
		public void add(double element)
		{
			add(new Value(element));
		}

		/**
		 * Behaves just like add(Value) method.
		 */
		public void add(String element)
		{
			add(new Value(element));
		}

		/**
		 * Converts list type to string – creates recursively concatenated
		 * string from list items. If the list is empty or this instance is
		 * not a list type, then an empty string will be returned.
		 */
		public String listToString()
		{
			StringBuffer sb = new StringBuffer();
			if (TypeOrError.LIST_TYPE == typeOrError && null != list)
			{
				for (Value value : list.values())
					try
					{
						if (!value.recursiveJam(this))
							sb.append(value.getString());
					}
					catch (Throwable e)
					{
						// System.err.println(e.getMessage());
					}
			}
			return sb.toString();
		}


		/**
		 * This method sets specified error state at unknown location (0) for
		 * this instance.
		 */
		public void setError(TypeOrError error)
		{
			this.typeOrError = error;
			errorLocation = 0;
		}

		/**
		 * This method sets specified error state at specified location for
		 * this instance.
		 */
		public void setError(TypeOrError error, long errorLocation)
		{
			this.typeOrError = error;
			this.errorLocation = errorLocation;
		}

		/**
		 * Gets an error state of the instance no matter wheter it contains
		 * the error code or not. For example the type
		 * Value.TypeOrError.NUMERIC_TYPE means that this instance is
		 * representing a valid double value.
		 */
		public TypeOrError getError() { return typeOrError; }

		/**
		 * Gets the location of error state of the instance. There is no
		 * special value returned in case that this instance is representing
		 * a valid type – a real number, for example.
		 */
		public long getErrorLocation() { return errorLocation; }


		/**
		 * Creates recursively concatenated human-readable dump of a list type.
		 * An entered separator will separate list items. This list itself
		 * and the nested sublists will start with prefix string and end with
		 * postfix string. This method use methods Literal.toString() and
		 * Value.toString() which automatically enclose the string values in
		 * quotes (&quot;). If index separator is not null, then the dump will
		 * also contain indexes.
		 */
		public String dumpList(String prefix, String separator,
			String postfix, String indexSeparator)
		{
			StringBuffer sb = new StringBuffer();
			if (TypeOrError.LIST_TYPE == typeOrError && null != list)
			{
				if (null != prefix) sb.append(prefix);
				boolean first = true;

				if (null != indexSeparator)
				{
					for (Map.Entry<Literal, Value> entry : list.entrySet())
					{
						if (first) first = false;
						else if (null != separator) sb.append(separator);

						Value value = entry.getValue();
						sb.append(entry.getKey());
						sb.append(indexSeparator);

						if (TypeOrError.LIST_TYPE == value.typeOrError)
						{
							if (null != value.list)
							{
								try
								{
									if (value.recursiveJam(this))
										sb.append("{jam}");
									else
										sb.append(value.dumpList(
											prefix, separator, postfix,
											indexSeparator));
								}
								catch (Throwable e)
								{
									sb.append("{jam}");
									// System.err.println(e.getMessage());
								}
							}
						}
						else
							sb.append(value.toString());
					}
				}
				else
				{
					for (Value value : list.values())
					{
						if (first) first = false;
						else if (null != separator) sb.append(separator);

						if (TypeOrError.LIST_TYPE == value.typeOrError)
						{
							if (null != value.list)
							{
								try
								{
									if (value.recursiveJam(this))
										sb.append("{jam}");
									else
										sb.append(value.dumpList(
											prefix, separator, postfix,
											indexSeparator));
								}
								catch (Throwable e)
								{
									sb.append("{jam}");
									// System.err.println(e.getMessage());
								}
							}
						}
						else
							sb.append(value.toString());
					}
				}

				if (null != postfix) sb.append(postfix);
			}
			return sb.toString();
		}


		// Common overrides

		/**
		 * Returns string representation of this instance. It differs by type:
		 * valid real value, original string value, concatenated list, or text
		 * of error state with its location.
		 */
		@Override public String toString()
		{
			if (TypeOrError.STRING_TYPE == typeOrError)
			{
				if (null == string) return "" + null;
				return '"' + string + '"';
			}

			if (TypeOrError.LIST_TYPE == typeOrError)
			{
				if (null == list) return "" + null;
				return dumpList("{", ", ", "}", ": ");
			}

			if (TypeOrError.NUMERIC_TYPE == typeOrError)
				return Double.toString(value);

			return "Error: " + typeOrError + " at " + errorLocation;
		}
	}


	//////////////////////////////////////////////////////////////////////
	// Identifier nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class stores identifiers of a variables or namespaces that must
	 * be found at runtime that means in the time of evaluating the parsed
	 * expression.
	 */
	public static class Identifier
	{
		/**
		 * Identifier of a variable. Must not be null.
		 */
		public final String identifier;

		/**
		 * This instance serves to access to all scopes where the variable
		 * might be found.
		 */
		public final ExpressionProcessor processor;

		/**
		 * Constructor receiving just identifier.
		 */
		public Identifier(ExpressionProcessor processor, String identifier)
		{
			this.processor = processor;
			this.identifier = identifier;
		}
	}


	//////////////////////////////////////////////////////////////////////
	// Variable nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * Objects of this class store named values are known as variables.
	 */
	public static class Variable extends Value
	{
		// Variable string identificator
		private String variableName;


		//////////////////
		// Constructors

		/**
		 * Creates a variable with the specified name and value.
		 */
		public Variable(String variableName, Value initialValue)
		{
			super(initialValue);
			this.variableName = variableName;
		}

		/**
		 * Creates a variable with the specified name and value.
		 */
		public Variable(String variableName, double initialValue)
		{
			super(initialValue);
			this.variableName = variableName;
		}

		/**
		 * Creates variable with the specified name and value.
		 */
		public Variable(String variableName, String initialValue)
		{
			super(initialValue);
			this.variableName = variableName;
		}

		/**
		 * Creates variable with the specified name initialised to 0.0 value.
		 */
		public Variable(String variableName)
		{
			super(0.0);
			this.variableName = variableName;
		}


		/**
		 * Gets variable name (variable’s string identificator).
		 */
		public String getName() { return variableName; }
	}


	//////////////////////////////////////////////////////////////////////
	// VariableScope nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class provides space for a group of variables accessible from
	 * the specific variable scope. Only one scope is predefined – it is the
	 * global space for variables. The name of its instance is:
	 * globalVariables.
	 */
	public static class VariableScope
	{
		/**
		 * This instance is returned after attempting to declare or define
		 * the already existing variable. It contains
		 * Value.TypeOrError.VARIABLE_EXISTS error state (at location 0).
		 */
		public final static Value VARIABLE_EXISTS =
			new Value(Value.TypeOrError.VARIABLE_EXISTS, 0);

		/**
		 * This instance is returned after attempting to read the value of
		 * a non-existing variable. It contains
		 * Value.TypeOrError.UNKNOWN_VARIABLE error state (at location 0).
		 */
		public final static Value UNKNOWN_VARIABLE =
			new Value(Value.TypeOrError.UNKNOWN_VARIABLE, 0);


		// List of nested namespaces
		private final TreeMap<String, VariableScope> namespaces =
			new TreeMap<String, VariableScope>();

		// List of variables
		private final TreeMap<String, Variable> list =
			new TreeMap<String, Variable>();


		//////////////////////////////////////////
		// Clearing/deleting the scope contents

		/**
		 * Clears this variable scope. Deletes everything inside – all
		 * variables and all nested namespaces.
		 */
		public void clear()
		{
			list.clear();
			for (VariableScope namespace : namespaces.values())
				namespace.clear();
			namespaces.clear();
		}

		/**
		 * Clears and removes specified nested namespace from this
		 * namespace.
		 */
		public void removeNamespace(String namespace)
		{
			VariableScope scope = namespaces.get(namespace);
			if (null != scope)
			{
				scope.clear();
				namespaces.remove(namespace);
			}
		}

		/**
		 * Removes specified variable from this namespace.
		 */
		public void removeVariable(String variableName)
		{
			Variable variable = list.get(variableName);
			if (null != variable) list.remove(variableName);
		}

		///////////////////////////////////////
		// Creating and searching namespaces

		/**
		 * This method creates a nested namespaces. It checks if entered
		 * namespace exists first and if not, it creates a new one and returns
		 * it as the return value. Otherwise, null is returned.
		 * 
		 * Notice that namespaces are in use only after calling
		 * ExpressionProcessor.useNamespaces(true) method.
		 * 
		 * There is one (predefined) global list of namespaces. Each
		 * namespace is, in fact, an independent variable scope and each
		 * variable scope may contain any number of nested namespaces.
		 */
		public VariableScope createNamespace(String namespace)
		{
			if (namespaces.containsKey(namespace)) return null;
			VariableScope newNamespace = new VariableScope();
			namespaces.put(namespace, newNamespace);
			return newNamespace;
		}

		/**
		 * This method gets nested namespace. It checks if entered namespace
		 * exists first and if so, it returns it. Otherwise, it returns null.
		 * 
		 * Notice that namespaces are in use only after calling
		 * ExpressionProcessor.useNamespaces(true) method.
		 */
		public VariableScope getNamespace(String namespace)
		{
			return namespaces.get(namespace);
		}

		/**
		 * This method gets a list of nested namespaces in the form of
		 * a string array. If no nested namespace is defined, an empty array
		 * is returned. (The method does not return null.)
		 */
		public String[] getNamespaces()
		{
			return (String[])namespaces.keySet().toArray();
		}

		//////////////////////////////////////
		// Creating and searching variables

		/**
		 * Creates a new variable with the specified name and returns it as instance
		 * only if it does not exist. If variable did exist before, the null
		 * value is returned. New variable is initialised by 0.0.
		 */
		public Variable create(String variableName)
		{
			if (list.containsKey(variableName)) return null;

			Variable variable = new Variable(variableName);
			list.put(variableName, variable);
			return variable;
		}

		/**
		 * Creates a new variable with the specified name and initial value and
		 * returns it as instance only if it does not exist. If variable did
		 * exist before, the null value is returned.
		 */
		public Variable create(String variableName, Value newValue)
		{
			if (list.containsKey(variableName)) return null;

			Variable variable = new Variable(variableName, newValue);
			list.put(variableName, variable);
			return variable;
		}

		/**
		 * Gets existing variable by its name. If the variable does not exist,
		 * the null value is returned.
		 */
		public Variable get(String variableName)
		{
			return list.get(variableName);
		}

		/**
		 * Gets existing or creates a new variable with the specified name and
		 * returns the relevant instance. (New variables are initialised by
		 * 0.0.)
		 */
		public Variable getOrCreate(String variableName)
		{
			Variable variable = list.get(variableName);
			if (null == variable)
			{
				variable = new Variable(variableName);
				list.put(variableName, variable);
			}
			return variable;
		}

		/**
		 * This method gets a list of all variables in this namespace
		 * in the form of a string array. If no variable is defined, an
		 * empty array is returned. (The method does not return null.)
		 */
		public String[] getVariables()
		{
			return (String[])list.keySet().toArray();
		}

		///////////////////////////////////////////////////////////
		// Declaring, defining, assigning and getting the values

		/**
		 * Declares new (and only new) variable with the specified name and
		 * returns the result as follows: If the variable does not exist then
		 * the return value is equal to the initial value of a new variable
		 * (which is 0.0). If the variable already exists, then the return
		 * value is the VariableScope.VARIABLE_EXISTS instance (which contains
		 * the typeOrError value: Value.TypeOrError.VARIABLE_EXISTS).
		 */
		public Value declare(String variableName)
		{
			Variable variable = create(variableName);
			if (null == variable)
				return VariableScope.VARIABLE_EXISTS;
			return variable;
		}

		/**
		 * Defines new (and only new) variable with the specified name,
		 * initialises it with the specified value and returns the result as
		 * follows: If the variable does not exist then the return value is
		 * equal to the initial value of a new variable. If the variable already
		 * exists, then the return value is the VariableScope.VARIABLE_EXISTS
		 * instance.
		 */
		public Value define(String variableName, Value newValue)
		{
			Variable variable = create(variableName, newValue);
			if (null == variable)
				return VariableScope.VARIABLE_EXISTS;
			return variable;
		}

		/**
		 * Assigns value to existing variable with the specified name. If the
		 * variable exists then the assigned value is returned (e. g. to allow
		 * chain assigning). If the variable does not exist, it is not created
		 * automatically. Attempts to assigning value to non-existing variable
		 * will result in returning VariableScope.UNKNOWN_VARIABLE instance.
		 */
		public Value assign(String variableName, Value newValue)
		{
			Variable variable = list.get(variableName);
			if (null == variable)
				return VariableScope.UNKNOWN_VARIABLE;
			variable.copy(newValue);
			return variable;
		}

		/**
		 * Assigns value to a variable with the specified name and returns the
		 * assigned value to allow chain assigning. If the variable does
		 * not exist, it is created automatically. (However, this method is
		 * not used directly by the expression parser. So any attempt to use
		 * non-existing variable in the parsed expression(s) will lead to
		 * Value.TypeOrError.INVALID_ASSIGNMENT error state.)
		 */
		public Value defineOrAssign(String variableName, Value newValue)
		{
			Variable variable = getOrCreate(variableName);
			variable.copy(newValue);
			return variable;
		}

		/**
		 * Registers existing variable created by user. The name of the specified
		 * variable must be unique. No variable with the same name may exist
		 * within this variable scope. The result of this method is as
		 * follows: if the variable name is unique, then the return value is
		 * equal to its value; if there already is a variable with the same
		 * name in this variable scope, then the return value is the
		 * VariableScope.VARIABLE_EXISTS instance (which contains the
		 * typeOrError value: Value.TypeOrError.VARIABLE_EXISTS).
		 */
		public Value register(Variable variable)
		{
			if (list.containsKey(variable.getName()))
				return VariableScope.VARIABLE_EXISTS;
			list.put(variable.getName(), variable);
			return variable;
		}


		/**
		 * Gets value of the specified variable. If the variable does not exist,
		 * the VariableScope.UNKNOWN_VARIABLE static instance is returned.
		 */
		public Value getValue(String variableName)
		{
			Variable variable = list.get(variableName);
			if (null == variable)
				return VariableScope.UNKNOWN_VARIABLE;
			return variable;
		}
	}


	//////////////////////////////////////////////////////////////////////
	// Node nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class creates the binary tree structure. Each node refers to its
	 * parent and may contain left and right subnode.
	 */
	public static class Node implements ValueProvider
	{
		/**
		 * This enumeration type holds the possible classes of (types) nodes.
		 */
		public static enum Class
		{
			///////////////////////////////////////////////////////////////////
			// Summary of values of this enumeration type:
			// =============================================================
			//
			//	NOT_CLASSIFIED      // undefined type
			//	LITERAL             // numeric or string literal (value)
			//	IDENTIFIER          // string indentifier of a variable or
			//	                    // namespace
			//	VARIABLE            // the variable
			//	OPERATOR            // common operator
			//	FUNCTION            // one of standard functions
			//	PARENTH_OPERATOR    // root of expression within parentheses
			//	BRACKET_OPERATOR    // root of expression within brackets
			//	TYPE_OR_ERROR       // type or error

			// Undefined type
			NOT_CLASSIFIED
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					// It is not necessary to bother by copying of “nothing”.
					// throw new RuntimeException("Undefined type");
				}

				public Value getValue(Node node)
				{
					return null;
				}

				public String getString(Node node)
				{
					return "" + null;
				}

				public String toString(Node node)
				{
					return "{not classified}";
				}
			},

			// Numeric or string literal (value)
			LITERAL
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.literal = nodeSource.literal;
				}

				public Value getValue(Node node)
				{
					return node.literal.getValue();
				}

				public String getString(Node node)
				{
					return node.literal.getString();
				}

				public String toString(Node node)
				{
					return node.getValue().toString();
				}
			},

			// String indentifier of a variable or namespace
			IDENTIFIER
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.identifier = nodeSource.identifier;
				}

				public Value getValue(Node node)
				{
					ExpressionProcessor processor = node.identifier.processor;
					String id = node.identifier.identifier;
					Variable findVariable = null;

					if (processor.localVariables != null)
						findVariable = processor.localVariables.get(id);

					if (null == findVariable &&
						null != processor.instanceVariables)
						findVariable = processor.instanceVariables.get(id);
					if (null == findVariable &&
						null != processor.superclassVariables)
						findVariable = processor.superclassVariables.get(id);

					if (null == findVariable)
						findVariable = globalVariables.get(id);

					if (null == findVariable)
					{
						if (processor.useCustoms)
						{
							Value value = processor.customVariable(id, null);
							if (null == value) value = new
								Value(Value.TypeOrError.UNKNOWN_VALUE);
							return value;
						}
						return new Value(Value.TypeOrError.
							UNKNOWN_VARIABLE_COMP, node.location);
					}
					else
						return findVariable;
				}

				public String getString(Node node)
				{
					ExpressionProcessor processor = node.identifier.processor;
					String id = node.identifier.identifier;
					Variable findVariable = null;

					if (processor.localVariables != null)
						findVariable = processor.localVariables.get(id);

					if (null == findVariable &&
						null != processor.instanceVariables)
						findVariable = processor.instanceVariables.get(id);
					if (null == findVariable &&
						null != processor.superclassVariables)
						findVariable = processor.superclassVariables.get(id);

					if (null == findVariable)
						findVariable = globalVariables.get(id);

					if (null == findVariable)
					{
						if (processor.useCustoms)
						{
							Value value = processor.customVariable(id, null);
							if (null == value) value = new
								Value(Value.TypeOrError.UNKNOWN_VALUE);
							return value.getString();
						}
						return new Value(Value.TypeOrError.
							UNKNOWN_VARIABLE_COMP, node.location).getString();
					}
					else
						return findVariable.getString();
				}

				public String toString(Node node)
				{
					return node.identifier.identifier;
				}
			},

			// The variable
			VARIABLE
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.variable = nodeSource.variable;
				}

				public Value getValue(Node node)
				{
					return node.variable;
				}

				public String getString(Node node)
				{
					return node.variable.getString();
				}

				public String toString(Node node)
				{
					return node.variable.getName();
				}
			},

			// Common operator
			OPERATOR
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.operator = nodeSource.operator;
				}

				public Value getValue(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value;
					return new Value(Value.TypeOrError.
						UNKNOWN_OPERATOR, node.location);
				}

				public String getString(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value.getString();
					return new Value(Value.TypeOrError.
						UNKNOWN_OPERATOR, node.location).getString();
				}

				public String toString(Node node)
				{
					return node.operator.string;
				}
			},

			// One of standard functions or custom function
			FUNCTION
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.function = nodeSource.function;
					nodeTarget.identifier = nodeSource.identifier;
				}

				public Value getValue(Node node)
				{
					if (null == node.function)
					{
						if (null != node.identifier)
						{
							ExpressionProcessor processor =
								node.identifier.processor;

							if (processor.useCustoms)
							{
								Value value = node.right.getValue();
								if (value.isError()) return value;

								value = processor.customFunction(
									node.identifier.identifier, value);
								if (null == value) value = new
									Value(Value.TypeOrError.UNKNOWN_VALUE);
								return value;
							}
						}

						return new Value(Value.TypeOrError.
							UNKNOWN_FUNCTION, node.location);
					}

					Value value = node.function.getValue(node);
					if (null != value) return value;
					return new Value(Value.TypeOrError.
						UNKNOWN_FUNCTION, node.location);
				}

				public String getString(Node node)
				{
					if (null == node.function)
					{
						if (null != node.identifier)
						{
							ExpressionProcessor processor =
								node.identifier.processor;

							if (processor.useCustoms)
							{
								Value value = node.right.getValue();
								if (value.isError()) return value.getString();

								value = processor.customFunction(
									node.identifier.identifier, value);
								if (null == value) value = new
									Value(Value.TypeOrError.UNKNOWN_VALUE);
								return value.getString();
							}
						}

						return new Value(Value.TypeOrError.
							UNKNOWN_FUNCTION, node.location).getString();
					}

					Value value = node.function.getValue(node);
					if (null != value) return value.getString();
					return new Value(Value.TypeOrError.UNKNOWN_FUNCTION,
						node.location).getString();
				}

				public String toString(Node node)
				{
					if (null == node.function)
					{
						// The useCustom is not static, but this shouldn’t
						// happen if the useCustom is not in use…
						if (null == node.identifier) // || !useCustoms)
							return "unkFunc";
						return node.identifier.identifier;
					}

					return node.function.name;
				}
			},

			// Root of expression within parentheses
			PARENTH_OPERATOR
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.operator = nodeSource.operator;
				}

				public Value getValue(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value;
					return new Value(Value.TypeOrError.UNKNOWN_OPERATOR,
						node.location);
				}

				public String getString(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value.getString();
					return new Value(Value.TypeOrError.UNKNOWN_OPERATOR,
						node.location).getString();
				}

				public String toString(Node node)
				{
					return "(" + node.operator.string + ")";
				}
			},

			// Root of expression within brackets
			BRACKET_OPERATOR
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					nodeTarget.operator = nodeSource.operator;
				}

				public Value getValue(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value;
					return new Value(Value.TypeOrError.UNKNOWN_OPERATOR,
						node.location);
				}

				public String getString(Node node)
				{
					Value value = node.operator.getValue(node);
					if (null != value) return value.getString();
					return new Value(Value.TypeOrError.UNKNOWN_OPERATOR,
						node.location).getString();
				}

				public String toString(Node node)
				{
					return "(" + node.operator.string + ")";
				}
			},

			// Type or error
			TYPE_OR_ERROR
			{
				public void copy(Node nodeTarget, Node nodeSource)
				{
					if (null == nodeTarget.typeOrError)
						nodeTarget.typeOrError = new Value(
							nodeSource.typeOrError);
					else
						nodeTarget.typeOrError.copy(nodeSource.typeOrError);
				}

				public Value getValue(Node node)
				{
					return node.typeOrError;
				}

				public String getString(Node node)
				{
					return node.typeOrError.getString();
				}

				public String toString(Node node)
				{
					return node.getValue().toString();
				}
			};

			/**
			 * This abstract method is used by Node.copy(Node) method.
			 * This method abstract and therefore it must be properly
			 * defined in each instance of this enumeration type. This
			 * principle is used in more enumeration types of this class.
			 */
			abstract public void copy(Node nodeTarget, Node nodeSource);

			/**
			 * This abstract method is used by Node.getValue() method.
			 * Each instance handles this request by its own way.
			 */
			abstract public Value getValue(Node node);

			/**
			 * This abstract method is used by Node.getString() method.
			 * Each instance handles this request by its own way.
			 */
			abstract public String getString(Node node);

			/**
			 * This abstract method is used by Node.toString() method.
			 * Each instance handles this request by its own way.
			 * See the description of Node.toString() method.
			 */
			abstract public String toString(Node node);
		}

		/**
		 * This enumeration type provides list of supported operators. Each
		 * operator is binary. Unary operators are implemented as functions,
		 * but the syntaxes for unary minus (-) and logical not (!) are
		 * supported and they are internally solved as converting to functions
		 * (unary plus is ignored).
		 * Operators LEFPAR and RIGPAR are not a real operators, just special
		 * states used by the parser. There is no ternary operator
		 * implemented.
		 * Full list of operators and functions is in the introduction
		 * (in the source code) of appropriate enumeration class.
		 */
		public static enum Operator
		{
			///////////////////////////////////////////////////////////////////
			// Summary of values of this enumeration type:
			// =============================================================
			//
			//	SEPARATOR   // ,   – list separator
			//	SELECTOR    // []  – list item selector (special operator)
			//	ASSIGN      // =   – assign value (to a variable)
			//
			//	OR          // ||  – logical or
			//	AND         // &&  – logical and
			//
			//	NEQ         // !=  – not equal to…
			//	EQ          // ==  – equal to…
			//	GTEQ        // >=  – greater than or equal to…
			//	GT          // >   – greater than…
			//	LTEQ        // <=  – less than or equal to…
			//	LT          // <   – less than…
			//
			//	MIN         // min – minimum of…
			//	MAX         // max – maximum of…
			//
			//	SUB         // -   – subtraction
			//	ADD         // +   – addition
			//	MOD         // %   – modulus
			//	DIV         // /   – division
			//	MUL         // *   – multiplication
			//	POW         // ^   – power of…
			//	NAMSPC      // ::  – namespace resolver
			//
			//	LEFBRA      // left bracket ‘[’ – list item selector start
			//	RIGBRA      // right bracket ‘]’ – list item selector end
			//
			//	LEFPAR      // left parenthesis ‘(’
			//	RIGPAR      // right parenthesis ‘)’


			// Definitions of prioritized operators
			// (only binary operators are defined):

			// List separator
			SEPARATOR((byte)0, ",")
			{
				public Value getValue(Node node)
				{
					Value c = new Value(node.left.getValue());
					c.add(node.right.getValue());
					return c;
				}
			},

			// List item selector (special operator)
			SELECTOR((byte)15, "[]")
			{
				public Value getValue(Node node)
				{
					return node.left.getValue().
						getValue(node.right.getValue());
				}
			},

			// Assign value (to a variable)
			ASSIGN((byte)1, "=")
			{
				public Value getValue(Node node)
				{
					return node.left.assign(node.right);
				}
			},

			// Logical or
			OR((byte)2, "||")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(0 != a.get() ||
							0 != b.get() ? 1.0 : 0.0);

					return advancedOr(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Logical and
			AND((byte)3, "&&")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(0 != a.get() &&
							0 != b.get() ? 1.0 : 0.0);

					return advancedAnd(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Not equal to…
			NEQ((byte)4, "!=")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().equals(
							node.right.getString()) ? 0.0 : 1.0);
					if (a.isList())
						return new Value(a.list.equals(
							node.right.getValue().list) ? 0.0 : 1.0);

					if (b.isString())
						return new Value(a.getString().equals(
							b.getString()) ? 0.0 : 1.0);
					if (b.isList())
						return new Value(a.list.equals(
							b.list) ? 0.0 : 1.0);

					return new Value(a.get() != b.get() ? 1.0 : 0.0);
				}
			},

			// Equal to…
			EQ((byte)4, "==")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().equals(
							node.right.getString()) ? 1.0 : 0.0);
					if (a.isList())
						return new Value(a.list.equals(
							node.right.getValue().list) ? 1.0 : 0.0);

					if (b.isString())
						return new Value(a.getString().equals(
							b.getString()) ? 1.0 : 0.0);
					if (b.isList())
						return new Value(a.list.equals(
							b.list) ? 1.0 : 0.0);

					return new Value(a.get() == b.get() ? 1.0 : 0.0);
				}
			},

			// Greater than or equal to…
			GTEQ((byte)5, ">=")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().compareTo(
							node.right.getString()) >= 0 ? 1.0 : 0.0);

					if (b.isString())
						return new Value(a.getString().compareTo(
							b.getString()) >= 0 ? 1.0 : 0.0);

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() >= b.get() ? 1.0 : 0.0);

					return advancedGtEq(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Greater than…
			GT((byte)5, ">")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().compareTo(
							node.right.getString()) > 0 ? 1.0 : 0.0);

					if (b.isString())
						return new Value(a.getString().compareTo(
							b.getString()) > 0 ? 1.0 : 0.0);

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() > b.get() ? 1.0 : 0.0);

					return advancedGt(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Less than or equal to…
			LTEQ((byte)5, "<=")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().compareTo(
							node.right.getString()) <= 0 ? 1.0 : 0.0);

					if (b.isString())
						return new Value(a.getString().compareTo(
							b.getString()) <= 0 ? 1.0 : 0.0);

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() <= b.get() ? 1.0 : 0.0);

					return advancedLtEq(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Less than…
			LT((byte)5, "<")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString().compareTo(
							node.right.getString()) < 0 ? 1.0 : 0.0);

					if (b.isString())
						return new Value(a.getString().compareTo(
							b.getString()) < 0 ? 1.0 : 0.0);

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() < b.get() ? 1.0 : 0.0);

					return advancedLt(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Minimum of…
			MIN((byte)6, "min")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(
							a.getString().compareTo(b.getString()) < 0 ?
							a.getString() : b.getString());

					if (b.isString())
						return new Value(
							a.getString().compareTo(b.getString()) < 0 ?
							a.getString() : b.getString());

					if (a.isNumber() && b.isNumber())
						return new Value(min(a.get(), b.get()));

					return advancedMin(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Maximum of…
			MAX((byte)7, "max")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(
							a.getString().compareTo(b.getString()) > 0 ?
							a.getString() : b.getString());

					if (b.isString())
						return new Value(
							a.getString().compareTo(b.getString()) > 0 ?
							a.getString() : b.getString());

					if (a.isNumber() && b.isNumber())
						return new Value(max(a.get(), b.get()));

					return advancedMax(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Subtraction
			SUB((byte)8, "-")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() - b.get());

					return advancedSub(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Addition
			ADD((byte)9, "+")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isString())
						return new Value(a.getString() + b.getString());

					if (a.isList())
					{
						Value c = new Value(a);
						c.add(node.right.getValue());
						return c;
					}

					if (b.isString())
						return new Value(a.getString() + b.getString());

					if (b.isList())
					{
						Value c = new Value(a);
						c.add(b);
						return c;
					}

					return new Value(a.get() + b.get());
				}
			},

			// Modulus
			MOD((byte)10, "%")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() % b.get());

					return advancedMod(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Division
			DIV((byte)12, "/") // Fix: was previously 11: less than the MUL…
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
					{
						if (0 == b.get())
							return new Value(Value.
								TypeOrError.DIVISION_BY_ZERO);
						return new Value(a.get() / b.get());
					}

					return advancedDiv(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Multiplication
			MUL((byte)12, "*")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(a.get() * b.get());

					return advancedMul(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Power of…
			POW((byte)13, "^")
			{
				public Value getValue(Node node)
				{
					Value a = node.left.getValue();
					if (a.isError()) return a;

					Value b = node.right.getValue();
					if (b.isError()) return b;

					if (a.isNumber() && b.isNumber())
						return new Value(pow(a.get(), b.get()));

					return advancedPow(a, b); // UNSUPPORTED_OPERATION
				}
			},

			// Namespace resolver
			NAMSPC((byte)14, "::")
			{
				// Signal helper class – this class transmits information
				// about error value that should be returned by this
				// operator.
				class NamespaceError extends Throwable
				{
					private final static long serialVersionUID = -1L;

					public final Value.TypeOrError type;
					public final long errorLocation;

					public NamespaceError(Value.TypeOrError type,
						long errorLocation)
					{
						this.type = type;
						this.errorLocation = errorLocation;
					}
				}

				// Helper method – search namespace in “branches” – deeper
				// in the subtree of current node.
				private VariableScope getBranchNamespace(Node node)
					throws NamespaceError
				{
					VariableScope scope;

					// Try to get scope from left node…
					if (node.left.checkNodeClass(Node.Class.OPERATOR) &&
						node.left.checkOperator(Node.Operator.NAMSPC))
					{
						scope = getBranchNamespace(node.left);
					}
					else
					{
						scope = getRootNamespace(node.left);
					}

					if (node.right.checkNodeClass(Node.Class.IDENTIFIER))
					{
						// …try to get scope from right node and return it.
						VariableScope scope2 = scope.getNamespace(
							node.right.identifier.identifier);

						if (null == scope2)
							throw new NamespaceError(
								Value.TypeOrError.UNKNOWN_NAMESPACE,
								node.right.location);
						else
							return scope2;
					}
					else throw new NamespaceError(
						Value.TypeOrError.MISSING_IDENTIFIER,
						node.right.location);
				}

				// Helper method – search through root of all namespaces.
				private VariableScope getRootNamespace(Node node)
					throws NamespaceError
				{
					if (node.checkNodeClass(Node.Class.IDENTIFIER))
					{
						ExpressionProcessor processor =
							node.identifier.processor;
						String identifier = node.identifier.identifier;
						VariableScope scope;

						// Resolve reserved words:
						if (identifier.equals("global") ||
							identifier.equals("general"))
							scope = globalVariables;
						else if (identifier.equals("super") ||
							identifier.equals("parent"))
							scope = processor.superclassVariables;
						else if (identifier.equals("this") ||
							identifier.equals("self"))
							scope = processor.instanceVariables;
						else
							// Get scope by identifier string:
							scope = ExpressionProcessor.
								getNamespace(identifier);

						if (null == scope)
							throw new NamespaceError(
								Value.TypeOrError.UNKNOWN_NAMESPACE,
								node.location);

						return scope;
					}
					else throw new NamespaceError(
						Value.TypeOrError.MISSING_IDENTIFIER,
						node.location);
				}

				public Value getValue(Node node)
				{
					try
					{
						VariableScope scope;

						if (node.left.checkNodeClass(Node.Class.OPERATOR) &&
							node.left.checkOperator(Node.Operator.NAMSPC))
						{
							scope = getBranchNamespace(node.left);
						}
						else
						{
							scope = getRootNamespace(node.left);
						}

						if (node.right.checkNodeClass(Node.Class.IDENTIFIER))
						{
							Variable variable = scope.get(
								node.right.identifier.identifier);

							if (null == variable)
								throw new NamespaceError(
									Value.TypeOrError.UNKNOWN_VARIABLE_COMP,
									node.right.location);
							else
								return variable;
						}
						else throw new NamespaceError(
							Value.TypeOrError.MISSING_IDENTIFIER,
							node.right.location);
					}
					catch (NamespaceError nse)
					{
						return new Value(nse.type, nse.errorLocation);
					}
				}
			},

			// Left bracket ‘[’ – list item selector start
			LEFBRA((byte)16, "[")
			{
				public Value getValue(Node node)
				{
					return null;
				}
			},

			// Right bracket ‘]’ – list item selector end
			RIGBRA((byte)16, "]")
			{
				public Value getValue(Node node)
				{
					return null;
				}
			},

			// Left parenthesis ‘(’
			LEFPAR((byte)16, "(")
			{
				public Value getValue(Node node)
				{
					return null;
				}
			},

			// Right parenthesis ‘)’
			RIGPAR((byte)16, ")")
			{
				public Value getValue(Node node)
				{
					return null;
				}
			};

			/** This is the operator’s priority. */
			public final byte priority;

			/** This is the string representation of the operator. */
			public final String string;

			// The default constructor.
			private Operator(byte priority, String string)
			{
				this.priority = priority;
				this.string = string;
			}

			/**
			 * This abstract method is used by Node.getValue() method.
			 * Each instance handles this request by its own way.
			 */
			abstract public Value getValue(Node node);
		}

		/**
		 * This enumeration type provides list of supported (default)
		 * functions. Only the default functions receiving only one parameter
		 * are implemented. All the default functions that require two
		 * parameters (div, max, min, mod, pow) are implemented as binary
		 * operators using infix syntax (e. g. a min b). All such “functions”
		 * except min and max have also the symbolic representation (e. g.:
		 * pow – ^, mod – %, or – ||, and – &&). According that the div
		 * operator is not integer division like in Pascal programming
		 * language. It is an alias for / operator.
		 * In addition, several default functions have one or more aliases
		 * defined.
		 * Full list of operators and functions is in the introduction
		 * (in the source code) of appropriate enumeration class.
		 */
		public static enum Function
		{
			///////////////////////////////////////////////////////////////////
			// Summary of values of this enumeration type:
			// =============================================================
			//
			//	Note: All functions are able to process single element list.
			//	      This situation equals to entering single value to the
			//	      function.
			//
			//	ABS     // abs
			//	ACOS    // acos, arccos
			//	ASIN    // asin, arcsin
			//	ATAN    // atan, atg, atn, arctan, arctg, arctn
			//	CBRT    // cbrt – cube root
			//	CEIL    // ceil
			//	COS     // cos
			//	COSH    // cosh, coshyp
			//	DEG     // deg – toDegrees
			//	(DIV)   // implemented as operator; a div b (also a / b)
			//	EXP     // exp
			//	FLOOR   // floor
			//	INT     // int, integer – uses conversion (double)(long)
			//	               or converts string to integer
			//	LN      // ln – uses function log
			//	LOG     // log – uses function log10
			//	(MAX)   // implemented as operator; a max b
			//	(MIN)   // implemented as operator; a min b
			//	(MOD)   // implemented as operator; a mod b (also a % b)
			//	NEG     // neg – negative value (unary minus; also -a)
			//	NUM     // num, number – keeps number or converts string
			//	               to number
			//	(POW)   // implemented as operator; a pow b, a ^ b
			//	RAD     // rad – toRadians
			//	RAND    // rand, rnd
			//	SIG     // sig, signum – signum
			//	SIN     // sin
			//	SINH    // sinh, sinhyp
			//	SQR     // sqr – square – power of 2
			//	SQRT    // sqrt – square root
			//	TAN     // tan, tg, tn
			//	TANH    // tanh, tgh, tnh, tanhyp, tghyp, tnhyp
			//	---
			//	(OR)    // implemented as operator; a or b (also a || b)
			//	(AND)   // implemented as operator; a and b (also a && b)
			//	NOT     // logical not operator (also !a)


			// Built-in functions (only functions that receive exactly one
			// argument are implemented):


			// abs
			ABS("abs")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(abs(value.get()));
				}
			},

			// acos, arccos
			ACOS("acos")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(acos(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// asin, arcsin
			ASIN("asin")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(asin(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// atan, atg, atn, arctan, arctg, arctn
			ATAN("atan")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(atan(value.get()));
				}
			},

			// cbrt – cube root
			CBRT("cbrt")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(cbrt(value.get()));
				}
			},

			// ceil
			CEIL("ceil")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(ceil(value.get()));
				}
			},

			// cos
			COS("cos")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(cos(value.get()));
				}
			},

			// cosh, coshyp
			COSH("cosh")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(cosh(value.get()));
				}
			},

			// deg – toDegrees
			DEG("deg")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(toDegrees(value.get()));
				}
			},

			// exp
			EXP("exp")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(exp(value.get()));
				}
			},

			// floor
			FLOOR("floor")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(floor(value.get()));
				}
			},

			// int, integer – uses conversion (double)(long) or converts string
			INT("int")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString())
					{
						try
						{
							return new Value((double)(long)Double.
								parseDouble(value.getString()));
						}
						catch (Exception e)
						{
							return new Value(
								Value.TypeOrError.INVALID_ARGUMENT);
						}
					}
					return new Value((double)(long)value.get());
				}
			},

			// ln – uses function log
			LN("ln")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(log(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// log – uses function log10
			LOG("log")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(log10(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// neg – negative value (unary minus)
			NEG("neg")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(-value.get());
				}
			},

			// num, number – keeps number or converts string to number
			NUM("num")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString())
					{
						try
						{
							return new Value(Double.
								parseDouble(value.getString()));
						}
						catch (Exception e)
						{
							return new Value(
								Value.TypeOrError.INVALID_ARGUMENT);
						}
					}
					return new Value(value.get());
				}
			},

			// rad, radians – toRadians
			RAD("rad")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(toRadians(value.get()));
				}
			},

			// rand, rnd
			RAND("rand")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList())
					{
						// TODO: consider the getTwoElements
						value = getSingleElement(value);
						if (value.isError())
						{
							if (value.getError() ==
								Value.TypeOrError.MISSING_ARGUMENT)
								return new Value(random());
							return value;
						}
					}
					else if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(random() * value.get());
				}
			},

			// sig – signum
			SIG("sig")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(signum(value.get()));
				}
			},

			// sin
			SIN("sin")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(sin(value.get()));
				}
			},

			// sinh, sinhyp
			SINH("sinh")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(sinh(value.get()));
				}
			},

			// sqr – square – power of 2
			SQR("sqr")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(value.get() * value.get());
				}
			},

			// sqrt – square root
			SQRT("sqrt")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(sqrt(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// tan, tg, tn
			TAN("tan")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(tan(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// tanh, tgh, tnh, tanhyp, tghyp, tnhyp
			TANH("tanh")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return new Value(tanh(value.get()),
						Value.TypeOrError.ARGUMENT_DOMAIN_ERROR,
						node.location);
				}
			},

			// logical not operator
			NOT("not")
			{
				public Value getValue(Node node)
				{
					Value value = node.right.getValue();
					if (value.isList()) value = getSingleElement(value);
					if (value.isError()) return value;
					if (value.isString()) return new Value(
						Value.TypeOrError.INVALID_ARGUMENT);
					return (0.0 != value.get()) ?
						new Value(0.0) : new Value(1.0);
				}
			};

			/**
			 * This is the string representation of the function
			 * (the function name).
			 */
			public final String name;

			// The default constructor.
			private Function(String name)
			{
				this.name = name;
			}

			// This method MUST BE used on list only! If the list contains
			// single element, it returns it as value (unless the element is
			// a list in what case it returns
			// Value.TypeOrError.TOO_MANY_ARGUMENTS error state). If the
			// list contains more elements, it returns
			// Value.TypeOrError.TOO_MANY_ARGUMENTS error state. If the list
			// is empty, it returns Value.TypeOrError.MISSING_ARGUMENT error
			// state.
			private static Value getSingleElement(Value value)
			{
				int size = value.size();

				if (1 == size)
				{
					if (value.elementExists(0))
					{
						value = value.getValue(0);
						if (value.isList())
							return new Value(Value.TypeOrError.
								TOO_MANY_ARGUMENTS);
						return value;
					}

					Literal[] indexes = value.getIndexes();
					value = value.getValue(indexes[0]);
					if (value.isList())
						return new Value(Value.TypeOrError.
							TOO_MANY_ARGUMENTS);
					return value;
				}

				if (1 < size) return new Value(
					Value.TypeOrError.TOO_MANY_ARGUMENTS);

				return new Value(Value.TypeOrError.MISSING_ARGUMENT);
			}

			/**
			 * This abstract method is used by Node.getValue() method.
			 * Each instance handles this request by its own way.
			 */
			abstract public Value getValue(Node node);
		}


		// Following attributes create the tree structure:
		private Node parent;
		private Node left;
		private Node right;

		// Each node refers to specific location in parsed string
		private long location;

		// The class determines which of next few objects is truly used
		private Class nodeClass;

		// Only one of the following objects is in use in one time:
		private Literal    literal;
		private Identifier identifier;
		private Variable   variable;
		private Operator   operator;
		private Function   function;
		private Value      typeOrError;


		// This private method resets the node. It is used in constructors.
		private void resetNode()
		{
			parent = null;
			left = null;
			right = null;

			location = 0;

			nodeClass = Node.Class.NOT_CLASSIFIED;

			literal = null;
			variable = null;
			operator = null;
			function = null;
			typeOrError = null;
		}


		//////////////////
		// Constructors

		/**
		 * Creates empty unclassified node.
		 */
		public Node()
		{
			resetNode();
		}

		/**
		 * Creates a new unclassified with the specified location set.
		 */
		public Node(long location)
		{
			resetNode();
			this.location = location;
		}

		/**
		 * Creates node with the numeric literal value that was found at
		 * specified location.
		 */
		public Node(double value, long location)
		{
			resetNode();
			this.location = location;
			setLiteral(value);
		}

		/**
		 * Creates node with the string literal value that was found at
		 * specified location.
		 */
		public Node(String string, long location)
		{
			resetNode();
			this.location = location;
			setLiteral(string);
		}


		/**
		 * Creates node with the literal value set that was found at specified
		 * location. Note that the Value object may also hold an error state.
		 */
		public Node(Value value, long location)
		{
			resetNode();
			this.location = location;
			setLiteral(value);
		}

		/**
		 * Creates node that is holding an identifier found at specified
		 * location. Note that the Value object may also hold an error state.
		 */
		public Node(Identifier identifier, long location)
		{
			resetNode();
			this.location = location;
			setIdentifier(identifier);
		}

		/**
		 * Creates node containing the variable which was found at specified
		 * location.
		 */
		public Node(Variable variable, long location)
		{
			resetNode();
			this.location = location;
			setVariable(variable);
		}


		/**
		 * Creates node containing the operator that was found at specified
		 * location.
		 */
		public Node(Operator operator, long location)
		{
			resetNode();
			this.location = location;
			setOperator(operator);
		}

		/**
		 * Creates node containing the function that was found at specified
		 * location.
		 */
		public Node(Function function, long location)
		{
			resetNode();
			this.location = location;
			setFunction(function);
		}


		/**
		 * Creates node holding error state which occured at specified
		 * location.
		 */
		public Node(Value.TypeOrError error, long location)
		{
			resetNode();
			this.location = location;
			setError(error);
		}

		/**
		 * Copy constructor. Copies everything from source instance to this
		 * instance (one to one).
		 */
		public Node(Node nodeSource)
		{
			resetNode();
			copy(nodeSource);
		}


		/**
		 * This method copies the content from source node. It follows the
		 * situation. For example it will copy the variable reference, but
		 * creates a new literal object. (The same way is also used by copy
		 * constructor.)
		 */
		public void copy(Node nodeSource)
		{
			nodeSource.nodeClass.copy(this, nodeSource);
			nodeClass = nodeSource.nodeClass;
			location = nodeSource.location;
		}


		/** Recursively clears this node. */
		public void clear()
		{
			if (null != left) left.clear();
			if (null != right) right.clear();
			resetNode();
		}


		private Value getElementToAssign()
		{
			Value list;

			if (left.checkNodeClass(Node.Class.OPERATOR))
				list = left.getElementToAssign();
			else
				list = left.getValue();

			Value index = right.getValue();
			if (!list.elementExists(index))
				list.set(index, 0);

			return list.getValue(index);
		}

		/**
		 * Assigns new value to a variable that is represented by this node. If
		 * this node is not a variable the result is Value instance containing
		 * Value.TypeOrError.INVALID_ASSIGNMENT error state.
		 */
		public Value assign(Value newValue)
		{
			if (checkNodeClass(Node.Class.VARIABLE))
			{
				variable.copy(newValue);
				return variable;
			}

			if (checkNodeClass(Node.Class.IDENTIFIER))
			{
				Value value = Node.Class.IDENTIFIER.getValue(this);
				if (value instanceof Variable)
				{
					((Variable)value).copy(newValue);
					return value;
				}
				else
				{
					ExpressionProcessor processor = identifier.processor;
					if (processor.useCustoms)
					{
						value = processor.customVariable(
							identifier.identifier, newValue);
						if (null == value) value = new
							Value(Value.TypeOrError.UNKNOWN_VALUE);
						return value;
					}
				}
			}
			else if (checkNodeClass(Node.Class.OPERATOR))
			{
				if (checkOperator(Node.Operator.SELECTOR))
				{
					// Gets or creates the array/list element:
					Value element = getElementToAssign();
					element.copy(newValue);
					return element;
				}
			}

			return new Value(Value.TypeOrError.INVALID_ASSIGNMENT, location);
		}

		/**
		 * Assigns new value to a variable that is represented by this node. If
		 * this node is not a variable the result is Value instance containing
		 * Value.TypeOrError.INVALID_ASSIGNMENT error state. The value to
		 * assign is computed from specified node.
		 */
		public Value assign(Node newValue)
		{
			return assign(newValue.getValue());
		}


		/**
		 * Computes this node’s value – evaluates the subtree.
		 * (This is part of implementation the ValueProvider interface.)
		 */
		public Value getValue()
		{
			Value value = nodeClass.getValue(this);
			if (null != value) return value;
			return new Value(Value.TypeOrError.INVALID_EXPRESSION, location);
		}

		/**
		 * Gets the location of current node – that means where this node was
		 * found during the parsing process.
		 */
		public long getLocation() { return location; }

		/**
		 * Returns the error location of the current node or zero. Zero must
		 * not mean that this node contains no errors.
		 */
		public long getErrorLocation()
		{
			if (checkNodeClass(Node.Class.TYPE_OR_ERROR))
				return getValue().getErrorLocation();
			else
				return 0;
		}


		////////////////////////////////////////////////////////////////////
		// Basic working with left and right subnode and with parent node

		/**
		 * This static method clears the parent of the specified node and cleans
		 * up the tree structure. This is also helping method for insertion
		 * of nodes.
		 */
		public static void clearParent(Node toClear)
		{
			if (null != toClear.parent)
			{
				if (toClear.parent.left == toClear)
					toClear.parent.left = null;
				if (toClear.parent.right == toClear)
					toClear.parent.right = null;
			}
		}

		/**
		 * Puts specified node as left subnode (which must be empty) of this
		 * node.
		 */
		public boolean putLeft(Node pLeft)
		{
			// Left node must not exist
			if (null != left) return false;

			// Nothing happens
			if (null == pLeft) return true;

			clearParent(pLeft);
			left = pLeft;
			pLeft.parent = this;

			return true;
		}

		/**
		 * Puts specified node as right subnode (which must be empty) of this
		 * node.
		 */
		public boolean putRight(Node pRight)
		{
			// Right node must not exist
			if (null != right) return false;

			// Nothing happens
			if (null == pRight) return true;

			clearParent(pRight);
			right = pRight;
			pRight.parent = this;

			return true;
		}


		/**
		 * Checks if this node has a left subnode and returns true if it has.
		 */
		public boolean hasLeft() { return null != left; }

		/**
		 * Checks if this node has a right subnode and returns true if it has.
		 */
		public boolean hasRight() { return null != right; }

		/**
		 * Checks if this node has a parent and returns true if it has.
		 */
		public boolean hasParent() { return null != parent; }


		/**
		 * Checks if left subnode of this node is empty and returns true
		 * if it is.
		 */
		public boolean noLeft() { return null == left; }

		/**
		 * Checks if right subnode of this node is empty and returns true
		 * if it is.
		 */
		public boolean noRight() { return null == right; }

		/**
		 * Checks if this node has no parent and returns true if it does
		 * not have the parent.
		 */
		public boolean noParent() { return null == parent; }


		/** Gets the left subnode of this node. */
		public Node getLeft() { return left; }

		/** Gets the right subnode of this node. */
		public Node getRight() { return right; }

		/** Gets the parent of this node. */
		public Node getParent() { return parent; }


		////////////////////////////////
		// Insert the new nodes into tree

		/**
		 * Inserts the new node into tree replacing the position of old node.
		 * The tree structure will be reconnected and the old node becomes
		 * the right subnode of a new node.
		 */
		public static boolean insert(Node oldNode, Node newNode)
		{
			if (null == oldNode || null == newNode) return false;

			// The new node have to be “fresh” (newly created and clear)
			if (null != newNode.right || null != newNode.parent)
				return false;

			// If the parent is null, the action is simple:
			if (null == oldNode.parent)
			{
				newNode.right = oldNode;
				oldNode.parent = newNode;
				return true;
			}
			else
			{
				// If the old node is in the right part of it’s parent
				if (oldNode.parent.right == oldNode)
				{
					newNode.parent = oldNode.parent;
					newNode.right = oldNode;
					newNode.parent.right = newNode;
					oldNode.parent = newNode;
					return true;
				}

				// If the old node is in the left part of it’s parent
				if (oldNode.parent.left == oldNode)
				{
					newNode.parent = oldNode.parent;
					newNode.right = oldNode;
					newNode.parent.left = newNode;
					oldNode.parent = newNode;
					return true;
				}

				return false;
			}
		}

		/**
		 * Puts the new node as right subnode of old node if it is empty at the
		 * time. If the right subnode of old node exists at the time, it will
		 * be saved to left subnode (of old node) which must be empty at the
		 * time.
		 */
		public static boolean putNode(Node oldNode, Node newNode)
		{
			if (null == oldNode || null == newNode) return false;

			// The new node must not be a root node
			if (null != newNode.parent) return false;

			// Only nodes of specific classes can recieve this new node:
			switch (oldNode.nodeClass)
			{
			case FUNCTION:
				if (null != oldNode.left ||
					null != oldNode.right) return false;

				return oldNode.putRight(newNode);

			case OPERATOR:
				if (null != oldNode.left &&
					null != oldNode.right) return false;

				if (!oldNode.putLeft(oldNode.right))
					return false;

				return oldNode.putRight(newNode);
			}

			return false;
		}


		//////////////////////////////////////////
		// Set different types/classes of nodes

		/**
		 * Sets this node to “not classified”.
		 */
		public void setNotClasified()
		{
			nodeClass = Node.Class.NOT_CLASSIFIED;
		}

		/** Sets this node to contain numeric literal value. */
		public void setLiteral(double value)
		{
			nodeClass = Node.Class.LITERAL;
			literal = new Literal(value);
		}

		/** Sets this node to contain string literal value. */
		public void setLiteral(String string)
		{
			nodeClass = Node.Class.LITERAL;
			literal = new Literal(string);
		}

		/**
		 * Sets this node to contain literal value. Either string or numeric –
		 * according to entered value instance.
		 */
		public void setLiteral(Value value)
		{
			if (value.isString())
			{
				nodeClass = Node.Class.LITERAL;
				literal = new Literal(value.getString());
			}
			else if (value.isError())
			{
				nodeClass = Node.Class.TYPE_OR_ERROR;
				if (null == typeOrError)
					// Create a new value
					typeOrError = new Value(value);
				else
					// Overwrite old value
					typeOrError.copy(value);
			}
			else
			{
				nodeClass = Node.Class.LITERAL;
				literal = new Literal(value);
			}
		}

		/** Sets this node to contain empty list. */
		public void setList()
		{
			// TODO: consider to create Node.Class.LIST
			nodeClass = Node.Class.TYPE_OR_ERROR;

			// Create a new numeric value…
			if (null == this.typeOrError)
				this.typeOrError = new Value(Double.NaN);

			// Convert it to empty list
			this.typeOrError.clear();
		}

		/**
		 * Sets this node to contain list of values. The entered value must
		 * contain list, otherwise this node will be set to INVALID_LIST
		 * error state.
		 */
		public void setList(Value value)
		{
			setList();
			this.typeOrError.add(value);
		}

		/**
		 * Sets this node to contain list of values containing at least one
		 * double value.
		 */
		public void setList(Double firstValue, Double... otherValues)
		{
			setList();
			if (null != firstValue)
				this.typeOrError.add(new Value(firstValue));
			for (Double nextValue : otherValues)
				if (null != nextValue)
					this.typeOrError.add(new Value(nextValue));
		}

		/**
		 * Sets this node to contain list of values containing at least one
		 * string value.
		 */
		public void setList(String firstValue, String... otherValues)
		{
			setList();
			if (null != firstValue)
				this.typeOrError.add(new Value(firstValue));
			for (String nextValue : otherValues)
				if (null != nextValue)
					this.typeOrError.add(new Value(nextValue));
		}

		/** Sets this node to contain identifier. */
		public void setIdentifier(Identifier identifier)
		{
			nodeClass = Node.Class.IDENTIFIER;
			this.identifier = identifier;
		}

		/** Sets this node to contain variable reference. */
		public void setVariable(Variable variable)
		{
			nodeClass = Node.Class.VARIABLE;
			this.variable = variable;
		}

		/** Sets this node to contain (specified) function. */
		public void setFunction(Function function)
		{
			nodeClass = Node.Class.FUNCTION;
			this.function = function;
		}

		/**
		 * Changes this node to contain custom function – the identifier
		 * must be set in advance.
		 */
		public void changeToCustomFunction() throws ReparseError
		{
			if (null == this.identifier)
				throw new ReparseError(Value.TypeOrError.
					INVALID_CUSTOM_FUNCTION);
			nodeClass = Node.Class.FUNCTION;
		}

		/** Sets this node to contain other (specified) operator. */
		public void setOperator(Operator operator)
		{
			nodeClass = Node.Class.OPERATOR;
			this.operator = operator;
		}

		/** Sets or resets the parenthesis flag. */
		public void setParenthesis(boolean bSet)
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				nodeClass = bSet ? Node.Class.PARENTH_OPERATOR :
					Node.Class.OPERATOR;
		}

		/** Sets the parenthesis flag. */
		public void setParenthesis()
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				nodeClass = Node.Class.PARENTH_OPERATOR;
		}

		/** Sets or resets the bracket flag. */
		public void setBracket(boolean bSet)
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				nodeClass = bSet ? Node.Class.BRACKET_OPERATOR :
					Node.Class.OPERATOR;
		}

		/** Sets the bracket flag. */
		public void setBracket()
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				nodeClass = Node.Class.BRACKET_OPERATOR;
		}

		/** Sets this node to contain specified error state. */
		public void setError(Value.TypeOrError error)
		{
			nodeClass = Node.Class.TYPE_OR_ERROR;

			if (null == this.typeOrError)
				// Create a new error
				this.typeOrError = new Value(error, location);
			else
				// Overwrite old error
				this.typeOrError.setError(error, location);
		}

		/**
		 * Sets this node to contain specified error state with the
		 * specification of the location where the error arises.
		 */
		public void setError(Value.TypeOrError error, long errorLocation)
		{
			nodeClass = Node.Class.TYPE_OR_ERROR;

			if (null == this.typeOrError)
				// Create a new error
				this.typeOrError = new Value(error, errorLocation);
			else
				// Overwrite old error
				this.typeOrError.setError(error, errorLocation);
		}


		////////////////////////////////////////////////////////////
		// Get the node classes and apropriate objects from nodes

		/** Gets the class of this node. */
		public Node.Class getNodeClass()
		{
			return nodeClass;
		}

		/** Check if the class of this node is of the specified type. */
		public boolean checkNodeClass(Node.Class nodeClass)
		{
			if (Node.Class.OPERATOR == nodeClass)
				return Node.Class.OPERATOR == this.nodeClass ||
					Node.Class.PARENTH_OPERATOR == this.nodeClass ||
					Node.Class.BRACKET_OPERATOR == this.nodeClass;
			return nodeClass == this.nodeClass;
		}

		/** Gets the operator instance of this node. */
		public Operator getOperator()
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				return operator;
			return null;
		}

		/** Check if this node represents specified operator. */
		public boolean checkOperator(Operator check)
		{
			if (Node.Class.OPERATOR == nodeClass ||
				Node.Class.PARENTH_OPERATOR == nodeClass ||
				Node.Class.BRACKET_OPERATOR == nodeClass)
				return check == operator;
			return false;
		}

		/** Gets the function instance of this node. */
		public Function getFunction()
		{
			if (Node.Class.FUNCTION == nodeClass)
				return function;
			return null;
		}

		/** Check if this node represents specified function. */
		public boolean checkFunction(Function check)
		{
			if (Node.Class.FUNCTION == nodeClass)
				return check == function;
			return false;
		}


		/**
		 * Finds and returns the root node of the specified node (if it is
		 * possible). If specified node does not exist (is null) the return
		 * value is (also) null.
		 */
		public static Node findRoot(Node find)
		{
			if (null == find) return null;
			while (find.hasParent())
				find = find.getParent();
			return find;
		}


		/**
		 * Returns a string value of this object.
		 * (This is part of implementation the ValueProvider interface.)
		 */
		public String getString()
		{
			return nodeClass.getString(this);
		}

		/**
		 * Converts this node to string form. Each instance of Node.Class
		 * enumeration implements toString(Node) method. Errors show the
		 * error text. Literals show their value. Variables show their name.
		 * Functions and operators show their string form (function name,
		 * operator name or symbol). Parenthesized operator is returned as
		 * operator string enclosed in [].
		 */
		@Override public String toString()
		{
			return nodeClass.toString(this);
		}

		public String dump()
		{
			StringBuffer sb = new StringBuffer();
			if (null == parent) sb.append("no parent"); else
			{
				sb.append("parent ");
				sb.append(parent.toString());
			}
			sb.append(" –> ");
			sb.append(toString());
			if (null != left && null != right)
			{
				sb.append("{");
				sb.append(left.toString());
				sb.append("; ");
				sb.append(right.toString());
				sb.append("}");
			}
			else if (null != right)
			{
				sb.append("{");
				sb.append(right.toString());
				if (right.hasLeft() || right.hasRight()) sb.append("…");
				sb.append("}");
			}
			else if (null != left)
			{
				// This situation should not happen.
				sb.append("{left ");
				sb.append(left.toString());
				if (left.hasLeft() || left.hasRight()) sb.append("…");
				sb.append("}");
			}
			return sb.toString();
		}

		public String dumpRecursive()
		{
			StringBuffer sb = new StringBuffer();
			sb.append(toString());
			if (null != left && null != right)
			{
				sb.append("{");
				sb.append(left.dumpRecursive());
				sb.append("; ");
				sb.append(right.dumpRecursive());
				sb.append("}");
			}
			else if (null != right)
			{
				sb.append("{");
				sb.append(right.dumpRecursive());
				sb.append("}");
			}
			else if (null != left)
			{
				// This situation should not happen.
				sb.append("{left ");
				sb.append(left.dumpRecursive());
				sb.append("}");
			}
			return sb.toString();
		}
	}


	//////////////////////////////////////////////////////////////////////
	// BinaryTree nested class
	//////////////////////////////////////////////////////////////////////

	/**
	 * This class stores the binary tree processed by the parser. This tree
	 * is the benefit of this group of classes. It stores the parsed state
	 * until next string is attached to the processor’s instance. The
	 * processing of the tree (evaluation of parsed expression) is very
	 * fast. That means that frequent changing (re-attaching) the string
	 * expression to the ExpressionProcessor instance is not effective.
	 */
	public static class BinaryTree
	{
		// The root node
		private Node root = null;

		// Current/selected node
		private Node currentNode = null;


		/**
		 * Checks the state of existence of this tree. A tree is considered
		 * as “existing” when it has a valid root node.
		 */
		public boolean treeExists()
		{
			return null != root;
		}

		/**
		 * Checks if any node is selected as the current node. The tree may
		 * exist and no selected node may be active.
		 */
		public boolean selectionExists()
		{
			return null != currentNode;
		}

		/**
		 * Checks if the entered node is part of this binary tree.
		 */
		public boolean isOfThisTree(Node check)
		{
			if (null == check) return false;
			while (check.hasParent())
				check = check.getParent();
			return getRoot() == check;
		}


		/**
		 * Sets specified node as the root node. If the root/tree has
		 * already existed, it is cleared. New root node is selected as
		 * the current node.
		 */
		public boolean setRoot(Node newRoot)
		{
			if (null != root) clear();
			if (null == newRoot) return false;
			currentNode = root = newRoot;
			return true;
		}

		/** Gets the root node of this binary tree. */
		public Node getRoot()
		{
			if (null == root) return null;
			while (root.hasParent())
				root = root.getParent();
			return root;
		}


		/**
		 * Selects root node as the current node if it exists. Otherwise the
		 * return value is false.
		 */
		public boolean goRoot()
		{
			if (null == root) return false;
			currentNode = getRoot();
			return true;
		}

		/**
		 * Moves to the left subnode of current node if it exists. Otherwise
		 * the return value is false.
		 */
		public boolean goLeft()
		{
			if (null == currentNode || !currentNode.hasLeft()) return false;
			currentNode = currentNode.getLeft();
			return true;
		}

		/**
		 * Moves to the right subnode of current node if it exists. Otherwise
		 * the return value is false.
		 */
		public boolean goRight()
		{
			if (null == currentNode || !currentNode.hasRight()) return false;
			currentNode = currentNode.getRight();
			return true;
		}

		/**
		 * Moves to the parent node of current node (if it exists). In case
		 * of failure the return value is false.
		 */
		public boolean goBack()
		{
			if (null == currentNode || !currentNode.hasParent()) return false;
			currentNode = currentNode.getParent();
			return true;
		}


		/** Recursively clears the tree. */
		public void clear()
		{
			if (null != root)
			{
				while (root.hasParent())
					root = root.getParent();
				root.clear();
				root = null;
			}
			else if (null != currentNode)
			{
				while (currentNode.hasParent())
					currentNode = currentNode.getParent();
				currentNode.clear();
			}
			currentNode = null;
		}


		/**
		 * Selects specified node as the current node. If entered node is
		 * null, current node is deselected. Entered node should not be from
		 * another tree, however no checking is made if the entered node
		 * actually is from this binary tree.
		 */
		public void setCurrent(Node current) { currentNode = current; }

		/** Gets the current node of this binary tree. */
		public Node getCurrent() { return currentNode; }
	}


	/**
	 * This is the list of global variables. This list is final that means
	 * it cannot be replaced with another list of variables. It should be
	 * uniqe list of variables. That means that the programmer who uses this
	 * library should ensure that this list is not equal to another list
	 * of (local or instance) variables.
	 */
	public final static VariableScope globalVariables = new VariableScope();


	//////////////////////////////////////////////////////////////////////
	// Central functionality, declarations and definitions
	//////////////////////////////////////////////////////////////////////

	// {
		/**
		 * This instance is returned by the getValue method when the parser
		 * is still in initial state or an empty string is attached to it.
		 */
		public final static Value NO_EXPRESSION =
			new Value(Value.TypeOrError.NO_EXPRESSION, 0);

		/**
		 * This instance is returned by the getValue method when the parse
		 * error occurs.
		 */
		public final static Value PARSE_ERROR =
			new Value(Value.TypeOrError.PARSE_ERROR, 0);


		// This instance will contain detailed dump report of parsing process.
		// It is cleared by initParsing method and updated by addDump method.
		private final StringBuffer dump = new StringBuffer();

		// This is the value of current indentation level of the dump lines.
		private int dumpIndent = 0;

		// True value means that the dumping of parsing process is enabled.
		private boolean dumpEnabled = false;

		// True value means that the parser superclass overrides the
		// customFunction and customVariable methods. This means that
		// the parser should not create parsing errors when unknown
		// function or variable is found.
		private boolean useCustoms = false;

		// True value means that the parser calls the joinIdentifiers(id1,
		// id2) method to join two consecutive identifiers.
		private boolean joinIdentifiers = false;

		// True value means that this instance should handle some basic
		// object-based functionality.
		private boolean useNamespaces = false;


		/** ExpressionProcessor instance states. */
		public static enum State
		{
			INITIALIZED,	// The tree is just initialised – still empty.
			ATTACHED,		// The string to be parsed has been attached, but
							// still not parsed.
			PARSED,			// The string is parsed (in the tree).
			PARSE_ERROR,	// A parse error has arisen.
		}


		/**
		 * This list is used only when useNamespaces is set to true.
		 * It is the list of superclass variables accessible for this
		 * expression. This list may be (and by default is) empty. The
		 * programmer who is using this library may put here a list containing
		 * variables that are accessible from the superclass of currently
		 * handled instance by this expression. This list will be then
		 * processed before the list of global variables.
		 */
		public VariableScope superclassVariables = null;


		/**
		 * This list is used only when useNamespaces is set to true.
		 * It is the list of instance variables accessible for this
		 * expression. This list may be (and by default is) empty. The
		 * programmer who is using this library may put here a list containing
		 * instance variables accessible for this expression during evaluation
		 * process. This list will be then processed before the list of
		 * superclass and global variables.
		 */
		public VariableScope instanceVariables = null;


		/**
		 * This is the list of local variables. This list may be (and
		 * by default is) empty. The programmer who is using this library
		 * may put here a list containing local variables accessible for
		 * this expression. (Method parameters are also considered to be the
		 * local variables.) This list will be then processed before the
		 * list of global, superclass and instance variables.
		 */
		public VariableScope localVariables = null;


		// Binary tree instance – this structure stores the expression’s
		// parsed state.
		private final BinaryTree tree = new BinaryTree();


		// String to be parsed.
		private String stringToParse = null;

		// Current state of this expression instance.
		private State state = State.INITIALIZED;


		/**
		 * Gets the root node of this expression’s binary tree (if the tree
		 * exists) or null (if it does not exist).
		 */
		public Node getRoot()
		{
			if (null != tree)
				return tree.getRoot();
			return null;
		}


		/** Attaches string to parse. */
		@SuppressWarnings("fallthrough")
		public boolean attachString(String stringToParse)
		{
			switch (state)
			{
			case PARSED:
			case PARSE_ERROR:
				tree.clear();

				// The fallthrough here is intentional –
				// do not put a break here‼

			case INITIALIZED:
				this.stringToParse = stringToParse;
				state = State.ATTACHED;
				return true;

			default:
				return false;
			}
		}


		/**
		 * Parses attached string. This method does not evaluate parsed
		 * string. It just recognizes the syntax and stores it into internal
		 * binary tree structure. (Parsing is performed automatically by the
		 * getValue method, but this method can be invoked to prepare the
		 * instance for evaluating.)
		 */
		public boolean parse()
		{
			if (state == State.PARSED)	// already parsed
				return true;

			if (state != State.ATTACHED) return false;	// status not changed

			if (stringToParse.isEmpty())	// change status: parse error
			{
				tree.clear();
				if (tree.setRoot(new Node(0)))
				{
					tree.getRoot().setError(
						Value.TypeOrError.MISSING_EXPRESSION);
					state = State.PARSE_ERROR;
				}
				return false;
			}

			initParsing(); // currentLocation = 0;

			if (!tree.setRoot(reparseTree()) ||
				tree.getRoot().checkNodeClass(
					Node.Class.TYPE_OR_ERROR))
			{
				// note: this does not necessarily mean the error
				// it might be also a list (but, in fact, list is not
				// a standard numeric result)
				closeParsing();
				state = State.PARSE_ERROR;
				return false;
			}

			closeParsing();
			state = State.PARSED;
			return true;
		}


		/**
		 * Calculates (evaluates) the parsed expression (or parses it if
		 * necessary) and gets computed value (or other type, or error state).
		 * (This is part of implementation the ValueProvider interface.)
		 */
		public Value getValue()
		{
			if (state == State.INITIALIZED) return NO_EXPRESSION;

			if (state == State.ATTACHED) parse();

			if (null == tree.getRoot()) return PARSE_ERROR;

			return tree.getRoot().getValue();
		}

		/**
		 * Gets the string attached to this instance – it is the string to be
		 * parsed. If the instance is in initial state null is returned.
		 */
		public String getAttachedString()
		{
			if (state == State.INITIALIZED) return null;
			return stringToParse;
		}

		/** Returns true if this instance is in error state. */
		public boolean hasParseErrors()
		{ return state == State.PARSE_ERROR; }

		/** Returns true if this instance is correctly parsed. */
		public boolean isParsed()
		{
			return state == State.PARSED;
		}


		// String reader created for parsing purposes.
		private StringReader reader = null;

		// String buffer used during parsing process.
		private final StringBuffer buffer = new StringBuffer();

		// Current position in string reader.
		private long currentLocation = 0;

		// Position of mark in string reader.
		private long markLocation = 0;

		// Flag indicating that the string reader is empty
		private boolean readerHasCharacters = true;

		// Last read character.
		private char lastChar = (char)0;


		// Helper method that is initializing the parsing process.
		// Initialization creates a new string reader that is used to get
		// characters for parsing one by one.
		private boolean initParsing()
		{
			dump.setLength(0);
			dumpIndent = 0;
			closeParsing();
			if (null != stringToParse)
			{
				reader = new StringReader(stringToParse);
				currentLocation = markLocation = 0;
				readerHasCharacters = true;
				lastChar = (char)0;
				return true;
			}
			return false;
		}

		// This helper method reads next character from the string reader
		// created in initParsing method.
		private boolean nextChar()
		{
			try
			{
				int data = reader.read();
				if (-1 != data)
				{
					lastChar = (char)data;
					++currentLocation;
					return true;
				}
				else readerHasCharacters = false;
			}
			catch (Exception e) {}
			return false;
		}

		// Sometimes it is necessary to step back by one character during the
		// parsing process. This helper method ensures returning the stream
		// by one character if it is possible.
		private boolean stepBack()
		{
			try
			{
				if (currentLocation > markLocation)
				{
					reader.reset();
					long skip = currentLocation - markLocation - 1;
					long skipped = reader.skip(skip);
					currentLocation = markLocation + skipped;
					readerHasCharacters = true;
					return true;
				}
			}
			catch (Exception e) {}
			return false;
		}

		// Marking the stream of string reader is important in several
		// situations during the parsing process. Without this possibility
		// it would not be possible to point on right place in case of
		// erroneous input or even the parsing of syntactically correct
		// string could fail.
		private boolean markReader()
		{
			try
			{
				markLocation = currentLocation;
				reader.mark(0);
			}
			catch (Exception e) {}
			return false;
		}

		// This helper method returns the stream and values of helper
		// variables to the place marked by markReader method.
		private boolean resetReader()
		{
			try
			{
				if (currentLocation != markLocation)
				{
					readerHasCharacters = true;
					currentLocation = markLocation;
				}
				reader.reset();
				return true;
			}
			catch (Exception e) {}
			return false;
		}

		// This helper method closes the parsing process.
		private boolean closeParsing()
		{
			try
			{
				if (null != reader)
				{
					reader.close();
					reader = null;
					return true;
				}
			}
			catch (Exception e) {}
			return false;
		}


		// This is the main helper method of the whole background of the
		// parsing process. It creates next node during the process. This
		// method is called several times in the reparseTree method.
		private Node parseNextNode()
		{
			Node tempNode = null;
			boolean notEndOfString = true;

			markReader();
			if (!nextChar())
			{
				// NO! return new Node(Value.TypeOrError.MISSING_EXPRESSION,
				// 	currentLocation); NO!
				return null;
			}

			// All characters between 0 and 32 are either illegal or
			// whitespace, so they will be ignored…
			if ((lastChar >= (char)0 && lastChar <= ' ') ||
				Character.isWhitespace(lastChar))
			{
				do
				{
					if (!nextChar()) return null;
				}
				while ((lastChar >= (char)0 && lastChar <= ' ') ||
					Character.isWhitespace(lastChar));

				stepBack();
				markReader();
				nextChar();
			}

			// Parse numbers:
			if (Character.isDigit(lastChar) || lastChar == '.')
			{
				char lastLastChar;
				buffer.setLength(0);
				do
				{
					lastLastChar = lastChar;
					buffer.append(lastChar);
					notEndOfString = nextChar();
				}
				while (notEndOfString && (
					Character.isDigit(lastChar) || lastChar == '.' ||
					(lastChar >= 'a' && lastChar <= 'e') || lastChar == 'x' ||
					(lastChar >= 'A' && lastChar <= 'E') || lastChar == 'X' ||
					((lastChar == '+' || lastChar == '-') &&
						(lastLastChar == 'E' || lastLastChar == 'e'))));

				if (notEndOfString) stepBack();

				try
				{
					double value = Double.parseDouble(buffer.toString());
					return new Node(value, markLocation);
				}
				catch (Exception e)
				{
					return new Node(Value.TypeOrError.INVALID_NUMERIC_LITERAL,
						markLocation + 1);
				}
			}

			// Parse strings:
			if (lastChar == '"')
			{
				buffer.setLength(0);
				while (notEndOfString = nextChar())
				{
					if (lastChar == '"')
					{
						if (notEndOfString = nextChar())
						{
							if (lastChar != '"')
							{
								stepBack();
								break;
							}
							// continue…
						}
						else
						{
							notEndOfString = true;
							break;
						}
					}
					buffer.append(lastChar);
				}

				if (!notEndOfString)
					return new Node(Value.TypeOrError.INVALID_STRING_LITERAL,
						markLocation + 1);

				return new Node(buffer.toString(), markLocation);
			}

			// Parse identifiers:
			if (Character.isJavaIdentifierStart(lastChar))
			{
				buffer.setLength(0);
				do
				{
					buffer.append(lastChar);
					notEndOfString = nextChar();
				}
				while (notEndOfString &&
					Character.isJavaIdentifierPart(lastChar));

				if (notEndOfString) stepBack();

				String value = buffer.toString();
				addDump("Java identifier: “" + value + "”");

				if (value.equals("abs"))
					return new Node(Node.Function.ABS, markLocation);

				if (value.equals("acos") || value.equals("arccos"))
					return new Node(Node.Function.ACOS, markLocation);

				if (value.equals("asin") || value.equals("arcsin"))
					return new Node(Node.Function.ASIN, markLocation);

				if (value.equals("atan") || value.equals("atg") ||
					value.equals("atn") || value.equals("arctan") ||
					value.equals("arctg") || value.equals("arctn"))
					return new Node(Node.Function.ATAN, markLocation);

				if (value.equals("cbrt"))
					return new Node(Node.Function.CBRT, markLocation);

				if (value.equals("ceil"))
					return new Node(Node.Function.CEIL, markLocation);

				if (value.equals("cos"))
					return new Node(Node.Function.COS, markLocation);

				if (value.equals("cosh") || value.equals("coshyp"))
					return new Node(Node.Function.COSH, markLocation);

				if (value.equals("div"))
					return new Node(Node.Operator.DIV, markLocation);

				if (value.equals("deg") || value.equals("degrees"))
					return new Node(Node.Function.DEG, markLocation);

				if (value.equals("exp"))
					return new Node(Node.Function.EXP, markLocation);

				if (value.equals("floor"))
					return new Node(Node.Function.FLOOR, markLocation);

				if (value.equals("int") || value.equals("integer"))
					return new Node(Node.Function.INT, markLocation);

				if (value.equals("ln"))
					return new Node(Node.Function.LN, markLocation);

				if (value.equals("log"))
					return new Node(Node.Function.LOG, markLocation);

				if (value.equals("max"))
					return new Node(Node.Operator.MAX, markLocation);

				if (value.equals("min"))
					return new Node(Node.Operator.MIN, markLocation);

				if (value.equals("mod"))
					return new Node(Node.Operator.MOD, markLocation);

				if (value.equals("neg"))
					return new Node(Node.Function.NEG, markLocation);

				if (value.equals("num") || value.equals("number"))
					return new Node(Node.Function.NUM, markLocation);

				if (value.equals("pow"))
					return new Node(Node.Operator.POW, markLocation);

				if (value.equals("rad") || value.equals("radians"))
					return new Node(Node.Function.RAD, markLocation);

				if (value.equals("rand") || value.equals("rnd") ||
					value.equals("random"))
					return new Node(Node.Function.RAND, markLocation);

				if (value.equals("sig") || value.equals("signum"))
					return new Node(Node.Function.SIG, markLocation);

				if (value.equals("sin"))
					return new Node(Node.Function.SIN, markLocation);

				if (value.equals("sinh") || value.equals("sinhyp"))
					return new Node(Node.Function.SINH, markLocation);

				if (value.equals("sqr"))
					return new Node(Node.Function.SQR, markLocation);

				if (value.equals("sqrt"))
					return new Node(Node.Function.SQRT, markLocation);

				if (value.equals("tan") || value.equals("tg") ||
					value.equals("tn"))
					return new Node(Node.Function.TAN, markLocation);

				if (value.equals("tanh") || value.equals("tgh") ||
					value.equals("tnh") || value.equals("tanhyp") ||
					value.equals("tghyp") || value.equals("tnhyp"))
					return new Node(Node.Function.TANH, markLocation);

				if (value.equals("or"))
					return new Node(Node.Operator.OR, markLocation);

				if (value.equals("and"))
					return new Node(Node.Operator.AND, markLocation);

				if (value.equals("not"))
					return new Node(Node.Function.NOT, markLocation);

				// Find variable or save indetifiers

				if (useNamespaces)
				{
					return new Node(new Identifier(this, value), markLocation);
				}
				else
				{
					Variable findVariable = null;

					if (localVariables != null)
						findVariable = localVariables.get(value);
					if (null == findVariable)
						findVariable = globalVariables.get(value);

					if (null == findVariable)
						return new Node(Value.TypeOrError.UNKNOWN_VARIABLE_PARS,
							markLocation + 1);
					else
						return new Node(findVariable, markLocation);
				}
			}

			// Parse all other characters:

			if (lastChar == '(')
				return new Node(Node.Operator.LEFPAR, markLocation);

			if (lastChar == ')')
				return new Node(Node.Operator.RIGPAR, markLocation);

			if (lastChar == '[')
				return new Node(Node.Operator.LEFBRA, markLocation);

			if (lastChar == ']')
				return new Node(Node.Operator.RIGBRA, markLocation);

			if (lastChar == ',')
				return new Node(Node.Operator.SEPARATOR, markLocation);

			if (lastChar == '-')
				return new Node(Node.Operator.SUB, markLocation);

			if (lastChar == '+')
				return new Node(Node.Operator.ADD, markLocation);

			if (lastChar == '%')
				return new Node(Node.Operator.MOD, markLocation);

			if (lastChar == '/')
				return new Node(Node.Operator.DIV, markLocation);

			if (lastChar == '*')
				return new Node(Node.Operator.MUL, markLocation);

			if (lastChar == '^')
				return new Node(Node.Operator.POW, markLocation);

			if (lastChar == ':')
			{
				if (!nextChar() || lastChar != ':')
					return new Node(Value.TypeOrError.INVALID_OPERATOR,
						markLocation - 1);
				return new Node(Node.Operator.NAMSPC, markLocation);
			}

			if (lastChar == '=')
			{
				markReader();
				if (nextChar() && lastChar == '=')
					return new Node(Node.Operator.EQ, markLocation);
				resetReader();
				return new Node(Node.Operator.ASSIGN, markLocation);
			}

			if (lastChar == '!')
			{
				markReader();
				if (nextChar() && lastChar == '=')
					return new Node(Node.Operator.NEQ, markLocation);
				resetReader();
				return new Node(Node.Function.NOT, markLocation);
			}

			if (lastChar == '<')
			{
				markReader();
				if (nextChar())
				{
					if (lastChar == '>')
						return new Node(Node.Operator.NEQ, markLocation);
					if (lastChar == '=')
						return new Node(Node.Operator.LTEQ, markLocation);
				}
				resetReader();
				return new Node(Node.Operator.LT, markLocation);
			}

			if (lastChar == '>')
			{
				markReader();
				if (nextChar())
				{
					if (lastChar == '=')
						return new Node(Node.Operator.GTEQ, markLocation);
				}
				resetReader();
				return new Node(Node.Operator.GT, markLocation);
			}

			if (lastChar == '&')
			{
				if (!nextChar() || lastChar != '&')
					return new Node(Value.TypeOrError.INVALID_OPERATOR,
						markLocation - 1);
				return new Node(Node.Operator.AND, markLocation);
			}

			if (lastChar == '|')
			{
				if (!nextChar() || lastChar != '|')
					return new Node(Value.TypeOrError.INVALID_OPERATOR,
						markLocation - 1);
				return new Node(Node.Operator.OR, markLocation);
			}

			return new Node(Value.TypeOrError.INVALID_CHARACTER, markLocation + 1);
		}


		// This is the core of the whole background of the parsing process.
		// This method (re)creates the binary tree (recursively).
		@SuppressWarnings("fallthrough")
		private Node reparseTree()
		{
			Node workNode = null;	// Current node
			Node newNode = null;	// New node

			// Note: comments in following section contain references to graph
			// that served well in preparation process in 2003 (it was during
			// programming the version in C/C++). The graph was not preserved.

			try
			{
				// Initialization
				addDump("Starting the new processing by the " +
					"reparseTree method.");
				indentDump();
				workNode = parseNextNode();

				// Check if incoming operator is unary plus or minus
				if (null != workNode &&
					workNode.checkNodeClass(Node.Class.OPERATOR) &&
					(workNode.checkOperator(Node.Operator.SUB) ||
					 workNode.checkOperator(Node.Operator.ADD)))
				{
					if (workNode.checkOperator(Node.Operator.SUB))
					// Create ‘neg’ function (same as unary minus)
					{
						addDump("Unary operator detected: -");
						workNode = null;
						workNode = new Node(Node.Function.NEG,
							currentLocation);
					}
					else
					// Ignore unary plus
					{
						addDump("Unary operator detected: +");
						workNode = null;
						workNode = parseNextNode();
					}
				}

				if (null == workNode) throw new ReparseBreak();

				addDump("First node: ", workNode);

				switch (workNode.getNodeClass())
				{
				case TYPE_OR_ERROR:
					// TODO – check if the list shouldn’t be handled here
					throw new ReparseBreak();

				case OPERATOR:

					// First node must NOT be an operator
					// (except ‘(’ operator).
					if (!workNode.checkOperator(Node.Operator.LEFPAR))
					{
						/*
						CHANGED – creates empty list anyway… All functions
							are able to handle it…

						// (another exception is ‘)’ – empty arguments list
						// when useCustom is in use)
						if (useCustoms && workNode.checkOperator(
							Node.Operator.RIGPAR))
						{
							addDump("Dismissing the new node. (Hopefully, " +
								"this is the custom function – there " +
								"is no way to check it.)");
							addDump("Searching for and returning the root.");
							while (workNode.hasParent())
								workNode = workNode.getParent();
							workNode.setParenthesis();
							throw new ReparseBreak();
						}
						throw new ReparseError(
							Value.TypeOrError.MISSING_OPERAND);
						*/

						addDump("Creating an empty list.");
						workNode = new Node();
						workNode.setList();
						throw new ReparseBreak();
					}

					workNode = null;
					workNode = reparseTree();

					// If error arises skip to end, otherwise continue
					// parsing…
					if (workNode.checkNodeClass(Node.Class.TYPE_OR_ERROR))
						throw new ReparseBreak();

				// Intentional fallthrough switch structure – do not put
				// break here…

				case LITERAL:
				case IDENTIFIER:
				case VARIABLE:
				case FUNCTION:

					// Standard parsing after initialization
					while (readerHasCharacters)
					{
						newNode = parseNextNode();

						if (null != newNode &&
							// check if the incoming operator
							// is unary plus or minus
							newNode.checkNodeClass(Node.Class.OPERATOR) &&
							(newNode.checkOperator(Node.Operator.SUB) ||
							 newNode.checkOperator(Node.Operator.ADD)) &&
							// if current node is either ‘operator
							// with missing operand’ or ‘function
							// without argument’
							(
								(
									workNode.checkNodeClass(
										Node.Class.OPERATOR)
									&&
									(workNode.noLeft() ||
										workNode.noRight())
								)
								||
								(
									workNode.checkNodeClass(
										Node.Class.FUNCTION)
									&&
									workNode.noRight()
								)
							))
						{
							if (newNode.checkOperator(Node.Operator.SUB))
							// create ‘neg’ function (same as unary minus)
							{
								addDump("Unary operator detected: -");
								newNode = null;
								newNode = new Node(
									Node.Function.NEG,
									currentLocation);
							}
							else
							// ignore unary plus
							{
								addDump("Unary operator detected: +");
								newNode = null;
								newNode = parseNextNode();
							}
						}

						addDump("Continuous status:");
						addDump(" *** Current node: ",
							workNode.dumpRecursive());

						if (null != newNode)
						{
							addDump(" *** New node: ",
								newNode.dumpRecursive());
							if (joinIdentifiers && newNode.checkNodeClass(
								Node.Class.IDENTIFIER) && workNode.
								checkNodeClass(Node.Class.IDENTIFIER))
							{
								addDump("Joining two identifiers.");
								workNode.setIdentifier(joinIdentifiers(
									workNode.identifier, newNode.identifier));
								newNode = null;
							}
							else switch (newNode.getNodeClass())
							{
							case LITERAL:			// new – A
							case IDENTIFIER:
							case VARIABLE:			// new – C
								switch (workNode.getNodeClass())
								{
								// Put variable, identifier or literal after
								// variable, identifier, literal, parentheses
								// or brackets:
								case LITERAL:			// cur. – A
								case IDENTIFIER:
								case VARIABLE:			// cur. – C
								case PARENTH_OPERATOR:	// cur. – G
								case BRACKET_OPERATOR:
									throw new ReparseError(
										Value.TypeOrError.OPERATOR_EXPECTED);

								// Put variable, identifier or literal after
								// function:
								case FUNCTION:			// cur. – B
									addDump("Putting a new node into " +
										"the tree.");
									if (!Node.putNode(workNode, newNode))
										throw new ReparseError(
											Value.TypeOrError.ODD_PARAMETER);
									addDump("Returning to the next " +
										"non-function node.");
									newNode = null;
									while (workNode.checkNodeClass(
										Node.Class.FUNCTION) &&
										workNode.hasParent())
										workNode = workNode.getParent();
									break;

								// Put variable, identifier or literal after
								// operator:
								case OPERATOR:			// cur. – F +(D, E)
									// Convert substitute operator to unary
									// minus when before literal
									if (newNode.checkNodeClass(
											Node.Class.LITERAL) &&
										workNode.checkNodeClass(
											Node.Class.OPERATOR) &&
										workNode.checkOperator(
											Node.Operator.SUB))
									{
										addDump("Unary operator detected: -");
										workNode.setOperator(
											Node.Operator.ADD);
										newNode.setLiteral(
											-newNode.getValue().get());
									}

									// Here was checking of valid assignment.
									// Now it is on another place.

									// Put node
									addDump("Putting a new node into " +
										"the tree.");
									if (!Node.putNode(workNode, newNode))
										throw new ReparseError(
											Value.TypeOrError.ODD_OPERAND);
									newNode = null;
									break;
								}
								break;

							case FUNCTION:			// new – B
								switch (workNode.getNodeClass())
								{
								// Put function after literal, variable,
								// parentheses or brackets:
								case LITERAL:			// cur. – A
								case IDENTIFIER:
								case VARIABLE:			// cur. – C
								case PARENTH_OPERATOR:	// cur. – G
								case BRACKET_OPERATOR:
									throw new ReparseError(
										Value.TypeOrError.
										ILLEGAL_FUNCTION_CALL);

								// Put function after function or operator:
								case FUNCTION:			// cur. – B
								case OPERATOR:			// cur. – F +(D, E)
									addDump("Putting a new node into " +
										"the tree.");
									if (!Node.putNode(workNode, newNode))
										throw new ReparseError(
											Value.TypeOrError.
											ILLEGAL_FUNCTION_CALL);
									addDump("Activating the new node.");
									workNode = newNode;	// activate node
									newNode = null;
									break;
								}
								break;

							case OPERATOR:
								switch (newNode.getOperator())
								{
								case RIGPAR:	// new – E
									switch (workNode.getNodeClass())
									{
									case LITERAL:
									case IDENTIFIER:
									case VARIABLE:
										if (workNode.hasParent())
											throw new ReparseError(
												Value.TypeOrError.PARSE_ERROR);
										break;
									case FUNCTION:
										if (workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_PARAMETER);
										break;
									case OPERATOR:
									case PARENTH_OPERATOR:
									case BRACKET_OPERATOR:
										if (workNode.noLeft() ||
											workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_OPERAND);
										break;
									}

									// In this case dismissing of a newNode is
									// possible, because it was not used:
									addDump("Dismissing the new node.");
									addDump("Searching for and returning " +
										"the root.");
									newNode = null;
									while (workNode.hasParent())
										workNode = workNode.getParent();
									workNode.setParenthesis();
									throw new ReparseBreak();

								case LEFPAR:	// new – D
									// current node have to be – B, F
									if (!workNode.checkNodeClass(
											Node.Class.FUNCTION) &&
										!workNode.checkNodeClass(
											Node.Class.OPERATOR))
									{
										if (useCustoms &&
											workNode.checkNodeClass(
												Node.Class.IDENTIFIER))
										{
											addDump("Changing to " +
												"a custom function.");

											// Change to custom function
											workNode.changeToCustomFunction();
										}
										else throw new ReparseError(
											Value.TypeOrError.
											OPERATOR_EXPECTED);
									}

									// In this case dismissing of a newNode is
									// possible, because it was not used:
									addDump("Dismissing the new node.");
									addDump("Creating a sub-tree…");

									newNode = null;
									newNode = reparseTree();

									if (newNode.checkNodeClass(
										Node.Class.TYPE_OR_ERROR) &&
										!newNode.typeOrError.isList())
									{
										while (workNode.hasParent())
											workNode = workNode.getParent();
										workNode.clear();
										workNode = null;
										addDump("Error arose – destroying " +
											"the current tree and returning " +
											"just with the error node.");
										workNode = newNode;
										newNode = null;
										throw new ReparseBreak();
									}

									addDump("Sub-tree: ",
										newNode.dumpRecursive());
									addDump("Current node: ",
										workNode.dump());

									addDump("Putting the returned sub-tree " +
										"into the existing tree.");

									if (!Node.putNode(workNode, newNode))
									{
										if (useCustoms &&
											workNode.hasRight() &&
											workNode.getRight().checkNodeClass(
												Node.Class.IDENTIFIER))
										{
											addDump("Activating the right " +
												"subnode and changing to " +
												"a custom function.");

											// Activate right subnode and
											// change it to custom function
											workNode = workNode.getRight();
											workNode.changeToCustomFunction();

											if (!Node.putNode(workNode,
												newNode)) throw new
												ReparseError(Value.TypeOrError.
													INVALID_PARENTHESES);
										}
										else throw new ReparseError(
											Value.TypeOrError.
											INVALID_PARENTHESES);
									}

									// workNode remains active
									newNode = null;
									break;


								case RIGBRA:
									switch (workNode.getNodeClass())
									{
									case LITERAL:
									case IDENTIFIER:
									case VARIABLE:
										if (workNode.hasParent())
											throw new ReparseError(
												Value.TypeOrError.PARSE_ERROR);
										break;
									case FUNCTION:
										if (workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_PARAMETER);
										break;
									case OPERATOR:
									case PARENTH_OPERATOR:
									case BRACKET_OPERATOR:
										if (workNode.noLeft() ||
											workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_OPERAND);
										break;
									}

									// In this case dismissing of a newNode is
									// possible, because it was not used:
									addDump("Dismissing the new node.");
									addDump("Searching for and returning " +
										"the root.");
									newNode = null;
									while (workNode.hasParent())
										workNode = workNode.getParent();
									workNode.setBracket();
									throw new ReparseBreak();

								case LEFBRA:

									if (
										// First dimension of array elements
										// access:
										!workNode.checkNodeClass(
											Node.Class.IDENTIFIER) &&
										!workNode.checkNodeClass(
											Node.Class.VARIABLE) &&

										// Multidimensional array or second
										// argument of some operation:
										!(workNode.checkNodeClass(
											Node.Class.OPERATOR) &&
											(
												// (multidimensional)
												workNode.checkOperator(
													Node.Operator.SELECTOR) ||
												// (second argument)
												(workNode.hasRight() &&
													(workNode.getRight().
														checkNodeClass(
															Node.Class.
															IDENTIFIER) ||
													workNode.getRight().
														checkNodeClass(
															Node.Class.
															VARIABLE)
													)
												)
											)))
										throw new ReparseError(
											Value.TypeOrError.
											IDENTIFIER_EXPECTED);

									// newNode will be converted to special
									// operator:
									addDump("Converting the new node to " +
										"a bracket operator.");
									addDump("Creating sub-tree…");

									newNode = new Node(Node.Operator.SELECTOR,
										currentLocation);

									// According to conditions above the
									// following condition should match
									// to “second argument” situation
									// (see above):
									if (workNode.checkNodeClass(
										Node.Class.OPERATOR) &&
										!workNode.checkOperator(
										Node.Operator.SELECTOR))
									{
										// Move to the identifier on right:
										addDump("Moving to a variable in " +
											"the right sub-node: " + workNode.
											dump() + " – " + workNode.
											getRight().dump());
										workNode = workNode.getRight();
									}

									if (!Node.insert(workNode, newNode))
										throw new ReparseError(
											Value.TypeOrError.PARSE_ERROR);
									workNode = newNode;	// activate node
									newNode = null;
									newNode = reparseTree();

									if (newNode.checkNodeClass(
										Node.Class.TYPE_OR_ERROR) &&
										!newNode.typeOrError.isList())
									{
										while (workNode.hasParent())
											workNode = workNode.getParent();
										workNode.clear();
										workNode = null;
										addDump("Error arose – destroying " +
											"the current tree and returning " +
											"just with the error node.");
										workNode = newNode;
										newNode = null;
										throw new ReparseBreak();
									}

									addDump("Sub-tree: ",
										newNode.dumpRecursive());
									addDump("Current node: ",
										workNode.dump());

									addDump("Putting the returned sub-tree " +
										"into the existing tree.");

									if (!Node.putNode(workNode, newNode))
										throw new ReparseError(
											Value.TypeOrError.INVALID_BRACKETS);

									// workNode remains active
									newNode = null;
									break;

								default:	// new – F = operator exc. ‘(’, ‘)’
									switch (workNode.getNodeClass())
									{
									case FUNCTION:
										if (workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_PARAMETER);
										addDump("Inserting the new node " +
											"into the tree.");
										if (!Node.insert(workNode, newNode))
											throw new ReparseError(
												Value.TypeOrError.PARSE_ERROR);
										addDump("Activating the new node.");
										workNode = newNode;	// activate node
										newNode = null;
										break;

									case LITERAL:
									case IDENTIFIER:
									case VARIABLE:
										// If active node is of type literal,
										// identifier or variable, it must not
										// have parent. Then put workNode into
										// newNode and activate newNode…
										addDump("Putting in reverse – new " +
											"node now contains the current " +
											"tree.");
										if (workNode.hasParent() ||
											!Node.putNode(newNode, workNode))
											throw new ReparseError(
												Value.TypeOrError.PARSE_ERROR);

										addDump("Activating the new node.");
										workNode = newNode;	// activate node
										newNode = null;
										break;

									case OPERATOR:

										// Here was found out that an error is
										// buried in the original C/C++
										// library – mistakenly the
										// PARENTH_OPERATOR was handled the
										// same way as common OPERATOR…

										if (workNode.noLeft() ||
											workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_OPERAND);

										// Here was checking of valid
										// assignment. Now it is on another
										// place.

										addDump("Consider priorities? " +
											"(new = ", newNode.getOperator().
											priority, ", current = ",
											workNode.getOperator().priority,
											")");

										if (newNode.getOperator().priority <=
											workNode.getOperator().priority)
										{
											addDump("Yes! Trying to find " +
												"adequate priority.");

											addDump("Priority: new = ",
												newNode.getOperator().
												priority, ", current = ",
												workNode.getOperator().
												priority, " (first)");

											while (workNode.hasParent())
											{
												workNode = workNode.getParent();
												if (newNode.getOperator().
													priority <=
													workNode.getOperator().
													priority) break;
												addDump("Priority: new = ",
													newNode.getOperator().
													priority, ", current = ",
													workNode.getOperator().
													priority, " (continuous)");
											}

											addDump("Priority: new = ",
												newNode.getOperator().
												priority, ", current = ",
												workNode.getOperator().
												priority, " (last)");
										}
										else
										{
											addDump("No. Do not consider…");
										}

										if (newNode.getOperator().priority <=
											workNode.getOperator().priority)
										{
											addDump("Failed (because we " +
												"are in root)!");
											addDump("Inserting the new node " +
												"instead of the current node.");
											if (!Node.insert(workNode, newNode))
												throw new ReparseError(
													Value.TypeOrError.
													PARSE_ERROR);
										}
										else
										{
											addDump("Inserting the new node " +
												"into the right part of the " +
												"current node.");
											if (!Node.insert(workNode.
												getRight(), newNode))
												throw new ReparseError(
													Value.TypeOrError.
													PARSE_ERROR);
										}

										addDump("Activating the new node.");
										workNode = newNode;
										newNode = null;

										break;

									case PARENTH_OPERATOR:

										if (workNode.noLeft() ||
											workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_OPERAND);

										addDump("Parenthesized operator " +
											"found!");
										addDump("Inserting the new node " +
											"instead of the current node " +
											"without any further checking.");
										if (!Node.insert(workNode, newNode))
											throw new ReparseError(
												Value.TypeOrError.
												PARSE_ERROR);

										addDump("Activating the new node.");
										workNode = newNode;
										newNode = null;

										break;

									case BRACKET_OPERATOR:

										if (workNode.noLeft() ||
											workNode.noRight())
											throw new ReparseError(
												Value.TypeOrError.
												MISSING_OPERAND);

										addDump("Bracketed operator " +
											"found!");
										addDump("Inserting the new node " +
											"instead of the current node " +
											"without any further checking.");
										if (!Node.insert(workNode, newNode))
											throw new ReparseError(
												Value.TypeOrError.PARSE_ERROR);

										addDump("Activating the new node.");
										workNode = newNode;
										newNode = null;

										break;
									}
								}
								break;

							case TYPE_OR_ERROR:
								addDump(newNode, ".");

								if (newNode.typeOrError.isList())
								{
									addDump("It’s a list, but I don’t " +
										"know how to handle it right now.");
									// TODO – consider how to handle lists here‼
									if (null != workNode)
									{
										while (workNode.hasParent())
											workNode = workNode.getParent();
										workNode.clear();
										workNode = null;
									}

									workNode = newNode;
									newNode = null;
								}
								else
								{
									if (null != workNode)
									{
										while (workNode.hasParent())
											workNode = workNode.getParent();
										workNode.clear();
										workNode = null;
									}

									workNode = newNode;
									newNode = null;
								}

								throw new ReparseBreak();

							}	// switch (newNode.getNodeClass())

						}	// if (null != newNode)

					}	// while (readerHasCharacters)

				}	// switch (workNode.getNodeClass())

			}
			catch (ReparseError error)
			{
				// Destroy (unused) new node…
				if (null != newNode)
				{
					newNode.clear();
					newNode = null;
				}

				// Destroy work node…
				if (null != workNode)
				{
					while (workNode.hasParent())
						workNode = workNode.getParent();
					workNode.clear();
					workNode = null;
				}

				workNode = new Node(error.type, currentLocation);
			}
			catch (ReparseBreak dummy)
			{
			}
			finally // ReparseError, ReparseBreak:
			{
				// Destroy (unused) new node…
				if (null != newNode)
				{
					newNode.clear();
					newNode = null;
				}

				if (null == workNode)
				{
					workNode = new Node(Value.TypeOrError.
						MISSING_EXPRESSION, currentLocation);
				}
				else
				{
					// Last check before exit…
					switch (workNode.getNodeClass())
					{
					case FUNCTION:
						if (workNode.noRight())
						{
							// if (null != workNode) // pointless – it would
								// crash already at the line above
							// {
								while (workNode.hasParent())
									workNode = workNode.getParent();
								workNode.clear();
								workNode = null;
							// }

							// TODO — need this? if (!useCustoms)
								workNode = new Node(Value.TypeOrError.
									MISSING_PARAMETER, currentLocation);
						}
						break;

					case OPERATOR:
					case PARENTH_OPERATOR:
					case BRACKET_OPERATOR:
						if (workNode.noLeft() || workNode.noRight())
						{
							// if (null != workNode) // pointless – it would
								// crash already at the line above
							// {
								while (workNode.hasParent())
									workNode = workNode.getParent();
								workNode.clear();
								workNode = null;
							// }

							if (useCustoms)
							{
								workNode = new Node();
								workNode.clear();
							}
							else
							{
								workNode = new Node(Value.TypeOrError.
									MISSING_OPERAND, currentLocation);
							}
						}
						break;
					}
				}
			}

			unindentDump();
			return Node.findRoot(workNode);
		}

		/**
		 * Returns a string value of this object.
		 * (This is part of implementation the ValueProvider interface.)
		 */
		public String getString()
		{
			if (state == State.INITIALIZED) return NO_EXPRESSION.getString();

			if (state == State.ATTACHED) parse();

			if (null == tree.getRoot()) return PARSE_ERROR.getString();

			return tree.getRoot().getString();
		}

		/**
		 * Gets string informing about the state of this instance.
		 */
		@Override public String toString()
		{
			switch (state)
			{
			case INITIALIZED: return "This instance is in initial state.";
			case ATTACHED: return "Attached: " + stringToParse;
			case PARSED: return "Parsed: " + stringToParse;
			case PARSE_ERROR:
				if (null == tree.getRoot())
					return "Error: " + PARSE_ERROR.getError().description;
				if (null == tree.getRoot().getValue())
					return "Error: " + tree.getRoot();
				if (null == tree.getRoot().getValue().getError())
					return "Error: " + tree.getRoot().getValue();
				return tree.getRoot().getValue().toString();
			}
			return "This instance is in unknown state.";
		}


		// This list is used only when useNamespaces is set to true (after
		// calling useNamespaces(true) method).
		private final static TreeMap<String, VariableScope> namespaces =
			new TreeMap<String, VariableScope>();

		/**
		 * True value means that this instance should handle some basic
		 * object-based functionality. In this case the variable names
		 * are resolved during evaluation not during processing. Also
		 * the namespaces are allowed and the instance scope and superclass
		 * scope are considered while resolving the variable name without
		 * namespace specification.
		 */
		public void useNamespaces(boolean use)
		{
			if (useNamespaces != use)
			{
				useNamespaces = use;

				if (State.PARSED == state ||
					State.PARSE_ERROR == state)
				{
					tree.clear();
					state = State.ATTACHED;
				}
			}
		}

		/**
		 * Check whether or not the namespaces are in use. See the description
		 * of useNamespaces(boolean) method.
		 */
		public boolean usesNamespaces()
		{
			return useNamespaces;
		}

		/**
		 * Notice that namespaces are in use only after calling
		 * useNamespaces(true) method. Each namespace is, in fact, an
		 * independent variable scope. (It might be an object (instance) or
		 * anything else…) There is one global list of namespaces each of
		 * which (each of variable scopes) may contain nested namespaces.
		 * (See the VariableScope.createNamespace(String) method.) Six
		 * identifiers are reserved and are not allowed to be used as global
		 * namespace: this, self, super, parent, global and general.
		 * This method checks if entered namespace exists and if not, it
		 * creates a new one and returns it as the return value. Otherwise, null
		 * is returned.
		 */
		public static VariableScope createNamespace(String namespace)
		{
			if (namespace.equals("this") || namespace.equals("self"))
			{
				// NO!
				// if (null == instanceVariables)
				// {
				// 	instanceVariables = new VariableScope();
				// 	return instanceVariables;
				// }
				// NO!
				return null;
			}

			if (namespace.equals("super") || namespace.equals("parent"))
			{
				// NO!
				// if (null == superclassVariables)
				// {
				// 	superclassVariables = new VariableScope();
				// 	return superclassVariables;
				// }
				// NO!
				return null;
			}

			if (namespace.equals("global") || namespace.equals("general"))
				return null;

			if (namespaces.containsKey(namespace)) return null;

			VariableScope newNamespace = new VariableScope();
			namespaces.put(namespace, newNamespace);
			return newNamespace;
		}

		/**
		 * Notice that namespaces are in use only after calling
		 * useNamespaces(true) method. This method checks if entered namespace
		 * exists and returns it. Otherwise, it returns null. Notice that six
		 * identifiers are reserved and are not allowed to be used as global
		 * namespace: this, self, super, parent, global and general. In these
		 * cases, this method returns always null.
		 */
		public static VariableScope getNamespace(String namespace)
		{
			if (namespace.equals("this") || namespace.equals("self") ||
				namespace.equals("super") || namespace.equals("parent") ||
				namespace.equals("global") || namespace.equals("general"))
				return null;
			return namespaces.get(namespace);
		}


		/**
		 * Checks whether the parsing machine joins two (or more)
		 * consecutive identifiers using the joinIdentifiers(id1, id2)
		 * method (that may be overloaded).
		 */
		public boolean joinsTheIdentifiers()
		{
			return joinIdentifiers;
		}

		/**
		 * Enables or disables joining of the consecutive identifiers
		 * by the parsing machine during the parsing process. When this
		 * property is enabled the parser uses the joinIdentifiers(id1, id2)
		 * method (that may be overloaded) to join the identifiers.
		 */
		public void joinTheIdentifiers(boolean join)
		{
			if (joinIdentifiers != join)
			{
				joinIdentifiers = join;

				if (State.PARSED == state ||
					State.PARSE_ERROR == state)
				{
					tree.clear();
					state = State.ATTACHED;
				}
			}
		}

		/**
		 * Checks whether the parsing of custom functions and variables
		 * is enabled. The return value is true only when both custom value
		 * providers and use of namespaces are enabled.
		 */
		public boolean useCustomValueProviders()
		{
			return useCustoms && useNamespaces;
		}

		/**
		 * Enables or disables the parsing of custom functions and
		 * variables. This also enables or disables the use of namespaces
		 * since the custom value providing depends on it. This means that
		 * when you need to disable custom value providers and want to keep
		 * use of namespaces enabled, you have to re-enable it after this
		 * method call.
		 */
		public void useCustomValueProviders(boolean use)
		{
			if (useCustoms != use)
			{
				useCustoms = use;

				if (useNamespaces != use)
					useNamespaces(use);
				else if (State.PARSED == state ||
					State.PARSE_ERROR == state)
				{
					tree.clear();
					state = State.ATTACHED;
				}
			}
		}

		/**
		 * This method has a default implementation but is open to be
		 * overridden in the superclass. This is one of the ways how
		 * superclasses may extend this processor. It opens the possibility
		 * to modify how the consecutive identifiers will be joined. The
		 * default implementation joins the identifiers using single space.
		 * That means it allows the identifiers to contain spaces and it
		 * basically works as a space trimmer.
		 * 
		 * NOTE: This method is invoked only in case the property
		 *     “joinTheIdentifiers” is enabled‼
		 */
		public Identifier joinIdentifiers(Identifier id1, Identifier id2)
		{
			return new Identifier(this, id1.identifier +
				" " + id2.identifier);
		}

		/**
		 * This method has a default implementation but is open to be
		 * overridden in the superclass. This is one of the ways how
		 * superclasses may extend this processor. It opens the possibility
		 * to implement custom functions providing unique functionality.
		 * The default implementation creates “unknown value” error (remember
		 * that this method is called only when the corresponding internal
		 * function is not found).
		 * 
		 * NOTE: This method is invoked only in case the property
		 *     “useCustomValueProviders” is enabled‼
		 */
		public Value customFunction(String funcID, Value params)
		{
			return new Value(Value.TypeOrError.UNKNOWN_VALUE);
		}

		/**
		 * This method has a default implementation but is open to be
		 * overridden in the superclass. This is one of the ways how
		 * superclasses may extend this processor. It opens the possibility
		 * to read or write the contents of custom variables. The default
		 * implementation creates “unknown value” (on the attempt to read)
		 * or “invalid assignment” (on the attempt to write) error (remember
		 * that this method is called only when the corresponding internal
		 * variable has not been found). This mechanism also allows
		 * implementing the declarative approach to the variables (that
		 * means that no variable will be created automatically).
		 * 
		 * NOTE: This method is invoked only in case the property
		 *     “useCustomValueProviders” is enabled‼
		 */
		public Value customVariable(String varID, Value write)
		{
			if (null != write)
				new Value(Value.TypeOrError.INVALID_ASSIGNMENT);
			return new Value(Value.TypeOrError.UNKNOWN_VALUE);
		}


		/**
		 * Returns detailed dump of last parsing process unless the dumping is
		 * disabled. Calling of this method gives sense only after attempt of
		 * parsing (and only if dumping is enabled). It will return detailed
		 * dump of successful or unsuccessful attempt of parsing process.
		 */
		public String dump() { return dump.toString(); }

		/** Checks if creating of dump of parsing process is enabled. */
		public boolean dumpingIsEnabled() { return dumpEnabled; }

		/** Enables creating of dump of parsing process. */
		public void enableDumping() { dumpEnabled = true; }

		/** Disables creating of dump of parsing process. */
		public void disableDumping() { dumpEnabled = false; }


		// Helper method creating dump listing continuously during parsing
		// process.
		private void addDump(Object... args)
		{
			if (dumpEnabled)
			{
				for (int i = 0; i < dumpIndent; ++i)
					dump.append("  ");
				for (Object arg : args)
				{
					if (null == arg)
						dump.append("null");
					else
						dump.append(arg.toString());
				}
				dump.append("\n");
			}
		}

		// Helper method increasing the indentation of next dump lines.
		private void indentDump() { ++dumpIndent; }

		// Helper method decreasing the indentation of next dump lines.
		private void unindentDump() { if (dumpIndent > 0) --dumpIndent; }
	// }


	//////////////////////////////////////////////////////////////////////
	// Static extensions to some operators
	//////////////////////////////////////////////////////////////////////

	// {

		/**
		 * This static method serves to extend the standard OR (||)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact logical) operation.
		 */
		public static Value advancedOr(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard AND (&&)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact logical) operation.
		 */
		public static Value advancedAnd(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard GTEQ (>=)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact relational) operation.
		 */
		public static Value advancedGtEq(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard GT (>)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact relational) operation.
		 */
		public static Value advancedGt(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard LTEQ (<=)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact relational) operation.
		 */
		public static Value advancedLtEq(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard LT (&lt;)
		 * operator functionality. You can overload it to enhance the
		 * standard “numeric” (in fact relational) operation.
		 */
		public static Value advancedLt(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard MIN (min)
		 * operator functionality. You can overload it to enhance the
		 * standard numeric operation (or better said a selector function
		 * implemented as binary operation).
		 */
		public static Value advancedMin(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard MAX (max)
		 * operator functionality. You can overload it to enhance the
		 * standard numeric operation (or better said a selector function
		 * implemented as binary operation).
		 */
		public static Value advancedMax(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard SUB (-)
		 * operator functionality. You can overload it to enhance this
		 * standard numeric operation.
		 */
		public static Value advancedSub(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard MOD (%)
		 * operator functionality. You can overload it to enhance this
		 * standard numeric operation.
		 */
		public static Value advancedMod(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard DIV (/)
		 * operator functionality. You can overload it to enhance this
		 * standard numeric operation.
		 */
		public static Value advancedDiv(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard MUL (*)
		 * operator functionality. You can overload it to enhance this
		 * standard numeric operation.
		 */
		public static Value advancedMul(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

		/**
		 * This static method serves to extend the standard POW (^)
		 * operator functionality. You can overload it to enhance this
		 * standard numeric operation.
		 */
		public static Value advancedPow(Value a, Value b)
		{ return new Value(Value.TypeOrError.UNSUPPORTED_OPERATION); }

	// }


	//////////////////////////////////////////////////////////////////////
	// Private helper nested classes
	//////////////////////////////////////////////////////////////////////

	// Helper class extended from Throwable superclass. Instances of this class
	// are thrown in some cases in the reparseTree method. Throwing of this
	// throwable changes the flow of the code inside the reparseTree method
	// and bears the information about arisen error.
	private static class ReparseError extends Throwable
	{
		private final static long serialVersionUID = -1L;

		public final Value.TypeOrError type;

		public ReparseError(Value.TypeOrError type)
		{
			this.type = type;
		}
	}

	// Helper dummy class extended from Throwable superclass. Instances
	// of this class are thrown in some cases in the reparseTree method.
	// Throwing of this throwable just redirects the code flow inside
	// complicated reparseTree method. There are no additional data needed.
	// Empty throwable is enough for this job.
	private static class ReparseBreak extends Throwable
	{
		private final static long serialVersionUID = -1L;
	}
}

/**
 * This interface is implemented by every class that should provide a value
 * and must override the toString method.
 */
interface ValueProvider
{
	public ExpressionProcessor.Value getValue();
	public String getString();
}
