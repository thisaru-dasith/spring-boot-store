package com.codewithmosh.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUserDto {
    @NotBlank(message = "name is required")
    @Size( max= 255, message = "Name must be less than 255 characters")
    private String name;
    @NotBlank(message = "email is required")
    @Email(message = "It must be a valid email")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6, max = 25, message = "password must be between 6 and 25 character long")
    private String password;
}
