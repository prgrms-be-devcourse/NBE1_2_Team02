package com.example.book_your_seat.concert.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeConcert is a Querydsl query type for LikeConcert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeConcert extends EntityPathBase<LikeConcert> {

    private static final long serialVersionUID = 722850671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeConcert likeConcert = new QLikeConcert("likeConcert");

    public final QConcert concert;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.book_your_seat.user.domain.QUser user;

    public QLikeConcert(String variable) {
        this(LikeConcert.class, forVariable(variable), INITS);
    }

    public QLikeConcert(Path<? extends LikeConcert> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeConcert(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeConcert(PathMetadata metadata, PathInits inits) {
        this(LikeConcert.class, metadata, inits);
    }

    public QLikeConcert(Class<? extends LikeConcert> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.concert = inits.isInitialized("concert") ? new QConcert(forProperty("concert")) : null;
        this.user = inits.isInitialized("user") ? new com.example.book_your_seat.user.domain.QUser(forProperty("user")) : null;
    }

}

