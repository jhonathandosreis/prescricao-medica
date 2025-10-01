package com.dev.prescricaomedica.dao;

import com.dev.prescricaomedica.model.Receita;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class ReceitaDAO extends GenericDAO<Receita, UUID> {

    public ReceitaDAO() {
        super(Receita.class);
    }

    @Override
    public Receita findById(UUID id) {
        TypedQuery<Receita> query = entityManager.createQuery(
                "SELECT DISTINCT r FROM Receita r " +
                "LEFT JOIN FETCH r.paciente " +
                "LEFT JOIN FETCH r.medicamentosReceitados mr " +
                "LEFT JOIN FETCH mr.medicamento " +
                "WHERE r.id = :id",
                Receita.class);
        query.setParameter("id", id);
        List<Receita> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Receita> findAll() {
        TypedQuery<Receita> query = entityManager.createQuery(
                "SELECT DISTINCT r FROM Receita r " +
                "LEFT JOIN FETCH r.paciente " +
                "LEFT JOIN FETCH r.medicamentosReceitados mr " +
                "LEFT JOIN FETCH mr.medicamento " +
                "ORDER BY r.dataPrescricao DESC",
                Receita.class);
        return query.getResultList();
    }

    public List<Receita> findByPacienteId(UUID pacienteId) {
        TypedQuery<Receita> query = entityManager.createQuery(
                "SELECT DISTINCT r FROM Receita r " +
                "LEFT JOIN FETCH r.paciente " +
                "LEFT JOIN FETCH r.medicamentosReceitados mr " +
                "LEFT JOIN FETCH mr.medicamento " +
                "WHERE r.paciente.id = :pacienteId " +
                "ORDER BY r.dataPrescricao DESC",
                Receita.class);
        query.setParameter("pacienteId", pacienteId);
        return query.getResultList();
    }

    public List<Receita> findByFilter(UUID pacienteId, UUID medicamentoId, int first, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Receita> cq = cb.createQuery(Receita.class);
        Root<Receita> root = cq.from(Receita.class);

        root.fetch("paciente", JoinType.LEFT);
        root.fetch("medicamentosReceitados", JoinType.LEFT).fetch("medicamento", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (pacienteId != null) {
            predicates.add(cb.equal(root.get("paciente").get("id"), pacienteId));
        }

        if (medicamentoId != null) {
            Join<Object, Object> medicamentosJoin = root.join("medicamentosReceitados");
            predicates.add(cb.equal(medicamentosJoin.get("medicamento").get("id"), medicamentoId));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("dataPrescricao")));
        cq.distinct(true);

        TypedQuery<Receita> query = entityManager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public Long countByFilter(UUID pacienteId, UUID medicamentoId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Receita> root = cq.from(Receita.class);

        List<Predicate> predicates = new ArrayList<>();

        if (pacienteId != null) {
            predicates.add(cb.equal(root.get("paciente").get("id"), pacienteId));
        }

        if (medicamentoId != null) {
            Join<Object, Object> medicamentosJoin = root.join("medicamentosReceitados");
            predicates.add(cb.equal(medicamentosJoin.get("medicamento").get("id"), medicamentoId));
        }

        cq.select(cb.countDistinct(root));

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getSingleResult();
    }
}
