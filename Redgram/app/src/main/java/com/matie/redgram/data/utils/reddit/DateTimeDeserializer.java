package com.matie.redgram.data.utils.reddit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Created by matie on 16/04/15.
 */
public class DateTimeDeserializer implements JsonDeserializer<DateTime> {

@Override
public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new DateTime(json.getAsLong() * 1000);
    }

}
