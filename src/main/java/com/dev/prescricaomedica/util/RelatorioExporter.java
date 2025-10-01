package com.dev.prescricaomedica.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RelatorioExporter {

    public static void exportarExcel(List<Object[]> medicamentosMaisPrescritos,
                                     List<Object[]> pacientesComMaisMedicamentos,
                                     List<Object[]> todosPacientes) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        criarAbaTopMedicamentos(workbook, medicamentosMaisPrescritos);
        criarAbaTopPacientes(workbook, pacientesComMaisMedicamentos);
        criarAbaTodosPacientes(workbook, todosPacientes);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_prescricoes.xlsx");

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();

        facesContext.responseComplete();
    }

    private static void criarAbaTopMedicamentos(Workbook workbook, List<Object[]> dados) {
        Sheet sheet = workbook.createSheet("Top 2 Medicamentos");

        CellStyle headerStyle = criarEstiloHeader(workbook);
        CellStyle dataStyle = criarEstiloData(workbook);

        Row headerRow = sheet.createRow(0);
        criarCelula(headerRow, 0, "#", headerStyle);
        criarCelula(headerRow, 1, "Medicamento", headerStyle);
        criarCelula(headerRow, 2, "Total", headerStyle);

        int rowNum = 1;
        for (Object[] row : dados) {
            Row dataRow = sheet.createRow(rowNum);
            criarCelula(dataRow, 0, String.valueOf(rowNum), dataStyle);
            criarCelula(dataRow, 1, (String) row[0], dataStyle);
            criarCelula(dataRow, 2, String.valueOf(row[1]), dataStyle);
            rowNum++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    private static void criarAbaTopPacientes(Workbook workbook, List<Object[]> dados) {
        Sheet sheet = workbook.createSheet("Top 2 Pacientes");

        CellStyle headerStyle = criarEstiloHeader(workbook);
        CellStyle dataStyle = criarEstiloData(workbook);

        Row headerRow = sheet.createRow(0);
        criarCelula(headerRow, 0, "#", headerStyle);
        criarCelula(headerRow, 1, "Paciente", headerStyle);
        criarCelula(headerRow, 2, "Total", headerStyle);

        int rowNum = 1;
        for (Object[] row : dados) {
            Row dataRow = sheet.createRow(rowNum);
            criarCelula(dataRow, 0, String.valueOf(rowNum), dataStyle);
            criarCelula(dataRow, 1, (String) row[0], dataStyle);
            criarCelula(dataRow, 2, String.valueOf(row[1]), dataStyle);
            rowNum++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    private static void criarAbaTodosPacientes(Workbook workbook, List<Object[]> dados) {
        Sheet sheet = workbook.createSheet("Todos os Pacientes");

        CellStyle headerStyle = criarEstiloHeader(workbook);
        CellStyle dataStyle = criarEstiloData(workbook);

        Row headerRow = sheet.createRow(0);
        criarCelula(headerRow, 0, "Paciente", headerStyle);
        criarCelula(headerRow, 1, "Total de Medicamentos", headerStyle);

        int rowNum = 1;
        for (Object[] row : dados) {
            Row dataRow = sheet.createRow(rowNum);
            criarCelula(dataRow, 0, (String) row[0], dataStyle);
            criarCelula(dataRow, 1, String.valueOf(row[1]), dataStyle);
            rowNum++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private static CellStyle criarEstiloHeader(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle criarEstiloData(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static void criarCelula(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public static void exportarPDF(List<Object[]> medicamentosMaisPrescritos,
                                   List<Object[]> pacientesComMaisMedicamentos,
                                   List<Object[]> todosPacientes) throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_prescricoes.pdf");

        OutputStream outputStream = response.getOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph("Relatório de Prescrições Médicas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        Paragraph subtitle1 = new Paragraph("Top 2 - Medicamentos Mais Prescritos", subtitleFont);
        subtitle1.setSpacingBefore(10);
        subtitle1.setSpacingAfter(10);
        document.add(subtitle1);

        PdfPTable table1 = criarTabelaPDF(new String[]{"#", "Medicamento", "Total"}, medicamentosMaisPrescritos, true);
        document.add(table1);

        Paragraph subtitle2 = new Paragraph("Top 2 - Pacientes com Mais Medicamentos", subtitleFont);
        subtitle2.setSpacingBefore(20);
        subtitle2.setSpacingAfter(10);
        document.add(subtitle2);

        PdfPTable table2 = criarTabelaPDF(new String[]{"#", "Paciente", "Total"}, pacientesComMaisMedicamentos, true);
        document.add(table2);

        Paragraph subtitle3 = new Paragraph("Todos os Pacientes - Total de Medicamentos", subtitleFont);
        subtitle3.setSpacingBefore(20);
        subtitle3.setSpacingAfter(10);
        document.add(subtitle3);

        PdfPTable table3 = criarTabelaPDF(new String[]{"Paciente", "Total de Medicamentos"}, todosPacientes, false);
        document.add(table3);

        document.close();
        outputStream.flush();
        outputStream.close();

        facesContext.responseComplete();
    }

    private static PdfPTable criarTabelaPDF(String[] headers, List<Object[]> dados, boolean comNumero) throws DocumentException {
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(102, 126, 234));
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        int rowNum = 1;
        for (Object[] row : dados) {
            if (comNumero) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(rowNum), dataFont));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (Object value : row) {
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(value), dataFont));
                cell.setPadding(5);
                table.addCell(cell);
            }
            rowNum++;
        }

        return table;
    }
}
