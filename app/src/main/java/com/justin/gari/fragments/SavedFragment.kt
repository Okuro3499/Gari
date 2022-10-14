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
import com.justin.gari.adapters.SavedCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.models.saveCarModels.SavedCarResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    private val sharedPrefFile = "sharedPrefData"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val clientId = sharedPreferences?.getString("client_id", "default")

        apiClient = ApiClient
        context?.let {
            apiClient.getApiService(it).getSavedCars(clientId).enqueue(object : Callback<SavedCarResponse> {
                    override fun onResponse(call: Call<SavedCarResponse>, response: Response<SavedCarResponse>
                    ) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
//                        if (response.isSuccessful) { recyclerview.apply {
//                                layoutManager = LinearLayoutManager(context)
//                                adapter = SavedCarAdapter(response.body()!!.saved_cars, context)
//                            }
//                        }
                    }

                    override fun onFailure(call: Call<SavedCarResponse>, t: Throwable) {
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }

//        swipeRefresh.setOnRefreshListener {
//            apiClient = ApiClient
//            context?.let {
//                apiClient.getApiService(it).getSavedCars(clientId)
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
