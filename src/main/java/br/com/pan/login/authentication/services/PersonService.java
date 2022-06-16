package br.com.pan.login.authentication.services;

import br.com.pan.login.authentication.domain.*;
import br.com.pan.login.authentication.exceptions.BadRequestException;
import br.com.pan.login.authentication.exceptions.NotFoundException;
import br.com.pan.login.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class PersonService {

    private static final String CPF_PATTERN = "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})";
    private static final String CNPJ_PATTERN = "([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})";
    private final UserRepository userRepository;

    public PersonService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static boolean isLegalPerson(String identity) {
        return identity.matches(CNPJ_PATTERN);
    }

    private static boolean isRegularPerson(String identity) {
        return identity.matches(CPF_PATTERN);
    }

    public Person getPerson(Intent intent, String clientId) throws NotFoundException {
        if (intent.personType().equals(PersonType.REGULAR)) {
            return getRegularPerson(clientId);
        }

        return getLegalPerson(clientId);
    }

    public PersonType getPersonType(String id) throws BadRequestException {
        if (isRegularPerson(id) && userRepository.personExists(id)) {
            return PersonType.REGULAR;
        } else if (isLegalPerson(id) && userRepository.legalPersonExists(id)) {
            return PersonType.LEGAL;
        }

        throw new BadRequestException("Tipo de pessoa inexistente");
    }

    private RegularPerson getRegularPerson(String cpf) throws NotFoundException {
        return userRepository.findByCpf(cpf).orElseThrow(
                () -> new NotFoundException("Usuário não existente")
        );
    }

    private LegalPerson getLegalPerson(String cnpj) throws NotFoundException {
        return userRepository.findByCnpj(cnpj).orElseThrow(
                () -> new NotFoundException("Usuário não existente")
        );
    }
}
