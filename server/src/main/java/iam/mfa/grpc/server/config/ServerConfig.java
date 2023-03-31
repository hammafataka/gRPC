package iam.mfa.grpc.server.config;

import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.server.interceptors.ServerAuthenticationInterceptor;
import iam.mfa.grpc.utils.SslUtils;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:04
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerConfig extends GRpcServerBuilderConfigurer {

    @Value("${grpc.trust-store-path}")
    private String trustStorePath;
    @Value("${grpc.trust-store-password}")
    private String trustStorePassword;

    @Override
    @SneakyThrows
    public void configure(ServerBuilder<?> serverBuilder) {
        final var builder = (NettyServerBuilder) serverBuilder;
        final var keyManagerFactory = SslUtils.buildNettyContextForServer(trustStorePath, trustStorePassword);
        builder.sslContext(keyManagerFactory)
                .intercept(ServerAuthenticationInterceptor.of());

    }
}
