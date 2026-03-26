package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.service.ServiceServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("servicos")
public class ControllerServico {

    private final ServiceServico ss;

    public ControllerServico(ServiceServico ss) {
        this.ss = ss;
    }

    @GetMapping
    public ResponseEntity<List<DTOServico>> getServico() {
        List<DTOServico> lista = ss.get();

        return lista.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lista);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getByIdServico(@RequestParam Long id) {
        DTOServico s = ss.getById(id);
        try {
            if (Objects.nonNull(s)) {
                return ResponseEntity.ok(s);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao buscar servico por id " + e);
            return ResponseEntity.badRequest().body("Erro ao buscar servico por id");
        }
    }

    @PostMapping
    public ResponseEntity<?> criarServico(@RequestBody DTOServico dS) {
        try {
            ss.criar(dS);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar criar um servico " + e);
            return ResponseEntity.badRequest().body("Erro ao tentar criar um servico");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarServico(@RequestParam Long id) {
        try {
            ss.deletar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar deletar um serviço " + e);
            return ResponseEntity.badRequest().body("Erro ao tentar deletar um serviço");
        }
    }
}
