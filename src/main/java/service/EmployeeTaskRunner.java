package service;

import io.WorkWIthExcel;
import model.EmployeeData;
import threads.EmployeeThread;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTaskRunner {
    public static void run() throws InterruptedException {
        String filePath = "src/main/resources/Data.xlsx";
        String taskSheetName = "Задачи";
        String empSheetName = "Сотрудники";

        TasksList tasksList = WorkWIthExcel.readTaskFromExcel(filePath, taskSheetName);
        List<EmployeeData> emp = WorkWIthExcel.readEmployeeDataFromExcel(filePath, empSheetName);
        List<EmployeeThread> threads = new ArrayList<>();

        System.out.println("Сотрудники начали работу" + "\n" + "-----------------------------------");
        for (int i = 0; i < emp.size(); i++) {
            EmployeeData data = emp.get(i);
            EmployeeThread thread = new EmployeeThread(data, tasksList);
            threads.add(thread);
            thread.start();
        }

        tasksList.setFinished();

        for (EmployeeThread employeeThread: threads){
            employeeThread.join();
        }

        int[] days = new int[emp.size()];
        int maxDay = 0;
        for (int i = 0; i < days.length; i++){
            days[i] = emp.get(i).getWorkDayCount();
            if (maxDay < days[i]){
                maxDay = days[i];
            }
        }

        if (maxDay % 10 == 1){
            System.out.println("-----------------------------------" + "\n" +
                    "Сотрудники закончили всю работу за " + maxDay + " день");
        } else if (maxDay % 10 == 2 || maxDay % 10 == 3 || maxDay % 10 == 4){
            System.out.println("-----------------------------------" + "\n" +
                    "Сотрудники закончили всю работу за " + maxDay + " дня");
        } else if (maxDay % 10 == 5 || maxDay % 10 == 6 || maxDay % 10 == 7 ||
                maxDay % 10 == 8 || maxDay % 10 == 9 || maxDay % 10 == 0){
            System.out.println("-----------------------------------" + "\n" +
                    "Сотрудники закончили всю работу за " + maxDay + " дней");
        }
    }
}
