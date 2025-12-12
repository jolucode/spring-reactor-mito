package com.mitocode.mapper;

import com.mitocode.dto.ClientRecord;
import com.mitocode.dto.DishRecord;
import com.mitocode.model.Client;
import com.mitocode.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    // ENTITY → RECORD
    @Mapping(source = "firstName", target = "nameClient")
    @Mapping(source = "lastName", target = "surnameClient")
    @Mapping(source = "birthDate", target = "birthDateClient")
    @Mapping(source = "urlPhoto", target = "urlPhotoClient")
    ClientRecord toRecord(Client client);

    // RECORD → ENTITY
    @Mapping(source = "nameClient", target = "firstName")
    @Mapping(source = "surnameClient", target = "lastName")
    @Mapping(source = "birthDateClient", target = "birthDate")
    @Mapping(source = "urlPhotoClient", target = "urlPhoto")
    Client toEntity(ClientRecord record);
}

