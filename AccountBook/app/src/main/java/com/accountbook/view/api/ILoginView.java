package com.accountbook.view.api;

public interface ILoginView {
    /**
     * 提供给presenter获取username的方法
     *
     * @return 用户名
     */
    String getLoginUsername();

    /**
     * 提供给presenter获取password的方法
     *
     * @return 密码
     */
    String getLoginPassword();

    /**
     * 登录成做的事情
     */
    void loginSuccess();

    /**
     * 登录失败做的事情
     */
    void loginFailed(String message);

    /**
     * 显示进度
     */
    void showProgress();
}
