package com.example.sampleinventry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DeleteCategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryDao = CategoryDatabase.getDatabase(requireContext()).categoryDao()
        val repository = CategoryRepository(categoryDao)
        val factory = CategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]
        categoryRecyclerView = view.findViewById(R.id.recycler_view)

        // Setup RecyclerView
        categoryAdapter = CategoryAdapter(
            onDeleteClick = { category ->
                categoryViewModel.deleteCategory(category)
                Toast.makeText(context, "Category deleted!", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { category ->
                // Navigate to AddEditCategoryFragment with category to edit
                val bundle = Bundle().apply {
                    putParcelable("category", category) // Pass the category to edit
                }
                val fragment = AddEditCategoryFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        categoryRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Observe the category list
        categoryViewModel.allCategories.observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoryAdapter.submitList(it)
            }
        })
    }
}
