package com.matie.redgram.data.models.api.reddit.base;

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
