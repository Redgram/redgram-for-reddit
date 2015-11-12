package com.matie.redgram.data.managers.images;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2015-11-01.
 */
@Module
public class ImageModule {
    @ActivityScope
    @Provides
    public ImageManager provideImageManager(App app){
        return new ImageManager();
    }
}
