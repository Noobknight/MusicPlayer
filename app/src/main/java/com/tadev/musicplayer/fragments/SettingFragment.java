package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.helpers.LocaleHelper;
import com.tadev.musicplayer.interfaces.OnLanguageChangeListener;
import com.tadev.musicplayer.utils.support.ScreenUtils;
import com.tadev.musicplayer.utils.support.SharedPrefsUtils;
import com.tadev.musicplayer.utils.support.StringUtils;
import com.tadev.musicplayer.utils.support.Utils;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public class SettingFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {
    public static final String TAG = "SettingFragment";
    public static final String KEY_POSITION_QUALITY = "position_spinner";
    private AppCompatSpinner mSpinnerQuality;
    private AppCompatButton btnApplySettings;
    private RadioGroup rdgLanguage;
    private boolean isSelectChange, isSpinnerSelectChange;
    private OnLanguageChangeListener mLanguageChangeListener;
    private int positionSelected;
    private LinearLayout lrlSetting;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(View view) {
        mSpinnerQuality = (AppCompatSpinner) view.findViewById(R.id.fragment_setting_spnQuality);
        btnApplySettings = (AppCompatButton) view.findViewById(R.id.fragment_setting_btnApply);
        rdgLanguage = (RadioGroup) view.findViewById(R.id.fragment_setting_rdgLanguage);
        lrlSetting = (LinearLayout) view.findViewById(R.id.lrlSetting);
        RadioButton rdbEnglish = (RadioButton) view.findViewById(R.id.fragment_setting_rdbEnglish);
        RadioButton rdbVietNam = (RadioButton) view.findViewById(R.id.fragment_setting_rdbVietNam);
        boolean isLanguageVi = LocaleHelper.getLanguage(context).equals("vi");
        if (isLanguageVi) {
            rdbVietNam.setChecked(true);
        } else {
            rdbEnglish.setChecked(true);
        }
        mActivityMain.getToolbar().setTitle(StringUtils.getStringRes(R.string.fragment_setting));
        mActivityMain.getToolbar().setBackgroundColor(Utils.getColorRes(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lrlSetting.setPadding(0, ScreenUtils.getStatusBarHeight(context), 0, 0);
        }
    }

    @Override
    protected void initViewData() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.music_quality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerQuality.setAdapter(adapter);
        int position = SharedPrefsUtils.getIntegerPreference(context, KEY_POSITION_QUALITY, 0);
        if (position != 0) {
            mSpinnerQuality.setSelection(position);
        }
    }


    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mLanguageChangeListener = (OnLanguageChangeListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + "" +
                        " must be implements OnLanguageChangeListener");
            }
        }
        super.onAttach(context);
    }

    @Override
    protected void setViewEvents() {
        rdgLanguage.setOnCheckedChangeListener(this);
        mSpinnerQuality.setOnItemSelectedListener(this);
        btnApplySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSaveSuccess = false;
                if (isSelectChange || isSpinnerSelectChange) {
                    isSaveSuccess = SharedPrefsUtils.setIntegerPreference(context, KEY_POSITION_QUALITY, positionSelected);
                    int language = rdgLanguage.getCheckedRadioButtonId();
                    if (language == R.id.fragment_setting_rdbEnglish) {
                        LocaleHelper.setLocale(context, "en");
                    } else {
                        LocaleHelper.setLocale(context, "vi");
                    }
                }
                if (isSaveSuccess) {
                    Toast.makeText(context, StringUtils.getStringRes(R.string.save_setting_success),
                            Toast.LENGTH_SHORT).show();
                    mLanguageChangeListener.onLanguageChanged();
                }
            }
        });

    }

    @Override
    protected String TAG() {
        return "SettingFragment";
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        isSelectChange = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        isSpinnerSelectChange = true;
        positionSelected = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
