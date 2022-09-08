package com.revature.ers.common;

import com.revature.ers.common.datasource.exceptions.InvalidRequestException;

public class AppUtils {

    public static Integer parseInt(String possibleInt) {
        try {
            return Integer.parseInt(possibleInt);
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("A non-numeric id value was provided!");
        }
    }

}
