package iam.mfa.grpc.server.sevice;

import org.lognet.springboot.grpc.GRpcService;

import iam.mfa.grpc.api.data.PersonRequest;
import iam.mfa.grpc.api.data.PersonResponse;
import iam.mfa.grpc.api.data.ReactorPersonSenderGrpc;
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


    @Override
    public Mono<PersonResponse> sendPerson(final Mono<PersonRequest> request) {
        return request.doOnNext(personRequest -> log.trace("Received person request [{}]", personRequest))
                .map(personRequest -> {
                    final var personId = personRequest.getId();
                    if (personId.contains(BLOCKED_ID_SUFFIX)) {
                        return PersonResponse.newBuilder()
                                .setResult("BLOCKED")
                                .setResultMessage("Person is in blocked list")
                                .build();
                    }
                    return PersonResponse.newBuilder()
                            .setResult("OK")
                            .setResultMessage("Person received successfully")
                            .build();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }
}
