package ru.glebov.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.glebov.springcourse.models.Person;


import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate; // создаем поле и внедряем через @autowired конструктор:

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /* код подключения к БД уже не нужен, так как они в dataSource
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class)); // два аргумента - запрос и объект
        // класса ро маппер (заменили на встроенный BeanPropertyRowMapper)
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person where id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                                .stream().findAny().orElse(null);//// передача параметров с помощью массива Object[]{id}
        // если список не пустой возврат объекта класса Person,
        // если пустой - возрат нул через лямбда выражение - findAny - если есть в списке - возврат его, orElse - возврат
        // нул (можно возвращать msg с ошибкой - не найден человек..)
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person VALUES(1,?,?,?)", person.getName(),person.getAge(),
                person.getEmail()); // var args
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?", updatedPerson.getName(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id); // var args
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person where id=?", id); // var args
    }
}