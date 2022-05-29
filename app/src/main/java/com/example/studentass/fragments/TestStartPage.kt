package com.example.studentass.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_test_start_page.*
import kotlinx.android.synthetic.main.fragment_test_start_page.closeTab
import kotlinx.android.synthetic.main.fragment_test_start_page.testTheme


class TestStartPage : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        val width = displayMetrics.widthPixels
//        val layoutParams = countOfTries.layoutParams
//        layoutParams.width = 34

        val drawable = DrawableCompat.wrap(
            context?.let { ContextCompat.getDrawable(it, R.drawable.test_info_count) }!!
        )
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
        val drawable1 = DrawableCompat.wrap(
            context?.let { ContextCompat.getDrawable(it, R.drawable.test_info_result) }!!
        )
        DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP)

        countOfTries.background = drawable
        resultOfTries.background = drawable1

        //countOfTries.layoutParams.width = 360

        val test = TestListFragment.currentTest
        if (test != null) {
            testDescriptionTV.text = test.theme.decryption
            testTheme.text = test.theme.name
            val countTry = "${test.ratings.size}"
            val maxResult = "${test.ratings.maxOrNull()}%"
            countOfTriesTV.text = countTry
            resultOfTriesTV.text = maxResult
        }
        closeTab.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }
        closeTestButton.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }
        repeatButton.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchSideways(TestFragment::class.java)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_start_page, container, false)
    }


}