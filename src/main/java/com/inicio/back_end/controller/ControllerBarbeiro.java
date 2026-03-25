package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.service.ServiceBarbeiro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<DTOBarbeiro>> getBarbeiros() {
        List<DTOBarbeiro> barbeiros = sb.get();

        if (barbeiros.isEmpty()) {
            // Retornar 204 No Content é mais profissional que 400 Bad Request para listas vazias
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(barbeiros);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getBarbeiroById(@RequestParam Long id) {
        try {
            DTOBarbeiro b = sb.getById(id);
            if(Objects.nonNull(b)) {
                return ResponseEntity.ok(sb.getById(id));
            }else{
                return ResponseEntity.badRequest().body(Map.of("message","Nenhum Barbeiro Encontrado com o id "+id));
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao procurar um barbeiro por id" + e);
            return ResponseEntity.badRequest().body(Map.of("message","Erro ao procurar um barbeiro por id"));
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarBarbeiro(@RequestBody DTOBarbeiro dtoBarbeiro){
        try{
            sb.cadastrar(dtoBarbeiro);
            return ResponseEntity.ok().body(Map.of("message","Barbeiro cadastrado com sucesso"));
        } catch (RuntimeException e) {
            System.out.println("Erro ao tentar cadastrar barbeiro " + e);
            return ResponseEntity.badRequest().body(Map.of("message", "Erro ao tentar cadastrar barbeiro"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editarBarbeiro(@RequestBody DTOBarbeiro dtoBarbeiro, @RequestParam Long id){
        try {
            sb.editar(dtoBarbeiro, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao editar o barbeiro " + e);
            return ResponseEntity.badRequest().body(Map.of("message", "Erro ao editar o barbeiro"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarBarbeiro(@RequestParam Long id){
        try{
            sb.deletar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("Erro ao deletar barbeiro" + e);
            return ResponseEntity.badRequest().body(Map.of("message","Erro ao deletar barbeiro"));
        }
    }
}