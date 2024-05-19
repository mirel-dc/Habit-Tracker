package com.example.habittracker.di.component

import com.example.data.di.DataModule
import com.example.habittracker.App
import com.example.habittracker.di.module.AppModule
import com.example.presentation.di.component.PresentationComponent
import com.example.presentation.di.module.DomainModule
import com.example.presentation.di.module.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        DomainModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)


    // Factory method for subcomponents
    fun presentationComponent(): PresentationComponent.Factory
}