package com.foxminded.sql_jdbc_school;

import com.foxminded.school.config.Context;
import com.foxminded.school.config.SQLScriptConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class SQLScriptConverterTest {

    SQLScriptConverter converter;
    Context context;

    @BeforeEach
    void initContext() {
        context = Context.connectorTypeBuilder(Context.DbType.H2);
    }

    @BeforeEach
    void init() {
        converter = context.getConverter();
    }

    @Test
    void convert_ShouldThrowExceptionWhenFileIsNotFound() {
        File file = new File("src/test/resources/nonexistentfile.sql");
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                converter.convert(file));
    }

    @Test
    void convert_ShouldThrowExceptionWhenFileIsEmpty() {
        File file = new File("src/test/resources/empty.sql");
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                converter.convert(file));
    }

    @Test
    void convert_ShouldThrowExceptionWhenInputIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                converter.convert(null));
    }
}