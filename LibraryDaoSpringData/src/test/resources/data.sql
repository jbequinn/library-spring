insert into user (id, first_name, last_name, phone, address, email, join_date)
values (1, 'Cristiano', 'Ronaldo', '555-555321', 'Concha Espina 1', 'cris@cr7.com', '2014-09-03 00:00:00');

insert into user (id, first_name, last_name, phone, address, email, join_date)
values (2, 'Karim', 'Benzema', '555-555322', 'Concha Espina 1', 'karim@benzema.com', '2014-09-04 00:00:00');

insert into book (id, title, isbn, bought_date)
values (1, 'Guerra y paz', '1234-5678', '2014-09-01 00:00:00');

insert into book (id, title, isbn, bought_date)
values (2, 'Crimen y castigo', '1234-5679', '2014-09-01 00:00:00');

insert into borrow (user_id, book_id, borrow_date, expected_return_date, actual_return_date)
values (1, 1, '2014-09-03 00:00:00', '2014-09-17 10:00:00', '2014-09-16 18:00:00');

insert into fine (id, user_id, fine_end_date)
values (1, 2, '2014-10-19 18:00:00')

insert into fine (id, user_id, fine_end_date)
values (2, 2, '2014-11-19 18:00:00')

insert into borrow (user_id, book_id, fine_id, borrow_date, expected_return_date, actual_return_date)
values (2, 1, 1, '2014-10-03 10:00:00', '2014-10-17 10:00:00', '2014-10-18 18:00:00');

insert into borrow (user_id, book_id, fine_id, borrow_date, expected_return_date, actual_return_date)
values (2, 2, 2, '2014-11-03 10:00:00', '2014-11-17 10:00:00', '2014-11-18 18:00:00');
