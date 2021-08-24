package ru.sorokin.gb_material.viewmodel.notes

import ru.sorokin.gb_material.model.notes.Note
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.CommonViewModel
import kotlinx.coroutines.*

class NotesViewModel : CommonViewModel(), CoroutineScope by MainScope() {

    fun getNotesFromServer() {
        data.value = AppState.Loading
        launch {
            val job: Deferred<List<Note>> = async(Dispatchers.IO) { Note.getDefValues() }
            data.value = AppState.Success(job.await())
        }
    }
}