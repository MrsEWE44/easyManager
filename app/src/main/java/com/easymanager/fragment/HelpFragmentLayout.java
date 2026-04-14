package com.easymanager.fragment;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easymanager.R;
import com.easymanager.core.api.DhizukuSystemServerApi;
import com.easymanager.core.api.ShizukuSystemServerApi;
import com.easymanager.utils.OtherTools;
import com.easymanager.utils.TextUtils;
import com.easymanager.utils.dialog.NetUtilsDialog;

import com.google.android.material.appbar.MaterialToolbar;

public class HelpFragmentLayout extends Fragment {

    private Boolean isShizuku,isDhizuku;
    private int uid;

    private Context context;
    private Button hflcheckupdate,hfl_donate,hflopengithub,hflopengitee,hflopenshizuku,hflopendhizuku;
    private NetUtilsDialog nu = new NetUtilsDialog();
    private TextUtils tvvv = nu.tu;
    private TextView hflAppName, hflAppVersion;
    private LinearLayout faqContainer;

    public HelpFragmentLayout() {
    }

    public HelpFragmentLayout(Boolean isShizuku, Boolean isDhizuku, int uid) {
        this.isShizuku = isShizuku;
        this.isDhizuku = isDhizuku;
        this.uid = uid;
    }

    public void updateAuthStatus(Boolean isShizuku, Boolean isDhizuku) {
        this.isShizuku = isShizuku;
        this.isDhizuku = isDhizuku;
        if (getView() != null) {
            MaterialToolbar toolbar = getView().findViewById(R.id.toolbar);
            if (toolbar != null) {
                String modeText = "[ General ]";
                if (isShizuku && ShizukuSystemServerApi.isShizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_SHIZUKU) {
                    modeText = "[ SHIZUKU ]";
                } else if (isDhizuku && DhizukuSystemServerApi.isDhizuku() && ShizukuSystemServerApi.runtimeMode == ShizukuSystemServerApi.MODE_DHIZUKU) {
                    modeText = "[ DHIZUKU ]";
                }
                toolbar.setTitle(getString(R.string.help_title) + " " + modeText);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View inflate = inflater.inflate(R.layout.help_fragment_layout, container, false);
        MaterialToolbar toolbar = inflate.findViewById(R.id.toolbar);
        if (toolbar != null) {
            updateAuthStatus(isShizuku, isDhizuku);
        }

        faqContainer = inflate.findViewById(R.id.faq_container);
        hflcheckupdate = inflate.findViewById(R.id.hflcheckupdate);
        hfl_donate = inflate.findViewById(R.id.hfl_donate);
        hflopengithub = inflate.findViewById(R.id.hflopengithub);
        hflopengitee = inflate.findViewById(R.id.hflopengitee);
        hflopenshizuku = inflate.findViewById(R.id.hflopenshizuku);
        hflopendhizuku = inflate.findViewById(R.id.hflopendhizuku);

        hflAppName = inflate.findViewById(R.id.hfl_app_name);
        hflAppVersion = inflate.findViewById(R.id.hfl_app_version);

        try {
            hflAppName.setText(R.string.app_name);
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            hflAppVersion.setText("Version: " + version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupFaq();
        btClicked();
        return inflate;
    }

    private void setupFaq() {
        addFaqItem(getLanStr(R.string.help_question_1), getLanStr(R.string.help_question_1_reply));
        addFaqItem(getLanStr(R.string.help_question_2), getLanStr(R.string.help_question_2_reply));
    }

    private void addFaqItem(String question, String answer) {
        View itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, faqContainer, false);
        TextView tv = itemView.findViewById(android.R.id.text1);
        tv.setText(question);
        tv.setPadding(32, 32, 32, 32);

        TextView answerView = new TextView(context);
        answerView.setText(answer);
        answerView.setPadding(48, 0, 48, 32);
        answerView.setVisibility(View.GONE);
        answerView.setTextColor(getContext().getColor(android.R.color.darker_gray));

        itemView.setOnClickListener(v -> {
            if (answerView.getVisibility() == View.GONE) {
                answerView.setVisibility(View.VISIBLE);
            } else {
                answerView.setVisibility(View.GONE);
            }
        });

        faqContainer.addView(itemView);
        faqContainer.addView(answerView);
    }

    private void btClicked() {
        hflcheckupdate.setOnClickListener(v -> nu.checkupdate(context));
        hfl_donate.setOnClickListener(v -> showDonateDialog());
        hflopengithub.setOnClickListener(v -> nu.openUrlWithBrowser(context,"https://github.com/MrsEWE44/easyManager"));
        hflopengitee.setOnClickListener(v -> nu.openUrlWithBrowser(context,"https://gitee.com/SorryMyLife/easyManager"));
        hflopenshizuku.setOnClickListener(v -> nu.openUrlWithBrowser(context,"https://github.com/RikkaApps/Shizuku"));
        hflopendhizuku.setOnClickListener(v -> nu.openUrlWithBrowser(context,"https://github.com/iamr0s/Dhizuku"));
    }

    private void showDonateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_donate, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        ImageView ivWechat = dialogView.findViewById(R.id.iv_wechat_qr);
        ImageView ivAlipay = dialogView.findViewById(R.id.iv_alipay_qr);
        Button btnClose = dialogView.findViewById(R.id.btn_close_donate);

        // Using existing images as placeholder

        nu.ft.setImageViewImg(context,"wechatqr.jpg",ivWechat);
        nu.ft.setImageViewImg(context,"aliqr.jpg",ivAlipay);


        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private String getLanStr(int id) {
        return tvvv.getLanguageString(context, id);
    }
}
