package com.exacs.ecra.utilities;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class HttpResponse<T> {

    private HttpStatus httpStatus;
    private HttpHeaders httpHeaders;

    public HttpResponse() {
        httpHeaders = new HttpHeaders();
    }

    public HttpResponse<T> status(int status) {
        this.httpStatus = HttpStatus.valueOf(status);
        return this;
    }

    public HttpResponse<T> header(String name, String value) {
        httpHeaders.set(name, value);
        return this;
    }

    public HttpResponse<T> location(URI location) {
        httpHeaders.setLocation(location);
        return this;
    }

    public HttpResponse<T> location(String path, Object... values) {
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().expand(values).encode().toUri();
        httpHeaders.setLocation(uri);
        return this;
    }

    public HttpResponse<T> created(URI location) {
        this.httpStatus = HttpStatus.CREATED;
        httpHeaders.setLocation(location);
        return this;
    }

    public HttpResponse<T> created(String path, Object... values) {
        return setStatusWithLocation(HttpStatus.CREATED, path, values);
    }

    public HttpResponse<T> accepted(String path, Object... values) {
        return setStatusWithLocation(HttpStatus.ACCEPTED, path, values);
    }

    private HttpResponse<T> setStatusWithLocation(HttpStatus httpStatus, String path, Object... values) {
        this.httpStatus = httpStatus;

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().expand(values).encode().toUri();
        httpHeaders.setLocation(uri);
        return this;
    }

    public HttpResponse<T> noContent() {
        this.httpStatus = HttpStatus.NO_CONTENT;
        return this;
    }

    public HttpResponse<T> ok() {
        this.httpStatus = HttpStatus.OK;
        return this;
    }

    public ResponseEntity<HttpStatus> build() {
        return new ResponseEntity<>(httpHeaders, httpStatus);
    }

    public ResponseEntity<T> build(T resource) {
        return new ResponseEntity<>(resource, httpHeaders, httpStatus);
    }

    protected static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }
}

