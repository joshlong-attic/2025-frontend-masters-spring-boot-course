package com.example.web;

import com.example.web.grpc.Users;
import com.example.web.grpc.UsersServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Collection;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}

@Controller
class MvcController {

    private final UsersClient client;

    MvcController(UsersClient client) {
        this.client = client;
    }

    @GetMapping("/users.html")
    String all(Model model) {
        model.addAttribute("users", client.all());
        return "users";
    }
}

//@Controller
//@ResponseBody
@RestController
class HttpJsonController {

    private final UsersClient client;

    HttpJsonController(UsersClient client) {
        this.client = client;
    }

    @GetMapping("/users")
    Collection<User> all() {
        return this.client.all();
    }
}


@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
@RequestMapping("/hateoas")
@RestController
class HateoasController {

    private final UserModelAssembler assembler;

    private final UsersClient client;

    HateoasController(UsersClient uc, UserModelAssembler assembler) {
        this.assembler = assembler;
        this.client = uc;
    }

    @GetMapping("/users/{id}")
    EntityModel<User> one(@PathVariable int id) {
        // todo lol
        return null;
    }

    @GetMapping("/users")
    CollectionModel<EntityModel<User>> all() {
        return assembler.toCollectionModel(this.client.all());
    }
}

@Component
class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User entity) {
        var controller = HateoasController.class;
        var self = linkTo(methodOn(controller).one(entity.id())).withSelfRel();
        var all = linkTo(methodOn(controller).all()).withRel("users");
        return EntityModel.of(entity, self, all);
    }
}


@Controller
class GraphqlController {

    private final UsersClient client;

    GraphqlController(UsersClient client) {
        this.client = client;
    }

    @QueryMapping
    Collection<User> users() {
        return this.client.all();
    }
}

//  grpcurl --plaintext localhost:8080 UsersService.All
@Service
class UsersGrpcService extends UsersServiceGrpc.UsersServiceImplBase {

    private final UsersClient client;

    UsersGrpcService(UsersClient client) {
        this.client = client;
    }

    @Override
    public void all(Empty request, StreamObserver<Users> responseObserver) {

        var all = this.client.all().stream()
                .map(u -> com.example.web.grpc.User.newBuilder()
                        .setId(u.id())
                        .setName(u.name())
                        .setEmail(u.email())
                        .setUsername(u.username())
                        .build())
                .toList();
        responseObserver.onNext(Users.newBuilder().addAllUsers(all).build());
        responseObserver.onCompleted();
    }
}


@Configuration
class UserClientsConfiguration {

    @Bean
    ApplicationRunner pokemonClientRunner(Map<String, UsersClient> clients) {
        return _ -> clients
                .forEach((k, v) -> v.all().forEach(System.out::println));
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();
    }

    @Bean
    HttpServiceProxyFactory httpServiceProxyFactory(RestClient http) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(RestClientAdapter.create(http))
                .build();
    }

//    @Bean
//    UsersClient usersClient(HttpServiceProxyFactory build) {
//        return build.createClient(UsersClient.class);
//    }

    @Bean
    DefaultUsersClient simpleUsersClient(RestClient restClient) {
        return new DefaultUsersClient(restClient);
    }
}


interface UsersClient {

    @GetExchange("/users")
    Collection<User> all();

}

class DefaultUsersClient implements UsersClient {

    private final RestClient client;

    DefaultUsersClient(RestClient client) {
        this.client = client;
    }

    @Override
    public Collection<User> all() {
        return this.client
                .get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}

record User(int id, String name, String username, String email, Address address) {
}

record Address(String street, String suite, String city, String zipcode, Geo geo) {
}

record Geo(float lat, float lng) {
}