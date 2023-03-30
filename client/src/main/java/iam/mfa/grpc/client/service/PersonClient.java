package iam.mfa.grpc.client.service;

import java.time.Duration;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.data.PersonRequest;
import iam.mfa.grpc.api.data.PersonResponse;
import iam.mfa.grpc.api.data.ReactorPersonSenderGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
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
        final var personRequest = PersonRequest.newBuilder()
                .setId(id)
                .setName("hamma")
                .setAge(23)
                .setEmail("m_fataka@utb.hz")
                .build();
        return stub.sendPerson(personRequest)
                .doOnNext(personResponse -> log.trace("received person response [{}]", personResponse))
                .map(PersonResponse::getResult)
                .onErrorResume(error -> handleError(error, id))
                .timeout(Duration.ofSeconds(10));
    }

    private Mono<String> handleError(final Throwable error, final String id) {
        if (error instanceof final StatusRuntimeException gRPCError) {
            return handleGrpcError(gRPCError, id);
        }
        final var rootCause = ExceptionUtils.getRootCause(error);
        if (rootCause instanceof final StatusRuntimeException gRPCError) {
            return handleGrpcError(gRPCError, id);
        }
        log.error("non-gRPC StatusRuntimeException error occurred, message [{}]", error.getMessage(), error);
        return Mono.error(error);
    }

    private static Mono<String> handleGrpcError(StatusRuntimeException gRPCError, String id) {
        final var errorStatus = gRPCError.getStatus();
        final var errorCode = errorStatus.getCode();
        var message = "status " + errorCode + " handling not implemented yet, message: " + errorStatus.getDescription();

        return switch (errorCode) {
            case FAILED_PRECONDITION -> {
                message = "server has failed preconditions for person id, message: " + errorStatus.getDescription();
                yield Mono.just(message);
            }
            case ALREADY_EXISTS -> {
                message = "person with id" + id + "already exists";
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
                message = "one of fields of person with id " + id + " is not valid, message: " + errorStatus.getDescription();
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
