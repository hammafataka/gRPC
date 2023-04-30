package iam.mfa.grpc.server.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import iam.mfa.grpc.server.sevice.PersonService;
import iam.mfa.grpc.server.sevice.grpc.PersonGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 16:04
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerConfig {
    private final PersonService personService;

    @PostConstruct
    public List<Server> servers() {
        final var servers = new ArrayList<Server>();
        final var numOfServers = 3;
        final var executorService = Executors.newFixedThreadPool(numOfServers);
        IntStream.range(0, 3)
                .forEach(index -> {
                    final var port = 6960 + index;
                    final var serverName = "Server_" + port;
                    executorService.submit(() -> {
                        final var server = ServerBuilder
                                .forPort(port)
                                .addService(new PersonGrpcService(serverName, personService))
                                .build();
                        try {
                            server.start();
                            log.trace("{} server started, listening on port: {}", serverName, server.getPort());
                            server.awaitTermination();
                            servers.add(server);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        }
                    });

                });
        return servers;
    }
}
