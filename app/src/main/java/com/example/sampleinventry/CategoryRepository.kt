package com.example.sampleinventry

import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    suspend fun update(category: Category) {
        categoryDao.update(category)
    }

    // Return LiveData instead of List
    fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }
}
