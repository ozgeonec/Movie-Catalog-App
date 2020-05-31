package io.springtrain.moviecatalogservice.resources;

import io.springtrain.moviecatalogservice.models.CatalogItem;
import io.springtrain.moviecatalogservice.models.Movie;
import io.springtrain.moviecatalogservice.models.Rating;
import io.springtrain.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ozgeonec
 */

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        WebClient.Builder builder = WebClient.builder();

        //get all rated movies Ids
        UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+ userId, UserRating.class);

        return ratings.getRatings().stream().map(rating -> {

            //for each movie id call movie info service and get details
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

            //put them all together
            return new CatalogItem(movie.getName(), "Desc",rating.getRating());

        }).collect(Collectors.toList());


    }

}
  /*  Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/
