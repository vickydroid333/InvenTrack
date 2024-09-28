package com.example.sampleinventry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AddEditCategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryNameEditText: EditText
    private lateinit var saveButton: Button

    private var categoryToEdit: Category? = null // To store the category if editing

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveButton = view.findViewById(R.id.save_button)
        categoryNameEditText = view.findViewById(R.id.category_name)
        val categoryDao = CategoryDatabase.getDatabase(requireContext()).categoryDao()
        val repository = CategoryRepository(categoryDao)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

        // Check if there's a category to edit
        categoryToEdit = arguments?.getParcelable("category")

        // If editing, pre-fill the EditText with the category's name
        categoryToEdit?.let {
            categoryNameEditText.setText(it.name)
            saveButton.text = "Update Category" // Change button text to indicate editing
        }

        // Handle Add/Edit functionality
        saveButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()
            if (categoryName.isNotEmpty()) {
                if (categoryToEdit == null) {
                    // Add new category
                    val category = Category(name = categoryName)
                    categoryViewModel.insertCategory(category)
                    Toast.makeText(context, "Category added!", Toast.LENGTH_SHORT).show()
                } else {
                    // Update existing category
                    val updatedCategory = categoryToEdit!!.copy(name = categoryName)
                    categoryViewModel.updateCategory(updatedCategory)
                    Toast.makeText(context, "Category updated!", Toast.LENGTH_SHORT).show()
                }

                // Clear the EditText field after saving/updating
                categoryNameEditText.text.clear()

                // Navigate back to DeleteCategoryFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DeleteCategoryFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
