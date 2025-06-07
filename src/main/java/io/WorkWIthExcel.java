package io;

import model.EmployeeData;
import model.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.TasksList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkWIthExcel {

    public static TasksList readTaskFromExcel(String filePath, String sheetName){
        TasksList tasksList = new TasksList();

        try(FileInputStream fileInputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream)){
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Task task = parseTaskRow(row);
                tasksList.addTask(task);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении Excel: " + e.getMessage());;
        }

        return tasksList;
    }

    public static List<EmployeeData> readEmployeeDataFromExcel(String filePath, String sheetName){
        List<EmployeeData> emp = new ArrayList<>();

        try (FileInputStream fileInputStream2 = new FileInputStream(filePath);
             Workbook workbook2 = new XSSFWorkbook(fileInputStream2)){
            Sheet sheet = workbook2.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                EmployeeData employeeData = parseEmployeeData(row);
                emp.add(employeeData);
            }

        } catch (IOException e){
            System.err.println("Ошибка при чтении Excel: " + e.getMessage());
        }

        return emp;
    }

    private static Task parseTaskRow(Row row){
        Cell nameCell = row.getCell(0);
        Cell durationCell = row.getCell(1);
        String name = "";
        int duration = 0;

        if (nameCell != null && nameCell.getCellType() == CellType.STRING){
            name = nameCell.getStringCellValue();
        }

        if (durationCell != null && durationCell.getCellType() == CellType.NUMERIC){
            duration = (int) durationCell.getNumericCellValue();
        }

        Task task = new Task(name, duration);
        return task;
    }

    private static EmployeeData parseEmployeeData(Row row){
        Cell idCell = row.getCell(0);
        Cell nameCell = row.getCell(1);
        int id = 0;
        String name = "";

        if (idCell != null && idCell.getCellType() == CellType.NUMERIC){
            id = (int) idCell.getNumericCellValue();
        }

        if (nameCell != null && nameCell.getCellType() == CellType.STRING){
            name = nameCell.getStringCellValue();
        }

        EmployeeData employeeData = new EmployeeData(id, name);
        return employeeData;
    }

    public synchronized static void updateExcelData(EmployeeData employeeData, String filePath,
                                                    String sheetName1, String sheetName2, int day, int eff){
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet empSheet = workbook.getSheet(sheetName1);
            Iterator<Row> rowIterator = empSheet.iterator();
            rowIterator.next();
            int currId = employeeData.getId();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell idCell = row.getCell(0);

                if (idCell != null && idCell.getCellType() == CellType.NUMERIC){
                    int id = (int) idCell.getNumericCellValue();

                    if (currId == id){
                        Cell tasksCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell dayCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell workTimeCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell idleTimeCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell effectivenessCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        tasksCell.setCellValue(employeeData.getTasksCompleted());
                        dayCell.setCellValue(employeeData.getWorkDayCount());
                        workTimeCell.setCellValue(employeeData.getWorkTime());
                        idleTimeCell.setCellValue(employeeData.getIdleTime());
                        effectivenessCell.setCellValue(employeeData.countGeneralEffectiveness(employeeData.getEffectiveness()));
                        break;
                    }
                }
            }

            Sheet effectivenessSheet = workbook.getSheet(sheetName2);
            Row row = effectivenessSheet.getRow(day);
            if (row == null){
                row = effectivenessSheet.createRow(day);
            }
            Cell dayCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            dayCell.setCellValue(day);

            int id = employeeData.getId();
            Cell effecivenessCell;
            switch (id){
                case 1:
                    effecivenessCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
                case 2:
                    effecivenessCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
                case 3:
                    effecivenessCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
                case 4:
                    effecivenessCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
                case 5:
                    effecivenessCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
                case 6:
                    effecivenessCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    effecivenessCell.setCellValue(eff);
                    break;
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
                workbook.write(fileOutputStream);
            }
        } catch(IOException e){
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }
}
