package Item.Time;

public class TimePeriod {
    private MyTime startTime;
    private MyTime endTime;

    public TimePeriod(MyTime startTime, MyTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(MyTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(MyTime endTime) {
        this.endTime = endTime;
    }

    public MyTime getStartTime() {
        return startTime;
    }

    public MyTime getEndTime() {
        return endTime;
    }
}
