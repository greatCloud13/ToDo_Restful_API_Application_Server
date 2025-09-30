package com.example.webapp.repository.impl;

import com.example.webapp.DTO.BestProducibilityDateDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.ToDoInsightRepository;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;

import static com.example.webapp.entity.QUser.user;
import static com.example.webapp.entity.QToDo.toDo;

@RequiredArgsConstructor
public class ToDoInsightRepositoryImpl implements ToDoInsightRepository {

    //        Config에서 생성한 JPAQueryFactory 주입
    private final JPAQueryFactory queryFactory;

    @Override
    public BestProducibilityDateDTO getBestProducibilityDate(User user) {

        // 총 갯수
        NumberExpression<Long> totalcount = toDo.count();

        // 완료 갯수
        NumberExpression<Long> completecount =
                new CaseBuilder()
                        .when(toDo.status.eq(ToDo.TaskStatus.COMPLETE))
                        .then(1L)
                        .otherwise(0L)
                        .sum();

        // 완료율
        NumberExpression<Long> completionRate =
                new CaseBuilder()
                        .when(totalcount.eq(0L).and(completecount.eq(0L))).then(0L)
                        .otherwise(completecount
                                .multiply(100)
                                .divide(totalcount));



        String dateString = toDo.doneAt.toString().substring(0,10);

        return queryFactory
                .select(Projections.fields(BestProducibilityDateDTO.class,
                        toDo.doneAt.stringValue().substring(0,10).as("date"),
                        completionRate.as("progress")
                ))
                .from(toDo)
                .where(toDo.doneAt.isNotNull(),
                        toDo.user.id.eq(user.getId()))
                .groupBy(toDo.doneAt.stringValue().substring(0,10))
                .orderBy(completecount.desc())
                .limit(1)
                .fetchOne();
    }
}
