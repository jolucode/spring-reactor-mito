package com.mitocode.service.impl;

import com.mitocode.model.Dish;
import com.mitocode.repo.IDishRepo;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {

    private final IDishRepo repo;

    @Override
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
    }
}
