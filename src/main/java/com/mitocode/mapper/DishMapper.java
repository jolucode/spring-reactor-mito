package com.mitocode.mapper;

import com.mitocode.dto.DishRecord;
import com.mitocode.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishMapper {
    @Mapping(source = "name", target = "nameDish")
    @Mapping(source = "price", target = "priceDish")
    @Mapping(source = "status", target = "statusDish")
    DishRecord toRecord(Dish dish);

    @Mapping(source = "nameDish", target = "name")
    @Mapping(source = "priceDish", target = "price")
    @Mapping(source = "statusDish", target = "status")
    Dish toEntity(DishRecord record);
}

