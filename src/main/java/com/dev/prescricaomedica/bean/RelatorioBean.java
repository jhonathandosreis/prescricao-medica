package com.dev.prescricaomedica.bean;

import com.dev.prescricaomedica.service.RelatorioService;
import com.dev.prescricaomedica.util.RelatorioExporter;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class RelatorioBean implements Serializable {

    @Inject
    private RelatorioService relatorioService;

    private List<Object[]> medicamentosMaisPrescritos;
    private List<Object[]> pacientesComMaisMedicamentos;
    private List<Object[]> todosPacientesComTotal;

    @PostConstruct
    public void init() {
        carregarRelatorios();
    }

    public void carregarRelatorios() {
        medicamentosMaisPrescritos = relatorioService.obterMedicamentosMaisPrescritos(2);
        pacientesComMaisMedicamentos = relatorioService.obterPacientesComMaisMedicamentos(2);
        todosPacientesComTotal = relatorioService.obterTodosPacientesComTotalMedicamentos();
    }

    public void exportarExcel() {
        try {
            RelatorioExporter.exportarExcel(
                    medicamentosMaisPrescritos,
                    pacientesComMaisMedicamentos,
                    todosPacientesComTotal
            );
        } catch (Exception e) {
            addMessage("Erro", "Erro ao exportar Excel: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void exportarPDF() {
        try {
            RelatorioExporter.exportarPDF(
                    medicamentosMaisPrescritos,
                    pacientesComMaisMedicamentos,
                    todosPacientesComTotal
            );
        } catch (Exception e) {
            addMessage("Erro", "Erro ao exportar PDF: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public List<Object[]> getMedicamentosMaisPrescritos() {
        return medicamentosMaisPrescritos;
    }

    public List<Object[]> getPacientesComMaisMedicamentos() {
        return pacientesComMaisMedicamentos;
    }

    public List<Object[]> getTodosPacientesComTotal() {
        return todosPacientesComTotal;
    }
}
