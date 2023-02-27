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
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 27.02.2023 0:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GreetingClient {
    private final ManagedChannel channel;
    private ReactorGreetingSenderGrpc.ReactorGreetingSenderStub stub;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        stub = ReactorGreetingSenderGrpc.newReactorStub(channel);
    }

    public Mono<String> sendGreet(String id) {
        return stub.sendGreeting(GreetingRequest.newBuilder()
                        .setId(id)
                        .setName("hamma")
                        .setAge(23)
                        .setEmail("m_fataka@utb.cz")
                        .build())
                .doOnNext(personResponse -> log.trace("received greeting response [{}]", personResponse))
                .map(GreetingResponse::getResult)
                .timeout(Duration.ofSeconds(10));
    }
}
