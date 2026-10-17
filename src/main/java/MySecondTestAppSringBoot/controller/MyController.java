package MySecondTestAppSringBoot.controller;

import MySecondTestAppSringBoot.exception.UnsupportedCodeException;
import MySecondTestAppSpringBoot.exception.ValidationFailedException;
import MySecondTestAppSringBoot.model.Codes;
import MySecondTestAppSringBoot.model.ErrorCodes;
import MySecondTestAppSringBoot.model.ErrorMessages;
import MySecondTestAppSringBoot.model.Request;
import MySecondTestAppSringBoot.model.Response;
import MySecondTestAppSringBoot.service.ModifyResponseService;
import MySecondTestAppSringBoot.service.ValidationService;
import MySecondTestAppSringBoot.util.DateTimeUtil;
import MySecondTestAppSringBoot.service.ModifyRequestService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyOperationUidResponseService;
    private final ModifyResponseService modifySystemTimeResponseService;
    private final ModifyRequestService modifyRequestService;

    public MyController(
            ValidationService validationService,
            @Qualifier("ModifyOperationUidResponseService")
            ModifyResponseService modifyOperationUidResponseService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService modifySystemTimeResponseService,
            ModifyRequestService modifyRequestService
    ) {

        this.validationService = validationService;
        this.modifyOperationUidResponseService = modifyOperationUidResponseService;
        this.modifySystemTimeResponseService = modifySystemTimeResponseService;
        this.modifyRequestService = modifyRequestService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult) {

        log.info("Получен запрос: {}", request);

        Response response = new Response();
        response.setUid(request.getUid());
        response.setOperationUid(request.getOperationUid());
        response.setSystemTime(
                DateTimeUtil.getCustomFormat().format(new Date())
        );
        response.setCode(Codes.SUCCESS);
        response.setErrorCode(ErrorCodes.EMPTY);
        response.setErrorMessage(ErrorMessages.EMPTY);

        try {
            validationService.isValid(bindingResult, request.getUid());

            response = modifyOperationUidResponseService.modify(response);
            response = modifySystemTimeResponseService.modify(response);
            modifyRequestService.modify(request);

            log.info("Сформирован ответ: {}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ValidationFailedException e) {
            log.error("Ошибка валидации: {}", e.getMessage());

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);

            log.info("Сформирован ответ: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (UnsupportedCodeException e) {
            log.error("Неподдерживаемый код: {}", e.getMessage());

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);

            log.info("Сформирован ответ: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обработке запроса", e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);

            log.info("Сформирован ответ: {}", response);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}