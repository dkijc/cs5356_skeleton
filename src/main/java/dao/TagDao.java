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

    public void insert(String tag, int receiptId) {
      List<Integer> receipts = getReceiptIds(tag);

      //iterate through receipts
      if(!receipts.contains(receiptId)) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPTID)
                .values(tag, receiptId)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
      } else {
        dsl.delete(TAGS)
        .where(TAGS.RECEIPTID.eq(receiptId))
        .and(TAGS.TAG.eq(tag))
        .execute();
      }
    }

    public List<TagsRecord> getTag(String tag) {
        return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).fetch();
    }

    public List<TagsRecord> getTagsForReceipt(int receiptId) {
      return dsl.selectFrom(TAGS).where(TAGS.RECEIPTID.eq(receiptId)).fetch();
    }

    public List<Integer> getReceiptIds(String tag) {
      return dsl.select().from(TAGS).where(TAGS.TAG.eq(tag)).fetch().getValues(TAGS.RECEIPTID);
    }

    public List<ReceiptsRecord> getReceiptsWithTag(String tag) {
      List<ReceiptsRecord> receiptsRecord = dsl.select().from(RECEIPTS)
                                                .join(TAGS).onKey()
                                                .where(TAGS.TAG.eq(tag))
                                                .fetchInto(RECEIPTS);
      return receiptsRecord;
    }
}
