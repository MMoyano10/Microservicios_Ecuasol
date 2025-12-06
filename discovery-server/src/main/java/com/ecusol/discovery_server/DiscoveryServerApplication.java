package com.ecusol.discovery_server; // Asegúrate de que este paquete coincida con tu carpeta

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// ESTA ES LA LÍNEA QUE TE FALTA:
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // Esta anotación necesita el import de arriba
public class DiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}

}