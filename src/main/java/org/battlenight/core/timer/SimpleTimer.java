package org.battlenight.core.timer;

import org.battlenight.api.timer.Timer;
import org.battlenight.core.BattleNight;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class SimpleTimer implements Timer {

    private BattleNight plugin;
    private long time = 0;

    private long timeRemaining = 0;
    private boolean running;
    private int taskID;

    public SimpleTimer(BattleNight plugin, long time) {
        this.plugin = plugin;
        this.time = time;
        timeRemaining = this.time;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public long getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public abstract void onTimeChange(long time);

    public abstract void onTimerEnd();

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    public void start() {
        if (running) return;
        onTimeChange(timeRemaining);
        BukkitTask task = new TimerTask().runTaskTimer(plugin, 20, 20);
        taskID = task.getTaskId();
        running = true;
    }

    @Override
    public void stop() {
        if (!running) return;
        Bukkit.getScheduler().cancelTask(taskID);
        running = false;
        timeRemaining = time;
    }

    class TimerTask extends BukkitRunnable {

        @Override
        public void run() {
            timeRemaining--;

            if (timeRemaining <= 0) {
                stop();
                onTimerEnd();
                return;
            }

            onTimeChange(timeRemaining);
        }

    }

    protected BattleNight getPlugin() {
        return plugin;
    }

}
