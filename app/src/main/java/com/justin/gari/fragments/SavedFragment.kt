package com.justin.gari.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.justin.gari.R
import com.justin.gari.adapters.BookingCarAdapter
import com.justin.gari.adapters.SavedCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.FragmentBookingsBinding
import com.justin.gari.databinding.FragmentSavedBinding
import com.justin.gari.models.saveCarModels.SavedCarResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    private val sharedPrefFile = "sharedPrefData"
    private var binding: FragmentSavedBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString("user_id", "")

        apiClient = ApiClient
        context?.let {
            apiClient.getApiService(it).getSavedCars(userId).enqueue(object : Callback<SavedCarResponse> {
                override fun onResponse(call: Call<SavedCarResponse>, response: Response<SavedCarResponse>) {
                    Log.e("Gideon", "onSuccess: ${response.body()}")
                    val savedAdapter = SavedCarAdapter(response.body()!!.saved_cars, context!!)
                    binding?.shimmerLayout?.stopShimmer()
                    binding?.shimmerLayout?.visibility = View.GONE;
                    binding?.recyclerview?.adapter = savedAdapter
//                  if (response.isSuccessful) { recyclerview.apply {
//                      layoutManager = LinearLayoutManager(context)
//                      adapter = SavedCarAdapter(response.body()!!.saved_cars, context)
//                      }
//                    }
                }

                override fun onFailure(call: Call<SavedCarResponse>, t: Throwable) {
                    binding?.shimmerLayout?.stopShimmer()
                    binding?.shimmerLayout?.visibility = View.GONE
                    binding?.errorPage?.visibility = View.VISIBLE
                    binding?.message?.text  = t.message
                    binding?.swipeRefresh?.visibility = View.GONE
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
        }

//        swipeRefresh.setOnRefreshListener {
//            apiClient = ApiClient
//            context?.let {
//                apiClient.getApiService(it).getSavedCars(userId)
//                    .enqueue(object : Callback<SavedCarResponse> {
//                        override fun onResponse(
//                            call: Call<SavedCarResponse>,
//                            response: Response<SavedCarResponse>
//                        ) {
//                            Log.e("Gideon", "onSuccess: ${response.body()}")
//                            if (response.isSuccessful) {
//                                recyclerview.apply {
//                                    layoutManager = LinearLayoutManager(context)
//                                    adapter = SavedCarAdapter(response.body()!!.saved_cars, context)
//                                }
//                            }
//                        }
//
//                        override fun onFailure(call: Call<SavedCarResponse>, t: Throwable) {
//                            Log.e("Gideon", "onFailure: ${t.message}")
//                        }
//                    })
//            }
//        }
    }
}
