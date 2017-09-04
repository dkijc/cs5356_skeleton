package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.TagsRecord;
import java.sql.Time;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class TagResponse {
    @JsonProperty
    String tag;

    @JsonProperty
    Integer receiptId;

    @JsonProperty
    String response;

    public TagResponse(TagsRecord dbRecord) {
      this.tag = dbRecord.getTag();
      this.receiptId = dbRecord.getId();
    }

    public TagResponse(String response) {
      this.response = response;
    }
}
