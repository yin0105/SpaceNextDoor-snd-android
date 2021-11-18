package com.spacenextdoor.ApolloGraphqlClient

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.spacenextdoor.model.UserInfoModel
import com.spacenextdoor.utils.Constant
import com.spacenextdoor.utils.PrefManger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class Apollo {

    public class AuthorizationInterceptor(val context: Context) : Interceptor {
        val user: UserInfoModel = PrefManger.getUser(context)!!

       private val authToken: String = user!!.accessToken.toString()
      //   private val authToken: String =  "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo4MzYsInJvbGVzIjpbIkNVU1RPTUVSIiwiUFJPVklERVIiXSwiaWF0IjoxNjE3MjU2ODI0LCJleHAiOjE2MTc4NjE2MjR9.FcKg2xD9vPGtwD8xQjP4Hali6JScXff0bPPDowZWxLI"


        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", authToken)
                .build()
            apolloClient(context)
            return chain.proceed(request)
        }

        fun apolloClient(context: Context): ApolloClient {
            return ApolloClient.builder()
                .serverUrl(Constant.BASE_URL)
                .okHttpClient(
                    OkHttpClient.Builder()
                        .addInterceptor(AuthorizationInterceptor(context))
                        .build()
                )
                .build()
        }
    }
}