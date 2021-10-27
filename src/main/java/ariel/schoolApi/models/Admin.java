package ariel.schoolApi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Admin {
    private String id,name,lastname,email,password;
}