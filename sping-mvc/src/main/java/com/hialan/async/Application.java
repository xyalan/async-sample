package com.hialan.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * User: Alan
 * Email: houlong.yang@shuyun.com
 * Date: 11:06 6/16/16
 */
@SpringBootApplication
@EnableAsync
public class Application {
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
