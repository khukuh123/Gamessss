package com.miko.gamesss.di

import com.miko.core.domain.usecase.GamesInteractor
import com.miko.core.domain.usecase.GamesUseCase
import com.miko.gamesss.detail.DetailViewModel
import com.miko.gamesss.favorite.FavoriteViewModel
import com.miko.gamesss.home.HomeViewModel
import com.miko.gamesss.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GamesUseCase> { GamesInteractor(get()) }
}

val viewModelModule = module {
    viewModel { DetailViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}