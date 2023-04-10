package lecture.registration.student.dto;

import lombok.Data;

@Data
public class StudentDto {

  private Long studentId;
  private String studentName;
  private String studentEmail;
  private String studentMobile;

}
