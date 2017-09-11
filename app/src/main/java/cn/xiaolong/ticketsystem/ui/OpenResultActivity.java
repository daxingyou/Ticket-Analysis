package cn.xiaolong.ticketsystem.ui;

import android.os.Bundle;
import android.widget.TextView;


import com.standards.library.util.DateUtils;

import cn.xiaolong.ticketsystem.R;
import cn.xiaolong.ticketsystem.base.BaseTitleBar;
import cn.xiaolong.ticketsystem.base.BaseTitleBarActivity;
import cn.xiaolong.ticketsystem.bean.TicketOpenData;
import cn.xiaolong.ticketsystem.bean.TicketType;
import cn.xiaolong.ticketsystem.presenter.OpenResultPresenter;
import cn.xiaolong.ticketsystem.presenter.view.IOpenResultView;
import cn.xiaolong.ticketsystem.utils.LaunchUtil;

/**
 * @author xiaolong
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/8 14:32
 */

public class OpenResultActivity extends BaseTitleBarActivity<OpenResultPresenter> implements IOpenResultView {
    private TextView tvTitle;
    private TextView tvOpenResult;
    private TextView tvOpenSerial;
    private TextView tvOpenTime;
    private TextView tvHistory;
    private TicketType mTicketType;
    private TicketOpenData mTicketOpenData;

    @Override
    public OpenResultPresenter getPresenter() {
        return new OpenResultPresenter(this);
    }

    public static Bundle buildBundle(TicketType ticketType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketType", ticketType);
        return bundle;
    }

    public static Bundle buildBundle(TicketOpenData ticketOpenData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketOpenData", ticketOpenData);
        return bundle;
    }

    @Override
    public void getExtra() {
        super.getExtra();
        mTicketType = (TicketType) getIntent().getSerializableExtra("ticketType");
        mTicketOpenData = (TicketOpenData) getIntent().getSerializableExtra("ticketOpenData");
    }

    @Override
    public void initTitleBar(BaseTitleBar titleBar) {
        tvTitle = (TextView) titleBar.center;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_result;
    }

    @Override
    protected void init() {
        tvOpenResult = findView(R.id.tvOpenResult);
        tvOpenSerial = findView(R.id.tvOpenSerial);
        tvOpenTime = findView(R.id.tvOpenTime);
        tvHistory = findView(R.id.tvHistory);

        if (mTicketType != null) {
            tvTitle.setText(mTicketType.descr);
            mPresenter.getSingleOpenResult(mTicketType.code, "");
        }
        if (mTicketOpenData != null) {
            tvTitle.setText(mTicketOpenData.name);
            mPresenter.getSingleOpenResult(mTicketType.code, mTicketOpenData.expect);
        }
    }

    @Override
    protected void setListener() {
        tvHistory.setOnClickListener(v ->
        {
            if (mTicketType == null) {
                finish();
            } else {
                LaunchUtil.launchActivity(this, HistoryActivity.class, HistoryActivity.buildBundle(mTicketType));
            }

        });
    }

    @Override
    public void getSingleOpenResultSuccess(TicketOpenData ticketOpenData) {
        tvOpenResult.setText(ticketOpenData.openCode);
        tvOpenTime.setText("开奖日期：" + DateUtils.timeStampToYYDDMM(ticketOpenData.timestamp * 1000));
        tvOpenSerial.setText("第" + ticketOpenData.expect + "期");
    }
}