package writer;

import entity.Table;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TableWriter {
    private static final String DATE_FORMAT;

    static {
        DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss_z";
    }

    public abstract boolean write(Table table);

    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }
}
