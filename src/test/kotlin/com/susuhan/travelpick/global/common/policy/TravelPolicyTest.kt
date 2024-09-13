package com.susuhan.travelpick.global.common.policy

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.exception.TravelLeaderDeletionException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TravelPolicyTest : BehaviorSpec({

    Given("요청을 보낸 사용자가 여행 주도자인 데이터가 주어졌을 때") {
        val userId = 1L
        val leaderId = 1L

        When("여행 주도자인지 확인하면") {
            val result = TravelPolicy.isTravelLeader(userId, leaderId)

            Then("아무 값도 반환하지 않아야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("요청을 보낸 사용자가 여행 주도자가 아닌 데이터가 주어졌을 때") {
        val leaderId = 1L
        val userId = 2L

        When("여행 주도자인지 확인하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    TravelPolicy.isTravelLeader(userId, leaderId)
                }
            }
        }
    }

    Given("참여자 권한을 가진 여행 메이트 데이터가 주어졌을 때") {
        val travelMate = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::groupRole) { GroupRole.PARTICIPANT }
        }

        When("주도자 역할을 가진 여행 메이트의 삭제인지 확인하면") {
            val result = TravelPolicy.isTravelLeaderDeletion(travelMate)

            Then("아무 값도 반환하지 않아야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("주도자 권한을 가진 여행 메이트 데이터가 주어졌을 때") {
        val travelMate = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::groupRole) { GroupRole.LEADER }
        }

        When("주도자 역할을 가진 여행 메이트의 삭제인지 확인하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderDeletionException> {
                    TravelPolicy.isTravelLeaderDeletion(travelMate)
                }
            }
        }
    }
})
