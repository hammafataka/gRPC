package iam.mfa.grpc.server.sevice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iam.mfa.grpc.api.rest.data.model.BasePerson;
import iam.mfa.grpc.api.rest.data.model.PersonDto;
import iam.mfa.grpc.api.rest.data.model.RestPersonRetrieveRequest;
import iam.mfa.grpc.server.jpa.model.Person;
import iam.mfa.grpc.server.sevice.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:15
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonRestService {

    private final PersonService personService;


    public Mono<PersonDto> savePerson(final BasePerson personalInfo) {
        return Mono.fromSupplier(() -> personalInfo)
                .doOnNext(personRequest -> log.trace("Received person request [{}]", personRequest))
                .map(personRequest -> {
                    final var person = Person.fromRest(personRequest);
                    return personService.savePerson(person);
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }

    public Mono<PersonDto> updatePerson(final BasePerson personalRequest) {
        return Mono.fromSupplier(() -> personalRequest)
                .doOnNext(updatePersonalRequest -> log.trace("received personal update request [{}]", updatePersonalRequest))
                .map(updateRequest -> {
                    final var person = Person.fromRest(updateRequest);
                    return personService.savePerson(person);
                })
                .doOnSuccess(resultResponse -> log.trace("update request responded with [{}]", resultResponse));
    }

    public Mono<PersonDto> retrievePerson(final RestPersonRetrieveRequest request) {
        return Mono.fromSupplier(() -> request)
                .doOnNext(personRequest -> log.trace("received retrieve person request [{}]", personRequest))
                .map(personRequest -> {
                    final var name = personRequest.getName();
                    final var email = personRequest.getEmail();
                    return personService.retrievePerson(name, email);
                })
                .doOnSuccess(resultResponse -> log.trace("retrieve request responded with [{}]", resultResponse));
    }
}
