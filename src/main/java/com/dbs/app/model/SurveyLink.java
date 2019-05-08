package com.dbs.app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "SURVEY_LINK", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"SQUAD_ID", "TOWER_ID", "GROUP_ID", "PROJECT_ID"})
})
public class SurveyLink {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    @JoinColumn(name = "SQUAD_ID")
    private Squad squad;

    @OneToOne
    @JoinColumn(name = "TOWER_ID")
    private Tower tower;

    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @OneToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @OneToOne
    @JoinColumn(name = "SURVEY_ID")
    private Survey survey;
}
