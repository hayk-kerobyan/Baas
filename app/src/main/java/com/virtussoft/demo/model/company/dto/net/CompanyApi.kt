package com.virtussoft.demo.model.company.dto.net

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.toRemoteDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CompanyApi @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    companion object {
        const val COLLECTION_NAME = "companies"
    }

    suspend fun createCompany(company: Company): CompanyNet {
        val newCompanyRef = firestore.collection(COLLECTION_NAME).document()
        val companyNet = company.toRemoteDto(newCompanyRef.id)
        newCompanyRef.set(companyNet).await()
        return companyNet
    }

    internal suspend fun getCompany(id: Long) = firestore
        .collection(COLLECTION_NAME)
        .document(id.toString())
        .get()
        .await()
        .toObject(CompanyNet::class.java)


    internal suspend fun getCompanies(since: String, count: Int) = firestore
        .collection(COLLECTION_NAME)
        .orderBy(CompanyNet::id.name)
        .limit(count.toLong())
        .startAfter(since)
        .get()
        .await()
        .toObjects(CompanyNet::class.java)
}