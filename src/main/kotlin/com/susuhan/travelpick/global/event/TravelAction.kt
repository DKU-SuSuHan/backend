package com.susuhan.travelpick.global.event

enum class TravelAction(val message: String) {

    CREATE_PLACE("님이 로드맵에 장소를 생성하였습니다."),
    DELETE_PLACE("님이 로드맵에 장소를 삭제하였습니다."),
    CREATE_VOTE("님이 투표를 생성하였습니다."),
    CLOSE_VOTE("님의 투표가 마감되었습니다."),
    ADD_MATE("님이 여행 메이트를 추가하였습니다."),
    DELETE_MATE("님이 여행 메이트를 삭제하였습니다."),
    CHANGE_LEADER("님이 여행 주도자를 위임하였습니다.")
}