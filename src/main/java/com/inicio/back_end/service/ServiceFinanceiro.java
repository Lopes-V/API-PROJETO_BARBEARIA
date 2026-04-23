package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.mapper.FinanceiroMapper;
import com.inicio.back_end.model.Financeiro;
import com.inicio.back_end.model.enums.StatusLancamento;
import com.inicio.back_end.model.enums.TipoLancamento;
import com.inicio.back_end.repository.RepositoryFinanceiro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class ServiceFinanceiro {

    private final RepositoryFinanceiro repositoryFinanceiro;
    private final FinanceiroMapper financeiroMapper;

    public ServiceFinanceiro(RepositoryFinanceiro repositoryFinanceiro, FinanceiroMapper financeiroMapper) {
        this.repositoryFinanceiro = repositoryFinanceiro;
        this.financeiroMapper = financeiroMapper;
    }

    @Transactional(readOnly = true)
    public List<DTOFinanceiro> get() {
        return repositoryFinanceiro.findAll().stream().map(financeiroMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<DTOFinanceiro> getByMes(int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return repositoryFinanceiro.findByDataLancamentoBetween(start, end).stream()
                .map(financeiroMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getBalanco(int ano, int mes) {
        List<DTOFinanceiro> transacoes = getByMes(ano, mes);
        BigDecimal totalReceitas = transacoes.stream()
                .filter(t -> TipoLancamento.RECEITA.equals(t.getTipoLancamento()))
                .map(DTOFinanceiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDespesas = transacoes.stream()
                .filter(t -> TipoLancamento.DESPESA.equals(t.getTipoLancamento()))
                .map(DTOFinanceiro::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lucroLiquido = totalReceitas.subtract(totalDespesas);
        return Map.of(
                "totalReceitas", totalReceitas,
                "totalDespesas", totalDespesas,
                "saldo", lucroLiquido
        );
    }

    @Transactional
    public DTOFinanceiro criarLancamento(DTOFinanceiro dto) {
        if (dto.getDataLancamento() == null) {
            dto.setDataLancamento(LocalDate.now());
        }
        if (dto.getStatusLancamento() == null) {
            dto.setStatusLancamento(StatusLancamento.PENDENTE);
        }
        Financeiro entity = financeiroMapper.toEntity(dto);
        Financeiro saved = repositoryFinanceiro.save(entity);
        return financeiroMapper.toDto(saved);
    }

    @Transactional
    public DTOFinanceiro marcarComoPago(Long id) {
        Financeiro financeiro = repositoryFinanceiro.findById(id)
                .orElseThrow(() -> new RuntimeException("Lançamento financeiro não encontrado"));
        financeiro.setStatusLancamento(StatusLancamento.PAGO);
        Financeiro atualizado = repositoryFinanceiro.save(financeiro);
        return financeiroMapper.toDto(atualizado);
    }
}
