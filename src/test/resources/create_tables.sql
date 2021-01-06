CREATE TABLE IF NOT EXISTS groups
(
group_id SERIAL PRIMARY KEY,
group_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS students
(
student_id SERIAL PRIMARY KEY,
group_id INT,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses
(
course_id SERIAL PRIMARY KEY,
course_name VARCHAR(50) NOT NULL,
course_description VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS students_courses
(
student_id INT NOT NULL,
course_id INT NOT NULL,
CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES students(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES courses(course_id ) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT student_course UNIQUE (student_id, course_id)
);

INSERT INTO groups (group_id, group_name)
VALUES (1, 'aa-01');
INSERT INTO groups (group_id, group_name)
VALUES (2, 'aa-02');
INSERT INTO groups (group_id, group_name)
VALUES (3, 'aa-03');
INSERT INTO groups (group_id, group_name)
VALUES (4, 'aa-04');

INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (1, 1, 'Alex', 'Smith');
INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (2, 2, 'Ann', 'White');
INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (3, 2, 'Leo', 'Messi');
INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (4, 3, 'Lisa', 'Ann');
INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (5, 3, 'Roy', 'Jones');
INSERT INTO students (student_id, group_id, first_name, last_name)
VALUES (6, 3, 'Bart', 'Simpson');

INSERT INTO courses (course_id, course_name, course_description)
VALUES (1, 'aaa', 'AAA');
INSERT INTO courses (course_id, course_name, course_description)
VALUES (2, 'bbb', 'BBB');
INSERT INTO courses (course_id, course_name, course_description)
VALUES (3, 'ccc', 'CCC');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 1);
INSERT INTO students_courses (student_id, course_id)
VALUES (2, 1);
INSERT INTO students_courses (student_id, course_id)
VALUES (2, 2);
INSERT INTO students_courses (student_id, course_id)
VALUES (3, 1);
INSERT INTO students_courses (student_id, course_id)
VALUES (3, 2);
INSERT INTO students_courses (student_id, course_id)
VALUES (3, 3);
INSERT INTO students_courses (student_id, course_id)
VALUES (4, 2);
INSERT INTO students_courses (student_id, course_id)
VALUES (4, 3);
INSERT INTO students_courses (student_id, course_id)
VALUES (5, 2);
