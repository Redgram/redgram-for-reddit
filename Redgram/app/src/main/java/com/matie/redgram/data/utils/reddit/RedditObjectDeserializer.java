package com.matie.redgram.data.utils.reddit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.base.RedditObjectWrapper;
import com.matie.redgram.data.models.api.reddit.main.RedditComment;

import java.lang.reflect.Type;

/**
 * Created by matie on 16/04/15.
 */
public class RedditObjectDeserializer implements JsonDeserializer<RedditObject> {
    public static final String TAG = RedditObjectDeserializer.class.getSimpleName();
    @Override
    public RedditObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonObject()){
            return null; //string returned
        }
        try{
            RedditObjectWrapper wrapper = new Gson().fromJson(json, RedditObjectWrapper.class);
            RedditObject redditObject = context.deserialize(wrapper.getData(), wrapper.getKind().getDerivedClass());
            return redditObject;
        }catch (JsonParseException e){
            Log.e(TAG, "Failed to deserialized", e);
            return null;
        }
    }
}
