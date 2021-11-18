package com.spacenextdoor.ui.activities.Inventory

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.spacenextdoor.R
import kotlinx.android.synthetic.main.activity_inventory_services.*

class InventoryServiceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_SpaceNextDoor)
        setContentView(R.layout.activity_inventory_services)

        setupButtons()
        roundCardView()

    }

    private fun roundCardView() {

        cardViewOne.setBackgroundResource(R.drawable.card_view)
        cardViewTwo.setBackgroundResource(R.drawable.card_view)
        cardViewThree.setBackgroundResource(R.drawable.card_view)
        cardViewFour.setBackgroundResource(R.drawable.btn_requestaccess_code)

    }

    private fun setupButtons() {

        ivDisableCardOne.setOnClickListener() {
            ivEnableCardOne.visibility = View.VISIBLE
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardTwo.visibility = View.GONE
            ivDisableCardTwo.visibility = View.VISIBLE
            cardViewTwo.cardElevation = 16f
            ivEnableCardThree.visibility = View.GONE
            ivDisableCardThree.visibility = View.VISIBLE
            cardViewOne.setBackgroundResource(R.drawable.cardview__focusable_border)
            cardViewTwo.setBackgroundResource(R.drawable.card_view)
            cardViewThree.setBackgroundResource(R.drawable.card_view)

        }
        ivEnableCardOne.setOnClickListener() {
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardOne.visibility = View.GONE
            ivEnableCardTwo.visibility = View.GONE
            ivDisableCardTwo.visibility = View.VISIBLE
            ivEnableCardThree.visibility = View.GONE
            ivDisableCardThree.visibility = View.VISIBLE
            cardViewOne.setBackgroundResource(R.drawable.card_view)

        }

        ivDisableCardTwo.setOnClickListener() {
            ivEnableCardTwo.visibility = View.VISIBLE
            ivDisableCardTwo.visibility = View.VISIBLE
            ivEnableCardThree.visibility = View.GONE
            ivDisableCardThree.visibility = View.VISIBLE
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardOne.visibility = View.GONE
            cardViewTwo.setBackgroundResource(R.drawable.cardview__focusable_border)
            cardViewOne.setBackgroundResource(R.drawable.card_view)
            cardViewThree.setBackgroundResource(R.drawable.card_view)


        }
        ivEnableCardTwo.setOnClickListener() {
            ivDisableCardTwo.visibility = View.VISIBLE
            ivEnableCardTwo.visibility = View.GONE
            ivEnableCardThree.visibility = View.GONE
            ivDisableCardThree.visibility = View.VISIBLE
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardOne.visibility = View.GONE
            cardViewTwo.setBackgroundResource(R.drawable.card_view)

        }

        ivDisableCardThree.setOnClickListener() {
            ivEnableCardThree.visibility = View.VISIBLE
            ivDisableCardThree.visibility = View.VISIBLE
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardOne.visibility = View.GONE
            ivEnableCardTwo.visibility = View.GONE
            ivDisableCardTwo.visibility = View.VISIBLE
            cardViewThree.setBackgroundResource(R.drawable.cardview__focusable_border)
            cardViewOne.setBackgroundResource(R.drawable.card_view)
            cardViewTwo.setBackgroundResource(R.drawable.card_view)
        }
        ivEnableCardThree.setOnClickListener() {
            ivDisableCardThree.visibility = View.VISIBLE
            ivEnableCardThree.visibility = View.GONE
            ivDisableCardOne.visibility = View.VISIBLE
            ivEnableCardOne.visibility = View.GONE
            ivEnableCardTwo.visibility = View.GONE
            ivDisableCardTwo.visibility = View.VISIBLE
            cardViewThree.setBackgroundResource(R.drawable.card_view)

        }

    }
}