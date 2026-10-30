package com.dormi.dormi_be.global.security;

import io.swagger.v3.oas.annotations.Hidden;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 컨트롤러의 Long 파라미터에 붙이면 현재 로그인한 사용자의 id가 주입된다. */
@Hidden
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthUser {
}
