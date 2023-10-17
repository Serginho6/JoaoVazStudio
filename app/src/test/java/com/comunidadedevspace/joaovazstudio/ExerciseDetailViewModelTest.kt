package com.comunidadedevspace.joaovazstudio

import com.comunidadedevspace.joaovazstudio.data.database.ActionType
import com.comunidadedevspace.joaovazstudio.data.database.ExerciseAction
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.ExerciseDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ExerciseDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val exerciseDao: ExerciseDao = mock()

    private val underTest: ExerciseDetailViewModel by lazy {
        ExerciseDetailViewModel(
            exerciseDao,
        )
    }

    @Test
    fun create_task() = runTest {
        //Given
        val exercise = Exercise(
            id = 1,
            userId = 0,
            trainId = 0,
            title = "title",
            description = "description",
            youtubeVideoId = String(),
            isSelected = false
        )

        val exerciseAction = ExerciseAction(
            exercise = exercise,
            taskActionType = ActionType.CREATE.name
        )

        //When
        underTest.execute(exerciseAction)

        //Then
        verify(exerciseDao).insert(exercise)
    }

    @Test
    fun update_task() = runTest {
        //Given
        val exercise = Exercise(
            id = 1,
            userId = 0,
            trainId = 0,
            title = "title",
            description = "description",
            youtubeVideoId = String(),
            isSelected = false
        )

        val exerciseAction = ExerciseAction(
            exercise = exercise,
            taskActionType = ActionType.UPDATE.name
        )

        //When
        underTest.execute(exerciseAction)

        //Then
        verify(exerciseDao).update(exercise)
    }

    @Test
    fun delete_task() = runTest {
        //Given
        val exercise = Exercise(
            id = 1,
            userId = 0,
            trainId = 0,
            title = "title",
            description = "description",
            youtubeVideoId = String(),
            isSelected = false
        )

        val exerciseAction = ExerciseAction(
            exercise = exercise,
            taskActionType = ActionType.DELETE.name
        )

        //When
        underTest.execute(exerciseAction)

        //Then
        verify(exerciseDao).deleteById(exercise.id)
    }

}