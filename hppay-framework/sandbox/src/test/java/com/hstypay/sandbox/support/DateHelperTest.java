package com.hstypay.sandbox.support;


import org.junit.Test;

import java.util.Date;

public class DateHelperTest {
    @Test
    public void getMonthBeginTime() throws Exception {
        System.out.println(DateHelper.getMonthBeginTime(new Date()));
    }

    @Test
    public void getMonthEndTime() throws Exception {
        System.out.println(DateHelper.getMonthEndTime(new Date()));
    }

    @Test
    public void getFirstOfWeek() throws Exception {
        System.out.println(DateHelper.getFirstOfWeek(new Date()));
        System.out.println(DateHelper.getFirstOfWeek(DateHelper.getStrToDate("2017-08-21", "yyyy-MM-dd")));
        System.out.println(DateHelper.getFirstOfWeek(DateHelper.getStrToDate("2017-08-27", "yyyy-MM-dd")));
    }

    @Test
    public void getLastOfWeek() throws Exception {
        System.out.println(DateHelper.getLastOfWeek(new Date()));
        System.out.println(DateHelper.getLastOfWeek(DateHelper.getStrToDate("2017-08-21", "yyyy-MM-dd")));
        System.out.println(DateHelper.getLastOfWeek(DateHelper.getStrToDate("2017-08-27", "yyyy-MM-dd")));
    }

    @Test
    public void getLastWeekSunday() throws Exception {
        System.out.println(DateHelper.getLastWeekMonday());
    }

    @Test
    public void getLastWeekMonday() throws Exception {
        System.out.println(DateHelper.getLastWeekSunday());
    }

}