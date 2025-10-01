package com.dev.prescricaomedica.service;

import com.dev.prescricaomedica.dao.MedicamentoDAO;
import com.dev.prescricaomedica.model.Medicamento;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Stateless
public class MedicamentoService {

    @Inject
    private MedicamentoDAO medicamentoDAO;

    @Transactional
    public void salvar(Medicamento medicamento) {
        if (medicamento.getId() == null) {
            medicamentoDAO.save(medicamento);
        } else {
            medicamentoDAO.update(medicamento);
        }
    }

    @Transactional
    public void excluir(Medicamento medicamento) {
        medicamentoDAO.delete(medicamento);
    }

    public Medicamento buscarPorId(UUID id) {
        return medicamentoDAO.findById(id);
    }

    public List<Medicamento> listarTodos() {
        return medicamentoDAO.findAll();
    }

    public List<Medicamento> buscarComFiltro(String nome, int first, int pageSize) {
        return medicamentoDAO.findByFilter(nome, first, pageSize);
    }

    public Long contarComFiltro(String nome) {
        return medicamentoDAO.countByFilter(nome);
    }
}
