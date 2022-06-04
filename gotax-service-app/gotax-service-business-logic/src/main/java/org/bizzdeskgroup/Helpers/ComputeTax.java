package org.bizzdeskgroup.Helpers;

import org.bizzdeskgroup.Dtos.Query.ComplexTaxDto;
import org.bizzdeskgroup.models.UserRoles;

public class ComputeTax {
    double grossIncome;
//    int numberOfChildrenBelow18;
//    int numberOfDependants;
//    String role;

    public ComputeTax(double grossIncomeAmount) {
        this.grossIncome = grossIncomeAmount;
//        this.numberOfChildrenBelow18 = numberOfChildrenBelow18;
//        this.numberOfDependants = numberOfDependants;
//        this.role = role;
    }

    public ComplexTaxDto SimpleTax() throws Exception {
        if (grossIncome <= 300000) {
//            return (1.0/100.0)*grossIncome;

            ComplexTaxDto complexTaxDto = new ComplexTaxDto();
            complexTaxDto.computedTax = (1.0/100.0) * grossIncome;
            complexTaxDto.grossIncome = grossIncome;
            complexTaxDto.nonTaxableIncome = 0.00;
            complexTaxDto.taxableIncome = grossIncome;
            return complexTaxDto;
        } else {
            throw new Exception("Gross below range");
        }
    }

    public ComplexTaxDto ComplexTax() throws Exception {
        //children and dependant consideration not applied
        if(grossIncome < 300000) throw new Exception("Gross not in range");
//        if (numberOfChildrenBelow18 < 0 || numberOfChildrenBelow18 > 4) throw new Exception("Number of children cannot be more than 4");
//        if (numberOfDependants < 0 || numberOfDependants > 2) throw new Exception("Number of dependants cannot be more than 2");
//        if(grossIncome > 5000000){
//
//        }

        ComplexTaxDto complexTaxDto = new ComplexTaxDto();
        double nonTaxableIncome = 0.0;
//        if (role.equals(UserRoles.Individual)) nonTaxableIncome = (((20.0/100.0) * grossIncome) + 200000) + (numberOfChildrenBelow18 * 2500) + (numberOfDependants * 2000);
//        if (role.equals(UserRoles.NonIndividual)) nonTaxableIncome = (((20.0/100.0) * grossIncome) + 200000);
        nonTaxableIncome = (((20.0/100.0) * grossIncome) + 200000);
        double taxableIncome = grossIncome - nonTaxableIncome;

        double computedTax = 0.0;
        complexTaxDto.grossIncome = grossIncome;
        complexTaxDto.nonTaxableIncome = nonTaxableIncome;
        complexTaxDto.taxableIncome = taxableIncome;

        //do iterations
        if(taxableIncome - 300000 >= 0){
            computedTax += (7.0/100.0) * 300000;
            taxableIncome = taxableIncome - 300000;
        } else {
            computedTax += (7.0/100.0) * taxableIncome;
        }

        if(taxableIncome - 300000 >= 0 ){
            computedTax += (11.0/100.0) * 300000;
            taxableIncome = taxableIncome - 300000;
        } else {
            computedTax += (11.0/100.0) * taxableIncome;
        }

//        if(taxableIncome - 300000 >= 0 ){
//            computedTax += (15.0/100.0) * 300000;
//            taxableIncome = taxableIncome - 300000;
//        } else {
//            computedTax += (15.0/100.0) * taxableIncome;
//        }

        if(taxableIncome - 500000 >= 0 ){
            computedTax += (15.0/100.0) * 500000;
            taxableIncome = taxableIncome - 500000;
        } else {
            computedTax += (15.0/100.0) * taxableIncome;
        }

        if(taxableIncome - 500000 >= 0 ){
            computedTax += (19.0/100.0) * 500000;
            taxableIncome = taxableIncome - 500000;
        } else {
            computedTax += (19.0/100.0) * taxableIncome;
        }

        if(taxableIncome - 1600000 >= 0 ){
            computedTax += (21.0/100.0) * 1600000;
            taxableIncome = taxableIncome - 1600000;
        } else {
            computedTax += (21.0/100.0) * taxableIncome;
        }

        if(taxableIncome > 0 ){
            computedTax += (24.0/100.0) * taxableIncome;
        }
        complexTaxDto.computedTax = computedTax;

        return complexTaxDto;
    }

}
