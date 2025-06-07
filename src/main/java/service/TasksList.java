package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksList {
    private final List<Task> tasks = new ArrayList<>();
    private boolean isFinished = false;

    public void addTask(Task task){
        synchronized (tasks){
            tasks.add(task);
            tasks.notifyAll();
        }
    }

    public Task getNextTask() throws InterruptedException {
        synchronized (tasks){
            while (tasks.isEmpty() && !isFinished){
                tasks.wait();
            }
            if (tasks.isEmpty()){
                return null;
            } else {
                return tasks.removeFirst();
            }
        }
    }

    public synchronized void setFinished(){
        isFinished = true;
        this.notifyAll();
    }
}
