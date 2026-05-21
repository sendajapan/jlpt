package com.scholarlyapps.pathlingo.data

import android.content.Context
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto
import com.scholarlyapps.pathlingo.models.Category
import com.scholarlyapps.pathlingo.models.Subcategory
import com.scholarlyapps.pathlingo.models.User
import com.scholarlyapps.pathlingo.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONObject

class DataManager private constructor() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()

    @Volatile private var user: User? = null
    private val categories: MutableList<Category> = mutableListOf()
    private var lessonData: JSONObject? = null

    @Volatile var isReady: Boolean = false
        private set

    fun loadData(context: Context) {
        ServiceLocator.init(context)
        scope.launch { refresh() }
    }

    suspend fun refresh() {
        mutex.withLock {
            val catalog = runCatching { ServiceLocator.catalogRepository.loadAll() }
            val me = runCatching { ServiceLocator.userRepository.me().getOrThrow() }

            catalog.getOrNull()?.let { (cats, subs, vocabs) ->
                categories.clear()
                categories.addAll(buildCategories(cats, subs, vocabs))
            }
            me.getOrNull()?.let { user = mapUser(it) }
            isReady = true
        }
    }

    fun getUser(): User? = user

    fun getCategories(): List<Category> = categories

    fun getCategoryById(id: String?): Category? = categories.firstOrNull { it.id == id }

    fun getLessonData(): JSONObject? = lessonData

    private fun buildCategories(
        cats: List<CategoryDto>,
        subs: List<SubcategoryDto>,
        vocabs: List<VocabularyDto>,
    ): List<Category> {
        val subsByCategory = subs.groupBy { it.vocabCategoryId }
        val vocabsBySubcategory = vocabs.groupBy { it.vocabSubcategoryId }
        return cats.map { c ->
            val cat = Category().apply {
                id = c.id.toString()
                jp = c.nameJp.orEmpty()
                en = c.nameEn.orEmpty()
                ch = c.nameRomaji.orEmpty()
                bg = c.categoryBgPath.orEmpty()
                img = c.iconUrl.orEmpty()
                progress = 0
                locked = c.isPremium
            }
            val mySubs = subsByCategory[c.id].orEmpty().map { s ->
                val words = vocabsBySubcategory[s.id].orEmpty().map { mapWord(it) }
                Subcategory().apply {
                    id = s.id.toString()
                    jp = s.nameJp.orEmpty()
                    en = s.nameEn.orEmpty()
                    bg = s.iconThumbnailBg.orEmpty()
                    total = words.size
                    mastered = 0
                    locked = s.isPremium
                    this.words.addAll(words)
                }
            }
            cat.subcategories.addAll(mySubs)
            cat.count = mySubs.sumOf { it.total }
            cat
        }
    }

    private fun mapWord(v: VocabularyDto): Word = Word().apply {
        kanji = v.wordJp.orEmpty()
        reading = v.wordJp.orEmpty()
        romaji = v.wordRomaji.orEmpty()
        en = v.wordEn.orEmpty()
        img = v.imageUrl.orEmpty()
        jlpt = ""
        type = ""
        xp = 0
        example_jp = v.sentenceJp.orEmpty()
        example_romaji = v.sentenceRomaji.orEmpty()
        example_en = v.sentenceEn.orEmpty()
        mastery = 0
        maxMastery = 5
        correct = false
        favorite = false
    }

    private fun mapUser(dto: AppUserDto): User = User().apply {
        name = dto.name.orEmpty()
        jpName = dto.username.orEmpty()
        level = dto.currentLevel
        xp = dto.xpPoints
        maxXp = 100
        streak = dto.currentStreak
        wordsKnown = dto.learnedWords
        accuracy = 0
        email = dto.email.orEmpty()
    }

    companion object {
        @Volatile private var instance: DataManager? = null

        @JvmStatic
        fun getInstance(): DataManager {
            return instance ?: synchronized(this) {
                instance ?: DataManager().also { instance = it }
            }
        }
    }
}
