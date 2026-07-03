package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.Instituicao;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.repository.ProfessorRepository;
import com.pucminas.moedaestudantil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private static final int MOEDAS_INICIAIS = 1000;

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Professor cadastrar(Professor professor, String senha) {
        if (usuarioRepository.existsByEmail(professor.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema.");
        }
        if (professorRepository.existsByCpf(professor.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        // O form envia apenas o id — resolve a entidade gerenciada antes de salvar
        if (professor.getInstituicao() == null || professor.getInstituicao().getId() == null) {
            throw new IllegalArgumentException("Selecione uma instituição de ensino.");
        }
        Instituicao instituicao = instituicaoRepository.findById(professor.getInstituicao().getId())
                .orElseThrow(() -> new IllegalArgumentException("Instituição de ensino inválida."));
        professor.setInstituicao(instituicao);
        professor.setSenha(passwordEncoder.encode(senha));
        professor.setSaldoMoedas(MOEDAS_INICIAIS);
        return professorRepository.save(professor);
    }

    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

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
