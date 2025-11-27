package com.qczhang18.rental.util;

import com.qczhang18.rental.exception.ValidationException;
import com.qczhang18.rental.model.RentalRequest;

public class Validator {

    public static void validateRequest(RentalRequest request) throws ValidationException {

        if (request == null) {
            throw new ValidationException("request is null");
        }

        if (!isValidDate(request.getRentalDay())) {
            throw new ValidationException(String.format("Rental day \"%s\" is invalid. Must be greater or equal to 1 ", request.getRentalDay()));
        }

        if (!isValidPercentage(request.getDiscountPercent())) {
            throw new ValidationException(String.format("Discount percentage \"%s\" is invalid. Must be between  0 and 100 ",  + request.getDiscountPercent()));
        }

    }

    private static boolean isValidDate(int days) {
        return days >= 1;
    }

    private static boolean isValidPercentage(int percent) {
        return percent >= 0 && percent <= 100;
    }
}
