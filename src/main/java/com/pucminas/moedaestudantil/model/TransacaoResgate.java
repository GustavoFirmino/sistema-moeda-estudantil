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

    @Column(name = "codigo_cupom", nullable = false, unique = true)
    private String codigoCupom;
}
