package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.service.AlunoService;
import com.pucminas.moedaestudantil.service.ProfessorService;
import com.pucminas.moedaestudantil.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final AlunoService alunoService;
    private final TransacaoService transacaoService;

    private Professor getProfessor(Authentication auth) {
        return professorService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Professor não encontrado."));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Professor professor = getProfessor(auth);
        model.addAttribute("professor", professor);
        model.addAttribute("enviosRecentes", transacaoService.extratoEnviosProfessor(professor.getId())
                .stream().limit(5).toList());
        return "professor/dashboard";
    }

    @GetMapping("/enviar-moedas")
    public String enviarMoedasForm(Authentication auth, Model model) {
        Professor professor = getProfessor(auth);
        model.addAttribute("professor", professor);
        model.addAttribute("alunos", alunoService.listarAtivos());
        return "professor/enviar-moedas";
    }

    @PostMapping("/enviar-moedas")
    public String enviarMoedas(@RequestParam Long alunoId,
                               @RequestParam int valor,
                               @RequestParam String mensagem,
                               Authentication auth,
                               RedirectAttributes redirect) {
        Professor professor = getProfessor(auth);
        try {
            transacaoService.enviarMoedas(professor.getId(), alunoId, valor, mensagem);
            redirect.addFlashAttribute("sucesso", "Moedas enviadas com sucesso! O aluno foi notificado por e-mail.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/professor/enviar-moedas";
    }

    @GetMapping("/extrato")
    public String extrato(Authentication auth, Model model) {
        Professor professor = getProfessor(auth);
        model.addAttribute("professor", professor);
        model.addAttribute("envios", transacaoService.extratoEnviosProfessor(professor.getId()));
        return "professor/extrato";
    }
}
