package Item.Time;

/**
 * Created by Yamaa on 2019/3/20.
 */

public class MyTime {
    private int hours;
    private int mins;

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getHours() {

        return hours;
    }

    public int getMins() {
        return mins;
    }

    public static MyTime add(MyTime time, TimeAmount timeAmount){
        int hours = time.hours + timeAmount.getMinutes()/60;
        int mins = time.mins + timeAmount.getMinutes()%60;

        return new MyTime(hours, mins);
    }

    public static TimeAmount getGap(MyTime time1, MyTime time2){
        return new TimeAmount( (time1.getHours()-time2.getHours())* 60 + time1.getMins() - time2.getMins() );
    }

    public MyTime(int hours, int mins){
        this.hours = hours;
        this.mins = mins;
    }

    public String toString(){
        if(mins<10){
            return hours + ":0" + mins;
        }
        return hours + ":" + mins;
    }
}
