package Item.Time;

/**
 * Created by Yamaa on 2019/3/20.
 */

public class Pomodoro {
    private Integer work;
    private Integer relax;

    public Integer getRelax() {
        return relax;
    }

    public void setRelax(int relax) {
        this.relax = relax;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public Integer getWork() {
        return work;
    }

    public Pomodoro(){
        work = null;
        relax = null;
    }

    public int getTotal(){
        return work+relax;
    }

    public Pomodoro(int work,int relax){
        this.work = work;
        this.relax = relax;
    }
}
