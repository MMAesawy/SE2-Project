package project;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/api")
public class API {
    // The Java method will process HTTP GET requests
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @Path("/register")
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
        ArrayList<APIError> errors = new ArrayList<>();
        if (!result.isEmpty() && result.get(0) != UserManager.errors.OK){
            for (UserManager.errors error : result){
                switch (error){
                    case USERNAME_INVALID:
                        errors.add(new APIError("Username is invalid."));
                        break;
                    case USERNAME_NOT_AVAILABLE:
                        errors.add(new APIError("Username already exists."));
                        break;
                    case EMAIL_INVALID:
                        errors.add(new APIError("Email is invalid."));
                        break;
                    case EMAIL_NOT_AVAILABLE:
                        errors.add(new APIError("Email already exists."));
                        break;
                    default:
                        errors.add(new APIError("Unknown error!"));
                        break;
                }
            }
            return Response.status(400)
                    .entity(errors).build();
        }
        return Response.status(201).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @Path("/login")
    public Response login(
            @FormParam("identifier") String identifier,
            @FormParam("password") String password
    ){
        UserManager userManager = UserManager.getInstance();
        User result = userManager.login(identifier, password);

        if (result == null){ // username, email, or password is incorrect
            return Response.status(400).entity(new APIError("Username, email, or password is incorrect")).build();
        }
        else{
            return Response.status(200).entity(result).build();
        }
    }
}