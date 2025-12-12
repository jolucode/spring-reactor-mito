package com.mitocode.mapper;

import com.mitocode.dto.InvoiceRecord;
import com.mitocode.model.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface InvoiceMapper {

    // ENTITY → RECORD
    InvoiceRecord toRecord(Invoice invoice);

    // RECORD → ENTITY
    Invoice toEntity(InvoiceRecord record);
}
