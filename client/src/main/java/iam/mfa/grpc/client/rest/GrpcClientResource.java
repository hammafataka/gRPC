package iam.mfa.grpc.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iam.mfa.grpc.api.data.PersonInfo;
import iam.mfa.grpc.api.data.ResultResponse;
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
@RequestMapping(path = "api/v1/grpc/client")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GrpcClientResource {
    private final PersonClientService personClientService;

    @GetMapping(path = "send/many/person/{id}")
    public Flux<String> sendManyPerson(@PathVariable(name = "id") final String id) {
        return Flux.range(0, 3)
                .flatMap(index -> personClientService.sendPerson(id))
                .map(ResultResponse::getResult)
                .map(list -> String.join(", ", list));
    }

    @GetMapping(path = "send/person/{id}")
    public Mono<ResultResponse> sendPerson(@PathVariable(name = "id") final String id) {
        return personClientService.sendPerson(id);
    }

    @GetMapping(path = "update/personal")
    public Mono<ResultResponse> updatePersonal() {
        return personClientService.updatePersonalInfo();
    }

    @GetMapping(path = "get/lifeInfo")
    public Mono<PersonInfo> retrievePerson() {
        return personClientService.retrievePerson();
    }
}
