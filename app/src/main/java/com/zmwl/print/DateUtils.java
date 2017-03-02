package com.zmwl.print;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 周旭 on 2017/2/9.
 * "日期转换工具类"
 */

public class DateUtils {
    /**
     * 把毫秒级的时间转化为标准时间
     *
     * @param millesTime 当前系统时间
     * @return
     * @throws ParseException
     */
    public static String getStandardDate(long millesTime) throws ParseException {
        Date currentTime = new Date(millesTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(currentTime);
        return date;
    }

    /**
     * @param time 毫秒级的时间
     * @return 返回年月日的数组
     * @throws ParseException
     */
    public static int[] getYearMonthDay(long time) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getStandardDate(time));
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        int year = now.get(Calendar.YEAR); //年
        int month = now.get(Calendar.MONTH) + 1; //月
        int day = now.get(Calendar.DAY_OF_MONTH); //日
        return new int[]{year, month, day};
    }
}
