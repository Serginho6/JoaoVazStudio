package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

class ExerciseListFragment : Fragment() {

//    private val db = FirebaseFirestore.getInstance()
//
//    private val userUid: String = ""
//    private var trainId: String = ""
//
//    private val exerciseListViewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)
//    private val query = exerciseListViewModel.getExerciseListQuery(userUid, trainId)
//
//    private val options = FirestoreRecyclerOptions.Builder<Exercise>()
//        .setQuery(query, Exercise::class.java)
//        .setLifecycleOwner(this)
//        .build()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = inflater.inflate(R.layout.fragment_exercise_list, container, false)
//        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_exercise_list)
//
//        val exerciseAdapter = ExerciseListAdapter(options, ::showDeleteExerciseDialog) { exercise ->
//            deleteExercise(exercise)
//        }
//        recyclerView.adapter = exerciseAdapter
//
//        return root
//    }
//
//    private fun showDeleteExerciseDialog(exercise: Exercise) {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Confirmação")
//        builder.setMessage("Tem certeza que deseja excluir este exercício?")
//        builder.setPositiveButton("Sim") { dialog, which ->
//            deleteExercise(exercise)
//        }
//        builder.setNegativeButton("Não") { dialog, which ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }
//
//    private fun deleteExercise(exercise: Exercise) {
//        val userUid = FirebaseAuth.getInstance().currentUser?.uid
//
//        if (userUid != null) {
//            val exerciseRef = db.collection("users")
//                .document(userUid)
//                .collection("trains")
//                .document(trainId)  // Substitua "trainId" pela variável adequada
//                .collection("exercises")
//                .document(exercise.exerciseId)  // ID do exercício
//
//            exerciseRef.delete()
//                .addOnSuccessListener {
//                    showToast("Exercício excluído com sucesso.")
//                }
//                .addOnFailureListener {
//                    showToast("Falha ao excluir o exercício.")
//                }
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }

    companion object {
        @JvmStatic
        fun newInstance(trainId: String) = ExerciseListFragment().apply {
            arguments = Bundle().apply {
                putString("trainId", trainId)
            }
        }
    }
}