package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
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

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy01Fragment.newInstance(), "01")
                    .commit()

        } else if (id == R.id.nav_gallery) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy02Fragment.newInstance(), "02")
                    .commit()

        } else if (id == R.id.nav_slideshow) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy02WithHeavyFragment.newInstance(), "02_heavy")
                    .commit()

        } else if (id == R.id.nav_manage) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy02TransactionalFragment.newInstance(), "02_trans")
                    .commit()

        } else if (id == R.id.nav_share) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy03Fragment.newInstance(), "03_default")
                    .commit()

        } else if (id == R.id.nav_send) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy04TranslateFragment.newInstance(), "04_snapshot")
                    .commit()

        } else if (id == R.id.nav_05) {

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FrpStudy05SpinnerFragment.newInstance(), "05_loop")
                    .commit()

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
