package com.foxminded.sql_jdbc_school.dao;

import com.foxminded.school.config.Context;
import com.foxminded.school.config.SQLScriptConverter;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.dao.entities.Student;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class StudentDAOTest extends BaseTest {

    StudentDAO studentDAO;
    File createTablesScriptFile;
    File dropTablesScriptFile;
    SQLScriptConverter converter;
    Context context;
    Connection connection;

    @BeforeEach
    void initContext() {
        context = Context.connectorTypeBuilder(Context.DbType.H2);
    }

    @BeforeEach
    void initConnection() {
        connection = context.getConnector().connect();
    }

    @BeforeEach
    void initCourseDAO() {
        studentDAO = context.getStudentDAO();
    }

    @BeforeEach
    void initTestDB() {
        createTablesScriptFile = new File("src/test/resources/create_tables.sql");
        converter = context.getConverter();
        converter.convert(createTablesScriptFile);
    }

    @AfterEach
    void dropTables() {
        dropTablesScriptFile = new File("src/test/resources/drop_tables.sql");
        converter = context.getConverter();
        converter.convert(dropTablesScriptFile);
    }

    @Test
    void create_ShouldAdd1RecordToDBWhenReceived1Student() {
        List<Student> studentsFromDBBefore = readAllStudents(connection);
        Assert.assertEquals(6, studentsFromDBBefore.size());

        Student student = new Student(7, 0, "Alex", "Jones");
        studentDAO.create(student);

        Student studentFromDB = readStudentByID(7, connection);
        Assert.assertEquals("Alex", studentFromDB.getFirstName());
        Assert.assertEquals("Jones", studentFromDB.getLastName());

        List<Student> studentsFromDBAfter = readAllStudents(connection);
        Assert.assertEquals(7, studentsFromDBAfter.size());
    }

    @Test
    void createStudents_ShouldAdd2RecordsToDBWhenReceivedAListOf2Students() {
        List<Student> studentsFromDBBefore = readAllStudents(connection);
        Assert.assertEquals(6, studentsFromDBBefore.size());

        List<Student> students = new ArrayList<>();
        students.add(new Student(7, 0, "Jane", "Blanc"));
        students.add(new Student(8, 0, "Rob", "Shnider"));

        studentDAO.createStudents(students);

        Student studentFromDB7 = readStudentByID(7, connection);
        Assert.assertEquals("Jane", studentFromDB7.getFirstName());
        Assert.assertEquals("Blanc", studentFromDB7.getLastName());
        Student studentFromDB8 = readStudentByID(8, connection);
        Assert.assertEquals("Rob", studentFromDB8.getFirstName());
        Assert.assertEquals("Shnider", studentFromDB8.getLastName());

        List<Student> studentsFromDBAfter = readAllStudents(connection);
        Assert.assertEquals(8, studentsFromDBAfter.size());
    }

    @Test
    void readAll_ShouldReturn6StudentsFromDBWhenDBHas6Records() {
        List<Student> studentsFromDB = studentDAO.readAll();
        Assert.assertEquals(6, studentsFromDB.size());
    }

    @Test
    void readByID_ShouldReturnRecordFromDBWhenReceivedItsStudentId() {
        Student dbStudentWhenIdIs3 = studentDAO.readByID(3);
        Assert.assertEquals("Leo", dbStudentWhenIdIs3.getFirstName());
        Assert.assertEquals("Messi", dbStudentWhenIdIs3.getLastName());
        Assert.assertEquals((Integer) 2, dbStudentWhenIdIs3.getGroupId());

        Student dbStudentWhenIdIs6 = studentDAO.readByID(6);
        Assert.assertEquals("Bart", dbStudentWhenIdIs6.getFirstName());
        Assert.assertEquals("Simpson", dbStudentWhenIdIs6.getLastName());
        Assert.assertEquals((Integer) 3, dbStudentWhenIdIs6.getGroupId());
    }

    @Test
    void readByCourse_ShouldReturnListOf4StudetsWhenReceivedCourseNameEqualsBbb() {
        List<Student> studentsOfBbbCourse = studentDAO.readByCourse("bbb");
        Assert.assertEquals(4, studentsOfBbbCourse.size());
    }

    @Test
    void update_ShouldUpdateRecordByIDInDBWhenReceivedStudentWithSameID() {
        Student studentBeforeUpdate = readStudentByID(4, connection);
        Assert.assertEquals("Lisa", studentBeforeUpdate.getFirstName());
        Assert.assertEquals("Ann", studentBeforeUpdate.getLastName());

        Student student = new Student(4, "Alex" , "Belyaev");
        studentDAO.update(student);

        Student studentAfterUpdate = readStudentByID(4, connection);
        Assert.assertEquals("Alex", studentAfterUpdate.getFirstName());
        Assert.assertEquals("Belyaev", studentAfterUpdate.getLastName());
    }

    @Test
    void updateStudentCourses_ShouldAddBbbCourseToStudentWithIdEquals1() {
        List<Student> studentsOfBbbCourseBeforeUpdate = readStudentByCourse("bbb", connection);
        Assert.assertEquals(4, studentsOfBbbCourseBeforeUpdate.size());

        studentDAO.updateStudentCourses(1, 2);

        List<Student> studentsOfBbbCourseAfterUpdate = readStudentByCourse("bbb", connection);
        Assert.assertEquals(5, studentsOfBbbCourseAfterUpdate.size());
    }

    @Test
    void delete_ShouldDeleteRecordInDBWhenReveivedItsID() {
        List<Student> studentsFromDBBefore = readAllStudents(connection);
        Assert.assertEquals(6, studentsFromDBBefore.size());

        studentDAO.delete(2);

        Student student = readStudentByID(2, connection);
        Assert.assertNull(student.getFirstName());
        Assert.assertNull(student.getLastName());

        List<Student> studentsFromDBAfter = readAllStudents(connection);
        Assert.assertEquals(5, studentsFromDBAfter.size());
    }

    @Test
    void deleteStudentFromCourse_ShouldDeleteRecordInDBWhenReceivedStudentAndCourseIDs() {
        List<Student> studentsOfAaaCourseBeforeDelete = readStudentByCourse("aaa", connection);
        Assert.assertEquals(3, studentsOfAaaCourseBeforeDelete.size());

        studentDAO.deleteStudentFromCourse(2, 1);

        List<Student> studentsOfAaaCourseAfterDelete = readStudentByCourse("aaa", connection);
        Assert.assertEquals(2, studentsOfAaaCourseAfterDelete.size());
    }
}