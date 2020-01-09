package sql.submit.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ServletComponentScan
@ComponentScan("sql.submit.server")
@EnableSwagger2
public class FlinkSqlSubmitApplicationServer extends SpringBootServletInitializer {
    public static void main(String[] args) {
        //SpringApplication.run(FlinkSqlSubmitApplicationServer.class, args);
        new SpringApplicationBuilder(FlinkSqlSubmitApplicationServer.class)
                .properties("spring.config.name:application-sql")
                .build()
                .run(args);
    }
}
