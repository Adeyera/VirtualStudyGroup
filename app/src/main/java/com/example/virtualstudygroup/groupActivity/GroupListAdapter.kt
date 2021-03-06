package com.example.virtualstudygroup.groupActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualstudygroup.R
import com.example.virtualstudygroup.model.Group
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class GroupListAdapter(private var groupList: MutableList<Group>): RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder>(),
    Filterable {

    var onGroupClickListener: ((group: Group) -> Unit)? = null
    private var groupFilterList: MutableList<Group>? = null
    private var searchIsEmpty: Boolean = true

    init {
        groupFilterList = groupList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupListViewHolder {
        val itemGroupView = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupListViewHolder(itemGroupView)
    }

    override fun getItemCount(): Int {
        if (groupFilterList.isNullOrEmpty() && searchIsEmpty) {
            return groupList.size
        }
        return groupFilterList!!.size
    }

    fun updateGroup(newGroupList: MutableList<Group>) {
        groupList = newGroupList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        if (groupFilterList.isNullOrEmpty()) {
            val group = groupList[position]
            holder.bind(group)
        } else {
            val group = groupFilterList?.get(position)
            if (group != null) {
                holder.bind(group)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    searchIsEmpty = true
                    groupFilterList = groupList
                } else {
                    searchIsEmpty = false
                    val resultList = ArrayList<Group>()
                    Log.i("info", groupList.toString())
                    for (row in groupList) {
                        Log.i("info", row.toString())
                        Log.i("info", charSearch.toLowerCase(Locale.ROOT))
                        Log.i("info", row.noteExchange.toString())
                        // var validGroup = true;
                        // rewrite the filter
                        if (row.teamName.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                            || row.className.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                            || (row.homeworkHelp && charSearch.toLowerCase(Locale.ROOT) == "homeworkhelp")
                            || (row.examSquad && charSearch.toLowerCase(Locale.ROOT) == "examsquad")
                            || (row.labMates && charSearch.toLowerCase(Locale.ROOT) == "labmate")
                            || (row.projectPartners && charSearch.toLowerCase(Locale.ROOT) == "projectpartner")
                            || (row.noteExchange && charSearch.toLowerCase(Locale.ROOT) == "noteexchange")) {
                            resultList.add(row)
                            Log.i("info", "resultList:")
                            Log.i("info", resultList.toString())
                        }
                    }
                    groupFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = groupFilterList
                Log.i("info", filterResults.values.toString())
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                groupFilterList = results?.values as MutableList<Group>
                Log.i("info", groupFilterList.toString())
                notifyDataSetChanged()
            }

        }
    }

    inner class GroupListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageGroup by lazy { itemView.findViewById<ImageView>(R.id.imageGroup) }
        private val textCourse by lazy { itemView.findViewById<TextView>(R.id.textCourse) }
        private val textTeamName by lazy { itemView.findViewById<TextView>(R.id.textTeamName) }

        fun bind(group: Group) {
            Picasso.get().load(group.img).error(R.drawable.ic_person_black_24dp)
                .into(imageGroup)
            textCourse.text = group.className
            textTeamName.text = group.teamName

            itemView.setOnClickListener{
                    onGroupClickListener?.invoke(group)
            }
        }

    }
}