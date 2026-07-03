package com.pucminas.moedaestudantil.service;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.model.Vantagem;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final QrCodeService qrCodeService;

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
        String html = String.format(
                "<div style=\"font-family:Arial,sans-serif;max-width:520px\">" +
                "<h2 style=\"color:#2552E6\">🪙 Resgate realizado com sucesso!</h2>" +
                "<p>Olá, <b>%s</b>!</p>" +
                "<p><b>Vantagem:</b> %s<br>" +
                "<b>Empresa:</b> %s<br>" +
                "<b>Custo:</b> %d moedas</p>" +
                "<p style=\"font-size:18px\"><b>Código do cupom:</b> " +
                "<span style=\"background:#EEF2FF;padding:4px 10px;border-radius:6px;letter-spacing:2px\">%s</span></p>" +
                "<p>Apresente o QR Code abaixo (ou o código) na empresa parceira para utilizar sua vantagem:</p>" +
                "<img src=\"cid:qrcode\" alt=\"QR Code do cupom\" width=\"240\" height=\"240\"/>" +
                "<p style=\"color:#6B7280;font-size:12px\">Sistema de Moeda Estudantil — PUC Minas</p>" +
                "</div>",
                aluno.getNome(), vantagem.getNome(),
                vantagem.getEmpresaParceira().getNomeFantasia(),
                vantagem.getCustoMoedas(), codigoCupom
        );
        enviarComQrCode(aluno.getEmail(), assunto, html, codigoCupom, vantagem);
    }

    public void enviarNotificacaoEmpresa(EmpresaParceira empresa, Aluno aluno, Vantagem vantagem, String codigoCupom) {
        String assunto = "Novo resgate: " + vantagem.getNome();
        String html = String.format(
                "<div style=\"font-family:Arial,sans-serif;max-width:520px\">" +
                "<h2 style=\"color:#2552E6\">📦 Novo resgate de vantagem</h2>" +
                "<p>Olá, <b>%s</b>!</p>" +
                "<p><b>Vantagem:</b> %s<br>" +
                "<b>Aluno:</b> %s<br>" +
                "<b>E-mail do aluno:</b> %s</p>" +
                "<p style=\"font-size:18px\"><b>Código de confirmação:</b> " +
                "<span style=\"background:#EEF2FF;padding:4px 10px;border-radius:6px;letter-spacing:2px\">%s</span></p>" +
                "<p>Escaneie o QR Code apresentado pelo aluno e compare com o código acima para conferir a troca:</p>" +
                "<img src=\"cid:qrcode\" alt=\"QR Code do cupom\" width=\"240\" height=\"240\"/>" +
                "<p style=\"color:#6B7280;font-size:12px\">Sistema de Moeda Estudantil — PUC Minas</p>" +
                "</div>",
                empresa.getNomeFantasia(), vantagem.getNome(),
                aluno.getNome(), aluno.getEmail(), codigoCupom
        );
        enviarComQrCode(empresa.getEmail(), assunto, html, codigoCupom, vantagem);
    }

    /**
     * Envia e-mail HTML com o QR Code único do cupom embutido (Lab05S01).
     * O conteúdo do QR identifica o cupom para conferência presencial.
     */
    private void enviarComQrCode(String destinatario, String assunto, String html,
                                 String codigoCupom, Vantagem vantagem) {
        String conteudoQr = String.format("MOEDA-ESTUDANTIL|CUPOM:%s|VANTAGEM:%s|EMPRESA:%s",
                codigoCupom, vantagem.getNome(), vantagem.getEmpresaParceira().getNomeFantasia());
        byte[] qrPng = qrCodeService.gerarPng(conteudoQr);
        qrCodeService.salvarCopiaLocal(codigoCupom, qrPng);

        if (!emailEnabled) {
            log.info("[EMAIL SIMULADO] Para: {} | Assunto: {} | QR Code: uploads/cupons/{}.png",
                    destinatario, assunto, codigoCupom);
            return;
        }
        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(html, true);
            helper.addInline("qrcode", new ByteArrayResource(qrPng), "image/png");
            mailSender.send(mensagem);
        } catch (Exception e) {
            log.error("Erro ao enviar email com QR Code para {}: {}", destinatario, e.getMessage());
        }
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
