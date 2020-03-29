package project;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/api")
public class API {
    // The Java method will process HTTP GET requests
    @POST
    @Path("/register")
    @Produces "Application"
    public Response register(
            @FormParam("username") String username,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("type") String type
    ) {
        UserManager userManager = UserManager.getInstance();
        User user = null;
        if (type.equals(UserManager.BUYER_TYPE)){
            user = new Buyer(email, username, password);
        }
        else{
            user = new StoreOwner(email, username, password);
        }
        ArrayList<UserManager.errors> result = userManager.registerUser(user);

        // registration has not occured due to errors
        if (!result.isEmpty() && result.get(0) != UserManager.errors.OK){
            for (UserManager.errors error : result){
                switch (error){
                    case USERNAME_INVALID:

                        break;
                    case USERNAME_NOT_AVAILABLE:

                        break;
                    case EMAIL_INVALID:

                        break;
                    case EMAIL_NOT_AVAILABLE:

                        break;
                    default:

                        break;
                }
            }
        }
    }

    @POST
    @Path("/login")
    @Produces("XML")
    public Response login(
            @FormParam("identifier") String identifier,
            @FormParam("password") String password
    ){
        UserManager userManager = UserManager.getInstance();
        User result = userManager.login(identifier, password);

        if (result == null){ // username, email, or password is incorrect

        }
        else{
            // TODO return the users details ???
        }
    }
}