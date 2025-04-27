package malla.dipesh.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {


        @JsonProperty("current")
        private Current current;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class Current {
            @JsonProperty("observation_time")
            private String observationTime;

            @JsonProperty("temperature")
            private int temperature;

            @JsonProperty("weather_code")
            private int weatherCode;

            @JsonProperty("weather_icons")
            private List<String> weatherIcons;

            @JsonProperty("weather_descriptions")
            private List<String> weatherDescriptions;

            @JsonProperty("feelslike")
            private int feelslike;

        }

}
