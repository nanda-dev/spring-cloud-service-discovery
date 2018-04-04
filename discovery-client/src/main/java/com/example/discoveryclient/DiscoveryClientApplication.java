package com.example.discoveryclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class DiscoveryClientApplication {

	@Autowired
	private EurekaClient client;
	
	@Autowired
	private RestTemplateBuilder builder;
	
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryClientApplication.class, args);
	}
	
	@GetMapping("/")
	public String callService() {
		
									//pass in the "service-id", and indicate whether it's a secured call or not.
		InstanceInfo instanceInfo 	= client.getNextServerFromEureka("discoveryService", false);		
		String baseUrl 				= instanceInfo.getHomePageUrl();
		
		RestTemplate template 		= builder.build();									
									//url, httpMethod, requestBody, responseType, uriVars
		ResponseEntity<String> resp = template.exchange(baseUrl, HttpMethod.GET, null, String.class);
		
		return resp.getBody();
	}
}
