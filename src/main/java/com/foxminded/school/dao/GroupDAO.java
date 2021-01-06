package com.foxminded.school.dao;

import com.foxminded.school.config.DBConnector;
import com.foxminded.school.dao.entities.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO implements DAO<Group, Integer> {
    private static final String UPDATE = "UPDATE groups set group_name=? WHERE group_id=?";
    private static final String READ_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
    private static final String GET_ALL = "SELECT * FROM groups";
    private static final String INSERT = "INSERT INTO groups (group_id, group_name) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM groups WHERE group_id = ?";
    private static final String GET_BY_STUDENTS_COUNT =
                    "SELECT groups.group_name, COUNT(students.student_id) " +
                    "FROM groups " +
                    "LEFT JOIN students " +
                    "ON students.group_id = groups.group_id " +
                    "GROUP BY groups.group_id " +
                    "HAVING COUNT(*) <= ?";

    private final Connection connection;

    public GroupDAO(DBConnector connector) {
        connection = connector.connect();
    }

    @Override
    public void create(Group group) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setInt(1, group.getId());
            preparedStatement.setString(2, group.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGroups(List<Group> groups) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            for(Group group : groups) {
                preparedStatement.setInt(1, group.getId());
                preparedStatement.setString(2, group.getName());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> readAll(){
        List<Group> result;
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()){
            result = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Group readByID(Integer id) {
        Group group = new Group();
        try(PreparedStatement preparedStatement = connection.prepareStatement(READ_BY_ID)) {
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

    public List<Group> readByNumberOfStudents(int count) {
        List<Group> result = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_STUDENTS_COUNT)){
            preparedStatement.setInt(1, count);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    Group group = new Group();
                    group.setName(resultSet.getString(1));
                    group.setStudentsCount(resultSet.getInt(2));
                    result.add(group);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void update(Group group) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getId());
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

    private List<Group> processResultSet(ResultSet resultSet) throws SQLException{
        List<Group> result = new ArrayList<>();
        while (resultSet.next()) {
            Group group = new Group();
            group.setId(resultSet.getInt(1));
            group.setName(resultSet.getString(2));
            result.add(group);
        }
        return result;
    }
}
