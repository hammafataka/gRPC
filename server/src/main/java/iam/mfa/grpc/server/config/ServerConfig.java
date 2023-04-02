package iam.mfa.grpc.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.utils.SslUtils;
import io.grpc.Server;
import io.grpc.TlsServerCredentials;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:04
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerConfig {

    @Value("${grpc.security.key-store-path}")
    private String trustStorePath;
    @Value("${grpc.security.key-store-password}")
    private String trustStorePassword;

    @Bean
    public Server configure() {
        final var serverCredentials = TlsServerCredentials.newBuilder()
                .keyManager(SslUtils.buildKeyManagerFactory(trustStorePath, trustStorePassword).getKeyManagers())
                .build();
        return NettyServerBuilder.forPort(6969, serverCredentials)
                .build();

    }
}
