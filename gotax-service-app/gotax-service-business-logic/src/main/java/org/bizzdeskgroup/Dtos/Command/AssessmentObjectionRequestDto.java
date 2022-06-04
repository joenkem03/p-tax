package org.bizzdeskgroup.Dtos.Command;

import javax.validation.constraints.NotNull;

public class AssessmentObjectionRequestDto {
    private int assessmentId;

    @NotNull
    private String objectionReason;

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getObjectionReason() {
        return objectionReason;
    }

    public void setObjectionReason(String objectionReason) {
        this.objectionReason = objectionReason;
    }
}
