package com.pucminas.moedaestudantil.exception;

public class VantagemNaoEncontradaException extends RuntimeException {

    public VantagemNaoEncontradaException(Long id) {
        super("Vantagem não encontrada com ID: " + id);
    }
}
