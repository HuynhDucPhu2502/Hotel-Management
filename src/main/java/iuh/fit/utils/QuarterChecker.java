package iuh.fit.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuarterChecker {
    public static final List<Integer> FIRST_QUATER = new ArrayList<>(Arrays.asList(1, 2, 3));
    public static final List<Integer> SECOND_QUATER = new ArrayList<>(Arrays.asList(4, 5, 6));
    public static final List<Integer> THIRD_QUATER = new ArrayList<>(Arrays.asList(7, 8, 9));
    public static final List<Integer> FORTH_QUATER = new ArrayList<>(Arrays.asList(10, 11, 12));

    public static boolean isQuarter(LocalDateTime invoiceCreatedDate, String quarter, boolean isNoneValue){
        if(isNoneValue) return false;
        int monthOfInvoice = invoiceCreatedDate.getMonthValue();
        switch (quarter){
            case "1" -> {
                return FIRST_QUATER.contains(monthOfInvoice);
            }
            case "2" -> {
                return SECOND_QUATER.contains(monthOfInvoice);
            }
            case "3" -> {
                return THIRD_QUATER.contains(monthOfInvoice);
            }
            case "4" -> {
                return FORTH_QUATER.contains(monthOfInvoice);
            }
            default -> {
                return false;
            }
        }
    }

    public static boolean isQuarter(int month, String quarter, boolean isNoneValue){
        // xuat ca 12 thang
        if(isNoneValue) return true;
        switch (quarter){
            case "1" -> {
                return FIRST_QUATER.contains(month);
            }
            case "2" -> {
                return SECOND_QUATER.contains(month);
            }
            case "3" -> {
                return THIRD_QUATER.contains(month);
            }
            case "4" -> {
                return FORTH_QUATER.contains(month);
            }
            default -> {
                return false;
            }
        }
    }
}
