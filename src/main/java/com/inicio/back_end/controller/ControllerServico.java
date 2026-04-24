package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import com.inicio.back_end.service.ServiceServico;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("servicos")
public class ControllerServico {

    private final ServiceServico ss;

    public ControllerServico(ServiceServico ss) {
        this.ss = ss;
    }

    @GetMapping
    public ResponseEntity<?> getServico() {
        try {
            List<DTOServico> lista = ss.get();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Serviços recuperados com sucesso",
                    "data", lista));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar serviços: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdServico(@PathVariable Long id) {
        try {
            DTOServico s = ss.getById(id);
            if (Objects.nonNull(s)) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Serviço encontrado com sucesso",
                        "data", s));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Serviço não encontrado"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao buscar serviço por ID: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> criarServico(@Valid @RequestBody DTOServico dS) {
        try {
            Servico servico = ss.criar(dS);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(servico.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(Map.of(
                    "success", true,
                    "message", "Serviço criado com sucesso",
                    "data", servico));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao criar serviço: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarServico(@Valid @RequestBody DTOServico dS, @PathVariable Long id) {
        try {
            Servico servico = ss.editar(dS, id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Serviço editado com sucesso",
                    "data", servico));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao editar serviço: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id) {
        try {
            ss.deletar(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Serviço deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao deletar serviço: " + e.getMessage()));
        }
    }
}
