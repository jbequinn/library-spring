-- Sequence: library."s_user"

CREATE SEQUENCE library.s_user
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
  CYCLE;
ALTER TABLE library.s_user
  OWNER TO libraryuser;


-- Sequence: library."s_book"

CREATE SEQUENCE library.s_book
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
  CYCLE;
ALTER TABLE library.s_book
  OWNER TO libraryuser;


-- Sequence: library."s_fine"

CREATE SEQUENCE library.s_fine
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
  CYCLE;
ALTER TABLE library.s_fine
  OWNER TO libraryuser;
