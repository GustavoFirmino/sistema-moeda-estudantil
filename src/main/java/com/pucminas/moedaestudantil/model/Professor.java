package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professores")
@DiscriminatorValue("PROFESSOR")
@Getter
@Setter
public class Professor extends Usuario {

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotBlank
    @Column(nullable = false)
    private String departamento;

    @Column(name = "saldo_moedas", nullable = false)
    private Integer saldoMoedas = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;
}
