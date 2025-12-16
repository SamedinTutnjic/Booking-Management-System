package ba.una.booking.util;

import ba.una.booking.model.Booking;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class BookingFilmExcelExporter {

    public static void export(String filmName, List<Booking> bookings, File file) {

        try (Workbook wb = new XSSFWorkbook()) {

            Sheet sheet = wb.createSheet("Bookings");

            // =========================
            // FONTS
            // =========================
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontHeightInPoints((short) 12);

            Font normalFont = wb.createFont();
            normalFont.setFontHeightInPoints((short) 11);

            Font statusFont = wb.createFont();
            statusFont.setBold(true);

            // =========================
            // HEADER STYLE (PLAVI)
            // =========================
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            ((XSSFCellStyle) headerStyle).setFillForegroundColor(
                    new XSSFColor(new Color(59, 130, 246), null) // #3B82F6
            );

            setBorders(headerStyle);

            // =========================
            // NORMAL CELL STYLE
            // =========================
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(normalFont);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(cellStyle);

            // =========================
            // DATE STYLE
            // =========================
            CellStyle dateStyle = wb.createCellStyle();
            dateStyle.cloneStyleFrom(cellStyle);
            dateStyle.setDataFormat(
                    wb.getCreationHelper()
                            .createDataFormat()
                            .getFormat("dd.MM.yyyy")
            );

            // =========================
            // STATUS STYLES
            // =========================
            CellStyle statusGreen = createStatusStyle(wb, statusFont, new Color(187, 247, 208));
            CellStyle statusYellow = createStatusStyle(wb, statusFont, new Color(254, 243, 199));
            CellStyle statusRed = createStatusStyle(wb, statusFont, new Color(254, 202, 202));

            // =========================
            // HEADER ROW
            // =========================
            String[] headers = {
                    "Film",
                    "Partner",
                    "Datum početka",
                    "Datum završetka",
                    "Materijal",
                    "Status",
                    "Kreirao",
                    "Kreirano"
            };

            Row header = sheet.createRow(0);
            header.setHeightInPoints(28);

            for (int i = 0; i < headers.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            // =========================
            // DATA ROWS
            // =========================
            int rowIdx = 1;
            for (Booking b : bookings) {

                Row r = sheet.createRow(rowIdx++);
                r.setHeightInPoints(24);

                createCell(r, 0, b.getFilm().getNaziv(), cellStyle);
                createCell(r, 1, b.getPartner().getNazivKina(), cellStyle);
                createCell(r, 2, b.getDatumPocetka(), dateStyle);
                createCell(r, 3, b.getDatumZavrsetka(), dateStyle);
                createCell(r, 4, b.getTipMaterijala(), cellStyle);

                Cell statusCell = r.createCell(5);
                statusCell.setCellValue(formatStatus(b.getStatus()));
                statusCell.setCellStyle(
                        switch (b.getStatus()) {
                            case POTVRDJENO -> statusGreen;
                            case ODBIJEN -> statusRed;
                            default -> statusYellow;
                        }
                );

                createCell(r, 6, "referent" + b.getCreatedByUserId(), cellStyle);
                createCell(r, 7, b.getCreatedAt(), dateStyle);
            }

            // =========================
            // UX POLISH
            // =========================
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(
                    new CellRangeAddress(0, rowIdx - 1, 0, headers.length - 1)
            );

            sheet.setColumnWidth(0, 5200);
            sheet.setColumnWidth(1, 6500);
            sheet.setColumnWidth(2, 5200);
            sheet.setColumnWidth(3, 5200);
            sheet.setColumnWidth(4, 4800);
            sheet.setColumnWidth(5, 5200);
            sheet.setColumnWidth(6, 5200);
            sheet.setColumnWidth(7, 5200);

            // =========================
            // WRITE FILE
            // =========================
            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // HELPERS
    // =========================
    private static void createCell(Row r, int col, String value, CellStyle style) {
        Cell c = r.createCell(col);
        c.setCellValue(value);
        c.setCellStyle(style);
    }

    private static void createCell(Row r, int col, java.time.LocalDate value, CellStyle style) {
        Cell c = r.createCell(col);
        if (value != null) {
            c.setCellValue(java.sql.Date.valueOf(value));
        }
        c.setCellStyle(style);
    }

    private static CellStyle createStatusStyle(Workbook wb, Font font, Color bg) {
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(style);

        ((XSSFCellStyle) style).setFillForegroundColor(
                new XSSFColor(bg, null)
        );

        return style;
    }

    private static void setBorders(CellStyle s) {
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
    }

    private static String formatStatus(Booking.Status s) {
        return switch (s) {
            case POTVRDJENO -> "Potvrđeno";
            case ODBIJEN -> "Odbijeno";
            default -> "Na čekanju";
        };
    }
}
