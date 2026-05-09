package com.pucminas.moedaestudantil.dto;

import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.TransacaoResgate;
import com.pucminas.moedaestudantil.model.Vantagem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmpresaDashboardDTO {

    private EmpresaParceira empresa;
    private List<Vantagem> vantagens;
    private List<TransacaoResgate> ultimosResgates;
    private long totalVantagensAtivas;
    private long totalResgatesRecebidos;
}
