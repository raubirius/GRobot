
package knižnica.podpora;

// Toto je jedna z externých podporných tried, ku ktorým nie je priamo
// vyhotovená dokumentácia. Je rozšírením pôvodného balíčka generator
// slúžiaceho na generovanie zvukov v reálnom čase. Licencia a zdroje sú
// uvedené nižšiev anglickom jazyku.
// 
// (Above Slovak note tells the students the basics about this class and its
// licensing. — This source code is released under the same terms as the whole
// package knižnica.)

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.Timer;

/**
 * <p>This class extends a Channel class adding some scheduling
 * functionality.</p>
 * 
 * @author Roman Horváth
 * @version 16. 10. 2017
 * 
 * @exclude
 */
public class BeepChannel extends Channel implements ActionListener
{
	// Internal schedule record
	private class Schedule
	{
		private int duration;
		private double frequency, volume;

		public Schedule(int duration, double frequency, double volume)
		{
			this.duration = duration;
			this.frequency = frequency;
			this.volume = volume;
		}
	}

	// Internal schedule
	private Vector<Schedule> schedule;

	// Internal timer instance
	private Timer timer;

	// Action countdown
	private int delay = 0;

	// Get next schedule record flag
	private boolean next = false;

	/** <p>The constructor.</p> */
	public BeepChannel()
	{
		initialise();
		schedule = new Vector<Schedule>();
		timer = new Timer(1, this);
		timer.start();
	}

	private static long nanoTime1a = System.nanoTime();
	// private static long nanoTime1b = nanoTime1a;

	/** <p>Timer tick handler…</p> */
	public void actionPerformed(ActionEvent evt)
	{
		long nanoTime2a = System.nanoTime();
		// System.out.println("System.nanoTime() A: " + // nanoTime1a + " " +
		// 	((double)(nanoTime2a - nanoTime1a) / 1_000_000.0) + " ms");
		int time = (int)((nanoTime2a - nanoTime1a) / 1000000);
		nanoTime1a = nanoTime2a;

		if (delay > 0)
		{
			// System.out.print("Delay: " + delay + " (" + ((double)delay /
			// 	1000.0) + " s); time: " + time + " (" + ((double)time /
			// 	1000.0) + " s)");

			delay -= time;
			if (delay <= 0) next = true;

			// System.out.println("; reduced: " + delay + " (" + ((double)delay /
			// 	1000.0) + " s); next: " + next);
		}

		if (next)
		{
			// long nanoTime2b = System.nanoTime();
			// System.out.println("System.nanoTime() B: " + // nanoTime1b + " " +
			// 	((double)(nanoTime2b - nanoTime1b) / 1_000_000_000.0) + " s");
			// nanoTime1b = nanoTime2b;
			// System.out.println("schedule.isEmpty(): " + schedule.isEmpty());

			if (schedule.isEmpty())
			{
				next = false;
				delay = 0;
				pause();
			}
			else
			{
				Schedule current = schedule.remove(0);
				if (current.duration >= 0) delay += current.duration;
				if (current.frequency >= 0) setFrequency(current.frequency);
				if (current.volume >= 0) setVolume(current.volume);
				next = delay <= 0;

				// System.out.println("  Frequency: " + getFrequency());
				// System.out.println("  Volume: " + getVolume());
				// System.out.println("  Delay: " + delay);
				// System.out.println("  Next: " + next);
			}
		}

		writeValuesToBuffers();
		if (almostDrained()) addChunk();
	}

	/** <p>Sets new delay value.</p> */
	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	/** <p>Add new schedule record.</p> */
	public void addSchedule(int duration, double frequency, double volume)
	{
		if (!next && delay <= 0 && schedule.isEmpty())
		{
			// System.out.println("Activate: ");

			next = false;
			if (duration >= 0) delay = duration; else next = true;
			if (frequency >= 0) setFrequency(frequency);
			if (volume >= 0) setVolume(volume);

			// System.out.println("  Frequency: " + getFrequency());
			// System.out.println("  Volume: " + getVolume());
			// System.out.println("  Delay: " + delay);
			// System.out.println("  Next: " + next);
		}
		else
		{
			// System.out.println("Schedule: ");

			schedule.add(new Schedule(duration, frequency, volume));
			if (!next && delay <= 0) next = true;

			// System.out.println("  Frequency: " + frequency);
			// System.out.println("  Volume: " + volume);
			// System.out.println("  Duration: " + duration);
			// System.out.println("  Next: " + next);
		}

		resume();
	}

	/** <p>Clears the schedule.</p> */
	public void clearSchedule()
	{
		schedule.clear();
	}

	/** <p>Override of original close method – stops the timer first.</p> */
	public void close()
	{
		timer.stop();
		super.close();
	}
}
