package com.pucminas.moedaestudantil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApresentacaoController {

    @GetMapping("/apresentacao")
    public String apresentacao() {
        return "apresentacao/index";
    }
}
