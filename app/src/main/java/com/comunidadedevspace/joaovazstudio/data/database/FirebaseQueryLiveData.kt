package com.comunidadedevspace.joaovazstudio.data.database

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FirebaseQueryLiveData<T>(private val query: Query, private val type: Class<T>) : LiveData<List<T>>() {
    private var listener: ListenerRegistration? = null

    private val eventListener = EventListener<QuerySnapshot> { querySnapshot, _ ->
        if (querySnapshot != null) {
            val items = querySnapshot.toObjects(type)
            value = items
        }
    }

    override fun onActive() {
        super.onActive()
        listener = query.addSnapshotListener(eventListener)
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
    }
}