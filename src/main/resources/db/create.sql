use team_mood_api_db;
create table GROUPS
(
  ID   VARCHAR(150) NOT NULL
    PRIMARY KEY,
  NAME VARCHAR(150) NULL
)
  ENGINE = InnoDB;

CREATE TABLE PROJECT
(
  ID       VARCHAR(150) NOT NULL
    PRIMARY KEY,
  NAME     VARCHAR(150) NULL,
  TOWER_ID VARCHAR(150) NULL
)
  ENGINE = InnoDB;

CREATE INDEX FKps4w8banq6isk3g3hgw6aopt7
  ON PROJECT (TOWER_ID);

CREATE TABLE SQUAD
(
  ID         VARCHAR(150) NOT NULL
    PRIMARY KEY,
  NAME       VARCHAR(150) NULL,
  PROJECT_ID VARCHAR(150) NULL,
  CONSTRAINT FKkm4hi58v8wwpe798ag71gm387
  FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT (ID)
)
  ENGINE = InnoDB;

CREATE INDEX FKkm4hi58v8wwpe798ag71gm387
  ON SQUAD (PROJECT_ID);

CREATE TABLE SURVEY
(
  ID         VARCHAR(150) NOT NULL
    PRIMARY KEY,
  CREATED_AT DATETIME     NULL,
  UPDATED_AT DATETIME     NULL,
  SQUAD_ID   VARCHAR(150) NULL,
  EXPIRES_ON DATETIME     NULL,
  CREATED_BY VARCHAR(150) NULL,
  CONSTRAINT FKreoudo7ywu8tnyhgo50mabufi
  FOREIGN KEY (SQUAD_ID) REFERENCES SQUAD (ID)
)
  ENGINE = InnoDB;

CREATE INDEX FKreoudo7ywu8tnyhgo50mabufi
  ON SURVEY (SQUAD_ID);

CREATE TABLE TEMPERATURE
(
  ID        VARCHAR(150) NOT NULL
    PRIMARY KEY,
  COMMENT   VARCHAR(150) NULL,
  RATING    INT          NULL,
  SURVEY_ID VARCHAR(150) NULL,
  CONSTRAINT FKjs3jdg1jgahelpgfachetehkq
  FOREIGN KEY (SURVEY_ID) REFERENCES SURVEY (ID)
)
  ENGINE = InnoDB;

CREATE INDEX FKjs3jdg1jgahelpgfachetehkq
  ON TEMPERATURE (SURVEY_ID);

CREATE TABLE TOWER
(
  ID       VARCHAR(150) NOT NULL
    PRIMARY KEY,
  NAME     VARCHAR(150) NULL,
  GROUP_ID VARCHAR(150) NULL,
  CONSTRAINT FKnhjd03wqet455hl40l3lo7l0w
  FOREIGN KEY (GROUP_ID) REFERENCES GROUPS (ID)
)
  ENGINE = InnoDB;

CREATE INDEX FKnhjd03wqet455hl40l3lo7l0w
  ON TOWER (GROUP_ID);

ALTER TABLE PROJECT
  ADD CONSTRAINT FKps4w8banq6isk3g3hgw6aopt7
FOREIGN KEY (TOWER_ID) REFERENCES TOWER (ID);

CREATE TABLE SURVEY_LINK
(
  ID         VARCHAR(150) NOT NULL
    PRIMARY KEY,
  GROUP_ID   VARCHAR(150) NULL,
  PROJECT_ID VARCHAR(150) NULL,
  SQUAD_ID   VARCHAR(150) NULL,
  SURVEY_ID  VARCHAR(150) NULL,
  TOWER_ID   VARCHAR(150) NULL,
  CONSTRAINT UKkwtxuqoqwhw3tepx6soi3rdnf
  UNIQUE (SQUAD_ID, TOWER_ID, GROUP_ID, PROJECT_ID),
  CONSTRAINT FKlkpkm3u3g8od0lk73pe5q7vln
  FOREIGN KEY (GROUP_ID) REFERENCES GROUPS (ID),
  CONSTRAINT FKkl8ynl3bivu7acb3b3qb5jm9d
  FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT (ID),
  CONSTRAINT FK66jqdc4xasrtf9236aitlaoo8
  FOREIGN KEY (SQUAD_ID) REFERENCES SQUAD (ID),
  CONSTRAINT FKdmxwu8ontk21e8qooaf5cd2ae
  FOREIGN KEY (SURVEY_ID) REFERENCES SURVEY (ID),
  CONSTRAINT FK9hh9mf6on2s0ry3y8rfe2kubv
  FOREIGN KEY (TOWER_ID) REFERENCES TOWER (ID)
)
  ENGINE = InnoDB;

CREATE INDEX FKlkpkm3u3g8od0lk73pe5q7vln
  ON SURVEY_LINK (GROUP_ID);

CREATE INDEX FKkl8ynl3bivu7acb3b3qb5jm9d
  ON SURVEY_LINK (PROJECT_ID);

CREATE INDEX FKdmxwu8ontk21e8qooaf5cd2ae
  ON SURVEY_LINK (SURVEY_ID);

CREATE INDEX FK9hh9mf6on2s0ry3y8rfe2kubv
  ON SURVEY_LINK (TOWER_ID);

ALTER TABLE SURVEY ADD SURVEY_KEY varchar(150) DEFAULT 'NULL';
ALTER TABLE SURVEY ADD SURVEY_STATUS varchar(50) DEFAULT 'ACTIVE';
ALTER TABLE SURVEY ADD COLUMN NO_OF_RESPONDENTS INT NOT NULL DEFAULT 10;