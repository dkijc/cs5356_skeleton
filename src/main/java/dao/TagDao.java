package dao;

import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tag, int receiptId) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPT)
                .values(tag, receiptId)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
    }

    public List<TagsRecord> getTaggedReceipts(String tag) {
        return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).fetch();
    }

    public TagsRecord getReceiptWithTag(String tag, int receiptId) {
      return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).where(TAGS.RECEIPT.eq(receiptId));
    }
}
