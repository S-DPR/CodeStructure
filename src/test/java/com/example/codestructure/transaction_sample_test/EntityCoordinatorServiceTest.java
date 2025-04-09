//package com.example.codestructure.transaction_sample_test;
//
//import com.example.codestructure.transaction_sample.dto.Entities;
//import com.example.codestructure.transaction_sample.entity.EntityA;
//import com.example.codestructure.transaction_sample.entity.EntityB;
//import com.example.codestructure.transaction_sample.repos.EntityARepos;
//import com.example.codestructure.transaction_sample.repos.EntityBRepos;
//import com.example.codestructure.transaction_sample.service.EntityCoordinatorService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class EntityCoordinatorServiceTest {
//
//    @Autowired
//    EntityCoordinatorService coordinatorService;
//
//    @Autowired
//    EntityARepos entityARepos;
//
//    @Autowired
//    EntityBRepos entityBRepos;
//
//    @Test
//    void save_성공_트랜잭션_동작확인() {
//        // given
//        EntityA entityA = new EntityA();
//        entityA.setData(123);
//
//        EntityB entityB = new EntityB();
//        entityB.setData(456);
//
//        Entities entities = new Entities(entityA, entityB);
//
//        // when
//        Entities saved = coordinatorService.save(entities);
//
//        // then
//        UUID entityAId = saved.getEntityA().getId();
//        UUID entityBId = saved.getEntityB().getId();
//
//        assertThat(entityAId).isNotNull();
//        assertThat(entityBId).isNotNull();
//
//        assertThat(entityARepos.existsById(entityAId)).isTrue();
//        assertThat(entityBRepos.existsById(entityBId)).isTrue();
//        assertThat(entityBRepos.findById(entityBId).get().getEntityAId()).isEqualTo(entityAId);
//    }
//}
