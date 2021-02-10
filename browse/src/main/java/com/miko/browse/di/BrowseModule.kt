package com.miko.browse.di

import com.miko.browse.BrowseViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val browseModule = module {
    viewModel { BrowseViewModel(get()) }
}