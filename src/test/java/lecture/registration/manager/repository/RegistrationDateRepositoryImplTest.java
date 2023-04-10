package lecture.registration.manager.repository;

import lecture.registration.manager.dto.RegistrationDateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RegistrationDateRepositoryImplTest {

  private final RegistrationManageRepository registrationDateRepository;

  @Autowired
  public RegistrationDateRepositoryImplTest(RegistrationManageRepository registrationDateRepository) {
    this.registrationDateRepository = registrationDateRepository;
  }

  @Test
  void insertAndUpdateAndGet() {
    //given
    String inserttStartDate = "2023-04-08";
    String insertEndDate = "2023-04-21";
    registrationDateRepository.insertRegistrationDate(inserttStartDate, insertEndDate);

    //when
    String startDate = "2023-05-08";
    String endDate = "2023-05-20";
    registrationDateRepository.updateRegistrationDate(startDate, endDate,1L);

    //then
    RegistrationDateDto registrationDate = registrationDateRepository.registrationDate();
    Assertions.assertThat(registrationDate.getStartDate()).isEqualTo(startDate);
  }
}