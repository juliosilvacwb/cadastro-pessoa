package com.springboot.api.entities.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseDTO<T> {

    private T body;
    private List<String> errors;

    public ResponseDTO<T> addError(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<String>();
        }
        errors.add(error);
        return this;
    }
}