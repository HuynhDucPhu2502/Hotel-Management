package iuh.fit.utils;

import com.dlsc.gemsfx.daterange.DateRange;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.enums.Month;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ExportFileHelper {
    private static final String DATA_LOCATED = "D://Data";

    public static void createExcelFile(TableView<InvoiceDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < tableView.getColumns().size(); i++)
                headerRow.createCell(i).setCellValue(
                        tableView.getColumns().get(i).getText()
                );

            ObservableList<InvoiceDisplayOnTable> data = tableView.getItems();
            for(int i = 0; i < tableView.getItems().size(); i++){
                Row contentRow = sheet.createRow(i + 1);
                InvoiceDisplayOnTable inv = data.get(i);
                contentRow.createCell(0).setCellValue(inv.getInvoiceID());
                contentRow.createCell(1).setCellValue(inv.getCusName());
                contentRow.createCell(2).setCellValue(inv.getRoomID());
                contentRow.createCell(3).setCellValue(inv.getEmpName());
                contentRow.createCell(4).setCellValue(inv.getCreateDate());
                contentRow.createCell(5).setCellValue(inv.getDeposit());
                contentRow.createCell(6).setCellValue(inv.getServiceCharge());
                contentRow.createCell(7).setCellValue(inv.getRoomCharge());
                contentRow.createCell(8).setCellValue(inv.getTax());
                contentRow.createCell(9).setCellValue(inv.getNetDue());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size()+1);
            statisticRow.createCell(6).setCellValue("Số bản ghi");
            statisticRow.createCell(7).setCellValue(numOfInvoice);
            statisticRow.createCell(8).setCellValue("Tổng tiền");
            statisticRow.createCell(9).setCellValue(totalMoney);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void creatUsingRoomExcelFile(TableView<UsingRoomDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < tableView.getColumns().size(); i++)
                headerRow.createCell(i).setCellValue(
                        tableView.getColumns().get(i).getText()
                );

            ObservableList<UsingRoomDisplayOnTable> data = tableView.getItems();
            for(int i = 0; i < tableView.getItems().size(); i++){
                Row contentRow = sheet.createRow(i + 1);
                UsingRoomDisplayOnTable inv = data.get(i);

                contentRow.createCell(0).setCellValue(inv.getRoomID());
                contentRow.createCell(1).setCellValue(inv.getCusName());
                contentRow.createCell(2).setCellValue(inv.getEmpName());
                contentRow.createCell(3).setCellValue(inv.getCreateDate());
                contentRow.createCell(4).setCellValue(inv.getDeposit());
                contentRow.createCell(5).setCellValue(inv.getServiceCharge());
                contentRow.createCell(6).setCellValue(inv.getRoomCharge());
                contentRow.createCell(7).setCellValue(inv.getTax());
                contentRow.createCell(8).setCellValue(inv.getNetDue());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size()+1);
            statisticRow.createCell(5).setCellValue("Số lần sử dụng phòng");
            statisticRow.createCell(6).setCellValue(numOfInvoice);
            statisticRow.createCell(7).setCellValue("Tổng tiền");
            statisticRow.createCell(8).setCellValue(totalMoney);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void creatUsingRoomDetailExcelFile(TableView<UsingRoomDetailDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < tableView.getColumns().size(); i++)
                headerRow.createCell(i).setCellValue(
                        tableView.getColumns().get(i).getText()
                );

            ObservableList<UsingRoomDetailDisplayOnTable> data = tableView.getItems();
            for(int i = 0; i < tableView.getItems().size(); i++){
                Row contentRow = sheet.createRow(i + 1);
                UsingRoomDetailDisplayOnTable inv = data.get(i);

                contentRow.createCell(0).setCellValue(inv.getRoomID());
                contentRow.createCell(1).setCellValue(inv.getTimesUsing());
                contentRow.createCell(3).setCellValue(inv.getNetDue());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size()+1);
            statisticRow.createCell(5).setCellValue("Số lần sử dụng phòng");
            statisticRow.createCell(6).setCellValue(numOfInvoice);
            statisticRow.createCell(7).setCellValue("Tổng tiền");
            statisticRow.createCell(8).setCellValue(totalMoney);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void exportExcelFile(TableView<InvoiceDisplayOnTable> tableView, ExportExcelCategory type, boolean forEmployee, DateRange date, int numOfInvoice, double totalMoney){
        FileChooser fileChooser = new FileChooser();
        File directoryFile = new File(DATA_LOCATED);
        if (!directoryFile.exists()) directoryFile.mkdirs();

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        switch (type){
            case ExportExcelCategory.ALL_OF_YEAR -> {
                InvoiceDisplayOnTable instance = tableView.getItems().getFirst();
                String year = String.valueOf(instance.getCreateDate().getYear());

                File yearFolder = new File(directoryFile.getPath() + "//" + year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = instance.getEmpName();
                    File employeeFolder = new File(yearFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = invoiceInstance.getCreateDate().getMonth().toString();

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getEmpName();
                    File employeeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName());
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    fileChooser.setInitialDirectory(employeeFolder);
                    fileChooser.setInitialFileName(employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    fileChooser.setInitialDirectory(monthFolder);
                    fileChooser.setInitialFileName(Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = invoiceInstance.getCreateDate().getMonth().toString();
                String day = String.valueOf(invoiceInstance.getCreateDate().getDayOfMonth());

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getEmpName();
                    File dayFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(day + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                InvoiceDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                File yearFolder = new File(directoryFile, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getEmpName();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                InvoiceDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String year = String.valueOf(date.getStartDate().getYear());
                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getEmpName();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            default -> {
                System.out.println("errors");
            }
        }
    }

    public static void exportUsingRoomExcelFile(TableView<UsingRoomDisplayOnTable> tableView, ExportExcelCategory type, boolean forEmployee, DateRange date, int numOfInvoice, double totalMoney){
        FileChooser fileChooser = new FileChooser();
        File directoryFile = new File(DATA_LOCATED);
        if (!directoryFile.exists()) directoryFile.mkdirs();

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        switch (type){
            case ExportExcelCategory.ALL_OF_YEAR -> {
                UsingRoomDisplayOnTable instance = tableView.getItems().getFirst();
                String year = String.valueOf(instance.getCreateDate().getYear());

                File yearFolder = new File(directoryFile.getPath() + "//" + year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = instance.getRoomID();
                    File employeeFolder = new File(yearFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                UsingRoomDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = invoiceInstance.getCreateDate().getMonth().toString();

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File employeeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName());
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    fileChooser.setInitialDirectory(employeeFolder);
                    fileChooser.setInitialFileName(employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    fileChooser.setInitialDirectory(monthFolder);
                    fileChooser.setInitialFileName(Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                UsingRoomDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = invoiceInstance.getCreateDate().getMonth().toString();
                String day = String.valueOf(invoiceInstance.getCreateDate().getDayOfMonth());

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dayFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(day + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                UsingRoomDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                File yearFolder = new File(directoryFile, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                UsingRoomDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String year = String.valueOf(date.getStartDate().getYear());
                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            default -> {
                System.out.println("errors");
            }
        }
    }

    public static void exportUsingRoomDetailExcelFile(TableView<UsingRoomDetailDisplayOnTable> tableView, ExportExcelCategory type, boolean forEmployee, DateRange date, int numOfInvoice, double totalMoney){
        FileChooser fileChooser = new FileChooser();
        File directoryFile = new File(DATA_LOCATED);
        if (!directoryFile.exists()) directoryFile.mkdirs();

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        switch (type){
            case ExportExcelCategory.ALL_OF_YEAR -> {
                UsingRoomDetailDisplayOnTable instance = tableView.getItems().getFirst();
                String year = String.valueOf(LocalDate.now().getYear());

                File yearFolder = new File(directoryFile.getPath() + "//" + year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = instance.getRoomID();
                    File employeeFolder = new File(yearFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath() + "//Cả năm");
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomDetailExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                UsingRoomDetailDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(LocalDate.now().getYear());
                String month = LocalDate.now().getMonth().toString();

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File employeeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName());
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    fileChooser.setInitialDirectory(employeeFolder);
                    fileChooser.setInitialFileName(employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    fileChooser.setInitialDirectory(monthFolder);
                    fileChooser.setInitialFileName(Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomDetailExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                UsingRoomDetailDisplayOnTable invoiceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(LocalDate.now().getYear());
                String month = LocalDate.now().getMonth().toString();
                String day = String.valueOf(LocalDate.now().getDayOfMonth());

                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dayFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngay " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    fileChooser.setInitialDirectory(dayFolder);
                    fileChooser.setInitialFileName(day + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomDetailExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                UsingRoomDetailDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                File yearFolder = new File(directoryFile, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomDetailExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                UsingRoomDetailDisplayOnTable invoiceInstance = tableView.getItems().get(0);
                String year = String.valueOf(date.getStartDate().getYear());
                File yearFolder = new File(directoryFile, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                if (!forEmployee) {
                    String employeeName = invoiceInstance.getRoomID();
                    File dateRangeFolder = new File(yearFolder, "Nhân viên/" + employeeName + "/" + date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(employeeName + " - TDTK-" + LocalDate.now() + ".xlsx");
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    fileChooser.setInitialDirectory(dateRangeFolder);
                    fileChooser.setInitialFileName(date.getStartDate() + " đến " + date.getEndDate() + "-TDTK-" + LocalDate.now() + ".xlsx");
                }

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    creatUsingRoomDetailExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            default -> {
                System.out.println("errors");
            }
        }
    }

    private static void openExcelFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);
                } else System.out.println("Desktop không hỗ trợ mở file.");
            } else System.out.println("File không tồn tại.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
