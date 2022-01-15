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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pcechz.getmega.R
import com.pcechz.getmega.data.MyInjection
import com.pcechz.getmega.data.api.RepoResponse
import com.pcechz.getmega.databinding.FragmentHomeBinding
import com.pcechz.getmega.ui.adapter.RepoAdapter
import com.pcechz.getmega.ui.adapter.RepoLoadState
import com.pcechz.getmega.ui.viewModel.RepoViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.util.NotificationLite.disposable
import kotlinx.android.synthetic.main.repo_load_state.*


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

        val adapter = RepoAdapter()


        binding.apply {

            recycler.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = RepoLoadState { adapter.retry() },
                    footer = RepoLoadState { adapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            retryButton.setOnClickListener {
                adapter.retry()
            }
        }





        adapter.addLoadStateListener { loadState ->
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
                    adapter.itemCount < 1
                ) {
                    recycler.isVisible = false
                } else {
                }
            }
        }

        setHasOptionsMenu(true)


        mDisposable.add(repoModel.getRepo().subscribe {
            adapter.submitData(lifecycle, it)
        })

    }

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



