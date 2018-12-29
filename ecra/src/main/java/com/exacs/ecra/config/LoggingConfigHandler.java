package com.exacs.ecra.config;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.MDC;

@Aspect
@Component
public class LoggingConfigHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Pointcut("execution(* *.*(..))")
    public void allMethod() {
    }

    @Pointcut("execution ( * com.exacs.ecra..* (..))")
    public void allMethods() {}

    @Pointcut("@annotation(com.exacs.ecra.annotations.ExcludeAspectDebug)")
    public void debugExclude() {}


    /**
     *
     * @param joinPoint
     */
    @Before("!controller() && allMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {

        CodeSignature sig = (CodeSignature) joinPoint.getSignature();
        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());

        MDC.put("backendFunctionName",
                sig.getDeclaringTypeName() + "_" + sig.getName());


        Object[] args = joinPoint.getArgs();
        if (args != null) {
            try {
                String[] names = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
                for (int i = 0; i < args.length; i++) {
                    log.debug("Entering Argument(s) - [" + names[i] + "]: " + args[i]);
                }
            } catch (Throwable t) {
            }
        }

    }

    /**
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "!controller() && allMethods() && !debugExclude()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        try {
            String returnValue = this.getValue(result);
            CodeSignature sig = (CodeSignature) joinPoint.getSignature();
            MDC.put("backendFunctionName",
                    joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
            log.debug("Leaving: " + sig.getDeclaringTypeName());
            log.debug("Method Return value : " + returnValue);
        } catch (Exception e) {
            // Ignore
        }

    }

    /**
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(pointcut = "!controller() && allMethods()", throwing = "exception")
    public void logAfterMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        CodeSignature sig = (CodeSignature) joinPoint.getSignature();
        MDC.put("backendFunctionName",
                joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
        log.debug("Throwing: " + sig.getDeclaringTypeName());

        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
    }

    /**
     * before -> Any resource annotated with @RestController annotation
     * and all method and function taking HttpServletRequest as first parameter
     *
     * @param joinPoint
     */
    @Before("controller() && allMethod()")
    public void logBefore(JoinPoint joinPoint) {

        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        final HttpServletRequest request = attr.getRequest();

        MDC.put("backendFunctionName", joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
        log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
        log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

        if (null != request) {
            log.debug("Start Header Section of request ");
            log.debug("Method Type : " + request.getMethod());
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
            }
            log.debug("Request Path info :" + request.getServletPath());
            log.debug("End Header Section of request ");
        }
    }

    /**
     * After -> All method within resource annotated with @RestController annotation
     * and return a  value
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "controller() && allMethod() && !debugExclude()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        try {
            MDC.put("backendFunctionName",
                    joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
            String returnValue = this.getValue(result);
            log.debug("Method Return value : " + returnValue);
        } catch (Exception e) {
            // Ignore
        }

    }

    /**
     * After -> Any method within resource annotated with @RestController annotation
     *     throws an exception ...Log it
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MDC.put("backendFunctionName",
                joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
    }

    /**
     * Around -> Any method within resource annotated with @RestController annotation
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controller() && allMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            MDC.put("backendFunctionName",
                    joinPoint.getSignature().getDeclaringTypeName() + "_" + joinPoint.getSignature().getName());
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            // Execute the method
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.debug("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
    private String getValue(Object result) throws JsonProcessingException {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(result);
                returnValue = json;
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }


}
