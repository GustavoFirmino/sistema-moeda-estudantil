package com.pucminas.moedaestudantil.exception;

public class SaldoInsuficienteException extends RuntimeException {

    private final int saldoAtual;
    private final int valorRequerido;

    public SaldoInsuficienteException(int saldoAtual, int valorRequerido) {
        super("Saldo insuficiente. Saldo atual: " + saldoAtual + " moedas. Necessário: " + valorRequerido + " moedas.");
        this.saldoAtual = saldoAtual;
        this.valorRequerido = valorRequerido;
    }

    public int getSaldoAtual() { return saldoAtual; }
    public int getValorRequerido() { return valorRequerido; }
}
