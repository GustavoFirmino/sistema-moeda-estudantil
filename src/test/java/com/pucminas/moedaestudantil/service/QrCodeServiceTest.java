package com.pucminas.moedaestudantil.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeServiceTest {

    private final QrCodeService qrCodeService = new QrCodeService();

    @Test
    void gerarPngDeveProduzirImagemPngValida() throws Exception {
        byte[] png = qrCodeService.gerarPng("MOEDA-ESTUDANTIL|CUPOM:ABC12345|VANTAGEM:Desconto|EMPRESA:Restaurante X");

        assertNotNull(png);
        assertTrue(png.length > 200, "PNG deve ter conteúdo");
        // Assinatura PNG: 0x89 'P' 'N' 'G'
        assertEquals((byte) 0x89, png[0]);
        assertEquals('P', png[1]);
        assertEquals('N', png[2]);
        assertEquals('G', png[3]);

        var imagem = javax.imageio.ImageIO.read(new ByteArrayInputStream(png));
        assertNotNull(imagem, "PNG deve ser decodificável");
        assertEquals(320, imagem.getWidth());
        assertEquals(320, imagem.getHeight());
    }

    @Test
    void qrCodesDeCuponsDiferentesDevemSerDiferentes() {
        byte[] qr1 = qrCodeService.gerarPng("CUPOM:AAAA1111");
        byte[] qr2 = qrCodeService.gerarPng("CUPOM:BBBB2222");
        assertFalse(java.util.Arrays.equals(qr1, qr2), "Cupons diferentes devem gerar QR Codes diferentes");
    }
}
