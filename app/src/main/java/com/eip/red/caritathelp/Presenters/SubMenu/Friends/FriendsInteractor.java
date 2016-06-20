package com.eip.red.caritathelp.Presenters.SubMenu.Friends;

import android.content.Context;
import android.widget.ProgressBar;

import com.eip.red.caritathelp.Models.Friends.Friends;
import com.eip.red.caritathelp.Models.Friendship;
import com.eip.red.caritathelp.Models.Network;
import com.eip.red.caritathelp.Models.User.User;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by pierr on 19/04/2016.
 */

public class FriendsInteractor {

    private Context context;
    private User    mainUser;
    private int     userId;

    public FriendsInteractor(Context context, User mainUser, int userId) {
        this.context = context;
        this.mainUser = mainUser;
        this.userId = userId;
    }

    public void getMyFriends(ProgressBar progressBar, final IOnFriendsFinishedListener listener) {
        JsonObject json = new JsonObject();

        json.addProperty("token", mainUser.getToken());

        Ion.with(context)
                .load("GET", Network.API_LOCATION + Network.API_REQUEST_FRIENDSHIP_VOLUNTEER + userId + Network.API_REQUEST_FRIENDSHIP)
                .progressBar(progressBar)
                .setJsonObjectBody(json)
                .as(new TypeToken<Friends>(){})
                .setCallback(new FutureCallback<Friends>() {
                    @Override
                    public void onCompleted(Exception error, Friends result) {
                        if (error == null) {
                            // Status == 400 == error
                            if (result.getStatus() == Network.API_STATUS_ERROR)
                                listener.onDialog("Statut 400", result.getMessage());
                            else
                                listener.onSuccessGetMyFriends(result.getResponse());
                        }
                        else
                            listener.onDialog("Problème de connection", "Vérifiez votre connexion Internet");
                    }
                });
    }

    public void getInvitations() {

    }

    public void getSent() {

    }

    public void blockFriend() {

    }

    public void removeFriend(int unfriendId, ProgressBar progressBar, final IOnFriendsFinishedListener listener) {
        JsonObject json = new JsonObject();

        json.addProperty("token", mainUser.getToken());
        json.addProperty("id", unfriendId);

        Ion.with(context)
                .load("DELETE", Network.API_LOCATION + Network.API_REQUEST_FRIENDSHIP_REMOVE)
                .progressBar(progressBar)
                .setJsonObjectBody(json)
                .as(new TypeToken<Friendship>(){})
                .setCallback(new FutureCallback<Friendship>() {
                    @Override
                    public void onCompleted(Exception error, Friendship result) {
                        if (error == null) {
                            // Status == 400 == error
                            if (result.getStatus() == Network.API_STATUS_ERROR)
                                listener.onDialog("Statut 400", result.getMessage());
                            else
                                listener.onSuccessRemoveFriend();
                        }
                        else
                            listener.onDialog("Problème de connection", "Vérifiez votre connexion Internet");
                    }
                });
    }

    public int getMainUserId() {
        return mainUser.getId();
    }

    public int getUserId() {
        return userId;
    }

}
