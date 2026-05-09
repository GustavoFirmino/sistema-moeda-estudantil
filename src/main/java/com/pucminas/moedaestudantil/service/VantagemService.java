package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Vantagem;
import com.pucminas.moedaestudantil.repository.VantagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VantagemService {

    private final VantagemRepository vantagemRepository;

    private static final String UPLOAD_DIR = "./uploads/";

    @Transactional
    public Vantagem cadastrar(Vantagem vantagem, MultipartFile foto, EmpresaParceira empresa) throws IOException {
        vantagem.setEmpresaParceira(empresa);
        if (foto != null && !foto.isEmpty()) {
            vantagem.setFotoUrl(salvarFoto(foto));
        }
        return vantagemRepository.save(vantagem);
    }

    @Transactional
    public Vantagem atualizar(Vantagem vantagemAtualizada, MultipartFile foto) throws IOException {
        Vantagem existente = vantagemRepository.findById(vantagemAtualizada.getId())
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));
        existente.setNome(vantagemAtualizada.getNome());
        existente.setDescricao(vantagemAtualizada.getDescricao());
        existente.setCustoMoedas(vantagemAtualizada.getCustoMoedas());
        if (foto != null && !foto.isEmpty()) {
            existente.setFotoUrl(salvarFoto(foto));
        }
        return vantagemRepository.save(existente);
    }

    @Transactional
    public void desativar(Long id) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));
        vantagem.setAtiva(false);
        vantagemRepository.save(vantagem);
    }

    public List<Vantagem> listarDisponiveis() {
        return vantagemRepository.findByAtivaTrue();
    }

    public List<Vantagem> listarPorEmpresa(Long empresaId) {
        return vantagemRepository.findByEmpresaParceiraId(empresaId);
    }

    public Optional<Vantagem> buscarPorId(Long id) {
        return vantagemRepository.findById(id);
    }

    private String salvarFoto(MultipartFile foto) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        Path destino = uploadPath.resolve(nomeArquivo);
        Files.copy(foto.getInputStream(), destino);
        return "/uploads/" + nomeArquivo;
    }
}
