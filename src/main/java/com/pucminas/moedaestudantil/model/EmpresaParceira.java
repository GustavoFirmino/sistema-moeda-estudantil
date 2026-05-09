package com.pucminas.moedaestudantil.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas_parceiras")
@DiscriminatorValue("EMPRESA")
@Getter
@Setter
public class EmpresaParceira extends Usuario {

    @NotBlank
    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(unique = true, nullable = false, length = 18)
    private String cnpj;

    @OneToMany(mappedBy = "empresaParceira", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vantagem> vantagens = new ArrayList<>();
}
