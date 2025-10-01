package com.dev.prescricaomedica.bean;

import com.dev.prescricaomedica.model.Paciente;
import com.dev.prescricaomedica.service.PacienteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class PacienteBean implements Serializable {

    @Inject
    private PacienteService pacienteService;

    private Paciente paciente;
    private Paciente pacienteSelecionado;
    private LazyDataModel<Paciente> lazyModel;
    private String filtroNome;
    private String filtroCpf;

    @PostConstruct
    public void init() {
        paciente = new Paciente();
        carregarLazyModel();
    }

    private void carregarLazyModel() {
        lazyModel = new LazyDataModel<Paciente>() {
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return pacienteService.contarComFiltro(filtroNome, filtroCpf).intValue();
            }

            @Override
            public List<Paciente> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return pacienteService.buscarComFiltro(filtroNome, filtroCpf, first, pageSize);
            }
        };
    }

    public void novo() {
        paciente = new Paciente();
    }

    public void salvar() {
        try {
            pacienteService.salvar(paciente);
            addMessage("Sucesso", "Paciente salvo com sucesso!", FacesMessage.SEVERITY_INFO);
            paciente = new Paciente();
        } catch (Exception e) {
            addMessage("Erro", e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void editar() {
        paciente = pacienteSelecionado;
    }

    public void excluir() {
        try {
            pacienteService.excluir(pacienteSelecionado);
            addMessage("Sucesso", "Paciente excluído com sucesso!", FacesMessage.SEVERITY_INFO);
            pacienteSelecionado = null;
        } catch (Exception e) {
            addMessage("Erro", "Erro ao excluir paciente: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void pesquisar() {
        carregarLazyModel();
    }

    public void limparFiltros() {
        filtroNome = null;
        filtroCpf = null;
        carregarLazyModel();
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paciente getPacienteSelecionado() {
        return pacienteSelecionado;
    }

    public void setPacienteSelecionado(Paciente pacienteSelecionado) {
        this.pacienteSelecionado = pacienteSelecionado;
    }

    public LazyDataModel<Paciente> getLazyModel() {
        return lazyModel;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }

    public String getFiltroCpf() {
        return filtroCpf;
    }

    public void setFiltroCpf(String filtroCpf) {
        this.filtroCpf = filtroCpf;
    }
}
