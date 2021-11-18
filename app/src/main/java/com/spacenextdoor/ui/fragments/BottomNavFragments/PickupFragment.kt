package com.spacenextdoor.ui.fragments.BottomNavFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.spacenextdoor.R
import com.spacenextdoor.ui.activities.Inventory.InventoryServiceActivity
import kotlinx.android.synthetic.main.fragment_pickup.*

class PickupFragment : Fragment() {

    @BindView(R.id.cardViewPickUp)
    lateinit var cardViewPickUp: CardView

    @BindView(R.id.tvPickupSchedule)
    lateinit var tvPickupSchedule: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = View.inflate(context, R.layout.fragment_pickup, null)
        ButterKnife.bind(this, root)

        cardViewPickUp.setBackgroundResource(R.drawable.card_view)
        tvPickupSchedule.isSelected = true

        return root
    }

    @OnClick(R.id.tvPickupSchedule)
    fun pickupScheduleBtn() {
        llChangeClr.setBackgroundColor(resources.getColor(R.color.primary_color))
        // To select
        tvPickupSchedule.isSelected = true
        // To deselect.
        tvPickupPost.isSelected = false

    }

    @OnClick(R.id.tvPickupPost)
    fun pickupPostBtn() {
        llChangeClr.setBackgroundColor(resources.getColor(R.color.txtview_round_color))
        tvPickupPost.isSelected = true
        tvPickupSchedule.isSelected = false
    }

    @OnClick(R.id.llPickupService)
    fun pickupService() {
        val intent = Intent(activity, InventoryServiceActivity::class.java)
        startActivity(intent)
    }
}