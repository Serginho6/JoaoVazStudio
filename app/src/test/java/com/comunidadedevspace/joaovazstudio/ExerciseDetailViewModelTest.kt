package com.comunidadedevspace.joaovazstudio

import com.comunidadedevspace.joaovazstudio.data.Task
import com.comunidadedevspace.joaovazstudio.data.TaskDao
import com.comunidadedevspace.joaovazstudio.presentation.ActionType
import com.comunidadedevspace.joaovazstudio.presentation.TaskAction
import com.comunidadedevspace.joaovazstudio.presentation.ExerciseDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ExerciseDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val taskDao: TaskDao = mock()

    private val underTest: ExerciseDetailViewModel by lazy {
        ExerciseDetailViewModel(
            taskDao,
        )
    }

    private val imageByteArray = "Dados da imagem".toByteArray()

    @Test
    fun create_task() = runTest {
        //Given
        val task = Task(
            id = 1,
            userId = 0,
            title = "title",
            description = "description",
            image = imageByteArray,
            isSelected = false
        )

        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.CREATE.name
        )

        //When
        underTest.execute(taskAction)

        //Then
        verify(taskDao).insert(task)
    }

    @Test
    fun update_task() = runTest {
        //Given
        val task = Task(
            id = 1,
            userId = 0,
            title = "title",
            description = "description",
            image = imageByteArray,
            isSelected = false
        )

        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.UPDATE.name
        )

        //When
        underTest.execute(taskAction)

        //Then
        verify(taskDao).update(task)
    }

    @Test
    fun delete_task() = runTest {
        //Given
        val task = Task(
            id = 1,
            userId = 0,
            title = "title",
            description = "description",
            image = imageByteArray,
            isSelected = false
        )

        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.DELETE.name
        )

        //When
        underTest.execute(taskAction)

        //Then
        verify(taskDao).deleteById(task.id)
    }

}