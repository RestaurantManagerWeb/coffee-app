package com.kirin.outlet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Outlet Service", version = "1.0",
        description = "Documentation Outlet Service v1.0"),
        servers = {
                @Server(url = "/outlet", description = "Сервис для работы с заказами на предприятии")
        })
public class OutletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutletServiceApplication.class, args);
    }

}
