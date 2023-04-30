package iam.mfa.grpc.client.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.data.GrpcPersonRequest;
import iam.mfa.grpc.api.data.GrpcPersonResponse;
import iam.mfa.grpc.api.data.GrpcRetrieveRequest;
import iam.mfa.grpc.api.data.ReactorPersonServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 25.04.2023 23:33
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonClientService {
    private final ManagedChannel channel;
    private ReactorPersonServiceGrpc.ReactorPersonServiceStub stub;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        stub = ReactorPersonServiceGrpc.newReactorStub(channel);
    }

    public Mono<GrpcPersonResponse> sendPerson() {
        return stub.savePerson(GrpcPersonRequest.newBuilder()
                        .setName("hamma")
                        .setAge(23)
                        .setEmail("mfataka@utb.cz")
                        .setLifeIntro("my name is hamma, living in zlin, studying software engineering!")
                        .build())
                .doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<GrpcPersonResponse> updatePersonalInfo() {
        return stub.updatePerson(GrpcPersonRequest.newBuilder()
                        .setName("hamma")
                        .setAge(22)
                        .setEmail("m_fataka@utb.cz")
                        .setLifeIntro("my name is Mohammed aka hamma, from turkey originally, living in zlin, studying software engineering!")
                        .build()
                ).doOnNext(personResponse -> log.trace("received update response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<GrpcPersonResponse> retrievePerson() {
        return stub.retrievePerson(GrpcRetrieveRequest.newBuilder()
                        .setName("hamma")
                        .setEmail("m_fataka@utb.cz")
                        .build()
                ).doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                .timeout(Duration.ofSeconds(10));
    }
}