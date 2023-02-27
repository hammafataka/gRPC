package iam.mfa.grpc.client.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.data.PersonRequest;
import iam.mfa.grpc.api.data.PersonResponse;
import iam.mfa.grpc.api.data.ReactorPersonSenderGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 18:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonClient {
    private final ManagedChannel channel;
    private ReactorPersonSenderGrpc.ReactorPersonSenderStub stub;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        stub = ReactorPersonSenderGrpc.newReactorStub(channel);
    }

    public Mono<String> sendPerson(String id) {
        return stub.sendPerson(PersonRequest.newBuilder()
                        .setId(id)
                        .setName("hamma")
                        .setAge(23)
                        .setEmail("m_fataka@utb.cz")
                        .build())
                .doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                .map(PersonResponse::getResult)
                .timeout(Duration.ofSeconds(10));
    }
}
