package com.proofpoint.cache.caffeine;

import com.github.benmanes.caffeine.jcache.configuration.FactoryCreator;
import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.cache.configuration.Factory;

public final class GuiceFactoryCreator implements FactoryCreator {
    final Injector injector;

    @Inject
    GuiceFactoryCreator(Injector injector) {
        this.injector = injector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Factory<T> factoryOf(String className) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(className);
            return injector.getProvider(clazz)::get;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
