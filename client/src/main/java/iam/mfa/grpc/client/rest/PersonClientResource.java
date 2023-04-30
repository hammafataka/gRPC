package iam.mfa.grpc.client.rest;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iam.mfa.grpc.api.data.GrpcPersonResponse;
import iam.mfa.grpc.client.service.PersonClientService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 27.02.2023 0:36
 */
@RestController
@RequestMapping(path = "api/v1/grpc/client/persons/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonClientResource {
    private final PersonClientService personClientService;

    @GetMapping(path = "send/people")
    public Publisher<String> sendPeople() {
        return Flux.range(0, 3)
                .flatMap(index -> personClientService.sendPerson(String.valueOf(index)))
                .map(list -> String.join(", ", list));
    }

    @GetMapping(path = "send")
    public Mono<String> sendPerson() {
        return personClientService.sendPerson("1");
    }

    @GetMapping(path = "send/with/retry")
    public Publisher<String> sendPersonWithRetry() {
        return personClientService.sendPersonWithRetry();
    }

    @GetMapping(path = "update")
    public Mono<String> updatePerson() {
        return personClientService.updatePersonalInfo()
                .map(GrpcPersonResponse::toString);
    }

    @GetMapping(path = "retrieve")
    public Mono<String> retrievePerson() {
        return personClientService.retrievePerson()
                .map(GrpcPersonResponse::toString);
    }
}
