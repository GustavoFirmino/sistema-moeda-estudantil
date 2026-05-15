package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("RESGATE")
@Getter
@Setter
public class TransacaoResgate extends Transacao {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aluno_resgate_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vantagem_id")
    private Vantagem vantagem;

    // SINGLE_TABLE: a coluna é compartilhada com TransacaoEnvio (cupom nulo),
    // por isso não pode ser NOT NULL no banco. A obrigatoriedade no resgate
    // é garantida em TransacaoService.
    @Column(name = "codigo_cupom", unique = true)
    private String codigoCupom;
}
