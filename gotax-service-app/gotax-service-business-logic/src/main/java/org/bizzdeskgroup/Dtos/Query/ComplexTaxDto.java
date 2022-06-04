package org.bizzdeskgroup.Dtos.Query;

import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "ComplexTaxDto")
public class ComplexTaxDto {
    public double grossIncome;
    public double taxableIncome;
    public double nonTaxableIncome;
    public double computedTax;
    public int assessmentId;
}
