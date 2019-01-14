package cn.dbw.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	  public static String DATE_FORMAT = "yyyy-MM-dd";

	    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	    public static String DATE_FORMAT_CHINESE = "yyyyƒÍM‘¬d»’";


 
    public static String getCurrentDate() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }
}
