package com.willhamill;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Optional;

public class HelloWorldMicroservice implements RequestHandler<String, HelloResponse> {

    private Item dynamoDbConfigItem;

    public HelloResponse handleRequest(String name, Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        if (dynamoDbConfigItem == null) {
            dynamoDbConfigItem = contextWrapper.getConfigItem();
        }
        String responseLanguage = dynamoDbConfigItem.getString("responseLanguage");

        String envVersion = Optional.ofNullable(context.getFunctionVersion()).orElse("no-alias");
        String functionName = Optional.ofNullable(context.getFunctionName()).orElse("default-name");
        String arnName = Optional.ofNullable(context.getInvokedFunctionArn()).orElse("default-arn");

        String response = "Hello " + name + " from version: " + envVersion + " of " + functionName + " with arn: " + arnName;

        return new HelloResponse(responseLanguage, response);
    }

}
