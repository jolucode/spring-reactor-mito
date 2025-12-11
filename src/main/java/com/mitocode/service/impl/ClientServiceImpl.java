package com.mitocode.service.impl;

import com.mitocode.model.Client;
import com.mitocode.model.Dish;
import com.mitocode.repo.IClientRepo;
import com.mitocode.repo.IDishRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IClientService;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl extends CRUDImpl<Client, String> implements IClientService {

    private final IClientRepo repo;

    @Override
    protected IGenericRepo<Client, String> getRepo() {
        return repo;
    }





    /*@Override
    public Mono<Dish> save(Dish dish) {
        return repo.save(dish);
    }

    @Override
    public Mono<Dish> update(Dish dish, String id) {
        return repo
                .findById(id)
                .flatMap(e -> repo.save(dish));
    }

    @Override
    public Flux<Dish> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<Dish> findById(String id) {
        return repo.findById(id);
    }

    public Mono<Boolean> delete(String id) {
        return repo
                .findById(id)
                .hasElement()
                .flatMap(e -> {
                    if (e) {
                        return repo.deleteById(id).thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }*/
}
