import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceCalculator {
    public static InvoiceSummary calculate(String invId,String customerType, List<OrderLine> lines, Map<String,MenuItem> menu){
        double subtotal= 0.0;
        List<InvoiceInLine> invoiceInLines = new ArrayList<>();

        for(OrderLine l : lines){
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            subtotal += lineTotal;
            invoiceInLines.add(new InvoiceInLine(item.name, l.qty, lineTotal));

        }

        double taxPct = TaxRules.taxPercent(customerType);
        double tax = subtotal * (taxPct/100.0);
        double discount = DiscountRules.discountAmount(customerType, subtotal, lines.size());
        
        double total = subtotal + tax - discount;

        return new InvoiceSummary(invId, invoiceInLines, subtotal ,taxPct, tax, discount, total);
    }
}
