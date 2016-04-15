package com.eip.red.caritathelp.Views.SubMenu.MyOrganisations;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eip.red.caritathelp.Models.Organisation.Organisation;
import com.eip.red.caritathelp.Presenters.SubMenu.MyOrganisations.MyOrganisationsPresenter;
import com.eip.red.caritathelp.R;
import com.eip.red.caritathelp.Tools;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by pierr on 05/04/2016.
 */

public class MyOrganisationsRVAdapter extends RecyclerView.Adapter<MyOrganisationsRVAdapter.DataObjectHolder> {

    private MyOrganisationsPresenter presenter;

    private List<Organisation> visibleObjects;
    private List<Organisation>  allObjects;

    public MyOrganisationsRVAdapter(MyOrganisationsPresenter presenter, List<Organisation> objects) {
        this.presenter = presenter;

        visibleObjects = new ArrayList<>();
        visibleObjects.addAll(objects);

        allObjects = new ArrayList<>();
        allObjects.addAll(objects);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircularImageView   logo;
        TextView            name;
        TextView            location;
        TextView            friends;

        public DataObjectHolder(View itemView) {
            super(itemView);
            logo = (CircularImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.name);
            location = (TextView) itemView.findViewById(R.id.location);
            friends = (TextView) itemView.findViewById(R.id.friends);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Organisation    organisation = visibleObjects.get(getAdapterPosition());

            if (organisation != null)
                presenter.goToOrganisationView(organisation);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_submenu_my_organisations_rv_row, parent, false);
        DataObjectHolder    holder = new DataObjectHolder(view);

        return (holder);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String  name = visibleObjects.get(position).getName();
        String  location = visibleObjects.get(position).getCity();
        String  friends = visibleObjects.get(position).getNb_friends_members();

//        holder.logo.setImageDrawable();

        // Set Name
        holder.name.setText(Tools.upperCaseFirstLetter(name));

        // Set Location
        if (location != null && !TextUtils.isEmpty(location))
            holder.location.setText(location);
        else
            holder.location.setVisibility(View.GONE);

        // Set Friends
        if (friends != null && !TextUtils.isEmpty(friends) && !friends.equals("0")) {
            if (friends.equals("1"))
                holder.friends.setText(new StringBuilder(friends).append(" ami"));
            else
                holder.friends.setText(new StringBuilder(friends).append(" amis"));
        }
        else
            holder.friends.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (visibleObjects.size());
    }

    public void update(List<Organisation>  organisations) {
        visibleObjects.clear();
        allObjects.clear();

        visibleObjects.addAll(organisations);
        allObjects.addAll(organisations);

        notifyDataSetChanged();
    }

    public void flushFilter(){
        visibleObjects.clear();
        visibleObjects.addAll(allObjects);
        notifyDataSetChanged();
    }

    public void filter(String queryText) {
        visibleObjects.clear();

        for (Organisation organisation :  allObjects) {
            if (organisation.getName().toLowerCase(Locale.getDefault()).contains(queryText))
                visibleObjects.add(organisation);
        }
        notifyDataSetChanged();
    }



}