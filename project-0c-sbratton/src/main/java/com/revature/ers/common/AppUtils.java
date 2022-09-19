package com.revature.ers.common;

import com.revature.ers.common.datasource.exceptions.InvalidRequestException;
import com.revature.ers.users.UserResponse;

public class AppUtils {

    public static Integer parseInt(String possibleInt) {
        try {
            return Integer.parseInt(possibleInt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidRequestException("A non-numeric id value was provided!");

        }
    }
}
