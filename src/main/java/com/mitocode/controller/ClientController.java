package com.mitocode.controller;

import com.mitocode.dto.ClientRecord;
import com.mitocode.dto.DishRecord;
import com.mitocode.mapper.ClientMapper;
import com.mitocode.mapper.DishMapper;
import com.mitocode.service.IClientService;
import com.mitocode.service.IDishService;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService service;
    private final ClientMapper mapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<ClientRecord>>> findAll() {

        Flux<ClientRecord> fx = service.findAll().map(mapper::toRecord);

        return  Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));*/
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<ClientRecord>>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(mapper::toRecord)
                .map(client -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(client))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
        /*Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findById(id)));*/
    }

    @PostMapping
    public Mono<ResponseEntity<ClientRecord>> save(@Valid @RequestBody ClientRecord record, final ServerHttpRequest req) {
        return service.save(mapper.toEntity(record))
                .map(saveClient -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(saveClient.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.toRecord(saveClient)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClientRecord>> update(@Valid @PathVariable("id") String id, @RequestBody ClientRecord record) {
        return service.findById(id)
                .flatMap(existingClient -> {
                    // Actualizar campos manualmente
                    existingClient.setFirstName(record.nameClient());
                    existingClient.setLastName(record.surnameClient());
                    existingClient.setBirthDate(record.birthDateClient());
                    existingClient.setUrlPhoto(record.urlPhotoClient());

                    return service.update(existingClient, id);
                })
                .map(mapper::toRecord)
                .map(updatedClient -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(updatedClient))
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
