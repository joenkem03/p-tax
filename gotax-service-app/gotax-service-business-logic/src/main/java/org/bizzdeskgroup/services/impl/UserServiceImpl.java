package org.bizzdeskgroup.services.impl;

//import com.kumuluz.ee.logs.cdi.Log;
import org.bizzdeskgroup.Dtos.Command.*;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.InfoEmailSmsUtil;
import org.bizzdeskgroup.Helpers.RandomString;
import org.bizzdeskgroup.factory.MyBatisConnectionFactory;
import org.bizzdeskgroup.models.ActivityLog;
import org.bizzdeskgroup.models.User;
import org.apache.ibatis.session.SqlSession;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.UserService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static org.bizzdeskgroup.Helpers.NotificationMixer.*;
import static org.bizzdeskgroup.Mappers.UserMapper.*;
import static org.eclipse.jetty.util.IO.close;

public class UserServiceImpl implements UserService {
    @Override
    public void CreateUser(CreateUserDto userDto, int createdBy, int businessId) throws Exception {

        if(createdBy > 0) {
//        if (userDto.role.equals(UserRoles.Agent) || userDto.role.equals(UserRoles.SubAdmin2) || userDto.role.equals(UserRoles.SubAdmin1) || userDto.role.equals(UserRoles.ProjectReport)) {
            userDto.setPassword(new RandomString(8).nextString());
        } else {
            if (userDto.getPassword().trim().isEmpty())
                throw new Exception("Password is required");

            if (!userDto.getPassword().equals(userDto.getPasswordConfirmation()))
                throw new Exception("Password and Confirm password do not match");
        }

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            User existingUserByPhone = session.selectOne("User.getUserByPhone", userDto);

//            if (existingUserByPhone != null && existingUser.phone.equals(userDto.phone))
            if (existingUserByPhone != null)
                throw new Exception("Phone  is already taken.");

            User existingUserByEmail = session.selectOne("User.getUserByEmail", userDto);
//            if (existingUser != null && existingUser.email.equals(userDto.email))
            if (existingUserByEmail != null)
                throw new Exception("Email is already taken.");

            User newUser = NewUser(userDto);
    //        newUser.EmailConfirmationCode = RandomGenerator.GenerateEmailCode(7);
//            newUser.EmailConfirmationCode = String.valueOf(new RandomString(8));
            newUser.emailConfirmationCode = new RandomString(8).nextString();
            //newUser.PhoneConfirmationCode = RandomGenerator.GeneratePhoneCode(6);
            System.out.println("EmailCode " + newUser.emailConfirmationCode);

            newUser.isEmailConfirmed = false;
            newUser.isActive = false;

            if (userDto.getRole().equals(UserRoles.Agent) || userDto.getRole().equals(UserRoles.SubAdmin2) || userDto.getRole().equals(UserRoles.SubAdmin1) || userDto.getRole().equals(UserRoles.ProjectReport))
            {
                newUser.isEmailConfirmed = true;
                newUser.emailConfirmedDate = DateTime();
                newUser.isActive = true;
            }

            newUser.passwordSalt = generateSalt();
            newUser.passwordHash = CreatePasswordHash(userDto.getPassword(), newUser.passwordSalt);
            newUser.createdDate = DateTime();
            newUser.createdBy = createdBy;
            newUser.businessId = businessId;

            int ins = session.insert("User.insert", newUser);
            //Notify
            String messageEncoded = DoSmipleWork(newUser.email, newUser.emailConfirmationCode);
            System.out.println("decodedBytes " + messageEncoded);
            String link;
            if(createdBy <= 0){
//            if(userDto.role.equals(UserRoles.NonIndividual) || userDto.role.equals(UserRoles.Individual)){
                link = "http://185.4.176.160/confirm/"+messageEncoded;
                InfoEmailSmsUtil.SendMail(userDto.getEmail(),
                    userDto.getFirstName() + " " + userDto.getLastName(),
                    "<html><body>Dear "+ userDto.getFirstName() +",<br><br>Kindly click <a href='"+link+"'>here</a> verify your account. Or copy the link below and paste in a browser<br><br>"+link+"  </body></html>", "Email Confirmation");

            } else {
                if (userDto.getRole().equals(UserRoles.Agent) || userDto.getRole().equals(UserRoles.SubAdmin2) || userDto.getRole().equals(UserRoles.SubAdmin1) || userDto.getRole().equals(UserRoles.ProjectReport)) {
                    link = "http://185.4.176.160/";
                    InfoEmailSmsUtil.SendMail(userDto.getEmail(),
                            userDto.getFirstName() + " " + userDto.getLastName(),
                            "<html><body>Dear " + userDto.getFirstName() + ",<br><br>New account creation<br>Username: " + userDto.getEmail() + "<br>Password: " + userDto.getPassword() + "<br>Kindly click <a href='" + link + "'>here</a> to login. Or copy the link below and paste in a browser<br><br>" + link + "  </body></html>", "New Account Creation");
                }
            }

//            ActivityLog activityLog = new ActivityLog();
//            activityLog.setAction("Create New User");
//            activityLog.setCreatedBy(createdBy);
//            activityLog.setBusinessId(businessId);
//            activityLog.setAffectedUser(newUser.getId());
//            activityLog.setActionDesc("Creation of user email: "+newUser.getEmail()+"; phone: "+newUser.getPhone()+"; role: "+newUser.getRole());
//            activityLog.setCreatedDate(DateTime());
//            activityLog.setTimeStamp(DateTime().getTime()/1000);
//            session.insert("ChangeAudit.NewActivity", activityLog);

            LogAction("Create New User", "Creation of user email: "+newUser.getEmail()+"; phone: "+newUser.getPhone()+"; role: "+newUser.getRole(), createdBy, newUser.getId(), businessId, "0.0.0.0");

        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public InternalUserDto UserAuthentication(AuthenticateUserDto user) throws Exception {

        if (user.username.trim().isEmpty()|| user.password.trim().isEmpty())
            throw new Exception("Username or password is incorrect.");
        UserDto ud = new UserDto();
        ud.email = user.username;
        ud.phone = user.username;

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            User userCheck = session.selectOne("User.getUserByPhoneOrEmail", ud);

            // check if user exists
            if (userCheck == null)
                throw new Exception("Username or password is incorrect.");

            // check if password is correct
            if (!VerifyPasswordHash(user.password, userCheck.passwordHash, userCheck.passwordSalt))
                throw new Exception("Username or password is incorrect.");

            ////Check if user is confirmed
            //if (!user.IsEmailConfirmed && !user.IsPhoneConfirmed)
            //    throw new AppException("Account is not confirmed.");

            //Check if user is confirmed
            if (!userCheck.isEmailConfirmed)
                throw new Exception("Account is not confirmed.");

            //Check if user is active
            if (!userCheck.isActive)
                throw new Exception("Account is not active.");

            // authentication successful
            userCheck.lastLoginDate = DateTime();
            session.update("User.update", userCheck);


//
//            ActivityLog activityLog = new ActivityLog();
//            activityLog.setAction("User Login");
//            activityLog.setCreatedBy(userCheck.getId());
//            activityLog.setBusinessId(userCheck.getBusinessId());
//            activityLog.setAffectedUser(userCheck.getId());
//            activityLog.setActionDesc("User authentication successful");
//            activityLog.setCreatedDate(DateTime());
//            activityLog.setTimeStamp(DateTime().getTime()/1000);
//            session.insert("ChangeAudit.NewActivity", activityLog);

            LogAction("User Login", "User authentication successful", userCheck.getId(), userCheck.getId(), userCheck.getBusinessId(), "0.0.0.0");

            return LoginInternalUser(userCheck);

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Login Failed");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
//        public List<UserDto> SearchUsers(List<String> roleType, String sortBy, String searchBy, String role, Integer formProjectId, boolean isAdmin) throws Exception {
    public List<UserDto> SearchUsers(String roleList, String sortBy, String searchBy, String role, Integer formProjectId, boolean isAdmin) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {

            UserSearchDto userFInd = new UserSearchDto();
            userFInd.setFirstName("%"+searchBy+"%");
            userFInd.setLastName("%"+searchBy+"%");
            userFInd.setEmail("%"+searchBy+"%");
            userFInd.setPhone("%"+searchBy+"%");
            userFInd.setOtherNames("%"+searchBy+"%");
            userFInd.setRole(roleList);

            userFInd.setBusinessId(formProjectId);

//            System.out.println("%"+searchBy+"%");
//            System.out.println(roleList);

            List<UserDto> users = session.selectList("User.searchAllUserMultiWhereRx", userFInd);
            if(!isAdmin && users.size() > 0){
                for (UserDto user:
                        users) {
                    user.id = 0;
                }
            }
            return users;


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.close();
            close(session);
        }
    }


    @Override
    public PaginatedDto AllUsers(List<String> roleType, String sortBy, int pageNo, int pageSize, String role, int filterBy, boolean isAdmin) throws Exception {

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            return session.selectList("User.All");
            int startFrom = 0;
            if (pageNo > 0 && pageSize > 0) {
                startFrom = (pageNo - 1) * pageSize;
            } else {
                //                pageSize = 10;
                pageSize = 0;
            }

            UserPageFinder userFInd = new UserPageFinder();
            userFInd.from = startFrom;
            userFInd.recordsPerPage = pageSize;
            userFInd.orderBy = sortBy;
            userFInd.roleTypes = roleType;

//            if (role.equals(UserRoles.SubAdmin1) || role.equals(UserRoles.SubAdmin2)) userFInd.filter = "State";
////            if (role.equals(UserRoles.SubAdmin2)) userFInd.filter = "MDA";
//            if (role.equals(UserRoles.Admin)) userFInd.filter = "All";
////            if (role.equals(UserRoles.SubAdmin1)) userFInd.filterId = filterBy;

            userFInd.businessId = filterBy;

//            System.out.println("SELECT * FROM users WHERE Role IN ("+userFInd.roleTypes+");");
//            LOGGER.trace("UserDto.selectAllUserWhere");
//            LOGGER.trace("kdd");
//            LOGGER.trace("fjjd", session.selectList("UserDto.selectAllUserWhere", userFInd));
//            LOGGER.trace(Marker.ANY_MARKER, session.selectList("UserDto.selectAllUserWhere", userFInd));
            List<UserDto> users = session.selectList("User.selectAllUserMultiWhere", userFInd);
            if(!isAdmin && users.size() > 0){
                for (UserDto user:
                     users) {
                    user.id = 0;
                    user.mdaId = 0;
                    user.officeId = 0;
                }
            }
//            LOGGER.trace("ccx", users);
            PaginatedDto paged = new PaginatedDto();
            paged.setStatus(200);
            if (startFrom == 0) {
                startFrom = 1;
            }
            int total = session.selectOne("User.getCountUserWhere", userFInd);
            int totalFound = users.size();

            paged.setTotal(total);
            paged.setData(users);
            paged.setFrom(startFrom);

            if(pageSize > 0) {
                if (total > pageSize) {
                    long remainder = total % pageSize;
                    int dividend = (total / pageSize);
                    if (remainder < pageSize) {
                        paged.setLast_page(dividend + 1);
                    } else {
                        paged.setLast_page(dividend);
                    }
                } else {
                    paged.setLast_page(1);
                }

                paged.setPer_page(pageSize);
                if (startFrom == 1) {
                    paged.setTo(totalFound);
                } else {
                    paged.setTo(startFrom + totalFound);
                    paged.setFrom(startFrom + 1);
                }
            }
            return paged;


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.close();
            close(session);
        }
    }

    @Override
    public UserDto SingleUser(int Id) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            return session.selectOne("User.getUserByIdDto", Id);
        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.close();
            close(session);
        }
    }

    @Override
    public UserDto SingleUserByPhoneOrEmail(String phoneOrEmail) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            UserDto ud = new UserDto();
            ud.email = phoneOrEmail;
            ud.phone = phoneOrEmail;

            return session.selectOne("User.getUserByPhoneOrEmailDto", ud);


        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.close();
            close(session);
        }
    }

    @Override
    public UserDto Update(int requestedByUserId, int userId, UserUpdateDto dto) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", userId);
            if (user == null)
                throw new Exception("User does not exist");
            if (!user.isActive)
                throw new Exception("invalid operation");

//            user.setEmail(dto.email.trim().isEmpty() ? user.getEmail() : dto.email);
//            user.setPhone(dto.phone.trim().isEmpty() ? user.getPhone() : dto.phone);
            user.setOtherNames(dto.otherNames.trim().isEmpty() ? user.otherNames : dto.otherNames);
            user.setFirstName(dto.firstName.trim().isEmpty() ? user.firstName : dto.firstName);
            user.setLastName(dto.lastName.trim().isEmpty() ? user.lastName : dto.lastName);
            user.setUpdatedDate(DateTime());
            user.setUpdatedBy(requestedByUserId);
            if (requestedByUserId == userId) user.setLastUpdatedDate(DateTime());

            String change = "Changed ";
            change += dto.otherNames.trim().isEmpty() ? "" : user.otherNames +" to "+ dto.otherNames+";";
            change += dto.firstName.trim().isEmpty() ? "" : user.firstName +" to "+ dto.firstName+";";
            change += dto.lastName.trim().isEmpty() ? "" : user.lastName +" to "+ dto.lastName+";";


            session.update("User.update", user);

            LogAction("Update User Details", change, requestedByUserId, userId, user.getBusinessId(), "0.0.0.0");

            return null;

        } catch (NullPointerException e){
//            return null;
            throw new Exception("User not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void UpdateRole(int requestedByUserId, int userId, String role) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", userId);
            if (user == null)
                throw new Exception("User does not exist");
            if (!user.isActive)
                throw new Exception("invalid operation");
            user.setRole(role);
            user.setUpdatedDate(DateTime());
            user.setUpdatedBy(requestedByUserId);


            session.update("User.update", user);

            LogAction("Changed User Role", "Update user role from "+user.getRole()+" to "+ role, requestedByUserId, userId, user.getBusinessId(), "0.0.0.0");


        } catch (NullPointerException e){
//            return null;
            throw new Exception("User not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void RequestActivationCode(String type) {

    }

    @Override
    public void Deactivate(int requestedByUserId, int userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", userId);
            if (user == null)
                throw new Exception("User does not exist");
            if (!user.isActive)
                throw new Exception("invalid operation");
            user.isActive = false;
            user.setUpdatedDate(DateTime());
            user.setUpdatedBy(requestedByUserId);
            session.update("User.update", user);

            LogAction("Update User Status", "Deactivate User", requestedByUserId, userId, user.getBusinessId(), "0.0.0.0");

        } catch (NullPointerException e){
//            return null;
            throw new Exception("User not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public void Activate(int requestedByUserId, int userId) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", userId);
            if (user == null)
                throw new Exception("User does not exist");
            if (user.isActive)
                throw new Exception("invalid operation");
            user.isActive = true;
            user.setLastUpdatedDate(DateTime());
            user.setUpdatedBy(requestedByUserId);
            session.update("User.update", user);

            LogAction("Update User Status", "Activated user access", requestedByUserId, userId, user.getBusinessId(), "0.0.0.0");

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("User not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    @Override
    public boolean ConfirmEmail(EmailConfirmDto emailConfirmDto) throws Exception {
        String[] decrypedMessage = ReverseSimpleWork(emailConfirmDto.code);
        if (decrypedMessage[0].isEmpty() || decrypedMessage[1].isEmpty())
            throw new Exception("Username or code is incorrect.");
        System.out.println("userId"+ Arrays.toString(decrypedMessage));
        System.out.println("userIdIs"+decrypedMessage[0]);
        System.out.println("userIdIsX"+decrypedMessage[1]);
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserByPhoneOrEmail", decrypedMessage[0]);
            if (user != null) {
                user.isEmailConfirmed = true;
                user.emailConfirmedDate = DateTime();
                user.isActive = true;
                user.lastUpdatedDate = DateTime();
                user.updatedBy = user.id;
                user.updatedDate = DateTime();

                session.update("User.update", user);

                LogAction("Confirm Email", "Successfully confirmed email address "+ user.getEmail(), user.getId(), user.getId(), user.getBusinessId(), "0.0.0.0");

                return true;
            } else {
                throw new Exception("Username or code is incorrect.");
//                return false;
            }
        } finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public boolean ConfirmPhone(PhoneConfirmDto phoneConfirmDto, String emailPone) throws Exception {
        if (phoneConfirmDto.code.isEmpty())
            throw new Exception("Username or code is incorrect.");

        User us = new User();
        us.email = phoneConfirmDto.code;
        us.phone = phoneConfirmDto.phone;

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
//            User user = session.selectOne("User.getUserByPhoneCode", us);
            User user = session.selectOne("User.getUserByPhoneOrEmail", us);
            if (user != null && user.phone.equals(phoneConfirmDto.phone)) {
                user.isPhoneConfirmed = true;
                user.phoneConfirmedDate = DateTime();
                user.isActive = true;
                user.lastUpdatedDate = DateTime();
                user.updatedBy = user.id;
                user.updatedDate = DateTime();

                session.update("User.update", user);

                LogAction("Confirm Phone", "Successfully confirmed phone number "+ user.getPhone(), user.getId(), user.getId(), user.getBusinessId(), "0.0.0.0");


                return true;
            } else {
                throw new Exception("Invalid code or phone.");
            }

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Phone not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public String PhoneConfirmRequest(PhoneConfirmRequestDto dto) throws Exception {

        if (dto.phone.trim().isEmpty())
            throw new Exception("User or phone is incorrect.");
        User us = new User();
        us.email = "";
        us.phone = dto.phone;
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserByPhoneOrEmail", us);
        if (user == null)
            throw new Exception("User or phone is incorrect.");
//        {
//            user.Phone = dto.Phone;
        user.phoneConfirmationCode = new RandomString(6).nextString();
//            user.lastUpdatedDate = DateTime();
            user.updatedBy = user.id;
            user.updatedDate = DateTime();

        session.update("User.update", user);
//            InfoEmailSmsUtil.SendSms("Your phone confirmation code: " + user.phoneConfirmationCode, user.phone);
            InfoEmailSmsUtil.SendSmsAlt("Your phone confirmation code: " + user.phoneConfirmationCode, user.phone);


            LogAction("Phone Confirmation Request", "Request to confirm phone number "+ dto.phone, user.getId(), user.getId(), user.getBusinessId(), "0.0.0.0");

            return user.resetPasswordCode;
//        }
//        else
//        {
//            return null;
//        }

        } catch (NullPointerException e){
            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public String PasswordResetRequest(PasswordResetRequestDto dto) throws Exception {

        if (dto.email.trim().isEmpty())
            throw new Exception("Email is incorrect.");
        User us = new User();
        us.email = dto.email;
        us.phone = "";
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
        User user = session.selectOne("User.getUserByPhoneOrEmail", us);
        if(user == null)
            throw new Exception("Email is incorrect.");
        user.resetPasswordCode = new RandomString(10).nextString();
        user.resetPasswordRequestDate = DateTime();

//        user.lastUpdatedDate = DateTime();
        user.updatedBy = user.id;
        user.updatedDate = DateTime();

        session.update("User.update", user);

        String link = "http://185.4.176.160/reset/"+DoSmipleWork(String.valueOf(user.id), user.resetPasswordCode);
        InfoEmailSmsUtil.SendMail(user.email,
                user.firstName,
                "<html><body>Dear "+ user.firstName +",<br><br>Kindly click <a href='"+link+"'>here</a> reset your account. Or copy the link below and paste in a browser<br>br>"+link+"  </body></html>", "Email Reset");


            LogAction("Password Reset Request", "Initiate password reset link on "+ user.getEmail()+"/"+user.getPhone(), user.getId(), user.getId(), user.getBusinessId(), "0.0.0.0");

            return user.resetPasswordCode;

    } finally {
        session.commit();
        session.close();
        close(session);
    }
    }

    @Override
    public boolean PasswordReset(PasswordResetDto dto) throws Exception {
        String[] decryptMessage = ReverseSimpleWork(dto.resetCode);

        SystemPasswordResetDto userReset = new SystemPasswordResetDto();
        userReset.setPassword(dto.password);
        userReset.setEmail(Integer.parseInt(decryptMessage[0]));
//        userReset.setCode(decryptMessage[1]);

        int id = Integer.parseInt(decryptMessage[0]);
//        System.out.println(userReset.email);
        if (userReset.email <= 0)
            throw new Exception("Email is incorrect.");

        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            User user = session.selectOne("User.getUserById", id);
            if (user != null)
            {
                user.passwordSalt = generateSalt();

                user.passwordHash = CreatePasswordHash(dto.password, user.passwordSalt);

                user.resetPasswordCount += 1;

                user.lastUpdatedDate = DateTime();
                user.updatedBy = user.id;
                user.updatedDate = DateTime();

                session.update("User.update", user);

                LogAction("Password Reset", "Successfully reset password on "+ user.getEmail()+"/"+user.getPhone(), user.getId(), user.getId(), user.getBusinessId(), "0.0.0.0");


                return true;
            }
            else
            {
                throw new Exception("Email is incorrect.");
//                return false;
            }

        } catch (NullPointerException e){
//            return 0;
            throw new Exception("Email not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public int CountUsers(List<String> roleType, int businessId, int mdaId, boolean status) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{

            UserPageFinder userFInd = new UserPageFinder();
            userFInd.roleTypes = roleType;
            userFInd.businessId = businessId;
            userFInd.mdaId = mdaId;
            userFInd.status = status;

            return session.selectOne("User.getCountUserWhere", userFInd);
        } catch (NullPointerException e){
//            System.out.println(e.getCause());
            return 0;
//            throw new Exception("User not found");
        } catch (Exception e){
//            System.out.println(e.getCause());
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }

    @Override
    public void ChangePassword(ChangePasswordDto dto, String emailPhone) throws Exception {
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try{
            User existingUser = session.selectOne("User.getUserByPhoneOrEmail", emailPhone);

            if (existingUser != null && existingUser.phone.equals(dto.currentPassword)) throw new Exception("Account not found");

//            assert existingUser != null;
            if (!VerifyPasswordHash(dto.currentPassword, existingUser.passwordHash, existingUser.passwordSalt)) throw new Exception("Username or password is incorrect.");

            existingUser.passwordSalt = generateSalt();
            existingUser.passwordHash = CreatePasswordHash(dto.newPassword, existingUser.passwordSalt);
            existingUser.lastUpdatedDate = DateTime();

//            user.lastUpdatedDate = DateTime();
            existingUser.updatedBy = existingUser.id;
            existingUser.updatedDate = DateTime();
            existingUser.isDefaultPass = false;
//            existingUser.up

            session.update("User.update", existingUser);

            LogAction("Password Change", "Changed password on account: "+ existingUser.getEmail()+"/"+existingUser.getPhone(), existingUser.getId(), existingUser.getId(), existingUser.getBusinessId(), "0.0.0.0");



        } catch (NullPointerException e){
//            return 0;
            throw new Exception("User not found");
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }

    }

    // private helper methods

    private static byte[] CreatePasswordHash(String password, byte[] passwordSalt) throws Exception {
        if (password == null) throw new Exception("password cannot be null");
        if (password.trim().isEmpty()) throw new Exception("password value cannot be empty or whitespace only string.");

        String HMACSHA = "HmacSHA512";
        try {
            Mac hmac_sha = Mac.getInstance(HMACSHA);
//            System.out.println("SaltUsed" + Arrays.toString(passwordSalt));
            final SecretKeySpec secretKey = new SecretKeySpec(passwordSalt, HMACSHA);
            hmac_sha.init(secretKey);
            byte[] dataBytes = password.getBytes();

            return hmac_sha.doFinal(dataBytes);
//            StringBuffer stringBuffer = new StringBuffer();
//            for (byte bytes : res) {
//                stringBuffer.append(String.format("%02x", bytes & 0xff));
//            }
//            return; stringBuffer.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            e.printStackTrace();
            return null;
        }
    }

    private static boolean VerifyPasswordHash(String password, byte[] storedHash, byte[] storedSalt) throws Exception {
        if (password == null) throw new Exception("password");
        if (password.trim().isEmpty()) throw new Exception("password value cannot be empty or whitespace only string.");
        if (storedHash.length != 64) throw new Exception("passwordHash invalid length of password hash (64 bytes expected).");
        if (storedSalt.length != 128) throw new Exception("passwordHash invalid length of password salt (128 bytes expected).");

        String HMACSHA = "HmacSHA512";

        Mac hmac_sha = Mac.getInstance(HMACSHA);

//        byte[] hmacKeyBytes = storedSalt;
        final SecretKeySpec secretKey = new SecretKeySpec(storedSalt, HMACSHA);
        hmac_sha.init(secretKey);
        byte[] dataBytes = password.getBytes();
        byte[] res = hmac_sha.doFinal(dataBytes);
        return Arrays.equals(res, storedHash);
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        SecureRandom sr = SecureRandom.getInstance("HmacSHA512");
        byte[] salt = new byte[128];
        sr.nextBytes(salt);
        return salt;
    }

    private void LogAction(String action, String actionDesc, int createdBy, int affectedUser, int businessId, String ipAddress) throws Exception{
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);
        try {
            ActivityLog activityLog = new ActivityLog();
            activityLog.setAction(action);
            activityLog.setCreatedBy(createdBy);
            activityLog.setBusinessId(businessId);
            activityLog.setAffectedUser(affectedUser);
            activityLog.setActionDesc(actionDesc);
            activityLog.setCreatedDate(DateTime());
            activityLog.setTimeStamp(DateTime().getTime()/1000);
            session.insert("ChangeAudit.NewActivity", activityLog);

        } catch (NullPointerException e){
//            return null;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }  finally {
            session.commit();
            session.close();
            close(session);
        }
    }
}
