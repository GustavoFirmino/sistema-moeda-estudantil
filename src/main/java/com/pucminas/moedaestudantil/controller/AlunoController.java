package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.TransacaoEnvio;
import com.pucminas.moedaestudantil.model.TransacaoResgate;
import com.pucminas.moedaestudantil.service.AlunoService;
import com.pucminas.moedaestudantil.service.TransacaoService;
import com.pucminas.moedaestudantil.service.VantagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/aluno")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final TransacaoService transacaoService;
    private final VantagemService vantagemService;

    private Aluno getAluno(Authentication auth) {
        return alunoService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Aluno não encontrado."));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Aluno aluno = getAluno(auth);
        List<TransacaoEnvio> recentes = transacaoService.extratoRecebimentosAluno(aluno.getId());
        model.addAttribute("aluno", aluno);
        model.addAttribute("recentes", recentes.stream().limit(5).toList());
        return "aluno/dashboard";
    }

    @GetMapping("/extrato")
    public String extrato(Authentication auth, Model model) {
        Aluno aluno = getAluno(auth);
        model.addAttribute("aluno", aluno);
        model.addAttribute("recebimentos", transacaoService.extratoRecebimentosAluno(aluno.getId()));
        model.addAttribute("resgates", transacaoService.extratoResgatesAluno(aluno.getId()));
        return "aluno/extrato";
    }

    @GetMapping("/vantagens")
    public String vantagens(Authentication auth, Model model) {
        Aluno aluno = getAluno(auth);
        model.addAttribute("aluno", aluno);
        model.addAttribute("vantagens", vantagemService.listarDisponiveis());
        return "aluno/vantagens";
    }

    @PostMapping("/resgatar/{vantagemId}")
    public String resgatar(@PathVariable Long vantagemId, Authentication auth, RedirectAttributes redirect) {
        Aluno aluno = getAluno(auth);
        try {
            transacaoService.resgatarVantagem(aluno.getId(), vantagemId);
            redirect.addFlashAttribute("sucesso", "Vantagem resgatada! Verifique seu e-mail para o cupom.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/aluno/vantagens";
    }

    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        model.addAttribute("aluno", getAluno(auth));
        return "aluno/perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute("aluno") Aluno alunoForm,
                                  Authentication auth,
                                  RedirectAttributes redirect) {
        Aluno aluno = getAluno(auth);
        alunoForm.setId(aluno.getId());
        try {
            alunoService.atualizar(alunoForm);
            redirect.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }
        return "redirect:/aluno/perfil";
    }

    @PostMapping("/desativar")
    public String desativar(Authentication auth, RedirectAttributes redirect) {
        Aluno aluno = getAluno(auth);
        alunoService.desativar(aluno.getId());
        redirect.addFlashAttribute("sucesso", "Conta desativada.");
        return "redirect:/auth/logout";
    }
}
