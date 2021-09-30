package com.github.christianmsc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.adapters.PagerAdapter
import com.github.christianmsc.com.github.christianmsc.ui.fragments.doalso.DoAlsoFragment
import com.github.christianmsc.com.github.christianmsc.ui.fragments.overview.OverviewFragment
import com.github.christianmsc.com.github.christianmsc.ui.fragments.variations.VariationsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(VariationsFragment())
        fragments.add(DoAlsoFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Variations")
        titles.add("Do Also")

        val resultBundle = Bundle()
        resultBundle.putParcelable("exerciseBundle", args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}