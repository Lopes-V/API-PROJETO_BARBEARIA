package com.inicio.back_end.controller;


import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.service.ServiceAgendamento;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            if (Objects.nonNull(sa.get())) {
                return ResponseEntity.ok(sa);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar buscar agendamentos " + e);
            return ResponseEntity.badRequest().body("Erro ao tentar buscar agendamentos");
        }
    }

    @GetMapping("/barbeiro/{id}")
    public ResponseEntity<?> getAgendamentoByBarbeiro(@RequestParam Long id) {
        List<DTOAgendamento> a = sa.getByBarbeiro(id);
        try {
            if (Objects.nonNull(a)) {
                return ResponseEntity.ok(a);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao procurar o agendamento pelo barbeiro " + e);
            return ResponseEntity.badRequest().body("Erro ao procurar o agendamento pelo barbeiro");
        }
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody DTOAgendamento dtoAgendamento) {
        try {
            sa.criar(dtoAgendamento);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao criar o usuario " + e);
            return ResponseEntity.badRequest().body("Erro ao criar o usuario");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarAgendamento(@RequestParam Long id) {
        try {
            sa.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar deletar o usuario " + e);
            return ResponseEntity.badRequest().body("Erro ao tentar deletar o usuario");
        }
    }

    @PatchMapping("/agendamentos/{id}/status")
    public ResponseEntity<?> changeStatusAgendamento(@RequestParam Long id, @RequestBody String status) {
        try {
            sa.changeStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar alterar o status " + e);
            return ResponseEntity.badRequest().body("Erro ao tentar alterar o status");
        }
    }
}
