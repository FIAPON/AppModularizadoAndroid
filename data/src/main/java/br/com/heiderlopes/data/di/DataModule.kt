package br.com.heiderlopes.data.di

import br.com.heiderlopes.data.local.database.ProductDataBase
import br.com.heiderlopes.data.local.datasource.ProductCacheDataSource
import br.com.heiderlopes.data.local.datasource.ProductCacheDataSourceImpl
import br.com.heiderlopes.data.repository.ProductRepositoryImpl
import br.com.heiderlopes.domain.repository.ProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<ProductRepository> {
        ProductRepositoryImpl(
            productsCacheDataSource = get(),
            productRemoteDataSource = get()
        )
    }
}

val dataModules = listOf(remoteDataSourceModule, repositoryModule, cacheDataModule)