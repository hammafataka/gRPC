package iam.mfa.grpc.server.sevice;

import java.util.HashMap;
import java.util.Map;

import iam.mfa.grpc.api.rest.data.model.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:15
 */

@Slf4j
public class PersonService {
    private static final String BLOCKED_ID_SUFFIX = "BL-000";
    private final Map<String, String> lifeInfos = new HashMap<>();

    public Mono<RestResultResponse> savePerson(final RestPersonalInfo personalInfo) {
        return Mono.fromSupplier(() -> personalInfo)
                .doOnNext(personRequest -> log.trace("Received person request [{}]", personRequest))
                .map(personRequest -> {
                    final var personRequestId = personRequest.getId();
                    if (personRequestId.contains(BLOCKED_ID_SUFFIX)) {
                        return RestResultResponse.builder()
                                .result("BLOCKED")
                                .resultMessage("person is in blocked list")
                                .build();
                    }
                    final var key = personRequest.getName() + "-" + personRequest.getEmail();
                    lifeInfos.computeIfAbsent(key, s -> personRequest.getLifeIntro());

                    return RestResultResponse.builder()
                            .result("OK")
                            .resultMessage("interesting life intro for: " + personRequest.getLifeIntro())
                            .build();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }

    public Mono<RestResultResponse> updatePerson(final RestUpdatePersonalRequest personalRequest) {
        return Mono.fromSupplier(() -> personalRequest)
                .doOnNext(updatePersonalRequest -> log.trace("received personal update request [{}]", updatePersonalRequest))
                .map(updateRequest -> {
                    final var key = updateRequest.getName() + "-" + updateRequest.getEmail();
                    lifeInfos.put(key, updateRequest.getLifeInfo());
                    return RestResultResponse.builder()
                            .result("ok")
                            .resultMessage("updated personal info for " + updateRequest.getName())
                            .build();
                })
                .doOnSuccess(resultResponse -> log.trace("update request responded with [{}]", resultResponse));
    }

    public Mono<RestPersonInfo> retrievePerson(final RestPersonRequest request) {
        return Mono.fromSupplier(() -> request)
                .doOnNext(personRequest -> log.trace("received retrieve person request [{}]", personRequest))
                .map(personRequest -> {
                    final var key = personRequest.getName() + "-" + personRequest.getEmail();
                    final var lifeInfo = lifeInfos.getOrDefault(key, "not found");
                    return RestPersonInfo.builder()
                            .name(personRequest.getName())
                            .lifeIntro(lifeInfo)
                            .build();
                })
                .doOnSuccess(resultResponse -> log.trace("retrieve request responded with [{}]", resultResponse));
    }
}
