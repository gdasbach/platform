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

import com.google.inject.Binder;
import com.google.inject.Module;
import org.ff4j.FF4j;

import static com.proofpoint.jaxrs.JaxrsBinder.jaxrsBinder;

public class FeatureFlagModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        if(getClass().getClassLoader().getResource("ff4j.xml") != null) {
            binder.bind(FF4j.class).toInstance(new FF4j("ff4j.xml").audit());
        } else {
            binder.bind(FF4j.class).toInstance(new FF4j().audit());
        }
    }
}
