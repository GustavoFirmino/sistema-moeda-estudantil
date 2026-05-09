package com.pucminas.moedaestudantil.util;

public final class CpfCnpjUtils {

    private CpfCnpjUtils() {}

    public static String formatarCpf(String cpf) {
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) return cpf;
        return digits.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarCnpj(String cnpj) {
        String digits = cnpj.replaceAll("\\D", "");
        if (digits.length() != 14) return cnpj;
        return digits.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    public static String removerMascara(String documento) {
        return documento == null ? null : documento.replaceAll("\\D", "");
    }

    public static boolean isCpfValido(String cpf) {
        String d = removerMascara(cpf);
        if (d == null || d.length() != 11 || d.matches("(\\d)\\1{10}")) return false;
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (d.charAt(i) - '0') * (10 - i);
        int r1 = (soma * 10) % 11;
        if (r1 == 10 || r1 == 11) r1 = 0;
        if (r1 != (d.charAt(9) - '0')) return false;
        soma = 0;
        for (int i = 0; i < 10; i++) soma += (d.charAt(i) - '0') * (11 - i);
        int r2 = (soma * 10) % 11;
        if (r2 == 10 || r2 == 11) r2 = 0;
        return r2 == (d.charAt(10) - '0');
    }

    public static boolean isCnpjValido(String cnpj) {
        String d = removerMascara(cnpj);
        if (d == null || d.length() != 14 || d.matches("(\\d)\\1{13}")) return false;
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digitoValido(d, pesos1, 12) && digitoValido(d, pesos2, 13);
    }

    private static boolean digitoValido(String cnpj, int[] pesos, int pos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) soma += (cnpj.charAt(i) - '0') * pesos[i];
        int r = soma % 11;
        int digito = (r < 2) ? 0 : 11 - r;
        return digito == (cnpj.charAt(pos) - '0');
    }
}
