package com.kirin.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Маршрутизатор для микросервисов. Настройки подключения в yaml файле
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Autowired
    RouteDefinitionLocator locator;

    /**
     * Настройки для группировки endpoints по микросервисам в Springdoc OpenAPI.
     * Сначала получаем все определенные маршруты для сервисов с помощью бина
     * RouteDefinitionLocator, затем идентификатор (id) каждого маршрута и устанавливаем
     * его в качестве имени группы. В результате получаем список ресурсов OpenAPI
     * по пути /v3/api-docs/{SERVICE_NAME}, например /v3/api-docs/outlet.
     * @return список групп для Springdoc OpenAPI
     */
    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        assert definitions != null;
        definitions.stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId().replaceAll("-service", "");
                    groups.add(GroupedOpenApi.builder()
                            .pathsToMatch("/" + name + "/**").group(name).build());
                });
        return groups;
    }
}
