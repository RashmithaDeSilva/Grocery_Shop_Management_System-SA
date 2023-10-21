package util.features;

public class Features {
    private int maximumUserCount = 7;
    private boolean lockerLogAvailability  = true;


    public int getMaximumUserCount() {
        return maximumUserCount;
    }

    public void setMaximumUserCount(int maximumUserCount) {
        this.maximumUserCount = maximumUserCount;
    }

    public boolean isLockerLogAvailability() {
        return lockerLogAvailability;
    }

    public void setLockerLogAvailability(boolean lockerLogAvailability) {
        this.lockerLogAvailability = lockerLogAvailability;
    }
}
