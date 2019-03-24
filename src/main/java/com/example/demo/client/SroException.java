package com.example.demo.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import feign.Response;
import feign.codec.ErrorDecoder;


public class SroException extends RuntimeException implements Serializable {
    public final int status;

    public final List<String> errors;

    public SroException(final int status, final String message, final Collection<String> errors) {
        super(message);
        this.status = status;
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SroException)) return false;
        SroException sroException = (SroException) o;
        return status == sroException.status &&
                Objects.equals(super.getMessage(), sroException.getMessage()) &&
                Objects.equals(errors, sroException.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, super.getMessage(), errors);
    }
}