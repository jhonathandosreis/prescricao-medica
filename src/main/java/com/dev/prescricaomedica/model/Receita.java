package com.dev.prescricaomedica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "receita")
public class Receita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Paciente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "data_prescricao", nullable = false)
    private LocalDate dataPrescricao;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicamentoReceitado> medicamentosReceitados = new ArrayList<>();

    public Receita() {
        this.dataPrescricao = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getDataPrescricao() {
        return dataPrescricao;
    }

    public void setDataPrescricao(LocalDate dataPrescricao) {
        this.dataPrescricao = dataPrescricao;
    }

    public List<MedicamentoReceitado> getMedicamentosReceitados() {
        return medicamentosReceitados;
    }

    public void setMedicamentosReceitados(List<MedicamentoReceitado> medicamentosReceitados) {
        this.medicamentosReceitados = medicamentosReceitados;
    }

    public void adicionarMedicamento(MedicamentoReceitado medicamentoReceitado) {
        medicamentosReceitados.add(medicamentoReceitado);
        medicamentoReceitado.setReceita(this);
    }

    public void removerMedicamento(MedicamentoReceitado medicamentoReceitado) {
        medicamentosReceitados.remove(medicamentoReceitado);
        medicamentoReceitado.setReceita(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receita receita = (Receita) o;
        return Objects.equals(id, receita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
