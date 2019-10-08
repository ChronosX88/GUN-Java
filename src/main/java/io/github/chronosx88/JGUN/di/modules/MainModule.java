package io.github.chronosx88.JGUN.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.github.chronosx88.JGUN.GraphNodeSerializer;
import io.github.chronosx88.JGUN.GunGraphNode;

import javax.inject.Singleton;

@Module
public class MainModule {
    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(GunGraphNode.class, new GraphNodeSerializer())
                .create();
    }
}
