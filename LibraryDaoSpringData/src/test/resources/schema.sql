drop table fine if exists;
drop table borrow if exists;
drop table user if exists;
drop table book if exists;

drop sequence if exists S_FINE;
drop sequence if exists S_USER;
drop sequence if exists S_BOOK;

create sequence S_FINE start with 1;
create sequence S_USER start with 1;
create sequence S_BOOK start with 1;

create table book (
        id integer not null,
        bought_date timestamp,
        isbn varchar(255),
        title varchar(255),
        primary key (id)
);

create table user (
        id integer not null,
        address varchar(255),
        first_name varchar(255),
        join_date timestamp,
        last_name varchar(255),
        phone varchar(255),
        email varchar(100),
        primary key (id)
);

create table fine (
        id integer not null,
        user_id integer not null,
        fine_end_date timestamp,
        primary key (id)
);

alter table fine
        add constraint FK_FINE_USER
        foreign key (user_id)
        references user(id);

create table borrow (
        book_id integer not null,
        borrow_date timestamp not null,
        user_id integer not null,
        fine_id integer,
        actual_return_date timestamp,
        expected_return_date timestamp,
        primary key (book_id, borrow_date, user_id)
);

alter table borrow
        add constraint FK_BORROW_BOOK
        foreign key (book_id)
        references book(id);

alter table borrow
        add constraint FK_BORROW_USER
        foreign key (user_id)
        references user(id);

alter table borrow
        add constraint FK_BORROW_FINE
        foreign key (fine_id)
        references user(id);
