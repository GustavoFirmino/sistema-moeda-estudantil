package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProfessorService professorService;
    private final InstituicaoRepository instituicaoRepository;

    @GetMapping("/professores")
    public String professores(Model model) {
        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", new Professor());
        }
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "admin/professores";
    }

    @PostMapping("/professores")
    public String cadastrarProfessor(@Valid @ModelAttribute("professor") Professor professor,
                                     BindingResult result,
                                     @RequestParam String senha,
                                     @RequestParam String confirmarSenha,
                                     Model model,
                                     RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("professores", professorService.listarTodos());
            model.addAttribute("instituicoes", instituicaoRepository.findAll());
            return "admin/professores";
        }
        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            model.addAttribute("professores", professorService.listarTodos());
            model.addAttribute("instituicoes", instituicaoRepository.findAll());
            return "admin/professores";
        }
        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter no mínimo 6 caracteres.");
            model.addAttribute("professores", professorService.listarTodos());
            model.addAttribute("instituicoes", instituicaoRepository.findAll());
            return "admin/professores";
        }
        try {
            professorService.cadastrar(professor, senha);
            redirect.addFlashAttribute("sucesso",
                    "Professor cadastrado com sucesso! 1.000 moedas creditadas.");
            return "redirect:/admin/professores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("professores", professorService.listarTodos());
            model.addAttribute("instituicoes", instituicaoRepository.findAll());
            return "admin/professores";
        }
    }
}
