package com.dmarts05.speedshield.data.local

import android.content.Context
import io.github.osipxd.security.crypto.encryptedPreferencesDataStore

const val TOKEN_DATA_STORE_NAME = "tokenDataStore"

val Context.tokenDataStore by encryptedPreferencesDataStore(name = TOKEN_DATA_STORE_NAME)