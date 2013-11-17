CREATE TABLE APP.parent
  (
  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  NAME VARCHAR(24) NOT NULL,
  AGE INTEGER NOT NULL
  );

  INSERT into APP.parent(NAME, AGE) values('P1', 1);
  INSERT into APP.parent(NAME, AGE) values('P2', 2);
  INSERT into APP.parent(NAME, AGE) values('P3', 3);
  INSERT into APP.parent(NAME, AGE) values('P4', 4);
  INSERT into APP.parent(NAME, AGE) values('P5', 5);