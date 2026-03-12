package com.example.reports;

/**
 * TODO (student):
 * Implement Proxy responsibilities here:
 * - access check
 * - lazy loading
 * - caching of RealReport within the same proxy
 */
public class ReportProxy implements Report {

    private final String reportId;
    private final String title;
    private final String classification;
    private final AccessControl accessControl = new AccessControl();
    private  RealReport realReport;

    
    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {
        // checks access!
        if(!accessControl.canAccess(user, classification)){
            System.out.println("Access Denied!");
            return;
        }
        // does lazy loading - creating instance only when needed 
        if(realReport == null){
            realReport = new RealReport(reportId, title, classification);
        }
        // offloading tasks to realReport
        realReport.display(user);
    }
    

}
