package com.dev.prescricaomedica.bean;

import com.dev.prescricaomedica.model.Medicamento;
import com.dev.prescricaomedica.model.MedicamentoReceitado;
import com.dev.prescricaomedica.model.Paciente;
import com.dev.prescricaomedica.model.Receita;
import com.dev.prescricaomedica.service.MedicamentoService;
import com.dev.prescricaomedica.service.PacienteService;
import com.dev.prescricaomedica.service.ReceitaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Named
@ViewScoped
public class ReceitaBean implements Serializable {

    @Inject
    private ReceitaService receitaService;

    @Inject
    private PacienteService pacienteService;

    @Inject
    private MedicamentoService medicamentoService;

    private Receita receita;
    private Receita receitaSelecionada;
    private LazyDataModel<Receita> lazyModel;

    private UUID filtroPacienteId;
    private UUID filtroMedicamentoId;

    private List<Paciente> pacientes;
    private List<Medicamento> medicamentos;

    private MedicamentoReceitado medicamentoReceitado;
    private Medicamento medicamentoSelecionado;

    @PostConstruct
    public void init() {
        receita = new Receita();
        medicamentoReceitado = new MedicamentoReceitado();
        pacientes = pacienteService.listarTodos();
        medicamentos = medicamentoService.listarTodos();
        carregarLazyModel();
    }

    private void carregarLazyModel() {
        lazyModel = new LazyDataModel<Receita>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return receitaService.contarComFiltro(filtroPacienteId, filtroMedicamentoId).intValue();
            }

            @Override
            public List<Receita> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return receitaService.buscarComFiltro(filtroPacienteId, filtroMedicamentoId, first, pageSize);
            }
        };
    }

    public void novo() {
        receita = new Receita();
        receita.setMedicamentosReceitados(new ArrayList<>());
        medicamentoReceitado = new MedicamentoReceitado();
        medicamentoSelecionado = null;
    }

    public void adicionarMedicamento() {
        if (medicamentoSelecionado == null) {
            addMessage("Atenção", "Selecione um medicamento", FacesMessage.SEVERITY_WARN);
            return;
        }

        MedicamentoReceitado mr = new MedicamentoReceitado();
        mr.setMedicamento(medicamentoSelecionado);
        mr.setPosologia(medicamentoReceitado.getPosologia());

        receita.adicionarMedicamento(mr);

        medicamentoSelecionado = null;
        medicamentoReceitado = new MedicamentoReceitado();

        addMessage("Sucesso", "Medicamento adicionado à receita", FacesMessage.SEVERITY_INFO);
    }

    public void removerMedicamento(MedicamentoReceitado mr) {
        receita.removerMedicamento(mr);
        addMessage("Sucesso", "Medicamento removido da receita", FacesMessage.SEVERITY_INFO);
    }

    public void visualizar(Receita receita) {
        this.receitaSelecionada = receita;
    }

    public void salvar() {
        try {
            if (receita.getPaciente() == null) {
                addMessage("Atenção", "Selecione um paciente", FacesMessage.SEVERITY_WARN);
                return;
            }

            receitaService.salvar(receita);
            addMessage("Sucesso", "Receita salva com sucesso!", FacesMessage.SEVERITY_INFO);
            novo();
        } catch (Exception e) {
            addMessage("Erro", e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void excluir() {
        try {
            receitaService.excluir(receitaSelecionada);
            addMessage("Sucesso", "Receita excluída com sucesso!", FacesMessage.SEVERITY_INFO);
            receitaSelecionada = null;
        } catch (Exception e) {
            addMessage("Erro", "Erro ao excluir receita: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void pesquisar() {
        carregarLazyModel();
    }

    public void limparFiltros() {
        filtroPacienteId = null;
        filtroMedicamentoId = null;
        carregarLazyModel();
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public Receita getReceitaSelecionada() {
        return receitaSelecionada;
    }

    public void setReceitaSelecionada(Receita receitaSelecionada) {
        this.receitaSelecionada = receitaSelecionada;
    }

    public LazyDataModel<Receita> getLazyModel() {
        return lazyModel;
    }

    public UUID getFiltroPacienteId() {
        return filtroPacienteId;
    }

    public void setFiltroPacienteId(UUID filtroPacienteId) {
        this.filtroPacienteId = filtroPacienteId;
    }

    public UUID getFiltroMedicamentoId() {
        return filtroMedicamentoId;
    }

    public void setFiltroMedicamentoId(UUID filtroMedicamentoId) {
        this.filtroMedicamentoId = filtroMedicamentoId;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public MedicamentoReceitado getMedicamentoReceitado() {
        return medicamentoReceitado;
    }

    public void setMedicamentoReceitado(MedicamentoReceitado medicamentoReceitado) {
        this.medicamentoReceitado = medicamentoReceitado;
    }

    public Medicamento getMedicamentoSelecionado() {
        return medicamentoSelecionado;
    }

    public void setMedicamentoSelecionado(Medicamento medicamentoSelecionado) {
        this.medicamentoSelecionado = medicamentoSelecionado;
    }
}
