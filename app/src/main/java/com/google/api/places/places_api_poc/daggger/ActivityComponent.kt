/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.places.places_api_poc.daggger

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.api.places.places_api_poc.model.AutocompletePredictionData
import com.google.api.places.places_api_poc.model.PlaceWrapper
import com.google.api.places.places_api_poc.model.PlacesAPI
import com.google.api.places.places_api_poc.ui.*
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Subcomponent(modules = [ExecutorModule::class, LiveDataModule::class])
interface ActivityComponent {
    fun inject(api: PlacesAPI)
    fun inject(fragment: Tab1Fragment)
    fun inject(fragment: Tab3Fragment)
    fun inject(bottomSheetDialogFragment: PlaceDetailsSheetFragment)
    fun inject(activity: DriverActivity)
    fun inject(fragment: Tab2Fragment)
}

typealias PlacesLiveData = MutableLiveData<List<PlaceWrapper>>
typealias AutocompletePredictionsLiveData = MutableLiveData<List<AutocompletePredictionData>>
typealias LocationLiveData = MutableLiveData<Location>

@Module
class LiveDataModule {
    @Provides
    @ActivityScope
    fun providesGetCurrentPlaceLiveData() = PlacesLiveData()

    @Provides
    @ActivityScope
    fun providesModalPlaceDetailsSheetLiveData() = ModalPlaceDetailsSheetLiveData()

    @Provides
    @ActivityScope
    fun providesAutocompletePredictionsLiveData() = AutocompletePredictionsLiveData()

    @Provides
    @ActivityScope
    fun providesGetLastLocationLiveData() = LocationLiveData()
}

@Module
class ExecutorModule {
    @Provides
    @ActivityScope
    fun provideExecutor(): ExecutorWrapper {
        return ExecutorWrapper()
    }
}

class ExecutorWrapper {
    lateinit var executor: ExecutorService

    fun create() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    fun destroy() {
        executor.shutdown()
    }
}
