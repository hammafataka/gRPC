package iam.mfa.grpc.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iam.mfa.grpc.client.service.GreetingClient;
import iam.mfa.grpc.client.service.PersonClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 27.02.2023 0:36
 */
@RestController
@RequestMapping(path = "api/v1/management/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ManagementResource {
    private final PersonClient personClient;
    private final GreetingClient greetingClient;

    @GetMapping(path = "send/person/{id}")
    public Flux<String> sendPerson(@PathVariable(name = "id") final String id) {
        return personClient.sendPersonWithRetry(id);
    }

    @GetMapping(path = "send/greeting/{id}")
    public Mono<String> sendGreeting(@PathVariable(name = "id") final String id) {
        return greetingClient.sendGreet(id);
    }

}
