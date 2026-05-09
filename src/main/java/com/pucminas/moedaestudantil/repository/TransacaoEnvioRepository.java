package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.TransacaoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoEnvioRepository extends JpaRepository<TransacaoEnvio, Long> {
    List<TransacaoEnvio> findByProfessorIdOrderByDataHoraDesc(Long professorId);
    List<TransacaoEnvio> findByAlunoIdOrderByDataHoraDesc(Long alunoId);
}
