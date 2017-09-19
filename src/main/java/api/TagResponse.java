package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import java.sql.Time;
import java.util.List;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *s
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class TagResponse {
    @JsonProperty
    String response;

    @JsonProperty
    String tag;
    
    @JsonProperty
    Integer receiptId;

    public TagResponse(String response, String tag, Integer receiptId) {
      this.response = response;
      this.tag = tag;
      this.receiptId = receiptId;
    }

    public TagResponse(TagsRecord dbRecord) {
      this.receiptId = dbRecord.getReceiptid();
      this.tag = dbRecord.getTag();
    }
}
