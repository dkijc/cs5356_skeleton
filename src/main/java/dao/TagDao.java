package dao;

import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.String;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.TAGS;
import static generated.Tables.RECEIPTS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tagName, int receiptId) {
      if(tagExists(tagName)) {
        List<TagsRecord> tag = getTag(tagName);
        ArrayList<Object> receipts = new ArrayList(Arrays.asList(tag.get(0).getReceipts()));

        //iterate through receipts
        if(!receipts.contains(receiptId)) {
          receipts.add(receiptId);
        } else {
          receipts.remove(receiptId);
        }

        dsl.update(TAGS)
        .set(TAGS.RECEIPTS, receipts)
        .where(TAGS.TAG.eq(tagName));
      }

      TagsRecord tagsRecord = dsl
              .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPTS)
              .values(tagName, receiptId)
              .returning(TAGS.ID)
              .fetchOne();

      checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
    }

    public List<TagsRecord> getTag(String tag) {
        return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).fetch();
    }
    //
    // public List<TagsRecord> getAllTags(String tag) {
    //   return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).where(TAGS.RECEIPT.eq(receiptId));
    // }

    public Boolean tagExists(String tagName) {
      return dsl.select(TAGS.TAG).from(TAGS).where(TAGS.TAG.eq(tagName)).fetchOne() != null ? true : false;
    }

    public List<ReceiptsRecord> getReceiptsWithTag(String tagName) {
      List<TagsRecord> tag = getTag(tagName);
      //int[] receipts = Arrays.stream(tag.get(0).getReceipts()).mapToInt(o -> (int)o).toArray();
      ArrayList<Object> receipts = new ArrayList(Arrays.asList(tag.get(0).getReceipts()));
      List<ReceiptsRecord> receiptsRecord = new ArrayList<ReceiptsRecord>();

      // System.out.println(Arrays.toString(receipts));
      for(Object receiptId : receipts) {
        // Integer receiptId = (int) receipt;
        System.out.println(receiptId.toString());
        List<ReceiptsRecord> record = dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(Integer.parseInt(receiptId.toString()))).fetch();
        receiptsRecord.add(record.get(0));
      }

      return receiptsRecord;
    }
}
