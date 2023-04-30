package iam.mfa.grpc.server.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import iam.mfa.grpc.api.rest.data.model.PersonDto;
import iam.mfa.grpc.server.jpa.model.Person;
import iam.mfa.grpc.server.jpa.repository.PersonRepository;
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

    public PersonDto savePerson(final Person person) {
        Preconditions.checkNotNull(person);
        return personRepository.savePerson(person).asDto();
    }

    public PersonDto retrievePerson(final String name, final String email) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(email);
        return personRepository.findPersonByNameAndEmail(name, email).asDto();
    }
}
