package com.inicio.back_end.controller;


import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.dto.especifico.DTOPatchAgendamento;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.service.ServiceAgendamento;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/agendamentos")
public class ControllerAgendamento {

    private final ServiceAgendamento sa;

    public ControllerAgendamento(ServiceAgendamento sa) {
        this.sa = sa;
    }

    @GetMapping
    public ResponseEntity<?> getAgendamento() {
        try {
            List<DTOAgendamento> agendamentoList = this.sa.get();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Agendamentos recuperados com sucesso",
                    "data", agendamentoList
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar agendamentos: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/barbeiro/{id}")
    public ResponseEntity<?> getAgendamentoByBarbeiro(@PathVariable Long id) {
        try {
            List<DTOAgendamento> a = sa.getByBarbeiro(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Agendamentos do barbeiro recuperados com sucesso",
                    "data", a
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar agendamentos do barbeiro: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@Valid @RequestBody DTOAgendamento dtoAgendamento) {
        try {
            Agendamento agendamento = sa.criar(dtoAgendamento);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(agendamento.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(Map.of(
                    "success", true,
                    "message", "Agendamento criado com sucesso",
                    "data", agendamento
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao criar agendamento: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAgendamento(@PathVariable Long id) {
        try {
            sa.delete(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Agendamento deletado com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao deletar agendamento: " + e.getMessage()
            ));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatusAgendamento(@PathVariable Long id, @Valid @RequestBody DTOPatchAgendamento status) {
        try {
            sa.changeStatus(id, status);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Status do agendamento alterado com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao alterar status do agendamento: " + e.getMessage()
            ));
        }
    }
}
