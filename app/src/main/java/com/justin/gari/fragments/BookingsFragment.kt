package com.justin.gari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.justin.gari.R
import com.justin.gari.adapters.BookingCarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.models.bookingCarModels.BookingsResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsFragment : Fragment() {
    private lateinit var apiClient: ApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookings, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shimmerFrameLayout = view?.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)
        shimmerFrameLayout?.startShimmer()

        apiClient = ApiClient
        context?.let {
            apiClient.getApiService(it).getBookedCars("1")
                .enqueue(object : Callback<BookingsResponse> {
                    override fun onResponse(
                        call: Call<BookingsResponse>,
                        response: Response<BookingsResponse>
                    ) {
//                        Log.e("Gideon", "onSuccess: $clientId")
                        if (response.isSuccessful) {

                            recyclerview.apply {
                                shimmerFrameLayout?.stopShimmer()
                                shimmerFrameLayout?.visibility = View.GONE
                                layoutManager = LinearLayoutManager(context)
                                adapter =
                                    BookingCarAdapter(response.body()!!.myBooked_cars, context)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BookingsResponse>, t: Throwable) {
                        shimmerFrameLayout?.stopShimmer()
                        shimmerFrameLayout?.visibility = View.GONE
                        //                Toast.makeText(this@MainActivity, "Check internet connectivity", Toast.LENGTH_LONG)
                        //                    .show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }
    }
}