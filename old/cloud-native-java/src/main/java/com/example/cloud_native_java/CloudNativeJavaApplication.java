package com.example.cloud_native_java;

import greetings.GreetingsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CloudNativeJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudNativeJavaApplication.class, args);
    }
    
  
    @Bean
    @ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
    GreetingsController kubernetesGreetingsController() {
        return new GreetingsController( "Kubernetes");
    }

    @Bean
    @ConditionalOnMissingBean
    GreetingsController defaultGreetingsController (){
        return new GreetingsController( "Cloud Native Java");
    }


}



