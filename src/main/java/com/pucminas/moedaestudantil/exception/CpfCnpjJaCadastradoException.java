package com.pucminas.moedaestudantil.exception;

public class CpfCnpjJaCadastradoException extends RuntimeException {

    public CpfCnpjJaCadastradoException(String documento) {
        super("O documento '" + documento + "' já está vinculado a outro cadastro.");
    }
}
