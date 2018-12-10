package nsl.sam.example;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnvironmentVariablesSupplier implements Supplier<Map<String, String>> {

    @Override
    public Map<String, String> get() {
        return Stream.of(new Object[][]{
                {"SMS_ENV_USER_1", "environment-demo-user:{noop}environment-demo-password USER"},
                {"SMS_ENV_TOKEN_1", "TOKEN_READ_FROM_ENVIRONMENT environment-demo-user USER"}
        }).collect(Collectors.toMap(row -> (String) row[0], row->(String) row[1]));
    }

}
