package iam.mfa.grpc.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.server.sevice.PersonService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:04
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerConfig {

    @Bean
    public Server server() {
        return ServerBuilder.forPort(6969)
                .build();
    }
}
