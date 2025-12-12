package com.mitocode.dto;

import com.mitocode.model.InvoiceDetail;
import jakarta.validation.constraints.*;

import java.util.List;

public record InvoiceRecord(
        String id,

        String description,
        ClientRecord client,
        List<InvoiceDetail> items
) {
}
