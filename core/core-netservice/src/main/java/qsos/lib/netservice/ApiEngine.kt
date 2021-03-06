package qsos.lib.netservice

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import qsos.core.lib.config.CoreConfig
import qsos.core.lib.utils.json.DateDeserializer
import qsos.core.lib.utils.json.DateSerializer
import qsos.lib.netservice.interceptor.AddCookiesInterceptor
import qsos.lib.netservice.interceptor.NetworkInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author 华清松
 * 使用Retrofit+OkHttp搭建网路请求框架
 */
object ApiEngine {

    private var mBuild: Retrofit.Builder
    private lateinit var mRetrofit: Retrofit
    private var mClient: OkHttpClient.Builder

    /**默认请求超时时长（秒）*/
    private const val mTimeOut: Long = 8L
    private val mGsonBuilder: GsonBuilder = GsonBuilder()
    private val mGsonConverterFactory: GsonConverterFactory
    /**是否已自定义配置*/
    private var mInit: Boolean = false

    init {
        // 配置请求时间解析，避免服务器返回的时间格式不一致导致的解析失败问题
        mGsonBuilder.registerTypeAdapter(Date::class.java, DateDeserializer()).setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        mGsonBuilder.registerTypeAdapter(Date::class.java, DateSerializer()).setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        mGsonConverterFactory = GsonConverterFactory.create(mGsonBuilder.create())

        mBuild = Retrofit.Builder()
                .baseUrl(CoreConfig.BASE_URL)
                .addConverterFactory(mGsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        mClient = OkHttpClient.Builder()
        // 请求超时设置
        mClient.connectTimeout(mTimeOut, TimeUnit.SECONDS)
        // 网络连接性拦截器
        mClient.addInterceptor(NetworkInterceptor())
        // COOKIE拦截器
        mClient.addInterceptor(AddCookiesInterceptor())

        if (CoreConfig.DEBUG) {
            // 日志拦截器
            mClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
    }

    /**自行配置网络请求设置，请调用此方法，注意全局调用一次即可，建议在Application.onCreate()中调用*/
    @SuppressWarnings
    fun init(mineConfig: IHttpConfig): ApiEngine {
        if (!mInit) {
            mBuild = mineConfig.getRetrofitBuilder()
            mClient = mineConfig.getOkHttpBuilder()
            mInit = true
            mRetrofit = mBuild.client(mClient.build()).build()
        }
        return this
    }

    /**创建普通服务，如果想自行定义 RetrofitBuilder 和 OkHttpBuilder ，请在调用此方法前调用 init(mineConfig: IHttpConfig)
     * @see init
     * */
    @SuppressWarnings
    fun <T> createService(service: Class<T>): T {
        if (!this::mRetrofit.isInitialized) {
            mRetrofit = mBuild.client(mClient.build()).build()
        }
        return mRetrofit.create(service)
    }

}
