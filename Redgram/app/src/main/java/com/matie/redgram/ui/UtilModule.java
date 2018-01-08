package com.matie.redgram.ui;

import android.content.Context;

import com.matie.redgram.data.utils.Logger;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Singleton
    @Provides
    public Logger provideLogger() {
        return new Logger();
    }

    @Singleton
    @Provides
    public ToastHandler provideToastHandler(Context context) {
        return new ToastHandler(context);
    }
}
