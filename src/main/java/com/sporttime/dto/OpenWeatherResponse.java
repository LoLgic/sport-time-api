package com.sporttime.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OpenWeatherResponse {

	@JsonProperty("list")
	private List<WeatherData> data;
}
