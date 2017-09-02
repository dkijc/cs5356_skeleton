package controllers;

import api.ReceiptResponse;
import dao.TagDao;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("{tag}")
    public void addTag(@PathParam("tag") String tag) {
      //TODO: add tag to the receipt
    }
    // 
    // @GET
    // @Path("{tag}")
    // public List<TagDao> getTaggedReceipts() {
    //   return List<TagDao>;
    // }
}
