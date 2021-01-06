package com.foxminded.school;

public class Main {

    public static void main(String[] args) {
        DBInitializer schoolBase = new DBInitializer();
        schoolBase.generateRandomDataAndRunInterface("src/main/resources/create_tables.sql",
                                                     "src/main/resources/drop_tables.sql");
    }
}
