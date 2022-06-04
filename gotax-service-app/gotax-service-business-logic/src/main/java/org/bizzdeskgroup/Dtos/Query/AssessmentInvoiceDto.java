package org.bizzdeskgroup.Dtos.Query;

import java.util.List;

public class AssessmentInvoiceDto {
    private AssessmentDto assessment;
    private List<InvoiceDto> invoices;

    public AssessmentDto getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDto assessment) {
        this.assessment = assessment;
    }

    public List<InvoiceDto> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDto> invoices) {
        this.invoices = invoices;
    }
}
