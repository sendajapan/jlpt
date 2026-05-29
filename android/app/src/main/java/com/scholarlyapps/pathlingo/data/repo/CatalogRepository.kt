package com.scholarlyapps.pathlingo.data.repo

import com.scholarlyapps.pathlingo.data.remote.ApiService
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CatalogRepository(private val api: ApiService) {

    suspend fun loadAll(): Triple<List<CategoryDto>, List<SubcategoryDto>, List<VocabularyDto>> = coroutineScope {
        val categories = async { api.getCategories().data }
        val subcategories = async { api.getSubcategories().data }
        val vocabularies = async { api.getVocabularies().data }
        Triple(categories.await(), subcategories.await(), vocabularies.await())
    }

    suspend fun favorites(): Map<String, Any?> = api.favorites()

    suspend fun addFavorite(vocabularyId: Long) = api.addFavorite(vocabularyId)

    suspend fun removeFavorite(vocabularyId: Long) = api.removeFavorite(vocabularyId)

    suspend fun markRead(vocabularyId: Long) = api.markRead(vocabularyId)
}
