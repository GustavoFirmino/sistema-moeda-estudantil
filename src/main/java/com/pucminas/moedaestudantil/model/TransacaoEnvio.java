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

    // SINGLE_TABLE: coluna compartilhada com TransacaoResgate (mensagem nula),
    // por isso não pode ser NOT NULL no banco. A obrigatoriedade no envio
    // é validada em EnviarMoedasDTO/TransacaoService.
    @Column(columnDefinition = "TEXT")
    private String mensagem;
}
