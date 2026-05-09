package com.pucminas.moedaestudantil.repository;

import com.pucminas.moedaestudantil.model.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    List<Vantagem> findByAtivaTrue();
    List<Vantagem> findByEmpresaParceiraId(Long empresaId);
    List<Vantagem> findByEmpresaParceiraIdAndAtivaTrue(Long empresaId);
}
