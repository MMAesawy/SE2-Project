package project;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/api")
public class API {
    // The Java method will process HTTP GET requests
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @Path("/register")
    public Response register(
            @QueryParam("username") String username,
            @QueryParam("email") String email,
            @QueryParam("password") String password,
            @QueryParam("type") String type
    ) {
        System.out.println(String.format("Received POST request with parameters:\n" +
                "username: %s, email: %s, password: %s, type: %s", username, email, password, type));
        UserManager userManager = UserManager.getInstance();
        User user = null;
        if (type.equals(UserFactory.ADMIN_TYPE)){
            System.out.println("Returning HTTP 400 response...");
            APIError error = new APIError("Invalid user type");
            return Response.status(400)
                    .entity(error).build();
        }
        else{
            user = UserFactory.makeUser(email, username, password, type);
        }
        ArrayList<UserManagerError> result = userManager.registerUser(user);

        // registration has not occured due to errors
        ArrayList<APIError> errors = new ArrayList<>();
        APIError e;
        if (!result.isEmpty() && result.get(0) != UserManagerError.OK){
            for (UserManagerError error : result){
                switch (error){
                    case USERNAME_INVALID:
                        e = new APIError("Username is invalid.");
                        errors.add(e);
                        System.err.println(e);
                        break;
                    case USERNAME_NOT_AVAILABLE:
                        e = new APIError("Username already exists.");
                        errors.add(e);
                        System.err.println(e);
                        break;
                    case EMAIL_INVALID:
                        e = new APIError("Email is invalid.");
                        errors.add(e);
                        System.err.println(e);
                        break;
                    case EMAIL_NOT_AVAILABLE:
                        e = new APIError("Email already exists.");
                        errors.add(e);
                        System.err.println(e);
                        break;
                    default:
                        e = new APIError("Unknown error!");
                        errors.add(e);
                        System.err.println(e);
                        break;
                }
            }
            System.out.println("Returning HTTP 400 response...");
            return Response.status(400)
                    .entity(new GenericEntity<ArrayList<APIError>>(errors){}).build();
        }
        System.out.println("Returning HTTP 201 response...");
        return Response.status(201).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    @Path("/login")
    public Response login(
            @QueryParam("identifier") String identifier,
            @QueryParam("password") String password
    ){
        UserManager userManager = UserManager.getInstance();
        User result = userManager.login(identifier, password);

        if (result == null){ // username, email, or password is incorrect
            APIError e = new APIError("Username, email, or password is incorrect");
            System.err.println(e);
            System.out.println("Returning HTTP 400 response...");
            return Response.status(400).entity(e).build();
        }
        else{
            System.out.println("Returning HTTP 200 response...");
            return Response.status(200).entity(result).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/getAllUsers")
    public Response getAllUsers(){
        UserManager userManager = UserManager.getInstance();
        ArrayList<User> users = userManager.getAllUsers();
        return Response.status(200)
                .entity(new GenericEntity<List<User>>(users){}).build();
    }
}