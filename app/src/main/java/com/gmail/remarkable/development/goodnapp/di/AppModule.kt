package com.gmail.remarkable.development.goodnapp.di

import com.gmail.remarkable.development.goodnapp.database.SleepDatabase
import org.koin.dsl.module

val appModule = module {

    single { SleepDatabase.create(get()) }

    single { get<SleepDatabase>().sleepDatabaseDao }

}