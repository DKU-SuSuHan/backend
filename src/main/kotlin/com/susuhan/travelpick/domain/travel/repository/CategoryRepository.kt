package com.susuhan.travelpick.domain.travel.repository

import com.susuhan.travelpick.domain.travel.entity.Category
import org.springframework.data.repository.Repository

interface CategoryRepository : Repository<Category, Long> {

    fun save(entity: Category): Category

    fun findById(categoryId: Long) : Category
}