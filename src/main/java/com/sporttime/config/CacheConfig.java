package com.sporttime.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching // Habilita el soporte para caché en la aplicación
public class CacheConfig {

	@Bean
    public CacheManager cacheManager() {
		
		// Crea un administrador de caché usando Caffeine
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
     // Configura el comportamiento de la caché
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // Expira después de 1 hora de escritura
                .maximumSize(500)					 // Límite de 500 entradas en caché
                .recordStats());                     // Activa estadísticas de uso
        return cacheManager;
    }
}
