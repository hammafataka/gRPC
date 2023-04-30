package iam.mfa.grpc.server.sevice;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import iam.mfa.grpc.api.rest.data.model.PersonDto;
import iam.mfa.grpc.server.jpa.model.Person;
import iam.mfa.grpc.server.jpa.repository.PersonRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:42
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PersonService {
    private final PersonRepository personRepository;
    private static final String BLOCKED_ID_SUFFIX = "BL-000";
    private final LocalDateTime registrationDate = LocalDateTime.of(2023, 6, 1, 0, 0);

    public PersonDto savePerson(final Person person) {
        Preconditions.checkNotNull(person);

        final var name = person.getName();
        final var email = person.getEmail();
        final var exists = personRepository.personExistsByNameAndEmail(name, email);
        if (name.contains(BLOCKED_ID_SUFFIX)) {
            throw new StatusRuntimeException(Status.PERMISSION_DENIED.withDescription("Person is in blocked list"));
        }
        if (LocalDateTime.now().isAfter(registrationDate)) {
            throw new StatusRuntimeException(Status.DEADLINE_EXCEEDED.withDescription("registration date is already expired"));
        }
        if (exists) {
            throw new StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("person is already registered with name:" + name));
        }
        if (!email.endsWith("@utb.cz")) {
            throw new StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription("person is not from utb"));
        }
        return personRepository.savePerson(person).asDto();
    }

    public PersonDto retrievePerson(final String name, final String email) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(email);
        return personRepository.findPersonByNameAndEmail(name, email).asDto();
    }
}
