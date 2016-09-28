package com.matie.redgram.ui.common.utils.data;

import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import rx.Observable;

/**
 * Created by matie on 2016-09-25.
 */
public class RxHelper {
    private RxHelper(){}
    public static LifecycleTransformer transformer(BaseContextView contextView) {
        if(contextView instanceof BaseActivity){
            return contextView.getBaseActivity().bindToLifecycle();
        }else{
            return contextView.getBaseFragment().bindToLifecycle();
        }
    }
}
