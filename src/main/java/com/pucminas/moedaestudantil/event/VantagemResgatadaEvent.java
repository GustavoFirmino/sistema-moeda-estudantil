package com.pucminas.moedaestudantil.event;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Vantagem;
import org.springframework.context.ApplicationEvent;

public class VantagemResgatadaEvent extends ApplicationEvent {

    private final Aluno aluno;
    private final Vantagem vantagem;
    private final EmpresaParceira empresa;
    private final String codigoCupom;

    public VantagemResgatadaEvent(Object source, Aluno aluno, Vantagem vantagem,
                                   EmpresaParceira empresa, String codigoCupom) {
        super(source);
        this.aluno = aluno;
        this.vantagem = vantagem;
        this.empresa = empresa;
        this.codigoCupom = codigoCupom;
    }

    public Aluno getAluno() { return aluno; }
    public Vantagem getVantagem() { return vantagem; }
    public EmpresaParceira getEmpresa() { return empresa; }
    public String getCodigoCupom() { return codigoCupom; }
}
