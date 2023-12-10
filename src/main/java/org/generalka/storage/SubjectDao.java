package org.generalka.storage;

import java.util.List;

public interface SubjectDao {
    void addSubject(Subject subject);
    Subject getSubjectById(int id);
    List<Subject> getAllSubjects();
    void updateSubject(Subject subject);
    void deleteSubject(int id);
}

