package org.bizzdeskgroup.Mappers;

import org.bizzdeskgroup.Dtos.Command.AdminCreateUserDto;
import org.bizzdeskgroup.Dtos.Command.AgentCreateUserDto;
import org.bizzdeskgroup.Dtos.Command.CreateSelfUserDto;
import org.bizzdeskgroup.Dtos.Command.CreateUserDto;
import org.bizzdeskgroup.Dtos.Query.AuthenticatedUserDto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.bizzdeskgroup.Dtos.Query.InternalUserDto;
import org.bizzdeskgroup.models.User;

public class UserMapper {
    public static AuthenticatedUserDto LoginUser(InternalUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        AuthenticatedUserDto destination = new AuthenticatedUserDto();

        factory.classMap(InternalUserDto.class, AuthenticatedUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, AuthenticatedUserDto.class);

        return destination;
    }
    public static InternalUserDto LoginInternalUser(User source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        InternalUserDto destination = new InternalUserDto();

        factory.classMap(User.class, InternalUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, InternalUserDto.class);

        return destination;
    }
    public static User NewUser(CreateUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        User destination = new User();

        factory.classMap(User.class, AuthenticatedUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, User.class);

        return destination;
    }
    public static CreateUserDto NewSelfUser(CreateSelfUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        CreateUserDto destination = new CreateUserDto();

        factory.classMap(CreateSelfUserDto.class, CreateUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, CreateUserDto.class);

        return destination;
    }
    public static CreateUserDto NewUserByAgent(AgentCreateUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        CreateUserDto destination = new CreateUserDto();

        factory.classMap(AgentCreateUserDto.class, CreateUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, CreateUserDto.class);

        return destination;
    }
    public static CreateUserDto NewUserByAdmin(AdminCreateUserDto source) {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        CreateUserDto destination = new CreateUserDto();

        factory.classMap(AdminCreateUserDto.class, CreateUserDto.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();
//        destination.setCreatedDate(new Date());

        MapperFacade mapper = factory.getMapperFacade();
        destination = mapper.map(source, CreateUserDto.class);

        return destination;
    }
}
