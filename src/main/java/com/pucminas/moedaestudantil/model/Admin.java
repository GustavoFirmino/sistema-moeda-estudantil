package com.pucminas.moedaestudantil.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "administradores")
@DiscriminatorValue("ADMIN")
@Getter
@Setter
public class Admin extends Usuario {

    @Column(nullable = false)
    private String nome;
}
