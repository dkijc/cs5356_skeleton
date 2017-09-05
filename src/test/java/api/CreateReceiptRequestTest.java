package api;


import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CreateReceiptRequestTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.merchant = "OK";

        receipt.amount = new BigDecimal(33.44);
        assertThat(validator.validate(receipt), empty());
    }

    @Test
    public void testMissingAmount() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.merchant = "OK";

        //receipt.amount = new BigDecimal(33.44);
        assertThat(validator.validate(receipt), empty());
    }

    @Test
    public void testMissingMerchant() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.amount = new BigDecimal(33.44);

        validator.validate(receipt);
        assertThat(validator.validate(receipt), hasSize(1));
    }

    @Test
    public void testInValidAmountType() {
      CreateReceiptRequest receipt = new CreateReceiptRequest();
      receipt.amount = new BigDecimal(12.44);;
      String amount = "12.44";
      validator.validate(receipt);

      assertNotEquals(receipt.amount, amount);
    }
}
