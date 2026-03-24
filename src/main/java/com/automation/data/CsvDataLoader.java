package com.automation.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CsvDataLoader {

    private CsvDataLoader() {
    }

    /**
     * Expects header: username,password,... — returns rows after header.
     */
    public static List<LoginCredentials> loadLoginUsers(String classpathResource) throws IOException, CsvException {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IOException("Resource not found: " + classpathResource);
            }
            try (CSVReader reader = new CSVReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                List<String[]> all = reader.readAll();
                if (all.isEmpty()) {
                    return List.of();
                }
                List<LoginCredentials> out = new ArrayList<>();
                for (int i = 1; i < all.size(); i++) {
                    String[] row = all.get(i);
                    if (row.length < 2) {
                        continue;
                    }
                    LoginCredentials c = new LoginCredentials();
                    c.setUsername(row[0].trim());
                    c.setPassword(row[1].trim());
                    if (row.length > 2) {
                        c.setDescription(row[2].trim());
                    }
                    out.add(c);
                }
                return out;
            }
        }
    }
}
