package com.spacenextdoor.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.helpcrunch.library.core.Callback
import com.helpcrunch.library.core.HelpCrunch
import com.helpcrunch.library.core.options.HCOptions
import com.helpcrunch.library.core.options.HCPreChatForm
import com.helpcrunch.library.core.options.design.HCTheme
import com.spacenextdoor.MyService
import com.spacenextdoor.R
import com.spacenextdoor.listeners.ChatBtnHideListener
import com.spacenextdoor.listeners.NavigationListener
import com.spacenextdoor.ui.fragments.BottomNavFragments.*
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import kotlinx.android.synthetic.main.activity_bottom_navigation.*


class BottomNavigationActivity : AppCompatActivity(), NavigationListener, ChatBtnHideListener {

    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var chatBtnHidden: ImageView

    var selectedFragment: Fragment? = null
    lateinit var fab_chat: ImageView
    lateinit var chatbtnListener: ChatBtnHideListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom_navigation)

        intent = Intent(this, MyService::class.java)
        startService(intent)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        chatBtnHidden = findViewById(R.id.fab_chat)
        chatbtnListener = this

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment(this, chatbtnListener)).commit()

        fab_chat = findViewById(R.id.fab_chat)


        val intent = intent.extras

        if (intent != null) {

            bottom_navigation.menu.findItem(R.id.navigation_payment).isChecked = true;
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PaymentFragment(chatbtnListener)).commit();

        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment(this, chatbtnListener)).commit();
        }

        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            fab_chat.setOnClickListener {
                runOnUiThread {
                    Log.e("TAG", "State " + HelpCrunch.getState().toString())
                    checkSettingsOpenScreen()
                }
            }
            registerReceiver(hcStateBroadcastReceiver, IntentFilter(HelpCrunch.STATE))
            registerReceiver(hcEventsBroadcastReceiver, IntentFilter(HelpCrunch.EVENTS))
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                this
            )
        }
    }

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.navigation_home -> selectedFragment = HomeFragment(this, chatbtnListener)

                R.id.navigation_payment -> selectedFragment = PaymentFragment(chatbtnListener)

                R.id.navigation_account -> selectedFragment = AccountFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment!!).commit()
            }

            true
        }


    private fun checkSettingsOpenScreen() {

        setChatTheme()
    }

    private fun setChatTheme() {
        var theme: HCTheme =
            HCTheme.Builder(R.color.btn_orange, true)//HCTheme.Builder(HCTheme.Type.CUSTOM)
                .build()

        var optionsBuilder: HCOptions.Builder = HCOptions.Builder()
            .setTheme(theme)

        var preChatForm: HCPreChatForm = HCPreChatForm.Builder()
            .withField("some key", "some value")
            .build()

        optionsBuilder.setPreChatForm(preChatForm)


        showChat(optionsBuilder.build())

    }

    private fun showChat(options: HCOptions) {

        runOnUiThread {

            /*        HelpCrunch.sendMessage("Hi thereeeee", false)

                    HelpCrunch.showChatScreen()
        */
            HelpCrunch.showChatScreen(
                options = options,
                callback = object : Callback<Any?>() {
                    override fun onSuccess(result: Any?) {

                        Log.e("TAG", "showing " + result.toString())
                    }

                    override fun onError(message: String) {
                        Log.e("TAG", "showing error " + message.toString())
                    }
                })
        }
    }

    private var hcStateBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var state: HelpCrunch.State =
                intent.getSerializableExtra(HelpCrunch.STATE_TYPE) as HelpCrunch.State

            Log.e("TAG", "STATE " + getStateString(state ?: HelpCrunch.State.IDLE))

        }
    }

    private var hcEventsBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var event: HelpCrunch.Event =
                intent.getSerializableExtra(HelpCrunch.EVENT_TYPE) as (HelpCrunch.Event)
            /*  var screen: HelpCrunch.Screen =
                  intent.getSerializableExtra(HelpCrunch.SCREEN_TYPE) as (HelpCrunch.Screen)*/
            /*var data: HashMap<String, String> =
                intent.getSerializableExtra(HelpCrunch.EVENT_DATA) as (HashMap<String, String>)*/


            if (event == null) {
                Log.w(HelpCrunch.EVENTS, "Can't receive data")
                return
            }


            when (event) {
                HelpCrunch.Event.FIRST_MESSAGE -> {
                    Log.e(HelpCrunch.EVENTS, "First Message")
                }

                HelpCrunch.Event.SCREEN_CLOSED -> {
                    Log.e(HelpCrunch.EVENTS, "SCREEN_CLOSED")
                }

                HelpCrunch.Event.SCREEN_OPENED -> {
                    Log.e(HelpCrunch.EVENTS, "SCREEN_OPENED")
                }

                HelpCrunch.Event.CHAT_CREATED -> {
                    Log.e(HelpCrunch.EVENTS, "CHAT_CREATED")
                }

                HelpCrunch.Event.MESSAGE_SENDING -> {
                    Log.e(HelpCrunch.EVENTS, "ON_UNREAD_COUNT_CHANGED")
                }
            }
        }

    }

    private fun getStateString(state: HelpCrunch.State): String? {

        var stateStr: String? = null

        when (state) {
            HelpCrunch.State.IDLE -> {
                stateStr = "Idle"
            }
            HelpCrunch.State.READY -> {
                stateStr = "READY"
            }
            HelpCrunch.State.LOADING -> {
                stateStr = "LOADING"
            }
            HelpCrunch.State.ERROR_BLOCKED_USER -> {
                stateStr = "ERROR_BLOCKED_USER"
            }
            HelpCrunch.State.ERROR_INITIALIZATION -> {
                stateStr = "ERROR_INITIALIZATION"
            }
            HelpCrunch.State.ERROR_UNKNOWN -> {
                stateStr = "ERROR_UNKNOWN"
            }
            HelpCrunch.State.HIDDEN -> {
                stateStr = "HIDDEN"
            }
        }
        return stateStr
    }

    override fun onBtnPressed() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_payment);
    }

    override fun onHideChat() {
        fab_chat.visibility = View.GONE
    }

    override fun onViewChat() {
        fab_chat.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        intent = Intent(this, MyService::class.java)
        stopService(intent)
        Log.e("TAG", "APP REMOVEs")
        super.onDestroy()
    }

}
