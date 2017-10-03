package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            //String regEx = "^[$]?(([1-9]\\d\{0,2\}\{,\\d\{3\}\}*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
            // String amountFinderRE = "[0-9]+\\.*[0-9]*";
            String amountFinderRE = "[*]?[$]?(([1-9]\\d\\{0,2\\}\\{,\\d\\{3\\}\\}*)(\\.\\d\\d)?$|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
            
            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                out.printf("Position : %s\n", annotation.getBoundingPoly());
                out.printf("Text: %s\n", annotation.getDescription());

                String description = annotation.getDescription();

                if (merchantName == null) {
                    out.printf("merchant is..?: %s\n", description.substring(0,description.indexOf('\n')));
                    merchantName = description.substring(0,description.indexOf('\n'));
                }

                if (description.matches(amountFinderRE)){
                    out.printf("amount is..?: %s\n", description);
                    if (description.indexOf('$') == 0) {
                        amount = new BigDecimal(description.substring(description.indexOf('$') + 1));
                    } else {
                        amount = new BigDecimal(description);
                    }
                }
                
                // This only gets the amount with dollar symbol!!
                // try {
                //     Number number = NumberFormat.getCurrencyInstance().parse(description);
                //     if (number != null) {
                //         out.printf("amount is.." + number.toString());
                //         amount = new BigDecimal(number.toString());
                //     }
                // } catch (Exception ex){
                //     continue;    
                // }
            }

            return new ReceiptSuggestionResponse(merchantName, amount);
            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            
        }
    }
}
