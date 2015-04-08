package com.qinchu.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qinchu.app.R;
import com.qinchu.app.db.UserProxy;
import com.qinchu.app.entity.User;
import com.qinchu.app.proxy.SettingProxy;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * <pre>
 * BMI值原来的设计是一个用于公众健康研究的统计工具。当我们需要知道肥胖是否为某一疾病的致病原因时，我们可以把病人的身高及体重换算成BMI值，
 * 再找出其数值及病发率是否有线性关连。不过，随着科技进步，现时BMI值只是一个参考值。要真正量度病人是否肥胖，体脂肪率比BMI更准确、
 * 而腰围身高比又比体脂肪率好、但是最好的看法是看内脏脂肪（若内脏脂肪正常，就算腰围很大及体脂肪率很高，健康风险不高，日本相扑很多都是这种胖法）。
 * 因此，BMI的角色也慢慢改变，从医学上的用途，变为一般大众的纤体指标。BMI是Body Mass Index 的缩写，BMI中文是“体质指数”的意思，是以你的身高体重计算出来的。
 * BMI是世界公认的一种评定肥胖程度的分级方法，世界卫生组织(WHO)也以BMI来对肥胖或超重进行定义。
 * 身高体重指数这个概念，是由19世纪中期的比利时通才凯特勒最先提出。它的定义如下：
 * 体质指数（BMI）=体重（kg）÷身高^2（m）
 * EX：70kg÷（1.75×1.75）=22.86
 * 举例：
 * 例如：一个人的身高为1.75米,体重为68千克，他的BMI=68/(1.75^2)=22.2（千克/米^2）当BMI指数为18.5～23.9时属正常。
 * BMI是与体内脂肪总量密切相关的指标,该指标考虑了体重和身高两个因素。BMI简单、实用、可反映全身性超重和肥胖。 在测量身体因超重而面临心脏病、高血压等风险时，比单纯的以体重来认定，更具准确性.
 *
 * 根据世界卫生组织定下的标准，亚洲人的BMI（体重指标BodyMassIndex)若高于22.9便属于过重。亚洲人和欧美人属于不同人种，WHO的标准不是非常适合中国人的情况，为此制定了中国参考标准：
 *      WHO标准    	亚洲标准     中国标准 	相关疾病发病危险性
 * 偏瘦	 <18.5       <18.5       <18.5           低（但其它疾病危险性增加）
 * 正常	18.5-24.9	18.5-22.9	18.5-23.9	     平均水平
 * 超重	 ≥25	      ≥23	       ≥24
 * 偏胖	25.0～29.9	23～24.9	     24～27.9	     增加
 * 肥胖	30.0～34.9	25～29.9      	≥28	         中度增加
 * 重度肥胖	35.0～39.9	≥30	       ——	         严重增加
 * 极重度肥胖	≥40.0	≥40.0          ≥40.0       非常严重增加
 * 最理想的体重指数是22。
 * </pre>
 */

public class BMIFragment extends Fragment {

    @InjectView(R.id.height)
    EditText heightEditText;
    @InjectView(R.id.weight)
    EditText weightEditText;
    @InjectView(R.id.bmiSeek)
    SeekBar bmiSeek;
    @InjectView(R.id.desc)
    TextView descTextView;

    public BMIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = UserProxy.getUser(SettingProxy.getUid());
        if (user != null) {
            heightEditText.setText(String.valueOf(user.getHeight()));
            weightEditText.setText(String.valueOf(user.getWeight()));
        }
        //这里用textwatch的话就不需要增加一个计算按钮了,用户体验比较好
        heightEditText.addTextChangedListener(new OnTextWatcher());
        weightEditText.addTextChangedListener(new OnTextWatcher());
    }

    class OnTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            resetValue();
        }
    }

    private void resetValue() {
        //这里的try catch 是保证了一些非法输入的时候不会报错
        try {

            descTextView.setVisibility(View.VISIBLE);
            bmiSeek.setVisibility(View.VISIBLE);

            User user = UserProxy.getUser(SettingProxy.getUid());
            if (user != null) {
                String weightString = weightEditText.getText().toString().trim();
                int weight = -1;
                if (!TextUtils.isEmpty(weightString)) {
                    weight = Integer.valueOf(weightString);
                }
                String heightString = heightEditText.getText().toString().trim();
                int height = -1;
                if (!TextUtils.isEmpty(heightString)) {
                    height = Integer.valueOf(heightString);
                }
                user.setHeight(height);
                user.setWeight(weight);

                //体质指数（BMI）=体重（kg）÷身高^2（m）
                float bmi = (float) weight / (float) Math.pow(((float) height / 100.0f), 2);
                bmiSeek.setProgress(Float.valueOf(bmi).intValue());
                String descValue = String.format("%.2f", bmi);
                String desc = null;
                //稍有标准判断不同,保证值在区间内
                if (bmi < 18.5) {
                    desc = "偏瘦,其它疾病危险性增加";
                } else if (bmi >= 18.5 && bmi < 24) {
                    desc = "正常";
                } else if (bmi >= 24 && bmi < 28) {
                    desc = "偏胖,相关疾病发病危险性增加";
                } else if (bmi >= 28 && bmi < 40) {
                    desc = "肥胖,相关疾病发病危险性中度增加";
                } else if (bmi >= 40) {
                    desc = "极重度肥胖,相关疾病发病危险性非常严重增加";
                }
                if (desc != null) {
                    descTextView.setText(descValue + " : " + desc);
                }
            }
        } catch (Exception e) {
            descTextView.setVisibility(View.INVISIBLE);
            bmiSeek.setVisibility(View.INVISIBLE);
        }

    }

}
