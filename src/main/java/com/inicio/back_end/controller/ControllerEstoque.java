package com.inicio.back_end.controller;

import com.inicio.back_end.dto.DTOEstoque;
import com.inicio.back_end.dto.especifico.DTOPatchEstoque;
import com.inicio.back_end.model.Estoque;
import com.inicio.back_end.service.ServiceEstoque;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estoque")
public class ControllerEstoque {

    private final ServiceEstoque serviceEstoque;

    public ControllerEstoque(ServiceEstoque serviceEstoque) {
        this.serviceEstoque = serviceEstoque;
    }

    @GetMapping
    public ResponseEntity<?> getEstoque(){
        try {
            List<DTOEstoque> dtoEstoqueList = serviceEstoque.get();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Itens do estoque recuperados com sucesso",
                    "data", dtoEstoqueList
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar itens do estoque: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/alerta")
    public ResponseEntity<?> getEstoqueAbaixo(){
        try {
            List<DTOEstoque> dtoEstoqueList = serviceEstoque.getAbaixo();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Itens abaixo do mínimo recuperados com sucesso",
                    "data", dtoEstoqueList
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar itens abaixo do mínimo: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarEstoque(@RequestBody DTOEstoque dtoEstoque){
        try{
            Estoque estoque = serviceEstoque.cadastrar(dtoEstoque);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(estoque.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(Map.of(
                    "success", true,
                    "message", "Item cadastrado no estoque com sucesso",
                    "data", estoque
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao cadastrar item no estoque: " + e.getMessage()
            ));
        }
    }

    @PatchMapping("/{id}/movimentacao")
    public ResponseEntity<?> movimentacaoEstoque(@PathVariable Long id, @RequestBody DTOPatchEstoque dtoPatchEstoque){
        try{
            serviceEstoque.movimentacao(dtoPatchEstoque);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Movimentação do estoque realizada com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao realizar movimentação do estoque: " + e.getMessage()
            ));
        }
    }
}
