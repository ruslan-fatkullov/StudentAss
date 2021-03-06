package com.example.studentass.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.fragments.MainFragment.Companion.colorTheme
import com.example.studentass.fragments.SubjectsFragmentNew.Companion.curSub
import com.example.studentass.fragments.SubjectsFragmentNew.Companion.subjects
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.PassedTests
import com.example.studentass.models.Subject
import com.example.studentass.models.TaskModel
import kotlinx.android.synthetic.main.fragment_subject_info_new.*
import kotlinx.android.synthetic.main.liter_task_test_item_card.view.*
import kotlinx.android.synthetic.main.subjects_overview_item_new.*
import kotlinx.android.synthetic.main.subjects_overview_item_new.view.*
import kotlin.random.Random


class SubjectInfoFragmentNew : Fragment() {
    private lateinit var sfm: FragmentManager
    private var literFragment: Fragment? = null
    private var taskFragment: Fragment? = null
    private var testFragment: Fragment? = null
    private var currentFrag: Fragment? = null
    private var currentFragID: Int? = 1

    companion object {
        var listOfListTest = arrayListOf<List<PassedTests>>()
        var listOfListTask = arrayListOf<List<TaskModel>>()
        var listOfListLiterature = arrayListOf<List<LiteratureData>>()
    }


    /*
     * Инициализация элементов интерфейса
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // цветовая тема фона
        subject_info_layout.background = colorTheme

        // цветовая тема предмета
        val colorThemeSubjectValues = subjects?.size?.let { List(it) { Random.nextInt(0, 4) } }
        var subjectBackground = colorThemeSubjectValues?.get(0)?.let { backgroundForSubject(it) }
        subOverView.subItemCL.background =
            subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }

        // название предмета
        subOverView.nameSub.text = curSub?.name

        // кнопки переключения
        if (subjects?.size == 1) {
            nextSubBtn.visibility = View.INVISIBLE
        }
        prevSubBtn.visibility = View.INVISIBLE

        subOverView.subItemCL.elevation = 15.0F

        /// cоздание списков прогресс бара
        val progressBarValues = subjects?.size?.let { List(it) { Random.nextInt(15, 27) } }
        var progressBarPercentsValues = arrayListOf<Int>()
        if (progressBarValues != null) {
            for (i in progressBarValues){
                progressBarPercentsValues.add(i.div(26.0).times(100).toInt())
            }
        }

        // присвоение значений
        subOverView.progressBarRating.progress = progressBarPercentsValues[0]
        textView10.text = "${progressBarValues?.get(0)}/26"


        /// текст предмета
        val typeSubjectText = subjects?.size?.let { List(it) { Random.nextInt(0, 2) } }
        type_subject_name.text = typeSubjectText?.get(0)?.let { setTypeSubjectText(it) }

        // цвет текста типа придмета
        var typeSubjectTextColor = setTypeSubjectTextColor(colorThemeSubjectValues?.get(0))
        if (typeSubjectTextColor != null) {
            type_subject_name.setTextColor(typeSubjectTextColor)
        }


        // таб меню списков
        subjectLiteratureLayout.bookTvCard.text = "Литература"
        subjectTaskLayout.bookTvCard.text = "Работы"
        subjectTestLayout.bookTvCard.text = "Тесты"
        subjectLiteratureLayout.bookIvCard.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_liter)
        subjectTaskLayout.bookIvCard.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_articles_active)
        subjectTestLayout.bookIvCard.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_projects)

        subjectTaskLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.activeTab))
        subjectTaskLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.typeSubjectTextOrange)

        subjectTaskLayout.cardLayout.background = ContextCompat.getDrawable(context!!, R.drawable.work_background_item_selector)
        subjectLiteratureLayout.cardLayout.background = ContextCompat.getDrawable(context!!, R.drawable.work_background_item_selector)
        subjectTestLayout.cardLayout.background = ContextCompat.getDrawable(context!!, R.drawable.work_background_item_selector)


        loadContent()
        nextSubBtn.setOnClickListener {
            prevSubBtn.visibility = View.VISIBLE
            val ind = subjects?.indexOf(curSub)
            if (ind != null) {
                subjectBackground = colorThemeSubjectValues?.get(ind + 1)?.let { backgroundForSubject(it) }
                type_subject_name.text = typeSubjectText?.get(ind + 1)?.let { setTypeSubjectText(it) }

                typeSubjectTextColor = setTypeSubjectTextColor(colorThemeSubjectValues?.get(ind + 1))
                if (typeSubjectTextColor != null) {
                    type_subject_name.setTextColor(typeSubjectTextColor!!)
                }

                subOverView.progressBarRating.progress = progressBarPercentsValues[ind + 1]
                textView10.text = "${progressBarValues?.get(ind + 1)}/26"

                subOverView.subItemCL.background =
                    subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }
                curSub = subjects?.get(ind + 1)
                subOverView.nameSub.text = curSub?.name
                if (ind == subjects?.size?.minus(2)) {
                    nextSubBtn.visibility = View.INVISIBLE
                }
            }
            loadContent()
        }
        prevSubBtn.setOnClickListener {
            nextSubBtn.visibility = View.VISIBLE
            val ind = subjects?.indexOf(curSub)
            if (ind != null) {
                subjectBackground = colorThemeSubjectValues?.get(ind - 1)?.let { backgroundForSubject(it) }
                type_subject_name.text = typeSubjectText?.get(ind - 1)?.let { setTypeSubjectText(it) }
                subOverView.subItemCL.background =
                    subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }

                typeSubjectTextColor = setTypeSubjectTextColor(colorThemeSubjectValues?.get(ind - 1))
                if (typeSubjectTextColor != null) {
                    type_subject_name.setTextColor(typeSubjectTextColor!!)
                }

                subOverView.progressBarRating.progress = progressBarPercentsValues[ind - 1]
                textView10.text = "${progressBarValues?.get(ind - 1)}/26"

                curSub = subjects?.get(ind - 1)
                subOverView.nameSub.text = curSub?.name
                if (ind == 1) {
                    prevSubBtn.visibility = View.INVISIBLE
                }
            }
            loadContent()

        }



        super.onHiddenChanged(false)
    }


    /*
     * Наполнение страницы элемнтами интерфейса
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_info_new, container, false)
    }

    private fun loadContent() {
        testFragment?.let { sfm.beginTransaction().remove(it).commit() }
        taskFragment?.let { sfm.beginTransaction().remove(it).commit() }
        literFragment?.let { sfm.beginTransaction().remove(it).commit() }

        sfm = getAppCompatActivity<MainActivity>()!!.createFragmentManagerForSubject()

        testFragment = TestListFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, testFragment!!).commit()
        sfm.beginTransaction().hide(testFragment as TestListFragment).commit()

        taskFragment = TaskListFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, taskFragment!!).commit()
        sfm.beginTransaction().hide(taskFragment as TaskListFragment).commit()

        literFragment = LiteratureFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, literFragment!!).commit()
        sfm.beginTransaction().hide(literFragment as LiteratureFragment).commit()

        currentFrag = when (currentFragID) {
            1 -> taskFragment
            2 -> literFragment
            else -> testFragment
        }
        currentFrag?.let { sfm.beginTransaction().show(it).commit() }



        subjectLiteratureLayout.cardLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = literFragment
            currentFragID = 2
            sfm.beginTransaction().show(literFragment as LiteratureFragment).commit()
            subjectTaskLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectTestLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectLiteratureLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.activeTab))

            subjectTaskLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)
            subjectTestLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)
            subjectLiteratureLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.typeSubjectTextOrange)

            subjectLiteratureLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_liter_active)
            subjectTaskLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_articles)
            subjectTestLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_projects)
        }
        subjectTaskLayout.cardLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = taskFragment
            currentFragID = 1
            sfm.beginTransaction().show(taskFragment as TaskListFragment).commit()
            subjectLiteratureLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectTestLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectTaskLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.activeTab))

            subjectTaskLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.typeSubjectTextOrange)
            subjectTestLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)
            subjectLiteratureLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)

            subjectLiteratureLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_liter)
            subjectTaskLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_articles_active)
            subjectTestLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_projects)
        }
        subjectTestLayout.cardLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = testFragment
            currentFragID = 3
            sfm.beginTransaction().show(testFragment as TestListFragment).commit()
            subjectLiteratureLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectTaskLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.noActiveTab))
            subjectTestLayout.bookTvCard.setTextColor(ContextCompat.getColor(context!!, R.color.activeTab))

            subjectTaskLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)
            subjectTestLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.typeSubjectTextOrange)
            subjectLiteratureLayout.view3.background = ContextCompat.getDrawable(context!!, R.color.white)

            subjectLiteratureLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_liter)
            subjectTaskLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_articles)
            subjectTestLayout.bookIvCard.background =
                ContextCompat.getDrawable(context!!, R.drawable.ic_projects_active)

        }
    }

    private fun backgroundForSubject(ran: Int): Int {
        return when (ran) {
            0 -> R.drawable.subject_background_rectangl_blue
            1 -> R.drawable.subject_background_rectangl_orange
            2 -> R.drawable.subject_background_rectangl_purple
            3 -> R.drawable.subject_background_rectangle_pink
            else -> {
                R.drawable.subject_background_rectangl_purple
            }
        }
    }

    private fun setTypeSubjectText(ran: Int): String {
        return when (ran) {
            0 -> "Зачет"
            else -> {
                "Экзамен"
            }
        }
    }

    private fun setTypeSubjectTextColor(subjectBackground: Int?): Int? {
        return context?.let {
            ContextCompat.getColor(
                it, when(subjectBackground){
                    0 -> R.color.typeSubjectTextBlue
                    1 -> R.color.typeSubjectTextOrange
                    2 -> R.color.typeSubjectTextPurple
                    3 -> R.color.typeSubjectTextPink
                    else -> {R.color.black}
                })
        }
    }

    private fun setColorToProgressBar(ran: Int) {

    }

}