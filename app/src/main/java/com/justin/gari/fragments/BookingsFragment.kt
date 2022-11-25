package com.justin.gari.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.justin.gari.adapters.BookingCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.FragmentBookingsBinding
import com.justin.gari.models.bookingCarModels.BookingsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsFragment : Fragment() {
    private lateinit var apiClient: ApiClient
    private val sharedPrefFile = "sharedPrefData"
    private var binding: FragmentBookingsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false);
        return binding!!.root;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString("user_id", "")
        binding?.shimmerLayout?.startShimmer();

        apiClient = ApiClient
        context?.let {
            apiClient.getApiService(it).getBookedCars(userId).enqueue(object : Callback<BookingsResponse> {
                override fun onResponse(call: Call<BookingsResponse>, response: Response<BookingsResponse>) {
                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        val bookingAdapter = BookingCarAdapter(response.body()!!.myBooked_cars, context!!)
                        binding?.shimmerLayout?.stopShimmer()
                        binding?.shimmerLayout?.visibility = View.GONE
                        binding?.recyclerview?.adapter = bookingAdapter
//                      recyclerview.apply {
//                        layoutManager = LinearLayoutManager(context)
//                        adapter = BookingCarAdapter(response.body()!!.myBooked_cars, context)
//                      }
                    }
                }

                override fun onFailure(call: Call<BookingsResponse>, t: Throwable) {
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
//                apiClient.getApiService(it).getBookedCars(userId).enqueue(object : Callback<BookingsResponse> {
//                        override fun onResponse(call: Call<BookingsResponse>, response: Response<BookingsResponse>) {
//                            if (response.isSuccessful) {
//                                Log.e("Gideon", "onSuccess: ${response.body()}")
//                                recyclerview.apply {
//                                    layoutManager = LinearLayoutManager(context)
//                                    adapter = BookingCarAdapter(response.body()!!.myBooked_cars, context)
//                                }
//                            }
//                        }
//
//                        override fun onFailure(call: Call<BookingsResponse>, t: Throwable) {
//                            Log.e("Gideon", "onFailure: ${t.message}")
//                        }
//                    })
//            }
//        }
    }
}
