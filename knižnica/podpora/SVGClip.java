
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to pomocná trieda slúžiaca na transfer SVG
// údajov (najmä s pomocou schránky operačného systému). (Pozri tiež triedu
// FileTransferable v rámci tohto balíčka.) Licencia a zdroje sú uvedené
// nižšie v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)
// 
// Sources:
// 
// Primary: https://stackoverflow.com/questions/33827607/create-svg-
//     dataflavor-in-java-with-dataflavor-image-svgxml
// Incomplete copy: https://stackoverflow.com/questions/33726321/how-to-
//     transfer-svg-image-to-other-programs-with-dragndrop-in-java
// 
// Other:
// 
// https://bugs.openjdk.java.net/browse/JDK-8140526
// http://bugs.java.com/bugdatabase/view_bug.do;jsessionid=
//     67bf0d656b66cc60360819906ba?bug_id=4493178
// http://www.javaexamples.org/java/java.awt.datatransfer/
//     getdefaultflavormap-in-systemflavormap.html

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class SVGClip implements Transferable
{
	public final static DataFlavor svgFlavor = new DataFlavor(
		"image/svg+xml; class=java.io.InputStream",
		"Scalable Vector Graphic");

	public final static DataFlavor inkscapeFlavor = new DataFlavor(
		"image/x-inkscape-svg; class=java.io.InputStream",
		"Scalable Vector Graphic");

	private final static DataFlavor[] supportedFlavors;

	static
	{
		SystemFlavorMap systemFlavorMap =
			(SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

		// Add flavors for “copy to clipboard” (output) transfer:
		systemFlavorMap.addUnencodedNativeForFlavor(
			svgFlavor, "image/svg+xml");
		systemFlavorMap.addUnencodedNativeForFlavor(
			inkscapeFlavor, "image/x-inkscape-svg");

		// Add flavors for “paste from clipboard” (input) transfer:
		systemFlavorMap.addFlavorForUnencodedNative(
			"image/svg+xml", svgFlavor);
		systemFlavorMap.addFlavorForUnencodedNative(
			"image/x-inkscape-svg", inkscapeFlavor);

		// This is always the same, so it has been made static:
		supportedFlavors = new DataFlavor[] {svgFlavor,
			DataFlavor.javaFileListFlavor, inkscapeFlavor};
	}


	private FileTransferable file;

	private final String svgString;

	public SVGClip(String str) throws IOException, FileNotFoundException
	{
		this.svgString = str;

		this.file = FileTransferable.createFileInTempDirectory("temp.svg");
		List<File> files = this.file.getFiles();

		if (files.isEmpty())
			throw new RuntimeException("Cannot create temporary file!");

		// Write the SVG string to a file in temp.
		PrintWriter writer = new PrintWriter(files.get(0));
		writer.println(this.svgString);
		writer.close();
	}


	// I don’t know what was the point of implementing this if it’s empty,
	// but I kept it. Maybe it is useful for some future implementations…
	// (??)
	public void lostOwnership(java.awt.datatransfer.Clipboard clip,
		Transferable tr) {}
	// Test (never executed at all): { System.out.println("Lost ownership."); }


	// Transferable implementation:

	@Override public DataFlavor[] getTransferDataFlavors()
	{ return supportedFlavors; }

	@Override public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		for (DataFlavor supported : supportedFlavors)
			if (flavor.equals(supported)) return true;
		return false;
	}

	@Override public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException
	{
		if (isDataFlavorSupported(flavor))
		{
			if (flavor.equals(inkscapeFlavor) || flavor.equals(svgFlavor))
			{
				InputStream stream = new ByteArrayInputStream(
					this.svgString.getBytes(StandardCharsets.UTF_8));
				return stream;
			}

			if (flavor.equals(DataFlavor.javaFileListFlavor))
				return this.file.getTransferData(flavor);
		}

		throw new UnsupportedFlavorException(flavor);
	}
}
