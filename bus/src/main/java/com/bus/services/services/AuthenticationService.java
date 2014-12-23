package com.bus.services.services;

import com.bus.services.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Service
@Path("/authenticate")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${admin.login.username}")
    private String userName;

    @Value("${admin.login.password}")
    private String password;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response authenticate(User user) {
        log.info("Authenticating ...");
        if(user == null || (StringUtils.isEmpty(user.getPassword()) && StringUtils.isEmpty(user.getUserName())) ){
            return Response.status(Response.Status.BAD_REQUEST).entity("请输入用户名和密码").build();
        }else{
            if(userName.equals(user.getUserName())){
                if(password.equals(user.getPassword())){
                    return Response.ok().build();
                }else{
                    return Response.status(Response.Status.UNAUTHORIZED).entity("您输入的密码有误").build();
                }
            }else{
                return Response.status(Response.Status.UNAUTHORIZED).entity("用户不存在: " + user.getUserName()).build();
            }
        }
    }
}
