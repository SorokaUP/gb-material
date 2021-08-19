package ru.sorokin.gb_material.view.notes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.databinding.NotesFragmentBinding
import ru.sorokin.gb_material.model.notes.Note
import ru.sorokin.gb_material.util.callbackError
import ru.sorokin.gb_material.util.hide
import ru.sorokin.gb_material.util.hideAllItems
import ru.sorokin.gb_material.util.show
import ru.sorokin.gb_material.viewmodel.AppState
import ru.sorokin.gb_material.viewmodel.notes.NotesViewModel
import java.util.*

class NotesFragment : Fragment() {
    private var _binding: NotesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private val adapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.apply {
            notesRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
            notesRecyclerView.adapter = adapter

            ItemTouchHelper(NotesTouchHelperCallback(adapter)).attachToRecyclerView(
                notesRecyclerView
            )
        }

        val observer = Observer<AppState> { renderData(it) }
        viewModel.setStringResources { id: Int -> getString(id) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        getData()
    }

    private fun getData() {
        viewModel.getNotesFromServer()
    }

    private fun renderData(data: AppState) = with(binding) {
        when (data) {
            is AppState.Success<*> -> {
                if (data.response is List<*>) {
                    adapter.setNotes(data.response as List<Note>)
                }
                notesLoadingLayout.hide()
            }
            is AppState.Loading -> {
                notesLoadingLayout.show()
            }
            is AppState.Error -> {
                callbackError(
                    data.error.message ?: getString(R.string.error_msg),
                    notesLoadingLayout,
                    root
                ) { getData() }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.hideAllItems()
        inflater.inflate(R.menu.menu_notes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notes_add_action -> {
                adapter.addNote(
                    Note(
                        Note.genId(),
                        Date(),
                        Note.genTitle(),
                        Note.genBody(),
                        false
                    )
                )
                return true
            }
            R.id.notes_edit_action -> {
                adapter.updateCurrentNote(Note.genTitle(), Note.genBody())
                return true
            }
            R.id.notes_delete_action -> {
                adapter.deleteCurrentNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}