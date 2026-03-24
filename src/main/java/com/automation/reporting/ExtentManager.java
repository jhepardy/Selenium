package com.automation.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Path;

public final class ExtentManager {

    private static ExtentReports reports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentManager() {
    }

    public static synchronized ExtentReports getReports() {
        if (reports == null) {
            Path out = Path.of("target", "extent-report", "index.html");
            out.getParent().toFile().mkdirs();
            ExtentSparkReporter spark = new ExtentSparkReporter(out.toString());
            reports = new ExtentReports();
            reports.attachReporter(spark);
            reports.setSystemInfo("env", System.getProperty("env", "default"));
        }
        return reports;
    }

    public static void startTest(String name) {
        TEST.set(getReports().createTest(name));
    }

    public static ExtentTest current() {
        return TEST.get();
    }

    public static void endTest() {
        TEST.remove();
    }

    public static void flush() {
        if (reports != null) {
            reports.flush();
        }
    }
}
