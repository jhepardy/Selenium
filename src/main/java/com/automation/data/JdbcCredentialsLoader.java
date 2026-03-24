package com.automation.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Example JDBC loader — use a dedicated test database; never point at prod without safeguards.
 * Configure URL/user/pass via system properties: {@code jdbc.url}, {@code jdbc.user}, {@code jdbc.password}.
 */
public final class JdbcCredentialsLoader {

    private JdbcCredentialsLoader() {
    }

    public static List<LoginCredentials> loadFromQuery(String sql) throws SQLException {
        String url = System.getProperty("jdbc.url");
        String user = System.getProperty("jdbc.user", "");
        String pass = System.getProperty("jdbc.password", "");
        if (url == null || url.isBlank()) {
            throw new SQLException("System property jdbc.url is required");
        }
        List<LoginCredentials> out = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoginCredentials c = new LoginCredentials();
                c.setUsername(rs.getString("username"));
                c.setPassword(rs.getString("password"));
                out.add(c);
            }
        }
        return out;
    }
}
