package com.foxminded.sql_jdbc_school.dao;

import com.foxminded.school.config.Context;
import com.foxminded.school.config.SQLScriptConverter;
import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.entities.Course;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class CourseDAOTest extends BaseTest{

    CourseDAO courseDAO;
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
        courseDAO = context.getCourseDAO();
    }

    @BeforeEach
    void createTables() {
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
    void create_ShouldAdd1RecordToDBWhenReceived1Course() {
        List<Course> coursesFromDBBefore = readAllCourses(connection);
        Assert.assertEquals(3, coursesFromDBBefore.size());

        Course course = new Course(4, "History", "HistoryDescription");
        courseDAO.create(course);

        Course courseFromDB = readCourseByID(4, connection);
        Assert.assertEquals("History", courseFromDB.getName());
        Assert.assertEquals("HistoryDescription", courseFromDB.getDescription());

        List<Course> coursesFromDBAfter = readAllCourses(connection);
        Assert.assertEquals(4, coursesFromDBAfter.size());
    }

    @Test
    void createCourses_ShouldAdd3RecordsToDBWhenReceivedAListOf3Courses() {
        List<Course> coursesFromDBBefore = readAllCourses(connection);
        Assert.assertEquals(3, coursesFromDBBefore.size());

        List<Course> courses = new ArrayList<>();
        courses.add(new Course(4,"History", "HistoryDescription"));
        courses.add(new Course(5,"Robotics", "RoboticsDescription"));
        courses.add(new Course(6,"English","EnglishDescription"));

        courseDAO.createCourses(courses);

        Course courseFromDB4 = readCourseByID(4, connection);
        Assert.assertEquals("History", courseFromDB4.getName());
        Assert.assertEquals("HistoryDescription", courseFromDB4.getDescription());
        Course courseFromDB5 = readCourseByID(5, connection);
        Assert.assertEquals("Robotics", courseFromDB5.getName());
        Assert.assertEquals("RoboticsDescription", courseFromDB5.getDescription());
        Course courseFromDB6 = readCourseByID(6, connection);
        Assert.assertEquals("English", courseFromDB6.getName());
        Assert.assertEquals("EnglishDescription", courseFromDB6.getDescription());

        List<Course> coursesFromDBAfter = readAllCourses(connection);
        Assert.assertEquals(6, coursesFromDBAfter.size());
    }

    @Test
    void readAll_ShouldReturn3CoursesFromDBWhenDBHas3Records() {
        List<Course> coursesFromDB = courseDAO.readAll();
        Assert.assertEquals(3, coursesFromDB.size());
    }

    @Test
    void readByID_ShouldReturnRecordFromDBWhenReceivedItsCourseId() {
        Course dbCourseWhenIdIs1 = courseDAO.readByID(1);
        Assert.assertEquals("aaa", dbCourseWhenIdIs1.getName());
        Assert.assertEquals("AAA", dbCourseWhenIdIs1.getDescription());

        Course dbCourseWhenIdIs3 = courseDAO.readByID(3);
        Assert.assertEquals("ccc", dbCourseWhenIdIs3.getName());
        Assert.assertEquals("CCC", dbCourseWhenIdIs3.getDescription());
    }

    @Test
    void readCoursesByStudentID_ShouldReturnListOf2CoursesWhenReceivedStudentIDEquals4() {
        List<Course> coursesWhenIdIs4 = courseDAO.readCoursesByStudentID(4);
        Assert.assertEquals(2, coursesWhenIdIs4.size());
    }

    @Test
    void update_ShouldUpdateRecordByIDInDBWhenReceivedCourseWithSameID() {
        Course courseBeforeUpdate = readCourseByID(2, connection);
        Assert.assertEquals("bbb", courseBeforeUpdate.getName());
        Assert.assertEquals("BBB", courseBeforeUpdate.getDescription());

        Course course = new Course(2, "hhh", "HHH");
        courseDAO.update(course);

        Course courseAfterUpdate = readCourseByID(2, connection);
        Assert.assertEquals("hhh", courseAfterUpdate.getName());
        Assert.assertEquals("HHH", courseAfterUpdate.getDescription());
    }

    @Test
    void delete_ShouldDeleteRecordInDBWhenReceivedItsID() {
        List<Course> coursesFromDBBefore = readAllCourses(connection);
        Assert.assertEquals(3, coursesFromDBBefore.size());

        courseDAO.delete(2);

        Course course = readCourseByID(2, connection);
        Assert.assertNull(course.getName());
        Assert.assertNull(course.getDescription());

        List<Course> coursesFromDBAfter = readAllCourses(connection);
        Assert.assertEquals(2, coursesFromDBAfter.size());
    }
}