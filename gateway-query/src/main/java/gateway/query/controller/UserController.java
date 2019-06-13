package gateway.adaptors.web;

import gateway.adaptors.clients.UserClient;
import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.User;
import gateway.adaptors.models.UserModel;
import gateway.adaptors.models.implementation.SortingAndOrderArguments;
import gateway.adaptors.models.implementation.UserSaveCommand;
import gateway.adaptors.models.implementation.UserUpdateCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Controller("/user")
public class UserController {

    private final UserClient userClient;
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @Get(uri="/list{?args*}" , consumes = MediaType.APPLICATION_JSON)
    public Optional<UserModel> findAll(SortingAndOrderArguments args) {
        return userClient.findAll(args);
    }

    @Get("/{id}")
    public Optional<User> findById(@NotNull Long id) {
        return userClient.findById(id);
    }

    @Delete("/{id}")
    public HttpResponse delete(Long id) {
        return userClient.delete(id);
    }

    @Put(uri = "/update/{id}", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse update(Long id, @Body UserUpdateCommand args) {
        return userClient.update(id,args);
    }

    @Post(uri = "/", consumes = MediaType.APPLICATION_JSON)
    public Hotel save(@Body UserSaveCommand args) {
        return userClient.save(args);
    }

}
