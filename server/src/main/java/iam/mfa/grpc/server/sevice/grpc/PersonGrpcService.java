package iam.mfa.grpc.server.sevice.grpc;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import iam.mfa.grpc.api.data.GrpcPersonRequest;
import iam.mfa.grpc.api.data.GrpcPersonResponse;
import iam.mfa.grpc.api.data.GrpcRetrieveRequest;
import iam.mfa.grpc.api.data.ReactorPersonServiceGrpc;
import iam.mfa.grpc.server.sevice.PersonService;
import iam.mfa.grpc.server.jpa.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 25.04.2023 23:33
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonGrpcService extends ReactorPersonServiceGrpc.PersonServiceImplBase {
    private final PersonService personService;

    @Override
    public Mono<GrpcPersonResponse> savePerson(Mono<GrpcPersonRequest> request) {
        return request.doOnNext(personRequest -> log.trace("Received person request [{}]", personRequest))
                .map(personRequest -> {
                    final var person = Person.fromGrpc(personRequest);
                    return personService.savePerson(person).toGrpc();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }

    @Override
    public Mono<GrpcPersonResponse> updatePerson(Mono<GrpcPersonRequest> request) {
        return request.doOnNext(updatePersonalRequest -> log.trace("received personal update request [{}]", updatePersonalRequest))
                .map(updateRequest -> {
                    final var person = Person.fromGrpc(updateRequest);
                    return personService.savePerson(person).toGrpc();
                })
                .doOnSuccess(resultResponse -> log.trace("update request responded with [{}]", resultResponse));
    }

    @Override
    public Mono<GrpcPersonResponse> retrievePerson(Mono<GrpcRetrieveRequest> request) {
        return request.doOnNext(personRequest -> log.trace("received retrieve person request [{}]", personRequest))
                .map(personRequest -> {
                    final var name = personRequest.getName();
                    final var email = personRequest.getEmail();
                    return personService.retrievePerson(name, email).toGrpc();
                })
                .doOnSuccess(resultResponse -> log.trace("retrieve request responded with [{}]", resultResponse));
    }
}
