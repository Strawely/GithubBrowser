package com.example.githubbrowser.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubbrowser.DateAdapter
import com.example.githubbrowser.GithubApi
import com.example.githubbrowser.R
import com.example.githubbrowser.ui.MainActivity
import com.example.githubbrowser.ui.RepoInfoFragment
import com.example.githubbrowser.ui.ReposListFragment
import com.example.githubbrowser.viewmodel.RepoInfoViewModel
import com.example.githubbrowser.viewmodel.ReposListViewModel
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.android.synthetic.main.activity_main.view.container
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelsMap[modelClass] ?: viewModelsMap.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReposListViewModel::class)
    abstract fun provideListViewModel(reposListViewModel: ReposListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoInfoViewModel::class)
    abstract fun provideInfoViewModel(repoInfoViewModel: RepoInfoViewModel): ViewModel
}

@Module
object ApiModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit) = retrofit.create(GithubApi::class.java)

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi, @Named("BASE_URL") baseUrl: String) =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder().add(DateAdapter()).build()
}

@Component(modules = [ViewModelModule::class, ApiModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(listFragment: ReposListFragment)
    fun inject(infoFragment: RepoInfoFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun baseUrl(@Named("BASE_URL") baseUrl: String): Builder

        @BindsInstance
        fun containerId(@Named("CONTAINER_ID") containerId: Int): Builder

        @BindsInstance
        fun fragmentManager(fragmentManager: FragmentManager): Builder

        fun build(): AppComponent
    }

    companion object {
        fun createComponent(activity: AppCompatActivity): AppComponent {
            return DaggerAppComponent.builder()
                .baseUrl("https://api.github.com")
                .containerId(R.id.container)
                .fragmentManager(activity.supportFragmentManager)
                .build()
        }
    }
}