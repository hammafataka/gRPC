package iam.mfa.grpc.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iam.mfa.grpc.api.data.GreetingResponse;
import iam.mfa.grpc.api.data.GrpcPersonResponse;
import iam.mfa.grpc.client.service.GreetingClientService;
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
    private final GreetingClientService greetingClientService;

    @GetMapping(path = "greeting/unary")
    public Mono<String> unaryGreeting() {
        return greetingClientService.sendGreeting()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "greeting/client/stream")
    public Mono<String> clientStreamGreeting() {
        return greetingClientService.clientStream()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "greeting/server/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> serverStreamGreeting() {
        return greetingClientService.serverStream()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "greeting/bidi/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> bidiStreamGreeting() {
        return greetingClientService.bidiGreeting()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "send/person")
    public Mono<String> sendPerson() {
        return personClientService.sendPerson();
    }

    @GetMapping(path = "update/person")
    public Mono<String> updatePerson() {
        return personClientService.updatePersonalInfo()
                .map(GrpcPersonResponse::toString);
    }

    @GetMapping(path = "retrieve/")
    public Mono<String> retrievePerson() {
        return personClientService.retrievePerson()
                .map(GrpcPersonResponse::toString);
    }
}
