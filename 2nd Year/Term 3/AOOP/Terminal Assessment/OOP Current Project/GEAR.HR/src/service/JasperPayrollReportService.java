package service;

import model.PayrollResult;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [ABSTRACTION] Generates the department payroll summary as a PDF using JasperReports.
 * The report template lives at {@code src/reports/payroll_summary.jrxml} and is filled from a
 * {@link JRBeanCollectionDataSource} over the already-filtered {@link PayrollResult} list.
 */
public class JasperPayrollReportService {

    private static final String TEMPLATE = "src/reports/payroll_summary.jrxml";
    private static final String TEMPLATE_ALT = "GEAR.HR/src/reports/payroll_summary.jrxml";

    /**
     * Fills the payroll summary template and writes a PDF to {@code outputFile}.
     *
     * @param results    payroll results for the selected department (already filtered)
     * @param month      payroll month label
     * @param department department/role-group label
     * @param outputFile destination PDF file
     * @throws JRException if the report cannot be compiled, filled, or exported
     * @throws IOException if the template cannot be read
     */
    public void exportToPdf(List<PayrollResult> results, String month, String department, File outputFile)
            throws JRException, IOException {
        List<PayrollResult> data = results != null ? results : new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_MONTH", month);
        params.put("REPORT_DEPARTMENT", department);
        params.put("GENERATED_AT", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try (InputStream template = openTemplate()) {
            JasperReport report = JasperCompileManager.compileReport(template);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data, false);
            JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
            JasperExportManager.exportReportToPdfFile(print, outputFile.getAbsolutePath());
        }
    }

    /** Opens the JRXML template, tolerating launches from the repo root or the GEAR.HR folder. */
    private InputStream openTemplate() throws IOException {
        Path path = Paths.get(TEMPLATE);
        if (!Files.exists(path)) {
            path = Paths.get(TEMPLATE_ALT);
        }
        if (!Files.exists(path)) {
            throw new IOException("JasperReports template not found at " + TEMPLATE);
        }
        return Files.newInputStream(path);
    }
}
