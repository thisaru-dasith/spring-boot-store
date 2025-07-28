package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.ChangePasswordRequest;
import com.codewithmosh.store.dto.RequestUserDto;
import com.codewithmosh.store.dto.UpdateUserRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserRepository userRepository;
    private  final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDto> getAllUsers(
           @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        if(!Set.of("name","email").contains(sortBy)){
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy)).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){

        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = userMapper.toUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody RequestUserDto requestUser,
            UriComponentsBuilder uriBuilder){

        if (userRepository.existsUserByEmail(requestUser.getEmail())){
            return ResponseEntity.badRequest().body(
                    Map.of("Email", "Email is already in use")
            );
        }

        User user = userMapper.toEntity(requestUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);

        UserDto userDto = userMapper.toUserDto(user);

        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest updateUser){

        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }

        userMapper.updateUser(user, updateUser);
        userRepository.save(user);
        System.out.println(updateUser);

        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){

        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest changePassword
            ){
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(changePassword.getOldPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        user.setPassword(changePassword.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }


}
