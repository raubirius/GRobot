
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
		if (delay > 0) --delay;
		else if (0 == delay)
		{
			--delay;
			if (schedule.isEmpty()) pause();
			else
			{
				Schedule current = schedule.remove(0);
				if (current.duration >= 0) delay = current.duration;
				else delay = 0;
				if (current.frequency >= 0) setFrequency(current.frequency);
				if (current.volume >= 0) setVolume(current.volume);
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
		if (delay < 0 && schedule.isEmpty())
		{
			if (duration >= 0) delay = duration; else delay = 0;
			if (frequency >= 0) setFrequency(frequency);
			if (volume >= 0) setVolume(volume);
		}
		else
		{
			schedule.add(new Schedule(duration, frequency, volume));
			if (delay < 0) delay = 0;
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
