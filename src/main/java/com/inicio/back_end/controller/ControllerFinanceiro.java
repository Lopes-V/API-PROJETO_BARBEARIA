package com.inicio.back_end.controller;


import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.service.ServiceFinanceiro;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financeiro")
public class    ControllerFinanceiro {

    private final ServiceFinanceiro serviceFinanceiro;

    public ControllerFinanceiro(ServiceFinanceiro serviceFinanceiro) {
        this.serviceFinanceiro = serviceFinanceiro;
    }

    @GetMapping
    public ResponseEntity<?> getFinanceiro(@RequestParam int ano, @RequestParam int mes) {
        try {
            List<DTOFinanceiro> financeiroList = serviceFinanceiro.getByMes(ano, mes);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Transações do mês recuperadas com sucesso",
                    "data", financeiroList
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao recuperar transações: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/balanco")
    public ResponseEntity<?> getBalancoFinanceiro(@RequestParam int ano, @RequestParam int mes) {
        try {
            Map<String, BigDecimal> balanco = serviceFinanceiro.getBalanco(ano, mes);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Balanço financeiro calculado com sucesso",
                    "data", balanco
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao calcular balanço: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/lancamento")
    public ResponseEntity<?> criarLancamento(@Valid @RequestBody DTOFinanceiro dto) {
        try {
            DTOFinanceiro criado = serviceFinanceiro.criarLancamento(dto);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Lançamento financeiro criado com sucesso",
                    "data", criado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao criar lançamento: " + e.getMessage()
            ));
        }
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<?> marcarComoPago(@PathVariable Long id) {
        try {
            DTOFinanceiro financeiroAtualizado = serviceFinanceiro.marcarComoPago(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Lançamento marcado como pago com sucesso",
                    "data", financeiroAtualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erro ao marcar como pago: " + e.getMessage()
            ));
        }
    }
}
