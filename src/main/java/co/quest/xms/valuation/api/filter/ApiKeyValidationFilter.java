package co.quest.xms.valuation.api.filter;

import co.quest.xms.valuation.domain.service.ApiKeyValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static co.quest.xms.valuation.util.Constants.API_KEY_HEADER_NAME;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class ApiKeyValidationFilter extends OncePerRequestFilter {

    private final ApiKeyValidationService apiKeyValidationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER_NAME);

        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(UNAUTHORIZED.value(), "Missing API Key");
            return;
        }

        try {
            apiKeyValidationService.validateApiKey(apiKey);
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
    }
}
