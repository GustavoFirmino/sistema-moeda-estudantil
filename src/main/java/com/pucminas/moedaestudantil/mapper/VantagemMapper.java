package com.pucminas.moedaestudantil.mapper;

import com.pucminas.moedaestudantil.dto.VantagemDTO;
import com.pucminas.moedaestudantil.model.EmpresaParceira;
import com.pucminas.moedaestudantil.model.Vantagem;
import org.springframework.stereotype.Component;

@Component
public class VantagemMapper {

    public Vantagem toEntity(VantagemDTO dto, EmpresaParceira empresa) {
        Vantagem v = new Vantagem();
        v.setNome(dto.getNome());
        v.setDescricao(dto.getDescricao());
        v.setCustoMoedas(dto.getCustoMoedas());
        v.setAtiva(dto.isAtiva());
        v.setEmpresaParceira(empresa);
        return v;
    }

    public VantagemDTO toDTO(Vantagem vantagem) {
        VantagemDTO dto = new VantagemDTO();
        dto.setId(vantagem.getId());
        dto.setNome(vantagem.getNome());
        dto.setDescricao(vantagem.getDescricao());
        dto.setCustoMoedas(vantagem.getCustoMoedas());
        dto.setAtiva(vantagem.isAtiva());
        dto.setFotoUrlAtual(vantagem.getFotoUrl());
        return dto;
    }

    public void updateEntity(Vantagem vantagem, VantagemDTO dto) {
        vantagem.setNome(dto.getNome());
        vantagem.setDescricao(dto.getDescricao());
        vantagem.setCustoMoedas(dto.getCustoMoedas());
        vantagem.setAtiva(dto.isAtiva());
    }
}
