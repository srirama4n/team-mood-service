package com.dbs.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "TEMPERATURE")
@ToString
@EqualsAndHashCode
public class Temperature {

    @Id
    @Column(name = "ID")
    private String id;

    @NotNull
    @Column(name = "COMMENT")
    private String comment;

    @NotNull
    @Column(name = "RATING")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;
}
