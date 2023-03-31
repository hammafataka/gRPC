package iam.mfa.grpc.server.sevice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.lognet.springboot.grpc.GRpcService;

import iam.mfa.grpc.api.data.PersonRequest;
import iam.mfa.grpc.api.data.PersonResponse;
import iam.mfa.grpc.api.data.ReactorPersonSenderGrpc;
import iam.mfa.grpc.security.SecurityConstants;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 26.02.2023 20:36
 */
@Slf4j
@GRpcService
public class PersonService extends ReactorPersonSenderGrpc.PersonSenderImplBase {
    private static final String BLOCKED_ID_SUFFIX = "BL-000";

    private final Map<String, PersonRequest> registeredPeople = new ConcurrentHashMap<>();
    private final LocalDateTime registrationDate = LocalDateTime.of(2023, 3, 1, 0, 0);

    @Override
    public Mono<PersonResponse> sendPerson(final Mono<PersonRequest> request) {
        final var clientId = SecurityConstants.CLIENT_ID_CONTEXT_KEY.get(Context.current());
        return request.doOnNext(personRequest -> log.trace("Received person request [{}] from client [{}]", personRequest, clientId))
                .map(personRequest -> {
                    final var personId = personRequest.getId();
                    final var email = personRequest.getEmail();
                    if (personId.contains(BLOCKED_ID_SUFFIX)) {
                        throw new StatusRuntimeException(Status.PERMISSION_DENIED.withDescription("Person is in blocked list"));
                    }
                    if (LocalDateTime.now().isAfter(registrationDate)) {
                        throw new StatusRuntimeException(Status.DEADLINE_EXCEEDED.withDescription("registration date is already expired"));
                    }
                    final var existingPerson = registeredPeople.get(personId);
                    if (Objects.nonNull(existingPerson)) {
                        throw new StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("person is already registered with id:" + personId));
                    }
                    if (!email.endsWith("@utb.cz")) {
                        throw new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription("person is not from utb"));
                    }
                    registeredPeople.putIfAbsent(personId, personRequest);
                    return PersonResponse.newBuilder()
                            .setResult("OK")
                            .setResultMessage("Person received successfully")
                            .build();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse))
                .doOnError(error -> log.warn("error occurred, message [{}]", error.getMessage(), error));
    }
}
