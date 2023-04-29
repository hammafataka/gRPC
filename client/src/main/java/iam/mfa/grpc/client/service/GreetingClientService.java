package iam.mfa.grpc.client.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.data.GreetingRequest;
import iam.mfa.grpc.api.data.GreetingResponse;
import iam.mfa.grpc.api.data.ReactorGreetingSenderGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 6:08
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GreetingClientService {
    private final ManagedChannel channel;
    private ReactorGreetingSenderGrpc.ReactorGreetingSenderStub stub;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        stub = ReactorGreetingSenderGrpc.newReactorStub(channel);
    }

    public Mono<GreetingResponse> sendGreeting() {
        return stub.sendGreeting(GreetingRequest.newBuilder()
                        .setGreeterName("hamma")
                        .build())
                .doOnNext(personResponse -> log.trace("received greeting response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }

    public Flux<GreetingResponse> serverStream() {
        return stub.lotsOfResponses(GreetingRequest.newBuilder()
                        .setGreeterName("hamma")
                        .build()
                ).doOnNext(personResponse -> log.trace("received greeting response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<GreetingResponse> clientStream() {
        final var firstPerson = GreetingRequest.newBuilder()
                .setGreeterName("hamma")
                .build();

        final var secondPerson = GreetingRequest.newBuilder()
                .setGreeterName("eliza")
                .build();

        final var publisher = Flux.just(firstPerson, secondPerson);
        return stub.lotsOfGreetings(publisher)
                .doOnNext(personResponse -> log.trace("received greeting response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }

    public Flux<GreetingResponse> bidiGreeting() {
        final var firstPerson = GreetingRequest.newBuilder()
                .setGreeterName("hamma")
                .build();

        final var secondPerson = GreetingRequest.newBuilder()
                .setGreeterName("eliza")
                .build();

        final var publisher = Flux.just(firstPerson, secondPerson);
        return stub.bidiGreeting(publisher)
                .doOnNext(personResponse -> log.trace("received greeting response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }


}
