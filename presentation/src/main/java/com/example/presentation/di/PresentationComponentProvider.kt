package com.example.presentation.di

import com.example.presentation.di.component.PresentationComponent

interface PresentationComponentProvider {
    fun provideAppComponent() : PresentationComponent
}