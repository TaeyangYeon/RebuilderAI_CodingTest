package lecture.registration.manager.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RegistrationDateServiceImplTest {

  @Autowired
  private RegistrationManageService registrationDateService;

  @Test
  void insertAndUpdateAndGet() {
    //given
    String insertStartDate = "2023-04-01";
    String insertEndDate = "2023-04-17";
    registrationDateService.insertOrUpdateRegistrationDate(insertStartDate, insertEndDate);

    //when
    String startDate = "2023-05-02";
    String endDate = "2023-05-20";
    registrationDateService.insertOrUpdateRegistrationDate(startDate, endDate);

    //then
    RegistrationDateDto registrationDate = registrationDateService.registrationDate();
    Assertions.assertThat(registrationDate.getStartDate()).isEqualTo(startDate);
  }


}