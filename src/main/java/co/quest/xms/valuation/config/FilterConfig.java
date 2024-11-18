package co.quest.xms.valuation.config;


import co.quest.xms.valuation.api.filter.ApiKeyValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyValidationFilter> apiKeyFilter(ApiKeyValidationFilter filter) {
        FilterRegistrationBean<ApiKeyValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/v1/*"); // Apply to API endpoints only
        return registrationBean;
    }
}
