-- Table: library."user"

CREATE TABLE library.user
(
  "id" bigint NOT NULL DEFAULT nextval('library.s_user'::regclass),
  "first_name" character varying(100) NOT NULL,
  "last_name" character varying(100) NOT NULL,
  "phone" character varying(100),
  "address" character varying(100) NOT NULL,
  "email" character varying(100) NOT NULL,
  "join_date" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT "user_PKEY" PRIMARY KEY ("id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE library.user
  OWNER TO libraryuser;


-- Table: library."book"

CREATE TABLE library.book
(
  "id" bigint NOT NULL DEFAULT nextval('library.s_book'::regclass),
  "title" character varying(100) NOT NULL,
  "isbn" character varying(100) NOT NULL UNIQUE,
  "bought_date" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT "book_PKEY" PRIMARY KEY ("id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE library.book
  OWNER TO libraryuser;


-- Table: library."borrow"

CREATE TABLE library.borrow
(
  "user_id" bigint NOT NULL REFERENCES library.user(id),
  "book_id" bigint NOT NULL REFERENCES library.book(id),
  "borrow_date" timestamp with time zone NOT NULL,
  "expected_return_date" timestamp with time zone NOT NULL,
  "actual_return_date" timestamp with time zone,
  CONSTRAINT "borrow_PKEY" PRIMARY KEY ("user_id", "book_id", "borrow_date")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE library.borrow
  OWNER TO libraryuser;


-- Table: library."fine"

CREATE TABLE library.fine
(
  "id" bigint NOT NULL DEFAULT nextval('library.s_fine'::regclass),
  "user_id" bigint NOT NULL REFERENCES library.user(id),
  "fine_start_date" timestamp with time zone NOT NULL,
  "fine_end_date" timestamp with time zone NOT NULL,
  CONSTRAINT "fine_PKEY" PRIMARY KEY ("id")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE library.borrow
  OWNER TO libraryuser;
