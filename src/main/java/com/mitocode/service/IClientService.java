package com.mitocode.service;

import com.mitocode.model.Client;
import com.mitocode.model.Dish;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService  extends IGenericService<Client, String>{
    
    /*Mono<Client> save(Client client);
    Mono<Client> update(Client client, String id);
    Flux<Client> findAll();
    Mono<Client> findById(String id);
    Mono<Boolean> delete(String id);*/

}
