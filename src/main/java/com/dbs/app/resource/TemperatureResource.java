package com.dbs.app.resource;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemperatureResource {
    String id;
    String comment;
    Integer rating;
    String surveyId;
}
