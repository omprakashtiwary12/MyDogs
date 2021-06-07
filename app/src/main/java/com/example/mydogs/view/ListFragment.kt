package com.example.mydogs.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mydogs.R
import com.example.mydogs.model.DogDatabase
import com.example.mydogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        GlobalScope.launch{
            val dogs = DogDatabase(activity!!.applicationContext).dogDao().getAllDogs()
            if (dogs == null){
                fetchRemoteData()
            }
        }


        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter

        }


        refreshLayout.setOnRefreshListener {
          dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.GONE
            viewModel.refreshByPassCache()
            refreshLayout.isRefreshing = false
        }
        observeViewModel()
    }

    private fun fetchRemoteData() {
        viewModel.dogs.observe(viewLifecycleOwner, Observer {
            dogsListAdapter.setDogData(it)
        })
    }

    fun observeViewModel(){
         viewModel.dogs.observe(viewLifecycleOwner, Observer {
             dogs ->
             dogs?.let {
               dogsList.visibility = View.VISIBLE
                 dogsListAdapter.updateDogList(dogs)
             }
         })
         viewModel.dogsLoadError.observe(viewLifecycleOwner, Observer {
             isError ->
             isError?.let {
                 listError.visibility =  if (it) View.VISIBLE else View.GONE
             }
         })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    dogsList.visibility = View.GONE

                }
            }
        })
     }
}
