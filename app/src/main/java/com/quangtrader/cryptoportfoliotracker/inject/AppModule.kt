package com.quangtrader.cryptoportfoliotracker.inject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.GsonBuilder
import com.quangtrader.cryptoportfoliotracker.dao.CoinDao
import com.quangtrader.cryptoportfoliotracker.data.api.BinanceApi
import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.api.CoinMarketApi
import com.quangtrader.cryptoportfoliotracker.data.api.NewsApi
import com.quangtrader.cryptoportfoliotracker.data.api.NewsCryptoApi
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.data.api.BingXApi
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
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


    @Provides
    @Singleton
    fun provideOpenAiApi(): CoinMarketApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("CoinMarketAPI").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(CoinMarketApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOpenTrendingGecko(): CoinGeckoTrendingApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("CoinTrending").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_TRENDING)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(CoinGeckoTrendingApi::class.java)
    }


    @Provides
    @Singleton
    fun provideGetRealtimePrice(): BinanceApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("Realtime_DATA").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_BINANCE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(BinanceApi::class.java)
    }


    @Provides
    @Singleton
    fun provideGetNews(): NewsApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("Realtime_DATA").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_NEWS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(NewsApi::class.java)
    }


    @Provides
    @Singleton
    fun provideGetNewsByToken(): NewsCryptoApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("Realtime_DATA").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_NEWS_TOKEN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(NewsCryptoApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOpenBingX(): BingXApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("BingxApi").e(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_BingX)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(BingXApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_NAME,
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): CoinDao = db.dao()




    @Module
    @InstallIn(SingletonComponent::class)
    object GeminiModule {
        @SuppressLint("SecretInSource")
        @Provides
        @Singleton
        fun provideGeminiModel(): GenerativeModel {
            val config = generationConfig {
                temperature = 0.15f
                topK = 32
                topP = 1f
            }
            return GenerativeModel(
                modelName = "gemini-flash-latest",
                apiKey = Constants.API_GEMINI,
                generationConfig = config,
                systemInstruction = content {
                    text("Act as an elite Market Structure & Price Action Analyst. Analyze the provided chart image with high precision.\n" +
                            "\n" +
                            "MISSION:\n" +
                            "1. Identify Asset & Timeframe.\n" +
                            "2. Analyze Market Structure (HH/HL or LH/LL) and overall Trend.\n" +
                            "3. Pinpoint Key Levels (Support/Resistance, Supply/Demand Zones).\n" +
                            "4. Detect high-probability Candlestick Patterns at key zones.\n" +
                            "5. Formulate a professional Trading Signal based on price action logic.\n" +
                            "\n" +
                            "OUTPUT FORMAT (Return ONLY a raw JSON object. No conversational text, no markdown blocks, no explanations outside the JSON):\n" +
                            "{\n" +
                            "  \"asset\": \"{Name} | {TF}\",\n" +
                            "  \"structure\": \"{Brief Description}\",\n" +
                            "  \"trend\": \"{Bullish/Bearish/Neutral}\",\n" +
                            "  \"key_levels\": \"{Price levels}\",\n" +
                            "  \"candle_pattern\": \"{Pattern detected}\",\n" +
                            "  \"signal\": \"{BUY/SELL/WAIT}\",\n" +
                            "  \"signal_type\": \"{e.g., Bearish Reversal}\",\n" +
                            "  \"entry\": {NumericValue},\n" +
                            "  \"stop_loss\": {NumericValue},\n" +
                            "  \"tp1\": {NumericValue},\n" +
                            "  \"tp2\": {NumericValue},\n" +
                            "  \"risk_reward\": \"{Ratio}\",\n" +
                            "  \"confidence\": {1-100}\n" +
                            "}\n" +
                            "\n" +
                            "CRITICAL INSTRUCTIONS: \n" +
                            "- All price fields (entry, stop_loss, tp1, tp2) MUST be numbers only (e.g., 1.3415). \n" +
                            "- Do not use quotes for numeric values.\n" +
                            "- Do not include any text before or after the JSON.")
                }
            )
        }
    }






}