package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.JwtResponse;
import com.codewithmosh.store.dto.LoginRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        /*String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!passwordEncoder.matches(password,user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(email).orElseThrow();

        String token = new JwtService().generateJwtToken(user);


        return ResponseEntity.ok(new JwtResponse(token));




    }

    /*@PostMapping("/validate")
    public boolean verify(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateJwtToken(token);

    }*/

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long)authentication.getPrincipal();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            ResponseEntity.notFound().build();
        }
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

 }