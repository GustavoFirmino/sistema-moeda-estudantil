package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.model.Vantagem;
import com.pucminas.moedaestudantil.repository.InstituicaoRepository;
import com.pucminas.moedaestudantil.service.EmpresaParceiraService;
import com.pucminas.moedaestudantil.service.ProfessorService;
import com.pucminas.moedaestudantil.service.VantagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaParceiraController {

    private final EmpresaParceiraService empresaService;
    private final VantagemService vantagemService;
    private final ProfessorService professorService;
    private final InstituicaoRepository instituicaoRepository;

    private EmpresaParceira getEmpresa(Authentication auth) {
        return empresaService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Empresa não encontrada."));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        EmpresaParceira empresa = getEmpresa(auth);
        model.addAttribute("empresa", empresa);
        model.addAttribute("vantagens", vantagemService.listarPorEmpresa(empresa.getId()));
        return "empresa/dashboard";
    }

    @GetMapping("/vantagens")
    public String vantagens(Authentication auth, Model model) {
        EmpresaParceira empresa = getEmpresa(auth);
        model.addAttribute("empresa", empresa);
        model.addAttribute("vantagens", vantagemService.listarPorEmpresa(empresa.getId()));
        return "empresa/vantagens";
    }

    @GetMapping("/vantagens/nova")
    public String novaVantagemForm(Model model) {
        model.addAttribute("vantagem", new Vantagem());
        return "empresa/nova-vantagem";
    }

    @PostMapping("/vantagens")
    public String salvarVantagem(@ModelAttribute("vantagem") Vantagem vantagem,
                                 @RequestParam(value = "foto", required = false) MultipartFile foto,
                                 Authentication auth,
                                 RedirectAttributes redirect) {
        EmpresaParceira empresa = getEmpresa(auth);
        try {
            vantagemService.cadastrar(vantagem, foto, empresa);
            redirect.addFlashAttribute("sucesso", "Vantagem cadastrada com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao cadastrar vantagem: " + e.getMessage());
        }
        return "redirect:/empresa/vantagens";
    }

    @GetMapping("/vantagens/{id}/editar")
    public String editarVantagemForm(@PathVariable Long id, Authentication auth, Model model) {
        EmpresaParceira empresa = getEmpresa(auth);
        Vantagem vantagem = vantagemService.buscarPorId(id)
                .filter(v -> v.getEmpresaParceira().getId().equals(empresa.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));
        model.addAttribute("vantagem", vantagem);
        return "empresa/editar-vantagem";
    }

    @PostMapping("/vantagens/{id}")
    public String atualizarVantagem(@PathVariable Long id,
                                    @ModelAttribute("vantagem") Vantagem vantagem,
                                    @RequestParam(value = "foto", required = false) MultipartFile foto,
                                    Authentication auth,
                                    RedirectAttributes redirect) {
        EmpresaParceira empresa = getEmpresa(auth);
        vantagemService.buscarPorId(id)
                .filter(v -> v.getEmpresaParceira().getId().equals(empresa.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));
        vantagem.setId(id);
        try {
            vantagemService.atualizar(vantagem, foto);
            redirect.addFlashAttribute("sucesso", "Vantagem atualizada com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/empresa/vantagens";
    }

    @PostMapping("/vantagens/{id}/desativar")
    public String desativarVantagem(@PathVariable Long id, Authentication auth, RedirectAttributes redirect) {
        EmpresaParceira empresa = getEmpresa(auth);
        vantagemService.buscarPorId(id)
                .filter(v -> v.getEmpresaParceira().getId().equals(empresa.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Vantagem não encontrada."));
        vantagemService.desativar(id);
        redirect.addFlashAttribute("sucesso", "Vantagem desativada.");
        return "redirect:/empresa/vantagens";
    }

    @GetMapping("/professores")
    public String professores(Authentication auth, Model model) {
        model.addAttribute("empresa", getEmpresa(auth));
        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", new Professor());
        }
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "empresa/professores";
    }

    @PostMapping("/professores")
    public String cadastrarProfessor(@Valid @ModelAttribute("professor") Professor professor,
                                     BindingResult result,
                                     @RequestParam String senha,
                                     @RequestParam String confirmarSenha,
                                     Authentication auth,
                                     Model model,
                                     RedirectAttributes redirect) {
        model.addAttribute("empresa", getEmpresa(auth));
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        if (result.hasErrors()) {
            return "empresa/professores";
        }
        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            return "empresa/professores";
        }
        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter no mínimo 6 caracteres.");
            return "empresa/professores";
        }
        try {
            professorService.cadastrar(professor, senha);
            redirect.addFlashAttribute("sucesso",
                    "Professor cadastrado com sucesso! 1.000 moedas creditadas.");
            return "redirect:/empresa/professores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "empresa/professores";
        }
    }

    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        model.addAttribute("empresa", getEmpresa(auth));
        return "empresa/perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@ModelAttribute("empresa") EmpresaParceira empresaForm,
                                  Authentication auth,
                                  RedirectAttributes redirect) {
        EmpresaParceira empresa = getEmpresa(auth);
        empresaForm.setId(empresa.getId());
        try {
            empresaService.atualizar(empresaForm);
            redirect.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/empresa/perfil";
    }

    @PostMapping("/desativar")
    public String desativar(Authentication auth, RedirectAttributes redirect) {
        EmpresaParceira empresa = getEmpresa(auth);
        empresaService.desativar(empresa.getId());
        redirect.addFlashAttribute("sucesso", "Conta desativada.");
        return "redirect:/auth/logout";
    }
}
