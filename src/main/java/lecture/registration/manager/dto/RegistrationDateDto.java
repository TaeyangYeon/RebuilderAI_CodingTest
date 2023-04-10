package lecture.registration.manager.dto;

import lombok.Data;

/**
 * 수강 등록 일정에 대한 dto class
 */

@Data
public class RegistrationDateDto {

  private Long lectureRegistrationPeriodId;
  private String startDate;
  private String endDate;

}
