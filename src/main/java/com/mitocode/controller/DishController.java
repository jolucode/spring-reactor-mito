package com.mitocode.controller;

import com.mitocode.model.Dish;
import com.mitocode.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.net.URI;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final IDishService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Dish>>> listar() {
        return service.findAll()
                .collectList()
                .map(list -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Flux.fromIterable(list))
                ).defaultIfEmpty(ResponseEntity.notFound().build());

        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));*/
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Dish>>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(dish -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(dish))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findById(id)));*/
    }

    @PostMapping
    public Mono<ResponseEntity<Dish>> save(@RequestBody Dish dish, final ServerHttpRequest req) {
        return service.save(dish)
                .map(saveDish -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(saveDish.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saveDish));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Dish>> update(@PathVariable("id") String id, @RequestBody Dish dish) {
        return service.findById(id)
                .flatMap(existingDish -> {
                    existingDish.setName(dish.getName());
                    existingDish.setStatus(dish.getStatus());
                    existingDish.setPrice(dish.getPrice());
                    return service.update(existingDish, id);
                })
                .map(updatedDish -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(updatedDish))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)    // Mono<Boolean>
                .map(deleted ->
                        deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build()
                );
    }


}
