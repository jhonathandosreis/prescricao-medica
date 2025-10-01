package com.dev.prescricaomedica.service;

import com.dev.prescricaomedica.dao.MedicamentoReceitadoDAO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class RelatorioService {

    @Inject
    private MedicamentoReceitadoDAO medicamentoReceitadoDAO;

    public List<Object[]> obterMedicamentosMaisPrescritos(int quantidade) {
        return medicamentoReceitadoDAO.findMedicamentosMaisPrescritos(quantidade);
    }

    public List<Object[]> obterPacientesComMaisMedicamentos(int quantidade) {
        return medicamentoReceitadoDAO.findPacientesComMaisMedicamentos(quantidade);
    }

    public List<Object[]> obterTodosPacientesComTotalMedicamentos() {
        return medicamentoReceitadoDAO.findPacientesComTotalMedicamentos();
    }
}
