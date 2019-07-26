package ru.domclick.am.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.oauth2.access-token-uri}")
    private String accessTokenUri;
    @Value("${swagger.oauth2.client-id}")
    private String clientId;
    @Value("${swagger.oauth2.client-secret}")
    private String clientSecret;

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder() //
                .displayRequestDuration(true) //
                .validatorUrl("") // Disable the validator to avoid "Error" at the bottom of the Swagger UI page
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(securitySchema()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API сервиса управления счетами клиентов")
                .contact(new Contact("Анасов Сергей", "", "sanasov@yandex.ru"))
                .build();
    }

    private OAuth securitySchema() {
        List<GrantType> grantTypes = newArrayList();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
        grantTypes.add(passwordCredentialsGrant);

        return new OAuth("oauth2", Collections.emptyList(), grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(new SecurityReference("oauth2", new AuthorizationScope[0]));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder().clientId(clientId).clientSecret(clientSecret).build();
    }
}
