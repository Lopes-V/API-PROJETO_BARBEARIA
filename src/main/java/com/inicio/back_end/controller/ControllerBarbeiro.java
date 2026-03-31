package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import com.inicio.back_end.service.ServiceBarbeiro;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/barbeiro")
public class ControllerBarbeiro {
    private final ServiceBarbeiro sb;

    public ControllerBarbeiro(ServiceBarbeiro sb) {
        this.sb = sb;
    }

    @GetMapping
    public ResponseEntity<?> getBarbeiros() {
        try {
            List<DTOBarbeiro> barbeiros = sb.get();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Barbeiros recuperados com sucesso",
                    "data", barbeiros
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar barbeiros: " + e.getMessage()
            ));
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBarbeiroById(@PathVariable Long id) {
        try {
            DTOBarbeiro dtoBarbeiro = sb.getById(id);
            if(Objects.nonNull(dtoBarbeiro)) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Barbeiro encontrado com sucesso",
                        "data", dtoBarbeiro
                ));
            }else{
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Barbeiro não encontrado"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao procurar barbeiro por ID: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarBarbeiro(@Valid  @RequestBody DTOBarbeiro dtoBarbeiro){
        try{
            Barbeiro barbeiro = sb.cadastrar(dtoBarbeiro);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(barbeiro.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(Map.of(
                    "success", true,
                    "message", "Barbeiro cadastrado com sucesso",
                    "data", barbeiro
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao cadastrar barbeiro: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarBarbeiro(@Valid @RequestBody DTOBarbeiro dtoBarbeiro, @PathVariable Long id){
        try {
            sb.editar(dtoBarbeiro, id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Barbeiro editado com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao editar barbeiro: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarBarbeiro(@PathVariable Long id){
        try{
            sb.deletar(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Barbeiro deletado com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao deletar barbeiro: " + e.getMessage()
            ));
        }
    }
}