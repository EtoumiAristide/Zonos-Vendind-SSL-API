package com.gs2e.vending.zonos.utils;

import java.time.LocalDateTime;

public class Utilitaires {
    public static long calculateTimeMillis(LocalDateTime localDateTime) {
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfYear();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();

        long millis = 0;

        // Calculate years since 1970
        int yearsSince1970 = year - 1970;

        // Convert years to milliseconds
        millis += yearsSince1970 * 365L * 24 * 60 * 60 * 1000;

        // Calculate days in the current year
        int daysInYear = getDaysInYear(year);
        for (int i = 1; i < month; i++) {
            daysInYear += getDaysInMonth(i, year);
        }

        // Convert days to milliseconds
        millis += (long) daysInYear * 24 * 60 * 60 * 1000;

        // Convert hours to milliseconds
        millis += (long) (hour - 1) * 60 * 60 * 1000;

        // Convert minutes to milliseconds
        millis += (long) (minute - 1) * 60 * 1000;

        // Convert seconds to milliseconds
        millis += (second) * 1000L;

        return millis;
    }

    private static int getDaysInYear(int year) {
        if (isLeapYear(year)) {
            return 366;
        } else {
            return 365;
        }
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                throw new IllegalArgumentException("Invalid month");
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
