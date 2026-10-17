package MySecondTestAppSringBoot.service;

import MySecondTestAppSringBoot.model.Response;
import MySecondTestAppSringBoot.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService
        implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        response.setSystemTime(
                DateTimeUtil.getCustomFormat().format(new Date())
        );
        log.info("Изменен systemTime в ответе: {}", response.getSystemTime());
        return response;
    }
}
