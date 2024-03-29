package com.azure.autorest.template;


// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See License.txt in the project root for license information.


import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.MethodGroupClient;
import com.azure.autorest.model.javamodel.JavaFile;

import java.util.HashSet;
import java.util.Set;

/**
 * Writes a MethodGroupClient to a JavaFile.
 */
public class MethodGroupTemplate implements IJavaTemplate<MethodGroupClient, JavaFile> {
    private static MethodGroupTemplate _instance = new MethodGroupTemplate();

    private MethodGroupTemplate() {
    }

    public static MethodGroupTemplate getInstance() {
        return _instance;
    }

    public final void write(MethodGroupClient methodGroupClient, JavaFile javaFile) {
        JavaSettings settings = JavaSettings.getInstance();
        Set<String> imports = new HashSet<String>();
        methodGroupClient.addImportsTo(imports, true, settings);
        javaFile.declareImport(imports);

        String parentDeclaration = !methodGroupClient.getImplementedInterfaces().isEmpty() ? String.format(" implements %1$s", String.join(", ", methodGroupClient.getImplementedInterfaces())) : "";

        javaFile.javadocComment(settings.getMaximumJavadocCommentWidth(), comment ->
        {
            comment.description(String.format("An instance of this class provides access to all the operations defined in %1$s.", methodGroupClient.getInterfaceName()));
        });
        javaFile.publicFinalClass(String.format("%1$s%2$s", methodGroupClient.getClassName(), parentDeclaration), classBlock ->
        {
            classBlock.javadocComment(String.format("The proxy service used to perform REST calls."));
            classBlock.privateMemberVariable(methodGroupClient.getProxy().getName(), "service");

            classBlock.javadocComment("The service client containing this operation class.");
            classBlock.privateMemberVariable(methodGroupClient.getServiceClientName(), "client");

            classBlock.javadocComment(comment ->
            {
                comment.description(String.format("Initializes an instance of %1$s.", methodGroupClient.getClassName()));
                comment.param("client", "the instance of the service client containing this operation class.");
            });
            classBlock.publicConstructor(String.format("%1$s(%2$s client)", methodGroupClient.getClassName(), methodGroupClient.getServiceClientName()), constructor ->
            {
                if (methodGroupClient.getProxy() != null) {
                    ClassType proxyType = (settings.isAzureOrFluent() ? ClassType.AzureProxy : ClassType.RestProxy);
                    constructor.line(String.format("this.service = %1$s.create(%2$s.class, client.getHttpPipeline());", proxyType.getName(), methodGroupClient.getProxy().getName()));
                }
                constructor.line("this.client = client;");
            });

            Templates.getProxyTemplate().write(methodGroupClient.getProxy(), classBlock);

            for (ClientMethod clientMethod : methodGroupClient.getClientMethods()) {
                Templates.getClientMethodTemplate().write(clientMethod, classBlock);
            }
        });
    }
}