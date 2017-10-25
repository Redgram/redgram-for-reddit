package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

import java.util.Map;

/**
 * Created by matie on 2017-09-27.
 */

public interface ProfileActivityPresenter extends BasePresenter {
    void getListing(String listingType, Map<String,String> params);
}
