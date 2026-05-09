package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.TransacaoResgate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoResgateRepository extends JpaRepository<TransacaoResgate, Long> {
    List<TransacaoResgate> findByAlunoIdOrderByDataHoraDesc(Long alunoId);
}
