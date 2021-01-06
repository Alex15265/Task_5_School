package com.foxminded.school.dao;

import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.config.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements DAO<Course, Integer> {

    private static final String UPDATE = "UPDATE courses set course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String INSERT = "INSERT INTO courses (course_id, course_name, course_description) VALUES (?, ?, ?)";
    private static final String READ_ALL = "SELECT * FROM courses";
    private static final String READ_BY_ID = "SELECT * FROM courses WHERE course_id = ?";

    private static final String DELETE = "DELETE FROM courses WHERE course_id = ?";
    private static final String GET_BY_STUDENT_ID =
                            "SELECT courses.course_id, courses.course_name, courses.course_description " +
                            "FROM students_courses " +
                            "INNER JOIN courses " +
                            "ON students_courses.course_id = courses.course_id " +
                            "WHERE students_courses.student_id = ?";

    private final Connection connection;

    public CourseDAO(DBConnector connector) {
        connection = connector.connect();
    }

    @Override
    public void create(Course course) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setInt(1, course.getId());
            preparedStatement.setString(2, course.getName());
            preparedStatement.setString(3, course.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCourses(List<Course> courses) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            for(Course course : courses) {
                preparedStatement.setInt(1, course.getId());
                preparedStatement.setString(2, course.getName());
                preparedStatement.setString(3, course.getDescription());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> readAll() {
        List<Course> result;
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()){
            result = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Course readByID(Integer id) {
        Course course = new Course();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID)) {
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

    public List<Course> readCoursesByStudentID(int studentId) {
        List<Course> result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_STUDENT_ID)) {
            preparedStatement.setInt(1, studentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = processResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void update(Course course) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.setInt(3, course.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Course> processResultSet(ResultSet resultSet) throws SQLException{
        List<Course> result = new ArrayList<>();
        while (resultSet.next()) {
            Course course = new Course();
            course.setId(resultSet.getInt(1));
            course.setName(resultSet.getString(2));
            course.setDescription(resultSet.getString(3));
            result.add(course);
        }
        return result;
    }
}
