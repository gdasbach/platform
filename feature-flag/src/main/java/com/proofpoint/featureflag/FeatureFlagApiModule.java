/*
 * Copyright 2018 Proofpoint, Inc.
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
package com.proofpoint.featureflag;

import com.google.common.base.Supplier;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.proofpoint.featureflag.api.*;
import org.ff4j.FF4j;
import org.ff4j.web.api.resources.RuntimeExceptionMapper;

import javax.annotation.PostConstruct;

import static com.proofpoint.jaxrs.JaxrsBinder.jaxrsBinder;

public class FeatureFlagApiModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        jaxrsBinder(binder).bindAdmin(FF4jResource.class);
        jaxrsBinder(binder).bindAdmin(FeatureResource.class);
        jaxrsBinder(binder).bindAdmin(FeatureStoreResource.class);
        jaxrsBinder(binder).bindAdmin(GroupResource.class);
        jaxrsBinder(binder).bindAdmin(MonitoringResource.class);
        jaxrsBinder(binder).bindAdmin(PropertyResource.class);
        jaxrsBinder(binder).bindAdmin(PropertyStoreResource.class);
        jaxrsBinder(binder).bindAdmin(RuntimeExceptionMapper.class);
    }
}
