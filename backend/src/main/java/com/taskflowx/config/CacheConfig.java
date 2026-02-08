package com.taskflowx.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration // Enables Spring's annotation-driven cache management
public class CacheConfig {

  @Value("${cache.expiry.minutes}")
  private int cacheExpiryMinutes;

  @Value("${cache.max.size}")
  private int cacheMaxSize;

  // Cache configuration: expire after 60 minutes, max 1000 entries
  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("tasks", "tasksList");

    cacheManager.setCaffeine(Caffeine.newBuilder()
      .expireAfterWrite(cacheExpiryMinutes, TimeUnit.MINUTES)
      .recordStats() // Enable cache statistics for monitoring
    );

    return cacheManager;
  }
}
