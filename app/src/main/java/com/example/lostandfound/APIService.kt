package com.example.lostandfound


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=	AAAAmOgo7iI:APA91bEQu75Ko_06DiaCe5MAqcNzToKGig4zIShpPxAx6U5MBEwuyEe6HwVOrCzIogh9VtLKXf07aVyjeSkvrcfcluRHmOw9ZEO5dlB3HY9K-1yVC7wEpz9jdNLxF54cmAQGObhC-YTz"
    )
    @POST("fcm/send")
    fun sendNotifcation(@Body body: NotificationSender?): Call<MyResponse?>?
}