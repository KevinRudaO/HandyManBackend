package co.com.ias.handyman.infraestructureServiceReport.commons;

import co.com.ias.handyman.applicationServiceReport.commons.error.ApplicationError;
import co.com.ias.handyman.applicationServiceReport.commons.operations.ApplicationRequest;
import co.com.ias.handyman.applicationServiceReport.commons.operations.ApplicationResponse;
import co.com.ias.handyman.applicationServiceReport.commons.operations.ApplicationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class UseCaseHttpExecutor {
    public <IN extends ApplicationRequest, OUT extends ApplicationResponse> ResponseEntity executeUseCase(
            ApplicationUseCase<IN, OUT> useCase,
            IN applicationRequest
    ) {
        try {
            OUT response = useCase.execute(applicationRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } catch (ApplicationError applicationError) {
            int httpStatusCode = applicationError.httpStatusCode().getCode();
            Map<String, Object> body = Map.of(
                    "message", applicationError.getMessage(),
                    "errorCode", applicationError.errorCode(),
                    "metadata", applicationError.metadata()
            );
            return ResponseEntity.status(httpStatusCode)
                    .body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
