package com.example.proprietary.customAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


//@ProprietaryServlet(name="cart", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="one", value="1") }, mapping = {"/cart/*"})
@Target(ElementType.TYPE)
public @interface ProprietaryServlet {
	String name() default "";
	String runAs() default "admin";
	String[] mapping() default "/api/"; 
	
	ProprietaryInitParam[] initParams() ;  //  { @ProprietaryInitParam (name="one", value="1") }
}



