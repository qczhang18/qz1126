package com.qczhang18.rental.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {
    private Tool tool;
    private int rentalDay;
    private int discountPercent;
    @JsonFormat(pattern = "M/d/yy")
    private LocalDate checkoutDate;
}
