package gateway.adaptors.clients;

import gateway.adaptors.models.Hotel;
import gateway.adaptors.models.implementation.HotelSaveCommand;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client(id = "hotel-write", path = "/")
public interface HotelWriteClient {

    @Post()
    HttpResponse<Hotel> save(@Body Hotel args);

}