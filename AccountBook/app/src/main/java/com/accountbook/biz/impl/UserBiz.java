package com.accountbook.biz.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.accountbook.biz.api.IUserBiz;
import com.accountbook.biz.api.OnLoginListener;
import com.accountbook.entity.User;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.presenter.MyApplication;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;


public class UserBiz implements IUserBiz {

    private static final UserBiz instance = new UserBiz();

    private UserBiz(){}

    public static UserBiz getInstance(){
        return instance;
    }

    /**
     * 处理登录
     * @param context 上下文对象
     * @param username 用户名
     * @param password 密码
     * @param listener 登录结果回调
     */
    @Override
    public void login(final Context context,final String username, final String password, final OnLoginListener listener) {

        if(Util.isNetworkAvailable(context)){

            AVUser.logInInBackground(username, password, new LogInCallback<UserForLeanCloud>() {

                @Override
                public void done(UserForLeanCloud avUser, AVException e) {
                    if(avUser == null){
                        listener.loginFailed("用户名密码错误");
                    }else{
                        //检查本地数据库中有没有记录
                        Cursor cursor = SQLite.db.query(SQLite.USERTABLE,new String[]{"id"},"id = ?",new String[]{avUser.getObjectId()},null,null,null);
                        if(cursor != null){
                            if(cursor.getCount() != 0){
                                //更新数据
                                ContentValues values = new ContentValues();
                                values.put(UserForLeanCloud.FID,avUser.getFid());
                                values.put(UserForLeanCloud.ACTOR,avUser.getActor());
                                values.put(UserForLeanCloud.MONEY, avUser.getMoney());
                                SQLite.db.update(SQLite.USERTABLE, values, "id = ?", new String[]{avUser.getObjectId()});
                            }else{
                                //插入新数据
                                addToLocal(avUser);
                            }
                            cursor.close();
                        }
                        //设置到Application中
                        MyApplication application = (MyApplication)context.getApplicationContext();
                        application.setUser(generateLocalUser(avUser));
                        listener.loginSuccess();
                    }
                }
            },UserForLeanCloud.class);

        }else{
            listener.loginFailed("请联网后重试");
        }
    }

    /**
     * 往本地用户表添加项
     * @param user 从云端获取到的user对象
     */
    public void addToLocal(UserForLeanCloud user){
        ContentValues values = new ContentValues();
        values.put("id",user.getObjectId());
        values.put("username",user.getUsername());
        values.put("email",user.getEmail());
        values.put(UserForLeanCloud.FID,user.getFid());
        values.put(UserForLeanCloud.ACTOR,user.getActor());
        values.put(UserForLeanCloud.MONEY, user.getMoney());
        SQLite.db.insert(SQLite.USERTABLE,null,values);
    }


    /**
     * 由UserForLeanCloud生成UserBean
     * @param user leanCloud的user
     * @return userBean
     */
    public User generateLocalUser(UserForLeanCloud user){
        User localUser = new User();
        localUser.setId(user.getObjectId());
        localUser.setUsername(user.getUsername());
        localUser.setEmail(user.getEmail());
        localUser.setActor(user.getActor());
        localUser.setFid(user.getFid());
        localUser.setMoney(user.getMoney());
        return localUser;
    }
}
