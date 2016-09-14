package com.matie.redgram.data.utils.reddit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.base.RedditObjectWrapper;
import com.matie.redgram.data.models.api.reddit.base.RedditType;

import java.lang.reflect.Type;

/**
 * Created by matie on 16/04/15.
 */
public class RedditObjectDeserializer implements JsonDeserializer<RedditObject> {

    public static final String KIND = "kind";
    public static final String DATA = "data";
    public static final String CHILDREN = "children";

    public static final String TAG = RedditObjectDeserializer.class.getSimpleName();
    @Override
    public RedditObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonObject()){
            return null; //string returned
        }
        try{
            checkRedditKind(json.getAsJsonObject());
            RedditObjectWrapper wrapper = new Gson().fromJson(json, RedditObjectWrapper.class);
            return context.deserialize(wrapper.getData(), wrapper.getKind().getDerivedClass());
        }catch (JsonParseException e){
            Log.e(TAG, "Failed to deserialized", e);
            return null;
        }
    }

    /**
     * Some Listings carry children with no "kind" property. This method makes sure to add a unique
     * Reddit type that matches the ones in {@link RedditType}
     *
     * @param jsonObject
     */
    private void checkRedditKind(JsonObject jsonObject) {
        String kind = jsonObject.getAsJsonPrimitive(KIND).getAsString();
        //avoids nulls
        if(RedditType.UserList.toString().equalsIgnoreCase(kind)){
            //modify children
            modifyUserListChildren(jsonObject.getAsJsonObject(DATA).getAsJsonArray(CHILDREN));
        }
    }

    private void modifyUserListChildren(JsonArray children) {
        for(int i = 0 ; i < children.size() ; i++){
            JsonElement child = children.get(i);
            if(child.isJsonObject()){
                JsonObject obj = new JsonObject();
                obj.add(KIND, new JsonPrimitive(RedditType.User.toString()));
                obj.add(DATA, child);
                children.set(i, obj);
            }
        }
    }
}
