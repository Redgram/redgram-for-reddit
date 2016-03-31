package com.matie.redgram.data.utils.reddit;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.matie.redgram.data.models.api.reddit.base.BooleanDate;

import java.lang.reflect.Type;

/**
 * Created by matie on 2016-03-07.
 */
public class BooleanDateDeserializer implements JsonDeserializer<BooleanDate> {
    public static final String TAG = BooleanDateDeserializer.class.getSimpleName();
    @Override
    public BooleanDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonPrimitive()) {
            return null; //non primitive
        }
        try {
            JsonObject object = new JsonObject();
            if(json.getAsJsonPrimitive().isBoolean()){
                object.addProperty("data", json.getAsBoolean());
                return context.deserialize(object, BooleanDate.BooleanInstance.class);
            }else{
                object.add("data", json);
                return context.deserialize(object, BooleanDate.DateInstance.class);
            }
        }catch (JsonParseException e){
            Log.e(TAG, "Failed to deserialized", e);
            return null;
        }
    }


}
