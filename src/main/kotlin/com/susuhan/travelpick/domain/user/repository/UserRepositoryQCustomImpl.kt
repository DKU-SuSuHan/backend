package com.susuhan.travelpick.domain.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.user.entity.QUser.user
import com.susuhan.travelpick.domain.user.entity.User
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : UserRepositoryQCustom {

    override fun existsNotDeletedUserByNickname(nickname: String): Boolean {
        return jpaQueryFactory
            .selectOne()
            .from(user)
            .where(
                user.nickname.eq(nickname),
                user.deleteAt.isNull,
            )
            .fetchFirst() != null
    }

    override fun findNotDeletedUser(id: Long): User? {
        return jpaQueryFactory
            .select(user)
            .from(user)
            .where(
                user.id.eq(id),
                user.deleteAt.isNull,
            )
            .fetchOne()
    }

    override fun findNotDeletedUserByNickname(nickname: String): User? {
        return jpaQueryFactory
            .select(user)
            .from(user)
            .where(
                user.nickname.eq(nickname),
                user.deleteAt.isNull,
            )
            .fetchOne()
    }

    override fun findNotDeletedUserBySocialId(socialId: String): User? {
        return jpaQueryFactory
            .select(user)
            .from(user)
            .where(
                user.socialId.eq(socialId),
                user.deleteAt.isNull,
            )
            .fetchOne()
    }

    override fun findAllNotDeletedUserById(ids: Set<Long>): List<User> {
        return jpaQueryFactory
            .select(user)
            .from(user)
            .where(
                user.id.`in`(ids),
                user.deleteAt.isNull,
            )
            .fetch()
    }
}
