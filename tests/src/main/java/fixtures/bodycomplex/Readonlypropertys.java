package fixtures.bodycomplex;

import com.azure.core.annotation.BodyParam;
import com.azure.core.annotation.ExpectedResponses;
import com.azure.core.annotation.Get;
import com.azure.core.annotation.Host;
import com.azure.core.annotation.Put;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ReturnValueWireType;
import com.azure.core.annotation.ServiceInterface;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.annotation.UnexpectedResponseExceptionType;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.SimpleResponse;
import com.azure.core.implementation.RestProxy;
import fixtures.bodycomplex.models.ErrorException;
import fixtures.bodycomplex.models.ReadonlyObj;
import reactor.core.publisher.Mono;

/**
 * An instance of this class provides access to all the operations defined in
 * Readonlypropertys.
 */
public final class Readonlypropertys {
    /**
     * The proxy service used to perform REST calls.
     */
    private ReadonlypropertysService service;

    /**
     * The service client containing this operation class.
     */
    private AutoRestComplexTestService client;

    /**
     * Initializes an instance of Readonlypropertys.
     * 
     * @param client the instance of the service client containing this operation class.
     */
    public Readonlypropertys(AutoRestComplexTestService client) {
        this.service = RestProxy.create(ReadonlypropertysService.class, client.getHttpPipeline());
        this.client = client;
    }

    /**
     * The interface defining all the services for
     * AutoRestComplexTestServiceReadonlypropertys to be used by the proxy
     * service to perform REST calls.
     */
    @Host("http://localhost:3000")
    @ServiceInterface(name = "AutoRestComplexTestServiceReadonlypropertys")
    private interface ReadonlypropertysService {
        @Get("/complex/readonlyproperty/valid")
        @ExpectedResponses({200})
        @ReturnValueWireType(ReadonlyObj.class)
        @UnexpectedResponseExceptionType(ErrorException.class)
        Mono<SimpleResponse<ReadonlyObj>> getValid();

        @Put("/complex/readonlyproperty/valid")
        @ExpectedResponses({200})
        @ReturnValueWireType(void.class)
        @UnexpectedResponseExceptionType(ErrorException.class)
        Mono<Response<Void>> putValid(@BodyParam("application/json") ReadonlyObj ComplexBody);
    }

    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<SimpleResponse<ReadonlyObj>> getValidWithResponseAsync() {
        return service.getValid();
    }

    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> putValidWithResponseAsync(ReadonlyObj ComplexBody) {
        return service.putValid(ComplexBody);
    }
}
