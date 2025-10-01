package com.dev.prescricaomedica.dao;

import com.dev.prescricaomedica.model.Medicamento;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class MedicamentoDAO extends GenericDAO<Medicamento, UUID> {

    public MedicamentoDAO() {
        super(Medicamento.class);
    }

    public List<Medicamento> findByFilter(String nome, int first, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Medicamento> cq = cb.createQuery(Medicamento.class);
        Root<Medicamento> root = cq.from(Medicamento.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("nome")));

        TypedQuery<Medicamento> query = entityManager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public Long countByFilter(String nome) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Medicamento> root = cq.from(Medicamento.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        cq.select(cb.count(root));

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getSingleResult();
    }
}
