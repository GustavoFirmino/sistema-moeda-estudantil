package com.pucminas.moedaestudantil.event;

import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.Professor;
import org.springframework.context.ApplicationEvent;

public class MoedasEnviadasEvent extends ApplicationEvent {

    private final Professor professor;
    private final Aluno aluno;
    private final int valor;
    private final String mensagem;

    public MoedasEnviadasEvent(Object source, Professor professor, Aluno aluno, int valor, String mensagem) {
        super(source);
        this.professor = professor;
        this.aluno = aluno;
        this.valor = valor;
        this.mensagem = mensagem;
    }

    public Professor getProfessor() { return professor; }
    public Aluno getAluno() { return aluno; }
    public int getValor() { return valor; }
    public String getMensagem() { return mensagem; }
}
