package com.automation.support;

import org.openqa.selenium.By;

/**
 * Encapsulate dynamic CSS / XPath patterns in one place.
 */
public final class LocatorStrategies {

    private LocatorStrategies() {
    }

    public static By dataTestId(String id) {
        return By.cssSelector("[data-testid='" + cssEscape(id) + "']");
    }

    public static By containsDataAttr(String attr, String value) {
        return By.cssSelector("[" + attr + "*='" + cssEscape(value) + "']");
    }

    public static By rowById(String rowId) {
        return By.cssSelector("[data-row-id='" + cssEscape(rowId) + "']");
    }

    /** XPath: normalize-space match; avoid quotes in {@code text} or extend escaping. */
    public static By xpathExactText(String tag, String text) {
        if (text != null && (text.contains("'") || text.contains("\""))) {
            throw new IllegalArgumentException("Use data-testid or split text; raw quotes not supported here");
        }
        return By.xpath("//" + tag + "[normalize-space()='" + text + "']");
    }

    private static String cssEscape(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace("\\", "\\\\").replace("'", "\\'");
    }
}
