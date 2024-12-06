package iuh.fit.utils;

import com.dlsc.gemsfx.daterange.DateRange;
import iuh.fit.controller.features.statistics.InvoiceRevenueStatisticsTabController;
import iuh.fit.controller.features.statistics.RoomRevenueStatisticsTabController;
import iuh.fit.controller.features.statistics.ServiceRevenueStatisticsTabController;
import iuh.fit.controller.features.statistics.StatisticalController;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.enums.Month;
import iuh.fit.models.wrapper.*;
import iuh.fit.security.PreferencesKey;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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
import java.util.Objects;

public class ExportFileHelper {
    private static final String DATA_LOCATED = "D://Thống kê doanh thu";

    public static void createInvoiceExcelFile(TableView<InvoiceDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
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
                contentRow.createCell(4).setCellValue(inv.getCreateDate().toString());
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
                throw new IllegalArgumentException(e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException ignored) {

                }
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static void createServiceExcelFile(TableView<ServiceDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row rowHeader = sheet.createRow(0);
            for(int i = 0; i < tableView.getColumns().size(); i++){
                rowHeader.createCell(i).setCellValue(
                        tableView.getColumns().get(i).getText()
                );
            }

            ObservableList<ServiceDisplayOnTable> data = tableView.getItems();
            for(int i = 0; i < tableView.getItems().size(); i++){
                Row record = sheet.createRow(i + 1);
                ServiceDisplayOnTable instance = data.get(i);
                record.createCell(0).setCellValue(instance.getServiceId());
                record.createCell(1).setCellValue(instance.getServiceName());
                record.createCell(2).setCellValue(instance.getServiceCategory());
                record.createCell(3).setCellValue(instance.getEmployeeName());
                record.createCell(4).setCellValue(instance.getDateAdded().toString());
                record.createCell(5).setCellValue(instance.getQuantity());
                record.createCell(6).setCellValue(instance.getUnitPrice());
                record.createCell(7).setCellValue(instance.getTotalMoney());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size() + 1);
            statisticRow.createCell(4).setCellValue("Số bản ghi");
            statisticRow.createCell(5).setCellValue(numOfInvoice);
            statisticRow.createCell(6).setCellValue("Tổng tiền");
            statisticRow.createCell(7).setCellValue(totalMoney);

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                workbook.write(fileOutputStream);
            }catch (Exception e){
                throw new IllegalArgumentException(e.getMessage());
            }finally {
                try {
                    workbook.close();
                }catch (Exception ignored){

                }
            }
        }catch (Exception ignored){

        }
    }

    public static void createRoomExcelFile(TableView<RoomDisplayOnTable> tableView, String filePath, int numOfInvoice, double totalMoney){
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row rowHeader = sheet.createRow(0);
            for(int i = 0; i < tableView.getColumns().size(); i++){
                rowHeader.createCell(i).setCellValue(
                        tableView.getColumns().get(i).getText()
                );
            }

            ObservableList<RoomDisplayOnTable> data = tableView.getItems();
            for(int i = 0; i < tableView.getItems().size(); i++){
                Row record = sheet.createRow(i + 1);
                RoomDisplayOnTable instance = data.get(i);
                record.createCell(0).setCellValue(instance.getRoomID());
                record.createCell(1).setCellValue(instance.getRoomCategory());
                record.createCell(2).setCellValue(instance.getNumOfPeople());
                record.createCell(3).setCellValue(instance.getBookingDate());
                record.createCell(4).setCellValue(instance.getCheckInDate().toString());
                record.createCell(5).setCellValue(instance.getCheckOutDate());
                record.createCell(6).setCellValue(instance.getTotalMoney());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size() + 1);
            statisticRow.createCell(3).setCellValue("Số bản ghi");
            statisticRow.createCell(4).setCellValue(numOfInvoice);
            statisticRow.createCell(5).setCellValue("Tổng tiền");
            statisticRow.createCell(6).setCellValue(totalMoney);

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                workbook.write(fileOutputStream);
            }catch (Exception e){
                throw new IllegalArgumentException(e.getMessage());
            }finally {
                try {
                    workbook.close();
                }catch (Exception ignored){

                }
            }
        }catch (Exception ignored){

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

                contentRow.createCell(0).setCellValue(inv.getRoomCategoryID());
                contentRow.createCell(1).setCellValue(inv.getNameRoomCategory());
                contentRow.createCell(2).setCellValue(inv.getRoomID());
                contentRow.createCell(3).setCellValue(inv.getCusName());
                contentRow.createCell(4).setCellValue(inv.getEmpName());
                contentRow.createCell(5).setCellValue(inv.getCreateDate());
                contentRow.createCell(6).setCellValue(inv.getDeposit());
                contentRow.createCell(7).setCellValue(inv.getServiceCharge());
                contentRow.createCell(8).setCellValue(inv.getRoomCharge());
                contentRow.createCell(9).setCellValue(inv.getNetDue());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size()+1);
            statisticRow.createCell(6).setCellValue("Số lần sử dụng phòng");
            statisticRow.createCell(7).setCellValue(numOfInvoice);
            statisticRow.createCell(8).setCellValue("Tổng tiền");
            statisticRow.createCell(9).setCellValue(totalMoney);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException ignored) {

                }
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
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

                contentRow.createCell(0).setCellValue(inv.getRoomCategoryID());
                contentRow.createCell(1).setCellValue(inv.getTimesUsing());
                contentRow.createCell(2).setCellValue(inv.getPercentUsing());
                contentRow.createCell(3).setCellValue(inv.getNetDue());
                contentRow.createCell(4).setCellValue(inv.getPercentNetDue());
            }

            Row statisticRow = sheet.createRow(tableView.getItems().size()+1);
            statisticRow.createCell(5).setCellValue("Số lần sử dụng phòng");
            statisticRow.createCell(6).setCellValue(numOfInvoice);
            statisticRow.createCell(7).setCellValue("Tổng tiền");
            statisticRow.createCell(8).setCellValue(totalMoney);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException egnored) {

                }
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static void exportInvoiceExcelFile(TableView<InvoiceDisplayOnTable> tableView, ExportExcelCategory type, boolean forEmployee, DateRange date, int numOfInvoice, double totalMoney){
        FileChooser fileChooser = new FileChooser();

        String fileAddress = FilePathManager.getPath(PreferencesKey.EXPORT_INVOICE_STATISTIC, PreferencesKey.DEFAULT_FILE_PATH);

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File serviceRevenueFolder = new File(fileAddress.concat("//Doanh thu hóa đơn"));
        if (!serviceRevenueFolder.exists()) serviceRevenueFolder.mkdirs();

        switch (type){
            case ExportExcelCategory.ALL_OF_TIME -> {
                File totalRevenueFolder = new File(serviceRevenueFolder, "//Tổng danh thu");
                if (!totalRevenueFolder.exists()) totalRevenueFolder.mkdirs();

                InvoiceDisplayOnTable invoiceInstance = tableView.getItems().getFirst();

                File saveFolder;
                String initialFileName;

                if(!forEmployee) {
                    String employeeName = invoiceInstance.getEmpName();
                    File employeeFolder = new File(totalRevenueFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + InvoiceRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + InvoiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(totalRevenueFolder.getPath());
                    initialFileName = InvoiceRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + InvoiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_YEAR -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getCreateDate().getYear());

                File yearFolder = new File(serviceRevenueFolder.getPath() + "//" + year + "//Cả năm");
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = serviceInstance.getEmpName();
                    File employeeFolder = new File(yearFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath());
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getCreateDate().getYear());
                String month = serviceInstance.getCreateDate().getMonth().toString();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = serviceInstance.getEmpName();
                    File employeeFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Nhân viên/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    saveFolder = new File(monthFolder.getPath());
                    initialFileName = Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.QUARTER -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getCreateDate().getYear());
                int month = serviceInstance.getCreateDate().getMonthValue();
                String quarter;

                if(QuarterChecker.FIRST_QUATER.contains(month)) quarter = "Quý 1";
                else if(QuarterChecker.SECOND_QUATER.contains(month)) quarter = "Quý 2";
                else if(QuarterChecker.THIRD_QUATER.contains(month)) quarter = "Quý 3";
                else quarter = "Quý 4";

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = serviceInstance.getEmpName();
                    File employeeFolder = new File(yearFolder, quarter + "/Nhân viên/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + quarter + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File quarterFolder = new File(yearFolder, quarter);
                    if (!quarterFolder.exists()) quarterFolder.mkdirs();

                    saveFolder = new File(quarterFolder.getPath());
                    initialFileName = quarter + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getCreateDate().getYear());
                String month = serviceInstance.getCreateDate().getMonth().toString();
                String day = String.valueOf(serviceInstance.getCreateDate().getDayOfMonth());

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    String employeeName = serviceInstance.getEmpName();
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName()
                            + "/Ngày " + day
                            + "/Nhân viên/" + employeeName + "/");
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngày " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = day + "-" + serviceInstance.getCreateDate().getMonth().getValue() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                String employeeName = serviceInstance.getEmpName();

                File yearFolder = new File(serviceRevenueFolder, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Nhân viên/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                InvoiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(date.getStartDate().getYear());

                String employeeName = serviceInstance.getEmpName();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forEmployee) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Nhân viên/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createInvoiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
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

    public static void exportServiceExcelFile(TableView<ServiceDisplayOnTable> tableView, ExportExcelCategory type, boolean forEmployee, boolean forService, DateRange date, int numOfInvoice, double totalMoney) throws IOException {
        FileChooser fileChooser = new FileChooser();

        String fileAddress = FilePathManager.getPath(PreferencesKey.EXPORT_SERVICE_STATISTIC, PreferencesKey.DEFAULT_FILE_PATH);

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File serviceRevenueFolder = new File(fileAddress.concat("//Doanh thu dịch vụ"));
        if (!serviceRevenueFolder.exists()) serviceRevenueFolder.mkdirs();

        switch (type){
            case ExportExcelCategory.ALL_OF_TIME -> {
                File totalRevenueFolder = new File(serviceRevenueFolder, "//Tổng danh thu");
                if (!totalRevenueFolder.exists()) totalRevenueFolder.mkdirs();
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String employeeName = serviceInstance.getEmployeeName();
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if(!forEmployee){
                        serviceCategoryFolder = new File(totalRevenueFolder.getPath() + "//Loại dịch vụ//" + serviceValue + "//Nhân viên//" + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + " - " + ServiceRevenueStatisticsTabController.allOfYears.getFirst()
                                + " - " + ServiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                    }

                    else{
                        serviceCategoryFolder = new File(totalRevenueFolder.getPath() + "//Loại dịch vụ//" + serviceValue);
                        initialFileName = serviceValue + " - " + ServiceRevenueStatisticsTabController.allOfYears.getFirst()
                                + " - " + ServiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                    }

                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if(!forEmployee) {
                    String employeeName = serviceInstance.getEmployeeName();
                    File employeeFolder = new File(totalRevenueFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + ServiceRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + ServiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(totalRevenueFolder.getPath());
                    initialFileName = ServiceRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + ServiceRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_YEAR -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getDateAdded().getYear());

                File yearFolder = new File(serviceRevenueFolder.getPath() + "//" + year + "//Cả năm");
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String employeeName = serviceInstance.getEmployeeName();
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//Loại dịch vụ//" + serviceValue + "//Nhân viên//" + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + "-TDTK-" + LocalDate.now();
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//Loại dịch vụ//" + serviceValue);
                        initialFileName = serviceValue + "-TDTK-" + LocalDate.now();
                    }

                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    String employeeName = serviceInstance.getEmployeeName();
                    File employeeFolder = new File(yearFolder.getPath() + "//Nhân viên//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath());
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getDateAdded().getYear());
                String month = serviceInstance.getDateAdded().getMonth().toString();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String employeeName = serviceInstance.getEmployeeName();
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + Month.valueOf(month).getName()
                                + "//Loại dịch vụ//"
                                + serviceValue
                                + "//Nhân viên//"
                                + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + "-TDTK-" + LocalDate.now();
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + Month.valueOf(month).getName()
                                + "//Loại dịch vụ//"
                                + serviceValue);
                        initialFileName = serviceValue + "-TDTK-" + LocalDate.now();
                    }
                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    String employeeName = serviceInstance.getEmployeeName();
                    File employeeFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Nhân viên/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    saveFolder = new File(monthFolder.getPath());
                    initialFileName = Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.QUARTER -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getDateAdded().getYear());
                int month = serviceInstance.getDateAdded().getMonthValue();
                String quarter;

                if(QuarterChecker.FIRST_QUATER.contains(month)) quarter = "Quý 1";
                else if(QuarterChecker.SECOND_QUATER.contains(month)) quarter = "Quý 2";
                else if(QuarterChecker.THIRD_QUATER.contains(month)) quarter = "Quý 3";
                else quarter = "Quý 4";

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String employeeName = serviceInstance.getEmployeeName();
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + quarter
                                + "//Loại dịch vụ//"
                                + serviceValue
                                + "//Nhân viên//"
                                + employeeName);
                        initialFileName = serviceValue + "-"
                                + employeeName + " - "
                                + quarter + "-TDTK-"
                                + LocalDate.now() + ".xlsx";
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + quarter
                                + "//Loại dịch vụ//"
                                + serviceValue);
                        initialFileName = serviceValue + "-"
                                + quarter + "-TDTK-"
                                + LocalDate.now() + ".xlsx";
                    }
                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    String employeeName = serviceInstance.getEmployeeName();
                    File employeeFolder = new File(yearFolder, quarter + "/Nhân viên/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + quarter + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File quarterFolder = new File(yearFolder, quarter);
                    if (!quarterFolder.exists()) quarterFolder.mkdirs();

                    saveFolder = new File(quarterFolder.getPath());
                    initialFileName = quarter + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getDateAdded().getYear());
                String month = serviceInstance.getDateAdded().getMonth().toString();
                String day = String.valueOf(serviceInstance.getDateAdded().getDayOfMonth());

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String employeeName = serviceInstance.getEmployeeName();
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + Month.valueOf(month).getName()
                                + "/Ngày " + day
                                + "//Loại dịch vụ//"
                                + serviceValue
                                + "//Nhân viên//"
                                + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + "-TDTK-" + LocalDate.now();
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath() + "//"
                                + Month.valueOf(month).getName()
                                + "/Ngày " + day
                                + "//Loại dịch vụ//"
                                + serviceValue);
                        initialFileName = serviceValue + "-TDTK-" + LocalDate.now();
                    }
                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    String employeeName = serviceInstance.getEmployeeName();
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName()
                            + "/Ngày " + day
                            + "/Nhân viên/" + employeeName + "/");
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngày " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = day + "-" + serviceInstance.getDateAdded().getMonth().getValue() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                String employeeName = serviceInstance.getEmployeeName();

                File yearFolder = new File(serviceRevenueFolder, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath(),
                                date.getStartDate() + " đến " + date.getEndDate()
                                + "//Loại dịch vụ//" + serviceValue
                                + "//Nhân viên//" + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + "-TDTK-" + LocalDate.now();
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath(),
                                date.getStartDate() + " đến " + date.getEndDate()
                                + "//Loại dịch vụ//" + serviceValue);
                        initialFileName = serviceValue + "-TDTK-" + LocalDate.now();
                    }

                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Nhân viên/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                ServiceDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(date.getStartDate().getYear());

                String employeeName = serviceInstance.getEmployeeName();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forService) {
                    String serviceValue = serviceInstance.getServiceCategory();
                    File serviceCategoryFolder;
                    if (!forEmployee) {
                        serviceCategoryFolder = new File(yearFolder.getPath(),
                                date.getStartDate() + " đến " + date.getEndDate()
                                        + "//Loại dịch vụ//" + serviceValue
                                        + "//Nhân viên//" + employeeName);
                        initialFileName = serviceValue + " - " + employeeName + "-TDTK-" + LocalDate.now();
                    } else {
                        serviceCategoryFolder = new File(yearFolder.getPath(),
                                date.getStartDate() + " đến " + date.getEndDate()
                                        + "//Loại dịch vụ//" + serviceValue);
                        initialFileName = serviceValue + "-TDTK-" + LocalDate.now();
                    }

                    if (!serviceCategoryFolder.exists()) serviceCategoryFolder.mkdirs();

                    saveFolder = new File(serviceCategoryFolder.getPath());
                } else if (!forEmployee) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Nhân viên/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createServiceExcelFile(tableView, filePath, numOfInvoice, totalMoney);
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

    public static void exportRoomExcelFile(TableView<RoomDisplayOnTable> tableView, ExportExcelCategory type, boolean forRoomCategory, DateRange date, int numOfInvoice, double totalMoney) throws IOException {
        FileChooser fileChooser = new FileChooser();

        String fileAddress = FilePathManager.getPath(PreferencesKey.EXPORT_ROOM_STATISTIC, PreferencesKey.DEFAULT_FILE_PATH);

        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File serviceRevenueFolder = new File(fileAddress.concat("//Doanh thu phòng"));
        if (!serviceRevenueFolder.exists()) serviceRevenueFolder.mkdirs();

        switch (type){
            case ExportExcelCategory.ALL_OF_TIME -> {
                File totalRevenueFolder = new File(serviceRevenueFolder, "//Tổng danh thu");
                if (!totalRevenueFolder.exists()) totalRevenueFolder.mkdirs();
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();

                File saveFolder;
                String initialFileName;

                if(!forRoomCategory) {
                    String employeeName = serviceInstance.getRoomCategory();
                    File employeeFolder = new File(totalRevenueFolder.getPath() + "//Loại phòng//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + RoomRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + RoomRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(totalRevenueFolder.getPath());
                    initialFileName = RoomRevenueStatisticsTabController.allOfYears.getFirst()
                            + " - " + RoomRevenueStatisticsTabController.allOfYears.getLast() + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_YEAR -> {
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getBookingDate().getYear());

                File yearFolder = new File(serviceRevenueFolder.getPath() + "//" + year + "//Cả năm");
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    String employeeName = serviceInstance.getRoomCategory();
                    File employeeFolder = new File(yearFolder.getPath() + "//Loại phòng//" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());

                    initialFileName = employeeName + " - " + year + "-TDTK-" + LocalDate.now();
                } else {
                    saveFolder = new File(yearFolder.getPath());
                    if (!saveFolder.exists()) saveFolder.mkdirs();

                    initialFileName = year + "-TDTK-" + LocalDate.now();
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);

                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getBookingDate().getYear());
                String month = serviceInstance.getBookingDate().getMonth().toString();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    String employeeName = serviceInstance.getRoomCategory();
                    File employeeFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Loại phòng/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + Month.valueOf(month).getName() + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File monthFolder = new File(yearFolder, Month.valueOf(month).getName());
                    if (!monthFolder.exists()) monthFolder.mkdirs();

                    saveFolder = new File(monthFolder.getPath());
                    initialFileName = Month.valueOf(month).getName() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.QUARTER -> {
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getBookingDate().getYear());
                int month = serviceInstance.getBookingDate().getMonthValue();
                String quarter;

                if(QuarterChecker.FIRST_QUATER.contains(month)) quarter = "Quý 1";
                else if(QuarterChecker.SECOND_QUATER.contains(month)) quarter = "Quý 2";
                else if(QuarterChecker.THIRD_QUATER.contains(month)) quarter = "Quý 3";
                else quarter = "Quý 4";

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    String employeeName = serviceInstance.getRoomCategory();
                    File employeeFolder = new File(yearFolder, quarter + "/Loại phòng/" + employeeName);
                    if (!employeeFolder.exists()) employeeFolder.mkdirs();

                    saveFolder = new File(employeeFolder.getPath());
                    initialFileName = employeeName + " - " + quarter + "-TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File quarterFolder = new File(yearFolder, quarter);
                    if (!quarterFolder.exists()) quarterFolder.mkdirs();

                    saveFolder = new File(quarterFolder.getPath());
                    initialFileName = quarter + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String year = String.valueOf(serviceInstance.getBookingDate().getYear());
                String month = serviceInstance.getBookingDate().getMonth().toString();
                String day = String.valueOf(serviceInstance.getBookingDate().getDayOfMonth());

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    String employeeName = serviceInstance.getRoomCategory();
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName()
                            + "/Ngày " + day
                            + "/Loại phòng/" + employeeName + "/");
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dayFolder = new File(yearFolder, Month.valueOf(month).getName() + "/Ngày " + day);
                    if (!dayFolder.exists()) dayFolder.mkdirs();

                    saveFolder = new File(dayFolder.getPath());
                    initialFileName = day + "-" + serviceInstance.getBookingDate().getMonth().getValue() + "-" + year + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                RoomDisplayOnTable serviceInstance = tableView.getItems().getFirst();
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());

                String employeeName = serviceInstance.getRoomCategory();

                File yearFolder = new File(serviceRevenueFolder, fromYear + "-" + toYear);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Loại phòng/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                } else {
                    System.out.println("No file selected.");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                RoomDisplayOnTable roomInstance = tableView.getItems().getFirst();
                String year = String.valueOf(date.getStartDate().getYear());

                String employeeName = roomInstance.getRoomCategory();

                File yearFolder = new File(serviceRevenueFolder, year);
                if (!yearFolder.exists()) yearFolder.mkdirs();

                File saveFolder;
                String initialFileName;

                if (!forRoomCategory) {
                    File dateRangeFolder = new File(yearFolder,
                            date.getStartDate() + " đến " + date.getEndDate()
                                    + "/Loại phòng/" + employeeName);
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = employeeName + " - TDTK-" + LocalDate.now() + ".xlsx";
                } else {
                    File dateRangeFolder = new File(yearFolder, date.getStartDate() + " đến " + date.getEndDate());
                    if (!dateRangeFolder.exists()) dateRangeFolder.mkdirs();

                    saveFolder = new File(dateRangeFolder.getPath());
                    initialFileName = date.getStartDate() + " đến " + date.getEndDate()
                            + "-TDTK-" + LocalDate.now() + ".xlsx";
                }

                fileChooser.setInitialDirectory(saveFolder);
                fileChooser.setInitialFileName(initialFileName);

                File userSelection = fileChooser.showSaveDialog(null);
                if (userSelection != null) {
                    String filePath = userSelection.getAbsolutePath();
                    createRoomExcelFile(tableView, filePath, numOfInvoice, totalMoney);
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
                    String employeeName = instance.getRoomCategoryID();
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
                    String employeeName = invoiceInstance.getRoomCategoryID();
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
                    String employeeName = invoiceInstance.getNameRoomCategory();
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
                    String employeeName = invoiceInstance.getNameRoomCategory();
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
                    String employeeName = invoiceInstance.getNameRoomCategory();
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
