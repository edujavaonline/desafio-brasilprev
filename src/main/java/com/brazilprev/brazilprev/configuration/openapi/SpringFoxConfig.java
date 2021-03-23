package com.brazilprev.brazilprev.configuration.openapi;

import com.brazilprev.brazilprev.exception.exceptionhandler.ApiError;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

    @Bean
    public Docket apiDocket() {
        var typeResolver = new TypeResolver();
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.brazilprev.brazilprev.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, globalGetResponseMessages())
                .globalResponses(HttpMethod.POST, globalPostResponseMessages())
                .additionalModels(typeResolver.resolve(ApiError.class))
                .apiInfo(apiInfo())
                .tags(new Tag("Cities", "Cities management"),
                      new Tag("Clients", "Clients management"),
                      new Tag("States", "States management"));
    }

    private List<Response> globalGetResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code("500")
                        .description( "Internal server error!")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(builderModelApiError())
                        .build(),
                new ResponseBuilder()
                        .code("406")
                        .description( "Resource representation is not accepted!" )
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(builderModelApiError())
               .build(),
                new ResponseBuilder()
                        .code("404")
                        .description( "Resource not found!" )
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(builderModelApiError())
                        .build()
        );
    }

    private List<Response> globalPostResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code("400")
                        .description( "Request invalid (API consumer error)!" )
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(builderModelApiError())
                        .build(),
                new ResponseBuilder()
                        .code("415")
                        .description( "Request declined because the body is in an unsupported format!" )
                        .build(),
                new ResponseBuilder()
                        .code("500")
                        .description( "Internal server error!" )
                        .build(),
                new ResponseBuilder()
                        .code("406")
                        .description( "Resource representation is not accepted!" )
                        .build()
        );
    }

    private Consumer<RepresentationBuilder> builderModelApiError() {
        return r->r.model(m->m.name("ApiError")
                .referenceModel(
                        ref->ref.key(
                                k->k.qualifiedModelName(
                                        q->q.name("ApiError")
                                                .namespace("com.brazilprev.brazilprev.exception.exceptionhandler")
                                ))));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("BRASILPREV API")
                .description("APi for client registration.")
                .version("3")
                .contact(new Contact("BRASILPREV", "https://www.brasilprev.com.br", "edubrasilprev@brasilprev.com"))
                .build();
    }

}
