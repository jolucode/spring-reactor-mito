package com.mitocode.service;

import com.mitocode.model.Dish;
import com.mitocode.model.Invoice;
import reactor.core.publisher.Mono;

public interface IInvoiceService extends IGenericService<Invoice, String>{
    
    /*Mono<Dish> save(Dish dish);
    Mono<Dish> update(Dish dish, String id);
    Flux<Dish> findAll();
    Mono<Dish> findById(String id);
    Mono<Boolean> delete(String id);*/

    Mono<byte[]> generarReport(String idInvoice);

}
