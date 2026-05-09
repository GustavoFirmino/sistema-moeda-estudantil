package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@DiscriminatorValue("ALUNO")
@Getter
@Setter
public class Aluno extends Usuario {

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @NotBlank
    @Column(nullable = false)
    private String rg;

    @NotBlank
    @Column(nullable = false)
    private String endereco;

    @NotBlank
    @Column(nullable = false)
    private String curso;

    @Column(name = "saldo_moedas", nullable = false)
    private Integer saldoMoedas = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;
}
