package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ENVIO")
@Getter
@Setter
public class TransacaoEnvio extends Transacao {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;
}
