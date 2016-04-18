package com.tadev.musicplayer.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.constant.Enums;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.models.music.Download;
import com.tadev.musicplayer.utils.design.support.StringUtils;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 12/04/2016.
 */
public class DialogDownloadFragment extends AppCompatDialogFragment implements View.OnClickListener {
    private final String TAG = "DialogDownloadFragment";
    private TextView txtTitle, txt128FileSize, txt320FileSize,
            txt500FileSize, txt1000FileSize;
    private RadioGroup rdgDownloadGroup;
    private RadioButton rdbMusic128, rdbMusic320, rdbMusic500, rdbMusicLossless;
    private AppCompatButton btnComfirmDownload;
    private AppCompatButton btnClose;
    private Download download;

    public static DialogDownloadFragment newInstance(Download download) {
        DialogDownloadFragment fragmentDownload = new DialogDownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.KEY_EXTRA_DATA, download);
        fragmentDownload.setArguments(bundle);

        return fragmentDownload;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDataSent();
        setStyle(AppCompatDialogFragment.STYLE_NO_FRAME, 0);
        return inflater.inflate(R.layout.fragment_dialog_download, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle = (TextView) view.findViewById(R.id.fragment_dialog_download_txtTitle);
        txt128FileSize = (TextView) view.findViewById(R.id.fragment_dialog_download_txt128Size);
        txt320FileSize = (TextView) view.findViewById(R.id.fragment_dialog_download_txt320Size);
        txt500FileSize = (TextView) view.findViewById(R.id.fragment_dialog_download_txtM4aSize);
        txt1000FileSize = (TextView) view.findViewById(R.id.fragment_dialog_download_txtLlSize);
        rdgDownloadGroup = (RadioGroup) view.findViewById(R.id.fragment_dialog_download_rdgDownload);
        rdbMusic128 = (RadioButton) view.findViewById(R.id.fragment_dialog_download_rdb128);
        rdbMusic320 = (RadioButton) view.findViewById(R.id.fragment_dialog_download_rdb320);
        rdbMusic500 = (RadioButton) view.findViewById(R.id.fragment_dialog_download_rdb500);
        rdbMusicLossless = (RadioButton) view.findViewById(R.id.fragment_dialog_download_rdb1000);
        btnComfirmDownload = (AppCompatButton) view.findViewById(R.id.fragment_dialog_download_btnComfirm);
        btnClose = (AppCompatButton) view.findViewById(R.id.fragment_dialog_download_btnClose);
        setDataViews();
        validateViewDownload();
        setActionViews();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }


    private void setActionViews() {
        btnComfirmDownload.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        rdgDownloadGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnComfirmDownload.setEnabled(true);
            }
        });
    }

    private void setDataViews() {
        txtTitle.setText(download.getName());
        txt128FileSize.append(Utils.readableFileSize(convertFileSize(download.getMusicFilesize())));
        txt320FileSize.append(Utils.readableFileSize(convertFileSize(download.getMusic320Filesize())));
        txt500FileSize.append(Utils.readableFileSize(convertFileSize(download.getMusicM4aFilesize())));
        txt1000FileSize.append(Utils.readableFileSize(convertFileSize(download.getMusicLosslessFilesize())));
    }

    private void validateViewDownload() {
        if (download != null) {
            if (download.getMusicFilesize().equals("0")) {
                rdbMusic128.setPaintFlags(rdbMusic128.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                rdbMusic128.setEnabled(false);
                txt128FileSize.append(StringUtils.getStringRes(R.string.txtEmptyFileSizeDownload));
            }
            if (download.getMusic320Filesize().equals("0")) {
                rdbMusic320.setPaintFlags(rdbMusic128.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                rdbMusic320.setEnabled(false);
                txt320FileSize.append(StringUtils.getStringRes(R.string.txtEmptyFileSizeDownload));
            }
            if (download.getMusicM4aFilesize().equals("0")) {
                rdbMusic500.setPaintFlags(rdbMusic128.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                rdbMusic500.setEnabled(false);
                txt500FileSize.append(StringUtils.getStringRes(R.string.txtEmptyFileSizeDownload));
            }
            if (download.getMusicLosslessFilesize().equals("0")) {
                rdbMusicLossless.setPaintFlags(rdbMusic128.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                rdbMusicLossless.setEnabled(false);
                txt1000FileSize.append(StringUtils.getStringRes(R.string.txtEmptyFileSizeDownload));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_dialog_download_btnComfirm:
                getTargetFragment().onActivityResult(getTargetRequestCode(), Extras.RESULT_CODE,
                        setDataCallback());
                dismiss();
                break;
            case R.id.fragment_dialog_download_btnClose:
                dismiss();
                break;
        }
    }

    private void getDataSent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            download = bundle.getParcelable(Extras.KEY_EXTRA_DATA);
        }
    }


    private long convertFileSize(String fileSize) {
        return Long.parseLong(fileSize);
    }

    private int validateRadioSelected() {
        return rdgDownloadGroup.getCheckedRadioButtonId();
    }

    private Intent setDataCallback() {
        Intent intent = new Intent();
        String bitrate = null;
        String url = null;
        Download dataCallBack = new Download();
        Bundle bundle = new Bundle();
        switch (validateRadioSelected()) {
            case R.id.fragment_dialog_download_rdb128:
                url = download.getUrl().get(Api.MUSIC_128);
                bitrate = Enums.Bitrate.MUSIC128.toString();
                break;
            case R.id.fragment_dialog_download_rdb320:
                url = download.getUrl().get(Api.MUSIC_320);
                bitrate = Enums.Bitrate.MUSIC320.toString();
                break;
            case R.id.fragment_dialog_download_rdb500:
                url = download.getUrl().get(Api.MUSIC_500);
                bitrate = Enums.Bitrate.MUSICM4A.toString();
                break;
            case R.id.fragment_dialog_download_rdb1000:
                url = download.getUrl().get(Api.MUSIC_1000);
                bitrate = Enums.Bitrate.MUSICLOSSLESS.toString();
                break;
        }
        dataCallBack.setUrlChoose(url);
        dataCallBack.setName(download.getName().concat(bitrate));
        dataCallBack.setId(download.getId());
        bundle.putParcelable(Extras.KEY_EXTRA_DATA, dataCallBack);
        intent.putExtras(bundle);
        return intent;
    }
}
