package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a213396_lingchinwei_drnazatulaini_project2.AtlasApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AtlasViewModel(
                helpRequestsRepository   = atlasApplication().container.helpRequestsRepository,
                communityHelpRepository  = atlasApplication().container.communityHelpRepository
            )
        }
    }
}

fun CreationExtras.atlasApplication(): AtlasApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AtlasApplication)