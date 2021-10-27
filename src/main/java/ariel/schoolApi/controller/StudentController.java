package ariel.schoolApi.controller;

import ariel.schoolApi.dao.StudentsDao;
import ariel.schoolApi.models.Admin;
import ariel.schoolApi.models.Students;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/")
@CrossOrigin(origins = "*",methods={RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT ,RequestMethod.DELETE})
public class StudentController {
    @Autowired
    private StudentsDao studentsDao;

    @GetMapping("")
    public String Home(){
        return "Hi word";
    }
    @GetMapping("api/students")
    public List<Students> getStudents(){
        return studentsDao.getAllStudents();
    }

    @PostMapping("api/students")
    public void setStudents(@RequestBody Students student){
        studentsDao.setStudent(student);
    }

    @PutMapping("api/students")
    public void updateStudent(@RequestBody Students student){
        studentsDao.updateStudent(student);
    }

    @DeleteMapping("api/students")
    public void rejectStudent(@RequestHeader(name="id") String id){
      studentsDao.deleteStudent(id);
    }

    @PostMapping("api/register")
    public void register(@RequestBody Admin admin){
        //argon works to hash password
        Argon2 argon2= Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //passwords hashes by argon
        String hash=argon2.hash(1,1024,1,admin.getPassword());
        //save it at admin
        admin.setPassword(hash);
        studentsDao.register(admin);
    }

    @PostMapping("api/auth")
    public boolean auth(@RequestBody Admin admin){
       return studentsDao.auth(admin);
    }
}
