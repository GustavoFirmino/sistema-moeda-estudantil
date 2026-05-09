package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.model.Vantagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@moedaestudantil.com}")
    private String remetente;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    public void enviarRecebimentoMoedas(Aluno aluno, Professor professor, int valor, String mensagem) {
        String assunto = "Você recebeu " + valor + " moedas! 🪙";
        String corpo = String.format(
                "Olá, %s!\n\n" +
                "O professor(a) %s reconheceu seu mérito e enviou %d moeda(s) para você.\n\n" +
                "Motivo: \"%s\"\n\n" +
                "Seu saldo atual é: %d moedas.\n\n" +
                "Acesse o sistema para trocar suas moedas por vantagens exclusivas!\n\n" +
                "Sistema de Moeda Estudantil — PUC Minas",
                aluno.getNome(), professor.getNome(), valor, mensagem, aluno.getSaldoMoedas()
        );
        enviar(aluno.getEmail(), assunto, corpo);
    }

    public void enviarCupomAluno(Aluno aluno, Vantagem vantagem, String codigoCupom) {
        String assunto = "Seu cupom: " + vantagem.getNome();
        String corpo = String.format(
                "Olá, %s!\n\n" +
                "Seu resgate foi realizado com sucesso!\n\n" +
                "Vantagem: %s\n" +
                "Empresa: %s\n" +
                "Custo: %d moedas\n\n" +
                "Código do cupom: %s\n\n" +
                "Apresente este código na empresa parceira para utilizar sua vantagem.\n\n" +
                "Sistema de Moeda Estudantil — PUC Minas",
                aluno.getNome(), vantagem.getNome(),
                vantagem.getEmpresaParceira().getNomeFantasia(),
                vantagem.getCustoMoedas(), codigoCupom
        );
        enviar(aluno.getEmail(), assunto, corpo);
    }

    public void enviarNotificacaoEmpresa(EmpresaParceira empresa, Aluno aluno, Vantagem vantagem, String codigoCupom) {
        String assunto = "Novo resgate: " + vantagem.getNome();
        String corpo = String.format(
                "Olá, %s!\n\n" +
                "Um aluno resgatou uma de suas vantagens.\n\n" +
                "Vantagem: %s\n" +
                "Aluno: %s\n" +
                "Email do aluno: %s\n\n" +
                "Código de confirmação: %s\n\n" +
                "Utilize este código para conferir a troca presencialmente.\n\n" +
                "Sistema de Moeda Estudantil — PUC Minas",
                empresa.getNomeFantasia(), vantagem.getNome(),
                aluno.getNome(), aluno.getEmail(), codigoCupom
        );
        enviar(empresa.getEmail(), assunto, corpo);
    }

    private void enviar(String destinatario, String assunto, String corpo) {
        if (!emailEnabled) {
            log.info("[EMAIL SIMULADO] Para: {} | Assunto: {}", destinatario, assunto);
            log.debug("[EMAIL CORPO] {}", corpo);
            return;
        }
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);
            mailSender.send(mensagem);
        } catch (Exception e) {
            log.error("Erro ao enviar email para {}: {}", destinatario, e.getMessage());
        }
    }
}
