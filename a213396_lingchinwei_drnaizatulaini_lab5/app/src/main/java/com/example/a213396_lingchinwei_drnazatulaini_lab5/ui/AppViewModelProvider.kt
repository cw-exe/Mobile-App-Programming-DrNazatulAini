package com.example.a213396_lingchinwei_drnazatulaini_lab5.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a213396_lingchinwei_drnazatulaini_lab5.AtlasApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AtlasViewModel(atlasApplication().container.helpRequestsRepository)
        }
    }
}

fun CreationExtras.atlasApplication(): AtlasApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AtlasApplication)
