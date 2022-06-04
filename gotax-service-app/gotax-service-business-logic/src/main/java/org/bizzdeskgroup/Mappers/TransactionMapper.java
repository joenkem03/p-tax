package org.bizzdeskgroup.Mappers;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bizzdeskgroup.Dtos.Command.CreatePosCardTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreateTransactionDto;
import org.bizzdeskgroup.Dtos.Command.CreateWebTransactionDto;
import org.bizzdeskgroup.Dtos.Query.IntRemittanceResponseDto;
import org.bizzdeskgroup.Dtos.Query.RemittanceResponseDto;
import org.bizzdeskgroup.models.Transaction;

import java.sql.Timestamp;

import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;

public class TransactionMapper {
    public static Transaction NewCardTransactionItem(CreatePosCardTransactionDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Transaction destination = new Transaction();

        factory.classMap(Transaction.class, CreatePosCardTransactionDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Transaction.class);

        return destination;
    }
    public static Transaction NewWebTransactionItem(CreateWebTransactionDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Transaction destination = new Transaction();

        factory.classMap(Transaction.class, CreateWebTransactionDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Transaction.class);

        return destination;
    }
    public static Transaction NewTransactionItem(CreateTransactionDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Transaction destination = new Transaction();

        factory.classMap(Transaction.class, CreateTransactionDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Transaction.class);

        return destination;
    }
    public static RemittanceResponseDto SingleRemittanceMap(IntRemittanceResponseDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        RemittanceResponseDto destination = new RemittanceResponseDto();

        factory.classMap(RemittanceResponseDto.class, IntRemittanceResponseDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());
//        destination.setCreatedDateStamp(DateTime().getTime()/1000);

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, RemittanceResponseDto.class);

        return destination;
    }
}
