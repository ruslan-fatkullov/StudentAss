package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.LiteratureRvAdapter
import com.example.studentass.adapters.TaskRvAdapter
import com.example.studentass.adapters.TestRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.*
import com.example.studentass.services.LiteratureApiService
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.ArrayList

class TaskFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private val subService = SubjectApiService.create()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        loadTask()

        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }







    private fun onGetTask(taskList: List<TaskModel>) {
        if (taskList != null) {
            var adapter = literatureRv.adapter as TaskRvAdapter
            adapter.dataList = taskList as ArrayList<TaskModel>
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context, "nol`", Toast.LENGTH_SHORT).show()
        }

    }


    fun loadTask() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TaskRvAdapter(context!!)

        val header = "Bearer " + LoginFragment.token
        val userId = SubjectsFragmentNew.curSub?.teacherIds?.get(0)
        val jsonObject = JSONObject()
        jsonObject.put("key", "userId")
        jsonObject.put("operation", "==")
        jsonObject.put("value", userId)
        val jsonObjectString = "[$jsonObject]"
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        val disposableSubjectListRx = subService
            .getIdTask(header, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetTask(r) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }



}


