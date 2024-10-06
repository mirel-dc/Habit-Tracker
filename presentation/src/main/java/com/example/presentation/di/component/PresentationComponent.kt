package com.example.presentation.di.component

import com.example.presentation.di.module.ViewModelFactoryModule
import com.example.presentation.fragments.BottomSheetFilterFragment
import com.example.presentation.fragments.CreateHabitFragment
import com.example.presentation.fragments.HabitsListFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelFactoryModule::class])
interface PresentationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): PresentationComponent
    }

    fun inject(fragment: CreateHabitFragment)
    fun inject(fragment: HabitsListFragment)
    fun inject(fragment: BottomSheetFilterFragment)
}
