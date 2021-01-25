package com.miko.gamesss.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miko.gamesss.R
import com.miko.gamesss.databinding.ItemGroupChildBinding
import com.miko.gamesss.databinding.ItemParentBinding
import com.miko.gamesss.model.Section

class DetailSectionAdapter(private val titles: List<Section>) : BaseExpandableListAdapter() {

    private class ParentHolder {
        lateinit var titleName: TextView
        lateinit var indicator: ImageView
    }

    private object ChildHolder {
        var horizontalListView: RecyclerView? = null
    }

    private var bindingItemParent: ItemParentBinding? = null
    private var bindingItemGroup: ItemGroupChildBinding? = null
    private var detailSectionItemAdapter: DetailSectionItemAdapter? = null

    override fun getGroupCount(): Int =
        titles.size

    override fun getChildrenCount(p0: Int): Int = 1

    override fun getGroup(p0: Int): Any = titles[p0]

    override fun getChild(p0: Int, p1: Int): Any = titles[p0]

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val parentHolder: ParentHolder?
        val section = getGroup(groupPosition) as Section
        var mConvertView = convertView
        bindingItemParent =
            ItemParentBinding.inflate(LayoutInflater.from(parent?.context), parent, false)

        if (mConvertView != null) {
            parentHolder = convertView?.tag as ParentHolder
        } else {
            mConvertView = bindingItemParent?.root?.rootView
            parentHolder = ParentHolder()
            mConvertView?.run {
                isHorizontalScrollBarEnabled = true
                tag = parentHolder
            }
        }

        with(parentHolder) {
            titleName = mConvertView?.findViewById(R.id.tvSectionTitle) as TextView
            titleName.text = section.name
            indicator = mConvertView.findViewById(R.id.image_indicator)
            indicator.setImageResource(if (isExpanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down)
        }

        return mConvertView as View
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val childHolder: ChildHolder?
        var mConvertView = convertView
        bindingItemGroup = ItemGroupChildBinding.inflate(
            LayoutInflater.from(parent?.context),
            parent,
            false
        )
        val section = getGroup(groupPosition) as Section

        if (mConvertView != null) {
            childHolder = mConvertView.tag as ChildHolder
        } else {
            mConvertView = bindingItemGroup?.root?.rootView
            childHolder = ChildHolder
            mConvertView?.tag = childHolder
        }


        val mLayoutManager = LinearLayoutManager(
            parent?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        detailSectionItemAdapter = DetailSectionItemAdapter(section.listItem)
        childHolder.horizontalListView = mConvertView?.findViewById(R.id.rvDetail) as RecyclerView
        childHolder.horizontalListView?.run {
            layoutManager = mLayoutManager
            adapter = detailSectionItemAdapter
        }

        return mConvertView
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = false

    override fun areAllItemsEnabled(): Boolean = false

    override fun isEmpty(): Boolean = false

    fun destroy() {
        bindingItemGroup = null
        bindingItemParent = null
        detailSectionItemAdapter?.destroy()
    }
}