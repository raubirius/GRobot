
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je to pomocná trieda slúžiaca na transfer
// ľubovoľných údajov prostredníctvom dočasného súboru (v „temp“ priečinku,
// ktorého názov a umiestnenie môže byť rôzne). Primárne táto trieda pomáha
// triede SVGClip vykonávať jej činnosť. Licencia a zdroj sú uvedené nižšie
// v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)
// 
// Source: https://stackoverflow.com/questions/4879956/swing-dragdrop-
//     file-transferable

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileTransferable implements Transferable
{
	private final List<File> files;
	private final DataFlavor[] flavors;

	/**
	 * A drag-and-drop object for transfering a file.
	 * @param file file to transfer – this file should already exist,
	 * otherwise it may not be accepted by drag targets.
	 */
	public FileTransferable(Collection<File> files)
	{
		this.files = Collections.unmodifiableList(new ArrayList<File>(files));
		this.flavors = new DataFlavor[] {DataFlavor.javaFileListFlavor};
	}

	static FileTransferable createFileInTempDirectory(String filename)
		throws IOException
	{
		File dir = new File(System.getProperty("java.io.tmpdir"));
		File f = new File(dir, filename);
		f.createNewFile();
		return new FileTransferable(Collections.singletonList(f));
	}

	public List<File> getFiles() { return this.files; }

	// Transferable implementation:

	@Override public Object getTransferData(DataFlavor flavor)
		// throws UnsupportedFlavorException
	{
		if (isDataFlavorSupported(flavor)) return this.files;
		return null;
	}

	@Override public DataFlavor[] getTransferDataFlavors()
	{ return this.flavors; }

	@Override public boolean isDataFlavorSupported(DataFlavor flavor)
	{ return DataFlavor.javaFileListFlavor.equals(flavor); }
}
