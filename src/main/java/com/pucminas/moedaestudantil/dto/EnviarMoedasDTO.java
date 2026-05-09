package com.pucminas.moedaestudantil.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnviarMoedasDTO {

    @NotNull(message = "Selecione o aluno destinatário.")
    private Long alunoId;

    @NotNull
    @Min(value = 1, message = "O valor deve ser pelo menos 1 moeda.")
    private Integer valor;

    @NotBlank(message = "A mensagem é obrigatória.")
    private String mensagem;
}
