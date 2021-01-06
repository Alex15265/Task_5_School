package com.foxminded.school.config;

import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.dao.entities.Group;
import com.foxminded.school.dao.entities.Student;

import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    private final Scanner scanner;
    private final CourseDAO courseDAO;
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;

    public CommandLineInterface(StudentDAO studentDAO, GroupDAO groupDAO, CourseDAO courseDAO) {
        scanner = new Scanner(System.in);
        this.courseDAO = courseDAO;
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }

    public void runInterface() {
        boolean exit = false;

        while (!exit) {
            printMainMenu();

            switch (scanner.next()) {
                case "a":
                    findGroups();
                    break;
                case "b":
                    findStudentsByCourseName();
                    break;
                case "c":
                    addNewStudent();
                    break;
                case "d":
                    deleteStudentById();
                    break;
                case "e":
                    addStudentToCourse();
                    break;
                case "f":
                    removeStudentCourse();
                    break;
                case "g":
                    System.out.println("Exiting....");
                    exit = true;
                    break;
                default:
                    System.out.println("Enter a letter from 'a' to 'g'!");
                    break;
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("*** MAIN MENU ***");
        System.out.println("a. Find all groups with less or equals student count");
        System.out.println("b. Find all students related to course with given name");
        System.out.println("c. Add new student");
        System.out.println("d. Delete student by STUDENT_ID");
        System.out.println("e. Add a student to the course (from a list)");
        System.out.println("f. Remove the student from one of his or her courses");
        System.out.println("g. Exit program");
        System.out.print("Enter menu-letter >>> ");
    }

    private void findGroups() {
        System.out.println("Find groups by max. students count: ");
        System.out.println("Enter students count >>> ");
        int studentsCount = readID();
        List<Group> groups = groupDAO.readByNumberOfStudents(studentsCount);
        printGroups(groups);
    }

    private void findStudentsByCourseName() {
        System.out.println("Find students by course name: ");
        System.out.print("Enter course name >>> ");
        String courseName = scanner.next();

        List<Student> students = studentDAO.readByCourse(courseName);

        System.out.println("Students from course \"" + courseName + "\":");
        System.out.println();
        printStudents(students);
    }

    private void addNewStudent() {
        System.out.println("Add new Student: ");
        System.out.println("Enter first name >>> ");
        String firstName = scanner.next();

        System.out.println("Enter last name >>> ");
        String lastName = scanner.next();

        Student newStudent = new Student();
        newStudent.setFirstName(firstName);
        newStudent.setLastName(lastName);

        System.out.println("Student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " inserted");
    }

    private void deleteStudentById() {
        System.out.println("Delete student by ID: ");
        int studentId = readID();
        studentDAO.delete(studentId);
        System.out.println("Student deleted");
    }

    private void addStudentToCourse() {
        System.out.println("Add student to course: ");
        System.out.println("List of students: ");
        printStudents(studentDAO.readAll());

        System.out.println("Enter student id >>> ");
        int studentId = readID();

        System.out.println("List of courses: ");
        printCourses(courseDAO.readAll());

        System.out.println("Enter course id >>> ");
        int courseId = readID();

        if (studentId > 0 && courseId > 0) {
            studentDAO.updateStudentCourses(studentId, courseId);
            System.out.println("Course added");
        } else {
            System.out.println("Error, wrong IDs entered");
        }
    }

    private void removeStudentCourse() {
        System.out.println("Remove student course: ");
        System.out.println("List of students: ");
        printStudents(studentDAO.readAll());

        System.out.println("Enter student id >>> ");
        int studentId = readID();

        System.out.println("List of student courses: ");
        printCourses(courseDAO.readCoursesByStudentID(studentId));

        System.out.println("Enter course id >>> ");
        int courseId = readID();

        if (studentId > 0 && courseId > 0) {
            studentDAO.deleteStudentFromCourse(studentId, courseId);
            System.out.println("Relation student-course deleted");
        } else {
            System.out.println("Error, wrong IDs entered");
        }

    }

    private int readID() {
        int number = 0;
        while (number == 0) {
            try {
                number = scanner.nextInt();
                System.out.println("Number entered: " + number);
            } catch (Exception e) {
                System.out.println("Error! Please enter number!");
            }
        }
        return number;
    }

    private void printGroups(List<Group> groups) {
        for (Group group : groups) {
            System.out.println("Group name: " + group.getName());
            System.out.println("Students: " + group.getStudentsCount());
            System.out.println();
        }
    }

    private void printStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println("Student ID: " + student.getId());
            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
            System.out.println();
        }
    }

    private void printCourses(List<Course> courses) {
        for (Course course : courses) {
            System.out.println("Course ID: " + course.getId());
            System.out.println("Name: " + course.getName());
            System.out.println();
        }
    }
}
