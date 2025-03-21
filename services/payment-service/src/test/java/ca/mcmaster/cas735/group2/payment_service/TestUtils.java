package ca.mcmaster.cas735.group2.payment_service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public final class TestUtils {

    private TestUtils() {}

    public static final String ORDER_ID = "123";
    public static final String PLATE_NUMBER = "ABC123";
    public static final String LOT_ID = "LOT01";
    public static final String SPOT_ID = "SPOT101";
    public static final String SUCCESS_CC_NUMBER = "987654321";
    public static final String FAILURE_CC_NUMBER = "123456789";
    public static final String CC_EXPIRY = "1223";
    public static final String CC_CVC = "123";
    public static final String SUCCESS_STAFF_ID = "b-tasif";
    public static final String FAILURE_STAFF_ID = "a-tasif";

    public static String convertMapToJson(Map<String, String> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    public static String translate(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
