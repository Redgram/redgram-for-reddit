package com.matie.redgram.data.network.api.reddit;


import android.widget.Toast;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceInterface;
import com.matie.redgram.data.network.api.reddit.user.RedditClientUserNullProxy;
import com.matie.redgram.data.network.api.utils.AccessLevel;
import com.matie.redgram.data.network.api.utils.Security;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class RedditServiceInvocationHandler implements InvocationHandler {

    private final RedditServiceInterface redditService;
    private final DatabaseManager databaseManager;
    private final RedditClientUserNullProxy nullProxy;
    private ToastHandler toastHandler;

    RedditServiceInvocationHandler(RedditServiceInterface redditService,
                                   DatabaseManager databaseManager,
                                   ToastHandler toastHandler) {
        this.redditService = redditService;
        this.databaseManager = databaseManager;
        this.nullProxy = new RedditClientUserNullProxy();
        this.toastHandler = toastHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Security annotation = method.getAnnotation(Security.class);

        if (guestUserInvokingAuthMethod(annotation) && toastHandler != null) {
            toastHandler.showBackgroundToast("Please log in to perform this action", Toast.LENGTH_SHORT);

            //follow Null Object Pattern here - call on the null proxy
            return method.invoke(nullProxy, args);
        }

        return method.invoke(redditService, args);
    }

    private boolean guestUserInvokingAuthMethod(Security annotation) {
        return databaseManager.getCurrentToken() != null
                && User.USER_GUEST.equalsIgnoreCase(databaseManager.getCurrentToken().getHolderType())
                && checkAnnotationAccessLevel(annotation, AccessLevel.USER);
    }

    private boolean checkAnnotationAccessLevel(Security annotation, AccessLevel level) {
        return annotation != null && annotation.accessLevel() == level;
    }
}
