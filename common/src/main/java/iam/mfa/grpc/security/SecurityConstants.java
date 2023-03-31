package iam.mfa.grpc.security;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import io.grpc.Context;
import io.grpc.Metadata;
import lombok.experimental.UtilityClass;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 30.03.2023 22:41
 */
@UtilityClass
public class SecurityConstants {
    public static final String JWT_SIGNING_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiY2xpZW50IiwiSXNzdWVyIjoiaGFtbWEiLCJVc2VybmFtZSI6ImNsaWVudF8xIiwiZXhwIjoxNjgwMjEwNzMwLCJpYXQiOjE2ODAyMTA3MzB9.jHChEFhC90rhf2HYpuk_KqcdH5wIwNptBn7mBON_ZKg";
    public static final String BEARER_TYPE = "Bearer";

    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

}
