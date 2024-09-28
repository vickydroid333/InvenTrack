package com.example.sampleinventry

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    // LiveData to hold the list of categories
    val allCategories: LiveData<List<Category>> = repository.getAllCategories()

    // Function to insert a new category
    fun insertCategory(category: Category) {
        viewModelScope.launch {
            repository.insert(category)
        }
    }

    // Function to delete a category
    fun deleteCategory(category: Category) {
        Log.d("DeleteCategory", "Deleting category: ${category.name}")
        viewModelScope.launch {
            repository.delete(category)
        }
    }

    // Function to update a category
    fun updateCategory(category: Category) {
        Log.d("DeleteCategory", "Deleting category: ${category.name}")
        viewModelScope.launch {
            repository.update(category)
        }
    }
}

// Factory to create an instance of the ViewModel with the CategoryRepository dependency
class CategoryViewModelFactory(private val repository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
