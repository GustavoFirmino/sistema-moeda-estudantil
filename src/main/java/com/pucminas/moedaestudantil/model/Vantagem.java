package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vantagens")
@Getter
@Setter
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "foto_url")
    private String fotoUrl;

    @NotNull
    @Min(1)
    @Column(name = "custo_moedas", nullable = false)
    private Integer custoMoedas;

    @Column(nullable = false)
    private boolean ativa = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaParceira empresaParceira;
}
