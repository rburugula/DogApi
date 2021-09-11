package com.example.dogapi

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapi.adapter.RecyclerViewAdapter
import com.example.dogapi.databinding.FragmentCatBreedBinding
import com.example.dogapi.viewModel.MainActivityViewModel
import javax.inject.Inject

class CatBreedFragment : Fragment(),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: FragmentCatBreedBinding

    private lateinit var recyclerAdapter: RecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBreedBinding.inflate(inflater, container, false)
        val view = binding.root
        initView()
        initViewModel()
        return view
    }

    private fun initView() {
        binding.searchView.setOnQueryTextListener(this)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        recyclerAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.onItemClick = { catBreed ->
            val action =
                CatBreedFragmentDirections.actionCatBreedFragmentToCatBreedDetailFragment(catBreed)
            findNavController().navigate(action)
        }
    }

    private fun initViewModel() {
        viewModel.catBreedsLiveData.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                recyclerAdapter.setUpdatedData(it)
            }.onFailure {
                showErrrorMessage()
            }
        }
        viewModel.fetchCatBreeds()
    }

    private fun showErrrorMessage() {
        Toast.makeText(context, getString(R.string.error_connecting_to_server), Toast.LENGTH_LONG)
            .show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        recyclerAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        recyclerAdapter.filter.filter(newText)
        return false
    }


}