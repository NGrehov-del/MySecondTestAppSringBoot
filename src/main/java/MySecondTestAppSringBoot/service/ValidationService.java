package MySecondTestAppSringBoot.service;

import MySecondTestAppSringBoot.exception.UnsupportedCodeException;
import MySecondTestAppSpringBoot.exception.ValidationFailedException;
import org.springframework.validation.BindingResult;

public interface ValidationService {

    void isValid(BindingResult bindingResult, String uid)
            throws ValidationFailedException, UnsupportedCodeException;
}