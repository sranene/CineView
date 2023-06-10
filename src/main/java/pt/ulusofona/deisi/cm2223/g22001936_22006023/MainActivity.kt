package pt.ulusofona.deisi.cm2223.g22001936_22006023

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Cinema
import pt.ulusofona.deisi.cm2223.g22001936_22006023.databinding.ActivityMainBinding
import kotlin.Boolean
import kotlin.Long
import kotlin.apply
import kotlin.let


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationManager.goToHomeFragment(supportFragmentManager)
        val jsonContent = this.assets.open("cinemas.json").bufferedReader().use {
            it.readText()
        }
        val cinemasJson = JSONObject(jsonContent)
        val cinemas = cinemasJson.getJSONArray("cinemas") as JSONArray
        Log.i("APP JSON",""+jsonContent)
        for (i in 0 until cinemas.length()){
            val cinema_id = cinemas.getJSONObject(i).get("cinema_id")
            val cinema_name = cinemas.getJSONObject(i).get("cinema_name")
            val cinema_provider = cinemas.getJSONObject(i).get("cinema_provider")
            val latitude = cinemas.getJSONObject(i).get("latitude")
            val longitude = cinemas.getJSONObject(i).get("longitude")
            val address = cinemas.getJSONObject(i).get("address")
            val postcode = cinemas.getJSONObject(i).get("postcode")
            val county = cinemas.getJSONObject(i).get("county")
            val ratings = cinemasJson.getJSONArray("ratings") as JSONArray
            for (j in 0 until ratings.length()) {
                val category = ratings.getJSONObject(j).get("category")
                val score = ratings.getJSONObject(j).get("score")
            }
            val openingHours = cinemasJson.getJSONObject("opening_hours")
            val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
            val openingClosingTimes = mutableMapOf<String, Pair<String, String>>()
            for (day in daysOfWeek) {
                val dayObject = openingHours.getJSONObject(day)
                val openTime = dayObject.getString("open")
                val closeTime = dayObject.getString("close")
                openingClosingTimes[day] = Pair(openTime, closeTime)
            }


// Exemplo de acesso aos horários de abertura e fechamento para segunda-feira:
            val mondayOpen = openingClosingTimes["Monday"]?.first
            val mondayClose = openingClosingTimes["Monday"]?.second


        }


    }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "CineView"
        setupDrawerMenu()

        binding.ivMic.setOnClickListener {
            onClick(this)
        }
    }


    fun onClick(context : Context) {
        var done = false
        var builder: AlertDialog.Builder? = context.let {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setPositiveButton("Done",
                    DialogInterface.OnClickListener { dialog, id ->
                        done = true
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        done = true
                    })
            }
        }
        builder?.setMessage("10")?.setTitle("Pesquisar por filme com microfone!")
        var dialog: AlertDialog?  = builder?.create()
        object : CountDownTimer(10500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(!done) {
                    builder?.setMessage((millisUntilFinished / 1000).toString())
                        ?.setTitle("Pesquisar por filme com microfone!")
                    dialog?.dismiss()
                    dialog = builder?.create()
                    dialog?.show()
                }
            }

            override fun onFinish() {
                dialog?.dismiss()
            }
        }.start()
    }

    private fun setupDrawerMenu() {

        val toggle = ActionBarDrawerToggle(this,
            binding.drawer, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.navDrawer.setNavigationItemSelectedListener{
            /*for (i in 0 until binding.navDrawer.menu.size()) {
                binding.navDrawer.menu.getItem(i).isChecked = false
            }
            menuInflater.inflate(R.menu.drawer_menu,binding.navDrawer.menu)
            for (i in 0 until binding.navDrawer.menu.size()) {
                val menuItem = binding.navDrawer.menu.getItem(i)
                menuItem.actionView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
            }
            // Marque a opção selecionada com uma cor de fundo diferente
            it.isChecked = true
            it.actionView.setBackgroundResource(R.color.purple_toolbar)*/
            onClickNavigationItem(it)

        }
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

    }
    private fun onClickNavigationItem(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home ->

                NavigationManager.goToHomeFragment(
                    supportFragmentManager
                )
            R.id.nav_registo ->
                NavigationManager.goToRegistarFilmeFragment(
                    supportFragmentManager
                )
            R.id.nav_filmes_vistos ->
                NavigationManager.goToFilmesFragment(
                    supportFragmentManager
                )
            R.id.nav_extra ->
                NavigationManager.goToExtraFragment(
                    supportFragmentManager
                )
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}