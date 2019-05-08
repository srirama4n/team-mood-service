package com.dbs.app.resource;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static org.springframework.http.HttpStatus.OK;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResource {
    int status = OK.value();
    String message;
    String code;
}
