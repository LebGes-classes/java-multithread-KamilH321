package threads;

import io.WorkWIthExcel;
import model.EmployeeData;
import model.Task;
import service.TasksList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeThread extends Thread{
    private final EmployeeData data; // данные сотрудника
    private final TasksList tasksList; // список задач
    private volatile boolean isRunning = true;

    public EmployeeThread(EmployeeData data, TasksList tasksList){
        this.data = data;
        this.tasksList = tasksList;
    }

    @Override
    public void run(){
        List<Integer> effectivenessList = new ArrayList<>();
        while (isRunning){
            try {
                Task task = tasksList.getNextTask();

                // если задачи закончились заканчиваем работу
                if (task == null){
                    isRunning = false;
                    break;
                }

                String filePath = "src/main/resources/Data.xlsx";
                int duration = task.getDuration();
                int dayCount = 0;
                while (duration != 0){
                    Random random = new Random();
                    int workDay = 8;
                    int workWithRest = 6;
                    int effectiveness = 0;
                    // если задача занимает больше одного рабочего дня + отдых
                    if (duration > workWithRest) {
                        int workTime = random.nextInt(1) + 5;
                        data.setWorkTime(data.getWorkTime() + workTime);
                        data.setIdleTime(data.getIdleTime() + (workWithRest - workTime));
                        duration -= workTime;
                        effectiveness += workTime * 100 / workWithRest; // подсчёт эффективности за день
                        effectivenessList.add(effectiveness);

                        System.out.println("Работник " + data.getName() +
                                " выполняет задачу: " + task.getName() + ". Время выполнения за сегодня: " + workWithRest + " ч.");

                        Thread.sleep(workTime * 100);
                        data.setWorkDayCount(data.getWorkDayCount() + 1);
                        dayCount++;
                        WorkWIthExcel.updateExcelData(data, filePath, "Сотрудники", "Эффективность", data.getWorkDayCount(), effectiveness);
                    } else {
                        data.setWorkTime(data.getWorkTime() + duration);
                        data.setIdleTime(data.getIdleTime() + (workDay - duration));
                        data.setTasksCompleted(data.getTasksCompleted() + 1);
                        effectiveness += duration * 100 / workWithRest; // подсчёт эффективности за день
                        effectivenessList.add(effectiveness);
                        data.setEffectiveness(effectivenessList);

                        System.out.println("Работник " + data.getName() +
                                " выполняет задачу: " + task.getName() + ". Время выполнения за сегодня: " + duration + " ч.");

                        Thread.sleep(duration * 100);
                        data.setWorkDayCount(data.getWorkDayCount() + 1);
                        dayCount++;
                        WorkWIthExcel.updateExcelData(data, filePath, "Сотрудники", "Эффективность", data.getWorkDayCount(), effectiveness);
                        duration = 0;

                        if (dayCount % 10 == 1){
                            System.out.println("Работник " + data.getName() +
                                    " выполнил задачу: " + task.getName() + " за " + dayCount + " день");
                        } else if (dayCount % 10 == 2 || dayCount % 10 == 3 || dayCount % 10 == 4){
                            System.out.println("Работник " + data.getName() +
                                    " выполнил задачу: " + task.getName() + " за " + dayCount + " дня");
                        } else if (dayCount % 10 == 5 || dayCount % 10 == 6 || dayCount % 10 == 7 ||
                                dayCount % 10 == 8 || dayCount % 10 == 9 || dayCount % 10 == 0){
                            System.out.println("Работник " + data.getName() +
                                    " выполнил задачу: " + task.getName() + " за " + dayCount + " дней");
                        }

                    }
                }


            } catch (InterruptedException e){
                isRunning = false;
            }
        }
    }

    public void stopRunning(){
        isRunning = false;
    }
}
