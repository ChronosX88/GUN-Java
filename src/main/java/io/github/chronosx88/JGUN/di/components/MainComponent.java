package io.github.chronosx88.JGUN.di.components;

import dagger.Component;
import io.github.chronosx88.JGUN.di.modules.MainModule;

import javax.inject.Singleton;

@Component(modules = {MainModule.class})
@Singleton
public interface MainComponent {
    <T> void inject(T object);
}
