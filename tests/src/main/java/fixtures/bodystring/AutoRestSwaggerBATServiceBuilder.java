package fixtures.bodystring;

import com.azure.core.annotation.ServiceClientBuilder;
import com.azure.core.http.HttpPipeline;
import com.azure.core.implementation.RestProxy;

/**
 * A builder for creating a new instance of the AutoRestSwaggerBATService type.
 */
@ServiceClientBuilder(serviceClients = AutoRestSwaggerBATService.class)
public final class AutoRestSwaggerBATServiceBuilder {
    /*
     * http://localhost:3000
     */
    private String host;

    /**
     * Sets http://localhost:3000.
     * 
     * @param host the host value.
     * @return the AutoRestSwaggerBATServiceBuilder.
     */
    public AutoRestSwaggerBATServiceBuilder host(String host) {
        this.host = host;
        return this;
    }

    /*
     * The HTTP pipeline to send requests through
     */
    private HttpPipeline pipeline;

    /**
     * Sets The HTTP pipeline to send requests through.
     * 
     * @param pipeline the pipeline value.
     * @return the AutoRestSwaggerBATServiceBuilder.
     */
    public AutoRestSwaggerBATServiceBuilder pipeline(HttpPipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    /**
     * Builds an instance of AutoRestSwaggerBATService with the provided parameters.
     * 
     * @return an instance of AutoRestSwaggerBATService.
     */
    public AutoRestSwaggerBATService build() {
        if (host == null) {
            this.host = "http://localhost:3000";
        }
        if (pipeline == null) {
            this.pipeline = RestProxy.createDefaultPipeline();
        }
        AutoRestSwaggerBATService client = new AutoRestSwaggerBATService(pipeline);
        if (this.host != null) {
            client.setHost(this.host);
        }
        return client;
    }
}
