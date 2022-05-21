package ifrs.edu;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;

@Path("/fault")
public class Fault {

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @Retry(maxRetries = 3, delay = 2000)
    @Fallback(fallbackMethod = "recover")
    @Timeout(7000)

    public String getName(@PathParam("name") String name) {
        if (name.equalsIgnoreCase("error")) {
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
            builder.entity("A requisição tem um erro, contate a galera da TI.");
            Response response = builder.build();
            throw new WebApplicationException(response);
        }
        return name;
    }

    // Método que irá ser executado caso o método getName não se recupere da falha
    public String recover(String name) {
        return "BLBLBALABLABAL";
    }

    @GET
    @Path("/bulkhead/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    @Bulkhead(value=1, waitingTaskQueue = 3)
    public String bulkhead(@PathParam("name") String name) {
        return name;
    }

}
