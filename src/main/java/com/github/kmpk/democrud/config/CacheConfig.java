package com.github.kmpk.democrud.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {
    public static final String BOOK_CACHE = "book";
    public static final String BOOKS_CACHE = "books";
}
