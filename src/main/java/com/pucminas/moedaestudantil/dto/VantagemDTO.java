package com.pucminas.moedaestudantil.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VantagemDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O custo em moedas é obrigatório.")
    @Min(value = 1, message = "O custo deve ser pelo menos 1 moeda.")
    private Integer custoMoedas;

    private boolean ativa = true;
    private String fotoUrlAtual;
    private MultipartFile foto;
}
