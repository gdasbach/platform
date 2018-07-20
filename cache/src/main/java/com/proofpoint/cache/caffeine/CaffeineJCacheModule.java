package com.proofpoint.cache.caffeine;

import com.github.benmanes.caffeine.jcache.configuration.FactoryCreator;
import com.github.benmanes.caffeine.jcache.configuration.TypesafeConfigurator;
import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.google.inject.AbstractModule;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;

public class CaffeineJCacheModule extends AbstractModule {
    @Override
    protected void configure() {
        CachingProvider provider = Caching.getCachingProvider(
                CaffeineCachingProvider.class.getName());
        CacheManager cacheManager = provider.getCacheManager(
                provider.getDefaultURI(), provider.getDefaultClassLoader());
        bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
        bind(CacheManager.class).toInstance(cacheManager);

        requestStaticInjection(TypesafeConfigurator.class);

        bind(FactoryCreator.class).to(GuiceFactoryCreator.class);
    }
}
