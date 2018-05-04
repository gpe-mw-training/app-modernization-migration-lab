package com.redhat.coolstore.cart.swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.feature.Feature;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SwaggerConfig {
	
	@Value("${cxf.path}")
	private String basePath;


  @Bean("swagger2Feature")
  public Feature swagger2Feature() {
    Swagger2Feature result = new Swagger2Feature();
    result.setTitle("Cart Service Implementation");
    result.setDescription("Cart Service Implementation in Spring boot");
    result.setBasePath(this.basePath);
    result.setVersion("v1");
    result.setPrettyPrint(true);
    return result;
  }
}