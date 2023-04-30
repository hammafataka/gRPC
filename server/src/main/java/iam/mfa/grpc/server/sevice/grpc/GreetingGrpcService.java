package iam.mfa.grpc.server.sevice.grpc;

import java.util.List;
import java.util.stream.Collectors;

import org.lognet.springboot.grpc.GRpcService;

import iam.mfa.grpc.api.data.GreetingRequest;
import iam.mfa.grpc.api.data.GreetingResponse;
import iam.mfa.grpc.api.data.ReactorGreetingSenderGrpc;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 6:25
 */
@Slf4j
@GRpcService
public class GreetingGrpcService extends ReactorGreetingSenderGrpc.GreetingSenderImplBase {

    @Override
    public Mono<GreetingResponse> sendGreeting(Mono<GreetingRequest> request) {
        return request.doOnNext(greetingRequest -> log.trace("greeting request received [{}]", greetingRequest))
                .map(greetingRequest -> GreetingResponse.newBuilder()
                        .setResult("hello " + greetingRequest.getGreeterName())
                        .build()
                )
                .doOnNext(greetingResponse -> log.trace("greeting responded with [{}]", greetingResponse));
    }

    @Override
    public Flux<GreetingResponse> lotsOfResponses(Mono<GreetingRequest> request) {
        return request.doOnNext(greetingRequest -> log.trace("greeting request received [{}]", greetingRequest))
                .flatMapIterable(greetingRequest -> {
                            final var greetingResponse = GreetingResponse.newBuilder()
                                    .setResult("hello " + greetingRequest.getGreeterName())
                                    .build();
                            return List.of(greetingResponse, greetingResponse, greetingResponse);
                        }
                )
                .doOnNext(greetingResponse -> log.trace("greeting responded with [{}]", greetingResponse));
    }

    @Override
    public Mono<GreetingResponse> lotsOfGreetings(Flux<GreetingRequest> request) {
        return request.doOnNext(greetingRequest -> log.trace("greeting request received [{}]", greetingRequest))
                .collectList()
                .map(list -> {
                    final var responseStream = list.stream()
                            .map(GreetingRequest::getGreeterName)
                            .collect(Collectors.joining(",", "hello ", ""));
                    return GreetingResponse.newBuilder()
                            .setResult(responseStream)
                            .build();
                });
    }

    @Override
    public Flux<GreetingResponse> bidiGreeting(Flux<GreetingRequest> request) {
        return request.doOnNext(greetingRequest -> log.trace("greeting request received [{}]", greetingRequest))
                .map(greetingRequest -> GreetingResponse.newBuilder()
                        .setResult("hello " + greetingRequest.getGreeterName())
                        .build()
                )
                .doOnNext(greetingResponse -> log.trace("greeting responded with [{}]", greetingResponse));
    }
}
