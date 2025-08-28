package com.quangtrader.cryptoportfoliotracker.inject

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    companion object {
        const val APP_NAME = "app name"
    }

    @Provides
    @Singleton
    fun provideContext(): Context = App.app

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences =
        RxSharedPreferences.create(sharedPreferences)


//    @Provides
//    @Singleton
//    fun provideOpenAiApi(): PixabayApi {
//        val loggingInterceptor = HttpLoggingInterceptor { message ->
//            Timber.tag("PixabayAPI").e(message)
//        }
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//        val networkInterceptor = Interceptor {
//            val request = it.request().newBuilder().build()
//            it.proceed(request)
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val requestBuilder = chain
//                    .request()
//                    .newBuilder()
//
//                chain.proceed(requestBuilder.build())
//            }
//            .addInterceptor(loggingInterceptor)
//            .addNetworkInterceptor(networkInterceptor)
//            .addNetworkInterceptor(StethoInterceptor())
//            .hostnameVerifier { _, _ -> true }
//            .retryOnConnectionFailure(false)
//            .connectTimeout(2, TimeUnit.MINUTES)
//            .writeTimeout(2, TimeUnit.MINUTES)
//            .readTimeout(2, TimeUnit.MINUTES)
//            .build()
//
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//
//        return retrofit.create(PixabayApi::class.java)
//    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_NAME,
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

//    @Provides
//    @Singleton
//    fun provideDao(db: AppDatabase): QrCodeService = db.dao()


}