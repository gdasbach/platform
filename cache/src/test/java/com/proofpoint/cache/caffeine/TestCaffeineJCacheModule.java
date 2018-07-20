/*
 * Copyright 2010 Proofpoint, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proofpoint.cache.caffeine;

import com.github.benmanes.caffeine.jcache.configuration.TypesafeConfigurator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.*;
import com.google.inject.util.Modules;
import com.proofpoint.cache.CacheModule;
import com.proofpoint.cache.caffeine.CaffeineJCacheModule;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheResult;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.integration.CacheLoader;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static org.testng.Assert.assertEquals;

public class TestCaffeineJCacheModule
{
    @Inject
    CacheManager cacheManager;
    @Inject Service service;

    @BeforeMethod
    public void setUp()
            throws Exception
    {
        Injector injector = Guice.createInjector(Modules.override(new CacheModule()).with(new CaffeineJCacheModule()));
        injector.injectMembers(this);

        System.out.println(injector.getBindings());
    }

    @AfterClass
    public void afterClass() {
        TypesafeConfigurator.setFactoryCreator(FactoryBuilder::factoryOf);
    }

    @Test
    public void factory() {
        Cache<Integer, Integer> cache = cacheManager.getCache("guice");
        Map<Integer, Integer> result = cache.getAll(ImmutableSet.of(1, 2, 3));
        assertEquals(result, ImmutableMap.of(1, 1, 2, 2, 3, 3));
    }

    @Test
    public void annotations() {
        for (int i = 0; i < 10; i++) {
            assertEquals(service.get(), Integer.valueOf(1));
        }
        assertEquals(service.times, 1);
    }

    static class Service {
        int times;

        @CacheResult(cacheName = "annotations")
        public Integer get() {
            return ++times;
        }
    }

    public static final class InjectedCacheLoader implements CacheLoader<Integer, Integer> {
        private final Service service;

        @Inject
        InjectedCacheLoader(Service service) {
            this.service = service;
        }

        @Override
        public Integer load(Integer key) {
            return ++service.times;
        }

        @Override
        public Map<Integer, Integer> loadAll(Iterable<? extends Integer> keys) {
            return Maps.toMap(ImmutableSet.copyOf(keys), this::load);
        }
    }
}
