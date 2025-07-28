package com.codewithmosh.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;

    @JsonFormat(pattern = "yyyy: MM : dd")
    private LocalDateTime createdAt;
}
