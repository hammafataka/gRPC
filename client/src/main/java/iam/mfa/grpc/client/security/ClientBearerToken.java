package iam.mfa.grpc.client.security;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

import iam.mfa.grpc.security.SecurityConstants;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 30.03.2023 23:36
 */
@NoArgsConstructor(staticName = "of")
public class ClientBearerToken extends CallCredentials {

    @Override
    public void applyRequestMetadata(final RequestInfo requestInfo, final Executor appExecutor, final MetadataApplier applier) {
        appExecutor.execute(() -> {
            try {
                final var headers = new Metadata();
                headers.put(SecurityConstants.AUTHORIZATION_METADATA_KEY, String.format("%s %s", SecurityConstants.BEARER_TYPE, buildJwtToken()));
                applier.apply(headers);
            } catch (Throwable e) {
                applier.fail(Status.UNAUTHENTICATED.withDescription("failed to apply request metadata").withCause(e));
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }

    private String buildJwtToken() {
        final var secretKey = Keys.hmacShaKeyFor(SecurityConstants.JWT_SIGNING_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setId("client_1")
                .signWith(secretKey)
                .compact();
    }
}

