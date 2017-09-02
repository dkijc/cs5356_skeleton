package dao;

import api.ReceiptResponse;
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

    public int insert(String tag, Integer receiptId) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPTID)
                .values(tag, receiptId)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();
    }

    public List<TagsRecord> getTaggedReceipts() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
