package com.qczhang18.rental.controller;

import com.qczhang18.rental.exception.ValidationException;
import com.qczhang18.rental.model.Agreement;
import com.qczhang18.rental.model.RentalRequest;
import com.qczhang18.rental.model.RentalResponse;
import com.qczhang18.rental.service.RentalService;
import com.qczhang18.rental.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rent")
    public RentalResponse rental(@RequestBody RentalRequest rentalRequest) {

        RentalResponse rentalResponse = new RentalResponse();

        try {
            Validator.validateRequest(rentalRequest);
            Agreement agreement = rentalService.checkout(rentalRequest);
            rentalResponse.setAgreement(agreement);
            rentalResponse.setMessage("success");
        } catch (ValidationException exception) {
            rentalResponse.setMessage("validation error, please check your request : " + exception.getMessage());
        } catch (Exception exception) {
            rentalResponse.setMessage("internal server error, please contact support");
        }

        return rentalResponse;
    }
}
