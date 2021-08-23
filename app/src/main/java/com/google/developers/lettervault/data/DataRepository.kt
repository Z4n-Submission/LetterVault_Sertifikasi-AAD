package com.google.developers.lettervault.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.developers.lettervault.R
import com.google.developers.lettervault.notification.NotificationWorker
import com.google.developers.lettervault.util.LETTER_ID
import com.google.developers.lettervault.util.LetterLock
import com.google.developers.lettervault.util.NOTIFICATION_CHANNEL_ID
import com.google.developers.lettervault.util.executeThread
import java.util.concurrent.TimeUnit

/**
 * Handles data sources and execute on the correct threads.
 */
class DataRepository(private val letterDao: LetterDao) {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = LetterDatabase.getInstance(context)
                    instance = DataRepository(database.letterDao())
                }
                return instance
            }
        }
    }

    /**
     * Get letters with a filtered state for paging.
     */
    fun getLetters(filter: LetterState): LiveData<PagedList<Letter>> {
        val letterSort = getFilteredQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .build()
        return LivePagedListBuilder(letterDao.getLetters(letterSort), config).build()
    }

    fun getLetter(id: Long): LiveData<Letter> {
        return letterDao.getLetter(id)
    }

    fun getRecentLetter(): LiveData<Letter> {
        return letterDao.getRecentLetter()
    }

    fun delete(letter: Letter) = executeThread {
        letterDao.delete(letter)
    }

    /**
     * Add a letter to database and schedule a notification on
     * when the letter vault can be opened.
     */
    fun save(letter: Letter, ctx: Context) = executeThread {
        val id = letterDao.insert(letter)
        val workManager = WorkManager.getInstance(ctx)
        val expireTime = letter.expires - System.currentTimeMillis()
        val channelName = ctx.getString(R.string.notify_channel_name)
        val dataInput = Data.Builder()
            .putLong(LETTER_ID, id)
            .putString(NOTIFICATION_CHANNEL_ID, channelName)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(expireTime, TimeUnit.MILLISECONDS)
            .setInputData(dataInput)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
    }

    /**
     * Update database with a decode letter content and update the opened timestamp.
     */
    fun openLetter(letter: Letter) = executeThread {
        val letterCopy = letter.copy(
            subject = LetterLock.retrieveMessage(letter.subject),
            content = LetterLock.retrieveMessage(letter.content),
            opened = System.currentTimeMillis()
        )
        letterDao.update(letterCopy)
    }

    /**
     * Create a raw query at runtime for filtering the letters.
     */
    private fun getFilteredQuery(filter: LetterState): SimpleSQLiteQuery {
        val now = System.currentTimeMillis()
        val simpleQuery = StringBuilder()
            .append("SELECT * FROM letter ")

        if (filter == LetterState.FUTURE) {
            simpleQuery.append("WHERE expires >= $now OR expires <= $now AND opened IS 0")
        }
        if (filter == LetterState.OPENED) {
            simpleQuery.append("WHERE opened IS NOT 0")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}
