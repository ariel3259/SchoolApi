package ariel.schoolApi.dao;

import ariel.schoolApi.models.Admin;
import ariel.schoolApi.models.Students;

import java.util.List;

public interface StudentsDao {
    List<Students> getAllStudents();
    void setStudent(Students student);
    void updateStudent(Students student);
    void deleteStudent(String id);
    void register(Admin admin);
    boolean auth(Admin admin);
}
