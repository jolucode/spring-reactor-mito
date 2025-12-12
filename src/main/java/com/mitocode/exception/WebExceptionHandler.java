package com.mitocode.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param applicationContext the application context
     */
    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest req) {
        Map<String, Object> generalError = getErrorAttributes(req, ErrorAttributeOptions.defaults());

        Map<String, Object> customError = new HashMap<>();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String statusCode = String.valueOf(generalError.get("status"));
        Throwable error = getError(req);

        switch (statusCode) {
            case "400":
                status = HttpStatus.BAD_REQUEST;
                customError.put("message", "Bad Request: " + error.getMessage());
                customError.put("status",status);
                break;
            case "404":
                status = HttpStatus.NOT_FOUND;
                customError.put("message", "Not Found: " + error.getMessage());
                break;
            case "500":
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                customError.put("message", "Internal Server Error: " + error.getMessage());
                break;
            default:
                customError.put("message", "Unknown Error: " + error.getMessage());

                break;

        }

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
    }
}










