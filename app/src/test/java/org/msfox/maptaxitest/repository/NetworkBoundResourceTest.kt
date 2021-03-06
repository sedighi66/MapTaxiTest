package org.msfox.maptaxitest.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import org.msfox.maptaxitest.api.ApiResponse
import org.msfox.maptaxitest.utils.ApiUtil
import org.msfox.maptaxitest.utils.MainCoroutineRule
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class NetworkBoundResourceTest() {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var handleSaveCallResult: (Foo) -> Unit

    private lateinit var handleShouldMatch: (Foo?) -> Boolean

    private lateinit var handleCreateCall: () -> LiveData<ApiResponse<Foo>>

    private val dbData = MutableLiveData<Foo>()

    private lateinit var networkBoundResource: NetworkBoundResource<Foo, Foo>

    private val fetchedOnce = AtomicBoolean(false)


    @Before
    fun init() {
        networkBoundResource = object : NetworkBoundResource<Foo, Foo>(coroutineRule.getAppCoroutineDispatchers()) {
            override suspend fun saveCallResult(item: Foo) {
                handleSaveCallResult(item)
            }

            override fun shouldFetch(data: Foo?): Boolean {
                // since test methods don't handle repetitive fetching, call it only once
                return handleShouldMatch(data) && fetchedOnce.compareAndSet(false, true)
            }

            override fun loadFromDb(): LiveData<Foo> {
                return dbData
            }

            override fun createCall(): LiveData<ApiResponse<Foo>> {
                return handleCreateCall()
            }
        }
    }

    @Test
    fun basicFromNetwork() = coroutineRule.runBlockingTest {
        val saved = AtomicReference<Foo>()
        handleShouldMatch = { it == null }
        val fetchedDbValue = Foo(1)
        handleSaveCallResult = { foo ->
            saved.set(foo)
            dbData.setValue(fetchedDbValue)
        }
        val networkResult = Foo(1)
        handleCreateCall = { ApiUtil.createCall(Response.success(networkResult)) }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        dbData.value = null
        assertThat(saved.get(), `is`(networkResult))
        verify(observer).onChanged(Resource.success(fetchedDbValue))
    }

    @Test
    fun failureFromNetwork() = coroutineRule.runBlockingTest {
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }
        val body = ResponseBody.create(MediaType.parse("text/html"), "error")
        handleCreateCall = { ApiUtil.createCall(Response.error<Foo>(500, body)) }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        dbData.value = null
        assertThat(saved.get(), `is`(false))
        verify(observer).onChanged(Resource.error("error", null))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithoutNetwork() = coroutineRule.runBlockingTest {
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        val dbFoo = Foo(1)
        dbData.value = dbFoo
        verify(observer).onChanged(Resource.success(dbFoo))
        assertThat(saved.get(), `is`(false))
        val dbFoo2 = Foo(2)
        dbData.value = dbFoo2
        verify(observer).onChanged(Resource.success(dbFoo2))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithFetchFailure() = coroutineRule.runBlockingTest {
        val dbValue = Foo(1)
        val saved = AtomicBoolean(false)
        handleShouldMatch = { foo -> foo === dbValue }
        handleSaveCallResult = {
            saved.set(true)
        }
        val body = ResponseBody.create(MediaType.parse("text/html"), "error")
        val apiResponseLiveData = MutableLiveData<ApiResponse<Foo>>()
        handleCreateCall = { apiResponseLiveData }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)

        dbData.value = dbValue
        // as we update functionality of networkBoundResource, there should not be any interactions
        // with observer
        //verify(observer).onChanged(Resource.loading(dbValue))

        apiResponseLiveData.value = ApiResponse.create(Response.error<Foo>(400, body))
        assertThat(saved.get(), `is`(false))
        // we changed the return type of error state to null
        verify(observer).onChanged(Resource.error("error", null))

        val dbValue2 = Foo(2)
        dbData.value = dbValue2
        // we changed the return type of error state to null
        verify(observer).onChanged(Resource.error("error", null))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithReFetchSuccess() = coroutineRule.runBlockingTest {
        val dbValue = Foo(1)
        val dbValue2 = Foo(2)
        val saved = AtomicReference<Foo>()
        handleShouldMatch = { foo -> foo === dbValue }
        handleSaveCallResult = { foo ->
            saved.set(foo)
            dbData.setValue(dbValue2)
        }
        val apiResponseLiveData = MutableLiveData<ApiResponse<Foo>>()
        handleCreateCall = { apiResponseLiveData }

        val observer = mock<Observer<Resource<Foo>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)

        dbData.value = dbValue
        val networkResult = Foo(1)
        // as we update functionality of networkBoundResource, there should not be any interactions
        // with observer
        //verify(observer).onChanged(Resource.loading(dbValue))
        apiResponseLiveData.value = ApiResponse.create(Response.success(networkResult))
        assertThat(saved.get(), `is`(networkResult))
        verify(observer).onChanged(Resource.success(dbValue2))
        verifyNoMoreInteractions(observer)
    }

    private data class Foo(var value: Int)
}