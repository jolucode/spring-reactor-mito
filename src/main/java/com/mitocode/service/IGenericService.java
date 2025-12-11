package com.mitocode.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGenericService<T, ID> {
    
    Mono<T> save(T t);
    Mono<T> update(T t, ID id);
    Flux<T> findAll();
    Mono<T> findById(ID id);
    Mono<Boolean> delete(ID id);

}
