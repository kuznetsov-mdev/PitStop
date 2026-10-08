package ru.kuznetsov.pitstop.di

import org.koin.dsl.module

/** Repository bindings — fake in-memory implementations first (step 5), SQLDelight-backed ones later (step 7). */
val dataModule = module { }
