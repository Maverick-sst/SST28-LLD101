public class PdfChannel implements Exporter {
    private final Exporter  pdfExporter;


    public PdfChannel(Exporter pdf){
        this.pdfExporter=pdf;
    }
    @Override
    public ExportResult export(ExportRequest req) {
        if (req.body != null && req.body.length() > 20) {
            throw new IllegalArgumentException("PDF cannot handle content > 20 chars");
        }
        return pdfExporter.export(req);
    }
}
