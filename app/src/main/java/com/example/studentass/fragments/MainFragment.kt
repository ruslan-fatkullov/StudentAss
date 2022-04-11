package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SubjectsRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.User
import com.example.studentass.services.SubjectApiService
import com.example.studentass.services.UserApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_subjects.*


/*
 * Управляет основными страницами, доступными студенту
 */
class MainFragment : Fragment() {
    private lateinit var sfm: FragmentManager
    private lateinit var currentFragment: Fragment



    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager

        val ratingFragment = RatingFragment()
        val scheduleFragmentNew = ScheduleFragmentNew()
        //val subjectsFragment = SubjectsFragment()
        val subjectsFragmentNew = SubjectsFragmentNew()

        val notificationsFragment = NotificationsFragment()
        val accountFragment = AccountFragment()

        sfm.beginTransaction().add(fragment_container.id, scheduleFragmentNew).commit()
        sfm.beginTransaction().hide(scheduleFragmentNew).commit()

//        sfm.beginTransaction().add(fragment_container.id, subjectsFragment).commit()
//        sfm.beginTransaction().hide(subjectsFragment).commit()
        sfm.beginTransaction().add(fragment_container.id, subjectsFragmentNew).commit()
        sfm.beginTransaction().hide(subjectsFragmentNew).commit()

        sfm.beginTransaction().add(fragment_container.id, ratingFragment).commit()
        sfm.beginTransaction().hide(ratingFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, notificationsFragment).commit()
        sfm.beginTransaction().hide(notificationsFragment).commit()

        sfm.beginTransaction().add(fragment_container.id, accountFragment).commit()
        sfm.beginTransaction().hide(accountFragment).commit()



        currentFragment = scheduleFragmentNew
        sfm.beginTransaction().show(currentFragment).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            sfm.beginTransaction().hide(currentFragment).commit()
            currentFragment = when (it.itemId) {
                R.id.bnv_schedule -> scheduleFragmentNew
                //R.id.bnv_subjects -> subjectsFragment
                R.id.bnv_subjects -> subjectsFragmentNew
                R.id.bnv_rating -> ratingFragment
                R.id.bnv_account -> accountFragment
                else -> currentFragment
            }
            sfm.beginTransaction().show(currentFragment).commit()
            true
        }




        onHiddenChanged(false)
    }



    /*
     * Наполнение фрагмента интерфейсом
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    /*
     * Управляет видамостию панели дайствий
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.show()
        }

        if (::currentFragment.isInitialized) {
            if (hidden) {
                sfm.beginTransaction().hide(currentFragment).commit()
            }
            else {
                sfm.beginTransaction().show(currentFragment).commit()
            }
        }
    }
}