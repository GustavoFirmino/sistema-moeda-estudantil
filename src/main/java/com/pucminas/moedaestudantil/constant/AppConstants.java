package com.pucminas.moedaestudantil.constant;

public final class AppConstants {

    private AppConstants() {}

    public static final int SENHA_MIN_LENGTH = 6;
    public static final int COUPON_CODE_LENGTH = 8;
    public static final int PROFESSOR_SALDO_INICIAL = 1000;
    public static final int ALUNO_SALDO_INICIAL = 0;

    public static final String ROLE_ALUNO = "ROLE_ALUNO";
    public static final String ROLE_PROFESSOR = "ROLE_PROFESSOR";
    public static final String ROLE_EMPRESA = "ROLE_EMPRESA";

    public static final String UPLOAD_DIR = "uploads/";
    public static final String UPLOAD_URL_PREFIX = "/uploads/";

    public static final String LOGIN_URL = "/auth/login";
    public static final String LOGOUT_URL = "/auth/logout";
    public static final String DASHBOARD_ALUNO = "/aluno/dashboard";
    public static final String DASHBOARD_PROFESSOR = "/professor/dashboard";
    public static final String DASHBOARD_EMPRESA = "/empresa/dashboard";
}
