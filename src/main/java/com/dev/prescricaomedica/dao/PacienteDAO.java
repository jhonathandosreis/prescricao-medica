package com.dev.prescricaomedica.dao;

import com.dev.prescricaomedica.model.Paciente;
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
public class PacienteDAO extends GenericDAO<Paciente, UUID> {

    public PacienteDAO() {
        super(Paciente.class);
    }

    public List<Paciente> findByFilter(String nome, String cpf, int first, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Paciente> cq = cb.createQuery(Paciente.class);
        Root<Paciente> root = cq.from(Paciente.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (cpf != null && !cpf.trim().isEmpty()) {
            String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
            predicates.add(cb.like(root.get("cpf"), "%" + cpfSemFormatacao + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("nome")));

        TypedQuery<Paciente> query = entityManager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public Long countByFilter(String nome, String cpf) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Paciente> root = cq.from(Paciente.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (cpf != null && !cpf.trim().isEmpty()) {
            String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
            predicates.add(cb.like(root.get("cpf"), "%" + cpfSemFormatacao + "%"));
        }

        cq.select(cb.count(root));

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getSingleResult();
    }

    public Paciente findByCpf(String cpf) {
        String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");

        TypedQuery<Paciente> query = entityManager.createQuery(
                "SELECT p FROM Paciente p WHERE p.cpf = :cpf", Paciente.class);
        query.setParameter("cpf", cpfSemFormatacao);

        List<Paciente> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public Long countReceitasByPacienteId(UUID pacienteId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM Receita r WHERE r.paciente.id = :pacienteId", Long.class);
        query.setParameter("pacienteId", pacienteId);
        return query.getSingleResult();
    }
}
