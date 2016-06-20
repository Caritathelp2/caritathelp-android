package com.eip.red.caritathelp.Views.SubMenu.Friends;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eip.red.caritathelp.Activities.Main.MainActivity;
import com.eip.red.caritathelp.Models.Network;
import com.eip.red.caritathelp.Models.User.User;
import com.eip.red.caritathelp.MyWidgets.DividerItemDecoration;
import com.eip.red.caritathelp.Presenters.SubMenu.Friends.FriendsPresenter;
import com.eip.red.caritathelp.R;

/**
 * Created by pierr on 19/04/2016.
 */

public class FriendsView extends Fragment implements IFriendView, View.OnClickListener {

    private FriendsPresenter    presenter;

    private RecyclerView        recyclerView;
    private MyFriendsRVAdapter  adapter;

    private SwipeRefreshLayout  swipeRefreshLayout;
    private ProgressBar         progressBar;
    private AlertDialog         dialog;

    public static FriendsView newInstance(int userId) {
        FriendsView    myFragment = new FriendsView();

        Bundle args = new Bundle();
        args.putInt("page", R.string.view_name_submenu_friends);
        args.putInt("user id", userId);
        myFragment.setArguments(args);

        return (myFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get User Model
        User    mainUser = ((MainActivity) getActivity()).getModelManager().getUser();
        int     userId = getArguments().getInt("user id");

        // Init Presenter
        presenter = new FriendsPresenter(this, mainUser, userId);

        // Init Dialog
        dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submenu_friends, container, false);

        // Init UI Element
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        // Init RefreshLayout
        initSwipeRefreshLayout();

        // Init RecyclerView
        initRecyclerView(view);

        // Init Listener
        view.findViewById(R.id.btn_add).setOnClickListener(this);

        return (view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init ToolBar Title
        getActivity().setTitle(getArguments().getInt("page"));

        // Init Friends Model
        presenter.getMyFriends(false);
    }

    private void initSwipeRefreshLayout() {
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(R.color.icons);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);

        // Init Refresh Listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Set Friends Model
                presenter.getMyFriends(true);
            }
        });
    }

    private void initRecyclerView(View view) {
        // Init Recycler Views
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_my_friends);

        // Init Adapter
        adapter = new MyFriendsRVAdapter(presenter);

        // Set Adapter
        recyclerView.setAdapter(adapter);

        // Init LayoutManager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Set Options to enable toolbar display/hide
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        // Init Divider (between items)
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDialog(String title, String msg) {
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    public MyFriendsRVAdapter getAdapter() {
        return adapter;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }
}
