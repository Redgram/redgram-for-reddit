package com.matie.redgram.ui.user;


import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.feed.links.LinksComponent;
import com.matie.redgram.ui.feed.links.LinksModule;
import com.matie.redgram.ui.feed.links.views.LinksView;
import com.matie.redgram.ui.userlist.UserListComponent;
import com.matie.redgram.ui.userlist.UserListModule;
import com.matie.redgram.ui.userlist.views.UserListControllerView;

import javax.inject.Inject;

public class UserComponentInjector {

    private final UserComponent userComponent;
    private UserListComponent userListComponent;
    private LinksComponent linksComponent;

    @Inject
    public UserComponentInjector(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

    public LinksComponent plusLinksComponent(BaseView baseView, LinksView linksView) {
        if (linksComponent == null) {
            linksComponent = userComponent.plus(new LinksModule(baseView, linksView));
        }

        return linksComponent;
    }

    public void clearLinksComponent() {
        linksComponent = null;
    }

    public UserListComponent plusUserListComponent(UserListControllerView userListView,
                                                   ContentView contentView,
                                                   boolean enableDefault) {
        if (userListComponent == null) {
            userListComponent = userComponent.plus(new UserListModule(userListView, contentView, enableDefault));
        }

        return userListComponent;
    }

    public void clearUserListComponent() {
        userListComponent = null;
    }
}
