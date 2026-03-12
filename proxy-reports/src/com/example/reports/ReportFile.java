package com.example.reports;

/**
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - Concrete class used directly by clients
 * - Expensive load happens every time display() is called
 * - No access control
 *
 * TODO (student):
 * - Convert this into the RealSubject behind a Proxy, or replace with RealReport.
 */
public class ReportFile {

    private final String reportId;
    private final String title;
    private final String classification; // PUBLIC / FACULTY / ADMIN

    public ReportFile(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    
}
