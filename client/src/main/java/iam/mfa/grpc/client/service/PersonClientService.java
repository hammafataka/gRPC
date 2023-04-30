package iam.mfa.grpc.client.service;

import java.time.Duration;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.data.GrpcPersonRequest;
import iam.mfa.grpc.api.data.GrpcPersonResponse;
import iam.mfa.grpc.api.data.GrpcRetrieveRequest;
import iam.mfa.grpc.api.data.ReactorPersonServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

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

    public Mono<String> sendPerson() {
        final var request = GrpcPersonRequest.newBuilder()
                .setName("hamma")
                .setAge(23)
                .setEmail("mfataka@utb.cz")
                .setLifeIntro("my name is hamma, living in zlin, studying software engineering!")
                .build();
        return stub.savePerson(request)
                .doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                .map(GrpcPersonResponse::toString)
                .onErrorResume(error -> handleError(error, request.getName()))
                .timeout(Duration.ofSeconds(10));
    }

    public Flux<String> sendPersonWithRetry() {
        final var personRequest = GrpcPersonRequest.newBuilder()
                .setName("hamma")
                .setAge(23)
                .setEmail("m_fataka@utb.hz")
                .build();

        return Flux.defer(() -> stub.savePerson(personRequest)
                        .doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                        .map(GrpcPersonResponse::toString)
                        .timeout(Duration.ofSeconds(10)))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .onErrorResume(error -> handleError(error, personRequest.getName()));
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


    private Mono<String> handleError(final Throwable error, final String name) {
        if (error instanceof final StatusRuntimeException gRPCError) {
            return handleGrpcError(gRPCError, name);
        }
        final var rootCause = ExceptionUtils.getRootCause(error);
        if (rootCause instanceof final StatusRuntimeException gRPCError) {
            return handleGrpcError(gRPCError, name);
        }
        log.error("non-gRPC StatusRuntimeException error occurred, message [{}]", error.getMessage(), error);
        return Mono.error(error);
    }

    private static Mono<String> handleGrpcError(StatusRuntimeException gRPCError, String name) {
        final var errorStatus = gRPCError.getStatus();
        final var errorCode = errorStatus.getCode();
        var message = "status " + errorCode + " handling not implemented yet, message: " + errorStatus.getDescription();

        return switch (errorCode) {
            case FAILED_PRECONDITION -> {
                message = "server has failed preconditions for person name, message: " + errorStatus.getDescription();
                yield Mono.just(message);
            }
            case ALREADY_EXISTS -> {
                message = "person with name" + name + "already exists";
                yield Mono.just(message);
            }
            case CANCELLED -> {
                message = "call was not sent to server, it was canceled on client call, message: " + errorStatus.getDescription();
                yield Mono.just(message);
            }
            case DEADLINE_EXCEEDED -> {
                message = "call exceeded expected time call, message: " + errorStatus.getDescription();
                yield Mono.just(message);
            }
            case PERMISSION_DENIED -> {
                message = "one of fields of person with name " + name + " is not valid, message: " + errorStatus.getDescription();
                yield Mono.just(message);
            }
            default -> {
                log.trace("error from server received, {}", message);
                log.error("unrecoverable error received, messgae [{}]", gRPCError.getMessage(), gRPCError);
                yield Mono.error(gRPCError);
            }

        };

    }
}
