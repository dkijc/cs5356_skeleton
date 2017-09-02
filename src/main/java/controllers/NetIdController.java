package controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/NetId")
public class NetIdController {
    @GET
    public String getNetId() {
        return "dk768";
    }
}
