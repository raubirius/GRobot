
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

	// System timer (“stopwatch”) for exact time measurements
	private static long nanoTime1 = System.nanoTime();

	/** <p>The constructor.</p> */
	public BeepChannel()
	{
		initialise();
		schedule = new Vector<Schedule>();
		timer = new Timer(1, this);
		timer.start();
	}

	/** <p>Timer tick handler…</p> */
	public void actionPerformed(ActionEvent evt)
	{
		// Measure the elapsed time
		long nanoTime2 = System.nanoTime();
		int time = (int)((nanoTime2 - nanoTime1) / 1000000);
		nanoTime1 = nanoTime2;

		if (delay > 0)
		{
			delay -= time;
			if (delay <= 0) next = true;
		}

		if (next)
		{
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
			next = false;
			if (duration >= 0) delay = duration; else next = true;
			if (frequency >= 0) setFrequency(frequency);
			if (volume >= 0) setVolume(volume);
		}
		else
		{
			schedule.add(new Schedule(duration, frequency, volume));
			if (!next && delay <= 0) next = true;
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
