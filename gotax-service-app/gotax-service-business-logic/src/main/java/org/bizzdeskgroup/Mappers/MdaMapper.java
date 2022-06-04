package org.bizzdeskgroup.Mappers;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.MdaDto;
import org.bizzdeskgroup.Dtos.Query.MdaOfficeDto;
import org.bizzdeskgroup.Dtos.Query.MdaServiceDto;
import org.bizzdeskgroup.models.*;

public class MdaMapper {
    public static Business NewBusiness(CreateBusinessDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Business destination = new Business();

        factory.classMap(Business.class, CreateBusinessDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Business.class);

        return destination;
    }

    public static Mda NewMda(CreateMdaDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        Mda destination = new Mda();

        factory.classMap(Mda.class, CreateMdaDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, Mda.class);

        return destination;
    }
    public static MdaOffice NewMdaOffice(CreateMdaOfficeDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaOffice destination = new MdaOffice();

        factory.classMap(MdaOffice.class, CreateMdaOfficeDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaOffice.class);

        return destination;
    }
    public static MdaService NewMdaService(CreateMdaServiceDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaService destination = new MdaService();

        factory.classMap(MdaService.class, CreateMdaServiceDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaService.class);

        return destination;
    }
    public static MdaUser NewMdaUser(CreateMdaUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaUser destination = new MdaUser();

        factory.classMap(MdaUser.class, CreateMdaUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaUser.class);

        return destination;
    }
    public static MdaDto GetMda(Mda source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaDto destination = new MdaDto();

        factory.classMap(Mda.class, MdaDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaDto.class);

        return destination;
    }
//    public static List<MdaDto> GetMdaList(List<Mda> source) {
//        MapperFactory factory = new DefaultMapperFactory.Builder().build();
//
//        List<MdaDto> destination = new List<MdaDto>();
//
//        factory.classMap(Mda.class, MdaDto.class)
//                .mapNulls(false)
//                .mapNullsInReverse(false)
//                .byDefault()
//                .register();
////        destination.setCreatedDate(new Date());
//
//        MapperFacade mapper = factory.getMapperFacade();
//        destination = mapper.map(source, MdaDto.class);
//
//        return destination;
//    }
    public static MdaOfficeDto GetMdaOffice(MdaOffice source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaOfficeDto destination = new MdaOfficeDto();

        factory.classMap(MdaOffice.class, MdaOfficeDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaOfficeDto.class);

        return destination;
    }
    public static MdaServiceDto GetMdaService(MdaService source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        MdaServiceDto destination = new MdaServiceDto();

        factory.classMap(MdaService.class, MdaServiceDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, MdaServiceDto.class);

        return destination;
    }
}
