package com.example.springhibernatedatajpa;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com/example/springhibernatedatajpa/entity"})
public class SpringHibernateDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHibernateDataJpaApplication.class, args);
    }

}
