package ariel.schoolApi.dao;

import ariel.schoolApi.models.Admin;
import ariel.schoolApi.models.Students;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class StudentsDaoImp implements StudentsDao{
    @Autowired
    private JdbcTemplate plantillas;

    public StudentsDaoImp(JdbcTemplate plantillas){
    this.plantillas=plantillas;
    }
    @Override
    public List<Students> getAllStudents() {
        return plantillas.query("select bin_to_uuid(id) id,name,lastname,dni from students",(rs,rowNum)->new Students(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("lastname"),
                rs.getString("dni")
        ));
    }

    @Override
    public void setStudent(Students student) {
        plantillas.update("insert into students(id,name,lastname,dni) values(uuid_to_bin(uuid()),?,?,?)",student.getName(),student.getLastname(),student.getDni());
    }

    @Override
    public void updateStudent(Students student) {
        plantillas.update("update students set name=?,lastname=?,dni=? where id=uuid_to_bin(?)",student.getName(),student.getLastname(),student.getDni(),student.getId());
    }

    @Override
    public void deleteStudent(String id) {
        plantillas.update("delete from students where id=uuid_to_bin(?)",id);
    }
    @Override
    public void register(Admin admin) {
        plantillas.update("insert into admin(id,name,lastname,email,password) values(uuid_to_bin(uuid()),?,?,?,?)",admin.getName(),admin.getLastname(),admin.getEmail(),admin.getPassword());
    }

    @Override
    public boolean auth(Admin admin) {
        //find the admin from the database
        List<Admin> extractAdmin=plantillas.query("select email,password from admin where email=?",(rs, rowNum)-> new Admin(
                null,
                null,
                null,
                rs.getString("email"),
                rs.getString("password")
        ),admin.getEmail());
        if(extractAdmin.isEmpty()){
            return false;
        }
        //extract admin from the List
        Admin verifyAdmin=extractAdmin.get(0);
        Argon2 argon2= Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //verifies if the password exists and if the admin insert the true password
        return argon2.verify(verifyAdmin.getPassword(),admin.getPassword());
    }
}
