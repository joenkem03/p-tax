package org.bizzdeskgroup.services.impl;

import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.Assessment;
import org.bizzdeskgroup.models.Invoice;

import java.util.List;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;
import static org.eclipse.jetty.util.IO.close;

public class UpdateInvoiceAssessment implements Runnable {
    private int assessmentId;
    public UpdateInvoiceAssessment(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Override
    public void run() {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            Assessment assessment = session.selectOne("Assessment.singleAssess", assessmentId);
            if(assessment != null) {
                double invoiceSum = session.selectOne("Invoice.invoicesByAssessUpdate", assessmentId);
                if(invoiceSum > 0) {

                    if(assessment.recommendedAmount > 0){
                        if(invoiceSum == assessment.recommendedAmount) {
                            assessment.setSettled(true);
                            assessment.setUpdatedBy(1);
                            assessment.setUpdatedDate(DateTime());
                            assessment.setAmountPaid(invoiceSum);
                            session.update("Assessment.update", assessment);
                        }
                        System.out.println("is rec");
                    } else {
                        System.out.println(invoiceSum);
                        if(invoiceSum == assessment.taxAmount) {
                            assessment.setSettled(true);
                            assessment.setUpdatedBy(1);
                            assessment.setUpdatedDate(DateTime());
                            assessment.setAmountPaid(invoiceSum);
                            session.update("Assessment.update", assessment);
                        }
                        System.out.println("is tax");
                    }
                }
                System.out.println(invoiceSum);
            }


        } catch (Exception e){
//            throw new Exception(e.getMessage());
            System.out.println("updating inv");
            e.printStackTrace();
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }
}
