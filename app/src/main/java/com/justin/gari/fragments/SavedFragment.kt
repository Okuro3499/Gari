package com.justin.gari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.justin.gari.R
import com.justin.gari.adapters.SavedCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.models.saveCarModels.SavedCarResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerview)
        val shimmerFrameLayout = view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout);
        shimmerFrameLayout?.startShimmer();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeRefresh = view?.findViewById(R.id.swipeRefresh)!!
        val shimmerFrameLayout = view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout);
        shimmerFrameLayout?.startShimmer();

        apiClient = ApiClient
        context?.let {
            apiClient.getApiService(it).getSavedCars("1").enqueue(object : Callback<SavedCarResponse> {
                override fun onResponse(call: Call<SavedCarResponse>, response: Response<SavedCarResponse>) {
                    if (response.isSuccessful) {
                        recyclerview.apply {
                            shimmerFrameLayout?.stopShimmer();
                            shimmerFrameLayout?.visibility = View.GONE;
                            layoutManager = LinearLayoutManager(context)
                            adapter = SavedCarAdapter(response.body()!!.saved_cars, context)
                        }
                    }
                }

                override fun onFailure(call: Call<SavedCarResponse>, t: Throwable) {
                    shimmerFrameLayout?.stopShimmer();
                    shimmerFrameLayout?.visibility = View.GONE;
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
        }

        swipeRefresh.setOnRefreshListener {
            apiClient = ApiClient
            context?.let {
                apiClient.getApiService(it).getSavedCars("1").enqueue(object : Callback<SavedCarResponse> {
                    override fun onResponse(call: Call<SavedCarResponse>, response: Response<SavedCarResponse>) {
                        if (response.isSuccessful) {
                            recyclerview.apply {
                                shimmerFrameLayout?.stopShimmer();
                                shimmerFrameLayout?.visibility = View.GONE;
                                layoutManager = LinearLayoutManager(context)
                                adapter = SavedCarAdapter(response.body()!!.saved_cars, context)
                            }
                        }
                    }
                    override fun onFailure(call: Call<SavedCarResponse>, t: Throwable) {
                        shimmerFrameLayout?.stopShimmer();
                        shimmerFrameLayout?.visibility = View.GONE;
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
            }
        }
    }
}