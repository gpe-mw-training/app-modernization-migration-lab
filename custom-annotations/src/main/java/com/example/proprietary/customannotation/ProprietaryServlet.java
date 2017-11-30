package com.example.proprietary.customannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


//@ProprietaryServlet(name="FOO", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="one", value="1") }, mapping = {"/foo/*"})
@Target(ElementType.TYPE)
public @interface ProprietaryServlet {
	String name() default "";
	String runAs() default "admin";
	String[] mapping() default "/api/"; 
	
	ProprietaryInitParam[] initParams() ;  //  { @ProprietaryInitParam (name="one", value="1") }
}



