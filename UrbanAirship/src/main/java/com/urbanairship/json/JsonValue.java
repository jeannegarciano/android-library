/*
Copyright 2009-2015 Urban Airship Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE URBAN AIRSHIP INC ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL URBAN AIRSHIP INC OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.urbanairship.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.urbanairship.Logger;
import com.urbanairship.util.UAStringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A JsonValue is a representation of any value that can be described using JSON. It can contain one
 * of the following: a JsonMap, a JsonList, a Number, a Boolean, String, or it can contain null.
 * </p>
 * JsonValues can be created from Java Objects by calling {@link #wrap(Object)} or from a JSON
 * String by calling{@link #parseString(String)}. The JsonValue {@link #toString()} returns the
 * JSON String representation of the object.
 */
public class JsonValue implements Parcelable {

    /**
     * A null representation of the JsonValue.
     */
    public final static JsonValue NULL = new JsonValue(null);

    private final Object value;

    /**
     * Constructs a new JsonValue.
     *
     * @param value The wrapped value.
     */
    private JsonValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the raw value of the JsonValue. Will be either a String, Boolean, Long, Double, Integer,
     * JsonMap, JsonArray, or null.
     *
     * @return The raw value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the contained value as a String.
     *
     * @return The value as a String, or null if the value is not a String.
     */
    public String getString() {
        return getString(null);
    }

    /**
     * Gets the contained values as a String.
     *
     * @param defaultValue The default value if the contained value is not a String.
     * @return The value as a String, or the defaultValue if the value is not a String.
     */
    public String getString(String defaultValue) {
        if (isNull()) {
            return null;
        }

        if (isString()) {
            return (String) value;
        }

        return defaultValue;
    }


    /**
     * Gets the contained values as an int.
     *
     * @param defaultValue The default value if the contained value is not a number.
     * @return The value as an int, or the defaultValue if the value is not a number.
     */
    public int getInt(int defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (isInteger()) {
            return (Integer) value;
        }

        if (isNumber()) {
            return ((Number) value).intValue();
        }

        return defaultValue;
    }

    /**
     * Gets the contained values as an double.
     *
     * @param defaultValue The default value if the contained value is not a number.
     * @return The value as a double, or the defaultValue if the value is not a number.
     */
    public double getDouble(double defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (isDouble()) {
            return (Double) value;
        }

        if (isNumber()) {
            return ((Number) value).doubleValue();
        }

        return defaultValue;
    }

    /**
     * Gets the contained values as an long.
     *
     * @param defaultValue The default value if the contained value is not a number.
     * @return The value as a long, or the defaultValue if the value is not a number.
     */
    public long getLong(long defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (isLong()) {
            return (Long) value;
        }

        if (isNumber()) {
            return ((Number) value).longValue();
        }

        return defaultValue;
    }

    /**
     * Gets the contained values as a Number.
     *
     * @return The value as a number or null if the value is null or not a number.
     */
    public Number getNumber() {
        if (isNull() || !isNumber()) {
            return null;
        }

        return (Number) value;
    }

    /**
     * Gets the contained values as a boolean.
     *
     * @param defaultValue The default value if the contained value is not a boolean.
     * @return The value as a boolean, or the defaultValue if the value is not a boolean.
     */
    public boolean getBoolean(boolean defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (isBoolean()) {
            return (Boolean) value;
        }

        return defaultValue;
    }

    /**
     * Gets the contained value as a JsonList.
     *
     * @return The value as a JsonList, or null if the value is not a JsonList.
     */
    public JsonList getList() {
        if (isNull() || !isJsonList()) {
            return null;
        }

        return (JsonList) value;
    }

    /**
     * Gets the contained values as a JsonMap.
     *
     * @return The value as JsonMap, or null if the value is not a JsonMap.
     */
    public JsonMap getMap() {
        if (isNull() || !isJsonMap()) {
            return null;
        }

        return (JsonMap) value;
    }

    /**
     * If the contained value is null.
     *
     * @return <code>true</code> if the contained value is null, otherwise <code>false</code>.
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * Checks if the value is a String.
     *
     * @return {@code true} if the value is a String, otherwise {@code false}.
     */
    public boolean isString() {
        return value instanceof String;
    }

    /**
     * Checks if the value is an Integer.
     *
     * @return {@code true} if the value is an Integer, otherwise {@code false}.
     */
    public boolean isInteger() {
        return value instanceof Integer;
    }

    /**
     * Checks if the value is a Double.
     *
     * @return {@code true} if the value is a Double, otherwise {@code false}.
     */
    public boolean isDouble() {
        return value instanceof Double;
    }

    /**
     * Checks if the value is a Long.
     *
     * @return {@code true} if the value is a Long, otherwise {@code false}.
     */
    public boolean isLong() {
        return value instanceof Long;
    }

    /**
     * Checks if the value is a Number.
     *
     * @return {@code true} if the value is a Number, otherwise {@code false}.
     */
    public boolean isNumber() {
        return value instanceof Number;
    }

    /**
     * Checks if the value is a Boolean.
     *
     * @return {@code true} if the value is a Boolean, otherwise {@code false}.
     */
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    /**
     * Checks if the value is a JsonMap.
     *
     * @return {@code true} if the value is a JsonMap, otherwise {@code false}.
     */
    public boolean isJsonMap() {
        return value instanceof JsonMap;
    }

    /**
     * Checks if the value is a JsonList.
     *
     * @return {@code true} if the value is a JsonList, otherwise {@code false}.
     */
    public boolean isJsonList() {
        return value instanceof JsonList;
    }

    /**
     * Parse a JSON encoded String.
     *
     * @param jsonString The json encoded String.
     * @return A JsonValue from the encoded String.
     * @throws JsonException If the JSON was unable to be parsed.
     */
    public static JsonValue parseString(String jsonString) throws JsonException {
        if (UAStringUtil.isEmpty(jsonString)) {
            return JsonValue.NULL;
        }

        JSONTokener tokener = new JSONTokener(jsonString);

        try {
            return JsonValue.wrap(tokener.nextValue());
        } catch (JSONException e) {
            throw new JsonException("Unable to parse string", e);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof JsonValue)) {
            return false;
        }

        JsonValue o = (JsonValue) object;

        if (isNull()) {
            return o.isNull();
        }

        return value.equals(o.value);
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (value != null) {
            result = 31 * result + value.hashCode();
        }
        return result;
    }

    /**
     * Returns the JsonValue as a JSON encoded String.
     *
     * @return The value as a JSON encoded String.
     */
    @Override
    public String toString() {
        if (isNull()) {
            return "null";
        }

        try {
            if (value instanceof String) {
                return JSONObject.quote((String) value);
            }

            if (value instanceof Number) {
                return JSONObject.numberToString((Number) value);
            }

            if (value instanceof JsonMap || value instanceof JsonList) {
                return value.toString();
            }

            return String.valueOf(value);
        } catch (JSONException e) {
            // Should never happen
            Logger.error("JsonValue - Failed to create JSON String.", e);
            return "";
        }
    }

    /**
     * Helper method that is used to write the value as a JSON String.
     *
     * @param stringer The JSONStringer object.
     * @throws JSONException If the value is unable to be written as JSON.
     */
    void write(JSONStringer stringer) throws JSONException {
        if (isNull()) {
            stringer.value(null);
            return;
        }

        if (value instanceof JsonList) {
            ((JsonList) value).write(stringer);
        } else if (value instanceof JsonMap) {
            ((JsonMap) value).write(stringer);
        } else {
            stringer.value(value);
        }
    }

    /**
     * Wraps a String as a JsonValue.
     *
     * @param value The value as a string.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(String value) {
        return JsonValue.wrap(value, null);
    }

    /**
     * Wraps a char as a JsonValue.
     *
     * @param value The value as a char.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(char value) {
        return JsonValue.wrap(value, null);
    }

    /**
     * Wraps an int as a JsonValue.
     *
     * @param value The value as an int.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(int value) {
        return JsonValue.wrap(value, null);
    }

    /**
     * Wraps a long as a JsonValue.
     *
     * @param value The value as a long.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(long value) {
        return JsonValue.wrap(value, null);
    }

    /**
     *
     * Wraps a boolean as a JsonValue.
     *
     * @param value The value as a boolean.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(boolean value) {
        return JsonValue.wrap(value, null);
    }

    /**
     * Wraps a JsonSerializable object as a JsonValue.
     *
     * @param value The value as a JsonSerializable object.
     * @return The JsonValue object.
     */
    public static JsonValue wrap(JsonSerializable value) {
        return JsonValue.wrap(value, null);
    }

    /**
     * Wraps any valid object into a JsonValue. If the object is unable to be wrapped, the default
     * value will be returned. See {@link #wrap(Object)} for rules on object wrapping.
     *
     * @param object The object to wrap.
     * @param defaultValue The default value if the object is unable to be wrapped.
     * @return The object wrapped in a JsonValue or the default value if the object is unable to be wrapped.
     */
    public static JsonValue wrap(Object object, JsonValue defaultValue) {
        try {
            return wrap(object);
        } catch (JsonException ex) {
            return defaultValue;
        }
    }

    /**
     * Wraps any valid object into a JsonValue.
     * <p/>
     * Objects will be wrapped with the following rules:
     * <ul>
     * <li>JSONObject.NULL or null will result in {@link JsonValue#NULL}.</li>
     * <li>Collections, arrays, JSONArray values will be wrapped into a JsonList</li>
     * <li>Maps with String keys will be wrapped into a JsonMap.</li>
     * <li>Strings, primitive wrapper objects, JsonMaps, and JsonLists will be wrapped directly into a JsonValue</li>
     * <li>Objects that implement {@link JsonSerializable} will return {@link JsonSerializable#toJsonValue} or {@link JsonValue#NULL}.</li>
     * <li>JsonValues will be unmodified.</li>
     * </ul>
     *
     * @param object The object to wrap.
     * @return The object wrapped in a JsonValue.
     * @throws JsonException If the object is not a supported type or contains an unsupported type.
     */
    public static JsonValue wrap(Object object) throws JsonException {
        if (object == null || object == JSONObject.NULL) {
            return NULL;
        }

        if (object instanceof JsonValue) {
            return (JsonValue) object;
        }

        if (object instanceof JsonMap ||
                object instanceof JsonList ||
                object instanceof Boolean ||
                object instanceof Integer ||
                object instanceof Long ||
                object instanceof String) {
            return new JsonValue(object);
        }

        if (object instanceof JsonSerializable) {
            JsonValue jsonValue = ((JsonSerializable) object).toJsonValue();
            return jsonValue == null ? JsonValue.NULL : jsonValue;
        }

        if (object instanceof Byte || object instanceof Short) {
            return new JsonValue(((Number) object).intValue());
        }

        if (object instanceof Character) {
            Character character = (Character) object;
            return new JsonValue(character.toString());
        }

        if (object instanceof Float) {
            return new JsonValue(((Number) object).doubleValue());
        }

        if (object instanceof Double) {
            Double d = (Double) object;
            if (d.isInfinite() || d.isNaN()) {
                throw new JsonException("Invalid Double value: " + d);
            }

            return new JsonValue(object);
        }

        try {
            if (object instanceof JSONArray) {
                return wrapJSONArray((JSONArray) object);
            }

            if (object instanceof JSONObject) {
                return wrapJSONObject((JSONObject) object);
            }

            if (object instanceof Collection) {
                return wrapCollection((Collection) object);
            }

            if (object.getClass().isArray()) {
                return wrapArray(object);
            }

            if (object instanceof Map) {
                return wrapMap((Map) object);
            }
        } catch (JsonException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new JsonException("Failed to wrap value.", exception);
        }

        throw new JsonException("Illegal object: " + object);
    }

    /**
     * Helper method to wrap an array.
     *
     * @param array The array to wrap.
     * @return The wrapped array.
     * @throws JsonException If the array contains an unwrappable object.
     */
    private static JsonValue wrapArray(Object array) throws JsonException {
        final int length = Array.getLength(array);
        List<JsonValue> list = new ArrayList<>(length);

        for (int i = 0; i < length; ++i) {
            list.add(wrap(Array.get(array, i)));
        }

        return new JsonValue(new JsonList(list));
    }

    /**
     * Helper method to wrap a collection.
     *
     * @param collection The collection to wrap.
     * @return The wrapped array.
     * @throws JsonException If the collection contains an unwrappable object.
     */
    private static JsonValue wrapCollection(Collection collection) throws JsonException {
        List<JsonValue> list = new ArrayList<>();

        for (Object obj : collection) {
            list.add(wrap(obj));
        }

        return new JsonValue(new JsonList(list));
    }

    /**
     * Helper method to wrap a Map.
     *
     * @param map The map to wrap.
     * @return The wrapped map.
     * @throws JsonException If the collection contains an unwrappable object.
     */
    private static JsonValue wrapMap(Map<?, ?> map) throws JsonException {
        Map<String, JsonValue> jsonValueMap = new HashMap<>();

        for (Map.Entry entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new JsonException("Only string map keys are accepted.");
            }

            jsonValueMap.put((String) entry.getKey(), wrap(entry.getValue()));
        }

        return new JsonValue(new JsonMap(jsonValueMap));
    }

    /**
     * Helper method to wrap a JSONArray.
     *
     * @param jsonArray The JSONArray to wrap.
     * @return The wrapped JSONArray.
     * @throws JsonException If the collection contains an unwrappable object.
     */
    private static JsonValue wrapJSONArray(JSONArray jsonArray) throws JsonException {
        List<JsonValue> list = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.isNull(i)) {
                list.add(NULL);
                continue;
            }

            list.add(wrap(jsonArray.opt(i)));
        }

        return new JsonValue(new JsonList(list));
    }

    /**
     * Helper method to wrap a JSONObject.
     *
     * @param jsonObject The JSONObject to wrap.
     * @return The wrapped JSONObject.
     * @throws JsonException If the collection contains an unwrappable object.
     */
    private static JsonValue wrapJSONObject(JSONObject jsonObject) throws JsonException {
        Map<String, JsonValue> jsonValueMap = new HashMap<>();

        if (jsonObject == null || jsonObject.length() == 0) {
            return new JsonValue(jsonValueMap);
        }

        Iterator iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            if (jsonObject.isNull(key)) {
                jsonValueMap.put(key, NULL);
                continue;
            }

            jsonValueMap.put(key, wrap(jsonObject.opt(key)));
        }

        return new JsonValue(new JsonMap(jsonValueMap));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toString());
    }

    /**
     * JsonValue parcel creator.
     */
    public static final Parcelable.Creator<JsonValue> CREATOR = new Parcelable.Creator<JsonValue>() {

        @Override
        public JsonValue createFromParcel(Parcel in) {
            try {
                return JsonValue.parseString(in.readString());
            } catch (JsonException e) {
                Logger.error("JsonValue - Unable to create JsonValue from parcel.", e);
                return null;
            }
        }

        @Override
        public JsonValue[] newArray(int size) {
            return new JsonValue[size];
        }
    };
}
