package malla.dipesh.journalApp.service;

import malla.dipesh.journalApp.api.response.WeatherResponse;
import malla.dipesh.journalApp.cache.AppCache;
import malla.dipesh.journalApp.constant.PlaceHolders;
import malla.dipesh.journalApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RedisService redisService;
    @Value("${weather.api.key}")
    private  String WEATHER_APIKEY;

    private final AppCache appCache;
    private final RestTemplate restTemplate;

    public WeatherService(AppCache appCache, RestTemplate restTemplate, RedisService redisService) {
        this.appCache = appCache;
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    public WeatherResponse getCurrentWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("Weather_of_" + city,WeatherResponse.class);
        if(weatherResponse != null){
            return weatherResponse;
        }else{
            String url = appCache.APP_CACHE.get("WEATHER_API").replace(PlaceHolders.API_KEY, WEATHER_APIKEY).replace(PlaceHolders.CITY, city);
            ResponseEntity<WeatherResponse> getResponse = restTemplate.exchange(url, HttpMethod.POST, null, WeatherResponse.class);
            WeatherResponse body = getResponse.getBody();
            if(body != null){
                redisService.set("Weather_of_" + city,body,300l );
            }
            return body;
        }
    }


    // a bit different than this one is post api
    public WeatherResponse postCurrentWeather(String city) {
        String url = appCache.APP_CACHE.get("weather_api").replace("<apiKey>", WEATHER_APIKEY).replace("<city>", city);
        // for sending to header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Key", "Value");
        //for Post
        User user = User.builder().username("dip").password("dip").build();
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, WeatherResponse.class);
        // we can handle status code here
        return response.getBody();

    }


}
