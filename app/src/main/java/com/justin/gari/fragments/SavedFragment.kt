package com.justin.gari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.justin.gari.adapters.SavedCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.FragmentSavedBinding
import com.justin.gari.models.saveCarModels.SavedCarResponse
import com.justin.gari.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    var pref: SharedPrefManager? = null
    private var binding: FragmentSavedBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = SharedPrefManager(requireActivity())
        val userId = pref!!.getUSERID()

        apiClient = ApiClient()
        context?.let {
            apiClient.getApiService().getSavedCars(userId)
                .enqueue(object : Callback<SavedCarResponse> {
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
                        binding?.message?.text = t.message
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
