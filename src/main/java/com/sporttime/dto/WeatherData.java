package com.sporttime.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherData {

	@JsonProperty("dt_txt")
	private String fechaTexto;

	@JsonProperty("main")
	private Main main;

	@JsonProperty("wind")
	private Wind wind;

	@JsonProperty("weather")
	private List<Weather> weather;


	@Data
	public static class Main {
		private Double temp;
		@JsonProperty("temp_max")
		private Double tempMax;
		@JsonProperty("temp_min")
		private Double tempMin;
		private Integer humidity;
	}
	
	@Data
    public static class Wind {
        private Double speed;
    }
	
	@Data
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }
}
