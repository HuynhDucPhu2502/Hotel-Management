package iuh.fit.models.enums;

public enum ExportExcelCategory {
    ALL_OF_TIME("All of time"),
    ALL_OF_YEAR("All of year"),
    ALL_OF_MONTH("All of month"),
    DAY_OF_MONTH("Day of month"),
    MANY_YEAR("Date range many year"),
    QUARTER("Quarter"),
    DATE_RANGE("Date range");

    private final String exportType;

    ExportExcelCategory(String exportType){
        this.exportType = exportType;
    }

    @Override
    public String toString() {
        return exportType;
    }
}
