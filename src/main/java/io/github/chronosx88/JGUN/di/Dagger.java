package io.github.chronosx88.JGUN.di;

import io.github.chronosx88.JGUN.di.components.MainComponent;

public class Dagger {
    private static MainComponent mainComponent = DaggerMainComponent.create();

    public static MainComponent getMainComponent() {
        return mainComponent;
    }
}
