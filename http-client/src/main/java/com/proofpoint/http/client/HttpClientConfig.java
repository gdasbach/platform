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
package com.proofpoint.http.client;

import com.google.common.net.HostAndPort;
import com.proofpoint.configuration.Config;
import com.proofpoint.configuration.DefunctConfig;
import com.proofpoint.configuration.LegacyConfig;
import com.proofpoint.units.DataSize;
import com.proofpoint.units.DataSize.Unit;
import com.proofpoint.units.Duration;
import com.proofpoint.units.MinDuration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@DefunctConfig({"http-client.max-connections", "http-client.keep-alive-interval"})
public class HttpClientConfig
{
    public static final String JAVAX_NET_SSL_KEY_STORE = "javax.net.ssl.keyStore";
    public static final String JAVAX_NET_SSL_KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    public static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    public static final String JAVAX_NET_SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    private Duration connectTimeout = new Duration(2, TimeUnit.SECONDS);
    private Duration requestTimeout = null;
    private Duration idleTimeout = new Duration(1, TimeUnit.MINUTES);
    private int maxConnectionsPerServer = 20;
    private int maxRequestsQueuedPerDestination = 20;
    private DataSize maxContentLength = new DataSize(16, Unit.MEGABYTE);
    private HostAndPort socksProxy;
    private String keyStorePath = System.getProperty(JAVAX_NET_SSL_KEY_STORE);
    private String keyStorePassword = System.getProperty(JAVAX_NET_SSL_KEY_STORE_PASSWORD);
    private String trustStorePath;
    private String trustStorePassword;

    @NotNull
    @MinDuration("0ms")
    public Duration getConnectTimeout()
    {
        return connectTimeout;
    }

    @Config("http-client.connect-timeout")
    public HttpClientConfig setConnectTimeout(Duration connectTimeout)
    {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @MinDuration("0ms")
    public Duration getRequestTimeout()
    {
        return requestTimeout;
    }

    @Config("http-client.request-timeout")
    public HttpClientConfig setRequestTimeout(Duration requestTimeout)
    {
        this.requestTimeout = requestTimeout;
        return this;
    }

    @NotNull
    @MinDuration("0ms")
    public Duration getIdleTimeout()
    {
        return idleTimeout;
    }

    @Config("http-client.idle-timeout")
    @LegacyConfig("http-client.read-timeout")
    public HttpClientConfig setIdleTimeout(Duration idleTimeout)
    {
        this.idleTimeout = idleTimeout;
        return this;
    }

    @Min(1)
    public int getMaxConnectionsPerServer()
    {
        return maxConnectionsPerServer;
    }

    @Config("http-client.max-connections-per-server")
    public HttpClientConfig setMaxConnectionsPerServer(int maxConnectionsPerServer)
    {
        this.maxConnectionsPerServer = maxConnectionsPerServer;
        return this;
    }

    @Min(0)
    public int getMaxRequestsQueuedPerDestination()
    {
        return maxRequestsQueuedPerDestination;
    }

    @Config("http-client.max-requests-queued-per-destination")
    public HttpClientConfig setMaxRequestsQueuedPerDestination(int maxRequestsQueuedPerDestination)
    {
        this.maxRequestsQueuedPerDestination = maxRequestsQueuedPerDestination;
        return this;
    }

    @NotNull
    public DataSize getMaxContentLength()
    {
        return maxContentLength;
    }

    @Config("http-client.max-content-length")
    public HttpClientConfig setMaxContentLength(DataSize maxContentLength)
    {
        this.maxContentLength = maxContentLength;
        return this;
    }

    public HostAndPort getSocksProxy()
    {
        return socksProxy;
    }

    @Config("http-client.socks-proxy")
    public HttpClientConfig setSocksProxy(HostAndPort socksProxy)
    {
        this.socksProxy = socksProxy;
        return this;
    }

    public String getKeyStorePath()
    {
        return keyStorePath;
    }

    @Config("http-client.key-store-path")
    public HttpClientConfig setKeyStorePath(String keyStorePath)
    {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public String getKeyStorePassword()
    {
        return keyStorePassword;
    }

    @Config("http-client.key-store-password")
    public HttpClientConfig setKeyStorePassword(String keyStorePassword)
    {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public String getTrustStorePath()
    {
        return trustStorePath;
    }

    @Config("http-client.trust-store-path")
    public HttpClientConfig setTrustStorePath(String trustStorePath)
    {
        this.trustStorePath = trustStorePath;
        return this;
    }

    public String getTrustStorePassword()
    {
        return trustStorePassword;
    }

    @Config("http-client.trust-store-password")
    public HttpClientConfig setTrustStorePassword(String trustStorePassword)
    {
        this.trustStorePassword = trustStorePassword;
        return this;
    }
}
