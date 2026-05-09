package com.pucminas.moedaestudantil.controller;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Vantagem;
import com.pucminas.moedaestudantil.service.EmpresaParceiraService;
import com.pucminas.moedaestudantil.service.VantagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaParceiraController {

    private final EmpresaParceiraService empresaService;
    private final VantagemService vantagemService;

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
