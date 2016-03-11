package com.accountbook.presenter;

import com.accountbook.biz.api.IAddBiz;
import com.accountbook.biz.impl.AddBiz;
import com.accountbook.entity.Role;
import com.accountbook.view.api.IAddView;

import java.util.List;

/**
 * Created by liuzipeng on 16/3/8.
 */
public class AddRecordPresenter {
    private IAddBiz mAddBiz;
    private IAddView mAddView;

    public AddRecordPresenter(IAddView addView) {
        this.mAddView = addView;
        this.mAddBiz = new AddBiz();
    }

    public void queryRole() {
        mAddBiz.queryRole(new AddBiz.OnQueryRoleListener() {
            @Override
            public void querySuccess(String[] roles) {
                mAddView.loadRole(roles);
            }

            @Override
            public void queryFailed() {
                mAddView.loadRoleFailed();
            }
        });
    }
}