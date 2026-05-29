package com.scholarlyapps.pathlingo.data.remote

import android.content.Context
import com.scholarlyapps.pathlingo.BuildConfig
import com.scholarlyapps.pathlingo.data.local.TokenStore
import com.scholarlyapps.pathlingo.data.repo.AuthRepository
import com.scholarlyapps.pathlingo.data.repo.CatalogRepository
import com.scholarlyapps.pathlingo.data.repo.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

    @Volatile private var initialized = false
    lateinit var tokenStore: TokenStore
        private set
    lateinit var api: ApiService
        private set
    lateinit var authRepository: AuthRepository
        private set
    lateinit var catalogRepository: CatalogRepository
        private set
    lateinit var userRepository: UserRepository
        private set

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            val appContext = context.applicationContext
            tokenStore = TokenStore(appContext)

            val logging = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(tokenStore))
                .addInterceptor(logging)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            api = retrofit.create(ApiService::class.java)
            authRepository = AuthRepository(api, tokenStore)
            catalogRepository = CatalogRepository(api)
            userRepository = UserRepository(api)
            initialized = true
        }
    }
}
