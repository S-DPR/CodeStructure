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

@SpringBootTest
@Transactional
class EntityCoordinatorServiceTest {

    @Autowired
    EntityCoordinatorService coordinatorService;

    @Autowired
    EntityARepos entityARepos;

    @Autowired
    EntityBRepos entityBRepos;

    @Test
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
}
