package com.pucminas.moedaestudantil.dto;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.TransacaoEnvio;
import com.pucminas.moedaestudantil.model.TransacaoResgate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlunoDashboardDTO {

    private Aluno aluno;
    private int saldo;
    private List<TransacaoEnvio> ultimosRecebimentos;
    private List<TransacaoResgate> ultimosResgates;
    private long totalRecebimentos;
    private long totalResgates;
}
