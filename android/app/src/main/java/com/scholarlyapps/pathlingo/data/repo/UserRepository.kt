package com.scholarlyapps.pathlingo.data.repo

import com.scholarlyapps.pathlingo.data.remote.ApiService
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto

class UserRepository(private val api: ApiService) {

    suspend fun me(): Result<AppUserDto> = runCatching { api.me().data }

    suspend fun coinsBalance(): Result<Int> = runCatching { api.coinsBalance().coins }
}
