package com.azure.autorest.mapper;

import com.azure.autorest.extension.base.model.codemodel.CodeModel;
import com.azure.autorest.extension.base.model.codemodel.ConstantSchema;
import com.azure.autorest.extension.base.model.codemodel.Operation;
import com.azure.autorest.extension.base.model.codemodel.OperationGroup;
import com.azure.autorest.extension.base.model.codemodel.Parameter;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.ClientMethodParameter;
import com.azure.autorest.model.clientmodel.Constructor;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.MethodGroupClient;
import com.azure.autorest.model.clientmodel.Proxy;
import com.azure.autorest.model.clientmodel.ProxyMethod;
import com.azure.autorest.model.clientmodel.ServiceClient;
import com.azure.autorest.model.clientmodel.ServiceClientProperty;
import com.azure.autorest.util.CodeNamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceClientMapper implements IMapper<CodeModel, ServiceClient> {
    private static ServiceClientMapper instance = new ServiceClientMapper();

    private ServiceClientMapper() {
    }

    public static ServiceClientMapper getInstance() {
        return instance;
    }

    @Override
    public ServiceClient map(CodeModel codeModel) {
        JavaSettings settings = JavaSettings.getInstance();

        String serviceClientInterfaceName = (settings.getClientTypePrefix() == null ? "" : settings.getClientTypePrefix())
                + codeModel.getLanguage().getJava().getName();

        String serviceClientClassName = serviceClientInterfaceName;
        if (settings.shouldGenerateClientAsImpl()) {
            serviceClientClassName += "Impl";
        }
        String subpackage = settings.shouldGenerateClientAsImpl() ? settings.getImplementationSubpackage() : null;
        if (settings.isCustomType(serviceClientClassName)) {
            subpackage = settings.getCustomTypesSubpackage();
        }
        String packageName = settings.getPackage(subpackage);

        Proxy serviceClientRestAPI = null;
        List<ClientMethod> serviceClientMethods = new ArrayList<>();
        List<Operation> codeModelRestAPIMethods = codeModel.getOperationGroups().stream()
                .filter(og -> og.getLanguage().getDefault().getName() == null || og.getLanguage().getDefault().getName().isEmpty())
                .flatMap(og -> og.getOperations().stream())
                .collect(Collectors.toList());
        if (!codeModelRestAPIMethods.isEmpty()) {
            String restAPIName = serviceClientInterfaceName + "Service";
            String restAPIBaseURL = codeModel.getGlobalParameters().stream()
                    .filter(p -> "$host".equals(p.getLanguage().getDefault().getName()))
                    .findFirst().get().getClientDefaultValue();
            List<ProxyMethod> restAPIMethods = new ArrayList<>();
            for (Operation codeModelRestAPIMethod : codeModelRestAPIMethods) {
                ProxyMethod restAPIMethod = Mappers.getProxyMethodMapper().map(codeModelRestAPIMethod);
                restAPIMethods.add(restAPIMethod);
            }
            serviceClientRestAPI = new Proxy(restAPIName, serviceClientInterfaceName, restAPIBaseURL, restAPIMethods);
            serviceClientMethods = codeModelRestAPIMethods.stream()
                    .flatMap(m -> Mappers.getClientMethodMapper().map(m).stream())
                    .collect(Collectors.toList());
        }

        List<MethodGroupClient> serviceClientMethodGroupClients = new ArrayList<>();
        List<OperationGroup> codeModelMethodGroups = codeModel.getOperationGroups().stream()
                .filter(og -> og.getLanguage().getDefault().getName() != null && !og.getLanguage().getDefault().getName().isEmpty())
                .collect(Collectors.toList());
        for (OperationGroup codeModelMethodGroup : codeModelMethodGroups) {
            serviceClientMethodGroupClients.add(Mappers.getMethodGroupMapper().map(codeModelMethodGroup));
        }

        boolean usesCredentials = false;

        List<ServiceClientProperty> serviceClientProperties = new ArrayList<>();
        for (Parameter p : codeModel.getOperationGroups().stream()
                .flatMap(og -> og.getOperations().stream())
                .flatMap(o -> o.getRequest().getParameters().stream())
                .filter(p -> p.getImplementation() == Parameter.ImplementationLocation.CLIENT)
                .distinct().collect(Collectors.toList())) {
            String serviceClientPropertyDescription = p.getClientDefaultValue();

            String serviceClientPropertyName = CodeNamer.getPropertyName(p.getLanguage().getJava().getName());

            IType serviceClientPropertyClientType = Mappers.getSchemaMapper().map(p.getSchema());
            if (p.isNullable() && serviceClientPropertyClientType != null) {
                serviceClientPropertyClientType = serviceClientPropertyClientType.asNullable();
            }

            boolean serviceClientPropertyIsReadOnly = serviceClientPropertyClientType instanceof ConstantSchema;

            String serviceClientPropertyDefaultValueExpression = serviceClientPropertyClientType.defaultValueExpression(p.getClientDefaultValue());

            if (serviceClientPropertyClientType == ClassType.TokenCredential) {
                usesCredentials = true;
            } else {
                serviceClientProperties.add(new ServiceClientProperty(serviceClientPropertyDescription, serviceClientPropertyClientType, serviceClientPropertyName, serviceClientPropertyIsReadOnly, serviceClientPropertyDefaultValueExpression));
            }
        }
        serviceClientProperties.add(new ServiceClientProperty("The HTTP pipeline to send requests through", ClassType.HttpPipeline, "httpPipeline", true, null));

        ClientMethodParameter tokenCredentialParameter = new ClientMethodParameter(
                "the credentials for Azure",
                false,
                ClassType.TokenCredential,
                "credential",
                true,
                false,
                true,
                null,
                JavaSettings.getInstance().shouldNonNullAnnotations()
                        ? Arrays.asList(ClassType.NonNull)
                        : new ArrayList<>());

        ClientMethodParameter httpPipelineParameter = new ClientMethodParameter(
                "The HTTP pipeline to send requests through",
                false,
                ClassType.HttpPipeline,
                "httpPipeline",
                true,
                false,
                true,
                null,
                JavaSettings.getInstance().shouldNonNullAnnotations()
                        ? Arrays.asList(ClassType.NonNull)
                        : new ArrayList<>());

        List<Constructor> serviceClientConstructors = new ArrayList<>();
        String constructorDescription = String.format("Initializes an instance of %s client.", serviceClientInterfaceName);
        // TODO: Azure Fluent
//        if (settings.isAzureOrFluent())
//        {
//            if (usesCredentials)
//            {
//                serviceClientConstructors.add(new Constructor(codeModel.ServiceClientCredentialsParameter.Value));
//                serviceClientConstructors.add(new Constructor(codeModel.ServiceClientCredentialsParameter.Value, codeModel.AzureEnvironmentParameter.Value));
//            }
//            else
//            {
//                serviceClientConstructors.add(new Constructor());
//                serviceClientConstructors.add(new Constructor(codeModel.AzureEnvironmentParameter.Value));
//            }
//
//            serviceClientConstructors.add(new Constructor(codeModel.HttpPipelineParameter.Value));
//            serviceClientConstructors.add(new Constructor(codeModel.HttpPipelineParameter.Value, codeModel.AzureEnvironmentParameter.Value));
//        }
//        else
//        {
        serviceClientConstructors.add(new Constructor(new ArrayList<>()));
        serviceClientConstructors.add(new Constructor(Arrays.asList(httpPipelineParameter)));
//        }

        return new ServiceClient(
                packageName,
                serviceClientClassName,
                serviceClientInterfaceName,
                serviceClientRestAPI,
                serviceClientMethodGroupClients,
                serviceClientProperties,
                serviceClientConstructors,
                serviceClientMethods,
                null,
                tokenCredentialParameter,
                httpPipelineParameter);
    }
}
