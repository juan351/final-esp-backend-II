package com.dh.msusers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Bill {


    private String idBill;

    private String customerBill;

    private String productBill;

    private Double totalPrice;
}
