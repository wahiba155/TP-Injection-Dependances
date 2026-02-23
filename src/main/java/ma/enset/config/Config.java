package ma.enset.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ma.enset.dao", "ma.enset.metier"})
public class Config {
}