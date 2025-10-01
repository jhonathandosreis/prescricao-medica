package com.dev.prescricaomedica.service;

import com.dev.prescricaomedica.dao.ReceitaDAO;
import com.dev.prescricaomedica.model.MedicamentoReceitado;
import com.dev.prescricaomedica.model.Receita;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Stateless
public class ReceitaService {

    @Inject
    private ReceitaDAO receitaDAO;

    @Transactional
    public void salvar(Receita receita) {
        if (receita.getMedicamentosReceitados().isEmpty()) {
            throw new IllegalArgumentException("A receita deve conter pelo menos um medicamento");
        }

        for (MedicamentoReceitado mr : receita.getMedicamentosReceitados()) {
            mr.setReceita(receita);
        }

        if (receita.getId() == null) {
            receitaDAO.save(receita);
        } else {
            receitaDAO.update(receita);
        }
    }

    @Transactional
    public void excluir(Receita receita) {
        receitaDAO.delete(receita);
    }

    public Receita buscarPorId(UUID id) {
        return receitaDAO.findById(id);
    }

    public List<Receita> listarTodos() {
        return receitaDAO.findAll();
    }

    public List<Receita> buscarPorPaciente(UUID pacienteId) {
        return receitaDAO.findByPacienteId(pacienteId);
    }

    public List<Receita> buscarComFiltro(UUID pacienteId, UUID medicamentoId, int first, int pageSize) {
        return receitaDAO.findByFilter(pacienteId, medicamentoId, first, pageSize);
    }

    public Long contarComFiltro(UUID pacienteId, UUID medicamentoId) {
        return receitaDAO.countByFilter(pacienteId, medicamentoId);
    }
}
