//package com.justin.gari.fragments
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.justin.gari.R
//import com.justin.gari.adapters.BookingCarAdapter
//import com.justin.gari.api.ApiClient
//import com.justin.gari.models.bookingCarModels.BookingsResponse
//import kotlinx.android.synthetic.main.activity_main.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class BookingsFragment : Fragment() {
//    private lateinit var apiClient: ApiClient
//    private val sharedPrefFile = "sharedPrefData"
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_bookings, container, false)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val clientId = sharedPreferences?.getString("client_id", "default")
//
//        apiClient = ApiClient
//        context?.let {
//            apiClient.getApiService(it).getBookedCars(clientId).enqueue(object : Callback<BookingsResponse> {
//                    override fun onResponse(call: Call<BookingsResponse>, response: Response<BookingsResponse>) {
//                        if (response.isSuccessful) {
//                            Log.e("Gideon", "onSuccess: ${response.body()}")
//                            recyclerview.apply {
//                                layoutManager = LinearLayoutManager(context)
//                                adapter =
//                                    BookingCarAdapter(response.body()!!.myBooked_cars, context)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<BookingsResponse>, t: Throwable) {
//                        Log.e("Gideon", "onFailure: ${t.message}")
//                    }
//                })
//        }
////        swipeRefresh.setOnRefreshListener {
////            apiClient = ApiClient
////            context?.let {
////                apiClient.getApiService(it).getBookedCars(clientId).enqueue(object : Callback<BookingsResponse> {
////                        override fun onResponse(call: Call<BookingsResponse>, response: Response<BookingsResponse>) {
////                            if (response.isSuccessful) {
////                                Log.e("Gideon", "onSuccess: ${response.body()}")
////                                recyclerview.apply {
////                                    layoutManager = LinearLayoutManager(context)
////                                    adapter = BookingCarAdapter(response.body()!!.myBooked_cars, context)
////                                }
////                            }
////                        }
////
////                        override fun onFailure(call: Call<BookingsResponse>, t: Throwable) {
////                            Log.e("Gideon", "onFailure: ${t.message}")
////                        }
////                    })
////            }
////        }
//    }
//}
