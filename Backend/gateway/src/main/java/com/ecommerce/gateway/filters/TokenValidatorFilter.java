package com.ecommerce.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.ecommerce.gateway.dto.ErrorResponseBody;
import com.ecommerce.gateway.dto.ErrorResponseException;
import com.ecommerce.gateway.dto.TokenResource;
import com.ecommerce.gateway.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class TokenValidatorFilter extends AbstractGatewayFilterFactory<TokenValidatorFilter.Config> {

    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    public TokenValidatorFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst(Constants.AUTHORIZATION_HEADER);
            if (token == null || token.isEmpty()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return webClientBuilder.build()
                    .post()
                    .uri(Constants.AUTH_SERVICE_TOKEN_VERIFICATION_ENDPOINT)
                    .header(Constants.AUTHORIZATION_HEADER, token)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            clientResponse -> clientResponse.bodyToMono(ErrorResponseException.class)
                                    .map(ErrorResponseException::new))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            clientResponse -> clientResponse.bodyToMono(ErrorResponseException.class)
                                    .map(ErrorResponseException::new))
                    .bodyToMono(TokenResource.class)
                    .flatMap(authResponse -> {
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header(Constants.HEADER_CLIENT_ID, authResponse.getId())
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(ErrorResponseException.class, error -> {
                        return handleError(error, exchange);
                    });
        };
    }

    private Mono<Void> handleError(ErrorResponseException e, ServerWebExchange exchange) {
        exchange.getResponse().setRawStatusCode(e.getStatusCode());
        ErrorResponseBody errorBody = new ErrorResponseBody(e);
        String errorMessage = convertObjectToJsonString(errorBody);
        if(errorMessage == null){
            return Mono.error(e);
        }
        byte[] bytes = errorMessage.getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    private String convertObjectToJsonString(ErrorResponseBody object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}