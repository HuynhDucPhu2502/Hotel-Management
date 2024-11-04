package iuh.fit.utils;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class EditDateRangePicker {
    public static void editDateRangePicker(DateRangePicker dateRangePicker){
        dateRangePicker.getDateRangeView().presetTitleProperty().set("Thời điểm thống kê");
        dateRangePicker.getDateRangeView().toTextProperty().set("Đến");
        dateRangePicker.getDateRangeView().cancelTextProperty().set("Hủy");
        dateRangePicker.getDateRangeView().applyTextProperty().set("Áp dụng");

        dateRangePicker.setValue(
                new DateRange("Hôm nay", LocalDate.now())
        );

        dateRangePicker.getDateRangeView().getPresets().set(
                0,
                new DateRangePreset("Hôm nay", () ->
                        new DateRange("Hôm nay", LocalDate.now())
                )
        );

        dateRangePicker.getDateRangeView().getPresets().set(
                1,
                new DateRangePreset("Hôm qua", () ->
                        new DateRange("Hôm qua", LocalDate.now().minusDays(1))
                )
        );

        dateRangePicker.getDateRangeView().getPresets().set(
                2,
                new DateRangePreset("Tuần này", () -> {
                    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
                    LocalDate startOfWeek = LocalDate.now().with(fieldISO, 1); // Ngày đầu tuần
                    LocalDate endOfWeek = startOfWeek.plusDays(6); // Ngày cuối tuần
                    return new DateRange("Tuần này", startOfWeek, endOfWeek);
                })
        );

        dateRangePicker.getDateRangeView().getPresets().set(
                3,
                new DateRangePreset("Tháng này", () -> {
                    LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                    return new DateRange("Tháng này", start, end);
                })
        );

        dateRangePicker.getDateRangeView().getPresets().set(
                4,
                new DateRangePreset("Tháng trước", () -> {
                    LocalDate start = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate end = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                    return new DateRange("Tháng trước", start, end);
                })
        );
    }
}
