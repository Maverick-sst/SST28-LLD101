public class InvoiceFormatter {

    public static String format(InvoiceSummary summary) {

        StringBuilder out = new StringBuilder();
        out.append("Invoice# ").append(summary.invId).append("\n");

        for (InvoiceInLine line : summary.lines) {
            out.append(String.format("- %s x%d = %.2f\n", line.itemName, line.qty, line.lineTotal));
        }

        out.append(String.format("Subtotal: %.2f\n", summary.subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", summary.taxPct, summary.tax));
        out.append(String.format("Discount: -%.2f\n", summary.discount));
        out.append(String.format("TOTAL: %.2f\n", summary.total));

        return out.toString();
    }
}
