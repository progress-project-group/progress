package Item.Time;

/**
 * Created by Yamaa on 2019/3/20.
 */

public class TimeAmount {
    private int minutes;

    private Pomodoro pomodoro;
    private  int pomodoroNums;

    public static void lala(){};

    public String getRealtime(){
        int hours = minutes/60;
        int mins = minutes%60;

        String hourString = hours + "小时";
        String minString = mins + "分钟";

        if(hours!=0&&mins!=0){
            return hourString + ' '+ minString;
        }else if(hours!=0){
            return hourString;
        }else{
            return minString;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public int getPomodoroNums() {
        return pomodoroNums;
    }

    public TimeAmount(int minutes){
        this.minutes = minutes;
    }

    public TimeAmount(Pomodoro pomodoro, int pomodoroNums){
        this.pomodoro = pomodoro;
        this.pomodoroNums = pomodoroNums;

        this.minutes = pomodoro.getTotal()*pomodoroNums;
    }

    //还需要一个用户设置时间，让系统自动配置番茄钟的构造函数
    //public TimeAmount(int minutes, Boolean flag)
    //public TimeAmount(int minutes, Pomodoro pomodoro)
}
