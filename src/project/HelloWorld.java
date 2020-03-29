package project;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/helloworld")
public class HelloWorld {
  // The Java method will process HTTP GET requests
  @GET
  // The Java method will produce content identified by the MIME Media type "text/plain"
  @Path("/int")
  @Produces("text/plain")
  public int getClichedMessage() {
    // Return some cliched textual content
    return 3+5;
  }
}