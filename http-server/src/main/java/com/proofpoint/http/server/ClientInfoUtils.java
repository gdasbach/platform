/*
 * Copyright 2016 Proofpoint, Inc.
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
package com.proofpoint.http.server;

import javax.servlet.http.HttpServletRequest;

/**
 * @deprecated Use {@link ClientAddressExtractor}
 */
@Deprecated
public class ClientInfoUtils
{
    private ClientInfoUtils()
    {}

    private static final ClientAddressExtractor clientAddressExtractor = new ClientAddressExtractor();

    /**
     * @deprecated Use {@link ClientAddressExtractor#clientAddressFor(HttpServletRequest)}.
     */
    @Deprecated
    public static String clientAddressFor(HttpServletRequest request)
    {
        return clientAddressExtractor.clientAddressFor(request);
    }
}
