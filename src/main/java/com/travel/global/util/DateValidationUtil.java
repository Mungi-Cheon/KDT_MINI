package com.travel.global.util;

import java.time.LocalDate;

public class DateValidationUtil {

    public static boolean isCheckInValid(LocalDate checkIn) {
        LocalDate now = LocalDate.now();
        return checkIn.isAfter(now) || checkIn.isEqual(now);
    }

    public static boolean isCheckOutValid(LocalDate checkIn, LocalDate checkOut) {
        return checkOut.isAfter(checkIn);
    }

    public static LocalDate checkInDate(LocalDate checkInDate) {
        if (checkInDate == null) {
            checkInDate = LocalDate.now();
        }
        return checkInDate;
    }

    public static LocalDate checkOutDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkOutDate == null) {
            checkOutDate = checkInDate.plusDays(1);
        }
        return checkOutDate;
    }
}
