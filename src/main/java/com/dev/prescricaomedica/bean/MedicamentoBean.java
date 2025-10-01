package com.dev.prescricaomedica.bean;

import com.dev.prescricaomedica.model.Medicamento;
import com.dev.prescricaomedica.service.MedicamentoService;
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
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class MedicamentoBean implements Serializable {

    @Inject
    private MedicamentoService medicamentoService;

    private Medicamento medicamento;
    private Medicamento medicamentoSelecionado;
    private LazyDataModel<Medicamento> lazyModel;
    private String filtroNome;

    @PostConstruct
    public void init() {
        medicamento = new Medicamento();
        carregarLazyModel();
    }

    private void carregarLazyModel() {
        lazyModel = new LazyDataModel<Medicamento>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return medicamentoService.contarComFiltro(filtroNome).intValue();
            }

            @Override
            public List<Medicamento> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return medicamentoService.buscarComFiltro(filtroNome, first, pageSize);
            }
        };
    }

    public void novo() {
        medicamento = new Medicamento();
    }

    public void salvar() {
        try {
            medicamentoService.salvar(medicamento);
            addMessage("Sucesso", "Medicamento salvo com sucesso!", FacesMessage.SEVERITY_INFO);
            medicamento = new Medicamento();
        } catch (Exception e) {
            addMessage("Erro", "Erro ao salvar medicamento: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void editar() {
        medicamento = medicamentoSelecionado;
    }

    public void excluir() {
        try {
            medicamentoService.excluir(medicamentoSelecionado);
            addMessage("Sucesso", "Medicamento excluído com sucesso!", FacesMessage.SEVERITY_INFO);
            medicamentoSelecionado = null;
        } catch (Exception e) {
            addMessage("Erro", "Erro ao excluir medicamento: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void pesquisar() {
    }

    public void limparFiltros() {
        filtroNome = null;
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Medicamento getMedicamentoSelecionado() {
        return medicamentoSelecionado;
    }

    public void setMedicamentoSelecionado(Medicamento medicamentoSelecionado) {
        this.medicamentoSelecionado = medicamentoSelecionado;
    }

    public LazyDataModel<Medicamento> getLazyModel() {
        return lazyModel;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }
}
