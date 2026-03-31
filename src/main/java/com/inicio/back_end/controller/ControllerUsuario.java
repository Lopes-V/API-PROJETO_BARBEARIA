package com.inicio.back_end.controller;


import com.inicio.back_end.dto.DTOUsuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(DTOUsuario dtoUsuario){

    }
}
