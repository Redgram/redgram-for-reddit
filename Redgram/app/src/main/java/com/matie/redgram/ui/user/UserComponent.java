package com.matie.redgram.ui.user;

import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.feed.links.LinksComponent;
import com.matie.redgram.ui.feed.links.LinksModule;
import com.matie.redgram.ui.scopes.UserScope;
import com.matie.redgram.ui.userlist.UserListComponent;
import com.matie.redgram.ui.userlist.UserListModule;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public interface UserComponent extends AppComponent {
    UserComponentInjector userComponentInjector();

    LinksComponent plus(LinksModule linksModule);
    UserListComponent plus(UserListModule userListModule);
}
