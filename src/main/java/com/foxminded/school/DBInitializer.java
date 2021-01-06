package com.foxminded.school;

import com.foxminded.school.config.CommandLineInterface;
import com.foxminded.school.config.Context;
import com.foxminded.school.config.DataGenerator;
import com.foxminded.school.config.SQLScriptConverter;
import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.dao.entities.Group;
import com.foxminded.school.dao.entities.Student;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DBInitializer {

    public void generateRandomDataAndRunInterface(String createTablesScriptFileName, String dropTablesScriptFileName){
        Context context = Context.connectorTypeBuilder(Context.DbType.POSTGRES);

        File createTablesScriptFile = new File(createTablesScriptFileName);
        File dropTablesScriptFile = new File(dropTablesScriptFileName);
        SQLScriptConverter converter = context.getConverter();

        converter.convert(dropTablesScriptFile);
        converter.convert(createTablesScriptFile);

        DataGenerator data = context.getGenerator();

        List<Group> groups = data.generateGroups();
        GroupDAO groupDao = context.getGroupDAO();
        groupDao.createGroups(groups);

        List<Course> courses = data.generateCourses();
        CourseDAO courseDao = context.getCourseDAO();
        courseDao.createCourses(courses);

        List<Student> students = data.generateStudentsAndAssignThemToGroups(groups);
        Map<Student, List<Course>> studentsCourses = data.generateStudentsCourses(students, courses);
        StudentDAO studentDao = context.getStudentDAO();
        studentDao.createStudents(students);
        studentDao.updateStudents(studentsCourses);

        CommandLineInterface userInterface = context.getCommandLineInterface();
        userInterface.runInterface();
    }
}
