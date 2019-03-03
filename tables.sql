SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'polyglot'
  AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS polyglot;

CREATE DATABASE polyglot
       WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       template = template0
       LC_COLLATE = 'C'
       LC_CTYPE = 'C'
       CONNECTION LIMIT = -1;

SET default_with_oids = false;
SET search_path = public;

\connect polyglot
-----------------------------------------------------------------------

CREATE SEQUENCE words_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE words_seq
  OWNER TO postgres;

CREATE SEQUENCE translations_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE translations_seq
  OWNER TO postgres;

CREATE SEQUENCE examples_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE examples_seq
  OWNER TO postgres;

CREATE SEQUENCE properties_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE properties_seq
  OWNER TO postgres;

-----------------------------------------------------------------------

CREATE TABLE words
(
  id integer NOT NULL DEFAULT nextval('words_seq'),
  word character varying(40) NOT NULL,
  language character varying(20) NOT NULL,
  word_type character varying(20) NOT NULL,
  priority smallint,
  CONSTRAINT words_pk PRIMARY KEY (id)
);
ALTER TABLE words
  OWNER TO postgres;

-----------------------------------------------------------------------

CREATE TABLE translations
(
  id integer NOT NULL DEFAULT nextval('translations_seq'),
  word_id integer NOT NULL,
  translation_id integer NOT NULL,
  CONSTRAINT translations_pk PRIMARY KEY (id),
  CONSTRAINT translations_fk FOREIGN KEY (translation_id)
      REFERENCES words (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT words_fk FOREIGN KEY (word_id)
      REFERENCES words (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE translations
  OWNER TO postgres;

-----------------------------------------------------------------------

CREATE TABLE examples
(
  id integer NOT NULL DEFAULT nextval('examples_seq'),
  example text NOT NULL,
  word_id integer NOT NULL,
  CONSTRAINT examples_pk PRIMARY KEY (id),
  CONSTRAINT examples_words_fk FOREIGN KEY (word_id)
      REFERENCES words (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE examples
  OWNER TO postgres;

-----------------------------------------------------------------------

CREATE TABLE properties
(
  id integer NOT NULL DEFAULT nextval('properties_seq'),
  key character varying(30) NOT NULL,
  value text NOT NULL,
  word_id integer NOT NULL,
  CONSTRAINT properties_pk PRIMARY KEY (id),
  CONSTRAINT properties_words_fk FOREIGN KEY (word_id)
      REFERENCES words (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE properties
  OWNER TO postgres;
