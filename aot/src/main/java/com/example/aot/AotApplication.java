package com.example.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.ReflectiveScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.Charset;

@SpringBootApplication
public class AotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AotApplication.class, args);
    }


}

class SerializableBeanFactoryInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

    private final MemberCategory[] values = MemberCategory.values();

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
        return (generationContext, _) -> {
            var hints = generationContext.getRuntimeHints();
            for (var beanName : beanFactory.getBeanDefinitionNames()) {
                var type = beanFactory.getType(beanName);
                if (Serializable.class.isAssignableFrom(type)) {
                    System.out.println("registering serializable type " + type);
                    hints.reflection().registerType(type, values);
                }
            }
        };
    }
}


class MessageRunner implements ApplicationRunner {

    private final Resource resource;

    MessageRunner(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var contents = this.resource.getContentAsString(Charset.defaultCharset());
        System.out.println("contents [" + contents + "]");
    }
}


class Foo {

    int x = 0;

    void bar() {
        System.out.println("invoking bar");
    }
}

@Component
class ReflectiveThingy {

    ReflectiveThingy() {

        var fooClass = Foo.class;
        for (var field : fooClass.getDeclaredFields())
            System.out.println("field [" + field + "]");

        for (var method : fooClass.getDeclaredMethods())
            System.out.println("method [" + method + "]");
    }
}

@Service
class ShoppingCart implements Serializable {
}

record Customer(int id, String name) {
}

@ReflectiveScan
@Configuration
@ImportRuntimeHints(AotConfiguration.Hints.class)
//@RegisterReflectionForBinding(ReflectiveThingy.class)
class AotConfiguration {


    private static final Resource RESOURCE = new ClassPathResource("message");


    static class Hints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(Foo.class, MemberCategory.values());
            hints.resources().registerResource(RESOURCE);
        }
    }

    @Bean
    static SerializableBeanFactoryInitializationAotProcessor myBeanFactoryInitializationAotProcessor() {
        return new SerializableBeanFactoryInitializationAotProcessor();
    }

    @Bean
    MessageRunner messageRunner() {
        return new MessageRunner(RESOURCE);
    }

}

@ShellComponent
class MyShell {

    private boolean connected;

    @ShellMethod("Connect to the server.")
    public void connect(String user, String password) {
        connected = true;
        System.out.println("connected " + user + ':' + password + '.');
    }

    @ShellMethod("Download the nuclear codes.")
    public void download() {
        System.out.println("downloading..");
    }

    public Availability downloadAvailability() {
        return connected
                ? Availability.available()
                : Availability.unavailable("you are not connected");
    }
}