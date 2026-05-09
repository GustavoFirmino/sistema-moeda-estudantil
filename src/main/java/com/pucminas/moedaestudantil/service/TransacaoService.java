package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.*;
import com.pucminas.moedaestudantil.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoEnvioRepository envioRepository;
    private final TransacaoResgateRepository resgateRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final EmailService emailService;

    @Transactional
    public TransacaoEnvio enviarMoedas(Long professorId, Long alunoId, int valor, String mensagem) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado."));
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        if (professor.getSaldoMoedas() < valor) {
            throw new IllegalArgumentException("Saldo insuficiente. Você tem " + professor.getSaldoMoedas() + " moedas.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }

        professor.setSaldoMoedas(professor.getSaldoMoedas() - valor);
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + valor);

        professorRepository.save(professor);
        alunoRepository.save(aluno);

        TransacaoEnvio transacao = new TransacaoEnvio();
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        transacao.setValor(valor);
        transacao.setMensagem(mensagem);
        TransacaoEnvio salva = envioRepository.save(transacao);

        emailService.enviarRecebimentoMoedas(aluno, professor, valor, mensagem);
        return salva;
    }

    @Transactional
    public TransacaoResgate resgatarVantagem(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));

        if (!vantagem.isAtiva()) {
            throw new IllegalArgumentException("Esta vantagem não está mais disponível.");
        }
        if (aluno.getSaldoMoedas() < vantagem.getCustoMoedas()) {
            throw new IllegalArgumentException("Saldo insuficiente. Você tem " + aluno.getSaldoMoedas()
                    + " moedas e esta vantagem custa " + vantagem.getCustoMoedas() + " moedas.");
        }

        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - vantagem.getCustoMoedas());
        alunoRepository.save(aluno);

        String codigoCupom = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        TransacaoResgate resgate = new TransacaoResgate();
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);
        resgate.setValor(vantagem.getCustoMoedas());
        resgate.setCodigoCupom(codigoCupom);
        TransacaoResgate salva = resgateRepository.save(resgate);

        emailService.enviarCupomAluno(aluno, vantagem, codigoCupom);
        emailService.enviarNotificacaoEmpresa(vantagem.getEmpresaParceira(), aluno, vantagem, codigoCupom);

        return salva;
    }

    public List<TransacaoEnvio> extratoEnviosProfessor(Long professorId) {
        return envioRepository.findByProfessorIdOrderByDataHoraDesc(professorId);
    }

    public List<TransacaoEnvio> extratoRecebimentosAluno(Long alunoId) {
        return envioRepository.findByAlunoIdOrderByDataHoraDesc(alunoId);
    }

    public List<TransacaoResgate> extratoResgatesAluno(Long alunoId) {
        return resgateRepository.findByAlunoIdOrderByDataHoraDesc(alunoId);
    }
}
