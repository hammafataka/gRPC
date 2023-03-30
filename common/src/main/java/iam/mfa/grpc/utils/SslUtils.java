package iam.mfa.grpc.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Objects;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project base
 * @date 14.10.2022 14:04
 */
@Slf4j
@UtilityClass
public class SslUtils {
    private final String DEFAULT_ALGORITHM = "SunX509";
    private final String DEFAULT_STORE_TYPE = "JKS";


    public SslContext buildNettyContextForServer(final String trustStorePath, final String trustStorePass) {
        return buildNettyContext(DEFAULT_ALGORITHM, trustStorePath, trustStorePass, DEFAULT_STORE_TYPE, true);
    }

    public SslContext buildNettyContextForClient(final String trustStorePath, final String trustStorePass) {
        return buildNettyContext(DEFAULT_ALGORITHM, trustStorePath, trustStorePass, DEFAULT_STORE_TYPE, false);
    }

    @SneakyThrows
    public SslContext buildNettyContext(final String algorithm,
                                        final String trustStorePath,
                                        final String trustStorePass,
                                        final String storeType,
                                        final boolean isServer) {

        if (isServer) {
            final var keyManagerFactory = buildKeyManagerFactory(algorithm, trustStorePath, trustStorePass, storeType);
            return GrpcSslContexts
                    .configure(SslContextBuilder.forServer(keyManagerFactory), SslProvider.JDK)
                    .build();
        }
        final var trustManagerFactory = buildTrustManagerFactory(algorithm, trustStorePath, trustStorePass, storeType);
        return GrpcSslContexts
                .configure(SslContextBuilder.forClient().trustManager(trustManagerFactory), SslProvider.JDK)
                .build();

    }


    @SneakyThrows
    public KeyStore buildKeyStore(final String keyStoreType, final String keyStorePath, final String keyStorePassword) {
        InputStream keystoreStream = null;
        try {
            final var keyStore = KeyStore.getInstance(keyStoreType);
            keystoreStream = Files.newInputStream(Paths.get(keyStorePath));
            keyStore.load(keystoreStream, keyStorePassword.toCharArray());
            return keyStore;
        } finally {
            if (Objects.nonNull(keystoreStream)) {
                keystoreStream.close();
            }
        }
    }

    @SneakyThrows
    public KeyManagerFactory buildKeyManagerFactory(final String keyStorePath, final String keyStorePass) {
        return buildKeyManagerFactory(DEFAULT_ALGORITHM, keyStorePath, keyStorePass, DEFAULT_STORE_TYPE);
    }

    @SneakyThrows
    public KeyManagerFactory buildKeyManagerFactory(final String defaultAlgorithm, final String keyStorePath, final String keyStorePass, final String keyStoreType) {
        final var keyStore = buildKeyStore(keyStoreType, keyStorePath, keyStorePass);
        final var keyManagerFactory = KeyManagerFactory.getInstance(defaultAlgorithm);
        keyManagerFactory.init(keyStore, keyStorePass.toCharArray());
        return keyManagerFactory;

    }

    @SneakyThrows
    public TrustManagerFactory buildTrustManagerFactory(final String defaultAlgorithm, final String trustStorePath, final String trustStorePass, final String trustStoreType) {
        final var trustStore = buildKeyStore(trustStoreType, trustStorePath, trustStorePass);
        final var trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
        trustManagerFactory.init(trustStore);
        return trustManagerFactory;
    }


}
