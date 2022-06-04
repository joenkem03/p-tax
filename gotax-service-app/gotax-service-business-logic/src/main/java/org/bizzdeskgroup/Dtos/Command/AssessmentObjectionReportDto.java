package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class AssessmentObjectionReportDto {
    private int assessmentId;

    @NotEmpty
    private String assessmentOfficerRecommendation;
//    public Timestamp recommendationDate;

    @Min(1)
    private double recommendedAmount;

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentOfficerRecommendation() {
        return assessmentOfficerRecommendation;
    }

    public void setAssessmentOfficerRecommendation(String assessmentOfficerRecommendation) {
        this.assessmentOfficerRecommendation = assessmentOfficerRecommendation;
    }

    public double getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(double recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }
}
