package iam.mfa.grpc.client.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iam.mfa.grpc.api.data.GreetingResponse;
import iam.mfa.grpc.client.service.GreetingClientService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 30.04.2023 2:32
 */
@RestController
@RequestMapping(path = "api/v1/grpc/client/greetings/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GreetingClientResource {
    private final GreetingClientService greetingClientService;

    @GetMapping(path = "unary")
    public Mono<String> unaryGreeting() {
        return greetingClientService.sendGreeting()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "client/stream")
    public Mono<String> clientStreamGreeting() {
        return greetingClientService.clientStream()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "server/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> serverStreamGreeting() {
        return greetingClientService.serverStream()
                .map(GreetingResponse::getResult);
    }

    @GetMapping(path = "bidi/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> bidiStreamGreeting() {
        return greetingClientService.bidiGreeting()
                .map(GreetingResponse::getResult);
    }
}
