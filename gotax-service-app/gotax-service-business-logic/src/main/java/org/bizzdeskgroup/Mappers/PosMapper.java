package org.bizzdeskgroup.Mappers;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bizzdeskgroup.Dtos.Command.AddPosDto;
import org.bizzdeskgroup.Dtos.Command.AddPosMetaDto;
import org.bizzdeskgroup.Dtos.Command.CreateMdaDto;
import org.bizzdeskgroup.Dtos.Command.CreatePosTransactionDto;
import org.bizzdeskgroup.Dtos.Query.PosTransactionDto;
import org.bizzdeskgroup.Dtos.Query.PosTransactionResponseDto;
import org.bizzdeskgroup.models.Mda;
import org.bizzdeskgroup.models.Pos;
import org.bizzdeskgroup.models.PosMeta;
import org.bizzdeskgroup.models.PosTransaction;

import static java.lang.Math.abs;
import static org.bizzdeskgroup.Helpers.NotificationMixer.DateTime;

public class PosMapper {
    public static Pos NewPos(AddPosDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Pos destination = new Pos();

        factory.classMap(Pos.class, AddPosDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
                destination.setCreatedDate(DateTime());
                destination.setPosDeviceSerial(source.terminalId);

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Pos.class);

        return destination;
    }
    public static PosMeta NewPosMeta(AddPosMetaDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        PosMeta destination = new PosMeta();

        factory.classMap(PosMeta.class, AddPosMetaDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
        destination.setCreatedDate(DateTime());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, PosMeta.class);

        return destination;
    }
    public static PosTransaction NewPosTransactionItem(CreatePosTransactionDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        PosTransaction destination = new PosTransaction();

        factory.classMap(PosTransaction.class, CreatePosTransactionDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(DateTime());
        destination.setAmountPaid(abs(source.amountPaid));

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, PosTransaction.class);

        return destination;
    }
//    public BoundMapperFacade<PosTransactionDto, PosTransactionResponseDto> mapListFields() {
//        final DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//        mapperFactory.classMap(Family.class, FamilyDto.class)
//                .byDefault()
//                .register();
//
//        mapperFactory.classMap(Person.class, PersonDto.class)
//                .field("firstName", "name")
//                .field("lastName", "surname")
//                .byDefault()
//                .register();
//        return mapperFactory.getMapperFacade(Family.class, FamilyDto.class);
//    }
}
