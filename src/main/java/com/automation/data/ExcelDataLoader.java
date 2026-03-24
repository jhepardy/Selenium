package com.automation.data;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class ExcelDataLoader {

    private ExcelDataLoader() {
    }

    /**
     * First sheet, row 0 = header (username, password), data from row 1.
     */
    public static List<LoginCredentials> loadLoginUsers(String classpathResource) throws IOException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IOException("Resource not found: " + classpathResource);
            }
            try (Workbook wb = WorkbookFactory.create(in)) {
                Sheet sheet = wb.getSheetAt(0);
                DataFormatter fmt = new DataFormatter();
                List<LoginCredentials> out = new ArrayList<>();
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    String u = fmt.formatCellValue(row.getCell(0));
                    String p = fmt.formatCellValue(row.getCell(1));
                    if (u == null || u.isBlank()) {
                        continue;
                    }
                    LoginCredentials c = new LoginCredentials();
                    c.setUsername(u.trim());
                    c.setPassword(p != null ? p.trim() : "");
                    out.add(c);
                }
                return out;
            }
        }
    }
}
