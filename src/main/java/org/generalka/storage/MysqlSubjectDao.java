package org.generalka.storage;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
public class MysqlSubjectDao implements SubjectDao{
    private JdbcTemplate jdbcTemplate;

    public MysqlSubjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveSubject(Subject subject) {
        String sql = "INSERT INTO Subject (subject, semester, year_of_study) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, subject.getSubject(), subject.getSemester(), subject.getYearOfStudy());
    }

    @Override
    public void addSubject(Subject subject) {

    }

    @Override
    public Subject getSubjectById(int id) {
        String sql = "SELECT * FROM Subject WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Subject.class));
    }

    @Override
    public List<Subject> getAllSubjects() {
        String sql = "SELECT * FROM Subject";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Subject.class));
    }

    @Override
    public void updateSubject(Subject subject) {
        String sql = "UPDATE Subject SET subject = ?, semester = ?, year_of_study = ? WHERE id = ?";
        jdbcTemplate.update(sql, subject.getSubject(), subject.getSemester(), subject.getYearOfStudy(), subject.getId());
    }

    @Override
    public void deleteSubject(int id) {
        String sql = "DELETE FROM Subject WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

