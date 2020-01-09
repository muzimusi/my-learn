package me.arjenlee.shirolearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("me.arjenlee.shirolearn.dao")
public class ShiroLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroLearnApplication.class, args);
    }

}
