package ru.nik66.crudservlet.store;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.nik66.crudservlet.model.City;
import ru.nik66.crudservlet.model.Country;
import ru.nik66.crudservlet.model.Role;
import ru.nik66.crudservlet.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MemoryStoreTest {

    private Store store;

    @Before
    public void init() {
        this.store = MemoryStore.getInstance();
        this.store.clear();
    }

    @After
    public void clear() {
        this.store.clear();
    }

    @Test
    public void whenAddUserAndFindAllUsers() {
        User user = new User("Nikolay", "nik", "p", Role.ADMIN, "nik@e.mail", LocalDateTime.now(), Country.USA, City.NY);
        this.store.add(user);
        List<User> actual = this.store.findAll();
        List<User> excepted = Collections.singletonList(user);
        assertThat(actual, is(excepted));
    }

    @Test
    public void whenUpdateUser() {
        User user = new User("Nikolay", "nik", "p", Role.ADMIN, "nik@e.mail", LocalDateTime.now(), Country.USA, City.NY);
        this.store.add(user);
        User newUser = new User(user.getId(), "Newby", "nb", "p", Role.ADMIN, "non@mm.nn", null, Country.USA, City.NY);
        this.store.update(newUser);
        List<User> actual = this.store.findAll();
        List<User> excepted = Collections.singletonList(newUser);
        assertThat(actual, is(excepted));
    }

    @Test
    public void whenDeleteUser() {
        User first = new User("Nikolay", "nik", "p", Role.USER, "nik@e.mail", LocalDateTime.now(), Country.USA, City.NY);
        User second = new User("Newby", "nb", "p", Role.USER, "non@mm.nn", LocalDateTime.now(), Country.USA, City.NY);
        this.store.add(first);
        this.store.add(second);
        this.store.delete(second);
        List<User> actual = this.store.findAll();
        List<User> excepted = Collections.singletonList(first);
        assertThat(actual, is(excepted));
    }

    @Test
    public void whenFindByIdUser() {
        User user = new User("Nikolay", "nik", "p", Role.ADMIN, "nik@e.mail", LocalDateTime.now(), Country.USA, City.NY);
        this.store.add(user);
        User actual = this.store.findById(user.getId());
        User expected = user;
        assertThat(actual, is(expected));
    }
}