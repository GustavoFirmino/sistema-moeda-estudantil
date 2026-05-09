package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public Optional<Professor> buscarPorEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.findById(id);
    }

    @Transactional
    public void creditarMoedasSemestrais() {
        professorRepository.adicionarMoedasSemestraisParaTodos();
    }
}
