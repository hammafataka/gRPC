package iam.mfa.grpc.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import iam.mfa.grpc.api.rest.data.model.BasePerson;
import iam.mfa.grpc.api.rest.data.model.PersonDto;
import iam.mfa.grpc.api.rest.data.model.RestPersonRetrieveRequest;
import iam.mfa.grpc.server.sevice.rest.PersonRestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:04
 */
@RestController
@RequestMapping(path = "api/v1/persons/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonController {
    private final PersonRestService personRestService;


    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PersonDto> savePerson(@RequestBody @Valid final BasePerson request) {
        return personRestService.savePerson(request);
    }

    @PutMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PersonDto> updatePerson(@RequestBody @Valid final BasePerson request) {
        return personRestService.updatePerson(request);
    }

    @GetMapping(path = "retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PersonDto> retrievePerson(@RequestBody @Valid final RestPersonRetrieveRequest personalRequest) {
        return personRestService.retrievePerson(personalRequest);
    }
}
