package MySecondTestAppSringBoot.service;

import MySecondTestAppSringBoot.exception.UnsupportedCodeException;
import MySecondTestAppSpringBoot.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult, String uid)
            throws ValidationFailedException, UnsupportedCodeException {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            log.error("Ошибки валидации запроса: {}", errors);
            throw new ValidationFailedException(errors);
        }

        if ("123".equals(uid)) {
            log.error("Передан неподдерживаемый uid: {}", uid);
            throw new UnsupportedCodeException("Не поддерживаемый uid: " + uid);
        }
    }
}
