package iam.mfa.grpc.server.sevice.grpc;

import iam.mfa.grpc.api.data.GreetingRequest;
import iam.mfa.grpc.api.data.GreetingResponse;
import iam.mfa.grpc.api.data.ReactorGreetingSenderGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 27.02.2023 0:53
 */
@Slf4j
@RequiredArgsConstructor
public class GreetingService extends ReactorGreetingSenderGrpc.GreetingSenderImplBase {
    private final String serverName;
    private static final String BLOCKED_ID_SUFFIX = "BL-000";

    @Override
    public Mono<GreetingResponse> sendGreeting(Mono<GreetingRequest> request) {
        return request.doOnNext(greetingRequest -> log.trace("Received greeting request [{}]", greetingRequest))
                .map(greetingRequest -> {
                    final var greetingRequestId = greetingRequest.getId();
                    if (greetingRequestId.contains(BLOCKED_ID_SUFFIX)) {
                        return GreetingResponse.newBuilder()
                                .setResult("BLOCKED")
                                .setResultMessage("greeting is in blocked list")
                                .build();
                    }
                    return GreetingResponse.newBuilder()
                            .setResult("OK")
                            .setResultMessage("greeting received successfully to server: " + serverName)
                            .build();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }
}

