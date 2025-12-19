package com.onlineexam.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirestoreService {

    private final Firestore firestore;

    public FirestoreService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public void save(String collection, String docId, Map<String, Object> data)
            throws Exception {
        firestore.collection(collection).document(docId).set(data).get();
    }

    public Map<String, Object> read(String collection, String docId)
            throws Exception {
        return firestore.collection(collection)
                .document(docId)
                .get()
                .get()
                .getData();
    }
}