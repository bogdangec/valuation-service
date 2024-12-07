package co.quest.xms.valuation.api.filter;

import co.quest.xms.valuation.application.service.ApiKeyService;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.model.ApiKey;
import co.quest.xms.valuation.infrastructure.rateLimiting.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static co.quest.xms.valuation.util.Constants.API_KEY_HEADER_NAME;
import static org.springframework.http.HttpStatus.*;

@Component
@RequiredArgsConstructor
public class ApiKeyValidationFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;
    private final RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyValue = request.getHeader(API_KEY_HEADER_NAME);

        if (isNullOrEmpty(apiKeyValue)) {
            response.sendError(BAD_REQUEST.value(), "Missing API Key");
            return;
        }

        try {
            ApiKey apiKey = apiKeyService.validateAndRetrieveApiKey(apiKeyValue);
            if (rateLimiterService.isRequestAllowed(apiKey)) {
                filterChain.doFilter(request, response);
            }
        } catch (InvalidApiKeyException e) {
            response.sendError(UNAUTHORIZED.value(), e.getMessage());
        } catch (RateLimitExceededException e) {
            response.sendError(TOO_MANY_REQUESTS.value(), e.getMessage());
        } catch (RuntimeException ex) {
            response.sendError(FORBIDDEN.value(), ex.getMessage());
        }
    }

    private static boolean isNullOrEmpty(String apiKeyValue) {
        return apiKeyValue == null || apiKeyValue.isEmpty();
    }
}
