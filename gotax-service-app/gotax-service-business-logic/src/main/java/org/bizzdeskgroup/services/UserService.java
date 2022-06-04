package org.bizzdeskgroup.services;

import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.InternalUserDto;
import org.bizzdeskgroup.Dtos.Query.PaginatedDto;
import org.bizzdeskgroup.Dtos.Query.UserDto;

import java.util.List;

public interface UserService {
    void CreateUser(CreateUserDto userDto, int createdBy, int businessId) throws  Exception;
    InternalUserDto UserAuthentication(AuthenticateUserDto user) throws Exception;

    PaginatedDto AllUsers(List<String> roleType, String sortBy, int pageNo, int pageSize, String role, int filterBy, boolean isAdmin) throws Exception;
    UserDto SingleUser(int Id) throws Exception;
    UserDto SingleUserByPhoneOrEmail(String phoneOrEmail) throws Exception;

    void UpdateRole(int requestedByUserId, int userId, String role) throws Exception;

    UserDto Update(int requestedByUserId, int userId, UserUpdateDto dto) throws Exception;
    void RequestActivationCode(String type);
    void Deactivate(int requestedByUserId, int userId) throws Exception;
    void Activate(int requestedByUserId, int userId) throws Exception;
    boolean ConfirmEmail(EmailConfirmDto emailConfirmDto) throws Exception;
    boolean ConfirmPhone(PhoneConfirmDto phoneConfirmDto, String emailPhone) throws Exception;
    String PhoneConfirmRequest(PhoneConfirmRequestDto dto) throws Exception;
    String PasswordResetRequest(PasswordResetRequestDto dto) throws Exception;
    boolean PasswordReset(PasswordResetDto dto) throws Exception;
    void ChangePassword(ChangePasswordDto dto, String emailPhone) throws Exception;

    List<UserDto> SearchUsers(String roleList, String sortBy, String searchBy, String role, Integer formProjectId, boolean isAdmin) throws Exception;

//    List<UserDto> SearchUsers(List<String> roleType, String sortBy, String searchBy, String role, Integer formProjectId, boolean isAdmin) throws Exception;

    int CountUsers(List<String> roleType, int businessId, int mdaId, boolean status) throws Exception;
//    int CountMdaUsers(List<String> roleType, int businessId, int mdaId) throws Exception;
}
