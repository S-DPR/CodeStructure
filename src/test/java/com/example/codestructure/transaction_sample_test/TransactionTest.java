package com.example.codestructure.transaction_sample_test;

import com.example.codestructure.transaction_sample.dto.Entities;
import com.example.codestructure.transaction_sample.entity.Parent;
import com.example.codestructure.transaction_sample.entity.Child;
import com.example.codestructure.transaction_sample.repos.ParentRepos;
import com.example.codestructure.transaction_sample.repos.ChildRepos;
import com.example.codestructure.transaction_sample.service.CoordinatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TransactionTest {

    @Autowired
    CoordinatorService coordinatorService;

    @Autowired
    ParentRepos parentRepos;

    @Autowired
    ChildRepos childRepos;

    @Test
    @Transactional
    void save_성공_동시에_넣기_동작확인() {
        // given
        Parent parent = new Parent();
        parent.setData(123);

        Child child = new Child();
        child.setData(456);

        Entities entities = new Entities(parent, child);

        // when
        Entities saved = coordinatorService.save(entities);

        // then
        UUID entityAId = saved.getParent().getId();
        UUID entityBId = saved.getChild().getId();

        assertThat(entityAId).isNotNull();
        assertThat(entityBId).isNotNull();

        assertThat(parentRepos.existsById(entityAId)).isTrue();
        assertThat(childRepos.existsById(entityBId)).isTrue();
        assertThat(childRepos.findById(entityBId).get().getParentId()).isEqualTo(entityAId);
    }

    @Test
    @Transactional
    void save_성공_A가_같은_요소를_동시에_두개_넣기_동작확인() {
        // given
        UUID prepareEntityAId = UUID.randomUUID();
        Parent parent = new Parent();
        parent.setId(prepareEntityAId);
        parent.setData(123);

        Child child = new Child();
        child.setData(456);

        Child child2 = new Child();
        child2.setData(789);

        Entities entities = new Entities(parent, child);
        Entities entities2 = new Entities(parent, child2);

        // when
        Entities saved = coordinatorService.save(entities);
        Entities saved2 = coordinatorService.save(entities2);

        // then
        UUID entityAId = saved.getParent().getId();
        UUID entityBId = saved.getChild().getId();

        UUID entityA2Id = saved2.getParent().getId();
        UUID entityB2Id = saved2.getChild().getId();

        assertThat(entityAId).isNotNull();
        assertThat(entityBId).isNotNull();
        assertThat(entityB2Id).isNotNull();

        assertThat(entityA2Id).isEqualTo(entityA2Id);
        assertThat(entityAId).isEqualTo(prepareEntityAId);
        assertThat(entityA2Id).isEqualTo(prepareEntityAId);

        assertThat(parentRepos.existsById(entityAId)).isTrue();
        assertThat(childRepos.existsById(entityBId)).isTrue();
        assertThat(childRepos.existsById(entityB2Id)).isTrue();
        assertThat(childRepos.findById(entityBId).get().getParentId()).isEqualTo(entityAId);
    }

    @Test
    @Transactional
    void delete_정상삭제_동작확인() {
        // given
        Parent parent = new Parent();
        parent.setData(999);

        Child child = new Child();
        child.setData(111);

        Entities entities = new Entities(parent, child);
        Entities saved = coordinatorService.save(entities);

        UUID aId = saved.getParent().getId();
        UUID bId = saved.getChild().getId();

        // when
        coordinatorService.delete(saved);

        // then
        assertThat(childRepos.existsById(bId)).isFalse();      // B는 삭제됨
        assertThat(parentRepos.existsById(aId)).isFalse();      // 연결된 B 없으니 A도 삭제됨
    }

    @Test
    @Transactional
    void delete_EntityB_남아있으면_EntityA_삭제되지않음() {
        // given
        Parent parent = new Parent();
        parent.setData(888);
        UUID aId = UUID.randomUUID();
        parent.setId(aId);

        Child child1 = new Child();
        child1.setData(111);
        child1.setParentId(aId);

        Child child2 = new Child();
        child2.setData(222);
        child2.setParentId(aId);

        Entities saved1 = coordinatorService.save(new Entities(parent, child1));
        Child savedB2 = childRepos.save(child2);

        // when: B 하나만 삭제
        coordinatorService.delete(new Entities(saved1.getParent(), saved1.getChild()));

        // then
        assertThat(childRepos.existsById(savedB2.getId())).isTrue();    // 다른 B는 남아있음
        assertThat(parentRepos.existsById(aId)).isTrue();                // A는 삭제되면 안 됨
    }

    @Test
    void save_중간에에러나면_모두롤백됨() {
        // given
        UUID id = UUID.randomUUID();
        Parent parent = new Parent();
        parent.setId(id);
        parent.setData(11);

        Child child = new Child();
        child.setId(UUID.randomUUID());
        child.setParentId(id);
        child.setData(22);

        // when
        assertThatThrownBy(() -> {
            coordinatorService.save(new Entities(parent, throwOnGet(child)));
        }).isInstanceOf(RuntimeException.class);

        // then
        assertThat(parentRepos.findById(id)).isEmpty(); // 롤백됐어야 함
        assertThat(childRepos.findById(child.getId())).isEmpty();
    }

    @Test
    void delete_중간에에러나면_모두롤백됨() {
        // given
        UUID aId = UUID.randomUUID();

        Parent parent = new Parent();
        parent.setId(aId);
        parent.setData(100);

        Child child = new Child();
        child.setParentId(aId);
        child.setData(200);

        parentRepos.save(parent);
        childRepos.save(child);

        // when

        Entities entities = new Entities(parent, throwOnGet(child));

        // 예외 발생을 위해 CoordinatorService 내부 existsByEntityAId 호출에서 예외 발생시킴
        assertThatThrownBy(() -> {
            coordinatorService.delete(entities);
        }).isInstanceOf(RuntimeException.class);

        // then: 둘 다 롤백돼야 함
        assertThat(parentRepos.findById(aId)).isPresent();
        assertThat(childRepos.findById(entities.getChild().getId())).isPresent();
    }

    // get 메소드 사용시 에러나는 EntityB 객체
    private Child throwOnGet(Child origin) {
        return new Child() {
            @Override
            public Integer getData() {
                throw new RuntimeException("일부러 터트림");
            }

            @Override
            public UUID getId() {
                return origin.getId();
            }

            @Override
            public UUID getParentId() {
                throw new RuntimeException("일부러 터트림");
            }
        };
    }
}
