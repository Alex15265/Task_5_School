package com.foxminded.school.config;

import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;

public final class Context {
    public enum DbType {H2, POSTGRES}

    private final DataGenerator generator;
    private final DBConnector connector;
    private final SQLScriptConverter converter;
    private final StudentDAO studentDAO;
    private final GroupDAO groupDAO;
    private final CourseDAO courseDAO;
    private final CommandLineInterface commandLineInterface;

    public Context(SQLScriptConverter converter,
                   DataGenerator generator,
                   StudentDAO studentDAO,
                   GroupDAO groupDAO,
                   CourseDAO courseDAO,
                   CommandLineInterface commandLineInterface,
                   DBConnector connector) {

        this.converter = converter;
        this.generator = generator;
        this.studentDAO = studentDAO;
        this.groupDAO = groupDAO;
        this.courseDAO = courseDAO;
        this.connector = connector;
        this.commandLineInterface = commandLineInterface;
    }

    public static Context connectorTypeBuilder(DbType dbType) {
        DBConnector connector;
        if (dbType == DbType.POSTGRES){
            connector = new DBConnector("postgres_config");
        } else {
            connector = new DBConnector("H2_config");
        }

        SQLScriptConverter converter = new SQLScriptConverter(connector);
        DataGenerator generator = new DataGenerator();
        StudentDAO studentDAO = new StudentDAO(connector);
        GroupDAO groupDAO = new GroupDAO(connector);
        CourseDAO courseDAO = new CourseDAO(connector);
        CommandLineInterface commandLineInterface = new CommandLineInterface (studentDAO, groupDAO, courseDAO);


        return new Context(converter, generator, studentDAO, groupDAO, courseDAO, commandLineInterface, connector);
    }

    public SQLScriptConverter getConverter() {
        return converter;
    }

    public CommandLineInterface getCommandLineInterface() {
        return commandLineInterface;
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public CourseDAO getCourseDAO() {
        return courseDAO;
    }

    public DataGenerator getGenerator() {
        return generator;
    }

    public DBConnector getConnector() {
        return connector;
    }
}
