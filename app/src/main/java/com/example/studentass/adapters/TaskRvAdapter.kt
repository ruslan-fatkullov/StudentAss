package com.example.studentass.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.TaskModel
import com.example.studentass.models.TestThemesData
import kotlinx.android.synthetic.main.literature_layout_item.view.*
import kotlinx.android.synthetic.main.task_layout_item.view.*
import kotlinx.android.synthetic.main.test_layout_item.view.*

class TaskRvAdapter(private val context: Context) : RecyclerView.Adapter<TaskRvAdapter.ViewHolder>() {


    private lateinit var mListner: TaskRvAdapter.onFocusChangeListener
    class ViewHolder(view: View, var mListner: TaskRvAdapter.onFocusChangeListener): RecyclerView.ViewHolder(view), View.OnFocusChangeListener {

        val titleTask = view.titleTask
        val descriptionTask = view.descriptionTask
        val typeTask = view.typeTask
        val mainTaskLayout = view.mainTaskLayout

        //val typeOfTask = when(itemView.)


        fun bind(itemData: TaskModel, context: Context) {

//            val taskTypeBackground = when (itemData.type) {
//                "LAB" -> R.drawable.task_item_lab_background
//                else -> R.drawable.task_item_practice_background
//            }


//            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.work_background_item_selector)!!)
//            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.colorNotificationBackground))
//            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
//            mainTaskLayout.background = drawable


            var drawable1 = DrawableCompat.wrap(ContextCompat.getDrawable(context, when (itemData.type) {
                "LAB" -> R.drawable.task_type_laboratory
                else -> R.drawable.task_type_practice
            })!!)
            DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP);
            typeTask.background = drawable1


            titleTask.text = itemData.title
            //descriptionTask.text = itemData.description
            typeTask.text = when(itemData.type){
                "LAB" -> "Сдана"
                "PRACTICE" -> "Не сдана"
                else -> ({}).toString()
            }
            mainTaskLayout.setOnFocusChangeListener{newFocus, _ ->

            }

        }

        init {
            mainTaskLayout.setOnFocusChangeListener(this)
        }



        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (mListner != null){
                mListner.setOnClickListener(adapterPosition)
            }
        }

    }

    var dataList = ArrayList<TaskModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return TaskRvAdapter.ViewHolder(inflater.inflate(R.layout.task_layout_item_second, parent, false), mListner)
    }




    override fun onBindViewHolder(holder: TaskRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.mainTaskLayout.setOnFocusChangeListener(object : View.OnFocusChangeListener{


            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (mListner != null){
                    mListner.setOnClickListener(holder.getAdapterPosition())
                }
            }
        })
//        holder.mainTaskLayout.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                if (mListner != null){
//                    mListner.setOnClickListener(holder.getAdapterPosition())
//                }
//            }
//        })

    }

    override fun getItemCount(): Int = dataList.size
    interface onFocusChangeListener{
        fun setOnClickListener(position: Int)
    }
    fun setonFocusChangeListener(mListner: onFocusChangeListener) {
        this.mListner = mListner
    }

}