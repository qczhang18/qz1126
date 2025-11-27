package com.qczhang18.rental.controller;

import com.qczhang18.rental.model.RentalRequest;
import com.qczhang18.rental.model.RentalResponse;
import com.qczhang18.rental.model.Tool;
import com.qczhang18.rental.service.RentalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @InjectMocks
    private RentalController controller;

    @Spy
    private RentalService rentalService = new RentalService();

    // ------------------ Test 1 ------------------
    // 101% discount → INVALID
    @Test
    void test1_invalidDiscountOver100() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.JAKR)
                .checkoutDate(LocalDate.of(2015, 9, 3))
                .rentalDay(5)
                .discountPercent(101)
                .build();

        RentalResponse response = controller.rental(request);

        assertEquals("validation error, please check your request : Discount percentage \"101\" is invalid. Must be between  0 and 100 ", response.getMessage());
    }

    // ------------------ Test 2 ------------------
    // SUCCESS CASE
    @Test
    void test2_success() {
        RentalRequest request = RentalRequest.builder()
                .tool(Tool.LADW)
                .checkoutDate(LocalDate.of(2020, 7, 2))
                .rentalDay(3)
                .discountPercent(10)
                .build();

        RentalResponse response = controller.rental(request);

        assertEquals("success", response.getMessage());
        assertEquals(2, response.getAgreement().getChargeDays());
        assertEquals(BigDecimal.valueOf(3.58), response.getAgreement().getFinalCharge());
    }

}