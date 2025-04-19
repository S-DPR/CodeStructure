package com.example.codestructure.transaction_sample_test;

import com.example.codestructure.transaction_sample.dto.Entities;
import com.example.codestructure.transaction_sample.entity.EntityA;
import com.example.codestructure.transaction_sample.entity.EntityB;
import com.example.codestructure.transaction_sample.repos.EntityARepos;
import com.example.codestructure.transaction_sample.repos.EntityBRepos;
import com.example.codestructure.transaction_sample.service.EntityCoordinatorService;
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
    EntityCoordinatorService coordinatorService;

    @Autowired
    EntityARepos entityARepos;

    @Autowired
    EntityBRepos entityBRepos;

    @Test
    @Transactional
    void save_성공_동시에_넣기_동작확인() {
        // given
        EntityA entityA = new EntityA();
        entityA.setData(123);

        EntityB entityB = new EntityB();
        entityB.setData(456);

        Entities entities = new Entities(entityA, entityB);

        // when
        Entities saved = coordinatorService.save(entities);

        // then
        UUID entityAId = saved.getEntityA().getId();
        UUID entityBId = saved.getEntityB().getId();

        assertThat(entityAId).isNotNull();
        assertThat(entityBId).isNotNull();

        assertThat(entityARepos.existsById(entityAId)).isTrue();
        assertThat(entityBRepos.existsById(entityBId)).isTrue();
        assertThat(entityBRepos.findById(entityBId).get().getEntityAId()).isEqualTo(entityAId);
    }

    @Test
    @Transactional
    void save_성공_A가_같은_요소를_동시에_두개_넣기_동작확인() {
        // given
        UUID prepareEntityAId = UUID.randomUUID();
        EntityA entityA = new EntityA();
        entityA.setId(prepareEntityAId);
        entityA.setData(123);

        EntityB entityB = new EntityB();
        entityB.setData(456);

        EntityB entityB2 = new EntityB();
        entityB2.setData(789);

        Entities entities = new Entities(entityA, entityB);
        Entities entities2 = new Entities(entityA, entityB2);

        // when
        Entities saved = coordinatorService.save(entities);
        Entities saved2 = coordinatorService.save(entities2);

        // then
        UUID entityAId = saved.getEntityA().getId();
        UUID entityBId = saved.getEntityB().getId();

        UUID entityA2Id = saved2.getEntityA().getId();
        UUID entityB2Id = saved2.getEntityB().getId();

        assertThat(entityAId).isNotNull();
        assertThat(entityBId).isNotNull();
        assertThat(entityB2Id).isNotNull();

        assertThat(entityA2Id).isEqualTo(entityA2Id);
        assertThat(entityAId).isEqualTo(prepareEntityAId);
        assertThat(entityA2Id).isEqualTo(prepareEntityAId);

        assertThat(entityARepos.existsById(entityAId)).isTrue();
        assertThat(entityBRepos.existsById(entityBId)).isTrue();
        assertThat(entityBRepos.existsById(entityB2Id)).isTrue();
        assertThat(entityBRepos.findById(entityBId).get().getEntityAId()).isEqualTo(entityAId);
    }

    @Test
    @Transactional
    void delete_정상삭제_동작확인() {
        // given
        EntityA entityA = new EntityA();
        entityA.setData(999);

        EntityB entityB = new EntityB();
        entityB.setData(111);

        Entities entities = new Entities(entityA, entityB);
        Entities saved = coordinatorService.save(entities);

        UUID aId = saved.getEntityA().getId();
        UUID bId = saved.getEntityB().getId();

        // when
        coordinatorService.delete(saved);

        // then
        assertThat(entityBRepos.existsById(bId)).isFalse();      // B는 삭제됨
        assertThat(entityARepos.existsById(aId)).isFalse();      // 연결된 B 없으니 A도 삭제됨
    }

    @Test
    @Transactional
    void delete_EntityB_남아있으면_EntityA_삭제되지않음() {
        // given
        EntityA entityA = new EntityA();
        entityA.setData(888);
        UUID aId = UUID.randomUUID();
        entityA.setId(aId);

        EntityB entityB1 = new EntityB();
        entityB1.setData(111);
        entityB1.setEntityAId(aId);

        EntityB entityB2 = new EntityB();
        entityB2.setData(222);
        entityB2.setEntityAId(aId);

        Entities saved1 = coordinatorService.save(new Entities(entityA, entityB1));
        EntityB savedB2 = entityBRepos.save(entityB2);

        // when: B 하나만 삭제
        coordinatorService.delete(new Entities(saved1.getEntityA(), saved1.getEntityB()));

        // then
        assertThat(entityBRepos.existsById(savedB2.getId())).isTrue();    // 다른 B는 남아있음
        assertThat(entityARepos.existsById(aId)).isTrue();                // A는 삭제되면 안 됨
    }

    @Test
    void save_중간에에러나면_모두롤백됨() {
        // given
        UUID id = UUID.randomUUID();
        EntityA entityA = new EntityA();
        entityA.setId(id);
        entityA.setData(11);

        EntityB entityB = new EntityB();
        entityB.setId(UUID.randomUUID());
        entityB.setEntityAId(id);
        entityB.setData(22);

        // when
        assertThatThrownBy(() -> {
            coordinatorService.save(new Entities(entityA, throwOnGet(entityB)));
        }).isInstanceOf(RuntimeException.class);

        // then
        assertThat(entityARepos.findById(id)).isEmpty(); // 롤백됐어야 함
        assertThat(entityBRepos.findById(entityB.getId())).isEmpty();
    }

    @Test
    void delete_중간에에러나면_모두롤백됨() {
        // given
        UUID aId = UUID.randomUUID();

        EntityA entityA = new EntityA();
        entityA.setId(aId);
        entityA.setData(100);

        EntityB entityB = new EntityB();
        entityB.setEntityAId(aId);
        entityB.setData(200);

        entityARepos.save(entityA);
        entityBRepos.save(entityB);

        // when

        Entities entities = new Entities(entityA, throwOnGet(entityB));

        // 예외 발생을 위해 CoordinatorService 내부 existsByEntityAId 호출에서 예외 발생시킴
        assertThatThrownBy(() -> {
            coordinatorService.delete(entities);
        }).isInstanceOf(RuntimeException.class);

        // then: 둘 다 롤백돼야 함
        assertThat(entityARepos.findById(aId)).isPresent();
        assertThat(entityBRepos.findById(entities.getEntityB().getId())).isPresent();
    }

    // get 메소드 사용시 에러나는 EntityB 객체
    private EntityB throwOnGet(EntityB origin) {
        return new EntityB() {
            @Override
            public Integer getData() {
                throw new RuntimeException("일부러 터트림");
            }

            @Override
            public UUID getId() {
                return origin.getId();
            }

            @Override
            public UUID getEntityAId() {
                throw new RuntimeException("일부러 터트림");
            }
        };
    }
}
