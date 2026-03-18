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


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChartAnalystModel

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CryptoChatbotModel

    @Module
    @InstallIn(SingletonComponent::class)
    object GeminiModule {
        @SuppressLint("SecretInSource")
        @Provides
        @Singleton
        @ChartAnalystModel
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



        @SuppressLint("SecretInSource")
        @Provides
        @Singleton
        @CryptoChatbotModel
        fun provideGeminiModelChatBot(): GenerativeModel {
            val config = generationConfig {
                temperature = 0.15f
                topK = 32
                topP = 1f
            }
            return GenerativeModel(
                modelName = "gemini-flash-latest",
                apiKey = Constants.API_GEMINI_CHAT_BOT,
                generationConfig = config,
                systemInstruction = content {
                    text("Act as an elite Crypto Analyst combining Market Data + Technical Analysis + Fundamental Insights.\n" +
                            "\n" +
                            "Your task is to generate a professional crypto report with a clean UI-like format using icons, sections, and clear structure.\n" +
                            "\n" +
                            "Project Name: {PROJECT_NAME}\n" +
                            "Price: {PRICE}\n" +
                            "24h Volume: {VOLUME}\n" +
                            "Performance:\n" +
                            "- 1h: {PERF_1H}\n" +
                            "- 24h: {PERF_24H}\n" +
                            "- 7d: {PERF_7D}\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83D\uDE80 {PROJECT_NAME} ANALYSIS \uD83D\uDE80\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "\uD83D\uDCB0 Price: {PRICE}\n" +
                            "\uD83D\uDCCA Vol 24h: {VOLUME}\n" +
                            "\n" +
                            "⏱ Performance:\n" +
                            "• 1h: {PERF_1H}\n" +
                            "• 24h: {PERF_24H}\n" +
                            "• 7d: {PERF_7D}\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83D\uDE80 CRYPTO TRADING VERDICT \uD83D\uDE80\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "1. \uD83D\uDCCA Market Bias: (Bullish / Bearish / Neutral)\n" +
                            "\n" +
                            "2. \uD83C\uDFAF Execution Strategy: (Buy / Sell / No Trade)\n" +
                            "\n" +
                            "4. \uD83D\uDD25 Confidence Score: X/10\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83E\uDDE0 COMPREHENSIVE ANALYSIS\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "\uD83D\uDCCC Fundamental Overview:\n" +
                            "- Mission & purpose\n" +
                            "- Real-world relevance\n" +
                            "- Competitive positioning\n" +
                            "\n" +
                            "\uD83D\uDCCC Team & Backers:\n" +
                            "- Founder background\n" +
                            "- Investors / partners\n" +
                            "- Any red flags\n" +
                            "\n" +
                            "\uD83D\uDCCC Use Cases & Adoption:\n" +
                            "- Key ecosystem (DeFi, NFT, AI, etc.)\n" +
                            "- Actual adoption vs hype\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83D\uDCCA MARKET & TECHNICAL ANALYSIS\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "- Trend (Short-term + Long-term)\n" +
                            "- Volume & liquidity insight\n" +
                            "- Price behavior (trend / range / breakout)\n" +
                            "\n" +
                            "\uD83D\uDCCC Key Levels:\n" +
                            "- Support:\n" +
                            "- Resistance:\n" +
                            "\n" +
                            "\uD83D\uDCCC Price Action Insight:\n" +
                            "(Explain clearly what's happening — consolidation, breakout, etc.)\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "⚠\uFE0F RISKS & WEAKNESSES\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "- Technical risks\n" +
                            "- Market risks\n" +
                            "- Fundamental risks\n" +
                            "- Ecosystem risks\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83E\uDDE0 FINAL VERDICT\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "✅ Strengths:\n" +
                            "- ...\n" +
                            "\n" +
                            "❌ Weaknesses:\n" +
                            "- ...\n" +
                            "\n" +
                            "\uD83D\uDCC8 Long-term Potential: (High / Medium / Low)\n" +
                            "⚡ Short-term Outlook: (Bullish / Neutral / Bearish)\n" +
                            "\uD83D\uDC64 Suitable For: (Trader / Investor / Avoid)\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "\uD83D\uDCCA PROJECT SCORE\n" +
                            "━━━━━━━━━━━━━━━━━━\n" +
                            "\n" +
                            "- Technology: X/10\n" +
                            "- Adoption: X/10\n" +
                            "- Team: X/10\n" +
                            "- Investment Potential: X/10\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "⚠\uFE0F Output Formatting Rules (VERY STRICT):\n" +
                            "\n" +
                            "- Output must be CLEAN PLAIN TEXT (no markdown)\n" +
                            "- NEVER use: **, __, ##, [], (), backticks\n" +
                            "- DO NOT bold anything\n" +
                            "- DO NOT italic anything\n" +
                            "- DO NOT add markdown headings\n" +
                            "\n" +
                            "- Use ONLY:\n" +
                            "  ✔ Icons (emoji)\n" +
                            "  ✔ Capital letters for emphasis\n" +
                            "  ✔ Line breaks\n" +
                            "  ✔ Dashes (-) and bullets (•)\n" +
                            "\n" +
                            "- Format like a mobile app report UI\n" +
                            "- Keep spacing consistent and easy to read")
                }
            )
        }




    }















}