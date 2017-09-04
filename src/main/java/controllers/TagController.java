package controllers;

import api.ReceiptResponse;
import api.TagResponse;

import dao.ReceiptDao;
import dao.TagDao;

import generated.tables.records.ReceiptsRecord;
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
    final ReceiptDao receipts;
    final String doesNotExist;
    final String successful;

    public TagController(TagDao tags, ReceiptDao receipts) {
      this.tags = tags;
      this.receipts = receipts;
      this.doesNotExist = "No receipts with the given tag";
      this.successful = "Receipt updated with given tag, ";
    }

    @PUT
    @Path("{tag}")
    public TagResponse togleTag(@PathParam("tag") String tag, int id) {]
      int receiptId = id;
      List<ReceiptsRecord> receiptRecord = receipts.getReceipt(id);

      if(receiptRecord.isEmpty()) {
        return new TagResponse("Receipt with the id '" + id + "' does not exist");
      }

      List<TagsRecord> tagRecord = tags.getReceiptWithTag(tag, id);

      if(tagRecord.isEmpty()) {
        return new TagResponse("Tag and/or Receipt Id do not match");
      }

      tags.insert(tag, id);

      return new TagResponse(successful + tag);
    }

    @GET
    @Path("{tag}")
    public List<TagResponse> getTaggedReceipts(@PathParam("tag") String tag) {
      List<TagsRecord> tagRecords = tags.getTaggedReceipts(tag);
      if (tagRecords.isEmpty()) {
        return new TagResponse(doesNotExist);
      }
    }
}
