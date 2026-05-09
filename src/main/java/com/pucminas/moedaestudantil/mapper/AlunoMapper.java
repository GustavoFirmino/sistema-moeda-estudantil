package com.pucminas.moedaestudantil.mapper;

import com.pucminas.moedaestudantil.dto.CadastroAlunoDTO;
import com.pucminas.moedaestudantil.model.Aluno;
import com.pucminas.moedaestudantil.model.Instituicao;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno toEntity(CadastroAlunoDTO dto, Instituicao instituicao) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setCpf(dto.getCpf());
        aluno.setRg(dto.getRg());
        aluno.setEndereco(dto.getEndereco());
        aluno.setCurso(dto.getCurso());
        aluno.setInstituicao(instituicao);
        return aluno;
    }

    public CadastroAlunoDTO toDTO(Aluno aluno) {
        CadastroAlunoDTO dto = new CadastroAlunoDTO();
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setCpf(aluno.getCpf());
        dto.setRg(aluno.getRg());
        dto.setEndereco(aluno.getEndereco());
        dto.setCurso(aluno.getCurso());
        if (aluno.getInstituicao() != null) dto.setInstituicaoId(aluno.getInstituicao().getId());
        return dto;
    }
}
