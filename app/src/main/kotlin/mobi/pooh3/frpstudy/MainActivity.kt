package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import mobi.pooh3.frpstudy.extensions.replaceFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).apply {
            drawer_layout.addDrawerListener(this)
            this.syncState()
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {

            replaceFragment(R.id.fragment_container, FrpStudy01Fragment.newInstance(), "01")

        } else if (id == R.id.nav_gallery) {

            replaceFragment(R.id.fragment_container, FrpStudy02Fragment.newInstance(), "02")

        } else if (id == R.id.nav_slideshow) {

            replaceFragment(R.id.fragment_container, FrpStudy02WithHeavyFragment.newInstance(), "02_heavy")

        } else if (id == R.id.nav_manage) {

            replaceFragment(R.id.fragment_container, FrpStudy02TransactionalFragment.newInstance(), "02_trans")

        } else if (id == R.id.nav_share) {

            replaceFragment(R.id.fragment_container, FrpStudy03Fragment.newInstance(), "03_default")

        } else if (id == R.id.nav_send) {

            replaceFragment(R.id.fragment_container, FrpStudy04TranslateFragment.newInstance(), "04_snapshot")

        } else if (id == R.id.nav_05) {

            replaceFragment(R.id.fragment_container, FrpStudy05SpinnerFragment.newInstance(), "05_loop")

        } else if (id == R.id.nav_06) {

            replaceFragment(R.id.fragment_container, FrpStudy06LiftFragment.newInstance(), "06_lift")

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}


