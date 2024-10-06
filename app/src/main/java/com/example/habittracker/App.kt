package com.example.habittracker

import android.app.Application
import com.example.habittracker.di.component.AppComponent
import com.example.habittracker.di.component.DaggerAppComponent
import com.example.habittracker.di.module.AppModule
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.di.component.PresentationComponent

class App : Application(),
    PresentationComponentProvider
{

    private var _appComponent: AppComponent? = null

    val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "AppComponent isn't initialized"
        }

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun provideAppComponent(): PresentationComponent {
        return appComponent.presentationComponent().create()
    }
}