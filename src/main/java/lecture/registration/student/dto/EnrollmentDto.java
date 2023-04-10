package lecture.registration.student.dto;

import lombok.Data;

@Data
public class EnrollmentDto {

  private Long enrollmentId;
  private Long studentId;
  private Long lectureId;
  private String registrationDate;

  public EnrollmentDto(Long studentId, Long lectureId, String registrationDate) {
    this.studentId = studentId;
    this.lectureId = lectureId;
    this.registrationDate = registrationDate;
  }
}
