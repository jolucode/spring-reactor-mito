package com.mitocode.controller;

import com.mitocode.dto.DishRecord;
import com.mitocode.mapper.DishMapper;
import com.mitocode.model.Dish;
import com.mitocode.service.IDishService;
import jakarta.validation.Valid;
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
    private final DishMapper mapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<DishRecord>>> findAll() {

        Flux<DishRecord> fx = service.findAll().map(mapper::toRecord);

        return  Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));*/
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<DishRecord>>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(mapper::toRecord)
                .map(dish -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(dish))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findById(id)));*/
    }

    @PostMapping
    public Mono<ResponseEntity<DishRecord>> save(@Valid @RequestBody DishRecord record, final ServerHttpRequest req) {
        return service.save(mapper.toEntity(record))
                .map(saveDish -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(saveDish.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.toRecord(saveDish)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DishRecord>> update(@Valid @PathVariable("id") String id, @RequestBody DishRecord record) {
        return service.findById(id)
                .flatMap(existingDish -> {
                    existingDish.setName(record.nameDish());
                    existingDish.setStatus(record.statusDish());
                    existingDish.setPrice(record.priceDish());
                    return service.update(existingDish, id);
                })
                .map(mapper::toRecord)
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
