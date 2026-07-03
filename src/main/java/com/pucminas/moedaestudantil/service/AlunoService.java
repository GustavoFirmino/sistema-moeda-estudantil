package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.Instituicao;
import com.pucminas.moedaestudantil.model.enums.TipoUsuario;
import com.pucminas.moedaestudantil.repository.AlunoRepository;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Aluno cadastrar(Aluno aluno, String senha) {
        if (usuarioRepository.existsByEmail(aluno.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema.");
        }
        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        // O form envia apenas o id — resolve a entidade gerenciada antes de salvar
        if (aluno.getInstituicao() == null || aluno.getInstituicao().getId() == null) {
            throw new IllegalArgumentException("Selecione uma instituição de ensino.");
        }
        Instituicao instituicao = instituicaoRepository.findById(aluno.getInstituicao().getId())
                .orElseThrow(() -> new IllegalArgumentException("Instituição de ensino inválida."));
        aluno.setInstituicao(instituicao);
        aluno.setSenha(passwordEncoder.encode(senha));
        aluno.setSaldoMoedas(0);
        return alunoRepository.save(aluno);
    }

    public Optional<Aluno> buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public List<Aluno> listarAtivos() {
        return alunoRepository.findByAtivoTrue();
    }

    @Transactional
    public Aluno atualizar(Aluno alunoAtualizado) {
        Aluno existente = alunoRepository.findById(alunoAtualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        existente.setNome(alunoAtualizado.getNome());
        existente.setEndereco(alunoAtualizado.getEndereco());
        existente.setCurso(alunoAtualizado.getCurso());
        existente.setRg(alunoAtualizado.getRg());
        return alunoRepository.save(existente);
    }

    @Transactional
    public void desativar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }
}
