package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;
import com.matie.redgram.data.models.db.User;

public interface MainPresenter extends BasePresenter {
    User getSessionUser();
}
