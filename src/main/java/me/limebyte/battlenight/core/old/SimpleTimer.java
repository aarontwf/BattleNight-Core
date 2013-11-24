package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class SimpleTimer implements Timer {

    private long time = 0;

    private long timeRemaining = 0;
    private boolean running;
    private int taskID;

    public SimpleTimer(long time) {
        this.time = time * 10;
        timeRemaining = this.time;
    }

    @Override
    public long getTime() {
        return time / 10;
    }

    @Override
    public long getTimeRemaining() {
        return timeRemaining / 10;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public abstract void onTimeChange(long time);

    public abstract void onTimerEnd();

    @Override
    public void setTime(long time) {
        this.time = time * 10;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining * 10;
    }

    @Override
    public void start() {
        BukkitTask task = new TimerTask().runTaskTimer(BattleNight.instance, 2, 2);
        taskID = task.getTaskId();
        running = true;
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
        running = false;
        timeRemaining = time;
    }

    class TimerTask extends BukkitRunnable {

        @Override
        public void run() {
            timeRemaining--;

            if (timeRemaining <= 0) {
                onTimerEnd();
                stop();
                return;
            }

            onTimeChange(timeRemaining);
        }

    }

}
