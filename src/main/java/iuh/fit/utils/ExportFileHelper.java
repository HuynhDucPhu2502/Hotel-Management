package iuh.fit.utils;

import com.dlsc.gemsfx.daterange.DateRange;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.enums.Month;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.io.File;
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

    public static void exportExcelFile(TableView<InvoiceDisplayOnTable> tabableView, ExportExcelCategory type, DateRange date, int numOfInvoice, double totalMoney){
        JFileChooser jFileChooser = new JFileChooser();
        // Thu muc luu du lieu mac dinh
        File directoryFile = new File(DATA_LOCATED);
        // Kiem tra xem thu muc ton tai chua, neu chua thi tao
        if (!directoryFile.exists()) directoryFile.mkdirs();
        switch (type){
            case ExportExcelCategory.ALL_OF_YEAR -> {
                InvoiceDisplayOnTable instance = tabableView.getItems().getFirst();
                String year = String.valueOf(instance.getCreateDate().getYear());
                File yearFolder = new File(directoryFile.getPath().concat("//").concat(year));
                if (!yearFolder.exists()) yearFolder.mkdirs();
                File allOfYearFolder = new File(yearFolder.getPath().concat("//").concat("Cả năm"));
                if (!allOfYearFolder.exists()) allOfYearFolder.mkdirs();
                jFileChooser.setCurrentDirectory(allOfYearFolder);
                jFileChooser.setSelectedFile(new File(year.concat("-TDTK-")
                        .concat(LocalDate.now().toString()).concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = jFileChooser.getSelectedFile();
                    if(fileToSave != null){
                        String filePath = fileToSave.getAbsolutePath();
                        createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                        openExcelFile(filePath);
                    }else{
                        throw new IllegalArgumentException("Errors");
                    }
                }else{
                    System.out.println("nothing");
                }

            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = String.valueOf(invoiceInstance.getCreateDate().getMonth());
                File yearFoler = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFoler.exists()) yearFoler.mkdirs();
                File monthFolder = new File(yearFoler.getPath()
                        .concat("//")
                        .concat(Month.valueOf(month).getName()));
                if(!monthFolder.exists()) monthFolder.mkdirs();
                File allOfMonth = new File(monthFolder.getPath()
                        .concat("//")
                        .concat("Cả tháng"));
                if(!allOfMonth.exists()) allOfMonth.mkdirs();
                jFileChooser.setCurrentDirectory(allOfMonth);
                jFileChooser.setSelectedFile(new File(Month.valueOf(month).getName()
                        .concat("-" + year)
                        .concat("-TDTK-")
                        .concat(LocalDate.now().toString())
                        .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = jFileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = String.valueOf(invoiceInstance.getCreateDate().getMonth());
                String day = String.valueOf(invoiceInstance.getCreateDate().getDayOfMonth());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File monthFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(Month.valueOf(month).getName()));
                if(!monthFolder.exists()) monthFolder.mkdirs();
                File dayFolder = new File(monthFolder.getPath()
                        .concat("//")
                        .concat(day)
                        .concat("-" + invoiceInstance.getCreateDate().getMonthValue() + "-" + year));
                if(!dayFolder.exists()) dayFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dayFolder);
                jFileChooser.setSelectedFile(new File(day.concat("-" + invoiceInstance.getCreateDate().getMonthValue() + "-" + year)
                        .concat("-TDTK-").concat(LocalDate.now().toString())
                        .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(fromYear + " - " + toYear));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File dateRangeFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(date.getStartDate() + " đến " + date.getEndDate()));
                if(!dateRangeFolder.exists()) dateRangeFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dateRangeFolder);
                jFileChooser.setSelectedFile(new File(
                        date.getStartDate().toString() + " đến " + date.getEndDate().toString()
                                .concat("-TDTK-").concat(LocalDate.now().toString())
                                .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File dateRangeFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(date.getStartDate().toString() + " đến " + date.getEndDate().toString()));
                if(!dateRangeFolder.exists()) dateRangeFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dateRangeFolder);
                jFileChooser.setSelectedFile(new File(
                         date.getStartDate().toString() + " đến " + date.getEndDate().toString()
                        .concat("-TDTK-").concat(LocalDate.now().toString())
                        .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            default -> {
                System.out.println("errors");
            }
        }
    }

    public static void exportExcelFileForEmployee(TableView<InvoiceDisplayOnTable> tabableView, ExportExcelCategory type, boolean forEmployee, DateRange date, int numOfInvoice, double totalMoney){
        JFileChooser jFileChooser = new JFileChooser();
        // Thu muc luu du lieu mac dinh
        File directoryFile = new File(DATA_LOCATED);
        // Kiem tra xem thu muc ton tai chua, neu chua thi tao
        if (!directoryFile.exists()) directoryFile.mkdirs();
        switch (type){
            case ExportExcelCategory.ALL_OF_YEAR -> {
                InvoiceDisplayOnTable instance = tabableView.getItems().getFirst();
                String year = String.valueOf(instance.getCreateDate().getYear());
                File yearFolder = new File(directoryFile.getPath().concat("//").concat(year));
                if (!yearFolder.exists()) yearFolder.mkdirs();
                if(forEmployee){
                    String employeeName = instance.getEmpName();
                    File employeeFolder = new File(yearFolder.getPath()
                            .concat("//")
                            .concat("Nhân viên"));
                    if(!employeeFolder.exists()) employeeFolder.mkdirs();
                    File employeeNameFolder = new File(employeeFolder.getPath()
                            .concat("//")
                            .concat(employeeName));
                    if(!employeeNameFolder.exists()) employeeNameFolder.mkdirs();
                    File allOfYearFolder = new File(employeeNameFolder.getPath().concat("//").concat("Cả năm"));
                    if (!allOfYearFolder.exists()) allOfYearFolder.mkdirs();
                    jFileChooser.setCurrentDirectory(allOfYearFolder);
                    jFileChooser.setSelectedFile(new File(employeeName + " - " + year.concat("-TDTK-")
                            .concat(LocalDate.now().toString()).concat(".xlsx")));
                }else{
                    File allOfYearFolder = new File(yearFolder.getPath().concat("//").concat("Cả năm"));
                    if (!allOfYearFolder.exists()) allOfYearFolder.mkdirs();
                    jFileChooser.setCurrentDirectory(allOfYearFolder);
                    jFileChooser.setSelectedFile(new File(year.concat("-TDTK-")
                            .concat(LocalDate.now().toString()).concat(".xlsx")));
                }
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = jFileChooser.getSelectedFile();
                    if(fileToSave != null){
                        String filePath = fileToSave.getAbsolutePath();
                        createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                        openExcelFile(filePath);
                    }else{
                        throw new IllegalArgumentException("Errors");
                    }
                }else{
                    System.out.println("nothing");
                }

            }
            case ExportExcelCategory.ALL_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = String.valueOf(invoiceInstance.getCreateDate().getMonth());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                if(forEmployee){
                    String employeeName = invoiceInstance.getEmpName();
                    File employeeFolder = new File(yearFolder.getPath()
                            .concat("//")
                            .concat("Nhân viên"));
                    if(!employeeFolder.exists()) employeeFolder.mkdirs();
                    File employeeNameFolder = new File(employeeFolder.getPath()
                            .concat("//")
                            .concat(employeeName));
                    if(!employeeNameFolder.exists()) employeeNameFolder.mkdirs();
                    File monthFolder = new File(employeeNameFolder.getPath()
                            .concat("//")
                            .concat(Month.valueOf(month).getName()));
                    if(!monthFolder.exists()) monthFolder.mkdirs();
                    jFileChooser.setCurrentDirectory(monthFolder);
                    jFileChooser.setSelectedFile(new File(employeeName + " - " + Month.valueOf(month).getName().concat("-TDTK-")
                            .concat(LocalDate.now().toString()).concat(".xlsx")));
                }else{
                    File monthFolder = new File(yearFolder.getPath()
                            .concat("//")
                            .concat(Month.valueOf(month).getName()));
                    if(!monthFolder.exists()) monthFolder.mkdirs();
                    jFileChooser.setCurrentDirectory(monthFolder);
                    jFileChooser.setSelectedFile(new File(Month.valueOf(month).getName()
                            .concat("-" + year)
                            .concat("-TDTK-")
                            .concat(LocalDate.now().toString())
                            .concat(".xlsx")));
                }
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = jFileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.DAY_OF_MONTH -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                String month = String.valueOf(invoiceInstance.getCreateDate().getMonth());
                String day = String.valueOf(invoiceInstance.getCreateDate().getDayOfMonth());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File monthFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(Month.valueOf(month).getName()));
                if(!monthFolder.exists()) monthFolder.mkdirs();
                File dayFolder = new File(monthFolder.getPath()
                        .concat("//")
                        .concat(day)
                        .concat("-" + invoiceInstance.getCreateDate().getMonthValue() + "-" + year));
                if(!dayFolder.exists()) dayFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dayFolder);
                jFileChooser.setSelectedFile(new File(day.concat("-" + invoiceInstance.getCreateDate().getMonthValue() + "-" + year)
                        .concat("-TDTK-").concat(LocalDate.now().toString())
                        .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.MANY_YEAR -> {
                String fromYear = String.valueOf(date.getStartDate().getYear());
                String toYear = String.valueOf(date.getEndDate().getYear());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(fromYear + " - " + toYear));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File dateRangeFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(date.getStartDate() + " đến " + date.getEndDate()));
                if(!dateRangeFolder.exists()) dateRangeFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dateRangeFolder);
                jFileChooser.setSelectedFile(new File(
                        date.getStartDate().toString() + " đến " + date.getEndDate().toString()
                                .concat("-TDTK-").concat(LocalDate.now().toString())
                                .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
                }
            }
            case ExportExcelCategory.DATE_RANGE -> {
                InvoiceDisplayOnTable invoiceInstance = tabableView.getItems().getFirst();
                String year = String.valueOf(invoiceInstance.getCreateDate().getYear());
                File yearFolder = new File(directoryFile.getPath()
                        .concat("//")
                        .concat(year));
                if(!yearFolder.exists()) yearFolder.mkdirs();
                File dateRangeFolder = new File(yearFolder.getPath()
                        .concat("//")
                        .concat(date.getStartDate().toString() + " đến " + date.getEndDate().toString()));
                if(!dateRangeFolder.exists()) dateRangeFolder.mkdirs();
                jFileChooser.setCurrentDirectory(dateRangeFolder);
                jFileChooser.setSelectedFile(new File(
                        date.getStartDate().toString() + " đến " + date.getEndDate().toString()
                                .concat("-TDTK-").concat(LocalDate.now().toString())
                                .concat(".xlsx")));
                int userSelection = jFileChooser.showSaveDialog(null);
                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File saveFile = jFileChooser.getSelectedFile();
                    String filePath = saveFile.getAbsolutePath();
                    createExcelFile(tabableView, filePath, numOfInvoice, totalMoney);
                    openExcelFile(filePath);
                }else{
                    System.out.println("nothing");
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
