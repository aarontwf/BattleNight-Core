package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Timer {

    private long time = 0;
    private long timeRemaining = 0;
    private boolean running;
    private int taskID;

    public Timer(long time) {
        this.time = time * 10;
        this.timeRemaining = this.time;
    }

    public void start() {
        BukkitTask task = new TimerTask().runTaskTimer(BattleNight.instance, 2, 2);
        taskID = task.getTaskId();
        running = true;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
        running = false;
        onTimerEnd();
        timeRemaining = time;
    }

    public long getTime() {
        return time / 10;
    }

    public void setTime(long time) {
        this.time = time * 10;
    }

    public long getTimeRemaining() {
        return timeRemaining / 10;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining * 10;
    }

    public boolean isRunning() {
        return running;
    }

    class TimerTask extends BukkitRunnable {

        public void run() {
            timeRemaining--;

            if (timeRemaining <= 0) {
                stop();
                return;
            }

            onTimeChange(timeRemaining);
        }

    }

    public abstract void onTimeChange(long time);

    public abstract void onTimerEnd();

}
