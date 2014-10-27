package com.msolo.stockeye.service.common;

import android.util.Log;

import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by mSolo on 2014/8/12.
 */
class UtilCalendar {

    private static final UtilCalendar INSTANCE = new UtilCalendar();

    protected static UtilCalendar getInstance() {
        return INSTANCE;
    }

    protected boolean checkIsWorkDay() {

        GregorianCalendar cal = new GregorianCalendar();
        return checkIsWorkDay(cal);

    }

    protected String[] getDateArrayOfWeek(String date) {

        String[] dateArray = new String[7];
        GregorianCalendar cal = new GregorianCalendar();

        setCalendarToWeekStartDate(date, cal);

        dateArray[0] = String.format("%tF", cal).replace("-", "");
        for (int i=1; i<7; i++) {
            cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            dateArray[i] = String.format("%tF", cal).replace("-", "");
        }

        return dateArray;

    }

    protected String getDateString(int dateInt) {

        String dateStr = String.valueOf(dateInt);

        return new StringBuilder().append(dateStr.substring(0, 4)).append("-")
                .append(dateStr.substring(4, 6)).append("-")
                .append(dateStr.substring(6, 8))
                .toString();

    }

    protected String[] getDownloadTransactionDateArray(boolean isNeedAddStartDate, String startDate) {

        LinkedList<String> dateList = new LinkedList<String>();

        // start to generate transaction date array
        GregorianCalendar cal = new GregorianCalendar();
        cal.set( Integer.parseInt(startDate.substring(0, 4)),
                 Integer.parseInt(startDate.substring(4, 6)) - 1,
                 Integer.parseInt(startDate.substring(6, 8)) );

        if (isNeedAddStartDate) {
            dateList.add( String.format("%tF", cal) );
        }

        int RefreshingDate = Integer.parseInt( getRefreshingDate() );

        while (true) {

            cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            if ( checkIsWorkDay(cal) ) {
                dateList.add( String.format("%tF", cal) );

                String date = String.format("%tF", cal);
                if ( Integer.parseInt(date.replace("-", "")) > RefreshingDate ) {
                    break;
                }

            }

        }

        int size = dateList.size();
        String[] dateArray = new String[size];
        for (int i=0; i<size; i++) {
            dateArray[i] = dateList.get(i);
        }

        return dateArray;

    }

    protected String getLastWeekStartDate(String date) {

        GregorianCalendar cal = new GregorianCalendar();
        setCalendarToWeekStartDate(date, cal);

        cal.add(GregorianCalendar.WEEK_OF_YEAR, -1);

        return String.format("%tF", cal).replace("-", "");

    }

    protected int[] getMonthArrayByQuarter(int quarter) {

        switch (quarter) {
            case 1:
                return new int[]{1, 2, 3};
            case 2:
                return new int[]{4, 5, 6};
            case 3:
                return new int[]{7, 8, 9};
            case 4:
                return new int[]{10, 11, 12};
            default:
                // should not go to here
                return null;
        }

    }

    protected String getNextWeekStartDate(String date) {

        GregorianCalendar cal = new GregorianCalendar();
        setCalendarToWeekStartDate(date, cal);

        cal.add(GregorianCalendar.WEEK_OF_YEAR, 1);

        return String.format("%tF", cal).replace("-", "");

    }

    protected int getQuarter(int month) {

        if (month < 4) {
            return 1;
        } else if (month < 7) {
            return 2;
        } else if (month < 10) {
            return 3;
        }

        return 4;

    }

    protected String getRefreshingDate() {

        GregorianCalendar cal = new GregorianCalendar();
        GregorianCalendar calAssist = new GregorianCalendar(
                cal.get(GregorianCalendar.YEAR),
                cal.get(GregorianCalendar.MONTH),
                cal.get(GregorianCalendar.DAY_OF_MONTH),
                0, 0, 0);

        int currentDateTimeBySecs = (int)( (cal.getTimeInMillis() - calAssist.getTimeInMillis()) / 1000l );

        // check if current time < 18:20
        if (currentDateTimeBySecs < 66000) {
            cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
        }

        boolean isWeekend =
                cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY ||
                cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY;

        if (isWeekend) {
            cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
            if (cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY) {
                cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
                return String.format("%tF", cal).replace("-", "");
            }

            return String.format("%tF", cal).replace("-", "");

        } else {
            return String.format("%tF", cal).replace("-", "");
        }

    }

    protected int getTodayTimeElapseBySecond() {

        GregorianCalendar cal = new GregorianCalendar();
        GregorianCalendar calAssistant = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),
                cal.get(GregorianCalendar.MONTH),
                cal.get(GregorianCalendar.DAY_OF_MONTH),
                0, 0, 0);

        int todayTimeSecondElapse = (int) ( cal.getTimeInMillis() - calAssistant.getTimeInMillis() );

        // 09:25 - 00:00 = 33900000
        // 15:05 - 00:00 = 54300000
        // 14:50 - 00:00 = 53451795
        // weekend, New Year, May Day, National Day
        // > 15:05
        // < 9:25


        return todayTimeSecondElapse;

    }

    protected String getToday() {
        return String.format("%tF", new GregorianCalendar());
    }

    protected int getWeekday(String date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)) - 1,
                Integer.parseInt(date.substring(6, 8)));

        return cal.get(GregorianCalendar.DAY_OF_WEEK) - 1;

    }

    protected int getWeekIndexOfYear(String date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)) - 1,
                Integer.parseInt(date.substring(6, 8)));

        int weekOfYear = cal.get(GregorianCalendar.WEEK_OF_YEAR);

        cal.add(GregorianCalendar.DAY_OF_YEAR, -7);
        int preWeekOfYear = cal.get(GregorianCalendar.WEEK_OF_YEAR);
        if (preWeekOfYear > weekOfYear) {
            return preWeekOfYear + 1;
        }

        return weekOfYear;

    }

    private boolean checkIsWorkDay(GregorianCalendar cal) {

        boolean workdayFlag =
                cal.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY &&
                        cal.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY;

        boolean holidayFlag = cal.get(GregorianCalendar.DAY_OF_YEAR) == 1 ||
                (cal.get(GregorianCalendar.MONTH) == 4 && cal.get(GregorianCalendar.DAY_OF_MONTH) < 4) ||
                (cal.get(GregorianCalendar.MONTH) == 9 && cal.get(GregorianCalendar.DAY_OF_MONTH) < 8);

        return workdayFlag && !holidayFlag;

    }

    private void setCalendarToWeekStartDate(String currentDate, GregorianCalendar currentCal) {

        currentCal.set(Integer.parseInt(currentDate.substring(0, 4)),
                Integer.parseInt(currentDate.substring(4, 6)) - 1,
                Integer.parseInt(currentDate.substring(6, 8)));

        int dayOfWeek = currentCal.get(GregorianCalendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case GregorianCalendar.MONDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -1);
                break;
            case GregorianCalendar.TUESDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -2);
                break;
            case GregorianCalendar.WEDNESDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -3);
                break;
            case GregorianCalendar.THURSDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -4);
                break;
            case GregorianCalendar.FRIDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -5);
                break;
            case GregorianCalendar.SATURDAY:
                currentCal.add(GregorianCalendar.DAY_OF_YEAR, -6);
                break;
            case GregorianCalendar.SUNDAY:
            default:
                break;
        }

    }

}
