package com.foxminded.school.config;

import com.foxminded.school.dao.entities.Course;
import com.foxminded.school.dao.entities.Group;
import com.foxminded.school.dao.entities.Student;

import java.util.*;

public class DataGenerator {

    private final String CHARACTERS = "qwertyuiopasdfghjklzxcvbnm";

    private final String[] firstNames =    {"James", "John", "Mike", "Jeremy", "Ivan",
                                    "Ron", "Anthony", "Jack", "Harry", "Jacob",
                                    "Kyle", "William", "David", "Richard", "Joseph",
                                    "Thomas", "Alexander", "Daniel", "Oscar", "Charlie"};

    private final String[] lastNames = {"Ainsley", "Appleton", "Clare", "Clifford", "Benson",
                                "Bentley", "Deighton", "Darlington", "Digby", "Kimberley",
                                "Kirby", "Langley", "Elton", "Everleigh", "Garrick",
                                "Milton", "Brixton", "Hallewell", "Oakes", "Perry"};

    private final int[] groupSizes = {0, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                                    21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private final Random random = new Random();


    public List<Group> generateGroups() {
        List<Group> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Group group = new Group();
            group.setId(i+1);
            String name = String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))) +
                    CHARACTERS.charAt(random.nextInt(CHARACTERS.length())) +
                    "-" +
                    random.nextInt(10) +
                    random.nextInt(10);
            group.setName(name);
            list.add(group);
        }
        return list;
    }

    public List<Course> generateCourses() {
        List<Course> list = new ArrayList<>();
        list.add(new Course(1, "Archaeology", "Archaeology is the study of human and prehistory, " +
                "conducted through the act of excavation and analysis."));
        list.add(new Course(2, "Architecture", "Buildings and other physical structures, but also" +
                "the art and science of designing buildings, and the design and " +
                "method of construction."));
        list.add(new Course(3, "Chemistry", "Chemistry is one of three central branches of " +
                "educational science. It is a physical science that studies " +
                "the composition, structure, properties and change of matter."));
        list.add(new Course(4, "Computer Science", "How we use computers and computer programmes " +
                "has utterly defined the world we live in today and its computer " +
                "scientists whom connect the abstract with concrete creating the " +
                "products we use every day."));
        list.add(new Course(5, "Criminology", "Criminology is the scientific study of criminal behaviour" +
                ", on individual, social and natural levels, and how it can be managed, " +
                "controlled and prevented."));
        list.add(new Course(6, "Economics", "Social science of which factors determine the production and " +
                "distribution goods and services in a consumer, capitalist society."));
        list.add(new Course(7, "History", "Historians use evidence to try to understand why people believed " +
                "what they believed and why they did what they did."));
        list.add(new Course(8, "Mathematics", "There are three main areas of study under the umbrella of " +
                "Mathematics – mathematics itself, statistics, and operational research"));
        list.add(new Course(9, "Robotics", "Robotics is a branch of mechanical engineering, electrical " +
                "engineering, electronic engineering and computer science."));
        list.add(new Course(10, "Sociology", "Sociology is the scientific study of behaviour by people in" +
                " the society in which they live, how it came about, is organised and " +
                "developed, and what it may become in the future."));
        return list;
    }

    public List<Student> generateStudentsAndAssignThemToGroups(List<Group> groups) {
        List<Student> students = new ArrayList<>();

        for(int i = 0; i < 200; i++) {
            Student student = new Student();
            student.setId(i + 1);
            student.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            student.setLastName(lastNames[random.nextInt(lastNames.length)]);
            students.add(student);
        }

        int remainingStudents = students.size();
        for(Group group : groups) {
            int studentsInGroup = groupSizes[random.nextInt(groupSizes.length)];
            if (studentsInGroup <= remainingStudents) {
                remainingStudents = remainingStudents - studentsInGroup;
                group.setStudentsCount(studentsInGroup);
                for (int i = 0; i < studentsInGroup; i++) {
                    for (Student student : students) {
                        if (student.getGroupId() == null) {
                            student.setGroupId(group.getId());
                            break;
                        }
                    }
                }
            } else {
                group.setStudentsCount(0);
            }
        }

        for (Student student : students) {
            if (student.getGroupId() == null) {
                student.setGroupId(0);
            }
        }

        return students;
    }

    public Map<Student, List<Course>> generateStudentsCourses(List<Student> students, List<Course> courses){
        Map<Student, List<Course>> result = new HashMap<>();
        for(Student student : students) {
            int coursesCount = random.nextInt(3) + 1;
            List<Course> coursesTmp = new ArrayList<>(courses);
            List<Course> studentCourses = new ArrayList<>();
            for(int i = 0; i < coursesCount; i++) {
                int randomCourseIndex = random.nextInt(coursesTmp.size());
                Course studentCourse = coursesTmp.get(randomCourseIndex);
                studentCourses.add(studentCourse);
                coursesTmp.remove(randomCourseIndex);
            }
            result.put(student, studentCourses);
        }
        return result;
    }
}
