package org.bizzdeskgroup.api.v1;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.discovery.annotations.RegisterService;
import com.kumuluz.ee.logs.cdi.Log;
import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.Info;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import org.bizzdeskgroup.models.UserRoles;
import org.eclipse.microprofile.auth.LoginConfig;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


//@Log
//@CrossOrigin()
//@CrossOrigin(name = "", allowOrigin = "*", allowGenericHttpRequests = true, maxAge = -1, allowSubdomains = true,
//        supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS", exposedHeaders = "", supportsCredentials = false, tagRequests = true, supportedHeaders = "*")
@CrossOrigin(allowOrigin = "*", allowGenericHttpRequests = true, maxAge = -1, allowSubdomains = true,
        supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS", exposedHeaders = "", supportsCredentials = false, supportedHeaders = "*")
@RegisterService("paysuretax-service")
@ApplicationPath("paysure-service/v1")
@DeclareRoles({UserRoles.Admin, UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.NonIndividual, UserRoles.Individual})
@LoginConfig(authMethod = "MP-JWT")
@SwaggerDefinition(info = @Info(title = "PaySure-Tax API", version = "v1.0.0"), securityDefinition =
@SecurityDefinition(
        apiKeyAuthDefinitions = {
                @ApiKeyAuthDefinition(
                        in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER,
                        key = "authorization",
                        name = "Authorization",
                        description = "Bearer Token"
                )
        }
))
public class PaySureTaxApp extends Application {

}
