package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.repository.EmpresaParceiraRepository;
import com.pucminas.moedaestudantil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EmpresaParceira cadastrar(EmpresaParceira empresa, String senha) {
        if (usuarioRepository.existsByEmail(empresa.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado no sistema.");
        }
        if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado no sistema.");
        }
        empresa.setSenha(passwordEncoder.encode(senha));
        return empresaRepository.save(empresa);
    }

    public Optional<EmpresaParceira> buscarPorEmail(String email) {
        return empresaRepository.findByEmail(email);
    }

    public Optional<EmpresaParceira> buscarPorId(Long id) {
        return empresaRepository.findById(id);
    }

    @Transactional
    public EmpresaParceira atualizar(EmpresaParceira empresaAtualizada) {
        EmpresaParceira existente = empresaRepository.findById(empresaAtualizada.getId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada."));
        existente.setNomeFantasia(empresaAtualizada.getNomeFantasia());
        return empresaRepository.save(existente);
    }

    @Transactional
    public void desativar(Long id) {
        EmpresaParceira empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada."));
        empresa.setAtivo(false);
        empresaRepository.save(empresa);
    }
}
