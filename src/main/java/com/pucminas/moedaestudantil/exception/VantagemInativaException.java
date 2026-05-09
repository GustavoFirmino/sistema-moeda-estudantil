package com.pucminas.moedaestudantil.exception;

public class VantagemInativaException extends RuntimeException {

    public VantagemInativaException(Long vantagemId) {
        super("A vantagem com ID " + vantagemId + " não está disponível para resgate.");
    }
}
