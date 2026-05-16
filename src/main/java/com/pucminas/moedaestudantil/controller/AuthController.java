package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.service.AlunoService;
import com.pucminas.moedaestudantil.service.EmpresaParceiraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AlunoService alunoService;
    private final EmpresaParceiraService empresaService;
    private final InstituicaoRepository instituicaoRepository;

    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return "redirect:/auth/login";
        boolean isAluno     = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"));
        boolean isProfessor = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"));
        boolean isAdmin     = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAluno)     return "redirect:/aluno/dashboard";
        if (isProfessor) return "redirect:/professor/dashboard";
        if (isAdmin)     return "redirect:/admin/professores";
        return "redirect:/empresa/dashboard";
    }

    @GetMapping("/auth/login")
    public String login(@RequestParam(required = false) String erro,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (erro != null) model.addAttribute("erro", "E-mail ou senha incorretos.");
        if (logout != null) model.addAttribute("sucesso", "Você saiu com sucesso.");
        return "auth/login";
    }

    @GetMapping("/auth/cadastro-aluno")
    public String cadastroAlunoForm(Model model) {
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "auth/cadastro-aluno";
    }

    @PostMapping("/auth/cadastro-aluno")
    public String cadastroAluno(@Valid @ModelAttribute("aluno") Aluno aluno,
                                BindingResult result,
                                @RequestParam String senha,
                                @RequestParam String confirmarSenha,
                                Model model,
                                RedirectAttributes redirect) {
        model.addAttribute("instituicoes", instituicaoRepository.findAll());

        if (result.hasErrors()) return "auth/cadastro-aluno";

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "auth/cadastro-aluno";
        }
        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter no mínimo 6 caracteres.");
            return "auth/cadastro-aluno";
        }

        try {
            alunoService.cadastrar(aluno, senha);
            redirect.addFlashAttribute("sucesso", "Cadastro realizado com sucesso! Faça seu login.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "auth/cadastro-aluno";
        }
    }

    @GetMapping("/auth/cadastro-empresa")
    public String cadastroEmpresaForm(Model model) {
        model.addAttribute("empresa", new EmpresaParceira());
        return "auth/cadastro-empresa";
    }

    @PostMapping("/auth/cadastro-empresa")
    public String cadastroEmpresa(@Valid @ModelAttribute("empresa") EmpresaParceira empresa,
                                  BindingResult result,
                                  @RequestParam String senha,
                                  @RequestParam String confirmarSenha,
                                  Model model,
                                  RedirectAttributes redirect) {
        if (result.hasErrors()) return "auth/cadastro-empresa";

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "auth/cadastro-empresa";
        }
        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter no mínimo 6 caracteres.");
            return "auth/cadastro-empresa";
        }

        try {
            empresaService.cadastrar(empresa, senha);
            redirect.addFlashAttribute("sucesso", "Empresa cadastrada com sucesso! Faça seu login.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "auth/cadastro-empresa";
        }
    }
}
