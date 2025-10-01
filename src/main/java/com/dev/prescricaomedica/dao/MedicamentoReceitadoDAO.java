package com.dev.prescricaomedica.dao;


import com.dev.prescricaomedica.model.MedicamentoReceitado;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

@Stateless
public class MedicamentoReceitadoDAO extends GenericDAO<MedicamentoReceitado, UUID> {

    public MedicamentoReceitadoDAO() {
        super(MedicamentoReceitado.class);
    }

    public List<Object[]> findMedicamentosMaisPrescritos(int limit) {
        String jpql = "SELECT m.medicamento.nome, COUNT(m) as total " +
                "FROM MedicamentoReceitado m " +
                "GROUP BY m.medicamento.id, m.medicamento.nome " +
                "ORDER BY total DESC";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Object[]> findPacientesComMaisMedicamentos(int limit) {
        String jpql = "SELECT r.paciente.nome, COUNT(m) as total " +
                "FROM MedicamentoReceitado m " +
                "JOIN m.receita r " +
                "GROUP BY r.paciente.id, r.paciente.nome " +
                "ORDER BY total DESC";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Object[]> findPacientesComTotalMedicamentos() {
        String jpql = "SELECT r.paciente.nome, COUNT(m) as total " +
                "FROM MedicamentoReceitado m " +
                "JOIN m.receita r " +
                "GROUP BY r.paciente.id, r.paciente.nome " +
                "ORDER BY r.paciente.nome ASC";

        return entityManager.createQuery(jpql, Object[].class).getResultList();
    }
}
