package thbt.webng.com.game.info;

public class DigitalClockModel {

    private int timeInSeconds;

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    int[] getDigitValues() {
        int seconds = timeInSeconds % 60;
        int minutes = (timeInSeconds / 60) % 60;
        int hours = (timeInSeconds / 60) / 60;

        return new int[]{seconds % 10, seconds / 10, minutes % 10, minutes / 10, hours % 10};
    }
}
