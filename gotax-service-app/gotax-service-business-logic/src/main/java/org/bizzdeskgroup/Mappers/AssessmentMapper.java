package org.bizzdeskgroup.Mappers;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.models.*;

import java.sql.Timestamp;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;

public class AssessmentMapper {
    public static Assessment NewAssessment(CustomerAssessmentDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Assessment destination = new Assessment();

        factory.classMap(Assessment.class, CustomerAssessmentDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
                destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Assessment.class);

        return destination;
    }
    public static Assessment NewAssessmentAlt(CustomerAssessmentAltDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Assessment destination = new Assessment();

        factory.classMap(Assessment.class, CustomerAssessmentAltDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Assessment.class);

        return destination;
    }

    public static AssessmentDto Assessment(Assessment source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        AssessmentDto destination = new AssessmentDto();

        factory.classMap(AssessmentDto.class, Assessment.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, AssessmentDto.class);

        return destination;
    }

    public static Invoice NewInvoice(GenerateInvoiceDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Invoice destination = new Invoice();

        factory.classMap(Invoice.class, GenerateInvoiceDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());
        destination.setInvoiceAmount(source.getInvoiceAmount());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Invoice.class);

        return destination;
    }

    public static InvoiceDto ReturnInvoice(Invoice source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        InvoiceDto destination = new InvoiceDto();

        factory.classMap(InvoiceDto.class, Invoice.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, InvoiceDto.class);

        return destination;
    }

    public static InvoiceDto ReturnFromInvoiceTin(InvoiceTinDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        InvoiceDto destination = new InvoiceDto();

        factory.classMap(InvoiceDto.class, InvoiceTinDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, InvoiceDto.class);

        try {
            if (source.getPayerType().equals(UserRoles.NonIndividual)) {
                destination.setPayerTin( source.getNjtbTin() != null && !source.getNjtbTin().trim().isEmpty() ? source.getNjtbTin().trim() : source.getNtemporaryTin().trim());
                destination.setTinType( source.getNjtbTin() != null && !source.getNjtbTin().trim().isEmpty() ? "JTB" : "Temp.");
            }
            if (source.getPayerType().equals(UserRoles.Individual)) {
                destination.setPayerTin( source.getIjtbTin() != null && !source.getIjtbTin().trim().isEmpty() ? source.getIjtbTin().trim() : source.getItemporaryTin().trim() );
                destination.setTinType( source.getIjtbTin() != null && !source.getIjtbTin().trim().isEmpty() ? "JTB" : "Temp." );
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return destination;
    }

    public static Individual NewIndividual(BaseIndividualEnumerationDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Individual destination = new Individual();

        factory.classMap(Individual.class, BaseIndividualEnumerationDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        destination.setDateOfBirth(new Timestamp(source.dateOfBirth.getTime()));

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Individual.class);

        return destination;
    }

    public static NonIndividual NewNonIndividual(BaseNonIndividualEnumerationDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        NonIndividual destination = new NonIndividual();

        factory.classMap(Individual.class, BaseNonIndividualEnumerationDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());
        destination.setCompanyRegistrationDate(new Timestamp(source.companyRegistrationDate.getTime()));
        destination.setCompanyCommencementDate(new Timestamp(source.companyCommencementDate.getTime()));

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, NonIndividual.class);

        return destination;
    }
}
