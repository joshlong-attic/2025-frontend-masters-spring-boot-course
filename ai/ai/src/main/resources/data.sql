

insert into dog (id, name, owner, description)
    values (44,'Rocky', null, 'A brown Chihuahua known for being protective') on conflict  do nothing ;

insert into dog (id, name, owner, description)
    values (34,'Charlie', null, 'A black Bulldog known for being curious.') on conflict  do nothing ;

insert into dog (id, name, owner, description)
    values (47,'Duke', null, 'A white German Shepherd known for being friendly.') on conflict  do nothing ;

insert into dog (id, name, owner, description) values (45, 'Prancer', null, 'a neurotic dog') on conflict  do nothing ;
