package com.example.webapp.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToDo is a Querydsl query type for ToDo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToDo extends EntityPathBase<ToDo> {

    private static final long serialVersionUID = -1202910943L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToDo toDo = new QToDo("toDo");

    public final StringPath category = createString("category");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> doneAt = createDateTime("doneAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> planningDate = createDateTime("planningDate", java.time.LocalDateTime.class);

    public final EnumPath<ToDo.TaskStatus> status = createEnum("status", ToDo.TaskStatus.class);

    public final EnumPath<ToDo.TaskPriority> taskPriority = createEnum("taskPriority", ToDo.TaskPriority.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QToDo(String variable) {
        this(ToDo.class, forVariable(variable), INITS);
    }

    public QToDo(Path<? extends ToDo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToDo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToDo(PathMetadata metadata, PathInits inits) {
        this(ToDo.class, metadata, inits);
    }

    public QToDo(Class<? extends ToDo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

