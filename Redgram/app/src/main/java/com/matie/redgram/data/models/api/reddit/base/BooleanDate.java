package com.matie.redgram.data.models.api.reddit.base;

import com.matie.redgram.data.models.api.reddit.main.RedditComment;
import com.matie.redgram.data.models.api.reddit.main.RedditLink;
import com.matie.redgram.data.models.api.reddit.main.RedditListing;
import com.matie.redgram.data.models.api.reddit.main.RedditMore;
import com.matie.redgram.data.models.api.reddit.main.RedditSubreddit;

import org.joda.time.DateTime;

/**
 * Created by matie on 2016-03-07.
 */
public class BooleanDate {

    private BooleanDate() {}

    public class DateInstance extends BooleanDate{
        private DateTime data;

        public DateTime getData() {
            return data;
        }
    }

    public class BooleanInstance extends BooleanDate{
        private Boolean data;

        public Boolean getData() {
            return data;
        }
    }

}
