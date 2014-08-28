package com.tomorrow.android.ui;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.tomorrow.android.R;
import com.tomorrow.android.api.SessionApi;
import com.tomorrow.android.api.StatusCode;
import com.tomorrow.android.data.cache.GlobalDataSource;
import com.tomorrow.android.data.cache.SourceName;
import com.tomorrow.android.data.model.User;
import com.tomorrow.android.utils.DialogUtil;
import com.xengine.android.data.cache.DefaultDataRepo;

/**
 * Created by jasontujun on 14-7-12.
 */
public class LoginFragment extends Fragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        usernameInput = (EditText) rootView.findViewById(R.id.username_input);
        passwordInput = (EditText) rootView.findViewById(R.id.password_input);
        loginBtn = (Button) rootView.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText() == null ?
                        null : usernameInput.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getActivity(), R.string.login_username_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                final String password = passwordInput.getText() == null ?
                        null : passwordInput.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), R.string.login_password_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<Void, Void, Integer>() {

                    private Dialog waitingDialog;
                    private User user;

                    @Override
                    protected void onPreExecute() {
                        if (getActivity() != null) {
                            waitingDialog = DialogUtil.createWaitingDialog(getActivity(),
                                    R.string.dialog_waiting, null);
                            waitingDialog.show();
                        }
                    }

                    @Override
                    protected Integer doInBackground(Void... voids) {
                        if (getActivity() == null)
                            return -1;
                        user = new User();
                        return SessionApi.login(getActivity(), username, password, user);
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if (getActivity() == null)
                            return;

                        if (StatusCode.success(result)) {
                            // 记住账号密码
                            GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                                    .getInstance().getSource(SourceName.GLOBAL_DATA);
                            globalDataSource.rememberUsernamePassword(username, password);// 记住帐号密码
                            globalDataSource.setCurrentUser(user);// 保存当前登录的User数据

                            Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity) getActivity();
//                            activity.switchScreen(MainActivity.FRAGMENT_MAIN);
                        } else {
                            Toast.makeText(getActivity(), StatusCode.toErrorString(result), Toast.LENGTH_SHORT).show();
                        }

                        if (waitingDialog != null)
                            waitingDialog.dismiss();
                    }

                    @Override
                    protected void onCancelled() {
                        if (waitingDialog != null)
                            waitingDialog.dismiss();
                    }
                }.execute();
            }
        });


        GlobalDataSource globalDataSource = (GlobalDataSource) DefaultDataRepo
                .getInstance().getSource(SourceName.GLOBAL_DATA);
        String username = globalDataSource.getRememberUserName();
        String password = globalDataSource.getRememberPassword();
        if (!TextUtils.isEmpty(username))
            usernameInput.setText(username);
        if (!TextUtils.isEmpty(password))
            passwordInput.setText(password);

        return rootView;
    }



}
