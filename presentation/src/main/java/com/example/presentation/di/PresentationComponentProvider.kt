package com.example.presentation.di

import com.example.presentation.di.component.PresentationComponent

//Интерфейс чтобы можно было прокинуть AppComponent из :app
interface PresentationComponentProvider {
    fun provideAppComponent(): PresentationComponent
}
