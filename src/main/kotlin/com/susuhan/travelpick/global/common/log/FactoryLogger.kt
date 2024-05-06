package com.susuhan.travelpick.global.common.log

import org.slf4j.LoggerFactory

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!
