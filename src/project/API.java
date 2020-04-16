package project;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/api")
public class API {

    public final static String AUTHENTICATION_SCHEME = "Bearer";

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

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/getMyInfo")
    public Response getMyInfo(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token
    ){
        token = stripAuthenticationHeader(token); // strip Bearer

        Privilege privilege = AuthorizationService.verifyToken(token);
        UserManager userManager = UserManager.getInstance();

        User user = null;
        if (privilege != null)
            user = userManager.searchUser(privilege.getUsername());

        if (privilege == null || user == null){ // invalid or expired token
            APIError e = new APIError("Invalid or expired token.");
            System.err.println(e);
            System.out.println("Returning HTTP 401 response...");
            return Response.status(401).entity(e).build();
        }
        else{
            System.out.println("Returning HTTP 200 response...");
            return Response.status(200).entity(user).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/getUserInfo")
    public Response getUserInfo(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @QueryParam("identifier") String identifier
    ){
        token = stripAuthenticationHeader(token); // strip Bearer

        Privilege privilege = AuthorizationService.verifyToken(token);
        UserManager userManager = UserManager.getInstance();

        if (privilege == null){
            return Response.status(401).build();
        }

        User user = userManager.searchUser(identifier);

        if (user == null){
            return Response.status(404).build();
        }

        if (privilege.getAccessType() == AccessType.ADMIN
                || privilege.getUsername().equals(user.getUsername())){
            System.out.println("Returning HTTP 200 response...");
            return Response.status(200).entity(user).build();
        }

        return Response.status(403).build();
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
            String token = AuthorizationService.generateToken(result);
            return Response.status(200).entity(token).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/getAllUsers")
    public Response getAllUsers(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token
    ){
        token = stripAuthenticationHeader(token); // strip Bearer
        Privilege privilege = AuthorizationService.verifyToken(token);

        if (privilege == null){
            return Response.status(401).build();
        }

        if (privilege.getAccessType() != AccessType.ADMIN){
            return Response.status(403).build();
        }

        UserManager userManager = UserManager.getInstance();
        ArrayList<User> users = userManager.getAllUsers();
        return Response.status(200)
                .entity(new GenericEntity<List<User>>(users){}).build();
    }

    private String stripAuthenticationHeader(String header){
        return header.substring(AUTHENTICATION_SCHEME.length()).trim();
    }
}