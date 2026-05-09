package com.pucminas.moedaestudantil.util;

public final class MoedaUtils {

    private MoedaUtils() {}

    public static String formatar(int quantidade) {
        if (quantidade == 1) return "1 moeda";
        return quantidade + " moedas";
    }

    public static String gerarCodigoCupom() {
        return java.util.UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    public static boolean saldoSuficiente(int saldo, int custo) {
        return saldo >= custo;
    }
}
