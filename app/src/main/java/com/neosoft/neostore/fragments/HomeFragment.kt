package com.neosoft.neostore.fragments
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.models.SlideModel
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var tablesFragment: TablesFragment
    private lateinit var sofasFragment: SofasFragment
    private lateinit var cupboardsFragment: CupboardsFragment
    private lateinit var chairsFragment: ChairsFragment

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.ic_slider_image_one, ""))
        imageList.add(SlideModel(R.drawable.ic_slider_image_two, ""))
        imageList.add(SlideModel(R.drawable.ic_image_slider_three, ""))
        imageList.add(SlideModel(R.drawable.ic_image_slider_four, ""))
        image_slider.setImageList(imageList)
        cv_tables.setOnClickListener(this)
        cv_chairs.setOnClickListener(this)
        cv_cupboards.setOnClickListener(this)
        cv_sofas.setOnClickListener(this)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.cv_tables ->
                getTableFragment()
            R.id.cv_sofas ->
                getSofasFragment()
            R.id.cv_chairs ->
                getChairsFragment()
            R.id.cv_cupboards ->
                getCupboardsFragment()
        }
    }
    // load cupboards fragment
    private fun getCupboardsFragment() {
        //cards_list.visibility = View.GONE
        cupboardsFragment = CupboardsFragment()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_layout, cupboardsFragment)
            addToBackStack(null)
            commit()
        }
    }
    // load chairs fragment
    private fun getChairsFragment() {
        //cards_list.visibility = View.GONE
        chairsFragment = ChairsFragment()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_layout, chairsFragment)
            addToBackStack(null)
            commit()
        }
    }
    // load sofas fragment
    private fun getSofasFragment() {
        //cards_list.visibility = View.GONE
        sofasFragment = SofasFragment()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_layout, sofasFragment)
            addToBackStack(null)
            commit()
        }
    }
    // load able fragment
    private fun getTableFragment() {
        //cards_list.visibility = View.GONE
        tablesFragment = TablesFragment()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_layout, tablesFragment)
            addToBackStack(null)
            commit()
        }
    }
    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = getString(R.string.app_name)
    }
    //handle back press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home ->
                activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}