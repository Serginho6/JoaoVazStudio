package com.comunidadedevspace.joaovazstudio

import com.comunidadedevspace.joaovazstudio.data.database.ActionType
import com.comunidadedevspace.joaovazstudio.data.database.TrainAction
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.TrainDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TrainDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val trainDao: TrainDao = mock()

    private val underTest: TrainDetailViewModel by lazy {
        TrainDetailViewModel(trainDao)
    }

    @Test
    fun create_train() = runTest {
        // Given
        val train = Train(
            userId = 1,
            trainTitle = "Train Title",
            trainDescription = "Train Description"
        )

        val trainAction = TrainAction(
            train = train,
            trainActionType = ActionType.CREATE.name
        )

        // When
        underTest.execute(trainAction)

        // Then
        verify(trainDao).insert(train)
    }

    @Test
    fun update_train() = runTest {
        // Given
        val train = Train(
            id = 1,
            userId = 1,
            trainTitle = "Train Title",
            trainDescription = "Train Description"
        )

        val trainAction = TrainAction(
            train = train,
            trainActionType = ActionType.UPDATE.name
        )

        // When
        underTest.execute(trainAction)

        // Then
        verify(trainDao).update(train)
    }

    @Test
    fun delete_train() = runTest {
        // Given
        val train = Train(
            id = 1,
            userId = 1,
            trainTitle = "Train Title",
            trainDescription = "Train Description"
        )

        val trainAction = TrainAction(
            train = train,
            trainActionType = ActionType.DELETE.name
        )

        // When
        underTest.execute(trainAction)

        // Then
        verify(trainDao).deleteById(train.id)
    }

}
