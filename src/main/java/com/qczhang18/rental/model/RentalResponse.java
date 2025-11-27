package com.qczhang18.rental.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RentalResponse {
    private String message;
    private Agreement agreement;
}
