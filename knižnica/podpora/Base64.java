
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Slúži na základnú prácu s kódovaním Base64.
// (Dokáže kódovať, dekódovať, zapisovať a čítať údaje kódované s použitím
// algoritmu Base64.) Licencia a zdroje sú uvedené nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)
// 
// 
// Sources:
//  Encode:
//   http://www.wikihow.com/Encode-a-String-to-Base64-With-Java
//   (unknown authors)
//   http://www.freeformatter.com/base64-encoder.html
//   (unknown authors)
//   https://en.wikipedia.org/wiki/Base64
//   (unknown authors)
//   http://stackoverflow.com/questions/5258057/images-in-css-or-html-as-data-base64
//   (multiple different authors)
//   http://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java/
//   (Author: John Purcell)
//   http://www.coderanch.com/t/391314/java/java/read-write-binary-file
//   (multiple different authors)
//  Decode:
//   http://www.coderanch.com/t/482256/java/java/Converting-Base-encoded-String-Image
//   (Author: Hanuma Deepak Muvvala)

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>This class should help the programmer to utilize the Base64 algorithm.
 * This algorithm is able to encode any binary data (e.g. image data) into
 * the string form (ASCII). The data may be stored in a text file and turned
 * back to the binary form later. (For more details see the sources; e.g.
 * <a href="https://tools.ietf.org/html/rfc3548"
 * target="_blank">RFC 3548</a> or
 * <a href="https://tools.ietf.org/html/rfc4648"
 * target="_blank">RFC 4648</a>.)</p>
 * 
 * @author Roman Horváth
 * @version 9. 6. 2018
 * 
 * @exclude
 */
public abstract class Base64
{
	// Base64 mapping table.
	private static final byte[] base64map = new byte[64];

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static final byte[] nibbleMap = new byte[128];

	// Default initialisation:
	static { useStandardMap(); }

	// Original data.
	private static byte[] bytes;

	// Base64 encoded data.
	private static byte[] encoded;


	// — — — — — — — — — — — — — — —   Setup   — — — — — — — — — — — — — — —

	/**
	 * <p>Initialises the standard Base64 maps.</p>
	 */
	public static void useStandardMap()
	{
		// Old way of initialisation:

		// base64map =
		// 	"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".
		// 	getBytes();

		// char[] map1 = new char[64];
		// {
		// 	int i = 0;

		// 	for (char c = 'A'; c <= 'Z'; ++c) map1[i++] = c;
		// 	for (char c = 'a'; c <= 'z'; ++c) map1[i++] = c;
		// 	for (char c = '0'; c <= '9'; ++c) map1[i++] = c;

		// 	map1[i++] = '+';
		// 	map1[i++] = '/';
		// }

		// for (int i = 0; i < nibbleMap.length; ++i) nibbleMap[i] = -1;
		// for (int i = 0; i < 64; ++i) nibbleMap[map1[i]] = (byte)i;

		// New way of initialisation:
		System.arraycopy(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".
			getBytes(), 0, base64map, 0, base64map.length);
		for (int i = 0; i < nibbleMap.length; ++i) nibbleMap[i] = -1;
		for (byte i = 0; i < 64; ++i) nibbleMap[base64map[i]] = i;
	}

	/**
	 * <p>Initialises the maps to URL safe maps. These maps are used in the
	 * URL- and filename-safe dialect described in <a
	 * href="https://tools.ietf.org/html/rfc3548#section-4"
	 * target="_blank">section 4 of the RFC 3548</a>
	 * <!-- old refference: http://www.faqs.org/rfcs/rfc3548.html -->
	 * and <a
	 * href="https://tools.ietf.org/html/rfc4648#section-5"
	 * target="_blank">section 5 of the RFC 4648</a>.</p>
	 * 
	 * <p>(In the RFCs you can see that the last two characters of the
	 * Base64 map became “hyphen” and “underscore” instead of “plus” and
	 * “slash.”)</p>
	 */
	public static void useURLSafeMap()
	{
		System.arraycopy(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".
			getBytes(), 0, base64map, 0, base64map.length);
		for (int i = 0; i < nibbleMap.length; ++i) nibbleMap[i] = -1;
		for (byte i = 0; i < 64; ++i) nibbleMap[base64map[i]] = i;
	}

	/**
	 * <p>Initialises the maps using custom string. The string must contain
	 * exactly 64 characters that are within the ASCII range 33 (!) – 126 (~),
	 * except the ASCII 61 (=), and no character may repeat itself within the
	 * string. You can use your own table to simple encryption of your
	 * data.</p>
	 * 
	 * @param table the string containing a new mapping table
	 * @return true if the new table has been accepted
	 */
	public static boolean useCustomMap(String table)
	{
		if (null == table || 64 != table.length()) return false;
		char ch = table.charAt(0);
		if (61 == ch || ch < 33 || ch > 126) return false;
		for (int i = table.length() - 1; i > 0; --i)
		{
			ch = table.charAt(i);
			if (61 == ch || ch < 33 || ch > 126) return false;
			for (int j = i - 1; j >= 0; --j)
				if (ch == table.charAt(j)) return false;
		}
		System.arraycopy(table.getBytes(), 0, base64map, 0, base64map.length);
		for (int i = 0; i < nibbleMap.length; ++i) nibbleMap[i] = -1;
		for (byte i = 0; i < 64; ++i) nibbleMap[base64map[i]] = i;
		return true;
	}


	// — — — — — — — — — — — — — — — Read/Write — — — — — — — — — — — — — — —

	/**
	 * <p>Reads contents of specified file and returns the data as array of
	 * bytes.</p>
	 * 
	 * @param filename name of the file to read the data from
	 * @return read data returned as array of bytes
	 */
	public static byte[] readBytes(String filename) throws IOException
	{
		File file = new File(filename);
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(filename);
			int length = (int)file.length();
			byte[] bytesRead = new byte[length];
			fis.read(bytesRead);
			return bytesRead;
		}
		finally
		{
			if (null != fis) fis.close();
		}
	}

	/**
	 * <p>Writes any data stored in the array of bytes into the specified
	 * file. (In fact this method does the same thing as if you would call
	 * the writeEncoded(filename, bytes) method, but is here for the
	 * convenience – use of this method can make the code more readable.)</p>
	 * 
	 * @param filename name of the file to write the data
	 * @param bytes array of bytes (the data) to write
	 */
	public static void writeBytes(String filename, byte[] bytes)
		throws IOException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(filename);
			fos.write(bytes);
		}
		finally
		{
			if (null != fos) fos.close();
		}
	}

	/**
	 * <p>Writes your Base64 encoded string into the specified file. (In fact
	 * this method writes any specified string into the file, but it is
	 * expected that you will pass a Base64 encoded data into the second
	 * parameter.)</p>
	 * 
	 * @param filename name of the file to write the data
	 * @param base64Encoded string containing Base64 encoded data
	 */
	public static void writeEncoded(String filename, String base64Encoded)
		throws IOException
	{
		writeEncoded(filename, base64Encoded.getBytes(), null);
	}

	/**
	 * <p>Writes your Base64 encoded string into the specified file. (In fact
	 * this method writes any specified string into the file, but it is
	 * expected that you will pass a Base64 encoded data into the second
	 * parameter.)</p>
	 * 
	 * <p>The {@code mimeType} parameter causes to write this prefix before
	 * the data to the stream: {@code data:}<em>«mimeType»</em>{@code 
	 * ;base64,}. If you type {@code null}, this parameter will be
	 * ignored.</p>
	 * 
	 * @param filename name of the file to write the data
	 * @param base64Encoded string containing Base64 encoded data
	 * @param mimeType MIME type of the data to be written; enter {@code null}
	 *     to get this parameter ignored
	 */
	public static void writeEncoded(String filename,
		String base64Encoded, String mimeType) throws IOException
	{
		writeEncoded(filename, base64Encoded.getBytes(), mimeType);
	}

	/**
	 * <p>Writes your Base64 encoded data stored in the array of bytes into
	 * the specified file. (In fact this method writes any data into the file,
	 * but it is expected that the array will contain a Base64 encoded
	 * data.)</p>
	 * 
	 * @param filename name of the file to write the data
	 * @param base64Encoded array of bytes containing the Base64 encoded data
	 */
	public static void writeEncoded(String filename, byte[] base64Encoded)
		throws IOException
	{
		writeEncoded(filename, base64Encoded, null);
	}

	/**
	 * <p>Writes your Base64 encoded data stored in the array of bytes into
	 * the specified file. (In fact this method writes any data into the file,
	 * but it is expected that the array will contain a Base64 encoded
	 * data.)</p>
	 * 
	 * <p>The {@code mimeType} parameter causes to write this prefix before
	 * the data to the stream: {@code data:}<em>«mimeType»</em>{@code 
	 * ;base64,}. If you type {@code null}, this parameter will be ignored.</p>
	 * 
	 * @param filename name of the file to write the data
	 * @param base64Encoded array of bytes containing the Base64 encoded data
	 * @param mimeType MIME type of the data to be written; enter {@code null}
	 *     to get this parameter ignored
	 */
	public static void writeEncoded(String filename,
		byte[] base64Encoded, String mimeType) throws IOException
	{
		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(filename);
			if (null != mimeType)
				fos.write(("data:" + mimeType + ";base64,").getBytes());
			fos.write(base64Encoded);
		}
		finally
		{
			if (null != fos) fos.close();
		}
	}

	/**
	 * <p>Puts your Base64 encoded data stored in the array of bytes into the
	 * specified data stream. (In fact this method puts any data into the
	 * stream, but it is expected that the array will contain a Base64 encoded
	 * data.)
	 * <!-- (This method has only one version.) -->
	 * 
	 * You can use this method to write the data to a previously opened file
	 * (as a stream).
	 * 
	 * The {@code mimeType} parameter causes to write this prefix before the
	 * data to the stream: {@code data:}<em>«mimeType»</em>{@code ;base64,}.
	 * If you type {@code null}, this parameter will be ignored.
	 * 
	 * @param outputStream stream to write the data
	 * @param base64Encoded array of bytes containing the Base64 encoded data
	 * @param mimeType MIME type of the data to be written; enter {@code null}
	 *     to get this parameter ignored
	 */
	public static void writeEncoded(FileOutputStream outputStream,
		byte[] base64Encoded, String mimeType) throws IOException
	{
		if (null != mimeType)
			outputStream.write(("data:" + mimeType + ";base64,").getBytes());
		outputStream.write(base64Encoded);
	}


	// — — — — — — — — — — — — — — —  Encode  — — — — — — — — — — — — — — —

	// Resizes the internal data array to new length padding the new elements
	// with zeroes.
	private static void zeroPad(int length)
	{
		byte[] padded = new byte[length]; // initialized to zero by JVM
		System.arraycopy(bytes, 0, padded, 0, bytes.length);
		bytes = padded;
	}

	// Encodes the data stored in the internal buffer (array named bytes) to
	// another internal buffer (array named encoded) using the Base64
	// algorithm.
	private static void encode()
	{
		// Determine target size and create new array.
		encoded = new byte[4 * ((bytes.length + 2) / 3)];

		// Determine how many padding bytes to add to the output.
		int paddingCount = (3 - (bytes.length % 3)) % 3;

		// Add any necessary padding to the input.
		if (0 != paddingCount)
			zeroPad(bytes.length + paddingCount);

		// Process 3 bytes at a time, churning out 4 output bytes
		// (worry about CRLF insertions later).
		for (int i = 0, k = 0; i < bytes.length; i += 3)
		{
			int j = ((bytes[i] & 0xff) << 16) |
				((bytes[i + 1] & 0xff) << 8) |
				(bytes[i + 2] & 0xff);

			encoded[k++] = base64map[(j >> 18) & 0x3f];
			encoded[k++] = base64map[(j >> 12) & 0x3f];
			encoded[k++] = base64map[(j >> 6) & 0x3f];
			encoded[k++] = base64map[j & 0x3f];
		}

		// original: for (int i = 0; i < paddingCount; ++i)
		// original: 	encoded[encoded.length - 1 - i] = '=';
		for (int i = encoded.length - paddingCount;
			i < encoded.length; ++i) encoded[i] = '=';
	}


	/**
	 * <p>Encodes passed data to a Base64 string and returns the resulting
	 * data.</p>
	 * 
	 * @param bytes bytes to encode
	 * @return Base64 encoded string
	 */
	public static String encode(byte[] bytes)
	{
		Base64.bytes = bytes;
		encode();
		try
		{
			// (Uses UTF-8, but in this case it does not matter…)
			return new String(encoded, "UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			return new String(encoded);
		}
	}

	/**
	 * <p>Encodes passed string to UTF-8 bytes (or locale default in case
	 * of failure) and converts them to Base64 string which is then
	 * returned.</p>
	 * 
	 * @param string original string
	 * @return UTF-8 and Base64 encoded string
	 */
	public static String encode(String string)
	{
		try
		{
			// Use appropriate encoding string!
			bytes = string.getBytes("UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			bytes = string.getBytes();
		}

		encode();
		try
		{
			// (Uses UTF-8, but in this case it does not matter…)
			return new String(encoded, "UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			return new String(encoded);
		}
	}

	/**
	 * <p>Encodes passed data to a Base64 data and returns the data in bytes
	 * array.</p>
	 * 
	 * @param bytes bytes to encode
	 * @return Base64 encoded bytes
	 */
	public static byte[] encodeToBytes(byte[] bytes)
	{
		Base64.bytes = bytes;
		encode();
		return encoded;
	}

	/**
	 * <p>Encodes passed string to UTF-8 bytes (or locale default in case
	 * of failure), converts them to Base64 bytes and returns the data
	 * in bytes array.</p>
	 * 
	 * @param string original string
	 * @return Base64 encoded bytes
	 */
	public static byte[] encodeToBytes(String string)
	{
		try
		{
			// Use appropriate encoding string!
			bytes = string.getBytes("UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			bytes = string.getBytes();
		}

		encode();
		return encoded;
	}


	// — — — — — — — — — — — — — — —  Split  — — — — — — — — — — — — — — —

	/**
	 * <p>Splits entered string to several lines with maximum size of 76
	 * characters. You can use this method to wrap encoded Base64 string.</p>
	 * 
	 * @param string original string
	 * @return split string
	 */
	public static String splitLines(String string)
	{
		return splitLines(string, 76);
	}

	/**
	 * <p>Splits entered string to several lines with maximum length specified
	 * by second parameter. You can use this method to wrap encoded Base64
	 * string.</p>
	 * 
	 * @param string original string
	 * @param splitLinesAt maximum length of the lines
	 * @return split string
	 */
	public static String splitLines(String string, int splitLinesAt)
	{
		/* Original way:
		String lines = "";

		for (int i = 0; i < string.length(); i += splitLinesAt)
		{
			lines += string.substring(i,
				Math.min(string.length(), i + splitLinesAt));
			lines += "\r\n";
		}

		return lines; */

		StringBuffer lines = new StringBuffer();

		for (int i = 0; i < string.length(); i += splitLinesAt)
		{
			lines.append(string, i, Math.min(
				string.length(), i + splitLinesAt));
			lines.append("\r\n");
		}

		return lines.toString();
	}


	// — — — — — — — — — — — — — — —  Decode  — — — — — — — — — — — — — — —

	/**
	 * <p>Decodes a byte array from Base64 format.</p>
	 * 
	 * @param string Base64 string to be decoded
	 * @return string containing the data decoded using UTF-8
	 *     or locale default encoding (when the UTF-8 fails)
	 * 
	 * @throws IllegalArgumentException if the input is not valid Base64
	 *     encoded data.
	 */
	public static String decode(String string)
	{
		byte[] out = decodeToBytes(string.replaceAll("\\s+", "").getBytes());

		try
		{
			return new String(out, "UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			return new String(out);
		}
	}

	/**
	 * <p>Decodes a byte array from Base64 format. No blanks or line breaks
	 * are allowed within the Base64 encoded data.</p>
	 * 
	 * @param bytes array containing the Base64 encoded data
	 * @return string containing the data decoded using UTF-8
	 *     or locale default encoding (when the UTF-8 fails)
	 * 
	 * @throws IllegalArgumentException if the input is not valid Base64
	 *     encoded data
	 */
	public static String decode(byte[] bytes)
	{
		byte[] out = decodeToBytes(bytes);

		try
		{
			return new String(out, "UTF-8");
		}
		catch (Exception ignored)
		{
			// Use locale default rather than croak…
			return new String(out);
		}
	}

	/**
	 * <p>Decodes a byte array from Base64 format.</p>
	 * 
	 * @param string Base64 string to be decoded
	 * @return array containing the decoded data bytes
	 * 
	 * @throws IllegalArgumentException if the input is not valid Base64
	 *     encoded data
	 */
	public static byte[] decodeToBytes(String string)
	{
		// Before the called method received the char[] type and thus this
		// method must have been use the string.toCharArray()…
		return decodeToBytes(string.replaceAll("\\s+", "").getBytes());
	}

	/**
	 * <p>Decodes a byte array from Base64 format. No blanks or line breaks
	 * are allowed within the Base64 encoded data.</p>
	 * 
	 * @param bytes array containing the Base64 encoded data
	 * @return array containing the decoded data bytes
	 * 
	 * @throws IllegalArgumentException if the input is not valid Base64
	 *     encoded data
	 */
	public static byte[] decodeToBytes(byte[] bytes)
	{
		int iLen = bytes.length;

		if (iLen % 4 != 0)
			throw new IllegalArgumentException(
				"Length of Base64 encoded input " +
				"string is not a multiple of 4.");

		while (iLen > 0 && bytes[iLen - 1] == '=') --iLen;

		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0, op = 0;

		while (ip < iLen)
		{
			int i0 = bytes[ip++];
			int i1 = bytes[ip++];
			int i2 = ip < iLen ? bytes[ip++] : 'A';
			int i3 = ip < iLen ? bytes[ip++] : 'A';

			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException(
					"Illegal character in Base64 encoded data.");

			int b0 = nibbleMap[i0];
			int b1 = nibbleMap[i1];
			int b2 = nibbleMap[i2];
			int b3 = nibbleMap[i3];

			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException(
					"Illegal character in Base64 encoded data.");

			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;

			if (op < oLen) out[op++] = (byte)o1;
			if (op < oLen) out[op++] = (byte)o2;
		}

		return out;
	}


	// — — — — — — — — — — — — — — —  Convert  — — — — — — — — — — — — — — —

	/**
	 * <p>Converts data from specified file (supposedly image file) into
	 * a new file that will contain data encoded using the Base64 algorithm.
	 * The output file name will appear in following form:
	 * <em>«original image name»</em>{@code .base64}.
	 * Following prefix will be written in front of the Base64 data:
	 * {@code data:}<em>«mimeType»</em>{@code ;base64,}; where
	 * <em>«mimeType»</em> can be one of: {@code image/png}, {@code image/gif},
	 * or {@code image/jpeg}.</p>
	 * 
	 * @param imageFile the name of the file containing an image
	 */
	public static void convertImage(String imageFile) throws IOException
	{
		convertImage(imageFile, (String)null);
	}

	/**
	 * <p>Converts data from specified file (supposedly image file) into
	 * a new file that will contain data encoded using the Base64 algorithm.
	 * Following prefix will be written in front of the Base64 data:
	 * {@code data:}<em>«mimeType»</em>{@code ;base64,}; where
	 * <em>«mimeType»</em> can be one of: {@code image/png}, {@code image/gif},
	 * or {@code image/jpeg}.</p>
	 * 
	 * @param imageFile the name of the file containing an image
	 * @param base64File the name of the file to write the Base64 data
	 */
	public static void convertImage(String imageFile, String base64File)
		throws IOException
	{
		String mimeType;
		int indexOf = imageFile.lastIndexOf('.');

		if (-1 == indexOf)
		{
			if (null == base64File || base64File.isEmpty())
				base64File = imageFile + ".base64";
			mimeType = "jpeg";
		}
		else
		{
			if (null == base64File || base64File.isEmpty())
				base64File = imageFile.substring(0, indexOf) + ".base64";
			mimeType = imageFile.substring(indexOf + 1);
		}

		if (mimeType.equalsIgnoreCase("png")) mimeType = "image/png";
		else if (mimeType.equalsIgnoreCase("gif")) mimeType = "image/gif";
		else mimeType = "image/jpeg";

		// System.out.println("Input: " + imageFile);
		// System.out.println("Output: " + base64File);
		// System.out.println("Mime: " + mimeType);

		bytes = readBytes(imageFile); encode();
		writeEncoded(base64File, encoded, mimeType);
	}

	/**
	 * <p>Converts data from specified file (supposedly image file) into
	 * a new file that will contain data encoded using the Base64 algorithm.
	 * Following prefix will be written in front of the Base64 data:
	 * {@code data:}<em>«mimeType»</em>{@code ;base64,}; where
	 * <em>«mimeType»</em> can be one of: {@code image/png}, {@code image/gif},
	 * or {@code image/jpeg}.</p>
	 * 
	 * @param imageFile the name of the file containing an image
	 * @param outputStream stream to write the Base64 data
	 */
	public static void convertImage(String imageFile,
		FileOutputStream outputStream) throws IOException
	{
		String mimeType;
		int indexOf = imageFile.lastIndexOf('.');

		if (-1 == indexOf) mimeType = "jpeg";
		else mimeType = imageFile.substring(indexOf + 1);

		if (mimeType.equalsIgnoreCase("png")) mimeType = "image/png";
		else if (mimeType.equalsIgnoreCase("gif")) mimeType = "image/gif";
		else mimeType = "image/jpeg";

		bytes = readBytes(imageFile); encode();
		writeEncoded(outputStream, encoded, mimeType);
	}
}
