package com.pucminas.moedaestudantil.dto;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.Professor;
import com.pucminas.moedaestudantil.model.TransacaoEnvio;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfessorDashboardDTO {

    private Professor professor;
    private int saldo;
    private List<TransacaoEnvio> ultimosEnvios;
    private List<Aluno> alunosDaInstituicao;
    private long totalMoedasEnviadas;
}
