package com.steelkiwi.errorparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on /31/17.
 */

public class Parser {

    private static final String TAG = Parser.class.getSimpleName();

    private static Map<String, Object> notNestingMap = new HashMap<>();
    private static Map<String, Object> clearedMap    = new HashMap<>();

    /**
     * This method returns first key and first value from json, this parsing will be enough
     * for simple and valid json, if you need to get first key-value message
     *
     * @param stringJson original String json for parsing
     * @return the firs key-value message
     */
    public static String simpleParsing(String stringJson) {
        String errorMessage = "";
        errorMessage = parseFirstError(stringJson, "");
        return errorMessage;
    }

    /**
     * This method returns first value from json without key, this parsing will be enough
     * for simple and valid json, if you need to get first message without key
     *
     * @param stringJson original String json for parsing
     * @param exceptKey  key that need to remove from message
     * @return first value message from json
     */
    public static String simpleParsing(String stringJson, String exceptKey) {
        String errorMessage = "";
        errorMessage = parseFirstError(stringJson, exceptKey);
        return errorMessage;
    }

    /**
     * This method returns first key-value message from json, if except key is present - value
     * without key will be returned. This method parses JSONObject or JSONArray
     *
     * @param stringJson original String json for parsing
     * @param exceptKey  key that need to remove from message
     * @return first key-value or value message depends on exceptKey
     */
    private static String parseFirstError(String stringJson, String exceptKey) {
        String errorKey = "";
        String message = "";
        try {
            int defaultValueIndex = 0;
            JSONObject jsonObject = new JSONObject(stringJson);
            Iterator<String> keys = jsonObject.keys();

            message = jsonObject.getJSONArray(keys.next()).get(defaultValueIndex).toString();

            Iterator<String> iter = jsonObject.keys();
            errorKey = iter.next();
            if (!isNullOrEmpty(errorKey) && !errorKey.equals(exceptKey)) {
                return errorKey + " - " + message;
            } else {
                return message;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(stringJson);
            Iterator<String> keys = jsonObject.keys();

            message = jsonObject.getString(keys.next());

            for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                errorKey = iter.next();
            }
            if (!isNullOrEmpty(errorKey) && !errorKey.equals(exceptKey)) {
                return errorKey + " - " + message;
            } else {
                return message;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static void jsonToMap(String source) throws JSONException {
        JSONObject json = new JSONObject(source);
        if (json != JSONObject.NULL) {
            notNestingMap = toMap(json);
        }
        Log.d(TAG, "cleared map - " + clearedMap.toString());
    }

    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if (isValueValid(value.toString())) {
                notNestingMap.put(key, value);
            }
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof List) {
                if (!((List<Object>) value).isEmpty()) {
                    map.put(key, value);
                }
            } else if (isValueValid(value.toString())) {
                map.put(key, value);
            }
        }
        clearUpMap(map);
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof List) {
                if (!((List<Object>) value).isEmpty()) {
                    list.add(value);
                }
            } else if (isValueValid(value.toString())) {
                list.add(value);
            }
        }
        list.removeAll(Arrays.asList(null, "", ",", " "));
        return list;
    }

    /**
     * This method returns value by key position and message position. This is what
     * you need when you need only one value from list of values in the json by key
     *
     * @param source          original String json for parsing
     * @param keyPosition     needed position of key
     * @param messagePosition needed position of message if there is list of messages
     * @return value without key from original json
     * @throws Exception if index of key or message not valid
     */
    public static String getMessageByPosition(String source, int keyPosition, int messagePosition) throws Exception {
        jsonToMap(source);
        if (keyPosition >= clearedMap.size()) {
            throw new IndexOutOfBoundsException("Make sure that indexes of keyPosition and messagePosition are valid");
        }
        String neededMessage = "";
        int i = 0;
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (i++ == keyPosition) {
                String[] messagesArr = entry.getValue().toString().split(",");
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededMessage;
    }

    /**
     * This method returns value by key and message position. This is what
     * you need when you need only one value from list of values in the json by key
     *
     * @param source          original String json for parsing
     * @param key             needed key
     * @param messagePosition needed position of message if there is list of messages
     * @return value without key from original json
     * @throws Exception if index of message position is not valid
     */
    public static String getMessageByPosition(String source, String key, int messagePosition) throws Exception {
        jsonToMap(source);
        String neededMessage = "";
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                String[] messagesArr = entry.getValue().toString().split(",");
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededMessage;
    }

    /**
     * This method returns key-value message. This is what
     * you need when you need key-value message from json
     *
     * @param source          original String json for parsing
     * @param keyPosition     position of key
     * @param messagePosition position of message
     * @return key-value message
     * @throws Exception if index of key or message is not valid
     */
    public static String getKeyWithMessageByPosition(String source, int keyPosition, int messagePosition) throws Exception {
        jsonToMap(source);
        if (keyPosition >= clearedMap.size()) {
            throw new IndexOutOfBoundsException("Make sure that indexes of keyPosition and messagePosition are valid");
        }
        String neededMessage = "";
        String neededKey = "";
        int i = 0;
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (i++ == keyPosition) {
                String[] messagesArr = entry.getValue().toString().split(",");
                neededKey = entry.getKey().toString();
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededKey + " - " + neededMessage;
    }

    /**
     * This method returns key-value message. This is what
     * you need when you need key-value message from json
     *
     * @param source          original String json for parsing
     * @param key             String key
     * @param messagePosition position of message
     * @return key-value message
     * @throws Exception if index of message is not valid
     */
    public static String getKeyWithMessageByPosition(String source, String key, int messagePosition) throws Exception {
        jsonToMap(source);
        String neededMessage = "";
        String neededKey = "";
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                String[] messagesArr = entry.getValue().toString().split(",");
                neededKey = entry.getKey().toString();
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededKey + " - " + neededMessage;
    }

    /**
     * This method returns value by key.
     * This is what you need when you need value message without key
     *
     * @param source original String json for parsing
     * @param key    String key
     * @return value message, without key
     * @throws Exception when json parsing occurs
     */
    public static String getMessageByKey(String source, String key) throws Exception {
        jsonToMap(source);
        String neededMessage = "";
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                neededMessage = entry.getValue().toString().trim();
                break;
            }
        }
        return neededMessage;
    }

    /**
     * This method returns value by key and message position if there is list of values.
     * This is what you need when you need value message by position without key
     *
     * @param source original String json for parsing
     * @param key    String key
     * @return value message, without key
     * @throws Exception
     */
    public static String getMessageByKey(String source, String key, int messagePosition) throws Exception {
        jsonToMap(source);
        String neededMessage = "";
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (key.equals(entry.getKey().toString())) {
                String[] messagesArr = entry.getValue().toString().split(",");
                neededMessage = messagesArr[messagePosition];
                break;
            }
        }
        return neededMessage;
    }

    /**
     * This method returns value message by key position. Value will be returned without key
     *
     * @param source      original String json for parsing
     * @param keyPosition position of key
     * @return value message by key position. Value will be returned without key
     * @throws JSONException if error of json parsing happened
     */
    public static String getMessageByKeyPosition(String source, int keyPosition) throws JSONException {
        jsonToMap(source);
        if (keyPosition >= clearedMap.size()) {
            throw new IndexOutOfBoundsException("Make sure that index of keyPosition is valid");
        }
        String neededMessage = "";
        int i = 0;
        for (Map.Entry entry : clearedMap.entrySet()) {
            if (i++ == keyPosition) {
                neededMessage = entry.getValue().toString();
                break;
            }
        }
        return neededMessage;
    }

    /**
     * This method returns full String json without nesting and empty objects. Clean String json
     * will be returned
     *
     * @param source original String json for parsing
     * @return full String json without nesting and empty json objects
     * @throws JSONException if error of json parsing happened
     */
    public static String getClearedJson(String source) throws JSONException {
        jsonToMap(source);
        JSONObject json = new JSONObject(clearedMap);
        Log.d(TAG, "json toString - " + json.toString());
        return json.toString();
    }

    private static boolean isValueValid(String value) {
        return !(value.trim().equals("{}") || value.trim().equals("[]") || value.trim().isEmpty()
                || value.trim().equals(","));
    }

    private static void clearUpMap(Map<String, Object> map) {
        for (Map.Entry entry : map.entrySet()) {
            String value = entry.getValue().toString();
            value = value
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll("\\{", "")
                    .replaceAll("\\}", "")
                    .replaceAll("\"", "")
                    .replaceAll("=", "-")
                    .replaceAll("\"", "")
                    .trim();
            if (isValueValid(value)) {
                clearedMap.put(entry.getKey().toString(), value);
            }
        }
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

}
