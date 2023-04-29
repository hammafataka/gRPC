package iam.mfa.grpc.server.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import iam.mfa.grpc.api.rest.data.model.*;
import iam.mfa.grpc.server.sevice.PersonService;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:04
 */
@RestController
@RequestMapping(path = "api/v1/persons/")
public class PersonController {
    private final PersonService personService = new PersonService();


    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RestResultResponse> savePerson(@RequestBody final RestPersonalInfo personalInfo) {
        return personService.savePerson(personalInfo);
    }

    @PutMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RestResultResponse> updatePerson(@RequestBody final RestUpdatePersonalRequest personalRequest) {
        return personService.updatePerson(personalRequest);
    }

    @GetMapping(path = "retrieve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RestPersonInfo> retrievePerson(@RequestBody final RestPersonRequest personalRequest) {
        return personService.retrievePerson(personalRequest);
    }
}
