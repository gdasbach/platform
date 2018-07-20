package com.proofpoint.featureflag;

import com.google.inject.Injector;
import com.proofpoint.bootstrap.LifeCycleManager;
import com.proofpoint.http.client.HttpClient;
import com.proofpoint.http.client.StringResponseHandler;
import com.proofpoint.http.client.jetty.JettyHttpClient;
import com.proofpoint.http.server.testing.TestingAdminHttpServer;
import com.proofpoint.json.JsonCodec;
import com.proofpoint.json.JsonModule;
import com.proofpoint.node.testing.TestingNodeModule;
import com.proofpoint.reporting.ReportingModule;
import com.proofpoint.testing.Closeables;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.proofpoint.http.client.StringResponseHandler.StringResponse;
import java.util.Map;

import static com.proofpoint.bootstrap.Bootstrap.bootstrapTest;
import static com.proofpoint.http.client.Request.Builder.prepareGet;
import static com.proofpoint.http.client.StringResponseHandler.createStringResponseHandler;
import static com.proofpoint.http.server.testing.TestingAdminHttpServerModule.initializesMainServletTestingAdminHttpServerModule;
import static com.proofpoint.jaxrs.JaxrsModule.explicitJaxrsModule;
import static com.proofpoint.json.JsonCodec.mapJsonCodec;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestFeatureFlagConsoleModule
{
    private final HttpClient client = new JettyHttpClient();
    private static final JsonCodec<Map<String, Object>> MAP_CODEC = mapJsonCodec(String.class, Object.class);

    private LifeCycleManager lifeCycleManager;
    private TestingAdminHttpServer server;
    private Injector injector;

    @BeforeMethod
    public void setup()
            throws Exception
    {
        injector = bootstrapTest()
                .withModules(
                        new TestingNodeModule(),
                        explicitJaxrsModule(),
                        initializesMainServletTestingAdminHttpServerModule(),
                        new JsonModule(),
                        new ReportingModule(),
                        new FeatureFlagModule(),
                        new FeatureFlagConsoleModule())
                .initialize();
        lifeCycleManager = injector.getInstance(LifeCycleManager.class);
        server = injector.getInstance(TestingAdminHttpServer.class);
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
    public void testFF4jResource() throws Exception {
        StringResponse response = client.execute(
                prepareGet().setUri(server.getBaseUrl().resolve("/admin/ff4j-console/")).build(),
                createStringResponseHandler());
        System.out.println(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("FF4J Administration Console"));
    }
}