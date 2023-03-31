package iam.mfa.grpc.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.utils.SslUtils;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

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
    public ManagedChannel channel() {
        return NettyChannelBuilder.forTarget("127.0.0.1:6969")
                .sslContext(SslUtils.buildNettyContextForClient(trustStorePath, trustStorePassword))
                .overrideAuthority("localhost")
                .useTransportSecurity()
                .build();
    }
}
