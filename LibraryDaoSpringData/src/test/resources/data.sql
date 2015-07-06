insert into user (id, first_name, last_name, phone, address, join_date)
values (1, 'Cristiano', 'Ronaldo', '555-555321', 'Concha Espina 1', '2014-09-03 00:00:00');

insert into book (id, title, isbn, bought_date)
values (1, 'Guerra y paz', '1234-5678', '2014-09-01 00:00:00');

insert into book (id, title, isbn, bought_date)
values (2, 'Crimen y castigo', '1234-5679', '2014-09-01 00:00:00');

insert into borrow (user_id, book_id, borrow_date, expected_return_date, actual_return_date)
values (1, 1, '2014-09-03 00:00:00', '2014-09-17 10:00:00', '2014-09-16 18:00:00');