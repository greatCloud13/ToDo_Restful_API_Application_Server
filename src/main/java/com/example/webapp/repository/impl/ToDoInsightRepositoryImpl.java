package com.example.webapp.repository.impl;

import com.example.webapp.DTO.BestProducibilityDateDTO;
import com.example.webapp.DTO.WarnInsightDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.ToDoInsightRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.webapp.entity.QUser.user;
import static com.example.webapp.entity.QToDo.toDo;

@RequiredArgsConstructor
public class ToDoInsightRepositoryImpl implements ToDoInsightRepository {

    //        Config에서 생성한 JPAQueryFactory 주입
    private final JPAQueryFactory queryFactory;

    @Override
    public BestProducibilityDateDTO getBestProducibilityDate(User user) {

        NumberExpression<Long> totalCount = toDo.count();

        NumberExpression<Long> completeCount =
                new CaseBuilder()
                        .when(toDo.status.eq(ToDo.TaskStatus.COMPLETE))
                        .then(1L)
                        .otherwise(0L)
                        .sum();

        NumberExpression<Long> completionRate =
                completeCount.multiply(100).divide(totalCount);

        return queryFactory
                .select(Projections.constructor(BestProducibilityDateDTO.class,
                        toDo.doneAt,
                        completionRate
                ))
                .from(toDo)
                .where(
                        toDo.doneAt.isNotNull(),
                        toDo.user.id.eq(user.getId())
                )
                // QueryDSL의 날짜 함수로 그룹화
                .groupBy(
                        toDo.doneAt.year(),
                        toDo.doneAt.month(),
                        toDo.doneAt.dayOfMonth()
                )
                .having(totalCount.goe(1L))
                .orderBy(completionRate.desc(), totalCount.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<WarnInsightDTO> getWarnInsight(User user) {
        return queryFactory
                .select(Projections.bean(WarnInsightDTO.class,
                        toDo.count().as("count"),
                        toDo.category.as("category")))
                .from(toDo)
                .where(toDo.user.id.eq(user.getId()),
                        toDo.doneAt.isNotNull(),
                        toDo.planningDate.before(LocalDateTime.now()))
                .groupBy(toDo.category)
                .fetch();
    }
}
