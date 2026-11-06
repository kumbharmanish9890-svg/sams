package com.sams.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sams.dto.PlayerRisk;

@Service
public class ReportService {
    private final InjuryService injuryService;

    public ReportService(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    public byte[] injuryReport() {
        List<PlayerRisk> risks = injuryService.allRisks();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font header = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

            doc.add(new Paragraph("SPORT - Injury Risk Report", title));
            doc.add(new Paragraph("Generated on: " + LocalDate.now(), normal));
            doc.add(new Paragraph("ACWR zones: 0.8-1.3 Safe | 1.3-1.5 Warning | above 1.5 High Risk", normal));
            doc.add(new Paragraph(" "));

            long high = risks.stream().filter(r -> r.getStatus().equals("High Risk")).count();
            long warn = risks.stream().filter(r -> r.getStatus().equals("Warning")).count();
            long safe = risks.stream().filter(r -> r.getStatus().equals("Safe")).count();
            doc.add(new Paragraph("Summary: " + risks.size() + " players | High Risk: " + high
                    + " | Warning: " + warn + " | Safe: " + safe, bold));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 3f, 2f, 2f, 1.5f, 2f });

            String[] heads = { "Player", "Acute (7d)", "Chronic (28d)", "ACWR", "Status" };
            for (String h : heads) {
                PdfPCell cell = new PdfPCell(new Phrase(h, header));
                cell.setBackgroundColor(new Color(30, 60, 114));
                cell.setPadding(6);
                table.addCell(cell);
            }

            for (PlayerRisk r : risks) {
                table.addCell(cell(r.getName(), normal, null));
                table.addCell(cell(String.valueOf(r.getAcute()), normal, null));
                table.addCell(cell(String.valueOf(r.getChronic()), normal, null));
                table.addCell(cell(String.valueOf(r.getAcwr()), bold, null));
                table.addCell(cell(r.getStatus(), bold, statusColor(r.getStatus())));
            }

            doc.add(table);
            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not create PDF", e);
        }
        return out.toByteArray();
    }

    private PdfPCell cell(String text, Font font, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setPadding(6);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        if (bg != null) c.setBackgroundColor(bg);
        return c;
    }

    private Color statusColor(String status) {
        switch (status) {
            case "High Risk": return new Color(255, 160, 160);
            case "Warning":   return new Color(255, 230, 150);
            case "Safe":      return new Color(170, 235, 190);
            default:          return new Color(225, 225, 225);
        }
    }
}