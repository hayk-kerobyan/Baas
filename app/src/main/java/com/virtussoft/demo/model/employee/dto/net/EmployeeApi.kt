package com.virtussoft.demo.model.employee.dto.net

import com.google.android.gms.tasks.Continuation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.Gson
import com.virtussoft.demo.model.employee.Employee
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EmployeeApi @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val gson: Gson
) {

    companion object {
        const val COLLECTION_NAME = "employees"
    }

    suspend fun createEmployee(employee: Employee): EmployeeNet {
//        val newEmployeeRef = firestore.collection(COLLECTION_NAME).document()
//        val employeeNet = employee.toRemoteDto(newEmployeeRef.id)
//        newEmployeeRef.set(employeeNet).await()
//        return employeeNet

        val data = mapOf("userId" to employee.userId, "companyId" to employee.companyId)

        val result = FirebaseFunctions.getInstance()
            .getHttpsCallable("addEmployee")
            .call(data)
            .await().data

        return gson.fromJson(gson.toJson(result), EmployeeNet::class.java)
    }

    internal suspend fun getEmployee(id: Long) = firestore
        .collection(COLLECTION_NAME)
        .document(id.toString())
        .get()
        .await()
        .toObject(EmployeeNet::class.java)

    internal suspend fun getEmployees(companyId: String, since: String, count: Int) = firestore
        .collection(COLLECTION_NAME)
        .whereEqualTo(EmployeeNet::companyId.name, companyId)
        .orderBy(EmployeeNet::id.name)
        .limit(count.toLong())
        .startAfter(since)
        .get()
        .await()
        .toObjects(EmployeeNet::class.java)
}