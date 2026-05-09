package com.pucminas.moedaestudantil.security;

import com.pucminas.moedaestudantil.constant.AppConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<String> getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        return Optional.of(auth.getName());
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    public static boolean isAluno() {
        return hasRole(AppConstants.ROLE_ALUNO);
    }

    public static boolean isProfessor() {
        return hasRole(AppConstants.ROLE_PROFESSOR);
    }

    public static boolean isEmpresa() {
        return hasRole(AppConstants.ROLE_EMPRESA);
    }
}
