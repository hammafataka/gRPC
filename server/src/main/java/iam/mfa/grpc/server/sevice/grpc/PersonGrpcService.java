package iam.mfa.grpc.server.sevice.grpc;

import java.util.HashMap;
import java.util.Map;

import org.lognet.springboot.grpc.GRpcService;

import iam.mfa.grpc.api.data.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 25.04.2023 23:33
 */
@Slf4j
@GRpcService
public class PersonGrpcService extends ReactorPersonServiceGrpc.PersonServiceImplBase {
    private static final String BLOCKED_ID_SUFFIX = "BL-000";
    private final Map<String, String> lifeInfos = new HashMap<>();


    @Override
    public Mono<ResultResponse> savePerson(Mono<PersonalInfo> request) {
        return request.doOnNext(personRequest -> log.trace("Received person request [{}]", personRequest))
                .map(personRequest -> {
                    final var personRequestId = personRequest.getId();
                    if (personRequestId.contains(BLOCKED_ID_SUFFIX)) {
                        return ResultResponse.newBuilder()
                                .setResult("BLOCKED")
                                .setResultMessage("person is in blocked list")
                                .build();
                    }
                    final var key = personRequest.getName() + "-" + personRequest.getEmail();
                    lifeInfos.computeIfAbsent(key, s -> personRequest.getLifeIntro());

                    return ResultResponse.newBuilder()
                            .setResult("OK")
                            .setResultMessage("interesting life intro for: " + personRequest.getLifeIntro())
                            .build();
                })
                .doOnSuccess(personResponse -> log.trace("responded with [{}]", personResponse));
    }

    @Override
    public Mono<ResultResponse> updatePerson(Mono<UpdatePersonalRequest> request) {
        return request.doOnNext(updatePersonalRequest -> log.trace("received personal update request [{}]", updatePersonalRequest))
                .map(updateRequest -> {
                    final var key = updateRequest.getName() + "-" + updateRequest.getEmail();
                    lifeInfos.put(key, updateRequest.getLifeIntro());
                    return ResultResponse.newBuilder()
                            .setResult("ok")
                            .setResultMessage("updated personal info for " + updateRequest.getName())
                            .build();
                })
                .doOnSuccess(resultResponse -> log.trace("update request responded with [{}]", resultResponse));
    }

    @Override
    public Mono<PersonInfo> retrievePerson(Mono<PersonRequest> request) {
        return request.doOnNext(personRequest -> log.trace("received retrieve person request [{}]", personRequest))
                .map(personRequest -> {
                    final var key = personRequest.getName() + "-" + personRequest.getEmail();
                    final var lifeInfo = lifeInfos.getOrDefault(key, "not found");
                    return PersonInfo.newBuilder()
                            .setName(personRequest.getName())
                            .setLifeIntro(lifeInfo)
                            .build();
                })
                .doOnSuccess(resultResponse -> log.trace("retrieve request responded with [{}]", resultResponse));
    }
}
