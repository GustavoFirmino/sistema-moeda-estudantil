package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);

    boolean existsByCpf(String cpf);

    @Modifying
    @Query("UPDATE Professor p SET p.saldoMoedas = p.saldoMoedas + 1000")
    void adicionarMoedasSemestraisParaTodos();
}
