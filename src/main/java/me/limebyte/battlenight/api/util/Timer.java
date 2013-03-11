package me.limebyte.battlenight.api.util;

public interface Timer {

    public void start();

    public void stop();

    public long getTime();

    public void setTime(long time);

    public long getTimeRemaining();

    public void setTimeRemaining(long time);

    public boolean isRunning();

}
