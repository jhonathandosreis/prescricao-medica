package com.dev.prescricaomedica.service;

import com.dev.prescricaomedica.dao.PacienteDAO;
import com.dev.prescricaomedica.model.Paciente;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Stateless
public class PacienteService {

    @Inject
    private PacienteDAO pacienteDAO;

    @Transactional
    public void salvar(Paciente paciente) {
        if (paciente.getId() == null) {
            validarCpfUnico(paciente.getCpf(), null);
            pacienteDAO.save(paciente);
        } else {
            validarCpfUnico(paciente.getCpf(), paciente.getId());
            pacienteDAO.update(paciente);
        }
    }

    @Transactional
    public void excluir(Paciente paciente) {
        validarExclusao(paciente);
        pacienteDAO.delete(paciente);
    }

    private void validarExclusao(Paciente paciente) {
        Long receitasVinculadas = pacienteDAO.countReceitasByPacienteId(paciente.getId());
        if (receitasVinculadas > 0) {
            throw new IllegalArgumentException("Não é possível excluir o paciente pois existem " + receitasVinculadas + " receita(s) vinculada(s)");
        }
    }

    public Paciente buscarPorId(UUID id) {
        return pacienteDAO.findById(id);
    }

    public List<Paciente> listarTodos() {
        return pacienteDAO.findAll();
    }

    public List<Paciente> buscarComFiltro(String nome, String cpf, int first, int pageSize) {
        return pacienteDAO.findByFilter(nome, cpf, first, pageSize);
    }

    public Long contarComFiltro(String nome, String cpf) {
        return pacienteDAO.countByFilter(nome, cpf);
    }

    private void validarCpfUnico(String cpf, UUID idAtual) {
        Paciente existente = pacienteDAO.findByCpf(cpf);
        if (existente != null && !existente.getId().equals(idAtual)) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema");
        }
    }
}
