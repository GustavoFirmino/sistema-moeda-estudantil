package com.pucminas.moedaestudantil.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Gera QR Codes únicos para os cupons de resgate (Lab05S01).
 * O QR Code carrega o código do cupom para conferência presencial na empresa parceira.
 */
@Service
@Slf4j
public class QrCodeService {

    private static final int TAMANHO = 320;

    public byte[] gerarPng(String conteudo) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, TAMANHO, TAMANHO,
                    Map.of(
                            EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M,
                            EncodeHintType.CHARACTER_SET, "UTF-8",
                            EncodeHintType.MARGIN, 2
                    ));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao gerar QR Code do cupom: " + e.getMessage(), e);
        }
    }

    /**
     * Salva uma cópia local do QR Code (uploads/cupons) para conferência/demonstração,
     * útil quando o envio real de e-mail está desativado.
     */
    public Path salvarCopiaLocal(String codigoCupom, byte[] png) {
        try {
            Path dir = Path.of("uploads", "cupons");
            Files.createDirectories(dir);
            Path arquivo = dir.resolve(codigoCupom + ".png");
            Files.write(arquivo, png);
            return arquivo;
        } catch (IOException e) {
            log.warn("Não foi possível salvar cópia local do QR Code {}: {}", codigoCupom, e.getMessage());
            return null;
        }
    }
}
