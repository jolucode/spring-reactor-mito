package com.mitocode.controller;

import com.mitocode.dto.ClientRecord;

import com.mitocode.dto.InvoiceRecord;
import com.mitocode.mapper.ClientMapper;
import com.mitocode.mapper.InvoiceMapper;
import com.mitocode.model.Client;
import com.mitocode.service.IInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final IInvoiceService service;
    private final InvoiceMapper invoiceMapper;
    private final ClientMapper clientMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<InvoiceRecord>>> findAll() {

        Flux<InvoiceRecord> fx = service.findAll().map(invoiceMapper::toRecord);

        return  Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<InvoiceRecord>>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(invoiceMapper::toRecord)
                .map(invoice -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(invoice))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<InvoiceRecord>> save(@Valid @RequestBody InvoiceRecord record, final ServerHttpRequest req) {
        return service.save(invoiceMapper.toEntity(record))
                .map(saveInvoice -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(saveInvoice.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(invoiceMapper.toRecord(saveInvoice)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<InvoiceRecord>> update(
            @Valid @PathVariable("id") String id,
            @RequestBody InvoiceRecord record) {

        return service.findById(id)
                .flatMap(existingInvoice -> {

                    //  Actualizar descripción
                    existingInvoice.setDescription(record.description());

                    // Actualizar cliente (usando mapper)
                    Client updatedClient = clientMapper.toEntity(record.client());
                    existingInvoice.setClient(updatedClient);

                    //  Actualizar items
                    existingInvoice.setItems(record.items());

                    // Guardar
                    return service.update(existingInvoice, id);
                })
                .map(invoiceMapper::toRecord) // Convertimos a record
                .map(updatedInvoice -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(updatedInvoice))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)    // Mono<Boolean>
                .map(deleted ->
                        deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build()
                );
    }


    @GetMapping("/generateReport/{id}")
    public Mono<ResponseEntity<byte[]>> generarReporte(@PathVariable("id") String id) {
        return service.generarReport(id)
                .map(reportBytes -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(reportBytes))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
