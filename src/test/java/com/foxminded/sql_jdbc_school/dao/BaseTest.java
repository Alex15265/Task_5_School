package com.foxminded.sql_jdbc_school.dao;

import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.dao.entities.Group;
import com.foxminded.school.dao.entities.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    private static final String READ_ALL_COURSES = "SELECT * FROM courses";
    private static final String READ_BY_ID_COURSE = "SELECT * FROM courses WHERE course_id = ?";
    private static final String READ_ALL_GROUPS = "SELECT * FROM groups";
    private static final String READ_BY_ID_GROUP = "SELECT * FROM groups WHERE group_id = ?";
    private static final String READ_ALL_STUDENTS = "SELECT * FROM students";
    private static final String READ_BY_ID_STUDENT = "SELECT * FROM students WHERE student_id = ?";
    private static final String GET_BY_COURSE_NAME =
                    "SELECT students.student_id, students.group_id, students.first_name, students.last_name " +
                    "FROM students_courses " +
                    "INNER  JOIN students " +
                    "ON students_courses.student_id = students.student_id " +
                    "INNER  JOIN courses " +
                    "ON students_courses.course_id = courses.course_id " +
                    "WHERE courses.course_name = ?";


    public List<Course> readAllCourses(Connection connection) {
        List<Course> result;
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_COURSES);
            ResultSet resultSet = preparedStatement.executeQuery()){
            result = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getInt(1));
                course.setName(resultSet.getString(2));
                course.setDescription(resultSet.getString(3));
                result.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Course readCourseByID(Integer id, Connection connection) {
        Course course = new Course();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID_COURSE)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    course.setId(resultSet.getInt(1));
                    course.setName(resultSet.getString(2));
                    course.setDescription(resultSet.getString(3));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course;
    }

    public List<Group> readAllGroups(Connection connection){
        List<Group> result;
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_GROUPS);
            ResultSet resultSet = preparedStatement.executeQuery()){
            result = new ArrayList<>();
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt(1));
                group.setName(resultSet.getString(2));
                result.add(group);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Group readGroupByID(Integer id, Connection connection) {
        Group group = new Group();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID_GROUP)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    group.setId(resultSet.getInt(1));
                    group.setName(resultSet.getString(2));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return group;
    }

    public List<Student> readAllStudents(Connection connection) {
        List<Student> result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_STUDENTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            result = new ArrayList<>();
            while(resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setGroupId(resultSet.getInt(2));
                student.setFirstName(resultSet.getString(3));
                student.setLastName(resultSet.getString(4));
                result.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Student readStudentByID(Integer id, Connection connection) {
        Student student = new Student();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID_STUDENT)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                    student.setGroupId(resultSet.getInt(2));
                    student.setFirstName(resultSet.getString(3));
                    student.setLastName(resultSet.getString(4));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public List<Student> readStudentByCourse(String courseName, Connection connection) {
        List<Student> result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_COURSE_NAME)) {
            preparedStatement.setString(1, courseName);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                result = new ArrayList<>();
                while(resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getInt(1));
                    student.setGroupId(resultSet.getInt(2));
                    student.setFirstName(resultSet.getString(3));
                    student.setLastName(resultSet.getString(4));
                    result.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
