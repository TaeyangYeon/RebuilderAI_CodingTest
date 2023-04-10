package lecture.registration.student.dto;

import lombok.Data;

@Data
public class LectureDto {

  private Long lectureId;
  private String lectureName;
  private String lectureDescription;
  private String lectureOpen;

}
