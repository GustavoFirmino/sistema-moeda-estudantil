package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {
    Optional<EmpresaParceira> findByEmail(String email);
    boolean existsByCnpj(String cnpj);
}
