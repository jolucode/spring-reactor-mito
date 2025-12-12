package com.mitocode.dto;

import com.mitocode.model.InvoiceDetail;

import java.util.List;

public record InvoiceDetailRecord(
        String id,

        int quantity,
        DishRecord dish
) {
}
