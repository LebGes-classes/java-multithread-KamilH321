package model;

public class Task {

    private final String name;
    private final int duration;

    public Task(String name, int duration){
        this.name = name;
        this.duration = duration;
    }

    public String getName() { return name; }

    public int getDuration() { return duration; }

    @Override
    public String toString() {
        return "model.Task{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
