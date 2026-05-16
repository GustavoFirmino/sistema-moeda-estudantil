package com.pucminas.moedaestudantil.config;

import com.pucminas.moedaestudantil.model.Admin;
import com.pucminas.moedaestudantil.model.Instituicao;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.repository.ProfessorRepository;
import com.pucminas.moedaestudantil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final InstituicaoRepository instituicaoRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (instituicaoRepository.count() > 0) {
                return;
            }

            log.info("Inicializando dados padrão do sistema...");

            List<Instituicao> instituicoes = List.of(
                new Instituicao("PUC Minas", "Belo Horizonte", "MG"),
                new Instituicao("UFMG", "Belo Horizonte", "MG"),
                new Instituicao("UFOP", "Ouro Preto", "MG"),
                new Instituicao("UFSJ", "São João del-Rei", "MG"),
                new Instituicao("CEFET-MG", "Belo Horizonte", "MG")
            );
            instituicaoRepository.saveAll(instituicoes);

            Instituicao puc = instituicoes.get(0);

            Professor prof = new Professor();
            prof.setEmail("professor@pucminas.br");
            prof.setSenha(passwordEncoder.encode("professor123"));
            prof.setNome("João Paulo Aramuni");
            prof.setCpf("111.222.333-44");
            prof.setDepartamento("Engenharia de Software");
            prof.setSaldoMoedas(1000);
            prof.setInstituicao(puc);
            professorRepository.save(prof);

            Admin admin = new Admin();
            admin.setEmail("admin@pucminas.br");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setNome("Administrador da Instituição");
            usuarioRepository.save(admin);

            log.info("Dados inicializados com sucesso!");
            log.info("Professor padrão criado: professor@pucminas.br / professor123");
            log.info("Admin padrão criado: admin@pucminas.br / admin123");
        };
    }
}
