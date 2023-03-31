package iam.mfa.grpc.server.interceptors;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import iam.mfa.grpc.security.SecurityConstants;
import io.grpc.*;
import io.grpc.internal.NoopServerCall;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 30.03.2023 22:42
 */
@Slf4j
@NoArgsConstructor(staticName = "of")
public class ServerAuthenticationInterceptor implements ServerInterceptor {
    private final JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(SecurityConstants.JWT_SIGNING_KEY.getBytes(StandardCharsets.UTF_8))
            .build();


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> call, Metadata headers, final ServerCallHandler<ReqT, RespT> next) {
        final var key = headers.get(SecurityConstants.AUTHORIZATION_METADATA_KEY);
        final var nonValidKey = nonValidKey(key);

        if (Objects.nonNull(nonValidKey)) {
            call.close(nonValidKey, headers);
            return new NoopServerCall.NoopServerCallListener<>();
        }
        try {
            final var token = key.substring(SecurityConstants.BEARER_TYPE.length()).trim();
            final var claims = jwtParser.parseClaimsJws(token);
            final var context = Context.current().withValue(SecurityConstants.CLIENT_ID_CONTEXT_KEY, claims.getBody().getId());
            return Contexts.interceptCall(context, call, headers, next);
        } catch (Exception e) {
            call.close(Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e), headers);
            return new NoopServerCall.NoopServerCallListener<>();
        }
    }

    private Status nonValidKey(final String key) {
        if (key == null) {
            return Status.UNAUTHENTICATED.withDescription("Missing authorization token");
        }
        if (!key.startsWith(SecurityConstants.BEARER_TYPE)) {
            return Status.UNAUTHENTICATED.withDescription("Authorization type is unknown");
        }
        return null;
    }
}
