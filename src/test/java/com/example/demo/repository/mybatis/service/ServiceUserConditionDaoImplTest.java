package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.service.ServiceUserConditionDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceUserConditionDaoImplTest {

    @Autowired
    private ServiceUserConditionDaoImpl serviceUserConditionDao;

    private final String currentDateFormat = "2025/01/09";
    private final Integer managerSeq = 1;


    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserConditionDao);
        serviceUserConditionDao.deleteAll();
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        assertEquals(cnt, serviceUserConditionDao.count());
    }

    @DisplayName("존재 여부 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 존재_여부_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            assertTrue(serviceUserConditionDao.existsByCondCode(dto.getCond_code()));
        }
    }

    @DisplayName("코드로 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 코드로_조회_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            ServiceUserConditionDto selected = serviceUserConditionDao.select(dto.getCond_code());

            assertNotNull(selected);
            assertEquals(dto.getCond_code(), selected.getCond_code());
            assertEquals(dto.getName(), selected.getName());
            assertEquals(dto.getShort_exp(), selected.getShort_exp());
            assertEquals(dto.getLong_exp(), selected.getLong_exp());
            assertEquals(dto.getChk_use(), selected.getChk_use());
            assertEquals(dto.getLaw1(), selected.getLaw1());
            assertEquals(dto.getLaw2(), selected.getLaw2());
            assertEquals(dto.getLaw3(), selected.getLaw3());
        }
    }

    @DisplayName("전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_조회_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        List<ServiceUserConditionDto> selected = serviceUserConditionDao.selectAll();
        assertNotNull(selected);
        assertEquals(cnt, selected.size());

        dummy.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));
        selected.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            ServiceUserConditionDto selectedDto = selected.get(i);

            assertEquals(dto.getCond_code(), selectedDto.getCond_code());
            assertEquals(dto.getName(), selectedDto.getName());
            assertEquals(dto.getShort_exp(), selectedDto.getShort_exp());
            assertEquals(dto.getLong_exp(), selectedDto.getLong_exp());
            assertEquals(dto.getChk_use(), selectedDto.getChk_use());
            assertEquals(dto.getLaw1(), selectedDto.getLaw1());
            assertEquals(dto.getLaw2(), selectedDto.getLaw2());
            assertEquals(dto.getLaw3(), selectedDto.getLaw3());
        }
    }

    @DisplayName("생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 생성_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        assertEquals(cnt, serviceUserConditionDao.count());
    }

    @DisplayName("수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 수정_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            dto.setName("수정된 이름" + i);
            dto.setShort_exp("수정된 짧은 설명" + i);
            dto.setLong_exp("수정된 긴 설명" + i);
            dto.setChk_use("N");
            dto.setLaw1("수정된 법안 내용 " + i);
            dto.setLaw2("수정된 법안 내용 " + i);
            dto.setLaw3("수정된 법안 내용 " + i);
            dto.setComt("수정된 비고" + i);
            dto.setUp_date(currentDateFormat);
            dto.setUp_user_seq(managerSeq);

            assertEquals(1, serviceUserConditionDao.update(dto));
        }

        List<ServiceUserConditionDto> selected = serviceUserConditionDao.selectAll();
        assertNotNull(selected);
        assertEquals(cnt, selected.size());

        dummy.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));
        selected.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            ServiceUserConditionDto selectedDto = selected.get(i);

            assertEquals(dto.getCond_code(), selectedDto.getCond_code());
            assertEquals(dto.getName(), selectedDto.getName());
            assertEquals(dto.getShort_exp(), selectedDto.getShort_exp());
            assertEquals(dto.getLong_exp(), selectedDto.getLong_exp());
            assertEquals(dto.getChk_use(), selectedDto.getChk_use());
            assertEquals(dto.getLaw1(), selectedDto.getLaw1());
            assertEquals(dto.getLaw2(), selectedDto.getLaw2());
            assertEquals(dto.getLaw3(), selectedDto.getLaw3());
        }
    }

    @DisplayName("사용여부 수정 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 사용여부_수정_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            dto.setChk_use("N");
            assertEquals(1, serviceUserConditionDao.updateChkUse(dto));
        }

        List<ServiceUserConditionDto> selected = serviceUserConditionDao.selectAll();
        assertNotNull(selected);
        assertEquals(cnt, selected.size());

        dummy.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));
        selected.sort((a, b) -> a.getCond_code().compareTo(b.getCond_code()));

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            ServiceUserConditionDto selectedDto = selected.get(i);

            assertEquals(dto.getCond_code(), selectedDto.getCond_code());
            assertEquals(dto.getName(), selectedDto.getName());
            assertEquals(dto.getShort_exp(), selectedDto.getShort_exp());
            assertEquals(dto.getLong_exp(), selectedDto.getLong_exp());
            assertEquals("N", selectedDto.getChk_use());
            assertEquals(dto.getLaw1(), selectedDto.getLaw1());
            assertEquals(dto.getLaw2(), selectedDto.getLaw2());
            assertEquals(dto.getLaw3(), selectedDto.getLaw3());
        }
    }

    @DisplayName("삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 삭제_테스트(int cnt) {
        List<ServiceUserConditionDto> dummy = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            dummy.add(dto);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = dummy.get(i);
            assertEquals(1, serviceUserConditionDao.delete(dto.getCond_code()));
        }

        assertEquals(0, serviceUserConditionDao.count());
    }

    @DisplayName("전체 삭제 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 전체_삭제_테스트(int cnt) {
        for (int i = 0; i < cnt; i++) {
            ServiceUserConditionDto dto = createDto(i);
            assertEquals(1, serviceUserConditionDao.insert(dto));
        }

        assertEquals(cnt, serviceUserConditionDao.count());
        assertEquals(cnt, serviceUserConditionDao.deleteAll());
        assertEquals(0, serviceUserConditionDao.count());
    }

    private ServiceUserConditionDto createDto(int i) {
        ServiceUserConditionDto dto = new ServiceUserConditionDto();

        dto.setCond_code("테스트용" + i);
        dto.setName("테스트용 항목" + i);
        dto.setShort_exp("짧은 설명" + i);
        dto.setLong_exp("긴 설명" + i);
        dto.setChk_use("Y");
        dto.setLaw1("법안 내용 " + i);
        dto.setLaw2("법안 내용 " + i);
        dto.setLaw3("법안 내용 " + i);
        dto.setComt("비고" + i);
        dto.setReg_date(currentDateFormat);
        dto.setReg_user_seq(managerSeq);
        dto.setUp_date(currentDateFormat);
        dto.setUp_user_seq(managerSeq);

        return dto;
    }


}