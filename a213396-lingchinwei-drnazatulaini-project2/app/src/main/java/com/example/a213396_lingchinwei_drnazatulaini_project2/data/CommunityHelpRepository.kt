package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CommunityHelpRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("community_requests")

    // Live stream of open/responding requests, newest first.
    // callbackFlow mirrors the same Flow shape as Room's getAllHelpRequestsStream().
    fun observeOpenRequests(): Flow<List<CommunityHelpRequest>> = callbackFlow {
        val listener = collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val requests = snapshot?.documents
                    ?.mapNotNull { doc -> doc.toObject(CommunityHelpRequest::class.java) }
                    ?: emptyList()
                trySend(requests)
            }
        awaitClose { listener.remove() }
    }

    // Live stream of requests the current volunteer has responded to, sorted client-side
    // to avoid needing a composite Firestore index (whereEqualTo + orderBy on different fields).
    fun observeMyResponses(volunteerName: String): Flow<List<CommunityHelpRequest>> = callbackFlow {
        val listener = collection
            .whereEqualTo("respondedBy", volunteerName)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val requests = snapshot?.documents
                    ?.mapNotNull { doc -> doc.toObject(CommunityHelpRequest::class.java) }
                    ?.sortedByDescending { it.timestamp }
                    ?: emptyList()
                trySend(requests)
            }
        awaitClose { listener.remove() }
    }

    // Posts a new community request. Firestore auto-generates the document ID.
    suspend fun postRequest(request: CommunityHelpRequest) {
        suspendCancellableCoroutine { cont ->
            collection.add(request)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { if (cont.isActive) cont.resumeWithException(it) }
        }
    }

    // Marks a request as "Responding" and records which volunteer picked it up.
    suspend fun respondToRequest(id: String, volunteerName: String) {
        suspendCancellableCoroutine { cont ->
            collection.document(id)
                .update(mapOf("status" to "Responding", "respondedBy" to volunteerName))
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { if (cont.isActive) cont.resumeWithException(it) }
        }
    }

    // Marks a request as fully resolved.
    suspend fun resolveRequest(id: String) {
        suspendCancellableCoroutine { cont ->
            collection.document(id)
                .update("status", "Resolved")
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { if (cont.isActive) cont.resumeWithException(it) }
        }
    }
}
