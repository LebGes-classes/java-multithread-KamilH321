package model;

import java.util.List;

public class EmployeeData {
    private int id;
    private String name;
    private volatile int workTime;
    private volatile int idleTime;
    private volatile int workDayCount;
    private volatile int tasksCompleted;
    private volatile List<Integer> effectiveness;

    public EmployeeData(int id, String name){
        this.id = id;
        this.name = name;
    }

    // подсчёт общей эффективности за все рабочие дни
    public double countGeneralEffectiveness(List<Integer> effectiveness){
        double generalEffectiveness;
        double sumEffectiveness = 0;
        if (effectiveness != null){
            for (double eff: effectiveness){
                sumEffectiveness += eff;
            }
            generalEffectiveness = sumEffectiveness / workDayCount;
            return  generalEffectiveness;
        }
        return 0;
    }

    public int getId() {return id;}

    public void setWorkTime(int workTime) {this.workTime = workTime;}

    public void setIdleTime(int idleTime) {this.idleTime = idleTime;}

    public void setWorkDayCount(int workDayCount) {this.workDayCount = workDayCount;}

    public void setTasksCompleted(int tasksCompleted) {this.tasksCompleted = tasksCompleted;}

    public int getIdleTime() {return idleTime;}

    public int getTasksCompleted() {return tasksCompleted;}

    public int getWorkDayCount() {return workDayCount;}

    public int getWorkTime() {return workTime;}

    public String getName() {return name;}

    public List<Integer> getEffectiveness() {return effectiveness;}

    public void setEffectiveness(List<Integer> effectiveness) {this.effectiveness = effectiveness;}

    @Override
    public String toString() {
        return "model.EmployeeData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workTime=" + workTime +
                ", idleTime=" + idleTime +
                ", workDayCount=" + workDayCount +
                ", tasksCompleted=" + tasksCompleted +
                ", effectiveness=" + effectiveness +
                '}';
    }
}
