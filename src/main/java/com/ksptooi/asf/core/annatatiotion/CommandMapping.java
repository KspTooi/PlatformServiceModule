package com.ksptooi.asf.core.annatatiotion;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMapping {

    String value();

}
