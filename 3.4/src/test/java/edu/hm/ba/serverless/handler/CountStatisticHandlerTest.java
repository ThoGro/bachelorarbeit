package edu.hm.ba.serverless.handler;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.response.GatewayResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CountStatisticHandlerTest {

    private static final Book BOOK = new Book("9783868009217", "Verwesung", "Simon Beckett", Category.FANTASY, "null");

    @Test
    public void successfulResponse() {
        CountStatisticHandler countStatisticHandler = new CountStatisticHandler();
        DynamodbEvent dynamodbEvent = new DynamodbEvent();
        List<DynamodbEvent.DynamodbStreamRecord> records = new ArrayList<>();
        DynamodbEvent.DynamodbStreamRecord record = new DynamodbEvent.DynamodbStreamRecord();
        StreamRecord streamRecord = new StreamRecord();
        Map<String, AttributeValue> newImage = new HashMap<>();
        newImage.put("isbn", new AttributeValue(BOOK.getIsbn()));
        newImage.put("title", new AttributeValue(BOOK.getTitle()));
        newImage.put("author", new AttributeValue(BOOK.getAuthor()));
        newImage.put("category", new AttributeValue(BOOK.getCategory().toString()));
        newImage.put("lender", new AttributeValue("TestUser"));
        streamRecord.setNewImage(newImage);
        Map<String, AttributeValue> oldImage = new HashMap<>();
        oldImage.put("isbn", new AttributeValue(BOOK.getIsbn()));
        oldImage.put("title", new AttributeValue(BOOK.getTitle()));
        oldImage.put("author", new AttributeValue(BOOK.getAuthor()));
        oldImage.put("category", new AttributeValue(BOOK.getCategory().toString()));
        oldImage.put("lender", new AttributeValue("null"));
        streamRecord.setOldImage(oldImage);
        record.setDynamodb(streamRecord);
        records.add(record);
        dynamodbEvent.setRecords(records);
        GatewayResponse response = countStatisticHandler.handleRequest(dynamodbEvent, null);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
