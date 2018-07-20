package com.proofpoint.featureflag;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.proofpoint.bootstrap.LifeCycleManager;
import com.proofpoint.featureflag.FeatureFlagModule;
import com.proofpoint.featureflag.testapi.TestingResource;
import com.proofpoint.http.client.HttpClient;
import com.proofpoint.http.client.StringResponseHandler.StringResponse;
import com.proofpoint.http.client.jetty.JettyHttpClient;
import com.proofpoint.http.server.testing.TestingAdminHttpServer;
import com.proofpoint.http.server.testing.TestingHttpServer;
import com.proofpoint.http.server.testing.TestingHttpServerModule;
import com.proofpoint.json.JsonCodec;
import com.proofpoint.json.JsonModule;
import com.proofpoint.node.testing.TestingNodeModule;
import com.proofpoint.reporting.ReportingModule;
import com.proofpoint.testing.Closeables;
import org.ff4j.FF4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.proofpoint.http.client.JsonResponseHandler.createJsonResponseHandler;
import static com.proofpoint.http.client.StringResponseHandler.createStringResponseHandler;
import static com.proofpoint.http.server.testing.TestingAdminHttpServerModule.initializesMainServletTestingAdminHttpServerModule;
import static com.proofpoint.jaxrs.JaxrsBinder.jaxrsBinder;
import static com.proofpoint.json.JsonCodec.mapJsonCodec;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import static com.proofpoint.bootstrap.Bootstrap.bootstrapTest;
import static com.proofpoint.http.client.Request.Builder.prepareGet;
import static com.proofpoint.jaxrs.JaxrsModule.explicitJaxrsModule;
import static com.proofpoint.testing.Assertions.assertContains;
import static org.testng.Assert.*;

public class TestFeatureFlagModule
{
    private final HttpClient client = new JettyHttpClient();
    private static final JsonCodec<Map<String, Object>> MAP_CODEC = mapJsonCodec(String.class, Object.class);

    private LifeCycleManager lifeCycleManager;
    private TestingHttpServer server;
    private Injector injector;

    @BeforeMethod
    public void setup()
            throws Exception
    {
        injector = bootstrapTest()
                .withModules(
                        new TestingNodeModule(),
                        explicitJaxrsModule(),
                        new TestingHttpServerModule(),
                        new JsonModule(),
                        new ReportingModule(),
                        new FeatureFlagModule(),
                        binder -> jaxrsBinder(binder).bind(TestingResource.class))
                .initialize();
        lifeCycleManager = injector.getInstance(LifeCycleManager.class);
        server = injector.getInstance(TestingHttpServer.class);

        FF4j ff4j = injector.getInstance(FF4j.class);
        if(ff4j != null && ff4j.exist("TestingResource"))
            ff4j.delete("TestingResource");
    }

    @AfterMethod(alwaysRun = true)
    public void teardown()
            throws Exception
    {
        if (lifeCycleManager != null) {
            lifeCycleManager.stop();
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardownClass()
    {
        Closeables.closeQuietly(client);
    }

    @Test
    public void testConfigured(){
        FF4j ff4j = injector.getInstance(FF4j.class);
        assertNotNull(ff4j);
        assertNotNull(ff4j.getFeatures());
    }

    @Test
    public void testFeatureMissing() throws URISyntaxException {
        FF4j ff4j = injector.getInstance(FF4j.class);
        assertFalse(ff4j.exist("TestingResource"));

        // Verify when feature is missing
        StringResponse response = client.execute(
                prepareGet().setUri(server.getBaseUrl().resolve("/")).build(),
                createStringResponseHandler());
        assertEquals(response.getBody().trim(), "false");
    }

    @Test
    public void testFeatureEnabled() throws URISyntaxException {
        FF4j ff4j = injector.getInstance(FF4j.class);
        ff4j.createFeature("TestingResource", true);

        // Verify when feature is enabled
        StringResponse response = client.execute(
                prepareGet().setUri(server.getBaseUrl().resolve("/")).build(),
                createStringResponseHandler());
        assertEquals(response.getBody().trim(), "true");
    }

    @Test
    public void testFeatureDisabled() throws URISyntaxException {
        FF4j ff4j = injector.getInstance(FF4j.class);
        ff4j.createFeature("TestingResource", false);

        // Verify when feature is disabled
        StringResponse response = client.execute(
                prepareGet().setUri(server.getBaseUrl().resolve("/")).build(),
                createStringResponseHandler());
        assertEquals(response.getBody().trim(), "false");
    }
}