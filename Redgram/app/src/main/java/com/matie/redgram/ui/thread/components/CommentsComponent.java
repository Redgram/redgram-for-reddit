package com.matie.redgram.ui.thread.components;

import com.matie.redgram.data.managers.presenters.CommentsPresenter;
import com.matie.redgram.ui.scopes.ThreadScope;
import com.matie.redgram.ui.thread.CommentsFragment;
import com.matie.redgram.ui.thread.modules.CommentsModule;
import com.matie.redgram.ui.thread.views.CommentsView;

import dagger.Component;

@ThreadScope
@Component(
        dependencies = ThreadComponent.class,
        modules = {
                CommentsModule.class
        }
)
public interface CommentsComponent {
    void inject(CommentsFragment fragment);

    CommentsView getCommentsView();
    CommentsPresenter getCommentsPresenter();
}
