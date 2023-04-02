package iam.mfa.grpc.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.utils.SslUtils;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import lombok.SneakyThrows;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:03
 */
@Configuration
public class ClientConfig {
    @Value("${grpc.trust-store-path}")
    private String trustStorePath;
    @Value("${grpc.trust-store-password}")
    private String trustStorePassword;

    @Bean
    @SneakyThrows
    public ManagedChannel channel() {
        final var trustManagerFactory = SslUtils.buildTrustManagerFactory(trustStorePath, trustStorePassword);
        return NettyChannelBuilder.forTarget("127.0.0.1:6969")
                .sslContext(GrpcSslContexts
                        .configure(SslContextBuilder.forClient().trustManager(trustManagerFactory), SslProvider.JDK)
                        .build()
                )
                .usePlaintext()
                .build();
    }
}
