package com.pucminas.moedaestudantil.handler;

import com.pucminas.moedaestudantil.exception.SaldoInsuficienteException;
import com.pucminas.moedaestudantil.exception.UsuarioNaoEncontradoException;
import com.pucminas.moedaestudantil.exception.VantagemInativaException;
import com.pucminas.moedaestudantil.exception.VantagemNaoEncontradaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoInsuficienteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleSaldoInsuficiente(SaldoInsuficienteException ex, Model model) {
        log.warn("Saldo insuficiente: {}", ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        model.addAttribute("status", 422);
        return "error/generico";
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex, Model model) {
        log.warn("Usuário não encontrado: {}", ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        model.addAttribute("status", 404);
        return "error/generico";
    }

    @ExceptionHandler({VantagemNaoEncontradaException.class, VantagemInativaException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleVantagem(RuntimeException ex, Model model) {
        log.warn("Erro de vantagem: {}", ex.getMessage());
        model.addAttribute("erro", ex.getMessage());
        model.addAttribute("status", 404);
        return "error/generico";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenerico(Exception ex, Model model) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        model.addAttribute("erro", "Ocorreu um erro inesperado. Tente novamente.");
        model.addAttribute("status", 500);
        return "error/generico";
    }
}
