package com.foxminded.school.dao;

import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.config.DBConnector;
import com.foxminded.school.dao.entities.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDAO implements DAO<Student,Integer> {

    private static final String UPDATE = "UPDATE students set first_name=?, last_name=? WHERE student_id=?";
    private static final String READ_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    private static final String GET_ALL = "SELECT * FROM students";
    private static final String INSERT = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String DELETE = "DELETE FROM students WHERE student_id = ?";
    private static final String GET_BY_COURSE_NAME =
                    "SELECT students.student_id, students.group_id, students.first_name, students.last_name " +
                    "FROM students_courses " +
                    "INNER  JOIN students " +
                    "ON students_courses.student_id = students.student_id " +
                    "INNER  JOIN courses " +
                    "ON students_courses.course_id = courses.course_id " +
                    "WHERE courses.course_name = ?";
    private static final String ASSIGN_TO_COURSE = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?)";
    private static final String DELETE_FROM_COURSE = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";

    private final Connection connection;

    public StudentDAO(DBConnector connector) {
        connection = connector.connect();
    }

    @Override
    public void create(Student student) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createStudents(List<Student> students) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            for(Student student : students) {
                preparedStatement.setInt(1, student.getGroupId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> readAll() {
        List<Student> result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
                result = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Student readByID(Integer id) {
        Student student = new Student();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID)) {
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

    public List<Student> readByCourse(String courseName) {
        List<Student> result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_COURSE_NAME)) {
            preparedStatement.setString(1, courseName);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                result = processResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void update(Student student) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setInt(3, student.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStudents(Map<Student, List<Course>> map) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_TO_COURSE)) {
            for (Map.Entry<Student, List<Course>> entry : map.entrySet()) {
                Student student = entry.getKey();
                for (Course course : entry.getValue()) {
                    preparedStatement.setInt(1, student.getId());
                    preparedStatement.setInt(2, course.getId());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStudentCourses(int studentId, int courseId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_TO_COURSE)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
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

    public void deleteStudentFromCourse(int studentId, int courseId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_COURSE)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Student> processResultSet(ResultSet resultSet) throws SQLException{
        List<Student> result = new ArrayList<>();
        while(resultSet.next()) {
            Student student = new Student();
            student.setId(resultSet.getInt(1));
            student.setGroupId(resultSet.getInt(2));
            student.setFirstName(resultSet.getString(3));
            student.setLastName(resultSet.getString(4));
            result.add(student);
        }
        return result;
    }
}
