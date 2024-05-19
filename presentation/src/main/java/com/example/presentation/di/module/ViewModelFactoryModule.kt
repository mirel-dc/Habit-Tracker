package com.example.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.presentation.viewmodels.CreateHabitViewModel
import com.example.presentation.viewmodels.HabitListViewModel
import com.example.presentation.viewmodels.factory.MultiViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: MultiViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateHabitViewModel::class)
    abstract fun bindCreateHabitViewModel(
        createHabitViewModel: CreateHabitViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HabitListViewModel::class)
    abstract fun bindHabitListViewModel(
        habitListViewModel: HabitListViewModel
    ): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)