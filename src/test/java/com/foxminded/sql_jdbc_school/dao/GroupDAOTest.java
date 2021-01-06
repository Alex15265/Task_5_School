package com.foxminded.sql_jdbc_school.dao;

import com.foxminded.school.config.Context;
import com.foxminded.school.config.SQLScriptConverter;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.entities.Group;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class GroupDAOTest extends BaseTest {
    GroupDAO groupDAO;
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
        groupDAO = context.getGroupDAO();
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
    void create_ShouldAdd1RecordToDBWhenReceived1Group() {
        List<Group> groupsFromDBBefore = readAllGroups(connection);
        Assert.assertEquals(4, groupsFromDBBefore.size());

        Group group = new Group(5, "aa-05");
        groupDAO.create(group);

        Group groupFromDB = readGroupByID(5, connection);
        Assert.assertEquals("aa-05", groupFromDB.getName());

        List<Group> groupsFromDBAfter = readAllGroups(connection);
        Assert.assertEquals(5, groupsFromDBAfter.size());
    }

    @Test
    void createGroups_ShouldAdd3RecordsToDBWhenReceivedAListOf3Groups() {
        List<Group> groupsFromDBBefore = readAllGroups(connection);
        Assert.assertEquals(4, groupsFromDBBefore.size());

        List<Group> groups = new ArrayList<>();
        groups.add(new Group(5,"aa-05"));
        groups.add(new Group(6,"aa-06"));
        groups.add(new Group(7,"aa-07"));

        groupDAO.createGroups(groups);

        Group courseFromDB5 = readGroupByID(5, connection);
        Assert.assertEquals("aa-05", courseFromDB5.getName());
        Group courseFromDB6 = readGroupByID(6, connection);
        Assert.assertEquals("aa-06", courseFromDB6.getName());
        Group courseFromDB7 = readGroupByID(7, connection);
        Assert.assertEquals("aa-07", courseFromDB7.getName());

        List<Group> groupsFromDBAfter = readAllGroups(connection);
        Assert.assertEquals(7, groupsFromDBAfter.size());
    }

    @Test
    void readAll_ShouldReturn4GroupsFromDBWhenDBHas4Records() {
        List<Group> groupsFromDB = groupDAO.readAll();
        Assert.assertEquals(4, groupsFromDB.size());
    }

    @Test
    void readByID_ShouldReturnRecordFromDBWhenReceivedItsGroupId() {
        Group dbGroupWhenIdIs1 = groupDAO.readByID(1);
        Assert.assertEquals("aa-01", dbGroupWhenIdIs1.getName());

        Group dbGroupWhenIdIs4 = groupDAO.readByID(4);
        Assert.assertEquals("aa-04", dbGroupWhenIdIs4.getName());
    }

    @Test
    void readByNumberOfStudents_ShouldReturnListOf3GroupsWhenReceivedNumberOfStudentsEquals2() {
        List<Group> groupsWhenStudentsIsLessThan3 = groupDAO.readByNumberOfStudents(2);
        Assert.assertEquals(3, groupsWhenStudentsIsLessThan3.size());
    }

    @Test
    void update_ShouldUpdateRecordByIDInDBWhenReceivedGroupWithSameID() {
        Group groupBeforeUpdate = readGroupByID(2, connection);
        Assert.assertEquals("aa-02", groupBeforeUpdate.getName());

        Group group = new Group(2, "bb-33");
        groupDAO.update(group);

        Group groupAfterUpdate = readGroupByID(2, connection);
        Assert.assertEquals("bb-33", groupAfterUpdate.getName());
    }

    @Test
    void delete_ShouldDeleteRecordInDBWhenReceivedItsID() {
        List<Group> groupsFromDBBefore = readAllGroups(connection);
        Assert.assertEquals(4, groupsFromDBBefore.size());

        groupDAO.delete(4);

        Group group = readGroupByID(4, connection);
        Assert.assertNull(group.getName());

        List<Group> groupsFromDBAfter = readAllGroups(connection);
        Assert.assertEquals(3, groupsFromDBAfter.size());
    }
}