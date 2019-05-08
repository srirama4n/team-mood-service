package com.dbs.app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "SURVEY")
public class Survey {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createAt = new Date();

    @NotNull
    @Column(name = "CREATED_BY")
    private String createBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRES_ON")
    private Date expiresOn;

    @Column(name = "SURVEY_KEY")
    private String surveyKey;

    @Column(name = "SURVEY_STATUS")
    private String surveyStatus;

    @Column(name = "NO_OF_RESPONDENTS")
    private Integer respondents;

    @ManyToOne
    @JoinColumn(name = "SQUAD_ID")
    private Squad squad;

    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "survey")
    private SurveyLink surveyLink;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "survey"
    )
    private Set<Temperature> temperature = new HashSet<>();
}
