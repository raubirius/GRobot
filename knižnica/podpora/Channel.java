
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je súčasťou pôvodného balíčka generator slúžiaceho
// na generovanie zvukov v reálnom čase. Licencia a zdroje sú uvedené nižšie
// v anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import static java.lang.Math.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.ByteBuffer;

import java.util.Arrays;
import java.util.Random;
// import java.util.Vector;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * <p>This is a generator channel that can be customized using three interfaces:
 * AudioFunction, FrequencyChanger and VolumeAdjuster.</p>
 * 
 * <p>Used information sources:<br />
 * ⟨http://www.wolinlabs.com/blog/java.sine.wave.html⟩.</p>
 * 
 * @author Roman Horváth
 * @version  2. 11. 2018 – added noise functions…
 * @version 11. 12. 2016 – first finished version
 * 
 * @exclude
 */
public class Channel implements AudioFunction
{
	/**
	 * <p>Sampling rate (in hertz).</p>
	 */
	public final static int SAMPLING_RATE = 44100;

	/**
	 * <p>Sample size (in bytes).</p>
	 */
	public final static int SAMPLE_SIZE = Short.BYTES; // 2

	/**
	 * <p>Number of channels – usually one (mono) or two (stereo).</p>
	 */
	public final static int CHANNELS = 1;

	/**
	 * <p>Signed/unsigned data type flag.</p>
	 */
	public final static boolean SIGNED = true;

	/**
	 * <p>Endianity data type flag.</p>
	 */
	public final static boolean BIG_ENDIAN = true;

	/**
	 * <p>Duration of the sine wave buffer. Legend: 0.1 means about a 100 ms.</p>
	 */
	public final static double BUFFER_DURATION = 0.044;


	/**
	 * <p>The number of cycles computed in one packet.</p>
	 */
	public final static int CYCLES_PER_PACKET = (int)(BUFFER_DURATION * 100);

	/**
	 * <p>The number of samples computed in each cycle.</p>
	 */
	public final static int SAMPLES_PER_CYCLE =
		(int)(BUFFER_DURATION * SAMPLING_RATE) / CYCLES_PER_PACKET;

	/**
	 * <p>Size (in bytes) of sine wave samples it will be created on each
	 * loop pass.</p>
	 */
	public final static int SINE_PACKET_SIZE =
		SAMPLES_PER_CYCLE * SAMPLE_SIZE * CYCLES_PER_PACKET;


	// Constants for alternative waves:
	private final static double γ = 1.415, δ = 1.666;

	// Constant defined to make some computations a little bit faster:
	private final static double SAMPLING_DIVISOR =
		SAMPLING_RATE / (2.0 * PI);


	// Custom audio generating function:
	private AudioFunction audioFunction = this;

	// Custom frequency changer:
	private FrequencyChanger frequencyChanger = null;

	// Custom volume adjuster:
	private VolumeAdjuster volumeAdjuster = null;

	// Audio source data line:
	private SourceDataLine line = null;

	// Data buffer storing generated data that are put to the
	// source data line immediately after generating:
	private ByteBuffer byteBuffer = null;

	// Flag signalizing that the initialisation has been made already:
	private boolean initialised = false;

	// Buffer storing planed frequency changes – patch to reach more
	// dynamic changes than those reachable by just generating relatively
	// big chunks that are put into the source data line at once (details
	// in the main comment of this file):
	private double[] frequencyValues =
		new double[(int)(BUFFER_DURATION * 1000.0)];

	// Buffer storing planed volume changes – it is connected to
	// frequencyValues buffer:
	private double[] volumeValues = new double[frequencyValues.length];


	// The last frequency used by recovery to the playing state:
	private double lastFrequency = 880.0;

	// The last volume used by recovery to the playing state:
	private double lastVolume = Short.MAX_VALUE * 0.8;

	// Current frequency value (zero means silence):
	private double currentFrequency = 0.0;

	// Current volume value:
	private double currentVolume = 0.0;

	// Active generator frequency – generated in the very moment:
	private double activeFrequency = currentFrequency;

	// Active generator volume – used in the very moment:
	private double activeVolume = currentVolume;

	// New generator frequency – used to transition between frequencies:
	private double newFrequency = currentFrequency;

	// New generator volume – used to transition between volumes:
	private double newVolume = currentVolume;

	// Fraction of cycle between samples:
	private double cycleIncrement = activeFrequency / SAMPLING_DIVISOR;

	// This property selects the type of generated wave (0 – sine…):
	private int waveType = 0;

	// The smoothness of the transition between the frequencies:
	private double smoothness = 1e3;

	// Current position of the channel (the first period starts at 0.0 and
	// ends at 2.0 * PI):
	private double currentPosition = 0.0;

	// Pointers for frequencyValues writer and reader (initially displaced
	// by the half of the buffer size):
	private int valueWriter = frequencyValues.length / 2, valueReader = 0;

	// Timer valid for frequency changer and volume adjuster:
	private int timer = 0;


	// ---------------- Saving the data to a file ----------------

	// Capture file name
	private String filename = null;

	// Temporary file instance
	private File rawOutputFile = null;

	// Capture output stream
	private FileOutputStream rawOutputStream = null;

	// Vector of buffers (beware of memory depletion)
	// private Vector<byte[]> filebuffer = null;

	// Audio format (created by initialisation)
	private AudioFormat format = null;


	/**
	 * <p>This method returns true ony if the channel is initialised
	 * correctly.</p>
	 */
	public boolean available()
	{
		return initialised && null != line && null != byteBuffer;
	}

	/**
	 * <p>This method performs the channel initialisation.</p>
	 */
	public boolean initialise()
	{
		// Exit, if this channel is initialised…
		if (initialised) return false;

		try
		{
			// Open up the audio output, using a sampling rate of
			// 44 100 Hz, 16-bit samples, mono, and big endian byte
			// ordering (see the constants defined above):
			format = new AudioFormat(SAMPLING_RATE,
				8 * SAMPLE_SIZE, CHANNELS, SIGNED, BIG_ENDIAN);

			// Ask for a buffer size of at least 2 * SINE_PACKET_SIZE:
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				format, SINE_PACKET_SIZE * 2);

			// It such data line is not available, throw the exception:
			if (!AudioSystem.isLineSupported(info))
				throw new LineUnavailableException();

			// Otherwise open and start the line:
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(format);
			line.start();
		}
		catch (LineUnavailableException e)
		{
			// (If the initialisation fails, it writes down the error
			// and returns:)
			System.err.println("Line of that type is not available...");
			e.printStackTrace();
			return false;
		}

		// Allocate buffer for the generated data:
		byteBuffer = ByteBuffer.allocate(SINE_PACKET_SIZE);

		// Initialise buffers:
		for (int i = 0; i < frequencyValues.length; ++i)
		{
			frequencyValues[i] = currentFrequency;
			volumeValues[i] = currentVolume;
		}

		// Finish the initialisation:
		if (null != frequencyChanger || null != volumeAdjuster)
		{
			valueWriter = 0;
			for (int i = frequencyValues.length / 2; i > 0; --i)
				writeValuesToBuffers();
		}

		// Set the initialised flag:
		initialised = true;

		// Adds first chunk of data:
		addChunk();

		return true;
	}

	/**
	 * <p>Closes the channel. It includes draining, stopping and closing
	 * the line.</p>
	 */
	public void close()
	{
		if (null != filename)
			closeAudioCapture();
		line.drain();
		line.close();
	}


	/**
	 * <p>Check if the data in internal line is almost drained.</p>
	 */
	public boolean almostDrained()
	{
		// Gets the number of queued samples (a line sample count)
		// in the source data line’s buffer and compares it with current
		// size of sine packet:
		return (line.getBufferSize() - line. available()) <= SINE_PACKET_SIZE;

		// It counts with less than SINE_PACKET_SIZE samples in the buffer.
		// The Buffer size is 2 * SINE_PACKET_SIZE at least, so there will
		// be room for at least SINE_PACKET_SIZE samples when this is true.
	}

	// Random noise generator:
	private final static Random noise = new Random();

	/**
	 * <p>This method computes and adds new chunk of audio data to the internal
	 * source audio data line that is currently playing.</p>
	 */
	public void addChunk()
	{
		// This should be called within the main loop of a generator.
		// It fills the available free space in the data line audio buffer.

		// Throw away samples from previous iteration:
		byteBuffer.clear();

		// Each data chunk is split to several cycles that should
		// correspond to one millisecond intervals. Frequency for single
		// millisecond itervals are read from frequencyValues buffer.
		// So the loop generates CYCLES_PER_PACKET * SAMPLES_PER_CYCLE
		// samples each based on the cycleIncrement computed regularly from
		// frequencies stored in frequencyValues buffer…

		// To prevent making CYCLES_PER_PACKET * SAMPLES_PER_CYCLE comparisons
		// and make just one, the next code is split to eleven almost
		// identical fragments – just the line generating true wave data
		// differs.
		switch (waveType)
		{
		case 0: // Sine wave:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume * // Sine:
							sin(currentPosition)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 1: // Cosine wave:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume * // Cosine:
							cos(currentPosition)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 2: // Imitative sawtooth A:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Imitative sawtooth A:
							(sin(currentPosition) / (γ +
								cos(currentPosition))) ));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 3: // Imitative sawtooth B:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Imitative sawtooth B:
							(-sin(currentPosition) / (γ +
								cos(currentPosition))) ));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 4: // Imitative sawtooth C:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Imitative sawtooth C:
							(cos(currentPosition) / (γ +
								sin(currentPosition))) ));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 5: // Imitative sawtooth D:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Imitative sawtooth D:
							(-cos(currentPosition) / (γ +
								sin(currentPosition))) ));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 6: // Multiplied imitative sawtooth A:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Multiplied imitative sawtooth A:
							((sin(currentPosition) * (γ +
								cos(currentPosition))) / δ)
							));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 7: // Multiplied imitative sawtooth B:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Multiplied imitative sawtooth B:
							((-sin(currentPosition) * (γ +
								cos(currentPosition))) / δ)
							));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 8: // Multiplied imitative sawtooth C:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Multiplied imitative sawtooth C:
							((cos(currentPosition) * (γ +
								sin(currentPosition))) / δ)
							));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 9: // Multiplied imitative sawtooth D:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// Multiplied imitative sawtooth D:
							((-cos(currentPosition) * (γ +
								sin(currentPosition))) / δ)
							));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 10: // sin * (1 - cos)
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// sin * (1 - cos)
							((sin(currentPosition) *
								(1.0 - cos(currentPosition))) / 1.3)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 11: // cos * (1 - sin)
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else
						byteBuffer.putShort((short)(activeVolume *
							// cos * (1 - sin)
							((cos(currentPosition) *
								(1.0 - sin(currentPosition))) / 1.3)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 12: // Gaussian noise:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else if (5 > currentPosition % 10)
						byteBuffer.putShort((short)(activeVolume *
							// Noisy sine:
							noise.nextGaussian() * sin(currentPosition)));
					else
						byteBuffer.putShort((short)(activeVolume *
							// Noisy cosine:
							noise.nextGaussian() * cos(currentPosition)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		case 13: // „Metal“ noise:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
						byteBuffer.putShort((short)0);
					else if (5 > currentPosition % 10)
						byteBuffer.putShort((short)(activeVolume *
							// Noisy sine:
							noise.nextDouble() * sin(currentPosition)));
					else
						byteBuffer.putShort((short)(activeVolume *
							// Noisy cosine:
							noise.nextDouble() * cos(currentPosition)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
			break;

		default: // Custom:
			for (int j = 0; j < CYCLES_PER_PACKET; ++j)
			{
				// Read current frequency from the frequencyValues buffer:
				newFrequency = frequencyValues[valueReader];

				// Read current volume from the volumeValues buffer:
				newVolume = volumeValues[valueReader];

				// Move to next value within the frequencyValues buffer:
				if (++valueReader >= frequencyValues.length)
					valueReader = 0;

				// Inner loop – create approximately one millisecond
				// long part of the sine wave:
				for (int i = 0; i < SAMPLES_PER_CYCLE; ++i)
				{
					// Update the volume value for each sample, if needed:
					if (newVolume != activeVolume)
					{
						double Δ = newVolume - activeVolume;

						if (Δ > -1 && Δ < 1)
							activeVolume = newVolume;
						else
							activeVolume += Δ / 1000;
					}

					// Put the sample to the data chunk buffer:
					if (0.0 == activeVolume)
					{
						// Just run the custom compute function…
						audioFunction.compute(currentPosition);
						byteBuffer.putShort((short)0);
					}
					else
						byteBuffer.putShort((short)(activeVolume * // Custom:
							audioFunction.compute(currentPosition)));

					// Update the frequency for each sample, if needed:
					if (newFrequency != activeFrequency)
					{
						double Δ = newFrequency - activeFrequency;

						if (0.0 == newFrequency ||
							0.0 == activeFrequency ||
							(Δ > -0.08 && Δ < 0.08))
							activeFrequency = newFrequency;
						else
							activeFrequency += Δ / smoothness;

						cycleIncrement = activeFrequency /
							SAMPLING_DIVISOR;
					}

					// Move to the next part of the sine wave
					// (if possible, i.e. if the increment is non-zero):
					currentPosition += cycleIncrement;
				}
			}
		}

		// Write sine samples to the line buffer. If the audio
		// buffer is full, this would block until there is enough
		// room, but we are not writing unless we know there is
		// enough space.
		line.write(byteBuffer.array(), 0, byteBuffer.position());

		// Write data to the temporary file
		if (null != rawOutputStream)
		{
			try
			{
				rawOutputStream.write(byteBuffer.array(),
					0, byteBuffer.position());
			}
			catch (IOException e)
			{
				if (null != rawOutputFile)
					System.err.println("Write to temporary file “" +
						rawOutputFile.getAbsolutePath() + "” failed.");
				e.printStackTrace();
			}
		}
	}


	/**
	 * <p>This is the simplest implementation of custom audio generating
	 * function. In fact, it is just sine wave generator that means it
	 * is a duplicate of the default generator mode.</p>
	 */
	public double compute(double currentPosition)
	{
		return sin(currentPosition);
	}


	/**
	 * <p>This is the frequency and volume buffer writer. It stores current
	 * values to reuse them later (within the very short period of time).
	 * It should be executed regularly for example in timer interface,
	 * otherwise the channel’s frequency and volume buffers gets drained
	 * quickly, will underflow and will provide false data. This method
	 * also uses the volume adjuster and frequency changer, if they are
	 * defined.</p>
	 */
	public void writeValuesToBuffers()
	{
		if (null != frequencyChanger)
			frequencyValues[valueWriter] =
				frequencyChanger.getFrequency(timer);
		else
			frequencyValues[valueWriter] = currentFrequency;

		if (null != volumeAdjuster)
			volumeValues[valueWriter] = currentVolume *
				volumeAdjuster.getAdjustment(timer);
		else
			volumeValues[valueWriter] = currentVolume;

		if (++valueWriter >= frequencyValues.length)
			valueWriter = 0;

		++timer;
	}

	/**
	 * <p>Checks if the channel is paused. This check is valid only if no custom
	 * frequency changer is defined. See the setFreqencyChanger method.</p>
	 */
	public boolean isPaused()
	{
		return currentVolume == 0.0 || currentFrequency == 0.0;
	}

	/**
	 * <p>Pauses the channel of generating the sound. It can be overriden by
	 * custom frequency changer – see setFreqencyChanger.</p>
	 */
	public void pause()
	{
		if (initialised)
		{
			// currentFrequency = 0.0;
			currentVolume = 0.0;
		}
	}

	/**
	 * <p>Resumes (possibly starts) the channel to generate the sound. It can be
	 * overriden by custom frequency changer – see setFreqencyChanger.</p>
	 */
	public void resume()
	{
		if (!initialised) initialise();
		currentFrequency = lastFrequency;
		currentVolume = lastVolume;
	}


	/**
	 * <p>Sets the smoothness value for the transition between the frequencies.</p>
	 */
	public void setSmoothness(double smoothness)
	{
		if (smoothness < 1.0001) this.smoothness = 1.0001;
		else this.smoothness = smoothness;
	}

	/**
	 * <p>Gets the smoothness value for the transition between the frequencies.</p>
	 */
	public double getSmoothness()
	{
		return smoothness;
	}

	/**
	 * <p>Sets the current position of the “track” played by this channel.
	 * For the default periodic implementations of sound generators it is
	 * just infinite periodic timeline… (See also getPosition.)</p>
	 */
	public void setPosition(double position)
	{
		currentPosition = position;
	}

	/**
	 * <p>Gets current position of the sound played by this channel. The
	 * value expresses current phase within the infinite periodic
	 * 2 * PI time axis and it changes over time according to currently
	 * played frequency. (See also setPosition.)</p>
	 */
	public double getPosition()
	{
		return currentPosition;
	}

	/**
	 * <p>Changes the frequency of this channel. It can be overriden by custom
	 * frequency changer – see setFreqencyChanger.</p>
	 */
	public void setFrequency(double newFrequency)
	{
		lastFrequency = newFrequency;
		if (0.0 != currentFrequency)
			currentFrequency = lastFrequency;
	}

	/**
	 * <p>Gets current frequency of this channel. This value is valid only if
	 * no custom frequency changer is defined. See the setFreqencyChanger
	 * method.</p>
	 */
	public double getFrequency()
	{
		return 0.0 == currentFrequency ? lastFrequency : currentFrequency;
	}

	/**
	 * <p>Changes the frequency of this channel using piano key number. Standard
	 * values are integers between 1 and 88, but you are allowed to use
	 * any number that will be recomputed to frequency.</p>
	 */
	public void playNote(double newNote)
	{
		setFrequency(pow(2.0, (newNote - 49.0) / 12.0) * 440.0);
		resume();
	}

	/**
	 * <p>Stops playing this channel only if entered note matches to currently
	 * playing frequency. It can be overriden by custom frequency changer –
	 * see setFreqencyChanger.</p>
	 */
	public void stopNote(double playingNote)
	{
		double frequency = 0.0 == currentFrequency ?
			lastFrequency : currentFrequency;
		double playing = pow(2.0, (playingNote - 49.0) / 12.0) * 440.0;
		if (frequency == playing) pause();
	}

	/**
	 * <p>Gets current note of this channel that is frequency recomputed to
	 * piano key number. This value is valid only if no custom frequency
	 * changer is defined. See the setFreqencyChanger method.</p>
	 */
	public double getNote()
	{
		double frequency = 0.0 == currentFrequency ?
			lastFrequency : currentFrequency;
		if (0.0 >= frequency) return 49.0;
		return (12.0 * (log10(frequency / 440.0) / log10(2.0))) + 49.0;
	}

	/**
	 * <p>Changes the volume of this channel.</p>
	 */
	public void setVolume(double newVolume)
	{
		lastVolume = Short.MAX_VALUE * (pow(2.0, newVolume) - 1.0);
		if (0.0 != currentVolume) currentVolume = lastVolume;
	}

	/**
	 * <p>Gets current volume of this channel.</p>
	 */
	public double getVolume()
	{
		return log10(((0.0 == currentVolume ? lastVolume : currentVolume) /
			Short.MAX_VALUE) + 1.0) / log10(2.0);
	}


	/**
	 * <p>Sets new type of wave:</p>
	 * 
	 * <ul>
	 * <li>0 – sine wave</li>
	 * <li>1 – cosine wave</li>
	 * <li>2 – imitative sawtooth A (function: sin / (γ + cos))</li>
	 * <li>3 – imitative sawtooth B (function: -sin / (γ + cos))</li>
	 * <li>4 – imitative sawtooth C (function: cos / (γ + sin))</li>
	 * <li>5 – imitative sawtooth D (function: -cos / (γ + sin))</li>
	 * <li>6 – multiplied imitative sawtooth A (func.: sin * (γ + cos))</li>
	 * <li>7 – multiplied imitative sawtooth B (func.: -sin * (γ + cos))</li>
	 * <li>8 – multiplied imitative sawtooth C (func.: cos * (γ + sin))</li>
	 * <li>9 – multiplied imitative sawtooth D (func.: -cos * (γ + sin))</li>
	 * <li>10 – function: sin * (1 - cos)</li>
	 * <li>11 – function: cos * (1 - sin)</li>
	 * <li>12 – gaussian noise</li>
	 * <li>13 – „metal“ noise</li>
	 * <li>other value: custom </li>
	 * </ul>
	 * 
	 * More variants of the same function were made to easily get phased
	 * (shifted) signals.
	 */
	public void setWaveType(int type)
	{
		waveType = type;
	}

	/**
	 * <p>Gets current wave type (function to generate signal). See the
	 * setWaveType method.</p>
	 */
	public int getWaveType()
	{
		return waveType;
	}

	/**
	 * <p>Sets custom type of audio function interface (implementing function
	 * used to generate signal).</p>
	 */
	public void setAudioFunction(AudioFunction function)
	{
		if (null == function)
			audioFunction = this;
		else
			audioFunction = function;

		if (audioFunction == this)
			waveType = 0; // this is the same, but effectiver
		else
			waveType = -1;
	}

	/**
	 * <p>Gets current audio function interface (implementing function to
	 * generate signal). See the setAudioFunction method.</p>
	 */
	public AudioFunction getAudioFunction()
	{
		return audioFunction;
	}


	/**
	 * <p>Changes the current timer value. The timer is valid for and used only
	 * by frequency changers (see the setFrequencyChanger method) and volume
	 * adjusters (see the setVolumeAdjuster method).</p>
	 */
	public void setTimer(int newValue)
	{
		timer = newValue;
	}

	/**
	 * <p>Gets the current timer value. See also the setTimer method.</p>
	 */
	public int getTimer()
	{
		return timer;
	}


	/**
	 * <p>Sets new frequency changer interface implementing change of channel’s
	 * frequency over time. Defining the changer disables and replaces the
	 * functionality of all following methods: isPaused, pause, resume,
	 * setFrequency, getFrequency, playNote, stopNote, and getNote.
	 * The changer should return desired frequency for each time tick that
	 * it receives and zero if the channel should be paused through that time.
	 * See also the FrequencyChanger interface.</p>
	 */
	public void setFrequencyChanger(FrequencyChanger changer)
	{
		frequencyChanger = changer;
	}

	/**
	 * <p>Gets current frequency changer interface. See the setFrequencyChanger
	 * method.</p>
	 */
	public FrequencyChanger getFrequencyChanger()
	{
		return frequencyChanger;
	}


	/**
	 * <p>Sets custom volume adjuster interface for this channel. The interface
	 * implements the adjustment of the volume level over time.
	 * See also the VolumeAdjuster interface.</p>
	 */
	public void setVolumeAdjuster(VolumeAdjuster adjuster)
	{
		volumeAdjuster = adjuster;
	}

	/**
	 * <p>Gets current volume adjuster interface.
	 * See also the setVolumeAdjuster method.</p>
	 */
	public VolumeAdjuster getVolumeAdjuster()
	{
		return volumeAdjuster;
	}


	/**
	 * <p>Opens capturing the data buffer to a file.</p>
	 */
	public boolean openAudioCapture(String filename)
	{
		boolean allOK = true;
		this.filename = filename;

		try
		{
			rawOutputFile = File.createTempFile("" +
				new Random().nextInt(), ".raw");
			rawOutputStream = new FileOutputStream(rawOutputFile);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Temporary file “" +
				rawOutputFile.getAbsolutePath() +
				"” cannot be open for write.");
			e.printStackTrace();
			allOK = false;
		}
		catch (IOException e)
		{
			System.err.println("Temporary file cannot created.");
			e.printStackTrace();
			allOK = false;
		}

		if (!allOK)
		{
			this.filename = null;
			rawOutputFile = null;
			rawOutputStream = null;
		}

		return allOK;
	}


	/**
	 * <p>Closes the capturing the data buffer to a file.</p>
	 */
	public boolean closeAudioCapture()
	{
		// Source: https://www.programcreek.com/java-api-examples/index.php?
		//         source_dir=TarsosDSP-master/src/jvm/be/tarsos/dsp/io/jvm/
		//         WaveformWriter.java
		if (null == filename) return true;

		if (null == rawOutputFile ||
			null == rawOutputStream) return false;

		boolean allOK = true;

		// try
		// {
		// 	rawOutputStream.close();
		// }
		// catch (IOException e)
		// {
		// 	e.printStackTrace();
		// }

		File outFile = new File(filename);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		AudioInputStream ais = null;

		try
		{
			// Stream the raw file
			fis = new FileInputStream(rawOutputFile);
			long lengthInSamples = rawOutputFile.length() /
				format.getFrameSize();

			// Create an audio stream form the raw file in the specified format
			ais = new AudioInputStream(fis, format, lengthInSamples);

			// Stream this to the out file
			fos = new FileOutputStream(outFile);

			// Detect type
			AudioFileFormat.Type type = null;
			String lowerName = filename.toLowerCase();

			if (lowerName.endsWith(".aiff") || lowerName.endsWith(".aif"))
				type = AudioFileFormat.Type.AIFF;
			else if (lowerName.endsWith(".au"))
				type = AudioFileFormat.Type.AU;
			else if (lowerName.endsWith(".snd"))
				type = AudioFileFormat.Type.SND;
			else
				type = AudioFileFormat.Type.WAVE;

			// Stream all the bytes to the output stream
			AudioSystem.write(ais, type, fos);
		}
		catch (IOException e)
		{
			System.err.println("Output file “" +
				outFile.getAbsolutePath() +
				"” cannot be written.");
			e.printStackTrace();
			allOK = false;
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("File format is not supported.");
			e.printStackTrace();
			allOK = false;
		}

		try
		{
			// Cleanup
			if (null != fos) fos.close();
			if (null != ais) ais.close();
			if (null != fis) fis.close();
			rawOutputStream.close();
			rawOutputFile.delete();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			allOK = false;
		}

		filename = null;
		rawOutputFile = null;
		rawOutputStream = null;
		return allOK;
	}
}
