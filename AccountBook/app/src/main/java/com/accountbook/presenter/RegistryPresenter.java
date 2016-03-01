package com.accountbook.presenter;

import android.content.Context;
import android.os.Handler;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnRegistryListener;
import com.accountbook.biz.impl.UserBiz;
import com.accountbook.view.api.IRegistryView;

/**
 * Created by Grady on 2016.2.24.
 */
public class RegistryPresenter {

    private String username;
    private String password;
    private String passwordConfirm;
    private IRegistryView view;
    private IUserBiz userBiz;

    public RegistryPresenter(IRegistryView view) {
        this.view = view;
        userBiz = UserBiz.getInstance();
    }

    /**
     * 执行注册
     */
    public void doRegistry() {
        username = view.getUsername();
        password = view.getPassword();
        passwordConfirm = view.getPasswordConfirm();

        if (validateUsername() && validatePassword()) {
            userBiz.registry((Context) view, username, password, new OnRegistryListener() {
                @Override
                public void registrySuccess() {

                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((IRegistryView) view).registerSuccess();
                        }
                    });
                }

                @Override
                public void registryFailed(final String message) {
                    //提交UI变化
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((IRegistryView) view).registerFailed(message);
                        }
                    });
                }
            });
        }//if
    }

    /**
     * 检查用户名
     *
     * @return true表示没问题
     */
    public boolean validateUsername() {
        if (username.equals("")) {
            view.showUsernameError("用户名不能为空");
            return false;
        } else return true;
    }

    /**
     * 检查密码
     *
     * @return true表示没问题
     */
    public boolean validatePassword() {
        if (password.equals("")) {
            view.showPasswordError("密码不能为空");
            return false;
        } else if (passwordConfirm.equals("")) {
            view.showPasswordConfirmError("重复密码不能为空");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            view.showPasswordError("两次密码不一样");
            view.showPasswordConfirmError("两次密码不一样");
            return false;
        } else return true;
    }
}