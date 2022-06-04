package org.bizzdeskgroup.services;

import org.apache.ibatis.annotations.Select;
import org.bizzdeskgroup.Dtos.Query.UserDto;

public interface UserTestService {

    @Select("SELECT * FROM users WHERE Role IN (#{roleType}) ORDER BY #{orderBy} ASC LIMIT #{from}, #{recordsPerPage}")
    UserDto selectBUsers(int roleType, String orderBy, String from, int recordsPerPage);
//    void CreateUser(CreateUserDto userDto) throws  Exception;
//    AuthenticatedUserDto UserAuthentication(AuthenticateUserDto user) throws Exception;
//    PaginatedDto AllUsers(String roleType, String sortBy, int pageNo, int pageSize) throws Exception;
//    UserDto SingleUser(int Id) throws Exception;
//
//    UserDto Update(int requestedByUserId, int userId, UserUpdateDto dto);
//    void RequestActivationCode(String type);
//    void Deactivate(int requestedByUserId, int userId);
//    void Activate(int requestedByUserId, int userId);
//    boolean ConfirmEmail(EmailConfirmDto emailConfirmDto) throws Exception;
//    boolean ConfirmPhone(PhoneConfirmDto phoneConfirmDto, String emailPhone) throws Exception;
//    String PhoneConfirmRequest(PhoneConfirmRequestDto dto) throws Exception;
//    String PasswordResetRequest(PasswordResetRequestDto dto) throws Exception;
//    boolean PasswordReset(PasswordResetDto dto) throws Exception;
//    void ChangePassword(ChangePasswordDto dto, String emailPhone) throws Exception;
}
