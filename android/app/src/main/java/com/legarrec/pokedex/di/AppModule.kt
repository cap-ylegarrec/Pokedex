package com.legarrec.pokedex.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.presentation.PokemonViewModel

val appModule = module {
}