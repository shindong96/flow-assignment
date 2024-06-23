package com.flow.assignment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
class AccessRuleRepositoryTest {

    @Autowired
    private AccessRuleRepository repository;

    @DisplayName("content 포함 여부로 조회를 검증한다.")
    @ParameterizedTest
    @CsvSource(value = {"구글,false,1", "카카오,false,2", "네이버,true,2"})
    void findByContentContaining(String content, boolean hasNext, int size) {
        // given
        saveAccessRuleByContent("구글 IP");
        saveAccessRuleByContent("카카오엔터 IP");
        saveAccessRuleByContent("카카오톡 IP");
        saveAccessRuleByContent("네이버 IP");
        saveAccessRuleByContent("네이버파이낸셜 IP");
        saveAccessRuleByContent("네이버웹툰 IP");
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Direction.DESC, "id"));

        // when
        Slice<AccessRule> results = repository.findByContentContaining(content, pageRequest);

        // then
        assertAll(
                () -> assertThat(results.hasNext()).isEqualTo(hasNext),
                () -> assertThat(results.getContent()).hasSize(size)
        );
    }

    private void saveAccessRuleByContent(final String content) {
        repository.save(AccessRule
                .builder()
                .content(content)
                .build());
    }
}
