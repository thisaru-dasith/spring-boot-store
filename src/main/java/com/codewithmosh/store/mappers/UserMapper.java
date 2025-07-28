package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.RequestUserDto;
import com.codewithmosh.store.dto.UpdateUserRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toUserDto(User user);

    User toEntity(RequestUserDto requestUserDto);

    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

}
