package com.pcechz.getmega.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pcechz.getmega.R
import com.pcechz.getmega.data.MyInjection
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.databinding.FragmentHomeBinding
import com.pcechz.getmega.ui.adapter.RepoAdapter
import com.pcechz.getmega.ui.adapter.RepoLoadState
import com.pcechz.getmega.ui.viewModel.RepoViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.util.NotificationLite.disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.repo_load_state.*
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val TAG = "fragment"
        fun newInstance() = HomeFragment()
    }

    private var total: Int = 0
    private val mDisposable = CompositeDisposable()
    private lateinit var repoModel: RepoViewModel
    private lateinit var repoAdapter: RepoAdapter


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        _binding = FragmentHomeBinding.bind(view)
        binding.srefresh.setOnRefreshListener(this)
        repoModel = ViewModelProvider(this, MyInjection.provideRxRemoteViewModel(view.context)).get(
            RepoViewModel::class.java)

        binding.apply {

            recycler.apply {
                layoutManager = LinearLayoutManager(activity)
                repoAdapter = RepoAdapter()
                adapter = repoAdapter
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = repoAdapter.withLoadStateHeaderAndFooter(
                    header = RepoLoadState { repoAdapter.retry() },
                    footer = RepoLoadState { repoAdapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            retryButton.setOnClickListener {
                repoAdapter.retry()
            }
        }





        repoAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading)
            {
                binding.shimmerContainer.visibility = View.VISIBLE
                binding.shimmerContainer.startShimmer()
            }else{
                binding.shimmerContainer.visibility = View.GONE
                binding.shimmerContainer.stopShimmer()
            }
            binding.apply {
                //shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
                recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                noNetImg.isVisible = loadState.source.refresh is LoadState.Error
                alientText.isVisible = loadState.source.refresh is LoadState.Error
                somethingWrong.isVisible = loadState.source.refresh is LoadState.Error

                // no results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    repoAdapter.itemCount < 1
                ) {
                    recycler.isVisible = false
                } else {
                }
            }
        }

        setHasOptionsMenu(true)


        mDisposable.add(repoModel.getRepo().subscribe {
            repoAdapter.submitData(lifecycle, it)
        })

    }

    // method to sort repo list by names using comparator and notify adapter
    fun sortByNames() {
        // first check if there is data in list
       repoModel.getRepositoryLiveData().toString();
    }

//    // method to sort repo list by stars using comparator and notify adapter
//    fun sortByStars() {
//        // first check if there is data in list
//        viewModel.getRepositoryLiveData().value?.let {
//            if (!it.data.isNullOrEmpty()) {
//                // sort in ascending
//                Collections.sort(it.data,
//                    kotlin.Comparator { t1, t2 -> t1.stars.toInt().compareTo(t2.stars.toInt()) })
//                listAdapter.setRepositories(it.data)
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDisposable.dispose()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //  binding.shimmerContainer.startShimmer()
    }



    override fun onRefresh() {

    }





}



