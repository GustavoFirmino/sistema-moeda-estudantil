package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
    Optional<Aluno> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    List<Aluno> findByAtivoTrue();
}
